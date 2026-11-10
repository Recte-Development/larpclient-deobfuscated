package com.larpclient.module;

import com.larpclient.hu;
import com.larpclient.q;
import com.larpclient.q8;
import com.larpclient.s5;
import com.larpclient.yq;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2315;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_310;

public abstract class r extends Module {
   private static final List<r> R = new ArrayList<>();
   private static boolean y;
   private final q.n n;
   private final List<r.j> V = new ArrayList<>();
   private final Map<lw, Boolean> l = new IdentityHashMap<>();
   private final List<r.d> F = new ArrayList<>();
   private final List<Entry<String, hu.q>> m = new ArrayList<>();

   protected r(String var1, String var2, q.n var3) {
      super(var1, var2, Category.PLAYER, -1);
      this.n = var3;
      synchronized (R) {
         R.add(this);
      }
   }

   public final q.n m() {
      return this.n;
   }

   protected final q u() {
      return q.U();
   }

   protected final lw o(String var1, String var2, Runnable var3) {
      return this.O(var1, var2, var3, false);
   }

   protected final lw O(String var1, String var2, Runnable var3, boolean var4) {
      lw var5 = this.addSetting(new lw(var1, var2, -1));
      this.V.add(new r.j(var5, var3, var4));
      return var5;
   }

   protected final lw t(String var1, hu.q var2) {
      return this.o(var1, "Look at a dispenser and press to assign it as the " + var2.c + " table", () -> y(hu.f().H(getMc(), var2)));
   }

   public final List<r.d> z() {
      return List.copyOf(this.F);
   }

   public final List<lb<?>> A() {
      ArrayList var1 = new ArrayList();

      for (r.j var3 : this.V) {
         var1.add(var3.key());
      }

      return var1;
   }

   protected final void y(String var1, Runnable var2) {
      this.F.add(new r.d(var1, var2));
   }

   protected final void B(String var1, Runnable var2, Supplier<String> var3) {
      this.F.add(new r.d(var1, var2, var3));
   }

   protected static String F(String var0, String var1) {
      String var2 = var0.startsWith("Select ") ? var0.substring(7) : var0;
      String var3 = Character.toUpperCase(var2.charAt(0)) + var2.substring(1) + " selected";
      return var1 != null && !var1.isBlank() ? var3 + ": " + var1 : var3;
   }

   protected final void e(String var1, hu.q var2) {
      String var3 = var1.startsWith("Select ") ? var1.substring(7) : var1;
      this.m.add(Map.entry(Character.toUpperCase(var3.charAt(0)) + var3.substring(1), var2));
      this.B(
         var1,
         () -> at.Q(
            "Right-click the " + var2.c + " dispenser (Esc cancels)",
            var0x -> var0x.method_26204() instanceof class_2315,
            "That is not a dispenser",
            var1x -> {
               hu var2x = hu.f();
               hu.x var3x = var2x.J(var2x.N(getMc(), var1x), var2);
               return var3x.message();
            }
         ),
         () -> {
            int var2x = hu.f().m(var2).size();
            if (var2x == 0) {
               return var1;
            }

            String var3x = F(var1, "");
            return var2x == 1 ? var3x : var2x + " " + var3x.replace("table selected", "tables selected");
         }
      );
   }

   protected final void I() {
      this.y(
         "Remove a table",
         () -> at.Q(
            "Right-click the dispenser to remove (Esc cancels)", var0 -> var0.method_26204() instanceof class_2315, "That is not a dispenser", var0 -> {
               hu var1 = hu.f();
               return var1.d(var1.N(getMc(), var0)).message();
            }
         )
      );
   }

   protected final void v(String var1, String var2, Predicate<class_1799> var3, String var4, Function<class_1799, String> var5) {
      this.y(var1, () -> at.o("Click the " + var2 + " in your inventory (Esc cancels)", var3, var4, var5));
   }

   protected final void b(String var1, String var2, Predicate<class_1799> var3, String var4, Function<class_1799, String> var5, Supplier<String> var6) {
      this.B(var1, () -> at.o("Click the " + var2 + " in your inventory (Esc cancels)", var3, var4, var5), () -> {
         String var2x = (String)var6.get();
         return var2x == null ? var1 : F(var1, var2x);
      });
   }

   protected final void q() {
      this.I();
      this.y("Open game log", as::O);
      this.y("Stop all games", r::U);
   }

   public String setupHint() {
      return "Press a Select button, then right-click the block or click the item. It flashes green when chosen. Press a selected button again to change it.";
   }

   public static void U() {
      q.U().e();
      List var0;
      synchronized (R) {
         var0 = List.copyOf(R);
      }

      for (r var2 : var0) {
         if (var2.isEnabled()) {
            var2.disable();
         }
      }

      m("All casino games stopped");
   }

   public static r m() {
      List var0;
      synchronized (R) {
         var0 = List.copyOf(R);
      }

      r var5 = null;

      for (r var3 : var0) {
         if (var3.isEnabled()) {
            if (var3.n != q.n.q) {
               return var3;
            }

            var5 = var3;
         }
      }

      return var5;
   }

   public final List<String[]> D() {
      ArrayList var1 = new ArrayList();
      if (this.n != q.n.q) {
         q var2 = this.u();
         String var3 = this.getStatus();
         var1.add(new String[]{this.targetName(), var3.isEmpty() ? "-" : var3});
         if (var2.y()) {
            var1.add(new String[]{"State", "Paused: unassigned dispenser"});
         } else if (q.K()) {
            var1.add(new String[]{"State", "Swapping cards"});
         }

         for (Entry var5 : this.m) {
            int var6 = hu.f().m((hu.q)var5.getValue()).size();
            var1.add(new String[]{(String)var5.getKey(), var6 == 0 ? "Not selected" : (var6 == 1 ? "Selected" : var6 + " selected")});
         }
      }

      this.addHudRows(var1);
      return var1;
   }

   protected String targetName() {
      return "Target";
   }

   protected void addHudRows(List<String[]> var1) {
   }

   public String setupSummary() {
      if (getMc() != null && getMc().field_1687 != null) {
         StringBuilder var1 = new StringBuilder();

         for (Entry var3 : this.m) {
            if (!var1.isEmpty()) {
               var1.append('\n');
            }

            var1.append(t((String)var3.getKey(), (hu.q)var3.getValue()));
         }

         return var1.toString();
      } else {
         return "Join a world to select tables";
      }
   }

   protected static String t(String var0, hu.q var1) {
      List var2 = hu.f().m(var1);
      if (var2.isEmpty()) {
         return var0 + ": none selected";
      }

      StringBuilder var3 = new StringBuilder(var0).append(": ");

      for (int var4 = 0; var4 < var2.size(); var4++) {
         class_2338 var5 = hu.f().j((String)var2.get(var4));
         if (var4 > 0) {
            var3.append(", ");
         }

         if (var4 == 3) {
            var3.append("+").append(var2.size() - 3).append(" more");
            break;
         }

         var3.append(var5.method_10263()).append(" ").append(var5.method_10264()).append(" ").append(var5.method_10260());
      }

      return var3.toString();
   }

   protected static String a(class_1792 var0) {
      return var0.method_63680().getString();
   }

   protected final lw j() {
      return this.o("Remove Bind", "Look at an assigned dispenser and press to remove its game assignment", () -> y(hu.f().E(getMc())));
   }

   protected final lw x() {
      return this.o("Log Bind", "Open the recent game log", as::t);
   }

   protected final lw D() {
      return this.O("Stop Bind", "Stop every casino game and drop pending actions", r::U, true);
   }

   private static void y(hu.x var0) {
      m(var0.message());
   }

   protected static void m(String var0) {
      if (var0 != null && !var0.isBlank()) {
         class_310 var1 = class_310.method_1551();
         if (var1 != null && var1.field_1724 != null) {
            var1.field_1724.method_7353(class_2561.method_43470(var0), true);
         }

         yq.E("Casino", var0);
      }
   }

   private static void J(q.n var0) {
      if (!y) {
         y = (boolean)1;

         try {
            List var1;
            synchronized (R) {
               var1 = List.copyOf(R);
            }

            for (r var3 : var1) {
               if (var3.n != q.n.q) {
                  int var4 = var3.n == var0 ? 1 : 0;
                  if (var4 != 0 && !var3.isEnabled()) {
                     var3.enable();
                  } else if (var4 == 0 && var3.isEnabled()) {
                     var3.disable();
                  }
               }
            }
         } finally {
            y = (boolean)0;
         }
      }
   }

   @Override
   public void onEnable() {
      q var1 = this.u();
      var1.a();
      this.l.clear();
      this.applySettings();
      if (this.n != q.n.q) {
         var1.C(this.n);
         q8.G("Rig", "{} enabled", this.getName());
      }
   }

   @Override
   public void onDisable() {
      this.l.clear();
      q var1 = this.u();
      if (this.n != q.n.q && var1.D() == this.n) {
         var1.x();
         hu.f().D();
         q8.G("Rig", "{} disabled; game state released", this.getName());
      }

      this.releaseExtra();
   }

   protected void releaseExtra() {
   }

   protected abstract void applySettings();

   @Override
   public void onTick() {
      class_310 var1 = getMc();
      this.u().E(var1);
      this.h(var1);
      this.afterTick(var1);
   }

   protected void afterTick(class_310 var1) {
   }

   private void h(class_310 var1) {
      if (var1.method_22683() != null) {
         int var2 = var1.field_1755 != null ? 1 : 0;
         long var3 = var1.method_22683().method_4490();

         for (r.j var6 : this.V) {
            int var7 = var6.key().getValue();
            int var8 = var7 > 0 && s5.m(var3, var7) ? 1 : 0;
            boolean var9 = this.l.getOrDefault(var6.key(), false);
            this.l.put(var6.key(), Boolean.valueOf((boolean)var8));
            if (var8 != 0 && !var9 && (var2 == 0 || var6.inScreen() || this.screenAllowsBinds(var1))) {
               try {
                  var6.action().run();
               } catch (RuntimeException var11) {
                  q8.u("Rig", this.getName() + " bind failed", var11);
               }
            }
         }
      }
   }

   protected boolean screenAllowsBinds(class_310 var1) {
      return false;
   }

   @Override
   public String getStatus() {
      q var1 = this.u();
      return var1.D() == this.n ? var1.k(this.n) : "";
   }

   static {
      q.U().y(r::J);
   }

   public record d(String label, Runnable run, Supplier<String> current) {
      public d(String var1, Runnable var2) {
         this(var1, var2, () -> var1);
      }

      public String currentLabel() {
         try {
            String var1 = this.current.get();
            return var1 != null && !var1.isBlank() ? var1 : this.label;
         } catch (RuntimeException var2) {
            return this.label;
         }
      }
   }

   private record j(lw key, Runnable action, boolean inScreen) {
   }
}
