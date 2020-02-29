package models.actions;

import java.util.Optional;

import models.BotPostModel;

public class NoAction implements Action {
    @Override
    public ActionType type() {
        return ActionType.NO_ACTION;
    }

    @Override
    public Optional<BotPostModel> messageToSend() {
        return Optional.empty();
    }
}
