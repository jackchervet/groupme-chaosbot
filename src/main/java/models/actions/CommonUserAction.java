package models.actions;

import java.util.List;

import com.google.common.collect.ImmutableList;

import models.BotPostModel.Attachment;
import models.MessageCallbackModel;

public class CommonUserAction implements UserAction {

    public static List<Action> checkActions(MessageCallbackModel sentMessage) {
        return new CommonUserAction().action(sentMessage);
    }

    @Override
    public List<Action> action(MessageCallbackModel sentMessage) {
        ImmutableList.Builder<Action> actionsList = new ImmutableList.Builder<>();
        String text = sentMessage.getText().toLowerCase();

        if (text.contains("new meme")) {
            actionsList.add(MessageAction.newBuilder()
                .setMessageText("new meme guys")
                .setAttachment(new Attachment.Builder()
                    .setType("image")
                    .setUrl("https://i.groupme.com/720x452.jpeg.668c3d3e124f45af8a9cef02763a4948")
                    .build())
                .build());
        }

        return actionsList.build();
    }
}
