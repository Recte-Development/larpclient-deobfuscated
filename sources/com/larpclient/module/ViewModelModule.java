package com.larpclient.module;

import com.larpclient.mt;
import com.larpclient.u;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.world.InteractionHand;

public final class ViewModelModule extends Module {
   public static final double M = 0.1;
   public static final double l = 2.0;
   private final ViewModelModule.w J = this.x("Main", "main hand");
   private final ViewModelModule.w d = this.x("Off", "offhand");
   private static ViewModelModule n;

   public ViewModelModule() {
      super("ViewModel", "Resize, move and rotate each hand's item. Drag hands in chat to place them", Category.VISUALS, -1);
      n = this;
   }

   public static ViewModelModule Z() {
      return n;
   }

   public List<df<?>> K() {
      return this.J.settings();
   }

   public List<df<?>> c() {
      return this.d.settings();
   }

   private ViewModelModule.w g(InteractionHand var1) {
      return var1 == InteractionHand.OFF_HAND ? this.d : this.J;
   }

   public void z(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModelModule.w var4 = this.g(var2);
         var1.translate((var3 ? -1 : 1) * var4.x().P(), var4.y().P(), var4.z().P());
      }
   }

   public void f(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModelModule.w var4 = this.g(var2);
         float var5 = var3 ? -1.0F : 1.0F;
         u.v(var1, Axis.XP.rotationDegrees(var4.pitch().P().floatValue()));
         u.v(var1, Axis.YP.rotationDegrees(var5 * var4.yaw().P().floatValue()));
         u.v(var1, Axis.ZP.rotationDegrees(var5 * var4.roll().P().floatValue()));
         float var6 = var4.scale().P().floatValue();
         var1.scale(var6, var6, var6);
      }
   }

   public void I(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         float var4 = (var3 ? 6 : -6) / 16.0F;
         float var5 = 0.75F;
         var1.translate(var4, var5, 0.0F);
         this.f(var1, var2, var3);
         var1.translate(-var4, -var5, 0.0F);
      }
   }

   public double h(InteractionHand var1) {
      return this.g(var1).scale().P();
   }

   public double u(InteractionHand var1) {
      return this.g(var1).x().P();
   }

   public double w(InteractionHand var1) {
      return this.g(var1).y().P();
   }

   public void l(InteractionHand var1, double var2, double var4) {
      ViewModelModule.w var6 = this.g(var1);
      var6.x().o(var2);
      var6.y().o(var4);
   }

   public void I(InteractionHand var1, double var2) {
      ViewModelModule.w var4 = this.g(var1);
      var4.scale().o(var4.scale().P() + var2 * 0.05);
   }

   public void t(InteractionHand var1) {
      ViewModelModule.w var2 = this.g(var1);

      for (d9 var4 : List.of(var2.scale(), var2.x(), var2.y(), var2.z())) {
         var4.B();
      }
   }

   @Override
   public void onEnable() {
      mt.K("ViewModel", "Active: main {}x, offhand {}x", this.J.scale().P(), this.d.scale().P());
   }

   @Override
   public void onDisable() {
      mt.K("ViewModel", "Hands returned to vanilla placement");
   }

   private ViewModelModule.w x(String var1, String var2) {
      return new ViewModelModule.w(
         this.W(var1 + " Scale", "Size", "Size of the " + var2 + " item or arm", 1.0, 0.1, 2.0, 0.05),
         this.W(var1 + " X", "Sideways", "Positive moves the " + var2 + " outward", 0.0, -2.5, 2.5, 0.01),
         this.W(var1 + " Y", "Height", "Vertical offset of the " + var2, 0.0, -2.0, 2.0, 0.01),
         this.W(var1 + " Z", "Depth", "Negative moves the " + var2 + " away from you", 0.0, -2.0, 1.0, 0.01),
         this.W(var1 + " Pitch", "Pitch", "Tilt of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.W(var1 + " Yaw", "Yaw", "Turn of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.W(var1 + " Roll", "Roll", "Roll of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0)
      );
   }

   private d9 W(String var1, String var2, String var3, double var4, double var6, double var8, double var10) {
      d9 var12 = this.addSetting(new d9(var1, var3, var4, var6, var8, var10));
      var12.n(var2);
      return var12;
   }

   private record w(d9 scale, d9 x, d9 y, d9 z, d9 pitch, d9 yaw, d9 roll) {
      List<df<?>> settings() {
         return List.of(this.scale, this.x, this.y, this.z, this.pitch, this.yaw, this.roll);
      }
   }
}
