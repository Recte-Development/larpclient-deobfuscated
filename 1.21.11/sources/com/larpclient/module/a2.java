package com.larpclient.module;

import com.larpclient.bk;
import com.larpclient.f4;
import com.larpclient.hu;
import com.larpclient.ju;
import com.larpclient.lk;
import com.larpclient.q7;
import com.larpclient.q8;
import com.larpclient.q_;
import com.larpclient.qc;
import com.larpclient.ql;
import com.larpclient.qq;
import com.larpclient.qr;
import com.larpclient.t3;
import com.larpclient.tw;
import com.larpclient.ux;
import com.larpclient.yb;
import com.larpclient.yh;
import com.larpclient.yu;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2428;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_9209;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;

public final class a2 {
   private static final String[] n = new String[]{"Player", "Hand 2", "Hand 3", "Dealer"};

   private a2() {
   }

   private static String R(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1).toLowerCase(Locale.ROOT);
   }

   private static int p(String var0) {
      return switch (var0) {
         case "Hand 2" -> 1;
         case "Hand 3" -> 2;
         case "Dealer" -> -1;
         default -> 0;
      };
   }

   private static String g(int var0) {
      return var0 < 0 ? "Dealer" : (var0 == 1 ? "Hand 2" : (var0 == 2 ? "Hand 3" : "Player"));
   }

   private static void B(List<String[]> var0, int var1) {
      com.larpclient.l4 var2 = com.larpclient.l4.K();

      for (int var3 = 0; var3 < var1; var3++) {
         var0.add(new String[]{var1 == 1 ? "Player" : "Hand " + (var3 + 1), var2.a(var3)});
      }

      var0.add(new String[]{"Dealer", var2.a(-1)});
      int var4 = q7.u().countTemplates();
      var0.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
   }

   public static final class b extends r {
      private final li N = this.addSetting(new li("Winner", "Which of the two items is shown as the winner", "1", "1", "2") {
         @Override
         public String getModeLabel(String var1) {
            return "1".equals(var1) ? "First item" : "Second item";
         }
      });

      @Override
      public String getDisplayName() {
         return "50/50";
      }

      @Override
      public List<String> getSearchAliases() {
         return List.of("50/50", "fifty fifty");
      }

      public b() {
         super("50-50", "Shows one of the two 50/50 items as the winner and keeps the real pull intact", com.larpclient.q.n.O);
         this.O("Side Bind", "Switch the winning item", this::Q, true);
         this.t("Dispenser Bind", hu.q.A);
         this.e("Select table", hu.q.A);
         this.j();
         this.x();
         this.D();
         this.q();
         this.N.a(var1 -> this.applySettings());
      }

      private void Q() {
         this.N.setValue(this.N.G("1") ? "2" : "1");
         m("50/50 winner: " + this.N.getModeLabel(this.N.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            ju.b().k(Integer.parseInt(this.N.getValue()));
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Table screen", ju.b().a() ? "Open" : "Closed"});
      }

      @Override
      protected boolean screenAllowsBinds(class_310 var1) {
         return ju.b().a();
      }
   }

   public static final class f extends r {
      private final li C = this.addSetting(new li("Target", "Whether the next card is made to read higher or lower", "Higher", "Higher", "Lower"));

      public f() {
         super("Higher or Lower", "Prepares the next card to read higher or lower than the reference", com.larpclient.q.n.g);
         this.o("Side Bind", "Switch between higher and lower", this::d);
         this.t("Dispenser Bind", hu.q.R);
         this.e("Select table", hu.q.R);
         this.j();
         this.x();
         this.D();
         this.q();
         this.C.a(var1 -> this.applySettings());
      }

      private void d() {
         this.C.setValue(this.C.G("Higher") ? "Lower" : "Higher");
         m("Higher or Lower: " + this.C.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            qr.h().k(this.C.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         qr var2 = qr.h();
         int var3 = var2.C();
         var1.add(new String[]{"Shown card", var3 < 1 ? "-" : Integer.toString(var3)});
         var1.add(new String[]{"Next card", var2.pendingLabel()});
         int var4 = var2.countTemplates();
         var1.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
      }
   }

   public static final class g extends r {
      private final li x = this.addSetting(new li("Target", "Whether the shot is shown as a win or a loss", "Win", "Win", "Lose"));

      @Override
      public String setupSummary() {
         return super.setupSummary() + "\nX map: " + (yu.o().H() ? "selected" : "none");
      }

      public g() {
         super("Arrow Game", "Shows the arrow dispenser and item frames for a win or a loss, visually only", com.larpclient.q.n.X);
         this.o("Side Bind", "Switch between win and lose", this::Z);
         this.o("Note Block Bind", "Look at a note block and press to bind it as the fake press trigger", this::j);
         this.o("X Map Bind", "Hold the filled map used as the X mark and press to store it", () -> {
            yu.o().M();
            m("Arrow Game: X map stored");
         });
         this.o("Clear X Map Bind", "Forget the stored X map", () -> {
            yu.o().r();
            m("Arrow Game: X map cleared");
         });
         this.t("Dispenser Bind", hu.q.Y);
         this.e("Select table", hu.q.Y);
         this.j();
         this.x();
         this.D();
         this.b(
            "Select X map",
            "filled map used as the X mark",
            var0 -> var0.method_31574(class_1802.field_8204) && var0.method_58694(class_9334.field_49646) != null,
            "Pick a filled map",
            var0 -> {
               int var1 = ((class_9209)var0.method_58694(class_9334.field_49646)).comp_2315();
               yh.U().W(var1);
               return "Arrow Game X map: map #" + var1;
            },
            () -> yu.o().H() ? "" : null
         );
         this.B(
            "Select note block",
            () -> at.Q(
               "Right-click the note block that fires the shot (Esc cancels)",
               var0 -> var0.method_26204() instanceof class_2428,
               "That is not a note block",
               var0 -> {
                  yu.o().f(class_310.method_1551(), var0);
                  return "Arrow Game note block selected";
               }
            ),
            () -> {
               int var0 = yh.U().g();
               return var0 == 0 ? "Select note block" : (var0 == 1 ? "Note block selected" : var0 + " note blocks selected");
            }
         );
         this.B("Clear X map", () -> {
            yu.o().r();
            m("Arrow Game: X map cleared");
         }, () -> yu.o().H() ? "Clear X map" : "No X map to clear");
         this.q();
         this.x.a(var1 -> this.applySettings());
      }

      private void j() {
         class_310 var1 = class_310.method_1551();
         if (var1.field_1687 != null
            && var1.field_1765 instanceof class_3965 var2
            && var2.method_17783() == class_240.field_1332
            && var1.field_1687.method_8320(var2.method_17777()).method_26204() instanceof class_2428) {
            yu.o().f(var1, var2.method_17777());
            m("Arrow Game: note block bound");
         } else {
            m("Look at a note block first");
         }
      }

      private void Z() {
         this.x.setValue(this.x.G("Win") ? "Lose" : "Win");
         m("Arrow Game: " + this.x.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            yu.o().q(this.x.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         int var2 = yh.U().g();
         var1.add(new String[]{"Note block", var2 == 0 ? "Not selected" : (var2 == 1 ? "Selected" : var2 + " selected")});
         var1.add(new String[]{"X map", yu.o().H() ? "Selected" : "Not selected"});
      }
   }

   public static final class i extends r {
      @Override
      public String setupSummary() {
         yb var1 = yb.b();
         return super.setupSummary() + "\nNormal: " + a(var1.Y) + ", trigger: " + a(var1.P) + (var1.N ? " (armed)" : "");
      }

      private static void H() {
         yb var0 = yb.b();
         if (var0.N) {
            var0.N = (boolean)0;
         } else {
            var0.Z();
         }

         m("Russian Roulette: " + (var0.N ? "armed, the next pull shows the trigger item" : "disarmed"));
      }

      @Override
      protected String targetName() {
         return "Next pull";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         yb var2 = yb.b();
         var1.add(new String[]{"Normal item", a(var2.Y)});
         var1.add(new String[]{"Trigger item", a(var2.P)});
      }

      public i() {
         super("Russian Roulette", "Swaps the normal item for the trigger item when armed, visually only", com.larpclient.q.n.y);
         this.o("Arm Bind", "Arm or disarm the trigger item", a2.i::H);
         this.o("From Hand Bind", "Store your hand item as the trigger item when armed, else the normal item", () -> m(this.u().T(com.larpclient.q.n.y).f));
         this.t("Dispenser Bind", hu.q.X);
         this.e("Select table", hu.q.X);
         this.j();
         this.x();
         this.D();
         this.b("Select normal item", "normal item", var0 -> true, "Pick an item", var1 -> {
            yb.b().Y = var1.method_7909();
            this.u().y();
            return "Russian Roulette normal item: " + var1.method_7964().getString();
         }, () -> a(yb.b().Y));
         this.b("Select trigger item", "trigger item", var0 -> true, "Pick an item", var1 -> {
            yb.b().P = var1.method_7909();
            this.u().y();
            return "Russian Roulette trigger item: " + var1.method_7964().getString();
         }, () -> a(yb.b().P));
         this.B("Arm or disarm", () -> {
            if (this.isEnabled()) {
               H();
            } else {
               m("Enable Russian Roulette first");
            }
         }, () -> yb.b().N ? "Armed: press to disarm" : "Arm trigger");
         this.q();
      }

      @Override
      protected void applySettings() {
      }
   }

   public static final class k extends r {
      private final li q = this.addSetting(
         new li(
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

      public k() {
         super("Mexican Jack", "Counts both hands and prepares the dealer's draws", com.larpclient.q.n.m);
         this.q.M("Drawing");
         this.o("Side Bind", "Switch between player and dealer drawing", this::K);
         this.t("Dispenser Bind", hu.q.m);
         this.e("Select table", hu.q.m);
         this.j();
         this.x();
         this.D();
         this.q();
         this.q.a(var1 -> this.applySettings());
      }

      private void K() {
         this.q.setValue(this.q.G("Player") ? "Dealer" : "Player");
         m("Mexican Jack: " + this.q.getModeLabel(this.q.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            q_.j().p(this.q.getValue().toLowerCase(Locale.ROOT), ql.Y());
         }
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         q_ var2 = q_.j();
         var1.add(new String[]{"Player", var2.e()});
         var1.add(new String[]{"Dealer", var2.c()});
         int var3 = var2.S();
         var1.add(new String[]{"Next card", var3 < 1 ? "none" : Integer.toString(var3)});
         int var4 = ql.Y().countTemplates();
         var1.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
      }
   }

   public static final class l extends r {
      private final lo w = this.addSetting(new lo("Hands", "Number of player hands at the table", 1.0, 1.0, 3.0, 1.0));
      private final li J = this.addSetting(
         new li(
            "Draw For",
            "Auto prepares the player's safe draws at 12 to 16, then the dealer's losing draw once the player stands at 17. Player or Dealer keeps one side",
            "Auto",
            "Auto",
            "Player",
            "Dealer"
         )
      );

      public l() {
         super("Blackjack Force Only", "Prepares the dispenser for the draw you want; the server decides what drops", com.larpclient.q.n.x);
         this.O("Side Bind", "Cycle Auto, Player and Dealer", this::u, true);
         this.o("Import Hotbar Bind", "Read the card map templates from your hotbar", this::D);
         this.t("Dispenser Bind", hu.q.k);
         this.e("Select table", hu.q.k);
         this.j();
         this.x();
         this.D();
         this.B("Import hotbar cards", this::D, () -> tw.s().K() ? "Hotbar cards imported" : "Import hotbar cards");
         this.q();
         this.J.a(var1 -> this.applySettings());
         this.w.a(var1 -> this.applySettings());
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         a2.B(var1, (int)Math.round(this.w.getValue()));
         var1.add(new String[]{"Card values", tw.s().K() ? "From hotbar" : "From table slots"});
      }

      private void D() {
         String var1 = tw.s().u(class_310.method_1551());
         m(var1 == null ? "Blackjack Force Only: hotbar card maps imported" : "Hotbar import failed: " + var1);
         if (var1 == null) {
            this.u().y();
         }
      }

      private void u() {
         this.J.P();
         m("Blackjack Force Only draws for: " + this.J.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            com.larpclient.l4 var1 = com.larpclient.l4.K();
            var1.B((int)Math.round(this.w.getValue()));
            var1.s(this.J.getValue());
         }
      }

      @Override
      public void onDisable() {
         super.onDisable();
         com.larpclient.l4.K().s("auto");
      }

      @Override
      protected boolean screenAllowsBinds(class_310 var1) {
         return tw.s().N(var1);
      }
   }

   public static final class n extends r {
      private final li a = this.addSetting(new li("Winner", "Which item is shown as the winning one", "First", "First", "Second", "Middle"));
      private final aj h = this.addSetting(
         new aj("Slot Targets", "Nine digits, one per dispenser slot from top left: 1 first item, 2 second item, 3 middle item", "112132122").A("112132122")
      );

      @Override
      public String setupSummary() {
         ux var1 = ux.o();
         String var2 = this.h.getValue() == null ? "" : this.h.getValue().trim();
         String var3;
         if (B(var2)) {
            var3 = "Slot targets: " + var2.substring(0, 3) + " / " + var2.substring(3, 6) + " / " + var2.substring(6);
         } else {
            int[] var4 = var1.o();
            StringBuilder var5 = new StringBuilder();

            for (int var6 = 0; var6 < 9; var6++) {
               var5.append(var6 > 0 && var6 % 3 == 0 ? " / " : "").append(var4[var6]);
            }

            var3 = "Slot Targets needs exactly 9 digits, each 1, 2 or 3. Still using " + var5;
         }

         return super.setupSummary() + "\nFirst: " + a(var1.Q) + ", second: " + a(var1.D) + ", middle: " + a(var1.S) + "\n" + var3;
      }

      @Override
      public String getDisplayName() {
         return "45/45/10";
      }

      @Override
      public List<String> getSearchAliases() {
         return List.of("45/45/10", "forty five");
      }

      private static boolean B(String var0) {
         return var0.matches("[123]{9}");
      }

      public n() {
         super("45-45-10", "Shows a chosen item of the 45/45/10 table as the winner, visually only", com.larpclient.q.n.P);
         this.o("Side Bind", "Cycle first, second and middle winner", this::j);
         this.o("From Hand Bind", "Store the item in your hand as the item of the current Winner", () -> this.M());
         this.t("Dispenser Bind", hu.q.g);
         this.e("Select table", hu.q.g);
         this.j();
         this.x();
         this.D();
         this.b("Select first item", "first (45%) item", var0 -> true, "Pick an item", var1 -> {
            ux.o().Q = var1.method_7909();
            this.u().y();
            return "45/45/10 first item: " + var1.method_7964().getString();
         }, () -> a(ux.o().Q));
         this.b("Select second item", "second (45%) item", var0 -> true, "Pick an item", var1 -> {
            ux.o().D = var1.method_7909();
            this.u().y();
            return "45/45/10 second item: " + var1.method_7964().getString();
         }, () -> a(ux.o().D));
         this.b("Select middle item", "middle (10%) item", var0 -> true, "Pick an item", var1 -> {
            ux.o().S = var1.method_7909();
            this.u().y();
            return "45/45/10 middle item: " + var1.method_7964().getString();
         }, () -> a(ux.o().S));
         this.q();
         this.a.a(var1 -> this.applySettings());
         this.h.a(var1 -> this.applySettings());
      }

      private void M() {
         com.larpclient.q.z var1 = this.u().T(com.larpclient.q.n.P);
         m(var1.f);
      }

      private void j() {
         this.a.setValue(this.a.G("First") ? "Second" : (this.a.G("Second") ? "Middle" : "First"));
         m("45/45/10 winner: " + this.a.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            ux var1 = ux.o();

            var1.z = switch ((String)this.a.getValue()) {
               case "Second" -> ux.k.l;
               case "Middle" -> ux.k.V;
               default -> ux.k.L;
            };
            String var5 = this.h.getValue() == null ? "" : this.h.getValue().trim();
            if (B(var5)) {
               int[] var6 = new int[9];

               for (int var4 = 0; var4 < 9; var4++) {
                  var6[var4] = var5.charAt(var4) - '0';
               }

               var1.g(var6);
            }

            this.u().y();
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         ux var2 = ux.o();

         class_1792 var3 = switch (var2.z) {
            case L -> var2.Q;
            case l -> var2.D;
            case V -> var2.S;
         };
         var1.add(new String[]{"Winner item", a(var3)});
         if (!B(this.h.getValue() == null ? "" : this.h.getValue().trim())) {
            var1.add(new String[]{"Slot Targets", "Invalid, see settings"});
         }
      }
   }

   public static final class o extends r {
      @Override
      public String setupSummary() {
         int var1 = bk.W().n();
         return var1 < 0 ? "No lure 3 rod in your inventory" : "Rig rod in inventory slot " + (var1 + 1);
      }

      public o() {
         super("Fishing Game", "Silently casts a lure 3 rod from your inventory when you use any rod", com.larpclient.q.n.q);
         this.o("Hide Rod Bind", "Hide or show the rig rod in your inventory display", () -> {
            bk.W().m();
            m("Fishing Game: rod " + (bk.W().R ? "hidden" : "visible"));
         });
         this.B("Hide or show rig rod", () -> {
            bk.W().m();
            m("Fishing Game: rod " + (bk.W().R ? "hidden" : "visible"));
         }, () -> bk.W().R ? "Rig rod hidden: press to show" : "Hide rig rod");
         this.y("Open game log", as::O);
         this.x();
         this.D();
      }

      @Override
      public void onEnable() {
         super.onEnable();
         bk.W().U(true);
      }

      @Override
      protected void releaseExtra() {
         bk.W().U(false);
      }

      @Override
      protected void applySettings() {
      }

      @Override
      public String getStatus() {
         return bk.W().U ? "Rig on" : "Rig paused";
      }

      @Override
      public String setupHint() {
         return "Keep a lure 3 rod anywhere in your inventory, then cast with any rod.";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         bk var2 = bk.W();
         int var3 = var2.n();
         var1.add(new String[]{"Rig", var2.U ? "On" : "Paused"});
         var1.add(new String[]{"Lure 3 rod", var3 < 0 ? "Not found" : "Slot " + (var3 + 1)});
         var1.add(new String[]{"Rod display", var2.R ? "Hidden" : "Visible"});
      }
   }

   abstract static class q extends r {
      private final f4 G;
      private final int e;
      private final li T;

      q(String var1, String var2, com.larpclient.q.n var3, f4 var4, int var5, hu.q var6) {
         super(var1, var2, var3);
         this.G = var4;
         this.e = var5;
         String[] var7 = new String[var5];

         for (int var8 = 0; var8 < var5; var8++) {
            var7[var8] = Integer.toString(var8 + 1);
         }

         this.T = this.addSetting(new li("Winner", "Which piece is shown as the winner", "1", var7) {
            @Override
            public String getModeLabel(String var1) {
               return q.this.R(Integer.parseInt(var1));
            }
         });
         this.o("Side Bind", "Advance to the next winner", this::k);
         this.t("Dispenser Bind", var6);
         this.e("Select table", var6);
         this.j();
         this.x();
         this.D();
         this.q();
         this.T.a(var1x -> this.applySettings());
      }

      private String R(int var1) {
         String var2 = this.G == f4.H ? (class_310.method_1551() != null && class_310.method_1551().field_1687 != null ? this.G.q(var1) : "") : this.G.f(var1);
         return var2 != null && !var2.isBlank() ? var2 : (this.G == f4.H ? "Horse " + var1 : Integer.toString(var1));
      }

      private void k() {
         this.T.setValue(Integer.toString(Integer.parseInt(this.T.getValue()) % this.e + 1));
         m(this.getName() + " winner: " + this.T.getModeLabel(this.T.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            this.G.x(Integer.parseInt(this.T.getValue()));
            this.G.a(true);
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Table", this.G.n()});
      }
   }

   public static final class s extends r {
      private final li T = this.addSetting(new li("Target", "Which table's drawn paper is shown as the higher one", "Host", "Host", "Viewer"));
      private final l3 N = this.addSetting(new l3("Universal Items", "Learn one item set and use it for both tables", true));
      private final aj r = this.addSetting(new aj("Host Suffix", "Name suffix that marks host table items", "Host"));
      private final aj e = this.addSetting(new aj("Viewer Suffix", "Name suffix that marks viewer table items", "Viewer"));

      public s() {
         super("Paper", "Shows the chosen table's paper draw as the higher one, visually only", com.larpclient.q.n.C);
         this.o("Side Bind", "Switch the target between host and viewer", this::A);
         this.t("Host Dispenser Bind", hu.q.U);
         this.e("Select host table", hu.q.U);
         this.t("Viewer Dispenser Bind", hu.q.o);
         this.e("Select viewer table", hu.q.o);
         this.j();
         this.x();
         this.D();
         this.q();
         this.T.a(var1 -> this.applySettings());
         this.N.a(var1 -> {
            this.applySettings();
            if (this.isEnabled()) {
               q8.G("Rig", "Paper: Universal Items {}", var1 ? "on" : "off");
            }
         });
         this.r.a(var1 -> this.applySettings());
         this.e.a(var1 -> this.applySettings());
      }

      private void A() {
         this.T.setValue(this.T.G("Host") ? "Viewer" : "Host");
         m("Paper: " + this.T.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            t3 var1 = t3.c();
            if (var1.z(this.N.getValue())) {
               this.u().y();
            }

            var1.b(this.r.getValue());
            var1.f(this.e.getValue());
            var1.O(this.T.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Items", this.N.getValue() ? "Same for both tables" : "Per table"});
      }
   }

   public static final class t extends r {
      private final lo M = this.addSetting(new lo("Hands", "Number of player hands at the table", 1.0, 1.0, 3.0, 1.0));
      private final li d = this.addSetting(
         new li(
            "Side",
            "Who takes the next cards. Player hands are counted; dealer draws are prepared to beat the best hand. Starts each round on the first hand",
            "Player",
            a2.n
         ) {
            @Override
            public List<String> getVisibleModes() {
               int var1 = t.this.d();
               return Arrays.stream(a2.n).filter(var1x -> var1x.equals("Dealer") || a2.p(var1x) < var1).toList();
            }

            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Player") && t.this.d() > 1 ? "Hand 1" : var1;
            }
         }
      );

      public t() {
         super("Blackjack", "Counts every hand and prepares the dealer's draws to beat the best player hand", com.larpclient.q.n.W);
         this.o("Side Bind", "Move to the next hand, then the dealer", this::i);
         this.t("Dispenser Bind", hu.q.G);
         this.e("Select table", hu.q.G);
         this.j();
         this.x();
         this.D();
         this.q();
         this.d.a(var1 -> this.applySettings());
         this.M.a(var1 -> {
            if (a2.p(this.d.getValue()) >= this.d()) {
               this.d.setValue("Player");
            }

            this.applySettings();
         });
      }

      private int d() {
         return (int)Math.round(this.M.getValue());
      }

      private void i() {
         this.d.P();
         m("Blackjack: " + this.d.getModeLabel(this.d.getValue()) + " draws next");
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            com.larpclient.l4 var1 = com.larpclient.l4.K();
            var1.B(this.d());
            var1.h(a2.p(this.d.getValue()));
         }
      }

      @Override
      protected void afterTick(class_310 var1) {
         String var2 = a2.g(com.larpclient.l4.K().k());
         if (!this.d.G(var2) && this.d.acceptsValue(var2)) {
            this.d.setValue(var2);
         }
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         a2.B(var1, this.d());
         var1.add(new String[]{"Dealer plan", com.larpclient.l4.K().n()});
      }
   }

   public static final class v extends r {
      private final li T = this.addSetting(
         new li("Target", "Which hand the automatic plan makes win. Off leaves the draws alone unless you queue a preset", "Player", "Player", "Banker", "Off") {
            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Off") ? "Off (presets only)" : var1;
            }
         }
      );
      private final aj[] P = new aj[5];
      private final lw[] L = new lw[5];

      public v() {
         super("Baccarat", "Counts the hands and queues card values for the next draws", com.larpclient.q.n.j);
         this.o("Side Bind", "Switch between player and banker", this::H);
         this.t("Player Dispenser Bind", hu.q.q);
         this.e("Select player table", hu.q.q);
         this.t("Banker Dispenser Bind", hu.q.E);
         this.e("Select banker table", hu.q.E);
         this.j();

         for (int var1 = 0; var1 < 5; var1++) {
            int var2 = var1;
            this.P[var1] = this.addSetting(new aj("Preset " + (var1 + 1), "Up to three card values 1 to 9 separated by spaces, queued by the preset bind", ""));
            this.P[var1].a(var1x -> this.applySettings());
            this.L[var1] = this.o("Preset " + (var1 + 1) + " Bind", "Queue the values in Preset " + (var1 + 1), () -> this.r(var2));
         }

         this.x();
         this.D();
         this.q();
         this.T.a(var1x -> this.applySettings());
      }

      private static int[] M(String var0) {
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

      private void r(int var1) {
         int[] var2 = M(this.P[var1].getValue());
         if (var2.length == 0) {
            m("Baccarat preset " + (var1 + 1) + " is empty");
         } else if (qq.f().isSwapBusy()) {
            m("Baccarat preset " + (var1 + 1) + " not queued: a card is being swapped, press it again after this draw");
         } else {
            if (this.u().B(var1, var2)) {
               m("Baccarat preset " + (var1 + 1) + " queued for the next draws: " + J(var2));
            } else {
               m("Baccarat preset " + (var1 + 1) + " not queued: open a Baccarat table once so the cards are learned");
            }
         }
      }

      private static String J(int[] var0) {
         StringBuilder var1 = new StringBuilder();

         for (int var5 : var0) {
            var1.append(var1.isEmpty() ? "" : " ").append(var5);
         }

         return var1.toString();
      }

      private static boolean Z(String var0) {
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
            String var3 = this.P[var2].getValue();
            if (var3 != null && !var3.isBlank()) {
               int[] var4 = M(var3);
               var1.append("\nPreset ")
                  .append(var2 + 1)
                  .append(": ")
                  .append(var4.length == 0 ? "no valid values" : J(var4))
                  .append(Z(var3) ? "" : " (only values 1 to 9, at most three, are used)");
            }
         }

         return var1.toString();
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         lk var2 = lk.k();
         int var3 = var2.v() > 0 ? 1 : 0;
         var1.add(new String[]{"Player", var3 != 0 ? Integer.toString(var2.E()) : "-"});
         var1.add(new String[]{"Banker", var3 != 0 ? Integer.toString(var2.s()) : "-"});
         String var4 = var2.N();
         var1.add(var4.isEmpty() ? new String[]{"Next card", qq.f().pendingLabel()} : new String[]{"Preset queue", var4});
         int var5 = qq.f().countTemplates();
         var1.add(new String[]{"Cards learned", var5 == 0 ? "None, open a table once" : Integer.toString(var5)});
      }

      private void H() {
         this.T.setValue(this.T.G("Player") ? "Banker" : "Player");
         m("Baccarat: " + this.T.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            lk.k().U(this.T.getValue().toLowerCase(Locale.ROOT), qq.f());
            int[] var1 = new int[15];
            Arrays.fill(var1, -1);

            for (int var2 = 0; var2 < 5; var2++) {
               int[] var3 = M(this.P[var2].getValue());
               System.arraycopy(var3, 0, var1, var2 * 3, var3.length);
            }

            this.u().X(var1);
         }
      }
   }

   public static final class w extends a2.q {
      public w() {
         super("Horse Racing", "Shows the chosen horse armor as the winner", com.larpclient.q.n.d, f4.H, 3, hu.q.w);
      }
   }

   public static final class x extends a2.q {
      public x() {
         super("Armor Game", "Shows the chosen armor piece as the winner", com.larpclient.q.n.e, f4.Y, 4, hu.q.J);
      }
   }

   public static final class z extends r {
      private final li v = this.addSetting(new li("Parity", "Which parity is shown as the result", "Odd", "Odd", "Even"));

      public z() {
         super("Even or Odd", "Swaps the dealt map card for one of the chosen parity", com.larpclient.q.n.S);
         this.o("Side Bind", "Switch between odd and even", this::S);
         this.t("Dispenser Bind", hu.q.C);
         this.e("Select table", hu.q.C);
         this.j();
         this.x();
         this.D();
         this.q();
         this.v.a(var1 -> this.applySettings());
      }

      private void S() {
         this.v.setValue(this.v.G("Odd") ? "Even" : "Odd");
         m("Even or Odd: " + this.v.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            qc var1 = qc.i();
            var1.p(this.v.G("Even") ? 1 : 0);
            var1.clearQueue();
            this.u().y();
         }
      }

      @Override
      protected String targetName() {
         return "Result";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         qc var2 = qc.i();
         var1.add(new String[]{"Next card", var2.pendingLabel()});
         int var3 = var2.countTemplates();
         var1.add(new String[]{"Cards learned", var3 == 0 ? "None, open the table once" : Integer.toString(var3)});
      }
   }
}
