package clients;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.client.annotation.Client;

import javax.validation.constraints.NotBlank;

@Client("http://api.smitegame.com/smiteapi.svc")
public interface HiRezClient {

    @Get("/createsessionJson/{devId}/{signature}/{timestamp}")
    String createSession(@NotBlank String devId, @NotBlank String signature, @NotBlank String timestamp);

    @Get("/getmatchhistoryJson/{devId}/{signature}/{session}/{timestamp}/{playerId}")
    String getMatchHistory(
        @NotBlank String devId,
        @NotBlank String signature,
        @NotBlank String session,
        @NotBlank String timestamp,
        @NotBlank String playerId
    );

    @Get("/getplayeridsbygamertagJson/{devId}/{signature}/{session}/{timestamp}/{gamertag}")
    String getPlayerIdsByGamertag(
        @NotBlank String devId,
        @NotBlank String signature,
        @NotBlank String session,
        @NotBlank String timestamp,
        @NotBlank String gamertag
    );
}
