package models.actions;

import java.util.List;

import com.google.common.collect.ImmutableList;

import helpers.Users;
import models.MessageCallbackModel;

public class GioUserAction implements UserAction {
    public static String getId() {
        return Users.GIO_ID;
    }

    @Override
    public List<Before> before(MessageCallbackModel sentMessage) {
        ImmutableList.Builder<Before> befores = new ImmutableList.Builder<>();
        befores.addAll(CommonUserAction.checkBefore(sentMessage));
        return befores.build();
    }

    @Override
    public List<Action> action(MessageCallbackModel sentMessage, List<BeforeResult> results) {
        ImmutableList.Builder<Action> actionsList = new ImmutableList.Builder<>();

        actionsList.addAll(CommonUserAction.checkActions(sentMessage, results));

        String text = sentMessage.getText().toLowerCase();

        if (text.matches("^\\/.*shack")) {
            actionsList.add(SendMessageAction.newBuilder()
                .setMessageText("https://www.youtube.com/watch?v=YWyHZNBz6FE")
                .build());
        }

        return actionsList.build();
    }

}
