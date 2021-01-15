
const createRequest=(url,method,data,token,functionDone,functionFail)=>{
    let request={
        method:method,              
        mode: 'cors',
        body:typeof data ==='string'?data:JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json',
            'Authorization':token,
            'Origin':window.location.origin,
        },
    };
    if(data===null || data===''){
        delete request.body
    }    
    return fetch(urlBase+"/api"+url,request)
    .then(
        response=>{
            if(!response.ok){
                if(response.headers.get("TOKEN_RESPONSE")==="EXPIRED"){
                    localStorage.removeItem("isTeacher");
                    localStorage.removeItem("teacher");
                    localStorage.removeItem("token");
                    localStorage.removeItem("username");
                    localStorage.removeItem("user");
                }
                const status=response.headers.get("STATUS");
                throw new Error(status);
              }
              return response.json();
        })
    .then(response=>{
        functionDone(response)
    })
    .catch((error)=>{
        functionFail(error);
    });
}

const createRequestLogin=(url,method,data,functionDone,functionFail)=>{
    let request={
        method:method,              
        mode: 'cors',
        body:JSON.stringify(data),
        headers: {
            'Content-Type': 'application/json',
            'Origin':window.location.origin
        },
    };
    if(data===null || data===''){
        delete request.body
    }
    return fetch(urlBase+url,request)
    .then(response=>{

        if(response.ok){
            functionDone(response)
        }
        else{
            throw new Error(response.error);
        }
    })
    .catch((error)=>{
        functionFail(error);
    });
}

// const urlBase="https://teste.afmiguez.me:8090";
const urlBase=process.env.NODE_ENV==='development'?"http://localhost:8090":"";
export {createRequest,createRequestLogin,urlBase};
