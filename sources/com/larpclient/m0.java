package com.larpclient;

import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class m0 implements po.j {
   private final Level I;
   private final Entity m;

   public m0(Level var1, Entity var2) {
      this.I = var1;
      this.m = var2;
   }

   @Override
   public double clip(Vec3 var1, Vec3 var2) {
      double var3 = var1.distanceTo(var2);
      if (var3 < 1.0E-7) {
         return 1.0;
      }

      BlockHitResult var5 = this.I.clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, this.m));
      double var6 = var5.getType() == Type.MISS ? 1.0 : var1.distanceTo(var5.getLocation()) / var3;

      for (Entity var9 : this.I
         .getEntities(this.m, new AABB(var1, var2).inflate(1.0), var0 -> var0.canBeHitByProjectile() && !(var0 instanceof Projectile) && !var0.isSpectator())) {
         Optional var10 = var9.getBoundingBox().inflate(0.3).clip(var1, var2);
         if (var10.isPresent()) {
            var6 = Math.min(var6, var1.distanceTo((Vec3)var10.get()) / var3);
         }
      }

      return var6;
   }

   @Override
   public boolean water(Vec3 var1) {
      return this.I.getFluidState(BlockPos.containing(var1)).is(FluidTags.WATER);
   }
}
