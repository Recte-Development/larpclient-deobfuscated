package com.larpclient;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

public final class jv {
   private static final String Z = "/assets/larp-client/knives/";
   static final String[] B = new String[]{"galaxy_clouds", "galaxy_glow", "galaxy_stars"};
   private static final EnumMap<yi, _r> H = new EnumMap<>(yi.class);
   private static boolean q;
   private static _r M;

   public static Identifier H(yi var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "dynamic/knife_" + var0.h);
   }

   public static Identifier n(int var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "dynamic/knife_" + B[var0]);
   }

   public static _r r(yi var0) {
      return q ? H.get(var0) : null;
   }

   public static _r N(yi var0, boolean var1) {
      return q && var0 == yi.m && var1 ? M : r(var0);
   }

   public static void M() {
      q = (boolean)0;
      H.clear();
      M = null;

      for (yi var3 : yi.values()) {
         try {
            H.put(var3, _r.read(jv.class.getResourceAsStream("/assets/larp-client/knives/" + var3.h + ".bin")));
         } catch (IOException var14) {
            r4.r("SwordModels", "Cannot load " + var3.h + "; retaining vanilla sword", var14);
         }
      }

      try {
         M = _r.read(jv.class.getResourceAsStream("/assets/larp-client/knives/dress_watch_slim.bin")).sharing(H.get(yi.m), "case");
      } catch (IOException var13) {
         H.remove(yi.m);
         r4.r("SwordModels", "Cannot load slim watch; retaining vanilla sword", var13);
      }

      try {
         for (int var20 = 0; var20 < B.length; var20++) {
            String var23 = B[var20];

            try (InputStream var26 = jv.class.getResourceAsStream("/assets/larp-client/knives/" + var23 + ".png")) {
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
                  Minecraft.getInstance().getTextureManager().register(n(var20), var4);
               } catch (RuntimeException var11) {
                  var4.close();
                  throw var11;
               }
            }
         }

         r4.g("SwordModels", "Galaxy blade layers loaded: {}", String.join(", ", B));
      } catch (IOException | RuntimeException var19) {
         H.keySet().removeIf(var0 -> !var0.E());
         r4.r("SwordModels", "Cannot load the galaxy blade finish; using vanilla swords for coated knives", var19);
      }

      for (yi var30 : yi.values()) {
         try (InputStream var31 = jv.class.getResourceAsStream("/assets/larp-client/knives/" + var30.h + ".png")) {
            if (var31 == null) {
               throw new IOException("Missing knife texture");
            }

            NativeImage var5 = NativeImage.read(var31);

            DynamicTexture var6;
            try {
               _r var7 = H.get(var30);
               if (var7 != null && !var30.E()) {
                  H.put(var30, var7.shaded(var5.getWidth(), var5.getHeight(), var5::getPixel));
               }

               var6 = new DynamicTexture(() -> "LARP " + var30.h, var5);
            } catch (RuntimeException var15) {
               var5.close();
               throw var15;
            }

            try {
               Minecraft.getInstance().getTextureManager().register(H(var30), var6);
            } catch (RuntimeException var9) {
               var6.close();
               throw var9;
            }
         } catch (IOException | RuntimeException var17) {
            H.remove(var30);
            r4.r("SwordModels", "Cannot load " + var30.h + " texture; using vanilla sword", var17);
         }
      }

      q = (boolean)1;
      if (!H.containsKey(yi.m)) {
         M = null;
      }

      for (Entry var25 : H.entrySet()) {
         if (!((yi)var25.getKey()).E()) {
            int var28 = ((_r)var25.getValue()).parts().stream().mapToInt(var0 -> var0.vertices().length / 8).sum();
            r4.g(
               "SwordModels",
               "{}: {} vertices, native palm fit Y={} Z={}, cached bevel shading",
               ((yi)var25.getKey()).t,
               var28,
               eq.d((yi)var25.getKey()),
               eq.j((yi)var25.getKey())
            );
         }
      }

      r4.V("SwordModels", "Loaded {} sword models and textures", H.size());
   }

   private jv() {
   }
}
