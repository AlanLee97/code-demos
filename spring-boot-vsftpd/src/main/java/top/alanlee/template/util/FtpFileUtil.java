package top.alanlee.template.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FtpFileUtil {

    @Value("${ftp.address}")
    private String host;
    // 端口
    @Value("${ftp.port}")
    private int port;
    // ftp用户名
    @Value("${ftp.username}")
    private String userName;
    // ftp用户密码
    @Value("${ftp.password}")
    private String passWord;

    /**
     * Description: 向FTP服务器上传文件
     * <p>
     * host     FTP服务器ip
     * port     FTP服务器端口
     * username FTP登录账号
     * password FTP登录密码
     * basePath FTP服务器基础目录,/file
     * @param filePath FTP服务器文件存放路径。例如分日期存放：/2018/05/28。文件的路径为basePath+filePath
     *
     * @param filename    上传到FTP服务器上的文件名
     * @param inputStream 输入流
     * @return 成功返回true，否则返回false
     */
    public boolean uploadFile(String filePath, String filename,String basePath, InputStream inputStream) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            // 连接FTP服务器
            ftp.connect(host, port);
            // 登录
            ftp.login(userName, passWord);
            int reply;
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            //切换到上传目录
            boolean changed = ftp.changeWorkingDirectory(filePath + filePath);
            if (!changed) {
                //如果目录不存在创建目录
                String[] dirs = filePath.split("/");
                String tempPath = basePath;
                for (String dir : dirs) {
                    if (null == dir || "".equals(dir)){
                        continue;
                    }
                    tempPath += "/" + dir;
                    if (!ftp.changeWorkingDirectory(tempPath)) {
                        if (!ftp.makeDirectory(tempPath)) {
                            return result;
                        } else {
                            ftp.changeWorkingDirectory(tempPath);
                        }
                    }
                }
            }
            System.out.println("当前目录" + ftp.printWorkingDirectory());
            //设置为被动模式
            // ftp.enterLocalPassiveMode();
            ftp.setControlEncoding("UTF-8");
            //设置上传文件的类型为二进制类型
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            //上传文件
            if (!ftp.storeFile(new String(filename.getBytes("UTF-8"), "ISO-8859-1"), inputStream)) {
                System.out.println("上传文件失败");
                return result;
            }

            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    inputStream.close();
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }
}