package tangpoetyanalyze.crawler.pipeline;

import tangpoetyanalyze.crawler.comment.Page;

import java.util.Map;

public class ConsolePipeline implements Pipeline {
    public void pipeline(final Page page){
        Map<String, Object> data = page.getDataSet().getData();
        //存储
        System.out.println(data);
    }
}
