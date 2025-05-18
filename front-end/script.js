document.addEventListener('DOMContentLoaded', function () {
    // Step navigation
    const steps = ['step1', 'step2', 'step3', 'step4'];
    const progressSteps = ['progress-step1', 'progress-step2', 'progress-step3', 'progress-step4'];

    // Dữ liệu thu thập từ tất cả các bước
    let quizData = {
        questions: null,
        config: null,
        students: null
    };

    function showStep(stepIndex) {
        steps.forEach((step, index) => {
            document.getElementById(step).classList.toggle('hidden', index !== stepIndex);
        });

        progressSteps.forEach((step, index) => {
            const stepElement = document.getElementById(step);
            if (index < stepIndex) {
                stepElement.classList.add('completed');
                stepElement.classList.remove('active');
                stepElement.querySelector('.progress-info').textContent = 'Đã nhập liệu';
            } else if (index === stepIndex) {
                stepElement.classList.add('active');
                stepElement.classList.remove('completed');
                stepElement.querySelector('.progress-info').textContent = 'Đang nhập liệu...';
            } else {
                stepElement.classList.remove('active', 'completed');
                stepElement.querySelector('.progress-info').textContent = 'Chưa bắt đầu';
            }
        });

        // Hiển thị thông báo khi đến bước cuối
        if (stepIndex === 2) {
            document.getElementById('allDataValidationFeedback').classList.remove('hidden');
            updateQuizInfoPanel();
        }
    }

    function updateQuizInfoPanel() {
        // Cập nhật thông tin bên sidebar khi có dữ liệu
        const quizInfoPanel = document.getElementById('currentQuizInfo');

        if (quizData.config) {
            document.getElementById('infoQuizTitle').textContent =
                document.getElementById('quizTitle').value || '-';
            document.getElementById('infoQuestionCount').textContent =
                document.getElementById('questionCount').value || '-';
            document.getElementById('infoDuration').textContent =
                (document.getElementById('durationMinutes').value || '-') + ' phút';
        }

        if (quizData.students) {
            document.getElementById('infoStudentCount').textContent =
                sampleStudents.length.toString();
        }
    }

    // Sample data for preview
    const sampleQuestions = [
        { id: 1, question: 'Thủ đô của Việt Nam là gì?', optionA: 'Hà Nội', optionB: 'Hồ Chí Minh', optionC: 'Đà Nẵng', optionD: 'Huế', correctAnswer: 'A' },
        { id: 2, question: 'Ai là tác giả của Truyện Kiều?', optionA: 'Hồ Xuân Hương', optionB: 'Nguyễn Du', optionC: 'Nguyễn Trãi', optionD: 'Nguyễn Bỉnh Khiêm', correctAnswer: 'B' },
        { id: 3, question: '1 + 1 = ?', optionA: '1', optionB: '2', optionC: '3', optionD: '4', correctAnswer: 'B' }
    ];

    const sampleStudents = [
        { id: 1, studentId: 'SV001', name: 'Nguyễn Văn A', email: 'nguyenvana@example.com', class: 'IT-K62' },
        { id: 2, studentId: 'SV002', name: 'Trần Thị B', email: 'tranthib@example.com', class: 'IT-K62' },
        { id: 3, studentId: 'SV003', name: 'Lê Văn C', email: 'levanc@example.com', class: 'IT-K62' }
    ];

    // Step 1: Questions Upload
    document.getElementById('questionFile').addEventListener('change', function (e) {
        if (e.target.files.length > 0) {
            // Simulate file processing
            setTimeout(() => {
                // Store questions data
                quizData.questions = sampleQuestions;

                // Show preview
                const previewPanel = document.getElementById('questionPreviewPanel');
                previewPanel.classList.remove('hidden');

                // Fill preview table
                const previewBody = document.getElementById('questionPreviewBody');
                previewBody.innerHTML = '';
                sampleQuestions.forEach(q => {
                    previewBody.innerHTML += `
                        <tr>
                            <td>${q.id}</td>
                            <td>${q.question}</td>
                            <td>${q.optionA}</td>
                            <td>${q.optionB}</td>
                            <td>${q.optionC}</td>
                            <td>${q.optionD}</td>
                            <td>${q.correctAnswer}</td>
                        </tr>
                    `;
                });

                // Enable next button
                document.getElementById('nextToStep2').disabled = false;
            }, 1000);
        }
    });

    document.getElementById('questionUploadArea').addEventListener('dragover', function (e) {
        e.preventDefault();
        this.classList.add('bg-light');
    });

    document.getElementById('questionUploadArea').addEventListener('dragleave', function (e) {
        e.preventDefault();
        this.classList.remove('bg-light');
    });

    document.getElementById('questionUploadArea').addEventListener('drop', function (e) {
        e.preventDefault();
        this.classList.remove('bg-light');

        if (e.dataTransfer.files.length > 0) {
            document.getElementById('questionFile').files = e.dataTransfer.files;
            // Trigger change event
            const event = new Event('change');
            document.getElementById('questionFile').dispatchEvent(event);
        }
    });

    document.getElementById('downloadSampleQuestions').addEventListener('click', function () {
        alert('Đã bắt đầu tải xuống tệp mẫu câu hỏi.');
        // In real implementation, this would trigger a file download
    });

    document.getElementById('nextToStep2').addEventListener('click', function () {
        if (quizData.questions) {
            showStep(1);
        } else {
            alert('Vui lòng tải lên danh sách câu hỏi trước khi tiếp tục.');
        }
    });

    // Step 2: Quiz Configuration
    document.getElementById('backToStep1').addEventListener('click', function () {
        showStep(0);
    });

    document.getElementById('nextToStep3').addEventListener('click', function () {
        // Collect config data
        const form = document.getElementById('configForm');
        const allInputsFilled = Array.from(form.querySelectorAll('input[required]'))
            .every(input => input.value.trim() !== '');

        if (allInputsFilled) {
            quizData.config = {
                title: document.getElementById('quizTitle').value,
                subject: document.getElementById('subjectName').value,
                duration: document.getElementById('durationMinutes').value,
                passingScore: document.getElementById('passingScore').value,
                questionCount: document.getElementById('questionCount').value,
                startDate: document.getElementById('startDate').value,
                endDate: document.getElementById('endDate').value,
                shuffleQuestions: document.getElementById('shuffleQuestions').checked,
                shuffleOptions: document.getElementById('shuffleOptions').checked,
                showResults: document.getElementById('showResults').checked
            };

            updateQuizInfoPanel();
            showStep(2);
        } else {
            alert('Vui lòng điền đầy đủ thông tin cấu hình bài thi trước khi tiếp tục.');
        }
    });

    // Step 3: Students Upload
    document.getElementById('backToStep2').addEventListener('click', function () {
        showStep(1);
    });

    document.getElementById('studentFile').addEventListener('change', function (e) {
        if (e.target.files.length > 0) {
            const file = e.target.files[0];
            const reader = new FileReader();

            // Show loading indicator
            const validationFeedback = document.getElementById('allDataValidationFeedback');
            validationFeedback.classList.remove('hidden');
            validationFeedback.querySelector('.alert').classList.add('alert-info');
            validationFeedback.querySelector('i').className = 'fas fa-spinner fa-spin me-2';
            document.getElementById('allDataValidationMessage').textContent = 'Đang đọc dữ liệu từ file Excel...';

            reader.onload = function (event) {
                try {
                    const data = new Uint8Array(event.target.result);
                    const workbook = XLSX.read(data, { type: 'array' });

                    // Get first worksheet
                    const worksheetName = workbook.SheetNames[0];
                    const worksheet = workbook.Sheets[worksheetName];

                    // Convert to JSON
                    const students = XLSX.utils.sheet_to_json(worksheet);

                    // Check if data has required fields
                    if (students.length > 0) {
                        // Store student data
                        quizData.students = students;

                        // Map fields based on your Excel column structure
                        const mappedStudents = students.map((s, index) => {
                            // Create full name by combining First name and Last name
                            const fullName = `${s['Last name'] || ''} ${s['First name'] || ''}`.trim();
                            
                            return {
                                id: s['STT'] || index + 1,
                                studentId: s['Student code'] || '',
                                name: fullName,
                                email: s['Email'] || '',
                                class: s['Class name'] || ''
                            };
                        });

                        // Show preview
                        const previewPanel = document.getElementById('studentPreviewPanel');
                        previewPanel.classList.remove('hidden');

                        // Fill preview table
                        const previewBody = document.getElementById('studentPreviewBody');
                        previewBody.innerHTML = '';
                        mappedStudents.forEach(s => {
                            previewBody.innerHTML += `
                                <tr>
                                    <td>${s.id}</td>
                                    <td>${s.studentId}</td>
                                    <td>${s.name}</td>
                                    <td>${s.email}</td>
                                    <td>${s.class}</td>
                                </tr>
                            `;
                        });

                        // Update student count in sidebar - use actual count not sample data
                        document.getElementById('infoStudentCount').textContent = mappedStudents.length;

                        // Update validation message
                        validationFeedback.querySelector('.alert').classList.remove('alert-info');
                        validationFeedback.querySelector('.alert').classList.add('alert-success');
                        validationFeedback.querySelector('i').className = 'fas fa-check-circle me-2';
                        document.getElementById('allDataValidationMessage').textContent =
                            `Đã tải lên thành công ${mappedStudents.length} sinh viên từ file Excel.`;

                        // Enable validate and create button
                        document.getElementById('validateAndCreateQuiz').disabled = false;
                        
                        // Update student count for the quiz creation step
                        document.getElementById('studentCount').textContent = mappedStudents.length;
                    } else {
                        throw new Error('Không tìm thấy dữ liệu sinh viên trong file Excel.');
                    }
                } catch (error) {
                    console.error(error);
                    validationFeedback.querySelector('.alert').classList.remove('alert-info');
                    validationFeedback.querySelector('.alert').classList.add('alert-danger');
                    validationFeedback.querySelector('i').className = 'fas fa-exclamation-circle me-2';
                    document.getElementById('allDataValidationMessage').textContent =
                        'Lỗi khi đọc file Excel: ' + error.message;
                }
            };

            reader.onerror = function () {
                validationFeedback.querySelector('.alert').classList.remove('alert-info');
                validationFeedback.querySelector('.alert').classList.add('alert-danger');
                validationFeedback.querySelector('i').className = 'fas fa-exclamation-circle me-2';
                document.getElementById('allDataValidationMessage').textContent = 'Lỗi khi đọc file. Vui lòng thử lại.';
            };

            reader.readAsArrayBuffer(file);
        }
    });

    document.getElementById('studentUploadArea').addEventListener('drop', function (e) {
        e.preventDefault();
        this.classList.remove('bg-light');

        if (e.dataTransfer.files.length > 0) {
            document.getElementById('studentFile').files = e.dataTransfer.files;
            // Trigger change event
            const event = new Event('change');
            document.getElementById('studentFile').dispatchEvent(event);
        }
    });

    document.getElementById('studentUploadArea').addEventListener('dragleave', function (e) {
        e.preventDefault();
        this.classList.remove('bg-light');
    });

    document.getElementById('studentUploadArea').addEventListener('drop', function (e) {
        e.preventDefault();
        this.classList.remove('bg-light');

        if (e.dataTransfer.files.length > 0) {
            document.getElementById('studentFile').files = e.dataTransfer.files;
            // Trigger change event
            const event = new Event('change');
            document.getElementById('studentFile').dispatchEvent(event);
        }
    });

    document.getElementById('downloadSampleStudents').addEventListener('click', function () {
        // Create workbook with sample data matching your format
        const sampleData = [
            { 'STT': 1, 'Student code': 'B21DCCN001', 'First name': 'A', 'Last name': 'Nguyen Van', 'Email': 'B21DCCN001@yopmail.com', 'Class name': 'D21CNPM05' },
            { 'STT': 2, 'Student code': 'B21DCCN002', 'First name': 'B', 'Last name': 'Tran Thi', 'Email': 'B21DCCN002@yopmail.com', 'Class name': 'D21CNPM05' },
            { 'STT': 3, 'Student code': 'B21DCCN003', 'First name': 'C', 'Last name': 'Le Van', 'Email': 'B21DCCN003@yopmail.com', 'Class name': 'D21CNPM05' }
        ];
        
        const ws = XLSX.utils.json_to_sheet(sampleData);
        const wb = XLSX.utils.book_new();
        XLSX.utils.book_append_sheet(wb, ws, "Sinh Viên");
        
        // Generate and download the file
        XLSX.writeFile(wb, "mau_danh_sach_sinh_vien.xlsx");
    });

    document.getElementById('validateAndCreateQuiz').addEventListener('click', function () {
        // Validate if we have all required data
        if (!quizData.questions) {
            alert('Thiếu dữ liệu câu hỏi. Vui lòng quay lại bước 1.');
            return;
        }

        if (!quizData.config) {
            alert('Thiếu thông tin cấu hình. Vui lòng quay lại bước 2.');
            return;
        }

        if (!quizData.students) {
            alert('Thiếu dữ liệu sinh viên. Vui lòng tải lên danh sách sinh viên.');
            return;
        }

        // Show loading state
        const validationFeedback = document.getElementById('allDataValidationFeedback');
        validationFeedback.querySelector('.alert').classList.remove('alert-info', 'alert-danger');
        validationFeedback.querySelector('.alert').classList.add('alert-warning');
        validationFeedback.querySelector('i').className = 'fas fa-spinner fa-spin me-2';
        document.getElementById('allDataValidationMessage').textContent = 'Đang gửi dữ liệu lên server để xác thực...';

        // Disable button while processing
        this.disabled = true;

        // Create FormData object to send files and data together
        const formData = new FormData();
        
        // Append the original files
        const questionFile = document.getElementById('questionFile').files[0];
        const studentFile = document.getElementById('studentFile').files[0];
        
        // Attach files if they exist
        if (questionFile) {
            formData.append('questionFile', questionFile);
        }
        
        if (studentFile) {
            formData.append('studentFile', studentFile);
        }
        
        // Add configuration as JSON
        formData.append('config', JSON.stringify(quizData.config));
        
        // Send data to server
        fetch('http://localhost:8888/api/v1/quiz-creation/create', {
            method: 'POST',
            body: formData  // No Content-Type header - browser sets it automatically with boundary
        })
        .then(response => {
            // Check if response is ok (status 200-299)
            if (!response.ok) {
                // If server returns an error status, throw an error
                return response.json().then(errorData => {
                    throw new Error(errorData.message || 'Có lỗi xảy ra khi tạo bài thi');
                });
            }
            return response.json();
        })
        .then(data => {
            // Success - show success message
            validationFeedback.querySelector('.alert').classList.remove('alert-warning');
            validationFeedback.querySelector('.alert').classList.add('alert-success');
            validationFeedback.querySelector('i').className = 'fas fa-check-circle me-2';
            document.getElementById('allDataValidationMessage').textContent = 'Dữ liệu hợp lệ! Đã tạo bài thi thành công!';

            // Display the quiz code from server response
            const quizCode = data.quizCode || ('QZ-' + Math.floor(10000 + Math.random() * 90000));
            document.getElementById('quizCode').textContent = quizCode;
            
            // Use actual student count
            const studentCount = quizData.students ? quizData.students.length : 0;
            document.getElementById('studentCount').textContent = studentCount;

            // Show result step
            setTimeout(() => {
                showStep(3);
            }, 1000);
        })
        .catch(error => {
            // Error handling
            console.error('Error creating quiz:', error);
            validationFeedback.querySelector('.alert').classList.remove('alert-warning');
            validationFeedback.querySelector('.alert').classList.add('alert-danger');
            validationFeedback.querySelector('i').className = 'fas fa-exclamation-circle me-2';
            document.getElementById('allDataValidationMessage').textContent = 'Lỗi: ' + error.message;
            
            // Re-enable the button so user can try again
            this.disabled = false;
        });
    });

    // Step 4: Result
    document.getElementById('copyQuizCode').addEventListener('click', function () {
        const quizCode = document.getElementById('quizCode').textContent;
        navigator.clipboard.writeText(quizCode).then(() => {
            this.innerHTML = '<i class="fas fa-check me-1"></i> Đã sao chép';
            setTimeout(() => {
                this.innerHTML = '<i class="fas fa-copy me-1"></i> Sao chép';
            }, 2000);
        });
    });

    document.getElementById('viewQuizDetails').addEventListener('click', function () {
        alert('Chức năng xem chi tiết bài thi sẽ được triển khai sau.');
    });

    document.getElementById('createNewQuiz').addEventListener('click', function () {
        // Reset data and form
        quizData = {
            questions: null,
            config: null,
            students: null
        };

        document.getElementById('configForm').reset();
        document.getElementById('questionFile').value = '';
        document.getElementById('studentFile').value = '';

        // Hide previews
        document.querySelectorAll('.preview-panel').forEach(el => {
            el.classList.add('hidden');
        });

        // Reset buttons
        document.getElementById('nextToStep2').disabled = true;
        document.getElementById('validateAndCreateQuiz').disabled = true;

        // Go to step 1
        showStep(0);
    });

    // Initialize
    showStep(0);
});