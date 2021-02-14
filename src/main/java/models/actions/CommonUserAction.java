package models.actions;

import static chaosbot.BotController.FLAGS;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Random;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import models.hirez.ListMatchData;
import models.hirez.MatchData;

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

        if (text.startsWith("/lastmatch") && Users.TO_PLAYER_ID.containsKey(sentMessage.getSenderId())) {
            beforeList.add(GetLatestMatchesBefore.newBuilder()
                .setPlayerId(Users.TO_PLAYER_ID.get(sentMessage.getSenderId()))
                .setNumberOfMatches(1)
                .build());
        }

        if (text.startsWith("/feeders")) {
            beforeList.addAll(Users.TO_PLAYER_ID.values().stream()
                .map(playerId -> GetLatestMatchesBefore.newBuilder()
                    .setPlayerId(playerId)
                    .setNumberOfMatches(10)
                    .build())
                .collect(Collectors.toList()));
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

        if (text.startsWith("/help")) {
            actionsList.add(buildHelpMessage());
        }

        if (text.startsWith("/lastmatch")) {
            Optional<ListMatchData> result = results.stream()
                .filter(ListMatchData.class::isInstance)
                .map(ListMatchData.class::cast)
                .findFirst();

            result.map(listMatchData -> actionsList.add(buildLastMatchResponse(listMatchData)))
                .orElseGet(() ->
                    actionsList.add(MessageAction.newBuilder().setMessageText("You don't play Smite...").build())
                );
        }

        if (text.startsWith("/feeders")) {
            List<ListMatchData> result = results.stream()
                .filter(ListMatchData.class::isInstance)
                .map(ListMatchData.class::cast)
                .collect(Collectors.toList());

            List<String> body = result.stream()
                .map(CommonUserAction::buildFeedingMessage)
                .sorted(Comparator.comparing(map -> Lists.newArrayList(map.values()).get(0).get(1), Comparator.reverseOrder()))
                .map(Map::entrySet)
                .map(set -> Lists.newArrayList(set).get(0))
                .map(e -> e.getKey() + ":: " + e.getValue().get(0) + "/" + e.getValue().get(1) + "/" + e.getValue().get(2))
                .collect(Collectors.toList());

            actionsList.add(MessageAction.newBuilder()
                .setMessageText(
                    "The biggest feeders recently are...\n\n" +
                    "(Name :: Average KDA over last 10 games)\n\n" +
                    IntStream.range(0, body.size())
                        .mapToObj(i -> i+1 + ". " + body.get(i))
                        .collect(Collectors.joining("\n"))
                )
                .build());
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

    private static MessageAction buildLastMatchResponse(ListMatchData listMatchData) {
        MatchData match = listMatchData.getData().get(0);
        return MessageAction.newBuilder()
            .setMessageText(
                "Here are the stats for your last match:\n\n" +
                "Mode: " + match.getQueue() + "\n" +
                "God: " + match.getGod() + "\n" +
                "KDA: " + match.getKills() + "/" + match.getDeaths() + "/" + match.getAssists() + "\n" +
                "Damage Taken: " + match.getDamage_Taken() + "\n" +
                "Damage Mitigated: " + match.getDamage_Mitigated() + "\n" +
                "Structure Damage: " + match.getDamage_Structure() + "\n" +
                "Damage: " + match.getDamage() + "\n" +
                "Healing: " + match.getHealing() + "\n" +
                "Gold: " + match.getGold() + "\n\n" +
                "Result: " + match.getWin_Status() + ""
            ).build();

    }

    private static BinaryOperator<List<Integer>> sumElements = (List<Integer> first, List<Integer> second) -> {
      if (first.size() != second.size()) {
          return ImmutableList.of();
      }

      return IntStream.range(0, first.size())
        .map(i -> (first.get(i) + second.get(i)))
        .boxed()
        .collect(Collectors.toList());
    };

    private static Map<String, List<Double>> buildFeedingMessage(ListMatchData listMatchData) {
        List<Double> avgKDA = listMatchData.getData().stream()
            .map(matchData -> ImmutableList.of(matchData.getKills(), matchData.getDeaths(), matchData.getAssists()))
            .collect(Collectors.collectingAndThen(
                Collectors.reducing(sumElements),
                list -> list.map(l -> l.stream()
                    .map(Integer::doubleValue)
                    .map(val -> val / listMatchData.getData().size())
                    .collect(Collectors.toList()))
                    .orElse(ImmutableList.of())
            ));

        String playerId = Users.TO_PLAYER_ID.entrySet().stream()
            .filter(e -> e.getValue().equals(listMatchData.getData().get(0).getPlayerId().toString()))
            .map(e -> USERS_TO_IDS.entrySet().stream().collect(Collectors.toMap(Entry::getValue, Entry::getKey)).get(e.getKey()))
            .map(s -> s.replace("@", ""))
            .findFirst()
            .orElse("UNKNOWN");

        return ImmutableMap.of(playerId, avgKDA);
    }

    private static Action buildHelpMessage() {
        return MessageAction.newBuilder()
            .setMessageText(
                "I can do a lot of stuff. Here's what I know about:\n\n" +
                "- rally to me! --- @ everyone to get on.\n" +
                "- /duel @[1 or more other users] --- Duel someone else. Loser will be kicked from the chat.\n\n" +
                "- new meme --- Post a new meme\n\n" +
                "- /lastmatch --- Get your stats from the last Smite match you played.")
            .build();
    }
}
