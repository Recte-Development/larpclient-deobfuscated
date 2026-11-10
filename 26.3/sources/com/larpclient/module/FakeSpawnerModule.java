package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient._8;
import com.larpclient._q;
import com.larpclient._v;
import com.larpclient.ds;
import com.larpclient.f9;
import com.larpclient.h0;
import com.larpclient.h4;
import com.larpclient.jn;
import com.larpclient.ni;
import com.larpclient.qb;
import com.larpclient.qw;
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
   private static final List<String> o = List.of(
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
   private static FakeSpawnerModule t;
   private final o[] W = new o[3];
   public final b O = this.addSetting(new b("Preview Count", "Count for a standalone menu preview", 1.0, 1.0, 5000.0, 1.0));
   public final m w = this.addSetting(new m("Enchant Glint", "Show an enchantment glint on spawner items", false));
   private final Map<String, b> z = new LinkedHashMap<>();
   private boolean M;
   private String g = "§dSpawner";
   private String L = "";
   private final Path c;

   private Path x() {
      return this.c != null ? this.c : FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-local.json");
   }

   public FakeSpawnerModule() {
      this(null);
   }

   public FakeSpawnerModule(Path var1) {
      super("Fake Spawner", "Show blocks as spawners; loot buys real items via /shop", Category.PLAYER, -1);
      this.c = var1;
      t = this;
      String[] var2 = b();

      for (final ni var6 : ni.values()) {
         this.W[var6.ordinal()] = new o(
            var6.N + " Block", "Use a different ordinary block for each mob type", var6 == ni.X ? "minecraft:cobblestone" : "None", var2
         ) {
            private final Map<String, ItemStack> l = new HashMap<>();
            private boolean h;

            @Override
            public String getDisplayName() {
               return switch (var6) {
                  case X -> "Skeleton";
                  case F -> "Iron";
                  case j -> "Piglin";
               };
            }

            @Override
            public List<String> getVisibleModes() {
               if (FakeSpawnerModule.o.contains(this.getValue())) {
                  return FakeSpawnerModule.o;
               }

               ArrayList var1x = new ArrayList<>(FakeSpawnerModule.o);
               var1x.add(this.getValue());
               return List.copyOf(var1x);
            }

            @Override
            public ItemStack getModeIcon(String var1) {
               if (var1.equals("None")) {
                  return ItemStack.EMPTY;
               }

               Item var2x = jn.Y(var1);
               if (!var2x.builtInRegistryHolder().areComponentsBound()) {
                  this.l.clear();
                  if (!this.h) {
                     _q.c("FakeSpawner", "Block icons deferred until item components are bound; text selection remains available");
                  }

                  this.h = (boolean)1;
                  return ItemStack.EMPTY;
               } else {
                  if (this.h) {
                     _q.c("FakeSpawner", "Item components ready; block icons resumed");
                  }

                  this.h = (boolean)0;
                  return this.l.computeIfAbsent(var1, var1x -> var2.getDefaultInstance());
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
                  for (o var5 : FakeSpawnerModule.this.W) {
                     if (var5 != null && var5 != this && var5.getValue().equals(var1)) {
                        throw new IllegalArgumentException("Each spawner type needs a different backing block");
                     }
                  }
               }

               super.setValue(var1);
            }
         };
         this.W[var6.ordinal()].w(var1x -> this.G());
      }

      for (String var8 : List.of("bone", "arrow", "iron_ingot", "rotten_flesh", "gold_nugget")) {
         b var9 = new b(var8.replace('_', ' ') + " Unit Price", "Cosmetic worth per item; -1 uses server order/API prices", -1.0, -1.0, 1.0E12, 0.01);
         var9.w(var1x -> this.G());
         this.z.put("minecraft:" + var8, var9);
      }

      if (var1 != null) {
         this.R();
      }
   }

   private static String[] b() {
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

   public static FakeSpawnerModule O() {
      return t;
   }

   public String I(int var1) {
      String var2 = this.W[var1].getValue();
      return var2.equals("None") ? "" : var2;
   }

   public String B() {
      return this.I(0);
   }

   public int l() {
      return this.O.getValue().intValue();
   }

   public String Q() {
      return this.g;
   }

   public boolean m() {
      return this.w.getValue();
   }

   public Double Q(String var1) {
      b var2 = this.z.get(var1);
      return var2 != null && !(var2.getValue() < 0.0) ? var2.getValue() : null;
   }

   public String l() {
      return this.L;
   }

   public void y(String var1) {
      this.L = var1 == null ? "" : var1.trim();
   }

   public String g() {
      return this.g;
   }

   public void r(String var1) {
      this.g = var1 == null ? "§dSpawner" : var1;
      this.G();
   }

   public void G() {
      if (!this.M) {
         JsonObject var1 = new JsonObject();
         var1.addProperty("itemName", this.g);
         var1.addProperty("apiKey", this.L);

         for (ni var5 : ni.values()) {
            var1.addProperty(var5.name() + "Block", this.W[var5.ordinal()].getValue());
         }

         JsonObject var7 = new JsonObject();
         this.z.forEach((var1x, var2) -> var7.addProperty(var1x, var2.getValue()));
         var1.add("prices", var7);

         try {
            ds.q(this.x(), var1.toString());
         } catch (IOException var6) {
            _q.o("FakeSpawner", "Local preference save failed", var6);
         }
      }
   }

   public void R() {
      if (Files.exists(this.x())) {
         this.M = (boolean)1;

         try {
            JsonObject var1 = ds.E(this.x());

            for (o var5 : this.W) {
               var5.setValue("None");
            }

            for (ni var15 : ni.values()) {
               String var6 = var15.name() + "Block";
               if (var1.has(var6)) {
                  this.W[var15.ordinal()].setValue(var1.get(var6).getAsString());
               } else {
                  this.W[var15.ordinal()].u();
               }
            }

            if (var1.has("prices")) {
               this.z.forEach((var1x, var2) -> {
                  if (var1.getAsJsonObject("prices").has(var1x)) {
                     var2.O(var1.getAsJsonObject("prices").get(var1x).getAsDouble());
                  }
               });
            }

            if (var1.has("itemName")) {
               this.g = var1.get("itemName").getAsString();
            }

            if (var1.has("apiKey")) {
               this.L = var1.get("apiKey").getAsString();
            }
         } catch (Exception var10) {
            _q.o("FakeSpawner", "Local preferences unreadable; valid values retained and file preserved", var10);
         } finally {
            this.M = (boolean)0;
         }
      }
   }

   public void A() {
      this.M = (boolean)1;

      try {
         for (o var4 : this.W) {
            var4.setValue("None");
         }

         for (o var13 : this.W) {
            var13.u();
         }

         for (b var11 : this.z.values()) {
            var11.u();
         }
      } finally {
         this.M = (boolean)0;
      }

      this.G();
   }

   public List<r<?>> c() {
      return List.of(this.W);
   }

   public List<r<?>> h() {
      return List.of(this.O, this.w);
   }

   public List<r<?>> W() {
      return List.copyOf(this.z.values());
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.E, l.k);
   }

   @Override
   public void onEnable() {
      this.R();
      jn.g().N();
      _q.z("FakeSpawner", "Active: {} / {} / {}; withdrawal purchases validate raw server lore", this.I(0), this.I(1), this.I(2));
      _q.c(
         "FakeSpawner",
         "Inventory clicks predict against real backing reservations; compact block choices show native icons and retain saved legacy selections"
      );
   }

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      f9.Z();
      jn.g().N();
      _v.h(var1);
      qb.z();
      _8.j();
      h4.L();
      qw.A();
      h0.u().m();
   }

   @Override
   public void onDisable() {
      _v.T();
      jn.g().Q();
      _8.P();
      qw.M();
      h4.v();
      _q.z("FakeSpawner", "Runtime released; saved piles retained for matching world/player restoration");
   }
}
