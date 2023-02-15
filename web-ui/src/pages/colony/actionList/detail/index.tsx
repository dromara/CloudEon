import { PageContainer, ProCard } from '@ant-design/pro-components';
import { Progress } from 'antd';
import styles from './index.less'
import { /*commandInfos,*/ statusColor,trailColor } from '../common'
import { formatDate } from '@/utils/common'
import { useState, useEffect, ReactChild, ReactFragment, ReactPortal } from 'react';
import { getCommandDetailAPI } from '@/services/ant-design-pro/colony';
import { FormattedMessage, history, SelectLang, useIntl, useModel } from 'umi';
const actionDetail: React.FC = () => {

  const [selectedTask, setSelectedTask] = useState('ZOOKEEPER1');
  const [loading, setLoading] = useState(false);
  const [commandInfos, setCommandInfos] = useState<commandType>();
  

  const getTableData = async (params: any) => {
    setLoading(true)
    const result: API.commandResult =  await getCommandDetailAPI(params);
    setLoading(false)
    if(result && result.success){
        setCommandInfos(result?.data)
    }
  };

  interface commandType {
    "id": number,
    "name": string,
    "type": string,
    "commandState": string,
    "submitTime": string,
    "startTime": string,
    "endTime": string,
    "currentProgress": number,
    "operateUserId": number,
    "clusterId": number,
    // "tasksMap":
  }


  useEffect(() => {
    const { query } = history.location;
    getTableData({ commandId: query?.commandId });
  }, []);

    
    return (
        <PageContainer header={{title:''}}>
            {commandInfos && (
            <div className={styles.commandLayout}>
            <div className={styles.titleBar}>{commandInfos.name}{`( ${commandInfos.currentProgress}/100) `}</div>
                <div className={styles.titleWrap}>
                    {/* <div className={styles.titleBar}>{commandInfos.name}</div> */}
                    <div className={styles.otherInfo}>
                        <div className={styles.otherInfoItem}>
                            状态：
                            <div style={{color:statusColor[commandInfos.commandState], whiteSpace: 'nowrap',display:'inline-block'}}> 
                                <span style={{backgroundColor: statusColor[commandInfos.commandState]}} className={styles.statusCircel}>
                                </span>
                                {commandInfos.commandState}
                            </div>
                        </div>
                        <div className={styles.otherInfoItem}>提交时间：{formatDate(commandInfos.submitTime, 'yyyy-MM-dd hh:mm:ss')}</div>
                        <div className={styles.otherInfoItem}>开始时间：{formatDate(commandInfos.startTime, 'yyyy-MM-dd hh:mm:ss')}</div>
                        <div className={styles.otherInfoItem}>结束时间：{formatDate(commandInfos.endTime, 'yyyy-MM-dd hh:mm:ss')}</div>
                        <div className={styles.otherInfoItem}>操作人：{commandInfos.operateUserId}</div>
                    </div>
                    <div className={styles.allProcessBar}><div style={{width:'60px'}}>进度：</div>
                        <Progress 
                            percent={commandInfos.currentProgress} 
                            strokeWidth={20} 
                            strokeColor={statusColor[commandInfos.commandState]} 
                            size="small" 
                            trailColor={trailColor[commandInfos.commandState]}
                            status={commandInfos.commandState=='RUNNING'?"active":'normal'} 
                            style={{minWidth:'150px'}}
                        />
                    </div>
                </div>
                <div className={styles.content}>
                    <div className={styles.overallInfo}>
                        <div className={styles.contentLeft}>
                            {Object.keys(commandInfos.tasksMap).map( taskItem => {
                                return (
                                    <div 
                                        className={[styles.taskmenu, selectedTask==taskItem ? styles.activetaskmenu :null].join(' ')}
                                        onClick={()=>{
                                            setSelectedTask(taskItem)
                                        }}
                                    >{taskItem}</div>
                                )
                            })}
                        </div>
                        <div className={styles.contentRight}>
                            {
                                commandInfos.tasksMap[selectedTask].map( 
                                    (taskDetail: 
                                        { taskName: boolean | ReactChild | ReactFragment | ReactPortal | null | undefined; 
                                          progress: number | undefined; 
                                          commandState: string; 
                                          startTime: string
                                        }) =>{
                                    return (
                                        <div className={styles.taskDetailItem}>
                                            <div className={styles.taskDetailCenter}>
                                                <div className={styles.taskName}>{taskDetail.taskName}</div>
                                                <div className={styles.taskTimeWrap}>
                                                    <Progress 
                                                        percent={taskDetail.progress} 
                                                        strokeWidth={10} 
                                                        strokeColor={statusColor[taskDetail.commandState]} 
                                                        size="small" 
                                                        status={taskDetail.commandState=='RUNNING'?"active":'normal'} 
                                                        style={{width:'450px'}}
                                                    />
                                                </div>
                                            </div>
                                            <div className={styles.taskDetailRight}>
                                                <div className={styles.taskTime}>开始：{formatDate(taskDetail.startTime, 'yyyy-MM-dd hh:mm:ss')}</div>
                                                <div className={styles.taskTime}>结束：{formatDate(taskDetail.startTime, 'yyyy-MM-dd hh:mm:ss')}</div>
                                            </div>
                                            <div className={styles.taskDetailLog}>
                                                <a onClick={()=>{
                                                    }}> 日志 </a>
                                            </div>
                                        </div>
                                    )
                                })
                            }

                        </div>
                    </div>
                </div>
            </div>

            )}
        </PageContainer>
    )
}

export default actionDetail