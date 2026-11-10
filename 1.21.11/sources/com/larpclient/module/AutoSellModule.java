package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.i8;
import com.larpclient.ii;
import com.larpclient.q8;
import com.larpclient.t8;
import com.larpclient.uk;
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
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_437;
import net.minecraft.class_476;
import net.minecraft.class_7923;
import net.minecraft.class_9276;
import net.minecraft.class_9288;
import net.minecraft.class_9290;
import net.minecraft.class_9304;
import net.minecraft.class_9334;

public final class AutoSellModule extends Module {
   private static final int F = 100;
   private static AutoSellModule z;
   private final aj m = this.addSetting(new aj("Menu Title", "Exact sell-menu title, ignoring case and surrounding spaces", "Sell"));
   private final lo S = this.W("Min Cycle Delay", "Minimum ticks between inventory cycles", 1);
   private final lo u = this.W("Max Cycle Delay", "Maximum ticks between inventory cycles", 3);
   private final lo q = this.W("Min Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 1);
   private final lo O = this.W("Max Click Delay", "Archived reference preference; transfers wait for server acknowledgment", 3);
   private final lo e = this.W("Menu Open Wait", "Ticks before the additional 20-tick server-response grace period", 3);
   private final l3 K = this.O("Randomize Delays", "Random cycle range and source pause jitter", true);
   private final l3 Y = this.O("Show Messages", "Report menu opening, transfers, sell clicks and recovery", true);
   private final l3 i = this.O("Show Stats", "Report attempted confirmations and acknowledged transfers when stopped", true);
   private final l3 L = this.O("Background Mode", "Archived source preference", true);
   private final li h = this.addSetting(
      new li("Item Mode", "Choose selected items or explicitly allow all inventory items; exclusions always win", i8.u.getFirst(), i8.u.toArray(String[]::new))
   );
   private final l3 V = this.O("Protect Hotbar", "Keep items in the nine hotbar slots", true);
   private final l3 H = this.O("Protect Named Items", "Keep items with a custom name", true);
   private final l3 M = this.O("Protect Enchanted Items", "Keep enchanted items and enchanted books", true);
   private final l3 T = this.O("Protect Filled Containers", "Keep containers and bundles that contain items", true);
   private final aj J = this.L("Selection Version", "1");
   private final aj W = this.L("Selection Error", "");
   private final List<aj> I = new ArrayList<>();
   private final List<aj> d = new ArrayList<>();
   private final uk n = new uk();
   private Object A;
   private Object t;
   private Object v;
   private class_1707 x;
   private final Map<Integer, class_1799> N = new LinkedHashMap<>();
   private Map<ii.w, Integer> P = Map.of();
   private Map<ii.w, Integer> U = Map.of();
   private ii.c X;
   private int g;
   private int p = -1;
   private int G;
   private int C;
   private int Q;
   private int f;
   private int R;
   private int D;
   private long l;
   private long Z;
   private long c;
   private boolean o;
   private boolean k;
   private boolean B;
   private i8.e y;

   public AutoSellModule() {
      super("Auto Sell", "Sell chosen inventory items through a validated server sell menu", Category.PLAYER, -1);
      z = this;
      this.q.O(() -> false);
      this.O.O(() -> false);
      this.L.O(() -> false);

      for (int var1 = 0; var1 < 32; var1++) {
         this.I.add(this.L(i8.i(var1), ""));
         this.d.add(this.L(i8.R(var1), ""));
      }

      for (lb var2 : this.getSettings()) {
         if (i8.f().contains(var2.R())) {
            this.d(var2);
         }
      }
   }

   private void d(lb var1) {
      var1.a(var1x -> {
         this.y = null;
         this.Z++;
      });
   }

   private aj L(String var1, String var2) {
      aj var3 = this.addSetting(new aj(var1, "Auto Sell item selection data", var2));
      var3.O(() -> false);
      return var3;
   }

   private lo W(String var1, String var2, int var3) {
      return this.addSetting(new lo(var1, var2, var3, 0.0, 20.0, 1.0));
   }

   private l3 O(String var1, String var2, boolean var3) {
      return this.addSetting(new l3(var1, var2, var3));
   }

   public List<lb<?>> t() {
      return List.of(this.m, this.e);
   }

   public List<lb<?>> J() {
      return List.of(this.S, this.u, this.q, this.O, this.K);
   }

   public List<lb<?>> m() {
      return List.of(this.Y, this.i, this.L);
   }

   public List<lb<?>> d() {
      return List.of(this.h, this.V, this.H, this.M, this.T);
   }

   public i8.e j() {
      if (this.y == null) {
         LinkedHashMap var1 = new LinkedHashMap();

         for (lb var3 : this.getSettings()) {
            if (i8.f().contains(var3.R())) {
               var1.put(var3.R(), var3.getValue());
            }
         }

         this.y = i8.j(var1);
      }

      return this.y;
   }

   public String a() {
      i8.e var1 = this.j();
      if (!var1.error().isEmpty()) {
         return var1.error();
      }

      long var2 = var1.selected().stream().filter(var0 -> !class_7923.field_41178.method_10250(class_2960.method_60654(var0))).count();
      return var1.mode()
         + ": "
         + var1.selected().size()
         + " selected, "
         + var1.excluded().size()
         + " excluded"
         + (var2 == 0L ? "" : ", " + var2 + " unavailable")
         + (var1.needsSelection() ? ". Choose items to begin." : "");
   }

   public Set<String> L(boolean var1) {
      return var1 ? this.j().excluded() : this.j().selected();
   }

   public void A(boolean var1, Collection<String> var2) {
      List var3 = i8.O(var2);
      List var4 = var1 ? this.d : this.I;
      if (!this.j().error().isEmpty()) {
         this.h.setValue(i8.u.getFirst());

         for (aj var6 : this.I) {
            var6.Y("");
         }

         for (aj var9 : this.d) {
            var9.Y("");
         }

         this.V.setValue(true);
         this.H.setValue(true);
         this.M.setValue(true);
         this.T.setValue(true);
      }

      for (int var8 = 0; var8 < var4.size(); var8++) {
         ((aj)var4.get(var8)).Y(var8 < var3.size() ? (String)var3.get(var8) : "");
      }

      this.J.Y("1");
      this.W.Y("");
   }

   public void a(class_437 var1, boolean var2) {
      getMc().method_1507(new t8(var1, this, var2));
   }

   public void B(boolean var1) {
      if (this.isInGame() && !mc.field_1724.method_6047().method_7960()) {
         LinkedHashSet var2 = new LinkedHashSet<>(this.L(var1));
         var2.add(n(mc.field_1724.method_6047()));
         this.A(var1, var2);
         this.T();
      } else {
         throw new IllegalArgumentException("Hold an item first.");
      }
   }

   public void u(boolean var1) {
      this.A(var1, Set.of());
      this.T();
   }

   public void T() {
      LarpClient var1 = LarpClient.b();
      if (var1 != null && var1.e() != null) {
         var1.e().U();
      }
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Idle";
      }

      i8.e var1 = this.j();
      if (!var1.error().isEmpty()) {
         return "Repair item selection";
      }

      if (var1.needsSelection()) {
         return "Choose items to begin";
      }

      if (this.X != null) {
         return "Waiting for item transfer";
      }

      if (this.k) {
         return "Waiting for sale response";
      }

      return switch (this.n.o()) {
         case L -> "Waiting for Sell menu";
         case h, X -> "Filling Sell menu";
         case o, A -> "Confirming selected items";
         default -> "Waiting for eligible items";
      };
   }

   @Override
   public void onEnable() {
      this.N();
      if (this.isInGame() && mc.field_1724.field_3944 != null) {
         this.A = mc.field_1724;
         this.t = mc.field_1687;
         this.v = mc.field_1724.field_3944;
         this.l = System.nanoTime();
         this.c = this.Z;
         this.H(this.j().needsSelection() ? "Choose items to begin; no /sell command will be sent." : "Auto Sell activated.");
         q8.v("AutoSell", "Activated with policy={}, selected={}, excluded={}", this.h.getValue(), this.L(false).size(), this.L(true).size());
      }
   }

   @Override
   public void onDisable() {
      if (this.l != 0L && this.i.getValue() && this.isInGame()) {
         this.v(
            "Auto Sell stopped: " + this.Q + " confirmation attempts, " + this.f + " empty-menu acknowledgments, " + this.C + " acknowledged stack transfers."
         );
      }

      if (this.P.isEmpty() && this.X == null && !this.k && (this.x == null || this.V(this.x).isEmpty())) {
         this.l();
      }

      this.N();
   }

   private void N() {
      this.n.S();
      this.A = this.t = this.v = null;
      this.x = null;
      this.N.clear();
      this.P = this.U = Map.of();
      this.X = null;
      this.p = -1;
      this.G = this.C = this.Q = this.f = this.R = this.D = 0;
      this.o = this.k = this.B = (boolean)0;
      this.l = 0L;
   }

   @Override
   public void onTick() {
      if (this.isInGame() && mc.field_1724.field_3944 != null) {
         if (mc.field_1724 != this.A || mc.field_1687 != this.t || mc.field_1724.field_3944 != this.v) {
            this.N();
            this.onEnable();
         } else if (!mc.field_1724.method_5805()) {
            this.U("Player is no longer alive; sale cancelled.");
         } else {
            i8.e var1 = this.j();
            if (!var1.error().isEmpty()) {
               this.U(var1.error());
            } else {
               if (this.Z != this.c) {
                  if (this.x != null || this.o) {
                     this.U("Item selection changed during a sale. Review the menu before restarting.");
                     return;
                  }

                  this.c = this.Z;
               }

               class_1707 var2 = this.p();
               if (this.x != null && var2 != this.x) {
                  if (this.k && this.B && var2 == null && mc.field_1724.field_7512 == mc.field_1724.field_7498) {
                     ii.j var12 = ii.x(true, ++this.D, mc.field_1724.field_7512.method_34255().method_7960(), this.U, this.l());
                     if (var12 != ii.j.j) {
                        if (var12 == ii.j.Y) {
                           this.P = Map.of();
                           this.k = this.B = (boolean)0;
                           this.D = 0;
                           this.x = null;
                           this.N.clear();
                           this.p = -1;
                           this.H("Server closed the menu after confirmation. Checking remaining eligible items.");
                           q8.v("AutoSell", "Native close settled after confirmation; sale revenue/outcome not measured");
                           this.A(uk.r.s, this.u());
                        } else {
                           this.U("Sell menu closed with returned or changed inventory. Review the result before restarting.");
                        }
                     }
                  } else {
                     this.U(
                        this.k
                           ? "Sell menu closed or changed after confirmation; check the sale result before restarting."
                           : "Sell menu closed or changed; pending sale cancelled."
                     );
                  }
               } else if (!mc.field_1724.field_7512.method_34255().method_7960()) {
                  if (this.X != null || this.k) {
                     this.n.L();
                     int var14 = this.n.c();
                     byte var7 = 100;
                     int var6 = var14;
                     if (Integer.compare(var6, var7) >>> 31 == 0) {
                        this.U("Cursor did not clear after the server response; no further click was sent.");
                     }
                  } else if (this.x != null || this.o) {
                     this.U("Cursor holds an item; sale cancelled without another click.");
                  }
               } else if (mc.field_1755 == null || var2 != null || this.o) {
                  this.n.L();
                  if (this.x != null && !this.k && !this.t(var2)) {
                     this.U("Sell menu controls changed; review the menu before restarting.");
                  } else {
                     switch (this.n.o()) {
                        case L:
                           if (var2 != null) {
                              this.o = (boolean)0;
                              this.R = 0;
                              if (this.H(var2)) {
                                 this.A(uk.r.h, 1);
                              }
                           } else if (this.n.P()) {
                              this.o = (boolean)0;
                              int var13 = ++this.R;
                              byte var11 = 3;
                              int var10 = var13;
                              if (Integer.compare(var10, var11) >>> 31 == 0) {
                                 this.U("Sell menu did not open after three requests.");
                                 return;
                              }

                              this.A(uk.r.s, this.d(3));
                           }
                           break;
                        case h:
                           this.d(var2);
                           break;
                        case X:
                           if (this.n.X()) {
                              this.A(uk.r.o, 0);
                           }
                           break;
                        case o:
                           this.K(var2);
                           break;
                        case A:
                           if (!this.n.X()) {
                              return;
                           }

                           Map var3 = this.V(var2);
                           if (ii.f(this.G, var2.method_37421(), var3, this.U, this.l())) {
                              if (!this.t(var2)) {
                                 this.U("Sell response changed the menu controls; inspect this server layout before restarting.");
                                 return;
                              }

                              this.k = (boolean)0;
                              byte var9 = 1;
                              int var8 = this.f;
                              this.f = var8 + var9;
                              this.P = Map.of();
                              this.H("Server cleared the selected sell items.");
                              if (this.C()) {
                                 this.A(uk.r.h, this.d(1));
                              } else {
                                 this.l();
                                 this.A(uk.r.s, this.u());
                              }
                           } else {
                              int var10000 = this.n.c();
                              byte var5 = 100;
                              int var4 = var10000;
                              if (Integer.compare(var4, var5) >>> 31 == 0) {
                                 this.U("Sale was rejected, returned items, or did not finish. No duplicate confirmation was sent.");
                              }
                           }
                           break;
                        case s:
                           if (this.n.X() && !var1.needsSelection()) {
                              if (var2 != null && this.C()) {
                                 if (this.H(var2)) {
                                    this.A(uk.r.h, 1);
                                 }
                              } else if (var2 == null && this.C()) {
                                 this.A(uk.r.u, 0);
                              } else {
                                 this.A(uk.r.s, this.u());
                              }
                              break;
                           }

                           return;
                        case u:
                           if (!this.C()) {
                              this.A(uk.r.s, this.u());
                              return;
                           }

                           if (mc.field_1755 == null && mc.field_1724.field_7512 == mc.field_1724.field_7498) {
                              this.o = (boolean)1;
                              mc.field_1724.field_3944.method_45730("sell");
                              this.H("Sent /sell command.");
                              this.A(uk.r.L, this.d(this.e.getValue().intValue()));
                              break;
                           }

                           return;
                        default:
                           this.A(uk.r.s, this.u());
                     }
                  }
               }
            }
         }
      }
   }

   private boolean H(class_1707 var1) {
      this.x = var1;
      this.N.clear();
      this.p = -1;
      int var2 = var1.method_17388() * 9;

      for (int var3 = var2 - 9; var3 < var2; var3++) {
         class_1799 var4 = var1.method_7611(var3).method_7677();
         if (!var4.method_7960() && v(var4) && var4.method_7947() == 1) {
            if (B(var4, var3, var2)) {
               if (this.p != -1) {
                  this.W(var1);
                  this.U("Multiple sell controls found. This menu layout needs review.");
                  return false;
               }

               this.p = var3;
               this.N.put(var3, var4.method_7972());
            } else if (ii.z(n(var4), var4.method_7964().getString(), var3, var2)) {
               this.N.put(var3, var4.method_7972());
            }
         }
      }

      if (this.p < 0) {
         this.W(var1);
         this.U("Unsupported Sell menu. Expected one named Sell or Confirm pane in its bottom row.");
         return false;
      } else if (!this.V(var1).isEmpty()) {
         this.U("Sell menu already contains items. Empty it manually before starting Auto Sell.");
         return false;
      } else {
         this.P = Map.of();
         this.c = this.Z;
         q8.v("AutoSell", "Validated {}-row menu, confirm slot={}, controls={}", var1.method_17388(), this.p + 1, this.N.size());
         return true;
      }
   }

   private void W(class_1707 var1) {
      int var2 = var1.method_17388() * 9;
      StringBuilder var3 = new StringBuilder();

      for (int var4 = 0; var4 < var2; var4++) {
         class_1799 var5 = var1.method_7611(var4).method_7677();
         if (!var5.method_7960()) {
            var3.append("\n  slot ")
               .append(var4 + 1)
               .append(var4 >= var2 - 9 ? " (bottom)" : "")
               .append(": ")
               .append(var5.method_7947())
               .append("x ")
               .append(n(var5))
               .append(" '")
               .append(var5.method_7964().getString())
               .append("' customName=")
               .append(var5.method_57826(class_9334.field_49631))
               .append(" itemName=")
               .append(var5.method_57826(class_9334.field_50239));

            for (String var7 : E(var5)) {
               var3.append("\n    | ").append(var7);
            }
         }
      }

      q8.L("AutoSell", "Rejected {}-row menu titled '{}'; nonempty slots:{}", var1.method_17388(), this.m.getValue(), var3.length() == 0 ? " none" : var3);
   }

   private void d(class_1707 var1) {
      if (var1 != null && mc.field_1761 != null) {
         if (this.X != null) {
            class_1799 var5 = var1.method_7611(this.g).method_7677();
            int var6 = var5.method_7960() ? 0 : (t(var5).equals(this.X.key()) ? var5.method_7947() : -1);
            int var4 = this.X.accepted(var1.method_37421(), var6, this.V(var1), this.l());
            if (var4 > 0) {
               this.P = ii.c(this.P, this.X.key(), var4);
               this.C++;
               this.X = null;
               if (!this.K(var1)) {
                  return;
               }

               this.A(uk.r.h, this.d(1));
            } else if (this.n.c() >= 100) {
               this.U("Item transfer was rejected, the menu is full, or the server did not respond. No repeat click was sent.");
            }
         } else if (this.n.X() && this.K(var1)) {
            for (int var2 = var1.method_17388() * 9; var2 < var1.field_7761.size(); var2++) {
               class_1735 var3 = var1.method_7611(var2);
               if (var3.field_7871 == mc.field_1724.method_31548() && this.v(var3.method_7677(), var3.method_34266()) && this.l(var1, var3.method_7677())) {
                  if (this.p() == var1 && mc.field_1724.field_7512 == var1 && var1.method_34255().method_7960() && this.Z == this.c && this.t(var1)) {
                     this.g = var2;
                     this.X = new ii.c(var1.method_37421(), t(var3.method_7677()), var3.method_7677().method_7947(), this.V(var1), this.l());
                     mc.field_1761.method_2906(var1.field_7763, var2, 0, class_1713.field_7794, mc.field_1724);
                     this.A(uk.r.h, 1);
                     return;
                  }

                  return;
               }
            }

            if (this.P.isEmpty()) {
               this.l();
               this.A(uk.r.s, this.u());
            } else {
               this.A(uk.r.X, this.d(1));
            }
         }
      }
   }

   private boolean K(class_1707 var1) {
      if (!this.V(var1).equals(this.P)) {
         this.U("Unexplained or returned items in the sell menu. Review them manually; no confirmation was sent.");
         return false;
      }

      for (int var2 = 0; var2 < var1.method_17388() * 9; var2++) {
         if (!this.N.containsKey(var2)) {
            class_1799 var3 = var1.method_7611(var2).method_7677();
            if (!var3.method_7960() && !this.v(var3, -1)) {
               this.U("Protected or excluded item in sell slot " + (var2 + 1) + ". No confirmation was sent.");
               return false;
            }
         }
      }

      return true;
   }

   private boolean l(class_1707 var1, class_1799 var2) {
      for (int var3 = 0; var3 < var1.method_17388() * 9; var3++) {
         if (!this.N.containsKey(var3)) {
            class_1735 var4 = var1.method_7611(var3);
            class_1799 var5 = var4.method_7677();
            if (ii.D(var5.method_7960(), t(var2).equals(t(var5)), var4.method_7680(var2), var5.method_7947(), var4.method_7676(var2))) {
               return true;
            }
         }
      }

      return false;
   }

   private void K(class_1707 var1) {
      if (var1 != null && mc.field_1761 != null && !this.k && !this.P.isEmpty()) {
         if (this.K(var1) && this.t(var1) && this.p() == var1 && var1.method_34255().method_7960() && mc.field_1724.field_7512 == var1 && this.Z == this.c) {
            this.G = var1.method_37421();
            this.U = this.l();
            this.k = (boolean)1;
            mc.field_1761.method_2906(var1.field_7763, this.p, 0, class_1713.field_7790, mc.field_1724);
            this.Q++;
            this.H("Sent one confirmation for the selected items.");
            this.A(uk.r.A, this.d(1));
         }
      }
   }

   private boolean t(class_1707 var1) {
      if (var1 != null && var1 == this.x && this.p >= 0) {
         int var2 = var1.method_17388() * 9;

         for (Entry var4 : this.N.entrySet()) {
            class_1799 var5 = var1.method_7611((Integer)var4.getKey()).method_7677();
            int var6 = var4.getKey() == this.p
               ? (v(var5) && var5.method_7947() == 1 && B(var5, this.p, var2) ? 1 : 0)
               : class_1799.method_7973((class_1799)var4.getValue(), var5);
            if (var6 == 0) {
               return false;
            }
         }

         return true;
      } else {
         return false;
      }
   }

   private static boolean v(class_1799 var0) {
      return var0.method_57826(class_9334.field_49631) || var0.method_57826(class_9334.field_50239);
   }

   private static List<String> E(class_1799 var0) {
      class_9290 var1 = (class_9290)var0.method_58694(class_9334.field_49632);
      if (var1 == null) {
         return List.of();
      }

      ArrayList var2 = new ArrayList();

      for (class_2561 var4 : var1.comp_2400()) {
         var2.add(var4.getString());
      }

      return var2;
   }

   private static boolean B(class_1799 var0, int var1, int var2) {
      return ii.j(n(var0), var0.method_7964().getString(), E(var0), var1, var2);
   }

   private boolean C() {
      for (int var1 = 0; var1 < 36; var1++) {
         if (this.v(mc.field_1724.method_31548().method_5438(var1), var1)) {
            return true;
         }
      }

      return false;
   }

   private boolean v(class_1799 var1, int var2) {
      return this.j().eligible(T(var1, var2));
   }

   public static i8.w T(class_1799 var0, int var1) {
      class_9288 var2 = (class_9288)var0.method_58694(class_9334.field_49622);
      class_9276 var3 = (class_9276)var0.method_58694(class_9334.field_49650);
      class_9304 var4 = (class_9304)var0.method_58694(class_9334.field_49643);
      int var5 = (var2 == null || !var2.method_59714().iterator().hasNext()) && (var3 == null || var3.method_57429()) ? 0 : 1;
      return new i8.w(
         n(var0),
         var0.method_7947(),
         var1,
         var0.method_57826(class_9334.field_49631),
         var0.method_7942() || var4 != null && !var4.method_57543(),
         (boolean)var5
      );
   }

   private Map<ii.w, Integer> V(class_1707 var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < var1.method_17388() * 9; var3++) {
         if (!this.N.containsKey(var3)) {
            e(var2, var1.method_7611(var3).method_7677());
         }
      }

      return Map.copyOf(var2);
   }

   private Map<ii.w, Integer> l() {
      HashMap var1 = new HashMap();

      for (int var2 = 0; var2 < 36; var2++) {
         e(var1, mc.field_1724.method_31548().method_5438(var2));
      }

      return Map.copyOf(var1);
   }

   private static void e(Map<ii.w, Integer> var0, class_1799 var1) {
      if (!var1.method_7960()) {
         var0.merge(t(var1), var1.method_7947(), Integer::sum);
      }
   }

   public static ii.w t(class_1799 var0) {
      class_1799 var1 = var0.method_7972();
      var1.method_57381(class_9334.field_49632);
      return new ii.w(n(var1), var1.method_57353());
   }

   private static String n(class_1799 var0) {
      return class_7923.field_41178.method_10221(var0.method_7909()).toString();
   }

   private class_1707 p() {
      return mc.field_1755 instanceof class_476 var1
            && mc.field_1724.field_7512 instanceof class_1707 var2
            && var1.method_17577() == var2
            && uk.J(var1.method_25440().getString(), this.m.getValue())
         ? var2
         : null;
   }

   public static boolean c(class_1707 var0) {
      return z != null && z.isEnabled() ? z != null && z.isRuntimeActive() && z.isInGame() && z.p() == var0 : false;
   }

   public static void K(int var0) {
      AutoSellModule var1 = z;
      if (var1 != null && var1.isRuntimeActive() && var1.x != null && var1.k && var1.x.field_7763 == var0) {
         var1.B = (boolean)1;
         var1.D = 0;
      }
   }

   private void A(uk.r var1, int var2) {
      this.n.I(var1, var2);
   }

   private int d(int var1) {
      return uk.j(var1, this.K.getValue(), ThreadLocalRandom.current());
   }

   private int u() {
      return uk.C(this.S.getValue().intValue(), this.u.getValue().intValue(), this.K.getValue(), ThreadLocalRandom.current());
   }

   private void l() {
      if (this.x != null && this.isInGame() && mc.field_1724 == this.A && mc.field_1687 == this.t && mc.field_1724.field_7512 == this.x) {
         mc.field_1724.method_7346();
      }

      this.x = null;
      this.N.clear();
      this.p = -1;
      this.o = this.k = (boolean)0;
   }

   private void U(String var1) {
      q8.L("AutoSell", "{} Stopped; enable again to retry.", var1);
      this.v(var1);
      this.disable();
   }

   private void H(String var1) {
      if (this.Y.getValue()) {
         this.v(var1);
      }
   }

   private void v(String var1) {
      if (this.isInGame()) {
         mc.method_44714().method_44736(class_2561.method_43470("[Auto Sell] " + var1), false);
      }
   }
}
