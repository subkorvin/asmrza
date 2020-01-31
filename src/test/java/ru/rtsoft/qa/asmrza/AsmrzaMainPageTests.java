package ru.rtsoft.qa.asmrza;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.rtsoft.qa.asmrza.testconfigs.BaseTest;
import ru.rtsoft.qa.asmrza.widgets.*;

import java.sql.SQLException;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;


public class AsmrzaMainPageTests extends BaseTest {

    @BeforeClass()
    public static void loginAsAdmin() {
        new LoginPage()
                .open()
                .enterData("admin", "qwerty123")
                .loginButton().click();
        new MainPage().correctLoginCheck();
    }


    @Test
    public void energoObjectsPresentCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        int itemsFromDb = database.getNumberOfRows("select * from asm_substation");
        MainPage mainPage = new MainPage();
        int itemsFromWeb = mainPage.getNumberOfEnergoObjects();
        assertThat(itemsFromDb, equalTo(itemsFromWeb));
    }

    @Test
    public void faultPresentCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        int itemsFromDb = database.getNumberOfRows("select * from asm_fault");
        MainPage mainPage = new MainPage();
        int itemsFromWeb = mainPage.getNumberOfFaults();
        assertThat(itemsFromDb, equalTo(itemsFromWeb));
    }

    @Test
    public void tracingEnegroObjectsCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        int itemsFormDb = database.getNumberOfRows("select * from asm_substation where in_service = true");
        MainPage mainPage = new MainPage();
        int itemsFromWeb = mainPage.getNumberOfTracingObjects();
        mainPage.checkTracingObjectsAttributes();
        assertThat(itemsFormDb, equalTo(itemsFromWeb));
    }

    @Test
    public void tracingEnergoObjectsMatching() throws SQLException {
        Database database = new Database();
        database.connect();
        List<String> itemsFromDb = database.getValueOfObjects("select name from asm_substation where in_service = true");
        MainPage mainPage = new MainPage();
        List<String> itemsFromWeb = mainPage.getNameOfObjects();
        assertThat(itemsFromDb, equalTo(itemsFromWeb));
    }

    @Test
    public void nonTracingEnergoObjectsCheck() throws SQLException {
        Database database = new Database();
        database.connect();
        int itemsFromDb = database.getNumberOfRows("select * from asm_substation where in_service = false");
        MainPage mainPage = new MainPage();
        int itemsFromWeb = mainPage.getNumberOfNonTracingObjects();
        mainPage.checkNonTracingObjectsAttributes();
        assertThat(itemsFromDb, equalTo(itemsFromWeb));
    }

    @Test
    public void objectsButtonCheck() {
        new MainPage().checkGotoButtonPresence();
    }

    @Test
    public void commonLinksTest() {
        new MainPage()
                .checkArchiveLink()
                .checkJournalLink()
                .checkUserLink()
                .exitLink();
        loginAsAdmin();
    }

    @Test
    public void rightPanelCheck() {
        new MainPage().panelPresenceCheck();
    }

    @Test
    public void filterControlsCheck() throws SQLException {
        new MainPage()
                .filterControlsPresenceCheck()
                .filterControlsContentCheck();
    }

    @Test
    public void filteringCheck() throws SQLException {
        Page mainPage = new Page();
        Filter filter = new Filter();
        String filterName = "Энергосистема";
        String[] filterBy = {"ЭС Юга"};
        mainPage.checkStateBeforeFiltering(filterName, filterBy);
        filter.openDropDownMenuFor(filterName)
                .filteringBy(filterName, filterBy);
        mainPage.checkStateAfterFiltering(filterName, filterBy);
        filter.dropFilters();
        filterName = "Диспетчер";
        filterBy = new String[]{"Кубанское РДУ"};
        mainPage.checkStateBeforeFiltering(filterName, filterBy);
        filter.openDropDownMenuFor(filterName)
                .filteringBy(filterName, filterBy);
        mainPage.checkStateAfterFiltering(filterName, filterBy);
        filter.dropFilters();
        filterName = "Собственник";
        filterBy = new String[]{"Кубанское ПМЭС", "ОАО «Сочинская ТЭС»"};
        mainPage.checkStateBeforeFiltering(filterName, filterBy);
        filter.openDropDownMenuFor(filterName)
                .filteringBy(filterName, filterBy);
        mainPage.checkStateAfterFiltering(filterName, filterBy);
        filter.dropFilters();
        filterName = "Класс напряжения";
        filterBy = new String[]{"220 кВ"};
        mainPage.checkStateBeforeFiltering(filterName, filterBy);
        filter.openDropDownMenuFor(filterName)
                .filteringBy(filterName, filterBy);
        mainPage.checkStateAfterFiltering(filterName, filterBy);
        filter.dropFilters();
    }

    @Test
    public void objectsNameCheck() throws SQLException {
        new MainPage().objectsNameCheck();
    }

    @Test
    public void objectsTypeCheck() throws SQLException {
        new MainPage().objectTypeCheck();
    }

    @Test
    public void objectVoltageClassCheck() throws SQLException {
        new MainPage().objectVoltageClassCheck();
    }

    @Test
    public void objectHierarchyCheck() throws SQLException {
        new MainPage().objectHierarchyCheck();
    }

    @Test
    public void lastFaultDateTimeCheck() throws SQLException {
        String stationName = "РП 220 кВ Черноморская";
        String lastDateTimeBeforeAdding = MainPage.lastFaultDateTimeForSubstation(stationName);
        StationPage stationPage = new MainPage().goToObjectPage(stationName);
        FaultDialog newFault = stationPage.gotoFaults().newFaultDialogOpen();
        String status = "Расследование";
        String switchgear = "КРУЭ 220 кВ ПС Черноморская";
        String equipment = "КВЛ 220 кВ Черноморская - Поселковая 2ц";
        int endTimeShift = 1;
        String phase = "AC";
        String distance = "10.05";
        newFault.addNewFault().enterValues(status, stationName, switchgear, equipment, endTimeShift, phase, distance);
        MainPage mainPage = Page.goHome();
        mainPage.lastDateTimeComparing(lastDateTimeBeforeAdding, stationName);
    }

    @Test
    public void goToObjectPage() {
        String stationName = "РП 220 кВ Черноморская";
        new MainPage().goToObjectPage(stationName);
    }

    @Test
    public void notificationWindowCheck(){
        MainPage mainPage = new MainPage();
        mainPage.checkNotificationWindowPresence().checkTabsPresence();
        String startDateTime = mainPage.newFaultMaking()[0];
        mainPage.checkNewFaultCardPresence(startDateTime);
        String stationName = "РП 220 кВ Черноморская";
        StationPage stationPage = new MainPage().goToObjectPage(stationName);
        String equipment = "КВЛ 220 кВ Черноморская - Поселковая";
        String relayName = "РЗ1 Черноморская – Поселковая 1 на РП Черноморская";
        stationPage.goToEquipment(stationName, equipment);
        RelayPage relayPage = stationPage.gotoRelay(relayName);
        relayPage.gotoSwitchTab().revertToInitialState().setSwitchers();
    }
}
