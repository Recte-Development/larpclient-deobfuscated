package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class i0 {
   private static final ArrayDeque<String> O = new ArrayDeque<>();
   private static String L = "";
   private static final Set<String> y = Set.of("Auth", "Config", "Render", "API", "Module", "Error", "Releases", "Reports", "Status");
   private static final Pattern q = Pattern.compile(
      "(?i)LARP-[A-F0-9]{4}(?:-[A-F0-9]{4}){3}|LARP-[A-F0-9]{16}|\\b[a-f0-9]{64}\\b|\\bBearer\\s+\\S+|\\bmfa\\.[\\w-]+|\\b[\\w-]{20,}\\.[\\w-]{6}\\.[\\w-]{20,}\\b|(?:license[_ -]?key|access[_ -]?token|refresh[_ -]?token|install[_ -]?token|session[_ -]?token|token|password|client[_ -]?secret|authorization|auth\\.json|hwid)\\s*[\"']?\\s*[:=][^\\r\\n]*|-----BEGIN .*PRIVATE KEY-----[\\s\\S]*?-----END .*PRIVATE KEY-----"
   );

   public static String d(String var0) {
      return var0 == null ? "" : q.matcher(var0).replaceAll("[redacted]").replaceAll("(?:[A-Za-z]:\\\\|/home/|/Users/)[^\\s\"']+", "[path]").replace('§', ' ');
   }

   public static synchronized void W(String var0, i0.p var1) {
      G(var0, var1.name());
   }

   public static synchronized void m(String var0, String var1, Throwable var2) {
      if (y.contains(var0)) {
         G(var0, var1 + " event");
         if (var2 != null) {
            L = "[Larp/" + var0 + "] " + var2.getClass().getSimpleName();
         }
      }
   }

   private static void G(String var0, String var1) {
      if (y.contains(var0)) {
         if (O.size() == 80) {
            O.removeFirst();
         }

         O.addLast("[Larp/" + var0 + "] " + var1);
      }
   }

   public static synchronized String G() {
      return String.join("\n", O);
   }

   public static synchronized String d() {
      return L;
   }

   public static JsonObject d(String var0, String var1, String var2, String var3, List<String> var4) {
      var0 = d(var0).trim();
      var1 = d(var1).trim();
      if (!var0.isEmpty() && var0.length() <= 120 && var1.length() >= 10 && var1.length() <= 4000) {
         JsonObject var5 = new JsonObject();
         var5.addProperty("title", var0);
         var5.addProperty("description", var1);
         var5.addProperty("minecraftVersion", a0.N);
         var5.addProperty("larpVersion", a0.r);
         var5.addProperty("fabricVersion", d(var2));
         var5.addProperty(
            "operatingSystem", var3.contains("Windows") ? "Windows" : (var3.contains("Mac") ? "macOS" : (var3.contains("Linux") ? "Linux" : "Other"))
         );
         JsonArray var6 = new JsonArray();
         var4.stream().sorted().limit(200L).filter(var0x -> var0x.matches("[A-Za-z0-9][A-Za-z0-9 _.-]{0,63}")).forEach(var6::add);
         var5.add("enabledModules", var6);
         var5.addProperty("logs", G());
         var5.addProperty("exception", d());
         if (var5.toString().getBytes(StandardCharsets.UTF_8).length > 32768) {
            throw new wr(413, "PAYLOAD_TOO_LARGE");
         } else {
            return var5;
         }
      } else {
         throw new IllegalArgumentException("Enter a title (1–120 characters) and description (10–4000 characters).");
      }
   }

   public enum p {
      F,
      g,
      Y,
      T,
      p,
      N;
   }
}
