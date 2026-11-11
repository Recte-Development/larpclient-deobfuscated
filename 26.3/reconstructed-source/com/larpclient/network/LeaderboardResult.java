package com.larpclient.network;

import com.larpclient.social.misc.TimedLyrics;
import java.util.List;

public record LeaderboardResult(TimedLyrics timed, List<String> plain, String status, long retryAfterSeconds) {
   public LeaderboardResult(TimedLyrics timed, List<String> plain, String status, long retryAfterSeconds) {
      plain = List.copyOf(plain);
      this.timed = timed;
      this.plain = plain;
      this.status = status;
      this.retryAfterSeconds = retryAfterSeconds;
   }

   public static LeaderboardResult unavailable(String var0, long var1) {
      return new LeaderboardResult(null, List.of(), var0, var1);
   }
}
