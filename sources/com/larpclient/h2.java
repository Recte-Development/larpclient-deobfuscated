package com.larpclient;

import java.util.List;

public record h2(y6 timed, List<String> plain, String status, long retryAfterSeconds) {
   public h2(y6 timed, List<String> plain, String status, long retryAfterSeconds) {
      plain = List.copyOf(plain);
      this.timed = timed;
      this.plain = plain;
      this.status = status;
      this.retryAfterSeconds = retryAfterSeconds;
   }

   public static h2 unavailable(String var0, long var1) {
      return new h2(null, List.of(), var0, var1);
   }
}
