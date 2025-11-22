<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Gemini AI Chatbot Widget -->
<style>
    /* Chatbot Button */
    #chatbot-button {
        position: fixed;
        bottom: 25px;
        right: 25px;
        width: 60px;
        height: 60px;
        border-radius: 50%;
        background: linear-gradient(135deg, #28a745, #4DC0B5);
        color: white;
        border: none;
        cursor: pointer;
        box-shadow: 0 4px 20px rgba(40, 167, 69, 0.4);
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 28px;
        transition: all 0.3s ease;
        z-index: 9999;
    }

    #chatbot-button:hover {
        transform: scale(1.1);
        box-shadow: 0 6px 25px rgba(40, 167, 69, 0.6);
    }

    #chatbot-button i {
        transition: transform 0.3s ease;
    }

    #chatbot-button.active i {
        transform: rotate(180deg);
    }

    /* Chatbot Container */
    #chatbot-container {
        position: fixed;
        bottom: 100px;
        right: 25px;
        width: 380px;
        height: 550px;
        background: white;
        border-radius: 16px;
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
        display: none;
        flex-direction: column;
        overflow: hidden;
        z-index: 9998;
        animation: slideUp 0.3s ease-out;
    }

    #chatbot-container.show {
        display: flex;
    }

    @keyframes slideUp {
        from {
            opacity: 0;
            transform: translateY(20px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    /* Chatbot Header */
    #chatbot-header {
        background: linear-gradient(135deg, #28a745, #4DC0B5);
        color: white;
        padding: 16px 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    #chatbot-header h3 {
        margin: 0;
        font-size: 18px;
        font-weight: 600;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    #chatbot-header .status-dot {
        width: 8px;
        height: 8px;
        background: #fff;
        border-radius: 50%;
        animation: pulse 2s infinite;
    }

    @keyframes pulse {
        0%, 100% { opacity: 1; }
        50% { opacity: 0.5; }
    }

    #chatbot-close {
        background: none;
        border: none;
        color: white;
        font-size: 24px;
        cursor: pointer;
        padding: 0;
        width: 30px;
        height: 30px;
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%;
        transition: background 0.2s;
    }

    #chatbot-close:hover {
        background: rgba(255, 255, 255, 0.2);
    }

    /* Chatbot Messages */
    #chatbot-messages {
        flex: 1;
        overflow-y: auto;
        padding: 20px;
        background: #f8f9fa;
    }

    #chatbot-messages::-webkit-scrollbar {
        width: 6px;
    }

    #chatbot-messages::-webkit-scrollbar-thumb {
        background: #28a745;
        border-radius: 3px;
    }

    .chat-message {
        margin-bottom: 15px;
        display: flex;
        gap: 10px;
        animation: fadeIn 0.3s ease-out;
    }

    @keyframes fadeIn {
        from {
            opacity: 0;
            transform: translateY(10px);
        }
        to {
            opacity: 1;
            transform: translateY(0);
        }
    }

    .chat-message.user {
        flex-direction: row-reverse;
    }

    .chat-avatar {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        flex-shrink: 0;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
    }

    .chat-message.bot .chat-avatar {
        background: linear-gradient(135deg, #28a745, #4DC0B5);
        color: white;
    }

    .chat-message.user .chat-avatar {
        background: #6c757d;
        color: white;
    }

    .chat-bubble {
        max-width: 75%;
        padding: 12px 16px;
        border-radius: 16px;
        word-wrap: break-word;
        line-height: 1.5;
    }

    .chat-message.bot .chat-bubble {
        background: white;
        color: #333;
        border-bottom-left-radius: 4px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }

    .chat-message.user .chat-bubble {
        background: linear-gradient(135deg, #28a745, #4DC0B5);
        color: white;
        border-bottom-right-radius: 4px;
    }

    .chat-time {
        font-size: 11px;
        color: #999;
        margin-top: 4px;
        padding: 0 16px;
    }

    .chat-message.user .chat-time {
        text-align: right;
    }

    /* Typing Indicator */
    .typing-indicator {
        display: none;
        padding: 12px 16px;
        background: white;
        border-radius: 16px;
        width: fit-content;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }

    .typing-indicator.show {
        display: block;
    }

    .typing-indicator span {
        height: 8px;
        width: 8px;
        background: #28a745;
        border-radius: 50%;
        display: inline-block;
        margin-right: 4px;
        animation: typing 1.4s infinite;
    }

    .typing-indicator span:nth-child(2) {
        animation-delay: 0.2s;
    }

    .typing-indicator span:nth-child(3) {
        animation-delay: 0.4s;
    }

    @keyframes typing {
        0%, 60%, 100% {
            transform: translateY(0);
            opacity: 0.7;
        }
        30% {
            transform: translateY(-8px);
            opacity: 1;
        }
    }

    /* Chatbot Input */
    #chatbot-input-area {
        padding: 16px;
        background: white;
        border-top: 1px solid #e9ecef;
        display: flex;
        gap: 10px;
    }

    #chatbot-input {
        flex: 1;
        border: 1px solid #e9ecef;
        border-radius: 24px;
        padding: 10px 16px;
        font-size: 14px;
        outline: none;
        transition: border-color 0.2s;
    }

    #chatbot-input:focus {
        border-color: #28a745;
    }

    #chatbot-send {
        width: 44px;
        height: 44px;
        border-radius: 50%;
        background: linear-gradient(135deg, #28a745, #4DC0B5);
        color: white;
        border: none;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        transition: all 0.2s;
    }

    #chatbot-send:hover {
        transform: scale(1.05);
        box-shadow: 0 2px 10px rgba(40, 167, 69, 0.4);
    }

    #chatbot-send:disabled {
        background: #6c757d;
        cursor: not-allowed;
        transform: none;
    }

    /* Welcome Message */
    .welcome-message {
        text-align: center;
        padding: 40px 20px;
        color: #6c757d;
    }

    .welcome-message i {
        font-size: 48px;
        color: #28a745;
        margin-bottom: 16px;
    }

    .welcome-message h4 {
        color: #333;
        margin-bottom: 8px;
    }

    .welcome-message p {
        font-size: 14px;
        margin-bottom: 20px;
    }

    .quick-questions {
        display: flex;
        flex-direction: column;
        gap: 8px;
        margin-top: 16px;
    }

    .quick-question-btn {
        background: white;
        border: 1px solid #28a745;
        color: #28a745;
        padding: 10px 16px;
        border-radius: 20px;
        cursor: pointer;
        font-size: 13px;
        transition: all 0.2s;
        text-align: left;
    }

    .quick-question-btn:hover {
        background: #28a745;
        color: white;
    }
</style>

<!-- Chatbot Button -->
<button id="chatbot-button" title="Chat với AI Assistant">
    <i class="fa fa-comments"></i>
</button>

<!-- Chatbot Container -->
<div id="chatbot-container">
    <div id="chatbot-header">
        <h3>
            <span class="status-dot"></span>
            Rentaly AI Assistant
        </h3>
        <button id="chatbot-close">×</button>
    </div>

    <div id="chatbot-messages">
        <div class="welcome-message">
            <i class="fa fa-robot"></i>
            <h4>Xin chào! 👋</h4>
            <p>Tôi là trợ lý AI của Rentaly. Tôi có thể giúp bạn:</p>
            <div class="quick-questions">
                <button class="quick-question-btn" data-question="Làm thế nào để đặt xe?">
                    📝 Làm thế nào để đặt xe?
                </button>
                <button class="quick-question-btn" data-question="Các hình thức thanh toán là gì?">
                    💳 Các hình thức thanh toán?
                </button>
                <button class="quick-question-btn" data-question="Chính sách hủy xe như thế nào?">
                    ❌ Chính sách hủy xe?
                </button>
                <button class="quick-question-btn" data-question="Làm thế nào để trở thành chủ xe?">
                    🚗 Trở thành chủ xe?
                </button>
            </div>
        </div>
        <div class="typing-indicator">
            <span></span><span></span><span></span>
        </div>
    </div>

    <div id="chatbot-input-area">
        <input type="text" id="chatbot-input" placeholder="Nhập câu hỏi của bạn..." />
        <button id="chatbot-send" disabled>
            <i class="fa fa-paper-plane"></i>
        </button>
    </div>
</div>

<script>
(function() {
    const chatbotButton = document.getElementById('chatbot-button');
    const chatbotContainer = document.getElementById('chatbot-container');
    const chatbotClose = document.getElementById('chatbot-close');
    const chatbotInput = document.getElementById('chatbot-input');
    const chatbotSend = document.getElementById('chatbot-send');
    const chatbotMessages = document.getElementById('chatbot-messages');
    const typingIndicator = document.querySelector('.typing-indicator');
    const welcomeMessage = document.querySelector('.welcome-message');

    let isFirstMessage = true;

    // Toggle chatbot
    chatbotButton.addEventListener('click', function() {
        const isVisible = chatbotContainer.classList.contains('show');
        if (isVisible) {
            chatbotContainer.classList.remove('show');
            chatbotButton.classList.remove('active');
        } else {
            chatbotContainer.classList.add('show');
            chatbotButton.classList.add('active');
            chatbotInput.focus();
        }
    });

    chatbotClose.addEventListener('click', function() {
        chatbotContainer.classList.remove('show');
        chatbotButton.classList.remove('active');
    });

    // Enable send button when input has text
    chatbotInput.addEventListener('input', function() {
        chatbotSend.disabled = this.value.trim() === '';
    });

    // Send message on Enter key
    chatbotInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && !chatbotSend.disabled) {
            sendMessage();
        }
    });

    // Send message on button click
    chatbotSend.addEventListener('click', sendMessage);

    // Quick question buttons
    document.querySelectorAll('.quick-question-btn').forEach(btn => {
        btn.addEventListener('click', function() {
            const question = this.getAttribute('data-question');
            chatbotInput.value = question;
            chatbotSend.disabled = false;
            sendMessage();
        });
    });

    function sendMessage() {
        const message = chatbotInput.value.trim();
        if (!message) return;

        // Hide welcome message on first interaction
        if (isFirstMessage) {
            welcomeMessage.style.display = 'none';
            isFirstMessage = false;
        }

        // Add user message
        addMessage('user', message);
        chatbotInput.value = '';
        chatbotSend.disabled = true;

        // Show typing indicator
        typingIndicator.classList.add('show');
        scrollToBottom();

        // Send to server
        fetch('${pageContext.request.contextPath}/api/chatbot', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ message: message })
        })
        .then(response => response.json())
        .then(data => {
            typingIndicator.classList.remove('show');
            if (data.success) {
                addMessage('bot', data.message);
            } else {
                addMessage('bot', 'Xin lỗi, đã xảy ra lỗi: ' + (data.error || 'Unknown error'));
            }
        })
        .catch(error => {
            typingIndicator.classList.remove('show');
            console.error('Error:', error);
            addMessage('bot', 'Xin lỗi, không thể kết nối đến server. Vui lòng thử lại sau.');
        });
    }

    function addMessage(type, content) {
        const messageDiv = document.createElement('div');
        messageDiv.className = 'chat-message ' + type;

        const avatar = document.createElement('div');
        avatar.className = 'chat-avatar';
        avatar.innerHTML = type === 'bot' ? '<i class="fa fa-robot"></i>' : '<i class="fa fa-user"></i>';

        const bubble = document.createElement('div');
        bubble.className = 'chat-bubble';
        bubble.textContent = content;

        messageDiv.appendChild(avatar);
        messageDiv.appendChild(bubble);

        // Insert before typing indicator
        chatbotMessages.insertBefore(messageDiv, typingIndicator);
        scrollToBottom();
    }

    function scrollToBottom() {
        chatbotMessages.scrollTop = chatbotMessages.scrollHeight;
    }

    // Load chat history on page load
    fetch('${pageContext.request.contextPath}/api/chatbot?action=history')
        .then(response => response.json())
        .then(data => {
            if (data.success && data.messages && data.messages.length > 0) {
                welcomeMessage.style.display = 'none';
                isFirstMessage = false;
                data.messages.forEach(msg => {
                    addMessage(msg.role === 'user' ? 'user' : 'bot', msg.content);
                });
            }
        })
        .catch(error => {
            console.error('Error loading chat history:', error);
        });
})();
</script>
