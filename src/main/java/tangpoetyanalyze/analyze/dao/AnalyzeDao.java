package tangpoetyanalyze.analyze.dao;

import tangpoetyanalyze.analyze.entity.PoetryInfo;
import tangpoetyanalyze.analyze.model.AuthorCount;

import javax.swing.*;
import java.util.List;

/*
    把数据全都查出来（按照作者的名称去分组，取出来作者作词的数量）
 */
public interface AnalyzeDao {

    /*
    分析唐诗中作者的创作数量
     */
    List<AuthorCount> analyzeAuthorCount();
    /*
    查询所有的诗文，便于业务层分析
     */
    List<PoetryInfo> queryAllpoetryInfo();

}
