# Quiz Config Service

## Tổng quan
Quiz Config Service quản lý cấu hình của bài thi trắc nghiệm, bao gồm tên bài thi, thời gian làm bài, số lượng câu hỏi, điểm tối đa, quy định, thời gian bắt đầu và kết thúc. Service này nhận dữ liệu cấu hình đã được xác thực từ Validation Service và cung cấp API để Quiz Service có thể truy xuất cấu hình khi tạo bài thi.

## Cài đặt
- Được xây dựng bằng `Dockerfile` đã cung cấp.
- Mã nguồn nằm trong thư mục `src/`.
- Yêu cầu Java 17 và Maven.

## Phát triển
- Định nghĩa API trong `docs/api-specs/quiz-config-service.yaml`.
- Chạy cục bộ thông qua lệnh `docker-compose up --build` từ thư mục gốc.
- Đăng ký với Eureka Server để khám phá dịch vụ.

## Các điểm cuối (Endpoints)
- URL cơ sở: `http://localhost:8081/`
- Các điểm cuối chính:
  - `POST /api/v1/quiz-config`: Tạo cấu hình bài thi mới

## Cơ sở dữ liệu
- Cơ sở dữ liệu MySQL
- Các bảng chính:
  - `quiz_configs`: Lưu trữ thông tin cấu hình bài thi

## Mô hình dữ liệu
- QuizConfig:
  - `id`: Định danh duy nhất của cấu hình bài thi
  - `durationMinutes`: Thời gian làm bài (phút)
  - `questionCount`: Số lượng câu hỏi
  - `maxScore`: Điểm tối đa
  - `rules`: Quy định của bài thi
  - `start`: Thời gian bắt đầu
  - `end`: Thời gian kết thúc

## Cấu hình
- Cấu hình kết nối cơ sở dữ liệu trong `application.yml`
- Cấu hình Eureka client trong `application.yml`