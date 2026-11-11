package com.larpclient.social.cosmetic;

import com.google.gson.JsonObject;
import com.larpclient.render.entity.AppearanceTextures;
import com.mojang.blaze3d.platform.NativeImage;
import java.util.UUID;
import net.minecraft.resources.Identifier;

public final class LocalPlayerSkin {
   public static LocalPlayerSkin.SkinData current;

   public static LocalPlayerSkin.SkinData getForPlayer(UUID var0, UUID var1) {
      return var1.equals(var0) ? current : null;
   }

   public static LocalPlayerSkin.SkinData create(String var0, PlayerAppearance var1, NativeImage var2, NativeImage var3) {
      return new LocalPlayerSkin.SkinData(
         AppearanceTextures.registerTexture(var0 + "/skin", var2),
         AppearanceTextures.registerTexture(var0 + "/cape", var3),
         var1.slim(),
         var1.hideCape(),
         var1.name(),
         var1.cosmetics()
      );
   }

   public static void clear() {
      if (current != null) {
         current.close();
      }

      current = null;
   }

   private LocalPlayerSkin() {
   }

   public record SkinData(Identifier skin, Identifier cape, boolean slim, boolean hideCape, String name, JsonObject cosmetics) {
      void close() {
         AppearanceTextures.releaseTexture(this.skin);
         AppearanceTextures.releaseTexture(this.cape);
      }
   }
}
