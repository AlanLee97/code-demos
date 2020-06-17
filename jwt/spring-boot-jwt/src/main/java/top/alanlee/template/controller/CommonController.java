package top.alanlee.template.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.alanlee.template.entity.dto.ApiJson;

@RestController
public class CommonController {

    @RequestMapping("/unlogin")
    public ApiJson unlogin(){
        return ApiJson.error("unlogin");
    }
}
