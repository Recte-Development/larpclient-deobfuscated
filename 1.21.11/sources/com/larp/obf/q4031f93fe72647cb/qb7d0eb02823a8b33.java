package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.q8;
import com.larpclient.y7;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import java.util.function.Supplier;
import net.minecraft.class_1044;
import net.minecraft.class_380;
import net.minecraft.class_8538;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_380.class)
public abstract class qb7d0eb02823a8b33 extends class_1044 {
   @Inject(method = "<init>", at = @At("RETURN"))
   private void a(Supplier<String> var1, class_8538 var2, boolean var3, CallbackInfo var4) {
      String var5 = (String)var1.get();
      if (y7.g(this.field_56974, var5, var3)) {
         this.field_63613 = RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR);
         q8.v("Font", "Linear UI glyph sampling selected for {}", var5);
      }
   }
}
