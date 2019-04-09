package tangpoetyanalyze.analyze.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tangpoetyanalyze.analyze.dao.AnalyzeDao;
import tangpoetyanalyze.analyze.entity.PoetryInfo;
import tangpoetyanalyze.analyze.model.AuthorCount;
import tangpoetyanalyze.analyze.service.AnalyzeService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AnalyzeDaoImpl implements AnalyzeDao {
    private final Logger logger = LoggerFactory.getLogger(AnalyzeDaoImpl.class);

    //准备数据源
    private final DataSource dataSource;

    public AnalyzeDaoImpl(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public List<AuthorCount> analyzeAuthorCount() {
        List<AuthorCount> datas = new ArrayList<>();
        String sql = "select count(*) as count, author from poetry_info group by author;";
        //自动关闭
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery()
        ){
            while(rs.next()){
                AuthorCount authorCount = new AuthorCount();
                authorCount.setAuthor(rs.getString("author"));
                authorCount.setCount(rs.getInt("count"));
                datas.add(authorCount);
            }
        }catch(SQLException e){
            //获取数据库查询异常
            logger.error("Database query occur exception {}.",
                    e.getMessage());
        }
        return datas;
    }

    @Override
    public List<PoetryInfo> queryAllpoetryInfo() {
        List<PoetryInfo> datas = new ArrayList<>();
        String sql = "select title, dynasty, author, content from poetry_info;";
        try(Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
        ){
            while(rs.next()){
                PoetryInfo poetryInfo = new PoetryInfo();
                poetryInfo.setTitle(rs.getString("title"));
                poetryInfo.setDynasty(rs.getString("dynasty"));
                poetryInfo.setAuthor(rs.getString("author"));
                poetryInfo.setContent(rs.getString("content"));
                datas.add(poetryInfo);
            }
        }catch (SQLException e){
            logger.error("Database query occur exception {}.",
                    e.getMessage());
        }
        return datas;
    }
}
