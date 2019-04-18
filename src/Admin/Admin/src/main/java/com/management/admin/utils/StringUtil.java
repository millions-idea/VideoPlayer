package com.management.admin.utils;

import com.google.common.base.Joiner;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class StringUtil {

	public static final String URL_REG_EXPRESSION = "^(https?://)?([a-zA-Z0-9_-]+\\.[a-zA-Z0-9_-]+)+(/*[A-Za-z0-9/\\-_&:?\\+=//.%]*)*";
	public static final String EMAIL_REG_EXPRESSION = "\\w+(\\.\\w+)*@\\w+(\\.\\w+)+";

	public static boolean isUrl(String s) {
		if (s == null) {
			return false;
		}
		return Pattern.matches(URL_REG_EXPRESSION, s);
	}

	public static boolean isEmail(String s) {
		if (s == null) {
			return true;
		}
		return Pattern.matches(EMAIL_REG_EXPRESSION, s);
	}

	public static boolean isBlank(String s) {
		if (s == null) {
			return true;
		}
		return Pattern.matches("\\s*", s);
	}

	/**
	 * 关联string数组，并为每个key加上单引号
	 * @param spliter
	 * @param arr
	 * @return
	 */
	public static String joinChar(String spliter, String[] arr){
		for (int i = 0; i < arr.length; i++) {
			arr[i] = "'" +  arr[i] + "'";
		}
		String join = String.join(",", arr);
		return join;
	}


	public static String joinChar(String spliter, List<String> arr){
		for (int i = 0; i < arr.size(); i++) {
			String s = arr.get(i);
			s = "'" +  arr.get(i) + "'";
			arr.set(i, s);
		}
		String join = String.join(",", arr);
		return join;
	}





	public static String join(String spliter, Object[] arr) {
		if (arr == null || arr.length == 0) {
			return "";
		}
		if (spliter == null) {
			spliter = "";
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			if (i == arr.length - 1) {
				break;
			}
			if (arr[i] == null) {
				continue;
			}
			builder.append(arr[i].toString());
			builder.append(spliter);
		}
		return builder.toString();
	}

	public static String fromFile(File f) throws IOException {
		InputStream is = new FileInputStream(f);
		byte[] bs = new byte[is.available()];
		is.read(bs);
		is.close();
		return new String(bs);
	}

	public static void toFile(File f, String s) throws IOException {
		FileOutputStream fos = new FileOutputStream(f);
		fos.write(s.getBytes());
		fos.close();
	}

	/**
	 * @作者 尧
	 * @功能 String左对齐
	 */
	public static String padLeft(String src, int len, char ch) {
		int diff = len - src.length();
		if (diff <= 0) {
			return src;
		}

		char[] charr = new char[len];
		System.arraycopy(src.toCharArray(), 0, charr, 0, src.length());
		for (int i = src.length(); i < len; i++) {
			charr[i] = ch;
		}
		return new String(charr);
	}
	/**
	 * @作者 尧
	 * @功能 String右对齐
	 */
	public static String padRight(String src, int len, char ch) {
		int diff = len - src.length();
		if (diff <= 0) {
			return src;
		}

		char[] charr = new char[len];
		System.arraycopy(src.toCharArray(), 0, charr, diff, src.length());
		for (int i = 0; i < diff; i++) {
			charr[i] = ch;
		}
		return new String(charr);
	}

	/**
	 * 通过HashSet特性剔除list中重复的数据  2018年9月1日02:34:28
	 * @param list
	 * @return
	 */
	public static List removeDuplicate(List list) {
		HashSet h = new HashSet(list);
		list.clear();
		list.addAll(h);
		return list;
	}

	/**
	 * map直接转为url参数
	 * @param map
	 * @param isURLEncoder
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String mapToFormData(Map<String, String> map, boolean isURLEncoder) throws UnsupportedEncodingException {
		String formData = "";
		if (map != null && map.size() > 0) {
			formData = Joiner.on("&").withKeyValueSeparator("=").join(map);
			if (isURLEncoder) {
				formData = URLEncoder.encode(formData, "UTF-8");
			}
		}
		return formData;
	}


	/**
	 * 取文本中间
	 * @param text
	 * @param left
	 * @param right
	 * @return
	 */
	public static String getSubString(String text, String left, String right) {
		String result = "";
		int zLen;
		if (left == null || left.isEmpty()) {
			zLen = 0;
		} else {
			zLen = text.indexOf(left);
			if (zLen > -1) {
				zLen += left.length();
			} else {
				zLen = 0;
			}
		}
		int yLen = text.indexOf(right, zLen);
		if (yLen < 0 || right == null || right.isEmpty()) {
			yLen = text.length();
		}
		result = text.substring(zLen, yLen);
		return result;
	}


}
