import { Button, Radio } from 'antd';
import { useState, useEffect } from 'react';
const ConfigSecurity: React.FC = () =>{
    const [securityMode, setSecurityMode] = useState('简单认证模式');
    return (
        <Radio.Group value={securityMode} onChange={e => setSecurityMode(e.target.value)}>
            <Radio.Button value="简单认证模式">简单认证模式</Radio.Button>
            <Radio.Button value="Kerberos认证模式" disabled>Kerberos认证模式</Radio.Button>
        </Radio.Group>
    )
}

export default ConfigSecurity