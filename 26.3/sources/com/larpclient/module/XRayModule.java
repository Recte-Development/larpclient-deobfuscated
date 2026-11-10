package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.j5;
import com.larpclient.vv;
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
   private static volatile XRayModule n;
   private static final Map<Block, Integer> E = new HashMap<>();
   private static final Map<Block, String> J = new LinkedHashMap<>();
   private static final String k = "Base Blocks";
   private static final int p = 32;
   private static volatile boolean t = (boolean)0;
   private static volatile String j = "Both";
   private static volatile Set<Block> c = Set.of();
   private static volatile boolean F;
   private final o l = this.addSetting(new o("Mode", "XRay rendering mode", "Both", "See Through", "ESP Outline", "Both"));
   private final b W = this.addSetting(new b("Range", "Scan radius", 32.0, 8.0, 64.0, 4.0));
   private final Map<String, m> r = new LinkedHashMap<>();
   private final j5 X;
   private final m V;
   private final m b;
   private final o o;
   private final b q;
   private final List<XRayModule.g> D;
   private final PriorityQueue<XRayModule.g> I;
   private Set<Block> Y;
   private int O;
   private boolean A;
   private BlockPos G;
   private int g;
   private int S;
   private int e;
   private ClientLevel x;

   private static void t(String var0, int var1, Block... var2) {
      for (Block var6 : var2) {
         E.put(var6, var1);
         J.put(var6, var0);
      }
   }

   public XRayModule() {
      super("XRay", "Highlight ores through blocks", Category.RENDER, -1);
      XRayModule var1 = this;
      this.X = new j5(var1x -> var1.addSetting(var1x));
      this.V = this.addSetting(new m("Ore Colors", "Color each marker by what it is", true));
      this.b = this.addSetting(new m("Labels", "Name, vein size and distance above each vein", true));
      this.o = this.addSetting(new o("Overlay Style", "Ore marker shape", "Box", "Box", "Corners", "Filled"));
      this.q = this.addSetting(new b("Max Ores", "Nearest ore markers retained", 512.0, 64.0, 2048.0, 64.0));
      this.D = new ArrayList<>();
      this.I = new PriorityQueue<>(Comparator.comparingDouble(XRayModule.g::distanceSquared).reversed());
      this.Y = Set.of();
      this.O = -1;
      Set var4 = Set.of("Coal", "Copper", "Quartz");

      for (String var3 : new LinkedHashSet<>(J.values())) {
         this.r.put(var3, this.addSetting(new m(var3, "Show " + var3.toLowerCase(Locale.ROOT), !var4.contains(var3))));
      }

      BooleanSupplier var5 = () -> !this.l.w("See Through");
      this.W.a(var5);
      this.q.a(var5);
      this.o.a(var5);
      this.V.a(var5);
      this.b.a(var5);
      this.X.q(() -> var5.getAsBoolean() && !this.V.getValue(), var5, var5, () -> var5.getAsBoolean() && this.o.w("Filled"));
      this.F();
      LevelRenderEvents.BEFORE_GIZMOS.register(this::Z);
      ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> N());
      n = this;
   }

   @Override
   public void onEnable() {
      j = this.l.getValue();
      this.F();
      t = (boolean)1;
      N();
      this.U();
      if (Y()) {
         this.Z();
      }

      String var1 = FabricLoader.getInstance()
         .getModContainer("sodium")
         .map(var0 -> "Sodium " + var0.getMetadata().getVersion().getFriendlyString())
         .orElse("vanilla");
      _q.z("XRay", "Enabled in {} mode, {} block types shown, {} terrain", j, c.size(), var1);
   }

   public List<r<?>> Q() {
      return List.of(this.l, this.o, this.W);
   }

   public List<r<?>> C() {
      return List.copyOf(this.r.values());
   }

   public List<r<?>> G() {
      ArrayList var1 = new ArrayList<>(List.of(this.V, this.b));
      var1.addAll(this.X.Q());
      var1.addAll(this.X.l());
      var1.addAll(this.X.M());
      return List.copyOf(var1);
   }

   public List<r<?>> w() {
      return List.of(this.q);
   }

   @Override
   public void onDisable() {
      t = (boolean)0;
      N();
      this.U();
      this.Z();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         String var1 = this.l.getValue();
         byte var2 = Y();
         boolean var3 = this.F();
         int var10000 = var1.equals(j);
         boolean var8 = false;
         byte var7 = (byte)var10000;
         var10000 = Integer.signum(var7);
         if ((var10000 | -var10000) >>> 31 == 0) {
            j = var1;
            this.U();
         }

         byte var4 = Y();
         z((boolean)var4);
         byte var10 = var4;
         byte var9 = var2;
         var10000 = Integer.compare(var9, var10);
         if ((var10000 | -var10000) >>> 31 != 0 || var3 && var4 != 0) {
            _q.c("XRay", "Rebuilding terrain: mode {}, filters changed {}", j, var3);
            this.Z();
         }

         int var15 = P();
         boolean var12 = false;
         byte var11 = (byte)var15;
         var15 = Integer.signum(var11);
         if (((var15 | -var15) >>> 31 ^ 1) == 0) {
            this.B();
         } else {
            int var17 = this.D.isEmpty();
            boolean var6 = false;
            byte var5 = (byte)var17;
            var17 = Integer.signum(var5);
            if (((var17 | -var17) >>> 31 ^ 1) != 0 || this.G != null) {
               this.U();
            }
         }
      }
   }

   private boolean F() {
      int var1 = 0;
      int var2 = 0;

      for (m var4 : this.r.values()) {
         if (var4.getValue()) {
            var1 |= 1 << var2;
         }

         var2++;
      }

      if (var1 == this.O) {
         return false;
      }

      this.O = var1;
      HashSet var8 = new HashSet();
      HashSet var9 = new HashSet();

      for (Block var6 : E.keySet()) {
         String var7 = J.get(var6);
         if (var7 == null) {
            var8.add(var6);
         } else if (this.r.get(var7).getValue()) {
            var8.add(var6);
            var9.add(var6);
         }
      }

      c = Set.copyOf(var8);
      this.Y = Set.copyOf(var9);
      this.U();
      return true;
   }

   private void U() {
      this.D.clear();
      this.I.clear();
      this.G = null;
      this.x = null;
      this.S = this.e = 0;
      this.A = (boolean)0;
   }

   private void B() {
      int var1 = this.W.getValue().intValue();
      BlockPos var2 = mc.player.blockPosition();
      if (this.x != mc.level || this.G == null || this.g != var1 || this.G.distSqr(var2) > 64.0) {
         this.U();
         this.x = mc.level;
         this.G = var2;
         this.g = var1;
      }

      if (this.e > 0) {
         this.e--;
      } else {
         int var3 = var1 * 2 + 1;
         int var4 = var3 * var3 * var3;
         MutableBlockPos var5 = new MutableBlockPos();

         for (int var6 = 0; var6 < 8192 && this.S < var4; this.S++) {
            int var7 = this.S % var3;
            int var8 = this.S / var3 % var3;
            int var9 = this.S / (var3 * var3);
            int var10 = h(var9);
            int var11 = h(var7);
            int var12 = h(var8);
            if (var10 * var10 + var11 * var11 + var12 * var12 <= var1 * var1) {
               var5.set(this.G.getX() + var10, this.G.getY() + var11, this.G.getZ() + var12);
               LevelChunk var13 = mc.level.getChunkSource().getChunkNow(var5.getX() >> 4, var5.getZ() >> 4);
               if (var13 != null && var5.getY() >= mc.level.getMinY() && var5.getY() <= mc.level.getMaxY()) {
                  Block var14 = var13.getBlockState(var5).getBlock();
                  if (this.Y.contains(var14)) {
                     int var15 = 0xFF000000 | E.get(var14);
                     this.I.add(new XRayModule.g(var5.immutable(), var14, var15, var5.distSqr(this.G), 0));

                     while (this.I.size() > this.q.getValue().intValue()) {
                        this.I.poll();
                     }
                  }
               }
            }

            var6++;
         }

         if (!this.A || this.S >= var4) {
            this.D.clear();
            this.D.addAll(this.I);
            this.D.sort(Comparator.comparingDouble(XRayModule.g::distanceSquared));
            this.e();
         }

         if (this.S >= var4) {
            this.A = (boolean)1;
            this.S = 0;
            this.I.clear();
            this.e = 10;
         }
      }
   }

   private void e() {
      HashMap var1 = new HashMap(this.D.size() * 2);

      for (int var2 = 0; var2 < this.D.size(); var2++) {
         var1.put(this.D.get(var2).pos(), var2);
      }

      boolean[] var13 = new boolean[this.D.size()];
      ArrayDeque var3 = new ArrayDeque();

      for (int var4 = 0; var4 < this.D.size(); var4++) {
         if (!var13[var4]) {
            var13[var4] = (boolean)1;
            XRayModule.g var5 = this.D.get(var4);
            String var6 = M(var5.block());
            int var7 = 0;
            var3.add(var4);

            while (!var3.isEmpty()) {
               BlockPos var8 = this.D.get((Integer)var3.poll()).pos();
               var7++;

               for (int var9 = -1; var9 <= 1; var9++) {
                  for (int var10 = -1; var10 <= 1; var10++) {
                     for (int var11 = -1; var11 <= 1; var11++) {
                        Integer var12 = (Integer)var1.get(var8.offset(var9, var10, var11));
                        if (var12 != null && !var13[var12] && M(this.D.get(var12).block()).equals(var6)) {
                           var13[var12] = (boolean)1;
                           var3.add(var12);
                        }
                     }
                  }
               }
            }

            this.D.set(var4, var5.withVein(var7));
         }
      }
   }

   private static String M(Block var0) {
      String var1 = J.get(var0);
      return "Base Blocks".equals(var1) ? var0.getName().getString() : var1;
   }

   private static int h(int var0) {
      return var0 == 0 ? 0 : (var0 % 2 == 1 ? (var0 + 1) / 2 : -var0 / 2);
   }

   private void Z(LevelRenderContext var1) {
      if (this.isOperational() && this.isInGame() && P() && this.x == mc.level) {
         try {
            int var2 = 0;
            int var3 = 0;
            double var4 = this.W.getValue();
            Vec3 var6 = mc.player.position();

            for (XRayModule.g var8 : this.D) {
               BlockPos var9 = var8.pos();
               LevelChunk var10 = mc.level.getChunkSource().getChunkNow(var9.getX() >> 4, var9.getZ() >> 4);
               if (var10 != null && var10.getBlockState(var9).getBlock() == var8.block()) {
                  Vec3 var11 = Vec3.atCenterOf(var9);
                  double var12 = var6.distanceTo(var11);
                  if (!(var12 >= var4)) {
                     if (var2++ >= this.q.getValue().intValue()) {
                        break;
                     }

                     int var14 = this.V.getValue() ? vv.O(var8.color(), j5.K(var12, var4)) : this.X.p(var8.color(), var12, var4, 1.0, var9.hashCode());
                     this.X.L(new AABB(var9).inflate(0.003), this.o.getValue(), var14);
                     if (this.b.getValue() && var8.vein() > 0 && var3 < 32 && var14 >>> 24 != 0) {
                        var3++;
                        String var15 = M(var8.block()) + (var8.vein() > 1 ? " x" + var8.vein() : "") + " · " + Math.round(var12) + "m";
                        this.X.P(var11.add(0.0, 0.85, 0.0), var15, var14);
                     }
                  }
               }
            }
         } catch (RuntimeException var16) {
            _q.g("ESP", "Ore overlay failed", var16);
         }
      }
   }

   private void Z() {
      if (mc.level != null && mc.levelExtractor != null) {
         mc.levelExtractor.allChanged();
      }
   }

   private static void N() {
      z(Y());
   }

   private static void z(boolean var0) {
      if (var0 != F) {
         F = var0;
         _q.c("XRay", "Terrain see-through {} for chunk rebuilds", var0 ? "admitted" : "withdrawn");
      }
   }

   public static boolean I() {
      return n != null && n.isEnabled() ? t : false;
   }

   public static boolean Y() {
      return n != null && n.isEnabled() ? t && (j.equals("See Through") || j.equals("Both")) : false;
   }

   public static boolean S() {
      return F;
   }

   public static boolean P() {
      return n != null && n.isEnabled() ? t && (j.equals("ESP Outline") || j.equals("Both")) : false;
   }

   public static boolean g(Block var0) {
      return c.contains(var0);
   }

   static {
      t("Diamond", 4910553, Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE);
      t("Emerald", 1564002, Blocks.EMERALD_ORE, Blocks.DEEPSLATE_EMERALD_ORE);
      t("Gold", 16570170, Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE, Blocks.NETHER_GOLD_ORE);
      t("Iron", 14857359, Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE);
      t("Redstone", 16724016, Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE);
      t("Lapis", 4025343, Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE);
      t("Copper", 15764043, Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE);
      t("Coal", 10790052, Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE);
      t("Ancient Debris", 12607548, Blocks.ANCIENT_DEBRIS);
      t("Quartz", 16052198, Blocks.NETHER_QUARTZ_ORE);
      t("Base Blocks", 4910553, Blocks.DIAMOND_BLOCK);
      t("Base Blocks", 1564002, Blocks.EMERALD_BLOCK);
      t("Base Blocks", 16570170, Blocks.GOLD_BLOCK);
      t("Base Blocks", 15132390, Blocks.IRON_BLOCK);
      t("Base Blocks", 10259596, Blocks.NETHERITE_BLOCK);
      t("Base Blocks", 16757820, Blocks.CHEST);
      t("Base Blocks", 11554047, Blocks.ENDER_CHEST);
      t("Base Blocks", 16727160, Blocks.SPAWNER);
      t("Base Blocks", 16734810, Blocks.TNT);

      for (Block var3 : new Block[]{Blocks.LAVA, Blocks.WATER, Blocks.OBSIDIAN, Blocks.BEDROCK}) {
         E.put(var3, 16777215);
      }
   }

   private record g(BlockPos pos, Block block, int color, double distanceSquared, int vein) {
      XRayModule.g withVein(int var1) {
         return new XRayModule.g(this.pos, this.block, this.color, this.distanceSquared, var1);
      }
   }
}
