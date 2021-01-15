import React, { useState} from 'react';
import CollapsableButton from './CollapsableButton';
import {createRequest} from "../request";

const RCO1 =()=>{

  const [distance,setDistance]=useState('');
  const [velocity,setVelocity]=useState('');
  const [frameLength,setFrameLength]=useState('');
  const [propagationTime,setPropagationTime]=useState('');
  const [bitRate,setBitRate]=useState('');
  const [frameTime,setFrameTime]=useState('');
  const [A,setA]=useState('');
  const [utilization,setUtilization]=useState('');
  const [sequenceBits,setSequenceBits]=useState('');
  const [windows,setWindows]=useState('');

  const [arqResult,setArqResult]=useState('');

  const [message,setMessage]=useState('');
  const [hammingResult,setHammingResult]=useState('');

  const [code,setCode]=useState('');
  const [codeResult,setCodeResult]=useState('');

  const [numberStations,setNumberStations]=useState('');
  const [iterations,setIterations]=useState('');
  const [csmaResult,setCsmaResult]=useState('');

  const ARQ={
    key:"arqCalculation",
    label:"ARQ",
    rows:[
      {
        inputs:[
          {
            inputLabel:"Distance",
            inputType:"text",
            value:distance,
            change:(event)=>{setDistance(event.target.value)},
          },
          {
            inputLabel:"Velocity",
            inputType:"text",
            value:velocity,
            change:(event)=>{setVelocity(event.target.value)},
          },
          {
            inputLabel:"Frame Length",
            inputType:"text",
            value:frameLength,
            change:(event)=>{setFrameLength(event.target.value)},
          },
          {
            inputLabel:"Bit Rate",
            inputType:"text",
            value:bitRate,
            change:(event)=>{setBitRate(event.target.value)},
          },
          {
            inputLabel:"Propagation Time",
            inputType:"text",
            value:propagationTime,
            change:(event)=>{setPropagationTime(event.target.value)},
          },
          {
            inputLabel:"Frame Time",
            inputType:"text",
            value:frameTime,
            change:(event)=>{setFrameTime(event.target.value)},
          },
          {
            inputLabel:"A",
            inputType:"text",
            value:A,
            change:(event)=>{setA(event.target.value)},
          },
          {
            inputLabel:"Utilization",
            inputType:"text",
            value:utilization,
            change:(event)=>{setUtilization(event.target.value)},
          },
          {
            inputLabel:"Windows",
            inputType:"text",
            value:windows,
            change:(event)=>{setWindows(event.target.value)},
          },
          {
            inputLabel:"Sequence bits",
            inputType:"text",
            value:sequenceBits,
            change:(event)=>{setSequenceBits(event.target.value)},
          },

        ],
        key:"arq",
        buttonLabel:"Calculate",
        buttonFunction:()=>{createRequest("/classes/rco1","put",{a:A,distance:distance,velocity:velocity,frameLength:frameLength,bitRate:bitRate,propagationTime:propagationTime,frameTime:frameTime,utilization:utilization,windows:windows,sequenceBits:sequenceBits},null,(response)=>setArqResult(response),alert)},
        result:arqResult
      }
    ]
  }

  const hamming={
    key:"hamming",
    label:"Hamming",
    rows:[
      {
        inputs:[
          {
            inputLabel:"Message",
            inputType:"text",
            value:message,
            change:(event)=>{setMessage(event.target.value)},
          },
        ],
        key:"calculateHamming",
        buttonLabel:"Calculate Hamming",
        buttonFunction:()=>{createRequest("/classes/rco1/hamming","put",{message:message},null,(response)=>setHammingResult(response),alert)},
        result:hammingResult
      },
      {
        inputs:[
          {
            inputLabel:"Code",
            inputType:"text",
            value:code,
            change:(event)=>{setCode(event.target.value)},
          },
        ],
        key:"decodeHamming",
        buttonLabel:"Decode Hamming",
        buttonFunction:()=>{createRequest("/classes/rco1/hamming/decode","put",{message:code},null,(response)=>setCodeResult(response),alert)},
        result:codeResult
      },
    ]
  }

  const csma= {
    key: "csma",
    label: "CSMA Collision",
    rows: [
      {
        inputs: [
          {
            inputLabel: "Number stations",
            inputType: "text",
            value:numberStations,
            change:(event)=>{setNumberStations(event.target.value)},
          },
          {
            inputLabel: "Iterations",
            inputType: "text",
            value: iterations,
            change:(event)=>{setIterations(event.target.value)}
          },
        ],
        key: "csmaCollision",
        buttonLabel: "Calculate CSMA Collisions",
        buttonFunction:()=>{createRequest("/classes/rco1/csma","put",{n:numberStations,iterations:iterations},null,(response)=>setCsmaResult(response),alert)},
        result:csmaResult
      }
    ]
  }

    return (
            <div>
            <CollapsableButton key="1" elements={ARQ} />
            <CollapsableButton key="2" elements={hamming} />
            <CollapsableButton key="3" elements={csma} />
            </div>
        );
}

export default RCO1;

