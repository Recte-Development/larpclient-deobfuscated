package com.larpclient.mixin;

import com.larpclient.LarpClient;
import com.larpclient.config.DisplayAliases;
import com.larpclient.module.render.NametagsModule;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V",
      at = @At("TAIL")
   )
   private void y(Entity var1, EntityRenderState var2, float var3, CallbackInfo var4) {
      if (var1 instanceof Player && var2.nameTag != null) {
         var2.nameTag = DisplayAliases.rewriteForPlayer(var1.getUUID(), ((Player)var1).getGameProfile().name(), var2.nameTag);
      }

      LarpClient var5 = LarpClient.getInstance();
      if (var5 != null) {
         NametagsModule var6 = var5.getModuleManager().getModule(NametagsModule.class);
         if (var6 != null && var6.isEnabled() && var6.isRendered(var1)) {
            var2.nameTag = null;
            var2.scoreText = null;
         }
      }
   }
}
