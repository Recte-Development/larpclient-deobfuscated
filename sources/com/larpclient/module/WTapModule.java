package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.d8;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.yu;
import com.larpclient.yy;
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
   private static WTapModule U;
   private final ln H = this.addSetting(new ln("Players Only", "Only release forward after attacking a player", true));
   private final ln j = this.addSetting(new ln("Weapons Only", "Require a held melee weapon", true));
   private final lc T = this.addSetting(new lc("Release Duration", "Forward release in native input ticks", 1.0, 1.0, 3.0, 1.0));
   private final yy.n c = new yy.n();
   private final yy.r C = new yy.r();
   private LocalPlayer X;
   private Object J;
   private String o = "Waiting for a sprint attack";

   public WTapModule() {
      super("W Tap", "Briefly release forward after a charged sprint attack", Category.COMBAT, -1);
      U = this;
   }

   public static void q(Player var0, Entity var1) {
      if (U != null && U.isEnabled()) {
         WTapModule var2 = U;
         if (var2 != null && var0 == getMc().player) {
            var2.C.u();
            if (var2.isRuntimeActive() && var0 == getMc().player && var0 instanceof LocalPlayer var3) {
               int var4 = !d8.E() && mc.options.keyAttack.isDown() && mc.hitResult instanceof EntityHitResult var5 && var5.getEntity() == var1 ? 1 : 0;
               boolean var7 = var2.v(var1);
               boolean var8 = yy.B(
                  (boolean)var4,
                  var2.y(var3),
                  var3.isSprinting(),
                  mc.options.keyUp.isDown(),
                  mc.options.keyDown.isDown(),
                  mc.options.keyShift.isDown(),
                  var3.getAttackStrengthScale(0.5F),
                  var7
               );
               var2.C.C(var0, var1, var8);
               if (var8) {
                  var2.X = var3;
                  var2.J = mc.level;
               }
            }
         }
      }
   }

   public static void u(Player var0, Entity var1) {
      if (U != null && U.isEnabled()) {
         WTapModule var2 = U;
         if (var2 != null) {
            var2.C.E(var0, var1);
         }
      }
   }

   public static void G(Player var0, Entity var1) {
      if (U != null && U.isEnabled()) {
         WTapModule var2 = U;
         if (var2 != null && var0 == getMc().player) {
            if (var2.C.K(var0, var1) && var2.isRuntimeActive() && var0 == var2.X && var2.y(var2.X)) {
               var2.c.O(var2.T.getValue().intValue());
               var2.o = "Forward release queued";
               r4.g("WTap", "Executed manual sprint attack; release queued for {} input ticks", var2.T.getValue().intValue());
            }
         }
      }
   }

   public static Input A(LocalPlayer var0, Input var1) {
      if (U != null && U.isEnabled()) {
         WTapModule var2 = U;
         if (var2 == null || !var2.isRuntimeActive()) {
            return var1;
         } else if (var2.X != var0 || var2.J != getMc().level || !var2.y(var0) || !var1.forward() || var1.backward() || var1.shift()) {
            var2.h("Input or movement context changed");
            return var1;
         } else if (!var2.c.X(var0.tickCount, true)) {
            var2.o = "Waiting for a sprint attack";
            return var1;
         } else {
            var2.o = "Releasing forward";
            var0.setSprinting(false);
            Input var3 = yy.D(var1);
            var0.input.moveVector = yy.M(var3);
            return var3;
         }
      } else {
         return var1;
      }
   }

   public static boolean I() {
      if (U != null && U.isEnabled()) {
         WTapModule var0 = U;
         Minecraft var1 = getMc();
         if (var0 == null || !var0.isRuntimeActive() || var1.player == null) {
            return false;
         }

         if (var0.X == var1.player && var0.J == var1.level && var0.y(var1.player)) {
            return var0.c.p(var1.player.tickCount);
         }

         var0.h("Sprint ownership changed");
         return false;
      } else {
         return false;
      }
   }

   private boolean y(LocalPlayer var1) {
      if (var1 != null
         && var1 == getMc().player
         && yu.r()
         && d8.T()
         && mc.isWindowActive()
         && rr.u() == rr.f.i
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
         && com.larpclient.k.F() == null) {
         AirStuckModule var2 = AirStuckModule.H();
         ElytraMotionModule var3 = ElytraMotionModule.S();
         FlyModule var4 = FlyModule.s();
         return (var2 == null || !var2.D()) && (var3 == null || !var3.m()) && (var4 == null || !var4.E()) && !AuraModule.w();
      } else {
         return false;
      }
   }

   private boolean v(Entity var1) {
      if (!(
         var1 instanceof LivingEntity var2
            && var1 != mc.player
            && var2.isAlive()
            && !var1.isRemoved()
            && !var1.isSpectator()
            && var1.level() == mc.level
            && !var1.isInvisible()
            && !mc.player.isAlliedTo(var1)
            && (!this.H.getValue() || var1 instanceof Player)
            && mc.player.hasLineOfSight(var1)
            && !(d8.L(var1) > mc.player.entityInteractionRange() + 1.0E-7)
      )) {
         return false;
      } else {
         if (this.j.getValue() && !Y(mc.player)) {
            return false;
         }

         LarpClient var3 = LarpClient.y();
         MCFriendModule var4 = var3 != null && var3.e() != null ? var3.e().y(MCFriendModule.class) : null;
         return !(var1 instanceof Player) || var4 == null || !var4.e(var1.getName().getString());
      }
   }

   public static boolean Y(Player var0) {
      ItemStack var1 = var0.getMainHandItem();
      return !var1.isEmpty() && (var1.is(ItemTags.SWORDS) || var1.is(ItemTags.AXES) || var1.is(ItemTags.MELEE_WEAPON_ENCHANTABLE));
   }

   private void h(String var1) {
      if (this.c.k()) {
         r4.g("WTap", "Release cancelled: {}; native input resumes", var1);
      }

      this.c.p();
      this.C.u();
      this.o = "Waiting for a sprint attack";
   }

   public static void N(LocalPlayer var0) {
      WTapModule var1 = U;
      if (var1 != null && var1.X == var0) {
         var1.h("Position correction");
      }
   }

   @Override
   public void onEnable() {
      this.h("Activated");
   }

   @Override
   public void onDisable() {
      this.h("Disabled or disconnected");
      this.X = null;
      this.J = null;
   }

   public static void b() {
      WTapModule var0 = U;
      if (var0 != null) {
         if (!var0.isRuntimeActive()) {
            var0.h("Runtime unavailable");
         } else {
            if (var0.X != null && (var0.X != getMc().player || var0.J != mc.level || !var0.y(mc.player))) {
               var0.h("Context unavailable");
            }
         }
      }
   }

   @Override
   public void onTick() {
      b();
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a);
   }

   @Override
   public String getStatus() {
      return this.o;
   }
}
