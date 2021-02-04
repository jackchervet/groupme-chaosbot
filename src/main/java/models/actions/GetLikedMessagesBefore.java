package models.actions;

import java.util.Optional;

import clients.HiRezClient;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.gson.Gson;

import helpers.AuthTokenSupplier;
import clients.GroupMeClient;
import helpers.Groups;
import models.LikedMessagesModel;

public class GetLikedMessagesBefore implements Before {
    private final String token = AuthTokenSupplier.get().get();
    private final String period;

    private GetLikedMessagesBefore(String period) {
        this.period = period;
    }

    @Override
    public Optional<BeforeResult> performBefore(GroupMeClient groupMeClient, HiRezClient hiRezClient, Cache<String, Object> cache) {
        String resp = groupMeClient.getLikesForPeriod(Groups.XBOX_ID, period, token);
        return !Strings.isNullOrEmpty(resp)
            ? Optional.of(new Gson().fromJson(resp, LikedMessagesModel.class))
            : Optional.empty();
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private String period = null;

        public Builder setPeriod(String period) {
            this.period = period;
            return this;
        }

        public GetLikedMessagesBefore build() {
            return new GetLikedMessagesBefore(this.period);
        }
    }
}
