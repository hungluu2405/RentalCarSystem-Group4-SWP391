-- =====================================================
-- User-to-User Chat System (Customer ↔ Car Owner)
-- =====================================================
-- Run this script to add user chat tables to your database

USE CarRentalDB;
GO

-- =====================================================
-- Table: USER_CONVERSATION
-- Stores conversations between users (linked to bookings)
-- =====================================================
CREATE TABLE [dbo].[USER_CONVERSATION](
	[conversation_id] [int] IDENTITY(1,1) NOT NULL,
	[booking_id] [int] NOT NULL,
	[customer_id] [int] NOT NULL,
	[owner_id] [int] NOT NULL,
	[created_at] [datetime] NOT NULL DEFAULT GETDATE(),
	[last_message_at] [datetime] NOT NULL DEFAULT GETDATE(),
	[is_active] [bit] NOT NULL DEFAULT 1,
PRIMARY KEY CLUSTERED
(
	[conversation_id] ASC
)
) ON [PRIMARY]
GO

-- =====================================================
-- Table: USER_MESSAGE
-- Stores individual messages in conversations
-- =====================================================
CREATE TABLE [dbo].[USER_MESSAGE](
	[message_id] [int] IDENTITY(1,1) NOT NULL,
	[conversation_id] [int] NOT NULL,
	[sender_id] [int] NOT NULL,
	[content] [nvarchar](max) NULL,
	[attachment_url] [nvarchar](500) NULL,
	[attachment_type] [nvarchar](50) NULL,  -- 'image', 'file', null
	[created_at] [datetime] NOT NULL DEFAULT GETDATE(),
	[is_read] [bit] NOT NULL DEFAULT 0,
	[read_at] [datetime] NULL,
PRIMARY KEY CLUSTERED
(
	[message_id] ASC
)
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO

-- =====================================================
-- Table: TYPING_STATUS
-- Tracks who is currently typing in a conversation
-- =====================================================
CREATE TABLE [dbo].[TYPING_STATUS](
	[status_id] [int] IDENTITY(1,1) NOT NULL,
	[conversation_id] [int] NOT NULL,
	[user_id] [int] NOT NULL,
	[is_typing] [bit] NOT NULL DEFAULT 0,
	[last_updated] [datetime] NOT NULL DEFAULT GETDATE(),
PRIMARY KEY CLUSTERED
(
	[status_id] ASC
)
) ON [PRIMARY]
GO

-- =====================================================
-- Foreign Key Constraints
-- =====================================================

-- USER_CONVERSATION foreign keys
ALTER TABLE [dbo].[USER_CONVERSATION]
ADD CONSTRAINT [FK_UserConv_Booking]
FOREIGN KEY([booking_id]) REFERENCES [dbo].[BOOKING] ([BOOKING_ID])
ON DELETE CASCADE;
GO

ALTER TABLE [dbo].[USER_CONVERSATION]
ADD CONSTRAINT [FK_UserConv_Customer]
FOREIGN KEY([customer_id]) REFERENCES [dbo].[USER] ([user_id]);
GO

ALTER TABLE [dbo].[USER_CONVERSATION]
ADD CONSTRAINT [FK_UserConv_Owner]
FOREIGN KEY([owner_id]) REFERENCES [dbo].[USER] ([user_id]);
GO

-- USER_MESSAGE foreign keys
ALTER TABLE [dbo].[USER_MESSAGE]
ADD CONSTRAINT [FK_UserMsg_Conversation]
FOREIGN KEY([conversation_id]) REFERENCES [dbo].[USER_CONVERSATION] ([conversation_id])
ON DELETE CASCADE;
GO

ALTER TABLE [dbo].[USER_MESSAGE]
ADD CONSTRAINT [FK_UserMsg_Sender]
FOREIGN KEY([sender_id]) REFERENCES [dbo].[USER] ([user_id]);
GO

-- TYPING_STATUS foreign keys
ALTER TABLE [dbo].[TYPING_STATUS]
ADD CONSTRAINT [FK_Typing_Conversation]
FOREIGN KEY([conversation_id]) REFERENCES [dbo].[USER_CONVERSATION] ([conversation_id])
ON DELETE CASCADE;
GO

ALTER TABLE [dbo].[TYPING_STATUS]
ADD CONSTRAINT [FK_Typing_User]
FOREIGN KEY([user_id]) REFERENCES [dbo].[USER] ([user_id]);
GO

-- =====================================================
-- Indexes for Performance
-- =====================================================
CREATE INDEX [IX_UserConv_BookingId] ON [dbo].[USER_CONVERSATION]([booking_id]);
CREATE INDEX [IX_UserConv_CustomerId] ON [dbo].[USER_CONVERSATION]([customer_id]);
CREATE INDEX [IX_UserConv_OwnerId] ON [dbo].[USER_CONVERSATION]([owner_id]);
CREATE INDEX [IX_UserMsg_ConversationId] ON [dbo].[USER_MESSAGE]([conversation_id]);
CREATE INDEX [IX_UserMsg_SenderId] ON [dbo].[USER_MESSAGE]([sender_id]);
CREATE INDEX [IX_UserMsg_IsRead] ON [dbo].[USER_MESSAGE]([is_read]);
CREATE INDEX [IX_Typing_ConversationId] ON [dbo].[TYPING_STATUS]([conversation_id]);
GO

-- =====================================================
-- View: Conversation Summary (for inbox display)
-- =====================================================
CREATE VIEW [dbo].[vw_UserConversationSummary] AS
SELECT
    c.conversation_id,
    c.booking_id,
    c.customer_id,
    c.owner_id,
    c.created_at,
    c.last_message_at,
    c.is_active,

    -- Latest message
    (SELECT TOP 1 content FROM USER_MESSAGE
     WHERE conversation_id = c.conversation_id
     ORDER BY created_at DESC) AS last_message_content,

    (SELECT TOP 1 sender_id FROM USER_MESSAGE
     WHERE conversation_id = c.conversation_id
     ORDER BY created_at DESC) AS last_message_sender_id,

    -- Unread count per user
    (SELECT COUNT(*) FROM USER_MESSAGE
     WHERE conversation_id = c.conversation_id
     AND sender_id != c.customer_id
     AND is_read = 0) AS customer_unread_count,

    (SELECT COUNT(*) FROM USER_MESSAGE
     WHERE conversation_id = c.conversation_id
     AND sender_id != c.owner_id
     AND is_read = 0) AS owner_unread_count

FROM USER_CONVERSATION c;
GO

-- =====================================================
-- Success Message
-- =====================================================
PRINT '';
PRINT '==================================================';
PRINT 'User Chat Tables Created Successfully!';
PRINT '==================================================';
PRINT 'Created:';
PRINT '- USER_CONVERSATION (conversations linked to bookings)';
PRINT '- USER_MESSAGE (messages with attachments support)';
PRINT '- TYPING_STATUS (typing indicators)';
PRINT '- vw_UserConversationSummary (conversation inbox view)';
PRINT '';
PRINT 'Features enabled:';
PRINT '✅ Customer ↔ Car Owner chat';
PRINT '✅ Image & file attachments';
PRINT '✅ Read/unread tracking';
PRINT '✅ Typing indicators';
PRINT '✅ Linked to bookings';
PRINT '';
PRINT 'Next: Run the Java application to test chat!';
PRINT '==================================================';
GO
