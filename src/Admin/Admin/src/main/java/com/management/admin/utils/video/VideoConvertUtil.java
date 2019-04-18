/***
 * @pName Admin
 * @name VideoConvertUtil
 * @user HongWei
 * @date 2019/4/3
 * @desc
 */
package com.management.admin.utils.video;

import com.management.admin.exception.InfoException;
import com.skyjilygao.util.VideoConvert;

import java.io.FileNotFoundException;

public class VideoConvertUtil {
    private static VideoConvert videoConvert;
    private final static String source = "C:\\video_convert\\temp.mp4";
    private final static String target = "C:\\video_convert\\temp.m3u8";


    public static void asyncToM3U8(String folder, String fileName, String ext) throws FileNotFoundException, InterruptedException {
//        String ffmpegPath = "D:\\!Soft\\ffmpeg-20181127-1035206-win64-static\\ffmpeg-20181127-1035206-win64-static\\bin\\ffmpeg.exe";
        String ffmpegPath = folder + "\\ffmpeg.exe";//"ffmpeg";
        VideoConvert convert = new VideoConvert(ffmpegPath);
        convert.start(folder + "\\" + fileName + ext, folder + "\\" + fileName + ".m3u8");
        videoConvert = convert;
    }

    public static boolean toM3U8(String folder, String fileName, String ext) throws FileNotFoundException, InterruptedException {
//        String ffmpegPath = "D:\\!Soft\\ffmpeg-20181127-1035206-win64-static\\ffmpeg-20181127-1035206-win64-static\\bin\\ffmpeg.exe";
        String ffmpegPath = folder + "\\ffmpeg.exe";//"ffmpeg";
        VideoConvert convert = new VideoConvert(ffmpegPath);
        convert.start(folder + "\\" + fileName + ext, folder + "\\" + fileName + ".m3u8");
        videoConvert = convert;
        int timeout = 60 * 1000 * 4;
        while (timeout > 0){
            timeout--;
            Thread.sleep(1000);
            if(videoConvert.getStatus().equals("completed")){
                timeout = 60 * 1000 * 4;
                break;
            }else if(!videoConvert.getStatus().equals("completed") && !videoConvert.getStatus().equals("processing")){
                throw new RuntimeException("转换m3u8失败");
            }
        }
        videoConvert.stop();
        return true;
    }

    private static String status(){
        return videoConvert.getStatus();
    }

    private static void stop(){
        videoConvert.stop();
    }


    public static void main(String[] args) {
        try {
            VideoConvertUtil.toM3U8("C:\\video_convert", "temp", ".mp4");
            System.out.println("转换结束");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
