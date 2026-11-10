package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._k;
import com.larpclient._n;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.ij;
import com.larpclient.nk;
import com.larpclient.nz;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;

public final class WTapModule extends Module {
   private static WTapModule K;
   private final m q = this.addSetting(new m("Players Only", "Only release forward after attacking a player", true));
   private final m n = this.addSetting(new m("Weapons Only", "Require a held melee weapon", true));
   private final b G = this.addSetting(new b("Release Duration", "Forward release in native input ticks", 1.0, 1.0, 3.0, 1.0));
   private final nk.b L = new nk.b();
   private final nk.l A = new nk.l();
   private LocalPlayer T;
   private Object w;
   private String X = "Waiting for a sprint attack";

   public WTapModule() {
      super("W Tap", "Briefly release forward after a charged sprint attack", Category.COMBAT, -1);
      K = this;
   }

   public static void E(Player var0, Entity var1) {
      if (K != null && K.isEnabled()) {
         WTapModule var2 = K;
         if (var2 != null && var0 == getMc().player) {
            var2.A.Q();
            if (var2.isRuntimeActive() && var0 == getMc().player && var0 instanceof LocalPlayer var3) {
               int var4 = !_p.v() && mc.options.keyAttack.isDown() && mc.hitResult instanceof EntityHitResult var5 && var5.getEntity() == var1 ? 1 : 0;
               boolean var7 = var2.E(var1);
               boolean var8 = nk.k(
                  (boolean)var4,
                  var2.t(var3),
                  var3.isSprinting(),
                  mc.options.keyUp.isDown(),
                  mc.options.keyDown.isDown(),
                  mc.options.keyShift.isDown(),
                  var3.getAttackStrengthScale(0.5F),
                  var7
               );
               var2.A.d(var0, var1, var8);
               if (var8) {
                  var2.T = var3;
                  var2.w = mc.level;
               }
            }
         }
      }
   }

   public static void y(Player var0, Entity var1) {
      if (K != null && K.isEnabled()) {
         WTapModule var2 = K;
         if (var2 != null) {
            var2.A.V(var0, var1);
         }
      }
   }

   public static void r(Player var0, Entity var1) {
      if (K != null && K.isEnabled()) {
         WTapModule var2 = K;
         if (var2 != null && var0 == getMc().player) {
            if (var2.A.t(var0, var1) && var2.isRuntimeActive() && var0 == var2.T && var2.t(var2.T)) {
               var2.L.R(var2.G.getValue().intValue());
               var2.X = "Forward release queued";
               _q.c("WTap", "Executed manual sprint attack; release queued for {} input ticks", var2.G.getValue().intValue());
            }
         }
      }
   }

   public static Input W(LocalPlayer var0, Input var1) {
      if (K != null && K.isEnabled()) {
         WTapModule var2 = K;
         if (var2 == null || !var2.isRuntimeActive()) {
            return var1;
         } else if (var2.T != var0 || var2.w != getMc().level || !var2.t(var0) || !var1.forward() || var1.backward() || var1.shift()) {
            var2.f("Input or movement context changed");
            return var1;
         } else if (!var2.L.H(var0.tickCount, true)) {
            var2.X = "Waiting for a sprint attack";
            return var1;
         } else {
            var2.X = "Releasing forward";
            _k.A();
            var0.setSprinting(false);
            Input var3 = nk.S(var1);
            var0.input.moveVector = nk.K(var3);
            return var3;
         }
      } else {
         return var1;
      }
   }

   public static boolean h() {
      if (K != null && K.isEnabled()) {
         WTapModule var0 = K;
         Minecraft var1 = getMc();
         if (var0 == null || !var0.isRuntimeActive() || var1.player == null) {
            return false;
         }

         if (var0.T == var1.player && var0.w == var1.level && var0.t(var1.player)) {
            return var0.L.T(var1.player.tickCount);
         }

         var0.f("Sprint ownership changed");
         return false;
      } else {
         return false;
      }
   }

   private boolean t(LocalPlayer var1) {
      if (var1 != null
         && var1 == getMc().player
         && nz.v()
         && _p.x()
         && mc.isWindowActive()
         && ij.L() == ij.r.X
         && !var1.isUsingItem()
         && !var1.isInWater()
         && !var1.isInLava()
         && !var1.onClimbable()
         && !var1.isPassenger()
         && !var1.getAbilities().flying
         && !var1.isFallFlying()
         && !var1.isSleeping()
         && !var1.isMobilityRestricted()
         && !var1.noPhysics
         && !var1.isCrouching()
         && !var1.isShiftKeyDown()
         && _n.P() == null) {
         AirStuckModule var2 = AirStuckModule.Z();
         ElytraMotionModule var3 = ElytraMotionModule.n();
         FlyModule var4 = FlyModule.Q();
         return (var2 == null || !var2.J()) && (var3 == null || !var3.Q()) && (var4 == null || !var4.r()) && !AuraModule.M();
      } else {
         return false;
      }
   }

   private boolean E(Entity var1) {
      if (!(
         var1 instanceof LivingEntity var2
            && var1 != mc.player
            && var2.isAlive()
            && !var1.isRemoved()
            && !var1.isSpectator()
            && var1.level() == mc.level
            && !var1.isInvisible()
            && !mc.player.isAlliedTo(var1)
            && (!this.q.getValue() || var1 instanceof Player)
            && mc.player.hasLineOfSight(var1)
            && !(_p.y(var1) > mc.player.entityInteractionRange() + 1.0E-7)
      )) {
         return false;
      } else {
         if (this.n.getValue() && !a(mc.player)) {
            return false;
         }

         LarpClient var3 = LarpClient.N();
         MCFriendModule var4 = var3 != null && var3.j() != null ? var3.j().y(MCFriendModule.class) : null;
         return !(var1 instanceof Player) || var4 == null || !var4.M(var1.getName().getString());
      }
   }

   public static boolean a(Player var0) {
      ItemStack var1 = var0.getMainHandItem();
      return !var1.isEmpty() && (var1.is(ItemTags.SWORDS) || var1.is(ItemTags.AXES) || var1.is(ItemTags.MELEE_WEAPON_ENCHANTABLE));
   }

   private void f(String var1) {
      if (this.L.w()) {
         _q.c("WTap", "Release cancelled: {}; native input resumes", var1);
      }

      this.L.J();
      this.A.Q();
      this.X = "Waiting for a sprint attack";
   }

   public static void M(LocalPlayer var0) {
      WTapModule var1 = K;
      if (var1 != null && var1.T == var0) {
         var1.f("Position correction");
      }
   }

   @Override
   public void onEnable() {
      this.f("Activated");
   }

   @Override
   public void onDisable() {
      this.f("Disabled or disconnected");
      this.T = null;
      this.w = null;
   }

   public static void c() {
      WTapModule var0 = K;
      if (var0 != null) {
         if (!var0.isRuntimeActive()) {
            var0.f("Runtime unavailable");
         } else {
            if (var0.T != null && (var0.T != getMc().player || var0.w != mc.level || !var0.t(mc.player))) {
               var0.f("Context unavailable");
            }
         }
      }
   }

   @Override
   public void onTick() {
      c();
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F);
   }

   @Override
   public String getStatus() {
      return this.X;
   }
}
