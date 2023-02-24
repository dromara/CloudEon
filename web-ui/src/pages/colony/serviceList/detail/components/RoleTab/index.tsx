import type { ProColumns } from '@ant-design/pro-components';
import { ProTable, TableDropdown } from '@ant-design/pro-components';
import { Button } from 'antd';
import { useState, useEffect } from 'react';

export type TableListItem = {
    key: number;
    name: string;
    containers: string;
    creator: string;
    status: string;
    createdAt: number;
    memo: string;
  };

const roleTab:React.FC = () => {

    const [tableData, setTableData] = useState<TableListItem[]>([
        {
          key: 1,
          name: 'AppName',
          containers: '1.0.2.2',
          creator: '111',
          status: '111',
          createdAt: Date.now() - Math.floor(Math.random() * 100000),
          memo: '很长很长很长很长很长很长很长的文字要展示但是要留下尾巴',
        }
    ]);



    const columns: ProColumns<TableListItem>[] = [
        {
            title: '名称',
            dataIndex: 'name',
            // render: (_) => <a>{_}</a>,
        },
        {
            title: '实例状态',
            dataIndex: 'status',
            initialValue: 'all',
            valueEnum: {
                all: { text: '全部', status: 'Default' },
                close: { text: '关闭', status: 'Default' },
                running: { text: '运行中', status: 'Processing' },
                online: { text: '已上线', status: 'Success' },
                error: { text: '异常', status: 'Error' },
            },
        },
        {
            title: '主机名称',
            dataIndex: 'containers',
            align: 'left',
            sorter: (a, b) => a.containers - b.containers,
        },
        {
            title: '操作',
            key: 'option',
            valueType: 'option',
            render: () => [
                <a key="link">启动</a>,
                <a key="link2">停止</a>,
                <a key="link3">重启</a>,
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
        <div>
            <ProTable<TableListItem>
                dataSource={tableData}
                rowKey="key"
                pagination={{
                    showQuickJumper: true,
                }}
                columns={columns}
                search={false}
            />
        </div>
    )
}

export default roleTab