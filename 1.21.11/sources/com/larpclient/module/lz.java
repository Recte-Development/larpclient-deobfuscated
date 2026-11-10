package com.larpclient.module;

import com.larpclient.LarpClient;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1308;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1531;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_746;

public final class lz {
   private class_1309 h;
   private Integer s;

   public class_1309 C(lz.z var1) {
      class_310 var2 = class_310.method_1551();
      if (var2.field_1724 != null && var2.field_1687 != null) {
         double var3 = var2.field_1724.method_18798().method_1033() * 2.0;
         double var5 = var1.searchRange() + Math.min(3.0, var3);
         if (var1.focus() && this.s != null) {
            if (var2.field_1687.method_8469(this.s) instanceof class_1309 var7
               && var7.method_5805()
               && this.X(var7, var1)
               && z(var7) <= var5
               && X(var7, var1.fov())
               && (var1.ignoreWalls() || var2.field_1724.method_6057(var7))) {
               this.h = var7;
               return this.h;
            }

            this.s = null;
         }

         ArrayList var11 = new ArrayList();

         for (class_1297 var9 : var2.field_1687.method_18112()) {
            if (var9 instanceof class_1309 var10
               && this.X(var10, var1)
               && z(var10) <= var5
               && X(var10, var1.fov())
               && (var1.ignoreWalls() || var2.field_1724.method_6057(var10))) {
               var11.add(var10);
            }
         }

         if (var11.isEmpty()) {
            this.g();
            return null;
         }

         var11.sort(r(var1.sort()));
         this.h = (class_1309)var11.getFirst();
         if (var1.focus()) {
            this.s = this.h.method_5628();
         }

         return this.h;
      } else {
         this.g();
         return null;
      }
   }

   public boolean X(class_1309 var1, lz.z var2) {
      class_310 var3 = class_310.method_1551();
      if (var1 == var3.field_1724) {
         return false;
      }

      if (!var1.method_5805() || var1.method_6032() <= 0.0F || var1.method_31481()) {
         return false;
      }

      if (var1 == var3.field_1724.method_49694()) {
         return false;
      }

      if (var1 instanceof class_1657 var4) {
         if (var4.method_7325()) {
            return false;
         } else if (e(var4)) {
            return false;
         } else if (!var2.players()) {
            return false;
         } else {
            return !var2.invisibles() && var4.method_5767() ? false : var2.naked() || !G(var4);
         }
      } else if (var1 instanceof class_1429) {
         return var2.animals();
      } else {
         return var1 instanceof class_1308 && !(var1 instanceof class_1531) ? var2.mobs() : false;
      }
   }

   private static boolean e(class_1657 var0) {
      MCFriendModule var1 = LarpClient.b().G().b(MCFriendModule.class);
      return var1 != null && var1.M(var0.method_5477().getString());
   }

   private static boolean G(class_1657 var0) {
      for (class_1304 var4 : new class_1304[]{class_1304.field_6169, class_1304.field_6174, class_1304.field_6172, class_1304.field_6166}) {
         if (!var0.method_6118(var4).method_7960()) {
            return false;
         }
      }

      return true;
   }

   private static double z(class_1309 var0) {
      return f.a(var0).method_1020(class_310.method_1551().field_1724.method_33571()).method_1033();
   }

   private static double c(class_1309 var0) {
      class_746 var1 = class_310.method_1551().field_1724;
      class_243 var2 = var1.method_33571();
      class_243 var3 = AuraModule.e();
      class_243 var4 = f.a(var0).method_1020(var2).method_1029();
      return Math.acos(class_3532.method_15350(var3.method_1026(var4), -1.0, 1.0)) * (180.0 / Math.PI);
   }

   private static boolean X(class_1309 var0, double var1) {
      return var1 >= 360.0 || c(var0) <= Math.max(0.0, var1) * 0.5;
   }

   private static Comparator<class_1309> r(String var0) {
      class_746 var1 = class_310.method_1551().field_1724;
      Comparator var2 = Comparator.comparingDouble(var1::method_5858);

      return switch (var0) {
         case "Health" -> Comparator.<class_1309>comparingDouble(var0x -> var0x.method_6032() + var0x.method_6067()).thenComparing(var2);
         case "FOV" -> Comparator.comparingDouble(lz::c).thenComparing(var2);
         case "Armor" -> Comparator.comparingDouble(class_1309::method_6096).thenComparingDouble(var0x -> var0x.method_6032() + var0x.method_6067());
         default -> var2;
      };
   }

   public class_1309 q() {
      return this.h;
   }

   public void g() {
      this.h = null;
      this.s = null;
   }

   public record z(
      double searchRange,
      boolean players,
      boolean mobs,
      boolean animals,
      boolean invisibles,
      boolean naked,
      boolean focus,
      boolean ignoreWalls,
      String sort,
      double fov
   ) {
   }
}
