package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.b4;
import com.larpclient.hq;
import com.larpclient.mp;
import com.larpclient.w4;
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
   private static final int j = 100;
   private static AutoSellModule n;
   private final g B = this.addSetting(new g("Menu Title", "Exact sell-menu title, ignoring case and surrounding spaces", "Sell"));
   private final e t = this.B("Min Cycle Delay", "Minimum ticks between inventory cycles", 1);
   private final e C = this.B("Max Cycle Delay", "Maximum ticks between inventory cycles", 3);
   private final e z = this.B("Min Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 1);
   private final e o = this.B("Max Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 3);
   private final e G = this.B("Menu Open Wait", "Ticks before the additional 20-tick server-response grace period", 3);
   private final s e = this.I("Randomize Delays", "Random cycle range and source pause jitter", true);
   private final s g = this.I("Show Messages", "Report menu opening, transfers, sell clicks and recovery", true);
   private final s F = this.I("Show Stats", "Report attempted confirmations and acknowledged transfers when stopped", true);
   private final s b = this.I("Background Mode", "Archived source preference", true);
   private final l X = this.addSetting(
      new l("Item Mode", "Choose selected items or explicitly allow all inventory items; exclusions always win", b4.Y.getFirst(), b4.Y.toArray(String[]::new))
   );
   private final s K = this.I("Protect Hotbar", "Keep items in the nine hotbar slots", true);
   private final s m = this.I("Protect Named Items", "Keep items with a custom name", true);
   private final s Z = this.I("Protect Enchanted Items", "Keep enchanted items and enchanted books", true);
   private final s q = this.I("Protect Filled Containers", "Keep containers and bundles that contain items", true);
   private final g f = this.q("Selection Version", "1");
   private final g L = this.q("Selection Error", "");
   private final List<g> k = new ArrayList<>();
   private final List<g> x = new ArrayList<>();
   private final w4 Y = new w4();
   private Object D;
   private Object U;
   private Object v;
   private ChestMenu N;
   private final Map<Integer, ItemStack> A = new LinkedHashMap<>();
   private Map<mp.y, Integer> S = Map.of();
   private Map<mp.y, Integer> i = Map.of();
   private mp.q r;
   private int a;
   private int P = -1;
   private int O;
   private int d;
   private int E;
   private int h;
   private int p;
   private int Q;
   private long s;
   private long J;
   private long W;
   private boolean V;
   private boolean R;
   private boolean M;
   private b4.e u;

   public AutoSellModule() {
      super("Auto Sell", "Sell chosen inventory items through a validated server sell menu", Category.PLAYER, -1);
      n = this;
      this.z.P(() -> false);
      this.o.P(() -> false);
      this.b.P(() -> false);

      for (int var1 = 0; var1 < 32; var1++) {
         this.k.add(this.q(b4.z(var1), ""));
         this.x.add(this.q(b4.p(var1), ""));
      }

      for (q var2 : this.getSettings()) {
         if (b4.a().contains(var2.e())) {
            this.m(var2);
         }
      }
   }

   private void m(q var1) {
      var1.o(var1x -> {
         this.u = null;
         this.J++;
      });
   }

   private g q(String var1, String var2) {
      g var3 = this.addSetting(new g(var1, "Auto Sell item selection data", var2));
      var3.P(() -> false);
      return var3;
   }

   private e B(String var1, String var2, int var3) {
      return this.addSetting(new e(var1, var2, var3, 0.0, 20.0, 1.0));
   }

   private s I(String var1, String var2, boolean var3) {
      return this.addSetting(new s(var1, var2, var3));
   }

   public List<q<?>> Z() {
      return List.of(this.B, this.G);
   }

   public List<q<?>> a() {
      return List.of(this.t, this.C, this.z, this.o, this.e);
   }

   public List<q<?>> T() {
      return List.of(this.g, this.F, this.b);
   }

   public List<q<?>> H() {
      return List.of(this.X, this.K, this.m, this.Z, this.q);
   }

   public b4.e a() {
      if (this.u == null) {
         LinkedHashMap var1 = new LinkedHashMap();

         for (q var3 : this.getSettings()) {
            if (b4.a().contains(var3.e())) {
               var1.put(var3.e(), var3.getValue());
            }
         }

         this.u = b4.Y(var1);
      }

      return this.u;
   }

   public String I() {
      b4.e var1 = this.a();
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

   public Set<String> o(boolean var1) {
      return var1 ? this.a().excluded() : this.a().selected();
   }

   public void b(boolean var1, Collection<String> var2) {
      List var3 = b4.p(var2);
      List var4 = var1 ? this.x : this.k;
      if (!this.a().error().isEmpty()) {
         this.X.setValue(b4.Y.getFirst());

         for (g var6 : this.k) {
            var6.k("");
         }

         for (g var9 : this.x) {
            var9.k("");
         }

         this.K.setValue(true);
         this.m.setValue(true);
         this.Z.setValue(true);
         this.q.setValue(true);
      }

      for (int var8 = 0; var8 < var4.size(); var8++) {
         ((g)var4.get(var8)).k(var8 < var3.size() ? (String)var3.get(var8) : "");
      }

      this.f.k("1");
      this.L.k("");
   }

   public void K(Screen var1, boolean var2) {
      getMc().gui.setScreen(new hq(var1, this, var2));
   }

   public void O(boolean var1) {
      if (this.isInGame() && !mc.player.getMainHandItem().isEmpty()) {
         LinkedHashSet var2 = new LinkedHashSet<>(this.o(var1));
         var2.add(M(mc.player.getMainHandItem()));
         this.b(var1, var2);
         this.e();
      } else {
         throw new IllegalArgumentException("Hold an item first.");
      }
   }

   public void p(boolean var1) {
      this.b(var1, Set.of());
      this.e();
   }

   public void e() {
      LarpClient var1 = LarpClient.R();
      if (var1 != null && var1.o() != null) {
         var1.o().C();
      }
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.U);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      }

      b4.e var1 = this.a();
      if (!var1.error().isEmpty()) {
         return "Repair item selection";
      }

      if (var1.needsSelection()) {
         return "Choose items to begin";
      }

      if (this.r != null) {
         return "Waiting for item transfer";
      }

      if (this.R) {
         return "Waiting for sale response";
      }

      return switch (this.Y.F()) {
         case L -> "Waiting for Sell menu";
         case p, D -> "Filling Sell menu";
         case S, d -> "Confirming selected items";
         default -> "Waiting for eligible items";
      };
   }

   @Override
   public void onEnable() {
      this.c();
      if (this.isInGame() && mc.player.connection != null) {
         this.D = mc.player;
         this.U = mc.level;
         this.v = mc.player.connection;
         this.s = System.nanoTime();
         this.W = this.J;
         this.Y(this.a().needsSelection() ? "Choose items to begin; no /sell command will be sent." : "Auto Sell activated.");
         com.larpclient.h.U("AutoSell", "Activated with policy={}, selected={}, excluded={}", this.X.getValue(), this.o(false).size(), this.o(true).size());
      }
   }

   @Override
   public void onDisable() {
      if (this.s != 0L && this.F.getValue() && this.isInGame()) {
         this.u(
            "Auto Sell stopped: " + this.E + " confirmation attempts, " + this.h + " empty-menu acknowledgments, " + this.d + " acknowledged stack transfers."
         );
      }

      if (this.S.isEmpty() && this.r == null && !this.R && (this.N == null || this.X(this.N).isEmpty())) {
         this.O();
      }

      this.c();
   }

   private void c() {
      this.Y.k();
      this.D = this.U = this.v = null;
      this.N = null;
      this.A.clear();
      this.S = this.i = Map.of();
      this.r = null;
      this.P = -1;
      this.O = this.d = this.E = this.h = this.p = this.Q = 0;
      this.V = this.R = this.M = (boolean)0;
      this.s = 0L;
   }

   @Override
   public void onTick() {
      if (this.isInGame() && mc.player.connection != null) {
         if (mc.player != this.D || mc.level != this.U || mc.player.connection != this.v) {
            this.c();
            this.onEnable();
         } else if (!mc.player.isAlive()) {
            this.o("Player is no longer alive; sale cancelled.");
         } else {
            b4.e var1 = this.a();
            if (!var1.error().isEmpty()) {
               this.o(var1.error());
            } else {
               if (this.J != this.W) {
                  if (this.N != null || this.V) {
                     this.o("Item selection changed during a sale. Review the menu before restarting.");
                     return;
                  }

                  this.W = this.J;
               }

               ChestMenu var2 = this.y();
               if (this.N != null && var2 != this.N) {
                  if (this.R && this.M && var2 == null && mc.player.containerMenu == mc.player.inventoryMenu) {
                     mp.j var12 = mp.d(true, ++this.Q, mc.player.containerMenu.getCarried().isEmpty(), this.i, this.M());
                     if (var12 != mp.j.y) {
                        if (var12 == mp.j.T) {
                           this.S = Map.of();
                           this.R = this.M = (boolean)0;
                           this.Q = 0;
                           this.N = null;
                           this.A.clear();
                           this.P = -1;
                           this.Y("Server closed the menu after confirmation. Checking remaining eligible items.");
                           com.larpclient.h.U("AutoSell", "Native close settled after confirmation; sale revenue/outcome not measured");
                           this.J(w4.l.C, this.Q());
                        } else {
                           this.o("Sell menu closed with returned or changed inventory. Review the result before restarting.");
                        }
                     }
                  } else {
                     this.o(
                        this.R
                           ? "Sell menu closed or changed after confirmation; check the sale result before restarting."
                           : "Sell menu closed or changed; pending sale cancelled."
                     );
                  }
               } else if (!mc.player.containerMenu.getCarried().isEmpty()) {
                  if (this.r != null || this.R) {
                     this.Y.b();
                     int var14 = this.Y.l();
                     byte var5 = 100;
                     int var4 = var14;
                     if (Integer.compare(var4, var5) >>> 31 == 0) {
                        this.o("Cursor did not clear after the server response; no further click was sent.");
                     }
                  } else if (this.N != null || this.V) {
                     this.o("Cursor holds an item; sale cancelled without another click.");
                  }
               } else if (mc.gui.screen() == null || var2 != null || this.V) {
                  this.Y.b();
                  if (this.N != null && !this.R && !this.k(var2)) {
                     this.o("Sell menu controls changed; review the menu before restarting.");
                  } else {
                     switch (this.Y.F()) {
                        case L:
                           if (var2 != null) {
                              this.V = (boolean)0;
                              this.p = 0;
                              if (this.e(var2)) {
                                 this.J(w4.l.p, 1);
                              }
                           } else if (this.Y.C()) {
                              this.V = (boolean)0;
                              int var13 = ++this.p;
                              byte var9 = 3;
                              int var8 = var13;
                              if (Integer.compare(var8, var9) >>> 31 == 0) {
                                 this.o("Sell menu did not open after three requests.");
                                 return;
                              }

                              this.J(w4.l.C, this.p(3));
                           }
                           break;
                        case p:
                           this.a(var2);
                           break;
                        case D:
                           if (this.Y.P()) {
                              this.J(w4.l.S, 0);
                           }
                           break;
                        case S:
                           this.f(var2);
                           break;
                        case d:
                           if (!this.Y.P()) {
                              return;
                           }

                           Map var3 = this.X(var2);
                           if (mp.j(this.O, var2.getStateId(), var3, this.i, this.M())) {
                              if (!this.k(var2)) {
                                 this.o("Sell response changed the menu controls; inspect this server layout before restarting.");
                                 return;
                              }

                              this.R = (boolean)0;
                              byte var7 = 1;
                              int var6 = this.h;
                              this.h = var6 + var7;
                              this.S = Map.of();
                              this.Y("Server cleared the selected sell items.");
                              if (this.f()) {
                                 this.J(w4.l.p, this.p(1));
                              } else {
                                 this.O();
                                 this.J(w4.l.C, this.Q());
                              }
                           } else {
                              int var10000 = this.Y.l();
                              byte var11 = 100;
                              int var10 = var10000;
                              if (Integer.compare(var10, var11) >>> 31 == 0) {
                                 this.o("Sale was rejected, returned items, or did not finish. No duplicate confirmation was sent.");
                              }
                           }
                           break;
                        case C:
                           if (!this.Y.P() || var1.needsSelection()) {
                              return;
                           }

                           if (var2 != null && this.f()) {
                              if (this.e(var2)) {
                                 this.J(w4.l.p, 1);
                              }
                           } else if (var2 == null && this.f()) {
                              this.J(w4.l.a, 0);
                           } else {
                              this.J(w4.l.C, this.Q());
                           }
                           break;
                        case a:
                           if (!this.f()) {
                              this.J(w4.l.C, this.Q());
                              return;
                           }

                           if (mc.gui.screen() != null || mc.player.containerMenu != mc.player.inventoryMenu) {
                              return;
                           }

                           this.V = (boolean)1;
                           mc.player.connection.sendCommand("sell");
                           this.Y("Sent /sell command.");
                           this.J(w4.l.L, this.p(this.G.getValue().intValue()));
                           break;
                        default:
                           this.J(w4.l.C, this.Q());
                     }
                  }
               }
            }
         }
      }
   }

   private boolean e(ChestMenu var1) {
      this.N = var1;
      this.A.clear();
      this.P = -1;
      int var2 = var1.getRowCount() * 9;

      for (int var3 = var2 - 9; var3 < var2; var3++) {
         ItemStack var4 = var1.getSlot(var3).getItem();
         if (!var4.isEmpty() && K(var4) && var4.getCount() == 1) {
            if (h(var4, var3, var2)) {
               if (this.P != -1) {
                  this.n(var1);
                  this.o("Multiple sell controls found. This menu layout needs review.");
                  return false;
               }

               this.P = var3;
               this.A.put(var3, var4.copy());
            } else if (mp.m(M(var4), var4.getHoverName().getString(), var3, var2)) {
               this.A.put(var3, var4.copy());
            }
         }
      }

      if (this.P < 0) {
         this.n(var1);
         this.o("Unsupported Sell menu. Expected one named Sell or Confirm pane in its bottom row.");
         return false;
      } else if (!this.X(var1).isEmpty()) {
         this.o("Sell menu already contains items. Empty it manually before starting Auto Sell.");
         return false;
      } else {
         this.S = Map.of();
         this.W = this.J;
         com.larpclient.h.U("AutoSell", "Validated {}-row menu, confirm slot={}, controls={}", var1.getRowCount(), this.P + 1, this.A.size());
         return true;
      }
   }

   private void n(ChestMenu var1) {
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
               .append(M(var5))
               .append(" '")
               .append(var5.getHoverName().getString())
               .append("' customName=")
               .append(var5.has(DataComponents.CUSTOM_NAME))
               .append(" itemName=")
               .append(var5.has(DataComponents.ITEM_NAME));

            for (String var7 : m(var5)) {
               var3.append("\n    | ").append(var7);
            }
         }
      }

      com.larpclient.h.g(
         "AutoSell", "Rejected {}-row menu titled '{}'; nonempty slots:{}", var1.getRowCount(), this.B.getValue(), var3.length() == 0 ? " none" : var3
      );
   }

   private void a(ChestMenu var1) {
      if (var1 != null && mc.gameMode != null) {
         if (this.r != null) {
            ItemStack var5 = var1.getSlot(this.a).getItem();
            int var6 = var5.isEmpty() ? 0 : (J(var5).equals(this.r.key()) ? var5.getCount() : -1);
            int var4 = this.r.accepted(var1.getStateId(), var6, this.X(var1), this.M());
            if (var4 > 0) {
               this.S = mp.t(this.S, this.r.key(), var4);
               this.d++;
               this.r = null;
               if (!this.o(var1)) {
                  return;
               }

               this.J(w4.l.p, this.p(1));
            } else if (this.Y.l() >= 100) {
               this.o("Item transfer was rejected, the menu is full, or the server did not respond. No repeat click was sent.");
            }
         } else if (this.Y.P() && this.o(var1)) {
            for (int var2 = var1.getRowCount() * 9; var2 < var1.slots.size(); var2++) {
               Slot var3 = var1.getSlot(var2);
               if (var3.container == mc.player.getInventory() && this.a(var3.getItem(), var3.getContainerSlot()) && this.d(var1, var3.getItem())) {
                  if (this.y() == var1 && mc.player.containerMenu == var1 && var1.getCarried().isEmpty() && this.J == this.W && this.k(var1)) {
                     this.a = var2;
                     this.r = new mp.q(var1.getStateId(), J(var3.getItem()), var3.getItem().getCount(), this.X(var1), this.M());
                     mc.gameMode.handleContainerInput(var1.containerId, var2, 0, ContainerInput.QUICK_MOVE, mc.player);
                     this.J(w4.l.p, 1);
                     return;
                  }

                  return;
               }
            }

            if (this.S.isEmpty()) {
               this.O();
               this.J(w4.l.C, this.Q());
            } else {
               this.J(w4.l.D, this.p(1));
            }
         }
      }
   }

   private boolean o(ChestMenu var1) {
      if (!this.X(var1).equals(this.S)) {
         this.o("Unexplained or returned items in the sell menu. Review them manually; no confirmation was sent.");
         return false;
      }

      for (int var2 = 0; var2 < var1.getRowCount() * 9; var2++) {
         if (!this.A.containsKey(var2)) {
            ItemStack var3 = var1.getSlot(var2).getItem();
            if (!var3.isEmpty() && !this.a(var3, -1)) {
               this.o("Protected or excluded item in sell slot " + (var2 + 1) + ". No confirmation was sent.");
               return false;
            }
         }
      }

      return true;
   }

   private boolean d(ChestMenu var1, ItemStack var2) {
      for (int var3 = 0; var3 < var1.getRowCount() * 9; var3++) {
         if (!this.A.containsKey(var3)) {
            Slot var4 = var1.getSlot(var3);
            ItemStack var5 = var4.getItem();
            if (mp.b(var5.isEmpty(), J(var2).equals(J(var5)), var4.mayPlace(var2), var5.getCount(), var4.getMaxStackSize(var2))) {
               return true;
            }
         }
      }

      return false;
   }

   private void f(ChestMenu var1) {
      if (var1 != null && mc.gameMode != null && !this.R && !this.S.isEmpty()) {
         if (this.o(var1) && this.k(var1) && this.y() == var1 && var1.getCarried().isEmpty() && mc.player.containerMenu == var1 && this.J == this.W) {
            this.O = var1.getStateId();
            this.i = this.M();
            this.R = (boolean)1;
            mc.gameMode.handleContainerInput(var1.containerId, this.P, 0, ContainerInput.PICKUP, mc.player);
            this.E++;
            this.Y("Sent one confirmation for the selected items.");
            this.J(w4.l.d, this.p(1));
         }
      }
   }

   private boolean k(ChestMenu var1) {
      if (var1 != null && var1 == this.N && this.P >= 0) {
         int var2 = var1.getRowCount() * 9;

         for (Entry var4 : this.A.entrySet()) {
            ItemStack var5 = var1.getSlot((Integer)var4.getKey()).getItem();
            int var6 = var4.getKey() == this.P
               ? (K(var5) && var5.getCount() == 1 && h(var5, this.P, var2) ? 1 : 0)
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

   private static boolean K(ItemStack var0) {
      return var0.has(DataComponents.CUSTOM_NAME) || var0.has(DataComponents.ITEM_NAME);
   }

   private static List<String> m(ItemStack var0) {
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

   private static boolean h(ItemStack var0, int var1, int var2) {
      return mp.A(M(var0), var0.getHoverName().getString(), m(var0), var1, var2);
   }

   private boolean f() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.a(mc.player.getInventory().getItem(var1), var1)) {
            return true;
         }
      }

      return false;
   }

   private boolean a(ItemStack var1, int var2) {
      return this.a().eligible(H(var1, var2));
   }

   public static b4.d H(ItemStack var0, int var1) {
      ItemContainerContents var2 = (ItemContainerContents)var0.get(DataComponents.CONTAINER);
      BundleContents var3 = (BundleContents)var0.get(DataComponents.BUNDLE_CONTENTS);
      ItemEnchantments var4 = (ItemEnchantments)var0.get(DataComponents.STORED_ENCHANTMENTS);
      int var5 = (var2 == null || !var2.nonEmptyItems().iterator().hasNext()) && (var3 == null || var3.isEmpty()) ? 0 : 1;
      return new b4.d(
         M(var0), var0.getCount(), var1, var0.has(DataComponents.CUSTOM_NAME), var0.isEnchanted() || var4 != null && !var4.isEmpty(), (boolean)var5
      );
   }

   private Map<mp.y, Integer> X(ChestMenu var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.getRowCount() * 9; var3++) {
         if (!this.A.containsKey(var3)) {
            a(var2, var1.getSlot(var3).getItem());
         }
      }

      return Map.copyOf(var2);
   }

   private Map<mp.y, Integer> M() {
      HashMap var1 = new HashMap();

      for (int var2 = 0; var2 < 36; var2++) {
         a(var1, mc.player.getInventory().getItem(var2));
      }

      return Map.copyOf(var1);
   }

   private static void a(Map<mp.y, Integer> var0, ItemStack var1) {
      if (!var1.isEmpty()) {
         var0.merge(J(var1), var1.getCount(), Integer::sum);
      }
   }

   public static mp.y J(ItemStack var0) {
      ItemStack var1 = var0.copy();
      var1.remove(DataComponents.LORE);
      return new mp.y(M(var1), var1.getComponents());
   }

   private static String M(ItemStack var0) {
      return BuiltInRegistries.ITEM.getKey(var0.getItem()).toString();
   }

   private ChestMenu y() {
      return mc.gui.screen() instanceof ContainerScreen var1
            && mc.player.containerMenu instanceof ChestMenu var2
            && var1.getMenu() == var2
            && w4.A(var1.getTitle().getString(), this.B.getValue())
         ? var2
         : null;
   }

   public static boolean f(ChestMenu var0) {
      return n != null && n.isEnabled() ? n != null && n.isRuntimeActive() && n.isInGame() && n.y() == var0 : false;
   }

   public static void L(int var0) {
      AutoSellModule var1 = n;
      if (var1 != null && var1.isRuntimeActive() && var1.N != null && var1.R && var1.N.containerId == var0) {
         var1.M = (boolean)1;
         var1.Q = 0;
      }
   }

   private void J(w4.l var1, int var2) {
      this.Y.C(var1, var2);
   }

   private int p(int var1) {
      return w4.Y(var1, this.e.getValue(), ThreadLocalRandom.current());
   }

   private int Q() {
      return w4.k(this.t.getValue().intValue(), this.C.getValue().intValue(), this.e.getValue(), ThreadLocalRandom.current());
   }

   private void O() {
      if (this.N != null && this.isInGame() && mc.player == this.D && mc.level == this.U && mc.player.containerMenu == this.N) {
         mc.player.closeContainer();
      }

      this.N = null;
      this.A.clear();
      this.P = -1;
      this.V = this.R = (boolean)0;
   }

   private void o(String var1) {
      com.larpclient.h.g("AutoSell", "{} Stopped; enable again to retry.", var1);
      this.u(var1);
      this.disable();
   }

   private void Y(String var1) {
      if (this.g.getValue()) {
         this.u(var1);
      }
   }

   private void u(String var1) {
      if (this.isInGame()) {
         mc.gui.chatListener().handleSystemMessage(Component.literal("[Auto Sell] " + var1), false);
      }
   }
}
