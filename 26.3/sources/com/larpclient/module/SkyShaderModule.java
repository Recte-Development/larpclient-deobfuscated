package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.fo;
import com.larpclient.hz;
import com.larpclient.nl;
import com.larpclient.pa;
import com.larpclient.w3;
import com.mojang.blaze3d.pipeline.RenderTarget;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import net.minecraft.world.level.material.FogType;

public final class SkyShaderModule extends Module {
   private static SkyShaderModule P;
   private final o x = this.addSetting(
      new o(
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
   private final b w = this.O("Speed", "Animation speed", 1.2, 0.1, 5.0, 0.1);
   private final b D = this.O("Scale", "Pattern scale", 5.0, 1.0, 20.0, 0.5);
   private final b g = this.O("Intensity", "Pattern intensity", 0.01, 0.001, 0.05, 0.001);
   private final b b = this.O("Alpha", "Sky opacity", 1.0, 0.3, 1.0, 0.05);
   private final m m = this.addSetting(new m("Theme Color", "Use the client accent", true));
   private final h M = this.addSetting(new h("Custom Color", "Sky tint", -1));
   private final b h = this.O("Lightning Rate", "How often lightning strikes", 1.0, 0.25, 3.0, 0.05);
   private final pa C = new pa();
   private final HashMap<String, w3> U = new HashMap<>();
   private final HashSet<String> e = new HashSet<>();
   private int t = -1;
   private Object Q;
   private double y = Double.NaN;
   private String E;
   private boolean T;
   private boolean r;
   private String N;
   private String v;
   private int H;
   private float F;
   private int A;

   public SkyShaderModule() {
      super("Sky Shader", "Show animated sky effects", Category.VISUALS, -1);
      P = this;
      this.M.a(() -> !this.m.getValue());
      this.D.a(() -> !this.k() && !this.x.w("BlackHole"));
      this.g.a(() -> !this.k());
      this.b.a(() -> !this.k());
      this.h.a(() -> this.x.w("Thunderstorm") || this.x.w("Volcano"));
   }

   private b O(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new b(var1, var2, var3, var5, var7, var9));
   }

   public static SkyShaderModule R() {
      return P;
   }

   public o E() {
      return this.x;
   }

   public List<r<?>> A() {
      return List.of(this.m, this.M, this.g, this.b);
   }

   public List<r<?>> f() {
      return List.of(this.w, this.D, this.h);
   }

   private boolean k() {
      return this.x.w("Core Caustic")
         || this.x.w("Core Nebula")
         || this.x.w("Sunset")
         || this.x.w("Thunderstorm")
         || this.x.w("Volcano")
         || this.x.w("Sandstorm")
         || this.x.w("Christmas");
   }

   public int B() {
      return this.m.getValue() ? com.larpclient.c.y : this.M.getValue();
   }

   public float x() {
      return this.w.getValue().floatValue();
   }

   public float v() {
      return this.D.getValue().floatValue();
   }

   public float l() {
      return this.g.getValue().floatValue();
   }

   public float C() {
      return this.h.getValue().floatValue();
   }

   public float T() {
      return this.k() ? 1.0F : this.b.getValue().floatValue();
   }

   public int g() {
      String var1 = this.x.getValue();
      int var2 = this.B();
      float var3 = this.l();
      if (!var1.equals(this.v) || var2 != this.H || var3 != this.F) {
         this.v = var1;
         this.H = var2;
         this.F = var3;
         this.A = fo.v(var1, var2, var3);
      }

      return this.A;
   }

   public boolean X() {
      if (!this.isEnabled()) {
         return false;
      }

      CustomSkyModule var1 = CustomSkyModule.p();
      return this.isRuntimeActive()
         && this.isInGame()
         && (var1 == null || !var1.isOperational())
         && CustomSkyModule.W(getMc().level.dimensionType().skybox())
         && getMc().gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE
         && (this.t != nl.q || !this.e.contains(this.x.getValue()));
   }

   public void k() {
      this.r = (boolean)0;
   }

   public boolean A() {
      return !this.isEnabled() ? false : this.r && this.X() && this.x.getValue().equals(this.N);
   }

   @Override
   public void onEnable() {
      this.c();
      _q.z("SkyShader", "Enabled; mode={}; CustomSky takes precedence when enabled", this.x.getValue());
   }

   @Override
   public void onDisable() {
      this.c();
   }

   @Override
   public void onTick() {
      if (this.Q != getMc().level) {
         this.c();
         this.Q = getMc().level;
      }

      CustomSkyModule var1 = CustomSkyModule.p();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      boolean var4 = false;
      int var3 = var2;
      int var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         boolean var6 = false;
         byte var5 = this.T;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            _q.z("SkyShader", "CustomSky is enabled; Larp sky is suspended without changing either preference");
         }
      }

      this.T = (boolean)var2;
      boolean var8 = false;
      int var7 = var2;
      var10000 = Integer.signum(var7);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         this.W();
         this.y = Double.NaN;
      }
   }

   public boolean z(RenderTarget var1) {
      if (this.isEnabled()) {
         this.r = (boolean)0;
         if (!this.X()) {
            return false;
         }

         String var2 = this.x.getValue();

         try {
            if (this.t != nl.q) {
               this.c();
               this.t = nl.q;
               this.Q = getMc().level;
            }

            if (!var2.equals(this.E)) {
               this.y = Double.NaN;
               this.E = var2;
               _q.z("SkyShader", "Rendering Larp shader {}; full-screen coverage, vanilla celestial/dark-disc/cloud suppression after success", var2);
            }
            String var3 = switch (var2) {
               case "Core Caustic" -> "core_caustic";
               case "Core Nebula" -> "core_nebula";
               default -> var2.toLowerCase(Locale.ROOT);
            };
            w3 var13 = this.U.computeIfAbsent(var2, var2x -> new w3(var3, this));
            double var14 = hz.Q();
            this.C.J = Double.isNaN(this.y) ? 0.0 : Math.clamp(var14 - this.y, 0.0, 0.1);
            this.y = var14;
            this.C.V = var1;
            var13.render(this.C);
            this.r = (boolean)1;
            this.N = var2;
            return true;
         } catch (RuntimeException | IOException var11) {
            this.e.add(var2);
            this.W();
            _q.o("SkyShader", var2 + " failed; vanilla sky until resource reload or disable/enable", var11);
            return false;
         } finally {
            this.C.V = null;
         }
      } else {
         return false;
      }
   }

   private void W() {
      for (w3 var2 : this.U.values()) {
         var2.close();
      }

      this.C.D.close();
   }

   public void c() {
      this.r = (boolean)0;
      this.N = null;
      this.W();
      this.U.clear();
      this.e.clear();
      this.t = -1;
      this.Q = null;
      this.E = null;
      this.y = Double.NaN;
      this.T = (boolean)0;
   }
}
