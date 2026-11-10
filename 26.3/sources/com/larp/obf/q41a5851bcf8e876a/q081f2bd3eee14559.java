package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.qc;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.gizmos.SimpleGizmoCollector;
import net.minecraft.gizmos.SimpleGizmoCollector.GizmoInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class q081f2bd3eee14559 {
   @WrapOperation(
      method = "finalizeGizmoCollection",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/gizmos/SimpleGizmoCollector;drainGizmos()Ljava/util/List;")
   )
   private List<GizmoInstance> a(SimpleGizmoCollector var1, Operation<List<GizmoInstance>> var2) {
      return qc.a((List<GizmoInstance>)var2.call(new Object[]{var1}));
   }
}
