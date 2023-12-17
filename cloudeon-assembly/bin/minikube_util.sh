#!/usr/bin/expect
# 参数提示
if { $argc != 2 } {
    send_user "Usage: expect_script <node_name> <password>\n"
    exit 1
}

# 获取参数值
set node_name [lindex $argv 0]
set new_root_password [lindex $argv 1]

# 获取节点ip
spawn ping -c 1 $node_name
expect eof
set output $expect_out(buffer)
regexp {\((\d+\.\d+\.\d+\.\d+)\)} $output match host_ip
puts "Detected IP for $node_name: $host_ip"

# 登录节点
spawn minikube ssh -n $node_name
expect "$ "

# 修改节点名映射的ip
send "sudo sed -i 's/127.0.1.1/$host_ip/' /etc/hosts\r"
expect "$ "

# 设置root密码
send "sudo passwd\r"
expect "New password: "
send "$new_root_password\r"
expect "Retype password: "
send "$new_root_password\r"
expect "$ "

# 配置允许root远程登录
send "sudo sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config\r"
expect "$ "
#send "sudo systemctl restart sshd\r"
#expect "$ "
send "nohup sudo systemctl restart sshd &\r"

# 退出
send "exit\r"
expect eof
