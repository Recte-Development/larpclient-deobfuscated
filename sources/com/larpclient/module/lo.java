package com.larpclient.module;

import com.larpclient.eg;
import com.larpclient.f2;
import com.larpclient.f9;
import com.larpclient.hk;
import com.larpclient.jf;
import com.larpclient.js;
import com.larpclient.mm;
import com.larpclient.mz;
import com.larpclient.o7;
import com.larpclient.oi;
import com.larpclient.ro;
import com.larpclient.u3;
import com.larpclient.ue;
import com.larpclient.ug;
import com.larpclient.um;
import com.larpclient.uu;
import com.larpclient.xa;
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

public final class lo {
   private static final String[] I = new String[]{"Player", "Hand 2", "Hand 3", "Dealer"};

   private lo() {
   }

   private static String P(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1).toLowerCase(Locale.ROOT);
   }

   private static int i(String var0) {
      return switch (var0) {
         case "Hand 2" -> 1;
         case "Hand 3" -> 2;
         case "Dealer" -> -1;
         default -> 0;
      };
   }

   private static String v(int var0) {
      return var0 < 0 ? "Dealer" : (var0 == 1 ? "Hand 2" : (var0 == 2 ? "Hand 3" : "Player"));
   }

   private static void f(List<String[]> var0, int var1) {
      mz var2 = mz.F();

      for (int var3 = 0; var3 < var1; var3++) {
         var0.add(new String[]{var1 == 1 ? "Player" : "Hand " + (var3 + 1), var2.I(var3)});
      }

      var0.add(new String[]{"Dealer", var2.I(-1)});
      int var4 = ug.w().countTemplates();
      var0.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
   }

   public static final class f extends a {
      private final lp h = this.addSetting(new lp("Parity", "Which parity is shown as the result", "Odd", "Odd", "Even"));

      public f() {
         super("Even or Odd", "Swaps the dealt map card for one of the chosen parity", com.larpclient.l6.e.Q);
         this.B("Side Bind", "Switch between odd and even", this::D);
         this.d("Dispenser Bind", f9.b.T);
         this.D("Select table", f9.b.T);
         this.U();
         this.S();
         this.Z();
         this.S();
         this.h.W(var1 -> this.applySettings());
      }

      private void D() {
         this.h.setValue(this.h.i("Odd") ? "Even" : "Odd");
         d("Even or Odd: " + this.h.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            ue var1 = ue.V();
            var1.X(this.h.i("Even") ? 1 : 0);
            var1.clearQueue();
            this.o().h();
         }
      }

      @Override
      protected String targetName() {
         return "Result";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         ue var2 = ue.V();
         var1.add(new String[]{"Next card", var2.pendingLabel()});
         int var3 = var2.countTemplates();
         var1.add(new String[]{"Cards learned", var3 == 0 ? "None, open the table once" : Integer.toString(var3)});
      }
   }

   public static final class g extends a {
      private final lp K = this.addSetting(new lp("Target", "Whether the next card is made to read higher or lower", "Higher", "Higher", "Lower"));

      public g() {
         super("Higher or Lower", "Prepares the next card to read higher or lower than the reference", com.larpclient.l6.e.y);
         this.B("Side Bind", "Switch between higher and lower", this::H);
         this.d("Dispenser Bind", f9.b.B);
         this.D("Select table", f9.b.B);
         this.U();
         this.S();
         this.Z();
         this.S();
         this.K.W(var1 -> this.applySettings());
      }

      private void H() {
         this.K.setValue(this.K.i("Higher") ? "Lower" : "Higher");
         d("Higher or Lower: " + this.K.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            um.T().I(this.K.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         um var2 = um.T();
         int var3 = var2.U();
         var1.add(new String[]{"Shown card", var3 < 1 ? "-" : Integer.toString(var3)});
         var1.add(new String[]{"Next card", var2.pendingLabel()});
         int var4 = var2.countTemplates();
         var1.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
      }
   }

   public static final class h extends a {
      private final lc b = this.addSetting(new lc("Hands", "Number of player hands at the table", 1.0, 1.0, 3.0, 1.0));
      private final lp t = this.addSetting(
         new lp(
            "Side",
            "Who takes the next cards. Player hands are counted; dealer draws are prepared to beat the best hand. Starts each round on the first hand",
            "Player",
            lo.I
         ) {
            @Override
            public List<String> getVisibleModes() {
               int var1 = h.this.A();
               return Arrays.stream(lo.I).filter(var1x -> var1x.equals("Dealer") || lo.i(var1x) < var1).toList();
            }

            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Player") && h.this.A() > 1 ? "Hand 1" : var1;
            }
         }
      );

      public h() {
         super("Blackjack", "Counts every hand and prepares the dealer's draws to beat the best player hand", com.larpclient.l6.e.F);
         this.B("Side Bind", "Move to the next hand, then the dealer", this::K);
         this.d("Dispenser Bind", f9.b.E);
         this.D("Select table", f9.b.E);
         this.U();
         this.S();
         this.Z();
         this.S();
         this.t.W(var1 -> this.applySettings());
         this.b.W(var1 -> {
            if (lo.i(this.t.getValue()) >= this.A()) {
               this.t.setValue("Player");
            }

            this.applySettings();
         });
      }

      private int A() {
         return (int)Math.round(this.b.getValue());
      }

      private void K() {
         this.t.F();
         d("Blackjack: " + this.t.getModeLabel(this.t.getValue()) + " draws next");
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            mz var1 = mz.F();
            var1.t(this.A());
            var1.T(lo.i(this.t.getValue()));
         }
      }

      @Override
      protected void afterTick(Minecraft var1) {
         String var2 = lo.v(mz.F().Z());
         if (!this.t.i(var2) && this.t.acceptsValue(var2)) {
            this.t.setValue(var2);
         }
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         lo.f(var1, this.A());
         var1.add(new String[]{"Dealer plan", mz.F().K()});
      }
   }

   public static final class l extends a {
      private final lp k = this.addSetting(new lp("Target", "Whether the shot is shown as a win or a loss", "Win", "Win", "Lose"));

      @Override
      public String setupSummary() {
         return super.setupSummary() + "\nX map: " + (f2.f().a() ? "selected" : "none");
      }

      public l() {
         super("Arrow Game", "Shows the arrow dispenser and item frames for a win or a loss, visually only", com.larpclient.l6.e.e);
         this.B("Side Bind", "Switch between win and lose", this::a);
         this.B("Note Block Bind", "Look at a note block and press to bind it as the fake press trigger", this::f);
         this.B("X Map Bind", "Hold the filled map used as the X mark and press to store it", () -> {
            f2.f().s();
            d("Arrow Game: X map stored");
         });
         this.B("Clear X Map Bind", "Forget the stored X map", () -> {
            f2.f().u();
            d("Arrow Game: X map cleared");
         });
         this.d("Dispenser Bind", f9.b.F);
         this.D("Select table", f9.b.F);
         this.U();
         this.S();
         this.Z();
         this.D(
            "Select X map",
            "filled map used as the X mark",
            var0 -> var0.is(Items.FILLED_MAP) && var0.get(DataComponents.MAP_ID) != null,
            "Pick a filled map",
            var0 -> {
               int var1 = ((MapId)var0.get(DataComponents.MAP_ID)).id();
               o7.a().g(var1);
               return "Arrow Game X map: map #" + var1;
            },
            () -> f2.f().a() ? "" : null
         );
         this.n(
            "Select note block",
            () -> a_.B(
               "Right-click the note block that fires the shot (Esc cancels)",
               var0 -> var0.getBlock() instanceof NoteBlock,
               "That is not a note block",
               var0 -> {
                  f2.f().Z(Minecraft.getInstance(), var0);
                  return "Arrow Game note block selected";
               }
            ),
            () -> {
               int var0 = o7.a().U();
               return var0 == 0 ? "Select note block" : (var0 == 1 ? "Note block selected" : var0 + " note blocks selected");
            }
         );
         this.n("Clear X map", () -> {
            f2.f().u();
            d("Arrow Game: X map cleared");
         }, () -> f2.f().a() ? "Clear X map" : "No X map to clear");
         this.S();
         this.k.W(var1 -> this.applySettings());
      }

      private void f() {
         Minecraft var1 = Minecraft.getInstance();
         if (var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var2.getType() == Type.BLOCK
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof NoteBlock) {
            f2.f().Z(var1, var2.getBlockPos());
            d("Arrow Game: note block bound");
         } else {
            d("Look at a note block first");
         }
      }

      private void a() {
         this.k.setValue(this.k.i("Win") ? "Lose" : "Win");
         d("Arrow Game: " + this.k.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            f2.f().X(this.k.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         int var2 = o7.a().U();
         var1.add(new String[]{"Note block", var2 == 0 ? "Not selected" : (var2 == 1 ? "Selected" : var2 + " selected")});
         var1.add(new String[]{"X map", f2.f().a() ? "Selected" : "Not selected"});
      }
   }

   public static final class m extends a {
      private final lp v = this.addSetting(new lp("Winner", "Which item is shown as the winning one", "First", "First", "Second", "Middle"));
      private final li I = this.addSetting(
         new li("Slot Targets", "Nine digits, one per dispenser slot from top left: 1 first item, 2 second item, 3 middle item", "112132122").R("112132122")
      );

      @Override
      public String setupSummary() {
         xa var1 = xa.r();
         String var2 = this.I.getValue() == null ? "" : this.I.getValue().trim();
         String var3;
         if (o(var2)) {
            var3 = "Slot targets: " + var2.substring(0, 3) + " / " + var2.substring(3, 6) + " / " + var2.substring(6);
         } else {
            int[] var4 = var1.u();
            StringBuilder var5 = new StringBuilder();

            for (int var6 = 0; var6 < 9; var6++) {
               var5.append(var6 > 0 && var6 % 3 == 0 ? " / " : "").append(var4[var6]);
            }

            var3 = "Slot Targets needs exactly 9 digits, each 1, 2 or 3. Still using " + var5;
         }

         return super.setupSummary() + "\nFirst: " + w(var1.c) + ", second: " + w(var1.N) + ", middle: " + w(var1.b) + "\n" + var3;
      }

      @Override
      public String getDisplayName() {
         return "45/45/10";
      }

      @Override
      public List<String> getSearchAliases() {
         return List.of("45/45/10", "forty five");
      }

      private static boolean o(String var0) {
         return var0.matches("[123]{9}");
      }

      public m() {
         super("45-45-10", "Shows a chosen item of the 45/45/10 table as the winner, visually only", com.larpclient.l6.e.E);
         this.B("Side Bind", "Cycle first, second and middle winner", this::p);
         this.B("From Hand Bind", "Store the item in your hand as the item of the current Winner", () -> this.i());
         this.d("Dispenser Bind", f9.b.i);
         this.D("Select table", f9.b.i);
         this.U();
         this.S();
         this.Z();
         this.D("Select first item", "first (45%) item", var0 -> true, "Pick an item", var1 -> {
            xa.r().c = var1.getItem();
            this.o().h();
            return "45/45/10 first item: " + var1.getHoverName().getString();
         }, () -> w(xa.r().c));
         this.D("Select second item", "second (45%) item", var0 -> true, "Pick an item", var1 -> {
            xa.r().N = var1.getItem();
            this.o().h();
            return "45/45/10 second item: " + var1.getHoverName().getString();
         }, () -> w(xa.r().N));
         this.D("Select middle item", "middle (10%) item", var0 -> true, "Pick an item", var1 -> {
            xa.r().b = var1.getItem();
            this.o().h();
            return "45/45/10 middle item: " + var1.getHoverName().getString();
         }, () -> w(xa.r().b));
         this.S();
         this.v.W(var1 -> this.applySettings());
         this.I.W(var1 -> this.applySettings());
      }

      private void i() {
         com.larpclient.l6.x var1 = this.o().u(com.larpclient.l6.e.E);
         d(var1.S);
      }

      private void p() {
         this.v.setValue(this.v.i("First") ? "Second" : (this.v.i("Second") ? "Middle" : "First"));
         d("45/45/10 winner: " + this.v.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            xa var1 = xa.r();

            var1.Y = switch ((String)this.v.getValue()) {
               case "Second" -> xa.i.Y;
               case "Middle" -> xa.i.J;
               default -> xa.i.y;
            };
            String var5 = this.I.getValue() == null ? "" : this.I.getValue().trim();
            if (o(var5)) {
               int[] var6 = new int[9];

               for (int var4 = 0; var4 < 9; var4++) {
                  var6[var4] = var5.charAt(var4) - '0';
               }

               var1.Z(var6);
            }

            this.o().h();
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         xa var2 = xa.r();

         Item var3 = switch (var2.Y) {
            case y -> var2.c;
            case Y -> var2.N;
            case J -> var2.b;
         };
         var1.add(new String[]{"Winner item", w(var3)});
         if (!o(this.I.getValue() == null ? "" : this.I.getValue().trim())) {
            var1.add(new String[]{"Slot Targets", "Invalid, see settings"});
         }
      }
   }

   public static final class n extends a {
      private final lp f = this.addSetting(
         new lp("Target", "Which hand the automatic plan makes win. Off leaves the draws alone unless you queue a preset", "Player", "Player", "Banker", "Off") {
            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Off") ? "Off (presets only)" : var1;
            }
         }
      );
      private final li[] Y = new li[5];
      private final la[] g = new la[5];

      public n() {
         super("Baccarat", "Counts the hands and queues card values for the next draws", com.larpclient.l6.e.h);
         this.B("Side Bind", "Switch between player and banker", this::n);
         this.d("Player Dispenser Bind", f9.b.L);
         this.D("Select player table", f9.b.L);
         this.d("Banker Dispenser Bind", f9.b.z);
         this.D("Select banker table", f9.b.z);
         this.U();

         for (int var1 = 0; var1 < 5; var1++) {
            int var2 = var1;
            this.Y[var1] = this.addSetting(new li("Preset " + (var1 + 1), "Up to three card values 1 to 9 separated by spaces, queued by the preset bind", ""));
            this.Y[var1].W(var1x -> this.applySettings());
            this.g[var1] = this.B("Preset " + (var1 + 1) + " Bind", "Queue the values in Preset " + (var1 + 1), () -> this.v(var2));
         }

         this.S();
         this.Z();
         this.S();
         this.f.W(var1x -> this.applySettings());
      }

      private static int[] C(String var0) {
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

      private void v(int var1) {
         int[] var2 = C(this.Y[var1].getValue());
         if (var2.length == 0) {
            d("Baccarat preset " + (var1 + 1) + " is empty");
         } else if (u3.H().isSwapBusy()) {
            d("Baccarat preset " + (var1 + 1) + " not queued: a card is being swapped, press it again after this draw");
         } else {
            if (this.o().q(var1, var2)) {
               d("Baccarat preset " + (var1 + 1) + " queued for the next draws: " + h(var2));
            } else {
               d("Baccarat preset " + (var1 + 1) + " not queued: open a Baccarat table once so the cards are learned");
            }
         }
      }

      private static String h(int[] var0) {
         StringBuilder var1 = new StringBuilder();

         for (int var5 : var0) {
            var1.append(var1.isEmpty() ? "" : " ").append(var5);
         }

         return var1.toString();
      }

      private static boolean y(String var0) {
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
            String var3 = this.Y[var2].getValue();
            if (var3 != null && !var3.isBlank()) {
               int[] var4 = C(var3);
               var1.append("\nPreset ")
                  .append(var2 + 1)
                  .append(": ")
                  .append(var4.length == 0 ? "no valid values" : h(var4))
                  .append(y(var3) ? "" : " (only values 1 to 9, at most three, are used)");
            }
         }

         return var1.toString();
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         jf var2 = jf.u();
         int var3 = var2.H() > 0 ? 1 : 0;
         var1.add(new String[]{"Player", var3 != 0 ? Integer.toString(var2.f()) : "-"});
         var1.add(new String[]{"Banker", var3 != 0 ? Integer.toString(var2.Z()) : "-"});
         String var4 = var2.q();
         var1.add(var4.isEmpty() ? new String[]{"Next card", u3.H().pendingLabel()} : new String[]{"Preset queue", var4});
         int var5 = u3.H().countTemplates();
         var1.add(new String[]{"Cards learned", var5 == 0 ? "None, open a table once" : Integer.toString(var5)});
      }

      private void n() {
         this.f.setValue(this.f.i("Player") ? "Banker" : "Player");
         d("Baccarat: " + this.f.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            jf.u().Y(this.f.getValue().toLowerCase(Locale.ROOT), u3.H());
            int[] var1 = new int[15];
            Arrays.fill(var1, -1);

            for (int var2 = 0; var2 < 5; var2++) {
               int[] var3 = C(this.Y[var2].getValue());
               System.arraycopy(var3, 0, var1, var2 * 3, var3.length);
            }

            this.o().L(var1);
         }
      }
   }

   public static final class p extends a {
      private final lp H = this.addSetting(new lp("Winner", "Which of the two items is shown as the winner", "1", "1", "2") {
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

      public p() {
         super("50-50", "Shows one of the two 50/50 items as the winner and keeps the real pull intact", com.larpclient.l6.e.k);
         this.C("Side Bind", "Switch the winning item", this::x, true);
         this.d("Dispenser Bind", f9.b.j);
         this.D("Select table", f9.b.j);
         this.U();
         this.S();
         this.Z();
         this.S();
         this.H.W(var1 -> this.applySettings());
      }

      private void x() {
         this.H.setValue(this.H.i("1") ? "2" : "1");
         d("50/50 winner: " + this.H.getModeLabel(this.H.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            ro.H().L(Integer.parseInt(this.H.getValue()));
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Table screen", ro.H().h() ? "Open" : "Closed"});
      }

      @Override
      protected boolean screenAllowsBinds(Minecraft var1) {
         return ro.H().h();
      }
   }

   public static final class q extends a {
      private final lp m = this.addSetting(new lp("Target", "Which table's drawn paper is shown as the higher one", "Host", "Host", "Viewer"));
      private final ln H = this.addSetting(new ln("Universal Items", "Learn one item set and use it for both tables", true));
      private final li p = this.addSetting(new li("Host Suffix", "Name suffix that marks host table items", "Host"));
      private final li q = this.addSetting(new li("Viewer Suffix", "Name suffix that marks viewer table items", "Viewer"));
      private boolean V;

      public q() {
         super("Paper", "Shows the chosen table's paper draw as the higher one, visually only", com.larpclient.l6.e.H);
         this.B("Side Bind", "Switch the target between host and viewer", this::q);
         this.d("Host Dispenser Bind", f9.b.y);
         this.D("Select host table", f9.b.y);
         this.d("Viewer Dispenser Bind", f9.b.C);
         this.D("Select viewer table", f9.b.C);
         this.U();
         this.S();
         this.Z();
         this.S();
         this.m.W(var1 -> this.applySettings());
         this.H.W(var1 -> {
            if (!this.V) {
               this.applySettings();
            }
         });
         this.p.W(var1 -> this.applySettings());
         this.q.W(var1 -> this.applySettings());
      }

      private void q() {
         this.m.setValue(this.m.i("Host") ? "Viewer" : "Host");
         d("Paper: " + this.m.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            oi var1 = oi.A();
            var1.e(this.H.getValue());
            var1.r(this.p.getValue());
            var1.L(this.q.getValue());
            var1.W(this.m.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void afterTick(Minecraft var1) {
         boolean var2 = oi.A().B();
         if (this.H.getValue() != var2) {
            this.V = (boolean)1;

            try {
               this.H.setValue(var2);
            } finally {
               this.V = (boolean)0;
            }

            if (!var2) {
               d("Paper: learned separate host and viewer items, Universal Items is now off");
            }
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Items", this.H.getValue() ? "Same for both tables" : "Per table"});
      }
   }

   public static final class s extends lo.z {
      public s() {
         super("Horse Racing", "Shows the chosen horse armor as the winner", com.larpclient.l6.e.s, hk.H, 3, f9.b.g);
      }
   }

   public static final class t extends a {
      private final lc h = this.addSetting(new lc("Hands", "Number of player hands at the table", 1.0, 1.0, 3.0, 1.0));
      private final lp q = this.addSetting(
         new lp(
            "Draw For",
            "Auto prepares the player's safe draws at 12 to 16, then the dealer's losing draw once the player stands at 17. Player or Dealer keeps one side",
            "Auto",
            "Auto",
            "Player",
            "Dealer"
         )
      );

      public t() {
         super("Blackjack Force Only", "Prepares the dispenser for the draw you want; the server decides what drops", com.larpclient.l6.e.L);
         this.C("Side Bind", "Cycle Auto, Player and Dealer", this::C, true);
         this.B("Import Hotbar Bind", "Read the card map templates from your hotbar", this::o);
         this.d("Dispenser Bind", f9.b.P);
         this.D("Select table", f9.b.P);
         this.U();
         this.S();
         this.Z();
         this.n("Import hotbar cards", this::o, () -> mm.W().S() ? "Hotbar cards imported" : "Import hotbar cards");
         this.S();
         this.q.W(var1 -> this.applySettings());
         this.h.W(var1 -> this.applySettings());
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         lo.f(var1, (int)Math.round(this.h.getValue()));
         var1.add(new String[]{"Card values", mm.W().S() ? "From hotbar" : "From table slots"});
      }

      private void o() {
         String var1 = mm.W().t(Minecraft.getInstance());
         d(var1 == null ? "Blackjack Force Only: hotbar card maps imported" : "Hotbar import failed: " + var1);
         if (var1 == null) {
            this.o().h();
         }
      }

      private void C() {
         this.q.F();
         d("Blackjack Force Only draws for: " + this.q.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            mz var1 = mz.F();
            var1.t((int)Math.round(this.h.getValue()));
            var1.n(this.q.getValue());
         }
      }

      @Override
      public void onDisable() {
         super.onDisable();
         mz.F().n("auto");
      }

      @Override
      protected boolean screenAllowsBinds(Minecraft var1) {
         return mm.W().T(var1);
      }
   }

   public static final class u extends lo.z {
      public u() {
         super("Armor Game", "Shows the chosen armor piece as the winner", com.larpclient.l6.e.w, hk.T, 4, f9.b.X);
      }
   }

   public static final class v extends a {
      private final lp m = this.addSetting(
         new lp(
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

      public v() {
         super("Mexican Jack", "Counts both hands and prepares the dealer's draws", com.larpclient.l6.e.W);
         this.m.b("Drawing");
         this.B("Side Bind", "Switch between player and dealer drawing", this::Q);
         this.d("Dispenser Bind", f9.b.c);
         this.D("Select table", f9.b.c);
         this.U();
         this.S();
         this.Z();
         this.S();
         this.m.W(var1 -> this.applySettings());
      }

      private void Q() {
         this.m.setValue(this.m.i("Player") ? "Dealer" : "Player");
         d("Mexican Jack: " + this.m.getModeLabel(this.m.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            com.larpclient.li.D().p(this.m.getValue().toLowerCase(Locale.ROOT), uu.M());
         }
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         com.larpclient.li var2 = com.larpclient.li.D();
         var1.add(new String[]{"Player", var2.v()});
         var1.add(new String[]{"Dealer", var2.G()});
         int var3 = var2.D();
         var1.add(new String[]{"Next card", var3 < 1 ? "none" : Integer.toString(var3)});
         int var4 = uu.M().countTemplates();
         var1.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
      }
   }

   public static final class w extends a {
      @Override
      public String setupSummary() {
         int var1 = eg.y().g();
         return var1 < 0 ? "No lure 3 rod in your inventory" : "Rig rod in inventory slot " + (var1 + 1);
      }

      public w() {
         super("Fishing Game", "Silently casts a lure 3 rod from your inventory when you use any rod", com.larpclient.l6.e.K);
         this.B("Hide Rod Bind", "Hide or show the rig rod in your inventory display", () -> {
            eg.y().f();
            d("Fishing Game: rod " + (eg.y().K ? "hidden" : "visible"));
         });
         this.n("Hide or show rig rod", () -> {
            eg.y().f();
            d("Fishing Game: rod " + (eg.y().K ? "hidden" : "visible"));
         }, () -> eg.y().K ? "Rig rod hidden: press to show" : "Hide rig rod");
         this.I("Open game log", l4::x);
         this.S();
         this.Z();
      }

      @Override
      public void onEnable() {
         super.onEnable();
         eg.y().C(true);
      }

      @Override
      protected void releaseExtra() {
         eg.y().C(false);
      }

      @Override
      protected void applySettings() {
      }

      @Override
      public String getStatus() {
         return eg.y().n ? "Rig on" : "Rig paused";
      }

      @Override
      public String setupHint() {
         return "Keep a lure 3 rod anywhere in your inventory, then cast with any rod.";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         eg var2 = eg.y();
         int var3 = var2.g();
         var1.add(new String[]{"Rig", var2.n ? "On" : "Paused"});
         var1.add(new String[]{"Lure 3 rod", var3 < 0 ? "Not found" : "Slot " + (var3 + 1)});
         var1.add(new String[]{"Rod display", var2.K ? "Hidden" : "Visible"});
      }
   }

   public static final class x extends a {
      @Override
      public String setupSummary() {
         js var1 = js.A();
         return super.setupSummary() + "\nNormal: " + w(var1.Z) + ", trigger: " + w(var1.E) + (var1.o ? " (armed)" : "");
      }

      private static void P() {
         js var0 = js.A();
         if (var0.o) {
            var0.o = (boolean)0;
         } else {
            var0.C();
         }

         d("Russian Roulette: " + (var0.o ? "armed, the next pull shows the trigger item" : "disarmed"));
      }

      @Override
      protected String targetName() {
         return "Next pull";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         js var2 = js.A();
         var1.add(new String[]{"Normal item", w(var2.Z)});
         var1.add(new String[]{"Trigger item", w(var2.E)});
      }

      public x() {
         super("Russian Roulette", "Swaps the normal item for the trigger item when armed, visually only", com.larpclient.l6.e.v);
         this.B("Arm Bind", "Arm or disarm the trigger item", lo.x::P);
         this.B("From Hand Bind", "Store your hand item as the trigger item when armed, else the normal item", () -> d(this.o().u(com.larpclient.l6.e.v).S));
         this.d("Dispenser Bind", f9.b.u);
         this.D("Select table", f9.b.u);
         this.U();
         this.S();
         this.Z();
         this.D("Select normal item", "normal item", var0 -> true, "Pick an item", var1 -> {
            js.A().Z = var1.getItem();
            this.o().h();
            return "Russian Roulette normal item: " + var1.getHoverName().getString();
         }, () -> w(js.A().Z));
         this.D("Select trigger item", "trigger item", var0 -> true, "Pick an item", var1 -> {
            js.A().E = var1.getItem();
            this.o().h();
            return "Russian Roulette trigger item: " + var1.getHoverName().getString();
         }, () -> w(js.A().E));
         this.n("Arm or disarm", () -> {
            if (this.isEnabled()) {
               P();
            } else {
               d("Enable Russian Roulette first");
            }
         }, () -> js.A().o ? "Armed: press to disarm" : "Arm trigger");
         this.S();
      }

      @Override
      protected void applySettings() {
      }
   }

   abstract static class z extends a {
      private final hk i;
      private final int w;
      private final lp f;

      z(String var1, String var2, com.larpclient.l6.e var3, hk var4, int var5, f9.b var6) {
         super(var1, var2, var3);
         this.i = var4;
         this.w = var5;
         String[] var7 = new String[var5];

         for (int var8 = 0; var8 < var5; var8++) {
            var7[var8] = Integer.toString(var8 + 1);
         }

         this.f = this.addSetting(new lp("Winner", "Which piece is shown as the winner", "1", var7) {
            @Override
            public String getModeLabel(String var1) {
               return z.this.k(Integer.parseInt(var1));
            }
         });
         this.B("Side Bind", "Advance to the next winner", this::n);
         this.d("Dispenser Bind", var6);
         this.D("Select table", var6);
         this.U();
         this.S();
         this.Z();
         this.S();
         this.f.W(var1x -> this.applySettings());
      }

      private String k(int var1) {
         String var2 = this.i == hk.H ? (Minecraft.getInstance() != null && Minecraft.getInstance().level != null ? this.i.Z(var1) : "") : this.i.X(var1);
         return var2 != null && !var2.isBlank() ? var2 : (this.i == hk.H ? "Horse " + var1 : Integer.toString(var1));
      }

      private void n() {
         this.f.setValue(Integer.toString(Integer.parseInt(this.f.getValue()) % this.w + 1));
         d(this.getName() + " winner: " + this.f.getModeLabel(this.f.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            this.i.X(Integer.parseInt(this.f.getValue()));
            this.i.U(true);
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Table", this.i.J()});
      }
   }
}
