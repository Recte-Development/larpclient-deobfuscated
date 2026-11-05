package com.larpclient.module;

import com.larpclient._d;
import com.larpclient._m;
import com.larpclient.mt;
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
   private static AnchorTapModule i;
   private final d9 X = this.addSetting(new d9("Delay", "Ticks between executed anchor steps", 0.0, 0.0, 20.0, 1.0));
   private final dh E = this.addSetting(new dh("Safe Anchor", "Place an obsidian shield between you and the anchor before detonation", false));
   private final dh I = this.addSetting(new dh("Air Replace", "Replace the removed anchor once after detonation", false));
   private final d9 b = this.addSetting(new d9("Range", "Reach capped by native block interaction distance", 4.1, 1.0, 6.0, 0.1));
   private final d6 W = this.addSetting(new d6("Rotation", "Visible turns the real view; Crosshair waits for your aim", "Visible", "Visible", "Crosshair"));
   private final dh A = this.addSetting(new dh("Auto Switch", "Use hotbar items and restore the selected slot after each step", true));
   private AnchorTapModule.j m = AnchorTapModule.j.L;
   private BlockPos t;
   private boolean Q;
   private long L;
   private long V;
   private String B = "Aim at an anchor and press Use";

   public AnchorTapModule() {
      super("AnchorTap", "Charge and explode an anchor with the use key", Category.COMBAT, -1);
      i = this;
   }

   public static boolean J() {
      if (i != null && i.isEnabled()) {
         AnchorTapModule var0 = i;
         if (var0 == null || !var0.isRuntimeActive() || !_m.z()) {
            return false;
         } else {
            return var0.m != AnchorTapModule.j.L
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
      return this.B;
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   @Override
   public void onTick() {
      _d.N(this);
      if (_m.z() && mc.isWindowActive()) {
         boolean var1 = mc.options.keyUse.isDown();
         if (var1) {
            boolean var11 = false;
            byte var10 = this.Q;
            int var10000 = Integer.signum(var10);
            if ((var10000 | -var10000) >>> 31 == 0
               && this.m == AnchorTapModule.j.L
               && mc.hitResult instanceof BlockHitResult var2
               && mc.level.getBlockState(var2.getBlockPos()).is(Blocks.RESPAWN_ANCHOR)) {
               this.t = var2.getBlockPos().immutable();
               this.m = AnchorTapModule.j.E;
               this.L = mc.level.getGameTime();
               this.V = 0L;
               mt.U("AnchorTap", "Anchor cycle started at {}", this.t);
            }
         }

         this.Q = var1;
         if (this.m != AnchorTapModule.j.L && this.t != null) {
            long var16 = mc.level.getGameTime();
            if (mc.level.hasChunkAt(this.t) && !mc.player.isUsingItem()) {
               if (var16 - this.L > 40L) {
                  this.e("Anchor response timed out");
               } else if (var16 >= this.V) {
                  BlockState var4 = mc.level.getBlockState(this.t);
                  boolean var5 = var4.is(Blocks.RESPAWN_ANCHOR);
                  switch (this.m) {
                     case E:
                        if (!var5) {
                           this.e("Anchor no longer exists");
                           return;
                        }

                        if ((Integer)var4.getValue(RespawnAnchorBlock.CHARGE) > 0) {
                           this.k(AnchorTapModule.j.G);
                           return;
                        }

                        this.w(
                           Items.GLOWSTONE,
                           AnchorTapModule.j.J,
                           () -> mc.level.getBlockState(this.t).is(Blocks.RESPAWN_ANCHOR)
                              && (Integer)mc.level.getBlockState(this.t).getValue(RespawnAnchorBlock.CHARGE) == 0
                        );
                        break;
                     case J:
                        if (!var5) {
                           this.e("Anchor removed before charging");
                        } else if ((Integer)var4.getValue(RespawnAnchorBlock.CHARGE) > 0) {
                           this.k(AnchorTapModule.j.G);
                        } else if (var16 - this.L > 8L) {
                           this.e("Anchor charge not confirmed");
                        }
                        break;
                     case G:
                        if (!this.E.P()) {
                           this.k(AnchorTapModule.j.N);
                           return;
                        }

                        BlockPos var18 = this.L();
                        if (!_d.Z(var18)) {
                           BlockState var19 = mc.level.getBlockState(var18);
                           if (!var19.is(Blocks.OBSIDIAN) && !var19.is(Blocks.CRYING_OBSIDIAN) && !var19.is(Blocks.BEDROCK)) {
                              this.e("Safe Anchor shield position occupied by another block");
                           } else {
                              this.k(AnchorTapModule.j.N);
                           }

                           return;
                        }

                        if (mc.player.getBoundingBox().intersects(new AABB(var18))) {
                           this.e("No safe shield position outside your body");
                           return;
                        }

                        int var23 = _d.B(Items.OBSIDIAN, this.A.P());
                        byte var15 = -1;
                        int var14 = var23;
                        var23 = Integer.compare(var14, var15);
                        if ((var23 | -var23) >>> 31 == 0) {
                           this.e("Safe Anchor requires hotbar obsidian");
                           return;
                        }

                        boolean var7 = _d.a(
                           this,
                           var18,
                           Items.OBSIDIAN,
                           this.b.P(),
                           this.W.S("Visible"),
                           this.A.P(),
                           () -> this.m == AnchorTapModule.j.G && this.z() && !mc.player.getBoundingBox().intersects(new AABB(var18)),
                           var1x -> {
                              if (var1x) {
                                 this.V = mc.level.getGameTime() + Math.max(1, this.X.P().intValue());
                                 this.B = "Waiting for obsidian shield";
                              }
                           }
                        );
                        if (!var7) {
                           this.B = "Aim at a reachable shield support";
                        }
                        break;
                     case N:
                        if (!this.z()) {
                           this.e("Charged anchor no longer exists");
                           return;
                        }

                        if ((Boolean)mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, this.t)) {
                           this.e("Anchors set spawn here; detonation skipped");
                           return;
                        }

                        if (mc.player.getOffhandItem().is(Items.GLOWSTONE)) {
                           int var22 = (Integer)var4.getValue(RespawnAnchorBlock.CHARGE);
                           byte var9 = 4;
                           int var8 = var22;
                           if ((Integer.compare(var8, var9) >>> 31 ^ 1) == 0) {
                              this.B = "Move offhand glowstone before detonation";
                              return;
                           }
                        }

                        Item var17 = this.K();
                        if (var17 == null) {
                           this.e("A non-glowstone hotbar item is required");
                           return;
                        }

                        this.w(
                           var17,
                           AnchorTapModule.j.R,
                           () -> this.z()
                              && (
                                 !mc.player.getOffhandItem().is(Items.GLOWSTONE)
                                    || (Integer)mc.level.getBlockState(this.t).getValue(RespawnAnchorBlock.CHARGE) == 4
                              )
                              && !(Boolean)mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, this.t)
                        );
                        break;
                     case R:
                        if (!var5) {
                           if (this.I.P() && _d.Z(this.t)) {
                              this.k(AnchorTapModule.j.e);
                           } else {
                              this.e("Anchor cycle complete");
                           }
                        } else if (var16 - this.L > 8L) {
                           this.e("Anchor removal not confirmed");
                        }
                        break;
                     case e:
                        if (!_d.Z(this.t)) {
                           this.e("Replacement position occupied");
                           return;
                        }

                        int var20 = _d.B(Items.RESPAWN_ANCHOR, this.A.P());
                        byte var13 = -1;
                        int var12 = var20;
                        var20 = Integer.compare(var12, var13);
                        if ((var20 | -var20) >>> 31 == 0) {
                           this.e("Replacement anchor missing from hotbar");
                           return;
                        }

                        boolean var6 = _d.a(
                           this,
                           this.t,
                           Items.RESPAWN_ANCHOR,
                           this.b.P(),
                           this.W.S("Visible"),
                           this.A.P(),
                           () -> this.m == AnchorTapModule.j.e && _d.Z(this.t),
                           var1x -> {
                              if (var1x) {
                                 this.k(AnchorTapModule.j.r);
                              }
                           }
                        );
                        if (!var6) {
                           this.B = "Aim at a reachable replacement support";
                        }
                        break;
                     case r:
                        if (var5) {
                           this.e("Anchor replacement complete");
                        } else if (var16 - this.L > 8L) {
                           this.e("Anchor replacement not confirmed");
                        }
                  }
               }
            } else {
               this.e("Anchor cycle cancelled");
            }
         }
      } else {
         this.e("Anchor interaction suspended");
      }
   }

   private BlockPos L() {
      Vec3 var1 = mc.player.position().subtract(Vec3.atCenterOf(this.t));
      Direction var2 = Math.abs(var1.x) > Math.abs(var1.z)
         ? (var1.x >= 0.0 ? Direction.EAST : Direction.WEST)
         : (var1.z >= 0.0 ? Direction.SOUTH : Direction.NORTH);
      return this.t.relative(var2);
   }

   private boolean z() {
      return this.t != null
         && mc.level.getBlockState(this.t).is(Blocks.RESPAWN_ANCHOR)
         && (Integer)mc.level.getBlockState(this.t).getValue(RespawnAnchorBlock.CHARGE) > 0;
   }

   private Item K() {
      ItemStack var1 = mc.player.getMainHandItem();
      if (!var1.is(Items.GLOWSTONE)) {
         return var1.getItem();
      }

      if (this.A.P()) {
         for (int var2 = 0; var2 < 9; var2++) {
            ItemStack var3 = mc.player.getInventory().getItem(var2);
            if (!var3.is(Items.GLOWSTONE)) {
               return var3.getItem();
            }
         }
      }

      return null;
   }

   private void w(Item var1, AnchorTapModule.j var2, BooleanSupplier var3) {
      if (_d.B(var1, this.A.P()) == -1) {
         this.e("Required anchor item missing from hotbar");
      } else {
         AnchorTapModule.j var4 = this.m;
         boolean var5 = _d.U(this, this.t, var1, this.b.P(), this.W.S("Visible"), this.A.P(), () -> this.m == var4 && var3.getAsBoolean(), var2x -> {
            if (var2x) {
               this.k(var2);
               this.V = mc.level.getGameTime() + Math.max(1, this.X.P().intValue());
            } else {
               this.B = "Anchor interaction rejected; retrying";
            }
         });
         if (!var5) {
            this.B = "Aim at the anchor within native reach";
         }
      }
   }

   private void k(AnchorTapModule.j var1) {
      this.m = var1;
      this.L = mc.level.getGameTime();
      this.B = var1.name().toLowerCase().replace('_', ' ');
   }

   private void e(String var1) {
      _d.N(this);
      if (this.m != AnchorTapModule.j.L) {
         mt.U("AnchorTap", "{}", var1);
      }

      this.m = AnchorTapModule.j.L;
      this.t = null;
      this.L = 0L;
      this.V = 0L;
      this.B = var1;
   }

   @Override
   public void onEnable() {
      this.Q = (boolean)(mc.player != null && mc.options.keyUse.isDown() ? 1 : 0);
      this.e("Aim at an anchor and press Use");
      mt.U("AnchorTap", "Anchor use handler active");
   }

   @Override
   public void onDisable() {
      this.e("Anchor intents cleared");
      this.Q = (boolean)0;
   }

   private enum j {
      L,
      E,
      J,
      G,
      N,
      R,
      e,
      r;
   }
}
