package com.larpclient.module;

import com.larpclient.d8;
import com.larpclient.m;
import com.larpclient.r4;
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
   private final lc b = this.addSetting(new lc("Delay", "Ticks between executed anchor steps", 0.0, 0.0, 20.0, 1.0));
   private final ln H = this.addSetting(new ln("Safe Anchor", "Place an obsidian shield between you and the anchor before detonation", false));
   private final ln n = this.addSetting(new ln("Air Replace", "Replace the removed anchor once after detonation", false));
   private final lc d = this.addSetting(new lc("Range", "Reach capped by native block interaction distance", 4.1, 1.0, 6.0, 0.1));
   private final lp S = this.addSetting(new lp("Rotation", "Visible turns the real view; Crosshair waits for your aim", "Visible", "Visible", "Crosshair"));
   private final ln Y = this.addSetting(new ln("Auto Switch", "Use hotbar items and restore the selected slot after each step", true));
   private AnchorTapModule.q a = AnchorTapModule.q.i;
   private BlockPos p;
   private boolean y;
   private long z;
   private long g;
   private String F = "Aim at an anchor and press Use";

   public AnchorTapModule() {
      super("AnchorTap", "Charge and explode an anchor with the use key", Category.COMBAT, -1);
      M = this;
   }

   public static boolean b() {
      if (M != null && M.isEnabled()) {
         AnchorTapModule var0 = M;
         if (var0 == null || !var0.isRuntimeActive() || !d8.a()) {
            return false;
         } else {
            return var0.a != AnchorTapModule.q.i
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
      return this.F;
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   @Override
   public void onTick() {
      m.g(this);
      if (d8.a() && mc.isWindowActive()) {
         boolean var1 = mc.options.keyUse.isDown();
         if (var1) {
            boolean var13 = false;
            byte var12 = this.y;
            int var10000 = Integer.signum(var12);
            if ((var10000 | -var10000) >>> 31 == 0
               && this.a == AnchorTapModule.q.i
               && mc.hitResult instanceof BlockHitResult var2
               && mc.level.getBlockState(var2.getBlockPos()).is(Blocks.RESPAWN_ANCHOR)) {
               this.p = var2.getBlockPos().immutable();
               this.a = AnchorTapModule.q.x;
               this.z = mc.level.getGameTime();
               this.g = 0L;
               r4.g("AnchorTap", "Anchor cycle started at {}", this.p);
            }
         }

         this.y = var1;
         if (this.a != AnchorTapModule.q.i && this.p != null) {
            long var16 = mc.level.getGameTime();
            if (mc.level.hasChunkAt(this.p) && !mc.player.isUsingItem()) {
               if (var16 - this.z > 40L) {
                  this.k("Anchor response timed out");
               } else if (var16 >= this.g) {
                  BlockState var4 = mc.level.getBlockState(this.p);
                  boolean var5 = var4.is(Blocks.RESPAWN_ANCHOR);
                  switch (this.a) {
                     case x:
                        if (!var5) {
                           this.k("Anchor no longer exists");
                           return;
                        }

                        if ((Integer)var4.getValue(RespawnAnchorBlock.CHARGE) > 0) {
                           this.a(AnchorTapModule.q.L);
                           return;
                        }

                        this.Y(
                           Items.GLOWSTONE,
                           AnchorTapModule.q.A,
                           () -> mc.level.getBlockState(this.p).is(Blocks.RESPAWN_ANCHOR)
                              && (Integer)mc.level.getBlockState(this.p).getValue(RespawnAnchorBlock.CHARGE) == 0
                        );
                        break;
                     case A:
                        if (!var5) {
                           this.k("Anchor removed before charging");
                        } else if ((Integer)var4.getValue(RespawnAnchorBlock.CHARGE) > 0) {
                           this.a(AnchorTapModule.q.L);
                        } else if (var16 - this.z > 8L) {
                           this.k("Anchor charge not confirmed");
                        }
                        break;
                     case L:
                        if (!this.H.getValue()) {
                           this.a(AnchorTapModule.q.w);
                           return;
                        }

                        BlockPos var18 = this.r();
                        if (!m.Q(var18)) {
                           BlockState var19 = mc.level.getBlockState(var18);
                           if (!var19.is(Blocks.OBSIDIAN) && !var19.is(Blocks.CRYING_OBSIDIAN) && !var19.is(Blocks.BEDROCK)) {
                              this.k("Safe Anchor shield position occupied by another block");
                           } else {
                              this.a(AnchorTapModule.q.w);
                           }

                           return;
                        }

                        if (mc.player.getBoundingBox().intersects(new AABB(var18))) {
                           this.k("No safe shield position outside your body");
                           return;
                        }

                        int var23 = m.Q(Items.OBSIDIAN, this.Y.getValue());
                        byte var11 = -1;
                        int var10 = var23;
                        var23 = Integer.compare(var10, var11);
                        if ((var23 | -var23) >>> 31 == 0) {
                           this.k("Safe Anchor requires hotbar obsidian");
                           return;
                        }

                        boolean var7 = m.U(
                           this,
                           var18,
                           Items.OBSIDIAN,
                           this.d.getValue(),
                           this.S.i("Visible"),
                           this.Y.getValue(),
                           () -> this.a == AnchorTapModule.q.L && this.V() && !mc.player.getBoundingBox().intersects(new AABB(var18)),
                           var1x -> {
                              if (var1x) {
                                 this.g = mc.level.getGameTime() + Math.max(1, this.b.getValue().intValue());
                                 this.F = "Waiting for obsidian shield";
                              }
                           }
                        );
                        if (!var7) {
                           this.F = "Aim at a reachable shield support";
                        }
                        break;
                     case w:
                        if (!this.V()) {
                           this.k("Charged anchor no longer exists");
                           return;
                        }

                        if ((Boolean)mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, this.p)) {
                           this.k("Anchors set spawn here; detonation skipped");
                           return;
                        }

                        if (mc.player.getOffhandItem().is(Items.GLOWSTONE)) {
                           int var22 = (Integer)var4.getValue(RespawnAnchorBlock.CHARGE);
                           byte var9 = 4;
                           int var8 = var22;
                           if ((Integer.compare(var8, var9) >>> 31 ^ 1) == 0) {
                              this.F = "Move offhand glowstone before detonation";
                              return;
                           }
                        }

                        Item var17 = this.p();
                        if (var17 == null) {
                           this.k("A non-glowstone hotbar item is required");
                           return;
                        }

                        this.Y(
                           var17,
                           AnchorTapModule.q.b,
                           () -> this.V()
                              && (
                                 !mc.player.getOffhandItem().is(Items.GLOWSTONE)
                                    || (Integer)mc.level.getBlockState(this.p).getValue(RespawnAnchorBlock.CHARGE) == 4
                              )
                              && !(Boolean)mc.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, this.p)
                        );
                        break;
                     case b:
                        if (!var5) {
                           if (this.n.getValue() && m.Q(this.p)) {
                              this.a(AnchorTapModule.q.g);
                           } else {
                              this.k("Anchor cycle complete");
                           }
                        } else if (var16 - this.z > 8L) {
                           this.k("Anchor removal not confirmed");
                        }
                        break;
                     case g:
                        if (!m.Q(this.p)) {
                           this.k("Replacement position occupied");
                           return;
                        }

                        int var20 = m.Q(Items.RESPAWN_ANCHOR, this.Y.getValue());
                        byte var15 = -1;
                        int var14 = var20;
                        var20 = Integer.compare(var14, var15);
                        if ((var20 | -var20) >>> 31 == 0) {
                           this.k("Replacement anchor missing from hotbar");
                           return;
                        }

                        boolean var6 = m.U(
                           this,
                           this.p,
                           Items.RESPAWN_ANCHOR,
                           this.d.getValue(),
                           this.S.i("Visible"),
                           this.Y.getValue(),
                           () -> this.a == AnchorTapModule.q.g && m.Q(this.p),
                           var1x -> {
                              if (var1x) {
                                 this.a(AnchorTapModule.q.p);
                              }
                           }
                        );
                        if (!var6) {
                           this.F = "Aim at a reachable replacement support";
                        }
                        break;
                     case p:
                        if (var5) {
                           this.k("Anchor replacement complete");
                        } else if (var16 - this.z > 8L) {
                           this.k("Anchor replacement not confirmed");
                        }
                  }
               }
            } else {
               this.k("Anchor cycle cancelled");
            }
         }
      } else {
         this.k("Anchor interaction suspended");
      }
   }

   private BlockPos r() {
      Vec3 var1 = mc.player.position().subtract(Vec3.atCenterOf(this.p));
      Direction var2 = Math.abs(var1.x) > Math.abs(var1.z)
         ? (var1.x >= 0.0 ? Direction.EAST : Direction.WEST)
         : (var1.z >= 0.0 ? Direction.SOUTH : Direction.NORTH);
      return this.p.relative(var2);
   }

   private boolean V() {
      return this.p != null
         && mc.level.getBlockState(this.p).is(Blocks.RESPAWN_ANCHOR)
         && (Integer)mc.level.getBlockState(this.p).getValue(RespawnAnchorBlock.CHARGE) > 0;
   }

   private Item p() {
      ItemStack var1 = mc.player.getMainHandItem();
      if (!var1.is(Items.GLOWSTONE)) {
         return var1.getItem();
      }

      if (this.Y.getValue()) {
         for (int var2 = 0; var2 < 9; var2++) {
            ItemStack var3 = mc.player.getInventory().getItem(var2);
            if (!var3.is(Items.GLOWSTONE)) {
               return var3.getItem();
            }
         }
      }

      return null;
   }

   private void Y(Item var1, AnchorTapModule.q var2, BooleanSupplier var3) {
      if (m.Q(var1, this.Y.getValue()) == -1) {
         this.k("Required anchor item missing from hotbar");
      } else {
         AnchorTapModule.q var4 = this.a;
         boolean var5 = m.e(
            this, this.p, var1, this.d.getValue(), this.S.i("Visible"), this.Y.getValue(), () -> this.a == var4 && var3.getAsBoolean(), var2x -> {
               if (var2x) {
                  this.a(var2);
                  this.g = mc.level.getGameTime() + Math.max(1, this.b.getValue().intValue());
               } else {
                  this.F = "Anchor interaction rejected; retrying";
               }
            }
         );
         if (!var5) {
            this.F = "Aim at the anchor within native reach";
         }
      }
   }

   private void a(AnchorTapModule.q var1) {
      this.a = var1;
      this.z = mc.level.getGameTime();
      this.F = var1.name().toLowerCase().replace('_', ' ');
   }

   private void k(String var1) {
      m.g(this);
      if (this.a != AnchorTapModule.q.i) {
         r4.g("AnchorTap", "{}", var1);
      }

      this.a = AnchorTapModule.q.i;
      this.p = null;
      this.z = 0L;
      this.g = 0L;
      this.F = var1;
   }

   @Override
   public void onEnable() {
      this.y = (boolean)(mc.player != null && mc.options.keyUse.isDown() ? 1 : 0);
      this.k("Aim at an anchor and press Use");
      r4.g("AnchorTap", "Anchor use handler active");
   }

   @Override
   public void onDisable() {
      this.k("Anchor intents cleared");
      this.y = (boolean)0;
   }

   private enum q {
      i,
      x,
      A,
      L,
      w,
      b,
      g,
      p;
   }
}
