package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q586814c6d8f3640b;
import com.larpclient.bn;
import com.larpclient.cr;
import com.larpclient.fs;
import com.larpclient.gn;
import com.larpclient.hl;
import com.larpclient.j8;
import com.larpclient.q8;
import com.larpclient.s8;
import com.larpclient.t9;
import com.larpclient.ug;
import com.larpclient.uz;
import com.larpclient.w6;
import com.larpclient.wp;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.Locale;
import java.util.Set;
import net.minecraft.class_10185;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3966;
import net.minecraft.class_5134;
import net.minecraft.class_5321;
import net.minecraft.class_634;
import net.minecraft.class_640;
import net.minecraft.class_6880;
import net.minecraft.class_746;
import net.minecraft.class_9362;

public final class MaceModule extends Module {
   private static MaceModule a;
   private final l3 J = this.addSetting(l("auto-swap", "Auto Swap", "Select a mace immediately before a smash hit", true));
   private final li V = this.addSetting(I("pick", "Pick", "Prefer damage or a Wind Burst mace for smash hits", "Damage", "Damage", "Wind Burst"));
   private final l3 T = this.addSetting(
      l(
         "breach-swap",
         "Breach Swap",
         "Use a Breach mace for ordinary hits when expected damage improves by 15%. Skipped on Paper servers, which reset the charge on a swap",
         false
      )
   );
   private final l3 x = this.addSetting(l("swap-back", "Swap Back", "Return to the previous hotbar slot after the attack", true));
   private final l3 o = this.addSetting(l("smash-assist", "Smash Assist", "Attack the enemy under the crosshair during a qualifying fall", false));
   private final li u = this.addSetting(
      I("smash-timing", "Smash Timing", "Delay while the next movement step can still reach the target", "Delayed", "Direct", "Delayed")
   );
   private final lo k = this.addSetting(new lo("min-fall", "Minimum fall distance for automatic smash hits", 3.0, 1.5, 30.0, 0.5) {
      @Override
      public String getDisplayName() {
         return "Min Fall";
      }
   });
   private final l3 G = this.addSetting(l("mobs", "Mobs", "Allow smash assistance against non-player enemies", false));
   private final l3 j = this.addSetting(
      l("silent-aim", "Silent Aim", "Turn toward a nearby enemy and smash it during a fall while your view stays free", false)
   );
   private final lo F = this.addSetting(n("aim-range", "Aim Range", "Distance at which Silent Aim starts turning toward an enemy", 6.0, 3.0, 10.0, 0.5));
   private final lo W = this.addSetting(n("turn-speed", "Turn Speed", "Maximum Silent Aim turn in degrees per tick", 60.0, 10.0, 180.0, 5.0));
   private final li m = this.addSetting(
      I(
         "rotation",
         "Rotation",
         "Kill Aura rotation engine that turns the player toward the target; Legit moves most like a hand",
         "Sloth",
         "Sloth",
         "FunTime",
         "Polar",
         "Matrix",
         "Legit"
      )
   );
   private final l3 i = this.addSetting(
      l("lead-turn", "Lead Turn", "In a fast fall, start turning early enough that the turn finishes before the target is in reach", true)
   );
   private final li q = this.addSetting(
      I(
         "stun-slam",
         "Stun Slam",
         "When a smash target raises a shield, hit it with your best axe to disable the shield, then smash. Same Tick sends both hits at once; Next Tick smashes a tick later, which Grim's experimental packet order check does not flag",
         "Same Tick",
         "Same Tick",
         "Next Tick",
         "Off"
      )
   );
   private final li O = this.addSetting(
      I(
         "range-ring",
         "Range Ring",
         "Draw Silent Aim's reach on the ground below you: an enemy standing inside the ring is in range. In Air shows it only while you are off the ground",
         "In Air",
         "In Air",
         "Always",
         "Off"
      )
   );
   private final l3 M = this.addSetting(
      l(
         "target-mark",
         "Target Mark",
         "Outline the enemy Silent Aim is turning toward and mark where your real facing points; green once that facing is on the enemy",
         true
      )
   );
   private final lo Z = this.addSetting(
      n(
         "return-speed",
         "Return Speed",
         "Most the player turns per tick back to your look after Silent Aim lets go, like a hand. 180 returns at once",
         35.0,
         10.0,
         180.0,
         5.0
      )
   );
   private final gn R = new gn();
   private final fs X = new fs(this);
   private final l6 w = new l6();
   private final lg e = new lg();
   private final a D = new a();
   private final ll d = new ll();
   private final lh b = new lh();
   private z h;
   private boolean y;
   private boolean r;
   private class_746 A;
   private Object N;
   private int f = Integer.MIN_VALUE;
   private class_1309 v;
   private String g = "Waiting";
   private class_1309 Y;
   private int z;
   private int n;
   private int t;
   private double L;
   private boolean l;
   private class_1309 P;
   private int K = Integer.MIN_VALUE;
   private boolean H;
   private String B = "";
   private String U = "";

   public MaceModule() {
      super("Mace", "Select a mace for smash hits, with optional smash assistance and Silent Aim", Category.COMBAT, -1);
      a = this;
      this.V.O(this.J::getValue);
      this.T.O(this.J::getValue);
      this.x.O(() -> this.J.getValue() || this.a());
      this.u.O(this::a);
      this.k.O(this::a);
      this.G.O(this::a);
      this.F.O(this.j::getValue);
      this.i.O(this.j::getValue);
      this.W.O(this.j::getValue);
      this.m.O(this.j::getValue);
      this.O.O(this.j::getValue);
      this.M.O(this.j::getValue);
      this.Z.O(this.j::getValue);
      this.j.a(var1 -> this.W());
   }

   public static MaceModule c() {
      return a;
   }

   public gn A() {
      return this.R;
   }

   public double L() {
      return this.Z.getValue();
   }

   @Override
   public String getDisplayName() {
      return "Auto Mace";
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   private boolean a() {
      return this.o.getValue() || this.j.getValue();
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y);
   }

   @Override
   public String getStatus() {
      return this.g;
   }

   @Override
   public void onEnable() {
      this.M();
      q8.G(
         "Mace",
         "Active; autoSwap={}, smashAssist={}, silentAim={}, timing={}, minFall={}, stunSlam={}, leadTurn={}, rangeRing={}, targetMark={}, returnSpeed={}",
         this.J.getValue(),
         this.o.getValue(),
         this.j.getValue(),
         this.u.getValue(),
         this.k.getValue(),
         this.q.getValue(),
         this.i.getValue(),
         this.O.getValue(),
         this.M.getValue(),
         this.Z.getValue().intValue()
      );
   }

   @Override
   public void onDisable() {
      this.M();
      this.W();
      this.X.o(getMc(), this.x.getValue());
      q8.v("Mace", "Disabled or context suspended; hotbar, view and sprint ownership released");
   }

   public static void E() {
      if (a != null && a.isEnabled()) {
         MaceModule var0 = a;
         if (var0 != null && var0.isRuntimeActive()) {
            class_310 var1 = getMc();
            var0.A = null;
            var0.N = null;
            var0.f = Integer.MIN_VALUE;
            if (var1.field_1724 != null && var1.field_1724.method_24828()) {
               var0.L();
            }

            if (var0.P == null) {
               var0.X.u(var1, var0.x.getValue());
            }
         }
      }
   }

   private void L() {
      if (this.l) {
         q8.G("Mace", "Landed before the server confirmed the smash on {}", T(this.Y));
      }

      this.y = this.l = (boolean)0;
      this.Y = null;
      this.t = 0;
      this.P = null;
      this.B = "";
   }

   public void w(class_746 var1, class_1297 var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var1 == getMc().field_1724 && var2 instanceof class_1309 var3) {
            boolean var4 = class_9362.method_58659(var1);
            if (this.Z(var1, var2)) {
               int var7 = bn.d(var1);
               if (this.X.m(var1, var7)) {
                  bn.f(var3);
                  this.P = var3;
                  this.K = var1.field_6012;
                  this.H = this.q.G("Same Tick");
                  this.g = "Stun Slam";
                  q8.G("Mace", "Stun Slam on {}: axe slot {} for your hit, smash {}", T(var3), var7 + 1, this.H ? "in the same tick" : "next tick");
               }
            } else if (this.J.getValue() && (!bn.B(var3, var1) || bn.G(var3))) {
               if (var4 || this.T.getValue() && !this.J("Breach Swap")) {
                  int var5 = this.Z(var1, var3, var4);
                  if (var5 >= 0 && j8.k(var4, C(var1, var1.method_31548().method_5438(var5), var3), C(var1, var1.method_6047(), var3))) {
                     int var6 = var1.method_31548().method_67532();
                     if (this.X.m(var1, var5) && var6 != var5) {
                        q8.v("Mace", "Selected hotbar slot {} for {} hit", var5 + 1, var4 ? "smash" : "Breach");
                     }

                     if (var4) {
                        this.K(var1, var3);
                     }
                  }
               }
            }
         }
      }
   }

   private boolean J(String var1) {
      if (!uz.l()) {
         return false;
      }

      if (!var1.equals(this.U)) {
         q8.G("Mace", "{} skipped: {} resets the charge on a swap", var1, uz.i());
      }

      this.U = var1;
      return true;
   }

   public boolean Z(class_746 var1, class_1297 var2) {
      if (!this.isEnabled()) {
         return false;
      } else if (this.isRuntimeActive()
         && !this.q.G("Off")
         && var1 == getMc().field_1724
         && var2 instanceof class_1657 var3
         && !cr.C()
         && class_9362.method_58659(var1)) {
         j8.i var4 = j8.p(this.q.getValue(), bn.B(var3, var1), bn.G(var3), bn.d(var1) >= 0, this.I(var1, var3) >= 0);
         return var4 == j8.i.p || var4 == j8.i.g;
      } else {
         return false;
      }
   }

   public void D(class_746 var1, class_1297 var2) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && var2 == this.P && this.H && this.K == var1.field_6012) {
            class_1309 var3 = this.P;
            this.P = null;
            this.S(getMc(), var1, var3, "Stun Slam smash after your axe hit");
         }
      }
   }

   private boolean S(class_310 var1, class_746 var2, class_1309 var3, String var4) {
      int var5 = this.I(var2, var3);
      if (var5 >= 0 && this.X.m(var2, var5)) {
         boolean var6 = cr.y(this, var3, () -> {
            var1.field_1761.method_2918(var2, var3);
            w6.M(var2, class_1268.field_5808);
         });
         if (var6) {
            this.K(var2, var3);
            q8.G("Mace", "{}: fall {}, mace slot {}", var4, q(var2.field_6017), var5 + 1);
         }

         return var6;
      } else {
         q8.G("Mace", "{} skipped: no usable mace", var4);
         return false;
      }
   }

   private int I(class_746 var1, class_1309 var2) {
      if (var1.method_6047().method_31574(class_1802.field_49814) && !var1.method_6047().method_63692()) {
         return var1.method_31548().method_67532();
      } else {
         return this.J.getValue() ? this.Z(var1, var2, true) : -1;
      }
   }

   private int Z(class_746 var1, class_1309 var2, boolean var3) {
      j8.k[] var4 = new j8.k[class_1661.method_7368()];

      for (int var5 = 0; var5 < var4.length; var5++) {
         class_1799 var6 = var1.method_31548().method_5438(var5);
         if (var6.method_31574(class_1802.field_49814) && !var6.method_63692()) {
            int var7 = w(var6, class_1893.field_50158);
            if (var3 || var7 != 0) {
               var4[var5] = new j8.k(var5, true, false, var7, w(var6, class_1893.field_50159), C(var1, var6, var2));
            }
         }
      }

      return j8.D(var4, var3, this.V.G("Wind Burst"));
   }

   private static float C(class_746 var0, class_1799 var1, class_1309 var2) {
      return j8.i(
         bn.i(var0.method_6047()),
         bn.M(var0),
         var0.field_6017,
         var1.method_31574(class_1802.field_49814),
         class_9362.method_58659(var0),
         w(var1, class_1893.field_50157),
         bn.A(var0, var2),
         w(var1, class_1893.field_9118),
         var2.method_6096(),
         (float)var2.method_45325(class_5134.field_23725),
         w(var1, class_1893.field_50158)
      );
   }

   private static int w(class_1799 var0, class_5321<class_1887> var1) {
      for (Entry var3 : var0.method_58657().method_57539()) {
         if (((class_6880)var3.getKey()).method_40225(var1)) {
            return var3.getIntValue();
         }
      }

      return 0;
   }

   @Override
   public void onTick() {
      boolean var1 = this.r;
      this.r = (boolean)0;
      class_310 var2 = getMc();
      if (var2.field_1724 != null) {
         this.G(var2.field_1724);
      }

      String var18;
      if (this.y) {
         boolean var10 = false;
         byte var9 = this.l;
         int var10001 = Integer.signum(var9);
         var18 = ((var10001 | -var10001) >>> 31 ^ 1) == 0 ? "Waiting for the server" : "Smash used this fall";
      } else {
         var18 = "Waiting";
      }

      this.g = var18;
      if (this.P != null) {
         boolean var14 = false;
         byte var13 = this.H;
         int var10000 = Integer.signum(var13);
         if ((var10000 | -var10000) >>> 31 == 0) {
            this.s(var2);
            return;
         }
      }

      label120: {
         if (this.H && this.P != null && var2.field_1724 != null) {
            int var12 = var2.field_1724.field_6012;
            int var11 = this.K;
            int var19 = Integer.compare(var11, var12);
            if ((var19 | -var19) >>> 31 == 0) {
               var20 = this.P;
               break label120;
            }
         }

         var20 = null;
      }

      this.P = var20;
      if (!this.a()) {
         this.W();
      } else {
         class_1309 var3 = this.j.getValue() ? this.D(var2) : null;
         if (var3 == null) {
            this.W();
         }

         if (bn.x(var2, wp.z.m) && fs.o(this)) {
            class_746 var4;
            class_3966 var5;
            class_1309 var6;
            int var7;
            boolean var17;
            int var22;
            boolean var10002;
            int var10003;
            boolean var10004;
            int var10005;
            boolean var10006;
            double var10007;
            double var10008;
            label96: {
               var4 = var2.field_1724;
               var5 = bn.U(var2);
               var6 = this.G(var4, var5, var3);
               var7 = var6 == null && var3 == null ? 0 : 1;
               var17 = bn.w(var4);
               var21 = bn.x(var2);
               var10002 = bn.l(var2);
               var10003 = var3 == null && t9.I() ? 1 : 0;
               var10004 = bn.R(var2, var4);
               var10005 = var4.method_18798().field_1351 < 0.0 ? 1 : 0;
               var10006 = class_9362.method_58659(var4);
               var10007 = var4.field_6017;
               var10008 = this.k.getValue();
               if (this.y) {
                  boolean var16 = false;
                  byte var15 = this.l;
                  var22 = Integer.signum(var15);
                  if ((var22 | -var22) >>> 31 == 0) {
                     var22 = 1;
                     break label96;
                  }
               }

               var22 = 0;
            }

            String var8 = j8.W(
               var17,
               var21,
               var10002,
               (boolean)var10003,
               var10004,
               (boolean)var10005,
               var10006,
               var10007,
               var10008,
               (boolean)var22,
               this.l,
               var6 != null && this.I(var4, var6) >= 0,
               var6 != null
            );
            if (var8 == null) {
               this.D(var2, var4, var5, var6, var3, var1);
            } else if (var7 != 0) {
               this.f(var8, var4, var3);
            }

            if (var3 != null && (!this.y || this.l)) {
               if (!bn.w(var4)) {
                  this.L(var2, var3);
               }
            } else {
               this.W();
            }
         } else {
            this.W();
         }
      }
   }

   private class_1309 G(class_746 var1, class_3966 var2, class_1309 var3) {
      if (!(var2 != null && var2.method_17782() instanceof class_1309 var4)) {
         return null;
      } else if (!this.o.getValue() && var4 != var3) {
         return null;
      } else {
         return bn.K(var1, var4) && (var4 instanceof class_1657 || this.G.getValue()) ? var4 : null;
      }
   }

   private void f(String var1, class_746 var2, class_1309 var3) {
      if (!var1.equals(this.B)) {
         this.B = var1;
         q8.G(
            "Mace",
            "Smash held: {}; fall {}, speed {}{}",
            var1,
            q(var2.field_6017),
            q(var2.method_18798().field_1351),
            var3 == null ? "" : ", Silent Aim target " + T(var3) + " at " + q(ug.L(var2.method_33571(), var3.method_5829()))
         );
      }
   }

   private void D(class_310 var1, class_746 var2, class_3966 var3, class_1309 var4, class_1309 var5, boolean var6) {
      this.B = "";
      j8.i var7 = j8.p(this.q.getValue(), bn.B(var4, var2), bn.G(var4), bn.d(var2) >= 0, this.I(var2, var4) >= 0);
      if ((var7 != j8.i.g || !H(var2, var4)) && var7 != j8.i.k) {
         if (var7 == j8.i.R && bn.B(var4, var2) && !bn.G(var4)) {
            this.f("raised shield and no axe in the hotbar", var2, var5);
         } else if (this.Y(var2, var4)) {
            this.r = (boolean)1;
            this.A = var2;
            this.N = var1.field_1687;
            this.f = var2.field_6012;
            var2.method_5728(false);
            bn.i(var1, Math.max(bn.v(var1), 1));
            this.g = "Delaying smash";
            if (!var6) {
               q8.v("Mace", "Delaying smash while the predicted next step remains in reach");
            }
         } else if (var7 != j8.i.R) {
            this.v(var1, var2, var4, var7);
         } else {
            int var8 = this.I(var2, var4);
            if (var8 == var2.method_31548().method_67532() || this.X.m(var2, var8)) {
               if (bn.u(var1, var3)) {
                  this.K(var2, var4);
                  bn.E(var1);
                  q8.G("Mace", "Smash sent on {}: fall {}, speed {}, silent={}", T(var4), q(var2.field_6017), q(var2.method_18798().field_1351), var4 == var5);
               } else {
                  q8.G("Mace", "Smash attack refused by the client gate on {}", T(var4));
               }
            }
         }
      } else {
         this.v(var1, var2, var4, var7);
      }
   }

   private void v(class_310 var1, class_746 var2, class_1309 var3, j8.i var4) {
      int var5 = bn.d(var2);
      int var6 = this.I(var2, var3);
      if (this.X.m(var2, var5)) {
         int var7 = var4 == j8.i.p ? 1 : 0;
         boolean var8 = cr.J(this, var3, () -> {
            var1.field_1761.method_2918(var2, var3);
            w6.M(var2, class_1268.field_5808);
            bn.f(var3);
            if (var7 && this.X.m(var2, var6)) {
               var1.field_1761.method_2918(var2, var3);
               w6.M(var2, class_1268.field_5808);
            }
         });
         if (!var8) {
            this.X.o(var1, true);
            q8.G("Mace", "Stun Slam refused: this tick's attack belongs to another owner");
         } else {
            bn.E(var1);
            if (var7 != 0) {
               this.K(var2, var3);
               this.g = "Stun Slam";
               q8.G("Mace", "Stun Slam on {}: axe slot {} then mace slot {} in one tick, fall {}", T(var3), var5 + 1, var6 + 1, q(var2.field_6017));
            } else if (var4 == j8.i.g) {
               this.P = var3;
               this.K = var2.field_6012;
               this.H = (boolean)0;
               this.g = "Stun Slam";
               q8.G("Mace", "Stun Slam on {}: axe slot {} now, smash next tick", T(var3), var5 + 1);
            } else {
               this.g = "Shield break hit";
               q8.G("Mace", "Shield break hit on {} with axe slot {}; no mace for the smash", T(var3), var5 + 1);
            }
         }
      }
   }

   private void s(class_310 var1) {
      class_746 var2 = var1.field_1724;
      class_1309 var3 = this.P;
      if (var2 != null && var2.field_6012 == this.K + 1) {
         this.P = null;
         class_3966 var4 = bn.U(var1);
         if (var4 != null && var4.method_17782() == var3 && class_9362.method_58659(var2) && !var2.method_24828()) {
            int var5 = this.I(var2, var3);
            if (var5 >= 0 && this.X.m(var2, var5)) {
               boolean var6 = cr.J(this, var3, () -> {
                  var1.field_1761.method_2918(var2, var3);
                  w6.M(var2, class_1268.field_5808);
               });
               if (var6) {
                  this.K(var2, var3);
                  bn.E(var1);
                  q8.G("Mace", "Stun Slam smash on {} one tick after the axe: fall {}", T(var3), q(var2.field_6017));
               } else {
                  q8.G("Mace", "Next Tick smash on {} refused: this tick's attack belongs to another owner", T(var3));
               }
            } else {
               this.X.o(var1, this.x.getValue());
            }
         } else {
            q8.G(
               "Mace",
               "Next Tick smash on {} skipped: {}",
               T(var3),
               var2.method_24828() ? "landed" : (!class_9362.method_58659(var2) ? "fall ended" : "facing left the target")
            );
            this.X.o(var1, this.x.getValue());
         }
      } else {
         if (var2 == null || var2.field_6012 > this.K + 1) {
            this.P = null;
            this.X.o(var1, this.x.getValue());
         }
      }
   }

   private void K(class_746 var1, class_1309 var2) {
      this.y = this.l = (boolean)1;
      this.Y = var2;
      this.z = var1.field_6012;
      this.n = var2.field_6235;
      this.L = var1.method_18798().field_1351;
      this.g = "Waiting for the server";
   }

   private void G(class_746 var1) {
      if (this.l && this.Y != null) {
         if (j8.R(this.L, var1.method_18798().field_1351, this.n, this.Y.field_6235)) {
            this.l = (boolean)0;
            q8.G("Mace", "Smash confirmed on {} after {} ticks", T(this.Y), var1.field_6012 - this.z);
         } else if (var1.field_6012 - this.z >= j8.J(this.S(var1))) {
            this.l = (boolean)0;
            if (this.t < 1 && !var1.method_24828() && class_9362.method_58659(var1)) {
               this.t++;
               this.y = (boolean)0;
               q8.G("Mace", "No server confirmation for the smash on {} after {} ticks; retrying once this fall", T(this.Y), var1.field_6012 - this.z);
            } else {
               q8.G("Mace", "No server confirmation for the smash on {}; not retrying ({})", T(this.Y), this.t >= 1 ? "already retried" : "fall ended");
            }
         }
      }
   }

   private int S(class_746 var1) {
      class_634 var2 = getMc().method_1562();
      class_640 var3 = var2 == null ? null : var2.method_2871(var1.method_5667());
      return var3 == null ? 0 : Math.round(var3.method_2959() / 50.0F);
   }

   private class_1309 D(class_310 var1) {
      class_746 var2 = var1.field_1724;
      if (var2 != null && var1.field_1687 != null && (!this.y || this.l) && !var2.method_24828() && !(var2.method_18798().field_1351 >= 0.0) && wp.h(wp.z.m)) {
         if (this.v != null && this.q(var2, this.v, this.p(var2, this.v))) {
            return this.v;
         }

         class_243 var3 = var2.method_33571();
         double var4 = 16.0;
         class_1309 var6 = null;
         double var7 = Double.MAX_VALUE;

         for (class_1297 var10 : var1.field_1687
            .method_8333(var2, var2.method_5829().method_1014(Math.max(this.F.getValue(), var4)), var0 -> var0 instanceof class_1309)) {
            class_1309 var11 = (class_1309)var10;
            double var12 = ug.L(var3, var11.method_5829());
            if (var12 < var7 && this.q(var2, var11, this.p(var2, var11))) {
               var6 = var11;
               var7 = var12;
            }
         }

         return var6;
      } else {
         return null;
      }
   }

   private double p(class_746 var1, class_1309 var2) {
      l4 var3 = new l4(var1.method_36454(), var1.method_36455());
      int var4 = this.H().ticksToReach(var3, a_.A(T(var1, var2)));
      return j8.N(this.F.getValue(), this.i.getValue(), -var1.method_18798().field_1351, var4, var1.method_55755());
   }

   public double F(class_746 var1) {
      if (!this.isEnabled()) {
         return 0.0;
      } else if (this.isRuntimeActive() && this.j.getValue() && var1 != null && !this.O.G("Off") && (!this.O.G("In Air") || !var1.method_24828())) {
         l4 var2 = new l4(var1.method_36454(), var1.method_36455());
         int var3 = this.H().ticksToReach(var2, new l4(var1.method_36454(), 90.0F));
         return j8.N(this.F.getValue(), this.i.getValue(), -var1.method_18798().field_1351, var3, var1.method_55755());
      } else {
         return 0.0;
      }
   }

   public class_1309 O() {
      if (!this.isEnabled()) {
         return null;
      } else {
         return this.M.getValue() && this.l() ? this.v : null;
      }
   }

   private boolean q(class_746 var1, class_1309 var2, double var3) {
      class_243 var5 = var1.method_33571();
      return bn.K(var1, var2)
         && (var2 instanceof class_1657 || this.G.getValue())
         && ug.L(var5, var2.method_5829()) <= var3
         && this.r(var1, var2)
         && bn.I(var1, var5, T(var1, var2));
   }

   private static class_243 T(class_746 var0, class_1309 var1) {
      class_238 var2 = var1.method_5829();
      class_243 var3 = var0.method_33571();
      class_238 var4 = var2.method_35580(
         Math.min(0.1, var2.method_17939() * 0.25), Math.min(0.1, var2.method_17940() * 0.25), Math.min(0.1, var2.method_17941() * 0.25)
      );
      class_243 var5 = new class_243(
         Math.clamp(var3.field_1352, var4.field_1323, var4.field_1320),
         Math.clamp(var3.field_1351, var4.field_1322, var4.field_1325),
         Math.clamp(var3.field_1350, var4.field_1321, var4.field_1324)
      );
      class_243 var6 = new class_243(var2.method_1005().field_1352, var2.field_1322 + var2.method_17940() * 0.6, var2.method_1005().field_1350);
      return cr.D(var1, var0.method_55755(), var6, s8.B(var2, var5)).orElse(var6);
   }

   private z H() {
      return switch ((String)this.m.getValue()) {
         case "FunTime" -> this.e;
         case "Polar" -> this.D;
         case "Matrix" -> this.d;
         case "Legit" -> this.b;
         default -> this.w;
      };
   }

   private void L(class_310 var1, class_1309 var2) {
      class_746 var3 = var1.field_1724;
      if (!cr.a(this)) {
         this.W();
      } else {
         z var4 = this.H();
         if (var4 != this.h) {
            if (this.h != null) {
               this.h.reset();
            }

            this.h = var4;
         }

         class_238 var5 = var2.method_5829();
         if (var2 != this.v) {
            this.h.targetChanged(var2);
            q8.G(
               "Mace",
               "Silent Aim target {} at {} blocks, falling {} blocks/tick, rotation={}",
               T(var2),
               q(ug.L(var3.method_33571(), var5)),
               q(-var3.method_18798().field_1351),
               this.m.getValue()
            );
         }

         this.v = var2;
         wp.S(wp.z.m);
         if (wp.o() != wp.z.m) {
            this.v = null;
         } else {
            l4 var6 = new l4(var3.method_36454(), var3.method_36455());
            double var7 = var3.method_55755();
            int var9 = ug.L(var3.method_33571(), var5) <= var7 ? 1 : 0;
            q586814c6d8f3640b var10 = (q586814c6d8f3640b)var3;
            boolean var11 = com.larpclient.module.f.n(var3, var5.method_1014(var2.method_5871()), var10.larpYRotLast(), var10.larpXRotLast(), var7 + 1.0, false);
            l4 var12 = this.h.rotate(var6, a_.A(T(var3, var2)), var2, (boolean)var9, var11);
            float[] var13 = hl.m(var6.j, var6.l, var12.j, var12.l, this.W.getValue());
            var12 = new l4(var13[0], var13[1]);
            var12 = this.h == this.b ? this.b.c(var6, var12) : a_.Z(var6, var12);
            t9.O(this, t9.v.z, var12.j, var12.l, 1.0);
            if (!t9.k(t9.v.z)) {
               q8.v("Mace", "Silent Aim yielded to a higher rotation owner");
               this.W();
            } else {
               if (!this.r) {
                  this.g = "Silent aim";
               }
            }
         }
      }
   }

   private void W() {
      if (this.v != null && this.h != null) {
         this.h.reset();
      }

      this.v = null;
      wp.g(wp.z.m, this.R, this.Z.getValue(), true);
   }

   public float I(float var1, float var2) {
      if (this.isEnabled()) {
         return this.l() ? AuraModule.p(var1, var2, 35.0F, 0.25F) : var1;
      } else {
         return var1;
      }
   }

   public boolean l() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.j.getValue() && this.v != null && this.v.method_5805() && !this.v.method_31481() && cr.W(this);
   }

   public void r() {
      if (this.isEnabled()) {
         this.y = (boolean)1;
         this.g = "Smash used this fall";
      }
   }

   public boolean r(class_746 var1, class_1309 var2) {
      if (!this.isEnabled()) {
         return false;
      }

      boolean var3 = var1.method_6047().method_31574(class_1802.field_49814);
      int var4 = !var3
            && this.J.getValue()
            && this.isRuntimeActive()
            && !this.y
            && class_9362.method_58659(var1)
            && var1.field_6017 >= this.k.getValue()
            && this.Z(var1, var2, true) >= 0
         ? 1
         : 0;
      return j8.Q(this.isRuntimeActive(), this.y, class_9362.method_58659(var1), var1.field_6017, this.k.getValue(), var3, this.J.getValue(), (boolean)var4);
   }

   public boolean Y(class_746 var1, class_1309 var2) {
      return this.isEnabled() ? j8.Q(H(var1, var2), this.u.G("Delayed"), bn.M(var1), var1.method_7261(1.5F), var2.field_6235, var1.method_5624()) : false;
   }

   private static boolean H(class_746 var0, class_1309 var1) {
      class_243 var2 = var0.method_18798();
      return j8.H(
         var0.method_73183().method_8587(var0, var0.method_5829().method_997(var2)),
         var0.method_55755(),
         var0.method_33571(),
         var2,
         var1.method_5829(),
         var1.method_73189().method_1023(var1.field_6014, var1.field_6036, var1.field_5969),
         var1.method_5871(),
         var0.method_5828(1.0F),
         (var1x, var2x) -> bn.I(var0, var1x, var2x)
      );
   }

   public static boolean O() {
      if (a != null && a.isEnabled()) {
         MaceModule var0 = a;
         if (var0 != null && var0.isRuntimeActive() && var0.a()) {
            class_310 var1 = getMc();
            if (var1.field_1724 == null) {
               return false;
            } else {
               return wp.o() == wp.z.m
                  ? true
                  : var1.field_1724 == var0.A
                     && var1.field_1687 == var0.N
                     && var1.field_1755 == null
                     && var1.field_1724.field_6012 >= var0.f
                     && (long)var1.field_1724.field_6012 - var0.f <= 1L;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static class_10185 p(class_746 var0, class_10185 var1) {
      if (a != null && a.isEnabled()) {
         MaceModule var2 = a;
         if (var2 != null && var2.isRuntimeActive() && var0 != null && var0 == getMc().field_1724 && wp.o() == wp.z.m) {
            if (var0.method_5624()) {
               var0.method_5728(false);
               q8.v("Mace", "Silent Aim lock released sprint");
            }

            return bn.I(var1);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private void M() {
      this.y = this.l = (boolean)0;
      this.r = (boolean)0;
      this.Y = null;
      this.t = 0;
      this.P = null;
      this.B = "";
      this.A = null;
      this.N = null;
      this.f = Integer.MIN_VALUE;
      this.g = "Waiting";
   }

   private static String T(class_1297 var0) {
      return var0 == null ? "?" : var0.method_5477().getString();
   }

   private static String q(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static l3 l(String var0, final String var1, String var2, boolean var3) {
      return new l3(var0, var2, var3) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static lo n(String var0, final String var1, String var2, double var3, double var5, double var7, double var9) {
      return new lo(var0, var2, var3, var5, var7, var9) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }

   private static li I(String var0, final String var1, String var2, String var3, String... var4) {
      return new li(var0, var2, var3, var4) {
         @Override
         public String getDisplayName() {
            return var1;
         }
      };
   }
}
