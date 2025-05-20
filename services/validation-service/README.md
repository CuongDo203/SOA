# Validation Service

## Tổng quan
Validation Service xác thực tính hợp lệ của các danh sách nhập vào hệ thống (danh sách câu hỏi, cấu hình bài thi, danh sách sinh viên). Service này nhận dữ liệu từ Import Service, thực hiện xác thực, và gửi dữ liệu đã xác thực đến các Entity Services tương ứng.

## Cài đặt
- Được xây dựng bằng `Dockerfile` đã cung cấp.
- Mã nguồn nằm trong thư mục `src/`.
- Yêu cầu Java 17 và Maven.

## Phát triển
- Định nghĩa API trong `docs/api-specs/validation-service.yaml`.
- Chạy cục bộ thông qua lệnh `docker-compose up --build` từ thư mục gốc.
- Đăng ký với Eureka Server để khám phá dịch vụ.

## Các điểm cuối (Endpoints)
- URL cơ sở: `http://localhost:8091/`
- Các điểm cuối chính:
  - `POST /api/v1/validation/questions`: Xác thực danh sách câu hỏi
  - `POST /api/v1/validation/students`: Xác thực danh sách sinh viên
  - `POST /api/v1/validation/quiz-config`: Xác thực cấu hình bài thi

## Quy tắc xác thực
### Xác thực câu hỏi
- Nội dung câu hỏi không được để trống
- Mỗi câu hỏi phải có ít nhất 2 lựa chọn
- Mỗi câu hỏi phải có ít nhất 1 đáp án đúng

### Xác thực sinh viên
- Mã sinh viên không được để trống và phải đúng định dạng
- Tên sinh viên không được để trống
- Email sinh viên phải đúng định dạng
- Không được có sinh viên trùng mã

### Xác thực cấu hình bài thi
- Tên bài thi không được để trống
- Thời gian làm bài phải lớn hơn 0
- Số lượng câu hỏi phải lớn hơn 0
- Điểm tối đa phải lớn hơn 0

## Các thư viện phụ thuộc
- Hibernate Validator: Thư viện xác thực dữ liệu

## Cấu hình
- Cấu hình quy tắc xác thực trong `application.yml`
- Cấu hình Eureka client trong `application.yml`