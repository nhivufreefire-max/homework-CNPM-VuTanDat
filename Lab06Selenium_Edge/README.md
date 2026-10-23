# Lab 06 Selenium - Microsoft Edge

Project Maven hoàn chỉnh theo yêu cầu trong `Lab_06.pdf`, sử dụng Microsoft Edge thay vì Chrome theo môi trường máy của bạn.

## Nội dung đáp ứng
- Maven + Selenium WebDriver + JUnit 5.
- Kiểm tra URL và tiêu đề trang.
- Điền họ tên, email, số điện thoại.
- Chọn ngày sinh bằng date picker.
- Chọn giới tính, môn học, sở thích.
- Upload file mẫu từ `src/test/resources/upload-sample.txt`.
- Submit và xác nhận modal/dữ liệu.
- Explicit wait ở nhiều bước.
- Chụp ảnh trước submit, sau submit; chụp ảnh khi lỗi.
- Page Object Model: locator và thao tác UI nằm trong `StudentFormPage`.
- 3 bộ dữ liệu kiểm thử.

## Yêu cầu
- JDK 17 trở lên (project compile target là Java 17).
- Microsoft Edge đã được cài.
- Kết nối Internet tới `https://demoqa.com/automation-practice-form`.
- Maven.

Selenium Manager (đi kèm Selenium 4.38.0) sẽ cố gắng tự tìm/tải Edge WebDriver. Mạng trường hoặc proxy có thể chặn trang DemoQA hay tải driver.

## Chạy trong PowerShell
Mở terminal tại thư mục project, rồi chạy:

```powershell
mvn clean test
```

Nếu `mvn` chưa có trong PATH, dùng đường dẫn Maven theo máy của bạn:

```powershell
& "C:\Users\Admin\Downloads\apache-maven-3.10.0-bin\apache-maven-3.10.0\bin\mvn.cmd" clean test
```

Nếu `JAVA_HOME` chưa được cấu hình trong terminal hiện tại, đường dẫn Java runtime Eclipse trên máy bạn trước đây là:

```powershell
$env:JAVA_HOME = "C:\Users\Admin\.p2\pool\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.12.v20260826-1216\jre"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
```

Chỉ dùng các dòng này nếu đường dẫn còn tồn tại trên đúng máy đó. Đường dẫn trên là cấu hình tạm thời cho terminal hiện tại.

## Kết quả
- Ảnh chụp được lưu trong thư mục `screenshots/` ở thư mục project.
- Báo cáo Surefire nằm tại `target/surefire-reports/`.

## Lưu ý mạng
Lỗi `net::ERR_INTERNET_DISCONNECTED` không phải lỗi Maven. Bài test cần mở được trang DemoQA trong Edge. Hãy thử mở URL bằng Edge trước khi chạy test.
