package com.larpclient;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

public final class zs {
   private static final String A = "/assets/larp-client/knives/";
   static final String[] h = new String[]{"galaxy_clouds", "galaxy_glow", "galaxy_stars"};
   private static final EnumMap<z5, c1> c = new EnumMap<>(z5.class);
   private static boolean p;
   private static c1 Z;

   public static Identifier j(z5 var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "dynamic/knife_" + var0.U);
   }

   public static Identifier y(int var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "dynamic/knife_" + h[var0]);
   }

   public static c1 U(z5 var0) {
      return p ? c.get(var0) : null;
   }

   public static c1 U(z5 var0, boolean var1) {
      return p && var0 == z5.r && var1 ? Z : U(var0);
   }

   public static void Q() {
      p = (boolean)0;
      c.clear();
      Z = null;

      for (z5 var3 : z5.values()) {
         try {
            c.put(var3, c1.read(zs.class.getResourceAsStream("/assets/larp-client/knives/" + var3.U + ".bin")));
         } catch (IOException var14) {
            mt.P("SwordModels", "Cannot load " + var3.U + "; retaining vanilla sword", var14);
         }
      }

      try {
         Z = c1.read(zs.class.getResourceAsStream("/assets/larp-client/knives/dress_watch_slim.bin")).sharing(c.get(z5.r), "case");
      } catch (IOException var13) {
         c.remove(z5.r);
         mt.P("SwordModels", "Cannot load slim watch; retaining vanilla sword", var13);
      }

      try {
         for (int var20 = 0; var20 < h.length; var20++) {
            String var23 = h[var20];

            try (InputStream var26 = zs.class.getResourceAsStream("/assets/larp-client/knives/" + var23 + ".png")) {
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
                  Minecraft.getInstance().getTextureManager().register(y(var20), var4);
               } catch (RuntimeException var11) {
                  var4.close();
                  throw var11;
               }
            }
         }

         mt.U("SwordModels", "Galaxy blade layers loaded: {}", String.join(", ", h));
      } catch (IOException | RuntimeException var19) {
         c.keySet().removeIf(var0 -> !var0.o());
         mt.P("SwordModels", "Cannot load the galaxy blade finish; using vanilla swords for coated knives", var19);
      }

      for (z5 var30 : z5.values()) {
         try (InputStream var31 = zs.class.getResourceAsStream("/assets/larp-client/knives/" + var30.U + ".png")) {
            if (var31 == null) {
               throw new IOException("Missing knife texture");
            }

            NativeImage var5 = NativeImage.read(var31);

            DynamicTexture var6;
            try {
               c1 var7 = c.get(var30);
               if (var7 != null && !var30.o()) {
                  c.put(var30, var7.shaded(var5.getWidth(), var5.getHeight(), var5::getPixel));
               }

               var6 = new DynamicTexture(() -> "LARP " + var30.U, var5);
            } catch (RuntimeException var15) {
               var5.close();
               throw var15;
            }

            try {
               Minecraft.getInstance().getTextureManager().register(j(var30), var6);
            } catch (RuntimeException var9) {
               var6.close();
               throw var9;
            }
         } catch (IOException | RuntimeException var17) {
            c.remove(var30);
            mt.P("SwordModels", "Cannot load " + var30.U + " texture; using vanilla sword", var17);
         }
      }

      p = (boolean)1;
      if (!c.containsKey(z5.r)) {
         Z = null;
      }

      for (Entry var25 : c.entrySet()) {
         if (!((z5)var25.getKey()).o()) {
            int var28 = ((c1)var25.getValue()).parts().stream().mapToInt(var0 -> var0.vertices().length / 8).sum();
            mt.U(
               "SwordModels",
               "{}: {} vertices, native palm fit Y={} Z={}, cached bevel shading",
               ((z5)var25.getKey()).g,
               var28,
               cb.c((z5)var25.getKey()),
               cb.v((z5)var25.getKey())
            );
         }
      }

      mt.K("SwordModels", "Loaded {} sword models and textures", c.size());
   }

   private zs() {
   }
}
