package com.larpclient;

import com.larp.obf.qa300483f54d804a3.q4a40bbd05996d213.q5ec041da2a8ccb33;
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

public final class t6 {
   static final int z = 100;
   static final int F = 3;
   static final int X = 60;
   static final int k = 100;
   static final int j = 12;
   static final int J = 5;
   static final int p = 192;
   static final int I = 16;
   static final int H = 6;
   static final int G = 3;
   private static final List<String> u = new ArrayList<>();
   private final String e;
   private t6.y x = t6.y.a;
   private t6.h O;
   private je.w Z;
   private int d;
   private int y;
   private int q;
   private int K;
   private int D;
   private int B;
   private int E;
   private int l;
   private int U;
   private int w;
   private long b;
   private long N;
   private long A;
   private int L;
   private int R;
   private int c;
   private t6.z C;
   private final Set<String> i;
   private String a;
   private boolean P;
   private boolean m;
   private Item Q;
   private String M;
   private long n;
   private boolean S;
   private boolean t;
   private final List<t6.z> V;
   private final List<t6.z> v;
   private int T;
   private int r;
   private boolean s;
   private long f;

   t6(String var1) {
      this.O = t6.h.O;
      this.l = -1000;
      this.L = -1;
      this.R = -1;
      this.i = new HashSet<>();
      this.a = "";
      this.M = "";
      this.n = -1L;
      this.V = new ArrayList<>();
      this.v = new ArrayList<>();
      this.f = -1L;
      this.e = var1;
   }

   public static void g(Component var0) {
      if (var0 != null) {
         synchronized (u) {
            if (u.size() < 64) {
               u.add(var0.getString());
            }
         }
      }
   }

   public void P(je.w var1, int var2, long var3, long var5) {
      this.Q = null;
      this.M = "";
      this.n = -1L;
      this.S = (boolean)0;
      this.f(var1, var2, var3, var5);
   }

   public void Q(Item var1, int var2, long var3, long var5, long var7) {
      this.Q = var1;
      this.M = new ItemStack(var1).getHoverName().getString();
      this.n = var5;
      this.S = (boolean)1;
      this.f(null, var2, var3 * 64L, var7);
   }

   private void f(je.w var1, int var2, long var3, long var5) {
      this.Z = var1;
      this.d = var2;
      this.b = var3;
      this.N = var5;
      this.y = this.q = this.K = this.w = 0;
      this.A = 0L;
      this.i.clear();
      this.C = null;
      this.a = "";
      this.L = this.R = -1;
      this.m = (boolean)0;
      this.P = (boolean)0;
      this.t = (boolean)(this.Q != null ? 1 : 0);
      this.s = (boolean)0;
      this.V.clear();
      this.v.clear();
      this.T = this.r = 0;
      this.f = -1L;
      synchronized (u) {
         u.clear();
      }

      this.x = t6.y.b;
      this.m(t6.h.O);
      if (this.Q != null) {
         com.larpclient.h.A(
            this.e,
            "Buying {} {} from /ah: at most {} a block, {} a listing, budget {}",
            var2,
            this.q(),
            je.z(var3 / 64L),
            this.n < 0L ? "no cap" : je.z(this.n),
            je.z(var5)
         );
      } else {
         com.larpclient.h.A(this.e, "Buying {} {} from /ah: at most {} per 64, budget {}", var2, this.q(), je.z(var3), je.z(var5));
      }
   }

   public t6.y g() {
      return this.x;
   }

   public int r() {
      return this.y;
   }

   public long j() {
      return this.A;
   }

   public String u() {
      return this.a;
   }

   public boolean o() {
      return this.P;
   }

   public boolean G() {
      return this.x == t6.y.b;
   }

   public long Q() {
      return this.f;
   }

   public void Q(String var1) {
      if (this.x == t6.y.b) {
         this.H(t6.y.g, var1);
      }
   }

   private void m(t6.h var1) {
      this.O = var1;
      this.D = 0;
      this.B = 0;
      this.E = 0;
   }

   private String q() {
      return this.Q != null ? this.M : (this.Z == je.w.W ? "Flight Duration 3 rockets" : "XP bottles");
   }

   private String V() {
      return this.Q != null ? je.R(this.M) : je.q(this.Z);
   }

   public t6.y u() {
      if (this.x != t6.y.b) {
         return this.x;
      }

      Minecraft var1 = Minecraft.getInstance();
      if (var1.player != null && var1.gameMode != null && var1.getConnection() != null) {
         this.U++;
         this.D++;
         if (!var1.player.containerMenu.getCarried().isEmpty() && this.O != t6.h.f && this.O != t6.h.h && this.U - this.l > 40) {
            return this.H(t6.y.g, "the cursor holds an item");
         }

         Screen var2 = c6.D();
         String var3 = var2 == null ? "" : var2.getTitle().getString();
         AbstractContainerMenu var4 = var1.player.containerMenu;
         int var5 = var4 instanceof ChestMenu var6 && var6.getRowCount() == 6 && je.w(var3) > 0 ? 1 : 0;
         switch (this.O) {
            case O:
               this.j(var1, var2 != null, (boolean)var5);
               break;
            case S:
            case t:
               if (var5 == 0) {
                  if (var2 != null && !this.m) {
                     return this.H(t6.y.g, "another menu opened: " + var3);
                  }

                  if (var2 == null && this.D > 50) {
                     this.m(t6.h.O);
                     return this.x;
                  }

                  if (var2 != null) {
                     return this.H(t6.y.g, "an unexpected menu opened: " + var3);
                  }

                  return this.x;
               }

               if (!this.p(var4)) {
                  return this.x;
               }

               int var8 = je.w(var3);
               if (this.O == t6.h.t && var8 <= this.R) {
                  if (this.D > 100) {
                     return this.H(t6.y.g, "the next page did not open");
                  }

                  return this.x;
               }

               this.L = var4.containerId;
               this.R = var8;
               if (this.O == t6.h.t) {
                  this.m(t6.h.S);
               }

               this.z(var1, (ChestMenu)var4, var8);
               break;
            case f:
               if (var2 instanceof DialogScreen var7) {
                  this.m(var1, var7, var3);
               } else {
                  this.p(var1, var2 != null, (boolean)var5, var3, var4);
               }
               break;
            case h:
               this.L(var1, (boolean)var5, var2 != null, var3);
         }

         return this.x;
      } else {
         return this.H(t6.y.g, "the world went away");
      }
   }

   private void j(Minecraft var1, boolean var2, boolean var3) {
      if (var3 && this.m && !this.s) {
         this.m(t6.h.S);
      } else if (this.D == 1) {
         this.s = (boolean)0;
         if (var2) {
            var1.player.closeContainer();
         }

         if (++this.q > 12 + this.w) {
            this.H(t6.y.g, "searched /ah " + this.q + " times without finishing");
         } else {
            this.K = 0;
            var1.player.connection.sendCommand(this.V());
            this.m = (boolean)1;
            com.larpclient.h.U(this.e, "Sent /{}", this.V());
         }
      } else if (var3) {
         this.m(t6.h.S);
      } else {
         if (this.D > 100) {
            this.H(t6.y.g, "/ah did not open");
         }
      }
   }

   private boolean p(AbstractContainerMenu var1) {
      int var2 = var1.containerId * 31 + var1.getStateId();

      for (int var3 = 0; var3 < 54 && var3 < var1.slots.size(); var3++) {
         ItemStack var4 = var1.getSlot(var3).getItem();
         var2 = 31 * var2 + ItemStack.hashItemAndComponents(var4) * 7 + var4.getCount();
      }

      if (var2 != this.E) {
         this.E = var2;
         this.B = 0;
         return false;
      } else {
         return ++this.B >= 3;
      }
   }

   private void z(Minecraft var1, ChestMenu var2, int var3) {
      if (this.y >= this.d) {
         this.H(t6.y.M, "");
      } else if (this.w >= 192) {
         this.H(t6.y.M, "at the purchase limit for one stop");
      } else if (this.U - this.l >= 6) {
         if (this.Q != null) {
            this.Y(var1, var2, var3);
         } else if (R(var1) == 0) {
            this.H(this.y > 0 ? t6.y.M : t6.y.g, "no free hotbar slot for more " + this.q());
         } else {
            t6.z var4 = null;
            long var5 = Long.MAX_VALUE;

            for (int var7 = 0; var7 < 45; var7++) {
               t6.z var8 = this.k(var2, var7, var3);
               if (var8 != null
                  && !this.i.contains(K(var8))
                  && je.g(var8.price(), var8.count(), this.b)
                  && (this.N < 0L || this.A + var8.price() <= this.N)
                  && var8.count() <= this.d - this.y + 63) {
                  long var9 = var8.price() * 64L / var8.count();
                  if (var9 < var5) {
                     var5 = var9;
                     var4 = var8;
                  }
               }
            }

            if (var4 != null) {
               this.Q(var1, var2, var4, var3);
            } else {
               int var11 = this.C(var2, je.v.Q);
               if (var11 >= 0 && ++this.K <= 5) {
                  this.q(var1, var2, var11);
                  this.m(t6.h.t);
               } else {
                  this.H(
                     this.y > 0 ? t6.y.M : t6.y.g,
                     this.N >= 0L && this.A > 0L
                        ? "no listing fits the price limit and the budget left"
                        : "no listing under the price limit (" + je.z(this.b) + " per 64)"
                  );
               }
            }
         }
      }
   }

   private void Y(Minecraft var1, ChestMenu var2, int var3) {
      int var4 = this.G(var1);
      int var5 = Math.min(this.d - this.y, var4);
      if (var5 <= 0) {
         this.H(this.y > 0 ? t6.y.M : t6.y.g, "no free inventory room for more " + this.q());
      } else {
         int var6 = this.C(var2, je.v.Q);
         if (this.t) {
            long var17 = 0L;

            for (int var18 = 0; var18 < 45; var18++) {
               ItemStack var10 = var2.getSlot(var18).getItem();
               if (!var10.isEmpty()) {
                  var17 = Math.max(var17, je.M(G(var10)));
               }

               t6.z var11 = this.k(var2, var18, var3);
               if (var11 != null) {
                  long var12 = (var11.price() + var11.count() - 1L) / var11.count();
                  if (this.f < 0L || var12 < this.f) {
                     this.f = var12;
                  }

                  if (je.g(var11.price(), var11.count(), this.b) && (this.n < 0L || var11.price() <= this.n) && !this.V.contains(var11)) {
                     this.V.add(var11);
                  }
               }
            }

            int var19 = new ItemStack(this.Q).getMaxStackSize();
            long var20 = 0L;

            for (t6.z var13 : this.V) {
               if (var17 > 0L && var13.price() * var19 <= var17 * var13.count()) {
                  var20 += var13.count();
               }
            }

            if (var20 < var5 && var6 >= 0 && this.K < 16) {
               this.K++;
               this.q(var1, var2, var6);
               this.m(t6.h.t);
            } else {
               this.t = (boolean)0;
               this.r++;
               this.v.clear();
               this.v.addAll(t(this.V, var5, var4, this.N < 0L ? -1L : this.N - this.A));
               if (this.v.isEmpty()) {
                  this.H(this.y > 0 ? t6.y.M : t6.y.g, this.g());
               } else {
                  this.T = 0;
                  long var22 = 0L;
                  int var14 = 0;

                  for (t6.z var16 : this.v) {
                     this.T = Math.max(this.T, var16.page());
                     var22 += var16.price();
                     var14 += var16.count();
                  }

                  com.larpclient.h.A(
                     this.e,
                     "Read {} pages of /ah for {}: {} listings under the caps, cheapest {} each; plan {} for {} in {} listings ({} each){}",
                     var3,
                     this.q(),
                     this.V.size(),
                     this.f < 0L ? "none" : je.z(this.f),
                     var14,
                     je.z(var22),
                     this.v.size(),
                     je.z((var22 + var14 - 1L) / var14),
                     var14 < var5 ? "; " + var5 + " were needed, the budget covers no more" : ""
                  );
                  this.s = (boolean)1;
                  this.m(t6.h.O);
               }
            }
         } else {
            for (int var7 = 0; var7 < 45; var7++) {
               t6.z var8 = this.k(var2, var7, var3);
               if (var8 != null && !this.i.contains(K(var8))) {
                  int var9 = this.J(var8);
                  if (var9 >= 0 && (this.N < 0L || this.A + var8.price() <= this.N)) {
                     this.v.remove(var9);
                     this.Q(var1, var2, var8, var3);
                     return;
                  }
               }
            }

            if (!this.v.isEmpty() && var6 >= 0 && var3 < this.T + 1 && this.K < 16) {
               this.K++;
               this.q(var1, var2, var6);
               this.m(t6.h.t);
            } else if (this.r < 3) {
               com.larpclient.h.A(this.e, "{} planned listings of {} were gone; reading /ah again for the {} still needed", this.v.size(), this.q(), var5);
               this.t = (boolean)1;
               this.V.clear();
               this.v.clear();
               this.s = (boolean)1;
               this.m(t6.h.O);
            } else {
               this.H(this.y > 0 ? t6.y.M : t6.y.g, "the listings kept changing; bought " + this.y + " of " + this.d);
            }
         }
      }
   }

   private String g() {
      return this.N >= 0L && this.A > 0L
         ? "no listing fits the price limit and the budget left"
         : "no listing under "
            + je.z(this.b / 64L)
            + " per block"
            + (this.n >= 0L ? " and " + je.z(this.n) + " per listing" : "")
            + (this.f >= 0L ? " (the cheapest was " + je.z(this.f) + " each)" : "");
   }

   private int J(t6.z var1) {
      for (int var2 = 0; var2 < this.v.size(); var2++) {
         if (this.v.get(var2).count() == var1.count() && this.v.get(var2).price() == var1.price()) {
            return var2;
         }
      }

      return -1;
   }

   private void Q(Minecraft var1, ChestMenu var2, t6.z var3, int var4) {
      this.C = var3;
      this.c = this.v(var1);
      this.q(var1, var2, var3.slot());
      com.larpclient.h.A(this.e, "Buying {} {} for {} (page {}, slot {})", var3.count(), this.q(), je.z(var3.price()), var4, var3.slot());
      this.m(t6.h.f);
   }

   static List<t6.z> t(List<t6.z> var0, int var1, int var2, long var3) {
      var1 = Math.min(var1, var2);
      if (var1 > 0 && !var0.isEmpty()) {
         long var5 = 2305843009213693951L;
         int var7 = var0.size();
         long[] var8 = new long[var2 + 1];
         Arrays.fill(var8, 2305843009213693951L);
         var8[0] = 0L;
         boolean[][] var9 = new boolean[var7][var2 + 1];

         for (int var10 = 0; var10 < var7; var10++) {
            int var11 = ((t6.z)var0.get(var10)).count();
            long var12 = ((t6.z)var0.get(var10)).price();
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
               var21.add((t6.z)var0.get(var22));
               var13 -= ((t6.z)var0.get(var22)).count();
            }
         }

         var21.sort(Comparator.comparingInt(t6.z::page).thenComparingInt(t6.z::slot));
         return var21;
      } else {
         return List.of();
      }
   }

   private int G(Minecraft var1) {
      int var2 = new ItemStack(this.Q).getMaxStackSize();
      int var3 = 0;
      Inventory var4 = var1.player.getInventory();

      for (int var5 = 0; var5 < 36; var5++) {
         ItemStack var6 = var4.getItem(var5);
         if (var6.isEmpty()) {
            var3 += var2;
         } else if (this.Q(var6) && var6.getComponentsPatch().isEmpty()) {
            var3 += Math.max(0, var2 - var6.getCount());
         }
      }

      return var3;
   }

   t6.z k(ChestMenu var1, int var2, int var3) {
      ItemStack var4 = var1.getSlot(var2).getItem();
      if (!this.Q(var4)) {
         return null;
      }

      long var5 = je.M(G(var4));
      return var5 <= 0L ? null : new t6.z(var2, var4.getCount(), var5, var3);
   }

   private boolean Q(ItemStack var1) {
      if (var1.isEmpty()) {
         return false;
      } else if (this.Q != null) {
         ItemContainerContents var2 = (ItemContainerContents)var1.get(DataComponents.CONTAINER);
         return var1.is(this.Q) && (var2 == null || var2.nonEmptyItemCopyStream().findAny().isEmpty());
      } else {
         return this.Z != je.w.W ? var1.is(Items.EXPERIENCE_BOTTLE) : var1.is(Items.FIREWORK_ROCKET) && cu.g(var1) && cu.Y(var1) == 3;
      }
   }

   private static List<String> G(ItemStack var0) {
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

   private static String K(t6.z var0) {
      return var0.page() + ":" + var0.slot() + ":" + var0.count() + ":" + var0.price();
   }

   private int C(ChestMenu var1, je.v var2) {
      int var3 = -1;

      for (int var4 = 45; var4 < 54; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (!var5.isEmpty() && je.d(BuiltInRegistries.ITEM.getKey(var5.getItem()).toString(), var5.getHoverName().getString()) == var2) {
            if (var3 >= 0) {
               return -1;
            }

            var3 = var4;
         }
      }

      return var3;
   }

   private void p(Minecraft var1, boolean var2, boolean var3, String var4, AbstractContainerMenu var5) {
      if (!this.d()) {
         if (this.v(var1) > this.c) {
            this.m(t6.h.h);
         } else if (var3) {
            if (this.D > 60) {
               this.i.add(K(this.C));
               com.larpclient.h.A(this.e, "No confirm screen for that listing; trying another");
               this.m(t6.h.S);
            }
         } else if (!var2) {
            if (this.D > 60) {
               this.H(t6.y.g, "the confirm screen never opened");
            }
         } else if (!this.p(var5)) {
            if (this.D > 60) {
               this.H(t6.y.g, "the confirm screen kept changing");
            }
         } else if (this.U - this.l >= 6) {
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
                     if (je.w(var13).contains("sell")) {
                        this.H(t6.y.g, "the confirm screen has a selling button: " + var4);
                        return;
                     }

                     if (this.Q(var12)) {
                        long var15 = je.M(G(var12));
                        if (var12.getCount() != this.C.count() || var15 >= 0L && var15 != this.C.price()) {
                           this.H(t6.y.g, "the confirm screen shows " + var12.getCount() + " for " + je.z(var15) + ", not the chosen listing");
                           return;
                        }

                        var8 = 1;
                     } else {
                        je.k var17 = je.Y(var14, var13);
                        if (var17 == je.k.h) {
                           var7++;
                        }

                        if (var17 == je.k.z) {
                           String var16 = var14 + "|" + je.w(var13);
                           if (var9 != null && !var9.equals(var16)) {
                              this.H(t6.y.g, "the confirm screen has two different confirm buttons: " + var4);
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
               this.q(var1, var5, var6);
               com.larpclient.h.U(this.e, "Confirmed on '{}' slot {}", var4, var6);
               this.m(t6.h.h);
            } else {
               com.larpclient.h.A(
                  this.e,
                  "Unrecognized confirm screen '{}': listing shown {}, confirm slot {}, cancel buttons {}",
                  var4,
                  Boolean.valueOf((boolean)var8),
                  var6,
                  var7
               );
               this.H(t6.y.g, "the confirm screen was not recognized (" + var4 + ")");
            }
         }
      }
   }

   private void m(Minecraft var1, DialogScreen<?> var2, String var3) {
      if (!this.d()) {
         if (this.U - this.l >= 6) {
            Dialog var4 = ((q5ec041da2a8ccb33)var2).larp$dialog();
            ActionButton var5 = null;
            if (var4 instanceof ConfirmationDialog var6) {
               if (je.m(var6.yesButton().button().label().getString())) {
                  var5 = var6.yesButton();
               }
            } else if (var4 instanceof MultiActionDialog var7 && var7.actions().size() == 2) {
               String var8 = ((ActionButton)var7.actions().get(0)).button().label().getString();
               String var9 = ((ActionButton)var7.actions().get(1)).button().label().getString();
               if (je.m(var8) && je.a(var9)) {
                  var5 = (ActionButton)var7.actions().get(0);
               } else if (je.a(var8) && je.m(var9)) {
                  var5 = (ActionButton)var7.actions().get(1);
               }
            }

            if (var4 != null && var5 != null) {
               CommonDialogData var17 = var4.common();
               String var18 = je.w(var17.title().getString());
               if (!je.q(var18)) {
                  this.H(t6.y.g, "an unexpected dialog opened: " + var17.title().getString());
               } else if (!var17.inputs().isEmpty()) {
                  this.H(t6.y.g, "the purchase dialog asks for input");
               } else {
                  int var19 = 0;
                  long var20 = -1L;

                  for (DialogBody var12 : var17.body()) {
                     if (var12 instanceof ItemBody var13) {
                        ItemStack var21 = var13.item().create();
                        if (!this.Q(var21) || var21.getCount() != this.C.count()) {
                           this.H(t6.y.g, "the purchase dialog shows " + var21.getCount() + " " + var21.getHoverName().getString() + ", not the chosen listing");
                           return;
                        }

                        var19++;
                        if (var13.description().isPresent()) {
                           var20 = Math.max(var20, je.M(List.of(((PlainMessage)var13.description().get()).contents().getString())));
                        }
                     } else if (var12 instanceof PlainMessage var14) {
                        long var15 = je.M(List.of(var14.contents().getString()));
                        if (var15 >= 0L) {
                           var20 = var15;
                        }
                     }
                  }

                  if (var19 == 1 && var20 == this.C.price()) {
                     var2.runAction(var5.action().flatMap(var0 -> var0.createAction(Map.of())));
                     this.l = this.U;
                     com.larpclient.h.A(
                        this.e, "Answered Yes to '{}' for {} {} at {}", var17.title().getString(), this.C.count(), this.q(), je.z(this.C.price())
                     );
                     this.m(t6.h.h);
                  } else {
                     com.larpclient.h.A(
                        this.e, "Purchase dialog '{}' did not match: items {}, price {} for listing {}", var17.title().getString(), var19, var20, this.C
                     );
                     this.H(t6.y.g, "the purchase dialog did not show the chosen listing and its price");
                  }
               }
            } else {
               com.larpclient.h.A(this.e, "Unrecognized dialog '{}': {}", var3, var4 == null ? "none" : var4.getClass().getSimpleName() + " " + var4);
               this.H(t6.y.g, "an unexpected dialog opened: " + var3);
            }
         }
      }
   }

   private void L(Minecraft var1, boolean var2, boolean var3, String var4) {
      if (!this.d()) {
         int var5 = this.v(var1);
         if (var5 >= this.c + this.C.count() || var5 > this.c && this.D > 20) {
            int var6 = var5 - this.c;
            this.y += var6;
            this.w++;
            this.A = this.A + this.C.price();
            this.i.add(K(this.C));
            com.larpclient.h.A(this.e, "Bought {} {} for {}; {} of {} wanted, {} spent", var6, this.q(), je.z(this.C.price()), this.y, this.d, je.z(this.A));
            this.C = null;
            if (this.y >= this.d) {
               this.H(t6.y.M, "");
            } else {
               this.m(var2 ? t6.h.S : t6.h.O);
            }
         } else if (var3 && !var2 && this.D > 10 && !je.w(var4).isEmpty()) {
            if (this.D > 100) {
               this.H(t6.y.g, "the purchase was not confirmed (still on " + var4 + ")");
            }
         } else {
            if (this.D > 100) {
               this.H(t6.y.g, "the items never arrived after confirming");
            }
         }
      }
   }

   private boolean d() {
      List var1;
      synchronized (u) {
         var1 = List.copyOf(u);
         u.clear();
      }

      for (String var3 : var1) {
         je.d var4 = je.I(var3);
         if (var4 != je.d.d) {
            com.larpclient.h.A(this.e, "Auction reply ({}): {}", var4, var3);
            switch (var4) {
               case K:
                  this.i.add(K(this.C));
                  this.C = null;
                  this.m(t6.h.O);
                  return true;
               case Y:
                  this.P = (boolean)1;
                  this.H(this.y > 0 ? t6.y.M : t6.y.g, "not enough money");
                  return true;
               case z:
                  this.H(this.y > 0 ? t6.y.M : t6.y.g, "the inventory is full");
                  return true;
               case I:
                  long var5 = je.A(var3);
                  if (this.C != null && var5 > this.C.price()) {
                     this.H(t6.y.g, "the receipt says " + je.z(var5) + ", more than the listing's " + je.z(this.C.price()));
                     return true;
                  }
            }
         }
      }

      return false;
   }

   private void q(Minecraft var1, AbstractContainerMenu var2, int var3) {
      ItemStack var4 = var2.getSlot(var3).getItem();
      if (je.w(var4.getHoverName().getString()).contains("sell")) {
         this.H(t6.y.g, "refused to click a selling button");
      } else {
         var1.gameMode.handleContainerInput(var2.containerId, var3, 0, ContainerInput.PICKUP, var1.player);
         this.l = this.U;
      }
   }

   private int v(Minecraft var1) {
      int var2 = 0;
      Inventory var3 = var1.player.getInventory();

      for (int var4 = 0; var4 < 36; var4++) {
         if (this.Q(var3.getItem(var4))) {
            var2 += var3.getItem(var4).getCount();
         }
      }

      if (this.Q(var1.player.getOffhandItem())) {
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

   private static int R(Minecraft var0) {
      int var1 = 0;

      for (int var2 = 0; var2 < 9; var2++) {
         if (var0.player.getInventory().getItem(var2).isEmpty()) {
            var1++;
         }
      }

      return var1;
   }

   private t6.y H(t6.y var1, String var2) {
      this.x = var1;
      this.a = var2 == null ? "" : var2;
      Minecraft var3 = Minecraft.getInstance();
      if (this.m && var3.player != null && var3.player.containerMenu != var3.player.inventoryMenu && var3.player.containerMenu.getCarried().isEmpty()) {
         var3.player.closeContainer();
      }

      this.m = (boolean)0;
      com.larpclient.h.A(this.e, "Buying {} ended: {}{}; bought {} for {}", this.q(), var1, this.a.isEmpty() ? "" : " (" + this.a + ")", this.y, je.z(this.A));
      return this.x;
   }

   private enum h {
      O,
      S,
      t,
      f,
      h;
   }

   public enum y {
      a,
      b,
      M,
      g;
   }

   record z(int slot, int count, long price, int page) {
   }
}
