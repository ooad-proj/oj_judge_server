package connector;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import com.google.gson.Gson;
import judger.JudgeDetail;
import utils.FileToString;

import java.io.BufferedInputStream;
import java.io.File;
import java.util.Base64;

public class RecvPacket {
    //type == 0: from file
    // type == 1: from input
    int type;

    public String getSubmitId() {
        return submitId;
    }

    String submitId;
    String file;
    String input;
    JudgeDetail judgeDetail;

    public JudgeDetail getJudgeDetail() {
        return judgeDetail;
    }

    public int getType() {
        return type;
    }

    public String getInput() {
        return input;
    }

    public RecvPacket(String submitId, int type, String file, String input, JudgeDetail judgeDetail) {
        this.submitId = submitId;
        this.file = file;
        this.judgeDetail = judgeDetail;
        this.input = input;
        this.type = type;
    }

    public RecvPacket(String submitId, String filePath, JudgeDetail judgeDetail) {
        this.submitId = submitId;
        this.file = FileToString.fileToString(filePath);
        this.judgeDetail = judgeDetail;
        this.type = 0;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static RecvPacket fromString(String str) {
        Gson gson = new Gson();
        return gson.fromJson(str, RecvPacket.class);
    }
}
