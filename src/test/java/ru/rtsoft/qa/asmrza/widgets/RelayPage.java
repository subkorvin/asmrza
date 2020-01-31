package ru.rtsoft.qa.asmrza.widgets;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.util.ArrayList;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;

public class RelayPage {

    private static SelenideElement tabsContainer = element(byCssSelector("div.styles__buttons-group____v-PW"));
    private static ElementsCollection swticherContainer = elements(byCssSelector("div.selector"));
    private static SelenideElement history = element(byCssSelector("a.styles__selectors__archive-item___1s88u"));

    public RelayPage gotoSwitchTab() {
        tabsContainer.find(byCssSelector("button[value='selectors']")).click();
        history.shouldBe(visible);
        return this;
    }

    public RelayPage revertToInitialState() {
        ArrayList<SelenideElement> switchers = new ArrayList<>();
        for (SelenideElement switcher : swticherContainer) {
            ElementsCollection switcherInfoElement = switcher.findAll(byCssSelector("div.styles__body-cell___1t6C_"));
            String disagreement = switcherInfoElement.get(1).text();
            if (!disagreement.equals("")) {
                switchers.add(switcher);
            }
        }
        element(byCssSelector("button.styles__button___Fbunz.styles__button_m___2zJuu.styles__button__transparent___2euQU.edit-button")).click();
        for (SelenideElement switcher : switchers) {
            String currentState = switcher.findAll(byCssSelector("div.styles__body-cell___1t6C_")).get(2).text();
            switcher.find(byCssSelector("div.styles__select__current___HeCLT")).click();
            element(byCssSelector("div.styles__options___13xTb")).find(byText(currentState)).click();
        }
        element(byCssSelector("button.styles__button___Fbunz.styles__button_m___2zJuu.styles__button__primary___2hHGd.save-btn")).click();
        history.shouldBe(visible);
        refresh();
        return this;
    }

    public void setSwitchers() {
        history.shouldBe(visible);
        SelenideElement selectedSwitcher = null;
        String currentState = "";
        String name = "";
        for (SelenideElement switcher : swticherContainer) {
            ElementsCollection switcherInfoElement = switcher.findAll(byCssSelector("div.styles__body-cell___1t6C_"));
            name = switcherInfoElement.get(0).text();
            currentState = switcherInfoElement.get(2).text();
            String expectedState = switcherInfoElement.get(3).text();
            if (currentState.equals(expectedState)) {
                selectedSwitcher = switcher;
                break;
            }
        }
        element(byCssSelector("button.styles__button___Fbunz.styles__button_m___2zJuu.styles__button__transparent___2euQU.edit-button")).click();
        assert selectedSwitcher != null;
        selectedSwitcher.find(byCssSelector("div.styles__select___3nFn0")).click();
        element(byCssSelector("div.styles__options-item___2QowE")).find(byXpath("./following-sibling::*")).click();
//        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm:ss"));
        element(byCssSelector("button.styles__button___Fbunz.styles__button_m___2zJuu.styles__button__primary___2hHGd.save-btn")).click();
        refresh();
        history.shouldBe(visible);
        for (SelenideElement switcher : swticherContainer) {
            ElementsCollection switcherInfoElement = switcher.findAll(byCssSelector("div.styles__body-cell___1t6C_"));
            currentState = switcherInfoElement.get(2).text();

        }


//        selectedSwitcher = swticherContainer.findBy(text(name));
//        selectedSwitcher = swticherContainer.findBy(matchText(name));
        String dateTimeFromSwitch = selectedSwitcher.findAll(byCssSelector("div.styles__body-cell___1t6C_")).get(1).text();
        sleep(1000);
    }
}
