package repositories;

import clients.HiRezClient;
import com.google.common.cache.Cache;
import models.hirez.MatchData;
import models.hirez.PlayerInfo;

import java.util.List;

public interface HiRezRepository {
    List<MatchData> getLatestMatches(String playerId);
    PlayerInfo getPlayerIdsByGamertag(String gamerTag);

    static HiRezRepository get(HiRezClient client, Cache<String, Object> cache) {
        return new ApiHiRezRepository(client, cache);
    }
}
