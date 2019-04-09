package tangpoetyanalyze.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tangpoetyanalyze.analyze.dao.AnalyzeDao;
import tangpoetyanalyze.analyze.dao.impl.AnalyzeDaoImpl;
import tangpoetyanalyze.analyze.service.AnalyzeService;
import tangpoetyanalyze.analyze.service.impl.AnalyzeServiceImpl;
import tangpoetyanalyze.crawler.MyCrawler;
import tangpoetyanalyze.crawler.comment.Page;
import tangpoetyanalyze.crawler.parse.DataPageParse;
import tangpoetyanalyze.crawler.parse.DocumentParse;
import tangpoetyanalyze.crawler.pipeline.ConsolePipeline;
import tangpoetyanalyze.crawler.pipeline.DataBasePipeline;
import tangpoetyanalyze.web.WebController;

import javax.sql.DataSource;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class ObjectFactory {

    private static final ObjectFactory instance = new
            ObjectFactory();//饿汉式
    private final Logger logger = LoggerFactory.getLogger(ObjectFactory.class);

    /*
    存放所有的对象
     */
    private final Map<Class, Object>
            objectHashMap = new HashMap<>();

    private ObjectFactory(){
        //1.初始化配置对象
       initConfigProperties();
        //2.数据源对象
        initDataSource();
        //3.初始化爬虫对象
        initCrawler();
        //4.web对象
        initWebController();
        //5.打印输出对象清单
        printObjectList();
    }

    private void initWebController() {
        //service
        DataSource dataSource = getObject(DataSource.class);
        AnalyzeDao analyzeDao = new AnalyzeDaoImpl(dataSource);
        AnalyzeService analyzeService = new AnalyzeServiceImpl(analyzeDao);
        WebController webController = new WebController(analyzeService);

        objectHashMap.put(WebController.class, webController);
    }

    private void initCrawler() {
        ConfigProperties configProperties = getObject
                (ConfigProperties.class);
//        ConfigProperties configProperties = new ConfigProperties();
        DataSource dataSource = getObject(DataSource.class);

        final Page page = new Page(
                configProperties.getCrawlerBase(),
                configProperties.getCrawlerPath(),
                configProperties.isCrawlerDetail()
        );

        MyCrawler myCrawler = new MyCrawler();
        myCrawler.addParse(new DocumentParse());//爬虫一启动，就会有一个入口文档；
        myCrawler.addParse(new DataPageParse());
        if(configProperties.isEnableConsole()){
            myCrawler.addPipeline(new ConsolePipeline());
        }
        myCrawler.addPipeline(new DataBasePipeline(dataSource));
        myCrawler.addPage(page);
        objectHashMap.put(MyCrawler.class,myCrawler);
//        myCrawler.start();//直接在子类实现
    }

    private void initConfigProperties(){
        ConfigProperties configProperties = new ConfigProperties();
        objectHashMap.put(ConfigProperties.class, configProperties);
        //打印配置信息
        logger.info("ConfigProperties info :\n{}",
                configProperties.toString());
    }

    private void initDataSource(){
        ConfigProperties configProperties = getObject(ConfigProperties.class);
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(configProperties.getDbUserName());
        dataSource.setPassword(configProperties.getDbPassword());
        dataSource.setDriverClassName(configProperties.getDbDriverClass());
        dataSource.setUrl(configProperties.getDbUrl());
        //放到对象工厂中
        objectHashMap.put(DataSource.class,dataSource);
    }

    /*
    获取对象
     */
    public  <T> T getObject(Class classz){
        if(!objectHashMap.containsKey(classz)){
            throw new IllegalArgumentException("class"
                    + classz.getName() + "not found Object");
    }
        return (T) objectHashMap.get(classz);

    }

    public static ObjectFactory getInstance(){
        return instance;
    }
    /*
    获取对象清单
     */
    private void printObjectList(){
        logger.info("----------------对象工厂清单----------------");
        for(Map.Entry<Class, Object> entry:objectHashMap.entrySet()){
            logger.info(String.format("\t[%s] --> [%s]",
                    entry.getKey().getCanonicalName(),
                    entry.getValue().getClass().getCanonicalName())
            );
        }
        logger.info("-------------------------------------------");
    }
}
