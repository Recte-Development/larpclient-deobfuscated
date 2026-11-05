package com.larpclient.module;

import com.larpclient.f9;
import com.larpclient.hh;
import com.larpclient.pe;
import com.larpclient.r4;
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

public abstract class a extends Module {
   private static final List<a> u = new ArrayList<>();
   private static boolean J;
   private final com.larpclient.l6.e Z;
   private final List<a.k> e = new ArrayList<>();
   private final Map<la, Boolean> z = new IdentityHashMap<>();
   private final List<a.s> O = new ArrayList<>();
   private final List<Entry<String, f9.b>> j = new ArrayList<>();

   protected a(String var1, String var2, com.larpclient.l6.e var3) {
      super(var1, var2, Category.PLAYER, -1);
      this.Z = var3;
      synchronized (u) {
         u.add(this);
      }
   }

   public final com.larpclient.l6.e n() {
      return this.Z;
   }

   protected final com.larpclient.l6 o() {
      return com.larpclient.l6.E();
   }

   protected final la B(String var1, String var2, Runnable var3) {
      return this.C(var1, var2, var3, false);
   }

   protected final la C(String var1, String var2, Runnable var3, boolean var4) {
      la var5 = this.addSetting(new la(var1, var2, -1));
      this.e.add(new a.k(var5, var3, var4));
      return var5;
   }

   protected final la d(String var1, f9.b var2) {
      return this.B(var1, "Look at a dispenser and press to assign it as the " + var2.w + " table", () -> K(f9.W().d(getMc(), var2)));
   }

   public final List<a.s> Y() {
      return List.copyOf(this.O);
   }

   public final List<l6<?>> z() {
      ArrayList var1 = new ArrayList();

      for (a.k var3 : this.e) {
         var1.add(var3.key());
      }

      return var1;
   }

   protected final void I(String var1, Runnable var2) {
      this.O.add(new a.s(var1, var2));
   }

   protected final void n(String var1, Runnable var2, Supplier<String> var3) {
      this.O.add(new a.s(var1, var2, var3));
   }

   protected static String p(String var0, String var1) {
      String var2 = var0.startsWith("Select ") ? var0.substring(7) : var0;
      String var3 = Character.toUpperCase(var2.charAt(0)) + var2.substring(1) + " selected";
      return var1 != null && !var1.isBlank() ? var3 + ": " + var1 : var3;
   }

   protected final void D(String var1, f9.b var2) {
      String var3 = var1.startsWith("Select ") ? var1.substring(7) : var1;
      this.j.add(Map.entry(Character.toUpperCase(var3.charAt(0)) + var3.substring(1), var2));
      this.n(
         var1,
         () -> a_.B(
            "Right-click the " + var2.w + " dispenser (Esc cancels)",
            var0x -> var0x.getBlock() instanceof DispenserBlock,
            "That is not a dispenser",
            var1x -> {
               f9 var2x = f9.W();
               f9.n var3x = var2x.Z(var2x.x(getMc(), var1x), var2);
               return var3x.message();
            }
         ),
         () -> {
            int var2x = f9.W().T(var2).size();
            if (var2x == 0) {
               return var1;
            }

            String var3x = p(var1, "");
            return var2x == 1 ? var3x : var2x + " " + var3x.replace("table selected", "tables selected");
         }
      );
   }

   protected final void e() {
      this.I(
         "Remove a table",
         () -> a_.B(
            "Right-click the dispenser to remove (Esc cancels)", var0 -> var0.getBlock() instanceof DispenserBlock, "That is not a dispenser", var0 -> {
               f9 var1 = f9.W();
               return var1.x(var1.x(getMc(), var0)).message();
            }
         )
      );
   }

   protected final void g(String var1, String var2, Predicate<ItemStack> var3, String var4, Function<ItemStack, String> var5) {
      this.I(var1, () -> a_.L("Click the " + var2 + " in your inventory (Esc cancels)", var3, var4, var5));
   }

   protected final void D(String var1, String var2, Predicate<ItemStack> var3, String var4, Function<ItemStack, String> var5, Supplier<String> var6) {
      this.n(var1, () -> a_.L("Click the " + var2 + " in your inventory (Esc cancels)", var3, var4, var5), () -> {
         String var2x = (String)var6.get();
         return var2x == null ? var1 : p(var1, var2x);
      });
   }

   protected final void S() {
      this.e();
      this.I("Open game log", l4::x);
      this.I("Stop all games", a::b);
   }

   public String setupHint() {
      return "Press a Select button, then right-click the block or click the item. It flashes green when chosen. Press a selected button again to change it.";
   }

   public static void b() {
      com.larpclient.l6.E().m();
      List var0;
      synchronized (u) {
         var0 = List.copyOf(u);
      }

      for (a var2 : var0) {
         if (var2.isEnabled()) {
            var2.disable();
         }
      }

      d("All casino games stopped");
   }

   public static a x() {
      List var0;
      synchronized (u) {
         var0 = List.copyOf(u);
      }

      a var5 = null;

      for (a var3 : var0) {
         if (var3.isEnabled()) {
            if (var3.Z != com.larpclient.l6.e.K) {
               return var3;
            }

            var5 = var3;
         }
      }

      return var5;
   }

   public final List<String[]> k() {
      ArrayList var1 = new ArrayList();
      if (this.Z != com.larpclient.l6.e.K) {
         com.larpclient.l6 var2 = this.o();
         String var3 = this.getStatus();
         var1.add(new String[]{this.targetName(), var3.isEmpty() ? "-" : var3});
         if (var2.G()) {
            var1.add(new String[]{"State", "Paused: unassigned dispenser"});
         } else if (com.larpclient.l6.n()) {
            var1.add(new String[]{"State", "Swapping cards"});
         }

         for (Entry var5 : this.j) {
            int var6 = f9.W().T((f9.b)var5.getValue()).size();
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

         for (Entry var3 : this.j) {
            if (!var1.isEmpty()) {
               var1.append('\n');
            }

            var1.append(V((String)var3.getKey(), (f9.b)var3.getValue()));
         }

         return var1.toString();
      } else {
         return "Join a world to select tables";
      }
   }

   protected static String V(String var0, f9.b var1) {
      List var2 = f9.W().T(var1);
      if (var2.isEmpty()) {
         return var0 + ": none selected";
      }

      StringBuilder var3 = new StringBuilder(var0).append(": ");

      for (int var4 = 0; var4 < var2.size(); var4++) {
         BlockPos var5 = f9.W().j((String)var2.get(var4));
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

   protected static String w(Item var0) {
      return !var0.builtInRegistryHolder().areComponentsBound()
         ? Component.translatable(var0.getDescriptionId()).getString()
         : new ItemStack(var0).getHoverName().getString();
   }

   protected final la U() {
      return this.B("Remove Bind", "Look at an assigned dispenser and press to remove its game assignment", () -> K(f9.W().Z(getMc())));
   }

   protected final la S() {
      return this.B("Log Bind", "Open the recent game log", l4::V);
   }

   protected final la Z() {
      return this.C("Stop Bind", "Stop every casino game and drop pending actions", a::b, true);
   }

   private static void K(f9.n var0) {
      d(var0.message());
   }

   protected static void d(String var0) {
      if (var0 != null && !var0.isBlank()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null) {
            var1.player.sendOverlayMessage(Component.literal(var0));
         }

         hh.K("Casino", var0);
      }
   }

   private static void e(com.larpclient.l6.e var0) {
      if (!J) {
         J = (boolean)1;

         try {
            List var1;
            synchronized (u) {
               var1 = List.copyOf(u);
            }

            for (a var3 : var1) {
               if (var3.Z != com.larpclient.l6.e.K) {
                  int var4 = var3.Z == var0 ? 1 : 0;
                  if (var4 != 0 && !var3.isEnabled()) {
                     var3.enable();
                  } else if (var4 == 0 && var3.isEnabled()) {
                     var3.disable();
                  }
               }
            }
         } finally {
            J = (boolean)0;
         }
      }
   }

   @Override
   public void onEnable() {
      com.larpclient.l6 var1 = this.o();
      var1.Y();
      this.z.clear();
      this.applySettings();
      if (this.Z != com.larpclient.l6.e.K) {
         var1.c(this.Z);
         r4.V("Rig", "{} enabled", this.getName());
      }
   }

   @Override
   public void onDisable() {
      this.z.clear();
      com.larpclient.l6 var1 = this.o();
      if (this.Z != com.larpclient.l6.e.K && var1.X() == this.Z) {
         var1.f();
         f9.W().r();
         r4.V("Rig", "{} disabled; game state released", this.getName());
      }

      this.releaseExtra();
   }

   protected void releaseExtra() {
   }

   protected abstract void applySettings();

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      this.o().f(var1);
      this.i(var1);
      this.afterTick(var1);
   }

   protected void afterTick(Minecraft var1) {
   }

   private void i(Minecraft var1) {
      if (var1.getWindow() != null) {
         int var2 = var1.gui.screen() != null ? 1 : 0;
         long var3 = var1.getWindow().handle();

         for (a.k var6 : this.e) {
            int var7 = var6.key().getValue();
            int var8 = var7 > 0 && pe.A(var3, var7) ? 1 : 0;
            boolean var9 = this.z.getOrDefault(var6.key(), false);
            this.z.put(var6.key(), Boolean.valueOf((boolean)var8));
            if (var8 != 0 && !var9 && (var2 == 0 || var6.inScreen() || this.screenAllowsBinds(var1))) {
               try {
                  var6.action().run();
               } catch (RuntimeException var11) {
                  r4.r("Rig", this.getName() + " bind failed", var11);
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
      com.larpclient.l6 var1 = this.o();
      return var1.X() == this.Z ? var1.z(this.Z) : "";
   }

   static {
      com.larpclient.l6.E().r(a::e);
   }

   private record k(la key, Runnable action, boolean inScreen) {
   }

   public record s(String label, Runnable run, Supplier<String> current) {
      public s(String var1, Runnable var2) {
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
