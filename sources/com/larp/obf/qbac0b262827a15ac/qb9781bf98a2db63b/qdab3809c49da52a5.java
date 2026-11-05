package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.dr;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Collection;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.blockentity.state.SpawnerRenderState;
import net.minecraft.client.renderer.extract.LevelExtractor;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LevelExtractor.class)
public abstract class qdab3809c49da52a5 {
   @Shadow
   private ClientLevel level;
   @Shadow
   @Final
   private LevelRenderer levelRenderer;

   @WrapMethod(method = "extractVisibleBlockEntities")
   private void g(Camera var1, float var2, LevelRenderState var3, Operation<Void> var4) {
      var4.call(new Object[]{var1, var2, var3});
      dr var5 = dr.z();
      if (this.level != null && var5.H()) {
         Collection var6 = var5.D(this.level);
         if (!var6.isEmpty()) {
            BlockEntityRenderer var7 = this.levelRenderer.blockEntityRenderDispatcher().getRenderer((SpawnerBlockEntity)var6.iterator().next());
            if (var7 != null) {
               Collection var8 = var5.S(this.level, var1.position(), var7.getViewDistance());
               LongOpenHashSet var9 = new LongOpenHashSet();
               var3.blockEntityRenderStates
                  .removeIf(var3x -> var5.O(var3x.blockPos) && !var7.shouldRenderOffScreen() && !var1.getCullFrustum().isVisible(new AABB(var3x.blockPos)));

               for (BlockEntityRenderState var11 : var3.blockEntityRenderStates) {
                  if (var5.O(var11.blockPos)) {
                     var9.add(var11.blockPos.asLong());
                  }
               }

               int var22 = 0;

               for (BlockEntity var12 : var8) {
                  BlockPos var13 = var12.getBlockPos();
                  if (!var12.isRemoved() && this.level.hasChunkAt(var13) && var5.O(var13)) {
                     BlockEntityRenderer var14 = this.levelRenderer.blockEntityRenderDispatcher().getRenderer(var12);
                     if (var14 != null
                        && var14.shouldRender(var12, var1.position())
                        && (var14.shouldRenderOffScreen() || var1.getCullFrustum().isVisible(new AABB(var13)))) {
                        if (var9.contains(var13.asLong())) {
                           var22++;
                        } else {
                           BlockEntityRenderState var15 = this.levelRenderer
                              .blockEntityRenderDispatcher()
                              .tryExtractRenderState(var12, var2, null, var14.shouldRenderOffScreen());
                           if (var15 != null) {
                              var3.blockEntityRenderStates.add(var15);
                              var22++;
                           }
                        }
                     }
                  }
               }

               var5.m(var8.size(), var22);

               for (BlockEntityRenderState var25 : var3.blockEntityRenderStates) {
                  if (var25 instanceof SpawnerRenderState var26 && var26.displayEntity != null && var5.O(var25.blockPos)) {
                     int var27 = LightCoordsUtil.block(var25.lightCoords);
                     int var28 = LightCoordsUtil.sky(var25.lightCoords);

                     for (Direction var19 : Direction.values()) {
                        BlockPos var20 = var25.blockPos.relative(var19);
                        if (this.level.hasChunkAt(var20)) {
                           int var21 = LightCoordsUtil.getLightCoords(this.level, var20);
                           var27 = Math.max(var27, LightCoordsUtil.block(var21));
                           var28 = Math.max(var28, LightCoordsUtil.sky(var21));
                        }
                     }

                     var25.lightCoords = LightCoordsUtil.pack(var27, var28);
                     var26.displayEntity.lightCoords = var25.lightCoords;
                  }
               }
            }
         }
      }
   }
}
