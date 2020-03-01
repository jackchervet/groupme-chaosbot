package models.actions;

import helpers.GroupMeClient;

public interface Action {
    ActionType type();
    void performAction(GroupMeClient client);
}
