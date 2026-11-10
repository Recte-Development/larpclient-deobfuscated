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

public final class sy {
   public static final double q = 4.0;
   private final Module m;
   private KeyMapping W;
   private boolean F;
   private boolean f;
   private int r = Integer.MIN_VALUE;
   private sy.t Y = sy.t.h;
   private int C;
   private boolean U;
   private static Module x;
   private static Module n;

   public sy(Module var1) {
      this.m = var1;
   }

   public sy H(boolean var1) {
      this.U = var1;
      return this;
   }

   public boolean h(int var1) {
      LocalPlayer var2 = Minecraft.getInstance().player;
      if (var2 == null) {
         return false;
      }

      sy.z var3 = w(var2, var1).rocket();
      if (var3 == null) {
         return false;
      }

      if (var3.hand() == InteractionHand.OFF_HAND || var3.slot() == var2.getInventory().getSelectedSlot()) {
         return true;
      }

      if (!s6.L(this.m)) {
         return false;
      }

      var2.getInventory().setSelectedSlot(var3.slot());
      return true;
   }

   public static void z(Module var0) {
      x = var0;
   }

   public static void v(Module var0) {
      if (x == var0) {
         x = null;
      }
   }

   public static boolean L(Module var0) {
      Module var1 = x;
      return var1 != null && var1 != var0 && var1.isRuntimeActive();
   }

   public static void e(Module var0) {
      n = var0;
   }

   public static void p(Module var0) {
      if (n == var0) {
         n = null;
      }
   }

   public static boolean w() {
      Module var0 = n;
      return var0 != null && var0.isRuntimeActive();
   }

   public static boolean M(ItemStack var0) {
      if (var0 != null && var0.is(Items.FIREWORK_ROCKET)) {
         Fireworks var1 = (Fireworks)var0.get(DataComponents.FIREWORKS);
         return var1 == null || var1.explosions().isEmpty();
      } else {
         return false;
      }
   }

   public static int E(ItemStack var0) {
      Fireworks var1 = var0 == null ? null : (Fireworks)var0.get(DataComponents.FIREWORKS);
      return var1 == null ? 0 : var1.flightDuration();
   }

   public static sy.v X(ItemStack var0, List<ItemStack> var1, int var2, int var3) {
      int var4 = var0 != null && var0.is(Items.FIREWORK_ROCKET) && !M(var0) ? 1 : 0;
      if (M(var0)) {
         return new sy.v(new sy.z(InteractionHand.OFF_HAND, -1, E(var0)), false);
      }

      int var5 = -1;
      int var6 = Integer.MIN_VALUE;

      for (int var7 = 0; var7 < var1.size() && var7 < 9; var7++) {
         ItemStack var8 = (ItemStack)var1.get(var7);
         if (var8 != null && var8.is(Items.FIREWORK_ROCKET) && !M(var8)) {
            var4 = 1;
         }

         if (M(var8)) {
            int var9 = E(var8);
            int var10 = (var3 > 0 ? (var9 == var3 ? 1000 : -Math.abs(var9 - var3)) : var9) * 10 + (var7 == var2 ? 1 : 0);
            if (var10 > var6) {
               var6 = var10;
               var5 = var7;
            }
         }
      }

      return var5 < 0 ? new sy.v(null, (boolean)var4) : new sy.v(new sy.z(InteractionHand.MAIN_HAND, var5, E((ItemStack)var1.get(var5))), false);
   }

   public static sy.v w(LocalPlayer var0, int var1) {
      Inventory var2 = var0.getInventory();
      ArrayList var3 = new ArrayList(9);

      for (int var4 = 0; var4 < 9; var4++) {
         var3.add(var2.getItem(var4));
      }

      return X(var0.getOffhandItem(), var3, var2.getSelectedSlot(), var1);
   }

   public static int a(LocalPlayer var0) {
      int var1 = M(var0.getOffhandItem()) ? var0.getOffhandItem().getCount() : 0;

      for (int var2 = 0; var2 < 9; var2++) {
         ItemStack var3 = var0.getInventory().getItem(var2);
         if (M(var3)) {
            var1 += var3.getCount();
         }
      }

      return var1;
   }

   public static boolean G(LocalPlayer var0, double var1) {
      if (var1 <= 0.0) {
         return false;
      }

      Vec3 var3 = var0.getEyePosition();
      Vec3 var4 = var3.add(var0.getLookAngle().scale(var1));
      BlockHitResult var5 = var0.level().clip(new ClipContext(var3, var4, Block.COLLIDER, Fluid.NONE, var0));
      return var5.getType() == Type.BLOCK;
   }

   public boolean T() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      return var1 != null && w(var1, 0).rocket() != null;
   }

   public void O(BooleanSupplier var1, double var2, int var4, Runnable var5) {
      _p.w(this.m, () -> this.H(var2, var4, var5), var1);
   }

   public sy.t H(double var1, int var3, Runnable var4) {
      Minecraft var5 = Minecraft.getInstance();
      LocalPlayer var6 = var5.player;
      if (var6 != null && var5.gameMode != null && _p.D(this.m) && !var6.isUsingItem()) {
         sy.v var7 = w(var6, var3);
         sy.z var8 = var7.rocket();
         if (var8 == null) {
            return this.h(var7.starsOnly() ? sy.t.B : sy.t.M);
         }

         if (G(var6, var1)) {
            return this.h(sy.t.q);
         }

         Inventory var9 = var6.getInventory();
         int var10 = var9.getSelectedSlot();
         int var11 = var8.hand() == InteractionHand.MAIN_HAND && var8.slot() != var10 ? 1 : 0;
         if (var11 != 0 && !s6.L(this.m)) {
            return this.h(sy.t.v);
         }

         if (var11 != 0) {
            var9.setSelectedSlot(var8.slot());
         }

         try {
            if (var5.gameMode.useItem(var6, var8.hand()).consumesAction()) {
               h8.i(var6, var8.hand());
               this.C++;
               var4.run();
            }
         } finally {
            if (var11 != 0 && !this.U && var9.getSelectedSlot() == var8.slot()) {
               var9.setSelectedSlot(var10);
            }
         }

         if (this.Y != sy.t.h) {
            _q.c("ElytraControl", "{}: rockets usable again", this.m.getName());
         }

         this.Y = sy.t.h;
         return sy.t.h;
      } else {
         return this.h(sy.t.d);
      }
   }

   private sy.t h(sy.t var1) {
      if (var1 != this.Y && var1 != sy.t.d) {
         _q.c("ElytraControl", "{}: no boost, {}", this.m.getName(), var1.g);
      }

      this.Y = var1;
      return var1;
   }

   public sy.t f() {
      return this.Y;
   }

   public int v() {
      return this.C;
   }

   public void T() {
      this.d(true);
   }

   public void d() {
      this.d(false);
   }

   public boolean H() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      return this.f && this.F && var1 != null && var1.tickCount == this.r;
   }

   public void v() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (this.f && (var1 == null || var1.tickCount != this.r)) {
         this.c();
      }
   }

   private void d(boolean var1) {
      Minecraft var2 = Minecraft.getInstance();
      this.W = var2.options.keyJump;
      this.W.setDown(var1);
      this.F = var1;
      this.f = (boolean)1;
      this.r = var2.player == null ? Integer.MIN_VALUE : var2.player.tickCount;
   }

   public void c() {
      if (this.f && this.W != null && this.W.isDown() == this.F) {
         this.W.setDown(nz.K(this.W));
      }

      this.f = (boolean)0;
      this.W = null;
   }

   public boolean Z() {
      LocalPlayer var1 = Minecraft.getInstance().player;
      if (var1 != null && var1.tryToStartFallFlying()) {
         ServerboundPlayerCommandPacket var2 = new ServerboundPlayerCommandPacket(var1, Action.START_FALL_FLYING);
         if (!NoFallModule.z(var2)) {
            var1.connection.send(var2);
         }

         _q.c("ElytraControl", "{}: glide started directly", this.m.getName());
         return true;
      } else {
         return false;
      }
   }

   public void x() {
      this.c();
      this.Y = sy.t.h;
      this.C = 0;
   }

   public static int p(double var0) {
      return (int)Math.ceil(Math.max(0.0, var0) / 50.0 - 1.0E-9);
   }

   public enum t {
      h("used"),
      M("no rocket in the offhand or hotbar"),
      B("only rockets with firework stars, which hurt when boosting"),
      q("a block is right in front of the view"),
      v("another module is using the hotbar"),
      d("the hands or the world are busy");

      public final String g;

      t(String var3) {
         this.g = var3;
      }
   }

   public record v(sy.z rocket, boolean starsOnly) {
   }

   public record z(InteractionHand hand, int slot, int duration) {
   }
}
