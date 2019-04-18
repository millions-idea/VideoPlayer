/***
 * @pName Admin
 * @name VideoDFSUtil
 * @user HongWei
 * @date 2019/4/3
 * @desc
 */
package com.management.admin.utils.video;

import com.management.admin.utils.FileUtil;
import org.apache.commons.io.FileUtils;
import org.apache.http.entity.ContentType;
import org.apache.tools.ant.DirectoryScanner;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class VideoDFSUtil {

    public static MultipartFile uploadToServer(MultipartFile uploadFile) throws FileNotFoundException, InterruptedException {
        //获取文件扩展名
        String fileName = uploadFile.getOriginalFilename();
        String extendsName = "." + fileName.substring(fileName.lastIndexOf(".") + 1);

        FileInputStream fileInputStream = null;
        try {
            String saveFileName = UUID.randomUUID().toString();
            FileUtil.saveToLocal(uploadFile, "C:\\video_convert\\" + saveFileName + extendsName);
            /*
            //拆分视频大文件,分段播放提升速度
            boolean result = VideoConvertUtil.toM3U8("C:\\video_convert", "temp", ".mp4");
            if(result){
                File file = new File("C:\\video_convert\\temp.mp4");
                FileUtils.deleteQuietly(file);
                //读取转换后的新文件
                file = new File("C:\\video_convert\\temp.m3u8");
                fileInputStream = new FileInputStream(file);
                MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                        ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
                fileInputStream.close();
                return multipartFile;
            }*/
            File file = new File("C:\\video_convert\\" + saveFileName + extendsName);
            fileInputStream = new FileInputStream(file);
            MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                    ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
            fileInputStream.close();
            FileUtils.deleteQuietly(file);
            return multipartFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<MultipartFile> getTsList() {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setIncludes(new String[]{"**/*.ts"});
        scanner.setBasedir("C:\\video_convert\\");
        scanner.setCaseSensitive(false);
        scanner.scan();
        String[] files = scanner.getIncludedFiles();

        List<MultipartFile> list  =new ArrayList<>();

        Arrays.stream(files).forEach(item -> {
            System.out.println(item);
            File file = new File("C:\\video_convert\\" + item);
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
                MultipartFile multipartFile = new MockMultipartFile(file.getName(), file.getName(),
                        ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
                fileInputStream.close();
                list.add(multipartFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });

        return list;
    }

    public static void main(String[] args) {
        List<MultipartFile> tsList = VideoDFSUtil.getTsList();
        System.out.println(tsList);
    }
}
