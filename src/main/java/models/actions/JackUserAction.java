package models.actions;

import static chaosbot.BotController.FLAGS;

import java.util.List;

import com.google.common.collect.ImmutableList;

import models.MessageCallbackModel;

public class JackUserAction implements UserAction {

    public static String getId() {
        return "16084546";
    }

    @Override
    public List<Action> action(MessageCallbackModel sentMessage) {
        ImmutableList.Builder<Action> actionsList = new ImmutableList.Builder<>();

        actionsList.addAll(CommonUserAction.checkActions(sentMessage));

        String text = sentMessage.getText().toLowerCase();

        if (text.startsWith("/peppers")) {
            if (FLAGS.peppersOn()) {
                actionsList.add(MessageAction.newBuilder()
                    .setMessageText("Okay, I'll lay off. Behave now Geppo...")
                    .build());
            } else {
                actionsList.add(MessageAction.newBuilder()
                    .setMessageText("Classic. I'll handle this.")
                    .build());
            }
            
            FLAGS.setPeppers(!FLAGS.peppersOn());
        }

        return actionsList.build();
    }

}
