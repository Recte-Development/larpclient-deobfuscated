package com.larpclient.module;

import com.larpclient.ez;
import com.larpclient.gx;
import com.larpclient.q8;
import com.larpclient.qo;
import com.larpclient.t_;
import com.larpclient.ul;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_276;
import net.minecraft.class_5636;

public final class SkyShaderModule extends Module {
   private static SkyShaderModule f;
   private final li M = this.addSetting(
      new li(
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
   private final lo y = this.P("Speed", "Animation speed", 1.2, 0.1, 5.0, 0.1);
   private final lo C = this.P("Scale", "Pattern scale", 5.0, 1.0, 20.0, 0.5);
   private final lo h = this.P("Intensity", "Pattern intensity", 0.01, 0.001, 0.05, 0.001);
   private final lo Q = this.P("Alpha", "Sky opacity", 1.0, 0.3, 1.0, 0.05);
   private final l3 r = this.addSetting(new l3("Theme Color", "Use the client accent", true));
   private final l0 l = this.addSetting(new l0("Custom Color", "Sky tint", -1));
   private final lo G = this.P("Lightning Rate", "How often lightning strikes", 1.0, 0.25, 3.0, 0.05);
   private final gx j = new gx();
   private final HashMap<String, com.larpclient.a> D = new HashMap<>();
   private final HashSet<String> H = new HashSet<>();
   private int v = -1;
   private Object I;
   private double k = Double.NaN;
   private String U;
   private boolean s;
   private boolean e;
   private String N;
   private String t;
   private int E;
   private float Y;
   private int p;

   public SkyShaderModule() {
      super("Sky Shader", "Show animated sky effects", Category.VISUALS, -1);
      f = this;
      this.l.O(() -> !this.r.getValue());
      this.C.O(() -> !this.H() && !this.M.G("BlackHole"));
      this.h.O(() -> !this.H());
      this.Q.O(() -> !this.H());
      this.G.O(() -> this.M.G("Thunderstorm") || this.M.G("Volcano"));
   }

   private lo P(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lo(var1, var2, var3, var5, var7, var9));
   }

   public static SkyShaderModule y() {
      return f;
   }

   public li i() {
      return this.M;
   }

   public List<lb<?>> q() {
      return List.of(this.r, this.l, this.h, this.Q);
   }

   public List<lb<?>> D() {
      return List.of(this.y, this.C, this.G);
   }

   private boolean H() {
      return this.M.G("Core Caustic")
         || this.M.G("Core Nebula")
         || this.M.G("Sunset")
         || this.M.G("Thunderstorm")
         || this.M.G("Volcano")
         || this.M.G("Sandstorm")
         || this.M.G("Christmas");
   }

   public int r() {
      return this.r.getValue() ? ez.a : this.l.getValue();
   }

   public float B() {
      return this.y.getValue().floatValue();
   }

   public float z() {
      return this.C.getValue().floatValue();
   }

   public float c() {
      return this.h.getValue().floatValue();
   }

   public float S() {
      return this.G.getValue().floatValue();
   }

   public float g() {
      return this.H() ? 1.0F : this.Q.getValue().floatValue();
   }

   public int u() {
      String var1 = this.M.getValue();
      int var2 = this.r();
      float var3 = this.c();
      if (!var1.equals(this.t) || var2 != this.E || var3 != this.Y) {
         this.t = var1;
         this.E = var2;
         this.Y = var3;
         this.p = ul.V(var1, var2, var3);
      }

      return this.p;
   }

   public boolean g() {
      if (!this.isEnabled()) {
         return false;
      }

      CustomSkyModule var1 = CustomSkyModule.M();
      return this.isRuntimeActive()
         && this.isInGame()
         && (var1 == null || !var1.isOperational())
         && CustomSkyModule.z(getMc().field_1687.method_8597().comp_5220())
         && getMc().field_1773.method_19418().method_19334() == class_5636.field_27888
         && (this.v != t_.g || !this.H.contains(this.M.getValue()));
   }

   public void g() {
      this.e = (boolean)0;
   }

   public boolean O() {
      return !this.isEnabled() ? false : this.e && this.g() && this.M.getValue().equals(this.N);
   }

   @Override
   public void onEnable() {
      this.w();
      q8.G("SkyShader", "Enabled; mode={}; CustomSky takes precedence when enabled", this.M.getValue());
   }

   @Override
   public void onDisable() {
      this.w();
   }

   @Override
   public void onTick() {
      if (this.I != getMc().field_1687) {
         this.w();
         this.I = getMc().field_1687;
      }

      CustomSkyModule var1 = CustomSkyModule.M();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      boolean var4 = false;
      int var3 = var2;
      int var10000 = Integer.signum(var3);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         boolean var6 = false;
         byte var5 = this.s;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            q8.G("SkyShader", "CustomSky is enabled; Larp sky is suspended without changing either preference");
         }
      }

      this.s = (boolean)var2;
      boolean var8 = false;
      int var7 = var2;
      var10000 = Integer.signum(var7);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         this.I();
         this.k = Double.NaN;
      }
   }

   public boolean g(class_276 var1) {
      if (this.isEnabled()) {
         this.e = (boolean)0;
         if (!this.g()) {
            return false;
         }

         String var2 = this.M.getValue();

         try {
            if (this.v != t_.g) {
               this.w();
               this.v = t_.g;
               this.I = getMc().field_1687;
            }

            if (!var2.equals(this.U)) {
               this.k = Double.NaN;
               this.U = var2;
               q8.G("SkyShader", "Rendering Larp shader {}; full-screen coverage, vanilla celestial/dark-disc/cloud suppression after success", var2);
            }
            String var3 = switch (var2) {
               case "Core Caustic" -> "core_caustic";
               case "Core Nebula" -> "core_nebula";
               default -> var2.toLowerCase(Locale.ROOT);
            };
            com.larpclient.a var13 = this.D.computeIfAbsent(var2, var2x -> new com.larpclient.a(var3, this));
            double var14 = qo.x();
            this.j.t = Double.isNaN(this.k) ? 0.0 : Math.clamp(var14 - this.k, 0.0, 0.1);
            this.k = var14;
            this.j.X = var1;
            var13.render(this.j);
            this.e = (boolean)1;
            this.N = var2;
            return true;
         } catch (RuntimeException | IOException var11) {
            this.H.add(var2);
            this.I();
            q8.u("SkyShader", var2 + " failed; vanilla sky until resource reload or disable/enable", var11);
            return false;
         } finally {
            this.j.X = null;
         }
      } else {
         return false;
      }
   }

   private void I() {
      for (com.larpclient.a var2 : this.D.values()) {
         var2.close();
      }

      this.j.z.close();
   }

   public void w() {
      this.e = (boolean)0;
      this.N = null;
      this.I();
      this.D.clear();
      this.H.clear();
      this.v = -1;
      this.I = null;
      this.U = null;
      this.k = Double.NaN;
      this.s = (boolean)0;
   }
}
