package cn.chatbot.api.domain.ai;

import java.io.IOException;

public interface IZhipuAI {

    String dozhipu(String question) throws IOException;


}
