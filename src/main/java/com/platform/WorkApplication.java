package com.platform;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import us.codecraft.webmagic.Spider;

@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class WorkApplication implements CommandLineRunner {
	/** 
	 * 启动入口
	 * @author 刘太信 
	 * @date 2017年8月5日 下午12:25:24 
	 * @param args 参数
	 */
	public static void main(String[] args) {
		SpringApplication.run(WorkApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Spider.create(null).runAsync();
	}
}
