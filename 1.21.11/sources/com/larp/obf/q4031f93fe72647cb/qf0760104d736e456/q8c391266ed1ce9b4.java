package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456;

import com.larpclient.b9;
import com.larpclient.cz;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Collection;
import net.minecraft.class_11658;
import net.minecraft.class_11954;
import net.minecraft.class_11973;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2586;
import net.minecraft.class_2636;
import net.minecraft.class_4184;
import net.minecraft.class_638;
import net.minecraft.class_761;
import net.minecraft.class_765;
import net.minecraft.class_824;
import net.minecraft.class_827;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(class_761.class)
public abstract class q8c391266ed1ce9b4 {
   @Shadow
   private class_638 field_4085;
   @Shadow
   @Final
   private class_824 field_27741;

   @WrapMethod(method = "method_74314")
   private void R(class_4184 var1, float var2, class_11658 var3, Operation<Void> var4) {
      var4.call(new Object[]{var1, var2, var3});
      cz var5 = cz.T();
      if (this.field_4085 != null && var5.J()) {
         Collection var6 = var5.c(this.field_4085);
         if (!var6.isEmpty()) {
            class_827 var7 = this.field_27741.method_3550((class_2636)var6.iterator().next());
            if (var7 != null) {
               Collection var8 = var5.O(this.field_4085, var1.method_71156(), var7.method_33893());
               LongOpenHashSet var9 = new LongOpenHashSet();
               var3.field_62646.removeIf(var2x -> var5.z(var2x.field_62673) && !var7.method_3563() && !b9.k().method_23093(new class_238(var2x.field_62673)));

               for (class_11954 var11 : var3.field_62646) {
                  if (var5.z(var11.field_62673)) {
                     var9.add(var11.field_62673.method_10063());
                  }
               }

               int var22 = 0;

               for (class_2586 var12 : var8) {
                  class_2338 var13 = var12.method_11016();
                  if (!var12.method_11015() && this.field_4085.method_22340(var13) && var5.z(var13)) {
                     class_827 var14 = this.field_27741.method_3550(var12);
                     if (var14 != null && var14.method_33892(var12, var1.method_71156()) && (var14.method_3563() || b9.k().method_23093(new class_238(var13)))) {
                        if (var9.contains(var13.method_10063())) {
                           var22++;
                        } else {
                           class_11954 var15 = this.field_27741.method_74348(var12, var2, null);
                           if (var15 != null) {
                              var3.field_62646.add(var15);
                              var22++;
                           }
                        }
                     }
                  }
               }

               var5.Z(var8.size(), var22);

               for (class_11954 var25 : var3.field_62646) {
                  if (var25 instanceof class_11973 var26 && var26.field_62750 != null && var5.z(var25.field_62673)) {
                     int var27 = class_765.method_24186(var25.field_62676);
                     int var28 = class_765.method_24187(var25.field_62676);

                     for (class_2350 var19 : class_2350.values()) {
                        class_2338 var20 = var25.field_62673.method_10093(var19);
                        if (this.field_4085.method_22340(var20)) {
                           int var21 = class_761.method_23794(this.field_4085, var20);
                           var27 = Math.max(var27, class_765.method_24186(var21));
                           var28 = Math.max(var28, class_765.method_24187(var21));
                        }
                     }

                     var25.field_62676 = class_765.method_23687(var27, var28);
                     var26.field_62750.field_61820 = var25.field_62676;
                  }
               }
            }
         }
      }
   }
}
