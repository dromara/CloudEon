package com.data.udh.test;

import com.data.udh.utils.SshUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;
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

    @Test
    public void upload() throws IOException {
        ClientSession session = SshUtils.openConnectionByPassword(
                "10.81.16.19",
                22,
                "root",
                "Ltcyhlwylym@admin2021zi!");

        SftpFileSystem fileSystem;
        try {
            fileSystem = SftpClientFactory.instance().createSftpFileSystem(session);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("打开sftp失败："+e);
        }
//        SshUtils.uploadLocalDirToRemote("/tmp/test/", "/Volumes/Samsung_T5/opensource/e-mapreduce/cloudeon-stack/UDH-1.0.0", fileSystem);
        SshUtils.uploadFile("/tmp/test/", "/Volumes/Samsung_T5/opensource/e-mapreduce/remote-script/check.sh", fileSystem);


    }




}
