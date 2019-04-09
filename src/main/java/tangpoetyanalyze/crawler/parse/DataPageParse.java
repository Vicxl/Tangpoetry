package tangpoetyanalyze.crawler.parse;

import com.gargoylesoftware.htmlunit.html.*;
import tangpoetyanalyze.crawler.comment.Page;
//详情页面的解析

public class DataPageParse implements Parse{
    @Override
    public void parse(final Page page) {
        if(!page.isDetail()){
            return;
        }

        HtmlPage htmlPage = page.getHtmlPage();
        HtmlElement body = htmlPage.getBody();
        //标题
        //DOM的提取1.遍历；2.XPath；
        String titlePath = "//div[@class='cont']/h1/text()";
        DomText titleDom= (DomText) body.getByXPath(titlePath).get(0);
        String title = titleDom.asText();
        //朝代
        String dynastyPath = "//div[@class='cont']/p/a[1]";
        HtmlAnchor dynastyDom = (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
        String dynasty = dynastyDom.asText();
        //作者
        String authorPath = "//div[@class='cont']/p/a[2]";
        HtmlAnchor authorDom = (HtmlAnchor) body.getByXPath(authorPath).get(0);
        String author = authorDom.asText();
        //正文
        //html中结构一定一致、ID有可能不相同
        String contentPath = "//div[@class='cont']/div[@class='contson']";
        HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
        String content = contentDom.asText();

//        PoetryInfo poetryInfo = new PoetryInfo();
//        poetryInfo.setTitle(title);
//        poetryInfo.setDynasty(dynasty);
//        poetryInfo.setAuthor(author);
//        poetryInfo.setContent(content);
        /*
        把解析后的数据存储在page中
         */
        page.getDataSet().putData("title",title);
        page.getDataSet().putData("dynasty",dynasty);
        page.getDataSet().putData("author",author);
        page.getDataSet().putData("content",content);
         //只要有对象，都可以传到数据库中，可以加载更多的数据
        //可以添加其他的属性
        //page.getDataSet().putData("poetry",poetryInfo);
    }


}
