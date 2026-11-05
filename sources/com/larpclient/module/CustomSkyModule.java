package com.larpclient.module;

import com.larpclient.ea;
import com.larpclient.f5;
import com.larpclient.yv;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.material.FogType;

public class CustomSkyModule extends Module {
   private static CustomSkyModule E;
   private final lp k = this.addSetting(new lp("Sky Style", "World sky effect", "Nebula", "Nebula", "Ocean", "Israel Flag"));
   private final lc q = this.addSetting(new lc("Animation Speed", "Effect motion speed; zero freezes it", 0.5, 0.0, 2.0, 0.1));
   private final ln N = this.addSetting(new ln("Stars", "Keep a star field visible in the custom sky", true));
   private final lc V = this.addSetting(new lc("Star Brightness", "Star field brightness", 0.65, 0.0, 1.0, 0.05));
   private final ln c = this.addSetting(new ln("Clouds", "Show vanilla clouds", false));
   private final ln W = this.addSetting(new ln("Match Fog", "Blend atmospheric fog into the horizon", true));
   private final lc d = this.addSetting(new lc("Fog Distance", "Multiplier of vanilla atmospheric distance", 1.0, 0.1, 10.0, 0.1));
   private final lc t = this.addSetting(new lc("Nebula Brightness", "Overall nebula exposure", 1.0, 0.2, 2.0, 0.05));
   private final lc f = this.addSetting(new lc("Nebula Saturation", "Nebula color richness", 0.9, 0.0, 1.5, 0.05));
   private final lc H = this.addSetting(new lc("Nebula Intensity", "Cloud and highlight strength", 0.85, 0.0, 1.5, 0.05));
   private final lc v = this.addSetting(new lc("Nebula Animation Speed", "Cloud motion multiplier; zero freezes cloud motion", 1.0, 0.0, 3.0, 0.1));
   private final lp i = this.addSetting(new lp("Nebula Motion", "Flowing clouds or the original slow drift", "Flow", "Flow", "Classic"));
   private final lc K = this.p("Nebula Flow Strength", "Cloud deformation", 0.35, 0.0, 1.0, 0.05);
   private final lc u = this.p("Nebula Scale", "Cloud pattern scale", 1.0, 0.5, 2.0, 0.05);
   private final lv D = this.addSetting(new lv("Nebula Color", "Cloud artwork tint", -1));
   private final lc m = this.addSetting(new lc("Star Density", "Procedural star field density", 0.45, 0.0, 1.0, 0.05));
   private final lc O = this.addSetting(new lc("Nebula Rotation Speed", "Degrees per second; zero stops rotation", 0.02, 0.0, 0.15, 0.01));
   private final ln w = this.addSetting(new ln("Show Vanilla Sun", "Show Minecraft's sun", false));
   private final ln Y = this.addSetting(new ln("Show Vanilla Moon", "Show Minecraft's moon", false));
   private final ln Z = this.addSetting(new ln("Horizon Blend", "Subtle fade into the fog palette", true));
   private final lc G = this.p("Exposure", "Sky brightness", 1.0, 0.25, 2.0, 0.05);
   private final lc J = this.p("Rotation Speed", "Degrees per second", 0.02, -0.2, 0.2, 0.01);
   private final lp I = this.addSetting(new lp("Sky Quality", "Procedural noise and caustic detail", "High", "Low", "Medium", "High", "Ultra"));
   private final lc r = this.p("Caustic Scale", "Size of the fluid light cells", 5.0, 2.0, 10.0, 0.25);
   private final lc X = this.p("Caustic Intensity", "Brightness of converging light", 0.65, 0.0, 1.5, 0.05);
   private final lc p = this.p("Caustic Distortion", "Fluid warping strength", 0.8, 0.0, 2.0, 0.1);
   private final lv C = this.addSetting(new lv("Ocean Base Color", "Deep water color", -16635057));
   private final lv a = this.addSetting(new lv("Ocean Highlight Color", "Caustic light color", -12133155));
   private f5 x;

   private lc p(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lc(var1, var2, var3, var5, var7, var9));
   }

   public lp G() {
      return this.k;
   }

   public List<l6<?>> C() {
      return switch (this.Q()) {
         case "Nebula" -> List.of(this.t, this.f, this.H, this.i, this.v, this.K, this.u, this.D, this.O);
         case "Ocean" -> List.of(this.r, this.X, this.p, this.C, this.a);
         default -> List.of();
      };
   }

   public List<l6<?>> b() {
      return List.of(this.t, this.f, this.H, this.D, this.X, this.C, this.a, this.G);
   }

   public List<l6<?>> e() {
      return List.of(this.q, this.i, this.v, this.O, this.J);
   }

   public List<l6<?>> X() {
      return List.of(this.N, this.V, this.m, this.c, this.w, this.Y, this.Z, this.W, this.d);
   }

   public List<l6<?>> l() {
      return List.of(this.K, this.u, this.r, this.p, this.I);
   }

   public String Q() {
      return this.k.getValue();
   }

   public float s() {
      return this.q.getValue().floatValue();
   }

   public float P() {
      return this.J.getValue().floatValue();
   }

   public float h() {
      return this.G.getValue().floatValue();
   }

   public boolean A() {
      return this.Z.getValue();
   }

   public float H() {
      return this.I.X().indexOf(this.I.getValue());
   }

   public float S() {
      return this.r.getValue().floatValue();
   }

   public float W() {
      return this.X.getValue().floatValue();
   }

   public float K() {
      return this.p.getValue().floatValue();
   }

   public int k() {
      return this.C.getValue();
   }

   public int F() {
      return this.a.getValue();
   }

   public boolean E() {
      return this.w.getValue();
   }

   public boolean w() {
      return this.Y.getValue();
   }

   public boolean C() {
      return !this.k.i("Israel Flag");
   }

   public boolean h() {
      return this.k.i("Nebula");
   }

   public float f() {
      return this.t.getValue().floatValue();
   }

   public float T() {
      return this.f.getValue().floatValue();
   }

   public float i() {
      return this.H.getValue().floatValue();
   }

   public float c() {
      return this.m.getValue().floatValue();
   }

   public float b() {
      return this.v.getValue().floatValue();
   }

   public float u() {
      return this.O.getValue().floatValue();
   }

   public boolean M() {
      return this.i.i("Flow");
   }

   public float G() {
      return this.K.getValue().floatValue();
   }

   public float A() {
      return this.u.getValue().floatValue();
   }

   public int f() {
      return this.D.getValue();
   }

   public CustomSkyModule() {
      super("CustomSky", "Use animated custom skies", Category.VISUALS, -1);
      E = this;

      for (l6 var4 : new l6[]{this.t, this.f, this.H, this.v, this.O, this.i, this.K, this.u, this.D}) {
         var4.x(this::h);
      }

      this.K.x(() -> this.h() && this.M());

      for (l6 var8 : new l6[]{this.r, this.X, this.p, this.C, this.a}) {
         var8.x(() -> this.k.i("Ocean"));
      }

      this.q.x(() -> this.k.i("Ocean"));
      this.J.x(() -> !this.h());
      this.G.x(() -> !this.h());
      this.Z.x(() -> !this.h());
      this.I.x(() -> this.k.i("Ocean"));
      this.N.x(this::h);
      this.V.x(() -> this.N.R() && this.N.getValue());
      this.m.x(this.V::R);
   }

   @Override
   public void onEnable() {
      yv.h();
   }

   public static CustomSkyModule Y() {
      return E;
   }

   public static boolean i() {
      if (E != null && E.isEnabled()) {
         Minecraft var0 = Minecraft.getInstance();
         return E != null
            && E.isOperational()
            && yv.Z(E.Q())
            && var0.level != null
            && var0.level.dimensionType().skybox() == Skybox.OVERWORLD
            && var0.gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE;
      } else {
         return false;
      }
   }

   private f5 q(int var1, int var2) {
      if (this.x == null || this.x.zenith() != var1 || this.x.horizon() != var2) {
         this.x = new f5(var1, var2);
      }

      return this.x;
   }

   public f5 p() {
      if (this.k.i("Ocean")) {
         return this.q(this.C.getValue(), this.C.getValue());
      } else {
         return this.k.i("Israel Flag") ? this.q(-1, -1) : this.q(this.C(-16251885), this.C(-15265755));
      }
   }

   private int C(int var1) {
      float var2 = var1 >> 16 & 0xFF;
      float var3 = var1 >> 8 & 0xFF;
      float var4 = var1 & 0xFF;
      float var5 = var2 * 0.2126F + var3 * 0.7152F + var4 * 0.0722F;
      float var6 = this.T();
      float var7 = this.f();
      int var8 = (int)Math.clamp((var5 + (var2 - var5) * var6) * var7, 0.0F, 255.0F);
      int var9 = (int)Math.clamp((var5 + (var3 - var5) * var6) * var7, 0.0F, 255.0F);
      int var10 = (int)Math.clamp((var5 + (var4 - var5) * var6) * var7, 0.0F, 255.0F);
      return 0xFF000000 | var8 << 16 | var9 << 8 | var10;
   }

   public float x() {
      return (float)(ea.y() * this.q.getValue());
   }

   public float p() {
      return !this.k.i("Israel Flag") && this.N.getValue() ? this.V.getValue().floatValue() : 0.0F;
   }

   public boolean d() {
      return this.c.getValue();
   }

   public boolean y() {
      return this.W.getValue();
   }

   public float V() {
      return this.d.getValue().floatValue();
   }
}
