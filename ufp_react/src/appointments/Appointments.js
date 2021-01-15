//https://codesandbox.io/s/github/fullcalendar/fullcalendar-example-projects/tree/master/react?file=/src/DemoApp.jsx:3182-3189

import React, { useEffect } from 'react'
import {useState} from "react";

import FormLogin from './FormLogin'
import CalendarComponent from './CalendarComponent'
import TeacherBackOffice from './TeacherBackOffice';

const Appointments= (props)=>{

  const [isTeacher, setIsTeacher] = useState(false);
  const [token,setTokenState]=useState('');
  const isLoggedIn=props.isLoggedIn;
  const setIsLoggedIn=props.setIsLoggedIn;
  const username=props.username;
  const setUsername=props.setUserName;

  useEffect(()=>{
    const token=localStorage.getItem("token");
    if(token){
      setIsLoggedIn(true);
    }
  },[setIsLoggedIn])

     return (
      <div>        
      <Greeting isLoggedIn={isLoggedIn} setIsLoggedIn={setIsLoggedIn} setUsername={setUsername} username={username} setTokenState={setTokenState} token={token} setIsTeacher={setIsTeacher} isTeacher={isTeacher}/>
      </div>
      )    
}

function Greeting(props) {
  const isLoggedIn = props.isLoggedIn;
  if (!isLoggedIn) {
    return <FormLogin setTokenState={props.setTokenState} setUsername={props.setUsername} username={props.username} setIsLoggedIn={props.setIsLoggedIn} setIsTeacher={props.setIsTeacher} />;
  }  
  if(props.isTeacher || localStorage.getItem("isTeacher")==="true"){
    return <TeacherBackOffice token={props.token} username={props.username} setIsLoggedIn={props.setIsLoggedIn}/>
  }
  return <CalendarComponent token={props.token} setIsLoggedIn={props.setIsLoggedIn} username={props.username}/>;
}

export default Appointments;