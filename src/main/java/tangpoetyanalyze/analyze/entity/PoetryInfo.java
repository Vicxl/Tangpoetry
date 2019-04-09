package tangpoetyanalyze.analyze.entity;

import lombok.Data;
/*
   前期将获取到的数据封装在类中

   后期是与数据库关联的数据
 */
@Data
public class PoetryInfo {
    //标题、朝代、作者、正文
    private String title;
    private String dynasty;
    private String author;
    private String content;
}
