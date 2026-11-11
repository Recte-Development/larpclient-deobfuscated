package com.larpclient.mixin;

import com.larpclient.core.StreamProofManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.gizmos.SimpleGizmoCollector;
import net.minecraft.gizmos.SimpleGizmoCollector.GizmoInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
   @WrapOperation(
      method = "finalizeGizmoCollection",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/gizmos/SimpleGizmoCollector;drainGizmos()Ljava/util/List;")
   )
   private List<GizmoInstance> a(SimpleGizmoCollector var1, Operation<List<GizmoInstance>> var2) {
      return StreamProofManager.deferGizmos((List<GizmoInstance>)var2.call(new Object[]{var1}));
   }
}
