let wsAPI = "ws://192.168.31.138:7700/log"; 
if(process.env.UMI_ENV === 'dev'){
    wsAPI="ws://192.168.31.138:7700/log" 
}else if(process.env.UMI_ENV === 'prod'){
    wsAPI="ws://localhost:7700/log"  // 生产环境用localhost
}

export {
    wsAPI
}