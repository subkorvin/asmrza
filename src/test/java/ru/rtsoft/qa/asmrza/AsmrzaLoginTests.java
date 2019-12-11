package ru.rtsoft.qa.asmrza;

import com.codeborne.selenide.SelenideElement;
import org.junit.Test;
import ru.rtsoft.qa.asmrza.testconfigs.BaseTest;
import ru.rtsoft.qa.asmrza.widgets.LoginPage;
import ru.rtsoft.qa.asmrza.widgets.MainPage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.element;
import static org.junit.Assert.assertEquals;

public class AsmrzaLoginTests extends BaseTest {


    @Test
    public void positiveLoginTests(){
        new LoginPage()
                .open()
                .enterData("admin", "qwerty123")
                .loginButton().click();
        new MainPage().correctLoginCheck()
                .logout();
        new LoginPage().enterData("admin", "qwerty123").
                loginButton().pressEnter();
        new MainPage().correctLoginCheck()
                .logout();
    }

    @Test
    public void negativeLoginTests(){
        LoginPage loginPage = new LoginPage();
        loginPage.open();
        loginPage.enterData("test_user", "qwerty123");
        SelenideElement loginButton = loginPage.loginButton();
        loginButton.click();
        SelenideElement selenideElement = element("[class^=styles__container__error").shouldBe(visible).shouldBe(text("Невозможно войти с предоставленными учетными данными."));
        assertEquals(loginPage.colorToHex(selenideElement), "#ff3434");

// дописать остальные тесты после решения по 93
    }



}
