package cn.hyperchain.sdk.response.config;

import cn.hyperchain.sdk.response.Response;
import com.google.gson.annotations.Expose;

public class ConfigResponse extends Response {

    @Expose
    private String result;

    public String getConfig() {
        return result;
    }

    @Override
    public String toString() {
        return "ConfigResponse{" +
                "result='" + result + '\'' +
                ", jsonrpc='" + jsonrpc + '\'' +
                ", id='" + id + '\'' +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", namespace='" + namespace + '\'' +
                '}';
    }
}
