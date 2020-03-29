package models;

import java.util.ArrayList;
import java.util.List;

public class BotPostModel {
    private String bot_id;
    private String text;
    private List<Attachment> attachments;

    private BotPostModel(String botId, String text, List<Attachment> attachments) {
        this.bot_id = botId;
        this.text = text;
        this.attachments = attachments.isEmpty() ? null : attachments;
    }

    public static class Builder {
        private String bot_id;
        private String text;
        private List<Attachment> attachments = new ArrayList<>();

        public Builder setBotId(String botId) {
            this.bot_id = botId;
            return this;
        }

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder addAllAttachments(List<Attachment> attachments) {
            this.attachments.addAll(attachments);
            return this;
        }

        public Builder addAttachment(Attachment attachment) {
            this.attachments.add(attachment);
            return this;
        }

        public BotPostModel build() {
            return new BotPostModel(this.bot_id, this.text, this.attachments);
        }
    }
}
