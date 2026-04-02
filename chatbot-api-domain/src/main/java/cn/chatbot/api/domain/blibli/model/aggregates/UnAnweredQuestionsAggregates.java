package cn.chatbot.api.domain.blibli.model.aggregates;

import cn.chatbot.api.domain.blibli.model.res.RespData;

public class UnAnweredQuestionsAggregates {

    private String message;
    private RespData data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public RespData getData() {
        return data;
    }

    public void setData(RespData data) {
        this.data = data;
    }
}
