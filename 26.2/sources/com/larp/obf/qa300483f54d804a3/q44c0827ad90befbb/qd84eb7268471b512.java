package com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb;

import com.larpclient.cc;
import com.larpclient.cy;
import com.larpclient.g5;
import com.larpclient.lh;
import com.larpclient.mg;
import com.larpclient.pw;
import com.larpclient.sg;
import com.larpclient.we;
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
public class qd84eb7268471b512 {
   @Shadow
   private ItemStack lastToolHighlight;
   private ItemStack g;
   private static final Method Z = A();

   @WrapOperation(
      method = {"extractCrosshair", "extractItemHotbar"},
      at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F")
   )
   private float P(LocalPlayer var1, float var2, Operation<Float> var3) {
      return we.Y().isSwapFreezing() ? 1.0F : (Float)var3.call(new Object[]{var1, var2});
   }

   @WrapOperation(method = "extractArmor", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getArmorValue()I"))
   private static int Z(Player var0, Operation<Integer> var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var0 != var2.player) {
         return (Integer)var1.call(new Object[]{var0});
      }

      ItemStack[] var3 = cy.d.v(var2);
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
   private ItemStack D(Inventory var1, Operation<ItemStack> var2) {
      return we.Y().projectSlot(mg.j(), (ItemStack)var2.call(new Object[]{var1}));
   }

   @ModifyVariable(method = "extractSlot", at = @At("HEAD"), argsOnly = true)
   private ItemStack u(ItemStack var1, GuiGraphicsExtractor var2, int var3, int var4, DeltaTracker var5, Player var6, ItemStack var7, int var8) {
      if (!var1.isEmpty() && pw.S().C(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      if (!var1.isEmpty() && lh.I().z(var1).isEmpty()) {
         return ItemStack.EMPTY;
      }

      sg var9 = sg.G();
      if (var9 == null) {
         return var1;
      }

      int var10 = var8 >= 1 && var8 <= 9 ? var8 - 1 : -1;
      if (var6 == Minecraft.getInstance().player && var10 >= 0) {
         var10 = S(var10);
      }

      if (var6 == Minecraft.getInstance().player) {
         ItemStack var11 = we.Y().getInventorySlotProjection(var10);
         if (var11 != null) {
            return var11;
         }
      }

      ItemStack var12 = var9.b(var1);
      var12 = g5.a().y(var10, var12);
      return cc.z().u(var10, var12);
   }

   private static Method A() {
      try {
         return Class.forName("com.verz.fakeelytra.client.VisualInventory", false, Minecraft.class.getClassLoader()).getMethod("actual", int.class);
      } catch (ReflectiveOperationException | LinkageError var1) {
         return null;
      }
   }

   private static int S(int var0) {
      if (Z != null) {
         try {
            return (Integer)Z.invoke(null, var0);
         } catch (ReflectiveOperationException | LinkageError var2) {
         }
      }

      return var0;
   }

   @WrapOperation(method = "extractSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getPopTime()I"), require = 1)
   private int m(ItemStack var1, Operation<Integer> var2) {
      return we.Y().isHotbarBobAllowed(var1) ? (Integer)var2.call(new Object[]{var1}) : 0;
   }

   @WrapOperation(method = "extractItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Inventory;getSelectedSlot()I"))
   private int d(Inventory var1, Operation<Integer> var2) {
      return cc.z().v() ? cc.z().g() : mg.j();
   }

   @Inject(method = "extractSelectedItemName", at = @At("HEAD"))
   private void f(GuiGraphicsExtractor var1, CallbackInfo var2) {
      sg var3 = sg.G();
      if (var3 != null && this.lastToolHighlight != null) {
         this.g = this.lastToolHighlight;
         this.lastToolHighlight = var3.b(this.lastToolHighlight);
         Minecraft var4 = Minecraft.getInstance();
         if (var4.player != null) {
            int var5 = mg.j();
            ItemStack var6 = we.Y().getInventorySlotProjection(var5);
            if (var6 != null) {
               this.lastToolHighlight = var6;
               return;
            }

            this.lastToolHighlight = cy.Q().D(this.lastToolHighlight);
            this.lastToolHighlight = g5.a().y(var5, this.lastToolHighlight);
            this.lastToolHighlight = cc.z().u(var5, this.lastToolHighlight);
         }
      }
   }

   @Inject(method = "extractSelectedItemName", at = @At("RETURN"))
   private void h(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (this.g != null) {
         this.lastToolHighlight = this.g;
         this.g = null;
      }
   }
}
