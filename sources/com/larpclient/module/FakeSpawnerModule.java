package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.ff;
import com.larpclient.fv;
import com.larpclient.jq;
import com.larpclient.jy;
import com.larpclient.mb;
import com.larpclient.mx;
import com.larpclient.pi;
import com.larpclient.r4;
import com.larpclient.ul;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.EntityBlock;

public final class FakeSpawnerModule extends Module {
   private static final List<String> z = List.of(
      "None",
      "minecraft:cobblestone",
      "minecraft:stone",
      "minecraft:deepslate",
      "minecraft:netherrack",
      "minecraft:end_stone",
      "minecraft:iron_block",
      "minecraft:gold_block",
      "minecraft:glass"
   );
   private static FakeSpawnerModule X;
   private final lp[] d = new lp[3];
   public final lc b = this.addSetting(new lc("Preview Count", "Count for a standalone menu preview", 1.0, 1.0, 5000.0, 1.0));
   public final ln s = this.addSetting(new ln("Enchant Glint", "Show an enchantment glint on spawner items", false));
   private final Map<String, lc> l = new LinkedHashMap<>();
   private boolean r;
   private String e = "§dSpawner";
   private String J = "";
   private final Path w;

   private Path x() {
      return this.w != null ? this.w : FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-local.json");
   }

   public FakeSpawnerModule() {
      this(null);
   }

   public FakeSpawnerModule(Path var1) {
      super("Fake Spawner", "Show blocks as spawners; loot buys real items via /shop", Category.PLAYER, -1);
      this.w = var1;
      X = this;
      String[] var2 = d();

      for (final mb var6 : mb.values()) {
         this.d[var6.ordinal()] = new lp(
            var6.x + " Block", "Use a different ordinary block for each mob type", var6 == mb.v ? "minecraft:cobblestone" : "None", var2
         ) {
            private final Map<String, ItemStack> D = new HashMap<>();
            private boolean a;

            @Override
            public String getDisplayName() {
               return switch (var6) {
                  case v -> "Skeleton";
                  case p -> "Iron";
                  case R -> "Piglin";
               };
            }

            @Override
            public List<String> getVisibleModes() {
               if (FakeSpawnerModule.z.contains(this.getValue())) {
                  return FakeSpawnerModule.z;
               }

               ArrayList var1x = new ArrayList<>(FakeSpawnerModule.z);
               var1x.add(this.getValue());
               return List.copyOf(var1x);
            }

            @Override
            public ItemStack getModeIcon(String var1) {
               if (var1.equals("None")) {
                  return ItemStack.EMPTY;
               }

               Item var2x = com.larpclient.lj.V(var1);
               if (!var2x.builtInRegistryHolder().areComponentsBound()) {
                  this.D.clear();
                  if (!this.a) {
                     r4.g("FakeSpawner", "Block icons deferred until item components are bound; text selection remains available");
                  }

                  this.a = (boolean)1;
                  return ItemStack.EMPTY;
               } else {
                  if (this.a) {
                     r4.g("FakeSpawner", "Item components ready; block icons resumed");
                  }

                  this.a = (boolean)0;
                  return this.D.computeIfAbsent(var1, var1x -> var2.getDefaultInstance());
               }
            }

            @Override
            public boolean isSearchable() {
               return true;
            }

            @Override
            public String getModeLabel(String var1) {
               String var2x = var1.startsWith("minecraft:") ? var1.substring("minecraft:".length()) : var1;
               return Arrays.stream(var2x.replace('_', ' ').split(" "))
                  .map(var0 -> (CharSequence)(var0.isEmpty() ? var0 : Character.toUpperCase(var0.charAt(0)) + var0.substring(1)))
                  .collect(Collectors.joining(" "));
            }

            @Override
            public void setValue(String var1) {
               if (!var1.equals("None")) {
                  for (lp var5 : FakeSpawnerModule.this.d) {
                     if (var5 != null && var5 != this && var5.getValue().equals(var1)) {
                        throw new IllegalArgumentException("Each spawner type needs a different backing block");
                     }
                  }
               }

               super.setValue(var1);
            }
         };
         this.d[var6.ordinal()].W(var1x -> this.o());
      }

      for (String var8 : List.of("bone", "arrow", "iron_ingot", "rotten_flesh", "gold_nugget")) {
         lc var9 = new lc(var8.replace('_', ' ') + " Unit Price", "Cosmetic worth per item; -1 uses server order/API prices", -1.0, -1.0, 1.0E12, 0.01);
         var9.W(var1x -> this.o());
         this.l.put("minecraft:" + var8, var9);
      }

      if (var1 != null) {
         this.A();
      }
   }

   private static String[] d() {
      ArrayList var0 = new ArrayList();
      var0.add("None");
      BuiltInRegistries.ITEM
         .stream()
         .filter(
            var0x -> var0x instanceof BlockItem var1
               && !(var1.getBlock() instanceof EntityBlock)
               && var0x != Items.SPAWNER
               && !var1.getBlock().defaultBlockState().isAir()
         )
         .map(var0x -> BuiltInRegistries.ITEM.getKey(var0x).toString())
         .sorted()
         .forEach(var0::add);
      return var0.toArray(String[]::new);
   }

   public static FakeSpawnerModule w() {
      return X;
   }

   public String w(int var1) {
      String var2 = this.d[var1].getValue();
      return var2.equals("None") ? "" : var2;
   }

   public String G() {
      return this.w(0);
   }

   public int F() {
      return this.b.getValue().intValue();
   }

   public String h() {
      return this.e;
   }

   public boolean F() {
      return this.s.getValue();
   }

   public Double e(String var1) {
      lc var2 = this.l.get(var1);
      return var2 != null && !(var2.getValue() < 0.0) ? var2.getValue() : null;
   }

   public String v() {
      return this.J;
   }

   public void z(String var1) {
      this.J = var1 == null ? "" : var1.trim();
   }

   public String k() {
      return this.e;
   }

   public void N(String var1) {
      this.e = var1 == null ? "§dSpawner" : var1;
      this.o();
   }

   public void o() {
      if (!this.r) {
         JsonObject var1 = new JsonObject();
         var1.addProperty("itemName", this.e);
         var1.addProperty("apiKey", this.J);

         for (mb var5 : mb.values()) {
            var1.addProperty(var5.name() + "Block", this.d[var5.ordinal()].getValue());
         }

         JsonObject var7 = new JsonObject();
         this.l.forEach((var1x, var2) -> var7.addProperty(var1x, var2.getValue()));
         var1.add("prices", var7);

         try {
            pi.U(this.x(), var1.toString());
         } catch (IOException var6) {
            r4.r("FakeSpawner", "Local preference save failed", var6);
         }
      }
   }

   public void A() {
      if (Files.exists(this.x())) {
         this.r = (boolean)1;

         try {
            JsonObject var1 = pi.Y(this.x());

            for (lp var5 : this.d) {
               var5.setValue("None");
            }

            for (mb var15 : mb.values()) {
               String var6 = var15.name() + "Block";
               if (var1.has(var6)) {
                  this.d[var15.ordinal()].setValue(var1.get(var6).getAsString());
               } else {
                  this.d[var15.ordinal()].x();
               }
            }

            if (var1.has("prices")) {
               this.l.forEach((var1x, var2) -> {
                  if (var1.getAsJsonObject("prices").has(var1x)) {
                     var2.C(var1.getAsJsonObject("prices").get(var1x).getAsDouble());
                  }
               });
            }

            if (var1.has("itemName")) {
               this.e = var1.get("itemName").getAsString();
            }

            if (var1.has("apiKey")) {
               this.J = var1.get("apiKey").getAsString();
            }
         } catch (Exception var10) {
            r4.r("FakeSpawner", "Local preferences unreadable; valid values retained and file preserved", var10);
         } finally {
            this.r = (boolean)0;
         }
      }
   }

   public void g() {
      this.r = (boolean)1;

      try {
         for (lp var4 : this.d) {
            var4.setValue("None");
         }

         for (lp var13 : this.d) {
            var13.x();
         }

         for (lc var11 : this.l.values()) {
            var11.x();
         }
      } finally {
         this.r = (boolean)0;
      }

      this.o();
   }

   public List<l6<?>> K() {
      return List.of(this.d);
   }

   public List<l6<?>> g() {
      return List.of(this.b, this.s);
   }

   public List<l6<?>> z() {
      return List.copyOf(this.l.values());
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.k, ld.c);
   }

   @Override
   public void onEnable() {
      this.A();
      com.larpclient.lj.j().L();
      r4.V("FakeSpawner", "Active: {} / {} / {}; withdrawal purchases validate raw server lore", this.w(0), this.w(1), this.w(2));
      r4.g(
         "FakeSpawner",
         "Inventory clicks predict against real backing reservations; compact block choices show native icons and retain saved legacy selections"
      );
   }

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      jq.X();
      com.larpclient.lj.j().L();
      fv.b(var1);
      jy.G();
      mx.C();
      ul.K();
      ff.y();
      com.larpclient.l7.q().n();
   }

   @Override
   public void onDisable() {
      fv.H();
      com.larpclient.lj.j().c();
      mx.B();
      ff.C();
      ul.f();
      r4.V("FakeSpawner", "Runtime released; saved piles retained for matching world/player restoration");
   }
}
