package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public abstract class h8 extends Screen implements o3 {
   protected final List<b> G = new ArrayList<>();
   protected b q;
   protected b f;

   protected h8(String var1) {
      super(Component.literal(var1));
   }

   protected void z() {
      for (b var2 : this.G) {
         var2.blur();
      }

      this.G.clear();
      this.q = this.f = null;
   }

   protected boolean accepts(b var1, double var2, double var4) {
      return var1.contains(var2, var4);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.q != null && this.q.captureMouse(mo.Z(var1))) {
         return true;
      }

      for (b var4 : this.G) {
         if (this.accepts(var4, var1.x(), var1.y())) {
            if (this.q != null && this.q != var4) {
               this.q.blur();
            }

            this.q = var4;
            var4.l = (boolean)1;
            var4.R = (boolean)0;
            if (var4.click(var1.x(), var1.y(), mo.Z(var1))) {
               this.f = var4;
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
      if (this.f != null) {
         this.f.drag(var1.x(), var1.y());
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.f != null) {
         this.f.release();
      }

      this.f = null;
      return true;
   }

   public boolean keyPressed(KeyEvent var1) {
      if (mo.O(var1) != 258) {
         return this.q != null && this.q.key(mo.O(var1)) ? true : super.keyPressed(var1);
      }

      int var2 = (var1.modifiers() & 1) != 0 ? -1 : 1;
      int var3 = this.q == null ? (var2 > 0 ? -1 : 0) : this.G.indexOf(this.q);
      if (this.q != null) {
         this.q.blur();
      }

      for (int var4 = 0; var4 < this.G.size(); var4++) {
         var3 = Math.floorMod(var3 + var2, this.G.size());
         b var5 = this.G.get(var3);
         if (var5.X && var5.D) {
            this.q = var5;
            var5.l = (boolean)1;
            var5.R = (boolean)1;
            this.focusChanged(var5);
            break;
         }
      }

      return true;
   }

   protected void focusChanged(b var1) {
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.q != null && this.q.character(var1.codepoint());
   }

   public boolean isPauseScreen() {
      return false;
   }
}
