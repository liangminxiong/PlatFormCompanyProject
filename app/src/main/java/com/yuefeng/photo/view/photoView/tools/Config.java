package com.yuefeng.photo.view.photoView.tools;

/**
 * 
 * @author join
 * 
 */
public class Config {

//	public static int limit;
	static String savePathString;

	static {
//		limit = 5;
		savePathString = "/temp";
	}

//	public static void setLimit(int limit) {
//		Config.limit = limit;
//	}
//
//	public static int getLimit() {
//		return limit;
//	}

	public static void setSavePath(String path) {
		Config.savePathString = path;
	}

	public static String getSavePath() {
		return savePathString;
	}
}
