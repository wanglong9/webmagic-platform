package com.platform.webmagic.download.proxy.source;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import us.codecraft.webmagic.proxy.Proxy;

/**
 * 固定配置来源
 * 
 * @author 刘太信
 * @date 2017年8月18日 下午9:02:19
 */
public class FixedHttpProxySource implements HttpProxySource {
	/** 代理列表 */
	protected final List<Proxy> proxies;
	/** 当前代理在立标中的索引位置 */
	protected final AtomicInteger pointer;

	/**
	 * 构造函数
	 * 
	 * @author 刘太信
	 * @date 2017年8月18日 下午9:03:44
	 */
	public FixedHttpProxySource() {
		List<Proxy> proxyList = new ArrayList<>();
		proxyList.add(new Proxy("122.72.32.73", 80));
		proxyList.add(new Proxy("122.72.32.93", 80));
		proxies = proxyList;
		pointer = new AtomicInteger(-1);
	}

	@Override
	public Proxy get() {
		return proxies.get(incrForLoop());
	}

	/**
	 * 循环间隔读取下一个
	 * 
	 * @author 刘太信
	 * @date 2017年8月18日 下午9:03:29
	 * @return
	 */
	protected int incrForLoop() {
		int p = pointer.incrementAndGet();
		int size = proxies.size();
		if (p < size) {
			return p;
		}
		while (!pointer.compareAndSet(p, p % size)) {
			p = pointer.get();
		}
		return p % size;
	}
}
