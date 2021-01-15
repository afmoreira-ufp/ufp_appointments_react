import {useState,useEffect} from "react";
import {urlBase} from "../request";
import Container from 'react-bootstrap/Container';
import AddExtraScheduleForm from './AddExtraScheduleForm';
import AddCourseToTeacher from './AddCourseToTeacher';
import {AssociateStudentsToCourse} from './AssociateStudentsToCourse';
import ListAppointment from './ListAppointment';

const TeacherBackOffice = props=>{
    const [courses,setCourses] =useState([]);
    const [teacher,setTeacher]=useState({});
    const [appointments,setAppointments]=useState([]);
    const username=props.username?props.username:localStorage.getItem("username");
    const token=props.token?props.token:localStorage.getItem("token");  
    
    if(!localStorage.getItem("token")){
        localStorage.setItem("token",token);
    }
    if(!localStorage.getItem("username")){
        localStorage.setItem("username",username);
    }

    const handleTeacherAndCoursesResponse=(data)=>{
        const teacher=data[0];
        const courses=data[1];
        const appointments=data[2];
        setAppointments(appointments);
        setTeacher(teacher);
        localStorage.setItem("teacher",JSON.stringify(teacher));
        if(courses){
            setCourses(
                courses.map((course)=>{
                    const courseName=course.name;
                    const checked=teacher.courses?teacher.courses.some((course)=>course.name===courseName):false;
                    return {
                        name:course.name,
                        checked:checked
                    }
                })
            )
        }

    }

    useEffect(()=>{       
        Promise.all([
            fetch(urlBase+"/api/teacher/name/"+username,{
                method:"GET",              
                mode: 'cors',
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin':'*',
                    'Authorization':token,
                }
            }),
            fetch(urlBase+"/api/course",{
                method:"GET",              
                mode: 'cors',
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin':'*',
                    'Authorization':token,
                }
            }),
            fetch(urlBase+"/api/teacher/"+username+"/nextAppointments",{
                method:"GET",
                mode: 'cors',
                headers: {
                    'Content-Type': 'application/json',
                    'Access-Control-Allow-Origin':'*',
                    'Authorization':token,
                }
            }),
        ]).then((responses)=>{
            return Promise.all(responses.map(function (response) {
                return response.json();
            }));
        }).then((data)=>{
            handleTeacherAndCoursesResponse(data);
        });  

      },[token,username])



    return(
        <Container>
        
        <AssociateStudentsToCourse courses={courses} setCourses={setCourses} setTeacher={setTeacher} teacher={teacher} token={token} username={username} />
        
        <AddCourseToTeacher courses={courses} setCourses={setCourses} setTeacher={setTeacher} teacher={teacher} token={token} username={username} />

        <AddExtraScheduleForm courses={courses} setCourses={setCourses} setTeacher={setTeacher} teacher={teacher} token={token} username={username} />

        <ListAppointment appointments={appointments} />
    </Container>
    )
}

export default TeacherBackOffice;