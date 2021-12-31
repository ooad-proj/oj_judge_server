package judger;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import configs.PathConfig;
import connector.Connector;
import result.Result;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Judger {
    String language = "";
    String code = "";
    int timeLimit;
    int memoryLimit;
    List<Template> languageTemplates = new ArrayList<>();

    public Judger(String language, String code, int timeLimit, int memoryLimit, List<Template> languageTemplates) {
        this.language = language;
        this.code = code;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.languageTemplates = languageTemplates;
    }

    public void judge() {
        initialize();
        judgeAll();
        finish();
    }

    public void initialize() {
        //update testcase path
        FileUtil.del(PathConfig.path + "operate/tc");
        FileUtil.del(PathConfig.path + "operate/tc.zip");
        Connector.getFile(PathConfig.path + "operate", "tc.zip");
        ZipUtil.unzip(PathConfig.path + "operate/tc.zip", PathConfig.path + "operate/tc");

        //replace template
        TemplateReplacer templateReplacer = new TemplateReplacer(languageTemplates, code, language);
        String replacedCode = templateReplacer.replace();

        //write code to run folder
        FileUtil.del(PathConfig.path + "sandbox/Main.java");
        FileUtil.del(PathConfig.path + "sandbox/Main.class");
        FileUtil.del(PathConfig.path + "sandbox/Main.py");
        FileUtil.del(PathConfig.path + "sandbox/__pycache__");
        FileWriter fw = new FileWriter(PathConfig.path + "sandbox/Main." + (language.equals("java") ? "java" : "py"));
        fw.write(replacedCode);

        System.out.println("------------------------------------replacedCode:\n");
        System.out.println(replacedCode);

        fw = new FileWriter(PathConfig.path + "sandbox/details.txt");
        fw.write(language + "\n" + timeLimit + "\n" + memoryLimit + "\n");
    }

    public void judgeAll() {
//        FileReader fr0 = new FileReader(PathConfig.path + "sandbox/error.txt");
//        List<String> results0 = fr0.readLines();
//        for (String i : results0) System.out.println(i);
//        FileUtil.del(PathConfig.path + "sandbox/error.txt");
//        FileUtil.del(PathConfig.path + "sandbox/error.txt");
//        try {
//            new File(PathConfig.path + "sandbox/error.txt").createNewFile();
//            new File(PathConfig.path + "sandbox/error.txt").createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        fr0 = new FileReader(PathConfig.path + "sandbox/error.txt");
//        results0 = fr0.readLines();
//        System.out.println("nmsl----------------");
//        for (String i : results0) System.out.println(i);

        int totalAmount = FileUtil.ls(PathConfig.path + "operate/tc").length / 2;
        for (int i = 1; i <= totalAmount; i++) {
            FileUtil.copy(PathConfig.path + "operate/tc/" + i + ".in", PathConfig.path + "sandbox/in.txt", true);
            //run cmd here
            //
            try {
                System.out.println("run judge" + i);
                Runtime.getRuntime().exec("./a.out").waitFor();
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            //
            FileReader fr = new FileReader(PathConfig.path + "sandbox/results.txt");
            List<String> results = fr.readLines();
            fr = new FileReader(PathConfig.path + "sandbox/out.txt");
            String output = fr.readString();
            if (output.length() > 1000) {
                output = output.substring(0,1000) + "\n...\n";
            }

            if (results.get(0).equals("0")) {
                int timeCost = Integer.parseInt(results.get(1));
                int memoryCost = Integer.parseInt(results.get(2)) / 1024 / 1024;

                fr = new FileReader(PathConfig.path + "operate/tc/"+ i +".in");
                String stdin = fr.readString();
                fr = new FileReader(PathConfig.path + "operate/tc/"+ i +".out");
                String stdout = fr.readString();
                boolean isSame = StrUtil.compare(stdout.replace("\r\n", "\n").trim(), output.replace("\r\n", "\n").trim(), false) == 0;

                if (isSame) {
                    Connector.sendResult(Result.AC(i, totalAmount, "Test case:\n" + stdin + "\nStandard Output:\n" + output, timeCost, memoryCost));
                } else {
                    String shownDetail = "Test case:\n" + stdin + "\nStandard Output:\n" + stdout + "\nYour Output:\n" + output;
                    Connector.sendResult(Result.WA(i, totalAmount, shownDetail, timeCost, memoryCost));
                }

            } else {
                switch (results.get(0)) {
                    case "-1": Connector.sendResult(Result.TLE(i, totalAmount, output)); break;
                    case "-2": Connector.sendResult(Result.MLE(i, totalAmount, output)); break;
                    case "-3": Connector.sendResult(Result.RE(i, totalAmount, output)); break;
                    case "-4": Connector.sendResult(Result.CE(i, totalAmount, "Compile Error\n" + output)); break;
                }
                if (results.get(0).equals("-4")) break;
            }
        }

    }

    public void finish() {

    }
}
