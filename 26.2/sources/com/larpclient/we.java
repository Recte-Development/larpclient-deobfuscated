package com.larpclient;

import com.larp.obf.qa300483f54d804a3.q44c0827ad90befbb.qaee82196bfd62460;
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
public class we {
   private static final we st = new we();
   private static final String c = "";
   private static final int B = 660;
   private static final int[] E = new int[]{3, 6, 10, 15, 22, 30, 40, 52, 66, 82, 100, 120, 150, 180};
   private static final int n = 100;
   private static final int G = 1200;
   private static final int P = 6;
   private static final int z = 16;
   private static final int h = 40;
   private final Deque<Integer> s_ = new ArrayDeque<>();
   private final Map<Integer, MapId> M = new HashMap<>();
   private final Map<Integer, Integer> w = new HashMap<>();
   private final Map<String, Map<Integer, Integer>> g = new HashMap<>();
   private final Map<String, Map<Integer, Integer>> sN = new HashMap<>();
   private Map<Integer, Integer> f = new HashMap<>();
   private final int[] e = new int[36];
   private final int[] t = new int[36];
   private int sM = -1;
   private int sF = -1;
   private Map<Integer, Integer> k;
   private int[] K;
   private int[] F;
   private boolean X;
   private String J;
   private we.q Z = we.q.s;
   private int sq = -1;
   private int D = -1;
   private int v = -1;
   private int C = -1;
   private int s9 = -1;
   private int S = -1;
   private int L;
   private int s1;
   private int N;
   private int y = -1;
   private int sa;
   private int x;
   private int sX;
   private int p = -1;
   private long R;
   private boolean W;
   private boolean I;
   private int s = -1;
   private ItemStack V;
   private int m;
   private int sr = -1;
   private int j;
   private int se;
   private int b;
   private int U = -1;
   private int q = -1;
   private int o = -1;
   private ItemStack A;
   private BlockPos i;
   private ug sm;
   private t0 l = new t0();
   private int H = -1;
   private int sZ = -1;
   private Map<Integer, ItemStack> T = Map.of();
   private int Q = -1;
   private boolean r;
   private ClientLevel u;
   private long O = -1L;
   private final Map<Integer, Integer> a = new HashMap<>();
   private int Y = -20;
   private boolean d;

   protected we() {
   }

   public static we u() {
      return st;
   }

   public static boolean M() {
      return Y().isSwapActive();
   }

   public static we Y() {
      we var0 = u();
      wc var1 = wc.s();
      wk var2 = wk.m();
      w3 var3 = w3.M();
      w0 var4 = w0.l();
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
      } else if (wc.s().Q()) {
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
      } else if (var0.P()) {
         return var0;
      } else if (var1.P()) {
         return var1;
      } else if (var2.P()) {
         return var2;
      } else if (var3.P()) {
         return var3;
      } else {
         return var4.P() ? var4 : var0;
      }
   }

   private boolean P() {
      return this.Z != we.q.s || this.U >= 0 || this.q >= 1 || this.sr >= 0;
   }

   public static we l() {
      we var0 = Y();
      if (var0.isSwapActive()) {
         return var0;
      }

      if (st.sm != null) {
         return st;
      }

      wk var1 = wk.m();
      return var1.sm != null ? var1 : wc.s();
   }

   private boolean T() {
      return this.Z == we.q.s && this.U < 0 && this.q < 1 && !this.s_.isEmpty();
   }

   public void queueForce(int var1) {
      this.queueForceSequence(var1);
   }

   public void queueForceSequence(int... var1) {
      if (this.isSwapBusy()) {
         if (!this.X) {
            te.R("SWAP " + this.E() + " queue replacement rejected while busy");
            this.X = (boolean)1;
         }
      } else {
         this.X = (boolean)0;
         this.s_.clear();
         this.e();
         if (var1 != null) {
            int var2 = Math.max(1, this.maxQueuedForces());

            for (int var6 : var1) {
               if (this.s_.size() >= var2) {
                  break;
               }

               if (var6 >= 1 && var6 <= 10 && this.getTemplateForValue(var6) != null) {
                  this.s_.addLast(var6);
               }
            }
         }

         if (this.s_.isEmpty()) {
            this.f.clear();
         } else {
            this.p();
         }
      }
   }

   private void p() {
      this.f = this.E();
      Minecraft var1 = Minecraft.getInstance();

      for (int var2 = 0; var2 < 36; var2++) {
         ItemStack var3 = var1.player == null ? ItemStack.EMPTY : var1.player.getInventory().getItem(var2);
         this.e[var2] = this.W(var3);
         this.t[var2] = var3.getCount();
      }

      this.sM = -1;
   }

   private int q(Minecraft var1, int var2) {
      if (this.E().getOrDefault(var2, 0) <= this.f.getOrDefault(var2, 0)) {
         return -1;
      }

      int[] var3 = new int[36];
      int[] var4 = new int[36];

      for (int var5 = 0; var5 < 36; var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         var3[var5] = this.W(var6);
         var4[var5] = var6.getCount();
      }

      return bx.dealtSlot(var2, this.e, this.t, var3, var4, this.sM);
   }

   public void clearQueue() {
      this.abortSwap();
      this.s_.clear();
      this.f.clear();
      this.X = (boolean)0;
   }

   public int getPendingValue() {
      Integer var1 = this.s_.peekFirst();
      return var1 == null ? -1 : var1;
   }

   public int getQueuedValueAt(int var1) {
      if (var1 < 0) {
         return -1;
      }

      int var2 = 0;

      for (Integer var4 : this.s_) {
         if (var2++ == var1) {
            return var4;
         }
      }

      return -1;
   }

   public boolean hasQueuedForce() {
      return !this.s_.isEmpty();
   }

   public String pendingLabel() {
      if (this.s_.isEmpty()) {
         return "none";
      }

      StringBuilder var1 = new StringBuilder();

      for (Integer var3 : this.s_) {
         if (!var1.isEmpty()) {
            var1.append(" -> ");
         }

         var1.append(this.M(var3));
      }

      return var1.toString();
   }

   public void tick(Minecraft var1) {
      if (var1 != null && var1.player != null) {
         if (var1.level != null) {
            if (this.u != var1.level) {
               this.u = var1.level;
               this.O = var1.level.getGameTime();
            } else if (var1.level.getGameTime() == this.O) {
               return;
            }
         }

         this.O = var1.level == null ? -1L : var1.level.getGameTime();
         if (this.sX > 0) {
            this.sX--;
         }

         if (this.m > 0) {
            this.m--;
         }

         if (this.sa > 0) {
            this.sa--;
         }

         if (this.Z != we.q.s) {
            this.n(var1);
         } else {
            if (this.b > 0 && --this.b == 0) {
               this.G("post-swap settle");
            }

            if (this.sr >= 0) {
               int var2 = this.sr;
               int var3 = this.j;
               int var4 = this.se;
               if (this.s_.isEmpty() || this.s_.peekFirst() != var4) {
                  this.k();
               } else if (this.t(var1, var2) < 0) {
                  this.k();
               } else if (this.sa <= 0) {
                  if (var3 == var4) {
                     this.h(var3, var3);
                     te.R("SWAP " + this.E() + " deferred deal already matched " + this.M(var4));
                     this.k();
                     this.e();
                     this.D();
                     return;
                  }

                  if (!this.swapWorthwhile(var3, var4)) {
                     this.h(var3, var3);
                     te.R("SWAP " + this.E() + " deferred skip real=" + this.M(var3) + " armed=" + this.M(var4));
                     this.k();
                     this.e();
                     this.D();
                     return;
                  }

                  if (this.o(var1, var2, var3)) {
                     return;
                  }
               }

               if (this.sr >= 0) {
                  return;
               }
            }

            if (this.Z == we.q.s && this.U >= 0 && this.sr < 0 && this.t(var1, this.U) < 0) {
               this.e();
            }

            if (M(var1.gui.screen()) && var1.player.containerMenu instanceof DispenserMenu) {
               this.scanCurrentContainerTemplateSlots(var1);
            }

            if (!this.s_.isEmpty()) {
               if (this.a()) {
                  this.D(var1);
               } else if (this.U >= 0 && this.Z == we.q.s && this.sr < 0 && this.sa <= 0) {
                  this.F(var1);
               }
            }
         }
      } else {
         this.abortSwap();
         this.clearQueue();
         this.u = null;
         this.O = -1L;
      }
   }

   private void F(Minecraft var1) {
      int var2 = this.U;
      if (var2 >= 0 && !this.s_.isEmpty()) {
         int var3 = this.C(new MapId(var2));
         if (var3 < 1) {
            this.e();
         } else {
            int var4 = this.s_.peekFirst();
            te.R("SWAP " + this.E() + " deal real=" + this.M(var3) + " (visual) map=" + var2 + " pending=" + this.M(var4));
            if (var3 == var4) {
               this.h(var3, var3);
               this.e();
               this.D();
            } else if (!this.swapWorthwhile(var3, var4)) {
               this.h(var3, var3);
               this.e();
               this.D();
            } else if (this.sa <= 0 && !M(var1.gui.screen())) {
               this.V();
               if (!this.o(var1, var2, var3) && this.t(var1, var2) >= 0) {
                  this.sr = var2;
                  this.j = var3;
                  this.se = var4;
                  this.e();
               }
            }
         }
      } else {
         this.e();
      }
   }

   public void setTemplateForValue(int var1, MapId var2) {
      if (var1 >= 1 && var1 <= 10) {
         if (var2 == null) {
            this.M.remove(var1);
            this.w.remove(var1);
         } else {
            this.M.put(var1, var2);
            this.w.put(var1, var2.id());
         }
      }
   }

   public MapId getTemplateForValue(int var1) {
      lh var2 = lh.I();
      if (var2.S() && var2.V()) {
         int var3 = var2.Z(var1);
         return var3 < 0 ? null : new MapId(var3);
      } else {
         return var1 >= 1 && var1 <= 10 ? this.M.get(var1) : null;
      }
   }

   public int getTemplateIdForValue(int var1) {
      lh var2 = lh.I();
      return var2.S() && var2.V() ? var2.Z(var1) : this.w.getOrDefault(var1, -1);
   }

   public void setTemplateIdForValue(int var1, int var2) {
      if (var1 >= 1 && var1 <= 10) {
         this.setTemplateForValue(var1, var2 < 0 ? null : new MapId(var2));
      }
   }

   public int countTemplates() {
      return this.M.size();
   }

   public boolean M(MapId var1) {
      return this.C(var1) >= 1;
   }

   public void setContainerContext(String var1) {
      if (!Objects.equals(this.J, var1) && this.sm != null) {
         this.D(Minecraft.getInstance(), "dispenser context changed");
      }

      this.J = var1;
      this.f.clear();
      this.e();
   }

   public String z() {
      return this.J;
   }

   public int v(int var1) {
      return this.j().getOrDefault(var1, -1);
   }

   public boolean scanCurrentContainerTemplateSlots(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.player.containerMenu instanceof DispenserMenu var2) {
         Map var11 = this.j();
         Map var4 = this.P();
         var11.clear();
         var4.clear();
         byte var5 = 0;

         for (Slot var7 : var2.slots) {
            if (var7 != null && !(var7.container instanceof Inventory)) {
               ItemStack var8 = var7.getItem();
               if (var8 != null && !var8.isEmpty() && var8.is(Items.FILLED_MAP)) {
                  MapId var9 = (MapId)var8.getComponents().get(DataComponents.MAP_ID);
                  int var10 = var9 == null ? -1 : this.C(var9);
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

   public int i(String var1, ItemStack var2) {
      if (var2 != null) {
         var2 = mg.W(var1, var2);
      }

      return var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP) ? this.C((MapId)var2.getComponents().get(DataComponents.MAP_ID)) : -1;
   }

   public int q(MapId var1) {
      return this.C(var1);
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

   private void h(int var1, int var2) {
      this.w(var1, var2, false);
   }

   private void w(int var1, int var2, boolean var3) {
      te.j(this.E(), var1, var2, this.getTemplateIdForValue(var2), var3);
      if (this.getClass() == we.class || this instanceof wk || this instanceof w0) {
         this.a.merge(this.getTemplateIdForValue(var3 ? var1 : var2), 1, Integer::sum);
      }
   }

   public void D(int var1, int var2) {
      int var3 = this.a.getOrDefault(var2, 0);
      if (var3 > 0) {
         if (var3 == 1) {
            this.a.remove(var2);
         } else {
            this.a.put(var2, var3 - 1);
         }
      } else {
         te.Z(this.E(), te.I(te.K(this.E(), var1), Integer.toString(var1), false, false));
      }
   }

   public void h() {
      this.a.clear();
   }

   protected String E() {
      if (this instanceof wc) {
         return "Higher/Lower";
      } else if (this instanceof w3) {
         return "Odd/Even";
      } else if (this instanceof wk) {
         return "Baccarat";
      } else {
         return this instanceof w0 ? "Mexican Jack" : "Blackjack";
      }
   }

   protected String M(int var1) {
      return switch (var1) {
         case 1 -> "A";
         case 10 -> "10";
         default -> String.valueOf(var1);
      };
   }

   public boolean isSwapActive() {
      return this.Z != we.q.s;
   }

   public boolean isSwapJuggling() {
      return (boolean)(switch (this.Z) {
         case o, P, B, z -> 1;
         default -> 0;
      });
   }

   public boolean isSwapFreezing() {
      if (this.m > 0) {
         return true;
      }

      return (boolean)(switch (this.Z) {
         case P, B, z -> 1;
         default -> 0;
      });
   }

   public boolean isSwapFreezingPassThrough() {
      return this.sX > 0;
   }

   public void signalFramePlacement(ItemFrame var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null) {
         ItemStack var3 = var2.player.getMainHandItem();
         if (var3.is(Items.FILLED_MAP) && var1.getItem().isEmpty() && this.Z != we.q.s && this.W(var3) == this.getTemplateIdForValue(this.sq)) {
            this.p = var1.getId();
         }

         this.r();
         this.x();
         this.sX = 40;
         us.R("PLACEMENT frame=" + var1.getId() + " map=" + this.W(var3) + " phase=" + this.Z + " vanilla animation");
      }
   }

   private boolean V(Minecraft var1, int var2) {
      return this.p >= 0 && var1.level != null && var1.level.getEntity(this.p) instanceof ItemFrame var3 && this.W(var3.getItem()) == var2;
   }

   public boolean q(ItemStack var1) {
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
      return this.isSwapActive() || this.sr >= 0;
   }

   public boolean isSwapSuppressing() {
      return this.Z != we.q.s ? true : this.S >= 0 && !this.s_.isEmpty();
   }

   public boolean isDealBobAllowed(ItemStack var1) {
      return this.M(var1, true);
   }

   public boolean isHotbarBobAllowed(ItemStack var1) {
      return this.M(var1, false);
   }

   private boolean M(ItemStack var1, boolean var2) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         Minecraft var3 = Minecraft.getInstance();
         if (var3 != null && var3.player != null && var3.player.getOffhandItem() == var1) {
            return false;
         }

         if (this.Z != we.q.s) {
            return false;
         }

         int var4 = this.getTemplateIdForValue(this.sq);
         if (var4 >= 0 && this.W(var1) == var4) {
            return false;
         }

         if (this.s_.isEmpty()) {
            return true;
         }

         int var5 = this.s_.peekFirst();
         int var6 = this.getTemplateIdForValue(var5);
         if (var6 >= 0 && this.W(var1) == var6) {
            return false;
         }

         if (var2) {
            this.v();
         }

         return true;
      } else {
         return true;
      }
   }

   private void v() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null) {
         if (!this.isDealtHandLocked()) {
            this.W = (boolean)1;
            this.s = var1.player.getInventory().getSelectedSlot();
            this.V = null;
            this.sX = 0;
            this.R++;
         }

         this.I = (boolean)0;
         if (var1.player.getMainHandItem().is(Items.FILLED_MAP)) {
            this.getDealtHandVisual(var1.player.getMainHandItem());
         }
      }
   }

   private void r() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.gameRenderer.itemInHandRenderer instanceof kl var2) {
         var2.larpRig$releaseHeldMap(this, var1.player.getMainHandItem());
      }
   }

   public boolean C() {
      return this.getClass() == we.class || this instanceof wk || this instanceof w0 || this instanceof w3;
   }

   private void V() {
      this.v();
      Minecraft var1 = Minecraft.getInstance();
      if (this.C() && var1.player != null && this.isDealtHandLocked() && var1.gameRenderer.itemInHandRenderer instanceof kl var2) {
         var2.larpRig$captureHeldMap(this, var1.player.getMainHandItem(), var1.player.getInventory().getSelectedSlot());
      }
   }

   private void x() {
      this.W = (boolean)0;
      this.I = (boolean)0;
      this.s = -1;
      this.V = null;
   }

   public boolean isDealtHandLocked() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.W && (var1.player == null || var1.player.getInventory().getSelectedSlot() != this.s)) {
         this.x();
      }

      return this.W;
   }

   public ItemStack getDealtHandVisual(ItemStack var1) {
      if (!this.isDealtHandLocked()) {
         return var1;
      }

      ItemStack var2 = this.getInventorySlotProjection(this.s);
      if (var2 != null) {
         this.V = var2;
         return var2;
      }

      if (!this.isDealtHandLocked()) {
         return var1;
      }

      if (this.I) {
         if (!var1.is(Items.FILLED_MAP) || this.V == null || this.W(var1) != this.W(this.V)) {
            this.x();
         }

         return var1;
      } else {
         if (var1.is(Items.FILLED_MAP)) {
            ItemStack var3 = this.getVisualOverride(var1);
            ItemStack var4 = var3 == null ? var1 : var3;
            if (this.V == null || this.W(this.V) != this.W(var4)) {
               this.V = var4.copy();
            }
         } else if (this.Z == we.q.s && this.m == 0 && this.q < 1 && this.sr < 0) {
            this.x();
            return var1;
         }

         return this.V == null ? var1 : this.V;
      }
   }

   public long getDealtHandSequence() {
      return this.R;
   }

   public boolean isDealtHandSettled() {
      return this.I;
   }

   public int getSwapValue() {
      return this.sq;
   }

   public int getSwapRealMapId() {
      return this.v;
   }

   public int R() {
      return this.sr;
   }

   public ItemStack getVisualOverride(ItemStack var1) {
      if (this.getClass() == we.class || this instanceof wk || this instanceof w0 || this instanceof w3) {
         return null;
      }

      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         Minecraft var2 = Minecraft.getInstance();
         if (var2 != null && var2.player != null && var2.player.getOffhandItem() == var1) {
            return null;
         }

         int var4 = -1;
         int var3;
         if (this.Z != we.q.s) {
            var3 = this.getTemplateIdForValue(this.sq);
            var4 = this.D;
         } else {
            if (this.q < 1) {
               return null;
            }

            var3 = this.getTemplateIdForValue(this.q);
         }

         if (var3 < 0) {
            return null;
         } else {
            int var5 = this.W(var1);
            if (var5 == var3) {
               return null;
            } else if (this.o >= 0 && var2 != null && var2.player != null && var2.player.getInventory().getItem(this.o) == var1) {
               return this.M(var1, var3);
            } else if (this.U >= 0 && var5 == this.U) {
               return this.M(var1, var3);
            } else {
               return var4 >= 1 && this.C(new MapId(var5)) == var4 ? this.M(var1, var3) : null;
            }
         }
      } else {
         return null;
      }
   }

   private ItemStack M(ItemStack var1, int var2) {
      if (this.A != null && this.W(this.A) == var2) {
         return this.A;
      }

      ItemStack var3 = var1.copy();
      var3.set(DataComponents.MAP_ID, new MapId(var2));
      this.A = var3;
      return var3;
   }

   public ItemStack getInventorySlotProjection(int var1) {
      ItemStack var2 = mg.Y(var1);
      if (var2 != null) {
         return var2;
      }

      Minecraft var3 = Minecraft.getInstance();
      if (var3.player == null) {
         return null;
      }

      if (!this.isSwapActive()) {
         if (var1 == this.o && this.q > 0) {
            ItemStack var4 = var3.player.getInventory().getItem(var1);
            int var5 = this.getTemplateIdForValue(this.q);
            if (this.W(var4) == this.U && var5 >= 0) {
               return this.M(var4, var5);
            }
         }

         return null;
      } else {
         if (this.isSwapJuggling()
            || this.sF >= 0
               && this.W(var3.player.getInventory().getItem(this.sF)) == this.v
               && var3.player.getInventory().getItem(this.sF).getCount() >= this.F[this.sF]
               && (
                  this.Q < 0
                     || this.Q == this.sF
                     || this.W(var3.player.getInventory().getItem(this.Q)) == this.K[this.Q]
                        && var3.player.getInventory().getItem(this.Q).getCount() == this.F[this.Q]
               )) {
            return this.T.get(var1);
         }

         this.T = Map.of();
         this.x();
         return null;
      }
   }

   public ItemStack projectSlot(int var1, ItemStack var2) {
      ItemStack var3 = this.getInventorySlotProjection(var1);
      return var3 == null ? var2 : var3;
   }

   public void B(int var1) {
      if (this.C() && this.isDealtHandLocked() && var1 == this.s) {
         this.x();
      }

      if (this.T.containsKey(var1) && this.isSwapActive()) {
         this.D(Minecraft.getInstance(), "projected card manually dropped; no replay");
      }
   }

   public boolean shouldSuppressOpen(MenuType<?> var1) {
      if (this.d()) {
         this.sm = null;
         this.S = -1;
         return false;
      } else {
         return var1 != null && var1 == MenuType.GENERIC_3x3 && this.isSwapSuppressing();
      }
   }

   public void onSwapContainerOpened(MenuType<?> var1, int var2) {
      if (var2 >= 0) {
         if (var1 == null || var1 == MenuType.GENERIC_3x3) {
            if (this.isSwapSuppressing()) {
               if (this.sm != null && this.sm.N) {
                  this.D(Minecraft.getInstance(), "container replaced; no replay");
               } else {
                  Minecraft var3 = Minecraft.getInstance();
                  this.sm = var3.player == null ? null : new ug(var3.player.containerMenu);
                  we.q var4 = this.Z;
                  this.S = var2;
                  us.R("OPEN arrived syncId=" + var2 + " phase=" + var4 + " clicksSinceOpen=0");
                  te.R("SWAP " + this.E() + " container opened syncId=" + var2);
               }
            }
         }
      }
   }

   public void abortSwap() {
      this.x();
      if (this.sm != null && this.sm.N && this.isSwapActive()) {
         this.D(Minecraft.getInstance(), "cancelled after transfer; no replay");
      } else if (this.Z == we.q.s) {
         if (this.S >= 0) {
            this.y(Minecraft.getInstance());
         }

         this.e();
         this.m = 0;
      } else if (this.y(Minecraft.getInstance())) {
         this.j(3, "abort");
      } else {
         this.y(Minecraft.getInstance());
         this.Z = we.q.s;
         this.e();
         this.m = 0;
         te.R("SWAP " + this.E() + " aborted");
      }
   }

   protected boolean supportsPhysicalSwap() {
      return this.getClass() == we.class;
   }

   protected BlockPos getAssignedDispenserPosition(Minecraft var1) {
      if (this instanceof wk) {
         BlockPos var2 = gj.c().n(this.z());
         return var2 != null ? var2 : gj.c().q(gj.d.x);
      } else {
         return this instanceof wc ? gj.c().q(gj.d.Z) : gj.c().q(gj.d.m);
      }
   }

   private void D(Minecraft var1) {
      ArrayList var2 = new ArrayList();
      Map var3 = this.E();

      for (Entry var5 : var3.entrySet()) {
         if ((Integer)var5.getValue() > this.f.getOrDefault(var5.getKey(), 0)) {
            var2.add((Integer)var5.getKey());
         }
      }

      if (!var2.isEmpty()) {
         if (this.s_.isEmpty()) {
            te.R("SWAP " + this.E() + " rise ignored (no plan armed) increased=" + var2 + " queue=" + this.pendingLabel());
         } else {
            int var7 = this.s_.peekFirst();
            int var8;
            if (var2.size() == 1) {
               var8 = (Integer)var2.get(0);
            } else {
               if (this.U < 0 || !var2.contains(this.U)) {
                  this.p();
                  return;
               }

               var8 = this.U;
            }

            int var6 = this.C(new MapId(var8));
            if (var6 < 1) {
               this.p();
            } else {
               te.R(
                  "SWAP "
                     + this.E()
                     + " deal real="
                     + this.M(var6)
                     + " (map "
                     + var8
                     + (this.U == var8 ? ", prearmed" : ", iterated")
                     + ") increased="
                     + var2
                     + " pending="
                     + this.M(var7)
               );
               if (var6 == var7) {
                  this.h(var6, var6);
                  this.e();
                  this.D();
               } else if (!this.swapWorthwhile(var6, var7)) {
                  this.h(var6, var6);
                  te.R("SWAP " + this.E() + " skip (dealer already wins) real=" + this.M(var6) + " armed=" + this.M(var7) + " queue=" + this.pendingLabel());
                  this.e();
                  this.D();
               } else {
                  this.U = var8;
                  this.q = var7;
                  this.V();
                  if (!this.o(var1, var8, var6)) {
                     if (this.t(var1, var8) >= 0) {
                        this.sr = var8;
                        this.j = var6;
                        this.se = var7;
                        this.e();
                     } else {
                        this.e();
                        this.D();
                     }
                  }
               }
            }
         }
      }
   }

   protected boolean swapWorthwhile(int var1, int var2) {
      return s3.S().T(var1, var2);
   }

   private boolean o(Minecraft var1, int var2, int var3) {
      if (var1 == null || var1.player == null || var1.level == null) {
         return false;
      }

      if (!this.supportsPhysicalSwap()) {
         return false;
      }

      we var4 = Y();
      if (var4 != this && var4.isSwapActive()) {
         return false;
      }

      if (this.s_.isEmpty()) {
         return false;
      }

      int var5 = this.s_.peekFirst();
      if (var5 < 1 || var5 > 10 || var5 == var3) {
         return false;
      }

      if (this.getTemplateForValue(var5) == null || var3 < 1) {
         return false;
      }

      if (!this.swapWorthwhile(var3, var5)) {
         return false;
      }

      int var6 = this.q(var1, var2);
      if (mg.S(var1) && var6 >= 0 && mg.O(this, var6, var5)) {
         this.U(var3, var5);
         return true;
      }

      if (this.sa > 0) {
         return false;
      }

      if (M(var1.gui.screen())) {
         return false;
      }

      if (!var1.player.containerMenu.getCarried().isEmpty() || !var1.player.inventoryMenu.getCarried().isEmpty()) {
         return false;
      }

      if (this.t(var1, var2) < 0) {
         return false;
      }

      int var7 = this.q(var1, var2);
      if (var7 < 0) {
         return false;
      }

      this.sF = var7;
      BlockPos var8 = this.getAssignedDispenserPosition(var1);
      if (var8 == null) {
         return false;
      }

      this.i = var8.immutable();
      this.p = -1;
      this.v = var2;
      this.D = var3;
      this.sq = var5;
      this.k = this.E();
      this.k.merge(var2, -1, Integer::sum);
      this.k.merge(this.getTemplateIdForValue(var5), 1, Integer::sum);
      this.K = new int[36];
      this.F = new int[36];

      for (int var9 = 0; var9 < 36; var9++) {
         ItemStack var10 = var1.player.getInventory().getItem(var9);
         this.K[var9] = this.W(var10);
         this.F[var9] = var10.getCount();
      }

      this.X = (boolean)0;
      DispenserMenu var12 = this.q(var1);
      if (var12 != null && this.sm != null && this.sm.Q == var12) {
         this.sm = this.sm.n();
      } else {
         this.sm = null;
      }

      this.l = new t0();
      this.S = var12 != null ? var12.containerId : -1;
      us.R("BEGIN swap real=" + this.M(var3) + " ->" + this.M(var5) + " retainedSyncId=" + this.S + " cooldown=" + this.sa + " phase=" + this.Z);
      this.C = -1;
      this.s9 = -1;
      this.y = -1;
      this.x = 0;
      this.N = 0;
      this.sa = 0;
      this.k();
      this.V();
      this.Q = ws.w(var7, this.F, var3x -> {
         ItemStack var4x = var1.player.getInventory().getItem(var3x);
         return this.W(var4x) == this.getTemplateIdForValue(var5) && var4x.getCount() < var4x.getMaxStackSize();
      });
      ItemStack var13 = var1.player.getInventory().getItem(var7);
      ItemStack var11 = var13.copyWithCount(1);
      var11.set(DataComponents.MAP_ID, this.getTemplateForValue(var5));
      this.T = ws.C(
         var7,
         this.Q,
         this.F[var7],
         this.Q >= 0 && this.Q != var7 ? this.F[this.Q] : 0,
         var13,
         this.Q >= 0 && this.F[this.Q] > 0 && this.Q != var7 ? var1.player.getInventory().getItem(this.Q) : var11,
         ItemStack::copyWithCount
      );
      this.V(we.q.e);
      this.s1 = 0;
      this.Y = -20;
      te.R("SWAP BEGIN " + this.E() + " real=" + this.M(var3) + " ->" + this.M(var5));
      this.G("swap begin");
      this.n(var1);
      return true;
   }

   private void U(int var1, int var2) {
      this.w(var1, var2, true);
      this.k();
      this.e();
      this.r();
      this.x();
      this.D();
      te.R("VISUAL " + this.E() + " real=" + var1 + " shown=" + var2);
   }

   public void onInventorySynchronized(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.sm != null && this.isSwapActive()) {
         this.sm.P("receive full sync=" + var1 + " size=" + var2.size() + " cursorCount=" + var3.getCount());
      }

      Minecraft var4 = Minecraft.getInstance();
      if (this.sm != null && var4.player != null && var4.player.containerMenu == this.sm.Q && var1 == this.sm.Q.containerId) {
         this.sm.s(var2, var3);
         if (this.sm.W) {
            this.l.I();
         }

         this.l();
      }
   }

   public void onServerSlot(int var1, int var2, ItemStack var3) {
      if (this.sm != null && this.isSwapActive()) {
         this.sm.P("receive slot sync=" + var1 + " slot=" + var2 + " map=" + this.W(var3) + " count=" + var3.getCount());
      }

      Minecraft var4 = Minecraft.getInstance();
      if (this.sm != null && var4.player != null && var4.player.containerMenu == this.sm.Q) {
         if (var1 == -1 && var2 == -1) {
            this.sm.W(-1, var3);
         } else if (var1 == this.sm.Q.containerId) {
            this.sm.W(var2, var3);
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
      if (this.sm != null && this.isSwapActive()) {
         this.sm.P("receive player slot=" + var1 + " map=" + this.W(var2) + " count=" + var2.getCount());
      }

      Minecraft var3 = Minecraft.getInstance();
      if (this.sm != null && var3.player != null && var3.player.containerMenu == this.sm.Q) {
         for (Slot var5 : this.sm.Q.slots) {
            if (var5.container == var3.player.getInventory() && var5.getContainerSlot() == var1) {
               this.sm.W(var5.index, var2);
               this.l();
               return;
            }
         }
      }
   }

   public int clickRevision(AbstractContainerMenu var1) {
      return this.sm != null && var1 == this.sm.Q && this.sm.K ? 32768 : var1.getStateId();
   }

   public void C(BlockPos var1, String var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null && var3.level != null && var1.equals(this.getAssignedDispenserPosition(var3))) {
         us.R(
            var2
               + " "
               + this.o(var3)
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
      if (this.Z == we.q.x) {
         us.R("OPEN ACK seq=" + var1 + " " + this.l.U());
      }

      this.l.P(var1);
      this.l();
   }

   private void l() {
      Minecraft var1 = Minecraft.getInstance();
      if (!this.r && this.sm != null && !this.sm.P && this.isSwapActive() && var1.player != null) {
         we.q var2 = this.Z;
         this.r = (boolean)1;

         try {
            if (this.Z == we.q.x && this.S >= 0) {
               this.V(we.q.o);
            }

            if (this.isSwapJuggling()) {
               this.p(var1);
            }
         } finally {
            this.r = (boolean)0;
            if (this.Z != var2) {
               us.R("PACKET continuation " + this.E() + " " + var2 + " -> " + this.Z + " syncId=" + this.S);
            }
         }
      }
   }

   public boolean p() {
      return this.d;
   }

   public boolean blocksUserInteraction() {
      return this.isSwapActive() && (this.sm == null || !this.sm.P);
   }

   public boolean allowsRecoveryOpen(BlockPos var1) {
      return this.Z == we.q.x && this.S < 0 && this.sm == null && var1 != null && var1.equals(this.i);
   }

   public boolean blocksClose() {
      return this.blocksUserInteraction();
   }

   public boolean p(AbstractContainerMenu var1) {
      return var1 instanceof InventoryMenu && (this.isSwapActive() || this.sm != null);
   }

   boolean d() {
      return !this.isSwapActive() && this.sm != null && this.sm.S();
   }

   public void e(AbstractContainerMenu var1) {
      if (this.p(var1) && this.d()) {
         this.y(Minecraft.getInstance());
      }
   }

   public boolean hidesCursor(AbstractContainerMenu var1) {
      return this.isSwapJuggling() && !this.T.isEmpty() && (this.p(var1) || this.sm != null && var1 == this.sm.Q);
   }

   private String o(Minecraft var1) {
      return "phase="
         + this.Z
         + " distance="
         + this.m(var1)
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

   public void N(int var1, String var2) {
      if (this.sm != null && this.isSwapActive() && this.sm.Q.containerId == var1) {
         this.sm.P(var2);
      }
   }

   public void y(int var1) {
      if (this.isSwapActive()) {
         us.R("SERVER CLOSE " + this.E() + " sync=" + var1 + " " + this.o(Minecraft.getInstance()));
      }
   }

   private void D(Minecraft var1, String var2) {
      us.R("BATCH RECOVERY " + this.E() + " reason=" + var2 + " " + this.o(var1) + " " + (this.sm == null ? "batch=null" : this.sm.p()));
      te.Z(this.E(), "Manual recovery required: " + var2 + " [phase=" + this.Z + "; details: swap-open.log]");
      AbstractContainerMenu var3 = this.sm == null ? null : this.sm.Q;
      this.sm = null;
      this.S = -1;
      this.Z = we.q.s;
      this.s_.clear();
      this.f.clear();
      this.k();
      this.e();
      this.r();
      this.x();
      if (var1.player != null && var1.player.containerMenu instanceof DispenserMenu var4 && (var4 == var3 || var1.gui.screen() == null)) {
         var1.gui.setScreen(new DispenserScreen(var4, var1.player.getInventory(), Component.literal(this.E() + " recovery: " + var2)));
      }
   }

   private void p(Minecraft var1) {
      ug var2 = this.sm;
      if (var2 == null || var1.player.containerMenu != var2.Q || M(var1.gui.screen())) {
         this.D(var1, "handler lost; no replay");
      } else if (!var2.W) {
         if (!this.r && ++this.L > 100) {
            this.D(var1, "initial authoritative inventory missing");
         }
      } else if (this.l.x() && !this.l.S()) {
         if (!this.r && ++this.L > 100) {
            this.D(var1, "open acknowledgement missing");
         }
      } else if (!var2.F(!this.r)) {
         if (var2.y != null) {
            this.D(var1, var2.y);
         }
      } else if (!var2.N && mg.S(var1)) {
         if (var2.S() && mg.O(this, this.sF, this.sq)) {
            this.y(var1);
            this.Z = we.q.s;
            this.U(this.D, this.sq);
         }
      } else {
         DispenserMenu var3 = (DispenserMenu)var2.Q;
         if (this.Z == we.q.o) {
            this.H = -1;
            this.sZ = -1;

            for (Slot var5 : var3.slots) {
               if (var5.container == var1.player.getInventory() && var5.getContainerSlot() == this.sF && this.W(var5.getItem()) == this.v) {
                  this.H = var5.index;
               }
            }

            if (!var2.X()) {
               this.t(var1, var2, "initial local/server ledger or cursor mismatch", "");
               return;
            }

            if (this.H < 0) {
               if (var2.S()) {
                  this.n(false, "draw moved before transfer");
               } else {
                  this.t(var1, var2, "real arrival unavailable", "");
               }

               return;
            }

            if (var3.getSlot(this.H).getItem().getCount() < this.F[this.sF]) {
               this.t(var1, var2, "original arrival quantity changed before transfer", "");
               return;
            }

            ArrayList var25 = new ArrayList();
            ArrayList var26 = new ArrayList();
            ArrayList var6 = new ArrayList();
            int var7 = this.getTemplateIdForValue(this.sq);
            int var8 = var2.F.get(this.H).getCount() == 1 ? 1 : 0;

            for (Slot var10 : var3.slots) {
               int var11 = var10.getContainerSlot();
               if (var10.container == var1.player.getInventory()
                  && var11 >= 0
                  && var11 < 36
                  && var11 != this.sF
                  && this.K[var11] == var7
                  && this.F[var11] > 0
                  && this.W(var10.getItem()) == var7
                  && var10.getItem().getCount() == this.F[var11]) {
                  var26.add(var10.index);
               }
            }

            if (var8 != 0) {
               var26.add(this.H);
            }

            int var27 = var26.size();

            for (Slot var31 : var3.slots) {
               if (!(var31.container instanceof Inventory)) {
                  if (var7 >= 0 && this.W(var31.getItem()) == var7) {
                     var25.add(var31.index);
                  }

                  if (!var31.getItem().isEmpty() && this.W(var31.getItem()) == this.v) {
                     var6.add(var31.index);
                  }
               } else if (var8 == 0
                  && var31.container == var1.player.getInventory()
                  && var31.getContainerSlot() >= 0
                  && var31.getContainerSlot() < 36
                  && this.F[var31.getContainerSlot()] == 0
                  && var31.getItem().isEmpty()
                  && var2.F.get(var31.index).isEmpty()) {
                  var26.add(var31.index);
               }
            }

            var26.subList(var27, var26.size()).sort(Comparator.comparingInt(var1x -> var3.getSlot(var1x).getContainerSlot()));

            for (Slot var32 : var3.slots) {
               if (!(var32.container instanceof Inventory) && var32.getItem().isEmpty() && var2.F.get(var32.index).isEmpty()) {
                  var6.add(var32.index);
               }
            }

            var6.removeIf(var2x -> !this.allowsReturnSlot(var3.getSlot(var2x).getContainerSlot(), this.D));
            tv.p var30 = tv.j(
               var25,
               var26,
               var6,
               (var3x, var4) -> var2.J(var1, var3x, var4, var4 == this.H, true).batch() != null,
               var3x -> var2.K(var1, this.H, var3x) != null
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
                     ug.r var23 = var2.J(var1, var17, var22, var22 == this.H, true);
                     if (var23.batch() != null) {
                        var18 = var22;
                        break;
                     }

                     var20.merge(var23.reason(), 1, Integer::sum);
                     if (var14 == null) {
                        var14 = var23.reason();
                     }

                     if (var19 == null) {
                        var19 = "destination=" + var22 + " " + var23.reason() + " " + var2.j(var17, var22);
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
                        ug.r var36 = var2.J(var1, this.H, var35.index, false, true);
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
                           .append(var2.j(this.H, var35.index));
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

               this.t(var1, var2, var33, var12.toString());
               return;
            }

            this.C = var30.source();
            this.sZ = var30.destination();
            this.y = var30.returnSlot();
            this.s9 = var3.getSlot(this.sZ).getContainerSlot();
            this.Q = this.s9;
            this.T = ws.C(
               this.sF,
               this.Q,
               this.F[this.sF],
               this.Q == this.sF ? 0 : this.F[this.Q],
               var2.F.get(this.H),
               this.Q != this.sF && !var2.F.get(this.sZ).isEmpty() ? var2.F.get(this.sZ) : var2.F.get(this.C),
               ItemStack::copyWithCount
            );
            this.V(we.q.P);
            var2.b(var1, this.H, this.y, this.E() + " RETURN_REAL", true);
         } else if (this.Z == we.q.P) {
            this.V(we.q.B);
            var2.b(var1, this.C, this.sZ, this.E() + " DELIVER_ARMED", true);
         } else {
            if (!var2.X()) {
               this.D(var1, "final quantity ledger changed");
               return;
            }

            this.g(true, "two one-card batches confirmed");
         }

         if (var2.y != null) {
            this.D(var1, var2.y);
         }
      }
   }

   private void t(Minecraft var1, ug var2, String var3, String var4) {
      StringBuilder var5 = new StringBuilder(
         "SWAP "
            + this.E()
            + " PREFLIGHT REJECT sync="
            + var2.Q.containerId
            + " revision="
            + var2.Q.getStateId()
            + " realMap="
            + this.v
            + " armedMap="
            + this.getTemplateIdForValue(this.sq)
            + " arrival="
            + this.H
            + " arrivalInv="
            + this.sF
            + " source="
            + this.C
            + " destination="
            + this.sZ
            + " cursorSafe="
            + var2.S()
            + " "
            + var2.o(-1)
            + " reason="
            + var3
            + "\n"
            + var4
      );

      for (Slot var7 : var2.Q.slots) {
         var5.append("\n").append(var2.o(var7.index));
         if (var7.container == var1.player.getInventory() && var7.getContainerSlot() >= 0 && var7.getContainerSlot() < 36) {
            var5.append(" inv=")
               .append(var7.getContainerSlot())
               .append(" drawBaselineMap=")
               .append(this.K[var7.getContainerSlot()])
               .append(" drawBaselineCount=")
               .append(this.F[var7.getContainerSlot()]);
         }
      }

      te.R(var5.toString());
      this.D(var1, var3);
   }

   private void n(Minecraft var1) {
      if (this.Z != we.q.o && this.Z != we.q.P && this.Z != we.q.B && this.Z != we.q.z) {
         this.s1++;
         if (this.s1 > 1200) {
            this.n(false, "whole-swap timeout");
         } else {
            switch (this.Z) {
               case e:
                  this.L++;
                  if (this.S >= 0) {
                     if (this.q(var1) != null) {
                        us.R("REQUEST retained harness live syncId=" + this.S + " tick=" + this.L);
                        this.V(we.q.o);
                        this.p(var1);
                     } else if (var1.player != null && var1.player.containerMenu instanceof DispenserMenu var2) {
                        this.S = var2.containerId;
                        us.R("REQUEST adopting live open syncId=" + var2.containerId + " tick=" + this.L + " screenOpen=" + (var1.gui.screen() != null));
                        te.R("SWAP " + this.E() + " retained harness vanished - adopting live open syncId=" + var2.containerId);
                        this.V(we.q.x);
                     } else if (!M(var1.gui.screen())) {
                        this.S = -1;
                        us.R("REQUEST retained harness gone - reopening tick=" + this.L);
                        te.R("SWAP " + this.E() + " retained harness vanished - reopening");
                        this.r(var1);
                     } else {
                        us.R(
                           "REQUEST retained gone but screen open tick="
                              + this.L
                              + " screen="
                              + (var1.gui.screen() == null ? "null" : var1.gui.screen().getClass().getSimpleName())
                              + " handler="
                              + (var1.player.containerMenu == null ? "null" : var1.player.containerMenu.getClass().getSimpleName())
                        );
                     }
                  } else if (this.L >= 1) {
                     us.R("REQUEST fresh - compact requestContainerOpen tick=" + this.L);
                     this.r(var1);
                  }
                  break;
               case x:
                  this.L++;
                  if (this.S >= 0) {
                     us.R("WAIT_OPEN open arrived tick=" + this.L + " syncId=" + this.S);
                     te.R("SWAP " + this.E() + " open-arrived tick=" + this.L + " syncId=" + this.S);
                     this.V(we.q.o);
                     this.p(var1);
                  } else if (this.L >= 100 && this.sm == null) {
                     this.n(false, "dispenser did not open; no cards moved");
                  } else if (this.L >= 660) {
                     us.R(
                        "WAIT_OPEN open timeout tick="
                           + this.L
                           + " screen="
                           + (var1.gui.screen() == null ? "null" : var1.gui.screen().getClass().getSimpleName())
                     );
                     this.Z("open timeout");
                  } else if (this.l.g(this.s1)) {
                     this.q(var1);
                  }
                  break;
               default:
                  this.Z = we.q.s;
            }
         }
      } else {
         this.p(var1);
      }
   }

   private void r(Minecraft var1) {
      if (this.i == null) {
         this.n(false, "dispenser unknown");
      } else {
         this.q(var1);
         this.V(we.q.x);
      }
   }

   private void q(Minecraft var1) {
      if (this.l.g(this.s1) && this.s1 - this.Y >= 4) {
         if (this.i != null && var1 != null && var1.player != null && var1.gameMode != null && var1.getConnection() != null && var1.level != null) {
            BlockHitResult var2 = this.Z(var1);
            if (var2 == null) {
               us.R("open blocked: no visible dispenser face pos=" + this.i);
            } else {
               boolean var3 = var1.player.isShiftKeyDown();
               double var4 = this.m(var1);
               us.R(
                  "open-issue phase="
                     + this.Z
                     + " tick="
                     + this.L
                     + " sneaking="
                     + var3
                     + " dist="
                     + String.format("%.1f", var4)
                     + " syncId="
                     + this.S
                     + " pass="
                     + this.N
               );
               te.R(
                  "SWAP "
                     + this.E()
                     + " open-issue phase="
                     + this.Z
                     + " tick="
                     + this.L
                     + " sneaking="
                     + var3
                     + " dist="
                     + String.format("%.1f", var4)
                     + " syncId="
                     + this.S
               );
               if (!var1.player.isShiftKeyDown() && !var1.options.keyUse.isDown()) {
                  this.Z(var1, var2, var1x -> {
                     this.Y = this.s1;
                     this.l.p(var1x, this.s1);
                  });
               }
            }
         }
      }
   }

   protected void Z(Minecraft var1, BlockHitResult var2, IntConsumer var3) {
      tj.V(
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
               this.d = (boolean)1;

               try {
                  var1.gameMode.useItemOn(var1.player, InteractionHand.MAIN_HAND, var2);
                  var3.accept(((qaee82196bfd62460)var1.level).LarpRig$getPendingUpdateManager().currentSequence());
                  return true;
               } finally {
                  this.d = (boolean)0;
               }
            } else {
               return false;
            }
         }
      );
   }

   static int O(int var0, int var1) {
      return var0 >= 0 && var0 < 9 ? var0 : var1;
   }

   private BlockHitResult Z(Minecraft var1) {
      return U(var1, this.i);
   }

   protected static BlockHitResult U(Minecraft var0, BlockPos var1) {
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

   private void Z(String var1) {
      if (this.N < 6) {
         this.N++;
         us.R("retry (" + var1 + ") pass=" + this.N + " phase=" + this.Z + " tick=" + this.L);
         te.R("SWAP " + this.E() + " retry (" + var1 + ")");
         this.j(1, "retry " + var1);
      } else {
         us.R("CRITICAL fail (" + var1 + ") after pass=" + this.N);
         this.n(false, var1);
      }
   }

   private void y(Minecraft var1) {
      this.sm = null;
      if (var1 != null && var1.player != null) {
         if (this.S >= 0 && var1.player.containerMenu != null && var1.player.containerMenu.containerId == this.S) {
            us.R("close suppressed container syncId=" + this.S + " phase=" + this.Z);
            if (!(var1.gui.screen() instanceof InventoryScreen) && !(var1.gui.screen() instanceof ChatScreen)) {
               var1.player.closeContainer();
            } else {
               var1.getConnection().send(new ServerboundContainerClosePacket(this.S));
            }
         }

         var1.player.containerMenu = var1.player.inventoryMenu;
         this.S = -1;
      }
   }

   private void n(boolean var1, String var2) {
      if (!var1) {
         te.Z(this.E(), "Swap failed: " + var2);
      }

      te.R("SWAP " + this.E() + " " + (var1 ? "OK" : "FAIL") + " (" + var2 + ") real=" + this.M(this.D) + " ->" + this.M(this.sq));
      if (!var1 && this.y(Minecraft.getInstance())) {
         this.j(2, "cleanup " + var2);
      } else {
         this.g(var1, var2);
      }
   }

   private void g(boolean var1, String var2) {
      Minecraft var3 = Minecraft.getInstance();
      ug var4 = this.sm;
      if (var4 != null) {
         us.R("BATCH TRACE " + this.E() + " " + this.o(var3) + " " + var4.p());
      }

      us.R("finishSwapDecided ok=" + var1 + " reason=" + var2 + " syncId=" + this.S + " queue=" + this.pendingLabel() + " phase=" + this.Z);
      this.y(var3);
      this.sa = 0;
      this.b = var1 ? 12 : 0;
      if (var1) {
         this.h(this.D, this.sq);
      }

      this.D();
      if (var1 && !this.s_.isEmpty() && var4 != null) {
         this.f = new HashMap<>(this.k);
         bx.completeDrawBaseline(this.K, this.F, this.sF, this.s9, this.getTemplateIdForValue(this.sq));
         System.arraycopy(this.K, 0, this.e, 0, 36);
         System.arraycopy(this.F, 0, this.t, 0, 36);
      }

      this.e();
      this.Z = we.q.s;
      this.X = (boolean)0;
      if (var1) {
         ItemStack var5 = var3.player.getMainHandItem();
         if (!this.isDealtHandLocked()
            || !var5.is(Items.FILLED_MAP)
            || this.W(var5) < 0
            || (this.V == null || this.W(var5) != this.W(this.V)) && this.W(var5) != this.getTemplateIdForValue(this.sq)) {
            this.r();
            this.x();
         } else {
            this.V = var5.copy();
            this.I = (boolean)1;
         }
      }

      if (!var1) {
         this.r();
         this.x();
      }

      this.m = var1 ? 16 : 0;
      this.G("swap end " + (var1 ? "ok" : "fail") + " " + var2);
      if (this.sr >= 0 && !this.s_.isEmpty() && this.s_.peekFirst() == this.se) {
         this.U = this.sr;
         this.q = this.se;
         this.F();
      } else {
         this.k();
         this.e();
      }
   }

   private void j(int var1, String var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.y(var3)) {
         this.y(var3);
         this.A(var3, var1, var2);
      } else {
         this.D(var3, var2 + ": occupied cursor before batch");
      }
   }

   private void A(Minecraft var1, int var2, String var3) {
      switch (var2) {
         case 1:
            this.x = 0;
            this.S = -1;
            this.L = 1;
            this.Z = we.q.e;
            break;
         case 2:
            this.g(false, var3);
            break;
         default:
            this.y(var1);
            this.Z = we.q.s;
            this.e();
            te.R("SWAP " + this.E() + " aborted");
      }
   }

   private boolean y(Minecraft var1) {
      DispenserMenu var2 = this.q(var1);
      return var2 != null && !var2.getCarried().isEmpty();
   }

   private void D() {
      if (!this.s_.isEmpty()) {
         te.R("PLAYED " + this.E() + " next=" + this.s_.peekFirst());
         this.s_.pollFirst();
         if (this.s_.isEmpty()) {
            this.f.clear();
            if (this.Z == we.q.s && this.S >= 0) {
               if (this.sm != null && this.sm.S()) {
                  this.y(Minecraft.getInstance());
               } else {
                  this.D(Minecraft.getInstance(), "queue ended without a safe cursor snapshot");
               }
            }
         } else {
            this.p();
         }
      }
   }

   private void V(we.q var1) {
      this.Z = var1;
      if (this.sm != null) {
         this.sm.P(this.o(Minecraft.getInstance()));
      }

      this.L = 0;
   }

   private void k() {
      this.sr = -1;
      this.j = 0;
      this.se = 0;
   }

   private void e() {
      this.T = Map.of();
      this.Q = -1;
      this.U = -1;
      this.q = -1;
      this.o = -1;
      this.A = null;
   }

   public int getSwapSyncId() {
      return this.S;
   }

   private void F() {
      this.o = -1;
      if (this.U >= 0) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null) {
            this.o = this.sM >= 0 && this.sM < 36 && this.W(var1.player.getInventory().getItem(this.sM)) == this.U ? this.sM : this.t(var1, this.U);
         }
      }
   }

   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      if (var3 >= 0) {
         int var4 = this.C(new MapId(var3));
         if (var4 >= 1) {
            Minecraft var5 = Minecraft.getInstance();
            Object var6 = var1 == 0 ? var5.player.inventoryMenu : var5.player.containerMenu;
            if (var2 >= 0 && var2 < ((AbstractContainerMenu)var6).slots.size()) {
               Slot var7 = var6.getSlot(var2);
               if (var7.container == var5.player.getInventory() && var7.getContainerSlot() < 36) {
                  this.sM = var7.getContainerSlot();
                  if (this.s_.isEmpty() || this.Z != we.q.s || this.q(var5, var3) >= 0) {
                     if (this.s_.isEmpty()) {
                        this.U = var3;
                        this.F();
                        te.R("SWAP " + this.E() + " slot-update sync=" + var1 + " slot=" + var2 + " real=" + this.M(var4) + " (no plan armed)");
                     } else if (this.Z == we.q.s) {
                        int var8 = this.s_.peekFirst();
                        if (var4 == var8) {
                           te.R("SWAP " + this.E() + " slot-update real=" + this.M(var4) + " == pending (no swap needed)");
                        } else {
                           this.U = var3;
                           this.q = var8;
                           this.F();
                           this.V();
                           if (!this.swapWorthwhile(var4, var8)) {
                              this.h(var4, var4);
                              te.R(
                                 "SWAP "
                                    + this.E()
                                    + " skip (dealer already wins) real="
                                    + this.M(var4)
                                    + " armed="
                                    + this.M(var8)
                                    + " queue="
                                    + this.pendingLabel()
                              );
                              this.e();
                              this.D();
                           } else {
                              if (!this.o(Minecraft.getInstance(), var3, var4) && this.t(Minecraft.getInstance(), var3) >= 0) {
                                 this.sr = var3;
                                 this.j = var4;
                                 this.se = var8;
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

   private DispenserMenu q(Minecraft var1) {
      if (var1 != null && var1.player != null && !M(var1.gui.screen())) {
         return var1.player.containerMenu instanceof DispenserMenu var2 && var2.containerId == this.S ? var2 : null;
      } else {
         return null;
      }
   }

   protected boolean allowsReturnSlot(int var1, int var2) {
      return true;
   }

   private int h(DispenserMenu var1, int var2) {
      if (var2 < 0) {
         return -1;
      }

      for (Slot var4 : var1.slots) {
         if (var4 != null && !(var4.container instanceof Inventory)) {
            ItemStack var5 = var4.getItem();
            if (var5 != null && var5.is(Items.FILLED_MAP) && this.W(var5) == var2) {
               return var4.index;
            }
         }
      }

      return -1;
   }

   private int B(DispenserMenu var1, ItemStack var2) {
      int var3 = this.W(var2);
      int var4 = this.C((MapId)var2.getComponents().get(DataComponents.MAP_ID));
      int var5 = -1;
      if (var4 >= 1 && var3 >= 0) {
         var5 = this.h(var1, var3);
         if (var5 < 0) {
            var5 = this.containerIndexForValue(var4);
         }
      } else {
         var5 = this.C;
      }

      if (var5 >= 0) {
         Slot var6 = var1.getSlot(var5);
         if (var6 != null) {
            ItemStack var7 = var6.getItem();
            if (var7 != null && !var7.isEmpty() && this.W(var7) != var3) {
               var5 = -1;
            }
         }
      }

      if (var5 < 0) {
         var5 = this.b(var1);
      }

      return var5;
   }

   private int b(DispenserMenu var1) {
      for (Slot var3 : var1.slots) {
         if (var3 != null && !(var3.container instanceof Inventory) && var3.getItem().isEmpty()) {
            return var3.index;
         }
      }

      return -1;
   }

   private int S(DispenserMenu var1, int var2) {
      int var3 = this.getTemplateIdForValue(var2);
      return var3 < 0 ? -1 : this.h(var1, var3);
   }

   private int t(Minecraft var1, int var2) {
      if (var1 != null && var1.player != null) {
         Inventory var3 = var1.player.getInventory();

         for (int var4 = 0; var4 < var3.getContainerSize(); var4++) {
            ItemStack var5 = var3.getItem(var4);
            if (var5 != null && var5.is(Items.FILLED_MAP) && this.W(var5) == var2) {
               return var4;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private int a(DispenserMenu var1, int var2) {
      if (var2 < 0) {
         return -1;
      }

      for (Slot var4 : var1.slots) {
         if (var4 != null && var4.container instanceof Inventory) {
            ItemStack var5 = var4.getItem();
            if (var5 != null && var5.is(Items.FILLED_MAP) && this.W(var5) == var2) {
               return var4.index;
            }
         }
      }

      return -1;
   }

   private boolean F(Minecraft var1, int var2) {
      return this.t(var1, var2) >= 0;
   }

   private int d(int var1) {
      return var1 >= 0 && var1 < 9 ? var1 + 36 : var1;
   }

   private boolean t(DispenserMenu var1, int var2) {
      return this.j(var1, this.getTemplateIdForValue(var2));
   }

   private boolean j(DispenserMenu var1, int var2) {
      ItemStack var3 = var1.getCarried();
      return var3 != null && var3.is(Items.FILLED_MAP) && this.W(var3) == var2;
   }

   private boolean w(DispenserMenu var1, int var2, int var3) {
      return this.N(var1, var2, this.getTemplateIdForValue(var3));
   }

   private boolean N(DispenserMenu var1, int var2, int var3) {
      if (var2 >= 0 && var2 < var1.slots.size()) {
         Slot var4 = var1.getSlot(var2);
         if (var4 == null) {
            return false;
         }

         ItemStack var5 = var4.getItem();
         return var5 != null && var5.is(Items.FILLED_MAP) && this.W(var5) == var3;
      } else {
         return false;
      }
   }

   private double m(Minecraft var1) {
      if (var1 != null && var1.player != null && this.i != null) {
         AABB var2 = new AABB(this.i);
         return Math.sqrt(var2.distanceToSqr(var1.player.getEyePosition()));
      } else {
         return -1.0;
      }
   }

   private void G(String var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         StringBuilder var3 = new StringBuilder();
         int var4 = var2.player.getInventory().getSelectedSlot();

         for (int var5 = 0; var5 < var2.player.getInventory().getContainerSize(); var5++) {
            ItemStack var6 = var2.player.getInventory().getItem(var5);
            if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
               MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
               int var8 = var7 == null ? -1 : this.C(var7);
               if (var8 >= 1) {
                  if (!var3.isEmpty()) {
                     var3.append(", ");
                  }

                  var3.append("v").append(this.M(var8)).append("x").append(var6.getCount()).append("@").append(var5).append(var5 == var4 ? "(sel)" : "");
               }
            }
         }

         te.R("DUMP " + this.E() + " " + var1 + " selected=" + var4 + " cards=[" + var3 + "]");
      }
   }

   private int W(ItemStack var1) {
      MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
      return var2 == null ? -1 : var2.id();
   }

   private int C(MapId var1) {
      if (var1 == null) {
         return -1;
      }

      lh var2 = lh.I();
      if (var2.S() && var2.V()) {
         return var2.d(var1.id());
      }

      for (Entry var4 : this.M.entrySet()) {
         if (var4.getValue() != null && ((MapId)var4.getValue()).id() == var1.id()) {
            return (Integer)var4.getKey();
         }
      }

      return -1;
   }

   private Map<Integer, Integer> j() {
      return this.g.computeIfAbsent(this.b(), var0 -> new HashMap<>());
   }

   private Map<Integer, Integer> P() {
      return this.sN.computeIfAbsent(this.b(), var0 -> new HashMap<>());
   }

   private String b() {
      return this.J == null ? "" : this.J;
   }

   private boolean a() {
      Map var1 = this.E();

      for (Entry var3 : var1.entrySet()) {
         if ((Integer)var3.getValue() > this.f.getOrDefault(var3.getKey(), 0)) {
            return true;
         }
      }

      return false;
   }

   private Map<Integer, Integer> E() {
      HashMap var1 = new HashMap();
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         for (int var3 = 0; var3 < var2.player.getInventory().getContainerSize(); var3++) {
            this.w(var1, var2.player.getInventory().getItem(var3));
         }
      }

      if (var2 != null && var2.level != null) {
         for (Entity var4 : var2.level.entitiesForRendering()) {
            if (var4 instanceof ItemFrame var5) {
               this.w(var1, var5.getItem());
            }
         }
      }

      return var1;
   }

   private void w(Map<Integer, Integer> var1, ItemStack var2) {
      if (var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP)) {
         MapId var3 = (MapId)var2.getComponents().get(DataComponents.MAP_ID);
         if (var3 != null) {
            var1.merge(var3.id(), var2.getCount(), Integer::sum);
         }
      }
   }

   public enum q {
      s,
      e,
      x,
      o,
      P,
      B,
      z;
   }
}
