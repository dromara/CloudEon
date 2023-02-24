
import { ProCard } from '@ant-design/pro-components';
import { Tree, Table } from 'antd';
import type { DataNode, TreeProps } from 'antd/es/tree';
import styles from './index.less'
import { useState, useEffect } from 'react';
import { getNodeListAPI } from '@/services/ant-design-pro/colony';

const AssignRoles : React.FC<{
    serviceList: any[]; // 原本选中的服务的树结构
    sourceServiceInfos: API.ServiceInfosItem[], // 原本选中的服务的树结构+节点(包括上一次选中的)
    setServiceInfosToParams: any
}> = ({serviceList, sourceServiceInfos, setServiceInfosToParams})=>{

    const [selectedRowKeys, setSelectedRowKeys] = useState<React.Key[]>([]); // 选中的节点
    const [nodeListData, setNodeListData] = useState<any[]>();
    const [currentSelectRoles, setCurrentSelectRoles] = useState<selectRoleItem>();
    const [loading, setLoading] = useState(false);
    const [serviceInfos, setServiceInfos] = useState<API.ServiceInfosItem[]>();
    
    const getData = JSON.parse(sessionStorage.getItem('colonyData') || '{}')

    type selectRoleItem = {
        stackServiceId: number,
        stackRoleName: any,
        nodeIds: any[]
    }

    const getNodeData = async (params: any) => {
        setLoading(true)
        const result: API.NodeList =  await getNodeListAPI(params);
        setLoading(false)
        setNodeListData(result?.data)
      };

    const initServiceInfos = () => {
        if(!sourceServiceInfos) return
        setServiceInfos(sourceServiceInfos)
        setServiceInfosToParams(sourceServiceInfos)
        const roleData = {
            stackServiceId: sourceServiceInfos[0].stackServiceId || 0,
            stackRoleName: sourceServiceInfos[0] && sourceServiceInfos[0].roles && sourceServiceInfos[0].roles[0].stackRoleName || '',
            nodeIds: sourceServiceInfos[0] && sourceServiceInfos[0].roles && sourceServiceInfos[0].roles[0].nodeIds || []
        }
        setCurrentSelectRoles(roleData)
        const newSelectedRowKeys = getNodeIds(sourceServiceInfos[0].stackServiceId, serviceList[0].roles[0])
        setSelectedRowKeys(newSelectedRowKeys);
    }

    const treeData = serviceList.map(item=>{
        let itemData = {
            title: item.label,
            key: item.id,
            selectable: false,
            children: item.roles? (item.roles.map((r: any)=>{
                return {
                    title: r,
                    key: r,
                    stackServiceId: item.id
                }
            })):[]
        }
        return itemData
    })

    const getNodeIds:any = (stackServiceId:number, stackRoleName:any) => {
        for (let sItem of (serviceInfos||sourceServiceInfos)) {
            if (sItem.stackServiceId == stackServiceId) {
                for (let role of sItem.roles||[]) {
                    if(role.stackRoleName == stackRoleName)
                    return role.nodeIds||[]
                }
            }
        }      
    }

    const onSelectTree: TreeProps['onSelect'] = (selectedKeys, info) => {
        // console.log('selected', selectedKeys, info);
        const newSelectedRowKeys = getNodeIds(info.selectedNodes[0].stackServiceId, selectedKeys[0])
        setSelectedRowKeys(newSelectedRowKeys);
        const roleData = {
            stackServiceId: info.selectedNodes[0].stackServiceId,
            stackRoleName: selectedKeys[0],
            nodeIds: newSelectedRowKeys
        }
        setCurrentSelectRoles(roleData)
        // console.log('--currentSelectRoles:', currentSelectRoles);
    };

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


    const onSelectChange = (newSelectedRowKeys: number[]) => {
        console.log('selectedRowKeys changed: ', newSelectedRowKeys);
        setSelectedRowKeys(newSelectedRowKeys);
        let roles = {...currentSelectRoles}
        const newServiceInfos = [...(serviceInfos||[])]
        roles.nodeIds = newSelectedRowKeys
        for (let sItem of newServiceInfos||[]) {
            if (sItem.stackServiceId == roles.stackServiceId) {
                for (let role of sItem.roles||[]) {
                    if(role.stackRoleName == roles.stackRoleName)
                    role.nodeIds = newSelectedRowKeys
                }
            }
        } 
        setServiceInfos(newServiceInfos)
        setServiceInfosToParams(newServiceInfos)
        console.log('--serviceInfos:',serviceInfos);
        
    };

    const rowSelection = {
        selectedRowKeys,
        onChange: onSelectChange,
        getCheckboxProps: (record: { name: string; }) => ({
            disabled: !(currentSelectRoles && currentSelectRoles.stackRoleName), 
          }),
      };

    useEffect(() => {
        initServiceInfos()
        getNodeData({ clusterId: getData.clusterId });
    }, []);




    return (
        <>
        <ProCard split="vertical" bordered>
            <ProCard title="服务列表" colSpan="30%" headerBordered className={styles.rolesLeft}>
            <Tree
                defaultExpandAll
                blockNode
                defaultSelectedKeys={[serviceList[0].roles[0]]}
                onSelect={onSelectTree}
                treeData={treeData}
                style={{background: '#fbfbfe'}}
            />
            </ProCard>
            <ProCard title="节点分配" headerBordered>
                <Table 
                    rowKey="id"
                    loading={loading}
                    rowSelection={rowSelection} 
                    columns={columns} 
                    dataSource={nodeListData} 
                />
            </ProCard>
            </ProCard>
        </>
    )
}

export default AssignRoles