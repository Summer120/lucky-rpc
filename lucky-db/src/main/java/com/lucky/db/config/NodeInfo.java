package com.lucky.db.config;

import com.lucky.db.exception.ConfigException;
import com.lucky.db.shard.HashRule;
import com.lucky.db.shard.Rule;
import lombok.Getter;
import org.springframework.util.Assert;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:chaoqiang.zhou
 * @Description:节点信息
 * @Date:Create in 13:59 2017/7/19
 */
public class NodeInfo {

    /**
     * 分片规则:hash,data,其他
     */
    private Rule rule;
    /*数据库分片，还是表分片*/
    private String type;
    private String mode;
    @Getter
    private String nodeName;
    List<Node> nodes;


    public NodeInfo() {
        this.rule = createRule(this.type);
    }

    public NodeInfo(Element element) {
        this.nodeName = element.getAttribute("name");
        this.type = element.getAttribute("type");
        this.rule = createRule(this.type);
        this.mode = element.getAttribute("mode");
        //子节点信息
        if (element.hasChildNodes()) {
            NodeList childNodes = element.getChildNodes();
            List<Node> list = new ArrayList<>(childNodes.getLength());
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (!(childNodes.item(i) instanceof Element)) continue;
                Node n = new Node((Element) childNodes.item(i));
                list.add(n);
            }
            if (!list.isEmpty()) this.nodes = list;
        }

    }

    /*通过规则获取对应的节点信息*/
    public Node getNode(String value) {
        Assert.notNull(value, "the  share type can not be null");
        int modeVal = (int) this.rule.getMatchKey(value);
        for (Node n : nodes) {
            int remainder = Integer.valueOf(n.getRemainders());
            if (remainder == modeVal) {
                return n;
            }
        }
        return null;
    }


    /***
     * 创建匹配规则信息
     * @param type
     * @return
     */
    private Rule createRule(String type) {
        Assert.notNull(type, "the  share type can not be null");
        switch (type) {
            case "hash":
                int count = Integer.valueOf(this.mode);
                return new HashRule(count);
            default:
                throw new ConfigException("unsupported rule type " + type);
        }
    }
}
