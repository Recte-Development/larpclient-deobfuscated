package com.larpclient.module;

import com.larpclient.LarpClient;
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

public final class kk {
   private LivingEntity y;
   private Integer h;

   public LivingEntity u(kk.c var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null && var2.level != null) {
         double var3 = var2.player.getDeltaMovement().length() * 2.0;
         double var5 = var1.searchRange() + Math.min(3.0, var3);
         if (var1.focus() && this.h != null) {
            if (var2.level.getEntity(this.h) instanceof LivingEntity var7
               && var7.isAlive()
               && this.n(var7, var1)
               && a(var7) <= var5
               && W(var7, var1.fov())
               && (var1.ignoreWalls() || var2.player.hasLineOfSight(var7))) {
               this.y = var7;
               return this.y;
            }

            this.h = null;
         }

         ArrayList var11 = new ArrayList();

         for (Entity var9 : var2.level.entitiesForRendering()) {
            if (var9 instanceof LivingEntity var10
               && this.n(var10, var1)
               && a(var10) <= var5
               && W(var10, var1.fov())
               && (var1.ignoreWalls() || var2.player.hasLineOfSight(var10))) {
               var11.add(var10);
            }
         }

         if (var11.isEmpty()) {
            this.C();
            return null;
         }

         var11.sort(E(var1.sort()));
         this.y = (LivingEntity)var11.getFirst();
         if (var1.focus()) {
            this.h = this.y.getId();
         }

         return this.y;
      } else {
         this.C();
         return null;
      }
   }

   public boolean n(LivingEntity var1, kk.c var2) {
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
         } else if (c(var4)) {
            return false;
         } else if (!var2.players()) {
            return false;
         } else {
            return !var2.invisibles() && var4.isInvisible() ? false : var2.naked() || !X(var4);
         }
      } else if (var1 instanceof Animal) {
         return var2.animals();
      } else {
         return var1 instanceof Mob && !(var1 instanceof ArmorStand) ? var2.mobs() : false;
      }
   }

   private static boolean c(Player var0) {
      MCFriendModule var1 = LarpClient.N().j().y(MCFriendModule.class);
      return var1 != null && var1.M(var0.getName().getString());
   }

   private static boolean X(Player var0) {
      for (EquipmentSlot var4 : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
         if (!var0.getItemBySlot(var4).isEmpty()) {
            return false;
         }
      }

      return true;
   }

   private static double a(LivingEntity var0) {
      return v.Q(var0).subtract(Minecraft.getInstance().player.getEyePosition()).length();
   }

   private static double n(LivingEntity var0) {
      LocalPlayer var1 = Minecraft.getInstance().player;
      Vec3 var2 = var1.getEyePosition();
      Vec3 var3 = AuraModule.u();
      Vec3 var4 = v.Q(var0).subtract(var2).normalize();
      return Math.acos(Mth.clamp(var3.dot(var4), -1.0, 1.0)) * (180.0 / Math.PI);
   }

   private static boolean W(LivingEntity var0, double var1) {
      return var1 >= 360.0 || n(var0) <= Math.max(0.0, var1) * 0.5;
   }

   private static Comparator<LivingEntity> E(String var0) {
      LocalPlayer var1 = Minecraft.getInstance().player;
      Comparator var2 = Comparator.comparingDouble(var1::distanceToSqr);

      return switch (var0) {
         case "Health" -> Comparator.<LivingEntity>comparingDouble(var0x -> var0x.getHealth() + var0x.getAbsorptionAmount()).thenComparing(var2);
         case "FOV" -> Comparator.<LivingEntity>comparingDouble(kk::n).thenComparing(var2);
         case "Armor" -> Comparator.comparingDouble(LivingEntity::getArmorValue).thenComparingDouble(var0x -> var0x.getHealth() + var0x.getAbsorptionAmount());
         default -> var2;
      };
   }

   public LivingEntity k() {
      return this.y;
   }

   public void C() {
      this.y = null;
      this.h = null;
   }

   public record c(
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
