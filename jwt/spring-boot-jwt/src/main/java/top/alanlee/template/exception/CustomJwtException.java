package top.alanlee.template.exception;

import top.alanlee.template.util.ResultCode;

import java.text.MessageFormat;

public class CustomJwtException extends RuntimeException{
    //错误代码
    ResultCode resultCode;

    public CustomJwtException(ResultCode resultCode){
        super(resultCode.message());
        this.resultCode = resultCode;
    }

    public CustomJwtException(ResultCode resultCode, Object... args){
        super(resultCode.message());
        String message = MessageFormat.format(resultCode.message(), args);
        resultCode.setMessage(message);
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode(){
        return resultCode;
    }

}
