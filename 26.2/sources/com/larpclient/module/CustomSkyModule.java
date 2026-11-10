package com.larpclient.module;

import com.larpclient.b1;
import com.larpclient.cf;
import com.larpclient.ct;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.material.FogType;

public class CustomSkyModule extends Module {
   private static CustomSkyModule S;
   private final l Z = this.addSetting(new l("Sky Style", "World sky effect", "Nebula", "Nebula", "Ocean", "Israel Flag"));
   private final e v = this.addSetting(new e("Animation Speed", "Effect motion speed; zero freezes it", 0.5, 0.0, 2.0, 0.1));
   private final s a = this.addSetting(new s("Stars", "Keep a star field visible in the custom sky", true));
   private final e D = this.addSetting(new e("Star Brightness", "Star field brightness", 0.65, 0.0, 1.0, 0.05));
   private final s E = this.addSetting(new s("Clouds", "Show vanilla clouds", false));
   private final s j = this.addSetting(new s("Match Fog", "Blend atmospheric fog into the horizon", true));
   private final e o = this.addSetting(new e("Fog Distance", "Multiplier of vanilla atmospheric distance", 1.0, 0.1, 10.0, 0.1));
   private final e V = this.addSetting(new e("Nebula Brightness", "Overall nebula exposure", 1.0, 0.2, 2.0, 0.05));
   private final e w = this.addSetting(new e("Nebula Saturation", "Nebula color richness", 0.9, 0.0, 1.5, 0.05));
   private final e B = this.addSetting(new e("Nebula Intensity", "Cloud and highlight strength", 0.85, 0.0, 1.5, 0.05));
   private final e q = this.addSetting(new e("Nebula Animation Speed", "Cloud motion multiplier; zero freezes cloud motion", 1.0, 0.0, 3.0, 0.1));
   private final l m = this.addSetting(new l("Nebula Motion", "Flowing clouds or the original slow drift", "Flow", "Flow", "Classic"));
   private final e P = this.P("Nebula Flow Strength", "Cloud deformation", 0.35, 0.0, 1.0, 0.05);
   private final e I = this.P("Nebula Scale", "Cloud pattern scale", 1.0, 0.5, 2.0, 0.05);
   private final i Y = this.addSetting(new i("Nebula Color", "Cloud artwork tint", -1));
   private final e b = this.addSetting(new e("Star Density", "Procedural star field density", 0.45, 0.0, 1.0, 0.05));
   private final e e = this.addSetting(new e("Nebula Rotation Speed", "Degrees per second; zero stops rotation", 0.02, 0.0, 0.15, 0.01));
   private final s n = this.addSetting(new s("Show Vanilla Sun", "Show Minecraft's sun", false));
   private final s x = this.addSetting(new s("Show Vanilla Moon", "Show Minecraft's moon", false));
   private final s A = this.addSetting(new s("Horizon Blend", "Subtle fade into the fog palette", true));
   private final e K = this.P("Exposure", "Sky brightness", 1.0, 0.25, 2.0, 0.05);
   private final e t = this.P("Rotation Speed", "Degrees per second", 0.02, -0.2, 0.2, 0.01);
   private final l Q = this.addSetting(new l("Sky Quality", "Procedural noise and caustic detail", "High", "Low", "Medium", "High", "Ultra"));
   private final e N = this.P("Caustic Scale", "Size of the fluid light cells", 5.0, 2.0, 10.0, 0.25);
   private final e J = this.P("Caustic Intensity", "Brightness of converging light", 0.65, 0.0, 1.5, 0.05);
   private final e R = this.P("Caustic Distortion", "Fluid warping strength", 0.8, 0.0, 2.0, 0.1);
   private final i L = this.addSetting(new i("Ocean Base Color", "Deep water color", -16635057));
   private final i h = this.addSetting(new i("Ocean Highlight Color", "Caustic light color", -12133155));
   private b1 O;

   private e P(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new e(var1, var2, var3, var5, var7, var9));
   }

   public l d() {
      return this.Z;
   }

   public List<q<?>> v() {
      return switch (this.o()) {
         case "Nebula" -> List.of(this.V, this.w, this.B, this.m, this.q, this.P, this.I, this.Y, this.e);
         case "Ocean" -> List.of(this.N, this.J, this.R, this.L, this.h);
         default -> List.of();
      };
   }

   public List<q<?>> q() {
      return List.of(this.V, this.w, this.B, this.Y, this.J, this.L, this.h, this.K);
   }

   public List<q<?>> J() {
      return List.of(this.v, this.m, this.q, this.e, this.t);
   }

   public List<q<?>> k() {
      return List.of(this.a, this.D, this.b, this.E, this.n, this.x, this.A, this.j, this.o);
   }

   public List<q<?>> h() {
      return List.of(this.P, this.I, this.N, this.R, this.Q);
   }

   public String o() {
      return this.Z.getValue();
   }

   public float U() {
      return this.v.getValue().floatValue();
   }

   public float A() {
      return this.t.getValue().floatValue();
   }

   public float z() {
      return this.K.getValue().floatValue();
   }

   public boolean o() {
      return this.A.getValue();
   }

   public float X() {
      return this.Q.a().indexOf(this.Q.getValue());
   }

   public float m() {
      return this.N.getValue().floatValue();
   }

   public float L() {
      return this.J.getValue().floatValue();
   }

   public float Q() {
      return this.R.getValue().floatValue();
   }

   public int n() {
      return this.L.getValue();
   }

   public int m() {
      return this.h.getValue();
   }

   public boolean U() {
      return this.n.getValue();
   }

   public boolean s() {
      return this.x.getValue();
   }

   public boolean I() {
      return !this.Z.O("Israel Flag");
   }

   public boolean X() {
      return this.Z.O("Nebula");
   }

   public float C() {
      return this.V.getValue().floatValue();
   }

   public float e() {
      return this.w.getValue().floatValue();
   }

   public float r() {
      return this.B.getValue().floatValue();
   }

   public float D() {
      return this.b.getValue().floatValue();
   }

   public float O() {
      return this.q.getValue().floatValue();
   }

   public float l() {
      return this.e.getValue().floatValue();
   }

   public boolean V() {
      return this.m.O("Flow");
   }

   public float a() {
      return this.P.getValue().floatValue();
   }

   public float S() {
      return this.I.getValue().floatValue();
   }

   public int b() {
      return this.Y.getValue();
   }

   public CustomSkyModule() {
      super("CustomSky", "Use animated custom skies", Category.VISUALS, -1);
      S = this;

      for (q var4 : new q[]{this.V, this.w, this.B, this.q, this.e, this.m, this.P, this.I, this.Y}) {
         var4.P(this::X);
      }

      this.P.P(() -> this.X() && this.V());

      for (q var8 : new q[]{this.N, this.J, this.R, this.L, this.h}) {
         var8.P(() -> this.Z.O("Ocean"));
      }

      this.v.P(() -> this.Z.O("Ocean"));
      this.t.P(() -> !this.X());
      this.K.P(() -> !this.X());
      this.A.P(() -> !this.X());
      this.Q.P(() -> this.Z.O("Ocean"));
      this.a.P(this::X);
      this.D.P(() -> this.a.W() && this.a.getValue());
      this.b.P(this.D::W);
   }

   @Override
   public void onEnable() {
      ct.S();
   }

   public static CustomSkyModule K() {
      return S;
   }

   public static boolean v(Skybox var0) {
      return var0 == Skybox.OVERWORLD || var0 == Skybox.END;
   }

   public static boolean i() {
      if (S != null && S.isEnabled()) {
         Minecraft var0 = Minecraft.getInstance();
         return S != null
            && S.isOperational()
            && ct.F(S.o())
            && var0.level != null
            && v(var0.level.dimensionType().skybox())
            && var0.gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE;
      } else {
         return false;
      }
   }

   private b1 h(int var1, int var2) {
      if (this.O == null || this.O.zenith() != var1 || this.O.horizon() != var2) {
         this.O = new b1(var1, var2);
      }

      return this.O;
   }

   public b1 m() {
      if (this.Z.O("Ocean")) {
         return this.h(this.L.getValue(), this.L.getValue());
      } else {
         return this.Z.O("Israel Flag") ? this.h(-1, -1) : this.h(this.W(-16251885), this.W(-15265755));
      }
   }

   private int W(int var1) {
      float var2 = var1 >> 16 & 0xFF;
      float var3 = var1 >> 8 & 0xFF;
      float var4 = var1 & 0xFF;
      float var5 = var2 * 0.2126F + var3 * 0.7152F + var4 * 0.0722F;
      float var6 = this.e();
      float var7 = this.C();
      int var8 = (int)Math.clamp((var5 + (var2 - var5) * var6) * var7, 0.0F, 255.0F);
      int var9 = (int)Math.clamp((var5 + (var3 - var5) * var6) * var7, 0.0F, 255.0F);
      int var10 = (int)Math.clamp((var5 + (var4 - var5) * var6) * var7, 0.0F, 255.0F);
      return 0xFF000000 | var8 << 16 | var9 << 8 | var10;
   }

   public float G() {
      return (float)(cf.F() * this.v.getValue());
   }

   public float K() {
      return !this.Z.O("Israel Flag") && this.a.getValue() ? this.D.getValue().floatValue() : 0.0F;
   }

   public boolean q() {
      return this.E.getValue();
   }

   public boolean p() {
      return this.j.getValue();
   }

   public float R() {
      return this.o.getValue().floatValue();
   }
}
