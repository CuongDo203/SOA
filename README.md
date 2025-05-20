# mid-project-189692116

## 💡 Use Case: Hệ thống tạo bài thi trắc nghiệm

## 👥 Thành viên
- **Phùng Văn Thịnh** - B21DCCN692
- **Đỗ Hoành Thông** - B21DCCN116
- **Đỗ Ngọc Cường** - B21DCCN189

---

## 📝 Mô tả

### Mô tả nghiệp vụ
Hệ thống tạo bài thi trắc nghiệm cho phép giáo viên tạo và quản lý các bài thi trắc nghiệm. Sau khi giáo viên tạo bài thi, hệ thống sẽ xử lý bằng cách:

- Import danh sách câu hỏi từ file Excel
- Xác thực tính hợp lệ của câu hỏi
- Cấu hình bài thi (thời gian, số lượng câu hỏi, điểm số)
- Xác thực tính hợp lệ của cấu hình bài thi
- Import danh sách sinh viên tham gia
- Xác thực tính hợp lệ của danh sách sinh viên
- Sinh mã bài thi và gửi thông báo đến sinh viên

Nếu tất cả các bước được thực hiện thành công, sinh viên sẽ nhận được mã bài thi để tham gia.

### Yêu cầu phân tích thiết kế hướng dịch vụ (SOA)
Phân tích và thiết kế hệ thống tạo bài thi trắc nghiệm dựa trên kiến trúc hướng dịch vụ (SOA), đảm bảo toàn bộ quy trình từ tạo bài thi đến gửi thông báo cho sinh viên được thực hiện **tự động** và **hiệu quả**.

---

## 🔁 Quy trình tạo bài thi

1. **Khởi tạo quy trình tạo bài thi**: Giáo viên truy cập hệ thống và bắt đầu quy trình tạo bài thi trắc nghiệm.
2. **Nhập danh sách câu hỏi**: Giáo viên nhập thủ công danh sách câu hỏi (bao gồm nội dung câu hỏi, đáp án, và đáp án đúng) vào hệ thống thông qua giao diện.
3. **Xác thực danh sách câu hỏi**: Hệ thống kiểm tra tính hợp lệ của danh sách câu hỏi (ví dụ: định dạng đúng, không có câu hỏi trùng lặp, đáp án hợp lệ).
4. **Thông báo lỗi nếu danh sách câu hỏi không hợp lệ**: Nếu danh sách câu hỏi không đáp ứng yêu cầu, hệ thống dừng quy trình và thông báo lỗi chi tiết cho giáo viên.
5. **Nhập cấu hình bài thi**: Giáo viên nhập các thông tin cấu hình bài thi, bao gồm thời gian làm bài, số lượng câu hỏi, thang điểm, và các quy định khác.
6. **Xác thực cấu hình bài thi**: Hệ thống kiểm tra tính hợp lệ của cấu hình (ví dụ: thời gian làm bài hợp lý, số lượng câu hỏi phù hợp với danh sách đã nhập).
7. **Thông báo lỗi nếu cấu hình không hợp lệ**: Nếu cấu hình không hợp lệ, hệ thống dừng quy trình và thông báo lỗi chi tiết cho giáo viên.
8. **Nhập danh sách sinh viên**: Giáo viên nhập danh sách tên hoặc mã sinh viên tham gia bài thi.
9. **Xác thực danh sách sinh viên**: Hệ thống kiểm tra tính hợp lệ của danh sách sinh viên (ví dụ: mã sinh viên tồn tại trong cơ sở dữ liệu, không có trùng lặp).
10. **Thông báo lỗi nếu danh sách sinh viên không hợp lệ**: Nếu danh sách sinh viên không hợp lệ, hệ thống dừng quy trình và thông báo lỗi chi tiết cho giáo viên.
11. **Lưu trữ và tạo bài thi**: Nếu tất cả dữ liệu (câu hỏi, cấu hình, sinh viên) đều hợp lệ, hệ thống lưu trữ dữ liệu và tạo bài thi, sinh mã bài thi duy nhất.
12. **Gửi thông báo thành công**: Hệ thống gửi mã bài thi đến sinh viên qua email và thông báo cho giáo viên rằng quy trình tạo bài thi đã hoàn tất.

---

## 🛠 Công nghệ sử dụng
### Kiến trúc và Công nghệ
- **Kiến trúc**: Microservices
- **Message Broker**: Apache Kafka
- **Service Discovery**: Netflix Eureka

### Backend
- **Ngôn ngữ và Framework**: Java với Spring Boot
- **Cơ sở dữ liệu**: MySQL
- **API Gateway**: Spring Cloud Gateway

### Frontend
- **UI Libraries**: html, css, js.

---

## 📅 Tiến độ

| Tuần | Ngày bắt đầu | Nội dung                                           |
|------|---------------|----------------------------------------------------|
| Tuần 1 | 20-27/04/2025    | Chọn đề tài, cập nhật mô tả đề tài                |
| Tuần 2 | 28/04/2025- 08/05/2025    | Bổ sung công nghệ sử dụng, thiết lập môi trường, cấu hình cơ bản cho dự án |
| Tuần 3 | 09/5/2025 - 20/5/2025 | Triển khai các service, cập nhật docker-compose và cấu hình cho các service |

---

## 🚀 Hướng dẫn chạy ứng dụng

### Yêu cầu
- Docker và Docker Compose
- Java 17 và Maven (để phát triển)

### Các bước chạy ứng dụng
1. Clone repository:
   ```bash
   git clone <repository-url>
   cd mid-project-189692116
   ```
2. Chạy lệnh Docker-compose up -d trong terminal.
   ```
3. Chạy container bằng Docker Desktop
   ```

4. Truy cập các endpoints:
   - Frontend: http://localhost:5500
   - API Gateway: http://localhost:8888

### Các endpoints chính
- Quiz Creation Service: http://localhost:9000
- Question Service: http://localhost:9004
- Student Service: http://localhost:9003
- Import Service: http://localhost:8082
- Validation Service: http://localhost:8091
- Quiz Config Service: http://localhost:8081
- Quiz Service: http://localhost:9005
- Notification Service: http://localhost:9002
- Eureka Server: http://localhost:8761

## 📁 Cấu trúc thư mục

```
mid-project-189692116/
├── README.md                       # Tài liệu hướng dẫn
├── docker-compose.yml              # Cấu hình Docker cho tất cả các services
├── docs/                           # Thư mục tài liệu
│   ├── architecture.md             # Mô tả thiết kế hệ thống
│   ├── analysis-and-design.md      # Tài liệu phân tích và thiết kế
│   ├── asset/                      # Hình ảnh, sơ đồ và tài liệu trực quan
│   └── api-specs/                  # Đặc tả API theo chuẩn OpenAPI (YAML)
│       ├── quiz-creation-service.yaml    # Đặc tả API cho Quiz Creation Service
│       ├── question-service.yaml         # Đặc tả API cho Question Service
│       ├── student-service.yaml          # Đặc tả API cho Student Service
│       ├── import-service.yaml           # Đặc tả API cho Import Service
│       ├── validation-service.yaml       # Đặc tả API cho Validation Service
│       ├── quiz-config-service.yaml      # Đặc tả API cho Quiz Config Service
│       ├── quiz-service.yaml             # Đặc tả API cho Quiz Service
│       └── notification-service.yaml     # Đặc tả API cho Notification Service
├── scripts/                        # Scripts tiện ích
│   └── init.sh
├── services/                       # Các microservices
│   ├── quiz-creation-service/      # Dịch vụ điều phối tạo bài thi (Java Spring Boot)
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── pom.xml
│   ├── question-service/           # Dịch vụ quản lý câu hỏi (Java Spring Boot)
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── pom.xml
│   ├── student-service/            # Dịch vụ quản lý sinh viên (Java Spring Boot)
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── pom.xml
│   ├── import-service/             # Dịch vụ import dữ liệu (Java Spring Boot)
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── pom.xml
│   ├── validation-service/         # Dịch vụ xác thực dữ liệu (Java Spring Boot)
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── pom.xml
│   ├── quiz-config/        # Dịch vụ quản lý cấu hình bài thi (Java Spring Boot)
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── pom.xml
│   ├── quiz-service/               # Dịch vụ quản lý bài thi (Java Spring Boot)
│   │   ├── Dockerfile
│   │   ├── src/
│   │   └── pom.xml
│   └── notification-service/       # Dịch vụ gửi thông báo (Java Spring Boot)
│       ├── Dockerfile
│       ├── src/
│       └── pom.xml
├── gateway/                        # API Gateway
│   ├── Dockerfile
│   └── src/
└── eureka-server/                  # Service Discovery
    ├── Dockerfile
    └── src/
```

## Sơ đồ kiến trúc

![Kiến trúc hệ thống](./docs/asset/system-architecture.png)
---

## 📚 Các dịch vụ (Services)

### 1. Quiz Creation Service
- **Mô tả**: Điều phối toàn bộ quy trình tạo bài thi, từ import danh sách câu hỏi, cấu hình bài thi, danh sách sinh viên, và xác thực các dữ liệu liên quan.
- **Cơ sở dữ liệu**: MySQL
- **Endpoints chính**:
  - `POST /api/v1/quizz-creation/create-quiz`: Tạo bài thi mới
  - `POST /api/v1/quizz-creation/import/questions`: Import danh sách câu hỏi
  - `POST /api/v1/quizz-creation/import/students`: Import danh sách sinh viên
  - `POST /api/v1/quizz-creation/import/quiz-config`: Import cấu hình bài thi

### 2. Question Service
- **Mô tả**: Quản lý danh sách câu hỏi, bao gồm lưu trữ, truy xuất và cập nhật câu hỏi.
- **Cơ sở dữ liệu**: MySQL
- **Endpoints chính**:
  - `POST /api/v1/questions`: Tạo câu hỏi mới

### 3. Student Service
- **Mô tả**: Quản lý danh sách sinh viên tham gia thi, bao gồm lưu trữ, truy xuất và cập nhật thông tin sinh viên.
- **Cơ sở dữ liệu**: MySQL
- **Endpoints chính**:
  - `POST /api/v1/students`: Tạo sinh viên mới
  - `POST /api/v1/students/check-existence`: Kiểm tra sự tồn tại của sinh viên
  - `POST /api/v1/students/by-ids`: Lấy thông tin sinh viên theo danh sách ID

### 4. Import Service
- **Mô tả**: Xử lý việc import thủ công các danh sách (danh sách câu hỏi, cấu hình bài thi, danh sách sinh viên) vào hệ thống.
- **Cơ sở dữ liệu**: MySQL
- **Endpoints chính**:
  - `POST /api/v1/import/questions/excel`: Import danh sách câu hỏi từ file Excel
  - `POST /api/v1/import/students/excel`: Import danh sách sinh viên từ file Excel

### 5. Validation Service
- **Mô tả**: Xác thực tính hợp lệ của các danh sách nhập vào hệ thống (danh sách câu hỏi, cấu hình bài thi, danh sách sinh viên).
- **Cơ sở dữ liệu**: MySQL
- **Endpoints chính**:
  - `POST /api/v1/validation/questions`: Xác thực danh sách câu hỏi
  - `POST /api/v1/validation/students`: Xác thực danh sách sinh viên
  - `POST /api/v1/validation/quiz-config`: Xác thực cấu hình bài thi

### 6. Quiz Config Service
- **Mô tả**: Quản lý cấu hình của bài thi trắc nghiệm, bao gồm thời gian, số lượng câu hỏi, điểm số, và các quy định khác.
- **Cơ sở dữ liệu**: MySQL
- **Endpoints chính**:
  - `POST /api/v1/quiz-config`: Tạo cấu hình bài thi mới

### 7. Quiz Service
- **Mô tả**: Quản lý các bài thi đã được tạo, bao gồm thông tin về bài thi, câu hỏi, và kết quả.
- **Cơ sở dữ liệu**: MySQL
- **Endpoints chính**:
  - `POST /api/v1/quiz`: Tạo bài thi mới

### 8. Notification Service
- **Mô tả**: Xử lý việc gửi thông báo đến người dùng thông qua các kênh khác nhau như email.
---

## 👩‍🏫 Yêu cầu nộp bài

Đảm bảo:
- `README.md` được cập nhật với mô tả dịch vụ và hướng dẫn sử dụng API
- Bao gồm danh sách thành viên nhóm và đóng góp của họ trong `README.md`
- Tất cả mã của bạn có thể **chạy bằng một lệnh**: `docker-compose up -d`