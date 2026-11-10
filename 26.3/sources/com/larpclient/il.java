package com.larpclient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.Identifier;

public final class il {
   public static final String o = "Empty";
   private static final String z = "/assets/larp-client/emotes/";
   public static final int s = 192;
   public static final String B = "cash_fans";
   public static final String a = "ak47";
   public static final List<il.w> h = List.of(
      new il.w("Take the L", "take_the_l.json", null),
      new il.w("Floss", "floss.json", null),
      new il.w("Maybach Bounce", "maybach_bounce.json", null, "maybach"),
      new il.w("Money Spread", "money_spread.json", null, "cash_fans"),
      new il.w("AK Spray", "ak_spray.json", null, "ak47"),
      new il.w("Crypto Mining", "crypto_mining.json", null, "crypto_rock")
   );
   private static final Map<String, bo> N = new ConcurrentHashMap<>();

   private il() {
   }

   public static String[] d() {
      String[] var0 = new String[h.size() + 1];
      var0[0] = "Empty";

      for (int var1 = 0; var1 < h.size(); var1++) {
         var0[var1 + 1] = h.get(var1).label();
      }

      return var0;
   }

   public static il.w c(String var0) {
      for (il.w var2 : h) {
         if (var2.label().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static bo o(il.w var0) {
      bo var1 = N.get(var0.label());
      if (var1 != null) {
         return var1;
      }

      try (InputStream var2 = il.class.getResourceAsStream("/assets/larp-client/emotes/" + var0.file())) {
         if (var2 == null) {
            throw new IllegalStateException("missing /assets/larp-client/emotes/" + var0.file());
         }

         bo var3 = bo.k(new InputStreamReader(var2, StandardCharsets.UTF_8), var0.loop());
         _q.c("Emotes", "Loaded {} by {}: ticks {}..{}, loop={} from {}, ignored={}", var0.label(), var3.l, var3.o, var3.g, var3.V, var3.B, var3.U());
         N.put(var0.label(), var3);
         return var3;
      } catch (Exception var7) {
         _q.o("Emotes", "Could not load bundled emote " + var0.label() + "; slot stays unavailable", var7);
         return null;
      }
   }

   public record w(String label, String file, bo.d loop, String prop) {
      public w(String var1, String var2, bo.d var3) {
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
