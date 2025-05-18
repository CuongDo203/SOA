document.addEventListener('DOMContentLoaded', function () {
    // Step navigation
    const steps = ['step1', 'step2', 'step3', 'step4'];
    const progressSteps = ['progress-step1', 'progress-step2', 'progress-step3', 'progress-step4'];

    // Dữ liệu thu thập từ tất cả các bước
    let quizData = {
        questions: null, // Sẽ được điền từ API backend
        config: null,
        students: null // Sẽ được điền từ file Excel sinh viên (XLSX.js hoặc API backend)
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
    const subjectNameInput = document.getElementById('subjectName');
    const durationMinutesInput = document.getElementById('durationMinutes');
    const passingScoreInput = document.getElementById('passingScore');
    const questionCountInput = document.getElementById('questionCount'); // Input số lượng câu hỏi cấu hình
    const startDateInput = document.getElementById('startDate');
    const endDateInput = document.getElementById('endDate');
    const shuffleQuestionsCheckbox = document.getElementById('shuffleQuestions');
    const shuffleOptionsCheckbox = document.getElementById('shuffleOptions');
    const showResultsCheckbox = document.getElementById('showResults');
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
        if (quizData.config && quizData.config.questionCount && (!quizData.questions || parseInt(quizData.config.questionCount) < currentQuestionCount) ) {
             // Nếu có cấu hình số câu hỏi và nó nhỏ hơn số câu đã import, hiển thị số cấu hình
            currentQuestionCount = quizData.config.questionCount;
        }
        document.getElementById('infoQuestionCount').textContent = currentQuestionCount;
        document.getElementById('infoDuration').textContent = (quizData.config?.duration || durationMinutesInput.value || '-') + ' phút';
        document.getElementById('infoStudentCount').textContent = quizData.students ? quizData.students.length : '-';
    }


    // --- STEP 1: Questions Upload ---
    function preventDefaults(e) { /* ... (giữ nguyên) ... */ e.preventDefault(); e.stopPropagation(); }
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => { /* ... (giữ nguyên) ... */
        questionUploadArea.addEventListener(eventName, preventDefaults, false);
        document.body.addEventListener(eventName, preventDefaults, false);
    });
    ['dragenter', 'dragover'].forEach(eventName => { /* ... (giữ nguyên) ... */
        questionUploadArea.addEventListener(eventName, () => questionUploadArea.classList.add('highlight', 'bg-light'), false);
    });
    ['dragleave', 'drop'].forEach(eventName => { /* ... (giữ nguyên) ... */
        questionUploadArea.addEventListener(eventName, () => questionUploadArea.classList.remove('highlight', 'bg-light'), false);
    });
    questionUploadArea.addEventListener('drop', handleQuestionFileDrop, false);
    questionUploadArea.addEventListener('click', () => questionFileInput.click());
    questionFileInput.addEventListener('change', handleQuestionFileSelect);
    function handleQuestionFileDrop(e) { /* ... (giữ nguyên) ... */ const dt = e.dataTransfer; const files = dt.files; processQuestionFiles(files); }
    function handleQuestionFileSelect(e) { /* ... (giữ nguyên) ... */ const files = e.target.files; processQuestionFiles(files); }

    async function processQuestionFiles(files) {
        // ... (Logic gọi API backend cho question upload giữ nguyên như phiên bản trước) ...
        if (files.length === 0) { alert('Không có file nào được chọn.'); return; }
        const file = files[0];
        const allowedExtensions = /(\.xlsx|\.xls)$/i;
        if (!allowedExtensions.exec(file.name)) { alert('Chỉ chấp nhận file .xlsx hoặc .xls.'); questionFileInput.value = ''; return; }
        questionUploadArea.querySelector('h4').textContent = `Đang xử lý: ${file.name}...`;
        const formData = new FormData();
        formData.append('file', file);
        try {
            const response = await fetch('http://localhost:8082/api/import/questions/excel', { method: 'POST', body: formData });
            if (!response.ok) {
                let errorMsg = `Lỗi từ server: ${response.status} ${response.statusText}`;
                try { const errorData = await response.json(); errorMsg = errorData.message || errorData.error || JSON.stringify(errorData); }
                catch (e) { errorMsg = await response.text(); }
                throw new Error(errorMsg);
            }
            const questions = await response.json();
            quizData.questions = questions;
            if (questions && questions.length > 0) {
                displayQuestionPreview(questions);
                questionPreviewPanel.classList.remove('hidden');
                questionUploadArea.classList.add('hidden');
                nextToStep2Button.disabled = false;
                // Tự động điền số lượng câu hỏi vào ô cấu hình ở Step 2
                if (questionCountInput) questionCountInput.value = questions.length;
            } else if (typeof questions === 'string') {
                questionPreviewBody.innerHTML = `<tr><td colspan="7">${questions}</td></tr>`;
                questionPreviewPanel.classList.remove('hidden');
                questionUploadArea.classList.add('hidden');
                nextToStep2Button.disabled = true;
            } else {
                questionPreviewBody.innerHTML = '<tr><td colspan="7">Không có câu hỏi nào được tìm thấy hoặc file trống sau header.</td></tr>';
                questionPreviewPanel.classList.remove('hidden');
                questionUploadArea.classList.add('hidden');
                nextToStep2Button.disabled = true;
            }
            updateQuizInfoPanel();
        } catch (error) {
            console.error('Lỗi khi upload hoặc xử lý file câu hỏi:', error);
            alert(`Đã xảy ra lỗi khi tải câu hỏi: ${error.message}`);
            quizData.questions = null; nextToStep2Button.disabled = true;
            questionUploadArea.classList.remove('hidden');
            questionPreviewPanel.classList.add('hidden');
            questionPreviewBody.innerHTML = '';
        } finally {
            questionUploadArea.querySelector('h4').textContent = 'Kéo & Thả tệp hoặc Nhấp để chọn';
            questionFileInput.value = '';
        }
    }
    function displayQuestionPreview(questions) { /* ... (giữ nguyên) ... */
        questionPreviewBody.innerHTML = '';
        questions.forEach((q, index) => {
            const row = questionPreviewBody.insertRow();
            row.insertCell().textContent = index + 1;
            row.insertCell().textContent = q.content || 'N/A';
            row.insertCell().textContent = q.ansA || 'N/A';
            row.insertCell().textContent = q.ansB || 'N/A';
            row.insertCell().textContent = q.ansC || 'N/A';
            row.insertCell().textContent = q.ansD || 'N/A';
            row.insertCell().textContent = q.correctAns || 'N/A';
        });
    }
    document.getElementById('downloadSampleQuestions').addEventListener('click', function () { /* ... (giữ nguyên) ... */ alert('Chức năng tải xuống tệp mẫu câu hỏi sẽ được triển khai sau.'); });
    nextToStep2Button.addEventListener('click', function () { /* ... (giữ nguyên) ... */
        if (quizData.questions && quizData.questions.length > 0) { showStep(1); }
        else { alert('Vui lòng tải lên danh sách câu hỏi hợp lệ trước khi tiếp tục.'); }
    });
    nextToStep2Button.disabled = true;


    // --- STEP 2: Quiz Configuration ---
    backToStep1Button.addEventListener('click', function () {
        showStep(0);
    });

    nextToStep3Button.addEventListener('click', function () {
        if (configForm.checkValidity()) { // Sử dụng HTML5 form validation
            const configuredQuestionCount = parseInt(questionCountInput.value, 10);

            // Kiểm tra xem đã import câu hỏi chưa
            if (!quizData.questions || quizData.questions.length === 0) {
                alert('Vui lòng import danh sách câu hỏi ở Bước 1 trước.');
                showStep(0); // Có thể quay lại bước 1
                return;
            }

            // Kiểm tra số lượng câu hỏi cấu hình so với số câu hỏi đã import
            if (isNaN(configuredQuestionCount) || configuredQuestionCount <= 0) {
                alert('Số lượng câu hỏi trong cấu hình phải là một số dương.');
                questionCountInput.focus();
                configForm.reportValidity(); // Hiển thị lỗi cụ thể cho trường này nếu trình duyệt hỗ trợ
                return;
            }

            if (configuredQuestionCount > quizData.questions.length) {
                alert(`Số lượng câu hỏi cấu hình (${configuredQuestionCount}) không được lớn hơn số câu hỏi đã import (${quizData.questions.length}). Vui lòng điều chỉnh.`);
                questionCountInput.focus();
                configForm.reportValidity();
                return;
            }

            // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
            const startDateValue = new Date(startDateInput.value);
            const endDateValue = new Date(endDateInput.value);
            if (endDateValue <= startDateValue) {
                alert('Ngày kết thúc phải sau ngày bắt đầu. Vui lòng kiểm tra lại.');
                endDateInput.focus();
                configForm.reportValidity();
                return;
            }


            quizData.config = {
                title: quizTitleInput.value,
                subject: subjectNameInput.value,
                duration: durationMinutesInput.value,
                passingScore: passingScoreInput.value,
                questionCount: questionCountInput.value, // Đã lấy ở trên và validate
                startDate: startDateInput.value,
                endDate: endDateInput.value,
                shuffleQuestions: shuffleQuestionsCheckbox.checked,
                shuffleOptions: shuffleOptionsCheckbox.checked,
                showResults: showResultsCheckbox.checked
            };
            updateQuizInfoPanel();
            showStep(2); // Chuyển sang bước 3 (Import Sinh viên)
        } else {
            configForm.reportValidity(); // Dòng này sẽ yêu cầu trình duyệt hiển thị lỗi validation
            // Không cần alert() ở đây nữa, để trình duyệt tự xử lý
        }
    });


    // --- STEP 3: Students Upload ---
    // ... (Giữ nguyên logic Step 3 bạn đã có, bao gồm cả processStudentFiles và displayStudentPreview)
    // Đảm bảo processStudentFiles gọi API backend /api/import/students/excel như đã thảo luận trước.
    // Và displayStudentPreview hiển thị StudentDTO từ backend.
    ['dragenter', 'dragover', 'dragleave', 'drop'].forEach(eventName => { studentUploadArea.addEventListener(eventName, preventDefaults, false); });
    ['dragenter', 'dragover'].forEach(eventName => { studentUploadArea.addEventListener(eventName, () => studentUploadArea.classList.add('highlight', 'bg-light'), false); });
    ['dragleave', 'drop'].forEach(eventName => { studentUploadArea.addEventListener(eventName, () => studentUploadArea.classList.remove('highlight', 'bg-light'), false); });
    studentUploadArea.addEventListener('drop', handleStudentFileDrop, false);
    studentUploadArea.addEventListener('click', () => studentFileInput.click());
    studentFileInput.addEventListener('change', handleStudentFileSelect);
    function handleStudentFileDrop(e) { const dt = e.dataTransfer; const files = dt.files; processStudentFiles(files); }
    function handleStudentFileSelect(e) { const files = e.target.files; processStudentFiles(files); }

    async function processStudentFiles(files) { // ĐÂY LÀ PHIÊN BẢN GỌI API BACKEND
        if (files.length === 0) { alert('Không có file sinh viên nào được chọn.'); return; }
        const file = files[0];
        const allowedStudentExtensions = /(\.xlsx|\.xls|\.csv)$/i;
        if (!allowedStudentExtensions.exec(file.name)) { alert('Chỉ chấp nhận file .xlsx, .xls hoặc .csv cho danh sách sinh viên.'); studentFileInput.value = ''; return; }

        studentUploadArea.querySelector('h4').textContent = `Đang xử lý file sinh viên: ${file.name}...`;
        allDataValidationFeedback.classList.remove('hidden');
        allDataValidationFeedback.querySelector('.alert').className = 'alert alert-info';
        allDataValidationMessage.textContent = 'Đang tải và xử lý file sinh viên...';
        const formData = new FormData();
        formData.append('file', file);
        try {
            const response = await fetch('http://localhost:8082/api/import/students/excel', { method: 'POST', body: formData });
            if (!response.ok) {
                let errorMsg = `Lỗi từ server khi tải sinh viên: ${response.status} ${response.statusText}`;
                try { const errorData = await response.json(); errorMsg = errorData.message || errorData.error || JSON.stringify(errorData); } catch (e) { errorMsg = await response.text(); }
                throw new Error(errorMsg);
            }
            const students = await response.json();
            quizData.students = students;
            if (students && students.length > 0) {
                displayStudentPreview(students);
                studentPreviewPanel.classList.remove('hidden');
                studentUploadArea.classList.add('hidden');
                validateAndCreateQuizButton.disabled = false;
                allDataValidationFeedback.querySelector('.alert').className = 'alert alert-success';
                allDataValidationMessage.textContent = `Đã tải lên thành công ${students.length} sinh viên.`;
                studentCountSpan.textContent = students.length; // Cập nhật span ở Step 4
            } else if (typeof students === 'string') {
                studentPreviewBody.innerHTML = `<tr><td colspan="5">${students}</td></tr>`;
                studentPreviewPanel.classList.remove('hidden'); studentUploadArea.classList.add('hidden'); validateAndCreateQuizButton.disabled = true;
            } else {
                studentPreviewBody.innerHTML = '<tr><td colspan="5">Không có sinh viên nào được tìm thấy hoặc file trống sau header.</td></tr>';
                studentPreviewPanel.classList.remove('hidden'); studentUploadArea.classList.add('hidden'); validateAndCreateQuizButton.disabled = true;
            }
            updateQuizInfoPanel();
        } catch (error) {
            console.error('Lỗi khi upload hoặc xử lý file sinh viên:', error);
            alert(`Đã xảy ra lỗi khi tải danh sách sinh viên: ${error.message}`);
            quizData.students = null; validateAndCreateQuizButton.disabled = true;
            studentUploadArea.classList.remove('hidden'); studentPreviewPanel.classList.add('hidden'); studentPreviewBody.innerHTML = '';
            allDataValidationFeedback.querySelector('.alert').className = 'alert alert-danger';
            allDataValidationMessage.textContent = 'Lỗi khi tải danh sách sinh viên: ' + error.message;
        } finally {
            studentUploadArea.querySelector('h4').textContent = 'Kéo & Thả tệp hoặc Nhấp để chọn';
            studentFileInput.value = '';
        }
    }

    function displayStudentPreview(students) {
        studentPreviewBody.innerHTML = ''; // Xóa nội dung preview cũ
        if (!students || !Array.isArray(students) || students.length === 0) {
            studentPreviewBody.innerHTML = '<tr><td colspan="5">Không có dữ liệu sinh viên để hiển thị.</td></tr>';
            return;
        }
        students.forEach((s, index) => {
            const row = studentPreviewBody.insertRow();
            console.log(`Processing student object for display (index ${index}):`, s); // GIỮ LẠI DÒNG NÀY ĐỂ DEBUG

            row.insertCell().textContent = index + 1; // Số thứ tự
            // SỬA LẠI CÁCH TRUY CẬP THUỘC TÍNH CHO KHỚP VỚI JSON TỪ BACKEND
            row.insertCell().textContent = s.student_code || 'N/A'; // Sử dụng s.student_code
            const fullName = `${s.last_name || ''} ${s.first_name || ''}`.trim(); // Sử dụng s.last_name và s.first_name
            row.insertCell().textContent = fullName || 'N/A';
            row.insertCell().textContent = s.email || 'N/A';         // s.email vẫn đúng
            row.insertCell().textContent = s.class_name || 'N/A';   // Sử dụng s.class_name
        });
    }
    backToStep2ButtonFromStep3.addEventListener('click', function () { showStep(1); });
    document.getElementById('downloadSampleStudents').addEventListener('click', function () { /* ... (giữ nguyên) ... */
        const sampleData = [ { 'Mã sinh viên': 'SV001', 'Họ': 'Nguyen Van', 'Tên': 'A', 'Email': 'b21dccn001@example.com', 'Lớp': 'D21CNPM01' }, { 'Mã sinh viên': 'SV002', 'Họ': 'Tran Thi', 'Tên': 'B', 'Email': 'b21dccn002@example.com', 'Lớp': 'D21CNPM01' } ];
        const ws = XLSX.utils.json_to_sheet(sampleData); const wb = XLSX.utils.book_new(); XLSX.utils.book_append_sheet(wb, ws, "Danh sách sinh viên"); XLSX.writeFile(wb, "mau_danh_sach_sinh_vien.xlsx");
    });
    validateAndCreateQuizButton.addEventListener('click', function () { /* ... (giữ nguyên logic gọi API tạo quiz) ... */
        if (!quizData.questions || quizData.questions.length === 0) { alert('Thiếu dữ liệu câu hỏi. Vui lòng quay lại bước 1.'); showStep(0); return; }
        if (!quizData.config) { alert('Thiếu thông tin cấu hình. Vui lòng quay lại bước 2.'); showStep(1); return; }
        if (!quizData.students || quizData.students.length === 0) { alert('Thiếu dữ liệu sinh viên. Vui lòng tải lên danh sách sinh viên ở bước 3.'); return; }
        allDataValidationFeedback.querySelector('.alert').className = 'alert alert-warning';
        allDataValidationMessage.textContent = 'Đang gửi dữ liệu tạo bài thi...'; this.disabled = true;
        const finalQuizData = { quizConfigurationDto: quizData.config, questions: quizData.questions, students: quizData.students };
        fetch('http://localhost:8083/api/quiz-creation/create-full-quiz', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(finalQuizData) })
        .then(response => { if (!response.ok) { return response.json().then(err => { throw new Error(err.message || 'Lỗi từ server tạo bài thi') }); } return response.json(); })
        .then(data => {
            allDataValidationFeedback.querySelector('.alert').className = 'alert alert-success';
            allDataValidationMessage.textContent = 'Bài thi đã được tạo thành công!';
            quizCodeDiv.textContent = data.quizCode || 'QZ-DEMO-' + Date.now();
            studentCountSpan.textContent = quizData.students.length;
            showStep(3); // Chuyển sang Step 4: Result
        })
        .catch(error => {
            console.error('Lỗi khi tạo bài thi:', error);
            allDataValidationFeedback.querySelector('.alert').className = 'alert alert-danger';
            allDataValidationMessage.textContent = 'Lỗi khi tạo bài thi: ' + error.message;
        }).finally(() => { this.disabled = false; });
    });
    validateAndCreateQuizButton.disabled = true;


    // --- STEP 4: Result ---
    copyQuizCodeButton.addEventListener('click', function () { /* ... (giữ nguyên) ... */ const quizCode = quizCodeDiv.textContent; navigator.clipboard.writeText(quizCode).then(() => { this.innerHTML = '<i class="fas fa-check me-1"></i> Đã sao chép'; setTimeout(() => { this.innerHTML = '<i class="fas fa-copy me-1"></i> Sao chép'; }, 2000); }); });
    viewQuizDetailsButton.addEventListener('click', function () { /* ... (giữ nguyên) ... */ alert('Chức năng xem chi tiết bài thi sẽ được triển khai sau.'); });
    createNewQuizButton.addEventListener('click', function () { /* ... (giữ nguyên) ... */
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