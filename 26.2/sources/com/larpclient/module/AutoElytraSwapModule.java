package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q541491ff38fb1637;
import com.larpclient.cu;
import com.larpclient.hu;
import com.larpclient.ks;
import com.larpclient.si;
import com.larpclient.ta;
import com.larpclient.wi;
import java.util.Set;
import java.util.function.ToIntFunction;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class AutoElytraSwapModule extends Module {
   static final double m = 1.2;
   static final double C = 1.5;
   private static final int u = 40;
   private static final int X = 5;
   private static final int x = 10;
   private static final int d = 5;
   private static final Item[] J = new Item[]{
      Items.NETHERITE_CHESTPLATE,
      Items.DIAMOND_CHESTPLATE,
      Items.IRON_CHESTPLATE,
      Items.COPPER_CHESTPLATE,
      Items.GOLDEN_CHESTPLATE,
      Items.CHAINMAIL_CHESTPLATE,
      Items.LEATHER_CHESTPLATE
   };
   private final e h = this.addSetting(new e("Swap Range", "How close an opponent must be, in clear view, to swap back to your chestplate", 3.0, 1.0, 6.0, 0.1));
   private final s T = this.addSetting(new s("Start Gliding", "Open the elytra right after equipping it", true));
   private final s o = this.addSetting(new s("Swap On Landing", "Put your chestplate back when you land without reaching anyone", true));
   private final s i = this.addSetting(
      new s("Hotbar Only", "Only swap pieces in your hotbar and never click inventory slots, which anticheats can flag while you move", false)
   );
   private final cu B = new cu(this);
   private final hu z = new hu(this);
   private LocalPlayer Z;
   private ClientLevel n;
   private ItemStack G;
   private Vec3 a;
   private Vec3 e = Vec3.ZERO;
   private AutoElytraSwapModule.h j = AutoElytraSwapModule.h.I;
   private boolean H;
   private long w;
   private long y = -1L;
   private long S;
   private long c;
   private long N = -1L;
   private int f = -1;
   private String g = "";

   public AutoElytraSwapModule() {
      super("Auto Elytra Swap", "Wear the elytra after a pearl catch and swap back to your chestplate when you can hit someone", Category.MOVEMENT, -1);
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.K, com.larpclient.module.m.U);
   }

   @Override
   public String getStatus() {
      return !this.isEnabled() ? "" : (this.j == AutoElytraSwapModule.h.J ? "Elytra on" : (this.j == AutoElytraSwapModule.h.U ? "Equipping" : this.g));
   }

   @Override
   public void onEnable() {
      this.S();
      this.Z = null;
      this.n = null;
      this.g = "";
      com.larpclient.h.A(
         "ElytraSwap",
         "Enabled; swap range={}, start gliding={}, swap on landing={}, hotbar only={}",
         this.h.getValue(),
         this.T.getValue(),
         this.o.getValue(),
         this.i.getValue()
      );
   }

   @Override
   public void onDisable() {
      ta.s(this);
      if (this.z.p()) {
         this.z.V(getMc(), true);
      }

      if (this.j != AutoElytraSwapModule.h.I) {
         com.larpclient.h.A("ElytraSwap", "Disabled during a swap; equipment left as worn");
      }

      this.S();
      this.Z = null;
      this.n = null;
   }

   private void S() {
      this.j = AutoElytraSwapModule.h.I;
      this.f = -1;
      this.N = this.y = -1L;
      this.a = null;
      this.e = Vec3.ZERO;
      this.H = (boolean)0;
      cu.Z(this);
      this.B.l();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         LocalPlayer var1 = mc.player;
         if (var1 != this.Z || mc.level != this.n) {
            if (this.j != AutoElytraSwapModule.h.I) {
               com.larpclient.h.A("ElytraSwap", "World changed; swap state released");
            }

            this.S();
            this.Z = var1;
            this.n = mc.level;
         }

         this.w++;
         int var10000 = cu.n(this);
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            if (this.j != AutoElytraSwapModule.h.I) {
               com.larpclient.h.A("ElytraSwap", "Elytra Pilot took the flight; swap state released");
               this.S();
            }
         } else {
            byte var2 = this.U(var1);
            this.q(var1);
            boolean var4 = false;
            byte var3 = var2;
            var10000 = Integer.signum(var3);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               long var12;
               var10000 = (var12 = this.w - this.y) == 0L ? 0 : (var12 < 0L ? -1 : 1);
               boolean var6 = false;
               int var5 = var10000;
               if (-Integer.signum(var5) >>> 31 == 0) {
                  this.y = -1L;
                  this.w(var1);
               }
            }

            if (this.j == AutoElytraSwapModule.h.U) {
               this.J();
            } else if (this.j == AutoElytraSwapModule.h.J) {
               this.d(var1);
            }
         }
      }
   }

   private boolean U(LocalPlayer var1) {
      Vec3 var2 = var1.position();
      int var3 = this.a != null && S(var2.subtract(this.a), this.e) ? 1 : 0;
      this.a = var2;
      this.e = var1.getDeltaMovement();
      return (boolean)var3;
   }

   private void q(LocalPlayer var1) {
      if (this.G == null) {
         this.G = new ItemStack(Items.ENDER_PEARL);
      }

      boolean var2 = var1.getCooldowns().isOnCooldown(this.G);
      if (var2 && !this.H) {
         this.y = Math.max(this.y, this.w + 40L);
      }

      this.H = var2;

      for (Entity var4 : mc.level.entitiesForRendering()) {
         if (var4 instanceof ThrownEnderpearl var5 && !var5.isRemoved() && var5.getOwner() == var1) {
            this.y = Math.max(this.y, this.w + 5L);
            return;
         }
      }
   }

   private void w(LocalPlayer var1) {
      if (this.j == AutoElytraSwapModule.h.I) {
         if (var1.onGround()
            || var1.isInWater()
            || var1.isInLava()
            || var1.isPassenger()
            || !mc.level.noCollision(var1, var1.getBoundingBox().expandTowards(0.0, -1.5, 0.0))) {
            com.larpclient.h.U("ElytraSwap", "Pearl teleport ended near the ground or in water; no swap");
         } else if (this.y(var1)) {
            com.larpclient.h.A("ElytraSwap", "Caught next to an opponent; keeping the chestplate on");
         } else {
            this.g = "";
            if (k(var1)) {
               this.f = -1;
               this.L();
               com.larpclient.h.A("ElytraSwap", "Caught with the elytra already on");
            } else {
               this.j = AutoElytraSwapModule.h.U;
               this.S = this.w + 10L;
               com.larpclient.h.A("ElytraSwap", "Pearl catch seen at y {}; equipping the elytra", Math.round(var1.getY()));
            }
         }
      }
   }

   private void J() {
      if (this.w > this.S) {
         this.X("Could not equip the elytra in time");
      } else {
         ta.F(this, this::U, () -> this.j == AutoElytraSwapModule.h.U && this.E());
      }
   }

   private void d(LocalPlayer var1) {
      if (!k(var1)) {
         com.larpclient.h.A("ElytraSwap", "Elytra was taken off by something else; swap state released");
         this.o();
      } else {
         boolean var2 = this.y(var1);
         int var3 = this.o.getValue() && var1.onGround() ? 1 : 0;
         if (!var2 && var3 == 0) {
            if (this.T.getValue() && this.w > this.c && this.w <= this.N && !var1.isFallFlying() && !var1.onGround() && ta.Y() && this.B.I()) {
               this.N = -1L;
            }
         } else if (this.O() < 0) {
            this.g = "No chestplate";
         } else {
            ta.F(this, () -> this.i(var2 ? "opponent in range" : "landed"), () -> this.j == AutoElytraSwapModule.h.J && this.E());
         }
      }
   }

   private void L() {
      this.j = AutoElytraSwapModule.h.J;
      this.c = this.w;
      this.N = this.w + 5L;
      cu.w(this);
   }

   private void o() {
      this.j = AutoElytraSwapModule.h.I;
      this.f = -1;
      this.N = -1L;
      cu.Z(this);
   }

   private void X(String var1) {
      this.o();
      this.g = var1;
      com.larpclient.h.A("ElytraSwap", "Elytra not equipped: {}", var1);
   }

   private void U() {
      int var1 = this.g(AutoElytraSwapModule::B);
      if (var1 < 0) {
         this.X(this.i.getValue() ? "No elytra in hotbar" : "No elytra");
      } else {
         LocalPlayer var2 = mc.player;
         int var3 = z(var2.getItemBySlot(EquipmentSlot.CHEST)) > 0 ? 1 : 0;
         if (this.s(var1) && k(var2)) {
            this.f = var3 != 0 ? var1 : -1;
            this.L();
            com.larpclient.h.A("ElytraSwap", "Elytra equipped from inventory slot {}; chestplate kept there: {}", var1, Boolean.valueOf((boolean)var3));
         }
      }
   }

   private void i(String var1) {
      int var2 = this.O();
      if (var2 >= 0) {
         LocalPlayer var3 = mc.player;
         if (this.s(var2) && !k(var3)) {
            this.o();
            com.larpclient.h.A("ElytraSwap", "Chestplate back on from inventory slot {}: {}", var2, var1);
         }
      }
   }

   private int O() {
      return this.f >= 0 && z(mc.player.getInventory().getItem(this.f)) > 0 ? this.f : this.g(AutoElytraSwapModule::z);
   }

   private int g(ToIntFunction<ItemStack> var1) {
      int var2 = this.c(var1, 0, 9);
      return var2 < 0 && !this.i.getValue() ? this.c(var1, 9, 36) : var2;
   }

   private int c(ToIntFunction<ItemStack> var1, int var2, int var3) {
      Inventory var4 = mc.player.getInventory();
      int var5 = -1;
      int var6 = 0;

      for (int var7 = var2; var7 < var3; var7++) {
         int var8 = var1.applyAsInt(var4.getItem(var7));
         if (var8 > var6) {
            var6 = var8;
            var5 = var7;
         }
      }

      return var5;
   }

   private boolean s(int var1) {
      if (var1 < 9) {
         return this.D(var1);
      }

      if (!this.Z()) {
         return false;
      }

      ElytraMotionModule.j(var1);
      return true;
   }

   private boolean D(int var1) {
      LocalPlayer var2 = mc.player;
      if (var1 != var2.getInventory().getSelectedSlot() && !this.z.O(var2, var1)) {
         return false;
      }

      try {
         boolean var3 = mc.gameMode.useItem(var2, InteractionHand.MAIN_HAND).consumesAction();
         if (var3) {
            ks.p(var2, InteractionHand.MAIN_HAND);
         }

         return var3;
      } finally {
         if (this.z.p()) {
            int var7 = var2.getInventory().getSelectedSlot();
            this.z.V(getMc(), true);
            if (var7 != var2.getInventory().getSelectedSlot()) {
               ((q541491ff38fb1637)mc.gameMode).larpSyncCarriedItem();
            }
         }
      }
   }

   private boolean E() {
      return this.isRuntimeActive() && this.isInGame() && mc.player == this.Z && ta.Y() && !mc.player.isUsingItem() && !AuraModule.N();
   }

   private boolean Z() {
      return mc.player.containerMenu == mc.player.inventoryMenu && mc.player.inventoryMenu.getCarried().isEmpty();
   }

   private static boolean k(LocalPlayer var0) {
      return var0.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
   }

   private boolean y(LocalPlayer var1) {
      LivingEntity var2 = ElytraHelperModule.R();
      if (var2 != null && this.h(var1, var2)) {
         return true;
      }

      for (AbstractClientPlayer var4 : mc.level.players()) {
         if (var4 != var1 && si.x(var1, var4) && this.h(var1, var4)) {
            return true;
         }
      }

      return false;
   }

   private boolean h(LocalPlayer var1, LivingEntity var2) {
      return s(var1.getEyePosition(), var1.getDeltaMovement(), var2.getBoundingBox(), this.h.getValue()) && var1.hasLineOfSight(var2);
   }

   static boolean S(Vec3 var0, Vec3 var1) {
      return var0.length() > 1.2 && var0.subtract(var1).length() > 1.2;
   }

   static boolean s(Vec3 var0, Vec3 var1, AABB var2, double var3) {
      return Math.min(wi.j(var0, var2), wi.j(var0.add(var1), var2)) <= var3;
   }

   static int z(ItemStack var0) {
      for (int var1 = 0; var1 < J.length; var1++) {
         if (var0.is(J[var1])) {
            return J.length - var1;
         }
      }

      return 0;
   }

   static int B(ItemStack var0) {
      return var0.is(Items.ELYTRA) && !var0.nextDamageWillBreak() ? var0.getMaxDamage() - var0.getDamageValue() : 0;
   }

   private enum h {
      I,
      U,
      J;
   }
}
