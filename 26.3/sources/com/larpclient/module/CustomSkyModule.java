package com.larpclient.module;

import com.larpclient._i;
import com.larpclient.hz;
import com.larpclient.pj;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.material.FogType;

public class CustomSkyModule extends Module {
   private static CustomSkyModule s;
   private final o I = this.addSetting(new o("Sky Style", "World sky effect", "Nebula", "Nebula", "Ocean", "Israel Flag"));
   private final b U = this.addSetting(new b("Animation Speed", "Effect motion speed; zero freezes it", 0.5, 0.0, 2.0, 0.1));
   private final m m = this.addSetting(new m("Stars", "Keep a star field visible in the custom sky", true));
   private final b X = this.addSetting(new b("Star Brightness", "Star field brightness", 0.65, 0.0, 1.0, 0.05));
   private final m R = this.addSetting(new m("Clouds", "Show vanilla clouds", false));
   private final m i = this.addSetting(new m("Match Fog", "Blend atmospheric fog into the horizon", true));
   private final b e = this.addSetting(new b("Fog Distance", "Multiplier of vanilla atmospheric distance", 1.0, 0.1, 10.0, 0.1));
   private final b o = this.addSetting(new b("Nebula Brightness", "Overall nebula exposure", 1.0, 0.2, 2.0, 0.05));
   private final b A = this.addSetting(new b("Nebula Saturation", "Nebula color richness", 0.9, 0.0, 1.5, 0.05));
   private final b u = this.addSetting(new b("Nebula Intensity", "Cloud and highlight strength", 0.85, 0.0, 1.5, 0.05));
   private final b B = this.addSetting(new b("Nebula Animation Speed", "Cloud motion multiplier; zero freezes cloud motion", 1.0, 0.0, 3.0, 0.1));
   private final o w = this.addSetting(new o("Nebula Motion", "Flowing clouds or the original slow drift", "Flow", "Flow", "Classic"));
   private final b x = this.a("Nebula Flow Strength", "Cloud deformation", 0.35, 0.0, 1.0, 0.05);
   private final b y = this.a("Nebula Scale", "Cloud pattern scale", 1.0, 0.5, 2.0, 0.05);
   private final h b = this.addSetting(new h("Nebula Color", "Cloud artwork tint", -1));
   private final b D = this.addSetting(new b("Star Density", "Procedural star field density", 0.45, 0.0, 1.0, 0.05));
   private final b N = this.addSetting(new b("Nebula Rotation Speed", "Degrees per second; zero stops rotation", 0.02, 0.0, 0.15, 0.01));
   private final m a = this.addSetting(new m("Show Vanilla Sun", "Show Minecraft's sun", false));
   private final m F = this.addSetting(new m("Show Vanilla Moon", "Show Minecraft's moon", false));
   private final m O = this.addSetting(new m("Horizon Blend", "Subtle fade into the fog palette", true));
   private final b h = this.a("Exposure", "Sky brightness", 1.0, 0.25, 2.0, 0.05);
   private final b G = this.a("Rotation Speed", "Degrees per second", 0.02, -0.2, 0.2, 0.01);
   private final o z = this.addSetting(new o("Sky Quality", "Procedural noise and caustic detail", "High", "Low", "Medium", "High", "Ultra"));
   private final b v = this.a("Caustic Scale", "Size of the fluid light cells", 5.0, 2.0, 10.0, 0.25);
   private final b q = this.a("Caustic Intensity", "Brightness of converging light", 0.65, 0.0, 1.5, 0.05);
   private final b n = this.a("Caustic Distortion", "Fluid warping strength", 0.8, 0.0, 2.0, 0.1);
   private final h T = this.addSetting(new h("Ocean Base Color", "Deep water color", -16635057));
   private final h J = this.addSetting(new h("Ocean Highlight Color", "Caustic light color", -12133155));
   private _i Y;

   private b a(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new b(var1, var2, var3, var5, var7, var9));
   }

   public o Z() {
      return this.I;
   }

   public List<r<?>> g() {
      return switch (this.E()) {
         case "Nebula" -> List.of(this.o, this.A, this.u, this.w, this.B, this.x, this.y, this.b, this.N);
         case "Ocean" -> List.of(this.v, this.q, this.n, this.T, this.J);
         default -> List.of();
      };
   }

   public List<r<?>> u() {
      return List.of(this.o, this.A, this.u, this.b, this.q, this.T, this.J, this.h);
   }

   public List<r<?>> V() {
      return List.of(this.U, this.w, this.B, this.N, this.G);
   }

   public List<r<?>> R() {
      return List.of(this.m, this.X, this.D, this.R, this.a, this.F, this.O, this.i, this.e);
   }

   public List<r<?>> o() {
      return List.of(this.x, this.y, this.v, this.n, this.z);
   }

   public String E() {
      return this.I.getValue();
   }

   public float j() {
      return this.U.getValue().floatValue();
   }

   public float f() {
      return this.G.getValue().floatValue();
   }

   public float T() {
      return this.h.getValue().floatValue();
   }

   public boolean G() {
      return this.O.getValue();
   }

   public float V() {
      return this.z.j().indexOf(this.z.getValue());
   }

   public float x() {
      return this.v.getValue().floatValue();
   }

   public float G() {
      return this.q.getValue().floatValue();
   }

   public float p() {
      return this.n.getValue().floatValue();
   }

   public int P() {
      return this.T.getValue();
   }

   public int f() {
      return this.J.getValue();
   }

   public boolean W() {
      return this.a.getValue();
   }

   public boolean F() {
      return this.F.getValue();
   }

   public boolean k() {
      return !this.I.w("Israel Flag");
   }

   public boolean w() {
      return this.I.w("Nebula");
   }

   public float J() {
      return this.o.getValue().floatValue();
   }

   public float d() {
      return this.A.getValue().floatValue();
   }

   public float X() {
      return this.u.getValue().floatValue();
   }

   public float F() {
      return this.D.getValue().floatValue();
   }

   public float L() {
      return this.B.getValue().floatValue();
   }

   public float l() {
      return this.N.getValue().floatValue();
   }

   public boolean y() {
      return this.w.w("Flow");
   }

   public float k() {
      return this.x.getValue().floatValue();
   }

   public float R() {
      return this.y.getValue().floatValue();
   }

   public int c() {
      return this.b.getValue();
   }

   public CustomSkyModule() {
      super("CustomSky", "Use animated custom skies", Category.VISUALS, -1);
      s = this;

      for (r var4 : new r[]{this.o, this.A, this.u, this.B, this.N, this.w, this.x, this.y, this.b}) {
         var4.a(this::w);
      }

      this.x.a(() -> this.w() && this.y());

      for (r var8 : new r[]{this.v, this.q, this.n, this.T, this.J}) {
         var8.a(() -> this.I.w("Ocean"));
      }

      this.U.a(() -> this.I.w("Ocean"));
      this.G.a(() -> !this.w());
      this.h.a(() -> !this.w());
      this.O.a(() -> !this.w());
      this.z.a(() -> this.I.w("Ocean"));
      this.m.a(this::w);
      this.X.a(() -> this.m.j() && this.m.getValue());
      this.D.a(this.X::j);
   }

   @Override
   public void onEnable() {
      pj.P();
   }

   public static CustomSkyModule p() {
      return s;
   }

   public static boolean W(Skybox var0) {
      return var0 == Skybox.OVERWORLD || var0 == Skybox.END;
   }

   public static boolean C() {
      if (s != null && s.isEnabled()) {
         Minecraft var0 = Minecraft.getInstance();
         return s != null
            && s.isOperational()
            && pj.Q(s.E())
            && var0.level != null
            && W(var0.level.dimensionType().skybox())
            && var0.gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE;
      } else {
         return false;
      }
   }

   private _i O(int var1, int var2) {
      if (this.Y == null || this.Y.zenith() != var1 || this.Y.horizon() != var2) {
         this.Y = new _i(var1, var2);
      }

      return this.Y;
   }

   public _i X() {
      if (this.I.w("Ocean")) {
         return this.O(this.T.getValue(), this.T.getValue());
      } else {
         return this.I.w("Israel Flag") ? this.O(-1, -1) : this.O(this.B(-16251885), this.B(-15265755));
      }
   }

   private int B(int var1) {
      float var2 = var1 >> 16 & 0xFF;
      float var3 = var1 >> 8 & 0xFF;
      float var4 = var1 & 0xFF;
      float var5 = var2 * 0.2126F + var3 * 0.7152F + var4 * 0.0722F;
      float var6 = this.d();
      float var7 = this.J();
      int var8 = (int)Math.clamp((var5 + (var2 - var5) * var6) * var7, 0.0F, 255.0F);
      int var9 = (int)Math.clamp((var5 + (var3 - var5) * var6) * var7, 0.0F, 255.0F);
      int var10 = (int)Math.clamp((var5 + (var4 - var5) * var6) * var7, 0.0F, 255.0F);
      return 0xFF000000 | var8 << 16 | var9 << 8 | var10;
   }

   public float U() {
      return (float)(hz.Q() * this.U.getValue());
   }

   public float O() {
      return !this.I.w("Israel Flag") && this.m.getValue() ? this.X.getValue().floatValue() : 0.0F;
   }

   public boolean r() {
      return this.R.getValue();
   }

   public boolean c() {
      return this.i.getValue();
   }

   public float N() {
      return this.e.getValue().floatValue();
   }
}
