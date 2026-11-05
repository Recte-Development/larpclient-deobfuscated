package com.larpclient.module;

import com.larpclient._3;
import com.larpclient.dq;
import com.larpclient.mt;
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
   private static volatile XRayModule L;
   private static final Map<Block, Integer> d = new HashMap<>();
   private static final Map<Block, String> z = new LinkedHashMap<>();
   private static final String q = "Base Blocks";
   private static final int I = 32;
   private static volatile boolean B = (boolean)0;
   private static volatile String S = "Both";
   private static volatile Set<Block> R = Set.of();
   private static volatile boolean v;
   private final d6 m = this.addSetting(new d6("Mode", "XRay rendering mode", "Both", "See Through", "ESP Outline", "Both"));
   private final d9 C = this.addSetting(new d9("Range", "Scan radius", 32.0, 8.0, 64.0, 4.0));
   private final Map<String, dh> F = new LinkedHashMap<>();
   private final dq H;
   private final dh P;
   private final dh s;
   private final d6 N;
   private final d9 O;
   private final List<XRayModule.q> g;
   private final PriorityQueue<XRayModule.q> b;
   private Set<Block> y;
   private int x;
   private boolean D;
   private BlockPos u;
   private int i;
   private int W;
   private int p;
   private ClientLevel k;

   private static void r(String var0, int var1, Block... var2) {
      for (Block var6 : var2) {
         d.put(var6, var1);
         z.put(var6, var0);
      }
   }

   public XRayModule() {
      super("XRay", "Highlight ores through blocks", Category.RENDER, -1);
      XRayModule var1 = this;
      this.H = new dq(var1x -> var1.addSetting(var1x));
      this.P = this.addSetting(new dh("Ore Colors", "Color each marker by what it is", true));
      this.s = this.addSetting(new dh("Labels", "Name, vein size and distance above each vein", true));
      this.N = this.addSetting(new d6("Overlay Style", "Ore marker shape", "Box", "Box", "Corners", "Filled"));
      this.O = this.addSetting(new d9("Max Ores", "Nearest ore markers retained", 512.0, 64.0, 2048.0, 64.0));
      this.g = new ArrayList<>();
      this.b = new PriorityQueue<>(Comparator.comparingDouble(XRayModule.q::distanceSquared).reversed());
      this.y = Set.of();
      this.x = -1;
      Set var4 = Set.of("Coal", "Copper", "Quartz");

      for (String var3 : new LinkedHashSet<>(z.values())) {
         this.F.put(var3, this.addSetting(new dh(var3, "Show " + var3.toLowerCase(Locale.ROOT), !var4.contains(var3))));
      }

      BooleanSupplier var5 = () -> !this.m.S("See Through");
      this.C.H(var5);
      this.O.H(var5);
      this.N.H(var5);
      this.P.H(var5);
      this.s.H(var5);
      this.H.o(() -> var5.getAsBoolean() && !this.P.P(), var5, var5, () -> var5.getAsBoolean() && this.N.S("Filled"));
      this.O();
      LevelRenderEvents.BEFORE_GIZMOS.register(this::O);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> A());
      L = this;
   }

   @Override
   public void onEnable() {
      S = this.m.P();
      this.O();
      B = (boolean)1;
      A();
      this.p();
      if (B()) {
         this.y();
      }

      String var1 = FabricLoader.getInstance()
         .getModContainer("sodium")
         .map(var0 -> "Sodium " + var0.getMetadata().getVersion().getFriendlyString())
         .orElse("vanilla");
      mt.K("XRay", "Enabled in {} mode, {} block types shown, {} terrain", S, R.size(), var1);
   }

   public List<df<?>> b() {
      return List.of(this.m, this.N, this.C);
   }

   public List<df<?>> j() {
      return List.copyOf(this.F.values());
   }

   public List<df<?>> B() {
      ArrayList var1 = new ArrayList<>(List.of(this.P, this.s));
      var1.addAll(this.H.q());
      var1.addAll(this.H.g());
      var1.addAll(this.H.L());
      return List.copyOf(var1);
   }

   public List<df<?>> L() {
      return List.of(this.O);
   }

   @Override
   public void onDisable() {
      B = (boolean)0;
      A();
      this.p();
      this.y();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.m.P();
         byte var2 = B();
         byte var3 = this.O();
         if (!var1.equals(S)) {
            S = var1;
            this.p();
         }

         label31: {
            byte var4 = B();
            A((boolean)var4);
            byte var8 = var4;
            byte var7 = var2;
            int var10000 = Integer.compare(var7, var8);
            if ((var10000 | -var10000) >>> 31 == 0) {
               boolean var10 = false;
               byte var9 = var3;
               var10000 = Integer.signum(var9);
               if (((var10000 | -var10000) >>> 31 ^ 1) != 0 || var4 == 0) {
                  break label31;
               }
            }

            mt.U("XRay", "Rebuilding terrain: mode {}, filters changed {}", S, Boolean.valueOf((boolean)var3));
            this.y();
         }

         int var14 = H();
         boolean var12 = false;
         byte var11 = (byte)var14;
         var14 = Integer.signum(var11);
         if (((var14 | -var14) >>> 31 ^ 1) == 0) {
            this.T();
         } else {
            int var16 = this.g.isEmpty();
            boolean var6 = false;
            byte var5 = (byte)var16;
            var16 = Integer.signum(var5);
            if (((var16 | -var16) >>> 31 ^ 1) != 0 || this.u != null) {
               this.p();
            }
         }
      }
   }

   private boolean O() {
      int var1 = 0;
      int var2 = 0;

      for (dh var4 : this.F.values()) {
         if (var4.P()) {
            var1 |= 1 << var2;
         }

         var2++;
      }

      if (var1 == this.x) {
         return false;
      }

      this.x = var1;
      HashSet var8 = new HashSet();
      HashSet var9 = new HashSet();

      for (Block var6 : d.keySet()) {
         String var7 = z.get(var6);
         if (var7 == null) {
            var8.add(var6);
         } else if (this.F.get(var7).P()) {
            var8.add(var6);
            var9.add(var6);
         }
      }

      R = Set.copyOf(var8);
      this.y = Set.copyOf(var9);
      this.p();
      return true;
   }

   private void p() {
      this.g.clear();
      this.b.clear();
      this.u = null;
      this.k = null;
      this.W = this.p = 0;
      this.D = (boolean)0;
   }

   private void T() {
      int var1 = this.C.P().intValue();
      BlockPos var2 = mc.player.blockPosition();
      if (this.k != mc.level || this.u == null || this.i != var1 || this.u.distSqr(var2) > 64.0) {
         this.p();
         this.k = mc.level;
         this.u = var2;
         this.i = var1;
      }

      if (this.p > 0) {
         this.p--;
      } else {
         int var3 = var1 * 2 + 1;
         int var4 = var3 * var3 * var3;
         MutableBlockPos var5 = new MutableBlockPos();

         for (int var6 = 0; var6 < 8192 && this.W < var4; this.W++) {
            int var7 = this.W % var3;
            int var8 = this.W / var3 % var3;
            int var9 = this.W / (var3 * var3);
            int var10 = q(var9);
            int var11 = q(var7);
            int var12 = q(var8);
            if (var10 * var10 + var11 * var11 + var12 * var12 <= var1 * var1) {
               var5.set(this.u.getX() + var10, this.u.getY() + var11, this.u.getZ() + var12);
               LevelChunk var13 = mc.level.getChunkSource().getChunkNow(var5.getX() >> 4, var5.getZ() >> 4);
               if (var13 != null && var5.getY() >= mc.level.getMinY() && var5.getY() <= mc.level.getMaxY()) {
                  Block var14 = var13.getBlockState(var5).getBlock();
                  if (this.y.contains(var14)) {
                     int var15 = 0xFF000000 | d.get(var14);
                     this.b.add(new XRayModule.q(var5.immutable(), var14, var15, var5.distSqr(this.u), 0));

                     while (this.b.size() > this.O.P().intValue()) {
                        this.b.poll();
                     }
                  }
               }
            }

            var6++;
         }

         if (!this.D || this.W >= var4) {
            this.g.clear();
            this.g.addAll(this.b);
            this.g.sort(Comparator.comparingDouble(XRayModule.q::distanceSquared));
            this.v();
         }

         if (this.W >= var4) {
            this.D = (boolean)1;
            this.W = 0;
            this.b.clear();
            this.p = 10;
         }
      }
   }

   private void v() {
      HashMap var1 = new HashMap(this.g.size() * 2);

      for (int var2 = 0; var2 < this.g.size(); var2++) {
         var1.put(this.g.get(var2).pos(), var2);
      }

      boolean[] var13 = new boolean[this.g.size()];
      ArrayDeque var3 = new ArrayDeque();

      for (int var4 = 0; var4 < this.g.size(); var4++) {
         if (!var13[var4]) {
            var13[var4] = (boolean)1;
            XRayModule.q var5 = this.g.get(var4);
            String var6 = U(var5.block());
            int var7 = 0;
            var3.add(var4);

            while (!var3.isEmpty()) {
               BlockPos var8 = this.g.get((Integer)var3.poll()).pos();
               var7++;

               for (int var9 = -1; var9 <= 1; var9++) {
                  for (int var10 = -1; var10 <= 1; var10++) {
                     for (int var11 = -1; var11 <= 1; var11++) {
                        Integer var12 = (Integer)var1.get(var8.offset(var9, var10, var11));
                        if (var12 != null && !var13[var12] && U(this.g.get(var12).block()).equals(var6)) {
                           var13[var12] = (boolean)1;
                           var3.add(var12);
                        }
                     }
                  }
               }
            }

            this.g.set(var4, var5.withVein(var7));
         }
      }
   }

   private static String U(Block var0) {
      String var1 = z.get(var0);
      return "Base Blocks".equals(var1) ? var0.getName().getString() : var1;
   }

   private static int q(int var0) {
      return var0 == 0 ? 0 : (var0 % 2 == 1 ? (var0 + 1) / 2 : -var0 / 2);
   }

   private void O(LevelRenderContext var1) {
      if (this.isOperational() && this.isInGame() && H() && this.k == mc.level) {
         try {
            int var2 = 0;
            int var3 = 0;
            double var4 = this.C.P();
            Vec3 var6 = mc.player.position();

            for (XRayModule.q var8 : this.g) {
               BlockPos var9 = var8.pos();
               LevelChunk var10 = mc.level.getChunkSource().getChunkNow(var9.getX() >> 4, var9.getZ() >> 4);
               if (var10 != null && var10.getBlockState(var9).getBlock() == var8.block()) {
                  Vec3 var11 = Vec3.atCenterOf(var9);
                  double var12 = var6.distanceTo(var11);
                  if (!(var12 >= var4)) {
                     if (var2++ >= this.O.P().intValue()) {
                        break;
                     }

                     int var14 = this.P.P() ? _3.U(var8.color(), dq.C(var12, var4)) : this.H.w(var8.color(), var12, var4, 1.0, var9.hashCode());
                     this.H.O(new AABB(var9).inflate(0.003), this.N.P(), var14);
                     if (this.s.P() && var8.vein() > 0 && var3 < 32 && var14 >>> 24 != 0) {
                        var3++;
                        String var15 = U(var8.block()) + (var8.vein() > 1 ? " x" + var8.vein() : "") + " · " + Math.round(var12) + "m";
                        this.H.d(var11.add(0.0, 0.85, 0.0), var15, var14);
                     }
                  }
               }
            }
         } catch (RuntimeException var16) {
            mt.B("ESP", "Ore overlay failed", var16);
         }
      }
   }

   private void y() {
      if (mc.level != null && mc.levelExtractor != null) {
         mc.levelExtractor.allChanged();
      }
   }

   private static void A() {
      A(B());
   }

   private static void A(boolean var0) {
      if (var0 != v) {
         v = var0;
         mt.U("XRay", "Terrain see-through {} for chunk rebuilds", var0 ? "admitted" : "withdrawn");
      }
   }

   public static boolean X() {
      return L != null && L.isEnabled() ? B : false;
   }

   public static boolean B() {
      return L != null && L.isEnabled() ? B && (S.equals("See Through") || S.equals("Both")) : false;
   }

   public static boolean m() {
      return v;
   }

   public static boolean H() {
      return L != null && L.isEnabled() ? B && (S.equals("ESP Outline") || S.equals("Both")) : false;
   }

   public static boolean z(Block var0) {
      return R.contains(var0);
   }

   static {
      r("Diamond", 4910553, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE);
      r("Emerald", 1564002, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE);
      r("Gold", 16570170, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE);
      r("Iron", 14857359, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE);
      r("Redstone", 16724016, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE);
      r("Lapis", 4025343, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE);
      r("Copper", 15764043, Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE);
      r("Coal", 10790052, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE);
      r("Ancient Debris", 12607548, Blocks.ANCIENT_DEBRIS);
      r("Quartz", 16052198, Blocks.NETHER_QUARTZ_ORE);
      r("Base Blocks", 4910553, Blocks.DIAMOND_BLOCK);
      r("Base Blocks", 1564002, Blocks.EMERALD_BLOCK);
      r("Base Blocks", 16570170, Blocks.GOLD_BLOCK);
      r("Base Blocks", 15132390, Blocks.IRON_BLOCK);
      r("Base Blocks", 10259596, Blocks.NETHERITE_BLOCK);
      r("Base Blocks", 16757820, Blocks.CHEST);
      r("Base Blocks", 11554047, Blocks.ENDER_CHEST);
      r("Base Blocks", 16727160, Blocks.SPAWNER);
      r("Base Blocks", 16734810, Blocks.TNT);

      for (Block var3 : new Block[]{Blocks.LAVA, Blocks.WATER, Blocks.OBSIDIAN, Blocks.BEDROCK}) {
         d.put(var3, 16777215);
      }
   }

   private record q(BlockPos pos, Block block, int color, double distanceSquared, int vein) {
      XRayModule.q withVein(int var1) {
         return new XRayModule.q(this.pos, this.block, this.color, this.distanceSquared, var1);
      }
   }
}
