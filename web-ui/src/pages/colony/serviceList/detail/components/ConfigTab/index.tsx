
import styles from './index.less'
import React, { useState, useEffect, useRef } from 'react';
import { Menu, Form, Table, Button, Typography, Popconfirm, InputNumber, Input, Tooltip, Modal, Select, Slider, Switch, message } from 'antd';
import { getServiceConfAPI, getListConfsAPI, saveServiceConfAPI } from '@/services/ant-design-pro/colony';
const { TextArea } = Input;
import {cloneDeep} from 'lodash'

const ConfigService:React.FC<{serviceId: number}> = ( {serviceId} )=>{

    const [form] = Form.useForm()
    const [initConfList, setInitConfList] = useState<any[]>();
    const [currentConfList, setCurrentConfList] = useState<any[]>();
    const [loading, setLoading] = useState(false);
    // const [serviceId, setServiceId] = useState(null);
    const [editingKey, setEditingKey] = useState('');
    const [isEditMode, setIsEditMode] = useState(false);
    // const [confData, setConfData] = useState({});
    const [addConfigForm] = Form.useForm()
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [customFileNames, setCustomFileNamesData] = useState<any>(); // 当前服务的配置文件
    const [currentNames, setCurrentNames] = useState<any[]>([]); // 当前服务的所有配置项名称，用来校验新增自定义配置的时候配置项名称重复
    
    // const getData = JSON.parse(sessionStorage.getItem('colonyData') || '{}')

    // const serviceMenu = getData.selectedServiceList.map((sItem: { label: any; id: any; })=>{
    //     return {
    //         label: sItem.label,
    //         key: sItem.id + '',
    //     }
    // })|| []

    // const updateConfig = (value:any) => {
    //     sessionStorage.setItem('allConfData', JSON.stringify(value))
    //     setConfData(value)
    // }


    useEffect(()=>{
        getConfListData()
    },[])
    

    const getConfListData = async () => {
        const params = {serviceInstanceId: serviceId}
        // if(confData && confData[params.serviceId]){
        //     setCurrentConfList(confData[params.serviceId])
        //     setCurrentNames(confData[params.serviceId].map((item: { name: any; })=>{return item.name}))
        //     const customFileNamesObj = JSON.parse(sessionStorage.getItem('customFileNamesObj') || '{}') 
        //     setCustomFileNamesData(customFileNamesObj[params.serviceId] || [])
        //     return
        // }else{
            setLoading(true)
            const result: API.ConfList =  await getListConfsAPI(params);
            setLoading(false)
            const confs = result?.data?.confs?.map((item,index)=>{
                // console.log('item: ', item, 'index:', index);
                    return {
                        sourceValue: item.valueType == "Switch" ? eval(item.recommendExpression||'false') : item.recommendExpression,
                        ...item,
                        value: item.valueType == "Switch" ? eval(item.value||'false') : item.value,
                        recommendExpression: item.valueType == "Switch" ? eval(item.recommendExpression||'false') : item.recommendExpression,
                    }
                })
            // const deepCf = JSON.parse(JSON.stringify(confs))
            setInitConfList(cloneDeep(confs))
            setCurrentConfList(cloneDeep(confs))
            setCurrentNames(confs?.map((item2)=>{return item2.name}) || [])
            const customFileNames = result?.data?.customFileNames
            setCustomFileNamesData(customFileNames)
            // const customFileNamesObj = JSON.parse(sessionStorage.getItem('customFileNamesObj') || '{}') 
            // customFileNamesObj[params.serviceId] = result?.data?.customFileNames
            // sessionStorage.setItem('customFileNamesObj', JSON.stringify(customFileNamesObj))
            // setCustomFileNamesData(result?.data?.customFileNames || [])
            // let confResult = {...(confData||{}),[params.serviceId]:confs}
            // updateConfig(confResult)
        // }
      };

    // const onSelectService = function(value: any){
    //     // if(serviceId){// 保存切换前的数据
    //     //     let confResult = {...confData,[serviceId]:currentConfList}
    //     //     updateConfig(confResult)
    //     // }
    //     setServiceId(value.key)
    //     const params = {
    //         serviceId: value.key,
    //         inWizard: true
    //     }
    //     getConfListData(params)
    // }

    // const isEditing = (record: Item) => record.name === editingKey;

    // const edit = (record: Item) => {
    //     form.setFieldsValue({ ...record });
    //     setEditingKey(record.name || '');
    // };
    
    // const cancel = () => {
    //     setEditingKey('');
    // };

    const handleDelete = (record: Item) => {
        const newData = [...(JSON.parse(JSON.stringify(currentConfList))||[])];
        const index = newData.findIndex(item => record.name === item.name);
        if(index > -1){
            newData.splice(index,1)
            console.log('--newData: ', newData);
            
            setCurrentConfList(newData);
        }
    }

    const resetSource = (record: Item, index:number) => {
        console.log('---record: ', record);
        record.value = record.recommendExpression
        setCurrentConfList(JSON.parse(JSON.stringify(currentConfList)));
        console.log('---currentConfList: ', currentConfList);
        form.setFieldValue(`${index}-value`, record.recommendExpression)
        // const row = {...record, value: record.recommendExpression}
        // const newData = [...(currentConfList||[])];
        // const index = newData.findIndex(item => row.name === item.name);
        // dealItemData(index,newData,row)
        
    }

    // const addRow = (row:any) => {
    //     let newData = [...(currentConfList||[])];
    //     newData.push(row)
    //     setCurrentConfList(newData);
    // }

    const submitEdit = async() => {
        const presetConfList = currentConfList?.filter(f=> !f.isCustomConf).map(item=>{
            return {
                name: item.name,
                value: item.value,
                id: item.id,
            }
        })
        const customConfList = currentConfList?.filter(f=> f.isCustomConf).map(item=>{
            return {
                name: item.name,
                value: item.value,
                confFile: item.confFile,
                id: item.id,
            }
        })

        const params = {
            serviceInstanceId: serviceId,
            presetConfList,
            customConfList
        }
        console.log('---params:', params);
        
        const result = await saveServiceConfAPI(params)
        if(result && result.success){
            message.success('保存成功')
            setIsEditMode(false)
            getConfListData()
          }

    }

    // const save = async (key: React.Key) => {        
    //     try {
    //         const row = (await form.validateFields()) as Item;
    //         const newData = [...(currentConfList||[])];
    //         const index = newData.findIndex(item => key === item.name);
    //         dealItemData(index,newData,row)
    //     } catch (errInfo) {
    //         console.log('Validate Failed:', errInfo);
    //     }
    // }

    // const dealItemData = (index:number,newData:any[],row:any) =>{
    //     if (index > -1) {
    //         const item = newData[index];
    //         newData.splice(index, 1, {
    //             ...item,
    //             ...row,
    //         });
    //         setCurrentConfList(newData);
    //         setEditingKey('');
    //     }else{
    //         newData.push(row);
    //         setCurrentConfList(newData);
    //         setEditingKey('');
    //     }
    //     // let confResult = serviceId ? {...confData,[serviceId]:newData} : {...confData}
    //     // updateConfig(confResult)
    // }

    interface Item {
        id?: number;
        value?: any;
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

    // interface EditableCellProps extends React.HTMLAttributes<HTMLElement> {
    //     editing: boolean;
    //     dataIndex: string;
    //     title: any;
    //     inputType: 'InputNumber' | 'InputString' | 'Slider' | 'Switch' | 'Select';
    //     record: Item;
    //     index: number;
    //     children: React.ReactNode;
    //   }

    // const EditableCell: React.FC<EditableCellProps> = ({
    //     editing,
    //     dataIndex,
    //     title,
    //     inputType,
    //     record,
    //     index,
    //     children,
    //     ...restProps
    //   }) => {
    //     let inputNode = <Input addonAfter={record?.unit || ''} />
    //     // const inputNode = inputType === 'number' ? <InputNumber /> : <Input />;
    //     switch(inputType){
    //         case 'InputNumber':
    //             inputNode = <InputNumber max={record.max || Number.MAX_SAFE_INTEGER} min={record.min || Number.MIN_SAFE_INTEGER} addonAfter={record.unit || ''} />
    //             ;break;
    //         case 'InputString':;break;
    //         case 'Slider':
    //             inputNode = <Slider max={record.max || 100} min={record.min || 0} />
    //             ;break;
    //         case 'Switch':
    //             inputNode = <Switch />
    //             ;break;
    //         case 'Select':
    //             inputNode = 
    //                     <Select
    //                             style={{ width: 120 }}
    //                             options={
    //                                 record.options.map((opItem: any)=>{
    //                                     return { value: opItem, label: opItem }
    //                                 })}
    //                         />
                        
    //             ;break;
    //     }
      
    //     return (
    //       <td {...restProps} className={(record?.sourceValue != record?.recommendExpression && !editing && !record.isCustomConf) ? styles.hasEdited:''}>
    //         {editing ? (
    //           <Form.Item
    //             name={dataIndex}
    //             style={{ margin: 0 }}
    //             rules={[
    //               {
    //                 required: true,
    //                 message: `请输入 ${title}!`,
    //               },
    //             ]}
    //           >
    //             {inputNode}
    //           </Form.Item>
    //         ) : (
    //           children
    //         )}
    //       </td>
    //     );
    //   };

    // const actionBlur = (e:any, record: Item)=>{
    //     console.log(e.target.value)
    //     record.value = e.target.value
    // }
    const actionOnChange = (val:any, record: Item) => {
        record.value = val
        let cdata = currentConfList ? JSON.parse(JSON.stringify(currentConfList)) :[]
        cdata && setCurrentConfList(cdata)
    }

    const configColumns = [
        {
            title: '配置项',
            dataIndex: 'name',
            editable: false,
        },{
            title: '配置类型',
            dataIndex: 'isCustomConf',
            width: 110,
            editable: false,
            render: (_: any, record: Item)=>{
                return (record.isCustomConf?'自定义配置':'预设配置')
            }
        },{
            title: '配置文件',
            dataIndex: 'confFile',
            width: 120,
            editable: false,
        },{
            title: '值',
            dataIndex: 'value',
            editable: true,
            render: (_: any, record: Item, index: any) => {
                <span>{record.value}&nbsp;{record.unit?record.unit:''}</span>
                let inputNode = <Input onChange={(e)=>actionOnChange(e,record)} addonAfter={record?.unit || ''} />
                // const inputNode = inputType === 'number' ? <InputNumber /> : <Input />;
                switch(record.valueType){
                    case 'InputNumber':
                        inputNode = <InputNumber onChange={(e)=>actionOnChange(e,record)} max={record.max || Number.MAX_SAFE_INTEGER} min={record.min || Number.MIN_SAFE_INTEGER} addonAfter={record.unit || ''} />
                        ;break;
                    case 'InputString':;break;
                    case 'Slider':
                        inputNode = <Slider onChange={(e)=>actionOnChange(e,record)} max={record.max || 100} min={record.min || 0} />
                        ;break;
                    case 'Switch':
                        inputNode = <Switch checked={record.value} onChange={(e)=>actionOnChange(e,record)} />
                        ;break;
                    case 'Select':
                        inputNode = 
                                <Select onChange={(e)=>actionOnChange(e,record)}
                                        style={{ width: 120 }}
                                        options={
                                            record.options.map((opItem: any)=>{
                                                return { value: opItem, label: opItem }
                                            })}
                                    />
                                
                        ;break;
                }
                  return  isEditMode?(
                    <Form.Item
                        name={`${index}-value`}
                        initialValue={record.value}
                        // key={record.name}
                        style={{ margin: 0 }}
                        rules={[
                        {
                            required: true,
                            message: `请输入值/选择值!`,
                        },
                        ]}
                    >
                        {inputNode}
                    </Form.Item>                    
                  ):(<> <span>{record.valueType == "Switch" ?record.value.toString() : record.value}&nbsp;{record.unit?record.unit:''}</span> </>)
            },
        },{
            title: '描述',
            dataIndex: 'description',
            editable: false,
            render: (_: any, record: Item)=>{
                return (record.description||'-')
            }
        },{
            title: '操作',
            width: 120,
            dataIndex: 'operation',
            render: (_: any, record: Item, index:any) => {
                let resultDom1 = <></>
                let resultDom2 = <></>
                const formData = form.getFieldsValue(true)
                if(isEditMode){
                    if(formData[`${index}-value`] != record.recommendExpression && !record.isCustomConf){
                        resultDom1 = (<div style={{marginRight: '5px'}}>
                            <Popconfirm title="确定恢复到初始值吗?" onConfirm={()=>resetSource(record,index)}>
                                <a >恢复初始值</a>
                            </Popconfirm>
                        </div>)
                    }
                    if(record.isCustomConf){
                        resultDom2 = (
                            <div>
                                <Popconfirm title="确定删除吗?" onConfirm={()=>handleDelete(record)}>
                                    <a>删除</a>
                                </Popconfirm>
                            </div>
                            
                        )
                    }
                }
                return <>{resultDom1}{resultDom2}</>
                // console.log('record',record);
                // const editable = isEditing(record);
                // return editable ? (
                //     <span>
                //     <Typography.Link onClick={() => save(record.name)} style={{ marginRight: 8 }}>
                //         确定
                //     </Typography.Link>
                //     <Typography.Link onClick={() => cancel()}>
                //         取消
                //     </Typography.Link>
                //     {/* <Popconfirm title="Sure to cancel?" onConfirm={cancel}>
                //         <a>取消</a>
                //     </Popconfirm> */}
                //     </span>
                // ) : (
                //     <div className={styles.actionBtnWrap}>
                //         <Typography.Link disabled={editingKey !== ''} style={{marginRight: '20px'}} onClick={() => edit(record)}>
                //         编辑
                //         </Typography.Link>
                //         {
                //             !record.isCustomConf?
                //             (record.sourceValue != record.recommendExpression) && (
                //                 <Popconfirm title="确定恢复到初始值吗?" onConfirm={()=>resetSource(record)}>
                //                     <a>恢复初始值</a>
                //                 </Popconfirm>
                //             ):(
                //                 <Popconfirm title="确定删除吗?" onConfirm={()=>handleDelete(record)}>
                //                     <a>删除</a>
                //                 </Popconfirm>
                //             )
                //         }
                //     </div>
                // );
            },
          },
    ]

    const mergedColumns = configColumns.map(col => {
        if (!col.editable) {
          return col;
        }
        return {
          ...col,
        //   onCell: (record: Item) => ({
        //     record,
        //     inputType: record.valueType,//'text', //col.dataIndex === 'age' ? 'number' : 'text',
        //     dataIndex: col.dataIndex,
        //     title: col.title,
        //     editing: isEditMode,//isEditing(record),
        //   }),
        };
      });

    
      const handleOk = () => {
        // console.log(form.getFieldsValue());
        addConfigForm
          .validateFields()
          .then(async (values) => {
            const row = {...values, isCustomConf: true, recommendExpression: values.value};
            const newData = currentConfList||[];
            const index = newData.findIndex(item => values.name === item.name);
            if(index == -1){
                const params = [...currentNames, values.name]
                setCurrentNames(params)
            }
            // dealItemData(index,newData,row)
            newData.push(row)
            setCurrentConfList(newData);
            setIsModalOpen(false)
          })
          .catch((err) => {
            console.log('err: ', err);
          });
      };
    
      const handleCancel = () => {
        addConfigForm.resetFields()
        setIsModalOpen(false);
      };

    const getFormInitData = (listdata: any[]) => {
        let formList:{ [key: string]: any } = {}
        for(let i =0;i<listdata.length;i++){
            formList[`${i}-value`] = listdata[i].value
        }
        return formList
        // let formList = []
        // for(let i =0;i<listdata.length;i++){
        //     formList.push(listdata[i].value)
        // }
        // return formList
    }

    return (
        <div className={styles.ConfigTabLayout}>
            {/* <div className={styles.CSLeft}>
                {serviceMenu && serviceMenu[0] && serviceMenu[0].key && (
                    <Menu 
                        mode="inline" 
                        items={serviceMenu} 
                        defaultSelectedKeys={[serviceMenu[0].key]}
                        className={styles.CSLeftMenu} 
                        onSelect={onSelectService} 
                    />
                )}
            </div> */}
            <div className={styles.CSRight}>
                <div className={styles.btnsBar}>
                    {
                        !isEditMode?(
                        <>
                            <Button 
                                key="editBtn"
                                type="primary"
                                disabled={!serviceId || loading}
                                onClick={() => {
                                    setIsEditMode(true)
                                }}
                            >
                                编辑配置项
                            </Button>
                        </>):(
                        <>
                            <Button 
                                key="saveBtn"
                                type="primary"
                                disabled={!serviceId || loading}
                                onClick={() => {
                                    console.log('--currentConfList: ', currentConfList, form.getFieldsValue(true));
                                    submitEdit()
                                }}
                            >
                                保存
                            </Button>
                            <Button 
                                key="cancelBtn"
                                type="primary"
                                disabled={!serviceId || loading}
                                onClick={() => {
                                    setCurrentConfList(cloneDeep(initConfList))
                                    // console.log('--initConfList:', initConfList, getFormInitData(initConfList||[]));
                                    form.setFieldsValue(getFormInitData(initConfList||[]))
                                    // form.resetFields();
                                    setIsEditMode(false)
                                }}
                            >
                                取消
                            </Button>
                           
                            <Button 
                                key="addconfig"
                                type="primary"
                                disabled={!serviceId || loading}
                                onClick={() => {
                                    setIsModalOpen(true)
                                    addConfigForm.resetFields()
                                }}
                            >
                                添加自定义配置
                            </Button>
                        </>)
                    }
                </div>
                <div>
                <Form form={form} component={false}>
                    <Table
                        // components={{
                        //     body: {
                        //         cell: EditableCell,
                        //     },
                        // }}
                        scroll={{
                            y:600
                        }}
                        rowKey="name"
                        pagination={false}
                        bordered
                        loading={loading}
                        dataSource={currentConfList}
                        columns={mergedColumns}
                        rowClassName="editable-row"
                    />
                </Form>
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
                                    console.log(err);
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