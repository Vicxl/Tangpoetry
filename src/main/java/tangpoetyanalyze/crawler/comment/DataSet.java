package tangpoetyanalyze.crawler.comment;

/**
 * 存储清洗的数据
 */

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
@ToString
public class DataSet {
    /**
     * data，把DOM（文档）解析，清洗之后的数据（标题、朝代、作者、正文）
     * 扩展
     */
    private Map<String, Object> data = new HashMap<>();
    public void putData(String key, Object value){
        this.data.put(key,value);//在此处获取要想获取数据
    }

    public Object getData(String key){
        return this.data.get(key);
    }

    public Map<String, Object> getData(){
        return new HashMap<>(this.data);//重新遍历创建新的对象在hashmap中，不担心数据出错
    }
}
