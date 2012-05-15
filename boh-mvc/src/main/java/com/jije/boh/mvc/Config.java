package com.jije.boh.mvc;

import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Component;

/**
 * 使用spring-jmx将一个pojo变成mbean
 * 1，用@ManagedResourc这样的Annotation注释
 * 2，如果属性是可读可写的，就在getter和setter上都注释@ManagedAttribute，只读的话就只在getter上注释。
 * 3，让Spring JMX工作很简单，在 applicationContext-jmx.xml,加入<context:mbean-export default-domain="bohmvc" registration="replaceExisting" />
 * 
 * 使用Jolokia把JMX的MBean Restful JSON 化
 * 1，将org.jolokia.http.AgentServlet加入web.xml中
 * 2，用法举例：
 * 	a,获取bohmvc下所有MBean的所有属性：/jolokia/read/bohmvc:name=*
 * 	b,获取Config MBean下的所有属性： /jolokia/read/bohmvc:name=config
 * 	c,获取Config MBean 下的mode 属性：/jolokia/read/bohmvc:name=config/mode
 *  d,执行Config MBean 下setMode /jolokia/exec/bohmvc:name=config/setMode/your-value
 *  e,列出bohmvc下所有MBean: /jolokia/list/bohmvc
 *  
 * @author eric.zhang
 *
 */
@Component
@ManagedResource(objectName=Config.MBEAN_NAME, description="bohmvc config management bean")
public class Config {

	public static final String MBEAN_NAME = "bohmvc:name=config";
	
	private String mode = "default";

	@ManagedAttribute(description="show mode")
	public String getMode() {
		return mode;
	}

	@ManagedAttribute(description="update mode")
	public void setMode(String mode) {
		this.mode = mode;
	}
	
	
}
