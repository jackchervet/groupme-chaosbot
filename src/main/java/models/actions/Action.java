package models.actions;

import clients.GroupMeClient;

public interface Action {
    ActionType type();
    void performAction(GroupMeClient client);
}
