package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.NoFallModule;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class cu {
   public static final double q = 4.0;
   private final Module x;
   private KeyMapping G;
   private boolean l;
   private boolean v;
   private int J = Integer.MIN_VALUE;
   private cu.w N = cu.w.E;
   private int u;
   private boolean p;
   private static Module I;
   private static Module R;

   public cu(Module var1) {
      this.x = var1;
   }

   public cu z(boolean var1) {
      this.p = var1;
      return this;
   }

   public boolean S(int var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      if (var2 == null) {
         return false;
      }

      cu.h var3 = e(var2, var1).rocket();
      if (var3 == null) {
         return false;
      }

      if (var3.hand() == InteractionHand.OFF_HAND || var3.slot() == var2.getInventory().getSelectedSlot()) {
         return true;
      }

      if (!hu.k(this.x)) {
         return false;
      }

      var2.getInventory().setSelectedSlot(var3.slot());
      return true;
   }

   public static void m(Module var0) {
      I = var0;
   }

   public static void k(Module var0) {
      if (I == var0) {
         I = null;
      }
   }

   public static boolean n(Module var0) {
      Module var1 = I;
      return var1 != null && var1 != var0 && var1.isRuntimeActive();
   }

   public static void w(Module var0) {
      R = var0;
   }

   public static void Z(Module var0) {
      if (R == var0) {
         R = null;
      }
   }

   public static boolean U() {
      Module var0 = R;
      return var0 != null && var0.isRuntimeActive();
   }

   public static boolean g(ItemStack var0) {
      if (var0 != null && var0.is(Items.FIREWORK_ROCKET)) {
         Fireworks var1 = (Fireworks)var0.get(DataComponents.FIREWORKS);
         return var1 == null || var1.explosions().isEmpty();
      } else {
         return false;
      }
   }

   public static int Y(ItemStack var0) {
      Fireworks var1 = var0 == null ? null : (Fireworks)var0.get(DataComponents.FIREWORKS);
      return var1 == null ? 0 : var1.flightDuration();
   }

   public static cu.t A(ItemStack var0, List<ItemStack> var1, int var2, int var3) {
      int var4 = var0 != null && var0.is(Items.FIREWORK_ROCKET) && !g(var0) ? 1 : 0;
      if (g(var0)) {
         return new cu.t(new cu.h(InteractionHand.OFF_HAND, -1, Y(var0)), false);
      }

      int var5 = -1;
      int var6 = Integer.MIN_VALUE;

      for (int var7 = 0; var7 < var1.size() && var7 < 9; var7++) {
         ItemStack var8 = (ItemStack)var1.get(var7);
         if (var8 != null && var8.is(Items.FIREWORK_ROCKET) && !g(var8)) {
            var4 = 1;
         }

         if (g(var8)) {
            int var9 = Y(var8);
            int var10 = (var3 > 0 ? (var9 == var3 ? 1000 : -Math.abs(var9 - var3)) : var9) * 10 + (var7 == var2 ? 1 : 0);
            if (var10 > var6) {
               var6 = var10;
               var5 = var7;
            }
         }
      }

      return var5 < 0 ? new cu.t(null, (boolean)var4) : new cu.t(new cu.h(InteractionHand.MAIN_HAND, var5, Y((ItemStack)var1.get(var5))), false);
   }

   public static cu.t e(LocalPlayer var0, int var1) {
      Inventory var2 = var0.getInventory();
      ArrayList var3 = new ArrayList(9);

      for (int var4 = 0; var4 < 9; var4++) {
         var3.add(var2.getItem(var4));
      }

      return A(var0.getOffhandItem(), var3, var2.getSelectedSlot(), var1);
   }

   public static int p(LocalPlayer var0) {
      int var1 = g(var0.getOffhandItem()) ? var0.getOffhandItem().getCount() : 0;

      for (int var2 = 0; var2 < 9; var2++) {
         ItemStack var3 = var0.getInventory().getItem(var2);
         if (g(var3)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   public static boolean s(LocalPlayer var0, double var1) {
      if (var1 <= 0.0) {
         return false;
      }

      Vec3 var3 = var0.getEyePosition();
      Vec3 var4 = var3.add(var0.getLookAngle().scale(var1));
      BlockHitResult var5 = var0.level().clip(new ClipContext(var3, var4, Block.COLLIDER, Fluid.NONE, var0));
      return var5.getType() == Type.BLOCK;
   }

   public boolean D() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      return var1 != null && e(var1, 0).rocket() != null;
   }

   public void m(BooleanSupplier var1, double var2, int var4, Runnable var5) {
      ta.F(this.x, () -> this.K(var2, var4, var5), var1);
   }

   public cu.w K(double var1, int var3, Runnable var4) {
      Minecraft var5 = Minecraft.getInstance();
      LocalPlayer var6 = var5.player;
      if (var6 != null && var5.gameMode != null && ta.S(this.x) && !var6.isUsingItem()) {
         cu.t var7 = e(var6, var3);
         cu.h var8 = var7.rocket();
         if (var8 == null) {
            return this.f(var7.starsOnly() ? cu.w.s : cu.w.x);
         }

         if (s(var6, var1)) {
            return this.f(cu.w.A);
         }

         Inventory var9 = var6.getInventory();
         int var10 = var9.getSelectedSlot();
         int var11 = var8.hand() == InteractionHand.MAIN_HAND && var8.slot() != var10 ? 1 : 0;
         if (var11 != 0 && !hu.k(this.x)) {
            return this.f(cu.w.D);
         }

         if (var11 != 0) {
            var9.setSelectedSlot(var8.slot());
         }

         try {
            if (var5.gameMode.useItem(var6, var8.hand()).consumesAction()) {
               ks.p(var6, var8.hand());
               this.u++;
               var4.run();
            }
         } finally {
            if (var11 != 0 && !this.p && var9.getSelectedSlot() == var8.slot()) {
               var9.setSelectedSlot(var10);
            }
         }

         if (this.N != cu.w.E) {
            com.larpclient.h.U("ElytraControl", "{}: rockets usable again", this.x.getName());
         }

         this.N = cu.w.E;
         return cu.w.E;
      } else {
         return this.f(cu.w.G);
      }
   }

   private cu.w f(cu.w var1) {
      if (var1 != this.N && var1 != cu.w.G) {
         com.larpclient.h.U("ElytraControl", "{}: no boost, {}", this.x.getName(), var1.h);
      }

      this.N = var1;
      return var1;
   }

   public cu.w C() {
      return this.N;
   }

   public int T() {
      return this.u;
   }

   public void d() {
      this.P(true);
   }

   public void Z() {
      this.P(false);
   }

   public boolean A() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      return this.v && this.l && var1 != null && var1.tickCount == this.J;
   }

   public void m() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (this.v && (var1 == null || var1.tickCount != this.J)) {
         this.y();
      }
   }

   private void P(boolean var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.G = var2.options.keyJump;
      this.G.setDown(var1);
      this.l = var1;
      this.v = (boolean)1;
      this.J = var2.player == null ? Integer.MIN_VALUE : var2.player.tickCount;
   }

   public void y() {
      if (this.v && this.G != null && this.G.isDown() == this.l) {
         this.G.setDown(c5.J(this.G));
      }

      this.v = (boolean)0;
      this.G = null;
   }

   public boolean I() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 != null && var1.tryToStartFallFlying()) {
         ServerboundPlayerCommandPacket var2 = new ServerboundPlayerCommandPacket(var1, Action.START_FALL_FLYING);
         if (!NoFallModule.F(var2)) {
            var1.connection.send(var2);
         }

         com.larpclient.h.U("ElytraControl", "{}: glide started directly", this.x.getName());
         return true;
      } else {
         return false;
      }
   }

   public void l() {
      this.y();
      this.N = cu.w.E;
      this.u = 0;
   }

   public static int j(double var0) {
      return (int)Math.ceil(Math.max(0.0, var0) / 50.0 - 1.0E-9);
   }

   public record h(InteractionHand hand, int slot, int duration) {
   }

   public record t(cu.h rocket, boolean starsOnly) {
   }

   public enum w {
      E("used"),
      x("no rocket in the offhand or hotbar"),
      s("only rockets with firework stars, which hurt when boosting"),
      A("a block is right in front of the view"),
      D("another module is using the hotbar"),
      G("the hands or the world are busy");

      public final String h;

      w(String var3) {
         this.h = var3;
      }
   }
}
