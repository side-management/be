package com.example.sidemanagementbe.web.jasypt.config;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

/**
 * bean: 3번 항목에서 만드는 Jasypt Config 파일에서 등록하는 빈의 이름을 적는다. algorithm: 암/복호화에 사용되는 알고리즘을 말한다. 알고리즘을 선택하는 상황은 많지 않아 예시로 가장
 * 많이 언급된 알고리즘을 적었다. pool-size: 암호화 요청을 담고 있는 pool의 크기이다. 2를 기본값으로 권장하고 있다. string-output-type: 암호화 이후에 어떤 형태로 값을 받을지
 * 설정한다. base64 / hexadecimal을 선택할 수 있다. key-obtention-iterations: 암호화 키를 얻기 위해 반복해야 하는 해시 횟수이다. 클수록 암호화는 오래 걸리지만 보안 강도는
 * 높아진다. password: 암호화 키이다. 비밀키이므로 노출되지 않도록 주의한다.
 */

@Slf4j
@Configuration
@EnableEncryptableProperties
public class JasyptConfig {

    @Value("${jasypt.encryptor.algorithm}")
    private String algorithm;
    @Value("${jasypt.encryptor.pool-size}")
    private int poolSize;

    @Value("${jasypt.encryptor.secret}")
    private String passwordFile;
    @Value("${jasypt.encryptor.string-output-type}")
    private String stringOutputType;
    @Value("${jasypt.encryptor.key-obtention-iterations}")
    private int keyObtentionIterations;

    public JasyptConfig() {
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac")) {
            passwordFile = "/Users/jaehong/secret-key.txt";
        } else {
            passwordFile = "C:/secret-key/secret-key.txt";
        }
    }

    //암호화 설정값 설정
    @Bean
    public StringEncryptor jasyptStringEncryptor() {
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        encryptor.setPoolSize(poolSize);
        encryptor.setAlgorithm(algorithm);
        encryptor.setPassword(readSecretKeyFromFile(passwordFile));
        encryptor.setStringOutputType(stringOutputType);
        encryptor.setKeyObtentionIterations(keyObtentionIterations);

        return encryptor;
    }

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(@Value("${jasypt.encryptor.password-file}") String passwordFile) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        try {
            File file = ResourceUtils.getFile(passwordFile);
            FileInputStream fis = new FileInputStream(file);
            Properties properties = new Properties();
            properties.load(fis);
            encryptor.setPassword(properties.getProperty("password"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptor;
    }

    private String readSecretKeyFromFile(String filePath) {
        String secretKey = ""; // 비밀 키 값
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            secretKey = reader.readLine();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return secretKey;
    }


}