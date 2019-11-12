package ru.rtsoft.qa.asmrza.testconfigs;

import com.codeborne.selenide.Configuration;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.Collections;

public class BaseTest {

    {
        Configuration.baseUrl = "http://192.168.10.61";
        Configuration.startMaximized = true;
//        Configuration.browser = "firefox";
//        ChromeOptions options = new ChromeOptions();
//        options.setExperimentalOption("useAutomationExtension", false);
//        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

    }
}
