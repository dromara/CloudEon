package com.data.udh.test;

import com.data.udh.utils.SshUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.session.ClientSession;
import org.junit.jupiter.api.Test;

import java.io.IOException;

@Slf4j
public class SshTest {


    @Test
    public void execCmdWithResult() throws IOException {
        ClientSession session = SshUtils.openConnectionByPassword(
                "10.81.16.19",
                22,
                "root",
                "Ltcyhlwylym@admin2021zi!");

//        SshUtils.uploadFile(session,"/opt/udh/ZOOKEEPER1/conf", "/Users/huzekang/Downloads/apache-zookeeper-3.6.3-bin.tar.gz");
//        SshUtils.uploadLocalDirToRemote(session,"/opt/udh/ZOOKEEPER1/conf","/Volumes/Samsung_T5/opensource/e-mapreduce/work/ZOOKEEPER1/fl001/conf");
    }




}
