package com.larpclient.module;

import com.larp.obf.qbac0b262827a15ac.q12d31c89547aa6a5;
import com.larpclient._8;
import com.larpclient.mt;
import com.larpclient.tq;
import com.larpclient.w7;
import com.larpclient.zd;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.util.Util;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class AutoTotemModule extends Module {
   private static AutoTotemModule N;
   private Object O;
   private Object s;
   private long w = Long.MIN_VALUE;
   private final d6 R = this.addSetting(new d6("Mode", "When to protect your offhand", "Health", "Health", "Always"));
   private final d9 S = this.addSetting(new d9("Health", "Equip at or below this health; an empty offhand is always protected", 10.0, 1.0, 20.0, 0.5));
   private final dh M = this.addSetting(new dh("Include Absorption", "Count absorption hearts when comparing health", false));
   private final d6 b = this.addSetting(new d6("Swap Mode", "Route from inventory to offhand", "Direct", "Direct", "Hotbar"));
   private final d6 Y = this.addSetting(
      new d6("Swap Style", "Legit stops and clicks like a player in the inventory; Instant clicks at once and gets flagged", "Legit", "Legit", "Instant")
   );
   private final d9 v = this.addSetting(new d9("Click Delay", "Shortest pause in milliseconds before each legit inventory step", 100.0, 0.0, 1000.0, 10.0));
   private final d9 f = this.addSetting(new d9("Click Delay Max", "Longest pause in milliseconds before each legit inventory step", 200.0, 0.0, 1000.0, 10.0));
   private final dh V = this.addSetting(new dh("Offhand Key", "Equip hotbar totems with the swap hands key, which works while moving", true));
   private final dh B = this.addSetting(new dh("Open Inventory", "Show the inventory during legit clicks, which also stops camera turns", false));
   private final d9 d = this.addSetting(new d9("Delay", "Milliseconds before equipping", 0.0, 0.0, 1000.0, 10.0));
   private final d9 X = this.addSetting(new d9("Random Delay", "Maximum additional milliseconds, sampled once per equip", 0.0, 0.0, 1000.0, 10.0));
   private final dh D = this.addSetting(new dh("Hotbar Refill", "Keep spare totems in empty hotbar slots without replacing items", false));
   private final d9 x = this.addSetting(new d9("Hotbar Reserves", "Desired number of hotbar slots holding spare totems", 1.0, 1.0, 9.0, 1.0));
   private final dh u = this.addSetting(new dh("In Inventory", "Allow swaps in the survival inventory when the cursor is empty", true));
   private final zd.d i = new zd.d();
   private final zd.s J = new zd.s();
   private final zd.g p = new zd.g();
   private LocalPlayer T;
   private Object A;
   private Screen e;
   private boolean n;

   public AutoTotemModule() {
      super("AutoTotem", "Automatically equip offhand totems", Category.COMBAT, -1);
      N = this;
      this.S.H(() -> this.R.S("Health"));
      this.M.H(() -> this.R.S("Health"));
      this.x.H(this.D::P);
      this.v.V(this.f).n("Click Delay");
      this.v.H(() -> this.Y.S("Legit"));
      this.f.H(() -> this.Y.S("Legit"));
      this.V.H(() -> this.Y.S("Legit"));
      this.B.H(() -> this.Y.S("Legit"));
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public void onEnable() {
      this.i.v();
      this.O = this.s = null;
      this.M();
   }

   @Override
   public void onDisable() {
      this.L("module disabled", true);
      this.i.v();
      this.O = this.s = null;
   }

   public static boolean w(LocalPlayer var0) {
      if (N != null && N.isEnabled()) {
         AutoTotemModule var1 = N;
         return var1 != null && var0 != null && var1.J.c() && var1.T == var0 && var1.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean E() {
      return B(false);
   }

   public static boolean A() {
      return B(true);
   }

   private static boolean B(boolean var0) {
      AutoTotemModule var1 = N;
      if (var1 != null && var1.isRuntimeActive()) {
         Minecraft var2 = getMc();
         LocalPlayer var3 = var2.player;
         if (var3 == null || var1.T != var3 || !var1.p.o() && (!var1.J.c() || var0)) {
            if (var3 != null
               && var2.level != null
               && var3.isAlive()
               && !var3.isSpectator()
               && !tq.f()
               && var3.containerMenu == var3.inventoryMenu
               && var3.inventoryMenu.getCarried().isEmpty()
               && (var2.gui.screen() == null || var1.u.P() && var2.gui.screen() instanceof InventoryScreen)) {
               if (var0 && !var1.b.S("Hotbar")) {
                  return false;
               }

               if (var1.O == var3 && var1.s == var2.level && Util.getMillis() - var1.w < 100L) {
                  return true;
               }

               boolean var4 = zd.V(
                  var3.getOffhandItem().is(Items.TOTEM_OF_UNDYING),
                  var3.getOffhandItem().isEmpty(),
                  var1.R.S("Always"),
                  var3.getHealth(),
                  var3.getAbsorptionAmount(),
                  var1.M.P(),
                  var1.S.P()
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

               return zd.j(var5, var7, var4, var1.b.S("Hotbar"), var1.D.P(), var1.x.P().intValue()) != null;
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
      if (this.T != null && (this.T != mc.player || this.A != mc.level)) {
         mt.U("AutoTotem", "Player or world changed during a swap; state dropped without packets");
         this.M();
      }

      if (this.isInGame() && mc.gameMode != null && mc.player.isAlive()) {
         int var10000 = mc.player.isSpectator();
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if ((var10000 | -var10000) >>> 31 == 0 && !tq.f() && mc.player.containerMenu == mc.player.inventoryMenu) {
            int var17 = mc.player.inventoryMenu.getCarried().isEmpty();
            boolean var10 = false;
            byte var9 = (byte)var17;
            var17 = Integer.signum(var9);
            if (((var17 | -var17) >>> 31 ^ 1) == 0 && this.G(mc.gui.screen())) {
               long var1 = Util.getMillis();
               boolean var3 = this.n;
               this.n = mc.player.isUsingItem();
               int var19 = this.p.o();
               boolean var12 = false;
               byte var11 = (byte)var19;
               var19 = Integer.signum(var11);
               if (((var19 | -var19) >>> 31 ^ 1) == 0) {
                  this.C(var1, var3);
                  return;
               }

               if (this.J.c()) {
                  this.b(var1);
                  return;
               }

               boolean var4 = this.g();
               if (!var4) {
                  this.i.B();
               }

               zd.m var5 = this.r(var4);
               if (var5 == null) {
                  this.i.B();
                  return;
               }

               if (var4) {
                  if (!this.i.c() && this.X.P() > 0.0) {
                     ThreadLocalRandom var22 = ThreadLocalRandom.current();
                     int var10001 = this.X.P().intValue();
                     byte var14 = 1;
                     int var13 = var10001;
                     var19 = var22.nextInt(var13 + var14);
                  } else {
                     var19 = 0;
                  }

                  int var6 = var19;
                  if (!this.i.f(var1, this.d.P().intValue() + var6)) {
                     return;
                  }
               } else if (!this.i.p(var1)) {
                  return;
               }

               if (!this.a(var5)) {
                  this.i.B();
                  return;
               }

               if (this.Y.S("Legit")) {
                  this.Z(var1, var5, var4);
                  return;
               }

               InventoryMenu var15 = mc.player.inventoryMenu;
               mc.gameMode.handleContainerInput(var15.containerId, var5.menuSlot(), var5.button(), ContainerInput.SWAP, mc.player);
               this.O = mc.player;
               this.s = mc.level;
               this.w = var1;
               this.i.B(var1, var5.equips());
               return;
            }
         }
      }

      this.L("context changed", true);
      this.i.B();
   }

   private void Z(long var1, zd.m var3, boolean var4) {
      Screen var5 = mc.gui.screen();
      if (var3.equips() && var3.source() < 9 && this.V.P() && var5 == null) {
         if (!mc.player.isUsingItem()) {
            this.T = mc.player;
            this.A = mc.level;
            int var7 = mc.player.getInventory().getSelectedSlot();
            this.p.r(var1, var7, var3.source(), this.X());
            mt.U("AutoTotem", "Swap hands route: hotbar {} to offhand, selected slot {}", var3.source() + 1, var7 + 1);
            this.C(var1, mc.player.isUsingItem());
         }
      } else if (var4 || var5 != null || this.f()) {
         this.T = mc.player;
         this.A = mc.level;
         int var6 = var5 == null ? 1 : 0;
         this.J.T(var1, (boolean)var6);
         if (var6 != 0 && this.B.P()) {
            this.e = new InventoryScreen(mc.player);
            mc.gui.setScreen(this.e);
         }

         mt.U(
            "AutoTotem",
            "Inventory visit for {} from slot {}: {}, waiting for idle input and no sprint",
            var4 ? "equip" : "refill",
            var3.source(),
            var6 != 0 ? (this.B.P() ? "opened the inventory" : "silent visit") : "player inventory already open"
         );
         this.b(var1);
      }
   }

   private void b(long var1) {
      if (this.e != null && mc.gui.screen() != this.e) {
         mt.U("AutoTotem", "Opened inventory was closed after {} clicks; visit ended", this.J.T());
         this.e = null;
         this.J.G();
         this.x();
      } else if (this.J.n(var1)) {
         mt.T("AutoTotem", "Inventory visit timed out in stage {}; closing and releasing input", this.J.Q());
         this.L("timed out", true);
      } else {
         boolean var3 = this.f();
         switch (this.J.Q()) {
            case D:
               if (this.J.E(var1, var3, this.X())) {
                  mt.U("AutoTotem", "Server has idle input and no sprint; first click after a human pause");
               }
               break;
            case H:
               if (!this.J.r(var1, var3)) {
                  return;
               }

               zd.m var4 = this.r(this.g());
               if (var4 != null && this.a(var4)) {
                  InventoryMenu var5 = mc.player.inventoryMenu;
                  mc.gameMode.handleContainerInput(var5.containerId, var4.menuSlot(), var4.button(), ContainerInput.SWAP, mc.player);
                  this.O = mc.player;
                  this.s = mc.level;
                  this.w = var1;
                  this.i.B(var1, var4.equips());
                  this.J.b(var1, this.X());
                  mt.U(
                     "AutoTotem",
                     "Clicked slot {} to {} (state {})",
                     var4.menuSlot(),
                     var4.button() == 40 ? "offhand" : "hotbar " + (var4.button() + 1),
                     var5.getStateId()
                  );
                  break;
               }

               if (this.J.Q(var1, this.X())) {
                  mt.U("AutoTotem", "Clicks done ({}); closing the inventory after a pause", this.J.T());
               } else {
                  mt.U("AutoTotem", "Visit ended after {} clicks", this.J.T());
                  this.W();
               }

               return;
            case m:
               if (!this.J.L(var1, var3)) {
                  return;
               }

               this.q();
               mt.U("AutoTotem", "Closed the inventory while still; input released");
               this.W();
               break;
            default:
               this.W();
         }
      }
   }

   private void C(long var1, boolean var3) {
      LocalPlayer var4 = mc.player;
      Inventory var5 = var4.getInventory();
      if (this.p.M(var1)) {
         mt.T("AutoTotem", "Swap hands route timed out in stage {}", this.p.U());
         this.L("timed out", true);
      } else if (this.p.v(var1)) {
         int var6 = this.p.L();
         switch (this.p.U()) {
            case l:
               if (!var5.getItem(var6).is(Items.TOTEM_OF_UNDYING) || var4.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
                  mt.U("AutoTotem", "Hotbar totem moved before selection; route cancelled");
                  this.p.Z();
                  this.x();
                  return;
               }

               if (var3 || var4.isUsingItem()) {
                  return;
               }

               var5.setSelectedSlot(var6);
               this.p.i(var1, this.X());
               mt.U("AutoTotem", "Selected hotbar {}; swap hands follows on a later tick", var6 + 1);
               break;
            case K:
               if (var5.getSelectedSlot() != var6) {
                  mt.U("AutoTotem", "Player changed the selected slot; swap hands route cancelled");
                  this.p.Z();
                  this.x();
                  return;
               }

               if (var4.getOffhandItem().is(Items.TOTEM_OF_UNDYING) || !var5.getItem(var6).is(Items.TOTEM_OF_UNDYING)) {
                  mt.U("AutoTotem", "Offhand or hotbar changed before swap hands; restoring the slot");
                  this.p.i(var1, this.X());
                  return;
               }

               if (var3 || !w7.f(mc, var4) || _8.H(var4)) {
                  return;
               }

               var4.connection.send(new ServerboundPlayerActionPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
               this.O = var4;
               this.s = mc.level;
               this.w = var1;
               this.i.B(var1, true);
               this.p.i(var1, this.X());
               mt.U("AutoTotem", "Pressed swap hands for hotbar {}", var6 + 1);
               break;
            case T:
               if (this.p.B(var5.getSelectedSlot())) {
                  var5.setSelectedSlot(this.p.Y());
                  mt.U("AutoTotem", "Restored hotbar {}", this.p.Y() + 1);
               } else {
                  mt.U("AutoTotem", "Player chose another slot; restore skipped");
               }

               this.p.Z();
               this.x();
               break;
            default:
               this.p.Z();
               this.x();
         }
      }
   }

   private void L(String var1, boolean var2) {
      int var3 = this.T != null && this.T == mc.player && this.A == mc.level ? 1 : 0;
      if (this.J.c()) {
         if (var2 && var3 != 0 && this.J.F() && (this.J.T() > 0 || this.e != null)) {
            this.q();
         }

         mt.U("AutoTotem", "Inventory visit stopped ({}) after {} clicks", var1, this.J.T());
      }

      if (this.p.o() && var3 != 0 && this.p.U() != zd.g.j.l && this.p.B(this.T.getInventory().getSelectedSlot())) {
         this.T.getInventory().setSelectedSlot(this.p.Y());
         mt.U("AutoTotem", "Swap hands route stopped ({}); restored hotbar {}", var1, this.p.Y() + 1);
      }

      this.M();
   }

   private void q() {
      LocalPlayer var1 = mc.player;
      if (var1 != null && var1 == this.T && var1.containerMenu == var1.inventoryMenu) {
         if (this.e != null && mc.gui.screen() == this.e) {
            var1.closeContainer();
         } else if (mc.gui.screen() == null) {
            var1.connection.send(new ServerboundContainerClosePacket(var1.inventoryMenu.containerId));
         }

         this.e = null;
      }
   }

   private void W() {
      this.J.G();
      this.x();
   }

   private void x() {
      if (!this.J.c() && !this.p.o()) {
         this.M();
      }
   }

   private void M() {
      this.J.G();
      this.p.Z();
      this.T = null;
      this.A = null;
      this.e = null;
      this.n = (boolean)0;
   }

   private boolean G(Screen var1) {
      return var1 == null || this.e != null && var1 == this.e || this.u.P() && var1 instanceof InventoryScreen;
   }

   private boolean f() {
      q12d31c89547aa6a5 var1 = (q12d31c89547aa6a5)mc.player;
      Input var2 = var1.larpLastSentInput();
      return var2 != null && zd.l(var1.larpWasSprinting(), var2.forward(), var2.backward(), var2.left(), var2.right(), var2.jump(), var2.shift());
   }

   private int X() {
      return zd.f(this.v.P().intValue(), this.f.P().intValue(), ThreadLocalRandom.current().nextDouble());
   }

   private boolean g() {
      ItemStack var1 = mc.player.getOffhandItem();
      return zd.V(
         var1.is(Items.TOTEM_OF_UNDYING), var1.isEmpty(), this.R.S("Always"), mc.player.getHealth(), mc.player.getAbsorptionAmount(), this.M.P(), this.S.P()
      );
   }

   private zd.m r(boolean var1) {
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

      return zd.j(var2, var4, var1, this.b.S("Hotbar"), this.D.P(), this.x.P().intValue());
   }

   private boolean a(zd.m var1) {
      Slot var2 = mc.player.inventoryMenu.getSlot(var1.menuSlot());
      return var2.getItem().is(Items.TOTEM_OF_UNDYING)
         && var2.mayPickup(mc.player)
         && var2.mayPlace(var1.equips() ? mc.player.getOffhandItem() : ItemStack.EMPTY)
         && (var1.equips() || mc.player.getInventory().getItem(var1.button()).isEmpty());
   }
}
