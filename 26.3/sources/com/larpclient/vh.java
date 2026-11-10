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
public final class vh extends vy {
   private static final vh Fg = new vh();
   private final Map<Integer, Integer> FI = new HashMap<>();
   private final Map<Integer, ItemStack> Fm = new HashMap<>();
   private ItemStack[] Ft;
   private Map<Integer, Integer> FH;
   private vh.i FT = vh.i.a;
   private vh.i FZ = vh.i.a;
   private int F7 = -1;
   private int Fl = -1;
   private int Fd = -1;
   private ItemStack Fn = ItemStack.EMPTY;
   private int F_ = -1;
   private int F9;
   private int FD;
   private boolean FR;
   private String FX;
   private BlockPos Fy;
   private int F4 = -1;
   private ClientLevel FN;
   private long FA = -1L;
   private vh.d Fq;
   private int FG = -1;
   private ItemStack Fe;
   private boolean Fc;
   private long FQ;
   private boolean F2;
   private boolean Fk;
   private long FJ;
   private String FS = "no map change";

   private vh() {
   }

   public static vh R() {
      return Fg;
   }

   public vh.i w() {
      return this.FT;
   }

   public String G() {
      return this.FT.i();
   }

   public int E() {
      return this.F7;
   }

   public int Z() {
      return this.F_;
   }

   public boolean B() {
      return this.FR;
   }

   public int V(int var1) {
      return this.FI.getOrDefault(this.getTemplateIdForValue(var1), 0);
   }

   public void x(String var1) {
      this.FT = "higher".equalsIgnoreCase(var1) ? vh.i.t : ("lower".equalsIgnoreCase(var1) ? vh.i.b : vh.i.a);
      if (this.FT == vh.i.a) {
         this.o();
      }
   }

   public void Z() {
      this.x(switch (this.FT) {
         case a -> "higher";
         case t -> "lower";
         case b -> "off";
      });
   }

   public void o() {
      this.clearQueue();
      this.F7 = this.Fl = this.Fd = -1;
      this.Fn = ItemStack.EMPTY;
      this.FH = null;
      this.FD = 0;
   }

   @Override
   public void clearQueue() {
      this.FR = (boolean)0;
      this.F_ = -1;
      if (this.Fq != null) {
         this.abortSwap();
      } else {
         this.FI.clear();
      }
   }

   @Override
   protected boolean supportsPhysicalSwap() {
      return false;
   }

   @Override
   protected BlockPos getAssignedDispenserPosition(Minecraft var1) {
      hk var2 = hk.e();
      return var2.A(hk.k.b).contains(this.w()) ? var2.Q(this.w()) : null;
   }

   @Override
   public boolean hasQueuedForce() {
      return this.FR && this.F_ > 0;
   }

   @Override
   public int getPendingValue() {
      return this.hasQueuedForce() ? this.F_ : -1;
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
      if (!Objects.equals(var1, this.w())) {
         if (this.w() != null) {
            this.o();
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
            int var6 = this.C(var5);
            if (var6 > 0) {
               this.Fm.put(var6, var5.copyWithCount(1));
            }
         }
      }

      return var2;
   }

   public void d(Minecraft var1) {
      this.s(var1);
      if (var1 != null && var1.player != null && com.larpclient.k.D().h && this.FT != vh.i.a && !this.FR && !this.isSwapBusy()) {
         vy var2 = vy.a();
         if (var2 == this || !var2.isSwapBusy()) {
            this.Ft = E(var1);
            if (this.F7 > 0) {
               int var3 = this.Fl >= 0 && this.Fl < this.Ft.length && !this.Fn.isEmpty() && S(this.Fn, this.Ft[this.Fl]) ? this.Fl : -1;
               if (var3 < 0) {
                  for (int var4 = 0; var4 < this.Ft.length; var4++) {
                     if (!this.Fn.isEmpty() && S(this.Fn, this.Ft[var4])) {
                        if (var3 >= 0) {
                           this.clearQueue();
                           bn.j("HigherLower", "Activation disarmed: moved reference stack is ambiguous");
                           return;
                        }

                        var3 = var4;
                     }
                  }
               }

               if (var3 < 0) {
                  this.o();
                  bn.j("HigherLower", "Previous reference removed; waiting for first card of new round");
               } else {
                  this.Fl = var3;
               }
            }

            this.FI.clear();
            this.FI.putAll(s(this.Ft));
            if (this.F7 < 1 && this.FH == null) {
               this.FH = W(var1);
            }

            this.Fd = this.F7;
            this.FZ = this.FT;
            this.FX = this.w();
            BlockPos var5 = this.getAssignedDispenserPosition(var1);
            this.Fy = var5 == null ? null : var5.immutable();
            this.F_ = this.J();
            this.F4 = this.n(this.F_);
            this.FR = (boolean)1;
            this.F9 = 0;
            this.FS = "no map change";
            bn.b("HigherLower Activation target=" + this.FZ.i() + " reference=" + this.Fd + " recommendation=" + this.F_);
         }
      }
   }

   @Override
   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      this.s(Minecraft.getInstance());
   }

   private void s(Minecraft var1) {
      if (this.FR && this.Fq == null && var1 != null && var1.player != null) {
         ItemStack[] var2 = E(var1);
         Map var3 = s(var2);
         int var4 = -1;
         int var5 = -1;

         for (int var6 = 0; var6 < var2.length; var6++) {
            if (!n(var2[var6], this.Ft[var6])) {
               ItemStack var7 = this.Ft[var6];
               ItemStack var8 = var2[var6];
               int var9 = this.C(var8);
               if (var4 >= 0 || var9 < 1 || !var7.isEmpty() && !S(var7, var8) || var8.getCount() != var7.getCount() + 1) {
                  this.FS = "slot="
                     + var6
                     + " reason="
                     + (
                        var4 >= 0
                           ? "multiple map changes"
                           : (var9 < 1 ? "unrecognized or removed map" : (!var7.isEmpty() && !S(var7, var8) ? "origin/components changed" : "not +1"))
                     )
                     + " before="
                     + f(var7)
                     + " after="
                     + f(var8);
                  return;
               }

               var4 = var6;
               var5 = var9;
            }
         }

         if (var4 >= 0 && var3.getOrDefault(E(var2[var4]), 0) == this.FI.getOrDefault(E(var2[var4]), 0) + 1) {
            int var12 = this.FZ == vh.i.t ? (var5 > this.Fd ? 1 : 0) : (var5 < this.Fd ? 1 : 0);
            if (this.Fd < 1 || this.F_ < 1 || var12 != 0 || var5 == this.F_) {
               this.j(var5, var4);
            } else if (qm.M(var1) && qm.v(this, var4, this.F_)) {
               int var14 = this.F_;
               this.x(var14, var4, var5, true);
            } else {
               int var13 = -1;
               int var15 = this.F_ == this.Fd ? 1 : 0;
               if (var15 != 0 && this.Fl >= 0 && this.C(this.Ft[this.Fl]) == this.F_) {
                  var13 = this.Fl;
               } else if (var15 == 0) {
                  int var16 = this.Fl >= 0 && this.Fl < 9 ? this.Fl : var1.player.getInventory().getSelectedSlot();

                  for (int var10 = 1; var10 <= 9; var10++) {
                     int var11 = (var16 + var10) % 9;
                     if (this.Ft[var11].isEmpty() && (var2[var11].isEmpty() || var11 == var4)) {
                        var13 = var11;
                        break;
                     }
                  }
               }

               ItemStack var17 = this.Fm.get(this.F_);
               if (var15 != 0 && var13 >= 0) {
                  var17 = this.Ft[var13].copyWithCount(1);
               }

               if (var17 != null
                  && E(var17) == this.getTemplateIdForValue(this.F_)
                  && var13 >= 0
                  && this.Fy != null
                  && (var15 == 0 || S(this.Ft[var13], var17) && this.Ft[var13].getCount() < var17.getMaxStackSize())) {
                  vh.d var18 = new vh.d(this.FX, this.Fy, this.FZ, this.Fd, this.F_, var5, var4, var13, this.Ft, var2[var4], this.FI);
                  var18.s = this.F4;
                  var18.U.merge(this.getTemplateIdForValue(var18.l), 1, Integer::sum);
                  var18.q.put(var4, var18.t[var4].copy());
                  var18.q.put(var13, var17.copyWithCount(var18.t[var13].getCount() + 1));
                  this.Fq = var18;
                  var18.V = 1;
                  this.FR = (boolean)0;
                  this.l(var1);
                  bn.j(
                     "HigherLower",
                     "One-card exchange real="
                        + var5
                        + " ->"
                        + var18.l
                        + " target="
                        + var18.K.i()
                        + " reference="
                        + var18.Y
                        + " arrival="
                        + var4
                        + " destination="
                        + var13
                  );
                  this.I();
               } else {
                  bn.j("HigherLower", "Exchange unavailable: destination, components, capacity or assignment");
                  this.j(var5, var4);
               }
            }
         } else {
            this.FS = "reason=no +1 map total arrival=" + var4;
         }
      }
   }

   private void j(int var1, int var2) {
      this.x(var1, var2, var1, false);
   }

   private void x(int var1, int var2, int var3, boolean var4) {
      if (this.Fq == null) {
         bn.j("HigherLower", bn.W(Integer.toString(var3), Integer.toString(var1), this.Fd > 0 && this.F_ > 0, var4));
      }

      this.F7 = var1;
      this.Fl = var2;
      this.Fn = Minecraft.getInstance().player.getInventory().getItem(var2).copyWithCount(1);
      this.FR = (boolean)0;
      this.F_ = -1;
      this.FI.clear();
   }

   @Override
   public void tick(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         if (this.FN != null && this.FN != var1.level) {
            this.Fq = null;
            this.FG = -1;
            this.o();
         }

         if (this.FN != var1.level || this.FA != var1.level.getGameTime()) {
            this.FN = var1.level;
            this.FA = var1.level.getGameTime();
            if (this.Fq != null) {
               this.Fq.V++;
               this.w(var1);
            } else if (com.larpclient.k.D().h) {
               this.s(var1);
               if (this.FR && ++this.F9 > 100) {
                  bn.j("HigherLower", "Draw timed out: " + this.FS + " baselineMapCounts=" + this.FI + " receivedMapCounts=" + s(E(var1)));
                  this.clearQueue();
               }

               if (this.Fq == null && !this.FR && this.F7 > 0 && this.FH != null) {
                  Map var2 = W(var1);
                  int var3 = 0;

                  for (Entry var5 : var2.entrySet()) {
                     var3 += Math.max(0, (Integer)var5.getValue() - this.FH.getOrDefault(var5.getKey(), 0));
                  }

                  this.FD = var3 == 0 ? this.FD + 1 : 0;
                  if (this.FD >= 6) {
                     this.o();
                  }
               }
            }
         }
      } else {
         this.Fq = null;
         this.FG = -1;
         this.o();
         this.FN = null;
      }
   }

   private void w(Minecraft var1) {
      vh.d var2 = this.Fq;
      if (!var2.N && var2.a.B(var2.h) || !Objects.equals(var2.A, this.w()) || !var2.G.equals(this.getAssignedDispenserPosition(var1))) {
         this.a("timeout or assignment changed step=" + var2.D + " sync=" + var2.R + " ready=" + var2.x + " " + var2.F.G());
      }

      AbstractContainerMenu var3 = var1.player.containerMenu;
      if (var2.R < 0) {
         if (var2.V >= 100) {
            this.a("dispenser did not open; no cards moved");
         }

         if (var2.J) {
            if (!var3.getCarried().isEmpty()) {
               var2.q.clear();
               this.FG = -1;
            }

            this.a(var1, false);
         } else {
            if (var1.gui.screen() == null && var3 == var1.player.inventoryMenu && var3.getCarried().isEmpty() && var2.F.n(var2.V)) {
               this.P(var1, var2);
            }
         }
      } else if (!(var3 instanceof DispenserMenu) || var3 != var2.H || var3.containerId != var2.R) {
         this.a("handler replaced");
         var2.q.clear();
         this.FG = -1;
         this.a(var1, false);
      } else if (!var2.r) {
         if (var2.x && var1.gameMode != null) {
            if (var2.J || var2.F.y()) {
               if (var2.N) {
                  if (!var2.a.q(var2.i < 0 || je.F(var2.p.get(var2.i), var2.Q), je.F(var2.p.get(var2.W), var2.P), je.F(var2.I, var2.m))) {
                     if (var2.a.y()) {
                        this.a("click not confirmed; no replay " + y(var2));
                        this.f(var1, var3, var2);
                     }

                     return;
                  }

                  bn.b("HigherLower Click confirmed " + y(var2));
                  var2.N = (boolean)0;
                  if (!var2.J && var2.D == 3) {
                     var2.q.put(var2.z, var2.p.get(var2.W).copy());
                  }

                  var2.X = 0;
                  if (!var2.J) {
                     var2.D += 3;
                  }
               }

               if (var2.J) {
                  this.W(var1, var3, var2);
               } else {
                  int var4 = S(var3, var1, var2.C);
                  int var5 = S(var3, var1, var2.z);
                  if (var4 >= 0 && var5 >= 0) {
                     if (var2.D == 0) {
                        if (!var3.getCarried().isEmpty()) {
                           this.a("occupied cursor");
                           return;
                        }

                        for (int var6 = 0; var6 < var2.t.length; var6++) {
                           ItemStack var7 = var6 == var2.C ? var2.L.copyWithCount(var2.t[var6].getCount() + 1) : var2.t[var6];
                           ItemStack var8 = var1.player.getInventory().getItem(var6);
                           if (var6 != var2.C && var6 != var2.z ? !n(var8, var7) : !je.F(var8, var7)) {
                              this.a("inventory changed before extraction slot=" + var6 + " expected=" + f(var7) + " received=" + f(var8));
                              return;
                           }
                        }

                        if (var2.f < 0) {
                           for (int var11 = 0; var11 < 9; var11++) {
                              if (var11 != var2.s
                                 && ItemStack.isSameItemSameComponents(var3.getSlot(var11).getItem(), var3.getSlot(var4).getItem())
                                 && ItemStack.isSameItemSameComponents(var2.p.get(var11), var2.p.get(var4))
                                 && r(var3.getSlot(var11), var2.L, 1)) {
                                 var2.f = var11;
                                 break;
                              }
                           }

                           for (int var12 = 0; var12 < 9; var12++) {
                              if (var2.f < 0
                                 && var12 != var2.s
                                 && var3.getSlot(var12).getItem().isEmpty()
                                 && var2.p.get(var12).isEmpty()
                                 && r(var3.getSlot(var12), var2.L, 1)) {
                                 var2.f = var12;
                                 break;
                              }
                           }
                        }

                        if (var2.s < 0
                           || var2.s >= 9
                           || var2.f < 0
                           || this.C(var3.getSlot(var2.s).getItem()) != var2.l
                           || !var3.getSlot(var2.s).mayPickup(var1.player)
                           || !var3.getSlot(var5).mayPlace(var3.getSlot(var2.s).getItem())
                           || !var2.t[var2.z].isEmpty() && !S(var2.t[var2.z], var3.getSlot(var2.s).getItem())
                           || var2.t[var2.z].getCount() + 1 > var3.getSlot(var5).getMaxStackSize(var3.getSlot(var2.s).getItem())
                           || !var3.getSlot(var4).mayPickup(var1.player)) {
                           this.a("source, return space, or destination unavailable");
                           return;
                        }

                        ItemStack var13 = var3.getSlot(var2.s).getItem();
                        if (!var2.t[var2.z].isEmpty()
                           && (
                              !ItemStack.isSameItemSameComponents(var13, var3.getSlot(var5).getItem())
                                 || !ItemStack.isSameItemSameComponents(var2.p.get(var2.s), var2.p.get(var5))
                           )) {
                           this.a("replacement destination has incompatible vanilla components");
                           return;
                        }

                        var2.c = var13.copy();
                        var2.y = var3.getSlot(var2.f).getItem().copy();
                        var2.q.put(var2.z, var13.copyWithCount(var2.t[var2.z].getCount() + 1));
                        this.w(var1, var3, var4, var2.f);
                     } else if (var2.D == 3) {
                        if (!var3.getCarried().isEmpty()
                           || !je.F(var3.getSlot(var2.s).getItem(), var2.c)
                           || !je.F(var3.getSlot(var4).getItem(), var2.t[var2.C])
                           || !je.F(var3.getSlot(var5).getItem(), var2.t[var2.z])
                           || !r(var3.getSlot(var5), var3.getSlot(var2.s).getItem(), 1)) {
                           this.a("replacement source changed");
                           return;
                        }

                        this.w(var1, var3, var2.s, var5);
                     } else {
                        int var14 = var2.I.isEmpty()
                              && var3.getCarried().isEmpty()
                              && var2.U.equals(s(E(var1)))
                              && je.F(var2.p.get(var2.s), var2.c.copyWithCount(var2.c.getCount() - 1))
                              && je.F(var2.p.get(var2.f), var2.L.copyWithCount(var2.y.getCount() + 1))
                           ? 1
                           : 0;

                        for (int var15 = 0; var15 < var2.t.length && var14 != 0; var15++) {
                           int var16 = S(var3, var1, var15);
                           ItemStack var9 = var15 == var2.z ? var2.q.get(var15) : var2.t[var15];
                           int var10 = var15 != var2.C && var15 != var2.z ? 0 : 1;
                           var14 = var16 < 0
                                 || (
                                    var10 != 0
                                       ? !je.F(var2.p.get(var16), var9) || !je.F(var1.player.getInventory().getItem(var15), var9)
                                       : !n(var2.p.get(var16), var9) || !n(var1.player.getInventory().getItem(var15), var9)
                                 )
                              ? 0
                              : 1;
                           if (var14 == 0) {
                              this.a(
                                 "final slot="
                                    + var15
                                    + " expected="
                                    + f(var9)
                                    + " server="
                                    + (var16 < 0 ? "missing" : f(var2.p.get(var16)))
                                    + " local="
                                    + f(var1.player.getInventory().getItem(var15))
                              );
                           }
                        }

                        if (var14 == 0) {
                           this.a(
                              "final quantity ledger mismatch expectedCounts="
                                 + var2.U
                                 + " receivedCounts="
                                 + s(E(var1))
                                 + " source="
                                 + f(var2.p.get(var2.s))
                                 + " expectedSource="
                                 + f(var2.c.copyWithCount(var2.c.getCount() - 1))
                                 + " deposit="
                                 + f(var2.p.get(var2.f))
                                 + " expectedDeposit="
                                 + f(var2.L.copyWithCount(var2.y.getCount() + 1))
                           );
                           return;
                        }

                        this.a(var1, true);
                     }
                  } else {
                     this.a("player slots missing");
                  }
               }
            }
         } else {
            if (var2.J) {
               this.f(var1, var3, var2);
            }
         }
      }
   }

   private void w(Minecraft var1, AbstractContainerMenu var2, int var3, int var4) {
      vh.d var5 = this.Fq;
      if (!var5.N && !var5.J && !var5.r) {
         if (var5.x && var2 == var5.H && var5.p.size() == var2.slots.size() && var5.I.isEmpty() && var2.getCarried().isEmpty()) {
            for (int var6 = 0; var6 < var5.p.size(); var6++) {
               if (!je.F(var2.getSlot(var6).getItem(), var5.p.get(var6))) {
                  this.a("pre-batch local/server mismatch slot=" + var6);
                  this.f(var1, var2, var5);
                  return;
               }
            }

            Slot var26 = var2.getSlot(var3);
            Slot var7 = var2.getSlot(var4);
            ItemStack var8 = var26.getItem().copy();
            ItemStack var9 = var7.getItem().copy();
            ItemStack var10 = var5.p.get(var3);
            ItemStack var11 = var5.p.get(var4);
            int var12 = !var9.isEmpty() && !ItemStack.isSameItemSameComponents(var8, var9)
                  || !var11.isEmpty() && !ItemStack.isSameItemSameComponents(var10, var11)
               ? 0
               : 1;
            hv.f var13 = hv.batch(
               var3,
               var4,
               var10.getCount(),
               var11.getCount(),
               (boolean)var12,
               Math.min(var26.getMaxStackSize(var10), var10.getMaxStackSize()),
               Math.min(var7.getMaxStackSize(var10), var10.getMaxStackSize())
            );
            hv.f var14 = hv.batch(
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
               var5.i = var3;
               var5.Q = var10.copyWithCount(var13.returned().slotCount());
               var5.W = var4;
               var5.P = (var11.isEmpty() ? var10 : var11).copyWithCount(var13.placement().slotCount());
               var5.m = ItemStack.EMPTY;
               var5.N = (boolean)1;
               var5.X = 0;
               ClientPacketListener var15 = var1.getConnection();
               PlayerInfo var16 = var15 == null ? null : var15.getPlayerInfo(var1.player.getUUID());
               var5.a.o(var16 == null ? 0 : var16.getLatency());
               var5.a.c(var2, var4, var3, this.FJ);
               var5.O = var5.o = var5.u = "none";

               for (int var17 = 0; var17 < var13.clicks(); var17++) {
                  int var18 = var17 == 1 ? var4 : var3;
                  int var19 = var17 == 0 ? var13.pickupButton() : (var17 == 1 ? 1 : 0);
                  var5.T = var19;
                  this.F2 = (boolean)(var17 == var13.clicks() - 1 ? 1 : 0);

                  try {
                     var1.gameMode.handleContainerInput(var2.containerId, var18, var19, ContainerInput.PICKUP, var1.player);
                  } finally {
                     this.F2 = (boolean)0;
                  }

                  int var20 = var17 == 2 ? var13.returned().slotCount() : var13.pickup().slotCount();
                  int var21 = var17 == 0 ? var9.getCount() : var13.placement().slotCount();
                  int var22 = var17 == 0 ? var13.pickup().cursorCount() : (var17 == 1 ? var13.placement().cursorCount() : 0);
                  ItemStack var23 = (var9.isEmpty() ? var8 : var9).copyWithCount(var21);
                  if (!ItemStack.matches(var26.getItem(), var8.copyWithCount(var20))
                     || !ItemStack.matches(var7.getItem(), var23)
                     || !ItemStack.matches(var2.getCarried(), var8.copyWithCount(var22))) {
                     this.a("batch prediction diverged after click=" + var17 + "; no replay");
                     this.f(var1, var2, var5);
                     return;
                  }
               }

               bn.b("HigherLower Batch issued clicks=" + var13.clicks() + " source=" + var3 + " " + y(var5));
            } else {
               this.a("unsafe batch: source, capacity or vanilla component merge mismatch");
            }
         } else {
            this.a("batch requires known handler and empty authoritative cursor");
         }
      }
   }

   private void e(Minecraft var1, AbstractContainerMenu var2, int var3, int var4) {
      vh.d var5 = this.Fq;
      var5.i = -1;
      if (je.F(var2.getSlot(var3).getItem(), var5.p.get(var3)) && je.F(var2.getCarried(), var5.I)) {
         ItemStack var6 = var5.p.get(var3);
         ItemStack var7 = var5.I;
         hv var8 = hv.plan(
            var6.getCount(),
            var7.getCount(),
            var4,
            var7.isEmpty() || r(var2.getSlot(var3), var7, var4 == 1 ? 1 : var7.getCount()),
            var7.isEmpty() ? 0 : Math.min(var2.getSlot(var3).getMaxStackSize(var7), var7.getMaxStackSize())
         );
         if (var8 == null) {
            this.a("transfer unavailable before click slot=" + var3);
         } else {
            var5.P = (var6.isEmpty() ? var7 : var6).copyWithCount(var8.slotCount());
            var5.m = (var7.isEmpty() ? var6 : var7).copyWithCount(var8.cursorCount());
            var5.N = (boolean)1;
            var5.W = var3;
            var5.T = var4;
            var5.X = 0;
            var5.a.U(var2, var3, this.FJ);
            var5.O = var5.o = var5.u = "none";
            this.F2 = (boolean)1;

            try {
               var1.gameMode.handleContainerInput(var2.containerId, var3, var4, ContainerInput.PICKUP, var1.player);
            } finally {
               this.F2 = (boolean)0;
            }

            bn.b("HigherLower Click issued " + y(var5));
            if (je.F(var5.P, var5.p.get(var3)) || je.F(var5.m, var5.I)) {
               this.a("issued pickup did not change both slot and cursor; no replay");
               this.f(var1, var2, var5);
            }
         }
      } else {
         this.a(
            "pre-click local/server mismatch step="
               + var5.D
               + " slot="
               + var3
               + " localSlot="
               + f(var2.getSlot(var3).getItem())
               + " serverSlot="
               + f(var5.p.get(var3))
               + " localCursor="
               + f(var2.getCarried())
               + " serverCursor="
               + f(var5.I)
         );
         this.f(var1, var2, var5);
      }
   }

   @Override
   public void onInventorySynchronized(int var1, List<ItemStack> var2, ItemStack var3) {
      long var4 = ++this.FJ;
      Minecraft var6 = Minecraft.getInstance();
      vh.d var7 = this.Fq;
      if (var7 != null) {
         var7.O = "full seq=" + var4 + " sync=" + var1 + " size=" + var2.size();
      }

      if (var7 != null && var6.player != null && var1 == var7.R && var6.player.containerMenu == var7.H && var2.size() == var7.H.slots.size()) {
         var7.p.clear();

         for (ItemStack var9 : var2) {
            var7.p.add(var9.copy());
         }

         var7.I = var3.copy();
         var7.o = var7.u = "full";
         var7.x = (boolean)1;
         var7.F.D();
         if (var7.N) {
            var7.a.X(var7.H, var4, true, -1, true);
         }
      }

      this.s(var6);
      this.I();
   }

   @Override
   public void onServerSlot(int var1, int var2, ItemStack var3) {
      long var4 = ++this.FJ;
      Minecraft var6 = Minecraft.getInstance();
      vh.d var7 = this.Fq;
      if (var7 != null) {
         var7.O = "slot seq=" + var4 + " sync=" + var1 + " slot=" + var2 + " " + f(var3);
      }

      if (var7 != null && var7.x && var6.player != null && var6.player.containerMenu == var7.H) {
         if (var1 == -1 && var2 == -1) {
            var7.I = var3.copy();
            var7.u = "cursor-packet";
            if (var7.N) {
               var7.a.X(var7.H, var4, false, -1, true);
            }
         } else if (var1 == var7.R && var2 >= 0 && var2 < var7.p.size()) {
            var7.p.set(var2, var3.copy());
            if (var2 == var7.W) {
               var7.o = "handler-slot";
            }

            if (var7.N) {
               var7.a.X(var7.H, var4, false, var2, false);
            }
         } else if (var1 == 0 && var2 >= 0 && var2 < var6.player.inventoryMenu.slots.size()) {
            Slot var8 = var6.player.inventoryMenu.getSlot(var2);
            int var9 = var8.container == var6.player.getInventory() ? S(var7.H, var6, var8.getContainerSlot()) : -1;
            if (var9 >= 0 && var9 < var7.p.size()) {
               var7.p.set(var9, var3.copy());
               if (var9 == var7.W) {
                  var7.o = "sync0-slot";
               }

               if (var7.N) {
                  var7.a.X(var7.H, var4, false, var9, false);
               }
            }
         }
      }

      this.s(Minecraft.getInstance());
      this.I();
   }

   @Override
   public void onPlayerInventorySlot(int var1, ItemStack var2) {
      long var3 = ++this.FJ;
      Minecraft var5 = Minecraft.getInstance();
      vh.d var6 = this.Fq;
      if (var6 != null) {
         var6.O = "player-inventory seq=" + var3 + " index=" + var1 + " " + f(var2);
      }

      if (var6 != null && var6.x && var5.player != null && var5.player.containerMenu == var6.H) {
         int var7 = S(var5.player.containerMenu, var5, var1);
         if (var7 >= 0 && var7 < var6.p.size()) {
            var6.p.set(var7, var2.copy());
            if (var7 == var6.W) {
               var6.o = "player-inventory";
            }

            if (var6.N) {
               var6.a.X(var6.H, var3, false, var7, false);
            }
         }
      }

      this.s(var5);
      this.I();
   }

   @Override
   public boolean hidesCursor(AbstractContainerMenu var1) {
      return this.Fq != null && var1.containerId == this.Fq.R && !this.Fq.q.isEmpty();
   }

   @Override
   public boolean blocksUserInteraction() {
      return this.Fq != null && !this.Fq.q.isEmpty();
   }

   @Override
   public int clickRevision(AbstractContainerMenu var1) {
      int var2 = var1.getStateId();
      vh.d var3 = this.Fq;
      if (var3 != null && var1 == var3.H && (this.F2 || var3.r)) {
         char var4 = '耀';
         if (this.F2) {
            var3.b = var2;
            var3.M = var4;
         }

         return var4;
      } else {
         return var2;
      }
   }

   @Override
   public boolean blocksClose() {
      vh.d var1 = this.Fq;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 == null || var2.player == null) {
         return false;
      }

      if (var1.r) {
         return false;
      }

      AbstractContainerMenu var3 = var2.player.containerMenu;
      return var3.containerId == var1.R && (var1.N || !var1.x || !var3.getCarried().isEmpty() || !var1.I.isEmpty());
   }

   @Override
   public boolean allowsRecoveryOpen(BlockPos var1) {
      return this.Fq != null && this.Fq.R < 0 && this.Fq.G.equals(var1);
   }

   public boolean M(BlockPos var1) {
      return this.Fq == null || this.Fq.R < 0 && this.Fq.G.equals(var1);
   }

   private void W(Minecraft var1, AbstractContainerMenu var2, vh.d var3) {
      if (!var3.r) {
         ItemStack var4 = var2.getCarried();
         if (var4.isEmpty() && var3.I.isEmpty()) {
            if (!var3.c.isEmpty()) {
               ItemStack var8 = var2.getSlot(var3.s).getItem();
               if (!var8.isEmpty() && !S(var8, var3.c) || var3.c.getCount() - var8.getCount() > 1) {
                  this.f(var1, var2, var3);
                  return;
               }
            }

            for (int var9 = 0; var9 < var3.t.length; var9++) {
               ItemStack var10 = var3.t[var9];
               ItemStack var11 = var1.player.getInventory().getItem(var9);
               if (!var10.isEmpty() && (!S(var10, var11) || var11.getCount() < var10.getCount())) {
                  this.f(var1, var2, var3);
                  return;
               }
            }

            this.a(var1, false);
         } else if (je.F(var4, var3.I)) {
            int var5 = this.C(var4) == var3.l ? var3.s : S(var2, var1, var3.C);
            if (var5 >= 0 && r(var2.getSlot(var5), var4, var4.getCount())) {
               this.e(var1, var2, var5, 0);
            } else {
               for (Slot var7 : var2.slots) {
                  if (r(var7, var4, var4.getCount())) {
                     this.e(var1, var2, var7.index, 0);
                     return;
                  }
               }

               this.f(var1, var2, var3);
            }
         }
      }
   }

   private void f(Minecraft var1, AbstractContainerMenu var2, vh.d var3) {
      var3.q.clear();
      var3.N = (boolean)0;
      this.FG = -1;
      if (!var3.r) {
         var3.r = (boolean)1;
         bn.j("HigherLower", "Manual recovery required: clear cursor, restore baseline slots and return any source surplus");
      }

      if (var2 instanceof DispenserMenu && (var1.gui.screen() == null || var1.gui.screen() instanceof AbstractContainerScreen var4 && var4.getMenu() != var2)) {
         var1.gui.setScreen(new DispenserScreen((DispenserMenu)var2, var1.player.getInventory(), Component.literal("HigherLower recovery: " + var3.w)));
      }
   }

   private void a(String var1) {
      if (this.Fq != null && !this.Fq.J) {
         this.Fq.J = (boolean)1;
         this.Fq.w = var1;
         bn.j("HigherLower", "Exchange failed: " + var1 + "; parking cursor without replay");
      }
   }

   @Override
   public void abortSwap() {
      this.a("cancelled");
   }

   private void a(Minecraft var1, boolean var2) {
      vh.d var3 = this.Fq;
      AbstractContainerMenu var4 = var1.player.containerMenu;
      if (var4.getCarried().isEmpty()) {
         if (var4 == var3.H) {
            var1.player.closeContainer();
         }

         if (var2) {
            bn.I("HigherLower", var3.S, var3.l, this.getTemplateIdForValue(var3.l));
            this.j(var3.l, var3.z);
            this.Fc = (boolean)1;
            if (var1.player.firstPersonHandsAndItems() instanceof q2 var5) {
               var5.larpRig$releaseHeldMap(this, var1.player.getMainHandItem());
            }
         } else {
            this.F7 = this.Fl = -1;
            this.Fn = ItemStack.EMPTY;
            this.FH = null;
            this.FG = -1;
            this.F_ = -1;
         }

         this.Fq = null;
         this.FI.clear();
         bn.j(
            "HigherLower",
            var2
               ? "One-card exchange confirmed (open=" + (var3.n - var3.h) / 1000000L + "ms, transfer=" + (System.nanoTime() - var3.n) / 1000000L + "ms)"
               : "Exchange stopped: " + var3.w
         );
      }
   }

   private void P(Minecraft var1, vh.d var2) {
      if (var1.gameMode != null && var1.getConnection() != null && !var1.player.isShiftKeyDown() && !var1.options.keyUse.isDown()) {
         BlockHitResult var3 = Y(var1, var2.G);
         if (var3 != null) {
            var2.d = (boolean)1;
            this.x(var1, var3, var1x -> var2.F.o(var1x, var2.V));
         }
      }
   }

   @Override
   public void onBlockInteractionAcknowledged(int var1) {
      vh.d var2 = this.Fq;
      if (var2 != null && var2.F.I()) {
         boolean var3 = var2.F.y();
         var2.F.x(var1);
         if (!var3 && var2.F.y()) {
            bn.b("HigherLower Dispenser interaction processed sync=" + var2.R + " " + var2.F.G());
         }

         this.I();
      }
   }

   private void I() {
      Minecraft var1 = Minecraft.getInstance();
      if (!this.Fk && !this.F2 && this.Fq != null && var1.player != null && var1.level != null) {
         this.Fk = (boolean)1;

         try {
            this.w(var1);
         } finally {
            this.Fk = (boolean)0;
         }
      }
   }

   @Override
   public boolean isSwapActive() {
      return this.Fq != null;
   }

   @Override
   public boolean isSwapBusy() {
      return this.Fq != null;
   }

   @Override
   public boolean isSwapSuppressing() {
      return this.Fq != null && this.Fq.d;
   }

   @Override
   public boolean shouldSuppressOpen(MenuType<?> var1) {
      return var1 == MenuType.GENERIC_3x3 && this.isSwapSuppressing();
   }

   @Override
   public void onSwapContainerOpened(MenuType<?> var1, int var2) {
      vh.d var3 = this.Fq;
      if (var3 != null && var1 == MenuType.GENERIC_3x3) {
         int var4 = var3.F.I() && !var3.N && var3.D <= 0 ? 0 : 1;
         bn.b("HigherLower Dispenser opened sync=" + var2 + " " + var3.F.G());
         if (var4 != 0) {
            this.a("unsolicited or mid-transfer container replacement");
            var3.N = (boolean)0;
         }

         var3.R = var2;
         var3.n = System.nanoTime();
         var3.x = (boolean)0;
         var3.H = Minecraft.getInstance().player.containerMenu;
         var3.a.U(var3.H, -1, this.FJ);
         var3.X = 0;
         var3.p.clear();
         var3.I = ItemStack.EMPTY;
         if (var4 != 0) {
            Minecraft var5 = Minecraft.getInstance();
            this.f(var5, var5.player.containerMenu, var3);
         }
      }
   }

   @Override
   public int getSwapSyncId() {
      return this.Fq == null ? -1 : this.Fq.R;
   }

   @Override
   public int getSwapValue() {
      return this.Fq == null ? this.F7 : this.Fq.l;
   }

   @Override
   public int getSwapRealMapId() {
      return this.Fq == null ? -1 : E(this.Fq.L);
   }

   @Override
   public boolean isSwapJuggling() {
      return this.Fq != null;
   }

   @Override
   public boolean isSwapFreezing() {
      return this.Fq != null;
   }

   @Override
   public boolean isSwapFreezingPassThrough() {
      return false;
   }

   @Override
   public boolean isDealBobAllowed(ItemStack var1) {
      return this.Fq == null;
   }

   @Override
   public boolean isHotbarBobAllowed(ItemStack var1) {
      return this.Fq == null;
   }

   @Override
   public ItemStack getVisualOverride(ItemStack var1) {
      return null;
   }

   @Override
   public ItemStack getInventorySlotProjection(int var1) {
      ItemStack var2 = qm.S(var1);
      if (var2 != null) {
         return var2;
      } else {
         return this.Fq == null ? null : this.Fq.q.get(var1);
      }
   }

   @Override
   public ItemStack projectSlot(int var1, ItemStack var2) {
      ItemStack var3 = this.getInventorySlotProjection(var1);
      return var3 == null ? var2 : var3;
   }

   private void l(Minecraft var1) {
      int var2 = var1.player.getInventory().getSelectedSlot();
      if (var2 != this.FG) {
         this.FQ++;
      }

      this.FG = var2;
      this.Fc = (boolean)0;
      this.Fe = this.projectSlot(var2, var1.player.getMainHandItem()).copy();
   }

   @Override
   public boolean isDealtHandLocked() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null || var1.player.getInventory().getSelectedSlot() != this.FG || this.Fc && !S(var1.player.getMainHandItem(), this.Fe)) {
         this.FG = -1;
      }

      return this.FG >= 0;
   }

   @Override
   public ItemStack getDealtHandVisual(ItemStack var1) {
      if (!this.isDealtHandLocked()) {
         return var1;
      } else {
         return this.Fc ? var1 : this.projectSlot(this.FG, var1);
      }
   }

   @Override
   public long getDealtHandSequence() {
      return this.FQ;
   }

   @Override
   public boolean isDealtHandSettled() {
      return this.Fc;
   }

   @Override
   public void signalFramePlacement(ItemFrame var1) {
      this.FG = -1;
      if (Minecraft.getInstance().player.firstPersonHandsAndItems() instanceof q2 var2) {
         var2.larpRig$releaseHeldMap(this, Minecraft.getInstance().player.getMainHandItem());
      }
   }

   private int J() {
      if (this.F7 >= 1 && this.FT != vh.i.a) {
         ArrayList var1 = new ArrayList();

         for (int var2 = 1; var2 <= 9; var2++) {
            int var3 = this.FT == vh.i.t ? (var2 > this.F7 ? 1 : 0) : (var2 < this.F7 ? 1 : 0);
            if (var3 != 0 && this.getTemplateForValue(var2) != null && this.n(var2) >= 0) {
               var1.add(var2);
            }
         }

         if (!var1.isEmpty()) {
            return (Integer)var1.get(ThreadLocalRandom.current().nextInt(var1.size()));
         } else {
            return (this.FT == vh.i.b && this.F7 == 1 || this.FT == vh.i.t && this.F7 == 9) && this.n(this.F7) >= 0 ? this.F7 : -1;
         }
      } else {
         return -1;
      }
   }

   private int C(ItemStack var1) {
      int var2 = E(var1);
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

   private static int E(ItemStack var0) {
      MapId var1 = var0.is(Items.FILLED_MAP) ? (MapId)var0.get(DataComponents.MAP_ID) : null;
      return var1 == null ? -1 : var1.id();
   }

   private static boolean S(ItemStack var0, ItemStack var1) {
      return je.q(var0, var1);
   }

   private static boolean n(ItemStack var0, ItemStack var1) {
      return !var0.is(Items.FILLED_MAP) && !var1.is(Items.FILLED_MAP) || je.F(var0, var1);
   }

   private static String f(ItemStack var0) {
      return "map=" + E(var0) + "/count=" + var0.getCount() + "/filled=" + var0.is(Items.FILLED_MAP);
   }

   private static String y(vh.d var0) {
      return "step="
         + var0.D
         + " slot="
         + var0.W
         + " button="
         + var0.T
         + " sync="
         + var0.R
         + " revision="
         + var0.b
         + " probe="
         + var0.M
         + " ack["
         + var0.a.E()
         + "] slotSource="
         + var0.o
         + " cursorSource="
         + var0.u
         + " lastPacket=["
         + var0.O
         + "] expectedSlot="
         + f(var0.P)
         + " receivedSlot="
         + f(var0.p.get(var0.W))
         + " expectedCursor="
         + f(var0.m)
         + " receivedCursor="
         + f(var0.I)
         + (var0.i < 0 ? "" : " batchSource=" + var0.i + " expectedSource=" + f(var0.Q) + " receivedSource=" + f(var0.p.get(var0.i)))
         + " slotIdentity="
         + je.F(var0.p.get(var0.W), var0.P)
         + " cursorIdentity="
         + je.F(var0.I, var0.m)
         + " slotComponentsExact="
         + ItemStack.isSameItemSameComponents(var0.p.get(var0.W), var0.P)
         + " cursorComponentsExact="
         + ItemStack.isSameItemSameComponents(var0.I, var0.m);
   }

   private static ItemStack[] E(Minecraft var0) {
      ItemStack[] var1 = new ItemStack[36];

      for (int var2 = 0; var2 < var1.length; var2++) {
         var1[var2] = var0.player.getInventory().getItem(var2).copy();
      }

      return var1;
   }

   private static Map<Integer, Integer> s(ItemStack[] var0) {
      HashMap var1 = new HashMap();

      for (ItemStack var5 : var0) {
         if (E(var5) >= 0) {
            var1.merge(E(var5), var5.getCount(), Integer::sum);
         }
      }

      return var1;
   }

   private static Map<Integer, Integer> W(Minecraft var0) {
      Map var1 = s(E(var0));
      ItemStack var2 = var0.player.containerMenu.getCarried();
      if (E(var2) >= 0) {
         var1.merge(E(var2), var2.getCount(), Integer::sum);
      }

      if (var0.level != null) {
         for (Entity var4 : var0.level.entitiesForRendering()) {
            if (var4 instanceof ItemFrame var5) {
               ItemStack var6 = var5.getItem();
               if (E(var6) >= 0) {
                  var1.merge(E(var6), var6.getCount(), Integer::sum);
               }
            }
         }
      }

      return var1;
   }

   private static int S(AbstractContainerMenu var0, Minecraft var1, int var2) {
      for (Slot var4 : var0.slots) {
         if (var4.container == var1.player.getInventory() && var4.getContainerSlot() == var2) {
            return var4.index;
         }
      }

      return -1;
   }

   private static boolean r(Slot var0, ItemStack var1, int var2) {
      ItemStack var3 = var0.getItem();
      return !var1.isEmpty()
         && var0.mayPlace(var1)
         && (var3.isEmpty() || S(var3, var1))
         && var3.getCount() + var2 <= Math.min(var0.getMaxStackSize(var1), var1.getMaxStackSize());
   }

   private static final class d {
      final long h = System.nanoTime();
      long n;
      final String A;
      final BlockPos G;
      final vh.i K;
      final int Y;
      final int l;
      final int S;
      final int C;
      final int z;
      final ItemStack[] t;
      final ItemStack L;
      final Map<Integer, Integer> U;
      final Map<Integer, ItemStack> q = new HashMap<>();
      int R = -1;
      int s = -1;
      int f = -1;
      int D;
      int V;
      int X;
      int W = -1;
      final p5 F = new p5();
      boolean d;
      boolean x;
      boolean N;
      boolean J;
      boolean r;
      String w;
      List<ItemStack> p = new ArrayList<>();
      ItemStack I = ItemStack.EMPTY;
      ItemStack P = ItemStack.EMPTY;
      ItemStack m = ItemStack.EMPTY;
      int i = -1;
      ItemStack Q = ItemStack.EMPTY;
      ItemStack c = ItemStack.EMPTY;
      ItemStack y = ItemStack.EMPTY;
      final oe a = new oe();
      AbstractContainerMenu H;
      int T;
      int b;
      int M;
      String O = "none";
      String o = "none";
      String u = "none";

      d(String var1, BlockPos var2, vh.i var3, int var4, int var5, int var6, int var7, int var8, ItemStack[] var9, ItemStack var10, Map<Integer, Integer> var11) {
         this.A = var1;
         this.G = var2;
         this.K = var3;
         this.Y = var4;
         this.l = var5;
         this.S = var6;
         this.C = var7;
         this.z = var8;
         this.t = var9;
         this.L = var10.copyWithCount(1);
         this.U = new HashMap<>(var11);
      }
   }

   public enum i {
      a("off"),
      t("higher"),
      b("lower");

      private final String s;

      i(String var3) {
         this.s = var3;
      }

      public String i() {
         return this.s;
      }
   }
}
