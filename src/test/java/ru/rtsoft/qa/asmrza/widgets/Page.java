package ru.rtsoft.qa.asmrza.widgets;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.Color;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.element;


public class Page {
    public void open() {
        Selenide.open("/");

    }

    public void logout() {
        element(byClassName("styles__submenu__exit___1VaJe")).click();
    }

    public void correctLoginCheck() {
        element(byClassName("styles__main__title___if37D")).shouldBe(visible).shouldBe(text("Наблюдаемые объекты"));
    }

    public String colorToHex(SelenideElement selenideElement) {
        String color = Color.fromString(selenideElement.getCssValue("color")).asHex();
        return color;
    }

    public SelenideElement getSelenideElement() {
        return element(byText("Войти в систему"));
    }

    public void enterData(String name, String pass) {
        $(byName("username")).setValue(name);
        $(byName("password")).setValue(pass);
    }
}
