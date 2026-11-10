package com.larpclient.module;

import com.larpclient.c;
import com.larpclient.c5;
import com.larpclient.cw;
import com.larpclient.ez;
import com.larpclient.f1;
import com.larpclient.g4;
import com.larpclient.gv;
import com.larpclient.i3;
import com.larpclient.im;
import com.larpclient.j_;
import com.larpclient.jo;
import com.larpclient.q8;
import com.larpclient.qm;
import com.larpclient.qo;
import com.larpclient.t_;
import com.larpclient.u8;
import com.larpclient.wc;
import com.larpclient.yt;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.class_10017;
import net.minecraft.class_10042;
import net.minecraft.class_10055;
import net.minecraft.class_12075;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_2663;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_3419;
import net.minecraft.class_4587;
import net.minecraft.class_742;
import net.minecraft.class_7920;
import net.minecraft.class_8143;
import org.joml.Vector3f;

public final class KillFXModule extends Module {
   public static final int X = 4;
   public static final double s = 0.45;
   private static KillFXModule Q;
   private final li Y = this.addSetting(
      new li(
         "Style",
         "Choose a finisher or vary each kill",
         "Random",
         "Random",
         "Execution",
         "Singularity",
         "Soul Pyre",
         "Star of David",
         "Tung Tung",
         "Tung Tung Floss",
         "Sakura",
         "Shatter",
         "Rug Pull"
      )
   );
   private final li Z = this.addSetting(new li("Colors", "Authored palettes, your theme or custom colors", "Style", "Style", "Theme", "Custom"));
   private final l0 m = this.addSetting(new l0("Primary Color", "Main energy color", -10101505));
   private final l0 D = this.addSetting(new l0("Secondary Color", "Trail and impact color", -32547));
   private final l3 u = this.addSetting(new l3("Bloom", "Use the shared world bloom pass", true));
   private final l3 t = this.addSetting(new l3("Sound", "Local windup and finishing cues", true));
   private final lo r = this.addSetting(new lo("Volume", "Local sound volume", 0.55, 0.0, 1.0, 0.05));
   private final lo S = this.addSetting(new lo("Render Distance", "Maximum distance in blocks", 48.0, 8.0, 64.0, 4.0));
   private final com.larpclient.r p = new com.larpclient.r();
   private final qm j = new qm();
   private final ArrayList<KillFXModule.d> d = new ArrayList<>();
   private List<KillFXModule.x> o = List.of();
   private Object a;
   private Object R;
   private int f = -1;
   private int J = -1;
   private long i;
   private class_243 g;
   private im K;
   private class_12075 h;
   private static final RenderStateDataKey<KillFXModule.p> b = RenderStateDataKey.create(() -> "Larp KillFX confirmed victim");

   public KillFXModule() {
      super("KillFX", "Play cinematic finishers when a player you damaged dies", Category.VISUALS, -1);
      Q = this;
      this.m.O(() -> this.Z.G("Custom"));
      this.D.O(() -> this.Z.G("Custom"));
      this.r.O(() -> this.t.getValue());
      this.Y.a(var1 -> {
         this.w();
         q8.G("KillFX", "Style changed: {}; active finishers cleared", var1);
      });
   }

   public static KillFXModule y() {
      return Q;
   }

   public List<lb<?>> t() {
      return List.of(this.Y, this.Z, this.m, this.D);
   }

   public lb<?> b() {
      return this.Y;
   }

   public List<lb<?>> H() {
      return List.of(this.u, this.t, this.r);
   }

   public List<lb<?>> x() {
      return List.of(this.S);
   }

   public String j() {
      return switch ((String)this.Y.getValue()) {
         case "Execution" -> "Lifts the victim into a double helix, slices the body and drops the skinned pieces onto terrain.";
         case "Singularity" -> "Twists the body into a collapsing star vortex, then releases a sharp ring of light.";
         case "Soul Pyre" -> "Burns the skin away in glowing fragments while spiralling soul embers rise from the body.";
         case "Star of David" -> "Lifts the body and turns its six skinned parts into two interlocking triangles facing your view.";
         case "Tung Tung" -> "Summons a towering Tung Tung Sahur, who raises his wooden bat and slams the victim to the ground.";
         case "Tung Tung Floss" -> "Sahur crouches, jumps onto the fallen player and dances the Floss with swinging arms and counter-swaying hips.";
         case "Sakura" -> "A petal shockwave drops the victim, the body blooms into a swirling sphere under a pillar of light, then bursts and settles into a glowing yin-yang seal.";
         case "Shatter" -> "Frost climbs the frozen victim and cracks of light spread before the body shatters into ice shards and tumbling pieces that melt away.";
         case "Rug Pull" -> "A trading chart pumps from $0 to $1,000 beside the victim, then a red candle crashes through the floor, the rug is yanked away and the profit drops to -$67,000 while a headline falls from the sky and floats in front of the chart.";
         default -> "Varies between all finishers without repeating the last animation.";
      };
   }

   @Override
   public void onEnable() {
      this.w();
      q8.G("KillFX", "Enabled: {}; server death events only, four playing finishers maximum", this.Y.getValue());
   }

   @Override
   public void onDisable() {
      this.w();
   }

   public void w() {
      this.p.E();
      this.d.clear();
      this.o = List.of();
      this.j.l();
      if (this.K != null) {
         this.K.close();
         this.K = null;
      }

      this.h = null;
      this.a = this.R = null;
      this.g = null;
      this.f = -1;
   }

   private boolean l() {
      class_310 var1 = getMc();
      if (this.isRuntimeActive() && var1.field_1687 != null && var1.field_1724 != null) {
         if (this.a != var1.field_1687
            || this.R != var1.field_1724
            || this.f != t_.g
            || this.g != null && this.g.method_1025(var1.field_1724.method_73189()) > 256.0) {
            this.w();
            this.a = var1.field_1687;
            this.R = var1.field_1724;
            this.f = t_.g;
         }

         this.g = var1.field_1724.method_73189();
         return true;
      } else {
         this.w();
         return false;
      }
   }

   public void a(class_8143 var1) {
      if (this.isEnabled()) {
         if (this.l()) {
            class_310 var2 = getMc();
            class_1297 var3 = var2.field_1687.method_8469(var1.comp_1267());
            if (var3 instanceof class_1657 && var3 != var2.field_1724) {
               this.p.u(var3, var1.comp_1269() == var2.field_1724.method_5628(), qo.K());
            }
         }
      }
   }

   public void t(class_2663 var1) {
      if (this.isEnabled()) {
         if (this.l()) {
            class_310 var2 = getMc();
            if (var1.method_11469(var2.field_1687) instanceof class_742 var4 && var4 != var2.field_1724) {
               if (var1.method_11470() == 35) {
                  this.p.y(var4);
                  this.d.removeIf(var1x -> var1x.B == var4);
                  this.o = this.o.stream().filter(var1x -> var1x.owner.B != var4).toList();
               } else if (var1.method_11470() == 3 && this.p.M(var4, qo.K())) {
                  if (!(var4.method_73189().method_1025(var2.field_1724.method_73189()) > this.S.getValue() * this.S.getValue())) {
                     double var5 = qo.K();
                     u8.w var7 = this.x();
                     int var8 = var7 == u8.w.I ? -19633 : (var7 == u8.w.R ? -5144321 : -10101505);
                     int var9 = var7 == u8.w.I ? -8915729 : (var7 == u8.w.R ? -8328705 : -32547);
                     if (var7 == u8.w.Y) {
                        var8 = -8474625;
                        var9 = -1444097;
                     }

                     if (var7 == u8.w.e || var7 == u8.w.K) {
                        var8 = -18594;
                        var9 = -5188;
                     }

                     if (var7 == u8.w.y) {
                        var8 = -22842;
                        var9 = -3337;
                     }

                     if (var7 == u8.w.v) {
                        var8 = -7938817;
                        var9 = -1114881;
                     }

                     if (var7 == u8.w.G) {
                        var8 = -13242230;
                        var9 = -50360;
                     }

                     if (this.Z.G("Custom")) {
                        var8 = this.m.getValue();
                        var9 = this.D.getValue();
                     } else if (this.Z.G("Theme")) {
                        var8 = ez.a;
                        var9 = ez.f;
                     }

                     long var10 = var4.method_5667().getMostSignificantBits() ^ var4.method_5667().getLeastSignificantBits() ^ ++this.i;
                     double var12 = Math.toRadians(var4.field_6283);
                     u8 var14 = new u8(var7, var4.method_73189(), var12, var5, var10, var4.method_52814().comp_1629() == class_7920.field_41122);
                     List var15 = this.d.stream().filter(var0 -> !var0.J()).toList();
                     if (var15.size() >= 4) {
                        ((KillFXModule.d)var15.getFirst()).K(var5);
                        q8.v("KillFX", "Finisher limit reached; retiring the oldest ({}) over {}s", ((KillFXModule.d)var15.getFirst()).a.q, 0.45);
                     }

                     while (this.d.size() >= 8) {
                        this.d.removeFirst();
                     }

                     KillFXModule.d var16 = new KillFXModule.d(var4, var14, var8, var9, this.u.getValue(), this.t.getValue(), this.r.getValue());
                     this.d.add(var16);
                     this.N(
                        var16,
                        var7 == u8.w.I
                           ? class_3417.field_15013
                           : (
                              var7 == u8.w.y
                                 ? class_3417.field_42556
                                 : (
                                    var7 == u8.w.v
                                       ? class_3417.field_27853
                                       : (
                                          var7 == u8.w.G
                                             ? (class_3414)class_3417.field_14725.comp_349()
                                             : (var7 == u8.w.K ? class_3417.field_14706 : (var7 == u8.w.e ? class_3417.field_14649 : class_3417.field_43154))
                                       )
                                 )
                           ),
                        0.8F
                     );
                     q8.v("KillFX", "Confirmed player death: id={}, style={}, source=recent observed server damage", var4.method_5628(), var7);
                  }
               }
            }
         }
      }
   }

   private u8.w x() {
      if (!this.Y.G("Random")) {
         return switch ((String)this.Y.getValue()) {
            case "Execution" -> u8.w.H;
            case "Singularity" -> u8.w.R;
            case "Star of David" -> u8.w.Y;
            case "Tung Tung" -> u8.w.e;
            case "Tung Tung Floss" -> u8.w.K;
            case "Sakura" -> u8.w.y;
            case "Shatter" -> u8.w.v;
            case "Rug Pull" -> u8.w.G;
            default -> u8.w.I;
         };
      } else {
         int var1 = u8.w.values().length;
         int var2 = Math.floorMod(this.i * 31L + Double.doubleToLongBits(qo.K()), var1);
         if (var2 == this.J) {
            var2 = (var2 + 1) % var1;
         }

         this.J = var2;
         return u8.w.values()[var2];
      }
   }

   public void j() {
      if (this.isEnabled()) {
         if (this.l()) {
            class_310 var1 = getMc();
            double var2 = qo.K();
            this.p.I(var2, var1x -> var1x instanceof class_1297 var2x && !var2x.method_31481() && var2x.method_73183() == var1.field_1687);
            this.d.removeIf(var2x -> var2x.a.X(var2) || var2x.J() && var2 - var2x.q >= 0.45);

            for (KillFXModule.d var5 : this.d) {
               if (var5.p && !var5.B.method_31481() && var5.B.method_5805() && !var5.J()) {
                  var5.K(var2);
                  q8.v("KillFX", "Victim id={} alive again; retiring its {} finisher", var5.B.method_5628(), var5.a.q);
               }

               var5.p = (boolean)(var5.p | (!var5.B.method_5805() ? 1 : 0));
               var5.a.t(var2, (var1x, var2x) -> i3.c(var1.field_1687, var1x, var2x));
               KillFXModule.t[] var6 = v(var5.a.q);

               while (var5.i < var6.length && var2 - var5.a.l >= var6[var5.i].time()) {
                  KillFXModule.t var7 = var6[var5.i++];
                  var5.b = (boolean)1;
                  if (!var5.J()) {
                     this.N(var5, var7.event(), var7.pitch());
                  }
               }
            }
         }
      }
   }

   private static KillFXModule.t[] v(u8.w var0) {
      return switch (var0) {
         case H -> new KillFXModule.t[]{new KillFXModule.t(0.85, (class_3414)class_3417.field_14896.comp_349(), 1.25F)};
         case R -> new KillFXModule.t[]{new KillFXModule.t(1.9, (class_3414)class_3417.field_23117.comp_349(), 1.25F)};
         case K -> new KillFXModule.t[]{new KillFXModule.t(1.62, class_3417.field_14794, 1.25F)};
         case e -> new KillFXModule.t[]{new KillFXModule.t(1.56, class_3417.field_14833, 1.25F)};
         case Y -> new KillFXModule.t[]{new KillFXModule.t(1.85, class_3417.field_43154, 1.25F)};
         case I -> new KillFXModule.t[]{new KillFXModule.t(1.25, class_3417.field_19344, 1.25F)};
         case y -> new KillFXModule.t[]{
            new KillFXModule.t(1.0, class_3417.field_14703, 1.4F),
            new KillFXModule.t(3.2, (class_3414)class_3417.field_49049.comp_349(), 1.1F),
            new KillFXModule.t(5.0, class_3417.field_26980, 0.9F)
         };
         case v -> new KillFXModule.t[]{
            new KillFXModule.t(0.95, class_3417.field_26980, 1.6F),
            new KillFXModule.t(1.32, class_3417.field_15081, 0.75F),
            new KillFXModule.t(1.87, class_3417.field_26943, 1.3F)
         };
         case G -> new KillFXModule.t[]{
            new KillFXModule.t(0.7, (class_3414)class_3417.field_14725.comp_349(), 0.75F),
            new KillFXModule.t(1.1, (class_3414)class_3417.field_14725.comp_349(), 0.9F),
            new KillFXModule.t(1.45, (class_3414)class_3417.field_14725.comp_349(), 1.05F),
            new KillFXModule.t(1.75, (class_3414)class_3417.field_14725.comp_349(), 1.25F),
            new KillFXModule.t(2.0, (class_3414)class_3417.field_14725.comp_349(), 1.5F),
            new KillFXModule.t(2.3, class_3417.field_14627, 1.6F),
            new KillFXModule.t(2.6, (class_3414)class_3417.field_14624.comp_349(), 0.5F),
            new KillFXModule.t(2.64, (class_3414)class_3417.field_18310.comp_349(), 0.5F),
            new KillFXModule.t(2.82, class_3417.field_49924, 0.8F),
            new KillFXModule.t(3.25, class_3417.field_15008, 0.85F)
         };
      };
   }

   private void N(KillFXModule.d var1, class_3414 var2, float var3) {
      if (var1.Z && !(var1.w <= 0.0) && getMc().field_1687 != null) {
         class_243 var4 = var1.a.z;
         float var10006 = (float)var1.w;
         getMc().field_1687.method_8486(var4.field_1352, var4.field_1351, var4.field_1350, var2, class_3419.field_15248, var10006, var3, false);
      }
   }

   public void j(class_12075 var1) {
      if (this.isEnabled()) {
         class_243 var2 = var1.field_63078;
         if (this.K != null) {
            this.K.i();
         }

         this.h = null;
         this.o = List.of();
         if (this.l() && !this.d.isEmpty()) {
            ArrayList var3 = new ArrayList();
            Vector3f var4 = var1.field_63081.transform(new Vector3f(1.0F, 0.0F, 0.0F));
            Vector3f var5 = var1.field_63081.transform(new Vector3f(0.0F, 1.0F, 0.0F));
            class_243 var6 = new class_243(var4.x, var4.y, var4.z);
            class_243 var7 = new class_243(var5.x, var5.y, var5.z);
            this.j.t();
            ArrayList var8 = new ArrayList();

            for (KillFXModule.d var10 : this.d) {
               if (!(var10.a.z.method_1025(var2) > this.S.getValue() * this.S.getValue())) {
                  if (var10.z == null) {
                     if (!(gv.i(var10.B, 1.0F) instanceof class_10055 var12)) {
                        q8.O("KillFX", "Native avatar snapshot unavailable; leaving original corpse visible", null);
                        continue;
                     }

                     var10.z = var12;
                  }

                  double var20 = Math.max(0.0, qo.x() - var10.a.l);
                  yt.l var13 = var10.a.q == u8.w.Y ? yt.U(yt.h(var10.a, var20), var2, var6, var7) : (var10.a.E() != null ? var10.a.E().a(var2, qo.x()) : yt.G);
                  u8.s[] var14 = new u8.s[6];
                  double var15 = var10.S(qo.x());
                  if (var10.a.E() != null) {
                     if (this.K == null) {
                        this.K = new im();
                     }

                     if (this.K.c()) {
                        try {
                           var10.a.E().h(var13, var20, var15, this.K::h, var3);
                        } catch (RuntimeException var19) {
                           q8.O("KillFX", "Rug Pull chart labels failed; the chart continues without text", var19);
                        }
                     }
                  }

                  for (int var17 = 0; var17 < var14.length; var17++) {
                     u8.s var18 = var10.a.S(var17, var20, qo.C(), var13);
                     var14[var17] = var15 >= 1.0
                        ? var18
                        : new u8.s(
                           var18.position(),
                           var18.rx(),
                           var18.ry(),
                           var18.rz(),
                           var18.scale(),
                           var18.alpha() * var15,
                           var18.dissolve(),
                           var18.landed(),
                           var18.orientation(),
                           var18.stretch()
                        );
                  }

                  jo.c var21 = null;
                  g4.j var22 = null;
                  if (var10.a.q == u8.w.e) {
                     j_.g();
                     if (var10.N == null) {
                        var10.N = new jo(j_.S());
                        q8.v("KillFX", "Tung Tung ready: shared Sahur mesh, height={} blocks, articulated bat strike at {}s", 3.6, 1.56);
                     }

                     var21 = var10.N.g(var10.a, var20, var2);
                  }

                  if (var10.a.q == u8.w.K) {
                     j_.g();
                     if (var10.T == null) {
                        var10.T = new g4(j_.S());
                        q8.v("KillFX", "Tung Tung Floss ready: articulated jump, landing at {}s, four Floss cycles, expiry={}s", 1.62, 7.5);
                     }

                     var22 = var10.T.a(var10.a, var20, var2);
                  }

                  var8.add(new KillFXModule.x(var10, var10.z, var14, var20, var2, var13, var21, var22, this.f, var15));
               }
            }

            this.o = List.copyOf(var8);
            if (this.K != null) {
               this.K.R(var3, var1);
               this.h = var1;
            }
         }
      }
   }

   public boolean u() {
      return !this.d.isEmpty();
   }

   public void H(class_1297 var1, class_10017 var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1.method_73183() == this.a) {
            for (KillFXModule.d var4 : this.d) {
               if (var4.B == var1) {
                  var2.setData(b, new KillFXModule.p(var1, this.a, this.f));
                  return;
               }
            }
         }
      }
   }

   public boolean B(class_10017 var1) {
      if (!this.isEnabled()) {
         return false;
      }

      if (this.isRuntimeActive() && wc.l() && wc.U("KillFX") && !gv.a()) {
         KillFXModule.p var2 = (KillFXModule.p)var1.getData(b);
         if (var2 != null && var2.world == getMc().field_1687 && var2.generation == t_.g) {
            for (KillFXModule.x var4 : this.o) {
               if (var4.owner.B == var2.victim
                  && this.d.contains(var4.owner)
                  && var4.generation == var2.generation
                  && (var4.age < 0.25 || var1 instanceof class_10042 var5 && var5.field_53449 > 0.0F)) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public boolean E() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.o.stream().anyMatch(var0 -> var0.owner.h);
   }

   public void E(f1 var1, class_4587 var2, cw var3, boolean var4) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            long var5 = c5.X();

            try {
               for (KillFXModule.x var8 : this.o) {
                  if (var8.generation == t_.g && (!var4 || var8.owner.h)) {
                     this.j.T(var8.skin, var8.owner.a, var8.parts, var8.camera, var2, var3, var8.owner.O, var4);
                     if (!var4 && var8.strike != null) {
                        j_.L(var2, var3, var8.strike.transform(), var8.strike.vertices(), var8.strike.colors(), var8.strike.opacity() * var8.fade);
                     }

                     if (!var4 && var8.floss != null) {
                        j_.L(var2, var3, var8.floss.transform(), var8.floss.vertices(), var8.floss.colors(), var8.floss.opacity() * var8.fade);
                     }

                     c.z(var1, var8.owner.a, var8.parts, var8.age, var8.owner.O, var8.owner.l, var4, var8.fade, var8.starPlane, var8.strike, var8.floss);
                  }
               }

               if (!var4 && this.K != null && this.h != null) {
                  this.K.A(var2, this.h, false, false, 1.0);
               }
            } finally {
               c5.w("KillFX", var5);
            }
         }
      }
   }

   private static final class d {
      final class_742 B;
      final u8 a;
      final int O;
      final int l;
      final boolean h;
      final boolean Z;
      final double w;
      class_10055 z;
      jo N;
      g4 T;
      boolean b;
      boolean p;
      int i;
      double q = Double.NaN;

      d(class_742 var1, u8 var2, int var3, int var4, boolean var5, boolean var6, double var7) {
         this.B = var1;
         this.a = var2;
         this.O = var3;
         this.l = var4;
         this.h = var5;
         this.Z = var6;
         this.w = var7;
      }

      boolean J() {
         return !Double.isNaN(this.q);
      }

      void K(double var1) {
         if (!this.J()) {
            this.q = var1;
         }
      }

      double S(double var1) {
         return this.J() ? 1.0 - u8.S((var1 - this.q) / 0.45) : 1.0;
      }
   }

   private record p(Object victim, Object world, int generation) {
   }

   private record t(double time, class_3414 event, float pitch) {
   }

   private record x(
      KillFXModule.d owner, class_10055 skin, u8.s[] parts, double age, class_243 camera, yt.l starPlane, jo.c strike, g4.j floss, int generation, double fade
   ) {
   }
}
