package com.larpclient.module;

import com.larpclient.t1;
import com.larpclient.ta;
import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RespawnAnchorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class AnchorTapModule extends Module {
   private static AnchorTapModule M;
   private final e U = this.addSetting(new e("Delay", "Ticks between executed anchor steps", 0.0, 0.0, 20.0, 1.0));
   private final s X = this.addSetting(new s("Safe Anchor", "Place an obsidian shield between you and the anchor before detonation", false));
   private final s Z = this.addSetting(new s("Air Replace", "Replace the removed anchor once after detonation", false));
   private final e A = this.addSetting(new e("Range", "Reach capped by native block interaction distance", 4.1, 1.0, 6.0, 0.1));
   private final l u = this.addSetting(new l("Rotation", "Visible turns the real view; Crosshair waits for your aim", "Visible", "Visible", "Crosshair"));
   private final s R = this.addSetting(new s("Auto Switch", "Use hotbar items and restore the selected slot after each step", true));
   private AnchorTapModule.u q = AnchorTapModule.u.A;
   private BlockPos p;
   private boolean x;
   private long v;
   private long J;
   private String z = "Aim at an anchor and press Use";

   public AnchorTapModule() {
      super("AnchorTap", "Charge and explode an anchor with the use key", Category.COMBAT, -1);
      M = this;
   }

   public static boolean b() {
      if (M != null && M.isEnabled()) {
         AnchorTapModule var0 = M;
         if (var0 == null || !var0.isRuntimeActive() || !ta.Y()) {
            return false;
         } else {
            return var0.q != AnchorTapModule.u.A
               ? true
               : mc.hitResult instanceof BlockHitResult var1
                  && mc.options.keyUse.isDown()
                  && mc.level.getBlockState(var1.getBlockPos()).is(Blocks.RESPAWN_ANCHOR);
         }
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      return this.z;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   @Override
   public void onTick() {
      t1.v(this);
      if (ta.Y() && mc.isWindowActive()) {
         boolean var1 = mc.options.keyUse.isDown();
         if (var1) {
            boolean var9 = false;
            byte var8 = this.x;
            int var10000 = Integer.signum(var8);
            if ((var10000 | -var10000) >>> 31 == 0
               && this.q == AnchorTapModule.u.A
               && mc.hitResult instanceof BlockHitResult var2
               && mc.level.getBlockState(var2.getBlockPos()).is(Blocks.RESPAWN_ANCHOR)) {
               this.p = var2.getBlockPos().immutable();
               this.q = AnchorTapModule.u.K;
               this.v = mc.level.getGameTime();
               this.J = 0L;
               com.larpclient.h.U("AnchorTap", "Anchor cycle started at {}", this.p);
            }
         }

         this.x = var1;
         if (this.q != AnchorTapModule.u.A && this.p != null) {
            long var16 = mc.level.getGameTime();
            if (mc.level.hasChunkAt(this.p) && !mc.player.isUsingItem()) {
               if (var16 - this.v > 40L) {
                  this.A("Anchor response timed out");
               } else if (var16 >= this.J) {
                  BlockState var4 = mc.level.getBlockState(this.p);
                  boolean var5 = var4.is(Blocks.RESPAWN_ANCHOR);
                  switch (this.q) {
                     case K:
                        if (!var5) {
                           this.A("Anchor no longer exists");
                           return;
                        }

                        if ((Integer)var4.getValue(RespawnAnchorBlock.CHARGE) > 0) {
                           this.V(AnchorTapModule.u.b);
                           return;
                        }

                        this.X(
                           Items.GLOWSTONE,
                           AnchorTapModule.u.r,
                           () -> mc.level.getBlockState(this.p).is(Blocks.RESPAWN_ANCHOR)
                              && (Integer)mc.level.getBlockState(this.p).getValue(RespawnAnchorBlock.CHARGE) == 0
                        );
                        break;
                     case r:
                        if (!var5) {
                           this.A("Anchor removed before charging");
                        } else if ((Integer)var4.getValue(RespawnAnchorBlock.CHARGE) > 0) {
                           this.V(AnchorTapModule.u.b);
                        } else if (var16 - this.v > 8L) {
                           this.A("Anchor charge not confirmed");
                        }
                        break;
                     case b:
                        if (!this.X.getValue()) {
                           this.V(AnchorTapModule.u.R);
                           return;
                        }

                        BlockPos var18 = this.W();
                        if (!t1.t(var18)) {
                           BlockState var19 = mc.level.getBlockState(var18);
                           if (!var19.is(Blocks.OBSIDIAN) && !var19.is(Blocks.CRYING_OBSIDIAN) && !var19.is(Blocks.BEDROCK)) {
                              this.A("Safe Anchor shield position occupied by another block");
                           } else {
                              this.V(AnchorTapModule.u.R);
                           }

                           return;
                        }

                        if (mc.player.getBoundingBox().intersects(new AABB(var18))) {
                           this.A("No safe shield position outside your body");
                           return;
                        }

                        int var23 = t1.M(Items.OBSIDIAN, this.R.getValue());
                        byte var15 = -1;
                        int var14 = var23;
                        var23 = Integer.compare(var14, var15);
                        if ((var23 | -var23) >>> 31 == 0) {
                           this.A("Safe Anchor requires hotbar obsidian");
                           return;
                        }

                        boolean var7 = t1.K(
                           this,
                           var18,
                           Items.OBSIDIAN,
                           this.A.getValue(),
                           this.u.O("Visible"),
                           this.R.getValue(),
                           () -> this.q == AnchorTapModule.u.b && this.h() && !mc.player.getBoundingBox().intersects(new AABB(var18)),
                           var1x -> {
                              if (var1x) {
                                 this.J = mc.level.getGameTime() + Math.max(1, this.U.getValue().intValue());
                                 this.z = "Waiting for obsidian shield";
                              }
                           }
                        );
                        if (!var7) {
                           this.z = "Aim at a reachable shield support";
                        }
                        break;
                     case R:
                        if (!this.h()) {
                           this.A("Charged anchor no longer exists");
                           return;
                        }

                        if ((Boolean)mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, this.p)) {
                           this.A("Anchors set spawn here; detonation skipped");
                           return;
                        }

                        if (mc.player.getOffhandItem().is(Items.GLOWSTONE)) {
                           int var22 = (Integer)var4.getValue(RespawnAnchorBlock.CHARGE);
                           byte var11 = 4;
                           int var10 = var22;
                           if ((Integer.compare(var10, var11) >>> 31 ^ 1) == 0) {
                              this.z = "Move offhand glowstone before detonation";
                              return;
                           }
                        }

                        Item var17 = this.G();
                        if (var17 == null) {
                           this.A("A non-glowstone hotbar item is required");
                           return;
                        }

                        this.X(
                           var17,
                           AnchorTapModule.u.F,
                           () -> this.h()
                              && (
                                 !mc.player.getOffhandItem().is(Items.GLOWSTONE)
                                    || (Integer)mc.level.getBlockState(this.p).getValue(RespawnAnchorBlock.CHARGE) == 4
                              )
                              && !(Boolean)mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, this.p)
                        );
                        break;
                     case F:
                        if (!var5) {
                           if (this.Z.getValue() && t1.t(this.p)) {
                              this.V(AnchorTapModule.u.q);
                           } else {
                              this.A("Anchor cycle complete");
                           }
                        } else if (var16 - this.v > 8L) {
                           this.A("Anchor removal not confirmed");
                        }
                        break;
                     case q:
                        if (!t1.t(this.p)) {
                           this.A("Replacement position occupied");
                           return;
                        }

                        int var20 = t1.M(Items.RESPAWN_ANCHOR, this.R.getValue());
                        byte var13 = -1;
                        int var12 = var20;
                        var20 = Integer.compare(var12, var13);
                        if ((var20 | -var20) >>> 31 == 0) {
                           this.A("Replacement anchor missing from hotbar");
                           return;
                        }

                        boolean var6 = t1.K(
                           this,
                           this.p,
                           Items.RESPAWN_ANCHOR,
                           this.A.getValue(),
                           this.u.O("Visible"),
                           this.R.getValue(),
                           () -> this.q == AnchorTapModule.u.q && t1.t(this.p),
                           var1x -> {
                              if (var1x) {
                                 this.V(AnchorTapModule.u.Q);
                              }
                           }
                        );
                        if (!var6) {
                           this.z = "Aim at a reachable replacement support";
                        }
                        break;
                     case Q:
                        if (var5) {
                           this.A("Anchor replacement complete");
                        } else if (var16 - this.v > 8L) {
                           this.A("Anchor replacement not confirmed");
                        }
                  }
               }
            } else {
               this.A("Anchor cycle cancelled");
            }
         }
      } else {
         this.A("Anchor interaction suspended");
      }
   }

   private BlockPos W() {
      Vec3 var1 = mc.player.position().subtract(Vec3.atCenterOf(this.p));
      Direction var2 = Math.abs(var1.x) > Math.abs(var1.z)
         ? (var1.x >= 0.0 ? Direction.EAST : Direction.WEST)
         : (var1.z >= 0.0 ? Direction.SOUTH : Direction.NORTH);
      return this.p.relative(var2);
   }

   private boolean h() {
      return this.p != null
         && mc.level.getBlockState(this.p).is(Blocks.RESPAWN_ANCHOR)
         && (Integer)mc.level.getBlockState(this.p).getValue(RespawnAnchorBlock.CHARGE) > 0;
   }

   private Item G() {
      ItemStack var1 = mc.player.getMainHandItem();
      if (!var1.is(Items.GLOWSTONE)) {
         return var1.getItem();
      }

      if (this.R.getValue()) {
         for (int var2 = 0; var2 < 9; var2++) {
            ItemStack var3 = mc.player.getInventory().getItem(var2);
            if (!var3.is(Items.GLOWSTONE)) {
               return var3.getItem();
            }
         }
      }

      return null;
   }

   private void X(Item var1, AnchorTapModule.u var2, BooleanSupplier var3) {
      if (t1.M(var1, this.R.getValue()) == -1) {
         this.A("Required anchor item missing from hotbar");
      } else {
         AnchorTapModule.u var4 = this.q;
         boolean var5 = t1.G(
            this, this.p, var1, this.A.getValue(), this.u.O("Visible"), this.R.getValue(), () -> this.q == var4 && var3.getAsBoolean(), var2x -> {
               if (var2x) {
                  this.V(var2);
                  this.J = mc.level.getGameTime() + Math.max(1, this.U.getValue().intValue());
               } else {
                  this.z = "Anchor interaction rejected; retrying";
               }
            }
         );
         if (!var5) {
            this.z = "Aim at the anchor within native reach";
         }
      }
   }

   private void V(AnchorTapModule.u var1) {
      this.q = var1;
      this.v = mc.level.getGameTime();
      this.z = var1.name().toLowerCase().replace('_', ' ');
   }

   private void A(String var1) {
      t1.v(this);
      if (this.q != AnchorTapModule.u.A) {
         com.larpclient.h.U("AnchorTap", "{}", var1);
      }

      this.q = AnchorTapModule.u.A;
      this.p = null;
      this.v = 0L;
      this.J = 0L;
      this.z = var1;
   }

   @Override
   public void onEnable() {
      this.x = (boolean)(mc.player != null && mc.options.keyUse.isDown() ? 1 : 0);
      this.A("Aim at an anchor and press Use");
      com.larpclient.h.U("AnchorTap", "Anchor use handler active");
   }

   @Override
   public void onDisable() {
      this.A("Anchor intents cleared");
      this.x = (boolean)0;
   }

   private enum u {
      A,
      K,
      r,
      b,
      R,
      F,
      q,
      Q;
   }
}
