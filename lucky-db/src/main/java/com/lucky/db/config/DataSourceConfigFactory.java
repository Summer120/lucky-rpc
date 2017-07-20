package com.lucky.db.config;

/**
 * @Author:chaoqiang.zhou
 * @Description:包含了所有得datasource连接的信息
 * @Date:Create in 17:43 2017/6/24
 */

import com.lucky.db.exception.ConfigException;
import lucky.util.config.ActiveProfileConfig;

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
                            dataSourceParams.setUrl(DataSourceConfigFactory.getProperty(el.getAttribute("value")));
                            break;
                        case "MaxIdleConns":
                            dataSourceParams.setMaxIdleConns(Integer.parseInt(DataSourceConfigFactory.getProperty(el.getAttribute("value"))));
                            break;
                        case "MaxOpenConns":
                            dataSourceParams.setMaxOpenConns(Integer.parseInt(DataSourceConfigFactory.getProperty(el.getAttribute("value"))));
                            break;
                        case "Username":
                            dataSourceParams.setUserName(DataSourceConfigFactory.getProperty(el.getAttribute("value")));
                            break;
                        case "Password":
                            dataSourceParams.setPassword(DataSourceConfigFactory.getProperty(el.getAttribute("value")));
                            break;
                    }
                }
                dataSourceConfig.setDataSourceParams(dataSourceParams);
                //存入公共的  hashmapl里
                dataSourceConfigs.put(dataSourceConfig.getName(),dataSourceConfig);
//            String name = dataSourceConfig.getName();
//            System.out.println("=name="+ dataSourceConfigs.get(name).getName());
//            System.out.println("=driver="+ dataSourceConfigs.get(name).getDriver());
//            System.out.println("=ConnString="+ dataSourceConfigs.get(name).getDataSourceParams().getUrl());
//            System.out.println("=MaxIdleConns="+ dataSourceConfigs.get(name).getDataSourceParams().getMaxIdleConns());
//            System.out.println("=MaxOpenConns="+ dataSourceConfigs.get(name).getDataSourceParams().getMaxOpenConns());
//            System.out.println("=Username="+ dataSourceConfigs.get(name).getDataSourceParams().getUserName());
//            System.out.println("=Password="+ dataSourceConfigs.get(name).getDataSourceParams().getPassword());
//            System.out.println("=====结束遍历database===== "+ i + dataSourceConfigs.get(dataSourceConfig.getName()));

            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }



    /**
     * @Author: ledary 武刚鹏
     * @param property
     * @return  String
     * @Description: 解析${}方法，${}占位符不合法时，原样返回
     * @Date: 2017年7月19日20:56:53
     */
    private static String getProperty(String property){
        char [] stringArr = property.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder  key  = new StringBuilder();
        int beginIndex =0;
        int endIndex = 0;
        for( int i=0;i<stringArr.length;i++){
            if( '$' == stringArr[i] && '{' == stringArr[i+1]) {
                beginIndex = i+2;
                i++;
            }else if(stringArr[i] == '}'){
                endIndex = i;
                stringBuilder.append(ActiveProfileConfig.get(property.substring(beginIndex,endIndex)));
                beginIndex = 0;
                endIndex = 0;
            }else if( beginIndex == 0){
                stringBuilder.append(stringArr[i]);
            }
        }
        return stringBuilder.toString();
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


    }
}
