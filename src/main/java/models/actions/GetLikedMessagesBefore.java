package models.actions;

import java.util.Optional;

import com.google.common.base.Strings;
import com.google.gson.Gson;

import helpers.AuthTokenSupplier;
import helpers.GroupMeClient;
import helpers.Groups;
import models.LikedMessagesModel;

public class GetLikedMessagesBefore implements Before {
    private final String token = AuthTokenSupplier.get().get();
    private final String period;

    private GetLikedMessagesBefore(String period) {
        this.period = period;
    }

    @Override
    public Optional<BeforeResult> performBefore(GroupMeClient client) {
        String resp = client.getLikesForPeriod(Groups.XBOX_ID, period, token);
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
