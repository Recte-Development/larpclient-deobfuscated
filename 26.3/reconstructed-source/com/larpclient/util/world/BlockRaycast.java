package com.larpclient.util.world;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.shapes.CollisionContext;

public final class BlockRaycast {
   public static BlockRaycast.RayHit raycastBlock(ClientLevel var0, Vec3 var1, Vec3 var2) {
      if (var0 != null && !(var1.distanceToSqr(var2) > 64.0) && var0.hasChunkAt(BlockPos.containing(var1)) && var0.hasChunkAt(BlockPos.containing(var2))) {
         BlockHitResult var3 = var0.clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, CollisionContext.empty()));
         if (var3.getType() == Type.BLOCK && !var3.isInside()) {
            Direction var4 = var3.getDirection();
            return new BlockRaycast.RayHit(var3.getLocation(), new Vec3(var4.getStepX(), var4.getStepY(), var4.getStepZ()));
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private BlockRaycast() {
   }

   public record RayHit(Vec3 point, Vec3 normal) {
   }

   @FunctionalInterface
   public interface RayHitTester {
      BlockRaycast.RayHit test(Vec3 var1, Vec3 var2);
   }
}
