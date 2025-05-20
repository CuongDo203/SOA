# Quiz Creation Service

## Tổng quan
Quiz Creation Service điều phối toàn bộ quy trình tạo bài thi trắc nghiệm, từ import danh sách câu hỏi, cấu hình bài thi, danh sách sinh viên, và xác thực các dữ liệu liên quan. Đây là service trung tâm kết nối với các service khác trong hệ thống.

## Cài đặt
- Được xây dựng bằng `Dockerfile` đã cung cấp.
- Mã nguồn nằm trong thư mục `src/`.
- Yêu cầu Java 17 và Maven.

## Phát triển
- Định nghĩa API trong `docs/api-specs/quiz-creation-service.yaml`.
- Chạy cục bộ thông qua lệnh `docker-compose up --build` từ thư mục gốc.
- Kết nối với các service khác sử dụng Feign Client.

## Các điểm cuối (Endpoints)
- URL cơ sở: `http://localhost:9000/`
- Các điểm cuối chính:
  - `POST /api/v1/quizz-creation/create-quiz`: Tạo bài thi mới
  - `POST /api/v1/quiz-creation/verify/questions`: Import và xác thực danh sách câu hỏi
  - `POST /api/v1/quiz-creation/verify/students`: Import và xác thực danh sách sinh viên
  - `POST /api/v1/quiz-creation/verify/quiz-config`: Xác thực cấu hình bài thi

## Các phụ thuộc
- Import Service: Xử lý việc import dữ liệu
- Validation Service: Xác thực tính hợp lệ của dữ liệu
- Question Service: Quản lý danh sách câu hỏi
- Quiz Config Service: Quản lý cấu hình bài thi
- Student Service: Quản lý danh sách sinh viên
- Quiz Service: Quản lý bài thi
- Notification Service: Gửi thông báo đến người dùng

## Cấu hình
- Cấu hình Feign Client endpoints trong `application.yml`
- Cấu hình Eureka client trong `application.yml`