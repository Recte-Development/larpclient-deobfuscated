package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.dy;
import com.larpclient.hl;
import com.larpclient.j5;
import com.larpclient.qq;
import com.larpclient.s3;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class wx extends ke {
   public static final int i = 64;
   public static final int N = 256;
   public static final int g = 24;
   public static final int t = 16;
   private final m x = this.addSetting(new m("Trial Spawners", "Also find received trial spawners", false));
   private final m S = this.addSetting(new m("Spawner Boxes", "Outline exact received spawner positions", true));
   private final m q = this.addSetting(new m("Spawner Labels", "Received mob type, coordinates and distance", true));
   private final m G = this.addSetting(new m("Range Rings", "16-block reference ring; server activation ranges can differ", true));
   private final m H = this.addSetting(new m("Spawner Tracers", "Lines from the camera to exact spawner positions", false));
   private final List<dy.w> s = new ArrayList<>();
   private int w;
   private int C;
   private int n;

   public wx() {
      super("SpawnerFinder", "Highlight loaded spawners and their mob types");
      this.x.w(var1 -> this.T("spawner types changed"));
   }

   @Override
   public List<com.larpclient.module.r<?>> detectionSettings() {
      return List.of(this.x);
   }

   @Override
   public List<com.larpclient.module.r<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.addAll(List.of(this.S, this.q, this.G, this.H));
      return List.copyOf(var1);
   }

   public static boolean W(BlockState var0) {
      return var0.is(Blocks.SPAWNER);
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return W(var1) || this.x.getValue() && var1.is(Blocks.TRIAL_SPAWNER);
   }

   @Override
   protected void beginScan() {
      this.s.clear();
   }

   @Override
   protected void clearEvidence() {
      this.s.clear();
   }

   @Override
   protected void count(BlockState var1, int var2) {
      if (this.candidate(var1)) {
         this.U(var2);
      }
   }

   @Override
   protected void scanBlock(LevelChunk var1, BlockState var2, int var3, int var4, int var5) {
      if (this.candidate(var2)) {
         this.U(var4);
         if (this.s.size() < 64) {
            BlockPos var6 = new BlockPos(var1.getPos().getMinBlockX() + var3, var4, var1.getPos().getMinBlockZ() + var5);
            boolean var7 = var2.is(Blocks.TRIAL_SPAWNER);
            String var8 = var7 ? "Trial Spawner" : "Spawner";

            try {
               BlockEntity var9 = var1.getBlockEntity(var6);
               Entity var10 = null;
               if (var9 instanceof SpawnerBlockEntity var11) {
                  var10 = var11.getSpawner().getOrCreateDisplayEntity(getMc().level, var6);
               } else if (var9 instanceof TrialSpawnerBlockEntity var12) {
                  TrialSpawner var13 = var12.getTrialSpawner();
                  var10 = var13.getStateData().getOrCreateDisplayEntity(var13, getMc().level, var12.getState());
               }

               if (var10 != null) {
                  var8 = var10.getType().getDescription().getString() + (var7 ? " Trial Spawner" : " Spawner");
               }
            } catch (RuntimeException var14) {
               _q.g("BaseFinding", "Spawner mob label unavailable; retaining generic name and exact position", var14);
            }

            this.s.add(new dy.w(var3, var4, var5, var7, var8));
         }
      }
   }

   @Override
   protected dy.h evidence() {
      dy.h var1 = super.evidence();
      return new dy.h(0, 0, var1.spawners(), var1.sampleY(), qq.EMPTY, hl.EMPTY, this.s);
   }

   @Override
   protected boolean qualifies(dy.h var1) {
      return var1.spawners() > 0;
   }

   @Override
   protected String describe(dy.h var1) {
      String var2 = var1.spawners() == 1 ? "" : "s";
      if (!var1.hits().isEmpty()) {
         dy.w var3 = var1.hits().getFirst();
         return var3.name() + " · " + var1.spawners() + " block" + var2 + " · Y:" + var3.y();
      } else {
         return var1.spawners() + " spawner block" + var2 + "; first at Y:" + var1.sampleY();
      }
   }

   @Override
   protected s3 alert(ChunkPos var1, dy.h var2) {
      return s3.spawners(var1.getMinBlockX(), var1.getMinBlockZ(), var2);
   }

   @Override
   protected void beginDraw() {
      this.w = this.C = this.n = 0;
   }

   @Override
   protected void drawEvidence(ChunkPos var1, dy.h var2, j5 var3, int var4) {
      for (dy.w var6 : var2.hits()) {
         if (this.w >= 256) {
            break;
         }

         BlockPos var7 = new BlockPos(var1.getMinBlockX() + var6.x(), var6.y(), var1.getMinBlockZ() + var6.z());
         BlockState var8 = getMc().level.getBlockState(var7);
         if (var6.trial() ? this.x.getValue() && var8.is(Blocks.TRIAL_SPAWNER) : W(var8)) {
            this.w++;
            Vec3 var9 = Vec3.atCenterOf(var7);
            if (this.S.getValue()) {
               var3.L(new AABB(var7).inflate(0.03), "Box", var4);
            }

            if (this.q.getValue() && this.C++ < 24) {
               var3.P(
                  var9.add(0.0, 1.0, 0.0),
                  var6.name() + " · " + Math.round(var9.distanceTo(getMc().player.position())) + "m · " + var7.getX() + ", " + var7.getY() + ", " + var7.getZ(),
                  var4
               );
            }

            if (this.H.getValue()) {
               var3.q(getMc().gameRenderer.mainCamera().position(), var9, var4);
            }

            if (this.G.getValue() && this.n++ < 16) {
               for (int var10 = 0; var10 < 48; var10++) {
                  double var11 = var10 * Math.PI * 2.0 / 48.0;
                  double var13 = (var10 + 1) * Math.PI * 2.0 / 48.0;
                  var3.q(var9.add(Math.cos(var11) * 16.0, 0.0, Math.sin(var11) * 16.0), var9.add(Math.cos(var13) * 16.0, 0.0, Math.sin(var13) * 16.0), var4);
               }
            }
         }
      }
   }

   @Override
   protected int tint() {
      return -943189;
   }
}
