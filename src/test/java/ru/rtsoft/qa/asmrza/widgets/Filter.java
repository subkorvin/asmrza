package ru.rtsoft.qa.asmrza.widgets;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.element;
import static com.codeborne.selenide.Selenide.sleep;

public class Filter {

    public void openDropDownMenuFor(String filterName) {
        SelenideElement filterBar = element(byCssSelector("[class^=styles__filter-list]")).shouldBe(visible);
        ElementsCollection filters = filterBar.findAll(byClassName("styles__text___1Qlyb"));
        SelenideElement filter = filters.findBy(text(filterName));
        filter.click();
        sleep(5000);
    }
}
