package com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828;

import com.larpclient.a6;
import com.larpclient.hh;
import com.larpclient.q5;
import com.larpclient.qm;
import com.larpclient.qo;
import com.larpclient.sl;
import com.larpclient.vy;
import com.larpclient.wj;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.lang.reflect.Method;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Hud.class)
public class q1d0291b494611dd8 {
   @Shadow
   private ItemStack lastToolHighlight;
   private ItemStack U;
   private static final Method w = a();

   @WrapOperation(
      method = {"extractCrosshair", "extractItemHotbar"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F")
   )
   private float e(LocalPlayer var1, float var2, Operation<Float> var3) {
      return vy.a().isSwapFreezing() ? 1.0F : (Float)var3.call(new Object[]{var1, var2});
   }

   @WrapOperation(method = "extractArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getArmorValue()I"))
   private static int I(Player var0, Operation<Integer> var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 != var2.player) {
         return (Integer)var1.call(new Object[]{var0});
      }

      ItemStack[] var3 = sl.s.S(var2);
      if (var3 == null) {
         return (Integer)var1.call(new Object[]{var0});
      }

      Holder var4 = Attributes.ARMOR;
      AttributeInstance var5 = var0.getAttribute(var4);
      if (var5 == null) {
         return (Integer)var1.call(new Object[]{var0});
      }

      AttributeInstance var6 = new AttributeInstance(var4, var0x -> {});
      var6.replaceFrom(var5);
      EquipmentSlot[] var7 = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};

      for (EquipmentSlot var11 : var7) {
         var0.getItemBySlot(var11).forEachModifier(var11, (var2x, var3x) -> {
            if (var2x.equals(var4)) {
               var6.removeModifier(var3x.id());
            }
         });
      }

      for (int var12 = 0; var12 < var7.length; var12++) {
         var3[var12 + 1].forEachModifier(var7[var12], (var2x, var3x) -> {
            if (var2x.equals(var4)) {
               var6.addOrUpdateTransientModifier(var3x);
            }
         });
      }

      return Mth.floor(var6.getValue());
   }

   @WrapOperation(
      method = "tick()V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedItem()Lnet/minecraft/world/item/ItemStack;")
   )
   private ItemStack B(Inventory var1, Operation<ItemStack> var2) {
      return vy.a().projectSlot(qm.z(), (ItemStack)var2.call(new Object[]{var1}));
   }

   @ModifyVariable(method = "extractSlot", at = @At("HEAD"), argsOnly = true)
   private ItemStack K(ItemStack var1, GuiGraphicsExtractor var2, int var3, int var4, DeltaTracker var5, Player var6, ItemStack var7, int var8) {
      if (!var1.isEmpty() && wj.m().F(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      if (!var1.isEmpty() && q5.a().v(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      a6 var9 = a6.F();
      if (var9 == null) {
         return var1;
      }

      int var10 = var8 >= 1 && var8 <= 9 ? var8 - 1 : -1;
      if (var6 == Minecraft.getInstance().player && var10 >= 0) {
         var10 = W(var10);
      }

      if (var6 == Minecraft.getInstance().player) {
         ItemStack var11 = vy.a().getInventorySlotProjection(var10);
         if (var11 != null) {
            return var11;
         }
      }

      ItemStack var12 = var9.m(var1);
      var12 = qo.Z().g(var10, var12);
      return hh.R().o(var10, var12);
   }

   private static Method a() {
      try {
         return Class.forName("com.verz.fakeelytra.client.VisualInventory", false, Minecraft.class.getClassLoader()).getMethod("actual", int.class);
      } catch (ReflectiveOperationException | LinkageError var1) {
         return null;
      }
   }

   private static int W(int var0) {
      if (w != null) {
         try {
            return (Integer)w.invoke(null, var0);
         } catch (ReflectiveOperationException | LinkageError var2) {
         }
      }

      return var0;
   }

   @WrapOperation(method = "extractSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getPopTime()I"), require = 1)
   private int K(ItemStack var1, Operation<Integer> var2) {
      return vy.a().isHotbarBobAllowed(var1) ? (Integer)var2.call(new Object[]{var1}) : 0;
   }

   @WrapOperation(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int S(Inventory var1, Operation<Integer> var2) {
      return hh.R().U() ? hh.R().j() : qm.z();
   }

   @Inject(method = "extractSelectedItemName", at = @At("HEAD"))
   private void c(GuiGraphicsExtractor var1, CallbackInfo var2) {
      a6 var3 = a6.F();
      if (var3 != null && this.lastToolHighlight != null) {
         this.U = this.lastToolHighlight;
         this.lastToolHighlight = var3.m(this.lastToolHighlight);
         Minecraft var4 = Minecraft.getInstance();
         if (var4.player != null) {
            int var5 = qm.z();
            ItemStack var6 = vy.a().getInventorySlotProjection(var5);
            if (var6 != null) {
               this.lastToolHighlight = var6;
               return;
            }

            this.lastToolHighlight = sl.X().V(this.lastToolHighlight);
            this.lastToolHighlight = qo.Z().g(var5, this.lastToolHighlight);
            this.lastToolHighlight = hh.R().o(var5, this.lastToolHighlight);
         }
      }
   }

   @Inject(method = "extractSelectedItemName", at = @At("RETURN"))
   private void I(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (this.U != null) {
         this.lastToolHighlight = this.U;
         this.U = null;
      }
   }
}
