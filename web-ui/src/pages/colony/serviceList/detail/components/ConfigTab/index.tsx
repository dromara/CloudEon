

import { Menu, Form, Table, Button, Typography, Popconfirm, InputNumber, Input, Tooltip, Modal } from 'antd';
import React, { useState, useEffect, useRef } from 'react';


const ConfigTab:React.FC = () => {
    // const [form] = Form.useForm()
    // const [loading, setLoading] = useState(false);
    // const [serviceId, setServiceId] = useState(null);
    // const [editingKey, setEditingKey] = useState('');
    // const [confData, setConfData] = useState({});

    // interface Item {
    //     name: string;
    //     recommendExpression: string;
    //     sourceValue: string;
    // }

    // interface EditableCellProps extends React.HTMLAttributes<HTMLElement> {
    //     editing: boolean;
    //     dataIndex: string;
    //     title: any;
    //     inputType: 'number' | 'text';
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
    //     const inputNode = inputType === 'number' ? <InputNumber /> : <Input />;
      
    //     return (
    //       <td {...restProps}>
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

    //   const isEditing = (record: Item) => record.name === editingKey;

    // const configColumns = [
    //     {
    //         title: '配置项',
    //         dataIndex: 'name',
    //         editable: false,
    //     },{
    //         title: '值',
    //         dataIndex: 'recommendExpression',
    //         editable: true,
    //         render: (_: any, record: Item) => {
    //             // console.log('record',record);
    //             const hasEdit = (record.sourceValue != record.recommendExpression);
    //             return hasEdit ? (
    //                 <div style={{position:'relative'}}>
    //                     <Tooltip color="volcano" getPopupContainer={(trigger) => trigger.parentNode} autoAdjustOverflow={false} arrowPointAtCenter={true} visible={true} open={true} placement="rightTop" title={()=>{
    //                         return (
    //                             <div style={{color:'#fff', fontWeight:'500'}}>已修改</div>
    //                         )
    //                     }}>
    //                     {record.recommendExpression}
    //                     </Tooltip>
    //                 </div>
    //             ) : (
    //                 <span>{record.recommendExpression}</span>
    //             );
    //         },
    //     },{
    //         title: '操作',
    //         dataIndex: 'operation',
    //         render: (_: any, record: Item) => {
    //             // console.log('record',record);
    //             const editable = isEditing(record);
    //             return editable ? (
    //                 <span>
    //                 <Typography.Link onClick={() => save(record.name)} style={{ marginRight: 8 }}>
    //                     确定
    //                 </Typography.Link>
    //                 <Typography.Link onClick={() => cancel()}>
    //                     取消
    //                 </Typography.Link>
    //                 {/* <Popconfirm title="Sure to cancel?" onConfirm={cancel}>
    //                     <a>取消</a>
    //                 </Popconfirm> */}
    //                 </span>
    //             ) : (
    //                 <span>
    //                     <Typography.Link disabled={editingKey !== ''} onClick={() => edit(record)}>
    //                     编辑
    //                     </Typography.Link>
    //                     {
    //                         (record.sourceValue != record.recommendExpression) && (
    //                             <Popconfirm title="确定恢复到初始值吗?" onConfirm={()=>resetSource(record)}>
    //                                 <a style={{display:'inline-block',marginLeft: '20px'}}>恢复初始值</a>
    //                             </Popconfirm>
    //                         )
    //                     }
    //                 </span>
    //             );
    //         },
    //       },
    // ]

    return (
        <div>
            {/* <Form form={form} component={false}>
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
                        bordered
                        loading={loading}
                        dataSource={currentConfList}
                        columns={mergedColumns}
                        rowClassName="editable-row"
                    />
                </Form> */}
        </div>
    )
}

export default ConfigTab