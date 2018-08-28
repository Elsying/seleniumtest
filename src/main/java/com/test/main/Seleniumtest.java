package com.test.main;

import org.junit.Before;
import org.junit.BeforeClass;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static java.lang.System.out;

public class Seleniumtest {
    private WebDriver driver;
    @BeforeMethod
    public void defaults(){
        System.setProperty("webdriver.chrome.driver", "D:\\chrome driver\\chromedriver.exe");
        driver = new ChromeDriver();
        out.println("start test");
    }
    @Test
    public void SeleniumTest(){
        driver.get("https://www.baidu.com");
        //通过ID，找到文本输入框
        WebElement element = driver.findElement(By.id("kw"));
        //设置输入框的内容
        element.sendKeys("aa");
        //通过ID，找到搜索按钮
        WebElement tijiao = driver.findElement(By.id("su"));
        //点击搜索按钮
        tijiao.click();

        //element.submit();
        //现在提交表单，webdriver的将找到的元素为我们的表单
        // driver.close();
    }

}
