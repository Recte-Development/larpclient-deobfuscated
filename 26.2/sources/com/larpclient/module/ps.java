package com.larpclient.module;

import com.larpclient.b7;
import com.larpclient.g0;
import com.larpclient.md;
import com.larpclient.z4;
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

public final class ps extends pm {
   public static final int A = 64;
   public static final int n = 256;
   public static final int E = 24;
   public static final int g = 16;
   private final s z = this.addSetting(new s("Trial Spawners", "Also find received trial spawners", false));
   private final s I = this.addSetting(new s("Spawner Boxes", "Outline exact received spawner positions", true));
   private final s x = this.addSetting(new s("Spawner Labels", "Received mob type, coordinates and distance", true));
   private final s Z = this.addSetting(new s("Range Rings", "16-block reference ring; server activation ranges can differ", true));
   private final s d = this.addSetting(new s("Spawner Tracers", "Lines from the camera to exact spawner positions", false));
   private final List<g0.g> l = new ArrayList<>();
   private int j;
   private int k;
   private int p;

   public ps() {
      super("SpawnerFinder", "Highlight loaded spawners and their mob types");
      this.z.o(var1 -> this.n("spawner types changed"));
   }

   @Override
   public List<q<?>> detectionSettings() {
      return List.of(this.z);
   }

   @Override
   public List<q<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.addAll(List.of(this.I, this.x, this.Z, this.d));
      return List.copyOf(var1);
   }

   public static boolean J(BlockState var0) {
      return var0.is(Blocks.SPAWNER);
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return J(var1) || this.z.getValue() && var1.is(Blocks.TRIAL_SPAWNER);
   }

   @Override
   protected void beginScan() {
      this.l.clear();
   }

   @Override
   protected void clearEvidence() {
      this.l.clear();
   }

   @Override
   protected void count(BlockState var1, int var2) {
      if (this.candidate(var1)) {
         this.R(var2);
      }
   }

   @Override
   protected void scanBlock(LevelChunk var1, BlockState var2, int var3, int var4, int var5) {
      if (this.candidate(var2)) {
         this.R(var4);
         if (this.l.size() < 64) {
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
               com.larpclient.h.w("BaseFinding", "Spawner mob label unavailable; retaining generic name and exact position", var14);
            }

            this.l.add(new g0.g(var3, var4, var5, var7, var8));
         }
      }
   }

   @Override
   protected g0.t evidence() {
      g0.t var1 = super.evidence();
      return new g0.t(0, 0, var1.spawners(), var1.sampleY(), com.larpclient.tm.EMPTY, b7.EMPTY, this.l);
   }

   @Override
   protected boolean qualifies(g0.t var1) {
      return var1.spawners() > 0;
   }

   @Override
   protected String describe(g0.t var1) {
      String var2 = var1.spawners() == 1 ? "" : "s";
      if (!var1.hits().isEmpty()) {
         g0.g var3 = var1.hits().getFirst();
         return var3.name() + " · " + var1.spawners() + " block" + var2 + " · Y:" + var3.y();
      } else {
         return var1.spawners() + " spawner block" + var2 + "; first at Y:" + var1.sampleY();
      }
   }

   @Override
   protected md alert(ChunkPos var1, g0.t var2) {
      return md.spawners(var1.getMinBlockX(), var1.getMinBlockZ(), var2);
   }

   @Override
   protected void beginDraw() {
      this.j = this.k = this.p = 0;
   }

   @Override
   protected void drawEvidence(ChunkPos var1, g0.t var2, z4 var3, int var4) {
      for (g0.g var6 : var2.hits()) {
         if (this.j >= 256) {
            break;
         }

         BlockPos var7 = new BlockPos(var1.getMinBlockX() + var6.x(), var6.y(), var1.getMinBlockZ() + var6.z());
         BlockState var8 = getMc().level.getBlockState(var7);
         if (var6.trial() ? this.z.getValue() && var8.is(Blocks.TRIAL_SPAWNER) : J(var8)) {
            this.j++;
            Vec3 var9 = Vec3.atCenterOf(var7);
            if (this.I.getValue()) {
               var3.R(new AABB(var7).inflate(0.03), "Box", var4);
            }

            if (this.x.getValue() && this.k++ < 24) {
               var3.d(
                  var9.add(0.0, 1.0, 0.0),
                  var6.name() + " · " + Math.round(var9.distanceTo(getMc().player.position())) + "m · " + var7.getX() + ", " + var7.getY() + ", " + var7.getZ(),
                  var4
               );
            }

            if (this.d.getValue()) {
               var3.S(getMc().gameRenderer.mainCamera().position(), var9, var4);
            }

            if (this.Z.getValue() && this.p++ < 16) {
               for (int var10 = 0; var10 < 48; var10++) {
                  double var11 = var10 * Math.PI * 2.0 / 48.0;
                  double var13 = (var10 + 1) * Math.PI * 2.0 / 48.0;
                  var3.S(var9.add(Math.cos(var11) * 16.0, 0.0, Math.sin(var11) * 16.0), var9.add(Math.cos(var13) * 16.0, 0.0, Math.sin(var13) * 16.0), var4);
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
