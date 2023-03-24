import { ProDescriptions } from '@ant-design/pro-components';
import { Image, Spin } from 'antd';
import styles from './index.less'
import example from '../../../../../../assets/images/1.png';
import { statusColor } from '../../../../../../utils/colonyColor'

const statusTab:React.FC<{statusInfo: API.serviceInfos, dashboardUrl:string, loading: boolean}> = ({statusInfo,dashboardUrl, loading}) => {
    
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
            <div className={styles.dashboardWrap}>
                <iframe src="https://4x.ant.design/components/progress-cn/" style={{border:0, width:'100%',height:'100%'}}></iframe>
            </div>
            </Spin>
        </div>
    )
}

export default statusTab;