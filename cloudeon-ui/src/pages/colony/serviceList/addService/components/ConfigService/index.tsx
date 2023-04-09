
import styles from './index.less'
import React, { useState, useEffect, useRef } from 'react';
import { Menu, Form, Table, Button, Typography, Popconfirm, InputNumber, Input, Tooltip, Modal, Select, Slider, Switch } from 'antd';
import { QuestionCircleFilled } from '@ant-design/icons';
import { getServiceConfAPI } from '@/services/ant-design-pro/colony';
import {cloneDeep} from 'lodash'
const { TextArea } = Input;

const ConfigService:React.FC<{setPresetConfListToParams: any}> = ( {setPresetConfListToParams} )=>{

    const [form] = Form.useForm()
    const [currentConfList, setCurrentConfList] = useState<any[]>();
    const [loading, setLoading] = useState(false);
    const [serviceId, setServiceId] = useState(null);
    const [editingKey, setEditingKey] = useState('');
    const [confData, setConfData] = useState({});
    const [addConfigForm] = Form.useForm()
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [customFileNames, setCustomFileNamesData] = useState<any>(); // 当前服务的配置文件
    const [currentNames, setCurrentNames] = useState<any[]>([]); // 当前服务的所有配置项名称，用来校验新增自定义配置的时候配置项名称重复
    const [fileGroupMap, setFileGroupMap] = useState<any>(); // 配置文件名和tag的json数据
    const [fileNameList, setFileList] = useState<any[]>(); // 配置文件名数组
    const [currentFile, setCurrentFile] = useState<any>(); // 当前选中的配置文件tab
    const [currentTag, setCurrentTag] = useState<any>(); // 当前选中的标签tag
    const [filterTableList, setFilterTableList] = useState<any[]>(); // 过滤后的table数据
    
    const getData = JSON.parse(sessionStorage.getItem('colonyData') || '{}')

    const serviceMenu = getData.selectedServiceList.map((sItem: { label: any; id: any; })=>{
        return {
            label: sItem.label,
            key: sItem.id + '',
        }
    })|| []

    const updateConfig = (value:any) => {
        sessionStorage.setItem('allConfData', JSON.stringify(value))
        setConfData(value)
    }


    useEffect(()=>{        
        if(serviceMenu && serviceMenu.length>0){
            setServiceId(serviceMenu[0].key)
            const params = {
                serviceId: serviceMenu[0].key,
                inWizard: true
            }
            getConfListData(params)
        }
    },[])
    

    const getConfListData = async (params: any) => {
        if(confData && confData[params.serviceId]){
            setCurrentConfList(confData[params.serviceId])
            setFilterTableList(confData[params.serviceId])
            setCurrentNames(confData[params.serviceId].map((item: { name: any; })=>{return item.name}))
            const customFileNamesObj = JSON.parse(sessionStorage.getItem('customFileNamesObj') || '{}') 
            setCustomFileNamesData(customFileNamesObj[params.serviceId] || [])
            return
        }else{
            setLoading(true)
            const result: API.ConfList =  await getServiceConfAPI(params);
            setLoading(false)
            const confs = result?.data?.confs?.map(item=>{
                    return {
                        sourceValue: item.recommendExpression,
                        ...item,
                    }
                })
            const fileMap = result?.data?.fileGroupMap
            let files = []
            for(let key in fileMap){
                files.push(key)
            }
            console.log('---files: ', fileMap, files);            
            setFileGroupMap(fileMap)
            setFileList(files)
            setCurrentFile(files[0])
            setCurrentConfList(confs)
            setFilterTableList(confs)
            setCurrentNames(confs?.map((item2)=>{return item2.name}) || [])
            const customFileNamesObj = JSON.parse(sessionStorage.getItem('customFileNamesObj') || '{}') 
            customFileNamesObj[params.serviceId] = result?.data?.customFileNames
            sessionStorage.setItem('customFileNamesObj', JSON.stringify(customFileNamesObj))
            setCustomFileNamesData(result?.data?.customFileNames || [])
            let confResult = {...(confData||{}),[params.serviceId]:confs}
            updateConfig(confResult)
        }
      };

    const onSelectService = function(value: any){
        // if(serviceId){// 保存切换前的数据
        //     let confResult = {...confData,[serviceId]:currentConfList}
        //     updateConfig(confResult)
        // }
        setServiceId(value.key)
        const params = {
            serviceId: value.key,
            inWizard: true
        }
        getConfListData(params)
    }

    const isEditing = (record: Item) => record.name === editingKey;

    const edit = (record: Item) => {
        form.setFieldsValue({ ...record });
        setEditingKey(record.name || '');
    };
    
    const cancel = () => {
        setEditingKey('');
    };

    const handleDelete = (record: Item) => {
        const newData = cloneDeep(currentConfList||[]);
        const index = newData.findIndex(item => record.name === item.name);
        if(index > -1){
            newData.splice(index,1)
            setCurrentConfList(newData);
            deleteFilterData(record) // 处理过滤表格的数据
        }
    }

    const resetSource = (record: Item) => {
        const row = {...record, recommendExpression: record.sourceValue}
        const newData = cloneDeep(currentConfList||[]);
        const index = newData.findIndex(item => row.name === item.name);
        dealItemData(index,newData,row)// 处理保存编辑的逻辑
        dealFilterData(record.name,row) // 处理过滤表格的数据
    }
    const save = async (key: string) => {        
        try {
            const row = (await form.validateFields()) as Item;
            const newData = cloneDeep(currentConfList||[])
            const index = newData.findIndex(item => key === item.name); // name是唯一值，所以用name做主键
            dealItemData(index,newData,row) // 处理保存编辑的逻辑
            dealFilterData(key,row) // 处理过滤表格的数据
        } catch (errInfo) {
            console.log('Validate Failed:', errInfo);
        }
    }
    // 处理总数据的编辑逻辑
    const dealItemData = (index:number,newData:any[],row:any) =>{
        if (index > -1) {
            const item = newData[index];
            newData.splice(index, 1, {
                ...item,
                ...row,
            });
            setCurrentConfList(newData);
            setEditingKey('');
        }else{
            newData.push(row);
            setCurrentConfList(newData);
            setEditingKey('');
        }
        let confResult = serviceId ? {...confData,[serviceId]:newData} : {...confData}
        updateConfig(confResult)
    }

    // 处理过滤表格数据（过滤文件配置、标签）的编辑逻辑
    const dealFilterData = (key:string, row:any) =>{
        if(filterTableList && filterTableList.length>0){
            const newData = cloneDeep(filterTableList)
            const index = newData?.findIndex(item => key === item.name);
            if (index > -1) {
                const item = newData[index];
                newData.splice(index, 1, {
                    ...item,
                    ...row,
                });
                setFilterTableList(newData)
            }else if(row.fileName == currentFile || row.confFile == currentFile || currentFile=="全部"){
                newData.push(row);
                setFilterTableList(newData)
            }
        }
    }
    // 处理过滤数据的删除逻辑
    const deleteFilterData = (record:Item) =>{
        const newData = cloneDeep(filterTableList||[]);
        const index = newData.findIndex(item => record.name === item.name);
        if(index > -1){
            newData.splice(index,1)
            setFilterTableList(newData);
        }
    }

    // 处理过滤数据怎么得到的，从总数据过滤得到
    const handleFiflterTableData = (tag: string, fileName: string)=>{
        if(fileName == '全部' && !tag){
            return currentConfList
        }else if(fileName == '全部' && tag){
            let arr = currentConfList?.filter(item=>{
                return item.tag == tag
            })
            return arr
        }else if(fileName != "全部" && !tag){
            let arr = currentConfList?.filter(item=>{
                return item.confFile == fileName
            })
            return arr
        }else{
            let arr = currentConfList?.filter(item=>{
                return item.confFile == fileName && item.tag == tag
            })
            return arr
        }
    }

    interface Item {
        options: any;
        unit: string;
        min: number;
        max: number;
        valueType: any;
        name: string;
        recommendExpression: string;
        sourceValue: string;
        isCustomConf: boolean;
        confFile:string;
        description: string
    }

    interface EditableCellProps extends React.HTMLAttributes<HTMLElement> {
        editing: boolean;
        dataIndex: string;
        title: any;
        inputType: 'InputNumber' | 'InputString' | 'Slider' | 'Switch' | 'Select';
        record: Item;
        index: number;
        children: React.ReactNode;
      }

    const EditableCell: React.FC<EditableCellProps> = ({
        editing,
        dataIndex,
        title,
        inputType,
        record,
        index,
        children,
        ...restProps
      }) => {
        let inputNode = <Input addonAfter={record?.unit || ''} />
        // const inputNode = inputType === 'number' ? <InputNumber /> : <Input />;
        switch(inputType){
            case 'InputNumber':
                inputNode = <InputNumber max={record.max || Number.MAX_SAFE_INTEGER} min={record.min || Number.MIN_SAFE_INTEGER} addonAfter={record.unit || ''} />
                ;break;
            case 'InputString':;break;
            case 'Slider':
                inputNode = <Slider max={record.max || 100} min={record.min || 0} />
                ;break;
            case 'Switch':
                inputNode = <Switch />
                ;break;
            case 'Select':
                inputNode = 
                        <Select
                                style={{ width: 120 }}
                                options={
                                    record.options.map((opItem: any)=>{
                                        return { value: opItem, label: opItem }
                                    })}
                            />
                        
                ;break;
        }
      
        return (
          <td {...restProps} className={(record?.sourceValue != record?.recommendExpression && !editing && !record.isCustomConf) ? styles.hasEdited:''}>
            {editing ? (
              <Form.Item
                name={dataIndex}
                style={{ margin: 0 }}
                rules={[
                  {
                    required: true,
                    message: `请输入 ${title}!`,
                  },
                ]}
              >
                {inputNode}
              </Form.Item>
            ) : (
              children
            )}
          </td>
        );
      };

    const configColumns = [
        {
            title: '配置项',
            dataIndex: 'name',
            editable: false,
            render: (_: any, record: Item)=>{
                return (<>
                <span>
                    {record.name} 
                    {record.description? <Tooltip title={record.description}><QuestionCircleFilled style={{marginLeft:'5px'}} /></Tooltip>:''}
                </span>
              </>)
            }
            
        },
        // {
        //     title: '配置类型',
        //     dataIndex: 'isCustomConf',
        //     width: 110,
        //     editable: false,
        //     render: (_: any, record: Item)=>{
        //         return (record.isCustomConf?'自定义配置':'预设配置')
        //     }
        // },
        {
            title: '配置文件',
            dataIndex: 'confFile',
            width: 120,
            editable: false,
        },
        {
            title: '值',
            dataIndex: 'recommendExpression',
            editable: true,
            render: (_: any, record: Item) => {
                // console.log('record',record);
                // const hasEdit = (record.sourceValue != record.recommendExpression);
                // return hasEdit && !record.isCustomConf ? (
                //     <div style={{position:'relative'}}>
                //         <Tooltip color="volcano" getPopupContainer={(trigger) => trigger.parentNode} autoAdjustOverflow={false} arrowPointAtCenter={true} visible={true} open={true} placement="rightTop" title={()=>{
                //             return (
                //                 <div style={{color:'#fff', fontWeight:'500'}}>已修改</div>
                //             )
                //         }}>
                //         {record.recommendExpression}
                //         </Tooltip>
                //     </div>
                // ) : (
                  return  <span>{record.recommendExpression}&nbsp;{record.unit?record.unit:''}</span>
                // );
            },
        },
        // {
        //     title: '描述',
        //     dataIndex: 'description',
        //     editable: false,
        //     render: (_: any, record: Item)=>{
        //         return (record.description||'-')
        //     }
        // },
        {
            title: '操作',
            width: 120,
            dataIndex: 'operation',
            render: (_: any, record: Item) => {
                // console.log('record',record);
                const editable = isEditing(record);
                return editable ? (
                    <span>
                    <Typography.Link onClick={() => save(record.name)} style={{ marginRight: 8 }}>
                        确定
                    </Typography.Link>
                    <Typography.Link onClick={() => cancel()}>
                        取消
                    </Typography.Link>
                    {/* <Popconfirm title="Sure to cancel?" onConfirm={cancel}>
                        <a>取消</a>
                    </Popconfirm> */}
                    </span>
                ) : (
                    <div className={styles.actionBtnWrap}>
                        <Typography.Link disabled={editingKey !== ''} style={{marginRight: '20px'}} onClick={() => edit(record)}>
                        编辑
                        </Typography.Link>
                        {
                            !record.isCustomConf?
                            (record.sourceValue != record.recommendExpression) && (
                                <Popconfirm title="确定恢复到初始值吗?" onConfirm={()=>resetSource(record)}>
                                    <a>恢复初始值</a>
                                </Popconfirm>
                            ):(
                                <Popconfirm title="确定删除吗?" onConfirm={()=>handleDelete(record)}>
                                    <a>删除</a>
                                </Popconfirm>
                            )
                        }
                    </div>
                );
            },
          },
    ]

    const mergedColumns = configColumns.map(col => {
        if (!col.editable) {
          return col;
        }
        return {
          ...col,
          onCell: (record: Item) => ({
            record,
            inputType: record.valueType,//'text', //col.dataIndex === 'age' ? 'number' : 'text',
            dataIndex: col.dataIndex,
            title: col.title,
            editing: isEditing(record),
          }),
        };
      });

    // 点击"添加自定义配置"的确认按钮
      const handleOk = () => {
        // console.log(form.getFieldsValue());
        addConfigForm
          .validateFields()
          .then(async (values) => {
            const row = {...values, isCustomConf: true, recommendExpression: values.value};
            const newData = cloneDeep(currentConfList||[]);
            const index = newData.findIndex(item => values.name === item.name);
            if(index == -1){
                const params = [...currentNames, values.name]
                setCurrentNames(params)
            }
            dealItemData(index,newData,row)
            dealFilterData(values.name,row)
            setIsModalOpen(false)
            // const result: API.normalResult = await createNodeAPI({...values, clusterId: getData.clusterId})
            // if(result && result.success){
            //   message.success('新增成功');
            //   getNodeData({ clusterId: getData.clusterId });
            //   setIsModalOpen(false);
            //   form.resetFields()
            // }
          })
          .catch((err) => {
            console.log('err: ', err);
          });
      };
    
      const handleCancel = () => {
        addConfigForm.resetFields()
        setIsModalOpen(false);
      };

    return (
        <div className={styles.CSLayout}>
            <div className={styles.CSLeft}>
                {serviceMenu && serviceMenu[0] && serviceMenu[0].key && (
                    <Menu 
                        mode="inline" 
                        items={serviceMenu} 
                        defaultSelectedKeys={[serviceMenu[0].key]}
                        className={styles.CSLeftMenu} 
                        onSelect={onSelectService} 
                    />
                )}
            </div>
            <div className={styles.CSRight}>
                <div className={styles.CSBtnWrap}>
                    <Button 
                        key="addconfig"
                        type="primary"
                        disabled={!serviceId}
                        onClick={() => {
                            setIsModalOpen(true)
                            addConfigForm.resetFields()
                        }}
                    >
                        添加自定义配置
                    </Button>
                </div>
                <div className={styles.fileTabLayout}>
                    <div className={styles.fileTabBar}>
                        {fileNameList?.map(item => {
                            return (
                                <div 
                                    key={item} 
                                    className={`${styles.fileTabItem} ${currentFile == item ? styles.active:''}`} 
                                    onClick={()=>{
                                        setCurrentTag('')
                                        setCurrentFile(item)
                                        setFilterTableList(handleFiflterTableData('',item))
                                    }}
                                >
                                    {item}
                                </div>
                            )
                        })}
                    </div>
                    <div className={styles.fileTabContent}>
                        <div className={styles.tagListWrap}>
                            {
                                fileGroupMap && fileGroupMap[currentFile] && fileGroupMap[currentFile].map((item:any)=>{
                                    return (
                                        <div 
                                            key={item} 
                                            className={`${styles.tagItem} ${currentTag == item ? styles.activeTag:''}`}
                                            onClick={()=>{
                                                setCurrentTag(item)
                                                setFilterTableList(handleFiflterTableData(item,currentFile))
                                            }}
                                        >
                                                {item}
                                        </div>
                                    )
                                })
                            }
                        </div>
                        <div className={styles.fileTabTable}>
                            <div>
                                <Form form={form} component={false}>
                                    <Table
                                        components={{
                                            body: {
                                                cell: EditableCell,
                                            },
                                        }}
                                        scroll={{
                                            y:600
                                        }}
                                        rowKey="name"
                                        pagination={false}
                                        // bordered
                                        loading={loading}
                                        dataSource={filterTableList} //{currentConfList}
                                        columns={mergedColumns}
                                        rowClassName="editable-row"
                                    />
                                </Form>
                            </div>
                        </div>
                    </div>
                </div>
                
            </div>
            <Modal
                key="addconfigmodal"
                title="添加自定义配置"
                forceRender={true}
                destroyOnClose={true}
                open={isModalOpen}
                onOk={handleOk}
                onCancel={handleCancel}
                // footer={null}
            >
                <div>
                <Form
                    form={addConfigForm}
                    key="addConfigForm"
                    name="添加自定义配置"
                    preserve={false}
                    labelCol={{ span: 6 }}
                    wrapperCol={{ span: 16 }}
                    initialValues={{ remember: true }}
                    // onFinish={onFinish}
                    autoComplete="off"
                >
                    <Form.Item
                        label="配置项"
                        name="name"
                        rules={[
                            { required: true, message: '请输入配置项!' },
                            { validator:(rule, value, callback)=>{
                                try {
                                    if(currentNames && currentNames.includes(value)){
                                        // throw new Error('该配置项已存在，请直接修改!');
                                        return Promise.reject(new Error('该配置项已存在，请直接修改!'));
                                    }
                                    return Promise.resolve();
                                  } catch (err) {
                                    callback(err);
                                  }
                            } }
                        ]}
                    >
                    <Input />
                    </Form.Item>

                    <Form.Item
                        label="值"
                        name="value"
                        rules={[{ required: true, message: '请输入值!' }]}
                    >
                    <Input />
                    </Form.Item>

                    <Form.Item
                        label="配置文件"
                        name="confFile"
                        rules={[{ required: true, message: '请选择配置文件!' }]}
                    >
                        <Select>
                            { customFileNames && customFileNames.map((fileNameItem: any) => {
                                return (<Select.Option key={fileNameItem} value={fileNameItem}>{fileNameItem}</Select.Option>)
                            }) }
                        </Select>
                    </Form.Item>

                    {/* <Form.Item
                        label="描述"
                        name="description"
                        rules={[]}
                    >
                    <TextArea rows={4} placeholder="请输入描述" />
                    </Form.Item> */}
                </Form>
                </div>
            </Modal>
        </div>
    )
}

export default ConfigService;