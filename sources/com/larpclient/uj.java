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

public final class uj extends Screen implements z2 {
   private static final oh.z Z = new oh.z();
   private static final zt.c D = new zt.c();
   private static final String d = "Larp Client";
   private static final k6.v T = k6.v.v;
   private static final float V = 0.45F;
   private static final float q = 1.1F;
   private final oh z = Z.l();
   private final List<uk> O = new ArrayList<>();
   private final List<uj.v> K = new ArrayList<>();
   private o4 G;
   private uk y;
   private uk M;
   private uk g;
   private uk f;
   private uk A;
   private UpdateLayout.Notice o;
   private boolean E;
   private boolean l;
   private boolean U;
   private oh.m N;
   private final cn j = new cn();
   private final mv S = new mv(D.B());
   private final ca u = new ca(this);

   public uj() {
      super(Component.literal("Larp Client"));
   }

   public static boolean l() {
      LarpClient var0 = LarpClient.F();
      return var0 == null || var0.f() == null || var0.f().t().U;
   }

   protected void init() {
      this.G = o4.calculate(this.width, this.height, (int)Math.ceil(k6.u("Larp Client", T) * 1.375F));
      this.v();
      this.O.clear();
      if (!this.j().r) {
         this.z.N();
      }

      this.y = (uk)this.addRenderableWidget(
         new uk(Component.translatable("menu.singleplayer"), uk.a.d, () -> this.e(new SelectWorldScreen(this)), this.G.navigation(0))
      );
      this.addRenderableWidget(new uk(Component.translatable("menu.multiplayer"), uk.a.d, () -> this.e(new JoinMultiplayerScreen(this)), this.G.navigation(1)));
      this.addRenderableWidget(new uk(Component.translatable("menu.online"), uk.a.d, () -> this.e(new RealmsMainScreen(this)), this.G.navigation(2)));
      this.addRenderableWidget(
         new uk(Component.literal("Minecraft Settings"), uk.a.h, () -> this.e(new OptionsScreen(this, this.minecraft.options)), this.G.navigation(3))
      );
      this.addRenderableWidget(new uk(Component.literal("Client"), uk.a.h, () -> this.e(h1.G(this)), this.G.navigation(4)));
      this.addRenderableWidget(new uk(Component.translatable("menu.quit"), uk.a.U, this.minecraft::stop, this.G.navigation(5)));
      zt.q var1 = mv.G(this.G);
      this.g = (uk)this.addRenderableWidget(
         new uk(
            Component.literal("Switch Microsoft account: " + this.minecraft.getUser().getName()),
            uk.a.Q,
            this.u::D,
            var1.visible() ? var1.name() : this.G.account()
         )
      );
      this.u.w(new p5.w(this.g.getX(), this.g.getY(), this.g.getWidth(), this.g.getHeight()));
      this.g.b = var1x -> {
         String var2 = k6.Q(this.minecraft.getUser().getName(), this.g.getWidth() - 32, k6.v.L);
         int var3x = k6.w(var2, k6.v.L);
         int var4x = this.g.getX() + (this.g.getWidth() - var3x - 14) / 2;
         k6.r(var1x, var2, var4x, this.g.getY() + 4, _3.U(-1513240, this.g.M()), k6.v.L);
         tp.N(var1x, tp.j(this.u.O() ? "chevron-up" : "chevron-down"), var4x + var3x + 5, this.g.getY() + 4, 9, _3.U(-5592406, this.g.M()));
         double var5 = this.g.V();
         uk.m(
            var1x,
            this.g.getX() + this.g.getWidth() / 2.0F,
            this.g.getY() + 17,
            (float)(20.0 + (var3x + 14 - 20) * var5),
            _3.U(_3.D(-11447983, -4342339, var5), this.g.M())
         );
      };

      for (GuiEventListener var3 : this.children()) {
         if (var3 instanceof uk var4 && var4 != this.g) {
            this.O.add(var4);
         }
      }

      this.M = (uk)this.addRenderableWidget(new uk(Component.literal("Press to enter"), uk.a.d, this::H, oh.q(this.width, this.height)));
      this.o = UpdateLayout.notice(this.width, this.height);
      this.f = (uk)this.addRenderableWidget(new uk(Component.literal("View update"), uk.a.h, () -> UpdateRuntime.open(this), this.o.open()));
      this.A = (uk)this.addRenderableWidget(new uk(Component.literal("Later"), uk.a.U, UpdateRuntime::later, this.o.later()));
      this.X();
      mt.U("Menu", "Text navigation initialized at {}x{}; bordered labels, Realms under Multiplayer, direct Minecraft options", this.width, this.height);
   }

   protected void setInitialFocus() {
      if (!this.z.g()) {
         if (this.z.i()) {
            this.setInitialFocus(this.M);
         } else {
            this.clearFocus();
         }
      } else {
         if (this.minecraft.getLastInputType().isKeyboard()) {
            this.setInitialFocus(this.y);
         } else {
            this.clearFocus();
         }
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.P();
      oh.w var5 = this.z.a(this.G, this.O());
      if (this.j().L && this.O()) {
         this.Q(var1);
      }

      var1.nextStratum();
      oh.l var6 = this.z.P(this.G, this.O());
      if (var6.alpha() > 0.0) {
         this.Y(var1, var6);
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
               var1.blit(RenderPipelines.GUI_TEXTURED, pw.k(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -16777216);
            } finally {
               var1.pose().popMatrix();
            }
         }

         var1.blit(RenderPipelines.GUI_TEXTURED, pw.k(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -855310);
      } finally {
         var1.pose().popMatrix();
      }

      oh.x var20 = this.z.G(this.O());
      if (!this.z.g() && var20.alpha() > 0.0) {
         this.B(
            var1,
            this.width / 2.0F,
            (float)(this.height * 0.48 + Math.clamp(this.height * 0.16, 30.0, 86.0) / 2.0 + 14.0),
            Math.min(1.65F, this.G.brandScale() + 0.15F),
            var20.alpha()
         );
      }

      this.S.V(var1, this.G, this.z, this.j, var2, var3, this.O(), this.j().r);
      this.b();
      var1.nextStratum();
      this.g(var1);
      if (!this.u.O() && this.D() && this.M(var2, var3)) {
         var1.requestCursor(CursorTypes.POINTING_HAND);
      }

      int var21 = this.u.O() || this.S.J() || !this.z.g() && !this.z.i() ? 0 : 1;
      super.extractRenderState(var1, var21 != 0 ? var2 : -1, var21 != 0 ? var3 : -1, var4);
      this.S.K(var1);
      if (this.S.J()) {
         var1.requestCursor(CursorTypes.RESIZE_ALL);
      } else if (!this.u.O() && this.S.d(var2, var3)) {
         var1.requestCursor(CursorTypes.POINTING_HAND);
      }

      this.u.s(var1, var2, var3);
   }

   private void g(GuiGraphicsExtractor var1) {
      if (this.f != null && this.f.visible) {
         UpdateRuntime.noticeShown();
         if (!this.o.compact()) {
            p5.w var2 = this.o.surface();
            tl.g(var1, var2.x(), var2.y(), var2.width(), var2.height(), 5.0F, -451208421);
            k6.r(var1, k6.n(UpdateRuntime.noticeTitle(), var2.width() - 16), var2.x() + 8, var2.y() + 9, -2171170, k6.v.B);
            String var3 = UpdateRuntime.noticeDetail();
            k6.G(var1, k6.n(var3, var2.width() - 16), var2.x() + 8, var2.y() + 25, -6513508);
         }
      }
   }

   private void B(GuiGraphicsExtractor var1, float var2, float var3, float var4, double var5) {
      float var7 = var4 * 1.375F;
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(var2 - k6.u("Larp Client", T) * var7 / 2.0F, var3 + 4.5F * var4 - 4.6F * var7);
         var1.pose().scale(var7, var7);
         k6.s(var1, "Larp Client", 0, 0, _3.U(-1644826, var5), _3.U(-16777216, var5), 0.45F, T);
      } finally {
         var1.pose().popMatrix();
      }
   }

   private void Y(GuiGraphicsExtractor var1, oh.l var2) {
      p5.w var3 = this.G.title();
      float var4 = this.G.brandScale() * 1.375F;
      float var5 = (float)(var3.y() + var3.height() / 2.0 - 4.6F * var4 + var2.lift());
      if (!(var2.clip() <= var2.x()) && !this.K.isEmpty()) {
         for (uj.v var7 : this.K) {
            double var8 = var2.x() + var7.x() * var4;
            double var10 = Math.max(1.0F, var7.width() * var4);
            double var12 = Math.clamp((var8 + var10 - var2.clip()) / var10, 0.0, 1.0);
            double var14 = var2.alpha() * var12 * var12 * (3.0 - 2.0 * var12);
            if (!(var14 < 0.016)) {
               q(var1, var7.text(), (float)var8, var5, var4, var14);
            }
         }
      } else {
         q(var1, "Larp Client", (float)var2.x(), var5, var4, var2.alpha());
      }
   }

   private static void q(GuiGraphicsExtractor var0, String var1, float var2, float var3, float var4, double var5) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var2, var3);
         var0.pose().scale(var4, var4);
         k6.s(var0, var1, 0, 0, _3.U(-1644826, var5), _3.U(-16777216, var5), 0.45F, T);
      } finally {
         var0.pose().popMatrix();
      }
   }

   private void v() {
      this.K.clear();
      float var1 = 0.0F;
      int var2 = 0;

      while (var2 < "Larp Client".length()) {
         int var3 = "Larp Client".codePointAt(var2);
         String var4 = "Larp Client".substring(var2, var2 + Character.charCount(var3));
         var2 += var4.length();
         float var5 = k6.u(var4, T);
         this.K.add(new uj.v(var4, var1, var5));
         var1 += var5;
      }
   }

   private void Q(GuiGraphicsExtractor var1) {
      for (int var2 = 0; var2 < 12; var2++) {
         oh.k var3 = this.z.p(var2, this.G);
         if (!(var3.alpha() <= 0.0)) {
            var1.pose().pushMatrix();

            try {
               var1.pose().translate((float)var3.x(), (float)var3.y());
               var1.pose().rotate((float)var3.rotation());
               float var4 = (float)(var3.size() / 64.0);
               var1.pose().scale(var4, var4);
               var1.blit(RenderPipelines.GUI_TEXTURED, pw.k(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, _3.A(-1513240, var3.alpha()));
            } finally {
               var1.pose().popMatrix();
            }
         }
      }
   }

   private xv j() {
      return LarpClient.F().f().t();
   }

   private void e(Screen var1) {
      if (var1 instanceof hw var2) {
         var2.A();
      } else if (var1 instanceof wv var3) {
         var3.larpOpenMenuPopup();
      } else {
         mt.T("Menu", "Popup hook unavailable for {}; opening destination directly", var1.getClass().getSimpleName());
      }

      mt.U("Menu", "Opening {} with the shared {} ms popup", var1.getClass().getSimpleName(), 240);
      this.minecraft.gui.setScreen(var1);
   }

   private boolean O() {
      return this.j().V && this.j().A != tf.f.E && (Double)this.minecraft.options.panoramaSpeed().get() > 0.0;
   }

   public oh P() {
      this.z.Y(var1x -> mo.N(this.minecraft.getWindow(), var1x));
      long var1 = System.nanoTime();
      int var3 = this.minecraft.gui.overlay() == null && this.minecraft.isWindowActive() ? 1 : 0;
      this.z.A(var1, (boolean)var3, this.O());
      this.X();
      if (!this.U && this.minecraft.gui.overlay() == null) {
         this.U = (boolean)1;
         mt.K("Menu", "Launch presentation: {}; Enter, Space or click to enter; another press skips motion", this.z.k());
      }

      return this.z;
   }

   private void X() {
      if (this.M != null) {
         this.M.visible = (boolean)(!this.z.g() && this.z.k(this.O()).alpha() > 0.0 ? 1 : 0);
         this.M.active = this.z.i();
         this.M.K(this.z.k(this.O()));

         for (int var1 = 0; var1 < this.O.size(); var1++) {
            uk var2 = this.O.get(var1);
            oh.x var3 = this.z.c(var1);
            var2.K(var3);
            var2.visible = (boolean)(var3.alpha() > 0.0 ? 1 : 0);
            var2.active = this.z.g();
         }

         this.b();
         if (this.f != null) {
            int var4 = this.z.g() && UpdateRuntime.notice() ? 1 : 0;
            this.f.visible = this.f.active = (boolean)var4;
            this.A.visible = this.A.active = (boolean)(var4 != 0 && !this.o.compact() ? 1 : 0);
            this.f.setMessage(Component.literal(this.o.compact() ? UpdateRuntime.noticeTitle() : UpdateRuntime.noticeAction()));
         }

         if (this.N != this.z.k()) {
            oh.m var5 = this.N;
            this.N = this.z.k();
            this.setInitialFocus();
            if (this.z.g()) {
               mt.K("Menu", "Launch reveal settled; native navigation active");
            } else if (this.z.i() && var5 == oh.m.t) {
               mt.K("Menu", "Entrance replay reached the welcome pose; waiting for entry");
            }
         }
      }
   }

   private void b() {
      if (this.g != null) {
         int var1 = !this.z.g() || zt.a(this.G).visible() && this.S.M() != 1.0 ? 0 : 1;
         this.g.visible = this.g.active = (boolean)var1;
         this.g.K(oh.x.REST);
         this.g.setMessage(Component.literal("Switch Microsoft account: " + this.minecraft.getUser().getName()));
      }
   }

   private void H() {
      this.P();
      if (this.z.l(this.O())) {
         this.clearFocus();
         this.X();
         mt.K("Menu", "Entry accepted; logo anticipation and side arc into the lockup, then the wordmark slides out from behind it while the menu staggers in");
      }
   }

   private boolean M(double var1, double var3) {
      p5.w var5 = this.G.logo();
      p5.w var6 = this.G.title();
      int var7 = Math.min(var5.x(), var6.x());
      int var8 = Math.min(var5.y(), var6.y());
      int var9 = Math.max(var5.x() + var5.width(), var6.x() + var6.width());
      int var10 = Math.max(var5.y() + var5.height(), var6.y() + var6.height());
      return var1 >= var7 && var1 < var9 && var3 >= var8 && var3 < var10;
   }

   private boolean D() {
      return this.z.g() && this.j().r;
   }

   private void L() {
      this.P();
      if (this.z.j(this.O())) {
         this.u.j();
         this.clearFocus();
         this.S.X();
         this.X();
         mt.K("Menu", "Logo clicked; rewinding the entrance to the welcome pose ({})", this.O() ? "animated" : "reduced motion");
      }
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.u.A(var1) || super.charTyped(var1);
   }

   public boolean keyPressed(KeyEvent var1) {
      if (this.u.X(var1)) {
         return true;
      }

      int var2 = mo.O(var1);
      if (this.z.p(var2)) {
         return true;
      }

      if (!this.z.g()) {
         if (var2 == 257 || var2 == 335 || var2 == 32) {
            this.minecraft.setLastInputType(InputType.KEYBOARD_TAB);
            this.z.R(var2);
            if (this.z.i()) {
               this.M.playDownSound(this.minecraft.getSoundManager());
               this.H();
            } else {
               this.z.V();
               this.X();
            }

            return true;
         }

         if (!this.z.i()) {
            this.z.V();
            this.X();
         }

         if (this.z.i()) {
            return true;
         }
      }

      return super.keyPressed(var1);
   }

   public boolean keyReleased(KeyEvent var1) {
      return this.z.K(mo.O(var1)) || super.keyReleased(var1);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.u.d(var1)) {
         return true;
      }

      if (!this.z.g()) {
         if (mo.Z(var1) == 0) {
            this.E = (boolean)1;
            if (this.z.i()) {
               this.M.playDownSound(this.minecraft.getSoundManager());
               this.H();
            } else {
               this.z.V();
               this.X();
            }
         }

         return true;
      } else if (mo.Z(var1) == 0 && this.S.s(var1.x(), var1.y())) {
         this.l = (boolean)1;
         this.u.j();
         this.clearFocus();
         return true;
      } else if (super.mouseClicked(var1, var2)) {
         return true;
      } else if (mo.Z(var1) == 0 && this.D() && this.M(var1.x(), var1.y())) {
         this.E = (boolean)1;
         this.M.playDownSound(this.minecraft.getSoundManager());
         this.L();
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      if (mo.Z(var1) == 0 && this.l) {
         this.l = (boolean)0;
         this.S.W();
         return true;
      } else if (this.u.O()) {
         return true;
      } else if (this.E && mo.Z(var1) == 0) {
         this.E = (boolean)0;
         return true;
      } else {
         return !this.z.g() || super.mouseReleased(var1);
      }
   }

   public void mouseMoved(double var1, double var3) {
      this.S.O(var1, var3);
      super.mouseMoved(var1, var3);
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      this.S.O(var1.x(), var1.y());
      return this.S.J() || super.mouseDragged(var1, var2, var4);
   }

   public void added() {
      super.added();
      this.z.Y(var1 -> mo.N(this.minecraft.getWindow(), var1));
      if (this.z.i()) {
         for (int var4 : new int[]{257, 335, 32}) {
            if (mo.N(this.minecraft.getWindow(), var4)) {
               this.z.R(var4);
            }
         }
      }
   }

   public void removed() {
      this.u.j();
      if (!this.z.i()) {
         this.z.V();
      }

      this.l = (boolean)0;
      this.S.D(!this.z.i());
      super.removed();
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      return this.u.o(var1, var3, var7) || super.mouseScrolled(var1, var3, var5, var7);
   }

   public boolean isPauseScreen() {
      return false;
   }

   private record v(String text, float x, float width) {
   }
}
