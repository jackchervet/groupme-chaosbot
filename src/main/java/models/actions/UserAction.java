package models.actions;

import java.util.List;

import models.MessageCallbackModel;

public interface UserAction {
    List<Action> action(MessageCallbackModel sentMessage);
}
