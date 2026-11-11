package com.larpclient.social.cosmetic;

import com.larpclient.module.misc.LARPModule;
import com.larpclient.module.misc.NameChangerModule;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.core.ClientAsset.ResourceTexture;
import net.minecraft.core.ClientAsset.Texture;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

public final class CosmeticOverrides {
   public static LocalPlayerSkin.SkinData getOverride(UUID var0) {
      LARPModule var1 = LARPModule.getInstance();
      return var1 != null && var1.isOperational() ? LocalPlayerSkin.getForPlayer(var0, Minecraft.getInstance().getUser().getProfileId()) : null;
   }

   public static PlayerSkin applySkin(UUID var0, PlayerSkin var1) {
      LocalPlayerSkin.SkinData var2 = getOverride(var0);
      return var2 == null
         ? var1
         : new PlayerSkin(
            (Texture)(var2.skin() == null ? var1.body() : new ResourceTexture(var2.skin(), var2.skin())),
            (Texture)(var2.hideCape() ? null : (var2.cape() == null ? var1.cape() : new ResourceTexture(var2.cape(), var2.cape()))),
            (Texture)(var2.hideCape() ? null : (var2.cape() == null ? var1.elytra() : new ResourceTexture(var2.cape(), var2.cape()))),
            var2.skin() == null ? var1.model() : (var2.slim() ? PlayerModelType.SLIM : PlayerModelType.WIDE),
            var1.secure()
         );
   }

   public static String nameOrDefault(UUID var0, String var1) {
      String var2 = getName(var0);
      return var2.isEmpty() ? var1 : var2;
   }

   public static String getName(UUID var0) {
      NameChangerModule var1 = NameChangerModule.getInstance();
      String var2 = var1 == null ? "" : var1.getCustomName();
      if (!var2.isEmpty()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var0.equals(var3.getUser().getProfileId()) || var3.player != null && var0.equals(var3.player.getUUID())) {
            return var2;
         }
      }

      LocalPlayerSkin.SkinData var4 = getOverride(var0);
      return var4 == null ? "" : var4.name();
   }

   public static boolean isActive() {
      LARPModule var0 = LARPModule.getInstance();
      NameChangerModule var1 = NameChangerModule.getInstance();
      return var0 != null && var0.isOperational() || var1 != null && var1.isOperational();
   }
}
