package com.chw.constant;

public class DeviceConstant {

    public static String ROOT_URL = "https://detail.zol.com.cn";

    public static String PHONE_COMPANY_URL = "https://detail.zol.com.cn/xhr3_Select_Manu_onlyData=1%5EsubcateId=57%5EhasPro=1.html";
    // %s %s 两个分别为proId和页数
    public static String PHONE_BASIC_URL = "https://detail.zol.com.cn/cell_phone_index/subcate57_%s_list_1_0_1_2_0_%s.html";
    // %s是phoneId
    public static String EVERY_PHONE_TYPE_URL = "https://detail.zol.com.cn/xhr5_Product_GetProSkuInfo_proId=%s.html";

    public static String COMPUTER_COMPANY_URL = "https://detail.zol.com.cn/xhr3_Select_Manu_onlyData=1%5EsubcateId=16%5EhasPro=1.html";

    public static String COMPUTER_BASIC_URL = "https://detail.zol.com.cn/notebook_index/subcate16_%s_list_1_0_1_2_0_%s.html";

}
