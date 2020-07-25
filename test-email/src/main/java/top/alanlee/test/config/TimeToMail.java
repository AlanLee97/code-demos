package top.alanlee.test.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.alanlee.test.service.impl.MailServiceImpl;
import top.alanlee.test.util.CronUtil;

@Component
public class TimeToMail {

    @Autowired
    MailServiceImpl mailService;

    final String date = CronUtil.getStringDateToCron("2020-03-18 12:00:00");

    final String CRON_DATE = date;

//    @Scheduled(cron = "")
    public void sendmail() {
        mailService.sendSimpleMail("972767524@qq.com","主题：你好普通邮件","内容：第一封邮件");
    }
}
