import Card from "react-bootstrap/Card";
import Accordion from "react-bootstrap/Accordion";
import Button from "react-bootstrap/Button";
import Form from "react-bootstrap/Form";
import Row from "react-bootstrap/Row";
import React from "react";
import MaterialTable from "material-table";
import {Container} from "react-bootstrap";


const ListAppointment=(props)=>{

    const appointments=props.appointments?props.appointments:[];

    const columnNames=["id","studentName","studentNumber","day","date","time"];
    const columns=columnNames.map((columnName)=>{return {title:columnName,field:columnName}})

    return (
        <Accordion defaultActiveKey="0">
            <Card>
                <Card.Header>
                    <Accordion.Toggle as={Button} variant="link" eventKey="1">
                        List Appointments
                    </Accordion.Toggle>
                </Card.Header>
                <Accordion.Collapse eventKey="1">
                    <Card.Body>
                        <Form>
                            <Form.Group as={Row} >
                                {
                                    <Container>
                                        <Row> #Appointments: {appointments.filter((appointment)=>appointment.studentNumber!=="27583").length}</Row>
                                        <MaterialTable columns={columns} data={appointments.filter((appointment)=>appointment.studentNumber!=="27583")} options={{paging:false,showTitle:false,search:false}}/>
                                    </Container>
                                }
                            </Form.Group>
                        </Form>
                    </Card.Body>
                </Accordion.Collapse>
            </Card>

        </Accordion>
    )
}

export default ListAppointment;