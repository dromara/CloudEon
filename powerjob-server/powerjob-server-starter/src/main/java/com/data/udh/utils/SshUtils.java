package com.data.udh.utils;

import cn.hutool.core.io.FileUtil;
import com.data.udh.dto.CheckHostInfo;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelExec;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.sftp.client.SftpClientFactory;
import org.apache.sshd.sftp.client.fs.SftpFileSystem;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class SshUtils {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(SshUtils.class);


    /**
     * 打开远程会话
     */
    public static ClientSession openConnection(String sshHost, Integer sshPort, String sshUser, String privateKey) {

        SshClient sshClient = SshClient.setUpDefaultClient();
        sshClient.start();
        ClientSession session = null;
        try {
            session = sshClient.connect(sshUser, sshHost, sshPort).verify().getClientSession();
            session.addPublicKeyIdentity(getKeyPairFromString(privateKey));
            if (session.auth().verify().isFailure()) {
                LOG.info("验证失败");
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOG.info(sshHost + " 连接成功");
        return session;
    }

    /**
     * 打开远程会话
     */
    public static ClientSession openConnectionByPassword(String sshHost, Integer sshPort, String sshUser, String password) {

        SshClient sshClient = SshClient.setUpDefaultClient();
        sshClient.start();
        ClientSession session = null;
        try {
            session = sshClient.connect(sshUser, sshHost, sshPort).verify().getClientSession();
            session.addPasswordIdentity(password);
            if (session.auth().verify().isFailure()) {
                LOG.info("验证失败");
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        LOG.info(sshHost + " 连接成功");
        return session;
    }


    /**
     * 关闭远程会话
     */
    public static void closeConnection(ClientSession session) {
        try {
            session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取密钥对
     */
    static KeyPair getKeyPairFromString(String pk) {
        final KeyPairGenerator rsa;
        try {
            rsa = KeyPairGenerator.getInstance("RSA");
            final KeyPair keyPair = rsa.generateKeyPair();
            final ByteArrayOutputStream stream = new ByteArrayOutputStream();
            stream.write(pk.getBytes());
            final ObjectOutputStream o = new ObjectOutputStream(stream);
            o.writeObject(keyPair);
            return keyPair;
        } catch (NoSuchAlgorithmException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 同步执行,需要获取执行完的结果
     *
     * @param session 连接
     * @param command 命令
     * @return 结果
     */
    public static String execCmdWithResult(ClientSession session, String command) throws IOException {
        session.resetAuthTimeout();
        LOG.info("exe cmd: {}", command);
        // 命令返回的结果
        ChannelExec ce = null;
        // 返回结果流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // 错误信息
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        ce = session.createExecChannel(command);
        ce.setOut(out);
        ce.setErr(err);
        // 执行并等待
        ce.open();
        Set<ClientChannelEvent> events =
                ce.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), TimeUnit.SECONDS.toMillis(100000));
        //  检查请求是否超时
        if (events.contains(ClientChannelEvent.TIMEOUT)) {
            throw new RuntimeException("ssh 连接超时");
        }
        int exitStatus = ce.getExitStatus();
        LOG.info("mina result {}", exitStatus);
        if (exitStatus != 0) {
            throw new RuntimeException("ssh 执行命令失败：" + err.toString());
        }
        LOG.info("exe cmd return : {}", out);
        return out.toString().trim();
    }

    /**
     * 上传单个文件到指定目录,相同路径ui覆盖
     *
     * @param session       连接
     * @param remoteDirPath 远程目录地址
     * @param inputFile     文件 File
     */
    public static boolean uploadFile(ClientSession session, String remoteDirPath, String inputFile,SftpFileSystem sftp) {
        File uploadFile = new File(inputFile);
        InputStream input = null;
        try {
            Path path = sftp.getDefaultDir().resolve(remoteDirPath);
            if (!Files.exists(path)) {
                LOG.info("create pathHome {} ", path);
                Files.createDirectories(path);
            }
            input = Files.newInputStream(uploadFile.toPath());
            Path file = path.resolve(uploadFile.getName());
            if (Files.exists(file)) {
                LOG.info("delete remote file  {}", file);
                Files.deleteIfExists(file);
            }
            Files.copy(input, file);
            LOG.info("local file {} copy to remote success " ,uploadFile.toPath());
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 将本地目录里所有文件拷贝到远程目录中
     * @param session
     * @param remoteDirPath
     * @param localDir
     */
    public static void uploadLocalDirToRemote(ClientSession session, String remoteDirPath, String localDir) {
        try {
            SftpFileSystem sftp = SftpClientFactory.instance().createSftpFileSystem(session);
            for (String file : new File(localDir).list()) {
                String localFilePath = localDir + File.separator + file;
                uploadFile(session, remoteDirPath, localFilePath,sftp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建目录
     *
     * @param path
     * @return
     */
    public static boolean createDir(ClientSession session, String path) {
        SftpFileSystem sftp = null;
        try {
            sftp = SftpClientFactory.instance().createSftpFileSystem(session);
            Path remoteRoot = sftp.getDefaultDir().resolve(path);
            if (!Files.exists(remoteRoot)) {
                Files.createDirectories(remoteRoot);
                return true;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        ClientSession session = SshUtils.openConnection("localhost", 22, "huzekang",
                "/Users/huzekang/.ssh/id_rsa");
        String ls = SshUtils.execCmdWithResult(session, "java -version");
        System.out.println(ls);
//        boolean dir = MinaUtils.createDir(session,"/home/shinow/test/");
//        System.out.println(dir);
//        boolean uploadFile = MinaUtils.uploadFile(session, "/Users/liuxin/opt/test", "/Users/liuxin/Downloads/yarn-default.xml");
//        System.out.println(uploadFile);
    }

}
