package com.larpclient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.class_2960;

public final class fj {
   public static final String b = "Empty";
   private static final String X = "/assets/larp-client/emotes/";
   public static final int C = 192;
   public static final String I = "cash_fans";
   public static final String N = "ak47";
   public static final List<fj.g> Q = List.of(
      new fj.g("Take the L", "take_the_l.json", null),
      new fj.g("Floss", "floss.json", null),
      new fj.g("Maybach Bounce", "maybach_bounce.json", null, "maybach"),
      new fj.g("Money Spread", "money_spread.json", null, "cash_fans"),
      new fj.g("AK Spray", "ak_spray.json", null, "ak47"),
      new fj.g("Crypto Mining", "crypto_mining.json", null, "crypto_rock")
   );
   private static final Map<String, cb> a = new ConcurrentHashMap<>();

   private fj() {
   }

   public static String[] e() {
      String[] var0 = new String[Q.size() + 1];
      var0[0] = "Empty";

      for (int var1 = 0; var1 < Q.size(); var1++) {
         var0[var1 + 1] = Q.get(var1).label();
      }

      return var0;
   }

   public static fj.g e(String var0) {
      for (fj.g var2 : Q) {
         if (var2.label().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static cb i(fj.g var0) {
      cb var1 = a.get(var0.label());
      if (var1 != null) {
         return var1;
      }

      try (InputStream var2 = fj.class.getResourceAsStream("/assets/larp-client/emotes/" + var0.file())) {
         if (var2 == null) {
            throw new IllegalStateException("missing /assets/larp-client/emotes/" + var0.file());
         }

         cb var3 = cb.A(new InputStreamReader(var2, StandardCharsets.UTF_8), var0.loop());
         q8.v("Emotes", "Loaded {} by {}: ticks {}..{}, loop={} from {}, ignored={}", var0.label(), var3.e, var3.E, var3.K, var3.M, var3.Y, var3.Q());
         a.put(var0.label(), var3);
         return var3;
      } catch (Exception var7) {
         q8.u("Emotes", "Could not load bundled emote " + var0.label() + "; slot stays unavailable", var7);
         return null;
      }
   }

   public record g(String label, String file, cb.p loop, String prop) {
      public g(String var1, String var2, cb.p var3) {
         this(var1, var2, var3, null);
      }

      public boolean handProp() {
         return "cash_fans".equals(this.prop) || "ak47".equals(this.prop);
      }

      public class_2960 icon() {
         return class_2960.method_60655("larp-client", "textures/gui/emotes/" + this.file.replace(".json", ".png"));
      }

      public class_2960 music() {
         return class_2960.method_60655("larp-client", "emote." + this.file.replace(".json", ""));
      }
   }
}
