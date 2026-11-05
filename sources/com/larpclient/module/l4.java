package com.larpclient.module;

import com.larpclient.h7;
import com.larpclient.hh;
import com.larpclient.r7;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;

public final class l4 extends Screen implements h7 {
   private static final int B = 11;
   private int h;

   private l4() {
      super(Component.literal("Casino log"));
   }

   public static void x() {
      Minecraft.getInstance().gui.setScreen(new l4());
   }

   public static void V() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.gui.screen() == null) {
         var0.gui.setScreen(new l4());
      }
   }

   public boolean isPauseScreen() {
      return false;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      this.h = Math.max(0, this.h - (int)Math.signum(var7) * 3);
      return true;
   }

   public boolean keyPressed(KeyEvent var1) {
      if (r7.g(var1) == 67) {
         hh.g();
         this.h = 0;
         return true;
      } else {
         return super.keyPressed(var1);
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      super.extractRenderState(var1, var2, var3, var4);
      int var5 = Math.max(1, Math.min(520, this.width - 24));
      int var6 = Math.max(1, Math.min(300, this.height - 24));
      int var7 = (this.width - var5) / 2;
      int var8 = (this.height - var6) / 2;
      var1.fill(var7, var8, var7 + var5, var8 + var6, -804253672);
      var1.text(this.font, "Casino log   Scroll to move, C clears, Esc closes", var7 + 8, var8 + 6, -1513232);
      List var9 = hh.a();
      int var10 = Math.max(1, (var6 - 28) / 11);
      int var11 = Math.max(0, var9.size() - this.h);
      int var12 = Math.max(0, var11 - var10);
      int var13 = var8 + 22;

      for (int var14 = var12; var14 < var11; var14++) {
         String var15 = this.font.plainSubstrByWidth((String)var9.get(var14), var5 - 16);
         var1.text(this.font, var15, var7 + 8, var13, -3618600);
         var13 += 11;
      }

      if (var9.isEmpty()) {
         var1.text(this.font, "No game events yet", var7 + 8, var13, -7829352);
      }
   }
}
