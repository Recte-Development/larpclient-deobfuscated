package com.larpclient;

import java.util.function.Predicate;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_746;

public final class wv {
   private static final b_ d = new b_();

   public static void D(final b_.s var0, int var1, final Predicate<class_1799> var2) {
      final class_310 var3 = class_310.method_1551();
      final class_746 var4 = var3.field_1724;
      if (var4 == null) {
         d.P(var0);
      } else {
         d.A(
            var0,
            new b_.p() {
               @Override
               public Object player() {
                  return var4;
               }

               @Override
               public int tick() {
                  return var4.field_6012;
               }

               @Override
               public boolean current() {
                  return var3.field_1724 == var4 && var3.field_1687 == var4.method_73183() && var4.method_5805();
               }

               @Override
               public boolean allowed() {
                  return this.current() && !var4.method_7325() && !wp.d();
               }

               @Override
               public int selected() {
                  return var4.method_31548().field_7545;
               }

               @Override
               public void select(int var1) {
                  var4.method_31548().field_7545 = var1;
               }

               @Override
               public boolean usable(int var1) {
                  class_1799 var2x = var4.method_31548().method_5438(var1);
                  return !var2x.method_7960() && var2.test(var2x);
               }

               @Override
               public boolean usingItem() {
                  return var4.method_6115();
               }

               @Override
               public boolean manualUse() {
                  return wv.n() == null && j.W(var3.field_1690.field_1904);
               }

               @Override
               public void holdUse(boolean var1) {
                  var3.field_1690.field_1904.method_23481(var1);
               }

               @Override
               public void releaseUse() {
                  int var1x = var3.field_1724 != null && this.manualUse() ? 1 : 0;
                  var3.field_1690.field_1904.method_23481((boolean)var1x);
                  if (var3.field_1724 == var4 && var3.field_1761 != null && var4.method_6115() && var1x == 0) {
                     var3.field_1761.method_2897(var4);
                  }
               }

               @Override
               public boolean startUnderScreen() {
                  class_437 var1x = wv.n();
                  return var1x != null && var3.field_1761 != null && !var3.field_1761.method_2923() && !var4.method_3144() && var4.field_3944.method_76760()
                     ? cr.l(() -> {
                        class_1269 var4x = var3.field_1761.method_2919(var4, class_1268.field_5808);
                        q8.G("Eating", "Started {} from slot {} with {} open: {}", var0, var4.method_31548().field_7545, var1.getClass().getSimpleName(), var4x);
                     })
                     : false;
               }
            },
            var1
         );
      }
   }

   private static class_437 n() {
      return class_310.method_1551().field_1755;
   }

   public static void P(b_.s var0) {
      d.P(var0);
   }

   public static void K() {
      d.r();
   }

   private wv() {
   }
}
