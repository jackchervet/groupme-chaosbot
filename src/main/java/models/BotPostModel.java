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
        this.attachments = attachments;
    }

    public static class Attachment {
        private String type;
        private String url;

        private Attachment(String type, String url) {
            this.type = type;
            this.url = url;
        }

        public static class Builder {
            private String type;
            private String url;

            public Builder setType(String type) {
                this.type = type;
                return this;
            }

            public Builder setUrl(String url) {
                this.url = url;
                return this;
            }

            public Attachment build() {
                return new Attachment(this.type, this.url);
            }
        }
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
