package com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552;

import com.larpclient.eg;
import com.larpclient.hk;
import com.larpclient.l6;
import com.larpclient.mm;
import com.larpclient.oi;
import com.larpclient.ro;
import com.larpclient.ug;
import com.larpclient.yg;
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
public class q72a85d78aa73ad03 {
   @Shadow
   private ItemStack lastToolHighlight;
   private ItemStack e;
   private static final Method Q = Y();

   @WrapOperation(
      method = {"extractCrosshair", "extractItemHotbar"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F")
   )
   private float d(LocalPlayer var1, float var2, Operation<Float> var3) {
      return ug.x().isSwapFreezing() ? 1.0F : (Float)var3.call(new Object[]{var1, var2});
   }

   @WrapOperation(method = "extractArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getArmorValue()I"))
   private static int n(Player var0, Operation<Integer> var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 != var2.player) {
         return (Integer)var1.call(new Object[]{var0});
      }

      ItemStack[] var3 = hk.T.U(var2);
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
   private ItemStack E(Inventory var1, Operation<ItemStack> var2) {
      return ug.x().projectSlot(yg.m(), (ItemStack)var2.call(new Object[]{var1}));
   }

   @ModifyVariable(method = "extractSlot", at = @At("HEAD"), argsOnly = true)
   private ItemStack H(ItemStack var1, GuiGraphicsExtractor var2, int var3, int var4, DeltaTracker var5, Player var6, ItemStack var7, int var8) {
      if (!var1.isEmpty() && ro.H().n(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      if (!var1.isEmpty() && mm.W().u(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      l6 var9 = l6.E();
      if (var9 == null) {
         return var1;
      }

      int var10 = var8 >= 1 && var8 <= 9 ? var8 - 1 : -1;
      if (var6 == Minecraft.getInstance().player && var10 >= 0) {
         var10 = R(var10);
      }

      if (var6 == Minecraft.getInstance().player) {
         ItemStack var11 = ug.x().getInventorySlotProjection(var10);
         if (var11 != null) {
            return var11;
         }
      }

      ItemStack var12 = var9.I(var1);
      var12 = oi.A().y(var10, var12);
      return eg.y().t(var10, var12);
   }

   private static Method Y() {
      try {
         return Class.forName("com.verz.fakeelytra.client.VisualInventory", false, Minecraft.class.getClassLoader()).getMethod("actual", int.class);
      } catch (ReflectiveOperationException | LinkageError var1) {
         return null;
      }
   }

   private static int R(int var0) {
      if (Q != null) {
         try {
            return (Integer)Q.invoke(null, var0);
         } catch (ReflectiveOperationException | LinkageError var2) {
         }
      }

      return var0;
   }

   @WrapOperation(method = "extractSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getPopTime()I"), require = 1)
   private int A(ItemStack var1, Operation<Integer> var2) {
      return ug.x().isHotbarBobAllowed(var1) ? (Integer)var2.call(new Object[]{var1}) : 0;
   }

   @WrapOperation(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int B(Inventory var1, Operation<Integer> var2) {
      return eg.y().G() ? eg.y().i() : yg.m();
   }

   @Inject(method = "extractSelectedItemName", at = @At("HEAD"))
   private void e(GuiGraphicsExtractor var1, CallbackInfo var2) {
      l6 var3 = l6.E();
      if (var3 != null && this.lastToolHighlight != null) {
         this.e = this.lastToolHighlight;
         this.lastToolHighlight = var3.I(this.lastToolHighlight);
         Minecraft var4 = Minecraft.getInstance();
         if (var4.player != null) {
            int var5 = yg.m();
            ItemStack var6 = ug.x().getInventorySlotProjection(var5);
            if (var6 != null) {
               this.lastToolHighlight = var6;
               return;
            }

            this.lastToolHighlight = hk.G().B(this.lastToolHighlight);
            this.lastToolHighlight = oi.A().y(var5, this.lastToolHighlight);
            this.lastToolHighlight = eg.y().t(var5, this.lastToolHighlight);
         }
      }
   }

   @Inject(method = "extractSelectedItemName", at = @At("RETURN"))
   private void T(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (this.e != null) {
         this.lastToolHighlight = this.e;
         this.e = null;
      }
   }
}
