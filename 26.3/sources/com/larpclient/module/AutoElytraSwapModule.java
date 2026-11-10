package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q9b8c1cd7fe41113a;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.h8;
import com.larpclient.jz;
import com.larpclient.qg;
import com.larpclient.s6;
import com.larpclient.sy;
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
   static final double w = 1.2;
   static final double r = 1.5;
   private static final int c = 40;
   private static final int Z = 5;
   private static final int Q = 10;
   private static final int U = 5;
   private static final Item[] J = new Item[]{
      Items.NETHERITE_CHESTPLATE,
      Items.DIAMOND_CHESTPLATE,
      Items.IRON_CHESTPLATE,
      Items.COPPER_CHESTPLATE,
      Items.GOLDEN_CHESTPLATE,
      Items.CHAINMAIL_CHESTPLATE,
      Items.LEATHER_CHESTPLATE
   };
   private final b S = this.addSetting(new b("Swap Range", "How close an opponent must be, in clear view, to swap back to your chestplate", 3.0, 1.0, 6.0, 0.1));
   private final m u = this.addSetting(new m("Start Gliding", "Open the elytra right after equipping it", true));
   private final m A = this.addSetting(new m("Swap On Landing", "Put your chestplate back when you land without reaching anyone", true));
   private final m P = this.addSetting(
      new m("Hotbar Only", "Only swap pieces in your hotbar and never click inventory slots, which anticheats can flag while you move", false)
   );
   private final sy K = new sy(this);
   private final s6 d = new s6(this);
   private LocalPlayer O;
   private ClientLevel R;
   private ItemStack l;
   private Vec3 t;
   private Vec3 W = Vec3.ZERO;
   private AutoElytraSwapModule.d h = AutoElytraSwapModule.d.q;
   private boolean C;
   private long x;
   private long L = -1L;
   private long V;
   private long g;
   private long H = -1L;
   private int v = -1;
   private String I = "";

   public AutoElytraSwapModule() {
      super("Auto Elytra Swap", "Wear the elytra after a pearl catch and swap back to your chestplate when you can hit someone", Category.MOVEMENT, -1);
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.F, com.larpclient.module.l.k);
   }

   @Override
   public String getStatus() {
      return !this.isEnabled() ? "" : (this.h == AutoElytraSwapModule.d.c ? "Elytra on" : (this.h == AutoElytraSwapModule.d.i ? "Equipping" : this.I));
   }

   @Override
   public void onEnable() {
      this.x();
      this.O = null;
      this.R = null;
      this.I = "";
      _q.z(
         "ElytraSwap",
         "Enabled; swap range={}, start gliding={}, swap on landing={}, hotbar only={}",
         this.S.getValue(),
         this.u.getValue(),
         this.A.getValue(),
         this.P.getValue()
      );
   }

   @Override
   public void onDisable() {
      _p.e(this);
      if (this.d.j()) {
         this.d.X(getMc(), true);
      }

      if (this.h != AutoElytraSwapModule.d.q) {
         _q.z("ElytraSwap", "Disabled during a swap; equipment left as worn");
      }

      this.x();
      this.O = null;
      this.R = null;
   }

   private void x() {
      this.h = AutoElytraSwapModule.d.q;
      this.v = -1;
      this.H = this.L = -1L;
      this.t = null;
      this.W = Vec3.ZERO;
      this.C = (boolean)0;
      sy.p(this);
      this.K.x();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         LocalPlayer var1 = mc.player;
         if (var1 != this.O || mc.level != this.R) {
            if (this.h != AutoElytraSwapModule.d.q) {
               _q.z("ElytraSwap", "World changed; swap state released");
            }

            this.x();
            this.O = var1;
            this.R = mc.level;
         }

         this.x++;
         int var10000 = sy.L(this);
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            if (this.h != AutoElytraSwapModule.d.q) {
               _q.z("ElytraSwap", "Elytra Pilot took the flight; swap state released");
               this.x();
            }
         } else {
            byte var2 = this.Y(var1);
            this.V(var1);
            boolean var4 = false;
            byte var3 = var2;
            var10000 = Integer.signum(var3);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               long var12;
               var10000 = (var12 = this.x - this.L) == 0L ? 0 : (var12 < 0L ? -1 : 1);
               boolean var6 = false;
               int var5 = var10000;
               if (-Integer.signum(var5) >>> 31 == 0) {
                  this.L = -1L;
                  this.p(var1);
               }
            }

            if (this.h == AutoElytraSwapModule.d.i) {
               this.r();
            } else if (this.h == AutoElytraSwapModule.d.c) {
               this.j(var1);
            }
         }
      }
   }

   private boolean Y(LocalPlayer var1) {
      Vec3 var2 = var1.position();
      int var3 = this.t != null && U(var2.subtract(this.t), this.W) ? 1 : 0;
      this.t = var2;
      this.W = var1.getDeltaMovement();
      return (boolean)var3;
   }

   private void V(LocalPlayer var1) {
      if (this.l == null) {
         this.l = new ItemStack(Items.ENDER_PEARL);
      }

      boolean var2 = var1.getCooldowns().isOnCooldown(this.l);
      if (var2 && !this.C) {
         this.L = Math.max(this.L, this.x + 40L);
      }

      this.C = var2;

      for (Entity var4 : mc.level.entitiesForRendering()) {
         if (var4 instanceof ThrownEnderpearl var5 && !var5.isRemoved() && var5.getOwner() == var1) {
            this.L = Math.max(this.L, this.x + 5L);
            return;
         }
      }
   }

   private void p(LocalPlayer var1) {
      if (this.h == AutoElytraSwapModule.d.q) {
         if (var1.onGround()
            || var1.isInWater()
            || var1.isInLava()
            || var1.isPassenger()
            || !mc.level.noCollision(var1, var1.getBoundingBox().expandTowards(0.0, -1.5, 0.0))) {
            _q.c("ElytraSwap", "Pearl teleport ended near the ground or in water; no swap");
         } else if (this.q(var1)) {
            _q.z("ElytraSwap", "Caught next to an opponent; keeping the chestplate on");
         } else {
            this.I = "";
            if (Z(var1)) {
               this.v = -1;
               this.q();
               _q.z("ElytraSwap", "Caught with the elytra already on");
            } else {
               this.h = AutoElytraSwapModule.d.i;
               this.V = this.x + 10L;
               _q.z("ElytraSwap", "Pearl catch seen at y {}; equipping the elytra", Math.round(var1.getY()));
            }
         }
      }
   }

   private void r() {
      if (this.x > this.V) {
         this.M("Could not equip the elytra in time");
      } else {
         _p.w(this, this::p, () -> this.h == AutoElytraSwapModule.d.i && this.w());
      }
   }

   private void j(LocalPlayer var1) {
      if (!Z(var1)) {
         _q.z("ElytraSwap", "Elytra was taken off by something else; swap state released");
         this.k();
      } else {
         boolean var2 = this.q(var1);
         int var3 = this.A.getValue() && var1.onGround() ? 1 : 0;
         if (!var2 && var3 == 0) {
            if (this.u.getValue() && this.x > this.g && this.x <= this.H && !var1.isFallFlying() && !var1.onGround() && _p.g() && this.K.Z()) {
               this.H = -1L;
            }
         } else if (this.h() < 0) {
            this.I = "No chestplate";
         } else {
            _p.w(this, () -> this.Y(var2 ? "opponent in range" : "landed"), () -> this.h == AutoElytraSwapModule.d.c && this.w());
         }
      }
   }

   private void q() {
      this.h = AutoElytraSwapModule.d.c;
      this.g = this.x;
      this.H = this.x + 5L;
      sy.e(this);
   }

   private void k() {
      this.h = AutoElytraSwapModule.d.q;
      this.v = -1;
      this.H = -1L;
      sy.p(this);
   }

   private void M(String var1) {
      this.k();
      this.I = var1;
      _q.z("ElytraSwap", "Elytra not equipped: {}", var1);
   }

   private void p() {
      int var1 = this.x(AutoElytraSwapModule::J);
      if (var1 < 0) {
         this.M(this.P.getValue() ? "No elytra in hotbar" : "No elytra");
      } else {
         LocalPlayer var2 = mc.player;
         int var3 = o(var2.getItemBySlot(EquipmentSlot.CHEST)) > 0 ? 1 : 0;
         if (this.m(var1) && Z(var2)) {
            this.v = var3 != 0 ? var1 : -1;
            this.q();
            _q.z("ElytraSwap", "Elytra equipped from inventory slot {}; chestplate kept there: {}", var1, Boolean.valueOf((boolean)var3));
         }
      }
   }

   private void Y(String var1) {
      int var2 = this.h();
      if (var2 >= 0) {
         LocalPlayer var3 = mc.player;
         if (this.m(var2) && !Z(var3)) {
            this.k();
            _q.z("ElytraSwap", "Chestplate back on from inventory slot {}: {}", var2, var1);
         }
      }
   }

   private int h() {
      return this.v >= 0 && o(mc.player.getInventory().getItem(this.v)) > 0 ? this.v : this.x(AutoElytraSwapModule::o);
   }

   private int x(ToIntFunction<ItemStack> var1) {
      int var2 = this.R(var1, 0, 9);
      return var2 < 0 && !this.P.getValue() ? this.R(var1, 9, 36) : var2;
   }

   private int R(ToIntFunction<ItemStack> var1, int var2, int var3) {
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

   private boolean m(int var1) {
      if (var1 < 9) {
         return this.z(var1);
      }

      if (!this.W()) {
         return false;
      }

      ElytraMotionModule.K(var1);
      return true;
   }

   private boolean z(int var1) {
      LocalPlayer var2 = mc.player;
      if (var1 != var2.getInventory().getSelectedSlot() && !this.d.h(var2, var1)) {
         return false;
      }

      try {
         boolean var3 = mc.gameMode.useItem(var2, InteractionHand.MAIN_HAND).consumesAction();
         if (var3) {
            h8.i(var2, InteractionHand.MAIN_HAND);
         }

         return var3;
      } finally {
         if (this.d.j()) {
            int var7 = var2.getInventory().getSelectedSlot();
            this.d.X(getMc(), true);
            if (var7 != var2.getInventory().getSelectedSlot()) {
               ((q9b8c1cd7fe41113a)mc.gameMode).larpSyncCarriedItem();
            }
         }
      }
   }

   private boolean w() {
      return this.isRuntimeActive() && this.isInGame() && mc.player == this.O && _p.g() && !mc.player.isUsingItem() && !AuraModule.K();
   }

   private boolean W() {
      return mc.player.containerMenu == mc.player.inventoryMenu && mc.player.inventoryMenu.getCarried().isEmpty();
   }

   private static boolean Z(LocalPlayer var0) {
      return var0.getItemBySlot(EquipmentSlot.CHEST).is(Items.ELYTRA);
   }

   private boolean q(LocalPlayer var1) {
      LivingEntity var2 = ElytraHelperModule.G();
      if (var2 != null && this.V(var1, var2)) {
         return true;
      }

      for (AbstractClientPlayer var4 : mc.level.players()) {
         if (var4 != var1 && qg.L(var1, var4) && this.V(var1, var4)) {
            return true;
         }
      }

      return false;
   }

   private boolean V(LocalPlayer var1, LivingEntity var2) {
      return v(var1.getEyePosition(), var1.getDeltaMovement(), var2.getBoundingBox(), this.S.getValue()) && var1.hasLineOfSight(var2);
   }

   static boolean U(Vec3 var0, Vec3 var1) {
      return var0.length() > 1.2 && var0.subtract(var1).length() > 1.2;
   }

   static boolean v(Vec3 var0, Vec3 var1, AABB var2, double var3) {
      return Math.min(jz.j(var0, var2), jz.j(var0.add(var1), var2)) <= var3;
   }

   static int o(ItemStack var0) {
      for (int var1 = 0; var1 < J.length; var1++) {
         if (var0.is(J[var1])) {
            return J.length - var1;
         }
      }

      return 0;
   }

   static int J(ItemStack var0) {
      return var0.is(Items.ELYTRA) && !var0.nextDamageWillBreak() ? var0.getMaxDamage() - var0.getDamageValue() : 0;
   }

   private enum d {
      q,
      i,
      c;
   }
}
