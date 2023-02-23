import { ProDescriptions } from '@ant-design/pro-components';
import { Image } from 'antd';
import styles from './index.less'
import example from '../../../../../../assets/images/1.png';
import { statusColor } from '../../../../../../utils/colonyColor'

const statusTab:React.FC = () => {
    return (
        <div className={styles.statusTabLayout}>
            <div className={styles.statusBar}>
                <div className={styles.leftBox}>
                    {/* <div>版本：19.6.0ffgfhcvbfghjfghjgh</div>
                    <div>监控状态：开启</div>
                    <div>服务配置：正常</div>
                    <div>产品：Basic</div>
                    <div>Kerberos：关闭</div> */}
                    <ProDescriptions
                        title=""
                        dataSource={{
                            id: '19.6.0ffgfhcvbfghjfghjgh',
                            date: '20200809',
                            money: '1212100',
                            state: 'all',
                            state2: 'open',
                        }}
                        columns={[
                            {
                                title: '版本',
                                key: 'text',
                                dataIndex: 'id',
                                ellipsis: true,
                                // copyable: true,
                            },
                            {
                            title: '监控状态',
                            key: 'state',
                            dataIndex: 'state',
                            valueType: 'select',
                            valueEnum: {
                                all: { text: '全部', status: 'Default' },
                                open: {
                                    text: '未解决',
                                    status: 'Error',
                                },
                                closed: {
                                    text: '已解决',
                                    status: 'Success',
                                },
                            },
                            },
                            {
                                title: '状态2',
                                key: 'state2',
                                dataIndex: 'state2',
                            },
                            {
                                title: '时间',
                                key: 'date',
                                dataIndex: 'date',
                                valueType: 'date',
                            },
                            {
                                title: 'money',
                                key: 'money',
                                dataIndex: 'money',
                                valueType: 'money',
                            },
                        ]}
                        >
                        <ProDescriptions.Item label="百分比" valueType="percent">
                            100
                        </ProDescriptions.Item>
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
                <Image width={700} src={example} alt="chucui" />
            </div>
        </div>
    )
}

export default statusTab;