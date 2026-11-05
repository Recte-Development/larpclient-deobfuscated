package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient._3;
import com.larpclient._6;
import com.larpclient.ki;
import com.larpclient.ml;
import com.larpclient.mt;
import com.larpclient.pf;
import com.larpclient.tj;
import com.larpclient.ty;
import com.larpclient.xh;
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
   private static ChinaHatModule S;
   private final d6 x = this.addSetting(new d6("Mode", "Hat style", "Cone", "Cone", "Kippah", "Crown", "Halo", "Horns"));
   private final dk n = this.addSetting(new dk("Kippah Color", "Skullcap fabric color", -15258800));
   private final dk e = this.addSetting(new dk("Star Color", "Star of David embroidery", -7517));
   private final d9 L = this.addSetting(w("Kippah Size", "Skullcap radius in blocks", 0.22, 0.15, 0.3, 0.01));
   private final d9 X = this.addSetting(w("Kippah Height", "Rounded dome height in blocks", 0.08, 0.03, 0.14, 0.01));
   private final d9 r = this.addSetting(new d9("Kippah Offset", "Clearance above the head in blocks", 0.005, 0.0, 0.1, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.P());
      }
   });
   private final d9 i = this.addSetting(w("Accessory Size", "Crown, halo or horns scale", 1.0, 0.6, 1.4, 0.05));
   private final d9 m = this.addSetting(w("Accessory Opacity", "Crown or halo transparency", 1.0, 0.1, 1.0, 0.05));
   private final dh q = this.addSetting(new dh("Self", "Show your hat in third person", true));
   private final dh P = this.addSetting(new dh("Other Players", "Local preview on nearby players; never advertises their hats", false));
   private final dh H = this.addSetting(new dh("Theme Colors", "Use the current theme gradient", true));
   private final dk c = this.addSetting(new dk("Top Color", "Cone tip color", -3429889));
   private final dk N = this.addSetting(new dk("Rim Color", "Brim color", -8890948));
   private final d9 s = this.addSetting(new d9("Radius", "Brim radius in blocks", 0.6, 0.25, 1.0, 0.05));
   private final d9 z = this.addSetting(new d9("Height", "Cone height in blocks", 0.3, 0.1, 0.7, 0.05));
   private final d9 Y = this.addSetting(new d9("Offset", "Height above the head", 0.04, 0.0, 0.5, 0.01));
   private final d9 R = this.addSetting(new d9("Opacity", "Hat transparency", 0.65, 0.1, 1.0, 0.05));
   private final dh G = this.addSetting(new dh("Outline", "Fine brim outline", true));
   private final dk U = this.addSetting(new dk("Horn Color", "Horn and thorn color", -61184));
   private final dh O = this.addSetting(new dh("Horn Theme", "Horns use the theme's second color", false));
   private final dk l = this.addSetting(new dk("Fire Color", "Outer flames and the horn tips", -42752));
   private final dk Z = this.addSetting(new dk("Fire Core Color", "Bright inner flames and the hottest tip points", -2304));
   private final dh f = this.addSetting(new dh("Fire Theme", "Fire uses the theme accent", false));
   private final d9 p = this.addSetting(w("Fire Size", "Width and height of the fire between the horns", 1.0, 0.5, 2.0, 0.05));
   private final d9 M = this.addSetting(w("Fire Heat", "How fast and high the flames rise", 1.0, 0.5, 2.0, 0.05));
   private List<ChinaHatModule.o> k = List.of();
   private ClientLevel C;
   private final Map<Integer, com.larpclient.db> K = new HashMap<>();
   private long W;
   private long v = -1L;

   public ChinaHatModule() {
      super("China Hat", "Wear a cone hat, kippah, crown, halo or fiery horns", Category.VISUALS, -1);
      S = this;

      for (df var4 : new df[]{this.n, this.e, this.L, this.X, this.r}) {
         var4.H(() -> this.x.S("Kippah"));
      }

      for (df var17 : new df[]{this.H, this.s, this.z, this.Y}) {
         var17.H(() -> this.x.S("Cone"));
      }

      this.i.H(() -> this.w() != null || this.L());
      this.m.H(() -> this.w() != null);

      for (df var18 : new df[]{this.R, this.G}) {
         var18.H(() -> this.w() == null && !this.L());
      }

      for (df var19 : new df[]{this.O, this.f, this.p, this.M}) {
         var19.H(this::L);
      }

      this.U.H(() -> this.L() && !this.O.P());

      for (df var20 : new df[]{this.l, this.Z}) {
         var20.H(() -> this.L() && !this.f.P());
      }

      this.c.H(() -> this.x.S("Cone") && !this.H.P());
      this.N.H(() -> this.x.S("Cone") && !this.H.P());
   }

   private static d9 w(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new d9(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.P());
         }
      };
   }

   private _6.l w() {
      return this.x.S("Crown") ? _6.l.T : (this.x.S("Halo") ? _6.l.U : null);
   }

   private boolean L() {
      return this.x.S("Horns");
   }

   private ChinaHatModule.z R() {
      int var1 = this.f.P() ? com.larpclient.f.C : this.l.P();
      return new ChinaHatModule.z(
         this.O.P() ? com.larpclient.f.O : this.U.P(), var1, this.f.P() ? ml.E(com.larpclient.f.C) : this.Z.P(), this.p.P(), this.M.P()
      );
   }

   public df<?> t() {
      return this.x;
   }

   public df<?> R() {
      return this.q;
   }

   public static ChinaHatModule i() {
      return S;
   }

   @Override
   public void onEnable() {
      mt.K("ChinaHat", "Cosmetic enabled; mode={}, cached geometry, depth tested; Kippah, Crown, Halo and Horns follow vanilla head pose", this.x.P());
   }

   private JsonObject c() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("version", 1);
      var1.addProperty("enabled", this.isOperational() && this.x.S("Cone"));
      var1.addProperty("top", this.H.P() ? com.larpclient.f.C : this.c.P());
      var1.addProperty("rim", this.H.P() ? com.larpclient.f.O : this.N.P());
      var1.addProperty("radius", this.s.P());
      var1.addProperty("height", this.z.P());
      var1.addProperty("offset", this.Y.P());
      var1.addProperty("opacity", this.R.P());
      var1.addProperty("outline", this.G.P());
      return var1;
   }

   public ChinaHatModule.d w() {
      if (this.isEnabled()) {
         if (!this.isOperational() || !this.q.P()) {
            return null;
         }

         if (this.L()) {
            double var5 = this.i.P();
            double var3 = (0.24F + com.larpclient.db.U(1.0, this.M.P()) * this.p.P()) * var5;
            return new ChinaHatModule.d(false, var5, Math.max(ml.B(var5), var3), 0.0, -1, 0, false, null, this.R());
         }

         _6.l var1 = this.w();
         if (var1 != null) {
            return new ChinaHatModule.d(false, this.i.P(), _6.V(var1, this.i.P()), 0.0, _3.A(16777215, this.m.P()), 0, false, var1);
         }

         double var2 = this.R.P();
         return this.x.S("Kippah")
            ? new ChinaHatModule.d(true, this.L.P(), this.X.P(), this.r.P(), _3.A(this.n.P(), var2), _3.A(this.e.P(), var2), this.G.P())
            : new ChinaHatModule.d(
               false,
               this.s.P(),
               this.z.P(),
               this.Y.P(),
               _3.A(this.H.P() ? com.larpclient.f.C : this.c.P(), var2),
               _3.A(this.H.P() ? com.larpclient.f.O : this.N.P(), var2),
               this.G.P()
            );
      } else {
         return null;
      }
   }

   public List<df<?>> D() {
      return List.of(this.q, this.P);
   }

   public List<df<?>> G() {
      return List.of(this.x, this.H, this.c, this.N, this.n, this.e, this.O, this.U, this.f, this.l, this.Z, this.R, this.m, this.G, this.p, this.M);
   }

   public List<df<?>> a() {
      return List.of(this.s, this.z, this.Y, this.L, this.X, this.r, this.i);
   }

   @Override
   public void onDisable() {
      this.k = List.of();
      this.C = null;
      this.K.clear();
      this.v = -1L;
   }

   public void w(float var1) {
      if (this.isEnabled()) {
         if (this.C != null && this.C != mc.level) {
            this.K.clear();
         }

         this.k = List.of();
         this.C = null;
         this.v = -1L;
         if (!this.isInGame()) {
            this.K.clear();
         } else {
            ArrayList var2 = new ArrayList();
            JsonObject var3 = this.c();
            var3.addProperty("enabled", this.isEnabled());
            boolean var4 = this.x.S("Kippah");
            _6.l var5 = this.w();
            ml.w var6 = this.L() ? ml.u() : null;
            ChinaHatModule.z var7 = var6 == null ? null : this.R();
            int var8 = 0;

            for (AbstractClientPlayer var10 : mc.level.players()) {
               JsonObject var11 = var3;
               if ((var10 == mc.player ? this.isOperational() && this.q.P() && !mc.options.getCameraType().isFirstPerson() : this.isOperational() && this.P.P())
                  && var11.get("enabled").getAsBoolean()) {
                  int var12 = _3.A(var11.get("top").getAsInt(), var11.get("opacity").getAsDouble());
                  int var13 = _3.A(var11.get("rim").getAsInt(), var11.get("opacity").getAsDouble());
                  if (var10.isAlive()
                     && !var10.isInvisible()
                     && !var10.isSpectator()
                     && !var10.isSwimming()
                     && !var10.isFallFlying()
                     && !var10.isVisuallyCrawling()
                     && !var10.isSleeping()
                     && !(var10.distanceToSqr(mc.player) > 2304.0)) {
                     if (this.L()) {
                        if (var6 != null && (var10 == mc.player || var8++ < 16)) {
                           com.larpclient.db var17 = this.K.computeIfAbsent(var10.getId(), var0 -> new com.larpclient.db(var0.intValue()));
                           var2.add(new ChinaHatModule.o(false, null, tj.s(var10, var1), this.i.P(), 0.0, 0.0, -1, 0, false, null, var7, var17));
                        }
                     } else if (var5 != null) {
                        if (var10 == mc.player || var8++ < 16) {
                           var2.add(
                              new ChinaHatModule.o(false, null, tj.s(var10, var1), this.i.P(), 0.0, 0.0, _3.A(16777215, this.m.P()), 0, false, var5, null, null)
                           );
                        }
                     } else if (var4) {
                        if (var10 == mc.player || var8++ < 16) {
                           double var16 = this.R.P();
                           var2.add(
                              new ChinaHatModule.o(
                                 true,
                                 null,
                                 tj.s(var10, var1),
                                 this.L.P(),
                                 this.X.P(),
                                 this.r.P(),
                                 _3.A(this.n.P(), var16),
                                 _3.A(this.e.P(), var16),
                                 this.G.P()
                              )
                           );
                        }
                     } else {
                        Vec3 var14 = var10.getPosition(var1).add(0.0, var10.getBbHeight() + var11.get("offset").getAsDouble(), 0.0);
                        var2.add(
                           new ChinaHatModule.o(
                              false,
                              var14,
                              tj.s(var10, var1),
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

            this.C = mc.level;
            this.k = List.copyOf(var2);
            this.K.values().removeIf(var1x -> var2.stream().noneMatch(var1xx -> var1xx.fire == var1x));
         }
      }
   }

   public void h(ki var1, float var2) {
      if (this.isEnabled()) {
         this.W++;
         if (this.C == mc.level) {
            var1.blend(pf.x.S);
            double var3 = ty.K();
            byte var5 = 0;

            for (ChinaHatModule.o var7 : this.k) {
               if (var7.horns != null) {
                  if (var5 != 0 || (var5 = ml.k(var7.horns.horn(), var7.horns.fire(), var7.horns.core())) != 0) {
                     Matrix4f var8 = var7.model == null ? null : var1.s(var7.model, var7.radius);
                     if (var8 != null) {
                        var7.fire.f.set(var8);
                        var7.fire.R = this.v = this.W;
                     }
                  }
               } else if (var7.accessory != null) {
                  if (var7.model != null) {
                     var1.d(var7.model, var7.accessory, var7.radius, (var7.top >>> 24) / 255.0);
                  }
               } else if (var7.kippah && var7.model != null) {
                  var1.Q(var7.model, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline);
               } else if (!var7.kippah) {
                  var1.p(var7.model, var7.base, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline);
               }
            }

            for (ChinaHatModule.o var10 : this.k) {
               if (var10.fire != null && var10.fire.R == this.W) {
                  var1.q(var10.fire, var10.fire.f, var10.radius, var10.horns.size(), var10.horns.heat(), var10.horns.fire(), var10.horns.core(), var3);
               }
            }
         }
      }
   }

   public boolean j() {
      return !this.isEnabled() ? false : this.isOperational() && this.C == mc.level && this.v == this.W;
   }

   public void O(ki var1, xh var2) {
      if (this.isEnabled()) {
         if (this.j()) {
            for (ChinaHatModule.o var4 : this.k) {
               if (var4.fire != null && var4.fire.R == this.W) {
                  var1.t(var4.fire.f, var4.radius);
               }
            }

            for (ChinaHatModule.o var6 : this.k) {
               if (var6.fire != null && var6.fire.R == this.W) {
                  var1.Z(var6.fire, var6.horns.fire(), var6.horns.core(), 0.6);
               }
            }

            var2.b();
         }
      }
   }

   public record d(boolean kippah, double radius, double height, double offset, int top, int rim, boolean outline, _6.l accessory, ChinaHatModule.z horns) {
      public d(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
         this(var1, var2, var4, var6, var8, var9, var10, null, null);
      }

      public d(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10, _6.l var11) {
         this(var1, var2, var4, var6, var8, var9, var10, var11, null);
      }

      public double rise() {
         return this.offset + this.height;
      }

      public String label() {
         return this.horns != null ? "Horns" : (this.accessory != null ? this.accessory.c : (this.kippah ? "Kippah" : "Cone"));
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
      _6.l accessory,
      ChinaHatModule.z horns,
      com.larpclient.db fire
   ) {
      o(boolean var1, Vec3 var2, EntityRenderState var3, double var4, double var6, double var8, int var10, int var11, boolean var12) {
         this(var1, var2, var3, var4, var6, var8, var10, var11, var12, null, null, null);
      }
   }

   public record z(int horn, int fire, int core, double size, double heat) {
   }
}
