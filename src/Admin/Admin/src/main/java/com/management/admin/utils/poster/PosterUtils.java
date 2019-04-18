/***
 * @pName Admin
 * @name PosterUtils
 * @user HongWei
 * @date 2019/3/10
 * @desc
 */
package com.management.admin.utils.poster;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.UUID;

public class PosterUtils {

    @Test
    public void test(){
        try {
            /*String result = PosterUtils.createPoster("测试标题",1L, "8888", "https://stack-1251694474.cos.ap-guangzhou.myqcloud.com/upload/62d4574a-0118-4819-9bb8-fc69db076f82.png",
                    "https://stack-1251694474.cos.ap-guangzhou.myqcloud.com/upload/02924d59-c41f-4061-b8d6-1500250b5713.png");*/

            String result = PosterUtils.createPromotionPoster("https://stack-1251694474.cos.ap-guangzhou.myqcloud.com/zhixiao/%E8%87%AA%E5%AE%9A%E4%B9%89%E6%8E%A8%E5%B9%BF%E7%A0%81%E8%83%8C%E6%99%AF.png"
                    ,"https://stack-1251694474.cos.ap-guangzhou.myqcloud.com/zhixiao/1552238406.png","测试最大的标题长度是多少个字");
            System.out.println(result);

           /* String result = PosterUtils.createProductPoster("超长的商品的标题啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊啊", "99","https://stack-1251694474.cos.ap-guangzhou.myqcloud.com/zhixiao/%E5%95%86%E5%93%81%E4%B8%BB%E5%9B%BE.png",
                    "https://stack-1251694474.cos.ap-guangzhou.myqcloud.com/zhixiao/1552238406.png");
            System.out.println(result);
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String createProductPoster(String title, String price, String productImageUrl,  String qrcodeUrl) throws Exception {
        String resourcePath = PosterUtils.class.getResource("/image/").getPath();
        long nowTime = System.currentTimeMillis();
        String qrcodeName = UUID.randomUUID().toString().replace("-", "");
        String petUrlName = UUID.randomUUID().toString().replace("-", "");
        URL petUnitUrl = new URL(productImageUrl);
        HttpURLConnection conn = (HttpURLConnection) petUnitUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        BufferedInputStream biss = new BufferedInputStream(conn.getInputStream());
        OutputStream outputStream = new FileOutputStream(new File(resourcePath + petUrlName + ".png"));
        int lens;
        byte[] arrs = new byte[1024];
        while ((lens = biss.read(arrs)) != -1) {
            outputStream.write(arrs, 0, lens);
            outputStream.flush();
        }
        outputStream.close();
        //二维码图片
        URL getCodeUrl = new URL(qrcodeUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) getCodeUrl.openConnection();
        httpURLConnection.setRequestMethod("GET");// 提交模式
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(5 * 1000);
        BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
        OutputStream ost = new FileOutputStream(new File(resourcePath + qrcodeName + ".png"));
        int len;
        byte[] arr = new byte[1024];
        while ((len = bis.read(arr)) != -1) {
            ost.write(arr, 0, len);
            ost.flush();
        }
        ost.close();
        //合成图片
        if (title.length() > 11) {
            title = StringUtils.substring(title, 0, 11) + "...";
        }
        //宠物图片文件
        File petImg = FileUtils.toFile(PosterUtils.class.getResource("/image/" + petUrlName + ".png"));
        //二维码图片文件 1
        File qrCodeImg = FileUtils.toFile(PosterUtils.class.getResource("/image/" + qrcodeName + ".png"));
        //背景地址
        //为了方便演示放在resources中，可根据实际情况（上传后）将背景模板放入单独的资源文件夹或远程资源服务器
        URL url = PosterUtils.class.getResource("/image/product-promotion.png");
        File fileBg = FileUtils.toFile(url);
        //1、背景图片输入流
        FileInputStream fis = new FileInputStream(fileBg);
        //2、背景图片对象
        Image srcImg = ImageIO.read(fis);
        //3、创建画布，根据背景图片的宽高
        BufferedImage bufferedImage = new BufferedImage(
                //宽度
                srcImg.getWidth(null),
                //高度
                srcImg.getHeight(null),
                //图片类型
                BufferedImage.TYPE_INT_RGB);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        //4、得到2d画笔对象
        Graphics2D g = bufferedImage.createGraphics();
        // 设置对线段的锯齿状边缘处理
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //5、设置画布背景
        g.drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        //6、```````````开始作画```````````
        Font rewardFirstFont = new Font("PingFang SC Bold", Font.PLAIN, 24);
        Font moneyFont = new Font("黑体", Font.PLAIN, 88);
        Font titleFont = new Font("微软雅黑", Font.BOLD, 44);
        //int rewardFirstWidth = Graphics2DUtils.getStringWidth(g, rewardFirstFont, "赏金￥");

        int moneyWidth = Graphics2DUtils.getStringWidth(g, moneyFont, price);
        int titleWidth = Graphics2DUtils.getStringWidth(g, titleFont, title);

        //赏金内容
        //Graphics2DUtils.drawString(g, Color.decode("#ffd434"), rewardFirstFont, "赏金￥", (width - rewardFirstWidth - rewardLastWidth) / 2, 550);
        Graphics2DUtils.drawString(g, Color.decode("#FF0000"), moneyFont, price, 85, height - 275);
        //标题
        Graphics2DUtils.drawString(g, Color.decode("#000000"), titleFont, title, 32, height - 195);
        //把宠物图片和二维码图片划入背景
        g.drawImage(ImageIO.read(petImg), 0, 0, 1080, 1540, null);
        g.drawImage(ImageIO.read(qrCodeImg), width - 278, height - 321, 230, 230, null);
        //   ```````````结束作画```````````
        //7、处理画作
        g.dispose();
        //8、得到输出流
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", os);
        //9、转成base64编码前端可以直接显示，也可转换成其它形式比如文件
        String encodeStr = Base64.getEncoder().encodeToString(os.toByteArray());
        //保存为图片文件
        //FileUtils.writeByteArrayToFile(new File("c:/" + nowTime + ".jpg"), os.toByteArray());
        //10、关闭输入输出流
        fis.close();
        os.close();
        qrCodeImg.delete();
        petImg.delete();
        return encodeStr;

    }

    public static String createPromotionPoster(String backUrl, String qrcodeUrl, String title) throws Exception {
        String resourcePath = PosterUtils.class.getResource("/image/").getPath();
        long nowTime = System.currentTimeMillis();
        String qrcodeName = UUID.randomUUID().toString().replace("-", "");
        String backUrlName = UUID.randomUUID().toString().replace("-", "");
        //二维码图片
        URL getCodeUrl = new URL(qrcodeUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) getCodeUrl.openConnection();
        httpURLConnection.setRequestMethod("GET");// 提交模式
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(5 * 1000);
        BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
        OutputStream ost = new FileOutputStream(new File(resourcePath + qrcodeName + ".png"));
        int len;
        byte[] arr = new byte[1024];
        while ((len = bis.read(arr)) != -1) {
            ost.write(arr, 0, len);
            ost.flush();
        }
        ost.close();
        //背景图片
        getCodeUrl = new URL(backUrl);
        httpURLConnection = (HttpURLConnection) getCodeUrl.openConnection();
        httpURLConnection.setRequestMethod("GET");// 提交模式
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(5 * 1000);
        bis = new BufferedInputStream(httpURLConnection.getInputStream());
        ost = new FileOutputStream(new File(resourcePath + backUrlName + ".png"));
       arr = new byte[1024];
        while ((len = bis.read(arr)) != -1) {
            ost.write(arr, 0, len);
            ost.flush();
        }
        ost.close();

        //二维码图片文件 1
        File qrCodeImg = FileUtils.toFile(PosterUtils.class.getResource("/image/" + qrcodeName + ".png"));
        //背景地址
        //为了方便演示放在resources中，可根据实际情况（上传后）将背景模板放入单独的资源文件夹或远程资源服务器
        File fileBg = FileUtils.toFile(PosterUtils.class.getResource("/image/" + backUrlName + ".png"));
        //1、背景图片输入流
        FileInputStream fis = new FileInputStream(fileBg);
        //2、背景图片对象
        Image srcImg = ImageIO.read(fis);
        //3、创建画布，根据背景图片的宽高
        BufferedImage bufferedImage = new BufferedImage(
                //宽度
                srcImg.getWidth(null),
                //高度
                srcImg.getHeight(null),
                //图片类型
                BufferedImage.TYPE_INT_RGB);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        //4、得到2d画笔对象
        Graphics2D g = bufferedImage.createGraphics();
        // 设置对线段的锯齿状边缘处理
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //5、设置画布背景
        g.drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        //6、```````````开始作画```````````

        if(title.length() > 11){
            title=title.substring(0,11) + "...";
        }
        Font titleFont = new Font("微软雅黑", Font.BOLD, 34);
        int titleWidth = Graphics2DUtils.getStringWidth(g, titleFont, title);
        //标题
        Graphics2DUtils.drawString(g, Color.decode("#000000"), titleFont, title, 713, height - 60);
        //把宠物图片和二维码图片划入背景
        g.drawImage(ImageIO.read(qrCodeImg), width - 365, height - 447, 320, 320, null);

        //   ```````````结束作画```````````
        //7、处理画作
        g.dispose();
        //8、得到输出流
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", os);
        //9、转成base64编码前端可以直接显示，也可转换成其它形式比如文件
        String encodeStr = Base64.getEncoder().encodeToString(os.toByteArray());
        //保存为图片文件
        //FileUtils.writeByteArrayToFile(new File("c:/" + nowTime + ".jpg"), os.toByteArray());
        //10、关闭输入输出流
        fis.close();
        os.close();
        qrCodeImg.delete();
       // petImg.delete();
        return encodeStr;

    }

    /**
     * 原版api, 弃用
     * @param posterTitle
     * @param scene
     * @param moneyReward
     * @param petNameUrl
     * @param qrcodeUrl
     * @return
     * @throws Exception
     */
    public static String createPoster(String posterTitle, Long scene, String moneyReward, String petNameUrl, String qrcodeUrl) throws Exception {
        String resourcePath = PosterUtils.class.getResource("/image/").getPath();
        long nowTime = System.currentTimeMillis();
        String qrcodeName = UUID.randomUUID().toString().replace("-", "");
        String petUrlName = UUID.randomUUID().toString().replace("-", "");
        URL petUnitUrl = new URL(petNameUrl);
        HttpURLConnection conn = (HttpURLConnection) petUnitUrl.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5 * 1000);
        BufferedInputStream biss = new BufferedInputStream(conn.getInputStream());
        OutputStream outputStream = new FileOutputStream(new File(resourcePath + petUrlName + ".png"));
        int lens;
        byte[] arrs = new byte[1024];
        while ((lens = biss.read(arrs)) != -1) {
            outputStream.write(arrs, 0, lens);
            outputStream.flush();
        }
        outputStream.close();
        //二维码图片
        URL getCodeUrl = new URL(qrcodeUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) getCodeUrl.openConnection();
        httpURLConnection.setRequestMethod("GET");// 提交模式
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setConnectTimeout(5 * 1000);
        BufferedInputStream bis = new BufferedInputStream(httpURLConnection.getInputStream());
        OutputStream ost = new FileOutputStream(new File(resourcePath + qrcodeName + ".png"));
        int len;
        byte[] arr = new byte[1024];
        while ((len = bis.read(arr)) != -1) {
            ost.write(arr, 0, len);
            ost.flush();
        }
        ost.close();

        //合成图片
        String title = posterTitle;
        if (title.length() > 21) {
            title = StringUtils.substring(title, 0, 21) + "...";
        }
        //宠物图片文件
        File petImg = FileUtils.toFile(PosterUtils.class.getResource("/image/" + petUrlName + ".png"));
        //二维码图片文件 1
        File qrCodeImg = FileUtils.toFile(PosterUtils.class.getResource("/image/" + qrcodeName + ".png"));
        //背景地址
        //为了方便演示放在resources中，可根据实际情况（上传后）将背景模板放入单独的资源文件夹或远程资源服务器
        URL url = PosterUtils.class.getResource("/image/c5073a05c0d02953c0a357e6f3372b5.png");
        File fileBg = FileUtils.toFile(url);
        //1、背景图片输入流
        FileInputStream fis = new FileInputStream(fileBg);
        //2、背景图片对象
        Image srcImg = ImageIO.read(fis);
        //3、创建画布，根据背景图片的宽高
        BufferedImage bufferedImage = new BufferedImage(
                //宽度
                srcImg.getWidth(null),
                //高度
                srcImg.getHeight(null),
                //图片类型
                BufferedImage.TYPE_INT_RGB);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        //4、得到2d画笔对象
        Graphics2D g = bufferedImage.createGraphics();
        // 设置对线段的锯齿状边缘处理
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        //5、设置画布背景
        g.drawImage(srcImg.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
        //6、```````````开始作画```````````
        Font rewardFirstFont = new Font("PingFang SC Bold", Font.PLAIN, 24);
        Font rewardLastFont = new Font("PingFang SC Bold", Font.PLAIN, 48);
        int rewardFirstWidth = Graphics2DUtils.getStringWidth(g, rewardFirstFont, "赏金￥");
        int rewardLastWidth = Graphics2DUtils.getStringWidth(g, rewardLastFont, moneyReward);
        //赏金内容
        Graphics2DUtils.drawString(g, Color.decode("#ffd434"), rewardFirstFont, "赏金￥", (width - rewardFirstWidth - rewardLastWidth) / 2, 550);
        Graphics2DUtils.drawString(g, Color.decode("#ffd434"), rewardLastFont, moneyReward, (width - rewardLastWidth + rewardFirstWidth) / 2, 550);
        //标题
        Graphics2DUtils.drawString(g, Color.decode("#323232"), new Font("PingFang SC Bold", Font.BOLD, 34), title, 0, 400, width, 12, 5, true);
        //把宠物图片和二维码图片划入背景
        g.drawImage(ImageIO.read(petImg), 130, 90, 362, 250, null);
        g.drawImage(ImageIO.read(qrCodeImg), 100, 700, 175, 175, null);
        //   ```````````结束作画```````````
        //7、处理画作
        g.dispose();
        //8、得到输出流
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", os);
        //9、转成base64编码前端可以直接显示，也可转换成其它形式比如文件
        String encodeStr = Base64.getEncoder().encodeToString(os.toByteArray());
        //保存为图片文件
        FileUtils.writeByteArrayToFile(new File("d:/" + nowTime + ".jpg"), os.toByteArray());
        //10、关闭输入输出流
        fis.close();
        os.close();
        qrCodeImg.delete();
        petImg.delete();
        return encodeStr;

    }
}