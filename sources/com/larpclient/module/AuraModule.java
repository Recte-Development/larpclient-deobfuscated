package com.larpclient.module;

import com.larp.obf.qbac0b262827a15ac.q12d31c89547aa6a5;
import com.larpclient.LarpClient;
import com.larpclient._d;
import com.larpclient._m;
import com.larpclient.cc;
import com.larpclient.hy;
import com.larpclient.m9;
import com.larpclient.mo;
import com.larpclient.mt;
import com.larpclient.pl;
import com.larpclient.te;
import com.larpclient.tq;
import com.larpclient.w7;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class AuraModule extends Module {
   private static AuraModule P;
   public static volatile boolean tC;
   private final d6 k = (d6)this.addSetting(
         new d6("Activation", "Toggle acts until you switch it off; Hold only acts while the module shortcut is held", "Toggle", "Toggle", "Hold")
      )
      .n("Keybind Mode");
   private final d6 tq = this.addSetting(
      new d6("Rotation", "Rotation engine that turns the player toward the aim point", "Sloth", "Sloth", "FunTime", "Polar", "Matrix", "Legit") {
         @Override
         public void setValue(String var1) {
            super.setValue(AuraModule.V(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(AuraModule.V(var1));
         }
      }
   );
   private final d6 R = this.addSetting(
      new d6("Aura Mode", "Blatant aims and swings on every chance; Normal waits until the attack is about to land", "Normal", "Blatant", "Normal") {
         @Override
         public void setValue(String var1) {
            super.setValue(AuraModule.h(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(AuraModule.h(var1));
         }
      }
   );
   private final d6 C = this.addSetting(new d6("Combat Version", "1.9+ waits for the attack charge; 1.8 clicks at the CPS range", "1.9", "1.9", "1.8") {
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
         return this.getModeLabel(this.P());
      }
   });
   private final d9 h = this.addSetting(new d9("Attack Range", "Maximum distance to the target box for an attack", 3.0, 2.0, 6.0, 0.1));
   private final d9 K = this.addSetting(new d9("Aim Range", "Extra distance beyond Attack Range where targets are tracked", 1.5, 0.0, 9.0, 0.1));
   private final d9 t3 = this.addSetting(new d9("FOV", "Field of view around your camera for new targets", 360.0, 30.0, 360.0, 5.0));
   private final dh N = this.addSetting(new dh("Players", "Target players", true));
   private final dh J = this.addSetting(new dh("Mobs", "Target hostile and neutral mobs", true));
   private final dh x = this.addSetting(new dh("Animals", "Target animals", false));
   private final dh c = this.addSetting(new dh("Invisibles", "Target invisible players", true));
   private final dh tA = this.addSetting(new dh("Naked Players", "Target players without armor", true));
   private final d6 E = this.addSetting(
      new d6(
         "Criticals", "Adaptive lands criticals when available; Always waits for a falling hit; None ignores them", "Adaptive", "Adaptive", "Always", "None"
      )
   );
   private final d6 D = this.addSetting(new d6("Aim Point", "Where on the target box to aim", "Nearest", "Nearest", "Center", "Multipoint", "Head", "Body"));
   private final dh A = this.addSetting(new dh("Predict", "Lead moving targets", false));
   private final d9 tJ = this.addSetting(new d9("Predict Value", "Ticks of target motion to lead", 1.0, 0.0, 4.0, 0.1));
   private final d9 U = this.addSetting(new d9("Reach Buffer", "Distance subtracted from Attack Range as a safety margin", 0.2, 0.0, 1.0, 0.05));
   private final dh t0 = this.addSetting(new dh("Rotation ETA", "Retained from the reference, which never reads it; it has no effect", true));
   private final dh tv = this.addSetting(new dh("Entity Raycast", "Attack whichever valid entity the final aim ray actually hits", true));
   private final dh u = this.addSetting(new dh("Dynamic Safe Reach", "Shorten reach while the gap to the target is changing quickly, scaled by ping", true));
   private final dh r = this.addSetting(new dh("Predict Criticals", "Start the sprint reset a tick or two before the jump arc falls", true));
   private final dh t_ = this.addSetting(new dh("Randomize Timing", "Attack at a randomized charge between 97 and 100 percent", true));
   private final d6 ts = this.addSetting(
      new d6(
         "Sprint Mode",
         "Legit drops sprint a tick before criticals; Keep Sprint keeps it held; None leaves sprint alone",
         "Legit",
         "Legit",
         "Keep Sprint",
         "None"
      )
   );
   private final dh t8 = this.addSetting(new dh("Through Walls", "Select and attack targets behind blocks", false));
   private final dh M = this.addSetting(
      new dh("Wall Bypass", "Through-wall attacks that first send break start/abort actions for blocks along the line of sight", false)
   );
   private final dh t9 = this.addSetting(new dh("Shield Breaker", "Swap to your best hotbar axe for hits on a raised shield, then back next tick", true));
   private final dh g = this.addSetting(new dh("Shield Unpress", "Lower your own shield right before attacking", false));
   private final dh a = this.addSetting(new dh("Raycast", "Require a block-free line to the target for each attack", true));
   private final dh B = this.addSetting(new dh("TPS Sync", "Scale timing and prediction to the measured server TPS", true));
   private final dh te = this.addSetting(new dh("Eating", "Do not attack while eating or drinking", true));
   private final dh t7 = this.addSetting(new dh("Breaking", "Do not attack while mining", false));
   private final dh Z = this.addSetting(new dh("Placing", "Do not attack while placing blocks", false));
   private final dh tD = this.addSetting(new dh("In Inventory", "Do not attack with a screen open", false));
   private final dh tx = this.addSetting(new dh("In Lava", "Do not attack while in lava", false));
   private final dh l = this.addSetting(new dh("In Web", "Do not attack while in cobweb", false));
   private final dh tX = this.addSetting(new dh("In Water", "Do not attack while in water", false));
   private final d6 X = this.addSetting(
      new d6("Movement Reference", "Target follows combat facing; Camera uses Move Fix to follow your view", "Target", "Target", "Camera")
   );
   private final d6 v = this.addSetting(
      new d6(
         "Move Fix",
         "Silent and Free steer your keys toward the camera direction; Targeted walks at the target on W; None moves exactly with the camera",
         "Silent",
         "Silent",
         "Targeted",
         "Free",
         "None"
      )
   );
   private final d9 O = this.addSetting(new d9("Min CPS", "Lowest 1.8 clicks per second", 8.0, 4.0, 16.0, 0.5));
   private final d9 F = this.addSetting(new d9("Max CPS", "Highest 1.8 clicks per second", 11.0, 4.0, 16.0, 0.5));
   private final dh q = this.addSetting(new dh("Auto Jump", "Jump to set up criticals", false));
   private final dh tc = this.addSetting(new dh("Smart Aim Visibility", "Move the aim point to the nearest visible part of the target", true) {
      @Override
      public String getDisplayName() {
         return "Smart Aim (Visibility)";
      }
   });
   private final dh n = this.addSetting(new dh("Focus Target", "Keep the current target while it stays valid", true));
   private final d6 S = this.addSetting(new d6("Sort Mode", "Order for choosing a new target", "Distance", "Distance", "Health", "FOV", "Armor"));
   private final dh V = this.addSetting(new dh("Deliberate Miss", "Occasionally swing just past the target", false));
   private final d9 I = this.addSetting(new d9("Miss Threshold", "Average hits between deliberate misses", 28.0, 10.0, 80.0, 1.0));
   private final dh y = this.addSetting(new dh("FOV Cone", "Show the attack range arc on the ground", true));
   private final d9 e = this.addSetting(new d9("Indicator Width", "Thickness of FOV range lines", 3.5, 1.0, 6.0, 0.5));
   private final dh z = this.addSetting(new dh("Visible Lines", "Keep FOV lines visible over terrain and entities", false));
   private final dh i = this.addSetting(new dh("Hit Zone", "Terrain positioning guide around the current target", true));
   private final d9 tR = this.addSetting(new d9("Zone Width", "Ground boundary stroke width", 2.0, 1.0, 6.0, 0.5));
   private final d9 p = this.addSetting(new d9("Zone Opacity", "Ground guide opacity", 0.65, 0.05, 1.0, 0.05));
   private final dh L = this.addSetting(new dh("Zone Fill", "Faint fill on supported collision surfaces", false));
   private final dh H = this.addSetting(new dh("Zone Bloom", "Soft glow through the shared visual effects", true));
   private final dh o = this.addSetting(new dh("Zone Motion", "Traveling highlights along the terrain boundary", true));
   private final dn ty = new dn();
   private final dp tm = new dp(this);
   private final dd f = new dd();
   private final z tO = new z();
   private final i tl = new i();
   private final da tK = new da();
   private final x tG = new x();
   private final db b = new db();
   private final di s = new di();
   private t G;
   private LivingEntity m;
   private LivingEntity T;
   private float t;
   private float d;
   private long j;
   private int Y = Integer.MIN_VALUE;
   private double w = 1.0;
   private boolean tz;
   private int t2;
   private boolean W;
   private String tY = "Idle";
   private int Q;
   private int ta = Integer.MIN_VALUE;

   public AuraModule() {
      super("Aura", "Automatically attacks nearby entities", Category.COMBAT, -1);
      P = this;
      this.tJ.H(this.A::P);
      this.v.H(() -> this.X.S("Camera"));
      this.r.H(() -> !this.E.S("None"));
      this.t_.H(() -> !this.X());
      this.O.H(this::X);
      this.F.H(this::X);
      this.I.H(this.V::P);
      this.e.H(this.y::P);
      this.z.H(this.y::P);
      this.tR.H(this.i::P);
      this.p.H(this.i::P);
      this.L.H(this.i::P);
      this.H.H(this.i::P);
      this.o.H(this.i::P);
      this.tq.h(var1 -> this.l("Rotation engine: " + var1));
   }

   public static AuraModule S() {
      return P;
   }

   @Override
   public String getDisplayName() {
      return "Kill Aura";
   }

   public boolean L() {
      return this.k.S("Hold");
   }

   @Override
   public d6 keybindMode() {
      return this.k;
   }

   @Override
   public void toggle() {
      if (this.L() && mc != null && mc.gui != null && mc.gui.screen() == null) {
         this.setEnabled(true);
      } else {
         super.toggle();
      }
   }

   private boolean o() {
      if (!this.L()) {
         return true;
      } else {
         int var1 = this.getKeyBind();
         if (!cc.N(var1) || mc.getWindow() == null) {
            return false;
         } else {
            return mc.gui.screen() == null && mc.isWindowActive() ? cc.N(mc.getWindow().handle(), var1) : false;
         }
      }
   }

   private static String h(String var0) {
      if ("Balanced".equalsIgnoreCase(var0) || "Balant".equalsIgnoreCase(var0)) {
         return "Blatant";
      } else {
         return "Legit".equalsIgnoreCase(var0) ? "Normal" : var0;
      }
   }

   private static String V(String var0) {
      if ("TEST".equalsIgnoreCase(var0)) {
         return "Legit";
      } else {
         return !"HolyWorld".equals(var0) && !"Neuro".equals(var0) && !"Smooth".equals(var0) ? var0 : "Sloth";
      }
   }

   @Override
   public boolean allowsScreenInteractions() {
      return !this.tD.P();
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z, dm.j);
   }

   public List<df<?>> o() {
      return List.of(this.tq, this.R, this.X, this.v, this.D, this.A, this.tJ, this.tc, this.t0);
   }

   public List<df<?>> C() {
      return List.of(this.C, this.h, this.K, this.U, this.u, this.E, this.r, this.ts, this.q, this.t_, this.O, this.F, this.tv, this.V, this.I);
   }

   public List<df<?>> H() {
      return List.of(this.N, this.J, this.x, this.c, this.tA);
   }

   public List<df<?>> r() {
      return List.of(this.t3, this.S, this.n);
   }

   public List<df<?>> x() {
      return List.of(this.t8, this.M, this.t9, this.g, this.a, this.B);
   }

   public List<df<?>> p() {
      return List.of(this.te, this.t7, this.Z, this.tD, this.tx, this.l, this.tX);
   }

   public List<df<?>> i() {
      return List.of(this.y, this.e, this.z, this.i, this.tR, this.p, this.L, this.H, this.o);
   }

   @Override
   public void onEnable() {
      this.Q = 3;
      this.ta = Integer.MIN_VALUE;
      this.tm.e();
      this.ty.o();
      this.f.i();
      d0.Y();
      this.m = this.T = null;
      tC = (boolean)0;
      this.t = this.d = 0.0F;
      this.j = 0L;
      this.Y = Integer.MIN_VALUE;
      this.w = 1.0;
      this.y();
      this.tY = "Idle";
      mt.K("Aura", "Movement: Reference=" + this.X.P() + "; Move Fix=" + this.v.P() + "; vanilla sprint eligibility; attacks wait for matching sprint reports");
   }

   @Override
   public void onDisable() {
      this.m = this.T = null;
      tC = (boolean)0;
      this.t = this.d = 0.0F;
      this.j = 0L;
      this.Y = Integer.MIN_VALUE;
      this.w = 1.0;
      this.y();
      this.l();
      if (this.G != null) {
         this.G.reset();
      }

      this.G = null;
      this.tm.t(this.getPlayer());
      this.tm.B(mc);
      this.tm.e();
      this.ty.o();
      this.f.i();
      d0.Y();
      this.tY = "Idle";
   }

   @Override
   public void onTick() {
      this.tm.S(mc);
      if (this.isInGame()) {
         LocalPlayer var1 = mc.player;
         if (!this.o()) {
            this.k("Hold released");
         } else if (_m.n(this) && !var1.isSpectator() && var1.isAlive()) {
            boolean var43 = false;
            int var42 = this.t2;
            if ((-Integer.signum(var42) >>> 31 ^ 1) == 0) {
               byte var45 = 1;
               int var44 = this.t2;
               this.t2 = var44 - var45;
            }

            boolean var2 = this.R.S("Blatant");
            byte var3 = this.u();
            int var4 = var3 == 0 && this.tm.M(var1, this.X(), this.B.P(), this.t_.P(), var2) ? 1 : 0;
            float var5 = this.tm.O(var1, this.X(), this.B.P(), this.t_.P(), var2);
            int var6 = !this.t8.P() && !this.M.P() ? 0 : 1;
            double var7 = Math.max(this.h.P() + this.K.P(), RodSwapModule.o());
            dn.m var9 = new dn.m(var7, this.N.P(), this.J.P(), this.x.P(), this.c.P(), this.tA.P(), this.n.P(), (boolean)var6, this.S.P(), this.t3.P());
            this.m = this.ty.k(var9);
            d5 var10 = new d5(var1.getYRot(), var1.getXRot());
            if (this.m != this.T) {
               if (this.G != null) {
                  this.G.targetChanged(this.m);
               }

               d0.Y();
               if (this.m != null) {
                  this.j = System.currentTimeMillis();
                  this.tm.T();
                  this.l("Target acquired: entity=" + this.m.getId());
               } else {
                  this.l();
                  this.l("Target lost");
               }

               this.T = this.m;
            }

            if (this.m == null) {
               this.y();
               tC = (boolean)0;
               this.tm.t(var1);
               this.j = 0L;
               this.l();
               this.tY = "No target";
            } else {
               t var11 = this.H();
               if (var11 != this.G) {
                  if (this.G != null) {
                     this.G.reset();
                  }

                  this.G = var11;
               }

               AABB var12 = this.m.getBoundingBox();
               double var13 = this.o(this.m);
               int var15 = com.larpclient.module.k.L(var1, var12) <= var13 ? 1 : 0;
               float var16 = 1.25F;
               if (var15 != 0) {
                  d5 var17 = dj.J(d0.b(var12));
                  var16 = Math.max(var16, this.G.ticksToReach(var10, var17) + 0.35F);
                  var16 = Math.max(var16, var2 ? 1.0F : 4.5F);
               }

               int var65;
               label261: {
                  boolean var47 = false;
                  byte var46 = var3;
                  var65 = Integer.signum(var46);
                  if ((var65 | -var65) >>> 31 == 0 && var15 != 0) {
                     boolean var49 = false;
                     int var48 = var4;
                     var65 = Integer.signum(var48);
                     if ((var65 | -var65) >>> 31 != 0 || var5 <= var16) {
                        var65 = 1;
                        break label261;
                     }
                  }

                  var65 = 0;
               }

               byte var51 = (byte)var65;
               int var18 = var15 != 0 && this.j > 0L && System.currentTimeMillis() - this.j < (var2 ? 150L : 500L) ? 1 : 0;
               int var19 = var15 == 0 || !var2 && var51 == 0 && var18 == 0 ? 0 : 1;
               Vec3 var20 = this.p(this.m, (boolean)var19);
               Vec3 var21 = RodSwapModule.r(this.m);
               if (var21 != null) {
                  var20 = var21;
               }

               d5 var22 = dj.J(var20);
               var19 = !(com.larpclient.module.k.L(var1, var12) <= var13) || !var2 && var51 == 0 ? 0 : 1;
               int var23 = var19 == 0 || var4 == 0 && !(var5 <= (var2 ? 0.5F : 1.15F)) ? 0 : 1;
               boolean var24 = this.e((boolean)var23);
               int var25 = this.G == this.s ? 1 : 0;
               if (this.j > 0L && var19 == 0 && var25 == 0) {
                  long var26 = System.currentTimeMillis() - this.j;
                  long var28 = var2 ? 0L : 120L;
                  float var30 = Mth.clamp((float)var26 / (float)var28, 0.0F, 1.0F);
                  if (var30 < 1.0F) {
                     var22 = new d5(var10.M + dj.h(var10.M, var22.M) * var30, var10.G + (var22.G - var10.G) * var30);
                  }
               }

               d5 var53 = D(var1);
               boolean var27 = com.larpclient.module.k.I(var1, var12.inflate(this.m.getPickRadius()), var53.M, var53.G, this.h.P() + 1.0, (boolean)var6);
               int var54 = var19 == 0 && var21 == null ? 0 : 1;
               d5 var29 = this.G.rotate(var10, var22, this.m, (boolean)var54, var27);
               if (var2 && var25 == 0) {
                  this.t += 0.015F;
                  double var56 = this.tO.f(this.t, 0.0, 0.0);
                  double var32 = this.tO.f(0.0, this.t, 0.0);
                  float var34 = var54 != 0 ? 0.15F : 1.0F;
                  var29.M += (float)(var56 * 0.12F * var34);
                  var29.G = Mth.clamp(var29.G + (float)(var32 * 0.08F * var34), -90.0F, 90.0F);
               }

               if (this.V.P()) {
                  this.f.I(this.I.P().intValue());
                  this.f.F();
                  if (this.f.E()) {
                     var29 = this.f.G(var29);
                  }
               } else {
                  this.f.i();
               }

               if (this.G == this.s) {
                  var29 = this.s.J(var10, var29);
               } else {
                  var29 = dj.W(var10, var29);
               }

               this.G(var29);
               if (!var24 && var3 == 0 && (!this.V.P() || !this.f.E())) {
                  boolean var57 = com.larpclient.module.k.I(var1, var12.inflate(this.m.getPickRadius()), var29.M, var29.G, var13, (boolean)var6);
                  d5 var31 = !var2 && !var57 ? var53 : var29;
                  LivingEntity var58 = this.m;
                  AABB var33 = var12;
                  double var59 = var13;
                  byte var36 = 1;
                  if (this.tv.P()) {
                     EntityHitResult var37 = com.larpclient.module.k.s(var1, var31.M, var31.G, var13, (boolean)var6);
                     var36 = 0;
                     if (var37 != null && var37.getEntity() instanceof LivingEntity var38 && this.ty.P(var38, var9)) {
                        double var62 = this.o(var38);
                        AABB var41 = var38.getBoundingBox();
                        if (com.larpclient.module.k.L(var1, var41) <= var62) {
                           var58 = var38;
                           var33 = var41;
                           var59 = var62;
                           var36 = 1;
                        }
                     }
                  }

                  LivingEntity var60 = var58;
                  Runnable var61 = this.M.P() ? () -> o(var60) : null;
                  boolean var63 = this.tm
                     .i(
                        var1,
                        new dp.c(
                           var58,
                           var33,
                           var31.M,
                           var31.G,
                           var59,
                           (boolean)var6,
                           this.A(),
                           this.u(),
                           this.t9.P(),
                           this.te.P(),
                           this.a.P() && var6 == 0,
                           (boolean)var36,
                           this.q.P(),
                           this.r.P(),
                           var61,
                           this.X(),
                           this.B.P(),
                           this.t_.P(),
                           var2,
                           this.O.P().floatValue(),
                           this.F.P().floatValue()
                        )
                     );
                  this.R();
                  if (var63) {
                     this.l("Attack executed: entity=" + var58.getId());
                     if (this.V.P()) {
                        this.f.Y();
                     }
                  }
               } else if (var3 != 0) {
                  this.tm.t(var1);
               }

               this.tY = var15 != 0 ? (var4 != 0 ? "Attacking" : "Charging") : "Tracking";
            }
         } else {
            this.k("Interaction unavailable");
         }
      }
   }

   private void k(String var1) {
      if (this.m != null || this.T != null) {
         this.l("Paused: " + var1);
      }

      this.m = this.T = null;
      tC = (boolean)0;
      this.tm.t(this.getPlayer());
      this.y();
      this.l();
      this.tY = var1;
   }

   private double o(LivingEntity var1) {
      double var2 = Math.max(0.0, this.h.P() - this.U.P());
      if (this.u.P() && mc.player != null && var1 != null) {
         AABB var4 = var1.getBoundingBox();
         double var5 = (var4.minX + var4.maxX) * 0.5 - mc.player.getX();
         double var7 = (var4.minZ + var4.maxZ) * 0.5 - mc.player.getZ();
         double var9 = var5 * var5 + var7 * var7;
         if (var9 <= 1.0E-6) {
            return var2;
         }

         double var11 = 1.0 / Math.sqrt(var9);
         var5 *= var11;
         var7 *= var11;
         Vec3 var13 = mc.player.getDeltaMovement();
         Vec3 var14 = var1.getDeltaMovement();
         double var15 = Math.abs((var14.x - var13.x) * var5 + (var14.z - var13.z) * var7);
         boolean var17 = this.R.S("Blatant");
         double var18 = var15 * this.X() * (var17 ? 0.15 : 0.4);
         double var20 = Mth.clamp(var18, 0.0, var17 ? 0.08 : 0.25);
         return Math.max(0.0, var2 - var20);
      } else {
         return var2;
      }
   }

   private double X() {
      int var1 = mc.player.tickCount;
      if (this.Y == Integer.MIN_VALUE || var1 < this.Y || var1 - this.Y >= 20) {
         double var2 = 1.0;
         if (mc.getConnection() != null) {
            PlayerInfo var4 = mc.getConnection().getPlayerInfo(mc.player.getUUID());
            if (var4 != null) {
               var2 = var4.getLatency() / 50.0F;
            }
         }

         this.w = Mth.clamp(var2, 0.5, 3.0);
         this.Y = var1;
      }

      return this.w;
   }

   private Vec3 p(LivingEntity var1, boolean var2) {
      double var3 = this.h.P() + this.K.P();
      int var5 = this.G == this.s ? 1 : 0;
      int var6 = var5;
      int var7 = this.G == this.s && this.A.P() && !var2 ? 1 : 0;
      Vec3 var8 = var7 != 0 ? dc.q(var1, this.Q()) : (this.A.P() && !var2 && var6 == 0 ? dc.q(var1, this.y()) : var1.position());
      AABB var9 = var1.getBoundingBox().move(var8.x - var1.getX(), var8.y - var1.getY(), var8.z - var1.getZ());
      Vec3 var10;
      if (var6 == 0 && !this.D.S("Center") && (!this.R.S("Normal") || this.D.S("Head") || this.D.S("Body") || this.D.S("Multipoint"))) {
         if (this.D.S("Multipoint")) {
            var10 = d0.V(var1, var9, var3);
         } else if (this.D.S("Head")) {
            var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.78F, var9.getCenter().z);
         } else if (this.D.S("Body")) {
            var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.55F, var9.getCenter().z);
         } else {
            var10 = d0.b(var9);
         }
      } else {
         var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.6F, var9.getCenter().z);
      }

      if (var2 && var5 != 0) {
         var10 = i(var1, var9, var10);
      }

      int var11 = var2 || !this.R.S("Normal") && var5 == 0 || var6 != 0 && var5 == 0 || var5 != 0 && o(var1) ? 0 : 1;
      if (var11 != 0) {
         float var12 = var5 != 0 ? 0.032F : 0.012F;
         double var13 = var1.getBbWidth() * (var5 != 0 ? 0.24F : 0.15F);
         double var15 = var1.getBbHeight() * (var5 != 0 ? 0.16F : 0.12F);
         this.d += var12;
         double var17 = this.tO.f(this.d, 120.0, 0.0) * var13;
         double var19 = this.tO.f(0.0, this.d, 240.0) * var15;
         double var21 = this.tO.f(0.0, 120.0, this.d) * var13;
         var10 = M(var10.add(var17, var19, var21), var9);
      }

      if (this.tc.P() && !this.t8.P() && !this.M.P()) {
         var10 = d0.u(var9, var10, var3);
      }

      return var10;
   }

   private double y() {
      double var1 = 1.0;
      if (this.B.P()) {
         float var3 = com.larpclient.module.e.A();
         if (Float.isFinite(var3) && var3 > 0.0F) {
            var1 = 20.0F / Mth.clamp(var3, 10.0F, 20.0F);
         }
      }

      return Mth.clamp(this.tJ.P() * var1, 0.0, 4.0);
   }

   private double Q() {
      return Mth.clamp(Math.max(1.0, this.y()) + 1.0, 1.0, 3.0);
   }

   private static Vec3 i(LivingEntity var0, AABB var1, Vec3 var2) {
      LocalPlayer var3 = Minecraft.getInstance().player;
      if (var3 != null && var0 != null && var1 != null) {
         Vec3 var4 = var0.getDeltaMovement();
         Vec3 var5 = var3.getDeltaMovement();
         double var6 = (var0.getX() - var0.xo) * 0.7F + var4.x * 0.3F - var5.x;
         double var8 = (var0.getY() - var0.yo) * 0.7F + var4.y * 0.3F - var5.y;
         double var10 = (var0.getZ() - var0.zo) * 0.7F + var4.z * 0.3F - var5.z;
         double var12 = Math.min(0.08F, var1.getXsize() * 0.25);
         double var14 = Math.min(0.08F, var1.getZsize() * 0.25);
         double var16 = Math.min(0.12F, var1.getYsize() * 0.12F);
         double var18 = var1.getXsize() * 0.22F;
         double var20 = var1.getZsize() * 0.22F;
         double var22 = var1.getYsize() * 0.1F;
         Vec3 var24 = var1.getCenter();
         return new Vec3(
            Mth.clamp(var24.x + Mth.clamp(var6 * 0.65F, -var18, var18), var1.minX + var12, var1.maxX - var12),
            Mth.clamp(var24.y + Mth.clamp(var8 * 0.45F, -var22, var22), var1.minY + var16, var1.maxY - var16),
            Mth.clamp(var24.z + Mth.clamp(var10 * 0.65F, -var20, var20), var1.minZ + var14, var1.maxZ - var14)
         );
      } else {
         return var2;
      }
   }

   private static boolean o(LivingEntity var0) {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var0 != null && var1 != null) {
         Vec3 var2 = var0.getDeltaMovement();
         Vec3 var3 = var1.getDeltaMovement();
         double var4 = (var0.getX() - var0.xo) * 0.7F + var2.x * 0.3F - var3.x;
         double var6 = (var0.getZ() - var0.zo) * 0.7F + var2.z * 0.3F - var3.z;
         return var4 * var4 + var6 * var6 >= 0.0025F;
      } else {
         return false;
      }
   }

   private static Vec3 M(Vec3 var0, AABB var1) {
      double var2 = 0.001F;
      return new Vec3(
         Mth.clamp(var0.x, var1.minX + var2, var1.maxX - var2),
         Mth.clamp(var0.y, var1.minY + var2, var1.maxY - var2),
         Mth.clamp(var0.z, var1.minZ + var2, var1.maxZ - var2)
      );
   }

   private t H() {
      return switch ((String)this.tq.P()) {
         case "FunTime" -> this.tK;
         case "Polar" -> this.tG;
         case "Matrix" -> this.b;
         case "Legit" -> this.s;
         default -> this.tl;
      };
   }

   private dp.h A() {
      return switch ((String)this.E.P()) {
         case "Always" -> dp.h.K;
         case "None" -> dp.h.R;
         default -> dp.h.d;
      };
   }

   private dp.m u() {
      return switch ((String)this.ts.P()) {
         case "Keep Sprint" -> dp.m.D;
         case "None" -> dp.m.V;
         default -> dp.m.t;
      };
   }

   private boolean X() {
      return this.C.S("1.8");
   }

   private boolean u() {
      if (mc.player != null && mc.level != null) {
         com.larpclient.module.f var1 = com.larpclient.module.f.X();
         w var2 = LarpClient.F().x().V(w.class);
         if (var1 != null && var1.isRuntimeActive() && _d.p(var1)) {
            return true;
         } else if (var2 != null && var2.isRuntimeActive() && _d.p(var2)) {
            return true;
         } else {
            AutoAnchorModule var3 = AutoAnchorModule.S();
            if (var3 != null && var3.isRuntimeActive() && _d.p(var3)) {
               return true;
            } else if (this.te.P() && com.larpclient.module.k.Q()) {
               return true;
            } else if (this.t7.P() && this.I()) {
               return true;
            } else if (this.Z.P() && this.O()) {
               return true;
            } else if (this.tD.P() && mc.gui.screen() != null) {
               return true;
            } else if (this.tx.P() && mc.player.isInLava()) {
               return true;
            } else {
               return this.l.P() && com.larpclient.module.k.S() ? true : this.tX.P() && (mc.player.isInWater() || mc.player.isUnderWater());
            }
         }
      } else {
         return true;
      }
   }

   private boolean I() {
      return mc.gameMode != null && mc.gameMode.isDestroying()
         ? true
         : mc.options != null && mc.options.keyAttack.isDown() && mc.hitResult != null && mc.hitResult.getType() == Type.BLOCK;
   }

   private boolean O() {
      return mc.options != null && mc.options.keyUse.isDown()
         ? mc.player.getMainHandItem().getItem() instanceof BlockItem || mc.player.getOffhandItem().getItem() instanceof BlockItem
         : false;
   }

   private boolean e(boolean var1) {
      if (!this.g.P() || !var1) {
         this.y();
         return false;
      }

      if (this.tz) {
         return false;
      }

      if (!mc.player.isUsingItem() || !(mc.player.getUseItem().getItem() instanceof ShieldItem)) {
         return false;
      }

      if (mc.gameMode == null) {
         return false;
      }

      if (mc.options != null) {
         mc.options.keyUse.setDown(false);
         this.W = (boolean)1;
      }

      mc.gameMode.releaseUsingItem(mc.player);
      this.tz = (boolean)1;
      return true;
   }

   private void R() {
      if (this.tz) {
         this.t2 = 1;
      }

      this.tz = (boolean)0;
      this.O();
   }

   private void y() {
      this.tz = (boolean)0;
      this.t2 = 0;
      this.O();
   }

   private void O() {
      if (this.W && mc.options != null) {
         int var1 = this.N();
         int var2;
         if (var1 >= 0 && mc.getWindow() != null) {
            var2 = hy.Y(mc.getWindow().handle(), var1) == 1 ? 1 : 0;
         } else {
            var2 = mc.options.keyUse.isDown();
         }

         mc.options.keyUse.setDown((boolean)var2);
         this.W = (boolean)0;
      } else {
         this.W = (boolean)0;
      }
   }

   private int N() {
      if (mc.options == null) {
         return -1;
      }

      for (int var1 = 0; var1 <= 7; var1++) {
         if (mc.options.keyUse.matches(com.mojang.blaze3d.platform.InputConstants.Type.MOUSE.getOrCreate(mo.A(var1)))) {
            return var1;
         }
      }

      return -1;
   }

   private static void o(LivingEntity var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.level != null && var0 != null) {
         Vec3 var2 = var1.player.getEyePosition();
         Vec3 var3 = var0.getEyePosition().subtract(var2);
         double var4 = var3.length();
         if (!(var4 < 0.01)) {
            Vec3 var6 = var3.normalize();

            for (double var7 = 0.5; var7 < var4; var7 += 0.5) {
               Vec3 var9 = var2.add(var6.scale(var7));
               BlockPos var10 = new BlockPos((int)Math.floor(var9.x), (int)Math.floor(var9.y), (int)Math.floor(var9.z));
               if (!var1.level.isEmptyBlock(var10)) {
                  var1.player.connection.send(new ServerboundPlayerActionPacket(Action.START_DESTROY_BLOCK, var10, Direction.UP));
                  var1.player.connection.send(new ServerboundPlayerActionPacket(Action.ABORT_DESTROY_BLOCK, var10, Direction.UP));
               }
            }
         }
      }
   }

   private void G(d5 var1) {
      if (mc.gui.screen() == null && !com.larpclient.te.i("Freelook") && !com.larpclient.te.i("Freecam")) {
         com.larpclient.tq.i(tq.y.u);
      }

      m9.k(this, m9.s.t, var1.M, var1.G, 1.0);
   }

   private void l() {
      com.larpclient.tq.A(tq.y.u);
   }

   private static d5 D(LocalPlayer var0) {
      q12d31c89547aa6a5 var1 = (q12d31c89547aa6a5)var0;
      return new d5(var1.larpYRotLast(), var1.larpXRotLast());
   }

   public boolean D() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.m != null && _m.B(this);
   }

   public boolean R() {
      return !this.isEnabled() ? false : this.D() && this.X.S("Camera") && this.v.S("None") && com.larpclient.tq.l() == tq.y.u;
   }

   public static Input r(LocalPlayer var0, Input var1) {
      if (P != null && P.isEnabled()) {
         AuraModule var2 = P;
         if (var2 != null && var2.isRuntimeActive() && var2.m != null && var0 != null) {
            float var3 = var1.forward() == var1.backward() ? 0.0F : (var1.forward() ? 1.0F : -1.0F);
            byte var4 = var1.sprint();
            if (var2.tm.L(var0)) {
               var4 = 0;
            } else if (var2.ts.S("Keep Sprint") && var3 > 0.0F && !var1.shift() && !var0.horizontalCollision && !var0.isInWater() && !var0.isUnderWater()) {
               var4 = 1;
            } else if (var2.ts.S("Legit")
               && (var1.sprint() || getMc().options != null && getMc().options.keySprint.isDown())
               && var3 > 0.0F
               && !var1.shift()
               && !var0.horizontalCollision) {
               var4 = 1;
            }

            Input var5 = new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump(), var1.shift(), (boolean)var4);
            if (com.larpclient.tq.l() == tq.y.u) {
               double var6 = var2.m.getX() - var0.getX();
               double var8 = var2.m.getZ() - var0.getZ();
               double var10 = Mth.wrapDegrees(Math.toDegrees(Math.atan2(var8, var6)) - 90.0);
               var5 = var2.l(var0.input, var5, var0.getYRot(), var2.X.S("Camera") ? J() : var0.getYRot(), var10);
            }

            return var5;
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public Input l(ClientInput var1, Input var2, float var3, float var4, double var5) {
      if (!this.isEnabled()) {
         return var2;
      }

      if (!this.X.S("Target") && !this.v.S("None")) {
         float var7 = var2.forward() == var2.backward() ? 0.0F : (var2.forward() ? 1.0F : -1.0F);
         float var8 = var2.left() == var2.right() ? 0.0F : (var2.left() ? 1.0F : -1.0F);
         if (var7 == 0.0F && var8 == 0.0F) {
            return var2;
         }

         double var9 = this.v.S("Targeted") && var7 > 0.0F && var8 == 0.0F ? var5 : Mth.wrapDegrees(Math.toDegrees(V(var4, var7, var8)));
         float[] var11 = l(var9, var3);
         var1.moveVector = new Vec2(var11[1], var11[0]).normalized();
         return new Input(var11[0] > 0.0F, var11[0] < 0.0F, var11[1] > 0.0F, var11[1] < 0.0F, var2.jump(), var2.shift(), var2.sprint());
      } else {
         return var2;
      }
   }

   public static double V(float var0, float var1, float var2) {
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

   public static float[] l(double var0, float var2) {
      double var3 = Mth.wrapDegrees(var0 - var2);
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

   public static boolean i() {
      if (P != null && P.isEnabled()) {
         AuraModule var0 = P;
         return var0 != null && var0.isRuntimeActive() && var0.g.P() && x() != null && (var0.tz || var0.t2 > 0);
      } else {
         return false;
      }
   }

   public float y(float var1, float var2) {
      if (this.isEnabled()) {
         if (!this.D()) {
            return var1;
         } else if (this.s()) {
            float var4 = Mth.wrapDegrees(J() - var1);
            return Math.abs(var4) > 0.5F ? var1 + var4 * 0.45F : J();
         } else {
            boolean var3 = this.R.S("Normal");
            return f(var1, var2, var3 ? 35.0F : 50.0F, var3 ? 0.25F : 0.3F);
         }
      } else {
         return var1;
      }
   }

   public static float f(float var0, float var1, float var2, float var3) {
      float var4 = Mth.wrapDegrees(var1 - var0);
      if (Math.abs(var4) <= var2) {
         return var0;
      }

      float var5 = var4 < 0.0F ? var1 + var2 : var1 - var2;
      return var0 + Mth.wrapDegrees(var5 - var0) * var3;
   }

   private boolean s() {
      LocalPlayer var1 = x();
      if (this.X.S("Camera") && this.v.S("Targeted") && var1 != null && var1.input != null && this.m != null) {
         Vec2 var2 = var1.input.getMoveVector();
         if (!(var2.y <= 0.0F) && !(Math.abs(var2.x) > 0.05F)) {
            float var3 = Mth.wrapDegrees((float)Math.toDegrees(Math.atan2(this.m.getZ() - var1.getZ(), this.m.getX() - var1.getX())) - 90.0F);
            return Math.abs(Mth.wrapDegrees(var3 - J())) <= 15.0F;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static LocalPlayer x() {
      return getMc().player;
   }

   public static boolean z() {
      if (P != null && P.isEnabled()) {
         AuraModule var0 = P;
         return var0 != null && var0.isRuntimeActive() && tC;
      } else {
         return false;
      }
   }

   public static float J() {
      Minecraft var0 = Minecraft.getInstance();
      return S() ? com.larpclient.tq.i(com.larpclient.tq.J(), com.larpclient.tq.F(), var0.options.getCameraType().isMirrored())[0] : var0.player.getYRot();
   }

   private static boolean S() {
      tq.y var0 = com.larpclient.tq.l();
      return var0 == tq.y.u || var0 == tq.y.e || var0 == tq.y.P;
   }

   public static float R() {
      Minecraft var0 = Minecraft.getInstance();
      return S() ? com.larpclient.tq.i(com.larpclient.tq.J(), com.larpclient.tq.F(), var0.options.getCameraType().isMirrored())[1] : var0.player.getXRot();
   }

   public static Vec3 R() {
      return dj.v(J(), R());
   }

   public double v() {
      return this.h.P();
   }

   public LivingEntity l() {
      if (this.isEnabled()) {
         return this.isRuntimeActive() ? this.m : null;
      } else {
         return null;
      }
   }

   public double K() {
      return this.t3.P();
   }

   public double l() {
      return _m.o(this.q());
   }

   private double q() {
      return Math.max(0.0, this.h.P() - this.U.P());
   }

   public boolean G() {
      return !this.isEnabled() ? false : this.isOperational() && this.isInGame() && mc.gui.screen() == null && this.y.P() && this.o();
   }

   public float q() {
      return this.e.P().floatValue();
   }

   public boolean F() {
      return this.z.P();
   }

   public boolean e() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.m != null && this.i.P() && _m.z() && this.o();
   }

   public float S() {
      return this.tR.P().floatValue();
   }

   public double i() {
      return this.p.P();
   }

   public boolean g() {
      return this.L.P();
   }

   public boolean A() {
      return this.H.P();
   }

   public boolean t() {
      return this.o.P();
   }

   public String B() {
      if (this.m != null && mc.player != null) {
         String var1 = _m.V(this.m, this.q(), mc.player.getLookAngle());
         return "range".equals(var1) ? "Outside reach" : (var1 == null ? "Valid attack ray" : "Inside reach: " + var1);
      } else {
         return "No lock";
      }
   }

   @Override
   public String getStatus() {
      return this.tY;
   }

   private void l(String var1) {
      mt.U("Aura", var1);
   }

   public static void k(LocalPlayer var0, String var1) {
      AuraModule var2 = P;
      if (var2 != null && var2.isRuntimeActive() && var2.m != null && var0 != null) {
         if (var1.equals("jump-before")) {
            if (var2.Q <= 0) {
               return;
            }

            var2.Q--;
            var2.ta = var0.tickCount + 16;
         }

         if (var0.tickCount <= var2.ta) {
            Input var3 = var0.input.keyPresses;
            Vec2 var4 = var0.input.getMoveVector();
            Vec3 var5 = var0.getDeltaMovement();
            d5 var6 = D(var0);
            Options var7 = getMc().options;
            Input var8 = new Input(
               var7.keyUp.isDown(),
               var7.keyDown.isDown(),
               var7.keyLeft.isDown(),
               var7.keyRight.isDown(),
               var7.keyJump.isDown(),
               var7.keyShift.isDown(),
               var7.keySprint.isDown()
            );
            mt.K(
               "AuraMovement",
               String.format(
                  Locale.ROOT,
                  "%s tick=%d mode=%s/%s/%s reference=%s target=%d ground=%s sprint=%s sprintReportMatches=%s reset=%s yaw=%.4f sentYaw=%.4f keys=%s physical=%s input=(%.5f,%.5f) pos=(%.6f,%.6f,%.6f) velocity=(%.6f,%.6f,%.6f) fall=%.5f orbit=%s",
                  var1,
                  var0.tickCount,
                  var2.tq.P(),
                  var2.v.P(),
                  var2.ts.P(),
                  var2.X.P(),
                  var2.m.getId(),
                  var0.onGround(),
                  var0.isSprinting(),
                  pl.I(var0.isSprinting()),
                  tC,
                  var0.getYRot(),
                  var6.M,
                  var3,
                  var8,
                  var4.x,
                  var4.y,
                  var0.getX(),
                  var0.getY(),
                  var0.getZ(),
                  var5.x,
                  var5.y,
                  var5.z,
                  var0.fallDistance,
                  TargetStrafeModule.Z()
               )
            );
         }
      }
   }

   public static Input F(Input var0) {
      return w7.E(var0);
   }
}
