package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.ch;
import com.larpclient.hi;
import com.larpclient.k7;
import com.larpclient.mr;
import com.larpclient.mt;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class AutoSellModule extends Module {
   private static final int D = 100;
   private static AutoSellModule B;
   private final ds X = this.addSetting(new ds("Menu Title", "Exact sell-menu title, ignoring case and surrounding spaces", "Sell"));
   private final d9 o = this.M("Min Cycle Delay", "Minimum ticks between inventory cycles", 1);
   private final d9 H = this.M("Max Cycle Delay", "Maximum ticks between inventory cycles", 3);
   private final d9 S = this.M("Min Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 1);
   private final d9 r = this.M("Max Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 3);
   private final d9 L = this.M("Menu Open Wait", "Ticks before the additional 20-tick server-response grace period", 3);
   private final dh j = this.O("Randomize Delays", "Random cycle range and source pause jitter", true);
   private final dh k = this.O("Show Messages", "Report menu opening, transfers, sell clicks and recovery", true);
   private final dh i = this.O("Show Stats", "Report attempted confirmations and acknowledged transfers when stopped", true);
   private final dh J = this.O("Background Mode", "Archived source preference", true);
   private final d6 A = this.addSetting(
      new d6("Item Mode", "Choose selected items or explicitly allow all inventory items; exclusions always win", ch.V.getFirst(), ch.V.toArray(String[]::new))
   );
   private final dh f = this.O("Protect Hotbar", "Keep items in the nine hotbar slots", true);
   private final dh x = this.O("Protect Named Items", "Keep items with a custom name", true);
   private final dh e = this.O("Protect Enchanted Items", "Keep enchanted items and enchanted books", true);
   private final dh n = this.O("Protect Filled Containers", "Keep containers and bundles that contain items", true);
   private final ds E = this.I("Selection Version", "1");
   private final ds h = this.I("Selection Error", "");
   private final List<ds> s = new ArrayList<>();
   private final List<ds> g = new ArrayList<>();
   private final k7 V = new k7();
   private Object t;
   private Object p;
   private Object I;
   private ChestMenu G;
   private final Map<Integer, ItemStack> c = new LinkedHashMap<>();
   private Map<mr.h, Integer> K = Map.of();
   private Map<mr.h, Integer> w = Map.of();
   private mr.l N;
   private ItemStack U = ItemStack.EMPTY;
   private int Z;
   private int Q = -1;
   private int F;
   private int Y;
   private int z;
   private int P;
   private int R;
   private int m;
   private long T;
   private long C;
   private long q;
   private boolean M;
   private boolean b;
   private boolean W;
   private ch.z l;

   public AutoSellModule() {
      super("Auto Sell", "Sell chosen inventory items through a validated server sell menu", Category.PLAYER, -1);
      B = this;
      this.S.H(() -> false);
      this.r.H(() -> false);
      this.J.H(() -> false);

      for (int var1 = 0; var1 < 32; var1++) {
         this.s.add(this.I(ch.o(var1), ""));
         this.g.add(this.I(ch.Q(var1), ""));
      }

      for (df var2 : this.getSettings()) {
         if (ch.G().contains(var2.D())) {
            this.n(var2);
         }
      }
   }

   private void n(df var1) {
      var1.h(var1x -> {
         this.l = null;
         this.C++;
      });
   }

   private ds I(String var1, String var2) {
      ds var3 = this.addSetting(new ds(var1, "Auto Sell item selection data", var2));
      var3.H(() -> false);
      return var3;
   }

   private d9 M(String var1, String var2, int var3) {
      return this.addSetting(new d9(var1, var2, var3, 0.0, 20.0, 1.0));
   }

   private dh O(String var1, String var2, boolean var3) {
      return this.addSetting(new dh(var1, var2, var3));
   }

   public List<df<?>> i() {
      return List.of(this.X, this.L);
   }

   public List<df<?>> M() {
      return List.of(this.o, this.H, this.S, this.r, this.j);
   }

   public List<df<?>> p() {
      return List.of(this.k, this.i, this.J);
   }

   public List<df<?>> L() {
      return List.of(this.A, this.f, this.x, this.e, this.n);
   }

   public ch.z K() {
      if (this.l == null) {
         LinkedHashMap var1 = new LinkedHashMap();

         for (df var3 : this.getSettings()) {
            if (ch.G().contains(var3.D())) {
               var1.put(var3.D(), var3.P());
            }
         }

         this.l = ch.o(var1);
      }

      return this.l;
   }

   public String h() {
      ch.z var1 = this.K();
      if (!var1.error().isEmpty()) {
         return var1.error();
      }

      long var2 = var1.selected().stream().filter(var0 -> !BuiltInRegistries.ITEM.containsKey(Identifier.parse(var0))).count();
      return var1.mode()
         + ": "
         + var1.selected().size()
         + " selected, "
         + var1.excluded().size()
         + " excluded"
         + (var2 == 0L ? "" : ", " + var2 + " unavailable")
         + (var1.needsSelection() ? ". Choose items to begin." : "");
   }

   public Set<String> r(boolean var1) {
      return var1 ? this.K().excluded() : this.K().selected();
   }

   public void P(boolean var1, Collection<String> var2) {
      List var3 = ch.U(var2);
      List var4 = var1 ? this.g : this.s;
      if (!this.K().error().isEmpty()) {
         this.A.setValue(ch.V.getFirst());

         for (ds var6 : this.s) {
            var6.v("");
         }

         for (ds var9 : this.g) {
            var9.v("");
         }

         this.f.setValue(true);
         this.x.setValue(true);
         this.e.setValue(true);
         this.n.setValue(true);
      }

      for (int var8 = 0; var8 < var4.size(); var8++) {
         ((ds)var4.get(var8)).v(var8 < var3.size() ? (String)var3.get(var8) : "");
      }

      this.E.v("1");
      this.h.v("");
   }

   public void N(Screen var1, boolean var2) {
      getMc().gui.setScreen(new hi(var1, this, var2));
   }

   public void j(boolean var1) {
      if (this.isInGame() && !mc.player.getMainHandItem().isEmpty()) {
         LinkedHashSet var2 = new LinkedHashSet<>(this.r(var1));
         var2.add(F(mc.player.getMainHandItem()));
         this.P(var1, var2);
         this.A();
      } else {
         throw new IllegalArgumentException("Hold an item first.");
      }
   }

   public void w(boolean var1) {
      this.P(var1, Set.of());
      this.A();
   }

   public void A() {
      LarpClient var1 = LarpClient.F();
      if (var1 != null && var1.f() != null) {
         var1.f().f();
      }
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      }

      ch.z var1 = this.K();
      if (!var1.error().isEmpty()) {
         return "Repair item selection";
      }

      if (var1.needsSelection()) {
         return "Choose items to begin";
      }

      if (this.N != null) {
         return "Waiting for item transfer";
      }

      if (this.b) {
         return "Waiting for sale response";
      }

      return switch (this.V.R()) {
         case u -> "Waiting for Sell menu";
         case l, q -> "Filling Sell menu";
         case n, M -> "Confirming selected items";
         default -> "Waiting for eligible items";
      };
   }

   @Override
   public void onEnable() {
      this.Y();
      if (this.isInGame() && mc.player.connection != null) {
         this.t = mc.player;
         this.p = mc.level;
         this.I = mc.player.connection;
         this.T = System.nanoTime();
         this.q = this.C;
         this.n(this.K().needsSelection() ? "Choose items to begin; no /sell command will be sent." : "Auto Sell activated.");
         mt.U("AutoSell", "Activated with policy={}, selected={}, excluded={}", this.A.P(), this.r(false).size(), this.r(true).size());
      }
   }

   @Override
   public void onDisable() {
      if (this.T != 0L && this.i.P() && this.isInGame()) {
         this.l(
            "Auto Sell stopped: " + this.z + " confirmation attempts, " + this.P + " empty-menu acknowledgments, " + this.Y + " acknowledged stack transfers."
         );
      }

      if (this.K.isEmpty() && this.N == null && !this.b && (this.G == null || this.u(this.G).isEmpty())) {
         this.B();
      }

      this.Y();
   }

   private void Y() {
      this.V.T();
      this.t = this.p = this.I = null;
      this.G = null;
      this.c.clear();
      this.K = this.w = Map.of();
      this.N = null;
      this.U = ItemStack.EMPTY;
      this.Q = -1;
      this.F = this.Y = this.z = this.P = this.R = this.m = 0;
      this.M = this.b = this.W = (boolean)0;
      this.T = 0L;
   }

   @Override
   public void onTick() {
      if (this.isInGame() && mc.player.connection != null) {
         if (mc.player != this.t || mc.level != this.p || mc.player.connection != this.I) {
            this.Y();
            this.onEnable();
         } else if (!mc.player.isAlive()) {
            this.h("Player is no longer alive; sale cancelled.");
         } else {
            ch.z var1 = this.K();
            if (!var1.error().isEmpty()) {
               this.h(var1.error());
            } else {
               if (this.C != this.q) {
                  if (this.G != null || this.M) {
                     this.h("Item selection changed during a sale. Review the menu before restarting.");
                     return;
                  }

                  this.q = this.C;
               }

               ChestMenu var2 = this.Z();
               if (this.G != null && var2 != this.G) {
                  if (this.b && this.W && var2 == null && mc.player.containerMenu == mc.player.inventoryMenu) {
                     mr.a var12 = mr.F(true, ++this.m, mc.player.containerMenu.getCarried().isEmpty(), this.w, this.s());
                     if (var12 != mr.a.g) {
                        if (var12 == mr.a.L) {
                           this.K = Map.of();
                           this.b = this.W = (boolean)0;
                           this.m = 0;
                           this.G = null;
                           this.c.clear();
                           this.Q = -1;
                           this.n("Server closed the menu after confirmation. Checking remaining eligible items.");
                           mt.U("AutoSell", "Native close settled after confirmation; sale revenue/outcome not measured");
                           this.K(k7.y.K, this.w());
                        } else {
                           this.h("Sell menu closed with returned or changed inventory. Review the result before restarting.");
                        }
                     }
                  } else {
                     this.h(
                        this.b
                           ? "Sell menu closed or changed after confirmation; check the sale result before restarting."
                           : "Sell menu closed or changed; pending sale cancelled."
                     );
                  }
               } else if (!mc.player.containerMenu.getCarried().isEmpty()) {
                  if (this.N != null || this.b) {
                     this.V.b();
                     int var14 = this.V.y();
                     byte var11 = 100;
                     int var10 = var14;
                     if (Integer.compare(var10, var11) >>> 31 == 0) {
                        this.h("Cursor did not clear after the server response; no further click was sent.");
                     }
                  } else if (this.G != null || this.M) {
                     this.h("Cursor holds an item; sale cancelled without another click.");
                  }
               } else if (mc.gui.screen() == null || var2 != null || this.M) {
                  this.V.b();
                  if (this.G != null && !this.b && !this.C(var2)) {
                     this.h("Sell menu controls changed; review the menu before restarting.");
                  } else {
                     switch (this.V.R()) {
                        case u:
                           if (var2 != null) {
                              this.M = (boolean)0;
                              this.R = 0;
                              if (this.r(var2)) {
                                 this.K(k7.y.l, 1);
                              }
                           } else if (this.V.p()) {
                              this.M = (boolean)0;
                              int var13 = ++this.R;
                              byte var5 = 3;
                              int var4 = var13;
                              if (Integer.compare(var4, var5) >>> 31 == 0) {
                                 this.h("Sell menu did not open after three requests.");
                                 return;
                              }

                              this.K(k7.y.K, this.p(3));
                           }
                           break;
                        case l:
                           this.f(var2);
                           break;
                        case q:
                           if (this.V.o()) {
                              this.K(k7.y.n, 0);
                           }
                           break;
                        case n:
                           this.Q(var2);
                           break;
                        case M:
                           if (!this.V.o()) {
                              return;
                           }

                           Map var3 = this.u(var2);
                           if (mr.b(this.F, var2.getStateId(), var3, this.w, this.s())) {
                              if (!this.C(var2)) {
                                 this.h("Sell response changed the menu controls; inspect this server layout before restarting.");
                                 return;
                              }

                              this.b = (boolean)0;
                              byte var9 = 1;
                              int var8 = this.P;
                              this.P = var8 + var9;
                              this.K = Map.of();
                              this.n("Server cleared the selected sell items.");
                              if (this.T()) {
                                 this.K(k7.y.l, this.p(1));
                              } else {
                                 this.B();
                                 this.K(k7.y.K, this.w());
                              }
                           } else {
                              int var10000 = this.V.y();
                              byte var7 = 100;
                              int var6 = var10000;
                              if (Integer.compare(var6, var7) >>> 31 == 0) {
                                 this.h("Sale was rejected, returned items, or did not finish. No duplicate confirmation was sent.");
                              }
                           }
                           break;
                        case K:
                           if (!this.V.o() || var1.needsSelection()) {
                              return;
                           }

                           if (var2 != null && this.T()) {
                              if (this.r(var2)) {
                                 this.K(k7.y.l, 1);
                              }
                           } else if (var2 == null && this.T()) {
                              this.K(k7.y.g, 0);
                           } else {
                              this.K(k7.y.K, this.w());
                           }
                           break;
                        case g:
                           if (!this.T()) {
                              this.K(k7.y.K, this.w());
                              return;
                           }

                           if (mc.gui.screen() == null && mc.player.containerMenu == mc.player.inventoryMenu) {
                              this.M = (boolean)1;
                              mc.player.connection.sendCommand("sell");
                              this.n("Sent /sell command.");
                              this.K(k7.y.u, this.p(this.L.P().intValue()));
                              break;
                           }

                           return;
                        default:
                           this.K(k7.y.K, this.w());
                     }
                  }
               }
            }
         }
      }
   }

   private boolean r(ChestMenu var1) {
      this.G = var1;
      this.c.clear();
      this.Q = -1;
      int var2 = var1.getRowCount() * 9;

      for (int var3 = var2 - 9; var3 < var2; var3++) {
         ItemStack var4 = var1.getSlot(var3).getItem();
         if (!var4.isEmpty() && var4.has(DataComponents.CUSTOM_NAME) && var4.getCount() == 1) {
            if (mr.G(F(var4), var4.getHoverName().getString(), var3, var2)) {
               if (this.Q != -1) {
                  this.h("Multiple sell controls found. This menu layout needs review.");
                  return false;
               }

               this.Q = var3;
               this.c.put(var3, var4.copy());
            } else if (mr.D(F(var4), var4.getHoverName().getString(), var3, var2)) {
               this.c.put(var3, var4.copy());
            }
         }
      }

      if (this.Q < 0) {
         this.h("Unsupported Sell menu. Expected one named Sell or Confirm pane in its bottom row.");
         return false;
      } else if (!this.u(var1).isEmpty()) {
         this.h("Sell menu already contains items. Empty it manually before starting Auto Sell.");
         return false;
      } else {
         this.K = Map.of();
         this.q = this.C;
         mt.U("AutoSell", "Validated {}-row menu, confirm slot={}, controls={}", var1.getRowCount(), this.Q + 1, this.c.size());
         return true;
      }
   }

   private void f(ChestMenu var1) {
      if (var1 != null && mc.gameMode != null) {
         if (this.N != null) {
            ItemStack var5 = var1.getSlot(this.Z).getItem();
            int var6 = var5.isEmpty() ? 0 : (ItemStack.isSameItemSameComponents(var5, this.U) ? var5.getCount() : -1);
            int var4 = this.N.accepted(var1.getStateId(), var6, this.u(var1), this.s());
            if (var4 > 0) {
               this.K = mr.x(this.K, this.N.key(), var4);
               this.Y++;
               this.N = null;
               this.U = ItemStack.EMPTY;
               if (!this.H(var1)) {
                  return;
               }

               this.K(k7.y.l, this.p(1));
            } else if (this.V.y() >= 100) {
               this.h("Item transfer was rejected, the menu is full, or the server did not respond. No repeat click was sent.");
            }
         } else if (this.V.o() && this.H(var1)) {
            for (int var2 = var1.getRowCount() * 9; var2 < var1.slots.size(); var2++) {
               Slot var3 = var1.getSlot(var2);
               if (var3.container == mc.player.getInventory() && this.x(var3.getItem(), var3.getContainerSlot()) && this.K(var1, var3.getItem())) {
                  if (this.Z() == var1 && mc.player.containerMenu == var1 && var1.getCarried().isEmpty() && this.C == this.q && this.C(var1)) {
                     this.Z = var2;
                     this.U = var3.getItem().copy();
                     this.N = new mr.l(var1.getStateId(), z(this.U), this.U.getCount(), this.u(var1), this.s());
                     mc.gameMode.handleContainerInput(var1.containerId, var2, 0, ContainerInput.QUICK_MOVE, mc.player);
                     this.K(k7.y.l, 1);
                     return;
                  }

                  return;
               }
            }

            if (this.K.isEmpty()) {
               this.B();
               this.K(k7.y.K, this.w());
            } else {
               this.K(k7.y.q, this.p(1));
            }
         }
      }
   }

   private boolean H(ChestMenu var1) {
      if (!this.u(var1).equals(this.K)) {
         this.h("Unexplained or returned items in the sell menu. Review them manually; no confirmation was sent.");
         return false;
      }

      for (int var2 = 0; var2 < var1.getRowCount() * 9; var2++) {
         if (!this.c.containsKey(var2)) {
            ItemStack var3 = var1.getSlot(var2).getItem();
            if (!var3.isEmpty() && !this.x(var3, -1)) {
               this.h("Protected or excluded item in sell slot " + (var2 + 1) + ". No confirmation was sent.");
               return false;
            }
         }
      }

      return true;
   }

   private boolean K(ChestMenu var1, ItemStack var2) {
      for (int var3 = 0; var3 < var1.getRowCount() * 9; var3++) {
         if (!this.c.containsKey(var3)) {
            Slot var4 = var1.getSlot(var3);
            ItemStack var5 = var4.getItem();
            if (mr.x(var5.isEmpty(), ItemStack.isSameItemSameComponents(var2, var5), var4.mayPlace(var2), var5.getCount(), var4.getMaxStackSize(var2))) {
               return true;
            }
         }
      }

      return false;
   }

   private void Q(ChestMenu var1) {
      if (var1 != null && mc.gameMode != null && !this.b && !this.K.isEmpty()) {
         if (this.H(var1) && this.C(var1) && this.Z() == var1 && var1.getCarried().isEmpty() && mc.player.containerMenu == var1 && this.C == this.q) {
            this.F = var1.getStateId();
            this.w = this.s();
            this.b = (boolean)1;
            mc.gameMode.handleContainerInput(var1.containerId, this.Q, 0, ContainerInput.PICKUP, mc.player);
            this.z++;
            this.n("Sent one confirmation for the selected items.");
            this.K(k7.y.M, this.p(1));
         }
      }
   }

   private boolean C(ChestMenu var1) {
      if (var1 != null && var1 == this.G && this.Q >= 0) {
         for (Entry var3 : this.c.entrySet()) {
            if (!ItemStack.matches((ItemStack)var3.getValue(), var1.getSlot((Integer)var3.getKey()).getItem())) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean T() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.x(mc.player.getInventory().getItem(var1), var1)) {
            return true;
         }
      }

      return false;
   }

   private boolean x(ItemStack var1, int var2) {
      return this.K().eligible(n(var1, var2));
   }

   public static ch.t n(ItemStack var0, int var1) {
      ItemContainerContents var2 = (ItemContainerContents)var0.get(DataComponents.CONTAINER);
      BundleContents var3 = (BundleContents)var0.get(DataComponents.BUNDLE_CONTENTS);
      ItemEnchantments var4 = (ItemEnchantments)var0.get(DataComponents.STORED_ENCHANTMENTS);
      int var5 = (var2 == null || !var2.nonEmptyItems().iterator().hasNext()) && (var3 == null || var3.isEmpty()) ? 0 : 1;
      return new ch.t(
         F(var0), var0.getCount(), var1, var0.has(DataComponents.CUSTOM_NAME), var0.isEnchanted() || var4 != null && !var4.isEmpty(), (boolean)var5
      );
   }

   private Map<mr.h, Integer> u(ChestMenu var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.getRowCount() * 9; var3++) {
         if (!this.c.containsKey(var3)) {
            r(var2, var1.getSlot(var3).getItem());
         }
      }

      return Map.copyOf(var2);
   }

   private Map<mr.h, Integer> s() {
      HashMap var1 = new HashMap();

      for (int var2 = 0; var2 < 36; var2++) {
         r(var1, mc.player.getInventory().getItem(var2));
      }

      return Map.copyOf(var1);
   }

   private static void r(Map<mr.h, Integer> var0, ItemStack var1) {
      if (!var1.isEmpty()) {
         var0.merge(z(var1), var1.getCount(), Integer::sum);
      }
   }

   private static mr.h z(ItemStack var0) {
      return new mr.h(F(var0), var0.copy().getComponents());
   }

   private static String F(ItemStack var0) {
      return BuiltInRegistries.ITEM.getKey(var0.getItem()).toString();
   }

   private ChestMenu Z() {
      return mc.gui.screen() instanceof ContainerScreen var1
            && mc.player.containerMenu instanceof ChestMenu var2
            && var1.getMenu() == var2
            && k7.P(var1.getTitle().getString(), this.X.P())
         ? var2
         : null;
   }

   public static boolean U(ChestMenu var0) {
      return B != null && B.isEnabled() ? B != null && B.isRuntimeActive() && B.isInGame() && B.Z() == var0 : false;
   }

   public static void T(int var0) {
      AutoSellModule var1 = B;
      if (var1 != null && var1.isRuntimeActive() && var1.G != null && var1.b && var1.G.containerId == var0) {
         var1.W = (boolean)1;
         var1.m = 0;
      }
   }

   private void K(k7.y var1, int var2) {
      this.V.R(var1, var2);
   }

   private int p(int var1) {
      return k7.u(var1, this.j.P(), ThreadLocalRandom.current());
   }

   private int w() {
      return k7.b(this.o.P().intValue(), this.H.P().intValue(), this.j.P(), ThreadLocalRandom.current());
   }

   private void B() {
      if (this.G != null && this.isInGame() && mc.player == this.t && mc.level == this.p && mc.player.containerMenu == this.G) {
         mc.player.closeContainer();
      }

      this.G = null;
      this.c.clear();
      this.Q = -1;
      this.M = this.b = (boolean)0;
   }

   private void h(String var1) {
      mt.T("AutoSell", "{} Stopped; enable again to retry.", var1);
      this.l(var1);
      this.disable();
   }

   private void n(String var1) {
      if (this.k.P()) {
         this.l(var1);
      }
   }

   private void l(String var1) {
      if (this.isInGame()) {
         mc.gui.chatListener().handleSystemMessage(Component.literal("[Auto Sell] " + var1), false);
      }
   }
}
