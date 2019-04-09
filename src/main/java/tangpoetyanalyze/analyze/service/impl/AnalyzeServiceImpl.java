package tangpoetyanalyze.analyze.service.impl;

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import tangpoetyanalyze.analyze.dao.AnalyzeDao;
import tangpoetyanalyze.analyze.entity.PoetryInfo;
import tangpoetyanalyze.analyze.model.AuthorCount;
import tangpoetyanalyze.analyze.model.WordCount;
import tangpoetyanalyze.analyze.service.AnalyzeService;

import java.util.*;

public class AnalyzeServiceImpl implements AnalyzeService {
    //依赖数据库查询层
    private final AnalyzeDao analyzeDao;

    public AnalyzeServiceImpl(AnalyzeDao analyzeDao) {
        this.analyzeDao = analyzeDao;
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        //此处可对结果进行排序（1.Dao层SQL排序；2.Service排序，对List集合进行排序）
        List<AuthorCount> authorCounts = analyzeDao.analyzeAuthorCount();
        Collections.sort(authorCounts, new Comparator<AuthorCount>() {
            @Override
            public int compare(AuthorCount o1, AuthorCount o2) {
//                //按照count升序
//                return o1.getCount() - o2.getCount();
                //降序
                return -1*o1.getCount().compareTo(o2.getCount());

            }
        });
        return authorCounts;
    }

    @Override
    public List<WordCount> analyzeWordCloud() {
        //1.查询出所有的数据；2.取出title、content；
        // 3.分词（过滤空的，字符，数字，长度大于1）；4.统计key-value
        Map<String , Integer> map = new HashMap<>();//包含了词的名称、词出现的频率

        List<PoetryInfo> poetryInfos = analyzeDao.queryAllpoetryInfo();
        for(PoetryInfo poetryInfo :poetryInfos){
            List<Term> terms = new ArrayList<>();//term下将放的是每一个真实的词
            String title = poetryInfo.getTitle();
            String content = poetryInfo.getContent();
            terms.addAll(NlpAnalysis.parse(title).getTerms());
            terms.addAll(NlpAnalysis.parse(content).getTerms());
            //过滤
            Iterator<Term> iterator = terms.iterator();
            while(iterator.hasNext()){
                Term term = iterator.next();
                //词性的过滤
                if(term.getNatureStr() == null || term.getNatureStr().
                        equals("w")){
                    iterator.remove();
                    continue;
                }
                //词的过滤
                if(term.getRealName().length()<2){
                    iterator.remove();
                    continue;
                }
                //统计词出现的频率
                String realName = term.getRealName();
                Integer count = 0;
                if(map.containsKey(realName)){
                    count = map.get(realName)+1;
                }else{
                    count = 1;
                }
                map.put(realName, count);
            }
        }
        //转换一下MAP
        List<WordCount> wordCounts = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : map.entrySet()){
            WordCount wordCount = new WordCount();
            wordCount.setWord(entry.getKey());
            wordCount.setCount(entry.getValue());
            wordCounts.add(wordCount);
        }
        return wordCounts;
    }


}
