# Import Service

## Tổng quan
Import Service xử lý việc import thủ công các danh sách (danh sách câu hỏi, cấu hình bài thi, danh sách sinh viên) vào hệ thống. Service này chịu trách nhiệm cho việc phân tích và chuyển đổi dữ liệu từ các định dạng khác nhau (Excel, CSV) thành các đối tượng trong hệ thống.

## Cài đặt
- Được xây dựng bằng `Dockerfile` đã cung cấp.
- Mã nguồn nằm trong thư mục `src/`.
- Yêu cầu Java 17 và Maven.

## Phát triển
- Định nghĩa API trong `docs/api-specs/import-service.yaml`.
- Chạy cục bộ thông qua lệnh `docker-compose up --build` từ thư mục gốc.
- Đăng ký với Eureka Server để khám phá dịch vụ.

## Các điểm cuối (Endpoints)
- URL cơ sở: `http://localhost:8082/`
- Các điểm cuối chính:
  - `POST /api/v1/import/questions/excel`: Import danh sách câu hỏi từ file Excel
  - `POST /api/v1/import/students/excel`: Import danh sách sinh viên từ file Excel

## Tính năng
- Hỗ trợ import từ file Excel (.xlsx, .xls)
- Hỗ trợ import từ file CSV
- Kiểm tra định dạng file
- Phân tích dữ liệu từ file
- Chuyển đổi dữ liệu thành các đối tượng trong hệ thống
- Trả về kết quả import với thông tin chi tiết về lỗi (nếu có)

## Các thư viện phụ thuộc
- Apache POI: Thư viện xử lý file Excel
- OpenCSV: Thư viện xử lý file CSV

## Cấu hình
- Cấu hình kích thước file tối đa trong `application.yml`
- Cấu hình Eureka client trong `application.yml`