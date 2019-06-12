import com.alibaba.fastjson.JSON;
import com.yw56.javaservice.bean.JsonTreeBean;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @Classname TestJsonParse
 * @Description TODO
 * @Date 2019/6/12 9:40
 * @Created by djl20
 */
public class TestJsonParse {
    @Test
    public void testParse() {
        String json = getFileAsString("TestParseJson.json");
        ArrayList<JsonTreeBean> treeBeans = JsonTreeBean.parse(json);
        System.out.println(JSON.toJSON(treeBeans).toString());
    }

    public String getFileAsString(String fileName) {
        try {
            FileInputStream is = new FileInputStream(fileName);
            int available = is.available();
            byte[] buffer = new byte[available];
            int read = is.read(buffer);
            return new String(buffer, "utf-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
