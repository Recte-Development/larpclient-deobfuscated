package com.larpclient.module;

import com.larpclient.s2;
import com.larpclient.tp;
import com.larpclient.yq;
import java.util.List;
import net.minecraft.class_11908;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_437;

public final class as extends class_437 implements tp {
   private static final int H = 11;
   private int A;

   private as() {
      super(class_2561.method_43470("Casino log"));
   }

   public static void O() {
      class_310.method_1551().method_1507(new as());
   }

   public static void t() {
      class_310 var0 = class_310.method_1551();
      if (var0.field_1755 == null) {
         var0.method_1507(new as());
      }
   }

   public boolean method_25421() {
      return false;
   }

   public boolean method_25401(double var1, double var3, double var5, double var7) {
      this.A = Math.max(0, this.A - (int)Math.signum(var7) * 3);
      return true;
   }

   public boolean method_25404(class_11908 var1) {
      if (s2.i(var1) == 67) {
         yq.Q();
         this.A = 0;
         return true;
      } else {
         return super.method_25404(var1);
      }
   }

   public void method_25394(class_332 var1, int var2, int var3, float var4) {
      super.method_25394(var1, var2, var3, var4);
      int var5 = Math.max(1, Math.min(520, this.field_22789 - 24));
      int var6 = Math.max(1, Math.min(300, this.field_22790 - 24));
      int var7 = (this.field_22789 - var5) / 2;
      int var8 = (this.field_22790 - var6) / 2;
      var1.method_25294(var7, var8, var7 + var5, var8 + var6, -804253672);
      var1.method_25303(this.field_22793, "Casino log   Scroll to move, C clears, Esc closes", var7 + 8, var8 + 6, -1513232);
      List var9 = yq.q();
      int var10 = Math.max(1, (var6 - 28) / 11);
      int var11 = Math.max(0, var9.size() - this.A);
      int var12 = Math.max(0, var11 - var10);
      int var13 = var8 + 22;

      for (int var14 = var12; var14 < var11; var14++) {
         String var15 = this.field_22793.method_27523((String)var9.get(var14), var5 - 16);
         var1.method_25303(this.field_22793, var15, var7 + 8, var13, -3618600);
         var13 += 11;
      }

      if (var9.isEmpty()) {
         var1.method_25303(this.field_22793, "No game events yet", var7 + 8, var13, -7829352);
      }
   }
}
