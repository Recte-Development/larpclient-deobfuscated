package com.larpclient.module;

import com.larpclient.fx;
import com.larpclient.r4;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.List;
import net.minecraft.world.InteractionHand;

public final class ViewModelModule extends Module {
   public static final double W = 0.1;
   public static final double i = 2.0;
   private final ViewModelModule.b k = this.L("Main", "main hand");
   private final ViewModelModule.b D = this.L("Off", "offhand");
   private static ViewModelModule G;

   public ViewModelModule() {
      super("ViewModel", "Resize, move and rotate each hand's item. Drag hands in chat to place them", Category.VISUALS, -1);
      G = this;
   }

   public static ViewModelModule p() {
      return G;
   }

   public List<l6<?>> n() {
      return this.k.settings();
   }

   public List<l6<?>> R() {
      return this.D.settings();
   }

   private ViewModelModule.b T(InteractionHand var1) {
      return var1 == InteractionHand.OFF_HAND ? this.D : this.k;
   }

   public void X(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModelModule.b var4 = this.T(var2);
         var1.translate((var3 ? -1 : 1) * var4.x().getValue(), var4.y().getValue(), var4.z().getValue());
      }
   }

   public void o(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModelModule.b var4 = this.T(var2);
         float var5 = var3 ? -1.0F : 1.0F;
         fx.b(var1, Axis.XP.rotationDegrees(var4.pitch().getValue().floatValue()));
         fx.b(var1, Axis.YP.rotationDegrees(var5 * var4.yaw().getValue().floatValue()));
         fx.b(var1, Axis.ZP.rotationDegrees(var5 * var4.roll().getValue().floatValue()));
         float var6 = var4.scale().getValue().floatValue();
         var1.scale(var6, var6, var6);
      }
   }

   public void u(PoseStack var1, InteractionHand var2, boolean var3) {
      if (this.isEnabled()) {
         float var4 = (var3 ? 6 : -6) / 16.0F;
         float var5 = 0.75F;
         var1.translate(var4, var5, 0.0F);
         this.o(var1, var2, var3);
         var1.translate(-var4, -var5, 0.0F);
      }
   }

   public double X(InteractionHand var1) {
      return this.T(var1).scale().getValue();
   }

   public double Y(InteractionHand var1) {
      return this.T(var1).x().getValue();
   }

   public double F(InteractionHand var1) {
      return this.T(var1).y().getValue();
   }

   public void g(InteractionHand var1, double var2, double var4) {
      ViewModelModule.b var6 = this.T(var1);
      var6.x().C(var2);
      var6.y().C(var4);
   }

   public void b(InteractionHand var1, double var2) {
      ViewModelModule.b var4 = this.T(var1);
      var4.scale().C(var4.scale().getValue() + var2 * 0.05);
   }

   public void O(InteractionHand var1) {
      ViewModelModule.b var2 = this.T(var1);

      for (lc var4 : List.of(var2.scale(), var2.x(), var2.y(), var2.z())) {
         var4.x();
      }
   }

   @Override
   public void onEnable() {
      r4.V("ViewModel", "Active: main {}x, offhand {}x", this.k.scale().getValue(), this.D.scale().getValue());
   }

   @Override
   public void onDisable() {
      r4.V("ViewModel", "Hands returned to vanilla placement");
   }

   private ViewModelModule.b L(String var1, String var2) {
      return new ViewModelModule.b(
         this.B(var1 + " Scale", "Size", "Size of the " + var2 + " item or arm", 1.0, 0.1, 2.0, 0.05),
         this.B(var1 + " X", "Sideways", "Positive moves the " + var2 + " outward", 0.0, -2.5, 2.5, 0.01),
         this.B(var1 + " Y", "Height", "Vertical offset of the " + var2, 0.0, -2.0, 2.0, 0.01),
         this.B(var1 + " Z", "Depth", "Negative moves the " + var2 + " away from you", 0.0, -2.0, 1.0, 0.01),
         this.B(var1 + " Pitch", "Pitch", "Tilt of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.B(var1 + " Yaw", "Yaw", "Turn of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.B(var1 + " Roll", "Roll", "Roll of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0)
      );
   }

   private lc B(String var1, String var2, String var3, double var4, double var6, double var8, double var10) {
      lc var12 = this.addSetting(new lc(var1, var3, var4, var6, var8, var10));
      var12.b(var2);
      return var12;
   }

   private record b(lc scale, lc x, lc y, lc z, lc pitch, lc yaw, lc roll) {
      List<l6<?>> settings() {
         return List.of(this.scale, this.x, this.y, this.z, this.pitch, this.yaw, this.roll);
      }
   }
}
