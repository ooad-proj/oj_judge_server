package connector;

import cn.hutool.core.io.FileUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import configs.PathConfig;
import judger.JudgeDetail;
import judger.Template;
import result.Result;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
        sendPacket(new SendPacket(0, getPacket().submitId, result));
    }

    public static void sendTestResult(Result result) {
        sendPacket(new SendPacket(2, getPacket().submitId, result));
    }

    protected static void finish() {
        sendPacket(new SendPacket(1, getPacket().submitId, null));
    }

    protected static void error() {
        sendPacket(new SendPacket(-1, getPacket().submitId, null));
    }

    public static void getFile(String path, String name) {
        if (localEnvironment) {
            System.out.println("Test file will be located in " + path + "/" + name);
        } else {
            FileUtil.copy(PathConfig.path + "connect/file.zip", path + "/" + name, true);
        }
    }

    public static RecvPacket getPacket() {
        if (localEnvironment) {
            RecvPacket packet = new RecvPacket("avbkdj", "src/main/resources/tc.zip", Connector.getJudgeDetailTest());
            return packet;
        } else {
            return RequestCustomer.packet;
        }
    }

    public static JudgeDetail getJudgeDetailTest() {
        String codeJava = FileUtil.readString("../../src/main/resources/Main.java", StandardCharsets.UTF_8);
        ArrayList<Template> templates = new ArrayList<>();
        Template javaTemplate = new Template();
        javaTemplate.setLanguage("python");
        javaTemplate.setCode(FileUtil.readString("../../src/main/resources/template.txt", StandardCharsets.UTF_8));
        templates.add(javaTemplate);

        judgeDetail = new JudgeDetail("python", codeJava, 2000, 128, templates);
        return judgeDetail;
    }

}
