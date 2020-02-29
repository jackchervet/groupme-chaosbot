package models.actions;

import java.util.Optional;

import models.BotPostModel;

public interface Action {
    ActionType type();
    Optional<BotPostModel> messageToSend();
}
