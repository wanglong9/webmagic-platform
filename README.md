# webmagic-platform
基于webmagic的分布式爬虫平台，实现通过参数化配置启动爬虫任务。

## 涉及到的技术
 webmagic、 spring-cloud、 elastic-job 、 jdk8 、 docker 

## 组件介绍 

### 1、processor 

#### 中间页面

  >  说明：追加页面url，追加中间数据。
  >  例如58租房，城区页面、商圈页面、房屋列表页面，均视为中间页面 .
  
#### 目标页面 

  >  说明：解析页面数据，例如58租房，租房详情列表页面 .
  
### 2、downloader

#### 普通HttpDownloader

> 说明：普通http请求获取页面.
> 例如：现有的HttpClientDownloader.

#### 渲染页面Downloader

> 说明：需要js渲染的网页页面.
> 例如 ：通过Chrome headless 特性(CdpSeleniumDownloader),
> 的webdriver+Selenium实现的(PhantomjsSeleniumDownloader)

### 3、 pipeline

#### 关系型数据库输出

#### 非关系型数据库输出

#### 消息中间件输出

### 4、Scheduler

#### 队列型调度器

#### 集合型调度器
