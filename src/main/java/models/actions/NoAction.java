package models.actions;

import helpers.GroupMeClient;

public class NoAction implements Action {
    @Override
    public ActionType type() {
        return ActionType.NO_ACTION;
    }

    @Override
    public void performAction(GroupMeClient client) {}
}
