package com.lucky.db.config;

import lombok.Getter;
import org.w3c.dom.Element;

/**
 * @Author:chaoqiang.zhou
 * @Description:节点信息
 * @Date:Create in 11:32 2017/7/19
 */
public class Node {

    //主节点
    @Getter
    private String read;
    //读节点信息
    @Getter
    private String write;

    //分表的时候，后缀别名
    private String suffix;


    @Getter
    private String remainders;


    public Node(Element element) {
        this.read = element.getAttribute("read");
        this.write = element.getAttribute("write");
        this.remainders = element.getAttribute("remainders");
    }
}
