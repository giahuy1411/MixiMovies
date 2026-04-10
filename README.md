# 🎬 MixiMovies — Web Xem Phim Trực Tuyến

MixiMovies là một ứng dụng web xem phim hiện đại được xây dựng trên nền tảng Java EE. Dự án tập trung vào trải nghiệm người dùng với giao diện tối (Dark Mode) sang trọng, tính năng tương tác cao và quản trị nội dung thông minh thông qua việc tích hợp API phim tự động.

## ✨ Tính năng nổi bật

### 👤 Dành cho người dùng
- **Khám phá**: Xem danh sách phim mới, lọc phim theo thể loại và tìm kiếm thông minh (Live Search).
- **Trình phát phim**: Xem phim chất lượng HD với trình phát mượt mà, đầy đủ thông tin đạo diễn, diễn viên.
- **Tương tác**: Bình luận, đánh giá phim và chia sẻ phim qua Email.
- **Cá nhân hóa**: Lưu danh mục phim yêu thích vào hồ sơ cá nhân.
- **Bảo mật**: Hệ thống đăng ký/đăng nhập an toàn, khôi phục mật khẩu qua mã OTP gửi tới Email.

### 🛡️ Dành cho Quản trị viên (Admin)
- **Dashboard**: Thống kê tổng quan về lượt xem, số lượng phim và người dùng bằng biểu đồ trực quan.
- **Quản lý Phim**: Thêm phim "siêu tốc" chỉ bằng 1 Click thông qua Slug từ KKPhim (tự động lấy toàn bộ thông tin và danh sách tập).
- **Quản lý Thể loại**: Tùy chỉnh danh mục hiển thị trên thanh Menu.
- **Quản lý Người dùng**: Khóa/mở khóa tài khoản hoặc thay đổi quyền quản trị.

---

## 🛠️ Công nghệ sử dụng

- **Backend**: Java Servlet, JSP, JSTL.
- **Database**: Microsoft SQL Server.
- **ORM**: JPA (Hibernate).
- **Build Tool**: Maven.
- **Security**: SHA-256 Hashing, Admin Filter, CSRF Protection, XSS Mitigation (`<c:out>`).
- **Styling**: Vanilla CSS (Custom Design System), Font Awesome icons, Google Fonts (Inter).

---

## 🚀 Hướng dẫn triển khai

### 1. Chuẩn bị cơ sở dữ liệu
- Tạo database mới có tên là `MovieDB` trong SQL Server.
- Cấu hình tài khoản truy cập trong file `src/main/resources/META-INF/persistence.xml`:
  ```xml
  <property name="javax.persistence.jdbc.user" value="huy"/>
  <property name="javax.persistence.jdbc.password" value="141108"/>
  ```
- Hệ thống sẽ tự động tạo bảng (table) thông qua cấu hình `hibernate.hbm2ddl.auto = update`.

### 2. Cấu hình Email (SMTP)
- Để sử dụng tính năng gửi OTP và chia sẻ phim, bạn cần cấu hình tài khoản Gmail trong class `utils.Mailer`.
- Sử dụng "Mật khẩu ứng dụng" (App Password) của Google để đảm bảo tính bảo mật.

### 3. Build và Chạy ứng dụng
- Sử dụng lệnh Maven để đóng gói:
  ```bash
  mvn clean package
  ```
- Copy file `target/miximovies.war` vào thư mục `webapps` của Apache Tomcat.
- Truy cập địa chỉ: `http://localhost:8080/miximovies/home`

---

## 📖 Hướng dẫn sử dụng Admin

1.  **Đăng nhập Admin**: Tài khoản cần có trường `admin = true` trong cơ sở dữ liệu.
2.  **Thêm phim mới**:
    - Truy cập **Dashboard** -> **Quản lý Video**.
    - Nhấn **Thêm phim qua Slug**.
    - Lấy Slug từ trang KKPhim (ví dụ: `ngoi-truong-xac-song`) và dán vào. Hệ thống sẽ tự lấy toàn bộ dữ liệu.
3.  **Thống kê**: Kiểm tra biểu đồ phân tích lượt xem theo thể loại để biết xu hướng người dùng.

---
*© 2025 MixiMovies — Phát triển bởi giahuy1411*
