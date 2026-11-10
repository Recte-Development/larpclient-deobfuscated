package com.larpclient.module;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.larpclient.bl;
import com.larpclient.cw;
import com.larpclient.ei;
import com.larpclient.ez;
import com.larpclient.f1;
import com.larpclient.g7;
import com.larpclient.gv;
import com.larpclient.q4;
import com.larpclient.q8;
import com.larpclient.qo;
import com.larpclient.tb;
import com.larpclient.ti;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_10017;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_638;
import net.minecraft.class_742;
import org.joml.Matrix4f;

public final class ChinaHatModule extends Module {
   private static ChinaHatModule h;
   private final l3 P = this.addSetting(new l3("Cone", "Wear the cone hat", true));
   private final l3 g = this.addSetting(new l3("Kippah", "Wear the kippah", false));
   private final l3 D = this.addSetting(new l3("Crown", "Wear the crown", false));
   private final l3 J = this.addSetting(new l3("Halo", "Wear the halo", false));
   private final l3 b = this.addSetting(new l3("Horns", "Wear the fiery horns", false));
   private final l3 m = this.addSetting(new l3("Dominus", "Wear the Dominus hood", false));
   private final l0 n = this.addSetting(new l0("Kippah Color", "Skullcap fabric color", -15258800));
   private final l0 U = this.addSetting(new l0("Star Color", "Star of David embroidery", -7517));
   private final lo s = this.addSetting(S("Kippah Size", "Skullcap radius in blocks", 0.22, 0.15, 0.3, 0.01));
   private final lo z = this.addSetting(S("Kippah Height", "Rounded dome height in blocks", 0.08, 0.03, 0.14, 0.01));
   private final lo v = this.addSetting(new lo("Kippah Offset", "Clearance above the head in blocks", 0.005, 0.0, 0.1, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final lo i = this.addSetting(S("Kippah Opacity", "Kippah fabric transparency", 1.0, 0.1, 1.0, 0.05));
   private final lo H = this.addSetting(S("Accessory Size", "Crown, halo, horns or Dominus scale", 1.0, 0.6, 1.4, 0.05));
   private final lo E = this.addSetting(S("Accessory Opacity", "Crown or halo transparency", 1.0, 0.1, 1.0, 0.05));
   private final l3 e = this.addSetting(new l3("Self", "Show your hat in third person", true));
   private final l3 r = this.addSetting(new l3("Other Players", "Local preview on nearby players; never advertises their hats", false));
   private final l3 N = this.addSetting(new l3("Theme Colors", "Use the current theme gradient", true));
   private final l0 T = this.addSetting(new l0("Top Color", "Cone tip color", -3429889));
   private final l0 O = this.addSetting(new l0("Rim Color", "Brim color", -8890948));
   private final lo B = this.addSetting(new lo("Radius", "Brim radius in blocks", 0.6, 0.25, 1.0, 0.05));
   private final lo t = this.addSetting(new lo("Height", "Cone height in blocks", 0.3, 0.1, 0.7, 0.05));
   private final lo W = this.addSetting(new lo("Offset", "Height above the head", 0.04, 0.0, 0.5, 0.01));
   private final lo o = this.addSetting(new lo("Opacity", "Cone transparency", 0.65, 0.1, 1.0, 0.05));
   private final l3 y = this.addSetting(new l3("Outline", "Fine outline on the cone brim and kippah hem", true));
   private final l0 M = this.addSetting(new l0("Horn Color", "Horn and thorn color", -61184));
   private final l3 C = this.addSetting(new l3("Horn Theme", "Horns use the theme's second color", false));
   private final l0 A = this.addSetting(new l0("Fire Color", "Outer flames and the horn tips", -42752));
   private final l0 p = this.addSetting(new l0("Fire Core Color", "Bright inner flames and the hottest tip points", -2304));
   private final l3 L = this.addSetting(new l3("Fire Theme", "Fire uses the theme accent", false));
   private final lo I = this.addSetting(S("Fire Size", "Width and height of the fire between the horns", 1.0, 0.5, 2.0, 0.05));
   private final lo Q = this.addSetting(S("Fire Heat", "How fast and high the flames rise", 1.0, 0.5, 2.0, 0.05));
   private final l0 u = this.addSetting(new l0("Dominus Color", "Tints the hood and wings; white keeps the original shading", -1));
   private final List<ChinaHatModule.v> S = new ArrayList<>();
   private List<ChinaHatModule.w> R = List.of();
   private class_638 q;
   private final Map<Integer, g7> w = new HashMap<>();
   private final Set<Integer> Z = new HashSet<>();
   private long K;
   private long Y = -1L;

   public ChinaHatModule() {
      super("China Hat", "Wear any mix of cone, kippah, crown, halo, fiery horns and Dominus", Category.VISUALS, -1);
      h = this;

      for (lb var4 : new lb[]{this.n, this.U, this.s, this.z, this.v, this.i}) {
         var4.O(this.g::getValue);
      }

      for (lb var14 : new lb[]{this.N, this.B, this.t, this.W}) {
         var14.O(this.P::getValue);
      }

      this.N.M("Cone Theme");
      this.B.M("Cone Radius");
      this.t.M("Cone Height");
      this.W.M("Cone Offset");
      this.H.O(() -> this.D.getValue() || this.J.getValue() || this.b.getValue() || this.m.getValue());
      this.u.O(this.m::getValue);
      this.E.O(() -> this.D.getValue() || this.J.getValue());
      this.o.O(this.P::getValue);
      this.y.O(() -> this.P.getValue() || this.g.getValue());

      for (lb var15 : new lb[]{this.C, this.L, this.I, this.Q}) {
         var15.O(this.b::getValue);
      }

      this.M.O(() -> this.b.getValue() && !this.C.getValue());

      for (lb var16 : new lb[]{this.A, this.p}) {
         var16.O(() -> this.b.getValue() && !this.L.getValue());
      }

      this.T.O(() -> this.P.getValue() && !this.N.getValue());
      this.O.O(() -> this.P.getValue() && !this.N.getValue());
   }

   @Override
   public String getDisplayName() {
      return "Hats";
   }

   @Override
   public List<String> getSearchAliases() {
      return List.of("China Hat");
   }

   public static void w(JsonObject var0) {
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

   private static lo S(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new lo(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return String.format(Locale.ROOT, "%.2f", this.getValue());
         }
      };
   }

   private List<q4.r> X() {
      if (this.D.getValue()) {
         return this.J.getValue() ? List.of(q4.r.g, q4.r.s) : List.of(q4.r.g);
      } else {
         return this.J.getValue() ? List.of(q4.r.s) : List.of();
      }
   }

   private ChinaHatModule.y B() {
      int var1 = this.L.getValue() ? ez.a : this.A.getValue();
      return new ChinaHatModule.y(
         this.C.getValue() ? ez.f : this.M.getValue(), var1, this.L.getValue() ? bl.G(ez.a) : this.p.getValue(), this.I.getValue(), this.Q.getValue()
      );
   }

   public List<lb<?>> L() {
      return List.of(this.P, this.g, this.D, this.J, this.b, this.m);
   }

   public lb<?> B() {
      return this.e;
   }

   public static ChinaHatModule h() {
      return h;
   }

   @Override
   public void onEnable() {
      q8.G(
         "ChinaHat",
         "Cosmetic enabled; hats={}, cached geometry, depth tested; Kippah, Crown, Halo, Horns and Dominus follow vanilla head pose",
         this.L().stream().filter(var0 -> Boolean.TRUE.equals(var0.getValue())).map(lb::R).toList()
      );
   }

   private JsonObject c() {
      JsonObject var1 = new JsonObject();
      var1.addProperty("version", 1);
      var1.addProperty("enabled", this.isOperational());
      var1.addProperty("top", this.N.getValue() ? ez.a : this.T.getValue());
      var1.addProperty("rim", this.N.getValue() ? ez.f : this.O.getValue());
      var1.addProperty("radius", this.B.getValue());
      var1.addProperty("height", this.t.getValue());
      var1.addProperty("offset", this.W.getValue());
      var1.addProperty("opacity", this.o.getValue());
      var1.addProperty("outline", this.y.getValue());
      return var1;
   }

   public List<ChinaHatModule.a> Y() {
      if (this.isOperational() && this.e.getValue()) {
         ArrayList var1 = new ArrayList(6);
         if (this.b.getValue()) {
            double var2 = this.H.getValue();
            double var4 = (0.24F + g7.s(1.0, this.Q.getValue()) * this.I.getValue()) * var2;
            var1.add(new ChinaHatModule.a(false, var2, Math.max(bl.A(var2), var4), 0.0, -1, 0, false, null, this.B()));
         }

         if (this.m.getValue()) {
            var1.add(new ChinaHatModule.a(false, this.H.getValue(), tb.f(this.H.getValue()), 0.0, this.u.getValue() | 0xFF000000, 0, false, null, null, true));
         }

         for (q4.r var3 : this.X()) {
            var1.add(new ChinaHatModule.a(false, this.H.getValue(), q4.a(var3, this.H.getValue()), 0.0, ei.D(16777215, this.E.getValue()), 0, false, var3));
         }

         if (this.g.getValue()) {
            var1.add(
               new ChinaHatModule.a(
                  true,
                  this.s.getValue(),
                  this.z.getValue(),
                  this.v.getValue(),
                  ei.D(this.n.getValue(), this.i.getValue()),
                  ei.D(this.U.getValue(), this.i.getValue()),
                  this.y.getValue()
               )
            );
         }

         double var7 = this.o.getValue();
         if (this.P.getValue()) {
            var1.add(
               new ChinaHatModule.a(
                  false,
                  this.B.getValue(),
                  this.t.getValue(),
                  this.W.getValue(),
                  ei.D(this.N.getValue() ? ez.a : this.T.getValue(), var7),
                  ei.D(this.N.getValue() ? ez.f : this.O.getValue(), var7),
                  this.y.getValue()
               )
            );
         }

         return var1;
      } else {
         return List.of();
      }
   }

   public List<lb<?>> E() {
      return List.of(this.e, this.r);
   }

   public List<lb<?>> a() {
      return List.of(this.N, this.T, this.O, this.n, this.U, this.i, this.C, this.M, this.L, this.A, this.p, this.u, this.o, this.E, this.y, this.I, this.Q);
   }

   public List<lb<?>> o() {
      return List.of(this.B, this.t, this.W, this.s, this.z, this.v, this.H);
   }

   @Override
   public void onDisable() {
      this.R = List.of();
      this.q = null;
      this.w.clear();
      this.Y = -1L;
      this.Z.clear();
      this.S.clear();
   }

   public boolean n(int var1) {
      return !this.isEnabled() ? false : this.isOperational() && this.Z.contains(var1);
   }

   public void d(float var1) {
      if (this.isEnabled()) {
         if (this.q != null && this.q != mc.field_1687) {
            this.w.clear();
         }

         this.R = List.of();
         this.q = null;
         this.Y = -1L;
         this.Z.clear();
         if (!this.isInGame()) {
            this.w.clear();
         } else {
            ArrayList var2 = new ArrayList();
            JsonObject var3 = this.c();
            var3.addProperty("enabled", this.isEnabled());
            List var4 = this.X();
            bl.a var5 = this.b.getValue() ? bl.q() : null;
            ChinaHatModule.y var6 = var5 == null ? null : this.B();
            int var7 = this.m.getValue() && tb.x() != null ? 1 : 0;
            int var8 = var4.size() + (var5 != null ? 1 : 0) + (var7 != 0 ? 1 : 0) + (this.g.getValue() ? 1 : 0);
            int var9 = 0;

            for (class_742 var11 : mc.field_1687.method_18456()) {
               JsonObject var12 = var3;
               if ((
                     var11 == mc.field_1724
                        ? this.isOperational() && this.e.getValue() && !mc.field_1690.method_31044().method_31034()
                        : this.isOperational() && this.r.getValue()
                  )
                  && var12.get("enabled").getAsBoolean()) {
                  int var13 = ei.D(var12.get("top").getAsInt(), var12.get("opacity").getAsDouble());
                  int var14 = ei.D(var12.get("rim").getAsInt(), var12.get("opacity").getAsDouble());
                  if (var11.method_5805()
                     && !var11.method_5767()
                     && !var11.method_7325()
                     && !var11.method_5681()
                     && !var11.method_6128()
                     && !var11.method_20448()
                     && !var11.method_6113()
                     && !(var11.method_5858(mc.field_1724) > 2304.0)) {
                     int var15 = var8 <= 0 || var11 != mc.field_1724 && var9 + var8 > 16 ? 0 : 1;
                     if (var15 != 0 || this.P.getValue()) {
                        if (var15 != 0 && var11 != mc.field_1724) {
                           var9 += var8;
                        }

                        class_10017 var16 = gv.i(var11, var1);
                        double var17 = 0.28
                           + 0.72 * mc.field_1687.method_22335(class_2338.method_49638(var11.method_5836(var1)), mc.field_1687.method_8594()) / 15.0;
                        if (var15 != 0) {
                           if (var5 != null) {
                              g7 var19 = this.w.computeIfAbsent(var11.method_5628(), var0 -> new g7(var0.intValue()));
                              var2.add(new ChinaHatModule.w(false, null, var16, this.H.getValue(), 0.0, 0.0, -1, 0, false, null, var6, var19, false, var17));
                           }

                           if (var7 != 0) {
                              var2.add(
                                 new ChinaHatModule.w(
                                    false, null, var16, this.H.getValue(), 0.0, 0.0, this.u.getValue() | 0xFF000000, 0, false, null, null, null, true, var17
                                 )
                              );
                              this.Z.add(var11.method_5628());
                           }

                           for (q4.r var20 : var4) {
                              var2.add(
                                 new ChinaHatModule.w(
                                    false,
                                    null,
                                    var16,
                                    this.H.getValue(),
                                    0.0,
                                    0.0,
                                    ei.D(16777215, this.E.getValue()),
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

                           if (this.g.getValue()) {
                              double var22 = this.i.getValue();
                              var2.add(
                                 new ChinaHatModule.w(
                                    true,
                                    null,
                                    var16,
                                    this.s.getValue(),
                                    this.z.getValue(),
                                    this.v.getValue(),
                                    ei.D(this.n.getValue(), var22),
                                    ei.D(this.U.getValue(), var22),
                                    this.y.getValue(),
                                    var17
                                 )
                              );
                           }
                        }

                        if (this.P.getValue()) {
                           class_243 var23 = var11.method_30950(var1).method_1031(0.0, var11.method_17682() + var12.get("offset").getAsDouble(), 0.0);
                           var2.add(
                              new ChinaHatModule.w(
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

            this.q = mc.field_1687;
            this.R = List.copyOf(var2);
            this.w.values().removeIf(var1x -> var2.stream().noneMatch(var1xx -> var1xx.fire == var1x));
         }
      }
   }

   public void R(f1 var1, float var2) {
      if (this.isEnabled()) {
         this.K++;
         this.S.clear();
         if (this.q == mc.field_1687) {
            var1.blend(ti.d.G);
            double var3 = qo.x();
            byte var5 = 0;

            for (ChinaHatModule.w var7 : this.R) {
               if (var7.dominus) {
                  if (var7.model != null) {
                     var1.i(var7.model, var7.radius, var7.top);
                  }
               } else if (var7.horns != null) {
                  if (var5 != 0 || (var5 = bl.i(var7.horns.horn(), var7.horns.fire(), var7.horns.core())) != 0) {
                     Matrix4f var13 = var7.model == null ? null : var1.x(var7.model, var7.radius);
                     if (var13 != null) {
                        var7.fire.r.set(var13);
                        var7.fire.l = this.Y = this.K;
                     }
                  }
               } else if (var7.accessory != null) {
                  if (var7.model != null) {
                     double var8 = (var7.top >>> 24) / 255.0;
                     Matrix4f var10 = var1.f(var7.model, var7.accessory, var7.radius, var8, var7.lit, var3);
                     if (var10 != null && var7.accessory == q4.r.s) {
                        this.S.add(new ChinaHatModule.v(var10, var7.radius, var8));
                        this.Y = this.K;
                     }
                  }
               } else if (var7.kippah && var7.model != null) {
                  var1.V(var7.model, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline, var7.lit);
               } else if (!var7.kippah) {
                  var1.J(var7.model, var7.base, var7.radius, var7.height, var7.offset, var7.top, var7.rim, var7.outline);
               }
            }

            for (ChinaHatModule.w var12 : this.R) {
               if (var12.fire != null && var12.fire.l == this.K) {
                  var1.X(var12.fire, var12.fire.r, var12.radius, var12.horns.size(), var12.horns.heat(), var12.horns.fire(), var12.horns.core(), var3);
               }
            }
         }
      }
   }

   public boolean p() {
      return !this.isEnabled() ? false : this.isOperational() && this.q == mc.field_1687 && this.Y == this.K;
   }

   public void A(f1 var1, cw var2) {
      if (this.isEnabled()) {
         if (this.p()) {
            double var3 = qo.x();

            for (ChinaHatModule.v var6 : this.S) {
               var1.v(var6.head, var6.scale, var6.opacity, var3);
            }

            for (ChinaHatModule.w var9 : this.R) {
               if (var9.fire != null && var9.fire.l == this.K) {
                  var1.B(var9.fire.r, var9.radius);
               }
            }

            for (ChinaHatModule.w var10 : this.R) {
               if (var10.fire != null && var10.fire.l == this.K) {
                  var1.j(var10.fire, var10.horns.fire(), var10.horns.core(), 0.6);
               }
            }

            var2.P();
         }
      }
   }

   public record a(
      boolean kippah, double radius, double height, double offset, int top, int rim, boolean outline, q4.r accessory, ChinaHatModule.y horns, boolean dominus
   ) {
      public a(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10) {
         this(var1, var2, var4, var6, var8, var9, var10, null, null, false);
      }

      public a(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10, q4.r var11) {
         this(var1, var2, var4, var6, var8, var9, var10, var11, null, false);
      }

      public a(boolean var1, double var2, double var4, double var6, int var8, int var9, boolean var10, q4.r var11, ChinaHatModule.y var12) {
         this(var1, var2, var4, var6, var8, var9, var10, var11, var12, false);
      }

      public double rise() {
         return this.offset + this.height;
      }

      public String label() {
         return this.dominus ? "Dominus" : (this.horns != null ? "Horns" : (this.accessory != null ? this.accessory.X : (this.kippah ? "Kippah" : "Cone")));
      }
   }

   private record v(Matrix4f head, double scale, double opacity) {
   }

   private record w(
      boolean kippah,
      class_243 base,
      class_10017 model,
      double radius,
      double height,
      double offset,
      int top,
      int rim,
      boolean outline,
      q4.r accessory,
      ChinaHatModule.y horns,
      g7 fire,
      boolean dominus,
      double lit
   ) {
      w(boolean var1, class_243 var2, class_10017 var3, double var4, double var6, double var8, int var10, int var11, boolean var12, double var13) {
         this(var1, var2, var3, var4, var6, var8, var10, var11, var12, null, null, null, false, var13);
      }
   }

   public record y(int horn, int fire, int core, double size, double heat) {
   }
}
