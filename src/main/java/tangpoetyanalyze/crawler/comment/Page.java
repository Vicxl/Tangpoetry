package tangpoetyanalyze.crawler.comment;

/**
 * 文档解析
 */

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Page {
    /**
    * 数据网站的根地址
    */
    private final String base;
    /**
     * 具体网页的地址
     */
    private final String path;
    /**
     * 网页的DOM对象（文档对象）
     */
    private HtmlPage htmlPage;
    /**
     * 标识网页是否是详情页
     */
    private final boolean detail;



    public String getUrl(){
        return this.base+this.path;
    }

    //子页面对象集合
    private Set<Page> subPage = new HashSet<>();

    //创建SetData 包装类，提取数据，存放数据
    private DataSet dataSet = new DataSet();



}
