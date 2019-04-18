/***
 * @pName Admin
 * @name PatternUtil
 * @user HongWei
 * @date 2019/3/11
 * @desc
 */
package com.management.admin.utils;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {
    //<navigator class="href" url="" @tap="onHref('http://www.baidu.com/')">测试连接</navigator>

    public static String replaceAllNavigation(String s){
        String mode = "(http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&*=]*))";
        Pattern p = Pattern.compile(mode);
        Matcher m = p.matcher(s);
        String s1 = "";
        int i = 0;
        while (m.find()) {
            String url = m.group();
            if(i>0) {
                s1 = s1.replaceAll(url,"<navigator class=\"href\" @tap=\"onHref(\'" + url +"\'" + ")\">" + url + "</navigator>");
                //System.out.println(i);
            }else {
                s1 = s.replaceAll(url,"<navigator class=\"href\" @tap=\"onHref(\'" + url +"\'" + ")\">" + url + "</navigator>");
            }
            i++;
            //System.out.println("src:" + url);
            //  System.out.println(s1);
        }

        System.out.println("最后替换的：" + s1);
        return s1;
    }

    public static String replaceAllFormatHtml(String s){
        String mode = "(http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&*=]*))";
        Pattern p = Pattern.compile(mode);
        Matcher m = p.matcher(s);
        String s1 = "";
        int i = 0;
        while (m.find()) {
            System.out.println("find...");
            String url = m.group();
            if(i>0) {
                s1 = s1.replaceAll(url,"<a href=\'" + url +"\'" + " target=\'_blank\'>" + url + "</a>");
               //System.out.println(i);
            }else {
                s1 = s.replaceAll(url,"<a href=\'" + url +"\'" + " target=\'_blank\'>" + url + "</a>");
            }
            i++;
            //System.out.println("src:" + url);
          //  System.out.println(s1);
        }

        //System.out.println("最后替换的：" + s1);
        return s1;
    }
}
