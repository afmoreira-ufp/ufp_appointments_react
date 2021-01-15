import React, {useState} from "react";

import {createRequest} from '../request';

import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import Accordion from 'react-bootstrap/Accordion';
import MaterialTable from "material-table";

const AddExtraScheduleForm=props=>{

    const [selectedCourse,setSelectedCourse]=useState("");
    const [startDate,setStartDate]=useState("");
    const [endDate,setEndDate]=useState("");
    const [startTime,setStartTime]=useState("");
    const [endTime,setEndTime]=useState("");  
    const [dayOfWeek, setDayOfWeek]  =useState("");


    const row=React.useRef([]);
    const handleClick = rows => {
        row.current = rows?rows.map(r=>r.id):[];
    };

    const token=props.token?props.token:localStorage.getItem("token");

    const teacher=props.teacher;
    const setTeacher=props.setTeacher;

    const getExtraordinarySchedules=()=>{
        if(teacher && teacher.schedules){
            return teacher.schedules.filter((schedule)=>schedule.type!=='standard');
        }
        return [];
    }

    const columnsName=["id","courseName","day","startDate","endDate","startTime","endTime"]
    const columns=columnsName.map((columnName)=>{return {title:columnName,field:columnName}});

    const data=getExtraordinarySchedules()?getExtraordinarySchedules().map((sch)=>{
        return {id:sch.id,courseName:sch.courseDTO.name,day:sch.dayOfWeek,startDate:sch.startDate,endDate:sch.endDate,startTime:sch.startTime,endTime:sch.endTime}
    }):[]

    const getTeacherCourses=()=>{        
        if(teacher){
            return teacher.courses;
        }
        return [];
    }    

    const changeStartTime=(event)=>{
        setStartTime(event.target.value)
    }
    
    const changeEndTime=(event)=>{
        setEndTime(event.target.value)
    }
    
    const changeStartDate=(event)=>{
        setStartDate(event.target.value)
    }
    
    const changeEndDate=(event)=>{
        setEndDate(event.target.value)
    }

    const changeDayOfWeek=(event)=>{
        setDayOfWeek(event.target.value)
    }
    const changeSelectedCourse=(event)=>{
        setSelectedCourse(event.target.value);
    }
    
    const insertNewExtraordinarySchedule=()=>{
        const request=[{
            dayOfWeek:dayOfWeek,
            startDate:startDate,
            endDate:endDate,
            startTime:startTime,
            endTime:endTime,
            courseDTO:{
                name:selectedCourse
            }
        } ];
        
        createRequest("/teacher/"+teacher.id+"/extraordinary","PATCH",request,token,(response)=>setTeacher(response),console.log);
    }
    
    const getDaysOfWeek=()=>{
        return ["MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY"];
    }

    const removeRow=()=>{
        createRequest("/teacher/name/"+teacher.username+"/removeExtra","put",row.current,token,(response)=>setTeacher(response),alert);
    }


    return(
        <Accordion defaultActiveKey="0">        
            <Card>
                <Card.Header>
                <Accordion.Toggle as={Button} variant="link" eventKey="1">
                    Include Extra Schedule to Teacher
                </Accordion.Toggle>
                </Card.Header>
                <Accordion.Collapse eventKey="1">
                <Card.Body>
                    <Form>
                        <Form.Group as={Row} controlId="extra_schedule_form">
                            <Container>
                            <Row className="justify-content-md-center">
                                <Col lg="4">
                                    <input type="date" onChange={changeStartDate}/>
                                </Col>
                                <Col lg="4">
                                    <input type="date" onChange={changeEndDate}/>
                                </Col>
                                <Col lg="4">
                                    <Form.Control as="select" onChange={changeDayOfWeek}>
                                        <option id="-1" value="">Select option</option>
                                        {getDaysOfWeek().map((dayOfWeek,index)=>{
                                            return (
                                                <option id={index} key={index} value={dayOfWeek}>{dayOfWeek} </option>
                                            )
                                            })}
                                    </Form.Control>
                                </Col>
                            </Row>
                                <Form.Group as={Row} controlId="extra_schedule_course_form">
                                <Col lg="4">
                                    <input type="time" onChange={changeStartTime} />
                                </Col>
                                <Col lg="4">
                                    <input type="time" onChange={changeEndTime}/>
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
                            <Row className="justify-content-md-center">
                            <Button lg="4" onClick={insertNewExtraordinarySchedule}>Insert</Button>
                            </Row>
                            </Container>                               
                        </Form.Group>
                    </Form>
                    <Row>
                    <Container>
                        <ul>{

                            <MaterialTable onSelectionChange={handleClick} columns={columns} data={data} options={{selection:true,paging:false,showTitle:false,search:false}} />}

                            <Button onClick={()=>removeRow()} >Remove Schedules </Button>

                        </ul>
                    </Container>
                    </Row>
                </Card.Body>
                </Accordion.Collapse>
            </Card>
        </Accordion>
    )
}

export default AddExtraScheduleForm;