package com.larpclient;

import com.larpclient.module.LARPModule;
import com.larpclient.module.NameChangerModule;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.core.ClientAsset.ResourceTexture;
import net.minecraft.core.ClientAsset.Texture;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

public final class x2 {
   public static y_.w D(UUID var0) {
      LARPModule var1 = LARPModule.C();
      return var1 != null && var1.isOperational() ? y_.T(var0, Minecraft.getInstance().getUser().getProfileId()) : null;
   }

   public static PlayerSkin X(UUID var0, PlayerSkin var1) {
      y_.w var2 = D(var0);
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

   public static String b(UUID var0, String var1) {
      String var2 = W(var0);
      return var2.isEmpty() ? var1 : var2;
   }

   public static String W(UUID var0) {
      NameChangerModule var1 = NameChangerModule.B();
      String var2 = var1 == null ? "" : var1.T();
      if (!var2.isEmpty()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var0.equals(var3.getUser().getProfileId()) || var3.player != null && var0.equals(var3.player.getUUID())) {
            return var2;
         }
      }

      y_.w var4 = D(var0);
      return var4 == null ? "" : var4.name();
   }

   public static boolean F() {
      LARPModule var0 = LARPModule.C();
      NameChangerModule var1 = NameChangerModule.B();
      return var0 != null && var0.isOperational() || var1 != null && var1.isOperational();
   }
}
