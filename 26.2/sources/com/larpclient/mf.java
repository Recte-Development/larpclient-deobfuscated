package com.larpclient;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.resources.Identifier;

public final class mf {
   public static final String Z = "Empty";
   private static final String V = "/assets/larp-client/emotes/";
   public static final int o = 192;
   public static final String e = "cash_fans";
   public static final String T = "ak47";
   public static final List<mf.h> F = List.of(
      new mf.h("Take the L", "take_the_l.json", null),
      new mf.h("Floss", "floss.json", null),
      new mf.h("Maybach Bounce", "maybach_bounce.json", null, "maybach"),
      new mf.h("Money Spread", "money_spread.json", null, "cash_fans"),
      new mf.h("AK Spray", "ak_spray.json", null, "ak47"),
      new mf.h("Crypto Mining", "crypto_mining.json", null, "crypto_rock")
   );
   private static final Map<String, cx> w = new ConcurrentHashMap<>();

   private mf() {
   }

   public static String[] j() {
      String[] var0 = new String[F.size() + 1];
      var0[0] = "Empty";

      for (int var1 = 0; var1 < F.size(); var1++) {
         var0[var1 + 1] = F.get(var1).label();
      }

      return var0;
   }

   public static mf.h j(String var0) {
      for (mf.h var2 : F) {
         if (var2.label().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static cx q(mf.h var0) {
      cx var1 = w.get(var0.label());
      if (var1 != null) {
         return var1;
      }

      try (InputStream var2 = mf.class.getResourceAsStream("/assets/larp-client/emotes/" + var0.file())) {
         if (var2 == null) {
            throw new IllegalStateException("missing /assets/larp-client/emotes/" + var0.file());
         }

         cx var3 = cx.c(new InputStreamReader(var2, StandardCharsets.UTF_8), var0.loop());
         com.larpclient.h.U(
            "Emotes", "Loaded {} by {}: ticks {}..{}, loop={} from {}, ignored={}", var0.label(), var3.d, var3.V, var3.E, var3.i, var3.Z, var3.g()
         );
         w.put(var0.label(), var3);
         return var3;
      } catch (Exception var7) {
         com.larpclient.h.m("Emotes", "Could not load bundled emote " + var0.label() + "; slot stays unavailable", var7);
         return null;
      }
   }

   public record h(String label, String file, cx.w loop, String prop) {
      public h(String var1, String var2, cx.w var3) {
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
