package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.ea;
import com.larpclient.fk;
import com.larpclient.hw;
import com.larpclient.jp;
import com.larpclient.ju;
import com.larpclient.oh;
import com.larpclient.r4;
import com.larpclient.r6;
import com.larpclient.rv;
import com.larpclient.uv;
import com.larpclient.y2;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class ChinaHatModule extends Module {
   private static ChinaHatModule A;
   private final lp y = this.addSetting(new lp("Mode", "Hat style", "Cone", "Cone", "Kippah", "Crown", "Halo", "Horns"));
   private final lv k = this.addSetting(new lv("Kippah Color", "Skullcap fabric color", -15258800));
   private final lv X = this.addSetting(new lv("Star Color", "Star of David embroidery", -7517));
   private final lc B = this.addSetting(M("Kippah Size", "Skullcap radius in blocks", 0.22, 0.15, 0.3, 0.01));
   private final lc F = this.addSetting(M("Kippah Height", "Rounded dome height in blocks", 0.08, 0.03, 0.14, 0.01));
   private final lc S = this.addSetting(new lc("Kippah Offset", "Clearance above the head in blocks", 0.005, 0.0, 0.1, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final lc z = this.addSetting(M("Accessory Size", "Crown, halo or horns scale", 1.0, 0.6, 1.4, 0.05));
   private final lc t = this.addSetting(M("Accessory Opacity", "Crown or halo transparency", 1.0, 0.1, 1.0, 0.05));
   private final ln h = this.addSetting(new ln("Self", "Show your hat in third person", true));
   private final ln o = this.addSetting(new ln("Other Players", "Local preview on nearby players; never advertises their hats", false));
   private final ln V = this.addSetting(new ln("Theme Colors", "Use the current theme gradient", true));
   private final lv e = this.addSetting(new lv("Top Color", "Cone tip color", -3429889));
   private final lv c = this.addSetting(new lv("Rim Color", "Brim color", -8890948));
   private final lc s = this.addSetting(new lc("Radius", "Brim radius in blocks", 0.6, 0.25, 1.0, 0.05));
   private final lc f = this.addSetting(new lc("Height", "Cone height in blocks", 0.3, 0.1, 0.7, 0.05));
   private final lc n = this.addSetting(new lc("Offset", "Height above the head", 0.04, 0.0, 0.5, 0.01));
   private final lc Q = this.addSetting(new lc("Opacity", "Hat transparency", 0.65, 0.1, 1.0, 0.05));
   private final ln q = this.addSetting(new ln("Outline", "Fine brim outline", true));
   private final lv g = this.addSetting(new lv("Horn Color", "Horn and thorn color", -61184));
   private final ln x = this.addSetting(new ln("Horn Theme", "Horns use the theme's second color", false));
   private final lv P = this.addSetting(new lv("Fire Color", "Outer flames and the horn tips", -42752));
   private final lv L = this.addSetting(new lv("Fire Core Color", "Bright inner flames and the hottest tip points", -2304));
   private final ln r = this.addSetting(new ln("Fire Theme", "Fire uses the theme accent", false));
   private final lc M = this.addSetting(M("Fire Size", "Width and height of the fire between the horns", 1.0, 0.5, 2.0, 0.05));
   private final lc G = this.addSetting(M("Fire Heat", "How fast and high the flames rise", 1.0, 0.5, 2.0, 0.05));
   private List<ChinaHatModule.o> l = List.of();
   private ClientLevel d;
   private final Map<Integer, jp> j = new HashMap<>();
   private long u;
   private long W = -1L;

   public ChinaHatModule() {
      super("China Hat", "Wear a cone hat, kippah, crown, halo or fiery horns", Category.VISUALS, -1);
      A = this;

      for (l6 var4 : new l6[]{this.k, this.X, this.B, this.F, this.S}) {
         var4.x(() -> this.y.i("Kippah"));
      }

      for (l6 var17 : new l6[]{this.V, this.s, this.f, this.n}) {
         var17.x(() -> this.y.i("Cone"));
      }

      this.z.x(() -> this.D() != null || this.h());
      this.t.x(() -> this.D() != null);

      for (l6 var18 : new l6[]{this.Q, this.q}) {
         var18.x(() -> this.D() == null && !this.h());
      }

      for (l6 var19 : new l6[]{this.x, this.r, this.M, this.G}) {
         var19.x(this::h);
      }

      this.g.x(() -> this.h() && !this.x.getValue());

      for (l6 var20 : new l6[]{this.P, this.L}) {
         var20.x(() -> this.h() && !this.r.getValue());
      }

      this.e.x(() -> this.y.i("Cone") && !this.V.getValue());
      this.c.x(() -> this.y.i("Cone") && !this.V.getValue());
   }

   private static lc M(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new lc(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      };
   }

   private hw.j D() {
      return this.y.i("Crown") ? hw.j.Y : (this.y.i("Halo") ? hw.j.f : null);
   }

   private boolean h() {
      return this.y.i("Horns");
   }

   private ChinaHatModule.e I() {
      int var1 = this.r.getValue() ? rv.e : this.P.getValue();
      return new ChinaHatModule.e(
         this.x.getValue() ? rv.GQ : this.g.getValue(), var1, this.r.getValue() ? ju.S(rv.e) : this.L.getValue(), this.M.getValue(), this.G.getValue()
      );
   }

   public l6<?> a() {
      return this.y;
   }

   public l6<?> i() {
      return this.h;
   }

   public static ChinaHatModule J() {
      return A;
   }

   @Override
   public void onEnable() {
      r4.V("ChinaHat", "Cosmetic enabled; mode={}, cached geometry, depth tested; Kippah, Crown, Halo and Horns follow vanilla head pose", this.y.getValue());
   }

   private JsonObject H() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("version", 1);
      var1.addProperty("enabled", this.isOperational() && this.y.i("Cone"));
      var1.addProperty("top", this.V.getValue() ? rv.e : this.e.getValue());
      var1.addProperty("rim", this.V.getValue() ? rv.GQ : this.c.getValue());
      var1.addProperty("radius", this.s.getValue());
      var1.addProperty("height", this.f.getValue());
      var1.addProperty("offset", this.n.getValue());
      var1.addProperty("opacity", this.Q.getValue());
      var1.addProperty("outline", this.q.getValue());
      return var1;
   }

   public ChinaHatModule.j L() {
      if (this.isEnabled()) {
         if (!this.isOperational() || !this.h.getValue()) {
            return null;
         }

         if (this.h()) {
            double var5 = this.z.getValue();
            double var3 = (0.24F + jp.G(1.0, this.G.getValue()) * this.M.getValue()) * var5;
            return new ChinaHatModule.j(false, var5, Math.max(ju.V(var5), var3), 0.0, -1, 0, false, null, this.I());
         }

         hw.j var1 = this.D();
         if (var1 != null) {
            return new ChinaHatModule.j(false, this.z.getValue(), hw.e(var1, this.z.getValue()), 0.0, oh.i(16777215, this.t.getValue()), 0, false, var1);
         }

         double var2 = this.Q.getValue();
         return this.y.i("Kippah")
            ? new ChinaHatModule.j(
               true, this.B.getValue(), this.F.getValue(), this.S.getValue(), oh.i(this.k.getValue(), var2), oh.i(this.X.getValue(), var2), this.q.getValue()
            )
            : new ChinaHatModule.j(
               false,
               this.s.getValue(),
               this.f.getValue(),
               this.n.getValue(),
               oh.i(this.V.getValue() ? rv.e : this.e.getValue(), var2),
               oh.i(this.V.getValue() ? rv.GQ : this.c.getValue(), var2),
               this.q.getValue()
            );
      } else {
         return null;
      }
   }

   public List<l6<?>> B() {
      return List.of(this.h, this.o);
   }

   public List<l6<?>> N() {
      return List.of(this.y, this.V, this.e, this.c, this.k, this.X, this.x, this.g, this.r, this.P, this.L, this.Q, this.t, this.q, this.M, this.G);
   }

   public List<l6<?>> o() {
      return List.of(this.s, this.f, this.n, this.B, this.F, this.S, this.z);
   }

   @Override
   public void onDisable() {
      this.l = List.of();
      this.d = null;
      this.j.clear();
      this.W = -1L;
   }

   public void W(float var1) {
      if (this.isEnabled()) {
         if (this.d != null && this.d != mc.level) {
            this.j.clear();
         }

         this.l = List.of();
         this.d = null;
         this.W = -1L;
         if (!this.isInGame()) {
            this.j.clear();
         } else {
            ArrayList var2 = new ArrayList();
            JsonObject var3 = this.H();
            var3.addProperty("enabled", this.isEnabled());
            boolean var4 = this.y.i("Kippah");
            hw.j var5 = this.D();
            ju.h var6 = this.h() ? ju.k() : null;
            ChinaHatModule.e var7 = var6 == null ? null : this.I();
            int var8 = 0;

            for (AbstractClientPlayer var10 : mc.level.players()) {
               JsonObject var11 = var3;
               if ((
                     var10 == mc.player
                        ? this.isOperational() && this.h.getValue() && !mc.options.getCameraType().isFirstPerson()
                        : this.isOperational() && this.o.getValue()
                  )
                  && var11.get("enabled").getAsBoolean()) {
                  int var12 = oh.i(var11.get("top").getAsInt(), var11.get("opacity").getAsDouble());
                  int var13 = oh.i(var11.get("rim").getAsInt(), var11.get("opacity").getAsDouble());
                  if (var10.isAlive()
                     && !var10.isInvisible()
                     && !var10.isSpectator()
                     && !var10.isSwimming()
                     && !var10.isFallFlying()
                     && !var10.isVisuallyCrawling()
                     && !var10.isSleeping()
                     && !(var10.distanceToSqr(mc.player) > 2304.0)) {
                     if (this.h()) {
                        if (var6 != null && (var10 == mc.player || var8++ < 16)) {
                           jp var17 = this.j.computeIfAbsent(var10.getId(), var0 -> new jp(var0.intValue()));
                           var2.add(new ChinaHatModule.o(false, null, uv.v(var10, var1), this.z.getValue(), 0.0, 0.0, -1, 0, false, null, var7, var17));
                        }
                     } else if (var5 != null) {
                        if (var10 == mc.player || var8++ < 16) {
                           var2.add(
                              new ChinaHatModule.o(
                                 false, null, uv.v(var10, var1), this.z.getValue(), 0.0, 0.0, oh.i(16777215, this.t.getValue()), 0, false, var5, null, null
                              )
                           );
                        }
                     } else if (var4) {
                        if (var10 == mc.player || var8++ < 16) {
                           double var16 = this.Q.getValue();
                           var2.add(
                              new ChinaHatModule.o(
                                 true,
                                 null,
                                 uv.v(var10, var1),
                                 this.B.getValue(),
                                 this.F.getValue(),
                                 this.S.getValue(),
                                 oh.i(this.k.getValue(), var16),
                                 oh.i(this.X.getValue(), var16),
                                 this.q.getValue()
                              )
                           );
                        }
                     } else {
                        Vec3 var14 = var10.getPosition(var1).add(0.0, var10.getBbHeight() + var11.get("offset").getAsDouble(), 0.0);
                        var2.add(
                           new ChinaHatModule.o(
                              false,
                              var14,
                              uv.v(var10, var1),
                              var11.get("radius").getAsDouble(),
                              var11.get("height").getAsDouble(),
                              var11.get("offset").getAsDouble(),
                              var12,
                              var13,
                              var11.get("outline").getAsBoolean()
                           )
                        );
                     }
                  }
               }
            }

            this.d = mc.level;
            this.l = List.copyOf(var2);
            this.j.values().removeIf(var1x -> var2.stream().noneMatch(var1xx -> var1xx.fire == var1x));
         }
      }
   }

   public void G(r6 var1, float var2) {
      if (this.isEnabled()) {
         this.u++;
         if (this.d == mc.level) {
            var1.blend(fk.h.z);
            double var3 = ea.y();
            byte var5 = 0;

            for (ChinaHatModule.o var7 : this.l) {
               if (var7.horns != null) {
                  if (var5 != 0 || (var5 = ju.F(var7.horns.horn(), var7.horns.fire(), var7.horns.core())) != 0) {
                     Matrix4f var8 = var7.model == null ? null : var1.Z(var7.model, var7.radius);
                     if (var8 != null) {
                        var7.fire.k.set(var8);
                        var7.fire.K = this.W = this.u;
                     }
                  }
               } else if (var7.accessory != null) {
                  if (var7.model != null) {
                     var1.G(var7.model, var7.accessory, var7.radius, (var7.top >>> 24) / 255.0);
                  }
               } else if (var7.kippah && var7.model != null) {
                  var1.m(var7.model, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline);
               } else if (!var7.kippah) {
                  var1.T(var7.model, var7.base, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline);
               }
            }

            for (ChinaHatModule.o var10 : this.l) {
               if (var10.fire != null && var10.fire.K == this.u) {
                  var1.a(var10.fire, var10.fire.k, var10.radius, var10.horns.size(), var10.horns.heat(), var10.horns.fire(), var10.horns.core(), var3);
               }
            }
         }
      }
   }

   public boolean K() {
      return !this.isEnabled() ? false : this.isOperational() && this.d == mc.level && this.W == this.u;
   }

   public void f(r6 var1, y2 var2) {
      if (this.isEnabled()) {
         if (this.K()) {
            for (ChinaHatModule.o var4 : this.l) {
               if (var4.fire != null && var4.fire.K == this.u) {
                  var1.c(var4.fire.k, var4.radius);
               }
            }

            for (ChinaHatModule.o var6 : this.l) {
               if (var6.fire != null && var6.fire.K == this.u) {
                  var1.T(var6.fire, var6.horns.fire(), var6.horns.core(), 0.6);
               }
            }

            var2.v();
         }
      }
   }

   public record e(int horn, int fire, int core, double size, double heat) {
   }

   public record j(boolean kippah, double radius, double height, double offset, int top, int rim, boolean outline, hw.j accessory, ChinaHatModule.e horns) {
      public j(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
         this(var1, var2, var4, var6, var8, var9, var10, null, null);
      }

      public j(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10, hw.j var11) {
         this(var1, var2, var4, var6, var8, var9, var10, var11, null);
      }

      public double rise() {
         return this.offset + this.height;
      }

      public String label() {
         return this.horns != null ? "Horns" : (this.accessory != null ? this.accessory.j : (this.kippah ? "Kippah" : "Cone"));
      }
   }

   private record o(
      boolean kippah,
      Vec3 base,
      EntityRenderState model,
      double radius,
      double height,
      double offset,
      int top,
      int rim,
      boolean outline,
      hw.j accessory,
      ChinaHatModule.e horns,
      jp fire
   ) {
      o(boolean var1, Vec3 var2, EntityRenderState var3, double var4, double var6, double var8, int var10, int var11, boolean var12) {
         this(var1, var2, var3, var4, var6, var8, var10, var11, var12, null, null, null);
      }
   }
}
