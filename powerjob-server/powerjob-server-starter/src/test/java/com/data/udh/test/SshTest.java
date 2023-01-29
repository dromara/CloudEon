package com.data.udh.test;

import com.data.udh.utils.SshUtils;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.session.ClientSession;
import org.junit.jupiter.api.Test;

@Slf4j
public class SshTest {


    @Test
    public void execCmdWithResult() {
        ClientSession session = SshUtils.openConnectionByPassword(
                "fl001",
                22,
                "root",
                "Ltcyhlwylym@admin2021zi!");

        SshUtils.uploadFile(session,"/tmp/hzk-open", "/Users/huzekang/Downloads/apache-zookeeper-3.6.3-bin.tar.gz");
        SshUtils.execCmdWithResult(session,"tar -zxvf /tmp/hzk-open/apache-zookeeper-3.6.3-bin.tar.gz -C /tmp/hzk-open");

    }




}
