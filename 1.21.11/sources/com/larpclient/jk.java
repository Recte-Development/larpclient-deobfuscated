package com.larpclient;

import net.minecraft.class_10034;
import net.minecraft.class_10055;
import net.minecraft.class_12249;
import net.minecraft.class_4587;
import net.minecraft.class_572;
import net.minecraft.class_591;
import net.minecraft.class_630;
import net.minecraft.class_7833;

public final class jk extends class_591 {
   private final class_572<class_10034> Z;
   private final class_630 q;
   private final class_630 G;
   private final class_630 B;
   private final class_630 o;
   private final class_630 M;
   private final class_630 U;
   private final class_10055 k = new class_10055();
   private gm.o R;
   private float g;
   private float O;
   private float t;

   public jk(class_630 var1) {
      this(var1, false);
   }

   public jk(class_630 var1, boolean var2) {
      super(var1, var2);
      this.Z = new class_572(var1, class_12249::method_76000);
      this.q = var1.method_32086("head");
      this.G = var1.method_32086("body");
      this.B = var1.method_32086("right_arm");
      this.o = var1.method_32086("left_arm");
      this.M = var1.method_32086("right_leg");
      this.U = var1.method_32086("left_leg");
   }

   public void W(yx.k var1) {
      this.o(var1, null);
   }

   public void o(yx.k var1, yx.h var2) {
      this.k.field_53328 = var1.ageInTicks();
      this.k.field_53450 = var1.walkPosition();
      this.k.field_53451 = var1.walkSpeed();
      this.k.field_53447 = (float)Math.toDegrees(var1.headYaw());
      this.k.field_53448 = (float)Math.toDegrees(var1.headPitch());
      this.k.field_53410 = (boolean)(var2 != null && var2.crouch() ? 1 : 0);
      gm.o(this.k, var1);
      this.Z.method_17087(this.k);
      if (var2 != null && !(var2.weight() <= 0.0F)) {
         float var3 = var2.weight();
         m(this.B, var2.rightArmX(), var2.rightArmZ(), var3);
         m(this.o, var2.leftArmX(), var2.leftArmZ(), var3);
         m(this.M, var2.rightLegX(), var2.rightLegZ(), var3);
         m(this.U, var2.leftLegX(), var2.leftLegZ(), var3);
         this.q.field_3674 = this.q.field_3674 + var2.headRoll() * var3;
      }
   }

   private static void m(class_630 var0, float var1, float var2, float var3) {
      var0.field_3654 = var0.field_3654 + (var1 - var0.field_3654) * var3;
      var0.field_3674 = var0.field_3674 + (var2 - var0.field_3674) * var3;
   }

   public void C(float var1, float var2, float var3) {
      this.g = var1;
      this.O = var2;
      this.t = var3;
   }

   public void A(class_4587 var1) {
      if (this.g != 0.0F || this.O != 0.0F || this.t != 0.0F) {
         var1.method_46416(this.O, this.t, 0.0F);
         var1.method_49278(class_7833.field_40718.rotation(this.g), 0.0F, -1.251F, 0.0F);
      }
   }

   class_10055 P() {
      return this.k;
   }

   class_630 l() {
      return this.q;
   }

   class_630 v() {
      return this.G;
   }

   class_630 t() {
      return this.M;
   }

   class_630 b() {
      return this.U;
   }

   void x(gm.o var1) {
      this.R = var1;
      yv var2 = var1 == null ? null : var1.korblox();
      this.q.field_3665 = (boolean)(var1 != null && var1.headless() ? 0 : 1);
      this.M.field_3665 = (boolean)(var2 != null && var2.right() ? 0 : 1);
      this.U.field_3665 = (boolean)(var2 != null && var2.left() ? 0 : 1);
   }

   gm.o l() {
      return this.R;
   }
}
