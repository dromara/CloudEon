import { BorderOuterOutlined } from '@ant-design/icons';
import { Image } from 'antd'
import styles from './ChooseService.less'

const ChooseService: React.FC<{
    serviceList: any[];
    changeStatus: object;
  }> = ({ serviceList, changeStatus }) => (
    <>
      <div className={styles.notSelectWrap}>
        <div>请选择需要安装的服务</div>
        <div className={styles.serviceListWrap}>
          {serviceList.map((sitem: any) => {
            return <ServiceItem key={sitem.id} item={sitem} type={0} changeStatus={changeStatus} />;
          })}
        </div>
      </div>
      <div className={styles.selectedWrap}>
        <div style={{ paddingLeft: '15px' }}>待安装的服务</div>
        <div className={styles.serviceListWrap}>
          {serviceList
            .filter((fitem: any) => {
              return fitem.selected;
            })
            .map((sitem: any) => {
              return <ServiceItem key={sitem.id} item={sitem} type={1} changeStatus={changeStatus} />;
            })}
        </div>
      </div>
    </>
  );
  
  const ServiceItem: React.FC<{
    item: any;
    type: number;
    changeStatus: any;
  }> = ({ item, type, changeStatus }) => (
    <div className={styles.serviceItem}>
      <div className={styles.serviceItemLeft}>
        {type == 0 ? (
          <>
            <div className={styles.serviceItemIcon}>
              {/* <BorderOuterOutlined /> */}
              <Image src={'data:image/jpeg;base64,'+item.iconApp} className={styles.serviceItemIcon} alt="" />
            </div>
            <div className={styles.serviceItemCenter}>
              <div className={styles.serviceItemTitle}>{item.label}</div>
              <div className={styles.serviceItemDesc}>{item.description}</div>
            </div>
          </>
          ):(
            <div style={{display:'flex',flexDirection: 'column'}}>
              <div style={{display:'flex',flexDirection: 'row', alignItems: 'center'}}>
                  {/* <BorderOuterOutlined /> */}
                  <Image src={'data:image/jpeg;base64,'+item.iconApp} className={styles.serviceItemIcon} alt="" />
                <div className={styles.serviceItemTitle}>{item.label}</div>
              </div>
              <div className={styles.serviceItemCenter}>
                <div className={styles.serviceItemDesc}>{item.description}</div>
                <div className={styles.serviceItemDesc}>dockerImage：{item.dockerImage}</div>
                <div className={styles.serviceItemDesc}>版本：{item.version}</div>
              </div>
            </div>
          )
        }
      </div>
      <div>
        <div>
          {type == 0 ? (
            item.selected ? (
              <div className={styles.disabledBtn}>已添加</div>
            ) : (
              <div
                className={styles.activeBtn}
                onClick={() => {
                  changeStatus(item.id);
                }}
              >
                添加
              </div>
            )
          ) : (
            <div
                className={styles.activeBtn}
                onClick={() => {
                  changeStatus(item.id);
                }}
            >移除</div>
          )}
        </div>
      </div>
    </div>
  );

  export default ChooseService