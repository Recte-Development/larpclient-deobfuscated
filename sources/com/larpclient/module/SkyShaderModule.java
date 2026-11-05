package com.larpclient.module;

import com.larpclient.ea;
import com.larpclient.jk;
import com.larpclient.r4;
import com.larpclient.rv;
import com.larpclient.uq;
import com.larpclient.uw;
import com.mojang.blaze3d.pipeline.RenderTarget;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.material.FogType;

public final class SkyShaderModule extends Module {
   private static SkyShaderModule V;
   private final lp z = this.addSetting(
      new lp(
         "Mode",
         "Larp sky shader",
         "Core Caustic",
         "Water",
         "Caustic",
         "Nebula",
         "Aurora",
         "Starfall",
         "BlackHole",
         "Plasma",
         "Voronoi",
         "Core Caustic",
         "Core Nebula",
         "Sunset",
         "Thunderstorm",
         "Volcano",
         "Sandstorm",
         "Christmas"
      )
   );
   private final lc X = this.b("Speed", "Animation speed", 1.2, 0.1, 5.0, 0.1);
   private final lc l = this.b("Scale", "Pattern scale", 5.0, 1.0, 20.0, 0.5);
   private final lc F = this.b("Intensity", "Pattern intensity", 0.01, 0.001, 0.05, 0.001);
   private final lc T = this.b("Alpha", "Sky opacity", 1.0, 0.3, 1.0, 0.05);
   private final ln m = this.addSetting(new ln("Theme Color", "Use the client accent", true));
   private final lv E = this.addSetting(new lv("Custom Color", "Sky tint", -1));
   private final lc s = this.b("Lightning Rate", "How often lightning strikes", 1.0, 0.25, 3.0, 0.05);
   private final com.larpclient.lb K = new com.larpclient.lb();
   private final HashMap<String, uw> r = new HashMap<>();
   private final HashSet<String> G = new HashSet<>();
   private int U = -1;
   private Object g;
   private double u = Double.NaN;
   private String M;
   private boolean I;
   private boolean a;
   private String c;
   private String P;
   private int h;
   private float x;
   private int S;

   public SkyShaderModule() {
      super("Sky Shader", "Show animated sky effects", Category.VISUALS, -1);
      V = this;
      this.E.x(() -> !this.m.getValue());
      this.l.x(() -> !this.W() && !this.z.i("BlackHole"));
      this.F.x(() -> !this.W());
      this.T.x(() -> !this.W());
      this.s.x(() -> this.z.i("Thunderstorm") || this.z.i("Volcano"));
   }

   private lc b(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lc(var1, var2, var3, var5, var7, var9));
   }

   public static SkyShaderModule B() {
      return V;
   }

   public lp d() {
      return this.z;
   }

   public List<l6<?>> V() {
      return List.of(this.m, this.E, this.F, this.T);
   }

   public List<l6<?>> d() {
      return List.of(this.X, this.l, this.s);
   }

   private boolean W() {
      return this.z.i("Core Caustic")
         || this.z.i("Core Nebula")
         || this.z.i("Sunset")
         || this.z.i("Thunderstorm")
         || this.z.i("Volcano")
         || this.z.i("Sandstorm")
         || this.z.i("Christmas");
   }

   public int B() {
      return this.m.getValue() ? rv.e : this.E.getValue();
   }

   public float J() {
      return this.X.getValue().floatValue();
   }

   public float f() {
      return this.l.getValue().floatValue();
   }

   public float N() {
      return this.F.getValue().floatValue();
   }

   public float n() {
      return this.s.getValue().floatValue();
   }

   public float G() {
      return this.W() ? 1.0F : this.T.getValue().floatValue();
   }

   public int L() {
      String var1 = this.z.getValue();
      int var2 = this.B();
      float var3 = this.N();
      if (!var1.equals(this.P) || var2 != this.h || var3 != this.x) {
         this.P = var1;
         this.h = var2;
         this.x = var3;
         this.S = uq.p(var1, var2, var3);
      }

      return this.S;
   }

   public boolean z() {
      if (!this.isEnabled()) {
         return false;
      }

      CustomSkyModule var1 = CustomSkyModule.Y();
      return this.isRuntimeActive()
         && this.isInGame()
         && (var1 == null || !var1.isOperational())
         && getMc().level.dimensionType().skybox() == Skybox.OVERWORLD
         && getMc().gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE
         && (this.U != jk.v || !this.G.contains(this.z.getValue()));
   }

   public void l() {
      this.a = (boolean)0;
   }

   public boolean Y() {
      return !this.isEnabled() ? false : this.a && this.z() && this.z.getValue().equals(this.c);
   }

   @Override
   public void onEnable() {
      this.S();
      r4.V("SkyShader", "Enabled; mode={}; CustomSky takes precedence when enabled", this.z.getValue());
   }

   @Override
   public void onDisable() {
      this.S();
   }

   @Override
   public void onTick() {
      if (this.g != getMc().level) {
         this.S();
         this.g = getMc().level;
      }

      CustomSkyModule var1 = CustomSkyModule.Y();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      boolean var4 = false;
      int var3 = var2;
      int var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         boolean var6 = false;
         byte var5 = this.I;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            r4.V("SkyShader", "CustomSky is enabled; Larp sky is suspended without changing either preference");
         }
      }

      this.I = (boolean)var2;
      boolean var8 = false;
      int var7 = var2;
      var10000 = Integer.signum(var7);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         this.L();
         this.u = Double.NaN;
      }
   }

   public boolean M(RenderTarget var1) {
      if (this.isEnabled()) {
         this.a = (boolean)0;
         if (!this.z()) {
            return false;
         }

         String var2 = this.z.getValue();

         try {
            if (this.U != jk.v) {
               this.S();
               this.U = jk.v;
               this.g = getMc().level;
            }

            if (!var2.equals(this.M)) {
               this.u = Double.NaN;
               this.M = var2;
               r4.V("SkyShader", "Rendering Larp shader {}; full-screen coverage, vanilla celestial/dark-disc/cloud suppression after success", var2);
            }
            String var3 = switch (var2) {
               case "Core Caustic" -> "core_caustic";
               case "Core Nebula" -> "core_nebula";
               default -> var2.toLowerCase(Locale.ROOT);
            };
            uw var13 = this.r.computeIfAbsent(var2, var2x -> new uw(var3, this));
            double var14 = ea.y();
            this.K.N = Double.isNaN(this.u) ? 0.0 : Math.clamp(var14 - this.u, 0.0, 0.1);
            this.u = var14;
            this.K.x = var1;
            var13.render(this.K);
            this.a = (boolean)1;
            this.c = var2;
            return true;
         } catch (RuntimeException | IOException var11) {
            this.G.add(var2);
            this.L();
            r4.r("SkyShader", var2 + " failed; vanilla sky until resource reload or disable/enable", var11);
            return false;
         } finally {
            this.K.x = null;
         }
      } else {
         return false;
      }
   }

   private void L() {
      for (uw var2 : this.r.values()) {
         var2.close();
      }

      this.K.d.close();
   }

   public void S() {
      this.a = (boolean)0;
      this.c = null;
      this.L();
      this.r.clear();
      this.G.clear();
      this.U = -1;
      this.g = null;
      this.M = null;
      this.u = Double.NaN;
      this.I = (boolean)0;
   }
}
