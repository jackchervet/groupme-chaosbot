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
    public List<Action> action(MessageCallbackModel sentMessage) {
        ImmutableList.Builder<Action> actionsList = new ImmutableList.Builder<>();

        actionsList.addAll(CommonUserAction.checkActions(sentMessage));

        String text = sentMessage.getText().toLowerCase();

        if (text.matches("^\\/.*shack")) {
            actionsList.add(MessageAction.newBuilder()
                .setMessageText("https://www.youtube.com/watch?v=YWyHZNBz6FE")
                .build());
        }

        return actionsList.build();
    }

}
