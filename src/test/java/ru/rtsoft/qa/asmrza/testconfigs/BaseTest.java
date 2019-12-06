package ru.rtsoft.qa.asmrza.testconfigs;

import com.codeborne.selenide.Configuration;


public class BaseTest {

    static {
        Configuration.baseUrl = "http://192.168.10.61";
        Configuration.startMaximized = true;
//        Configuration.screenshots = true;
//        Configuration.browser = "firefox";
//        ChromeOptions options = new ChromeOptions();
//        options.setExperimentalOption("useAutomationExtension", false);
//        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

    }
}
