package com.larpclient.gui.hud;

import com.larpclient.gui.misc.GuiSounds;
import com.larpclient.gui.theme.Icons;
import com.larpclient.gui.widget.MenuButton;
import com.larpclient.gui.widget.SettingRowWidget;
import com.larpclient.gui.widget.Widget;
import com.larpclient.hud.HudLayout;
import com.larpclient.hud.HudLayout2;
import com.larpclient.hud.HudPanels;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.module.misc.Setting;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.SdfText;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.DrawUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class HudEditorPopup {
   public static final float sCALE = 0.86F;
   private final List<Widget> rows = new ArrayList<>();
   private final Runnable onChange;
   private final Runnable onSave;
   private HudLayout2.Rect bounds;
   private HudLayout.HudElement selectedElement;
   private Widget activeRow;
   private boolean dragging;
   private double dragOffsetX;
   private double dragOffsetY;
   private int scrollOffset;
   private final Animation openAnimation = new Animation(0.0);

   public HudEditorPopup(Runnable var1, Runnable var2) {
      this.onChange = var1;
      this.onSave = var2;
   }

   public boolean isOpen() {
      return this.bounds != null;
   }

   public HudLayout2.Rect getScaledBounds() {
      return this.bounds == null
         ? null
         : new HudLayout2.Rect(this.bounds.left(), this.bounds.top(), (int)Math.ceil(this.bounds.width() * 0.86F), (int)Math.ceil(this.bounds.height() * 0.86F));
   }

   private double toLocalX(double var1) {
      return this.bounds.left() + (var1 - this.bounds.left()) / 0.86F;
   }

   private double toLocalY(double var1) {
      return this.bounds.top() + (var1 - this.bounds.top()) / 0.86F;
   }

   public void reset() {
      this.rows.clear();
      this.bounds = null;
      this.activeRow = null;
      this.dragging = (boolean)0;
      this.scrollOffset = 0;
   }

   public void close() {
      if (this.isOpen()) {
         for (Widget var2 : this.rows) {
            var2.blur();
         }

         this.rows.clear();
         this.bounds = null;
         this.activeRow = null;
         this.dragging = (boolean)0;
         this.scrollOffset = 0;
         this.onSave.run();
      }
   }

   public void openElementList(int var1, int var2, int var3, int var4) {
      this.close();
      this.selectedElement = null;

      for (HudLayout.HudElement var6 : HudLayout.addBounds()) {
         MenuButton var7 = new MenuButton(HudLayout.getDisplayName(var6.id), () -> {
            int var2x = !HudLayout.isEnabled(var6) ? 1 : 0;
            HudLayout.setElementEnabled(var6, (boolean)var2x);
            this.onChange.run();
            this.onSave.run();
            GuiSounds.play(var2x != 0 ? GuiSounds.Sound.SWITCH_ON : GuiSounds.Sound.SWITCH_OFF);
         });
         var7.clickSound = null;
         var7.height = 22;
         this.rows.add(var7);
      }

      this.openPanel(var1, var2, 220, var3, var4);
   }

   public void openElementSettings(HudLayout.HudElement var1, int var2, int var3, int var4, int var5) {
      this.close();
      this.selectedElement = var1;
      NumberSetting var6 = new NumberSetting("Widget Size", "Scale this HUD widget", var1.scale(), 0.2, 4.0, 0.02);
      var6.onChange(var2x -> {
         var1.setScale(var2x);
         this.onChange.run();
      });
      this.rows.add(new SettingRowWidget(var6, true));

      for (Setting var8 : HudLayout.getSettings(var1)) {
         this.rows.add(new SettingRowWidget(var8, true));
      }

      MenuButton var9 = new MenuButton("Hide widget", () -> {
         var1.setHidden(true);
         this.onChange.run();
         this.close();
      });
      MenuButton var10 = new MenuButton("Reset placement", () -> {
         var1.resetPlacement();
         this.onChange.run();
         this.onSave.run();
      });
      this.rows.add(var9);
      this.rows.add(var10);
      this.openPanel(var2, var3, 252, var4, var5);
      GuiSounds.play(GuiSounds.Sound.SETTINGS_OPEN);
   }

   private void openPanel(int var1, int var2, int var3, int var4, int var5) {
      var3 = Math.max(1, Math.min(var3, (int)((var4 - 10) / 0.86F)));
      int var6 = Math.max(1, Math.min((int)((var5 - 10) / 0.86F), 46 + this.rows.stream().mapToInt(var0 -> var0.height + 2).sum()));
      this.bounds = new HudLayout2.Rect(
         HudLayout2.clampPosition(var1, (int)Math.ceil(var3 * 0.86F), var4), HudLayout2.clampPosition(var2, (int)Math.ceil(var6 * 0.86F), var5), var3, var6
      );
      this.scrollOffset = 0;
      this.openAnimation.snapTo(0.0);
      this.openAnimation.animateTo(1.0, 300);
      this.openElementList();
   }

   private void openElementList() {
      int var1 = this.bounds.top() + 39 - this.scrollOffset;

      for (Widget var3 : this.rows) {
         var3.bounds(this.bounds.left() + 10, var1, Math.max(1, this.bounds.width() - 20));
         if (var3 instanceof MenuButton && this.selectedElement == null) {
            var3.height = 22;
         }

         if (var3 instanceof SettingRowWidget var4) {
            var4.layoutPopup(this.bounds.top() + 39, this.bounds.top() + this.bounds.height() - 7);
         }

         var1 += var3.height + 2;
      }
   }

   private boolean close(double var1, double var3) {
      return this.bounds.contains(var1, var3, 0) && var3 >= this.bounds.top() + 39 && var3 < this.bounds.top() + this.bounds.height() - 7;
   }

   public boolean mouseClicked(double var1, double var3, int var5, int var6, int var7) {
      if (!this.isOpen()) {
         return false;
      }

      this.openElementList();
      double var8 = this.toLocalX(var1);
      double var10 = this.toLocalY(var3);
      if (!this.bounds.contains(var8, var10, 0)) {
         this.close();
         return false;
      }

      if (var10 < this.bounds.top() + 34) {
         if (var8 >= this.bounds.left() + this.bounds.width() - 24) {
            GuiSounds.play(GuiSounds.Sound.SETTINGS_CLOSE);
            this.close();
         } else if (var5 == 0) {
            this.dragging = (boolean)1;
            this.dragOffsetX = var1 - this.bounds.left();
            this.dragOffsetY = var3 - this.bounds.top();
         }

         return true;
      } else {
         for (Widget var13 : this.rows) {
            if (var13 instanceof SettingRowWidget var14 && var14.isPopupOpen()) {
               if (var14.containsPopupPoint(var8, var10)) {
                  var14.clickPopup(var8, var10, var5);
                  this.activeRow = var13;
                  this.onChange.run();
                  return true;
               }

               var14.closePopup();
            }
         }

         if (!this.close(var8, var10)) {
            return true;
         }

         for (int var16 = 0; var16 < this.rows.size(); var16++) {
            Widget var17 = this.rows.get(var16);
            if (var17.y >= this.bounds.top() + 39 && var17.y + var17.height <= this.bounds.top() + this.bounds.height() - 7 && var17.contains(var8, var10)) {
               if (this.selectedElement == null && var5 == 1) {
                  this.openElementSettings(HudLayout.addBounds().get(var16), (int)var1, (int)var3, var6, var7);
                  return true;
               }

               for (Widget var15 : this.rows) {
                  if (var15 != var17) {
                     var15.blur();
                  }
               }

               if (var17.click(var8, var10, var5)) {
                  this.activeRow = var17;
                  this.onChange.run();
               }

               return true;
            }
         }

         return true;
      }
   }

   public boolean mouseDragged(double var1, double var3, int var5, int var6) {
      if (!this.isOpen()) {
         return false;
      } else if (this.dragging) {
         HudLayout2.Rect var7 = this.getScaledBounds();
         this.bounds = new HudLayout2.Rect(
            HudLayout2.clampPosition((int)Math.round(var1 - this.dragOffsetX), var7.width(), var5),
            HudLayout2.clampPosition((int)Math.round(var3 - this.dragOffsetY), var7.height(), var6),
            this.bounds.width(),
            this.bounds.height()
         );
         this.openElementList();
         return true;
      } else if (this.activeRow != null) {
         this.activeRow.drag(this.toLocalX(var1), this.toLocalY(var3));
         this.onChange.run();
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased() {
      int var1 = !this.dragging && this.activeRow == null ? 0 : 1;
      this.dragging = (boolean)0;
      if (this.activeRow != null) {
         this.activeRow.release();
         this.activeRow = null;
         this.onSave.run();
      }

      return (boolean)var1;
   }

   public boolean keyPressed(int var1) {
      for (Widget var3 : this.rows) {
         if (var3 instanceof SettingRowWidget var4 && var4.getShortcutKey()) {
            boolean var5 = var4.key(var1);
            this.onChange.run();
            return var5;
         }
      }

      return false;
   }

   public boolean charTyped(int var1) {
      for (Widget var3 : this.rows) {
         if (var3 instanceof SettingRowWidget var4 && var4.getShortcutKey()) {
            var4.character(var1);
            return true;
         }
      }

      return false;
   }

   public boolean mouseScrolled(double var1, double var3, double var5) {
      if (!this.isOpen()) {
         return false;
      }

      double var7 = this.toLocalX(var1);
      double var9 = this.toLocalY(var3);
      if (!this.bounds.contains(var7, var9, 0)) {
         return false;
      }

      for (Widget var12 : this.rows) {
         if (var12 instanceof SettingRowWidget var13 && var13.containsPopupPoint(var7, var9)) {
            var13.shortcutBadgeWidth(var5);
            return true;
         }
      }

      int var14 = this.rows.stream().mapToInt(var0 -> var0.height + 2).sum();
      this.scrollOffset = Math.clamp(this.scrollOffset - (int)Math.round(var5 * 22.0), 0, Math.max(0, var14 - this.bounds.height() + 46));
      this.openElementList();
      return true;
   }

   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      if (this.isOpen()) {
         this.openElementList();
         int var4 = (int)Math.floor(this.toLocalX(var2));
         int var5 = (int)Math.floor(this.toLocalY(var3));
         var1.pose().pushMatrix();
         var1.pose().translate(this.bounds.left(), this.bounds.top()).scale(0.86F).translate(-this.bounds.left(), -this.bounds.top());

         try (AlphaScope var6 = AlphaScope.push(this.openAnimation.getValue())) {
            DrawUtil.fillRoundedRect(var1, this.bounds.left() - 3, this.bounds.top() - 3, this.bounds.width() + 6, this.bounds.height() + 6, 13.0F, 1593835520);
            DrawUtil.fillRoundedRect(var1, this.bounds.left(), this.bounds.top(), this.bounds.width(), this.bounds.height(), 10.0F, -183957239);
            HudPanels.drawText(
               var1,
               this.selectedElement == null ? "HUD Elements" : HudLayout.getDisplayName(this.selectedElement.id),
               this.bounds.left() + 12,
               SdfText.centerBaselineY(this.bounds.top(), 32.0, 13.0),
               13.0,
               -1250068
            );
            Icons.texture(var1, Icons.get("x"), this.bounds.left() + this.bounds.width() - 20, this.bounds.top() + 12, 12, -5592406);
            DrawUtil.fillRect(var1, this.bounds.left() + 2, this.bounds.top() + 32, this.bounds.width() - 4, 1, 675036230);
            var1.enableScissor(
               this.bounds.left(), this.bounds.top() + 39, this.bounds.left() + this.bounds.width(), this.bounds.top() + this.bounds.height() - 7
            );

            try {
               for (int var7 = 0; var7 < this.rows.size(); var7++) {
                  Widget var8 = this.rows.get(var7);
                  if (var8.y + var8.height >= this.bounds.top() + 39 && var8.y < this.bounds.top() + this.bounds.height() - 7) {
                     if (this.selectedElement == null && var8 instanceof MenuButton var9) {
                        var9.flat = (boolean)1;
                        var9.selected = HudLayout.isEnabled(HudLayout.addBounds().get(var7));
                        var9.toggleState = var9.selected;
                     }

                     var8.render(var1, this.close(var4, var5) ? var4 : -1, var5);
                  }
               }
            } finally {
               var1.disableScissor();
            }

            for (Widget var25 : this.rows) {
               if (var25 instanceof SettingRowWidget var26 && var26.isPopupOpen()) {
                  var26.renderPopup(var1, var4, var5);
               }
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }
}
