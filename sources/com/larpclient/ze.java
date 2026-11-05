package com.larpclient;

import com.larp.obf.qbac0b262827a15ac.q03e2e72dbe55651a.q4be5233a4c9f3d34;
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
public class ze {
   private static final ze t = new ze();
   private static final String m = "";
   private static final int iI = 660;
   private static final int[] j = new int[]{3, 6, 10, 15, 22, 30, 40, 52, 66, 82, 100, 120, 150, 180};
   private static final int w = 100;
   private static final int i = 1200;
   private static final int G = 6;
   private static final int Q = 16;
   private static final int r = 40;
   private final Deque<Integer> v = new ArrayDeque<>();
   private final Map<Integer, MapId> J = new HashMap<>();
   private final Map<Integer, Integer> h = new HashMap<>();
   private final Map<String, Map<Integer, Integer>> K = new HashMap<>();
   private final Map<String, Map<Integer, Integer>> e = new HashMap<>();
   private Map<Integer, Integer> ig = new HashMap<>();
   private final int[] T = new int[36];
   private final int[] iw = new int[36];
   private int is = -1;
   private int O = -1;
   private Map<Integer, Integer> b;
   private int[] g;
   private int[] W;
   private boolean P;
   private String ik;
   private ze.x d = ze.x.A;
   private int E = -1;
   private int C = -1;
   private int B = -1;
   private int N = -1;
   private int y = -1;
   private int H = -1;
   private int p;
   private int R;
   private int A;
   private int Z = -1;
   private int X;
   private int k;
   private int n;
   private int q = -1;
   private long f;
   private boolean i6;
   private boolean iX;
   private int S = -1;
   private ItemStack iz;
   private int l;
   private int I = -1;
   private int L;
   private int a;
   private int Y;
   private int V = -1;
   private int D = -1;
   private int il = -1;
   private ItemStack ip;
   private BlockPos iV;
   private ul o;
   private _n u = new _n();
   private int z = -1;
   private int c = -1;
   private Map<Integer, ItemStack> x = Map.of();
   private int M = -1;
   private boolean F;
   private ClientLevel U;
   private long s = -1L;
   private final Map<Integer, Integer> in = new HashMap<>();
   private int ii = -20;
   private boolean i3;

   protected ze() {
   }

   public static ze V() {
      return t;
   }

   public static boolean n() {
      return c().isSwapActive();
   }

   public static ze c() {
      ze var0 = V();
      z8 var1 = z8.l();
      z6 var2 = z6.u();
      zu var3 = zu.v();
      zw var4 = zw.K();
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
      } else if (z8.l().k()) {
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
      } else if (var0.E()) {
         return var0;
      } else if (var1.E()) {
         return var1;
      } else if (var2.E()) {
         return var2;
      } else if (var3.E()) {
         return var3;
      } else {
         return var4.E() ? var4 : var0;
      }
   }

   private boolean E() {
      return this.d != ze.x.A || this.V >= 0 || this.D >= 1 || this.I >= 0;
   }

   public static ze n() {
      ze var0 = c();
      if (var0.isSwapActive()) {
         return var0;
      }

      if (t.o != null) {
         return t;
      }

      z6 var1 = z6.u();
      return var1.o != null ? var1 : z8.l();
   }

   private boolean D() {
      return this.d == ze.x.A && this.V < 0 && this.D < 1 && !this.v.isEmpty();
   }

   public void queueForce(int var1) {
      this.queueForceSequence(var1);
   }

   public void queueForceSequence(int... var1) {
      if (this.isSwapBusy()) {
         if (!this.P) {
            xa.i("SWAP " + this.l() + " queue replacement rejected while busy");
            this.P = (boolean)1;
         }
      } else {
         this.P = (boolean)0;
         this.v.clear();
         this.x();
         if (var1 != null) {
            int var2 = Math.max(1, this.maxQueuedForces());

            for (int var6 : var1) {
               if (this.v.size() >= var2) {
                  break;
               }

               if (var6 >= 1 && var6 <= 10 && this.getTemplateForValue(var6) != null) {
                  this.v.addLast(var6);
               }
            }
         }

         if (this.v.isEmpty()) {
            this.ig.clear();
         } else {
            this.N();
         }
      }
   }

   private void N() {
      this.ig = this.U();
      Minecraft var1 = Minecraft.getInstance();

      for (int var2 = 0; var2 < 36; var2++) {
         ItemStack var3 = var1.player == null ? ItemStack.EMPTY : var1.player.getInventory().getItem(var2);
         this.T[var2] = this.y(var3);
         this.iw[var2] = var3.getCount();
      }

      this.is = -1;
   }

   private int P(Minecraft var1, int var2) {
      if (this.U().getOrDefault(var2, 0) <= this.ig.getOrDefault(var2, 0)) {
         return -1;
      }

      int[] var3 = new int[36];
      int[] var4 = new int[36];

      for (int var5 = 0; var5 < 36; var5++) {
         ItemStack var6 = var1.player.getInventory().getItem(var5);
         var3[var5] = this.y(var6);
         var4[var5] = var6.getCount();
      }

      return com.larpclient.m.dealtSlot(var2, this.T, this.iw, var3, var4, this.is);
   }

   public void clearQueue() {
      this.abortSwap();
      this.v.clear();
      this.ig.clear();
      this.P = (boolean)0;
   }

   public int getPendingValue() {
      Integer var1 = this.v.peekFirst();
      return var1 == null ? -1 : var1;
   }

   public int getQueuedValueAt(int var1) {
      if (var1 < 0) {
         return -1;
      }

      int var2 = 0;

      for (Integer var4 : this.v) {
         if (var2++ == var1) {
            return var4;
         }
      }

      return -1;
   }

   public boolean hasQueuedForce() {
      return !this.v.isEmpty();
   }

   public String pendingLabel() {
      if (this.v.isEmpty()) {
         return "none";
      }

      StringBuilder var1 = new StringBuilder();

      for (Integer var3 : this.v) {
         if (!var1.isEmpty()) {
            var1.append(" -> ");
         }

         var1.append(this.F(var3));
      }

      return var1.toString();
   }

   public void tick(Minecraft var1) {
      if (var1 != null && var1.player != null) {
         if (var1.level != null) {
            if (this.U != var1.level) {
               this.U = var1.level;
               this.s = var1.level.getGameTime();
            } else if (var1.level.getGameTime() == this.s) {
               return;
            }
         }

         this.s = var1.level == null ? -1L : var1.level.getGameTime();
         if (this.n > 0) {
            this.n--;
         }

         if (this.l > 0) {
            this.l--;
         }

         if (this.X > 0) {
            this.X--;
         }

         if (this.d != ze.x.A) {
            this.S(var1);
         } else {
            if (this.Y > 0 && --this.Y == 0) {
               this.a("post-swap settle");
            }

            if (this.I >= 0) {
               int var2 = this.I;
               int var3 = this.L;
               int var4 = this.a;
               if (this.v.isEmpty() || this.v.peekFirst() != var4) {
                  this.H();
               } else if (this.v(var1, var2) < 0) {
                  this.H();
               } else if (this.X <= 0) {
                  if (var3 == var4) {
                     this.b(var3, var3);
                     xa.i("SWAP " + this.l() + " deferred deal already matched " + this.F(var4));
                     this.H();
                     this.x();
                     this.w();
                     return;
                  }

                  if (!this.swapWorthwhile(var3, var4)) {
                     this.b(var3, var3);
                     xa.i("SWAP " + this.l() + " deferred skip real=" + this.F(var3) + " armed=" + this.F(var4));
                     this.H();
                     this.x();
                     this.w();
                     return;
                  }

                  if (this.J(var1, var2, var3)) {
                     return;
                  }
               }

               if (this.I >= 0) {
                  return;
               }
            }

            if (this.d == ze.x.A && this.V >= 0 && this.I < 0 && this.v(var1, this.V) < 0) {
               this.x();
            }

            if (y(var1.gui.screen()) && var1.player.containerMenu instanceof DispenserMenu) {
               this.scanCurrentContainerTemplateSlots(var1);
            }

            if (!this.v.isEmpty()) {
               if (this.v()) {
                  this.T(var1);
               } else if (this.V >= 0 && this.d == ze.x.A && this.I < 0 && this.X <= 0) {
                  this.Q(var1);
               }
            }
         }
      } else {
         this.abortSwap();
         this.clearQueue();
         this.U = null;
         this.s = -1L;
      }
   }

   private void Q(Minecraft var1) {
      int var2 = this.V;
      if (var2 >= 0 && !this.v.isEmpty()) {
         int var3 = this.R(new MapId(var2));
         if (var3 < 1) {
            this.x();
         } else {
            int var4 = this.v.peekFirst();
            xa.i("SWAP " + this.l() + " deal real=" + this.F(var3) + " (visual) map=" + var2 + " pending=" + this.F(var4));
            if (var3 == var4) {
               this.b(var3, var3);
               this.x();
               this.w();
            } else if (!this.swapWorthwhile(var3, var4)) {
               this.b(var3, var3);
               this.x();
               this.w();
            } else if (this.X <= 0 && !y(var1.gui.screen())) {
               this.s();
               if (!this.J(var1, var2, var3) && this.v(var1, var2) >= 0) {
                  this.I = var2;
                  this.L = var3;
                  this.a = var4;
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
            this.J.remove(var1);
            this.h.remove(var1);
         } else {
            this.J.put(var1, var2);
            this.h.put(var1, var2.id());
         }
      }
   }

   public MapId getTemplateForValue(int var1) {
      kq var2 = kq.I();
      if (var2.Z() && var2.f()) {
         int var3 = var2.T(var1);
         return var3 < 0 ? null : new MapId(var3);
      } else {
         return var1 >= 1 && var1 <= 10 ? this.J.get(var1) : null;
      }
   }

   public int getTemplateIdForValue(int var1) {
      kq var2 = kq.I();
      return var2.Z() && var2.f() ? var2.T(var1) : this.h.getOrDefault(var1, -1);
   }

   public void setTemplateIdForValue(int var1, int var2) {
      if (var1 >= 1 && var1 <= 10) {
         this.setTemplateForValue(var1, var2 < 0 ? null : new MapId(var2));
      }
   }

   public int countTemplates() {
      return this.J.size();
   }

   public boolean P(MapId var1) {
      return this.R(var1) >= 1;
   }

   public void setContainerContext(String var1) {
      if (!Objects.equals(this.ik, var1) && this.o != null) {
         this.g(Minecraft.getInstance(), "dispenser context changed");
      }

      this.ik = var1;
      this.ig.clear();
      this.x();
   }

   public String a() {
      return this.ik;
   }

   public int K(int var1) {
      return this.R().getOrDefault(var1, -1);
   }

   public boolean scanCurrentContainerTemplateSlots(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.player.containerMenu instanceof DispenserMenu var2) {
         Map var11 = this.R();
         Map var4 = this.l();
         var11.clear();
         var4.clear();
         byte var5 = 0;

         for (Slot var7 : var2.slots) {
            if (var7 != null && !(var7.container instanceof Inventory)) {
               ItemStack var8 = var7.getItem();
               if (var8 != null && !var8.isEmpty() && var8.is(Items.FILLED_MAP)) {
                  MapId var9 = (MapId)var8.getComponents().get(DataComponents.MAP_ID);
                  int var10 = var9 == null ? -1 : this.R(var9);
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

   public int J(String var1, ItemStack var2) {
      if (var2 != null) {
         var2 = w5.V(var1, var2);
      }

      return var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP) ? this.R((MapId)var2.getComponents().get(DataComponents.MAP_ID)) : -1;
   }

   public int Y(MapId var1) {
      return this.R(var1);
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

   private void b(int var1, int var2) {
      this.S(var1, var2, false);
   }

   private void S(int var1, int var2, boolean var3) {
      xa.R(this.l(), var1, var2, this.getTemplateIdForValue(var2), var3);
      if (this.getClass() == ze.class || this instanceof z6 || this instanceof zw) {
         this.in.merge(this.getTemplateIdForValue(var3 ? var1 : var2), 1, Integer::sum);
      }
   }

   public void S(int var1, int var2) {
      int var3 = this.in.getOrDefault(var2, 0);
      if (var3 > 0) {
         if (var3 == 1) {
            this.in.remove(var2);
         } else {
            this.in.put(var2, var3 - 1);
         }
      } else {
         xa.q(this.l(), xa.U(xa.y(this.l(), var1), Integer.toString(var1), false, false));
      }
   }

   public void d() {
      this.in.clear();
   }

   protected String l() {
      if (this instanceof z8) {
         return "Higher/Lower";
      } else if (this instanceof zu) {
         return "Odd/Even";
      } else if (this instanceof z6) {
         return "Baccarat";
      } else {
         return this instanceof zw ? "Mexican Jack" : "Blackjack";
      }
   }

   protected String F(int var1) {
      return switch (var1) {
         case 1 -> "A";
         case 10 -> "10";
         default -> String.valueOf(var1);
      };
   }

   public boolean isSwapActive() {
      return this.d != ze.x.A;
   }

   public boolean isSwapJuggling() {
      return (boolean)(switch (this.d) {
         case D, r, Z, I -> 1;
         default -> 0;
      });
   }

   public boolean isSwapFreezing() {
      if (this.l > 0) {
         return true;
      }

      return (boolean)(switch (this.d) {
         case r, Z, I -> 1;
         default -> 0;
      });
   }

   public boolean isSwapFreezingPassThrough() {
      return this.n > 0;
   }

   public void signalFramePlacement(ItemFrame var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.player != null) {
         ItemStack var3 = var2.player.getMainHandItem();
         if (var3.is(Items.FILLED_MAP) && var1.getItem().isEmpty() && this.d != ze.x.A && this.y(var3) == this.getTemplateIdForValue(this.E)) {
            this.q = var1.getId();
         }

         this.B();
         this.v();
         this.n = 40;
         pk.q("PLACEMENT frame=" + var1.getId() + " map=" + this.y(var3) + " phase=" + this.d + " vanilla animation");
      }
   }

   private boolean h(Minecraft var1, int var2) {
      return this.q >= 0 && var1.level != null && var1.level.getEntity(this.q) instanceof ItemFrame var3 && this.y(var3.getItem()) == var2;
   }

   public boolean d(ItemStack var1) {
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
      return this.isSwapActive() || this.I >= 0;
   }

   public boolean isSwapSuppressing() {
      return this.d != ze.x.A ? true : this.H >= 0 && !this.v.isEmpty();
   }

   public boolean isDealBobAllowed(ItemStack var1) {
      return this.Y(var1, true);
   }

   public boolean isHotbarBobAllowed(ItemStack var1) {
      return this.Y(var1, false);
   }

   private boolean Y(ItemStack var1, boolean var2) {
      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         Minecraft var3 = Minecraft.getInstance();
         if (var3 != null && var3.player != null && var3.player.getOffhandItem() == var1) {
            return false;
         }

         if (this.d != ze.x.A) {
            return false;
         }

         int var4 = this.getTemplateIdForValue(this.E);
         if (var4 >= 0 && this.y(var1) == var4) {
            return false;
         }

         if (this.v.isEmpty()) {
            return true;
         }

         int var5 = this.v.peekFirst();
         int var6 = this.getTemplateIdForValue(var5);
         if (var6 >= 0 && this.y(var1) == var6) {
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
            this.i6 = (boolean)1;
            this.S = var1.player.getInventory().getSelectedSlot();
            this.iz = null;
            this.n = 0;
            this.f++;
         }

         this.iX = (boolean)0;
         if (var1.player.getMainHandItem().is(Items.FILLED_MAP)) {
            this.getDealtHandVisual(var1.player.getMainHandItem());
         }
      }
   }

   private void B() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.player.firstPersonHandsAndItems() instanceof ed var2) {
         var2.larpRig$releaseHeldMap(this, var1.player.getMainHandItem());
      }
   }

   public boolean q() {
      return this.getClass() == ze.class || this instanceof z6 || this instanceof zw || this instanceof zu;
   }

   private void s() {
      this.q();
      Minecraft var1 = Minecraft.getInstance();
      if (this.q() && var1.player != null && this.isDealtHandLocked() && var1.player.firstPersonHandsAndItems() instanceof ed var2) {
         var2.larpRig$captureHeldMap(this, var1.player.getMainHandItem(), var1.player.getInventory().getSelectedSlot());
      }
   }

   private void v() {
      this.i6 = (boolean)0;
      this.iX = (boolean)0;
      this.S = -1;
      this.iz = null;
   }

   public boolean isDealtHandLocked() {
      Minecraft var1 = Minecraft.getInstance();
      if (this.i6 && (var1.player == null || var1.player.getInventory().getSelectedSlot() != this.S)) {
         this.v();
      }

      return this.i6;
   }

   public ItemStack getDealtHandVisual(ItemStack var1) {
      if (!this.isDealtHandLocked()) {
         return var1;
      }

      ItemStack var2 = this.getInventorySlotProjection(this.S);
      if (var2 != null) {
         this.iz = var2;
         return var2;
      }

      if (!this.isDealtHandLocked()) {
         return var1;
      }

      if (this.iX) {
         if (!var1.is(Items.FILLED_MAP) || this.iz == null || this.y(var1) != this.y(this.iz)) {
            this.v();
         }

         return var1;
      } else {
         if (var1.is(Items.FILLED_MAP)) {
            ItemStack var3 = this.getVisualOverride(var1);
            ItemStack var4 = var3 == null ? var1 : var3;
            if (this.iz == null || this.y(this.iz) != this.y(var4)) {
               this.iz = var4.copy();
            }
         } else if (this.d == ze.x.A && this.l == 0 && this.D < 1 && this.I < 0) {
            this.v();
            return var1;
         }

         return this.iz == null ? var1 : this.iz;
      }
   }

   public long getDealtHandSequence() {
      return this.f;
   }

   public boolean isDealtHandSettled() {
      return this.iX;
   }

   public int getSwapValue() {
      return this.E;
   }

   public int getSwapRealMapId() {
      return this.B;
   }

   public int q() {
      return this.I;
   }

   public ItemStack getVisualOverride(ItemStack var1) {
      if (this.getClass() == ze.class || this instanceof z6 || this instanceof zw || this instanceof zu) {
         return null;
      }

      if (var1 != null && !var1.isEmpty() && var1.is(Items.FILLED_MAP)) {
         Minecraft var2 = Minecraft.getInstance();
         if (var2 != null && var2.player != null && var2.player.getOffhandItem() == var1) {
            return null;
         }

         int var4 = -1;
         int var3;
         if (this.d != ze.x.A) {
            var3 = this.getTemplateIdForValue(this.E);
            var4 = this.C;
         } else {
            if (this.D < 1) {
               return null;
            }

            var3 = this.getTemplateIdForValue(this.D);
         }

         if (var3 < 0) {
            return null;
         } else {
            int var5 = this.y(var1);
            if (var5 == var3) {
               return null;
            } else if (this.il >= 0 && var2 != null && var2.player != null && var2.player.getInventory().getItem(this.il) == var1) {
               return this.T(var1, var3);
            } else if (this.V >= 0 && var5 == this.V) {
               return this.T(var1, var3);
            } else {
               return var4 >= 1 && this.R(new MapId(var5)) == var4 ? this.T(var1, var3) : null;
            }
         }
      } else {
         return null;
      }
   }

   private ItemStack T(ItemStack var1, int var2) {
      if (this.ip != null && this.y(this.ip) == var2) {
         return this.ip;
      }

      ItemStack var3 = var1.copy();
      var3.set(DataComponents.MAP_ID, new MapId(var2));
      this.ip = var3;
      return var3;
   }

   public ItemStack getInventorySlotProjection(int var1) {
      ItemStack var2 = w5.Q(var1);
      if (var2 != null) {
         return var2;
      }

      Minecraft var3 = Minecraft.getInstance();
      if (var3.player == null) {
         return null;
      }

      if (!this.isSwapActive()) {
         if (var1 == this.il && this.D > 0) {
            ItemStack var4 = var3.player.getInventory().getItem(var1);
            int var5 = this.getTemplateIdForValue(this.D);
            if (this.y(var4) == this.V && var5 >= 0) {
               return this.T(var4, var5);
            }
         }

         return null;
      } else {
         if (this.isSwapJuggling()
            || this.O >= 0
               && this.y(var3.player.getInventory().getItem(this.O)) == this.B
               && var3.player.getInventory().getItem(this.O).getCount() >= this.W[this.O]
               && (
                  this.M < 0
                     || this.M == this.O
                     || this.y(var3.player.getInventory().getItem(this.M)) == this.g[this.M]
                        && var3.player.getInventory().getItem(this.M).getCount() == this.W[this.M]
               )) {
            return this.x.get(var1);
         }

         this.x = Map.of();
         this.v();
         return null;
      }
   }

   public ItemStack projectSlot(int var1, ItemStack var2) {
      ItemStack var3 = this.getInventorySlotProjection(var1);
      return var3 == null ? var2 : var3;
   }

   public void o(int var1) {
      if (this.q() && this.isDealtHandLocked() && var1 == this.S) {
         this.v();
      }

      if (this.x.containsKey(var1) && this.isSwapActive()) {
         this.g(Minecraft.getInstance(), "projected card manually dropped; no replay");
      }
   }

   public boolean shouldSuppressOpen(MenuType<?> var1) {
      if (this.o()) {
         this.o = null;
         this.H = -1;
         return false;
      } else {
         return var1 != null && var1 == MenuType.GENERIC_3x3 && this.isSwapSuppressing();
      }
   }

   public void onSwapContainerOpened(MenuType<?> var1, int var2) {
      if (var2 >= 0) {
         if (var1 == null || var1 == MenuType.GENERIC_3x3) {
            if (this.isSwapSuppressing()) {
               if (this.o != null && this.o.w) {
                  this.g(Minecraft.getInstance(), "container replaced; no replay");
               } else {
                  Minecraft var3 = Minecraft.getInstance();
                  this.o = var3.player == null ? null : new ul(var3.player.containerMenu);
                  ze.x var4 = this.d;
                  this.H = var2;
                  pk.q("OPEN arrived syncId=" + var2 + " phase=" + var4 + " clicksSinceOpen=0");
                  xa.i("SWAP " + this.l() + " container opened syncId=" + var2);
               }
            }
         }
      }
   }

   public void abortSwap() {
      this.v();
      if (this.o != null && this.o.w && this.isSwapActive()) {
         this.g(Minecraft.getInstance(), "cancelled after transfer; no replay");
      } else if (this.d == ze.x.A) {
         if (this.H >= 0) {
            this.v(Minecraft.getInstance());
         }

         this.x();
         this.l = 0;
      } else if (this.m(Minecraft.getInstance())) {
         this.Q(3, "abort");
      } else {
         this.v(Minecraft.getInstance());
         this.d = ze.x.A;
         this.x();
         this.l = 0;
         xa.i("SWAP " + this.l() + " aborted");
      }
   }

   protected boolean supportsPhysicalSwap() {
      return this.getClass() == ze.class;
   }

   protected BlockPos getAssignedDispenserPosition(Minecraft var1) {
      if (this instanceof z6) {
         BlockPos var2 = ep.H().x(this.a());
         return var2 != null ? var2 : ep.H().n(ep.o.m);
      } else {
         return this instanceof z8 ? ep.H().n(ep.o.c) : ep.H().n(ep.o.e);
      }
   }

   private void T(Minecraft var1) {
      ArrayList var2 = new ArrayList();
      Map var3 = this.U();

      for (Entry var5 : var3.entrySet()) {
         if ((Integer)var5.getValue() > this.ig.getOrDefault(var5.getKey(), 0)) {
            var2.add((Integer)var5.getKey());
         }
      }

      if (!var2.isEmpty()) {
         if (this.v.isEmpty()) {
            xa.i("SWAP " + this.l() + " rise ignored (no plan armed) increased=" + var2 + " queue=" + this.pendingLabel());
         } else {
            int var7 = this.v.peekFirst();
            int var8;
            if (var2.size() == 1) {
               var8 = (Integer)var2.get(0);
            } else {
               if (this.V < 0 || !var2.contains(this.V)) {
                  this.N();
                  return;
               }

               var8 = this.V;
            }

            int var6 = this.R(new MapId(var8));
            if (var6 < 1) {
               this.N();
            } else {
               xa.i(
                  "SWAP "
                     + this.l()
                     + " deal real="
                     + this.F(var6)
                     + " (map "
                     + var8
                     + (this.V == var8 ? ", prearmed" : ", iterated")
                     + ") increased="
                     + var2
                     + " pending="
                     + this.F(var7)
               );
               if (var6 == var7) {
                  this.b(var6, var6);
                  this.x();
                  this.w();
               } else if (!this.swapWorthwhile(var6, var7)) {
                  this.b(var6, var6);
                  xa.i("SWAP " + this.l() + " skip (dealer already wins) real=" + this.F(var6) + " armed=" + this.F(var7) + " queue=" + this.pendingLabel());
                  this.x();
                  this.w();
               } else {
                  this.V = var8;
                  this.D = var7;
                  this.s();
                  if (!this.J(var1, var8, var6)) {
                     if (this.v(var1, var8) >= 0) {
                        this.I = var8;
                        this.L = var6;
                        this.a = var7;
                        this.x();
                     } else {
                        this.x();
                        this.w();
                     }
                  }
               }
            }
         }
      }
   }

   protected boolean swapWorthwhile(int var1, int var2) {
      return mj.D().U(var1, var2);
   }

   private boolean J(Minecraft var1, int var2, int var3) {
      if (var1 == null || var1.player == null || var1.level == null) {
         return false;
      }

      if (!this.supportsPhysicalSwap()) {
         return false;
      }

      ze var4 = c();
      if (var4 != this && var4.isSwapActive()) {
         return false;
      }

      if (this.v.isEmpty()) {
         return false;
      }

      int var5 = this.v.peekFirst();
      if (var5 < 1 || var5 > 10 || var5 == var3) {
         return false;
      }

      if (this.getTemplateForValue(var5) == null || var3 < 1) {
         return false;
      }

      if (!this.swapWorthwhile(var3, var5)) {
         return false;
      }

      int var6 = this.P(var1, var2);
      if (w5.h(var1) && var6 >= 0 && w5.J(this, var6, var5)) {
         this.f(var3, var5);
         return true;
      }

      if (this.X > 0) {
         return false;
      }

      if (y(var1.gui.screen())) {
         return false;
      }

      if (!var1.player.containerMenu.getCarried().isEmpty() || !var1.player.inventoryMenu.getCarried().isEmpty()) {
         return false;
      }

      if (this.v(var1, var2) < 0) {
         return false;
      }

      int var7 = this.P(var1, var2);
      if (var7 < 0) {
         return false;
      }

      this.O = var7;
      BlockPos var8 = this.getAssignedDispenserPosition(var1);
      if (var8 == null) {
         return false;
      }

      this.iV = var8.immutable();
      this.q = -1;
      this.B = var2;
      this.C = var3;
      this.E = var5;
      this.b = this.U();
      this.b.merge(var2, -1, Integer::sum);
      this.b.merge(this.getTemplateIdForValue(var5), 1, Integer::sum);
      this.g = new int[36];
      this.W = new int[36];

      for (int var9 = 0; var9 < 36; var9++) {
         ItemStack var10 = var1.player.getInventory().getItem(var9);
         this.g[var9] = this.y(var10);
         this.W[var9] = var10.getCount();
      }

      this.P = (boolean)0;
      DispenserMenu var12 = this.C(var1);
      if (var12 != null && this.o != null && this.o.x == var12) {
         this.o = this.o.I();
      } else {
         this.o = null;
      }

      this.u = new _n();
      this.H = var12 != null ? var12.containerId : -1;
      pk.q("BEGIN swap real=" + this.F(var3) + " ->" + this.F(var5) + " retainedSyncId=" + this.H + " cooldown=" + this.X + " phase=" + this.d);
      this.N = -1;
      this.y = -1;
      this.Z = -1;
      this.k = 0;
      this.A = 0;
      this.X = 0;
      this.H();
      this.s();
      this.M = x_.N(var7, this.W, var3x -> {
         ItemStack var4x = var1.player.getInventory().getItem(var3x);
         return this.y(var4x) == this.getTemplateIdForValue(var5) && var4x.getCount() < var4x.getMaxStackSize();
      });
      ItemStack var13 = var1.player.getInventory().getItem(var7);
      ItemStack var11 = var13.copyWithCount(1);
      var11.set(DataComponents.MAP_ID, this.getTemplateForValue(var5));
      this.x = x_.f(
         var7,
         this.M,
         this.W[var7],
         this.M >= 0 && this.M != var7 ? this.W[this.M] : 0,
         var13,
         this.M >= 0 && this.W[this.M] > 0 && this.M != var7 ? var1.player.getInventory().getItem(this.M) : var11,
         ItemStack::copyWithCount
      );
      this.F(ze.x.J);
      this.R = 0;
      this.ii = -20;
      xa.i("SWAP BEGIN " + this.l() + " real=" + this.F(var3) + " ->" + this.F(var5));
      this.a("swap begin");
      this.S(var1);
      return true;
   }

   private void f(int var1, int var2) {
      this.S(var1, var2, true);
      this.H();
      this.x();
      this.B();
      this.v();
      this.w();
      xa.i("VISUAL " + this.l() + " real=" + var1 + " shown=" + var2);
   }

   public void onInventorySynchronized(int var1, List<ItemStack> var2, ItemStack var3) {
      if (this.o != null && this.isSwapActive()) {
         this.o.K("receive full sync=" + var1 + " size=" + var2.size() + " cursorCount=" + var3.getCount());
      }

      Minecraft var4 = Minecraft.getInstance();
      if (this.o != null && var4.player != null && var4.player.containerMenu == this.o.x && var1 == this.o.x.containerId) {
         this.o.N(var2, var3);
         if (this.o.d) {
            this.u.z();
         }

         this.I();
      }
   }

   public void onServerSlot(int var1, int var2, ItemStack var3) {
      if (this.o != null && this.isSwapActive()) {
         this.o.K("receive slot sync=" + var1 + " slot=" + var2 + " map=" + this.y(var3) + " count=" + var3.getCount());
      }

      Minecraft var4 = Minecraft.getInstance();
      if (this.o != null && var4.player != null && var4.player.containerMenu == this.o.x) {
         if (var1 == -1 && var2 == -1) {
            this.o.L(-1, var3);
         } else if (var1 == this.o.x.containerId) {
            this.o.L(var2, var3);
         } else if (var1 == 0 && var2 >= 0 && var2 < var4.player.inventoryMenu.slots.size()) {
            Slot var5 = var4.player.inventoryMenu.getSlot(var2);
            if (var5.container == var4.player.getInventory()) {
               this.onPlayerInventorySlot(var5.getContainerSlot(), var3);
            }
         }

         this.I();
      }
   }

   public void onPlayerInventorySlot(int var1, ItemStack var2) {
      if (this.o != null && this.isSwapActive()) {
         this.o.K("receive player slot=" + var1 + " map=" + this.y(var2) + " count=" + var2.getCount());
      }

      Minecraft var3 = Minecraft.getInstance();
      if (this.o != null && var3.player != null && var3.player.containerMenu == this.o.x) {
         for (Slot var5 : this.o.x.slots) {
            if (var5.container == var3.player.getInventory() && var5.getContainerSlot() == var1) {
               this.o.L(var5.index, var2);
               this.I();
               return;
            }
         }
      }
   }

   public int clickRevision(AbstractContainerMenu var1) {
      return this.o != null && var1 == this.o.x && this.o.P ? 32768 : var1.getStateId();
   }

   public void u(BlockPos var1, String var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3.player != null && var3.level != null && var1.equals(this.getAssignedDispenserPosition(var3))) {
         pk.q(
            var2
               + " "
               + this.P(var3)
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
      if (this.d == ze.x.X) {
         pk.q("OPEN ACK seq=" + var1 + " " + this.u.m());
      }

      this.u.n(var1);
      this.I();
   }

   private void I() {
      Minecraft var1 = Minecraft.getInstance();
      if (!this.F && this.o != null && !this.o.j && this.isSwapActive() && var1.player != null) {
         ze.x var2 = this.d;
         this.F = (boolean)1;

         try {
            if (this.d == ze.x.X && this.H >= 0) {
               this.F(ze.x.D);
            }

            if (this.isSwapJuggling()) {
               this.n(var1);
            }
         } finally {
            this.F = (boolean)0;
            if (this.d != var2) {
               pk.q("PACKET continuation " + this.l() + " " + var2 + " -> " + this.d + " syncId=" + this.H);
            }
         }
      }
   }

   public boolean I() {
      return this.i3;
   }

   public boolean blocksUserInteraction() {
      return this.isSwapActive() && (this.o == null || !this.o.j);
   }

   public boolean allowsRecoveryOpen(BlockPos var1) {
      return this.d == ze.x.X && this.H < 0 && this.o == null && var1 != null && var1.equals(this.iV);
   }

   public boolean blocksClose() {
      return this.blocksUserInteraction();
   }

   public boolean h(AbstractContainerMenu var1) {
      return var1 instanceof InventoryMenu && (this.isSwapActive() || this.o != null);
   }

   boolean o() {
      return !this.isSwapActive() && this.o != null && this.o.r();
   }

   public void f(AbstractContainerMenu var1) {
      if (this.h(var1) && this.o()) {
         this.v(Minecraft.getInstance());
      }
   }

   public boolean hidesCursor(AbstractContainerMenu var1) {
      return this.isSwapJuggling() && !this.x.isEmpty() && (this.h(var1) || this.o != null && var1 == this.o.x);
   }

   private String P(Minecraft var1) {
      return "phase="
         + this.d
         + " distance="
         + this.W(var1)
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
      if (this.o != null && this.isSwapActive() && this.o.x.containerId == var1) {
         this.o.K(var2);
      }
   }

   public void X(int var1) {
      if (this.isSwapActive()) {
         pk.q("SERVER CLOSE " + this.l() + " sync=" + var1 + " " + this.P(Minecraft.getInstance()));
      }
   }

   private void g(Minecraft var1, String var2) {
      pk.q("BATCH RECOVERY " + this.l() + " reason=" + var2 + " " + this.P(var1) + " " + (this.o == null ? "batch=null" : this.o.L()));
      xa.q(this.l(), "Manual recovery required: " + var2 + " [phase=" + this.d + "; details: swap-open.log]");
      AbstractContainerMenu var3 = this.o == null ? null : this.o.x;
      this.o = null;
      this.H = -1;
      this.d = ze.x.A;
      this.v.clear();
      this.ig.clear();
      this.H();
      this.x();
      this.B();
      this.v();
      if (var1.player != null && var1.player.containerMenu instanceof DispenserMenu var4 && (var4 == var3 || var1.gui.screen() == null)) {
         var1.gui.setScreen(new DispenserScreen(var4, var1.player.getInventory(), Component.literal(this.l() + " recovery: " + var2)));
      }
   }

   private void n(Minecraft var1) {
      ul var2 = this.o;
      if (var2 == null || var1.player.containerMenu != var2.x || y(var1.gui.screen())) {
         this.g(var1, "handler lost; no replay");
      } else if (!var2.d) {
         if (!this.F && ++this.p > 100) {
            this.g(var1, "initial authoritative inventory missing");
         }
      } else if (this.u.N() && !this.u.Z()) {
         if (!this.F && ++this.p > 100) {
            this.g(var1, "open acknowledgement missing");
         }
      } else if (!var2.i(!this.F)) {
         if (var2.s != null) {
            this.g(var1, var2.s);
         }
      } else if (!var2.w && w5.h(var1)) {
         if (var2.r() && w5.J(this, this.O, this.E)) {
            this.v(var1);
            this.d = ze.x.A;
            this.f(this.C, this.E);
         }
      } else {
         DispenserMenu var3 = (DispenserMenu)var2.x;
         if (this.d == ze.x.D) {
            this.z = -1;
            this.c = -1;

            for (Slot var5 : var3.slots) {
               if (var5.container == var1.player.getInventory() && var5.getContainerSlot() == this.O && this.y(var5.getItem()) == this.B) {
                  this.z = var5.index;
               }
            }

            if (!var2.T()) {
               this.m(var1, var2, "initial local/server ledger or cursor mismatch", "");
               return;
            }

            if (this.z < 0) {
               if (var2.r()) {
                  this.e(false, "draw moved before transfer");
               } else {
                  this.m(var1, var2, "real arrival unavailable", "");
               }

               return;
            }

            if (var3.getSlot(this.z).getItem().getCount() < this.W[this.O]) {
               this.m(var1, var2, "original arrival quantity changed before transfer", "");
               return;
            }

            ArrayList var25 = new ArrayList();
            ArrayList var26 = new ArrayList();
            ArrayList var6 = new ArrayList();
            int var7 = this.getTemplateIdForValue(this.E);
            int var8 = var2.k.get(this.z).getCount() == 1 ? 1 : 0;

            for (Slot var10 : var3.slots) {
               int var11 = var10.getContainerSlot();
               if (var10.container == var1.player.getInventory()
                  && var11 >= 0
                  && var11 < 36
                  && var11 != this.O
                  && this.g[var11] == var7
                  && this.W[var11] > 0
                  && this.y(var10.getItem()) == var7
                  && var10.getItem().getCount() == this.W[var11]) {
                  var26.add(var10.index);
               }
            }

            if (var8 != 0) {
               var26.add(this.z);
            }

            int var27 = var26.size();

            for (Slot var31 : var3.slots) {
               if (!(var31.container instanceof Inventory)) {
                  if (var7 >= 0 && this.y(var31.getItem()) == var7) {
                     var25.add(var31.index);
                  }

                  if (!var31.getItem().isEmpty() && this.y(var31.getItem()) == this.B) {
                     var6.add(var31.index);
                  }
               } else if (var8 == 0
                  && var31.container == var1.player.getInventory()
                  && var31.getContainerSlot() >= 0
                  && var31.getContainerSlot() < 36
                  && this.W[var31.getContainerSlot()] == 0
                  && var31.getItem().isEmpty()
                  && var2.k.get(var31.index).isEmpty()) {
                  var26.add(var31.index);
               }
            }

            var26.subList(var27, var26.size()).sort(Comparator.comparingInt(var1x -> var3.getSlot(var1x).getContainerSlot()));

            for (Slot var32 : var3.slots) {
               if (!(var32.container instanceof Inventory) && var32.getItem().isEmpty() && var2.k.get(var32.index).isEmpty()) {
                  var6.add(var32.index);
               }
            }

            var6.removeIf(var2x -> !this.allowsReturnSlot(var3.getSlot(var2x).getContainerSlot(), this.C));
            on.o var30 = on.k(
               var25,
               var26,
               var6,
               (var3x, var4) -> var2.J(var1, var3x, var4, var4 == this.z, true).batch() != null,
               var3x -> var2.N(var1, this.z, var3x) != null
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
                     ul.z var23 = var2.J(var1, var17, var22, var22 == this.z, true);
                     if (var23.batch() != null) {
                        var18 = var22;
                        break;
                     }

                     var20.merge(var23.reason(), 1, Integer::sum);
                     if (var14 == null) {
                        var14 = var23.reason();
                     }

                     if (var19 == null) {
                        var19 = "destination=" + var22 + " " + var23.reason() + " " + var2.F(var17, var22);
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
                        ul.z var36 = var2.J(var1, this.z, var35.index, false, true);
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
                           .append(var2.F(this.z, var35.index));
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

               this.m(var1, var2, var33, var12.toString());
               return;
            }

            this.N = var30.source();
            this.c = var30.destination();
            this.Z = var30.returnSlot();
            this.y = var3.getSlot(this.c).getContainerSlot();
            this.M = this.y;
            this.x = x_.f(
               this.O,
               this.M,
               this.W[this.O],
               this.M == this.O ? 0 : this.W[this.M],
               var2.k.get(this.z),
               this.M != this.O && !var2.k.get(this.c).isEmpty() ? var2.k.get(this.c) : var2.k.get(this.N),
               ItemStack::copyWithCount
            );
            this.F(ze.x.r);
            var2.Z(var1, this.z, this.Z, this.l() + " RETURN_REAL", true);
         } else if (this.d == ze.x.r) {
            this.F(ze.x.Z);
            var2.Z(var1, this.N, this.c, this.l() + " DELIVER_ARMED", true);
         } else {
            if (!var2.T()) {
               this.g(var1, "final quantity ledger changed");
               return;
            }

            this.F(true, "two one-card batches confirmed");
         }

         if (var2.s != null) {
            this.g(var1, var2.s);
         }
      }
   }

   private void m(Minecraft var1, ul var2, String var3, String var4) {
      StringBuilder var5 = new StringBuilder(
         "SWAP "
            + this.l()
            + " PREFLIGHT REJECT sync="
            + var2.x.containerId
            + " revision="
            + var2.x.getStateId()
            + " realMap="
            + this.B
            + " armedMap="
            + this.getTemplateIdForValue(this.E)
            + " arrival="
            + this.z
            + " arrivalInv="
            + this.O
            + " source="
            + this.N
            + " destination="
            + this.c
            + " cursorSafe="
            + var2.r()
            + " "
            + var2.t(-1)
            + " reason="
            + var3
            + "\n"
            + var4
      );

      for (Slot var7 : var2.x.slots) {
         var5.append("\n").append(var2.t(var7.index));
         if (var7.container == var1.player.getInventory() && var7.getContainerSlot() >= 0 && var7.getContainerSlot() < 36) {
            var5.append(" inv=")
               .append(var7.getContainerSlot())
               .append(" drawBaselineMap=")
               .append(this.g[var7.getContainerSlot()])
               .append(" drawBaselineCount=")
               .append(this.W[var7.getContainerSlot()]);
         }
      }

      xa.i(var5.toString());
      this.g(var1, var3);
   }

   private void S(Minecraft var1) {
      if (this.d != ze.x.D && this.d != ze.x.r && this.d != ze.x.Z && this.d != ze.x.I) {
         this.R++;
         if (this.R > 1200) {
            this.e(false, "whole-swap timeout");
         } else {
            switch (this.d) {
               case J:
                  this.p++;
                  if (this.H >= 0) {
                     if (this.C(var1) != null) {
                        pk.q("REQUEST retained harness live syncId=" + this.H + " tick=" + this.p);
                        this.F(ze.x.D);
                        this.n(var1);
                     } else if (var1.player != null && var1.player.containerMenu instanceof DispenserMenu var2) {
                        this.H = var2.containerId;
                        pk.q("REQUEST adopting live open syncId=" + var2.containerId + " tick=" + this.p + " screenOpen=" + (var1.gui.screen() != null));
                        xa.i("SWAP " + this.l() + " retained harness vanished - adopting live open syncId=" + var2.containerId);
                        this.F(ze.x.X);
                     } else if (!y(var1.gui.screen())) {
                        this.H = -1;
                        pk.q("REQUEST retained harness gone - reopening tick=" + this.p);
                        xa.i("SWAP " + this.l() + " retained harness vanished - reopening");
                        this.D(var1);
                     } else {
                        pk.q(
                           "REQUEST retained gone but screen open tick="
                              + this.p
                              + " screen="
                              + (var1.gui.screen() == null ? "null" : var1.gui.screen().getClass().getSimpleName())
                              + " handler="
                              + (var1.player.containerMenu == null ? "null" : var1.player.containerMenu.getClass().getSimpleName())
                        );
                     }
                  } else if (this.p >= 1) {
                     pk.q("REQUEST fresh - compact requestContainerOpen tick=" + this.p);
                     this.D(var1);
                  }
                  break;
               case X:
                  this.p++;
                  if (this.H >= 0) {
                     pk.q("WAIT_OPEN open arrived tick=" + this.p + " syncId=" + this.H);
                     xa.i("SWAP " + this.l() + " open-arrived tick=" + this.p + " syncId=" + this.H);
                     this.F(ze.x.D);
                     this.n(var1);
                  } else if (this.p >= 100 && this.o == null) {
                     this.e(false, "dispenser did not open; no cards moved");
                  } else if (this.p >= 660) {
                     pk.q(
                        "WAIT_OPEN open timeout tick="
                           + this.p
                           + " screen="
                           + (var1.gui.screen() == null ? "null" : var1.gui.screen().getClass().getSimpleName())
                     );
                     this.B("open timeout");
                  } else if (this.u.c(this.R)) {
                     this.G(var1);
                  }
                  break;
               default:
                  this.d = ze.x.A;
            }
         }
      } else {
         this.n(var1);
      }
   }

   private void D(Minecraft var1) {
      if (this.iV == null) {
         this.e(false, "dispenser unknown");
      } else {
         this.G(var1);
         this.F(ze.x.X);
      }
   }

   private void G(Minecraft var1) {
      if (this.u.c(this.R) && this.R - this.ii >= 4) {
         if (this.iV != null && var1 != null && var1.player != null && var1.gameMode != null && var1.getConnection() != null && var1.level != null) {
            BlockHitResult var2 = this.g(var1);
            if (var2 == null) {
               pk.q("open blocked: no visible dispenser face pos=" + this.iV);
            } else {
               boolean var3 = var1.player.isShiftKeyDown();
               double var4 = this.W(var1);
               pk.q(
                  "open-issue phase="
                     + this.d
                     + " tick="
                     + this.p
                     + " sneaking="
                     + var3
                     + " dist="
                     + String.format("%.1f", var4)
                     + " syncId="
                     + this.H
                     + " pass="
                     + this.A
               );
               xa.i(
                  "SWAP "
                     + this.l()
                     + " open-issue phase="
                     + this.d
                     + " tick="
                     + this.p
                     + " sneaking="
                     + var3
                     + " dist="
                     + String.format("%.1f", var4)
                     + " syncId="
                     + this.H
               );
               if (!var1.player.isShiftKeyDown() && !var1.options.keyUse.isDown()) {
                  this.q(var1, var2, var1x -> {
                     this.ii = this.R;
                     this.u.j(var1x, this.R);
                  });
               }
            }
         }
      }
   }

   protected void q(Minecraft var1, BlockHitResult var2, IntConsumer var3) {
      pb.N(
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
               this.i3 = (boolean)1;

               try {
                  var1.gameMode.useItemOn(var1.player, InteractionHand.MAIN_HAND, var2);
                  var3.accept(((q4be5233a4c9f3d34)var1.level).LarpRig$getPendingUpdateManager().currentSequence());
                  return true;
               } finally {
                  this.i3 = (boolean)0;
               }
            } else {
               return false;
            }
         }
      );
   }

   static int r(int var0, int var1) {
      return var0 >= 0 && var0 < 9 ? var0 : var1;
   }

   private BlockHitResult g(Minecraft var1) {
      return q(var1, this.iV);
   }

   protected static BlockHitResult q(Minecraft var0, BlockPos var1) {
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

   private void B(String var1) {
      if (this.A < 6) {
         this.A++;
         pk.q("retry (" + var1 + ") pass=" + this.A + " phase=" + this.d + " tick=" + this.p);
         xa.i("SWAP " + this.l() + " retry (" + var1 + ")");
         this.Q(1, "retry " + var1);
      } else {
         pk.q("CRITICAL fail (" + var1 + ") after pass=" + this.A);
         this.e(false, var1);
      }
   }

   private void v(Minecraft var1) {
      this.o = null;
      if (var1 != null && var1.player != null) {
         if (this.H >= 0 && var1.player.containerMenu != null && var1.player.containerMenu.containerId == this.H) {
            pk.q("close suppressed container syncId=" + this.H + " phase=" + this.d);
            if (!(var1.gui.screen() instanceof InventoryScreen) && !(var1.gui.screen() instanceof ChatScreen)) {
               var1.player.closeContainer();
            } else {
               var1.getConnection().send(new ServerboundContainerClosePacket(this.H));
            }
         }

         var1.player.containerMenu = var1.player.inventoryMenu;
         this.H = -1;
      }
   }

   private void e(boolean var1, String var2) {
      if (!var1) {
         xa.q(this.l(), "Swap failed: " + var2);
      }

      xa.i("SWAP " + this.l() + " " + (var1 ? "OK" : "FAIL") + " (" + var2 + ") real=" + this.F(this.C) + " ->" + this.F(this.E));
      if (!var1 && this.m(Minecraft.getInstance())) {
         this.Q(2, "cleanup " + var2);
      } else {
         this.F(var1, var2);
      }
   }

   private void F(boolean var1, String var2) {
      Minecraft var3 = Minecraft.getInstance();
      ul var4 = this.o;
      if (var4 != null) {
         pk.q("BATCH TRACE " + this.l() + " " + this.P(var3) + " " + var4.L());
      }

      pk.q("finishSwapDecided ok=" + var1 + " reason=" + var2 + " syncId=" + this.H + " queue=" + this.pendingLabel() + " phase=" + this.d);
      this.v(var3);
      this.X = 0;
      this.Y = var1 ? 12 : 0;
      if (var1) {
         this.b(this.C, this.E);
      }

      this.w();
      if (var1 && !this.v.isEmpty() && var4 != null) {
         this.ig = new HashMap<>(this.b);
         com.larpclient.m.completeDrawBaseline(this.g, this.W, this.O, this.y, this.getTemplateIdForValue(this.E));
         System.arraycopy(this.g, 0, this.T, 0, 36);
         System.arraycopy(this.W, 0, this.iw, 0, 36);
      }

      this.x();
      this.d = ze.x.A;
      this.P = (boolean)0;
      if (var1) {
         ItemStack var5 = var3.player.getMainHandItem();
         if (!this.isDealtHandLocked()
            || !var5.is(Items.FILLED_MAP)
            || this.y(var5) < 0
            || (this.iz == null || this.y(var5) != this.y(this.iz)) && this.y(var5) != this.getTemplateIdForValue(this.E)) {
            this.B();
            this.v();
         } else {
            this.iz = var5.copy();
            this.iX = (boolean)1;
         }
      }

      if (!var1) {
         this.B();
         this.v();
      }

      this.l = var1 ? 16 : 0;
      this.a("swap end " + (var1 ? "ok" : "fail") + " " + var2);
      if (this.I >= 0 && !this.v.isEmpty() && this.v.peekFirst() == this.a) {
         this.V = this.I;
         this.D = this.a;
         this.O();
      } else {
         this.H();
         this.x();
      }
   }

   private void Q(int var1, String var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (!this.m(var3)) {
         this.v(var3);
         this.Q(var3, var1, var2);
      } else {
         this.g(var3, var2 + ": occupied cursor before batch");
      }
   }

   private void Q(Minecraft var1, int var2, String var3) {
      switch (var2) {
         case 1:
            this.k = 0;
            this.H = -1;
            this.p = 1;
            this.d = ze.x.J;
            break;
         case 2:
            this.F(false, var3);
            break;
         default:
            this.v(var1);
            this.d = ze.x.A;
            this.x();
            xa.i("SWAP " + this.l() + " aborted");
      }
   }

   private boolean m(Minecraft var1) {
      DispenserMenu var2 = this.C(var1);
      return var2 != null && !var2.getCarried().isEmpty();
   }

   private void w() {
      if (!this.v.isEmpty()) {
         xa.i("PLAYED " + this.l() + " next=" + this.v.peekFirst());
         this.v.pollFirst();
         if (this.v.isEmpty()) {
            this.ig.clear();
            if (this.d == ze.x.A && this.H >= 0) {
               if (this.o != null && this.o.r()) {
                  this.v(Minecraft.getInstance());
               } else {
                  this.g(Minecraft.getInstance(), "queue ended without a safe cursor snapshot");
               }
            }
         } else {
            this.N();
         }
      }
   }

   private void F(ze.x var1) {
      this.d = var1;
      if (this.o != null) {
         this.o.K(this.P(Minecraft.getInstance()));
      }

      this.p = 0;
   }

   private void H() {
      this.I = -1;
      this.L = 0;
      this.a = 0;
   }

   private void x() {
      this.x = Map.of();
      this.M = -1;
      this.V = -1;
      this.D = -1;
      this.il = -1;
      this.ip = null;
   }

   public int getSwapSyncId() {
      return this.H;
   }

   private void O() {
      this.il = -1;
      if (this.V >= 0) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null) {
            this.il = this.is >= 0 && this.is < 36 && this.y(var1.player.getInventory().getItem(this.is)) == this.V ? this.is : this.v(var1, this.V);
         }
      }
   }

   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      if (var3 >= 0) {
         int var4 = this.R(new MapId(var3));
         if (var4 >= 1) {
            Minecraft var5 = Minecraft.getInstance();
            Object var6 = var1 == 0 ? var5.player.inventoryMenu : var5.player.containerMenu;
            if (var2 >= 0 && var2 < ((AbstractContainerMenu)var6).slots.size()) {
               Slot var7 = var6.getSlot(var2);
               if (var7.container == var5.player.getInventory() && var7.getContainerSlot() < 36) {
                  this.is = var7.getContainerSlot();
                  if (this.v.isEmpty() || this.d != ze.x.A || this.P(var5, var3) >= 0) {
                     if (this.v.isEmpty()) {
                        this.V = var3;
                        this.O();
                        xa.i("SWAP " + this.l() + " slot-update sync=" + var1 + " slot=" + var2 + " real=" + this.F(var4) + " (no plan armed)");
                     } else if (this.d == ze.x.A) {
                        int var8 = this.v.peekFirst();
                        if (var4 == var8) {
                           xa.i("SWAP " + this.l() + " slot-update real=" + this.F(var4) + " == pending (no swap needed)");
                        } else {
                           this.V = var3;
                           this.D = var8;
                           this.O();
                           this.s();
                           if (!this.swapWorthwhile(var4, var8)) {
                              this.b(var4, var4);
                              xa.i(
                                 "SWAP "
                                    + this.l()
                                    + " skip (dealer already wins) real="
                                    + this.F(var4)
                                    + " armed="
                                    + this.F(var8)
                                    + " queue="
                                    + this.pendingLabel()
                              );
                              this.x();
                              this.w();
                           } else {
                              if (!this.J(Minecraft.getInstance(), var3, var4) && this.v(Minecraft.getInstance(), var3) >= 0) {
                                 this.I = var3;
                                 this.L = var4;
                                 this.a = var8;
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

   static boolean y(Screen var0) {
      return var0 != null && !(var0 instanceof InventoryScreen) && !(var0 instanceof ChatScreen);
   }

   private DispenserMenu C(Minecraft var1) {
      if (var1 != null && var1.player != null && !y(var1.gui.screen())) {
         return var1.player.containerMenu instanceof DispenserMenu var2 && var2.containerId == this.H ? var2 : null;
      } else {
         return null;
      }
   }

   protected boolean allowsReturnSlot(int var1, int var2) {
      return true;
   }

   private int F(DispenserMenu var1, int var2) {
      if (var2 < 0) {
         return -1;
      }

      for (Slot var4 : var1.slots) {
         if (var4 != null && !(var4.container instanceof Inventory)) {
            ItemStack var5 = var4.getItem();
            if (var5 != null && var5.is(Items.FILLED_MAP) && this.y(var5) == var2) {
               return var4.index;
            }
         }
      }

      return -1;
   }

   private int o(DispenserMenu var1, ItemStack var2) {
      int var3 = this.y(var2);
      int var4 = this.R((MapId)var2.getComponents().get(DataComponents.MAP_ID));
      int var5 = -1;
      if (var4 >= 1 && var3 >= 0) {
         var5 = this.F(var1, var3);
         if (var5 < 0) {
            var5 = this.containerIndexForValue(var4);
         }
      } else {
         var5 = this.N;
      }

      if (var5 >= 0) {
         Slot var6 = var1.getSlot(var5);
         if (var6 != null) {
            ItemStack var7 = var6.getItem();
            if (var7 != null && !var7.isEmpty() && this.y(var7) != var3) {
               var5 = -1;
            }
         }
      }

      if (var5 < 0) {
         var5 = this.g(var1);
      }

      return var5;
   }

   private int g(DispenserMenu var1) {
      for (Slot var3 : var1.slots) {
         if (var3 != null && !(var3.container instanceof Inventory) && var3.getItem().isEmpty()) {
            return var3.index;
         }
      }

      return -1;
   }

   private int Q(DispenserMenu var1, int var2) {
      int var3 = this.getTemplateIdForValue(var2);
      return var3 < 0 ? -1 : this.F(var1, var3);
   }

   private int v(Minecraft var1, int var2) {
      if (var1 != null && var1.player != null) {
         Inventory var3 = var1.player.getInventory();

         for (int var4 = 0; var4 < var3.getContainerSize(); var4++) {
            ItemStack var5 = var3.getItem(var4);
            if (var5 != null && var5.is(Items.FILLED_MAP) && this.y(var5) == var2) {
               return var4;
            }
         }

         return -1;
      } else {
         return -1;
      }
   }

   private int R(DispenserMenu var1, int var2) {
      if (var2 < 0) {
         return -1;
      }

      for (Slot var4 : var1.slots) {
         if (var4 != null && var4.container instanceof Inventory) {
            ItemStack var5 = var4.getItem();
            if (var5 != null && var5.is(Items.FILLED_MAP) && this.y(var5) == var2) {
               return var4.index;
            }
         }
      }

      return -1;
   }

   private boolean l(Minecraft var1, int var2) {
      return this.v(var1, var2) >= 0;
   }

   private int V(int var1) {
      return var1 >= 0 && var1 < 9 ? var1 + 36 : var1;
   }

   private boolean Q(DispenserMenu var1, int var2) {
      return this.I(var1, this.getTemplateIdForValue(var2));
   }

   private boolean I(DispenserMenu var1, int var2) {
      ItemStack var3 = var1.getCarried();
      return var3 != null && var3.is(Items.FILLED_MAP) && this.y(var3) == var2;
   }

   private boolean g(DispenserMenu var1, int var2, int var3) {
      return this.i(var1, var2, this.getTemplateIdForValue(var3));
   }

   private boolean i(DispenserMenu var1, int var2, int var3) {
      if (var2 >= 0 && var2 < var1.slots.size()) {
         Slot var4 = var1.getSlot(var2);
         if (var4 == null) {
            return false;
         }

         ItemStack var5 = var4.getItem();
         return var5 != null && var5.is(Items.FILLED_MAP) && this.y(var5) == var3;
      } else {
         return false;
      }
   }

   private double W(Minecraft var1) {
      if (var1 != null && var1.player != null && this.iV != null) {
         AABB var2 = new AABB(this.iV);
         return Math.sqrt(var2.distanceToSqr(var1.player.getEyePosition()));
      } else {
         return -1.0;
      }
   }

   private void a(String var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         StringBuilder var3 = new StringBuilder();
         int var4 = var2.player.getInventory().getSelectedSlot();

         for (int var5 = 0; var5 < var2.player.getInventory().getContainerSize(); var5++) {
            ItemStack var6 = var2.player.getInventory().getItem(var5);
            if (var6 != null && !var6.isEmpty() && var6.is(Items.FILLED_MAP)) {
               MapId var7 = (MapId)var6.getComponents().get(DataComponents.MAP_ID);
               int var8 = var7 == null ? -1 : this.R(var7);
               if (var8 >= 1) {
                  if (!var3.isEmpty()) {
                     var3.append(", ");
                  }

                  var3.append("v").append(this.F(var8)).append("x").append(var6.getCount()).append("@").append(var5).append(var5 == var4 ? "(sel)" : "");
               }
            }
         }

         xa.i("DUMP " + this.l() + " " + var1 + " selected=" + var4 + " cards=[" + var3 + "]");
      }
   }

   private int y(ItemStack var1) {
      MapId var2 = (MapId)var1.getComponents().get(DataComponents.MAP_ID);
      return var2 == null ? -1 : var2.id();
   }

   private int R(MapId var1) {
      if (var1 == null) {
         return -1;
      }

      kq var2 = kq.I();
      if (var2.Z() && var2.f()) {
         return var2.P(var1.id());
      }

      for (Entry var4 : this.J.entrySet()) {
         if (var4.getValue() != null && ((MapId)var4.getValue()).id() == var1.id()) {
            return (Integer)var4.getKey();
         }
      }

      return -1;
   }

   private Map<Integer, Integer> R() {
      return this.K.computeIfAbsent(this.L(), var0 -> new HashMap<>());
   }

   private Map<Integer, Integer> l() {
      return this.e.computeIfAbsent(this.L(), var0 -> new HashMap<>());
   }

   private String L() {
      return this.ik == null ? "" : this.ik;
   }

   private boolean v() {
      Map var1 = this.U();

      for (Entry var3 : var1.entrySet()) {
         if ((Integer)var3.getValue() > this.ig.getOrDefault(var3.getKey(), 0)) {
            return true;
         }
      }

      return false;
   }

   private Map<Integer, Integer> U() {
      HashMap var1 = new HashMap();
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.player != null) {
         for (int var3 = 0; var3 < var2.player.getInventory().getContainerSize(); var3++) {
            this.H(var1, var2.player.getInventory().getItem(var3));
         }
      }

      if (var2 != null && var2.level != null) {
         for (Entity var4 : var2.level.entitiesForRendering()) {
            if (var4 instanceof ItemFrame var5) {
               this.H(var1, var5.getItem());
            }
         }
      }

      return var1;
   }

   private void H(Map<Integer, Integer> var1, ItemStack var2) {
      if (var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP)) {
         MapId var3 = (MapId)var2.getComponents().get(DataComponents.MAP_ID);
         if (var3 != null) {
            var1.merge(var3.id(), var2.getCount(), Integer::sum);
         }
      }
   }

   public enum x {
      A,
      J,
      X,
      D,
      r,
      Z,
      I;
   }
}
