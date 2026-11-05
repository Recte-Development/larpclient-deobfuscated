package com.larpclient.module;

import com.larpclient._g;
import com.larpclient._u;
import com.larpclient.c3;
import com.larpclient.c6;
import com.larpclient.ep;
import com.larpclient.g;
import com.larpclient.kq;
import com.larpclient.mi;
import com.larpclient.mj;
import com.larpclient.pj;
import com.larpclient.tb;
import com.larpclient.tr;
import com.larpclient.z6;
import com.larpclient.z8;
import com.larpclient.ze;
import com.larpclient.zu;
import com.larpclient.zv;
import com.larpclient.zw;
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

public final class m {
   private static final String[] O = new String[]{"Player", "Hand 2", "Hand 3", "Dealer"};

   private m() {
   }

   private static String c(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1).toLowerCase(Locale.ROOT);
   }

   private static int G(String var0) {
      return switch (var0) {
         case "Hand 2" -> 1;
         case "Hand 3" -> 2;
         case "Dealer" -> -1;
         default -> 0;
      };
   }

   private static String o(int var0) {
      return var0 < 0 ? "Dealer" : (var0 == 1 ? "Hand 2" : (var0 == 2 ? "Hand 3" : "Player"));
   }

   private static void z(List<String[]> var0, int var1) {
      mj var2 = mj.D();

      for (int var3 = 0; var3 < var1; var3++) {
         var0.add(new String[]{var1 == 1 ? "Player" : "Hand " + (var3 + 1), var2.Y(var3)});
      }

      var0.add(new String[]{"Dealer", var2.Y(-1)});
      int var4 = ze.V().countTemplates();
      var0.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
   }

   public static final class b extends com.larpclient.module.v {
      private final d6 t = this.addSetting(new d6("Winner", "Which of the two items is shown as the winner", "1", "1", "2") {
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
         super("50-50", "Shows one of the two 50/50 items as the winner and keeps the real pull intact", c3.a.R);
         this.x("Side Bind", "Switch the winning item", this::m, true);
         this.c("Dispenser Bind", ep.o.M);
         this.x("Select table", ep.o.M);
         this.E();
         this.j();
         this.o();
         this.K();
         this.t.h(var1 -> this.applySettings());
      }

      private void m() {
         this.t.setValue(this.t.S("1") ? "2" : "1");
         b("50/50 winner: " + this.t.getModeLabel(this.t.P()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            c6.K().u(Integer.parseInt(this.t.P()));
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Table screen", c6.K().q() ? "Open" : "Closed"});
      }

      @Override
      protected boolean screenAllowsBinds(Minecraft var1) {
         return c6.K().q();
      }
   }

   public static final class c extends com.larpclient.module.v {
      private final d9 s = this.addSetting(new d9("Hands", "Number of player hands at the table", 1.0, 1.0, 3.0, 1.0));
      private final d6 u = this.addSetting(
         new d6(
            "Side",
            "Who takes the next cards. Player hands are counted; dealer draws are prepared to beat the best hand. Starts each round on the first hand",
            "Player",
            m.O
         ) {
            @Override
            public List<String> getVisibleModes() {
               int var1 = c.this.f();
               return Arrays.stream(m.O).filter(var1x -> var1x.equals("Dealer") || m.G(var1x) < var1).toList();
            }

            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Player") && c.this.f() > 1 ? "Hand 1" : var1;
            }
         }
      );

      public c() {
         super("Blackjack", "Counts every hand and prepares the dealer's draws to beat the best player hand", c3.a.E);
         this.i("Side Bind", "Move to the next hand, then the dealer", this::z);
         this.c("Dispenser Bind", ep.o.e);
         this.x("Select table", ep.o.e);
         this.E();
         this.j();
         this.o();
         this.K();
         this.u.h(var1 -> this.applySettings());
         this.s.h(var1 -> {
            if (m.G(this.u.P()) >= this.f()) {
               this.u.setValue("Player");
            }

            this.applySettings();
         });
      }

      private int f() {
         return (int)Math.round(this.s.P());
      }

      private void z() {
         this.u.f();
         b("Blackjack: " + this.u.getModeLabel(this.u.P()) + " draws next");
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            mj var1 = mj.D();
            var1.o(this.f());
            var1.U(m.G(this.u.P()));
         }
      }

      @Override
      protected void afterTick(Minecraft var1) {
         String var2 = m.o(mj.D().A());
         if (!this.u.S(var2) && this.u.acceptsValue(var2)) {
            this.u.setValue(var2);
         }
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         m.z(var1, this.f());
         var1.add(new String[]{"Dealer plan", mj.D().r()});
      }
   }

   public static final class e extends com.larpclient.module.v {
      private final d6 D = this.addSetting(new d6("Target", "Whether the next card is made to read higher or lower", "Higher", "Higher", "Lower"));

      public e() {
         super("Higher or Lower", "Prepares the next card to read higher or lower than the reference", c3.a.s);
         this.i("Side Bind", "Switch between higher and lower", this::r);
         this.c("Dispenser Bind", ep.o.c);
         this.x("Select table", ep.o.c);
         this.E();
         this.j();
         this.o();
         this.K();
         this.D.h(var1 -> this.applySettings());
      }

      private void r() {
         this.D.setValue(this.D.S("Higher") ? "Lower" : "Higher");
         b("Higher or Lower: " + this.D.P());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            z8.l().o(this.D.P().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         z8 var2 = z8.l();
         int var3 = var2.g();
         var1.add(new String[]{"Shown card", var3 < 1 ? "-" : Integer.toString(var3)});
         var1.add(new String[]{"Next card", var2.pendingLabel()});
         int var4 = var2.countTemplates();
         var1.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
      }
   }

   public static final class f extends m.t {
      public f() {
         super("Horse Racing", "Shows the chosen horse armor as the winner", c3.a.l, _g.G, 3, ep.o.Q);
      }
   }

   public static final class h extends com.larpclient.module.v {
      private final d9 B = this.addSetting(new d9("Hands", "Number of player hands at the table", 1.0, 1.0, 3.0, 1.0));
      private final d6 X = this.addSetting(
         new d6(
            "Draw For",
            "Auto prepares the player's safe draws at 12 to 16, then the dealer's losing draw once the player stands at 17. Player or Dealer keeps one side",
            "Auto",
            "Auto",
            "Player",
            "Dealer"
         )
      );

      public h() {
         super("Blackjack Force Only", "Prepares the dispenser for the draw you want; the server decides what drops", c3.a.r);
         this.x("Side Bind", "Cycle Auto, Player and Dealer", this::o, true);
         this.i("Import Hotbar Bind", "Read the card map templates from your hotbar", this::U);
         this.c("Dispenser Bind", ep.o.v);
         this.x("Select table", ep.o.v);
         this.E();
         this.j();
         this.o();
         this.c("Import hotbar cards", this::U, () -> kq.I().f() ? "Hotbar cards imported" : "Import hotbar cards");
         this.K();
         this.X.h(var1 -> this.applySettings());
         this.B.h(var1 -> this.applySettings());
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         m.z(var1, (int)Math.round(this.B.P()));
         var1.add(new String[]{"Card values", kq.I().f() ? "From hotbar" : "From table slots"});
      }

      private void U() {
         String var1 = kq.I().w(Minecraft.getInstance());
         b(var1 == null ? "Blackjack Force Only: hotbar card maps imported" : "Hotbar import failed: " + var1);
         if (var1 == null) {
            this.t().w();
         }
      }

      private void o() {
         this.X.f();
         b("Blackjack Force Only draws for: " + this.X.P());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            mj var1 = mj.D();
            var1.o((int)Math.round(this.B.P()));
            var1.h(this.X.P());
         }
      }

      @Override
      public void onDisable() {
         super.onDisable();
         mj.D().h("auto");
      }

      @Override
      protected boolean screenAllowsBinds(Minecraft var1) {
         return kq.I().l(var1);
      }
   }

   public static final class j extends com.larpclient.module.v {
      @Override
      public String setupSummary() {
         int var1 = pj.U().B();
         return var1 < 0 ? "No lure 3 rod in your inventory" : "Rig rod in inventory slot " + (var1 + 1);
      }

      public j() {
         super("Fishing Game", "Silently casts a lure 3 rod from your inventory when you use any rod", c3.a.f);
         this.i("Hide Rod Bind", "Hide or show the rig rod in your inventory display", () -> {
            pj.U().g();
            b("Fishing Game: rod " + (pj.U().Z ? "hidden" : "visible"));
         });
         this.c("Hide or show rig rod", () -> {
            pj.U().g();
            b("Fishing Game: rod " + (pj.U().Z ? "hidden" : "visible"));
         }, () -> pj.U().Z ? "Rig rod hidden: press to show" : "Hide rig rod");
         this.D("Open game log", com.larpclient.module.j::b);
         this.j();
         this.o();
      }

      @Override
      public void onEnable() {
         super.onEnable();
         pj.U().K(true);
      }

      @Override
      protected void releaseExtra() {
         pj.U().K(false);
      }

      @Override
      protected void applySettings() {
      }

      @Override
      public String getStatus() {
         return pj.U().F ? "Rig on" : "Rig paused";
      }

      @Override
      public String setupHint() {
         return "Keep a lure 3 rod anywhere in your inventory, then cast with any rod.";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         pj var2 = pj.U();
         int var3 = var2.B();
         var1.add(new String[]{"Rig", var2.F ? "On" : "Paused"});
         var1.add(new String[]{"Lure 3 rod", var3 < 0 ? "Not found" : "Slot " + (var3 + 1)});
         var1.add(new String[]{"Rod display", var2.Z ? "Hidden" : "Visible"});
      }
   }

   public static final class k extends m.t {
      public k() {
         super("Armor Game", "Shows the chosen armor piece as the winner", c3.a.Z, _g.Y, 4, ep.o.U);
      }
   }

   public static final class p extends com.larpclient.module.v {
      private final d6 L = this.addSetting(new d6("Target", "Which table's drawn paper is shown as the higher one", "Host", "Host", "Viewer"));
      private final dh g = this.addSetting(new dh("Universal Items", "Learn one item set and use it for both tables", true));
      private final ds u = this.addSetting(new ds("Host Suffix", "Name suffix that marks host table items", "Host"));
      private final ds m = this.addSetting(new ds("Viewer Suffix", "Name suffix that marks viewer table items", "Viewer"));
      private boolean h;

      public p() {
         super("Paper", "Shows the chosen table's paper draw as the higher one, visually only", c3.a.e);
         this.i("Side Bind", "Switch the target between host and viewer", this::b);
         this.c("Host Dispenser Bind", ep.o.k);
         this.x("Select host table", ep.o.k);
         this.c("Viewer Dispenser Bind", ep.o.h);
         this.x("Select viewer table", ep.o.h);
         this.E();
         this.j();
         this.o();
         this.K();
         this.L.h(var1 -> this.applySettings());
         this.g.h(var1 -> {
            if (!this.h) {
               this.applySettings();
            }
         });
         this.u.h(var1 -> this.applySettings());
         this.m.h(var1 -> this.applySettings());
      }

      private void b() {
         this.L.setValue(this.L.S("Host") ? "Viewer" : "Host");
         b("Paper: " + this.L.P());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            mi var1 = mi.L();
            var1.W(this.g.P());
            var1.f(this.u.P());
            var1.X(this.m.P());
            var1.C(this.L.P().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void afterTick(Minecraft var1) {
         boolean var2 = mi.L().l();
         if (this.g.P() != var2) {
            this.h = (boolean)1;

            try {
               this.g.setValue(var2);
            } finally {
               this.h = (boolean)0;
            }

            if (!var2) {
               b("Paper: learned separate host and viewer items, Universal Items is now off");
            }
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Items", this.g.P() ? "Same for both tables" : "Per table"});
      }
   }

   public static final class q extends com.larpclient.module.v {
      private final d6 y = this.addSetting(
         new d6(
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

      public q() {
         super("Mexican Jack", "Counts both hands and prepares the dealer's draws", c3.a.M);
         this.y.n("Drawing");
         this.i("Side Bind", "Switch between player and dealer drawing", this::j);
         this.c("Dispenser Bind", ep.o.q);
         this.x("Select table", ep.o.q);
         this.E();
         this.j();
         this.o();
         this.K();
         this.y.h(var1 -> this.applySettings());
      }

      private void j() {
         this.y.setValue(this.y.S("Player") ? "Dealer" : "Player");
         b("Mexican Jack: " + this.y.getModeLabel(this.y.P()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            g.s().K(this.y.P().toLowerCase(Locale.ROOT), zw.K());
         }
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         g var2 = g.s();
         var1.add(new String[]{"Player", var2.f()});
         var1.add(new String[]{"Dealer", var2.l()});
         int var3 = var2.F();
         var1.add(new String[]{"Next card", var3 < 1 ? "none" : Integer.toString(var3)});
         int var4 = zw.K().countTemplates();
         var1.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
      }
   }

   public static final class s extends com.larpclient.module.v {
      private final d6 L = this.addSetting(
         new d6("Target", "Which hand the automatic plan makes win. Off leaves the draws alone unless you queue a preset", "Player", "Player", "Banker", "Off") {
            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Off") ? "Off (presets only)" : var1;
            }
         }
      );
      private final ds[] K = new ds[5];
      private final dv[] A = new dv[5];

      public s() {
         super("Baccarat", "Counts the hands and queues card values for the next draws", c3.a.i);
         this.i("Side Bind", "Switch between player and banker", this::X);
         this.c("Player Dispenser Bind", ep.o.m);
         this.x("Select player table", ep.o.m);
         this.c("Banker Dispenser Bind", ep.o.p);
         this.x("Select banker table", ep.o.p);
         this.E();

         for (int var1 = 0; var1 < 5; var1++) {
            int var2 = var1;
            this.K[var1] = this.addSetting(new ds("Preset " + (var1 + 1), "Up to three card values 1 to 9 separated by spaces, queued by the preset bind", ""));
            this.K[var1].h(var1x -> this.applySettings());
            this.A[var1] = this.i("Preset " + (var1 + 1) + " Bind", "Queue the values in Preset " + (var1 + 1), () -> this.L(var2));
         }

         this.j();
         this.o();
         this.K();
         this.L.h(var1x -> this.applySettings());
      }

      private static int[] X(String var0) {
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

      private void L(int var1) {
         int[] var2 = X(this.K[var1].P());
         if (var2.length == 0) {
            b("Baccarat preset " + (var1 + 1) + " is empty");
         } else if (z6.u().isSwapBusy()) {
            b("Baccarat preset " + (var1 + 1) + " not queued: a card is being swapped, press it again after this draw");
         } else {
            if (this.t().r(var1, var2)) {
               b("Baccarat preset " + (var1 + 1) + " queued for the next draws: " + d(var2));
            } else {
               b("Baccarat preset " + (var1 + 1) + " not queued: open a Baccarat table once so the cards are learned");
            }
         }
      }

      private static String d(int[] var0) {
         StringBuilder var1 = new StringBuilder();

         for (int var5 : var0) {
            var1.append(var1.isEmpty() ? "" : " ").append(var5);
         }

         return var1.toString();
      }

      private static boolean U(String var0) {
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
            String var3 = this.K[var2].P();
            if (var3 != null && !var3.isBlank()) {
               int[] var4 = X(var3);
               var1.append("\nPreset ")
                  .append(var2 + 1)
                  .append(": ")
                  .append(var4.length == 0 ? "no valid values" : d(var4))
                  .append(U(var3) ? "" : " (only values 1 to 9, at most three, are used)");
            }
         }

         return var1.toString();
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         tb var2 = tb.q();
         int var3 = var2.i() > 0 ? 1 : 0;
         var1.add(new String[]{"Player", var3 != 0 ? Integer.toString(var2.V()) : "-"});
         var1.add(new String[]{"Banker", var3 != 0 ? Integer.toString(var2.a()) : "-"});
         String var4 = var2.D();
         var1.add(var4.isEmpty() ? new String[]{"Next card", z6.u().pendingLabel()} : new String[]{"Preset queue", var4});
         int var5 = z6.u().countTemplates();
         var1.add(new String[]{"Cards learned", var5 == 0 ? "None, open a table once" : Integer.toString(var5)});
      }

      private void X() {
         this.L.setValue(this.L.S("Player") ? "Banker" : "Player");
         b("Baccarat: " + this.L.P());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            tb.q().l(this.L.P().toLowerCase(Locale.ROOT), z6.u());
            int[] var1 = new int[15];
            Arrays.fill(var1, -1);

            for (int var2 = 0; var2 < 5; var2++) {
               int[] var3 = X(this.K[var2].P());
               System.arraycopy(var3, 0, var1, var2 * 3, var3.length);
            }

            this.t().V(var1);
         }
      }
   }

   abstract static class t extends com.larpclient.module.v {
      private final _g i;
      private final int l;
      private final d6 b;

      t(String var1, String var2, c3.a var3, _g var4, int var5, ep.o var6) {
         super(var1, var2, var3);
         this.i = var4;
         this.l = var5;
         String[] var7 = new String[var5];

         for (int var8 = 0; var8 < var5; var8++) {
            var7[var8] = Integer.toString(var8 + 1);
         }

         this.b = this.addSetting(new d6("Winner", "Which piece is shown as the winner", "1", var7) {
            @Override
            public String getModeLabel(String var1) {
               return t.this.Y(Integer.parseInt(var1));
            }
         });
         this.i("Side Bind", "Advance to the next winner", this::I);
         this.c("Dispenser Bind", var6);
         this.x("Select table", var6);
         this.E();
         this.j();
         this.o();
         this.K();
         this.b.h(var1x -> this.applySettings());
      }

      private String Y(int var1) {
         String var2 = this.i == _g.G ? (Minecraft.getInstance() != null && Minecraft.getInstance().level != null ? this.i.D(var1) : "") : this.i.X(var1);
         return var2 != null && !var2.isBlank() ? var2 : (this.i == _g.G ? "Horse " + var1 : Integer.toString(var1));
      }

      private void I() {
         this.b.setValue(Integer.toString(Integer.parseInt(this.b.P()) % this.l + 1));
         b(this.getName() + " winner: " + this.b.getModeLabel(this.b.P()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            this.i.o(Integer.parseInt(this.b.P()));
            this.i.m(true);
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Table", this.i.A()});
      }
   }

   public static final class u extends com.larpclient.module.v {
      private final d6 l = this.addSetting(new d6("Target", "Whether the shot is shown as a win or a loss", "Win", "Win", "Lose"));

      @Override
      public String setupSummary() {
         return super.setupSummary() + "\nX map: " + (com.larpclient.z.v().t() ? "selected" : "none");
      }

      public u() {
         super("Arrow Game", "Shows the arrow dispenser and item frames for a win or a loss, visually only", c3.a.K);
         this.i("Side Bind", "Switch between win and lose", this::H);
         this.i("Note Block Bind", "Look at a note block and press to bind it as the fake press trigger", this::T);
         this.i("X Map Bind", "Hold the filled map used as the X mark and press to store it", () -> {
            com.larpclient.z.v().G();
            b("Arrow Game: X map stored");
         });
         this.i("Clear X Map Bind", "Forget the stored X map", () -> {
            com.larpclient.z.v().T();
            b("Arrow Game: X map cleared");
         });
         this.c("Dispenser Bind", ep.o.x);
         this.x("Select table", ep.o.x);
         this.E();
         this.j();
         this.o();
         this.b(
            "Select X map",
            "filled map used as the X mark",
            var0 -> var0.is(Items.FILLED_MAP) && var0.get(DataComponents.MAP_ID) != null,
            "Pick a filled map",
            var0 -> {
               int var1 = ((MapId)var0.get(DataComponents.MAP_ID)).id();
               tr.O().L(var1);
               return "Arrow Game X map: map #" + var1;
            },
            () -> com.larpclient.z.v().t() ? "" : null
         );
         this.c(
            "Select note block",
            () -> d.X(
               "Right-click the note block that fires the shot (Esc cancels)",
               var0 -> var0.getBlock() instanceof NoteBlock,
               "That is not a note block",
               var0 -> {
                  com.larpclient.z.v().o(Minecraft.getInstance(), var0);
                  return "Arrow Game note block selected";
               }
            ),
            () -> {
               int var0 = tr.O().M();
               return var0 == 0 ? "Select note block" : (var0 == 1 ? "Note block selected" : var0 + " note blocks selected");
            }
         );
         this.c("Clear X map", () -> {
            com.larpclient.z.v().T();
            b("Arrow Game: X map cleared");
         }, () -> com.larpclient.z.v().t() ? "Clear X map" : "No X map to clear");
         this.K();
         this.l.h(var1 -> this.applySettings());
      }

      private void T() {
         Minecraft var1 = Minecraft.getInstance();
         if (var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var2.getType() == Type.BLOCK
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof NoteBlock) {
            com.larpclient.z.v().o(var1, var2.getBlockPos());
            b("Arrow Game: note block bound");
         } else {
            b("Look at a note block first");
         }
      }

      private void H() {
         this.l.setValue(this.l.S("Win") ? "Lose" : "Win");
         b("Arrow Game: " + this.l.P());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            com.larpclient.z.v().A(this.l.P().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         int var2 = tr.O().M();
         var1.add(new String[]{"Note block", var2 == 0 ? "Not selected" : (var2 == 1 ? "Selected" : var2 + " selected")});
         var1.add(new String[]{"X map", com.larpclient.z.v().t() ? "Selected" : "Not selected"});
      }
   }

   public static final class v extends com.larpclient.module.v {
      @Override
      public String setupSummary() {
         _u var1 = _u.O();
         return super.setupSummary() + "\nNormal: " + u(var1.l) + ", trigger: " + u(var1.z) + (var1.J ? " (armed)" : "");
      }

      private static void P() {
         _u var0 = _u.O();
         if (var0.J) {
            var0.J = (boolean)0;
         } else {
            var0.e();
         }

         b("Russian Roulette: " + (var0.J ? "armed, the next pull shows the trigger item" : "disarmed"));
      }

      @Override
      protected String targetName() {
         return "Next pull";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         _u var2 = _u.O();
         var1.add(new String[]{"Normal item", u(var2.l)});
         var1.add(new String[]{"Trigger item", u(var2.z)});
      }

      public v() {
         super("Russian Roulette", "Swaps the normal item for the trigger item when armed, visually only", c3.a.w);
         this.i("Arm Bind", "Arm or disarm the trigger item", m.v::P);
         this.i("From Hand Bind", "Store your hand item as the trigger item when armed, else the normal item", () -> b(this.t().q(c3.a.w).e));
         this.c("Dispenser Bind", ep.o.l);
         this.x("Select table", ep.o.l);
         this.E();
         this.j();
         this.o();
         this.b("Select normal item", "normal item", var0 -> true, "Pick an item", var1 -> {
            _u.O().l = var1.getItem();
            this.t().w();
            return "Russian Roulette normal item: " + var1.getHoverName().getString();
         }, () -> u(_u.O().l));
         this.b("Select trigger item", "trigger item", var0 -> true, "Pick an item", var1 -> {
            _u.O().z = var1.getItem();
            this.t().w();
            return "Russian Roulette trigger item: " + var1.getHoverName().getString();
         }, () -> u(_u.O().z));
         this.c("Arm or disarm", () -> {
            if (this.isEnabled()) {
               P();
            } else {
               b("Enable Russian Roulette first");
            }
         }, () -> _u.O().J ? "Armed: press to disarm" : "Arm trigger");
         this.K();
      }

      @Override
      protected void applySettings() {
      }
   }

   public static final class x extends com.larpclient.module.v {
      private final d6 q = this.addSetting(new d6("Winner", "Which item is shown as the winning one", "First", "First", "Second", "Middle"));
      private final ds C = this.addSetting(
         new ds("Slot Targets", "Nine digits, one per dispenser slot from top left: 1 first item, 2 second item, 3 middle item", "112132122").C("112132122")
      );

      @Override
      public String setupSummary() {
         zv var1 = zv.x();
         String var2 = this.C.P() == null ? "" : this.C.P().trim();
         String var3;
         if (c(var2)) {
            var3 = "Slot targets: " + var2.substring(0, 3) + " / " + var2.substring(3, 6) + " / " + var2.substring(6);
         } else {
            int[] var4 = var1.W();
            StringBuilder var5 = new StringBuilder();

            for (int var6 = 0; var6 < 9; var6++) {
               var5.append(var6 > 0 && var6 % 3 == 0 ? " / " : "").append(var4[var6]);
            }

            var3 = "Slot Targets needs exactly 9 digits, each 1, 2 or 3. Still using " + var5;
         }

         return super.setupSummary() + "\nFirst: " + u(var1.e) + ", second: " + u(var1.k) + ", middle: " + u(var1.n) + "\n" + var3;
      }

      @Override
      public String getDisplayName() {
         return "45/45/10";
      }

      @Override
      public List<String> getSearchAliases() {
         return List.of("45/45/10", "forty five");
      }

      private static boolean c(String var0) {
         return var0.matches("[123]{9}");
      }

      public x() {
         super("45-45-10", "Shows a chosen item of the 45/45/10 table as the winner, visually only", c3.a.I);
         this.i("Side Bind", "Cycle first, second and middle winner", this::l);
         this.i("From Hand Bind", "Store the item in your hand as the item of the current Winner", () -> this.z());
         this.c("Dispenser Bind", ep.o.P);
         this.x("Select table", ep.o.P);
         this.E();
         this.j();
         this.o();
         this.b("Select first item", "first (45%) item", var0 -> true, "Pick an item", var1 -> {
            zv.x().e = var1.getItem();
            this.t().w();
            return "45/45/10 first item: " + var1.getHoverName().getString();
         }, () -> u(zv.x().e));
         this.b("Select second item", "second (45%) item", var0 -> true, "Pick an item", var1 -> {
            zv.x().k = var1.getItem();
            this.t().w();
            return "45/45/10 second item: " + var1.getHoverName().getString();
         }, () -> u(zv.x().k));
         this.b("Select middle item", "middle (10%) item", var0 -> true, "Pick an item", var1 -> {
            zv.x().n = var1.getItem();
            this.t().w();
            return "45/45/10 middle item: " + var1.getHoverName().getString();
         }, () -> u(zv.x().n));
         this.K();
         this.q.h(var1 -> this.applySettings());
         this.C.h(var1 -> this.applySettings());
      }

      private void z() {
         c3.q var1 = this.t().q(c3.a.I);
         b(var1.e);
      }

      private void l() {
         this.q.setValue(this.q.S("First") ? "Second" : (this.q.S("Second") ? "Middle" : "First"));
         b("45/45/10 winner: " + this.q.P());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            zv var1 = zv.x();

            var1.V = switch ((String)this.q.P()) {
               case "Second" -> zv.u.U;
               case "Middle" -> zv.u.J;
               default -> zv.u.D;
            };
            String var5 = this.C.P() == null ? "" : this.C.P().trim();
            if (c(var5)) {
               int[] var6 = new int[9];

               for (int var4 = 0; var4 < 9; var4++) {
                  var6[var4] = var5.charAt(var4) - '0';
               }

               var1.j(var6);
            }

            this.t().w();
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         zv var2 = zv.x();

         Item var3 = switch (var2.V) {
            case D -> var2.e;
            case U -> var2.k;
            case J -> var2.n;
         };
         var1.add(new String[]{"Winner item", u(var3)});
         if (!c(this.C.P() == null ? "" : this.C.P().trim())) {
            var1.add(new String[]{"Slot Targets", "Invalid, see settings"});
         }
      }
   }

   public static final class z extends com.larpclient.module.v {
      private final d6 G = this.addSetting(new d6("Parity", "Which parity is shown as the result", "Odd", "Odd", "Even"));

      public z() {
         super("Even or Odd", "Swaps the dealt map card for one of the chosen parity", c3.a.z);
         this.i("Side Bind", "Switch between odd and even", this::f);
         this.c("Dispenser Bind", ep.o.K);
         this.x("Select table", ep.o.K);
         this.E();
         this.j();
         this.o();
         this.K();
         this.G.h(var1 -> this.applySettings());
      }

      private void f() {
         this.G.setValue(this.G.S("Odd") ? "Even" : "Odd");
         b("Even or Odd: " + this.G.P());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            zu var1 = zu.v();
            var1.F(this.G.S("Even") ? 1 : 0);
            var1.clearQueue();
            this.t().w();
         }
      }

      @Override
      protected String targetName() {
         return "Result";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         zu var2 = zu.v();
         var1.add(new String[]{"Next card", var2.pendingLabel()});
         int var3 = var2.countTemplates();
         var1.add(new String[]{"Cards learned", var3 == 0 ? "None, open the table once" : Integer.toString(var3)});
      }
   }
}
