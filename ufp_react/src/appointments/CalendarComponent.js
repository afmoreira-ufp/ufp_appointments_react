import React, {useState,useEffect,} from "react";

import {createRequest} from '../request';

import FullCalendar from '@fullcalendar/react'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'

import Col from 'react-bootstrap/Col';
import Form from 'react-bootstrap/Form';
import Container from 'react-bootstrap/Container';

import 'moment-timezone';
import moment from 'moment';
import { Row } from "react-bootstrap";

const CalendarComponent =props=>{

  const token=props.token?props.token:localStorage.getItem("token");
  const username=props.username?props.username:localStorage.getItem("username");

  const calendarRef = React.createRef();
  const [weekendsVisible]=useState(false);
  const [currentEvents, setCurrentEvents]=useState([]);
  const [teachers,setTeachers]=useState([]);
  const [teacherUsername, setTeacherUsername]=useState('');
  const [businessHours, setBusinessHours]=useState([]);

  if(!localStorage.getItem("token")){
    localStorage.setItem("token",token);
  }
  if(!localStorage.getItem("username")){
      localStorage.setItem("username",username);
  }

  useEffect(()=>{
    createRequest("/teacher","get",null,token,response=>{
      setTeachers(response);
    },alert)
       

  },[token])
  
    const handleDateSelect =(selectInfo)=> {
      const calendarApi = selectInfo.view.calendar
  
      calendarApi.unselect() // clear date selection
  
        const appointment={          
          startHour:moment(new Date(selectInfo.startStr)).format("YYYY-MM-DD HH:mm:ss"),
          studentUsername:username,
          teacherUsername:teacherUsername
        }

        createRequest("/appointment","POST",appointment,token,
        (response)=>{
          const event={
            id:response.id,
            title:"My appointment",
            start: selectInfo.startStr,
            end: selectInfo.endStr,
            allDay: selectInfo.allDay
          };
          calendarApi.addEvent(event);

          setCurrentEvents([...currentEvents,event])
        },

            (error)=>{alert(error)}
        );
    }

    const resetTeacherInformation=(event)=>{
      setTeacherUsername(event.target.value);

      currentEvents.forEach((appointment)=>{
        appointment.remove();
      })
      setCurrentEvents([]);
    }

    const getRegularSchedulesAsBusinessHours=(schedules)=>{
      return schedules.filter((schedule)=>schedule.type==="standard").map((schedule)=>{
        return {
          daysOfWeek:[getDowDict()[schedule.dayOfWeek]],
          startTime:schedule.startTime,
          endTime:schedule.endTime
        }
      });
    }

    const getDowDict = ()=> {
      let dowDict = [];
      dowDict["SUNDAY"] = 0;
      dowDict["MONDAY"] = 1;
      dowDict["TUESDAY"] = 2;
      dowDict["WEDNESDAY"] = 3;
      dowDict["THURSDAY"] = 4;
      dowDict["FRIDAY"] = 5;
      dowDict["SATURDAY"] = 6;
      return dowDict;
      };

    const dayBetweenPeriod=(schedule)=>{
      const startDate=moment(new Date(schedule.startDate));
      const endDate=moment(new Date(schedule.endDate));

      let days=[];

      while(startDate.isSameOrBefore(endDate)){
        if(startDate.day()===getDowDict()[schedule.dayOfWeek]){
          days.push(startDate.clone());
        }
        startDate.add(1,'days');
      }
      return days;
    }

    const getExtraordinarySchedulesAsEvents=(schedules)=>{
      return schedules.filter((schedule)=>schedule.type!=="standard").map((schedule)=>{

        const days=dayBetweenPeriod(schedule);
        
        const startTime=moment(schedule.startTime,"HH:mm");
        const endTime=moment(schedule.endTime,"HH:mm");
        
        return days.map((day)=>{
          return {
            start: day.set('hour',startTime.get('hour')).set('minute',startTime.get('minute')).format(),
            end: day.set('hour',endTime.get('hour')).set('minute',endTime.get('minute')).format(),
            display: 'background',
            backgroundColor: "#FF0000"
          }
        })        
      }).flat();

    }

    const addTeacherRegularSchedules=(teacherUsername)=>{
      const teacher=getTeacher(teacherUsername);
      const teacherSchedules=teacher.schedules;
      if(teacherSchedules){
        const schedules=getRegularSchedulesAsBusinessHours(teacherSchedules);
        setBusinessHours(schedules);
      }
    }

    const addTeacherExtraordinarySchedules=(teacherUsername)=>{
      const teacher=getTeacher(teacherUsername);
      const teacherSchedules=teacher.schedules;
      const extraordinaryScheduleAsEvents=getExtraordinarySchedulesAsEvents(teacherSchedules);

      const calendarApi = calendarRef.current.getApi();
      calendarApi.unselect() // clear date selection
      if(extraordinaryScheduleAsEvents){        
        setCurrentEvents(extraordinaryScheduleAsEvents);
        extraordinaryScheduleAsEvents.forEach((schedule)=>{
          calendarApi.addEvent(schedule);            
        });
      }
      
    }

    const addTeacherAppointments=(teacherUsername)=>{
      const teacher=getTeacher(teacherUsername);
      const teacherAppointments=teacher.appointments;
      if(teacherAppointments){
        const events=teacherAppointments.map((appointment)=>{
          const {id,startHour,expectedEndHour,studentUser}=appointment;
          return {
            id:id,
            title:username===studentUser?"My appointment":"Busy",
            start:moment(new Date(startHour)).format(),
            end:moment(new Date(expectedEndHour)).format()
          }                  
        })

        let calendarApi = calendarRef.current.getApi();  
        calendarApi.unselect() // clear date selection
        setCurrentEvents(events);
        events.forEach((event)=>{
          calendarApi.addEvent(event);
        })
        
      }
    }

    const getTeacher=(username)=>{
      const teacher=teachers.filter(
          (teacher)=>teacher.username===username
      );
      if(teacher.length!==0){
        return teacher[0];
      }
      return []
    }
  
    const handleEventClick =(clickInfo)=> {
      if (window.confirm(`Are you sure you want to delete the event '${clickInfo.event.title}'`)) {

        createRequest(
            "/appointment/"+clickInfo.event.id,
            "DELETE",null,token,()=>{setCurrentEvents([currentEvents.filter((event)=>event.id!==clickInfo.event.id)])
        clickInfo.event.remove()},
            (error)=>{alert(error)}
        )

      }
    }
  
    const handleEvents =(events)=> {
      setCurrentEvents( events)
    }
  
  
    const renderEventContent=(eventInfo)=> {
    return (
      <>
        <b>{eventInfo.timeText}</b>
        <i>{eventInfo.event.title}</i>
      </>
    )
  }

  const populateTeacherAppointments=(event)=>{
    setCurrentEvents([]);

    resetTeacherInformation(event);
    const teacherUsername=event.target.value;

    addTeacherRegularSchedules(teacherUsername);
    addTeacherExtraordinarySchedules(teacherUsername);
    addTeacherAppointments(teacherUsername);
  }
    
      return <Container>
        <Row>
          <Col sm="4">
          <Form.Control as="select" onChange={populateTeacherAppointments}>
            <option value="">Select teacher</option>
            {
              teachers?teachers.map((option,index)=>{
              return (
                <option id={index} key={index} value={option.username}>{option.username} </option>
              )
            }):[]
            }
          </Form.Control>
          </Col>
          </Row>
      
          <FullCalendar
          ref={calendarRef}
          plugins={[dayGridPlugin, timeGridPlugin, interactionPlugin]}
          headerToolbar={{
              left: 'prev,next today',
              center: 'title',
              right: 'dayGridMonth,timeGridWeek,timeGridDay'
          }}
          initialView='timeGridWeek'
          editable={true}
          selectable={true}
          selectMirror={true}
          dayMaxEvents={true}
           weekends={weekendsVisible}
          businessHours={businessHours}
          displayEventTime={false}
          
          select={handleDateSelect}
          eventContent={renderEventContent} // custom render function
          eventClick={handleEventClick}
          eventsSet={handleEvents} // called after events are initialized/added/changed/removed
          
          />
        </Container>;
}

export default CalendarComponent;