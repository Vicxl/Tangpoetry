package tangpoetyanalyze;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;
import spark.Route;
import tangpoetyanalyze.analyze.dao.AnalyzeDao;
import tangpoetyanalyze.analyze.dao.impl.AnalyzeDaoImpl;
import tangpoetyanalyze.analyze.entity.PoetryInfo;
import tangpoetyanalyze.analyze.model.AuthorCount;
import tangpoetyanalyze.analyze.service.AnalyzeService;
import tangpoetyanalyze.analyze.service.impl.AnalyzeServiceImpl;
import tangpoetyanalyze.config.ConfigProperties;
import tangpoetyanalyze.config.ObjectFactory;
import tangpoetyanalyze.crawler.MyCrawler;
import tangpoetyanalyze.crawler.comment.Page;
import tangpoetyanalyze.crawler.parse.DataPageParse;
import tangpoetyanalyze.crawler.parse.DocumentParse;
import tangpoetyanalyze.crawler.pipeline.ConsolePipeline;
import tangpoetyanalyze.crawler.pipeline.DataBasePipeline;
import tangpoetyanalyze.web.WebController;

import java.time.LocalDateTime;
import java.util.List;
import static spark.Spark.get;


public class TangpoetyAnalyzeApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(TangpoetyAnalyzeApplication.class);
    public static void main(String[] args) {

        WebController webController = ObjectFactory.getInstance()
                .getObject(WebController.class);
        //运行了Web服务，提供接口
        LOGGER.info("Web Server launch ...");
        webController.launch();

        //启动爬虫（可以降低爬虫所带来的资源浪费）
        if(args.length == 1&& args[0].equals("run_myCrawler")){
            MyCrawler myCrawler = ObjectFactory.getInstance().getObject(MyCrawler.class);
            LOGGER.info("MyCrawler start ...");
            myCrawler.start();
        }

    }
}
