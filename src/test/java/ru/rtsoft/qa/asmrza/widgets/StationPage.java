package ru.rtsoft.qa.asmrza.widgets;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.element;
import static com.codeborne.selenide.Selenide.elements;
import static org.junit.Assert.assertEquals;


public class StationPage extends Page{

    public StationPage gotoFaults() {
        element(byCssSelector("button[value='faults']")).click();
        elements(byCssSelector("button")).findBy(text("Новая авария")).shouldBe(visible);
        return this;
    }

    public FaultDialog newFaultDialogOpen() {
        FaultDialog faultDialog = new FaultDialog();
        elements(byCssSelector("button")).findBy(text("Новая авария")).click();
        assertEquals("Добавление новой аварии", element(byCssSelector("div.styles__modal___WLErM div.styles__content__header___3TIWx")).text());
        return faultDialog;
    }
}
