package com.larpclient.social.emote;

import com.larpclient.util.misc.LarpLog;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.Identifier;

public final class EmoteRegistry {
   public static final String eMPTY_LABEL = "Empty";
   private static final String aSSET_PATH = "/assets/larp-client/emotes/";
   public static final int iCON_SIZE = 192;
   public static final String pROP_CASH_FANS = "cash_fans";
   public static final String pROP_AK47 = "ak47";
   public static final List<EmoteRegistry.Emote> eMOTES = List.of(
      new EmoteRegistry.Emote("Take the L", "take_the_l.json", null),
      new EmoteRegistry.Emote("Floss", "floss.json", null),
      new EmoteRegistry.Emote("Maybach Bounce", "maybach_bounce.json", null, "maybach"),
      new EmoteRegistry.Emote("Money Spread", "money_spread.json", null, "cash_fans"),
      new EmoteRegistry.Emote("AK Spray", "ak_spray.json", null, "ak47"),
      new EmoteRegistry.Emote("Crypto Mining", "crypto_mining.json", null, "crypto_rock")
   );
   private static final Map<String, EmoteAnimation> cache = new ConcurrentHashMap<>();

   private EmoteRegistry() {
   }

   public static String[] getLabels() {
      String[] var0 = new String[eMOTES.size() + 1];
      var0[0] = "Empty";

      for (int var1 = 0; var1 < eMOTES.size(); var1++) {
         var0[var1 + 1] = eMOTES.get(var1).label();
      }

      return var0;
   }

   public static EmoteRegistry.Emote findByLabel(String var0) {
      for (EmoteRegistry.Emote var2 : eMOTES) {
         if (var2.label().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static EmoteAnimation load(EmoteRegistry.Emote var0) {
      EmoteAnimation var1 = cache.get(var0.label());
      if (var1 != null) {
         return var1;
      }

      try (InputStream var2 = EmoteRegistry.class.getResourceAsStream("/assets/larp-client/emotes/" + var0.file())) {
         if (var2 == null) {
            throw new IllegalStateException("missing /assets/larp-client/emotes/" + var0.file());
         }

         EmoteAnimation var3 = EmoteAnimation.parse(new InputStreamReader(var2, StandardCharsets.UTF_8), var0.loop());
         LarpLog.debug(
            "Emotes",
            "Loaded {} by {}: ticks {}..{}, loop={} from {}, ignored={}",
            var0.label(),
            var3.author,
            var3.beginTick,
            var3.endTick,
            var3.looping,
            var3.returnTick,
            var3.getUnsupportedFeatures()
         );
         cache.put(var0.label(), var3);
         return var3;
      } catch (Exception var7) {
         LarpLog.error("Emotes", "Could not load bundled emote " + var0.label() + "; slot stays unavailable", var7);
         return null;
      }
   }

   public record Emote(String label, String file, EmoteAnimation.LoopRange loop, String prop) {
      public Emote(String var1, String var2, EmoteAnimation.LoopRange var3) {
         this(var1, var2, var3, null);
      }

      public boolean handProp() {
         return "cash_fans".equals(this.prop) || "ak47".equals(this.prop);
      }

      public Identifier icon() {
         return Identifier.fromNamespaceAndPath("larp-client", "textures/gui/emotes/" + this.file.replace(".json", ".png"));
      }

      public Identifier music() {
         return Identifier.fromNamespaceAndPath("larp-client", "emote." + this.file.replace(".json", ""));
      }
   }
}
