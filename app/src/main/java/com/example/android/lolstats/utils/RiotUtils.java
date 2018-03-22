package com.example.android.lolstats.utils;

import android.net.Uri;
import com.google.gson.Gson;


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

    private static final Gson gson = new Gson();

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

    public static String buildChampionDataURL(long championID, String region) {
        String riotURL = getRiotURL(region);
        return Uri.parse(riotURL).buildUpon()
                .appendEncodedPath(RIOT_CHAMPION_DATA)
                .appendPath(String.valueOf(championID))
                .appendQueryParameter(RIOT_API_APPID_PARAM, RIOT_API_APPID)
                .build().toString();
    }

    public static class SummonerDataResults {
        String name;
        long accountId;
    }

    public static class RecentMatchResults {
        MatchData[] matches;
    }

    public static class MatchData {
        long champion;
        long timestamp;
        long gameId;
    }

    public static class DetailedMatchData {
        ParticipantIdentity[] participantIdentities;
        TeamStats[] teams;
        Participants[] participants;
    }

    public static class ParticipantIdentity {
        Player player;
        long participantId;
    }

    public static class Player {
        String summonerName;
    }

    public static class TeamStats {
        String win;
        String baronKills;
        String inhibitorKills;
        String towerKills;
        String dragonKills;
    }

    public static class Participants {
        Stats stats;
        long championId;
    }

    public static class Stats {
        String kills;
        String assists;
        String deaths;
        String win;
        String totalMinionsKilled;
    }

    public static class ChampionData {
        String name;
    }

    public static SummonerDataResults parseSummonerDataJSON(String summonerJSON) {
        SummonerDataResults summonerData = gson.fromJson(summonerJSON, SummonerDataResults.class);
        return summonerData;
    }

    public static MatchData[] parseRecentMatchDataJSON(String recentMatchJSON) {
        RecentMatchResults recentMatchResults = gson.fromJson(recentMatchJSON, RecentMatchResults.class);
        MatchData[] matches = null;
        if (recentMatchResults != null && recentMatchResults.matches != null) {
            matches = recentMatchResults.matches;
        }
        return matches;
    }

    public static DetailedMatchData parseDetailedMatchData(String detailedMatchJSON) {
        DetailedMatchData detailedMatchData = gson.fromJson(detailedMatchJSON, DetailedMatchData.class);
        return detailedMatchData;
    }

    public static ChampionData parseChampionData(String championJSON) {
        ChampionData championData = gson.fromJson(championJSON, ChampionData.class);
        return championData;
    }
}
