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

public class Runner {
    String language = "";
    String code = "";
    int timeLimit;
    int memoryLimit;
    List<Template> languageTemplates = new ArrayList<>();
    String input = "";

    public Runner(String language, String code, int timeLimit, int memoryLimit, List<Template> languageTemplates, String input) {
        this.language = language;
        this.code = code;
        this.timeLimit = timeLimit;
        this.memoryLimit = memoryLimit;
        this.languageTemplates = languageTemplates;
        this.input = input;
    }

    public void run() {
        initialize();
        judgeAll();
        finish();
    }

    public void initialize() {

        //update testcase path
        FileUtil.del(PathConfig.path + "operate/tc");
        FileUtil.mkdir(PathConfig.path + "operate/tc");

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

        fw = new FileWriter(PathConfig.path + "sandbox/details.txt");
        fw.write(language + "\n" + timeLimit + "\n" + memoryLimit + "\n");
    }

    public void judgeAll() {
        FileWriter fw = new FileWriter(PathConfig.path + "sandbox/in.txt");
        fw.write(input);
        //run cmd here
        //
        try {
            System.out.println("run test");
            Runtime.getRuntime().exec("./a.out").waitFor();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        //
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

            Connector.sendTestResult(Result.AC(1, 1, output, timeCost, memoryCost));

        } else {
            switch (results.get(0)) {
                case "-1": Connector.sendTestResult(Result.TLE(1, 1, output)); break;
                case "-2": Connector.sendTestResult(Result.MLE(1, 1, output)); break;
                case "-3": Connector.sendTestResult(Result.RE(1, 1, output)); break;
                case "-4": Connector.sendTestResult(Result.CE(1, 1, "Compile Error\n" + output)); break;
            }
        }


    }

    public void finish() {

    }
}
