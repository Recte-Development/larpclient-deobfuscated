package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.ix;
import com.larpclient.jr;
import com.larpclient.lb;
import com.larpclient.q3;
import com.larpclient.qx;
import com.larpclient.module.Feature3DUIModule;
import com.larpclient.module.GUIAnimationModule;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_437.class)
public abstract class q65154d6067563ba6 implements q3, qx {
   @Shadow
   public int field_22789;
   @Shadow
   public int field_22790;
   @Unique
   private long K;
   @Unique
   private float R = 1.0F;
   @Unique
   private boolean y;
   @Unique
   private boolean S;
   @Unique
   private jr Q;
   @Unique
   private boolean T;

   @Override
   public void larpOpenMenuPopup() {
      this.Q = new jr();
   }

   @Override
   public jr larpMenuPopup() {
      return this.Q;
   }

   @Override
   public boolean larpDrawingMenuPopup() {
      return this.T;
   }

   @Inject(method = "method_25423(II)V", at = @At("TAIL"))
   private void D(int var1, int var2, CallbackInfo var3) {
      this.K = System.nanoTime();
      this.R = 1.0F;
   }

   @Override
   public float larpContainerScale() {
      return this.R;
   }

   @WrapMethod(method = "method_47413")
   private void S(class_332 var1, int var2, int var3, float var4, Operation<Void> var5) {
      class_310 var6 = class_310.method_1551();
      if (this.Q != null && var6.field_1687 == null) {
         ix.g var7 = this.Q.U(System.nanoTime(), var6.method_18506() == null && var6.method_1569(), qx.x());
         if (!this.Q.m()) {
            var1.method_51448().pushMatrix();
            var1.method_51448().mul(var7.matrix(this.field_22789 / 2.0, this.field_22790 / 2.0));
            this.T = true;

            try {
               var5.call(
                  new Object[]{
                     var1, (int)Math.round(var7.localX(var2, this.field_22789 / 2.0)), (int)Math.round(var7.localY(var3, this.field_22790 / 2.0)), var4
                  }
               );
            } finally {
               this.T = false;
               var1.method_51448().popMatrix();
            }

            return;
         }
      }

      lb.z var16 = Feature3DUIModule.q((class_437)this);
      if (var16 == null) {
         this.j(var1, var2, var3, var4, var5);
      } else {
         var1.method_71048();
         Feature3DUIModule.E(var1, this.field_22789, this.field_22790);
         class_310.method_1551().field_1705.method_72738();
         double[] var8 = var16.inverse(var2, var3);

         try {
            lb.y(var1, var16, true, () -> this.j(var1, (int)Math.floor(var8[0]), (int)Math.floor(var8[1]), var4, var5));
         } finally {
            var1.method_71048();
            lb.w(this.R, this.field_22789 / 2.0, this.field_22790 / 2.0);
         }
      }
   }

   @Unique
   private void j(class_332 var1, int var2, int var3, float var4, Operation<Void> var5) {
      GUIAnimationModule var6 = GUIAnimationModule.Z();
      this.y = this instanceof class_465 && var6 != null && var6.w();
      this.R = this.y ? var6.s(this.K) : 1.0F;
      if (!this.y) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else {
         var1.method_51448().pushMatrix();
         this.b(var1, this.R);
         int var7 = (int)Math.round(var6.Q(var2, this.field_22789 / 2.0, this.R));
         int var8 = (int)Math.round(var6.Q(var3, this.field_22790 / 2.0, this.R));

         try {
            var5.call(new Object[]{var1, var7, var8, var4});
         } finally {
            var1.method_51448().popMatrix();
            this.y = false;
            this.S = false;
         }
      }
   }

   @Unique
   private void b(class_332 var1, float var2) {
      var1.method_51448().translate(this.field_22789 / 2.0F, this.field_22790 / 2.0F);
      var1.method_51448().scale(var2, var2);
      var1.method_51448().translate(-this.field_22789 / 2.0F, -this.field_22790 / 2.0F);
   }

   @Inject(method = "method_52752", at = @At("HEAD"), cancellable = true)
   private void E(class_332 var1, CallbackInfo var2) {
      if (lb.X(this) != null) {
         var2.cancel();
      }
   }

   @Inject(method = "method_25420", at = @At("HEAD"))
   private void Z(class_332 var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.S = this.y;
      if (this.S) {
         var1.method_51448().pushMatrix();
         this.b(var1, 1.0F / this.R);
      }
   }

   @Inject(method = "method_25420", at = @At("RETURN"))
   private void G(class_332 var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (this.S) {
         var1.method_51448().popMatrix();
         this.S = false;
      }
   }
}
