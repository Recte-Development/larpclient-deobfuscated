package com.larpclient;

import java.util.List;

public record jy(bc timed, List<String> plain, String status, long retryAfterSeconds) {
   public jy(bc timed, List<String> plain, String status, long retryAfterSeconds) {
      plain = List.copyOf(plain);
      this.timed = timed;
      this.plain = plain;
      this.status = status;
      this.retryAfterSeconds = retryAfterSeconds;
   }

   public static jy unavailable(String var0, long var1) {
      return new jy(null, List.of(), var0, var1);
   }
}
