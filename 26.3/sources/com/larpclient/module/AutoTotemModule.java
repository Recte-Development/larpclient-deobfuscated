package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q71cffb54eb30bf94;
import com.larp.obf.q41a5851bcf8e876a.qee5f93289f869c83;
import com.larpclient._q;
import com.larpclient.ij;
import com.larpclient.n1;
import com.larpclient.qg;
import com.larpclient.qx;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class AutoTotemModule extends Module {
   private static AutoTotemModule g;
   private Object F;
   private Object M;
   private long U = Long.MIN_VALUE;
   private final o E = this.addSetting(new o("Mode", "When to protect your offhand", "Health", "Health", "Always"));
   private final b J = this.addSetting(new b("Health", "Equip at or below this health; an empty offhand is always protected", 10.0, 1.0, 20.0, 0.5));
   private final m W = this.addSetting(new m("Include Absorption", "Count absorption hearts when comparing health", false));
   private final o C = this.addSetting(new o("Swap Mode", "Route from inventory to offhand", "Direct", "Direct", "Hotbar"));
   private final o L = this.addSetting(
      new o("Swap Style", "Legit stops and clicks like a player in the inventory; Instant clicks at once and gets flagged", "Legit", "Legit", "Instant")
   );
   private final b u = this.addSetting(new b("Click Delay", "Shortest pause in milliseconds before each legit inventory step", 100.0, 0.0, 1000.0, 10.0));
   private final b c = this.addSetting(new b("Click Delay Max", "Longest pause in milliseconds before each legit inventory step", 200.0, 0.0, 1000.0, 10.0));
   private final m b = this.addSetting(new m("Offhand Key", "Equip hotbar totems with the swap hands key, which works while moving", true));
   private final m Z = this.addSetting(new m("Open Inventory", "Show the inventory during legit clicks, which also stops camera turns", false));
   private final b A = this.addSetting(new b("Delay", "Milliseconds before equipping", 0.0, 0.0, 1000.0, 10.0));
   private final b h = this.addSetting(new b("Random Delay", "Maximum additional milliseconds, sampled once per equip", 0.0, 0.0, 1000.0, 10.0));
   private final m Y = this.addSetting(new m("Hotbar Refill", "Keep spare totems in empty hotbar slots without replacing items", false));
   private final b v = this.addSetting(new b("Hotbar Reserves", "Desired number of hotbar slots holding spare totems", 1.0, 1.0, 9.0, 1.0));
   private final m H = this.addSetting(new m("In Inventory", "Allow swaps in the survival inventory when the cursor is empty", true));
   private final qx.r R = new qx.r();
   private final qx.q p = new qx.q();
   private final qx.j I = new qx.j();
   private LocalPlayer x;
   private Object e;
   private Screen t;

   public AutoTotemModule() {
      super("AutoTotem", "Automatically equip offhand totems", Category.COMBAT, -1);
      g = this;
      this.J.a(() -> this.E.w("Health"));
      this.W.a(() -> this.E.w("Health"));
      this.v.a(this.Y::getValue);
      this.u.R(this.c).B("Click Delay");
      this.u.a(() -> this.L.w("Legit"));
      this.c.a(() -> this.L.w("Legit"));
      this.b.a(() -> this.L.w("Legit"));
      this.Z.a(() -> this.L.w("Legit"));
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   @Override
   public void onEnable() {
      this.R.a();
      this.F = this.M = null;
      this.o();
   }

   @Override
   public void onDisable() {
      this.X("module disabled", true);
      this.R.a();
      this.F = this.M = null;
   }

   public static boolean S(LocalPlayer var0) {
      if (g != null && g.isEnabled()) {
         AutoTotemModule var1 = g;
         return var1 != null && var0 != null && var1.p.k() && var1.x == var0 && var1.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean k() {
      return x(false);
   }

   public static boolean U() {
      return x(true);
   }

   private static boolean x(boolean var0) {
      AutoTotemModule var1 = g;
      if (var1 != null && var1.isRuntimeActive()) {
         Minecraft var2 = getMc();
         LocalPlayer var3 = var2.player;
         if (var3 == null || var1.x != var3 || !var1.I.h() && (!var1.p.k() || var0)) {
            if (var3 != null
               && var2.level != null
               && var3.isAlive()
               && !var3.isSpectator()
               && !ij.y()
               && var3.containerMenu == var3.inventoryMenu
               && var3.inventoryMenu.getCarried().isEmpty()
               && var1.m(var2.gui.screen())) {
               if (var0 && !var1.C.w("Hotbar")) {
                  return false;
               }

               if (var1.F == var3 && var1.M == var2.level && Util.getMillis() - var1.U < 100L) {
                  return true;
               }

               boolean var4 = qx.a(
                  var3.getOffhandItem().is(Items.TOTEM_OF_UNDYING),
                  var3.getOffhandItem().isEmpty(),
                  var1.E.w("Always"),
                  var3.getHealth(),
                  var3.getAbsorptionAmount(),
                  var1.W.getValue(),
                  var1.J.getValue()
               );
               long var5 = 0L;
               int var7 = 0;

               for (int var8 = 0; var8 < 36; var8++) {
                  ItemStack var9 = var3.getInventory().getItem(var8);
                  if (var9.is(Items.TOTEM_OF_UNDYING)) {
                     var5 |= 1L << var8;
                  } else if (var8 < 9 && var9.isEmpty()) {
                     var7 |= 1 << var8;
                  }
               }

               return qx.o(var5, var7, var4, var1.C.w("Hotbar"), var1.Y.getValue(), var1.v.getValue().intValue()) != null;
            } else {
               return false;
            }
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   @Override
   public void onTick() {
      if (this.x != null && (this.x != mc.player || this.e != mc.level)) {
         _q.c("AutoTotem", "Player or world changed during a swap; state dropped without packets");
         this.o();
      }

      if (this.isInGame() && mc.gameMode != null) {
         int var10000 = mc.player.isAlive();
         boolean var7 = false;
         byte var6 = (byte)var10000;
         var10000 = Integer.signum(var6);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0
            && !mc.player.isSpectator()
            && !ij.y()
            && mc.player.containerMenu == mc.player.inventoryMenu
            && mc.player.inventoryMenu.getCarried().isEmpty()) {
            int var16 = this.m(mc.gui.screen());
            boolean var9 = false;
            byte var8 = (byte)var16;
            var16 = Integer.signum(var8);
            if ((var16 | -var16) >>> 31 != 0) {
               long var1 = Util.getMillis();
               int var18 = this.I.h();
               boolean var11 = false;
               byte var10 = (byte)var18;
               var18 = Integer.signum(var10);
               if (((var18 | -var18) >>> 31 ^ 1) == 0) {
                  this.w(var1);
                  return;
               }

               if (this.p.k()) {
                  this.m(var1);
                  return;
               }

               if (this.R.s()) {
                  if (mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
                     this.R.E();
                     _q.c("AutoTotem", "Server confirmed the swap hands press");
                  } else {
                     if (this.R.b(var1)) {
                        return;
                     }

                     this.R.E();
                     _q.R("AutoTotem", "No totem {} ms after swap hands; the server dropped it or is lagging, retrying", 750L);
                  }
               }

               byte var3 = this.I();
               if (var3 == 0) {
                  this.R.s();
               }

               qx.w var4 = this.J((boolean)var3);
               if (var4 == null) {
                  this.R.s();
                  return;
               }

               boolean var13 = false;
               byte var12 = var3;
               var18 = Integer.signum(var12);
               if (((var18 | -var18) >>> 31 ^ 1) == 0 && this.L.w("Legit") && this.b.getValue() && this.G()) {
                  var4 = qx.t(var4, this.F(), !this.m(), mc.player.isUsingItem());
               }

               if (var3 != 0) {
                  int var5 = !this.R.m() && this.h.getValue() > 0.0 ? ThreadLocalRandom.current().nextInt(this.h.getValue().intValue() + 1) : 0;
                  if (!this.R.b(var1, this.A.getValue().intValue() + var5)) {
                     return;
                  }
               } else if (!this.R.M(var1)) {
                  return;
               }

               if (!this.e(var4)) {
                  this.R.s();
                  return;
               }

               if (this.L.w("Legit")) {
                  this.q(var1, var4, (boolean)var3);
                  return;
               }

               InventoryMenu var14 = mc.player.inventoryMenu;
               mc.gameMode.handleContainerInput(var14.containerId, var4.menuSlot(), var4.button(), ContainerInput.SWAP, mc.player);
               this.F = mc.player;
               this.M = mc.level;
               this.U = var1;
               this.R.c(var1, var4.equips());
               return;
            }
         }
      }

      this.X("context changed", true);
      this.R.s();
   }

   private void q(long var1, qx.w var3, boolean var4) {
      Screen var5 = mc.gui.screen();
      if (var3.equips() && var3.source() < 9 && this.b.getValue() && var5 == null) {
         this.x = mc.player;
         this.e = mc.level;
         int var8 = mc.player.getInventory().getSelectedSlot();
         this.I.P(var1, var8, var3.source(), this.E());
         _q.c("AutoTotem", "Swap hands route: hotbar {} to offhand, selected slot {}", var3.source() + 1, var8 + 1);
         this.w(var1);
      } else {
         boolean var6 = var5 instanceof InventoryScreen;
         if (var4 || var6 || this.m()) {
            this.x = mc.player;
            this.e = mc.level;
            this.p.p(var1, !var6);
            int var7 = var5 == null && this.Z.getValue() ? 1 : 0;
            if (var7 != 0) {
               this.t = new InventoryScreen(mc.player);
               mc.gui.setScreen(this.t);
            }

            _q.c(
               "AutoTotem",
               "Inventory visit for {} from slot {}: {}, waiting for idle input and no sprint",
               var4 ? "equip" : "refill",
               var3.source(),
               var7 != 0 ? "opened the inventory" : (var6 ? "player inventory already open" : "silent visit")
            );
            this.m(var1);
         }
      }
   }

   private void m(long var1) {
      if (this.t != null && mc.gui.screen() != this.t) {
         _q.c("AutoTotem", "Opened inventory was closed after {} clicks; visit ended", this.p.E());
         this.t = null;
         this.p.t();
         this.m();
      } else if (this.p.C(var1)) {
         _q.R("AutoTotem", "Inventory visit timed out in stage {}; closing and releasing input", this.p.g());
         this.X("timed out", true);
      } else {
         boolean var3 = this.m();
         switch (this.p.g()) {
            case M:
               if (this.p.Z(var1, var3, this.E())) {
                  _q.c("AutoTotem", "Server has idle input and no sprint; first click after a human pause");
               }
               break;
            case N:
               if (!this.p.L(var1, var3)) {
                  return;
               }

               qx.w var4 = this.J(this.I());
               if (var4 != null && this.e(var4)) {
                  InventoryMenu var5 = mc.player.inventoryMenu;
                  mc.gameMode.handleContainerInput(var5.containerId, var4.menuSlot(), var4.button(), ContainerInput.SWAP, mc.player);
                  this.F = mc.player;
                  this.M = mc.level;
                  this.U = var1;
                  this.R.c(var1, var4.equips());
                  this.p.s(var1, this.E());
                  _q.c(
                     "AutoTotem",
                     "Clicked slot {} to {} (state {})",
                     var4.menuSlot(),
                     var4.button() == 40 ? "offhand" : "hotbar " + (var4.button() + 1),
                     var5.getStateId()
                  );
                  break;
               }

               if (this.p.P(var1, this.E())) {
                  _q.c("AutoTotem", "Clicks done ({}); closing the inventory after a pause", this.p.E());
               } else {
                  _q.c("AutoTotem", "Visit ended after {} clicks", this.p.E());
                  this.S();
               }

               return;
            case z:
               if (!this.p.q(var1, var3)) {
                  return;
               }

               this.g();
               _q.c("AutoTotem", "Closed the inventory while still; input released");
               this.S();
               break;
            default:
               this.S();
         }
      }
   }

   private void w(long var1) {
      LocalPlayer var3 = mc.player;
      Inventory var4 = var3.getInventory();
      if (this.I.b(var1)) {
         _q.R("AutoTotem", "Swap hands route timed out in stage {}", this.I.n());
         this.X("timed out", true);
      } else if (this.I.C(var1)) {
         int var5 = this.I.T();
         switch (this.I.n()) {
            case v:
               if (!var4.getItem(var5).is(Items.TOTEM_OF_UNDYING) || var3.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
                  _q.c("AutoTotem", "Hotbar totem moved before selection; route cancelled");
                  this.I.p();
                  this.m();
                  return;
               }

               var4.setSelectedSlot(var5);
               this.I.r(var1, this.E());
               _q.c("AutoTotem", "Selected hotbar {}; swap hands follows on a later tick", var5 + 1);
               break;
            case C:
               if (var4.getSelectedSlot() != var5) {
                  _q.c("AutoTotem", "Player changed the selected slot; swap hands route cancelled");
                  this.I.p();
                  this.m();
                  return;
               }

               if (var3.getOffhandItem().is(Items.TOTEM_OF_UNDYING) || !var4.getItem(var5).is(Items.TOTEM_OF_UNDYING)) {
                  _q.c("AutoTotem", "Offhand or hotbar changed before swap hands; restoring the slot");
                  this.I.r(var1, this.E());
                  return;
               }

               if (this.G() && !qg.f(mc.options.keySwapOffhand)) {
                  ((qee5f93289f869c83)mc.options.keySwapOffhand).larpSetClickCount(1);
                  this.F = var3;
                  this.M = mc.level;
                  this.U = var1;
                  this.R.Z(var1);
                  this.I.r(var1, this.E());
                  _q.c("AutoTotem", "Pressed swap hands for hotbar {}", var5 + 1);
                  break;
               }

               return;
            case W:
               if (this.I.i(var4.getSelectedSlot())) {
                  var4.setSelectedSlot(this.I.V());
                  _q.c("AutoTotem", "Restored hotbar {}", this.I.V() + 1);
               } else {
                  _q.c("AutoTotem", "Player chose another slot; restore skipped");
               }

               this.I.p();
               this.m();
               break;
            default:
               this.I.p();
               this.m();
         }
      }
   }

   private void X(String var1, boolean var2) {
      int var3 = this.x != null && this.x == mc.player && this.e == mc.level ? 1 : 0;
      if (this.p.k()) {
         if (var2 && var3 != 0 && this.p.o() && (this.p.E() > 0 || this.t != null)) {
            this.g();
         }

         _q.c("AutoTotem", "Inventory visit stopped ({}) after {} clicks", var1, this.p.E());
      }

      if (this.I.h() && var3 != 0 && this.I.n() != qx.j.x.v && this.I.i(this.x.getInventory().getSelectedSlot())) {
         this.x.getInventory().setSelectedSlot(this.I.V());
         _q.c("AutoTotem", "Swap hands route stopped ({}); restored hotbar {}", var1, this.I.V() + 1);
      }

      this.o();
   }

   private void g() {
      LocalPlayer var1 = mc.player;
      if (var1 != null && var1 == this.x && var1.containerMenu == var1.inventoryMenu) {
         if (this.t != null && mc.gui.screen() == this.t) {
            var1.closeContainer();
         } else if (!(mc.gui.screen() instanceof InventoryScreen)) {
            var1.connection.send(new ServerboundContainerClosePacket(var1.inventoryMenu.containerId));
         }

         this.t = null;
      }
   }

   private void S() {
      this.p.t();
      this.m();
   }

   private void m() {
      if (!this.p.k() && !this.I.h()) {
         this.o();
      }
   }

   private void o() {
      this.p.t();
      this.I.p();
      this.x = null;
      this.e = null;
      this.t = null;
   }

   private boolean m(Screen var1) {
      return var1 == null
         || this.t != null && var1 == this.t
         || this.H.getValue() && var1 instanceof InventoryScreen
         || var1 instanceof ChatScreen
         || var1 instanceof PauseScreen
         || var1 instanceof n1;
   }

   private boolean G() {
      return mc.gui.screen() == null;
   }

   private int F() {
      int var1 = 0;

      for (int var2 = 0; var2 < 9; var2++) {
         if (mc.player.getInventory().getItem(var2).is(Items.TOTEM_OF_UNDYING)) {
            var1 |= 1 << var2;
         }
      }

      return var1;
   }

   private boolean m() {
      q71cffb54eb30bf94 var1 = (q71cffb54eb30bf94)mc.player;
      Input var2 = var1.larpLastSentInput();
      return var2 != null && qx.T(var1.larpWasSprinting(), var2.forward(), var2.backward(), var2.left(), var2.right(), var2.jump(), var2.shift());
   }

   private int E() {
      return qx.p(this.u.getValue().intValue(), this.c.getValue().intValue(), ThreadLocalRandom.current().nextDouble());
   }

   private boolean I() {
      ItemStack var1 = mc.player.getOffhandItem();
      return qx.a(
         var1.is(Items.TOTEM_OF_UNDYING),
         var1.isEmpty(),
         this.E.w("Always"),
         mc.player.getHealth(),
         mc.player.getAbsorptionAmount(),
         this.W.getValue(),
         this.J.getValue()
      );
   }

   private qx.w J(boolean var1) {
      long var2 = 0L;
      int var4 = 0;

      for (int var5 = 0; var5 < 36; var5++) {
         ItemStack var6 = mc.player.getInventory().getItem(var5);
         if (var6.is(Items.TOTEM_OF_UNDYING)) {
            var2 |= 1L << var5;
         } else if (var5 < 9 && var6.isEmpty()) {
            var4 |= 1 << var5;
         }
      }

      return qx.o(var2, var4, var1, this.C.w("Hotbar"), this.Y.getValue(), this.v.getValue().intValue());
   }

   private boolean e(qx.w var1) {
      Slot var2 = mc.player.inventoryMenu.getSlot(var1.menuSlot());
      return var2.getItem().is(Items.TOTEM_OF_UNDYING)
         && var2.mayPickup(mc.player)
         && var2.mayPlace(var1.equips() ? mc.player.getOffhandItem() : ItemStack.EMPTY)
         && (var1.equips() || mc.player.getInventory().getItem(var1.button()).isEmpty());
   }
}
