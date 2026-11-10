package com.larpclient.module;

import com.larpclient.cf;
import com.larpclient.ho;
import com.larpclient.k0;
import com.larpclient.wa;
import com.larpclient.wj;
import com.larpclient.zl;
import com.mojang.blaze3d.pipeline.RenderTarget;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import net.minecraft.world.level.material.FogType;

public final class SkyShaderModule extends Module {
   private static SkyShaderModule d;
   private final l C = this.addSetting(
      new l(
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
   private final e x = this.r("Speed", "Animation speed", 1.2, 0.1, 5.0, 0.1);
   private final e N = this.r("Scale", "Pattern scale", 5.0, 1.0, 20.0, 0.5);
   private final e m = this.r("Intensity", "Pattern intensity", 0.01, 0.001, 0.05, 0.001);
   private final e g = this.r("Alpha", "Sky opacity", 1.0, 0.3, 1.0, 0.05);
   private final s t = this.addSetting(new s("Theme Color", "Use the client accent", true));
   private final i a = this.addSetting(new i("Custom Color", "Sky tint", -1));
   private final e E = this.r("Lightning Rate", "How often lightning strikes", 1.0, 0.25, 3.0, 0.05);
   private final wj Q = new wj();
   private final HashMap<String, zl> R = new HashMap<>();
   private final HashSet<String> v = new HashSet<>();
   private int W = -1;
   private Object X;
   private double k = Double.NaN;
   private String w;
   private boolean j;
   private boolean q;
   private String f;
   private String P;
   private int b;
   private float o;
   private int n;

   public SkyShaderModule() {
      super("Sky Shader", "Show animated sky effects", Category.VISUALS, -1);
      d = this;
      this.a.P(() -> !this.t.getValue());
      this.N.P(() -> !this.d() && !this.C.O("BlackHole"));
      this.m.P(() -> !this.d());
      this.g.P(() -> !this.d());
      this.E.P(() -> this.C.O("Thunderstorm") || this.C.O("Volcano"));
   }

   private e r(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new e(var1, var2, var3, var5, var7, var9));
   }

   public static SkyShaderModule t() {
      return d;
   }

   public l S() {
      return this.C;
   }

   public List<q<?>> Y() {
      return List.of(this.t, this.a, this.m, this.g);
   }

   public List<q<?>> i() {
      return List.of(this.x, this.N, this.E);
   }

   private boolean d() {
      return this.C.O("Core Caustic")
         || this.C.O("Core Nebula")
         || this.C.O("Sunset")
         || this.C.O("Thunderstorm")
         || this.C.O("Volcano")
         || this.C.O("Sandstorm")
         || this.C.O("Christmas");
   }

   public int n() {
      return this.t.getValue() ? ho.J : this.a.getValue();
   }

   public float k() {
      return this.x.getValue().floatValue();
   }

   public float r() {
      return this.N.getValue().floatValue();
   }

   public float W() {
      return this.m.getValue().floatValue();
   }

   public float L() {
      return this.E.getValue().floatValue();
   }

   public float O() {
      return this.d() ? 1.0F : this.g.getValue().floatValue();
   }

   public int C() {
      String var1 = this.C.getValue();
      int var2 = this.n();
      float var3 = this.W();
      if (!var1.equals(this.P) || var2 != this.b || var3 != this.o) {
         this.P = var1;
         this.b = var2;
         this.o = var3;
         this.n = k0.J(var1, var2, var3);
      }

      return this.n;
   }

   public boolean O() {
      if (!this.isEnabled()) {
         return false;
      }

      CustomSkyModule var1 = CustomSkyModule.K();
      return this.isRuntimeActive()
         && this.isInGame()
         && (var1 == null || !var1.isOperational())
         && CustomSkyModule.v(getMc().level.dimensionType().skybox())
         && getMc().gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE
         && (this.W != wa.s || !this.v.contains(this.C.getValue()));
   }

   public void p() {
      this.q = (boolean)0;
   }

   public boolean Q() {
      return !this.isEnabled() ? false : this.q && this.O() && this.C.getValue().equals(this.f);
   }

   @Override
   public void onEnable() {
      this.O();
      com.larpclient.h.A("SkyShader", "Enabled; mode={}; CustomSky takes precedence when enabled", this.C.getValue());
   }

   @Override
   public void onDisable() {
      this.O();
   }

   @Override
   public void onTick() {
      if (this.X != getMc().level) {
         this.O();
         this.X = getMc().level;
      }

      CustomSkyModule var1 = CustomSkyModule.K();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      boolean var4 = false;
      int var3 = var2;
      int var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         boolean var6 = false;
         byte var5 = this.j;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            com.larpclient.h.A("SkyShader", "CustomSky is enabled; Larp sky is suspended without changing either preference");
         }
      }

      this.j = (boolean)var2;
      boolean var8 = false;
      int var7 = var2;
      var10000 = Integer.signum(var7);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         this.w();
         this.k = Double.NaN;
      }
   }

   public boolean z(RenderTarget var1) {
      if (this.isEnabled()) {
         this.q = (boolean)0;
         if (!this.O()) {
            return false;
         }

         String var2 = this.C.getValue();

         try {
            if (this.W != wa.s) {
               this.O();
               this.W = wa.s;
               this.X = getMc().level;
            }

            if (!var2.equals(this.w)) {
               this.k = Double.NaN;
               this.w = var2;
               com.larpclient.h.A(
                  "SkyShader", "Rendering Larp shader {}; full-screen coverage, vanilla celestial/dark-disc/cloud suppression after success", var2
               );
            }
            String var3 = switch (var2) {
               case "Core Caustic" -> "core_caustic";
               case "Core Nebula" -> "core_nebula";
               default -> var2.toLowerCase(Locale.ROOT);
            };
            zl var13 = this.R.computeIfAbsent(var2, var2x -> new zl(var3, this));
            double var14 = cf.F();
            this.Q.k = Double.isNaN(this.k) ? 0.0 : Math.clamp(var14 - this.k, 0.0, 0.1);
            this.k = var14;
            this.Q.S = var1;
            var13.render(this.Q);
            this.q = (boolean)1;
            this.f = var2;
            return true;
         } catch (RuntimeException | IOException var11) {
            this.v.add(var2);
            this.w();
            com.larpclient.h.m("SkyShader", var2 + " failed; vanilla sky until resource reload or disable/enable", var11);
            return false;
         } finally {
            this.Q.S = null;
         }
      } else {
         return false;
      }
   }

   private void w() {
      for (zl var2 : this.R.values()) {
         var2.close();
      }

      this.Q.M.close();
   }

   public void O() {
      this.q = (boolean)0;
      this.f = null;
      this.w();
      this.R.clear();
      this.v.clear();
      this.W = -1;
      this.X = null;
      this.w = null;
      this.k = Double.NaN;
      this.j = (boolean)0;
   }
}
