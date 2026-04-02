package cn.chatbot.api.domain.blibli.model.req;

/**
 * 请求问答信息
 */
public class AnswerReq {
    private ReplyReq req_data;

    public AnswerReq(ReplyReq req_data) {
        this.req_data = req_data;
    }

    public ReplyReq getReq_data() {
        return req_data;
    }

    public void setReq_data(ReplyReq req_data) {
        this.req_data = req_data;
    }
}
