package models.actions;

import java.util.Optional;

import models.MessageCallbackModel;

public interface UserAction {
    Optional<Action> action(MessageCallbackModel sentMessage);
}
