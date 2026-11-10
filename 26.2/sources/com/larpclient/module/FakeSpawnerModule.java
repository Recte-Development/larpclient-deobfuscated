package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.b8;
import com.larpclient.bo;
import com.larpclient.kb;
import com.larpclient.ki;
import com.larpclient.l7;
import com.larpclient.ll;
import com.larpclient.mm;
import com.larpclient.sc;
import com.larpclient.t4;
import com.larpclient.za;
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
   private static final List<String> l = List.of(
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
   private final l[] S = new l[3];
   public final e Q = this.addSetting(new e("Preview Count", "Count for a standalone menu preview", 1.0, 1.0, 5000.0, 1.0));
   public final s H = this.addSetting(new s("Enchant Glint", "Show an enchantment glint on spawner items", false));
   private final Map<String, e> F = new LinkedHashMap<>();
   private boolean s;
   private String k = "§dSpawner";
   private String I = "";
   private final Path O;

   private Path s() {
      return this.O != null ? this.O : FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-local.json");
   }

   public FakeSpawnerModule() {
      this(null);
   }

   public FakeSpawnerModule(Path var1) {
      super("Fake Spawner", "Show blocks as spawners; loot buys real items via /shop", Category.PLAYER, -1);
      this.O = var1;
      t = this;
      String[] var2 = I();

      for (final ll var6 : ll.values()) {
         this.S[var6.ordinal()] = new l(
            var6.n + " Block", "Use a different ordinary block for each mob type", var6 == ll.o ? "minecraft:cobblestone" : "None", var2
         ) {
            private final Map<String, ItemStack> U = new HashMap<>();
            private boolean B;

            @Override
            public String getDisplayName() {
               return switch (var6) {
                  case o -> "Skeleton";
                  case C -> "Iron";
                  case f -> "Piglin";
               };
            }

            @Override
            public List<String> getVisibleModes() {
               if (FakeSpawnerModule.l.contains(this.getValue())) {
                  return FakeSpawnerModule.l;
               }

               ArrayList var1x = new ArrayList<>(FakeSpawnerModule.l);
               var1x.add(this.getValue());
               return List.copyOf(var1x);
            }

            @Override
            public ItemStack getModeIcon(String var1) {
               if (var1.equals("None")) {
                  return ItemStack.EMPTY;
               }

               Item var2x = b8.C(var1);
               if (!var2x.builtInRegistryHolder().areComponentsBound()) {
                  this.U.clear();
                  if (!this.B) {
                     com.larpclient.h.U("FakeSpawner", "Block icons deferred until item components are bound; text selection remains available");
                  }

                  this.B = (boolean)1;
                  return ItemStack.EMPTY;
               } else {
                  if (this.B) {
                     com.larpclient.h.U("FakeSpawner", "Item components ready; block icons resumed");
                  }

                  this.B = (boolean)0;
                  return this.U.computeIfAbsent(var1, var1x -> var2.getDefaultInstance());
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
                  for (l var5 : FakeSpawnerModule.this.S) {
                     if (var5 != null && var5 != this && var5.getValue().equals(var1)) {
                        throw new IllegalArgumentException("Each spawner type needs a different backing block");
                     }
                  }
               }

               super.setValue(var1);
            }
         };
         this.S[var6.ordinal()].o(var1x -> this.w());
      }

      for (String var8 : List.of("bone", "arrow", "iron_ingot", "rotten_flesh", "gold_nugget")) {
         e var9 = new e(var8.replace('_', ' ') + " Unit Price", "Cosmetic worth per item; -1 uses server order/API prices", -1.0, -1.0, 1.0E12, 0.01);
         var9.o(var1x -> this.w());
         this.F.put("minecraft:" + var8, var9);
      }

      if (var1 != null) {
         this.L();
      }
   }

   private static String[] I() {
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

   public static FakeSpawnerModule M() {
      return t;
   }

   public String Y(int var1) {
      String var2 = this.S[var1].getValue();
      return var2.equals("None") ? "" : var2;
   }

   public String F() {
      return this.Y(0);
   }

   public int x() {
      return this.Q.getValue().intValue();
   }

   public String w() {
      return this.k;
   }

   public boolean w() {
      return this.H.getValue();
   }

   public Double e(String var1) {
      e var2 = this.F.get(var1);
      return var2 != null && !(var2.getValue() < 0.0) ? var2.getValue() : null;
   }

   public String S() {
      return this.I;
   }

   public void F(String var1) {
      this.I = var1 == null ? "" : var1.trim();
   }

   public String L() {
      return this.k;
   }

   public void Z(String var1) {
      this.k = var1 == null ? "§dSpawner" : var1;
      this.w();
   }

   public void w() {
      if (!this.s) {
         JsonObject var1 = new JsonObject();
         var1.addProperty("itemName", this.k);
         var1.addProperty("apiKey", this.I);

         for (ll var5 : ll.values()) {
            var1.addProperty(var5.name() + "Block", this.S[var5.ordinal()].getValue());
         }

         JsonObject var7 = new JsonObject();
         this.F.forEach((var1x, var2) -> var7.addProperty(var1x, var2.getValue()));
         var1.add("prices", var7);

         try {
            l7.O(this.s(), var1.toString());
         } catch (IOException var6) {
            com.larpclient.h.m("FakeSpawner", "Local preference save failed", var6);
         }
      }
   }

   public void L() {
      if (Files.exists(this.s())) {
         this.s = (boolean)1;

         try {
            JsonObject var1 = l7.k(this.s());

            for (l var5 : this.S) {
               var5.setValue("None");
            }

            for (ll var15 : ll.values()) {
               String var6 = var15.name() + "Block";
               if (var1.has(var6)) {
                  this.S[var15.ordinal()].setValue(var1.get(var6).getAsString());
               } else {
                  this.S[var15.ordinal()].u();
               }
            }

            if (var1.has("prices")) {
               this.F.forEach((var1x, var2) -> {
                  if (var1.getAsJsonObject("prices").has(var1x)) {
                     var2.X(var1.getAsJsonObject("prices").get(var1x).getAsDouble());
                  }
               });
            }

            if (var1.has("itemName")) {
               this.k = var1.get("itemName").getAsString();
            }

            if (var1.has("apiKey")) {
               this.I = var1.get("apiKey").getAsString();
            }
         } catch (Exception var10) {
            com.larpclient.h.m("FakeSpawner", "Local preferences unreadable; valid values retained and file preserved", var10);
         } finally {
            this.s = (boolean)0;
         }
      }
   }

   public void s() {
      this.s = (boolean)1;

      try {
         for (l var4 : this.S) {
            var4.setValue("None");
         }

         for (l var13 : this.S) {
            var13.u();
         }

         for (e var11 : this.F.values()) {
            var11.u();
         }
      } finally {
         this.s = (boolean)0;
      }

      this.w();
   }

   public List<q<?>> S() {
      return List.of(this.S);
   }

   public List<q<?>> R() {
      return List.of(this.Q, this.H);
   }

   public List<q<?>> o() {
      return List.copyOf(this.F.values());
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.k, m.U);
   }

   @Override
   public void onEnable() {
      this.L();
      b8.k().l();
      com.larpclient.h.A("FakeSpawner", "Active: {} / {} / {}; withdrawal purchases validate raw server lore", this.Y(0), this.Y(1), this.Y(2));
      com.larpclient.h.U(
         "FakeSpawner",
         "Inventory clicks predict against real backing reservations; compact block choices show native icons and retain saved legacy selections"
      );
   }

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      sc.r();
      b8.k().l();
      mm.N(var1);
      bo.b();
      ki.I();
      za.n();
      t4.T();
      kb.h().h();
   }

   @Override
   public void onDisable() {
      mm.S();
      b8.k().m();
      ki.X();
      t4.c();
      za.M();
      com.larpclient.h.A("FakeSpawner", "Runtime released; saved piles retained for matching world/player restoration");
   }
}
