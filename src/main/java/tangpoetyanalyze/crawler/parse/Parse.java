package tangpoetyanalyze.crawler.parse;

import tangpoetyanalyze.crawler.comment.Page;

public interface Parse {
    /**
     * 解析页面
     * @param page
     */
    void parse(final Page page);
}
