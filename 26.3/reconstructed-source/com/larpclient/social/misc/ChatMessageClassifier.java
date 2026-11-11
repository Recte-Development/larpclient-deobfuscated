package com.larpclient.social.misc;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ChatMessageClassifier {
   static final List<ChatMessageClassifier.Rule> rules = List.of(
      new ChatMessageClassifier.Rule(
         ChatMessageClassifier.Kind.IGNORED, List.of("duel queue", "rtp queue", "rtpqueue", "opponent", "1v1", " duel", "party"), List.of(), List.of()
      ),
      rule(
         ChatMessageClassifier.Kind.RATE_LIMITED,
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
      rule(
         ChatMessageClassifier.Kind.UNKNOWN_COMMAND,
         List.of(
            "unknown command",
            "this command does not exist",
            "unknown or incomplete command",
            "no permission",
            "don't have permission",
            "do not have permission"
         )
      ),
      rule(ChatMessageClassifier.Kind.ALREADY_TELEPORTING, List.of("already rtping", "already rtp'ing", "already teleporting", "already being teleported")),
      rule(ChatMessageClassifier.Kind.MAINTENANCE, List.of("area in maintenance", "in maintenance", "servers are updating", "server is restarting")),
      new ChatMessageClassifier.Rule(
         ChatMessageClassifier.Kind.UNSAFE_LOCATION,
         List.of("unsafe", "no safe location", "could not find a safe", "couldn't find a safe"),
         List.of(List.of("teleport", "tp", "location", "rtp")),
         List.of()
      ),
      rule(ChatMessageClassifier.Kind.IN_COMBAT, List.of("in combat", "combat tag", "combat-tag", "combat logged", "while fighting")),
      new ChatMessageClassifier.Rule(
         ChatMessageClassifier.Kind.PROTECTED_AREA,
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
      rule(
         ChatMessageClassifier.Kind.SEARCHING,
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
      rule(
         ChatMessageClassifier.Kind.TELEPORTED,
         List.of(
            "teleported to a random location", "you have been teleported", "teleported you to", "you were teleported", "you teleported", "teleported randomly"
         )
      ),
      new ChatMessageClassifier.Rule(
         ChatMessageClassifier.Kind.CANCELLED, List.of("cancelled", "canceled", "you moved"), List.of(List.of("teleport", "rtp", "moved")), List.of("request")
      ),
      new ChatMessageClassifier.Rule(
         ChatMessageClassifier.Kind.COUNTDOWN,
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
      new ChatMessageClassifier.Rule(
         ChatMessageClassifier.Kind.COOLDOWN,
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
   private static final Pattern clockPattern = Pattern.compile("(?<![0-9])(\\d{1,2}):(\\d{2})(?![0-9])");
   private static final Pattern durationPattern = Pattern.compile(
      "(\\d{1,5})\\s*(hours|hour|hrs|hr|h|minutes|minute|mins|min|m|seconds|second|secs|sec|s)(?![a-z])"
   );
   private static final Pattern colorCodePattern = Pattern.compile("§.");
   private static final Pattern chatPrefixPattern = Pattern.compile("^(\\[[^\\]]{0,24}\\]\\s*)*<[^>]{1,40}>\\s");
   private static final Pattern senderPattern = Pattern.compile("^(?:\\[[^\\]]{0,32}\\]\\s*|[^\\w\\s.]{1,3}\\s*)*([.\\w]{2,17})\\s*(?:▷|»|>>|➥|→|:)\\s");
   private static final String smallCapsChars = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ";
   private static final String plainChars = "abcdefghijklmnopqrstuvwyz";

   private static ChatMessageClassifier.Rule rule(ChatMessageClassifier.Kind var0, List<String> var1) {
      return new ChatMessageClassifier.Rule(var0, var1, List.of(), List.of());
   }

   public static ChatMessageClassifier.Result classify(String var0, Predicate<String> var1) {
      if (var0 != null && !var0.isBlank() && var0.length() <= 240) {
         String var2 = normalize(var0);
         if (!var2.isEmpty() && !var2.startsWith("larp ") && !isPlayerChat(var2, var1)) {
            for (ChatMessageClassifier.Rule var4 : rules) {
               if (var4.matches(var2)) {
                  if (var4.kind() == ChatMessageClassifier.Kind.COOLDOWN) {
                     return new ChatMessageClassifier.Result(ChatMessageClassifier.Kind.COOLDOWN, parseWaitMs(var2));
                  }

                  return new ChatMessageClassifier.Result(var4.kind(), -1L);
               }
            }

            if (var2.matches(".*\\b(wait|again in)\\b.*")) {
               long var5 = parseWaitMs(var2);
               if (var5 >= 0L) {
                  return new ChatMessageClassifier.Result(ChatMessageClassifier.Kind.COOLDOWN, var5);
               }
            }

            return ChatMessageClassifier.Result.NONE;
         } else {
            return ChatMessageClassifier.Result.NONE;
         }
      } else {
         return ChatMessageClassifier.Result.NONE;
      }
   }

   static boolean isPlayerChat(String var0, Predicate<String> var1) {
      if (chatPrefixPattern.matcher(var0).find()) {
         return true;
      }

      if (var1 == null) {
         return false;
      }

      Matcher var2 = senderPattern.matcher(var0);
      return var2.find() && var1.test(var2.group(1));
   }

   public static String normalize(String var0) {
      String var1 = colorCodePattern.matcher(var0).replaceAll("").replace('’', '\'').trim().toLowerCase(Locale.ROOT);
      StringBuilder var2 = new StringBuilder(var1.length());

      for (int var3 = 0; var3 < var1.length(); var3++) {
         char var4 = var1.charAt(var3);
         int var5 = "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘǫʀꜱᴛᴜᴠᴡʏᴢ".indexOf(var4);
         var2.append(var5 >= 0 ? "abcdefghijklmnopqrstuvwyz".charAt(var5) : var4);
      }

      return var2.toString();
   }

   public static long parseWaitMs(String var0) {
      if (var0 == null) {
         return -1L;
      }

      Matcher var1 = clockPattern.matcher(var0);
      if (var1.find()) {
         long var2 = Long.parseLong(var1.group(1));
         long var4 = Long.parseLong(var1.group(2));
         if (var4 < 60L) {
            return (var2 * 60L + var4) * 1000L;
         }
      }

      long var9 = 0L;
      byte var10 = 0;

      for (Matcher var5 = durationPattern.matcher(var0); var5.find(); var10 = 1) {
         long var6 = Long.parseLong(var5.group(1));
         String var8 = var5.group(2);
         var9 += var8.startsWith("h") ? var6 * 3600000L : (var8.startsWith("m") ? var6 * 60000L : var6 * 1000L);
      }

      return var10 != 0 ? var9 : -1L;
   }

   private ChatMessageClassifier() {
   }

   public enum Kind {
      IGNORED,
      SEARCHING,
      ALREADY_TELEPORTING,
      TELEPORTED,
      COUNTDOWN,
      CANCELLED,
      UNSAFE_LOCATION,
      MAINTENANCE,
      COOLDOWN,
      RATE_LIMITED,
      IN_COMBAT,
      UNKNOWN_COMMAND,
      PROTECTED_AREA;
   }

   public record Result(ChatMessageClassifier.Kind kind, long waitMs) {
      public static final ChatMessageClassifier.Result NONE = new ChatMessageClassifier.Result(ChatMessageClassifier.Kind.IGNORED, -1L);
   }

   record Rule(ChatMessageClassifier.Kind kind, List<String> phrases, List<List<String>> context, List<String> unless) {
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
}
