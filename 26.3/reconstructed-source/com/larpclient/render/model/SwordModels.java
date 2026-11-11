package com.larpclient.render.model;

import com.larpclient.util.misc.LarpLog;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

public final class SwordModels {
   private static final String mODEL_PATH = "/assets/larp-client/knives/";
   static final String[] gALAXY_LAYERS = new String[]{"galaxy_clouds", "galaxy_glow", "galaxy_stars"};
   private static final EnumMap<CustomItemModel, KnifeMesh> models = new EnumMap<>(CustomItemModel.class);
   private static boolean loaded;
   private static KnifeMesh slimWatchModel;

   public static Identifier knifeTextureId(CustomItemModel var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "dynamic/knife_" + var0.modelId);
   }

   public static Identifier galaxyLayerTextureId(int var0) {
      return Identifier.fromNamespaceAndPath("larp-client", "dynamic/knife_" + gALAXY_LAYERS[var0]);
   }

   public static KnifeMesh getModel(CustomItemModel var0) {
      return loaded ? models.get(var0) : null;
   }

   public static KnifeMesh getModelForVariant(CustomItemModel var0, boolean var1) {
      return loaded && var0 == CustomItemModel.lUXURY_WATCH && var1 ? slimWatchModel : getModel(var0);
   }

   public static void loadAll() {
      loaded = (boolean)0;
      models.clear();
      slimWatchModel = null;

      for (CustomItemModel var3 : CustomItemModel.values()) {
         try {
            models.put(var3, KnifeMesh.read(SwordModels.class.getResourceAsStream("/assets/larp-client/knives/" + var3.modelId + ".bin")));
         } catch (IOException var14) {
            LarpLog.error("SwordModels", "Cannot load " + var3.modelId + "; retaining vanilla sword", var14);
         }
      }

      try {
         slimWatchModel = KnifeMesh.read(SwordModels.class.getResourceAsStream("/assets/larp-client/knives/dress_watch_slim.bin"))
            .sharing(models.get(CustomItemModel.lUXURY_WATCH), "case");
      } catch (IOException var13) {
         models.remove(CustomItemModel.lUXURY_WATCH);
         LarpLog.error("SwordModels", "Cannot load slim watch; retaining vanilla sword", var13);
      }

      try {
         for (int var20 = 0; var20 < gALAXY_LAYERS.length; var20++) {
            String var23 = gALAXY_LAYERS[var20];

            try (InputStream var26 = SwordModels.class.getResourceAsStream("/assets/larp-client/knives/" + var23 + ".png")) {
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
                  Minecraft.getInstance().getTextureManager().register(galaxyLayerTextureId(var20), var4);
               } catch (RuntimeException var11) {
                  var4.close();
                  throw var11;
               }
            }
         }

         LarpLog.debug("SwordModels", "Galaxy blade layers loaded: {}", String.join(", ", gALAXY_LAYERS));
      } catch (IOException | RuntimeException var19) {
         models.keySet().removeIf(var0 -> !var0.isAccessory());
         LarpLog.error("SwordModels", "Cannot load the galaxy blade finish; using vanilla swords for coated knives", var19);
      }

      for (CustomItemModel var30 : CustomItemModel.values()) {
         try (InputStream var31 = SwordModels.class.getResourceAsStream("/assets/larp-client/knives/" + var30.modelId + ".png")) {
            if (var31 == null) {
               throw new IOException("Missing knife texture");
            }

            NativeImage var5 = NativeImage.read(var31);

            DynamicTexture var6;
            try {
               KnifeMesh var7 = models.get(var30);
               if (var7 != null && !var30.isAccessory()) {
                  models.put(var30, var7.shaded(var5.getWidth(), var5.getHeight(), var5::getPixel));
               }

               var6 = new DynamicTexture(() -> "LARP " + var30.modelId, var5);
            } catch (RuntimeException var15) {
               var5.close();
               throw var15;
            }

            try {
               Minecraft.getInstance().getTextureManager().register(knifeTextureId(var30), var6);
            } catch (RuntimeException var9) {
               var6.close();
               throw var9;
            }
         } catch (IOException | RuntimeException var17) {
            models.remove(var30);
            LarpLog.error("SwordModels", "Cannot load " + var30.modelId + " texture; using vanilla sword", var17);
         }
      }

      loaded = (boolean)1;
      if (!models.containsKey(CustomItemModel.lUXURY_WATCH)) {
         slimWatchModel = null;
      }

      for (Entry var25 : models.entrySet()) {
         if (!((CustomItemModel)var25.getKey()).isAccessory()) {
            int var28 = ((KnifeMesh)var25.getValue()).parts().stream().mapToInt(var0 -> var0.vertices().length / 8).sum();
            LarpLog.debug(
               "SwordModels",
               "{}: {} vertices, native palm fit Y={} Z={}, cached bevel shading",
               ((CustomItemModel)var25.getKey()).displayName,
               var28,
               KnifeHandTransform.palmOffsetY((CustomItemModel)var25.getKey()),
               KnifeHandTransform.palmOffsetZ((CustomItemModel)var25.getKey())
            );
         }
      }

      LarpLog.info("SwordModels", "Loaded {} sword models and textures", models.size());
   }

   private SwordModels() {
   }
}
