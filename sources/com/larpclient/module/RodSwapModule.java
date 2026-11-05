package com.larpclient.module;

import com.larp.obf.q5682c6a8aca9b6ec.q143ef5d8ac4bd5b8;
import com.larp.obf.q5682c6a8aca9b6ec.qa1ad45ac3ead4599;
import com.larpclient._g;
import com.larpclient.d8;
import com.larpclient.d_;
import com.larpclient.ep;
import com.larpclient.hp;
import com.larpclient.j5;
import com.larpclient.o_;
import com.larpclient.r4;
import com.larpclient.rr;
import java.util.List;
import java.util.Set;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class RodSwapModule extends Module {
   private static RodSwapModule I;
   private final lp v = this.addSetting(
      new lp("Mode", "Cast Only returns once the bobber hits or misses; Hook and Reel pulls a hooked player once", "Cast Only", "Cast Only", "Hook and Reel")
   );
   private final lc R = this.addSetting(
      new lc("Range", "Farthest player distance for a throw; Kill Aura tracks players this far while a rod is ready", 6.0, 3.0, 8.0, 0.25)
   );
   private final lc l = this.addSetting(new lc("Delay", "Milliseconds with your weapon back in hand before the next throw", 1000.0, 250.0, 5000.0, 50.0));
   private final lc r = this.addSetting(new lc("Timeout", "Longest a throw keeps the rod out before returning to your weapon", 1000.0, 250.0, 2000.0, 50.0));
   private static final int E = 20;
   private static final long B = 250000000L;
   private static final int d = 20;
   private static final double m = 0.5;
   private final o_ F = new o_(this);
   private final com.larpclient.lx g = new com.larpclient.lx();
   private final com.larpclient.lx.a c = new com.larpclient.lx.a();
   private String a;
   private long Q;
   private LocalPlayer h;
   private ClientLevel G;
   private Player H;
   private Player x;
   private FishingHook s;
   private ItemStack j = ItemStack.EMPTY;
   private int D = -1;
   private int O = -1;
   private int C;
   private String f = "Enable Kill Aura";

   public RodSwapModule() {
      super("Rod Swap", "Kill Aura throws a hotbar rod at its player target on a delay, then returns to its weapon", Category.COMBAT, -1);
      I = this;
      this.v.W(var1 -> this.d("Mode changed", true));
   }

   @Override
   public String getDisplayName() {
      return "Auto Rod";
   }

   public List<l6<?>> C() {
      return List.of(this.v);
   }

   public List<l6<?>> W() {
      return List.of(this.R, this.l, this.r);
   }

   public static void c() {
      if (I != null && I.isRuntimeActive()) {
         I.d("Manual slot selection", false);
         I.g.r(System.nanoTime());
      }
   }

   public static boolean k() {
      return I != null && I.isEnabled() ? I != null && I.isRuntimeActive() && I.g.U() && I.F.k() : false;
   }

   public static void x() {
      if (k()) {
         I.d("Your attack", true);
      }
   }

   public static double L() {
      RodSwapModule var0 = I;
      return var0 != null && var0.isRuntimeActive() && var0.h != null && var0.A() >= 0 ? var0.R.getValue() : 0.0;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a, ld.c);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.f : "";
   }

   @Override
   public void onEnable() {
      this.h = getMc().player;
      this.G = mc.level;
      this.g.s();
      this.c.L();
      this.b();
      this.f = "Ready";
      this.a = null;
      r4.V(
         "RodSwap",
         "Auto Rod enabled; mode={}, range={}, delay={}ms, timeout={}ms",
         this.v.getValue(),
         this.R.getValue(),
         this.l.getValue().longValue(),
         this.r.getValue().longValue()
      );
   }

   @Override
   public void onDisable() {
      this.d("Disabled or disconnected", true);
      this.b();
      this.h = null;
      this.G = null;
   }

   private boolean F() {
      Minecraft var1 = getMc();
      return this.h != null
         && this.h == var1.player
         && this.G != null
         && this.G == var1.level
         && (j5.A(var1) || j5.o(var1, rr.f.B))
         && !this.h.isSpectator()
         && this.h.isAlive();
   }

   private boolean E() {
      for (KeyMapping var4 : mc.options.keyHotbarSlots) {
         if (j5.m(var4)) {
            return true;
         }
      }

      return false;
   }

   private boolean D() {
      if (this.h != null && this.D >= 0 && this.D < 9) {
         ItemStack var1 = this.h.getInventory().getItem(this.D);
         return !var1.is(Items.FISHING_ROD) && ItemStack.isSameItemSameComponents(this.j, var1);
      } else {
         return false;
      }
   }

   private boolean z(int var1) {
      if (this.h != null && this.G != null && var1 >= 0 && var1 < 9) {
         ItemStack var2 = this.h.getInventory().getItem(var1);
         return var2.is(Items.FISHING_ROD)
            && !var2.nextDamageWillBreak()
            && var2.isItemEnabled(this.G.enabledFeatures())
            && !this.h.getCooldowns().isOnCooldown(var2);
      } else {
         return false;
      }
   }

   private int A() {
      for (int var1 = 0; var1 < 9; var1++) {
         if (this.z(var1)) {
            return var1;
         }
      }

      return -1;
   }

   private boolean s(Entity var1) {
      return var1 instanceof Player var2 && j5.p(this.h, var2) && !this.h.isAlliedTo(var2) && var2.isPickable();
   }

   private static Vec3 o(Entity var0) {
      return var0.position().subtract(var0.xo, var0.yo, var0.zo);
   }

   private double t(Player var1) {
      return d_.T(this.h.getEyePosition(), var1.getBoundingBox());
   }

   private Entity j() {
      _g.u var1 = _g.G(this.h.getEyePosition(), this.h.getYRot(), this.h.getXRot());
      List var2 = this.G
         .getEntities(
            this.h,
            this.h.getBoundingBox().inflate(this.R.getValue() + 4.0),
            var1x -> var1x.isAlive() && !var1x.isSpectator() && var1x.canBeHitByProjectile() && var1x != this.h.getVehicle()
         );

      for (int var3 = 0; var3 < 20; var3++) {
         Vec3 var4 = var1.position();
         Vec3 var5 = var1.collisionEnd();
         BlockHitResult var6 = this.G.clip(new ClipContext(var4, var5, Block.COLLIDER, Fluid.ANY, this.h));
         double var7 = var6.getType() == Type.MISS ? Double.POSITIVE_INFINITY : var4.distanceToSqr(var6.getLocation());
         Entity var9 = null;

         for (Entity var11 : var2) {
            AABB var12 = var11.getBoundingBox().move(_g.j(o(var11), var3)).inflate(_g.f(var3));
            double var13 = _g.H(var4, var5, var12);
            if (var13 < var7) {
               var7 = var13;
               var9 = var11;
            }
         }

         if (var9 != null || var6.getType() != Type.MISS) {
            return var9;
         }

         var1 = var1.next();
      }

      return null;
   }

   private String m(Player var1) {
      if (!var1.isSpectator() && !var1.isCreative()) {
         if (this.h.connection.getPlayerInfo(var1.getUUID()) == null) {
            return "no player-list entry";
         } else if (this.h.isAlliedTo(var1) || !this.h.canHarmPlayer(var1)) {
            return "teammate";
         } else {
            return var1.isPickable() && var1.isAttackable() ? "friend" : "not attackable";
         }
      } else {
         return "creative or spectator";
      }
   }

   private void C(String var1) {
      long var2 = System.nanoTime();
      if (!var1.equals(this.a) && (this.a == null || var2 - this.Q >= 1000000000L)) {
         this.a = var1;
         this.Q = var2;
         r4.V("RodSwap", "Waiting: {}", var1);
      }
   }

   private String a(long var1) {
      if (this.h.getMainHandItem().is(Items.FISHING_ROD)) {
         return "Hold your weapon, not the rod";
      } else if (this.h.getOffhandItem().is(Items.FISHING_ROD)) {
         return "Move the offhand rod to the hotbar";
      } else if (this.h.fishing != null) {
         return "Existing bobber";
      } else if (this.A() < 0) {
         return "No usable rod in hotbar";
      } else if (!this.g.O(var1) || this.E()) {
         return "Manual slot selection";
      } else if (this.h.isInWater()) {
         return "In water";
      } else if (!j5.E(mc, this.h) || j5.V(mc) || j5.D(mc)) {
         return "Your hands are busy";
      } else if (!o_.N(this)) {
         return "Another module owns the hotbar";
      } else {
         return d8.V() ? "Critical hit pending" : null;
      }
   }

   private boolean d(long var1) {
      return this.isRuntimeActive()
         && this.F()
         && !this.g.U()
         && this.a(var1) == null
         && ep.W(ep.z.n)
         && !j5.n(this.h)
         && ((qa1ad45ac3ead4599)mc).larpUseDelay() <= 0;
   }

   public static Vec3 q(LivingEntity var0) {
      if (I != null && I.isEnabled()) {
         RodSwapModule var1 = I;
         return var1 == null ? null : var1.Z(var0);
      } else {
         return null;
      }
   }

   private Vec3 Z(LivingEntity var1) {
      if (this.isRuntimeActive() && !this.g.U() && var1 instanceof Player var2 && this.F()) {
         long var3 = System.nanoTime();
         String var5 = this.h(var2, var3);
         if (var5 != null) {
            this.b();
            if (!var5.isEmpty()) {
               this.C(var5);
            }

            return null;
         } else {
            _g.d var6 = this.K(var2);
            if (var6 == null) {
               this.b();
               this.C("No rod arc reaches " + var2.getName().getString());
               return null;
            }

            if (this.x != var2) {
               this.x = var2;
               this.C = 0;
            }

            if (++this.C > 20) {
               r4.V("RodSwap", "Throw at {} did not line up within {} ticks; retrying shortly", var2.getName().getString(), 20);
               this.c.C(var3, 250000000L);
               this.b();
               return null;
            } else {
               this.f = "Aiming";
               Vec3 var7 = this.h.getEyePosition();
               double var8 = Math.max(1.0, var7.distanceTo(var2.getBoundingBox().getCenter()));
               return var7.add(Vec3.directionFromRotation(var6.pitch(), var6.yaw()).scale(var8));
            }
         }
      } else {
         this.b();
         return null;
      }
   }

   private String h(Player var1, long var2) {
      if (!this.c.J(var2)) {
         this.f = "Delay";
         return "";
      }

      if (!this.s(var1)) {
         return "Kill Aura target " + var1.getName().getString() + " ignored: " + this.m(var1);
      }

      AuraModule var4 = AuraModule.G();
      if (var4 == null) {
         return "";
      }

      double var5 = this.t(var1);
      if (var5 <= var4.H() + 0.5) {
         this.f = "Inside sword reach";
         return "";
      }

      if (var5 > this.R.getValue()) {
         this.f = "Beyond Range";
         return "";
      }

      String var7 = this.a(var2);
      if (var7 != null) {
         this.f = var7;
      }

      return var7;
   }

   private _g.d K(Player var1) {
      Vec3 var2 = this.h.getEyePosition();
      Vec3 var3 = var1.getBoundingBox().getCenter();
      Vec3 var4 = o(var1);
      _g.d var5 = _g.C(var2, var3, 20);

      for (int var6 = 0; var6 < 2 && var5 != null; var6++) {
         var5 = _g.C(var2, var3.add(_g.j(var4, var5.ticks())), 20);
      }

      return var5;
   }

   private void b() {
      this.x = null;
      this.C = 0;
   }

   public void D() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            if (this.h != getMc().player || this.G != mc.level) {
               this.d("World changed", false);
               this.c.L();
               this.b();
               this.h = mc.player;
               this.G = mc.level;
            }

            if (!this.F()) {
               this.d("Paused", true);
            } else if (this.E()) {
               this.d("Manual slot selection", false);
               this.g.r(System.nanoTime());
            } else if (this.g.U()) {
               if (!this.F.k()) {
                  this.d("Slot ownership changed", false);
                  this.g.r(System.nanoTime());
               } else if (this.D()
                  && this.z(this.O)
                  && o_.N(this)
                  && j5.E(mc, this.h)
                  && !j5.D(mc)
                  && !j5.V(mc)
                  && (!mc.options.keyAttack.isDown() || mc.hitResult == null || mc.hitResult.getType() != Type.BLOCK)) {
                  if (!this.s(this.H)) {
                     this.d("Target unavailable", true);
                  } else {
                     FishingHook var1 = this.h.fishing;
                     if (var1 == null || var1.getPlayerOwner() == this.h && var1.level() == this.G && !var1.isRemoved()) {
                        if (this.s != null && var1 != null && var1 != this.s) {
                           this.d("Bobber replaced", true);
                        } else {
                           if (var1 != null && this.s == null) {
                              this.s = var1;
                              r4.g("RodSwap", "Owned bobber observed; entity={}", this.s.getId());
                           }

                           Entity var2 = var1 == null ? null : var1.getHookedIn();
                           int var3 = var1 != null
                                 && var1.tickCount > 2
                                 && var1.position().subtract(this.H.getBoundingBox().getCenter()).dot(var1.getDeltaMovement()) > 0.0
                                 && var1.distanceToSqr(this.H) > 2.25
                              ? 1
                              : 0;
                           com.larpclient.lx.x var4 = this.g
                              .s(
                                 System.nanoTime(),
                                 var1 != null,
                                 var2 == this.H,
                                 var2 != null && var2 != this.H,
                                 var1 != null && var1.onGround(),
                                 (boolean)var3
                              );
                           if (var4 == com.larpclient.lx.x.R) {
                              this.d(var2 == this.H ? "Hit" : "Missed or timed out", true);
                           } else if (var4 == com.larpclient.lx.x.J) {
                              this.f = "Player hooked";
                           }
                        }
                     } else {
                        this.d("Bobber unavailable", true);
                     }
                  }
               } else {
                  this.d("Your input takes priority", true);
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.F();
      boolean var7 = false;
      byte var6 = (byte)var10000;
      var10000 = Integer.signum(var6);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.d("Paused", true);
      } else if (this.g.U()) {
         if (this.g.P() == com.larpclient.lx.m.T) {
            d8.A(this, this::L, this::Z);
         }
      } else {
         AuraModule var1 = AuraModule.G();
         if (var1 != null && var1.isRuntimeActive()) {
            LivingEntity var3 = var1.G();
            int var17 = var3 instanceof Player;
            boolean var9 = false;
            byte var8 = (byte)var17;
            var17 = Integer.signum(var8);
            if (((var17 | -var17) >>> 31 ^ 1) == 0) {
               Player var2 = (Player)var3;
               if (this.x == var2) {
                  long var14 = System.nanoTime();
                  int var21 = this.d(var14);
                  boolean var13 = false;
                  byte var12 = (byte)var21;
                  var21 = Integer.signum(var12);
                  if ((var21 | -var21) >>> 31 == 0) {
                     String var15 = this.a(var14);
                     this.f = var15 == null ? "Aiming" : var15;
                  } else if (this.j() != var2) {
                     this.f = "Aiming";
                  } else {
                     int var5 = this.A();
                     d8.A(
                        this,
                        () -> this.a(var2, var5),
                        () -> this.d(System.nanoTime()) && this.z(var5) && this.s(var2) && this.x == var2 && var1.G() == var2 && this.j() == var2
                     );
                  }
               }
            } else {
               this.b();
               int var19 = this.f.equals("Delay");
               boolean var11 = false;
               byte var10 = (byte)var19;
               var19 = Integer.signum(var10);
               if ((var19 | -var19) >>> 31 == 0) {
                  this.f = "No player target";
               }
            }
         } else {
            this.b();
            this.f = "Enable Kill Aura";
         }
      }
   }

   private void a(Player var1, int var2) {
      this.D = this.h.getInventory().getSelectedSlot();
      this.j = this.h.getMainHandItem().copy();
      this.O = var2;
      this.b();
      if (!this.F.s(this.h, var2)) {
         this.c.C(System.nanoTime(), 250000000L);
         this.d("Hotbar busy", false);
      } else {
         this.H = var1;
         this.s = null;
         this.g.v(System.nanoTime(), this.r.getValue().longValue() * 1000000L, this.v.i("Hook and Reel"));
         boolean var3 = mc.gameMode.useItem(this.h, InteractionHand.MAIN_HAND).consumesAction();
         if (!var3) {
            this.d("Throw rejected locally", true);
         } else {
            hp.Q(this.h, InteractionHand.MAIN_HAND);
            ((qa1ad45ac3ead4599)mc).larpUseDelay(4);
            this.f = "Thrown";
            this.a = null;
            r4.V(
               "RodSwap",
               "Throw; mode={}, rodSlot={}, returnSlot={}, target={}, distance={}, yaw={}, pitch={}",
               this.v.getValue(),
               var2 + 1,
               this.D + 1,
               this.H.getName().getString(),
               String.format("%.2f", this.t(this.H)),
               String.format("%.1f", this.h.getYRot()),
               String.format("%.1f", this.h.getXRot())
            );
         }
      }
   }

   private boolean Z() {
      return this.isRuntimeActive()
         && this.F()
         && this.g.P() == com.larpclient.lx.m.T
         && this.F.k()
         && this.D()
         && this.z(this.O)
         && o_.N(this)
         && j5.E(mc, this.h)
         && !j5.D(mc)
         && !j5.V(mc)
         && !j5.n(this.h)
         && ((qa1ad45ac3ead4599)mc).larpUseDelay() <= 0
         && this.s != null
         && this.h.fishing == this.s
         && this.s.getHookedIn() == this.H
         && this.s(this.H);
   }

   private void L() {
      if (this.Z()) {
         this.g.E();
         if (!mc.gameMode.useItem(this.h, InteractionHand.MAIN_HAND).consumesAction()) {
            this.d("Reel rejected locally", true);
         } else {
            hp.Q(this.h, InteractionHand.MAIN_HAND);
            ((qa1ad45ac3ead4599)mc).larpUseDelay(4);
            this.f = "Reeled";
            r4.g("RodSwap", "Reel requested for confirmed target {}; awaiting bobber removal", this.H.getId());
         }
      }
   }

   private void d(String var1, boolean var2) {
      d8.K(this);
      int var3 = !this.g.U() && !this.F.K() ? 0 : 1;
      com.larpclient.lx.m var4 = this.g.P();
      int var5 = this.h != null && this.h == getMc().player && this.G == mc.level && this.h.isAlive() ? 1 : 0;
      int var6 = var5 != 0 ? this.h.getInventory().getSelectedSlot() : -1;
      this.F.m(getMc(), var2 && var5 != 0 && this.D());
      if (var5 != 0 && mc.gameMode != null && var6 != this.h.getInventory().getSelectedSlot()) {
         ((q143ef5d8ac4bd5b8)mc.gameMode).larpSyncCarriedItem();
      }

      if (var3 != 0) {
         this.c.X(System.nanoTime(), this.l.getValue().longValue() * 1000000L);
      }

      this.g.s();
      this.H = null;
      this.s = null;
      this.j = ItemStack.EMPTY;
      this.D = -1;
      this.O = -1;
      this.f = var1;
      if (var3 != 0) {
         r4.V("RodSwap", "Throw ended: {} at {}; weapon slot restored={}", var1, var4, var5 != 0 && var6 != this.h.getInventory().getSelectedSlot());
      }
   }
}
