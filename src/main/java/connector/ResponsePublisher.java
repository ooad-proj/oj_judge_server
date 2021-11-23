package connector;

import cn.hutool.core.io.FileUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import configs.PathConfig;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.concurrent.TimeoutException;

public class ResponsePublisher {
    static Channel channel = null;

    public static void init(String host, int port) {
        try {
            Connection connection = MqUtil.getConnection(host, port);
            channel = connection.createChannel();

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static void send(SendPacket packet) throws IOException {
        String message = packet.toString();
        System.out.println("[send] " + message);
        channel.basicPublish("", MqUtil.RESPONSE_QUEUE, null, message.getBytes(StandardCharsets.UTF_8));
    }


    public static void testClear(String host, int port) {
        try {
            Connection connection = MqUtil.getConnection(host, port);
            Channel channel = connection.createChannel();
            channel.queueDeclare(MqUtil.RESPONSE_QUEUE, false, false, false, null);
            channel.basicConsume(MqUtil.RESPONSE_QUEUE, true, (s, d) -> {}, s -> {});
            System.out.println("yes");
            channel.close();

        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        testClear("120.24.252.217", 5672);
    }


}
