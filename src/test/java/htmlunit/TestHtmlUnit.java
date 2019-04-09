package htmlunit;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;

public class TestHtmlUnit {
    public static void main(String[] args) {
        try(WebClient webClient = new WebClient(BrowserVersion.CHROME)){
            webClient.getOptions().setJavaScriptEnabled(false);
            HtmlPage htmlPage= webClient.getPage("https://so.gushiwen.org/shiwenv_45c396367f59.aspx");//get返回值上界继承自page
            HtmlElement body = htmlPage.getBody();
            //标题
            //DOM的提取1.遍历；2.XPath；
            String titlePath = "//div[@class='cont']/h1/text()";
            DomText titleDom= (DomText) body.getByXPath(titlePath).get(0);
            System.out.println(titleDom);
            String title = titleDom.asText();
//
//            //朝代
//            String dynastyPath = "//div[@class='cont']/p/a[1]";
//            HtmlAnchor dynastyDom = (HtmlAnchor) body.getByXPath(dynastyPath).get(0);
//            //System.out.println(dynastyDom);
//            String dynasty = dynastyDom.asText();
//            System.out.println(dynasty);
////            //作者
//            String authorPath = "//div[@class='cont']/p/a[2]";
//            HtmlAnchor authorDom = (HtmlAnchor) body.getByXPath(authorPath).get(0);
//            String author = authorDom.asText();
//            System.out.println(author);
////            //正文
////            //html中结构一定一致、ID有可能不相同
//            String contentPath = "//div[@class='cont']/div[@class='contson']";
//            HtmlDivision contentDom = (HtmlDivision) body.getByXPath(contentPath).get(0);
//            String content = contentDom.asText();
//            System.out.println(content);
//
////            PoetryInfo poetryInfo = new PoetryInfo();
////            poetryInfo.setTitle(title);
////            poetryInfo.setDynasty(dynasty);
////            poetryInfo.setAuthor(author);
////            poetryInfo.setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
