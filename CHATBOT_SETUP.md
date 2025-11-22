# Gemini AI Chatbot Setup Guide

## Overview
This chatbot integrates Google's Gemini AI to provide intelligent customer support for the Rentaly car rental system. It can answer questions about booking, payments, policies, and becoming a car owner.

## Features
- ✅ Real-time chat with Gemini AI
- ✅ Conversation history tracking
- ✅ Green/white theme matching the Rentaly brand
- ✅ Support for both logged-in users and guests
- ✅ Mobile-friendly responsive design
- ✅ Quick question buttons for common queries
- ✅ Typing indicators and smooth animations

## Database Setup

### Step 1: Verify Database Tables
The following tables should already exist in your `CarRentalDB` database from the `DB60.sql` file:

1. **CHAT_CONVERSATION** - Stores chat sessions
2. **CHAT_MESSAGE** - Stores individual messages
3. **CHATBOT_ANALYTICS** - Tracks performance metrics
4. **CHATBOT_CONFIG** - Configuration settings

If these tables don't exist, run the `DB60.sql` script provided.

### Step 2: Insert Gemini API Key
You need to add your Gemini API key to the database:

```sql
-- Insert Gemini API key configuration
INSERT INTO CHATBOT_CONFIG (config_key, config_value, description, is_active, created_at, updated_at)
VALUES (
    'GEMINI_API_KEY',
    'YOUR_GEMINI_API_KEY_HERE',
    'Google Gemini API key for chatbot',
    1,
    GETDATE(),
    GETDATE()
);
```

### Step 3: Get Your Gemini API Key

1. Go to [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Sign in with your Google account
3. Click "Create API Key"
4. Copy the API key
5. Replace `YOUR_GEMINI_API_KEY_HERE` in the SQL above with your actual key

## Files Created

### Backend (Java)
- `src/main/java/model/ChatConversation.java` - Conversation model
- `src/main/java/model/ChatMessage.java` - Message model
- `src/main/java/model/ChatbotConfig.java` - Configuration model
- `src/main/java/dao/implement/ChatbotDAO.java` - Database operations
- `src/main/java/service/chatbot/GeminiService.java` - Gemini API integration
- `src/main/java/controller/chatbot/ChatbotServlet.java` - REST API endpoints

### Frontend (JSP)
- `src/main/webapp/view/common/chatbot/chatbot-widget.jsp` - Chatbot UI component

### Configuration
- `pom.xml` - Updated with Gemini API dependency

## How It Works

### 1. User Interaction
- User clicks the floating chat button (bottom-right corner)
- Chatbot window opens with welcome message and quick questions
- User types a question or clicks a quick question button

### 2. Message Processing
- Message is sent to `/api/chatbot` endpoint via POST request
- System checks for existing conversation or creates a new one
- User message is saved to database

### 3. AI Response
- User message is sent to Gemini API with system context about Rentaly
- Gemini generates an intelligent response
- Response is saved to database with metadata (tokens, response time)
- Response is displayed to the user

### 4. Conversation History
- All messages are stored in the database
- Conversation history is loaded when chatbot is opened
- Supports both authenticated users and guest sessions

## API Endpoints

### POST /api/chatbot
Send a new message to the chatbot

**Request Body:**
```json
{
  "message": "How do I book a car?"
}
```

**Response:**
```json
{
  "success": true,
  "message": "To book a car on Rentaly...",
  "conversationId": 123,
  "messageId": 456
}
```

### GET /api/chatbot?action=history
Get conversation history for current session

**Response:**
```json
{
  "success": true,
  "messages": [
    {
      "messageId": 1,
      "role": "user",
      "content": "How do I book?",
      "createdAt": "2025-11-22T10:00:00"
    },
    {
      "messageId": 2,
      "role": "model",
      "content": "To book a car...",
      "createdAt": "2025-11-22T10:00:01"
    }
  ],
  "conversationId": 123
}
```

## Customization

### Change Chatbot Personality
Edit the `buildSystemContext()` method in `GeminiService.java` to modify:
- System instructions
- Company information
- Response style
- Supported topics

### Modify UI Theme
Edit the CSS in `chatbot-widget.jsp`:
- Colors: Change `#28a745` and `#4DC0B5` to your brand colors
- Size: Adjust `width` and `height` in `#chatbot-container`
- Position: Modify `bottom` and `right` values

### Add Quick Questions
Edit the quick question buttons in `chatbot-widget.jsp`:
```html
<button class="quick-question-btn" data-question="Your question here">
    🔹 Your question text
</button>
```

## Testing

### 1. Verify Database Connection
- Ensure SQL Server is running
- Check database name is `CarRentalDB`
- Verify credentials in `DBContext.java`

### 2. Test API Key
Run this SQL to check if API key is configured:
```sql
SELECT * FROM CHATBOT_CONFIG WHERE config_key = 'GEMINI_API_KEY';
```

### 3. Test Chatbot
1. Start your Tomcat server
2. Open any page (e.g., home page)
3. Click the green chat button in bottom-right corner
4. Try a quick question or type your own
5. Verify response appears

### 4. Check Logs
Monitor Tomcat logs for:
- Database connection status
- API call errors
- Response generation issues

## Troubleshooting

### "Gemini API key not configured"
- Run the INSERT SQL command above with your API key
- Restart Tomcat server
- Verify `is_active = 1` in CHATBOT_CONFIG

### "Failed to create conversation"
- Check database connection
- Verify CHAT_CONVERSATION table exists
- Check Tomcat logs for SQL errors

### No response from chatbot
- Check internet connection (Gemini API requires internet)
- Verify API key is valid
- Check Tomcat logs for API errors
- Ensure Gemini API is not rate-limited

### Chatbot doesn't appear
- Clear browser cache
- Check browser console for JavaScript errors
- Verify `chatbot-widget.jsp` is included in footer

## Security Notes

⚠️ **IMPORTANT:**
- Never commit your API key to Git
- Store API keys securely in the database
- Consider encrypting sensitive config values
- Implement rate limiting to prevent API abuse
- Monitor API usage to avoid unexpected costs

## Dependencies

The chatbot uses:
- Google Gemini AI (`generativeai:0.3.0`)
- Gson for JSON processing
- Apache HttpClient for API calls
- Jakarta Servlet 5.0

All dependencies are already added to `pom.xml`.

## Support

For issues or questions:
1. Check Tomcat logs first
2. Verify database tables exist
3. Test API key validity
4. Contact: tungender1508@gmail.com

## Future Enhancements

Potential improvements:
- [ ] Add multi-language support
- [ ] Implement user feedback system
- [ ] Add chat analytics dashboard
- [ ] Support file uploads (images of cars)
- [ ] Add voice input/output
- [ ] Implement chat export functionality
- [ ] Add admin panel for managing chatbot

---

**Created by:** hungluu2405
**Date:** November 22, 2025
**Version:** 1.0
