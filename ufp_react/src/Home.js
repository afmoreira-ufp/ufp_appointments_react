import React, {useState} from "react";
import {  BrowserRouter as Router,  Switch,  Route } from "react-router-dom";

import Navbar from 'react-bootstrap/Navbar';
import Nav from 'react-bootstrap/Nav';


import Appointments from './appointments/Appointments';
import ACPT from './lectures/ACPT';
import RCO1 from './lectures/RCO1';
import RCO2 from './lectures/RCO2';
import Button from "react-bootstrap/Button";
import Col from "react-bootstrap/Col";
// import SO from './lectures/SO';


export default function App() {

  const [isLoggedIn,setIsLoggedIn]=useState(localStorage.getItem("isLoggedIn") === 'true');
  const [username, setUsername] = useState(localStorage.getItem("username")?localStorage.getItem("username"):'');



  const logout=()=>{
    setIsLoggedIn(false);
    localStorage.removeItem("isLoggedIn");
    localStorage.removeItem("isTeacher");
    localStorage.removeItem("token");
    localStorage.removeItem("username");
  }

  return (
    <Router>         
      
        <Navbar bg="light" expand="lg">
          <Navbar.Brand href="/">Afmiguez</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="mr-auto">
            <Nav.Link href="/appointments">Appointments</Nav.Link>
              <Nav.Link href="/acpt">ACPT</Nav.Link>
              <Nav.Link href="/rco1">RCO1</Nav.Link>              
              <Nav.Link href="/rco2">RCO2</Nav.Link>              
              {/*<Nav.Link href="/so">SO</Nav.Link>*/}
            </Nav>
            <Col sm="2">
              Hi, {isLoggedIn?username:"Anonymous"}
            </Col>
            <Col sm="2">
              <Button id="login" variant="primary" type="button" onClick={logout} hidden={!isLoggedIn}>
                Logout
              </Button>
            </Col>
          </Navbar.Collapse>
        </Navbar>

        <Switch>
          <Route path="/acpt">
            <ACPT isLoggedIn={isLoggedIn} />
          </Route>
          <Route path="/appointments">
            <Appointments isLoggedIn={isLoggedIn} setIsLoggedIn={setIsLoggedIn} username={username} setUserName={setUsername}/>
          </Route>
          <Route path="/rco1">
            <RCO1 isLoggedIn={isLoggedIn} />
          </Route>
          <Route path="/rco2">
            <RCO2 isLoggedIn={isLoggedIn} />
          </Route>
          {/*<Route path="/so">*/}
          {/*  <SO />*/}
          {/*</Route>             */}
          <Route path="/">
            <Home isLoggedIn={isLoggedIn} />
          </Route>    
        </Switch>
      
    </Router>
  );
}

function Home() {
  return <h2>Home</h2>;
}