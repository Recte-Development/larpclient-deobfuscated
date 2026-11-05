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
public final class um extends ug {
   private static final um oQ = new um();
   private final Map<Integer, Integer> oS = new HashMap<>();
   private final Map<Integer, ItemStack> o0 = new HashMap<>();
   private ItemStack[] og;
   private Map<Integer, Integer> oz;
   private um.e oV = um.e.R;
   private um.e ox = um.e.R;
   private int oI = -1;
   private int oh = -1;
   private int oe = -1;
   private ItemStack oi = ItemStack.EMPTY;
   private int oP = -1;
   private int o5;
   private int oy;
   private boolean or;
   private String od;
   private BlockPos o2;
   private int ov = -1;
   private ClientLevel oO;
   private long oH = -1L;
   private um.z oD;
   private int oB = -1;
   private ItemStack oT;
   private boolean of;
   private long o7;
   private boolean o9;
   private boolean oU;
   private long o8;
   private String ok = "no map change";

   private um() {
   }

   public static um T() {
      return oQ;
   }

   public um.e V() {
      return this.oV;
   }

   public String J() {
      return this.oV.i();
   }

   public int U() {
      return this.oI;
   }

   public int H() {
      return this.oP;
   }

   public boolean m() {
      return this.or;
   }

   public int F(int var1) {
      return this.oS.getOrDefault(this.getTemplateIdForValue(var1), 0);
   }

   public void I(String var1) {
      this.oV = "higher".equalsIgnoreCase(var1) ? um.e.D : ("lower".equalsIgnoreCase(var1) ? um.e.G : um.e.R);
      if (this.oV == um.e.R) {
         this.M();
      }
   }

   public void v() {
      this.I(switch (this.oV) {
         case R -> "higher";
         case D -> "lower";
         case G -> "off";
      });
   }

   public void M() {
      this.clearQueue();
      this.oI = this.oh = this.oe = -1;
      this.oi = ItemStack.EMPTY;
      this.oz = null;
      this.oy = 0;
   }

   @Override
   public void clearQueue() {
      this.or = (boolean)0;
      this.oP = -1;
      if (this.oD != null) {
         this.abortSwap();
      } else {
         this.oS.clear();
      }
   }

   @Override
   protected boolean supportsPhysicalSwap() {
      return false;
   }

   @Override
   protected BlockPos getAssignedDispenserPosition(Minecraft var1) {
      f9 var2 = f9.W();
      return var2.T(f9.b.B).contains(this.A()) ? var2.j(this.A()) : null;
   }

   @Override
   public boolean hasQueuedForce() {
      return this.or && this.oP > 0;
   }

   @Override
   public int getPendingValue() {
      return this.hasQueuedForce() ? this.oP : -1;
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
      if (!Objects.equals(var1, this.A())) {
         if (this.A() != null) {
            this.M();
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
            int var6 = this.B(var5);
            if (var6 > 0) {
               this.o0.put(var6, var5.copyWithCount(1));
            }
         }
      }

      return var2;
   }

   public void z(Minecraft var1) {
      this.r(var1);
      if (var1 != null && var1.player != null && ft.V().x && this.oV != um.e.R && !this.or && !this.isSwapBusy()) {
         ug var2 = ug.x();
         if (var2 == this || !var2.isSwapBusy()) {
            this.og = Z(var1);
            if (this.oI > 0) {
               int var3 = this.oh >= 0 && this.oh < this.og.length && !this.oi.isEmpty() && p(this.oi, this.og[this.oh]) ? this.oh : -1;
               if (var3 < 0) {
                  for (int var4 = 0; var4 < this.og.length; var4++) {
                     if (!this.oi.isEmpty() && p(this.oi, this.og[var4])) {
                        if (var3 >= 0) {
                           this.clearQueue();
                           hh.K("HigherLower", "Activation disarmed: moved reference stack is ambiguous");
                           return;
                        }

                        var3 = var4;
                     }
                  }
               }

               if (var3 < 0) {
                  this.M();
                  hh.K("HigherLower", "Previous reference removed; waiting for first card of new round");
               } else {
                  this.oh = var3;
               }
            }

            this.oS.clear();
            this.oS.putAll(R(this.og));
            if (this.oI < 1 && this.oz == null) {
               this.oz = s(var1);
            }

            this.oe = this.oI;
            this.ox = this.oV;
            this.od = this.A();
            BlockPos var5 = this.getAssignedDispenserPosition(var1);
            this.o2 = var5 == null ? null : var5.immutable();
            this.oP = this.u();
            this.ov = this.r(this.oP);
            this.or = (boolean)1;
            this.o5 = 0;
            this.ok = "no map change";
            hh.L("HigherLower Activation target=" + this.ox.i() + " reference=" + this.oe + " recommendation=" + this.oP);
         }
      }
   }

   @Override
   public void onDealtCardSlotUpdate(int var1, int var2, int var3) {
      this.r(Minecraft.getInstance());
   }

   private void r(Minecraft var1) {
      if (this.or && this.oD == null && var1 != null && var1.player != null) {
         ItemStack[] var2 = Z(var1);
         Map var3 = R(var2);
         int var4 = -1;
         int var5 = -1;

         for (int var6 = 0; var6 < var2.length; var6++) {
            if (!h(var2[var6], this.og[var6])) {
               ItemStack var7 = this.og[var6];
               ItemStack var8 = var2[var6];
               int var9 = this.B(var8);
               if (var4 >= 0 || var9 < 1 || !var7.isEmpty() && !p(var7, var8) || var8.getCount() != var7.getCount() + 1) {
                  this.ok = "slot="
                     + var6
                     + " reason="
                     + (
                        var4 >= 0
                           ? "multiple map changes"
                           : (var9 < 1 ? "unrecognized or removed map" : (!var7.isEmpty() && !p(var7, var8) ? "origin/components changed" : "not +1"))
                     )
                     + " before="
                     + s(var7)
                     + " after="
                     + s(var8);
                  return;
               }

               var4 = var6;
               var5 = var9;
            }
         }

         if (var4 >= 0 && var3.getOrDefault(c(var2[var4]), 0) == this.oS.getOrDefault(c(var2[var4]), 0) + 1) {
            int var12 = this.ox == um.e.D ? (var5 > this.oe ? 1 : 0) : (var5 < this.oe ? 1 : 0);
            if (this.oe < 1 || this.oP < 1 || var12 != 0 || var5 == this.oP) {
               this.G(var5, var4);
            } else if (yg.u(var1) && yg.B(this, var4, this.oP)) {
               int var14 = this.oP;
               this.W(var14, var4, var5, true);
            } else {
               int var13 = -1;
               int var15 = this.oP == this.oe ? 1 : 0;
               if (var15 != 0 && this.oh >= 0 && this.B(this.og[this.oh]) == this.oP) {
                  var13 = this.oh;
               } else if (var15 == 0) {
                  int var16 = this.oh >= 0 && this.oh < 9 ? this.oh : var1.player.getInventory().getSelectedSlot();

                  for (int var10 = 1; var10 <= 9; var10++) {
                     int var11 = (var16 + var10) % 9;
                     if (this.og[var11].isEmpty() && (var2[var11].isEmpty() || var11 == var4)) {
                        var13 = var11;
                        break;
                     }
                  }
               }

               ItemStack var17 = this.o0.get(this.oP);
               if (var15 != 0 && var13 >= 0) {
                  var17 = this.og[var13].copyWithCount(1);
               }

               if (var17 != null
                  && c(var17) == this.getTemplateIdForValue(this.oP)
                  && var13 >= 0
                  && this.o2 != null
                  && (var15 == 0 || p(this.og[var13], var17) && this.og[var13].getCount() < var17.getMaxStackSize())) {
                  um.z var18 = new um.z(this.od, this.o2, this.ox, this.oe, this.oP, var5, var4, var13, this.og, var2[var4], this.oS);
                  var18.X = this.ov;
                  var18.O.merge(this.getTemplateIdForValue(var18.Y), 1, Integer::sum);
                  var18.C.put(var4, var18.w[var4].copy());
                  var18.C.put(var13, var17.copyWithCount(var18.w[var13].getCount() + 1));
                  this.oD = var18;
                  var18.T = 1;
                  this.or = (boolean)0;
                  this.B(var1);
                  hh.K(
                     "HigherLower",
                     "One-card exchange real="
                        + var5
                        + " ->"
                        + var18.Y
                        + " target="
                        + var18.F.i()
                        + " reference="
                        + var18.i
                        + " arrival="
                        + var4
                        + " destination="
                        + var13
                  );
                  this.I();
               } else {
                  hh.K("HigherLower", "Exchange unavailable: destination, components, capacity or assignment");
                  this.G(var5, var4);
               }
            }
         } else {
            this.ok = "reason=no +1 map total arrival=" + var4;
         }
      }
   }

   private void G(int var1, int var2) {
      this.W(var1, var2, var1, false);
   }

   private void W(int var1, int var2, int var3, boolean var4) {
      if (this.oD == null) {
         hh.K("HigherLower", hh.e(Integer.toString(var3), Integer.toString(var1), this.oe > 0 && this.oP > 0, var4));
      }

      this.oI = var1;
      this.oh = var2;
      this.oi = Minecraft.getInstance().player.getInventory().getItem(var2).copyWithCount(1);
      this.or = (boolean)0;
      this.oP = -1;
      this.oS.clear();
   }

   @Override
   public void tick(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         if (this.oO != null && this.oO != var1.level) {
            this.oD = null;
            this.oB = -1;
            this.M();
         }

         if (this.oO != var1.level || this.oH != var1.level.getGameTime()) {
            this.oO = var1.level;
            this.oH = var1.level.getGameTime();
            if (this.oD != null) {
               this.oD.T++;
               this.T(var1);
            } else if (ft.V().x) {
               this.r(var1);
               if (this.or && ++this.o5 > 100) {
                  hh.K("HigherLower", "Draw timed out: " + this.ok + " baselineMapCounts=" + this.oS + " receivedMapCounts=" + R(Z(var1)));
                  this.clearQueue();
               }

               if (this.oD == null && !this.or && this.oI > 0 && this.oz != null) {
                  Map var2 = s(var1);
                  int var3 = 0;

                  for (Entry var5 : var2.entrySet()) {
                     var3 += Math.max(0, (Integer)var5.getValue() - this.oz.getOrDefault(var5.getKey(), 0));
                  }

                  this.oy = var3 == 0 ? this.oy + 1 : 0;
                  if (this.oy >= 6) {
                     this.M();
                  }
               }
            }
         }
      } else {
         this.oD = null;
         this.oB = -1;
         this.M();
         this.oO = null;
      }
   }

   private void T(Minecraft var1) {
      um.z var2 = this.oD;
      if (!var2.j && var2.y.O(var2.I) || !Objects.equals(var2.r, this.A()) || !var2.c.equals(this.getAssignedDispenserPosition(var1))) {
         this.c("timeout or assignment changed step=" + var2.f + " sync=" + var2.o + " ready=" + var2.a + " " + var2.t.E());
      }

      AbstractContainerMenu var3 = var1.player.containerMenu;
      if (var2.o < 0) {
         if (var2.T >= 100) {
            this.c("dispenser did not open; no cards moved");
         }

         if (var2.V) {
            if (!var3.getCarried().isEmpty()) {
               var2.C.clear();
               this.oB = -1;
            }

            this.b(var1, false);
         } else {
            if (var1.gui.screen() == null && var3 == var1.player.inventoryMenu && var3.getCarried().isEmpty() && var2.t.u(var2.T)) {
               this.v(var1, var2);
            }
         }
      } else if (!(var3 instanceof DispenserMenu) || var3 != var2.L || var3.containerId != var2.o) {
         this.c("handler replaced");
         var2.C.clear();
         this.oB = -1;
         this.b(var1, false);
      } else if (!var2.m) {
         if (var2.a && var1.gameMode != null) {
            if (var2.V || var2.t.p()) {
               if (var2.j) {
                  if (!var2.y.l(var2.W < 0 || mq.T(var2.B.get(var2.W), var2.z), mq.T(var2.B.get(var2.R), var2.S), mq.T(var2.q, var2.s))) {
                     if (var2.y.t()) {
                        this.c("click not confirmed; no replay " + z(var2));
                        this.L(var1, var3, var2);
                     }

                     return;
                  }

                  hh.L("HigherLower Click confirmed " + z(var2));
                  var2.j = (boolean)0;
                  if (!var2.V && var2.f == 3) {
                     var2.C.put(var2.b, var2.B.get(var2.R).copy());
                  }

                  var2.D = 0;
                  if (!var2.V) {
                     var2.f += 3;
                  }
               }

               if (var2.V) {
                  this.u(var1, var3, var2);
               } else {
                  int var4 = B(var3, var1, var2.p);
                  int var5 = B(var3, var1, var2.b);
                  if (var4 >= 0 && var5 >= 0) {
                     if (var2.f == 0) {
                        if (!var3.getCarried().isEmpty()) {
                           this.c("occupied cursor");
                           return;
                        }

                        for (int var6 = 0; var6 < var2.w.length; var6++) {
                           ItemStack var7 = var6 == var2.p ? var2.Z.copyWithCount(var2.w[var6].getCount() + 1) : var2.w[var6];
                           ItemStack var8 = var1.player.getInventory().getItem(var6);
                           if (var6 != var2.p && var6 != var2.b ? !h(var8, var7) : !mq.T(var8, var7)) {
                              this.c("inventory changed before extraction slot=" + var6 + " expected=" + s(var7) + " received=" + s(var8));
                              return;
                           }
                        }

                        if (var2.l < 0) {
                           for (int var11 = 0; var11 < 9; var11++) {
                              if (var11 != var2.X
                                 && ItemStack.isSameItemSameComponents(var3.getSlot(var11).getItem(), var3.getSlot(var4).getItem())
                                 && ItemStack.isSameItemSameComponents(var2.B.get(var11), var2.B.get(var4))
                                 && e(var3.getSlot(var11), var2.Z, 1)) {
                                 var2.l = var11;
                                 break;
                              }
                           }

                           for (int var12 = 0; var12 < 9; var12++) {
                              if (var2.l < 0
                                 && var12 != var2.X
                                 && var3.getSlot(var12).getItem().isEmpty()
                                 && var2.B.get(var12).isEmpty()
                                 && e(var3.getSlot(var12), var2.Z, 1)) {
                                 var2.l = var12;
                                 break;
                              }
                           }
                        }

                        if (var2.X < 0
                           || var2.X >= 9
                           || var2.l < 0
                           || this.B(var3.getSlot(var2.X).getItem()) != var2.Y
                           || !var3.getSlot(var2.X).mayPickup(var1.player)
                           || !var3.getSlot(var5).mayPlace(var3.getSlot(var2.X).getItem())
                           || !var2.w[var2.b].isEmpty() && !p(var2.w[var2.b], var3.getSlot(var2.X).getItem())
                           || var2.w[var2.b].getCount() + 1 > var3.getSlot(var5).getMaxStackSize(var3.getSlot(var2.X).getItem())
                           || !var3.getSlot(var4).mayPickup(var1.player)) {
                           this.c("source, return space, or destination unavailable");
                           return;
                        }

                        ItemStack var13 = var3.getSlot(var2.X).getItem();
                        if (!var2.w[var2.b].isEmpty()
                           && (
                              !ItemStack.isSameItemSameComponents(var13, var3.getSlot(var5).getItem())
                                 || !ItemStack.isSameItemSameComponents(var2.B.get(var2.X), var2.B.get(var5))
                           )) {
                           this.c("replacement destination has incompatible vanilla components");
                           return;
                        }

                        var2.K = var13.copy();
                        var2.H = var3.getSlot(var2.l).getItem().copy();
                        var2.C.put(var2.b, var13.copyWithCount(var2.w[var2.b].getCount() + 1));
                        this.P(var1, var3, var4, var2.l);
                     } else if (var2.f == 3) {
                        if (!var3.getCarried().isEmpty()
                           || !mq.T(var3.getSlot(var2.X).getItem(), var2.K)
                           || !mq.T(var3.getSlot(var4).getItem(), var2.w[var2.p])
                           || !mq.T(var3.getSlot(var5).getItem(), var2.w[var2.b])
                           || !e(var3.getSlot(var5), var3.getSlot(var2.X).getItem(), 1)) {
                           this.c("replacement source changed");
                           return;
                        }

                        this.P(var1, var3, var2.X, var5);
                     } else {
                        int var14 = var2.q.isEmpty()
                              && var3.getCarried().isEmpty()
                              && var2.O.equals(R(Z(var1)))
                              && mq.T(var2.B.get(var2.X), var2.K.copyWithCount(var2.K.getCount() - 1))
                              && mq.T(var2.B.get(var2.l), var2.Z.copyWithCount(var2.H.getCount() + 1))
                           ? 1
                           : 0;

                        for (int var15 = 0; var15 < var2.w.length && var14 != 0; var15++) {
                           int var16 = B(var3, var1, var15);
                           ItemStack var9 = var15 == var2.b ? var2.C.get(var15) : var2.w[var15];
                           int var10 = var15 != var2.p && var15 != var2.b ? 0 : 1;
                           var14 = var16 < 0
                                 || (
                                    var10 != 0
                                       ? !mq.T(var2.B.get(var16), var9) || !mq.T(var1.player.getInventory().getItem(var15), var9)
                                       : !h(var2.B.get(var16), var9) || !h(var1.player.getInventory().getItem(var15), var9)
                                 )
                              ? 0
                              : 1;
                           if (var14 == 0) {
                              this.c(
                                 "final slot="
                                    + var15
                                    + " expected="
                                    + s(var9)
                                    + " server="
                                    + (var16 < 0 ? "missing" : s(var2.B.get(var16)))
                                    + " local="
                                    + s(var1.player.getInventory().getItem(var15))
                              );
                           }
                        }

                        if (var14 == 0) {
                           this.c(
                              "final quantity ledger mismatch expectedCounts="
                                 + var2.O
                                 + " receivedCounts="
                                 + R(Z(var1))
                                 + " source="
                                 + s(var2.B.get(var2.X))
                                 + " expectedSource="
                                 + s(var2.K.copyWithCount(var2.K.getCount() - 1))
                                 + " deposit="
                                 + s(var2.B.get(var2.l))
                                 + " expectedDeposit="
                                 + s(var2.Z.copyWithCount(var2.H.getCount() + 1))
                           );
                           return;
                        }

                        this.b(var1, true);
                     }
                  } else {
                     this.c("player slots missing");
                  }
               }
            }
         } else {
            if (var2.V) {
               this.L(var1, var3, var2);
            }
         }
      }
   }

   private void P(Minecraft var1, AbstractContainerMenu var2, int var3, int var4) {
      um.z var5 = this.oD;
      if (!var5.j && !var5.V && !var5.m) {
         if (var5.a && var2 == var5.L && var5.B.size() == var2.slots.size() && var5.q.isEmpty() && var2.getCarried().isEmpty()) {
            for (int var6 = 0; var6 < var5.B.size(); var6++) {
               if (!mq.T(var2.getSlot(var6).getItem(), var5.B.get(var6))) {
                  this.c("pre-batch local/server mismatch slot=" + var6);
                  this.L(var1, var2, var5);
                  return;
               }
            }

            Slot var26 = var2.getSlot(var3);
            Slot var7 = var2.getSlot(var4);
            ItemStack var8 = var26.getItem().copy();
            ItemStack var9 = var7.getItem().copy();
            ItemStack var10 = var5.B.get(var3);
            ItemStack var11 = var5.B.get(var4);
            int var12 = !var9.isEmpty() && !ItemStack.isSameItemSameComponents(var8, var9)
                  || !var11.isEmpty() && !ItemStack.isSameItemSameComponents(var10, var11)
               ? 0
               : 1;
            yk.e var13 = yk.batch(
               var3,
               var4,
               var10.getCount(),
               var11.getCount(),
               (boolean)var12,
               Math.min(var26.getMaxStackSize(var10), var10.getMaxStackSize()),
               Math.min(var7.getMaxStackSize(var10), var10.getMaxStackSize())
            );
            yk.e var14 = yk.batch(
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
               var5.W = var3;
               var5.z = var10.copyWithCount(var13.returned().slotCount());
               var5.R = var4;
               var5.S = (var11.isEmpty() ? var10 : var11).copyWithCount(var13.placement().slotCount());
               var5.s = ItemStack.EMPTY;
               var5.j = (boolean)1;
               var5.D = 0;
               ClientPacketListener var15 = var1.getConnection();
               PlayerInfo var16 = var15 == null ? null : var15.getPlayerInfo(var1.player.getUUID());
               var5.y.f(var16 == null ? 0 : var16.getLatency());
               var5.y.P(var2, var4, var3, this.o8);
               var5.v = var5.n = var5.Q = "none";

               for (int var17 = 0; var17 < var13.clicks(); var17++) {
                  int var18 = var17 == 1 ? var4 : var3;
                  int var19 = var17 == 0 ? var13.pickupButton() : (var17 == 1 ? 1 : 0);
                  var5.A = var19;
                  this.o9 = (boolean)(var17 == var13.clicks() - 1 ? 1 : 0);

                  try {
                     var1.gameMode.handleContainerInput(var2.containerId, var18, var19, ContainerInput.PICKUP, var1.player);
                  } finally {
                     this.o9 = (boolean)0;
                  }

                  int var20 = var17 == 2 ? var13.returned().slotCount() : var13.pickup().slotCount();
                  int var21 = var17 == 0 ? var9.getCount() : var13.placement().slotCount();
                  int var22 = var17 == 0 ? var13.pickup().cursorCount() : (var17 == 1 ? var13.placement().cursorCount() : 0);
                  ItemStack var23 = (var9.isEmpty() ? var8 : var9).copyWithCount(var21);
                  if (!ItemStack.matches(var26.getItem(), var8.copyWithCount(var20))
                     || !ItemStack.matches(var7.getItem(), var23)
                     || !ItemStack.matches(var2.getCarried(), var8.copyWithCount(var22))) {
                     this.c("batch prediction diverged after click=" + var17 + "; no replay");
                     this.L(var1, var2, var5);
                     return;
                  }
               }

               hh.L("HigherLower Batch issued clicks=" + var13.clicks() + " source=" + var3 + " " + z(var5));
            } else {
               this.c("unsafe batch: source, capacity or vanilla component merge mismatch");
            }
         } else {
            this.c("batch requires known handler and empty authoritative cursor");
         }
      }
   }

   private void T(Minecraft var1, AbstractContainerMenu var2, int var3, int var4) {
      um.z var5 = this.oD;
      var5.W = -1;
      if (mq.T(var2.getSlot(var3).getItem(), var5.B.get(var3)) && mq.T(var2.getCarried(), var5.q)) {
         ItemStack var6 = var5.B.get(var3);
         ItemStack var7 = var5.q;
         yk var8 = yk.plan(
            var6.getCount(),
            var7.getCount(),
            var4,
            var7.isEmpty() || e(var2.getSlot(var3), var7, var4 == 1 ? 1 : var7.getCount()),
            var7.isEmpty() ? 0 : Math.min(var2.getSlot(var3).getMaxStackSize(var7), var7.getMaxStackSize())
         );
         if (var8 == null) {
            this.c("transfer unavailable before click slot=" + var3);
         } else {
            var5.S = (var6.isEmpty() ? var7 : var6).copyWithCount(var8.slotCount());
            var5.s = (var7.isEmpty() ? var6 : var7).copyWithCount(var8.cursorCount());
            var5.j = (boolean)1;
            var5.R = var3;
            var5.A = var4;
            var5.D = 0;
            var5.y.l(var2, var3, this.o8);
            var5.v = var5.n = var5.Q = "none";
            this.o9 = (boolean)1;

            try {
               var1.gameMode.handleContainerInput(var2.containerId, var3, var4, ContainerInput.PICKUP, var1.player);
            } finally {
               this.o9 = (boolean)0;
            }

            hh.L("HigherLower Click issued " + z(var5));
            if (mq.T(var5.S, var5.B.get(var3)) || mq.T(var5.s, var5.q)) {
               this.c("issued pickup did not change both slot and cursor; no replay");
               this.L(var1, var2, var5);
            }
         }
      } else {
         this.c(
            "pre-click local/server mismatch step="
               + var5.f
               + " slot="
               + var3
               + " localSlot="
               + s(var2.getSlot(var3).getItem())
               + " serverSlot="
               + s(var5.B.get(var3))
               + " localCursor="
               + s(var2.getCarried())
               + " serverCursor="
               + s(var5.q)
         );
         this.L(var1, var2, var5);
      }
   }

   @Override
   public void onInventorySynchronized(int var1, List<ItemStack> var2, ItemStack var3) {
      long var4 = ++this.o8;
      Minecraft var6 = Minecraft.getInstance();
      um.z var7 = this.oD;
      if (var7 != null) {
         var7.v = "full seq=" + var4 + " sync=" + var1 + " size=" + var2.size();
      }

      if (var7 != null && var6.player != null && var1 == var7.o && var6.player.containerMenu == var7.L && var2.size() == var7.L.slots.size()) {
         var7.B.clear();

         for (ItemStack var9 : var2) {
            var7.B.add(var9.copy());
         }

         var7.q = var3.copy();
         var7.n = var7.Q = "full";
         var7.a = (boolean)1;
         var7.t.d();
         if (var7.j) {
            var7.y.K(var7.L, var4, true, -1, true);
         }
      }

      this.r(var6);
      this.I();
   }

   @Override
   public void onServerSlot(int var1, int var2, ItemStack var3) {
      long var4 = ++this.o8;
      Minecraft var6 = Minecraft.getInstance();
      um.z var7 = this.oD;
      if (var7 != null) {
         var7.v = "slot seq=" + var4 + " sync=" + var1 + " slot=" + var2 + " " + s(var3);
      }

      if (var7 != null && var7.a && var6.player != null && var6.player.containerMenu == var7.L) {
         if (var1 == -1 && var2 == -1) {
            var7.q = var3.copy();
            var7.Q = "cursor-packet";
            if (var7.j) {
               var7.y.K(var7.L, var4, false, -1, true);
            }
         } else if (var1 == var7.o && var2 >= 0 && var2 < var7.B.size()) {
            var7.B.set(var2, var3.copy());
            if (var2 == var7.R) {
               var7.n = "handler-slot";
            }

            if (var7.j) {
               var7.y.K(var7.L, var4, false, var2, false);
            }
         } else if (var1 == 0 && var2 >= 0 && var2 < var6.player.inventoryMenu.slots.size()) {
            Slot var8 = var6.player.inventoryMenu.getSlot(var2);
            int var9 = var8.container == var6.player.getInventory() ? B(var7.L, var6, var8.getContainerSlot()) : -1;
            if (var9 >= 0 && var9 < var7.B.size()) {
               var7.B.set(var9, var3.copy());
               if (var9 == var7.R) {
                  var7.n = "sync0-slot";
               }

               if (var7.j) {
                  var7.y.K(var7.L, var4, false, var9, false);
               }
            }
         }
      }

      this.r(Minecraft.getInstance());
      this.I();
   }

   @Override
   public void onPlayerInventorySlot(int var1, ItemStack var2) {
      long var3 = ++this.o8;
      Minecraft var5 = Minecraft.getInstance();
      um.z var6 = this.oD;
      if (var6 != null) {
         var6.v = "player-inventory seq=" + var3 + " index=" + var1 + " " + s(var2);
      }

      if (var6 != null && var6.a && var5.player != null && var5.player.containerMenu == var6.L) {
         int var7 = B(var5.player.containerMenu, var5, var1);
         if (var7 >= 0 && var7 < var6.B.size()) {
            var6.B.set(var7, var2.copy());
            if (var7 == var6.R) {
               var6.n = "player-inventory";
            }

            if (var6.j) {
               var6.y.K(var6.L, var3, false, var7, false);
            }
         }
      }

      this.r(var5);
      this.I();
   }

   @Override
   public boolean hidesCursor(AbstractContainerMenu var1) {
      return this.oD != null && var1.containerId == this.oD.o && !this.oD.C.isEmpty();
   }

   @Override
   public boolean blocksUserInteraction() {
      return this.oD != null && !this.oD.C.isEmpty();
   }

   @Override
   public int clickRevision(AbstractContainerMenu var1) {
      int var2 = var1.getStateId();
      um.z var3 = this.oD;
      if (var3 != null && var1 == var3.L && (this.o9 || var3.m)) {
         char var4 = '耀';
         if (this.o9) {
            var3.M = var2;
            var3.P = var4;
         }

         return var4;
      } else {
         return var2;
      }
   }

   @Override
   public boolean blocksClose() {
      um.z var1 = this.oD;
      Minecraft var2 = Minecraft.getInstance();
      if (var1 == null || var2.player == null) {
         return false;
      }

      if (var1.m) {
         return false;
      }

      AbstractContainerMenu var3 = var2.player.containerMenu;
      return var3.containerId == var1.o && (var1.j || !var1.a || !var3.getCarried().isEmpty() || !var1.q.isEmpty());
   }

   @Override
   public boolean allowsRecoveryOpen(BlockPos var1) {
      return this.oD != null && this.oD.o < 0 && this.oD.c.equals(var1);
   }

   public boolean e(BlockPos var1) {
      return this.oD == null || this.oD.o < 0 && this.oD.c.equals(var1);
   }

   private void u(Minecraft var1, AbstractContainerMenu var2, um.z var3) {
      if (!var3.m) {
         ItemStack var4 = var2.getCarried();
         if (var4.isEmpty() && var3.q.isEmpty()) {
            if (!var3.K.isEmpty()) {
               ItemStack var8 = var2.getSlot(var3.X).getItem();
               if (!var8.isEmpty() && !p(var8, var3.K) || var3.K.getCount() - var8.getCount() > 1) {
                  this.L(var1, var2, var3);
                  return;
               }
            }

            for (int var9 = 0; var9 < var3.w.length; var9++) {
               ItemStack var10 = var3.w[var9];
               ItemStack var11 = var1.player.getInventory().getItem(var9);
               if (!var10.isEmpty() && (!p(var10, var11) || var11.getCount() < var10.getCount())) {
                  this.L(var1, var2, var3);
                  return;
               }
            }

            this.b(var1, false);
         } else if (mq.T(var4, var3.q)) {
            int var5 = this.B(var4) == var3.Y ? var3.X : B(var2, var1, var3.p);
            if (var5 >= 0 && e(var2.getSlot(var5), var4, var4.getCount())) {
               this.T(var1, var2, var5, 0);
            } else {
               for (Slot var7 : var2.slots) {
                  if (e(var7, var4, var4.getCount())) {
                     this.T(var1, var2, var7.index, 0);
                     return;
                  }
               }

               this.L(var1, var2, var3);
            }
         }
      }
   }

   private void L(Minecraft var1, AbstractContainerMenu var2, um.z var3) {
      var3.C.clear();
      var3.j = (boolean)0;
      this.oB = -1;
      if (!var3.m) {
         var3.m = (boolean)1;
         hh.K("HigherLower", "Manual recovery required: clear cursor, restore baseline slots and return any source surplus");
      }

      if (var2 instanceof DispenserMenu && (var1.gui.screen() == null || var1.gui.screen() instanceof AbstractContainerScreen var4 && var4.getMenu() != var2)) {
         var1.gui.setScreen(new DispenserScreen((DispenserMenu)var2, var1.player.getInventory(), Component.literal("HigherLower recovery: " + var3.G)));
      }
   }

   private void c(String var1) {
      if (this.oD != null && !this.oD.V) {
         this.oD.V = (boolean)1;
         this.oD.G = var1;
         hh.K("HigherLower", "Exchange failed: " + var1 + "; parking cursor without replay");
      }
   }

   @Override
   public void abortSwap() {
      this.c("cancelled");
   }

   private void b(Minecraft var1, boolean var2) {
      um.z var3 = this.oD;
      AbstractContainerMenu var4 = var1.player.containerMenu;
      if (var4.getCarried().isEmpty()) {
         if (var4 == var3.L) {
            var1.player.closeContainer();
         }

         if (var2) {
            hh.s("HigherLower", var3.U, var3.Y, this.getTemplateIdForValue(var3.Y));
            this.G(var3.Y, var3.b);
            this.of = (boolean)1;
            if (var1.player.firstPersonHandsAndItems() instanceof en var5) {
               var5.larpRig$releaseHeldMap(this, var1.player.getMainHandItem());
            }
         } else {
            this.oI = this.oh = -1;
            this.oi = ItemStack.EMPTY;
            this.oz = null;
            this.oB = -1;
            this.oP = -1;
         }

         this.oD = null;
         this.oS.clear();
         hh.K(
            "HigherLower",
            var2
               ? "One-card exchange confirmed (open=" + (var3.g - var3.I) / 1000000L + "ms, transfer=" + (System.nanoTime() - var3.g) / 1000000L + "ms)"
               : "Exchange stopped: " + var3.G
         );
      }
   }

   private void v(Minecraft var1, um.z var2) {
      if (var1.gameMode != null && var1.getConnection() != null && !var1.player.isShiftKeyDown() && !var1.options.keyUse.isDown()) {
         BlockHitResult var3 = b(var1, var2.c);
         if (var3 != null) {
            var2.x = (boolean)1;
            this.j(var1, var3, var1x -> var2.t.s(var1x, var2.T));
         }
      }
   }

   @Override
   public void onBlockInteractionAcknowledged(int var1) {
      um.z var2 = this.oD;
      if (var2 != null && var2.t.I()) {
         boolean var3 = var2.t.p();
         var2.t.w(var1);
         if (!var3 && var2.t.p()) {
            hh.L("HigherLower Dispenser interaction processed sync=" + var2.o + " " + var2.t.E());
         }

         this.I();
      }
   }

   private void I() {
      Minecraft var1 = Minecraft.getInstance();
      if (!this.oU && !this.o9 && this.oD != null && var1.player != null && var1.level != null) {
         this.oU = (boolean)1;

         try {
            this.T(var1);
         } finally {
            this.oU = (boolean)0;
         }
      }
   }

   @Override
   public boolean isSwapActive() {
      return this.oD != null;
   }

   @Override
   public boolean isSwapBusy() {
      return this.oD != null;
   }

   @Override
   public boolean isSwapSuppressing() {
      return this.oD != null && this.oD.x;
   }

   @Override
   public boolean shouldSuppressOpen(MenuType<?> var1) {
      return var1 == MenuType.GENERIC_3x3 && this.isSwapSuppressing();
   }

   @Override
   public void onSwapContainerOpened(MenuType<?> var1, int var2) {
      um.z var3 = this.oD;
      if (var3 != null && var1 == MenuType.GENERIC_3x3) {
         int var4 = var3.t.I() && !var3.j && var3.f <= 0 ? 0 : 1;
         hh.L("HigherLower Dispenser opened sync=" + var2 + " " + var3.t.E());
         if (var4 != 0) {
            this.c("unsolicited or mid-transfer container replacement");
            var3.j = (boolean)0;
         }

         var3.o = var2;
         var3.g = System.nanoTime();
         var3.a = (boolean)0;
         var3.L = Minecraft.getInstance().player.containerMenu;
         var3.y.l(var3.L, -1, this.o8);
         var3.D = 0;
         var3.B.clear();
         var3.q = ItemStack.EMPTY;
         if (var4 != 0) {
            Minecraft var5 = Minecraft.getInstance();
            this.L(var5, var5.player.containerMenu, var3);
         }
      }
   }

   @Override
   public int getSwapSyncId() {
      return this.oD == null ? -1 : this.oD.o;
   }

   @Override
   public int getSwapValue() {
      return this.oD == null ? this.oI : this.oD.Y;
   }

   @Override
   public int getSwapRealMapId() {
      return this.oD == null ? -1 : c(this.oD.Z);
   }

   @Override
   public boolean isSwapJuggling() {
      return this.oD != null;
   }

   @Override
   public boolean isSwapFreezing() {
      return this.oD != null;
   }

   @Override
   public boolean isSwapFreezingPassThrough() {
      return false;
   }

   @Override
   public boolean isDealBobAllowed(ItemStack var1) {
      return this.oD == null;
   }

   @Override
   public boolean isHotbarBobAllowed(ItemStack var1) {
      return this.oD == null;
   }

   @Override
   public ItemStack getVisualOverride(ItemStack var1) {
      return null;
   }

   @Override
   public ItemStack getInventorySlotProjection(int var1) {
      ItemStack var2 = yg.P(var1);
      if (var2 != null) {
         return var2;
      } else {
         return this.oD == null ? null : this.oD.C.get(var1);
      }
   }

   @Override
   public ItemStack projectSlot(int var1, ItemStack var2) {
      ItemStack var3 = this.getInventorySlotProjection(var1);
      return var3 == null ? var2 : var3;
   }

   private void B(Minecraft var1) {
      int var2 = var1.player.getInventory().getSelectedSlot();
      if (var2 != this.oB) {
         this.o7++;
      }

      this.oB = var2;
      this.of = (boolean)0;
      this.oT = this.projectSlot(var2, var1.player.getMainHandItem()).copy();
   }

   @Override
   public boolean isDealtHandLocked() {
      Minecraft var1 = Minecraft.getInstance();
      if (var1.player == null || var1.player.getInventory().getSelectedSlot() != this.oB || this.of && !p(var1.player.getMainHandItem(), this.oT)) {
         this.oB = -1;
      }

      return this.oB >= 0;
   }

   @Override
   public ItemStack getDealtHandVisual(ItemStack var1) {
      if (!this.isDealtHandLocked()) {
         return var1;
      } else {
         return this.of ? var1 : this.projectSlot(this.oB, var1);
      }
   }

   @Override
   public long getDealtHandSequence() {
      return this.o7;
   }

   @Override
   public boolean isDealtHandSettled() {
      return this.of;
   }

   @Override
   public void signalFramePlacement(ItemFrame var1) {
      this.oB = -1;
      if (Minecraft.getInstance().player.firstPersonHandsAndItems() instanceof en var2) {
         var2.larpRig$releaseHeldMap(this, Minecraft.getInstance().player.getMainHandItem());
      }
   }

   private int u() {
      if (this.oI >= 1 && this.oV != um.e.R) {
         ArrayList var1 = new ArrayList();

         for (int var2 = 1; var2 <= 9; var2++) {
            int var3 = this.oV == um.e.D ? (var2 > this.oI ? 1 : 0) : (var2 < this.oI ? 1 : 0);
            if (var3 != 0 && this.getTemplateForValue(var2) != null && this.r(var2) >= 0) {
               var1.add(var2);
            }
         }

         if (!var1.isEmpty()) {
            return (Integer)var1.get(ThreadLocalRandom.current().nextInt(var1.size()));
         } else {
            return (this.oV == um.e.G && this.oI == 1 || this.oV == um.e.D && this.oI == 9) && this.r(this.oI) >= 0 ? this.oI : -1;
         }
      } else {
         return -1;
      }
   }

   private int B(ItemStack var1) {
      int var2 = c(var1);
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

   private static int c(ItemStack var0) {
      MapId var1 = var0.is(Items.FILLED_MAP) ? (MapId)var0.get(DataComponents.MAP_ID) : null;
      return var1 == null ? -1 : var1.id();
   }

   private static boolean p(ItemStack var0, ItemStack var1) {
      return mq.U(var0, var1);
   }

   private static boolean h(ItemStack var0, ItemStack var1) {
      return !var0.is(Items.FILLED_MAP) && !var1.is(Items.FILLED_MAP) || mq.T(var0, var1);
   }

   private static String s(ItemStack var0) {
      return "map=" + c(var0) + "/count=" + var0.getCount() + "/filled=" + var0.is(Items.FILLED_MAP);
   }

   private static String z(um.z var0) {
      return "step="
         + var0.f
         + " slot="
         + var0.R
         + " button="
         + var0.A
         + " sync="
         + var0.o
         + " revision="
         + var0.M
         + " probe="
         + var0.P
         + " ack["
         + var0.y.c()
         + "] slotSource="
         + var0.n
         + " cursorSource="
         + var0.Q
         + " lastPacket=["
         + var0.v
         + "] expectedSlot="
         + s(var0.S)
         + " receivedSlot="
         + s(var0.B.get(var0.R))
         + " expectedCursor="
         + s(var0.s)
         + " receivedCursor="
         + s(var0.q)
         + (var0.W < 0 ? "" : " batchSource=" + var0.W + " expectedSource=" + s(var0.z) + " receivedSource=" + s(var0.B.get(var0.W)))
         + " slotIdentity="
         + mq.T(var0.B.get(var0.R), var0.S)
         + " cursorIdentity="
         + mq.T(var0.q, var0.s)
         + " slotComponentsExact="
         + ItemStack.isSameItemSameComponents(var0.B.get(var0.R), var0.S)
         + " cursorComponentsExact="
         + ItemStack.isSameItemSameComponents(var0.q, var0.s);
   }

   private static ItemStack[] Z(Minecraft var0) {
      ItemStack[] var1 = new ItemStack[36];

      for (int var2 = 0; var2 < var1.length; var2++) {
         var1[var2] = var0.player.getInventory().getItem(var2).copy();
      }

      return var1;
   }

   private static Map<Integer, Integer> R(ItemStack[] var0) {
      HashMap var1 = new HashMap();

      for (ItemStack var5 : var0) {
         if (c(var5) >= 0) {
            var1.merge(c(var5), var5.getCount(), Integer::sum);
         }
      }

      return var1;
   }

   private static Map<Integer, Integer> s(Minecraft var0) {
      Map var1 = R(Z(var0));
      ItemStack var2 = var0.player.containerMenu.getCarried();
      if (c(var2) >= 0) {
         var1.merge(c(var2), var2.getCount(), Integer::sum);
      }

      if (var0.level != null) {
         for (Entity var4 : var0.level.entitiesForRendering()) {
            if (var4 instanceof ItemFrame var5) {
               ItemStack var6 = var5.getItem();
               if (c(var6) >= 0) {
                  var1.merge(c(var6), var6.getCount(), Integer::sum);
               }
            }
         }
      }

      return var1;
   }

   private static int B(AbstractContainerMenu var0, Minecraft var1, int var2) {
      for (Slot var4 : var0.slots) {
         if (var4.container == var1.player.getInventory() && var4.getContainerSlot() == var2) {
            return var4.index;
         }
      }

      return -1;
   }

   private static boolean e(Slot var0, ItemStack var1, int var2) {
      ItemStack var3 = var0.getItem();
      return !var1.isEmpty()
         && var0.mayPlace(var1)
         && (var3.isEmpty() || p(var3, var1))
         && var3.getCount() + var2 <= Math.min(var0.getMaxStackSize(var1), var1.getMaxStackSize());
   }

   public enum e {
      R("off"),
      D("higher"),
      G("lower");

      private final String V;

      e(String var3) {
         this.V = var3;
      }

      public String i() {
         return this.V;
      }
   }

   private static final class z {
      final long I = System.nanoTime();
      long g;
      final String r;
      final BlockPos c;
      final um.e F;
      final int i;
      final int Y;
      final int U;
      final int p;
      final int b;
      final ItemStack[] w;
      final ItemStack Z;
      final Map<Integer, Integer> O;
      final Map<Integer, ItemStack> C = new HashMap<>();
      int o = -1;
      int X = -1;
      int l = -1;
      int f;
      int T;
      int D;
      int R = -1;
      final lh t = new lh();
      boolean x;
      boolean a;
      boolean j;
      boolean V;
      boolean m;
      String G;
      List<ItemStack> B = new ArrayList<>();
      ItemStack q = ItemStack.EMPTY;
      ItemStack S = ItemStack.EMPTY;
      ItemStack s = ItemStack.EMPTY;
      int W = -1;
      ItemStack z = ItemStack.EMPTY;
      ItemStack K = ItemStack.EMPTY;
      ItemStack H = ItemStack.EMPTY;
      final com.larpclient.e y = new com.larpclient.e();
      AbstractContainerMenu L;
      int A;
      int M;
      int P;
      String v = "none";
      String n = "none";
      String Q = "none";

      z(String var1, BlockPos var2, um.e var3, int var4, int var5, int var6, int var7, int var8, ItemStack[] var9, ItemStack var10, Map<Integer, Integer> var11) {
         this.r = var1;
         this.c = var2;
         this.F = var3;
         this.i = var4;
         this.Y = var5;
         this.U = var6;
         this.p = var7;
         this.b = var8;
         this.w = var9;
         this.Z = var10.copyWithCount(1);
         this.O = new HashMap<>(var11);
      }
   }
}
