import connector.Connector;
import connector.RequestCustomer;
import connector.ResponsePublisher;
import judger.JudgeDetail;
import judger.Judger;
import judger.Runner;

public class Main {

    public static void main(String[] args) {
        try {
            Thread.sleep(1000*15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        RequestCustomer.testSend("rabbitmq", 5672);
        ResponsePublisher.testClear("rabbitmq", 5672);
        System.out.println("Running success!");
        Connector.startUp("rabbitmq", 5672, Main::judge, false);
    }

    public static void judge() {
        System.out.println("[Info] Recognize judge request " + Connector.getPacket().getSubmitId());
        JudgeDetail jd = Connector.getPacket().getJudgeDetail();
        int type = Connector.getPacket().getType();
        if (type == 0) { //file
            Judger judger = new Judger(jd.language, jd.code, jd.timeLimit, jd.memoryLimit, jd.languageTemplates);
            judger.judge();
        } else {
            Runner runner = new Runner(jd.language, jd.code, jd.timeLimit, jd.memoryLimit, jd.languageTemplates, Connector.getPacket().getInput());
            runner.run();
        }

    }

}
