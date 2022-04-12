package ru.javaschool.JavaSchoolBackend2.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.jupiter.api.Assertions.*;

public class SignInPageTest {

    private static ChromeDriver chromeDriver;

    @BeforeAll
    public static void init() {
        System.setProperty("webdriver.chrome.driver","C:\\Users\\marus\\Desktop\\JavaSchoolBackend2\\src\\main\\resources\\chromedriver.exe");
        chromeDriver = new ChromeDriver();
        chromeDriver.get("http://localhost:3000");
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
    }

    @Test
    @DisplayName("Test incorrect user data")
    void incorrectDataTest() {
        fillForm("task", "task");
        String actual = getAnswer();
        String expected = "Login failed";
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Test correct doctor's user data")
    void correctUserDataForDoctor() {
        fillForm("doctor", "doctor");
        WebDriverWait wait1 = new WebDriverWait(chromeDriver, 10);
        boolean isRedirect = wait1.until(ExpectedConditions.urlToBe("http://localhost:3000/doctor_main_page"));
        if(isRedirect) {
            chromeDriver.findElement(By.xpath("/html/body/div/div/div[1]/nav/ul/li/a")).sendKeys(Keys.ENTER);
        }
        assertTrue(isRedirect);
    }


    @Test
    @DisplayName("Test correct nurse user data")
    void correctUserDataForNurse() {
        fillForm("nurse", "nurse");
        WebDriverWait wait1 = new WebDriverWait(chromeDriver, 10);
        boolean isRedirect = wait1.until(ExpectedConditions.urlToBe("http://localhost:3000/nurse_main_page"));
        if(isRedirect) {
            chromeDriver.findElement(By.xpath("/html/body/div/div/div[1]/nav/ul/li/a")).sendKeys(Keys.ENTER);
        }
        assertTrue(isRedirect);
    }


   @Test
    @DisplayName("Test incorrect password")
    void incorrectPasswordTest() {
        fillForm("doctor", "doc");
        String actual = getAnswer();
        String expected = "Wrong password";
        assertEquals(expected, actual);
    }


    void fillForm(String name, String password) {
        WebDriverWait wait1 = new WebDriverWait(chromeDriver, 10);
        WebElement login = wait1.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder= \'login\']")));
        login.sendKeys(name);
        chromeDriver.findElement(By.xpath("//input[@placeholder= \'password\']")).sendKeys(password);
        chromeDriver.findElement(By.xpath("/html/body/div/div/div/form/p/input")).sendKeys(Keys.ENTER);
    }

    String getAnswer() {
        WebDriverWait wait1 = new WebDriverWait(chromeDriver, 10);
        wait1.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/form/p/p")));
        String answer = chromeDriver.findElement(By.xpath("/html/body/div/div/div/form/p/p")).getText();
        return answer;
    }

}
