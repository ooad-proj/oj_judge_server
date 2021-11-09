package connector;

import cn.hutool.core.io.FileUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import configs.PathConfig;
import judger.JudgeDetail;
import result.Result;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Connector {
    static Channel channel;
    static boolean localEnvironment = false;
    static JudgeDetail judgeDetail;

    public static void startUp(String host, int port, ConnectorEntry entry, boolean localEnvironment) {
        Connector.localEnvironment = localEnvironment;
        if (localEnvironment) {
            entry.run();
        } else {
            ResponsePublisher.init(host, port);
            RequestCustomer.consume(host, port, entry);

        }
    }

    protected static void sendPacket(SendPacket packet) {
        if (localEnvironment) {
            System.out.println(packet.toString());
        } else {
            try {
                ResponsePublisher.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sendResult(Result result) {
        sendPacket(new SendPacket(0, result));
    }

    protected static void finish() {
        sendPacket(new SendPacket(1, null));
    }

    public static void getFile(String path, String name) {
        if (localEnvironment) {
            System.out.println("Test file will be located in " + path + "/" + name);
        } else {
            FileUtil.copy(PathConfig.path + "connect/file", path + "/" + name, true);
        }
    }

    public static RecvPacket getPacket() {
        return RequestCustomer.packet;
    }

    public static JudgeDetail getJudgeDetailTest() {
        String codeJava = "import java.util.Scanner;\n" +
                "\n" +
                "public class Main {\n" +
                "    \n" +
                "    public static void main(String[] args) {\n" +
                "        Scanner s = new Scanner(System.in);\n" +
                "        int len = s.nextInt();\n" +
                "        for (i = 1; i <= len; i++) {\n" +
                "            System.out.println(s.nextInt() + s.nextInt());\n" +
                "        }\n" +
                "    }\n" +
                "}";
        judgeDetail = new JudgeDetail("java", codeJava, 2000, 128, null);
        return judgeDetail;
    }

}
