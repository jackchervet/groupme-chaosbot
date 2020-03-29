package models;

import java.util.List;

import models.actions.BeforeResult;

public class LikedMessagesModel extends BeforeResult {
    private Response response;

    public Response getResponse() {
        return response;
    }

    public List<Message> getMessages() {
        return response.getMessages();
    }

    public static class Response {
        private List<Message> messages;

        public List<Message> getMessages() {
            return messages;
        }
    }

    public static class Message {
        private String user_id;
        private String name;
        private String text;
        private List<Attachment> attachments;
        private List<String> favorited_by;

        public String getUser_id() {
            return user_id;
        }

        public String getName() {
            return name;
        }

        public String getText() {
            return text;
        }

        public List<Attachment> getAttachments() {
            return attachments;
        }

        public List<String> getFavorited_by() {
            return favorited_by;
        }
    }
}
