package connector;

import com.google.gson.Gson;
import result.Result;

public class SendPacket {
    //type == 0: standard
    //type == 1: end
    int type;
    Result result;

    public SendPacket(int type, Result result) {
        this.type = type;
        this.result = result;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
