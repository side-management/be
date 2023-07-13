package com.example.sidemanagementbe;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SideManagementBeApplication {
    /*
     * test init commit
     * */
    public static void main(String[] args) {
        SpringApplication.run(SideManagementBeApplication.class, args);
    }

    /**
     * 서버 타임존 설정
     */
    @PostConstruct
    public void started() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }


}