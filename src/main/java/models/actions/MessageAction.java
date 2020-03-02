package models.actions;

import com.google.gson.Gson;

import helpers.BotIdSupplier;
import helpers.GroupMeClient;
import models.BotPostModel;
import models.BotPostModel.Attachment;
import repositories.GroupMeRepository;

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
    public void performAction(GroupMeClient client) {
        GroupMeRepository repo = GroupMeRepository.get(client);

        BotPostModel message = new BotPostModel.Builder()
            .setBotId(BotIdSupplier.get().get())
            .setText(this.messageText)
            .addAttachment(this.attachment)
        .build();

        Gson gson = new Gson();
        System.out.println(gson.toJson(message));
//        repo.sendMessageToGroup(message);
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
