package repositories;

import cache.Cache;
import clients.HiRezClient;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import helpers.HiRezAuthSupplier;
import helpers.PlayerIds;
import models.hirez.MatchData;
import models.hirez.PlayerInfo;
import models.hirez.RequestBase;
import models.hirez.Session;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ApiHiRezRepository implements HiRezRepository {
    private final HiRezClient httpClient;
    private final Cache cache;
    private static final long SESSION_TIMEOUT_IN_MILLIS = 15 * 60 * 1000;

    public ApiHiRezRepository(HiRezClient httpClient, Cache cache) {
        this.httpClient = httpClient;
        this.cache = cache;
    }

    private Session getSession() {
        return (Session) cache.get("session").orElseGet(this::createNewSession);
    }

    private Session createNewSession() {
        Gson gson = new Gson();
        RequestBase req = HiRezAuthSupplier.getSignature("createsession");
        String resp = httpClient.createSession(HiRezAuthSupplier.getDevId(), req.getSignature(), req.getTimestamp());
        System.out.println("Created new session: " + resp);
        Session session = gson.fromJson(
            resp,
            Session.class
        );
        cache.add("session", session, System.currentTimeMillis() + SESSION_TIMEOUT_IN_MILLIS);
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
