package com.platform.webmagic.download.proxy.source;

import java.util.List;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.StringUtils;

import com.platform.util.LogUtil;
import com.platform.util.PropertiesUtil;

import us.codecraft.webmagic.proxy.Proxy;

/**
 * 从配置文件读取代理信息源
 * 
 * @author 刘太信
 * @date 2017年8月18日 下午9:02:07
 */
public class FileHttpProxySource extends FixedHttpProxySource {
	/**
	 * 构造函数
	 * 
	 * @author 刘太信
	 * @date 2017年8月18日 下午8:49:14
	 */
	public FileHttpProxySource() {
		PropertiesConfiguration config = PropertiesUtil.parseFile("proxy.properties");
		if (null == config) {
			LogUtil.error("缺少proxy.properties配置文件");
			throw new RuntimeException("缺少proxy.properties配置文件");
		}
		if (config.containsKey("")) {
			@SuppressWarnings("unchecked")
			List<String> list = (List<String>) config.getProperty("");
			if (null != list && !list.isEmpty()) {
				String[] strItems;
				int port;
				String host, userName, password;
				for (String strProxy : list) {
					strItems = strProxy.split("-");
					if (null != strItems && strItems.length > 0) {
						host = strItems.length >= 1 ? strItems[0] : null;
						port = strItems.length >= 2 ? Integer.parseInt(strItems[1]) : 80;
						userName = strItems.length >= 3 ? strItems[2] : null;
						password = strItems.length >= 4 ? strItems[3] : null;
						if (StringUtils.isNotBlank(host) && port > 0) {
							Proxy proxy = null;
							if (StringUtils.isNotBlank(userName)) {
								proxy = new Proxy(host, port, userName, password);
							} else {
								proxy = new Proxy(host, port);
							}
							this.proxies.add(proxy);
						}
					}
				}
			}
		}
	}
}
