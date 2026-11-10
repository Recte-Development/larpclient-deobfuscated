package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q1881b7c262e73011;
import com.larp.obf.qa300483f54d804a3.qa7574265f54f83cc;
import com.larpclient.bg;
import com.larpclient.hx;
import com.larpclient.p_;
import com.larpclient.si;
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
   private static AutoTotemModule q;
   private Object S;
   private Object o;
   private long p = Long.MIN_VALUE;
   private final l X = this.addSetting(new l("Mode", "When to protect your offhand", "Health", "Health", "Always"));
   private final e l = this.addSetting(new e("Health", "Equip at or below this health; an empty offhand is always protected", 10.0, 1.0, 20.0, 0.5));
   private final s t = this.addSetting(new s("Include Absorption", "Count absorption hearts when comparing health", false));
   private final l K = this.addSetting(new l("Swap Mode", "Route from inventory to offhand", "Direct", "Direct", "Hotbar"));
   private final l Y = this.addSetting(
      new l("Swap Style", "Legit stops and clicks like a player in the inventory; Instant clicks at once and gets flagged", "Legit", "Legit", "Instant")
   );
   private final e C = this.addSetting(new e("Click Delay", "Shortest pause in milliseconds before each legit inventory step", 100.0, 0.0, 1000.0, 10.0));
   private final e f = this.addSetting(new e("Click Delay Max", "Longest pause in milliseconds before each legit inventory step", 200.0, 0.0, 1000.0, 10.0));
   private final s Q = this.addSetting(new s("Offhand Key", "Equip hotbar totems with the swap hands key, which works while moving", true));
   private final s u = this.addSetting(new s("Open Inventory", "Show the inventory during legit clicks, which also stops camera turns", false));
   private final e F = this.addSetting(new e("Delay", "Milliseconds before equipping", 0.0, 0.0, 1000.0, 10.0));
   private final e Z = this.addSetting(new e("Random Delay", "Maximum additional milliseconds, sampled once per equip", 0.0, 0.0, 1000.0, 10.0));
   private final s b = this.addSetting(new s("Hotbar Refill", "Keep spare totems in empty hotbar slots without replacing items", false));
   private final e H = this.addSetting(new e("Hotbar Reserves", "Desired number of hotbar slots holding spare totems", 1.0, 1.0, 9.0, 1.0));
   private final s y = this.addSetting(new s("In Inventory", "Allow swaps in the survival inventory when the cursor is empty", true));
   private final hx.q s = new hx.q();
   private final hx.j x = new hx.j();
   private final hx.o P = new hx.o();
   private LocalPlayer T;
   private Object e;
   private Screen E;

   public AutoTotemModule() {
      super("AutoTotem", "Automatically equip offhand totems", Category.COMBAT, -1);
      q = this;
      this.l.P(() -> this.X.O("Health"));
      this.t.P(() -> this.X.O("Health"));
      this.H.P(this.b::getValue);
      this.C.C(this.f).h("Click Delay");
      this.C.P(() -> this.Y.O("Legit"));
      this.f.P(() -> this.Y.O("Legit"));
      this.Q.P(() -> this.Y.O("Legit"));
      this.u.P(() -> this.Y.O("Legit"));
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   @Override
   public void onEnable() {
      this.s.R();
      this.S = this.o = null;
      this.C();
   }

   @Override
   public void onDisable() {
      this.p("module disabled", true);
      this.s.R();
      this.S = this.o = null;
   }

   public static boolean T(LocalPlayer var0) {
      if (q != null && q.isEnabled()) {
         AutoTotemModule var1 = q;
         return var1 != null && var0 != null && var1.x.b() && var1.T == var0 && var1.isRuntimeActive();
      } else {
         return false;
      }
   }

   public static boolean X() {
      return L(false);
   }

   public static boolean N() {
      return L(true);
   }

   private static boolean L(boolean var0) {
      AutoTotemModule var1 = q;
      if (var1 != null && var1.isRuntimeActive()) {
         Minecraft var2 = getMc();
         LocalPlayer var3 = var2.player;
         if (var3 == null || var1.T != var3 || !var1.P.x() && (!var1.x.b() || var0)) {
            if (var3 != null
               && var2.level != null
               && var3.isAlive()
               && !var3.isSpectator()
               && !p_.V()
               && var3.containerMenu == var3.inventoryMenu
               && var3.inventoryMenu.getCarried().isEmpty()
               && var1.Y(var2.gui.screen())) {
               if (var0 && !var1.K.O("Hotbar")) {
                  return false;
               }

               if (var1.S == var3 && var1.o == var2.level && Util.getMillis() - var1.p < 100L) {
                  return true;
               }

               boolean var4 = hx.n(
                  var3.getOffhandItem().is(Items.TOTEM_OF_UNDYING),
                  var3.getOffhandItem().isEmpty(),
                  var1.X.O("Always"),
                  var3.getHealth(),
                  var3.getAbsorptionAmount(),
                  var1.t.getValue(),
                  var1.l.getValue()
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

               return hx.E(var5, var7, var4, var1.K.O("Hotbar"), var1.b.getValue(), var1.H.getValue().intValue()) != null;
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
      if (this.T != null && (this.T != mc.player || this.e != mc.level)) {
         com.larpclient.h.U("AutoTotem", "Player or world changed during a swap; state dropped without packets");
         this.C();
      }

      if (this.isInGame()
         && mc.gameMode != null
         && mc.player.isAlive()
         && !mc.player.isSpectator()
         && !p_.V()
         && mc.player.containerMenu == mc.player.inventoryMenu
         && mc.player.inventoryMenu.getCarried().isEmpty()
         && this.Y(mc.gui.screen())) {
         long var1 = Util.getMillis();
         int var10000 = this.P.x();
         boolean var7 = false;
         byte var6 = (byte)var10000;
         var10000 = Integer.signum(var6);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.y(var1);
         } else {
            int var16 = this.x.b();
            boolean var11 = false;
            byte var10 = (byte)var16;
            var16 = Integer.signum(var10);
            if (((var16 | -var16) >>> 31 ^ 1) == 0) {
               this.b(var1);
            } else {
               if (this.s.s()) {
                  if (mc.player.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
                     this.s.L();
                     com.larpclient.h.U("AutoTotem", "Server confirmed the swap hands press");
                  } else {
                     int var18 = this.s.w(var1);
                     boolean var13 = false;
                     byte var12 = (byte)var18;
                     var18 = Integer.signum(var12);
                     if (((var18 | -var18) >>> 31 ^ 1) == 0) {
                        return;
                     }

                     this.s.L();
                     com.larpclient.h.g("AutoTotem", "No totem {} ms after swap hands; the server dropped it or is lagging, retrying", 750L);
                  }
               }

               boolean var3 = this.z();
               if (!var3) {
                  this.s.z();
               }

               hx.n var4 = this.n(var3);
               if (var4 == null) {
                  this.s.z();
               } else {
                  if (var3) {
                     int var20 = this.Y.O("Legit");
                     boolean var9 = false;
                     byte var8 = (byte)var20;
                     var20 = Integer.signum(var8);
                     if (((var20 | -var20) >>> 31 ^ 1) == 0 && this.Q.getValue() && this.I()) {
                        var4 = hx.u(var4, this.C(), !this.o(), mc.player.isUsingItem());
                     }
                  }

                  if (var3) {
                     int var5 = !this.s.A() && this.Z.getValue() > 0.0 ? ThreadLocalRandom.current().nextInt(this.Z.getValue().intValue() + 1) : 0;
                     if (!this.s.x(var1, this.F.getValue().intValue() + var5)) {
                        return;
                     }
                  } else if (!this.s.k(var1)) {
                     return;
                  }

                  if (!this.v(var4)) {
                     this.s.z();
                  } else if (this.Y.O("Legit")) {
                     this.j(var1, var4, var3);
                  } else {
                     InventoryMenu var14 = mc.player.inventoryMenu;
                     mc.gameMode.handleContainerInput(var14.containerId, var4.menuSlot(), var4.button(), ContainerInput.SWAP, mc.player);
                     this.S = mc.player;
                     this.o = mc.level;
                     this.p = var1;
                     this.s.Q(var1, var4.equips());
                  }
               }
            }
         }
      } else {
         this.p("context changed", true);
         this.s.z();
      }
   }

   private void j(long var1, hx.n var3, boolean var4) {
      Screen var5 = mc.gui.screen();
      if (var3.equips() && var3.source() < 9 && this.Q.getValue() && var5 == null) {
         this.T = mc.player;
         this.e = mc.level;
         int var8 = mc.player.getInventory().getSelectedSlot();
         this.P.A(var1, var8, var3.source(), this.i());
         com.larpclient.h.U("AutoTotem", "Swap hands route: hotbar {} to offhand, selected slot {}", var3.source() + 1, var8 + 1);
         this.y(var1);
      } else {
         boolean var6 = var5 instanceof InventoryScreen;
         if (var4 || var6 || this.o()) {
            this.T = mc.player;
            this.e = mc.level;
            this.x.o(var1, !var6);
            int var7 = var5 == null && this.u.getValue() ? 1 : 0;
            if (var7 != 0) {
               this.E = new InventoryScreen(mc.player);
               mc.gui.setScreen(this.E);
            }

            com.larpclient.h.U(
               "AutoTotem",
               "Inventory visit for {} from slot {}: {}, waiting for idle input and no sprint",
               var4 ? "equip" : "refill",
               var3.source(),
               var7 != 0 ? "opened the inventory" : (var6 ? "player inventory already open" : "silent visit")
            );
            this.b(var1);
         }
      }
   }

   private void b(long var1) {
      if (this.E != null && mc.gui.screen() != this.E) {
         com.larpclient.h.U("AutoTotem", "Opened inventory was closed after {} clicks; visit ended", this.x.b());
         this.E = null;
         this.x.p();
         this.M();
      } else if (this.x.v(var1)) {
         com.larpclient.h.g("AutoTotem", "Inventory visit timed out in stage {}; closing and releasing input", this.x.I());
         this.p("timed out", true);
      } else {
         boolean var3 = this.o();
         switch (this.x.I()) {
            case m:
               if (this.x.S(var1, var3, this.i())) {
                  com.larpclient.h.U("AutoTotem", "Server has idle input and no sprint; first click after a human pause");
               }
               break;
            case H:
               if (!this.x.M(var1, var3)) {
                  return;
               }

               hx.n var4 = this.n(this.z());
               if (var4 != null && this.v(var4)) {
                  InventoryMenu var5 = mc.player.inventoryMenu;
                  mc.gameMode.handleContainerInput(var5.containerId, var4.menuSlot(), var4.button(), ContainerInput.SWAP, mc.player);
                  this.S = mc.player;
                  this.o = mc.level;
                  this.p = var1;
                  this.s.Q(var1, var4.equips());
                  this.x.t(var1, this.i());
                  com.larpclient.h.U(
                     "AutoTotem",
                     "Clicked slot {} to {} (state {})",
                     var4.menuSlot(),
                     var4.button() == 40 ? "offhand" : "hotbar " + (var4.button() + 1),
                     var5.getStateId()
                  );
                  break;
               }

               if (this.x.H(var1, this.i())) {
                  com.larpclient.h.U("AutoTotem", "Clicks done ({}); closing the inventory after a pause", this.x.b());
               } else {
                  com.larpclient.h.U("AutoTotem", "Visit ended after {} clicks", this.x.b());
                  this.B();
               }

               return;
            case k:
               if (!this.x.H(var1, var3)) {
                  return;
               }

               this.T();
               com.larpclient.h.U("AutoTotem", "Closed the inventory while still; input released");
               this.B();
               break;
            default:
               this.B();
         }
      }
   }

   private void y(long var1) {
      LocalPlayer var3 = mc.player;
      Inventory var4 = var3.getInventory();
      if (this.P.l(var1)) {
         com.larpclient.h.g("AutoTotem", "Swap hands route timed out in stage {}", this.P.s());
         this.p("timed out", true);
      } else if (this.P.Y(var1)) {
         int var5 = this.P.Z();
         switch (this.P.s()) {
            case c:
               if (var4.getItem(var5).is(Items.TOTEM_OF_UNDYING) && !var3.getOffhandItem().is(Items.TOTEM_OF_UNDYING)) {
                  var4.setSelectedSlot(var5);
                  this.P.o(var1, this.i());
                  com.larpclient.h.U("AutoTotem", "Selected hotbar {}; swap hands follows on a later tick", var5 + 1);
                  break;
               }

               com.larpclient.h.U("AutoTotem", "Hotbar totem moved before selection; route cancelled");
               this.P.Y();
               this.M();
               return;
            case G:
               if (var4.getSelectedSlot() != var5) {
                  com.larpclient.h.U("AutoTotem", "Player changed the selected slot; swap hands route cancelled");
                  this.P.Y();
                  this.M();
                  return;
               }

               if (!var3.getOffhandItem().is(Items.TOTEM_OF_UNDYING) && var4.getItem(var5).is(Items.TOTEM_OF_UNDYING)) {
                  if (this.I() && !si.h(mc.options.keySwapOffhand)) {
                     ((qa7574265f54f83cc)mc.options.keySwapOffhand).larpSetClickCount(1);
                     this.S = var3;
                     this.o = mc.level;
                     this.p = var1;
                     this.s.g(var1);
                     this.P.o(var1, this.i());
                     com.larpclient.h.U("AutoTotem", "Pressed swap hands for hotbar {}", var5 + 1);
                     break;
                  }

                  return;
               }

               com.larpclient.h.U("AutoTotem", "Offhand or hotbar changed before swap hands; restoring the slot");
               this.P.o(var1, this.i());
               return;
            case o:
               if (this.P.K(var4.getSelectedSlot())) {
                  var4.setSelectedSlot(this.P.B());
                  com.larpclient.h.U("AutoTotem", "Restored hotbar {}", this.P.B() + 1);
               } else {
                  com.larpclient.h.U("AutoTotem", "Player chose another slot; restore skipped");
               }

               this.P.Y();
               this.M();
               break;
            default:
               this.P.Y();
               this.M();
         }
      }
   }

   private void p(String var1, boolean var2) {
      int var3 = this.T != null && this.T == mc.player && this.e == mc.level ? 1 : 0;
      if (this.x.b()) {
         if (var2 && var3 != 0 && this.x.v() && (this.x.b() > 0 || this.E != null)) {
            this.T();
         }

         com.larpclient.h.U("AutoTotem", "Inventory visit stopped ({}) after {} clicks", var1, this.x.b());
      }

      if (this.P.x() && var3 != 0 && this.P.s() != hx.o.h.c && this.P.K(this.T.getInventory().getSelectedSlot())) {
         this.T.getInventory().setSelectedSlot(this.P.B());
         com.larpclient.h.U("AutoTotem", "Swap hands route stopped ({}); restored hotbar {}", var1, this.P.B() + 1);
      }

      this.C();
   }

   private void T() {
      LocalPlayer var1 = mc.player;
      if (var1 != null && var1 == this.T && var1.containerMenu == var1.inventoryMenu) {
         if (this.E != null && mc.gui.screen() == this.E) {
            var1.closeContainer();
         } else if (!(mc.gui.screen() instanceof InventoryScreen)) {
            var1.connection.send(new ServerboundContainerClosePacket(var1.inventoryMenu.containerId));
         }

         this.E = null;
      }
   }

   private void B() {
      this.x.p();
      this.M();
   }

   private void M() {
      if (!this.x.b() && !this.P.x()) {
         this.C();
      }
   }

   private void C() {
      this.x.p();
      this.P.Y();
      this.T = null;
      this.e = null;
      this.E = null;
   }

   private boolean Y(Screen var1) {
      return var1 == null
         || this.E != null && var1 == this.E
         || this.y.getValue() && var1 instanceof InventoryScreen
         || var1 instanceof ChatScreen
         || var1 instanceof PauseScreen
         || var1 instanceof bg;
   }

   private boolean I() {
      return mc.gui.screen() == null;
   }

   private int C() {
      int var1 = 0;

      for (int var2 = 0; var2 < 9; var2++) {
         if (mc.player.getInventory().getItem(var2).is(Items.TOTEM_OF_UNDYING)) {
            var1 |= 1 << var2;
         }
      }

      return var1;
   }

   private boolean o() {
      q1881b7c262e73011 var1 = (q1881b7c262e73011)mc.player;
      Input var2 = var1.larpLastSentInput();
      return var2 != null && hx.u(var1.larpWasSprinting(), var2.forward(), var2.backward(), var2.left(), var2.right(), var2.jump(), var2.shift());
   }

   private int i() {
      return hx.C(this.C.getValue().intValue(), this.f.getValue().intValue(), ThreadLocalRandom.current().nextDouble());
   }

   private boolean z() {
      ItemStack var1 = mc.player.getOffhandItem();
      return hx.n(
         var1.is(Items.TOTEM_OF_UNDYING),
         var1.isEmpty(),
         this.X.O("Always"),
         mc.player.getHealth(),
         mc.player.getAbsorptionAmount(),
         this.t.getValue(),
         this.l.getValue()
      );
   }

   private hx.n n(boolean var1) {
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

      return hx.E(var2, var4, var1, this.K.O("Hotbar"), this.b.getValue(), this.H.getValue().intValue());
   }

   private boolean v(hx.n var1) {
      Slot var2 = mc.player.inventoryMenu.getSlot(var1.menuSlot());
      return var2.getItem().is(Items.TOTEM_OF_UNDYING)
         && var2.mayPickup(mc.player)
         && var2.mayPlace(var1.equips() ? mc.player.getOffhandItem() : ItemStack.EMPTY)
         && (var1.equips() || mc.player.getInventory().getItem(var1.button()).isEmpty());
   }
}
