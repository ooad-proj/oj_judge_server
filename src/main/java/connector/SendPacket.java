package connector;

import com.google.gson.Gson;
import result.Result;

public class SendPacket {
    //type == 0: standard
    //type == 2: testcase test
    //type == 1: end
    int type;
    String submitId;
    Result result;

    public SendPacket(int type, String submitId, Result result) {
        this.submitId = submitId;
        this.type = type;
        this.result = result;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
