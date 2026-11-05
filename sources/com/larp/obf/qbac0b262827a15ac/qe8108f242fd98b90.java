package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.LarpClient;
import com.larpclient._r;
import com.larpclient.module.NametagsModule;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public class qe8108f242fd98b90 {
   @Inject(
      method = "extractRenderState(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/entity/state/EntityRenderState;F)V",
      at = @At("TAIL")
   )
   private void z(Entity var1, EntityRenderState var2, float var3, CallbackInfo var4) {
      if (var1 instanceof Player && var2.nameTag != null) {
         var2.nameTag = _r.L(var1.getUUID(), ((Player)var1).getGameProfile().name(), var2.nameTag);
      }

      LarpClient var5 = LarpClient.F();
      if (var5 != null) {
         NametagsModule var6 = var5.x().V(NametagsModule.class);
         if (var6 != null && var6.y(var1)) {
            var2.nameTag = null;
            var2.scoreText = null;
         }
      }
   }
}
