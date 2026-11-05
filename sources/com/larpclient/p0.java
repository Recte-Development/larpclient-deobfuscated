package com.larpclient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.Identifier;

public final class p0 {
   public static final String v = "Empty";
   private static final String U = "/assets/larp-client/emotes/";
   public static final String u = "cash_fans";
   public static final String z = "ak47";
   public static final List<p0.u> d = List.of(
      new p0.u("Take the L", "take_the_l.json", null),
      new p0.u("Floss", "floss.json", null),
      new p0.u("Maybach Bounce", "maybach_bounce.json", null, "maybach"),
      new p0.u("Money Spread", "money_spread.json", null, "cash_fans"),
      new p0.u("AK Spray", "ak_spray.json", null, "ak47")
   );
   private static final Map<String, uy> J = new ConcurrentHashMap<>();

   private p0() {
   }

   public static String[] y() {
      String[] var0 = new String[d.size() + 1];
      var0[0] = "Empty";

      for (int var1 = 0; var1 < d.size(); var1++) {
         var0[var1 + 1] = d.get(var1).label();
      }

      return var0;
   }

   public static p0.u X(String var0) {
      for (p0.u var2 : d) {
         if (var2.label().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static uy x(p0.u var0) {
      uy var1 = J.get(var0.label());
      if (var1 != null) {
         return var1;
      }

      try (InputStream var2 = p0.class.getResourceAsStream("/assets/larp-client/emotes/" + var0.file())) {
         if (var2 == null) {
            throw new IllegalStateException("missing /assets/larp-client/emotes/" + var0.file());
         }

         uy var3 = uy.B(new InputStreamReader(var2, StandardCharsets.UTF_8), var0.loop());
         mt.U("Emotes", "Loaded {} by {}: ticks {}..{}, loop={} from {}, ignored={}", var0.label(), var3.v, var3.d, var3.k, var3.K, var3.P, var3.E());
         J.put(var0.label(), var3);
         return var3;
      } catch (Exception var7) {
         mt.P("Emotes", "Could not load bundled emote " + var0.label() + "; slot stays unavailable", var7);
         return null;
      }
   }

   public record u(String label, String file, uy.h loop, String prop) {
      public u(String var1, String var2, uy.h var3) {
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
