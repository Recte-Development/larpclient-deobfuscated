package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q1881b7c262e73011;
import com.larpclient.LarpClient;
import com.larpclient.bc;
import com.larpclient.c5;
import com.larpclient.j6;
import com.larpclient.k2;
import com.larpclient.kq;
import com.larpclient.p_;
import com.larpclient.si;
import com.larpclient.t1;
import com.larpclient.ta;
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
import org.lwjgl.glfw.GLFW;

public final class AuraModule extends Module {
   private static AuraModule at;
   public static volatile boolean al;
   private final l p = (l)this.addSetting(
         new l("Activation", "Toggle acts until you switch it off; Hold only acts while the module shortcut is held", "Toggle", "Toggle", "Hold")
      )
      .h("Keybind Mode");
   private final l G = this.addSetting(
      new l("Rotation", "Rotation engine that turns the player toward the aim point", "Sloth", "Sloth", "FunTime", "Polar", "Matrix", "Legit") {
         @Override
         public void setValue(String var1) {
            super.setValue(AuraModule.O(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(AuraModule.O(var1));
         }
      }
   );
   private final l aF = this.addSetting(
      new l("Aura Mode", "Blatant aims and swings on every chance; Normal waits until the attack is about to land", "Normal", "Blatant", "Normal") {
         @Override
         public void setValue(String var1) {
            super.setValue(AuraModule.C(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(AuraModule.C(var1));
         }
      }
   );
   private final l q = this.addSetting(new l("Combat Version", "1.9+ waits for the attack charge; 1.8 clicks at the CPS range", "1.9", "1.9", "1.8") {
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
   private final e F = this.addSetting(new e("Attack Range", "Maximum distance to the target box for an attack", 3.0, 2.0, 6.0, 0.1));
   private final e aE = this.addSetting(new e("Aim Range", "Extra distance beyond Attack Range where targets are tracked", 1.5, 0.0, 9.0, 0.1));
   private final e a8 = this.addSetting(new e("FOV", "Field of view around your camera for new targets", 360.0, 30.0, 360.0, 5.0));
   private final s L = this.addSetting(new s("Players", "Target players", true));
   private final s e = this.addSetting(new s("Mobs", "Target hostile and neutral mobs", true));
   private final s I = this.addSetting(new s("Animals", "Target animals", false));
   private final s b = this.addSetting(new s("Invisibles", "Target invisible players", true));
   private final s w = this.addSetting(new s("Naked Players", "Target players without armor", true));
   private final l i = this.addSetting(
      new l("Criticals", "Adaptive lands criticals when available; Always waits for a falling hit; None ignores them", "Adaptive", "Adaptive", "Always", "None")
   );
   private final l c = this.addSetting(new l("Aim Point", "Where on the target box to aim", "Nearest", "Nearest", "Center", "Multipoint", "Head", "Body"));
   private final s S = this.addSetting(new s("Predict", "Lead moving targets", false));
   private final e aJ = this.addSetting(new e("Predict Value", "Ticks of target motion to lead", 1.0, 0.0, 4.0, 0.1));
   private final e C = this.addSetting(new e("Reach Buffer", "Distance subtracted from Attack Range as a safety margin", 0.2, 0.0, 1.0, 0.05));
   private final s D = this.addSetting(new s("Rotation ETA", "Retained from the reference, which never reads it; it has no effect", true));
   private final s n = this.addSetting(new s("Entity Raycast", "Attack whichever valid entity the final aim ray actually hits", true));
   private final s aj = this.addSetting(new s("Dynamic Safe Reach", "Shorten reach while the gap to the target is changing quickly, scaled by ping", true));
   private final s r = this.addSetting(new s("Predict Criticals", "Start the sprint reset a tick or two before the jump arc falls", true));
   private final s aU = this.addSetting(new s("Randomize Timing", "Attack at a randomized charge between 97 and 100 percent", true));
   private final l T = this.addSetting(
      new l(
         "Sprint Mode",
         "Legit drops sprint a tick before criticals; Keep Sprint keeps it held; None leaves sprint alone",
         "Legit",
         "Legit",
         "Keep Sprint",
         "None"
      )
   );
   private final s A = this.addSetting(new s("Through Walls", "Select and attack targets behind blocks", false));
   private final s j = this.addSetting(
      new s("Wall Bypass", "Through-wall attacks that first send break start/abort actions for blocks along the line of sight", false)
   );
   private final s U = this.addSetting(new s("Shield Breaker", "Swap to your best hotbar axe for hits on a raised shield, then back next tick", true));
   private final s aA = this.addSetting(new s("Shield Unpress", "Lower your own shield right before attacking", false));
   private final s W = this.addSetting(new s("Raycast", "Require a block-free line to the target for each attack", true));
   private final s aM = this.addSetting(new s("TPS Sync", "Scale timing and prediction to the measured server TPS", true));
   private final s h = this.addSetting(new s("Eating", "Do not attack while eating or drinking", true));
   private final s M = this.addSetting(new s("Breaking", "Do not attack while mining", false));
   private final s az = this.addSetting(new s("Placing", "Do not attack while placing blocks", false));
   private final s a_ = this.addSetting(new s("In Inventory", "Do not attack with a screen open", false));
   private final s aH = this.addSetting(new s("In Lava", "Do not attack while in lava", false));
   private final s t = this.addSetting(new s("In Web", "Do not attack while in cobweb", false));
   private final s aK = this.addSetting(new s("In Water", "Do not attack while in water", false));
   private final l u = this.addSetting(
      new l("Movement Reference", "Target follows combat facing; Camera uses Move Fix to follow your view", "Target", "Target", "Camera")
   );
   private final l V = this.addSetting(
      new l(
         "Move Fix",
         "Silent and Free steer your keys toward the camera direction; Targeted walks at the target on W; None moves exactly with the camera",
         "Silent",
         "Silent",
         "Targeted",
         "Free",
         "None"
      )
   );
   private final e aC = this.addSetting(new e("Min CPS", "Lowest 1.8 clicks per second", 8.0, 4.0, 16.0, 0.5));
   private final e f = this.addSetting(new e("Max CPS", "Highest 1.8 clicks per second", 11.0, 4.0, 16.0, 0.5));
   private final s E = this.addSetting(new s("Auto Jump", "Jump to set up criticals", false));
   private final s O = this.addSetting(new s("Smart Aim Visibility", "Move the aim point to the nearest visible part of the target", true) {
      @Override
      public String getDisplayName() {
         return "Smart Aim (Visibility)";
      }
   });
   private final s an = this.addSetting(new s("Focus Target", "Keep the current target while it stays valid", true));
   private final l y = this.addSetting(new l("Sort Mode", "Order for choosing a new target", "Distance", "Distance", "Health", "FOV", "Armor"));
   private final s a9 = this.addSetting(new s("Deliberate Miss", "Occasionally swing just past the target", false));
   private final e a = this.addSetting(new e("Miss Threshold", "Average hits between deliberate misses", 28.0, 10.0, 80.0, 1.0));
   private final s v = this.addSetting(new s("FOV Cone", "Show the attack range arc on the ground", true));
   private final e aO = this.addSetting(new e("Indicator Width", "Thickness of FOV range lines", 3.5, 1.0, 6.0, 0.5));
   private final s l = this.addSetting(new s("Visible Lines", "Keep FOV lines visible over terrain and entities", false));
   private final s J = this.addSetting(new s("Hit Zone", "Terrain positioning guide around the current target", true));
   private final e aW = this.addSetting(new e("Zone Width", "Ground boundary stroke width", 2.0, 1.0, 6.0, 0.5));
   private final e R = this.addSetting(new e("Zone Opacity", "Ground guide opacity", 0.65, 0.05, 1.0, 0.05));
   private final s aa = this.addSetting(new s("Zone Fill", "Faint fill on supported collision surfaces", false));
   private final s H = this.addSetting(new s("Zone Bloom", "Soft glow through the shared visual effects", true));
   private final s a6 = this.addSetting(new s("Zone Motion", "Traveling highlights along the terrain boundary", true));
   private final td d = new td();
   private final ts K = new ts(this);
   private final d B = new d();
   private final h Q = new h();
   private final t N = new t();
   private final a x = new a();
   private final tf Z = new tf();
   private final j k = new j();
   private final b ab = new b();
   private x ao;
   private LivingEntity m;
   private LivingEntity Y;
   private float g;
   private float a5;
   private long aT;
   private int X = Integer.MIN_VALUE;
   private double z = 1.0;
   private boolean aG;
   private int aB;
   private boolean P;
   private String aD = "Idle";
   private int s;
   private int o = Integer.MIN_VALUE;

   public AuraModule() {
      super("Aura", "Automatically attacks nearby entities", Category.COMBAT, -1);
      at = this;
      this.aJ.P(this.S::getValue);
      this.V.P(() -> this.u.O("Camera"));
      this.r.P(() -> !this.i.O("None"));
      this.aU.P(() -> !this.t());
      this.aC.P(this::t);
      this.f.P(this::t);
      this.a.P(this.a9::getValue);
      this.aO.P(this.v::getValue);
      this.l.P(this.v::getValue);
      this.aW.P(this.J::getValue);
      this.R.P(this.J::getValue);
      this.aa.P(this.J::getValue);
      this.H.P(this.J::getValue);
      this.a6.P(this.J::getValue);
      this.G.o(var1 -> this.o("Rotation engine: " + var1));
   }

   public static AuraModule V() {
      return at;
   }

   @Override
   public String getDisplayName() {
      return "Kill Aura";
   }

   public boolean T() {
      return this.p.O("Hold");
   }

   @Override
   public l keybindMode() {
      return this.p;
   }

   @Override
   public void toggle() {
      if (this.T() && mc != null && mc.gui != null && mc.gui.screen() == null) {
         this.setEnabled(true);
      } else {
         super.toggle();
      }
   }

   private boolean b() {
      if (!this.T()) {
         return true;
      } else {
         int var1 = this.getKeyBind();
         if (!bc.a(var1) || mc.getWindow() == null) {
            return false;
         } else {
            return mc.gui.screen() == null && mc.isWindowActive() ? bc.I(mc.getWindow().handle(), var1) : false;
         }
      }
   }

   private static String C(String var0) {
      if ("Balanced".equalsIgnoreCase(var0) || "Balant".equalsIgnoreCase(var0)) {
         return "Blatant";
      } else {
         return "Legit".equalsIgnoreCase(var0) ? "Normal" : var0;
      }
   }

   private static String O(String var0) {
      if ("TEST".equalsIgnoreCase(var0)) {
         return "Legit";
      } else {
         return !"HolyWorld".equals(var0) && !"Neuro".equals(var0) && !"Smooth".equals(var0) ? var0 : "Sloth";
      }
   }

   @Override
   public boolean allowsScreenInteractions() {
      return !this.a_.getValue();
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.K, com.larpclient.module.m.U);
   }

   public List<q<?>> V() {
      return List.of(this.G, this.aF, this.u, this.V, this.c, this.S, this.aJ, this.O, this.D);
   }

   public List<q<?>> t() {
      return List.of(this.q, this.F, this.aE, this.C, this.aj, this.i, this.r, this.T, this.E, this.aU, this.aC, this.f, this.n, this.a9, this.a);
   }

   public List<q<?>> q() {
      return List.of(this.L, this.e, this.I, this.b, this.w);
   }

   public List<q<?>> u() {
      return List.of(this.a8, this.y, this.an);
   }

   public List<q<?>> s() {
      return List.of(this.A, this.j, this.U, this.aA, this.W, this.aM);
   }

   public List<q<?>> Q() {
      return List.of(this.h, this.M, this.az, this.a_, this.aH, this.t, this.aK);
   }

   public List<q<?>> B() {
      return List.of(this.v, this.aO, this.l, this.J, this.aW, this.R, this.aa, this.H, this.a6);
   }

   @Override
   public void onEnable() {
      this.s = 3;
      this.o = Integer.MIN_VALUE;
      this.K.h();
      this.d.m();
      this.B.g();
      tm.u();
      this.m = this.Y = null;
      al = (boolean)0;
      this.g = this.a5 = 0.0F;
      this.aT = 0L;
      this.X = Integer.MIN_VALUE;
      this.z = 1.0;
      this.c();
      this.aD = "Idle";
      com.larpclient.h.A(
         "Aura",
         "Movement: Reference="
            + this.u.getValue()
            + "; Move Fix="
            + this.V.getValue()
            + "; vanilla sprint eligibility; attacks wait for matching sprint reports"
      );
   }

   @Override
   public void onDisable() {
      this.m = this.Y = null;
      al = (boolean)0;
      this.g = this.a5 = 0.0F;
      this.aT = 0L;
      this.X = Integer.MIN_VALUE;
      this.z = 1.0;
      this.c();
      this.K();
      if (this.ao != null) {
         this.ao.reset();
      }

      this.ao = null;
      this.K.l(this.getPlayer());
      this.K.L(mc);
      this.K.h();
      this.d.m();
      this.B.g();
      tm.u();
      this.aD = "Idle";
   }

   @Override
   public void onTick() {
      this.K.M(mc);
      if (this.isInGame()) {
         LocalPlayer var1 = mc.player;
         if (!this.b()) {
            this.I("Hold released");
         } else if (ta.S(this) && !var1.isSpectator() && var1.isAlive()) {
            boolean var43 = false;
            int var42 = this.aB;
            if ((-Integer.signum(var42) >>> 31 ^ 1) == 0) {
               byte var45 = 1;
               int var44 = this.aB;
               this.aB = var44 - var45;
            }

            boolean var2 = this.aF.O("Blatant");
            boolean var3 = this.c();
            int var4 = !var3 && this.K.G(var1, this.t(), this.aM.getValue(), this.aU.getValue(), var2) ? 1 : 0;
            float var5 = this.K.b(var1, this.t(), this.aM.getValue(), this.aU.getValue(), var2);
            int var6 = !this.A.getValue() && !this.j.getValue() ? 0 : 1;
            double var7 = Math.max(this.F.getValue() + this.aE.getValue(), RodSwapModule.Y());
            td.e var9 = new td.e(
               var7,
               this.L.getValue(),
               this.e.getValue(),
               this.I.getValue(),
               this.b.getValue(),
               this.w.getValue(),
               this.an.getValue(),
               (boolean)var6,
               this.y.getValue(),
               this.a8.getValue()
            );
            this.m = this.d.N(var9);
            u var10 = new u(var1.getYRot(), var1.getXRot());
            if (this.m != this.Y) {
               if (this.ao != null) {
                  this.ao.targetChanged(this.m);
               }

               tm.u();
               if (this.m != null) {
                  this.aT = System.currentTimeMillis();
                  this.K.M();
                  this.o("Target acquired: entity=" + this.m.getId());
               } else {
                  this.K();
                  this.o("Target lost");
               }

               this.Y = this.m;
            }

            if (this.m == null) {
               this.c();
               al = (boolean)0;
               this.K.l(var1);
               this.aT = 0L;
               this.K();
               this.aD = "No target";
            } else {
               x var11 = this.N();
               if (var11 != this.ao) {
                  if (this.ao != null) {
                     this.ao.reset();
                  }

                  this.ao = var11;
               }

               AABB var12 = this.m.getBoundingBox();
               double var13 = this.q(this.m);
               int var15 = com.larpclient.module.y.D(var1, var12) <= var13 ? 1 : 0;
               float var16 = 1.25F;
               if (var15 != 0) {
                  u var17 = tv.C(tm.t(var12));
                  var16 = Math.max(var16, this.ao.ticksToReach(var10, var17) + 0.35F);
                  var16 = Math.max(var16, var2 ? 1.0F : 4.5F);
               }

               int var51 = var3 || var15 == 0 || var4 == 0 && !(var5 <= var16) ? 0 : 1;
               int var18 = var15 != 0 && this.aT > 0L && System.currentTimeMillis() - this.aT < (var2 ? 150L : 500L) ? 1 : 0;
               int var19 = var15 == 0 || !var2 && var51 == 0 && var18 == 0 ? 0 : 1;
               Vec3 var20 = this.z(this.m, (boolean)var19);
               Vec3 var21 = RodSwapModule.Y(this.m);
               if (var21 != null) {
                  var20 = var21;
               }

               u var22 = tv.C(var20);
               var19 = !(com.larpclient.module.y.D(var1, var12) <= var13) || !var2 && var51 == 0 ? 0 : 1;
               int var23 = var19 == 0 || var4 == 0 && !(var5 <= (var2 ? 0.5F : 1.15F)) ? 0 : 1;
               boolean var24 = this.M((boolean)var23);
               int var25 = this.ao == this.ab ? 1 : 0;
               if (this.aT > 0L && var19 == 0 && var25 == 0) {
                  long var26 = System.currentTimeMillis() - this.aT;
                  long var28 = var2 ? 0L : 120L;
                  float var30 = Mth.clamp((float)var26 / (float)var28, 0.0F, 1.0F);
                  if (var30 < 1.0F) {
                     var22 = new u(var10.R + tv.p(var10.R, var22.R) * var30, var10.x + (var22.x - var10.x) * var30);
                  }
               }

               u var53 = Q(var1);
               boolean var27 = com.larpclient.module.y.t(var1, var12.inflate(this.m.getPickRadius()), var53.R, var53.x, this.F.getValue() + 1.0, (boolean)var6);
               boolean var47 = false;
               int var46 = var19;
               int var10000 = Integer.signum(var46);
               int var54 = (var10000 | -var10000) >>> 31 == 0 && var21 == null ? 0 : 1;
               u var29 = this.ao.rotate(var10, var22, this.m, (boolean)var54, var27);
               if (var2 && var25 == 0) {
                  this.g += 0.015F;
                  double var56 = this.Q.i(this.g, 0.0, 0.0);
                  double var32 = this.Q.i(0.0, this.g, 0.0);
                  float var34 = var54 != 0 ? 0.15F : 1.0F;
                  var29.R += (float)(var56 * 0.12F * var34);
                  var29.x = Mth.clamp(var29.x + (float)(var32 * 0.08F * var34), -90.0F, 90.0F);
               }

               if (this.a9.getValue()) {
                  this.B.p(this.a.getValue().intValue());
                  this.B.h();
                  if (this.B.N()) {
                     var29 = this.B.u(var29);
                  }
               } else {
                  this.B.g();
               }

               if (this.ao == this.ab) {
                  var29 = this.ab.u(var10, var29);
               } else {
                  var29 = tv.o(var10, var29);
               }

               this.G(var29);
               if (!var24 && !var3 && (!this.a9.getValue() || !this.B.N())) {
                  boolean var57 = com.larpclient.module.y.t(var1, var12.inflate(this.m.getPickRadius()), var29.R, var29.x, var13, (boolean)var6);
                  u var31 = !var2 && !var57 ? var53 : var29;
                  LivingEntity var58 = this.m;
                  AABB var33 = var12;
                  double var59 = var13;
                  byte var36 = 1;
                  if (this.n.getValue()) {
                     EntityHitResult var37 = com.larpclient.module.y.H(var1, var31.R, var31.x, var13, (boolean)var6);
                     var36 = 0;
                     if (var37 != null && var37.getEntity() instanceof LivingEntity var38) {
                        int var64 = this.d.W(var38, var9);
                        boolean var49 = false;
                        byte var48 = (byte)var64;
                        var64 = Integer.signum(var48);
                        if (((var64 | -var64) >>> 31 ^ 1) == 0) {
                           double var62 = this.q(var38);
                           AABB var41 = var38.getBoundingBox();
                           if (com.larpclient.module.y.D(var1, var41) <= var62) {
                              var58 = var38;
                              var33 = var41;
                              var59 = var62;
                              var36 = 1;
                           }
                        }
                     }
                  }

                  LivingEntity var60 = var58;
                  Runnable var61 = this.j.getValue() ? () -> x(var60) : null;
                  boolean var63 = this.K
                     .K(
                        var1,
                        new ts.w(
                           var58,
                           var33,
                           var31.R,
                           var31.x,
                           var59,
                           (boolean)var6,
                           this.P(),
                           this.P(),
                           this.U.getValue(),
                           this.h.getValue(),
                           this.W.getValue() && var6 == 0,
                           (boolean)var36,
                           this.E.getValue(),
                           this.r.getValue(),
                           var61,
                           this.t(),
                           this.aM.getValue(),
                           this.aU.getValue(),
                           var2,
                           this.aC.getValue().floatValue(),
                           this.f.getValue().floatValue()
                        )
                     );
                  this.e();
                  if (var63) {
                     this.o("Attack executed: entity=" + var58.getId());
                     if (this.a9.getValue()) {
                        this.B.L();
                     }
                  }
               } else if (var3) {
                  this.K.l(var1);
               }

               this.aD = var15 != 0 ? (var4 != 0 ? "Attacking" : "Charging") : "Tracking";
            }
         } else {
            this.I("Interaction unavailable");
         }
      }
   }

   private void I(String var1) {
      if (this.m != null || this.Y != null) {
         this.o("Paused: " + var1);
      }

      this.m = this.Y = null;
      al = (boolean)0;
      this.K.l(this.getPlayer());
      this.c();
      this.K();
      this.aD = var1;
   }

   private double q(LivingEntity var1) {
      double var2 = Math.max(0.0, this.F.getValue() - this.C.getValue());
      if (this.aj.getValue() && mc.player != null && var1 != null) {
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
         boolean var17 = this.aF.O("Blatant");
         double var18 = var15 * this.O() * (var17 ? 0.15 : 0.4);
         double var20 = Mth.clamp(var18, 0.0, var17 ? 0.08 : 0.25);
         return Math.max(0.0, var2 - var20);
      } else {
         return var2;
      }
   }

   private double O() {
      int var1 = mc.player.tickCount;
      if (this.X == Integer.MIN_VALUE || var1 < this.X || var1 - this.X >= 20) {
         double var2 = 1.0;
         if (mc.getConnection() != null) {
            PlayerInfo var4 = mc.getConnection().getPlayerInfo(mc.player.getUUID());
            if (var4 != null) {
               var2 = var4.getLatency() / 50.0F;
            }
         }

         this.z = Mth.clamp(var2, 0.5, 3.0);
         this.X = var1;
      }

      return this.z;
   }

   private Vec3 z(LivingEntity var1, boolean var2) {
      double var3 = this.F.getValue() + this.aE.getValue();
      int var5 = this.ao == this.ab ? 1 : 0;
      int var6 = var5;
      int var7 = this.ao == this.ab && this.S.getValue() && !var2 ? 1 : 0;
      Vec3 var8 = var7 != 0
         ? com.larpclient.module.o.l(var1, this.t())
         : (this.S.getValue() && !var2 && var6 == 0 ? com.larpclient.module.o.l(var1, this.H()) : var1.position());
      AABB var9 = var1.getBoundingBox().move(var8.x - var1.getX(), var8.y - var1.getY(), var8.z - var1.getZ());
      Vec3 var10;
      if (var6 == 0 && !this.c.O("Center") && (!this.aF.O("Normal") || this.c.O("Head") || this.c.O("Body") || this.c.O("Multipoint"))) {
         if (this.c.O("Multipoint")) {
            var10 = tm.O(var1, var9, var3);
         } else if (this.c.O("Head")) {
            var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.78F, var9.getCenter().z);
         } else if (this.c.O("Body")) {
            var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.55F, var9.getCenter().z);
         } else {
            var10 = tm.t(var9);
         }
      } else {
         var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.6F, var9.getCenter().z);
      }

      if (var2 && var5 != 0) {
         var10 = L(var1, var9, var10);
      }

      int var11 = var2 || !this.aF.O("Normal") && var5 == 0 || var6 != 0 && var5 == 0 || var5 != 0 && c(var1) ? 0 : 1;
      if (var11 != 0) {
         float var12 = var5 != 0 ? 0.032F : 0.012F;
         double var13 = var1.getBbWidth() * (var5 != 0 ? 0.24F : 0.15F);
         double var15 = var1.getBbHeight() * (var5 != 0 ? 0.16F : 0.12F);
         this.a5 += var12;
         double var17 = this.Q.i(this.a5, 120.0, 0.0) * var13;
         double var19 = this.Q.i(0.0, this.a5, 240.0) * var15;
         double var21 = this.Q.i(0.0, 120.0, this.a5) * var13;
         var10 = V(var10.add(var17, var19, var21), var9);
      }

      if (this.O.getValue() && !this.A.getValue() && !this.j.getValue()) {
         var10 = tm.k(var9, var10, var3);
      }

      return var10;
   }

   private double H() {
      double var1 = 1.0;
      if (this.aM.getValue()) {
         float var3 = t3.e();
         if (Float.isFinite(var3) && var3 > 0.0F) {
            var1 = 20.0F / Mth.clamp(var3, 10.0F, 20.0F);
         }
      }

      return Mth.clamp(this.aJ.getValue() * var1, 0.0, 4.0);
   }

   private double t() {
      return Mth.clamp(Math.max(1.0, this.H()) + 1.0, 1.0, 3.0);
   }

   private static Vec3 L(LivingEntity var0, AABB var1, Vec3 var2) {
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

   private static boolean c(LivingEntity var0) {
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

   private static Vec3 V(Vec3 var0, AABB var1) {
      double var2 = 0.001F;
      return new Vec3(
         Mth.clamp(var0.x, var1.minX + var2, var1.maxX - var2),
         Mth.clamp(var0.y, var1.minY + var2, var1.maxY - var2),
         Mth.clamp(var0.z, var1.minZ + var2, var1.maxZ - var2)
      );
   }

   private x N() {
      return switch ((String)this.G.getValue()) {
         case "FunTime" -> this.x;
         case "Polar" -> this.Z;
         case "Matrix" -> this.k;
         case "Legit" -> this.ab;
         default -> this.N;
      };
   }

   private ts.y P() {
      return switch ((String)this.i.getValue()) {
         case "Always" -> ts.y.g;
         case "None" -> ts.y.P;
         default -> ts.y.r;
      };
   }

   private ts.u P() {
      return switch ((String)this.T.getValue()) {
         case "Keep Sprint" -> ts.u.r;
         case "None" -> ts.u.W;
         default -> ts.u.i;
      };
   }

   private boolean t() {
      return this.q.O("1.8");
   }

   private boolean c() {
      if (mc.player != null && mc.level != null) {
         tb var1 = tb.R();
         th var2 = LarpClient.R().z().p(th.class);
         if (var1 != null && var1.isRuntimeActive() && t1.s(var1)) {
            return true;
         } else if (var2 != null && var2.isRuntimeActive() && t1.s(var2)) {
            return true;
         } else {
            AutoAnchorModule var3 = AutoAnchorModule.y();
            if (var3 != null && var3.isRuntimeActive() && t1.s(var3)) {
               return true;
            } else if (this.h.getValue() && com.larpclient.module.y.w()) {
               return true;
            } else if (this.M.getValue() && this.O()) {
               return true;
            } else if (this.az.getValue() && this.s()) {
               return true;
            } else if (this.a_.getValue() && mc.gui.screen() != null) {
               return true;
            } else if (this.aH.getValue() && mc.player.isInLava()) {
               return true;
            } else {
               return this.t.getValue() && com.larpclient.module.y.B() ? true : this.aK.getValue() && (mc.player.isInWater() || mc.player.isUnderWater());
            }
         }
      } else {
         return true;
      }
   }

   private boolean O() {
      return mc.gameMode != null && mc.gameMode.isDestroying()
         ? true
         : mc.options != null && mc.options.keyAttack.isDown() && mc.hitResult != null && mc.hitResult.getType() == Type.BLOCK;
   }

   private boolean s() {
      return mc.options != null && mc.options.keyUse.isDown()
         ? mc.player.getMainHandItem().getItem() instanceof BlockItem || mc.player.getOffhandItem().getItem() instanceof BlockItem
         : false;
   }

   private boolean M(boolean var1) {
      if (!this.aA.getValue() || !var1) {
         this.c();
         return false;
      }

      if (this.aG) {
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
         this.P = (boolean)1;
      }

      mc.gameMode.releaseUsingItem(mc.player);
      this.aG = (boolean)1;
      return true;
   }

   private void e() {
      if (this.aG) {
         this.aB = 1;
      }

      this.aG = (boolean)0;
      this.s();
   }

   private void c() {
      this.aG = (boolean)0;
      this.aB = 0;
      this.s();
   }

   private void s() {
      if (this.P && mc.options != null) {
         int var1 = this.t();
         int var2;
         if (var1 >= 0 && mc.getWindow() != null) {
            var2 = GLFW.glfwGetMouseButton(mc.getWindow().handle(), var1) == 1 ? 1 : 0;
         } else {
            var2 = mc.options.keyUse.isDown();
         }

         mc.options.keyUse.setDown((boolean)var2);
         this.P = (boolean)0;
      } else {
         this.P = (boolean)0;
      }
   }

   private int t() {
      if (mc.options == null) {
         return -1;
      }

      for (int var1 = 0; var1 <= 7; var1++) {
         if (mc.options.keyUse.matches(com.mojang.blaze3d.platform.InputConstants.Type.MOUSE.getOrCreate(k2.f(var1)))) {
            return var1;
         }
      }

      return -1;
   }

   private static void x(LivingEntity var0) {
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

   private void G(u var1) {
      if (mc.gui.screen() == null && !c5.t("Freelook") && !c5.t("Freecam")) {
         p_.p(p_.o.b);
      }

      j6.U(this, j6.l.J, var1.R, var1.x, 1.0);
   }

   private void K() {
      p_.Y(p_.o.b);
   }

   private static u Q(LocalPlayer var0) {
      q1881b7c262e73011 var1 = (q1881b7c262e73011)var0;
      return new u(var1.larpYRotLast(), var1.larpXRotLast());
   }

   public boolean B() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.m != null && ta.e(this);
   }

   public boolean e() {
      return !this.isEnabled() ? false : this.B() && this.u.O("Camera") && this.V.O("None") && p_.A() == p_.o.b;
   }

   public static Input O(LocalPlayer var0, Input var1) {
      if (at != null && at.isEnabled()) {
         AuraModule var2 = at;
         if (var2 != null && var2.isRuntimeActive() && var2.m != null && var0 != null) {
            float var3 = var1.forward() == var1.backward() ? 0.0F : (var1.forward() ? 1.0F : -1.0F);
            byte var4 = var1.sprint();
            if (var2.K.G(var0)) {
               var4 = 0;
            } else if (var2.T.O("Keep Sprint") && var3 > 0.0F && !var1.shift() && !var0.horizontalCollision && !var0.isInWater() && !var0.isUnderWater()) {
               var4 = 1;
            } else if (var2.T.O("Legit")
               && (var1.sprint() || getMc().options != null && getMc().options.keySprint.isDown())
               && var3 > 0.0F
               && !var1.shift()
               && !var0.horizontalCollision) {
               var4 = 1;
            }

            Input var5 = new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump(), var1.shift(), (boolean)var4);
            if (p_.A() == p_.o.b) {
               double var6 = var2.m.getX() - var0.getX();
               double var8 = var2.m.getZ() - var0.getZ();
               double var10 = Mth.wrapDegrees(Math.toDegrees(Math.atan2(var8, var6)) - 90.0);
               var5 = var2.X(var0.input, var5, var0.getYRot(), var2.u.O("Camera") ? z() : var0.getYRot(), var10);
            }

            return var5;
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public Input X(ClientInput var1, Input var2, float var3, float var4, double var5) {
      if (!this.isEnabled()) {
         return var2;
      }

      if (!this.u.O("Target") && !this.V.O("None")) {
         float var7 = var2.forward() == var2.backward() ? 0.0F : (var2.forward() ? 1.0F : -1.0F);
         float var8 = var2.left() == var2.right() ? 0.0F : (var2.left() ? 1.0F : -1.0F);
         if (var7 == 0.0F && var8 == 0.0F) {
            return var2;
         }

         double var9 = this.V.O("Targeted") && var7 > 0.0F && var8 == 0.0F ? var5 : Mth.wrapDegrees(Math.toDegrees(h(var4, var7, var8)));
         float[] var11 = i(var9, var3);
         var1.moveVector = new Vec2(var11[1], var11[0]).normalized();
         return new Input(var11[0] > 0.0F, var11[0] < 0.0F, var11[1] > 0.0F, var11[1] < 0.0F, var2.jump(), var2.shift(), var2.sprint());
      } else {
         return var2;
      }
   }

   public static double h(float var0, float var1, float var2) {
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

   public static float[] i(double var0, float var2) {
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

   public static boolean N() {
      if (at != null && at.isEnabled()) {
         AuraModule var0 = at;
         return var0 != null && var0.isRuntimeActive() && var0.aA.getValue() && A() != null && (var0.aG || var0.aB > 0);
      } else {
         return false;
      }
   }

   public float A(float var1, float var2) {
      if (this.isEnabled()) {
         if (!this.B()) {
            return var1;
         } else if (this.Q()) {
            float var4 = Mth.wrapDegrees(z() - var1);
            return Math.abs(var4) > 0.5F ? var1 + var4 * 0.45F : z();
         } else {
            boolean var3 = this.aF.O("Normal");
            return O(var1, var2, var3 ? 35.0F : 50.0F, var3 ? 0.25F : 0.3F);
         }
      } else {
         return var1;
      }
   }

   public static float O(float var0, float var1, float var2, float var3) {
      float var4 = Mth.wrapDegrees(var1 - var0);
      if (Math.abs(var4) <= var2) {
         return var0;
      }

      float var5 = var4 < 0.0F ? var1 + var2 : var1 - var2;
      return var0 + Mth.wrapDegrees(var5 - var0) * var3;
   }

   private boolean Q() {
      LocalPlayer var1 = A();
      if (this.u.O("Camera") && this.V.O("Targeted") && var1 != null && var1.input != null && this.m != null) {
         Vec2 var2 = var1.input.getMoveVector();
         if (!(var2.y <= 0.0F) && !(Math.abs(var2.x) > 0.05F)) {
            float var3 = Mth.wrapDegrees((float)Math.toDegrees(Math.atan2(this.m.getZ() - var1.getZ(), this.m.getX() - var1.getX())) - 90.0F);
            return Math.abs(Mth.wrapDegrees(var3 - z())) <= 15.0F;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static LocalPlayer A() {
      return getMc().player;
   }

   public static boolean G() {
      if (at != null && at.isEnabled()) {
         AuraModule var0 = at;
         return var0 != null && var0.isRuntimeActive() && al;
      } else {
         return false;
      }
   }

   public static float z() {
      Minecraft var0 = Minecraft.getInstance();
      return f() ? p_.e(p_.V(), p_.s(), var0.options.getCameraType().isMirrored())[0] : var0.player.getYRot();
   }

   private static boolean f() {
      p_.o var0 = p_.A();
      return var0 == p_.o.b || var0 == p_.o.K || var0 == p_.o.R;
   }

   public static float p() {
      Minecraft var0 = Minecraft.getInstance();
      return f() ? p_.e(p_.V(), p_.s(), var0.options.getCameraType().isMirrored())[1] : var0.player.getXRot();
   }

   public static Vec3 c() {
      return tv.Z(z(), p());
   }

   public double s() {
      return this.F.getValue();
   }

   public LivingEntity u() {
      if (this.isEnabled()) {
         return this.isRuntimeActive() ? this.m : null;
      } else {
         return null;
      }
   }

   public double b() {
      return this.a8.getValue();
   }

   public double d() {
      return ta.c(this.J());
   }

   private double J() {
      return Math.max(0.0, this.F.getValue() - this.C.getValue());
   }

   public boolean x() {
      return !this.isEnabled() ? false : this.isOperational() && this.isInGame() && mc.gui.screen() == null && this.v.getValue() && this.b();
   }

   public float o() {
      return this.aO.getValue().floatValue();
   }

   public boolean A() {
      return this.l.getValue();
   }

   public boolean k() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.m != null && this.J.getValue() && ta.Y() && this.b();
   }

   public float J() {
      return this.aW.getValue().floatValue();
   }

   public double i() {
      return this.R.getValue();
   }

   public boolean F() {
      return this.aa.getValue();
   }

   public boolean z() {
      return this.H.getValue();
   }

   public boolean u() {
      return this.a6.getValue();
   }

   public String G() {
      if (this.m != null && mc.player != null) {
         String var1 = ta.I(this.m, this.J(), mc.player.getLookAngle());
         return "range".equals(var1) ? "Outside reach" : (var1 == null ? "Valid attack ray" : "Inside reach: " + var1);
      } else {
         return "No lock";
      }
   }

   @Override
   public String getStatus() {
      return this.aD;
   }

   private void o(String var1) {
      com.larpclient.h.U("Aura", var1);
   }

   public static void M(LocalPlayer var0, String var1) {
      AuraModule var2 = at;
      if (var2 != null && var2.isRuntimeActive() && var2.m != null && var0 != null) {
         if (var1.equals("jump-before")) {
            if (var2.s <= 0) {
               return;
            }

            var2.s--;
            var2.o = var0.tickCount + 16;
         }

         if (var0.tickCount <= var2.o) {
            Input var3 = var0.input.keyPresses;
            Vec2 var4 = var0.input.getMoveVector();
            Vec3 var5 = var0.getDeltaMovement();
            u var6 = Q(var0);
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
            com.larpclient.h.A(
               "AuraMovement",
               String.format(
                  Locale.ROOT,
                  "%s tick=%d mode=%s/%s/%s reference=%s target=%d ground=%s sprint=%s sprintReportMatches=%s reset=%s yaw=%.4f sentYaw=%.4f keys=%s physical=%s input=(%.5f,%.5f) pos=(%.6f,%.6f,%.6f) velocity=(%.6f,%.6f,%.6f) fall=%.5f orbit=%s",
                  var1,
                  var0.tickCount,
                  var2.G.getValue(),
                  var2.V.getValue(),
                  var2.T.getValue(),
                  var2.u.getValue(),
                  var2.m.getId(),
                  var0.onGround(),
                  var0.isSprinting(),
                  kq.M(var0.isSprinting()),
                  al,
                  var0.getYRot(),
                  var6.R,
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
                  TargetStrafeModule.q()
               )
            );
         }
      }
   }

   public static Input h(Input var0) {
      return si.X(var0);
   }
}
