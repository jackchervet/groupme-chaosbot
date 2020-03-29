package models.actions;

import static chaosbot.BotController.FLAGS;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;

import helpers.Users;
import models.BotPostModel;
import models.MessageCallbackModel;

public class ToxUserAction implements UserAction {
    private static ImmutableList<String> IMAGES = new ImmutableList.Builder<String>()
        .add("https://i.insider.com/5b9235bb5c5e5254548b59f5?width=1100&format=jpeg&auto=webp")
        .add("https://i.guim.co.uk/img/media/0a5f59d149d984985581a2bf99fd140f13629644/0_177_3888_2333/master/3888.jpg?width=700&quality=85&auto=format&fit=max&s=8eff91cd658e6838ea383a61abb56137")
        .add("https://www.audubon.org/sites/default/files/heyhey_1024x10242x.jpg")
        .add("https://ichef.bbci.co.uk/images/ic/1376x774/p07j6k9p.jpg")
        .add("https://i.imgflip.com/2e8syk.jpg")
        .add("https://stayhipp.com/wp-content/uploads/2018/08/Brain-Meme.jpg")
        .build();

    private static ImmutableList<String> VIDEOS = new ImmutableList.Builder<String>()
        .add("https://www.youtube.com/watch?v=9Ht5RZpzPqw")
        .add("https://www.youtube.com/watch?v=s1tAYmMjLdY")
        .build();

    public static String getId() {
        return Users.TOX_ID;
    }

    @Override
    public List<Action> action(MessageCallbackModel sentMessage) {
        ImmutableList.Builder<Action> actionsList = new ImmutableList.Builder<>();

        actionsList.addAll(CommonUserAction.checkActions(sentMessage));

        if (FLAGS.tomDelongeOn()) {
            actionsList.add(getRandomMessageAction());
        }

        return actionsList.build();
    }

    private static MessageAction getRandomMessageAction() {
        Random rand = new Random();
        if (rand.nextInt() % 2 == 0) {
            return MessageAction.newBuilder()
                .addAttachment(new BotPostModel.Attachment.Builder()
                    .setType("image")
                    .setUrl(IMAGES.get(rand.nextInt(IMAGES.size())))
                    .build())
                .build();
        } else {
            return MessageAction.newBuilder()
                .setMessageText(VIDEOS.get(rand.nextInt(VIDEOS.size())))
                .build();
        }
    }
}
