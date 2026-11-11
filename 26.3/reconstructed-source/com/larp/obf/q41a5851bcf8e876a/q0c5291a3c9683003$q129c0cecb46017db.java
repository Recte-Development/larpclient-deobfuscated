package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.render.entity.LegTrail;
import com.larpclient.social.cosmetic.SkinOverrideAccess;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(AvatarRenderState.class)
public abstract class q0c5291a3c9683003$q129c0cecb46017db implements SkinOverrideAccess {
   @Unique
   private int K;
   @Unique
   private LegTrail d;

   @Override
   public int larp$headless() {
      return this.K;
   }

   @Override
   public LegTrail larp$korblox() {
      return this.d;
   }

   @Override
   public void larp$roblox(int var1, LegTrail var2) {
      this.K = var1;
      this.d = var2;
   }
}
