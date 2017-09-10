package com.example.demo.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PreDestroy;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.*;

@Slf4j
public class UploadFtp implements CommandLineRunner {


    @Value("${watch_dir}")
    private String watchDir;

    @Value("${host:http://hz01-sw-gacc04.hz01:8820/cms_movie}")
    private String apiHost;

    @Value("${ftp_request_host:http://hz01-sw-gacc04.hz01:8421/ftp}")
    private String ftpRequestHost;

    @Value("${ftp.instance:4}")
    private int ftpClientInstanceCount;

    @Value("${ftp.speed_limit:4096}")
    private int ftpSpeedLimit;

    @Value("${task.interval:10}")
    private int taskIntervalInS;


    private ScheduledExecutorService executor;

    private ExecutorService ftpClients;

    private ConcurrentHashMap<Long, Integer> currentDoingMovies = new ConcurrentHashMap<>();

    @Override
    public void run(String... args) throws Exception {

        executor = Executors.newScheduledThreadPool(100);
        ftpClients = Executors.newFixedThreadPool(ftpClientInstanceCount);
    }

    private void tryToUpload(final MovieFtpStatus status, final File file) {
        if (currentDoingMovies.containsKey(status.getMovieId())) {
            log.info("movie already in process, will ignore");
            return;
        }

        if (currentDoingMovies.size() >= ftpClientInstanceCount) {
            log.info("ftp client current {}, will ignore", currentDoingMovies);
            return;
        }

        ftpClients.submit(new Runnable() {
            @Override
            public void run() {
                FTPClient ftp = null;
                try {
                    currentDoingMovies.put(status.getMovieId(), 1);

                    // first connect to ftp server
                    ftp = new FTPClient();
                    ftp.connect(status.getFtpHost(), status.getFtpPort());
                    int reply = ftp.getReplyCode();
                    if (!FTPReply.isPositiveCompletion(reply)) {
                        log.error("connect to ftp failed: reply_code={}", reply);
                        return;
                    }

                    if (!ftp.login(status.getUsername(), status.getPassword())) {
                        log.error("login failed: error_msg={}", ftp.getReplyString());
                        return;
                    }

                    ftp.enterLocalPassiveMode();
                    if (!ftp.setFileType(FTP.BINARY_FILE_TYPE)) {
                        log.error("set file type failed: error_msg={}", ftp.getReplyString());
                        return;
                    }

                    ftp.setBufferSize(1024 * 1024);

                    long fileSize = 0;
                    FTPFile[] files = ftp.listFiles(status.getMovieId() + ".mp4");
                    if (files.length == 1 && files[0].isFile()) {
                        fileSize = files[0].getSize();
                    }

                    if (file.length() == fileSize) {
                        log.info("in second check, file upload complete, total_size={}", fileSize);
                        status.setStatus(CmsMovieStatus.CMS_MOVIE_UPLOADED);
                        //reportMovieStatus(status);
                        return;
                    } else if (file.length() < fileSize) {
                        ftp.deleteFile(status.getMovieId() + ".mp4");
                        fileSize = 0;
                        log.error("ftp dir file size > local file size: " +
                                "ftp_file_size={}, local_file_size={}", fileSize, file.length());
                    }

                    InputStream inputStream = FileUtils.openInputStream(file);
                    inputStream.skip(fileSize);
                    ftp.appendFile(status.getMovieId() + ".mp4", inputStream);
                    inputStream.close();

                } catch (Throwable t) {
                    log.error("error in try upload", t);
                } finally {
                    currentDoingMovies.remove(status.getMovieId());
                    if (ftp != null && ftp.isConnected()) {
                        try {
                            ftp.logout();
                            ftp.disconnect();
                        } catch (Exception e) {
                            log.error("error in disconnect", e);
                        }
                    }
                }
            }
        });

    }

    @PreDestroy
    private void destroy() {
        executor.shutdown();
        try {
            executor.awaitTermination(5000, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            log.error("shutdown executor failed", e);
        }

        ftpClients.shutdown();
        try {
            ftpClients.awaitTermination(5000, TimeUnit.MICROSECONDS);
        } catch (InterruptedException e) {
            log.error("shutdown ftpClients failed", e);
        }
    }

}


@Data
class MovieFtpStatus {

    private long movieId;

    private String ftpHost;

    private int ftpPort;

    private String username;

    private String password;

    private CmsMovieStatus status;

}

@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.STRING)
enum CmsMovieStatus implements CodeEnum {
    CMS_MOVIE_UNKNOWN(404),
    // 品牌商还在编辑movie信息的时候
    CMS_MOVIE_UPLOAD_WAITING_DETAIL(-2),
    //    CMS_MOVIE_REINIT(-1),
    CMS_MOVIE_INIT(0),

    CMS_MOVIE_ONLINE_ENABLED(1),
    CMS_MOVIE_ONLINE_DISABLED(2),
    CMS_MOVIE_DUSTED(3),

    // 正在上传合作商的视频到服务器
    CMS_MOVIE_UPLOADING(31),
    // 上传成功，会有ct脚本定期查看该状态下的movie，然后负责把movie移到local并split，若出错则修改为出错状态
    CMS_MOVIE_UPLOADED(32),

    CMS_MOVIE_WAIT_TO_DISPATCH_DOWNLOAD(35),
    CMS_MOVIE_WAIT_TO_DOWNLOAD(36),
    CMS_MOVIE_DOWNLOADING(37),
    // 暂不启用
    CMS_MOVIE_DOWNLOADED(38),

    // TODO：后续有需求，要进行重新分片，那么会有该状态进行过渡
    CMS_MOVIE_WAIT_TO_SPLIT(40),
    //  表示正在ffmpeg分片
    CMS_MOVIE_SPLITTING(41),
    //  分片完成，等待验证verify
    CMS_MOVIE_WAIT_TO_VERIFY(45),
    CMS_MOVIE_VERIFY_OK(46),

    // cms页面上点击“预发布”，会改为该状态，cdn挂载的机器上会有ct脚本，负责对这个状态的movie进行push cdn
    CMS_MOVIE_PREPUBLISH(50),
    // 正在上传cdn，该状态由脚本更改，因为不同的movie分布在不同机器上，无法由cms-server来进行，所以cms-server只负责把状态改为CMS_MOVIE_PREPUBLISH状态
    CMS_MOVIE_PUSHING_CDN(51),

    CMS_MOVIE_WAIT_TO_ONLINE(61),
    // 同步到新内容平台
    CMS_MOVIE_SYNC_TO_NEW_PLATFORM(62),

    CMS_MOVIE_DOWNLOAD_FAIL(100),
    CMS_MOVIE_SPLIT_FAIL(101),
    CMS_MOVIE_PUSH_CDN_FAIL(102),
    CMS_MOVIE_VERIFY_FAIL(103),
    // ftp上传失败，或者ct脚本没找到状态为CMS_MOVIE_UPLOADED的movie的时候，都会改为此状态
    CMS_MOVIE_UPLOAD_FAIL(104);

    @Getter
    public final int code;
}