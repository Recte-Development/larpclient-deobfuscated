package com.larpclient;

import java.util.List;

public record sd(h1 timed, List<String> plain, String status, long retryAfterSeconds) {
   public sd(h1 timed, List<String> plain, String status, long retryAfterSeconds) {
      plain = List.copyOf(plain);
      this.timed = timed;
      this.plain = plain;
      this.status = status;
      this.retryAfterSeconds = retryAfterSeconds;
   }

   public static sd unavailable(String var0, long var1) {
      return new sd(null, List.of(), var0, var1);
   }
}
