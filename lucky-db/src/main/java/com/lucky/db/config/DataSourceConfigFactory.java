package com.lucky.db.config;

/**
 * @Author:chaoqiang.zhou
 * @Description:包含了所有得datasource连接的信息
 * @Date:Create in 17:43 2017/6/24
 */

import com.lucky.db.exception.ConfigException;
import lucky.util.config.ActiveProfileConfig;

import lucky.util.config.PropertyPlaceholderHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.util.HashMap;



/**
 * <databases>
 * <database name="WorkFlowStatsWriteData" provider="mysql" driver="" url="">
 * <setting name="url" value=""/>
 * <setting name="MaxIdleConns" value=""/>
 * <setting name="MaxOpenConns" value=""/>
 * <setting name="Username" value=""/>
 * <setting name="Password" value=""/>
 * </database>
 * <database name="WorkFlowStatsReadData" provider="mysql" driver="${cmc.mysql.dirver} url=">
 * </database>
 * </databases>
 */
//参数可以后期进行参数化，配置，也就是不同的环境，可以进行${}替换操作，这样就可以切不同得环境
//${}中得参数，可以直接从ActiveProfileConfig类中进行获取，环境启动时，会把激活得配置文件得信息读入到该类中
public class DataSourceConfigFactory {



    //包含了所有的数据源的操作信息,初始化的时候进行加载配置信息，路径是/config/database.conf
    private static HashMap<String, DataSourceConfig> dataSourceConfigs = new HashMap<>();

    //项目启动的时候就加载
    static {

        //静态方法进行初始化操作
        //初始化dom工程建造类  -武刚鹏-2017年7月19日20:54:33
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try{
            String path=DataSourceConfigFactory.class.getResource("/").toString()+"config/db.sql.conf";
            path=path.replace("file:/","");
            //初始化dom工厂
            DocumentBuilder db = dbf.newDocumentBuilder();
            //建造dom类
            Document doc = db.parse(path);
            NodeList nodeList = doc.getElementsByTagName("database");

            //遍历database节点，把所有的name和driver放到datasourceconfig对象内
            for (int i = 0; i < nodeList.getLength(); i++) {
                DataSourceConfig dataSourceConfig = new DataSourceConfig();
                DataSourceConfig.DataSourceParams dataSourceParams = new DataSourceConfig. DataSourceParams();
                Element elem = (Element) nodeList.item(i);
                dataSourceConfig .setName(elem.getAttribute("name"));
                dataSourceConfig.setDriver(elem.getAttribute("driver"));
                NodeList childNodeList = elem.getElementsByTagName("setting");
                //循环遍历，把所有的用户和连接池信息放到datasourceconfigd的datasourceParams里
                for(int j =0 ;j<childNodeList.getLength();j++){
                    Element el = (Element) childNodeList.item(j);
                    switch(el.getAttribute("name")) {
                        case "ConnString":
//                          //工具类解析占位符， ActiveProfileConfig类完成对properties文件的解析
                            dataSourceParams.setUrl(PropertyPlaceholderHelper.
                                    defaultHolder.replacePlaceholders(el.getAttribute("value"),ActiveProfileConfig.configs));
                            break;
                        case "MaxIdleConns":
//                            dataSourceParams.setMaxIdleConns(Integer.parseInt(DataSourceConfigFactory.getProperty(el.getAttribute("value"))));
                            dataSourceParams.setMaxIdleConns(Integer.parseInt(PropertyPlaceholderHelper.
                                    defaultHolder.replacePlaceholders(el.getAttribute("value"),ActiveProfileConfig.configs)));

                            break;
                        case "MaxOpenConns":
                            dataSourceParams.setMaxOpenConns(Integer.parseInt(PropertyPlaceholderHelper.
                                    defaultHolder.replacePlaceholders(el.getAttribute("value"),ActiveProfileConfig.configs)));
                            break;
                        case "Username":
                            dataSourceParams.setUserName(PropertyPlaceholderHelper.
                                    defaultHolder.replacePlaceholders(el.getAttribute("value"),ActiveProfileConfig.configs));
                            break;
                        case "Password":
                            dataSourceParams.setPassword(PropertyPlaceholderHelper.
                                    defaultHolder.replacePlaceholders(el.getAttribute("value"),ActiveProfileConfig.configs));
                            break;
                    }
                }
                dataSourceConfig.setDataSourceParams(dataSourceParams);
                //存入公共的  hashmapl里
                dataSourceConfigs.put(dataSourceConfig.getName(),dataSourceConfig);



            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    //通过名称来获取
    public static DataSourceConfig get(String name) {
        DataSourceConfig dataSourceConfig = dataSourceConfigs.get(name);
        if (dataSourceConfig == null) {
            //配置的异常类信息
            throw new ConfigException("can not find db,name={}" + name);
        }
        return dataSourceConfig;
    }

    //测试方法 void main
    public static void main(String[] args) {
        DataSourceConfigFactory dscf = new DataSourceConfigFactory();
        DataSourceConfigFactory.dataSourceConfigs.size();
        for(Object key : DataSourceConfigFactory.dataSourceConfigs.keySet()) {   //只能遍历key
            System.out.print("Key = " + key + "\n");
            String name = key.toString();
            DataSourceConfig dsc = dataSourceConfigs.get(name);
            System.out.println("=driver=" + dsc.getDriver());
            System.out.println("=ConnString=" + dsc.getDataSourceParams().getUrl());
            System.out.println("=MaxIdleConns=" + dsc.getDataSourceParams().getMaxIdleConns());
            System.out.println("=MaxOpenConns=" + dsc.getDataSourceParams().getMaxOpenConns());
            System.out.println("=Username=" + dsc.getDataSourceParams().getUserName());
            System.out.println("=Password=" + dataSourceConfigs.get(name).getDataSourceParams().getPassword());
            System.out.println("=====结束遍历database===== " + dsc);
        }


    }
}
