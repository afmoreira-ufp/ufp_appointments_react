import {useState} from "react";


import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Card from 'react-bootstrap/Card';
import Accordion from 'react-bootstrap/Accordion';
import {createRequest } from "../request";


const AssociateStudentsToCourse=props=>{

    const token=props.token?props.token:localStorage.getItem("token");

    const teacher=props.teacher;

    const [selectedCourse,setSelectedCourse]=useState("");
    const [students,setStudents]=useState([]);


    const changeSelectedCourse=(event)=>{
        const courseName=event.target.value;
        setSelectedCourse(courseName);
        if(courseName){
            createRequest(encodeURI("/course/"+courseName),"GET",null,token,(response)=>{setStudents(response.students);console.log(response)},console.log);
        }

    }

    const getTeacherCourses=()=>{        
        if(teacher){
            return teacher.courses;
        }
        return [];
    }    

    const associateStudents=()=>{
        const request={
            course:{name:selectedCourse},
            students:students
        };
        
        createRequest("/course","PUT",request,token,(response)=>{console.log(response)},console.log);
    }

    const updateStudents=(event)=>{
        setStudents(event.target.value.split("\n"));
    }


    return (
        <Accordion defaultActiveKey="0">
            <Card>
                <Card.Header>
                <Accordion.Toggle as={Button} variant="link" eventKey="1">
                    Associate Students to Course
                </Accordion.Toggle>
                </Card.Header>
                <Accordion.Collapse eventKey="1">
                <Card.Body>
                    <Form>
                        <Form.Group as={Row} controlId="select_student_course">

                        <Col lg="8">
                            <textarea rows="10" cols="70" onChange={updateStudents} value={students.join("\n")}/>
                        </Col>
                        <Col lg="4">
                            <Form.Control as="select" onChange={changeSelectedCourse}>
                                <option value="">Select option</option>
                                {
                                
                                getTeacherCourses()?getTeacherCourses().map((course,index)=>{
                                    return (
                                        <option id={index} key={index} value={course.name}>{course.name} </option>
                                    )
                                    }):<option value="">Select option</option>                                        
                                }
                            </Form.Control>
                        </Col>
                        </Form.Group>
                        <Button onClick={associateStudents}>Associate</Button>
                    </Form>
                </Card.Body>
                </Accordion.Collapse>
            </Card>
        </Accordion>
    )
}

export {AssociateStudentsToCourse};