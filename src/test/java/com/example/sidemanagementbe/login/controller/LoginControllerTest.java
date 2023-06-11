package com.example.sidemanagementbe.login.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.example.sidemanagementbe.login.dto.AccessTokenRequest;
import com.example.sidemanagementbe.login.dto.AccessTokenResponse;
import com.example.sidemanagementbe.login.security.util.JwtTokenProvider;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.Map;
import org.hamcrest.Matchers;
import org.jasypt.encryption.StringEncryptor;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;


@AutoConfigureMockMvc
@SpringBootTest
class LoginControllerTest {

    //kakao client id 암호화를 위함
    @Qualifier("jasyptStringEncryptor")
    @Autowired
    StringEncryptor stringEncryptor;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext ctx;

    @Autowired
    private LoginController controller;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private WebDriver driver;

    private String kakaoLoginResponse;

    /**
     * mockMvc에서의 response 한글 깨짐 방지
     */
    @BeforeEach
    public void setUp() {
        mockMvc = webAppContextSetup(ctx).addFilter(((request, response, chain) -> {
            response.setCharacterEncoding("UTF-8");
            chain.doFilter(request, response);
        })).build();
    }

    /**
     * 브라우저 닫기
     */
    @AfterEach
    public void exitBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    /**
     * 카카오톡 로그인 API 테스트를 실행 유효한 아이디(userId)와 비밀번호(userPw)를 입력해주고 실행할 것 각 계정 정보는 다르기에 특정 response 값을 고정적으로 check할 수 없음으로
     * 반환되는 json key 값을 가지고 있는지만 체크함 chrome driver 파일 필요 (크롬 버전에 맞는 chromedriver.exe 필요)
     */
    @Test
    @DisplayName("카카오 소셜 로그인 API 테스트")
    public void kakaoLoginApiResponseTest() throws Exception {
        //given
        String requestUrl = "https://kauth.kakao.com/oauth/authorize?client_id=" + stringEncryptor.decrypt(
                "2+e+SW0RMh0YOBiIahG8YF54j3PoWL0slPoMt4WbhTsuyFHmoe3lwxD/t9hygECZ")
                + "&redirect_uri=http://localhost:8080/login/oauth/kakao&response_type=code&scope=account_email,gender,birthday";

        //when
        //userId 입력하기
        String userId = "";
        //userPw 입력하기
        String userPw = "";
        String code = getResponseCode(requestUrl, userId, userPw);

        //then
        checkLoginResponse(code);
    }

    public String getResponseCode(String requestUrl, String userId, String userPw) {
        Assertions.assertNotEquals(userId, "nick1324@naver.com", "유효한 카카오톡 ID를 입력해주세요");
        Assertions.assertNotEquals(userPw, "jayyou!3204", "유효한 카카오톡 비밀번호를 입력해주세요");

        // Chrome WebDriver 설정
        String filePath = "";
        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.contains("mac")) {
            filePath = "/chromedriver/chromedriver";
        } else if (osName.contains("win")) {
            filePath = "C:\\chromedriver\\chromedriver.exe";
        }
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);

        // 특정 URL로 접속
        driver.get(requestUrl);

        // 응답 가져오기

        WebElement usernameInput = driver.findElement(By.id("loginKey--1"));
        WebElement passwordInput = driver.findElement(By.id("password--2"));
        WebElement loginButton = driver.findElement(By.className("submit"));

        usernameInput.sendKeys(userId);
        passwordInput.sendKeys(userPw);
        loginButton.click();

        // WebDriverWait를 사용하여 특정 요소가 나타날 때까지 대기
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // 최대 10초까지 대기
        By elementLocator = By.className("neterror");
        wait.until(ExpectedConditions.visibilityOfElementLocated(elementLocator));

        String responseUrl = driver.getCurrentUrl();
        //url 속 code 추출
        return getCode(responseUrl);
    }

    public void checkLoginResponse(String code) {
        try {
            MvcResult mvcResult = mockMvc.perform(get("/login/oauth/kakao")
                            .param("code", code)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON)
                            .characterEncoding("UTF-8"))
                    .andExpect(content().string(Matchers.containsString("id")))
                    .andExpect(content().string(Matchers.containsString("nickName")))
                    .andExpect(content().string(Matchers.containsString("email")))
                    .andExpect(content().string(Matchers.containsString("imageUrl")))
                    .andExpect(content().string(Matchers.containsString("role")))
                    .andExpect(content().string(Matchers.containsString("tokenType")))
                    .andExpect(content().string(Matchers.containsString("accessToken")))
                    .andExpect(content().string(Matchers.containsString("refreshToken")))
                    .andExpect(status().isOk())
                    .andReturn();

            String response = mvcResult.getResponse().getContentAsString();
            kakaoLoginResponse = response;
        } catch (Exception e) {
            e.printStackTrace();
            driver.quit();
        }
        driver.quit();
    }

    public String getCode(String url) {
        String key = null;
        String value = null;
        try {
            URI uri = new URI(url);
            String query = uri.getQuery();

            String[] params = query.split("&");
            for (String param : params) {
                String[] keyValue = param.split("=");
                key = keyValue[0];
                value = keyValue[1];

            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * access token이 만료해서 새로운 access token를 발급받는 테스트 refresh token를
     */
    @Test
    @DisplayName("JWT Access Token 재발급 테스트")
    public void AccessTokenRegenerate() throws Exception {
        //given
        kakaoLoginApiResponseTest();

        AccessTokenRequest request = new AccessTokenRequest(
                JsonParseToken(kakaoLoginResponse, "refreshToken"),
                JsonParseToken(kakaoLoginResponse, "accessToken"));  // 생성할 AccessTokenRequest 객체

        Map<String, Object> beforePayloadMap = jwtTokenProvider.getPayload(request.getAccessToken());

        //when
        ResponseEntity<AccessTokenResponse> response = controller.regenerateAccessToken(request);
        Map<String, Object> afterPayloadMap = jwtTokenProvider.getPayload(response.getBody().getAccessToken());

        //then
        Assertions.assertNotEquals(request.getAccessToken(), response.getBody().getAccessToken(), "토큰이 일치합니다.");
        Assertions.assertEquals(beforePayloadMap.get("id"), afterPayloadMap.get("id"), "payload의 id가 일치하지 않습니다.");
        Assertions.assertEquals(beforePayloadMap.get("role"), afterPayloadMap.get("role"), "payload의 id가 일치하지 않습니다.");

    }

    public String JsonParseToken(String token, String tokenType) {
        JSONObject jsonObject = new JSONObject(token);
        String refreshToken = jsonObject.getString(tokenType);

        return refreshToken;
    }

}