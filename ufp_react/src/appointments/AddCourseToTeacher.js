import React from "react";
import {createRequest} from '../request';

import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Card from 'react-bootstrap/Card';
import Accordion from 'react-bootstrap/Accordion';

const AddCourseToTeacher = props=>{

    const token=props.token?props.token:localStorage.getItem("token");
    const username=props.username;

    const courses=props.courses;
    const setCourses=props.setCourses;
    
    const teacher=props.teacher?props.teacher:JSON.parse(localStorage.getItem("teacher"));
    const setTeacher=props.setTeacher;

    if(!localStorage.getItem("teacher")){
        localStorage.setItem("teacher",JSON.stringify(teacher));
    }  


    const updateCourses=()=>{
        const selectedCourses=courses
        .filter((item)=> {return item.checked})
        .map((item,index)=> {return {name:item.name}});

        createRequest(
            "/teacher/name/"+username+"/course",
            "PUT",
            selectedCourses,
            token,
            (response)=>{setTeacher(response)},
            console.log
        );

        
    }    
    const handleCheckboxChange=(event)=>{
    var isChecked = event.target.checked;
    var item = event.target.name;
    if(courses){
        setCourses(courses.map((course)=>{
                return{
                    name:course.name,
                    checked:item===course.name?isChecked:course.checked
                }
            })
        );
    }
    }

    return (
        <Accordion defaultActiveKey="0">
            <Card>
                <Card.Header>
                <Accordion.Toggle as={Button} variant="link" eventKey="1">
                    Associate Teacher to Course
                </Accordion.Toggle>
                </Card.Header>
                <Accordion.Collapse eventKey="1">
                <Card.Body>
                    <Form>
                        <Form.Group as={Row} >
                        {                      
                            courses.map((course,index)=>{                                
                                return (<Form.Check 
                                type='checkbox'
                                id={index}
                                key={index}
                                label={course.name}
                                checked={course.checked}
                                name={course.name}
                                onChange={handleCheckboxChange}
                            />)
                            })
                        }
                        </Form.Group>
                        <Button onClick={updateCourses}>Update Courses</Button>
                    </Form>
                </Card.Body>
                </Accordion.Collapse>
            </Card>

        </Accordion>
    )
}

export default AddCourseToTeacher;