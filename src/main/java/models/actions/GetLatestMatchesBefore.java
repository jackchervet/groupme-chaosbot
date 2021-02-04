package models.actions;

import cache.Cache;
import clients.GroupMeClient;
import clients.HiRezClient;
import models.hirez.ListMatchData;
import models.hirez.MatchData;
import repositories.HiRezRepository;

import java.util.List;
import java.util.Optional;

public class GetLatestMatchesBefore implements Before {
    private final Integer numberOfMatches;
    private final String playerId;

    GetLatestMatchesBefore(Integer numberOfMatches, String playerId) {
        this.numberOfMatches = numberOfMatches;
        this.playerId = playerId;
    }

    @Override
    public Optional<BeforeResult> performBefore(GroupMeClient groupMeClient, HiRezClient hiRezClient, Cache cache) {
        HiRezRepository hirezRepo = HiRezRepository.get(hiRezClient, cache);
        List<MatchData> matches = hirezRepo.getLatestMatches(playerId);

        return Optional.of(new ListMatchData(matches.subList(0, numberOfMatches)));
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {
        private Integer numberOfMatches = 5;
        private String playerId = null;

        public Builder setNumberOfMatches(Integer numMatches) {
            this.numberOfMatches = numMatches;
            return this;
        }

        public Builder setPlayerId(String playerId) {
            this.playerId = playerId;
            return this;
        }

        public GetLatestMatchesBefore build() {
            return new GetLatestMatchesBefore(this.numberOfMatches, this.playerId);
        }
    }
}
