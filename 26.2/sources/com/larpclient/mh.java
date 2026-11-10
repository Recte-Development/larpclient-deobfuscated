package com.larpclient;

import com.larpclient.update.UpdateLayout;
import com.larpclient.update.UpdateRuntime;
import com.mojang.blaze3d.platform.cursor.CursorTypes;
import com.mojang.realmsclient.RealmsMainScreen;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.InputType;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public final class mh extends Screen implements ma {
   private static final m7.m f = new m7.m();
   private static final tx.q q = new tx.q();
   private static final String Z = "Larp Client";
   private static final bj.j h = bj.j.y;
   private static final float C = 0.45F;
   private static final float S = 1.1F;
   private final m7 b = f.t();
   private final List<sw> m = new ArrayList<>();
   private final List<mh.n> U = new ArrayList<>();
   private uz F;
   private sw e;
   private sw c;
   private sw I;
   private sw n;
   private sw a;
   private UpdateLayout.Notice v;
   private boolean p;
   private boolean o;
   private boolean N;
   private m7.l V;
   private final b6 D = new b6();
   private final bm y = new bm(q.P());
   private final cm W = new cm(this);

   public mh() {
      super(Component.literal("Larp Client"));
   }

   public static boolean j() {
      LarpClient var0 = LarpClient.R();
      return var0 == null || var0.o() == null || var0.o().g().t;
   }

   protected void init() {
      this.F = uz.calculate(this.width, this.height, (int)Math.ceil(bj.j("Larp Client", h) * 1.375F));
      this.E();
      this.m.clear();
      if (!this.J().M) {
         this.b.v();
      }

      this.e = (sw)this.addRenderableWidget(
         new sw(Component.translatable("menu.singleplayer"), sw.r.t, () -> this.n(new SelectWorldScreen(this)), this.F.navigation(0))
      );
      this.addRenderableWidget(new sw(Component.translatable("menu.multiplayer"), sw.r.t, () -> this.n(new JoinMultiplayerScreen(this)), this.F.navigation(1)));
      this.addRenderableWidget(new sw(Component.translatable("menu.online"), sw.r.t, () -> this.n(new RealmsMainScreen(this)), this.F.navigation(2)));
      this.addRenderableWidget(
         new sw(Component.literal("Minecraft Settings"), sw.r.J, () -> this.n(new OptionsScreen(this, this.minecraft.options, false)), this.F.navigation(3))
      );
      this.addRenderableWidget(new sw(Component.literal("Client"), sw.r.J, () -> this.n(hi.w(this)), this.F.navigation(4)));
      this.addRenderableWidget(new sw(Component.translatable("menu.quit"), sw.r.a, this.minecraft::stop, this.F.navigation(5)));
      tx.m var1 = bm.R(this.F);
      this.I = (sw)this.addRenderableWidget(
         new sw(
            Component.literal("Switch Microsoft account: " + this.minecraft.getUser().getName()),
            sw.r.V,
            this.W::u,
            var1.visible() ? var1.name() : this.F.account()
         )
      );
      this.W.N(new p1.e(this.I.getX(), this.I.getY(), this.I.getWidth(), this.I.getHeight()));
      this.I.C = var1x -> {
         String var2 = bj.v(this.minecraft.getUser().getName(), this.I.getWidth() - 32, bj.j.w);
         int var3x = bj.G(var2, bj.j.w);
         int var4x = this.I.getX() + (this.I.getWidth() - var3x - 14) / 2;
         bj.z(var1x, var2, var4x, this.I.getY() + 4, uh.F(-1513240, this.I.E()), bj.j.w);
         ur.O(var1x, ur.V(this.W.o() ? "chevron-up" : "chevron-down"), var4x + var3x + 5, this.I.getY() + 4, 9, uh.F(-5592406, this.I.E()));
         double var5 = this.I.H();
         sw.l(
            var1x,
            this.I.getX() + this.I.getWidth() / 2.0F,
            this.I.getY() + 17,
            (float)(20.0 + (var3x + 14 - 20) * var5),
            uh.F(uh.q(-11447983, -4342339, var5), this.I.E())
         );
      };

      for (GuiEventListener var3 : this.children()) {
         if (var3 instanceof sw var4 && var4 != this.I) {
            this.m.add(var4);
         }
      }

      this.c = (sw)this.addRenderableWidget(new sw(Component.literal("Press to enter"), sw.r.t, this::A, m7.T(this.width, this.height)));
      this.v = UpdateLayout.notice(this.width, this.height);
      this.n = (sw)this.addRenderableWidget(new sw(Component.literal("View update"), sw.r.J, () -> {
         UpdateRuntime.later();
         this.n(hf.Z(this, "Updates"));
      }, this.v.open()));
      this.a = (sw)this.addRenderableWidget(new sw(Component.literal("Later"), sw.r.a, UpdateRuntime::later, this.v.later()));
      this.b();
      com.larpclient.h.U(
         "Menu", "Text navigation initialized at {}x{}; bordered labels, Realms under Multiplayer, direct Minecraft options", this.width, this.height
      );
   }

   protected void setInitialFocus() {
      if (!this.b.l()) {
         if (this.b.G()) {
            this.setInitialFocus(this.c);
         } else {
            this.clearFocus();
         }
      } else {
         if (this.minecraft.getLastInputType().isKeyboard()) {
            this.setInitialFocus(this.e);
         } else {
            this.clearFocus();
         }
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.L();
      m7.h var5 = this.b.L(this.F, this.c());
      if (this.J().u && this.c()) {
         this.T(var1);
      }

      var1.nextStratum();
      m7.e var6 = this.b.Q(this.F, this.c());
      if (var6.alpha() > 0.0) {
         this.Q(var1, var6);
         var1.nextStratum();
      }

      var1.pose().pushMatrix();

      try {
         var1.pose().translate((float)var5.x(), (float)var5.y());
         var1.pose().rotate((float)var5.rotation());
         float var7 = (float)(var5.size() / 64.0);
         float var8 = (float)(var5.size() / 64.0 * var5.stretch());
         var1.pose().scale(var7, var8);

         for (int var9 = 0; var9 < 16; var9++) {
            double var10 = var9 * Math.PI / 8.0;
            var1.pose().pushMatrix();

            try {
               var1.pose().translate((float)(Math.cos(var10) * 1.1F / var7), (float)(Math.sin(var10) * 1.1F / var8));
               var1.blit(RenderPipelines.GUI_TEXTURED, gr.V(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -16777216);
            } finally {
               var1.pose().popMatrix();
            }
         }

         var1.blit(RenderPipelines.GUI_TEXTURED, gr.V(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -855310);
      } finally {
         var1.pose().popMatrix();
      }

      m7.q var20 = this.b.j(this.c());
      if (!this.b.l() && var20.alpha() > 0.0) {
         this.v(
            var1,
            this.width / 2.0F,
            (float)(this.height * 0.48 + Math.clamp(this.height * 0.16, 30.0, 86.0) / 2.0 + 14.0),
            Math.min(1.65F, this.F.brandScale() + 0.15F),
            var20.alpha()
         );
      }

      this.y.v(var1, this.F, this.b, this.D, var2, var3, this.c(), this.J().M);
      this.h();
      var1.nextStratum();
      this.x(var1);
      if (!this.W.o() && this.H() && this.B(var2, var3)) {
         var1.requestCursor(CursorTypes.POINTING_HAND);
      }

      int var21 = this.W.o() || this.y.w() || !this.b.l() && !this.b.G() ? 0 : 1;
      super.extractRenderState(var1, var21 != 0 ? var2 : -1, var21 != 0 ? var3 : -1, var4);
      this.y.L(var1);
      if (this.y.w()) {
         var1.requestCursor(CursorTypes.RESIZE_ALL);
      } else if (!this.W.o() && this.y.S(var2, var3)) {
         var1.requestCursor(CursorTypes.POINTING_HAND);
      }

      this.W.O(var1, var2, var3);
   }

   private void x(GuiGraphicsExtractor var1) {
      if (this.n != null && this.n.visible) {
         UpdateRuntime.noticeShown();
         if (!this.v.compact()) {
            p1.e var2 = this.v.surface();
            tb.E(var1, var2.x(), var2.y(), var2.width(), var2.height(), 5.0F, -451208421);
            bj.z(var1, bj.G(UpdateRuntime.noticeTitle(), var2.width() - 16), var2.x() + 8, var2.y() + 9, -2171170, bj.j.T);
            String var3 = UpdateRuntime.noticeDetail();
            bj.h(var1, bj.G(var3, var2.width() - 16), var2.x() + 8, var2.y() + 25, -6513508);
         }
      }
   }

   private void v(GuiGraphicsExtractor var1, float var2, float var3, float var4, double var5) {
      float var7 = var4 * 1.375F;
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(var2 - bj.j("Larp Client", h) * var7 / 2.0F, var3 + 4.5F * var4 - 4.6F * var7);
         var1.pose().scale(var7, var7);
         bj.Q(var1, "Larp Client", 0, 0, uh.F(-1644826, var5), uh.F(-16777216, var5), 0.45F, h);
      } finally {
         var1.pose().popMatrix();
      }
   }

   private void Q(GuiGraphicsExtractor var1, m7.e var2) {
      p1.e var3 = this.F.title();
      float var4 = this.F.brandScale() * 1.375F;
      float var5 = (float)(var3.y() + var3.height() / 2.0 - 4.6F * var4 + var2.lift());
      if (!(var2.clip() <= var2.x()) && !this.U.isEmpty()) {
         for (mh.n var7 : this.U) {
            double var8 = var2.x() + var7.x() * var4;
            double var10 = Math.max(1.0F, var7.width() * var4);
            double var12 = Math.clamp((var8 + var10 - var2.clip()) / var10, 0.0, 1.0);
            double var14 = var2.alpha() * var12 * var12 * (3.0 - 2.0 * var12);
            if (!(var14 < 0.016)) {
               l(var1, var7.text(), (float)var8, var5, var4, var14);
            }
         }
      } else {
         l(var1, "Larp Client", (float)var2.x(), var5, var4, var2.alpha());
      }
   }

   private static void l(GuiGraphicsExtractor var0, String var1, float var2, float var3, float var4, double var5) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var2, var3);
         var0.pose().scale(var4, var4);
         bj.Q(var0, var1, 0, 0, uh.F(-1644826, var5), uh.F(-16777216, var5), 0.45F, h);
      } finally {
         var0.pose().popMatrix();
      }
   }

   private void E() {
      this.U.clear();
      float var1 = 0.0F;
      int var2 = 0;

      while (var2 < "Larp Client".length()) {
         int var3 = "Larp Client".codePointAt(var2);
         String var4 = "Larp Client".substring(var2, var2 + Character.charCount(var3));
         var2 += var4.length();
         float var5 = bj.j(var4, h);
         this.U.add(new mh.n(var4, var1, var5));
         var1 += var5;
      }
   }

   private void T(GuiGraphicsExtractor var1) {
      for (int var2 = 0; var2 < 12; var2++) {
         m7.b var3 = this.b.U(var2, this.F);
         if (!(var3.alpha() <= 0.0)) {
            var1.pose().pushMatrix();

            try {
               var1.pose().translate((float)var3.x(), (float)var3.y());
               var1.pose().rotate((float)var3.rotation());
               float var4 = (float)(var3.size() / 64.0);
               var1.pose().scale(var4, var4);
               var1.blit(RenderPipelines.GUI_TEXTURED, gr.V(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, uh.H(-1513240, var3.alpha()));
            } finally {
               var1.pose().popMatrix();
            }
         }
      }
   }

   private hb J() {
      return LarpClient.R().o().g();
   }

   private void n(Screen var1) {
      if (var1 instanceof hf var2) {
         var2.x();
      } else if (var1 instanceof tw var3) {
         var3.larpOpenMenuPopup();
      } else {
         com.larpclient.h.g("Menu", "Popup hook unavailable for {}; opening destination directly", var1.getClass().getSimpleName());
      }

      com.larpclient.h.U("Menu", "Opening {} with the shared {} ms popup", var1.getClass().getSimpleName(), 240);
      this.minecraft.gui.setScreen(var1);
   }

   private boolean c() {
      return this.J().L && this.J().o != j.v.A && (Double)this.minecraft.options.panoramaSpeed().get() > 0.0;
   }

   public m7 L() {
      this.b.k(var1x -> k2.w(this.minecraft.getWindow(), var1x));
      long var1 = System.nanoTime();
      int var3 = this.minecraft.gui.overlay() == null && this.minecraft.isWindowActive() ? 1 : 0;
      this.b.w(var1, (boolean)var3, this.c());
      this.b();
      if (!this.N && this.minecraft.gui.overlay() == null) {
         this.N = (boolean)1;
         com.larpclient.h.A("Menu", "Launch presentation: {}; Enter, Space or click to enter; another press skips motion", this.b.M());
      }

      return this.b;
   }

   private void b() {
      if (this.c != null) {
         this.c.visible = (boolean)(!this.b.l() && this.b.S(this.c()).alpha() > 0.0 ? 1 : 0);
         this.c.active = this.b.G();
         this.c.I(this.b.S(this.c()));

         for (int var1 = 0; var1 < this.m.size(); var1++) {
            sw var2 = this.m.get(var1);
            m7.q var3 = this.b.Z(var1);
            var2.I(var3);
            var2.visible = (boolean)(var3.alpha() > 0.0 ? 1 : 0);
            var2.active = this.b.l();
         }

         this.h();
         if (this.n != null) {
            int var4 = this.b.l() && UpdateRuntime.notice() ? 1 : 0;
            this.n.visible = this.n.active = (boolean)var4;
            this.a.visible = this.a.active = (boolean)(var4 != 0 && !this.v.compact() ? 1 : 0);
            this.n.setMessage(Component.literal(this.v.compact() ? UpdateRuntime.noticeTitle() : UpdateRuntime.noticeAction()));
         }

         if (this.V != this.b.M()) {
            m7.l var5 = this.V;
            this.V = this.b.M();
            this.setInitialFocus();
            if (this.b.l()) {
               com.larpclient.h.A("Menu", "Launch reveal settled; native navigation active");
            } else if (this.b.G() && var5 == m7.l.R) {
               com.larpclient.h.A("Menu", "Entrance replay reached the welcome pose; waiting for entry");
            }
         }
      }
   }

   private void h() {
      if (this.I != null) {
         int var1 = !this.b.l() || tx.a(this.F).visible() && this.y.Y() != 1.0 ? 0 : 1;
         this.I.visible = this.I.active = (boolean)var1;
         this.I.I(m7.q.REST);
         this.I.setMessage(Component.literal("Switch Microsoft account: " + this.minecraft.getUser().getName()));
      }
   }

   private void A() {
      this.L();
      if (this.b.H(this.c())) {
         this.clearFocus();
         this.b();
         com.larpclient.h.A(
            "Menu", "Entry accepted; logo anticipation and side arc into the lockup, then the wordmark slides out from behind it while the menu staggers in"
         );
      }
   }

   private boolean B(double var1, double var3) {
      p1.e var5 = this.F.logo();
      p1.e var6 = this.F.title();
      int var7 = Math.min(var5.x(), var6.x());
      int var8 = Math.min(var5.y(), var6.y());
      int var9 = Math.max(var5.x() + var5.width(), var6.x() + var6.width());
      int var10 = Math.max(var5.y() + var5.height(), var6.y() + var6.height());
      return var1 >= var7 && var1 < var9 && var3 >= var8 && var3 < var10;
   }

   private boolean H() {
      return this.b.l() && this.J().M;
   }

   private void F() {
      this.L();
      if (this.b.g(this.c())) {
         this.W.j();
         this.clearFocus();
         this.y.H();
         this.b();
         com.larpclient.h.A("Menu", "Logo clicked; rewinding the entrance to the welcome pose ({})", this.c() ? "animated" : "reduced motion");
      }
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.W.t(var1) || super.charTyped(var1);
   }

   public boolean keyPressed(KeyEvent var1) {
      if (this.W.v(var1)) {
         return true;
      }

      int var2 = k2.a(var1);
      if (this.b.G(var2)) {
         return true;
      }

      if (!this.b.l()) {
         if (var2 == 257 || var2 == 335 || var2 == 32) {
            this.minecraft.setLastInputType(InputType.KEYBOARD_TAB);
            this.b.f(var2);
            if (this.b.G()) {
               this.c.playDownSound(this.minecraft.getSoundManager());
               this.A();
            } else {
               this.b.S();
               this.b();
            }

            return true;
         }

         if (!this.b.G()) {
            this.b.S();
            this.b();
         }

         if (this.b.G()) {
            return true;
         }
      }

      return super.keyPressed(var1);
   }

   public boolean keyReleased(KeyEvent var1) {
      return this.b.W(k2.a(var1)) || super.keyReleased(var1);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.W.y(var1)) {
         return true;
      }

      if (!this.b.l()) {
         if (k2.w(var1) == 0) {
            this.p = (boolean)1;
            if (this.b.G()) {
               this.c.playDownSound(this.minecraft.getSoundManager());
               this.A();
            } else {
               this.b.S();
               this.b();
            }
         }

         return true;
      } else if (k2.w(var1) == 0 && this.y.j(var1.x(), var1.y())) {
         this.o = (boolean)1;
         this.W.j();
         this.clearFocus();
         return true;
      } else if (super.mouseClicked(var1, var2)) {
         return true;
      } else if (k2.w(var1) == 0 && this.H() && this.B(var1.x(), var1.y())) {
         this.p = (boolean)1;
         this.c.playDownSound(this.minecraft.getSoundManager());
         this.F();
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      if (k2.w(var1) == 0 && this.o) {
         this.o = (boolean)0;
         this.y.c();
         return true;
      } else if (this.W.o()) {
         return true;
      } else if (this.p && k2.w(var1) == 0) {
         this.p = (boolean)0;
         return true;
      } else {
         return !this.b.l() || super.mouseReleased(var1);
      }
   }

   public void mouseMoved(double var1, double var3) {
      this.y.c(var1, var3);
      super.mouseMoved(var1, var3);
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      this.y.c(var1.x(), var1.y());
      return this.y.w() || super.mouseDragged(var1, var2, var4);
   }

   public void added() {
      super.added();
      this.b.k(var1 -> k2.w(this.minecraft.getWindow(), var1));
      if (this.b.G()) {
         for (int var4 : new int[]{257, 335, 32}) {
            if (k2.w(this.minecraft.getWindow(), var4)) {
               this.b.f(var4);
            }
         }
      }
   }

   public void removed() {
      this.W.j();
      if (!this.b.G()) {
         this.b.S();
      }

      this.o = (boolean)0;
      this.y.f(!this.b.G());
      super.removed();
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      return this.W.h(var1, var3, var7) || super.mouseScrolled(var1, var3, var5, var7);
   }

   public boolean isPauseScreen() {
      return false;
   }

   private record n(String text, float x, float width) {
   }
}
