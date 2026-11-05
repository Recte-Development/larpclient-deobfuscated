package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.cl;
import com.larpclient.dr;
import com.larpclient.eg;
import com.larpclient.k9;
import com.larpclient.mt;
import com.larpclient.op;
import com.larpclient.t1;
import com.larpclient.u0;
import com.larpclient.wf;
import com.larpclient.x7;
import com.larpclient.z_;
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
   private static final List<String> x = List.of(
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
   private final d6[] a = new d6[3];
   public final d9 c = this.addSetting(new d9("Preview Count", "Count for a standalone menu preview", 1.0, 1.0, 5000.0, 1.0));
   public final dh N = this.addSetting(new dh("Enchant Glint", "Show an enchantment glint on spawner items", false));
   private final Map<String, d9> z = new LinkedHashMap<>();
   private boolean F;
   private String t = "§dSpawner";
   private String Y = "";
   private final Path S;

   private Path N() {
      return this.S != null ? this.S : FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-local.json");
   }

   public FakeSpawnerModule() {
      this(null);
   }

   public FakeSpawnerModule(Path var1) {
      super("Fake Spawner", "Show blocks as spawners; loot buys real items via /shop", Category.PLAYER, -1);
      this.S = var1;
      X = this;
      String[] var2 = w();

      for (final cl var6 : cl.values()) {
         this.a[var6.ordinal()] = new d6(
            var6.Z + " Block", "Use a different ordinary block for each mob type", var6 == cl.A ? "minecraft:cobblestone" : "None", var2
         ) {
            private final Map<String, ItemStack> s = new HashMap<>();
            private boolean K;

            @Override
            public String getDisplayName() {
               return switch (var6) {
                  case A -> "Skeleton";
                  case k -> "Iron";
                  case h -> "Piglin";
               };
            }

            @Override
            public List<String> getVisibleModes() {
               if (FakeSpawnerModule.x.contains(this.P())) {
                  return FakeSpawnerModule.x;
               }

               ArrayList var1x = new ArrayList<>(FakeSpawnerModule.x);
               var1x.add(this.P());
               return List.copyOf(var1x);
            }

            @Override
            public ItemStack getModeIcon(String var1) {
               if (var1.equals("None")) {
                  return ItemStack.EMPTY;
               }

               Item var2x = dr.E(var1);
               if (!var2x.builtInRegistryHolder().areComponentsBound()) {
                  this.s.clear();
                  if (!this.K) {
                     mt.U("FakeSpawner", "Block icons deferred until item components are bound; text selection remains available");
                  }

                  this.K = (boolean)1;
                  return ItemStack.EMPTY;
               } else {
                  if (this.K) {
                     mt.U("FakeSpawner", "Item components ready; block icons resumed");
                  }

                  this.K = (boolean)0;
                  return this.s.computeIfAbsent(var1, var1x -> var2.getDefaultInstance());
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
                  for (d6 var5 : FakeSpawnerModule.this.a) {
                     if (var5 != null && var5 != this && var5.P().equals(var1)) {
                        throw new IllegalArgumentException("Each spawner type needs a different backing block");
                     }
                  }
               }

               super.setValue(var1);
            }
         };
         this.a[var6.ordinal()].h(var1x -> this.A());
      }

      for (String var8 : List.of("bone", "arrow", "iron_ingot", "rotten_flesh", "gold_nugget")) {
         d9 var9 = new d9(var8.replace('_', ' ') + " Unit Price", "Cosmetic worth per item; -1 uses server order/API prices", -1.0, -1.0, 1.0E12, 0.01);
         var9.h(var1x -> this.A());
         this.z.put("minecraft:" + var8, var9);
      }

      if (var1 != null) {
         this.u();
      }
   }

   private static String[] w() {
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

   public static FakeSpawnerModule T() {
      return X;
   }

   public String x(int var1) {
      String var2 = this.a[var1].P();
      return var2.equals("None") ? "" : var2;
   }

   public String J() {
      return this.x(0);
   }

   public int m() {
      return this.c.P().intValue();
   }

   public String X() {
      return this.t;
   }

   public boolean d() {
      return this.N.P();
   }

   public Double G(String var1) {
      d9 var2 = this.z.get(var1);
      return var2 != null && !(var2.P() < 0.0) ? var2.P() : null;
   }

   public String j() {
      return this.Y;
   }

   public void H(String var1) {
      this.Y = var1 == null ? "" : var1.trim();
   }

   public String s() {
      return this.t;
   }

   public void B(String var1) {
      this.t = var1 == null ? "§dSpawner" : var1;
      this.A();
   }

   public void A() {
      if (!this.F) {
         JsonObject var1 = new JsonObject();
         var1.addProperty("itemName", this.t);
         var1.addProperty("apiKey", this.Y);

         for (cl var5 : cl.values()) {
            var1.addProperty(var5.name() + "Block", this.a[var5.ordinal()].P());
         }

         JsonObject var7 = new JsonObject();
         this.z.forEach((var1x, var2) -> var7.addProperty(var1x, var2.P()));
         var1.add("prices", var7);

         try {
            eg.o(this.N(), var1.toString());
         } catch (IOException var6) {
            mt.P("FakeSpawner", "Local preference save failed", var6);
         }
      }
   }

   public void u() {
      if (Files.exists(this.N())) {
         this.F = (boolean)1;

         try {
            JsonObject var1 = eg.J(this.N());

            for (d6 var5 : this.a) {
               var5.setValue("None");
            }

            for (cl var15 : cl.values()) {
               String var6 = var15.name() + "Block";
               if (var1.has(var6)) {
                  this.a[var15.ordinal()].setValue(var1.get(var6).getAsString());
               } else {
                  this.a[var15.ordinal()].B();
               }
            }

            if (var1.has("prices")) {
               this.z.forEach((var1x, var2) -> {
                  if (var1.getAsJsonObject("prices").has(var1x)) {
                     var2.o(var1.getAsJsonObject("prices").get(var1x).getAsDouble());
                  }
               });
            }

            if (var1.has("itemName")) {
               this.t = var1.get("itemName").getAsString();
            }

            if (var1.has("apiKey")) {
               this.Y = var1.get("apiKey").getAsString();
            }
         } catch (Exception var10) {
            mt.P("FakeSpawner", "Local preferences unreadable; valid values retained and file preserved", var10);
         } finally {
            this.F = (boolean)0;
         }
      }
   }

   public void V() {
      this.F = (boolean)1;

      try {
         for (d6 var4 : this.a) {
            var4.setValue("None");
         }

         for (d6 var13 : this.a) {
            var13.B();
         }

         for (d9 var11 : this.z.values()) {
            var11.B();
         }
      } finally {
         this.F = (boolean)0;
      }

      this.A();
   }

   public List<df<?>> Q() {
      return List.of(this.a);
   }

   public List<df<?>> s() {
      return List.of(this.c, this.N);
   }

   public List<df<?>> W() {
      return List.copyOf(this.z.values());
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.m, dm.j);
   }

   @Override
   public void onEnable() {
      this.u();
      dr.z().k();
      mt.K("FakeSpawner", "Active: {} / {} / {}; withdrawal purchases validate raw server lore", this.x(0), this.x(1), this.x(2));
      mt.U(
         "FakeSpawner",
         "Inventory clicks predict against real backing reservations; compact block choices show native icons and retain saved legacy selections"
      );
   }

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      wf.g();
      dr.z().k();
      z_.Z(var1);
      x7.t();
      u0.I();
      k9.c();
      t1.z();
      op.a().O();
   }

   @Override
   public void onDisable() {
      z_.f();
      dr.z().u();
      u0.u();
      t1.n();
      k9.F();
      mt.K("FakeSpawner", "Runtime released; saved piles retained for matching world/player restoration");
   }
}
