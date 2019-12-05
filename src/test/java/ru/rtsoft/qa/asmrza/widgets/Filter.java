package ru.rtsoft.qa.asmrza.widgets;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.element;

public class Filter {

    public void openDropDownMenuFor(String filterName) {
        SelenideElement filterBar = element(byCssSelector("[class^=styles__filter-list]")).shouldBe(visible);
        SelenideElement dropDownMenu = filterBar.findAll(byClassName("styles__text___1Qlyb")).findBy(text(filterName));
        dropDownMenu.click();
//        Попытка получить состояние чекбокса (пока неудачная)
//        String script = "return window.getComputedStyle(document.querySelector('span.styles__checkbox__container___3smLo'),':after').getPropertyValue('width')";
//        String script = "return window.getComputedStyle(document.querySelectorAll('span.styles__checkbox__container___3smLo'),':after').getPropertyValue('width')";
//        String content = Selenide.executeJavaScript(script);
//        String content = Selenide.executeJavaScript(script);
//        NodeList list = Selenide.executeJavaScript(script);
//        List<Node> content = IntStream.range(0, list.getLength())
//                .mapToObj(list::item)
//                .collect(Collectors.toList());
//        System.out.println(content);
//        sleep(5000);
    }


    public void filteringBy(String filterName, String filterBy) {
        SelenideElement filterBar = element(byCssSelector("[class^=styles__filter-list]")).shouldBe(visible);
        SelenideElement dropDownMenu = filterBar.findAll(byClassName("styles__text___1Qlyb")).findBy(text(filterName));
        SelenideElement dropDownList = dropDownMenu.parent().parent().parent().parent().find(byCssSelector("div.styles__scrollbar___1GbIe "));
        SelenideElement dropDownMenuItem = dropDownList.find(byText(filterBy)).parent().find(byCssSelector("span.styles__checkbox__container___3smLo"));
        dropDownMenuItem.click();
    }

    public void dropFilters() {
        SelenideElement filterBar = element(byCssSelector("div[class^=styles__filter-list]")).shouldBe(visible);
        SelenideElement dropFilterButton = filterBar.find(byCssSelector("button.styles__button___Fbunz styles__button_s___240cs styles__button__transparent___2euQU styles__filter-list__btn___15lOn"));
        if (dropFilterButton.isDisplayed()){
            dropFilterButton.click();
        }
    }
}
