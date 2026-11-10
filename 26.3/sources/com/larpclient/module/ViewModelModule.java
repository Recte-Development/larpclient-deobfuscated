package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.qd;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.world.InteractionHand;

public final class ViewModelModule extends Module {
   public static final double E = 0.1;
   public static final double r = 2.0;
   private static final int g = 2;
   private final ViewModelModule.c D = this.H("Main", "main hand");
   private final ViewModelModule.c Q = this.H("Off", "offhand");
   private final b k = this.f("Swing Speed", "Speed", "How fast your hits animate. 1 is normal, lower is slower, higher is faster", 1.0, 0.25, 3.0, 0.25);
   private static ViewModelModule v;

   public ViewModelModule() {
      super("ViewModel", "Resize, move and rotate each hand's item and change swing speed. Drag hands in chat to place them", Category.VISUALS, -1);
      v = this;
   }

   public static ViewModelModule t() {
      return v;
   }

   public List<r<?>> R() {
      return List.of(this.k);
   }

   public List<r<?>> K() {
      return this.D.settings();
   }

   public List<r<?>> t() {
      return this.Q.settings();
   }

   public int U(int var1) {
      if (this.isEnabled()) {
         double var2 = this.k.getValue();
         return var2 == 1.0 ? var1 : Math.max(2, (int)Math.round(var1 / var2));
      } else {
         return var1;
      }
   }

   private ViewModelModule.c N(InteractionHand var1) {
      return var1 == InteractionHand.OFF_HAND ? this.Q : this.D;
   }

   public void i(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModelModule.c var4 = this.N(var2);
         var1.translate((var3 ? -1 : 1) * var4.x().getValue(), var4.y().getValue(), var4.z().getValue());
      }
   }

   public void n(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModelModule.c var4 = this.N(var2);
         float var5 = var3 ? -1.0F : 1.0F;
         qd.i(var1, Axis.XP.rotationDegrees(var4.pitch().getValue().floatValue()));
         qd.i(var1, Axis.YP.rotationDegrees(var5 * var4.yaw().getValue().floatValue()));
         qd.i(var1, Axis.ZP.rotationDegrees(var5 * var4.roll().getValue().floatValue()));
         float var6 = var4.scale().getValue().floatValue();
         var1.scale(var6, var6, var6);
      }
   }

   public void U(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         float var4 = (var3 ? 6 : -6) / 16.0F;
         float var5 = 0.75F;
         var1.translate(var4, var5, 0.0F);
         this.n(var1, var2, var3);
         var1.translate(-var4, -var5, 0.0F);
      }
   }

   public double q(InteractionHand var1) {
      return this.N(var1).scale().getValue();
   }

   public double b(InteractionHand var1) {
      return this.N(var1).x().getValue();
   }

   public double F(InteractionHand var1) {
      return this.N(var1).y().getValue();
   }

   public void C(InteractionHand var1, double var2, double var4) {
      ViewModelModule.c var6 = this.N(var1);
      var6.x().O(var2);
      var6.y().O(var4);
   }

   public void i(InteractionHand var1, double var2) {
      ViewModelModule.c var4 = this.N(var1);
      var4.scale().O(var4.scale().getValue() + var2 * 0.05);
   }

   public void R(InteractionHand var1) {
      ViewModelModule.c var2 = this.N(var1);

      for (b var4 : List.of(var2.scale(), var2.x(), var2.y(), var2.z())) {
         var4.u();
      }
   }

   @Override
   public void onEnable() {
      _q.z("ViewModel", "Active: main {}x, offhand {}x, swing speed {}x", this.D.scale().getValue(), this.Q.scale().getValue(), this.k.getValue());
   }

   @Override
   public void onDisable() {
      _q.z("ViewModel", "Hands returned to vanilla placement");
   }

   private ViewModelModule.c H(String var1, String var2) {
      return new ViewModelModule.c(
         this.f(var1 + " Scale", "Size", "Size of the " + var2 + " item or arm", 1.0, 0.1, 2.0, 0.05),
         this.f(var1 + " X", "Sideways", "Positive moves the " + var2 + " outward", 0.0, -2.5, 2.5, 0.01),
         this.f(var1 + " Y", "Height", "Vertical offset of the " + var2, 0.0, -2.0, 2.0, 0.01),
         this.f(var1 + " Z", "Depth", "Negative moves the " + var2 + " away from you", 0.0, -2.0, 1.0, 0.01),
         this.f(var1 + " Pitch", "Pitch", "Tilt of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.f(var1 + " Yaw", "Yaw", "Turn of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.f(var1 + " Roll", "Roll", "Roll of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0)
      );
   }

   private b f(String var1, String var2, String var3, double var4, double var6, double var8, double var10) {
      b var12 = this.addSetting(new b(var1, var3, var4, var6, var8, var10));
      var12.B(var2);
      return var12;
   }

   private record c(b scale, b x, b y, b z, b pitch, b yaw, b roll) {
      List<r<?>> settings() {
         return List.of(this.scale, this.x, this.y, this.z, this.pitch, this.yaw, this.roll);
      }
   }
}
