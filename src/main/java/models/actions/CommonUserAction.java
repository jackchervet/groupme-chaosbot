package models.actions;

import java.util.List;

import com.google.common.collect.Lists;

import models.MessageCallbackModel;

public class CommonUserAction implements UserAction {

    public static List<Action> checkActions(MessageCallbackModel sentMessage) {
        return new CommonUserAction().action(sentMessage);
    }

    @Override
    public List<Action> action(MessageCallbackModel sentMessage) {
        return Lists.newArrayList();
    }
}
