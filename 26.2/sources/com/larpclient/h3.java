package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public abstract class h3 extends Screen implements bg {
   protected final List<jd> p = new ArrayList<>();
   protected jd o;
   protected jd F;

   protected h3(String var1) {
      super(Component.literal(var1));
   }

   protected void u() {
      for (jd var2 : this.p) {
         var2.blur();
      }

      this.p.clear();
      this.o = this.F = null;
   }

   protected boolean accepts(jd var1, double var2, double var4) {
      return var1.contains(var2, var4);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.o != null && this.o.captureMouse(k2.w(var1))) {
         return true;
      }

      for (jd var4 : this.p) {
         if (this.accepts(var4, var1.x(), var1.y())) {
            if (this.o != null && this.o != var4) {
               this.o.blur();
            }

            this.o = var4;
            var4.T = (boolean)1;
            var4.W = (boolean)0;
            if (var4.click(var1.x(), var1.y(), k2.w(var1))) {
               this.F = var4;
               return true;
            }
         }
      }

      if (this.o != null) {
         this.o.blur();
      }

      this.o = null;
      return false;
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      if (this.F != null) {
         this.F.drag(var1.x(), var1.y());
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      if (this.F != null) {
         this.F.release();
      }

      this.F = null;
      return true;
   }

   public boolean keyPressed(KeyEvent var1) {
      if (k2.a(var1) != 258) {
         return this.o != null && this.o.key(k2.a(var1)) ? true : super.keyPressed(var1);
      }

      int var2 = (var1.modifiers() & 1) != 0 ? -1 : 1;
      int var3 = this.o == null ? (var2 > 0 ? -1 : 0) : this.p.indexOf(this.o);
      if (this.o != null) {
         this.o.blur();
      }

      for (int var4 = 0; var4 < this.p.size(); var4++) {
         var3 = Math.floorMod(var3 + var2, this.p.size());
         jd var5 = this.p.get(var3);
         if (var5.p && var5.i) {
            this.o = var5;
            var5.T = (boolean)1;
            var5.W = (boolean)1;
            this.focusChanged(var5);
            break;
         }
      }

      return true;
   }

   protected void focusChanged(jd var1) {
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.o != null && this.o.character(var1.codepoint());
   }

   public boolean isPauseScreen() {
      return false;
   }
}
