package com.larpclient.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient._0;
import com.larpclient._q;
import com.larpclient.dj;
import com.larpclient.fz;
import com.larpclient.hu;
import com.larpclient.hz;
import com.larpclient.np;
import com.larpclient.sb;
import com.larpclient.vv;
import com.larpclient.w7;
import com.larpclient.wh;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class ChinaHatModule extends Module {
   private static ChinaHatModule i;
   private final m G = this.addSetting(new m("Cone", "Wear the cone hat", true));
   private final m v = this.addSetting(new m("Kippah", "Wear the kippah", false));
   private final m S = this.addSetting(new m("Crown", "Wear the crown", false));
   private final m R = this.addSetting(new m("Halo", "Wear the halo", false));
   private final m k = this.addSetting(new m("Horns", "Wear the fiery horns", false));
   private final m T = this.addSetting(new m("Dominus", "Wear the Dominus hood", false));
   private final h C = this.addSetting(new h("Kippah Color", "Skullcap fabric color", -15258800));
   private final h r = this.addSetting(new h("Star Color", "Star of David embroidery", -7517));
   private final b u = this.addSetting(a("Kippah Size", "Skullcap radius in blocks", 0.22, 0.15, 0.3, 0.01));
   private final b M = this.addSetting(a("Kippah Height", "Rounded dome height in blocks", 0.08, 0.03, 0.14, 0.01));
   private final b w = this.addSetting(new b("Kippah Offset", "Clearance above the head in blocks", 0.005, 0.0, 0.1, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final b c = this.addSetting(a("Kippah Opacity", "Kippah fabric transparency", 1.0, 0.1, 1.0, 0.05));
   private final b I = this.addSetting(a("Accessory Size", "Crown, halo, horns or Dominus scale", 1.0, 0.6, 1.4, 0.05));
   private final b N = this.addSetting(a("Accessory Opacity", "Crown or halo transparency", 1.0, 0.1, 1.0, 0.05));
   private final m f = this.addSetting(new m("Self", "Show your hat in third person", true));
   private final m H = this.addSetting(new m("Other Players", "Local preview on nearby players; never advertises their hats", false));
   private final m b = this.addSetting(new m("Theme Colors", "Use the current theme gradient", true));
   private final h g = this.addSetting(new h("Top Color", "Cone tip color", -3429889));
   private final h m = this.addSetting(new h("Rim Color", "Brim color", -8890948));
   private final b Q = this.addSetting(new b("Radius", "Brim radius in blocks", 0.6, 0.25, 1.0, 0.05));
   private final b L = this.addSetting(new b("Height", "Cone height in blocks", 0.3, 0.1, 0.7, 0.05));
   private final b X = this.addSetting(new b("Offset", "Height above the head", 0.04, 0.0, 0.5, 0.01));
   private final b p = this.addSetting(new b("Opacity", "Cone transparency", 0.65, 0.1, 1.0, 0.05));
   private final m D = this.addSetting(new m("Outline", "Fine outline on the cone brim and kippah hem", true));
   private final h s = this.addSetting(new h("Horn Color", "Horn and thorn color", -61184));
   private final m y = this.addSetting(new m("Horn Theme", "Horns use the theme's second color", false));
   private final h a = this.addSetting(new h("Fire Color", "Outer flames and the horn tips", -42752));
   private final h h = this.addSetting(new h("Fire Core Color", "Bright inner flames and the hottest tip points", -2304));
   private final m W = this.addSetting(new m("Fire Theme", "Fire uses the theme accent", false));
   private final b j = this.addSetting(a("Fire Size", "Width and height of the fire between the horns", 1.0, 0.5, 2.0, 0.05));
   private final b E = this.addSetting(a("Fire Heat", "How fast and high the flames rise", 1.0, 0.5, 2.0, 0.05));
   private final h e = this.addSetting(new h("Dominus Color", "Tints the hood and wings; white keeps the original shading", -1));
   private final List<ChinaHatModule.g> Z = new ArrayList<>();
   private List<ChinaHatModule.z> K = List.of();
   private ClientLevel V;
   private final Map<Integer, dj> O = new HashMap<>();
   private final Set<Integer> z = new HashSet<>();
   private long J;
   private long F = -1L;

   public ChinaHatModule() {
      super("China Hat", "Wear any mix of cone, kippah, crown, halo, fiery horns and Dominus", Category.VISUALS, -1);
      i = this;

      for (r var4 : new r[]{this.C, this.r, this.u, this.M, this.w, this.c}) {
         var4.a(this.v::getValue);
      }

      for (r var14 : new r[]{this.b, this.Q, this.L, this.X}) {
         var14.a(this.G::getValue);
      }

      this.b.B("Cone Theme");
      this.Q.B("Cone Radius");
      this.L.B("Cone Height");
      this.X.B("Cone Offset");
      this.I.a(() -> this.S.getValue() || this.R.getValue() || this.k.getValue() || this.T.getValue());
      this.e.a(this.T::getValue);
      this.N.a(() -> this.S.getValue() || this.R.getValue());
      this.p.a(this.G::getValue);
      this.D.a(() -> this.G.getValue() || this.v.getValue());

      for (r var15 : new r[]{this.y, this.W, this.j, this.E}) {
         var15.a(this.k::getValue);
      }

      this.s.a(() -> this.k.getValue() && !this.y.getValue());

      for (r var16 : new r[]{this.a, this.h}) {
         var16.a(() -> this.k.getValue() && !this.W.getValue());
      }

      this.g.a(() -> this.G.getValue() && !this.b.getValue());
      this.m.a(() -> this.G.getValue() && !this.b.getValue());
   }

   @Override
   public String getDisplayName() {
      return "Hats";
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("China Hat");
   }

   public static void Q(JsonObject var0) {
      JsonElement var1 = var0.remove("Mode");
      if (var1 != null && var1.isJsonPrimitive() && var1.getAsJsonPrimitive().isString()) {
         List var2 = List.of("Cone", "Kippah", "Crown", "Halo", "Horns", "Dominus");
         String var3 = var1.getAsString();
         if (var2.contains(var3) && !var2.stream().anyMatch(var0::has)) {
            for (String var5 : var2) {
               var0.addProperty(var5, var5.equals(var3));
            }
         }
      }
   }

   private static b a(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new b(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      };
   }

   private List<sb.q> f() {
      if (this.S.getValue()) {
         return this.R.getValue() ? List.of(sb.q.t, sb.q.f) : List.of(sb.q.t);
      } else {
         return this.R.getValue() ? List.of(sb.q.f) : List.of();
      }
   }

   private ChinaHatModule.q e() {
      int var1 = this.W.getValue() ? com.larpclient.c.y : this.a.getValue();
      return new ChinaHatModule.q(
         this.y.getValue() ? com.larpclient.c.t : this.s.getValue(),
         var1,
         this.W.getValue() ? np.B(com.larpclient.c.y) : this.h.getValue(),
         this.j.getValue(),
         this.E.getValue()
      );
   }

   public List<r<?>> u() {
      return List.of(this.G, this.v, this.S, this.R, this.k, this.T);
   }

   public r<?> S() {
      return this.f;
   }

   public static ChinaHatModule E() {
      return i;
   }

   @Override
   public void onEnable() {
      _q.z(
         "ChinaHat",
         "Cosmetic enabled; hats={}, cached geometry, depth tested; Kippah, Crown, Halo, Horns and Dominus follow vanilla head pose",
         this.u().stream().filter(var0 -> Boolean.TRUE.equals(var0.getValue())).map(r::X).toList()
      );
   }

   private JsonObject c() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("version", 1);
      var1.addProperty("enabled", this.isOperational());
      var1.addProperty("top", this.b.getValue() ? com.larpclient.c.y : this.g.getValue());
      var1.addProperty("rim", this.b.getValue() ? com.larpclient.c.t : this.m.getValue());
      var1.addProperty("radius", this.Q.getValue());
      var1.addProperty("height", this.L.getValue());
      var1.addProperty("offset", this.X.getValue());
      var1.addProperty("opacity", this.p.getValue());
      var1.addProperty("outline", this.D.getValue());
      return var1;
   }

   public List<ChinaHatModule.y> n() {
      if (this.isOperational() && this.f.getValue()) {
         ArrayList var1 = new ArrayList(6);
         if (this.k.getValue()) {
            double var2 = this.I.getValue();
            double var4 = (0.24F + dj.g(1.0, this.E.getValue()) * this.j.getValue()) * var2;
            var1.add(new ChinaHatModule.y(false, var2, Math.max(np.s(var2), var4), 0.0, -1, 0, false, null, this.e()));
         }

         if (this.T.getValue()) {
            var1.add(new ChinaHatModule.y(false, this.I.getValue(), hu.j(this.I.getValue()), 0.0, this.e.getValue() | 0xFF000000, 0, false, null, null, true));
         }

         for (sb.q var3 : this.f()) {
            var1.add(new ChinaHatModule.y(false, this.I.getValue(), sb.t(var3, this.I.getValue()), 0.0, vv.N(16777215, this.N.getValue()), 0, false, var3));
         }

         if (this.v.getValue()) {
            var1.add(
               new ChinaHatModule.y(
                  true,
                  this.u.getValue(),
                  this.M.getValue(),
                  this.w.getValue(),
                  vv.N(this.C.getValue(), this.c.getValue()),
                  vv.N(this.r.getValue(), this.c.getValue()),
                  this.D.getValue()
               )
            );
         }

         double var7 = this.p.getValue();
         if (this.G.getValue()) {
            var1.add(
               new ChinaHatModule.y(
                  false,
                  this.Q.getValue(),
                  this.L.getValue(),
                  this.X.getValue(),
                  vv.N(this.b.getValue() ? com.larpclient.c.y : this.g.getValue(), var7),
                  vv.N(this.b.getValue() ? com.larpclient.c.t : this.m.getValue(), var7),
                  this.D.getValue()
               )
            );
         }

         return var1;
      } else {
         return List.of();
      }
   }

   public List<r<?>> J() {
      return List.of(this.f, this.H);
   }

   public List<r<?>> K() {
      return List.of(this.b, this.g, this.m, this.C, this.r, this.c, this.y, this.s, this.W, this.a, this.h, this.e, this.p, this.N, this.D, this.j, this.E);
   }

   public List<r<?>> r() {
      return List.of(this.Q, this.L, this.X, this.u, this.M, this.w, this.I);
   }

   @Override
   public void onDisable() {
      this.K = List.of();
      this.V = null;
      this.O.clear();
      this.F = -1L;
      this.z.clear();
      this.Z.clear();
   }

   public boolean Q(int var1) {
      return !this.isEnabled() ? false : this.isOperational() && this.z.contains(var1);
   }

   public void f(float var1) {
      if (this.isEnabled()) {
         if (this.V != null && this.V != mc.level) {
            this.O.clear();
         }

         this.K = List.of();
         this.V = null;
         this.F = -1L;
         this.z.clear();
         if (!this.isInGame()) {
            this.O.clear();
         } else {
            ArrayList var2 = new ArrayList();
            JsonObject var3 = this.c();
            var3.addProperty("enabled", this.isEnabled());
            List var4 = this.f();
            np.u var5 = this.k.getValue() ? np.G() : null;
            ChinaHatModule.q var6 = var5 == null ? null : this.e();
            int var7 = this.T.getValue() && hu.j() != null ? 1 : 0;
            int var8 = var4.size() + (var5 != null ? 1 : 0) + (var7 != 0 ? 1 : 0) + (this.v.getValue() ? 1 : 0);
            int var9 = 0;

            for (AbstractClientPlayer var11 : mc.level.players()) {
               JsonObject var12 = var3;
               if ((
                     var11 == mc.player
                        ? this.isOperational() && this.f.getValue() && !mc.options.getCameraType().isFirstPerson()
                        : this.isOperational() && this.H.getValue()
                  )
                  && var12.get("enabled").getAsBoolean()) {
                  int var13 = vv.N(var12.get("top").getAsInt(), var12.get("opacity").getAsDouble());
                  int var14 = vv.N(var12.get("rim").getAsInt(), var12.get("opacity").getAsDouble());
                  if (var11.isAlive()
                     && !var11.isInvisible()
                     && !var11.isSpectator()
                     && !var11.isSwimming()
                     && !var11.isFallFlying()
                     && !var11.isVisuallyCrawling()
                     && !var11.isSleeping()
                     && !(var11.distanceToSqr(mc.player) > 2304.0)) {
                     int var15 = var8 > 0 && (var11 == mc.player || var9 + var8 <= 16) ? 1 : 0;
                     if (var15 != 0 || this.G.getValue()) {
                        if (var15 != 0 && var11 != mc.player) {
                           var9 += var8;
                        }

                        EntityRenderState var16 = w7.u(var11, var1);
                        double var17 = 0.28 + 0.72 * mc.level.getRawBrightness(BlockPos.containing(var11.getEyePosition(var1)), mc.level.getSkyDarken()) / 15.0;
                        if (var15 != 0) {
                           if (var5 != null) {
                              dj var19 = this.O.computeIfAbsent(var11.getId(), var0 -> new dj(var0.intValue()));
                              var2.add(new ChinaHatModule.z(false, null, var16, this.I.getValue(), 0.0, 0.0, -1, 0, false, null, var6, var19, false, var17));
                           }

                           if (var7 != 0) {
                              var2.add(
                                 new ChinaHatModule.z(
                                    false, null, var16, this.I.getValue(), 0.0, 0.0, this.e.getValue() | 0xFF000000, 0, false, null, null, null, true, var17
                                 )
                              );
                              this.z.add(var11.getId());
                           }

                           for (sb.q var20 : var4) {
                              var2.add(
                                 new ChinaHatModule.z(
                                    false,
                                    null,
                                    var16,
                                    this.I.getValue(),
                                    0.0,
                                    0.0,
                                    vv.N(16777215, this.N.getValue()),
                                    0,
                                    false,
                                    var20,
                                    null,
                                    null,
                                    false,
                                    var17
                                 )
                              );
                           }

                           if (this.v.getValue()) {
                              double var22 = this.c.getValue();
                              var2.add(
                                 new ChinaHatModule.z(
                                    true,
                                    null,
                                    var16,
                                    this.u.getValue(),
                                    this.M.getValue(),
                                    this.w.getValue(),
                                    vv.N(this.C.getValue(), var22),
                                    vv.N(this.r.getValue(), var22),
                                    this.D.getValue(),
                                    var17
                                 )
                              );
                           }
                        }

                        if (this.G.getValue()) {
                           Vec3 var23 = var11.getPosition(var1).add(0.0, var11.getBbHeight() + var12.get("offset").getAsDouble(), 0.0);
                           var2.add(
                              new ChinaHatModule.z(
                                 false,
                                 var23,
                                 var16,
                                 var12.get("radius").getAsDouble(),
                                 var12.get("height").getAsDouble(),
                                 var12.get("offset").getAsDouble(),
                                 var13,
                                 var14,
                                 var12.get("outline").getAsBoolean(),
                                 var17
                              )
                           );
                        }
                     }
                  }
               }
            }

            this.V = mc.level;
            this.K = List.copyOf(var2);
            this.O.values().removeIf(var1x -> var2.stream().noneMatch(var1xx -> var1xx.fire == var1x));
         }
      }
   }

   public void j(fz var1, float var2) {
      if (this.isEnabled()) {
         this.J++;
         this.Z.clear();
         if (this.V == mc.level) {
            var1.blend(_0.v.z);
            double var3 = hz.Q();
            byte var5 = 0;

            for (ChinaHatModule.z var7 : this.K) {
               if (var7.dominus) {
                  if (var7.model != null) {
                     var1.t(var7.model, var7.radius, var7.top);
                  }
               } else if (var7.horns != null) {
                  if (var5 != 0 || (var5 = np.I(var7.horns.horn(), var7.horns.fire(), var7.horns.core())) != 0) {
                     Matrix4f var13 = var7.model == null ? null : var1.H(var7.model, var7.radius);
                     if (var13 != null) {
                        var7.fire.A.set(var13);
                        var7.fire.S = this.F = this.J;
                     }
                  }
               } else if (var7.accessory != null) {
                  if (var7.model != null) {
                     double var8 = (var7.top >>> 24) / 255.0;
                     Matrix4f var10 = var1.X(var7.model, var7.accessory, var7.radius, var8, var7.lit, var3);
                     if (var10 != null && var7.accessory == sb.q.f) {
                        this.Z.add(new ChinaHatModule.g(var10, var7.radius, var8));
                        this.F = this.J;
                     }
                  }
               } else if (var7.kippah && var7.model != null) {
                  var1.y(var7.model, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline, var7.lit);
               } else if (!var7.kippah) {
                  var1.b(var7.model, var7.base, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline);
               }
            }

            for (ChinaHatModule.z var12 : this.K) {
               if (var12.fire != null && var12.fire.S == this.J) {
                  var1.q(var12.fire, var12.fire.A, var12.radius, var12.horns.size(), var12.horns.heat(), var12.horns.fire(), var12.horns.core(), var3);
               }
            }
         }
      }
   }

   public boolean d() {
      return !this.isEnabled() ? false : this.isOperational() && this.V == mc.level && this.F == this.J;
   }

   public void Q(fz var1, wh var2) {
      if (this.isEnabled()) {
         if (this.d()) {
            double var3 = hz.Q();

            for (ChinaHatModule.g var6 : this.Z) {
               var1.u(var6.head, var6.scale, var6.opacity, var3);
            }

            for (ChinaHatModule.z var9 : this.K) {
               if (var9.fire != null && var9.fire.S == this.J) {
                  var1.r(var9.fire.A, var9.radius);
               }
            }

            for (ChinaHatModule.z var10 : this.K) {
               if (var10.fire != null && var10.fire.S == this.J) {
                  var1.z(var10.fire, var10.horns.fire(), var10.horns.core(), 0.6);
               }
            }

            var2.S();
         }
      }
   }

   private record g(Matrix4f head, double scale, double opacity) {
   }

   public record q(int horn, int fire, int core, double size, double heat) {
   }

   public record y(
      boolean kippah, double radius, double height, double offset, int top, int rim, boolean outline, sb.q accessory, ChinaHatModule.q horns, boolean dominus
   ) {
      public y(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
         this(var1, var2, var4, var6, var8, var9, var10, null, null, false);
      }

      public y(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10, sb.q var11) {
         this(var1, var2, var4, var6, var8, var9, var10, var11, null, false);
      }

      public y(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10, sb.q var11, ChinaHatModule.q var12) {
         this(var1, var2, var4, var6, var8, var9, var10, var11, var12, false);
      }

      public double rise() {
         return this.offset + this.height;
      }

      public String label() {
         return this.dominus ? "Dominus" : (this.horns != null ? "Horns" : (this.accessory != null ? this.accessory.Z : (this.kippah ? "Kippah" : "Cone")));
      }
   }

   private record z(
      boolean kippah,
      Vec3 base,
      EntityRenderState model,
      double radius,
      double height,
      double offset,
      int top,
      int rim,
      boolean outline,
      sb.q accessory,
      ChinaHatModule.q horns,
      dj fire,
      boolean dominus,
      double lit
   ) {
      z(boolean var1, Vec3 var2, EntityRenderState var3, double var4, double var6, double var8, int var10, int var11, boolean var12, double var13) {
         this(var1, var2, var3, var4, var6, var8, var10, var11, var12, null, null, null, false, var13);
      }
   }
}
