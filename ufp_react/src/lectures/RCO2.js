import React, { useState } from 'react';

import {createRequest} from '../request';

import Form from 'react-bootstrap/Form';
import FormControl from 'react-bootstrap/FormControl';
import Row from 'react-bootstrap/Row';
import Col from 'react-bootstrap/Col';
import Container from 'react-bootstrap/Container';
import Button from 'react-bootstrap/Button';
import Card from 'react-bootstrap/Card';
import Accordion from 'react-bootstrap/Accordion';

const RCO2=props=>{
      const [lanSize, setLanSize]=useState(["100"]);
      const [routerNetworks, setRouterNetworks]=useState("2");
      const [ipBase, setIpBase]=useState("192.168.1.0");
      const [cidr, setCIDR]=useState("24");
      const [responseData, setResponseData]=useState("");

    const addNewRow=()=>{
        setLanSize([...lanSize,""]);
    }

    const onSubmit = () => {

        const request={
            hostsSize:lanSize,
            numberOfRouterNetworks:routerNetworks,
            ip:ipBase,
            cidr:cidr,
        };

        createRequest("/classes/vlsm/calculate","PUT",request,null,
            (response)=>{
                setResponseData(
                    response.map(element => {                
                        return "prefix: "+element.network+"/"+element.cidr+"\t\tbroadcast: "+element.broadcast+"/"+element.cidr+"\n";
                    }).join("")
                )},
            (error)=>console.log(error)
        )
      };


    const handleLansizeChange=index =>event=>{
        setLanSize(lanSize.map((size,itIndex)=>{
            if(itIndex!==index)return size;
            return event.target.value;
        }));
    }

    return(
        <Container>
            <Accordion defaultActiveKey="0">

            <Card.Header>
                <Accordion.Toggle as={Button} variant="link" eventKey="1">
                    Subnet Calculator
                </Accordion.Toggle>
            </Card.Header>
            <Accordion.Collapse eventKey="1">
            <Card.Body>
                <Form>     
                    <Form.Group as={Row} >
                    {
                        lanSize.map((item,index)=>{
                            console.log(index+": "+item);
                            const id="lanSize"+index;                        
                            return <FormControl id={id} placeholder="Lan Size" key={index} value={item} onChange={handleLansizeChange(index)}  />
                        })
                    }       
                    </Form.Group>

                    <Form.Group as={Row} controlId="routerNetworks">
                        <FormControl placeholder="Number of /30 networks" value={routerNetworks} onChange={e => setRouterNetworks( e.target.value)}/>
                    </Form.Group>
                    <Form.Group as={Row} > 
                        <Col>
                            <FormControl placeholder="IP to be Subnetted" id="ipBase" value={ipBase} onChange={e => setIpBase(e.target.value)}/>
                        </Col> / 
                        <Col>
                            <FormControl id="CIDR" placeholder="CIDR" value={cidr} onChange={e => setCIDR( e.target.value)}/>
                        </Col>  
                    </Form.Group>

                    <Button variant="outline-secondary" id="newRow" onClick={addNewRow}>New Lan</Button>
                    <Button variant="outline-secondary" id="calculate" onClick={onSubmit}>Calculate</Button>
                </Form>
                
                <Form.Control as="textarea" rows={10} value={responseData} readOnly/>
            </Card.Body>
            </Accordion.Collapse>
        </Accordion>
        </Container>
    )
}

export default RCO2;