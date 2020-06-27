package models.actions;

import static chaosbot.BotController.FLAGS;

import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import helpers.Groups;
import helpers.Users;
import models.Attachment;
import models.LikedMessagesModel;
import models.LikedMessagesModel.Message;
import models.MessageCallbackModel;

public class CommonUserAction implements UserAction {
    private static final ImmutableList<String> DUEL_GIFS = ImmutableList.of(
        "https://media0.giphy.com/media/1qaUQO7L5YkxEiyZna/source.gif",
        "https://thumbs.gfycat.com/DangerousSociableGalago-small.gif",
        "https://media2.giphy.com/media/kaBU6pgv0OsPHz2yxy/giphy.gif",
        "https://media0.giphy.com/media/l49FqlUguNsGDNCGk/source.gif",
        "https://media.giphy.com/media/HVweQ5FuSFZJe/giphy.gif");

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


    public static List<Before> checkBefore(MessageCallbackModel sentMessage) {
        return new CommonUserAction().before(sentMessage);
    }

    @Override
    public List<Before> before(MessageCallbackModel sentMessage) {
        if ("bot".equals(sentMessage.getSenderType())) {
            return Lists.newArrayList();
        }

        ImmutableList.Builder<Before> beforeList = new ImmutableList.Builder<>();
        String text = sentMessage.getText().toLowerCase();

        if (text.matches("^\\/funniest (today|this week|this month)")) {
            String period = text
                .replaceAll("\\/funniest ", "").replaceAll("this ", "").replaceAll("to", "");
            beforeList.add(GetLikedMessagesBefore.newBuilder()
                .setPeriod(period)
                .build());
        }

        return beforeList.build();
    }

    public static List<Action> checkActions(
        MessageCallbackModel sentMessage,
        List<BeforeResult> results)
    {
        return new CommonUserAction().action(sentMessage, results);
    }

    @Override
    public List<Action> action(MessageCallbackModel sentMessage, List<BeforeResult> results) {
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

        if (text.matches("^\\/funniest (today|this week|this month)")) {
            Optional<LikedMessagesModel> result = results.stream()
                .filter(LikedMessagesModel.class::isInstance)
                .map(LikedMessagesModel.class::cast)
                .findFirst();

            if (result.isPresent()) {
                actionsList.add(MessageAction.newBuilder()
                    .setMessageText("The funniest messages " + text.replaceAll("\\/funniest ", "") + " were...")
                    .build());
                actionsList.addAll(result.get().getMessages().subList(0, 3).stream()
                    .map(CommonUserAction::buildMessageActionFromLikedMessages)
                    .collect(Collectors.toList()));
            }
        }

        if (text.startsWith("/duel") && FLAGS.duelsOn()) {
            actionsList.addAll(buildDuelActions(sentMessage));
        }

        return actionsList.build();
    }

    private static MessageAction buildMessageActionFromLikedMessages(Message message) {
        String author = "Author: " + message.getName() + "\n";
        String likes = "Likes: " + message.getFavorited_by().size() + "\n";
        return MessageAction.newBuilder()
            .setMessageText(author + likes + Strings.nullToEmpty(message.getText()))
            .setAttachments(message.getAttachments().stream()
                .filter(a -> !a.getType().equals("mentions"))
                .collect(Collectors.toList()))
            .build();
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

    private static String duel(List<String> duelingUsers) {
        Random rand = new Random();
        return duelingUsers.get(rand.nextInt(duelingUsers.size()));
    }

    private static String getDuelGif() {
        Random rand = new Random();
        return DUEL_GIFS.get(rand.nextInt(DUEL_GIFS.size()));
    }

    private static List<Action> buildDuelActions(MessageCallbackModel sentMessage) {
        if (sentMessage.getAttachments() == null || sentMessage.getAttachments().isEmpty()) {
            return ImmutableList.of(MessageAction.newBuilder()
                .setMessageText("Who do you want to duel? Type /duel and then tag one or more people.")
                .build());
        }

        List<String> duelingUsers = sentMessage.getAttachments().stream()
            .filter(a -> "mentions".equals(a.getType()))
            .map(Attachment::getUserIds)
            .flatMap(Collection::stream)
            .filter(s -> !Users.JACK_ID.equals(s))
            .collect(Collectors.toList());

        if (!Users.JACK_ID.equals(sentMessage.getUserId())) {
            duelingUsers.add(sentMessage.getUserId());
        }

        String loserId = duelingUsers.contains(Users.GEPPO_ID)
            ? Users.GEPPO_ID
            : duel(duelingUsers);

        String loserName = USERS_TO_IDS.entrySet().stream()
            .filter(e -> e.getValue().equals(loserId))
            .map(Entry::getKey)
            .findFirst()
            .orElse("")
            .replace("@", "")
            .trim();

        return ImmutableList.of(
            MessageAction.newBuilder()
                .setMessageText(loserName + " lost the duel!")
                .addAttachment(Attachment.newBuilder()
                    .setType("image")
                    .setUrl(getDuelGif())
                    .build())
                .build(),
            RemovalAction.newBuilder()
                .setUserToRemove(Users.toMemberId.get(loserId))
                .setGroupId(Groups.XBOX_ID)
                .build()
        );
    }
}
