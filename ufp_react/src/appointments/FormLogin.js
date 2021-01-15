import React, {useState} from "react";
import {createRequestLogin} from '../request';

import Form from 'react-bootstrap/Form';
import Col from 'react-bootstrap/Col';
import Button from 'react-bootstrap/Button';

const FormLogin =props=>{
  const [password,setPassword]=useState("");
  
  const setTokenState=props.setTokenState;
  const username=props.username;
  const setUsername=props.setUsername
  const setIsLoggedIn=props.setIsLoggedIn;
  const setIsTeacher=props.setIsTeacher;

  const handleLogin=(response)=>{
    const headers=response.headers;
    const token=headers.get("Authorization");
    const userType=headers.get("USER_TYPE");
    if(userType==="teacher"){
      setIsTeacher(true);
      localStorage.setItem("isTeacher",true);
    }else{
      setIsTeacher(false);
      localStorage.setItem("isTeacher",false);              
    }
    localStorage.setItem("isLoggedIn","true");
    setTokenState(token);
    setIsLoggedIn(true);
    setUsername(username);            
    return response
  }

  const login=()=>{
    const credentials={
        username:username,
        password:password
      }
        
     createRequestLogin("/login","POST",credentials,
      handleLogin,()=>alert("Login failed"))
    }

    return(
      
        <Form>        
        <Col sm="2">
          <Form.Control type="email" placeholder="Student Number" value={username} onChange={e => setUsername(e.target.value)} />
        </Col>
      
        <Col sm="2">
          <Form.Control type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} />
        </Col>
        <Col sm="2">
            <Button id="login" variant="primary" type="button" onClick={login}>
            Login
            </Button>          
        </Col>
            
      </Form>
    )
    }
export default FormLogin