# Student Service

## Tổng quan
Student Service quản lý danh sách sinh viên tham gia thi, bao gồm lưu trữ, truy xuất và cập nhật thông tin sinh viên. Service này nhận dữ liệu sinh viên đã được xác thực từ Validation Service và cung cấp API để Quiz Service có thể truy xuất danh sách sinh viên khi tạo bài thi.

## Cài đặt
- Được xây dựng bằng `Dockerfile` đã cung cấp.
- Mã nguồn nằm trong thư mục `src/`.
- Yêu cầu Java 17 và Maven.

## Phát triển
- Định nghĩa API trong `docs/api-specs/student-service.yaml`.
- Chạy cục bộ thông qua lệnh `docker-compose up --build` từ thư mục gốc.
- Đăng ký với Eureka Server để khám phá dịch vụ.

## Các điểm cuối (Endpoints)
- URL cơ sở: `http://localhost:9003/`
- Các điểm cuối chính:
  - `POST /api/v1/students`: Tạo sinh viên mới
  - `POST /api/v1/students/check-existence`: Kiểm tra sự tồn tại của sinh viên
  - `POST /api/v1/students/by-ids`: Lấy thông tin sinh viên theo danh sách ID

## Cơ sở dữ liệu
- Cơ sở dữ liệu MySQL
- Các bảng chính:
  - `student`: Lưu trữ thông tin sinh viên

## Mô hình dữ liệu
- Student:
  - `id`: Định danh duy nhất của sinh viên
  - `studentCode`: Mã sinh viên
  - `firstName`: Tên của sinh viên
  - `lastName`: Họ của sinh viên
  - `email`: Email của sinh viên
  - `className`: Lớp của sinh viên

## Cấu hình
- Cấu hình kết nối cơ sở dữ liệu trong `application.yml`
- Cấu hình Eureka client trong `application.yml`