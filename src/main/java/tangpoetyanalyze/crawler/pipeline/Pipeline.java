package tangpoetyanalyze.crawler.pipeline;

import tangpoetyanalyze.crawler.comment.Page;

public interface Pipeline {
    /*
    处理page中的数据
     */
    void pipeline(final Page page);
}
