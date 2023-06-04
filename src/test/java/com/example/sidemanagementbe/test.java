package com.example.sidemanagementbe;

import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class test {
    @Qualifier("jasyptStringEncryptor")
    @Autowired
    StringEncryptor stringEncryptor;
    @Test
    public void encodeDatabaseData(){
        System.out.println("username 암호화:"+stringEncryptor.encrypt("root"));
        System.out.println("password 암호화:"+stringEncryptor.encrypt("root"));

    }
}
