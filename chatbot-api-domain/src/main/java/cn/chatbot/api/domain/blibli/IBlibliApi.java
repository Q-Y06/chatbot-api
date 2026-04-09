package cn.chatbot.api.domain.blibli;

import cn.chatbot.api.domain.blibli.model.aggregates.UnAnweredQuestionsAggregates;

import java.io.IOException;

public interface IBlibliApi {

    UnAnweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String oid, String cookie, String wRid, String wts) throws IOException;

    boolean reply(String oid, String cookie, String rootId, String parentId, String replyMid, String replyUname, String text) throws IOException;
}
