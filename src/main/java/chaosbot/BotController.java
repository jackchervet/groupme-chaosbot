package chaosbot;

import java.util.Optional;

import javax.inject.Inject;

import com.google.gson.Gson;

import helpers.GroupMeClient;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import models.MessageCallbackModel;
import models.actions.Action;
import models.actions.ActionType;
import models.actions.NoAction;
import models.actions.UserAction;
import models.actions.UserActionFactory;
import repositories.GroupMeRepository;

@Controller("/bot")
public class BotController {
    @Inject
    private GroupMeClient client;

    @Post("/messageAdded")
    public void messageAdded(@Body String body) {
        Gson gson = new Gson();
        MessageCallbackModel message = gson.fromJson(body, MessageCallbackModel.class);

        Optional<UserAction> userAction = UserActionFactory.getActionForUser(message.getSenderId());

        if (userAction.isPresent()) {
            GroupMeRepository repo = GroupMeRepository.get(client);
            Action action = userAction.get().action(message).orElse(new NoAction());

            if (ActionType.MESSAGE.equals(action.type()) && action.messageToSend().isPresent()) {
                repo.sendMessageToGroup(action.messageToSend().get());
            }
        }
    }
}
