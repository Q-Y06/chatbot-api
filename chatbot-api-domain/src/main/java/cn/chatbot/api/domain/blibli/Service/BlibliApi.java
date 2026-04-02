package cn.chatbot.api.domain.blibli.Service;

import cn.chatbot.api.domain.blibli.IBlibliApi;
import cn.chatbot.api.domain.blibli.model.aggregates.UnAnweredQuestionsAggregates;
import cn.chatbot.api.domain.blibli.model.req.ReplyReq;
import cn.chatbot.api.domain.blibli.model.res.ReplyAddData;
import cn.chatbot.api.domain.blibli.model.res.ReplyAddRes;
import com.alibaba.fastjson.JSON;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Service
public class BlibliApi implements IBlibliApi {

    private Logger logger = LoggerFactory.getLogger(BlibliApi.class);

    @Override
    public UnAnweredQuestionsAggregates queryUnAnsweredQuestionsTopicId(String oid, String cookie, String wRid, String wts) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        String url = "https://api.bilibili.com/x/v2/reply/wbi/main"
                + "?oid=" + oid
                + "&type=12"
                + "&mode=3"
                + "&pagination_str=%7B%22offset%22:%22%22%7D"
                + "&plat=1"
                + "&seek_rpid="
                + "&web_location=1315875"
                + "&w_rid=" + wRid
                + "&wts=" + wts;

        HttpGet get = new HttpGet(url);

        get.addHeader("Cookie", cookie);

        get.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        get.addHeader("Referer", "https://www.bilibili.com/");

        get.addHeader("Origin", "https://www.bilibili.com");

        get.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0");


        CloseableHttpResponse response = httpClient.execute(get);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");

            logger.info("B站评论拉取。oid: {}  jsonStr: {}", oid, jsonStr);

            return JSON.parseObject(jsonStr, UnAnweredQuestionsAggregates.class);
        } else {
            throw new RuntimeException("query bilibili reply list fail, statusCode=" + response.getStatusLine().getStatusCode());
        }

    }

    @Override
    public boolean reply(String oid, String cookie, String replyId, String text) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        String url = "https://api.bilibili.com/x/v2/reply/add"
                + "?dm_img_list=[]"
                + "&dm_img_str=V2ViR0wgMS4wIChPcGVuR0wgRVMgMi4wIENocm9taXVtKQ"
                + "&dm_cover_img_str=QU5HTEUgKE5WSURJQSwgTlZJRElBIEdlRm9yY2UgUlRYIDQwNjAgTGFwdG9wIEdQVSAoMHgwMDAwMjhFMCkgRGlyZWN0M0QxMSB2c181XzAgcHNfNV8wLCBEM0QxMSlHb29nbGUgSW5jLiAoTlZJRElBKQ"
                + "&dm_img_inter=%7B%22ds%22:[],%22wh%22:[3793,2441,71],%22of%22:[1171,1742,271]%7D"
                + "&w_rid=6e53e060ffd3b0172a9e62999564d5da"
                + "&wts=1774956281"
                + "&b_wet=8zi6Rop9f7ohSvoKpbEc9Bw3wBcOK2e10DzH3EIh7xI9vKwcycNGQScL5yMt%2B57I0ESjkGLfqDGbxWQOWZW%2FqvP8eTMZNK5Gll%2FFFMMkS8Jdkg7Mv82hMAWN2Cq1Cw%2FMNPHu36plAh9Dl7ds%2B86leKiGSjTysjrDe%2BzlwhzAfsQ%3D.SAJPMsrksHyjOZipXEaw3nqOw%2BVw%2BLBrY9OeTivQjvDhBgiX0DIDbYAVP3kDoDwQWVhNPzLf5465BXAl%2FIfekMPC%2BEIaFB9FOnY0gSSkZnd3qDhKY2xjqIrCy%2FVrH9sPub8%2Boa29vrVFfKu7ZUpApF%2F56EaNvL%2FwIYW%2BdFZhJD4LJIswiCBp0Zhs3V%2Bu6X62BE2iiEk87%2Foc5d%2BVM%2BtWfsp9ADkdWO98ujuEiu0jDte7q33JfSqObYnX%2Fq5tmCRTVw7shcNFqIS4HR5Qli8gqeLy60zSxkN7aIkHm6fjHbSkysP3Kb%2BjYu%2FYhAKxCh6USaFSQfSqsVvP2R6tRIO1c5NT4WFBf2xQeICXV2xG0seeDh%2FRKpNmLYUEFH1s0WJfBfeyRk%2B%2FzMGXf2ydz3uR4QJE%2Bim7PZl3OYxynR0XKyNOXBs%2BGyd7WVSVKyPWq2JNRt8vQQ0r%2BPIqGRmh1yn30QkCe2i3b6199xlynTgD0FYvsNONf884Nubd5e2xDvLP3USd6mu5S9Y5Lgnpe0YDJ7svk%2FHEsGybrjGDdv1ElnbSWrmYVyoBlwyN2qCDwvTtouAIW%2B8eqjkDYd6Dcr2CEZtjnBi3e4kUWWPbg0nLCI94fbZoAI%2BJUw3pP5Wabo4y8coNu7MoijfLbPB47M0Zjkaf6TCjKTJg1GYySobirf4%3D.v1";


        HttpPost post = new HttpPost(url);

        post.addHeader("Cookie", "buvid3=C5E8E156-EFF8-61A7-FBDF-EF87C55C2F0B85699infoc; b_nut=1772352585; _uuid=C23B26103-BD87-101D4-94A4-1C10D77E410A6884858infoc; buvid_fp=19418955853200b0c2d85e2631407045; buvid4=57F1636F-4241-2516-C646-EB0845CFE03786516-026030116-9cMiC3JIQqfiQ3rJ7dbsgQ%3D%3D; DedeUserID=1964509525; DedeUserID__ckMd5=0e9d99f2283e1d2a; theme-tip-show=SHOWED; rpdid=|(um~ku||uRR0J'u~~kJlkYu); theme-avatar-tip-show=SHOWED; CURRENT_FNVAL=4048; CURRENT_QUALITY=80; hit-dyn-v2=1; bp_t_offset_1964509525=1182962657653686272; home_feed_column=4; browser_resolution=821-958; bili_ticket=eyJhbGciOiJIUzI1NiIsImtpZCI6InMwMyIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NzQ5NDA3NTksImlhdCI6MTc3NDY4MTQ5OSwicGx0IjotMX0.4xu40a6sTzyHnWm8sYONCzhlBWHSbZbTcLuMXuRSSxs; bili_ticket_expires=1774940699; SESSDATA=b08d8533%2C1790233562%2C02ce9%2A31CjCx8-tYF0Ns63oB191ryO90i3ht4IRNf5PkThbYzdJx0FTKK3-i4EGCcX33Gg3l94ASVndsbDNWckpCX01zSmxLelZwWEtxMHpTeVhxQTh6dkQ4UFdCQ0I4UkRyYlFWUVQyR2JEaGxsQklFWFdOTUxtcHdpeEV6ZThJSkJlVlZtTTZaWFdwTjBRIIEC; bili_jct=181b52c4fe37f77aa74c9a55eeff87fd; sid=72bwpf7i; b_lsid=2276023B_19D33A5B2F0");

        post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        post.addHeader("Referer", "https://www.bilibili.com/");

        post.addHeader("Origin", "https://www.bilibili.com");

        post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0");
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("plat", "1"));
        params.add(new BasicNameValuePair("oid", oid));
        params.add(new BasicNameValuePair("type", "12"));
        params.add(new BasicNameValuePair("message", text));
        params.add(new BasicNameValuePair("at_name_to_mid", "{}"));
        params.add(new BasicNameValuePair("gaia_source", "main_web"));
        params.add(new BasicNameValuePair("csrf", "181b52c4fe37f77aa74c9a55eeff87fd"));
        params.add(new BasicNameValuePair("statistics", "{\"appId\":100,\"platform\":5}"));

        //查看是否有一级评论
        if (replyId != null && !replyId.isEmpty()) {
            params.add(new BasicNameValuePair("root", replyId));
            params.add(new BasicNameValuePair("parent", replyId));
        }


        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.info("B站回复评论结果。oid: {} root/parent: {} jsonStr: {}", oid, replyId, jsonStr);
            ReplyAddData replyReq = JSON.parseObject(jsonStr, ReplyAddData.class);
            return replyReq.isSucceedded();
        } else {
            throw new RuntimeException("answer Err Code is " + response.getStatusLine().getStatusCode());
        }
    }
}
