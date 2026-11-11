package com.larpclient.social.misc;

import com.larpclient.LarpClient;
import com.larpclient.config.ConfigManager;
import com.larpclient.hud.HudPanels;
import com.larpclient.module.render.ViewModelModule;
import com.larpclient.render.overlay.ViewModelOutline;
import com.larpclient.util.data.HandBoundsTracker;
import com.larpclient.util.misc.LarpLog;
import java.util.Locale;
import java.util.function.BooleanSupplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.InteractionHand;

public final class ViewModelChatEditor {
   private static final long sAVE_RETRY_NANOS = 750000000L;
   private final BooleanSupplier saveAction;
   private InteractionHand selectedHand;
   private InteractionHand draggingHand;
   private HandBoundsTracker.HandBounds dragBounds;
   private double dragStartX;
   private double dragStartY;
   private double startOffsetX;
   private double startOffsetY;
   private double scrollAccumulator;
   private boolean dirty;
   private long dirtySinceNanos;

   public ViewModelChatEditor() {
      this(() -> {
         ConfigManager var0 = LarpClient.getInstance().getConfigManager();
         return var0.saveProfile(var0.getActiveProfile());
      });
   }

   public ViewModelChatEditor(BooleanSupplier var1) {
      this.saveAction = var1;
   }

   private static ViewModelModule getModule() {
      ViewModelModule var0 = ViewModelModule.getInstance();
      return var0 != null && var0.isRuntimeActive() ? var0 : null;
   }

   public boolean isDragging() {
      return this.draggingHand != null;
   }

   public InteractionHand isDragging() {
      return this.selectedHand;
   }

   private InteractionHand findHandAt(double var1, double var3) {
      if (this.selectedHand != null && isOverHand(this.selectedHand, var1, var3)) {
         return this.selectedHand;
      }

      for (InteractionHand var8 : InteractionHand.values()) {
         if (isOverHand(var8, var1, var3)) {
            return var8;
         }
      }

      return null;
   }

   private static boolean isOverHand(InteractionHand var0, double var1, double var3) {
      HandBoundsTracker.HandBounds var5 = HandBoundsTracker.getBounds(var0);
      return var5 != null && var5.contains(var1, var3);
   }

   public boolean mouseClicked(double var1, double var3, int var5) {
      ViewModelModule var6 = getModule();
      if (this.isDragging()) {
         return true;
      } else {
         InteractionHand var7 = var6 == null ? null : this.findHandAt(var1, var3);
         if (var7 == null) {
            this.clearSelection();
            return false;
         } else if (var5 == 0) {
            this.dragBounds = HandBoundsTracker.getBounds(var7);
            this.selectedHand = this.draggingHand = var7;
            this.dragStartX = var1;
            this.dragStartY = var3;
            this.startOffsetX = var6.getOffsetX(var7);
            this.startOffsetY = var6.getOffsetY(var7);
            LarpLog.debug("ViewModel", "Dragging {} in chat", getHandLabel(var7));
            return true;
         } else if (var5 == 1) {
            this.selectedHand = var7;
            var6.resetHand(var7);
            this.mouseClicked();
            this.savePlacement();
            LarpLog.debug("ViewModel", "Reset {} size and position from chat", getHandLabel(var7));
            return true;
         } else {
            return false;
         }
      }
   }

   public boolean mouseDragged(double var1, double var3, int var5) {
      if (this.isDragging() && var5 == 0) {
         ViewModelModule var6 = getModule();
         if (var6 == null) {
            this.endDrag();
            return true;
         }

         if (this.dragBounds == null) {
            return true;
         }

         double var7 = (var1 - this.dragStartX) * this.dragBounds.unitsPerPixelX() * (this.dragBounds.left() ? -1 : 1);
         double var9 = -(var3 - this.dragStartY) * this.dragBounds.unitsPerPixelY();
         var6.setOffset(this.draggingHand, this.startOffsetX + var7, this.startOffsetY + var9);
         this.mouseClicked();
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(int var1) {
      if (this.isDragging() && var1 == 0) {
         this.endDrag();
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5) {
      ViewModelModule var7 = getModule();
      if (var7 != null && this.selectedHand != null && isOverHand(this.selectedHand, var1, var3)) {
         this.scrollAccumulator += var5;
         int var8 = (int)this.scrollAccumulator;
         if (var8 != 0) {
            this.scrollAccumulator -= var8;
            var7.applyTranslation(this.selectedHand, var8);
            this.mouseClicked();
         }

         return true;
      } else {
         return false;
      }
   }

   public void clearSelection() {
      if (this.isDragging()) {
         this.endDrag();
      }

      this.selectedHand = null;
      this.scrollAccumulator = 0.0;
   }

   public void setChatOpen(boolean var1) {
      if (!var1 && this.isDragging()) {
         this.endDrag();
      }
   }

   public void close() {
      this.clearSelection();
      this.savePlacement();
   }

   private void endDrag() {
      this.draggingHand = null;
      this.dragBounds = null;
      this.savePlacement();
   }

   private void mouseClicked() {
      this.dirty = (boolean)1;
      this.dirtySinceNanos = System.nanoTime();
   }

   private void savePlacement() {
      if (this.dirty) {
         if (this.saveAction.getAsBoolean()) {
            this.dirty = (boolean)0;
            LarpLog.debug("ViewModel", "Saved chat hand placement to the active profile");
         } else {
            LarpLog.warn("ViewModel", "Hand placement save failed; retrying on the next edit or when chat closes");
         }
      }
   }

   public void render(GuiGraphicsExtractor var1, int var2, int var3, boolean var4) {
      ViewModelModule var5 = getModule();
      if (var5 == null) {
         if (this.selectedHand != null || this.isDragging()) {
            this.clearSelection();
         }
      } else {
         if (this.dirty && !this.isDragging() && System.nanoTime() - this.dirtySinceNanos > 750000000L) {
            this.savePlacement();
         }

         InteractionHand var6 = !var4 && !this.isDragging() ? this.findHandAt(var2, var3) : null;
         ViewModelOutline.setHands(this.selectedHand, var6);

         for (InteractionHand var10 : InteractionHand.values()) {
            HandBoundsTracker.HandBounds var11 = HandBoundsTracker.getBounds(var10);
            if (var11 != null && (var10 == this.selectedHand || var10 == var6)) {
               String var12 = getHandLabel(var10) + " " + String.format(Locale.ROOT, "%.2fx", var5.getScale(var10));
               int var13 = var11.screenY() > 16 ? var11.screenY() - 13 : var11.screenY() + var11.height() + 3;
               HudPanels.drawText(var1, var12, Math.max(2, var11.screenX()), var13, 10.0, var10 == this.selectedHand ? -1 : -1325400065);
               if (var10 == this.selectedHand && !this.isDragging()) {
                  HudPanels.drawText(
                     var1,
                     "Drag to move, scroll to resize, right-click to reset",
                     Math.max(2, var11.screenX()),
                     var11.screenY() > 16 ? var13 - 11 : var13 + 11,
                     9.0,
                     -1694498817
                  );
               }
            }
         }
      }
   }

   private static String getHandLabel(InteractionHand var0) {
      return var0 == InteractionHand.OFF_HAND ? "Offhand" : "Main hand";
   }
}
