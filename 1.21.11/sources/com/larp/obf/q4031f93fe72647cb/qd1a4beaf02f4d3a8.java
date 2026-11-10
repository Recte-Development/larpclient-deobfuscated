package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.lq;
import com.larpclient.wp;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReferenceArray;
import net.minecraft.class_1923;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2806;
import net.minecraft.class_2818;
import net.minecraft.class_310;
import net.minecraft.class_4076;
import net.minecraft.class_4184;
import net.minecraft.class_5539;
import net.minecraft.class_638;
import net.minecraft.class_769;
import net.minecraft.class_8679;
import net.minecraft.class_846.class_851;
import net.minecraft.class_8679.class_762;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_8679.class)
public abstract class qd1a4beaf02f4d3a8 {
   @Shadow
   private class_769 field_45623;
   @Unique
   private boolean T;
   @Unique
   private volatile LongOpenHashSet r;
   @Unique
   private volatile class_2338 G;

   @Shadow
   public abstract void method_52817();

   @Inject(method = "method_52834", at = @At("HEAD"))
   private void Q(CallbackInfo var1) {
      boolean var2 = wp.d();
      if (var2 != this.T) {
         this.method_52817();
      }

      this.T = var2;
   }

   @Inject(method = "method_52833", at = @At("HEAD"))
   private void N(boolean var1, class_4184 var2, class_243 var3, LongOpenHashSet var4, CallbackInfo var5) {
      LongOpenHashSet var6 = null;
      class_638 var7 = class_310.method_1551().field_1687;
      if (this.T && var7 != null) {
         var6 = new LongOpenHashSet();
         AtomicReferenceArray var8 = var7.method_2935().field_16246.field_16251;

         for (int var9 = 0; var9 < var8.length(); var9++) {
            class_2818 var10 = (class_2818)var8.get(var9);
            if (var10 != null) {
               class_1923 var11 = var10.method_12004();
               if (var7.method_2935().method_2857(var11.field_9181, var11.field_9180, class_2806.field_12803, false) == var10) {
                  var6.add(var11.method_8324());
               }
            }
         }
      }

      this.G = var2.method_19328().method_10062();
      this.r = var6;
   }

   @Inject(method = "method_52821", at = @At("HEAD"), cancellable = true)
   private void J(class_4184 var1, Queue<class_762> var2, CallbackInfo var3) {
      LongOpenHashSet var4 = this.r;
      if (var4 != null && this.field_45623 != null && this.G != null) {
         class_5539 var5 = this.field_45623.method_52840();
         lq.w(var4, this.G, this.field_45623.method_52839(), var5.method_32891(), var5.method_31597(), var2x -> {
            class_851 var4x = this.field_45623.method_3323(class_4076.method_18677(var2x).method_19767());
            if (var4x != null) {
               var2.add(q50c98a508c767a5d.larpCreateNode(var4x, null, 0));
            }
         });
         var3.cancel();
      }
   }

   @Inject(method = "method_52819", at = @At("TAIL"))
   private void o(CallbackInfo var1) {
      if (this.T) {
         this.method_52817();
      }
   }

   @Inject(method = "method_52826", at = @At("TAIL"))
   private void U(class_769 var1, CallbackInfo var2) {
      this.r = null;
      this.G = null;
      this.T = false;
   }
}
