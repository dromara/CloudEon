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
                    <div>
                        <div>版本：</div>
                        <div>{statusInfo.version}</div>
                    </div>
                    <div>
                        <div>dockerImage：</div>
                        <div>{statusInfo.dockerImage}</div>
                    </div>
                    <div>
                        <div>服务实例名：</div>
                        <div>{statusInfo.name}</div>
                    </div>
                    <div>
                        <div>框架服务名：</div>
                        <div>{statusInfo.stackServiceName}</div>
                    </div>
                    <div>
                        <div>服务描述：</div>
                        <div>{statusInfo.stackServiceDesc}</div>
                    </div>
                    {/* <ProDescriptions
                        title=""
                        dataSource={statusInfo}
                        columns={[
                            {
                                title: '版本',
                                key: 'version',
                                dataIndex: 'version',
                                ellipsis: true,
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
                            },
                            {
                                title: '服务描述',
                                key: 'stackServiceDesc',
                                dataIndex: 'stackServiceDesc',
                                ellipsis: true,
                            },
                            {
                                title: '镜像',
                                key: 'dockerImage',
                                dataIndex: 'dockerImage',
                                ellipsis: true,
                            }
                        ]}
                        >
                        </ProDescriptions> */}
                </div>
                <div className={styles.rightBox}>
                    <div style={{whiteSpace: 'nowrap'}}> 
                        <div>服务状态：</div>
                        <div className={styles.statusTitle}>
                            <span style={{backgroundColor: statusColor['SUCCESS']}} 
                                className={styles.statusCircel}>
                            </span>
                            健康
                        </div>
                    </div>
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