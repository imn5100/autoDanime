package com.shaw.utils;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @version 1.0
 */
public class PropertiesUtil {

	private static Log log = LogFactory.getLog(PropertiesUtil.class);

	/**
	 * 获取messages.properties里的key对应的value值
	 */
	public PropertiesUtil() {
		init();
	}

	static CompositeConfiguration comp_config;

	/**
	 * 这么做能保证config文件只在此类加载的时候读取一遍
	 * 把init方法放开是为了当配置文件修改后，需要主动重新加载配置文件（有文件的情况却不重启web容器）
	 * */
	public static void init() {
		comp_config = new CompositeConfiguration();
		try {
			comp_config.addConfiguration(new PropertiesConfiguration("config.properties"));

		} catch (ConfigurationException e) {
			log.warn("读取配置文件出错！", e);
			e.printStackTrace();
		}
	}

	public static Configuration getConfiguration() {
		if (comp_config == null) {
			init();
		}
		return comp_config;
	}

}
