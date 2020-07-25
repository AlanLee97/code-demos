package top.alanlee.test.sms.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;

public class SMSUtils {
    private static String accessKeyId = "填写真实的accessKeyId";
    private static String secret = "填写真实的secret";
    private static String signName = "熊猫约拍";
    private static String validateCodeTemplateCode = "SMS_187226224";
    private static String noticeTemplateCode = "SMS_187261136";

    private static DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, secret);
    private static IAcsClient client = new DefaultAcsClient(profile);


    public static boolean sendSMS(String type, String phoneNumbers, String jsonStringTemplateParam){
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("SignName", signName);
        if ("validateCode".equals(type)){
            request.putQueryParameter("TemplateCode", validateCodeTemplateCode);
        }else if ("notice".equals(type)){
            request.putQueryParameter("TemplateCode", noticeTemplateCode);
        }else {
            throw new NullPointerException("TemplateCode 不正确");
        }
        request.putQueryParameter("TemplateParam", jsonStringTemplateParam);
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            boolean ok = response.getData().contains("OK");
            System.out.println("发送短信是否成功：" + ok);
            return ok;
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }


}
