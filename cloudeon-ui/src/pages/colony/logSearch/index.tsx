import {PageContainer, ProTable} from '@ant-design/pro-components';
import React, {useEffect, useState} from 'react';
import {getRoleNamesAPI, getServiceLogAPI} from '@/services/ant-design-pro/colony';
import styles from './index.less'

const actionList: React.FC = () => {
    const [logListData, setLogListData] = useState<any[]>();
    const [roleNameList, setRoleNameList] = useState<string[]>();

    useEffect(() => {
        const asyncFun = async ()=>{
            const result: API.normalResult =  await getRoleNamesAPI({ clusterId: getData.clusterId });
            console.log('getRoleNamesAPI: ',result)
            setRoleNameList(result?.data)
        }
        asyncFun()
        return () =>{
        }
    }, []);

    const getData = JSON.parse(sessionStorage.getItem('colonyData') || '{}')



    interface Item {
        id: number;
        message: string;
        bizTime: string;
        logLevel: string;
        ip: string;
        serviceName: string;
        roleName: string;
    }

    // const statusColor = {
    //   'ERROR': '#f5222d',
    //   'RUNNING': '#1890ff',
    //   'SUCCESS': '#52c41a',
    // }


    const columns = [
        {
            title: '时间',
            dataIndex: 'bizTime',
            key: 'bizTime',
            hideInSearch: true,

        },
        {
            title: '信息',
            dataIndex: 'message',
            key: 'message',
            hideInSearch: true
        },


        {
            title: '服务角色名',
            dataIndex: 'roleName',
            key: 'roleName',
            hideInTable: true,
            // valueEnum: roleNameList
            valueType: 'select',
            fieldProps: {
                options: roleNameList?.map(rolename=>{return {label: rolename,value: rolename,}})
            },
        },
        {
            title: '日志级别',
            dataIndex: 'logLevel',
            key: 'logLevel',
            hideInTable: true,
            // render: (_: any, record: Item) => {
            //   return (
            //     <div style={{color:statusColor[record.commandState], whiteSpace: 'nowrap'}}> <span style={{backgroundColor: statusColor[record.commandState]}} className={styles.statusCircel}></span>{record.commandState}</div>
            //   )
            // },
            initialValue:'INFO',
            valueEnum: {
                'DEBUG': {
                    text: 'DEBUG',
                    status: 'DEBUG',
                },
                'INFO': {
                    text: 'INFO',
                    status: 'INFO',
                },
                'WARN': {
                    text: 'WARN',
                    status: 'WARN',
                },
                'ERROR': {
                    text: 'ERROR',
                    status: 'ERROR',
                },
            },
        },
        {
            title: '节点ip',
            dataIndex: 'hostip',
            key: 'hostip',
            hideInSearch: true
        },

    ]


    return (
        <PageContainer className={styles.logSearchPageContainer} header={{title:''}}>

            <ProTable
                search={{defaultCollapsed:false}}
                rowKey="id"
                columns={columns}
                dataSource={logListData}
                request={async (params = {}, sort, filter) => {

                    const result = await getServiceLogAPI({...params, clusterId: getData.clusterId});
                    setLogListData(result?.data?.logs)
                    return result?.data?.logs
                }}
            />
        </PageContainer>
    );
};

export default actionList;
