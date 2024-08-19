package models.actions;

import clients.GroupMeClient;
import repositories.GroupMeRepository;

public class DeleteMessageAction implements Action {
    private final String messageId;
    private final String groupId;
    private DeleteMessageAction(String groupId, String messageId) {
        this.messageId = messageId;
        this.groupId = groupId;
    }

    @Override
    public ActionType type() {
        return ActionType.DELETE_MESSAGE;
    }

    @Override
    public void performAction(GroupMeClient client) {
        GroupMeRepository repo = GroupMeRepository.get(client);
        repo.deleteMessage(groupId, messageId);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String messageId = null;
        private String groupId = null;

        public Builder setMessageId(String messageId) {
            this.messageId = messageId;
            return this;
        }

        public Builder setGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public DeleteMessageAction build() {
            return new DeleteMessageAction(groupId, messageId);
        }
    }
}
