package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.er;
import java.util.List;
import net.minecraft.class_338;
import net.minecraft.class_303.class_7590;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_338.class)
public abstract class qa0eee7fe1d7f71f1 {
   @Shadow
   private int field_2066;
   @Shadow
   private boolean field_2067;
   @Shadow
   @Final
   private List<class_7590> field_2064;
   @Unique
   private long j = -1L;

   @Shadow
   private void method_44813() {
      throw new AssertionError();
   }

   @Shadow
   public abstract int method_1813();

   @Inject(method = "method_45584", at = @At("HEAD"))
   private void X(CallbackInfo var1) {
      if (this.j != er.M()) {
         this.j = er.M();
         int var2 = this.field_2066;
         boolean var3 = this.field_2067;
         this.method_44813();
         this.field_2066 = Math.max(0, Math.min(var2, this.field_2064.size() - this.method_1813()));
         this.field_2067 = var3;
      }
   }
}
