import React, { Component } from 'react';
import CustomRow from './CustomRow'
import Button from 'react-bootstrap/Button';
import Form from 'react-bootstrap/Form';
import Row from 'react-bootstrap/Row';
import Container from 'react-bootstrap/Container';
import Card from 'react-bootstrap/Card';
import Accordion from 'react-bootstrap/Accordion';


class CollapsableButton extends Component{
    render(){
        
        const {elements}=this.props

        const rows=elements.rows.map(
            (row,index)=>{
                return <CustomRow key={index} row={row}/>
            }
        )

        return (
            <Container>
                <Accordion defaultActiveKey="0">
                    <Card>
                        <Card.Header>
                        <Accordion.Toggle as={Button} variant="link" eventKey="1">
                            {elements.label}
                        </Accordion.Toggle>
                        </Card.Header>
                        <Accordion.Collapse eventKey="1">
                        <Card.Body>
                            <Form>
                                <Form.Group as={Row} controlId="formPlaintextEmail">
                                    {rows}                
                                </Form.Group>
                            </Form>
                        </Card.Body>
                        </Accordion.Collapse>
                    </Card>
                </Accordion>
            </Container>
            
        )
    }
}

export default CollapsableButton