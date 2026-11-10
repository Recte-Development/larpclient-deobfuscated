package com.larpclient.module;

import com.larpclient.uh;
import com.larpclient.z4;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class XRayModule extends Module {
   private static volatile XRayModule k;
   private static final Map<Block, Integer> t = new HashMap<>();
   private static final Map<Block, String> Y = new LinkedHashMap<>();
   private static final String T = "Base Blocks";
   private static final int x = 32;
   private static volatile boolean u = (boolean)0;
   private static volatile String N = "Both";
   private static volatile Set<Block> i = Set.of();
   private static volatile boolean E;
   private final l v = this.addSetting(new l("Mode", "XRay rendering mode", "Both", "See Through", "ESP Outline", "Both"));
   private final e R = this.addSetting(new e("Range", "Scan radius", 32.0, 8.0, 64.0, 4.0));
   private final Map<String, s> F = new LinkedHashMap<>();
   private final z4 r;
   private final s Q;
   private final s K;
   private final l U;
   private final e H;
   private final List<XRayModule.r> p;
   private final PriorityQueue<XRayModule.r> S;
   private Set<Block> c;
   private int A;
   private boolean z;
   private BlockPos j;
   private int s;
   private int G;
   private int a;
   private ClientLevel J;

   private static void W(String var0, int var1, Block... var2) {
      for (Block var6 : var2) {
         t.put(var6, var1);
         Y.put(var6, var0);
      }
   }

   public XRayModule() {
      super("XRay", "Highlight ores through blocks", Category.RENDER, -1);
      XRayModule var1 = this;
      this.r = new z4(var1x -> var1.addSetting(var1x));
      this.Q = this.addSetting(new s("Ore Colors", "Color each marker by what it is", true));
      this.K = this.addSetting(new s("Labels", "Name, vein size and distance above each vein", true));
      this.U = this.addSetting(new l("Overlay Style", "Ore marker shape", "Box", "Box", "Corners", "Filled"));
      this.H = this.addSetting(new e("Max Ores", "Nearest ore markers retained", 512.0, 64.0, 2048.0, 64.0));
      this.p = new ArrayList<>();
      this.S = new PriorityQueue<>(Comparator.comparingDouble(XRayModule.r::distanceSquared).reversed());
      this.c = Set.of();
      this.A = -1;
      Set var4 = Set.of("Coal", "Copper", "Quartz");

      for (String var3 : new LinkedHashSet<>(Y.values())) {
         this.F.put(var3, this.addSetting(new s(var3, "Show " + var3.toLowerCase(Locale.ROOT), !var4.contains(var3))));
      }

      BooleanSupplier var5 = () -> !this.v.O("See Through");
      this.R.P(var5);
      this.H.P(var5);
      this.U.P(var5);
      this.Q.P(var5);
      this.K.P(var5);
      this.r.s(() -> var5.getAsBoolean() && !this.Q.getValue(), var5, var5, () -> var5.getAsBoolean() && this.U.O("Filled"));
      this.o();
      LevelRenderEvents.BEFORE_GIZMOS.register(this::Q);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> L());
      k = this;
   }

   @Override
   public void onEnable() {
      N = this.v.getValue();
      this.o();
      u = (boolean)1;
      L();
      this.s();
      if (f()) {
         this.i();
      }

      String var1 = FabricLoader.getInstance()
         .getModContainer("sodium")
         .map(var0 -> "Sodium " + var0.getMetadata().getVersion().getFriendlyString())
         .orElse("vanilla");
      com.larpclient.h.A("XRay", "Enabled in {} mode, {} block types shown, {} terrain", N, i.size(), var1);
   }

   public List<q<?>> i() {
      return List.of(this.v, this.U, this.R);
   }

   public List<q<?>> N() {
      return List.copyOf(this.F.values());
   }

   public List<q<?>> J() {
      ArrayList var1 = new ArrayList<>(List.of(this.Q, this.K));
      var1.addAll(this.r.f());
      var1.addAll(this.r.O());
      var1.addAll(this.r.p());
      return List.copyOf(var1);
   }

   public List<q<?>> r() {
      return List.of(this.H);
   }

   @Override
   public void onDisable() {
      u = (boolean)0;
      L();
      this.s();
      this.i();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.v.getValue();
         byte var2 = f();
         boolean var3 = this.o();
         int var10000 = var1.equals(N);
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            N = var1;
            this.s();
         }

         label31: {
            byte var4 = f();
            S((boolean)var4);
            byte var8 = var4;
            byte var7 = var2;
            var10000 = Integer.compare(var7, var8);
            if ((var10000 | -var10000) >>> 31 == 0) {
               if (!var3) {
                  break label31;
               }

               boolean var10 = false;
               byte var9 = var4;
               var10000 = Integer.signum(var9);
               if (((var10000 | -var10000) >>> 31 ^ 1) != 0) {
                  break label31;
               }
            }

            com.larpclient.h.U("XRay", "Rebuilding terrain: mode {}, filters changed {}", N, var3);
            this.i();
         }

         int var16 = D();
         boolean var12 = false;
         byte var11 = (byte)var16;
         var16 = Integer.signum(var11);
         if (((var16 | -var16) >>> 31 ^ 1) == 0) {
            this.y();
         } else if (!this.p.isEmpty() || this.j != null) {
            this.s();
         }
      }
   }

   private boolean o() {
      int var1 = 0;
      int var2 = 0;

      for (s var4 : this.F.values()) {
         if (var4.getValue()) {
            var1 |= 1 << var2;
         }

         var2++;
      }

      if (var1 == this.A) {
         return false;
      }

      this.A = var1;
      HashSet var8 = new HashSet();
      HashSet var9 = new HashSet();

      for (Block var6 : t.keySet()) {
         String var7 = Y.get(var6);
         if (var7 == null) {
            var8.add(var6);
         } else if (this.F.get(var7).getValue()) {
            var8.add(var6);
            var9.add(var6);
         }
      }

      i = Set.copyOf(var8);
      this.c = Set.copyOf(var9);
      this.s();
      return true;
   }

   private void s() {
      this.p.clear();
      this.S.clear();
      this.j = null;
      this.J = null;
      this.G = this.a = 0;
      this.z = (boolean)0;
   }

   private void y() {
      int var1 = this.R.getValue().intValue();
      BlockPos var2 = mc.player.blockPosition();
      if (this.J != mc.level || this.j == null || this.s != var1 || this.j.distSqr(var2) > 64.0) {
         this.s();
         this.J = mc.level;
         this.j = var2;
         this.s = var1;
      }

      if (this.a > 0) {
         this.a--;
      } else {
         int var3 = var1 * 2 + 1;
         int var4 = var3 * var3 * var3;
         MutableBlockPos var5 = new MutableBlockPos();

         for (int var6 = 0; var6 < 8192 && this.G < var4; this.G++) {
            int var7 = this.G % var3;
            int var8 = this.G / var3 % var3;
            int var9 = this.G / (var3 * var3);
            int var10 = s(var9);
            int var11 = s(var7);
            int var12 = s(var8);
            if (var10 * var10 + var11 * var11 + var12 * var12 <= var1 * var1) {
               var5.set(this.j.getX() + var10, this.j.getY() + var11, this.j.getZ() + var12);
               LevelChunk var13 = mc.level.getChunkSource().getChunkNow(var5.getX() >> 4, var5.getZ() >> 4);
               if (var13 != null && var5.getY() >= mc.level.getMinY() && var5.getY() <= mc.level.getMaxY()) {
                  Block var14 = var13.getBlockState(var5).getBlock();
                  if (this.c.contains(var14)) {
                     int var15 = 0xFF000000 | t.get(var14);
                     this.S.add(new XRayModule.r(var5.immutable(), var14, var15, var5.distSqr(this.j), 0));

                     while (this.S.size() > this.H.getValue().intValue()) {
                        this.S.poll();
                     }
                  }
               }
            }

            var6++;
         }

         if (!this.z || this.G >= var4) {
            this.p.clear();
            this.p.addAll(this.S);
            this.p.sort(Comparator.comparingDouble(XRayModule.r::distanceSquared));
            this.k();
         }

         if (this.G >= var4) {
            this.z = (boolean)1;
            this.G = 0;
            this.S.clear();
            this.a = 10;
         }
      }
   }

   private void k() {
      HashMap var1 = new HashMap(this.p.size() * 2);

      for (int var2 = 0; var2 < this.p.size(); var2++) {
         var1.put(this.p.get(var2).pos(), var2);
      }

      boolean[] var13 = new boolean[this.p.size()];
      ArrayDeque var3 = new ArrayDeque();

      for (int var4 = 0; var4 < this.p.size(); var4++) {
         if (!var13[var4]) {
            var13[var4] = (boolean)1;
            XRayModule.r var5 = this.p.get(var4);
            String var6 = J(var5.block());
            int var7 = 0;
            var3.add(var4);

            while (!var3.isEmpty()) {
               BlockPos var8 = this.p.get((Integer)var3.poll()).pos();
               var7++;

               for (int var9 = -1; var9 <= 1; var9++) {
                  for (int var10 = -1; var10 <= 1; var10++) {
                     for (int var11 = -1; var11 <= 1; var11++) {
                        Integer var12 = (Integer)var1.get(var8.offset(var9, var10, var11));
                        if (var12 != null && !var13[var12] && J(this.p.get(var12).block()).equals(var6)) {
                           var13[var12] = (boolean)1;
                           var3.add(var12);
                        }
                     }
                  }
               }
            }

            this.p.set(var4, var5.withVein(var7));
         }
      }
   }

   private static String J(Block var0) {
      String var1 = Y.get(var0);
      return "Base Blocks".equals(var1) ? var0.getName().getString() : var1;
   }

   private static int s(int var0) {
      return var0 == 0 ? 0 : (var0 % 2 == 1 ? (var0 + 1) / 2 : -var0 / 2);
   }

   private void Q(LevelRenderContext var1) {
      if (this.isOperational() && this.isInGame() && D() && this.J == mc.level) {
         try {
            int var2 = 0;
            int var3 = 0;
            double var4 = this.R.getValue();
            Vec3 var6 = mc.player.position();

            for (XRayModule.r var8 : this.p) {
               BlockPos var9 = var8.pos();
               LevelChunk var10 = mc.level.getChunkSource().getChunkNow(var9.getX() >> 4, var9.getZ() >> 4);
               if (var10 != null && var10.getBlockState(var9).getBlock() == var8.block()) {
                  Vec3 var11 = Vec3.atCenterOf(var9);
                  double var12 = var6.distanceTo(var11);
                  if (!(var12 >= var4)) {
                     if (var2++ >= this.H.getValue().intValue()) {
                        break;
                     }

                     int var14 = this.Q.getValue() ? uh.F(var8.color(), z4.M(var12, var4)) : this.r.c(var8.color(), var12, var4, 1.0, var9.hashCode());
                     this.r.R(new AABB(var9).inflate(0.003), this.U.getValue(), var14);
                     if (this.K.getValue() && var8.vein() > 0 && var3 < 32 && var14 >>> 24 != 0) {
                        var3++;
                        String var15 = J(var8.block()) + (var8.vein() > 1 ? " x" + var8.vein() : "") + " · " + Math.round(var12) + "m";
                        this.r.d(var11.add(0.0, 0.85, 0.0), var15, var14);
                     }
                  }
               }
            }
         } catch (RuntimeException var16) {
            com.larpclient.h.w("ESP", "Ore overlay failed", var16);
         }
      }
   }

   private void i() {
      if (mc.level != null && mc.levelExtractor != null) {
         mc.levelExtractor.allChanged();
      }
   }

   private static void L() {
      S(f());
   }

   private static void S(boolean var0) {
      if (var0 != E) {
         E = var0;
         com.larpclient.h.U("XRay", "Terrain see-through {} for chunk rebuilds", var0 ? "admitted" : "withdrawn");
      }
   }

   public static boolean a() {
      return k != null && k.isEnabled() ? u : false;
   }

   public static boolean f() {
      return k != null && k.isEnabled() ? u && (N.equals("See Through") || N.equals("Both")) : false;
   }

   public static boolean z() {
      return E;
   }

   public static boolean D() {
      return k != null && k.isEnabled() ? u && (N.equals("ESP Outline") || N.equals("Both")) : false;
   }

   public static boolean Z(Block var0) {
      return i.contains(var0);
   }

   static {
      W("Diamond", 4910553, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE);
      W("Emerald", 1564002, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE);
      W("Gold", 16570170, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE);
      W("Iron", 14857359, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE);
      W("Redstone", 16724016, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE);
      W("Lapis", 4025343, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE);
      W("Copper", 15764043, Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE);
      W("Coal", 10790052, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE);
      W("Ancient Debris", 12607548, Blocks.ANCIENT_DEBRIS);
      W("Quartz", 16052198, Blocks.NETHER_QUARTZ_ORE);
      W("Base Blocks", 4910553, Blocks.DIAMOND_BLOCK);
      W("Base Blocks", 1564002, Blocks.EMERALD_BLOCK);
      W("Base Blocks", 16570170, Blocks.GOLD_BLOCK);
      W("Base Blocks", 15132390, Blocks.IRON_BLOCK);
      W("Base Blocks", 10259596, Blocks.NETHERITE_BLOCK);
      W("Base Blocks", 16757820, Blocks.CHEST);
      W("Base Blocks", 11554047, Blocks.ENDER_CHEST);
      W("Base Blocks", 16727160, Blocks.SPAWNER);
      W("Base Blocks", 16734810, Blocks.TNT);

      for (Block var3 : new Block[]{Blocks.LAVA, Blocks.WATER, Blocks.OBSIDIAN, Blocks.BEDROCK}) {
         t.put(var3, 16777215);
      }
   }

   private record r(BlockPos pos, Block block, int color, double distanceSquared, int vein) {
      XRayModule.r withVein(int var1) {
         return new XRayModule.r(this.pos, this.block, this.color, this.distanceSquared, var1);
      }
   }
}
