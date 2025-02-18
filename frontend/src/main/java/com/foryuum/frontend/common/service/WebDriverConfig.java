package com.foryuum.frontend.common.service;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/properties/linkage.properties")
public class WebDriverConfig {
	
	private static String driverPath;

	@Value("#{environment['driverPath']}")
	private void setDriverPath (String path) {
		driverPath = path;
	}
	
    public static WebDriver webDriver() {
        System.setProperty("webdriver.chrome.driver", driverPath);

        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--headless");    // 헤드리스 모드 활성화
//        options.addArguments("--no-sandbox");  // 보안 모드 비활성화 (리눅스에서 필요할 수 있음)
//        options.addArguments("--disable-dev-shm-usage");  // DevTools 프로토콜 비활성화

        return new ChromeDriver(options);
    }
}