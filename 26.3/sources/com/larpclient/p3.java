package com.larpclient;

import com.larpclient.module.LARPModule;
import com.larpclient.module.NameChangerModule;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.core.ClientAsset.ResourceTexture;
import net.minecraft.core.ClientAsset.Texture;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;

public final class p3 {
   public static ny.p p(UUID var0) {
      LARPModule var1 = LARPModule.I();
      return var1 != null && var1.isOperational() ? ny.v(var0, Minecraft.getInstance().getUser().getProfileId()) : null;
   }

   public static PlayerSkin n(UUID var0, PlayerSkin var1) {
      ny.p var2 = p(var0);
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

   public static String J(UUID var0, String var1) {
      String var2 = Z(var0);
      return var2.isEmpty() ? var1 : var2;
   }

   public static String Z(UUID var0) {
      NameChangerModule var1 = NameChangerModule.B();
      String var2 = var1 == null ? "" : var1.v();
      if (!var2.isEmpty()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var0.equals(var3.getUser().getProfileId()) || var3.player != null && var0.equals(var3.player.getUUID())) {
            return var2;
         }
      }

      ny.p var4 = p(var0);
      return var4 == null ? "" : var4.name();
   }

   public static boolean d() {
      LARPModule var0 = LARPModule.I();
      NameChangerModule var1 = NameChangerModule.B();
      return var0 != null && var0.isOperational() || var1 != null && var1.isOperational();
   }
}
