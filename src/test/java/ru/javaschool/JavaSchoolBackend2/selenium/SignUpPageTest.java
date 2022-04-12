package ru.javaschool.JavaSchoolBackend2.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.javaschool.JavaSchoolBackend2.enums.Position;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SignUpPageTest {

    private static ChromeDriver chromeDriver;

    @BeforeAll
    static void init() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\marus\\Desktop\\JavaSchoolBackend2\\src\\main\\resources\\chromedriver.exe");
        chromeDriver = new ChromeDriver();
        chromeDriver.get("http://localhost:3000/registration");
    }

    @AfterAll
    static void close() {
        chromeDriver.quit();
    }

    @BeforeEach
    void clearForm() {
        WebDriverWait wait1 = new WebDriverWait(chromeDriver, 10);
        WebElement login = wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder= \'login\']")));
        login.clear();
        chromeDriver.findElement(By.xpath("//input[@placeholder= \'password\']")).clear();
        chromeDriver.findElement(By.xpath("/html/body/div/div/div/form/select")).sendKeys("");

    }




    @Test
    @DisplayName("Test correct user data")
    void correctUserDataForNurse() {
        fillForm("test", "test", Position.NURSE);
        WebDriverWait wait1 = new WebDriverWait(chromeDriver, 10);
        boolean isRedirect = wait1.until(ExpectedConditions.urlToBe("http://localhost:3000/"));
        if(isRedirect) {
            chromeDriver.get("http://localhost:3000/registration");
        }
        assertTrue(isRedirect);
    }


    @Test
    @DisplayName("Test already exist user")
    void incorrectPasswordTest() {
        fillForm("doctor", "doc", Position.NURSE);
        String actual = getAnswer();
        String expected = "can't save users with equal names";
        assertEquals(expected, actual);
    }


    void fillForm(String name, String password, Position position) {
        WebDriverWait wait1 = new WebDriverWait(chromeDriver, 10);
        WebElement login = wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder= \'login\']")));
        if (name != null) {
            login.sendKeys(name);
        }
        chromeDriver.findElement(By.xpath("//input[@placeholder= \'password\']")).sendKeys(password);
        WebElement select = chromeDriver.findElement(By.xpath("/html/body/div/div/div/form/select"));
     //   Select select = new Select(chromeDriver.findElement(By.id("/html/body/div/div/div/form/select")));
        if(position == Position.DOCTOR) {
            select.sendKeys("Doctor");
        //    WebElement doctor = wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div/form/select/option[2]")));
        //    doctor.sendKeys(Keys.ENTER);
        }
        else if(position == Position.NURSE) {
            select.sendKeys("Nurse");
        }
        chromeDriver.findElement(By.xpath("/html/body/div/div/div/form/p/input")).sendKeys(Keys.ENTER);
    }

    String getAnswer() {
        WebDriverWait wait1 = new WebDriverWait(chromeDriver, 10);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/form/p[1]")));
        String answer = chromeDriver.findElement(By.xpath("/html/body/div/div/div/form/p[1]")).getText();
        return answer;
    }
}
