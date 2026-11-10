package com.larpclient.module;

import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.vi;
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
   private static AnchorTapModule E;
   private final b L = this.addSetting(new b("Delay", "Ticks between executed anchor steps", 0.0, 0.0, 20.0, 1.0));
   private final m y = this.addSetting(new m("Safe Anchor", "Place an obsidian shield between you and the anchor before detonation", false));
   private final m H = this.addSetting(new m("Air Replace", "Replace the removed anchor once after detonation", false));
   private final b K = this.addSetting(new b("Range", "Reach capped by native block interaction distance", 4.1, 1.0, 6.0, 0.1));
   private final o T = this.addSetting(new o("Rotation", "Visible turns the real view; Crosshair waits for your aim", "Visible", "Visible", "Crosshair"));
   private final m N = this.addSetting(new m("Auto Switch", "Use hotbar items and restore the selected slot after each step", true));
   private AnchorTapModule.u v = AnchorTapModule.u.j;
   private BlockPos M;
   private boolean q;
   private long h;
   private long l;
   private String m = "Aim at an anchor and press Use";

   public AnchorTapModule() {
      super("AnchorTap", "Charge and explode an anchor with the use key", Category.COMBAT, -1);
      E = this;
   }

   public static boolean e() {
      if (E != null && E.isEnabled()) {
         AnchorTapModule var0 = E;
         if (var0 == null || !var0.isRuntimeActive() || !_p.g()) {
            return false;
         } else {
            return var0.v != AnchorTapModule.u.j
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
      return this.m;
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.k);
   }

   @Override
   public void onTick() {
      vi.J(this);
      if (_p.g() && mc.isWindowActive()) {
         boolean var1 = mc.options.keyUse.isDown();
         if (var1) {
            boolean var15 = false;
            byte var14 = this.q;
            int var10000 = Integer.signum(var14);
            if ((var10000 | -var10000) >>> 31 == 0
               && this.v == AnchorTapModule.u.j
               && mc.hitResult instanceof BlockHitResult var2
               && mc.level.getBlockState(var2.getBlockPos()).is(Blocks.RESPAWN_ANCHOR)) {
               this.M = var2.getBlockPos().immutable();
               this.v = AnchorTapModule.u.e;
               this.h = mc.level.getGameTime();
               this.l = 0L;
               _q.c("AnchorTap", "Anchor cycle started at {}", this.M);
            }
         }

         this.q = var1;
         if (this.v != AnchorTapModule.u.j && this.M != null) {
            long var16 = mc.level.getGameTime();
            if (mc.level.hasChunkAt(this.M) && !mc.player.isUsingItem()) {
               if (var16 - this.h > 40L) {
                  this.A("Anchor response timed out");
               } else if (var16 >= this.l) {
                  BlockState var4 = mc.level.getBlockState(this.M);
                  boolean var5 = var4.is(Blocks.RESPAWN_ANCHOR);
                  switch (this.v) {
                     case e:
                        if (!var5) {
                           this.A("Anchor no longer exists");
                           return;
                        }

                        if ((Integer)var4.getValue(RespawnAnchorBlock.CHARGE) > 0) {
                           this.k(AnchorTapModule.u.l);
                           return;
                        }

                        this.S(
                           Items.GLOWSTONE,
                           AnchorTapModule.u.y,
                           () -> mc.level.getBlockState(this.M).is(Blocks.RESPAWN_ANCHOR)
                              && (Integer)mc.level.getBlockState(this.M).getValue(RespawnAnchorBlock.CHARGE) == 0
                        );
                        break;
                     case y:
                        if (!var5) {
                           this.A("Anchor removed before charging");
                        } else if ((Integer)var4.getValue(RespawnAnchorBlock.CHARGE) > 0) {
                           this.k(AnchorTapModule.u.l);
                        } else if (var16 - this.h > 8L) {
                           this.A("Anchor charge not confirmed");
                        }
                        break;
                     case l:
                        if (!this.y.getValue()) {
                           this.k(AnchorTapModule.u.Z);
                           return;
                        }

                        BlockPos var18 = this.Z();
                        if (!vi.i(var18)) {
                           BlockState var19 = mc.level.getBlockState(var18);
                           if (!var19.is(Blocks.OBSIDIAN) && !var19.is(Blocks.CRYING_OBSIDIAN) && !var19.is(Blocks.BEDROCK)) {
                              this.A("Safe Anchor shield position occupied by another block");
                           } else {
                              this.k(AnchorTapModule.u.Z);
                           }

                           return;
                        }

                        if (mc.player.getBoundingBox().intersects(new AABB(var18))) {
                           this.A("No safe shield position outside your body");
                           return;
                        }

                        int var23 = vi.u(Items.OBSIDIAN, this.N.getValue());
                        byte var9 = -1;
                        int var8 = var23;
                        var23 = Integer.compare(var8, var9);
                        if ((var23 | -var23) >>> 31 == 0) {
                           this.A("Safe Anchor requires hotbar obsidian");
                           return;
                        }

                        boolean var7 = vi.a(
                           this,
                           var18,
                           Items.OBSIDIAN,
                           this.K.getValue(),
                           this.T.w("Visible"),
                           this.N.getValue(),
                           () -> this.v == AnchorTapModule.u.l && this.Y() && !mc.player.getBoundingBox().intersects(new AABB(var18)),
                           var1x -> {
                              if (var1x) {
                                 this.l = mc.level.getGameTime() + Math.max(1, this.L.getValue().intValue());
                                 this.m = "Waiting for obsidian shield";
                              }
                           }
                        );
                        if (!var7) {
                           this.m = "Aim at a reachable shield support";
                        }
                        break;
                     case Z:
                        if (!this.Y()) {
                           this.A("Charged anchor no longer exists");
                           return;
                        }

                        if ((Boolean)mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, this.M)) {
                           this.A("Anchors set spawn here; detonation skipped");
                           return;
                        }

                        if (mc.player.getOffhandItem().is(Items.GLOWSTONE)) {
                           int var22 = (Integer)var4.getValue(RespawnAnchorBlock.CHARGE);
                           byte var13 = 4;
                           int var12 = var22;
                           if ((Integer.compare(var12, var13) >>> 31 ^ 1) == 0) {
                              this.m = "Move offhand glowstone before detonation";
                              return;
                           }
                        }

                        Item var17 = this.w();
                        if (var17 == null) {
                           this.A("A non-glowstone hotbar item is required");
                           return;
                        }

                        this.S(
                           var17,
                           AnchorTapModule.u.F,
                           () -> this.Y()
                              && (
                                 !mc.player.getOffhandItem().is(Items.GLOWSTONE)
                                    || (Integer)mc.level.getBlockState(this.M).getValue(RespawnAnchorBlock.CHARGE) == 4
                              )
                              && !(Boolean)mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, this.M)
                        );
                        break;
                     case F:
                        if (!var5) {
                           if (this.H.getValue() && vi.i(this.M)) {
                              this.k(AnchorTapModule.u.J);
                           } else {
                              this.A("Anchor cycle complete");
                           }
                        } else if (var16 - this.h > 8L) {
                           this.A("Anchor removal not confirmed");
                        }
                        break;
                     case J:
                        if (!vi.i(this.M)) {
                           this.A("Replacement position occupied");
                           return;
                        }

                        int var20 = vi.u(Items.RESPAWN_ANCHOR, this.N.getValue());
                        byte var11 = -1;
                        int var10 = var20;
                        var20 = Integer.compare(var10, var11);
                        if ((var20 | -var20) >>> 31 == 0) {
                           this.A("Replacement anchor missing from hotbar");
                           return;
                        }

                        boolean var6 = vi.a(
                           this,
                           this.M,
                           Items.RESPAWN_ANCHOR,
                           this.K.getValue(),
                           this.T.w("Visible"),
                           this.N.getValue(),
                           () -> this.v == AnchorTapModule.u.J && vi.i(this.M),
                           var1x -> {
                              if (var1x) {
                                 this.k(AnchorTapModule.u.q);
                              }
                           }
                        );
                        if (!var6) {
                           this.m = "Aim at a reachable replacement support";
                        }
                        break;
                     case q:
                        if (var5) {
                           this.A("Anchor replacement complete");
                        } else if (var16 - this.h > 8L) {
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

   private BlockPos Z() {
      Vec3 var1 = mc.player.position().subtract(Vec3.atCenterOf(this.M));
      Direction var2 = Math.abs(var1.x) > Math.abs(var1.z)
         ? (var1.x >= 0.0 ? Direction.EAST : Direction.WEST)
         : (var1.z >= 0.0 ? Direction.SOUTH : Direction.NORTH);
      return this.M.relative(var2);
   }

   private boolean Y() {
      return this.M != null
         && mc.level.getBlockState(this.M).is(Blocks.RESPAWN_ANCHOR)
         && (Integer)mc.level.getBlockState(this.M).getValue(RespawnAnchorBlock.CHARGE) > 0;
   }

   private Item w() {
      ItemStack var1 = mc.player.getMainHandItem();
      if (!var1.is(Items.GLOWSTONE)) {
         return var1.getItem();
      }

      if (this.N.getValue()) {
         for (int var2 = 0; var2 < 9; var2++) {
            ItemStack var3 = mc.player.getInventory().getItem(var2);
            if (!var3.is(Items.GLOWSTONE)) {
               return var3.getItem();
            }
         }
      }

      return null;
   }

   private void S(Item var1, AnchorTapModule.u var2, BooleanSupplier var3) {
      if (vi.u(var1, this.N.getValue()) == -1) {
         this.A("Required anchor item missing from hotbar");
      } else {
         AnchorTapModule.u var4 = this.v;
         boolean var5 = vi.d(
            this, this.M, var1, this.K.getValue(), this.T.w("Visible"), this.N.getValue(), () -> this.v == var4 && var3.getAsBoolean(), var2x -> {
               if (var2x) {
                  this.k(var2);
                  this.l = mc.level.getGameTime() + Math.max(1, this.L.getValue().intValue());
               } else {
                  this.m = "Anchor interaction rejected; retrying";
               }
            }
         );
         if (!var5) {
            this.m = "Aim at the anchor within native reach";
         }
      }
   }

   private void k(AnchorTapModule.u var1) {
      this.v = var1;
      this.h = mc.level.getGameTime();
      this.m = var1.name().toLowerCase().replace('_', ' ');
   }

   private void A(String var1) {
      vi.J(this);
      if (this.v != AnchorTapModule.u.j) {
         _q.c("AnchorTap", "{}", var1);
      }

      this.v = AnchorTapModule.u.j;
      this.M = null;
      this.h = 0L;
      this.l = 0L;
      this.m = var1;
   }

   @Override
   public void onEnable() {
      this.q = (boolean)(mc.player != null && mc.options.keyUse.isDown() ? 1 : 0);
      this.A("Aim at an anchor and press Use");
      _q.c("AnchorTap", "Anchor use handler active");
   }

   @Override
   public void onDisable() {
      this.A("Anchor intents cleared");
      this.q = (boolean)0;
   }

   private enum u {
      j,
      e,
      y,
      l,
      Z,
      F,
      J,
      q;
   }
}
