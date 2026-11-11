package com.larpclient.hud;

import com.larpclient.gui.misc.UiState;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.Setting;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public final class HudEditorSession {
   public static final int maxHistory = 64;
   private static long failedSaveSession = -1L;
   private final BooleanSupplier saveAction;
   private final Predicate<HudLayout.HudElement> lockedFilter;
   private final List<Module> modules;
   private final HudEditor dragController;
   private final ArrayDeque<HudEditorSession.EditSnapshot> undoStack = new ArrayDeque<>();
   private final ArrayDeque<HudEditorSession.EditSnapshot> redoStack = new ArrayDeque<>();
   private HudEditorSession.EditSnapshot pendingSnapshot;
   private long sessionId = UiState.revision;
   private long saveCount;
   private boolean dirty;
   private boolean forceDirty;
   private String statusText = "Saved";

   public HudEditorSession(BooleanSupplier var1, Predicate<HudLayout.HudElement> var2) {
      this(var1, var2, List.of());
   }

   public HudEditorSession(BooleanSupplier var1, Predicate<HudLayout.HudElement> var2, List<Module> var3) {
      this.modules = List.copyOf(var3);
      this.saveAction = var1;
      this.lockedFilter = var2;
      this.dirty = (boolean)(failedSaveSession == UiState.revision ? 1 : 0);
      if (this.dirty) {
         this.statusText = "Save failed. Retry";
      }

      this.dragController = new HudEditor(this::commit, var0 -> {}, () -> {});
      this.dragController.enableWorkspaceMode(var2);
   }

   public HudEditor commit() {
      return this.dragController;
   }

   public HudLayout.HudElement getSelected() {
      return this.dragController.isOverEditorUi();
   }

   public boolean canUndo() {
      return !this.undoStack.isEmpty();
   }

   public boolean canRedo() {
      return !this.redoStack.isEmpty();
   }

   public int undoCount() {
      return this.undoStack.size();
   }

   public boolean isDirty() {
      return this.dirty;
   }

   public String getStatusText() {
      return this.statusText;
   }

   public void select(HudLayout.HudElement var1) {
      this.dragController.select(var1);
      this.commit();
   }

   public void beginEdit() {
      this.endSession();
      if (this.pendingSnapshot == null) {
         this.pendingSnapshot = HudEditorSession.EditSnapshot.capture(this.modules);
      }
   }

   public void markDirty() {
      this.forceDirty = (boolean)1;
   }

   public void runEdit(Runnable var1) {
      this.beginEdit();
      var1.run();
      this.commit();
   }

   public boolean commit() {
      if (this.sessionId != UiState.revision) {
         this.endSession();
         return true;
      }

      byte var1 = this.forceDirty;
      if (this.pendingSnapshot != null) {
         if (!this.pendingSnapshot.equals(HudEditorSession.EditSnapshot.capture(this.modules))) {
            commit(this.undoStack, this.pendingSnapshot);
            this.redoStack.clear();
            var1 = 1;
         }

         this.pendingSnapshot = null;
      }

      this.dirty |= var1;
      this.forceDirty = (boolean)0;
      return var1 != 0 ? this.save() : !this.dirty;
   }

   public boolean save() {
      if (!this.dirty) {
         return true;
      } else {
         this.saveCount++;
         if (this.saveAction.getAsBoolean()) {
            this.dirty = (boolean)0;
            failedSaveSession = -1L;
            this.statusText = "Saved";
            LarpLog.debug("HUD editor", "Saved completed workspace edit");
            return true;
         } else {
            failedSaveSession = this.sessionId;
            this.statusText = "Save failed. Retry";
            LarpLog.warn("HUD editor", "Workspace save failed; pending edits retained for retry");
            return false;
         }
      }
   }

   public boolean undo() {
      return this.restoreFrom(this.undoStack, this.redoStack);
   }

   public boolean redo() {
      return this.restoreFrom(this.redoStack, this.undoStack);
   }

   private boolean restoreFrom(ArrayDeque<HudEditorSession.EditSnapshot> var1, ArrayDeque<HudEditorSession.EditSnapshot> var2) {
      this.dragController.mouseReleased(0);
      this.commit();
      if (var1.isEmpty()) {
         return false;
      }

      commit(var2, HudEditorSession.EditSnapshot.capture(this.modules));
      ((HudEditorSession.EditSnapshot)var1.removeLast()).restore();
      this.dirty = (boolean)1;
      this.save();
      return true;
   }

   private static void commit(ArrayDeque<HudEditorSession.EditSnapshot> var0, HudEditorSession.EditSnapshot var1) {
      if (var0.size() == 64) {
         var0.removeFirst();
      }

      var0.addLast(var1);
   }

   public void moveSelected(int var1, int var2, int var3, int var4) {
      HudLayout.HudElement var5 = this.getSelected();
      if (var5 != null && !this.lockedFilter.test(var5)) {
         this.runEdit(
            () -> var5.setPosition(
               HudLayout2.clampPosition(var5.getX(var3) + var1, var5.scaledWidth(), var3),
               HudLayout2.clampPosition(var5.getY(var4) + var2, var5.scaledHeight(), var4),
               var3,
               var4
            )
         );
      }
   }

   public void runEdit() {
      this.runEdit(() -> HudLayout.resetPlacements(var1 -> !this.lockedFilter.test(var1)));
   }

   public void undoCount(boolean var1, boolean var2) {
      UiState.HudEditorState var3 = UiState.hudEditor;
      if (var3.snapping() != var1 || var3.grid() != var2) {
         UiState.hudEditor = new UiState.HudEditorState(var3.panel(), var3.collapsed(), var1, var2);
         this.dirty = (boolean)1;
         this.save();
      }
   }

   public boolean endSession() {
      if (this.sessionId == UiState.revision) {
         return false;
      }

      this.dragController.updateChatState(true, true);
      this.undoStack.clear();
      this.redoStack.clear();
      this.pendingSnapshot = null;
      this.dirty = this.forceDirty = (boolean)0;
      this.statusText = "Saved";
      this.sessionId = UiState.revision;
      return true;
   }

   public void settleDrag() {
      this.dragController.mouseReleased(0);
      this.commit();
   }

   public void finishInteraction() {
      long var1 = this.saveCount;
      this.dragController.close();
      this.commit();
      if (this.saveCount == var1) {
         this.save();
      }
   }

   public record EditSnapshot(
      Map<String, UiState.HudPosition> positions, Map<String, UiState.HudStyle> styles, Map<Setting<?>, Object> settings, Map<Module, Boolean> owners
   ) {
      public EditSnapshot(
         Map<String, UiState.HudPosition> positions, Map<String, UiState.HudStyle> styles, Map<Setting<?>, Object> settings, Map<Module, Boolean> owners
      ) {
         positions = Map.copyOf(positions);
         styles = Map.copyOf(styles);
         settings = Map.copyOf(settings);
         owners = Map.copyOf(owners);
         this.positions = positions;
         this.styles = styles;
         this.settings = settings;
         this.owners = owners;
      }

      public static HudEditorSession.EditSnapshot capture() {
         return capture(List.of());
      }

      public static HudEditorSession.EditSnapshot capture(List<Module> var0) {
         LinkedHashMap var1 = new LinkedHashMap();
         LinkedHashMap var2 = new LinkedHashMap();

         for (Module var4 : var0) {
            var2.put(var4, var4.isEnabled());

            for (Setting var6 : var4.getSettings()) {
               var1.put(var6, var6.getValue());
            }
         }

         return new HudEditorSession.EditSnapshot(UiState.hudPositions, UiState.hudStyles, var1, var2);
      }

      private static void restoreSetting(Setting<?> var0, Object var1) {
         if (!Objects.equals(var0.getValue(), var1)) {
            var0.setValue(var1);
         }
      }

      public void restore() {
         UiState.hudPositions.clear();
         UiState.hudPositions.putAll(this.positions);
         UiState.hudStyles.clear();
         UiState.hudStyles.putAll(this.styles);
         this.settings.forEach(HudEditorSession.EditSnapshot::restoreSetting);
         this.owners.forEach((var0, var1) -> {
            if (var0.isEnabled() != var1) {
               var0.setEnabled(var1);
            }
         });
      }
   }

   public record EditorLayout(
      HudLayout2.Rect dock, HudLayout2.Rect panel, HudLayout2.Rect body, HudLayout2.Rect retry, List<HudLayout2.Rect> tools, int width, int height
   ) {
      public EditorLayout(
         HudLayout2.Rect dock, HudLayout2.Rect panel, HudLayout2.Rect body, HudLayout2.Rect retry, List<HudLayout2.Rect> tools, int width, int height
      ) {
         tools = List.copyOf(tools);
         this.dock = dock;
         this.panel = panel;
         this.body = body;
         this.retry = retry;
         this.tools = tools;
         this.width = width;
         this.height = height;
      }

      public static HudEditorSession.EditorLayout calculate(
         int var0, int var1, HudEditorSession.PanelMode var2, int var3, int var4, int var5, boolean var6, boolean var7
      ) {
         if (!var6) {
            return new HudEditorSession.EditorLayout(null, null, null, null, List.of(), var0, var1);
         }

         int var8 = Math.min(12, Math.max(0, var0 / 4));
         int var9 = Math.max(1, Math.min(340, var0 - var8 * 2));
         HudLayout2.Rect var10 = new HudLayout2.Rect((var0 - var9) / 2, Math.max(0, var1 - var8 - 42), var9, Math.min(42, var1));
         ArrayList var11 = new ArrayList();
         int var12 = Math.max(2, var9 - 172);
         int[] var13 = new int[]{var12 / 2 + 4, 28, 28, 28, var12 - var12 / 2 - 4, 56};
         double var14 = Math.min(1.0, (double)Math.max(1, var9 - 32) / Arrays.stream(var13).sum());
         int var16 = var10.left() + 6;

         for (int var20 : var13) {
            int var21 = Math.max(1, (int)Math.floor(var20 * var14));
            var11.add(new HudLayout2.Rect(var16, var10.top() + 6, var21, Math.min(30, var10.height())));
            var16 += var21 + 4;
         }

         HudLayout2.Rect var26 = var7 ? new HudLayout2.Rect((var0 - Math.min(208, var9)) / 2, Math.max(0, var10.top() - 29), Math.min(208, var9), 22) : null;
         if (var2 == HudEditorSession.PanelMode.NONE) {
            return new HudEditorSession.EditorLayout(var10, null, null, var26, var11, var0, var1);
         }

         int var27 = Math.max(1, Math.min(var2 == HudEditorSession.PanelMode.WIDGETS ? 268 : 260, var0 - var8 * 2));
         int var28 = header(var2);
         int var29 = Math.max(1, var10.top() - (var7 ? 40 : 12) - var8);
         int var30 = Math.min(var28 + Math.min(Math.max(0, var3), 280) + 10, var29);
         int var22 = Math.clamp(var4 < 0 ? var10.left() : var4, var8, Math.max(var8, var0 - var8 - var27));
         int var23 = Math.clamp(var5 < 0 ? var10.top() - var30 - 12 : var5, var8, Math.max(var8, var10.top() - (var7 ? 40 : 12) - var30));
         HudLayout2.Rect var24 = new HudLayout2.Rect(var22, var23, var27, var30);
         HudLayout2.Rect var25 = new HudLayout2.Rect(var22 + 10, var23 + Math.min(var28, var30), Math.max(1, var27 - 20), Math.max(0, var30 - var28 - 10));
         return new HudEditorSession.EditorLayout(var10, var24, var25, var26, var11, var0, var1);
      }

      public static int header(HudEditorSession.PanelMode var0) {
         return var0 == HudEditorSession.PanelMode.WIDGETS
            ? 78
            : (var0 != HudEditorSession.PanelMode.APPEARANCE && var0 != HudEditorSession.PanelMode.POSITION ? 45 : 76);
      }

      public boolean panelContains(double var1, double var3) {
         return this.panel != null && this.panel.contains(var1, var3, 0);
      }

      public boolean canvasContains(double var1, double var3) {
         return var1 >= 0.0
            && var1 < this.width
            && var3 >= 0.0
            && var3 < this.height
            && !this.panelContains(var1, var3)
            && (this.dock == null || !this.dock.contains(var1, var3, 0))
            && (this.retry == null || !this.retry.contains(var1, var3, 0));
      }

      public boolean controlContains(HudLayout2.Rect var1, double var2, double var4) {
         return this.body != null && this.body.contains(var2, var4, 0) && var1.contains(var2, var4, 0);
      }
   }

   public enum PanelMode {
      NONE,
      WIDGETS,
      APPEARANCE,
      POSITION,
      OPTIONS;
   }
}
