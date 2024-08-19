package chaosbot;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.inject.Inject;

import clients.GPT4oClient;
import clients.HiRezClient;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;

import clients.GroupMeClient;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import models.MessageCallbackModel;
import models.actions.Action;
import models.actions.Before;
import models.actions.BeforeResult;
import models.actions.UserAction;
import models.actions.UserActionFactory;

@Controller("/bot")
public class BotController {
    public static final ActionFlags FLAGS = new ActionFlags();

    @Inject private GroupMeClient groupMeClient;
    @Inject private HiRezClient hiRezClient;
    @Inject private GPT4oClient gptClient;
    private final Cache<String, Object> sessionCache = CacheBuilder.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .build();

    @Post("/messageAdded")
    public void messageAdded(@Body String body) {
        Gson gson = new Gson();
        MessageCallbackModel message = gson.fromJson(body, MessageCallbackModel.class);

        Optional<UserAction> userAction = UserActionFactory.getActionForUser(message.getSenderId());

        if (userAction.isPresent()) {
            List<Before> befores = userAction.get().before(message);
            List<BeforeResult> results = befores.stream()
                .map(b -> b.performBefore(groupMeClient, hiRezClient, gptClient, sessionCache))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
            List<Action> actions = userAction.get().action(message, results);
            actions.forEach(a -> a.performAction(groupMeClient));
        }
    }
}
