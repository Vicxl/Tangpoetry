package tangpoetyanalyze.crawler.parse;

import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import tangpoetyanalyze.crawler.comment.Page;

public class DocumentParse implements Parse {
    @Override
    public void parse(final Page page) {
        if(page.isDetail()){
            return;
        }
        /**
         * 提取超链接
         */
        HtmlPage htmlPage = page.getHtmlPage();
        htmlPage.getBody()
                .getElementsByAttribute("div",
                        "class","typecont")
                .forEach(div->{
                    DomNodeList<HtmlElement> aNodeList = div.
                            getElementsByTagName("a");
                    aNodeList.forEach(aNode->{
                        //提取超链接
                        String path = aNode.getAttribute("href");//返回子页面的path
                        Page subPage = new Page(
                                page.getBase(),
                                path,
                               true
                        );
                        page.getSubPage().add(subPage);
                    }
                    );
                });

    }
}
