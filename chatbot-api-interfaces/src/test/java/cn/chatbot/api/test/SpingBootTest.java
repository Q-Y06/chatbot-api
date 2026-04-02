package cn.chatbot.api.test;

import cn.chatbot.api.domain.ai.IZhipuAI;
import cn.chatbot.api.domain.ai.service.ZhipuAI;
import cn.chatbot.api.domain.blibli.IBlibliApi;
import cn.chatbot.api.domain.blibli.model.aggregates.UnAnweredQuestionsAggregates;
import cn.chatbot.api.domain.blibli.model.res.ReplyItem;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpingBootTest {

    private Logger logger = LoggerFactory.getLogger(SpringBootTest.class);

    @Value("${chatbot-api.oid}")
    private  String oid;

    @Value("${chatbot-api.cookie}")
    private  String cookie;

    @Value("${chatbot-api.wRid}")
    private  String wRid;

    @Value("${chatbot-api.wts}")
    private  String wts;

    @Value("${chatbot-api.apiKey}")
    private String apiKey;

    @Resource
    private IBlibliApi blibliApi;

    @Resource
    private IZhipuAI zhipuAI;


    @Test
    public void test_BlibliApi() throws IOException {
        UnAnweredQuestionsAggregates UnAnweredQuestionsAggregates = blibliApi.queryUnAnsweredQuestionsTopicId(oid, cookie,wRid, wts);
        logger.info("test end : {}", JSON.toJSONString(UnAnweredQuestionsAggregates));

        List<ReplyItem> topics = UnAnweredQuestionsAggregates.getData().getReplies();
        for (ReplyItem reply : topics) {
            Long rpid = reply.getRpid();
            String uname = reply.getMember().getUname();
            String text = reply.getContent().getMessage();
            logger.info("rpid: {} uname: {} text: {}", rpid, uname, text);
        }
    }

    @Test
    public void test_zhipuAI() throws IOException {
        String response = zhipuAI.dozhipu("柳智敏和张元英谁漂亮,必须选一个");
        logger.info("智谱AI回复：{}", response);
    }
}
