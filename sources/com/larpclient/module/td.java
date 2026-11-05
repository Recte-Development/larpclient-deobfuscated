package com.larpclient.module;

import com.larpclient.da;
import com.larpclient.mi;
import com.larpclient.oa;
import com.larpclient.r4;
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

public final class td extends tf {
   public static final int I = 64;
   public static final int f = 256;
   public static final int p = 24;
   public static final int i = 16;
   private final ln q = this.addSetting(new ln("Trial Spawners", "Also find received trial spawners", false));
   private final ln h = this.addSetting(new ln("Spawner Boxes", "Outline exact received spawner positions", true));
   private final ln X = this.addSetting(new ln("Spawner Labels", "Received mob type, coordinates and distance", true));
   private final ln r = this.addSetting(new ln("Range Rings", "16-block reference ring; server activation ranges can differ", true));
   private final ln Z = this.addSetting(new ln("Spawner Tracers", "Lines from the camera to exact spawner positions", false));
   private final List<oa.o> V = new ArrayList<>();
   private int m;
   private int B;
   private int v;

   public td() {
      super("SpawnerFinder", "Highlight loaded spawners and their mob types");
      this.q.W(var1 -> this.C("spawner types changed"));
   }

   @Override
   public List<l6<?>> detectionSettings() {
      return List.of(this.q);
   }

   @Override
   public List<l6<?>> markerSettings() {
      ArrayList var1 = new ArrayList<>(super.markerSettings());
      var1.addAll(List.of(this.h, this.X, this.r, this.Z));
      return List.copyOf(var1);
   }

   public static boolean H(BlockState var0) {
      return var0.is(Blocks.SPAWNER);
   }

   @Override
   protected boolean candidate(BlockState var1) {
      return H(var1) || this.q.getValue() && var1.is(Blocks.TRIAL_SPAWNER);
   }

   @Override
   protected void beginScan() {
      this.V.clear();
   }

   @Override
   protected void clearEvidence() {
      this.V.clear();
   }

   @Override
   protected void count(BlockState var1, int var2) {
      if (this.candidate(var1)) {
         this.S(var2);
      }
   }

   @Override
   protected void scanBlock(LevelChunk var1, BlockState var2, int var3, int var4, int var5) {
      if (this.candidate(var2)) {
         this.S(var4);
         if (this.V.size() < 64) {
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
               r4.T("BaseFinding", "Spawner mob label unavailable; retaining generic name and exact position", var14);
            }

            this.V.add(new oa.o(var3, var4, var5, var7, var8));
         }
      }
   }

   @Override
   protected oa.j evidence() {
      oa.j var1 = super.evidence();
      return new oa.j(0, 0, var1.spawners(), var1.sampleY(), com.larpclient.l4.EMPTY, da.EMPTY, this.V);
   }

   @Override
   protected boolean qualifies(oa.j var1) {
      return var1.spawners() > 0;
   }

   @Override
   protected String describe(oa.j var1) {
      if (!var1.hits().isEmpty()) {
         oa.o var2 = var1.hits().getFirst();
         return var2.name() + " · " + var1.spawners() + " blocks · Y:" + var2.y();
      } else {
         return var1.spawners() + " spawner blocks; first at Y:" + var1.sampleY();
      }
   }

   @Override
   protected void beginDraw() {
      this.m = this.B = this.v = 0;
   }

   @Override
   protected void drawEvidence(ChunkPos var1, oa.j var2, mi var3, int var4) {
      for (oa.o var6 : var2.hits()) {
         if (this.m >= 256) {
            break;
         }

         BlockPos var7 = new BlockPos(var1.getMinBlockX() + var6.x(), var6.y(), var1.getMinBlockZ() + var6.z());
         BlockState var8 = getMc().level.getBlockState(var7);
         if (var6.trial() ? this.q.getValue() && var8.is(Blocks.TRIAL_SPAWNER) : H(var8)) {
            this.m++;
            Vec3 var9 = Vec3.atCenterOf(var7);
            if (this.h.getValue()) {
               var3.I(new AABB(var7).inflate(0.03), "Box", var4);
            }

            if (this.X.getValue() && this.B++ < 24) {
               var3.t(
                  var9.add(0.0, 1.0, 0.0),
                  var6.name() + " · " + Math.round(var9.distanceTo(getMc().player.position())) + "m · " + var7.getX() + ", " + var7.getY() + ", " + var7.getZ(),
                  var4
               );
            }

            if (this.Z.getValue()) {
               var3.D(getMc().gameRenderer.mainCamera().position(), var9, var4);
            }

            if (this.r.getValue() && this.v++ < 16) {
               for (int var10 = 0; var10 < 48; var10++) {
                  double var11 = var10 * Math.PI * 2.0 / 48.0;
                  double var13 = (var10 + 1) * Math.PI * 2.0 / 48.0;
                  var3.D(var9.add(Math.cos(var11) * 16.0, 0.0, Math.sin(var11) * 16.0), var9.add(Math.cos(var13) * 16.0, 0.0, Math.sin(var13) * 16.0), var4);
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
