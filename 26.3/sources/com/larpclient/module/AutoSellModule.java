package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._q;
import com.larpclient.as;
import com.larpclient.f4;
import com.larpclient.s7;
import com.larpclient.v5;
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
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class AutoSellModule extends Module {
   private static final int F = 100;
   private static AutoSellModule W;
   private final e Y = this.addSetting(new e("Menu Title", "Exact sell-menu title, ignoring case and surrounding spaces", "Sell"));
   private final b p = this.S("Min Cycle Delay", "Minimum ticks between inventory cycles", 1);
   private final b E = this.S("Max Cycle Delay", "Maximum ticks between inventory cycles", 3);
   private final b P = this.S("Min Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 1);
   private final b S = this.S("Max Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 3);
   private final b H = this.S("Menu Open Wait", "Ticks before the additional 20-tick server-response grace period", 3);
   private final m o = this.k("Randomize Delays", "Random cycle range and source pause jitter", true);
   private final m L = this.k("Show Messages", "Report menu opening, transfers, sell clicks and recovery", true);
   private final m T = this.k("Show Stats", "Report attempted confirmations and acknowledged transfers when stopped", true);
   private final m z = this.k("Background Mode", "Archived source preference", true);
   private final o U = this.addSetting(
      new o("Item Mode", "Choose selected items or explicitly allow all inventory items; exclusions always win", as.n.getFirst(), as.n.toArray(String[]::new))
   );
   private final m b = this.k("Protect Hotbar", "Keep items in the nine hotbar slots", true);
   private final m O = this.k("Protect Named Items", "Keep items with a custom name", true);
   private final m N = this.k("Protect Enchanted Items", "Keep enchanted items and enchanted books", true);
   private final m s = this.k("Protect Filled Containers", "Keep containers and bundles that contain items", true);
   private final e B = this.o("Selection Version", "1");
   private final e e = this.o("Selection Error", "");
   private final List<e> w = new ArrayList<>();
   private final List<e> R = new ArrayList<>();
   private final s7 X = new s7();
   private Object g;
   private Object J;
   private Object u;
   private ChestMenu v;
   private final Map<Integer, ItemStack> c = new LinkedHashMap<>();
   private Map<v5.p, Integer> q = Map.of();
   private Map<v5.p, Integer> r = Map.of();
   private v5.j f;
   private int y;
   private int D = -1;
   private int C;
   private int G;
   private int n;
   private int h;
   private int t;
   private int K;
   private long m;
   private long Z;
   private long k;
   private boolean x;
   private boolean I;
   private boolean a;
   private as.r Q;

   public AutoSellModule() {
      super("Auto Sell", "Sell chosen inventory items through a validated server sell menu", Category.PLAYER, -1);
      W = this;
      this.P.a(() -> false);
      this.S.a(() -> false);
      this.z.a(() -> false);

      for (int var1 = 0; var1 < 32; var1++) {
         this.w.add(this.o(as.K(var1), ""));
         this.R.add(this.o(as.y(var1), ""));
      }

      for (r var2 : this.getSettings()) {
         if (as.Z().contains(var2.X())) {
            this.v(var2);
         }
      }
   }

   private void v(r var1) {
      var1.w(var1x -> {
         this.Q = null;
         this.Z++;
      });
   }

   private e o(String var1, String var2) {
      e var3 = this.addSetting(new e(var1, "Auto Sell item selection data", var2));
      var3.a(() -> false);
      return var3;
   }

   private b S(String var1, String var2, int var3) {
      return this.addSetting(new b(var1, var2, var3, 0.0, 20.0, 1.0));
   }

   private m k(String var1, String var2, boolean var3) {
      return this.addSetting(new m(var1, var2, var3));
   }

   public List<r<?>> K() {
      return List.of(this.Y, this.H);
   }

   public List<r<?>> a() {
      return List.of(this.p, this.E, this.P, this.S, this.o);
   }

   public List<r<?>> g() {
      return List.of(this.L, this.T, this.z);
   }

   public List<r<?>> P() {
      return List.of(this.U, this.b, this.O, this.N, this.s);
   }

   public as.r U() {
      if (this.Q == null) {
         LinkedHashMap var1 = new LinkedHashMap();

         for (r var3 : this.getSettings()) {
            if (as.Z().contains(var3.X())) {
               var1.put(var3.X(), var3.getValue());
            }
         }

         this.Q = as.S(var1);
      }

      return this.Q;
   }

   public String m() {
      as.r var1 = this.U();
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

   public Set<String> M(boolean var1) {
      return var1 ? this.U().excluded() : this.U().selected();
   }

   public void W(boolean var1, Collection<String> var2) {
      List var3 = as.K(var2);
      List var4 = var1 ? this.R : this.w;
      if (!this.U().error().isEmpty()) {
         this.U.setValue(as.n.getFirst());

         for (e var6 : this.w) {
            var6.T("");
         }

         for (e var9 : this.R) {
            var9.T("");
         }

         this.b.setValue(true);
         this.O.setValue(true);
         this.N.setValue(true);
         this.s.setValue(true);
      }

      for (int var8 = 0; var8 < var4.size(); var8++) {
         ((e)var4.get(var8)).T(var8 < var3.size() ? (String)var3.get(var8) : "");
      }

      this.B.T("1");
      this.e.T("");
   }

   public void b(Screen var1, boolean var2) {
      getMc().gui.setScreen(new f4(var1, this, var2));
   }

   public void z(boolean var1) {
      if (this.isInGame() && !mc.player.getMainHandItem().isEmpty()) {
         LinkedHashSet var2 = new LinkedHashSet<>(this.M(var1));
         var2.add(Q(mc.player.getMainHandItem()));
         this.W(var1, var2);
         this.n();
      } else {
         throw new IllegalArgumentException("Hold an item first.");
      }
   }

   public void y(boolean var1) {
      this.W(var1, Set.of());
      this.n();
   }

   public void n() {
      LarpClient var1 = LarpClient.N();
      if (var1 != null && var1.h() != null) {
         var1.h().z();
      }
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      }

      as.r var1 = this.U();
      if (!var1.error().isEmpty()) {
         return "Repair item selection";
      }

      if (var1.needsSelection()) {
         return "Choose items to begin";
      }

      if (this.f != null) {
         return "Waiting for item transfer";
      }

      if (this.I) {
         return "Waiting for sale response";
      }

      return switch (this.X.u()) {
         case N -> "Waiting for Sell menu";
         case M, H -> "Filling Sell menu";
         case Z, G -> "Confirming selected items";
         default -> "Waiting for eligible items";
      };
   }

   @Override
   public void onEnable() {
      this.q();
      if (this.isInGame() && mc.player.connection != null) {
         this.g = mc.player;
         this.J = mc.level;
         this.u = mc.player.connection;
         this.m = System.nanoTime();
         this.k = this.Z;
         this.h(this.U().needsSelection() ? "Choose items to begin; no /sell command will be sent." : "Auto Sell activated.");
         _q.c("AutoSell", "Activated with policy={}, selected={}, excluded={}", this.U.getValue(), this.M(false).size(), this.M(true).size());
      }
   }

   @Override
   public void onDisable() {
      if (this.m != 0L && this.T.getValue() && this.isInGame()) {
         this.u(
            "Auto Sell stopped: " + this.n + " confirmation attempts, " + this.h + " empty-menu acknowledgments, " + this.G + " acknowledged stack transfers."
         );
      }

      if (this.q.isEmpty() && this.f == null && !this.I && (this.v == null || this.V(this.v).isEmpty())) {
         this.d();
      }

      this.q();
   }

   private void q() {
      this.X.L();
      this.g = this.J = this.u = null;
      this.v = null;
      this.c.clear();
      this.q = this.r = Map.of();
      this.f = null;
      this.D = -1;
      this.C = this.G = this.n = this.h = this.t = this.K = 0;
      this.x = this.I = this.a = (boolean)0;
      this.m = 0L;
   }

   @Override
   public void onTick() {
      if (this.isInGame() && mc.player.connection != null) {
         if (mc.player != this.g || mc.level != this.J || mc.player.connection != this.u) {
            this.q();
            this.onEnable();
         } else if (!mc.player.isAlive()) {
            this.L("Player is no longer alive; sale cancelled.");
         } else {
            as.r var1 = this.U();
            if (!var1.error().isEmpty()) {
               this.L(var1.error());
            } else {
               if (this.Z != this.k) {
                  if (this.v != null || this.x) {
                     this.L("Item selection changed during a sale. Review the menu before restarting.");
                     return;
                  }

                  this.k = this.Z;
               }

               ChestMenu var2 = this.R();
               if (this.v != null && var2 != this.v) {
                  if (this.I && this.a && var2 == null && mc.player.containerMenu == mc.player.inventoryMenu) {
                     v5.l var12 = v5.L(true, ++this.K, mc.player.containerMenu.getCarried().isEmpty(), this.r, this.Z());
                     if (var12 != v5.l.w) {
                        if (var12 == v5.l.P) {
                           this.q = Map.of();
                           this.I = this.a = (boolean)0;
                           this.K = 0;
                           this.v = null;
                           this.c.clear();
                           this.D = -1;
                           this.h("Server closed the menu after confirmation. Checking remaining eligible items.");
                           _q.c("AutoSell", "Native close settled after confirmation; sale revenue/outcome not measured");
                           this.o(s7.i.F, this.N());
                        } else {
                           this.L("Sell menu closed with returned or changed inventory. Review the result before restarting.");
                        }
                     }
                  } else {
                     this.L(
                        this.I
                           ? "Sell menu closed or changed after confirmation; check the sale result before restarting."
                           : "Sell menu closed or changed; pending sale cancelled."
                     );
                  }
               } else if (!mc.player.containerMenu.getCarried().isEmpty()) {
                  if (this.f != null || this.I) {
                     this.X.x();
                     int var14 = this.X.k();
                     byte var7 = 100;
                     int var6 = var14;
                     if (Integer.compare(var6, var7) >>> 31 == 0) {
                        this.L("Cursor did not clear after the server response; no further click was sent.");
                     }
                  } else if (this.v != null || this.x) {
                     this.L("Cursor holds an item; sale cancelled without another click.");
                  }
               } else if (mc.gui.screen() == null || var2 != null || this.x) {
                  this.X.x();
                  if (this.v != null && !this.I && !this.c(var2)) {
                     this.L("Sell menu controls changed; review the menu before restarting.");
                  } else {
                     switch (this.X.u()) {
                        case N:
                           if (var2 != null) {
                              this.x = (boolean)0;
                              this.t = 0;
                              if (this.E(var2)) {
                                 this.o(s7.i.M, 1);
                              }
                           } else if (this.X.D()) {
                              this.x = (boolean)0;
                              int var13 = ++this.t;
                              byte var5 = 3;
                              int var4 = var13;
                              if (Integer.compare(var4, var5) >>> 31 == 0) {
                                 this.L("Sell menu did not open after three requests.");
                                 return;
                              }

                              this.o(s7.i.F, this.Z(3));
                           }
                           break;
                        case M:
                           this.N(var2);
                           break;
                        case H:
                           if (this.X.C()) {
                              this.o(s7.i.Z, 0);
                           }
                           break;
                        case Z:
                           this.n(var2);
                           break;
                        case G:
                           if (!this.X.C()) {
                              return;
                           }

                           Map var3 = this.V(var2);
                           if (v5.c(this.C, var2.getStateId(), var3, this.r, this.Z())) {
                              if (!this.c(var2)) {
                                 this.L("Sell response changed the menu controls; inspect this server layout before restarting.");
                                 return;
                              }

                              this.I = (boolean)0;
                              byte var11 = 1;
                              int var10 = this.h;
                              this.h = var10 + var11;
                              this.q = Map.of();
                              this.h("Server cleared the selected sell items.");
                              if (this.A()) {
                                 this.o(s7.i.M, this.Z(1));
                              } else {
                                 this.d();
                                 this.o(s7.i.F, this.N());
                              }
                           } else {
                              int var10000 = this.X.k();
                              byte var9 = 100;
                              int var8 = var10000;
                              if (Integer.compare(var8, var9) >>> 31 == 0) {
                                 this.L("Sale was rejected, returned items, or did not finish. No duplicate confirmation was sent.");
                              }
                           }
                           break;
                        case F:
                           if (!this.X.C() || var1.needsSelection()) {
                              return;
                           }

                           if (var2 != null && this.A()) {
                              if (this.E(var2)) {
                                 this.o(s7.i.M, 1);
                              }
                           } else if (var2 == null && this.A()) {
                              this.o(s7.i.j, 0);
                           } else {
                              this.o(s7.i.F, this.N());
                           }
                           break;
                        case j:
                           if (!this.A()) {
                              this.o(s7.i.F, this.N());
                              return;
                           }

                           if (mc.gui.screen() == null && mc.player.containerMenu == mc.player.inventoryMenu) {
                              this.x = (boolean)1;
                              mc.player.connection.sendCommand("sell");
                              this.h("Sent /sell command.");
                              this.o(s7.i.N, this.Z(this.H.getValue().intValue()));
                              break;
                           }

                           return;
                        default:
                           this.o(s7.i.F, this.N());
                     }
                  }
               }
            }
         }
      }
   }

   private boolean E(ChestMenu var1) {
      this.v = var1;
      this.c.clear();
      this.D = -1;
      int var2 = var1.getRowCount() * 9;

      for (int var3 = var2 - 9; var3 < var2; var3++) {
         ItemStack var4 = var1.getSlot(var3).getItem();
         if (!var4.isEmpty() && n(var4) && var4.getCount() == 1) {
            if (J(var4, var3, var2)) {
               if (this.D != -1) {
                  this.C(var1);
                  this.L("Multiple sell controls found. This menu layout needs review.");
                  return false;
               }

               this.D = var3;
               this.c.put(var3, var4.copy());
            } else if (v5.D(Q(var4), var4.getHoverName().getString(), var3, var2)) {
               this.c.put(var3, var4.copy());
            }
         }
      }

      if (this.D < 0) {
         this.C(var1);
         this.L("Unsupported Sell menu. Expected one named Sell or Confirm pane in its bottom row.");
         return false;
      } else if (!this.V(var1).isEmpty()) {
         this.L("Sell menu already contains items. Empty it manually before starting Auto Sell.");
         return false;
      } else {
         this.q = Map.of();
         this.k = this.Z;
         _q.c("AutoSell", "Validated {}-row menu, confirm slot={}, controls={}", var1.getRowCount(), this.D + 1, this.c.size());
         return true;
      }
   }

   private void C(ChestMenu var1) {
      int var2 = var1.getRowCount() * 9;
      StringBuilder var3 = new StringBuilder();

      for (int var4 = 0; var4 < var2; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         if (!var5.isEmpty()) {
            var3.append("\n  slot ")
               .append(var4 + 1)
               .append(var4 >= var2 - 9 ? " (bottom)" : "")
               .append(": ")
               .append(var5.getCount())
               .append("x ")
               .append(Q(var5))
               .append(" '")
               .append(var5.getHoverName().getString())
               .append("' customName=")
               .append(var5.has(DataComponents.CUSTOM_NAME))
               .append(" itemName=")
               .append(var5.has(DataComponents.ITEM_NAME));

            for (String var7 : Q(var5)) {
               var3.append("\n    | ").append(var7);
            }
         }
      }

      _q.R("AutoSell", "Rejected {}-row menu titled '{}'; nonempty slots:{}", var1.getRowCount(), this.Y.getValue(), var3.length() == 0 ? " none" : var3);
   }

   private void N(ChestMenu var1) {
      if (var1 != null && mc.gameMode != null) {
         if (this.f != null) {
            ItemStack var5 = var1.getSlot(this.y).getItem();
            int var6 = var5.isEmpty() ? 0 : (g(var5).equals(this.f.key()) ? var5.getCount() : -1);
            int var4 = this.f.accepted(var1.getStateId(), var6, this.V(var1), this.Z());
            if (var4 > 0) {
               this.q = v5.R(this.q, this.f.key(), var4);
               this.G++;
               this.f = null;
               if (!this.j(var1)) {
                  return;
               }

               this.o(s7.i.M, this.Z(1));
            } else if (this.X.k() >= 100) {
               this.L("Item transfer was rejected, the menu is full, or the server did not respond. No repeat click was sent.");
            }
         } else if (this.X.C() && this.j(var1)) {
            for (int var2 = var1.getRowCount() * 9; var2 < var1.slots.size(); var2++) {
               Slot var3 = var1.getSlot(var2);
               if (var3.container == mc.player.getInventory() && this.f(var3.getItem(), var3.getContainerSlot()) && this.k(var1, var3.getItem())) {
                  if (this.R() == var1 && mc.player.containerMenu == var1 && var1.getCarried().isEmpty() && this.Z == this.k && this.c(var1)) {
                     this.y = var2;
                     this.f = new v5.j(var1.getStateId(), g(var3.getItem()), var3.getItem().getCount(), this.V(var1), this.Z());
                     mc.gameMode.handleContainerInput(var1.containerId, var2, 0, ContainerInput.QUICK_MOVE, mc.player);
                     this.o(s7.i.M, 1);
                     return;
                  }

                  return;
               }
            }

            if (this.q.isEmpty()) {
               this.d();
               this.o(s7.i.F, this.N());
            } else {
               this.o(s7.i.H, this.Z(1));
            }
         }
      }
   }

   private boolean j(ChestMenu var1) {
      if (!this.V(var1).equals(this.q)) {
         this.L("Unexplained or returned items in the sell menu. Review them manually; no confirmation was sent.");
         return false;
      }

      for (int var2 = 0; var2 < var1.getRowCount() * 9; var2++) {
         if (!this.c.containsKey(var2)) {
            ItemStack var3 = var1.getSlot(var2).getItem();
            if (!var3.isEmpty() && !this.f(var3, -1)) {
               this.L("Protected or excluded item in sell slot " + (var2 + 1) + ". No confirmation was sent.");
               return false;
            }
         }
      }

      return true;
   }

   private boolean k(ChestMenu var1, ItemStack var2) {
      for (int var3 = 0; var3 < var1.getRowCount() * 9; var3++) {
         if (!this.c.containsKey(var3)) {
            Slot var4 = var1.getSlot(var3);
            ItemStack var5 = var4.getItem();
            if (v5.L(var5.isEmpty(), g(var2).equals(g(var5)), var4.mayPlace(var2), var5.getCount(), var4.getMaxStackSize(var2))) {
               return true;
            }
         }
      }

      return false;
   }

   private void n(ChestMenu var1) {
      if (var1 != null && mc.gameMode != null && !this.I && !this.q.isEmpty()) {
         if (this.j(var1) && this.c(var1) && this.R() == var1 && var1.getCarried().isEmpty() && mc.player.containerMenu == var1 && this.Z == this.k) {
            this.C = var1.getStateId();
            this.r = this.Z();
            this.I = (boolean)1;
            mc.gameMode.handleContainerInput(var1.containerId, this.D, 0, ContainerInput.PICKUP, mc.player);
            this.n++;
            this.h("Sent one confirmation for the selected items.");
            this.o(s7.i.G, this.Z(1));
         }
      }
   }

   private boolean c(ChestMenu var1) {
      if (var1 != null && var1 == this.v && this.D >= 0) {
         int var2 = var1.getRowCount() * 9;

         for (Entry var4 : this.c.entrySet()) {
            ItemStack var5 = var1.getSlot((Integer)var4.getKey()).getItem();
            int var6 = var4.getKey() == this.D
               ? (n(var5) && var5.getCount() == 1 && J(var5, this.D, var2) ? 1 : 0)
               : ItemStack.matches((ItemStack)var4.getValue(), var5);
            if (var6 == 0) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean n(ItemStack var0) {
      return var0.has(DataComponents.CUSTOM_NAME) || var0.has(DataComponents.ITEM_NAME);
   }

   private static List<String> Q(ItemStack var0) {
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

   private static boolean J(ItemStack var0, int var1, int var2) {
      return v5.a(Q(var0), var0.getHoverName().getString(), Q(var0), var1, var2);
   }

   private boolean A() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.f(mc.player.getInventory().getItem(var1), var1)) {
            return true;
         }
      }

      return false;
   }

   private boolean f(ItemStack var1, int var2) {
      return this.U().eligible(C(var1, var2));
   }

   public static as.b C(ItemStack var0, int var1) {
      ItemContainerContents var2 = (ItemContainerContents)var0.get(DataComponents.CONTAINER);
      BundleContents var3 = (BundleContents)var0.get(DataComponents.BUNDLE_CONTENTS);
      ItemEnchantments var4 = (ItemEnchantments)var0.get(DataComponents.STORED_ENCHANTMENTS);
      int var5 = (var2 == null || !var2.nonEmptyItems().iterator().hasNext()) && (var3 == null || var3.isEmpty()) ? 0 : 1;
      return new as.b(
         Q(var0), var0.getCount(), var1, var0.has(DataComponents.CUSTOM_NAME), var0.isEnchanted() || var4 != null && !var4.isEmpty(), (boolean)var5
      );
   }

   private Map<v5.p, Integer> V(ChestMenu var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.getRowCount() * 9; var3++) {
         if (!this.c.containsKey(var3)) {
            o(var2, var1.getSlot(var3).getItem());
         }
      }

      return Map.copyOf(var2);
   }

   private Map<v5.p, Integer> Z() {
      HashMap var1 = new HashMap();

      for (int var2 = 0; var2 < 36; var2++) {
         o(var1, mc.player.getInventory().getItem(var2));
      }

      return Map.copyOf(var1);
   }

   private static void o(Map<v5.p, Integer> var0, ItemStack var1) {
      if (!var1.isEmpty()) {
         var0.merge(g(var1), var1.getCount(), Integer::sum);
      }
   }

   public static v5.p g(ItemStack var0) {
      ItemStack var1 = var0.copy();
      var1.remove(DataComponents.LORE);
      return new v5.p(Q(var1), var1.getComponents());
   }

   private static String Q(ItemStack var0) {
      return BuiltInRegistries.ITEM.getKey(var0.getItem()).toString();
   }

   private ChestMenu R() {
      return mc.gui.screen() instanceof ContainerScreen var1
            && mc.player.containerMenu instanceof ChestMenu var2
            && var1.getMenu() == var2
            && s7.M(var1.getTitle().getString(), this.Y.getValue())
         ? var2
         : null;
   }

   public static boolean O(ChestMenu var0) {
      return W != null && W.isEnabled() ? W != null && W.isRuntimeActive() && W.isInGame() && W.R() == var0 : false;
   }

   public static void F(int var0) {
      AutoSellModule var1 = W;
      if (var1 != null && var1.isRuntimeActive() && var1.v != null && var1.I && var1.v.containerId == var0) {
         var1.a = (boolean)1;
         var1.K = 0;
      }
   }

   private void o(s7.i var1, int var2) {
      this.X.o(var1, var2);
   }

   private int Z(int var1) {
      return s7.F(var1, this.o.getValue(), ThreadLocalRandom.current());
   }

   private int N() {
      return s7.o(this.p.getValue().intValue(), this.E.getValue().intValue(), this.o.getValue(), ThreadLocalRandom.current());
   }

   private void d() {
      if (this.v != null && this.isInGame() && mc.player == this.g && mc.level == this.J && mc.player.containerMenu == this.v) {
         mc.player.closeContainer();
      }

      this.v = null;
      this.c.clear();
      this.D = -1;
      this.x = this.I = (boolean)0;
   }

   private void L(String var1) {
      _q.R("AutoSell", "{} Stopped; enable again to retry.", var1);
      this.u(var1);
      this.disable();
   }

   private void h(String var1) {
      if (this.L.getValue()) {
         this.u(var1);
      }
   }

   private void u(String var1) {
      if (this.isInGame()) {
         mc.gui.chatListener().handleSystemMessage(Component.literal("[Auto Sell] " + var1), false);
      }
   }
}
