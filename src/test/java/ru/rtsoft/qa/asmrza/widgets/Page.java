package ru.rtsoft.qa.asmrza.widgets;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.Color;

import java.sql.SQLException;
import java.util.*;

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
            SelenideElement s = elements(byCssSelector("span.styles__substation__name___1qVW9")).findBy(text(name)).parent().parent().parent();
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
        elements(byCssSelector("button"))
                .findBy(value("faults"))
                .shouldBe(visible)
                .find(byCssSelector("span.styles__fault-label___atLWx"))
                .shouldHave(text("Аварии"));
        elements(byCssSelector("button"))
                .findBy(value("malfunctions"))
                .shouldBe(visible)
                .find(byCssSelector("span.styles__malfunction-label___1u7uw"))
                .shouldHave(text("Неисправности"));
        elements(byCssSelector("button"))
                .findBy(value("system"))
                .shouldBe(visible)
                .find(byCssSelector("span.styles__system-label___3zPio"))
                .shouldHave(text("Оперативные"));
    }

    public void filterControlsPresenceCheck() {
        SelenideElement container = element(byCssSelector("[class^=styles__filter-list]")).shouldBe(visible);
        List<String> filtersExpected = Arrays.asList(new String[] {"Энергосистема", "Диспетчер", "Собственник", "Класс напряжения", "Смежные объекты"});
        ArrayList<String> filtersActual = new ArrayList<>();
        ElementsCollection elements = container.findAll(byClassName("styles__text___1Qlyb"));
        for (SelenideElement element:elements) {
            filtersActual.add(element.text());
        }
        assertThat(filtersActual, equalTo(filtersExpected));
        ElementsCollection filtersCheckbox = container.findAll(byCssSelector("div.styles__checkbox-list__title___gtUmD"));
        for (SelenideElement checkbox:filtersCheckbox) {
            checkbox.click();
            SelenideElement s  = checkbox.parent().parent();
            s.find(byCssSelector("div[class^=styles__dropdown___2G4eg]")).shouldBe(visible);
            checkbox.click();
        }
    }

    public void filterControlsContentCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        SelenideElement container = element(byCssSelector("[class^=styles__filter-list]")).shouldBe(visible);
        ElementsCollection containerElements = container.findAll(byClassName("styles__text___1Qlyb"));
        String sqlQuery;
        for (SelenideElement containerElement:containerElements) {
            switch (containerElement.text()){
                case "Энергосистема":
                    sqlQuery = "select distinct name from asm_geographical_region";
                    findingAndAssert(database, containerElement, sqlQuery);
                    break;
                case "Диспетчер":
                    sqlQuery = "SELECT distinct asm_company.name FROM asm_company JOIN asm_substation ON asm_company.id = asm_substation.operator_id";
                    findingAndAssert(database, containerElement, sqlQuery);
                    break;
                case "Собственник":
                    sqlQuery = "SELECT distinct asm_company.name FROM asm_company JOIN asm_substation ON asm_company.id = asm_substation.owner_id";
                    findingAndAssert(database, containerElement, sqlQuery);
                    break;
                case "Класс напряжения":
                    sqlQuery = "select max (asm_base_voltage.nominal) from asm_substation left join asm_switchgear on asm_substation.id = asm_switchgear.substation_id\n" +
                            "left join asm_base_voltage on asm_switchgear.base_voltage_id = asm_base_voltage.id\n" +
                            "group by asm_substation.name";
                    findingUniqueAndAssert(database, containerElement, sqlQuery);
            }
        }
    }

    private void findingAndAssert(Database database, SelenideElement containerElement, String sqlQuery) throws SQLException {
        ArrayList<String> nameOfElementsFromDb = database.getNameOfObjects(sqlQuery);
        nameOfElementsFromDb.sort(Comparator.naturalOrder());
        ArrayList<String> nameOfElementsFromWeb = new ArrayList<>();
        SelenideElement parentElement = containerElement.parent().parent().parent().parent();
        parentElement.click();
        ElementsCollection contents = parentElement.findAll(byCssSelector("label[class=styles__checkbox__content___3_xkR]"));
        for (SelenideElement content:contents) {
            nameOfElementsFromWeb.add(content.text());
        }
        nameOfElementsFromWeb.sort(Comparator.naturalOrder());
        assertThat(nameOfElementsFromDb, equalTo(nameOfElementsFromWeb));
        parentElement.click();
    }

    private void findingUniqueAndAssert(Database database, SelenideElement containerElement, String sqlQuery) throws SQLException {
        ArrayList<String> s = database.getNameOfObjects(sqlQuery);
        HashSet<String> ss = new HashSet<>(s);
        ArrayList<String> nameOfElementsFromDb = new ArrayList<>(ss);
        ArrayList<String> nameOfElementsFromWeb = new ArrayList<>();
        SelenideElement parentElement = containerElement.parent().parent().parent().parent();
        parentElement.click();
        ElementsCollection contents = parentElement.findAll(byCssSelector("label[class=styles__checkbox__content___3_xkR]"));
        for (SelenideElement content:contents) {
            nameOfElementsFromWeb.add(String.valueOf(Integer.valueOf((int) Double.parseDouble(content.text().substring(0, 3)) * 1000)));
        }
        assertThat(nameOfElementsFromDb, equalTo(nameOfElementsFromWeb));
        parentElement.click();
    }

    public void filteringByEnergoSystem(String energoSystemName) throws SQLException {
        Database database = new Database();
        database.connect();
        element(byCssSelector("li.styles__substation___1JooU")).shouldBe(visible);
        int initialNumberStationOfSystemFromDb = database.getNumberOfItems("select asm_substation.name from asm_substation left join asm_geographical_region on " +
                "asm_substation.geographical_region_id = asm_geographical_region.\"id\" where asm_geographical_region.\"name\" = 'ЭС Юга'");
        int initialNumberStationOfSystemFromWeb = 0;
        ElementsCollection initialContainers = elements(byCssSelector("li.styles__substation___1JooU"));
        for (SelenideElement container:initialContainers) {
            String stationInfo = container.find(byClassName("styles__substation__info___31rGu")).text();
            String energoSystem = stationInfo.substring(0, stationInfo.indexOf('|') - 1);
            if (energoSystem.equals(energoSystemName)){
                initialNumberStationOfSystemFromWeb++;
            }
        }
        assertThat(initialNumberStationOfSystemFromDb, equalTo(initialNumberStationOfSystemFromWeb));
        int finalNumberStationOfSystemFromDb = database.getNumberOfItems("select asm_substation.name from asm_substation left join asm_geographical_region on" +
                " asm_substation.geographical_region_id = asm_geographical_region.\"id\" where asm_geographical_region.\"name\" != 'ЭС Юга'");
        int finalNumberStationOfSystemFromWeb = 0;
        elements(byCssSelector("div.styles__text___1Qlyb")).findBy(text(energoSystemName)).click();
        element(byCssSelector("label.styles__checkbox__icon___2jsv2")).click();
        ElementsCollection finalContainers = elements(byCssSelector("li.styles__substation___1JooU"));
        for (SelenideElement container:finalContainers){
            String stationInfo = container.find(byClassName("styles__substation__info___31rGu")).text();
            String energoSystem = stationInfo.substring(0, stationInfo.indexOf('|') - 1);
            if (energoSystem.equals(energoSystemName)){
                finalNumberStationOfSystemFromWeb++;
            }
        }
        assertThat(finalNumberStationOfSystemFromDb, equalTo(finalNumberStationOfSystemFromWeb));
    }
}
