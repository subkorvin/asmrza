package ru.rtsoft.qa.asmrza.widgets;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.Color;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.*;
import static com.codeborne.selenide.Selenide.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.testng.Assert.assertTrue;


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

    public void checkTracingObjectsAttributes() {
        element(byCssSelector("li.styles__substation___1JooU")).shouldBe(visible);
        ElementsCollection containers = elements(byCssSelector("li.styles__substation___1JooU"));
        for (SelenideElement container : containers) {
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

    public void checkStateBeforeFiltering(String filterName, String[] filterItems) throws SQLException {
        Database database = new Database();
        database.connect();
        element(byCssSelector("li.styles__substation___1JooU")).shouldBe(visible);
        String sqlQuery;
        String sqlQueryNotFiltered;
        String sqlAdd = "";
        String sqlAddVoltage = "";
        for (int i = 0; i < filterItems.length; i++) {
            if (i == filterItems.length - 1) {
                if (!filterName.equals("Класс напряжения")) {
                    sqlAdd = sqlAdd + "'" + filterItems[i] + "'";
                } else {
                    sqlAddVoltage = sqlAddVoltage + (int) (Double.parseDouble(filterItems[i].split(" ")[0]) * 1000);
                }
            } else {
                if (!filterName.equals("Класс напряжения")) {
                    sqlAdd = sqlAdd + "'" + filterItems[i] + "', ";
                } else {
                    sqlAddVoltage = sqlAddVoltage + (int) (Double.parseDouble(filterItems[i].split(" ")[0]) * 1000) + ", ";
                }
            }
        }
        switch (filterName) {
            case "Энергосистема":
                sqlQuery = "select asm_substation.name from asm_substation left join asm_geographical_region on" +
                        " asm_substation.geographical_region_id = asm_geographical_region.id where asm_geographical_region.name in (" + sqlAdd + ")";
//                }
                sqlQueryNotFiltered = "select distinct asm_geographical_region.name from asm_substation left join asm_geographical_region on" +
                        " asm_substation.geographical_region_id = asm_geographical_region.id";
                break;
            case "Диспетчер":
                sqlQuery = "select asm_substation.name from asm_substation left join asm_company on" +
                        " asm_substation.operator_id = asm_company.id where asm_company.name in (" + sqlAdd + ")";
                sqlQueryNotFiltered = "select distinct asm_company.name from asm_substation left join asm_company on" +
                        " asm_substation.operator_id = asm_company.id";
                break;
            case "Собственник":
                sqlQuery = "select asm_substation.name from asm_substation left join asm_company on" +
                        " asm_substation.owner_id = asm_company.id where asm_company.name in (" + sqlAdd + ")";
                sqlQueryNotFiltered = "select distinct asm_company.name from asm_substation left join asm_company on" +
                        " asm_substation.owner_id = asm_company.id";
                break;
            case "Класс напряжения":
                sqlQuery = "select distinct asm_substation.name from asm_substation left join asm_switchgear on" +
                        " asm_substation.id = asm_switchgear.substation_id left join asm_base_voltage on" +
                        " asm_switchgear.base_voltage_id = asm_base_voltage.id where asm_base_voltage.nominal in (" + sqlAddVoltage + ")" + "group by asm_substation.name";
                sqlQueryNotFiltered = "select distinct max (asm_base_voltage.nominal) from asm_substation left join asm_switchgear on" +
                        " asm_substation.id = asm_switchgear.substation_id left join asm_base_voltage on" +
                        " asm_switchgear.base_voltage_id = asm_base_voltage.id group by asm_substation.name";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + filterName);
        }
        int initialNumberItemsFromDb = database.getNumberOfItems(sqlQuery);
        int numberOfNotFilteredItemsFromDb = database.getNumberOfItems(sqlQueryNotFiltered); // для проверки количества объектов в фильтре (Собственник 3 из 3, например)
        int initialNumberItemsFromWeb = 0;
        ElementsCollection initialContainers = elements(byCssSelector("li.styles__substation___1JooU"));
        for (SelenideElement container : initialContainers) {
            String[] itemInfo = container.find(byClassName("styles__substation__info___31rGu")).text().split(" \\| ");
            String itemValue;
            switch (filterName) {
                case "Энергосистема":
                    itemValue = itemInfo[0];
                    break;
                case "Диспетчер":
                    itemValue = itemInfo[1];
                    break;
                case "Собственник":
                    itemValue = itemInfo[2];
                    break;
                case "Класс напряжения":
                    itemValue = container.find(byCssSelector("span.styles__substation__voltage___GmCRg")).text();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + filterName);
            }
            for (String filterItem : filterItems) {
                if (itemValue.equals(filterItem)) {
                    initialNumberItemsFromWeb++;
                }
            }
        }
        assertThat(initialNumberItemsFromDb, equalTo(initialNumberItemsFromWeb));
        System.out.println(initialNumberItemsFromDb + "  " + initialNumberItemsFromWeb);
        assertThat(numberOfNotFilteredItemsFromDb, equalTo(Integer.parseInt(element(withText(filterName))
                .parent().parent().find(byCssSelector("span.styles__checkbox-list__count___2UoT9 span")).text())));
    }

    public void checkStateAfterFiltering(String filterName, String[] filterItems) throws SQLException {
        Database database = new Database();
        database.connect();
        String sqlQuery;
        String sqlAdd = "";
        String sqlAddVoltage = "";
        Boolean flag = true;
        ElementsCollection finalContainers = null;
        for (int i = 0; i < filterItems.length; i++) {
            if (i == filterItems.length - 1) {
                if (!filterName.equals("Класс напряжения")) {
                    sqlAdd = sqlAdd + "'" + filterItems[i] + "'";
                } else {
                    sqlAddVoltage = sqlAddVoltage + (int) (Double.parseDouble(filterItems[i].split(" ")[0]) * 1000);
                }
            } else {
                if (!filterName.equals("Класс напряжения")) {
                    sqlAdd = sqlAdd + "'" + filterItems[i] + "', ";
                } else {
                    sqlAddVoltage = sqlAddVoltage + (int) (Double.parseDouble(filterItems[i].split(" ")[0]) * 1000) + ", ";
                }
            }
        }
        switch (filterName) {
            case "Энергосистема":
                sqlQuery = "select asm_substation.name from asm_substation left join asm_geographical_region on" +
                        " asm_substation.geographical_region_id = asm_geographical_region.id where asm_geographical_region.name not in (" + sqlAdd + ")";
                break;
            case "Диспетчер":
                sqlQuery = "select asm_substation.name from asm_substation left join asm_company on" +
                        " asm_substation.operator_id = asm_company.id where asm_company.name not in (" + sqlAdd + ")";
                break;
            case "Собственник":
                sqlQuery = "select asm_substation.name from asm_substation left join asm_company on" +
                        " asm_substation.owner_id = asm_company.id where asm_company.name not in (" + sqlAdd + ")";
                break;
            case "Класс напряжения":
                sqlQuery = "select distinct asm_substation.name from asm_substation left join asm_switchgear on " +
                        "asm_substation.id = asm_switchgear.substation_id left join asm_base_voltage on" +
                        " asm_switchgear.base_voltage_id = asm_base_voltage.id where asm_base_voltage.nominal = (select max(asm_base_voltage.nominal)" +
                        " from asm_base_voltage) and asm_base_voltage.nominal not in (" + sqlAddVoltage + ") group by asm_substation.name";
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + filterName);
        }
        int finalNumberItemsFromDb = database.getNumberOfItems(sqlQuery);
        if (elements(byCssSelector("li.styles__substation___1JooU")).size() == 0 && finalNumberItemsFromDb == 0) {
            SelenideElement emptyList = element(byCssSelector("span.styles__list-empty___1_drg")).shouldBe(visible);
            assertThat(emptyList.text(), equalTo("Нет подстанций соответствующих текущему состоянию фильтра"));
        } else {
            finalContainers = elements(byCssSelector("li.styles__substation___1JooU"));
            for (SelenideElement container : finalContainers) {
                String[] itemInfo = container.find(byClassName("styles__substation__info___31rGu")).text().split(" \\| ");
                String itemValue;
                switch (filterName) {
                    case "Энергосистема":
                        itemValue = itemInfo[0];
                        break;
                    case "Диспетчер":
                        itemValue = itemInfo[1];
                        break;
                    case "Собственник":
                        itemValue = itemInfo[2];
                        break;
                    case "Класс напряжения":
                        itemValue = container.find(byCssSelector("span.styles__substation__voltage___GmCRg")).text();
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + filterName);
                }
                if (contains(filterItems, itemValue)){
                    flag = false;
                    break;
                }

            }
        }
        assertTrue(flag);
        if (finalNumberItemsFromDb == 0){
            assertNull(finalContainers);
        } else {
            assertThat(finalContainers.size(), equalTo(finalNumberItemsFromDb));
        }
        assertThat(finalNumberItemsFromDb, equalTo(Integer.parseInt(element(withText(filterName))
                .parent().parent().find(byCssSelector("span.styles__checkbox-list__count___2UoT9 span")).text())));
        if (finalNumberItemsFromDb == 0) {
            assertThat(colorToHex(element(withText(filterName)).parent().parent().find(byCssSelector("span.styles__checkbox-list__count___2UoT9 span"))), equalTo("#ff3434"));
        }
    }

    public Boolean contains(String[] filterItems, String itemValue) {
        for (String filterItem : filterItems) {
            if (itemValue.equals(filterItem)) {
                return true;
            }
        }
        return false;
    }


    protected void findingAndAssert(Database database, SelenideElement containerElement, String sqlQuery) throws SQLException {
        ArrayList<String> nameOfElementsFromDb = database.getNameOfObjects(sqlQuery);
        nameOfElementsFromDb.sort(Comparator.naturalOrder());
        ArrayList<String> nameOfElementsFromWeb = new ArrayList<>();
        SelenideElement parentElement = containerElement.parent().parent().parent().parent();
        parentElement.click();
        ElementsCollection contents = parentElement.findAll(byCssSelector("label[class=styles__checkbox__content___3_xkR]"));
        for (SelenideElement content : contents) {
            nameOfElementsFromWeb.add(content.text());
        }
        nameOfElementsFromWeb.sort(Comparator.naturalOrder());
        assertThat(nameOfElementsFromDb, equalTo(nameOfElementsFromWeb));
        parentElement.click();
    }

    protected void findingUniqueAndAssert(Database database, SelenideElement containerElement, String sqlQuery) throws SQLException {
        ArrayList<String> s = database.getNameOfObjects(sqlQuery);
        HashSet<String> ss = new HashSet<>(s);
        ArrayList<String> nameOfElementsFromDb = new ArrayList<>(ss);
        ArrayList<String> nameOfElementsFromWeb = new ArrayList<>();
        SelenideElement parentElement = containerElement.parent().parent().parent().parent();
        parentElement.click();
        ElementsCollection contents = parentElement.findAll(byCssSelector("label[class=styles__checkbox__content___3_xkR]"));
        for (SelenideElement content : contents) {
            nameOfElementsFromWeb.add(String.valueOf(Integer.valueOf((int) Double.parseDouble(content.text().substring(0, 3)) * 1000)));
        }
        assertThat(nameOfElementsFromDb, equalTo(nameOfElementsFromWeb));
        parentElement.click();
    }
}