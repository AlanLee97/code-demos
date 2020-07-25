package top.alanlee.test.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import top.alanlee.test.dto.MailDTO;
import top.alanlee.test.service.impl.MailServiceImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EmailJob implements Job {
    @Autowired
    MailServiceImpl mailService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        System.out.println("打印接收的数据");
        System.out.println(jobDataMap.get("mailDTO").toString());
        MailDTO mailDTO = (MailDTO) jobDataMap.get("mailDTO");

        Date date = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("现在的时间是：" + sf.format(date));
        //具体的业务逻辑
        System.out.println("开始任务");
        mailService.sendSimpleMail(mailDTO.getTo(), mailDTO.getSubject(), mailDTO.getSubject());
    }
}
