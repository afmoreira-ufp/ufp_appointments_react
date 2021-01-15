import React from 'react';
import Form from 'react-bootstrap/Form';
import FormControl from 'react-bootstrap/FormControl';
import Col from 'react-bootstrap/Col';
import Row from 'react-bootstrap/Row';
import Button from 'react-bootstrap/Button';
import CacheTable from "./CacheTable";
import Container from "react-bootstrap/Container";

const CustomRow =props=>{


    const {row}=props

    const input=row.inputs.map(
        (input,index)=>{
            if(input.inputType==="text"){
                return <FormControl key={index} rows={3} placeholder={input.inputLabel} value={input.value} onChange={input.change}/>
            }else{
                return <Form.Control as="textarea" key={index} rows={3} placeholder={input.inputLabel} value={input.value} onChange={input.change}/>
            }
        }
    )
    const resultId=row.key+'result';

    let resultDiv;
    if( (row.key==='createCache' || row.key==='memoryAccess' ) && row.result && row.result.offsets){
        const columnNames=["Index","V","Tag"].concat(row.result.offsets);
        const columns=columnNames.map((columnName)=>{return {title:columnName,field:columnName.toLocaleLowerCase()}})
        const data=Object.keys(row.result.cacheTable).map((key)=>{return{index:key,v:row.result.cacheTable[key]?1:0,tag:row.result.cacheTable[key]}});

        const columnsAccess=["Address","Tag","Index","Offset","Result"].map((col)=>{return{title:col,field:col.toLocaleLowerCase()}});
        const dataAccess=row.result.memoryAccesses?row.result.memoryAccesses:[];
        resultDiv=<CacheTable columns={columns} data={data} columnsAccess={columnsAccess} dataAccess={dataAccess} tmam={row.result.tmam?row.result.tmam:''} missRatio={row.result.missRatio?row.result.missRatio:''}/>
    }
    else if( (row.key==='calculateHamming' || row.key==='decodeHamming') && row.result) {

        resultDiv =<Container>
            <p>message: {row.result.message}</p>
            <p>code: {row.result.code}</p>
            {
                row.result.operations ? row.result.operations.map(
                    (operation) =>
                <p>{Object.keys(operation)
                    .map((key)=>(key+": "+operation[key]) )}
                </p>
                ) : ''
            }
        </Container>
    }else if(row.key==='csmaCollision'){
        console.log(row);
        resultDiv=<Container>
            {row.result ? row.result.map((result) =>
                <p>{Object.keys(result)
                    .map((key)=>(key+": "+result[key])+" " )}</p>
            ) : ''}
        </Container>
    }
    else
    {
        resultDiv= <div id={resultId}>
            {row.result ? Object.keys(row.result).map((key,index) => (<p>{key}: {row.result[key]}</p>)) : ''}
        </div>
    }

    return(
            <Col>
                <Col>
                    <Row>
                        {input}
                    </Row>

                </Col>
                <Col>
                    <Button variant="outline-secondary" onClick={row.buttonFunction} id={row.key}>{row.buttonLabel}</Button>
                </Col>
                {resultDiv}
            </Col>
        )

}

export default CustomRow