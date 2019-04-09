package tangpoetyanalyze.analyze.service;

import tangpoetyanalyze.analyze.model.AuthorCount;
import tangpoetyanalyze.analyze.model.WordCount;

import java.util.List;

public interface AnalyzeService {
    /*
    分析唐诗中作者作词的数量
     */
    List<AuthorCount> analyzeAuthorCount();
    /*
    词云分析
     */
    List<WordCount> analyzeWordCloud();
}
