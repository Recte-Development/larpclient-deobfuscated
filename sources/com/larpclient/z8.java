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
public final class z8 extends ze {
   private static final z8 EO = new z8();
   private final Map<Integer, Integer> EN = new HashMap<>();
   private final Map<Integer, ItemStack> Ea = new HashMap<>();
   private ItemStack[] Ez;
   private Map<Integer, Integer> EI;
   private z8.c Er = z8.c.w;
   private z8.c Eh = z8.c.w;
   private int E8 = -1;
   private int E5 = -1;
   private int E1 = -1;
   private ItemStack EC = ItemStack.EMPTY;
   private int E2 = -1;
   private int EL;
   private int EQ;
   private boolean Ew;
   private String ER;
   private BlockPos Eg;
   private int E3 = -1;
   private ClientLevel Ec;
   private long Ef = -1L;
   private z8.v E_;
   private int EX = -1;
   private ItemStack EP;
   private boolean Em;
   private long E6;
   private boolean EH;
   private boolean Ex;
   private long Eu;
   private String Et = "no map change";

   private z8() {
   }

   public static z8 l() {
      return EO;
   }

   public z8.c I() {
      return this.Er;
   }

   public String M() {
      return this.Er.p();
   }

   public int g() {
      return this.E8;
   }

   public int t() {
      return this.E2;
   }

   public boolean k() {
      return this.Ew;
   }

   public int p(int var1) {
      return this.EN.getOrDefault(this.getTemplateIdForValue(var1), 0);
   }

   public void o(String var1) {
      this.Er = "higher".equalsIgnoreCase(var1) ? z8.c.Q : ("lower".equalsIgnoreCase(var1) ? z8.c.Y : z8.c.w);
      if (this.Er == z8.c.w) {
         this.Q();
      }
   }

   public void J() {
      this.o(switch (this.Er) {
         case w -> "higher";
         case Q -> "lower";
         case Y -> "off";
      });
   }

   public void Q() {
      this.clearQueue();
      this.E8 = this.E5 = this.E1 = -1;
      this.EC = ItemStack.EMPTY;
      this.EI = null;
      this.EQ = 0;
   }

   @Override
   public void clearQueue() {
      this.Ew = (boolean)0;
      this.E2 = -1;
      if (this.E_ != null) {
         this.abortSwap();
      } else {
         this.EN.clear();
      }
   }

   @Override
   protected boolean supportsPhysicalSwap() {
      return false;
   }

   @Override
   protected BlockPos getAssignedDispenserPosition(Minecraft var1) {
      ep var2 = ep.H();
      return var2.v(ep.o.c).contains(this.a()) ? var2.x(this.a()) : null;
   }

   @Override
   public boolean hasQueuedForce() {
      return this.Ew && this.E2 > 0;
   }

   @Override
   public int getPendingValue() {
      return this.hasQueuedForce() ? this.E2 : -1;
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
      if (!Objects.equals(var1, this.a())) {
         if (this.a() != null) {
            this.Q();
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
            int var6 = this.t(var5);
            if (var6 > 0) {
               this.Ea.put(var6, var5.copyWithCount(1));
            }
         }
      }

      return var2;
   }

   public void j(Minecraft var1) {
      this.s(var1);
      if (var1 != null && var1.player != null && _z.e().p && this.Er != z8.c.w && !this.Ew && !this.isSwapBusy()) {
         ze var2 = ze.c();
         if (var2 == this || !var2.isSwapBusy()) {
            this.Ez = D(var1);
            if (this.E8 > 0) {
               int var3 = this.E5 >= 0 && this.E5 < this.Ez.length && !this.EC.isEmpty() && j(this.EC, this.Ez[this.E5]) ? this.E5 : -1;
               if (var3 < 0) {
                  for (int var4 = 0; var4 < this.Ez.length; var4++) {
                     if (!this.EC.isEmpty() && j(this.EC, this.Ez[var4])) {
                        if (var3 >= 0) {
                           this.clearQueue();
                           xa.q("HigherLower", "Activation disarmed: moved reference stack is ambiguous");
                           return;
                        }

                        var3 = var4;
                     }
                  }
               }

               if (var3 < 0) {
                  this.Q();
                  xa.q("HigherLower", "Previous reference removed; waiting for first card of new round");
               } else {
                  this.E5 = var3;
               }
            }

            this.EN.clear();
            this.EN.putAll(L(this.Ez));
            if (this.E8 < 1 && this.EI == null) {
               this.EI = o(var1);
            }

            this.E1 = this.E8;
            this.Eh = this.Er;
            this.ER = this.a();
            BlockPos var5 = this.getAssignedDispenserPosition(var1);
            this.Eg = var5 == null ? null : var5.immutable();
            this.E2 = this.v();
            this.E3 = this.K(this.E2);
            this.Ew = (boolean)1;
            this.EL = 0;
            this.Et = "no map change";
            xa.i("HigherLower Activation target=" + this.Eh.p() + " reference=" + this.E1 + " recommendation=" + this.E2);
         }
      }
   }

   @Override
   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      this.s(Minecraft.getInstance());
   }

   private void s(Minecraft var1) {
      if (this.Ew && this.E_ == null && var1 != null && var1.player != null) {
         ItemStack[] var2 = D(var1);
         Map var3 = L(var2);
         int var4 = -1;
         int var5 = -1;

         for (int var6 = 0; var6 < var2.length; var6++) {
            if (!u(var2[var6], this.Ez[var6])) {
               ItemStack var7 = this.Ez[var6];
               ItemStack var8 = var2[var6];
               int var9 = this.t(var8);
               if (var4 >= 0 || var9 < 1 || !var7.isEmpty() && !j(var7, var8) || var8.getCount() != var7.getCount() + 1) {
                  this.Et = "slot="
                     + var6
                     + " reason="
                     + (
                        var4 >= 0
                           ? "multiple map changes"
                           : (var9 < 1 ? "unrecognized or removed map" : (!var7.isEmpty() && !j(var7, var8) ? "origin/components changed" : "not +1"))
                     )
                     + " before="
                     + X(var7)
                     + " after="
                     + X(var8);
                  return;
               }

               var4 = var6;
               var5 = var9;
            }
         }

         if (var4 >= 0 && var3.getOrDefault(I(var2[var4]), 0) == this.EN.getOrDefault(I(var2[var4]), 0) + 1) {
            int var12 = this.Eh == z8.c.Q ? (var5 > this.E1 ? 1 : 0) : (var5 < this.E1 ? 1 : 0);
            if (this.E1 < 1 || this.E2 < 1 || var12 != 0 || var5 == this.E2) {
               this.e(var5, var4);
            } else if (w5.h(var1) && w5.J(this, var4, this.E2)) {
               int var14 = this.E2;
               this.O(var14, var4, var5, true);
            } else {
               int var13 = -1;
               int var15 = this.E2 == this.E1 ? 1 : 0;
               if (var15 != 0 && this.E5 >= 0 && this.t(this.Ez[this.E5]) == this.E2) {
                  var13 = this.E5;
               } else if (var15 == 0) {
                  int var16 = this.E5 >= 0 && this.E5 < 9 ? this.E5 : var1.player.getInventory().getSelectedSlot();

                  for (int var10 = 1; var10 <= 9; var10++) {
                     int var11 = (var16 + var10) % 9;
                     if (this.Ez[var11].isEmpty() && (var2[var11].isEmpty() || var11 == var4)) {
                        var13 = var11;
                        break;
                     }
                  }
               }

               ItemStack var17 = this.Ea.get(this.E2);
               if (var15 != 0 && var13 >= 0) {
                  var17 = this.Ez[var13].copyWithCount(1);
               }

               if (var17 != null
                  && I(var17) == this.getTemplateIdForValue(this.E2)
                  && var13 >= 0
                  && this.Eg != null
                  && (var15 == 0 || j(this.Ez[var13], var17) && this.Ez[var13].getCount() < var17.getMaxStackSize())) {
                  z8.v var18 = new z8.v(this.ER, this.Eg, this.Eh, this.E1, this.E2, var5, var4, var13, this.Ez, var2[var4], this.EN);
                  var18.e = this.E3;
                  var18.y.merge(this.getTemplateIdForValue(var18.C), 1, Integer::sum);
                  var18.f.put(var4, var18.R[var4].copy());
                  var18.f.put(var13, var17.copyWithCount(var18.R[var13].getCount() + 1));
                  this.E_ = var18;
                  var18.F = 1;
                  this.Ew = (boolean)0;
                  this.Z(var1);
                  xa.q(
                     "HigherLower",
                     "One-card exchange real="
                        + var5
                        + " ->"
                        + var18.C
                        + " target="
                        + var18.U.p()
                        + " reference="
                        + var18.w
                        + " arrival="
                        + var4
                        + " destination="
                        + var13
                  );
                  this.t();
               } else {
                  xa.q("HigherLower", "Exchange unavailable: destination, components, capacity or assignment");
                  this.e(var5, var4);
               }
            }
         } else {
            this.Et = "reason=no +1 map total arrival=" + var4;
         }
      }
   }

   private void e(int var1, int var2) {
      this.O(var1, var2, var1, false);
   }

   private void O(int var1, int var2, int var3, boolean var4) {
      if (this.E_ == null) {
         xa.q("HigherLower", xa.U(Integer.toString(var3), Integer.toString(var1), this.E1 > 0 && this.E2 > 0, var4));
      }

      this.E8 = var1;
      this.E5 = var2;
      this.EC = Minecraft.getInstance().player.getInventory().getItem(var2).copyWithCount(1);
      this.Ew = (boolean)0;
      this.E2 = -1;
      this.EN.clear();
   }

   @Override
   public void tick(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         if (this.Ec != null && this.Ec != var1.level) {
            this.E_ = null;
            this.EX = -1;
            this.Q();
         }

         if (this.Ec != var1.level || this.Ef != var1.level.getGameTime()) {
            this.Ec = var1.level;
            this.Ef = var1.level.getGameTime();
            if (this.E_ != null) {
               this.E_.F++;
               this.y(var1);
            } else if (_z.e().p) {
               this.s(var1);
               if (this.Ew && ++this.EL > 100) {
                  xa.q("HigherLower", "Draw timed out: " + this.Et + " baselineMapCounts=" + this.EN + " receivedMapCounts=" + L(D(var1)));
                  this.clearQueue();
               }

               if (this.E_ == null && !this.Ew && this.E8 > 0 && this.EI != null) {
                  Map var2 = o(var1);
                  int var3 = 0;

                  for (Entry var5 : var2.entrySet()) {
                     var3 += Math.max(0, (Integer)var5.getValue() - this.EI.getOrDefault(var5.getKey(), 0));
                  }

                  this.EQ = var3 == 0 ? this.EQ + 1 : 0;
                  if (this.EQ >= 6) {
                     this.Q();
                  }
               }
            }
         }
      } else {
         this.E_ = null;
         this.EX = -1;
         this.Q();
         this.Ec = null;
      }
   }

   private void y(Minecraft var1) {
      z8.v var2 = this.E_;
      if (!var2.V && var2.O.Y(var2.l) || !Objects.equals(var2.x, this.a()) || !var2.s.equals(this.getAssignedDispenserPosition(var1))) {
         this.N("timeout or assignment changed step=" + var2.d + " sync=" + var2.A + " ready=" + var2.b + " " + var2.g.m());
      }

      AbstractContainerMenu var3 = var1.player.containerMenu;
      if (var2.A < 0) {
         if (var2.F >= 100) {
            this.N("dispenser did not open; no cards moved");
         }

         if (var2.D) {
            if (!var3.getCarried().isEmpty()) {
               var2.f.clear();
               this.EX = -1;
            }

            this.Q(var1, false);
         } else {
            if (var1.gui.screen() == null && var3 == var1.player.inventoryMenu && var3.getCarried().isEmpty() && var2.g.c(var2.F)) {
               this.w(var1, var2);
            }
         }
      } else if (!(var3 instanceof DispenserMenu) || var3 != var2.K || var3.containerId != var2.A) {
         this.N("handler replaced");
         var2.f.clear();
         this.EX = -1;
         this.Q(var1, false);
      } else if (!var2.v) {
         if (var2.b && var1.gameMode != null) {
            if (var2.D || var2.g.Z()) {
               if (var2.V) {
                  if (!var2.O.p(var2.J < 0 || wm.d(var2.k.get(var2.J), var2.m), wm.d(var2.k.get(var2.c), var2.G), wm.d(var2.i, var2.L))) {
                     if (var2.O.g()) {
                        this.N("click not confirmed; no replay " + X(var2));
                        this.k(var1, var3, var2);
                     }

                     return;
                  }

                  xa.i("HigherLower Click confirmed " + X(var2));
                  var2.V = (boolean)0;
                  if (!var2.D && var2.d == 3) {
                     var2.f.put(var2.Z, var2.k.get(var2.c).copy());
                  }

                  var2.I = 0;
                  if (!var2.D) {
                     var2.d += 3;
                  }
               }

               if (var2.D) {
                  this.v(var1, var3, var2);
               } else {
                  int var4 = F(var3, var1, var2.T);
                  int var5 = F(var3, var1, var2.Z);
                  if (var4 >= 0 && var5 >= 0) {
                     if (var2.d == 0) {
                        if (!var3.getCarried().isEmpty()) {
                           this.N("occupied cursor");
                           return;
                        }

                        for (int var6 = 0; var6 < var2.R.length; var6++) {
                           ItemStack var7 = var6 == var2.T ? var2.W.copyWithCount(var2.R[var6].getCount() + 1) : var2.R[var6];
                           ItemStack var8 = var1.player.getInventory().getItem(var6);
                           if (var6 != var2.T && var6 != var2.Z ? !u(var8, var7) : !wm.d(var8, var7)) {
                              this.N("inventory changed before extraction slot=" + var6 + " expected=" + X(var7) + " received=" + X(var8));
                              return;
                           }
                        }

                        if (var2.q < 0) {
                           for (int var11 = 0; var11 < 9; var11++) {
                              if (var11 != var2.e
                                 && ItemStack.isSameItemSameComponents(var3.getSlot(var11).getItem(), var3.getSlot(var4).getItem())
                                 && ItemStack.isSameItemSameComponents(var2.k.get(var11), var2.k.get(var4))
                                 && z(var3.getSlot(var11), var2.W, 1)) {
                                 var2.q = var11;
                                 break;
                              }
                           }

                           for (int var12 = 0; var12 < 9; var12++) {
                              if (var2.q < 0
                                 && var12 != var2.e
                                 && var3.getSlot(var12).getItem().isEmpty()
                                 && var2.k.get(var12).isEmpty()
                                 && z(var3.getSlot(var12), var2.W, 1)) {
                                 var2.q = var12;
                                 break;
                              }
                           }
                        }

                        if (var2.e < 0
                           || var2.e >= 9
                           || var2.q < 0
                           || this.t(var3.getSlot(var2.e).getItem()) != var2.C
                           || !var3.getSlot(var2.e).mayPickup(var1.player)
                           || !var3.getSlot(var5).mayPlace(var3.getSlot(var2.e).getItem())
                           || !var2.R[var2.Z].isEmpty() && !j(var2.R[var2.Z], var3.getSlot(var2.e).getItem())
                           || var2.R[var2.Z].getCount() + 1 > var3.getSlot(var5).getMaxStackSize(var3.getSlot(var2.e).getItem())
                           || !var3.getSlot(var4).mayPickup(var1.player)) {
                           this.N("source, return space, or destination unavailable");
                           return;
                        }

                        ItemStack var13 = var3.getSlot(var2.e).getItem();
                        if (!var2.R[var2.Z].isEmpty()
                           && (
                              !ItemStack.isSameItemSameComponents(var13, var3.getSlot(var5).getItem())
                                 || !ItemStack.isSameItemSameComponents(var2.k.get(var2.e), var2.k.get(var5))
                           )) {
                           this.N("replacement destination has incompatible vanilla components");
                           return;
                        }

                        var2.a = var13.copy();
                        var2.E = var3.getSlot(var2.q).getItem().copy();
                        var2.f.put(var2.Z, var13.copyWithCount(var2.R[var2.Z].getCount() + 1));
                        this.b(var1, var3, var4, var2.q);
                     } else if (var2.d == 3) {
                        if (!var3.getCarried().isEmpty()
                           || !wm.d(var3.getSlot(var2.e).getItem(), var2.a)
                           || !wm.d(var3.getSlot(var4).getItem(), var2.R[var2.T])
                           || !wm.d(var3.getSlot(var5).getItem(), var2.R[var2.Z])
                           || !z(var3.getSlot(var5), var3.getSlot(var2.e).getItem(), 1)) {
                           this.N("replacement source changed");
                           return;
                        }

                        this.b(var1, var3, var2.e, var5);
                     } else {
                        int var14 = var2.i.isEmpty()
                              && var3.getCarried().isEmpty()
                              && var2.y.equals(L(D(var1)))
                              && wm.d(var2.k.get(var2.e), var2.a.copyWithCount(var2.a.getCount() - 1))
                              && wm.d(var2.k.get(var2.q), var2.W.copyWithCount(var2.E.getCount() + 1))
                           ? 1
                           : 0;

                        for (int var15 = 0; var15 < var2.R.length && var14 != 0; var15++) {
                           int var16 = F(var3, var1, var15);
                           ItemStack var9 = var15 == var2.Z ? var2.f.get(var15) : var2.R[var15];
                           int var10 = var15 != var2.T && var15 != var2.Z ? 0 : 1;
                           var14 = var16 < 0
                                 || (
                                    var10 != 0
                                       ? !wm.d(var2.k.get(var16), var9) || !wm.d(var1.player.getInventory().getItem(var15), var9)
                                       : !u(var2.k.get(var16), var9) || !u(var1.player.getInventory().getItem(var15), var9)
                                 )
                              ? 0
                              : 1;
                           if (var14 == 0) {
                              this.N(
                                 "final slot="
                                    + var15
                                    + " expected="
                                    + X(var9)
                                    + " server="
                                    + (var16 < 0 ? "missing" : X(var2.k.get(var16)))
                                    + " local="
                                    + X(var1.player.getInventory().getItem(var15))
                              );
                           }
                        }

                        if (var14 == 0) {
                           this.N(
                              "final quantity ledger mismatch expectedCounts="
                                 + var2.y
                                 + " receivedCounts="
                                 + L(D(var1))
                                 + " source="
                                 + X(var2.k.get(var2.e))
                                 + " expectedSource="
                                 + X(var2.a.copyWithCount(var2.a.getCount() - 1))
                                 + " deposit="
                                 + X(var2.k.get(var2.q))
                                 + " expectedDeposit="
                                 + X(var2.W.copyWithCount(var2.E.getCount() + 1))
                           );
                           return;
                        }

                        this.Q(var1, true);
                     }
                  } else {
                     this.N("player slots missing");
                  }
               }
            }
         } else {
            if (var2.D) {
               this.k(var1, var3, var2);
            }
         }
      }
   }

   private void b(Minecraft var1, AbstractContainerMenu var2, int var3, int var4) {
      z8.v var5 = this.E_;
      if (!var5.V && !var5.D && !var5.v) {
         if (var5.b && var2 == var5.K && var5.k.size() == var2.slots.size() && var5.i.isEmpty() && var2.getCarried().isEmpty()) {
            for (int var6 = 0; var6 < var5.k.size(); var6++) {
               if (!wm.d(var2.getSlot(var6).getItem(), var5.k.get(var6))) {
                  this.N("pre-batch local/server mismatch slot=" + var6);
                  this.k(var1, var2, var5);
                  return;
               }
            }

            Slot var26 = var2.getSlot(var3);
            Slot var7 = var2.getSlot(var4);
            ItemStack var8 = var26.getItem().copy();
            ItemStack var9 = var7.getItem().copy();
            ItemStack var10 = var5.k.get(var3);
            ItemStack var11 = var5.k.get(var4);
            int var12 = !var9.isEmpty() && !ItemStack.isSameItemSameComponents(var8, var9)
                  || !var11.isEmpty() && !ItemStack.isSameItemSameComponents(var10, var11)
               ? 0
               : 1;
            m.y var13 = com.larpclient.m.batch(
               var3,
               var4,
               var10.getCount(),
               var11.getCount(),
               (boolean)var12,
               Math.min(var26.getMaxStackSize(var10), var10.getMaxStackSize()),
               Math.min(var7.getMaxStackSize(var10), var10.getMaxStackSize())
            );
            m.y var14 = com.larpclient.m.batch(
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
               var5.J = var3;
               var5.m = var10.copyWithCount(var13.returned().slotCount());
               var5.c = var4;
               var5.G = (var11.isEmpty() ? var10 : var11).copyWithCount(var13.placement().slotCount());
               var5.L = ItemStack.EMPTY;
               var5.V = (boolean)1;
               var5.I = 0;
               ClientPacketListener var15 = var1.getConnection();
               PlayerInfo var16 = var15 == null ? null : var15.getPlayerInfo(var1.player.getUUID());
               var5.O.z(var16 == null ? 0 : var16.getLatency());
               var5.O.L(var2, var4, var3, this.Eu);
               var5.Q = var5.u = var5.z = "none";

               for (int var17 = 0; var17 < var13.clicks(); var17++) {
                  int var18 = var17 == 1 ? var4 : var3;
                  int var19 = var17 == 0 ? var13.pickupButton() : (var17 == 1 ? 1 : 0);
                  var5.t = var19;
                  this.EH = (boolean)(var17 == var13.clicks() - 1 ? 1 : 0);

                  try {
                     var1.gameMode.handleContainerInput(var2.containerId, var18, var19, ContainerInput.PICKUP, var1.player);
                  } finally {
                     this.EH = (boolean)0;
                  }

                  int var20 = var17 == 2 ? var13.returned().slotCount() : var13.pickup().slotCount();
                  int var21 = var17 == 0 ? var9.getCount() : var13.placement().slotCount();
                  int var22 = var17 == 0 ? var13.pickup().cursorCount() : (var17 == 1 ? var13.placement().cursorCount() : 0);
                  ItemStack var23 = (var9.isEmpty() ? var8 : var9).copyWithCount(var21);
                  if (!ItemStack.matches(var26.getItem(), var8.copyWithCount(var20))
                     || !ItemStack.matches(var7.getItem(), var23)
                     || !ItemStack.matches(var2.getCarried(), var8.copyWithCount(var22))) {
                     this.N("batch prediction diverged after click=" + var17 + "; no replay");
                     this.k(var1, var2, var5);
                     return;
                  }
               }

               xa.i("HigherLower Batch issued clicks=" + var13.clicks() + " source=" + var3 + " " + X(var5));
            } else {
               this.N("unsafe batch: source, capacity or vanilla component merge mismatch");
            }
         } else {
            this.N("batch requires known handler and empty authoritative cursor");
         }
      }
   }

   private void i(Minecraft var1, AbstractContainerMenu var2, int var3, int var4) {
      z8.v var5 = this.E_;
      var5.J = -1;
      if (wm.d(var2.getSlot(var3).getItem(), var5.k.get(var3)) && wm.d(var2.getCarried(), var5.i)) {
         ItemStack var6 = var5.k.get(var3);
         ItemStack var7 = var5.i;
         m var8 = com.larpclient.m.plan(
            var6.getCount(),
            var7.getCount(),
            var4,
            var7.isEmpty() || z(var2.getSlot(var3), var7, var4 == 1 ? 1 : var7.getCount()),
            var7.isEmpty() ? 0 : Math.min(var2.getSlot(var3).getMaxStackSize(var7), var7.getMaxStackSize())
         );
         if (var8 == null) {
            this.N("transfer unavailable before click slot=" + var3);
         } else {
            var5.G = (var6.isEmpty() ? var7 : var6).copyWithCount(var8.slotCount());
            var5.L = (var7.isEmpty() ? var6 : var7).copyWithCount(var8.cursorCount());
            var5.V = (boolean)1;
            var5.c = var3;
            var5.t = var4;
            var5.I = 0;
            var5.O.O(var2, var3, this.Eu);
            var5.Q = var5.u = var5.z = "none";
            this.EH = (boolean)1;

            try {
               var1.gameMode.handleContainerInput(var2.containerId, var3, var4, ContainerInput.PICKUP, var1.player);
            } finally {
               this.EH = (boolean)0;
            }

            xa.i("HigherLower Click issued " + X(var5));
            if (wm.d(var5.G, var5.k.get(var3)) || wm.d(var5.L, var5.i)) {
               this.N("issued pickup did not change both slot and cursor; no replay");
               this.k(var1, var2, var5);
            }
         }
      } else {
         this.N(
            "pre-click local/server mismatch step="
               + var5.d
               + " slot="
               + var3
               + " localSlot="
               + X(var2.getSlot(var3).getItem())
               + " serverSlot="
               + X(var5.k.get(var3))
               + " localCursor="
               + X(var2.getCarried())
               + " serverCursor="
               + X(var5.i)
         );
         this.k(var1, var2, var5);
      }
   }

   @Override
   public void onInventorySynchronized(int var1, List<ItemStack> var2, ItemStack var3) {
      long var4 = ++this.Eu;
      Minecraft var6 = Minecraft.getInstance();
      z8.v var7 = this.E_;
      if (var7 != null) {
         var7.Q = "full seq=" + var4 + " sync=" + var1 + " size=" + var2.size();
      }

      if (var7 != null && var6.player != null && var1 == var7.A && var6.player.containerMenu == var7.K && var2.size() == var7.K.slots.size()) {
         var7.k.clear();

         for (ItemStack var9 : var2) {
            var7.k.add(var9.copy());
         }

         var7.i = var3.copy();
         var7.u = var7.z = "full";
         var7.b = (boolean)1;
         var7.g.z();
         if (var7.V) {
            var7.O.z(var7.K, var4, true, -1, true);
         }
      }

      this.s(var6);
      this.t();
   }

   @Override
   public void onServerSlot(int var1, int var2, ItemStack var3) {
      long var4 = ++this.Eu;
      Minecraft var6 = Minecraft.getInstance();
      z8.v var7 = this.E_;
      if (var7 != null) {
         var7.Q = "slot seq=" + var4 + " sync=" + var1 + " slot=" + var2 + " " + X(var3);
      }

      if (var7 != null && var7.b && var6.player != null && var6.player.containerMenu == var7.K) {
         if (var1 == -1 && var2 == -1) {
            var7.i = var3.copy();
            var7.z = "cursor-packet";
            if (var7.V) {
               var7.O.z(var7.K, var4, false, -1, true);
            }
         } else if (var1 == var7.A && var2 >= 0 && var2 < var7.k.size()) {
            var7.k.set(var2, var3.copy());
            if (var2 == var7.c) {
               var7.u = "handler-slot";
            }

            if (var7.V) {
               var7.O.z(var7.K, var4, false, var2, false);
            }
         } else if (var1 == 0 && var2 >= 0 && var2 < var6.player.inventoryMenu.slots.size()) {
            Slot var8 = var6.player.inventoryMenu.getSlot(var2);
            int var9 = var8.container == var6.player.getInventory() ? F(var7.K, var6, var8.getContainerSlot()) : -1;
            if (var9 >= 0 && var9 < var7.k.size()) {
               var7.k.set(var9, var3.copy());
               if (var9 == var7.c) {
                  var7.u = "sync0-slot";
               }

               if (var7.V) {
                  var7.O.z(var7.K, var4, false, var9, false);
               }
            }
         }
      }

      this.s(Minecraft.getInstance());
      this.t();
   }

   @Override
   public void onPlayerInventorySlot(int var1, ItemStack var2) {
      long var3 = ++this.Eu;
      Minecraft var5 = Minecraft.getInstance();
      z8.v var6 = this.E_;
      if (var6 != null) {
         var6.Q = "player-inventory seq=" + var3 + " index=" + var1 + " " + X(var2);
      }

      if (var6 != null && var6.b && var5.player != null && var5.player.containerMenu == var6.K) {
         int var7 = F(var5.player.containerMenu, var5, var1);
         if (var7 >= 0 && var7 < var6.k.size()) {
            var6.k.set(var7, var2.copy());
            if (var7 == var6.c) {
               var6.u = "player-inventory";
            }

            if (var6.V) {
               var6.O.z(var6.K, var3, false, var7, false);
            }
         }
      }

      this.s(var5);
      this.t();
   }

   @Override
   public boolean hidesCursor(AbstractContainerMenu var1) {
      return this.E_ != null && var1.containerId == this.E_.A && !this.E_.f.isEmpty();
   }

   @Override
   public boolean blocksUserInteraction() {
      return this.E_ != null && !this.E_.f.isEmpty();
   }

   @Override
   public int clickRevision(AbstractContainerMenu var1) {
      int var2 = var1.getStateId();
      z8.v var3 = this.E_;
      if (var3 != null && var1 == var3.K && (this.EH || var3.v)) {
         char var4 = '耀';
         if (this.EH) {
            var3.j = var2;
            var3.X = var4;
         }

         return var4;
      } else {
         return var2;
      }
   }

   @Override
   public boolean blocksClose() {
      z8.v var1 = this.E_;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 == null || var2.player == null) {
         return false;
      }

      if (var1.v) {
         return false;
      }

      AbstractContainerMenu var3 = var2.player.containerMenu;
      return var3.containerId == var1.A && (var1.V || !var1.b || !var3.getCarried().isEmpty() || !var1.i.isEmpty());
   }

   @Override
   public boolean allowsRecoveryOpen(BlockPos var1) {
      return this.E_ != null && this.E_.A < 0 && this.E_.s.equals(var1);
   }

   public boolean v(BlockPos var1) {
      return this.E_ == null || this.E_.A < 0 && this.E_.s.equals(var1);
   }

   private void v(Minecraft var1, AbstractContainerMenu var2, z8.v var3) {
      if (!var3.v) {
         ItemStack var4 = var2.getCarried();
         if (var4.isEmpty() && var3.i.isEmpty()) {
            if (!var3.a.isEmpty()) {
               ItemStack var8 = var2.getSlot(var3.e).getItem();
               if (!var8.isEmpty() && !j(var8, var3.a) || var3.a.getCount() - var8.getCount() > 1) {
                  this.k(var1, var2, var3);
                  return;
               }
            }

            for (int var9 = 0; var9 < var3.R.length; var9++) {
               ItemStack var10 = var3.R[var9];
               ItemStack var11 = var1.player.getInventory().getItem(var9);
               if (!var10.isEmpty() && (!j(var10, var11) || var11.getCount() < var10.getCount())) {
                  this.k(var1, var2, var3);
                  return;
               }
            }

            this.Q(var1, false);
         } else if (wm.d(var4, var3.i)) {
            int var5 = this.t(var4) == var3.C ? var3.e : F(var2, var1, var3.T);
            if (var5 >= 0 && z(var2.getSlot(var5), var4, var4.getCount())) {
               this.i(var1, var2, var5, 0);
            } else {
               for (Slot var7 : var2.slots) {
                  if (z(var7, var4, var4.getCount())) {
                     this.i(var1, var2, var7.index, 0);
                     return;
                  }
               }

               this.k(var1, var2, var3);
            }
         }
      }
   }

   private void k(Minecraft var1, AbstractContainerMenu var2, z8.v var3) {
      var3.f.clear();
      var3.V = (boolean)0;
      this.EX = -1;
      if (!var3.v) {
         var3.v = (boolean)1;
         xa.q("HigherLower", "Manual recovery required: clear cursor, restore baseline slots and return any source surplus");
      }

      if (var2 instanceof DispenserMenu && (var1.gui.screen() == null || var1.gui.screen() instanceof AbstractContainerScreen var4 && var4.getMenu() != var2)) {
         var1.gui.setScreen(new DispenserScreen((DispenserMenu)var2, var1.player.getInventory(), Component.literal("HigherLower recovery: " + var3.h)));
      }
   }

   private void N(String var1) {
      if (this.E_ != null && !this.E_.D) {
         this.E_.D = (boolean)1;
         this.E_.h = var1;
         xa.q("HigherLower", "Exchange failed: " + var1 + "; parking cursor without replay");
      }
   }

   @Override
   public void abortSwap() {
      this.N("cancelled");
   }

   private void Q(Minecraft var1, boolean var2) {
      z8.v var3 = this.E_;
      AbstractContainerMenu var4 = var1.player.containerMenu;
      if (var4.getCarried().isEmpty()) {
         if (var4 == var3.K) {
            var1.player.closeContainer();
         }

         if (var2) {
            xa.t("HigherLower", var3.H, var3.C, this.getTemplateIdForValue(var3.C));
            this.e(var3.C, var3.Z);
            this.Em = (boolean)1;
            if (var1.player.firstPersonHandsAndItems() instanceof ed var5) {
               var5.larpRig$releaseHeldMap(this, var1.player.getMainHandItem());
            }
         } else {
            this.E8 = this.E5 = -1;
            this.EC = ItemStack.EMPTY;
            this.EI = null;
            this.EX = -1;
            this.E2 = -1;
         }

         this.E_ = null;
         this.EN.clear();
         xa.q(
            "HigherLower",
            var2
               ? "One-card exchange confirmed (open=" + (var3.P - var3.l) / 1000000L + "ms, transfer=" + (System.nanoTime() - var3.P) / 1000000L + "ms)"
               : "Exchange stopped: " + var3.h
         );
      }
   }

   private void w(Minecraft var1, z8.v var2) {
      if (var1.gameMode != null && var1.getConnection() != null && !var1.player.isShiftKeyDown() && !var1.options.keyUse.isDown()) {
         BlockHitResult var3 = q(var1, var2.s);
         if (var3 != null) {
            var2.N = (boolean)1;
            this.q(var1, var3, var1x -> var2.g.j(var1x, var2.F));
         }
      }
   }

   @Override
   public void onBlockInteractionAcknowledged(int var1) {
      z8.v var2 = this.E_;
      if (var2 != null && var2.g.N()) {
         boolean var3 = var2.g.Z();
         var2.g.n(var1);
         if (!var3 && var2.g.Z()) {
            xa.i("HigherLower Dispenser interaction processed sync=" + var2.A + " " + var2.g.m());
         }

         this.t();
      }
   }

   private void t() {
      Minecraft var1 = Minecraft.getInstance();
      if (!this.Ex && !this.EH && this.E_ != null && var1.player != null && var1.level != null) {
         this.Ex = (boolean)1;

         try {
            this.y(var1);
         } finally {
            this.Ex = (boolean)0;
         }
      }
   }

   @Override
   public boolean isSwapActive() {
      return this.E_ != null;
   }

   @Override
   public boolean isSwapBusy() {
      return this.E_ != null;
   }

   @Override
   public boolean isSwapSuppressing() {
      return this.E_ != null && this.E_.N;
   }

   @Override
   public boolean shouldSuppressOpen(MenuType<?> var1) {
      return var1 == MenuType.GENERIC_3x3 && this.isSwapSuppressing();
   }

   @Override
   public void onSwapContainerOpened(MenuType<?> var1, int var2) {
      z8.v var3 = this.E_;
      if (var3 != null && var1 == MenuType.GENERIC_3x3) {
         int var4 = var3.g.N() && !var3.V && var3.d <= 0 ? 0 : 1;
         xa.i("HigherLower Dispenser opened sync=" + var2 + " " + var3.g.m());
         if (var4 != 0) {
            this.N("unsolicited or mid-transfer container replacement");
            var3.V = (boolean)0;
         }

         var3.A = var2;
         var3.P = System.nanoTime();
         var3.b = (boolean)0;
         var3.K = Minecraft.getInstance().player.containerMenu;
         var3.O.O(var3.K, -1, this.Eu);
         var3.I = 0;
         var3.k.clear();
         var3.i = ItemStack.EMPTY;
         if (var4 != 0) {
            Minecraft var5 = Minecraft.getInstance();
            this.k(var5, var5.player.containerMenu, var3);
         }
      }
   }

   @Override
   public int getSwapSyncId() {
      return this.E_ == null ? -1 : this.E_.A;
   }

   @Override
   public int getSwapValue() {
      return this.E_ == null ? this.E8 : this.E_.C;
   }

   @Override
   public int getSwapRealMapId() {
      return this.E_ == null ? -1 : I(this.E_.W);
   }

   @Override
   public boolean isSwapJuggling() {
      return this.E_ != null;
   }

   @Override
   public boolean isSwapFreezing() {
      return this.E_ != null;
   }

   @Override
   public boolean isSwapFreezingPassThrough() {
      return false;
   }

   @Override
   public boolean isDealBobAllowed(ItemStack var1) {
      return this.E_ == null;
   }

   @Override
   public boolean isHotbarBobAllowed(ItemStack var1) {
      return this.E_ == null;
   }

   @Override
   public ItemStack getVisualOverride(ItemStack var1) {
      return null;
   }

   @Override
   public ItemStack getInventorySlotProjection(int var1) {
      ItemStack var2 = w5.Q(var1);
      if (var2 != null) {
         return var2;
      } else {
         return this.E_ == null ? null : this.E_.f.get(var1);
      }
   }

   @Override
   public ItemStack projectSlot(int var1, ItemStack var2) {
      ItemStack var3 = this.getInventorySlotProjection(var1);
      return var3 == null ? var2 : var3;
   }

   private void Z(Minecraft var1) {
      int var2 = var1.player.getInventory().getSelectedSlot();
      if (var2 != this.EX) {
         this.E6++;
      }

      this.EX = var2;
      this.Em = (boolean)0;
      this.EP = this.projectSlot(var2, var1.player.getMainHandItem()).copy();
   }

   @Override
   public boolean isDealtHandLocked() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null || var1.player.getInventory().getSelectedSlot() != this.EX || this.Em && !j(var1.player.getMainHandItem(), this.EP)) {
         this.EX = -1;
      }

      return this.EX >= 0;
   }

   @Override
   public ItemStack getDealtHandVisual(ItemStack var1) {
      if (!this.isDealtHandLocked()) {
         return var1;
      } else {
         return this.Em ? var1 : this.projectSlot(this.EX, var1);
      }
   }

   @Override
   public long getDealtHandSequence() {
      return this.E6;
   }

   @Override
   public boolean isDealtHandSettled() {
      return this.Em;
   }

   @Override
   public void signalFramePlacement(ItemFrame var1) {
      this.EX = -1;
      if (Minecraft.getInstance().player.firstPersonHandsAndItems() instanceof ed var2) {
         var2.larpRig$releaseHeldMap(this, Minecraft.getInstance().player.getMainHandItem());
      }
   }

   private int v() {
      if (this.E8 >= 1 && this.Er != z8.c.w) {
         ArrayList var1 = new ArrayList();

         for (int var2 = 1; var2 <= 9; var2++) {
            int var3 = this.Er == z8.c.Q ? (var2 > this.E8 ? 1 : 0) : (var2 < this.E8 ? 1 : 0);
            if (var3 != 0 && this.getTemplateForValue(var2) != null && this.K(var2) >= 0) {
               var1.add(var2);
            }
         }

         if (!var1.isEmpty()) {
            return (Integer)var1.get(ThreadLocalRandom.current().nextInt(var1.size()));
         } else {
            return (this.Er == z8.c.Y && this.E8 == 1 || this.Er == z8.c.Q && this.E8 == 9) && this.K(this.E8) >= 0 ? this.E8 : -1;
         }
      } else {
         return -1;
      }
   }

   private int t(ItemStack var1) {
      int var2 = I(var1);
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

   private static int I(ItemStack var0) {
      MapId var1 = var0.is(Items.FILLED_MAP) ? (MapId)var0.get(DataComponents.MAP_ID) : null;
      return var1 == null ? -1 : var1.id();
   }

   private static boolean j(ItemStack var0, ItemStack var1) {
      return wm.u(var0, var1);
   }

   private static boolean u(ItemStack var0, ItemStack var1) {
      return !var0.is(Items.FILLED_MAP) && !var1.is(Items.FILLED_MAP) || wm.d(var0, var1);
   }

   private static String X(ItemStack var0) {
      return "map=" + I(var0) + "/count=" + var0.getCount() + "/filled=" + var0.is(Items.FILLED_MAP);
   }

   private static String X(z8.v var0) {
      return "step="
         + var0.d
         + " slot="
         + var0.c
         + " button="
         + var0.t
         + " sync="
         + var0.A
         + " revision="
         + var0.j
         + " probe="
         + var0.X
         + " ack["
         + var0.O.a()
         + "] slotSource="
         + var0.u
         + " cursorSource="
         + var0.z
         + " lastPacket=["
         + var0.Q
         + "] expectedSlot="
         + X(var0.G)
         + " receivedSlot="
         + X(var0.k.get(var0.c))
         + " expectedCursor="
         + X(var0.L)
         + " receivedCursor="
         + X(var0.i)
         + (var0.J < 0 ? "" : " batchSource=" + var0.J + " expectedSource=" + X(var0.m) + " receivedSource=" + X(var0.k.get(var0.J)))
         + " slotIdentity="
         + wm.d(var0.k.get(var0.c), var0.G)
         + " cursorIdentity="
         + wm.d(var0.i, var0.L)
         + " slotComponentsExact="
         + ItemStack.isSameItemSameComponents(var0.k.get(var0.c), var0.G)
         + " cursorComponentsExact="
         + ItemStack.isSameItemSameComponents(var0.i, var0.L);
   }

   private static ItemStack[] D(Minecraft var0) {
      ItemStack[] var1 = new ItemStack[36];

      for (int var2 = 0; var2 < var1.length; var2++) {
         var1[var2] = var0.player.getInventory().getItem(var2).copy();
      }

      return var1;
   }

   private static Map<Integer, Integer> L(ItemStack[] var0) {
      HashMap var1 = new HashMap();

      for (ItemStack var5 : var0) {
         if (I(var5) >= 0) {
            var1.merge(I(var5), var5.getCount(), Integer::sum);
         }
      }

      return var1;
   }

   private static Map<Integer, Integer> o(Minecraft var0) {
      Map var1 = L(D(var0));
      ItemStack var2 = var0.player.containerMenu.getCarried();
      if (I(var2) >= 0) {
         var1.merge(I(var2), var2.getCount(), Integer::sum);
      }

      if (var0.level != null) {
         for (Entity var4 : var0.level.entitiesForRendering()) {
            if (var4 instanceof ItemFrame var5) {
               ItemStack var6 = var5.getItem();
               if (I(var6) >= 0) {
                  var1.merge(I(var6), var6.getCount(), Integer::sum);
               }
            }
         }
      }

      return var1;
   }

   private static int F(AbstractContainerMenu var0, Minecraft var1, int var2) {
      for (Slot var4 : var0.slots) {
         if (var4.container == var1.player.getInventory() && var4.getContainerSlot() == var2) {
            return var4.index;
         }
      }

      return -1;
   }

   private static boolean z(Slot var0, ItemStack var1, int var2) {
      ItemStack var3 = var0.getItem();
      return !var1.isEmpty()
         && var0.mayPlace(var1)
         && (var3.isEmpty() || j(var3, var1))
         && var3.getCount() + var2 <= Math.min(var0.getMaxStackSize(var1), var1.getMaxStackSize());
   }

   public enum c {
      w("off"),
      Q("higher"),
      Y("lower");

      private final String c;

      c(String var3) {
         this.c = var3;
      }

      public String p() {
         return this.c;
      }
   }

   private static final class v {
      final long l = System.nanoTime();
      long P;
      final String x;
      final BlockPos s;
      final z8.c U;
      final int w;
      final int C;
      final int H;
      final int T;
      final int Z;
      final ItemStack[] R;
      final ItemStack W;
      final Map<Integer, Integer> y;
      final Map<Integer, ItemStack> f = new HashMap<>();
      int A = -1;
      int e = -1;
      int q = -1;
      int d;
      int F;
      int I;
      int c = -1;
      final _n g = new _n();
      boolean N;
      boolean b;
      boolean V;
      boolean D;
      boolean v;
      String h;
      List<ItemStack> k = new ArrayList<>();
      ItemStack i = ItemStack.EMPTY;
      ItemStack G = ItemStack.EMPTY;
      ItemStack L = ItemStack.EMPTY;
      int J = -1;
      ItemStack m = ItemStack.EMPTY;
      ItemStack a = ItemStack.EMPTY;
      ItemStack E = ItemStack.EMPTY;
      final w3 O = new w3();
      AbstractContainerMenu K;
      int t;
      int j;
      int X;
      String Q = "none";
      String u = "none";
      String z = "none";

      v(String var1, BlockPos var2, z8.c var3, int var4, int var5, int var6, int var7, int var8, ItemStack[] var9, ItemStack var10, Map<Integer, Integer> var11) {
         this.x = var1;
         this.s = var2;
         this.U = var3;
         this.w = var4;
         this.C = var5;
         this.H = var6;
         this.T = var7;
         this.Z = var8;
         this.R = var9;
         this.W = var10.copyWithCount(1);
         this.y = new HashMap<>(var11);
      }
   }
}
