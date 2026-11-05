package com.larpclient.module;

import com.larp.obf.q5682c6a8aca9b6ec.qac882a24641eea19;
import com.larpclient.LarpClient;
import com.larpclient.d8;
import com.larpclient.eo;
import com.larpclient.ep;
import com.larpclient.j5;
import com.larpclient.m;
import com.larpclient.pe;
import com.larpclient.r4;
import com.larpclient.r7;
import com.larpclient.rr;
import com.larpclient.xq;
import com.larpclient.yu;
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
   private static AuraModule f;
   public static volatile boolean s;
   private final lp Wv = (lp)this.addSetting(
         new lp("Activation", "Toggle acts until you switch it off; Hold only acts while the module shortcut is held", "Toggle", "Toggle", "Hold")
      )
      .b("Keybind Mode");
   private final lp L = this.addSetting(
      new lp("Rotation", "Rotation engine that turns the player toward the aim point", "Sloth", "Sloth", "FunTime", "Polar", "Matrix", "Legit") {
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
   private final lp H = this.addSetting(
      new lp("Aura Mode", "Blatant aims and swings on every chance; Normal waits until the attack is about to land", "Normal", "Blatant", "Normal") {
         @Override
         public void setValue(String var1) {
            super.setValue(AuraModule.F(var1));
         }

         @Override
         public boolean acceptsValue(String var1) {
            return super.acceptsValue(AuraModule.F(var1));
         }
      }
   );
   private final lp F = this.addSetting(new lp("Combat Version", "1.9+ waits for the attack charge; 1.8 clicks at the CPS range", "1.9", "1.9", "1.8") {
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
   private final lc Ws = this.addSetting(new lc("Attack Range", "Maximum distance to the target box for an attack", 3.0, 2.0, 6.0, 0.1));
   private final lc i = this.addSetting(new lc("Aim Range", "Extra distance beyond Attack Range where targets are tracked", 1.5, 0.0, 9.0, 0.1));
   private final lc a = this.addSetting(new lc("FOV", "Field of view around your camera for new targets", 360.0, 30.0, 360.0, 5.0));
   private final ln w = this.addSetting(new ln("Players", "Target players", true));
   private final ln Wn = this.addSetting(new ln("Mobs", "Target hostile and neutral mobs", true));
   private final ln P = this.addSetting(new ln("Animals", "Target animals", false));
   private final ln l = this.addSetting(new ln("Invisibles", "Target invisible players", true));
   private final ln d = this.addSetting(new ln("Naked Players", "Target players without armor", true));
   private final lp v = this.addSetting(
      new lp(
         "Criticals", "Adaptive lands criticals when available; Always waits for a falling hit; None ignores them", "Adaptive", "Adaptive", "Always", "None"
      )
   );
   private final lp X = this.addSetting(new lp("Aim Point", "Where on the target box to aim", "Nearest", "Nearest", "Center", "Multipoint", "Head", "Body"));
   private final ln e = this.addSetting(new ln("Predict", "Lead moving targets", false));
   private final lc Wy = this.addSetting(new lc("Predict Value", "Ticks of target motion to lead", 1.0, 0.0, 4.0, 0.1));
   private final lc K = this.addSetting(new lc("Reach Buffer", "Distance subtracted from Attack Range as a safety margin", 0.2, 0.0, 1.0, 0.05));
   private final ln WT = this.addSetting(new ln("Rotation ETA", "Retained from the reference, which never reads it; it has no effect", true));
   private final ln WY = this.addSetting(new ln("Entity Raycast", "Attack whichever valid entity the final aim ray actually hits", true));
   private final ln B = this.addSetting(new ln("Dynamic Safe Reach", "Shorten reach while the gap to the target is changing quickly, scaled by ping", true));
   private final ln m = this.addSetting(new ln("Predict Criticals", "Start the sprint reset a tick or two before the jump arc falls", true));
   private final ln o = this.addSetting(new ln("Randomize Timing", "Attack at a randomized charge between 97 and 100 percent", true));
   private final lp T = this.addSetting(
      new lp(
         "Sprint Mode",
         "Legit drops sprint a tick before criticals; Keep Sprint keeps it held; None leaves sprint alone",
         "Legit",
         "Legit",
         "Keep Sprint",
         "None"
      )
   );
   private final ln I = this.addSetting(new ln("Through Walls", "Select and attack targets behind blocks", false));
   private final ln We = this.addSetting(
      new ln("Wall Bypass", "Through-wall attacks that first send break start/abort actions for blocks along the line of sight", false)
   );
   private final ln c = this.addSetting(new ln("Shield Breaker", "Swap to your best hotbar axe for hits on a raised shield, then back next tick", true));
   private final ln W8 = this.addSetting(new ln("Shield Unpress", "Lower your own shield right before attacking", false));
   private final ln W5 = this.addSetting(new ln("Raycast", "Require a block-free line to the target for each attack", true));
   private final ln Q = this.addSetting(new ln("TPS Sync", "Scale timing and prediction to the measured server TPS", true));
   private final ln R = this.addSetting(new ln("Eating", "Do not attack while eating or drinking", true));
   private final ln U = this.addSetting(new ln("Breaking", "Do not attack while mining", false));
   private final ln A = this.addSetting(new ln("Placing", "Do not attack while placing blocks", false));
   private final ln WJ = this.addSetting(new ln("In Inventory", "Do not attack with a screen open", false));
   private final ln z = this.addSetting(new ln("In Lava", "Do not attack while in lava", false));
   private final ln WI = this.addSetting(new ln("In Web", "Do not attack while in cobweb", false));
   private final ln Wt = this.addSetting(new ln("In Water", "Do not attack while in water", false));
   private final lp E = this.addSetting(
      new lp("Movement Reference", "Target follows combat facing; Camera uses Move Fix to follow your view", "Target", "Target", "Camera")
   );
   private final lp r = this.addSetting(
      new lp(
         "Move Fix",
         "Silent and Free steer your keys toward the camera direction; Targeted walks at the target on W; None moves exactly with the camera",
         "Silent",
         "Silent",
         "Targeted",
         "Free",
         "None"
      )
   );
   private final lc O = this.addSetting(new lc("Min CPS", "Lowest 1.8 clicks per second", 8.0, 4.0, 16.0, 0.5));
   private final lc k = this.addSetting(new lc("Max CPS", "Highest 1.8 clicks per second", 11.0, 4.0, 16.0, 0.5));
   private final ln p = this.addSetting(new ln("Auto Jump", "Jump to set up criticals", false));
   private final ln Wf = this.addSetting(new ln("Smart Aim Visibility", "Move the aim point to the nearest visible part of the target", true) {
      @Override
      public String getDisplayName() {
         return "Smart Aim (Visibility)";
      }
   });
   private final ln x = this.addSetting(new ln("Focus Target", "Keep the current target while it stays valid", true));
   private final lp q = this.addSetting(new lp("Sort Mode", "Order for choosing a new target", "Distance", "Distance", "Health", "FOV", "Armor"));
   private final ln V = this.addSetting(new ln("Deliberate Miss", "Occasionally swing just past the target", false));
   private final lc W1 = this.addSetting(new lc("Miss Threshold", "Average hits between deliberate misses", 28.0, 10.0, 80.0, 1.0));
   private final ln y = this.addSetting(new ln("FOV Cone", "Show the attack range arc on the ground", true));
   private final lc j = this.addSetting(new lc("Indicator Width", "Thickness of FOV range lines", 3.5, 1.0, 6.0, 0.5));
   private final ln G = this.addSetting(new ln("Visible Lines", "Keep FOV lines visible over terrain and entities", false));
   private final ln Wj = this.addSetting(new ln("Hit Zone", "Terrain positioning guide around the current target", true));
   private final lc J = this.addSetting(new lc("Zone Width", "Ground boundary stroke width", 2.0, 1.0, 6.0, 0.5));
   private final lc Wa = this.addSetting(new lc("Zone Opacity", "Ground guide opacity", 0.65, 0.05, 1.0, 0.05));
   private final ln Wo = this.addSetting(new ln("Zone Fill", "Faint fill on supported collision surfaces", false));
   private final ln Z = this.addSetting(new ln("Zone Bloom", "Soft glow through the shared visual effects", true));
   private final ln D = this.addSetting(new ln("Zone Motion", "Traveling highlights along the terrain boundary", true));
   private final l7 W = new l7();
   private final lq n = new lq(this);
   private final l9 WE = new l9();
   private final ls h = new ls();
   private final l5 WQ = new l5();
   private final l8 N = new l8();
   private final lb g = new lb();
   private final lu Wz = new lu();
   private final lt Y = new lt();
   private lg WB;
   private LivingEntity t;
   private LivingEntity WK;
   private float S;
   private float C;
   private long Wu;
   private int WU = Integer.MIN_VALUE;
   private double u = 1.0;
   private boolean b;
   private int Wc;
   private boolean WM;
   private String WG = "Idle";
   private int W0;
   private int M = Integer.MIN_VALUE;

   public AuraModule() {
      super("Aura", "Automatically attacks nearby entities", Category.COMBAT, -1);
      f = this;
      this.Wy.x(this.e::getValue);
      this.r.x(() -> this.E.i("Camera"));
      this.m.x(() -> !this.v.i("None"));
      this.o.x(() -> !this.z());
      this.O.x(this::z);
      this.k.x(this::z);
      this.W1.x(this.V::getValue);
      this.j.x(this.y::getValue);
      this.G.x(this.y::getValue);
      this.J.x(this.Wj::getValue);
      this.Wa.x(this.Wj::getValue);
      this.Wo.x(this.Wj::getValue);
      this.Z.x(this.Wj::getValue);
      this.D.x(this.Wj::getValue);
      this.L.W(var1 -> this.G("Rotation engine: " + var1));
   }

   public static AuraModule G() {
      return f;
   }

   @Override
   public String getDisplayName() {
      return "Kill Aura";
   }

   public boolean m() {
      return this.Wv.i("Hold");
   }

   @Override
   public lp keybindMode() {
      return this.Wv;
   }

   @Override
   public void toggle() {
      if (this.m() && mc != null && mc.gui != null && mc.gui.screen() == null) {
         this.setEnabled(true);
      } else {
         super.toggle();
      }
   }

   private boolean P() {
      if (!this.m()) {
         return true;
      } else {
         int var1 = this.getKeyBind();
         if (!pe.F(var1) || mc.getWindow() == null) {
            return false;
         } else {
            return mc.gui.screen() == null && mc.isWindowActive() ? pe.A(mc.getWindow().handle(), var1) : false;
         }
      }
   }

   private static String F(String var0) {
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
      return !this.WJ.getValue();
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a, ld.c);
   }

   public List<l6<?>> o() {
      return List.of(this.L, this.H, this.E, this.r, this.X, this.e, this.Wy, this.Wf, this.WT);
   }

   public List<l6<?>> J() {
      return List.of(this.F, this.Ws, this.i, this.K, this.B, this.v, this.m, this.T, this.p, this.o, this.O, this.k, this.WY, this.V, this.W1);
   }

   public List<l6<?>> B() {
      return List.of(this.w, this.Wn, this.P, this.l, this.d);
   }

   public List<l6<?>> F() {
      return List.of(this.a, this.q, this.x);
   }

   public List<l6<?>> Z() {
      return List.of(this.I, this.We, this.c, this.W8, this.W5, this.Q);
   }

   public List<l6<?>> X() {
      return List.of(this.R, this.U, this.A, this.WJ, this.z, this.WI, this.Wt);
   }

   public List<l6<?>> g() {
      return List.of(this.y, this.j, this.G, this.Wj, this.J, this.Wa, this.Wo, this.Z, this.D);
   }

   @Override
   public void onEnable() {
      this.W0 = 3;
      this.M = Integer.MIN_VALUE;
      this.n.Q();
      this.W.K();
      this.WE.z();
      ll.q();
      this.t = this.WK = null;
      s = (boolean)0;
      this.S = this.C = 0.0F;
      this.Wu = 0L;
      this.WU = Integer.MIN_VALUE;
      this.u = 1.0;
      this.o();
      this.WG = "Idle";
      r4.V(
         "Aura",
         "Movement: Reference="
            + this.E.getValue()
            + "; Move Fix="
            + this.r.getValue()
            + "; vanilla sprint eligibility; attacks wait for matching sprint reports"
      );
   }

   @Override
   public void onDisable() {
      this.t = this.WK = null;
      s = (boolean)0;
      this.S = this.C = 0.0F;
      this.Wu = 0L;
      this.WU = Integer.MIN_VALUE;
      this.u = 1.0;
      this.o();
      this.a();
      if (this.WB != null) {
         this.WB.reset();
      }

      this.WB = null;
      this.n.q(this.getPlayer());
      this.n.i(mc);
      this.n.Q();
      this.W.K();
      this.WE.z();
      ll.q();
      this.WG = "Idle";
   }

   @Override
   public void onTick() {
      this.n.b(mc);
      if (this.isInGame()) {
         LocalPlayer var1 = mc.player;
         if (!this.P()) {
            this.S("Hold released");
         } else {
            int var10000 = d8.r(this);
            boolean var43 = false;
            byte var42 = (byte)var10000;
            var10000 = Integer.signum(var42);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && !var1.isSpectator() && var1.isAlive()) {
               boolean var45 = false;
               int var44 = this.Wc;
               if ((-Integer.signum(var44) >>> 31 ^ 1) == 0) {
                  byte var47 = 1;
                  int var46 = this.Wc;
                  this.Wc = var46 - var47;
               }

               boolean var2 = this.H.i("Blatant");
               boolean var3 = this.B();
               int var4 = !var3 && this.n.r(var1, this.z(), this.Q.getValue(), this.o.getValue(), var2) ? 1 : 0;
               float var5 = this.n.a(var1, this.z(), this.Q.getValue(), this.o.getValue(), var2);
               int var6 = !this.I.getValue() && !this.We.getValue() ? 0 : 1;
               double var7 = Math.max(this.Ws.getValue() + this.i.getValue(), RodSwapModule.L());
               l7.l var9 = new l7.l(
                  var7,
                  this.w.getValue(),
                  this.Wn.getValue(),
                  this.P.getValue(),
                  this.l.getValue(),
                  this.d.getValue(),
                  this.x.getValue(),
                  (boolean)var6,
                  this.q.getValue(),
                  this.a.getValue()
               );
               this.t = this.W.V(var9);
               lx var10 = new lx(var1.getYRot(), var1.getXRot());
               if (this.t != this.WK) {
                  if (this.WB != null) {
                     this.WB.targetChanged(this.t);
                  }

                  ll.q();
                  if (this.t != null) {
                     this.Wu = System.currentTimeMillis();
                     this.n.n();
                     this.G("Target acquired: entity=" + this.t.getId());
                  } else {
                     this.a();
                     this.G("Target lost");
                  }

                  this.WK = this.t;
               }

               if (this.t == null) {
                  this.o();
                  s = (boolean)0;
                  this.n.q(var1);
                  this.Wu = 0L;
                  this.a();
                  this.WG = "No target";
               } else {
                  lg var11 = this.t();
                  if (var11 != this.WB) {
                     if (this.WB != null) {
                        this.WB.reset();
                     }

                     this.WB = var11;
                  }

                  AABB var12 = this.t.getBoundingBox();
                  double var13 = this.T(this.t);
                  int var15 = lf.p(var1, var12) <= var13 ? 1 : 0;
                  float var16 = 1.25F;
                  if (var15 != 0) {
                     lx var17 = le.p(ll.x(var12));
                     var16 = Math.max(var16, this.WB.ticksToReach(var10, var17) + 0.35F);
                     var16 = Math.max(var16, var2 ? 1.0F : 4.5F);
                  }

                  int var51;
                  label245: {
                     label244: {
                        var51 = var3 || var15 == 0 || var4 == 0 && !(var5 <= var16) ? 0 : 1;
                        int var18 = var15 != 0 && this.Wu > 0L && System.currentTimeMillis() - this.Wu < (var2 ? 150L : 500L) ? 1 : 0;
                        if (var15 != 0) {
                           if (var2 || var51 != 0) {
                              break label244;
                           }

                           boolean var49 = false;
                           int var48 = var18;
                           var10000 = Integer.signum(var48);
                           if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                              break label244;
                           }
                        }

                        var66 = 0;
                        break label245;
                     }

                     var66 = 1;
                  }

                  int var19 = var66;
                  Vec3 var20 = this.C(this.t, (boolean)var19);
                  Vec3 var21 = RodSwapModule.q(this.t);
                  if (var21 != null) {
                     var20 = var21;
                  }

                  lx var22 = le.p(var20);
                  var19 = !(lf.p(var1, var12) <= var13) || !var2 && var51 == 0 ? 0 : 1;
                  int var23 = var19 == 0 || var4 == 0 && !(var5 <= (var2 ? 0.5F : 1.15F)) ? 0 : 1;
                  boolean var24 = this.C((boolean)var23);
                  int var25 = this.WB == this.Y ? 1 : 0;
                  if (this.Wu > 0L && var19 == 0 && var25 == 0) {
                     long var26 = System.currentTimeMillis() - this.Wu;
                     long var28 = var2 ? 0L : 120L;
                     float var30 = Mth.clamp((float)var26 / (float)var28, 0.0F, 1.0F);
                     if (var30 < 1.0F) {
                        var22 = new lx(var10.z + le.P(var10.z, var22.z) * var30, var10.I + (var22.I - var10.I) * var30);
                     }
                  }

                  lx var53 = L(var1);
                  boolean var27 = lf.X(var1, var12.inflate(this.t.getPickRadius()), var53.z, var53.I, this.Ws.getValue() + 1.0, (boolean)var6);
                  int var54 = var19 == 0 && var21 == null ? 0 : 1;
                  lx var29 = this.WB.rotate(var10, var22, this.t, (boolean)var54, var27);
                  if (var2 && var25 == 0) {
                     this.S += 0.015F;
                     double var56 = this.h.q(this.S, 0.0, 0.0);
                     double var32 = this.h.q(0.0, this.S, 0.0);
                     float var34 = var54 != 0 ? 0.15F : 1.0F;
                     var29.z += (float)(var56 * 0.12F * var34);
                     var29.I = Mth.clamp(var29.I + (float)(var32 * 0.08F * var34), -90.0F, 90.0F);
                  }

                  if (this.V.getValue()) {
                     this.WE.x(this.W1.getValue().intValue());
                     this.WE.c();
                     if (this.WE.w()) {
                        var29 = this.WE.i(var29);
                     }
                  } else {
                     this.WE.z();
                  }

                  if (this.WB == this.Y) {
                     var29 = this.Y.C(var10, var29);
                  } else {
                     var29 = le.c(var10, var29);
                  }

                  this.x(var29);
                  if (!var24 && !var3 && (!this.V.getValue() || !this.WE.w())) {
                     boolean var57 = lf.X(var1, var12.inflate(this.t.getPickRadius()), var29.z, var29.I, var13, (boolean)var6);
                     lx var31 = !var2 && !var57 ? var53 : var29;
                     LivingEntity var58 = this.t;
                     AABB var33 = var12;
                     double var59 = var13;
                     byte var36 = 1;
                     if (this.WY.getValue()) {
                        EntityHitResult var37 = lf.y(var1, var31.z, var31.I, var13, (boolean)var6);
                        var36 = 0;
                        if (var37 != null && var37.getEntity() instanceof LivingEntity var38 && this.W.F(var38, var9)) {
                           double var62 = this.T(var38);
                           AABB var41 = var38.getBoundingBox();
                           if (lf.p(var1, var41) <= var62) {
                              var58 = var38;
                              var33 = var41;
                              var59 = var62;
                              var36 = 1;
                           }
                        }
                     }

                     LivingEntity var60 = var58;
                     Runnable var61 = this.We.getValue() ? () -> U(var60) : null;
                     boolean var63 = this.n
                        .V(
                           var1,
                           new lq.u(
                              var58,
                              var33,
                              var31.z,
                              var31.I,
                              var59,
                              (boolean)var6,
                              this.S(),
                              this.j(),
                              this.c.getValue(),
                              this.R.getValue(),
                              this.W5.getValue() && var6 == 0,
                              (boolean)var36,
                              this.p.getValue(),
                              this.m.getValue(),
                              var61,
                              this.z(),
                              this.Q.getValue(),
                              this.o.getValue(),
                              var2,
                              this.O.getValue().floatValue(),
                              this.k.getValue().floatValue()
                           )
                        );
                     this.N();
                     if (var63) {
                        this.G("Attack executed: entity=" + var58.getId());
                        if (this.V.getValue()) {
                           this.WE.p();
                        }
                     }
                  } else if (var3) {
                     this.n.q(var1);
                  }

                  this.WG = var15 != 0 ? (var4 != 0 ? "Attacking" : "Charging") : "Tracking";
               }
            } else {
               this.S("Interaction unavailable");
            }
         }
      }
   }

   private void S(String var1) {
      if (this.t != null || this.WK != null) {
         this.G("Paused: " + var1);
      }

      this.t = this.WK = null;
      s = (boolean)0;
      this.n.q(this.getPlayer());
      this.o();
      this.a();
      this.WG = var1;
   }

   private double T(LivingEntity var1) {
      double var2 = Math.max(0.0, this.Ws.getValue() - this.K.getValue());
      if (this.B.getValue() && mc.player != null && var1 != null) {
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
         boolean var17 = this.H.i("Blatant");
         double var18 = var15 * this.s() * (var17 ? 0.15 : 0.4);
         double var20 = Mth.clamp(var18, 0.0, var17 ? 0.08 : 0.25);
         return Math.max(0.0, var2 - var20);
      } else {
         return var2;
      }
   }

   private double s() {
      int var1 = mc.player.tickCount;
      if (this.WU == Integer.MIN_VALUE || var1 < this.WU || var1 - this.WU >= 20) {
         double var2 = 1.0;
         if (mc.getConnection() != null) {
            PlayerInfo var4 = mc.getConnection().getPlayerInfo(mc.player.getUUID());
            if (var4 != null) {
               var2 = var4.getLatency() / 50.0F;
            }
         }

         this.u = Mth.clamp(var2, 0.5, 3.0);
         this.WU = var1;
      }

      return this.u;
   }

   private Vec3 C(LivingEntity var1, boolean var2) {
      double var3 = this.Ws.getValue() + this.i.getValue();
      int var5 = this.WB == this.Y ? 1 : 0;
      int var6 = var5;
      int var7 = this.WB == this.Y && this.e.getValue() && !var2 ? 1 : 0;
      Vec3 var8 = var7 != 0 ? l3.a(var1, this.c()) : (this.e.getValue() && !var2 && var6 == 0 ? l3.a(var1, this.Q()) : var1.position());
      AABB var9 = var1.getBoundingBox().move(var8.x - var1.getX(), var8.y - var1.getY(), var8.z - var1.getZ());
      Vec3 var10;
      if (var6 == 0 && !this.X.i("Center") && (!this.H.i("Normal") || this.X.i("Head") || this.X.i("Body") || this.X.i("Multipoint"))) {
         if (this.X.i("Multipoint")) {
            var10 = ll.W(var1, var9, var3);
         } else if (this.X.i("Head")) {
            var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.78F, var9.getCenter().z);
         } else if (this.X.i("Body")) {
            var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.55F, var9.getCenter().z);
         } else {
            var10 = ll.x(var9);
         }
      } else {
         var10 = new Vec3(var9.getCenter().x, var9.minY + var9.getYsize() * 0.6F, var9.getCenter().z);
      }

      if (var2 && var5 != 0) {
         var10 = x(var1, var9, var10);
      }

      int var11 = !var2 && (this.H.i("Normal") || var5 != 0) && (var6 == 0 || var5 != 0) && (var5 == 0 || !T(var1)) ? 1 : 0;
      if (var11 != 0) {
         float var12 = var5 != 0 ? 0.032F : 0.012F;
         double var13 = var1.getBbWidth() * (var5 != 0 ? 0.24F : 0.15F);
         double var15 = var1.getBbHeight() * (var5 != 0 ? 0.16F : 0.12F);
         this.C += var12;
         double var17 = this.h.q(this.C, 120.0, 0.0) * var13;
         double var19 = this.h.q(0.0, this.C, 240.0) * var15;
         double var21 = this.h.q(0.0, 120.0, this.C) * var13;
         var10 = M(var10.add(var17, var19, var21), var9);
      }

      if (this.Wf.getValue() && !this.I.getValue() && !this.We.getValue()) {
         var10 = ll.U(var9, var10, var3);
      }

      return var10;
   }

   private double Q() {
      double var1 = 1.0;
      if (this.Q.getValue()) {
         float var3 = lm.a();
         if (Float.isFinite(var3) && var3 > 0.0F) {
            var1 = 20.0F / Mth.clamp(var3, 10.0F, 20.0F);
         }
      }

      return Mth.clamp(this.Wy.getValue() * var1, 0.0, 4.0);
   }

   private double c() {
      return Mth.clamp(Math.max(1.0, this.Q()) + 1.0, 1.0, 3.0);
   }

   private static Vec3 x(LivingEntity var0, AABB var1, Vec3 var2) {
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

   private static boolean T(LivingEntity var0) {
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

   private lg t() {
      return switch ((String)this.L.getValue()) {
         case "FunTime" -> this.N;
         case "Polar" -> this.g;
         case "Matrix" -> this.Wz;
         case "Legit" -> this.Y;
         default -> this.WQ;
      };
   }

   private lq.z S() {
      return switch ((String)this.v.getValue()) {
         case "Always" -> lq.z.u;
         case "None" -> lq.z.J;
         default -> lq.z.s;
      };
   }

   private lq.x j() {
      return switch ((String)this.T.getValue()) {
         case "Keep Sprint" -> lq.x.t;
         case "None" -> lq.x.P;
         default -> lq.x.L;
      };
   }

   private boolean z() {
      return this.F.i("1.8");
   }

   private boolean B() {
      if (mc.player != null && mc.level != null) {
         t var1 = com.larpclient.module.t.d();
         k var2 = LarpClient.y().e().y(k.class);
         if (var1 != null && var1.isRuntimeActive() && com.larpclient.m.h(var1)) {
            return true;
         } else if (var2 != null && var2.isRuntimeActive() && com.larpclient.m.h(var2)) {
            return true;
         } else {
            AutoAnchorModule var3 = AutoAnchorModule.y();
            if (var3 != null && var3.isRuntimeActive() && com.larpclient.m.h(var3)) {
               return true;
            } else if (this.R.getValue() && lf.B()) {
               return true;
            } else if (this.U.getValue() && this.G()) {
               return true;
            } else if (this.A.getValue() && this.A()) {
               return true;
            } else if (this.WJ.getValue() && mc.gui.screen() != null) {
               return true;
            } else if (this.z.getValue() && mc.player.isInLava()) {
               return true;
            } else {
               return this.WI.getValue() && lf.u() ? true : this.Wt.getValue() && (mc.player.isInWater() || mc.player.isUnderWater());
            }
         }
      } else {
         return true;
      }
   }

   private boolean G() {
      return mc.gameMode != null && mc.gameMode.isDestroying()
         ? true
         : mc.options != null && mc.options.keyAttack.isDown() && mc.hitResult != null && mc.hitResult.getType() == Type.BLOCK;
   }

   private boolean A() {
      return mc.options != null && mc.options.keyUse.isDown()
         ? mc.player.getMainHandItem().getItem() instanceof BlockItem || mc.player.getOffhandItem().getItem() instanceof BlockItem
         : false;
   }

   private boolean C(boolean var1) {
      if (!this.W8.getValue() || !var1) {
         this.o();
         return false;
      }

      if (this.b) {
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
         this.WM = (boolean)1;
      }

      mc.gameMode.releaseUsingItem(mc.player);
      this.b = (boolean)1;
      return true;
   }

   private void N() {
      if (this.b) {
         this.Wc = 1;
      }

      this.b = (boolean)0;
      this.G();
   }

   private void o() {
      this.b = (boolean)0;
      this.Wc = 0;
      this.G();
   }

   private void G() {
      if (this.WM && mc.options != null) {
         int var1 = this.b();
         int var2;
         if (var1 >= 0 && mc.getWindow() != null) {
            var2 = eo.Y(mc.getWindow().handle(), var1) == 1 ? 1 : 0;
         } else {
            var2 = mc.options.keyUse.isDown();
         }

         mc.options.keyUse.setDown((boolean)var2);
         this.WM = (boolean)0;
      } else {
         this.WM = (boolean)0;
      }
   }

   private int b() {
      if (mc.options == null) {
         return -1;
      }

      for (int var1 = 0; var1 <= 7; var1++) {
         if (mc.options.keyUse.matches(com.mojang.blaze3d.platform.InputConstants.Type.MOUSE.getOrCreate(r7.y(var1)))) {
            return var1;
         }
      }

      return -1;
   }

   private static void U(LivingEntity var0) {
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

   private void x(lx var1) {
      if (mc.gui.screen() == null && !yu.G("Freelook") && !yu.G("Freecam")) {
         rr.W(rr.f.B);
      }

      ep.t(this, ep.z.n, var1.z, var1.I, 1.0);
   }

   private void a() {
      rr.N(rr.f.B);
   }

   private static lx L(LocalPlayer var0) {
      qac882a24641eea19 var1 = (qac882a24641eea19)var0;
      return new lx(var1.larpYRotLast(), var1.larpXRotLast());
   }

   public boolean Y() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.t != null && d8.T(this);
   }

   public boolean l() {
      return !this.isEnabled() ? false : this.Y() && this.E.i("Camera") && this.r.i("None") && rr.u() == rr.f.B;
   }

   public static Input B(LocalPlayer var0, Input var1) {
      if (f != null && f.isEnabled()) {
         AuraModule var2 = f;
         if (var2 != null && var2.isRuntimeActive() && var2.t != null && var0 != null) {
            float var3 = var1.forward() == var1.backward() ? 0.0F : (var1.forward() ? 1.0F : -1.0F);
            byte var4 = var1.sprint();
            if (var2.n.W(var0)) {
               var4 = 0;
            } else if (var2.T.i("Keep Sprint") && var3 > 0.0F && !var1.shift() && !var0.horizontalCollision && !var0.isInWater() && !var0.isUnderWater()) {
               var4 = 1;
            } else if (var2.T.i("Legit")
               && (var1.sprint() || getMc().options != null && getMc().options.keySprint.isDown())
               && var3 > 0.0F
               && !var1.shift()
               && !var0.horizontalCollision) {
               var4 = 1;
            }

            Input var5 = new Input(var1.forward(), var1.backward(), var1.left(), var1.right(), var1.jump(), var1.shift(), (boolean)var4);
            if (rr.u() == rr.f.B) {
               double var6 = var2.t.getX() - var0.getX();
               double var8 = var2.t.getZ() - var0.getZ();
               double var10 = Mth.wrapDegrees(Math.toDegrees(Math.atan2(var8, var6)) - 90.0);
               var5 = var2.b(var0.input, var5, var0.getYRot(), var2.E.i("Camera") ? s() : var0.getYRot(), var10);
            }

            return var5;
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public Input b(ClientInput var1, Input var2, float var3, float var4, double var5) {
      if (!this.isEnabled()) {
         return var2;
      }

      if (!this.E.i("Target") && !this.r.i("None")) {
         float var7 = var2.forward() == var2.backward() ? 0.0F : (var2.forward() ? 1.0F : -1.0F);
         float var8 = var2.left() == var2.right() ? 0.0F : (var2.left() ? 1.0F : -1.0F);
         if (var7 == 0.0F && var8 == 0.0F) {
            return var2;
         }

         double var9 = this.r.i("Targeted") && var7 > 0.0F && var8 == 0.0F ? var5 : Mth.wrapDegrees(Math.toDegrees(P(var4, var7, var8)));
         float[] var11 = g(var9, var3);
         var1.moveVector = new Vec2(var11[1], var11[0]).normalized();
         return new Input(var11[0] > 0.0F, var11[0] < 0.0F, var11[1] > 0.0F, var11[1] < 0.0F, var2.jump(), var2.shift(), var2.sprint());
      } else {
         return var2;
      }
   }

   public static double P(float var0, float var1, float var2) {
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

   public static float[] g(double var0, float var2) {
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

   public static boolean W() {
      if (f != null && f.isEnabled()) {
         AuraModule var0 = f;
         return var0 != null && var0.isRuntimeActive() && var0.W8.getValue() && j() != null && (var0.b || var0.Wc > 0);
      } else {
         return false;
      }
   }

   public float n(float var1, float var2) {
      if (this.isEnabled()) {
         if (!this.Y()) {
            return var1;
         } else if (this.C()) {
            float var4 = Mth.wrapDegrees(s() - var1);
            return Math.abs(var4) > 0.5F ? var1 + var4 * 0.45F : s();
         } else {
            boolean var3 = this.H.i("Normal");
            return B(var1, var2, var3 ? 35.0F : 50.0F, var3 ? 0.25F : 0.3F);
         }
      } else {
         return var1;
      }
   }

   public static float B(float var0, float var1, float var2, float var3) {
      float var4 = Mth.wrapDegrees(var1 - var0);
      if (Math.abs(var4) <= var2) {
         return var0;
      }

      float var5 = var4 < 0.0F ? var1 + var2 : var1 - var2;
      return var0 + Mth.wrapDegrees(var5 - var0) * var3;
   }

   private boolean C() {
      LocalPlayer var1 = j();
      if (this.E.i("Camera") && this.r.i("Targeted") && var1 != null && var1.input != null && this.t != null) {
         Vec2 var2 = var1.input.getMoveVector();
         if (!(var2.y <= 0.0F) && !(Math.abs(var2.x) > 0.05F)) {
            float var3 = Mth.wrapDegrees((float)Math.toDegrees(Math.atan2(this.t.getZ() - var1.getZ(), this.t.getX() - var1.getX())) - 90.0F);
            return Math.abs(Mth.wrapDegrees(var3 - s())) <= 15.0F;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static LocalPlayer j() {
      return getMc().player;
   }

   public static boolean w() {
      if (f != null && f.isEnabled()) {
         AuraModule var0 = f;
         return var0 != null && var0.isRuntimeActive() && s;
      } else {
         return false;
      }
   }

   public static float s() {
      Minecraft var0 = Minecraft.getInstance();
      return I() ? rr.l(rr.j(), rr.T(), var0.options.getCameraType().isMirrored())[0] : var0.player.getYRot();
   }

   private static boolean I() {
      rr.f var0 = rr.u();
      return var0 == rr.f.B || var0 == rr.f.T || var0 == rr.f.E;
   }

   public static float K() {
      Minecraft var0 = Minecraft.getInstance();
      return I() ? rr.l(rr.j(), rr.T(), var0.options.getCameraType().isMirrored())[1] : var0.player.getXRot();
   }

   public static Vec3 W() {
      return le.W(s(), K());
   }

   public double H() {
      return this.Ws.getValue();
   }

   public LivingEntity G() {
      if (this.isEnabled()) {
         return this.isRuntimeActive() ? this.t : null;
      } else {
         return null;
      }
   }

   public double p() {
      return this.a.getValue();
   }

   public double X() {
      return d8.T(this.e());
   }

   private double e() {
      return Math.max(0.0, this.Ws.getValue() - this.K.getValue());
   }

   public boolean h() {
      return !this.isEnabled() ? false : this.isOperational() && this.isInGame() && mc.gui.screen() == null && this.y.getValue() && this.P();
   }

   public float o() {
      return this.j.getValue().floatValue();
   }

   public boolean y() {
      return this.G.getValue();
   }

   public boolean b() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.t != null && this.Wj.getValue() && d8.a() && this.P();
   }

   public float b() {
      return this.J.getValue().floatValue();
   }

   public double n() {
      return this.Wa.getValue();
   }

   public boolean x() {
      return this.Wo.getValue();
   }

   public boolean o() {
      return this.Z.getValue();
   }

   public boolean f() {
      return this.D.getValue();
   }

   public String m() {
      if (this.t != null && mc.player != null) {
         String var1 = d8.R(this.t, this.e(), mc.player.getLookAngle());
         return "range".equals(var1) ? "Outside reach" : (var1 == null ? "Valid attack ray" : "Inside reach: " + var1);
      } else {
         return "No lock";
      }
   }

   @Override
   public String getStatus() {
      return this.WG;
   }

   private void G(String var1) {
      r4.g("Aura", var1);
   }

   public static void J(LocalPlayer var0, String var1) {
      AuraModule var2 = f;
      if (var2 != null && var2.isRuntimeActive() && var2.t != null && var0 != null) {
         if (var1.equals("jump-before")) {
            if (var2.W0 <= 0) {
               return;
            }

            var2.W0--;
            var2.M = var0.tickCount + 16;
         }

         if (var0.tickCount <= var2.M) {
            Input var3 = var0.input.keyPresses;
            Vec2 var4 = var0.input.getMoveVector();
            Vec3 var5 = var0.getDeltaMovement();
            lx var6 = L(var0);
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
            r4.V(
               "AuraMovement",
               String.format(
                  Locale.ROOT,
                  "%s tick=%d mode=%s/%s/%s reference=%s target=%d ground=%s sprint=%s sprintReportMatches=%s reset=%s yaw=%.4f sentYaw=%.4f keys=%s physical=%s input=(%.5f,%.5f) pos=(%.6f,%.6f,%.6f) velocity=(%.6f,%.6f,%.6f) fall=%.5f orbit=%s",
                  var1,
                  var0.tickCount,
                  var2.L.getValue(),
                  var2.r.getValue(),
                  var2.T.getValue(),
                  var2.E.getValue(),
                  var2.t.getId(),
                  var0.onGround(),
                  var0.isSprinting(),
                  xq.E(var0.isSprinting()),
                  s,
                  var0.getYRot(),
                  var6.z,
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
                  TargetStrafeModule.D()
               )
            );
         }
      }
   }

   public static Input a(Input var0) {
      return j5.g(var0);
   }
}
