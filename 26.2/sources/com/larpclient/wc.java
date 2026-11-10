package com.larpclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.DispenserScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.BlockHitResult;

@Environment(EnvType.CLIENT)
public final class wc extends we {
   private static final wc lZ = new wc();
   private final Map<Integer, Integer> l3 = new HashMap<>();
   private final Map<Integer, ItemStack> l2 = new HashMap<>();
   private ItemStack[] ls;
   private Map<Integer, Integer> lN;
   private wc.n l_ = wc.n.H;
   private wc.n lp = wc.n.H;
   private int ll = -1;
   private int ld = -1;
   private int lG = -1;
   private ItemStack lo = ItemStack.EMPTY;
   private int lr = -1;
   private int lw;
   private int lP;
   private boolean li;
   private String lB;
   private BlockPos l5;
   private int lk = -1;
   private ClientLevel lW;
   private long lM = -1L;
   private wc.t lA;
   private int ln = -1;
   private ItemStack lf;
   private boolean lx;
   private long lc;
   private boolean lh;
   private boolean lj;
   private long l0;
   private String lF = "no map change";

   private wc() {
   }

   public static wc s() {
      return lZ;
   }

   public wc.n O() {
      return this.l_;
   }

   public String J() {
      return this.l_.I();
   }

   public int o() {
      return this.ll;
   }

   public int p() {
      return this.lr;
   }

   public boolean Q() {
      return this.li;
   }

   public int I(int var1) {
      return this.l3.getOrDefault(this.getTemplateIdForValue(var1), 0);
   }

   public void d(String var1) {
      this.l_ = "higher".equalsIgnoreCase(var1) ? wc.n.s : ("lower".equalsIgnoreCase(var1) ? wc.n.F : wc.n.H);
      if (this.l_ == wc.n.H) {
         this.Z();
      }
   }

   public void W() {
      this.d(switch (this.l_) {
         case H -> "higher";
         case s -> "lower";
         case F -> "off";
      });
   }

   public void Z() {
      this.clearQueue();
      this.ll = this.ld = this.lG = -1;
      this.lo = ItemStack.EMPTY;
      this.lN = null;
      this.lP = 0;
   }

   @Override
   public void clearQueue() {
      this.li = (boolean)0;
      this.lr = -1;
      if (this.lA != null) {
         this.abortSwap();
      } else {
         this.l3.clear();
      }
   }

   @Override
   protected boolean supportsPhysicalSwap() {
      return false;
   }

   @Override
   protected BlockPos getAssignedDispenserPosition(Minecraft var1) {
      gj var2 = gj.c();
      return var2.S(gj.d.Z).contains(this.z()) ? var2.n(this.z()) : null;
   }

   @Override
   public boolean hasQueuedForce() {
      return this.li && this.lr > 0;
   }

   @Override
   public int getPendingValue() {
      return this.hasQueuedForce() ? this.lr : -1;
   }

   @Override
   public int getQueuedValueAt(int var1) {
      return var1 == 0 ? this.getPendingValue() : -1;
   }

   @Override
   public String pendingLabel() {
      return this.getPendingValue() < 1 ? "none" : String.valueOf(this.getPendingValue());
   }

   @Override
   public int containerIndexForValue(int var1) {
      return var1 >= 1 && var1 <= 9 ? var1 - 1 : -1;
   }

   @Override
   public void setContainerContext(String var1) {
      if (!Objects.equals(var1, this.z())) {
         if (this.z() != null) {
            this.Z();
         }

         super.setContainerContext(var1);
      }
   }

   @Override
   public boolean scanCurrentContainerTemplateSlots(Minecraft var1) {
      boolean var2 = super.scanCurrentContainerTemplateSlots(var1);
      if (var1 != null && var1.player != null && var1.player.containerMenu instanceof DispenserMenu var3) {
         for (int var7 = 0; var7 < 9; var7++) {
            ItemStack var5 = var3.getSlot(var7).getItem();
            int var6 = this.h(var5);
            if (var6 > 0) {
               this.l2.put(var6, var5.copyWithCount(1));
            }
         }
      }

      return var2;
   }

   public void N(Minecraft var1) {
      this.t(var1);
      if (var1 != null && var1.player != null && k6.d().Q && this.l_ != wc.n.H && !this.li && !this.isSwapBusy()) {
         we var2 = we.Y();
         if (var2 == this || !var2.isSwapBusy()) {
            this.ls = n(var1);
            if (this.ll > 0) {
               int var3 = this.ld >= 0 && this.ld < this.ls.length && !this.lo.isEmpty() && I(this.lo, this.ls[this.ld]) ? this.ld : -1;
               if (var3 < 0) {
                  for (int var4 = 0; var4 < this.ls.length; var4++) {
                     if (!this.lo.isEmpty() && I(this.lo, this.ls[var4])) {
                        if (var3 >= 0) {
                           this.clearQueue();
                           te.Z("HigherLower", "Activation disarmed: moved reference stack is ambiguous");
                           return;
                        }

                        var3 = var4;
                     }
                  }
               }

               if (var3 < 0) {
                  this.Z();
                  te.Z("HigherLower", "Previous reference removed; waiting for first card of new round");
               } else {
                  this.ld = var3;
               }
            }

            this.l3.clear();
            this.l3.putAll(a(this.ls));
            if (this.ll < 1 && this.lN == null) {
               this.lN = i(var1);
            }

            this.lG = this.ll;
            this.lp = this.l_;
            this.lB = this.z();
            BlockPos var5 = this.getAssignedDispenserPosition(var1);
            this.l5 = var5 == null ? null : var5.immutable();
            this.lr = this.i();
            this.lk = this.v(this.lr);
            this.li = (boolean)1;
            this.lw = 0;
            this.lF = "no map change";
            te.R("HigherLower Activation target=" + this.lp.I() + " reference=" + this.lG + " recommendation=" + this.lr);
         }
      }
   }

   @Override
   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      this.t(Minecraft.getInstance());
   }

   private void t(Minecraft var1) {
      if (this.li && this.lA == null && var1 != null && var1.player != null) {
         ItemStack[] var2 = n(var1);
         Map var3 = a(var2);
         int var4 = -1;
         int var5 = -1;

         for (int var6 = 0; var6 < var2.length; var6++) {
            if (!R(var2[var6], this.ls[var6])) {
               ItemStack var7 = this.ls[var6];
               ItemStack var8 = var2[var6];
               int var9 = this.h(var8);
               if (var4 >= 0 || var9 < 1 || !var7.isEmpty() && !I(var7, var8) || var8.getCount() != var7.getCount() + 1) {
                  this.lF = "slot="
                     + var6
                     + " reason="
                     + (
                        var4 >= 0
                           ? "multiple map changes"
                           : (var9 < 1 ? "unrecognized or removed map" : (!var7.isEmpty() && !I(var7, var8) ? "origin/components changed" : "not +1"))
                     )
                     + " before="
                     + g(var7)
                     + " after="
                     + g(var8);
                  return;
               }

               var4 = var6;
               var5 = var9;
            }
         }

         if (var4 >= 0 && var3.getOrDefault(M(var2[var4]), 0) == this.l3.getOrDefault(M(var2[var4]), 0) + 1) {
            int var12 = this.lp == wc.n.s ? (var5 > this.lG ? 1 : 0) : (var5 < this.lG ? 1 : 0);
            if (this.lG < 1 || this.lr < 1 || var12 != 0 || var5 == this.lr) {
               this.u(var5, var4);
            } else if (mg.S(var1) && mg.O(this, var4, this.lr)) {
               int var14 = this.lr;
               this.L(var14, var4, var5, true);
            } else {
               int var13 = -1;
               int var15 = this.lr == this.lG ? 1 : 0;
               if (var15 != 0 && this.ld >= 0 && this.h(this.ls[this.ld]) == this.lr) {
                  var13 = this.ld;
               } else if (var15 == 0) {
                  int var16 = this.ld >= 0 && this.ld < 9 ? this.ld : var1.player.getInventory().getSelectedSlot();

                  for (int var10 = 1; var10 <= 9; var10++) {
                     int var11 = (var16 + var10) % 9;
                     if (this.ls[var11].isEmpty() && (var2[var11].isEmpty() || var11 == var4)) {
                        var13 = var11;
                        break;
                     }
                  }
               }

               ItemStack var17 = this.l2.get(this.lr);
               if (var15 != 0 && var13 >= 0) {
                  var17 = this.ls[var13].copyWithCount(1);
               }

               if (var17 != null
                  && M(var17) == this.getTemplateIdForValue(this.lr)
                  && var13 >= 0
                  && this.l5 != null
                  && (var15 == 0 || I(this.ls[var13], var17) && this.ls[var13].getCount() < var17.getMaxStackSize())) {
                  wc.t var18 = new wc.t(this.lB, this.l5, this.lp, this.lG, this.lr, var5, var4, var13, this.ls, var2[var4], this.l3);
                  var18.h = this.lk;
                  var18.i.merge(this.getTemplateIdForValue(var18.e), 1, Integer::sum);
                  var18.K.put(var4, var18.Z[var4].copy());
                  var18.K.put(var13, var17.copyWithCount(var18.Z[var13].getCount() + 1));
                  this.lA = var18;
                  var18.r = 1;
                  this.li = (boolean)0;
                  this.A(var1);
                  te.Z(
                     "HigherLower",
                     "One-card exchange real="
                        + var5
                        + " ->"
                        + var18.e
                        + " target="
                        + var18.n.I()
                        + " reference="
                        + var18.F
                        + " arrival="
                        + var4
                        + " destination="
                        + var13
                  );
                  this.H();
               } else {
                  te.Z("HigherLower", "Exchange unavailable: destination, components, capacity or assignment");
                  this.u(var5, var4);
               }
            }
         } else {
            this.lF = "reason=no +1 map total arrival=" + var4;
         }
      }
   }

   private void u(int var1, int var2) {
      this.L(var1, var2, var1, false);
   }

   private void L(int var1, int var2, int var3, boolean var4) {
      if (this.lA == null) {
         te.Z("HigherLower", te.I(Integer.toString(var3), Integer.toString(var1), this.lG > 0 && this.lr > 0, var4));
      }

      this.ll = var1;
      this.ld = var2;
      this.lo = Minecraft.getInstance().player.getInventory().getItem(var2).copyWithCount(1);
      this.li = (boolean)0;
      this.lr = -1;
      this.l3.clear();
   }

   @Override
   public void tick(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         if (this.lW != null && this.lW != var1.level) {
            this.lA = null;
            this.ln = -1;
            this.Z();
         }

         if (this.lW != var1.level || this.lM != var1.level.getGameTime()) {
            this.lW = var1.level;
            this.lM = var1.level.getGameTime();
            if (this.lA != null) {
               this.lA.r++;
               this.v(var1);
            } else if (k6.d().Q) {
               this.t(var1);
               if (this.li && ++this.lw > 100) {
                  te.Z("HigherLower", "Draw timed out: " + this.lF + " baselineMapCounts=" + this.l3 + " receivedMapCounts=" + a(n(var1)));
                  this.clearQueue();
               }

               if (this.lA == null && !this.li && this.ll > 0 && this.lN != null) {
                  Map var2 = i(var1);
                  int var3 = 0;

                  for (Entry var5 : var2.entrySet()) {
                     var3 += Math.max(0, (Integer)var5.getValue() - this.lN.getOrDefault(var5.getKey(), 0));
                  }

                  this.lP = var3 == 0 ? this.lP + 1 : 0;
                  if (this.lP >= 6) {
                     this.Z();
                  }
               }
            }
         }
      } else {
         this.lA = null;
         this.ln = -1;
         this.Z();
         this.lW = null;
      }
   }

   private void v(Minecraft var1) {
      wc.t var2 = this.lA;
      if (!var2.M && var2.L.U(var2.x) || !Objects.equals(var2.G, this.z()) || !var2.E.equals(this.getAssignedDispenserPosition(var1))) {
         this.a("timeout or assignment changed step=" + var2.P + " sync=" + var2.z + " ready=" + var2.O + " " + var2.T.U());
      }

      AbstractContainerMenu var3 = var1.player.containerMenu;
      if (var2.z < 0) {
         if (var2.r >= 100) {
            this.a("dispenser did not open; no cards moved");
         }

         if (var2.C) {
            if (!var3.getCarried().isEmpty()) {
               var2.K.clear();
               this.ln = -1;
            }

            this.S(var1, false);
         } else {
            if (var1.gui.screen() == null && var3 == var1.player.inventoryMenu && var3.getCarried().isEmpty() && var2.T.g(var2.r)) {
               this.k(var1, var2);
            }
         }
      } else if (!(var3 instanceof DispenserMenu) || var3 != var2.t || var3.containerId != var2.z) {
         this.a("handler replaced");
         var2.K.clear();
         this.ln = -1;
         this.S(var1, false);
      } else if (!var2.B) {
         if (var2.O && var1.gameMode != null) {
            if (var2.C || var2.T.S()) {
               if (var2.M) {
                  if (!var2.L
                     .I(
                        var2.y < 0 || com.larpclient.s9.z(var2.S.get(var2.y), var2.D),
                        com.larpclient.s9.z(var2.S.get(var2.o), var2.c),
                        com.larpclient.s9.z(var2.A, var2.J)
                     )) {
                     if (var2.L.E()) {
                        this.a("click not confirmed; no replay " + h(var2));
                        this.y(var1, var3, var2);
                     }

                     return;
                  }

                  te.R("HigherLower Click confirmed " + h(var2));
                  var2.M = (boolean)0;
                  if (!var2.C && var2.P == 3) {
                     var2.K.put(var2.U, var2.S.get(var2.o).copy());
                  }

                  var2.N = 0;
                  if (!var2.C) {
                     var2.P += 3;
                  }
               }

               if (var2.C) {
                  this.o(var1, var3, var2);
               } else {
                  int var4 = D(var3, var1, var2.k);
                  int var5 = D(var3, var1, var2.U);
                  if (var4 >= 0 && var5 >= 0) {
                     if (var2.P == 0) {
                        if (!var3.getCarried().isEmpty()) {
                           this.a("occupied cursor");
                           return;
                        }

                        for (int var6 = 0; var6 < var2.Z.length; var6++) {
                           ItemStack var7 = var6 == var2.k ? var2.V.copyWithCount(var2.Z[var6].getCount() + 1) : var2.Z[var6];
                           ItemStack var8 = var1.player.getInventory().getItem(var6);
                           if (var6 != var2.k && var6 != var2.U ? !R(var8, var7) : !com.larpclient.s9.z(var8, var7)) {
                              this.a("inventory changed before extraction slot=" + var6 + " expected=" + g(var7) + " received=" + g(var8));
                              return;
                           }
                        }

                        if (var2.q < 0) {
                           for (int var11 = 0; var11 < 9; var11++) {
                              if (var11 != var2.h
                                 && ItemStack.isSameItemSameComponents(var3.getSlot(var11).getItem(), var3.getSlot(var4).getItem())
                                 && ItemStack.isSameItemSameComponents(var2.S.get(var11), var2.S.get(var4))
                                 && H(var3.getSlot(var11), var2.V, 1)) {
                                 var2.q = var11;
                                 break;
                              }
                           }

                           for (int var12 = 0; var12 < 9; var12++) {
                              if (var2.q < 0
                                 && var12 != var2.h
                                 && var3.getSlot(var12).getItem().isEmpty()
                                 && var2.S.get(var12).isEmpty()
                                 && H(var3.getSlot(var12), var2.V, 1)) {
                                 var2.q = var12;
                                 break;
                              }
                           }
                        }

                        if (var2.h < 0
                           || var2.h >= 9
                           || var2.q < 0
                           || this.h(var3.getSlot(var2.h).getItem()) != var2.e
                           || !var3.getSlot(var2.h).mayPickup(var1.player)
                           || !var3.getSlot(var5).mayPlace(var3.getSlot(var2.h).getItem())
                           || !var2.Z[var2.U].isEmpty() && !I(var2.Z[var2.U], var3.getSlot(var2.h).getItem())
                           || var2.Z[var2.U].getCount() + 1 > var3.getSlot(var5).getMaxStackSize(var3.getSlot(var2.h).getItem())
                           || !var3.getSlot(var4).mayPickup(var1.player)) {
                           this.a("source, return space, or destination unavailable");
                           return;
                        }

                        ItemStack var13 = var3.getSlot(var2.h).getItem();
                        if (!var2.Z[var2.U].isEmpty()
                           && (
                              !ItemStack.isSameItemSameComponents(var13, var3.getSlot(var5).getItem())
                                 || !ItemStack.isSameItemSameComponents(var2.S.get(var2.h), var2.S.get(var5))
                           )) {
                           this.a("replacement destination has incompatible vanilla components");
                           return;
                        }

                        var2.b = var13.copy();
                        var2.Y = var3.getSlot(var2.q).getItem().copy();
                        var2.K.put(var2.U, var13.copyWithCount(var2.Z[var2.U].getCount() + 1));
                        this.l(var1, var3, var4, var2.q);
                     } else if (var2.P == 3) {
                        if (!var3.getCarried().isEmpty()
                           || !com.larpclient.s9.z(var3.getSlot(var2.h).getItem(), var2.b)
                           || !com.larpclient.s9.z(var3.getSlot(var4).getItem(), var2.Z[var2.k])
                           || !com.larpclient.s9.z(var3.getSlot(var5).getItem(), var2.Z[var2.U])
                           || !H(var3.getSlot(var5), var3.getSlot(var2.h).getItem(), 1)) {
                           this.a("replacement source changed");
                           return;
                        }

                        this.l(var1, var3, var2.h, var5);
                     } else {
                        int var14 = var2.A.isEmpty()
                              && var3.getCarried().isEmpty()
                              && var2.i.equals(a(n(var1)))
                              && com.larpclient.s9.z(var2.S.get(var2.h), var2.b.copyWithCount(var2.b.getCount() - 1))
                              && com.larpclient.s9.z(var2.S.get(var2.q), var2.V.copyWithCount(var2.Y.getCount() + 1))
                           ? 1
                           : 0;

                        for (int var15 = 0; var15 < var2.Z.length && var14 != 0; var15++) {
                           int var16 = D(var3, var1, var15);
                           ItemStack var9 = var15 == var2.U ? var2.K.get(var15) : var2.Z[var15];
                           int var10 = var15 != var2.k && var15 != var2.U ? 0 : 1;
                           var14 = var16 < 0
                                 || (
                                    var10 != 0
                                       ? !com.larpclient.s9.z(var2.S.get(var16), var9) || !com.larpclient.s9.z(var1.player.getInventory().getItem(var15), var9)
                                       : !R(var2.S.get(var16), var9) || !R(var1.player.getInventory().getItem(var15), var9)
                                 )
                              ? 0
                              : 1;
                           if (var14 == 0) {
                              this.a(
                                 "final slot="
                                    + var15
                                    + " expected="
                                    + g(var9)
                                    + " server="
                                    + (var16 < 0 ? "missing" : g(var2.S.get(var16)))
                                    + " local="
                                    + g(var1.player.getInventory().getItem(var15))
                              );
                           }
                        }

                        if (var14 == 0) {
                           this.a(
                              "final quantity ledger mismatch expectedCounts="
                                 + var2.i
                                 + " receivedCounts="
                                 + a(n(var1))
                                 + " source="
                                 + g(var2.S.get(var2.h))
                                 + " expectedSource="
                                 + g(var2.b.copyWithCount(var2.b.getCount() - 1))
                                 + " deposit="
                                 + g(var2.S.get(var2.q))
                                 + " expectedDeposit="
                                 + g(var2.V.copyWithCount(var2.Y.getCount() + 1))
                           );
                           return;
                        }

                        this.S(var1, true);
                     }
                  } else {
                     this.a("player slots missing");
                  }
               }
            }
         } else {
            if (var2.C) {
               this.y(var1, var3, var2);
            }
         }
      }
   }

   private void l(Minecraft var1, AbstractContainerMenu var2, int var3, int var4) {
      wc.t var5 = this.lA;
      if (!var5.M && !var5.C && !var5.B) {
         if (var5.O && var2 == var5.t && var5.S.size() == var2.slots.size() && var5.A.isEmpty() && var2.getCarried().isEmpty()) {
            for (int var6 = 0; var6 < var5.S.size(); var6++) {
               if (!com.larpclient.s9.z(var2.getSlot(var6).getItem(), var5.S.get(var6))) {
                  this.a("pre-batch local/server mismatch slot=" + var6);
                  this.y(var1, var2, var5);
                  return;
               }
            }

            Slot var26 = var2.getSlot(var3);
            Slot var7 = var2.getSlot(var4);
            ItemStack var8 = var26.getItem().copy();
            ItemStack var9 = var7.getItem().copy();
            ItemStack var10 = var5.S.get(var3);
            ItemStack var11 = var5.S.get(var4);
            int var12 = !var9.isEmpty() && !ItemStack.isSameItemSameComponents(var8, var9)
                  || !var11.isEmpty() && !ItemStack.isSameItemSameComponents(var10, var11)
               ? 0
               : 1;
            bx.i var13 = bx.batch(
               var3,
               var4,
               var10.getCount(),
               var11.getCount(),
               (boolean)var12,
               Math.min(var26.getMaxStackSize(var10), var10.getMaxStackSize()),
               Math.min(var7.getMaxStackSize(var10), var10.getMaxStackSize())
            );
            bx.i var14 = bx.batch(
               var3,
               var4,
               var8.getCount(),
               var9.getCount(),
               (boolean)var12,
               Math.min(var26.getMaxStackSize(var8), var8.getMaxStackSize()),
               Math.min(var7.getMaxStackSize(var8), var8.getMaxStackSize())
            );
            if (var13 != null
               && var13.equals(var14)
               && var26.mayPickup(var1.player)
               && var7.mayPlace(var8)
               && var7.mayPlace(var10)
               && (var13.clicks() != 3 || var26.mayPlace(var8) && var26.mayPlace(var10))) {
               var5.y = var3;
               var5.D = var10.copyWithCount(var13.returned().slotCount());
               var5.o = var4;
               var5.c = (var11.isEmpty() ? var10 : var11).copyWithCount(var13.placement().slotCount());
               var5.J = ItemStack.EMPTY;
               var5.M = (boolean)1;
               var5.N = 0;
               ClientPacketListener var15 = var1.getConnection();
               PlayerInfo var16 = var15 == null ? null : var15.getPlayerInfo(var1.player.getUUID());
               var5.L.Y(var16 == null ? 0 : var16.getLatency());
               var5.L.W(var2, var4, var3, this.l0);
               var5.R = var5.v = var5.I = "none";

               for (int var17 = 0; var17 < var13.clicks(); var17++) {
                  int var18 = var17 == 1 ? var4 : var3;
                  int var19 = var17 == 0 ? var13.pickupButton() : (var17 == 1 ? 1 : 0);
                  var5.a = var19;
                  this.lh = (boolean)(var17 == var13.clicks() - 1 ? 1 : 0);

                  try {
                     var1.gameMode.handleContainerInput(var2.containerId, var18, var19, ContainerInput.PICKUP, var1.player);
                  } finally {
                     this.lh = (boolean)0;
                  }

                  int var20 = var17 == 2 ? var13.returned().slotCount() : var13.pickup().slotCount();
                  int var21 = var17 == 0 ? var9.getCount() : var13.placement().slotCount();
                  int var22 = var17 == 0 ? var13.pickup().cursorCount() : (var17 == 1 ? var13.placement().cursorCount() : 0);
                  ItemStack var23 = (var9.isEmpty() ? var8 : var9).copyWithCount(var21);
                  if (!ItemStack.matches(var26.getItem(), var8.copyWithCount(var20))
                     || !ItemStack.matches(var7.getItem(), var23)
                     || !ItemStack.matches(var2.getCarried(), var8.copyWithCount(var22))) {
                     this.a("batch prediction diverged after click=" + var17 + "; no replay");
                     this.y(var1, var2, var5);
                     return;
                  }
               }

               te.R("HigherLower Batch issued clicks=" + var13.clicks() + " source=" + var3 + " " + h(var5));
            } else {
               this.a("unsafe batch: source, capacity or vanilla component merge mismatch");
            }
         } else {
            this.a("batch requires known handler and empty authoritative cursor");
         }
      }
   }

   private void Z(Minecraft var1, AbstractContainerMenu var2, int var3, int var4) {
      wc.t var5 = this.lA;
      var5.y = -1;
      if (com.larpclient.s9.z(var2.getSlot(var3).getItem(), var5.S.get(var3)) && com.larpclient.s9.z(var2.getCarried(), var5.A)) {
         ItemStack var6 = var5.S.get(var3);
         ItemStack var7 = var5.A;
         bx var8 = bx.plan(
            var6.getCount(),
            var7.getCount(),
            var4,
            var7.isEmpty() || H(var2.getSlot(var3), var7, var4 == 1 ? 1 : var7.getCount()),
            var7.isEmpty() ? 0 : Math.min(var2.getSlot(var3).getMaxStackSize(var7), var7.getMaxStackSize())
         );
         if (var8 == null) {
            this.a("transfer unavailable before click slot=" + var3);
         } else {
            var5.c = (var6.isEmpty() ? var7 : var6).copyWithCount(var8.slotCount());
            var5.J = (var7.isEmpty() ? var6 : var7).copyWithCount(var8.cursorCount());
            var5.M = (boolean)1;
            var5.o = var3;
            var5.a = var4;
            var5.N = 0;
            var5.L.p(var2, var3, this.l0);
            var5.R = var5.v = var5.I = "none";
            this.lh = (boolean)1;

            try {
               var1.gameMode.handleContainerInput(var2.containerId, var3, var4, ContainerInput.PICKUP, var1.player);
            } finally {
               this.lh = (boolean)0;
            }

            te.R("HigherLower Click issued " + h(var5));
            if (com.larpclient.s9.z(var5.c, var5.S.get(var3)) || com.larpclient.s9.z(var5.J, var5.A)) {
               this.a("issued pickup did not change both slot and cursor; no replay");
               this.y(var1, var2, var5);
            }
         }
      } else {
         this.a(
            "pre-click local/server mismatch step="
               + var5.P
               + " slot="
               + var3
               + " localSlot="
               + g(var2.getSlot(var3).getItem())
               + " serverSlot="
               + g(var5.S.get(var3))
               + " localCursor="
               + g(var2.getCarried())
               + " serverCursor="
               + g(var5.A)
         );
         this.y(var1, var2, var5);
      }
   }

   @Override
   public void onInventorySynchronized(int var1, List<ItemStack> var2, ItemStack var3) {
      long var4 = ++this.l0;
      Minecraft var6 = Minecraft.getInstance();
      wc.t var7 = this.lA;
      if (var7 != null) {
         var7.R = "full seq=" + var4 + " sync=" + var1 + " size=" + var2.size();
      }

      if (var7 != null && var6.player != null && var1 == var7.z && var6.player.containerMenu == var7.t && var2.size() == var7.t.slots.size()) {
         var7.S.clear();

         for (ItemStack var9 : var2) {
            var7.S.add(var9.copy());
         }

         var7.A = var3.copy();
         var7.v = var7.I = "full";
         var7.O = (boolean)1;
         var7.T.I();
         if (var7.M) {
            var7.L.C(var7.t, var4, true, -1, true);
         }
      }

      this.t(var6);
      this.H();
   }

   @Override
   public void onServerSlot(int var1, int var2, ItemStack var3) {
      long var4 = ++this.l0;
      Minecraft var6 = Minecraft.getInstance();
      wc.t var7 = this.lA;
      if (var7 != null) {
         var7.R = "slot seq=" + var4 + " sync=" + var1 + " slot=" + var2 + " " + g(var3);
      }

      if (var7 != null && var7.O && var6.player != null && var6.player.containerMenu == var7.t) {
         if (var1 == -1 && var2 == -1) {
            var7.A = var3.copy();
            var7.I = "cursor-packet";
            if (var7.M) {
               var7.L.C(var7.t, var4, false, -1, true);
            }
         } else if (var1 == var7.z && var2 >= 0 && var2 < var7.S.size()) {
            var7.S.set(var2, var3.copy());
            if (var2 == var7.o) {
               var7.v = "handler-slot";
            }

            if (var7.M) {
               var7.L.C(var7.t, var4, false, var2, false);
            }
         } else if (var1 == 0 && var2 >= 0 && var2 < var6.player.inventoryMenu.slots.size()) {
            Slot var8 = var6.player.inventoryMenu.getSlot(var2);
            int var9 = var8.container == var6.player.getInventory() ? D(var7.t, var6, var8.getContainerSlot()) : -1;
            if (var9 >= 0 && var9 < var7.S.size()) {
               var7.S.set(var9, var3.copy());
               if (var9 == var7.o) {
                  var7.v = "sync0-slot";
               }

               if (var7.M) {
                  var7.L.C(var7.t, var4, false, var9, false);
               }
            }
         }
      }

      this.t(Minecraft.getInstance());
      this.H();
   }

   @Override
   public void onPlayerInventorySlot(int var1, ItemStack var2) {
      long var3 = ++this.l0;
      Minecraft var5 = Minecraft.getInstance();
      wc.t var6 = this.lA;
      if (var6 != null) {
         var6.R = "player-inventory seq=" + var3 + " index=" + var1 + " " + g(var2);
      }

      if (var6 != null && var6.O && var5.player != null && var5.player.containerMenu == var6.t) {
         int var7 = D(var5.player.containerMenu, var5, var1);
         if (var7 >= 0 && var7 < var6.S.size()) {
            var6.S.set(var7, var2.copy());
            if (var7 == var6.o) {
               var6.v = "player-inventory";
            }

            if (var6.M) {
               var6.L.C(var6.t, var3, false, var7, false);
            }
         }
      }

      this.t(var5);
      this.H();
   }

   @Override
   public boolean hidesCursor(AbstractContainerMenu var1) {
      return this.lA != null && var1.containerId == this.lA.z && !this.lA.K.isEmpty();
   }

   @Override
   public boolean blocksUserInteraction() {
      return this.lA != null && !this.lA.K.isEmpty();
   }

   @Override
   public int clickRevision(AbstractContainerMenu var1) {
      int var2 = var1.getStateId();
      wc.t var3 = this.lA;
      if (var3 != null && var1 == var3.t && (this.lh || var3.B)) {
         char var4 = '耀';
         if (this.lh) {
            var3.H = var2;
            var3.l = var4;
         }

         return var4;
      } else {
         return var2;
      }
   }

   @Override
   public boolean blocksClose() {
      wc.t var1 = this.lA;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 == null || var2.player == null) {
         return false;
      }

      if (var1.B) {
         return false;
      }

      AbstractContainerMenu var3 = var2.player.containerMenu;
      return var3.containerId == var1.z && (var1.M || !var1.O || !var3.getCarried().isEmpty() || !var1.A.isEmpty());
   }

   @Override
   public boolean allowsRecoveryOpen(BlockPos var1) {
      return this.lA != null && this.lA.z < 0 && this.lA.E.equals(var1);
   }

   public boolean Y(BlockPos var1) {
      return this.lA == null || this.lA.z < 0 && this.lA.E.equals(var1);
   }

   private void o(Minecraft var1, AbstractContainerMenu var2, wc.t var3) {
      if (!var3.B) {
         ItemStack var4 = var2.getCarried();
         if (var4.isEmpty() && var3.A.isEmpty()) {
            if (!var3.b.isEmpty()) {
               ItemStack var8 = var2.getSlot(var3.h).getItem();
               if (!var8.isEmpty() && !I(var8, var3.b) || var3.b.getCount() - var8.getCount() > 1) {
                  this.y(var1, var2, var3);
                  return;
               }
            }

            for (int var9 = 0; var9 < var3.Z.length; var9++) {
               ItemStack var10 = var3.Z[var9];
               ItemStack var11 = var1.player.getInventory().getItem(var9);
               if (!var10.isEmpty() && (!I(var10, var11) || var11.getCount() < var10.getCount())) {
                  this.y(var1, var2, var3);
                  return;
               }
            }

            this.S(var1, false);
         } else if (com.larpclient.s9.z(var4, var3.A)) {
            int var5 = this.h(var4) == var3.e ? var3.h : D(var2, var1, var3.k);
            if (var5 >= 0 && H(var2.getSlot(var5), var4, var4.getCount())) {
               this.Z(var1, var2, var5, 0);
            } else {
               for (Slot var7 : var2.slots) {
                  if (H(var7, var4, var4.getCount())) {
                     this.Z(var1, var2, var7.index, 0);
                     return;
                  }
               }

               this.y(var1, var2, var3);
            }
         }
      }
   }

   private void y(Minecraft var1, AbstractContainerMenu var2, wc.t var3) {
      var3.K.clear();
      var3.M = (boolean)0;
      this.ln = -1;
      if (!var3.B) {
         var3.B = (boolean)1;
         te.Z("HigherLower", "Manual recovery required: clear cursor, restore baseline slots and return any source surplus");
      }

      if (var2 instanceof DispenserMenu && (var1.gui.screen() == null || var1.gui.screen() instanceof AbstractContainerScreen var4 && var4.getMenu() != var2)) {
         var1.gui.setScreen(new DispenserScreen((DispenserMenu)var2, var1.player.getInventory(), Component.literal("HigherLower recovery: " + var3.w)));
      }
   }

   private void a(String var1) {
      if (this.lA != null && !this.lA.C) {
         this.lA.C = (boolean)1;
         this.lA.w = var1;
         te.Z("HigherLower", "Exchange failed: " + var1 + "; parking cursor without replay");
      }
   }

   @Override
   public void abortSwap() {
      this.a("cancelled");
   }

   private void S(Minecraft var1, boolean var2) {
      wc.t var3 = this.lA;
      AbstractContainerMenu var4 = var1.player.containerMenu;
      if (var4.getCarried().isEmpty()) {
         if (var4 == var3.t) {
            var1.player.closeContainer();
         }

         if (var2) {
            te.H("HigherLower", var3.p, var3.e, this.getTemplateIdForValue(var3.e));
            this.u(var3.e, var3.U);
            this.lx = (boolean)1;
            if (var1.gameRenderer.itemInHandRenderer instanceof kl var5) {
               var5.larpRig$releaseHeldMap(this, var1.player.getMainHandItem());
            }
         } else {
            this.ll = this.ld = -1;
            this.lo = ItemStack.EMPTY;
            this.lN = null;
            this.ln = -1;
            this.lr = -1;
         }

         this.lA = null;
         this.l3.clear();
         te.Z(
            "HigherLower",
            var2
               ? "One-card exchange confirmed (open=" + (var3.g - var3.x) / 1000000L + "ms, transfer=" + (System.nanoTime() - var3.g) / 1000000L + "ms)"
               : "Exchange stopped: " + var3.w
         );
      }
   }

   private void k(Minecraft var1, wc.t var2) {
      if (var1.gameMode != null && var1.getConnection() != null && !var1.player.isShiftKeyDown() && !var1.options.keyUse.isDown()) {
         BlockHitResult var3 = U(var1, var2.E);
         if (var3 != null) {
            var2.j = (boolean)1;
            this.Z(var1, var3, var1x -> var2.T.p(var1x, var2.r));
         }
      }
   }

   @Override
   public void onBlockInteractionAcknowledged(int var1) {
      wc.t var2 = this.lA;
      if (var2 != null && var2.T.x()) {
         boolean var3 = var2.T.S();
         var2.T.P(var1);
         if (!var3 && var2.T.S()) {
            te.R("HigherLower Dispenser interaction processed sync=" + var2.z + " " + var2.T.U());
         }

         this.H();
      }
   }

   private void H() {
      Minecraft var1 = Minecraft.getInstance();
      if (!this.lj && !this.lh && this.lA != null && var1.player != null && var1.level != null) {
         this.lj = (boolean)1;

         try {
            this.v(var1);
         } finally {
            this.lj = (boolean)0;
         }
      }
   }

   @Override
   public boolean isSwapActive() {
      return this.lA != null;
   }

   @Override
   public boolean isSwapBusy() {
      return this.lA != null;
   }

   @Override
   public boolean isSwapSuppressing() {
      return this.lA != null && this.lA.j;
   }

   @Override
   public boolean shouldSuppressOpen(MenuType<?> var1) {
      return var1 == MenuType.GENERIC_3x3 && this.isSwapSuppressing();
   }

   @Override
   public void onSwapContainerOpened(MenuType<?> var1, int var2) {
      wc.t var3 = this.lA;
      if (var3 != null && var1 == MenuType.GENERIC_3x3) {
         int var4 = var3.T.x() && !var3.M && var3.P <= 0 ? 0 : 1;
         te.R("HigherLower Dispenser opened sync=" + var2 + " " + var3.T.U());
         if (var4 != 0) {
            this.a("unsolicited or mid-transfer container replacement");
            var3.M = (boolean)0;
         }

         var3.z = var2;
         var3.g = System.nanoTime();
         var3.O = (boolean)0;
         var3.t = Minecraft.getInstance().player.containerMenu;
         var3.L.p(var3.t, -1, this.l0);
         var3.N = 0;
         var3.S.clear();
         var3.A = ItemStack.EMPTY;
         if (var4 != 0) {
            Minecraft var5 = Minecraft.getInstance();
            this.y(var5, var5.player.containerMenu, var3);
         }
      }
   }

   @Override
   public int getSwapSyncId() {
      return this.lA == null ? -1 : this.lA.z;
   }

   @Override
   public int getSwapValue() {
      return this.lA == null ? this.ll : this.lA.e;
   }

   @Override
   public int getSwapRealMapId() {
      return this.lA == null ? -1 : M(this.lA.V);
   }

   @Override
   public boolean isSwapJuggling() {
      return this.lA != null;
   }

   @Override
   public boolean isSwapFreezing() {
      return this.lA != null;
   }

   @Override
   public boolean isSwapFreezingPassThrough() {
      return false;
   }

   @Override
   public boolean isDealBobAllowed(ItemStack var1) {
      return this.lA == null;
   }

   @Override
   public boolean isHotbarBobAllowed(ItemStack var1) {
      return this.lA == null;
   }

   @Override
   public ItemStack getVisualOverride(ItemStack var1) {
      return null;
   }

   @Override
   public ItemStack getInventorySlotProjection(int var1) {
      ItemStack var2 = mg.Y(var1);
      if (var2 != null) {
         return var2;
      } else {
         return this.lA == null ? null : this.lA.K.get(var1);
      }
   }

   @Override
   public ItemStack projectSlot(int var1, ItemStack var2) {
      ItemStack var3 = this.getInventorySlotProjection(var1);
      return var3 == null ? var2 : var3;
   }

   private void A(Minecraft var1) {
      int var2 = var1.player.getInventory().getSelectedSlot();
      if (var2 != this.ln) {
         this.lc++;
      }

      this.ln = var2;
      this.lx = (boolean)0;
      this.lf = this.projectSlot(var2, var1.player.getMainHandItem()).copy();
   }

   @Override
   public boolean isDealtHandLocked() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null || var1.player.getInventory().getSelectedSlot() != this.ln || this.lx && !I(var1.player.getMainHandItem(), this.lf)) {
         this.ln = -1;
      }

      return this.ln >= 0;
   }

   @Override
   public ItemStack getDealtHandVisual(ItemStack var1) {
      if (!this.isDealtHandLocked()) {
         return var1;
      } else {
         return this.lx ? var1 : this.projectSlot(this.ln, var1);
      }
   }

   @Override
   public long getDealtHandSequence() {
      return this.lc;
   }

   @Override
   public boolean isDealtHandSettled() {
      return this.lx;
   }

   @Override
   public void signalFramePlacement(ItemFrame var1) {
      this.ln = -1;
      if (Minecraft.getInstance().gameRenderer.itemInHandRenderer instanceof kl var2) {
         var2.larpRig$releaseHeldMap(this, Minecraft.getInstance().player.getMainHandItem());
      }
   }

   private int i() {
      if (this.ll >= 1 && this.l_ != wc.n.H) {
         ArrayList var1 = new ArrayList();

         for (int var2 = 1; var2 <= 9; var2++) {
            int var3 = this.l_ == wc.n.s ? (var2 > this.ll ? 1 : 0) : (var2 < this.ll ? 1 : 0);
            if (var3 != 0 && this.getTemplateForValue(var2) != null && this.v(var2) >= 0) {
               var1.add(var2);
            }
         }

         if (!var1.isEmpty()) {
            return (Integer)var1.get(ThreadLocalRandom.current().nextInt(var1.size()));
         } else {
            return (this.l_ == wc.n.F && this.ll == 1 || this.l_ == wc.n.s && this.ll == 9) && this.v(this.ll) >= 0 ? this.ll : -1;
         }
      } else {
         return -1;
      }
   }

   private int h(ItemStack var1) {
      int var2 = M(var1);
      if (var2 < 0) {
         return -1;
      }

      for (int var3 = 1; var3 <= 9; var3++) {
         if (this.getTemplateIdForValue(var3) == var2) {
            return var3;
         }
      }

      return -1;
   }

   private static int M(ItemStack var0) {
      MapId var1 = var0.is(Items.FILLED_MAP) ? (MapId)var0.get(DataComponents.MAP_ID) : null;
      return var1 == null ? -1 : var1.id();
   }

   private static boolean I(ItemStack var0, ItemStack var1) {
      return com.larpclient.s9.E(var0, var1);
   }

   private static boolean R(ItemStack var0, ItemStack var1) {
      return !var0.is(Items.FILLED_MAP) && !var1.is(Items.FILLED_MAP) || com.larpclient.s9.z(var0, var1);
   }

   private static String g(ItemStack var0) {
      return "map=" + M(var0) + "/count=" + var0.getCount() + "/filled=" + var0.is(Items.FILLED_MAP);
   }

   private static String h(wc.t var0) {
      return "step="
         + var0.P
         + " slot="
         + var0.o
         + " button="
         + var0.a
         + " sync="
         + var0.z
         + " revision="
         + var0.H
         + " probe="
         + var0.l
         + " ack["
         + var0.L.y()
         + "] slotSource="
         + var0.v
         + " cursorSource="
         + var0.I
         + " lastPacket=["
         + var0.R
         + "] expectedSlot="
         + g(var0.c)
         + " receivedSlot="
         + g(var0.S.get(var0.o))
         + " expectedCursor="
         + g(var0.J)
         + " receivedCursor="
         + g(var0.A)
         + (var0.y < 0 ? "" : " batchSource=" + var0.y + " expectedSource=" + g(var0.D) + " receivedSource=" + g(var0.S.get(var0.y)))
         + " slotIdentity="
         + com.larpclient.s9.z(var0.S.get(var0.o), var0.c)
         + " cursorIdentity="
         + com.larpclient.s9.z(var0.A, var0.J)
         + " slotComponentsExact="
         + ItemStack.isSameItemSameComponents(var0.S.get(var0.o), var0.c)
         + " cursorComponentsExact="
         + ItemStack.isSameItemSameComponents(var0.A, var0.J);
   }

   private static ItemStack[] n(Minecraft var0) {
      ItemStack[] var1 = new ItemStack[36];

      for (int var2 = 0; var2 < var1.length; var2++) {
         var1[var2] = var0.player.getInventory().getItem(var2).copy();
      }

      return var1;
   }

   private static Map<Integer, Integer> a(ItemStack[] var0) {
      HashMap var1 = new HashMap();

      for (ItemStack var5 : var0) {
         if (M(var5) >= 0) {
            var1.merge(M(var5), var5.getCount(), Integer::sum);
         }
      }

      return var1;
   }

   private static Map<Integer, Integer> i(Minecraft var0) {
      Map var1 = a(n(var0));
      ItemStack var2 = var0.player.containerMenu.getCarried();
      if (M(var2) >= 0) {
         var1.merge(M(var2), var2.getCount(), Integer::sum);
      }

      if (var0.level != null) {
         for (Entity var4 : var0.level.entitiesForRendering()) {
            if (var4 instanceof ItemFrame var5) {
               ItemStack var6 = var5.getItem();
               if (M(var6) >= 0) {
                  var1.merge(M(var6), var6.getCount(), Integer::sum);
               }
            }
         }
      }

      return var1;
   }

   private static int D(AbstractContainerMenu var0, Minecraft var1, int var2) {
      for (Slot var4 : var0.slots) {
         if (var4.container == var1.player.getInventory() && var4.getContainerSlot() == var2) {
            return var4.index;
         }
      }

      return -1;
   }

   private static boolean H(Slot var0, ItemStack var1, int var2) {
      ItemStack var3 = var0.getItem();
      return !var1.isEmpty()
         && var0.mayPlace(var1)
         && (var3.isEmpty() || I(var3, var1))
         && var3.getCount() + var2 <= Math.min(var0.getMaxStackSize(var1), var1.getMaxStackSize());
   }

   public enum n {
      H("off"),
      s("higher"),
      F("lower");

      private final String P;

      n(String var3) {
         this.P = var3;
      }

      public String I() {
         return this.P;
      }
   }

   private static final class t {
      final long x = System.nanoTime();
      long g;
      final String G;
      final BlockPos E;
      final wc.n n;
      final int F;
      final int e;
      final int p;
      final int k;
      final int U;
      final ItemStack[] Z;
      final ItemStack V;
      final Map<Integer, Integer> i;
      final Map<Integer, ItemStack> K = new HashMap<>();
      int z = -1;
      int h = -1;
      int q = -1;
      int P;
      int r;
      int N;
      int o = -1;
      final t0 T = new t0();
      boolean j;
      boolean O;
      boolean M;
      boolean C;
      boolean B;
      String w;
      List<ItemStack> S = new ArrayList<>();
      ItemStack A = ItemStack.EMPTY;
      ItemStack c = ItemStack.EMPTY;
      ItemStack J = ItemStack.EMPTY;
      int y = -1;
      ItemStack D = ItemStack.EMPTY;
      ItemStack b = ItemStack.EMPTY;
      ItemStack Y = ItemStack.EMPTY;
      final w9 L = new w9();
      AbstractContainerMenu t;
      int a;
      int H;
      int l;
      String R = "none";
      String v = "none";
      String I = "none";

      t(String var1, BlockPos var2, wc.n var3, int var4, int var5, int var6, int var7, int var8, ItemStack[] var9, ItemStack var10, Map<Integer, Integer> var11) {
         this.G = var1;
         this.E = var2;
         this.n = var3;
         this.F = var4;
         this.e = var5;
         this.p = var6;
         this.k = var7;
         this.U = var8;
         this.Z = var9;
         this.V = var10.copyWithCount(1);
         this.i = new HashMap<>(var11);
      }
   }
}
