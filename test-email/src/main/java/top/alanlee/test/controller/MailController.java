package top.alanlee.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.alanlee.test.service.impl.MailServiceImpl;

@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    MailServiceImpl mailService;

    @PostMapping("/simple")
    public String sendSimpleMail(String to, String subject, String content){
        mailService.sendSimpleMail(to, subject, content);
        return "ok";
    }

}
