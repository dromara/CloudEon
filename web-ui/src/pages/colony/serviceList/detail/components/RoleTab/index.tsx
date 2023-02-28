import type { ProColumns } from '@ant-design/pro-components';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import { Spin } from 'antd';
import { useState, useEffect } from 'react';

// export type TableListItem = {
//     key: number;
//     name: string;
//     containers: string;
//     creator: string;
//     status: string;
//     createdAt: number;
//     memo: string;
//   };

const roleTab:React.FC<{rolesInfo: API.rolesInfos[], loading: Boolean}> = ({rolesInfo, loading}) => {

    // const [tableData, setTableData] = useState<API.rolesInfos[]>([
    //     {
    //         "name": "DORIS_FE",
    //         "id": 101,
    //         "roleStatus": "OPERATING",
    //         "nodeId": 1,
    //         "nodeHostname": "fl001",
    //         "nodeHostIp": "10.81.16.19",
    //         "uiUrls": [
    //             "http://fl001:1000/info",
    //             "http://10.81.16.19:1000/info"
    //         ]
    //     }
    // ]);

    // useEffect(()=>{
    //     if(rolesInfo) setTableData(rolesInfo)
    // },[])



    const columns: ProColumns<API.rolesInfos>[] = [
        {
            title: '名称',
            dataIndex: 'name',
            // render: (_) => <a>{_}</a>,
        },
        {
            title: '状态',
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
            dataIndex: 'nodeHostname',
            align: 'left'
        },
        {
            title: '主机ip',
            dataIndex: 'nodeHostIp',
            align: 'left'
        },
        {
            title: 'ui地址',
            dataIndex: 'uiUrls',
            align: 'left'
        },
        {
            title: '操作',
            key: 'option',
            valueType: 'option',
            render: () => [
                <a key="link">启动</a>,
                <a key="link2">停止</a>,
                <a key="link3">删除</a>,
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
        <div style={{minHeight:'200px'}}>
            <Spin tip="Loading" size="small" spinning={!!loading}>
                <ProTable
                    dataSource={rolesInfo}
                    rowKey="key"
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