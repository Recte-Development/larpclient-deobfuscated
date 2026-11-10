package com.larpclient;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.file.FileSystemException;
import javax.net.ssl.SSLException;

public final class eo {
   private eo() {
   }

   public static String u(Exception var0) {
      if (var0 instanceof FileSystemException) {
         return "Cannot access local license files; check .config/larp-client permissions and retry";
      } else if (var0 instanceof UnknownHostException) {
         return "Auth server address not found (DNS)";
      } else if (var0 instanceof SocketTimeoutException) {
         return "Auth server timed out; retry shortly";
      } else {
         return var0 instanceof SSLException ? "Secure auth connection failed (TLS)" : "Cannot reach auth server; check connection";
      }
   }

   public static String M(int var0) {
      return switch (var0) {
         case 401, 403 -> "Auth server rejected client credentials";
         case 404 -> "Auth endpoint not found; check server setup";
         case 429 -> "Too many auth requests; retry shortly";
         default -> var0 >= 500 ? "Auth service unavailable (HTTP " + var0 + ")" : "Auth request rejected (HTTP " + var0 + ")";
      };
   }
}
