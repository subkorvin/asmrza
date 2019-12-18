package ru.rtsoft.qa.asmrza.widgets;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.element;
import static com.codeborne.selenide.Selenide.elements;


public class StationPage extends Page{

    public StationPage gotoFaults() {
        element(byCssSelector("button[value='faults']")).click();
        return this;
    }

    public FaultDialog newFaultDialogOpen() {
        FaultDialog faultDialog = new FaultDialog();
        elements(byCssSelector("button")).findBy(text("Новая авария")).click();
        return faultDialog;
    }
}
