package com.larpclient;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class zi {
   static final List<zi.i> R = List.of(
      new zi.i(zi.e.s, List.of("duel queue", "rtp queue", "rtpqueue", "opponent", "1v1", " duel", "party"), List.of(), List.of()),
      U(
         zi.e.X,
         List.of(
            "before your next message",
            "do not repeat the same",
            "don't repeat the same",
            "sending messages too fast",
            "sending commands too fast",
            "please slow down your chat",
            "you are being rate limited"
         )
      ),
      U(
         zi.e.T,
         List.of(
            "unknown command",
            "this command does not exist",
            "unknown or incomplete command",
            "no permission",
            "don't have permission",
            "do not have permission"
         )
      ),
      U(zi.e.A, List.of("already rtping", "already rtp'ing", "already teleporting", "already being teleported")),
      U(zi.e.q, List.of("area in maintenance", "in maintenance", "servers are updating", "server is restarting")),
      new zi.i(
         zi.e.V,
         List.of("unsafe", "no safe location", "could not find a safe", "couldn't find a safe"),
         List.of(List.of("teleport", "tp", "location", "rtp")),
         List.of()
      ),
      U(zi.e.L, List.of("in combat", "combat tag", "combat-tag", "combat logged", "while fighting")),
      new zi.i(
         zi.e.M,
         List.of(
            "cannot do this on this area",
            "can't do this on this area",
            "cannot do this in this area",
            "can't do this in this area",
            "this area is protected",
            "you cannot break this",
            "you can't break this",
            "you cannot break blocks",
            "you can't break blocks",
            "not allowed to break",
            "cannot break blocks here",
            "you cannot build here",
            "you can't build here",
            "can't break that block here",
            "cannot break that block here"
         ),
         List.of(),
         List.of("without")
      ),
      U(
         zi.e.C,
         List.of(
            "finding random location",
            "finding a random location",
            "finding a safe location",
            "searching for a safe",
            "searching for a location",
            "looking for a safe",
            "finding a location"
         )
      ),
      U(
         zi.e.r,
         List.of(
            "teleported to a random location", "you have been teleported", "teleported you to", "you were teleported", "you teleported", "teleported randomly"
         )
      ),
      new zi.i(zi.e.R, List.of("cancelled", "canceled", "you moved"), List.of(List.of("teleport", "rtp", "moved")), List.of("request")),
      new zi.i(
         zi.e.f,
         List.of(
            "teleporting in",
            "teleport in",
            "teleportation in",
            "teleportation will commence",
            "will be teleported in",
            "don't move",
            "do not move",
            "stand still"
         ),
         List.of(),
         List.of("request", "cancel")
      ),
      new zi.i(
         zi.e.J,
         List.of(
            "cooldown",
            "cool down",
            "please wait",
            "you must wait",
            "wait before",
            "try again in",
            "too fast",
            "slow down",
            "you need to wait",
            "can use this again",
            "can rtp again"
         ),
         List.of(),
         List.of("refreshing")
      )
   );
   private static final Pattern C = Pattern.compile("(?<![0-9])(\\d{1,2}):(\\d{2})(?![0-9])");
   private static final Pattern k = Pattern.compile("(\\d{1,5})\\s*(hours|hour|hrs|hr|h|minutes|minute|mins|min|m|seconds|second|secs|sec|s)(?![a-z])");
   private static final Pattern Q = Pattern.compile("§.");
   private static final Pattern O = Pattern.compile("^(\\[[^\\]]{0,24}\\]\\s*)*<[^>]{1,40}>\\s");
   private static final Pattern e = Pattern.compile("^(?:\\[[^\\]]{0,32}\\]\\s*|[^\\w\\s.]{1,3}\\s*)*([.\\w]{2,17})\\s*(?:▷|»|>>|➥|→|:)\\s");
   private static final String h = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ";
   private static final String X = "abcdefghijklmnopqrstuvwyz";

   private static zi.i U(zi.e var0, List<String> var1) {
      return new zi.i(var0, var1, List.of(), List.of());
   }

   public static zi.l p(String var0, Predicate<String> var1) {
      if (var0 != null && !var0.isBlank() && var0.length() <= 240) {
         String var2 = c(var0);
         if (!var2.isEmpty() && !var2.startsWith("larp ") && !v(var2, var1)) {
            for (zi.i var4 : R) {
               if (var4.matches(var2)) {
                  if (var4.kind() == zi.e.J) {
                     return new zi.l(zi.e.J, G(var2));
                  }

                  return new zi.l(var4.kind(), -1L);
               }
            }

            if (var2.matches(".*\\b(wait|again in)\\b.*")) {
               long var5 = G(var2);
               if (var5 >= 0L) {
                  return new zi.l(zi.e.J, var5);
               }
            }

            return zi.l.NONE;
         } else {
            return zi.l.NONE;
         }
      } else {
         return zi.l.NONE;
      }
   }

   static boolean v(String var0, Predicate<String> var1) {
      if (O.matcher(var0).find()) {
         return true;
      }

      if (var1 == null) {
         return false;
      }

      Matcher var2 = e.matcher(var0);
      return var2.find() && var1.test(var2.group(1));
   }

   public static String c(String var0) {
      String var1 = Q.matcher(var0).replaceAll("").replace('’', '\'').trim().toLowerCase(Locale.ROOT);
      StringBuilder var2 = new StringBuilder(var1.length());

      for (int var3 = 0; var3 < var1.length(); var3++) {
         char var4 = var1.charAt(var3);
         int var5 = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ".indexOf(var4);
         var2.append(var5 >= 0 ? "abcdefghijklmnopqrstuvwyz".charAt(var5) : var4);
      }

      return var2.toString();
   }

   public static long G(String var0) {
      if (var0 == null) {
         return -1L;
      }

      Matcher var1 = C.matcher(var0);
      if (var1.find()) {
         long var2 = Long.parseLong(var1.group(1));
         long var4 = Long.parseLong(var1.group(2));
         if (var4 < 60L) {
            return (var2 * 60L + var4) * 1000L;
         }
      }

      long var9 = 0L;
      byte var10 = 0;

      for (Matcher var5 = k.matcher(var0); var5.find(); var10 = 1) {
         long var6 = Long.parseLong(var5.group(1));
         String var8 = var5.group(2);
         var9 += var8.startsWith("h") ? var6 * 3600000L : (var8.startsWith("m") ? var6 * 60000L : var6 * 1000L);
      }

      return var10 != 0 ? var9 : -1L;
   }

   private zi() {
   }

   public enum e {
      s,
      C,
      A,
      r,
      f,
      R,
      V,
      q,
      J,
      X,
      L,
      T,
      M;
   }

   record i(zi.e kind, List<String> phrases, List<List<String>> context, List<String> unless) {
      boolean matches(String var1) {
         if (this.phrases.stream().noneMatch(var1::contains)) {
            return false;
         }

         for (List var3 : this.context) {
            if (var3.stream().noneMatch(var1::contains)) {
               return false;
            }
         }

         return this.unless.stream().noneMatch(var1::contains);
      }
   }

   public record l(zi.e kind, long waitMs) {
      public static final zi.l NONE = new zi.l(zi.e.s, -1L);
   }
}
