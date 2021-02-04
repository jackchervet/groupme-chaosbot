package models.actions;

import clients.GroupMeClient;
import repositories.GroupMeRepository;

public class RemovalAction implements Action {
    private final String userToRemove;
    private final String groupId;

    private RemovalAction(String userToRemove, String groupId) {
        this.userToRemove = userToRemove;
        this.groupId = groupId;
    }

    @Override
    public ActionType type() {
        return ActionType.REMOVAL;
    }

    @Override
    public void performAction(GroupMeClient client) {
        GroupMeRepository repo = GroupMeRepository.get(client);
        repo.removeUserFromGroup(groupId, userToRemove);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String userToRemove;
        private String groupId;

        public Builder setUserToRemove(String userToRemove) {
            this.userToRemove = userToRemove;
            return this;
        }

        public Builder setGroupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public RemovalAction build() {
            return new RemovalAction(userToRemove, groupId);
        }
    }
}
