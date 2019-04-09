package tangpoetyanalyze.web;

import com.google.gson.Gson;
import spark.ResponseTransformer;
import spark.Spark;
import tangpoetyanalyze.analyze.model.AuthorCount;
import tangpoetyanalyze.analyze.model.WordCount;
import tangpoetyanalyze.analyze.service.AnalyzeService;
import tangpoetyanalyze.config.ObjectFactory;
import tangpoetyanalyze.crawler.MyCrawler;

import java.util.List;
/*
访问业务层
 */
public class WebController {

    private final AnalyzeService analyzeService;
    public WebController(AnalyzeService analyzeService) {
        this.analyzeService = analyzeService;
    }

    //host : http//:127.0.0.1:4567/
    //地址 ：/analyze/author_count
    private List<AuthorCount> analyzeAuthorCount(){
        return analyzeService.analyzeAuthorCount();
    }
    //host : http//:127.0.0.1:4567/
    //地址 ：/analyze/word_cloud
    private List<WordCount> analyzeWordCloud(){
        return analyzeService.analyzeWordCloud();
    }

    public void launch(){
        ResponseTransformer transformer = new JSONResponseTransformer();

        //前端静态文件的目录： src/main/resource/static
        Spark.staticFileLocation("static");

        //两个服务器接口
        Spark.get("/analyze/author_count",
                ((request, response) -> analyzeAuthorCount()),transformer);

        Spark.get("/analyze/word_cloud",
                ((request, response) -> analyzeWordCloud()), transformer);

        Spark.get("/myCrawler/stop",((request, response) -> {
            MyCrawler myCrawler = ObjectFactory.getInstance().
                    getObject(MyCrawler.class);
            myCrawler.stop();
            return "MyCrawler stop!!!";
        }));
    }

    public static class JSONResponseTransformer
            implements ResponseTransformer{
         //把对象转变成一个字符串
        private Gson gson = new Gson();

        @Override
        public String render(Object o) throws Exception {
            return gson.toJson(o);
        }
    }



}
