package com.larpclient.gui.widget;

import com.larpclient.util.render.DrawUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class Widget {
   public int x;
   public int y;
   public int width;
   public int height = 24;
   public boolean visible = (boolean)1;
   public boolean enabled = (boolean)1;
   public boolean focused;
   public boolean focusVisible;

   public void bounds(int var1, int var2, int var3) {
      this.x = var1;
      this.y = var2;
      this.width = var3;
   }

   public boolean contains(double var1, double var3) {
      return this.visible && this.enabled && DrawUtil.isHovered(var1, var3, this.x, this.y, this.width, this.height);
   }

   public abstract void render(GuiGraphicsExtractor var1, int var2, int var3);

   public boolean click(double var1, double var3, int var5) {
      return false;
   }

   public void drag(double var1, double var3) {
   }

   public void release() {
   }

   public boolean key(int var1) {
      return false;
   }

   public boolean character(int var1) {
      return false;
   }

   public boolean captureMouse(int var1) {
      return false;
   }

   public void blur() {
      this.focused = (boolean)0;
      this.focusVisible = (boolean)0;
      this.release();
   }
}
