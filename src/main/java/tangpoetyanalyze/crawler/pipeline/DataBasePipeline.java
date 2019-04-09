package tangpoetyanalyze.crawler.pipeline;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tangpoetyanalyze.crawler.comment.Page;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
@Data
public class DataBasePipeline implements Pipeline{

    private final Logger logger = LoggerFactory.getLogger(DataBasePipeline.class);
    //添加数据源
    private final DataSource dataSource;

    public DataBasePipeline(DataSource dataSource){
        this.dataSource = dataSource;
    }

    @Override
    public void pipeline(Page page) {


//        PoetryInfo poetryInfo = (PoetryInfo) page.getDataSet()
//                .getData("poetry");

        // 能解析多少数据解析多少数据，更灵活
//        PoetryInfo poetryInfo = new PoetryInfo();
//        poetryInfo.setTitle((String) page.getDataSet().getData("title"));
//        poetryInfo.setDynasty((String) page.getDataSet().getData("dynasty"));
//        poetryInfo.setAuthor((String) page.getDataSet().getData("author"));
//        poetryInfo.setContent((String) page.getDataSet().getData("ocntent"));
        String title = (String) page.getDataSet().getData("title");
        String dynasty = (String) page.getDataSet().getData("dynasty");
        String author = (String) page.getDataSet().getData("author");
        String content = (String) page.getDataSet().getData("content");

        String sql = "insert into poetry_info (title," +
                "dynasty, author, content) values (?,?,?,?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
        ){
            statement.setString(1, title);
            statement.setString(2, dynasty);
            statement.setString(3, author);
            statement.setString(4, content);
            statement.executeUpdate();//更新

        }catch (SQLException e){
            //打印写入数据库发生的异常情况
            logger.error("DataBase insert occur exception {} .",
                    e.getMessage());
        }

    }


}
