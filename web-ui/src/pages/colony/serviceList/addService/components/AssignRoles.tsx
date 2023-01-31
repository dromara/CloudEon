
import { ProCard } from '@ant-design/pro-components';
import { Tree, Table } from 'antd';
import type { DataNode, TreeProps } from 'antd/es/tree';
import styles from './AssignRoles.less'
import { useState, useEffect } from 'react';
import { getNodeListAPI } from '@/services/ant-design-pro/colony';

const AssignRoles : React.FC<{
    serviceList: any[];
}> = ({serviceList})=>{

    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]);
    const [nodeListData, setNodeListData] = useState<any[]>();
    const [loading, setLoading] = useState(false);

    const getNodeData = async (params: any) => {
        setLoading(true)
        const result: API.NodeList =  await getNodeListAPI(params);
        setLoading(false)
        setNodeListData(result.data)
      };

    const treeData = serviceList.map(item=>{
        let itemData = {
            title: item.label,
            key: item.id,
            selectable: false,
            children: item.roles? (item.roles.map((r: any)=>{
                return {
                    title: r,
                    key: r,
                }
            })):[]
        }
        return itemData
    })

    const onSelect: TreeProps['onSelect'] = (selectedKeys, info) => {
        console.log('selected', selectedKeys, info);
    };

    // const dataSource = [
    //     {
    //       key: '1',
    //       name: '测试节点',
    //       age: 32,
    //     },
    //     {
    //       key: '2',
    //       name: '测试节点',
    //       age: 42,
    //     },
    // ];

    const columns = [
        {
            title: '节点',
            dataIndex: 'hostname',
            key: 'hostname',
        },
        {
            title: 'ip地址',
            dataIndex: 'ip',
            key: 'ip',
        },
    ];

    const onSelectChange = (newSelectedRowKeys: React.Key[]) => {
        console.log('selectedRowKeys changed: ', newSelectedRowKeys);
        setSelectedRowKeys(newSelectedRowKeys);
      };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
      };




    return (
        <>
        <ProCard split="vertical" bordered>
            <ProCard title="服务列表" colSpan="30%" headerBordered className={styles.rolesLeft}>
            <Tree
                defaultExpandAll
                onSelect={onSelect}
                treeData={treeData}
                style={{background: '#fbfbfe'}}
            />
            </ProCard>
            <ProCard title="节点分配" headerBordered>
                <Table 
                    rowSelection={rowSelection} 
                    dataSource={nodeListData} 
                    columns={columns} 
                />
            </ProCard>
            </ProCard>
        </>
    )
}

export default AssignRoles