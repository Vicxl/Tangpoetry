package tangpoetyanalyze.config;

import lombok.Data;
import org.apache.xpath.operations.Bool;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Data
public class ConfigProperties {

    private String crawlerBase;
    private String crawlerPath;
    private boolean crawlerDetail;

    private String dbUserName;
    private String dbPassword;
    private String dbUrl;
    private String dbDriverClass;

    //决定控制台是否打印信息
    private boolean enableConsole;
    //对象实例化
    public ConfigProperties(){
        //从外部文件加载数据
        //读取数据文件
        InputStream inputStream = ConfigProperties.class.
                getClassLoader().getResourceAsStream("config.properties");
        Properties p = new Properties();
        try {
            p.load(inputStream);
            System.out.println(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //把字符串转成包装类型
        this.crawlerBase = String.valueOf(p.get("crawler.base"));
        this.crawlerPath = String.valueOf(p.get("crawler.path"));
        this.crawlerDetail = Boolean.parseBoolean(String.valueOf(p.get("crawler.detail")));

        this.dbUserName = String.valueOf(p.get("db.username"));
        this.dbPassword = String.valueOf(p.get("db.password"));
        this.dbDriverClass = String.valueOf(p.get("db.driver_class"));
        this.dbUrl = String.valueOf(p.get("db.url"));

        this.enableConsole = Boolean.valueOf(
                String.valueOf(p.getProperty("config.enable_console","false"))
        );
    }

}


