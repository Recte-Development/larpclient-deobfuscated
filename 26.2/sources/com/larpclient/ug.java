package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapId;

final class ug {
   final AbstractContainerMenu Q;
   final List<ItemStack> F = new ArrayList<>();
   private final List<ItemStack> m = new ArrayList<>();
   private final Set<Integer> G = new HashSet<>();
   private final w9 d = new w9();
   private ItemStack H = ItemStack.EMPTY;
   private ItemStack h = ItemStack.EMPTY;
   private bx.i L;
   private BiConsumer<Integer, Integer> Z;
   private boolean g;
   private long p;
   private int r;
   private int O;
   boolean z;
   boolean P;
   boolean K;
   boolean W;
   boolean N;
   String y;
   private final long v = System.nanoTime();
   private final ArrayDeque<String> E = new ArrayDeque<>();

   void P(String var1) {
      if (this.E.size() == 48) {
         this.E.removeFirst();
      }

      this.E.addLast((System.nanoTime() - this.v) / 1000000L + "ms " + var1);
   }

   String p() {
      return "sync="
         + this.Q.containerId
         + " revision="
         + this.Q.getStateId()
         + " pending="
         + this.z
         + " ack["
         + this.d.y()
         + "] source{"
         + this.o(this.r)
         + "} destination{"
         + this.o(this.O)
         + "} "
         + this.o(-1)
         + " events="
         + this.E;
   }

   ug(AbstractContainerMenu var1) {
      this.Q = var1;
   }

   ug n() {
      ug var1 = new ug(this.Q);
      if (this.W) {
         var1.s(this.F, this.H);
      }

      return var1;
   }

   void s(List<ItemStack> var1, ItemStack var2) {
      if (var1.size() == this.Q.slots.size()) {
         this.F.clear();

         for (ItemStack var4 : var1) {
            this.F.add(var4.copy());
         }

         this.H = var2.copy();
         this.W = (boolean)1;
         this.d.C(this.Q, ++this.p, true, -1, true);
      }
   }

   void W(int var1, ItemStack var2) {
      if (this.W) {
         if (var1 == -1) {
            this.H = var2.copy();
         } else {
            if (var1 < 0 || var1 >= this.F.size()) {
               return;
            }

            this.F.set(var1, var2.copy());
         }

         this.d.C(this.Q, ++this.p, false, var1, var1 == -1);
      }
   }

   boolean X() {
      if (this.W && s9.z(this.H, this.h) && s9.z(this.Q.getCarried(), this.h)) {
         for (int var1 = 0; var1 < this.F.size(); var1++) {
            if (!s9.z(this.F.get(var1), this.Q.getSlot(var1).getItem()) || this.G.contains(var1) && !s9.z(this.F.get(var1), this.m.get(var1))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   ItemStack Z(int var1) {
      return this.m.get(var1);
   }

   boolean f(int var1) {
      return this.G.contains(var1);
   }

   boolean S() {
      return this.W && !this.z && this.y == null && this.H.isEmpty() && this.Q.getCarried().isEmpty();
   }

   boolean Q() {
      return this.F(true);
   }

   boolean F(boolean var1) {
      if (this.P) {
         return false;
      }

      if (this.y != null) {
         return false;
      }

      if (!this.z) {
         return true;
      }

      if (this.d.I(s9.z(this.F.get(this.r), this.m.get(this.r)), s9.z(this.F.get(this.O), this.m.get(this.O)), s9.z(this.H, this.h))
         && (!this.g || this.d.N())
         && this.X()) {
         if (this.L == null) {
            this.z = (boolean)0;
            return true;
         }

         ItemStack var2 = this.F.get(this.r);
         if (ItemStack.matches(var2, this.Q.getSlot(this.r).getItem())
            && ItemStack.matches(this.H, this.Q.getCarried())
            && (ItemStack.isSameItemSameComponents(var2, this.H) || cg.n(var2, this.H))
            && this.Q.getSlot(this.r).mayPlace(this.H)
            && var2.getCount() + this.H.getCount() <= Math.min(this.Q.getSlot(this.r).getMaxStackSize(this.H), this.H.getMaxStackSize())) {
            this.m.set(this.r, var2.copyWithCount(this.L.returned().slotCount()));
            this.h = ItemStack.EMPTY;
            this.L = null;
            this.d.W(this.Q, this.r, this.O, this.p);
            this.P = this.K = (boolean)1;

            try {
               this.Z.accept(this.r, 0);
            } catch (RuntimeException var7) {
               this.y = "surplus click failed; no replay: " + var7.getClass().getSimpleName();
            } finally {
               this.P = this.K = (boolean)0;
               this.Z = null;
            }

            return false;
         } else {
            this.y = "unsafe authoritative surplus return; no replay";
            return false;
         }
      } else {
         if (var1 && this.d.E()) {
            this.y = "batch acknowledgement timeout; no replay " + this.d.y();
         }

         return false;
      }
   }

   bx.i d(Minecraft var1, int var2, int var3, boolean var4) {
      return this.Z(var1, var2, var3, var4).batch();
   }

   bx.i K(Minecraft var1, int var2, int var3) {
      return this.J(var1, var2, var3, false, true).batch();
   }

   ug.r Z(Minecraft var1, int var2, int var3, boolean var4) {
      return this.J(var1, var2, var3, var4, false);
   }

   ug.r J(Minecraft var1, int var2, int var3, boolean var4, boolean var5) {
      if (!this.W) {
         return new ug.r(null, "authoritative inventory missing");
      }

      if (var2 >= 0 && var3 >= 0 && var2 < this.F.size() && var3 < this.F.size()) {
         Slot var6 = this.Q.getSlot(var2);
         Slot var7 = this.Q.getSlot(var3);
         ItemStack var8 = this.F.get(var2);
         ItemStack var9 = var4 ? ItemStack.EMPTY : this.F.get(var3);
         ItemStack var10 = var6.getItem();
         ItemStack var11 = var4 ? ItemStack.EMPTY : var7.getItem();
         int var12 = !var9.isEmpty() && !ItemStack.isSameItemSameComponents(var8, var9) && (!var5 || !cg.n(var8, var9)) ? 0 : 1;
         int var13 = !var11.isEmpty() && !ItemStack.isSameItemSameComponents(var10, var11) && (!var5 || !cg.n(var10, var11)) ? 0 : 1;
         int var14 = var12 != 0 && var13 != 0 ? 1 : 0;
         bx.i var15 = bx.batch(
            var2,
            var3,
            var8.getCount(),
            var9.getCount(),
            (boolean)var14,
            Math.min(var6.getMaxStackSize(var8), var8.getMaxStackSize()),
            Math.min(var7.getMaxStackSize(var8), var8.getMaxStackSize())
         );
         bx.i var16 = bx.batch(
            var2,
            var3,
            var10.getCount(),
            var11.getCount(),
            (boolean)var14,
            Math.min(var6.getMaxStackSize(var10), var10.getMaxStackSize()),
            Math.min(var7.getMaxStackSize(var10), var10.getMaxStackSize())
         );
         String var17 = null;
         if (var2 == var3) {
            var17 = "source equals destination";
         } else if (var14 == 0) {
            var17 = (var12 == 0 ? "server merge mismatch " + D(var8, var9) : "")
               + (var12 == 0 && var13 == 0 ? "; " : "")
               + (var13 == 0 ? "local merge mismatch " + D(var10, var11) : "");
         } else if (var15 == null) {
            var17 = "server count/capacity rejected";
         } else if (var16 == null) {
            var17 = "local count/capacity rejected";
         } else if (!var15.equals(var16)) {
            var17 = "local/server plan divergence server=" + var15 + " local=" + var16;
         } else if (!var6.mayPickup(var1.player)) {
            var17 = "source take permission denied";
         } else if (!var7.mayPlace(var8)) {
            var17 = "destination insert permission denied (server stack)";
         } else if (!var7.mayPlace(var10)) {
            var17 = "destination insert permission denied (local stack)";
         } else if (var15.clicks() == 3 && !var6.mayPlace(var8)) {
            var17 = "source remainder insert permission denied (server stack)";
         } else if (var15.clicks() == 3 && !var6.mayPlace(var10)) {
            var17 = "source remainder insert permission denied (local stack)";
         }

         return new ug.r(var17 == null ? var15 : null, var17 == null ? "ok" : var17);
      } else {
         return new ug.r(null, "source/destination slot unavailable");
      }
   }

   private static String D(ItemStack var0, ItemStack var1) {
      HashSet var2 = new HashSet(var0.getComponents().keySet());
      var2.addAll(var1.getComponents().keySet());
      TreeSet var3 = new TreeSet();

      for (DataComponentType var5 : var2) {
         if (!Objects.equals(var0.getComponents().get(var5), var1.getComponents().get(var5))) {
            var3.add(String.valueOf(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(var5)));
         }
      }

      return "itemEqual="
         + var0.is(var1.getItem())
         + " componentKeys="
         + var3
         + (var3.contains("minecraft:lore") ? " loreA=" + cg.C(var0) + " loreB=" + cg.C(var1) : "");
   }

   String o(int var1) {
      if (var1 == -1) {
         return "cursor server{" + M(this.H, null) + "} local{" + M(this.Q.getCarried(), null) + "} server/local " + D(this.H, this.Q.getCarried());
      } else if (this.W && var1 >= 0 && var1 < this.F.size()) {
         Slot var2 = this.Q.getSlot(var1);
         ItemStack var3 = this.F.get(var1);
         ItemStack var4 = var2.getItem();
         return "slot=" + var1 + " server{" + M(var3, var2) + "} local{" + M(var4, var2) + "} server/local " + D(var3, var4);
      } else {
         return "slot=" + var1 + " unavailable";
      }
   }

   private static String M(ItemStack var0, Slot var1) {
      MapId var2 = (MapId)var0.get(DataComponents.MAP_ID);
      return "map="
         + (var2 == null ? -1 : var2.id())
         + " count="
         + var0.getCount()
         + (var1 == null ? "" : " limit=" + Math.min(var1.getMaxStackSize(var0), var0.getMaxStackSize()));
   }

   String j(int var1, int var2) {
      Slot var3 = this.Q.getSlot(var1);
      Slot var4 = this.Q.getSlot(var2);
      ItemStack var5 = this.F.get(var1);
      ItemStack var6 = var3.getItem();
      return "effectiveLimits(server/local) source="
         + Math.min(var3.getMaxStackSize(var5), var5.getMaxStackSize())
         + "/"
         + Math.min(var3.getMaxStackSize(var6), var6.getMaxStackSize())
         + " destination="
         + Math.min(var4.getMaxStackSize(var5), var5.getMaxStackSize())
         + "/"
         + Math.min(var4.getMaxStackSize(var6), var6.getMaxStackSize());
   }

   boolean w(Minecraft var1, int var2, int var3, String var4) {
      return this.b(var1, var2, var3, var4, false);
   }

   boolean b(Minecraft var1, int var2, int var3, String var4, boolean var5) {
      if (this.z || this.y != null) {
         return false;
      } else if (var1.gameMode == null) {
         this.y = "interaction manager unavailable; no replay";
         return false;
      } else {
         ClientPacketListener var6 = var1.getConnection();
         PlayerInfo var7 = var6 == null ? null : var6.getPlayerInfo(var1.player.getUUID());
         this.d.Y(var7 == null ? 0 : var7.getLatency());
         bx.i var8 = this.J(var1, var2, var3, false, var5).batch();
         if (this.X() && var8 != null && var1.player.containerMenu == this.Q) {
            te.R("SWAP " + var4 + " batch clicks=" + var8.clicks() + " source=" + var2 + " destination=" + var3);
            return this.t(var2, var3, var8, var4, (var2x, var3x) -> {
               if (var1.player.containerMenu != this.Q) {
                  throw new IllegalStateException("handler lost");
               }

               var1.gameMode.handleContainerInput(this.Q.containerId, var2x, var3x, ContainerInput.PICKUP, var1.player);
            }, var5);
         } else {
            this.y = "unsafe pre-batch ledger, permission, capacity or vanilla merge";
            return false;
         }
      }
   }

   boolean w(int var1, int var2, bx.i var3, String var4, BiConsumer<Integer, Integer> var5) {
      return this.t(var1, var2, var3, var4, var5, false);
   }

   boolean t(int var1, int var2, bx.i var3, String var4, BiConsumer<Integer, Integer> var5, boolean var6) {
      if (!this.z && this.y == null && var3 != null && this.X()) {
         ItemStack var7 = this.F.get(var1);
         ItemStack var8 = this.F.get(var2);
         ItemStack var9 = this.Q.getSlot(var1).getItem().copy();
         ItemStack var10 = this.Q.getSlot(var2).getItem().copy();
         this.g = (boolean)(!var6 || var8.isEmpty() || ItemStack.isSameItemSameComponents(var7, var8) && ItemStack.isSameItemSameComponents(var9, var10)
            ? 0
            : 1);
         if (!this.g
            || (ItemStack.isSameItemSameComponents(var7, var8) || cg.n(var7, var8)) && (ItemStack.isSameItemSameComponents(var9, var10) || cg.n(var9, var10))) {
            if (this.m.isEmpty()) {
               for (ItemStack var12 : this.F) {
                  this.m.add(var12.copy());
               }
            }

            this.G.add(var1);
            this.G.add(var2);
            this.L = this.g && var3.clicks() == 3 ? var3 : null;
            this.Z = this.L == null ? null : var5;
            this.h = this.L == null ? ItemStack.EMPTY : var7.copyWithCount(var3.placement().cursorCount());
            this.m.set(var1, var7.copyWithCount(this.L == null ? var3.returned().slotCount() : var3.pickup().slotCount()));
            this.m.set(var2, (var8.isEmpty() ? var7 : var8).copyWithCount(var3.placement().slotCount()));
            this.r = var1;
            this.O = var2;
            this.P("issue " + var4 + " source=" + var1 + " destination=" + var2 + " clicks=" + var3.clicks());
            this.z = this.N = (boolean)1;
            this.d.W(this.Q, var1, var2, this.p);
            return var3.execute(
                  var7x -> {
                     this.P = (boolean)1;
                     this.K = (boolean)(var7x == (this.g ? 1 : var3.clicks() - 1) ? 1 : 0);

                     try {
                        this.P("click call index=" + var7x + " probe=" + this.K + " revision=" + this.Q.getStateId());
                        var5.accept(var7x == 1 ? var2 : var1, var7x == 0 ? var3.pickupButton() : (var7x == 1 ? 1 : 0));
                     } catch (RuntimeException var13) {
                        this.y = "batch click failed; no replay: " + var13.getClass().getSimpleName();
                        return false;
                     } finally {
                        this.P = this.K = (boolean)0;
                     }

                     if (this.g && var7x == 1) {
                        return false;
                     }

                     this.P("click returned index=" + var7x);
                     int var8x = var7x == 2 ? var3.returned().slotCount() : var3.pickup().slotCount();
                     int var9x = var7x == 0 ? var10.getCount() : var3.placement().slotCount();
                     int var10x = var7x == 0 ? var3.pickup().cursorCount() : (var7x == 1 ? var3.placement().cursorCount() : 0);
                     if (ItemStack.matches(this.Q.getSlot(var1).getItem(), var9.copyWithCount(var8x))
                        && ItemStack.matches(this.Q.getSlot(var2).getItem(), (var10.isEmpty() ? var9 : var10).copyWithCount(var9x))
                        && ItemStack.matches(this.Q.getCarried(), var9.copyWithCount(var10x))) {
                        return true;
                     }

                     this.y = "batch prediction diverged after click=" + var7x + "; no replay";
                     return false;
                  }
               )
               || this.g && this.y == null;
         } else {
            this.y = "worth return policy rejected before clicks; no replay";
            return false;
         }
      } else {
         return false;
      }
   }

   record r(bx.i batch, String reason) {
   }
}
