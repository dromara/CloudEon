import type { ProColumns } from '@ant-design/pro-components';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import { Spin, Tooltip } from 'antd';
import { useState, useEffect } from 'react';
import styles from './index.less'

const roleTab:React.FC<{rolesInfo: API.rolesInfos[], loading: Boolean}> = ({rolesInfo, loading}) => {

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
            render: () => [
                <a key="link">启动</a>,
                <a key="link2">停止</a>,
                <a key="link5">删除</a>,
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
            <Spin tip="Loading" size="small" spinning={!!loading}>
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