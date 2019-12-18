package ru.rtsoft.qa.asmrza.widgets;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

public class MainPage extends Page {


    private final ElementsCollection containers = elements(byCssSelector("li.styles__substation___1JooU"));
    private final SelenideElement filterContainer = element(byCssSelector("[class^=styles__filter-list]"));
    private final SelenideElement anyContainer = element(byCssSelector("li.styles__substation___1JooU"));

    public int getNumberOfEnergoObjects() {
        anyContainer.shouldBe(visible);
        int s = elements(byClassName("styles__substation__body___26JUD")).size();
        return s;
    }

    public int getNumberOfFaults() {
        element(byCssSelector("div.styles__fault___1kqTH")).shouldBe(visible);
        int s = elements(byClassName("styles__fault___1kqTH")).size();
        return s;
    }

    public int getNumberOfTracingObjects() {
        anyContainer.shouldBe(visible);
        int s = elements(byCssSelector("li[class^=styles__navigation__item___2PZLM]")).size();
        return s;
    }

    public ArrayList<String> getNameOfObjects() {
        anyContainer.shouldBe(visible);
        ArrayList<String> name = new ArrayList<String>();
        ElementsCollection items = elements(byCssSelector("li[class^=styles__navigation__item___2PZLM]"));
        for (SelenideElement item : items) {
            name.add(item.text());
        }
        name.sort(Comparator.naturalOrder());
        return name;
    }

    public int getNumberOfNonTracingObjects() {
        anyContainer.shouldBe(visible);
        int s = elements(byCssSelector("span.styles__substation__adjacent-text___25NGJ")).size();
        return s;
    }

    public MainPage checkNonTracingObjectsAttributes() {
        element(byCssSelector("span.styles__substation__adjacent-icon___2U8jP")).shouldBe(visible);
        element(byCssSelector("span.styles__substation__adjacent-text___25NGJ")).shouldHave(text("Ненаблюдаемый объект (без сбора данных)"));
        return this;
    }

    public MainPage checkGotoButtonPresence() {
        anyContainer.shouldBe(visible);
        ArrayList<String> names = new ArrayList<>();
        for (SelenideElement container : containers) {
            String objNameMain = container.find(byClassName("styles__substation__name___1qVW9")).text();
            names.add(objNameMain);
        }
        for (String name : names) {
            anyContainer.shouldBe(visible);
            SelenideElement s = elements(byCssSelector("span.styles__substation__name___1qVW9")).findBy(text(name)).parent().parent().parent();
            SelenideElement button = s.find(withText("Перейти к объекту"));
            button.click();
            String objName = element(byClassName("styles__header__name-content___3qA1W")).text();
            assertThat(name, equalTo(objName));
            back();
        }
        return this;
    }

    public MainPage checkArchiveLink() {
        element(byCssSelector("li.styles__submenu__link___1q3c2 a")).shouldHave(text("Архив аварий")).click();
        element(byCssSelector("div.styles__faults__title___2_UCJ")).shouldHave(text("Архив аварий"));
        element(byCssSelector("div.styles__table___2FJid")).shouldBe(visible);
        back();
        return this;
    }

    public MainPage checkJournalLink() {
        elements(byCssSelector("li.styles__submenu__link___1q3c2 a")).findBy(text("Журнал событий")).click();
        element(byCssSelector("div.styles__journal__title___1znyZ")).shouldHave(text("Журнал событий"));
        element(byCssSelector("div.styles__table-body___2KcPG")).shouldBe(visible);
        back();
        return this;
    }

    public MainPage checkUserLink() {
        element(byClassName("styles__submenu__profile___1tlNA")).click();
        element(byClassName("styles__modal___WLErM")).shouldBe(visible);
        elements(byCssSelector("div.styles__label___2AZWx")).findBy(text("ФИО")).shouldBe(visible);
        element(byClassName("styles__modal__close-icon___5ctke")).click();
        element(byClassName("styles__main__title___if37D")).shouldBe(visible).shouldBe(text("Наблюдаемые объекты"));
        return this;
    }

    public MainPage exitLink() {
        element(byClassName("styles__submenu__exit___1VaJe")).click();
        element(byClassName("styles__container__title___RA7He")).shouldHave(text("Авторизация"));
        return this;
    }

    public MainPage panelPresenceCheck() {
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
        return this;
    }

    public MainPage filterControlsPresenceCheck() {
        filterContainer.shouldBe(visible);
        List<String> filtersExpected = Arrays.asList("Энергосистема", "Диспетчер", "Собственник", "Класс напряжения", "Смежные объекты");
        ArrayList<String> filtersActual = new ArrayList<>();
        ElementsCollection elements = filterContainer.findAll(byClassName("styles__text___1Qlyb"));
        for (SelenideElement element : elements) {
            filtersActual.add(element.text());
        }
        assertThat(filtersActual, equalTo(filtersExpected));
        ElementsCollection filtersCheckbox = filterContainer.findAll(byCssSelector("div.styles__checkbox-list__title___gtUmD"));
        for (SelenideElement checkbox : filtersCheckbox) {
            checkbox.click();
            SelenideElement s = checkbox.parent().parent();
            s.find(byCssSelector("div[class^=styles__dropdown___2G4eg]")).shouldBe(visible);
            checkbox.click();
        }
        return this;
    }

    public MainPage filterControlsContentCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        filterContainer.shouldBe(visible);
        ElementsCollection containerElements = filterContainer.findAll(byClassName("styles__text___1Qlyb"));
        String sqlQuery;
        for (SelenideElement containerElement : containerElements) {
            switch (containerElement.text()) {
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
        return this;
    }

    public MainPage logout() {
        element(byClassName("styles__submenu__exit___1VaJe")).click();
        return this;
    }

    public MainPage correctLoginCheck() {
        element(byClassName("styles__main__title___if37D")).shouldBe(visible).shouldBe(text("Наблюдаемые объекты"));
        return this;
    }


    public void checkTracingObjectsAttributes() throws SQLException {
        Database database = new Database();
        database.connect();
        anyContainer.shouldBe(visible);
        String sqlBody = "from asm_substation left join asm_panel on asm_substation.id = asm_panel.substation_id " +
                "left join asm_protection_device on asm_panel.id = asm_protection_device.panel_id " +
                "left join asm_malfunction_history on asm_protection_device.id = asm_malfunction_history.device_id " +
                "where asm_malfunction_history.malfunction_end is null and asm_malfunction_history.malfunction_start is not null";
        int rowsNumber = database.getNumberOfRows("select distinct asm_substation.name " + sqlBody);
        ArrayList<String> rowsValue = database.getValueOfObjects("select distinct asm_substation.name " + sqlBody);
        for (SelenideElement container : containers) {
            String stationName = container.find(byClassName("styles__substation__name___1qVW9")).text();
            if (!container.find(byCssSelector(".styles__substation___1JooU span.styles__substation__adjacent-icon___2U8jP")).exists()) // ненаблюдаемый объект
            {
                container.find(byClassName("styles__substation__fault-icon___2dvY5")).shouldBe(visible);
                container.find(byClassName("styles__substation__events-count-date___1Ayf0")).shouldBe(visible);
                container.find(byClassName("styles__substation__events-count___Sn81X")).shouldBe(visible);
                for (String value : rowsValue) {
                    if (rowsNumber > 0 && value.equals(stationName)) {
                        container.find(byCssSelector("span[class*=substation__triangle-icon_red]")).shouldBe(visible);
                        String malfanctionNumberFromDb = String.valueOf(database.getNumberOfRows("select asm_malfunction_history.id " + sqlBody + " and asm_substation.name = " + "'" + stationName + "'"));
                        String malfanctionNumberFromWeb = container.find(byCssSelector("[class*=substation__redirect-relay]")).text().split(" ")[0];
                        assertThat(malfanctionNumberFromDb, equalTo(malfanctionNumberFromWeb));
                    } else {
                        container.find(byCssSelector("[class*=malfunctions-icon]")).shouldBe(visible);
                        container.find(byCssSelector("[class*=malfunctions-text]")).shouldBe(visible);
                    }
                }
            }
        }
    }

    public void objectsNameCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        anyContainer.shouldBe(visible);
        ArrayList<String> namesFromWeb = new ArrayList<>();
        ArrayList<String> namesFromDb = database.getValueOfObjects("select name from asm_substation");
        for (SelenideElement container : containers) {
            String stationNameFromWeb = container.find(byClassName("styles__substation__name___1qVW9")).shouldBe(visible).text();
            namesFromWeb.add(stationNameFromWeb);
        }
        namesFromWeb.sort(Comparator.naturalOrder());
        namesFromDb.sort(Comparator.naturalOrder());
        assertEquals(namesFromDb, namesFromWeb);
    }

    public void objectTypeCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        anyContainer.shouldBe(visible);
        for (SelenideElement container : containers) {
            String objectName = container.find(byClassName("styles__substation__name___1qVW9")).text();
            String objectTypeFromWeb = container.find(byCssSelector("span.styles__substation__type___2GrAD")).text();
            String objectNameFromDb = database.getValueOfObjects("select type from asm_substation where name = " + "'" + objectName + "'").get(0);
            switch (objectNameFromDb) {
                case "1":
                    assertEquals("Подстанция", objectTypeFromWeb);
                    break;
                case "2":
                    assertEquals("Станция", objectTypeFromWeb);
            }
        }
    }

    public void objectVoltageClassCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        anyContainer.shouldBe(visible);
        for (SelenideElement container : containers) {
            String objectName = container.find(byClassName("styles__substation__name___1qVW9")).text();
            String objectVoltageClassFromWeb = String.valueOf((int) (Double.parseDouble(container.find(byCssSelector("span.styles__substation__voltage___GmCRg")).text().split(" ")[0]) * 1000));
            String objectVoltageClassFromDb = database.getValueOfObjects("select max (asm_base_voltage.nominal) from asm_substation " +
                    "left join asm_switchgear on asm_substation.id = asm_switchgear.substation_id " +
                    "left join asm_base_voltage on asm_switchgear.base_voltage_id = asm_base_voltage.id " +
                    "where asm_substation.name = " + "'" + objectName + "'").get(0);
            assertThat(objectVoltageClassFromDb, equalTo(objectVoltageClassFromWeb));
        }
    }

    public void objectHierarchyCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        anyContainer.shouldBe(visible);
        for (SelenideElement container : containers) {
            ArrayList<String> hierarchyFromWeb = new ArrayList<>();
            String objectName = container.find(byClassName("styles__substation__name___1qVW9")).text();
            String[] hierarchy = container.find(byClassName("styles__substation__info___31rGu")).text().split(" \\| ");
            Collections.addAll(hierarchyFromWeb, hierarchy);
            hierarchyFromWeb.add(0, objectName);
            ArrayList<String> hierarchyFromDB = database.getValuesOfObjects("select asm_substation.name, asm_geographical_region.name, t1.name as owner, t2.name as operator from asm_substation " +
                    "left join asm_geographical_region on asm_substation.geographical_region_id = asm_geographical_region.id " +
                    "left join asm_company as t1 on asm_substation.operator_id = t1.id " +
                    "left join asm_company as t2 on asm_substation.owner_id = t2.id " +
                    "where asm_substation.name = '" + objectName + "'");
            assertThat(hierarchyFromDB, equalTo(hierarchyFromWeb));
        }
    }

    public MainPage lastFaultDateTimeForSubstation(String stationName) throws SQLException {
        Database database = new Database();
        database.connect();
        element(byCssSelector("li.styles__substation___1JooU")).shouldBe(visible);
        SelenideElement station = containers.findBy(text(stationName));
        String lastFaultDateFromWeb = station.find(byCssSelector("span.styles__substation__events-count-date___1Ayf0")).text().split(", ")[0];
        String lastFaultTimeFromWeb = station.find(byCssSelector("span.styles__substation__events-count-date___1Ayf0")).text().split(", ")[1];
        String lastFaultDateFromDb = LocalDate.parse((database.getValueOfObjects("select max (asm_fault.start_time) from asm_substation " +
                "left join asm_substation_line_segment_equivalent on asm_substation_line_segment_equivalent.substation_id = asm_substation.id " +
                "left join asm_line on asm_substation_line_segment_equivalent.line_id = asm_line.id " +
                "left join asm_fault on asm_fault.equipment_id = asm_line.power_equipment_id " +
                "where asm_substation.name = '" + stationName + "'" +
                "group by asm_substation.name").get(0).split(" ")[0]), DateTimeFormatter.ofPattern("yyyy-MM-dd")).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        String t = database.getValueOfObjects("select max (asm_fault.start_time) from asm_substation " +
                "left join asm_substation_line_segment_equivalent on asm_substation_line_segment_equivalent.substation_id = asm_substation.id " +
                "left join asm_line on asm_substation_line_segment_equivalent.line_id = asm_line.id " +
                "left join asm_fault on asm_fault.equipment_id = asm_line.power_equipment_id " +
                "where asm_substation.name = '" + stationName + "'" +
                "group by asm_substation.name").get(0).split(" ")[1];
        String[] split = t.substring(0, t.indexOf("+")).split(":");
        String lastFaultTimeFromDb = (Integer.parseInt(split[0]) + 3) + ":" + split[1] + ":" + split[2];
        String lastFaultDateTimeFromWeb = lastFaultDateFromWeb + " " + lastFaultTimeFromWeb;
        String lastFaultDateTimeFromDb = lastFaultDateFromDb + " " + lastFaultTimeFromDb;
        assertEquals(lastFaultDateTimeFromDb, lastFaultDateTimeFromWeb);
        return this;
    }

    public StationPage goToObjectPage(String stationName) {
        StationPage stationPage = new StationPage();
        anyContainer.shouldBe(visible);
        SelenideElement container = containers.findBy(text(stationName));
        container.find(withText("Перейти к объекту")).click();
        SelenideElement pageHeader = element(byCssSelector("div.styles__header__name-content___3qA1W")).shouldBe(visible);
        assertThat(pageHeader.text(), equalTo(stationName));
        return stationPage;
    }
}
