package cn.chatbot.api.domain.blibli.Service;

import cn.chatbot.api.domain.blibli.IBlibliApi;
import cn.chatbot.api.domain.blibli.model.aggregates.UnAnweredQuestionsAggregates;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class BlibliApi implements IBlibliApi {

    private static final Pattern BILI_JCT_PATTERN = Pattern.compile("bili_jct=([^;]+)");
    private final Logger logger = LoggerFactory.getLogger(BlibliApi.class);

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
            logger.info("B站评论拉取。oid: {} jsonStr: {}", oid, jsonStr);
            return JSON.parseObject(jsonStr, UnAnweredQuestionsAggregates.class);
        }
        throw new RuntimeException("query bilibili reply list fail, statusCode=" + response.getStatusLine().getStatusCode());
    }

    @Override
    public boolean reply(String oid, String cookie, String rootId, String parentId, String replyMid, String replyUname, String text) throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost("https://api.bilibili.com/x/v2/reply/add");
        post.addHeader("Cookie", cookie);
        post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        post.addHeader("Referer", "https://www.bilibili.com/");
        post.addHeader("Origin", "https://www.bilibili.com");
        post.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/146.0.0.0 Safari/537.36 Edg/146.0.0.0");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("plat", "1"));
        params.add(new BasicNameValuePair("oid", oid));
        params.add(new BasicNameValuePair("type", "12"));
        params.add(new BasicNameValuePair("message", text));
        params.add(new BasicNameValuePair("at_name_to_mid", buildAtNameToMid(rootId, parentId, replyMid, replyUname)));
        params.add(new BasicNameValuePair("gaia_source", "main_web"));
        params.add(new BasicNameValuePair("csrf", extractCsrf(cookie)));
        params.add(new BasicNameValuePair("statistics", "{\"appId\":100,\"platform\":5}"));

        if (rootId != null && !rootId.isEmpty()) {
            params.add(new BasicNameValuePair("root", rootId));
        }
        if (parentId != null && !parentId.isEmpty()) {
            params.add(new BasicNameValuePair("parent", parentId));
        }

        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
            logger.info("B站回复评论结果。oid: {} rootId: {} parentId: {} jsonStr: {}", oid, rootId, parentId, jsonStr);
            ReplyAddRes replyRes = JSON.parseObject(jsonStr, ReplyAddRes.class);
            return replyRes != null
                    && replyRes.getCode() != null
                    && replyRes.getCode() == 0
                    && replyRes.getData() != null
                    && replyRes.getData().isSucceed();
        }
        throw new RuntimeException("answer Err Code is " + response.getStatusLine().getStatusCode());
    }

    private String buildAtNameToMid(String rootId, String parentId, String replyMid, String replyUname) {
        if (rootId != null && rootId.equals(parentId)) {
            return "{}";
        }
        if (replyMid == null || replyMid.trim().isEmpty() || replyUname == null || replyUname.trim().isEmpty()) {
            return "{}";
        }
        return "{\"" + replyUname.replace("\"", "\\\"") + "\":" + replyMid + "}";
    }

    private String extractCsrf(String cookie) {
        Matcher matcher = BILI_JCT_PATTERN.matcher(cookie);
        if (matcher.find()) {
            return matcher.group(1);
        }
        throw new IllegalArgumentException("cookie 中未找到 bili_jct，无法提取 csrf");
    }
}
