import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TestBenchFile {

    static class Me {
        byte[] sth;
    }


    public static void main(String[] args) throws IOException {
        BufferedInputStream is = FileUtil.getInputStream("../../src/main/resources/tc.zip");
        String en = Base64.getEncoder().encodeToString(IoUtil.readBytes(is));
        byte[] b = en.getBytes(StandardCharsets.UTF_8);
        en = new String(b);
        byte[] bytes = Base64.getDecoder().decode(en);
        BufferedOutputStream os = FileUtil.getOutputStream("../../src/main/resources/tc2.zip");
        os.write(bytes);
        os.flush();

//        String s = "12";
//        byte[] b = s.getBytes(StandardCharsets.UTF_8);
//        Me me = new Me();
//        me.sth = b;
//        Gson gson = new Gson();
//        System.out.println(gson.toJson(me));
    }
}
