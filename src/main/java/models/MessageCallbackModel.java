package models;

import java.util.List;

public class MessageCallbackModel {
    private List<Attachment> attachments;
    private String group_id;
    private String id;
    private String name;
    private String sender_id;
    private String sender_type;
    private String text;
    private String user_id;

    public String getGroupId() {
        return group_id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSenderId() {
        return sender_id;
    }

    public String getSenderType() {
        return sender_type;
    }

    public String getText() {
        return text;
    }

    public String getUserId() {
        return user_id;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }
}
