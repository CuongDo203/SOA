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

    // Elements for Step 1
    const questionFileInput = document.getElementById('questionFile');
    const questionUploadArea = document.getElementById('questionUploadArea');
    const questionPreviewPanel = document.getElementById('questionPreviewPanel');
    const questionPreviewBody = document.getElementById('questionPreviewBody');
    const nextToStep2Button = document.getElementById('nextToStep2');

    // Elements for Step 2
    const configForm = document.getElementById('configForm'); // Lấy form cấu hình
    const quizTitleInput = document.getElementById('quizTitle');
    const durationMinutesInput = document.getElementById('durationMinutes');
    const passingScoreInput = document.getElementById('passingScore');
    const questionCountInput = document.getElementById('questionCount'); // Input số lượng câu hỏi cấu hình
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const backToStep1Button = document.getElementById('backToStep1');
    const nextToStep3Button = document.getElementById('nextToStep3');


    // Elements for Step 3
    const studentFileInput = document.getElementById('studentFile');
    const studentUploadArea = document.getElementById('studentUploadArea');
    const studentPreviewPanel = document.getElementById('studentPreviewPanel');
    const studentPreviewBody = document.getElementById('studentPreviewBody');
    const allDataValidationFeedback = document.getElementById('allDataValidationFeedback');
    const allDataValidationMessage = document.getElementById('allDataValidationMessage');
    const validateAndCreateQuizButton = document.getElementById('validateAndCreateQuiz');
    const backToStep2ButtonFromStep3 = document.getElementById('backToStep2'); // Nút quay lại từ Step 3

    // Elements for Step 4
    const quizCodeDiv = document.getElementById('quizCode');
    const studentCountSpan = document.getElementById('studentCount'); // Span hiển thị số lượng SV ở Step 4
    const copyQuizCodeButton = document.getElementById('copyQuizCode');
    const viewQuizDetailsButton = document.getElementById('viewQuizDetails');
    const createNewQuizButton = document.getElementById('createNewQuiz');


    function showStep(stepIndex) {
        steps.forEach((step, index) => {
            document.getElementById(step).classList.toggle('hidden', index !== stepIndex);
        });

        progressSteps.forEach((step, index) => {
            const stepElement = document.getElementById(step);
            if (index < stepIndex) {
                stepElement.classList.add('completed');
                stepElement.classList.remove('active');
                stepElement.querySelector('.progress-info').textContent = 'Hoàn thành';
            } else if (index === stepIndex) {
                stepElement.classList.add('active');
                stepElement.classList.remove('completed');
                stepElement.querySelector('.progress-info').textContent = 'Đang thực hiện...';
            } else {
                stepElement.classList.remove('active', 'completed');
                stepElement.querySelector('.progress-info').textContent = 'Chưa bắt đầu';
            }
        });

        if (stepIndex === 2) { // Khi đến bước Import Students (index 2)
            allDataValidationFeedback.classList.remove('hidden');
        } else {
            allDataValidationFeedback.classList.add('hidden');
        }
        updateQuizInfoPanel(); // Cập nhật info panel mỗi khi chuyển step
    }

    function updateQuizInfoPanel() {
        // Cập nhật thông tin bên sidebar
        document.getElementById('infoQuizTitle').textContent = quizData.config?.title || quizTitleInput.value || '-';
        // Ưu tiên số câu hỏi đã import, sau đó mới đến số cấu hình (nếu chưa có câu hỏi)
        let currentQuestionCount = quizData.questions ? quizData.questions.length : (questionCountInput.value || '-');
        if (quizData.config && quizData.config.questionCount && (!quizData.questions || parseInt(quizData.config.questionCount) < currentQuestionCount)) {
            // Nếu có cấu hình số câu hỏi và nó nhỏ hơn số câu đã import, hiển thị số cấu hình
            currentQuestionCount = quizData.config.questionCount;
        }
        document.getElementById('infoQuestionCount').textContent = currentQuestionCount;
        document.getElementById('infoDuration').textContent = (quizData.config?.duration || durationMinutesInput.value || '-') + ' phút';
        document.getElementById('infoStudentCount').textContent = quizData.students ? quizData.students.length : '-';
    }


    // --- STEP 1: Questions Upload ---
    function preventDefaults(e) {  e.preventDefault(); e.stopPropagation(); }
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => { 
        questionUploadArea.addEventListener(eventName, preventDefaults, false);
        document.body.addEventListener(eventName, preventDefaults, false);
    });
    ['dragenter', 'dragover'].forEach(eventName => { 
        questionUploadArea.addEventListener(eventName, () => questionUploadArea.classList.add('highlight', 'bg-light'), false);
    });
    ['dragleave', 'drop'].forEach(eventName => { 
        questionUploadArea.addEventListener(eventName, () => questionUploadArea.classList.remove('highlight', 'bg-light'), false);
    });
    questionUploadArea.addEventListener('drop', handleQuestionFileDrop, false);
    questionUploadArea.addEventListener('click', () => questionFileInput.click());
    questionFileInput.addEventListener('change', handleQuestionFileSelect);
    function handleQuestionFileDrop(e) {  const dt = e.dataTransfer; const files = dt.files; processQuestionFiles(files); }
    function handleQuestionFileSelect(e) {  const files = e.target.files; processQuestionFiles(files); }

    async function processQuestionFiles(files) {
        if (files.length === 0) { alert('Không có file nào được chọn.'); return; }
        const file = files[0];
        const allowedExtensions = /(\.xlsx|\.xls)$/i;
        if (!allowedExtensions.exec(file.name)) { 
            alert('Chỉ chấp nhận file .xlsx hoặc .xls.'); 
            questionFileInput.value = ''; 
            return; 
        }
        
        questionUploadArea.querySelector('h4').textContent = `Đang xử lý: ${file.name}...`;
        const formData = new FormData();
        formData.append('file', file);
        
        try {
            const response = await fetch('http://localhost:8888/api/v1/quiz-creation/verify/questions', { 
                method: 'POST', 
                body: formData 
            });
            console.log(response);
            // Kiểm tra content-type của response
            const contentType = response.headers.get('content-type');
            if (!contentType || !contentType.includes('application/json')) {
                // Nếu không phải JSON, hiển thị nội dung text để debug
                const textResponse = await response.text();
                console.error('Response không phải JSON:', textResponse);
                throw new Error(`Server trả về không phải JSON. Vui lòng kiểm tra kết nối đến server.`);
            }
            
            if (!response.ok) {
                const errorData = await response.json();
                console.error('Lỗi từ server:', errorData);
                
                // Hiển thị lỗi và cho phép người dùng chọn file khác
                if (errorData.result && Array.isArray(errorData.result) && errorData.result.length > 0) {
                    const errorList = errorData.result.map(err => `• ${err}`).join('\n');
                    const errorMessage = `${errorData.message || 'Lỗi khi xác thực câu hỏi'}:\n\n${errorList}`;
                    alert(errorMessage);
                } else {
                    alert(`Lỗi khi xác thực câu hỏi: ${errorData.message || 'Vui lòng kiểm tra lại file'}`);
                }
                
                // Hiển thị thông báo lỗi trong bảng xem trước
                questionPreviewBody.innerHTML = `<tr><td colspan="7" class="text-danger">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    File không hợp lệ: ${errorData.message || 'Vui lòng chọn file khác'}
                </td></tr>`;
                questionPreviewPanel.classList.remove('hidden');
                questionUploadArea.classList.add('hidden');
                
                quizData.questions = null;
                nextToStep2Button.disabled = true;
                return;
            }
            
            const questions = await response.json();
            quizData.questions = questions.result || [];
            
            if (questions.result && questions.result.length > 0) {
                displayQuestionPreview(questions);
                questionPreviewPanel.classList.remove('hidden');
                questionUploadArea.classList.add('hidden');
                nextToStep2Button.disabled = false;
                
                // Tự động điền số lượng câu hỏi vào ô cấu hình ở Step 2
                if (questionCountInput) questionCountInput.value = questions.result.length;
            } else {
                questionPreviewBody.innerHTML = '<tr><td colspan="7" class="text-warning">Không có câu hỏi nào được tìm thấy hoặc file trống.</td></tr>';
                questionPreviewPanel.classList.remove('hidden');
                questionUploadArea.classList.add('hidden');
                nextToStep2Button.disabled = true;
            }
            updateQuizInfoPanel();
        } catch (error) {
            console.error('Lỗi khi upload hoặc xử lý file câu hỏi:', error);
            alert(`Đã xảy ra lỗi khi tải câu hỏi: ${error.message}`);
            
            // Hiển thị thông báo lỗi trong bảng xem trước
            questionPreviewBody.innerHTML = `<tr><td colspan="7" class="text-danger">
                <i class="fas fa-exclamation-circle me-2"></i>
                Lỗi: ${error.message}
            </td></tr>`;
            questionPreviewPanel.classList.remove('hidden');
            questionUploadArea.classList.add('hidden');
            
            quizData.questions = null; 
            nextToStep2Button.disabled = true;
        } finally {
            questionUploadArea.querySelector('h4').textContent = 'Kéo & Thả tệp hoặc Nhấp để chọn';
            questionFileInput.value = '';
        }
    }
    function displayQuestionPreview(questions) {
        questionPreviewBody.innerHTML = '';
        // Check if we're getting the wrapper object or direct array
        const questionData = questions.result || questions;

        if (!questionData || questionData.length === 0) {
            questionPreviewBody.innerHTML = '<tr><td colspan="7">Không có câu hỏi nào được tìm thấy.</td></tr>';
            return;
        }

        questionData.forEach((q, index) => {
            const row = questionPreviewBody.insertRow();
            row.insertCell().textContent = index + 1;
            row.insertCell().textContent = q.content || 'N/A';
            row.insertCell().textContent = q.option_a || 'N/A';
            row.insertCell().textContent = q.option_b || 'N/A';
            row.insertCell().textContent = q.option_c || 'N/A';
            row.insertCell().textContent = q.option_d || 'N/A';
            row.insertCell().textContent = q.answer_key || 'N/A';
        });
    }
    nextToStep2Button.addEventListener('click', function () { 
        if (quizData.questions && quizData.questions.length > 0) { showStep(1); }
        else { alert('Vui lòng tải lên danh sách câu hỏi hợp lệ trước khi tiếp tục.'); }
    });
    nextToStep2Button.disabled = true;

    // Thêm event listener cho nút "Chọn file khác" ở bước 1
    const chooseAnotherQuestionFile = document.getElementById('chooseAnotherQuestionFile');
    if (chooseAnotherQuestionFile) {
        chooseAnotherQuestionFile.addEventListener('click', function() {
            // Ẩn bảng xem trước câu hỏi
            questionPreviewPanel.classList.add('hidden');
            
            // Hiển thị lại khu vực tải lên file
            questionUploadArea.classList.remove('hidden');
            
            // Reset nội dung của bảng xem trước (tùy chọn)
            questionPreviewBody.innerHTML = '';
            
            // Reset giá trị của input file để người dùng có thể chọn lại cùng một file nếu muốn
            questionFileInput.value = '';
            
            // Vô hiệu hóa nút "Tiếp theo" cho đến khi file mới được tải lên
            nextToStep2Button.disabled = true;
                    
            quizData.questions = null;
            updateQuizInfoPanel();
            
            console.log("Đã chuyển về chế độ tải file câu hỏi mới");
        });
    }

    // --- STEP 2: Quiz Configuration ---
    backToStep1Button.addEventListener('click', function () {
        showStep(0);
    });

    nextToStep3Button.addEventListener('click', function () {
        if (configForm.checkValidity()) { // Use HTML5 form validation first
            const configuredQuestionCount = parseInt(questionCountInput.value, 10);

            // Check if questions have been imported
            if (!quizData.questions || quizData.questions.length === 0) {
                alert('Vui lòng import danh sách câu hỏi ở Bước 1 trước.');
                showStep(0);
                return;
            }

            // Validate question count
            if (isNaN(configuredQuestionCount) || configuredQuestionCount <= 0) {
                alert('Số lượng câu hỏi trong cấu hình phải là một số dương.');
                questionCountInput.focus();
                return;
            }

            if (configuredQuestionCount > quizData.questions.length) {
                alert(`Số lượng câu hỏi cấu hình (${configuredQuestionCount}) không được lớn hơn số câu hỏi đã import (${quizData.questions.length}). Vui lòng điều chỉnh.`);
                questionCountInput.focus();
                return;
            }

            // Validate dates
            const startDateValue = new Date(startDateInput.value);
            const endDateValue = new Date(endDateInput.value);
            if (endDateValue <= startDateValue) {
                alert('Ngày kết thúc phải sau ngày bắt đầu. Vui lòng kiểm tra lại.');
                endDateInput.focus();
                return;
            }

            // Temporarily disable the button during API validation
            nextToStep3Button.disabled = true;
            nextToStep3Button.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Đang kiểm tra...';

            // Create config data to send to API for validation
            const configData = {
                title: quizTitleInput.value,
                durationMinutes: parseInt(durationMinutesInput.value),
                maxScore: parseInt(passingScoreInput.value),
                questionCount: configuredQuestionCount,
                start: startDateInput.value,
                end: endDateInput.value
            };
            console.log(configData);
            // Call API to verify configuration
            fetch('http://localhost:8888/api/v1/quiz-creation/verify/quiz-config', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(configData)
            })
                .then(response => {
                    console.log(response);
                    if (!response.ok) {
                        return response.json().then(errorData => {
                            throw errorData; // Pass the entire error object, not just the message
                        });
                    }
                    return response.json();
                })
                .then(data => {
                    // If validation is successful, save config and proceed
                    quizData.config = {
                        title: quizTitleInput.value,
                        duration: durationMinutesInput.value,
                        passingScore: passingScoreInput.value,
                        questionCount: questionCountInput.value,
                        startDate: startDateInput.value,
                        endDate: endDateInput.value,
                    };

                    // Update progress indicator for step 2
                    const stepElement = document.getElementById('progress-step2');
                    stepElement.classList.add('completed');
                    stepElement.classList.remove('active');
                    stepElement.querySelector('.progress-info').textContent = 'Hoàn thành';

                    updateQuizInfoPanel();
                    showStep(2); // Proceed to step 3 (Import Students)
                })
                .catch(errorData => {
                    console.error('Lỗi khi xác thực cấu hình:', errorData);

                    // Create a more detailed error message from the validation errors
                    if (errorData.result && Array.isArray(errorData.result) && errorData.result.length > 0) {
                        const errorList = errorData.result.map(err => `• ${err}`).join('\n');
                        const errorMessage = `${errorData.message || 'Cấu hình bài thi không hợp lệ'}:\n\n${errorList}`;
                        alert(errorMessage);
                    } else {
                        // Fallback to simple error message if we don't have the expected structure
                        alert(`Cấu hình bài thi không hợp lệ: ${errorData.message || 'Vui lòng kiểm tra lại thông tin'}`);
                    }
                })
                .finally(() => {
                    nextToStep3Button.disabled = false;
                    nextToStep3Button.innerHTML = 'Tiếp theo <i class="fas fa-arrow-right ms-2"></i>';
                });
        } else {
            configForm.reportValidity();
        }
    });


    // --- STEP 3: Students Upload ---
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => { studentUploadArea.addEventListener(eventName, preventDefaults, false); });
    ['dragenter', 'dragover'].forEach(eventName => { studentUploadArea.addEventListener(eventName, () => studentUploadArea.classList.add('highlight', 'bg-light'), false); });
    ['dragleave', 'drop'].forEach(eventName => { studentUploadArea.addEventListener(eventName, () => studentUploadArea.classList.remove('highlight', 'bg-light'), false); });
    studentUploadArea.addEventListener('drop', handleStudentFileDrop, false);
    studentUploadArea.addEventListener('click', () => studentFileInput.click());
    studentFileInput.addEventListener('change', handleStudentFileSelect);
    function handleStudentFileDrop(e) { const dt = e.dataTransfer; const files = dt.files; processStudentFiles(files); }
    function handleStudentFileSelect(e) { const files = e.target.files; processStudentFiles(files); }

    async function processStudentFiles(files) { 
        if (files.length === 0) { alert('Không có file sinh viên nào được chọn.'); return; }
        const file = files[0];
        const allowedStudentExtensions = /(\.xlsx|\.xls|\.csv)$/i;
        if (!allowedStudentExtensions.exec(file.name)) { 
            alert('Chỉ chấp nhận file .xlsx, .xls hoặc .csv cho danh sách sinh viên.'); 
            studentFileInput.value = ''; 
            return; 
        }

        studentUploadArea.querySelector('h4').textContent = `Đang xử lý file sinh viên: ${file.name}...`;
        allDataValidationFeedback.classList.remove('hidden');
        allDataValidationFeedback.querySelector('.alert').className = 'alert alert-info';
        allDataValidationMessage.textContent = 'Đang tải và xử lý file sinh viên...';
        
        const formData = new FormData();
        formData.append('file', file);
        
        try {
            const response = await fetch('http://localhost:8888/api/v1/quiz-creation/verify/students', { 
                method: 'POST', 
                body: formData 
            });
            console.log(response);
            // Kiểm tra content-type của response
            const contentType = response.headers.get('content-type');
            if (!contentType || !contentType.includes('application/json')) {
                // Nếu không phải JSON, hiển thị nội dung text để debug
                const textResponse = await response.text();
                console.error('Response không phải JSON:', textResponse);
                throw new Error(`Server trả về không phải JSON. Vui lòng kiểm tra kết nối đến server.`);
            }
            
            if (!response.ok) {
                const errorData = await response.json();
                console.error('Lỗi từ server:', errorData);
                
                // Hiển thị lỗi và cho phép người dùng chọn file khác
                if (errorData.result && Array.isArray(errorData.result) && errorData.result.length > 0) {
                    const errorList = errorData.result.map(err => `• ${err}`).join('\n');
                    const errorMessage = `${errorData.message || 'Lỗi khi xác thực sinh viên'}:\n\n${errorList}`;
                    alert(errorMessage);
                } else {
                    alert(`Lỗi khi xác thực sinh viên: ${errorData.message || 'Vui lòng kiểm tra lại file'}`);
                }
                
                // Hiển thị thông báo lỗi trong bảng xem trước
                studentPreviewBody.innerHTML = `<tr><td colspan="5" class="text-danger">
                    <i class="fas fa-exclamation-circle me-2"></i>
                    File không hợp lệ: ${errorData.message || 'Vui lòng chọn file khác'}
                </td></tr>`;
                studentPreviewPanel.classList.remove('hidden');
                studentUploadArea.classList.add('hidden');
                
                quizData.students = null;
                validateAndCreateQuizButton.disabled = true;
                allDataValidationFeedback.querySelector('.alert').className = 'alert alert-danger';
                allDataValidationMessage.textContent = 'Lỗi khi tải danh sách sinh viên: ' + (errorData.message || 'Vui lòng kiểm tra lại file');
                return;
            }
            
            const data = await response.json();
            quizData.students = data.result || [];

            if (data.result && data.result.length > 0) {
                displayStudentPreview(data);
                studentPreviewPanel.classList.remove('hidden');
                studentUploadArea.classList.add('hidden');
                validateAndCreateQuizButton.disabled = false;
                allDataValidationFeedback.querySelector('.alert').className = 'alert alert-success';
                allDataValidationMessage.textContent = `Đã tải lên thành công ${data.result.length} sinh viên.`;
                studentCountSpan.textContent = data.result.length;

                // Update progress indicator for step 3
                const stepElement = document.getElementById('progress-step3');
                stepElement.classList.add('completed');
                stepElement.classList.remove('active');
                stepElement.querySelector('.progress-info').textContent = 'Hoàn thành';
            } else {
                studentPreviewBody.innerHTML = '<tr><td colspan="5" class="text-warning">Không có sinh viên nào được tìm thấy hoặc file trống.</td></tr>';
                studentPreviewPanel.classList.remove('hidden');
                studentUploadArea.classList.add('hidden');
                validateAndCreateQuizButton.disabled = true;
                allDataValidationFeedback.querySelector('.alert').className = 'alert alert-warning';
                allDataValidationMessage.textContent = 'Không tìm thấy dữ liệu sinh viên trong file.';
            }
            updateQuizInfoPanel();
        } catch (error) {
            console.error('Lỗi khi upload hoặc xử lý file sinh viên:', error);
            alert(`Đã xảy ra lỗi khi tải danh sách sinh viên: ${error.message}`);
            
            // Hiển thị thông báo lỗi trong bảng xem trước
            studentPreviewBody.innerHTML = `<tr><td colspan="5" class="text-danger">
                <i class="fas fa-exclamation-circle me-2"></i>
                Lỗi: ${error.message}
            </td></tr>`;
            studentPreviewPanel.classList.remove('hidden');
            studentUploadArea.classList.add('hidden');
            
            quizData.students = null;
            validateAndCreateQuizButton.disabled = true;
            allDataValidationFeedback.querySelector('.alert').className = 'alert alert-danger';
            allDataValidationMessage.textContent = 'Lỗi khi tải danh sách sinh viên: ' + error.message;
        } finally {
            studentUploadArea.querySelector('h4').textContent = 'Kéo & Thả tệp hoặc Nhấp để chọn';
            studentFileInput.value = '';
        }
    }

    function displayStudentPreview(students) {
        studentPreviewBody.innerHTML = ''; 

        // Check if we're getting the wrapper object or direct array
        const studentData = students.result || students;

        if (!studentData || !Array.isArray(studentData) || studentData.length === 0) {
            studentPreviewBody.innerHTML = '<tr><td colspan="5">Không có dữ liệu sinh viên để hiển thị.</td></tr>';
            return;
        }

        studentData.forEach((s, index) => {
            const row = studentPreviewBody.insertRow();

            row.insertCell().textContent = index + 1; // Số thứ tự
            row.insertCell().textContent = s.student_code || 'N/A';
            const fullName = `${s.last_name || ''} ${s.first_name || ''}`.trim();
            row.insertCell().textContent = fullName || 'N/A';
            row.insertCell().textContent = s.email || 'N/A';
            row.insertCell().textContent = s.class_name || 'N/A';
        });
    }
    backToStep2ButtonFromStep3.addEventListener('click', function () { showStep(1); });
    validateAndCreateQuizButton.addEventListener('click', function () {
        if (!quizData.questions || quizData.questions.length === 0) {
            alert('Thiếu dữ liệu câu hỏi. Vui lòng quay lại bước 1.');
            showStep(0);
            return;
        }

        if (!quizData.config) {
            alert('Thiếu thông tin cấu hình. Vui lòng quay lại bước 2.');
            showStep(1);
            return;
        }

        if (!quizData.students || quizData.students.length === 0) {
            alert('Thiếu dữ liệu sinh viên. Vui lòng tải lên danh sách sinh viên ở bước 3.');
            return;
        }

        allDataValidationFeedback.querySelector('.alert').className = 'alert alert-warning';
        allDataValidationMessage.textContent = 'Đang gửi dữ liệu tạo bài thi...';
        this.disabled = true;

        // Tạo dữ liệu theo định dạng mới yêu cầu
        const finalQuizData = {
            quizTitle: quizData.config.title,
            students: quizData.students,
            quizConfig: {
                durationMinutes: parseInt(quizData.config.duration),
                questionCount: parseInt(quizData.config.questionCount),
                maxScore: parseInt(quizData.config.passingScore),
                rules: "",
                start: quizData.config.startDate,
                end: quizData.config.endDate
            },
            questions: quizData.questions
        };

        fetch('http://localhost:8888/api/v1/quiz-creation/create-quiz', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(finalQuizData)
        })
            .then(response => {
                if (!response.ok) {
                    return response.json().then(err => {
                        throw new Error(err.message || 'Lỗi từ server tạo bài thi')
                    });
                }
                return response.json();
            })
            .then(data => {
                allDataValidationFeedback.querySelector('.alert').className = 'alert alert-success';
                allDataValidationMessage.textContent = 'Bài thi đã được tạo thành công!';

                // Update to use the correct field name from the API response
                quizCodeDiv.textContent = data.result.code || 'QZ-DEMO-' + Date.now();

                studentCountSpan.textContent = quizData.students.length;

                // First navigate to the final step
                showStep(3); // Chuyển sang Step 4: Result

                const stepElement = document.getElementById('progress-step4');
                stepElement.classList.add('completed');
                stepElement.classList.remove('active');
                stepElement.querySelector('.progress-info').textContent = 'Hoàn thành';
            })
            .catch(error => {
                console.error('Lỗi khi tạo bài thi:', error);
                allDataValidationFeedback.querySelector('.alert').className = 'alert alert-danger';
                allDataValidationMessage.textContent = 'Lỗi khi tạo bài thi: ' + error.message;
            })
            .finally(() => {
                this.disabled = false;
            });
    });
    validateAndCreateQuizButton.disabled = true;

    // Tương tự, thêm event listener cho nút "Chọn file khác" ở bước 3 (danh sách sinh viên)
    const chooseAnotherStudentFile = document.getElementById('chooseAnotherStudentFile');
    if (chooseAnotherStudentFile) {
        chooseAnotherStudentFile.addEventListener('click', function() {
            // Ẩn bảng xem trước sinh viên
            studentPreviewPanel.classList.add('hidden');
            
            // Hiển thị lại khu vực tải lên file
            studentUploadArea.classList.remove('hidden');
            
            // Reset nội dung của bảng xem trước (tùy chọn)
            studentPreviewBody.innerHTML = '';
            
            // Reset giá trị của input file để người dùng có thể chọn lại cùng một file nếu muốn
            studentFileInput.value = '';
            
            // Vô hiệu hóa nút "Xác thực và Tạo bài thi" cho đến khi file mới được tải lên
            validateAndCreateQuizButton.disabled = true;
            
            // Cập nhật thông tin phản hồi
            allDataValidationFeedback.querySelector('.alert').className = 'alert alert-info';
            allDataValidationMessage.textContent = 'Vui lòng tải lên danh sách sinh viên.';
            
            // Cập nhật thông tin bên sidebar (nếu cần)
            // Nếu bạn muốn giữ lại dữ liệu sinh viên đã tải, hãy comment dòng dưới đây
            quizData.students = null;
            updateQuizInfoPanel();
            
            console.log("Đã chuyển về chế độ tải file sinh viên mới");
        });
    }

    // --- STEP 4: Result ---
    copyQuizCodeButton.addEventListener('click', function () { 
        const quizCode = quizCodeDiv.textContent; navigator.clipboard.writeText(quizCode)
            .then(() => {
                this.innerHTML = '<i class="fas fa-check me-1"></i> Đã sao chép'; setTimeout(() => {
                    this.innerHTML = '<i class="fas fa-copy me-1"></i> Sao chép';
                }, 2000);
            });
    });
    viewQuizDetailsButton.addEventListener('click', function () { 
        alert('Chức năng xem chi tiết bài thi sẽ được triển khai sau.');
    });
    createNewQuizButton.addEventListener('click', function () { 
        quizData = { questions: null, config: null, students: null };
        configForm.reset(); questionFileInput.value = ''; studentFileInput.value = '';
        document.querySelectorAll('.preview-panel').forEach(el => el.classList.add('hidden'));
        questionUploadArea.classList.remove('hidden'); studentUploadArea.classList.remove('hidden');
        nextToStep2Button.disabled = true; validateAndCreateQuizButton.disabled = true;
        updateQuizInfoPanel(); document.getElementById('infoStudentCount').textContent = '-';
        showStep(0);
    });

    // Initialize
    showStep(0);
    updateQuizInfoPanel();
});
