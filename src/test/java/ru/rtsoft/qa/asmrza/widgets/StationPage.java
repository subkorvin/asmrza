package ru.rtsoft.qa.asmrza.widgets;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.element;
import static com.codeborne.selenide.Selenide.elements;
import static org.junit.Assert.assertEquals;


public class StationPage extends Page{

    private static SelenideElement equipmentContainer = element(byCssSelector("div.styles__left-container___30HGv"));
    private static ElementsCollection relayContainers = elements(byCssSelector("div.styles__relay___1Cw_D"));

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

    public StationPage goToEquipment(String stationName, String equipment) {
        element(byCssSelector("div.styles__header__name-content___3qA1W")).shouldHave(text(stationName)).shouldBe(visible);
        equipmentContainer.findAll(byCssSelector("div.styles__text___1Qlyb")).findBy(text(equipment)).click();
//        equipmentContainer.find(byCssSelector("div.styles__text___1Qlyb")).find(withText(equipment)).click();
//        equipmentContainer.find(byText(equipment)).click();
        element(byCssSelector("div.styles__substation-name___2jhWo")).shouldHave(text(equipment)).shouldBe(visible);
        return this;
    }

    public RelayPage gotoRelay(String relayName) {
        relayContainers.findBy(text(relayName)).findAll(byCssSelector("button")).findBy(text("Перейти к РЗА")).click();
        return new RelayPage();
    }
}
