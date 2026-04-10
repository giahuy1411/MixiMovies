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

### Yêu cầu hệ thống
- **JDK**: 17+
- **Apache Tomcat**: 8.5 hoặc 9.x
- **SQL Server**: 2019+
- **Maven**: 3.8+

### Bước 1: Clone dự án
```bash
git clone https://github.com/giahuy1411/MixiMovies.git
cd MixiMovies
```

### Bước 2: Cấu hình thông tin nhạy cảm
> ⚠️ **Quan trọng**: Dự án sử dụng file `env.properties` để bảo vệ thông tin nhạy cảm. File này **không được push lên Git**.

```bash
# Copy file mẫu thành file cấu hình thật
cp src/main/resources/env.example.properties src/main/resources/env.properties
```

Mở file `src/main/resources/env.properties` và điền thông tin thật của bạn:
```properties
# Database (SQL Server)
DB_URL=jdbc:sqlserver://localhost:1433;databaseName=MovieDB;encrypt=true;trustServerCertificate=true
DB_USER=sa
DB_PASSWORD=your_password_here

# Email (Gmail SMTP - dùng App Password)
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=xxxx xxxx xxxx xxxx
```

### Bước 3: Tạo Database
- Mở SQL Server Management Studio (SSMS).
- Tạo database mới có tên: `MovieDB`.
- **Không cần tạo bảng** — Hibernate sẽ tự động tạo bảng khi chạy lần đầu (`hbm2ddl.auto = update`).

### Bước 4: Build và Deploy
```bash
mvn clean package
```
Copy file `target/miximovies.war` vào thư mục `webapps` của Apache Tomcat, sau đó khởi động Tomcat.

### Bước 5: Truy cập
```
http://localhost:8080/miximovies/home
```

---

## 📖 Hướng dẫn sử dụng

### Người dùng
1. **Đăng ký** tài khoản mới tại trang Đăng ký.
2. **Đăng nhập** và khám phá kho phim.
3. **Xem phim**, bình luận và thêm phim vào danh sách yêu thích.
4. **Chia sẻ** phim hay qua Email cho bạn bè.
5. **Quên mật khẩu?** Hệ thống sẽ gửi mã OTP qua Email để đặt lại mật khẩu.

### Quản trị viên (Admin)
1. **Đăng nhập** bằng tài khoản có quyền Admin (`admin = true` trong DB).
2. **Thêm phim mới**: Vào Dashboard → Quản lý Video → Nhấn "Thêm phim qua Slug" → Nhập slug từ KKPhim (ví dụ: `ngoi-truong-xac-song`). Hệ thống sẽ tự lấy toàn bộ dữ liệu.
3. **Quản lý thể loại**: Thêm, sửa, xóa danh mục phim.
4. **Quản lý người dùng**: Khóa/mở khóa tài khoản, thay đổi quyền Admin.
5. **Thống kê**: Xem biểu đồ phân tích lượt xem theo thể loại.

---

## 📁 Cấu trúc thư mục chính

```
MixiMovies/
├── src/main/java/
│   ├── dao/          # Data Access Objects (CRUD)
│   ├── entity/       # JPA Entities (User, Series, Comment, ...)
│   ├── servlet/      # Servlets & Filters
│   └── utils/        # Tiện ích (XJPA, Mailer, AuthUtil, ParamUtil, ...)
├── src/main/resources/
│   ├── META-INF/persistence.xml    # Cấu hình JPA (không chứa credentials)
│   ├── env.properties              # 🔒 Credentials thật (BỊ GITIGNORE)
│   └── env.example.properties      # 📋 File mẫu cho người clone
├── src/main/webapp/
│   ├── components/navbar.jsp       # Navbar dùng chung
│   ├── views/                      # Các trang JSP (index, detail, admin, ...)
│   └── assets/                     # CSS, JS, Images
├── .gitignore
├── pom.xml
└── README.md
```

---

## 🔒 Bảo mật

- **Mật khẩu**: Mã hóa SHA-256 (không lưu plain text).
- **XSS**: Sử dụng `<c:out>` trên toàn bộ JSP và `textContent` trong JavaScript.
- **CSRF**: Các thao tác xóa/sửa sử dụng phương thức POST.
- **Credentials**: Tách riêng vào `env.properties`, không push lên Git.
- **Admin Filter**: Bảo vệ các route `/admin/*` chỉ dành cho tài khoản Admin.

---

*© 2025 MixiMovies — Phát triển bởi giahuy1411*
