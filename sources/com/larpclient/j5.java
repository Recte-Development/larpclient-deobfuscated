package com.larpclient;

import com.larp.obf.q5682c6a8aca9b6ec.q19f6d37a64cd71d5;
import com.larp.obf.q5682c6a8aca9b6ec.qa1ad45ac3ead4599;
import com.larp.obf.q5682c6a8aca9b6ec.qac882a24641eea19;
import com.larpclient.module.MCFriendModule;
import com.larpclient.module.MaceModule;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class j5 {
   private static Player P;
   private static Object R;
   private static int d = -1;
   private static LivingEntity b;
   private static int S;
   private static final double J = Math.toRadians(10.0);

   public static void o(Player var0) {
      Minecraft var1 = Minecraft.getInstance();
      if (var0 == var1.player) {
         P = var0;
         R = var1.level;
         d = var0.tickCount;
      }
   }

   public static boolean n(LocalPlayer var0) {
      return var0 != null && P == var0 && R == var0.level() && d == var0.tickCount;
   }

   public static void P() {
      P = null;
      R = null;
      d = -1;
      b = null;
   }

   public static boolean A(Minecraft var0) {
      return o(var0, rr.f.i);
   }

   public static boolean o(Minecraft var0, rr.f var1) {
      rr.f var2 = rr.u();
      return d8.a()
         && var0.gui.overlay() == null
         && var0.isWindowActive()
         && var0.getCameraEntity() == var0.player
         && (var2 == rr.f.i || var2 == var1 || var2 == rr.f.E && var1 != rr.f.i);
   }

   public static boolean E(Minecraft var0, LocalPlayer var1) {
      if (!var1.isUsingItem() && !var1.isHandsBusy() && !var0.gameMode.isDestroying() && !m(var0.options.keySwapOffhand) && !m(var0.options.keyDrop)) {
         Input var2 = var1.getLastSentInput();
         return !(var1.getVehicle() instanceof AbstractBoat) || var2.forward() == var2.backward() && var2.left() == var2.right();
      } else {
         return false;
      }
   }

   public static boolean m(KeyMapping var0) {
      return ((q19f6d37a64cd71d5)var0).larpClickCount() > 0;
   }

   public static boolean V(Minecraft var0) {
      return m(var0.options.keyAttack);
   }

   public static boolean D(Minecraft var0) {
      return var0.options.keyUse.isDown() || m(var0.options.keyUse);
   }

   public static Input g(Input var0) {
      return !var0.sprint() ? var0 : new Input(var0.forward(), var0.backward(), var0.left(), var0.right(), var0.jump(), var0.shift(), false);
   }

   public static int w(Minecraft var0) {
      return ((qa1ad45ac3ead4599)var0).larpAttackDelay();
   }

   public static void i(Minecraft var0, int var1) {
      ((qa1ad45ac3ead4599)var0).larpAttackDelay(var1);
   }

   public static void s(Minecraft var0) {
      while (var0.options.keyAttack.consumeClick()) {
      }

      while (var0.options.keyUse.consumeClick()) {
      }

      qa1ad45ac3ead4599 var1 = (qa1ad45ac3ead4599)var0;
      var1.larpAttackDelay(Math.max(var1.larpAttackDelay(), 1));
      var1.larpUseDelay(Math.max(var1.larpUseDelay(), 2));
   }

   public static boolean p(LocalPlayer var0, Entity var1) {
      if (var1 instanceof LivingEntity var2
         && var1 != var0
         && !(var1 instanceof ArmorStand)
         && var1 != var0.getVehicle()
         && var2.isAlive()
         && var1.isAttackable()
         && !var1.isRemoved()
         && var1.level() == var0.level()) {
         if (var1 instanceof Player var3) {
            if (var3.isSpectator() || var3.isCreative() || !var0.canHarmPlayer(var3) || var0.connection.getPlayerInfo(var3.getUUID()) == null) {
               return false;
            }

            LarpClient var4 = LarpClient.y();
            MCFriendModule var5 = var4 != null && var4.e() != null ? var4.e().y(MCFriendModule.class) : null;
            if (var5 != null && var5.e(var3.getName().getString())) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean D(LocalPlayer var0, Vec3 var1, Vec3 var2) {
      BlockHitResult var3 = var0.level().clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, var0));
      return var3.getType() == Type.MISS || var1.distanceToSqr(var3.getLocation()) >= var1.distanceToSqr(var2) - 0.001;
   }

   public static EntityHitResult R(Minecraft var0) {
      if (var0.player != null && var0.level != null) {
         HitResult var1 = var0.player.raycastHitResult(1.0F, var0.player);
         return var1 instanceof EntityHitResult var2 && D(var0.player, var0.player.getEyePosition(), var1.getLocation()) ? var2 : null;
      } else {
         return null;
      }
   }

   public static float G(LocalPlayer var0) {
      return var0.getAttackStrengthScale(0.5F);
   }

   public static double R(ItemStack var0) {
      double[] var1 = new double[]{0.0};
      var0.forEachModifier(EquipmentSlot.MAINHAND, (var1x, var2) -> {
         if (var1x.equals(Attributes.ATTACK_DAMAGE) && var2.operation() == Operation.ADD_VALUE) {
            var1[0] += var2.amount();
         }
      });
      return var1[0];
   }

   public static boolean m(LocalPlayer var0, LivingEntity var1) {
      return G(var0) > 0.9F
         && var0.fallDistance > 0.0
         && !var0.onGround()
         && !var0.onClimbable()
         && !var0.isInWater()
         && !var0.isMobilityRestricted()
         && !var0.isPassenger()
         && !var0.isSprinting();
   }

   public static boolean X(LivingEntity var0, LocalPlayer var1) {
      if (!var0.isUsingItem()) {
         return false;
      }

      ItemStack var2 = var0.getUseItem();
      BlocksAttacks var3 = (BlocksAttacks)var2.get(DataComponents.BLOCKS_ATTACKS);
      if (var3 != null && var0.getTicksUsingItem() + H(var1) >= var3.blockDelayTicks()) {
         DamageSource var4 = var1.damageSources().playerAttack(var1);
         if (var3.bypassedBy().map(var1x -> var1x.contains(var4.typeHolder())).orElse(false)) {
            return false;
         }

         Vec3 var5 = var1.position().subtract(var0.position()).multiply(1.0, 0.0, 1.0).normalize();
         double var6 = Math.acos(Math.clamp(var5.dot(Vec3.directionFromRotation(0.0F, var0.getYHeadRot())), -1.0, 1.0));
         return var3.resolveBlockedDamage(var4, 1.0F, Math.max(0.0, var6 - J)) > 0.0F;
      } else {
         return false;
      }
   }

   public static int H(LocalPlayer var0) {
      PlayerInfo var1 = var0.connection.getPlayerInfo(var0.getUUID());
      return var1 == null ? 1 : Math.max(0, (var1.getLatency() + 49) / 50) + 1;
   }

   public static void h(LivingEntity var0) {
      LocalPlayer var1 = Minecraft.getInstance().player;
      b = var0;
      S = var0.tickCount + Math.clamp(var1 == null ? 3L : H(var1) + 2, 3, 20);
   }

   public static boolean F(LivingEntity var0) {
      return var0 == b && var0.tickCount < S;
   }

   public static int X(LocalPlayer var0) {
      int var1 = -1;
      double var2 = Double.NEGATIVE_INFINITY;

      for (int var4 = 0; var4 < Inventory.getSelectionSize(); var4++) {
         ItemStack var5 = var0.getInventory().getItem(var4);
         Weapon var6 = (Weapon)var5.get(DataComponents.WEAPON);
         if (!var5.nextDamageWillBreak() && var6 != null && !(var6.disableBlockingForSeconds() <= 0.0F)) {
            double var7 = R(var5);
            if (var7 > var2) {
               var1 = var4;
               var2 = var7;
            }
         }
      }

      return var1;
   }

   public static boolean u(Minecraft var0, EntityHitResult var1) {
      LocalPlayer var2 = var0.player;
      if (var2 != null
         && w(var0) <= 0
         && !n(var2)
         && E(var0, var2)
         && !var2.isSpectator()
         && var2.isSprinting() == ((qac882a24641eea19)var2).larpWasSprinting()) {
         ItemStack var3 = var2.getMainHandItem();
         return !var3.has(DataComponents.PIERCING_WEAPON) && var3.isItemEnabled(var0.level.enabledFeatures()) && !var2.cannotAttackWithItem(var3, 0)
            ? d8.a(MaceModule.Q(), var1.getEntity(), var2.entityInteractionRange())
            : false;
      } else {
         return false;
      }
   }

   private j5() {
   }
}
