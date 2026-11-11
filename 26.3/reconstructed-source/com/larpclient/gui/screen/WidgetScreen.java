package com.larpclient.gui.screen;

import com.larpclient.gui.widget.Widget;
import com.larpclient.util.data.Marker;
import com.larpclient.util.misc.KeyCodes;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public abstract class WidgetScreen extends Screen implements Marker {
   protected final List<Widget> widgets = new ArrayList<>();
   protected Widget focusedWidget;
   protected Widget draggedWidget;

   protected WidgetScreen(String var1) {
      super(Component.literal(var1));
   }

   protected void clearWidgets() {
      for (Widget var2 : this.widgets) {
         var2.blur();
      }

      this.widgets.clear();
      this.focusedWidget = this.draggedWidget = null;
   }

   protected boolean accepts(Widget var1, double var2, double var4) {
      return var1.contains(var2, var4);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.focusedWidget != null && this.focusedWidget.captureMouse(KeyCodes.mouseButtonOf(var1))) {
         return true;
      }

      for (Widget var4 : this.widgets) {
         if (this.accepts(var4, var1.x(), var1.y())) {
            if (this.focusedWidget != null && this.focusedWidget != var4) {
               this.focusedWidget.blur();
            }

            this.focusedWidget = var4;
            var4.focused = (boolean)1;
            var4.focusVisible = (boolean)0;
            if (var4.click(var1.x(), var1.y(), KeyCodes.mouseButtonOf(var1))) {
               this.draggedWidget = var4;
               return true;
            }
         }
      }

      if (this.focusedWidget != null) {
         this.focusedWidget.blur();
      }

      this.focusedWidget = null;
      return false;
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.draggedWidget != null) {
         this.draggedWidget.drag(var1.x(), var1.y());
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.draggedWidget != null) {
         this.draggedWidget.release();
      }

      this.draggedWidget = null;
      return true;
   }

   public boolean keyPressed(KeyEvent var1) {
      if (KeyCodes.keyOf(var1) != 258) {
         return this.focusedWidget != null && this.focusedWidget.key(KeyCodes.keyOf(var1)) ? true : super.keyPressed(var1);
      }

      int var2 = (var1.modifiers() & 1) != 0 ? -1 : 1;
      int var3 = this.focusedWidget == null ? (var2 > 0 ? -1 : 0) : this.widgets.indexOf(this.focusedWidget);
      if (this.focusedWidget != null) {
         this.focusedWidget.blur();
      }

      for (int var4 = 0; var4 < this.widgets.size(); var4++) {
         var3 = Math.floorMod(var3 + var2, this.widgets.size());
         Widget var5 = this.widgets.get(var3);
         if (var5.enabled && var5.visible) {
            this.focusedWidget = var5;
            var5.focused = (boolean)1;
            var5.focusVisible = (boolean)1;
            this.focusChanged(var5);
            break;
         }
      }

      return true;
   }

   protected void focusChanged(Widget var1) {
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.focusedWidget != null && this.focusedWidget.character(var1.codepoint());
   }

   public boolean isPauseScreen() {
      return false;
   }
}
