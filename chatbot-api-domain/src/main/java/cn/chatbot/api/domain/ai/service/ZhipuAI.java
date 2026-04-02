package cn.chatbot.api.domain.ai.service;

import cn.chatbot.api.domain.ai.IZhipuAI;
import cn.chatbot.api.domain.ai.model.aggregates.AIAnswer;
import cn.chatbot.api.domain.ai.model.vo.Choices;
import cn.chatbot.api.domain.blibli.IBlibliApi;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class ZhipuAI implements IZhipuAI {

    private Logger logger = LoggerFactory.getLogger(ZhipuAI.class);

    @Value("${chatbot-api.apiKey}")
    private String apiKey;

    @Override
    public String dozhipu(String question) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://open.bigmodel.cn/api/paas/v4/chat/completions");
        post.addHeader("Content-Type","application/json");
        post.addHeader("Authorization","Bearer " + apiKey);

        String paramJson = "{"
                + "\"model\":\"glm-4.7\","
                + "\"messages\":[{\"role\":\"user\",\"content\":\" "+question +" \"}],"
                + "\"temperature\":0.7"
                + "}";
        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("application/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String JsonStr = EntityUtils.toString(response.getEntity());
            AIAnswer aiAnswer = JSON.parseObject(JsonStr, AIAnswer.class);
            StringBuilder answers = new StringBuilder();
            List<Choices> choices = aiAnswer.getChoices();
            for (Choices choice : choices) {
                answers.append(choice.getMessage().getContent());
            }
            return answers.toString();
        } else {
            throw new RuntimeException("api.zhipuai.com Err Code is " + response.getStatusLine().getStatusCode());
        }
    }

}
