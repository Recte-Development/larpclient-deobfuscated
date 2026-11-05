package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public abstract class pw extends Screen implements h7 {
   protected final List<oq> k = new ArrayList<>();
   protected oq W;
   protected oq L;

   protected pw(String var1) {
      super(Component.literal(var1));
   }

   protected void u() {
      for (oq var2 : this.k) {
         var2.blur();
      }

      this.k.clear();
      this.W = this.L = null;
   }

   protected boolean accepts(oq var1, double var2, double var4) {
      return var1.contains(var2, var4);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.W != null && this.W.captureMouse(r7.y(var1))) {
         return true;
      }

      for (oq var4 : this.k) {
         if (this.accepts(var4, var1.x(), var1.y())) {
            if (this.W != null && this.W != var4) {
               this.W.blur();
            }

            this.W = var4;
            var4.A = (boolean)1;
            var4.h = (boolean)0;
            if (var4.click(var1.x(), var1.y(), r7.y(var1))) {
               this.L = var4;
               return true;
            }
         }
      }

      if (this.W != null) {
         this.W.blur();
      }

      this.W = null;
      return false;
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.L != null) {
         this.L.drag(var1.x(), var1.y());
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.L != null) {
         this.L.release();
      }

      this.L = null;
      return true;
   }

   public boolean keyPressed(KeyEvent var1) {
      if (r7.g(var1) != 258) {
         return this.W != null && this.W.key(r7.g(var1)) ? true : super.keyPressed(var1);
      }

      int var2 = (var1.modifiers() & 1) != 0 ? -1 : 1;
      int var3 = this.W == null ? (var2 > 0 ? -1 : 0) : this.k.indexOf(this.W);
      if (this.W != null) {
         this.W.blur();
      }

      for (int var4 = 0; var4 < this.k.size(); var4++) {
         var3 = Math.floorMod(var3 + var2, this.k.size());
         oq var5 = this.k.get(var3);
         if (var5.x && var5.F) {
            this.W = var5;
            var5.A = (boolean)1;
            var5.h = (boolean)1;
            this.focusChanged(var5);
            break;
         }
      }

      return true;
   }

   protected void focusChanged(oq var1) {
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.W != null && this.W.character(var1.codepoint());
   }

   public boolean isPauseScreen() {
      return false;
   }
}
