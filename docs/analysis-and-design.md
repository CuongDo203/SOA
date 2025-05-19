# SOA Service Modelling for Quiz Creation System

Tài liệu này trình bày chi tiết mô hình hóa dịch vụ theo kiến trúc hướng dịch vụ (SOA) cho quy trình tạo bài thi trắc nghiệm. Hệ thống cho phép giáo viên import thủ công danh sách câu hỏi, cấu hình bài thi và danh sách sinh viên, đồng thời xác thực các dữ liệu trước khi sinh mã bài quiz và gửi thông báo đến sinh viên.

---

## Mục lục

- [Bước 1: Phân rã quy trình nghiệp vụ](#bước-1-phân-rã-quy-trình-nghiệp-vụ)
- [Bước 2: Loại bỏ các hành động không tự động](#bước-2-loại-bỏ-các-hành-động-không-tự-động)
- [Bước 3: Xác định các dịch vụ theo thực thể (Entity Service Candidates)](#bước-3-xác-định-các-dịch-vụ-theo-thực-thể-entity-service-candidates)
- [Bước 4: Xác định các tài nguyên (Resources)](#bước-4-xác-định-các-tài-nguyên-resources)
- [Bước 5: Gắn các API và phương thức vào tài nguyên](#bước-5-gắn-các-api-và-phương-thức-vào-tài-nguyên)
- [Bước 6: Xác định các luồng dịch vụ phối hợp (Composition Flow)](#bước-6-xác-định-các-luồng-dịch-vụ-phối-hợp-composition-flow)

---

## Bước 1: Phân rã quy trình nghiệp vụ

1. Khởi tạo quy trình tạo bài thi trắc nghiệm.
2. Import danh sách câu hỏi.
3. Xác thực danh sách câu hỏi.
4. Nếu không hợp lệ → kết thúc và thông báo lỗi.
5. Import cấu hình bài quiz.
6. Xác thực cấu hình bài quiz.
7. Nếu không hợp lệ → kết thúc và thông báo lỗi.
8. Import danh sách sinh viên.
9. Xác thực danh sách sinh viên.
10. Nếu không hợp lệ → kết thúc và thông báo lỗi.
11. Nếu tất cả hợp lệ → tạo bài quiz, sinh mã và lưu vào hệ thống.
12. Gửi mã bài quiz tới sinh viên.

---

## Bước 2: Loại bỏ các hành động không tự động

Các hành động thực hiện thủ công bởi giáo viên không được tự động hóa:

- Giáo viên nhập thủ công danh sách câu hỏi, cấu hình và danh sách sinh viên.

- Sinh viên nhận mã và làm bài sau đó (ngoài phạm vi tạo quiz)

## Bước 3: Xác định các ứng viên dịch vụ thực thể (Entity Service Candidates)

### Non-Agnostic Resources: Micro Service + Task Service 
- Quiz Creation Service: Điều phối toàn bộ quy trình tạo quiz, kiểm soát luồng xử lý và xác thực.
- Validation Service: Dịch vụ xác thực chung cho câu hỏi, cấu hình, sinh viên.
### Agnostic Resources: Utility Service + Entity Service
- Question Service: Quản lý và xác thực danh sách câu hỏi.
- Quiz Config Service: Quản lý và xác thực cấu hình quiz.
- Student Service: Quản lý và xác thực danh sách sinh viên.
- Quiz Service: Tạo và lưu trữ bài quiz, sinh mã quiz.
- Import Service: Xử lý việc import dữ liệu đầu vào.
- Notification Service: Gửi email chứa mã quiz đến sinh viên.

## Bước 4: Xác định logic cụ thể quy trình

