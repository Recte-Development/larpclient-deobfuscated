package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p2;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.List;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.gizmos.SimpleGizmoCollector;
import net.minecraft.gizmos.SimpleGizmoCollector.GizmoInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class q8e0ec3af317d5cfb {
   @WrapOperation(
      method = "finalizeGizmoCollection",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/gizmos/SimpleGizmoCollector;drainGizmos()Ljava/util/List;")
   )
   private List<GizmoInstance> p(SimpleGizmoCollector var1, Operation<List<GizmoInstance>> var2) {
      return p2.L((List<GizmoInstance>)var2.call(new Object[]{var1}));
   }
}
