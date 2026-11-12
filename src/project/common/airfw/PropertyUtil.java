package project.common.airfw;

import java.io.File;
import java.util.Properties;

import project.common.util.FileUtil;

public class PropertyUtil {
	
	static Properties prop;
	
	private static String serverType;
	
	private static String classPath;
	
	public String getServerType() {
		return serverType;
	}
	public void setServerType(String serverType) {
		this.serverType = serverType;
		classPath = this.getClass().getResource("/").getPath().replace("classes/", "") + "conf";
	}
	
	
	public static String getProperty(String fileName, String propertyName) {
		return getProperty(fileName, propertyName, null);
	}
	public static String getProperty(String fileName, String propertyName, String defaultValue) {
		String value = defaultValue;
		
		FileUtil fileUtil = new FileUtil();
		try{
			
			File file = new File(classPath, serverType + "-" +fileName);
			if(file.isFile()) {
				prop = fileUtil.loadProperties(classPath, serverType + "-" +fileName);
			}else {
				prop = fileUtil.loadProperties(classPath, fileName);
			}
			
		}catch(Exception e1){
			e1.printStackTrace();
		}
		
		value = prop.getProperty(propertyName);
		
		return value;
	}

}
