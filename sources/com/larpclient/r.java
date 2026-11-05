package com.larpclient;

import java.util.List;

public record r(e6 timed, List<String> plain, String status, long retryAfterSeconds) {
   public r(e6 timed, List<String> plain, String status, long retryAfterSeconds) {
      plain = List.copyOf(plain);
      this.timed = timed;
      this.plain = plain;
      this.status = status;
      this.retryAfterSeconds = retryAfterSeconds;
   }

   public static r unavailable(String var0, long var1) {
      return new r(null, List.of(), var0, var1);
   }
}
