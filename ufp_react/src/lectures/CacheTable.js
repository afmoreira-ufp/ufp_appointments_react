import MaterialTable from "material-table";
import Container from "react-bootstrap/Container";
import React from "react";

const CacheTable=props=>{

    const {data,columns,columnsAccess,dataAccess,tmam,missRatio}=props;
    return(
        <Container>
            <MaterialTable columns={columns} data={data} options={{paging:false,showTitle:false,search:false,}}/>
            <p>tmam:{tmam}</p>
            <p>miss ratio:{missRatio}</p>
            <MaterialTable columns={columnsAccess} data={dataAccess} options={{paging:false,showTitle:false,search:false,}}/>
        </Container>

    );
}

export default CacheTable;
