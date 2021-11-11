package connector;

import cn.hutool.core.util.RuntimeUtil;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException, InterruptedException {
//        RuntimeUtil.exec("java C:/Users/13053/Desktop/code/java/oj_judge_server/usr/local/judge-server/sandbox/Main.java 2< C:/Users/13053/Desktop/code/java/oj_judge_server/usr/local/judge-server/sandbox/in.txt 2> C:/Users/13053/Desktop/code/java/oj_judge_server/usr/local/judge-server/sandbox/out.txt");
//        Runtime.getRuntime().exec("java C:/Users/13053/Desktop/code/java/oj_judge_server/usr/local/judge-server/sandbox/Main.java 2< C:/Users/13053/Desktop/code/java/oj_judge_server/usr/local/judge-server/sandbox/in.txt 2> C:/Users/13053/Desktop/code/java/oj_judge_server/usr/local/judge-server/sandbox/out.txt");
        String [] command = {"cmd" , "/C" ,"java C:/Users/13053/Desktop/code/java/oj_judge_server/usr/local/judge-server/sandbox/Main.java 2< C:/Users/13053/Desktop/code/java/oj_judge_server/usr/local/judge-server/sandbox/in.txt 2> C:/Users/13053/Desktop/code/java/oj_judge_server/usr/local/judge-server/sandbox/out.txt"};
        Runtime.getRuntime().exec(command);

    }
}
