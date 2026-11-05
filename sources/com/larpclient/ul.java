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

final class ul {
   final AbstractContainerMenu x;
   final List<ItemStack> k = new ArrayList<>();
   private final List<ItemStack> Q = new ArrayList<>();
   private final Set<Integer> X = new HashSet<>();
   private final w3 o = new w3();
   private ItemStack B = ItemStack.EMPTY;
   private ItemStack c = ItemStack.EMPTY;
   private m.y U;
   private BiConsumer<Integer, Integer> F;
   private boolean m;
   private long z;
   private int f;
   private int u;
   boolean y;
   boolean j;
   boolean P;
   boolean d;
   boolean w;
   String s;
   private final long g = System.nanoTime();
   private final ArrayDeque<String> V = new ArrayDeque<>();

   void K(String var1) {
      if (this.V.size() == 48) {
         this.V.removeFirst();
      }

      this.V.addLast((System.nanoTime() - this.g) / 1000000L + "ms " + var1);
   }

   String L() {
      return "sync="
         + this.x.containerId
         + " revision="
         + this.x.getStateId()
         + " pending="
         + this.y
         + " ack["
         + this.o.a()
         + "] source{"
         + this.t(this.f)
         + "} destination{"
         + this.t(this.u)
         + "} "
         + this.t(-1)
         + " events="
         + this.V;
   }

   ul(AbstractContainerMenu var1) {
      this.x = var1;
   }

   ul I() {
      ul var1 = new ul(this.x);
      if (this.d) {
         var1.N(this.k, this.B);
      }

      return var1;
   }

   void N(List<ItemStack> var1, ItemStack var2) {
      if (var1.size() == this.x.slots.size()) {
         this.k.clear();

         for (ItemStack var4 : var1) {
            this.k.add(var4.copy());
         }

         this.B = var2.copy();
         this.d = (boolean)1;
         this.o.z(this.x, ++this.z, true, -1, true);
      }
   }

   void L(int var1, ItemStack var2) {
      if (this.d) {
         if (var1 == -1) {
            this.B = var2.copy();
         } else {
            if (var1 < 0 || var1 >= this.k.size()) {
               return;
            }

            this.k.set(var1, var2.copy());
         }

         this.o.z(this.x, ++this.z, false, var1, var1 == -1);
      }
   }

   boolean T() {
      if (this.d && wm.d(this.B, this.c) && wm.d(this.x.getCarried(), this.c)) {
         for (int var1 = 0; var1 < this.k.size(); var1++) {
            if (!wm.d(this.k.get(var1), this.x.getSlot(var1).getItem()) || this.X.contains(var1) && !wm.d(this.k.get(var1), this.Q.get(var1))) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   ItemStack T(int var1) {
      return this.Q.get(var1);
   }

   boolean I(int var1) {
      return this.X.contains(var1);
   }

   boolean r() {
      return this.d && !this.y && this.s == null && this.B.isEmpty() && this.x.getCarried().isEmpty();
   }

   boolean w() {
      return this.i(true);
   }

   boolean i(boolean var1) {
      if (this.j) {
         return false;
      }

      if (this.s != null) {
         return false;
      }

      if (!this.y) {
         return true;
      }

      if (this.o.p(wm.d(this.k.get(this.f), this.Q.get(this.f)), wm.d(this.k.get(this.u), this.Q.get(this.u)), wm.d(this.B, this.c))
         && (!this.m || this.o.d())
         && this.T()) {
         if (this.U == null) {
            this.y = (boolean)0;
            return true;
         }

         ItemStack var2 = this.k.get(this.f);
         if (ItemStack.matches(var2, this.x.getSlot(this.f).getItem())
            && ItemStack.matches(this.B, this.x.getCarried())
            && (ItemStack.isSameItemSameComponents(var2, this.B) || wg.s(var2, this.B))
            && this.x.getSlot(this.f).mayPlace(this.B)
            && var2.getCount() + this.B.getCount() <= Math.min(this.x.getSlot(this.f).getMaxStackSize(this.B), this.B.getMaxStackSize())) {
            this.Q.set(this.f, var2.copyWithCount(this.U.returned().slotCount()));
            this.c = ItemStack.EMPTY;
            this.U = null;
            this.o.L(this.x, this.f, this.u, this.z);
            this.j = this.P = (boolean)1;

            try {
               this.F.accept(this.f, 0);
            } catch (RuntimeException var7) {
               this.s = "surplus click failed; no replay: " + var7.getClass().getSimpleName();
            } finally {
               this.j = this.P = (boolean)0;
               this.F = null;
            }

            return false;
         } else {
            this.s = "unsafe authoritative surplus return; no replay";
            return false;
         }
      } else {
         if (var1 && this.o.g()) {
            this.s = "batch acknowledgement timeout; no replay " + this.o.a();
         }

         return false;
      }
   }

   m.y S(Minecraft var1, int var2, int var3, boolean var4) {
      return this.t(var1, var2, var3, var4).batch();
   }

   m.y N(Minecraft var1, int var2, int var3) {
      return this.J(var1, var2, var3, false, true).batch();
   }

   ul.z t(Minecraft var1, int var2, int var3, boolean var4) {
      return this.J(var1, var2, var3, var4, false);
   }

   ul.z J(Minecraft var1, int var2, int var3, boolean var4, boolean var5) {
      if (!this.d) {
         return new ul.z(null, "authoritative inventory missing");
      }

      if (var2 >= 0 && var3 >= 0 && var2 < this.k.size() && var3 < this.k.size()) {
         Slot var6 = this.x.getSlot(var2);
         Slot var7 = this.x.getSlot(var3);
         ItemStack var8 = this.k.get(var2);
         ItemStack var9 = var4 ? ItemStack.EMPTY : this.k.get(var3);
         ItemStack var10 = var6.getItem();
         ItemStack var11 = var4 ? ItemStack.EMPTY : var7.getItem();
         int var12 = !var9.isEmpty() && !ItemStack.isSameItemSameComponents(var8, var9) && (!var5 || !wg.s(var8, var9)) ? 0 : 1;
         int var13 = !var11.isEmpty() && !ItemStack.isSameItemSameComponents(var10, var11) && (!var5 || !wg.s(var10, var11)) ? 0 : 1;
         int var14 = var12 != 0 && var13 != 0 ? 1 : 0;
         m.y var15 = com.larpclient.m.batch(
            var2,
            var3,
            var8.getCount(),
            var9.getCount(),
            (boolean)var14,
            Math.min(var6.getMaxStackSize(var8), var8.getMaxStackSize()),
            Math.min(var7.getMaxStackSize(var8), var8.getMaxStackSize())
         );
         m.y var16 = com.larpclient.m.batch(
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
            var17 = (var12 == 0 ? "server merge mismatch " + d(var8, var9) : "")
               + (var12 == 0 && var13 == 0 ? "; " : "")
               + (var13 == 0 ? "local merge mismatch " + d(var10, var11) : "");
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

         return new ul.z(var17 == null ? var15 : null, var17 == null ? "ok" : var17);
      } else {
         return new ul.z(null, "source/destination slot unavailable");
      }
   }

   private static String d(ItemStack var0, ItemStack var1) {
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
         + (var3.contains("minecraft:lore") ? " loreA=" + wg.V(var0) + " loreB=" + wg.V(var1) : "");
   }

   String t(int var1) {
      if (var1 == -1) {
         return "cursor server{" + Q(this.B, null) + "} local{" + Q(this.x.getCarried(), null) + "} server/local " + d(this.B, this.x.getCarried());
      } else if (this.d && var1 >= 0 && var1 < this.k.size()) {
         Slot var2 = this.x.getSlot(var1);
         ItemStack var3 = this.k.get(var1);
         ItemStack var4 = var2.getItem();
         return "slot=" + var1 + " server{" + Q(var3, var2) + "} local{" + Q(var4, var2) + "} server/local " + d(var3, var4);
      } else {
         return "slot=" + var1 + " unavailable";
      }
   }

   private static String Q(ItemStack var0, Slot var1) {
      MapId var2 = (MapId)var0.get(DataComponents.MAP_ID);
      return "map="
         + (var2 == null ? -1 : var2.id())
         + " count="
         + var0.getCount()
         + (var1 == null ? "" : " limit=" + Math.min(var1.getMaxStackSize(var0), var0.getMaxStackSize()));
   }

   String F(int var1, int var2) {
      Slot var3 = this.x.getSlot(var1);
      Slot var4 = this.x.getSlot(var2);
      ItemStack var5 = this.k.get(var1);
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

   boolean m(Minecraft var1, int var2, int var3, String var4) {
      return this.Z(var1, var2, var3, var4, false);
   }

   boolean Z(Minecraft var1, int var2, int var3, String var4, boolean var5) {
      if (this.y || this.s != null) {
         return false;
      } else if (var1.gameMode == null) {
         this.s = "interaction manager unavailable; no replay";
         return false;
      } else {
         ClientPacketListener var6 = var1.getConnection();
         PlayerInfo var7 = var6 == null ? null : var6.getPlayerInfo(var1.player.getUUID());
         this.o.z(var7 == null ? 0 : var7.getLatency());
         m.y var8 = this.J(var1, var2, var3, false, var5).batch();
         if (this.T() && var8 != null && var1.player.containerMenu == this.x) {
            xa.i("SWAP " + var4 + " batch clicks=" + var8.clicks() + " source=" + var2 + " destination=" + var3);
            return this.d(var2, var3, var8, var4, (var2x, var3x) -> {
               if (var1.player.containerMenu != this.x) {
                  throw new IllegalStateException("handler lost");
               }

               var1.gameMode.handleContainerInput(this.x.containerId, var2x, var3x, ContainerInput.PICKUP, var1.player);
            }, var5);
         } else {
            this.s = "unsafe pre-batch ledger, permission, capacity or vanilla merge";
            return false;
         }
      }
   }

   boolean s(int var1, int var2, m.y var3, String var4, BiConsumer<Integer, Integer> var5) {
      return this.d(var1, var2, var3, var4, var5, false);
   }

   boolean d(int var1, int var2, m.y var3, String var4, BiConsumer<Integer, Integer> var5, boolean var6) {
      if (!this.y && this.s == null && var3 != null && this.T()) {
         ItemStack var7 = this.k.get(var1);
         ItemStack var8 = this.k.get(var2);
         ItemStack var9 = this.x.getSlot(var1).getItem().copy();
         ItemStack var10 = this.x.getSlot(var2).getItem().copy();
         this.m = (boolean)(!var6 || var8.isEmpty() || ItemStack.isSameItemSameComponents(var7, var8) && ItemStack.isSameItemSameComponents(var9, var10)
            ? 0
            : 1);
         if (!this.m
            || (ItemStack.isSameItemSameComponents(var7, var8) || wg.s(var7, var8)) && (ItemStack.isSameItemSameComponents(var9, var10) || wg.s(var9, var10))) {
            if (this.Q.isEmpty()) {
               for (ItemStack var12 : this.k) {
                  this.Q.add(var12.copy());
               }
            }

            this.X.add(var1);
            this.X.add(var2);
            this.U = this.m && var3.clicks() == 3 ? var3 : null;
            this.F = this.U == null ? null : var5;
            this.c = this.U == null ? ItemStack.EMPTY : var7.copyWithCount(var3.placement().cursorCount());
            this.Q.set(var1, var7.copyWithCount(this.U == null ? var3.returned().slotCount() : var3.pickup().slotCount()));
            this.Q.set(var2, (var8.isEmpty() ? var7 : var8).copyWithCount(var3.placement().slotCount()));
            this.f = var1;
            this.u = var2;
            this.K("issue " + var4 + " source=" + var1 + " destination=" + var2 + " clicks=" + var3.clicks());
            this.y = this.w = (boolean)1;
            this.o.L(this.x, var1, var2, this.z);
            return var3.execute(
                  var7x -> {
                     this.j = (boolean)1;
                     this.P = (boolean)(var7x == (this.m ? 1 : var3.clicks() - 1) ? 1 : 0);

                     try {
                        this.K("click call index=" + var7x + " probe=" + this.P + " revision=" + this.x.getStateId());
                        var5.accept(var7x == 1 ? var2 : var1, var7x == 0 ? var3.pickupButton() : (var7x == 1 ? 1 : 0));
                     } catch (RuntimeException var13) {
                        this.s = "batch click failed; no replay: " + var13.getClass().getSimpleName();
                        return false;
                     } finally {
                        this.j = this.P = (boolean)0;
                     }

                     if (this.m && var7x == 1) {
                        return false;
                     }

                     this.K("click returned index=" + var7x);
                     int var8x = var7x == 2 ? var3.returned().slotCount() : var3.pickup().slotCount();
                     int var9x = var7x == 0 ? var10.getCount() : var3.placement().slotCount();
                     int var10x = var7x == 0 ? var3.pickup().cursorCount() : (var7x == 1 ? var3.placement().cursorCount() : 0);
                     if (ItemStack.matches(this.x.getSlot(var1).getItem(), var9.copyWithCount(var8x))
                        && ItemStack.matches(this.x.getSlot(var2).getItem(), (var10.isEmpty() ? var9 : var10).copyWithCount(var9x))
                        && ItemStack.matches(this.x.getCarried(), var9.copyWithCount(var10x))) {
                        return true;
                     }

                     this.s = "batch prediction diverged after click=" + var7x + "; no replay";
                     return false;
                  }
               )
               || this.m && this.s == null;
         } else {
            this.s = "worth return policy rejected before clicks; no replay";
            return false;
         }
      } else {
         return false;
      }
   }

   record z(m.y batch, String reason) {
   }
}
