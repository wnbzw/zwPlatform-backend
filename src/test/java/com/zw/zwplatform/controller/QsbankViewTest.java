package com.zw.zwplatform.controller;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class QsbankViewTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        // 设置 ChromeDriver 路径（根据你的本地环境调整）
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\16247\\AppData\\Local\\Google\\Chrome\\Application\\chrome.exe");

        // 初始化浏览器
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @Test
    void testViewQsBankList() {
        // 1. 打开登录页面（替换为你的实际登录页面 URL）
        driver.get("http://localhost:3000/user/login");

        // 2. 输入用户名和密码并提交登录表单
        WebElement usernameInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        usernameInput.sendKeys("admin");

        WebElement passwordInput = driver.findElement(By.name("password"));
        passwordInput.sendKeys("12345678");

        WebElement loginButton = driver.findElement(By.id("loginBtn"));
        loginButton.click();

        // 3. 等待跳转到题库管理页面
        wait.until(ExpectedConditions.urlContains("/question-bank/list"));

        // 4. 进入分页查看题库列表页面（调用接口渲染前端）
        driver.get("http://localhost:3000/question-bank/list?page=1&size=10");

        // 5. 验证题库数据是否加载
        WebElement bankListContainer = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("bank-list")));

        // 6. 检查是否存在题库项
        java.util.List<WebElement> bankItems = bankListContainer.findElements(By.cssSelector(".bank-item"));
        Assertions.assertFalse(bankItems.isEmpty(), "题库列表不应为空");

        // 7. 验证第一个题库名称是否存在
        WebElement firstBankName = bankItems.get(0).findElement(By.cssSelector(".bank-name"));
        String bankNameText = firstBankName.getText();
        Assertions.assertNotNull(bankNameText);
        Assertions.assertFalse(bankNameText.trim().isEmpty());

        // 8. 验证分页组件存在并可点击下一页
        WebElement nextPageButton = driver.findElement(By.id("next-page-btn"));
        Assertions.assertTrue(nextPageButton.isDisplayed());
        nextPageButton.click();

        // 9. 等待下一页加载
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector(".bank-name"), bankNameText)));

        // 10. 成功断言
        Assertions.assertTrue(true, "用户成功查看题库列表并分页切换");
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

