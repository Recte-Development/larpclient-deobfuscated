package com.larpclient.hud;

import com.larpclient.LarpClient;
import com.larpclient.config.ConfigManager;
import com.larpclient.gui.clickgui.ClickGuiScreen;
import com.larpclient.gui.hud.HudEditorPopup;
import com.larpclient.gui.misc.UiState;
import com.larpclient.gui.theme.Icons;
import com.larpclient.gui.theme.Theme;
import com.larpclient.render.overlay.SdfText;
import com.larpclient.util.math.DragSmoother;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public final class HudEditor {
   private final BooleanSupplier commitProfile;
   private final Consumer<HudLayout.HudElement> openSettings;
   private final Runnable notifySaveFailed;
   private HudLayout.HudElement selected;
   private HudLayout.HudElement activeElement;
   private HudLayout2.DragGrab moveGesture;
   private HudLayout2.ResizeDrag scaleGesture;
   private UiState.HudPosition initialPlacement;
   private UiState.HudStyle initialScale;
   private HudLayout2.Rect contextMenu;
   private int guideX = -1;
   private int guideY = -1;
   private long profileGeneration = UiState.revision;
   private boolean dirty;
   private boolean chatEditingStarted;
   private double scaleOffsetX;
   private double scaleOffsetY;
   private String saveError = "";
   private boolean workspaceTools;
   private final HudEditorPopup toolbar = new HudEditorPopup(() -> this.dirty = (boolean)1, this::commitChanges);
   private final DragSmoother dragSmoother = new DragSmoother();
   private double pointerX;
   private double pointerY;
   private boolean snapBypass;
   private long lastDragNanos;
   private boolean workspaceMode;
   private Predicate<HudLayout.HudElement> lockedPredicate = var0 -> false;
   private int startX;
   private int startY;
   private int lockedAxis = -1;

   public HudEditor() {
      this(
         () -> {
            ConfigManager var0 = LarpClient.getInstance().getConfigManager();
            return var0.saveProfile(var0.getActiveProfile());
         },
         var0 -> {
            Minecraft var1 = Minecraft.getInstance();
            var1.gui.setScreen(ClickGuiScreen.openHudSettings(var1.gui.screen(), var0));
         },
         () -> Minecraft.getInstance()
            .gui
            .hud
            .getChat()
            .addClientSystemMessage(Component.literal("HUD save failed. Right-click an overlay and choose Retry save. See the client log."))
      );
      this.workspaceTools = (boolean)1;
      this.lockedPredicate = var0 -> LarpClient.getInstance().getConfigManager().getPreferences().isHudLocked(var0.id);
   }

   public HudEditor(BooleanSupplier var1, Consumer<HudLayout.HudElement> var2, Runnable var3) {
      this.commitProfile = var1;
      this.openSettings = var2;
      this.notifySaveFailed = var3;
   }

   public void enableWorkspaceMode(Predicate<HudLayout.HudElement> var1) {
      this.workspaceMode = (boolean)1;
      this.lockedPredicate = Objects.requireNonNull(var1);
   }

   public void select(HudLayout.HudElement var1) {
      if (this.isDragging()) {
         this.endGesture();
      }

      this.selected = var1;
      this.contextMenu = null;
   }

   public int endGesture() {
      return this.guideX;
   }

   public int getGuideY() {
      return this.guideY;
   }

   public HudLayout.HudElement isOverEditorUi() {
      return this.selected;
   }

   public void closeContextMenu() {
      this.contextMenu = null;
      this.toolbar.close();
   }

   public boolean isDragging() {
      return this.activeElement != null;
   }

   public boolean isOverEditorUi(double var1, double var3, int var5, int var6, List<HudLayout.ElementBounds> var7) {
      if (!this.isDragging() && (this.contextMenu == null || !this.contextMenu.contains(var1, var3, 0))) {
         if (this.workspaceTools && this.toolbar.isOpen() && this.toolbar.getScaledBounds().contains(var1, var3, 0)) {
            return true;
         }

         List var8 = var7.stream().filter(var1x -> this.workspaceMode || !var1x.element().isHidden()).toList();
         return var8.stream().anyMatch(var4 -> var4.bounds().contains(var1, var3, 0))
            ? true
            : this.workspaceTools
               && HudLayout.addBounds()
                  .stream()
                  .filter(var1x -> !this.lockedPredicate.test(var1x) && !HudLayout.isAliasActive(var1x))
                  .anyMatch(
                     var7x -> HudLayout.findElementBounds(var8, var7x)
                        .map(var6x -> HudLayout2.resizeHandleBounds(var6x, var5, var6).contains(var1, var3, 0))
                        .orElse(false)
                  );
      } else {
         return true;
      }
   }

   public boolean mousePressed(double var1, double var3, int var5, int var6, int var7, List<HudLayout.ElementBounds> var8, boolean var9) {
      if (this.isDragging()) {
         return true;
      }

      if (var9) {
         this.contextMenu = null;
         return false;
      }

      if (this.workspaceTools && this.toolbar.mouseClicked(var1, var3, var5, var6, var7)) {
         return true;
      }

      if (this.contextMenu != null && this.contextMenu.contains(var1, var3, 0) && var5 == 0) {
         int var15 = (int)(var3 - this.contextMenu.top()) / 20;
         this.contextMenu = null;
         switch (var15) {
            case 0:
               this.selected.setHidden(true);
               this.dirty = (boolean)1;
               this.commitChanges();
               this.selected = null;
               break;
            case 1:
               this.commitChanges();
               this.openSettings.accept(this.selected);
               break;
            case 2:
               this.selected.resetPlacement();
               this.dirty = (boolean)1;
               this.commitChanges();
               break;
            case 3:
               this.commitChanges();
         }

         return true;
      } else {
         this.contextMenu = null;
         if (var5 != 0 && var5 != 1) {
            return false;
         }

         List var10 = var8.stream().filter(var1x -> this.workspaceMode || !var1x.element().isHidden()).toList();
         Optional var11 = HudLayout.findElementBounds(var10, this.selected);
         HudLayout.HudElement var12 = this.workspaceTools && var5 == 0
            ? HudLayout.addBounds()
               .stream()
               .filter(var1x -> !this.lockedPredicate.test(var1x) && !HudLayout.isAliasActive(var1x))
               .filter(
                  var7x -> HudLayout.findElementBounds(var10, var7x)
                     .map(var6x -> HudLayout2.resizeHandleBounds(var6x, var6, var7).contains(var1, var3, 0))
                     .orElse(false)
               )
               .reduce((var0, var1x) -> var1x)
               .orElse(null)
            : null;
         if (var12 != null) {
            this.selected = var12;
            var11 = HudLayout.findElementBounds(var10, this.selected);
         }

         if (var5 == 0
            && this.selected != null
            && !this.lockedPredicate.test(this.selected)
            && !HudLayout.isAliasActive(this.selected)
            && var11.isPresent()
            && HudLayout2.resizeHandleBounds((HudLayout2.Rect)var11.get(), var6, var7).contains(var1, var3, 0)) {
            this.beginGesture(this.selected);
            this.startX = this.selected.getX(var6);
            this.startY = this.selected.getY(var7);
            HudLayout2.Rect var16 = (HudLayout2.Rect)var11.get();
            this.scaleGesture = new HudLayout2.ResizeDrag(var16, var1, var3, this.selected.effectiveScale());
            this.scaleOffsetX = (var16.left() - this.selected.getX(var6)) / this.selected.effectiveScale();
            this.scaleOffsetY = (var16.top() - this.selected.getY(var7)) / this.selected.effectiveScale();
            return true;
         }

         HudLayout.ElementBounds var13 = HudLayout2.findTopmostAt(var10, var0 -> true, HudLayout.ElementBounds::bounds, var1, var3);
         if (var13 == null) {
            this.selected = null;
            if (this.workspaceTools && var5 == 1) {
               this.toolbar.openElementList((int)var1, (int)var3, var6, var7);
               return true;
            } else {
               return false;
            }
         } else {
            this.selected = var13.element();
            LarpLog.debug("HUD editor", "Selected {} in {}", this.selected.id, this.workspaceMode ? "workspace" : "chat");
            if (var5 == 1) {
               if (this.workspaceTools) {
                  this.toolbar.openElementSettings(this.selected, (int)var1, (int)var3, var6, var7);
                  return true;
               }

               int var14 = this.saveError.isEmpty() ? 60 : 80;
               this.contextMenu = new HudLayout2.Rect(
                  HudLayout2.clampPosition((int)var1, 142, var6), HudLayout2.clampPosition((int)var3, var14, var7), Math.min(142, var6), Math.min(var14, var7)
               );
            } else {
               if (this.lockedPredicate.test(this.selected)) {
                  return true;
               }

               this.beginGesture(this.selected);
               if (HudLayout.isAliasActive(this.selected)) {
                  this.selected.setPosition(var13.bounds().left(), var13.bounds().top(), var6, var7);
                  this.selected.popScale((String)null);
                  HudLayout.setSelected(this.selected);
                  LarpLog.debug("HUD editor", "Lifted {} out of {}", this.selected.id, HudLayout.renderHud(this.selected.id));
               }

               this.startX = this.selected.getX(var6);
               this.startY = this.selected.getY(var7);
               this.moveGesture = new HudLayout2.DragGrab(var1 - this.selected.getX(var6), var3 - this.selected.getY(var7));
               this.dragSmoother.reset(this.selected.getX(var6), this.selected.getY(var7));
               this.pointerX = var1;
               this.pointerY = var3;
               this.snapBypass = (boolean)0;
               this.lastDragNanos = System.nanoTime();
            }

            return true;
         }
      }
   }

   private void beginGesture(HudLayout.HudElement var1) {
      this.activeElement = var1;
      this.lockedAxis = -1;
      this.initialPlacement = UiState.hudPositions.get(var1.id);
      this.initialScale = UiState.hudStyles.get(var1.id);
   }

   public boolean commitChanges(double var1, double var3, int var5, int var6, int var7, boolean var8) {
      return this.mouseDragged(var1, var3, var5, var6, var7, var8, false);
   }

   public boolean mouseDragged(double var1, double var3, int var5, int var6, int var7, boolean var8, boolean var9) {
      if (this.workspaceTools && var5 == 0 && this.toolbar.mouseDragged(var1, var3, var6, var7)) {
         return true;
      }

      if (this.isDragging() && var5 == 0) {
         if (this.scaleGesture != null) {
            this.activeElement.setScale(this.workspaceTools ? DragSmoother.step(this.scaleGesture, var1, var3) : this.scaleGesture.scale(var1, var3));
            this.activeElement
               .setPosition(
                  (int)Math.round(this.scaleGesture.bounds().left() - this.scaleOffsetX * this.activeElement.effectiveScale()),
                  (int)Math.round(this.scaleGesture.bounds().top() - this.scaleOffsetY * this.activeElement.effectiveScale()),
                  var6,
                  var7
               );
         } else {
            if (this.workspaceTools) {
               this.pointerX = var1;
               this.pointerY = var3;
               this.snapBypass = var8;
               return true;
            }

            HudLayout2.DragResult var10 = this.moveGesture
               .move(
                  var1,
                  var3,
                  this.activeElement.setPosition(var6, var7),
                  var6,
                  var7,
                  UiState.hudEditor.snapping(),
                  UiState.hudEditor.grid(),
                  var8,
                  this.guideX,
                  this.guideY
               );
            int var11 = var10.posX();
            int var12 = var10.posY();
            if (this.workspaceMode && var9) {
               int var13 = var11 - this.startX;
               int var14 = var12 - this.startY;
               if (this.lockedAxis < 0 && Math.max(Math.abs(var13), Math.abs(var14)) >= 3) {
                  this.lockedAxis = Math.abs(var13) >= Math.abs(var14) ? 0 : 1;
               }

               if (this.lockedAxis == 0) {
                  var12 = this.startY;
               } else if (this.lockedAxis == 1) {
                  var11 = this.startX;
               }
            } else {
               this.lockedAxis = -1;
            }

            this.activeElement.setPosition(var11, var12, var6, var7);
            this.guideX = var9 && this.lockedAxis == 1 ? -1 : var10.guideX();
            this.guideY = var9 && this.lockedAxis == 0 ? -1 : var10.guideY();
            this.endGesture(var6, var7, var8);
         }

         return true;
      } else {
         return false;
      }
   }

   private void endGesture(int var1, int var2, boolean var3) {
      if (this.activeElement != null && HudLayout.renderHud(this.activeElement.id) != null) {
         int var4 = this.scaleGesture == null && !var3 && HudLayout.isAliasOverlapping(this.activeElement, var1, var2) ? 1 : 0;
         HudLayout.setSelected(var4 != 0 ? this.activeElement : null);
         if (var4 != 0) {
            this.guideX = this.guideY = -1;
         }
      }
   }

   public boolean mouseReleased(int var1) {
      if (this.workspaceTools && var1 == 0 && this.toolbar.mouseReleased()) {
         return true;
      } else if (this.isDragging() && var1 == 0) {
         this.endGesture();
         return true;
      } else {
         return false;
      }
   }

   private void endGesture() {
      if (this.activeElement != null && HudLayout.getSelected() == this.activeElement) {
         String var1 = HudLayout.renderHud(this.activeElement.id);
         this.activeElement.popScale(var1);
         LarpLog.info("HUD editor", "Merged {} into {}", this.activeElement.id, var1);
      }

      HudLayout.setSelected(null);
      if (this.activeElement != null) {
         this.dirty = (boolean)(this.dirty
            | (
               Objects.equals(this.initialPlacement, UiState.hudPositions.get(this.activeElement.id))
                     && Objects.equals(this.initialScale, UiState.hudStyles.get(this.activeElement.id))
                  ? 0
                  : 1
            ));
         LarpLog.debug("HUD editor", "Released {} gesture for {}", this.scaleGesture == null ? "move" : "scale", this.activeElement.id);
      }

      this.activeElement = null;
      this.moveGesture = null;
      this.scaleGesture = null;
      this.initialPlacement = null;
      this.initialScale = null;
      this.guideX = this.guideY = -1;
      this.commitChanges();
   }

   private void commitChanges() {
      if (this.dirty) {
         if (this.commitProfile.getAsBoolean()) {
            this.dirty = (boolean)0;
            this.saveError = "";
            LarpLog.debug("HUD editor", "Committed {} edit to active profile", this.workspaceMode ? "workspace" : "chat");
         } else {
            if (this.saveError.isEmpty()) {
               this.notifySaveFailed.run();
            }

            this.saveError = "HUD save failed. Retry save in the menu.";
            LarpLog.warn("HUD editor", "HUD save failed; pending changes retained for retry/close");
         }
      }
   }

   public void updateChatState(boolean var1, boolean var2) {
      if (this.profileGeneration != UiState.revision) {
         this.toolbar.reset();
         this.activeElement = null;
         this.moveGesture = null;
         this.scaleGesture = null;
         this.dirty = (boolean)0;
         this.saveError = "";
         HudLayout.setSelected(null);
         this.guideX = this.guideY = -1;
         this.selected = null;
         this.contextMenu = null;
         this.profileGeneration = UiState.revision;
         LarpLog.debug("HUD editor", "Released chat editing after profile replacement");
      }

      if (var1 && var2) {
         this.chatEditingStarted = (boolean)0;
      } else {
         if (!this.chatEditingStarted) {
            this.close();
         }

         this.chatEditingStarted = (boolean)1;
      }
   }

   public void close() {
      this.toolbar.close();
      this.endGesture();
      this.selected = null;
      this.contextMenu = null;
   }

   public boolean mouseDragged(int var1) {
      return this.workspaceTools && this.toolbar.keyPressed(var1);
   }

   public boolean keyReleased(int var1) {
      return this.workspaceTools && this.toolbar.charTyped(var1);
   }

   public boolean mouseScrolled(double var1, double var3, double var5, boolean var7) {
      return this.workspaceTools && !var7 && this.toolbar.mouseScrolled(var1, var3, var5);
   }

   static void isOverEditorUi(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
      int var8 = var1 / 2;
      int var9 = var2 / 2;
      int var10 = var4 * var5;

      for (int var11 = HudLayout2.wrapIndex(var1, var4); var11 < var1; var11 += var4) {
         if (var11 != var8) {
            DrawUtil.fillRect(var0, var11, 0, 1, var2, (var11 - var8) % var10 == 0 ? var7 : var6);
         }
      }

      for (int var12 = HudLayout2.wrapIndex(var2, var4); var12 < var3; var12 += var4) {
         if (var12 != var9) {
            DrawUtil.fillRect(var0, 0, var12, var1, 1, (var12 - var9) % var10 == 0 ? var7 : var6);
         }
      }
   }

   static void drawCenterGuides(GuiGraphicsExtractor var0, int var1, int var2, int var3, int var4) {
      int var5 = var1 / 2;
      int var6 = var2 / 2;
      int var7 = ColorUtil.withAlpha(Theme.accent, 0.9);
      int var8 = 1224736767;
      DrawUtil.fillRect(var0, var5, 0, 1, var2, var3 == var5 ? var7 : var8);
      DrawUtil.fillRect(var0, 0, var6, var1, 1, var4 == var6 ? var7 : var8);
      if (var3 >= 0 && var3 != var5) {
         DrawUtil.fillRect(var0, Math.min(var1 - 1, var3), 0, 1, var2, var7);
      }

      if (var4 >= 0 && var4 != var6) {
         DrawUtil.fillRect(var0, 0, Math.min(var2 - 1, var4), var1, 1, var7);
      }
   }

   public void render(GuiGraphicsExtractor var1, int var2, int var3, int var4, int var5, boolean var6) {
      if (this.workspaceTools && this.activeElement != null && this.moveGesture != null) {
         long var7 = System.nanoTime();
         HudLayout2.DragResult var9 = this.moveGesture
            .move(
               this.pointerX,
               this.pointerY,
               this.activeElement.setPosition(var4, var5),
               var4,
               var5,
               UiState.hudEditor.snapping(),
               UiState.hudEditor.grid(),
               this.snapBypass,
               this.guideX,
               this.guideY
            );
         HudLayout2.DragResult var10 = this.dragSmoother
            .step(var9.posX(), var9.posY(), Minecraft.getInstance().hasShiftDown(), (var7 - this.lastDragNanos) / 1.0E9);
         this.lastDragNanos = var7;
         this.activeElement.setPosition(var10.posX(), var10.posY(), var4, var5);
         this.guideX = var9.guideX();
         this.guideY = var9.guideY();
         this.endGesture(var4, var5, this.snapBypass);
      }

      List var12 = HudLayout.collectVisibleBounds();
      Optional var8 = HudLayout.findElementBounds(var12, this.selected);
      if (this.selected != null && var8.isEmpty() && !this.isDragging()) {
         this.selected = null;
         this.contextMenu = null;
      }

      HudLayout.ElementBounds var13 = var6 ? null : HudLayout2.findTopmostAt(var12, var0 -> true, HudLayout.ElementBounds::bounds, var2, var3);
      if (var13 != null && var13.element() != this.selected) {
         drawOutline(var1, var13.bounds(), 1620481696);
      }

      if (var8.isPresent()) {
         HudLayout2.Rect var14 = (HudLayout2.Rect)var8.get();
         drawOutline(var1, var14, ColorUtil.withAlpha(Theme.accent, 0.8));
         if (!this.lockedPredicate.test(this.selected) && !HudLayout.isAliasActive(this.selected)) {
            HudLayout2.Rect var11 = HudLayout2.resizeHandleBounds(var14, var4, var5);
            DrawUtil.fillRoundedRect(var1, var11.left(), var11.top(), var11.width(), var11.height(), 2.0F, Theme.accent);
         }
      }

      if (this.isDragging()) {
         if (this.workspaceTools) {
            isOverEditorUi(var1, var4, var5, var5 - 14, 20, 4, 268435455, 587202559);
         }

         drawCenterGuides(var1, var4, var5, this.guideX, this.guideY);
      }

      if (this.workspaceTools) {
         for (HudLayout.HudElement var17 : HudLayout.addBounds()) {
            if (!this.lockedPredicate.test(var17) && !HudLayout.isAliasActive(var17)) {
               HudLayout.findElementBounds(var12, var17)
                  .ifPresent(
                     var5x -> {
                        HudLayout2.Rect var6x = HudLayout2.resizeHandleBounds(var5x, var4, var5);
                        DrawUtil.fillRoundedRect(
                           var1,
                           var6x.left(),
                           var6x.top(),
                           var6x.width(),
                           var6x.height(),
                           2.0F,
                           ColorUtil.withAlpha(Theme.accent, var6x.contains(var2, var3, 0) ? 0.6 : 0.35)
                        );
                        DrawUtil.fillRect(var1, var6x.left() + 2, var6x.top() + var6x.height() - 2, var6x.width() - 4, 1, -1073741825);
                        DrawUtil.fillRect(var1, var6x.left() + var6x.width() - 2, var6x.top() + 2, 1, var6x.height() - 4, -1073741825);
                     }
                  );
            }
         }

         this.toolbar.render(var1, var2, var3);
      }

      if (this.contextMenu != null) {
         DrawUtil.fillRoundedRect(var1, this.contextMenu.left(), this.contextMenu.top(), this.contextMenu.width(), this.contextMenu.height(), 6.0F, -267645934);
         String[] var16 = new String[]{"Hide", "Settings", "Reset placement", "Retry save"};

         for (int var18 = 0; var18 < this.contextMenu.height() / 20; var18++) {
            if (!var6 && this.contextMenu.contains(var2, var3, 0) && (var3 - this.contextMenu.top()) / 20 == var18) {
               DrawUtil.fillRoundedRect(
                  var1, this.contextMenu.left() + 2, this.contextMenu.top() + var18 * 20, this.contextMenu.width() - 4, 20.0F, 4.0F, 905969663
               );
            }

            Icons.texture(
               var1,
               Icons.get(new String[]{"eye", "settings", "rotate-ccw", "badge-check"}[var18]),
               this.contextMenu.left() + 7,
               this.contextMenu.top() + var18 * 20 + 4,
               12,
               Theme.accent
            );
            HudPanels.drawText(
               var1, var16[var18], this.contextMenu.left() + 25, SdfText.centerBaselineY(this.contextMenu.top() + var18 * 20, 20.0, 11.0), 11.0, -1
            );
         }
      }
   }

   private static void drawOutline(GuiGraphicsExtractor var0, HudLayout2.Rect var1, int var2) {
      DrawUtil.drawOutline(var0, var1.left() - 1, var1.top() - 1, var1.width() + 2, var1.height() + 2, var2);
   }
}
