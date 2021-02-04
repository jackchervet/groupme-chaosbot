package models.actions;

import java.util.ArrayList;
import java.util.List;

import helpers.BotIdSupplier;
import clients.GroupMeClient;
import models.BotPostModel;
import models.Attachment;
import repositories.GroupMeRepository;

public class MessageAction implements Action {
    private String messageText;
    private List<Attachment> attachments;

    private MessageAction(String messageText, List<Attachment> attachments) {
        this.messageText = messageText;
        this.attachments = attachments;
    }

    @Override
    public ActionType type() {
        return ActionType.MESSAGE;
    }

    @Override
    public void performAction(GroupMeClient client) {
        GroupMeRepository repo = GroupMeRepository.get(client);

        BotPostModel message = new BotPostModel.Builder()
            .setBotId(BotIdSupplier.get().get())
            .setText(this.messageText)
            .addAllAttachments(this.attachments)
        .build();

        repo.sendMessageToGroup(message);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String messageText = null;
        private List<Attachment> attachments = new ArrayList<>();

        public Builder setMessageText(String messageText) {
            this.messageText = messageText;
            return this;
        }

        public Builder addAttachment(Attachment attachment) {
            this.attachments.add(attachment);
            return this;
        }

        public Builder addAllAttachments(List<Attachment> attachments) {
            this.attachments.addAll(attachments);
            return this;
        }

        public Builder setAttachments(List<Attachment> attachments) {
            this.attachments = attachments;
            return this;
        }

        public MessageAction build() {
            return new MessageAction(this.messageText, this.attachments);
        }
    }
}
