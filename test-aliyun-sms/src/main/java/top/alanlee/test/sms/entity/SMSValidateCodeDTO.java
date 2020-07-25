package top.alanlee.test.sms.entity;

import java.io.Serializable;

public class SMSValidateCodeDTO implements Serializable {
    private Integer code;

    public SMSValidateCodeDTO() {
    }

    public SMSValidateCodeDTO(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "SMSValidateCodeDTO{" +
                "code=" + code +
                '}';
    }
}
