package com.larpclient;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

public final class gy {
   private static final String h = "/assets/larp-client/knives/";
   static final String[] c = new String[]{"galaxy_clouds", "galaxy_glow", "galaxy_stars"};
   private static final EnumMap<tc, m8> U = new EnumMap<>(tc.class);
   private static boolean S;
   private static m8 y;

   public static Identifier T(tc var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "dynamic/knife_" + var0.A);
   }

   public static Identifier D(int var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "dynamic/knife_" + c[var0]);
   }

   public static m8 x(tc var0) {
      return S ? U.get(var0) : null;
   }

   public static m8 K(tc var0, boolean var1) {
      return S && var0 == tc.x && var1 ? y : x(var0);
   }

   public static void B() {
      S = (boolean)0;
      U.clear();
      y = null;

      for (tc var3 : tc.values()) {
         try {
            U.put(var3, m8.read(gy.class.getResourceAsStream("/assets/larp-client/knives/" + var3.A + ".bin")));
         } catch (IOException var14) {
            com.larpclient.h.m("SwordModels", "Cannot load " + var3.A + "; retaining vanilla sword", var14);
         }
      }

      try {
         y = m8.read(gy.class.getResourceAsStream("/assets/larp-client/knives/dress_watch_slim.bin")).sharing(U.get(tc.x), "case");
      } catch (IOException var13) {
         U.remove(tc.x);
         com.larpclient.h.m("SwordModels", "Cannot load slim watch; retaining vanilla sword", var13);
      }

      try {
         for (int var20 = 0; var20 < c.length; var20++) {
            String var23 = c[var20];

            try (InputStream var26 = gy.class.getResourceAsStream("/assets/larp-client/knives/" + var23 + ".png")) {
               if (var26 == null) {
                  throw new IOException("Missing " + var23 + " texture");
               }

               NativeImage var29 = NativeImage.read(var26);

               DynamicTexture var4;
               try {
                  var4 = new DynamicTexture(() -> "LARP knife " + var23, var29);
               } catch (RuntimeException var12) {
                  var29.close();
                  throw var12;
               }

               try {
                  Minecraft.getInstance().getTextureManager().register(D(var20), var4);
               } catch (RuntimeException var11) {
                  var4.close();
                  throw var11;
               }
            }
         }

         com.larpclient.h.U("SwordModels", "Galaxy blade layers loaded: {}", String.join(", ", c));
      } catch (IOException | RuntimeException var19) {
         U.keySet().removeIf(var0 -> !var0.L());
         com.larpclient.h.m("SwordModels", "Cannot load the galaxy blade finish; using vanilla swords for coated knives", var19);
      }

      for (tc var30 : tc.values()) {
         try (InputStream var31 = gy.class.getResourceAsStream("/assets/larp-client/knives/" + var30.A + ".png")) {
            if (var31 == null) {
               throw new IOException("Missing knife texture");
            }

            NativeImage var5 = NativeImage.read(var31);

            DynamicTexture var6;
            try {
               m8 var7 = U.get(var30);
               if (var7 != null && !var30.L()) {
                  U.put(var30, var7.shaded(var5.getWidth(), var5.getHeight(), var5::getPixel));
               }

               var6 = new DynamicTexture(() -> "LARP " + var30.A, var5);
            } catch (RuntimeException var15) {
               var5.close();
               throw var15;
            }

            try {
               Minecraft.getInstance().getTextureManager().register(T(var30), var6);
            } catch (RuntimeException var9) {
               var6.close();
               throw var9;
            }
         } catch (IOException | RuntimeException var17) {
            U.remove(var30);
            com.larpclient.h.m("SwordModels", "Cannot load " + var30.A + " texture; using vanilla sword", var17);
         }
      }

      S = (boolean)1;
      if (!U.containsKey(tc.x)) {
         y = null;
      }

      for (Entry var25 : U.entrySet()) {
         if (!((tc)var25.getKey()).L()) {
            int var28 = ((m8)var25.getValue()).parts().stream().mapToInt(var0 -> var0.vertices().length / 8).sum();
            com.larpclient.h.U(
               "SwordModels",
               "{}: {} vertices, native palm fit Y={} Z={}, cached bevel shading",
               ((tc)var25.getKey()).P,
               var28,
               pu.S((tc)var25.getKey()),
               pu.m((tc)var25.getKey())
            );
         }
      }

      com.larpclient.h.A("SwordModels", "Loaded {} sword models and textures", U.size());
   }

   private gy() {
   }
}
