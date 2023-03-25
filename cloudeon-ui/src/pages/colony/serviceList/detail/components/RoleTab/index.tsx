import type { ProColumns } from '@ant-design/pro-components';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import { Spin, Button, Popconfirm, message } from 'antd';
import { useState, useEffect } from 'react';
import styles from './index.less'
import { startRoleAPI, stopRoleAPI, getServiceRolesAPI } from '@/services/ant-design-pro/colony';


const roleTab:React.FC<{ serviceId: any}> = ({serviceId}) => {
    const [confirmLoading, setConfirmLoading] = useState(false);
    const [currentId, setCurrentId] = useState(0);
    const [currentType, setCurrentType] = useState('');
    const [apiLoading, setApiLoading] = useState(false);
    const [rolesInfo, setRolesInfo] = useState<API.rolesInfos[]>();


    // 获取角色数据
  const getRoles = async () =>{
    const params = {serviceInstanceId: serviceId}
    setApiLoading(true)
    const result = await getServiceRolesAPI(params)
    setApiLoading(false)
    if(result?.success){
      setRolesInfo(result?.data)
    }
  }

  useEffect(()=>{
    getRoles()
  },[])


    const confirm = async(type:string, id:number)=>{
        const params = { roleInstanceId: id}
        setCurrentId(id)
        setCurrentType(type)
        switch(type){
            case 'start': await startRole(params);break;
            case 'stop': await stopRole(params);break;
            default: break;
        }
        setCurrentId(0)
        setCurrentType('')
    }

    const startRole = async(params:any)=>{
        setConfirmLoading(true)
        const result = await startRoleAPI(params)
        setConfirmLoading(false)
        if(result?.success){
            message.success('启动成功！', 3)
            getRoles()
        }else{
            message.error('启动失败：'+result?.message, 3)
        } 
    }

    const stopRole = async(params:any)=>{
        setConfirmLoading(true)
        const result = await stopRoleAPI(params)
        setConfirmLoading(false)
        if(result?.success){
            message.success('停止成功！', 3)
            getRoles()
        }else{
            message.error('停止失败：'+result?.message, 3)
        }
    }

    const columns: ProColumns<API.rolesInfos>[] = [
        {
            title: '名称',
            key:'name',
            dataIndex: 'name',
            // render: (_) => <a>{_}</a>,
        },
        {
            title: '状态',
            key:'roleStatus',
            dataIndex: 'roleStatus',
            initialValue: 'all',
            valueEnum: {
                all: { text: '全部', status: 'Default' },
                close: { text: '关闭', status: 'Default' },
                running: { text: '运行中', status: 'OPERATING' },
                online: { text: '已上线', status: 'Success' },
                error: { text: '异常', status: 'Error' },
            },
        },
        {
            title: '主机名称',
            key:'nodeHostname',
            dataIndex: 'nodeHostname',
            align: 'left'
        },
        {
            title: '主机ip',
            key:'nodeHostIp',
            dataIndex: 'nodeHostIp',
            align: 'left'
        },
        // {
        //     title: 'ui地址',
        //     key:'uiUrls',
        //     dataIndex: 'uiUrls',
        //     align: 'left',
        //     render: (_, record) => {
        //         return (
        //             <>
        //             {record.uiUrls && 
        //                     <Tooltip 
        //                     overlayStyle={{maxWidth:'1000px'}} overlayInnerStyle={{width:'auto'}}  color="#fff"
        //                     title={()=>{
        //                     return  record?.uiUrls?.map((urlItem,index)=>{
        //                             return (
        //                                 <div 
        //                                     key={index.toString()} 
        //                                     style={{width:'auto'}} 
        //                                     // className={styles.urlLinkWrap} 
        //                                     >
        //                                         <a 
        //                                             key={'url'+index.toString()} 
        //                                             // className={styles.urlLinkWrap}  
        //                                             target="_blank" 
        //                                             href={urlItem}
        //                                             >
        //                                                 {urlItem}
        //                                         </a>
        //                                 </div>
        //                             )
        //                         })
        //                     }}
        //                             placement="top">                                                
        //                         <a>查看</a>
        //                     </Tooltip>
        //             }
        //             </>
        //         )
        //     }
        // },
        {
            title: '操作',
            key: 'actionBtns',
            dataIndex: 'actionBtns',
            valueType: 'option',
            render: (_, record) => [
                <Popconfirm
                    title="确定要启动吗?"
                    onConfirm={()=>confirm('start',record?.id || 0)}
                    okText="确定"
                    cancelText="取消"
                >
                    <Button className={styles.roleBtn} type="link" loading={currentType=='start' && currentId == record.id} disabled={confirmLoading}>启动</Button>
              </Popconfirm>,
              <Popconfirm
                    title="确定要停止吗?"
                    onConfirm={()=>confirm('stop',record?.id || 0)}
                    okText="确定"
                    cancelText="取消"
                >
                    <Button className={styles.roleBtn} type="link" loading={currentType=='stop' && currentId == record.id} disabled={confirmLoading}>停止</Button>
                </Popconfirm>,
                // <a key="link">启动</a>,
                // <a key="link2">停止</a>,
                // <a key="link5">删除</a>,
                // <TableDropdown
                //     key="actionGroup"
                //     menus={[
                //         { key: 'copy', name: '复制' },
                //         { key: 'delete', name: '删除' },
                //     ]}
                // />,
            ],
        },
    ];

    return (
        <div style={{minHeight:'200px'}} className={styles.roleTab}>
            {/* <Tooltip open={true} overlayStyle={{maxWidth:'1000px'}} overlayInnerStyle={{width:'auto'}}  color="#fff"
            title={<a>Tooltip will show on mouse enter.</a>}>
                <a>Tooltip will show on mouse enter.</a>
            </Tooltip> */}
            <Spin tip="Loading" size="small" spinning={!!apiLoading}>
                <ProTable
                    dataSource={rolesInfo}
                    rowKey="id"
                    pagination={{
                        showQuickJumper: true,
                    }}
                    columns={columns}
                    search={false}
                />
            </Spin>
        </div>
    )
}

export default roleTab