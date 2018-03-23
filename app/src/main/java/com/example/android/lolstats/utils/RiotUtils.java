package com.example.android.lolstats.utils;

import android.net.Uri;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;


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

    private static final String RIOT_API_APPID_PARAM = "api_key";

    private static final String  RIOT_API_APPID = "RGAPI-bf5e4831-5d54-4ce0-a379-797e8c7db738";

    private static final Gson gson = new Gson();

    public static String getRiotURL(String region) {
        return HTTPS + region + RIOT_BASE_URL;
    }

    public static String buildSummonerSearchURL(String summonerName, String region) {
        String riotUrl = getRiotURL(region);
        return Uri.parse(riotUrl).buildUpon()
                .appendEncodedPath(RIOT_SUMMONER_SEARCH_PATH)
                .appendPath(String.valueOf(summonerName))
                .appendQueryParameter(RIOT_API_APPID_PARAM, RIOT_API_APPID)
                .build().toString();
    }

    public static String buildRecentMatchesURL(long accountID, String region) {
        String riotURL = getRiotURL(region);
        return Uri.parse(riotURL).buildUpon()
                .appendEncodedPath(RIOT_RECENT_MATCHES_PATH_START)
                .appendPath(String.valueOf(accountID))
                .appendEncodedPath(RIOT_RECENT_MATCHES_PATH_END)
                .appendQueryParameter(RIOT_API_APPID_PARAM, RIOT_API_APPID)
                .build().toString();
    }

    public static String buildDetailedMatchURL(long matchID, String region) {
        String riotURL = getRiotURL(region);
        return Uri.parse(riotURL).buildUpon()
                .appendEncodedPath(RIOT_MATCH_PATH)
                .appendPath(String.valueOf(matchID))
                .appendQueryParameter(RIOT_API_APPID_PARAM, RIOT_API_APPID)
                .build().toString();
    }

    public static class SummonerDataResults implements Serializable {
        public String name;
        public long accountId;
    }

    public static class RecentMatchResults {
        MatchData[] matches;
    }

    public static class MatchData implements Serializable {
        public long timestamp;
        public long gameId;
        public String lane;
        public static final String EXTRA_MATCH_ITEM = "extraMatchItem";
    }

    public static class DetailedMatchData {
        public ParticipantIdentity[] participantIdentities;
        public TeamStats[] teams;
        public Participants[] participants;
    }

    public static class ParticipantIdentity {
        public Player player;
        public long participantId;
    }

    public static class Player {
        public String summonerName;
    }

    public static class TeamStats implements Serializable {
        public String win;
        public String baronKills;
        public String inhibitorKills;
        public String towerKills;
        public String dragonKills;
    }

    public static class Participants {
        public Stats stats;
        public long championId;
    }

    public static class Stats {
        public String kills;
        public String assists;
        public String deaths;
        public String win;
        public String totalMinionsKilled;
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
}
