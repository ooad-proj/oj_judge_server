package connector;

import cn.hutool.core.io.FileUtil;
import com.rabbitmq.client.*;
import configs.PathConfig;
import judger.JudgeDetail;
import utils.FileToString;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeoutException;

public class RequestCustomer {
    static RecvPacket packet = null;
    static Channel channel = null;

    public static void consume(String host, int port, ConnectorEntry entry) {
        try {
            Connection connection = MqUtil.getConnection(host, port);
            channel = connection.createChannel();

            DeliverCallback deliverCallback = (s, delivery) -> {
                byte[] bytes = delivery.getBody();
                System.out.println(new String(bytes));
                RecvPacket packet = RecvPacket.fromString(new String(bytes));

                if (!(packet.file == null || packet.file.equals(""))) {
                    FileToString.stringToFile(PathConfig.path + "connect/file.zip", packet.file);
                }

                RequestCustomer.packet = packet;

                try {
                    entry.run();
                    Connector.finish();
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                } catch (Exception e) {
                    e.printStackTrace();
                    Connector.error();
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                }
            };

            channel.basicConsume(MqUtil.REQUEST_QUEUE, false, deliverCallback, s -> {});

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }

    }

    public static void testSend(String host, int port) {
//        RecvPacket packet = new RecvPacket("avbkdj", "src/main/resources/tc.zip", Connector.getJudgeDetailTest());


        try {
            Connection connection = MqUtil.getConnection(host, port);
            Channel channel = connection.createChannel();
            channel.queueDeclare(MqUtil.REQUEST_QUEUE, false, false, false, null);
//            channel.basicPublish("", MqUtil.REQUEST_QUEUE, null, packet.toString().getBytes(StandardCharsets.UTF_8));
            channel.close();
//            connection.close();
            System.out.println("init request queue");


        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        testSend("120.24.252.217", 5672);
//        consume("localhost", 5672);
    }

}
