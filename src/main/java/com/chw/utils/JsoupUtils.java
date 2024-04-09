package com.chw.utils;

import org.jsoup.nodes.Element;

public class JsoupUtils {

    public static Element parse(Element node, String tag) {
        if (null == node) {
            return null;
        }
        Element targetNode = node.select(tag).first();
        return targetNode;
    }

    /**
     * 按path解析元素
     *
     * @param node
     * @param path
     * @param attr
     * @return
     */
    public static String parsePath(Element node, String path, String attr) {
        String[] tags = path.split("/");
        Element targetNode = node;
        for (String tag : tags) {
            targetNode = parse(targetNode, tag);
            if (null == targetNode) {
                break;
            }
        }
        if (null == targetNode) {
            return "";
        }
        else {
            if (StringUtils.isNotEmpty(attr)) {
                return targetNode.attr(attr);
            }
            else {
                return targetNode.text();
            }
        }
    }

    public static String parsePath(Element node, String path) {
        return parsePath(node, path, null);
    }
}
