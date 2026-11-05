package com.larpclient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.Identifier;

public final class ya {
   public static final String v = "Empty";
   private static final String g = "/assets/larp-client/emotes/";
   public static final String M = "cash_fans";
   public static final String O = "ak47";
   public static final List<ya.g> A = List.of(
      new ya.g("Take the L", "take_the_l.json", null),
      new ya.g("Floss", "floss.json", null),
      new ya.g("Maybach Bounce", "maybach_bounce.json", null, "maybach"),
      new ya.g("Money Spread", "money_spread.json", null, "cash_fans"),
      new ya.g("AK Spray", "ak_spray.json", null, "ak47")
   );
   private static final Map<String, jc> d = new ConcurrentHashMap<>();

   private ya() {
   }

   public static String[] P() {
      String[] var0 = new String[A.size() + 1];
      var0[0] = "Empty";

      for (int var1 = 0; var1 < A.size(); var1++) {
         var0[var1 + 1] = A.get(var1).label();
      }

      return var0;
   }

   public static ya.g T(String var0) {
      for (ya.g var2 : A) {
         if (var2.label().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static jc M(ya.g var0) {
      jc var1 = d.get(var0.label());
      if (var1 != null) {
         return var1;
      }

      try (InputStream var2 = ya.class.getResourceAsStream("/assets/larp-client/emotes/" + var0.file())) {
         if (var2 == null) {
            throw new IllegalStateException("missing /assets/larp-client/emotes/" + var0.file());
         }

         jc var3 = jc.K(new InputStreamReader(var2, StandardCharsets.UTF_8), var0.loop());
         r4.g("Emotes", "Loaded {} by {}: ticks {}..{}, loop={} from {}, ignored={}", var0.label(), var3.Y, var3.c, var3.q, var3.P, var3.X, var3.s());
         d.put(var0.label(), var3);
         return var3;
      } catch (Exception var7) {
         r4.r("Emotes", "Could not load bundled emote " + var0.label() + "; slot stays unavailable", var7);
         return null;
      }
   }

   public record g(String label, String file, jc.f loop, String prop) {
      public g(String var1, String var2, jc.f var3) {
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
