package models.actions;

import java.util.List;
import java.util.Random;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import helpers.Users;
import models.BotPostModel.Attachment;
import models.MessageCallbackModel;

public class CommonUserAction implements UserAction {
    private static final ImmutableList<String> BOX_REPLACEMENTS = new ImmutableList.Builder<String>()
        .add("BOX")
        .add("CRATE")
        .add("RHOMBUS")
        .add("CARTON")
        .add("SARCOPHAGUS")
        .add("Xbox One Elite ®")
        .add("TRASH RECEPTACLE")
        .build();

    private static final ImmutableMap<String, String> USERS_TO_IDS = new ImmutableMap.Builder<String, String>()
        .put("@Jack ", Users.JACK_ID)
        .put("@Z ", Users.Z_ID)
        .put("@Tox ", Users.TOX_ID)
        .put("@George ", Users.GEORGE_ID)
        .put("@Geppo ", Users.GEPPO_ID)
        .put("@Gio ", Users.GIO_ID)
        .put("@John ", Users.JOHN_ID)
        .put("@Mike ", Users.MIKE_ID)
        .put("@Ben ", Users.BEN_ID)
        .put("@Tarik ", Users.TARIK_ID)
        .build();


    public static List<Action> checkActions(MessageCallbackModel sentMessage) {
        return new CommonUserAction().action(sentMessage);
    }

    @Override
    public List<Action> action(MessageCallbackModel sentMessage) {
        if ("bot".equals(sentMessage.getSenderType())) {
            return Lists.newArrayList();
        }

        ImmutableList.Builder<Action> actionsList = new ImmutableList.Builder<>();
        String text = sentMessage.getText().toLowerCase();

        if (text.contains("new meme")) {
            actionsList.add(MessageAction.newBuilder()
                .setMessageText("new meme guys")
                .addAttachment(new Attachment.Builder()
                    .setType("image")
                    .setUrl("https://i.groupme.com/720x452.jpeg.668c3d3e124f45af8a9cef02763a4948")
                    .build())
                .build());
        }

        if (text.matches("rally to me\\!*")) {
            String mentionsMessage = getMentionsMessage();
            actionsList.add(MessageAction.newBuilder()
                .setMessageText(mentionsMessage)
                .addAttachment(buildMentionsAttachment(mentionsMessage))
                .build());
        }
        return actionsList.build();
    }

    private static String getMentionsMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("WHO ON THAT MF %s???\n", getBoxReplacement()));
        USERS_TO_IDS.keySet().forEach(sb::append);
        return sb.toString();
    }

    private static Attachment buildMentionsAttachment(String mentionsMessage) {
        Attachment.Builder attachmentBuilder = new Attachment.Builder();
        attachmentBuilder.setType("mentions");
        int next = 0;
        while (true) {
            int first = mentionsMessage.indexOf("@", next);
            next = mentionsMessage.indexOf("@", first+1);
            if (next == -1) {
                break;
            } else {
                attachmentBuilder.addLoci(Lists.newArrayList(first, next - first));
                attachmentBuilder.addUserId(USERS_TO_IDS.get(mentionsMessage.substring(first, next)));
            }
        }

        return attachmentBuilder.build();
    }

    private static String getBoxReplacement() {
        Random rand = new Random();
        return BOX_REPLACEMENTS.get(rand.nextInt(BOX_REPLACEMENTS.size()));
    }
}
