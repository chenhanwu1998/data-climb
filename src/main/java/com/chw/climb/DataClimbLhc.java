package com.chw.climb;

import com.chw.entity.LhcData;
import com.chw.entity.LhcDataColor;
import com.chw.entity.LhcDataNum;
import com.chw.entity.LhcDataSx;
import com.chw.service.ILhcDataColorService;
import com.chw.service.ILhcDataNumService;
import com.chw.service.ILhcDataSxService;
import com.chw.utils.CollectionUtils;
import com.chw.utils.JsoupUtils;
import com.chw.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Order(3)
@Component
public class DataClimbLhc implements ApplicationRunner {

    @Autowired
    ILhcDataNumService lhcDataNumService;
    @Autowired
    ILhcDataColorService lhcDataColorService;
    @Autowired
    ILhcDataSxService lhcDataSxService;


    public void climbLhc() throws IOException {
//        lhcDataColorService.remove(Wrappers.lambdaQuery());
//        lhcDataSxService.remove(Wrappers.lambdaQuery());
//        lhcDataNumService.remove(Wrappers.lambdaQuery());

        String baseUrl = "https://kj.123565.com/kj/?year=";
//        List<List<LhcData>> data = new ArrayList<>();
        int id = 1;
        for (int year = 1976; year <= 2023; year++) {
            String url = baseUrl + year;
            Document document = Jsoup.connect(url).get();
            Element element = document.select("#whiteBox").first();
            if (null == element) {
                log.info("没有记录，跳过");
            }
            Elements divList = element.select(".kj-box");
            List<List<LhcData>> lhcDataTemp = new ArrayList<>();
            for (Element div : divList) {
                List<LhcData> lhcDataList = new ArrayList<>();
                Elements liList = div.select("li");
                for (Element li : liList) {
                    String numStr = JsoupUtils.parsePath(li, "dt");
                    String color = JsoupUtils.parsePath(li, "dt", "class");
                    String sxStr = JsoupUtils.parsePath(li, "dd");
                    if (StringUtils.isEmpty(numStr)) {
                        log.info("当前li没有数据，跳过");
                        continue;
                    }
                    LhcData lhcData = new LhcData();
                    lhcData.setYear(year);
                    lhcData.setNum(numStr);
                    lhcData.setColor(color);
                    lhcData.setSx(sxStr);
                    lhcDataList.add(lhcData);
                }
                if (CollectionUtils.isNotEmpty(lhcDataList)) {
                    lhcDataTemp.add(lhcDataList);
                }
            }
            List<List<LhcData>> targetList = new ArrayList<>();
            for (int i = lhcDataTemp.size() - 1; i >= 0; i--) {
                targetList.add(lhcDataTemp.get(i));
            }
            for (int i = 0; i < targetList.size(); i++) {
                int finalI = i;
                targetList.get(i).forEach(e -> e.setIndex(finalI + 1));
            }
            List<LhcDataNum> lhcDataNumList = new ArrayList<>();
            List<LhcDataColor> lhcDataColorList = new ArrayList<>();
            List<LhcDataSx> lhcDataSxList = new ArrayList<>();
            for (List<LhcData> list : targetList) {
                if (list.size() < 7) {
                    log.info("当前年份:{},期数:{},获取的数据不全", year, list.get(0).getIndex());
                    continue;
                }
                LhcDataNum lhcDataNum = new LhcDataNum();
                LhcDataColor lhcDataColor = new LhcDataColor();
                LhcDataSx lhcDataSx = new LhcDataSx();
                lhcDataNum.setYearNum(list.get(0).getYear());
                lhcDataNum.setIndexNum(list.get(0).getIndex());
                lhcDataColor.setYearNum(list.get(0).getYear());
                lhcDataColor.setIndexNum(list.get(0).getIndex());
                lhcDataSx.setYearNum(list.get(0).getYear());
                lhcDataSx.setIndexNum(list.get(0).getIndex());
                lhcDataNum.setId(id);
                lhcDataColor.setId(id);
                lhcDataSx.setId(id);

                lhcDataNum.setNum1(Integer.parseInt(list.get(0).getNum()));
                lhcDataNum.setNum2(Integer.parseInt(list.get(1).getNum()));
                lhcDataNum.setNum3(Integer.parseInt(list.get(2).getNum()));
                lhcDataNum.setNum4(Integer.parseInt(list.get(3).getNum()));
                lhcDataNum.setNum5(Integer.parseInt(list.get(4).getNum()));
                lhcDataNum.setNum6(Integer.parseInt(list.get(5).getNum()));
                lhcDataNum.setSpecNum(Integer.parseInt(list.get(6).getNum()));

                lhcDataColor.setColor1(list.get(0).getColor());
                lhcDataColor.setColor2(list.get(1).getColor());
                lhcDataColor.setColor3(list.get(2).getColor());
                lhcDataColor.setColor4(list.get(3).getColor());
                lhcDataColor.setColor5(list.get(4).getColor());
                lhcDataColor.setColor6(list.get(5).getColor());
                lhcDataColor.setSpecColor(list.get(6).getColor());

                lhcDataSx.setSx1(list.get(0).getSx());
                lhcDataSx.setSx2(list.get(1).getSx());
                lhcDataSx.setSx3(list.get(2).getSx());
                lhcDataSx.setSx4(list.get(3).getSx());
                lhcDataSx.setSx5(list.get(4).getSx());
                lhcDataSx.setSx6(list.get(5).getSx());
                lhcDataSx.setSpecSx(list.get(6).getSx());

                lhcDataNumList.add(lhcDataNum);
                lhcDataColorList.add(lhcDataColor);
                lhcDataSxList.add(lhcDataSx);
                id++;
            }
            for (List<LhcData> list : targetList) {
                for (LhcData temp : list) {
                    log.info(temp.toString());
                }
                log.info("---------------------");
            }
            lhcDataNumService.saveOrUpdateBatch(lhcDataNumList);
            lhcDataColorService.saveOrUpdateBatch(lhcDataColorList);
            lhcDataSxService.saveOrUpdateBatch(lhcDataSxList);
//            for (int i = 0; i < lhcDataNumList.size(); i++) {
//                log.info("lhcDataNum:" + lhcDataNumList.get(i));
//                log.info("lhcDataColor:" + lhcDataColorList.get(i));
//                log.info("lhcDataSx:" + lhcDataSxList.get(i));
//            }
//            data.addAll(targetList);
//            break;
        }

    }

    public static void main(String[] args) throws IOException {
        new DataClimbLhc().climbLhc();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        climbLhc();
    }
}
