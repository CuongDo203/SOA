# Question Service

## Tổng quan
Question Service quản lý danh sách câu hỏi trắc nghiệm, bao gồm lưu trữ, truy xuất và cập nhật câu hỏi. Service này nhận dữ liệu câu hỏi đã được xác thực từ Validation Service và cung cấp API để Quiz Service có thể truy xuất câu hỏi khi tạo bài thi.

## Cài đặt
- Được xây dựng bằng `Dockerfile` đã cung cấp.
- Mã nguồn nằm trong thư mục `src/`.
- Yêu cầu Java 17 và Maven.

## Phát triển
- Định nghĩa API trong `docs/api-specs/question-service.yaml`.
- Chạy cục bộ thông qua lệnh `docker-compose up --build` từ thư mục gốc.
- Đăng ký với Eureka Server để khám phá dịch vụ.

## Các điểm cuối (Endpoints)
- URL cơ sở: `http://localhost:9004/`
- Các điểm cuối chính:
  - `POST /api/v1/questions`: Tạo câu hỏi mới

## Cơ sở dữ liệu
- Cơ sở dữ liệu MySQL
- Các bảng chính:
  - `question`: Lưu trữ thông tin câu hỏi

## Mô hình dữ liệu
- Question:
  - `id`: Định danh duy nhất của câu hỏi
  - `content`: Nội dung câu hỏi
  - `optionA`: Lựa chọn A
  - `optionB`: Lựa chọn B
  - `optionC`: Lựa chọn C
  - `optionD`: Lựa chọn D
  - `answerKey`: Đáp án đúng (A, B, C, hoặc D)

## Cấu hình
- Cấu hình kết nối cơ sở dữ liệu trong `application.yml`
- Cấu hình Eureka client trong `application.yml`