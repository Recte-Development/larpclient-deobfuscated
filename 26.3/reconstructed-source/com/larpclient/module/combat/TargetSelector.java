package com.larpclient.module.combat;

import com.larpclient.LarpClient;
import com.larpclient.module.misc.CombatUtil;
import com.larpclient.module.misc.MCFriendModule;
import java.util.ArrayList;
import java.util.Comparator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public final class TargetSelector {
   private LivingEntity currentTarget;
   private Integer focusedTargetId;

   public LivingEntity findTarget(TargetSelector.TargetFilter var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.level != null) {
         double var3 = var2.player.getDeltaMovement().length() * 2.0;
         double var5 = var1.searchRange() + Math.min(3.0, var3);
         if (var1.focus() && this.focusedTargetId != null) {
            if (var2.level.getEntity(this.focusedTargetId) instanceof LivingEntity var7
               && var7.isAlive()
               && this.isValidTarget(var7, var1)
               && distanceToTarget(var7) <= var5
               && isInFov(var7, var1.fov())
               && (var1.ignoreWalls() || var2.player.hasLineOfSight(var7))) {
               this.currentTarget = var7;
               return this.currentTarget;
            }

            this.focusedTargetId = null;
         }

         ArrayList var11 = new ArrayList();

         for (Entity var9 : var2.level.entitiesForRendering()) {
            if (var9 instanceof LivingEntity var10
               && this.isValidTarget(var10, var1)
               && distanceToTarget(var10) <= var5
               && isInFov(var10, var1.fov())
               && (var1.ignoreWalls() || var2.player.hasLineOfSight(var10))) {
               var11.add(var10);
            }
         }

         if (var11.isEmpty()) {
            this.clear();
            return null;
         }

         var11.sort(comparatorFor(var1.sort()));
         this.currentTarget = (LivingEntity)var11.getFirst();
         if (var1.focus()) {
            this.focusedTargetId = this.currentTarget.getId();
         }

         return this.currentTarget;
      } else {
         this.clear();
         return null;
      }
   }

   public boolean isValidTarget(LivingEntity var1, TargetSelector.TargetFilter var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var1 == var3.player) {
         return false;
      }

      if (!var1.isAlive() || var1.getHealth() <= 0.0F || var1.isRemoved()) {
         return false;
      }

      if (var1 == var3.player.getControlledVehicle()) {
         return false;
      }

      if (var1 instanceof Player var4) {
         if (var4.isSpectator()) {
            return false;
         } else if (isFriend(var4)) {
            return false;
         } else if (!var2.players()) {
            return false;
         } else {
            return !var2.invisibles() && var4.isInvisible() ? false : var2.naked() || !isNaked(var4);
         }
      } else if (var1 instanceof Animal) {
         return var2.animals();
      } else {
         return var1 instanceof Mob && !(var1 instanceof ArmorStand) ? var2.mobs() : false;
      }
   }

   private static boolean isFriend(Player var0) {
      MCFriendModule var1 = LarpClient.getInstance().getModuleManager().getModule(MCFriendModule.class);
      return var1 != null && var1.isFriend(var0.getName().getString());
   }

   private static boolean isNaked(Player var0) {
      for (EquipmentSlot var4 : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
         if (!var0.getItemBySlot(var4).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private static double distanceToTarget(LivingEntity var0) {
      return CombatUtil.closestPointOnBox(var0).subtract(Minecraft.getInstance().player.getEyePosition()).length();
   }

   private static double isValidTarget(LivingEntity var0) {
      LocalPlayer var1 = Minecraft.getInstance().player;
      Vec3 var2 = var1.getEyePosition();
      Vec3 var3 = AuraModule.normalizeAuraMode();
      Vec3 var4 = CombatUtil.closestPointOnBox(var0).subtract(var2).normalize();
      return Math.acos(Mth.clamp(var3.dot(var4), -1.0, 1.0)) * (180.0 / Math.PI);
   }

   private static boolean isInFov(LivingEntity var0, double var1) {
      return var1 >= 360.0 || isValidTarget(var0) <= Math.max(0.0, var1) * 0.5;
   }

   private static Comparator<LivingEntity> comparatorFor(String var0) {
      LocalPlayer var1 = Minecraft.getInstance().player;
      Comparator var2 = Comparator.comparingDouble(var1::distanceToSqr);

      return switch (var0) {
         case "Health" -> Comparator.<LivingEntity>comparingDouble(var0x -> var0x.getHealth() + var0x.getAbsorptionAmount()).thenComparing(var2);
         case "FOV" -> Comparator.<LivingEntity>comparingDouble(TargetSelector::isValidTarget).thenComparing(var2);
         case "Armor" -> Comparator.comparingDouble(LivingEntity::getArmorValue).thenComparingDouble(var0x -> var0x.getHealth() + var0x.getAbsorptionAmount());
         default -> var2;
      };
   }

   public LivingEntity getTarget() {
      return this.currentTarget;
   }

   public void clear() {
      this.currentTarget = null;
      this.focusedTargetId = null;
   }

   public record TargetFilter(
      double searchRange,
      boolean players,
      boolean mobs,
      boolean animals,
      boolean invisibles,
      boolean naked,
      boolean focus,
      boolean ignoreWalls,
      String sort,
      double fov
   ) {
   }
}
