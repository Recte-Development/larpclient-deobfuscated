package com.larpclient.module;

import com.google.gson.JsonObject;
import com.larpclient.cz;
import com.larpclient.ie;
import com.larpclient.iv;
import com.larpclient.iy;
import com.larpclient.q8;
import com.larpclient.qh;
import com.larpclient.qu;
import com.larpclient.s3;
import com.larpclient.ts;
import com.larpclient.u2;
import com.larpclient.yp;
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
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2343;
import net.minecraft.class_310;
import net.minecraft.class_7923;

public final class FakeSpawnerModule extends Module {
   private static final List<String> B = List.of(
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
   private static FakeSpawnerModule u;
   private final li[] l = new li[3];
   public final lo y = this.addSetting(new lo("Preview Count", "Count for a standalone menu preview", 1.0, 1.0, 5000.0, 1.0));
   public final l3 k = this.addSetting(new l3("Enchant Glint", "Show an enchantment glint on spawner items", false));
   private final Map<String, lo> R = new LinkedHashMap<>();
   private boolean O;
   private String x = "§dSpawner";
   private String D = "";
   private final Path a;

   private Path H() {
      return this.a != null ? this.a : FabricLoader.getInstance().getConfigDir().resolve("larp-client/fake-spawner-local.json");
   }

   public FakeSpawnerModule() {
      this(null);
   }

   public FakeSpawnerModule(Path var1) {
      super("Fake Spawner", "Show blocks as spawners; loot buys real items via /shop", Category.PLAYER, -1);
      this.a = var1;
      u = this;
      String[] var2 = C();

      for (final qh var6 : qh.values()) {
         this.l[var6.ordinal()] = new li(
            var6.g + " Block", "Use a different ordinary block for each mob type", var6 == qh.x ? "minecraft:cobblestone" : "None", var2
         ) {
            private final Map<String, class_1799> z = new HashMap<>();

            @Override
            public String getDisplayName() {
               return switch (var6) {
                  case x -> "Skeleton";
                  case v -> "Iron";
                  case B -> "Piglin";
               };
            }

            @Override
            public List<String> getVisibleModes() {
               if (FakeSpawnerModule.B.contains(this.getValue())) {
                  return FakeSpawnerModule.B;
               }

               ArrayList var1x = new ArrayList<>(FakeSpawnerModule.B);
               var1x.add(this.getValue());
               return List.copyOf(var1x);
            }

            @Override
            public class_1799 getModeIcon(String var1) {
               return var1.equals("None") ? class_1799.field_8037 : this.z.computeIfAbsent(var1, var1x -> var0.method_7854());
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
                  for (li var5 : FakeSpawnerModule.this.l) {
                     if (var5 != null && var5 != this && var5.getValue().equals(var1)) {
                        throw new IllegalArgumentException("Each spawner type needs a different backing block");
                     }
                  }
               }

               super.setValue(var1);
            }
         };
         this.l[var6.ordinal()].a(var1x -> this.b());
      }

      for (String var8 : List.of("bone", "arrow", "iron_ingot", "rotten_flesh", "gold_nugget")) {
         lo var9 = new lo(var8.replace('_', ' ') + " Unit Price", "Cosmetic worth per item; -1 uses server order/API prices", -1.0, -1.0, 1.0E12, 0.01);
         var9.a(var1x -> this.b());
         this.R.put("minecraft:" + var8, var9);
      }

      if (var1 != null) {
         this.O();
      }
   }

   private static String[] C() {
      ArrayList var0 = new ArrayList();
      var0.add("None");
      class_7923.field_41178
         .method_10220()
         .filter(
            var0x -> var0x instanceof class_1747 var1
               && !(var1.method_7711() instanceof class_2343)
               && var0x != class_1802.field_8849
               && !var1.method_7711().method_9564().method_26215()
         )
         .map(var0x -> class_7923.field_41178.method_10221(var0x).toString())
         .sorted()
         .forEach(var0::add);
      return var0.toArray(String[]::new);
   }

   public static FakeSpawnerModule i() {
      return u;
   }

   public String y(int var1) {
      String var2 = this.l[var1].getValue();
      return var2.equals("None") ? "" : var2;
   }

   public String x() {
      return this.y(0);
   }

   public int B() {
      return this.y.getValue().intValue();
   }

   public String H() {
      return this.x;
   }

   public boolean x() {
      return this.k.getValue();
   }

   public Double E(String var1) {
      lo var2 = this.R.get(var1);
      return var2 != null && !(var2.getValue() < 0.0) ? var2.getValue() : null;
   }

   public String w() {
      return this.D;
   }

   public void e(String var1) {
      this.D = var1 == null ? "" : var1.trim();
   }

   public String p() {
      return this.x;
   }

   public void K(String var1) {
      this.x = var1 == null ? "§dSpawner" : var1;
      this.b();
   }

   public void b() {
      if (!this.O) {
         JsonObject var1 = new JsonObject();
         var1.addProperty("itemName", this.x);
         var1.addProperty("apiKey", this.D);

         for (qh var5 : qh.values()) {
            var1.addProperty(var5.name() + "Block", this.l[var5.ordinal()].getValue());
         }

         JsonObject var7 = new JsonObject();
         this.R.forEach((var1x, var2) -> var7.addProperty(var1x, var2.getValue()));
         var1.add("prices", var7);

         try {
            qu.d(this.H(), var1.toString());
         } catch (IOException var6) {
            q8.u("FakeSpawner", "Local preference save failed", var6);
         }
      }
   }

   public void O() {
      if (Files.exists(this.H())) {
         this.O = (boolean)1;

         try {
            JsonObject var1 = qu.v(this.H());

            for (li var5 : this.l) {
               var5.setValue("None");
            }

            for (qh var15 : qh.values()) {
               String var6 = var15.name() + "Block";
               if (var1.has(var6)) {
                  this.l[var15.ordinal()].setValue(var1.get(var6).getAsString());
               } else {
                  this.l[var15.ordinal()].T();
               }
            }

            if (var1.has("prices")) {
               this.R.forEach((var1x, var2) -> {
                  if (var1.getAsJsonObject("prices").has(var1x)) {
                     var2.q(var1.getAsJsonObject("prices").get(var1x).getAsDouble());
                  }
               });
            }

            if (var1.has("itemName")) {
               this.x = var1.get("itemName").getAsString();
            }

            if (var1.has("apiKey")) {
               this.D = var1.get("apiKey").getAsString();
            }
         } catch (Exception var10) {
            q8.u("FakeSpawner", "Local preferences unreadable; valid values retained and file preserved", var10);
         } finally {
            this.O = (boolean)0;
         }
      }
   }

   public void P() {
      this.O = (boolean)1;

      try {
         for (li var4 : this.l) {
            var4.setValue("None");
         }

         for (li var13 : this.l) {
            var13.T();
         }

         for (lo var11 : this.R.values()) {
            var11.T();
         }
      } finally {
         this.O = (boolean)0;
      }

      this.b();
   }

   public List<lb<?>> W() {
      return List.of(this.l);
   }

   public List<lb<?>> H() {
      return List.of(this.y, this.k);
   }

   public List<lb<?>> p() {
      return List.copyOf(this.R.values());
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.E, l5.F);
   }

   @Override
   public void onEnable() {
      this.O();
      cz.T().m();
      q8.G("FakeSpawner", "Active: {} / {} / {}; withdrawal purchases validate raw server lore", this.y(0), this.y(1), this.y(2));
      q8.v(
         "FakeSpawner",
         "Inventory clicks predict against real backing reservations; compact block choices show native icons and retain saved legacy selections"
      );
   }

   @Override
   public void onTick() {
      class_310 var1 = getMc();
      ts.v();
      cz.T().m();
      iv.S(var1);
      u2.f();
      iy.d();
      yp.g();
      ie.R();
      s3.T().L();
   }

   @Override
   public void onDisable() {
      iv.b();
      cz.T().R();
      iy.j();
      ie.N();
      yp.z();
      q8.G("FakeSpawner", "Runtime released; saved piles retained for matching world/player restoration");
   }
}
