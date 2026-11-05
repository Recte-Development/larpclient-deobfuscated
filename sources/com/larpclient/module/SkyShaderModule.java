package com.larpclient.module;

import com.larpclient.cw;
import com.larpclient.e_;
import com.larpclient.m5;
import com.larpclient.mt;
import com.larpclient.o_;
import com.larpclient.ty;
import com.mojang.blaze3d.pipeline.RenderTarget;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import net.minecraft.world.level.dimension.DimensionType.Skybox;
import net.minecraft.world.level.material.FogType;

public final class SkyShaderModule extends Module {
   private static SkyShaderModule F;
   private final d6 z = this.addSetting(
      new d6(
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
   private final d9 I = this.V("Speed", "Animation speed", 1.2, 0.1, 5.0, 0.1);
   private final d9 h = this.V("Scale", "Pattern scale", 5.0, 1.0, 20.0, 0.5);
   private final d9 U = this.V("Intensity", "Pattern intensity", 0.01, 0.001, 0.05, 0.001);
   private final d9 r = this.V("Alpha", "Sky opacity", 1.0, 0.3, 1.0, 0.05);
   private final dh f = this.addSetting(new dh("Theme Color", "Use the client accent", true));
   private final dk o = this.addSetting(new dk("Custom Color", "Sky tint", -1));
   private final d9 M = this.V("Lightning Rate", "How often lightning strikes", 1.0, 0.25, 3.0, 0.05);
   private final e_ i = new e_();
   private final HashMap<String, o_> C = new HashMap<>();
   private final HashSet<String> g = new HashSet<>();
   private int H = -1;
   private Object t;
   private double S = Double.NaN;
   private String N;
   private boolean n;
   private boolean k;
   private String a;
   private String x;
   private int J;
   private float B;
   private int G;

   public SkyShaderModule() {
      super("Sky Shader", "Show animated sky effects", Category.VISUALS, -1);
      F = this;
      this.o.H(() -> !this.f.P());
      this.h.H(() -> !this.M() && !this.z.S("BlackHole"));
      this.U.H(() -> !this.M());
      this.r.H(() -> !this.M());
      this.M.H(() -> this.z.S("Thunderstorm") || this.z.S("Volcano"));
   }

   private d9 V(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new d9(var1, var2, var3, var5, var7, var9));
   }

   public static SkyShaderModule U() {
      return F;
   }

   public d6 p() {
      return this.z;
   }

   public List<df<?>> E() {
      return List.of(this.f, this.o, this.U, this.r);
   }

   public List<df<?>> x() {
      return List.of(this.I, this.h, this.M);
   }

   private boolean M() {
      return this.z.S("Core Caustic")
         || this.z.S("Core Nebula")
         || this.z.S("Sunset")
         || this.z.S("Thunderstorm")
         || this.z.S("Volcano")
         || this.z.S("Sandstorm")
         || this.z.S("Christmas");
   }

   public int i() {
      return this.f.P() ? com.larpclient.f.C : this.o.P();
   }

   public float V() {
      return this.I.P().floatValue();
   }

   public float g() {
      return this.h.P().floatValue();
   }

   public float q() {
      return this.U.P().floatValue();
   }

   public float K() {
      return this.M.P().floatValue();
   }

   public float w() {
      return this.M() ? 1.0F : this.r.P().floatValue();
   }

   public int e() {
      String var1 = this.z.P();
      int var2 = this.i();
      float var3 = this.q();
      if (!var1.equals(this.x) || var2 != this.J || var3 != this.B) {
         this.x = var1;
         this.J = var2;
         this.B = var3;
         this.G = cw.u(var1, var2, var3);
      }

      return this.G;
   }

   public boolean w() {
      if (!this.isEnabled()) {
         return false;
      }

      CustomSkyModule var1 = CustomSkyModule.Y();
      return this.isRuntimeActive()
         && this.isInGame()
         && (var1 == null || !var1.isOperational())
         && getMc().level.dimensionType().skybox() == Skybox.OVERWORLD
         && getMc().gameRenderer.mainCamera().getFluidInCamera() == FogType.NONE
         && (this.H != m5.k || !this.g.contains(this.z.P()));
   }

   public void b() {
      this.k = (boolean)0;
   }

   public boolean d() {
      return !this.isEnabled() ? false : this.k && this.w() && this.z.P().equals(this.a);
   }

   @Override
   public void onEnable() {
      this.m();
      mt.K("SkyShader", "Enabled; mode={}; CustomSky takes precedence when enabled", this.z.P());
   }

   @Override
   public void onDisable() {
      this.m();
   }

   @Override
   public void onTick() {
      if (this.t != getMc().level) {
         this.m();
         this.t = getMc().level;
      }

      CustomSkyModule var1 = CustomSkyModule.Y();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      boolean var4 = false;
      int var3 = var2;
      int var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         boolean var6 = false;
         byte var5 = this.n;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            mt.K("SkyShader", "CustomSky is enabled; Larp sky is suspended without changing either preference");
         }
      }

      this.n = (boolean)var2;
      boolean var8 = false;
      int var7 = var2;
      var10000 = Integer.signum(var7);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         this.Z();
         this.S = Double.NaN;
      }
   }

   public boolean z(RenderTarget var1) {
      if (this.isEnabled()) {
         this.k = (boolean)0;
         if (!this.w()) {
            return false;
         }

         String var2 = this.z.P();

         try {
            if (this.H != m5.k) {
               this.m();
               this.H = m5.k;
               this.t = getMc().level;
            }

            if (!var2.equals(this.N)) {
               this.S = Double.NaN;
               this.N = var2;
               mt.K("SkyShader", "Rendering Larp shader {}; full-screen coverage, vanilla celestial/dark-disc/cloud suppression after success", var2);
            }
            String var3 = switch (var2) {
               case "Core Caustic" -> "core_caustic";
               case "Core Nebula" -> "core_nebula";
               default -> var2.toLowerCase(Locale.ROOT);
            };
            o_ var13 = this.C.computeIfAbsent(var2, var2x -> new o_(var3, this));
            double var14 = ty.K();
            this.i.R = Double.isNaN(this.S) ? 0.0 : Math.clamp(var14 - this.S, 0.0, 0.1);
            this.S = var14;
            this.i.F = var1;
            var13.render(this.i);
            this.k = (boolean)1;
            this.a = var2;
            return true;
         } catch (RuntimeException | IOException var11) {
            this.g.add(var2);
            this.Z();
            mt.P("SkyShader", var2 + " failed; vanilla sky until resource reload or disable/enable", var11);
            return false;
         } finally {
            this.i.F = null;
         }
      } else {
         return false;
      }
   }

   private void Z() {
      for (o_ var2 : this.C.values()) {
         var2.close();
      }

      this.i.t.close();
   }

   public void m() {
      this.k = (boolean)0;
      this.a = null;
      this.Z();
      this.C.clear();
      this.g.clear();
      this.H = -1;
      this.t = null;
      this.N = null;
      this.S = Double.NaN;
      this.n = (boolean)0;
   }
}
