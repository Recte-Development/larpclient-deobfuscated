package com.larpclient.module;

import com.larp.obf.q5682c6a8aca9b6ec.qac882a24641eea19;
import com.larpclient.hp;
import com.larpclient.j5;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.yh;
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
   private static AutoTotemModule f;
   private Object p;
   private Object y;
   private long S = Long.MIN_VALUE;
   private final lp d = this.addSetting(new lp("Mode", "When to protect your offhand", "Health", "Health", "Always"));
   private final lc G = this.addSetting(new lc("Health", "Equip at or below this health; an empty offhand is always protected", 10.0, 1.0, 20.0, 0.5));
   private final ln v = this.addSetting(new ln("Include Absorption", "Count absorption hearts when comparing health", false));
   private final lp z = this.addSetting(new lp("Swap Mode", "Route from inventory to offhand", "Direct", "Direct", "Hotbar"));
   private final lp J = this.addSetting(
      new lp("Swap Style", "Legit stops and clicks like a player in the inventory; Instant clicks at once and gets flagged", "Legit", "Legit", "Instant")
   );
   private final lc c = this.addSetting(new lc("Click Delay", "Shortest pause in milliseconds before each legit inventory step", 100.0, 0.0, 1000.0, 10.0));
   private final lc M = this.addSetting(new lc("Click Delay Max", "Longest pause in milliseconds before each legit inventory step", 200.0, 0.0, 1000.0, 10.0));
   private final ln K = this.addSetting(new ln("Offhand Key", "Equip hotbar totems with the swap hands key, which works while moving", true));
   private final ln e = this.addSetting(new ln("Open Inventory", "Show the inventory during legit clicks, which also stops camera turns", false));
   private final lc W = this.addSetting(new lc("Delay", "Milliseconds before equipping", 0.0, 0.0, 1000.0, 10.0));
   private final lc X = this.addSetting(new lc("Random Delay", "Maximum additional milliseconds, sampled once per equip", 0.0, 0.0, 1000.0, 10.0));
   private final ln u = this.addSetting(new ln("Hotbar Refill", "Keep spare totems in empty hotbar slots without replacing items", false));
   private final lc b = this.addSetting(new lc("Hotbar Reserves", "Desired number of hotbar slots holding spare totems", 1.0, 1.0, 9.0, 1.0));
   private final ln B = this.addSetting(new ln("In Inventory", "Allow swaps in the survival inventory when the cursor is empty", true));
   private final yh.j C = new yh.j();
   private final yh.k Q = new yh.k();
   private final yh.x j = new yh.x();
   private LocalPlayer a;
   private Object U;
   private Screen Y;
   private boolean P;

   public AutoTotemModule() {
      super("AutoTotem", "Automatically equip offhand totems", Category.COMBAT, -1);
      f = this;
      this.G.x(() -> this.d.i("Health"));
      this.v.x(() -> this.d.i("Health"));
      this.b.x(this.u::getValue);
      this.c.A(this.M).b("Click Delay");
      this.c.x(() -> this.J.i("Legit"));
      this.M.x(() -> this.J.i("Legit"));
      this.K.x(() -> this.J.i("Legit"));
      this.e.x(() -> this.J.i("Legit"));
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public void onEnable() {
      this.C.k();
      this.p = this.y = null;
      this.k();
   }

   @Override
   public void onDisable() {
      this.F("module disabled", true);
      this.C.k();
      this.p = this.y = null;
   }

   public static boolean H(LocalPlayer var0) {
      if (f != null && f.isEnabled()) {
         AutoTotemModule var1 = f;
         return var1 != null && var0 != null && var1.Q.f() && var1.a == var0 && var1.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean F() {
      return O(false);
   }

   public static boolean j() {
      return O(true);
   }

   private static boolean O(boolean var0) {
      AutoTotemModule var1 = f;
      if (var1 != null && var1.isRuntimeActive()) {
         Minecraft var2 = getMc();
         LocalPlayer var3 = var2.player;
         if (var3 == null || var1.a != var3 || !var1.j.D() && (!var1.Q.f() || var0)) {
            if (var3 != null
               && var2.level != null
               && var3.isAlive()
               && !var3.isSpectator()
               && !rr.o()
               && var3.containerMenu == var3.inventoryMenu
               && var3.inventoryMenu.getCarried().isEmpty()
               && (var2.gui.screen() == null || var1.B.getValue() && var2.gui.screen() instanceof InventoryScreen)) {
               if (var0 && !var1.z.i("Hotbar")) {
                  return false;
               }

               if (var1.p == var3 && var1.y == var2.level && Util.getMillis() - var1.S < 100L) {
                  return true;
               }

               boolean var4 = yh.b(
                  var3.getOffhandItem().is(Items.TOTEM_OF_UNDYING),
                  var3.getOffhandItem().isEmpty(),
                  var1.d.i("Always"),
                  var3.getHealth(),
                  var3.getAbsorptionAmount(),
                  var1.v.getValue(),
                  var1.G.getValue()
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

               return yh.b(var5, var7, var4, var1.z.i("Hotbar"), var1.u.getValue(), var1.b.getValue().intValue()) != null;
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
      if (this.a != null && (this.a != mc.player || this.U != mc.level)) {
         r4.g("AutoTotem", "Player or world changed during a swap; state dropped without packets");
         this.k();
      }

      if (this.isInGame()
         && mc.gameMode != null
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && !rr.o()
         && mc.player.containerMenu == mc.player.inventoryMenu
         && mc.player.inventoryMenu.getCarried().isEmpty()
         && this.i(mc.gui.screen())) {
         long var1 = Util.getMillis();
         boolean var3 = this.P;
         this.P = mc.player.isUsingItem();
         if (this.j.D()) {
            this.z(var1, var3);
         } else if (this.Q.f()) {
            this.t(var1);
         } else {
            byte var4 = this.y();
            boolean var10 = false;
            byte var9 = var4;
            int var10000 = Integer.signum(var9);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.C.o();
            }

            yh.b var5 = this.K((boolean)var4);
            if (var5 == null) {
               this.C.o();
            } else {
               boolean var14 = false;
               byte var13 = var4;
               var10000 = Integer.signum(var13);
               if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                  int var6 = !this.C.H() && this.X.getValue() > 0.0 ? ThreadLocalRandom.current().nextInt(this.X.getValue().intValue() + 1) : 0;
                  int var17 = this.C.B(var1, this.W.getValue().intValue() + var6);
                  boolean var12 = false;
                  byte var11 = (byte)var17;
                  var17 = Integer.signum(var11);
                  if ((var17 | -var17) >>> 31 == 0) {
                     return;
                  }
               } else if (!this.C.R(var1)) {
                  return;
               }

               int var19 = this.t(var5);
               boolean var8 = false;
               byte var7 = (byte)var19;
               var19 = Integer.signum(var7);
               if ((var19 | -var19) >>> 31 == 0) {
                  this.C.o();
               } else if (this.J.i("Legit")) {
                  this.B(var1, var5, (boolean)var4);
               } else {
                  InventoryMenu var15 = mc.player.inventoryMenu;
                  mc.gameMode.handleContainerInput(var15.containerId, var5.menuSlot(), var5.button(), ContainerInput.SWAP, mc.player);
                  this.p = mc.player;
                  this.y = mc.level;
                  this.S = var1;
                  this.C.B(var1, var5.equips());
               }
            }
         }
      } else {
         this.F("context changed", true);
         this.C.o();
      }
   }

   private void B(long var1, yh.b var3, boolean var4) {
      Screen var5 = mc.gui.screen();
      if (var3.equips() && var3.source() < 9 && this.K.getValue() && var5 == null) {
         if (!mc.player.isUsingItem()) {
            this.a = mc.player;
            this.U = mc.level;
            int var7 = mc.player.getInventory().getSelectedSlot();
            this.j.K(var1, var7, var3.source(), this.Q());
            r4.g("AutoTotem", "Swap hands route: hotbar {} to offhand, selected slot {}", var3.source() + 1, var7 + 1);
            this.z(var1, mc.player.isUsingItem());
         }
      } else if (var4 || var5 != null || this.M()) {
         this.a = mc.player;
         this.U = mc.level;
         int var6 = var5 == null ? 1 : 0;
         this.Q.R(var1, (boolean)var6);
         if (var6 != 0 && this.e.getValue()) {
            this.Y = new InventoryScreen(mc.player);
            mc.gui.setScreen(this.Y);
         }

         r4.g(
            "AutoTotem",
            "Inventory visit for {} from slot {}: {}, waiting for idle input and no sprint",
            var4 ? "equip" : "refill",
            var3.source(),
            var6 != 0 ? (this.e.getValue() ? "opened the inventory" : "silent visit") : "player inventory already open"
         );
         this.t(var1);
      }
   }

   private void t(long var1) {
      if (this.Y != null && mc.gui.screen() != this.Y) {
         r4.g("AutoTotem", "Opened inventory was closed after {} clicks; visit ended", this.Q.H());
         this.Y = null;
         this.Q.g();
         this.y();
      } else if (this.Q.c(var1)) {
         r4.m("AutoTotem", "Inventory visit timed out in stage {}; closing and releasing input", this.Q.D());
         this.F("timed out", true);
      } else {
         boolean var3 = this.M();
         switch (this.Q.D()) {
            case o:
               if (this.Q.j(var1, var3, this.Q())) {
                  r4.g("AutoTotem", "Server has idle input and no sprint; first click after a human pause");
               }
               break;
            case d:
               if (!this.Q.U(var1, var3)) {
                  return;
               }

               yh.b var4 = this.K(this.y());
               if (var4 != null && this.t(var4)) {
                  InventoryMenu var5 = mc.player.inventoryMenu;
                  mc.gameMode.handleContainerInput(var5.containerId, var4.menuSlot(), var4.button(), ContainerInput.SWAP, mc.player);
                  this.p = mc.player;
                  this.y = mc.level;
                  this.S = var1;
                  this.C.B(var1, var4.equips());
                  this.Q.n(var1, this.Q());
                  r4.g(
                     "AutoTotem",
                     "Clicked slot {} to {} (state {})",
                     var4.menuSlot(),
                     var4.button() == 40 ? "offhand" : "hotbar " + (var4.button() + 1),
                     var5.getStateId()
                  );
                  break;
               }

               if (this.Q.t(var1, this.Q())) {
                  r4.g("AutoTotem", "Clicks done ({}); closing the inventory after a pause", this.Q.H());
               } else {
                  r4.g("AutoTotem", "Visit ended after {} clicks", this.Q.H());
                  this.q();
               }

               return;
            case c:
               if (!this.Q.x(var1, var3)) {
                  return;
               }

               this.F();
               r4.g("AutoTotem", "Closed the inventory while still; input released");
               this.q();
               break;
            default:
               this.q();
         }
      }
   }

   private void z(long var1, boolean var3) {
      LocalPlayer var4 = mc.player;
      Inventory var5 = var4.getInventory();
      if (this.j.h(var1)) {
         r4.m("AutoTotem", "Swap hands route timed out in stage {}", this.j.m());
         this.F("timed out", true);
      } else if (this.j.a(var1)) {
         int var6 = this.j.r();
         switch (this.j.m()) {
            case i:
               if (!var5.getItem(var6).is(Items.TOTEM_OF_UNDYING) || var4.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
                  r4.g("AutoTotem", "Hotbar totem moved before selection; route cancelled");
                  this.j.D();
                  this.y();
                  return;
               }

               if (var3 || var4.isUsingItem()) {
                  return;
               }

               var5.setSelectedSlot(var6);
               this.j.q(var1, this.Q());
               r4.g("AutoTotem", "Selected hotbar {}; swap hands follows on a later tick", var6 + 1);
               break;
            case M:
               if (var5.getSelectedSlot() != var6) {
                  r4.g("AutoTotem", "Player changed the selected slot; swap hands route cancelled");
                  this.j.D();
                  this.y();
                  return;
               }

               if (!var4.getOffhandItem().is(Items.TOTEM_OF_UNDYING) && var5.getItem(var6).is(Items.TOTEM_OF_UNDYING)) {
                  if (var3 || !j5.E(mc, var4) || hp.a(var4)) {
                     return;
                  }

                  var4.connection.send(new ServerboundPlayerActionPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
                  this.p = var4;
                  this.y = mc.level;
                  this.S = var1;
                  this.C.B(var1, true);
                  this.j.q(var1, this.Q());
                  r4.g("AutoTotem", "Pressed swap hands for hotbar {}", var6 + 1);
                  break;
               }

               r4.g("AutoTotem", "Offhand or hotbar changed before swap hands; restoring the slot");
               this.j.q(var1, this.Q());
               return;
            case K:
               if (this.j.Y(var5.getSelectedSlot())) {
                  var5.setSelectedSlot(this.j.V());
                  r4.g("AutoTotem", "Restored hotbar {}", this.j.V() + 1);
               } else {
                  r4.g("AutoTotem", "Player chose another slot; restore skipped");
               }

               this.j.D();
               this.y();
               break;
            default:
               this.j.D();
               this.y();
         }
      }
   }

   private void F(String var1, boolean var2) {
      int var3 = this.a != null && this.a == mc.player && this.U == mc.level ? 1 : 0;
      if (this.Q.f()) {
         if (var2 && var3 != 0 && this.Q.Z() && (this.Q.H() > 0 || this.Y != null)) {
            this.F();
         }

         r4.g("AutoTotem", "Inventory visit stopped ({}) after {} clicks", var1, this.Q.H());
      }

      if (this.j.D() && var3 != 0 && this.j.m() != yh.x.m.i && this.j.Y(this.a.getInventory().getSelectedSlot())) {
         this.a.getInventory().setSelectedSlot(this.j.V());
         r4.g("AutoTotem", "Swap hands route stopped ({}); restored hotbar {}", var1, this.j.V() + 1);
      }

      this.k();
   }

   private void F() {
      LocalPlayer var1 = mc.player;
      if (var1 != null && var1 == this.a && var1.containerMenu == var1.inventoryMenu) {
         if (this.Y != null && mc.gui.screen() == this.Y) {
            var1.closeContainer();
         } else if (mc.gui.screen() == null) {
            var1.connection.send(new ServerboundContainerClosePacket(var1.inventoryMenu.containerId));
         }

         this.Y = null;
      }
   }

   private void q() {
      this.Q.g();
      this.y();
   }

   private void y() {
      if (!this.Q.f() && !this.j.D()) {
         this.k();
      }
   }

   private void k() {
      this.Q.g();
      this.j.D();
      this.a = null;
      this.U = null;
      this.Y = null;
      this.P = (boolean)0;
   }

   private boolean i(Screen var1) {
      return var1 == null || this.Y != null && var1 == this.Y || this.B.getValue() && var1 instanceof InventoryScreen;
   }

   private boolean M() {
      qac882a24641eea19 var1 = (qac882a24641eea19)mc.player;
      Input var2 = var1.larpLastSentInput();
      return var2 != null && yh.S(var1.larpWasSprinting(), var2.forward(), var2.backward(), var2.left(), var2.right(), var2.jump(), var2.shift());
   }

   private int Q() {
      return yh.Z(this.c.getValue().intValue(), this.M.getValue().intValue(), ThreadLocalRandom.current().nextDouble());
   }

   private boolean y() {
      ItemStack var1 = mc.player.getOffhandItem();
      return yh.b(
         var1.is(Items.TOTEM_OF_UNDYING),
         var1.isEmpty(),
         this.d.i("Always"),
         mc.player.getHealth(),
         mc.player.getAbsorptionAmount(),
         this.v.getValue(),
         this.G.getValue()
      );
   }

   private yh.b K(boolean var1) {
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

      return yh.b(var2, var4, var1, this.z.i("Hotbar"), this.u.getValue(), this.b.getValue().intValue());
   }

   private boolean t(yh.b var1) {
      Slot var2 = mc.player.inventoryMenu.getSlot(var1.menuSlot());
      return var2.getItem().is(Items.TOTEM_OF_UNDYING)
         && var2.mayPickup(mc.player)
         && var2.mayPlace(var1.equips() ? mc.player.getOffhandItem() : ItemStack.EMPTY)
         && (var1.equips() || mc.player.getInventory().getItem(var1.button()).isEmpty());
   }
}
