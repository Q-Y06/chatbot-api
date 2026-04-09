package cn.chatbot.api.application.job;

import cn.chatbot.api.domain.ai.IZhipuAI;
import cn.chatbot.api.domain.blibli.IBlibliApi;
import cn.chatbot.api.domain.blibli.model.aggregates.UnAnweredQuestionsAggregates;
import cn.chatbot.api.domain.blibli.model.res.Member;
import cn.chatbot.api.domain.blibli.model.res.ReplyItem;
import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@EnableScheduling
@Configuration
public class Chatbotschedule {

    private final Logger logger = LoggerFactory.getLogger(Chatbotschedule.class);
    private final Set<String> processedReplyIds = ConcurrentHashMap.newKeySet();

    @Value("${chatbot-api.oid}")
    private String oid;

    @Value("${chatbot-api.cookie}")
    private String cookie;

    @Value("${chatbot-api.wRid}")
    private String wRid;

    @Value("${chatbot-api.wts}")
    private String wts;

    @Resource
    private IBlibliApi blibliApi;

    @Resource
    private IZhipuAI zhipuAI;

    @Scheduled(cron = "0 0/30 * * * ?")
    public void run() {
        try {
            GregorianCalendar calendar = new GregorianCalendar();
            int hour = calendar.get(GregorianCalendar.HOUR_OF_DAY);
            if (hour > 22 || hour < 7) {
                logger.info("当前时间不在工作时段，跳过本轮任务。hour={}", hour);
                return;
            }

            UnAnweredQuestionsAggregates aggregates = blibliApi.queryUnAnsweredQuestionsTopicId(oid, cookie, wRid, wts);
            logger.info("评论抓取结果：{}", JSON.toJSONString(aggregates));

            if (aggregates == null || aggregates.getData() == null || aggregates.getData().getReplies() == null) {
                logger.info("评论抓取结果为空，跳过本轮任务");
                return;
            }

            List<ReplyTask> tasks = buildReplyTasks(aggregates.getData().getReplies());
            if (tasks.isEmpty()) {
                logger.info("当前没有可回复的评论，跳过本轮任务");
                return;
            }

            int successCount = 0;
            int failCount = 0;

            for (ReplyTask task : tasks) {
                if (!processedReplyIds.add(task.targetReplyId)) {
                    logger.info("该评论已处理过，跳过。targetReplyId: {}", task.targetReplyId);
                    continue;
                }

                try {
                    String answer = sanitizeReply(zhipuAI.dozhipu(task.question), task.replyUname);
                    logger.info("AI answer generated. targetReplyId: {} answer: {}", task.targetReplyId, answer);

                    boolean status = blibliApi.reply(
                            oid,
                            cookie,
                            task.rootId,
                            task.parentId,
                            task.replyMid,
                            task.replyUname,
                            answer
                    );

                    logger.info("reply status: {} oid: {} rootId: {} parentId: {} question: {} answer: {}",
                            status,
                            oid,
                            task.rootId,
                            task.parentId,
                            task.question,
                            answer);

                    if (status) {
                        successCount++;
                    } else {
                        failCount++;
                        processedReplyIds.remove(task.targetReplyId);
                    }
                } catch (Exception e) {
                    failCount++;
                    processedReplyIds.remove(task.targetReplyId);
                    logger.error("回复评论失败。targetReplyId: {}", task.targetReplyId, e);
                }
            }

            logger.info("本轮任务结束。待回复评论数: {}，成功: {}，失败: {}", tasks.size(), successCount, failCount);
        } catch (Exception e) {
            logger.error("自动回复评论异常", e);
        }
    }

    private List<ReplyTask> buildReplyTasks(List<ReplyItem> replyItems) {
        List<ReplyTask> tasks = new ArrayList<>();
        if (replyItems == null || replyItems.isEmpty()) {
            return tasks;
        }

        for (ReplyItem topReply : replyItems) {
            if (topReply == null || topReply.getRpid() == null) {
                continue;
            }

            if (topReply.getReplies() != null && !topReply.getReplies().isEmpty()) {
                for (ReplyItem childReply : topReply.getReplies()) {
                    if (childReply == null || childReply.getRpid() == null) {
                        continue;
                    }
                    ReplyTask task = createChildReplyTask(topReply, childReply);
                    tasks.add(task);
                }
            } else {
                tasks.add(createTopReplyTask(topReply));
            }
        }

        return tasks;
    }

    private ReplyTask createTopReplyTask(ReplyItem topReply) {
        ReplyTask task = new ReplyTask();
        task.targetReplyId = String.valueOf(topReply.getRpid());
        task.rootId = String.valueOf(topReply.getRpid());
        task.parentId = task.rootId;
        task.question = topReply.getContent() != null ? topReply.getContent().getMessage() : "";
        task.replyMid = topReply.getMember() != null ? topReply.getMember().getMid() : null;
        task.replyUname = topReply.getMember() != null ? topReply.getMember().getUname() : null;
        logger.info("加入一级评论回复任务。topRpid: {}", topReply.getRpid());
        return task;
    }

    private ReplyTask createChildReplyTask(ReplyItem topReply, ReplyItem childReply) {
        ReplyTask task = new ReplyTask();
        task.targetReplyId = String.valueOf(childReply.getRpid());
        task.rootId = String.valueOf(topReply.getRpid());
        task.parentId = String.valueOf(childReply.getRpid());
        task.question = childReply.getContent() != null ? childReply.getContent().getMessage() : "";
        Member member = childReply.getMember();
        task.replyMid = member != null ? member.getMid() : null;
        task.replyUname = member != null ? member.getUname() : null;
        logger.info("加入子评论回复任务。topRpid: {} childRpid: {}", topReply.getRpid(), childReply.getRpid());
        return task;
    }

    private String sanitizeReply(String answer, String replyUname) {
        if (answer == null || answer.trim().isEmpty()) {
            return replyUname == null ? "我来帮你看看。" : "回复 @" + replyUname + "：我来帮你看看。";
        }

        String normalized = answer
                .replaceAll("(?m)^#+\\s*", "")
                .replace("**", "")
                .replace("```", "")
                .replace("\r", " ")
                .replace("\n", " ")
                .replaceAll("https?://\\S+", "")
                .replaceAll("[`*_>#-]", " ")
                .replaceAll("\\s{2,}", " ")
                .trim();

        if (normalized.length() > 220) {
            normalized = normalized.substring(0, 220) + "...";
        }

        if (replyUname != null && !replyUname.trim().isEmpty() && !normalized.startsWith("回复 @")) {
            return "回复 @" + replyUname + "：" + normalized;
        }
        return normalized;
    }

    private static class ReplyTask {
        private String targetReplyId;
        private String rootId;
        private String parentId;
        private String question;
        private String replyMid;
        private String replyUname;
    }
}
