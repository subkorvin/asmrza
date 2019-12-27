package ru.rtsoft.qa.asmrza.widgets;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.Assert.assertEquals;


public class FaultDialog {

    private final SelenideElement container = element(byCssSelector("div.styles__modal___WLErM"));
    private final SelenideElement objectsSelector = element(byCssSelector("div.styles__modal___2T6NJ"));

    public FaultDialog addNewFault() {
        elements(By.cssSelector("button")).findBy(text("Перейти к добавлению")).click();
        assertEquals("Информация об аварии", container.find(byClassName("styles__modeling-form__title____zEqH")).text());
        return this;
    }

    public void enterValues(String status, String substation, String switchgear, String equipment, int endTimeShift, String phase, String distance) {
        SelenideElement statusField = container.find(byCssSelector("div.styles__select__current___HeCLT"));
        statusField.click();
        container.parent().find(byXpath("./following-sibling::div[@class='styles__options___13xTb']")).find(withText(status)).click();
        elements(byCssSelector("div.styles__modeling-form__row___1TWvL")).findBy(text("Место повреждения")).find(byCssSelector("input.styles__input___1_7aF")).click();
        assertEquals("Выберите объект системы", objectsSelector.find(byClassName("styles__equipment-title___1e_Lb")).text());
        objectsSelector.find(withText(substation)).click();  //выбираем подстанцию
        objectsSelector.find(withText(switchgear)).click();  //выбираем группу присоединений (сгруппированы по уровню напряжений)
        objectsSelector.find(withText(equipment)).click();   //выбираем присоединение
        objectsSelector.findAll(byCssSelector("button")).findBy(text("Применить")).click();   //подтверждаем выбор
        SelenideElement endTimeElement = container.find(withText("Время окончания"))
                .parent()
                .find(byCssSelector("div.react-datepicker-wrapper"))
                .find(byXpath("./following-sibling::*"));
        String endTime = endTimeElement.getAttribute("value");
        String endTimeSec = String.valueOf(Integer.parseInt(endTime.substring(0, endTime.indexOf(".")).split(":")[2]) + endTimeShift);
        String endTimeMsec = endTime.substring(endTime.indexOf(".") + 1);
        endTime = endTime.split(":")[0] + ":" + endTime.split(":")[1] + ":" + endTimeSec + "." + endTimeMsec;
        StringSelection stringSelection = new StringSelection(endTime);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        endTimeElement.click();
        endTimeElement.sendKeys(Keys.CONTROL, "a");
        endTimeElement.sendKeys(Keys.CONTROL, "v");
        container.find(withText("Фаза")).find(byXpath("./following-sibling::*")).find(byCssSelector("div.styles__select__current___HeCLT")).click();
        container.parent().find(byXpath("./following-sibling::div[@class='styles__options___13xTb']")).find(withText(phase)).click();
        container.find(withText("ОМП")).find(byXpath("./following-sibling::*")).find(byCssSelector("input")).setValue(distance);
        container.findAll(byCssSelector("button")).findBy(text("Сохранить")).click();
    }
}
