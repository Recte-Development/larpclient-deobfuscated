package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q71cffb54eb30bf94;
import com.larpclient.LarpClient;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.b6;
import com.larpclient.ij;
import com.larpclient.j2;
import com.larpclient.n2;
import com.larpclient.nz;
import com.larpclient.p4;
import com.larpclient.q1;
import com.larpclient.qg;
import com.larpclient.vi;
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
   private static AuraModule z;
   public static volatile boolean K;
   private final o E = (o)this.addSetting(
         new o("Activation", "Toggle acts until you switch it off; Hold only acts while the module shortcut is held", "Toggle", "Toggle", "Hold")
      )
      .B("Keybind Mode");
   private final o L = this.addSetting(
      new o("Rotation", "Rotation engine that turns the player toward the aim point", "Sloth", "Sloth", "FunTime", "Polar", "Matrix", "Legit") {
         @Override
         public void setValue(String var1) {
            super.setValue(AuraModule.Y(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(AuraModule.Y(var1));
         }
      }
   );
   private final o ie = this.addSetting(
      new o("Aura Mode", "Blatant aims and swings on every chance; Normal waits until the attack is about to land", "Normal", "Blatant", "Normal") {
         @Override
         public void setValue(String var1) {
            super.setValue(AuraModule.u(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(AuraModule.u(var1));
         }
      }
   );
   private final o I = this.addSetting(new o("Combat Version", "1.9+ waits for the attack charge; 1.8 clicks at the CPS range", "1.9", "1.9", "1.8") {
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
   private final b i = this.addSetting(new b("Attack Range", "Maximum distance to the target box for an attack", 3.0, 2.0, 6.0, 0.1));
   private final b iJ = this.addSetting(new b("Aim Range", "Extra distance beyond Attack Range where targets are tracked", 1.5, 0.0, 9.0, 0.1));
   private final b y = this.addSetting(new b("FOV", "Field of view around your camera for new targets", 360.0, 30.0, 360.0, 5.0));
   private final m iP = this.addSetting(new m("Players", "Target players", true));
   private final m A = this.addSetting(new m("Mobs", "Target hostile and neutral mobs", true));
   private final m g = this.addSetting(new m("Animals", "Target animals", false));
   private final m a = this.addSetting(new m("Invisibles", "Target invisible players", true));
   private final m C = this.addSetting(new m("Naked Players", "Target players without armor", true));
   private final o R = this.addSetting(
      new o("Criticals", "Adaptive lands criticals when available; Always waits for a falling hit; None ignores them", "Adaptive", "Adaptive", "Always", "None")
   );
   private final o ic = this.addSetting(new o("Aim Point", "Where on the target box to aim", "Nearest", "Nearest", "Center", "Multipoint", "Head", "Body"));
   private final m v = this.addSetting(new m("Predict", "Lead moving targets", false));
   private final b iO = this.addSetting(new b("Predict Value", "Ticks of target motion to lead", 1.0, 0.0, 4.0, 0.1));
   private final b iA = this.addSetting(new b("Reach Buffer", "Distance subtracted from Attack Range as a safety margin", 0.2, 0.0, 1.0, 0.05));
   private final m j = this.addSetting(new m("Rotation ETA", "Retained from the reference, which never reads it; it has no effect", true));
   private final m iL = this.addSetting(new m("Entity Raycast", "Attack whichever valid entity the final aim ray actually hits", true));
   private final m V = this.addSetting(new m("Dynamic Safe Reach", "Shorten reach while the gap to the target is changing quickly, scaled by ping", true));
   private final m T = this.addSetting(new m("Predict Criticals", "Start the sprint reset a tick or two before the jump arc falls", true));
   private final m G = this.addSetting(new m("Randomize Timing", "Attack at a randomized charge between 97 and 100 percent", true));
   private final o i7 = this.addSetting(
      new o(
         "Sprint Mode",
         "Legit drops sprint a tick before criticals; Keep Sprint keeps it held; None leaves sprint alone",
         "Legit",
         "Legit",
         "Keep Sprint",
         "None"
      )
   );
   private final m o = this.addSetting(new m("Through Walls", "Select and attack targets behind blocks", false));
   private final m N = this.addSetting(
      new m("Wall Bypass", "Through-wall attacks that first send break start/abort actions for blocks along the line of sight", false)
   );
   private final m i4 = this.addSetting(new m("Shield Breaker", "Swap to your best hotbar axe for hits on a raised shield, then back next tick", true));
   private final m Q = this.addSetting(new m("Shield Unpress", "Lower your own shield right before attacking", false));
   private final m iZ = this.addSetting(new m("Raycast", "Require a block-free line to the target for each attack", true));
   private final m M = this.addSetting(new m("TPS Sync", "Scale timing and prediction to the measured server TPS", true));
   private final m s = this.addSetting(new m("Eating", "Do not attack while eating or drinking", true));
   private final m X = this.addSetting(new m("Breaking", "Do not attack while mining", false));
   private final m n = this.addSetting(new m("Placing", "Do not attack while placing blocks", false));
   private final m l = this.addSetting(new m("In Inventory", "Do not attack with a screen open", false));
   private final m W = this.addSetting(new m("In Lava", "Do not attack while in lava", false));
   private final m ir = this.addSetting(new m("In Web", "Do not attack while in cobweb", false));
   private final m i0 = this.addSetting(new m("In Water", "Do not attack while in water", false));
   private final o ia = this.addSetting(
      new o("Movement Reference", "Target follows combat facing; Camera uses Move Fix to follow your view", "Target", "Target", "Camera")
   );
   private final o it = this.addSetting(
      new o(
         "Move Fix",
         "Silent and Free steer your keys toward the camera direction; Targeted walks at the target on W; None moves exactly with the camera",
         "Silent",
         "Silent",
         "Targeted",
         "Free",
         "None"
      )
   );
   private final b t = this.addSetting(new b("Min CPS", "Lowest 1.8 clicks per second", 8.0, 4.0, 16.0, 0.5));
   private final b iT = this.addSetting(new b("Max CPS", "Highest 1.8 clicks per second", 11.0, 4.0, 16.0, 0.5));
   private final m x = this.addSetting(new m("Auto Jump", "Jump to set up criticals", false));
   private final m c = this.addSetting(new m("Smart Aim Visibility", "Move the aim point to the nearest visible part of the target", true) {
      @Override
      public String getDisplayName() {
         return "Smart Aim (Visibility)";
      }
   });
   private final m iG = this.addSetting(new m("Focus Target", "Keep the current target while it stays valid", true));
   private final o id = this.addSetting(new o("Sort Mode", "Order for choosing a new target", "Distance", "Distance", "Health", "FOV", "Armor"));
   private final m il = this.addSetting(new m("Deliberate Miss", "Occasionally swing just past the target", false));
   private final b w = this.addSetting(new b("Miss Threshold", "Average hits between deliberate misses", 28.0, 10.0, 80.0, 1.0));
   private final m i_ = this.addSetting(new m("FOV Cone", "Show the attack range arc on the ground", true));
   private final b d = this.addSetting(new b("Indicator Width", "Thickness of FOV range lines", 3.5, 1.0, 6.0, 0.5));
   private final m m = this.addSetting(new m("Visible Lines", "Keep FOV lines visible over terrain and entities", false));
   private final m p = this.addSetting(new m("Hit Zone", "Terrain positioning guide around the current target", true));
   private final b b = this.addSetting(new b("Zone Width", "Ground boundary stroke width", 2.0, 1.0, 6.0, 0.5));
   private final b iz = this.addSetting(new b("Zone Opacity", "Ground guide opacity", 0.65, 0.05, 1.0, 0.05));
   private final m U = this.addSetting(new m("Zone Fill", "Faint fill on supported collision surfaces", false));
   private final m h = this.addSetting(new m("Zone Bloom", "Soft glow through the shared visual effects", true));
   private final m ix = this.addSetting(new m("Zone Motion", "Traveling highlights along the terrain boundary", true));
   private final kk iF = new kk();
   private final f Y = new f(this);
   private final i iY = new i();
   private final d D = new d();
   private final f_ H = new f_();
   private final p B = new p();
   private final g F = new g();
   private final com.larpclient.module.a f = new com.larpclient.module.a();
   private final q k = new q();
   private k iU;
   private LivingEntity J;
   private LivingEntity P;
   private float iq;
   private float iN;
   private long i2;
   private int S = Integer.MIN_VALUE;
   private double e = 1.0;
   private boolean iB;
   private int u;
   private boolean O;
   private String Z = "Idle";
   private int q;
   private int r = Integer.MIN_VALUE;

   public AuraModule() {
      super("Aura", "Automatically attacks nearby entities", Category.COMBAT, -1);
      z = this;
      this.iO.a(this.v::getValue);
      this.it.a(() -> this.ia.w("Camera"));
      this.T.a(() -> !this.R.w("None"));
      this.G.a(() -> !this.c());
      this.t.a(this::c);
      this.iT.a(this::c);
      this.w.a(this.il::getValue);
      this.d.a(this.i_::getValue);
      this.m.a(this.i_::getValue);
      this.b.a(this.p::getValue);
      this.iz.a(this.p::getValue);
      this.U.a(this.p::getValue);
      this.h.a(this.p::getValue);
      this.ix.a(this.p::getValue);
      this.L.w(var1 -> this.T("Rotation engine: " + var1));
   }

   public static AuraModule t() {
      return z;
   }

   @Override
   public String getDisplayName() {
      return "Kill Aura";
   }

   public boolean L() {
      return this.E.w("Hold");
   }

   @Override
   public o keybindMode() {
      return this.E;
   }

   @Override
   public void toggle() {
      if (this.L() && mc != null && mc.gui != null && mc.gui.screen() == null) {
         this.setEnabled(true);
      } else {
         super.toggle();
      }
   }

   private boolean V() {
      if (!this.L()) {
         return true;
      } else {
         int var1 = this.getKeyBind();
         if (!p4.g(var1) || mc.getWindow() == null) {
            return false;
         } else {
            return mc.gui.screen() == null && mc.isWindowActive() ? p4.Y(mc.getWindow().handle(), var1) : false;
         }
      }
   }

   private static String u(String var0) {
      if ("Balanced".equalsIgnoreCase(var0) || "Balant".equalsIgnoreCase(var0)) {
         return "Blatant";
      } else {
         return "Legit".equalsIgnoreCase(var0) ? "Normal" : var0;
      }
   }

   private static String Y(String var0) {
      if ("TEST".equalsIgnoreCase(var0)) {
         return "Legit";
      } else {
         return !"HolyWorld".equals(var0) && !"Neuro".equals(var0) && !"Smooth".equals(var0) ? var0 : "Sloth";
      }
   }

   @Override
   public boolean allowsScreenInteractions() {
      return !this.l.getValue();
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.F, com.larpclient.module.l.k);
   }

   public List<r<?>> T() {
      return List.of(this.L, this.ie, this.ia, this.it, this.ic, this.v, this.iO, this.c, this.j);
   }

   public List<r<?>> H() {
      return List.of(this.I, this.i, this.iJ, this.iA, this.V, this.R, this.T, this.i7, this.x, this.G, this.t, this.iT, this.iL, this.il, this.w);
   }

   public List<r<?>> b() {
      return List.of(this.iP, this.A, this.g, this.a, this.C);
   }

   public List<r<?>> Y() {
      return List.of(this.y, this.id, this.iG);
   }

   public List<r<?>> W() {
      return List.of(this.o, this.N, this.i4, this.Q, this.iZ, this.M);
   }

   public List<r<?>> d() {
      return List.of(this.s, this.X, this.n, this.l, this.W, this.ir, this.i0);
   }

   public List<r<?>> Q() {
      return List.of(this.i_, this.d, this.m, this.p, this.b, this.iz, this.U, this.h, this.ix);
   }

   @Override
   public void onEnable() {
      this.q = 3;
      this.r = Integer.MIN_VALUE;
      this.Y.x();
      this.iF.C();
      this.iY.c();
      com.larpclient.module.n.l();
      this.J = this.P = null;
      K = (boolean)0;
      this.iq = this.iN = 0.0F;
      this.i2 = 0L;
      this.S = Integer.MIN_VALUE;
      this.e = 1.0;
      this.W();
      this.Z = "Idle";
      _q.z(
         "Aura",
         "Movement: Reference="
            + this.ia.getValue()
            + "; Move Fix="
            + this.it.getValue()
            + "; vanilla sprint eligibility; attacks wait for matching sprint reports"
      );
   }

   @Override
   public void onDisable() {
      this.J = this.P = null;
      K = (boolean)0;
      this.iq = this.iN = 0.0F;
      this.i2 = 0L;
      this.S = Integer.MIN_VALUE;
      this.e = 1.0;
      this.W();
      this.L();
      if (this.iU != null) {
         this.iU.reset();
      }

      this.iU = null;
      this.Y.w(this.getPlayer());
      this.Y.b(mc);
      this.Y.x();
      this.iF.C();
      this.iY.c();
      com.larpclient.module.n.l();
      this.Z = "Idle";
   }

   @Override
   public void onTick() {
      this.Y.X(mc);
      if (this.isInGame()) {
         LocalPlayer var1 = mc.player;
         if (!this.V()) {
            this.y("Hold released");
         } else {
            if (_p.D(this)) {
               int var10000 = var1.isSpectator();
               boolean var43 = false;
               byte var42 = (byte)var10000;
               var10000 = Integer.signum(var42);
               if ((var10000 | -var10000) >>> 31 == 0 && var1.isAlive()) {
                  boolean var45 = false;
                  int var44 = this.u;
                  if ((-Integer.signum(var44) >>> 31 ^ 1) == 0) {
                     byte var47 = 1;
                     int var46 = this.u;
                     this.u = var46 - var47;
                  }

                  boolean var2 = this.ie.w("Blatant");
                  boolean var3 = this.X();
                  int var4 = !var3 && this.Y.z(var1, this.c(), this.M.getValue(), this.G.getValue(), var2) ? 1 : 0;
                  float var5 = this.Y.v(var1, this.c(), this.M.getValue(), this.G.getValue(), var2);
                  int var6 = !this.o.getValue() && !this.N.getValue() ? 0 : 1;
                  double var7 = Math.max(this.i.getValue() + this.iJ.getValue(), RodSwapModule.u());
                  kk.c var9 = new kk.c(
                     var7,
                     this.iP.getValue(),
                     this.A.getValue(),
                     this.g.getValue(),
                     this.a.getValue(),
                     this.C.getValue(),
                     this.iG.getValue(),
                     (boolean)var6,
                     this.id.getValue(),
                     this.y.getValue()
                  );
                  this.J = this.iF.u(var9);
                  c var10 = new c(var1.getYRot(), var1.getXRot());
                  if (this.J != this.P) {
                     if (this.iU != null) {
                        this.iU.targetChanged(this.J);
                     }

                     com.larpclient.module.n.l();
                     if (this.J != null) {
                        this.i2 = System.currentTimeMillis();
                        this.Y.j();
                        this.T("Target acquired: entity=" + this.J.getId());
                     } else {
                        this.L();
                        this.T("Target lost");
                     }

                     this.P = this.J;
                  }

                  if (this.J == null) {
                     this.W();
                     K = (boolean)0;
                     this.Y.w(var1);
                     this.i2 = 0L;
                     this.L();
                     this.Z = "No target";
                     return;
                  }

                  k var11 = this.h();
                  if (var11 != this.iU) {
                     if (this.iU != null) {
                        this.iU.reset();
                     }

                     this.iU = var11;
                  }

                  AABB var12 = this.J.getBoundingBox();
                  double var13 = this.r(this.J);
                  int var15 = com.larpclient.module.v.E(var1, var12) <= var13 ? 1 : 0;
                  float var16 = 1.25F;
                  if (var15 != 0) {
                     c var17 = com.larpclient.module.w.U(com.larpclient.module.n.z(var12));
                     var16 = Math.max(var16, this.iU.ticksToReach(var10, var17) + 0.35F);
                     var16 = Math.max(var16, var2 ? 1.0F : 4.5F);
                  }

                  int var51 = var3 || var15 == 0 || var4 == 0 && !(var5 <= var16) ? 0 : 1;
                  int var18 = var15 != 0 && this.i2 > 0L && System.currentTimeMillis() - this.i2 < (var2 ? 150L : 500L) ? 1 : 0;
                  int var19 = var15 == 0 || !var2 && var51 == 0 && var18 == 0 ? 0 : 1;
                  Vec3 var20 = this.U(this.J, (boolean)var19);
                  Vec3 var21 = RodSwapModule.s(this.J);
                  if (var21 != null) {
                     var20 = var21;
                  }

                  c var22 = com.larpclient.module.w.U(var20);
                  var19 = !(com.larpclient.module.v.E(var1, var12) <= var13) || !var2 && var51 == 0 ? 0 : 1;
                  int var23 = var19 == 0 || var4 == 0 && !(var5 <= (var2 ? 0.5F : 1.15F)) ? 0 : 1;
                  boolean var24 = this.W((boolean)var23);
                  int var25 = this.iU == this.k ? 1 : 0;
                  if (this.i2 > 0L && var19 == 0 && var25 == 0) {
                     long var26 = System.currentTimeMillis() - this.i2;
                     long var28 = var2 ? 0L : 120L;
                     float var30 = Mth.clamp((float)var26 / (float)var28, 0.0F, 1.0F);
                     if (var30 < 1.0F) {
                        var22 = new c(var10.V + com.larpclient.module.w.J(var10.V, var22.V) * var30, var10.T + (var22.T - var10.T) * var30);
                     }
                  }

                  c var53 = q(var1);
                  boolean var27 = com.larpclient.module.v.B(
                     var1, var12.inflate(this.J.getPickRadius()), var53.V, var53.T, this.i.getValue() + 1.0, (boolean)var6
                  );
                  int var54 = var19 == 0 && var21 == null ? 0 : 1;
                  c var29 = this.iU.rotate(var10, var22, this.J, (boolean)var54, var27);
                  if (var2 && var25 == 0) {
                     this.iq += 0.015F;
                     double var56 = this.D.X(this.iq, 0.0, 0.0);
                     double var32 = this.D.X(0.0, this.iq, 0.0);
                     float var34 = var54 != 0 ? 0.15F : 1.0F;
                     var29.V += (float)(var56 * 0.12F * var34);
                     var29.T = Mth.clamp(var29.T + (float)(var32 * 0.08F * var34), -90.0F, 90.0F);
                  }

                  if (this.il.getValue()) {
                     this.iY.G(this.w.getValue().intValue());
                     this.iY.x();
                     if (this.iY.R()) {
                        var29 = this.iY.W(var29);
                     }
                  } else {
                     this.iY.c();
                  }

                  if (this.iU == this.k) {
                     var29 = this.k.x(var10, var29);
                  } else {
                     var29 = com.larpclient.module.w.H(var10, var29);
                  }

                  this.U(var29);
                  if (!var24 && !var3 && (!this.il.getValue() || !this.iY.R())) {
                     boolean var57 = com.larpclient.module.v.B(var1, var12.inflate(this.J.getPickRadius()), var29.V, var29.T, var13, (boolean)var6);
                     c var31 = !var2 && !var57 ? var53 : var29;
                     LivingEntity var58 = this.J;
                     AABB var33 = var12;
                     double var59 = var13;
                     byte var36 = 1;
                     if (this.iL.getValue()) {
                        EntityHitResult var37 = com.larpclient.module.v.F(var1, var31.V, var31.T, var13, (boolean)var6);
                        var36 = 0;
                        if (var37 != null && var37.getEntity() instanceof LivingEntity var38 && this.iF.n(var38, var9)) {
                           double var62 = this.r(var38);
                           AABB var41 = var38.getBoundingBox();
                           if (com.larpclient.module.v.E(var1, var41) <= var62) {
                              var58 = var38;
                              var33 = var41;
                              var59 = var62;
                              var36 = 1;
                           }
                        }
                     }

                     LivingEntity var60 = var58;
                     Runnable var61 = this.N.getValue() ? () -> l(var60) : null;
                     boolean var63 = this.Y
                        .M(
                           var1,
                           new f.c(
                              var58,
                              var33,
                              var31.V,
                              var31.T,
                              var59,
                              (boolean)var6,
                              this.X(),
                              this.T(),
                              this.i4.getValue(),
                              this.s.getValue(),
                              this.iZ.getValue() && var6 == 0,
                              (boolean)var36,
                              this.x.getValue(),
                              this.T.getValue(),
                              var61,
                              this.c(),
                              this.M.getValue(),
                              this.G.getValue(),
                              var2,
                              this.t.getValue().floatValue(),
                              this.iT.getValue().floatValue()
                           )
                        );
                     this.c();
                     if (var63) {
                        this.T("Attack executed: entity=" + var58.getId());
                        int var65 = this.il.getValue();
                        boolean var49 = false;
                        byte var48 = (byte)var65;
                        var65 = Integer.signum(var48);
                        if (((var65 | -var65) >>> 31 ^ 1) == 0) {
                           this.iY.e();
                        }
                     }
                  } else if (var3) {
                     this.Y.w(var1);
                  }

                  this.Z = var15 != 0 ? (var4 != 0 ? "Attacking" : "Charging") : "Tracking";
                  return;
               }
            }

            this.y("Interaction unavailable");
         }
      }
   }

   private void y(String var1) {
      if (this.J != null || this.P != null) {
         this.T("Paused: " + var1);
      }

      this.J = this.P = null;
      K = (boolean)0;
      this.Y.w(this.getPlayer());
      this.W();
      this.L();
      this.Z = var1;
   }

   private double r(LivingEntity var1) {
      double var2 = Math.max(0.0, this.i.getValue() - this.iA.getValue());
      if (this.V.getValue() && mc.player != null && var1 != null) {
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
         boolean var17 = this.ie.w("Blatant");
         double var18 = var15 * this.i() * (var17 ? 0.15 : 0.4);
         double var20 = Mth.clamp(var18, 0.0, var17 ? 0.08 : 0.25);
         return Math.max(0.0, var2 - var20);
      } else {
         return var2;
      }
   }

   private double i() {
      int var1 = mc.player.tickCount;
      if (this.S == Integer.MIN_VALUE || var1 < this.S || var1 - this.S >= 20) {
         double var2 = 1.0;
         if (mc.getConnection() != null) {
            PlayerInfo var4 = mc.getConnection().getPlayerInfo(mc.player.getUUID());
            if (var4 != null) {
               var2 = var4.getLatency() / 50.0F;
            }
         }

         this.e = Mth.clamp(var2, 0.5, 3.0);
         this.S = var1;
      }

      return this.e;
   }

   private Vec3 U(LivingEntity var1, boolean var2) {
      double var3 = this.i.getValue() + this.iJ.getValue();
      int var5 = this.iU == this.k ? 1 : 0;
      int var6 = var5;
      int var7 = this.iU == this.k && this.v.getValue() && !var2 ? 1 : 0;
      Vec3 var8 = var7 != 0 ? f5.u(var1, this.L()) : (this.v.getValue() && !var2 && var6 == 0 ? f5.u(var1, this.C()) : var1.position());
      AABB var9 = var1.getBoundingBox().move(var8.x - var1.getX(), var8.y - var1.getY(), var8.z - var1.getZ());
      Vec3 var10;
      if (var6 == 0 && !this.ic.w("Center") && (!this.ie.w("Normal") || this.ic.w("Head") || this.ic.w("Body") || this.ic.w("Multipoint"))) {
         if (this.ic.w("Multipoint")) {
            var10 = com.larpclient.module.n.l(var1, var9, var3);
         } else if (this.ic.w("Head")) {
            var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.78F, var9.getCenter().z);
         } else if (this.ic.w("Body")) {
            var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.55F, var9.getCenter().z);
         } else {
            var10 = com.larpclient.module.n.z(var9);
         }
      } else {
         var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.6F, var9.getCenter().z);
      }

      if (var2 && var5 != 0) {
         var10 = T(var1, var9, var10);
      }

      int var11 = !var2 && (this.ie.w("Normal") || var5 != 0) && (var6 == 0 || var5 != 0) && (var5 == 0 || !p(var1)) ? 1 : 0;
      if (var11 != 0) {
         float var12 = var5 != 0 ? 0.032F : 0.012F;
         double var13 = var1.getBbWidth() * (var5 != 0 ? 0.24F : 0.15F);
         double var15 = var1.getBbHeight() * (var5 != 0 ? 0.16F : 0.12F);
         this.iN += var12;
         double var17 = this.D.X(this.iN, 120.0, 0.0) * var13;
         double var19 = this.D.X(0.0, this.iN, 240.0) * var15;
         double var21 = this.D.X(0.0, 120.0, this.iN) * var13;
         var10 = R(var10.add(var17, var19, var21), var9);
      }

      if (this.c.getValue() && !this.o.getValue() && !this.N.getValue()) {
         var10 = com.larpclient.module.n.s(var9, var10, var3);
      }

      return var10;
   }

   private double C() {
      double var1 = 1.0;
      if (this.M.getValue()) {
         float var3 = com.larpclient.module.y.w();
         if (Float.isFinite(var3) && var3 > 0.0F) {
            var1 = 20.0F / Mth.clamp(var3, 10.0F, 20.0F);
         }
      }

      return Mth.clamp(this.iO.getValue() * var1, 0.0, 4.0);
   }

   private double L() {
      return Mth.clamp(Math.max(1.0, this.C()) + 1.0, 1.0, 3.0);
   }

   private static Vec3 T(LivingEntity var0, AABB var1, Vec3 var2) {
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

   private static boolean p(LivingEntity var0) {
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

   private static Vec3 R(Vec3 var0, AABB var1) {
      double var2 = 0.001F;
      return new Vec3(
         Mth.clamp(var0.x, var1.minX + var2, var1.maxX - var2),
         Mth.clamp(var0.y, var1.minY + var2, var1.maxY - var2),
         Mth.clamp(var0.z, var1.minZ + var2, var1.maxZ - var2)
      );
   }

   private k h() {
      return switch ((String)this.L.getValue()) {
         case "FunTime" -> this.B;
         case "Polar" -> this.F;
         case "Matrix" -> this.f;
         case "Legit" -> this.k;
         default -> this.H;
      };
   }

   private f.v X() {
      return switch ((String)this.R.getValue()) {
         case "Always" -> f.v.L;
         case "None" -> f.v.z;
         default -> f.v.S;
      };
   }

   private f.t T() {
      return switch ((String)this.i7.getValue()) {
         case "Keep Sprint" -> f.t.E;
         case "None" -> f.t.k;
         default -> f.t.d;
      };
   }

   private boolean c() {
      return this.I.w("1.8");
   }

   private boolean X() {
      if (mc.player != null && mc.level != null) {
         kv var1 = kv.m();
         kb var2 = LarpClient.N().j().y(kb.class);
         if (var1 != null && var1.isRuntimeActive() && vi.m(var1)) {
            return true;
         } else if (var2 != null && var2.isRuntimeActive() && vi.m(var2)) {
            return true;
         } else {
            AutoAnchorModule var3 = AutoAnchorModule.q();
            if (var3 != null && var3.isRuntimeActive() && vi.m(var3)) {
               return true;
            } else if (this.s.getValue() && com.larpclient.module.v.d()) {
               return true;
            } else if (this.X.getValue() && this.z()) {
               return true;
            } else if (this.n.getValue() && this.v()) {
               return true;
            } else if (this.l.getValue() && mc.gui.screen() != null) {
               return true;
            } else if (this.W.getValue() && mc.player.isInLava()) {
               return true;
            } else {
               return this.ir.getValue() && com.larpclient.module.v.r() ? true : this.i0.getValue() && (mc.player.isInWater() || mc.player.isUnderWater());
            }
         }
      } else {
         return true;
      }
   }

   private boolean z() {
      return mc.gameMode != null && mc.gameMode.isDestroying()
         ? true
         : mc.options != null && mc.options.keyAttack.isDown() && mc.hitResult != null && mc.hitResult.getType() == Type.BLOCK;
   }

   private boolean v() {
      return mc.options != null && mc.options.keyUse.isDown()
         ? mc.player.getMainHandItem().getItem() instanceof BlockItem || mc.player.getOffhandItem().getItem() instanceof BlockItem
         : false;
   }

   private boolean W(boolean var1) {
      if (!this.Q.getValue() || !var1) {
         this.W();
         return false;
      }

      if (this.iB) {
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
         this.O = (boolean)1;
      }

      mc.gameMode.releaseUsingItem(mc.player);
      this.iB = (boolean)1;
      return true;
   }

   private void c() {
      if (this.iB) {
         this.u = 1;
      }

      this.iB = (boolean)0;
      this.h();
   }

   private void W() {
      this.iB = (boolean)0;
      this.u = 0;
      this.h();
   }

   private void h() {
      if (this.O && mc.options != null) {
         int var1 = this.a();
         int var2;
         if (var1 >= 0 && mc.getWindow() != null) {
            var2 = n2.L(mc.getWindow().handle(), var1) == 1 ? 1 : 0;
         } else {
            var2 = mc.options.keyUse.isDown();
         }

         mc.options.keyUse.setDown((boolean)var2);
         this.O = (boolean)0;
      } else {
         this.O = (boolean)0;
      }
   }

   private int a() {
      if (mc.options == null) {
         return -1;
      }

      for (int var1 = 0; var1 <= 7; var1++) {
         if (mc.options.keyUse.matches(com.mojang.blaze3d.platform.InputConstants.Type.MOUSE.getOrCreate(j2.K(var1)))) {
            return var1;
         }
      }

      return -1;
   }

   private static void l(LivingEntity var0) {
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

   private void U(c var1) {
      if (mc.gui.screen() == null && !nz.X("Freelook") && !nz.X("Freecam")) {
         ij.V(ij.r.N);
      }

      b6.P(this, b6.w.P, var1.V, var1.T, 1.0);
   }

   private void L() {
      ij.W(ij.r.N);
   }

   private static c q(LocalPlayer var0) {
      q71cffb54eb30bf94 var1 = (q71cffb54eb30bf94)var0;
      return new c(var1.larpYRotLast(), var1.larpXRotLast());
   }

   public boolean I() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.J != null && _p.x(this);
   }

   public boolean h() {
      return !this.isEnabled() ? false : this.I() && this.ia.w("Camera") && this.it.w("None") && ij.L() == ij.r.N;
   }

   public static Input T(LocalPlayer var0, Input var1) {
      if (z != null && z.isEnabled()) {
         AuraModule var2 = z;
         if (var2 != null && var2.isRuntimeActive() && var2.J != null && var0 != null) {
            float var3 = var1.forward() == var1.backward() ? 0.0F : (var1.forward() ? 1.0F : -1.0F);
            byte var4 = var1.sprint();
            if (var2.Y.H(var0)) {
               var4 = 0;
            } else if (var2.i7.w("Keep Sprint") && var3 > 0.0F && !var1.shift() && !var0.horizontalCollision && !var0.isInWater() && !var0.isUnderWater()) {
               var4 = 1;
            } else if (var2.i7.w("Legit")
               && (var1.sprint() || getMc().options != null && getMc().options.keySprint.isDown())
               && var3 > 0.0F
               && !var1.shift()
               && !var0.horizontalCollision) {
               var4 = 1;
            }

            Input var5 = new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump(), var1.shift(), (boolean)var4);
            if (ij.L() == ij.r.N) {
               double var6 = var2.J.getX() - var0.getX();
               double var8 = var2.J.getZ() - var0.getZ();
               double var10 = Mth.wrapDegrees(Math.toDegrees(Math.atan2(var8, var6)) - 90.0);
               var5 = var2.R(var0.input, var5, var0.getYRot(), var2.ia.w("Camera") ? t() : var0.getYRot(), var10);
            }

            return var5;
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public Input R(ClientInput var1, Input var2, float var3, float var4, double var5) {
      if (!this.isEnabled()) {
         return var2;
      }

      if (!this.ia.w("Target") && !this.it.w("None")) {
         float var7 = var2.forward() == var2.backward() ? 0.0F : (var2.forward() ? 1.0F : -1.0F);
         float var8 = var2.left() == var2.right() ? 0.0F : (var2.left() ? 1.0F : -1.0F);
         if (var7 == 0.0F && var8 == 0.0F) {
            return var2;
         }

         double var9 = this.it.w("Targeted") && var7 > 0.0F && var8 == 0.0F ? var5 : Mth.wrapDegrees(Math.toDegrees(b(var4, var7, var8)));
         float[] var11 = a(var9, var3);
         var1.moveVector = new Vec2(var11[1], var11[0]).normalized();
         return new Input(var11[0] > 0.0F, var11[0] < 0.0F, var11[1] > 0.0F, var11[1] < 0.0F, var2.jump(), var2.shift(), var2.sprint());
      } else {
         return var2;
      }
   }

   public static double b(float var0, float var1, float var2) {
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

   public static float[] a(double var0, float var2) {
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

   public static boolean K() {
      if (z != null && z.isEnabled()) {
         AuraModule var0 = z;
         return var0 != null && var0.isRuntimeActive() && var0.Q.getValue() && c() != null && (var0.iB || var0.u > 0);
      } else {
         return false;
      }
   }

   public float B(float var1, float var2) {
      if (this.isEnabled()) {
         if (!this.I()) {
            return var1;
         } else if (this.t()) {
            float var4 = Mth.wrapDegrees(t() - var1);
            return Math.abs(var4) > 0.5F ? var1 + var4 * 0.45F : t();
         } else {
            boolean var3 = this.ie.w("Normal");
            return H(var1, var2, var3 ? 35.0F : 50.0F, var3 ? 0.25F : 0.3F);
         }
      } else {
         return var1;
      }
   }

   public static float H(float var0, float var1, float var2, float var3) {
      float var4 = Mth.wrapDegrees(var1 - var0);
      if (Math.abs(var4) <= var2) {
         return var0;
      }

      float var5 = var4 < 0.0F ? var1 + var2 : var1 - var2;
      return var0 + Mth.wrapDegrees(var5 - var0) * var3;
   }

   private boolean t() {
      LocalPlayer var1 = c();
      if (this.ia.w("Camera") && this.it.w("Targeted") && var1 != null && var1.input != null && this.J != null) {
         Vec2 var2 = var1.input.getMoveVector();
         if (!(var2.y <= 0.0F) && !(Math.abs(var2.x) > 0.05F)) {
            float var3 = Mth.wrapDegrees((float)Math.toDegrees(Math.atan2(this.J.getZ() - var1.getZ(), this.J.getX() - var1.getX())) - 90.0F);
            return Math.abs(Mth.wrapDegrees(var3 - t())) <= 15.0F;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static LocalPlayer c() {
      return getMc().player;
   }

   public static boolean M() {
      if (z != null && z.isEnabled()) {
         AuraModule var0 = z;
         return var0 != null && var0.isRuntimeActive() && K;
      } else {
         return false;
      }
   }

   public static float t() {
      Minecraft var0 = Minecraft.getInstance();
      return p() ? ij.p(ij.D(), ij.J(), var0.options.getCameraType().isMirrored())[0] : var0.player.getYRot();
   }

   private static boolean p() {
      ij.r var0 = ij.L();
      return var0 == ij.r.N || var0 == ij.r.w || var0 == ij.r.o;
   }

   public static float N() {
      Minecraft var0 = Minecraft.getInstance();
      return p() ? ij.p(ij.D(), ij.J(), var0.options.getCameraType().isMirrored())[1] : var0.player.getXRot();
   }

   public static Vec3 u() {
      return com.larpclient.module.w.Y(t(), N());
   }

   public double j() {
      return this.i.getValue();
   }

   public LivingEntity R() {
      if (this.isEnabled()) {
         return this.isRuntimeActive() ? this.J : null;
      } else {
         return null;
      }
   }

   public double t() {
      return this.y.getValue();
   }

   public double v() {
      return _p.M(this.d());
   }

   private double d() {
      return Math.max(0.0, this.i.getValue() - this.iA.getValue());
   }

   public boolean g() {
      return !this.isEnabled() ? false : this.isOperational() && this.isInGame() && mc.gui.screen() == null && this.i_.getValue() && this.V();
   }

   public float P() {
      return this.d.getValue().floatValue();
   }

   public boolean N() {
      return this.m.getValue();
   }

   public boolean A() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.J != null && this.p.getValue() && _p.g() && this.V();
   }

   public float l() {
      return this.b.getValue().floatValue();
   }

   public double l() {
      return this.iz.getValue();
   }

   public boolean a() {
      return this.U.getValue();
   }

   public boolean U() {
      return this.h.getValue();
   }

   public boolean x() {
      return this.ix.getValue();
   }

   public String I() {
      if (this.J != null && mc.player != null) {
         String var1 = _p.e(this.J, this.d(), mc.player.getLookAngle());
         return "range".equals(var1) ? "Outside reach" : (var1 == null ? "Valid attack ray" : "Inside reach: " + var1);
      } else {
         return "No lock";
      }
   }

   @Override
   public String getStatus() {
      return this.Z;
   }

   private void T(String var1) {
      _q.c("Aura", var1);
   }

   public static void H(LocalPlayer var0, String var1) {
      AuraModule var2 = z;
      if (var2 != null && var2.isRuntimeActive() && var2.J != null && var0 != null) {
         if (var1.equals("jump-before")) {
            if (var2.q <= 0) {
               return;
            }

            var2.q--;
            var2.r = var0.tickCount + 16;
         }

         if (var0.tickCount <= var2.r) {
            Input var3 = var0.input.keyPresses;
            Vec2 var4 = var0.input.getMoveVector();
            Vec3 var5 = var0.getDeltaMovement();
            c var6 = q(var0);
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
            _q.z(
               "AuraMovement",
               String.format(
                  Locale.ROOT,
                  "%s tick=%d mode=%s/%s/%s reference=%s target=%d ground=%s sprint=%s sprintReportMatches=%s reset=%s yaw=%.4f sentYaw=%.4f keys=%s physical=%s input=(%.5f,%.5f) pos=(%.6f,%.6f,%.6f) velocity=(%.6f,%.6f,%.6f) fall=%.5f orbit=%s",
                  var1,
                  var0.tickCount,
                  var2.L.getValue(),
                  var2.it.getValue(),
                  var2.i7.getValue(),
                  var2.ia.getValue(),
                  var2.J.getId(),
                  var0.onGround(),
                  var0.isSprinting(),
                  q1.h(var0.isSprinting()),
                  K,
                  var0.getYRot(),
                  var6.V,
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
                  TargetStrafeModule.t()
               )
            );
         }
      }
   }

   public static Input W(Input var0) {
      return qg.Y(var0);
   }
}
