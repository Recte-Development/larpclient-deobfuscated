package com.larpclient.module.combat;

import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.world.ScanPhase;
import com.larpclient.util.misc.InteractionArbiter;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.NativePlacement;
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
   private static AnchorTapModule instance;
   private final NumberSetting delay = this.addSetting(new NumberSetting("Delay", "Ticks between executed anchor steps", 0.0, 0.0, 20.0, 1.0));
   private final BooleanSetting safeAnchor = this.addSetting(
      new BooleanSetting("Safe Anchor", "Place an obsidian shield between you and the anchor before detonation", false)
   );
   private final BooleanSetting airReplace = this.addSetting(new BooleanSetting("Air Replace", "Replace the removed anchor once after detonation", false));
   private final NumberSetting range = this.addSetting(new NumberSetting("Range", "Reach capped by native block interaction distance", 4.1, 1.0, 6.0, 0.1));
   private final ModeSetting rotation = this.addSetting(
      new ModeSetting("Rotation", "Visible turns the real view; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final BooleanSetting autoSwitch = this.addSetting(
      new BooleanSetting("Auto Switch", "Use hotbar items and restore the selected slot after each step", true)
   );
   private AnchorTapModule.Step step = AnchorTapModule.Step.IDLE;
   private BlockPos anchorPos;
   private boolean wasUseDown;
   private long stepStartTick;
   private long nextActionTick;
   private String status = "Aim at an anchor and press Use";

   public AnchorTapModule() {
      super("AnchorTap", "Charge and explode an anchor with the use key", Category.COMBAT, -1);
      instance = this;
   }

   public static boolean isHandlingUse() {
      if (instance != null && instance.isEnabled()) {
         AnchorTapModule var0 = instance;
         if (var0 == null || !var0.isRuntimeActive() || !InteractionArbiter.canInteract()) {
            return false;
         } else {
            return var0.step != AnchorTapModule.Step.IDLE
               ? true
               : minecraft.hitResult instanceof BlockHitResult var1
                  && minecraft.options.keyUse.isDown()
                  && minecraft.level.getBlockState(var1.getBlockPos()).is(Blocks.RESPAWN_ANCHOR);
         }
      } else {
         return false;
      }
   }

   @Override
   public String getStatus() {
      return this.status;
   }

   @Override
   public Module.TickPhase getTickPhase() {
      return Module.TickPhase.MAIN;
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.dONE);
   }

   @Override
   public void onTick() {
      NativePlacement.cancel(this);
      if (InteractionArbiter.canInteract() && minecraft.isWindowActive()) {
         boolean var1 = minecraft.options.keyUse.isDown();
         if (var1) {
            boolean var15 = false;
            byte var14 = this.wasUseDown;
            int var10000 = Integer.signum(var14);
            if ((var10000 | -var10000) >>> 31 == 0
               && this.step == AnchorTapModule.Step.IDLE
               && minecraft.hitResult instanceof BlockHitResult var2
               && minecraft.level.getBlockState(var2.getBlockPos()).is(Blocks.RESPAWN_ANCHOR)) {
               this.anchorPos = var2.getBlockPos().immutable();
               this.step = AnchorTapModule.Step.CHARGE;
               this.stepStartTick = minecraft.level.getGameTime();
               this.nextActionTick = 0L;
               LarpLog.debug("AnchorTap", "Anchor cycle started at {}", this.anchorPos);
            }
         }

         this.wasUseDown = var1;
         if (this.step != AnchorTapModule.Step.IDLE && this.anchorPos != null) {
            long var16 = minecraft.level.getGameTime();
            if (minecraft.level.hasChunkAt(this.anchorPos) && !minecraft.player.isUsingItem()) {
               if (var16 - this.stepStartTick > 40L) {
                  this.reset("Anchor response timed out");
               } else if (var16 >= this.nextActionTick) {
                  BlockState var4 = minecraft.level.getBlockState(this.anchorPos);
                  boolean var5 = var4.is(Blocks.RESPAWN_ANCHOR);
                  switch (this.step) {
                     case CHARGE:
                        if (!var5) {
                           this.reset("Anchor no longer exists");
                           return;
                        }

                        if ((Integer)var4.getValue(RespawnAnchorBlock.CHARGE) > 0) {
                           this.setStep(AnchorTapModule.Step.SHIELD);
                           return;
                        }

                        this.requestInteraction(
                           Items.GLOWSTONE,
                           AnchorTapModule.Step.AWAIT_CHARGE,
                           () -> minecraft.level.getBlockState(this.anchorPos).is(Blocks.RESPAWN_ANCHOR)
                              && (Integer)minecraft.level.getBlockState(this.anchorPos).getValue(RespawnAnchorBlock.CHARGE) == 0
                        );
                        break;
                     case AWAIT_CHARGE:
                        if (!var5) {
                           this.reset("Anchor removed before charging");
                        } else if ((Integer)var4.getValue(RespawnAnchorBlock.CHARGE) > 0) {
                           this.setStep(AnchorTapModule.Step.SHIELD);
                        } else if (var16 - this.stepStartTick > 8L) {
                           this.reset("Anchor charge not confirmed");
                        }
                        break;
                     case SHIELD:
                        if (!this.safeAnchor.getValue()) {
                           this.setStep(AnchorTapModule.Step.DETONATE);
                           return;
                        }

                        BlockPos var18 = this.getShieldPos();
                        if (!NativePlacement.isReplaceable(var18)) {
                           BlockState var19 = minecraft.level.getBlockState(var18);
                           if (!var19.is(Blocks.OBSIDIAN) && !var19.is(Blocks.CRYING_OBSIDIAN) && !var19.is(Blocks.BEDROCK)) {
                              this.reset("Safe Anchor shield position occupied by another block");
                           } else {
                              this.setStep(AnchorTapModule.Step.DETONATE);
                           }

                           return;
                        }

                        if (minecraft.player.getBoundingBox().intersects(new AABB(var18))) {
                           this.reset("No safe shield position outside your body");
                           return;
                        }

                        int var23 = NativePlacement.findItemSlot(Items.OBSIDIAN, this.autoSwitch.getValue());
                        byte var9 = -1;
                        int var8 = var23;
                        var23 = Integer.compare(var8, var9);
                        if ((var23 | -var23) >>> 31 == 0) {
                           this.reset("Safe Anchor requires hotbar obsidian");
                           return;
                        }

                        boolean var7 = NativePlacement.requestPlacement(
                           this,
                           var18,
                           Items.OBSIDIAN,
                           this.range.getValue(),
                           this.rotation.isMode("Visible"),
                           this.autoSwitch.getValue(),
                           () -> this.step == AnchorTapModule.Step.SHIELD
                              && this.isAnchorCharged()
                              && !minecraft.player.getBoundingBox().intersects(new AABB(var18)),
                           var1x -> {
                              if (var1x) {
                                 this.nextActionTick = minecraft.level.getGameTime() + Math.max(1, this.delay.getValue().intValue());
                                 this.status = "Waiting for obsidian shield";
                              }
                           }
                        );
                        if (!var7) {
                           this.status = "Aim at a reachable shield support";
                        }
                        break;
                     case DETONATE:
                        if (!this.isAnchorCharged()) {
                           this.reset("Charged anchor no longer exists");
                           return;
                        }

                        if ((Boolean)minecraft.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, this.anchorPos)) {
                           this.reset("Anchors set spawn here; detonation skipped");
                           return;
                        }

                        if (minecraft.player.getOffhandItem().is(Items.GLOWSTONE)) {
                           int var22 = (Integer)var4.getValue(RespawnAnchorBlock.CHARGE);
                           byte var13 = 4;
                           int var12 = var22;
                           if ((Integer.compare(var12, var13) >>> 31 ^ 1) == 0) {
                              this.status = "Move offhand glowstone before detonation";
                              return;
                           }
                        }

                        Item var17 = this.findDetonationItem();
                        if (var17 == null) {
                           this.reset("A non-glowstone hotbar item is required");
                           return;
                        }

                        this.requestInteraction(
                           var17,
                           AnchorTapModule.Step.AWAIT_REMOVAL,
                           () -> this.isAnchorCharged()
                              && (
                                 !minecraft.player.getOffhandItem().is(Items.GLOWSTONE)
                                    || (Integer)minecraft.level.getBlockState(this.anchorPos).getValue(RespawnAnchorBlock.CHARGE) == 4
                              )
                              && !(Boolean)minecraft.level.environmentAttributes().getValue(EnvironmentAttributes.RESPAWN_ANCHOR_WORKS, this.anchorPos)
                        );
                        break;
                     case AWAIT_REMOVAL:
                        if (!var5) {
                           if (this.airReplace.getValue() && NativePlacement.isReplaceable(this.anchorPos)) {
                              this.setStep(AnchorTapModule.Step.REPLACE);
                           } else {
                              this.reset("Anchor cycle complete");
                           }
                        } else if (var16 - this.stepStartTick > 8L) {
                           this.reset("Anchor removal not confirmed");
                        }
                        break;
                     case REPLACE:
                        if (!NativePlacement.isReplaceable(this.anchorPos)) {
                           this.reset("Replacement position occupied");
                           return;
                        }

                        int var20 = NativePlacement.findItemSlot(Items.RESPAWN_ANCHOR, this.autoSwitch.getValue());
                        byte var11 = -1;
                        int var10 = var20;
                        var20 = Integer.compare(var10, var11);
                        if ((var20 | -var20) >>> 31 == 0) {
                           this.reset("Replacement anchor missing from hotbar");
                           return;
                        }

                        boolean var6 = NativePlacement.requestPlacement(
                           this,
                           this.anchorPos,
                           Items.RESPAWN_ANCHOR,
                           this.range.getValue(),
                           this.rotation.isMode("Visible"),
                           this.autoSwitch.getValue(),
                           () -> this.step == AnchorTapModule.Step.REPLACE && NativePlacement.isReplaceable(this.anchorPos),
                           var1x -> {
                              if (var1x) {
                                 this.setStep(AnchorTapModule.Step.AWAIT_REPLACEMENT);
                              }
                           }
                        );
                        if (!var6) {
                           this.status = "Aim at a reachable replacement support";
                        }
                        break;
                     case AWAIT_REPLACEMENT:
                        if (var5) {
                           this.reset("Anchor replacement complete");
                        } else if (var16 - this.stepStartTick > 8L) {
                           this.reset("Anchor replacement not confirmed");
                        }
                  }
               }
            } else {
               this.reset("Anchor cycle cancelled");
            }
         }
      } else {
         this.reset("Anchor interaction suspended");
      }
   }

   private BlockPos getShieldPos() {
      Vec3 var1 = minecraft.player.position().subtract(Vec3.atCenterOf(this.anchorPos));
      Direction var2 = Math.abs(var1.x) > Math.abs(var1.z)
         ? (var1.x >= 0.0 ? Direction.EAST : Direction.WEST)
         : (var1.z >= 0.0 ? Direction.SOUTH : Direction.NORTH);
      return this.anchorPos.relative(var2);
   }

   private boolean isAnchorCharged() {
      return this.anchorPos != null
         && minecraft.level.getBlockState(this.anchorPos).is(Blocks.RESPAWN_ANCHOR)
         && (Integer)minecraft.level.getBlockState(this.anchorPos).getValue(RespawnAnchorBlock.CHARGE) > 0;
   }

   private Item findDetonationItem() {
      ItemStack var1 = minecraft.player.getMainHandItem();
      if (!var1.is(Items.GLOWSTONE)) {
         return var1.getItem();
      }

      if (this.autoSwitch.getValue()) {
         for (int var2 = 0; var2 < 9; var2++) {
            ItemStack var3 = minecraft.player.getInventory().getItem(var2);
            if (!var3.is(Items.GLOWSTONE)) {
               return var3.getItem();
            }
         }
      }

      return null;
   }

   private void requestInteraction(Item var1, AnchorTapModule.Step var2, BooleanSupplier var3) {
      if (NativePlacement.findItemSlot(var1, this.autoSwitch.getValue()) == -1) {
         this.reset("Required anchor item missing from hotbar");
      } else {
         AnchorTapModule.Step var4 = this.step;
         boolean var5 = NativePlacement.pause(
            this,
            this.anchorPos,
            var1,
            this.range.getValue(),
            this.rotation.isMode("Visible"),
            this.autoSwitch.getValue(),
            () -> this.step == var4 && var3.getAsBoolean(),
            var2x -> {
               if (var2x) {
                  this.setStep(var2);
                  this.nextActionTick = minecraft.level.getGameTime() + Math.max(1, this.delay.getValue().intValue());
               } else {
                  this.status = "Anchor interaction rejected; retrying";
               }
            }
         );
         if (!var5) {
            this.status = "Aim at the anchor within native reach";
         }
      }
   }

   private void setStep(AnchorTapModule.Step var1) {
      this.step = var1;
      this.stepStartTick = minecraft.level.getGameTime();
      this.status = var1.name().toLowerCase().replace('_', ' ');
   }

   private void reset(String var1) {
      NativePlacement.cancel(this);
      if (this.step != AnchorTapModule.Step.IDLE) {
         LarpLog.debug("AnchorTap", "{}", var1);
      }

      this.step = AnchorTapModule.Step.IDLE;
      this.anchorPos = null;
      this.stepStartTick = 0L;
      this.nextActionTick = 0L;
      this.status = var1;
   }

   @Override
   public void onEnable() {
      this.wasUseDown = (boolean)(minecraft.player != null && minecraft.options.keyUse.isDown() ? 1 : 0);
      this.reset("Aim at an anchor and press Use");
      LarpLog.debug("AnchorTap", "Anchor use handler active");
   }

   @Override
   public void onDisable() {
      this.reset("Anchor intents cleared");
      this.wasUseDown = (boolean)0;
   }

   private enum Step {
      IDLE,
      CHARGE,
      AWAIT_CHARGE,
      SHIELD,
      DETONATE,
      AWAIT_REMOVAL,
      REPLACE,
      AWAIT_REPLACEMENT;
   }
}
