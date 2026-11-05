package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._m;
import com.larpclient.d_;
import com.larpclient.mt;
import com.larpclient.te;
import com.larpclient.tq;
import com.larpclient.wq;
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
   private static WTapModule A;
   private final dh i = this.addSetting(new dh("Players Only", "Only release forward after attacking a player", true));
   private final dh B = this.addSetting(new dh("Weapons Only", "Require a held melee weapon", true));
   private final d9 V = this.addSetting(new d9("Release Duration", "Forward release in native input ticks", 1.0, 1.0, 3.0, 1.0));
   private final d_.k o = new d_.k();
   private final d_.l v = new d_.l();
   private LocalPlayer I;
   private Object J;
   private String l = "Waiting for a sprint attack";

   public WTapModule() {
      super("W Tap", "Briefly release forward after a charged sprint attack", Category.COMBAT, -1);
      A = this;
   }

   public static void f(Player var0, Entity var1) {
      if (A != null && A.isEnabled()) {
         WTapModule var2 = A;
         if (var2 != null && var0 == getMc().player) {
            var2.v.X();
            if (var2.isRuntimeActive() && var0 == getMc().player && var0 instanceof LocalPlayer var3) {
               int var4 = !_m.w() && mc.options.keyAttack.isDown() && mc.hitResult instanceof EntityHitResult var5 && var5.getEntity() == var1 ? 1 : 0;
               boolean var7 = var2.Z(var1);
               boolean var8 = d_.Q(
                  (boolean)var4,
                  var2.i(var3),
                  var3.isSprinting(),
                  mc.options.keyUp.isDown(),
                  mc.options.keyDown.isDown(),
                  mc.options.keyShift.isDown(),
                  var3.getAttackStrengthScale(0.5F),
                  var7
               );
               var2.v.l(var0, var1, var8);
               if (var8) {
                  var2.I = var3;
                  var2.J = mc.level;
               }
            }
         }
      }
   }

   public static void U(Player var0, Entity var1) {
      if (A != null && A.isEnabled()) {
         WTapModule var2 = A;
         if (var2 != null) {
            var2.v.y(var0, var1);
         }
      }
   }

   public static void o(Player var0, Entity var1) {
      if (A != null && A.isEnabled()) {
         WTapModule var2 = A;
         if (var2 != null && var0 == getMc().player) {
            if (var2.v.U(var0, var1) && var2.isRuntimeActive() && var0 == var2.I && var2.i(var2.I)) {
               var2.o.l(var2.V.P().intValue());
               var2.l = "Forward release queued";
               mt.U("WTap", "Executed manual sprint attack; release queued for {} input ticks", var2.V.P().intValue());
            }
         }
      }
   }

   public static Input g(LocalPlayer var0, Input var1) {
      if (A != null && A.isEnabled()) {
         WTapModule var2 = A;
         if (var2 == null || !var2.isRuntimeActive()) {
            return var1;
         } else if (var2.I != var0 || var2.J != getMc().level || !var2.i(var0) || !var1.forward() || var1.backward() || var1.shift()) {
            var2.c("Input or movement context changed");
            return var1;
         } else if (!var2.o.b(var0.tickCount, true)) {
            var2.l = "Waiting for a sprint attack";
            return var1;
         } else {
            var2.l = "Releasing forward";
            var0.setSprinting(false);
            Input var3 = d_.r(var1);
            var0.input.moveVector = d_.Y(var3);
            return var3;
         }
      } else {
         return var1;
      }
   }

   public static boolean u() {
      if (A != null && A.isEnabled()) {
         WTapModule var0 = A;
         Minecraft var1 = getMc();
         if (var0 == null || !var0.isRuntimeActive() || var1.player == null) {
            return false;
         }

         if (var0.I == var1.player && var0.J == var1.level && var0.i(var1.player)) {
            return var0.o.L(var1.player.tickCount);
         }

         var0.c("Sprint ownership changed");
         return false;
      } else {
         return false;
      }
   }

   private boolean i(LocalPlayer var1) {
      if (var1 != null
         && var1 == getMc().player
         && te.i()
         && _m.k()
         && mc.isWindowActive()
         && tq.l() == tq.y.A
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
         && wq.g() == null) {
         AirStuckModule var2 = AirStuckModule.X();
         ElytraMotionModule var3 = ElytraMotionModule.d();
         FlyModule var4 = FlyModule.d();
         return (var2 == null || !var2.U()) && (var3 == null || !var3.z()) && (var4 == null || !var4.s()) && !AuraModule.z();
      } else {
         return false;
      }
   }

   private boolean Z(Entity var1) {
      if (!(
         var1 instanceof LivingEntity var2
            && var1 != mc.player
            && var2.isAlive()
            && !var1.isRemoved()
            && !var1.isSpectator()
            && var1.level() == mc.level
            && !var1.isInvisible()
            && !mc.player.isAlliedTo(var1)
            && (!this.i.P() || var1 instanceof Player)
            && mc.player.hasLineOfSight(var1)
            && !(_m.p(var1) > mc.player.entityInteractionRange() + 1.0E-7)
      )) {
         return false;
      } else {
         if (this.B.P() && !e(mc.player)) {
            return false;
         }

         LarpClient var3 = LarpClient.F();
         MCFriendModule var4 = var3 != null && var3.x() != null ? var3.x().V(MCFriendModule.class) : null;
         return !(var1 instanceof Player) || var4 == null || !var4.V(var1.getName().getString());
      }
   }

   public static boolean e(Player var0) {
      ItemStack var1 = var0.getMainHandItem();
      return !var1.isEmpty() && (var1.is(ItemTags.SWORDS) || var1.is(ItemTags.AXES) || var1.is(ItemTags.MELEE_WEAPON_ENCHANTABLE));
   }

   private void c(String var1) {
      if (this.o.F()) {
         mt.U("WTap", "Release cancelled: {}; native input resumes", var1);
      }

      this.o.i();
      this.v.X();
      this.l = "Waiting for a sprint attack";
   }

   public static void F(LocalPlayer var0) {
      WTapModule var1 = A;
      if (var1 != null && var1.I == var0) {
         var1.c("Position correction");
      }
   }

   @Override
   public void onEnable() {
      this.c("Activated");
   }

   @Override
   public void onDisable() {
      this.c("Disabled or disconnected");
      this.I = null;
      this.J = null;
   }

   public static void z() {
      WTapModule var0 = A;
      if (var0 != null) {
         if (!var0.isRuntimeActive()) {
            var0.c("Runtime unavailable");
         } else {
            if (var0.I != null && (var0.I != getMc().player || var0.J != mc.level || !var0.i(mc.player))) {
               var0.c("Context unavailable");
            }
         }
      }
   }

   @Override
   public void onTick() {
      z();
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z);
   }

   @Override
   public String getStatus() {
      return this.l;
   }
}
