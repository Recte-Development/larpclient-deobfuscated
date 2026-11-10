package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.a6;
import com.larpclient.bf;
import com.larpclient.h6;
import com.larpclient.hh;
import com.larpclient.hk;
import com.larpclient.i6;
import com.larpclient.ig;
import com.larpclient.io;
import com.larpclient.p2;
import com.larpclient.pp;
import com.larpclient.q5;
import com.larpclient.qo;
import com.larpclient.sl;
import com.larpclient.v2;
import com.larpclient.vf;
import com.larpclient.vh;
import com.larpclient.vy;
import com.larpclient.vz;
import com.larpclient.wj;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public final class t {
   private static final String[] N = new String[]{"Player", "Hand 2", "Hand 3", "Dealer"};

   private t() {
   }

   private static String R(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1).toLowerCase(Locale.ROOT);
   }

   private static int j(String var0) {
      return switch (var0) {
         case "Hand 2" -> 1;
         case "Hand 3" -> 2;
         case "Dealer" -> -1;
         default -> 0;
      };
   }

   private static String N(int var0) {
      return var0 < 0 ? "Dealer" : (var0 == 1 ? "Hand 2" : (var0 == 2 ? "Hand 3" : "Player"));
   }

   private static void Z(List<String[]> var0, int var1) {
      io var2 = io.z();

      for (int var3 = 0; var3 < var1; var3++) {
         var0.add(new String[]{var1 == 1 ? "Player" : "Hand " + (var3 + 1), var2.k(var3)});
      }

      var0.add(new String[]{"Dealer", var2.k(-1)});
      int var4 = vy.w().countTemplates();
      var0.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
   }

   public static final class a extends kj {
      private final com.larpclient.module.b D = this.addSetting(new com.larpclient.module.b("Hands", "Number of player hands at the table", 1.0, 1.0, 3.0, 1.0));
      private final com.larpclient.module.o x = this.addSetting(
         new com.larpclient.module.o(
            "Side",
            "Who takes the next cards. Player hands are counted; dealer draws are prepared to beat the best hand. Starts each round on the first hand",
            "Player",
            t.N
         ) {
            @Override
            public List<String> getVisibleModes() {
               int var1 = a.this.K();
               return Arrays.stream(t.N).filter(var1x -> var1x.equals("Dealer") || t.j(var1x) < var1).toList();
            }

            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Player") && a.this.K() > 1 ? "Hand 1" : var1;
            }
         }
      );

      public a() {
         super("Blackjack", "Counts every hand and prepares the dealer's draws to beat the best player hand", a6.d.d);
         this.W("Side Bind", "Move to the next hand, then the dealer", this::U);
         this.u("Dispenser Bind", hk.k.x);
         this.x("Select table", hk.k.x);
         this.o();
         this.z();
         this.T();
         this.X();
         this.x.w(var1 -> this.applySettings());
         this.D.w(var1 -> {
            if (t.j(this.x.getValue()) >= this.K()) {
               this.x.setValue("Player");
            }

            this.applySettings();
         });
      }

      private int K() {
         return (int)Math.round(this.D.getValue());
      }

      private void U() {
         this.x.C();
         i("Blackjack: " + this.x.getModeLabel(this.x.getValue()) + " draws next");
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            io var1 = io.z();
            var1.t(this.K());
            var1.M(t.j(this.x.getValue()));
         }
      }

      @Override
      protected void afterTick(Minecraft var1) {
         String var2 = t.N(io.z().P());
         if (!this.x.w(var2) && this.x.acceptsValue(var2)) {
            this.x.setValue(var2);
         }
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         t.Z(var1, this.K());
         var1.add(new String[]{"Dealer plan", io.z().A()});
      }
   }

   public static final class b extends kj {
      private final com.larpclient.module.o P = this.addSetting(
         new com.larpclient.module.o(
            "Target", "Which hand the automatic plan makes win. Off leaves the draws alone unless you queue a preset", "Player", "Player", "Banker", "Off"
         ) {
            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Off") ? "Off (presets only)" : var1;
            }
         }
      );
      private final e[] o = new e[5];
      private final com.larpclient.module.x[] O = new com.larpclient.module.x[5];

      public b() {
         super("Baccarat", "Counts the hands and queues card values for the next draws", a6.d.B);
         this.W("Side Bind", "Switch between player and banker", this::B);
         this.u("Player Dispenser Bind", hk.k.A);
         this.x("Select player table", hk.k.A);
         this.u("Banker Dispenser Bind", hk.k.I);
         this.x("Select banker table", hk.k.I);
         this.o();

         for (int var1 = 0; var1 < 5; var1++) {
            int var2 = var1;
            this.o[var1] = this.addSetting(new e("Preset " + (var1 + 1), "Up to three card values 1 to 9 separated by spaces, queued by the preset bind", ""));
            this.o[var1].w(var1x -> this.applySettings());
            this.O[var1] = this.W("Preset " + (var1 + 1) + " Bind", "Queue the values in Preset " + (var1 + 1), () -> this.O(var2));
         }

         this.z();
         this.T();
         this.X();
         this.P.w(var1x -> this.applySettings());
      }

      private static int[] o(String var0) {
         int[] var1 = new int[3];
         int var2 = 0;
         if (var0 != null) {
            for (String var6 : var0.trim().split("[\\s,]+")) {
               if (var2 == var1.length) {
                  break;
               }

               if (var6.matches("[1-9]")) {
                  var1[var2++] = Integer.parseInt(var6);
               }
            }
         }

         return Arrays.copyOf(var1, var2);
      }

      private void O(int var1) {
         int[] var2 = o(this.o[var1].getValue());
         if (var2.length == 0) {
            i("Baccarat preset " + (var1 + 1) + " is empty");
         } else if (vf.x().isSwapBusy()) {
            i("Baccarat preset " + (var1 + 1) + " not queued: a card is being swapped, press it again after this draw");
         } else {
            if (this.a().V(var1, var2)) {
               i("Baccarat preset " + (var1 + 1) + " queued for the next draws: " + x(var2));
            } else {
               i("Baccarat preset " + (var1 + 1) + " not queued: open a Baccarat table once so the cards are learned");
            }
         }
      }

      private static String x(int[] var0) {
         StringBuilder var1 = new StringBuilder();

         for (int var5 : var0) {
            var1.append(var1.isEmpty() ? "" : " ").append(var5);
         }

         return var1.toString();
      }

      private static boolean f(String var0) {
         if (var0 != null && !var0.isBlank()) {
            String[] var1 = var0.trim().split("[\\s,]+");
            if (var1.length > 3) {
               return false;
            }

            for (String var5 : var1) {
               if (!var5.matches("[1-9]")) {
                  return false;
               }
            }

            return true;
         } else {
            return true;
         }
      }

      @Override
      public String setupSummary() {
         StringBuilder var1 = new StringBuilder(super.setupSummary());

         for (int var2 = 0; var2 < 5; var2++) {
            String var3 = this.o[var2].getValue();
            if (var3 != null && !var3.isBlank()) {
               int[] var4 = o(var3);
               var1.append("\nPreset ")
                  .append(var2 + 1)
                  .append(": ")
                  .append(var4.length == 0 ? "no valid values" : x(var4))
                  .append(f(var3) ? "" : " (only values 1 to 9, at most three, are used)");
            }
         }

         return var1.toString();
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         h6 var2 = h6.l();
         int var3 = var2.x() > 0 ? 1 : 0;
         var1.add(new String[]{"Player", var3 != 0 ? Integer.toString(var2.Z()) : "-"});
         var1.add(new String[]{"Banker", var3 != 0 ? Integer.toString(var2.k()) : "-"});
         String var4 = var2.i();
         var1.add(var4.isEmpty() ? new String[]{"Next card", vf.x().pendingLabel()} : new String[]{"Preset queue", var4});
         int var5 = vf.x().countTemplates();
         var1.add(new String[]{"Cards learned", var5 == 0 ? "None, open a table once" : Integer.toString(var5)});
      }

      private void B() {
         this.P.setValue(this.P.w("Player") ? "Banker" : "Player");
         i("Baccarat: " + this.P.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            h6.l().Y(this.P.getValue().toLowerCase(Locale.ROOT), vf.x());
            int[] var1 = new int[15];
            Arrays.fill(var1, -1);

            for (int var2 = 0; var2 < 5; var2++) {
               int[] var3 = o(this.o[var2].getValue());
               System.arraycopy(var3, 0, var1, var2 * 3, var3.length);
            }

            this.a().t(var1);
         }
      }
   }

   public static final class d extends t.j {
      public d() {
         super("Armor Game", "Shows the chosen armor piece as the winner", a6.d.F, sl.s, 4, hk.k.V);
      }
   }

   public static final class g extends kj {
      @Override
      public String setupSummary() {
         int var1 = hh.R().t();
         return var1 < 0 ? "No lure 3 rod in your inventory" : "Rig rod in inventory slot " + (var1 + 1);
      }

      public g() {
         super("Fishing Game", "Silently casts a lure 3 rod from your inventory when you use any rod", a6.d.s);
         this.W("Hide Rod Bind", "Hide or show the rig rod in your inventory display", () -> {
            hh.R().b();
            i("Fishing Game: rod " + (hh.R().x ? "hidden" : "visible"));
         });
         this.q("Hide or show rig rod", () -> {
            hh.R().b();
            i("Fishing Game: rod " + (hh.R().x ? "hidden" : "visible"));
         }, () -> hh.R().x ? "Rig rod hidden: press to show" : "Hide rig rod");
         this.i("Open game log", com.larpclient.module.j::m);
         this.z();
         this.T();
      }

      @Override
      public void onEnable() {
         super.onEnable();
         hh.R().U(true);
      }

      @Override
      protected void releaseExtra() {
         hh.R().U(false);
      }

      @Override
      protected void applySettings() {
      }

      @Override
      public String getStatus() {
         return hh.R().s ? "Rig on" : "Rig paused";
      }

      @Override
      public String setupHint() {
         return "Keep a lure 3 rod anywhere in your inventory, then cast with any rod.";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         hh var2 = hh.R();
         int var3 = var2.t();
         var1.add(new String[]{"Rig", var2.s ? "On" : "Paused"});
         var1.add(new String[]{"Lure 3 rod", var3 < 0 ? "Not found" : "Slot " + (var3 + 1)});
         var1.add(new String[]{"Rod display", var2.x ? "Hidden" : "Visible"});
      }
   }

   public static final class h extends kj {
      @Override
      public String setupSummary() {
         pp var1 = pp.A();
         return super.setupSummary() + "\nNormal: " + u(var1.n) + ", trigger: " + u(var1.E) + (var1.g ? " (armed)" : "");
      }

      private static void V() {
         pp var0 = pp.A();
         if (var0.g) {
            var0.g = (boolean)0;
         } else {
            var0.z();
         }

         i("Russian Roulette: " + (var0.g ? "armed, the next pull shows the trigger item" : "disarmed"));
      }

      @Override
      protected String targetName() {
         return "Next pull";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         pp var2 = pp.A();
         var1.add(new String[]{"Normal item", u(var2.n)});
         var1.add(new String[]{"Trigger item", u(var2.E)});
      }

      public h() {
         super("Russian Roulette", "Swaps the normal item for the trigger item when armed, visually only", a6.d.f);
         this.W("Arm Bind", "Arm or disarm the trigger item", t.h::V);
         this.W("From Hand Bind", "Store your hand item as the trigger item when armed, else the normal item", () -> i(this.a().b(a6.d.f).i));
         this.u("Dispenser Bind", hk.k.s);
         this.x("Select table", hk.k.s);
         this.o();
         this.z();
         this.T();
         this.M("Select normal item", "normal item", var0 -> true, "Pick an item", var1 -> {
            pp.A().n = var1.getItem();
            this.a().A();
            return "Russian Roulette normal item: " + var1.getHoverName().getString();
         }, () -> u(pp.A().n));
         this.M("Select trigger item", "trigger item", var0 -> true, "Pick an item", var1 -> {
            pp.A().E = var1.getItem();
            this.a().A();
            return "Russian Roulette trigger item: " + var1.getHoverName().getString();
         }, () -> u(pp.A().E));
         this.q("Arm or disarm", () -> {
            if (this.isEnabled()) {
               V();
            } else {
               i("Enable Russian Roulette first");
            }
         }, () -> pp.A().g ? "Armed: press to disarm" : "Arm trigger");
         this.X();
      }

      @Override
      protected void applySettings() {
      }
   }

   public static final class i extends t.j {
      public i() {
         super("Horse Racing", "Shows the chosen horse armor as the winner", a6.d.H, sl.I, 3, hk.k.k);
      }
   }

   abstract static class j extends kj {
      private final sl h;
      private final int p;
      private final com.larpclient.module.o F;

      j(String var1, String var2, a6.d var3, sl var4, int var5, hk.k var6) {
         super(var1, var2, var3);
         this.h = var4;
         this.p = var5;
         String[] var7 = new String[var5];

         for (int var8 = 0; var8 < var5; var8++) {
            var7[var8] = Integer.toString(var8 + 1);
         }

         this.F = this.addSetting(new com.larpclient.module.o("Winner", "Which piece is shown as the winner", "1", var7) {
            @Override
            public String getModeLabel(String var1) {
               return j.this.w(Integer.parseInt(var1));
            }
         });
         this.W("Side Bind", "Advance to the next winner", this::J);
         this.u("Dispenser Bind", var6);
         this.x("Select table", var6);
         this.o();
         this.z();
         this.T();
         this.X();
         this.F.w(var1x -> this.applySettings());
      }

      private String w(int var1) {
         String var2 = this.h == sl.I ? (Minecraft.getInstance() != null && Minecraft.getInstance().level != null ? this.h.z(var1) : "") : this.h.s(var1);
         return var2 != null && !var2.isBlank() ? var2 : (this.h == sl.I ? "Horse " + var1 : Integer.toString(var1));
      }

      private void J() {
         this.F.setValue(Integer.toString(Integer.parseInt(this.F.getValue()) % this.p + 1));
         i(this.getName() + " winner: " + this.F.getModeLabel(this.F.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            this.h.S(Integer.parseInt(this.F.getValue()));
            this.h.s(true);
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Table", this.h.C()});
      }
   }

   public static final class k extends kj {
      private final com.larpclient.module.o c = this.addSetting(
         new com.larpclient.module.o("Winner", "Which of the two items is shown as the winner", "1", "1", "2") {
            @Override
            public String getModeLabel(String var1) {
               return "1".equals(var1) ? "First item" : "Second item";
            }
         }
      );

      @Override
      public String getDisplayName() {
         return "50/50";
      }

      @Override
      public List<String> getSearchAliases() {
         return List.of("50/50", "fifty fifty");
      }

      public k() {
         super("50-50", "Shows one of the two 50/50 items as the winner and keeps the real pull intact", a6.d.K);
         this.g("Side Bind", "Switch the winning item", this::f, true);
         this.u("Dispenser Bind", hk.k.B);
         this.x("Select table", hk.k.B);
         this.o();
         this.z();
         this.T();
         this.X();
         this.c.w(var1 -> this.applySettings());
      }

      private void f() {
         this.c.setValue(this.c.w("1") ? "2" : "1");
         i("50/50 winner: " + this.c.getModeLabel(this.c.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            wj.m().P(Integer.parseInt(this.c.getValue()));
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Table screen", wj.m().M() ? "Open" : "Closed"});
      }

      @Override
      protected boolean screenAllowsBinds(Minecraft var1) {
         return wj.m().M();
      }
   }

   public static final class m extends kj {
      private final com.larpclient.module.b d = this.addSetting(new com.larpclient.module.b("Hands", "Number of player hands at the table", 1.0, 1.0, 3.0, 1.0));
      private final com.larpclient.module.o r = this.addSetting(
         new com.larpclient.module.o(
            "Draw For",
            "Auto prepares the player's safe draws at 12 to 16, then the dealer's losing draw once the player stands at 17. Player or Dealer keeps one side",
            "Auto",
            "Auto",
            "Player",
            "Dealer"
         )
      );

      public m() {
         super("Blackjack Force Only", "Prepares the dispenser for the draw you want; the server decides what drops", a6.d.a);
         this.g("Side Bind", "Cycle Auto, Player and Dealer", this::K, true);
         this.W("Import Hotbar Bind", "Read the card map templates from your hotbar", this::a);
         this.u("Dispenser Bind", hk.k.q);
         this.x("Select table", hk.k.q);
         this.o();
         this.z();
         this.T();
         this.q("Import hotbar cards", this::a, () -> q5.a().G() ? "Hotbar cards imported" : "Import hotbar cards");
         this.X();
         this.r.w(var1 -> this.applySettings());
         this.d.w(var1 -> this.applySettings());
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         t.Z(var1, (int)Math.round(this.d.getValue()));
         var1.add(new String[]{"Card values", q5.a().G() ? "From hotbar" : "From table slots"});
      }

      private void a() {
         String var1 = q5.a().r(Minecraft.getInstance());
         i(var1 == null ? "Blackjack Force Only: hotbar card maps imported" : "Hotbar import failed: " + var1);
         if (var1 == null) {
            this.a().A();
         }
      }

      private void K() {
         this.r.C();
         i("Blackjack Force Only draws for: " + this.r.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            io var1 = io.z();
            var1.t((int)Math.round(this.d.getValue()));
            var1.w(this.r.getValue());
         }
      }

      @Override
      public void onDisable() {
         super.onDisable();
         io.z().w("auto");
      }

      @Override
      protected boolean screenAllowsBinds(Minecraft var1) {
         return q5.a().f(var1);
      }
   }

   public static final class n extends kj {
      private final com.larpclient.module.o R = this.addSetting(
         new com.larpclient.module.o("Winner", "Which item is shown as the winning one", "First", "First", "Second", "Middle")
      );
      private final e U = this.addSetting(
         new e("Slot Targets", "Nine digits, one per dispenser slot from top left: 1 first item, 2 second item, 3 middle item", "112132122").L("112132122")
      );

      @Override
      public String setupSummary() {
         p2 var1 = p2.p();
         String var2 = this.U.getValue() == null ? "" : this.U.getValue().trim();
         String var3;
         if (e(var2)) {
            var3 = "Slot targets: " + var2.substring(0, 3) + " / " + var2.substring(3, 6) + " / " + var2.substring(6);
         } else {
            int[] var4 = var1.e();
            StringBuilder var5 = new StringBuilder();

            for (int var6 = 0; var6 < 9; var6++) {
               var5.append(var6 > 0 && var6 % 3 == 0 ? " / " : "").append(var4[var6]);
            }

            var3 = "Slot Targets needs exactly 9 digits, each 1, 2 or 3. Still using " + var5;
         }

         return super.setupSummary() + "\nFirst: " + u(var1.S) + ", second: " + u(var1.O) + ", middle: " + u(var1.G) + "\n" + var3;
      }

      @Override
      public String getDisplayName() {
         return "45/45/10";
      }

      @Override
      public List<String> getSearchAliases() {
         return List.of("45/45/10", "forty five");
      }

      private static boolean e(String var0) {
         return var0.matches("[123]{9}");
      }

      public n() {
         super("45-45-10", "Shows a chosen item of the 45/45/10 table as the winner, visually only", a6.d.k);
         this.W("Side Bind", "Cycle first, second and middle winner", this::k);
         this.W("From Hand Bind", "Store the item in your hand as the item of the current Winner", () -> this.P());
         this.u("Dispenser Bind", hk.k.j);
         this.x("Select table", hk.k.j);
         this.o();
         this.z();
         this.T();
         this.M("Select first item", "first (45%) item", var0 -> true, "Pick an item", var1 -> {
            p2.p().S = var1.getItem();
            this.a().A();
            return "45/45/10 first item: " + var1.getHoverName().getString();
         }, () -> u(p2.p().S));
         this.M("Select second item", "second (45%) item", var0 -> true, "Pick an item", var1 -> {
            p2.p().O = var1.getItem();
            this.a().A();
            return "45/45/10 second item: " + var1.getHoverName().getString();
         }, () -> u(p2.p().O));
         this.M("Select middle item", "middle (10%) item", var0 -> true, "Pick an item", var1 -> {
            p2.p().G = var1.getItem();
            this.a().A();
            return "45/45/10 middle item: " + var1.getHoverName().getString();
         }, () -> u(p2.p().G));
         this.X();
         this.R.w(var1 -> this.applySettings());
         this.U.w(var1 -> this.applySettings());
      }

      private void P() {
         a6.g var1 = this.a().b(a6.d.k);
         i(var1.i);
      }

      private void k() {
         this.R.setValue(this.R.w("First") ? "Second" : (this.R.w("Second") ? "Middle" : "First"));
         i("45/45/10 winner: " + this.R.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            p2 var1 = p2.p();

            var1.z = switch ((String)this.R.getValue()) {
               case "Second" -> p2.v.F;
               case "Middle" -> p2.v.b;
               default -> p2.v.g;
            };
            String var5 = this.U.getValue() == null ? "" : this.U.getValue().trim();
            if (e(var5)) {
               int[] var6 = new int[9];

               for (int var4 = 0; var4 < 9; var4++) {
                  var6[var4] = var5.charAt(var4) - '0';
               }

               var1.U(var6);
            }

            this.a().A();
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         p2 var2 = p2.p();

         Item var3 = switch (var2.z) {
            case g -> var2.S;
            case F -> var2.O;
            case b -> var2.G;
         };
         var1.add(new String[]{"Winner item", u(var3)});
         if (!e(this.U.getValue() == null ? "" : this.U.getValue().trim())) {
            var1.add(new String[]{"Slot Targets", "Invalid, see settings"});
         }
      }
   }

   public static final class p extends kj {
      private final com.larpclient.module.o g = this.addSetting(
         new com.larpclient.module.o(
            "Target",
            "Who takes the next cards after the first two. Player draws are counted; dealer draws are prepared to reach at least the player's total without going over 9",
            "Player",
            "Player",
            "Dealer"
         ) {
            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Player") ? "Player (count)" : "Dealer (prepared)";
            }
         }
      );

      public p() {
         super("Mexican Jack", "Counts both hands and prepares the dealer's draws", a6.d.j);
         this.g.B("Drawing");
         this.W("Side Bind", "Switch between player and dealer drawing", this::A);
         this.u("Dispenser Bind", hk.k.Y);
         this.x("Select table", hk.k.Y);
         this.o();
         this.z();
         this.T();
         this.X();
         this.g.w(var1 -> this.applySettings());
      }

      private void A() {
         this.g.setValue(this.g.w("Player") ? "Dealer" : "Player");
         i("Mexican Jack: " + this.g.getModeLabel(this.g.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            i6.k().I(this.g.getValue().toLowerCase(Locale.ROOT), v2.L());
         }
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         i6 var2 = i6.k();
         var1.add(new String[]{"Player", var2.d()});
         var1.add(new String[]{"Dealer", var2.f()});
         int var3 = var2.f();
         var1.add(new String[]{"Next card", var3 < 1 ? "none" : Integer.toString(var3)});
         int var4 = v2.L().countTemplates();
         var1.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
      }
   }

   public static final class s extends kj {
      private final com.larpclient.module.o p = this.addSetting(
         new com.larpclient.module.o("Target", "Whether the next card is made to read higher or lower", "Higher", "Higher", "Lower")
      );

      public s() {
         super("Higher or Lower", "Prepares the next card to read higher or lower than the reference", a6.d.Y);
         this.W("Side Bind", "Switch between higher and lower", this::R);
         this.u("Dispenser Bind", hk.k.b);
         this.x("Select table", hk.k.b);
         this.o();
         this.z();
         this.T();
         this.X();
         this.p.w(var1 -> this.applySettings());
      }

      private void R() {
         this.p.setValue(this.p.w("Higher") ? "Lower" : "Higher");
         i("Higher or Lower: " + this.p.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            vh.R().x(this.p.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         vh var2 = vh.R();
         int var3 = var2.E();
         var1.add(new String[]{"Shown card", var3 < 1 ? "-" : Integer.toString(var3)});
         var1.add(new String[]{"Next card", var2.pendingLabel()});
         int var4 = var2.countTemplates();
         var1.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
      }
   }

   public static final class u extends kj {
      private final com.larpclient.module.o l = this.addSetting(
         new com.larpclient.module.o("Target", "Whether the shot is shown as a win or a loss", "Win", "Win", "Lose")
      );

      @Override
      public String setupSummary() {
         return super.setupSummary() + "\nX map: " + (ig.r().x() ? "selected" : "none");
      }

      public u() {
         super("Arrow Game", "Shows the arrow dispenser and item frames for a win or a loss, visually only", a6.d.N);
         this.W("Side Bind", "Switch between win and lose", this::G);
         this.W("Note Block Bind", "Look at a note block and press to bind it as the fake press trigger", this::n);
         this.W("X Map Bind", "Hold the filled map used as the X mark and press to store it", () -> {
            ig.r().I();
            i("Arrow Game: X map stored");
         });
         this.W("Clear X Map Bind", "Forget the stored X map", () -> {
            ig.r().W();
            i("Arrow Game: X map cleared");
         });
         this.u("Dispenser Bind", hk.k.N);
         this.x("Select table", hk.k.N);
         this.o();
         this.z();
         this.T();
         this.M(
            "Select X map",
            "filled map used as the X mark",
            var0 -> var0.is(Items.FILLED_MAP) && var0.get(DataComponents.MAP_ID) != null,
            "Pick a filled map",
            var0 -> {
               int var1 = ((MapId)var0.get(DataComponents.MAP_ID)).id();
               bf.v().J(var1);
               return "Arrow Game X map: map #" + var1;
            },
            () -> ig.r().x() ? "" : null
         );
         this.q(
            "Select note block",
            () -> fb.f(
               "Right-click the note block that fires the shot (Esc cancels)",
               var0 -> var0.getBlock() instanceof NoteBlock,
               "That is not a note block",
               var0 -> {
                  ig.r().o(Minecraft.getInstance(), var0);
                  return "Arrow Game note block selected";
               }
            ),
            () -> {
               int var0 = bf.v().b();
               return var0 == 0 ? "Select note block" : (var0 == 1 ? "Note block selected" : var0 + " note blocks selected");
            }
         );
         this.q("Clear X map", () -> {
            ig.r().W();
            i("Arrow Game: X map cleared");
         }, () -> ig.r().x() ? "Clear X map" : "No X map to clear");
         this.X();
         this.l.w(var1 -> this.applySettings());
      }

      private void n() {
         Minecraft var1 = Minecraft.getInstance();
         if (var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var2.getType() == Type.BLOCK
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof NoteBlock) {
            ig.r().o(var1, var2.getBlockPos());
            i("Arrow Game: note block bound");
         } else {
            i("Look at a note block first");
         }
      }

      private void G() {
         this.l.setValue(this.l.w("Win") ? "Lose" : "Win");
         i("Arrow Game: " + this.l.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            ig.r().d(this.l.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         int var2 = bf.v().b();
         var1.add(new String[]{"Note block", var2 == 0 ? "Not selected" : (var2 == 1 ? "Selected" : var2 + " selected")});
         var1.add(new String[]{"X map", ig.r().x() ? "Selected" : "Not selected"});
      }
   }

   public static final class v extends kj {
      private final com.larpclient.module.o c = this.addSetting(
         new com.larpclient.module.o("Parity", "Which parity is shown as the result", "Odd", "Odd", "Even")
      );

      public v() {
         super("Even or Odd", "Swaps the dealt map card for one of the chosen parity", a6.d.g);
         this.W("Side Bind", "Switch between odd and even", this::J);
         this.u("Dispenser Bind", hk.k.i);
         this.x("Select table", hk.k.i);
         this.o();
         this.z();
         this.T();
         this.X();
         this.c.w(var1 -> this.applySettings());
      }

      private void J() {
         this.c.setValue(this.c.w("Odd") ? "Even" : "Odd");
         i("Even or Odd: " + this.c.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            vz var1 = vz.E();
            var1.d(this.c.w("Even") ? 1 : 0);
            var1.clearQueue();
            this.a().A();
         }
      }

      @Override
      protected String targetName() {
         return "Result";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         vz var2 = vz.E();
         var1.add(new String[]{"Next card", var2.pendingLabel()});
         int var3 = var2.countTemplates();
         var1.add(new String[]{"Cards learned", var3 == 0 ? "None, open the table once" : Integer.toString(var3)});
      }
   }

   public static final class x extends kj {
      private final com.larpclient.module.o V = this.addSetting(
         new com.larpclient.module.o("Target", "Which table's drawn paper is shown as the higher one", "Host", "Host", "Viewer")
      );
      private final com.larpclient.module.m k = this.addSetting(
         new com.larpclient.module.m("Universal Items", "Learn one item set and use it for both tables", true)
      );
      private final e D = this.addSetting(new e("Host Suffix", "Name suffix that marks host table items", "Host"));
      private final e i = this.addSetting(new e("Viewer Suffix", "Name suffix that marks viewer table items", "Viewer"));

      public x() {
         super("Paper", "Shows the chosen table's paper draw as the higher one, visually only", a6.d.L);
         this.W("Side Bind", "Switch the target between host and viewer", this::C);
         this.u("Host Dispenser Bind", hk.k.y);
         this.x("Select host table", hk.k.y);
         this.u("Viewer Dispenser Bind", hk.k.w);
         this.x("Select viewer table", hk.k.w);
         this.o();
         this.z();
         this.T();
         this.X();
         this.V.w(var1 -> this.applySettings());
         this.k.w(var1 -> {
            this.applySettings();
            if (this.isEnabled()) {
               _q.z("Rig", "Paper: Universal Items {}", var1 ? "on" : "off");
            }
         });
         this.D.w(var1 -> this.applySettings());
         this.i.w(var1 -> this.applySettings());
      }

      private void C() {
         this.V.setValue(this.V.w("Host") ? "Viewer" : "Host");
         i("Paper: " + this.V.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            qo var1 = qo.Z();
            if (var1.Q(this.k.getValue())) {
               this.a().A();
            }

            var1.M(this.D.getValue());
            var1.X(this.i.getValue());
            var1.X(this.V.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Items", this.k.getValue() ? "Same for both tables" : "Per table"});
      }
   }
}
