package com.larpclient;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ServerExplosion;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class zu {
   public static final double q = 6.0;

   public static float s(double var0, double var2) {
      return z(var0, var2, 6.0);
   }

   public static float z(double var0, double var2, double var4) {
      if (Double.isFinite(var0) && Double.isFinite(var2) && Double.isFinite(var4) && !(var4 <= 0.0)) {
         double var6 = var4 * 2.0;
         if (var0 > var6) {
            return 0.0F;
         }

         double var8 = (1.0 - Math.clamp(var0 / var6, 0.0, 1.0)) * Math.clamp(var2, 0.0, 1.0);
         return (float)((var8 * var8 + var8) / 2.0 * 7.0 * var6 + 1.0);
      } else {
         return 0.0F;
      }
   }

   public static boolean P(double var0, double var2, double var4) {
      return Double.isFinite(var0) && var0 <= var2 && var0 < var4;
   }

   public static float R(LivingEntity var0, Vec3 var1) {
      return z(var0, var1, 6.0);
   }

   public static float z(LivingEntity var0, Vec3 var1, double var2) {
      return z(var0, var1, var2)[0];
   }

   public static float V(LivingEntity var0, Vec3 var1, BlockPos var2) {
      return w(var0, var1, var2, Vec3.ZERO);
   }

   public static float w(LivingEntity var0, Vec3 var1, BlockPos var2, Vec3 var3) {
      return L(var0, var1, var2, var3, 6.0)[0];
   }

   public static float[] z(LivingEntity var0, Vec3 var1, double... var2) {
      return L(var0, var1, null, Vec3.ZERO, var2);
   }

   private static float[] L(LivingEntity var0, Vec3 var1, BlockPos var2, Vec3 var3, double... var4) {
      float[] var5 = new float[var4.length];
      if (var0 instanceof Player var6 && var6.getAbilities().invulnerable) {
         return var5;
      } else {
         double var15 = var0.position().add(var3).distanceTo(var1);
         double var8 = 0.0;

         for (double var13 : var4) {
            if (Double.isFinite(var13)) {
               var8 = Math.max(var8, var13 * 2.0);
            }
         }

         if (var15 > var8) {
            return var5;
         }

         float var16 = m(var1, var0, var2, var3);

         for (int var17 = 0; var17 < var4.length; var17++) {
            var5[var17] = E(var0, z(var15, var16, var4[var17]));
         }

         return var5;
      }
   }

   public static float P(Vec3 var0, Entity var1, BlockPos var2) {
      return m(var0, var1, var2, Vec3.ZERO);
   }

   public static float m(Vec3 var0, Entity var1, BlockPos var2, Vec3 var3) {
      if (var2 == null && var3.equals(Vec3.ZERO)) {
         return ServerExplosion.getSeenPercent(var0, var1);
      }

      AABB var4 = var1.getBoundingBox().move(var3);
      AABB var5 = var2 == null ? null : new AABB(var2);
      double var6 = 1.0 / ((var4.maxX - var4.minX) * 2.0 + 1.0);
      double var8 = 1.0 / ((var4.maxY - var4.minY) * 2.0 + 1.0);
      double var10 = 1.0 / ((var4.maxZ - var4.minZ) * 2.0 + 1.0);
      double var12 = (1.0 - Math.floor(1.0 / var6) * var6) / 2.0;
      double var14 = (1.0 - Math.floor(1.0 / var10) * var10) / 2.0;
      if (!(var6 < 0.0) && !(var8 < 0.0) && !(var10 < 0.0)) {
         int var16 = 0;
         int var17 = 0;

         for (double var18 = 0.0; var18 <= 1.0; var18 += var6) {
            for (double var20 = 0.0; var20 <= 1.0; var20 += var8) {
               for (double var22 = 0.0; var22 <= 1.0; var22 += var10) {
                  Vec3 var24 = new Vec3(
                     Mth.lerp(var18, var4.minX, var4.maxX) + var12, Mth.lerp(var20, var4.minY, var4.maxY), Mth.lerp(var22, var4.minZ, var4.maxZ) + var14
                  );
                  if ((var5 == null || var5.clip(var24, var0).isEmpty())
                     && var1.level().clip(new ClipContext(var24, var0, Block.COLLIDER, Fluid.NONE, var1)).getType() == Type.MISS) {
                     var16++;
                  }

                  var17++;
               }
            }
         }

         return (float)var16 / var17;
      } else {
         return 0.0F;
      }
   }

   public static float E(LivingEntity var0, float var1) {
      if (var1 <= 0.0F) {
         return 0.0F;
      }

      if (var0 instanceof Player) {
         var1 = switch (var0.level().getDifficulty()) {
            case PEACEFUL -> 0.0F;
            case EASY -> Math.min(var1 / 2.0F + 1.0F, var1);
            case NORMAL -> var1;
            case HARD -> var1 * 1.5F;
            default -> throw new MatchException(null, null);
         };
      }

      DamageSource var2 = var0.level().damageSources().explosion(null);
      var1 = CombatRules.getDamageAfterAbsorb(var0, var1, var2, var0.getArmorValue(), (float)var0.getAttributeValue(Attributes.ARMOR_TOUGHNESS));
      MobEffectInstance var3 = var0.getEffect(MobEffects.RESISTANCE);
      if (var3 != null) {
         var1 *= Math.max(0.0F, 1.0F - 0.2F * (var3.getAmplifier() + 1));
      }

      int var4 = 0;

      for (EquipmentSlot var8 : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
         ItemEnchantments var9 = (ItemEnchantments)var0.getItemBySlot(var8).get(DataComponents.ENCHANTMENTS);
         if (var9 != null) {
            for (Entry var11 : var9.entrySet()) {
               if (((Holder)var11.getKey()).is(Enchantments.PROTECTION)) {
                  var4 += var11.getIntValue();
               }

               if (((Holder)var11.getKey()).is(Enchantments.BLAST_PROTECTION)) {
                  var4 += 2 * var11.getIntValue();
               }
            }
         }
      }

      return CombatRules.getDamageAfterMagicAbsorb(var1, Math.min(20, var4));
   }

   private zu() {
   }
}
