package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.y8;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.gizmos.SimpleGizmoCollector;
import net.minecraft.gizmos.SimpleGizmoCollector.GizmoInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class q44a21c046c5986e4 {
   @WrapOperation(
      method = "finalizeGizmoCollection",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/gizmos/SimpleGizmoCollector;drainGizmos()Ljava/util/List;")
   )
   private List<GizmoInstance> L(SimpleGizmoCollector var1, Operation<List<GizmoInstance>> var2) {
      return y8.c((List<GizmoInstance>)var2.call(new Object[]{var1}));
   }
}
