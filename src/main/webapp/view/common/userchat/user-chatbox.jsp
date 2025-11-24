<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- User-to-User Chat Widget -->
<style>
    /* Chat Button - Different from AI chatbot */
    #user-chat-button {
        position: fixed;
        bottom: 100px;  /* Different position from AI chatbot */
        right: 25px;
        width: 60px;
        height: 60px;
        border-radius: 50%;
        background: linear-gradient(135deg, #4DC0B5, #28a745);  /* Reversed gradient */
        color: white;
        border: 2px solid #fff;
        cursor: pointer;
        box-shadow: 0 4px 20px rgba(77, 192, 181, 0.4);
        display: none;  /* Hidden by default, shown when chat is available */
        align-items: center;
        justify-content: center;
        font-size: 24px;
        transition: all 0.3s ease;
        z-index: 9997;
    }

    #user-chat-button:hover {
        transform: scale(1.1);
        box-shadow: 0 6px 25px rgba(77, 192, 181, 0.6);
    }

    #user-chat-button .unread-badge {
        position: absolute;
        top: -5px;
        right: -5px;
        background: #dc3545;
        color: white;
        border-radius: 50%;
        width: 24px;
        height: 24px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 12px;
        font-weight: bold;
        border: 2px solid white;
    }

    /* Chat Container */
    #user-chat-container {
        position: fixed;
        bottom: 175px;  /* Different position */
        right: 25px;
        width: 380px;
        height: 550px;
        background: white;
        border-radius: 16px;
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
        display: none;
        flex-direction: column;
        overflow: hidden;
        z-index: 9996;
        animation: slideUp 0.3s ease-out;
    }

    #user-chat-container.show {
        display: flex;
    }

    /* Chat Header */
    #user-chat-header {
        background: linear-gradient(135deg, #4DC0B5, #28a745);
        color: white;
        padding: 16px 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
    }

    #user-chat-header h3 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
    }

    #user-chat-header .chat-info {
        font-size: 12px;
        opacity: 0.9;
    }

    #user-chat-close {
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

    #user-chat-close:hover {
        background: rgba(255, 255, 255, 0.2);
    }

    /* Messages Area */
    #user-chat-messages {
        flex: 1;
        overflow-y: auto;
        padding: 20px;
        background: #f8f9fa;
    }

    #user-chat-messages::-webkit-scrollbar {
        width: 6px;
    }

    #user-chat-messages::-webkit-scrollbar-thumb {
        background: #4DC0B5;
        border-radius: 3px;
    }

    .user-chat-message {
        margin-bottom: 15px;
        display: flex;
        gap: 10px;
        animation: fadeIn 0.3s ease-out;
    }

    .user-chat-message.sent {
        flex-direction: row-reverse;
    }

    .user-chat-avatar {
        width: 36px;
        height: 36px;
        border-radius: 50%;
        flex-shrink: 0;
        background: #6c757d;
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        font-weight: bold;
    }

    .user-chat-bubble {
        max-width: 75%;
        padding: 12px 16px;
        border-radius: 16px;
        word-wrap: break-word;
        line-height: 1.5;
    }

    .user-chat-message.received .user-chat-bubble {
        background: white;
        color: #333;
        border-bottom-left-radius: 4px;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
    }

    .user-chat-message.sent .user-chat-bubble {
        background: linear-gradient(135deg, #4DC0B5, #28a745);
        color: white;
        border-bottom-right-radius: 4px;
    }

    .user-chat-time {
        font-size: 11px;
        color: #999;
        margin-top: 4px;
    }

    .user-chat-message.sent .user-chat-time {
        text-align: right;
    }

    /* Attachment Styles */
    .chat-attachment {
        margin-top: 8px;
    }

    .chat-attachment img {
        max-width: 200px;
        max-height: 200px;
        border-radius: 8px;
        cursor: pointer;
    }

    .chat-attachment.file {
        background: rgba(255, 255, 255, 0.3);
        padding: 10px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        gap: 10px;
    }

    .user-chat-message.received .chat-attachment.file {
        background: #f0f0f0;
    }

    .chat-attachment.file i {
        font-size: 24px;
    }

    .chat-attachment.file a {
        color: inherit;
        text-decoration: none;
        font-size: 13px;
    }

    /* Typing Indicator */
    .user-typing-indicator {
        display: none;
        padding: 12px 16px;
        background: white;
        border-radius: 16px;
        width: fit-content;
        box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        margin-bottom: 15px;
    }

    .user-typing-indicator.show {
        display: block;
    }

    .user-typing-indicator span {
        height: 8px;
        width: 8px;
        background: #4DC0B5;
        border-radius: 50%;
        display: inline-block;
        margin-right: 4px;
        animation: typing 1.4s infinite;
    }

    .user-typing-indicator span:nth-child(2) {
        animation-delay: 0.2s;
    }

    .user-typing-indicator span:nth-child(3) {
        animation-delay: 0.4s;
    }

    /* Input Area */
    #user-chat-input-area {
        padding: 16px;
        background: white;
        border-top: 1px solid #e9ecef;
    }

    #user-chat-input-row {
        display: flex;
        gap: 10px;
        align-items: flex-end;
    }

    #user-chat-input {
        flex: 1;
        border: 1px solid #e9ecef;
        border-radius: 24px;
        padding: 10px 16px;
        font-size: 14px;
        outline: none;
        resize: none;
        max-height: 100px;
        min-height: 44px;
        transition: border-color 0.2s;
    }

    #user-chat-input:focus {
        border-color: #4DC0B5;
    }

    .chat-action-btn {
        width: 44px;
        height: 44px;
        border-radius: 50%;
        border: none;
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        transition: all 0.2s;
        background: #f0f0f0;
        color: #333;
    }

    .chat-action-btn:hover {
        background: #e0e0e0;
    }

    #user-chat-send {
        background: linear-gradient(135deg, #4DC0B5, #28a745);
        color: white;
    }

    #user-chat-send:hover {
        transform: scale(1.05);
        box-shadow: 0 2px 10px rgba(77, 192, 181, 0.4);
    }

    #user-chat-send:disabled {
        background: #6c757d;
        cursor: not-allowed;
        transform: none;
    }

    /* File upload */
    #chat-file-input {
        display: none;
    }

    .upload-progress {
        padding: 10px;
        background: #e3f2fd;
        border-radius: 8px;
        margin-bottom: 10px;
        font-size: 13px;
    }
</style>

<!-- Chat Button -->
<button id="user-chat-button" title="Chat với chủ xe/khách hàng">
    <i class="fa fa-comments"></i>
    <span class="unread-badge" style="display: none;">0</span>
</button>

<!-- Chat Container -->
<div id="user-chat-container">
    <div id="user-chat-header">
        <div>
            <h3 id="user-chat-title">Chat</h3>
            <div class="chat-info" id="user-chat-info"></div>
        </div>
        <button id="user-chat-close">×</button>
    </div>

    <div id="user-chat-messages">
        <div class="user-typing-indicator">
            <span></span><span></span><span></span>
        </div>
    </div>

    <div id="user-chat-input-area">
        <div id="user-chat-input-row">
            <button class="chat-action-btn" id="chat-attach-btn" title="Đính kèm file/hình ảnh">
                <i class="fa fa-paperclip"></i>
            </button>
            <textarea id="user-chat-input" placeholder="Nhập tin nhắn..."></textarea>
            <button id="user-chat-send" disabled>
                <i class="fa fa-paper-plane"></i>
            </button>
        </div>
    </div>
</div>

<input type="file" id="chat-file-input" accept="image/*,.pdf,.doc,.docx,.xls,.xlsx,.txt" />

<script>
(function() {
    const chatButton = document.getElementById('user-chat-button');
    const chatContainer = document.getElementById('user-chat-container');
    const chatClose = document.getElementById('user-chat-close');
    const chatInput = document.getElementById('user-chat-input');
    const chatSend = document.getElementById('user-chat-send');
    const chatMessages = document.getElementById('user-chat-messages');
    const chatTitle = document.getElementById('user-chat-title');
    const chatInfo = document.getElementById('user-chat-info');
    const typingIndicator = document.querySelector('.user-typing-indicator');
    const attachBtn = document.getElementById('chat-attach-btn');
    const fileInput = document.getElementById('chat-file-input');
    const unreadBadge = chatButton.querySelector('.unread-badge');

    let currentConversationId = null;
    let currentBookingId = null;
    let otherUserId = null;
    let otherUserName = '';
    let lastMessageId = 0;
    let pollInterval = null;
    let typingTimeout = null;
    let isTyping = false;

    // Initialize chat from URL parameter (bookingId)
    function initChatFromBooking(bookingId) {
        console.log('[User Chat] Initializing chat with bookingId:', bookingId);
        fetch('${pageContext.request.contextPath}/api/init-chat?bookingId=' + bookingId)
            .then(response => {
                console.log('[User Chat] Init response status:', response.status);
                return response.json();
            })
            .then(data => {
                console.log('[User Chat] Init response data:', data);
                if (data.success) {
                    currentConversationId = data.conversationId;
                    currentBookingId = data.bookingId;
                    otherUserId = data.otherUserId;
                    otherUserName = data.otherUserName;

                    chatTitle.textContent = otherUserName;
                    chatInfo.textContent = 'Về: ' + data.carName;

                    // Show chat button
                    chatButton.style.display = 'flex';
                    console.log('[User Chat] ✅ Chat button displayed!');

                    // Load messages
                    loadMessages();

                    // Start polling
                    startPolling();

                    // Auto-open chat widget
                    chatContainer.classList.add('show');
                    chatInput.focus();
                    console.log('[User Chat] ✅ Chat widget auto-opened!');
                } else {
                    console.error('[User Chat] ❌ Failed to init chat:', data.error);
                    alert('Không thể khởi tạo chat: ' + (data.error || 'Unknown error'));
                }
            })
            .catch(error => {
                console.error('[User Chat] ❌ Error initializing chat:', error);
                alert('Lỗi kết nối API: ' + error.message);
            });
    }

    // Check if current page has bookingId (e.g., in booking details page)
    const urlParams = new URLSearchParams(window.location.search);
    const bookingId = urlParams.get('bookingId');
    console.log('[User Chat] BookingId from URL:', bookingId);

    if (bookingId) {
        console.log('[User Chat] Auto-initializing with bookingId from URL');
        initChatFromBooking(bookingId);
    } else {
        console.log('[User Chat] No bookingId in URL. Chat button hidden. Call window.initUserChat(bookingId) to show.');
    }

    // Also expose global function for manual init
    window.initUserChat = initChatFromBooking;
    console.log('[User Chat] Global function window.initUserChat() is ready');

    // Toggle chatbox
    chatButton.addEventListener('click', function() {
        const isVisible = chatContainer.classList.contains('show');
        if (isVisible) {
            chatContainer.classList.remove('show');
        } else {
            chatContainer.classList.add('show');
            chatInput.focus();
            markAsRead();
            unreadBadge.style.display = 'none';
        }
    });

    chatClose.addEventListener('click', function() {
        chatContainer.classList.remove('show');
    });

    // Enable send button when input has text
    chatInput.addEventListener('input', function() {
        chatSend.disabled = this.value.trim() === '';

        // Typing indicator
        if (!isTyping && this.value.trim() !== '') {
            isTyping = true;
            updateTypingStatus(true);
        }

        clearTimeout(typingTimeout);
        typingTimeout = setTimeout(() => {
            if (isTyping) {
                isTyping = false;
                updateTypingStatus(false);
            }
        }, 3000);
    });

    // Send on Enter (Shift+Enter for new line)
    chatInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && !e.shiftKey && !chatSend.disabled) {
            e.preventDefault();
            sendMessage();
        }
    });

    chatSend.addEventListener('click', () => sendMessage());

    // File attachment
    attachBtn.addEventListener('click', () => {
        fileInput.click();
    });

    fileInput.addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            uploadFile(file);
        }
        fileInput.value = ''; // Reset
    });

    function sendMessage(attachmentUrl = null, attachmentType = null) {
        const message = chatInput.value.trim();
        if (!message && !attachmentUrl) return;
        if (!currentConversationId) return;

        const data = {
            conversationId: currentConversationId,
            content: message
        };

        if (attachmentUrl) {
            data.attachmentUrl = attachmentUrl;
            data.attachmentType = attachmentType;
        }

        fetch('${pageContext.request.contextPath}/api/user-chat?action=send', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                addMessage(data.message, true);
                chatInput.value = '';
                chatSend.disabled = true;
                lastMessageId = data.message.messageId;
            } else {
                alert('Lỗi gửi tin nhắn: ' + (data.error || 'Unknown error'));
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Không thể gửi tin nhắn. Vui lòng thử lại.');
        });
    }

    function loadMessages() {
        if (!currentConversationId) return;

        fetch('${pageContext.request.contextPath}/api/user-chat?action=messages&conversationId=' + currentConversationId)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Clear messages (except typing indicator)
                    const messages = chatMessages.querySelectorAll('.user-chat-message');
                    messages.forEach(msg => msg.remove());

                    // Add messages in correct order (reverse since we get DESC from server)
                    data.messages.reverse().forEach(msg => {
                        addMessage(msg, msg.senderId === ${sessionScope.user != null ? sessionScope.user.userId : 0});
                        if (msg.messageId > lastMessageId) {
                            lastMessageId = msg.messageId;
                        }
                    });
                }
            })
            .catch(error => {
                console.error('Error loading messages:', error);
            });
    }

    function addMessage(message, isSent) {
        const messageDiv = document.createElement('div');
        messageDiv.className = 'user-chat-message ' + (isSent ? 'sent' : 'received');

        const avatar = document.createElement('div');
        avatar.className = 'user-chat-avatar';
        avatar.textContent = isSent ? 'B' : (otherUserName ? otherUserName.charAt(0).toUpperCase() : 'U');

        const bubble = document.createElement('div');
        bubble.className = 'user-chat-bubble';

        if (message.content) {
            const contentText = document.createElement('div');
            contentText.textContent = message.content;
            bubble.appendChild(contentText);
        }

        // Handle attachments
        if (message.attachmentUrl) {
            const attachment = document.createElement('div');
            attachment.className = 'chat-attachment';

            if (message.attachmentType === 'image') {
                attachment.innerHTML = '<img src="' + message.attachmentUrl + '" alt="Image" onclick="window.open(this.src)">';
            } else {
                attachment.className += ' file';
                const fileName = message.attachmentUrl.split('/').pop();
                attachment.innerHTML = '<i class="fa fa-file"></i><a href="' + message.attachmentUrl + '" target="_blank">' + fileName + '</a>';
            }

            bubble.appendChild(attachment);
        }

        const time = document.createElement('div');
        time.className = 'user-chat-time';
        time.textContent = formatTime(message.createdAt);
        bubble.appendChild(time);

        messageDiv.appendChild(avatar);
        messageDiv.appendChild(bubble);

        chatMessages.insertBefore(messageDiv, typingIndicator);
        scrollToBottom();
    }

    function uploadFile(file) {
        const formData = new FormData();
        formData.append('file', file);

        // Show progress
        const progress = document.createElement('div');
        progress.className = 'upload-progress';
        progress.textContent = 'Đang tải lên: ' + file.name + '...';
        chatMessages.appendChild(progress);
        scrollToBottom();

        fetch('${pageContext.request.contextPath}/api/chat-upload', {
            method: 'POST',
            body: formData
        })
        .then(response => response.json())
        .then(data => {
            progress.remove();
            if (data.success) {
                sendMessage(data.fileUrl, data.fileType);
            } else {
                alert('Lỗi tải file: ' + (data.error || 'Unknown error'));
            }
        })
        .catch(error => {
            progress.remove();
            console.error('Upload error:', error);
            alert('Không thể tải file. Vui lòng thử lại.');
        });
    }

    function startPolling() {
        if (pollInterval) clearInterval(pollInterval);

        pollInterval = setInterval(() => {
            pollNewMessages();
            checkTypingStatus();
        }, 2000); // Poll every 2 seconds
    }

    function pollNewMessages() {
        if (!currentConversationId || !lastMessageId) return;

        fetch('${pageContext.request.contextPath}/api/user-chat?action=poll&conversationId=' + currentConversationId + '&lastMessageId=' + lastMessageId)
            .then(response => response.json())
            .then(data => {
                if (data.success && data.count > 0) {
                    data.messages.forEach(msg => {
                        addMessage(msg, msg.senderId === ${sessionScope.user != null ? sessionScope.user.userId : 0});
                        lastMessageId = msg.messageId;
                    });

                    // Show unread badge if chat is closed
                    if (!chatContainer.classList.contains('show')) {
                        const currentCount = parseInt(unreadBadge.textContent) || 0;
                        unreadBadge.textContent = currentCount + data.count;
                        unreadBadge.style.display = 'flex';
                    } else {
                        markAsRead();
                    }
                }
            })
            .catch(error => {
                console.error('Poll error:', error);
            });
    }

    function updateTypingStatus(typing) {
        if (!currentConversationId) return;

        fetch('${pageContext.request.contextPath}/api/user-chat?action=typing', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                conversationId: currentConversationId,
                isTyping: typing
            })
        }).catch(error => {
            console.error('Typing status error:', error);
        });
    }

    function checkTypingStatus() {
        if (!currentConversationId) return;

        fetch('${pageContext.request.contextPath}/api/user-chat?action=checkTyping&conversationId=' + currentConversationId)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    if (data.isTyping) {
                        typingIndicator.classList.add('show');
                        scrollToBottom();
                    } else {
                        typingIndicator.classList.remove('show');
                    }
                }
            })
            .catch(error => {
                console.error('Check typing error:', error);
            });
    }

    function markAsRead() {
        if (!currentConversationId) return;

        fetch('${pageContext.request.contextPath}/api/user-chat?action=markRead', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                conversationId: currentConversationId
            })
        }).catch(error => {
            console.error('Mark read error:', error);
        });
    }

    function formatTime(timestamp) {
        const date = new Date(timestamp);
        const now = new Date();
        const diff = now - date;

        if (diff < 60000) return 'Vừa xong';
        if (diff < 3600000) return Math.floor(diff / 60000) + ' phút trước';
        if (diff < 86400000) return Math.floor(diff / 3600000) + ' giờ trước';

        return date.toLocaleDateString('vi-VN') + ' ' + date.toLocaleTimeString('vi-VN', {hour: '2-digit', minute: '2-digit'});
    }

    function scrollToBottom() {
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }

    // Cleanup on page unload
    window.addEventListener('beforeunload', () => {
        if (pollInterval) clearInterval(pollInterval);
        if (isTyping) updateTypingStatus(false);
    });
})();
</script>
