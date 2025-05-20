# Notification Service

## Tổng quan
Notification Service xử lý việc gửi thông báo đến người dùng thông qua các kênh khác nhau như email. Service này nhận các sự kiện thông báo từ Kafka, xử lý và gửi thông báo đến người dùng. Đặc biệt, service này gửi email thông báo kèm mã bài thi đến sinh viên sau khi bài thi được tạo thành công.

## Cài đặt
- Được xây dựng bằng `Dockerfile` đã cung cấp.
- Mã nguồn nằm trong thư mục `src/`.
- Yêu cầu Java 17 và Maven.

## Phát triển
- Chạy cục bộ thông qua lệnh `docker-compose up --build` từ thư mục gốc.
- Đăng ký với Eureka Server để khám phá dịch vụ.

## Kafka Consumers
- `quiz-creation-notification-topic`: Nhận sự kiện SendQuizCodeEvent từ Quiz Creation Service

## Cấu hình
- Cấu hình Kafka consumer trong `application.yml`
- Cấu hình máy chủ SMTP trong `application.yml`
- Cấu hình Eureka client trong `application.yml`