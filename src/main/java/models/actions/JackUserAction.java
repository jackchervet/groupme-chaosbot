package models.actions;

import static chaosbot.BotController.FLAGS;

import java.util.List;

import com.google.common.collect.ImmutableList;

import helpers.Users;
import models.MessageCallbackModel;

public class JackUserAction implements UserAction {

    public static String getId() {
        return Users.JACK_ID;
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

        if (text.startsWith("/tomdelonge")) {
            if (FLAGS.tomDelongeOn()) {
                actionsList.add(MessageAction.newBuilder()
                    .setMessageText("Okay, I'm done. Wake up sheeple.")
                    .build());
            } else {
                actionsList.add(MessageAction.newBuilder()
                    .setMessageText("ALIENS")
                    .build());
            }

            FLAGS.setTomDelonge(!FLAGS.tomDelongeOn());
        }

        return actionsList.build();
    }

}
