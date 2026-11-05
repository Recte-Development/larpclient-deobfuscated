package com.larp.obf.qbac0b262827a15ac.qb9781bf98a2db63b;

import com.larpclient.dr;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientLevel.class)
public abstract class q604e85b85e3a66fc {
   @Inject(method = "setBlock", at = @At("RETURN"))
   private void V(BlockPos var1, BlockState var2, int var3, int var4, CallbackInfoReturnable<Boolean> var5) {
      dr var6 = dr.z();
      if ((Boolean)var5.getReturnValue() && var6.H()) {
         var6.d(var1, var2);
      }
   }

   @Inject(method = "syncBlockState", at = @At("HEAD"))
   private void r(BlockPos var1, BlockState var2, Vec3 var3, CallbackInfo var4) {
      dr var5 = dr.z();
      if (var5.H()) {
         var5.K(var1, var2);
      }
   }

   @Redirect(
      method = {"setBlock", "syncBlockState"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
      )
   )
   private BlockState d(ClientLevel var1, BlockPos var2) {
      if (!dr.z().H()) {
         return var1.getBlockState(var2);
      }

      if (var1.isOutsideBuildHeight(var2)) {
         return Blocks.AIR.defaultBlockState();
      }

      LevelChunk var3 = var1.getChunkAt(var2);
      return var3.getSection(var3.getSectionIndex(var2.getY())).getBlockState(var2.getX() & 15, var2.getY() & 15, var2.getZ() & 15);
   }

   @Inject(method = "setServerVerifiedBlockState", at = @At("HEAD"))
   private void a(BlockPos var1, BlockState var2, int var3, CallbackInfo var4) {
      dr var5 = dr.z();
      if (var5.H()) {
         var5.K(var1, var2);
      }
   }

   @ModifyVariable(method = "addDestroyBlockEffect", at = @At("HEAD"), argsOnly = true, ordinal = 0)
   private BlockState f(BlockState var1, BlockPos var2, BlockState var3) {
      return dr.z().f(var2, var1);
   }

   @Redirect(
      method = "addBreakingBlockEffects",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/ClientLevel;getBlockState(Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/level/block/state/BlockState;"
      )
   )
   private BlockState j(ClientLevel var1, BlockPos var2) {
      return dr.z().f(var2, var1.getBlockState(var2));
   }

   @Inject(
      method = "playSeededSound(Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/Holder;Lnet/minecraft/sounds/SoundSource;FFJ)V",
      at = @At("HEAD"),
      cancellable = true
   )
   private void Q(
      Entity var1, double var2, double var4, double var6, Holder<SoundEvent> var8, SoundSource var9, float var10, float var11, long var12, CallbackInfo var14
   ) {
      Minecraft var15 = Minecraft.getInstance();
      if (var15.isSameThread() && var9 == SoundSource.BLOCKS) {
         dr var16 = dr.z();
         dr.p var17 = var16.q(BlockPos.containing(var2, var4, var6), (SoundEvent)var8.value(), var10, var11);
         if (var17 != null) {
            var14.cancel();
            ((ClientLevel)this)
               .playSeededSound(var1, var2, var4, var6, BuiltInRegistries.SOUND_EVENT.wrapAsHolder(var17.event()), var9, var17.volume(), var17.pitch(), var12);
         }
      }
   }
}
