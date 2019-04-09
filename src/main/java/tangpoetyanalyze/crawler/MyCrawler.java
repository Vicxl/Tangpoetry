package tangpoetyanalyze.crawler;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tangpoetyanalyze.crawler.comment.Page;
import tangpoetyanalyze.crawler.parse.DataPageParse;
import tangpoetyanalyze.crawler.parse.DocumentParse;
import tangpoetyanalyze.crawler.parse.Parse;
import tangpoetyanalyze.crawler.pipeline.ConsolePipeline;
import tangpoetyanalyze.crawler.pipeline.Pipeline;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class MyCrawler {

    private final Logger logger = LoggerFactory.getLogger(MyCrawler.class);

    /**
     *放置文档页面
     */
    private Queue<Page> docQueue = new LinkedBlockingQueue<>();
    /**
     *放置详情页面
     */
    private Queue<Page> detailQueue = new LinkedBlockingQueue<>();
    /**
     * 采集页面
     */
    private WebClient webClient;
    /**
     * 所有的解析器
     */
    private List<Parse> parseList = new LinkedList<>();
    /**
     * 所有的清洗器
     */
    private List<Pipeline> pipelineList = new LinkedList<>();
    /**
     * 线程调度器
     */
    private ExecutorService executorService;

    public MyCrawler(){
        this.webClient=new WebClient(BrowserVersion.CHROME);
        this.webClient.getOptions().setJavaScriptEnabled(false);
        //线程工厂
        this.executorService = Executors.newFixedThreadPool
                (8, new ThreadFactory() {
                    private final AtomicInteger id = new  AtomicInteger(0);
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("MyCrawal-Thread-"+id.getAndIncrement());
                        return thread;
            }
        });
    }

    public void start(){
        //爬取
        //解析  //解析与清洗分开做
        //清洗

        //(方法引用)
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                parse();
            }
        });
        this.executorService.submit(new Runnable() {
            @Override
            public void run() {
                pipeline();
            }
        });
    }

    /**
     * 文档解析
     * 采集解析的线程调度器
     */
    private void parse()  {
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Parse occur exception {} .",
                        e.getMessage());
            }
            final Page page = this.docQueue.poll();
            //System.out.println("Parse----"+page);
            if (page == null) {
                continue;
            }
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {

                    //有page就去解析
                    try {
                        //采集页面
                        HtmlPage htmlpage = MyCrawler.this.webClient.getPage(page.getUrl());
                        page.setHtmlPage(htmlpage);
                        //如果采集的page不是文档页就会去解析
                        for (Parse parse : MyCrawler.this.parseList) {
                            parse.parse(page);
                        }
                        //详情页面
                        if (page.isDetail()) {
                            MyCrawler.this.detailQueue.add(page);
                        } else {
                            //取子页面加到文档对象中
                            Iterator<Page> iterator = page.getSubPage().iterator();
                            while (iterator.hasNext()) {
                                Page subPage = iterator.next();
                                //System.out.println(subPage);
                                MyCrawler.this.docQueue.add(subPage);
                                iterator.remove();//配置的subpage最终被删除完毕
                            }
                        }
                    } catch (IOException e) {
                        //说明解析任务发生的异常情况
                        logger.error("Parse task occur exception {} .",
                                e.getMessage());
                    }
                }
            });
        }
    }

    private void pipeline(){
        while(true){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error("Pipeline occur exception {} .",
                        e.getMessage());
            }
            final Page page = this.detailQueue.poll();
            if(page==null){
                continue;
            }
            this.executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for(Pipeline pipeline:MyCrawler.this.pipelineList){
                        pipeline.pipeline(page);
                    }
                }
            });
        }
    }

    public void addPage(Page page){
        this.docQueue.add(page);
    }

    public void addParse(Parse parse){
        this.parseList.add(parse);
    }

    public void addPipeline(Pipeline pipeline){
        this.pipelineList.add(pipeline);
    }

    /**
     * 停止爬虫
     */
    public void stop(){
        if(this.executorService!=null &&
                !this.executorService.isShutdown()){
            this.executorService.shutdown();
        }
        logger.info("MyCrawler stopped ...");
    }


}
