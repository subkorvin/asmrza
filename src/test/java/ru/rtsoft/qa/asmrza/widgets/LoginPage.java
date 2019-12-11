package ru.rtsoft.qa.asmrza.widgets;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class LoginPage extends Page{


    public LoginPage open() {
        Selenide.open("/");
        return this;
    }

    public LoginPage logout() {
        element(byClassName("styles__submenu__exit___1VaJe")).click();
        return this;
    }

    public LoginPage correctLoginCheck() {
        element(byClassName("styles__main__title___if37D")).shouldBe(visible).shouldBe(text("Наблюдаемые объекты"));
        return this;
    }

    public SelenideElement loginButton() {
        return element(byText("Войти в систему"));
    }

    public LoginPage enterData(String name, String pass) {
        $(byName("username")).setValue(name);
        $(byName("password")).setValue(pass);
        return this;
    }

    public void checkTracingObjectsAttributes() {
        element(byCssSelector("li.styles__substation___1JooU")).shouldBe(visible);
        ElementsCollection containers = elements(byCssSelector("li.styles__substation___1JooU"));
        for (SelenideElement container : containers) {
            if (container.find(byCssSelector(".styles__substation___1JooU span.styles__substation__adjacent-icon___2U8jP")).exists()) {
                continue;
            } else {
                container.find(byClassName("styles__substation__fault-icon___2dvY5")).shouldBe(visible);
                container.find(byClassName("styles__substation__events-count-date___1Ayf0")).shouldBe(visible);
                container.find(byClassName("styles__substation__events-count___Sn81X")).shouldBe(visible);
                container.find(byCssSelector("[class*=malfunctions-icon]")).shouldBe(visible);
                container.find(byCssSelector("[class*=malfunctions-text]")).shouldBe(visible);
            }
        }
    }
}
