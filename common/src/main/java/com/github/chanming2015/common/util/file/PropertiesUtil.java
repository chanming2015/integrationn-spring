package com.github.chanming2015.common.util.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.chanming2015.common.util.TaskUtil;

/**
 * Project:common
 * Package:com.github.chanming2015.common.util.config
 * FileName:PropertiesUtil.java
 * Comments:
 * JDK Version:
 * @author XuMaoSen
 * Create Date:2015年12月4日 下午9:16:42
 * Description: 属性文件工具类
 * Version:1.0.0
 */
public class PropertiesUtil {
	private static Map<String,PropertiesHolder> props = new HashMap<String, PropertiesHolder>();
	private static List<PropertiesListener> listeners = new CopyOnWriteArrayList<PropertiesListener>();
	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private static final String utf8 = "utf-8";
	
	/**
	 * 加载项目类路径下的属性文件，每分钟自动监视属性文件的更改
	 */
	public static Properties get(String resource) {
		return get(resource, utf8);
	}
	
	/**
	 * 加载项目类路径下的属性文件，每分钟自动监视属性文件的更改，支持自定义字符集
	 */
	public static Properties get(String resource, String charset) {
		PropertiesHolder prop = props.get(resource);
		if(prop == null || (prop.charset == null ? charset != null : !prop.charset.equalsIgnoreCase(charset))) {
			try {
				URL url = getResource(resource);
				if(url == null) return null;
				
				boolean charsetChange = (prop != null) && (prop.charset == null ? charset != null : !prop.charset.equalsIgnoreCase(charset));
				if(prop == null) {
					prop = new PropertiesHolder();
					prop.charset = charset;
					prop.resource = resource;
				}else if(charsetChange) prop.charset = charset;
				URLConnection connection = url.openConnection();
				prop.lastModified = connection.getLastModified();
				InputStream is = connection.getInputStream();
				if(prop.charset != null) {
					prop.properties.load(new InputStreamReader(is, prop.charset));
				}else {
					prop.properties.load(is);
				}
				is.close();
				extractPropertyValues(prop.properties);
				logger.info("加载属性文件("+resource+")："+url);
			} catch (IOException e) {
				logger.warn("加载属性文件出错："+resource, e);
				return null;
			}
			props.put(resource, prop);
		}
		return prop.properties;
	}
	
	/**
	 * 获取属性文件组合内容，自动监视文件变化
	 */
	public static Properties gets(final String resource, final String ... extraResources) {
		StringBuilder resourcesBuilder = new StringBuilder(resource);
		for(String extraResource:extraResources) {
			resourcesBuilder.append(","+extraResource);
		}
		final String resources = resourcesBuilder.toString();
		
		PropertiesHolder prop = props.get(resources);
		if(prop == null) {
			Properties loads = loads(resource, extraResources);
			prop = new PropertiesHolder();
			prop.resource = resources;
			prop.properties.putAll(loads);
			
			final Properties gets = prop.properties;
			addPropertiesListener(new PropertiesListener() {
				public String getResource() {
					return resources;
				}
				public void afterReloaded(Properties props) {
					Properties loads = loads(resource, extraResources);
					gets.clear();
					gets.putAll(loads);
				}
			});
			props.put(resources, prop);
		}
		return prop.properties;
	}
	
	/**
	 * 重新加载属性文件，用于合并多个配置文件，然后计算变量，extractPropertyValues
	 */
	public static Properties load(String resource, String charset) {
		Properties props = new Properties();
		try {
			URL url = getResource(resource);
			if(url == null) return props;
			
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			if(charset != null) {
				props.load(new InputStreamReader(is, charset));
			}else {
				props.load(is);
			}
			is.close();
			logger.info("读取属性文件("+resource+")："+url);
		} catch (IOException e) {
			logger.warn("读取属性文件出错："+resource, e);
			return null;
		}
		return props;
	}
	
	public static Properties loads(String resource, String ... extraResources) {
		Properties loads = new Properties();
		Properties reload = load(resource, utf8);
		if(reload!=null) loads.putAll(reload);
		for(String extraResource:extraResources) {
			Properties reloadTemp = load(extraResource, utf8);
			if(reloadTemp!=null) loads.putAll(reloadTemp);
		}
		extractPropertyValues(loads);
		return loads;
	}
	
	private static Pattern tagPattern = Pattern.compile("\\{(\\w+)\\}");
	
	/**
	 * 解析配置变量{name}，uploads={upload}/uploads
	 */
	public static void extractPropertyValues(Properties prop) {
		List<String> remains = new ArrayList<String>();
		for(String name : prop.stringPropertyNames()) {
			String value = prop.getProperty(name);
			StringBuilder b = new StringBuilder();
			Matcher matcher = tagPattern.matcher(value);
			int idx = 0;
			while(matcher.find()) {
				int start = matcher.start();
				b.append(value.substring(idx, start));
				idx = matcher.end();
				
				String k = matcher.group(1);
				String v = prop.getProperty(k);
				b.append(v!=null ? v : matcher.group(0));
			}
			if(idx > 0) {
				b.append(value.substring(idx));
				String extractValue = b.toString();
				prop.setProperty(name, extractValue);
				
				matcher = tagPattern.matcher(extractValue);
				if(matcher.find() && !extractValue.equals(value)) remains.add(name);
			}
		}
		if(remains.size()>0) extractPropertyValues(prop);
	}
	
	/**
	 * 资源文件更新监听器
	 */
	public static interface PropertiesListener {
		String getResource();
		void afterReloaded(Properties props);
	}
	public static void addPropertiesListener(PropertiesListener listener) {
		listeners.add(listener);
	}
	private static void notifyListeners(PropertiesHolder props) {
		for(PropertiesListener listener : listeners) {
			if(listener.getResource()==null) continue;
			String[] resources = listener.getResource().split(",");
			for(String resource:resources) {
				if(resource.equals(props.resource)) {
					listener.afterReloaded(props.properties);
					break;
				}
			}
		}
	}
	
	static class PropertiesHolder {
		long lastModified;
		String resource;
		String charset;
		Properties properties = new Properties();
	}
	
	private static URL getResource(String resource) {
		URL url = PropertiesUtil.class.getClassLoader().getResource(resource);
		return url;
	}
	
	static {
		TaskUtil.scheduleAtFixedRate(new Runnable() {//每分钟检查一下时间戳，需要时自动更新属性集
			@Override
			public void run() {
				for(String resource : props.keySet()) {
					String[] resources = resource.split(",");
					for(String res:resources) {
						PropertiesHolder prop = props.get(res);
						if(prop == null) {
							get(res);
							prop = props.get(res);
						}
						try {
							URL url = getResource(res);
							if(url==null) continue;
							URLConnection connection = url.openConnection();
							long lastModified = connection.getLastModified();
							if(lastModified > prop.lastModified) {
								prop.properties.clear();
								InputStream is = connection.getInputStream();
								if(prop.charset != null) {
									prop.properties.load(new InputStreamReader(is, prop.charset));
								}else {
									prop.properties.load(is);
								}
								is.close();
								prop.lastModified = lastModified;
								extractPropertyValues(prop.properties);
								notifyListeners(prop);
								logger.info("更新属性文件("+resource+")："+url);
							}
						} catch (IOException e) {
							logger.warn("更新属性文件出错："+res, e);
						}
					}
				}
			}
		}, 10, 60, TimeUnit.SECONDS);
	}
}
