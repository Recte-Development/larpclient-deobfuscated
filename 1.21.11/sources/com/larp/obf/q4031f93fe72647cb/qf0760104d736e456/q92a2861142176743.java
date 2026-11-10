package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.b1;
import com.larpclient.cz;
import com.larpclient.iy;
import com.larpclient.q8;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_481;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(class_481.class)
public abstract class q92a2861142176743 {
   @Shadow
   private class_1735 field_2889;

   @WrapMethod(method = "method_2383")
   private void Z(class_1735 var1, int var2, int var3, class_1713 var4, Operation<Void> var5) {
      cz var6 = cz.T();
      if (!iy.n()) {
         var5.call(new Object[]{var1, var2, var3, var4});
      } else if (!iy.m()) {
         class_310 var7 = class_310.method_1551();
         class_1735 var8 = iy.a(var1);
         if (var8 != null && !(var8 instanceof iy.n) && var8.field_7871 == var7.field_1724.method_31548()) {
            for (class_1735 var10 : var7.field_1724.field_7498.field_7761) {
               if (var10.field_7871 == var8.field_7871 && var10.method_34266() == var8.method_34266()) {
                  var8 = var10;
                  break;
               }
            }
         }

         boolean var13 = false;
         int var14 = var4 == class_1713.field_7791 && var8 instanceof iy.n && var3 >= 0 && var3 < 9 ? iy.E(var3) : var3;
         if (var1 == this.field_2889 && var4 == class_1713.field_7794) {
            for (class_1735 var12 : var7.field_1724.field_7498.field_7761) {
               if (!var6.n(var7.field_1724.field_7498, var12.field_7874, var3, var4)) {
                  var13 = true;
                  break;
               }
            }
         }

         if (var4 != class_1713.field_7790 || !var6.h(var7.field_1724.field_7498, var8, var3)) {
            if (var13
               || iy.l(var8)
               || var8 != null && var8.field_7871 == var7.field_1724.method_31548() && !var6.n(var7.field_1724.field_7498, var8.field_7874, var14, var4)
               || var4 == class_1713.field_7793 && var6.H(var7.field_1724.field_7498.method_34255())) {
               q8.v(
                  "FakeSpawner",
                  "Creative input blocked type={} nativeSlot={} physical={} clearReserved={}",
                  var4,
                  var8 == null ? -1 : var8.field_7874,
                  var8 == null ? -1 : var8.method_34266(),
                  var13
               );
               b1.q(class_2561.method_43470("This slot is reserved or awaiting real items. Move an available stack instead."), true);
            } else if (var4 == class_1713.field_7791 && var3 >= 0 && var3 < 9 && var8 instanceof iy.n) {
               iy.i(var7.field_1724.field_7498, var8, var3);
            } else {
               iy.U(var8);
               var6.u(() -> var5.call(new Object[]{var1, var2, var3, var4}));
            }
         }
      }
   }
}
