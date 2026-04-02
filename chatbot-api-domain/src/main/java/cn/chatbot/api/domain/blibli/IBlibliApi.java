package cn.chatbot.api.domain.blibli;

import cn.chatbot.api.domain.blibli.model.aggregates.UnAnweredQuestionsAggregates;
import org.slf4j.Logger;

import java.io.IOException;

public interface IBlibliApi {


//查评论区
    UnAnweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String oid, String cookie, String wRid, String wts) throws IOException;
//发评论
    boolean reply(String oid, String cookie , String replyId,String text) throws IOException;


}
