

import java.io.*;
import java.util.Arrays;

public class SandBoxTest {

    //assume sandbox is in the below folder "sandbox"
    public static void main(String[] args) throws IOException, InterruptedException {
        Runtime.getRuntime().exec("").waitFor();
        String[] output = readToString("out.txt").split("\n");
        String[] details = readToString("details.txt").split("\n");
        System.out.println(Arrays.toString(output));
        System.out.println(Arrays.toString(details));

    }

    public static String readToString(String fileName) {
        String encoding = "UTF-8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }

    public static void saveStringTOFile(String fileName, String content){
        FileWriter writer=null;
        try {
            writer = new FileWriter(new File(fileName));
            writer.write(content);
        } catch (IOException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                // TODO 自动生成的 catch 块
                e.printStackTrace();
            }
        }
        System.out.println("写入成功！！！");
    }
}
