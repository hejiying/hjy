package com.yc.net.bank;

import java.io.IOException;
import java.util.Properties;

/**
 * 设计为单例模式
 * @author BD-DELL
 *
 */
public class MyProperties extends Properties{
	
	private static MyProperties instance = null;
	private MyProperties(){
		try {
			this.load(MyProperties.class.getClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static MyProperties getInstance(){
		if(null == instance){
			instance = new MyProperties();
		}
		return instance;
	}
	
	
}
