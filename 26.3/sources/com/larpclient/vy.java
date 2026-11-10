package com.larpclient;

import com.larp.obf.q41a5851bcf8e876a.q2d3f5242fd3d8828.qa6b3f1c381a58196;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.function.IntConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

@Environment(EnvType.CLIENT)
public class vy {
   private static final vy ug = new vy();
   private static final String j = "";
   private static final int uj = 660;
   private static final int[] v = new int[]{3, 6, 10, 15, 22, 30, 40, 52, 66, 82, 100, 120, 150, 180};
   private static final int k = 100;
   private static final int M = 1200;
   private static final int ue = 6;
   private static final int l = 16;
   private static final int u8 = 40;
   private final Deque<Integer> u = new ArrayDeque<>();
   private final Map<Integer, MapId> I = new HashMap<>();
   private final Map<Integer, Integer> o = new HashMap<>();
   private final Map<String, Map<Integer, Integer>> R = new HashMap<>();
   private final Map<String, Map<Integer, Integer>> d = new HashMap<>();
   private Map<Integer, Integer> uL = new HashMap<>();
   private final int[] n = new int[36];
   private final int[] c = new int[36];
   private int Z = -1;
   private int X = -1;
   private Map<Integer, Integer> a;
   private int[] uP;
   private int[] uN;
   private boolean u7;
   private String uq;
   private vy.y S = vy.y.m;
   private int e = -1;
   private int U = -1;
   private int uY = -1;
   private int p = -1;
   private int P = -1;
   private int i = -1;
   private int W;
   private int s;
   private int x;
   private int us = -1;
   private int uv;
   private int h;
   private int F;
   private int V = -1;
   private long b;
   private boolean m;
   private boolean K;
   private int N = -1;
   private ItemStack C;
   private int r;
   private int q = -1;
   private int t;
   private int B;
   private int L;
   private int z = -1;
   private int Y = -1;
   private int f = -1;
   private ItemStack D;
   private BlockPos H;
   private p9 G;
   private p5 u4 = new p5();
   private int uh = -1;
   private int g = -1;
   private Map<Integer, ItemStack> J = Map.of();
   private int w = -1;
   private boolean E;
   private ClientLevel O;
   private long y = -1L;
   private final Map<Integer, Integer> T = new HashMap<>();
   private int Q = -20;
   private boolean A;

   protected vy() {
   }

   public static vy w() {
      return ug;
   }

   public static boolean t() {
      return a().isSwapActive();
   }

   public static vy a() {
      vy var0 = w();
      vh var1 = vh.R();
      vf var2 = vf.x();
      vz var3 = vz.E();
      v2 var4 = v2.L();
      if (var0.isSwapActive()) {
         return var0;
      } else if (var1.isSwapActive()) {
         return var1;
      } else if (var2.isSwapActive()) {
         return var2;
      } else if (var3.isSwapActive()) {
         return var3;
      } else if (var4.isSwapActive()) {
         return var4;
      } else if (var0.hasQueuedForce()) {
         return var0;
      } else if (var1.hasQueuedForce()) {
         return var1;
      } else if (var2.hasQueuedForce()) {
         return var2;
      } else if (var3.hasQueuedForce()) {
         return var3;
      } else if (var4.hasQueuedForce()) {
         return var4;
      } else if (vh.R().B()) {
         return var1;
      } else if (var0.isDealtHandLocked()) {
         return var0;
      } else if (var1.isDealtHandLocked()) {
         return var1;
      } else if (var2.isDealtHandLocked()) {
         return var2;
      } else if (var3.isDealtHandLocked()) {
         return var3;
      } else if (var4.isDealtHandLocked()) {
         return var4;
      } else if (var0.J()) {
         return var0;
      } else if (var1.J()) {
         return var1;
      } else if (var2.J()) {
         return var2;
      } else if (var3.J()) {
         return var3;
      } else {
         return var4.J() ? var4 : var0;
      }
   }

   private boolean J() {
      return this.S != vy.y.m || this.z >= 0 || this.Y >= 1 || this.q >= 0;
   }

   public static vy x() {
      vy var0 = a();
      if (var0.isSwapActive()) {
         return var0;
      }

      if (ug.G != null) {
         return ug;
      }

      vf var1 = vf.x();
      return var1.G != null ? var1 : vh.R();
   }

   private boolean z() {
      return this.S == vy.y.m && this.z < 0 && this.Y < 1 && !this.u.isEmpty();
   }

   public void queueForce(int var1) {
      this.queueForceSequence(var1);
   }

   public void queueForceSequence(int... var1) {
      if (this.isSwapBusy()) {
         if (!this.u7) {
            bn.b("SWAP " + this.J() + " queue replacement rejected while busy");
            this.u7 = (boolean)1;
         }
      } else {
         this.u7 = (boolean)0;
         this.u.clear();
         this.y();
         if (var1 != null) {
            int var2 = Math.max(1, this.maxQueuedForces());

            for (int var6 : var1) {
               if (this.u.size() >= var2) {
                  break;
               }

               if (var6 >= 1 && var6 <= 10 && this.getTemplateForValue(var6) != null) {
                  this.u.addLast(var6);
               }
            }
         }

         if (this.u.isEmpty()) {
            this.uL.clear();
         } else {
            this.K();
         }
      }
   }

   private void K() {
      this.uL = this.T();
      Minecraft var1 = Minecraft.getInstance();

      for (int var2 = 0; var2 < 36; var2++) {
         ItemStack var3 = var1.player == null ? ItemStack.EMPTY : var1.player.getInventory().getItem(var2);
         this.n[var2] = this.V(var3);
         this.c[var2] = var3.getCount();
      }

      this.Z = -1;
   }

   private int h(Minecraft var1, int var2) {
      if (this.T().getOrDefault(var2, 0) <= this.uL.getOrDefault(var2, 0)) {
         return -1;
      }

      int[] var3 = new int[36];
      int[] var4 = new int[36];

      for (int var5 = 0; var5 < 36; var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         var3[var5] = this.V(var6);
         var4[var5] = var6.getCount();
      }

      return hv.dealtSlot(var2, this.n, this.c, var3, var4, this.Z);
   }

   public void clearQueue() {
      this.abortSwap();
      this.u.clear();
      this.uL.clear();
      this.u7 = (boolean)0;
   }

   public int getPendingValue() {
      Integer var1 = this.u.peekFirst();
      return var1 == null ? -1 : var1;
   }

   public int getQueuedValueAt(int var1) {
      if (var1 < 0) {
         return -1;
      }

      int var2 = 0;

      for (Integer var4 : this.u) {
         if (var2++ == var1) {
            return var4;
         }
      }

      return -1;
   }

   public boolean hasQueuedForce() {
      return !this.u.isEmpty();
   }

   public String pendingLabel() {
      if (this.u.isEmpty()) {
         return "none";
      }

      StringBuilder var1 = new StringBuilder();

      for (Integer var3 : this.u) {
         if (!var1.isEmpty()) {
            var1.append(" -> ");
         }

         var1.append(this.O(var3));
      }

      return var1.toString();
   }

   public void tick(Minecraft var1) {
      if (var1 != null && var1.player != null) {
         if (var1.level != null) {
            if (this.O != var1.level) {
               this.O = var1.level;
               this.y = var1.level.getGameTime();
            } else if (var1.level.getGameTime() == this.y) {
               return;
            }
         }

         this.y = var1.level == null ? -1L : var1.level.getGameTime();
         if (this.F > 0) {
            this.F--;
         }

         if (this.r > 0) {
            this.r--;
         }

         if (this.uv > 0) {
            this.uv--;
         }

         if (this.S != vy.y.m) {
            this.I(var1);
         } else {
            if (this.L > 0 && --this.L == 0) {
               this.j("post-swap settle");
            }

            if (this.q >= 0) {
               int var2 = this.q;
               int var3 = this.t;
               int var4 = this.B;
               if (this.u.isEmpty() || this.u.peekFirst() != var4) {
                  this.P();
               } else if (this.x(var1, var2) < 0) {
                  this.P();
               } else if (this.uv <= 0) {
                  if (var3 == var4) {
                     this.J(var3, var3);
                     bn.b("SWAP " + this.J() + " deferred deal already matched " + this.O(var4));
                     this.P();
                     this.y();
                     this.x();
                     return;
                  }

                  if (!this.swapWorthwhile(var3, var4)) {
                     this.J(var3, var3);
                     bn.b("SWAP " + this.J() + " deferred skip real=" + this.O(var3) + " armed=" + this.O(var4));
                     this.P();
                     this.y();
                     this.x();
                     return;
                  }

                  if (this.Q(var1, var2, var3)) {
                     return;
                  }
               }

               if (this.q >= 0) {
                  return;
               }
            }

            if (this.S == vy.y.m && this.z >= 0 && this.q < 0 && this.x(var1, this.z) < 0) {
               this.y();
            }

            if (M(var1.gui.screen()) && var1.player.containerMenu instanceof DispenserMenu) {
               this.scanCurrentContainerTemplateSlots(var1);
            }

            if (!this.u.isEmpty()) {
               if (this.K()) {
                  this.n(var1);
               } else if (this.z >= 0 && this.S == vy.y.m && this.q < 0 && this.uv <= 0) {
                  this.G(var1);
               }
            }
         }
      } else {
         this.abortSwap();
         this.clearQueue();
         this.O = null;
         this.y = -1L;
      }
   }

   private void G(Minecraft var1) {
      int var2 = this.z;
      if (var2 >= 0 && !this.u.isEmpty()) {
         int var3 = this.h(new MapId(var2));
         if (var3 < 1) {
            this.y();
         } else {
            int var4 = this.u.peekFirst();
            bn.b("SWAP " + this.J() + " deal real=" + this.O(var3) + " (visual) map=" + var2 + " pending=" + this.O(var4));
            if (var3 == var4) {
               this.J(var3, var3);
               this.y();
               this.x();
            } else if (!this.swapWorthwhile(var3, var4)) {
               this.J(var3, var3);
               this.y();
               this.x();
            } else if (this.uv <= 0 && !M(var1.gui.screen())) {
               this.j();
               if (!this.Q(var1, var2, var3) && this.x(var1, var2) >= 0) {
                  this.q = var2;
                  this.t = var3;
                  this.B = var4;
                  this.y();
               }
            }
         }
      } else {
         this.y();
      }
   }

   public void setTemplateForValue(int var1, MapId var2) {
      if (var1 >= 1 && var1 <= 10) {
         if (var2 == null) {
            this.I.remove(var1);
            this.o.remove(var1);
         } else {
            this.I.put(var1, var2);
            this.o.put(var1, var2.id());
         }
      }
   }

   public MapId getTemplateForValue(int var1) {
      q5 var2 = q5.a();
      if (var2.N() && var2.G()) {
         int var3 = var2.R(var1);
         return var3 < 0 ? null : new MapId(var3);
      } else {
         return var1 >= 1 && var1 <= 10 ? this.I.get(var1) : null;
      }
   }

   public int getTemplateIdForValue(int var1) {
      q5 var2 = q5.a();
      return var2.N() && var2.G() ? var2.R(var1) : this.o.getOrDefault(var1, -1);
   }

   public void setTemplateIdForValue(int var1, int var2) {
      if (var1 >= 1 && var1 <= 10) {
         this.setTemplateForValue(var1, var2 < 0 ? null : new MapId(var2));
      }
   }

   public int countTemplates() {
      return this.I.size();
   }

   public boolean o(MapId var1) {
      return this.h(var1) >= 1;
   }

   public void setContainerContext(String var1) {
      if (!Objects.equals(this.uq, var1) && this.G != null) {
         this.X(Minecraft.getInstance(), "dispenser context changed");
      }

      this.uq = var1;
      this.uL.clear();
      this.y();
   }

   public String w() {
      return this.uq;
   }

   public int n(int var1) {
      return this.a().getOrDefault(var1, -1);
   }

   public boolean scanCurrentContainerTemplateSlots(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.player.containerMenu instanceof DispenserMenu var2) {
         Map var11 = this.a();
         Map var4 = this.M();
         var11.clear();
         var4.clear();
         byte var5 = 0;

         for (Slot var7 : var2.slots) {
            if (var7 != null && !(var7.container instanceof Inventory)) {
               ItemStack var8 = var7.getItem();
               if (var8 != null && !var8.isEmpty() && var8.is(Items.FILLED_MAP)) {
                  MapId var9 = (MapId)var8.getComponents().get(DataComponents.MAP_ID);
                  int var10 = var9 == null ? -1 : this.h(var9);
                  if (var9 != null) {
                     var4.put(var9.id(), var7.index);
                  }

                  if (var10 > 0) {
                     var11.put(var10, var7.index);
                     var5 = 1;
                  }
               }
            }
         }

         return (boolean)var5;
      } else {
         return false;
      }
   }

   public int B(String var1, ItemStack var2) {
      if (var2 != null) {
         var2 = qm.v(var1, var2);
      }

      return var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP) ? this.h((MapId)var2.getComponents().get(DataComponents.MAP_ID)) : -1;
   }

   public int V(MapId var1) {
      return this.h(var1);
   }

   public int containerIndexForValue(int var1) {
      if (var1 == 1) {
         return 0;
      } else {
         return var1 >= 3 && var1 <= 10 ? var1 - 2 : -1;
      }
   }

   protected int maxQueuedForces() {
      return 10;
   }

   private void J(int var1, int var2) {
      this.c(var1, var2, false);
   }

   private void c(int var1, int var2, boolean var3) {
      bn.N(this.J(), var1, var2, this.getTemplateIdForValue(var2), var3);
      if (this.getClass() == vy.class || this instanceof vf || this instanceof v2) {
         this.T.merge(this.getTemplateIdForValue(var3 ? var1 : var2), 1, Integer::sum);
      }
   }

   public void l(int var1, int var2) {
      int var3 = this.T.getOrDefault(var2, 0);
      if (var3 > 0) {
         if (var3 == 1) {
            this.T.remove(var2);
         } else {
            this.T.put(var2, var3 - 1);
         }
      } else {
         bn.j(this.J(), bn.W(bn.I(this.J(), var1), Integer.toString(var1), false, false));
      }
   }

   public void n() {
      this.T.clear();
   }

   protected String J() {
      if (this instanceof vh) {
         return "Higher/Lower";
      } else if (this instanceof vz) {
         return "Odd/Even";
      } else if (this instanceof vf) {
         return "Baccarat";
      } else {
         return this instanceof v2 ? "Mexican Jack" : "Blackjack";
      }
   }

   protected String O(int var1) {
      return switch (var1) {
         case 1 -> "A";
         case 10 -> "10";
         default -> String.valueOf(var1);
      };
   }

   public boolean isSwapActive() {
      return this.S != vy.y.m;
   }

   public boolean isSwapJuggling() {
      return (boolean)(switch (this.S) {
         case e, E, Q, A -> 1;
         default -> 0;
      });
   }

   public boolean isSwapFreezing() {
      if (this.r > 0) {
         return true;
      }

      return (boolean)(switch (this.S) {
         case E, Q, A -> 1;
         default -> 0;
      });
   }

   public boolean isSwapFreezingPassThrough() {
      return this.F > 0;
   }

   public void signalFramePlacement(ItemFrame var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null) {
         ItemStack var3 = var2.player.getMainHandItem();
         if (var3.is(Items.FILLED_MAP) && var1.getItem().isEmpty() && this.S != vy.y.m && this.V(var3) == this.getTemplateIdForValue(this.e)) {
            this.V = var1.getId();
         }

         this.p();
         this.T();
         this.F = 40;
         we.r("PLACEMENT frame=" + var1.getId() + " map=" + this.V(var3) + " phase=" + this.S + " vanilla animation");
      }
   }

   private boolean s(Minecraft var1, int var2) {
      return this.V >= 0 && var1.level != null && var1.level.getEntity(this.V) instanceof ItemFrame var3 && this.V(var3.getItem()) == var2;
   }

   public boolean j(ItemStack var1) {
      if (this.isSwapJuggling()) {
         return true;
      } else if (!this.isSwapActive()) {
         return false;
      } else {
         Minecraft var2 = Minecraft.getInstance();
         if (var2.player != null && this.getInventorySlotProjection(var2.player.getInventory().getSelectedSlot()) != null) {
            return true;
         } else {
            return var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP) ? this.getVisualOverride(var1) != null : false;
         }
      }
   }

   public boolean isSwapBusy() {
      return this.isSwapActive() || this.q >= 0;
   }

   public boolean isSwapSuppressing() {
      return this.S != vy.y.m ? true : this.i >= 0 && !this.u.isEmpty();
   }

   public boolean isDealBobAllowed(ItemStack var1) {
      return this.a(var1, true);
   }

   public boolean isHotbarBobAllowed(ItemStack var1) {
      return this.a(var1, false);
   }

   private boolean a(ItemStack var1, boolean var2) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         Minecraft var3 = Minecraft.getInstance();
         if (var3 != null && var3.player != null && var3.player.getOffhandItem() == var1) {
            return false;
         }

         if (this.S != vy.y.m) {
            return false;
         }

         int var4 = this.getTemplateIdForValue(this.e);
         if (var4 >= 0 && this.V(var1) == var4) {
            return false;
         }

         if (this.u.isEmpty()) {
            return true;
         }

         int var5 = this.u.peekFirst();
         int var6 = this.getTemplateIdForValue(var5);
         if (var6 >= 0 && this.V(var1) == var6) {
            return false;
         }

         if (var2) {
            this.B();
         }

         return true;
      } else {
         return true;
      }
   }

   private void B() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null) {
         if (!this.isDealtHandLocked()) {
            this.m = (boolean)1;
            this.N = var1.player.getInventory().getSelectedSlot();
            this.C = null;
            this.F = 0;
            this.b++;
         }

         this.K = (boolean)0;
         if (var1.player.getMainHandItem().is(Items.FILLED_MAP)) {
            this.getDealtHandVisual(var1.player.getMainHandItem());
         }
      }
   }

   private void p() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.firstPersonHandsAndItems() instanceof q2 var2) {
         var2.larpRig$releaseHeldMap(this, var1.player.getMainHandItem());
      }
   }

   public boolean W() {
      return this.getClass() == vy.class || this instanceof vf || this instanceof v2 || this instanceof vz;
   }

   private void j() {
      this.B();
      Minecraft var1 = Minecraft.getInstance();
      if (this.W() && var1.player != null && this.isDealtHandLocked() && var1.player.firstPersonHandsAndItems() instanceof q2 var2) {
         var2.larpRig$captureHeldMap(this, var1.player.getMainHandItem(), var1.player.getInventory().getSelectedSlot());
      }
   }

   private void T() {
      this.m = (boolean)0;
      this.K = (boolean)0;
      this.N = -1;
      this.C = null;
   }

   public boolean isDealtHandLocked() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.m && (var1.player == null || var1.player.getInventory().getSelectedSlot() != this.N)) {
         this.T();
      }

      return this.m;
   }

   public ItemStack getDealtHandVisual(ItemStack var1) {
      if (!this.isDealtHandLocked()) {
         return var1;
      }

      ItemStack var2 = this.getInventorySlotProjection(this.N);
      if (var2 != null) {
         this.C = var2;
         return var2;
      }

      if (!this.isDealtHandLocked()) {
         return var1;
      }

      if (this.K) {
         if (!var1.is(Items.FILLED_MAP) || this.C == null || this.V(var1) != this.V(this.C)) {
            this.T();
         }

         return var1;
      } else {
         if (var1.is(Items.FILLED_MAP)) {
            ItemStack var3 = this.getVisualOverride(var1);
            ItemStack var4 = var3 == null ? var1 : var3;
            if (this.C == null || this.V(this.C) != this.V(var4)) {
               this.C = var4.copy();
            }
         } else if (this.S == vy.y.m && this.r == 0 && this.Y < 1 && this.q < 0) {
            this.T();
            return var1;
         }

         return this.C == null ? var1 : this.C;
      }
   }

   public long getDealtHandSequence() {
      return this.b;
   }

   public boolean isDealtHandSettled() {
      return this.K;
   }

   public int getSwapValue() {
      return this.e;
   }

   public int getSwapRealMapId() {
      return this.uY;
   }

   public int Y() {
      return this.q;
   }

   public ItemStack getVisualOverride(ItemStack var1) {
      if (this.getClass() == vy.class || this instanceof vf || this instanceof v2 || this instanceof vz) {
         return null;
      }

      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         Minecraft var2 = Minecraft.getInstance();
         if (var2 != null && var2.player != null && var2.player.getOffhandItem() == var1) {
            return null;
         }

         int var4 = -1;
         int var3;
         if (this.S != vy.y.m) {
            var3 = this.getTemplateIdForValue(this.e);
            var4 = this.U;
         } else {
            if (this.Y < 1) {
               return null;
            }

            var3 = this.getTemplateIdForValue(this.Y);
         }

         if (var3 < 0) {
            return null;
         } else {
            int var5 = this.V(var1);
            if (var5 == var3) {
               return null;
            } else if (this.f >= 0 && var2 != null && var2.player != null && var2.player.getInventory().getItem(this.f) == var1) {
               return this.v(var1, var3);
            } else if (this.z >= 0 && var5 == this.z) {
               return this.v(var1, var3);
            } else {
               return var4 >= 1 && this.h(new MapId(var5)) == var4 ? this.v(var1, var3) : null;
            }
         }
      } else {
         return null;
      }
   }

   private ItemStack v(ItemStack var1, int var2) {
      if (this.D != null && this.V(this.D) == var2) {
         return this.D;
      }

      ItemStack var3 = var1.copy();
      var3.set(DataComponents.MAP_ID, new MapId(var2));
      this.D = var3;
      return var3;
   }

   public ItemStack getInventorySlotProjection(int var1) {
      ItemStack var2 = qm.S(var1);
      if (var2 != null) {
         return var2;
      }

      Minecraft var3 = Minecraft.getInstance();
      if (var3.player == null) {
         return null;
      }

      if (!this.isSwapActive()) {
         if (var1 == this.f && this.Y > 0) {
            ItemStack var4 = var3.player.getInventory().getItem(var1);
            int var5 = this.getTemplateIdForValue(this.Y);
            if (this.V(var4) == this.z && var5 >= 0) {
               return this.v(var4, var5);
            }
         }

         return null;
      } else {
         if (this.isSwapJuggling()
            || this.X >= 0
               && this.V(var3.player.getInventory().getItem(this.X)) == this.uY
               && var3.player.getInventory().getItem(this.X).getCount() >= this.uN[this.X]
               && (
                  this.w < 0
                     || this.w == this.X
                     || this.V(var3.player.getInventory().getItem(this.w)) == this.uP[this.w]
                        && var3.player.getInventory().getItem(this.w).getCount() == this.uN[this.w]
               )) {
            return this.J.get(var1);
         }

         this.J = Map.of();
         this.T();
         return null;
      }
   }

   public ItemStack projectSlot(int var1, ItemStack var2) {
      ItemStack var3 = this.getInventorySlotProjection(var1);
      return var3 == null ? var2 : var3;
   }

   public void v(int var1) {
      if (this.W() && this.isDealtHandLocked() && var1 == this.N) {
         this.T();
      }

      if (this.J.containsKey(var1) && this.isSwapActive()) {
         this.X(Minecraft.getInstance(), "projected card manually dropped; no replay");
      }
   }

   public boolean shouldSuppressOpen(MenuType<?> var1) {
      if (this.C()) {
         this.G = null;
         this.i = -1;
         return false;
      } else {
         return var1 != null && var1 == MenuType.GENERIC_3x3 && this.isSwapSuppressing();
      }
   }

   public void onSwapContainerOpened(MenuType<?> var1, int var2) {
      if (var2 >= 0) {
         if (var1 == null || var1 == MenuType.GENERIC_3x3) {
            if (this.isSwapSuppressing()) {
               if (this.G != null && this.G.P) {
                  this.X(Minecraft.getInstance(), "container replaced; no replay");
               } else {
                  Minecraft var3 = Minecraft.getInstance();
                  this.G = var3.player == null ? null : new p9(var3.player.containerMenu);
                  vy.y var4 = this.S;
                  this.i = var2;
                  we.r("OPEN arrived syncId=" + var2 + " phase=" + var4 + " clicksSinceOpen=0");
                  bn.b("SWAP " + this.J() + " container opened syncId=" + var2);
               }
            }
         }
      }
   }

   public void abortSwap() {
      this.T();
      if (this.G != null && this.G.P && this.isSwapActive()) {
         this.X(Minecraft.getInstance(), "cancelled after transfer; no replay");
      } else if (this.S == vy.y.m) {
         if (this.i >= 0) {
            this.f(Minecraft.getInstance());
         }

         this.y();
         this.r = 0;
      } else if (this.e(Minecraft.getInstance())) {
         this.a(3, "abort");
      } else {
         this.f(Minecraft.getInstance());
         this.S = vy.y.m;
         this.y();
         this.r = 0;
         bn.b("SWAP " + this.J() + " aborted");
      }
   }

   protected boolean supportsPhysicalSwap() {
      return this.getClass() == vy.class;
   }

   protected BlockPos getAssignedDispenserPosition(Minecraft var1) {
      if (this instanceof vf) {
         BlockPos var2 = hk.e().Q(this.w());
         return var2 != null ? var2 : hk.e().f(hk.k.A);
      } else {
         return this instanceof vh ? hk.e().f(hk.k.b) : hk.e().f(hk.k.x);
      }
   }

   private void n(Minecraft var1) {
      ArrayList var2 = new ArrayList();
      Map var3 = this.T();

      for (Entry var5 : var3.entrySet()) {
         if ((Integer)var5.getValue() > this.uL.getOrDefault(var5.getKey(), 0)) {
            var2.add((Integer)var5.getKey());
         }
      }

      if (!var2.isEmpty()) {
         if (this.u.isEmpty()) {
            bn.b("SWAP " + this.J() + " rise ignored (no plan armed) increased=" + var2 + " queue=" + this.pendingLabel());
         } else {
            int var7 = this.u.peekFirst();
            int var8;
            if (var2.size() == 1) {
               var8 = (Integer)var2.get(0);
            } else {
               if (this.z < 0 || !var2.contains(this.z)) {
                  this.K();
                  return;
               }

               var8 = this.z;
            }

            int var6 = this.h(new MapId(var8));
            if (var6 < 1) {
               this.K();
            } else {
               bn.b(
                  "SWAP "
                     + this.J()
                     + " deal real="
                     + this.O(var6)
                     + " (map "
                     + var8
                     + (this.z == var8 ? ", prearmed" : ", iterated")
                     + ") increased="
                     + var2
                     + " pending="
                     + this.O(var7)
               );
               if (var6 == var7) {
                  this.J(var6, var6);
                  this.y();
                  this.x();
               } else if (!this.swapWorthwhile(var6, var7)) {
                  this.J(var6, var6);
                  bn.b("SWAP " + this.J() + " skip (dealer already wins) real=" + this.O(var6) + " armed=" + this.O(var7) + " queue=" + this.pendingLabel());
                  this.y();
                  this.x();
               } else {
                  this.z = var8;
                  this.Y = var7;
                  this.j();
                  if (!this.Q(var1, var8, var6)) {
                     if (this.x(var1, var8) >= 0) {
                        this.q = var8;
                        this.t = var6;
                        this.B = var7;
                        this.y();
                     } else {
                        this.y();
                        this.x();
                     }
                  }
               }
            }
         }
      }
   }

   protected boolean swapWorthwhile(int var1, int var2) {
      return io.z().P(var1, var2);
   }

   private boolean Q(Minecraft var1, int var2, int var3) {
      if (var1 == null || var1.player == null || var1.level == null) {
         return false;
      }

      if (!this.supportsPhysicalSwap()) {
         return false;
      }

      vy var4 = a();
      if (var4 != this && var4.isSwapActive()) {
         return false;
      }

      if (this.u.isEmpty()) {
         return false;
      }

      int var5 = this.u.peekFirst();
      if (var5 < 1 || var5 > 10 || var5 == var3) {
         return false;
      }

      if (this.getTemplateForValue(var5) == null || var3 < 1) {
         return false;
      }

      if (!this.swapWorthwhile(var3, var5)) {
         return false;
      }

      int var6 = this.h(var1, var2);
      if (qm.M(var1) && var6 >= 0 && qm.v(this, var6, var5)) {
         this.O(var3, var5);
         return true;
      }

      if (this.uv > 0) {
         return false;
      }

      if (M(var1.gui.screen())) {
         return false;
      }

      if (!var1.player.containerMenu.getCarried().isEmpty() || !var1.player.inventoryMenu.getCarried().isEmpty()) {
         return false;
      }

      if (this.x(var1, var2) < 0) {
         return false;
      }

      int var7 = this.h(var1, var2);
      if (var7 < 0) {
         return false;
      }

      this.X = var7;
      BlockPos var8 = this.getAssignedDispenserPosition(var1);
      if (var8 == null) {
         return false;
      }

      this.H = var8.immutable();
      this.V = -1;
      this.uY = var2;
      this.U = var3;
      this.e = var5;
      this.a = this.T();
      this.a.merge(var2, -1, Integer::sum);
      this.a.merge(this.getTemplateIdForValue(var5), 1, Integer::sum);
      this.uP = new int[36];
      this.uN = new int[36];

      for (int var9 = 0; var9 < 36; var9++) {
         ItemStack var10 = var1.player.getInventory().getItem(var9);
         this.uP[var9] = this.V(var10);
         this.uN[var9] = var10.getCount();
      }

      this.u7 = (boolean)0;
      DispenserMenu var12 = this.N(var1);
      if (var12 != null && this.G != null && this.G.R == var12) {
         this.G = this.G.x();
      } else {
         this.G = null;
      }

      this.u4 = new p5();
      this.i = var12 != null ? var12.containerId : -1;
      we.r("BEGIN swap real=" + this.O(var3) + " ->" + this.O(var5) + " retainedSyncId=" + this.i + " cooldown=" + this.uv + " phase=" + this.S);
      this.p = -1;
      this.P = -1;
      this.us = -1;
      this.h = 0;
      this.x = 0;
      this.uv = 0;
      this.P();
      this.j();
      this.w = f1.i(var7, this.uN, var3x -> {
         ItemStack var4x = var1.player.getInventory().getItem(var3x);
         return this.V(var4x) == this.getTemplateIdForValue(var5) && var4x.getCount() < var4x.getMaxStackSize();
      });
      ItemStack var13 = var1.player.getInventory().getItem(var7);
      ItemStack var11 = var13.copyWithCount(1);
      var11.set(DataComponents.MAP_ID, this.getTemplateForValue(var5));
      this.J = f1.l(
         var7,
         this.w,
         this.uN[var7],
         this.w >= 0 && this.w != var7 ? this.uN[this.w] : 0,
         var13,
         this.w >= 0 && this.uN[this.w] > 0 && this.w != var7 ? var1.player.getInventory().getItem(this.w) : var11,
         ItemStack::copyWithCount
      );
      this.B(vy.y.f);
      this.s = 0;
      this.Q = -20;
      bn.b("SWAP BEGIN " + this.J() + " real=" + this.O(var3) + " ->" + this.O(var5));
      this.j("swap begin");
      this.I(var1);
      return true;
   }

   private void O(int var1, int var2) {
      this.c(var1, var2, true);
      this.P();
      this.y();
      this.p();
      this.T();
      this.x();
      bn.b("VISUAL " + this.J() + " real=" + var1 + " shown=" + var2);
   }

   public void onInventorySynchronized(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.G != null && this.isSwapActive()) {
         this.G.i("receive full sync=" + var1 + " size=" + var2.size() + " cursorCount=" + var3.getCount());
      }

      Minecraft var4 = Minecraft.getInstance();
      if (this.G != null && var4.player != null && var4.player.containerMenu == this.G.R && var1 == this.G.R.containerId) {
         this.G.O(var2, var3);
         if (this.G.d) {
            this.u4.D();
         }

         this.l();
      }
   }

   public void onServerSlot(int var1, int var2, ItemStack var3) {
      if (this.G != null && this.isSwapActive()) {
         this.G.i("receive slot sync=" + var1 + " slot=" + var2 + " map=" + this.V(var3) + " count=" + var3.getCount());
      }

      Minecraft var4 = Minecraft.getInstance();
      if (this.G != null && var4.player != null && var4.player.containerMenu == this.G.R) {
         if (var1 == -1 && var2 == -1) {
            this.G.C(-1, var3);
         } else if (var1 == this.G.R.containerId) {
            this.G.C(var2, var3);
         } else if (var1 == 0 && var2 >= 0 && var2 < var4.player.inventoryMenu.slots.size()) {
            Slot var5 = var4.player.inventoryMenu.getSlot(var2);
            if (var5.container == var4.player.getInventory()) {
               this.onPlayerInventorySlot(var5.getContainerSlot(), var3);
            }
         }

         this.l();
      }
   }

   public void onPlayerInventorySlot(int var1, ItemStack var2) {
      if (this.G != null && this.isSwapActive()) {
         this.G.i("receive player slot=" + var1 + " map=" + this.V(var2) + " count=" + var2.getCount());
      }

      Minecraft var3 = Minecraft.getInstance();
      if (this.G != null && var3.player != null && var3.player.containerMenu == this.G.R) {
         for (Slot var5 : this.G.R.slots) {
            if (var5.container == var3.player.getInventory() && var5.getContainerSlot() == var1) {
               this.G.C(var5.index, var2);
               this.l();
               return;
            }
         }
      }
   }

   public int clickRevision(AbstractContainerMenu var1) {
      return this.G != null && var1 == this.G.R && this.G.q ? 32768 : var1.getStateId();
   }

   public void s(BlockPos var1, String var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null && var3.level != null && var1.equals(this.getAssignedDispenserPosition(var3))) {
         we.r(
            var2
               + " "
               + this.p(var3)
               + " yaw="
               + var3.player.getYRot()
               + " pitch="
               + var3.player.getXRot()
               + " main="
               + var3.player.getMainHandItem()
               + " off="
               + var3.player.getOffhandItem()
         );
      }
   }

   public void onBlockInteractionAcknowledged(int var1) {
      if (this.S == vy.y.o) {
         we.r("OPEN ACK seq=" + var1 + " " + this.u4.G());
      }

      this.u4.x(var1);
      this.l();
   }

   private void l() {
      Minecraft var1 = Minecraft.getInstance();
      if (!this.E && this.G != null && !this.G.A && this.isSwapActive() && var1.player != null) {
         vy.y var2 = this.S;
         this.E = (boolean)1;

         try {
            if (this.S == vy.y.o && this.i >= 0) {
               this.B(vy.y.e);
            }

            if (this.isSwapJuggling()) {
               this.L(var1);
            }
         } finally {
            this.E = (boolean)0;
            if (this.S != var2) {
               we.r("PACKET continuation " + this.J() + " " + var2 + " -> " + this.S + " syncId=" + this.i);
            }
         }
      }
   }

   public boolean l() {
      return this.A;
   }

   public boolean blocksUserInteraction() {
      return this.isSwapActive() && (this.G == null || !this.G.A);
   }

   public boolean allowsRecoveryOpen(BlockPos var1) {
      return this.S == vy.y.o && this.i < 0 && this.G == null && var1 != null && var1.equals(this.H);
   }

   public boolean blocksClose() {
      return this.blocksUserInteraction();
   }

   public boolean u(AbstractContainerMenu var1) {
      return var1 instanceof InventoryMenu && (this.isSwapActive() || this.G != null);
   }

   boolean C() {
      return !this.isSwapActive() && this.G != null && this.G.L();
   }

   public void y(AbstractContainerMenu var1) {
      if (this.u(var1) && this.C()) {
         this.f(Minecraft.getInstance());
      }
   }

   public boolean hidesCursor(AbstractContainerMenu var1) {
      return this.isSwapJuggling() && !this.J.isEmpty() && (this.u(var1) || this.G != null && var1 == this.G.R);
   }

   private String p(Minecraft var1) {
      return "phase="
         + this.S
         + " distance="
         + this.Y(var1)
         + (
            var1.player == null
               ? " player=null"
               : " liveSync="
                  + var1.player.containerMenu.containerId
                  + " pos="
                  + var1.player.position()
                  + " velocity="
                  + var1.player.getDeltaMovement()
                  + " selected="
                  + var1.player.getInventory().getSelectedSlot()
                  + " forward="
                  + var1.options.keyUp.isDown()
                  + " back="
                  + var1.options.keyDown.isDown()
         )
         + " screen="
         + (var1.gui.screen() == null ? "none" : var1.gui.screen().getClass().getSimpleName());
   }

   public void V(int var1, String var2) {
      if (this.G != null && this.isSwapActive() && this.G.R.containerId == var1) {
         this.G.i(var2);
      }
   }

   public void U(int var1) {
      if (this.isSwapActive()) {
         we.r("SERVER CLOSE " + this.J() + " sync=" + var1 + " " + this.p(Minecraft.getInstance()));
      }
   }

   private void X(Minecraft var1, String var2) {
      we.r("BATCH RECOVERY " + this.J() + " reason=" + var2 + " " + this.p(var1) + " " + (this.G == null ? "batch=null" : this.G.u()));
      bn.j(this.J(), "Manual recovery required: " + var2 + " [phase=" + this.S + "; details: swap-open.log]");
      AbstractContainerMenu var3 = this.G == null ? null : this.G.R;
      this.G = null;
      this.i = -1;
      this.S = vy.y.m;
      this.u.clear();
      this.uL.clear();
      this.P();
      this.y();
      this.p();
      this.T();
      if (var1.player != null && var1.player.containerMenu instanceof DispenserMenu var4 && (var4 == var3 || var1.gui.screen() == null)) {
         var1.gui.setScreen(new DispenserScreen(var4, var1.player.getInventory(), Component.literal(this.J() + " recovery: " + var2)));
      }
   }

   private void L(Minecraft var1) {
      p9 var2 = this.G;
      if (var2 == null || var1.player.containerMenu != var2.R || M(var1.gui.screen())) {
         this.X(var1, "handler lost; no replay");
      } else if (!var2.d) {
         if (!this.E && ++this.W > 100) {
            this.X(var1, "initial authoritative inventory missing");
         }
      } else if (this.u4.I() && !this.u4.y()) {
         if (!this.E && ++this.W > 100) {
            this.X(var1, "open acknowledgement missing");
         }
      } else if (!var2.F(!this.E)) {
         if (var2.B != null) {
            this.X(var1, var2.B);
         }
      } else if (!var2.P && qm.M(var1)) {
         if (var2.L() && qm.v(this, this.X, this.e)) {
            this.f(var1);
            this.S = vy.y.m;
            this.O(this.U, this.e);
         }
      } else {
         DispenserMenu var3 = (DispenserMenu)var2.R;
         if (this.S == vy.y.e) {
            this.uh = -1;
            this.g = -1;

            for (Slot var5 : var3.slots) {
               if (var5.container == var1.player.getInventory() && var5.getContainerSlot() == this.X && this.V(var5.getItem()) == this.uY) {
                  this.uh = var5.index;
               }
            }

            if (!var2.B()) {
               this.V(var1, var2, "initial local/server ledger or cursor mismatch", "");
               return;
            }

            if (this.uh < 0) {
               if (var2.L()) {
                  this.u(false, "draw moved before transfer");
               } else {
                  this.V(var1, var2, "real arrival unavailable", "");
               }

               return;
            }

            if (var3.getSlot(this.uh).getItem().getCount() < this.uN[this.X]) {
               this.V(var1, var2, "original arrival quantity changed before transfer", "");
               return;
            }

            ArrayList var25 = new ArrayList();
            ArrayList var26 = new ArrayList();
            ArrayList var6 = new ArrayList();
            int var7 = this.getTemplateIdForValue(this.e);
            int var8 = var2.y.get(this.uh).getCount() == 1 ? 1 : 0;

            for (Slot var10 : var3.slots) {
               int var11 = var10.getContainerSlot();
               if (var10.container == var1.player.getInventory()
                  && var11 >= 0
                  && var11 < 36
                  && var11 != this.X
                  && this.uP[var11] == var7
                  && this.uN[var11] > 0
                  && this.V(var10.getItem()) == var7
                  && var10.getItem().getCount() == this.uN[var11]) {
                  var26.add(var10.index);
               }
            }

            if (var8 != 0) {
               var26.add(this.uh);
            }

            int var27 = var26.size();

            for (Slot var31 : var3.slots) {
               if (!(var31.container instanceof Inventory)) {
                  if (var7 >= 0 && this.V(var31.getItem()) == var7) {
                     var25.add(var31.index);
                  }

                  if (!var31.getItem().isEmpty() && this.V(var31.getItem()) == this.uY) {
                     var6.add(var31.index);
                  }
               } else if (var8 == 0
                  && var31.container == var1.player.getInventory()
                  && var31.getContainerSlot() >= 0
                  && var31.getContainerSlot() < 36
                  && this.uN[var31.getContainerSlot()] == 0
                  && var31.getItem().isEmpty()
                  && var2.y.get(var31.index).isEmpty()) {
                  var26.add(var31.index);
               }
            }

            var26.subList(var27, var26.size()).sort(Comparator.comparingInt(var1x -> var3.getSlot(var1x).getContainerSlot()));

            for (Slot var32 : var3.slots) {
               if (!(var32.container instanceof Inventory) && var32.getItem().isEmpty() && var2.y.get(var32.index).isEmpty()) {
                  var6.add(var32.index);
               }
            }

            var6.removeIf(var2x -> !this.allowsReturnSlot(var3.getSlot(var2x).getContainerSlot(), this.U));
            oi.y var30 = oi.f(
               var25,
               var26,
               var6,
               (var3x, var4) -> var2.B(var1, var3x, var4, var4 == this.uh, true).batch() != null,
               var3x -> var2.M(var1, this.uh, var3x) != null
            );
            if (var30 == null) {
               String var33 = var25.isEmpty()
                  ? "armed source unavailable"
                  : (
                     var26.isEmpty()
                        ? "player destination unavailable: no truly empty baseline slot for duplicate arrival"
                        : (var6.isEmpty() ? "real return unavailable: no real-map or empty container slot" : null)
                  );
               StringBuilder var12 = new StringBuilder("sources=" + var25 + " destinations=" + var26 + " eligibleReturns=" + var6 + " afterRemoval=" + var8);
               byte var13 = 0;
               String var14 = null;
               String var15 = null;

               for (int var17 : var25) {
                  int var18 = -1;
                  String var19 = null;
                  LinkedHashMap var20 = new LinkedHashMap();

                  for (int var22 : var26) {
                     p9.i var23 = var2.B(var1, var17, var22, var22 == this.uh, true);
                     if (var23.batch() != null) {
                        var18 = var22;
                        break;
                     }

                     var20.merge(var23.reason(), 1, Integer::sum);
                     if (var14 == null) {
                        var14 = var23.reason();
                     }

                     if (var19 == null) {
                        var19 = "destination=" + var22 + " " + var23.reason() + " " + var2.r(var17, var22);
                     }
                  }

                  var12.append("\nsource=")
                     .append(var17)
                     .append(" delivery=")
                     .append(var18)
                     .append(" firstReject=")
                     .append(var19)
                     .append(" rejectionCounts=")
                     .append(var20);
                  var13 |= var18 >= 0 ? 1 : 0;

                  for (Slot var35 : var3.slots) {
                     if (!(var35.container instanceof Inventory)) {
                        p9.i var36 = var2.B(var1, this.uh, var35.index, false, true);
                        String var24 = var35.index == var17
                           ? "excluded chosen source"
                           : (!var6.contains(var35.index) ? "ineligible return (not real-map/empty); planner=" + var36.reason() : var36.reason());
                        var12.append("\nsource=")
                           .append(var17)
                           .append(" return=")
                           .append(var35.index)
                           .append(" returnPlan=")
                           .append(var24)
                           .append(" ")
                           .append(var2.r(this.uh, var35.index));
                        if (var15 == null && var18 >= 0 && var35.index != var17 && var6.contains(var35.index)) {
                           var15 = var24;
                        }
                     }
                  }
               }

               if (var33 == null) {
                  var33 = var13 != 0
                     ? "real return unavailable: " + (var15 == null ? "only chosen source eligible" : var15)
                     : "armed delivery invalid: " + var14;
               }

               this.V(var1, var2, var33, var12.toString());
               return;
            }

            this.p = var30.source();
            this.g = var30.destination();
            this.us = var30.returnSlot();
            this.P = var3.getSlot(this.g).getContainerSlot();
            this.w = this.P;
            this.J = f1.l(
               this.X,
               this.w,
               this.uN[this.X],
               this.w == this.X ? 0 : this.uN[this.w],
               var2.y.get(this.uh),
               this.w != this.X && !var2.y.get(this.g).isEmpty() ? var2.y.get(this.g) : var2.y.get(this.p),
               ItemStack::copyWithCount
            );
            this.B(vy.y.E);
            var2.c(var1, this.uh, this.us, this.J() + " RETURN_REAL", true);
         } else if (this.S == vy.y.E) {
            this.B(vy.y.Q);
            var2.c(var1, this.p, this.g, this.J() + " DELIVER_ARMED", true);
         } else {
            if (!var2.B()) {
               this.X(var1, "final quantity ledger changed");
               return;
            }

            this.g(true, "two one-card batches confirmed");
         }

         if (var2.B != null) {
            this.X(var1, var2.B);
         }
      }
   }

   private void V(Minecraft var1, p9 var2, String var3, String var4) {
      StringBuilder var5 = new StringBuilder(
         "SWAP "
            + this.J()
            + " PREFLIGHT REJECT sync="
            + var2.R.containerId
            + " revision="
            + var2.R.getStateId()
            + " realMap="
            + this.uY
            + " armedMap="
            + this.getTemplateIdForValue(this.e)
            + " arrival="
            + this.uh
            + " arrivalInv="
            + this.X
            + " source="
            + this.p
            + " destination="
            + this.g
            + " cursorSafe="
            + var2.L()
            + " "
            + var2.w(-1)
            + " reason="
            + var3
            + "\n"
            + var4
      );

      for (Slot var7 : var2.R.slots) {
         var5.append("\n").append(var2.w(var7.index));
         if (var7.container == var1.player.getInventory() && var7.getContainerSlot() >= 0 && var7.getContainerSlot() < 36) {
            var5.append(" inv=")
               .append(var7.getContainerSlot())
               .append(" drawBaselineMap=")
               .append(this.uP[var7.getContainerSlot()])
               .append(" drawBaselineCount=")
               .append(this.uN[var7.getContainerSlot()]);
         }
      }

      bn.b(var5.toString());
      this.X(var1, var3);
   }

   private void I(Minecraft var1) {
      if (this.S != vy.y.e && this.S != vy.y.E && this.S != vy.y.Q && this.S != vy.y.A) {
         this.s++;
         if (this.s > 1200) {
            this.u(false, "whole-swap timeout");
         } else {
            switch (this.S) {
               case f:
                  this.W++;
                  if (this.i >= 0) {
                     if (this.N(var1) != null) {
                        we.r("REQUEST retained harness live syncId=" + this.i + " tick=" + this.W);
                        this.B(vy.y.e);
                        this.L(var1);
                     } else if (var1.player != null && var1.player.containerMenu instanceof DispenserMenu var2) {
                        this.i = var2.containerId;
                        we.r("REQUEST adopting live open syncId=" + var2.containerId + " tick=" + this.W + " screenOpen=" + (var1.gui.screen() != null));
                        bn.b("SWAP " + this.J() + " retained harness vanished - adopting live open syncId=" + var2.containerId);
                        this.B(vy.y.o);
                     } else if (!M(var1.gui.screen())) {
                        this.i = -1;
                        we.r("REQUEST retained harness gone - reopening tick=" + this.W);
                        bn.b("SWAP " + this.J() + " retained harness vanished - reopening");
                        this.N(var1);
                     } else {
                        we.r(
                           "REQUEST retained gone but screen open tick="
                              + this.W
                              + " screen="
                              + (var1.gui.screen() == null ? "null" : var1.gui.screen().getClass().getSimpleName())
                              + " handler="
                              + (var1.player.containerMenu == null ? "null" : var1.player.containerMenu.getClass().getSimpleName())
                        );
                     }
                  } else if (this.W >= 1) {
                     we.r("REQUEST fresh - compact requestContainerOpen tick=" + this.W);
                     this.N(var1);
                  }
                  break;
               case o:
                  this.W++;
                  if (this.i >= 0) {
                     we.r("WAIT_OPEN open arrived tick=" + this.W + " syncId=" + this.i);
                     bn.b("SWAP " + this.J() + " open-arrived tick=" + this.W + " syncId=" + this.i);
                     this.B(vy.y.e);
                     this.L(var1);
                  } else if (this.W >= 100 && this.G == null) {
                     this.u(false, "dispenser did not open; no cards moved");
                  } else if (this.W >= 660) {
                     we.r(
                        "WAIT_OPEN open timeout tick="
                           + this.W
                           + " screen="
                           + (var1.gui.screen() == null ? "null" : var1.gui.screen().getClass().getSimpleName())
                     );
                     this.X("open timeout");
                  } else if (this.u4.n(this.s)) {
                     this.v(var1);
                  }
                  break;
               default:
                  this.S = vy.y.m;
            }
         }
      } else {
         this.L(var1);
      }
   }

   private void N(Minecraft var1) {
      if (this.H == null) {
         this.u(false, "dispenser unknown");
      } else {
         this.v(var1);
         this.B(vy.y.o);
      }
   }

   private void v(Minecraft var1) {
      if (this.u4.n(this.s) && this.s - this.Q >= 4) {
         if (this.H != null && var1 != null && var1.player != null && var1.gameMode != null && var1.getConnection() != null && var1.level != null) {
            BlockHitResult var2 = this.Z(var1);
            if (var2 == null) {
               we.r("open blocked: no visible dispenser face pos=" + this.H);
            } else {
               boolean var3 = var1.player.isShiftKeyDown();
               double var4 = this.Y(var1);
               we.r(
                  "open-issue phase="
                     + this.S
                     + " tick="
                     + this.W
                     + " sneaking="
                     + var3
                     + " dist="
                     + String.format("%.1f", var4)
                     + " syncId="
                     + this.i
                     + " pass="
                     + this.x
               );
               bn.b(
                  "SWAP "
                     + this.J()
                     + " open-issue phase="
                     + this.S
                     + " tick="
                     + this.W
                     + " sneaking="
                     + var3
                     + " dist="
                     + String.format("%.1f", var4)
                     + " syncId="
                     + this.i
               );
               if (!var1.player.isShiftKeyDown() && !var1.options.keyUse.isDown()) {
                  this.x(var1, var2, var1x -> {
                     this.Q = this.s;
                     this.u4.o(var1x, this.s);
                  });
               }
            }
         }
      }
   }

   protected void x(Minecraft var1, BlockHitResult var2, IntConsumer var3) {
      w8.P(
         var1,
         var2,
         () -> {
            if (this.isSwapActive()
               && var1.player != null
               && var1.level != null
               && var1.player.containerMenu == var1.player.inventoryMenu
               && !var1.player.isShiftKeyDown()
               && !var1.options.keyUse.isDown()
               && var2.getBlockPos().equals(this.getAssignedDispenserPosition(var1))) {
               this.A = (boolean)1;

               try {
                  var1.gameMode.useItemOn(var1.player, InteractionHand.MAIN_HAND, var2);
                  var3.accept(((qa6b3f1c381a58196)var1.level).LarpRig$getPendingUpdateManager().currentSequence());
                  return true;
               } finally {
                  this.A = (boolean)0;
               }
            } else {
               return false;
            }
         }
      );
   }

   static int H(int var0, int var1) {
      return var0 >= 0 && var0 < 9 ? var0 : var1;
   }

   private BlockHitResult Z(Minecraft var1) {
      return Y(var1, this.H);
   }

   protected static BlockHitResult Y(Minecraft var0, BlockPos var1) {
      if (var0.hitResult instanceof BlockHitResult var2
         && var2.getType() == Type.BLOCK
         && var2.getBlockPos().equals(var1)
         && var0.player.getEyePosition().distanceToSqr(var2.getLocation()) <= Math.pow(var0.player.blockInteractionRange(), 2.0)) {
         return var2;
      } else {
         Vec3 var14 = var0.player.getEyePosition();
         BlockHitResult var15 = null;
         double var4 = Double.MAX_VALUE;

         for (Direction var9 : Direction.values()) {
            Vec3 var10 = Vec3.atCenterOf(var1).add(var9.getStepX() * 0.499, var9.getStepY() * 0.499, var9.getStepZ() * 0.499);
            BlockHitResult var11 = var0.level.clip(new ClipContext(var14, var10, Block.OUTLINE, Fluid.NONE, var0.player));
            double var12 = var14.distanceToSqr(var11.getLocation());
            if (var11.getType() == Type.BLOCK
               && var11.getBlockPos().equals(var1)
               && var12 <= var0.player.blockInteractionRange() * var0.player.blockInteractionRange()
               && var12 < var4) {
               var15 = var11;
               var4 = var12;
            }
         }

         return var15;
      }
   }

   private void X(String var1) {
      if (this.x < 6) {
         this.x++;
         we.r("retry (" + var1 + ") pass=" + this.x + " phase=" + this.S + " tick=" + this.W);
         bn.b("SWAP " + this.J() + " retry (" + var1 + ")");
         this.a(1, "retry " + var1);
      } else {
         we.r("CRITICAL fail (" + var1 + ") after pass=" + this.x);
         this.u(false, var1);
      }
   }

   private void f(Minecraft var1) {
      this.G = null;
      if (var1 != null && var1.player != null) {
         if (this.i >= 0 && var1.player.containerMenu != null && var1.player.containerMenu.containerId == this.i) {
            we.r("close suppressed container syncId=" + this.i + " phase=" + this.S);
            if (!(var1.gui.screen() instanceof InventoryScreen) && !(var1.gui.screen() instanceof ChatScreen)) {
               var1.player.closeContainer();
            } else {
               var1.getConnection().send(new ServerboundContainerClosePacket(this.i));
            }
         }

         var1.player.containerMenu = var1.player.inventoryMenu;
         this.i = -1;
      }
   }

   private void u(boolean var1, String var2) {
      if (!var1) {
         bn.j(this.J(), "Swap failed: " + var2);
      }

      bn.b("SWAP " + this.J() + " " + (var1 ? "OK" : "FAIL") + " (" + var2 + ") real=" + this.O(this.U) + " ->" + this.O(this.e));
      if (!var1 && this.e(Minecraft.getInstance())) {
         this.a(2, "cleanup " + var2);
      } else {
         this.g(var1, var2);
      }
   }

   private void g(boolean var1, String var2) {
      Minecraft var3 = Minecraft.getInstance();
      p9 var4 = this.G;
      if (var4 != null) {
         we.r("BATCH TRACE " + this.J() + " " + this.p(var3) + " " + var4.u());
      }

      we.r("finishSwapDecided ok=" + var1 + " reason=" + var2 + " syncId=" + this.i + " queue=" + this.pendingLabel() + " phase=" + this.S);
      this.f(var3);
      this.uv = 0;
      this.L = var1 ? 12 : 0;
      if (var1) {
         this.J(this.U, this.e);
      }

      this.x();
      if (var1 && !this.u.isEmpty() && var4 != null) {
         this.uL = new HashMap<>(this.a);
         hv.completeDrawBaseline(this.uP, this.uN, this.X, this.P, this.getTemplateIdForValue(this.e));
         System.arraycopy(this.uP, 0, this.n, 0, 36);
         System.arraycopy(this.uN, 0, this.c, 0, 36);
      }

      this.y();
      this.S = vy.y.m;
      this.u7 = (boolean)0;
      if (var1) {
         ItemStack var5 = var3.player.getMainHandItem();
         if (!this.isDealtHandLocked()
            || !var5.is(Items.FILLED_MAP)
            || this.V(var5) < 0
            || (this.C == null || this.V(var5) != this.V(this.C)) && this.V(var5) != this.getTemplateIdForValue(this.e)) {
            this.p();
            this.T();
         } else {
            this.C = var5.copy();
            this.K = (boolean)1;
         }
      }

      if (!var1) {
         this.p();
         this.T();
      }

      this.r = var1 ? 16 : 0;
      this.j("swap end " + (var1 ? "ok" : "fail") + " " + var2);
      if (this.q >= 0 && !this.u.isEmpty() && this.u.peekFirst() == this.B) {
         this.z = this.q;
         this.Y = this.B;
         this.E();
      } else {
         this.P();
         this.y();
      }
   }

   private void a(int var1, String var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.e(var3)) {
         this.f(var3);
         this.i(var3, var1, var2);
      } else {
         this.X(var3, var2 + ": occupied cursor before batch");
      }
   }

   private void i(Minecraft var1, int var2, String var3) {
      switch (var2) {
         case 1:
            this.h = 0;
            this.i = -1;
            this.W = 1;
            this.S = vy.y.f;
            break;
         case 2:
            this.g(false, var3);
            break;
         default:
            this.f(var1);
            this.S = vy.y.m;
            this.y();
            bn.b("SWAP " + this.J() + " aborted");
      }
   }

   private boolean e(Minecraft var1) {
      DispenserMenu var2 = this.N(var1);
      return var2 != null && !var2.getCarried().isEmpty();
   }

   private void x() {
      if (!this.u.isEmpty()) {
         bn.b("PLAYED " + this.J() + " next=" + this.u.peekFirst());
         this.u.pollFirst();
         if (this.u.isEmpty()) {
            this.uL.clear();
            if (this.S == vy.y.m && this.i >= 0) {
               if (this.G != null && this.G.L()) {
                  this.f(Minecraft.getInstance());
               } else {
                  this.X(Minecraft.getInstance(), "queue ended without a safe cursor snapshot");
               }
            }
         } else {
            this.K();
         }
      }
   }

   private void B(vy.y var1) {
      this.S = var1;
      if (this.G != null) {
         this.G.i(this.p(Minecraft.getInstance()));
      }

      this.W = 0;
   }

   private void P() {
      this.q = -1;
      this.t = 0;
      this.B = 0;
   }

   private void y() {
      this.J = Map.of();
      this.w = -1;
      this.z = -1;
      this.Y = -1;
      this.f = -1;
      this.D = null;
   }

   public int getSwapSyncId() {
      return this.i;
   }

   private void E() {
      this.f = -1;
      if (this.z >= 0) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null) {
            this.f = this.Z >= 0 && this.Z < 36 && this.V(var1.player.getInventory().getItem(this.Z)) == this.z ? this.Z : this.x(var1, this.z);
         }
      }
   }

   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      if (var3 >= 0) {
         int var4 = this.h(new MapId(var3));
         if (var4 >= 1) {
            Minecraft var5 = Minecraft.getInstance();
            Object var6 = var1 == 0 ? var5.player.inventoryMenu : var5.player.containerMenu;
            if (var2 >= 0 && var2 < ((AbstractContainerMenu)var6).slots.size()) {
               Slot var7 = var6.getSlot(var2);
               if (var7.container == var5.player.getInventory() && var7.getContainerSlot() < 36) {
                  this.Z = var7.getContainerSlot();
                  if (this.u.isEmpty() || this.S != vy.y.m || this.h(var5, var3) >= 0) {
                     if (this.u.isEmpty()) {
                        this.z = var3;
                        this.E();
                        bn.b("SWAP " + this.J() + " slot-update sync=" + var1 + " slot=" + var2 + " real=" + this.O(var4) + " (no plan armed)");
                     } else if (this.S == vy.y.m) {
                        int var8 = this.u.peekFirst();
                        if (var4 == var8) {
                           bn.b("SWAP " + this.J() + " slot-update real=" + this.O(var4) + " == pending (no swap needed)");
                        } else {
                           this.z = var3;
                           this.Y = var8;
                           this.E();
                           this.j();
                           if (!this.swapWorthwhile(var4, var8)) {
                              this.J(var4, var4);
                              bn.b(
                                 "SWAP "
                                    + this.J()
                                    + " skip (dealer already wins) real="
                                    + this.O(var4)
                                    + " armed="
                                    + this.O(var8)
                                    + " queue="
                                    + this.pendingLabel()
                              );
                              this.y();
                              this.x();
                           } else {
                              if (!this.Q(Minecraft.getInstance(), var3, var4) && this.x(Minecraft.getInstance(), var3) >= 0) {
                                 this.q = var3;
                                 this.t = var4;
                                 this.B = var8;
                              }
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   static boolean M(Screen var0) {
      return var0 != null && !(var0 instanceof InventoryScreen) && !(var0 instanceof ChatScreen);
   }

   private DispenserMenu N(Minecraft var1) {
      if (var1 != null && var1.player != null && !M(var1.gui.screen())) {
         return var1.player.containerMenu instanceof DispenserMenu var2 && var2.containerId == this.i ? var2 : null;
      } else {
         return null;
      }
   }

   protected boolean allowsReturnSlot(int var1, int var2) {
      return true;
   }

   private int Z(DispenserMenu var1, int var2) {
      if (var2 < 0) {
         return -1;
      }

      for (Slot var4 : var1.slots) {
         if (var4 != null && !(var4.container instanceof Inventory)) {
            ItemStack var5 = var4.getItem();
            if (var5 != null && var5.is(Items.FILLED_MAP) && this.V(var5) == var2) {
               return var4.index;
            }
         }
      }

      return -1;
   }

   private int O(DispenserMenu var1, ItemStack var2) {
      int var3 = this.V(var2);
      int var4 = this.h((MapId)var2.getComponents().get(DataComponents.MAP_ID));
      int var5 = -1;
      if (var4 >= 1 && var3 >= 0) {
         var5 = this.Z(var1, var3);
         if (var5 < 0) {
            var5 = this.containerIndexForValue(var4);
         }
      } else {
         var5 = this.p;
      }

      if (var5 >= 0) {
         Slot var6 = var1.getSlot(var5);
         if (var6 != null) {
            ItemStack var7 = var6.getItem();
            if (var7 != null && !var7.isEmpty() && this.V(var7) != var3) {
               var5 = -1;
            }
         }
      }

      if (var5 < 0) {
         var5 = this.S(var1);
      }

      return var5;
   }

   private int S(DispenserMenu var1) {
      for (Slot var3 : var1.slots) {
         if (var3 != null && !(var3.container instanceof Inventory) && var3.getItem().isEmpty()) {
            return var3.index;
         }
      }

      return -1;
   }

   private int g(DispenserMenu var1, int var2) {
      int var3 = this.getTemplateIdForValue(var2);
      return var3 < 0 ? -1 : this.Z(var1, var3);
   }

   private int x(Minecraft var1, int var2) {
      if (var1 != null && var1.player != null) {
         Inventory var3 = var1.player.getInventory();

         for (int var4 = 0; var4 < var3.getContainerSize(); var4++) {
            ItemStack var5 = var3.getItem(var4);
            if (var5 != null && var5.is(Items.FILLED_MAP) && this.V(var5) == var2) {
               return var4;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private int q(DispenserMenu var1, int var2) {
      if (var2 < 0) {
         return -1;
      }

      for (Slot var4 : var1.slots) {
         if (var4 != null && var4.container instanceof Inventory) {
            ItemStack var5 = var4.getItem();
            if (var5 != null && var5.is(Items.FILLED_MAP) && this.V(var5) == var2) {
               return var4.index;
            }
         }
      }

      return -1;
   }

   private boolean M(Minecraft var1, int var2) {
      return this.x(var1, var2) >= 0;
   }

   private int h(int var1) {
      return var1 >= 0 && var1 < 9 ? var1 + 36 : var1;
   }

   private boolean S(DispenserMenu var1, int var2) {
      return this.x(var1, this.getTemplateIdForValue(var2));
   }

   private boolean x(DispenserMenu var1, int var2) {
      ItemStack var3 = var1.getCarried();
      return var3 != null && var3.is(Items.FILLED_MAP) && this.V(var3) == var2;
   }

   private boolean o(DispenserMenu var1, int var2, int var3) {
      return this.M(var1, var2, this.getTemplateIdForValue(var3));
   }

   private boolean M(DispenserMenu var1, int var2, int var3) {
      if (var2 >= 0 && var2 < var1.slots.size()) {
         Slot var4 = var1.getSlot(var2);
         if (var4 == null) {
            return false;
         }

         ItemStack var5 = var4.getItem();
         return var5 != null && var5.is(Items.FILLED_MAP) && this.V(var5) == var3;
      } else {
         return false;
      }
   }

   private double Y(Minecraft var1) {
      if (var1 != null && var1.player != null && this.H != null) {
         AABB var2 = new AABB(this.H);
         return Math.sqrt(var2.distanceToSqr(var1.player.getEyePosition()));
      } else {
         return -1.0;
      }
   }

   private void j(String var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         StringBuilder var3 = new StringBuilder();
         int var4 = var2.player.getInventory().getSelectedSlot();

         for (int var5 = 0; var5 < var2.player.getInventory().getContainerSize(); var5++) {
            ItemStack var6 = var2.player.getInventory().getItem(var5);
            if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
               MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
               int var8 = var7 == null ? -1 : this.h(var7);
               if (var8 >= 1) {
                  if (!var3.isEmpty()) {
                     var3.append(", ");
                  }

                  var3.append("v").append(this.O(var8)).append("x").append(var6.getCount()).append("@").append(var5).append(var5 == var4 ? "(sel)" : "");
               }
            }
         }

         bn.b("DUMP " + this.J() + " " + var1 + " selected=" + var4 + " cards=[" + var3 + "]");
      }
   }

   private int V(ItemStack var1) {
      MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
      return var2 == null ? -1 : var2.id();
   }

   private int h(MapId var1) {
      if (var1 == null) {
         return -1;
      }

      q5 var2 = q5.a();
      if (var2.N() && var2.G()) {
         return var2.n(var1.id());
      }

      for (Entry var4 : this.I.entrySet()) {
         if (var4.getValue() != null && ((MapId)var4.getValue()).id() == var1.id()) {
            return (Integer)var4.getKey();
         }
      }

      return -1;
   }

   private Map<Integer, Integer> a() {
      return this.R.computeIfAbsent(this.h(), var0 -> new HashMap<>());
   }

   private Map<Integer, Integer> M() {
      return this.d.computeIfAbsent(this.h(), var0 -> new HashMap<>());
   }

   private String h() {
      return this.uq == null ? "" : this.uq;
   }

   private boolean K() {
      Map var1 = this.T();

      for (Entry var3 : var1.entrySet()) {
         if ((Integer)var3.getValue() > this.uL.getOrDefault(var3.getKey(), 0)) {
            return true;
         }
      }

      return false;
   }

   private Map<Integer, Integer> T() {
      HashMap var1 = new HashMap();
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         for (int var3 = 0; var3 < var2.player.getInventory().getContainerSize(); var3++) {
            this.Z(var1, var2.player.getInventory().getItem(var3));
         }
      }

      if (var2 != null && var2.level != null) {
         for (Entity var4 : var2.level.entitiesForRendering()) {
            if (var4 instanceof ItemFrame var5) {
               this.Z(var1, var5.getItem());
            }
         }
      }

      return var1;
   }

   private void Z(Map<Integer, Integer> var1, ItemStack var2) {
      if (var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP)) {
         MapId var3 = (MapId)var2.getComponents().get(DataComponents.MAP_ID);
         if (var3 != null) {
            var1.merge(var3.id(), var2.getCount(), Integer::sum);
         }
      }
   }

   public enum y {
      m,
      f,
      o,
      e,
      E,
      Q,
      A;
   }
}
