package com.larpclient.gui.widget;

import com.larpclient.gui.clickgui.ClickGuiScreen;
import com.larpclient.gui.misc.FlowLayout;
import com.larpclient.gui.misc.GuiLayout;
import com.larpclient.gui.screen.WidgetScreen;
import com.larpclient.gui.theme.Theme;
import com.larpclient.render.overlay.AlphaScope;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.render.overlay.GlassPainter;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.util.misc.KeyCodes;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import com.larpclient.util.render.DrawUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;

abstract class TabPanel extends WidgetScreen {
   protected ClickGuiScreen gui;
   protected GuiLayout frame;
   protected final List<Widget> widgets = new ArrayList<>();
   protected final List<MenuButton> tabButtons = new ArrayList<>();
   protected int scrollOffset;
   protected int contentLeft;
   protected int contentTop;
   protected int contentRight;
   protected int contentBottom;
   protected int contentHeight;
   protected boolean dialogOpen;
   protected String title;
   protected String dialogTitle = "";
   protected String dialogBody = "";
   private final List<MenuButton> dialogButtons = new ArrayList<>();
   private final List<MenuButton> optionButtons = new ArrayList<>();
   private final MenuButton closeButton = new MenuButton("", this::dismissDialog);
   private List<String> titleLines = List.of();
   private List<String> bodyLines = List.of();
   private int dialogX;
   private int dialogY;
   private int dialogWidth;
   private int dialogHeight;
   private final Animation revealAnimation = new Animation(1.0);
   private final Animation scrollAnimation = new Animation(0.0);
   private final Animation indicatorXAnimation = new Animation(0.0);
   private final Animation indicatorWidthAnimation = new Animation(0.0);
   private boolean scrollInitialized;
   private boolean indicatorInitialized;
   private boolean wasDialogOpen;
   private static final int mAX_DIALOG_WIDTH = 300;
   private static final int dIALOG_PADDING = 14;
   private static final int bUTTON_HEIGHT = 20;

   protected final void resetReveal() {
      this.revealAnimation.snapTo(0.0);
      this.revealAnimation.animateTo(1.0, 200);
   }

   protected double revealAmount() {
      return this.revealAnimation.animateTo(1.0, 200);
   }

   TabPanel(String var1) {
      super(var1);
      this.title = var1;
      this.closeButton.flat = (boolean)1;
      this.closeButton.icon = "x";
      this.closeButton.height = 18;
      this.closeButton.visible = (boolean)0;
   }

   final void attach(ClickGuiScreen var1, GuiLayout var2) {
      this.gui = var1;
      this.frame = var2;
      this.init(var1.width, var1.height);
      this.resetReveal();
   }

   void attach(GuiLayout var1) {
      if (this.frame == null || this.frame.width() != var1.width() || this.frame.height() != var1.height()) {
         this.scrollInitialized = (boolean)0;
      }

      this.frame = var1;
      this.layout();
   }

   protected abstract void build();

   protected void init() {
      this.build();
   }

   @Override
   protected void clearWidgets() {
      this.clearWidgets();
      this.widgets.clear();
      this.tabButtons.clear();
      this.dialogButtons.clear();
      this.optionButtons.clear();
      this.dialogTitle = this.dialogBody = "";
      this.addWidget(this.closeButton);
   }

   protected void dismissDialog() {
   }

   protected MenuButton addDialogButton(String var1, Runnable var2) {
      MenuButton var3 = new MenuButton(var1, var2);
      var3.pill = (boolean)1;
      var3.height = 20;
      this.dialogButtons.add(var3);
      this.addWidget(var3);
      return var3;
   }

   protected MenuButton option(String var1, String var2, Runnable var3) {
      MenuButton var4 = this.addButton(var1, var3);
      var4.flat = (boolean)1;
      var4.accented = (boolean)0;
      var4.subtitle = var2;
      var4.height = 30;
      this.optionButtons.add(var4);
      return var4;
   }

   protected void addSeparator() {
      this.addWidget(new TabPanel.Divider());
   }

   private boolean isDialogWidget(Widget var1) {
      return var1 == this.closeButton || var1 instanceof MenuButton var2 && this.dialogButtons.contains(var2);
   }

   protected void addWidget(Widget var1) {
      this.widgets.add(var1);
      this.widgets.add(var1);
   }

   protected void text(String var1) {
      this.addWidget(new TabPanel.InfoRow(var1));
   }

   protected void addDynamicText(Supplier<String> var1) {
      this.addWidget(new TabPanel.InfoRow(var1));
   }

   protected void addColoredText(Supplier<String> var1, IntSupplier var2) {
      this.addWidget(new TabPanel.InfoRow(var1, var2));
   }

   protected void addLabeledValue(String var1, Supplier<String> var2) {
      this.addWidget(new TabPanel.InfoRow(var1, var2));
   }

   protected MenuButton addButton(String var1, Runnable var2) {
      MenuButton var3 = new MenuButton(var1, var2);
      var3.accented = (boolean)(!var1.equals("Load") && !var1.startsWith("Save") && !var1.equals("Fetch preview") ? 0 : 1);
      var3.danger = var1.startsWith("Delete");
      this.addWidget(var3);
      return var3;
   }

   protected void addWidget(String var1, boolean var2, Runnable var3) {
      MenuButton var4 = new MenuButton(var1, var3);
      var4.flat = (boolean)1;
      var4.highlightOnSelectOnly = (boolean)1;
      var4.selected = var2;
      this.tabButtons.add(var4);
      this.widgets.add(var4);
   }

   protected int bodyTop() {
      return this.frame.bodyTop() + 28;
   }

   protected void layout() {
      int var1 = this.frame.contentX();
      int var2 = Math.max(1, (this.frame.right() - var1) / Math.max(1, this.tabButtons.size()));

      for (MenuButton var4 : this.tabButtons) {
         int var5 = Math.min(var2, Math.max(62, FontRenderer.drawDefaultText(var4.label) + 20));
         var4.bounds(var1, this.frame.top() + 12, var5 - 4);
         var4.height = 24;
         var1 += var5;
      }

      this.closeButton.visible = this.dialogOpen;
      if (this.dialogOpen) {
         this.attach();
      } else {
         this.contentLeft = this.frame.contentX();
         this.contentRight = this.frame.right();
         this.contentTop = this.bodyTop();
         this.contentBottom = this.frame.bottom();
         this.layoutContent();
      }
   }

   private void attach() {
      int var1 = this.frame.bodyTop() + 8;
      int var2 = Math.max(1, this.frame.bottom() - 8 - var1);
      int var3 = this.frame.right() - this.frame.contentX();
      this.dialogWidth = Math.max(1, Math.min(300, var3 - 16));
      this.dialogX = this.frame.contentX() + (var3 - this.dialogWidth) / 2;
      this.contentLeft = this.dialogX + 14 - 8;
      this.contentRight = this.dialogX + this.dialogWidth - 14 + 12;
      int var4 = Math.max(1, this.dialogWidth - 28 - 22);
      this.titleLines = this.dialogTitle.isEmpty()
         ? List.of()
         : wrapText(this.dialogTitle, var4, var0 -> FontRenderer.measureWidth(var0, FontRenderer.TextStyle.TITLE));
      this.bodyLines = this.dialogBody.isEmpty()
         ? List.of()
         : wrapText(this.dialogBody, var4, var0 -> FontRenderer.measureWidth(var0, FontRenderer.TextStyle.HELP));
      int var5 = 14
         + Math.max(
            14,
            this.titleLines.size() * (FontRenderer.TextStyle.TITLE.lineHeight() + 3)
               + (this.bodyLines.isEmpty() ? 0 : 1 + this.bodyLines.size() * (FontRenderer.TextStyle.HELP.lineHeight() + 3))
         )
         + 8;
      this.layoutRows();
      int var6 = Math.max(0, this.contentHeight - 4);
      int var7 = this.dialogButtons.isEmpty() ? 10 : 46;
      this.dialogHeight = Math.min(var2, var5 + var6 + var7);
      this.dialogY = var1 + (var2 - this.dialogHeight) * 2 / 5 + (int)Math.round((1.0 - this.revealAmount()) * 6.0);
      this.contentTop = this.dialogY + var5;
      this.contentBottom = Math.max(this.contentTop, this.dialogY + this.dialogHeight - var7);
      this.applyScroll(0);
      this.closeButton.bounds(this.dialogX + this.dialogWidth - 14 - 18 + 4, this.dialogY + 14 - 4, 18);
      int var8 = this.dialogY + this.dialogHeight - 14 - 20;
      List var9 = this.dialogButtons.stream().map(var0 -> Math.max(64, FontRenderer.measureWidth(var0.label, FontRenderer.TextStyle.HELP) + 24)).toList();
      int var10 = Math.max(0, this.dialogButtons.size() - 1) * 6;
      int var11 = var9.stream().mapToInt(Integer::intValue).sum();
      double var12 = Math.min(1.0, (double)Math.max(1, this.dialogWidth - 28 - var10) / Math.max(1, var11));
      int var14 = this.dialogX + this.dialogWidth - 14;

      for (int var15 = this.dialogButtons.size() - 1; var15 >= 0; var15--) {
         int var16 = Math.max(1, (int)(((Integer)var9.get(var15)).intValue() * var12));
         var14 -= var16;
         this.dialogButtons.get(var15).bounds(var14, var8, var16);
         var14 -= 6;
      }
   }

   protected final void layoutContent() {
      if (!this.layoutCustomContent()) {
         this.layoutRows();
         this.applyScroll((int)Math.round((1.0 - this.revealAmount()) * 6.0));
      }
   }

   private void layoutRows() {
      this.contentHeight = 0;

      for (Widget var2 : this.widgets) {
         if (!this.isDialogWidget(var2)) {
            var2.visible = this.componentShown(var2);
            if (!var2.visible) {
               var2.blur();
            } else {
               var2.bounds(this.contentLeft + 8, 0, Math.max(1, this.contentRight - this.contentLeft - 20));
               if (this.dialogOpen && var2 instanceof MenuButton var3 && this.optionButtons.contains(var3)) {
                  var2.x -= 8;
                  var2.width += 16;
               }

               if (var2 instanceof TabPanel.InfoRow var11) {
                  if (this.dialogOpen) {
                     var11.x -= 4;
                  }

                  var11.rewrap();
               }
            }
         }
      }

      int var9 = 0;

      while (var9 < this.widgets.size()) {
         Widget var10 = this.widgets.get(var9);
         if (!var10.visible || this.isDialogWidget(var10)) {
            var9++;
         } else if (var10 instanceof SettingRowWidget var12 && var12.isChip()) {
            ArrayList var13 = new ArrayList();

            while (var9 < this.widgets.size()) {
               if (!(this.widgets.get(var9) instanceof SettingRowWidget var15) || !var15.isChip()) {
                  break;
               }

               if (var15.visible) {
                  var13.add(var15);
               }

               var9++;
            }

            FlowLayout.Layout var16 = FlowLayout.layout(
               Math.max(1, this.contentRight - this.contentLeft - 20), var13.stream().map(SettingRowWidget::preferredWidth).toList()
            );

            for (int var19 = 0; var19 < var13.size(); var19++) {
               FlowLayout.Cell var20 = var16.cells().get(var19);
               ((SettingRowWidget)var13.get(var19)).bounds(this.contentLeft + 8 + var20.x2(), this.contentHeight + var20.y2(), var20.width());
            }

            this.contentHeight = this.contentHeight + var16.height() + 4;
         } else if (var10 instanceof MenuButton var4 && var4.tag != null) {
            ArrayList var5 = new ArrayList();

            while (var9 < this.widgets.size()) {
               if (!(this.widgets.get(var9) instanceof MenuButton var6) || var6.tag != var4.tag) {
                  break;
               }

               if (var6.visible) {
                  var5.add(var6);
               }

               var9++;
            }

            FlowLayout.Layout var14 = FlowLayout.layoutJustified(
               Math.max(1, this.contentRight - this.contentLeft - 20),
               var5.stream().map(var0 -> FontRenderer.measureWidth(var0.label, FontRenderer.TextStyle.BODY) + 18).toList()
            );

            for (int var17 = 0; var17 < var5.size(); var17++) {
               FlowLayout.Cell var8 = var14.cells().get(var17);
               ((MenuButton)var5.get(var17)).bounds(this.contentLeft + 8 + var8.x2(), this.contentHeight + var8.y2(), var8.width());
               ((MenuButton)var5.get(var17)).height = 24;
            }

            this.contentHeight = this.contentHeight + var14.height() + 4;
         } else {
            var10.y = this.contentHeight;
            this.contentHeight = this.contentHeight + var10.height + 4;
            var9++;
         }
      }
   }

   private void applyScroll(int var1) {
      int var2 = this.contentHeight - (this.dialogOpen ? 4 : 0) - (this.contentBottom - this.contentTop);
      this.scrollOffset = Math.clamp(this.scrollOffset, 0, Math.max(0, var2));
      if (!this.scrollInitialized) {
         this.scrollAnimation.snapTo(this.scrollOffset);
         this.scrollInitialized = (boolean)1;
      }

      int var3 = (int)Math.round(this.scrollAnimation.animateTo(this.scrollOffset, 150));
      int var4 = this.contentTop - var3 + var1;

      for (Widget var6 : this.widgets) {
         if (var6.visible && !this.isDialogWidget(var6)) {
            var6.y += var4;
            if (var6 instanceof SettingRowWidget var7) {
               var7.layoutPopup(this.frame.top() + 12, this.contentBottom);
            }
         }
      }
   }

   protected boolean componentShown(Widget var1) {
      return var1 instanceof SettingRowWidget var3 ? var3.isActive() : !(var1 instanceof SectionLabel var2 && !var2.isVisible());
   }

   protected boolean layoutCustomContent() {
      return false;
   }

   protected boolean contentShown() {
      return true;
   }

   protected boolean showTitle() {
      return true;
   }

   protected int titleX() {
      return this.frame.contentX() + 8;
   }

   protected int titleY() {
      return this.frame.bodyTop() + 8;
   }

   protected void renderExtra(GuiGraphicsExtractor var1, int var2, int var3) {
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!this.horizontalInput(var2)) {
         var2 = -1;
      }

      if (this.dialogOpen != this.wasDialogOpen) {
         this.resetReveal();
         this.wasDialogOpen = this.dialogOpen;
      }

      this.layout();

      for (MenuButton var6 : this.tabButtons) {
         if (var6.selected) {
            if (!this.indicatorInitialized) {
               this.indicatorXAnimation.snapTo(var6.x);
               this.indicatorWidthAnimation.snapTo(var6.width);
               this.indicatorInitialized = (boolean)1;
            }

            int var7 = (int)Math.round(this.indicatorXAnimation.animateTo(var6.x, 200));
            int var8 = (int)Math.round(this.indicatorWidthAnimation.animateTo(var6.width, 200));
            int var9 = Theme.isThemed() ? Theme.readableAccent(var7, Theme.background) : ColorUtil.withAlpha(Theme.textPrimary, 0.75);
            GlassPainter.fillRoundedRect(var1, var7 + 7, var6.y + var6.height, Math.max(1, var8 - 14), 2.0F, 1.0F, 4.0F, ColorUtil.withAlpha(var9, 0.45));
            GlassPainter.fillRoundedRect(var1, var7 + 7, var6.y + var6.height, Math.max(1, var8 - 14), 2, 1, var9);
         }
      }

      for (MenuButton var24 : this.tabButtons) {
         var24.render(var1, this.dialogOpen ? -1 : var2, var3);
      }

      if (this.showTitle()) {
         FontRenderer.drawText(
            var1,
            FontRenderer.trimToWidth(this.title, Math.max(1, this.contentRight - this.titleX() - 32), FontRenderer.TextStyle.TITLE),
            this.titleX(),
            this.titleY(),
            Theme.textPrimary,
            FontRenderer.TextStyle.TITLE
         );
      }

      this.renderExtra(var1, this.dialogOpen ? -1 : var2, var3);
      if (this.dialogOpen) {
         this.renderDialog(var1, var2, var3);
      }

      var1.enableScissor(this.contentLeft, this.contentTop, this.contentRight, this.contentBottom);

      try (AlphaScope var22 = AlphaScope.push(this.revealAmount())) {
         for (Widget var27 : this.widgets) {
            if (this.contentShown() && var27.visible && !this.isDialogWidget(var27) && var27.y + var27.height > this.contentTop && var27.y < this.contentBottom
               )
             {
               var27.render(var1, var3 >= this.contentTop && var3 < this.contentBottom ? var2 : -1, var3);
            }
         }
      } finally {
         var1.disableScissor();
      }

      int var23 = this.contentBottom - this.contentTop;
      if (this.contentShown()) {
         GlassPainter.drawScrollbar(
            var1,
            this.contentRight - 3,
            this.contentTop,
            Math.max(1, var23),
            this.contentHeight - (this.dialogOpen ? 4 : 0),
            (int)Math.round(this.scrollAnimation.getValue())
         );
      }

      if (this.dialogOpen) {
         try (AlphaScope var26 = AlphaScope.push(this.revealAmount())) {
            if (this.contentHeight - 4 > var23) {
               int var28 = ColorUtil.multiplyAlpha(Theme.accentGlow, 1.2);
               if (this.scrollOffset > 0) {
                  GlassPainter.fillRoundedRect(var1, this.dialogX + 14, this.contentTop - 1, this.dialogWidth - 28, 1, 0, var28);
               }

               GlassPainter.fillRoundedRect(var1, this.dialogX + 14, this.contentBottom, this.dialogWidth - 28, 1, 0, var28);
            }

            this.closeButton.render(var1, var2, var3);

            for (MenuButton var30 : this.dialogButtons) {
               var30.render(var1, var2, var3);
            }
         }
      }

      if (this.renderPopupInline()) {
         this.renderPopup(var1, var2, var3);
      }
   }

   private void renderDialog(GuiGraphicsExtractor var1, int var2, int var3) {
      double var4 = this.revealAmount();
      GlassPainter.fillRoundedRect(
         var1,
         this.frame.contentX(),
         this.frame.bodyTop(),
         this.frame.right() - this.frame.contentX(),
         this.frame.bottom() - this.frame.bodyTop(),
         Theme.radiusControl,
         ColorUtil.multiplyAlpha(-1728053248, var4)
      );
      RoundedShapes.drawGlow(
         var1,
         this.dialogX,
         this.dialogY + 3,
         this.dialogWidth,
         this.dialogHeight,
         Theme.radiusControl,
         16.0F,
         ColorUtil.multiplyAlpha(805306368, var4),
         ColorUtil.multiplyAlpha(1342177280, var4)
      );
      GlassPainter.drawFocusedField(
         var1, this.dialogX, this.dialogY, this.dialogWidth, this.dialogHeight, Theme.radiusControl, ColorUtil.multiplyAlpha(Theme.headerBackground, var4), 0.0
      );

      try (AlphaScope var6 = AlphaScope.push(var4)) {
         int var7 = this.dialogY + 14;

         for (String var9 : this.titleLines) {
            FontRenderer.drawText(var1, var9, this.dialogX + 14, var7, Theme.textPrimary, FontRenderer.TextStyle.TITLE);
            var7 += FontRenderer.TextStyle.TITLE.lineHeight() + 3;
         }

         var7++;

         for (String var14 : this.bodyLines) {
            FontRenderer.drawText(var1, var14, this.dialogX + 14, var7, Theme.textSecondary, FontRenderer.TextStyle.HELP);
            var7 += FontRenderer.TextStyle.HELP.lineHeight() + 3;
         }
      }
   }

   private boolean isOutsideDialog(double var1, double var3) {
      return this.dialogOpen
         && !DrawUtil.isHovered(var1, var3, this.dialogX, this.dialogY, this.dialogWidth, this.dialogHeight)
         && var1 >= this.frame.contentX()
         && var1 < this.frame.right()
         && var3 >= this.frame.top()
         && var3 < this.frame.bottom();
   }

   protected boolean renderPopupInline() {
      return true;
   }

   final void renderPopup(GuiGraphicsExtractor var1, int var2, int var3) {
      SettingRowWidget var4 = this.findOpenPopup();
      if (var4 != null) {
         var4.renderPopup(var1, this.horizontalInput(var2) ? var2 : -1, var3);
      }
   }

   private SettingRowWidget findOpenPopup() {
      for (Widget var2 : this.widgets) {
         if (var2 instanceof SettingRowWidget var3 && var3.isPopupOpen()) {
            return var3;
         }
      }

      return null;
   }

   @Override
   protected boolean accepts(Widget var1, double var2, double var4) {
      if (!this.horizontalInput(var2)) {
         return false;
      } else if (!super.accepts(var1, var2, var4)) {
         return false;
      } else if (this.dialogOpen && !this.widgets.contains(var1)) {
         return false;
      } else {
         return this.isDialogWidget(var1)
            ? this.dialogOpen
            : !this.widgets.contains(var1)
               || this.contentShown() && var4 >= this.contentTop && var4 < this.contentBottom && var2 >= this.contentLeft && var2 < this.contentRight;
      }
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      this.layout();
      SettingRowWidget var3 = this.findOpenPopup();
      if (var3 == null) {
         if (KeyCodes.mouseButtonOf(var1) == 0
            && this.isOutsideDialog(var1.x(), var1.y())
            && this.horizontalInput(var1.x())
            && this.widgets.stream().noneMatch(var0 -> var0 instanceof TextField)) {
            this.dismissDialog();
            return true;
         } else {
            return super.mouseClicked(var1, var2);
         }
      } else {
         if (this.horizontalInput(var1.x()) && var3.containsPopupPoint(var1.x(), var1.y())) {
            var3.clickPopup(var1.x(), var1.y(), KeyCodes.mouseButtonOf(var1));
            this.draggedWidget = var3;
         } else {
            var3.closePopup();
         }

         return true;
      }
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      SettingRowWidget var9 = this.findOpenPopup();
      if (var9 != null) {
         if (this.horizontalInput(var1) && var9.containsPopupPoint(var1, var3)) {
            var9.shortcutBadgeWidth(var7);
         }

         return true;
      } else if (this.horizontalInput(var1)
         && this.contentShown()
         && !(var1 < this.contentLeft)
         && !(var1 >= this.contentRight)
         && !(var3 < this.contentTop)
         && !(var3 >= this.contentBottom)) {
         this.scrollOffset -= (int)(var7 * 27.0);
         this.layout();
         return true;
      } else {
         return false;
      }
   }

   @Override
   protected void focusChanged(Widget var1) {
      if (this.widgets.contains(var1)) {
         if (var1.y < this.contentTop) {
            this.scrollOffset = this.scrollOffset - (this.contentTop - var1.y);
         } else if (var1.y + var1.height > this.contentBottom) {
            this.scrollOffset = this.scrollOffset + (var1.y + var1.height - this.contentBottom);
         }

         this.scrollAnimation.snapTo(this.scrollOffset);
         this.layout();
      }
   }

   protected void addLabeledValue(Widget var1) {
      this.focusedWidget = var1;
      var1.focused = var1.focusVisible = (boolean)1;
   }

   protected boolean horizontalInput(double var1) {
      return true;
   }

   List<Widget> getFocusableWidgets() {
      return this.widgets
         .stream()
         .filter(
            var1 -> var1.visible && var1.enabled && (!this.widgets.contains(var1) || this.contentShown()) && (!this.dialogOpen || this.widgets.contains(var1))
         )
         .toList();
   }

   Widget addButton() {
      return this.focusedWidget;
   }

   void clearFocus() {
      if (this.focusedWidget != null) {
         this.focusedWidget.blur();
      }

      this.focusedWidget = this.draggedWidget = null;
   }

   void focusWidget(Widget var1) {
      this.addLabeledValue(var1);
      this.focusChanged(var1);
   }

   void suspend() {
      this.clearWidgets();
   }

   String findOpenPopup(int var1, int var2) {
      if (this.dialogOpen) {
         return "";
      } else if (this.focusedWidget instanceof SettingRowWidget var3 && this.focusedWidget.focusVisible) {
         return var3.tooltip();
      } else if (this.horizontalInput(var1) && var2 >= this.contentTop && var2 < this.contentBottom) {
         for (Widget var7 : this.widgets) {
            if (var7 instanceof SettingRowWidget var5 && var5.contains(var1, var2)) {
               return var5.tooltip();
            }
         }

         return "";
      } else {
         return "";
      }
   }

   boolean isModalOpen() {
      return this.dialogOpen || this.findOpenPopup() != null;
   }

   @Override
   public boolean keyPressed(KeyEvent var1) {
      SettingRowWidget var2 = this.findOpenPopup();
      if (KeyCodes.keyOf(var1) == 256 && var2 != null) {
         var2.closePopup();
         return true;
      }

      if (KeyCodes.keyOf(var1) == 258) {
         List var3 = this.getFocusableWidgets();
         int var4 = (var1.modifiers() & 1) != 0 ? -1 : 1;
         int var5 = this.focusedWidget == null ? (var4 > 0 ? -1 : 0) : var3.indexOf(this.focusedWidget);
         this.clearFocus();
         if (!var3.isEmpty()) {
            this.focusWidget((Widget)var3.get(Math.floorMod(var5 + var4, var3.size())));
         }

         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void onClose() {
      this.gui.onClose();
   }

   static List<String> wrapText(String var0, int var1, ToIntFunction<String> var2) {
      ArrayList var3 = new ArrayList();

      for (String var7 : var0.split("\n", -1)) {
         String var8 = var7;

         while (!var8.isEmpty() && var2.applyAsInt(var8) > var1) {
            int var9 = var8.length();

            while (var9 > Character.charCount(var8.codePointAt(0)) && var2.applyAsInt(var8.substring(0, var9)) > var1) {
               var9 = var8.offsetByCodePoints(var9, -1);
            }

            int var10 = var8.lastIndexOf(32, var9 - 1);
            if (var10 > 0) {
               var9 = var10 + 1;
            }

            var3.add(var8.substring(0, var9));
            var8 = var8.substring(var9);
         }

         var3.add(var8);
      }

      return var3;
   }

   private static final class Divider extends Widget {
      Divider() {
         this.enabled = (boolean)0;
         this.height = 5;
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         GlassPainter.fillRoundedRect(var1, this.x, this.y + 2, Math.max(1, this.width), 1, 0, ColorUtil.multiplyAlpha(Theme.accentGlow, 1.2));
      }
   }

   private static final class InfoRow extends Widget {
      private final Supplier<String> textSupplier;
      private List<String> wrappedLines = List.of();
      private int wrappedWidth = -1;
      private String wrappedText;
      private final String label;
      private final IntSupplier colorSupplier;

      InfoRow(String var1) {
         this(() -> var1);
      }

      InfoRow(Supplier<String> var1) {
         this(var1, () -> Theme.textSecondary);
      }

      InfoRow(Supplier<String> var1, IntSupplier var2) {
         this(null, var1, var2);
      }

      InfoRow(String var1, Supplier<String> var2) {
         this(var1, var2, () -> Theme.textSecondary);
      }

      private InfoRow(String var1, Supplier<String> var2, IntSupplier var3) {
         this.label = var1;
         this.textSupplier = var2;
         this.colorSupplier = var3;
         this.enabled = (boolean)0;
      }

      void rewrap() {
         if (this.label == null) {
            String var1 = this.textSupplier.get();
            if (this.wrappedWidth != this.width || !var1.equals(this.wrappedText)) {
               this.wrappedWidth = this.width;
               this.wrappedText = var1;
               this.wrappedLines = TabPanel.wrapText(var1, this.width - 8, FontRenderer::drawDefaultText);
               this.height = Math.max(22, this.wrappedLines.size() * 12 + 6);
            }
         }
      }

      @Override
      public void render(GuiGraphicsExtractor var1, int var2, int var3) {
         if (this.label != null) {
            String var6 = FontRenderer.getTextWidth(this.textSupplier.get(), Math.max(1, this.width * 2 / 3));
            int var5 = FontRenderer.drawDefaultText(var6);
            FontRenderer.drawText(var1, FontRenderer.getTextWidth(this.label, Math.max(1, this.width - var5 - 20)), this.x + 4, this.y + 7, Theme.textPrimary);
            FontRenderer.drawText(var1, var6, this.x + this.width - 4 - var5, this.y + 7, this.colorSupplier.getAsInt());
         } else {
            for (int var4 = 0; var4 < this.wrappedLines.size(); var4++) {
               FontRenderer.drawText(var1, this.wrappedLines.get(var4), this.x + 4, this.y + 4 + var4 * 12, this.colorSupplier.getAsInt());
            }
         }
      }
   }
}
