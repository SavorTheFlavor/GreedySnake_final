package cn.edu.scau.snake;

import java.io.IOException;
import java.util.Properties;

/**
 * 
 * 从配置文件里读取数据的操作
 * 为方便起见，封装成了一个类
 *
 */

public class PropertyManager {
	
	
	private final static PropertyManager pm = //单例设计模式,只加载一次，提高效率
			new PropertyManager();//饿汉式
	
	private PropertyManager(){}//使其不能被实例化
	
	public static PropertyManager getInstance(){
		return pm;
	}
	
	public String getProperties(String key){
		Properties p = new Properties();
		
		try {
			p.load(PropertyManager.class.getClassLoader().getResourceAsStream("someConfigurations.properties"));
			return p.getProperty(key);
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
	}

}
