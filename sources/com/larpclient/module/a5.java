package com.larpclient.module;

import com.larpclient.ce;
import com.larpclient.dq;
import com.larpclient.mt;
import com.larpclient.xl;
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

public final class a5 extends az {
   public static final int o = 64;
   public static final int j = 256;
   public static final int a = 24;
   public static final int Z = 16;
   private final dh Q = this.addSetting(new dh("Trial Spawners", "Also find received trial spawners", false));
   private final dh T = this.addSetting(new dh("Spawner Boxes", "Outline exact received spawner positions", true));
   private final dh W = this.addSetting(new dh("Spawner Labels", "Received mob type, coordinates and distance", true));
   private final dh H = this.addSetting(new dh("Range Rings", "16-block reference ring; server activation ranges can differ", true));
   private final dh E = this.addSetting(new dh("Spawner Tracers", "Lines from the camera to exact spawner positions", false));
   private final List<com.larpclient.d6.b> I = new ArrayList<>();
   private int O;
   private int s;
   private int t;

   public a5() {
      super("SpawnerFinder", "Highlight loaded spawners and their mob types");
      this.Q.h(var1 -> this.s("spawner types changed"));
   }

   @Override
   public List<df<?>> detectionSettings() {
      return List.of(this.Q);
   }

   @Override
   public List<df<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.addAll(List.of(this.T, this.W, this.H, this.E));
      return List.copyOf(var1);
   }

   public static boolean m(BlockState var0) {
      return var0.is(Blocks.SPAWNER);
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return m(var1) || this.Q.P() && var1.is(Blocks.TRIAL_SPAWNER);
   }

   @Override
   protected void beginScan() {
      this.I.clear();
   }

   @Override
   protected void clearEvidence() {
      this.I.clear();
   }

   @Override
   protected void count(BlockState var1, int var2) {
      if (this.candidate(var1)) {
         this.b(var2);
      }
   }

   @Override
   protected void scanBlock(LevelChunk var1, BlockState var2, int var3, int var4, int var5) {
      if (this.candidate(var2)) {
         this.b(var4);
         if (this.I.size() < 64) {
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
               mt.B("BaseFinding", "Spawner mob label unavailable; retaining generic name and exact position", var14);
            }

            this.I.add(new com.larpclient.d6.b(var3, var4, var5, var7, var8));
         }
      }
   }

   @Override
   protected com.larpclient.d6.f evidence() {
      com.larpclient.d6.f var1 = super.evidence();
      return new com.larpclient.d6.f(0, 0, var1.spawners(), var1.sampleY(), ce.EMPTY, xl.EMPTY, this.I);
   }

   @Override
   protected boolean qualifies(com.larpclient.d6.f var1) {
      return var1.spawners() > 0;
   }

   @Override
   protected String describe(com.larpclient.d6.f var1) {
      if (!var1.hits().isEmpty()) {
         com.larpclient.d6.b var2 = var1.hits().getFirst();
         return var2.name() + " · " + var1.spawners() + " blocks · Y:" + var2.y();
      } else {
         return var1.spawners() + " spawner blocks; first at Y:" + var1.sampleY();
      }
   }

   @Override
   protected void beginDraw() {
      this.O = this.s = this.t = 0;
   }

   @Override
   protected void drawEvidence(ChunkPos var1, com.larpclient.d6.f var2, dq var3, int var4) {
      for (com.larpclient.d6.b var6 : var2.hits()) {
         if (this.O >= 256) {
            break;
         }

         BlockPos var7 = new BlockPos(var1.getMinBlockX() + var6.x(), var6.y(), var1.getMinBlockZ() + var6.z());
         BlockState var8 = getMc().level.getBlockState(var7);
         if (var6.trial() ? this.Q.P() && var8.is(Blocks.TRIAL_SPAWNER) : m(var8)) {
            this.O++;
            Vec3 var9 = Vec3.atCenterOf(var7);
            if (this.T.P()) {
               var3.O(new AABB(var7).inflate(0.03), "Box", var4);
            }

            if (this.W.P() && this.s++ < 24) {
               var3.d(
                  var9.add(0.0, 1.0, 0.0),
                  var6.name() + " · " + Math.round(var9.distanceTo(getMc().player.position())) + "m · " + var7.getX() + ", " + var7.getY() + ", " + var7.getZ(),
                  var4
               );
            }

            if (this.E.P()) {
               var3.n(getMc().gameRenderer.mainCamera().position(), var9, var4);
            }

            if (this.H.P() && this.t++ < 16) {
               for (int var10 = 0; var10 < 48; var10++) {
                  double var11 = var10 * Math.PI * 2.0 / 48.0;
                  double var13 = (var10 + 1) * Math.PI * 2.0 / 48.0;
                  var3.n(var9.add(Math.cos(var11) * 16.0, 0.0, Math.sin(var11) * 16.0), var9.add(Math.cos(var13) * 16.0, 0.0, Math.sin(var13) * 16.0), var4);
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
