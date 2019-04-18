package com.management.admin.apiController;

import com.alibaba.fastjson.JSONReader;
import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.google.common.base.Joiner;
import com.management.admin.entity.param.UploadParam;
import com.management.admin.entity.template.Constant;
import com.management.admin.entity.template.JsonResult;
import com.management.admin.exception.InfoException;
import com.management.admin.utils.FileUtil;
import com.management.admin.utils.JsonUtil;
import com.management.admin.utils.RequestUtil;
import com.management.admin.utils.ResponseUtil;
import com.management.admin.utils.http.CosUtil;
import com.management.admin.utils.http.TencentCosUtil;
import com.management.admin.utils.video.RemoteCommandUtil;
import com.management.admin.utils.video.VideoDFSUtil;
import com.qcloud.cos.transfer.Upload;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;


@RestController
@RequestMapping(value = "/api/file")
public class FileApiController {

    private static final String PREFIX = "file";

    @Autowired
    private FastFileStorageClient storageClient;

    /**
     * 实现文件上传
     */
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    @ResponseBody

    public void fileUpload(MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
        //文件不存在
        if (file == null || file.isEmpty()) {
            throw new InfoException("文件不存在");
        }
        //后缀名检测
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());

        //判断后缀名
        String[] fileTypes = Constant.FILE_TYPES;
        boolean flag = false;
        for (String fileType : fileTypes) {
            if (fileType.equals(extension)){
                flag = true;
                break;
            }
        }

        if (!flag){
            //不能上传
            throw new InfoException("只能上传" + Joiner.on(",").join(Constant.FILE_TYPES) + "格式的图片");
        }

        try {
            //文件类型检测
            String mimeType = FileUtil.getFileMimeType(file.getBytes());

            flag = false;
            for (String mt : Constant.MIME_TYPES) {
                if (mt.equals(mimeType)){
                    flag = true;
                    break;
                }
            }
            System.out.println(mimeType);

            if (!flag){
                //不能上传
                throw new InfoException("只能上传" + Joiner.on(",").join(Constant.MIME_TYPES) + "类型的图片");
            }

            //可以上传
            //动态生成图片名称 名称=日期毫秒+3位随机数字
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String format = dateFormat.format(new Date());


            //生成随机数
            Random random = new Random();
            for (int i = 0; i < 6; i++) {
                format += random.nextInt(10);
            }

            //获取跟目录
            File abspath = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!abspath.exists())
                abspath = new File("");

            //如果上传目录为/static/images/upload/，则可以如下获取：
            File upload = new File(abspath.getAbsolutePath(), "/static/images/upload/");
            if (!upload.exists())
                upload.mkdirs();

            //保存的路径
            String path = upload.getAbsoluteFile().getAbsolutePath();

            //最后的文件名
            String finalFileName = format + "." + extension;

            File dest = new File(path + "/" + finalFileName);
            if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
                dest.getParentFile().mkdir();
            }


            //保存文件
            file.transferTo(dest);

            //前台img访问的地址  直接使用 img:src 显示
            String url = finalFileName;

            //返回保存文件的地址
            ResponseUtil.renderJson(response, new JsonResult().successful(url));

        } catch (IllegalStateException e) {
            ResponseUtil.renderJson(response, JsonResult.failing());
        } catch (Exception e) {
            ResponseUtil.renderJson(response, JsonResult.failing());
        }
    }

    @RequestMapping(value = "cosUpload", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult cosUpload(MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url=TencentCosUtil.upload(file);
        return new JsonResult().successful(url);
    }

    @RequestMapping(value = "cosUploadBase64", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult cosUploadBase64(String file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url=TencentCosUtil.upload(file);
        return new JsonResult().successful(url);
    }

    @PostMapping("/cosUploadMulti")
    @ResponseBody
    public JsonResult cosUploadMulti(HttpServletRequest request) {
        String postBody = RequestUtil.getPostBody(request);
        UploadParam uploadParam = JsonUtil.getModel(postBody, UploadParam.class);
        List<String> urls = new ArrayList<>();
        uploadParam.getUploadFileList().forEach(data -> {
            String url= null;
            try {
                data = data.replace("data:image/png;base64,", "");
                url = TencentCosUtil.upload(data);
                urls.add(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return new JsonResult().successful(urls);
    }


    @RequestMapping(value = "videoUpload", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult videoUpload(MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(file == null) throw new InfoException("请选择文件");
        MultipartFile convertFile = VideoDFSUtil.uploadToServer(file);
        /*List<MultipartFile> tsList = VideoDFSUtil.getTsList();*/
        if(convertFile != null){

           /* List<String> pathList = new ArrayList<>();

            tsList.forEach(item -> {
                StorePath storePath = null;
                try {
                    storePath = storageClient.uploadFile(item.getInputStream(), item.getSize(),
                            FilenameUtils.getExtension(item.getOriginalFilename()), null);
                    System.out.println(storePath);
                    pathList.add(Constant.DFSUrl + storePath.getPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });*/

            StorePath storePath = storageClient.uploadFile(convertFile.getInputStream(), convertFile.getSize(),
                    FilenameUtils.getExtension(convertFile.getOriginalFilename()), null);
            String path = storePath.getPath();

            //远程分片、生成缩略图

            String fileName = file.getOriginalFilename();
            String extendsName = "." + fileName.substring(fileName.lastIndexOf(".") + 1);

            String name = path.replaceAll("M00/00/00/", "");
            String prefix = " cd /usr/local/nginx/html/fastdfs/group0/M00/00/00/&&";
            String thum = "sudo ffmpeg -i " + name + " -y -f mjpeg -ss 3 -t 0.001 -aspect 16:9 " +  name.replaceAll(extendsName, "")  + ".jpg && ";
            thum = prefix + thum;
            String cmd = thum + "sudo ffmpeg -i " + name + " -c:v libx264 -hls_time 60 -hls_list_size 0 -c:a aac -strict -2 -f hls " + name.replaceAll(extendsName, "") + ".m3u8";
            RemoteCommandUtil.authExecute(cmd);

            return new JsonResult().successful(Constant.DFSUrl + "M00/00/00/" + name.replaceAll(extendsName, "") + ".m3u8");
        }
        return JsonResult.failing();
    }


}
