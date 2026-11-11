package com.larpclient.module.combat;

import com.larpclient.core.RotationManager;
import com.larpclient.core.ViewController;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.render.world.PlacementHighlight;
import com.larpclient.util.world.NativePlacement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.phys.Vec3;

public final class AutoWebModule extends NativePlacement.PlacementModule implements PlacementHighlight.PlacementHighlightHolder {
   private final PlacementHighlight placementHighlight = new PlacementHighlight(this, var1 -> this.addSetting(var1));
   private static AutoWebModule instance;
   private Object rotationPlayer;
   private Object rotationLevel;
   private int rotationTick;
   private final BooleanSetting legs = this.addBooleanSetting("Legs", "Place webs at the opponent's feet", true);
   private final BooleanSetting head = this.addBooleanSetting("Head", "Place webs at the opponent's head", true);
   private final BooleanSetting surround = this.addBooleanSetting("Surround", "Web the lower perimeter", false);
   private final BooleanSetting upperSurround = this.addBooleanSetting("Upper Surround", "Web the upper perimeter", false);
   private final BooleanSetting webPredict = this.addBooleanSetting("Web Predict", "Predict movement unless the target is already in webs", false);
   private final NumberSetting predictTicks = this.addNumberSetting("Predict Ticks", "Ticks of displacement prediction", 2.0, 0.0, 10.0, 0.5);
   private final BooleanSetting integrateWithTrap = this.addBooleanSetting("Integrate With Trap", "Choose the footprint used by the active AutoTrap", false);
   private final BooleanSetting autoSign = this.addBooleanSetting("Auto Sign", "Use ordinary hotbar signs at head positions instead of head webs", false);

   @Override
   public PlacementHighlight placementHighlight() {
      return this.placementHighlight;
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.placementHighlight.reset();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.placementHighlight.clear();
      this.clearRotation();
   }

   public AutoWebModule() {
      super("AutoWeb", "Place cobwebs around opponents");
      instance = this;
      this.predictTicks.visibleWhen(this.webPredict::getValue);
      this.rotationMode.onChange(var1 -> {
         NativePlacement.cancel(this);
         this.clearRotation();
      });
   }

   public static AutoWebModule getInstance() {
      return instance;
   }

   @Override
   protected void aim(float var1, float var2) {
      if (this.isEnabled()) {
         if (ViewController.canAcquire(ViewController.Owner.AUTO_WEB)) {
            super.aim(var1, var2);
            if (RotationManager.isActive(RotationManager.Priority.DEFENSE)) {
               this.rotationPlayer = minecraft.player;
               this.rotationLevel = minecraft.level;
               this.rotationTick = minecraft.player.tickCount;
               ViewController.acquire(ViewController.Owner.AUTO_WEB);
            } else {
               this.clearRotation();
            }
         }
      }
   }

   private void clearRotation() {
      this.rotationPlayer = this.rotationLevel = null;
      ViewController.release(ViewController.Owner.AUTO_WEB);
   }

   public boolean isRotationVisible() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.rotationMode.isMode("Visible")
            && minecraft.player != null
            && this.rotationPlayer == minecraft.player
            && this.rotationLevel == minecraft.level
            && minecraft.player.tickCount >= this.rotationTick
            && minecraft.player.tickCount - this.rotationTick <= 1
            && (!this.pauseUsingItem.getValue() || !minecraft.player.isUsingItem())
            && (!this.pauseMining.getValue() || !minecraft.options.keyAttack.isDown());
   }

   public float getHeadYaw(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && ViewController.update() == ViewController.Owner.AUTO_WEB && RotationManager.isActive(RotationManager.Priority.DEFENSE)) {
            minecraft.player.setYHeadRot(minecraft.player.getYRot());
            return minecraft.player.getYRot();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private boolean hasWebAt(Player var1) {
      for (BlockPos var3 : AutoTrap.expandTrapPattern(var1, var1.position())) {
         if (minecraft.level.getBlockState(var3).is(Blocks.COBWEB)) {
            return true;
         }
      }

      return false;
   }

   private Item findSignItem() {
      for (int var1 = 0; var1 < 9; var1++) {
         Item var2 = minecraft.player.getInventory().getItem(var1).getItem();
         if (var2 instanceof BlockItem var3 && var3.getBlock() instanceof SignBlock) {
            return var2;
         }
      }

      return null;
   }

   @Override
   public void onTick() {
      this.placementHighlight.setTarget(null);
      NativePlacement.cancel(this);
      boolean var1 = this.isReady();
      if (var1 || this.placementHighlight.isEnabled() && this.canInteract()) {
         Player var2 = this.findTarget();
         if (var2 == null) {
            this.setStatus("Waiting for a target");
         } else {
            int var10001 = this.webPredict.getValue();
            boolean var19 = false;
            byte var18 = (byte)var10001;
            var10001 = Integer.signum(var18);
            Vec3 var3 = AutoTrap.predictPosition(var2, ((var10001 | -var10001) >>> 31 ^ 1) == 0 && !this.hasWebAt(var2) ? this.predictTicks.getValue() : 0.0);
            List var4 = AutoTrap.expandTrapPattern(var2, var3);
            AutoTrap var5 = AutoTrap.getInstance();
            if (this.integrateWithTrap.getValue() && var5 != null && var5.isRuntimeActive()) {
               LinkedHashSet var6 = new LinkedHashSet();

               for (BlockPos var8 : var5.getTrapPositions(var2)) {
                  if (var8.getY() == ((BlockPos)var4.getFirst()).getY() - 1) {
                     var6.add(var8.above());
                  }
               }

               int var10000 = var6.isEmpty();
               boolean var15 = false;
               byte var14 = (byte)var10000;
               var10000 = Integer.signum(var14);
               if ((var10000 | -var10000) >>> 31 == 0) {
                  var4 = new ArrayList(var6);
               }
            }

            Item var22 = this.autoSign.getValue() ? this.findSignItem() : null;
            LinkedHashSet var23 = new LinkedHashSet();
            LinkedHashSet var24 = new LinkedHashSet();

            for (BlockPos var10 : var4) {
               if (this.legs.getValue()) {
                  var23.add(var10);
               }

               if (var22 != null) {
                  var24.add(var10.above());
               } else if (this.head.getValue()) {
                  var23.add(var10.above());
               }

               for (Direction var12 : Plane.HORIZONTAL) {
                  BlockPos var13 = var10.relative(var12);
                  if (!var4.contains(var13)) {
                     if (this.surround.getValue()) {
                        var23.add(var13);
                     }

                     if (this.upperSurround.getValue()) {
                        var23.add(var13.above());
                     }
                  }
               }
            }

            int var30 = NativePlacement.findItemSlot(Items.COBWEB, this.autoSwitch.getValue());
            byte var21 = -1;
            int var20 = var30;
            var30 = Integer.compare(var20, var21);
            if (((var30 | -var30) >>> 31 ^ 1) == 0) {
               for (BlockPos var27 : var23) {
                  if (var1 ? this.requestPlacement(var27, Items.COBWEB, var2) : this.canPlace(var27, Items.COBWEB)) {
                     this.placementHighlight.setTarget(var27);
                     if (var1) {
                        this.setStatus("Preparing web placement");
                     }

                     return;
                  }
               }
            }

            if (var22 != null) {
               for (BlockPos var28 : var24) {
                  if (var1 ? this.requestPlacement(var28, var22, var2) : this.canPlace(var28, var22)) {
                     this.placementHighlight.setTarget(var28);
                     if (var1) {
                        this.setStatus("Preparing sign placement");
                     }

                     return;
                  }
               }
            }

            var10001 = NativePlacement.findItemSlot(Items.COBWEB, this.autoSwitch.getValue());
            byte var17 = -1;
            int var16 = var10001;
            var10001 = Integer.compare(var16, var17);
            this.setStatus(
               (var10001 | -var10001) >>> 31 == 0
                  ? "Cobwebs missing from hotbar"
                  : "No reachable empty web position: " + NativePlacement.getFailureReason(this)
            );
         }
      }
   }
}
