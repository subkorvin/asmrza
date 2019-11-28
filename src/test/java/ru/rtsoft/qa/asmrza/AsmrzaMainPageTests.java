package ru.rtsoft.qa.asmrza;

import com.codeborne.selenide.SelenideElement;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.rtsoft.qa.asmrza.testconfigs.BaseTest;
import ru.rtsoft.qa.asmrza.widgets.Database;
import ru.rtsoft.qa.asmrza.widgets.Page;
import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;



public class AsmrzaMainPageTests extends BaseTest {

    @BeforeClass()
    public static void loginAsAdmin() {
        Page loginPage = new Page();
        loginPage.open();
        loginPage.enterData("admin", "qwerty123");
        SelenideElement loginButton = loginPage.getSelenideElement();
        loginButton.click();
        loginPage.correctLoginCheck();
    }


    @Test
    public void energoObjectsPresentCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        int itemsFromDb = database.getNumberOfItems("select * from asm_substation");
        Page mainPage = new Page();
        int itemsFromWeb = mainPage.getNumberOfEnergoObjects();
        assertThat(itemsFromDb, equalTo(itemsFromWeb));
    }

    @Test
    public void faultPresentCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        int itemsFromDb = database.getNumberOfItems("select * from asm_fault");
        Page mainPage = new Page();
        int itemsFromWeb = mainPage.getNumberOfFaults();
        assertThat(itemsFromDb, equalTo(itemsFromWeb));
    }

    @Test
    public void tracingEnegroObjectsCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        int itemsFormDb = database.getNumberOfItems("select * from asm_substation where in_service = true");
        Page mainPage = new Page();
        int itemsFromWeb = mainPage.getNumberOfTracingObjects();
        mainPage.checkTracingObjectsAttributes();
        assertThat(itemsFormDb, equalTo(itemsFromWeb));
    }

    @Test
    public void tracingEnergoObjectsMatching() throws SQLException {
        Database database = new Database();
        database.connect();
        List<String> itemsFromDb = database.getNameOfObjects("select name from asm_substation where in_service = true");
        Page mainPage = new Page();
        List<String> itemsFromWeb = mainPage.getNameOfObjects();
        assertThat(itemsFromDb, equalTo(itemsFromWeb));
    }

    @Test
    public void nonTracingEnergoObjectsCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        int itemsFromDb = database.getNumberOfItems("select * from asm_substation where in_service = false");
        Page mainPage = new Page();
        int itemsFromWeb = mainPage.getNumberOfNonTracingObjects();
        mainPage.checkNonTracingObjectsAttributes();
        assertThat(itemsFromDb, equalTo(itemsFromWeb));
    }

    @Test
    public void objectsButtonCheck(){
        Page mainPage = new Page();
        mainPage.checkGotoButtonPresence();
    }

    @Test
    public void commonLinksTest() {
        Page mainPage = new Page();
        mainPage.checkArchiveLink();
        mainPage.checkJournalLink();
        mainPage.checkUserLink();
        mainPage.exitLink();
        loginAsAdmin();
    }

    @Test
    public void rightPanelCheck(){
        Page mainPage = new Page();
        mainPage.panelPresenceCheck();
    }

    @Test
    public void filterControlsCheck() throws SQLException {
        Page mainPage = new Page();
        mainPage.filterControlsPresenceCheck();
        mainPage.filterControlsContentCheck();
    }
}
