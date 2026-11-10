package com.larpclient.module;

import com.larp.obf.q4031f93fe72647cb.q586814c6d8f3640b;
import com.larpclient.LarpClient;
import com.larpclient.bd;
import com.larpclient.bn;
import com.larpclient.cr;
import com.larpclient.fk;
import com.larpclient.j;
import com.larpclient.q8;
import com.larpclient.s5;
import com.larpclient.t9;
import com.larpclient.wp;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.class_10185;
import net.minecraft.class_11909;
import net.minecraft.class_11910;
import net.minecraft.class_1309;
import net.minecraft.class_1747;
import net.minecraft.class_1819;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2846;
import net.minecraft.class_310;
import net.minecraft.class_315;
import net.minecraft.class_3532;
import net.minecraft.class_3966;
import net.minecraft.class_640;
import net.minecraft.class_744;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2846.class_2847;
import org.lwjgl.glfw.GLFW;

public final class AuraModule extends Module {
   private static AuraModule hO;
   public static volatile boolean c;
   private final li n = (li)this.addSetting(
         new li("Activation", "Toggle acts until you switch it off; Hold only acts while the module shortcut is held", "Toggle", "Toggle", "Hold")
      )
      .M("Keybind Mode");
   private final li h1 = this.addSetting(
      new li("Rotation", "Rotation engine that turns the player toward the aim point", "Sloth", "Sloth", "FunTime", "Polar", "Matrix", "Legit") {
         @Override
         public void setValue(String var1) {
            super.setValue(AuraModule.X(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(AuraModule.X(var1));
         }
      }
   );
   private final li F = this.addSetting(
      new li("Aura Mode", "Blatant aims and swings on every chance; Normal waits until the attack is about to land", "Normal", "Blatant", "Normal") {
         @Override
         public void setValue(String var1) {
            super.setValue(AuraModule.q(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(AuraModule.q(var1));
         }
      }
   );
   private final li he = this.addSetting(new li("Combat Version", "1.9+ waits for the attack charge; 1.8 clicks at the CPS range", "1.9", "1.9", "1.8") {
      @Override
      public void setValue(String var1) {
         super.setValue("1.9+".equals(var1) ? "1.9" : var1);
      }

      @Override
      public boolean acceptsValue(String var1) {
         return "1.9+".equals(var1) || super.acceptsValue(var1);
      }

      @Override
      public String getModeLabel(String var1) {
         return "1.9".equals(var1) ? "1.9+" : var1;
      }

      @Override
      public String getValueAsString() {
         return this.getModeLabel(this.getValue());
      }
   });
   private final lo A = this.addSetting(new lo("Attack Range", "Maximum distance to the target box for an attack", 3.0, 2.0, 6.0, 0.1));
   private final lo H = this.addSetting(new lo("Aim Range", "Extra distance beyond Attack Range where targets are tracked", 1.5, 0.0, 9.0, 0.1));
   private final lo hC = this.addSetting(new lo("FOV", "Field of view around your camera for new targets", 360.0, 30.0, 360.0, 5.0));
   private final l3 h6 = this.addSetting(new l3("Players", "Target players", true));
   private final l3 B = this.addSetting(new l3("Mobs", "Target hostile and neutral mobs", true));
   private final l3 Y = this.addSetting(new l3("Animals", "Target animals", false));
   private final l3 T = this.addSetting(new l3("Invisibles", "Target invisible players", true));
   private final l3 x = this.addSetting(new l3("Naked Players", "Target players without armor", true));
   private final li u = this.addSetting(
      new li(
         "Criticals", "Adaptive lands criticals when available; Always waits for a falling hit; None ignores them", "Adaptive", "Adaptive", "Always", "None"
      )
   );
   private final li K = this.addSetting(new li("Aim Point", "Where on the target box to aim", "Nearest", "Nearest", "Center", "Multipoint", "Head", "Body"));
   private final l3 v = this.addSetting(new l3("Predict", "Lead moving targets", false));
   private final lo f = this.addSetting(new lo("Predict Value", "Ticks of target motion to lead", 1.0, 0.0, 4.0, 0.1));
   private final lo t = this.addSetting(new lo("Reach Buffer", "Distance subtracted from Attack Range as a safety margin", 0.2, 0.0, 1.0, 0.05));
   private final l3 q = this.addSetting(new l3("Rotation ETA", "Retained from the reference, which never reads it; it has no effect", true));
   private final l3 hv = this.addSetting(new l3("Entity Raycast", "Attack whichever valid entity the final aim ray actually hits", true));
   private final l3 Z = this.addSetting(new l3("Dynamic Safe Reach", "Shorten reach while the gap to the target is changing quickly, scaled by ping", true));
   private final l3 N = this.addSetting(new l3("Predict Criticals", "Start the sprint reset a tick or two before the jump arc falls", true));
   private final l3 i = this.addSetting(new l3("Randomize Timing", "Attack at a randomized charge between 97 and 100 percent", true));
   private final li r = this.addSetting(
      new li(
         "Sprint Mode",
         "Legit drops sprint a tick before criticals; Keep Sprint keeps it held; None leaves sprint alone",
         "Legit",
         "Legit",
         "Keep Sprint",
         "None"
      )
   );
   private final l3 g = this.addSetting(new l3("Through Walls", "Select and attack targets behind blocks", false));
   private final l3 hu = this.addSetting(
      new l3("Wall Bypass", "Through-wall attacks that first send break start/abort actions for blocks along the line of sight", false)
   );
   private final l3 Q = this.addSetting(new l3("Shield Breaker", "Swap to your best hotbar axe for hits on a raised shield, then back next tick", true));
   private final l3 ha = this.addSetting(new l3("Shield Unpress", "Lower your own shield right before attacking", false));
   private final l3 hk = this.addSetting(new l3("Raycast", "Require a block-free line to the target for each attack", true));
   private final l3 I = this.addSetting(new l3("TPS Sync", "Scale timing and prediction to the measured server TPS", true));
   private final l3 hj = this.addSetting(new l3("Eating", "Do not attack while eating or drinking", true));
   private final l3 h = this.addSetting(new l3("Breaking", "Do not attack while mining", false));
   private final l3 L = this.addSetting(new l3("Placing", "Do not attack while placing blocks", false));
   private final l3 m = this.addSetting(new l3("In Inventory", "Do not attack with a screen open", false));
   private final l3 hP = this.addSetting(new l3("In Lava", "Do not attack while in lava", false));
   private final l3 W = this.addSetting(new l3("In Web", "Do not attack while in cobweb", false));
   private final l3 y = this.addSetting(new l3("In Water", "Do not attack while in water", false));
   private final li hI = this.addSetting(
      new li("Movement Reference", "Target follows combat facing; Camera uses Move Fix to follow your view", "Target", "Target", "Camera")
   );
   private final li O = this.addSetting(
      new li(
         "Move Fix",
         "Silent and Free steer your keys toward the camera direction; Targeted walks at the target on W; None moves exactly with the camera",
         "Silent",
         "Silent",
         "Targeted",
         "Free",
         "None"
      )
   );
   private final lo hU = this.addSetting(new lo("Min CPS", "Lowest 1.8 clicks per second", 8.0, 4.0, 16.0, 0.5));
   private final lo hH = this.addSetting(new lo("Max CPS", "Highest 1.8 clicks per second", 11.0, 4.0, 16.0, 0.5));
   private final l3 hL = this.addSetting(new l3("Auto Jump", "Jump to set up criticals", false));
   private final l3 hz = this.addSetting(new l3("Smart Aim Visibility", "Move the aim point to the nearest visible part of the target", true) {
      @Override
      public String getDisplayName() {
         return "Smart Aim (Visibility)";
      }
   });
   private final l3 hX = this.addSetting(new l3("Focus Target", "Keep the current target while it stays valid", true));
   private final li j = this.addSetting(new li("Sort Mode", "Order for choosing a new target", "Distance", "Distance", "Health", "FOV", "Armor"));
   private final l3 G = this.addSetting(new l3("Deliberate Miss", "Occasionally swing just past the target", false));
   private final lo k = this.addSetting(new lo("Miss Threshold", "Average hits between deliberate misses", 28.0, 10.0, 80.0, 1.0));
   private final l3 hm = this.addSetting(new l3("FOV Cone", "Show the attack range arc on the ground", true));
   private final lo hb = this.addSetting(new lo("Indicator Width", "Thickness of FOV range lines", 3.5, 1.0, 6.0, 0.5));
   private final l3 hN = this.addSetting(new l3("Visible Lines", "Keep FOV lines visible over terrain and entities", false));
   private final l3 h3 = this.addSetting(new l3("Hit Zone", "Terrain positioning guide around the current target", true));
   private final lo d = this.addSetting(new lo("Zone Width", "Ground boundary stroke width", 2.0, 1.0, 6.0, 0.5));
   private final lo l = this.addSetting(new lo("Zone Opacity", "Ground guide opacity", 0.65, 0.05, 1.0, 0.05));
   private final l3 D = this.addSetting(new l3("Zone Fill", "Faint fill on supported collision surfaces", false));
   private final l3 R = this.addSetting(new l3("Zone Bloom", "Soft glow through the shared visual effects", true));
   private final l3 a = this.addSetting(new l3("Zone Motion", "Traveling highlights along the terrain boundary", true));
   private final lz hF = new lz();
   private final l V = new l(this);
   private final ax P = new ax();
   private final ly e = new ly();
   private final l6 J = new l6();
   private final lg z = new lg();
   private final a hw = new a();
   private final ll h2 = new ll();
   private final lh b = new lh();
   private z U;
   private class_1309 M;
   private class_1309 hy;
   private float S;
   private float w;
   private long p;
   private int s = Integer.MIN_VALUE;
   private double h8 = 1.0;
   private boolean X;
   private int E;
   private boolean hJ;
   private String C = "Idle";
   private int o;
   private int hs = Integer.MIN_VALUE;

   public AuraModule() {
      super("Aura", "Automatically attacks nearby entities", Category.COMBAT, -1);
      hO = this;
      this.f.O(this.v::getValue);
      this.O.O(() -> this.hI.G("Camera"));
      this.N.O(() -> !this.u.G("None"));
      this.i.O(() -> !this.i());
      this.hU.O(this::i);
      this.hH.O(this::i);
      this.k.O(this.G::getValue);
      this.hb.O(this.hm::getValue);
      this.hN.O(this.hm::getValue);
      this.d.O(this.h3::getValue);
      this.l.O(this.h3::getValue);
      this.D.O(this.h3::getValue);
      this.R.O(this.h3::getValue);
      this.a.O(this.h3::getValue);
      this.h1.a(var1 -> this.I("Rotation engine: " + var1));
   }

   public static AuraModule R() {
      return hO;
   }

   @Override
   public String getDisplayName() {
      return "Kill Aura";
   }

   public boolean y() {
      return this.n.G("Hold");
   }

   @Override
   public li keybindMode() {
      return this.n;
   }

   @Override
   public void toggle() {
      if (this.y() && mc != null && mc.field_1705 != null && mc.field_1755 == null) {
         this.setEnabled(true);
      } else {
         super.toggle();
      }
   }

   private boolean a() {
      if (!this.y()) {
         return true;
      } else {
         int var1 = this.getKeyBind();
         if (!s5.A(var1) || mc.method_22683() == null) {
            return false;
         } else {
            return mc.field_1755 == null && mc.method_1569() ? s5.m(mc.method_22683().method_4490(), var1) : false;
         }
      }
   }

   private static String q(String var0) {
      if ("Balanced".equalsIgnoreCase(var0) || "Balant".equalsIgnoreCase(var0)) {
         return "Blatant";
      } else {
         return "Legit".equalsIgnoreCase(var0) ? "Normal" : var0;
      }
   }

   private static String X(String var0) {
      if ("TEST".equalsIgnoreCase(var0)) {
         return "Legit";
      } else {
         return !"HolyWorld".equals(var0) && !"Neuro".equals(var0) && !"Smooth".equals(var0) ? var0 : "Sloth";
      }
   }

   @Override
   public boolean allowsScreenInteractions() {
      return !this.m.getValue();
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y, l5.F);
   }

   public List<lb<?>> t() {
      return List.of(this.h1, this.F, this.hI, this.O, this.K, this.v, this.f, this.hz, this.q);
   }

   public List<lb<?>> x() {
      return List.of(this.he, this.A, this.H, this.t, this.Z, this.u, this.N, this.r, this.hL, this.i, this.hU, this.hH, this.hv, this.G, this.k);
   }

   public List<lb<?>> M() {
      return List.of(this.h6, this.B, this.Y, this.T, this.x);
   }

   public List<lb<?>> u() {
      return List.of(this.hC, this.j, this.hX);
   }

   public List<lb<?>> m() {
      return List.of(this.g, this.hu, this.Q, this.ha, this.hk, this.I);
   }

   public List<lb<?>> n() {
      return List.of(this.hj, this.h, this.L, this.m, this.hP, this.W, this.y);
   }

   public List<lb<?>> y() {
      return List.of(this.hm, this.hb, this.hN, this.h3, this.d, this.l, this.D, this.R, this.a);
   }

   @Override
   public void onEnable() {
      this.o = 3;
      this.hs = Integer.MIN_VALUE;
      this.V.H();
      this.hF.g();
      this.P.o();
      com.larpclient.module.t.g();
      this.M = this.hy = null;
      c = (boolean)0;
      this.S = this.w = 0.0F;
      this.p = 0L;
      this.s = Integer.MIN_VALUE;
      this.h8 = 1.0;
      this.C();
      this.C = "Idle";
      q8.G(
         "Aura",
         "Movement: Reference="
            + this.hI.getValue()
            + "; Move Fix="
            + this.O.getValue()
            + "; vanilla sprint eligibility; attacks wait for matching sprint reports"
      );
   }

   @Override
   public void onDisable() {
      this.M = this.hy = null;
      c = (boolean)0;
      this.S = this.w = 0.0F;
      this.p = 0L;
      this.s = Integer.MIN_VALUE;
      this.h8 = 1.0;
      this.C();
      this.M();
      if (this.U != null) {
         this.U.reset();
      }

      this.U = null;
      this.V.H(this.getPlayer());
      this.V.K(mc);
      this.V.H();
      this.hF.g();
      this.P.o();
      com.larpclient.module.t.g();
      this.C = "Idle";
   }

   @Override
   public void onTick() {
      this.V.N(mc);
      if (this.isInGame()) {
         class_746 var1 = mc.field_1724;
         if (!this.a()) {
            this.G("Hold released");
         } else if (cr.o(this) && !var1.method_7325() && var1.method_5805()) {
            boolean var43 = false;
            int var42 = this.E;
            if ((-Integer.signum(var42) >>> 31 ^ 1) == 0) {
               byte var45 = 1;
               int var44 = this.E;
               this.E = var44 - var45;
            }

            boolean var2 = this.F.G("Blatant");
            boolean var3 = this.b();
            int var4 = !var3 && this.V.o(var1, this.i(), this.I.getValue(), this.i.getValue(), var2) ? 1 : 0;
            float var5 = this.V.O(var1, this.i(), this.I.getValue(), this.i.getValue(), var2);
            int var6 = !this.g.getValue() && !this.hu.getValue() ? 0 : 1;
            double var7 = Math.max(this.A.getValue() + this.H.getValue(), RodSwapModule.r());
            lz.z var9 = new lz.z(
               var7,
               this.h6.getValue(),
               this.B.getValue(),
               this.Y.getValue(),
               this.T.getValue(),
               this.x.getValue(),
               this.hX.getValue(),
               (boolean)var6,
               this.j.getValue(),
               this.hC.getValue()
            );
            this.M = this.hF.C(var9);
            l4 var10 = new l4(var1.method_36454(), var1.method_36455());
            if (this.M != this.hy) {
               if (this.U != null) {
                  this.U.targetChanged(this.M);
               }

               com.larpclient.module.t.g();
               if (this.M != null) {
                  this.p = System.currentTimeMillis();
                  this.V.p();
                  this.I("Target acquired: entity=" + this.M.method_5628());
               } else {
                  this.M();
                  this.I("Target lost");
               }

               this.hy = this.M;
            }

            if (this.M == null) {
               this.C();
               c = (boolean)0;
               this.V.H(var1);
               this.p = 0L;
               this.M();
               this.C = "No target";
            } else {
               z var11 = this.Y();
               if (var11 != this.U) {
                  if (this.U != null) {
                     this.U.reset();
                  }

                  this.U = var11;
               }

               class_238 var12 = this.M.method_5829();
               double var13 = this.l(this.M);
               int var15 = com.larpclient.module.f.d(var1, var12) <= var13 ? 1 : 0;
               float var16 = 1.25F;
               if (var15 != 0) {
                  l4 var17 = a_.A(com.larpclient.module.t.d(var12));
                  var16 = Math.max(var16, this.U.ticksToReach(var10, var17) + 0.35F);
                  var16 = Math.max(var16, var2 ? 1.0F : 4.5F);
               }

               int var51;
               int var64;
               label245: {
                  label244: {
                     var51 = var3 || var15 == 0 || var4 == 0 && !(var5 <= var16) ? 0 : 1;
                     int var18 = var15 != 0 && this.p > 0L && System.currentTimeMillis() - this.p < (var2 ? 150L : 500L) ? 1 : 0;
                     if (var15 != 0) {
                        if (var2) {
                           break label244;
                        }

                        boolean var47 = false;
                        int var46 = var51;
                        var64 = Integer.signum(var46);
                        if ((var64 | -var64) >>> 31 != 0 || var18 != 0) {
                           break label244;
                        }
                     }

                     var64 = 0;
                     break label245;
                  }

                  var64 = 1;
               }

               int var19 = var64;
               class_243 var20 = this.e(this.M, (boolean)var19);
               class_243 var21 = RodSwapModule.b(this.M);
               if (var21 != null) {
                  var20 = var21;
               }

               l4 var22;
               label224: {
                  label223: {
                     var22 = a_.A(var20);
                     var19 = !(com.larpclient.module.f.d(var1, var12) <= var13) || !var2 && var51 == 0 ? 0 : 1;
                     if (var19 != 0) {
                        if (var4 != 0) {
                           break label223;
                        }

                        float var67;
                        var64 = (var67 = var5 - (var2 ? 0.5F : 1.15F)) == 0.0F ? 0 : (var67 < 0.0F ? -1 : 1);
                        boolean var49 = false;
                        int var48 = var64;
                        if (-Integer.signum(var48) >>> 31 == 0) {
                           break label223;
                        }
                     }

                     var66 = 0;
                     break label224;
                  }

                  var66 = 1;
               }

               byte var23 = var66;
               boolean var24 = this.K((boolean)var23);
               int var25 = this.U == this.b ? 1 : 0;
               if (this.p > 0L && var19 == 0 && var25 == 0) {
                  long var26 = System.currentTimeMillis() - this.p;
                  long var28 = var2 ? 0L : 120L;
                  float var30 = class_3532.method_15363((float)var26 / (float)var28, 0.0F, 1.0F);
                  if (var30 < 1.0F) {
                     var22 = new l4(var10.j + a_.x(var10.j, var22.j) * var30, var10.l + (var22.l - var10.l) * var30);
                  }
               }

               l4 var53 = U(var1);
               boolean var27 = com.larpclient.module.f.n(
                  var1, var12.method_1014(this.M.method_5871()), var53.j, var53.l, this.A.getValue() + 1.0, (boolean)var6
               );
               int var54 = var19 == 0 && var21 == null ? 0 : 1;
               l4 var29 = this.U.rotate(var10, var22, this.M, (boolean)var54, var27);
               if (var2 && var25 == 0) {
                  this.S += 0.015F;
                  double var56 = this.e.B(this.S, 0.0, 0.0);
                  double var32 = this.e.B(0.0, this.S, 0.0);
                  float var34 = var54 != 0 ? 0.15F : 1.0F;
                  var29.j += (float)(var56 * 0.12F * var34);
                  var29.l = class_3532.method_15363(var29.l + (float)(var32 * 0.08F * var34), -90.0F, 90.0F);
               }

               if (this.G.getValue()) {
                  this.P.F(this.k.getValue().intValue());
                  this.P.X();
                  if (this.P.X()) {
                     var29 = this.P.u(var29);
                  }
               } else {
                  this.P.o();
               }

               if (this.U == this.b) {
                  var29 = this.b.c(var10, var29);
               } else {
                  var29 = a_.Z(var10, var29);
               }

               this.x(var29);
               if (!var24 && !var3 && (!this.G.getValue() || !this.P.X())) {
                  boolean var57 = com.larpclient.module.f.n(var1, var12.method_1014(this.M.method_5871()), var29.j, var29.l, var13, (boolean)var6);
                  l4 var31 = !var2 && !var57 ? var53 : var29;
                  class_1309 var58 = this.M;
                  class_238 var33 = var12;
                  double var59 = var13;
                  byte var36 = 1;
                  if (this.hv.getValue()) {
                     class_3966 var37 = com.larpclient.module.f.h(var1, var31.j, var31.l, var13, (boolean)var6);
                     var36 = 0;
                     if (var37 != null && var37.method_17782() instanceof class_1309 var38 && this.hF.X(var38, var9)) {
                        double var62 = this.l(var38);
                        class_238 var41 = var38.method_5829();
                        if (com.larpclient.module.f.d(var1, var41) <= var62) {
                           var58 = var38;
                           var33 = var41;
                           var59 = var62;
                           var36 = 1;
                        }
                     }
                  }

                  class_1309 var60 = var58;
                  Runnable var61 = this.hu.getValue() ? () -> R(var60) : null;
                  boolean var63 = this.V
                     .d(
                        var1,
                        new l.b(
                           var58,
                           var33,
                           var31.j,
                           var31.l,
                           var59,
                           (boolean)var6,
                           this.n(),
                           this.p(),
                           this.Q.getValue(),
                           this.hj.getValue(),
                           this.hk.getValue() && var6 == 0,
                           (boolean)var36,
                           this.hL.getValue(),
                           this.N.getValue(),
                           var61,
                           this.i(),
                           this.I.getValue(),
                           this.i.getValue(),
                           var2,
                           this.hU.getValue().floatValue(),
                           this.hH.getValue().floatValue()
                        )
                     );
                  this.P();
                  if (var63) {
                     this.I("Attack executed: entity=" + var58.method_5628());
                     if (this.G.getValue()) {
                        this.P.G();
                     }
                  }
               } else if (var3) {
                  this.V.H(var1);
               }

               this.C = var15 != 0 ? (var4 != 0 ? "Attacking" : "Charging") : "Tracking";
            }
         } else {
            this.G("Interaction unavailable");
         }
      }
   }

   private void G(String var1) {
      if (this.M != null || this.hy != null) {
         this.I("Paused: " + var1);
      }

      this.M = this.hy = null;
      c = (boolean)0;
      this.V.H(this.getPlayer());
      this.C();
      this.M();
      this.C = var1;
   }

   private double l(class_1309 var1) {
      double var2 = Math.max(0.0, this.A.getValue() - this.t.getValue());
      if (this.Z.getValue() && mc.field_1724 != null && var1 != null) {
         class_238 var4 = var1.method_5829();
         double var5 = (var4.field_1323 + var4.field_1320) * 0.5 - mc.field_1724.method_23317();
         double var7 = (var4.field_1321 + var4.field_1324) * 0.5 - mc.field_1724.method_23321();
         double var9 = var5 * var5 + var7 * var7;
         if (var9 <= 1.0E-6) {
            return var2;
         }

         double var11 = 1.0 / Math.sqrt(var9);
         var5 *= var11;
         var7 *= var11;
         class_243 var13 = mc.field_1724.method_18798();
         class_243 var14 = var1.method_18798();
         double var15 = Math.abs((var14.field_1352 - var13.field_1352) * var5 + (var14.field_1350 - var13.field_1350) * var7);
         boolean var17 = this.F.G("Blatant");
         double var18 = var15 * this.y() * (var17 ? 0.15 : 0.4);
         double var20 = class_3532.method_15350(var18, 0.0, var17 ? 0.08 : 0.25);
         return Math.max(0.0, var2 - var20);
      } else {
         return var2;
      }
   }

   private double y() {
      int var1 = mc.field_1724.field_6012;
      if (this.s == Integer.MIN_VALUE || var1 < this.s || var1 - this.s >= 20) {
         double var2 = 1.0;
         if (mc.method_1562() != null) {
            class_640 var4 = mc.method_1562().method_2871(mc.field_1724.method_5667());
            if (var4 != null) {
               var2 = var4.method_2959() / 50.0F;
            }
         }

         this.h8 = class_3532.method_15350(var2, 0.5, 3.0);
         this.s = var1;
      }

      return this.h8;
   }

   private class_243 e(class_1309 var1, boolean var2) {
      double var3 = this.A.getValue() + this.H.getValue();
      int var5 = this.U == this.b ? 1 : 0;
      int var6 = var5;
      int var7 = this.U == this.b && this.v.getValue() && !var2 ? 1 : 0;
      class_243 var8 = var7 != 0 ? ap.f(var1, this.F()) : (this.v.getValue() && !var2 && var6 == 0 ? ap.f(var1, this.r()) : var1.method_73189());
      class_238 var9 = var1.method_5829()
         .method_989(var8.field_1352 - var1.method_23317(), var8.field_1351 - var1.method_23318(), var8.field_1350 - var1.method_23321());
      class_243 var10;
      if (var6 == 0 && !this.K.G("Center") && (!this.F.G("Normal") || this.K.G("Head") || this.K.G("Body") || this.K.G("Multipoint"))) {
         if (this.K.G("Multipoint")) {
            var10 = com.larpclient.module.t.F(var1, var9, var3);
         } else if (this.K.G("Head")) {
            var10 = new class_243(var9.method_1005().field_1352, var9.field_1322 + var9.method_17940() * 0.78F, var9.method_1005().field_1350);
         } else if (this.K.G("Body")) {
            var10 = new class_243(var9.method_1005().field_1352, var9.field_1322 + var9.method_17940() * 0.55F, var9.method_1005().field_1350);
         } else {
            var10 = com.larpclient.module.t.d(var9);
         }
      } else {
         var10 = new class_243(var9.method_1005().field_1352, var9.field_1322 + var9.method_17940() * 0.6F, var9.method_1005().field_1350);
      }

      if (var2 && var5 != 0) {
         var10 = T(var1, var9, var10);
      }

      int var11 = var2 || !this.F.G("Normal") && var5 == 0 || var6 != 0 && var5 == 0 || var5 != 0 && O(var1) ? 0 : 1;
      if (var11 != 0) {
         float var12 = var5 != 0 ? 0.032F : 0.012F;
         double var13 = var1.method_17681() * (var5 != 0 ? 0.24F : 0.15F);
         double var15 = var1.method_17682() * (var5 != 0 ? 0.16F : 0.12F);
         this.w += var12;
         double var17 = this.e.B(this.w, 120.0, 0.0) * var13;
         double var19 = this.e.B(0.0, this.w, 240.0) * var15;
         double var21 = this.e.B(0.0, 120.0, this.w) * var13;
         var10 = a(var10.method_1031(var17, var19, var21), var9);
      }

      if (this.hz.getValue() && !this.g.getValue() && !this.hu.getValue()) {
         var10 = com.larpclient.module.t.a(var9, var10, var3);
      }

      return var10;
   }

   private double r() {
      double var1 = 1.0;
      if (this.I.getValue()) {
         float var3 = a1.S();
         if (Float.isFinite(var3) && var3 > 0.0F) {
            var1 = 20.0F / class_3532.method_15363(var3, 10.0F, 20.0F);
         }
      }

      return class_3532.method_15350(this.f.getValue() * var1, 0.0, 4.0);
   }

   private double F() {
      return class_3532.method_15350(Math.max(1.0, this.r()) + 1.0, 1.0, 3.0);
   }

   private static class_243 T(class_1309 var0, class_238 var1, class_243 var2) {
      class_746 var3 = class_310.method_1551().field_1724;
      if (var3 != null && var0 != null && var1 != null) {
         class_243 var4 = var0.method_18798();
         class_243 var5 = var3.method_18798();
         double var6 = (var0.method_23317() - var0.field_6014) * 0.7F + var4.field_1352 * 0.3F - var5.field_1352;
         double var8 = (var0.method_23318() - var0.field_6036) * 0.7F + var4.field_1351 * 0.3F - var5.field_1351;
         double var10 = (var0.method_23321() - var0.field_5969) * 0.7F + var4.field_1350 * 0.3F - var5.field_1350;
         double var12 = Math.min(0.08F, var1.method_17939() * 0.25);
         double var14 = Math.min(0.08F, var1.method_17941() * 0.25);
         double var16 = Math.min(0.12F, var1.method_17940() * 0.12F);
         double var18 = var1.method_17939() * 0.22F;
         double var20 = var1.method_17941() * 0.22F;
         double var22 = var1.method_17940() * 0.1F;
         class_243 var24 = var1.method_1005();
         return new class_243(
            class_3532.method_15350(var24.field_1352 + class_3532.method_15350(var6 * 0.65F, -var18, var18), var1.field_1323 + var12, var1.field_1320 - var12),
            class_3532.method_15350(var24.field_1351 + class_3532.method_15350(var8 * 0.45F, -var22, var22), var1.field_1322 + var16, var1.field_1325 - var16),
            class_3532.method_15350(var24.field_1350 + class_3532.method_15350(var10 * 0.65F, -var20, var20), var1.field_1321 + var14, var1.field_1324 - var14)
         );
      } else {
         return var2;
      }
   }

   private static boolean O(class_1309 var0) {
      class_746 var1 = class_310.method_1551().field_1724;
      if (var0 != null && var1 != null) {
         class_243 var2 = var0.method_18798();
         class_243 var3 = var1.method_18798();
         double var4 = (var0.method_23317() - var0.field_6014) * 0.7F + var2.field_1352 * 0.3F - var3.field_1352;
         double var6 = (var0.method_23321() - var0.field_5969) * 0.7F + var2.field_1350 * 0.3F - var3.field_1350;
         return var4 * var4 + var6 * var6 >= 0.0025F;
      } else {
         return false;
      }
   }

   private static class_243 a(class_243 var0, class_238 var1) {
      double var2 = 0.001F;
      return new class_243(
         class_3532.method_15350(var0.field_1352, var1.field_1323 + var2, var1.field_1320 - var2),
         class_3532.method_15350(var0.field_1351, var1.field_1322 + var2, var1.field_1325 - var2),
         class_3532.method_15350(var0.field_1350, var1.field_1321 + var2, var1.field_1324 - var2)
      );
   }

   private z Y() {
      return switch ((String)this.h1.getValue()) {
         case "FunTime" -> this.z;
         case "Polar" -> this.hw;
         case "Matrix" -> this.h2;
         case "Legit" -> this.b;
         default -> this.J;
      };
   }

   private l.z n() {
      return switch ((String)this.u.getValue()) {
         case "Always" -> l.z.R;
         case "None" -> l.z.x;
         default -> l.z.H;
      };
   }

   private l.e p() {
      return switch ((String)this.r.getValue()) {
         case "Keep Sprint" -> l.e.i;
         case "None" -> l.e.r;
         default -> l.e.s;
      };
   }

   private boolean i() {
      return this.he.G("1.8");
   }

   private boolean b() {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         g var1 = com.larpclient.module.g.K();
         b var2 = LarpClient.b().G().b(b.class);
         if (var1 != null && var1.isRuntimeActive() && fk.b(var1)) {
            return true;
         } else if (var2 != null && var2.isRuntimeActive() && fk.b(var2)) {
            return true;
         } else {
            AutoAnchorModule var3 = AutoAnchorModule.T();
            if (var3 != null && var3.isRuntimeActive() && fk.b(var3)) {
               return true;
            } else if (this.hj.getValue() && com.larpclient.module.f.X()) {
               return true;
            } else if (this.h.getValue() && this.J()) {
               return true;
            } else if (this.L.getValue() && this.n()) {
               return true;
            } else if (this.m.getValue() && mc.field_1755 != null) {
               return true;
            } else if (this.hP.getValue() && mc.field_1724.method_5771()) {
               return true;
            } else {
               return this.W.getValue() && com.larpclient.module.f.i()
                  ? true
                  : this.y.getValue() && (mc.field_1724.method_5799() || mc.field_1724.method_5869());
            }
         }
      } else {
         return true;
      }
   }

   private boolean J() {
      return mc.field_1761 != null && mc.field_1761.method_2923()
         ? true
         : mc.field_1690 != null && mc.field_1690.field_1886.method_1434() && mc.field_1765 != null && mc.field_1765.method_17783() == class_240.field_1332;
   }

   private boolean n() {
      return mc.field_1690 != null && mc.field_1690.field_1904.method_1434()
         ? mc.field_1724.method_6047().method_7909() instanceof class_1747 || mc.field_1724.method_6079().method_7909() instanceof class_1747
         : false;
   }

   private boolean K(boolean var1) {
      if (!this.ha.getValue() || !var1) {
         this.C();
         return false;
      }

      if (this.X) {
         return false;
      }

      if (!mc.field_1724.method_6115() || !(mc.field_1724.method_6030().method_7909() instanceof class_1819)) {
         return false;
      }

      if (mc.field_1761 == null) {
         return false;
      }

      if (mc.field_1690 != null) {
         mc.field_1690.field_1904.method_23481(false);
         this.hJ = (boolean)1;
      }

      mc.field_1761.method_2897(mc.field_1724);
      this.X = (boolean)1;
      return true;
   }

   private void P() {
      if (this.X) {
         this.E = 1;
      }

      this.X = (boolean)0;
      this.d();
   }

   private void C() {
      this.X = (boolean)0;
      this.E = 0;
      this.d();
   }

   private void d() {
      if (this.hJ && mc.field_1690 != null) {
         int var1 = this.M();
         int var2;
         if (var1 >= 0 && mc.method_22683() != null) {
            var2 = GLFW.glfwGetMouseButton(mc.method_22683().method_4490(), var1) == 1 ? 1 : 0;
         } else {
            var2 = mc.field_1690.field_1904.method_1434();
         }

         mc.field_1690.field_1904.method_23481((boolean)var2);
         this.hJ = (boolean)0;
      } else {
         this.hJ = (boolean)0;
      }
   }

   private int M() {
      if (mc.field_1690 == null) {
         return -1;
      }

      for (int var1 = 0; var1 <= 7; var1++) {
         if (mc.field_1690.field_1904.method_1433(new class_11909(0.0, 0.0, new class_11910(var1, 0)))) {
            return var1;
         }
      }

      return -1;
   }

   private static void R(class_1309 var0) {
      class_310 var1 = class_310.method_1551();
      if (var1.field_1724 != null && var1.field_1687 != null && var0 != null) {
         class_243 var2 = var1.field_1724.method_33571();
         class_243 var3 = var0.method_33571().method_1020(var2);
         double var4 = var3.method_1033();
         if (!(var4 < 0.01)) {
            class_243 var6 = var3.method_1029();

            for (double var7 = 0.5; var7 < var4; var7 += 0.5) {
               class_243 var9 = var2.method_1019(var6.method_1021(var7));
               class_2338 var10 = new class_2338((int)Math.floor(var9.field_1352), (int)Math.floor(var9.field_1351), (int)Math.floor(var9.field_1350));
               if (!var1.field_1687.method_22347(var10)) {
                  var1.field_1724.field_3944.method_52787(new class_2846(class_2847.field_12968, var10, class_2350.field_11036));
                  var1.field_1724.field_3944.method_52787(new class_2846(class_2847.field_12971, var10, class_2350.field_11036));
               }
            }
         }
      }
   }

   private void x(l4 var1) {
      if (mc.field_1755 == null && !com.larpclient.j.F("Freelook") && !com.larpclient.j.F("Freecam")) {
         wp.S(wp.z.L);
      }

      t9.O(this, t9.v.m, var1.j, var1.l, 1.0);
   }

   private void M() {
      wp.k(wp.z.L);
   }

   private static l4 U(class_746 var0) {
      q586814c6d8f3640b var1 = (q586814c6d8f3640b)var0;
      return new l4(var1.larpYRotLast(), var1.larpXRotLast());
   }

   public boolean G() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.M != null && cr.W(this);
   }

   public boolean Z() {
      return !this.isEnabled() ? false : this.G() && this.hI.G("Camera") && this.O.G("None") && wp.o() == wp.z.L;
   }

   public static class_10185 j(class_746 var0, class_10185 var1) {
      if (hO != null && hO.isEnabled()) {
         AuraModule var2 = hO;
         if (var2 != null && var2.isRuntimeActive() && var2.M != null && var0 != null) {
            float var3 = var1.comp_3159() == var1.comp_3160() ? 0.0F : (var1.comp_3159() ? 1.0F : -1.0F);
            byte var4 = var1.comp_3165();
            if (var2.V.q(var0)) {
               var4 = 0;
            } else if (var2.r.G("Keep Sprint") && var3 > 0.0F && !var1.comp_3164() && !var0.field_5976 && !var0.method_5799() && !var0.method_5869()) {
               var4 = 1;
            } else if (var2.r.G("Legit")
               && (var1.comp_3165() || getMc().field_1690 != null && getMc().field_1690.field_1867.method_1434())
               && var3 > 0.0F
               && !var1.comp_3164()
               && !var0.field_5976) {
               var4 = 1;
            }

            class_10185 var5 = new class_10185(
               var1.comp_3159(), var1.comp_3160(), var1.comp_3161(), var1.comp_3162(), var1.comp_3163(), var1.comp_3164(), (boolean)var4
            );
            if (wp.o() == wp.z.L) {
               double var6 = var2.M.method_23317() - var0.method_23317();
               double var8 = var2.M.method_23321() - var0.method_23321();
               double var10 = class_3532.method_15338(Math.toDegrees(Math.atan2(var8, var6)) - 90.0);
               var5 = var2.c(var0.field_3913, var5, var0.method_36454(), var2.hI.G("Camera") ? s() : var0.method_36454(), var10);
            }

            return var5;
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public class_10185 c(class_744 var1, class_10185 var2, float var3, float var4, double var5) {
      if (!this.isEnabled()) {
         return var2;
      }

      if (!this.hI.G("Target") && !this.O.G("None")) {
         float var7 = var2.comp_3159() == var2.comp_3160() ? 0.0F : (var2.comp_3159() ? 1.0F : -1.0F);
         float var8 = var2.comp_3161() == var2.comp_3162() ? 0.0F : (var2.comp_3161() ? 1.0F : -1.0F);
         if (var7 == 0.0F && var8 == 0.0F) {
            return var2;
         }

         double var9 = this.O.G("Targeted") && var7 > 0.0F && var8 == 0.0F ? var5 : class_3532.method_15338(Math.toDegrees(m(var4, var7, var8)));
         float[] var11 = V(var9, var3);
         var1.field_55868 = new class_241(var11[1], var11[0]).method_35581();
         return new class_10185(var11[0] > 0.0F, var11[0] < 0.0F, var11[1] > 0.0F, var11[1] < 0.0F, var2.comp_3163(), var2.comp_3164(), var2.comp_3165());
      } else {
         return var2;
      }
   }

   public static double m(float var0, float var1, float var2) {
      if (var1 < 0.0F) {
         var0 += 180.0F;
      }

      float var3 = 1.0F;
      if (var1 < 0.0F) {
         var3 = -0.5F;
      }

      if (var1 > 0.0F) {
         var3 = 0.5F;
      }

      if (var2 > 0.0F) {
         var0 -= 90.0F * var3;
      }

      if (var2 < 0.0F) {
         var0 += 90.0F * var3;
      }

      return Math.toRadians(var0);
   }

   public static float[] V(double var0, float var2) {
      double var3 = class_3532.method_15338(var0 - var2);
      if (var3 >= -22.5 && var3 <= 22.5) {
         return new float[]{1.0F, 0.0F};
      } else if (var3 > 22.5 && var3 <= 67.5) {
         return new float[]{1.0F, -1.0F};
      } else if (var3 > 67.5 && var3 <= 112.5) {
         return new float[]{0.0F, -1.0F};
      } else if (var3 > 112.5 && var3 <= 157.5) {
         return new float[]{-1.0F, -1.0F};
      } else if (var3 > 157.5 || var3 <= -157.5) {
         return new float[]{-1.0F, 0.0F};
      } else if (var3 < -22.5 && var3 >= -67.5) {
         return new float[]{1.0F, 1.0F};
      } else {
         return var3 < -67.5 && var3 >= -112.5 ? new float[]{0.0F, 1.0F} : new float[]{-1.0F, 1.0F};
      }
   }

   public static boolean d() {
      if (hO != null && hO.isEnabled()) {
         AuraModule var0 = hO;
         return var0 != null && var0.isRuntimeActive() && var0.ha.getValue() && x() != null && (var0.X || var0.E > 0);
      } else {
         return false;
      }
   }

   public float F(float var1, float var2) {
      if (this.isEnabled()) {
         if (!this.G()) {
            return var1;
         } else if (this.k()) {
            float var4 = class_3532.method_15393(s() - var1);
            return Math.abs(var4) > 0.5F ? var1 + var4 * 0.45F : s();
         } else {
            boolean var3 = this.F.G("Normal");
            return p(var1, var2, var3 ? 35.0F : 50.0F, var3 ? 0.25F : 0.3F);
         }
      } else {
         return var1;
      }
   }

   public static float p(float var0, float var1, float var2, float var3) {
      float var4 = class_3532.method_15393(var1 - var0);
      if (Math.abs(var4) <= var2) {
         return var0;
      }

      float var5 = var4 < 0.0F ? var1 + var2 : var1 - var2;
      return var0 + class_3532.method_15393(var5 - var0) * var3;
   }

   private boolean k() {
      class_746 var1 = x();
      if (this.hI.G("Camera") && this.O.G("Targeted") && var1 != null && var1.field_3913 != null && this.M != null) {
         class_241 var2 = var1.field_3913.method_3128();
         if (!(var2.field_1342 <= 0.0F) && !(Math.abs(var2.field_1343) > 0.05F)) {
            float var3 = class_3532.method_15393(
               (float)Math.toDegrees(Math.atan2(this.M.method_23321() - var1.method_23321(), this.M.method_23317() - var1.method_23317())) - 90.0F
            );
            return Math.abs(class_3532.method_15393(var3 - s())) <= 15.0F;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static class_746 x() {
      return getMc().field_1724;
   }

   public static boolean N() {
      if (hO != null && hO.isEnabled()) {
         AuraModule var0 = hO;
         return var0 != null && var0.isRuntimeActive() && c;
      } else {
         return false;
      }
   }

   public static float s() {
      class_310 var0 = class_310.method_1551();
      return X() ? wp.N(wp.g(), wp.J(), var0.field_1690.method_31044().method_31035())[0] : var0.field_1724.method_36454();
   }

   private static boolean X() {
      wp.z var0 = wp.o();
      return var0 == wp.z.L || var0 == wp.z.m || var0 == wp.z.u;
   }

   public static float d() {
      class_310 var0 = class_310.method_1551();
      return X() ? wp.N(wp.g(), wp.J(), var0.field_1690.method_31044().method_31035())[1] : var0.field_1724.method_36455();
   }

   public static class_243 e() {
      return a_.w(s(), d());
   }

   public double D() {
      return this.A.getValue();
   }

   public class_1309 B() {
      if (this.isEnabled()) {
         return this.isRuntimeActive() ? this.M : null;
      } else {
         return null;
      }
   }

   public double B() {
      return this.hC.getValue();
   }

   public double P() {
      return cr.R(this.A());
   }

   private double A() {
      return Math.max(0.0, this.A.getValue() - this.t.getValue());
   }

   public boolean V() {
      return !this.isEnabled() ? false : this.isOperational() && this.isInGame() && mc.field_1755 == null && this.hm.getValue() && this.a();
   }

   public float Z() {
      return this.hb.getValue().floatValue();
   }

   public boolean T() {
      return this.hN.getValue();
   }

   public boolean Y() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.M != null && this.h3.getValue() && cr.f() && this.a();
   }

   public float Y() {
      return this.d.getValue().floatValue();
   }

   public double w() {
      return this.l.getValue();
   }

   public boolean q() {
      return this.D.getValue();
   }

   public boolean f() {
      return this.R.getValue();
   }

   public boolean m() {
      return this.a.getValue();
   }

   public String n() {
      if (this.M != null && mc.field_1724 != null) {
         String var1 = cr.l(this.M, this.A(), mc.field_1724.method_5720());
         return "range".equals(var1) ? "Outside reach" : (var1 == null ? "Valid attack ray" : "Inside reach: " + var1);
      } else {
         return "No lock";
      }
   }

   @Override
   public String getStatus() {
      return this.C;
   }

   private void I(String var1) {
      q8.v("Aura", var1);
   }

   public static void e(class_746 var0, String var1) {
      AuraModule var2 = hO;
      if (var2 != null && var2.isRuntimeActive() && var2.M != null && var0 != null) {
         if (var1.equals("jump-before")) {
            if (var2.o <= 0) {
               return;
            }

            var2.o--;
            var2.hs = var0.field_6012 + 16;
         }

         if (var0.field_6012 <= var2.hs) {
            class_10185 var3 = var0.field_3913.field_54155;
            class_241 var4 = var0.field_3913.method_3128();
            class_243 var5 = var0.method_18798();
            l4 var6 = U(var0);
            class_315 var7 = getMc().field_1690;
            class_10185 var8 = new class_10185(
               var7.field_1894.method_1434(),
               var7.field_1881.method_1434(),
               var7.field_1913.method_1434(),
               var7.field_1849.method_1434(),
               var7.field_1903.method_1434(),
               var7.field_1832.method_1434(),
               var7.field_1867.method_1434()
            );
            q8.G(
               "AuraMovement",
               String.format(
                  Locale.ROOT,
                  "%s tick=%d mode=%s/%s/%s reference=%s target=%d ground=%s sprint=%s sprintReportMatches=%s reset=%s yaw=%.4f sentYaw=%.4f keys=%s physical=%s input=(%.5f,%.5f) pos=(%.6f,%.6f,%.6f) velocity=(%.6f,%.6f,%.6f) fall=%.5f orbit=%s",
                  var1,
                  var0.field_6012,
                  var2.h1.getValue(),
                  var2.O.getValue(),
                  var2.r.getValue(),
                  var2.hI.getValue(),
                  var2.M.method_5628(),
                  var0.method_24828(),
                  var0.method_5624(),
                  bd.t(var0.method_5624()),
                  c,
                  var0.method_36454(),
                  var6.j,
                  var3,
                  var8,
                  var4.field_1343,
                  var4.field_1342,
                  var0.method_23317(),
                  var0.method_23318(),
                  var0.method_23321(),
                  var5.field_1352,
                  var5.field_1351,
                  var5.field_1350,
                  var0.field_6017,
                  TargetStrafeModule.b()
               )
            );
         }
      }
   }

   public static class_10185 U(class_10185 var0) {
      return bn.I(var0);
   }
}
