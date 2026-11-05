package com.larpclient.module;

import com.larpclient.et;
import com.larpclient.ty;
import com.larpclient.wy;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.material.FogType;

public class CustomSkyModule extends Module {
   private static CustomSkyModule G;
   private final d6 N = this.addSetting(new d6("Sky Style", "World sky effect", "Nebula", "Nebula", "Ocean", "Israel Flag"));
   private final d9 c = this.addSetting(new d9("Animation Speed", "Effect motion speed; zero freezes it", 0.5, 0.0, 2.0, 0.1));
   private final dh o = this.addSetting(new dh("Stars", "Keep a star field visible in the custom sky", true));
   private final d9 X = this.addSetting(new d9("Star Brightness", "Star field brightness", 0.65, 0.0, 1.0, 0.05));
   private final dh A = this.addSetting(new dh("Clouds", "Show vanilla clouds", false));
   private final dh u = this.addSetting(new dh("Match Fog", "Blend atmospheric fog into the horizon", true));
   private final d9 Y = this.addSetting(new d9("Fog Distance", "Multiplier of vanilla atmospheric distance", 1.0, 0.1, 10.0, 0.1));
   private final d9 L = this.addSetting(new d9("Nebula Brightness", "Overall nebula exposure", 1.0, 0.2, 2.0, 0.05));
   private final d9 d = this.addSetting(new d9("Nebula Saturation", "Nebula color richness", 0.9, 0.0, 1.5, 0.05));
   private final d9 H = this.addSetting(new d9("Nebula Intensity", "Cloud and highlight strength", 0.85, 0.0, 1.5, 0.05));
   private final d9 M = this.addSetting(new d9("Nebula Animation Speed", "Cloud motion multiplier; zero freezes cloud motion", 1.0, 0.0, 3.0, 0.1));
   private final d6 k = this.addSetting(new d6("Nebula Motion", "Flowing clouds or the original slow drift", "Flow", "Flow", "Classic"));
   private final d9 C = this.D("Nebula Flow Strength", "Cloud deformation", 0.35, 0.0, 1.0, 0.05);
   private final d9 i = this.D("Nebula Scale", "Cloud pattern scale", 1.0, 0.5, 2.0, 0.05);
   private final dk h = this.addSetting(new dk("Nebula Color", "Cloud artwork tint", -1));
   private final d9 W = this.addSetting(new d9("Star Density", "Procedural star field density", 0.45, 0.0, 1.0, 0.05));
   private final d9 O = this.addSetting(new d9("Nebula Rotation Speed", "Degrees per second; zero stops rotation", 0.02, 0.0, 0.15, 0.01));
   private final dh V = this.addSetting(new dh("Show Vanilla Sun", "Show Minecraft's sun", false));
   private final dh z = this.addSetting(new dh("Show Vanilla Moon", "Show Minecraft's moon", false));
   private final dh p = this.addSetting(new dh("Horizon Blend", "Subtle fade into the fog palette", true));
   private final d9 v = this.D("Exposure", "Sky brightness", 1.0, 0.25, 2.0, 0.05);
   private final d9 F = this.D("Rotation Speed", "Degrees per second", 0.02, -0.2, 0.2, 0.01);
   private final d6 g = this.addSetting(new d6("Sky Quality", "Procedural noise and caustic detail", "High", "Low", "Medium", "High", "Ultra"));
   private final d9 q = this.D("Caustic Scale", "Size of the fluid light cells", 5.0, 2.0, 10.0, 0.25);
   private final d9 U = this.D("Caustic Intensity", "Brightness of converging light", 0.65, 0.0, 1.5, 0.05);
   private final d9 S = this.D("Caustic Distortion", "Fluid warping strength", 0.8, 0.0, 2.0, 0.1);
   private final dk l = this.addSetting(new dk("Ocean Base Color", "Deep water color", -16635057));
   private final dk a = this.addSetting(new dk("Ocean Highlight Color", "Caustic light color", -12133155));
   private et R;

   private d9 D(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new d9(var1, var2, var3, var5, var7, var9));
   }

   public d6 N() {
      return this.N;
   }

   public List<df<?>> D() {
      return switch (this.h()) {
         case "Nebula" -> List.of(this.L, this.d, this.H, this.k, this.M, this.C, this.i, this.h, this.O);
         case "Ocean" -> List.of(this.q, this.U, this.S, this.l, this.a);
         default -> List.of();
      };
   }

   public List<df<?>> P() {
      return List.of(this.L, this.d, this.H, this.h, this.U, this.l, this.a, this.v);
   }

   public List<df<?>> m() {
      return List.of(this.c, this.k, this.M, this.O, this.F);
   }

   public List<df<?>> N() {
      return List.of(this.o, this.X, this.W, this.A, this.V, this.z, this.p, this.u, this.Y);
   }

   public List<df<?>> M() {
      return List.of(this.C, this.i, this.q, this.S, this.g);
   }

   public String h() {
      return this.N.P();
   }

   public float B() {
      return this.c.P().floatValue();
   }

   public float v() {
      return this.F.P().floatValue();
   }

   public float d() {
      return this.v.P().floatValue();
   }

   public boolean X() {
      return this.p.P();
   }

   public float N() {
      return this.g.N().indexOf(this.g.P());
   }

   public float R() {
      return this.q.P().floatValue();
   }

   public float I() {
      return this.U.P().floatValue();
   }

   public float i() {
      return this.S.P().floatValue();
   }

   public int p() {
      return this.l.P();
   }

   public int X() {
      return this.a.P();
   }

   public boolean K() {
      return this.V.P();
   }

   public boolean f() {
      return this.z.P();
   }

   public boolean U() {
      return !this.N.S("Israel Flag");
   }

   public boolean F() {
      return this.N.S("Nebula");
   }

   public float t() {
      return this.L.P().floatValue();
   }

   public float b() {
      return this.d.P().floatValue();
   }

   public float S() {
      return this.H.P().floatValue();
   }

   public float y() {
      return this.W.P().floatValue();
   }

   public float M() {
      return this.M.P().floatValue();
   }

   public float s() {
      return this.O.P().floatValue();
   }

   public boolean W() {
      return this.k.S("Flow");
   }

   public float A() {
      return this.C.P().floatValue();
   }

   public float h() {
      return this.i.P().floatValue();
   }

   public int x() {
      return this.h.P();
   }

   public CustomSkyModule() {
      super("CustomSky", "Use animated custom skies", Category.VISUALS, -1);
      G = this;

      for (df var4 : new df[]{this.L, this.d, this.H, this.M, this.O, this.k, this.C, this.i, this.h}) {
         var4.H(this::F);
      }

      this.C.H(() -> this.F() && this.W());

      for (df var8 : new df[]{this.q, this.U, this.S, this.l, this.a}) {
         var8.H(() -> this.N.S("Ocean"));
      }

      this.c.H(() -> this.N.S("Ocean"));
      this.F.H(() -> !this.F());
      this.v.H(() -> !this.F());
      this.p.H(() -> !this.F());
      this.g.H(() -> this.N.S("Ocean"));
      this.o.H(this::F);
      this.X.H(() -> this.o.B() && this.o.P());
      this.W.H(this.X::B);
   }

   @Override
   public void onEnable() {
      wy.e();
   }

   public static CustomSkyModule Y() {
      return G;
   }

   public static boolean a() {
      if (G != null && G.isEnabled()) {
         Minecraft var0 = Minecraft.getInstance();
         return G != null
            && G.isOperational()
            && wy.X(G.h())
            && var0.level != null
            && var0.level.dimensionType().skybox() == Skybox.OVERWORLD
            && var0.gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE;
      } else {
         return false;
      }
   }

   private et p(int var1, int var2) {
      if (this.R == null || this.R.zenith() != var1 || this.R.horizon() != var2) {
         this.R = new et(var1, var2);
      }

      return this.R;
   }

   public et P() {
      if (this.N.S("Ocean")) {
         return this.p(this.l.P(), this.l.P());
      } else {
         return this.N.S("Israel Flag") ? this.p(-1, -1) : this.p(this.j(-16251885), this.j(-15265755));
      }
   }

   private int j(int var1) {
      float var2 = var1 >> 16 & 0xFF;
      float var3 = var1 >> 8 & 0xFF;
      float var4 = var1 & 0xFF;
      float var5 = var2 * 0.2126F + var3 * 0.7152F + var4 * 0.0722F;
      float var6 = this.b();
      float var7 = this.t();
      int var8 = (int)Math.clamp((var5 + (var2 - var5) * var6) * var7, 0.0F, 255.0F);
      int var9 = (int)Math.clamp((var5 + (var3 - var5) * var6) * var7, 0.0F, 255.0F);
      int var10 = (int)Math.clamp((var5 + (var4 - var5) * var6) * var7, 0.0F, 255.0F);
      return 0xFF000000 | var8 << 16 | var9 << 8 | var10;
   }

   public float X() {
      return (float)(ty.K() * this.c.P());
   }

   public float G() {
      return !this.N.S("Israel Flag") && this.o.P() ? this.X.P().floatValue() : 0.0F;
   }

   public boolean Q() {
      return this.A.P();
   }

   public boolean k() {
      return this.u.P();
   }

   public float x() {
      return this.Y.P().floatValue();
   }
}
