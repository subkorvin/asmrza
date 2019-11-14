package ru.rtsoft.qa.asmrza;

import com.codeborne.selenide.SelenideElement;
import org.junit.Before;
import org.junit.Test;
import ru.rtsoft.qa.asmrza.testconfigs.BaseTest;
import ru.rtsoft.qa.asmrza.widgets.Database;
import ru.rtsoft.qa.asmrza.widgets.Page;

import java.sql.Connection;
import java.sql.SQLException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selenide.element;

public class AsmrzaMainPageTests extends BaseTest {

    @Before
    public void loginAsAdmin() {
        Page loginPage = new Page();
        loginPage.open();
        loginPage.enterData("admin", "qwerty123");
        SelenideElement loginButton = loginPage.getSelenideElement();
        loginButton.click();
        element(byClassName("styles__main__title___if37D")).shouldBe(visible).shouldBe(text("Наблюдаемые объекты"));
    }

    @Test
    public void commonElementsPresentCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        int itemsFromDb = database.getNumberOfItems("select * from asm_substation");
    }
}
