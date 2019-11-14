package ru.rtsoft.qa.asmrza;

import com.codeborne.selenide.SelenideElement;

import org.junit.Test;
import ru.rtsoft.qa.asmrza.testconfigs.BaseTest;
import ru.rtsoft.qa.asmrza.widgets.Page;


import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.element;
import static org.junit.Assert.*;

public class AsmrzaLoginTests extends BaseTest {


    @Test
    public void positiveLoginTests(){
        Page loginPage = new Page();
        loginPage.open();
        loginPage.enterData("admin", "qwerty123");
        SelenideElement loginButton = loginPage.getSelenideElement();
        loginButton.click();
//        loginPage.correctLoginCheck();
        element(byClassName("styles__main__title___if37D")).shouldBe(visible).shouldBe(text("Наблюдаемые объекты"));
        loginPage.logout();


        loginPage.enterData("admin", "qwerty123");
        loginButton.pressEnter();
//        loginPage.correctLoginCheck();
        element(byClassName("styles__main__title___if37D")).shouldBe(visible).shouldBe(text("Наблюдаемые объекты"));
        loginPage.logout();
    }

    @Test
    public void negativeLoginTests(){
        Page loginPage = new Page();
        loginPage.open();
        loginPage.enterData("test_user", "qwerty123");
        SelenideElement loginButton = loginPage.getSelenideElement();
        loginButton.click();
        SelenideElement selenideElement = element("[class^=styles__container__error").shouldBe(visible).shouldBe(text("Невозможно войти с предоставленными учетными данными."));
        assertEquals(loginPage.colorToHex(selenideElement), "#ff3434");

// дописать остальные тесты после решения по 93
    }



}
