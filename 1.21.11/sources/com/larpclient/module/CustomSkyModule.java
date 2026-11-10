package com.larpclient.module;

import com.larpclient.f8;
import com.larpclient.qo;
import java.util.List;
import net.minecraft.class_310;
import net.minecraft.class_5636;
import net.minecraft.class_2874.class_12326;

public class CustomSkyModule extends Module {
   private static CustomSkyModule q;
   private final li k = this.addSetting(new li("Sky Style", "World sky effect", "Nebula", "Nebula", "Ocean", "Israel Flag"));
   private final lo a = this.addSetting(new lo("Animation Speed", "Effect motion speed; zero freezes it", 0.5, 0.0, 2.0, 0.1));
   private final l3 Q = this.addSetting(new l3("Stars", "Keep a star field visible in the custom sky", true));
   private final lo T = this.addSetting(new lo("Star Brightness", "Star field brightness", 0.65, 0.0, 1.0, 0.05));
   private final l3 U = this.addSetting(new l3("Clouds", "Show vanilla clouds", false));
   private final l3 J = this.addSetting(new l3("Match Fog", "Blend atmospheric fog into the horizon", true));
   private final lo v = this.addSetting(new lo("Fog Distance", "Multiplier of vanilla atmospheric distance", 1.0, 0.1, 10.0, 0.1));
   private final lo l = this.addSetting(new lo("Nebula Brightness", "Overall nebula exposure", 1.0, 0.2, 2.0, 0.05));
   private final lo c = this.addSetting(new lo("Nebula Saturation", "Nebula color richness", 0.9, 0.0, 1.5, 0.05));
   private final lo n = this.addSetting(new lo("Nebula Intensity", "Cloud and highlight strength", 0.85, 0.0, 1.5, 0.05));
   private final lo h = this.addSetting(new lo("Nebula Animation Speed", "Cloud motion multiplier; zero freezes cloud motion", 1.0, 0.0, 3.0, 0.1));
   private final li w = this.addSetting(new li("Nebula Motion", "Flowing clouds or the original slow drift", "Flow", "Flow", "Classic"));
   private final lo j = this.I("Nebula Flow Strength", "Cloud deformation", 0.35, 0.0, 1.0, 0.05);
   private final lo F = this.I("Nebula Scale", "Cloud pattern scale", 1.0, 0.5, 2.0, 0.05);
   private final l0 f = this.addSetting(new l0("Nebula Color", "Cloud artwork tint", -1));
   private final lo o = this.addSetting(new lo("Star Density", "Procedural star field density", 0.45, 0.0, 1.0, 0.05));
   private final lo O = this.addSetting(new lo("Nebula Rotation Speed", "Degrees per second; zero stops rotation", 0.02, 0.0, 0.15, 0.01));
   private final l3 K = this.addSetting(new l3("Show Vanilla Sun", "Show Minecraft's sun", false));
   private final l3 u = this.addSetting(new l3("Show Vanilla Moon", "Show Minecraft's moon", false));
   private final l3 V = this.addSetting(new l3("Horizon Blend", "Subtle fade into the fog palette", true));
   private final lo I = this.I("Exposure", "Sky brightness", 1.0, 0.25, 2.0, 0.05);
   private final lo H = this.I("Rotation Speed", "Degrees per second", 0.02, -0.2, 0.2, 0.01);
   private final li p = this.addSetting(new li("Sky Quality", "Procedural noise and caustic detail", "High", "Low", "Medium", "High", "Ultra"));
   private final lo t = this.I("Caustic Scale", "Size of the fluid light cells", 5.0, 2.0, 10.0, 0.25);
   private final lo X = this.I("Caustic Intensity", "Brightness of converging light", 0.65, 0.0, 1.5, 0.05);
   private final lo b = this.I("Caustic Distortion", "Fluid warping strength", 0.8, 0.0, 2.0, 0.1);
   private final l0 C = this.addSetting(new l0("Ocean Base Color", "Deep water color", -16635057));
   private final l0 z = this.addSetting(new l0("Ocean Highlight Color", "Caustic light color", -12133155));
   private com.larpclient.lw A;

   private lo I(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lo(var1, var2, var3, var5, var7, var9));
   }

   public li r() {
      return this.k;
   }

   public List<lb<?>> O() {
      return switch (this.V()) {
         case "Nebula" -> List.of(this.l, this.c, this.n, this.w, this.h, this.j, this.F, this.f, this.O);
         case "Ocean" -> List.of(this.t, this.X, this.b, this.C, this.z);
         default -> List.of();
      };
   }

   public List<lb<?>> h() {
      return List.of(this.l, this.c, this.n, this.f, this.X, this.C, this.z, this.I);
   }

   public List<lb<?>> s() {
      return List.of(this.a, this.w, this.h, this.O, this.H);
   }

   public List<lb<?>> y() {
      return List.of(this.Q, this.T, this.o, this.U, this.K, this.u, this.V, this.J, this.v);
   }

   public List<lb<?>> C() {
      return List.of(this.j, this.F, this.t, this.b, this.p);
   }

   public String V() {
      return this.k.getValue();
   }

   public float L() {
      return this.a.getValue().floatValue();
   }

   public float o() {
      return this.H.getValue().floatValue();
   }

   public float U() {
      return this.I.getValue().floatValue();
   }

   public boolean D() {
      return this.V.getValue();
   }

   public float E() {
      return this.p.e().indexOf(this.p.getValue());
   }

   public float p() {
      return this.t.getValue().floatValue();
   }

   public float t() {
      return this.X.getValue().floatValue();
   }

   public float B() {
      return this.b.getValue().floatValue();
   }

   public int P() {
      return this.C.getValue();
   }

   public int V() {
      return this.z.getValue();
   }

   public boolean o() {
      return this.K.getValue();
   }

   public boolean T() {
      return this.u.getValue();
   }

   public boolean H() {
      return !this.k.G("Israel Flag");
   }

   public boolean S() {
      return this.k.G("Nebula");
   }

   public float g() {
      return this.l.getValue().floatValue();
   }

   public float c() {
      return this.c.getValue().floatValue();
   }

   public float v() {
      return this.n.getValue().floatValue();
   }

   public float N() {
      return this.o.getValue().floatValue();
   }

   public float P() {
      return this.h.getValue().floatValue();
   }

   public float D() {
      return this.O.getValue().floatValue();
   }

   public boolean P() {
      return this.w.G("Flow");
   }

   public float X() {
      return this.j.getValue().floatValue();
   }

   public float R() {
      return this.F.getValue().floatValue();
   }

   public int y() {
      return this.f.getValue();
   }

   public CustomSkyModule() {
      super("CustomSky", "Use animated custom skies", Category.VISUALS, -1);
      q = this;

      for (lb var4 : new lb[]{this.l, this.c, this.n, this.h, this.O, this.w, this.j, this.F, this.f}) {
         var4.O(this::S);
      }

      this.j.O(() -> this.S() && this.P());

      for (lb var8 : new lb[]{this.t, this.X, this.b, this.C, this.z}) {
         var8.O(() -> this.k.G("Ocean"));
      }

      this.a.O(() -> this.k.G("Ocean"));
      this.H.O(() -> !this.S());
      this.I.O(() -> !this.S());
      this.V.O(() -> !this.S());
      this.p.O(() -> this.k.G("Ocean"));
      this.Q.O(this::S);
      this.T.O(() -> this.Q.B() && this.Q.getValue());
      this.o.O(this.T::B);
   }

   @Override
   public void onEnable() {
      f8.R();
   }

   public static CustomSkyModule M() {
      return q;
   }

   public static boolean z(class_12326 var0) {
      return var0 == class_12326.field_64386 || var0 == class_12326.field_64387;
   }

   public static boolean h() {
      if (q != null && q.isEnabled()) {
         class_310 var0 = class_310.method_1551();
         return q != null
            && q.isOperational()
            && f8.Y(q.V())
            && var0.field_1687 != null
            && z(var0.field_1687.method_8597().comp_5220())
            && var0.field_1773.method_19418().method_19334() == class_5636.field_27888;
      } else {
         return false;
      }
   }

   private com.larpclient.lw D(int var1, int var2) {
      if (this.A == null || this.A.zenith() != var1 || this.A.horizon() != var2) {
         this.A = new com.larpclient.lw(var1, var2);
      }

      return this.A;
   }

   public com.larpclient.lw Y() {
      if (this.k.G("Ocean")) {
         return this.D(this.C.getValue(), this.C.getValue());
      } else {
         return this.k.G("Israel Flag") ? this.D(-1, -1) : this.D(this.K(-16251885), this.K(-15265755));
      }
   }

   private int K(int var1) {
      float var2 = var1 >> 16 & 0xFF;
      float var3 = var1 >> 8 & 0xFF;
      float var4 = var1 & 0xFF;
      float var5 = var2 * 0.2126F + var3 * 0.7152F + var4 * 0.0722F;
      float var6 = this.c();
      float var7 = this.g();
      int var8 = (int)Math.clamp((var5 + (var2 - var5) * var6) * var7, 0.0F, 255.0F);
      int var9 = (int)Math.clamp((var5 + (var3 - var5) * var6) * var7, 0.0F, 255.0F);
      int var10 = (int)Math.clamp((var5 + (var4 - var5) * var6) * var7, 0.0F, 255.0F);
      return 0xFF000000 | var8 << 16 | var9 << 8 | var10;
   }

   public float Q() {
      return (float)(qo.x() * this.a.getValue());
   }

   public float M() {
      return !this.k.G("Israel Flag") && this.Q.getValue() ? this.T.getValue().floatValue() : 0.0F;
   }

   public boolean w() {
      return this.U.getValue();
   }

   public boolean z() {
      return this.J.getValue();
   }

   public float f() {
      return this.v.getValue().floatValue();
   }
}
