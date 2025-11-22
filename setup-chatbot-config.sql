-- =====================================================
-- Gemini AI Chatbot Configuration Setup Script
-- =====================================================
-- This script sets up the configuration for the Gemini AI chatbot
-- Run this after importing DB60.sql

USE CarRentalDB;
GO

-- =====================================================
-- Step 1: Insert Gemini API Key
-- =====================================================
-- IMPORTANT: Replace 'YOUR_GEMINI_API_KEY_HERE' with your actual Gemini API key
-- Get your API key from: https://makersuite.google.com/app/apikey

-- Check if GEMINI_API_KEY already exists
IF EXISTS (SELECT 1 FROM CHATBOT_CONFIG WHERE config_key = 'GEMINI_API_KEY')
BEGIN
    PRINT 'GEMINI_API_KEY already exists. Updating value...';

    UPDATE CHATBOT_CONFIG
    SET config_value = 'YOUR_GEMINI_API_KEY_HERE',
        updated_at = GETDATE()
    WHERE config_key = 'GEMINI_API_KEY';
END
ELSE
BEGIN
    PRINT 'Inserting new GEMINI_API_KEY...';

    INSERT INTO CHATBOT_CONFIG (config_key, config_value, description, is_active, created_at, updated_at)
    VALUES (
        'GEMINI_API_KEY',
        'YOUR_GEMINI_API_KEY_HERE',
        'Google Gemini API key for AI chatbot integration',
        1,
        GETDATE(),
        GETDATE()
    );
END
GO

-- =====================================================
-- Step 2: Insert Additional Chatbot Configurations (Optional)
-- =====================================================

-- Max tokens per response
IF NOT EXISTS (SELECT 1 FROM CHATBOT_CONFIG WHERE config_key = 'MAX_TOKENS')
BEGIN
    INSERT INTO CHATBOT_CONFIG (config_key, config_value, description, is_active, created_at, updated_at)
    VALUES (
        'MAX_TOKENS',
        '1024',
        'Maximum number of tokens for Gemini response',
        1,
        GETDATE(),
        GETDATE()
    );
END
GO

-- Temperature setting (0.0 - 1.0, higher = more creative)
IF NOT EXISTS (SELECT 1 FROM CHATBOT_CONFIG WHERE config_key = 'TEMPERATURE')
BEGIN
    INSERT INTO CHATBOT_CONFIG (config_key, config_value, description, is_active, created_at, updated_at)
    VALUES (
        'TEMPERATURE',
        '0.7',
        'Temperature for Gemini API (0.0-1.0, higher = more creative)',
        1,
        GETDATE(),
        GETDATE()
    );
END
GO

-- Enable/Disable chatbot
IF NOT EXISTS (SELECT 1 FROM CHATBOT_CONFIG WHERE config_key = 'CHATBOT_ENABLED')
BEGIN
    INSERT INTO CHATBOT_CONFIG (config_key, config_value, description, is_active, created_at, updated_at)
    VALUES (
        'CHATBOT_ENABLED',
        'true',
        'Enable or disable the chatbot feature',
        1,
        GETDATE(),
        GETDATE()
    );
END
GO

-- =====================================================
-- Step 3: Verify Configuration
-- =====================================================
PRINT '';
PRINT '==================================================';
PRINT 'Chatbot Configuration Status';
PRINT '==================================================';

SELECT
    config_key AS 'Configuration Key',
    CASE
        WHEN config_key = 'GEMINI_API_KEY' AND config_value = 'YOUR_GEMINI_API_KEY_HERE'
        THEN '⚠️ NOT CONFIGURED - Please update with your actual API key'
        WHEN config_key = 'GEMINI_API_KEY' AND config_value != 'YOUR_GEMINI_API_KEY_HERE'
        THEN '✅ CONFIGURED'
        ELSE config_value
    END AS 'Value/Status',
    description AS 'Description',
    CASE WHEN is_active = 1 THEN 'Active' ELSE 'Inactive' END AS 'Status',
    created_at AS 'Created',
    updated_at AS 'Updated'
FROM CHATBOT_CONFIG
ORDER BY config_key;

PRINT '';
PRINT '==================================================';
PRINT 'Next Steps:';
PRINT '==================================================';
PRINT '1. If GEMINI_API_KEY shows "NOT CONFIGURED":';
PRINT '   - Get your API key from: https://makersuite.google.com/app/apikey';
PRINT '   - Replace YOUR_GEMINI_API_KEY_HERE in this script';
PRINT '   - Run this script again';
PRINT '';
PRINT '2. Restart your Tomcat server';
PRINT '';
PRINT '3. Test the chatbot on your website';
PRINT '   - Look for the green chat button in bottom-right corner';
PRINT '   - Click it and try asking a question';
PRINT '';
PRINT '==================================================';

-- =====================================================
-- Optional: View chatbot statistics
-- =====================================================
-- Uncomment to see current chatbot usage statistics

/*
SELECT * FROM vw_ChatbotStats
ORDER BY conversation_date DESC;
*/

GO
