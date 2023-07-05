package com.example.sidemanagementbe;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
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

    @Bean
    JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

}