package com.larpclient;

import com.larp.obf.q5682c6a8aca9b6ec.q42bdff5f872a0552.q75e5da671f8dbf02;
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
public class ug {
   private static final ug Iz = new ug();
   private static final String Q = "";
   private static final int w = 660;
   private static final int[] m = new int[]{3, 6, 10, 15, 22, 30, 40, 52, 66, 82, 100, 120, 150, 180};
   private static final int P = 100;
   private static final int IA = 1200;
   private static final int IB = 6;
   private static final int t = 16;
   private static final int q = 40;
   private final Deque<Integer> g = new ArrayDeque<>();
   private final Map<Integer, MapId> U = new HashMap<>();
   private final Map<Integer, Integer> u = new HashMap<>();
   private final Map<String, Map<Integer, Integer>> i = new HashMap<>();
   private final Map<String, Map<Integer, Integer>> S = new HashMap<>();
   private Map<Integer, Integer> D = new HashMap<>();
   private final int[] p = new int[36];
   private final int[] l = new int[36];
   private int Ib = -1;
   private int h = -1;
   private Map<Integer, Integer> C;
   private int[] Ih;
   private int[] I6;
   private boolean E;
   private String I;
   private ug.w I0 = ug.w.v;
   private int Iv = -1;
   private int IO = -1;
   private int K = -1;
   private int y = -1;
   private int x = -1;
   private int IG = -1;
   private int W;
   private int M;
   private int R;
   private int V = -1;
   private int b;
   private int G;
   private int L;
   private int k = -1;
   private long o;
   private boolean B;
   private boolean Iu;
   private int f = -1;
   private ItemStack Y;
   private int Z;
   private int O = -1;
   private int r;
   private int d;
   private int T;
   private int z = -1;
   private int c = -1;
   private int F = -1;
   private ItemStack a;
   private BlockPos ID;
   private mw e;
   private lh n = new lh();
   private int j = -1;
   private int A = -1;
   private Map<Integer, ItemStack> v = Map.of();
   private int s = -1;
   private boolean Iw;
   private ClientLevel IT;
   private long X = -1L;
   private final Map<Integer, Integer> H = new HashMap<>();
   private int J = -20;
   private boolean N;

   protected ug() {
   }

   public static ug w() {
      return Iz;
   }

   public static boolean p() {
      return x().isSwapActive();
   }

   public static ug x() {
      ug var0 = w();
      um var1 = um.T();
      u3 var2 = u3.H();
      ue var3 = ue.V();
      uu var4 = uu.M();
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
      } else if (um.T().m()) {
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
      } else if (var0.b()) {
         return var0;
      } else if (var1.b()) {
         return var1;
      } else if (var2.b()) {
         return var2;
      } else if (var3.b()) {
         return var3;
      } else {
         return var4.b() ? var4 : var0;
      }
   }

   private boolean b() {
      return this.I0 != ug.w.v || this.z >= 0 || this.c >= 1 || this.O >= 0;
   }

   public static ug h() {
      ug var0 = x();
      if (var0.isSwapActive()) {
         return var0;
      }

      if (Iz.e != null) {
         return Iz;
      }

      u3 var1 = u3.H();
      return var1.e != null ? var1 : um.T();
   }

   private boolean e() {
      return this.I0 == ug.w.v && this.z < 0 && this.c < 1 && !this.g.isEmpty();
   }

   public void queueForce(int var1) {
      this.queueForceSequence(var1);
   }

   public void queueForceSequence(int... var1) {
      if (this.isSwapBusy()) {
         if (!this.E) {
            hh.L("SWAP " + this.r() + " queue replacement rejected while busy");
            this.E = (boolean)1;
         }
      } else {
         this.E = (boolean)0;
         this.g.clear();
         this.x();
         if (var1 != null) {
            int var2 = Math.max(1, this.maxQueuedForces());

            for (int var6 : var1) {
               if (this.g.size() >= var2) {
                  break;
               }

               if (var6 >= 1 && var6 <= 10 && this.getTemplateForValue(var6) != null) {
                  this.g.addLast(var6);
               }
            }
         }

         if (this.g.isEmpty()) {
            this.D.clear();
         } else {
            this.N();
         }
      }
   }

   private void N() {
      this.D = this.n();
      Minecraft var1 = Minecraft.getInstance();

      for (int var2 = 0; var2 < 36; var2++) {
         ItemStack var3 = var1.player == null ? ItemStack.EMPTY : var1.player.getInventory().getItem(var2);
         this.p[var2] = this.p(var3);
         this.l[var2] = var3.getCount();
      }

      this.Ib = -1;
   }

   private int Q(Minecraft var1, int var2) {
      if (this.n().getOrDefault(var2, 0) <= this.D.getOrDefault(var2, 0)) {
         return -1;
      }

      int[] var3 = new int[36];
      int[] var4 = new int[36];

      for (int var5 = 0; var5 < 36; var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         var3[var5] = this.p(var6);
         var4[var5] = var6.getCount();
      }

      return yk.dealtSlot(var2, this.p, this.l, var3, var4, this.Ib);
   }

   public void clearQueue() {
      this.abortSwap();
      this.g.clear();
      this.D.clear();
      this.E = (boolean)0;
   }

   public int getPendingValue() {
      Integer var1 = this.g.peekFirst();
      return var1 == null ? -1 : var1;
   }

   public int getQueuedValueAt(int var1) {
      if (var1 < 0) {
         return -1;
      }

      int var2 = 0;

      for (Integer var4 : this.g) {
         if (var2++ == var1) {
            return var4;
         }
      }

      return -1;
   }

   public boolean hasQueuedForce() {
      return !this.g.isEmpty();
   }

   public String pendingLabel() {
      if (this.g.isEmpty()) {
         return "none";
      }

      StringBuilder var1 = new StringBuilder();

      for (Integer var3 : this.g) {
         if (!var1.isEmpty()) {
            var1.append(" -> ");
         }

         var1.append(this.R(var3));
      }

      return var1.toString();
   }

   public void tick(Minecraft var1) {
      if (var1 != null && var1.player != null) {
         if (var1.level != null) {
            if (this.IT != var1.level) {
               this.IT = var1.level;
               this.X = var1.level.getGameTime();
            } else if (var1.level.getGameTime() == this.X) {
               return;
            }
         }

         this.X = var1.level == null ? -1L : var1.level.getGameTime();
         if (this.L > 0) {
            this.L--;
         }

         if (this.Z > 0) {
            this.Z--;
         }

         if (this.b > 0) {
            this.b--;
         }

         if (this.I0 != ug.w.v) {
            this.t(var1);
         } else {
            if (this.T > 0 && --this.T == 0) {
               this.B("post-swap settle");
            }

            if (this.O >= 0) {
               int var2 = this.O;
               int var3 = this.r;
               int var4 = this.d;
               if (this.g.isEmpty() || this.g.peekFirst() != var4) {
                  this.e();
               } else if (this.S(var1, var2) < 0) {
                  this.e();
               } else if (this.b <= 0) {
                  if (var3 == var4) {
                     this.W(var3, var3);
                     hh.L("SWAP " + this.r() + " deferred deal already matched " + this.R(var4));
                     this.e();
                     this.x();
                     this.X();
                     return;
                  }

                  if (!this.swapWorthwhile(var3, var4)) {
                     this.W(var3, var3);
                     hh.L("SWAP " + this.r() + " deferred skip real=" + this.R(var3) + " armed=" + this.R(var4));
                     this.e();
                     this.x();
                     this.X();
                     return;
                  }

                  if (this.U(var1, var2, var3)) {
                     return;
                  }
               }

               if (this.O >= 0) {
                  return;
               }
            }

            if (this.I0 == ug.w.v && this.z >= 0 && this.O < 0 && this.S(var1, this.z) < 0) {
               this.x();
            }

            if (P(var1.gui.screen()) && var1.player.containerMenu instanceof DispenserMenu) {
               this.scanCurrentContainerTemplateSlots(var1);
            }

            if (!this.g.isEmpty()) {
               if (this.N()) {
                  this.c(var1);
               } else if (this.z >= 0 && this.I0 == ug.w.v && this.O < 0 && this.b <= 0) {
                  this.x(var1);
               }
            }
         }
      } else {
         this.abortSwap();
         this.clearQueue();
         this.IT = null;
         this.X = -1L;
      }
   }

   private void x(Minecraft var1) {
      int var2 = this.z;
      if (var2 >= 0 && !this.g.isEmpty()) {
         int var3 = this.G(new MapId(var2));
         if (var3 < 1) {
            this.x();
         } else {
            int var4 = this.g.peekFirst();
            hh.L("SWAP " + this.r() + " deal real=" + this.R(var3) + " (visual) map=" + var2 + " pending=" + this.R(var4));
            if (var3 == var4) {
               this.W(var3, var3);
               this.x();
               this.X();
            } else if (!this.swapWorthwhile(var3, var4)) {
               this.W(var3, var3);
               this.x();
               this.X();
            } else if (this.b <= 0 && !P(var1.gui.screen())) {
               this.D();
               if (!this.U(var1, var2, var3) && this.S(var1, var2) >= 0) {
                  this.O = var2;
                  this.r = var3;
                  this.d = var4;
                  this.x();
               }
            }
         }
      } else {
         this.x();
      }
   }

   public void setTemplateForValue(int var1, MapId var2) {
      if (var1 >= 1 && var1 <= 10) {
         if (var2 == null) {
            this.U.remove(var1);
            this.u.remove(var1);
         } else {
            this.U.put(var1, var2);
            this.u.put(var1, var2.id());
         }
      }
   }

   public MapId getTemplateForValue(int var1) {
      mm var2 = mm.W();
      if (var2.I() && var2.S()) {
         int var3 = var2.c(var1);
         return var3 < 0 ? null : new MapId(var3);
      } else {
         return var1 >= 1 && var1 <= 10 ? this.U.get(var1) : null;
      }
   }

   public int getTemplateIdForValue(int var1) {
      mm var2 = mm.W();
      return var2.I() && var2.S() ? var2.c(var1) : this.u.getOrDefault(var1, -1);
   }

   public void setTemplateIdForValue(int var1, int var2) {
      if (var1 >= 1 && var1 <= 10) {
         this.setTemplateForValue(var1, var2 < 0 ? null : new MapId(var2));
      }
   }

   public int countTemplates() {
      return this.U.size();
   }

   public boolean O(MapId var1) {
      return this.G(var1) >= 1;
   }

   public void setContainerContext(String var1) {
      if (!Objects.equals(this.I, var1) && this.e != null) {
         this.i(Minecraft.getInstance(), "dispenser context changed");
      }

      this.I = var1;
      this.D.clear();
      this.x();
   }

   public String A() {
      return this.I;
   }

   public int r(int var1) {
      return this.T().getOrDefault(var1, -1);
   }

   public boolean scanCurrentContainerTemplateSlots(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.player.containerMenu instanceof DispenserMenu var2) {
         Map var11 = this.T();
         Map var4 = this.v();
         var11.clear();
         var4.clear();
         byte var5 = 0;

         for (Slot var7 : var2.slots) {
            if (var7 != null && !(var7.container instanceof Inventory)) {
               ItemStack var8 = var7.getItem();
               if (var8 != null && !var8.isEmpty() && var8.is(Items.FILLED_MAP)) {
                  MapId var9 = (MapId)var8.getComponents().get(DataComponents.MAP_ID);
                  int var10 = var9 == null ? -1 : this.G(var9);
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

   public int n(String var1, ItemStack var2) {
      if (var2 != null) {
         var2 = yg.W(var1, var2);
      }

      return var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP) ? this.G((MapId)var2.getComponents().get(DataComponents.MAP_ID)) : -1;
   }

   public int u(MapId var1) {
      return this.G(var1);
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

   private void W(int var1, int var2) {
      this.f(var1, var2, false);
   }

   private void f(int var1, int var2, boolean var3) {
      hh.B(this.r(), var1, var2, this.getTemplateIdForValue(var2), var3);
      if (this.getClass() == ug.class || this instanceof u3 || this instanceof uu) {
         this.H.merge(this.getTemplateIdForValue(var3 ? var1 : var2), 1, Integer::sum);
      }
   }

   public void f(int var1, int var2) {
      int var3 = this.H.getOrDefault(var2, 0);
      if (var3 > 0) {
         if (var3 == 1) {
            this.H.remove(var2);
         } else {
            this.H.put(var2, var3 - 1);
         }
      } else {
         hh.K(this.r(), hh.e(hh.M(this.r(), var1), Integer.toString(var1), false, false));
      }
   }

   public void T() {
      this.H.clear();
   }

   protected String r() {
      if (this instanceof um) {
         return "Higher/Lower";
      } else if (this instanceof ue) {
         return "Odd/Even";
      } else if (this instanceof u3) {
         return "Baccarat";
      } else {
         return this instanceof uu ? "Mexican Jack" : "Blackjack";
      }
   }

   protected String R(int var1) {
      return switch (var1) {
         case 1 -> "A";
         case 10 -> "10";
         default -> String.valueOf(var1);
      };
   }

   public boolean isSwapActive() {
      return this.I0 != ug.w.v;
   }

   public boolean isSwapJuggling() {
      return (boolean)(switch (this.I0) {
         case H, R, G, m -> 1;
         default -> 0;
      });
   }

   public boolean isSwapFreezing() {
      if (this.Z > 0) {
         return true;
      }

      return (boolean)(switch (this.I0) {
         case R, G, m -> 1;
         default -> 0;
      });
   }

   public boolean isSwapFreezingPassThrough() {
      return this.L > 0;
   }

   public void signalFramePlacement(ItemFrame var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null) {
         ItemStack var3 = var2.player.getMainHandItem();
         if (var3.is(Items.FILLED_MAP) && var1.getItem().isEmpty() && this.I0 != ug.w.v && this.p(var3) == this.getTemplateIdForValue(this.Iv)) {
            this.k = var1.getId();
         }

         this.n();
         this.c();
         this.L = 40;
         o5.m("PLACEMENT frame=" + var1.getId() + " map=" + this.p(var3) + " phase=" + this.I0 + " vanilla animation");
      }
   }

   private boolean D(Minecraft var1, int var2) {
      return this.k >= 0 && var1.level != null && var1.level.getEntity(this.k) instanceof ItemFrame var3 && this.p(var3.getItem()) == var2;
   }

   public boolean l(ItemStack var1) {
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
      return this.isSwapActive() || this.O >= 0;
   }

   public boolean isSwapSuppressing() {
      return this.I0 != ug.w.v ? true : this.IG >= 0 && !this.g.isEmpty();
   }

   public boolean isDealBobAllowed(ItemStack var1) {
      return this.k(var1, true);
   }

   public boolean isHotbarBobAllowed(ItemStack var1) {
      return this.k(var1, false);
   }

   private boolean k(ItemStack var1, boolean var2) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         Minecraft var3 = Minecraft.getInstance();
         if (var3 != null && var3.player != null && var3.player.getOffhandItem() == var1) {
            return false;
         }

         if (this.I0 != ug.w.v) {
            return false;
         }

         int var4 = this.getTemplateIdForValue(this.Iv);
         if (var4 >= 0 && this.p(var1) == var4) {
            return false;
         }

         if (this.g.isEmpty()) {
            return true;
         }

         int var5 = this.g.peekFirst();
         int var6 = this.getTemplateIdForValue(var5);
         if (var6 >= 0 && this.p(var1) == var6) {
            return false;
         }

         if (var2) {
            this.q();
         }

         return true;
      } else {
         return true;
      }
   }

   private void q() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null) {
         if (!this.isDealtHandLocked()) {
            this.B = (boolean)1;
            this.f = var1.player.getInventory().getSelectedSlot();
            this.Y = null;
            this.L = 0;
            this.o++;
         }

         this.Iu = (boolean)0;
         if (var1.player.getMainHandItem().is(Items.FILLED_MAP)) {
            this.getDealtHandVisual(var1.player.getMainHandItem());
         }
      }
   }

   private void n() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.firstPersonHandsAndItems() instanceof en var2) {
         var2.larpRig$releaseHeldMap(this, var1.player.getMainHandItem());
      }
   }

   public boolean u() {
      return this.getClass() == ug.class || this instanceof u3 || this instanceof uu || this instanceof ue;
   }

   private void D() {
      this.q();
      Minecraft var1 = Minecraft.getInstance();
      if (this.u() && var1.player != null && this.isDealtHandLocked() && var1.player.firstPersonHandsAndItems() instanceof en var2) {
         var2.larpRig$captureHeldMap(this, var1.player.getMainHandItem(), var1.player.getInventory().getSelectedSlot());
      }
   }

   private void c() {
      this.B = (boolean)0;
      this.Iu = (boolean)0;
      this.f = -1;
      this.Y = null;
   }

   public boolean isDealtHandLocked() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.B && (var1.player == null || var1.player.getInventory().getSelectedSlot() != this.f)) {
         this.c();
      }

      return this.B;
   }

   public ItemStack getDealtHandVisual(ItemStack var1) {
      if (!this.isDealtHandLocked()) {
         return var1;
      }

      ItemStack var2 = this.getInventorySlotProjection(this.f);
      if (var2 != null) {
         this.Y = var2;
         return var2;
      }

      if (!this.isDealtHandLocked()) {
         return var1;
      }

      if (this.Iu) {
         if (!var1.is(Items.FILLED_MAP) || this.Y == null || this.p(var1) != this.p(this.Y)) {
            this.c();
         }

         return var1;
      } else {
         if (var1.is(Items.FILLED_MAP)) {
            ItemStack var3 = this.getVisualOverride(var1);
            ItemStack var4 = var3 == null ? var1 : var3;
            if (this.Y == null || this.p(this.Y) != this.p(var4)) {
               this.Y = var4.copy();
            }
         } else if (this.I0 == ug.w.v && this.Z == 0 && this.c < 1 && this.O < 0) {
            this.c();
            return var1;
         }

         return this.Y == null ? var1 : this.Y;
      }
   }

   public long getDealtHandSequence() {
      return this.o;
   }

   public boolean isDealtHandSettled() {
      return this.Iu;
   }

   public int getSwapValue() {
      return this.Iv;
   }

   public int getSwapRealMapId() {
      return this.K;
   }

   public int T() {
      return this.O;
   }

   public ItemStack getVisualOverride(ItemStack var1) {
      if (this.getClass() == ug.class || this instanceof u3 || this instanceof uu || this instanceof ue) {
         return null;
      }

      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         Minecraft var2 = Minecraft.getInstance();
         if (var2 != null && var2.player != null && var2.player.getOffhandItem() == var1) {
            return null;
         }

         int var4 = -1;
         int var3;
         if (this.I0 != ug.w.v) {
            var3 = this.getTemplateIdForValue(this.Iv);
            var4 = this.IO;
         } else {
            if (this.c < 1) {
               return null;
            }

            var3 = this.getTemplateIdForValue(this.c);
         }

         if (var3 < 0) {
            return null;
         } else {
            int var5 = this.p(var1);
            if (var5 == var3) {
               return null;
            } else if (this.F >= 0 && var2 != null && var2.player != null && var2.player.getInventory().getItem(this.F) == var1) {
               return this.Q(var1, var3);
            } else if (this.z >= 0 && var5 == this.z) {
               return this.Q(var1, var3);
            } else {
               return var4 >= 1 && this.G(new MapId(var5)) == var4 ? this.Q(var1, var3) : null;
            }
         }
      } else {
         return null;
      }
   }

   private ItemStack Q(ItemStack var1, int var2) {
      if (this.a != null && this.p(this.a) == var2) {
         return this.a;
      }

      ItemStack var3 = var1.copy();
      var3.set(DataComponents.MAP_ID, new MapId(var2));
      this.a = var3;
      return var3;
   }

   public ItemStack getInventorySlotProjection(int var1) {
      ItemStack var2 = yg.P(var1);
      if (var2 != null) {
         return var2;
      }

      Minecraft var3 = Minecraft.getInstance();
      if (var3.player == null) {
         return null;
      }

      if (!this.isSwapActive()) {
         if (var1 == this.F && this.c > 0) {
            ItemStack var4 = var3.player.getInventory().getItem(var1);
            int var5 = this.getTemplateIdForValue(this.c);
            if (this.p(var4) == this.z && var5 >= 0) {
               return this.Q(var4, var5);
            }
         }

         return null;
      } else {
         if (this.isSwapJuggling()
            || this.h >= 0
               && this.p(var3.player.getInventory().getItem(this.h)) == this.K
               && var3.player.getInventory().getItem(this.h).getCount() >= this.I6[this.h]
               && (
                  this.s < 0
                     || this.s == this.h
                     || this.p(var3.player.getInventory().getItem(this.s)) == this.Ih[this.s]
                        && var3.player.getInventory().getItem(this.s).getCount() == this.I6[this.s]
               )) {
            return this.v.get(var1);
         }

         this.v = Map.of();
         this.c();
         return null;
      }
   }

   public ItemStack projectSlot(int var1, ItemStack var2) {
      ItemStack var3 = this.getInventorySlotProjection(var1);
      return var3 == null ? var2 : var3;
   }

   public void m(int var1) {
      if (this.u() && this.isDealtHandLocked() && var1 == this.f) {
         this.c();
      }

      if (this.v.containsKey(var1) && this.isSwapActive()) {
         this.i(Minecraft.getInstance(), "projected card manually dropped; no replay");
      }
   }

   public boolean shouldSuppressOpen(MenuType<?> var1) {
      if (this.c()) {
         this.e = null;
         this.IG = -1;
         return false;
      } else {
         return var1 != null && var1 == MenuType.GENERIC_3x3 && this.isSwapSuppressing();
      }
   }

   public void onSwapContainerOpened(MenuType<?> var1, int var2) {
      if (var2 >= 0) {
         if (var1 == null || var1 == MenuType.GENERIC_3x3) {
            if (this.isSwapSuppressing()) {
               if (this.e != null && this.e.h) {
                  this.i(Minecraft.getInstance(), "container replaced; no replay");
               } else {
                  Minecraft var3 = Minecraft.getInstance();
                  this.e = var3.player == null ? null : new mw(var3.player.containerMenu);
                  ug.w var4 = this.I0;
                  this.IG = var2;
                  o5.m("OPEN arrived syncId=" + var2 + " phase=" + var4 + " clicksSinceOpen=0");
                  hh.L("SWAP " + this.r() + " container opened syncId=" + var2);
               }
            }
         }
      }
   }

   public void abortSwap() {
      this.c();
      if (this.e != null && this.e.h && this.isSwapActive()) {
         this.i(Minecraft.getInstance(), "cancelled after transfer; no replay");
      } else if (this.I0 == ug.w.v) {
         if (this.IG >= 0) {
            this.w(Minecraft.getInstance());
         }

         this.x();
         this.Z = 0;
      } else if (this.n(Minecraft.getInstance())) {
         this.x(3, "abort");
      } else {
         this.w(Minecraft.getInstance());
         this.I0 = ug.w.v;
         this.x();
         this.Z = 0;
         hh.L("SWAP " + this.r() + " aborted");
      }
   }

   protected boolean supportsPhysicalSwap() {
      return this.getClass() == ug.class;
   }

   protected BlockPos getAssignedDispenserPosition(Minecraft var1) {
      if (this instanceof u3) {
         BlockPos var2 = f9.W().j(this.A());
         return var2 != null ? var2 : f9.W().R(f9.b.L);
      } else {
         return this instanceof um ? f9.W().R(f9.b.B) : f9.W().R(f9.b.E);
      }
   }

   private void c(Minecraft var1) {
      ArrayList var2 = new ArrayList();
      Map var3 = this.n();

      for (Entry var5 : var3.entrySet()) {
         if ((Integer)var5.getValue() > this.D.getOrDefault(var5.getKey(), 0)) {
            var2.add((Integer)var5.getKey());
         }
      }

      if (!var2.isEmpty()) {
         if (this.g.isEmpty()) {
            hh.L("SWAP " + this.r() + " rise ignored (no plan armed) increased=" + var2 + " queue=" + this.pendingLabel());
         } else {
            int var7 = this.g.peekFirst();
            int var8;
            if (var2.size() == 1) {
               var8 = (Integer)var2.get(0);
            } else {
               if (this.z < 0 || !var2.contains(this.z)) {
                  this.N();
                  return;
               }

               var8 = this.z;
            }

            int var6 = this.G(new MapId(var8));
            if (var6 < 1) {
               this.N();
            } else {
               hh.L(
                  "SWAP "
                     + this.r()
                     + " deal real="
                     + this.R(var6)
                     + " (map "
                     + var8
                     + (this.z == var8 ? ", prearmed" : ", iterated")
                     + ") increased="
                     + var2
                     + " pending="
                     + this.R(var7)
               );
               if (var6 == var7) {
                  this.W(var6, var6);
                  this.x();
                  this.X();
               } else if (!this.swapWorthwhile(var6, var7)) {
                  this.W(var6, var6);
                  hh.L("SWAP " + this.r() + " skip (dealer already wins) real=" + this.R(var6) + " armed=" + this.R(var7) + " queue=" + this.pendingLabel());
                  this.x();
                  this.X();
               } else {
                  this.z = var8;
                  this.c = var7;
                  this.D();
                  if (!this.U(var1, var8, var6)) {
                     if (this.S(var1, var8) >= 0) {
                        this.O = var8;
                        this.r = var6;
                        this.d = var7;
                        this.x();
                     } else {
                        this.x();
                        this.X();
                     }
                  }
               }
            }
         }
      }
   }

   protected boolean swapWorthwhile(int var1, int var2) {
      return mz.F().K(var1, var2);
   }

   private boolean U(Minecraft var1, int var2, int var3) {
      if (var1 == null || var1.player == null || var1.level == null) {
         return false;
      }

      if (!this.supportsPhysicalSwap()) {
         return false;
      }

      ug var4 = x();
      if (var4 != this && var4.isSwapActive()) {
         return false;
      }

      if (this.g.isEmpty()) {
         return false;
      }

      int var5 = this.g.peekFirst();
      if (var5 < 1 || var5 > 10 || var5 == var3) {
         return false;
      }

      if (this.getTemplateForValue(var5) == null || var3 < 1) {
         return false;
      }

      if (!this.swapWorthwhile(var3, var5)) {
         return false;
      }

      int var6 = this.Q(var1, var2);
      if (yg.u(var1) && var6 >= 0 && yg.B(this, var6, var5)) {
         this.I(var3, var5);
         return true;
      }

      if (this.b > 0) {
         return false;
      }

      if (P(var1.gui.screen())) {
         return false;
      }

      if (!var1.player.containerMenu.getCarried().isEmpty() || !var1.player.inventoryMenu.getCarried().isEmpty()) {
         return false;
      }

      if (this.S(var1, var2) < 0) {
         return false;
      }

      int var7 = this.Q(var1, var2);
      if (var7 < 0) {
         return false;
      }

      this.h = var7;
      BlockPos var8 = this.getAssignedDispenserPosition(var1);
      if (var8 == null) {
         return false;
      }

      this.ID = var8.immutable();
      this.k = -1;
      this.K = var2;
      this.IO = var3;
      this.Iv = var5;
      this.C = this.n();
      this.C.merge(var2, -1, Integer::sum);
      this.C.merge(this.getTemplateIdForValue(var5), 1, Integer::sum);
      this.Ih = new int[36];
      this.I6 = new int[36];

      for (int var9 = 0; var9 < 36; var9++) {
         ItemStack var10 = var1.player.getInventory().getItem(var9);
         this.Ih[var9] = this.p(var10);
         this.I6[var9] = var10.getCount();
      }

      this.E = (boolean)0;
      DispenserMenu var12 = this.u(var1);
      if (var12 != null && this.e != null && this.e.J == var12) {
         this.e = this.e.L();
      } else {
         this.e = null;
      }

      this.n = new lh();
      this.IG = var12 != null ? var12.containerId : -1;
      o5.m("BEGIN swap real=" + this.R(var3) + " ->" + this.R(var5) + " retainedSyncId=" + this.IG + " cooldown=" + this.b + " phase=" + this.I0);
      this.y = -1;
      this.x = -1;
      this.V = -1;
      this.G = 0;
      this.R = 0;
      this.b = 0;
      this.e();
      this.D();
      this.s = hu.p(var7, this.I6, var3x -> {
         ItemStack var4x = var1.player.getInventory().getItem(var3x);
         return this.p(var4x) == this.getTemplateIdForValue(var5) && var4x.getCount() < var4x.getMaxStackSize();
      });
      ItemStack var13 = var1.player.getInventory().getItem(var7);
      ItemStack var11 = var13.copyWithCount(1);
      var11.set(DataComponents.MAP_ID, this.getTemplateForValue(var5));
      this.v = hu.x(
         var7,
         this.s,
         this.I6[var7],
         this.s >= 0 && this.s != var7 ? this.I6[this.s] : 0,
         var13,
         this.s >= 0 && this.I6[this.s] > 0 && this.s != var7 ? var1.player.getInventory().getItem(this.s) : var11,
         ItemStack::copyWithCount
      );
      this.W(ug.w.C);
      this.M = 0;
      this.J = -20;
      hh.L("SWAP BEGIN " + this.r() + " real=" + this.R(var3) + " ->" + this.R(var5));
      this.B("swap begin");
      this.t(var1);
      return true;
   }

   private void I(int var1, int var2) {
      this.f(var1, var2, true);
      this.e();
      this.x();
      this.n();
      this.c();
      this.X();
      hh.L("VISUAL " + this.r() + " real=" + var1 + " shown=" + var2);
   }

   public void onInventorySynchronized(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.e != null && this.isSwapActive()) {
         this.e.u("receive full sync=" + var1 + " size=" + var2.size() + " cursorCount=" + var3.getCount());
      }

      Minecraft var4 = Minecraft.getInstance();
      if (this.e != null && var4.player != null && var4.player.containerMenu == this.e.J && var1 == this.e.J.containerId) {
         this.e.N(var2, var3);
         if (this.e.I) {
            this.n.d();
         }

         this.R();
      }
   }

   public void onServerSlot(int var1, int var2, ItemStack var3) {
      if (this.e != null && this.isSwapActive()) {
         this.e.u("receive slot sync=" + var1 + " slot=" + var2 + " map=" + this.p(var3) + " count=" + var3.getCount());
      }

      Minecraft var4 = Minecraft.getInstance();
      if (this.e != null && var4.player != null && var4.player.containerMenu == this.e.J) {
         if (var1 == -1 && var2 == -1) {
            this.e.o(-1, var3);
         } else if (var1 == this.e.J.containerId) {
            this.e.o(var2, var3);
         } else if (var1 == 0 && var2 >= 0 && var2 < var4.player.inventoryMenu.slots.size()) {
            Slot var5 = var4.player.inventoryMenu.getSlot(var2);
            if (var5.container == var4.player.getInventory()) {
               this.onPlayerInventorySlot(var5.getContainerSlot(), var3);
            }
         }

         this.R();
      }
   }

   public void onPlayerInventorySlot(int var1, ItemStack var2) {
      if (this.e != null && this.isSwapActive()) {
         this.e.u("receive player slot=" + var1 + " map=" + this.p(var2) + " count=" + var2.getCount());
      }

      Minecraft var3 = Minecraft.getInstance();
      if (this.e != null && var3.player != null && var3.player.containerMenu == this.e.J) {
         for (Slot var5 : this.e.J.slots) {
            if (var5.container == var3.player.getInventory() && var5.getContainerSlot() == var1) {
               this.e.o(var5.index, var2);
               this.R();
               return;
            }
         }
      }
   }

   public int clickRevision(AbstractContainerMenu var1) {
      return this.e != null && var1 == this.e.J && this.e.i ? 32768 : var1.getStateId();
   }

   public void L(BlockPos var1, String var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null && var3.level != null && var1.equals(this.getAssignedDispenserPosition(var3))) {
         o5.m(
            var2
               + " "
               + this.u(var3)
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
      if (this.I0 == ug.w.k) {
         o5.m("OPEN ACK seq=" + var1 + " " + this.n.E());
      }

      this.n.w(var1);
      this.R();
   }

   private void R() {
      Minecraft var1 = Minecraft.getInstance();
      if (!this.Iw && this.e != null && !this.e.f && this.isSwapActive() && var1.player != null) {
         ug.w var2 = this.I0;
         this.Iw = (boolean)1;

         try {
            if (this.I0 == ug.w.k && this.IG >= 0) {
               this.W(ug.w.H);
            }

            if (this.isSwapJuggling()) {
               this.m(var1);
            }
         } finally {
            this.Iw = (boolean)0;
            if (this.I0 != var2) {
               o5.m("PACKET continuation " + this.r() + " " + var2 + " -> " + this.I0 + " syncId=" + this.IG);
            }
         }
      }
   }

   public boolean j() {
      return this.N;
   }

   public boolean blocksUserInteraction() {
      return this.isSwapActive() && (this.e == null || !this.e.f);
   }

   public boolean allowsRecoveryOpen(BlockPos var1) {
      return this.I0 == ug.w.k && this.IG < 0 && this.e == null && var1 != null && var1.equals(this.ID);
   }

   public boolean blocksClose() {
      return this.blocksUserInteraction();
   }

   public boolean C(AbstractContainerMenu var1) {
      return var1 instanceof InventoryMenu && (this.isSwapActive() || this.e != null);
   }

   boolean c() {
      return !this.isSwapActive() && this.e != null && this.e.o();
   }

   public void v(AbstractContainerMenu var1) {
      if (this.C(var1) && this.c()) {
         this.w(Minecraft.getInstance());
      }
   }

   public boolean hidesCursor(AbstractContainerMenu var1) {
      return this.isSwapJuggling() && !this.v.isEmpty() && (this.C(var1) || this.e != null && var1 == this.e.J);
   }

   private String u(Minecraft var1) {
      return "phase="
         + this.I0
         + " distance="
         + this.V(var1)
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

   public void p(int var1, String var2) {
      if (this.e != null && this.isSwapActive() && this.e.J.containerId == var1) {
         this.e.u(var2);
      }
   }

   public void G(int var1) {
      if (this.isSwapActive()) {
         o5.m("SERVER CLOSE " + this.r() + " sync=" + var1 + " " + this.u(Minecraft.getInstance()));
      }
   }

   private void i(Minecraft var1, String var2) {
      o5.m("BATCH RECOVERY " + this.r() + " reason=" + var2 + " " + this.u(var1) + " " + (this.e == null ? "batch=null" : this.e.h()));
      hh.K(this.r(), "Manual recovery required: " + var2 + " [phase=" + this.I0 + "; details: swap-open.log]");
      AbstractContainerMenu var3 = this.e == null ? null : this.e.J;
      this.e = null;
      this.IG = -1;
      this.I0 = ug.w.v;
      this.g.clear();
      this.D.clear();
      this.e();
      this.x();
      this.n();
      this.c();
      if (var1.player != null && var1.player.containerMenu instanceof DispenserMenu var4 && (var4 == var3 || var1.gui.screen() == null)) {
         var1.gui.setScreen(new DispenserScreen(var4, var1.player.getInventory(), Component.literal(this.r() + " recovery: " + var2)));
      }
   }

   private void m(Minecraft var1) {
      mw var2 = this.e;
      if (var2 == null || var1.player.containerMenu != var2.J || P(var1.gui.screen())) {
         this.i(var1, "handler lost; no replay");
      } else if (!var2.I) {
         if (!this.Iw && ++this.W > 100) {
            this.i(var1, "initial authoritative inventory missing");
         }
      } else if (this.n.I() && !this.n.p()) {
         if (!this.Iw && ++this.W > 100) {
            this.i(var1, "open acknowledgement missing");
         }
      } else if (!var2.T(!this.Iw)) {
         if (var2.z != null) {
            this.i(var1, var2.z);
         }
      } else if (!var2.h && yg.u(var1)) {
         if (var2.o() && yg.B(this, this.h, this.Iv)) {
            this.w(var1);
            this.I0 = ug.w.v;
            this.I(this.IO, this.Iv);
         }
      } else {
         DispenserMenu var3 = (DispenserMenu)var2.J;
         if (this.I0 == ug.w.H) {
            this.j = -1;
            this.A = -1;

            for (Slot var5 : var3.slots) {
               if (var5.container == var1.player.getInventory() && var5.getContainerSlot() == this.h && this.p(var5.getItem()) == this.K) {
                  this.j = var5.index;
               }
            }

            if (!var2.H()) {
               this.H(var1, var2, "initial local/server ledger or cursor mismatch", "");
               return;
            }

            if (this.j < 0) {
               if (var2.o()) {
                  this.O(false, "draw moved before transfer");
               } else {
                  this.H(var1, var2, "real arrival unavailable", "");
               }

               return;
            }

            if (var3.getSlot(this.j).getItem().getCount() < this.I6[this.h]) {
               this.H(var1, var2, "original arrival quantity changed before transfer", "");
               return;
            }

            ArrayList var25 = new ArrayList();
            ArrayList var26 = new ArrayList();
            ArrayList var6 = new ArrayList();
            int var7 = this.getTemplateIdForValue(this.Iv);
            int var8 = var2.Q.get(this.j).getCount() == 1 ? 1 : 0;

            for (Slot var10 : var3.slots) {
               int var11 = var10.getContainerSlot();
               if (var10.container == var1.player.getInventory()
                  && var11 >= 0
                  && var11 < 36
                  && var11 != this.h
                  && this.Ih[var11] == var7
                  && this.I6[var11] > 0
                  && this.p(var10.getItem()) == var7
                  && var10.getItem().getCount() == this.I6[var11]) {
                  var26.add(var10.index);
               }
            }

            if (var8 != 0) {
               var26.add(this.j);
            }

            int var27 = var26.size();

            for (Slot var31 : var3.slots) {
               if (!(var31.container instanceof Inventory)) {
                  if (var7 >= 0 && this.p(var31.getItem()) == var7) {
                     var25.add(var31.index);
                  }

                  if (!var31.getItem().isEmpty() && this.p(var31.getItem()) == this.K) {
                     var6.add(var31.index);
                  }
               } else if (var8 == 0
                  && var31.container == var1.player.getInventory()
                  && var31.getContainerSlot() >= 0
                  && var31.getContainerSlot() < 36
                  && this.I6[var31.getContainerSlot()] == 0
                  && var31.getItem().isEmpty()
                  && var2.Q.get(var31.index).isEmpty()) {
                  var26.add(var31.index);
               }
            }

            var26.subList(var27, var26.size()).sort(Comparator.comparingInt(var1x -> var3.getSlot(var1x).getContainerSlot()));

            for (Slot var32 : var3.slots) {
               if (!(var32.container instanceof Inventory) && var32.getItem().isEmpty() && var2.Q.get(var32.index).isEmpty()) {
                  var6.add(var32.index);
               }
            }

            var6.removeIf(var2x -> !this.allowsReturnSlot(var3.getSlot(var2x).getContainerSlot(), this.IO));
            ha.a var30 = ha.p(
               var25,
               var26,
               var6,
               (var3x, var4) -> var2.w(var1, var3x, var4, var4 == this.j, true).batch() != null,
               var3x -> var2.X(var1, this.j, var3x) != null
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
                     mw.q var23 = var2.w(var1, var17, var22, var22 == this.j, true);
                     if (var23.batch() != null) {
                        var18 = var22;
                        break;
                     }

                     var20.merge(var23.reason(), 1, Integer::sum);
                     if (var14 == null) {
                        var14 = var23.reason();
                     }

                     if (var19 == null) {
                        var19 = "destination=" + var22 + " " + var23.reason() + " " + var2.R(var17, var22);
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
                        mw.q var36 = var2.w(var1, this.j, var35.index, false, true);
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
                           .append(var2.R(this.j, var35.index));
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

               this.H(var1, var2, var33, var12.toString());
               return;
            }

            this.y = var30.source();
            this.A = var30.destination();
            this.V = var30.returnSlot();
            this.x = var3.getSlot(this.A).getContainerSlot();
            this.s = this.x;
            this.v = hu.x(
               this.h,
               this.s,
               this.I6[this.h],
               this.s == this.h ? 0 : this.I6[this.s],
               var2.Q.get(this.j),
               this.s != this.h && !var2.Q.get(this.A).isEmpty() ? var2.Q.get(this.A) : var2.Q.get(this.y),
               ItemStack::copyWithCount
            );
            this.W(ug.w.R);
            var2.B(var1, this.j, this.V, this.r() + " RETURN_REAL", true);
         } else if (this.I0 == ug.w.R) {
            this.W(ug.w.G);
            var2.B(var1, this.y, this.A, this.r() + " DELIVER_ARMED", true);
         } else {
            if (!var2.H()) {
               this.i(var1, "final quantity ledger changed");
               return;
            }

            this.e(true, "two one-card batches confirmed");
         }

         if (var2.z != null) {
            this.i(var1, var2.z);
         }
      }
   }

   private void H(Minecraft var1, mw var2, String var3, String var4) {
      StringBuilder var5 = new StringBuilder(
         "SWAP "
            + this.r()
            + " PREFLIGHT REJECT sync="
            + var2.J.containerId
            + " revision="
            + var2.J.getStateId()
            + " realMap="
            + this.K
            + " armedMap="
            + this.getTemplateIdForValue(this.Iv)
            + " arrival="
            + this.j
            + " arrivalInv="
            + this.h
            + " source="
            + this.y
            + " destination="
            + this.A
            + " cursorSafe="
            + var2.o()
            + " "
            + var2.F(-1)
            + " reason="
            + var3
            + "\n"
            + var4
      );

      for (Slot var7 : var2.J.slots) {
         var5.append("\n").append(var2.F(var7.index));
         if (var7.container == var1.player.getInventory() && var7.getContainerSlot() >= 0 && var7.getContainerSlot() < 36) {
            var5.append(" inv=")
               .append(var7.getContainerSlot())
               .append(" drawBaselineMap=")
               .append(this.Ih[var7.getContainerSlot()])
               .append(" drawBaselineCount=")
               .append(this.I6[var7.getContainerSlot()]);
         }
      }

      hh.L(var5.toString());
      this.i(var1, var3);
   }

   private void t(Minecraft var1) {
      if (this.I0 != ug.w.H && this.I0 != ug.w.R && this.I0 != ug.w.G && this.I0 != ug.w.m) {
         this.M++;
         if (this.M > 1200) {
            this.O(false, "whole-swap timeout");
         } else {
            switch (this.I0) {
               case C:
                  this.W++;
                  if (this.IG >= 0) {
                     if (this.u(var1) != null) {
                        o5.m("REQUEST retained harness live syncId=" + this.IG + " tick=" + this.W);
                        this.W(ug.w.H);
                        this.m(var1);
                     } else if (var1.player != null && var1.player.containerMenu instanceof DispenserMenu var2) {
                        this.IG = var2.containerId;
                        o5.m("REQUEST adopting live open syncId=" + var2.containerId + " tick=" + this.W + " screenOpen=" + (var1.gui.screen() != null));
                        hh.L("SWAP " + this.r() + " retained harness vanished - adopting live open syncId=" + var2.containerId);
                        this.W(ug.w.k);
                     } else if (!P(var1.gui.screen())) {
                        this.IG = -1;
                        o5.m("REQUEST retained harness gone - reopening tick=" + this.W);
                        hh.L("SWAP " + this.r() + " retained harness vanished - reopening");
                        this.g(var1);
                     } else {
                        o5.m(
                           "REQUEST retained gone but screen open tick="
                              + this.W
                              + " screen="
                              + (var1.gui.screen() == null ? "null" : var1.gui.screen().getClass().getSimpleName())
                              + " handler="
                              + (var1.player.containerMenu == null ? "null" : var1.player.containerMenu.getClass().getSimpleName())
                        );
                     }
                  } else if (this.W >= 1) {
                     o5.m("REQUEST fresh - compact requestContainerOpen tick=" + this.W);
                     this.g(var1);
                  }
                  break;
               case k:
                  this.W++;
                  if (this.IG >= 0) {
                     o5.m("WAIT_OPEN open arrived tick=" + this.W + " syncId=" + this.IG);
                     hh.L("SWAP " + this.r() + " open-arrived tick=" + this.W + " syncId=" + this.IG);
                     this.W(ug.w.H);
                     this.m(var1);
                  } else if (this.W >= 100 && this.e == null) {
                     this.O(false, "dispenser did not open; no cards moved");
                  } else if (this.W >= 660) {
                     o5.m(
                        "WAIT_OPEN open timeout tick="
                           + this.W
                           + " screen="
                           + (var1.gui.screen() == null ? "null" : var1.gui.screen().getClass().getSimpleName())
                     );
                     this.l("open timeout");
                  } else if (this.n.u(this.M)) {
                     this.R(var1);
                  }
                  break;
               default:
                  this.I0 = ug.w.v;
            }
         }
      } else {
         this.m(var1);
      }
   }

   private void g(Minecraft var1) {
      if (this.ID == null) {
         this.O(false, "dispenser unknown");
      } else {
         this.R(var1);
         this.W(ug.w.k);
      }
   }

   private void R(Minecraft var1) {
      if (this.n.u(this.M) && this.M - this.J >= 4) {
         if (this.ID != null && var1 != null && var1.player != null && var1.gameMode != null && var1.getConnection() != null && var1.level != null) {
            BlockHitResult var2 = this.U(var1);
            if (var2 == null) {
               o5.m("open blocked: no visible dispenser face pos=" + this.ID);
            } else {
               boolean var3 = var1.player.isShiftKeyDown();
               double var4 = this.V(var1);
               o5.m(
                  "open-issue phase="
                     + this.I0
                     + " tick="
                     + this.W
                     + " sneaking="
                     + var3
                     + " dist="
                     + String.format("%.1f", var4)
                     + " syncId="
                     + this.IG
                     + " pass="
                     + this.R
               );
               hh.L(
                  "SWAP "
                     + this.r()
                     + " open-issue phase="
                     + this.I0
                     + " tick="
                     + this.W
                     + " sneaking="
                     + var3
                     + " dist="
                     + String.format("%.1f", var4)
                     + " syncId="
                     + this.IG
               );
               if (!var1.player.isShiftKeyDown() && !var1.options.keyUse.isDown()) {
                  this.j(var1, var2, var1x -> {
                     this.J = this.M;
                     this.n.s(var1x, this.M);
                  });
               }
            }
         }
      }
   }

   protected void j(Minecraft var1, BlockHitResult var2, IntConsumer var3) {
      _s.q(
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
               this.N = (boolean)1;

               try {
                  var1.gameMode.useItemOn(var1.player, InteractionHand.MAIN_HAND, var2);
                  var3.accept(((q75e5da671f8dbf02)var1.level).LarpRig$getPendingUpdateManager().currentSequence());
                  return true;
               } finally {
                  this.N = (boolean)0;
               }
            } else {
               return false;
            }
         }
      );
   }

   static int i(int var0, int var1) {
      return var0 >= 0 && var0 < 9 ? var0 : var1;
   }

   private BlockHitResult U(Minecraft var1) {
      return b(var1, this.ID);
   }

   protected static BlockHitResult b(Minecraft var0, BlockPos var1) {
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

   private void l(String var1) {
      if (this.R < 6) {
         this.R++;
         o5.m("retry (" + var1 + ") pass=" + this.R + " phase=" + this.I0 + " tick=" + this.W);
         hh.L("SWAP " + this.r() + " retry (" + var1 + ")");
         this.x(1, "retry " + var1);
      } else {
         o5.m("CRITICAL fail (" + var1 + ") after pass=" + this.R);
         this.O(false, var1);
      }
   }

   private void w(Minecraft var1) {
      this.e = null;
      if (var1 != null && var1.player != null) {
         if (this.IG >= 0 && var1.player.containerMenu != null && var1.player.containerMenu.containerId == this.IG) {
            o5.m("close suppressed container syncId=" + this.IG + " phase=" + this.I0);
            if (!(var1.gui.screen() instanceof InventoryScreen) && !(var1.gui.screen() instanceof ChatScreen)) {
               var1.player.closeContainer();
            } else {
               var1.getConnection().send(new ServerboundContainerClosePacket(this.IG));
            }
         }

         var1.player.containerMenu = var1.player.inventoryMenu;
         this.IG = -1;
      }
   }

   private void O(boolean var1, String var2) {
      if (!var1) {
         hh.K(this.r(), "Swap failed: " + var2);
      }

      hh.L("SWAP " + this.r() + " " + (var1 ? "OK" : "FAIL") + " (" + var2 + ") real=" + this.R(this.IO) + " ->" + this.R(this.Iv));
      if (!var1 && this.n(Minecraft.getInstance())) {
         this.x(2, "cleanup " + var2);
      } else {
         this.e(var1, var2);
      }
   }

   private void e(boolean var1, String var2) {
      Minecraft var3 = Minecraft.getInstance();
      mw var4 = this.e;
      if (var4 != null) {
         o5.m("BATCH TRACE " + this.r() + " " + this.u(var3) + " " + var4.h());
      }

      o5.m("finishSwapDecided ok=" + var1 + " reason=" + var2 + " syncId=" + this.IG + " queue=" + this.pendingLabel() + " phase=" + this.I0);
      this.w(var3);
      this.b = 0;
      this.T = var1 ? 12 : 0;
      if (var1) {
         this.W(this.IO, this.Iv);
      }

      this.X();
      if (var1 && !this.g.isEmpty() && var4 != null) {
         this.D = new HashMap<>(this.C);
         yk.completeDrawBaseline(this.Ih, this.I6, this.h, this.x, this.getTemplateIdForValue(this.Iv));
         System.arraycopy(this.Ih, 0, this.p, 0, 36);
         System.arraycopy(this.I6, 0, this.l, 0, 36);
      }

      this.x();
      this.I0 = ug.w.v;
      this.E = (boolean)0;
      if (var1) {
         ItemStack var5 = var3.player.getMainHandItem();
         if (!this.isDealtHandLocked()
            || !var5.is(Items.FILLED_MAP)
            || this.p(var5) < 0
            || (this.Y == null || this.p(var5) != this.p(this.Y)) && this.p(var5) != this.getTemplateIdForValue(this.Iv)) {
            this.n();
            this.c();
         } else {
            this.Y = var5.copy();
            this.Iu = (boolean)1;
         }
      }

      if (!var1) {
         this.n();
         this.c();
      }

      this.Z = var1 ? 16 : 0;
      this.B("swap end " + (var1 ? "ok" : "fail") + " " + var2);
      if (this.O >= 0 && !this.g.isEmpty() && this.g.peekFirst() == this.d) {
         this.z = this.O;
         this.c = this.d;
         this.L();
      } else {
         this.e();
         this.x();
      }
   }

   private void x(int var1, String var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.n(var3)) {
         this.w(var3);
         this.B(var3, var1, var2);
      } else {
         this.i(var3, var2 + ": occupied cursor before batch");
      }
   }

   private void B(Minecraft var1, int var2, String var3) {
      switch (var2) {
         case 1:
            this.G = 0;
            this.IG = -1;
            this.W = 1;
            this.I0 = ug.w.C;
            break;
         case 2:
            this.e(false, var3);
            break;
         default:
            this.w(var1);
            this.I0 = ug.w.v;
            this.x();
            hh.L("SWAP " + this.r() + " aborted");
      }
   }

   private boolean n(Minecraft var1) {
      DispenserMenu var2 = this.u(var1);
      return var2 != null && !var2.getCarried().isEmpty();
   }

   private void X() {
      if (!this.g.isEmpty()) {
         hh.L("PLAYED " + this.r() + " next=" + this.g.peekFirst());
         this.g.pollFirst();
         if (this.g.isEmpty()) {
            this.D.clear();
            if (this.I0 == ug.w.v && this.IG >= 0) {
               if (this.e != null && this.e.o()) {
                  this.w(Minecraft.getInstance());
               } else {
                  this.i(Minecraft.getInstance(), "queue ended without a safe cursor snapshot");
               }
            }
         } else {
            this.N();
         }
      }
   }

   private void W(ug.w var1) {
      this.I0 = var1;
      if (this.e != null) {
         this.e.u(this.u(Minecraft.getInstance()));
      }

      this.W = 0;
   }

   private void e() {
      this.O = -1;
      this.r = 0;
      this.d = 0;
   }

   private void x() {
      this.v = Map.of();
      this.s = -1;
      this.z = -1;
      this.c = -1;
      this.F = -1;
      this.a = null;
   }

   public int getSwapSyncId() {
      return this.IG;
   }

   private void L() {
      this.F = -1;
      if (this.z >= 0) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null) {
            this.F = this.Ib >= 0 && this.Ib < 36 && this.p(var1.player.getInventory().getItem(this.Ib)) == this.z ? this.Ib : this.S(var1, this.z);
         }
      }
   }

   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      if (var3 >= 0) {
         int var4 = this.G(new MapId(var3));
         if (var4 >= 1) {
            Minecraft var5 = Minecraft.getInstance();
            Object var6 = var1 == 0 ? var5.player.inventoryMenu : var5.player.containerMenu;
            if (var2 >= 0 && var2 < ((AbstractContainerMenu)var6).slots.size()) {
               Slot var7 = var6.getSlot(var2);
               if (var7.container == var5.player.getInventory() && var7.getContainerSlot() < 36) {
                  this.Ib = var7.getContainerSlot();
                  if (this.g.isEmpty() || this.I0 != ug.w.v || this.Q(var5, var3) >= 0) {
                     if (this.g.isEmpty()) {
                        this.z = var3;
                        this.L();
                        hh.L("SWAP " + this.r() + " slot-update sync=" + var1 + " slot=" + var2 + " real=" + this.R(var4) + " (no plan armed)");
                     } else if (this.I0 == ug.w.v) {
                        int var8 = this.g.peekFirst();
                        if (var4 == var8) {
                           hh.L("SWAP " + this.r() + " slot-update real=" + this.R(var4) + " == pending (no swap needed)");
                        } else {
                           this.z = var3;
                           this.c = var8;
                           this.L();
                           this.D();
                           if (!this.swapWorthwhile(var4, var8)) {
                              this.W(var4, var4);
                              hh.L(
                                 "SWAP "
                                    + this.r()
                                    + " skip (dealer already wins) real="
                                    + this.R(var4)
                                    + " armed="
                                    + this.R(var8)
                                    + " queue="
                                    + this.pendingLabel()
                              );
                              this.x();
                              this.X();
                           } else {
                              if (!this.U(Minecraft.getInstance(), var3, var4) && this.S(Minecraft.getInstance(), var3) >= 0) {
                                 this.O = var3;
                                 this.r = var4;
                                 this.d = var8;
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

   static boolean P(Screen var0) {
      return var0 != null && !(var0 instanceof InventoryScreen) && !(var0 instanceof ChatScreen);
   }

   private DispenserMenu u(Minecraft var1) {
      if (var1 != null && var1.player != null && !P(var1.gui.screen())) {
         return var1.player.containerMenu instanceof DispenserMenu var2 && var2.containerId == this.IG ? var2 : null;
      } else {
         return null;
      }
   }

   protected boolean allowsReturnSlot(int var1, int var2) {
      return true;
   }

   private int d(DispenserMenu var1, int var2) {
      if (var2 < 0) {
         return -1;
      }

      for (Slot var4 : var1.slots) {
         if (var4 != null && !(var4.container instanceof Inventory)) {
            ItemStack var5 = var4.getItem();
            if (var5 != null && var5.is(Items.FILLED_MAP) && this.p(var5) == var2) {
               return var4.index;
            }
         }
      }

      return -1;
   }

   private int F(DispenserMenu var1, ItemStack var2) {
      int var3 = this.p(var2);
      int var4 = this.G((MapId)var2.getComponents().get(DataComponents.MAP_ID));
      int var5 = -1;
      if (var4 >= 1 && var3 >= 0) {
         var5 = this.d(var1, var3);
         if (var5 < 0) {
            var5 = this.containerIndexForValue(var4);
         }
      } else {
         var5 = this.y;
      }

      if (var5 >= 0) {
         Slot var6 = var1.getSlot(var5);
         if (var6 != null) {
            ItemStack var7 = var6.getItem();
            if (var7 != null && !var7.isEmpty() && this.p(var7) != var3) {
               var5 = -1;
            }
         }
      }

      if (var5 < 0) {
         var5 = this.Q(var1);
      }

      return var5;
   }

   private int Q(DispenserMenu var1) {
      for (Slot var3 : var1.slots) {
         if (var3 != null && !(var3.container instanceof Inventory) && var3.getItem().isEmpty()) {
            return var3.index;
         }
      }

      return -1;
   }

   private int b(DispenserMenu var1, int var2) {
      int var3 = this.getTemplateIdForValue(var2);
      return var3 < 0 ? -1 : this.d(var1, var3);
   }

   private int S(Minecraft var1, int var2) {
      if (var1 != null && var1.player != null) {
         Inventory var3 = var1.player.getInventory();

         for (int var4 = 0; var4 < var3.getContainerSize(); var4++) {
            ItemStack var5 = var3.getItem(var4);
            if (var5 != null && var5.is(Items.FILLED_MAP) && this.p(var5) == var2) {
               return var4;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private int e(DispenserMenu var1, int var2) {
      if (var2 < 0) {
         return -1;
      }

      for (Slot var4 : var1.slots) {
         if (var4 != null && var4.container instanceof Inventory) {
            ItemStack var5 = var4.getItem();
            if (var5 != null && var5.is(Items.FILLED_MAP) && this.p(var5) == var2) {
               return var4.index;
            }
         }
      }

      return -1;
   }

   private boolean r(Minecraft var1, int var2) {
      return this.S(var1, var2) >= 0;
   }

   private int d(int var1) {
      return var1 >= 0 && var1 < 9 ? var1 + 36 : var1;
   }

   private boolean T(DispenserMenu var1, int var2) {
      return this.h(var1, this.getTemplateIdForValue(var2));
   }

   private boolean h(DispenserMenu var1, int var2) {
      ItemStack var3 = var1.getCarried();
      return var3 != null && var3.is(Items.FILLED_MAP) && this.p(var3) == var2;
   }

   private boolean F(DispenserMenu var1, int var2, int var3) {
      return this.t(var1, var2, this.getTemplateIdForValue(var3));
   }

   private boolean t(DispenserMenu var1, int var2, int var3) {
      if (var2 >= 0 && var2 < var1.slots.size()) {
         Slot var4 = var1.getSlot(var2);
         if (var4 == null) {
            return false;
         }

         ItemStack var5 = var4.getItem();
         return var5 != null && var5.is(Items.FILLED_MAP) && this.p(var5) == var3;
      } else {
         return false;
      }
   }

   private double V(Minecraft var1) {
      if (var1 != null && var1.player != null && this.ID != null) {
         AABB var2 = new AABB(this.ID);
         return Math.sqrt(var2.distanceToSqr(var1.player.getEyePosition()));
      } else {
         return -1.0;
      }
   }

   private void B(String var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         StringBuilder var3 = new StringBuilder();
         int var4 = var2.player.getInventory().getSelectedSlot();

         for (int var5 = 0; var5 < var2.player.getInventory().getContainerSize(); var5++) {
            ItemStack var6 = var2.player.getInventory().getItem(var5);
            if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
               MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
               int var8 = var7 == null ? -1 : this.G(var7);
               if (var8 >= 1) {
                  if (!var3.isEmpty()) {
                     var3.append(", ");
                  }

                  var3.append("v").append(this.R(var8)).append("x").append(var6.getCount()).append("@").append(var5).append(var5 == var4 ? "(sel)" : "");
               }
            }
         }

         hh.L("DUMP " + this.r() + " " + var1 + " selected=" + var4 + " cards=[" + var3 + "]");
      }
   }

   private int p(ItemStack var1) {
      MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
      return var2 == null ? -1 : var2.id();
   }

   private int G(MapId var1) {
      if (var1 == null) {
         return -1;
      }

      mm var2 = mm.W();
      if (var2.I() && var2.S()) {
         return var2.d(var1.id());
      }

      for (Entry var4 : this.U.entrySet()) {
         if (var4.getValue() != null && ((MapId)var4.getValue()).id() == var1.id()) {
            return (Integer)var4.getKey();
         }
      }

      return -1;
   }

   private Map<Integer, Integer> T() {
      return this.i.computeIfAbsent(this.v(), var0 -> new HashMap<>());
   }

   private Map<Integer, Integer> v() {
      return this.S.computeIfAbsent(this.v(), var0 -> new HashMap<>());
   }

   private String v() {
      return this.I == null ? "" : this.I;
   }

   private boolean N() {
      Map var1 = this.n();

      for (Entry var3 : var1.entrySet()) {
         if ((Integer)var3.getValue() > this.D.getOrDefault(var3.getKey(), 0)) {
            return true;
         }
      }

      return false;
   }

   private Map<Integer, Integer> n() {
      HashMap var1 = new HashMap();
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         for (int var3 = 0; var3 < var2.player.getInventory().getContainerSize(); var3++) {
            this.i(var1, var2.player.getInventory().getItem(var3));
         }
      }

      if (var2 != null && var2.level != null) {
         for (Entity var4 : var2.level.entitiesForRendering()) {
            if (var4 instanceof ItemFrame var5) {
               this.i(var1, var5.getItem());
            }
         }
      }

      return var1;
   }

   private void i(Map<Integer, Integer> var1, ItemStack var2) {
      if (var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP)) {
         MapId var3 = (MapId)var2.getComponents().get(DataComponents.MAP_ID);
         if (var3 != null) {
            var1.merge(var3.id(), var2.getCount(), Integer::sum);
         }
      }
   }

   public enum w {
      v,
      C,
      k,
      H,
      R,
      G,
      m;
   }
}
