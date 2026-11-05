package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.ej;
import com.larpclient.jw;
import com.larpclient.ph;
import com.larpclient.r4;
import com.larpclient.r8;
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
   private static final int u = 100;
   private static AutoSellModule z;
   private final li R = this.addSetting(new li("Menu Title", "Exact sell-menu title, ignoring case and surrounding spaces", "Sell"));
   private final lc c = this.E("Min Cycle Delay", "Minimum ticks between inventory cycles", 1);
   private final lc p = this.E("Max Cycle Delay", "Maximum ticks between inventory cycles", 3);
   private final lc Z = this.E("Min Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 1);
   private final lc r = this.E("Max Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 3);
   private final lc m = this.E("Menu Open Wait", "Ticks before the additional 20-tick server-response grace period", 3);
   private final ln H = this.y("Randomize Delays", "Random cycle range and source pause jitter", true);
   private final ln V = this.y("Show Messages", "Report menu opening, transfers, sell clicks and recovery", true);
   private final ln P = this.y("Show Stats", "Report attempted confirmations and acknowledged transfers when stopped", true);
   private final ln N = this.y("Background Mode", "Archived source preference", true);
   private final lp f = this.addSetting(
      new lp("Item Mode", "Choose selected items or explicitly allow all inventory items; exclusions always win", ej.R.getFirst(), ej.R.toArray(String[]::new))
   );
   private final ln X = this.y("Protect Hotbar", "Keep items in the nine hotbar slots", true);
   private final ln I = this.y("Protect Named Items", "Keep items with a custom name", true);
   private final ln Y = this.y("Protect Enchanted Items", "Keep enchanted items and enchanted books", true);
   private final ln A = this.y("Protect Filled Containers", "Keep containers and bundles that contain items", true);
   private final li x = this.b("Selection Version", "1");
   private final li Q = this.b("Selection Error", "");
   private final List<li> D = new ArrayList<>();
   private final List<li> G = new ArrayList<>();
   private final r8 s = new r8();
   private Object a;
   private Object w;
   private Object o;
   private ChestMenu J;
   private final Map<Integer, ItemStack> y = new LinkedHashMap<>();
   private Map<jw.k, Integer> O = Map.of();
   private Map<jw.k, Integer> q = Map.of();
   private jw.o h;
   private ItemStack v = ItemStack.EMPTY;
   private int k;
   private int d = -1;
   private int i;
   private int T;
   private int g;
   private int W;
   private int j;
   private int b;
   private long e;
   private long S;
   private long L;
   private boolean B;
   private boolean U;
   private boolean K;
   private ej.q t;

   public AutoSellModule() {
      super("Auto Sell", "Sell chosen inventory items through a validated server sell menu", Category.PLAYER, -1);
      z = this;
      this.Z.x(() -> false);
      this.r.x(() -> false);
      this.N.x(() -> false);

      for (int var1 = 0; var1 < 32; var1++) {
         this.D.add(this.b(ej.W(var1), ""));
         this.G.add(this.b(ej.Y(var1), ""));
      }

      for (l6 var2 : this.getSettings()) {
         if (ej.b().contains(var2.k())) {
            this.y(var2);
         }
      }
   }

   private void y(l6 var1) {
      var1.W(var1x -> {
         this.t = null;
         this.S++;
      });
   }

   private li b(String var1, String var2) {
      li var3 = this.addSetting(new li(var1, "Auto Sell item selection data", var2));
      var3.x(() -> false);
      return var3;
   }

   private lc E(String var1, String var2, int var3) {
      return this.addSetting(new lc(var1, var2, var3, 0.0, 20.0, 1.0));
   }

   private ln y(String var1, String var2, boolean var3) {
      return this.addSetting(new ln(var1, var2, var3));
   }

   public List<l6<?>> E() {
      return List.of(this.R, this.m);
   }

   public List<l6<?>> L() {
      return List.of(this.c, this.p, this.Z, this.r, this.H);
   }

   public List<l6<?>> s() {
      return List.of(this.V, this.P, this.N);
   }

   public List<l6<?>> O() {
      return List.of(this.f, this.X, this.I, this.Y, this.A);
   }

   public ej.q l() {
      if (this.t == null) {
         LinkedHashMap var1 = new LinkedHashMap();

         for (l6 var3 : this.getSettings()) {
            if (ej.b().contains(var3.k())) {
               var1.put(var3.k(), var3.getValue());
            }
         }

         this.t = ej.e(var1);
      }

      return this.t;
   }

   public String T() {
      ej.q var1 = this.l();
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

   public Set<String> W(boolean var1) {
      return var1 ? this.l().excluded() : this.l().selected();
   }

   public void U(boolean var1, Collection<String> var2) {
      List var3 = ej.V(var2);
      List var4 = var1 ? this.G : this.D;
      if (!this.l().error().isEmpty()) {
         this.f.setValue(ej.R.getFirst());

         for (li var6 : this.D) {
            var6.i("");
         }

         for (li var9 : this.G) {
            var9.i("");
         }

         this.X.setValue(true);
         this.I.setValue(true);
         this.Y.setValue(true);
         this.A.setValue(true);
      }

      for (int var8 = 0; var8 < var4.size(); var8++) {
         ((li)var4.get(var8)).i(var8 < var3.size() ? (String)var3.get(var8) : "");
      }

      this.x.i("1");
      this.Q.i("");
   }

   public void w(Screen var1, boolean var2) {
      getMc().gui.setScreen(new ph(var1, this, var2));
   }

   public void O(boolean var1) {
      if (this.isInGame() && !mc.player.getMainHandItem().isEmpty()) {
         LinkedHashSet var2 = new LinkedHashSet<>(this.W(var1));
         var2.add(x(mc.player.getMainHandItem()));
         this.U(var1, var2);
         this.V();
      } else {
         throw new IllegalArgumentException("Hold an item first.");
      }
   }

   public void S(boolean var1) {
      this.U(var1, Set.of());
      this.V();
   }

   public void V() {
      LarpClient var1 = LarpClient.y();
      if (var1 != null && var1.v() != null) {
         var1.v().A();
      }
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      }

      ej.q var1 = this.l();
      if (!var1.error().isEmpty()) {
         return "Repair item selection";
      }

      if (var1.needsSelection()) {
         return "Choose items to begin";
      }

      if (this.h != null) {
         return "Waiting for item transfer";
      }

      if (this.U) {
         return "Waiting for sale response";
      }

      return switch (this.s.I()) {
         case V -> "Waiting for Sell menu";
         case e, S -> "Filling Sell menu";
         case Z, Q -> "Confirming selected items";
         default -> "Waiting for eligible items";
      };
   }

   @Override
   public void onEnable() {
      this.M();
      if (this.isInGame() && mc.player.connection != null) {
         this.a = mc.player;
         this.w = mc.level;
         this.o = mc.player.connection;
         this.e = System.nanoTime();
         this.L = this.S;
         this.Y(this.l().needsSelection() ? "Choose items to begin; no /sell command will be sent." : "Auto Sell activated.");
         r4.g("AutoSell", "Activated with policy={}, selected={}, excluded={}", this.f.getValue(), this.W(false).size(), this.W(true).size());
      }
   }

   @Override
   public void onDisable() {
      if (this.e != 0L && this.P.getValue() && this.isInGame()) {
         this.d(
            "Auto Sell stopped: " + this.g + " confirmation attempts, " + this.W + " empty-menu acknowledgments, " + this.T + " acknowledged stack transfers."
         );
      }

      if (this.O.isEmpty() && this.h == null && !this.U && (this.J == null || this.S(this.J).isEmpty())) {
         this.e();
      }

      this.M();
   }

   private void M() {
      this.s.b();
      this.a = this.w = this.o = null;
      this.J = null;
      this.y.clear();
      this.O = this.q = Map.of();
      this.h = null;
      this.v = ItemStack.EMPTY;
      this.d = -1;
      this.i = this.T = this.g = this.W = this.j = this.b = 0;
      this.B = this.U = this.K = (boolean)0;
      this.e = 0L;
   }

   @Override
   public void onTick() {
      if (this.isInGame() && mc.player.connection != null) {
         if (mc.player != this.a || mc.level != this.w || mc.player.connection != this.o) {
            this.M();
            this.onEnable();
         } else if (!mc.player.isAlive()) {
            this.D("Player is no longer alive; sale cancelled.");
         } else {
            ej.q var1 = this.l();
            if (!var1.error().isEmpty()) {
               this.D(var1.error());
            } else {
               if (this.S != this.L) {
                  if (this.J != null || this.B) {
                     this.D("Item selection changed during a sale. Review the menu before restarting.");
                     return;
                  }

                  this.L = this.S;
               }

               ChestMenu var2 = this.S();
               if (this.J != null && var2 != this.J) {
                  if (this.U && this.K && var2 == null && mc.player.containerMenu == mc.player.inventoryMenu) {
                     jw.v var12 = jw.P(true, ++this.b, mc.player.containerMenu.getCarried().isEmpty(), this.q, this.K());
                     if (var12 != jw.v.Y) {
                        if (var12 == jw.v.c) {
                           this.O = Map.of();
                           this.U = this.K = (boolean)0;
                           this.b = 0;
                           this.J = null;
                           this.y.clear();
                           this.d = -1;
                           this.Y("Server closed the menu after confirmation. Checking remaining eligible items.");
                           r4.g("AutoSell", "Native close settled after confirmation; sale revenue/outcome not measured");
                           this.q(r8.v.G, this.Z());
                        } else {
                           this.D("Sell menu closed with returned or changed inventory. Review the result before restarting.");
                        }
                     }
                  } else {
                     this.D(
                        this.U
                           ? "Sell menu closed or changed after confirmation; check the sale result before restarting."
                           : "Sell menu closed or changed; pending sale cancelled."
                     );
                  }
               } else if (!mc.player.containerMenu.getCarried().isEmpty()) {
                  if (this.h != null || this.U) {
                     this.s.H();
                     int var14 = this.s.v();
                     byte var9 = 100;
                     int var8 = var14;
                     if (Integer.compare(var8, var9) >>> 31 == 0) {
                        this.D("Cursor did not clear after the server response; no further click was sent.");
                     }
                  } else if (this.J != null || this.B) {
                     this.D("Cursor holds an item; sale cancelled without another click.");
                  }
               } else if (mc.gui.screen() == null || var2 != null || this.B) {
                  this.s.H();
                  if (this.J != null && !this.U && !this.D(var2)) {
                     this.D("Sell menu controls changed; review the menu before restarting.");
                  } else {
                     switch (this.s.I()) {
                        case V:
                           if (var2 != null) {
                              this.B = (boolean)0;
                              this.j = 0;
                              if (this.v(var2)) {
                                 this.q(r8.v.e, 1);
                              }
                           } else if (this.s.G()) {
                              this.B = (boolean)0;
                              int var13 = ++this.j;
                              byte var5 = 3;
                              int var4 = var13;
                              if (Integer.compare(var4, var5) >>> 31 == 0) {
                                 this.D("Sell menu did not open after three requests.");
                                 return;
                              }

                              this.q(r8.v.G, this.Q(3));
                           }
                           break;
                        case e:
                           this.z(var2);
                           break;
                        case S:
                           if (this.s.L()) {
                              this.q(r8.v.Z, 0);
                           }
                           break;
                        case Z:
                           this.g(var2);
                           break;
                        case Q:
                           if (!this.s.L()) {
                              return;
                           }

                           Map var3 = this.S(var2);
                           if (jw.r(this.i, var2.getStateId(), var3, this.q, this.K())) {
                              if (!this.D(var2)) {
                                 this.D("Sell response changed the menu controls; inspect this server layout before restarting.");
                                 return;
                              }

                              this.U = (boolean)0;
                              byte var7 = 1;
                              int var6 = this.W;
                              this.W = var6 + var7;
                              this.O = Map.of();
                              this.Y("Server cleared the selected sell items.");
                              if (this.J()) {
                                 this.q(r8.v.e, this.Q(1));
                              } else {
                                 this.e();
                                 this.q(r8.v.G, this.Z());
                              }
                           } else {
                              int var10000 = this.s.v();
                              byte var11 = 100;
                              int var10 = var10000;
                              if (Integer.compare(var10, var11) >>> 31 == 0) {
                                 this.D("Sale was rejected, returned items, or did not finish. No duplicate confirmation was sent.");
                              }
                           }
                           break;
                        case G:
                           if (this.s.L() && !var1.needsSelection()) {
                              if (var2 != null && this.J()) {
                                 if (this.v(var2)) {
                                    this.q(r8.v.e, 1);
                                 }
                              } else if (var2 == null && this.J()) {
                                 this.q(r8.v.g, 0);
                              } else {
                                 this.q(r8.v.G, this.Z());
                              }
                              break;
                           }

                           return;
                        case g:
                           if (!this.J()) {
                              this.q(r8.v.G, this.Z());
                              return;
                           }

                           if (mc.gui.screen() != null || mc.player.containerMenu != mc.player.inventoryMenu) {
                              return;
                           }

                           this.B = (boolean)1;
                           mc.player.connection.sendCommand("sell");
                           this.Y("Sent /sell command.");
                           this.q(r8.v.V, this.Q(this.m.getValue().intValue()));
                           break;
                        default:
                           this.q(r8.v.G, this.Z());
                     }
                  }
               }
            }
         }
      }
   }

   private boolean v(ChestMenu var1) {
      this.J = var1;
      this.y.clear();
      this.d = -1;
      int var2 = var1.getRowCount() * 9;

      for (int var3 = var2 - 9; var3 < var2; var3++) {
         ItemStack var4 = var1.getSlot(var3).getItem();
         if (!var4.isEmpty() && var4.has(DataComponents.CUSTOM_NAME) && var4.getCount() == 1) {
            if (jw.T(x(var4), var4.getHoverName().getString(), var3, var2)) {
               if (this.d != -1) {
                  this.D("Multiple sell controls found. This menu layout needs review.");
                  return false;
               }

               this.d = var3;
               this.y.put(var3, var4.copy());
            } else if (jw.e(x(var4), var4.getHoverName().getString(), var3, var2)) {
               this.y.put(var3, var4.copy());
            }
         }
      }

      if (this.d < 0) {
         this.D("Unsupported Sell menu. Expected one named Sell or Confirm pane in its bottom row.");
         return false;
      } else if (!this.S(var1).isEmpty()) {
         this.D("Sell menu already contains items. Empty it manually before starting Auto Sell.");
         return false;
      } else {
         this.O = Map.of();
         this.L = this.S;
         r4.g("AutoSell", "Validated {}-row menu, confirm slot={}, controls={}", var1.getRowCount(), this.d + 1, this.y.size());
         return true;
      }
   }

   private void z(ChestMenu var1) {
      if (var1 != null && mc.gameMode != null) {
         if (this.h != null) {
            ItemStack var5 = var1.getSlot(this.k).getItem();
            int var6 = var5.isEmpty() ? 0 : (ItemStack.isSameItemSameComponents(var5, this.v) ? var5.getCount() : -1);
            int var4 = this.h.accepted(var1.getStateId(), var6, this.S(var1), this.K());
            if (var4 > 0) {
               this.O = jw.e(this.O, this.h.key(), var4);
               this.T++;
               this.h = null;
               this.v = ItemStack.EMPTY;
               if (!this.U(var1)) {
                  return;
               }

               this.q(r8.v.e, this.Q(1));
            } else if (this.s.v() >= 100) {
               this.D("Item transfer was rejected, the menu is full, or the server did not respond. No repeat click was sent.");
            }
         } else if (this.s.L() && this.U(var1)) {
            for (int var2 = var1.getRowCount() * 9; var2 < var1.slots.size(); var2++) {
               Slot var3 = var1.getSlot(var2);
               if (var3.container == mc.player.getInventory() && this.N(var3.getItem(), var3.getContainerSlot()) && this.L(var1, var3.getItem())) {
                  if (this.S() == var1 && mc.player.containerMenu == var1 && var1.getCarried().isEmpty() && this.S == this.L && this.D(var1)) {
                     this.k = var2;
                     this.v = var3.getItem().copy();
                     this.h = new jw.o(var1.getStateId(), W(this.v), this.v.getCount(), this.S(var1), this.K());
                     mc.gameMode.handleContainerInput(var1.containerId, var2, 0, ContainerInput.QUICK_MOVE, mc.player);
                     this.q(r8.v.e, 1);
                     return;
                  }

                  return;
               }
            }

            if (this.O.isEmpty()) {
               this.e();
               this.q(r8.v.G, this.Z());
            } else {
               this.q(r8.v.S, this.Q(1));
            }
         }
      }
   }

   private boolean U(ChestMenu var1) {
      if (!this.S(var1).equals(this.O)) {
         this.D("Unexplained or returned items in the sell menu. Review them manually; no confirmation was sent.");
         return false;
      }

      for (int var2 = 0; var2 < var1.getRowCount() * 9; var2++) {
         if (!this.y.containsKey(var2)) {
            ItemStack var3 = var1.getSlot(var2).getItem();
            if (!var3.isEmpty() && !this.N(var3, -1)) {
               this.D("Protected or excluded item in sell slot " + (var2 + 1) + ". No confirmation was sent.");
               return false;
            }
         }
      }

      return true;
   }

   private boolean L(ChestMenu var1, ItemStack var2) {
      for (int var3 = 0; var3 < var1.getRowCount() * 9; var3++) {
         if (!this.y.containsKey(var3)) {
            Slot var4 = var1.getSlot(var3);
            ItemStack var5 = var4.getItem();
            if (jw.u(var5.isEmpty(), ItemStack.isSameItemSameComponents(var2, var5), var4.mayPlace(var2), var5.getCount(), var4.getMaxStackSize(var2))) {
               return true;
            }
         }
      }

      return false;
   }

   private void g(ChestMenu var1) {
      if (var1 != null && mc.gameMode != null && !this.U && !this.O.isEmpty()) {
         if (this.U(var1) && this.D(var1) && this.S() == var1 && var1.getCarried().isEmpty() && mc.player.containerMenu == var1 && this.S == this.L) {
            this.i = var1.getStateId();
            this.q = this.K();
            this.U = (boolean)1;
            mc.gameMode.handleContainerInput(var1.containerId, this.d, 0, ContainerInput.PICKUP, mc.player);
            this.g++;
            this.Y("Sent one confirmation for the selected items.");
            this.q(r8.v.Q, this.Q(1));
         }
      }
   }

   private boolean D(ChestMenu var1) {
      if (var1 != null && var1 == this.J && this.d >= 0) {
         for (Entry var3 : this.y.entrySet()) {
            if (!ItemStack.matches((ItemStack)var3.getValue(), var1.getSlot((Integer)var3.getKey()).getItem())) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean J() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.N(mc.player.getInventory().getItem(var1), var1)) {
            return true;
         }
      }

      return false;
   }

   private boolean N(ItemStack var1, int var2) {
      return this.l().eligible(r(var1, var2));
   }

   public static ej.v r(ItemStack var0, int var1) {
      ItemContainerContents var2 = (ItemContainerContents)var0.get(DataComponents.CONTAINER);
      BundleContents var3 = (BundleContents)var0.get(DataComponents.BUNDLE_CONTENTS);
      ItemEnchantments var4 = (ItemEnchantments)var0.get(DataComponents.STORED_ENCHANTMENTS);
      int var5 = (var2 == null || !var2.nonEmptyItems().iterator().hasNext()) && (var3 == null || var3.isEmpty()) ? 0 : 1;
      return new ej.v(
         x(var0), var0.getCount(), var1, var0.has(DataComponents.CUSTOM_NAME), var0.isEnchanted() || var4 != null && !var4.isEmpty(), (boolean)var5
      );
   }

   private Map<jw.k, Integer> S(ChestMenu var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.getRowCount() * 9; var3++) {
         if (!this.y.containsKey(var3)) {
            M(var2, var1.getSlot(var3).getItem());
         }
      }

      return Map.copyOf(var2);
   }

   private Map<jw.k, Integer> K() {
      HashMap var1 = new HashMap();

      for (int var2 = 0; var2 < 36; var2++) {
         M(var1, mc.player.getInventory().getItem(var2));
      }

      return Map.copyOf(var1);
   }

   private static void M(Map<jw.k, Integer> var0, ItemStack var1) {
      if (!var1.isEmpty()) {
         var0.merge(W(var1), var1.getCount(), Integer::sum);
      }
   }

   private static jw.k W(ItemStack var0) {
      return new jw.k(x(var0), var0.copy().getComponents());
   }

   private static String x(ItemStack var0) {
      return BuiltInRegistries.ITEM.getKey(var0.getItem()).toString();
   }

   private ChestMenu S() {
      return mc.gui.screen() instanceof ContainerScreen var1
            && mc.player.containerMenu instanceof ChestMenu var2
            && var1.getMenu() == var2
            && r8.F(var1.getTitle().getString(), this.R.getValue())
         ? var2
         : null;
   }

   public static boolean Q(ChestMenu var0) {
      return z != null && z.isEnabled() ? z != null && z.isRuntimeActive() && z.isInGame() && z.S() == var0 : false;
   }

   public static void O(int var0) {
      AutoSellModule var1 = z;
      if (var1 != null && var1.isRuntimeActive() && var1.J != null && var1.U && var1.J.containerId == var0) {
         var1.K = (boolean)1;
         var1.b = 0;
      }
   }

   private void q(r8.v var1, int var2) {
      this.s.U(var1, var2);
   }

   private int Q(int var1) {
      return r8.k(var1, this.H.getValue(), ThreadLocalRandom.current());
   }

   private int Z() {
      return r8.b(this.c.getValue().intValue(), this.p.getValue().intValue(), this.H.getValue(), ThreadLocalRandom.current());
   }

   private void e() {
      if (this.J != null && this.isInGame() && mc.player == this.a && mc.level == this.w && mc.player.containerMenu == this.J) {
         mc.player.closeContainer();
      }

      this.J = null;
      this.y.clear();
      this.d = -1;
      this.B = this.U = (boolean)0;
   }

   private void D(String var1) {
      r4.m("AutoSell", "{} Stopped; enable again to retry.", var1);
      this.d(var1);
      this.disable();
   }

   private void Y(String var1) {
      if (this.V.getValue()) {
         this.d(var1);
      }
   }

   private void d(String var1) {
      if (this.isInGame()) {
         mc.gui.chatListener().handleSystemMessage(Component.literal("[Auto Sell] " + var1), false);
      }
   }
}
