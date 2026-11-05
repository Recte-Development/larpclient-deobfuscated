package com.larpclient.module;

import com.larpclient.mi;
import com.larpclient.oh;
import com.larpclient.r4;
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
   private static volatile XRayModule j;
   private static final Map<Block, Integer> c = new HashMap<>();
   private static final Map<Block, String> a = new LinkedHashMap<>();
   private static final String w = "Base Blocks";
   private static final int z = 32;
   private static volatile boolean B = (boolean)0;
   private static volatile String r = "Both";
   private static volatile Set<Block> H = Set.of();
   private static volatile boolean I;
   private final lp p = this.addSetting(new lp("Mode", "XRay rendering mode", "Both", "See Through", "ESP Outline", "Both"));
   private final lc m = this.addSetting(new lc("Range", "Scan radius", 32.0, 8.0, 64.0, 4.0));
   private final Map<String, ln> e = new LinkedHashMap<>();
   private final mi V;
   private final ln Y;
   private final ln F;
   private final lp J;
   private final lc k;
   private final List<XRayModule.t> t;
   private final PriorityQueue<XRayModule.t> h;
   private Set<Block> f;
   private int q;
   private boolean M;
   private BlockPos E;
   private int d;
   private int O;
   private int W;
   private ClientLevel L;

   private static void D(String var0, int var1, Block... var2) {
      for (Block var6 : var2) {
         c.put(var6, var1);
         a.put(var6, var0);
      }
   }

   public XRayModule() {
      super("XRay", "Highlight ores through blocks", Category.RENDER, -1);
      XRayModule var1 = this;
      this.V = new mi(var1x -> var1.addSetting(var1x));
      this.Y = this.addSetting(new ln("Ore Colors", "Color each marker by what it is", true));
      this.F = this.addSetting(new ln("Labels", "Name, vein size and distance above each vein", true));
      this.J = this.addSetting(new lp("Overlay Style", "Ore marker shape", "Box", "Box", "Corners", "Filled"));
      this.k = this.addSetting(new lc("Max Ores", "Nearest ore markers retained", 512.0, 64.0, 2048.0, 64.0));
      this.t = new ArrayList<>();
      this.h = new PriorityQueue<>(Comparator.comparingDouble(XRayModule.t::distanceSquared).reversed());
      this.f = Set.of();
      this.q = -1;
      Set var4 = Set.of("Coal", "Copper", "Quartz");

      for (String var3 : new LinkedHashSet<>(a.values())) {
         this.e.put(var3, this.addSetting(new ln(var3, "Show " + var3.toLowerCase(Locale.ROOT), !var4.contains(var3))));
      }

      BooleanSupplier var5 = () -> !this.p.i("See Through");
      this.m.x(var5);
      this.k.x(var5);
      this.J.x(var5);
      this.Y.x(var5);
      this.F.x(var5);
      this.V.J(() -> var5.getAsBoolean() && !this.Y.getValue(), var5, var5, () -> var5.getAsBoolean() && this.J.i("Filled"));
      this.E();
      LevelRenderEvents.BEFORE_GIZMOS.register(this::i);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> u());
      j = this;
   }

   @Override
   public void onEnable() {
      r = this.p.getValue();
      this.E();
      B = (boolean)1;
      u();
      this.K();
      if (l()) {
         this.v();
      }

      String var1 = FabricLoader.getInstance()
         .getModContainer("sodium")
         .map(var0 -> "Sodium " + var0.getMetadata().getVersion().getFriendlyString())
         .orElse("vanilla");
      r4.V("XRay", "Enabled in {} mode, {} block types shown, {} terrain", r, H.size(), var1);
   }

   public List<l6<?>> N() {
      return List.of(this.p, this.J, this.m);
   }

   public List<l6<?>> L() {
      return List.copyOf(this.e.values());
   }

   public List<l6<?>> u() {
      ArrayList var1 = new ArrayList<>(List.of(this.Y, this.F));
      var1.addAll(this.V.t());
      var1.addAll(this.V.J());
      var1.addAll(this.V.Q());
      return List.copyOf(var1);
   }

   public List<l6<?>> C() {
      return List.of(this.k);
   }

   @Override
   public void onDisable() {
      B = (boolean)0;
      u();
      this.K();
      this.v();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.p.getValue();
         byte var2 = l();
         byte var3 = this.E();
         int var10000 = var1.equals(r);
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if ((var10000 | -var10000) >>> 31 == 0) {
            r = var1;
            this.K();
         }

         label31: {
            byte var4 = l();
            T((boolean)var4);
            byte var8 = var4;
            byte var7 = var2;
            var10000 = Integer.compare(var7, var8);
            if ((var10000 | -var10000) >>> 31 == 0) {
               boolean var10 = false;
               byte var9 = var3;
               var10000 = Integer.signum(var9);
               if (((var10000 | -var10000) >>> 31 ^ 1) != 0) {
                  break label31;
               }

               boolean var12 = false;
               byte var11 = var4;
               var10000 = Integer.signum(var11);
               if (((var10000 | -var10000) >>> 31 ^ 1) != 0) {
                  break label31;
               }
            }

            r4.g("XRay", "Rebuilding terrain: mode {}, filters changed {}", r, Boolean.valueOf((boolean)var3));
            this.v();
         }

         if (n()) {
            this.R();
         } else if (!this.t.isEmpty() || this.E != null) {
            this.K();
         }
      }
   }

   private boolean E() {
      int var1 = 0;
      int var2 = 0;

      for (ln var4 : this.e.values()) {
         if (var4.getValue()) {
            var1 |= 1 << var2;
         }

         var2++;
      }

      if (var1 == this.q) {
         return false;
      }

      this.q = var1;
      HashSet var8 = new HashSet();
      HashSet var9 = new HashSet();

      for (Block var6 : c.keySet()) {
         String var7 = a.get(var6);
         if (var7 == null) {
            var8.add(var6);
         } else if (this.e.get(var7).getValue()) {
            var8.add(var6);
            var9.add(var6);
         }
      }

      H = Set.copyOf(var8);
      this.f = Set.copyOf(var9);
      this.K();
      return true;
   }

   private void K() {
      this.t.clear();
      this.h.clear();
      this.E = null;
      this.L = null;
      this.O = this.W = 0;
      this.M = (boolean)0;
   }

   private void R() {
      int var1 = this.m.getValue().intValue();
      BlockPos var2 = mc.player.blockPosition();
      if (this.L != mc.level || this.E == null || this.d != var1 || this.E.distSqr(var2) > 64.0) {
         this.K();
         this.L = mc.level;
         this.E = var2;
         this.d = var1;
      }

      if (this.W > 0) {
         this.W--;
      } else {
         int var3 = var1 * 2 + 1;
         int var4 = var3 * var3 * var3;
         MutableBlockPos var5 = new MutableBlockPos();

         for (int var6 = 0; var6 < 8192 && this.O < var4; this.O++) {
            int var7 = this.O % var3;
            int var8 = this.O / var3 % var3;
            int var9 = this.O / (var3 * var3);
            int var10 = W(var9);
            int var11 = W(var7);
            int var12 = W(var8);
            if (var10 * var10 + var11 * var11 + var12 * var12 <= var1 * var1) {
               var5.set(this.E.getX() + var10, this.E.getY() + var11, this.E.getZ() + var12);
               LevelChunk var13 = mc.level.getChunkSource().getChunkNow(var5.getX() >> 4, var5.getZ() >> 4);
               if (var13 != null && var5.getY() >= mc.level.getMinY() && var5.getY() <= mc.level.getMaxY()) {
                  Block var14 = var13.getBlockState(var5).getBlock();
                  if (this.f.contains(var14)) {
                     int var15 = 0xFF000000 | c.get(var14);
                     this.h.add(new XRayModule.t(var5.immutable(), var14, var15, var5.distSqr(this.E), 0));

                     while (this.h.size() > this.k.getValue().intValue()) {
                        this.h.poll();
                     }
                  }
               }
            }

            var6++;
         }

         if (!this.M || this.O >= var4) {
            this.t.clear();
            this.t.addAll(this.h);
            this.t.sort(Comparator.comparingDouble(XRayModule.t::distanceSquared));
            this.m();
         }

         if (this.O >= var4) {
            this.M = (boolean)1;
            this.O = 0;
            this.h.clear();
            this.W = 10;
         }
      }
   }

   private void m() {
      HashMap var1 = new HashMap(this.t.size() * 2);

      for (int var2 = 0; var2 < this.t.size(); var2++) {
         var1.put(this.t.get(var2).pos(), var2);
      }

      boolean[] var13 = new boolean[this.t.size()];
      ArrayDeque var3 = new ArrayDeque();

      for (int var4 = 0; var4 < this.t.size(); var4++) {
         if (!var13[var4]) {
            var13[var4] = (boolean)1;
            XRayModule.t var5 = this.t.get(var4);
            String var6 = O(var5.block());
            int var7 = 0;
            var3.add(var4);

            while (!var3.isEmpty()) {
               BlockPos var8 = this.t.get((Integer)var3.poll()).pos();
               var7++;

               for (int var9 = -1; var9 <= 1; var9++) {
                  for (int var10 = -1; var10 <= 1; var10++) {
                     for (int var11 = -1; var11 <= 1; var11++) {
                        Integer var12 = (Integer)var1.get(var8.offset(var9, var10, var11));
                        if (var12 != null && !var13[var12] && O(this.t.get(var12).block()).equals(var6)) {
                           var13[var12] = (boolean)1;
                           var3.add(var12);
                        }
                     }
                  }
               }
            }

            this.t.set(var4, var5.withVein(var7));
         }
      }
   }

   private static String O(Block var0) {
      String var1 = a.get(var0);
      return "Base Blocks".equals(var1) ? var0.getName().getString() : var1;
   }

   private static int W(int var0) {
      return var0 == 0 ? 0 : (var0 % 2 == 1 ? (var0 + 1) / 2 : -var0 / 2);
   }

   private void i(LevelRenderContext var1) {
      if (this.isOperational() && this.isInGame() && n() && this.L == mc.level) {
         try {
            int var2 = 0;
            int var3 = 0;
            double var4 = this.m.getValue();
            Vec3 var6 = mc.player.position();

            for (XRayModule.t var8 : this.t) {
               BlockPos var9 = var8.pos();
               LevelChunk var10 = mc.level.getChunkSource().getChunkNow(var9.getX() >> 4, var9.getZ() >> 4);
               if (var10 != null && var10.getBlockState(var9).getBlock() == var8.block()) {
                  Vec3 var11 = Vec3.atCenterOf(var9);
                  double var12 = var6.distanceTo(var11);
                  if (!(var12 >= var4)) {
                     if (var2++ >= this.k.getValue().intValue()) {
                        break;
                     }

                     int var14 = this.Y.getValue() ? oh.S(var8.color(), mi.G(var12, var4)) : this.V.k(var8.color(), var12, var4, 1.0, var9.hashCode());
                     this.V.I(new AABB(var9).inflate(0.003), this.J.getValue(), var14);
                     if (this.F.getValue() && var8.vein() > 0 && var3 < 32 && var14 >>> 24 != 0) {
                        var3++;
                        String var15 = O(var8.block()) + (var8.vein() > 1 ? " x" + var8.vein() : "") + " · " + Math.round(var12) + "m";
                        this.V.t(var11.add(0.0, 0.85, 0.0), var15, var14);
                     }
                  }
               }
            }
         } catch (RuntimeException var16) {
            r4.T("ESP", "Ore overlay failed", var16);
         }
      }
   }

   private void v() {
      if (mc.level != null && mc.levelExtractor != null) {
         mc.levelExtractor.allChanged();
      }
   }

   private static void u() {
      T(l());
   }

   private static void T(boolean var0) {
      if (var0 != I) {
         I = var0;
         r4.g("XRay", "Terrain see-through {} for chunk rebuilds", var0 ? "admitted" : "withdrawn");
      }
   }

   public static boolean Q() {
      return j != null && j.isEnabled() ? B : false;
   }

   public static boolean l() {
      return j != null && j.isEnabled() ? B && (r.equals("See Through") || r.equals("Both")) : false;
   }

   public static boolean q() {
      return I;
   }

   public static boolean n() {
      return j != null && j.isEnabled() ? B && (r.equals("ESP Outline") || r.equals("Both")) : false;
   }

   public static boolean c(Block var0) {
      return H.contains(var0);
   }

   static {
      D("Diamond", 4910553, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE);
      D("Emerald", 1564002, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE);
      D("Gold", 16570170, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE);
      D("Iron", 14857359, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE);
      D("Redstone", 16724016, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE);
      D("Lapis", 4025343, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE);
      D("Copper", 15764043, Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE);
      D("Coal", 10790052, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE);
      D("Ancient Debris", 12607548, Blocks.ANCIENT_DEBRIS);
      D("Quartz", 16052198, Blocks.NETHER_QUARTZ_ORE);
      D("Base Blocks", 4910553, Blocks.DIAMOND_BLOCK);
      D("Base Blocks", 1564002, Blocks.EMERALD_BLOCK);
      D("Base Blocks", 16570170, Blocks.GOLD_BLOCK);
      D("Base Blocks", 15132390, Blocks.IRON_BLOCK);
      D("Base Blocks", 10259596, Blocks.NETHERITE_BLOCK);
      D("Base Blocks", 16757820, Blocks.CHEST);
      D("Base Blocks", 11554047, Blocks.ENDER_CHEST);
      D("Base Blocks", 16727160, Blocks.SPAWNER);
      D("Base Blocks", 16734810, Blocks.TNT);

      for (Block var3 : new Block[]{Blocks.LAVA, Blocks.WATER, Blocks.OBSIDIAN, Blocks.BEDROCK}) {
         c.put(var3, 16777215);
      }
   }

   private record t(BlockPos pos, Block block, int color, double distanceSquared, int vein) {
      XRayModule.t withVein(int var1) {
         return new XRayModule.t(this.pos, this.block, this.color, this.distanceSquared, var1);
      }
   }
}
