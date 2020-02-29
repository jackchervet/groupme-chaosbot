package models.actions;

import java.util.Optional;

import models.MessageCallbackModel;

public class CommonUserAction implements UserAction {

    public static Optional<Action> checkActions(MessageCallbackModel sentMessage) {
        return new CommonUserAction().action(sentMessage);
    }

    @Override
    public Optional<Action> action(MessageCallbackModel sentMessage) {
        return Optional.empty();
    }
}
