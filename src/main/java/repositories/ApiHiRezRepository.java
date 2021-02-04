package repositories;

import clients.HiRezClient;
import com.google.common.cache.Cache;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import helpers.HiRezAuthSupplier;
import helpers.PlayerIds;
import models.hirez.MatchData;
import models.hirez.PlayerInfo;
import models.hirez.RequestBase;
import models.hirez.Session;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ApiHiRezRepository implements HiRezRepository {
    private final HiRezClient httpClient;
    private final Cache<String, Object> cache;
    private static final long SESSION_TIMEOUT_IN_MILLIS = 15 * 60 * 1000;

    public ApiHiRezRepository(HiRezClient httpClient, Cache<String, Object> cache) {
        this.httpClient = httpClient;
        this.cache = cache;
    }

    private Session getSession() {
        return (Session) Optional.ofNullable(cache.getIfPresent("session")).orElseGet(this::createNewSession);
    }

    private Session createNewSession() {
        Gson gson = new Gson();
        RequestBase req = HiRezAuthSupplier.getSignature("createsession");
        Session session = gson.fromJson(
            httpClient.createSession(HiRezAuthSupplier.getDevId(), req.getSignature(), req.getTimestamp()),
            Session.class
        );
        System.out.println("Created new session: " + session.getSession_id());
        cache.put("session", session);
        return session;
    }

    @Override
    public PlayerInfo getPlayerIdsByGamertag(String gamerTag) {
        Gson gson = new Gson();
        Session session = getSession();
        RequestBase req = HiRezAuthSupplier.getSignature("getplayeridsbygamertag");
        String response = httpClient.getPlayerIdsByGamertag(
                HiRezAuthSupplier.getDevId(),
                req.getSignature(),
                session.getSession_id(),
                req.getTimestamp(),
                gamerTag
        );
        System.out.println("playerId response: " + response);
        return gson.fromJson(response, PlayerInfo.class);
    }

    @Override
    public List<MatchData> getLatestMatches(String playerId) {
        Gson gson = new Gson();
        Session session = getSession();
        System.out.println("[GetLatestMatches] Using session: " + session.getSession_id());
        RequestBase req = HiRezAuthSupplier.getSignature("getmatchhistory");
        JsonArray response = gson.fromJson(httpClient.getMatchHistory(
            HiRezAuthSupplier.getDevId(),
            req.getSignature(),
            session.getSession_id(),
            req.getTimestamp(),
            playerId
        ), JsonArray.class);

        return IntStream.range(0, response.size())
            .mapToObj(i -> response.get(i).getAsJsonObject())
            .map(jsonObject -> gson.fromJson(jsonObject.toString(), MatchData.class))
            .collect(Collectors.toList());
    }
}
