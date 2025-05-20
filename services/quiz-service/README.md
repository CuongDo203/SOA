# Quiz Service

## Tổng quan
Quiz Service quản lý các bài thi đã được tạo, bao gồm thông tin về bài thi, câu hỏi, và kết quả. Service này tổng hợp dữ liệu từ Question Service, Quiz Config Service, và Student Service để tạo bài thi hoàn chỉnh và sinh mã bài thi duy nhất cho mỗi bài thi.

## Cài đặt
- Được xây dựng bằng `Dockerfile` đã cung cấp.
- Mã nguồn nằm trong thư mục `src/`.
- Yêu cầu Java 17 và Maven.

## Phát triển
- Định nghĩa API trong `docs/api-specs/quiz-service.yaml`.
- Chạy cục bộ thông qua lệnh `docker-compose up --build` từ thư mục gốc.
- Đăng ký với Eureka Server để khám phá dịch vụ.

## Các điểm cuối (Endpoints)
- URL cơ sở: `http://localhost:9005/`
- Các điểm cuối chính:
  - `POST /api/v1/quizz`: Tạo bài thi mới

## Cơ sở dữ liệu
- Cơ sở dữ liệu MySQL
- Các bảng chính:
  - `quiz`: Lưu trữ thông tin bài thi
  - `quiz_questions`: Lưu trữ mối quan hệ giữa bài thi và câu hỏi
  - `quiz_students`: Lưu trữ mối quan hệ giữa bài thi và sinh viên

## Mô hình dữ liệu
- Quiz:
  - `id`: Định danh duy nhất của bài thi
  - `code`: Mã bài thi duy nhất
  - `quiz_config_id`: ID của cấu hình bài thi
  - `title`: Tiêu đề của bài thi
  - `question_ids`: Danh sách ID của các câu hỏi trong bài thi
  - `assigned_student_ids`: Danh sách ID của các sinh viên được giao bài thi

## Cấu hình
- Cấu hình kết nối cơ sở dữ liệu trong `application.yml`
- Cấu hình Eureka client trong `application.yml`