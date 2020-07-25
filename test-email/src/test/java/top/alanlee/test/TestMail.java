package top.alanlee.test;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.alanlee.test.service.MailService;
import top.alanlee.test.util.CronUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMail {

    @Autowired
    MailService mailService;

    /**
     * 测试发送文本邮件
     */
    @Test
    public void sendmail() {
        mailService.sendSimpleMail("972767524@qq.com","主题：你好普通邮件","内容：第一封邮件");
    }

    @Test
    public void getCorn(){
        String date = "2020-03-18 12:30:00";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = simpleDateFormat.parse(date);
            String cron = CronUtil.getCron(parse);
            System.out.println(cron);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test(){
        Long timestamp = 1584544374L * 1000L;
        String formats = "yyyy-MM-dd HH:mm:ss";
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));

        System.out.println("转换后的时间：" + date);
    }

}
