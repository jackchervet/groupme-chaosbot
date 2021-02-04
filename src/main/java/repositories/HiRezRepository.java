package repositories;

import cache.Cache;
import clients.HiRezClient;
import models.hirez.MatchData;
import models.hirez.PlayerInfo;

import java.util.List;

public interface HiRezRepository {
    List<MatchData> getLatestMatches(String playerId);
    PlayerInfo getPlayerIdsByGamertag(String gamerTag);

    static HiRezRepository get(HiRezClient client, Cache cache) {
        return new ApiHiRezRepository(client, cache);
    }
}
