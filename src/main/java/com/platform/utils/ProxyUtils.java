package com.platform.utils;

import static com.alibaba.fastjson.JSON.parseObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.platform.utils.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.platform.utils.http.HttpData;

import us.codecraft.xsoup.Xsoup;

/**
 * 代理工具类
 *
 * @author 孙星阳
 * @date 2017/9/25 下午12:16
 */
public class ProxyUtils {
    static Logger logger = LoggerFactory.getLogger(ProxyUtils.class);

    private static BlockingQueue<String> ipQueue = new LinkedBlockingQueue<>();
    /**
     * 讯代理接口 每次返回10个代理IP
     *
     * @author 孙星阳
     * @date 2017/9/25 下午4:37
     */
    static final String XUNURL = "http://www.xdaili.cn/ipagent//freeip/getFreeIps";
    /**
     * 虫代理接口 每次返回50个代理IP
     *
     * @author 孙星阳
     * @date 2017/9/25 下午4:37
     */
    // static final String CHONGURL =
    // "http://www.bugng.com/api/getproxy/json?num=50&anonymity=1&type=0";
    /**
     * 66代理 每次返回50个代理IP
     *
     * @author 孙星阳
     * @date 2017/9/25 下午5:16
     */
    static final String SIXURL = "http://www.66ip.cn/nmtq.php?getnum=50&isp=0&anonymoustype=4&start=&ports=&export=&ipaddress=&area=1&proxytype=2&api=66ip";
    /**
     * 西刺代理 每次100个代理IP
     *
     * @author 孙星阳
     * @date 2017/9/25 下午6:07
     */
    static final String XICIURL = "http://www.xicidaili.com/nn/";
    /**
     * 检测接口
     *
     * @author 孙星阳
     * @date 2017/9/25 下午4:37
     */
    static final String CHECKEPROXYURL = "http://www.xdaili.cn/ipagent//checkIp/ipList";

    private static final String KUAIPROXYURL = "http://dev.kuaidaili.com/api/getproxy/?orderid=970649054303695&num=50&area=%E6%B5%99%E6%B1%9F%2C%E6%B1%9F%E8%8B%8F%2C%E4%B8%8A%E6%B5%B7%2C%E5%8D%97%E4%BA%AC&b_pcchrome=1&b_pcie=1&b_pcff=1&protocol=1&method=1&an_an=1&an_ha=1&sp2=1&sep=1";

    private static List<String> getKuaiProxyIP() {
        List<String> resultIP = new ArrayList<>();
        try {
            String result = HttpUtil.get(KUAIPROXYURL);
            logger.debug("快代理获取的代理{}", result);
            if (StringUtils.isEmpty(result)) {
                return resultIP;
            }
            resultIP = Arrays.asList(result.split("\r\n"));
        } catch (Exception e) {
            logger.warn("获取快代理失败:{}", e.getMessage());
        }
        return resultIP;
    }

    /**
     * 获取讯代理IP
     *
     * @param
     * @author 孙星阳
     * @date 2017/9/22 下午4:37
     */
    private static List<String> getXunProxyIP() {
        List<String> resultIP = new ArrayList<>();
        try {
            String result = HttpUtil.get(XUNURL);
            logger.debug("讯代理获取的代理{}", result);
            if (StringUtils.isEmpty(result)) {
                return resultIP;
            }
            JSONArray jsonArray = parseObject(result).getJSONObject("RESULT").getJSONArray("rows");

            for (Object o : jsonArray) {
                resultIP.add(
                        parseObject(o.toString()).getString("ip") + ":" + parseObject(o.toString()).getString("port"));
            }
        } catch (Exception e) {
            logger.warn("获取讯代理失败:{}", e.getMessage());
        }
        return resultIP;
    }

    /**
     * 获取虫代理IP
     *
     * @param
     * @author 孙星阳
     * @date 2017/9/25 下午4:38
     */
    // private static List<String> getChongProxyIP() {
    // List<String> resultIP = new ArrayList<>();
    // try {
    // String result = HttpUtil.get(CHONGURL);
    // logger.debug("虫代理获取的代理{}", result);
    // if (StringUtils.isEmpty(result)) {
    // return resultIP;
    // }
    // JSONArray jsonArray =
    // parseObject(result).getJSONObject("data").getJSONArray("proxy_list");
    // for (Object o : jsonArray) {
    // resultIP.add(o.toString());
    // }
    // } catch (Exception e) {
    // logger.warn("获取虫代理失败:{}", e.getMessage());
    // }
    // return resultIP;
    // }

    /**
     * 获取66代理
     *
     * @param
     * @author 孙星阳
     * @date 2017/9/25 下午5:36
     */
    private static List<String> getSixProxyIP() {
        List<String> resultIP = new ArrayList<>();
        try {
            String result = HttpUtil.get(SIXURL);
            if (StringUtils.isEmpty(result) || !result.contains("<br />")) {
                return resultIP;
            }
            String[] ips = Xsoup.select(result, "body/text()").get().trim().split(" ");
            resultIP = Arrays.asList(ips);
            logger.debug("66代理获取的代理{}", resultIP.toString());
        } catch (Exception e) {
            logger.warn("获取66代理失败:{}", e.getMessage());
        }
        return resultIP;
    }

    /**
     * 获取西刺代理
     *
     * @param
     * @author 孙星阳
     * @date 2017/9/25 下午8:57
     */
    private static List<String> getXiciProxyIP() {
        List<String> resultIP = new ArrayList<>();
        HttpData data = new HttpData();
        data.addHeader("Accept",
                "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        data.addHeader("Accept-Encoding", "gzip, deflate");
        data.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        data.addHeader("Cookie",
                "_free_proxy_session=BAh7B0kiD3Nlc3Npb25faWQGOgZFVEkiJWUzOTAzNTBjNGZjNmUwNzg1MGVmY2M0YjExZDU5OTU4BjsAVEkiEF9jc3JmX3Rva2VuBjsARkkiMTczNHArYk5hN0cyM2s4TWZlbWg1cWZMSjlxSnZQbThqVmk5b3dkNW1NdGM9BjsARg%3D%3D--ce596aca3fae67a80e3fcbd666493bbae1052981; Hm_lvt_0cf76c77469e965d2957f0553e6ecf59=1504233401,1504754549,1506333621; Hm_lpvt_0cf76c77469e965d2957f0553e6ecf59=1506333704");
        data.addHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36");
        try {
            String result = HttpUtil.get(XICIURL, data);
            if (StringUtils.isEmpty(result)) {
                return resultIP;
            }
            Elements elements = Xsoup.select(result, "table[@id='ip_list']/tbody/tr").getElements();
            for (Element element : elements) {
                String ip = Xsoup.select(element, "tr/td[2]/text()").get();
                String port = Xsoup.select(element, "tr/td[3]/text()").get();
                resultIP.add(ip + ":" + port);
            }
            logger.debug("西刺代理获取的代理{}", resultIP.toString());
        } catch (Exception e) {
            logger.warn("获取西刺代理失败:{}", e.getMessage());
        }
        return resultIP;
    }

    /**
     * 检查代理IP
     *
     * @param
     * @author 孙星阳
     * @date 2017/9/22 下午4:38
     */
    public static List<String> checkProxyIPs(List<String> proxList) {
        List<String> resultIP = new ArrayList<>();
        try {
            if (proxList.isEmpty()) {
                return resultIP;
            }
            String result = HttpUtil.get(CHECKEPROXYURL, proxList);
            JSONArray jsonArray = parseObject(result).getJSONArray("RESULT");
            for (Object o : jsonArray) {
                String res = checkProxyIP(o.toString());
                if (null != res) {
                    resultIP.add(res);
                }
            }
        } catch (Exception e) {
            logger.warn("检查代理失败:{}", e.getMessage());
        }
        return resultIP;
    }

    /**
     * 判斷代理IP是否是好的 1、高匿 2、响应时间小于200ms
     *
     * @param proxyIp
     * @author 孙星阳
     * @date 2017/9/25 上午10:16
     */
    public static String checkProxyIP(String proxyIp) {
        if (!proxyIp.contains("time")) {
            return null;
        }
        JSONObject jsonObject = JSONObject.parseObject(proxyIp);
        String anony = jsonObject.getString("anony");
        String time = jsonObject.getString("time");
        Integer times = Integer.parseInt(time.replace("ms", ""));
        if (anony.contains("高匿") && times < 200) {
            return jsonObject.getString("ip") + ":" + jsonObject.getString("port");
        }
        return null;
    }

    /**
     * 获取可用代理IP
     *
     * @param
     * @author 孙星阳
     * @date 2017/9/25 上午11:35
     */
    public static List<String> getProxyIPs() {
        List<String> result = new ArrayList<>();
        // List<String> kuai = getKuaiProxyIP();
        // List<String> result = ProxyUtils.checkProxyIPs(kuai);
        // if (result.size() < 6) {
        // logger.debug("快代理可用代理不足 5 个,开始获取讯代理");
        // List<String> xun = getXunProxyIP();
        // result.addAll(ProxyUtils.checkProxyIPs(xun));
        // if (result.size() < 6) {
        // logger.debug("讯代理可用代理不足 5 个,开始获取虫代理");
        // List<String> chong = getChongProxyIP();
        // result.addAll(ProxyUtils.checkProxyIPs(chong));
        // if (result.size() < 6) {
        // logger.debug("讯代理和虫代理可用代理不足 5 个,开始获取66代理");
        List<String> six = getSixProxyIP();
        result.addAll(ProxyUtils.checkProxyIPs(six));
        if (result.size() < 6) {
            logger.debug("讯代理、虫代理和66代理可用代理不足 5 个,开始获取西刺代理");
            result.addAll(ProxyUtils.checkProxyIPs(getXiciProxyIP()));
        }
        // }
        // }
        // }
        ipQueue.addAll(result);
        logger.info("可用代理有 {} 个,分别是{}", ipQueue.size(), ipQueue);
        return result;
    }

    public static String getActiveIP() {
        while (ipQueue.isEmpty() && ipQueue.size() < 5) {
            synchronized (ProxyUtils.class) {
                if (ipQueue.isEmpty() && ipQueue.size() < 5) {
                    logger.info("获取代理IP开始");
                    getProxyIPs();
                    logger.info("获取代理IP结束");
                }
            }
        }
        return ipQueue.poll();
    }

    /**
     * 获取本地外网IP地址
     *
     * @throws SocketException
     */
    public static String getV4IP() {
        String ip = "";
        String chinaz = "http://ip.chinaz.com";

        StringBuilder inputLine = new StringBuilder();
        String read = "";
        URL url = null;
        HttpURLConnection urlConnection = null;
        BufferedReader in = null;
        try {
            url = new URL(chinaz);
            urlConnection = (HttpURLConnection) url.openConnection();
            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
            while ((read = in.readLine()) != null) {
                inputLine.append(read + "\r\n");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
        Matcher m = p.matcher(inputLine.toString());
        if (m.find()) {
            String ipstr = m.group(1);
            ip = ipstr;
        }
        return ip;
    }

}
