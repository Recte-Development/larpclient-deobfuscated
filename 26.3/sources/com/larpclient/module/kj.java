package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.a6;
import com.larpclient.bn;
import com.larpclient.hk;
import com.larpclient.p4;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;

public abstract class kj extends Module {
   private static final List<kj> Q = new ArrayList<>();
   private static boolean G;
   private final a6.d E;
   private final List<kj.m> a = new ArrayList<>();
   private final Map<x, Boolean> C = new IdentityHashMap<>();
   private final List<kj.o> B = new ArrayList<>();
   private final List<Entry<String, hk.k>> T = new ArrayList<>();

   protected kj(String var1, String var2, a6.d var3) {
      super(var1, var2, Category.PLAYER, -1);
      this.E = var3;
      synchronized (Q) {
         Q.add(this);
      }
   }

   public final a6.d R() {
      return this.E;
   }

   protected final a6 a() {
      return a6.F();
   }

   protected final x W(String var1, String var2, Runnable var3) {
      return this.g(var1, var2, var3, false);
   }

   protected final x g(String var1, String var2, Runnable var3, boolean var4) {
      x var5 = this.addSetting(new x(var1, var2, -1));
      this.a.add(new kj.m(var5, var3, var4));
      return var5;
   }

   protected final x u(String var1, hk.k var2) {
      return this.W(var1, "Look at a dispenser and press to assign it as the " + var2.o + " table", () -> p(hk.e().O(getMc(), var2)));
   }

   public final List<kj.o> q() {
      return List.copyOf(this.B);
   }

   public final List<r<?>> w() {
      ArrayList var1 = new ArrayList();

      for (kj.m var3 : this.a) {
         var1.add(var3.key());
      }

      return var1;
   }

   protected final void i(String var1, Runnable var2) {
      this.B.add(new kj.o(var1, var2));
   }

   protected final void q(String var1, Runnable var2, Supplier<String> var3) {
      this.B.add(new kj.o(var1, var2, var3));
   }

   protected static String Q(String var0, String var1) {
      String var2 = var0.startsWith("Select ") ? var0.substring(7) : var0;
      String var3 = Character.toUpperCase(var2.charAt(0)) + var2.substring(1) + " selected";
      return var1 != null && !var1.isBlank() ? var3 + ": " + var1 : var3;
   }

   protected final void x(String var1, hk.k var2) {
      String var3 = var1.startsWith("Select ") ? var1.substring(7) : var1;
      this.T.add(Map.entry(Character.toUpperCase(var3.charAt(0)) + var3.substring(1), var2));
      this.q(
         var1,
         () -> fb.f(
            "Right-click the " + var2.o + " dispenser (Esc cancels)",
            var0x -> var0x.getBlock() instanceof DispenserBlock,
            "That is not a dispenser",
            var1x -> {
               hk var2x = hk.e();
               hk.o var3x = var2x.R(var2x.v(getMc(), var1x), var2);
               return var3x.message();
            }
         ),
         () -> {
            int var2x = hk.e().A(var2).size();
            if (var2x == 0) {
               return var1;
            }

            String var3x = Q(var1, "");
            return var2x == 1 ? var3x : var2x + " " + var3x.replace("table selected", "tables selected");
         }
      );
   }

   protected final void i() {
      this.i(
         "Remove a table",
         () -> fb.f(
            "Right-click the dispenser to remove (Esc cancels)", var0 -> var0.getBlock() instanceof DispenserBlock, "That is not a dispenser", var0 -> {
               hk var1 = hk.e();
               return var1.y(var1.v(getMc(), var0)).message();
            }
         )
      );
   }

   protected final void q(String var1, String var2, Predicate<ItemStack> var3, String var4, Function<ItemStack, String> var5) {
      this.i(var1, () -> fb.u("Click the " + var2 + " in your inventory (Esc cancels)", var3, var4, var5));
   }

   protected final void M(String var1, String var2, Predicate<ItemStack> var3, String var4, Function<ItemStack, String> var5, Supplier<String> var6) {
      this.q(var1, () -> fb.u("Click the " + var2 + " in your inventory (Esc cancels)", var3, var4, var5), () -> {
         String var2x = (String)var6.get();
         return var2x == null ? var1 : Q(var1, var2x);
      });
   }

   protected final void X() {
      this.i();
      this.i("Open game log", j::m);
      this.i("Stop all games", kj::M);
   }

   public String setupHint() {
      return "Press a Select button, then right-click the block or click the item. It flashes green when chosen. Press a selected button again to change it.";
   }

   public static void M() {
      a6.F().x();
      List var0;
      synchronized (Q) {
         var0 = List.copyOf(Q);
      }

      for (kj var2 : var0) {
         if (var2.isEnabled()) {
            var2.disable();
         }
      }

      i("All casino games stopped");
   }

   public static kj O() {
      List var0;
      synchronized (Q) {
         var0 = List.copyOf(Q);
      }

      kj var5 = null;

      for (kj var3 : var0) {
         if (var3.isEnabled()) {
            if (var3.E != a6.d.s) {
               return var3;
            }

            var5 = var3;
         }
      }

      return var5;
   }

   public final List<String[]> y() {
      ArrayList var1 = new ArrayList();
      if (this.E != a6.d.s) {
         a6 var2 = this.a();
         String var3 = this.getStatus();
         var1.add(new String[]{this.targetName(), var3.isEmpty() ? "-" : var3});
         if (var2.K()) {
            var1.add(new String[]{"State", "Paused: unassigned dispenser"});
         } else if (a6.i()) {
            var1.add(new String[]{"State", "Swapping cards"});
         }

         for (Entry var5 : this.T) {
            int var6 = hk.e().A((hk.k)var5.getValue()).size();
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
      if (getMc() != null && getMc().level != null) {
         StringBuilder var1 = new StringBuilder();

         for (Entry var3 : this.T) {
            if (!var1.isEmpty()) {
               var1.append('\n');
            }

            var1.append(H((String)var3.getKey(), (hk.k)var3.getValue()));
         }

         return var1.toString();
      } else {
         return "Join a world to select tables";
      }
   }

   protected static String H(String var0, hk.k var1) {
      List var2 = hk.e().A(var1);
      if (var2.isEmpty()) {
         return var0 + ": none selected";
      }

      StringBuilder var3 = new StringBuilder(var0).append(": ");

      for (int var4 = 0; var4 < var2.size(); var4++) {
         BlockPos var5 = hk.e().Q((String)var2.get(var4));
         if (var4 > 0) {
            var3.append(", ");
         }

         if (var4 == 3) {
            var3.append("+").append(var2.size() - 3).append(" more");
            break;
         }

         var3.append(var5.getX()).append(" ").append(var5.getY()).append(" ").append(var5.getZ());
      }

      return var3.toString();
   }

   protected static String u(Item var0) {
      return !var0.builtInRegistryHolder().areComponentsBound()
         ? Component.translatable(var0.getDescriptionId()).getString()
         : new ItemStack(var0).getHoverName().getString();
   }

   protected final x o() {
      return this.W("Remove Bind", "Look at an assigned dispenser and press to remove its game assignment", () -> p(hk.e().J(getMc())));
   }

   protected final x z() {
      return this.W("Log Bind", "Open the recent game log", j::B);
   }

   protected final x T() {
      return this.g("Stop Bind", "Stop every casino game and drop pending actions", kj::M, true);
   }

   private static void p(hk.o var0) {
      i(var0.message());
   }

   protected static void i(String var0) {
      if (var0 != null && !var0.isBlank()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null) {
            var1.player.sendOverlayMessage(Component.literal(var0));
         }

         bn.j("Casino", var0);
      }
   }

   private static void O(a6.d var0) {
      if (!G) {
         G = (boolean)1;

         try {
            List var1;
            synchronized (Q) {
               var1 = List.copyOf(Q);
            }

            for (kj var3 : var1) {
               if (var3.E != a6.d.s) {
                  int var4 = var3.E == var0 ? 1 : 0;
                  if (var4 != 0 && !var3.isEnabled()) {
                     var3.enable();
                  } else if (var4 == 0 && var3.isEnabled()) {
                     var3.disable();
                  }
               }
            }
         } finally {
            G = (boolean)0;
         }
      }
   }

   @Override
   public void onEnable() {
      a6 var1 = this.a();
      var1.F();
      this.C.clear();
      this.applySettings();
      if (this.E != a6.d.s) {
         var1.S(this.E);
         _q.z("Rig", "{} enabled", this.getName());
      }
   }

   @Override
   public void onDisable() {
      this.C.clear();
      a6 var1 = this.a();
      if (this.E != a6.d.s && var1.E() == this.E) {
         var1.V();
         hk.e().L();
         _q.z("Rig", "{} disabled; game state released", this.getName());
      }

      this.releaseExtra();
   }

   protected void releaseExtra() {
   }

   protected abstract void applySettings();

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      this.a().g(var1);
      this.U(var1);
      this.afterTick(var1);
   }

   protected void afterTick(Minecraft var1) {
   }

   private void U(Minecraft var1) {
      if (var1.getWindow() != null) {
         int var2 = var1.gui.screen() != null ? 1 : 0;
         long var3 = var1.getWindow().handle();

         for (kj.m var6 : this.a) {
            int var7 = var6.key().getValue();
            int var8 = var7 > 0 && p4.Y(var3, var7) ? 1 : 0;
            boolean var9 = this.C.getOrDefault(var6.key(), false);
            this.C.put(var6.key(), Boolean.valueOf((boolean)var8));
            if (var8 != 0 && !var9 && (var2 == 0 || var6.inScreen() || this.screenAllowsBinds(var1))) {
               try {
                  var6.action().run();
               } catch (RuntimeException var11) {
                  _q.o("Rig", this.getName() + " bind failed", var11);
               }
            }
         }
      }
   }

   protected boolean screenAllowsBinds(Minecraft var1) {
      return false;
   }

   @Override
   public String getStatus() {
      a6 var1 = this.a();
      return var1.E() == this.E ? var1.N(this.E) : "";
   }

   static {
      a6.F().r(kj::O);
   }

   private record m(x key, Runnable action, boolean inScreen) {
   }

   public record o(String label, Runnable run, Supplier<String> current) {
      public o(String var1, Runnable var2) {
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
}
