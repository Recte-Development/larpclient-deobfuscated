package com.larpclient.module;

import com.larpclient.em;
import com.larpclient.q8;
import java.util.List;
import net.minecraft.class_1268;
import net.minecraft.class_4587;
import net.minecraft.class_7833;

public final class ViewModelModule extends Module {
   public static final double R = 0.1;
   public static final double h = 2.0;
   private static final int f = 2;
   private final ViewModelModule.j u = this.X("Main", "main hand");
   private final ViewModelModule.j D = this.X("Off", "offhand");
   private final lo k = this.r("Swing Speed", "Speed", "How fast your hits animate. 1 is normal, lower is slower, higher is faster", 1.0, 0.25, 3.0, 0.25);
   private static ViewModelModule Z;

   public ViewModelModule() {
      super("ViewModel", "Resize, move and rotate each hand's item and change swing speed. Drag hands in chat to place them", Category.VISUALS, -1);
      Z = this;
   }

   public static ViewModelModule B() {
      return Z;
   }

   public List<lb<?>> x() {
      return List.of(this.k);
   }

   public List<lb<?>> E() {
      return this.u.settings();
   }

   public List<lb<?>> a() {
      return this.D.settings();
   }

   public int D(int var1) {
      if (this.isEnabled()) {
         double var2 = this.k.getValue();
         return var2 == 1.0 ? var1 : Math.max(2, (int)Math.round(var1 / var2));
      } else {
         return var1;
      }
   }

   private ViewModelModule.j E(class_1268 var1) {
      return var1 == class_1268.field_5810 ? this.D : this.u;
   }

   public void K(class_4587 var1, class_1268 var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModelModule.j var4 = this.E(var2);
         var1.method_22904((var3 ? -1 : 1) * var4.x().getValue(), var4.y().getValue(), var4.z().getValue());
      }
   }

   public void R(class_4587 var1, class_1268 var2, boolean var3) {
      if (this.isEnabled()) {
         ViewModelModule.j var4 = this.E(var2);
         float var5 = var3 ? -1.0F : 1.0F;
         em.W(var1, class_7833.field_40714.rotationDegrees(var4.pitch().getValue().floatValue()));
         em.W(var1, class_7833.field_40716.rotationDegrees(var5 * var4.yaw().getValue().floatValue()));
         em.W(var1, class_7833.field_40718.rotationDegrees(var5 * var4.roll().getValue().floatValue()));
         float var6 = var4.scale().getValue().floatValue();
         var1.method_22905(var6, var6, var6);
      }
   }

   public void n(class_4587 var1, class_1268 var2, boolean var3) {
      if (this.isEnabled()) {
         float var4 = (var3 ? 6 : -6) / 16.0F;
         float var5 = 0.75F;
         var1.method_46416(var4, var5, 0.0F);
         this.R(var1, var2, var3);
         var1.method_46416(-var4, -var5, 0.0F);
      }
   }

   public double l(class_1268 var1) {
      return this.E(var1).scale().getValue();
   }

   public double n(class_1268 var1) {
      return this.E(var1).x().getValue();
   }

   public double w(class_1268 var1) {
      return this.E(var1).y().getValue();
   }

   public void G(class_1268 var1, double var2, double var4) {
      ViewModelModule.j var6 = this.E(var1);
      var6.x().q(var2);
      var6.y().q(var4);
   }

   public void U(class_1268 var1, double var2) {
      ViewModelModule.j var4 = this.E(var1);
      var4.scale().q(var4.scale().getValue() + var2 * 0.05);
   }

   public void x(class_1268 var1) {
      ViewModelModule.j var2 = this.E(var1);

      for (lo var4 : List.of(var2.scale(), var2.x(), var2.y(), var2.z())) {
         var4.T();
      }
   }

   @Override
   public void onEnable() {
      q8.G("ViewModel", "Active: main {}x, offhand {}x, swing speed {}x", this.u.scale().getValue(), this.D.scale().getValue(), this.k.getValue());
   }

   @Override
   public void onDisable() {
      q8.G("ViewModel", "Hands returned to vanilla placement");
   }

   private ViewModelModule.j X(String var1, String var2) {
      return new ViewModelModule.j(
         this.r(var1 + " Scale", "Size", "Size of the " + var2 + " item or arm", 1.0, 0.1, 2.0, 0.05),
         this.r(var1 + " X", "Sideways", "Positive moves the " + var2 + " outward", 0.0, -2.5, 2.5, 0.01),
         this.r(var1 + " Y", "Height", "Vertical offset of the " + var2, 0.0, -2.0, 2.0, 0.01),
         this.r(var1 + " Z", "Depth", "Negative moves the " + var2 + " away from you", 0.0, -2.0, 1.0, 0.01),
         this.r(var1 + " Pitch", "Pitch", "Tilt of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.r(var1 + " Yaw", "Yaw", "Turn of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0),
         this.r(var1 + " Roll", "Roll", "Roll of the " + var2 + " item", 0.0, -180.0, 180.0, 1.0)
      );
   }

   private lo r(String var1, String var2, String var3, double var4, double var6, double var8, double var10) {
      lo var12 = this.addSetting(new lo(var1, var3, var4, var6, var8, var10));
      var12.M(var2);
      return var12;
   }

   private record j(lo scale, lo x, lo y, lo z, lo pitch, lo yaw, lo roll) {
      List<lb<?>> settings() {
         return List.of(this.scale, this.x, this.y, this.z, this.pitch, this.yaw, this.roll);
      }
   }
}
