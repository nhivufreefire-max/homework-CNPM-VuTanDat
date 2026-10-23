package vn.edu.lab.tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import vn.edu.lab.pages.StudentFormPage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;

class StudentFormTest {
    private WebDriver driver;
    private StudentFormPage formPage;

    @ParameterizedTest(name = "Đăng ký sinh viên: {0} {1}")
    @CsvSource({
            "An, Nguyen, an.nguyen.lab@example.com, 0912345678, Male, Sports",
            "Binh, Tran, binh.tran.lab@example.com, 0987654321, Female, Reading",
            "Chi, Le, chi.le.lab@example.com, 0901122334, Other, Music"
    })
    void testRegisterStudent(String firstName, String lastName, String email,
                             String mobile, String gender, String hobby) throws Exception {
        EdgeOptions options = new EdgeOptions();
        // Nếu máy phòng lab có chính sách riêng, có thể bỏ dòng dưới.
        options.addArguments("--start-maximized");
        driver = new EdgeDriver(options);
        formPage = new StudentFormPage(driver);

        try {
            formPage.open();

            // Explicit wait #1 nằm trong open(): đợi URL và ô First Name xuất hiện.
            Assertions.assertTrue(formPage.getCurrentUrl().contains("demoqa.com/automation-practice-form"),
                    "URL phải trỏ đến trang Practice Form");
            Assertions.assertFalse(formPage.getTitle().isBlank(), "Tiêu đề trang không được rỗng");

            formPage.enterName(firstName, lastName);
            formPage.enterEmail(email);
            formPage.enterMobile(mobile);
            formPage.chooseGender(gender);
            formPage.chooseDateOfBirth(LocalDate.of(2002, 5, 15));
            formPage.addSubject("Maths");
            formPage.chooseHobby(hobby);
            formPage.enterAddress("Lab 06 Selenium - " + firstName);

            Path uploadFile = Path.of("src", "test", "resources", "upload-sample.txt").toAbsolutePath();
            formPage.uploadFile(uploadFile);

            System.out.println("Đang kiểm thử: " + firstName + " " + lastName
                    + " | " + email + " | " + mobile + " | " + gender + " | " + hobby);
            System.out.println("Ngày sinh hiển thị: " + formPage.getDateOfBirthValue());
            System.out.println("Tệp upload: " + formPage.getUploadedFileName());

            captureScreenshot("before_submit_" + firstName);
            formPage.capturePreSubmitState();
            formPage.submit(); // Explicit wait #2: đợi modal xác nhận xuất hiện.

            String confirmationTitle = formPage.getConfirmationTitle();
            var rows = formPage.getConfirmationRows();

            Assertions.assertEquals("Thanks for submitting the form", confirmationTitle);
            Assertions.assertTrue(rows.stream().anyMatch(row -> row.contains(firstName + " " + lastName)),
                    "Modal phải hiển thị họ tên đã nhập");
            Assertions.assertTrue(rows.stream().anyMatch(row -> row.contains(email)),
                    "Modal phải hiển thị email đã nhập");
            Assertions.assertTrue(rows.stream().anyMatch(row -> row.contains(mobile)),
                    "Modal phải hiển thị số điện thoại đã nhập");

            captureScreenshot("after_submit_" + firstName);
            System.out.println("KẾT QUẢ: PASS | " + firstName + " " + lastName);
            System.out.println("Thông báo: " + confirmationTitle);
            System.out.println("Dữ liệu xác nhận: " + rows);
        } catch (Exception | AssertionError e) {
            try {
                captureScreenshot("error_" + firstName);
            } catch (Exception screenshotError) {
                System.err.println("Không chụp được screenshot lỗi: " + screenshotError.getMessage());
            }
            System.err.println("KẾT QUẢ: FAIL | " + firstName + " " + lastName + " | " + e.getMessage());
            throw e;
        }
    }

    private void captureScreenshot(String prefix) throws IOException {
        if (driver == null) return;
        File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
        File dest = new File("screenshots/" + prefix + "_" + time + ".png");
        FileUtils.copyFile(src, dest);
        System.out.println("Đã lưu screenshot: " + dest.getAbsolutePath());
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }
}
