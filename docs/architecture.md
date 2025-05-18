# System Architecture

## Tổng quan
Hệ thống tạo bài thi trắc nghiệm cho phép giáo viên khởi tạo một quiz với danh sách câu hỏi, cấu hình quiz và danh sách sinh viên tham gia. Giáo viên sẽ import thủ công danh sách câu hỏi, cấu hình bài thi, và danh sách sinh viên. Hệ thống cần xác thực tính hợp lệ của danh sách câu hỏi, cấu hình quiz và danh sách sinh viên. Nếu có bất kỳ lỗi nào về format hoặc dữ liệu không hợp lệ, quy trình sẽ kết thúc và thông báo lỗi cho người dùng
## Các thành phần hệ thống
- Quiz Creation Service: Xử lý toàn bộ quy trình tạo bài thi trắc nghiệm, từ import
danh sách câu hỏi, cấu hình bài thi, danh sách sinh viên, và xác thực các dữ liệu liên
quan.

- Question Service: Quản lý danh sách câu hỏi được nhập vào và xử lý xác thực.

- Quiz Config Service: Quản lý cấu hình của bài thi trắc nghiệm, bao gồm thời gian,
số lượng câu hỏi, điểm số, và các quy định khác.

- Student Service: Quản lý danh sách sinh viên tham gia thi, bao gồm việc xác thực
tên hoặc mã sinh viên.

- Validation Service: Xác thực tính hợp lệ của các danh sách nhập vào hệ thống
(danh sách câu hỏi, cấu hình bài thi, danh sách sinh viên).

- Import Service: Xử lý việc import thủ công các danh sách (danh sách câu hỏi, cấu
hình bài thi, danh sách sinh viên) vào hệ thống.

- API Gateway : Đóng vai trò làm điểm vào duy nhất cho hệ thống. Định tuyến request từ người dùng (giáo viên) đến các service tương ứng.
  
## Giao tiếp
- Sử dụng Rest API.

## Luồng dữ liệu
1. Bắt đầu từ giáo viên
Giáo viên tương tác với hệ thống thông qua API Gateway. Đây là điểm truy cập duy nhất, giúp định tuyến request tới các service phù hợp.

2. Quiz Creation Service điều phối toàn bộ quy trình
Sau khi nhận yêu cầu từ API Gateway, Quiz Creation Service chịu trách nhiệm gọi đến Import Service để thực hiện nhập các dữ liệu:

Danh sách câu hỏi

Cấu hình bài thi

Danh sách sinh viên

3. Validation Service xử lý xác thực
Sau khi dữ liệu được Import, Quiz Creation Service gửi dữ liệu đó đến Validation Service để xác thực:

Nếu dữ liệu không hợp lệ, Validation Service sẽ trả về lỗi → hệ thống hiển thị thông báo lỗi và dừng quy trình.

Nếu hợp lệ, Validation Service gửi dữ liệu đã xác thực đến các Entity Services tương ứng:

4. Ghi nhận dữ liệu đã xác thực vào các Entity Services
Question Service lưu các câu hỏi hợp lệ.

Quiz Config Service lưu cấu hình hợp lệ.

Student Service lưu danh sách sinh viên hợp lệ.

5. Tạo bài thi và gửi mã
Khi toàn bộ dữ liệu đã xác thực và lưu trữ thành công, Quiz Creation Service tổng hợp lại:

Truy xuất câu hỏi, cấu hình, danh sách sinh viên từ các Entity Service

Sinh ra một mã bài thi (quiz code) tương ứng

Gửi mã bài thi + thông báo đến sinh viên tham gia thi

## Sơ đồ
- Reference a high-level architecture diagram (place in `docs/asset/`).

## Scalability & Fault Tolerance
1. Khả năng mở rộng (Scalability)
Do hệ thống áp dụng kiến trúc microservices, mỗi service (như Validation, Question, Quiz Creation...) có thể được scale độc lập mà không ảnh hưởng đến service khác.

2. Khả năng chịu lỗi (Fault Tolerance)
Hệ thống được thiết kế để chịu lỗi cục bộ: nếu một service gặp sự cố, các service còn lại vẫn hoạt động bình thường.
