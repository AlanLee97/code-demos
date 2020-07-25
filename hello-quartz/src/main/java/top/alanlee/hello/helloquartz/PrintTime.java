package top.alanlee.hello.helloquartz;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PrintTime {

    @Scheduled(cron = "0/10 * * * * ? ")
    public void hello(){
        System.out.println(
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date()));
    }
}
