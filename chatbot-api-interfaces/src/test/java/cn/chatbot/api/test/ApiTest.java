package cn.chatbot.api.test;

import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * @description 启动入口
 */
public class ApiTest {

    /**
     * 查询
     */
    @Test
    public void query_unanswered_questions() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpGet get = new HttpGet("https://api.bilibili.com/x/v2/reply/wbi/main?oid=47081899&type=12&mode=3&pagination_str=%7B%22offset%22:%22%22%7D&plat=1&seek_rpid=&web_location=1315875&w_rid=94fb0f14737e286b16b71da08ef10d69&wts=1774682774");


        get.addHeader("Cookie", "buvid3=C5E8E156-EFF8-61A7-FBDF-EF87C55C2F0B85699infoc; b_nut=1772352585; _uuid=C23B26103-BD87-101D4-94A4-1C10D77E410A6884858infoc; buvid_fp=19418955853200b0c2d85e2631407045; buvid4=57F1636F-4241-2516-C646-EB0845CFE03786516-026030116-9cMiC3JIQqfiQ3rJ7dbsgQ%3D%3D; DedeUserID=1964509525; DedeUserID__ckMd5=0e9d99f2283e1d2a; theme-tip-show=SHOWED; rpdid=|(um~ku||uRR0J'u~~kJlkYu); theme-avatar-tip-show=SHOWED; SESSDATA=7b9fbdb5%2C1789645572%2Ce831b%2A31CjD2fs1e1jJWe-s7P7A78AWZLqSDPl2N3erI-ebXq-5DnzZZ3cPpwQEKqyNn8ADUR5ISVnVVZkxfZXlpSVhPemplNlpnckFxNUVjZ1RRWDMyNy0tcDFmQ2lHNDY5TWJzVHF6QXNyV3BKMThRek9OVXktUFlicDczMWJMbGlLNzNMWFF1RkIyb3ZnIIEC; bili_jct=2494ba9bf13c4137757ac6b5e26e9707; bsource=search_bing; sid=7u51ce7c; CURRENT_FNVAL=4048; CURRENT_QUALITY=80; hit-dyn-v2=1; bili_ticket=eyJhbGciOiJIUzI1NiIsImtpZCI6InMwMyIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NzQ1MjcyNDYsImlhdCI6MTc3NDI2Nzk4NiwicGx0IjotMX0.1LwtIAMR2yY6n9U06utlEIPaFDSQ0YAlq10WLF1K0I4; bili_ticket_expires=1774527186; bp_t_offset_1964509525=1182962657653686272; home_feed_column=4; browser_resolution=821-958; b_lsid=0C72202E_19D1ACD15A5");

        get.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");


        CloseableHttpResponse response = httpClient.execute(get);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }

    @Test
    public void answer() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        HttpPost post = new HttpPost("https://api.bilibili.com/x/v2/reply/add?dm_img_list=[]&dm_img_str=V2ViR0wgMS4wIChPcGVuR0wgRVMgMi4wIENocm9taXVtKQ&dm_cover_img_str=QU5HTEUgKE5WSURJQSwgTlZJRElBIEdlRm9yY2UgUlRYIDQwNjAgTGFwdG9wIEdQVSAoMHgwMDAwMjhFMCkgRGlyZWN0M0QxMSB2c181XzAgcHNfNV8wLCBEM0QxMSlHb29nbGUgSW5jLiAoTlZJRElBKQ&dm_img_inter=%7B%22ds%22:[],%22wh%22:[3805,2445,75],%22of%22:[335,670,335]%7D&w_rid=d1d42e8ff72392f4526bc40da0724739&wts=1774687185");

        post.addHeader("Cookie", "buvid3=C5E8E156-EFF8-61A7-FBDF-EF87C55C2F0B85699infoc; b_nut=1772352585; _uuid=C23B26103-BD87-101D4-94A4-1C10D77E410A6884858infoc; buvid_fp=19418955853200b0c2d85e2631407045; buvid4=57F1636F-4241-2516-C646-EB0845CFE03786516-026030116-9cMiC3JIQqfiQ3rJ7dbsgQ%3D%3D; DedeUserID=1964509525; DedeUserID__ckMd5=0e9d99f2283e1d2a; theme-tip-show=SHOWED; rpdid=|(um~ku||uRR0J'u~~kJlkYu); theme-avatar-tip-show=SHOWED; CURRENT_FNVAL=4048; CURRENT_QUALITY=80; hit-dyn-v2=1; bp_t_offset_1964509525=1182962657653686272; home_feed_column=4; browser_resolution=821-958; bili_ticket=eyJhbGciOiJIUzI1NiIsImtpZCI6InMwMyIsInR5cCI6IkpXVCJ9.eyJleHAiOjE3NzQ5NDA3NTksImlhdCI6MTc3NDY4MTQ5OSwicGx0IjotMX0.4xu40a6sTzyHnWm8sYONCzhlBWHSbZbTcLuMXuRSSxs; bili_ticket_expires=1774940699; SESSDATA=b08d8533%2C1790233562%2C02ce9%2A31CjCx8-tYF0Ns63oB191ryO90i3ht4IRNf5PkThbYzdJx0FTKK3-i4EGCcX33Gg3l94ASVndsbDNWckpCX01zSmxLelZwWEtxMHpTeVhxQTh6dkQ4UFdCQ0I4UkRyYlFWUVQyR2JEaGxsQklFWFdOTUxtcHdpeEV6ZThJSkJlVlZtTTZaWFdwTjBRIIEC; bili_jct=181b52c4fe37f77aa74c9a55eeff87fd; sid=72bwpf7i; b_lsid=2276023B_19D33A5B2F0");
        post.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("plat", "1"));
        params.add(new BasicNameValuePair("oid", "47081899"));
        params.add(new BasicNameValuePair("type", "12"));
        params.add(new BasicNameValuePair("message", "帮我写一个排序"));
        params.add(new BasicNameValuePair("at_name_to_mid", "{}"));
        params.add(new BasicNameValuePair("gaia_source", "main_web"));
        params.add(new BasicNameValuePair("csrf", "181b52c4fe37f77aa74c9a55eeff87fd"));
        params.add(new BasicNameValuePair("statistics", "{\"appId\":100,\"platform\":5}"));
        params.add(new BasicNameValuePair("root", "296946165872"));
        params.add(new BasicNameValuePair("parent", "296946165872"));

        post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        CloseableHttpResponse response = httpClient.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }
    @Test
    public void test_chatZhiPu() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("https://open.bigmodel.cn/api/paas/v4/chat/completions");
        post.addHeader("Content-Type","application/json");
        post.addHeader("Authorization","Bearer d96115fde7174ef69fdea52e6ce8e683.Cr0pFCFGbuHq5XYz");

        String paramJson = "{"
                + "\"model\":\"glm-4.7\","
                + "\"messages\":[{\"role\":\"user\",\"content\":\"S帮我写一个java的排序\"}],"
                + "\"temperature\":0.7"
                + "}";
        StringEntity stringEntity = new StringEntity(paramJson, ContentType.create("application/json", "UTF-8"));
        post.setEntity(stringEntity);

        CloseableHttpResponse response = httpClient.execute(post);

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            String res = EntityUtils.toString(response.getEntity());
            System.out.println(res);
        } else {
            System.out.println(response.getStatusLine().getStatusCode());
        }
    }


}
