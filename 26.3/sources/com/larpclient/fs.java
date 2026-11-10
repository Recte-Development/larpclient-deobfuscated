package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public abstract class fs extends Screen implements n1 {
   protected final List<j0> J = new ArrayList<>();
   protected j0 q;
   protected j0 g;

   protected fs(String var1) {
      super(Component.literal(var1));
   }

   protected void W() {
      for (j0 var2 : this.J) {
         var2.blur();
      }

      this.J.clear();
      this.q = this.g = null;
   }

   protected boolean accepts(j0 var1, double var2, double var4) {
      return var1.contains(var2, var4);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.q != null && this.q.captureMouse(j2.L(var1))) {
         return true;
      }

      for (j0 var4 : this.J) {
         if (this.accepts(var4, var1.x(), var1.y())) {
            if (this.q != null && this.q != var4) {
               this.q.blur();
            }

            this.q = var4;
            var4.F = (boolean)1;
            var4.X = (boolean)0;
            if (var4.click(var1.x(), var1.y(), j2.L(var1))) {
               this.g = var4;
               return true;
            }
         }
      }

      if (this.q != null) {
         this.q.blur();
      }

      this.q = null;
      return false;
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.g != null) {
         this.g.drag(var1.x(), var1.y());
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.g != null) {
         this.g.release();
      }

      this.g = null;
      return true;
   }

   public boolean keyPressed(KeyEvent var1) {
      if (j2.P(var1) != 258) {
         return this.q != null && this.q.key(j2.P(var1)) ? true : super.keyPressed(var1);
      }

      int var2 = (var1.modifiers() & 1) != 0 ? -1 : 1;
      int var3 = this.q == null ? (var2 > 0 ? -1 : 0) : this.J.indexOf(this.q);
      if (this.q != null) {
         this.q.blur();
      }

      for (int var4 = 0; var4 < this.J.size(); var4++) {
         var3 = Math.floorMod(var3 + var2, this.J.size());
         j0 var5 = this.J.get(var3);
         if (var5.V && var5.R) {
            this.q = var5;
            var5.F = (boolean)1;
            var5.X = (boolean)1;
            this.focusChanged(var5);
            break;
         }
      }

      return true;
   }

   protected void focusChanged(j0 var1) {
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.q != null && this.q.character(var1.codepoint());
   }

   public boolean isPauseScreen() {
      return false;
   }
}
