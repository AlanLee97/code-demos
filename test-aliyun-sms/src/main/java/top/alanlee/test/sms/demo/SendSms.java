package top.alanlee.test.sms.demo;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import top.alanlee.test.sms.entity.SMSContentDTO;
import top.alanlee.test.sms.entity.SMSValidateCodeDTO;
import top.alanlee.test.sms.util.SMSUtils;

public class SendSms {

    public static void main(String[] args) {
//        SMSValidateCodeDTO smsValidateCodeDTO = new SMSValidateCodeDTO(9999);
//        String jsonString = JSONObject.toJSONString(smsValidateCodeDTO);

        SMSContentDTO smsContentDTO = new SMSContentDTO("Alan", "测试", "4");
        String jsonString = JSONObject.toJSONString(smsContentDTO);
        boolean b = SMSUtils.sendSMS("notice", "15622282904", jsonString);

        if (b){
            System.out.println("发送成功");
        }else {
            System.out.println("发送失败");
        }
    }

    @Test
    public void testNum(){
        String n = String.valueOf(Math.floor(Math.random() * 1000000));
        String code = n.substring(0, 4);
        System.out.println("code = " + code);

    }
}