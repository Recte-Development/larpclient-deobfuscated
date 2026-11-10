package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q9b8c1cd7fe41113a;
import com.larp.obf.q41a5851bcf8e876a.qc90ed4dd4cb15de4;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.b6;
import com.larpclient.h8;
import com.larpclient.ij;
import com.larpclient.ix;
import com.larpclient.jz;
import com.larpclient.q0;
import com.larpclient.qg;
import com.larpclient.s6;
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
   private static RodSwapModule p;
   private final o C = this.addSetting(
      new o("Mode", "Cast Only returns once the bobber hits or misses; Hook and Reel pulls a hooked player once", "Cast Only", "Cast Only", "Hook and Reel")
   );
   private final b r = this.addSetting(
      new b("Range", "Farthest player distance for a throw; Kill Aura tracks players this far while a rod is ready", 6.0, 3.0, 8.0, 0.25)
   );
   private final b i = this.addSetting(new b("Delay", "Milliseconds with your weapon back in hand before the next throw", 1000.0, 250.0, 5000.0, 50.0));
   private final b j = this.addSetting(new b("Timeout", "Longest a throw keeps the rod out before returning to your weapon", 1000.0, 250.0, 2000.0, 50.0));
   private static final int Q = 20;
   private static final long D = 250000000L;
   private static final int R = 20;
   private static final double I = 0.5;
   private final s6 d = new s6(this);
   private final ix K = new ix();
   private final ix.r V = new ix.r();
   private String w;
   private long F;
   private LocalPlayer G;
   private ClientLevel Z;
   private Player t;
   private Player J;
   private FishingHook b;
   private ItemStack x = ItemStack.EMPTY;
   private int T = -1;
   private int h = -1;
   private int u;
   private String H = "Enable Kill Aura";

   public RodSwapModule() {
      super("Rod Swap", "Kill Aura throws a hotbar rod at its player target on a delay, then returns to its weapon", Category.COMBAT, -1);
      p = this;
      this.C.w(var1 -> this.H("Mode changed", true));
   }

   @Override
   public String getDisplayName() {
      return "Auto Rod";
   }

   public List<r<?>> Y() {
      return List.of(this.C);
   }

   public List<r<?>> t() {
      return List.of(this.r, this.i, this.j);
   }

   public static void q() {
      if (p != null && p.isRuntimeActive()) {
         p.H("Manual slot selection", false);
         p.K.Z(System.nanoTime());
      }
   }

   public static boolean m() {
      return p != null && p.isEnabled() ? p != null && p.isRuntimeActive() && p.K.Q() && p.d.l() : false;
   }

   public static void G() {
      if (m()) {
         p.H("Your attack", true);
      }
   }

   public static double u() {
      RodSwapModule var0 = p;
      return var0 != null && var0.isRuntimeActive() && var0.G != null && var0.h() >= 0 ? var0.r.getValue() : 0.0;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F, l.k);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.H : "";
   }

   @Override
   public void onEnable() {
      this.G = getMc().player;
      this.Z = mc.level;
      this.K.Q();
      this.V.c();
      this.C();
      this.H = "Ready";
      this.w = null;
      _q.z(
         "RodSwap",
         "Auto Rod enabled; mode={}, range={}, delay={}ms, timeout={}ms",
         this.C.getValue(),
         this.r.getValue(),
         this.i.getValue().longValue(),
         this.j.getValue().longValue()
      );
   }

   @Override
   public void onDisable() {
      this.H("Disabled or disconnected", true);
      this.C();
      this.G = null;
      this.Z = null;
   }

   private boolean d() {
      Minecraft var1 = getMc();
      return this.G != null
         && this.G == var1.player
         && this.Z != null
         && this.Z == var1.level
         && (qg.U(var1) || qg.j(var1, ij.r.N))
         && !this.G.isSpectator()
         && this.G.isAlive();
   }

   private boolean t() {
      for (KeyMapping var4 : mc.options.keyHotbarSlots) {
         if (qg.f(var4)) {
            return true;
         }
      }

      return false;
   }

   private boolean M() {
      if (this.G != null && this.T >= 0 && this.T < 9) {
         ItemStack var1 = this.G.getInventory().getItem(this.T);
         return !var1.is(Items.FISHING_ROD) && ItemStack.isSameItemSameComponents(this.x, var1);
      } else {
         return false;
      }
   }

   private boolean B(int var1) {
      if (this.G != null && this.Z != null && var1 >= 0 && var1 < 9) {
         ItemStack var2 = this.G.getInventory().getItem(var1);
         return var2.is(Items.FISHING_ROD)
            && !var2.nextDamageWillBreak()
            && var2.isItemEnabled(this.Z.enabledFeatures())
            && !this.G.getCooldowns().isOnCooldown(var2);
      } else {
         return false;
      }
   }

   private int h() {
      for (int var1 = 0; var1 < 9; var1++) {
         if (this.B(var1)) {
            return var1;
         }
      }

      return -1;
   }

   private boolean w(Entity var1) {
      return var1 instanceof Player var2 && qg.L(this.G, var2) && !this.G.isAlliedTo(var2) && var2.isPickable();
   }

   private static Vec3 j(Entity var0) {
      return var0.position().subtract(var0.xo, var0.yo, var0.zo);
   }

   private double F(Player var1) {
      return jz.j(this.G.getEyePosition(), var1.getBoundingBox());
   }

   private Entity J() {
      q0.c var1 = q0.r(this.G.getEyePosition(), this.G.getYRot(), this.G.getXRot());
      List var2 = this.Z
         .getEntities(
            this.G,
            this.G.getBoundingBox().inflate(this.r.getValue() + 4.0),
            var1x -> var1x.isAlive() && !var1x.isSpectator() && var1x.canBeHitByProjectile() && var1x != this.G.getVehicle()
         );

      for (int var3 = 0; var3 < 20; var3++) {
         Vec3 var4 = var1.position();
         Vec3 var5 = var1.collisionEnd();
         BlockHitResult var6 = this.Z.clip(new ClipContext(var4, var5, Block.COLLIDER, Fluid.ANY, this.G));
         double var7 = var6.getType() == Type.MISS ? Double.POSITIVE_INFINITY : var4.distanceToSqr(var6.getLocation());
         Entity var9 = null;

         for (Entity var11 : var2) {
            AABB var12 = var11.getBoundingBox().move(q0.Y(j(var11), var3)).inflate(q0.x(var3));
            double var13 = q0.b(var4, var5, var12);
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

   private String k(Player var1) {
      if (!var1.isSpectator() && !var1.isCreative()) {
         if (this.G.connection.getPlayerInfo(var1.getUUID()) == null) {
            return "no player-list entry";
         } else if (this.G.isAlliedTo(var1) || !this.G.canHarmPlayer(var1)) {
            return "teammate";
         } else {
            return var1.isPickable() && var1.isAttackable() ? "friend" : "not attackable";
         }
      } else {
         return "creative or spectator";
      }
   }

   private void A(String var1) {
      long var2 = System.nanoTime();
      if (!var1.equals(this.w) && (this.w == null || var2 - this.F >= 1000000000L)) {
         this.w = var1;
         this.F = var2;
         _q.z("RodSwap", "Waiting: {}", var1);
      }
   }

   private String R(long var1) {
      if (this.G.getMainHandItem().is(Items.FISHING_ROD)) {
         return "Hold your weapon, not the rod";
      } else if (this.G.getOffhandItem().is(Items.FISHING_ROD)) {
         return "Move the offhand rod to the hotbar";
      } else if (this.G.fishing != null) {
         return "Existing bobber";
      } else if (this.h() < 0) {
         return "No usable rod in hotbar";
      } else if (!this.K.y(var1) || this.t()) {
         return "Manual slot selection";
      } else if (this.G.isInWater()) {
         return "In water";
      } else if (!qg.g(mc, this.G) || qg.t(mc) || qg.O(mc)) {
         return "Your hands are busy";
      } else if (!s6.L(this)) {
         return "Another module owns the hotbar";
      } else {
         return _p.k() ? "Critical hit pending" : null;
      }
   }

   private boolean Q(long var1) {
      return this.isRuntimeActive()
         && this.d()
         && !this.K.Q()
         && this.R(var1) == null
         && b6.c(b6.w.P)
         && !qg.Y(this.G)
         && ((qc90ed4dd4cb15de4)mc).larpUseDelay() <= 0;
   }

   public static Vec3 s(LivingEntity var0) {
      if (p != null && p.isEnabled()) {
         RodSwapModule var1 = p;
         return var1 == null ? null : var1.r(var0);
      } else {
         return null;
      }
   }

   private Vec3 r(LivingEntity var1) {
      if (this.isRuntimeActive() && !this.K.Q() && var1 instanceof Player var2 && this.d()) {
         long var3 = System.nanoTime();
         String var5 = this.J(var2, var3);
         if (var5 != null) {
            this.C();
            if (!var5.isEmpty()) {
               this.A(var5);
            }

            return null;
         } else {
            q0.f var6 = this.d(var2);
            if (var6 == null) {
               this.C();
               this.A("No rod arc reaches " + var2.getName().getString());
               return null;
            }

            if (this.J != var2) {
               this.J = var2;
               this.u = 0;
            }

            if (++this.u > 20) {
               _q.z("RodSwap", "Throw at {} did not line up within {} ticks; retrying shortly", var2.getName().getString(), 20);
               this.V.V(var3, 250000000L);
               this.C();
               return null;
            } else {
               this.H = "Aiming";
               Vec3 var7 = this.G.getEyePosition();
               double var8 = Math.max(1.0, var7.distanceTo(var2.getBoundingBox().getCenter()));
               return var7.add(Vec3.directionFromRotation(var6.pitch(), var6.yaw()).scale(var8));
            }
         }
      } else {
         this.C();
         return null;
      }
   }

   private String J(Player var1, long var2) {
      if (!this.V.V(var2)) {
         this.H = "Delay";
         return "";
      }

      if (!this.w(var1)) {
         return "Kill Aura target " + var1.getName().getString() + " ignored: " + this.k(var1);
      }

      AuraModule var4 = AuraModule.t();
      if (var4 == null) {
         return "";
      }

      double var5 = this.F(var1);
      if (var5 <= var4.j() + 0.5) {
         this.H = "Inside sword reach";
         return "";
      }

      if (var5 > this.r.getValue()) {
         this.H = "Beyond Range";
         return "";
      }

      String var7 = this.R(var2);
      if (var7 != null) {
         this.H = var7;
      }

      return var7;
   }

   private q0.f d(Player var1) {
      Vec3 var2 = this.G.getEyePosition();
      Vec3 var3 = var1.getBoundingBox().getCenter();
      Vec3 var4 = j(var1);
      q0.f var5 = q0.B(var2, var3, 20);

      for (int var6 = 0; var6 < 2 && var5 != null; var6++) {
         var5 = q0.B(var2, var3.add(q0.Y(var4, var5.ticks())), 20);
      }

      return var5;
   }

   private void C() {
      this.J = null;
      this.u = 0;
   }

   public void N() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            if (this.G != getMc().player || this.Z != mc.level) {
               this.H("World changed", false);
               this.V.c();
               this.C();
               this.G = mc.player;
               this.Z = mc.level;
            }

            if (!this.d()) {
               this.H("Paused", true);
            } else if (this.t()) {
               this.H("Manual slot selection", false);
               this.K.Z(System.nanoTime());
            } else if (this.K.Q()) {
               if (!this.d.l()) {
                  this.H("Slot ownership changed", false);
                  this.K.Z(System.nanoTime());
               } else if (this.M()
                  && this.B(this.h)
                  && s6.L(this)
                  && qg.g(mc, this.G)
                  && !qg.O(mc)
                  && !qg.t(mc)
                  && (!mc.options.keyAttack.isDown() || mc.hitResult == null || mc.hitResult.getType() != Type.BLOCK)) {
                  if (!this.w(this.t)) {
                     this.H("Target unavailable", true);
                  } else {
                     FishingHook var1 = this.G.fishing;
                     if (var1 == null || var1.getPlayerOwner() == this.G && var1.level() == this.Z && !var1.isRemoved()) {
                        if (this.b != null && var1 != null && var1 != this.b) {
                           this.H("Bobber replaced", true);
                        } else {
                           if (var1 != null && this.b == null) {
                              this.b = var1;
                              _q.c("RodSwap", "Owned bobber observed; entity={}", this.b.getId());
                           }

                           Entity var2 = var1 == null ? null : var1.getHookedIn();
                           int var3 = var1 != null
                                 && var1.tickCount > 2
                                 && var1.position().subtract(this.t.getBoundingBox().getCenter()).dot(var1.getDeltaMovement()) > 0.0
                                 && var1.distanceToSqr(this.t) > 2.25
                              ? 1
                              : 0;
                           ix.k var4 = this.K
                              .a(
                                 System.nanoTime(),
                                 var1 != null,
                                 var2 == this.t,
                                 var2 != null && var2 != this.t,
                                 var1 != null && var1.onGround(),
                                 (boolean)var3
                              );
                           if (var4 == ix.k.u) {
                              this.H(var2 == this.t ? "Hit" : "Missed or timed out", true);
                           } else if (var4 == ix.k.d) {
                              this.H = "Player hooked";
                           }
                        }
                     } else {
                        this.H("Bobber unavailable", true);
                     }
                  }
               } else {
                  this.H("Your input takes priority", true);
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.d();
      boolean var7 = false;
      byte var6 = (byte)var10000;
      var10000 = Integer.signum(var6);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.H("Paused", true);
      } else {
         int var17 = this.K.Q();
         boolean var11 = false;
         byte var10 = (byte)var17;
         var17 = Integer.signum(var10);
         if (((var17 | -var17) >>> 31 ^ 1) == 0) {
            if (this.K.N() == ix.u.S) {
               _p.w(this, this::H, this::a);
            }
         } else {
            AuraModule var1 = AuraModule.t();
            if (var1 != null && var1.isRuntimeActive()) {
               LivingEntity var3 = var1.R();
               int var19 = var3 instanceof Player;
               boolean var9 = false;
               byte var8 = (byte)var19;
               var19 = Integer.signum(var8);
               if (((var19 | -var19) >>> 31 ^ 1) == 0) {
                  Player var2 = (Player)var3;
                  if (this.J == var2) {
                     long var14 = System.nanoTime();
                     int var21 = this.Q(var14);
                     boolean var13 = false;
                     byte var12 = (byte)var21;
                     var21 = Integer.signum(var12);
                     if ((var21 | -var21) >>> 31 == 0) {
                        String var15 = this.R(var14);
                        this.H = var15 == null ? "Aiming" : var15;
                     } else if (this.J() != var2) {
                        this.H = "Aiming";
                     } else {
                        int var5 = this.h();
                        _p.w(
                           this,
                           () -> this.B(var2, var5),
                           () -> this.Q(System.nanoTime()) && this.B(var5) && this.w(var2) && this.J == var2 && var1.R() == var2 && this.J() == var2
                        );
                     }
                  }
               } else {
                  this.C();
                  if (!this.H.equals("Delay")) {
                     this.H = "No player target";
                  }
               }
            } else {
               this.C();
               this.H = "Enable Kill Aura";
            }
         }
      }
   }

   private void B(Player var1, int var2) {
      this.T = this.G.getInventory().getSelectedSlot();
      this.x = this.G.getMainHandItem().copy();
      this.h = var2;
      this.C();
      if (!this.d.h(this.G, var2)) {
         this.V.V(System.nanoTime(), 250000000L);
         this.H("Hotbar busy", false);
      } else {
         this.t = var1;
         this.b = null;
         this.K.Z(System.nanoTime(), this.j.getValue().longValue() * 1000000L, this.C.w("Hook and Reel"));
         boolean var3 = mc.gameMode.useItem(this.G, InteractionHand.MAIN_HAND).consumesAction();
         if (!var3) {
            this.H("Throw rejected locally", true);
         } else {
            h8.i(this.G, InteractionHand.MAIN_HAND);
            ((qc90ed4dd4cb15de4)mc).larpUseDelay(4);
            this.H = "Thrown";
            this.w = null;
            _q.z(
               "RodSwap",
               "Throw; mode={}, rodSlot={}, returnSlot={}, target={}, distance={}, yaw={}, pitch={}",
               this.C.getValue(),
               var2 + 1,
               this.T + 1,
               this.t.getName().getString(),
               String.format("%.2f", this.F(this.t)),
               String.format("%.1f", this.G.getYRot()),
               String.format("%.1f", this.G.getXRot())
            );
         }
      }
   }

   private boolean a() {
      return this.isRuntimeActive()
         && this.d()
         && this.K.N() == ix.u.S
         && this.d.l()
         && this.M()
         && this.B(this.h)
         && s6.L(this)
         && qg.g(mc, this.G)
         && !qg.O(mc)
         && !qg.t(mc)
         && !qg.Y(this.G)
         && ((qc90ed4dd4cb15de4)mc).larpUseDelay() <= 0
         && this.b != null
         && this.G.fishing == this.b
         && this.b.getHookedIn() == this.t
         && this.w(this.t);
   }

   private void H() {
      if (this.a()) {
         this.K.u();
         if (!mc.gameMode.useItem(this.G, InteractionHand.MAIN_HAND).consumesAction()) {
            this.H("Reel rejected locally", true);
         } else {
            h8.i(this.G, InteractionHand.MAIN_HAND);
            ((qc90ed4dd4cb15de4)mc).larpUseDelay(4);
            this.H = "Reeled";
            _q.c("RodSwap", "Reel requested for confirmed target {}; awaiting bobber removal", this.t.getId());
         }
      }
   }

   private void H(String var1, boolean var2) {
      _p.e(this);
      int var3 = !this.K.Q() && !this.d.j() ? 0 : 1;
      ix.u var4 = this.K.N();
      int var5 = this.G != null && this.G == getMc().player && this.Z == mc.level && this.G.isAlive() ? 1 : 0;
      int var6 = var5 != 0 ? this.G.getInventory().getSelectedSlot() : -1;
      this.d.X(getMc(), var2 && var5 != 0 && this.M());
      if (var5 != 0 && mc.gameMode != null && var6 != this.G.getInventory().getSelectedSlot()) {
         ((q9b8c1cd7fe41113a)mc.gameMode).larpSyncCarriedItem();
      }

      if (var3 != 0) {
         this.V.e(System.nanoTime(), this.i.getValue().longValue() * 1000000L);
      }

      this.K.Q();
      this.t = null;
      this.b = null;
      this.x = ItemStack.EMPTY;
      this.T = -1;
      this.h = -1;
      this.H = var1;
      if (var3 != 0) {
         _q.z("RodSwap", "Throw ended: {} at {}; weapon slot restored={}", var1, var4, var5 != 0 && var6 != this.G.getInventory().getSelectedSlot());
      }
   }
}
