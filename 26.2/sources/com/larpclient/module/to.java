package com.larpclient.module;

import com.larpclient.bc;
import com.larpclient.gj;
import com.larpclient.sg;
import com.larpclient.te;
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

public abstract class to extends Module {
   private static final List<to> P = new ArrayList<>();
   private static boolean h;
   private final sg.i R;
   private final List<to.w> f = new ArrayList<>();
   private final Map<v, Boolean> z = new IdentityHashMap<>();
   private final List<to.b> x = new ArrayList<>();
   private final List<Entry<String, gj.d>> v = new ArrayList<>();

   protected to(String var1, String var2, sg.i var3) {
      super(var1, var2, Category.PLAYER, -1);
      this.R = var3;
      synchronized (P) {
         P.add(this);
      }
   }

   public final sg.i o() {
      return this.R;
   }

   protected final sg G() {
      return sg.G();
   }

   protected final v L(String var1, String var2, Runnable var3) {
      return this.z(var1, var2, var3, false);
   }

   protected final v z(String var1, String var2, Runnable var3, boolean var4) {
      v var5 = this.addSetting(new v(var1, var2, -1));
      this.f.add(new to.w(var5, var3, var4));
      return var5;
   }

   protected final v C(String var1, gj.d var2) {
      return this.L(var1, "Look at a dispenser and press to assign it as the " + var2.p + " table", () -> A(gj.c().a(getMc(), var2)));
   }

   public final List<to.b> i() {
      return List.copyOf(this.x);
   }

   public final List<q<?>> e() {
      ArrayList var1 = new ArrayList();

      for (to.w var3 : this.f) {
         var1.add(var3.key());
      }

      return var1;
   }

   protected final void I(String var1, Runnable var2) {
      this.x.add(new to.b(var1, var2));
   }

   protected final void c(String var1, Runnable var2, Supplier<String> var3) {
      this.x.add(new to.b(var1, var2, var3));
   }

   protected static String Y(String var0, String var1) {
      String var2 = var0.startsWith("Select ") ? var0.substring(7) : var0;
      String var3 = Character.toUpperCase(var2.charAt(0)) + var2.substring(1) + " selected";
      return var1 != null && !var1.isBlank() ? var3 + ": " + var1 : var3;
   }

   protected final void Z(String var1, gj.d var2) {
      String var3 = var1.startsWith("Select ") ? var1.substring(7) : var1;
      this.v.add(Map.entry(Character.toUpperCase(var3.charAt(0)) + var3.substring(1), var2));
      this.c(
         var1,
         () -> com.larpclient.module.w.r(
            "Right-click the " + var2.p + " dispenser (Esc cancels)",
            var0x -> var0x.getBlock() instanceof DispenserBlock,
            "That is not a dispenser",
            var1x -> {
               gj var2x = gj.c();
               gj.a var3x = var2x.R(var2x.w(getMc(), var1x), var2);
               return var3x.message();
            }
         ),
         () -> {
            int var2x = gj.c().S(var2).size();
            if (var2x == 0) {
               return var1;
            }

            String var3x = Y(var1, "");
            return var2x == 1 ? var3x : var2x + " " + var3x.replace("table selected", "tables selected");
         }
      );
   }

   protected final void D() {
      this.I(
         "Remove a table",
         () -> com.larpclient.module.w.r(
            "Right-click the dispenser to remove (Esc cancels)", var0 -> var0.getBlock() instanceof DispenserBlock, "That is not a dispenser", var0 -> {
               gj var1 = gj.c();
               return var1.x(var1.w(getMc(), var0)).message();
            }
         )
      );
   }

   protected final void X(String var1, String var2, Predicate<ItemStack> var3, String var4, Function<ItemStack, String> var5) {
      this.I(var1, () -> com.larpclient.module.w.q("Click the " + var2 + " in your inventory (Esc cancels)", var3, var4, var5));
   }

   protected final void k(String var1, String var2, Predicate<ItemStack> var3, String var4, Function<ItemStack, String> var5, Supplier<String> var6) {
      this.c(var1, () -> com.larpclient.module.w.q("Click the " + var2 + " in your inventory (Esc cancels)", var3, var4, var5), () -> {
         String var2x = (String)var6.get();
         return var2x == null ? var1 : Y(var1, var2x);
      });
   }

   protected final void h() {
      this.D();
      this.I("Open game log", z::E);
      this.I("Stop all games", to::i);
   }

   public String setupHint() {
      return "Press a Select button, then right-click the block or click the item. It flashes green when chosen. Press a selected button again to change it.";
   }

   public static void i() {
      sg.G().P();
      List var0;
      synchronized (P) {
         var0 = List.copyOf(P);
      }

      for (to var2 : var0) {
         if (var2.isEnabled()) {
            var2.disable();
         }
      }

      c("All casino games stopped");
   }

   public static to A() {
      List var0;
      synchronized (P) {
         var0 = List.copyOf(P);
      }

      to var5 = null;

      for (to var3 : var0) {
         if (var3.isEnabled()) {
            if (var3.R != sg.i.H) {
               return var3;
            }

            var5 = var3;
         }
      }

      return var5;
   }

   public final List<String[]> G() {
      ArrayList var1 = new ArrayList();
      if (this.R != sg.i.H) {
         sg var2 = this.G();
         String var3 = this.getStatus();
         var1.add(new String[]{this.targetName(), var3.isEmpty() ? "-" : var3});
         if (var2.p()) {
            var1.add(new String[]{"State", "Paused: unassigned dispenser"});
         } else if (sg.I()) {
            var1.add(new String[]{"State", "Swapping cards"});
         }

         for (Entry var5 : this.v) {
            int var6 = gj.c().S((gj.d)var5.getValue()).size();
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

         for (Entry var3 : this.v) {
            if (!var1.isEmpty()) {
               var1.append('\n');
            }

            var1.append(u((String)var3.getKey(), (gj.d)var3.getValue()));
         }

         return var1.toString();
      } else {
         return "Join a world to select tables";
      }
   }

   protected static String u(String var0, gj.d var1) {
      List var2 = gj.c().S(var1);
      if (var2.isEmpty()) {
         return var0 + ": none selected";
      }

      StringBuilder var3 = new StringBuilder(var0).append(": ");

      for (int var4 = 0; var4 < var2.size(); var4++) {
         BlockPos var5 = gj.c().n((String)var2.get(var4));
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

   protected static String j(Item var0) {
      return !var0.builtInRegistryHolder().areComponentsBound()
         ? Component.translatable(var0.getDescriptionId()).getString()
         : new ItemStack(var0).getHoverName().getString();
   }

   protected final v M() {
      return this.L("Remove Bind", "Look at an assigned dispenser and press to remove its game assignment", () -> A(gj.c().b(getMc())));
   }

   protected final v V() {
      return this.L("Log Bind", "Open the recent game log", z::B);
   }

   protected final v N() {
      return this.z("Stop Bind", "Stop every casino game and drop pending actions", to::i, true);
   }

   private static void A(gj.a var0) {
      c(var0.message());
   }

   protected static void c(String var0) {
      if (var0 != null && !var0.isBlank()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null) {
            var1.player.sendOverlayMessage(Component.literal(var0));
         }

         te.Z("Casino", var0);
      }
   }

   private static void W(sg.i var0) {
      if (!h) {
         h = (boolean)1;

         try {
            List var1;
            synchronized (P) {
               var1 = List.copyOf(P);
            }

            for (to var3 : var1) {
               if (var3.R != sg.i.H) {
                  int var4 = var3.R == var0 ? 1 : 0;
                  if (var4 != 0 && !var3.isEnabled()) {
                     var3.enable();
                  } else if (var4 == 0 && var3.isEnabled()) {
                     var3.disable();
                  }
               }
            }
         } finally {
            h = (boolean)0;
         }
      }
   }

   @Override
   public void onEnable() {
      sg var1 = this.G();
      var1.v();
      this.z.clear();
      this.applySettings();
      if (this.R != sg.i.H) {
         var1.Q(this.R);
         com.larpclient.h.A("Rig", "{} enabled", this.getName());
      }
   }

   @Override
   public void onDisable() {
      this.z.clear();
      sg var1 = this.G();
      if (this.R != sg.i.H && var1.o() == this.R) {
         var1.K();
         gj.c().a();
         com.larpclient.h.A("Rig", "{} disabled; game state released", this.getName());
      }

      this.releaseExtra();
   }

   protected void releaseExtra() {
   }

   protected abstract void applySettings();

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      this.G().g(var1);
      this.u(var1);
      this.afterTick(var1);
   }

   protected void afterTick(Minecraft var1) {
   }

   private void u(Minecraft var1) {
      if (var1.getWindow() != null) {
         int var2 = var1.gui.screen() != null ? 1 : 0;
         long var3 = var1.getWindow().handle();

         for (to.w var6 : this.f) {
            int var7 = var6.key().getValue();
            int var8 = var7 > 0 && bc.I(var3, var7) ? 1 : 0;
            boolean var9 = this.z.getOrDefault(var6.key(), false);
            this.z.put(var6.key(), Boolean.valueOf((boolean)var8));
            if (var8 != 0 && !var9 && (var2 == 0 || var6.inScreen() || this.screenAllowsBinds(var1))) {
               try {
                  var6.action().run();
               } catch (RuntimeException var11) {
                  com.larpclient.h.m("Rig", this.getName() + " bind failed", var11);
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
      sg var1 = this.G();
      return var1.o() == this.R ? var1.d(this.R) : "";
   }

   static {
      sg.G().s(to::W);
   }

   public record b(String label, Runnable run, Supplier<String> current) {
      public b(String var1, Runnable var2) {
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

   private record w(v key, Runnable action, boolean inScreen) {
   }
}
