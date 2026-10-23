package vn.edu.lab.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class StudentFormPage {
    public static final String URL = "https://demoqa.com/automation-practice-form";

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By firstName = By.id("firstName");
    private final By lastName = By.id("lastName");
    private final By email = By.id("userEmail");
    private final By mobile = By.id("userNumber");
    private final By dateInput = By.id("dateOfBirthInput");
    private final By subjectInput = By.id("subjectsInput");
    private final By uploadInput = By.id("uploadPicture");
    private final By currentAddress = By.id("currentAddress");
    private final By submitButton = By.id("submit");
    private final By confirmationTitle = By.id("example-modal-sizes-title-lg");
    private final By confirmationRows = By.cssSelector(".table-responsive tbody tr");

    public StudentFormPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void open() {
        driver.get(URL);
        wait.until(ExpectedConditions.urlContains("demoqa.com/automation-practice-form"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstName));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public void enterName(String first, String last) {
        type(firstName, first);
        type(lastName, last);
    }

    public void enterEmail(String value) {
        type(email, value);
    }

    public void chooseGender(String gender) {
        String normalized = gender.trim().toLowerCase();
        String id = switch (normalized) {
            case "male" -> "gender-radio-1";
            case "female" -> "gender-radio-2";
            case "other" -> "gender-radio-3";
            default -> throw new IllegalArgumentException("Gender must be Male, Female, or Other");
        };
        click(By.cssSelector("label[for='" + id + "']"));
    }

    public void enterMobile(String value) {
        type(mobile, value);
    }

    public void chooseDateOfBirth(LocalDate date) {
        click(dateInput);
        WebElement monthSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".react-datepicker__month-select")));
        new Select(monthSelect).selectByValue(String.valueOf(date.getMonthValue() - 1));

        WebElement yearSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".react-datepicker__year-select")));
        new Select(yearSelect).selectByValue(String.valueOf(date.getYear()));

        String day = String.valueOf(date.getDayOfMonth());
        By dayLocator = By.xpath("//div[contains(@class,'react-datepicker__day')"
                + " and not(contains(@class,'outside-month'))"
                + " and normalize-space(text())='" + day + "']");
        wait.until(ExpectedConditions.elementToBeClickable(dayLocator)).click();

        String expected = date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"));
        wait.until(d -> expected.equals(d.findElement(dateInput).getAttribute("value")));
    }

    public String getDateOfBirthValue() {
        return driver.findElement(dateInput).getAttribute("value");
    }

    public void addSubject(String subject) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(subjectInput));
        input.sendKeys(subject);
        By option = By.xpath("//div[contains(@class,'subjects-auto-complete__option')"
                + " and normalize-space()='" + subject + "']");
        wait.until(ExpectedConditions.elementToBeClickable(option)).click();
    }

    public void chooseHobby(String hobby) {
        String id = switch (hobby.trim().toLowerCase()) {
            case "sports" -> "hobbies-checkbox-1";
            case "reading" -> "hobbies-checkbox-2";
            case "music" -> "hobbies-checkbox-3";
            default -> throw new IllegalArgumentException("Hobby must be Sports, Reading, or Music");
        };
        click(By.cssSelector("label[for='" + id + "']"));
    }

    public void enterAddress(String address) {
        type(currentAddress, address);
    }

    public void uploadFile(Path file) {
        driver.findElement(uploadInput).sendKeys(file.toAbsolutePath().toString());
        wait.until(d -> {
            String value = d.findElement(By.id("uploadedFilePath")).getText();
            return value != null && !value.isBlank();
        });
    }

    public String getUploadedFileName() {
        return driver.findElement(By.id("uploadedFilePath")).getText();
    }

    public void capturePreSubmitState() {
        wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        scrollTo(submitButton);
    }

    public void submit() {
        WebElement button = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        scrollTo(submitButton);
        try {
            button.click();
        } catch (Exception e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationTitle));
    }

    public String getConfirmationTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationTitle)).getText();
    }

    public List<String> getConfirmationRows() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmationTitle));
        return driver.findElements(confirmationRows).stream()
                .map(WebElement::getText)
                .toList();
    }

    private void type(By locator, String value) {
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        element.clear();
        element.sendKeys(value);
    }

    private void click(By locator) {
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
        scrollTo(locator);
        element.click();
    }

    private void scrollTo(By locator) {
        WebElement element = driver.findElement(locator);
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", element);
    }
}
