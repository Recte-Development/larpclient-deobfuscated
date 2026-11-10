package com.larpclient.module;

import com.larpclient.uf;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.world.InteractionHand;

public final class ViewModelModule extends Module {
   public static final double V = 0.1;
   public static final double M = 2.0;
   private static final int C = 2;
   private final ViewModelModule.s X = this.n("Main", "main hand");
   private final ViewModelModule.s e = this.n("Off", "offhand");
   private final e E = this.B("Swing Speed", "Speed", "How fast your hits animate. 1 is normal, lower is slower, higher is faster", 1.0, 0.25, 3.0, 0.25);
   private static ViewModelModule J;

   public ViewModelModule() {
      super("ViewModel", "Resize, move and rotate each hand's item and change swing speed. Drag hands in chat to place them", Category.VISUALS, -1);
      J = this;
   }

   public static ViewModelModule b() {
      return J;
   }

   public List<q<?>> i() {
      return List.of(this.E);
   }

   public List<q<?>> X() {
      return this.X.settings();
   }

   public List<q<?>> u() {
      return this.e.settings();
   }

   public int U(int var1) {
      if (this.isEnabled()) {
         double var2 = this.E.getValue();
         return var2 == 1.0 ? var1 : Math.max(2, (int)Math.round(var1 / var2));
      } else {
         return var1;
      }
   }

   private ViewModelModule.s D(InteractionHand var1) {
      return var1 == InteractionHand.OFF_HAND ? this.e : this.X;
   }

   public void t(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModelModule.s var4 = this.D(var2);
         var1.translate((var3 ? -1 : 1) * var4.x().getValue(), var4.y().getValue(), var4.z().getValue());
      }
   }

   public void O(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModelModule.s var4 = this.D(var2);
         float var5 = var3 ? -1.0F : 1.0F;
         uf.q(var1, Axis.XP.rotationDegrees(var4.pitch().getValue().floatValue()));
         uf.q(var1, Axis.YP.rotationDegrees(var5 * var4.yaw().getValue().floatValue()));
         uf.q(var1, Axis.ZP.rotationDegrees(var5 * var4.roll().getValue().floatValue()));
         float var6 = var4.scale().getValue().floatValue();
         var1.scale(var6, var6, var6);
      }
   }

   public void b(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         float var4 = (var3 ? 6 : -6) / 16.0F;
         float var5 = 0.75F;
         var1.translate(var4, var5, 0.0F);
         this.O(var1, var2, var3);
         var1.translate(-var4, -var5, 0.0F);
      }
   }

   public double F(InteractionHand var1) {
      return this.D(var1).scale().getValue();
   }

   public double Q(InteractionHand var1) {
      return this.D(var1).x().getValue();
   }

   public double h(InteractionHand var1) {
      return this.D(var1).y().getValue();
   }

   public void c(InteractionHand var1, double var2, double var4) {
      ViewModelModule.s var6 = this.D(var1);
      var6.x().X(var2);
      var6.y().X(var4);
   }

   public void H(InteractionHand var1, double var2) {
      ViewModelModule.s var4 = this.D(var1);
      var4.scale().X(var4.scale().getValue() + var2 * 0.05);
   }

   public void n(InteractionHand var1) {
      ViewModelModule.s var2 = this.D(var1);

      for (e var4 : List.of(var2.scale(), var2.x(), var2.y(), var2.z())) {
         var4.u();
      }
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A("ViewModel", "Active: main {}x, offhand {}x, swing speed {}x", this.X.scale().getValue(), this.e.scale().getValue(), this.E.getValue());
   }

   @Override
   public void onDisable() {
      com.larpclient.h.A("ViewModel", "Hands returned to vanilla placement");
   }

   private ViewModelModule.s n(String var1, String var2) {
      return new ViewModelModule.s(
         this.B(var1 + " Scale", "Size", "Size of the " + var2 + " item or arm", 1.0, 0.1, 2.0, 0.05),
         this.B(var1 + " X", "Sideways", "Positive moves the " + var2 + " outward", 0.0, -2.5, 2.5, 0.01),
         this.B(var1 + " Y", "Height", "Vertical offset of the " + var2, 0.0, -2.0, 2.0, 0.01),
         this.B(var1 + " Z", "Depth", "Negative moves the " + var2 + " away from you", 0.0, -2.0, 1.0, 0.01),
         this.B(var1 + " Pitch", "Pitch", "Tilt of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.B(var1 + " Yaw", "Yaw", "Turn of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.B(var1 + " Roll", "Roll", "Roll of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0)
      );
   }

   private e B(String var1, String var2, String var3, double var4, double var6, double var8, double var10) {
      e var12 = this.addSetting(new e(var1, var3, var4, var6, var8, var10));
      var12.h(var2);
      return var12;
   }

   private record s(e scale, e x, e y, e z, e pitch, e yaw, e roll) {
      List<q<?>> settings() {
         return List.of(this.scale, this.x, this.y, this.z, this.pitch, this.yaw, this.roll);
      }
   }
}
