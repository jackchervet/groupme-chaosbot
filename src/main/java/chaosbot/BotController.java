package chaosbot;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import cache.Cache;
import clients.HiRezClient;
import com.google.gson.Gson;

import clients.GroupMeClient;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import models.MessageCallbackModel;
import models.actions.Action;
import models.actions.Before;
import models.actions.BeforeResult;
import models.actions.UserAction;
import models.actions.UserActionFactory;
import repositories.HiRezRepository;

@Controller("/bot")
public class BotController {
    public static final ActionFlags FLAGS = new ActionFlags();

    @Inject private GroupMeClient groupMeClient;
    @Inject private HiRezClient hiRezClient;
    private final Cache cache = Cache.get();

    @Post("/messageAdded")
    public void messageAdded(@Body String body) {
        Gson gson = new Gson();
        MessageCallbackModel message = gson.fromJson(body, MessageCallbackModel.class);

        Optional<UserAction> userAction = UserActionFactory.getActionForUser(message.getSenderId());

        if (userAction.isPresent()) {
            List<Before> befores = userAction.get().before(message);
            List<BeforeResult> results = befores.stream()
                .map(b -> b.performBefore(groupMeClient, hiRezClient, cache))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
            List<Action> actions = userAction.get().action(message, results);
            actions.forEach(a -> a.performAction(groupMeClient));
        }
    }

    // For Debugging use
/*
    @Get("/latestMatches")
    @Produces(MediaType.APPLICATION_JSON)
    public String latestMatches(@QueryValue String playerId) {
        Gson gson = new Gson();
        return gson.toJson(HiRezRepository.get(hiRezClient, cache).getLatestMatches(playerId));
    }

    @Get("/getPlayerId")
    @Produces(MediaType.APPLICATION_JSON)
    public String getPlayerIdByGamertag(@QueryValue String gamertag) {
        Gson gson = new Gson();
        return gson.toJson(HiRezRepository.get(hiRezClient, cache).getPlayerIdsByGamertag(gamertag));
    }
*/
}
