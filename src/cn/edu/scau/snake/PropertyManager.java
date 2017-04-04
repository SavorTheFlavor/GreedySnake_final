package cn.edu.scau.snake;

import java.io.IOException;
import java.util.Properties;

/**
 * 
 * �������ļ����ȡ���ݵĲ���
 * Ϊ�����������װ����һ����
 *
 */

public class PropertyManager {
	
	
	private final static PropertyManager pm = //�������ģʽ,ֻ����һ�Σ����Ч��
			new PropertyManager();//����ʽ
	
	private PropertyManager(){}//ʹ�䲻�ܱ�ʵ����
	
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
