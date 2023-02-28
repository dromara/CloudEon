import { ProDescriptions } from '@ant-design/pro-components';
import { Image, Spin } from 'antd';
import styles from './index.less'
import example from '../../../../../../assets/images/1.png';
import { statusColor } from '../../../../../../utils/colonyColor'

const statusTab:React.FC<{statusInfo: API.serviceInfos, loading: boolean}> = ({statusInfo, loading}) => {
    return (
        <div className={styles.statusTabLayout}>
            <Spin tip="Loading" size="small" spinning={!!loading}>
            <div className={styles.statusBar}>
                <div className={styles.leftBox}>
                    {/* <div>版本：19.6.0ffgfhcvbfghjfghjgh</div>
                    <div>监控状态：开启</div>
                    <div>服务配置：正常</div>
                    <div>产品：Basic</div>
                    <div>Kerberos：关闭</div> */}
                    <ProDescriptions
                        title=""
                        dataSource={statusInfo}
                        columns={[
                            {
                                title: '版本',
                                key: 'version',
                                dataIndex: 'version',
                                ellipsis: true,
                                // copyable: true,
                            },
                            {
                                title: '服务状态',
                                key: 'serviceStatus',
                                dataIndex: 'serviceStatus',
                                valueType: 'select',
                                valueEnum: {
                                    all: { text: '全部', status: 'Default' },
                                    open: {
                                        text: '出错',
                                        status: 'Error',
                                    },
                                    closed: {
                                        text: '运行中',
                                        status: 'OPERATING',
                                    },
                                },
                            },
                            {
                                title: '服务实例名',
                                key: 'name',
                                dataIndex: 'name',
                            },
                            {
                                title: '框架服务名',
                                key: 'stackServiceName',
                                dataIndex: 'stackServiceName',
                                ellipsis: true,
                                // copyable: true,
                            },
                            {
                                title: '服务描述',
                                key: 'stackServiceDesc',
                                dataIndex: 'stackServiceDesc',
                                ellipsis: true,
                                // copyable: true,
                            }
                            // {
                            //     title: '时间',
                            //     key: 'date',
                            //     dataIndex: 'date',
                            //     valueType: 'date',
                            // },
                            // {
                            //     title: 'money',
                            //     key: 'money',
                            //     dataIndex: 'money',
                            //     valueType: 'money',
                            // },
                        ]}
                        >
                        </ProDescriptions>
                </div>
                <div className={styles.rightBox}>
                    <div style={{whiteSpace: 'nowrap'}}> 
                        <span style={{backgroundColor: statusColor['SUCCESS']}} 
                            className={styles.statusCircel}>
                        </span>
                        健康
                    </div>
                {/* <ProDescriptions
                    title=""
                    dataSource={{
                        state: 'Success',
                    }}
                    columns={[
                        {
                            title: '监控状态',
                            key: 'state',
                            dataIndex: 'state',
                            valueType: 'select',
                            valueEnum: {
                                all: { text: '全部', status: 'Default' },
                                Error: {
                                    text: '出错',
                                    status: 'Error',
                                },
                                Success: {
                                    text: '健康',
                                    status: 'Success',
                                },
                            },
                        },
                    ]}
                ></ProDescriptions> */}
                </div>
            </div>
            <div>
                <Image width={'100%'} src={example} alt="chucui" />
            </div>
            </Spin>
        </div>
    )
}

export default statusTab;