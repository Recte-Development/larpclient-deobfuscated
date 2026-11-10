package com.larpclient.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.cf;
import com.larpclient.gl;
import com.larpclient.gm;
import com.larpclient.hh;
import com.larpclient.ho;
import com.larpclient.j4;
import com.larpclient.kj;
import com.larpclient.ky;
import com.larpclient.me;
import com.larpclient.sf;
import com.larpclient.uh;
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
   private static ChinaHatModule m;
   private final s K = this.addSetting(new s("Cone", "Wear the cone hat", true));
   private final s e = this.addSetting(new s("Kippah", "Wear the kippah", false));
   private final s Z = this.addSetting(new s("Crown", "Wear the crown", false));
   private final s J = this.addSetting(new s("Halo", "Wear the halo", false));
   private final s h = this.addSetting(new s("Horns", "Wear the fiery horns", false));
   private final s A = this.addSetting(new s("Dominus", "Wear the Dominus hood", false));
   private final com.larpclient.module.i s = this.addSetting(new com.larpclient.module.i("Kippah Color", "Skullcap fabric color", -15258800));
   private final com.larpclient.module.i n = this.addSetting(new com.larpclient.module.i("Star Color", "Star of David embroidery", -7517));
   private final e d = this.addSetting(S("Kippah Size", "Skullcap radius in blocks", 0.22, 0.15, 0.3, 0.01));
   private final e u = this.addSetting(S("Kippah Height", "Rounded dome height in blocks", 0.08, 0.03, 0.14, 0.01));
   private final e D = this.addSetting(new e("Kippah Offset", "Clearance above the head in blocks", 0.005, 0.0, 0.1, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final e I = this.addSetting(S("Kippah Opacity", "Kippah fabric transparency", 1.0, 0.1, 1.0, 0.05));
   private final e g = this.addSetting(S("Accessory Size", "Crown, halo, horns or Dominus scale", 1.0, 0.6, 1.4, 0.05));
   private final e f = this.addSetting(S("Accessory Opacity", "Crown or halo transparency", 1.0, 0.1, 1.0, 0.05));
   private final s X = this.addSetting(new s("Self", "Show your hat in third person", true));
   private final s o = this.addSetting(new s("Other Players", "Local preview on nearby players; never advertises their hats", false));
   private final s r = this.addSetting(new s("Theme Colors", "Use the current theme gradient", true));
   private final com.larpclient.module.i V = this.addSetting(new com.larpclient.module.i("Top Color", "Cone tip color", -3429889));
   private final com.larpclient.module.i c = this.addSetting(new com.larpclient.module.i("Rim Color", "Brim color", -8890948));
   private final e L = this.addSetting(new e("Radius", "Brim radius in blocks", 0.6, 0.25, 1.0, 0.05));
   private final e i = this.addSetting(new e("Height", "Cone height in blocks", 0.3, 0.1, 0.7, 0.05));
   private final e S = this.addSetting(new e("Offset", "Height above the head", 0.04, 0.0, 0.5, 0.01));
   private final e Y = this.addSetting(new e("Opacity", "Cone transparency", 0.65, 0.1, 1.0, 0.05));
   private final s y = this.addSetting(new s("Outline", "Fine outline on the cone brim and kippah hem", true));
   private final com.larpclient.module.i N = this.addSetting(new com.larpclient.module.i("Horn Color", "Horn and thorn color", -61184));
   private final s B = this.addSetting(new s("Horn Theme", "Horns use the theme's second color", false));
   private final com.larpclient.module.i b = this.addSetting(new com.larpclient.module.i("Fire Color", "Outer flames and the horn tips", -42752));
   private final com.larpclient.module.i j = this.addSetting(
      new com.larpclient.module.i("Fire Core Color", "Bright inner flames and the hottest tip points", -2304)
   );
   private final s R = this.addSetting(new s("Fire Theme", "Fire uses the theme accent", false));
   private final e E = this.addSetting(S("Fire Size", "Width and height of the fire between the horns", 1.0, 0.5, 2.0, 0.05));
   private final e w = this.addSetting(S("Fire Heat", "How fast and high the flames rise", 1.0, 0.5, 2.0, 0.05));
   private final com.larpclient.module.i t = this.addSetting(
      new com.larpclient.module.i("Dominus Color", "Tints the hood and wings; white keeps the original shading", -1)
   );
   private final List<ChinaHatModule.z> O = new ArrayList<>();
   private List<ChinaHatModule.d> z = List.of();
   private ClientLevel k;
   private final Map<Integer, ky> l = new HashMap<>();
   private final Set<Integer> W = new HashSet<>();
   private long q;
   private long p = -1L;

   public ChinaHatModule() {
      super("China Hat", "Wear any mix of cone, kippah, crown, halo, fiery horns and Dominus", Category.VISUALS, -1);
      m = this;

      for (q var4 : new q[]{this.s, this.n, this.d, this.u, this.D, this.I}) {
         var4.P(this.e::getValue);
      }

      for (q var14 : new q[]{this.r, this.L, this.i, this.S}) {
         var14.P(this.K::getValue);
      }

      this.r.h("Cone Theme");
      this.L.h("Cone Radius");
      this.i.h("Cone Height");
      this.S.h("Cone Offset");
      this.g.P(() -> this.Z.getValue() || this.J.getValue() || this.h.getValue() || this.A.getValue());
      this.t.P(this.A::getValue);
      this.f.P(() -> this.Z.getValue() || this.J.getValue());
      this.Y.P(this.K::getValue);
      this.y.P(() -> this.K.getValue() || this.e.getValue());

      for (q var15 : new q[]{this.B, this.R, this.E, this.w}) {
         var15.P(this.h::getValue);
      }

      this.N.P(() -> this.h.getValue() && !this.B.getValue());

      for (q var16 : new q[]{this.b, this.j}) {
         var16.P(() -> this.h.getValue() && !this.R.getValue());
      }

      this.V.P(() -> this.K.getValue() && !this.r.getValue());
      this.c.P(() -> this.K.getValue() && !this.r.getValue());
   }

   @Override
   public String getDisplayName() {
      return "Hats";
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("China Hat");
   }

   public static void e(JsonObject var0) {
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

   private static e S(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new e(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      };
   }

   private List<gm.y> o() {
      if (this.Z.getValue()) {
         return this.J.getValue() ? List.of(gm.y.k, gm.y.d) : List.of(gm.y.k);
      } else {
         return this.J.getValue() ? List.of(gm.y.d) : List.of();
      }
   }

   private ChinaHatModule.i J() {
      int var1 = this.R.getValue() ? ho.J : this.b.getValue();
      return new ChinaHatModule.i(
         this.B.getValue() ? ho.ds : this.N.getValue(), var1, this.R.getValue() ? sf.J(ho.J) : this.j.getValue(), this.E.getValue(), this.w.getValue()
      );
   }

   public List<q<?>> B() {
      return List.of(this.K, this.e, this.Z, this.J, this.h, this.A);
   }

   public q<?> o() {
      return this.X;
   }

   public static ChinaHatModule N() {
      return m;
   }

   @Override
   public void onEnable() {
      com.larpclient.h.A(
         "ChinaHat",
         "Cosmetic enabled; hats={}, cached geometry, depth tested; Kippah, Crown, Halo, Horns and Dominus follow vanilla head pose",
         this.B().stream().filter(var0 -> Boolean.TRUE.equals(var0.getValue())).map(q::e).toList()
      );
   }

   private JsonObject G() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("version", 1);
      var1.addProperty("enabled", this.isOperational());
      var1.addProperty("top", this.r.getValue() ? ho.J : this.V.getValue());
      var1.addProperty("rim", this.r.getValue() ? ho.ds : this.c.getValue());
      var1.addProperty("radius", this.L.getValue());
      var1.addProperty("height", this.i.getValue());
      var1.addProperty("offset", this.S.getValue());
      var1.addProperty("opacity", this.Y.getValue());
      var1.addProperty("outline", this.y.getValue());
      return var1;
   }

   public List<ChinaHatModule.k> v() {
      if (this.isOperational() && this.X.getValue()) {
         ArrayList var1 = new ArrayList(6);
         if (this.h.getValue()) {
            double var2 = this.g.getValue();
            double var4 = (0.24F + ky.s(1.0, this.w.getValue()) * this.E.getValue()) * var2;
            var1.add(new ChinaHatModule.k(false, var2, Math.max(sf.X(var2), var4), 0.0, -1, 0, false, null, this.J()));
         }

         if (this.A.getValue()) {
            var1.add(new ChinaHatModule.k(false, this.g.getValue(), me.E(this.g.getValue()), 0.0, this.t.getValue() | 0xFF000000, 0, false, null, null, true));
         }

         for (gm.y var3 : this.o()) {
            var1.add(new ChinaHatModule.k(false, this.g.getValue(), gm.z(var3, this.g.getValue()), 0.0, uh.H(16777215, this.f.getValue()), 0, false, var3));
         }

         if (this.e.getValue()) {
            var1.add(
               new ChinaHatModule.k(
                  true,
                  this.d.getValue(),
                  this.u.getValue(),
                  this.D.getValue(),
                  uh.H(this.s.getValue(), this.I.getValue()),
                  uh.H(this.n.getValue(), this.I.getValue()),
                  this.y.getValue()
               )
            );
         }

         double var7 = this.Y.getValue();
         if (this.K.getValue()) {
            var1.add(
               new ChinaHatModule.k(
                  false,
                  this.L.getValue(),
                  this.i.getValue(),
                  this.S.getValue(),
                  uh.H(this.r.getValue() ? ho.J : this.V.getValue(), var7),
                  uh.H(this.r.getValue() ? ho.ds : this.c.getValue(), var7),
                  this.y.getValue()
               )
            );
         }

         return var1;
      } else {
         return List.of();
      }
   }

   public List<q<?>> e() {
      return List.of(this.X, this.o);
   }

   public List<q<?>> q() {
      return List.of(this.r, this.V, this.c, this.s, this.n, this.I, this.B, this.N, this.R, this.b, this.j, this.t, this.Y, this.f, this.y, this.E, this.w);
   }

   public List<q<?>> Q() {
      return List.of(this.L, this.i, this.S, this.d, this.u, this.D, this.g);
   }

   @Override
   public void onDisable() {
      this.z = List.of();
      this.k = null;
      this.l.clear();
      this.p = -1L;
      this.W.clear();
      this.O.clear();
   }

   public boolean E(int var1) {
      return !this.isEnabled() ? false : this.isOperational() && this.W.contains(var1);
   }

   public void i(float var1) {
      if (this.isEnabled()) {
         if (this.k != null && this.k != mc.level) {
            this.l.clear();
         }

         this.z = List.of();
         this.k = null;
         this.p = -1L;
         this.W.clear();
         if (!this.isInGame()) {
            this.l.clear();
         } else {
            ArrayList var2 = new ArrayList();
            JsonObject var3 = this.G();
            var3.addProperty("enabled", this.isEnabled());
            List var4 = this.o();
            sf.a var5 = this.h.getValue() ? sf.m() : null;
            ChinaHatModule.i var6 = var5 == null ? null : this.J();
            int var7 = this.A.getValue() && me.v() != null ? 1 : 0;
            int var8 = var4.size() + (var5 != null ? 1 : 0) + (var7 != 0 ? 1 : 0) + (this.e.getValue() ? 1 : 0);
            int var9 = 0;

            for (AbstractClientPlayer var11 : mc.level.players()) {
               JsonObject var12 = var3;
               if ((
                     var11 == mc.player
                        ? this.isOperational() && this.X.getValue() && !mc.options.getCameraType().isFirstPerson()
                        : this.isOperational() && this.o.getValue()
                  )
                  && var12.get("enabled").getAsBoolean()) {
                  int var13 = uh.H(var12.get("top").getAsInt(), var12.get("opacity").getAsDouble());
                  int var14 = uh.H(var12.get("rim").getAsInt(), var12.get("opacity").getAsDouble());
                  if (var11.isAlive()
                     && !var11.isInvisible()
                     && !var11.isSpectator()
                     && !var11.isSwimming()
                     && !var11.isFallFlying()
                     && !var11.isVisuallyCrawling()
                     && !var11.isSleeping()
                     && !(var11.distanceToSqr(mc.player) > 2304.0)) {
                     int var15 = var8 <= 0 || var11 != mc.player && var9 + var8 > 16 ? 0 : 1;
                     if (var15 != 0 || this.K.getValue()) {
                        if (var15 != 0 && var11 != mc.player) {
                           var9 += var8;
                        }

                        EntityRenderState var16 = j4.d(var11, var1);
                        double var17 = 0.28 + 0.72 * mc.level.getRawBrightness(BlockPos.containing(var11.getEyePosition(var1)), mc.level.getSkyDarken()) / 15.0;
                        if (var15 != 0) {
                           if (var5 != null) {
                              ky var19 = this.l.computeIfAbsent(var11.getId(), var0 -> new ky(var0.intValue()));
                              var2.add(new ChinaHatModule.d(false, null, var16, this.g.getValue(), 0.0, 0.0, -1, 0, false, null, var6, var19, false, var17));
                           }

                           if (var7 != 0) {
                              var2.add(
                                 new ChinaHatModule.d(
                                    false, null, var16, this.g.getValue(), 0.0, 0.0, this.t.getValue() | 0xFF000000, 0, false, null, null, null, true, var17
                                 )
                              );
                              this.W.add(var11.getId());
                           }

                           for (gm.y var20 : var4) {
                              var2.add(
                                 new ChinaHatModule.d(
                                    false,
                                    null,
                                    var16,
                                    this.g.getValue(),
                                    0.0,
                                    0.0,
                                    uh.H(16777215, this.f.getValue()),
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

                           if (this.e.getValue()) {
                              double var22 = this.I.getValue();
                              var2.add(
                                 new ChinaHatModule.d(
                                    true,
                                    null,
                                    var16,
                                    this.d.getValue(),
                                    this.u.getValue(),
                                    this.D.getValue(),
                                    uh.H(this.s.getValue(), var22),
                                    uh.H(this.n.getValue(), var22),
                                    this.y.getValue(),
                                    var17
                                 )
                              );
                           }
                        }

                        if (this.K.getValue()) {
                           Vec3 var23 = var11.getPosition(var1).add(0.0, var11.getBbHeight() + var12.get("offset").getAsDouble(), 0.0);
                           var2.add(
                              new ChinaHatModule.d(
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

            this.k = mc.level;
            this.z = List.copyOf(var2);
            this.l.values().removeIf(var1x -> var2.stream().noneMatch(var1xx -> var1xx.fire == var1x));
         }
      }
   }

   public void R(kj var1, float var2) {
      if (this.isEnabled()) {
         this.q++;
         this.O.clear();
         if (this.k == mc.level) {
            var1.blend(gl.c.K);
            double var3 = cf.F();
            byte var5 = 0;

            for (ChinaHatModule.d var7 : this.z) {
               if (var7.dominus) {
                  if (var7.model != null) {
                     var1.n(var7.model, var7.radius, var7.top);
                  }
               } else if (var7.horns != null) {
                  if (var5 != 0 || (var5 = sf.N(var7.horns.horn(), var7.horns.fire(), var7.horns.core())) != 0) {
                     Matrix4f var13 = var7.model == null ? null : var1.f(var7.model, var7.radius);
                     if (var13 != null) {
                        var7.fire.T.set(var13);
                        var7.fire.G = this.p = this.q;
                     }
                  }
               } else if (var7.accessory != null) {
                  if (var7.model != null) {
                     double var8 = (var7.top >>> 24) / 255.0;
                     Matrix4f var10 = var1.w(var7.model, var7.accessory, var7.radius, var8, var7.lit, var3);
                     if (var10 != null && var7.accessory == gm.y.d) {
                        this.O.add(new ChinaHatModule.z(var10, var7.radius, var8));
                        this.p = this.q;
                     }
                  }
               } else if (var7.kippah && var7.model != null) {
                  var1.w(var7.model, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline, var7.lit);
               } else if (!var7.kippah) {
                  var1.c(var7.model, var7.base, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline);
               }
            }

            for (ChinaHatModule.d var12 : this.z) {
               if (var12.fire != null && var12.fire.G == this.q) {
                  var1.L(var12.fire, var12.fire.T, var12.radius, var12.horns.size(), var12.horns.heat(), var12.horns.fire(), var12.horns.core(), var3);
               }
            }
         }
      }
   }

   public boolean d() {
      return !this.isEnabled() ? false : this.isOperational() && this.k == mc.level && this.p == this.q;
   }

   public void u(kj var1, hh var2) {
      if (this.isEnabled()) {
         if (this.d()) {
            double var3 = cf.F();

            for (ChinaHatModule.z var6 : this.O) {
               var1.k(var6.head, var6.scale, var6.opacity, var3);
            }

            for (ChinaHatModule.d var9 : this.z) {
               if (var9.fire != null && var9.fire.G == this.q) {
                  var1.m(var9.fire.T, var9.radius);
               }
            }

            for (ChinaHatModule.d var10 : this.z) {
               if (var10.fire != null && var10.fire.G == this.q) {
                  var1.I(var10.fire, var10.horns.fire(), var10.horns.core(), 0.6);
               }
            }

            var2.K();
         }
      }
   }

   private record d(
      boolean kippah,
      Vec3 base,
      EntityRenderState model,
      double radius,
      double height,
      double offset,
      int top,
      int rim,
      boolean outline,
      gm.y accessory,
      ChinaHatModule.i horns,
      ky fire,
      boolean dominus,
      double lit
   ) {
      d(boolean var1, Vec3 var2, EntityRenderState var3, double var4, double var6, double var8, int var10, int var11, boolean var12, double var13) {
         this(var1, var2, var3, var4, var6, var8, var10, var11, var12, null, null, null, false, var13);
      }
   }

   public record i(int horn, int fire, int core, double size, double heat) {
   }

   public record k(
      boolean kippah, double radius, double height, double offset, int top, int rim, boolean outline, gm.y accessory, ChinaHatModule.i horns, boolean dominus
   ) {
      public k(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
         this(var1, var2, var4, var6, var8, var9, var10, null, null, false);
      }

      public k(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10, gm.y var11) {
         this(var1, var2, var4, var6, var8, var9, var10, var11, null, false);
      }

      public k(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10, gm.y var11, ChinaHatModule.i var12) {
         this(var1, var2, var4, var6, var8, var9, var10, var11, var12, false);
      }

      public double rise() {
         return this.offset + this.height;
      }

      public String label() {
         return this.dominus ? "Dominus" : (this.horns != null ? "Horns" : (this.accessory != null ? this.accessory.W : (this.kippah ? "Kippah" : "Cone")));
      }
   }

   private record z(Matrix4f head, double scale, double opacity) {
   }
}
