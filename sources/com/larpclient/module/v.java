package com.larpclient.module;

import com.larpclient.c3;
import com.larpclient.cc;
import com.larpclient.ep;
import com.larpclient.mt;
import com.larpclient.xa;
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

public abstract class v extends Module {
   private static final List<v> Z = new ArrayList<>();
   private static boolean O;
   private final c3.a N;
   private final List<v.x> r = new ArrayList<>();
   private final Map<dv, Boolean> f = new IdentityHashMap<>();
   private final List<v.n> E = new ArrayList<>();
   private final List<Entry<String, ep.o>> F = new ArrayList<>();

   protected v(String var1, String var2, c3.a var3) {
      super(var1, var2, Category.PLAYER, -1);
      this.N = var3;
      synchronized (Z) {
         Z.add(this);
      }
   }

   public final c3.a f() {
      return this.N;
   }

   protected final c3 t() {
      return c3.z();
   }

   protected final dv i(String var1, String var2, Runnable var3) {
      return this.x(var1, var2, var3, false);
   }

   protected final dv x(String var1, String var2, Runnable var3, boolean var4) {
      dv var5 = this.addSetting(new dv(var1, var2, -1));
      this.r.add(new v.x(var5, var3, var4));
      return var5;
   }

   protected final dv c(String var1, ep.o var2) {
      return this.i(var1, "Look at a dispenser and press to assign it as the " + var2.N + " table", () -> l(ep.H().s(getMc(), var2)));
   }

   public final List<v.n> H() {
      return List.copyOf(this.E);
   }

   public final List<df<?>> s() {
      ArrayList var1 = new ArrayList();

      for (v.x var3 : this.r) {
         var1.add(var3.key());
      }

      return var1;
   }

   protected final void D(String var1, Runnable var2) {
      this.E.add(new v.n(var1, var2));
   }

   protected final void c(String var1, Runnable var2, Supplier<String> var3) {
      this.E.add(new v.n(var1, var2, var3));
   }

   protected static String g(String var0, String var1) {
      String var2 = var0.startsWith("Select ") ? var0.substring(7) : var0;
      String var3 = Character.toUpperCase(var2.charAt(0)) + var2.substring(1) + " selected";
      return var1 != null && !var1.isBlank() ? var3 + ": " + var1 : var3;
   }

   protected final void x(String var1, ep.o var2) {
      String var3 = var1.startsWith("Select ") ? var1.substring(7) : var1;
      this.F.add(Map.entry(Character.toUpperCase(var3.charAt(0)) + var3.substring(1), var2));
      this.c(
         var1,
         () -> d.X(
            "Right-click the " + var2.N + " dispenser (Esc cancels)",
            var0x -> var0x.getBlock() instanceof DispenserBlock,
            "That is not a dispenser",
            var1x -> {
               ep var2x = ep.H();
               ep.d var3x = var2x.T(var2x.m(getMc(), var1x), var2);
               return var3x.message();
            }
         ),
         () -> {
            int var2x = ep.H().v(var2).size();
            if (var2x == 0) {
               return var1;
            }

            String var3x = g(var1, "");
            return var2x == 1 ? var3x : var2x + " " + var3x.replace("table selected", "tables selected");
         }
      );
   }

   protected final void J() {
      this.D(
         "Remove a table",
         () -> d.X("Right-click the dispenser to remove (Esc cancels)", var0 -> var0.getBlock() instanceof DispenserBlock, "That is not a dispenser", var0 -> {
            ep var1 = ep.H();
            return var1.v(var1.m(getMc(), var0)).message();
         })
      );
   }

   protected final void T(String var1, String var2, Predicate<ItemStack> var3, String var4, Function<ItemStack, String> var5) {
      this.D(var1, () -> d.l("Click the " + var2 + " in your inventory (Esc cancels)", var3, var4, var5));
   }

   protected final void b(String var1, String var2, Predicate<ItemStack> var3, String var4, Function<ItemStack, String> var5, Supplier<String> var6) {
      this.c(var1, () -> d.l("Click the " + var2 + " in your inventory (Esc cancels)", var3, var4, var5), () -> {
         String var2x = (String)var6.get();
         return var2x == null ? var1 : g(var1, var2x);
      });
   }

   protected final void K() {
      this.J();
      this.D("Open game log", j::b);
      this.D("Stop all games", v::V);
   }

   public String setupHint() {
      return "Press a Select button, then right-click the block or click the item. It flashes green when chosen. Press a selected button again to change it.";
   }

   public static void V() {
      c3.z().c();
      List var0;
      synchronized (Z) {
         var0 = List.copyOf(Z);
      }

      for (v var2 : var0) {
         if (var2.isEnabled()) {
            var2.disable();
         }
      }

      b("All casino games stopped");
   }

   public static v v() {
      List var0;
      synchronized (Z) {
         var0 = List.copyOf(Z);
      }

      v var5 = null;

      for (v var3 : var0) {
         if (var3.isEnabled()) {
            if (var3.N != c3.a.f) {
               return var3;
            }

            var5 = var3;
         }
      }

      return var5;
   }

   public final List<String[]> a() {
      ArrayList var1 = new ArrayList();
      if (this.N != c3.a.f) {
         c3 var2 = this.t();
         String var3 = this.getStatus();
         var1.add(new String[]{this.targetName(), var3.isEmpty() ? "-" : var3});
         if (var2.c()) {
            var1.add(new String[]{"State", "Paused: unassigned dispenser"});
         } else if (c3.Q()) {
            var1.add(new String[]{"State", "Swapping cards"});
         }

         for (Entry var5 : this.F) {
            int var6 = ep.H().v((ep.o)var5.getValue()).size();
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

         for (Entry var3 : this.F) {
            if (!var1.isEmpty()) {
               var1.append('\n');
            }

            var1.append(N((String)var3.getKey(), (ep.o)var3.getValue()));
         }

         return var1.toString();
      } else {
         return "Join a world to select tables";
      }
   }

   protected static String N(String var0, ep.o var1) {
      List var2 = ep.H().v(var1);
      if (var2.isEmpty()) {
         return var0 + ": none selected";
      }

      StringBuilder var3 = new StringBuilder(var0).append(": ");

      for (int var4 = 0; var4 < var2.size(); var4++) {
         BlockPos var5 = ep.H().x((String)var2.get(var4));
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

   protected final dv E() {
      return this.i("Remove Bind", "Look at an assigned dispenser and press to remove its game assignment", () -> l(ep.H().t(getMc())));
   }

   protected final dv j() {
      return this.i("Log Bind", "Open the recent game log", j::f);
   }

   protected final dv o() {
      return this.x("Stop Bind", "Stop every casino game and drop pending actions", v::V, true);
   }

   private static void l(ep.d var0) {
      b(var0.message());
   }

   protected static void b(String var0) {
      if (var0 != null && !var0.isBlank()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null) {
            var1.player.sendOverlayMessage(Component.literal(var0));
         }

         xa.q("Casino", var0);
      }
   }

   private static void j(c3.a var0) {
      if (!O) {
         O = (boolean)1;

         try {
            List var1;
            synchronized (Z) {
               var1 = List.copyOf(Z);
            }

            for (v var3 : var1) {
               if (var3.N != c3.a.f) {
                  int var4 = var3.N == var0 ? 1 : 0;
                  if (var4 != 0 && !var3.isEnabled()) {
                     var3.enable();
                  } else if (var4 == 0 && var3.isEnabled()) {
                     var3.disable();
                  }
               }
            }
         } finally {
            O = (boolean)0;
         }
      }
   }

   @Override
   public void onEnable() {
      c3 var1 = this.t();
      var1.j();
      this.f.clear();
      this.applySettings();
      if (this.N != c3.a.f) {
         var1.S(this.N);
         mt.K("Rig", "{} enabled", this.getName());
      }
   }

   @Override
   public void onDisable() {
      this.f.clear();
      c3 var1 = this.t();
      if (this.N != c3.a.f && var1.h() == this.N) {
         var1.D();
         ep.H().m();
         mt.K("Rig", "{} disabled; game state released", this.getName());
      }

      this.releaseExtra();
   }

   protected void releaseExtra() {
   }

   protected abstract void applySettings();

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      this.t().P(var1);
      this.c(var1);
      this.afterTick(var1);
   }

   protected void afterTick(Minecraft var1) {
   }

   private void c(Minecraft var1) {
      if (var1.getWindow() != null) {
         int var2 = var1.gui.screen() != null ? 1 : 0;
         long var3 = var1.getWindow().handle();

         for (v.x var6 : this.r) {
            int var7 = var6.key().P();
            int var8 = var7 > 0 && cc.N(var3, var7) ? 1 : 0;
            boolean var9 = this.f.getOrDefault(var6.key(), false);
            this.f.put(var6.key(), Boolean.valueOf((boolean)var8));
            if (var8 != 0 && !var9 && (var2 == 0 || var6.inScreen() || this.screenAllowsBinds(var1))) {
               try {
                  var6.action().run();
               } catch (RuntimeException var11) {
                  mt.P("Rig", this.getName() + " bind failed", var11);
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
      c3 var1 = this.t();
      return var1.h() == this.N ? var1.w(this.N) : "";
   }

   static {
      c3.z().k(v::j);
   }

   public record n(String label, Runnable run, Supplier<String> current) {
      public n(String var1, Runnable var2) {
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

   private record x(dv key, Runnable action, boolean inScreen) {
   }
}
