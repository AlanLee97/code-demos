package top.alanlee.test.main;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.alanlee.test.sms.entity.SMSContentDTO;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestSMS {

    @Test
    public void testJson(){
        SMSContentDTO smsContentDTO = new SMSContentDTO("Alan", "Test", "4");
        Object o = JSONObject.toJSON(smsContentDTO);
        System.out.println(o);
    }

}
