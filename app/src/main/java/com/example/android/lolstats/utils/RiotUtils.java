package com.example.android.lolstats.utils;

        import android.net.Uri;
        import android.text.TextUtils;

        import com.google.gson.Gson;

        import java.io.Serializable;
/**
 * Created by Nick Giles on 3/21/2018.
 */

public class RiotUtils {
    private static final String HTTPS = "https://";
    private static final String RIOT_BASE_URL = ".api.riotgames.com/lol";
    private static final String RIOT_SUMMONER_SEARCH_PATH = "summoner/v3/summoners/by-name";
    private static final String RIOT_RECENT_MATCHES_PATH_START = "match/v3/matchlists/by-account";
    private static final String RIOT_RECENT_MATCHES_PATH_END = "recent";
    private static final String RIOT_MATCH_PATH = "match/v3/matches";
    private static final String RIOT_CHAMPION_DATA = "static-data/v3/champions";
    private static final String RIOT_API_APPID_PARAM = "api_key";

    private static final String  RIOT_API_APPID = "RGAPI-a390c6ea-a5cf-48c2-a411-fc4af4b3471c";

    public static String getRiotURL(String region) {
        return HTTPS + region + RIOT_BASE_URL;
    }

    public static String buildSummonerSearchURL(String summonerName, String region) {
        String riotUrl = getRiotURL(region);
        return Uri.parse(riotUrl).buildUpon()
                .appendEncodedPath(RIOT_SUMMONER_SEARCH_PATH)
                .appendPath(summonerName)
                .appendQueryParameter(RIOT_API_APPID_PARAM, RIOT_API_APPID)
                .build().toString();
    }

    public static String buildRecentMatchesURL(String accountID, String region) {
        String riotURL = getRiotURL(region);
        return Uri.parse(riotURL).buildUpon()
                .appendEncodedPath(RIOT_RECENT_MATCHES_PATH_START)
                .appendPath(accountID)
                .appendEncodedPath(RIOT_RECENT_MATCHES_PATH_END)
                .appendQueryParameter(RIOT_API_APPID_PARAM, RIOT_API_APPID)
                .build().toString();
    }

    public static String buildDetailedMatchURL(String matchID, String region) {
        String riotURL = getRiotURL(region);
        return Uri.parse(riotURL).buildUpon()
                .appendEncodedPath(RIOT_MATCH_PATH)
                .appendPath(matchID)
                .appendQueryParameter(RIOT_API_APPID_PARAM, RIOT_API_APPID)
                .build().toString();
    }

    public static String buildChampionDataURL(String championID, String region) {
        String riotURL = getRiotURL(region);
        return Uri.parse(riotURL).buildUpon()
                .appendEncodedPath(RIOT_CHAMPION_DATA)
                .appendPath(championID)
                .build().toString();
    }
}
