import CollapsableButton from './CollapsableButton'
import React, { useState} from 'react';
import {createRequest} from "../request";

 const ACPT=()=>{
  const [binaryValue,setBinaryValue]=useState('');
  const [binaryResult,setBinaryResult]=useState('');

   const [decimalValue,setDecimalValue]=useState('');
   const [decimalResult,setDecimalResult]=useState('');

   const [hexadecimal,setHexadecimal]=useState('');
   const [instruction,setInstruction]=useState('');
   const [code,setCode]=useState('');

   const [hexadecimalResult, setHexadecimalResult]=useState('');
   const [instructionResult, setInstructionResult]=useState('');
   const [codeResult, setCodeResult]=useState('');

   const [hexadecimalFP,setHexadecimalFP]=useState('');
   const [decimalFP,setDecimalFP]=useState('');

   const [hexadecimalFPResult,setHexadecimalFPResult]=useState('');
   const [decimalFPResult,setDecimalFPResult]=useState('');

   const [blocks,setBlocks]=useState('')
   const [blockSize,setBlocksize]=useState('')
   const [hitTime,setHitTime]=useState('')
   const [failTime,setFailTime]=useState('')

     const [cacheResult,setCacheResult]=useState({})

   const [nextAddress,setNextAddress]=useState('')

   const binaryConversionStruct={
     key:"binaryConversion",
     label:"Binary Conversion",
     rows:[
       {
         inputs:[
           {
             inputLabel:"Binary",
             inputType:"text",
             value:decimalValue,
             change:(event)=>{setDecimalValue(event.target.value)},
           }
         ],
         key:"decimal",
         buttonLabel:"Convert to Decimal",
         buttonFunction:()=>{createRequest("/classes/acpt/binary","put",decimalValue,null,(response)=>setDecimalResult(response),alert)},
         result:decimalResult
       },{
         inputs:[
           {
             inputLabel:"Decimal",
             inputType:"text",
             value:binaryValue,
             change:(event)=>{setBinaryValue(event.target.value)},
           }
         ],
         key:"binary",
         buttonLabel:"Convert to Binary",
         buttonFunction:()=>{createRequest("/classes/acpt/decimal","put",binaryValue,null,(response)=>setBinaryResult(response),alert)},
         result:binaryResult
       }
     ]
   }

   const instructionConversion={
     key:"instructionConversion",
     label:"Instruction Conversion",
     rows:[
       {
         inputs:[
           {
             inputLabel:"Hexadecimal",
             inputType:"text",
             value:hexadecimal,
             change:(event)=>{setHexadecimal(event.target.value)},
           }
         ],
         key:"hexadecimal",
         buttonLabel:"Convert to Instruction",
         buttonFunction:()=>{createRequest("/classes/acpt/hexadecimal","put",{hexadecimal:hexadecimal},null,(response)=>setHexadecimalResult(response),alert)},
         result:hexadecimalResult
       },{
         inputs:[
           {
             inputLabel:"Instruction",
             inputType:"text",
             value:instruction,
             change:(event)=>{setInstruction(event.target.value)},
           }
         ],
         key:"instruction",
         buttonLabel:"Convert to Hexadecimal",
         buttonFunction:()=>{createRequest("/classes/acpt/instruction","put",{instruction:instruction},null,(response)=>setInstructionResult(response),alert)},
         result:instructionResult
       },{
         inputs:[
           {
             inputLabel:"Instruction",
             inputType:"textarea",
             value:code,
             change:(event)=>{setCode(event.target.value)},
           }
         ],
         key:"completeCode",
         buttonLabel:"Convert to Hexadecimal",
         buttonFunction:()=>{createRequest("/classes/acpt/code","put",code.split("\n"),null,(response)=>setCodeResult(response),alert)},
         result:codeResult
       }
     ]
   }

   const fpConversion={
     key:"fpConversion",
     label:"Floating Point Conversion",
     rows:[
       {
         inputs:[
           {
             inputLabel:"Hexadecimal",
             inputType:"text",
             value:hexadecimalFP,
             change:(event)=>{setHexadecimalFP(event.target.value)},
           }
         ],
         key:"fpHexadecimal",
         buttonLabel:"Convert to Floating Point",
         buttonFunction:()=>{createRequest("/classes/acpt/FPToDecimal","put",hexadecimalFP,null,(response)=>setHexadecimalFPResult(response),alert)},
         result:hexadecimalFPResult
       },{
         inputs:[
           {
             inputLabel:"Decimal",
             inputType:"text",
             value:decimalFP,
             change:(event)=>{setDecimalFP(event.target.value)},
           }
         ],
         key:"fpDecimal",
         buttonLabel:"Convert to Decimal Floating Point",
         buttonFunction:()=>{createRequest("/classes/acpt/decimalToFP","put",decimalFP,null,(response)=>setDecimalFPResult(response),alert)},
         result:decimalFPResult
       }
     ]
   }

   const cacheSimulator={
     key:"cacheSimulator",
     label:"Cache Simulator",
     rows:[
       {
         inputs:[
           {inputLabel:"N Blocks" ,inputType:"text",value:blocks, change:(event)=>{setBlocks(event.target.value)},},
           {inputLabel:"Block Size" ,inputType:"text",value:blockSize, change:(event)=>{setBlocksize(event.target.value)}},
           {inputLabel:"Hit Time" ,inputType:"text",value:hitTime, change:(event)=>{setHitTime(event.target.value)}},
           {inputLabel:"Fail Time" ,inputType:"text",value:failTime, change:(event)=>{setFailTime(event.target.value)}}
         ],
         key:"createCache",
         buttonLabel:"Create Cache",
           buttonFunction:()=>{createRequest("/classes/acpt/cache/create","put",{blockSize:blockSize,nblocks:blocks,hitTime:hitTime,failTime:failTime},null,(response)=>setCacheResult(response),alert)},
           result:cacheResult
       },{
         inputs:[
           {
             inputLabel:"Next Address",
             inputType:"text",
               value:nextAddress,
               change:(event)=>{setNextAddress(event.target.value)}
           }
         ],
         key:"memoryAccess",
         buttonLabel:"New Memory Access",
         buttonFunction:()=>{createRequest("/classes/acpt/cache/interactive","put",{cache:cacheResult,nextAddress:nextAddress?nextAddress.split(","):[] },null,(response)=>setCacheResult(response),alert)},
         //result:cacheResult
       }
     ]
   }

    return(
         <div>
            <CollapsableButton key="1" elements={binaryConversionStruct} />
            <CollapsableButton key="2" elements={instructionConversion}/>
            <CollapsableButton key="3" elements={fpConversion}/>
            <CollapsableButton key="4" elements={cacheSimulator}/>
        </div>
    )
}

export default ACPT;



