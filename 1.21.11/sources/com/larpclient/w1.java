package com.larpclient;

import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map.Entry;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_2960;
import net.minecraft.class_310;

public final class w1 {
   private static final String M = "/assets/larp-client/knives/";
   static final String[] Y = new String[]{"galaxy_clouds", "galaxy_glow", "galaxy_stars"};
   private static final EnumMap<co, g_> S = new EnumMap<>(co.class);
   private static boolean J;
   private static g_ B;

   public static class_2960 T(co var0) {
      return class_2960.method_60655("larp-client", "dynamic/knife_" + var0.P);
   }

   public static class_2960 A(int var0) {
      return class_2960.method_60655("larp-client", "dynamic/knife_" + Y[var0]);
   }

   public static g_ P(co var0) {
      return J ? S.get(var0) : null;
   }

   public static g_ n(co var0, boolean var1) {
      return J && var0 == co.F && var1 ? B : P(var0);
   }

   public static void J() {
      J = (boolean)0;
      S.clear();
      B = null;

      for (co var3 : co.values()) {
         try {
            S.put(var3, g_.read(w1.class.getResourceAsStream("/assets/larp-client/knives/" + var3.P + ".bin")));
         } catch (IOException var14) {
            q8.u("SwordModels", "Cannot load " + var3.P + "; retaining vanilla sword", var14);
         }
      }

      try {
         B = g_.read(w1.class.getResourceAsStream("/assets/larp-client/knives/dress_watch_slim.bin")).sharing(S.get(co.F), "case");
      } catch (IOException var13) {
         S.remove(co.F);
         q8.u("SwordModels", "Cannot load slim watch; retaining vanilla sword", var13);
      }

      try {
         for (int var20 = 0; var20 < Y.length; var20++) {
            String var23 = Y[var20];

            try (InputStream var26 = w1.class.getResourceAsStream("/assets/larp-client/knives/" + var23 + ".png")) {
               if (var26 == null) {
                  throw new IOException("Missing " + var23 + " texture");
               }

               class_1011 var29 = class_1011.method_4309(var26);

               class_1043 var4;
               try {
                  var4 = new class_1043(() -> "LARP knife " + var23, var29);
               } catch (RuntimeException var12) {
                  var29.close();
                  throw var12;
               }

               try {
                  class_310.method_1551().method_1531().method_4616(A(var20), var4);
               } catch (RuntimeException var11) {
                  var4.close();
                  throw var11;
               }
            }
         }

         q8.v("SwordModels", "Galaxy blade layers loaded: {}", String.join(", ", Y));
      } catch (IOException | RuntimeException var19) {
         S.keySet().removeIf(var0 -> !var0.v());
         q8.u("SwordModels", "Cannot load the galaxy blade finish; using vanilla swords for coated knives", var19);
      }

      for (co var30 : co.values()) {
         try (InputStream var31 = w1.class.getResourceAsStream("/assets/larp-client/knives/" + var30.P + ".png")) {
            if (var31 == null) {
               throw new IOException("Missing knife texture");
            }

            class_1011 var5 = class_1011.method_4309(var31);

            class_1043 var6;
            try {
               g_ var7 = S.get(var30);
               if (var7 != null && !var30.v()) {
                  S.put(var30, var7.shaded(var5.method_4307(), var5.method_4323(), var5::method_61940));
               }

               var6 = new class_1043(() -> "LARP " + var30.P, var5);
            } catch (RuntimeException var15) {
               var5.close();
               throw var15;
            }

            try {
               class_310.method_1551().method_1531().method_4616(T(var30), var6);
            } catch (RuntimeException var9) {
               var6.close();
               throw var9;
            }
         } catch (IOException | RuntimeException var17) {
            S.remove(var30);
            q8.u("SwordModels", "Cannot load " + var30.P + " texture; using vanilla sword", var17);
         }
      }

      J = (boolean)1;
      if (!S.containsKey(co.F)) {
         B = null;
      }

      for (Entry var25 : S.entrySet()) {
         if (!((co)var25.getKey()).v()) {
            int var28 = ((g_)var25.getValue()).parts().stream().mapToInt(var0 -> var0.vertices().length / 8).sum();
            q8.v(
               "SwordModels",
               "{}: {} vertices, native palm fit Y={} Z={}, cached bevel shading",
               ((co)var25.getKey()).L,
               var28,
               sy.V((co)var25.getKey()),
               sy.c((co)var25.getKey())
            );
         }
      }

      q8.G("SwordModels", "Loaded {} sword models and textures", S.size());
   }

   private w1() {
   }
}
