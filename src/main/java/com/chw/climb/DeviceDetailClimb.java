package com.chw.climb;

import com.alibaba.fastjson.JSONObject;
import com.chw.constant.DeviceConstant;
import com.chw.utils.JsoupUtils;
import com.chw.utils.StringUtils;
import com.chw.utils.http.HttpClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class DeviceDetailClimb {

    private static final String charPattern = "[：＞+>]";

    private static final Pattern romPattern = Pattern.compile("[~\\d]*([\\d]+[GBgbMm]+)[~\\d]*");

    private static final Pattern percentPattern = Pattern.compile("[~\\d]*([\\d\\.]+)[~\\d]*");

    private static final Pattern numPattern = Pattern.compile("[~\\d]*([\\d]+)[~\\d]*");

    private static Map<String, String> keyMap = new HashMap<>();

    private static Map<String, String> targetKeyMap = new HashMap<>();

    static {
        keyMap.put("CPU", "cpu");
        keyMap.put("后置", "frontPix");
        keyMap.put("前置", "backPix");
        keyMap.put("内存", "ram");
        keyMap.put("电池", "battery");
        keyMap.put("屏幕", "screen");
        keyMap.put("分辨率", "resRatio");
        keyMap.put("CPU主频", "cpu");
        keyMap.put("内存容量", "ram");
        keyMap.put("笔记本重量", "weight");


        for (Map.Entry<String, String> entry : keyMap.entrySet()) {
            targetKeyMap.put(entry.getKey(), entry.getValue());
            targetKeyMap.put(entry.getKey() + "_best", entry.getValue() + "Best");
            targetKeyMap.put(entry.getKey() + "_desc", entry.getValue() + "Desc");
            targetKeyMap.put(entry.getKey() + "_level", entry.getValue() + "Level");
        }
    }

    private static Map<String, String> paramHeaderMap = new HashMap<>();
    private static Map<String, String> basicParamKeyMap = new HashMap<>();
    private static String priceKey = "price";
    private static String romKey = "rom";
    private static String ramKey = "ram";
    private static String nvidiaRomKey = "nvidiaRom";

    static {
        paramHeaderMap.put("基本参数", "basicParam");
        paramHeaderMap.put("外形", "appearance");
        paramHeaderMap.put("硬件", "hardware");
        paramHeaderMap.put("屏幕", "screen");
        paramHeaderMap.put("摄像头", "camera");
        paramHeaderMap.put("网络与连接", "network");
        paramHeaderMap.put("电池与续航", "battery");
        paramHeaderMap.put("功能与服务", "service");
        paramHeaderMap.put("保修信息", "protectedInfo");
        paramHeaderMap.put("I/O接口", "ioInterface");
        paramHeaderMap.put("外观", "appearance");
        paramHeaderMap.put("多媒体设备", "mediaDevice");
        paramHeaderMap.put("基本参数", "basicParam");
        paramHeaderMap.put("摄像头", "camera");
        paramHeaderMap.put("显卡", "nvidia");
        paramHeaderMap.put("电源描述", "battery");
        paramHeaderMap.put("笔记本附件", "appendix");
        paramHeaderMap.put("处理器", "cpu");
        paramHeaderMap.put("存储设备", "storage");
        paramHeaderMap.put("其他", "other");
        paramHeaderMap.put("保修信息", "protectedInfo");
        paramHeaderMap.put("显示屏", "screen");
        paramHeaderMap.put("输入设备", "other");
        paramHeaderMap.put("网络通信", "network");

        basicParamKeyMap.put("国内发布时间", "publishTime");
        basicParamKeyMap.put("电商报价", priceKey);
        basicParamKeyMap.put("上市日期", "marketTime");
        basicParamKeyMap.put("手机类型", "phoneType");
        basicParamKeyMap.put("摄像头总数", "cameraNum");
        basicParamKeyMap.put("出厂系统内核", "systemKernel");
        basicParamKeyMap.put("ROM容量", romKey);
        basicParamKeyMap.put("RAM容量", ramKey);
        basicParamKeyMap.put("CPU频率", "cpuFreq");
        basicParamKeyMap.put("CPU核心数", "cpuCoreNum");
        basicParamKeyMap.put("质保时间", "procetedTime");
        basicParamKeyMap.put("硬盘容量", "diskCapacity");
        basicParamKeyMap.put("CPU系列", "cpuSerial");
        basicParamKeyMap.put("CPU主频", "cpuFreq");
        basicParamKeyMap.put("CPU型号", "cpuType");
        basicParamKeyMap.put("核心/线程数", "coreSize");
        basicParamKeyMap.put("三级缓存", "threeCache");
        basicParamKeyMap.put("硬盘描述", "diskDesc");
        basicParamKeyMap.put("内存类型", "romType");
        basicParamKeyMap.put("内存容量", romKey);
        basicParamKeyMap.put("最大内存容量", "largestRom");
        basicParamKeyMap.put("笔记本重量", "weight");
        basicParamKeyMap.put("产品定位", "productLoc");
        basicParamKeyMap.put("操作系统", "osSystem");
        basicParamKeyMap.put("产品型号", "productType");
        basicParamKeyMap.put("显卡类型", "nvidiaType");
        basicParamKeyMap.put("显卡芯片", "nvidiaCore");
        basicParamKeyMap.put("显存类型", "nvidiaRomType");
        basicParamKeyMap.put("显存容量", nvidiaRomKey);

    }

    public static Map<String, String> commentKeyMap = new HashMap<>();

    static {
        commentKeyMap.put("续航", "endurance");
        commentKeyMap.put("性价比", "costPerf");
        commentKeyMap.put("拍照", "photo");
        commentKeyMap.put("性能", "performance");
    }

    public Object[] getDeviceDetail(String subUrlComment) throws IOException {
        String subUrlParam = subUrlComment.replace("review", "param");
        Object[] objects = getDeviceParam(subUrlParam);
        return objects;
    }


    public Map<String, String> getDevicePerformanceDetail(String subUrlComment) throws IOException {
        return getDevicePerformance(subUrlComment);
    }

    private String clearChar(String str) {
        return str.replaceAll(charPattern, "").trim();
    }

    private String matcher(String str, Pattern pattern) {
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(0).trim();
        }
        return "";
    }

    private String getPercent(String str) {
        str = matcher(str, percentPattern);
        return StringUtils.isEmpty(str) ? "0" : str;
    }

    private String getRom(String str) {
        return matcher(str, romPattern);
    }

    private String getNum(String str) {
        str = matcher(str, numPattern);
        return StringUtils.isEmpty(str) ? "0" : str;
    }

    private void disposeParam(String key, Map<String, String> map, boolean isNum) {
        if (map.containsKey(key)) {
            if (isNum) {
                map.put(key, getNum(map.get(key)));
            }
            else {
                map.put(key, getRom(map.get(key)));
            }
        }
    }

    /**
     * 获取手机详情和参数信息
     *
     * @param subUrl
     * @return
     * @throws IOException
     */
    public Object[] getDeviceParam(String subUrl) throws IOException {
        String url = DeviceConstant.ROOT_URL + subUrl;
        Document document = Jsoup.connect(url).get();
        Elements elements = document.select(".info-list-fr");
        log.info("device param url:" + url);
        log.info("size:" + elements.size());
        Map<String, String> deviceDetailMap = new HashMap<>();
        if (elements.size() == 0) {
            log.info("没有参数信息");
        }
        else {
            Elements liList = elements.first().select("li");
            log.info("li size:" + liList.size());
            Map<String, String> oriDeviceDetailMap = new HashMap<>();
            for (Element li : liList) {
                String attr = JsoupUtils.parsePath(li, ".product-link");
                String bestAttr = JsoupUtils.parsePath(li, ".main-param-hyzg");
                String level = JsoupUtils.parsePath(li, ".text");
                String label = JsoupUtils.parsePath(li, "label");
                String desc = JsoupUtils.parsePath(li, ".text-hui");

                label = clearChar(label);
                bestAttr = clearChar(bestAttr);
                level = getPercent(level);
                if (StringUtils.isEmpty(label)) {
                    continue;
                }
                deviceDetailMap.put(targetKeyMap.get(label), attr);
                deviceDetailMap.put(targetKeyMap.get(label + "_level"), level);
                deviceDetailMap.put(targetKeyMap.get(label + "_desc"), desc);
                deviceDetailMap.put(targetKeyMap.get(label + "_best"), bestAttr);
                oriDeviceDetailMap.put(label, attr);
                oriDeviceDetailMap.put(label + "_level", level);
                oriDeviceDetailMap.put(label + "_desc", desc);
                oriDeviceDetailMap.put(label + "_best", bestAttr);
            }
        }


        Elements divElements = document.select(".detailed-parameters");
        Map<String, String> deviceDetailParam = new HashMap<>();
        if (divElements.size() == 0) {
            log.info("没有详细信息");
        }
        else {
            Elements tableElements = divElements.first().select("table");
            Map<String, String> oriDetailParam = new HashMap<>();
            log.info("tableElements.size:" + tableElements.size());
            for (Element element : tableElements) {
                Elements trList = element.select("tr");
                if (trList.size() == 0) {
                    continue;
                }
                int i = 0;
                String key = "";
                Map<String, String> paramMap = new HashMap<>();
                for (Element tr : trList) {
                    String thText = JsoupUtils.parsePath(tr, "th/span");
                    if (StringUtils.isEmpty(thText)) {
                        thText = JsoupUtils.parsePath(tr, "th");
                    }
                    String tdText = JsoupUtils.parsePath(tr, "td/span");
                    if (StringUtils.isEmpty(tdText)) {
                        tdText = JsoupUtils.parsePath(tr, "td");
                    }
                    if (StringUtils.isEmpty(thText) && StringUtils.isEmpty(tdText)) {
                        continue;
                    }
                    thText = clearChar(thText);
                    tdText = clearChar(tdText);
                    if (i == 0) {
                        key = tdText;
                    }
                    else {
                        paramMap.put(thText, tdText);
                    }
                    String detailParamKey = basicParamKeyMap.get(thText);
                    if (StringUtils.isNotEmpty(detailParamKey)) {
                        deviceDetailParam.put(detailParamKey, tdText);
                    }
                    i++;
                }
                deviceDetailParam.put(paramHeaderMap.get(key), JSONObject.toJSONString(paramMap));
                oriDetailParam.put(key, JSONObject.toJSONString(paramMap));
            }
        }

        disposeParam(priceKey, deviceDetailParam, true);
        disposeParam(ramKey, deviceDetailParam, false);
        disposeParam(romKey, deviceDetailParam, false);
        disposeParam(nvidiaRomKey, deviceDetailParam, false);
//        log.info("oriDeviceDetailMap:" + JSONObject.toJSONString(oriDeviceDetailMap));
//        log.info("oriDetailParam:" + JSONObject.toJSONString(oriDetailParam));
//        log.info("deviceDetailMap:" + JSONObject.toJSONString(deviceDetailMap));
//        log.info("deviceDetailParam:" + JSONObject.toJSONString(deviceDetailParam));
        return new Object[]{deviceDetailMap, deviceDetailParam};
    }

    public Map<String, String> getDevicePerformance(String subUrlComment) throws IOException {
        String url = DeviceConstant.ROOT_URL + subUrlComment;
        log.info("commentUrl:" + url);
        Document document = Jsoup.connect(url).get();

        Map<String, String> performanceMap = new HashMap<>();
        Element totalScoreEle = document.select(".total-score").first();
        String totalScore = JsoupUtils.parsePath(totalScoreEle, "strong");
        totalScore = getPercent(totalScore);
        performanceMap.put("totalScore", totalScore);
        Elements divList = document.select(".features-circle");
        for (Element div : divList) {
            String label = JsoupUtils.parsePath(div, ".circle-text");
            String score = JsoupUtils.parsePath(div, ".circle-value");
            String key = commentKeyMap.get(label);
            if (StringUtils.isNotEmpty(key)) {
                performanceMap.put(key, score);
            }
        }

        String goodWord = "";
        Element commentGood = document.selectFirst("#_j_words_filter");
        if (null != commentGood) {
            List<String> wordList = new ArrayList<>();
            Elements goodList = commentGood.select(".good-words");
            for (Element node : goodList) {
                wordList.add(node.text());
            }
            goodWord = StringUtils.join(wordList, ",");
        }
        performanceMap.put("goodWord", goodWord);
//        log.info("performanceMap:" + performanceMap);
        return performanceMap;
    }

    public List<Map<String, String>> getDeviceComment(Integer deviceId) {
        Integer page = 1;
        String urlFormat = "https://detail.zol.com.cn/xhr4_Review_GetList_%5EproId=" + deviceId + "%5Epage=";
        String commentUrl;
        List<Map<String, String>> commentList = new ArrayList<>();
        while (true) {
            commentUrl = urlFormat + page + ".html";
            log.info(commentUrl);
            String result = HttpClientUtils.sendGet(commentUrl, 10);
            Map<String, String> resultMap = JSONObject.parseObject(result, Map.class);
            String xml = resultMap.get("list");
            if (StringUtils.isEmpty(xml)) {
                log.info("获取评论结束退出");
                break;
            }
            Document commentDoc = Jsoup.parse(xml);
            Elements commentEles = commentDoc.select(".comments-item");
            if (commentEles.size() == 0) {
                log.info("comment size为0获取评论结束退出");
                break;
            }
            for (Element commentEle : commentEles) {
                Map<String, String> commentMap = new HashMap<>();
                String article = JsoupUtils.parsePath(commentEle, ".words-article");
                if (StringUtils.isEmpty(article)) {
                    article = JsoupUtils.parsePath(commentEle,".content-inner");
                }
                String userName = JsoupUtils.parsePath(commentEle, ".comments-user/.name");
                String date = JsoupUtils.parsePath(commentEle, ".date");
                commentMap.put("username", userName);
                commentMap.put("createDate", date);
                commentMap.put("article", article);
                commentList.add(commentMap);
            }
            page++;

        }
        log.info("commentList size:" + commentList.size());
        return commentList;
    }


    public void getPhoneEveryType(Integer phoneId) {
        String detailUrl = String.format(DeviceConstant.EVERY_PHONE_TYPE_URL, phoneId);
        String result = HttpClientUtils.sendGet(detailUrl);
        Map<String, Object> data = JSONObject.parseObject(result);
        log.info("data:" + data);
    }

    public static void main(String[] args) throws IOException {
        DeviceDetailClimb deviceDetailClimb = new DeviceDetailClimb();
//        deviceDetailClimb.getPhoneEveryType(1390597);
//        deviceDetailClimb.getDeviceDetail("/1455/1454334/review.shtml");
//        deviceDetailClimb.getDeviceDetail("/1391/1390597/review.shtml");  //1391/1390597/review.shtml
        List<Map<String, String>> result = deviceDetailClimb.getDeviceComment(11978);
        log.info("result:" + result);
    }
}
