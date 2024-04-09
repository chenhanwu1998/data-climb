package com.chw.climb;

import com.alibaba.fastjson.JSONObject;
import com.chw.constant.DeviceConstant;
import com.chw.entity.DeviceBasicInfo;
import com.chw.entity.DeviceCompanyType;
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
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取每个company的mobile基础信息
 */
@Slf4j
public class DeviceBasicClimb {

    public static final Pattern pattern = Pattern.compile("[~\\d]*(\\d+)[~\\d]*");


    /**
     * 解析获取手机基础信息
     *
     * @param proId
     * @param page
     * @return
     * @throws IOException
     */
    public static List<DeviceBasicInfo> jsoupAnalysis(Integer proId, Integer page, String url) throws IOException {
        String mobilePhoneBasicUrl = String.format(url, proId, page);
        log.info("mobilePhoneBasicUrl:" + mobilePhoneBasicUrl);
        Document doc = Jsoup.connect(mobilePhoneBasicUrl).get();
        Elements elements = doc.select("#J_PicMode");
        log.info("size:" + elements.size());
        if (elements.size() == 0) {
            log.info("当前页面没有数据返回");
            return null;
        }
        Element div = elements.get(0);
        Elements liList = div.select("li");
        if (liList.size() == 0) {
            log.info("当前页面没有数据返回");
            return null;
        }
        List<DeviceBasicInfo> phoneBasicInfoList = new ArrayList<>();
        for (Element li : liList) {
            DeviceBasicInfo phoneBasicInfo = new DeviceBasicInfo();
            String id = li.attr("data-follow-id");
            if (StringUtils.isEmpty(id)) {
                continue;
            }
            String detailUrl = JsoupUtils.parsePath(li, "h3/a", "href");
            String desc = JsoupUtils.parsePath(li, "h3/span");
            String name = JsoupUtils.parsePath(li, "a/img", "alt");
            String imgUrl = JsoupUtils.parsePath(li, "a/img", ".src");

            phoneBasicInfo.setId(Integer.parseInt(id.replace("p", "")));
            phoneBasicInfo.setProId(proId);
            phoneBasicInfo.setRemark(desc);
            phoneBasicInfo.setDetailUrl(detailUrl);
            phoneBasicInfo.setName(name);
            phoneBasicInfo.setImgUrl(imgUrl);

            String priceText = JsoupUtils.parsePath(li, ".price-row");
            Matcher matcher = pattern.matcher(priceText);
            String price = "0";
            if (matcher.find()) {
                price = matcher.group(0);
            }
            phoneBasicInfo.setPrice(Double.parseDouble(price));
            String score = JsoupUtils.parsePath(li, ".score");
            if (StringUtils.isEmpty(score)) {
                score = "0";
            }
            String commentText = JsoupUtils.parsePath(li, ".comment-num");
            String heat = "0";
            Matcher commentMatch = pattern.matcher(commentText);
            if (commentMatch.find()) {
                heat = commentMatch.group(0);
            }
            String commentUrl = JsoupUtils.parsePath(li, ".comment-num", "href");
            phoneBasicInfo.setScore(Double.parseDouble(score));
            phoneBasicInfo.setHeat(Long.parseLong(heat));
            phoneBasicInfo.setCommentUrl(commentUrl);
            phoneBasicInfoList.add(phoneBasicInfo);
        }
        return phoneBasicInfoList;
    }

    public List<DeviceCompanyType> getPhoneCompanyBasicInfo() {
        String url = DeviceConstant.PHONE_COMPANY_URL;
        return getDeviceBasicInfo(url);
    }

    /**
     * 获取mobile公司数据
     *
     * @return
     */
    public List<DeviceCompanyType> getComputerCompanyBasicInfo() {
        String url = DeviceConstant.COMPUTER_COMPANY_URL;
        return getDeviceBasicInfo(url);
    }

    /**
     * 获取mobile公司数据
     *
     * @return
     */
    public List<DeviceCompanyType> getDeviceBasicInfo(String url) {
        String result = HttpClientUtils.sendGet(url);
        Map<String, Object> resultMap = JSONObject.parseObject(result);
        List<DeviceCompanyType> companyTypes = new ArrayList<>();
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            companyTypes.add(JSONObject.parseObject(JSONObject.toJSONString(entry.getValue()), DeviceCompanyType.class));
        }
        log.info("companyTypes:" + companyTypes);
        return companyTypes;
    }

    public static void main(String[] args) {
        DeviceBasicClimb deviceBasicClimb = new DeviceBasicClimb();
        List<DeviceCompanyType> companyTypeList = deviceBasicClimb.getComputerCompanyBasicInfo();
        log.info("" + companyTypeList);
        companyTypeList = deviceBasicClimb.getPhoneCompanyBasicInfo();
        log.info("computer:" + companyTypeList);
    }

}
