package top.alanlee.demo.dubbo.provider.user;

import org.apache.dubbo.container.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoDubboProviderUserApplication {
    public static void main(String[] args) {
        SpringApplication.run(DemoDubboProviderUserApplication.class, args);
        Main.main(args);
    }
}
