
import styles from './ConfigService.less'
import { ProCard } from '@ant-design/pro-components';
import { Menu } from 'antd';

const ConfigService:React.FC = ()=>{
    const items = [
        { label: '菜单项一', key: 'item-1' }, // 菜单项务必填写 key
        { label: '菜单项二', key: 'item-2' },
        {
          label: '子菜单',
          key: 'submenu',
          children: [{ label: '子菜单项', key: 'submenu-item-1' }],
        },
      ];
    return (
        <div className={styles.CSLayout}>
            <div className={styles.CSLeft}>
                <Menu items={items} />
            </div>
            <div className={styles.CSRight}>
                111
            </div>
        </div>
    )
}

export default ConfigService;