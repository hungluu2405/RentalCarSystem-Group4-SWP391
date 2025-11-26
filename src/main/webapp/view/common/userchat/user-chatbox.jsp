<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Multi-Conversation User Chat Widget (Like Messenger) -->
<style>
    /* Chat Button */
    #user-chat-button {
        position: fixed;
        bottom: 100px;
        right: 25px;
        width: 60px;
        height: 60px;
        border-radius: 50%;
        background: linear-gradient(135deg, #4DC0B5, #28a745);
        color: white;
        border: 2px solid #fff;
        cursor: pointer;
        box-shadow: 0 4px 20px rgba(77, 192, 181, 0.4);
        display: none;
        align-items: center;
        justify-content: center;
        font-size: 24px;
        transition: all 0.3s ease;
        z-index: 10000;
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
        bottom: 175px;
        right: 25px;
        width: 380px;
        height: 550px;
        background: white;
        border-radius: 16px;
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
        display: none;
        flex-direction: column;
        overflow: hidden;
        z-index: 9999;
        animation: slideUp 0.3s ease-out;
    }

    #user-chat-container.show {
        display: flex;
    }

    @keyframes slideUp {
        from {
            transform: translateY(20px);
            opacity: 0;
        }
        to {
            transform: translateY(0);
            opacity: 1;
        }
    }

    /* Header */
    #user-chat-header {
        background: linear-gradient(135deg, #4DC0B5, #28a745);
        color: white;
        padding: 16px 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        flex-shrink: 0;
    }

    #user-chat-header h3 {
        margin: 0;
        font-size: 16px;
        font-weight: 600;
    }

    .chat-header-left {
        display: flex;
        align-items: center;
        gap: 10px;
    }

    #back-to-list-btn {
        background: none;
        border: none;
        color: white;
        font-size: 20px;
        cursor: pointer;
        padding: 5px;
        display: none;
    }

    #back-to-list-btn:hover {
        opacity: 0.8;
    }

    .chat-header-buttons {
        display: flex;
        gap: 8px;
        align-items: center;
    }

    .chat-header-btn {
        background: none;
        border: none;
        color: white;
        font-size: 20px;
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

    .chat-header-btn:hover {
        background: rgba(255, 255, 255, 0.2);
    }

    /* Conversation List View */
    #conversation-list-view {
        display: flex;
        flex-direction: column;
        height: 100%;
    }

    #conversation-list-view.hidden {
        display: none;
    }

    .conversation-list {
        flex: 1;
        overflow-y: auto;
        background: #f8f9fa;
    }

    .conversation-item {
        padding: 15px 20px;
        border-bottom: 1px solid #e9ecef;
        cursor: pointer;
        transition: background 0.2s;
        display: flex;
        align-items: center;
        gap: 12px;
    }

    .conversation-item:hover {
        background: #e9ecef;
    }

    .conversation-item.active {
        background: #d4edda;
    }

    .conversation-avatar {
        width: 48px;
        height: 48px;
        border-radius: 50%;
        background: linear-gradient(135deg, #4DC0B5, #28a745);
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 18px;
        font-weight: bold;
        flex-shrink: 0;
    }

    .conversation-info {
        flex: 1;
        min-width: 0;
    }

    .conversation-name {
        font-weight: 600;
        font-size: 14px;
        color: #333;
        margin-bottom: 4px;
    }

    .conversation-car {
        font-size: 12px;
        color: #666;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .conversation-last-msg {
        font-size: 12px;
        color: #999;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        margin-top: 2px;
    }

    .conversation-badge {
        background: #dc3545;
        color: white;
        border-radius: 50%;
        width: 20px;
        height: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 11px;
        font-weight: bold;
    }

    .conversation-remove {
        color: #dc3545;
        font-size: 16px;
        padding: 5px;
        opacity: 0;
        transition: opacity 0.2s;
    }

    .conversation-item:hover .conversation-remove {
        opacity: 1;
    }

    .empty-conversations {
        padding: 60px 20px;
        text-align: center;
        color: #999;
    }

    .empty-conversations i {
        font-size: 48px;
        color: #ddd;
        margin-bottom: 15px;
    }

    /* Chat View */
    #chat-view {
        display: none;
        flex-direction: column;
        height: 100%;
    }

    #chat-view.active {
        display: flex;
    }

    .chat-info-bar {
        padding: 10px 20px;
        background: #f8f9fa;
        border-bottom: 1px solid #e9ecef;
        font-size: 13px;
        color: #666;
        text-align: center;
    }

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

    @keyframes typing {
        0%, 60%, 100% {
            transform: translateY(0);
        }
        30% {
            transform: translateY(-10px);
        }
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
        flex-shrink: 0;
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
    <!-- Header -->
    <div id="user-chat-header">
        <div class="chat-header-left">
            <button id="back-to-list-btn">
                <i class="fa fa-arrow-left"></i>
            </button>
            <div>
                <h3 id="user-chat-title">Tin nhắn</h3>
                <div class="chat-info" id="user-chat-info" style="font-size: 12px; opacity: 0.9;"></div>
            </div>
        </div>
        <div class="chat-header-buttons">
            <button class="chat-header-btn" id="user-chat-close" title="Thu gọn">×</button>
        </div>
    </div>

    <!-- Conversation List View -->
    <div id="conversation-list-view">
        <div class="conversation-list" id="conversation-list">
            <div class="empty-conversations">
                <i class="fa fa-comments"></i>
                <p>Chưa có cuộc hội thoại nào</p>
                <p style="font-size: 12px;">Bấm "Chat" trong booking để bắt đầu!</p>
            </div>
        </div>
    </div>

    <!-- Chat View (Single Conversation) -->
    <div id="chat-view">
        <div class="chat-info-bar" id="chat-info-bar"></div>

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
</div>

<input type="file" id="chat-file-input" accept="image/*,.pdf,.doc,.docx,.xls,.xlsx,.txt" />

<script>
(function() {
    // DOM Elements
    const chatButton = document.getElementById('user-chat-button');
    const chatContainer = document.getElementById('user-chat-container');
    const chatClose = document.getElementById('user-chat-close');
    const chatTitle = document.getElementById('user-chat-title');
    const chatInfo = document.getElementById('user-chat-info');
    const chatInput = document.getElementById('user-chat-input');
    const chatSend = document.getElementById('user-chat-send');
    const chatMessages = document.getElementById('user-chat-messages');
    const typingIndicator = document.querySelector('.user-typing-indicator');
    const attachBtn = document.getElementById('chat-attach-btn');
    const fileInput = document.getElementById('chat-file-input');
    const unreadBadge = chatButton.querySelector('.unread-badge');

    const conversationListView = document.getElementById('conversation-list-view');
    const conversationList = document.getElementById('conversation-list');
    const chatView = document.getElementById('chat-view');
    const backToListBtn = document.getElementById('back-to-list-btn');
    const chatInfoBar = document.getElementById('chat-info-bar');

    // State
    let conversations = {}; // { conversationId: { ... } }
    let activeConversationId = null;
    let pollIntervals = {}; // { conversationId: intervalId }
    let typingTimeout = null;
    let isTyping = false;

    const STORAGE_KEY = 'userChatConversations';
    const currentUserId = ${sessionScope.user != null ? sessionScope.user.userId : 0};

    // ========== STORAGE FUNCTIONS ==========
    function loadConversationsFromStorage() {
        try {
            const stored = localStorage.getItem(STORAGE_KEY);
            if (stored) {
                const parsed = JSON.parse(stored);
                console.log('[Multi Chat] Raw loaded data:', parsed);

                // Validate and clean data - remove conversations with "false" strings
                let cleaned = false;
                Object.keys(parsed).forEach(key => {
                    const conv = parsed[key];
                    if (conv.otherUserName === 'false' || conv.carName === 'false' ||
                        conv.otherUserName === false || conv.carName === false ||
                        !conv.otherUserName || !conv.carName) {
                        console.warn('[Multi Chat] Removing corrupted conversation:', conv);
                        delete parsed[key];
                        cleaned = true;
                    }
                });

                conversations = parsed;

                if (cleaned) {
                    console.log('[Multi Chat] Cleaned corrupted data, saving...');
                    saveConversationsToStorage();
                }

                console.log('[Multi Chat] Loaded conversations:', conversations);
                return true;
            }
        } catch (error) {
            console.error('[Multi Chat] Error loading storage:', error);
            // Clear corrupted localStorage
            localStorage.removeItem(STORAGE_KEY);
        }
        return false;
    }

    function saveConversationsToStorage() {
        try {
            localStorage.setItem(STORAGE_KEY, JSON.stringify(conversations));
            console.log('[Multi Chat] Saved conversations');
        } catch (error) {
            console.error('[Multi Chat] Error saving storage:', error);
        }
    }

    function addConversation(data) {
        console.log('[Multi Chat] Adding conversation with data:', data);

        // Validate data to prevent storing "false" strings
        if (!data || !data.conversationId) {
            console.error('[Multi Chat] Invalid conversation data:', data);
            return;
        }

        conversations[data.conversationId] = {
            conversationId: data.conversationId,
            bookingId: data.bookingId || 0,
            otherUserId: data.otherUserId || 0,
            otherUserName: data.otherUserName || 'Unknown User',
            carName: data.carName || 'Unknown Car',
            lastMessageId: 0,
            unreadCount: 0,
            lastMessage: '',
            lastMessageTime: Date.now()
        };

        console.log('[Multi Chat] Stored conversation:', conversations[data.conversationId]);
        saveConversationsToStorage();
        renderConversationList();
    }

    function removeConversation(conversationId) {
        if (pollIntervals[conversationId]) {
            clearInterval(pollIntervals[conversationId]);
            delete pollIntervals[conversationId];
        }
        delete conversations[conversationId];
        saveConversationsToStorage();
        renderConversationList();

        if (activeConversationId === conversationId) {
            showConversationList();
        }
    }

    // ========== UI FUNCTIONS ==========
    function renderConversationList() {
        const convArray = Object.values(conversations);

        if (convArray.length === 0) {
            conversationList.innerHTML = `
                <div class="empty-conversations">
                    <i class="fa fa-comments"></i>
                    <p>Chưa có cuộc hội thoại nào</p>
                    <p style="font-size: 12px;">Bấm "Chat" trong booking để bắt đầu!</p>
                </div>
            `;
            chatButton.style.display = 'none';
            return;
        }

        // Show chat button
        chatButton.style.display = 'flex';

        // Sort by last message time
        convArray.sort((a, b) => b.lastMessageTime - a.lastMessageTime);

        let html = '';
        let totalUnread = 0;

        convArray.forEach(conv => {
            const initial = conv.otherUserName ? conv.otherUserName.charAt(0).toUpperCase() : 'U';
            const isActive = activeConversationId === conv.conversationId;

            html += `
                <div class="conversation-item ${isActive ? 'active' : ''}" data-conversation-id="${conv.conversationId}">
                    <div class="conversation-avatar">${initial}</div>
                    <div class="conversation-info">
                        <div class="conversation-name">${conv.otherUserName || 'Unknown'}</div>
                        <div class="conversation-car"><i class="fa fa-car"></i> ${conv.carName || 'N/A'}</div>
                        ${conv.lastMessage ? '<div class="conversation-last-msg">' + conv.lastMessage + '</div>' : ''}
                    </div>
                    ${conv.unreadCount > 0 ? '<div class="conversation-badge">' + conv.unreadCount + '</div>' : ''}
                    <i class="fa fa-times conversation-remove" data-conversation-id="${conv.conversationId}" title="Xóa cuộc hội thoại"></i>
                </div>
            `;

            totalUnread += conv.unreadCount;
        });

        conversationList.innerHTML = html;

        // Update total unread badge
        if (totalUnread > 0) {
            unreadBadge.textContent = totalUnread;
            unreadBadge.style.display = 'flex';
        } else {
            unreadBadge.style.display = 'none';
        }

        // Add click listeners
        conversationList.querySelectorAll('.conversation-item').forEach(item => {
            item.addEventListener('click', function(e) {
                if (e.target.classList.contains('conversation-remove')) {
                    const convId = parseInt(e.target.dataset.conversationId);
                    if (confirm('Xóa cuộc hội thoại này?')) {
                        removeConversation(convId);
                    }
                } else {
                    const convId = parseInt(this.dataset.conversationId);
                    openConversation(convId);
                }
            });
        });
    }

    function showConversationList() {
        conversationListView.classList.remove('hidden');
        chatView.classList.remove('active');
        chatTitle.textContent = 'Tin nhắn';
        chatInfo.textContent = '';
        backToListBtn.style.display = 'none';
        activeConversationId = null;
    }

    function openConversation(conversationId) {
        const conv = conversations[conversationId];
        if (!conv) return;

        activeConversationId = conversationId;

        conversationListView.classList.add('hidden');
        chatView.classList.add('active');
        backToListBtn.style.display = 'block';

        chatTitle.textContent = conv.otherUserName;
        chatInfo.textContent = '';
        chatInfoBar.innerHTML = '<i class="fa fa-car"></i> Về: ' + conv.carName;

        // Clear messages
        const messages = chatMessages.querySelectorAll('.user-chat-message');
        messages.forEach(msg => msg.remove());

        // Load messages
        loadMessages(conversationId);

        // Mark as read
        conv.unreadCount = 0;
        saveConversationsToStorage();
        renderConversationList();

        // Start polling if not already
        if (!pollIntervals[conversationId]) {
            startPolling(conversationId);
        }
    }

    // ========== API FUNCTIONS ==========
    function initChatFromBooking(bookingId) {
        console.log('[Multi Chat] Init chat for bookingId:', bookingId);
        console.log('[Multi Chat] Current conversations:', conversations);

        fetch('${pageContext.request.contextPath}/api/init-chat?bookingId=' + bookingId)
            .then(response => {
                console.log('[Multi Chat] Response status:', response.status);
                if (!response.ok) {
                    throw new Error('HTTP error! status: ' + response.status);
                }
                return response.json();
            })
            .then(data => {
                console.log('[Multi Chat] API Response:', data);

                if (data.success) {
                    // Validate response data
                    if (!data.conversationId || !data.otherUserName || !data.carName) {
                        console.error('[Multi Chat] Incomplete API response:', data);
                        alert('Lỗi: Dữ liệu không đầy đủ từ server');
                        return;
                    }

                    // Check if conversation already exists
                    const existing = Object.values(conversations).find(c => c.bookingId === data.bookingId);

                    if (!existing) {
                        console.log('[Multi Chat] Creating new conversation');
                        addConversation(data);
                        startPolling(data.conversationId);
                    } else {
                        console.log('[Multi Chat] Conversation already exists:', existing);
                    }

                    // Open this conversation
                    chatContainer.classList.add('show');
                    openConversation(data.conversationId);
                } else {
                    console.error('[Multi Chat] API returned error:', data.error);
                    alert('Không thể khởi tạo chat: ' + (data.error || 'Unknown error'));
                }
            })
            .catch(error => {
                console.error('[Multi Chat] Fetch Error:', error);
                alert('Lỗi kết nối API: ' + error.message);
            });
    }

    function loadMessages(conversationId) {
        const conv = conversations[conversationId];
        if (!conv) return;

        fetch('${pageContext.request.contextPath}/api/user-chat?action=messages&conversationId=' + conversationId)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    data.messages.reverse().forEach(msg => {
                        addMessageToUI(msg, msg.senderId === currentUserId);
                        if (msg.messageId > conv.lastMessageId) {
                            conv.lastMessageId = msg.messageId;
                        }
                    });
                    scrollToBottom();
                }
            })
            .catch(error => {
                console.error('Error loading messages:', error);
            });
    }

    function sendMessage(attachmentUrl = null, attachmentType = null) {
        const message = chatInput.value.trim();
        if (!message && !attachmentUrl) return;
        if (!activeConversationId) return;

        const conv = conversations[activeConversationId];
        if (!conv) return;

        const data = {
            conversationId: activeConversationId,
            content: message
        };

        if (attachmentUrl) {
            data.attachmentUrl = attachmentUrl;
            data.attachmentType = attachmentType;
        }

        fetch('${pageContext.request.contextPath}/api/user-chat?action=send', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(data)
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                addMessageToUI(data.message, true);
                chatInput.value = '';
                chatSend.disabled = true;

                // Update conversation
                conv.lastMessageId = data.message.messageId;
                conv.lastMessage = message || '[File]';
                conv.lastMessageTime = Date.now();
                saveConversationsToStorage();
            } else {
                alert('Lỗi gửi tin nhắn: ' + (data.error || 'Unknown error'));
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Không thể gửi tin nhắn.');
        });
    }

    function addMessageToUI(message, isSent) {
        const conv = conversations[activeConversationId];
        const messageDiv = document.createElement('div');
        messageDiv.className = 'user-chat-message ' + (isSent ? 'sent' : 'received');

        const avatar = document.createElement('div');
        avatar.className = 'user-chat-avatar';
        avatar.textContent = isSent ? 'B' : (conv.otherUserName ? conv.otherUserName.charAt(0).toUpperCase() : 'U');

        const bubble = document.createElement('div');
        bubble.className = 'user-chat-bubble';

        if (message.content) {
            const contentText = document.createElement('div');
            contentText.textContent = message.content;
            bubble.appendChild(contentText);
        }

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

    function startPolling(conversationId) {
        if (pollIntervals[conversationId]) return;

        pollIntervals[conversationId] = setInterval(() => {
            pollNewMessages(conversationId);
            checkTypingStatus(conversationId);
        }, 2000);
    }

    function pollNewMessages(conversationId) {
        const conv = conversations[conversationId];
        if (!conv || !conv.lastMessageId) return;

        fetch('${pageContext.request.contextPath}/api/user-chat?action=poll&conversationId=' + conversationId + '&lastMessageId=' + conv.lastMessageId)
            .then(response => response.json())
            .then(data => {
                if (data.success && data.count > 0) {
                    data.messages.forEach(msg => {
                        if (activeConversationId === conversationId) {
                            addMessageToUI(msg, msg.senderId === currentUserId);
                        } else {
                            // Increase unread count
                            conv.unreadCount += 1;
                        }

                        conv.lastMessageId = msg.messageId;
                        conv.lastMessage = msg.content || '[File]';
                        conv.lastMessageTime = Date.now();
                    });

                    saveConversationsToStorage();
                    renderConversationList();
                }
            })
            .catch(error => {
                console.error('Poll error:', error);
            });
    }

    function checkTypingStatus(conversationId) {
        if (activeConversationId !== conversationId) return;

        fetch('${pageContext.request.contextPath}/api/user-chat?action=checkTyping&conversationId=' + conversationId)
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Check if conversation was deleted on backend
                    if (data.conversationDeleted) {
                        console.log('[Multi Chat] Conversation deleted on backend, removing from localStorage');
                        removeConversation(conversationId);
                        return;
                    }

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

    function updateTypingStatus(typing) {
        if (!activeConversationId) return;

        fetch('${pageContext.request.contextPath}/api/user-chat?action=typing', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                conversationId: activeConversationId,
                isTyping: typing
            })
        }).catch(error => {
            console.error('Typing status error:', error);
        });
    }

    function uploadFile(file) {
        const formData = new FormData();
        formData.append('file', file);

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
            alert('Không thể tải file.');
        });
    }

    // ========== HELPERS ==========
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

    // ========== EVENT LISTENERS ==========
    chatButton.addEventListener('click', function() {
        const isVisible = chatContainer.classList.contains('show');
        if (isVisible) {
            chatContainer.classList.remove('show');
        } else {
            chatContainer.classList.add('show');
            if (Object.keys(conversations).length > 0) {
                showConversationList();
            }
        }
    });

    chatClose.addEventListener('click', function() {
        chatContainer.classList.remove('show');
    });

    backToListBtn.addEventListener('click', function() {
        showConversationList();
    });

    chatInput.addEventListener('input', function() {
        chatSend.disabled = this.value.trim() === '';

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

    chatInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter' && !e.shiftKey && !chatSend.disabled) {
            e.preventDefault();
            sendMessage();
        }
    });

    chatSend.addEventListener('click', () => sendMessage());

    attachBtn.addEventListener('click', () => {
        fileInput.click();
    });

    fileInput.addEventListener('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            uploadFile(file);
        }
        fileInput.value = '';
    });

    // ========== INITIALIZATION ==========
    const urlParams = new URLSearchParams(window.location.search);
    const bookingId = urlParams.get('bookingId');

    // Load from storage first
    loadConversationsFromStorage();
    renderConversationList();

    // Start polling for all conversations
    Object.keys(conversations).forEach(convId => {
        startPolling(parseInt(convId));
    });

    // If URL has bookingId, init that chat
    if (bookingId) {
        initChatFromBooking(bookingId);
    }

    // Expose global functions
    window.initUserChat = function(bookingId) {
        console.log('[Multi Chat] window.initUserChat called with bookingId:', bookingId);
        return initChatFromBooking(bookingId);
    };

    // Expose function to clear corrupted localStorage
    window.clearUserChatData = function() {
        localStorage.removeItem(STORAGE_KEY);
        conversations = {};
        Object.values(pollIntervals).forEach(intervalId => clearInterval(intervalId));
        pollIntervals = {};
        renderConversationList();
        console.log('[Multi Chat] ✅ Cleared all conversation data');
        alert('Chat data cleared! Please refresh the page.');
    };

    console.log('[Multi Chat] Ready! Conversations:', Object.keys(conversations).length);
    console.log('[Multi Chat] window.initUserChat is:', typeof window.initUserChat);
    console.log('[Multi Chat] To clear corrupted data, run: window.clearUserChatData()');

    // Cleanup
    window.addEventListener('beforeunload', () => {
        Object.values(pollIntervals).forEach(intervalId => clearInterval(intervalId));
        if (isTyping) updateTypingStatus(false);
    });
})();
</script>
