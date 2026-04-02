package cn.chatbot.api.domain.blibli.model.res;

public class ReplyAddRes {
    private Integer code;
    private String message;
    private Integer ttl;
    private ReplyAddData data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getTtl() {
        return ttl;
    }

    public void setTtl(Integer ttl) {
        this.ttl = ttl;
    }

    public ReplyAddData getData() {
        return data;
    }

    public void setData(ReplyAddData data) {
        this.data = data;
    }
}
