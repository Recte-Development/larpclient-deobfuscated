package com.larpclient;

import com.larp.obf.q41a5851bcf8e876a.qb75a226e201a934e.q8ecc8da09c365bfb;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.dialog.DialogScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.dialog.ActionButton;
import net.minecraft.server.dialog.CommonDialogData;
import net.minecraft.server.dialog.ConfirmationDialog;
import net.minecraft.server.dialog.Dialog;
import net.minecraft.server.dialog.MultiActionDialog;
import net.minecraft.server.dialog.body.DialogBody;
import net.minecraft.server.dialog.body.ItemBody;
import net.minecraft.server.dialog.body.PlainMessage;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.ItemLore;

public final class vk {
   static final int x = 100;
   static final int N = 3;
   static final int V = 60;
   static final int C = 100;
   static final int D = 12;
   static final int n = 5;
   static final int O = 192;
   static final int Z = 16;
   static final int I = 6;
   static final int i = 3;
   private static final List<String> t = new ArrayList<>();
   private final String J;
   private vk.q l = vk.q.O;
   private vk.a X = vk.a.A;
   private pc.c e;
   private int L;
   private int U;
   private int p;
   private int R;
   private int w;
   private int q;
   private int o;
   private int y = -1000;
   private int r;
   private int H;
   private long P;
   private long d;
   private long g;
   private int T = -1;
   private int f = -1;
   private int z;
   private vk.x k;
   private final Set<String> A = new HashSet<>();
   private String G = "";
   private boolean M;
   private boolean b;
   private Item B;
   private String W = "";
   private long E = -1L;
   private boolean Q;
   private boolean m;
   private final List<vk.x> h = new ArrayList<>();
   private final List<vk.x> Y = new ArrayList<>();
   private int S;
   private int F;
   private boolean s;
   private long j = -1L;

   vk(String var1) {
      this.J = var1;
   }

   public static void a(Component var0) {
      if (var0 != null) {
         synchronized (t) {
            if (t.size() < 64) {
               t.add(var0.getString());
            }
         }
      }
   }

   public void L(pc.c var1, int var2, long var3, long var5) {
      this.B = null;
      this.W = "";
      this.E = -1L;
      this.Q = (boolean)0;
      this.k(var1, var2, var3, var5);
   }

   public void p(Item var1, int var2, long var3, long var5, long var7) {
      this.B = var1;
      this.W = new ItemStack(var1).getHoverName().getString();
      this.E = var5;
      this.Q = (boolean)1;
      this.k(null, var2, var3 * 64L, var7);
   }

   private void k(pc.c var1, int var2, long var3, long var5) {
      this.e = var1;
      this.L = var2;
      this.P = var3;
      this.d = var5;
      this.U = this.p = this.R = this.H = 0;
      this.g = 0L;
      this.A.clear();
      this.k = null;
      this.G = "";
      this.T = this.f = -1;
      this.b = (boolean)0;
      this.M = (boolean)0;
      this.m = (boolean)(this.B != null ? 1 : 0);
      this.s = (boolean)0;
      this.h.clear();
      this.Y.clear();
      this.S = this.F = 0;
      this.j = -1L;
      synchronized (t) {
         t.clear();
      }

      this.l = vk.q.M;
      this.Z(vk.a.A);
      if (this.B != null) {
         _q.z(
            this.J,
            "Buying {} {} from /ah: at most {} a block, {} a listing, budget {}",
            var2,
            this.f(),
            pc.b(var3 / 64L),
            this.E < 0L ? "no cap" : pc.b(this.E),
            pc.b(var5)
         );
      } else {
         _q.z(this.J, "Buying {} {} from /ah: at most {} per 64, budget {}", var2, this.f(), pc.b(var3), pc.b(var5));
      }
   }

   public vk.q E() {
      return this.l;
   }

   public int j() {
      return this.U;
   }

   public long u() {
      return this.g;
   }

   public String a() {
      return this.G;
   }

   public boolean M() {
      return this.M;
   }

   public boolean W() {
      return this.l == vk.q.M;
   }

   public long o() {
      return this.j;
   }

   public void B(String var1) {
      if (this.l == vk.q.M) {
         this.u(vk.q.w, var1);
      }
   }

   private void Z(vk.a var1) {
      this.X = var1;
      this.w = 0;
      this.q = 0;
      this.o = 0;
   }

   private String f() {
      return this.B != null ? this.W : (this.e == pc.c.x ? "Flight Duration 3 rockets" : "XP bottles");
   }

   private String m() {
      return this.B != null ? pc.J(this.W) : pc.a(this.e);
   }

   public vk.q t() {
      if (this.l != vk.q.M) {
         return this.l;
      }

      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.gameMode != null && var1.getConnection() != null) {
         this.r++;
         this.w++;
         if (!var1.player.containerMenu.getCarried().isEmpty() && this.X != vk.a.W && this.X != vk.a.T && this.r - this.y > 40) {
            return this.u(vk.q.w, "the cursor holds an item");
         }

         Screen var2 = d1.E();
         String var3 = var2 == null ? "" : var2.getTitle().getString();
         AbstractContainerMenu var4 = var1.player.containerMenu;
         int var5 = var4 instanceof ChestMenu var6 && var6.getRowCount() == 6 && pc.q(var3) > 0 ? 1 : 0;
         switch (this.X) {
            case A:
               this.o(var1, var2 != null, (boolean)var5);
               break;
            case X:
            case L:
               if (var5 == 0) {
                  if (var2 != null && !this.b) {
                     return this.u(vk.q.w, "another menu opened: " + var3);
                  }

                  if (var2 == null && this.w > 50) {
                     this.Z(vk.a.A);
                     return this.l;
                  }

                  if (var2 != null) {
                     return this.u(vk.q.w, "an unexpected menu opened: " + var3);
                  }

                  return this.l;
               }

               if (!this.d(var4)) {
                  return this.l;
               }

               int var8 = pc.q(var3);
               if (this.X == vk.a.L && var8 <= this.f) {
                  if (this.w > 100) {
                     return this.u(vk.q.w, "the next page did not open");
                  }

                  return this.l;
               }

               this.T = var4.containerId;
               this.f = var8;
               if (this.X == vk.a.L) {
                  this.Z(vk.a.X);
               }

               this.B(var1, (ChestMenu)var4, var8);
               break;
            case W:
               if (var2 instanceof DialogScreen var7) {
                  this.h(var1, var7, var3);
               } else {
                  this.r(var1, var2 != null, (boolean)var5, var3, var4);
               }
               break;
            case T:
               this.F(var1, (boolean)var5, var2 != null, var3);
         }

         return this.l;
      } else {
         return this.u(vk.q.w, "the world went away");
      }
   }

   private void o(Minecraft var1, boolean var2, boolean var3) {
      if (var3 && this.b && !this.s) {
         this.Z(vk.a.X);
      } else if (this.w == 1) {
         this.s = (boolean)0;
         if (var2) {
            var1.player.closeContainer();
         }

         if (++this.p > 12 + this.H) {
            this.u(vk.q.w, "searched /ah " + this.p + " times without finishing");
         } else {
            this.R = 0;
            var1.player.connection.sendCommand(this.m());
            this.b = (boolean)1;
            _q.c(this.J, "Sent /{}", this.m());
         }
      } else if (var3) {
         this.Z(vk.a.X);
      } else {
         if (this.w > 100) {
            this.u(vk.q.w, "/ah did not open");
         }
      }
   }

   private boolean d(AbstractContainerMenu var1) {
      int var2 = var1.containerId * 31 + var1.getStateId();

      for (int var3 = 0; var3 < 54 && var3 < var1.slots.size(); var3++) {
         ItemStack var4 = var1.getSlot(var3).getItem();
         var2 = 31 * var2 + ItemStack.hashItemAndComponents(var4) * 7 + var4.getCount();
      }

      if (var2 != this.o) {
         this.o = var2;
         this.q = 0;
         return false;
      } else {
         return ++this.q >= 3;
      }
   }

   private void B(Minecraft var1, ChestMenu var2, int var3) {
      if (this.U >= this.L) {
         this.u(vk.q.S, "");
      } else if (this.H >= 192) {
         this.u(vk.q.S, "at the purchase limit for one stop");
      } else if (this.r - this.y >= 6) {
         if (this.B != null) {
            this.e(var1, var2, var3);
         } else if (l(var1) == 0) {
            this.u(this.U > 0 ? vk.q.S : vk.q.w, "no free hotbar slot for more " + this.f());
         } else {
            vk.x var4 = null;
            long var5 = Long.MAX_VALUE;

            for (int var7 = 0; var7 < 45; var7++) {
               vk.x var8 = this.E(var2, var7, var3);
               if (var8 != null
                  && !this.A.contains(G(var8))
                  && pc.u(var8.price(), var8.count(), this.P)
                  && (this.d < 0L || this.g + var8.price() <= this.d)
                  && var8.count() <= this.L - this.U + 63) {
                  long var9 = var8.price() * 64L / var8.count();
                  if (var9 < var5) {
                     var5 = var9;
                     var4 = var8;
                  }
               }
            }

            if (var4 != null) {
               this.z(var1, var2, var4, var3);
            } else {
               int var11 = this.S(var2, pc.i.F);
               if (var11 >= 0 && ++this.R <= 5) {
                  this.v(var1, var2, var11);
                  this.Z(vk.a.L);
               } else {
                  this.u(
                     this.U > 0 ? vk.q.S : vk.q.w,
                     this.d >= 0L && this.g > 0L
                        ? "no listing fits the price limit and the budget left"
                        : "no listing under the price limit (" + pc.b(this.P) + " per 64)"
                  );
               }
            }
         }
      }
   }

   private void e(Minecraft var1, ChestMenu var2, int var3) {
      int var4 = this.A(var1);
      int var5 = Math.min(this.L - this.U, var4);
      if (var5 <= 0) {
         this.u(this.U > 0 ? vk.q.S : vk.q.w, "no free inventory room for more " + this.f());
      } else {
         int var6 = this.S(var2, pc.i.F);
         if (this.m) {
            long var17 = 0L;

            for (int var18 = 0; var18 < 45; var18++) {
               ItemStack var10 = var2.getSlot(var18).getItem();
               if (!var10.isEmpty()) {
                  var17 = Math.max(var17, pc.Y(v(var10)));
               }

               vk.x var11 = this.E(var2, var18, var3);
               if (var11 != null) {
                  long var12 = (var11.price() + var11.count() - 1L) / var11.count();
                  if (this.j < 0L || var12 < this.j) {
                     this.j = var12;
                  }

                  if (pc.u(var11.price(), var11.count(), this.P) && (this.E < 0L || var11.price() <= this.E) && !this.h.contains(var11)) {
                     this.h.add(var11);
                  }
               }
            }

            int var19 = new ItemStack(this.B).getMaxStackSize();
            long var20 = 0L;

            for (vk.x var13 : this.h) {
               if (var17 > 0L && var13.price() * var19 <= var17 * var13.count()) {
                  var20 += var13.count();
               }
            }

            if (var20 < var5 && var6 >= 0 && this.R < 16) {
               this.R++;
               this.v(var1, var2, var6);
               this.Z(vk.a.L);
            } else {
               this.m = (boolean)0;
               this.F++;
               this.Y.clear();
               this.Y.addAll(J(this.h, var5, var4, this.d < 0L ? -1L : this.d - this.g));
               if (this.Y.isEmpty()) {
                  this.u(this.U > 0 ? vk.q.S : vk.q.w, this.A());
               } else {
                  this.S = 0;
                  long var22 = 0L;
                  int var14 = 0;

                  for (vk.x var16 : this.Y) {
                     this.S = Math.max(this.S, var16.page());
                     var22 += var16.price();
                     var14 += var16.count();
                  }

                  _q.z(
                     this.J,
                     "Read {} pages of /ah for {}: {} listings under the caps, cheapest {} each; plan {} for {} in {} listings ({} each){}",
                     var3,
                     this.f(),
                     this.h.size(),
                     this.j < 0L ? "none" : pc.b(this.j),
                     var14,
                     pc.b(var22),
                     this.Y.size(),
                     pc.b((var22 + var14 - 1L) / var14),
                     var14 < var5 ? "; " + var5 + " were needed, the budget covers no more" : ""
                  );
                  this.s = (boolean)1;
                  this.Z(vk.a.A);
               }
            }
         } else {
            for (int var7 = 0; var7 < 45; var7++) {
               vk.x var8 = this.E(var2, var7, var3);
               if (var8 != null && !this.A.contains(G(var8))) {
                  int var9 = this.S(var8);
                  if (var9 >= 0 && (this.d < 0L || this.g + var8.price() <= this.d)) {
                     this.Y.remove(var9);
                     this.z(var1, var2, var8, var3);
                     return;
                  }
               }
            }

            if (!this.Y.isEmpty() && var6 >= 0 && var3 < this.S + 1 && this.R < 16) {
               this.R++;
               this.v(var1, var2, var6);
               this.Z(vk.a.L);
            } else if (this.F < 3) {
               _q.z(this.J, "{} planned listings of {} were gone; reading /ah again for the {} still needed", this.Y.size(), this.f(), var5);
               this.m = (boolean)1;
               this.h.clear();
               this.Y.clear();
               this.s = (boolean)1;
               this.Z(vk.a.A);
            } else {
               this.u(this.U > 0 ? vk.q.S : vk.q.w, "the listings kept changing; bought " + this.U + " of " + this.L);
            }
         }
      }
   }

   private String A() {
      return this.d >= 0L && this.g > 0L
         ? "no listing fits the price limit and the budget left"
         : "no listing under "
            + pc.b(this.P / 64L)
            + " per block"
            + (this.E >= 0L ? " and " + pc.b(this.E) + " per listing" : "")
            + (this.j >= 0L ? " (the cheapest was " + pc.b(this.j) + " each)" : "");
   }

   private int S(vk.x var1) {
      for (int var2 = 0; var2 < this.Y.size(); var2++) {
         if (this.Y.get(var2).count() == var1.count() && this.Y.get(var2).price() == var1.price()) {
            return var2;
         }
      }

      return -1;
   }

   private void z(Minecraft var1, ChestMenu var2, vk.x var3, int var4) {
      this.k = var3;
      this.z = this.o(var1);
      this.v(var1, var2, var3.slot());
      _q.z(this.J, "Buying {} {} for {} (page {}, slot {})", var3.count(), this.f(), pc.b(var3.price()), var4, var3.slot());
      this.Z(vk.a.W);
   }

   static List<vk.x> J(List<vk.x> var0, int var1, int var2, long var3) {
      var1 = Math.min(var1, var2);
      if (var1 > 0 && !var0.isEmpty()) {
         long var5 = 2305843009213693951L;
         int var7 = var0.size();
         long[] var8 = new long[var2 + 1];
         Arrays.fill(var8, 2305843009213693951L);
         var8[0] = 0L;
         boolean[][] var9 = new boolean[var7][var2 + 1];

         for (int var10 = 0; var10 < var7; var10++) {
            int var11 = ((vk.x)var0.get(var10)).count();
            long var12 = ((vk.x)var0.get(var10)).price();
            if (var11 > 0 && var11 <= var2 && var12 > 0L) {
               for (int var14 = var2; var14 >= var11; var14--) {
                  if (var8[var14 - var11] != 2305843009213693951L) {
                     long var15 = var8[var14 - var11] + var12;
                     if (var15 < var8[var14]) {
                        var8[var14] = var15;
                        var9[var10][var14] = (boolean)1;
                     }
                  }
               }
            }
         }

         int var18 = -1;

         for (int var19 = var1; var19 <= var2; var19++) {
            if (var8[var19] < 2305843009213693951L && (var3 < 0L || var8[var19] <= var3) && (var18 < 0 || var8[var19] < var8[var18])) {
               var18 = var19;
            }
         }

         if (var18 < 0) {
            for (int var20 = var1 - 1; var20 > 0; var20--) {
               if (var8[var20] < 2305843009213693951L && (var3 < 0L || var8[var20] <= var3)) {
                  var18 = var20;
                  break;
               }
            }
         }

         if (var18 < 0) {
            return List.of();
         }

         ArrayList var21 = new ArrayList();
         int var22 = var7 - 1;

         for (int var13 = var18; var22 >= 0 && var13 > 0; var22--) {
            if (var9[var22][var13]) {
               var21.add((vk.x)var0.get(var22));
               var13 -= ((vk.x)var0.get(var22)).count();
            }
         }

         var21.sort(Comparator.comparingInt(vk.x::page).thenComparingInt(vk.x::slot));
         return var21;
      } else {
         return List.of();
      }
   }

   private int A(Minecraft var1) {
      int var2 = new ItemStack(this.B).getMaxStackSize();
      int var3 = 0;
      Inventory var4 = var1.player.getInventory();

      for (int var5 = 0; var5 < 36; var5++) {
         ItemStack var6 = var4.getItem(var5);
         if (var6.isEmpty()) {
            var3 += var2;
         } else if (this.J(var6) && var6.getComponentsPatch().isEmpty()) {
            var3 += Math.max(0, var2 - var6.getCount());
         }
      }

      return var3;
   }

   vk.x E(ChestMenu var1, int var2, int var3) {
      ItemStack var4 = var1.getSlot(var2).getItem();
      if (!this.J(var4)) {
         return null;
      }

      long var5 = pc.Y(v(var4));
      return var5 <= 0L ? null : new vk.x(var2, var4.getCount(), var5, var3);
   }

   private boolean J(ItemStack var1) {
      if (var1.isEmpty()) {
         return false;
      } else if (this.B != null) {
         ItemContainerContents var2 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
         return var1.is(this.B) && (var2 == null || var2.nonEmptyItemCopyStream().findAny().isEmpty());
      } else {
         return this.e != pc.c.x ? var1.is(Items.EXPERIENCE_BOTTLE) : var1.is(Items.FIREWORK_ROCKET) && sy.M(var1) && sy.E(var1) == 3;
      }
   }

   private static List<String> v(ItemStack var0) {
      ItemLore var1 = (ItemLore)var0.get(DataComponents.LORE);
      if (var1 == null) {
         return List.of();
      }

      ArrayList var2 = new ArrayList();

      for (Component var4 : var1.lines()) {
         var2.add(var4.getString());
      }

      return var2;
   }

   private static String G(vk.x var0) {
      return var0.page() + ":" + var0.slot() + ":" + var0.count() + ":" + var0.price();
   }

   private int S(ChestMenu var1, pc.i var2) {
      int var3 = -1;

      for (int var4 = 45; var4 < 54; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (!var5.isEmpty() && pc.z(BuiltInRegistries.ITEM.getKey(var5.getItem()).toString(), var5.getHoverName().getString()) == var2) {
            if (var3 >= 0) {
               return -1;
            }

            var3 = var4;
         }
      }

      return var3;
   }

   private void r(Minecraft var1, boolean var2, boolean var3, String var4, AbstractContainerMenu var5) {
      if (!this.D()) {
         if (this.o(var1) > this.z) {
            this.Z(vk.a.T);
         } else if (var3) {
            if (this.w > 60) {
               this.A.add(G(this.k));
               _q.z(this.J, "No confirm screen for that listing; trying another");
               this.Z(vk.a.X);
            }
         } else if (!var2) {
            if (this.w > 60) {
               this.u(vk.q.w, "the confirm screen never opened");
            }
         } else if (!this.d(var5)) {
            if (this.w > 60) {
               this.u(vk.q.w, "the confirm screen kept changing");
            }
         } else if (this.r - this.y >= 6) {
            int var6 = -1;
            int var7 = 0;
            byte var8 = 0;
            String var9 = null;

            for (int var10 = 0; var10 < var5.slots.size(); var10++) {
               Slot var11 = var5.getSlot(var10);
               if (var11.container != var1.player.getInventory()) {
                  ItemStack var12 = var11.getItem();
                  if (!var12.isEmpty()) {
                     String var13 = var12.getHoverName().getString();
                     String var14 = BuiltInRegistries.ITEM.getKey(var12.getItem()).toString();
                     if (pc.D(var13).contains("sell")) {
                        this.u(vk.q.w, "the confirm screen has a selling button: " + var4);
                        return;
                     }

                     if (this.J(var12)) {
                        long var15 = pc.Y(v(var12));
                        if (var12.getCount() != this.k.count() || var15 >= 0L && var15 != this.k.price()) {
                           this.u(vk.q.w, "the confirm screen shows " + var12.getCount() + " for " + pc.b(var15) + ", not the chosen listing");
                           return;
                        }

                        var8 = 1;
                     } else {
                        pc.m var17 = pc.r(var14, var13);
                        if (var17 == pc.m.R) {
                           var7++;
                        }

                        if (var17 == pc.m.A) {
                           String var16 = var14 + "|" + pc.D(var13);
                           if (var9 != null && !var9.equals(var16)) {
                              this.u(vk.q.w, "the confirm screen has two different confirm buttons: " + var4);
                              return;
                           }

                           var9 = var16;
                           if (var6 < 0) {
                              var6 = var10;
                           }
                        }
                     }
                  }
               }
            }

            if (var8 != 0 && var6 >= 0 && var7 != 0) {
               this.v(var1, var5, var6);
               _q.c(this.J, "Confirmed on '{}' slot {}", var4, var6);
               this.Z(vk.a.T);
            } else {
               _q.z(
                  this.J,
                  "Unrecognized confirm screen '{}': listing shown {}, confirm slot {}, cancel buttons {}",
                  var4,
                  Boolean.valueOf((boolean)var8),
                  var6,
                  var7
               );
               this.u(vk.q.w, "the confirm screen was not recognized (" + var4 + ")");
            }
         }
      }
   }

   private void h(Minecraft var1, DialogScreen<?> var2, String var3) {
      if (!this.D()) {
         if (this.r - this.y >= 6) {
            Dialog var4 = ((q8ecc8da09c365bfb)var2).larp$dialog();
            ActionButton var5 = null;
            if (var4 instanceof ConfirmationDialog var6) {
               if (pc.N(var6.yesButton().button().label().getString())) {
                  var5 = var6.yesButton();
               }
            } else if (var4 instanceof MultiActionDialog var7 && var7.actions().size() == 2) {
               String var8 = ((ActionButton)var7.actions().get(0)).button().label().getString();
               String var9 = ((ActionButton)var7.actions().get(1)).button().label().getString();
               if (pc.N(var8) && pc.J(var9)) {
                  var5 = (ActionButton)var7.actions().get(0);
               } else if (pc.J(var8) && pc.N(var9)) {
                  var5 = (ActionButton)var7.actions().get(1);
               }
            }

            if (var4 != null && var5 != null) {
               CommonDialogData var17 = var4.common();
               String var18 = pc.D(var17.title().getString());
               if (!pc.e(var18)) {
                  this.u(vk.q.w, "an unexpected dialog opened: " + var17.title().getString());
               } else if (!var17.inputs().isEmpty()) {
                  this.u(vk.q.w, "the purchase dialog asks for input");
               } else {
                  int var19 = 0;
                  long var20 = -1L;

                  for (DialogBody var12 : var17.body()) {
                     if (var12 instanceof ItemBody var13) {
                        ItemStack var21 = var13.item().create();
                        if (!this.J(var21) || var21.getCount() != this.k.count()) {
                           this.u(vk.q.w, "the purchase dialog shows " + var21.getCount() + " " + var21.getHoverName().getString() + ", not the chosen listing");
                           return;
                        }

                        var19++;
                        if (var13.description().isPresent()) {
                           var20 = Math.max(var20, pc.Y(List.of(((PlainMessage)var13.description().get()).contents().getString())));
                        }
                     } else if (var12 instanceof PlainMessage var14) {
                        long var15 = pc.Y(List.of(var14.contents().getString()));
                        if (var15 >= 0L) {
                           var20 = var15;
                        }
                     }
                  }

                  if (var19 == 1 && var20 == this.k.price()) {
                     var2.runAction(var5.action().flatMap(var0 -> var0.createAction(Map.of())));
                     this.y = this.r;
                     _q.z(this.J, "Answered Yes to '{}' for {} {} at {}", var17.title().getString(), this.k.count(), this.f(), pc.b(this.k.price()));
                     this.Z(vk.a.T);
                  } else {
                     _q.z(this.J, "Purchase dialog '{}' did not match: items {}, price {} for listing {}", var17.title().getString(), var19, var20, this.k);
                     this.u(vk.q.w, "the purchase dialog did not show the chosen listing and its price");
                  }
               }
            } else {
               _q.z(this.J, "Unrecognized dialog '{}': {}", var3, var4 == null ? "none" : var4.getClass().getSimpleName() + " " + var4);
               this.u(vk.q.w, "an unexpected dialog opened: " + var3);
            }
         }
      }
   }

   private void F(Minecraft var1, boolean var2, boolean var3, String var4) {
      if (!this.D()) {
         int var5 = this.o(var1);
         if (var5 >= this.z + this.k.count() || var5 > this.z && this.w > 20) {
            int var6 = var5 - this.z;
            this.U += var6;
            this.H++;
            this.g = this.g + this.k.price();
            this.A.add(G(this.k));
            _q.z(this.J, "Bought {} {} for {}; {} of {} wanted, {} spent", var6, this.f(), pc.b(this.k.price()), this.U, this.L, pc.b(this.g));
            this.k = null;
            if (this.U >= this.L) {
               this.u(vk.q.S, "");
            } else {
               this.Z(var2 ? vk.a.X : vk.a.A);
            }
         } else if (var3 && !var2 && this.w > 10 && !pc.D(var4).isEmpty()) {
            if (this.w > 100) {
               this.u(vk.q.w, "the purchase was not confirmed (still on " + var4 + ")");
            }
         } else {
            if (this.w > 100) {
               this.u(vk.q.w, "the items never arrived after confirming");
            }
         }
      }
   }

   private boolean D() {
      List var1;
      synchronized (t) {
         var1 = List.copyOf(t);
         t.clear();
      }

      for (String var3 : var1) {
         pc.j var4 = pc.p(var3);
         if (var4 != pc.j.R) {
            _q.z(this.J, "Auction reply ({}): {}", var4, var3);
            switch (var4) {
               case C:
                  this.A.add(G(this.k));
                  this.k = null;
                  this.Z(vk.a.A);
                  return true;
               case h:
                  this.M = (boolean)1;
                  this.u(this.U > 0 ? vk.q.S : vk.q.w, "not enough money");
                  return true;
               case a:
                  this.u(this.U > 0 ? vk.q.S : vk.q.w, "the inventory is full");
                  return true;
               case E:
                  long var5 = pc.S(var3);
                  if (this.k != null && var5 > this.k.price()) {
                     this.u(vk.q.w, "the receipt says " + pc.b(var5) + ", more than the listing's " + pc.b(this.k.price()));
                     return true;
                  }
            }
         }
      }

      return false;
   }

   private void v(Minecraft var1, AbstractContainerMenu var2, int var3) {
      ItemStack var4 = var2.getSlot(var3).getItem();
      if (pc.D(var4.getHoverName().getString()).contains("sell")) {
         this.u(vk.q.w, "refused to click a selling button");
      } else {
         var1.gameMode.handleContainerInput(var2.containerId, var3, 0, ContainerInput.PICKUP, var1.player);
         this.y = this.r;
      }
   }

   private int o(Minecraft var1) {
      int var2 = 0;
      Inventory var3 = var1.player.getInventory();

      for (int var4 = 0; var4 < 36; var4++) {
         if (this.J(var3.getItem(var4))) {
            var2 += var3.getItem(var4).getCount();
         }
      }

      if (this.J(var1.player.getOffhandItem())) {
         var2 += var1.player.getOffhandItem().getCount();
      }

      return var2;
   }

   private static int c(Minecraft var0) {
      int var1 = 0;

      for (int var2 = 0; var2 < 36; var2++) {
         if (var0.player.getInventory().getItem(var2).isEmpty()) {
            var1++;
         }
      }

      return var1;
   }

   private static int l(Minecraft var0) {
      int var1 = 0;

      for (int var2 = 0; var2 < 9; var2++) {
         if (var0.player.getInventory().getItem(var2).isEmpty()) {
            var1++;
         }
      }

      return var1;
   }

   private vk.q u(vk.q var1, String var2) {
      this.l = var1;
      this.G = var2 == null ? "" : var2;
      Minecraft var3 = Minecraft.getInstance();
      if (this.b && var3.player != null && var3.player.containerMenu != var3.player.inventoryMenu && var3.player.containerMenu.getCarried().isEmpty()) {
         var3.player.closeContainer();
      }

      this.b = (boolean)0;
      _q.z(this.J, "Buying {} ended: {}{}; bought {} for {}", this.f(), var1, this.G.isEmpty() ? "" : " (" + this.G + ")", this.U, pc.b(this.g));
      return this.l;
   }

   private enum a {
      A,
      X,
      L,
      W,
      T;
   }

   public enum q {
      O,
      M,
      S,
      w;
   }

   record x(int slot, int count, long price, int page) {
   }
}
