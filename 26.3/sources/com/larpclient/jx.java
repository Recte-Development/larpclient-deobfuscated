package com.larpclient;

import java.util.List;

public record jx(j timed, List<String> plain, String status, long retryAfterSeconds) {
   public jx(j timed, List<String> plain, String status, long retryAfterSeconds) {
      plain = List.copyOf(plain);
      this.timed = timed;
      this.plain = plain;
      this.status = status;
      this.retryAfterSeconds = retryAfterSeconds;
   }

   public static jx unavailable(String var0, long var1) {
      return new jx(null, List.of(), var0, var1);
   }
}
