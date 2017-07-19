package com.lucky.db.config;

import com.lucky.db.exception.ConfigException;
import com.sun.jndi.toolkit.url.UrlUtil;
import lucky.util.log.Logger;
import lucky.util.log.LoggerFactory;
import org.springframework.util.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author:chaoqiang.zhou
 * @Description:
 * @Date:Create in 14:04 2017/7/19
 */
public class ShardConfig {


    private static Logger logger = LoggerFactory.getLogger(ShardConfig.class);
    private static Map<String, NodeInfo> nodes = new ConcurrentHashMap<>();

    public static ShardConfig getDefault() {
        return Holder.defaultInstance;
    }

    private ShardConfig() {

    }

    public ShardConfig(String filePath) {
        //这里进行实例化nodes节点信息
        //<nodes>
        //  <node name="Order" shard="range">
        //      <node read="Order1_R" write="Order1_W" shard="none" start="1" end="1000000"/>
        //      <node read="Order2_R" write="Order2_W" shard="none" start="1000000" end="2000000"/>
        //  </node>
        //  <node name="UserOrder" shard="time">
        //      <node shard="hash" count="2" start="2015-10-01" end="2015-11-01">
        //          <node read="Order1_1_R" write="Order1_1_W" shard="none" mod="0"/>
        //          <node read="Order1_2_R" write="Order1_2_W" shard="none" mod="1"/>
        //      </node>
        //      <node shard="hash" count="2" start="2015-11-01" end="2015-12-01">
        //          <node read="Order2_1_R" write="Order2_1_W" shard="none" mod="0"/>
        //          <node read="Order2_2_R" write="Order2_2_W" shard="none" mod="1"/>
        //      </node>
        //  </node>
        //</nodes>

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            File file = new File(filePath);
            Document document = db.parse(file);
            NodeList nodeList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                if (!(nodeList.item(i) instanceof Element)) continue;

                Element elem = (Element) nodeList.item(i);
                NodeInfo shard = new NodeInfo(elem);
                this.nodes.put(shard.getNodeName(), shard);
            }
        } catch (Exception e) {
            throw new ConfigException(e);
        }
    }


    /**
     * 获取所有的分片节点信息
     *
     * @param nodeName
     * @return
     */
    public static NodeInfo getNodeInfo(String nodeName) {
        return nodes.get(nodeName);
    }


    public static Node getNode(String nodeName, String key) {

        Assert.notNull(nodeName);
        Assert.notNull(key);
        return nodes.get(nodeName).getNode(key);
    }

    public static class Holder {
        private static ShardConfig defaultInstance;
        static {
            try {
                URL etcUrl = Thread.currentThread().getContextClassLoader().getResource("config");
                String filePath = UrlUtil.decode(etcUrl.getPath()) + "/" + "shard.conf";
                if (filePath == null) {
                    defaultInstance = new ShardConfig();
                } else {
                    defaultInstance = new ShardConfig(filePath);
                }
            } catch (Exception e) {
                logger.error("load shard confi failed {}", e);
            }

        }
    }

}
