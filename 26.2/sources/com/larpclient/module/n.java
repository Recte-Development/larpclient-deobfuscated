package com.larpclient.module;

import com.larpclient.bs;
import com.larpclient.cc;
import com.larpclient.cy;
import com.larpclient.g5;
import com.larpclient.gj;
import com.larpclient.kr;
import com.larpclient.lh;
import com.larpclient.m9;
import com.larpclient.pq;
import com.larpclient.pw;
import com.larpclient.s3;
import com.larpclient.sg;
import com.larpclient.t5;
import com.larpclient.w0;
import com.larpclient.w3;
import com.larpclient.wc;
import com.larpclient.we;
import com.larpclient.wk;
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

public final class n {
   private static final String[] r = new String[]{"Player", "Hand 2", "Hand 3", "Dealer"};

   private n() {
   }

   private static String r(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1).toLowerCase(Locale.ROOT);
   }

   private static int P(String var0) {
      return switch (var0) {
         case "Hand 2" -> 1;
         case "Hand 3" -> 2;
         case "Dealer" -> -1;
         default -> 0;
      };
   }

   private static String s(int var0) {
      return var0 < 0 ? "Dealer" : (var0 == 1 ? "Hand 2" : (var0 == 2 ? "Hand 3" : "Player"));
   }

   private static void u(List<String[]> var0, int var1) {
      s3 var2 = s3.S();

      for (int var3 = 0; var3 < var1; var3++) {
         var0.add(new String[]{var1 == 1 ? "Player" : "Hand " + (var3 + 1), var2.a(var3)});
      }

      var0.add(new String[]{"Dealer", var2.a(-1)});
      int var4 = we.u().countTemplates();
      var0.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
   }

   public static final class a extends to {
      private final com.larpclient.module.l a = this.addSetting(
         new com.larpclient.module.l("Target", "Which table's drawn paper is shown as the higher one", "Host", "Host", "Viewer")
      );
      private final com.larpclient.module.s H = this.addSetting(
         new com.larpclient.module.s("Universal Items", "Learn one item set and use it for both tables", true)
      );
      private final com.larpclient.module.g y = this.addSetting(new com.larpclient.module.g("Host Suffix", "Name suffix that marks host table items", "Host"));
      private final com.larpclient.module.g F = this.addSetting(
         new com.larpclient.module.g("Viewer Suffix", "Name suffix that marks viewer table items", "Viewer")
      );

      public a() {
         super("Paper", "Shows the chosen table's paper draw as the higher one, visually only", sg.i.V);
         this.L("Side Bind", "Switch the target between host and viewer", this::p);
         this.C("Host Dispenser Bind", gj.d.J);
         this.Z("Select host table", gj.d.J);
         this.C("Viewer Dispenser Bind", gj.d.R);
         this.Z("Select viewer table", gj.d.R);
         this.M();
         this.V();
         this.N();
         this.h();
         this.a.o(var1 -> this.applySettings());
         this.H.o(var1 -> {
            this.applySettings();
            if (this.isEnabled()) {
               com.larpclient.h.A("Rig", "Paper: Universal Items {}", var1 ? "on" : "off");
            }
         });
         this.y.o(var1 -> this.applySettings());
         this.F.o(var1 -> this.applySettings());
      }

      private void p() {
         this.a.setValue(this.a.O("Host") ? "Viewer" : "Host");
         c("Paper: " + this.a.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            g5 var1 = g5.a();
            if (var1.G(this.H.getValue())) {
               this.G().Q();
            }

            var1.d(this.y.getValue());
            var1.E(this.F.getValue());
            var1.F(this.a.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Items", this.H.getValue() ? "Same for both tables" : "Per table"});
      }
   }

   public static final class b extends to {
      private final com.larpclient.module.l Y = this.addSetting(
         new com.larpclient.module.l("Winner", "Which of the two items is shown as the winner", "1", "1", "2") {
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

      public b() {
         super("50-50", "Shows one of the two 50/50 items as the winner and keeps the real pull intact", sg.i.a);
         this.z("Side Bind", "Switch the winning item", this::P, true);
         this.C("Dispenser Bind", gj.d.V);
         this.Z("Select table", gj.d.V);
         this.M();
         this.V();
         this.N();
         this.h();
         this.Y.o(var1 -> this.applySettings());
      }

      private void P() {
         this.Y.setValue(this.Y.O("1") ? "2" : "1");
         c("50/50 winner: " + this.Y.getModeLabel(this.Y.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            pw.S().m(Integer.parseInt(this.Y.getValue()));
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Table screen", pw.S().X() ? "Open" : "Closed"});
      }

      @Override
      protected boolean screenAllowsBinds(Minecraft var1) {
         return pw.S().X();
      }
   }

   public static final class c extends to {
      private final com.larpclient.module.l A = this.addSetting(
         new com.larpclient.module.l("Winner", "Which item is shown as the winning one", "First", "First", "Second", "Middle")
      );
      private final com.larpclient.module.g Q = this.addSetting(
         new com.larpclient.module.g(
               "Slot Targets", "Nine digits, one per dispenser slot from top left: 1 first item, 2 second item, 3 middle item", "112132122"
            )
            .F("112132122")
      );

      @Override
      public String setupSummary() {
         t5 var1 = t5.O();
         String var2 = this.Q.getValue() == null ? "" : this.Q.getValue().trim();
         String var3;
         if (r(var2)) {
            var3 = "Slot targets: " + var2.substring(0, 3) + " / " + var2.substring(3, 6) + " / " + var2.substring(6);
         } else {
            int[] var4 = var1.E();
            StringBuilder var5 = new StringBuilder();

            for (int var6 = 0; var6 < 9; var6++) {
               var5.append(var6 > 0 && var6 % 3 == 0 ? " / " : "").append(var4[var6]);
            }

            var3 = "Slot Targets needs exactly 9 digits, each 1, 2 or 3. Still using " + var5;
         }

         return super.setupSummary() + "\nFirst: " + j(var1.F) + ", second: " + j(var1.m) + ", middle: " + j(var1.X) + "\n" + var3;
      }

      @Override
      public String getDisplayName() {
         return "45/45/10";
      }

      @Override
      public List<String> getSearchAliases() {
         return List.of("45/45/10", "forty five");
      }

      private static boolean r(String var0) {
         return var0.matches("[123]{9}");
      }

      public c() {
         super("45-45-10", "Shows a chosen item of the 45/45/10 table as the winner, visually only", sg.i.D);
         this.L("Side Bind", "Cycle first, second and middle winner", this::u);
         this.L("From Hand Bind", "Store the item in your hand as the item of the current Winner", () -> this.q());
         this.C("Dispenser Bind", gj.d.l);
         this.Z("Select table", gj.d.l);
         this.M();
         this.V();
         this.N();
         this.k("Select first item", "first (45%) item", var0 -> true, "Pick an item", var1 -> {
            t5.O().F = var1.getItem();
            this.G().Q();
            return "45/45/10 first item: " + var1.getHoverName().getString();
         }, () -> j(t5.O().F));
         this.k("Select second item", "second (45%) item", var0 -> true, "Pick an item", var1 -> {
            t5.O().m = var1.getItem();
            this.G().Q();
            return "45/45/10 second item: " + var1.getHoverName().getString();
         }, () -> j(t5.O().m));
         this.k("Select middle item", "middle (10%) item", var0 -> true, "Pick an item", var1 -> {
            t5.O().X = var1.getItem();
            this.G().Q();
            return "45/45/10 middle item: " + var1.getHoverName().getString();
         }, () -> j(t5.O().X));
         this.h();
         this.A.o(var1 -> this.applySettings());
         this.Q.o(var1 -> this.applySettings());
      }

      private void q() {
         sg.f var1 = this.G().J(sg.i.D);
         c(var1.f);
      }

      private void u() {
         this.A.setValue(this.A.O("First") ? "Second" : (this.A.O("Second") ? "Middle" : "First"));
         c("45/45/10 winner: " + this.A.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            t5 var1 = t5.O();

            var1.b = switch ((String)this.A.getValue()) {
               case "Second" -> t5.o.E;
               case "Middle" -> t5.o.B;
               default -> t5.o.i;
            };
            String var5 = this.Q.getValue() == null ? "" : this.Q.getValue().trim();
            if (r(var5)) {
               int[] var6 = new int[9];

               for (int var4 = 0; var4 < 9; var4++) {
                  var6[var4] = var5.charAt(var4) - '0';
               }

               var1.A(var6);
            }

            this.G().Q();
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         t5 var2 = t5.O();

         Item var3 = switch (var2.b) {
            case i -> var2.F;
            case E -> var2.m;
            case B -> var2.X;
         };
         var1.add(new String[]{"Winner item", j(var3)});
         if (!r(this.Q.getValue() == null ? "" : this.Q.getValue().trim())) {
            var1.add(new String[]{"Slot Targets", "Invalid, see settings"});
         }
      }
   }

   public static final class g extends to {
      private final com.larpclient.module.l a = this.addSetting(
         new com.larpclient.module.l(
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

      public g() {
         super("Mexican Jack", "Counts both hands and prepares the dealer's draws", sg.i.A);
         this.a.h("Drawing");
         this.L("Side Bind", "Switch between player and dealer drawing", this::L);
         this.C("Dispenser Bind", gj.d.q);
         this.Z("Select table", gj.d.q);
         this.M();
         this.V();
         this.N();
         this.h();
         this.a.o(var1 -> this.applySettings());
      }

      private void L() {
         this.a.setValue(this.a.O("Player") ? "Dealer" : "Player");
         c("Mexican Jack: " + this.a.getModeLabel(this.a.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            kr.N().S(this.a.getValue().toLowerCase(Locale.ROOT), w0.l());
         }
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         kr var2 = kr.N();
         var1.add(new String[]{"Player", var2.k()});
         var1.add(new String[]{"Dealer", var2.T()});
         int var3 = var2.S();
         var1.add(new String[]{"Next card", var3 < 1 ? "none" : Integer.toString(var3)});
         int var4 = w0.l().countTemplates();
         var1.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
      }
   }

   public static final class h extends to {
      private final com.larpclient.module.l d = this.addSetting(
         new com.larpclient.module.l("Parity", "Which parity is shown as the result", "Odd", "Odd", "Even")
      );

      public h() {
         super("Even or Odd", "Swaps the dealt map card for one of the chosen parity", sg.i.n);
         this.L("Side Bind", "Switch between odd and even", this::o);
         this.C("Dispenser Bind", gj.d.b);
         this.Z("Select table", gj.d.b);
         this.M();
         this.V();
         this.N();
         this.h();
         this.d.o(var1 -> this.applySettings());
      }

      private void o() {
         this.d.setValue(this.d.O("Odd") ? "Even" : "Odd");
         c("Even or Odd: " + this.d.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            w3 var1 = w3.M();
            var1.s(this.d.O("Even") ? 1 : 0);
            var1.clearQueue();
            this.G().Q();
         }
      }

      @Override
      protected String targetName() {
         return "Result";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         w3 var2 = w3.M();
         var1.add(new String[]{"Next card", var2.pendingLabel()});
         int var3 = var2.countTemplates();
         var1.add(new String[]{"Cards learned", var3 == 0 ? "None, open the table once" : Integer.toString(var3)});
      }
   }

   public static final class k extends to {
      @Override
      public String setupSummary() {
         int var1 = cc.z().W();
         return var1 < 0 ? "No lure 3 rod in your inventory" : "Rig rod in inventory slot " + (var1 + 1);
      }

      public k() {
         super("Fishing Game", "Silently casts a lure 3 rod from your inventory when you use any rod", sg.i.H);
         this.L("Hide Rod Bind", "Hide or show the rig rod in your inventory display", () -> {
            cc.z().q();
            c("Fishing Game: rod " + (cc.z().Y ? "hidden" : "visible"));
         });
         this.c("Hide or show rig rod", () -> {
            cc.z().q();
            c("Fishing Game: rod " + (cc.z().Y ? "hidden" : "visible"));
         }, () -> cc.z().Y ? "Rig rod hidden: press to show" : "Hide rig rod");
         this.I("Open game log", com.larpclient.module.z::E);
         this.V();
         this.N();
      }

      @Override
      public void onEnable() {
         super.onEnable();
         cc.z().F(true);
      }

      @Override
      protected void releaseExtra() {
         cc.z().F(false);
      }

      @Override
      protected void applySettings() {
      }

      @Override
      public String getStatus() {
         return cc.z().y ? "Rig on" : "Rig paused";
      }

      @Override
      public String setupHint() {
         return "Keep a lure 3 rod anywhere in your inventory, then cast with any rod.";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         cc var2 = cc.z();
         int var3 = var2.W();
         var1.add(new String[]{"Rig", var2.y ? "On" : "Paused"});
         var1.add(new String[]{"Lure 3 rod", var3 < 0 ? "Not found" : "Slot " + (var3 + 1)});
         var1.add(new String[]{"Rod display", var2.Y ? "Hidden" : "Visible"});
      }
   }

   public static final class l extends to {
      private final com.larpclient.module.l H = this.addSetting(
         new com.larpclient.module.l("Target", "Whether the shot is shown as a win or a loss", "Win", "Win", "Lose")
      );

      @Override
      public String setupSummary() {
         return super.setupSummary() + "\nX map: " + (bs.R().P() ? "selected" : "none");
      }

      public l() {
         super("Arrow Game", "Shows the arrow dispenser and item frames for a win or a loss, visually only", sg.i.L);
         this.L("Side Bind", "Switch between win and lose", this::c);
         this.L("Note Block Bind", "Look at a note block and press to bind it as the fake press trigger", this::m);
         this.L("X Map Bind", "Hold the filled map used as the X mark and press to store it", () -> {
            bs.R().i();
            c("Arrow Game: X map stored");
         });
         this.L("Clear X Map Bind", "Forget the stored X map", () -> {
            bs.R().A();
            c("Arrow Game: X map cleared");
         });
         this.C("Dispenser Bind", gj.d.g);
         this.Z("Select table", gj.d.g);
         this.M();
         this.V();
         this.N();
         this.k(
            "Select X map",
            "filled map used as the X mark",
            var0 -> var0.is(Items.FILLED_MAP) && var0.get(DataComponents.MAP_ID) != null,
            "Pick a filled map",
            var0 -> {
               int var1 = ((MapId)var0.get(DataComponents.MAP_ID)).id();
               com.larpclient.b.m().A(var1);
               return "Arrow Game X map: map #" + var1;
            },
            () -> bs.R().P() ? "" : null
         );
         this.c(
            "Select note block",
            () -> com.larpclient.module.w.r(
               "Right-click the note block that fires the shot (Esc cancels)",
               var0 -> var0.getBlock() instanceof NoteBlock,
               "That is not a note block",
               var0 -> {
                  bs.R().i(Minecraft.getInstance(), var0);
                  return "Arrow Game note block selected";
               }
            ),
            () -> {
               int var0 = com.larpclient.b.m().l();
               return var0 == 0 ? "Select note block" : (var0 == 1 ? "Note block selected" : var0 + " note blocks selected");
            }
         );
         this.c("Clear X map", () -> {
            bs.R().A();
            c("Arrow Game: X map cleared");
         }, () -> bs.R().P() ? "Clear X map" : "No X map to clear");
         this.h();
         this.H.o(var1 -> this.applySettings());
      }

      private void m() {
         Minecraft var1 = Minecraft.getInstance();
         if (var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var2.getType() == Type.BLOCK
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof NoteBlock) {
            bs.R().i(var1, var2.getBlockPos());
            c("Arrow Game: note block bound");
         } else {
            c("Look at a note block first");
         }
      }

      private void c() {
         this.H.setValue(this.H.O("Win") ? "Lose" : "Win");
         c("Arrow Game: " + this.H.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            bs.R().H(this.H.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         int var2 = com.larpclient.b.m().l();
         var1.add(new String[]{"Note block", var2 == 0 ? "Not selected" : (var2 == 1 ? "Selected" : var2 + " selected")});
         var1.add(new String[]{"X map", bs.R().P() ? "Selected" : "Not selected"});
      }
   }

   public static final class m extends to {
      private final com.larpclient.module.l n = this.addSetting(
         new com.larpclient.module.l(
            "Target", "Which hand the automatic plan makes win. Off leaves the draws alone unless you queue a preset", "Player", "Player", "Banker", "Off"
         ) {
            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Off") ? "Off (presets only)" : var1;
            }
         }
      );
      private final com.larpclient.module.g[] d = new com.larpclient.module.g[5];
      private final v[] o = new v[5];

      public m() {
         super("Baccarat", "Counts the hands and queues card values for the next draws", sg.i.O);
         this.L("Side Bind", "Switch between player and banker", this::B);
         this.C("Player Dispenser Bind", gj.d.x);
         this.Z("Select player table", gj.d.x);
         this.C("Banker Dispenser Bind", gj.d.c);
         this.Z("Select banker table", gj.d.c);
         this.M();

         for (int var1 = 0; var1 < 5; var1++) {
            int var2 = var1;
            this.d[var1] = this.addSetting(
               new com.larpclient.module.g("Preset " + (var1 + 1), "Up to three card values 1 to 9 separated by spaces, queued by the preset bind", "")
            );
            this.d[var1].o(var1x -> this.applySettings());
            this.o[var1] = this.L("Preset " + (var1 + 1) + " Bind", "Queue the values in Preset " + (var1 + 1), () -> this.I(var2));
         }

         this.V();
         this.N();
         this.h();
         this.n.o(var1x -> this.applySettings());
      }

      private static int[] v(String var0) {
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

      private void I(int var1) {
         int[] var2 = v(this.d[var1].getValue());
         if (var2.length == 0) {
            c("Baccarat preset " + (var1 + 1) + " is empty");
         } else if (wk.m().isSwapBusy()) {
            c("Baccarat preset " + (var1 + 1) + " not queued: a card is being swapped, press it again after this draw");
         } else {
            if (this.G().M(var1, var2)) {
               c("Baccarat preset " + (var1 + 1) + " queued for the next draws: " + J(var2));
            } else {
               c("Baccarat preset " + (var1 + 1) + " not queued: open a Baccarat table once so the cards are learned");
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

      private static boolean P(String var0) {
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
            String var3 = this.d[var2].getValue();
            if (var3 != null && !var3.isBlank()) {
               int[] var4 = v(var3);
               var1.append("\nPreset ")
                  .append(var2 + 1)
                  .append(": ")
                  .append(var4.length == 0 ? "no valid values" : J(var4))
                  .append(P(var3) ? "" : " (only values 1 to 9, at most three, are used)");
            }
         }

         return var1.toString();
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         m9 var2 = m9.O();
         int var3 = var2.u() > 0 ? 1 : 0;
         var1.add(new String[]{"Player", var3 != 0 ? Integer.toString(var2.H()) : "-"});
         var1.add(new String[]{"Banker", var3 != 0 ? Integer.toString(var2.x()) : "-"});
         String var4 = var2.n();
         var1.add(var4.isEmpty() ? new String[]{"Next card", wk.m().pendingLabel()} : new String[]{"Preset queue", var4});
         int var5 = wk.m().countTemplates();
         var1.add(new String[]{"Cards learned", var5 == 0 ? "None, open a table once" : Integer.toString(var5)});
      }

      private void B() {
         this.n.setValue(this.n.O("Player") ? "Banker" : "Player");
         c("Baccarat: " + this.n.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            m9.O().q(this.n.getValue().toLowerCase(Locale.ROOT), wk.m());
            int[] var1 = new int[15];
            Arrays.fill(var1, -1);

            for (int var2 = 0; var2 < 5; var2++) {
               int[] var3 = v(this.d[var2].getValue());
               System.arraycopy(var3, 0, var1, var2 * 3, var3.length);
            }

            this.G().N(var1);
         }
      }
   }

   public static final class o extends n.x {
      public o() {
         super("Horse Racing", "Shows the chosen horse armor as the winner", sg.i.I, cy.Z, 3, gj.d.T);
      }
   }

   public static final class p extends to {
      private final com.larpclient.module.l G = this.addSetting(
         new com.larpclient.module.l("Target", "Whether the next card is made to read higher or lower", "Higher", "Higher", "Lower")
      );

      public p() {
         super("Higher or Lower", "Prepares the next card to read higher or lower than the reference", sg.i.h);
         this.L("Side Bind", "Switch between higher and lower", this::c);
         this.C("Dispenser Bind", gj.d.Z);
         this.Z("Select table", gj.d.Z);
         this.M();
         this.V();
         this.N();
         this.h();
         this.G.o(var1 -> this.applySettings());
      }

      private void c() {
         this.G.setValue(this.G.O("Higher") ? "Lower" : "Higher");
         c("Higher or Lower: " + this.G.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            wc.s().d(this.G.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         wc var2 = wc.s();
         int var3 = var2.o();
         var1.add(new String[]{"Shown card", var3 < 1 ? "-" : Integer.toString(var3)});
         var1.add(new String[]{"Next card", var2.pendingLabel()});
         int var4 = var2.countTemplates();
         var1.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
      }
   }

   public static final class s extends to {
      private final e s = this.addSetting(new e("Hands", "Number of player hands at the table", 1.0, 1.0, 3.0, 1.0));
      private final com.larpclient.module.l G = this.addSetting(
         new com.larpclient.module.l(
            "Side",
            "Who takes the next cards. Player hands are counted; dealer draws are prepared to beat the best hand. Starts each round on the first hand",
            "Player",
            n.r
         ) {
            @Override
            public List<String> getVisibleModes() {
               int var1 = s.this.x();
               return Arrays.stream(n.r).filter(var1x -> var1x.equals("Dealer") || n.P(var1x) < var1).toList();
            }

            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Player") && s.this.x() > 1 ? "Hand 1" : var1;
            }
         }
      );

      public s() {
         super("Blackjack", "Counts every hand and prepares the dealer's draws to beat the best player hand", sg.i.c);
         this.L("Side Bind", "Move to the next hand, then the dealer", this::s);
         this.C("Dispenser Bind", gj.d.m);
         this.Z("Select table", gj.d.m);
         this.M();
         this.V();
         this.N();
         this.h();
         this.G.o(var1 -> this.applySettings());
         this.s.o(var1 -> {
            if (n.P(this.G.getValue()) >= this.x()) {
               this.G.setValue("Player");
            }

            this.applySettings();
         });
      }

      private int x() {
         return (int)Math.round(this.s.getValue());
      }

      private void s() {
         this.G.U();
         c("Blackjack: " + this.G.getModeLabel(this.G.getValue()) + " draws next");
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            s3 var1 = s3.S();
            var1.S(this.x());
            var1.G(n.P(this.G.getValue()));
         }
      }

      @Override
      protected void afterTick(Minecraft var1) {
         String var2 = n.s(s3.S().L());
         if (!this.G.O(var2) && this.G.acceptsValue(var2)) {
            this.G.setValue(var2);
         }
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         n.u(var1, this.x());
         var1.add(new String[]{"Dealer plan", s3.S().U()});
      }
   }

   public static final class t extends n.x {
      public t() {
         super("Armor Game", "Shows the chosen armor piece as the winner", sg.i.Q, cy.d, 4, gj.d.u);
      }
   }

   public static final class u extends to {
      @Override
      public String setupSummary() {
         pq var1 = pq.Q();
         return super.setupSummary() + "\nNormal: " + j(var1.c) + ", trigger: " + j(var1.Y) + (var1.a ? " (armed)" : "");
      }

      private static void P() {
         pq var0 = pq.Q();
         if (var0.a) {
            var0.a = (boolean)0;
         } else {
            var0.g();
         }

         c("Russian Roulette: " + (var0.a ? "armed, the next pull shows the trigger item" : "disarmed"));
      }

      @Override
      protected String targetName() {
         return "Next pull";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         pq var2 = pq.Q();
         var1.add(new String[]{"Normal item", j(var2.c)});
         var1.add(new String[]{"Trigger item", j(var2.Y)});
      }

      public u() {
         super("Russian Roulette", "Swaps the normal item for the trigger item when armed, visually only", sg.i.J);
         this.L("Arm Bind", "Arm or disarm the trigger item", n.u::P);
         this.L("From Hand Bind", "Store your hand item as the trigger item when armed, else the normal item", () -> c(this.G().J(sg.i.J).f));
         this.C("Dispenser Bind", gj.d.s);
         this.Z("Select table", gj.d.s);
         this.M();
         this.V();
         this.N();
         this.k("Select normal item", "normal item", var0 -> true, "Pick an item", var1 -> {
            pq.Q().c = var1.getItem();
            this.G().Q();
            return "Russian Roulette normal item: " + var1.getHoverName().getString();
         }, () -> j(pq.Q().c));
         this.k("Select trigger item", "trigger item", var0 -> true, "Pick an item", var1 -> {
            pq.Q().Y = var1.getItem();
            this.G().Q();
            return "Russian Roulette trigger item: " + var1.getHoverName().getString();
         }, () -> j(pq.Q().Y));
         this.c("Arm or disarm", () -> {
            if (this.isEnabled()) {
               P();
            } else {
               c("Enable Russian Roulette first");
            }
         }, () -> pq.Q().a ? "Armed: press to disarm" : "Arm trigger");
         this.h();
      }

      @Override
      protected void applySettings() {
      }
   }

   abstract static class x extends to {
      private final cy N;
      private final int M;
      private final com.larpclient.module.l X;

      x(String var1, String var2, sg.i var3, cy var4, int var5, gj.d var6) {
         super(var1, var2, var3);
         this.N = var4;
         this.M = var5;
         String[] var7 = new String[var5];

         for (int var8 = 0; var8 < var5; var8++) {
            var7[var8] = Integer.toString(var8 + 1);
         }

         this.X = this.addSetting(new com.larpclient.module.l("Winner", "Which piece is shown as the winner", "1", var7) {
            @Override
            public String getModeLabel(String var1) {
               return x.this.V(Integer.parseInt(var1));
            }
         });
         this.L("Side Bind", "Advance to the next winner", this::b);
         this.C("Dispenser Bind", var6);
         this.Z("Select table", var6);
         this.M();
         this.V();
         this.N();
         this.h();
         this.X.o(var1x -> this.applySettings());
      }

      private String V(int var1) {
         String var2 = this.N == cy.Z ? (Minecraft.getInstance() != null && Minecraft.getInstance().level != null ? this.N.i(var1) : "") : this.N.h(var1);
         return var2 != null && !var2.isBlank() ? var2 : (this.N == cy.Z ? "Horse " + var1 : Integer.toString(var1));
      }

      private void b() {
         this.X.setValue(Integer.toString(Integer.parseInt(this.X.getValue()) % this.M + 1));
         c(this.getName() + " winner: " + this.X.getModeLabel(this.X.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            this.N.f(Integer.parseInt(this.X.getValue()));
            this.N.G(true);
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Table", this.N.L()});
      }
   }

   public static final class z extends to {
      private final e E = this.addSetting(new e("Hands", "Number of player hands at the table", 1.0, 1.0, 3.0, 1.0));
      private final com.larpclient.module.l k = this.addSetting(
         new com.larpclient.module.l(
            "Draw For",
            "Auto prepares the player's safe draws at 12 to 16, then the dealer's losing draw once the player stands at 17. Player or Dealer keeps one side",
            "Auto",
            "Auto",
            "Player",
            "Dealer"
         )
      );

      public z() {
         super("Blackjack Force Only", "Prepares the dispenser for the draw you want; the server decides what drops", sg.i.g);
         this.z("Side Bind", "Cycle Auto, Player and Dealer", this::c, true);
         this.L("Import Hotbar Bind", "Read the card map templates from your hotbar", this::X);
         this.C("Dispenser Bind", gj.d.a);
         this.Z("Select table", gj.d.a);
         this.M();
         this.V();
         this.N();
         this.c("Import hotbar cards", this::X, () -> lh.I().V() ? "Hotbar cards imported" : "Import hotbar cards");
         this.h();
         this.k.o(var1 -> this.applySettings());
         this.E.o(var1 -> this.applySettings());
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         n.u(var1, (int)Math.round(this.E.getValue()));
         var1.add(new String[]{"Card values", lh.I().V() ? "From hotbar" : "From table slots"});
      }

      private void X() {
         String var1 = lh.I().W(Minecraft.getInstance());
         c(var1 == null ? "Blackjack Force Only: hotbar card maps imported" : "Hotbar import failed: " + var1);
         if (var1 == null) {
            this.G().Q();
         }
      }

      private void c() {
         this.k.U();
         c("Blackjack Force Only draws for: " + this.k.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            s3 var1 = s3.S();
            var1.S((int)Math.round(this.E.getValue()));
            var1.L(this.k.getValue());
         }
      }

      @Override
      public void onDisable() {
         super.onDisable();
         s3.S().L("auto");
      }

      @Override
      protected boolean screenAllowsBinds(Minecraft var1) {
         return lh.I().g(var1);
      }
   }
}
