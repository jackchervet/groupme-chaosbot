package models.actions;

import java.util.Optional;

import com.google.common.collect.ImmutableMap;

public class UserActionFactory {
    private static ImmutableMap<String, UserAction> ID_TO_ACTION_MAP =
        new ImmutableMap.Builder<String, UserAction>()
            .put(GeppoUserAction.getId(), new GeppoUserAction())
            .put(JackUserAction.getId(), new JackUserAction())
            .build();

    public static Optional<UserAction> getActionForUser(String userId) {
        return Optional.ofNullable(ID_TO_ACTION_MAP.getOrDefault(userId, new CommonUserAction()));
    }
}
