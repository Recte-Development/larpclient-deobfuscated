package com.larpclient;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

public final class fb {
   private static final String I = "/assets/larp-client/knives/";
   static final String[] k = new String[]{"galaxy_clouds", "galaxy_glow", "galaxy_stars"};
   private static final EnumMap<s_, w9> B = new EnumMap<>(s_.class);
   private static boolean u;
   private static w9 O;

   public static Identifier D(s_ var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "dynamic/knife_" + var0.p);
   }

   public static Identifier B(int var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "dynamic/knife_" + k[var0]);
   }

   public static w9 m(s_ var0) {
      return u ? B.get(var0) : null;
   }

   public static w9 T(s_ var0, boolean var1) {
      return u && var0 == s_.r && var1 ? O : m(var0);
   }

   public static void L() {
      u = (boolean)0;
      B.clear();
      O = null;

      for (s_ var3 : s_.values()) {
         try {
            B.put(var3, w9.read(fb.class.getResourceAsStream("/assets/larp-client/knives/" + var3.p + ".bin")));
         } catch (IOException var14) {
            _q.o("SwordModels", "Cannot load " + var3.p + "; retaining vanilla sword", var14);
         }
      }

      try {
         O = w9.read(fb.class.getResourceAsStream("/assets/larp-client/knives/dress_watch_slim.bin")).sharing(B.get(s_.r), "case");
      } catch (IOException var13) {
         B.remove(s_.r);
         _q.o("SwordModels", "Cannot load slim watch; retaining vanilla sword", var13);
      }

      try {
         for (int var20 = 0; var20 < k.length; var20++) {
            String var23 = k[var20];

            try (InputStream var26 = fb.class.getResourceAsStream("/assets/larp-client/knives/" + var23 + ".png")) {
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
                  Minecraft.getInstance().getTextureManager().register(B(var20), var4);
               } catch (RuntimeException var11) {
                  var4.close();
                  throw var11;
               }
            }
         }

         _q.c("SwordModels", "Galaxy blade layers loaded: {}", String.join(", ", k));
      } catch (IOException | RuntimeException var19) {
         B.keySet().removeIf(var0 -> !var0.d());
         _q.o("SwordModels", "Cannot load the galaxy blade finish; using vanilla swords for coated knives", var19);
      }

      for (s_ var30 : s_.values()) {
         try (InputStream var31 = fb.class.getResourceAsStream("/assets/larp-client/knives/" + var30.p + ".png")) {
            if (var31 == null) {
               throw new IOException("Missing knife texture");
            }

            NativeImage var5 = NativeImage.read(var31);

            DynamicTexture var6;
            try {
               w9 var7 = B.get(var30);
               if (var7 != null && !var30.d()) {
                  B.put(var30, var7.shaded(var5.getWidth(), var5.getHeight(), var5::getPixel));
               }

               var6 = new DynamicTexture(() -> "LARP " + var30.p, var5);
            } catch (RuntimeException var15) {
               var5.close();
               throw var15;
            }

            try {
               Minecraft.getInstance().getTextureManager().register(D(var30), var6);
            } catch (RuntimeException var9) {
               var6.close();
               throw var9;
            }
         } catch (IOException | RuntimeException var17) {
            B.remove(var30);
            _q.o("SwordModels", "Cannot load " + var30.p + " texture; using vanilla sword", var17);
         }
      }

      u = (boolean)1;
      if (!B.containsKey(s_.r)) {
         O = null;
      }

      for (Entry var25 : B.entrySet()) {
         if (!((s_)var25.getKey()).d()) {
            int var28 = ((w9)var25.getValue()).parts().stream().mapToInt(var0 -> var0.vertices().length / 8).sum();
            _q.c(
               "SwordModels",
               "{}: {} vertices, native palm fit Y={} Z={}, cached bevel shading",
               ((s_)var25.getKey()).N,
               var28,
               f_.j((s_)var25.getKey()),
               f_.h((s_)var25.getKey())
            );
         }
      }

      _q.z("SwordModels", "Loaded {} sword models and textures", B.size());
   }

   private fb() {
   }
}
