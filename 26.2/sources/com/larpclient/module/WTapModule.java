package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.af;
import com.larpclient.c5;
import com.larpclient.p_;
import com.larpclient.ta;
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
   private static WTapModule v;
   private final s U = this.addSetting(new s("Players Only", "Only release forward after attacking a player", true));
   private final s T = this.addSetting(new s("Weapons Only", "Require a held melee weapon", true));
   private final e x = this.addSetting(new e("Release Duration", "Forward release in native input ticks", 1.0, 1.0, 3.0, 1.0));
   private final com.larpclient.a.u O = new com.larpclient.a.u();
   private final com.larpclient.a.b S = new com.larpclient.a.b();
   private LocalPlayer l;
   private Object X;
   private String B = "Waiting for a sprint attack";

   public WTapModule() {
      super("W Tap", "Briefly release forward after a charged sprint attack", Category.COMBAT, -1);
      v = this;
   }

   public static void i(Player var0, Entity var1) {
      if (v != null && v.isEnabled()) {
         WTapModule var2 = v;
         if (var2 != null && var0 == getMc().player) {
            var2.S.a();
            if (var2.isRuntimeActive() && var0 == getMc().player && var0 instanceof LocalPlayer var3) {
               int var4 = !ta.U() && mc.options.keyAttack.isDown() && mc.hitResult instanceof EntityHitResult var5 && var5.getEntity() == var1 ? 1 : 0;
               boolean var7 = var2.N(var1);
               boolean var8 = com.larpclient.a.Z(
                  (boolean)var4,
                  var2.h(var3),
                  var3.isSprinting(),
                  mc.options.keyUp.isDown(),
                  mc.options.keyDown.isDown(),
                  mc.options.keyShift.isDown(),
                  var3.getAttackStrengthScale(0.5F),
                  var7
               );
               var2.S.F(var0, var1, var8);
               if (var8) {
                  var2.l = var3;
                  var2.X = mc.level;
               }
            }
         }
      }
   }

   public static void F(Player var0, Entity var1) {
      if (v != null && v.isEnabled()) {
         WTapModule var2 = v;
         if (var2 != null) {
            var2.S.F(var0, var1);
         }
      }
   }

   public static void v(Player var0, Entity var1) {
      if (v != null && v.isEnabled()) {
         WTapModule var2 = v;
         if (var2 != null && var0 == getMc().player) {
            if (var2.S.u(var0, var1) && var2.isRuntimeActive() && var0 == var2.l && var2.h(var2.l)) {
               var2.O.v(var2.x.getValue().intValue());
               var2.B = "Forward release queued";
               com.larpclient.h.U("WTap", "Executed manual sprint attack; release queued for {} input ticks", var2.x.getValue().intValue());
            }
         }
      }
   }

   public static Input X(LocalPlayer var0, Input var1) {
      if (v != null && v.isEnabled()) {
         WTapModule var2 = v;
         if (var2 == null || !var2.isRuntimeActive()) {
            return var1;
         } else if (var2.l != var0 || var2.X != getMc().level || !var2.h(var0) || !var1.forward() || var1.backward() || var1.shift()) {
            var2.Q("Input or movement context changed");
            return var1;
         } else if (!var2.O.l(var0.tickCount, true)) {
            var2.B = "Waiting for a sprint attack";
            return var1;
         } else {
            var2.B = "Releasing forward";
            com.larpclient.to.j();
            var0.setSprinting(false);
            Input var3 = com.larpclient.a.y(var1);
            var0.input.moveVector = com.larpclient.a.G(var3);
            return var3;
         }
      } else {
         return var1;
      }
   }

   public static boolean a() {
      if (v != null && v.isEnabled()) {
         WTapModule var0 = v;
         Minecraft var1 = getMc();
         if (var0 == null || !var0.isRuntimeActive() || var1.player == null) {
            return false;
         }

         if (var0.l == var1.player && var0.X == var1.level && var0.h(var1.player)) {
            return var0.O.X(var1.player.tickCount);
         }

         var0.Q("Sprint ownership changed");
         return false;
      } else {
         return false;
      }
   }

   private boolean h(LocalPlayer var1) {
      if (var1 != null
         && var1 == getMc().player
         && c5.J()
         && ta.H()
         && mc.isWindowActive()
         && p_.A() == p_.o.T
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
         && af.K() == null) {
         AirStuckModule var2 = AirStuckModule.T();
         ElytraMotionModule var3 = ElytraMotionModule.v();
         FlyModule var4 = FlyModule.S();
         return (var2 == null || !var2.I()) && (var3 == null || !var3.K()) && (var4 == null || !var4.f()) && !AuraModule.G();
      } else {
         return false;
      }
   }

   private boolean N(Entity var1) {
      if (!(
         var1 instanceof LivingEntity var2
            && var1 != mc.player
            && var2.isAlive()
            && !var1.isRemoved()
            && !var1.isSpectator()
            && var1.level() == mc.level
            && !var1.isInvisible()
            && !mc.player.isAlliedTo(var1)
            && (!this.U.getValue() || var1 instanceof Player)
            && mc.player.hasLineOfSight(var1)
            && !(ta.M(var1) > mc.player.entityInteractionRange() + 1.0E-7)
      )) {
         return false;
      } else {
         if (this.T.getValue() && !R(mc.player)) {
            return false;
         }

         LarpClient var3 = LarpClient.R();
         MCFriendModule var4 = var3 != null && var3.z() != null ? var3.z().p(MCFriendModule.class) : null;
         return !(var1 instanceof Player) || var4 == null || !var4.S(var1.getName().getString());
      }
   }

   public static boolean R(Player var0) {
      ItemStack var1 = var0.getMainHandItem();
      return !var1.isEmpty() && (var1.is(ItemTags.SWORDS) || var1.is(ItemTags.AXES) || var1.is(ItemTags.MELEE_WEAPON_ENCHANTABLE));
   }

   private void Q(String var1) {
      if (this.O.u()) {
         com.larpclient.h.U("WTap", "Release cancelled: {}; native input resumes", var1);
      }

      this.O.l();
      this.S.a();
      this.B = "Waiting for a sprint attack";
   }

   public static void W(LocalPlayer var0) {
      WTapModule var1 = v;
      if (var1 != null && var1.l == var0) {
         var1.Q("Position correction");
      }
   }

   @Override
   public void onEnable() {
      this.Q("Activated");
   }

   @Override
   public void onDisable() {
      this.Q("Disabled or disconnected");
      this.l = null;
      this.X = null;
   }

   public static void J() {
      WTapModule var0 = v;
      if (var0 != null) {
         if (!var0.isRuntimeActive()) {
            var0.Q("Runtime unavailable");
         } else {
            if (var0.l != null && (var0.l != getMc().player || var0.X != mc.level || !var0.h(mc.player))) {
               var0.Q("Context unavailable");
            }
         }
      }
   }

   @Override
   public void onTick() {
      J();
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.K);
   }

   @Override
   public String getStatus() {
      return this.B;
   }
}
