import connector.Connector;
import judger.JudgeDetail;
import judger.Judger;
import judger.Runner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Running success!");
        Connector.startUp("120.24.252.217", 5672, Main::judge, false);
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
