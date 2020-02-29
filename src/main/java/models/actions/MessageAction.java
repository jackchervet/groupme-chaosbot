package models.actions;

import java.util.Optional;

import helpers.BotIdSupplier;
import models.BotPostModel;
import models.BotPostModel.Attachment;

public class MessageAction implements Action {
    private String messageText;
    private Attachment attachment;

    private MessageAction(String messageText, Attachment attachment) {
        this.messageText = messageText;
        this.attachment = attachment;
    }

    @Override
    public ActionType type() {
        return ActionType.MESSAGE;
    }

    @Override
    public Optional<BotPostModel> messageToSend() {
        return Optional.of(
            new BotPostModel.Builder()
                .setBotId(BotIdSupplier.get().get())
                .setText(this.messageText)
                .addAttachment(this.attachment)
            .build());
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String messageText = null;
        private Attachment attachment = null;

        public Builder setMessageText(String messageText) {
            this.messageText = messageText;
            return this;
        }

        public Builder setAttachment(Attachment attachment) {
            this.attachment = attachment;
            return this;
        }

        public MessageAction build() {
            return new MessageAction(this.messageText, this.attachment);
        }
    }
}
