package ru.rtsoft.qa.asmrza.widgets;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.junit.Assert;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.Color;

import java.util.ArrayList;
import java.util.Comparator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


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

    public int getNumberOfEnergoObjects() {
        element(byCssSelector("li.styles__substation___1JooU")).shouldBe(visible);
        int s = elements(byClassName("styles__substation__body___26JUD")).size();
        return s;
    }

    public int getNumberOfFaults() {
        element(byCssSelector("div.styles__fault___1kqTH")).shouldBe(visible);
        int s = elements(byClassName("styles__fault___1kqTH")).size();
        return s;
    }

    public int getNumberOfTracingObjects() {
        element(byCssSelector("li.styles__substation___1JooU")).shouldBe(visible);
        int s = elements(byCssSelector("li[class^=styles__navigation__item___2PZLM]")).size();
        return s;
    }

    public ArrayList<String> getNameOfObjects() {
        element(byCssSelector("li.styles__substation___1JooU")).shouldBe(visible);
        ArrayList<String> name = new ArrayList<String>();
        ElementsCollection items = elements(byCssSelector("li[class^=styles__navigation__item___2PZLM]"));
        for (SelenideElement item: items) {
            name.add(item.text());
        }
        name.sort(Comparator.naturalOrder());
        return name;
    }

    public int getNumberOfNonTracingObjects() {
        element(byCssSelector("li.styles__substation___1JooU")).shouldBe(visible);
        int s = elements(byCssSelector("span.styles__substation__adjacent-text___25NGJ")).size();
        return s;
    }

    public void checkNonTracingObjectsAttributes() {
        element(byCssSelector("span.styles__substation__adjacent-icon___2U8jP")).shouldBe(visible);
        element(byCssSelector("span.styles__substation__adjacent-text___25NGJ")).shouldHave(text("Ненаблюдаемый объект (без сбора данных)"));
    }

    public void checkTracingObjectsAttributes() {
        element(byCssSelector("li.styles__substation___1JooU")).shouldBe(visible);
        ElementsCollection containers = elements(byCssSelector("li.styles__substation___1JooU"));
        for (SelenideElement container: containers) {
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

    public void checkGotoButtonPresence() {
        element(byCssSelector("li.styles__substation___1JooU")).shouldBe(visible);
        ElementsCollection containers = elements(byCssSelector("li.styles__substation___1JooU"));
        ArrayList<String> names = new ArrayList<>();
        for (SelenideElement container: containers) {
            String objNameMain = container.find(byClassName("styles__substation__name___1qVW9")).text();
            names.add(objNameMain);
        }
        for (String name: names) {
            element(byCssSelector("li.styles__substation___1JooU")).shouldBe(visible);
            ElementsCollection coll = elements(byCssSelector("span.styles__substation__name___1qVW9")).filterBy(text(name));
            SelenideElement s = coll.get(0).parent().parent().parent();
            SelenideElement button = s.find(withText("Перейти к объекту"));
            button.click();
            String objName = element(byClassName("styles__header__name-content___3qA1W")).text();
            assertThat(name, equalTo(objName));
            back();
        }
    }

    public void checkArchiveLink() {
        element(byCssSelector("li.styles__submenu__link___1q3c2 a")).shouldHave(text("Архив аварий")).click();
        element(byCssSelector("div.styles__faults__title___2_UCJ")).shouldHave(text("Архив аварий"));
        element(byCssSelector("div.styles__table___2FJid")).shouldBe(visible);
        back();
    }

    public void checkJournalLink() {
        elements(byCssSelector("li.styles__submenu__link___1q3c2 a")).findBy(text("Журнал событий")).click();
        element(byCssSelector("div.styles__journal__title___1znyZ")).shouldHave(text("Журнал событий"));
        element(byCssSelector("div.styles__table-body___2KcPG")).shouldBe(visible);
        back();
    }

    public void checkUserLink() {
        element(byClassName("styles__submenu__profile___1tlNA")).click();
        element(byClassName("styles__modal___WLErM")).shouldBe(visible);
        elements(byCssSelector("div.styles__label___2AZWx")).findBy(text("ФИО")).shouldBe(visible);
        element(byClassName("styles__modal__close-icon___5ctke")).click();
        element(byClassName("styles__main__title___if37D")).shouldBe(visible).shouldBe(text("Наблюдаемые объекты"));
    }

    public void exitLink() {
        element(byClassName("styles__submenu__exit___1VaJe")).click();
        element(byClassName("styles__container__title___RA7He")).shouldHave(text("Авторизация"));
    }

    public void panelPresenceCheck() {
        element(byClassName("styles__main__right___CTlka")).shouldBe(visible);
        elements(byCssSelector("button")).findBy(value("faults")).shouldBe(visible);
    }
}
