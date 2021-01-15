package me.afmiguez.project.ufp_applications.appointments.infrastructure.calendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import me.afmiguez.project.ufp_applications.appointments.domain.models.Appointment;
import me.afmiguez.project.ufp_applications.appointments.domain.usecases.interfaces.CalendarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Profile({"dev","prod"})
@Service
public class GoogleCalendarService implements CalendarService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private Calendar calendar;

    @Value("${me.edu.afmiguez.calendarId}")
    private String gcalId;

    private final Set<String> SCOPES = CalendarScopes.all();

    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */

    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        String CREDENTIALS_FILE_PATH = "/credentials.json";
        InputStream in = GoogleCalendarTest.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            logger.error("Should get a credentials file from Google Console");
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        String TOKENS_DIRECTORY_PATH = "tokens";
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

    }

    public GoogleCalendarService() {


        final NetHttpTransport HTTP_TRANSPORT;
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            String APPLICATION_NAME = "Google Calendar API Java Quickstart";
            this.calendar = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    public boolean createGCalEvent(Appointment appointment) {

        Event event = this.convertAppointmentToEvent(appointment);
        logger.info("try to schedule appointment: " + appointment.toString());
        if(appointment.getStudent()!=null && appointment.getStudent().getUsername()!=null && appointment.getStudent().getFullName()!=null) {
            logger.info("Student: " + appointment.getStudent().getUsername()+" "+appointment.getStudent().getFullName());
        }else{
            logger.error("Student not found");
        }
        if(appointment.getTeacher()!=null && appointment.getTeacher().getUsername()!=null) {
            logger.info("Teacher: " + appointment.getTeacher().getUsername());
        }else{
            logger.error("Teacher not found");
        }
        if(null == this.calendar){
            logger.error("Error loading calendar");
            return false;
        }
        try {
            logger.info(event.toString());
            this.calendar.events().insert("Atendimento " + appointment.getStudent().getFullName(), event).setCalendarId(gcalId).execute();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    public boolean removeGCalEvent(Appointment appointment) {

        logger.info("try to cancel appointment: " + appointment.toString());
        if(null == this.calendar){
            logger.error("Error loading calendar");
            return false;
        }
        Optional<Event> event = this.findEventByAppointment(appointment);
        if (event.isPresent()) {
            try {
                this.calendar.events().delete("Atendimento " + appointment.getStudent().getFullName(), event.get().getId()).setCalendarId(gcalId).execute();
                return true;
            } catch (IOException e) {
                logger.warn("Cannot delete the event");
                logger.error(e.getMessage());
            }
        }
        return false;
    }

    public Optional<Event> findEventByAppointment(Appointment appointment) {
        Event event = convertAppointmentToEvent(appointment);
        List<Event> events = this.listEvents(gcalId);
//        events=this.filterEvents(events,appointment.getStartTime().toLocalDate());
        for (Event ev : events) {
            if (ev.getStart().equals(event.getStart()) && ev.getEnd().equals(event.getEnd())) {
                logger.info("Event found "+event.toString());
                return Optional.of(ev);
            }
        }
        logger.info("Did not find event "+event.toString());
        return Optional.empty();
    }

    public List<Event> filterEvents(List<Event> events, LocalDate date){
        List<Event> filteredEvents=new ArrayList<>();
        for(Event event:events){
            long eventEpochValue=event.getStart().getDateTime().getValue();
            LocalDate eventDate= Instant.ofEpochMilli(eventEpochValue).atZone(ZoneId.systemDefault()).toLocalDate();
            if(eventDate.equals(date)){
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    private List<Event> listEvents(String calendarId) {
        try {
            return this.calendar.events().list(calendarId).setMaxResults(1000).execute().getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private Event convertAppointmentToEvent(Appointment appointment) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        //TimeZone.getTimeZone( "Europe/Lisbon").inDaylightTime( appointment.getStartTime().toLocalDate());
        ZoneId z = ZoneId.of("Europe/Lisbon");
        ZoneRules zoneRules = z.getRules();
        boolean isDst = zoneRules.isDaylightSavings( appointment.getStartHour().toInstant(ZoneOffset.UTC) );

        Appointment appointmentClone;
        if(isDst ){
            appointmentClone=appointment.convertDST();
        }else{
            appointmentClone=Appointment.builder()
                    .startHour(appointment.getStartHour())
                    .expectedEndTime(appointment.getExpectedEndTime())
                    .student(appointment.getStudent())
                    .teacher(appointment.getTeacher())
                    .build();
        }

        DateTime startDateTime = DateTime.parseRfc3339(formatter.format(appointmentClone.getStartHour()));
        Event event = new Event().setSummary("Atendimento " + appointmentClone.getStudent().getFullName());
        EventDateTime start = new EventDateTime().setDateTime(startDateTime);
        event.setStart(start);

        DateTime endDateTime = DateTime.parseRfc3339(formatter.format(appointmentClone.getExpectedEndTime()));
        EventDateTime end = new EventDateTime().setDateTime(endDateTime);
        event.setEnd(end);

        return event;
    }
}
