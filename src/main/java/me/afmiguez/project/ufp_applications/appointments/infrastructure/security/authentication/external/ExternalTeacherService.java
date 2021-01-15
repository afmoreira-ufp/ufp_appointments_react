package me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external;

import me.afmiguez.project.ufp_applications.appointments.Utils;
import me.afmiguez.project.ufp_applications.appointments.domain.models.AbstractSchedule;
import me.afmiguez.project.ufp_applications.appointments.domain.models.RegularSchedule;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Teacher;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.models.DocenteDetailsJson;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.models.DocenteJSON;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.models.DocentesJSON;
import me.afmiguez.project.ufp_applications.appointments.infrastructure.security.authentication.external.models.ScheduleSoap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import ufp.wsdl.Docente;
import ufp.wsdl.DocenteResponse;
import ufp.wsdl.Docentes;
import ufp.wsdl.DocentesResponse;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


public class ExternalTeacherService extends SoapGeneric {
    @Value("${wsdl.action.docentes}")
    private final String docentesAction = "http://tempuri.org/docentes";

    @Value("${wsdl.action.docente}")
    private final String docenteAction = "http://tempuri.org/docente";

    public DocentesJSON getDocentes()  {

        Docentes docentes = new Docentes();
        DocentesResponse response = (DocentesResponse) getWebServiceTemplate()
                .marshalSendAndReceive(this.getUrl(), docentes
                        , new SoapActionCallback(docentesAction)
                );
        try {
            return Utils.getValue(response.getDocentesResult(), DocentesJSON.class);
        } catch (IOException e) {
            return null;
        }
    }

    public DocenteDetailsJson getDocenteInformationByUsername(String username){
        DocentesJSON docentesJSON=getDocentes();
        if(docentesJSON!=null){
            for(DocenteJSON docenteJSON:docentesJSON.getDocentes()){
                DocenteDetailsJson docenteDetailsJson=getDocenteInformation(docenteJSON.getSigla());
                if(docenteDetailsJson!=null) {
                    if(docenteDetailsJson.getEmail().contains(username)){
                        return docenteDetailsJson;
                    }
                }
            }
        }
        return null;
    }

    public Teacher getTeacherFromExternal(String username){
        if(username.equals("teacher")){
            List<AbstractSchedule> schedules=Arrays.asList(
                    RegularSchedule.builder()
                            .dayOfWeek(DayOfWeek.MONDAY)
                            .startTime(LocalTime.of(10,0))
                            .endTime(LocalTime.of(18,0))
                            .build(),
                    RegularSchedule.builder()
                            .dayOfWeek(DayOfWeek.TUESDAY)
                            .startTime(LocalTime.of(10,0))
                            .endTime(LocalTime.of(18,0))
                            .build()
            );
            Teacher teacher=Teacher.builder()
                    .fullName("Teacher")
                    .username("teacher").build();
            schedules.forEach(teacher::addSchedule);
            return teacher;
        }
        DocenteDetailsJson docenteDetailsJson=getDocenteInformationByUsername(username);
        if(docenteDetailsJson!=null){
            List<RegularSchedule> regularSchedules=docenteDetailsJson.getSchedule().stream().map(scheduleSoap -> RegularSchedule.builder()
                    .startTime(scheduleSoap.getStartTime())
                    .endTime(scheduleSoap.getEndTime())
                    .dayOfWeek(scheduleSoap.getDayOfWeek())
                    .build()).collect(Collectors.toList());
            Teacher teacher= Teacher.builder()
                    .username(username)
                    .fullName(docenteDetailsJson.getName())
                    .build();

            regularSchedules.forEach(teacher::addSchedule);

            return teacher;
        }
        return null;
    }


    public DocenteDetailsJson getDocenteInformation(String initial){
        Docente docente = new Docente();
        docente.setSigla(initial);
        DocenteResponse docenteResponse = (DocenteResponse) getWebServiceTemplate()
                .marshalSendAndReceive(this.getUrl(), docente
                        , new SoapActionCallback(docenteAction)
                );

        DocenteJSON docenteJSON1 = new DocenteJSON();
        docenteJSON1.setDocente(docenteResponse.getDocenteResult());

        Document doc= Jsoup.parse(docenteResponse.getDocenteResult());
        String name=doc.getElementsByTag("b").text();

        String email="";

        LocalDate last_update=null;
        List<Element> elements=doc.getElementsByTag("p");
        for(Element schedule:elements){
            if(schedule.wholeText().contains("Correio Electrónico:")) {
                email=schedule.wholeText().split("Correio Electrónico:")[1].trim();
                if(email.contains(". ")){
                    String[] emailSplit = email.split(". ");
                    email=emailSplit[0];
                }
                if(email.endsWith(".")){
                    email=email.substring(0,email.length()-1);
                }
            }
            if(schedule.wholeText().contains("Última actualização: ")){
                String date=schedule.wholeText().split("Última actualização: ")[1].trim();
                last_update=LocalDate.parse(date);
            }
        }

        List<ScheduleSoap> schedules=new ArrayList<>();
        List<Element> schdeulesHTML=doc.getElementsByTag("li");
        for(Element schedule:schdeulesHTML){
            String[] scheduleArr=schedule.wholeText().split("das");
            DayOfWeek dayOfWeek=translateDays.get(scheduleArr[0].trim());
            String[] hoursArr=scheduleArr[1].split("às");
            LocalTime startHour=LocalTime.parse(hoursArr[0].trim());
            LocalTime endHour=LocalTime.parse(hoursArr[1].trim());
            schedules.add(ScheduleSoap.builder().dayOfWeek(dayOfWeek).startTime(startHour).endTime(endHour).build());
        }
        return DocenteDetailsJson.builder().email(email).name(name).schedule(schedules).last_update(last_update).build();
    }

    private static final Map<String, DayOfWeek> translateDays=new HashMap<>();
    static{
        translateDays.put("Segunda", DayOfWeek.MONDAY);
        translateDays.put("Terça", DayOfWeek.TUESDAY);
        translateDays.put("Quarta", DayOfWeek.WEDNESDAY);
        translateDays.put("Quinta", DayOfWeek.THURSDAY);
        translateDays.put("Sexta", DayOfWeek.FRIDAY);
        translateDays.put("Sábado", DayOfWeek.SATURDAY);
        translateDays.put("Domingo", DayOfWeek.SUNDAY);

    }

}
