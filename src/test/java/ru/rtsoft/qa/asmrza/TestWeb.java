package ru.rtsoft.qa.asmrza;

import com.codeborne.selenide.SelenideElement;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.rtsoft.qa.asmrza.testconfigs.BaseTest;
import ru.rtsoft.qa.asmrza.widgets.LoginPage;
import ru.rtsoft.qa.asmrza.widgets.Page;

import static com.codeborne.selenide.Selectors.byClassName;
import static com.codeborne.selenide.Selectors.byCssSelector;
import static com.codeborne.selenide.Selenide.element;

public class TestWeb extends BaseTest {

    @BeforeClass()
    public static void loginAsAdmin() {
        LoginPage loginPage = new LoginPage();
        loginPage.open();
        loginPage.enterData("admin", "qwerty123");
        SelenideElement loginButton = loginPage.loginButton();
        loginButton.click();
        loginPage.correctLoginCheck();
    }

    @Test
    public void check() {
        Page mainPage = new Page();
        String stationInfo = element(byCssSelector("li.styles__substation___1JooU")).find(byClassName("styles__substation__info___31rGu")).text();
        String[] stations = stationInfo.split(" \\| ");
        for (int i=0; i < stations.length; i++){
            System.out.print(stations[i] + "\n");
        }
//        System.out.printf(stationInfo.length() + "\n");
//        for (int i = 0; i < stationInfo.length(); i++){
//            if (String.valueOf(stationInfo.charAt(i)).equals("|")){
//                int index1 = i;
//                System.out.printf(index1 + "\n");
//            }
//        }
    }
}
