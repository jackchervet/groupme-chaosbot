package models.actions;

import java.util.List;

import models.MessageCallbackModel;

public interface UserAction {
    List<Before> before(MessageCallbackModel sentMessage);
    List<Action> action(MessageCallbackModel sentMessage, List<BeforeResult> results);
}
