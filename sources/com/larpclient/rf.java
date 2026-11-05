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

public final class rf extends Screen implements hd {
   private static final hi.o U = new hi.o();
   private static final og.j D = new og.j();
   private static final String a = "Larp Client";
   private static final j_.w Q = j_.w.k;
   private static final float z = 0.45F;
   private static final float F = 1.1F;
   private final hi E = U.p();
   private final List<x6> w = new ArrayList<>();
   private final List<rf.x> b = new ArrayList<>();
   private _q u;
   private x6 P;
   private x6 B;
   private x6 M;
   private x6 q;
   private x6 f;
   private UpdateLayout.Notice I;
   private boolean t;
   private boolean l;
   private boolean N;
   private hi.c L;
   private final yd c = new yd();
   private final xu r = new xu(D.K());
   private final u6 m = new u6(this);

   public rf() {
      super(Component.literal("Larp Client"));
   }

   public static boolean H() {
      LarpClient var0 = LarpClient.y();
      return var0 == null || var0.v() == null || var0.v().E().NB;
   }

   protected void init() {
      this.u = _q.calculate(this.width, this.height, (int)Math.ceil(j_.U("Larp Client", Q) * 1.375F));
      this.y();
      this.w.clear();
      if (!this.u().W) {
         this.E.y();
      }

      this.P = (x6)this.addRenderableWidget(
         new x6(Component.translatable("menu.singleplayer"), x6.v.W, () -> this.U(new SelectWorldScreen(this)), this.u.navigation(0))
      );
      this.addRenderableWidget(new x6(Component.translatable("menu.multiplayer"), x6.v.W, () -> this.U(new JoinMultiplayerScreen(this)), this.u.navigation(1)));
      this.addRenderableWidget(new x6(Component.translatable("menu.online"), x6.v.W, () -> this.U(new RealmsMainScreen(this)), this.u.navigation(2)));
      this.addRenderableWidget(
         new x6(Component.literal("Minecraft Settings"), x6.v.S, () -> this.U(new OptionsScreen(this, this.minecraft.options)), this.u.navigation(3))
      );
      this.addRenderableWidget(new x6(Component.literal("Client"), x6.v.S, () -> this.U(pc.o(this)), this.u.navigation(4)));
      this.addRenderableWidget(new x6(Component.translatable("menu.quit"), x6.v.L, this.minecraft::stop, this.u.navigation(5)));
      og.u var1 = xu.V(this.u);
      this.M = (x6)this.addRenderableWidget(
         new x6(
            Component.literal("Switch Microsoft account: " + this.minecraft.getUser().getName()),
            x6.v.A,
            this.m::a,
            var1.visible() ? var1.name() : this.u.account()
         )
      );
      this.m.J(new o6.b(this.M.getX(), this.M.getY(), this.M.getWidth(), this.M.getHeight()));
      this.M.F = var1x -> {
         String var2 = j_.Y(this.minecraft.getUser().getName(), this.M.getWidth() - 32, j_.w.P);
         int var3x = j_.E(var2, j_.w.P);
         int var4x = this.M.getX() + (this.M.getWidth() - var3x - 14) / 2;
         j_.y(var1x, var2, var4x, this.M.getY() + 4, oh.S(-1513240, this.M.z()), j_.w.P);
         ou.u(var1x, ou.e(this.m.I() ? "chevron-up" : "chevron-down"), var4x + var3x + 5, this.M.getY() + 4, 9, oh.S(-5592406, this.M.z()));
         double var5 = this.M.B();
         x6.h(
            var1x,
            this.M.getX() + this.M.getWidth() / 2.0F,
            this.M.getY() + 17,
            (float)(20.0 + (var3x + 14 - 20) * var5),
            oh.S(oh.j(-11447983, -4342339, var5), this.M.z())
         );
      };

      for (GuiEventListener var3 : this.children()) {
         if (var3 instanceof x6 var4 && var4 != this.M) {
            this.w.add(var4);
         }
      }

      this.B = (x6)this.addRenderableWidget(new x6(Component.literal("Press to enter"), x6.v.W, this::o, hi.Z(this.width, this.height)));
      this.I = UpdateLayout.notice(this.width, this.height);
      this.q = (x6)this.addRenderableWidget(new x6(Component.literal("View update"), x6.v.S, () -> UpdateRuntime.open(this), this.I.open()));
      this.f = (x6)this.addRenderableWidget(new x6(Component.literal("Later"), x6.v.L, UpdateRuntime::later, this.I.later()));
      this.f();
      r4.g("Menu", "Text navigation initialized at {}x{}; bordered labels, Realms under Multiplayer, direct Minecraft options", this.width, this.height);
   }

   protected void setInitialFocus() {
      if (!this.E.K()) {
         if (this.E.q()) {
            this.setInitialFocus(this.B);
         } else {
            this.clearFocus();
         }
      } else {
         if (this.minecraft.getLastInputType().isKeyboard()) {
            this.setInitialFocus(this.P);
         } else {
            this.clearFocus();
         }
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.T();
      hi.k var5 = this.E.v(this.u, this.p());
      if (this.u().J && this.p()) {
         this.j(var1);
      }

      var1.nextStratum();
      hi.s var6 = this.E.K(this.u, this.p());
      if (var6.alpha() > 0.0) {
         this.V(var1, var6);
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
               var1.blit(RenderPipelines.GUI_TEXTURED, dm.Q(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -16777216);
            } finally {
               var1.pose().popMatrix();
            }
         }

         var1.blit(RenderPipelines.GUI_TEXTURED, dm.Q(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -855310);
      } finally {
         var1.pose().popMatrix();
      }

      hi.t var20 = this.E.w(this.p());
      if (!this.E.K() && var20.alpha() > 0.0) {
         this.p(
            var1,
            this.width / 2.0F,
            (float)(this.height * 0.48 + Math.clamp(this.height * 0.16, 30.0, 86.0) / 2.0 + 14.0),
            Math.min(1.65F, this.u.brandScale() + 0.15F),
            var20.alpha()
         );
      }

      this.r.q(var1, this.u, this.E, this.c, var2, var3, this.p(), this.u().W);
      this.j();
      var1.nextStratum();
      this.n(var1);
      if (!this.m.I() && this.f() && this.q(var2, var3)) {
         var1.requestCursor(CursorTypes.POINTING_HAND);
      }

      int var21 = this.m.I() || this.r.A() || !this.E.K() && !this.E.q() ? 0 : 1;
      super.extractRenderState(var1, var21 != 0 ? var2 : -1, var21 != 0 ? var3 : -1, var4);
      this.r.N(var1);
      if (this.r.A()) {
         var1.requestCursor(CursorTypes.RESIZE_ALL);
      } else if (!this.m.I() && this.r.w(var2, var3)) {
         var1.requestCursor(CursorTypes.POINTING_HAND);
      }

      this.m.R(var1, var2, var3);
   }

   private void n(GuiGraphicsExtractor var1) {
      if (this.q != null && this.q.visible) {
         UpdateRuntime.noticeShown();
         if (!this.I.compact()) {
            o6.b var2 = this.I.surface();
            ow.P(var1, var2.x(), var2.y(), var2.width(), var2.height(), 5.0F, -451208421);
            j_.y(var1, j_.x(UpdateRuntime.noticeTitle(), var2.width() - 16), var2.x() + 8, var2.y() + 9, -2171170, j_.w.Q);
            String var3 = UpdateRuntime.noticeDetail();
            j_.c(var1, j_.x(var3, var2.width() - 16), var2.x() + 8, var2.y() + 25, -6513508);
         }
      }
   }

   private void p(GuiGraphicsExtractor var1, float var2, float var3, float var4, double var5) {
      float var7 = var4 * 1.375F;
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(var2 - j_.U("Larp Client", Q) * var7 / 2.0F, var3 + 4.5F * var4 - 4.6F * var7);
         var1.pose().scale(var7, var7);
         j_.Q(var1, "Larp Client", 0, 0, oh.S(-1644826, var5), oh.S(-16777216, var5), 0.45F, Q);
      } finally {
         var1.pose().popMatrix();
      }
   }

   private void V(GuiGraphicsExtractor var1, hi.s var2) {
      o6.b var3 = this.u.title();
      float var4 = this.u.brandScale() * 1.375F;
      float var5 = (float)(var3.y() + var3.height() / 2.0 - 4.6F * var4 + var2.lift());
      if (!(var2.clip() <= var2.x()) && !this.b.isEmpty()) {
         for (rf.x var7 : this.b) {
            double var8 = var2.x() + var7.x() * var4;
            double var10 = Math.max(1.0F, var7.width() * var4);
            double var12 = Math.clamp((var8 + var10 - var2.clip()) / var10, 0.0, 1.0);
            double var14 = var2.alpha() * var12 * var12 * (3.0 - 2.0 * var12);
            if (!(var14 < 0.016)) {
               r(var1, var7.text(), (float)var8, var5, var4, var14);
            }
         }
      } else {
         r(var1, "Larp Client", (float)var2.x(), var5, var4, var2.alpha());
      }
   }

   private static void r(GuiGraphicsExtractor var0, String var1, float var2, float var3, float var4, double var5) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var2, var3);
         var0.pose().scale(var4, var4);
         j_.Q(var0, var1, 0, 0, oh.S(-1644826, var5), oh.S(-16777216, var5), 0.45F, Q);
      } finally {
         var0.pose().popMatrix();
      }
   }

   private void y() {
      this.b.clear();
      float var1 = 0.0F;
      int var2 = 0;

      while (var2 < "Larp Client".length()) {
         int var3 = "Larp Client".codePointAt(var2);
         String var4 = "Larp Client".substring(var2, var2 + Character.charCount(var3));
         var2 += var4.length();
         float var5 = j_.U(var4, Q);
         this.b.add(new rf.x(var4, var1, var5));
         var1 += var5;
      }
   }

   private void j(GuiGraphicsExtractor var1) {
      for (int var2 = 0; var2 < 12; var2++) {
         hi.l var3 = this.E.S(var2, this.u);
         if (!(var3.alpha() <= 0.0)) {
            var1.pose().pushMatrix();

            try {
               var1.pose().translate((float)var3.x(), (float)var3.y());
               var1.pose().rotate((float)var3.rotation());
               float var4 = (float)(var3.size() / 64.0);
               var1.pose().scale(var4, var4);
               var1.blit(RenderPipelines.GUI_TEXTURED, dm.Q(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, oh.i(-1513240, var3.alpha()));
            } finally {
               var1.pose().popMatrix();
            }
         }
      }
   }

   private fs u() {
      return LarpClient.y().v().E();
   }

   private void U(Screen var1) {
      if (var1 instanceof p0 var2) {
         var2.I();
      } else if (var1 instanceof ys var3) {
         var3.larpOpenMenuPopup();
      } else {
         r4.m("Menu", "Popup hook unavailable for {}; opening destination directly", var1.getClass().getSimpleName());
      }

      r4.g("Menu", "Opening {} with the shared {} ms popup", var1.getClass().getSimpleName(), 240);
      this.minecraft.gui.setScreen(var1);
   }

   private boolean p() {
      return this.u().o && this.u().S != xb.s.C && (Double)this.minecraft.options.panoramaSpeed().get() > 0.0;
   }

   public hi T() {
      this.E.F(var1x -> r7.r(this.minecraft.getWindow(), var1x));
      long var1 = System.nanoTime();
      int var3 = this.minecraft.gui.overlay() == null && this.minecraft.isWindowActive() ? 1 : 0;
      this.E.F(var1, (boolean)var3, this.p());
      this.f();
      if (!this.N && this.minecraft.gui.overlay() == null) {
         this.N = (boolean)1;
         r4.V("Menu", "Launch presentation: {}; Enter, Space or click to enter; another press skips motion", this.E.B());
      }

      return this.E;
   }

   private void f() {
      if (this.B != null) {
         this.B.visible = (boolean)(!this.E.K() && this.E.U(this.p()).alpha() > 0.0 ? 1 : 0);
         this.B.active = this.E.q();
         this.B.P(this.E.U(this.p()));

         for (int var1 = 0; var1 < this.w.size(); var1++) {
            x6 var2 = this.w.get(var1);
            hi.t var3 = this.E.L(var1);
            var2.P(var3);
            var2.visible = (boolean)(var3.alpha() > 0.0 ? 1 : 0);
            var2.active = this.E.K();
         }

         this.j();
         if (this.q != null) {
            int var4 = this.E.K() && UpdateRuntime.notice() ? 1 : 0;
            this.q.visible = this.q.active = (boolean)var4;
            this.f.visible = this.f.active = (boolean)(var4 != 0 && !this.I.compact() ? 1 : 0);
            this.q.setMessage(Component.literal(this.I.compact() ? UpdateRuntime.noticeTitle() : UpdateRuntime.noticeAction()));
         }

         if (this.L != this.E.B()) {
            hi.c var5 = this.L;
            this.L = this.E.B();
            this.setInitialFocus();
            if (this.E.K()) {
               r4.V("Menu", "Launch reveal settled; native navigation active");
            } else if (this.E.q() && var5 == hi.c.q) {
               r4.V("Menu", "Entrance replay reached the welcome pose; waiting for entry");
            }
         }
      }
   }

   private void j() {
      if (this.M != null) {
         int var1 = !this.E.K() || og.m(this.u).visible() && this.r.d() != 1.0 ? 0 : 1;
         this.M.visible = this.M.active = (boolean)var1;
         this.M.P(hi.t.REST);
         this.M.setMessage(Component.literal("Switch Microsoft account: " + this.minecraft.getUser().getName()));
      }
   }

   private void o() {
      this.T();
      if (this.E.m(this.p())) {
         this.clearFocus();
         this.f();
         r4.V("Menu", "Entry accepted; logo anticipation and side arc into the lockup, then the wordmark slides out from behind it while the menu staggers in");
      }
   }

   private boolean q(double var1, double var3) {
      o6.b var5 = this.u.logo();
      o6.b var6 = this.u.title();
      int var7 = Math.min(var5.x(), var6.x());
      int var8 = Math.min(var5.y(), var6.y());
      int var9 = Math.max(var5.x() + var5.width(), var6.x() + var6.width());
      int var10 = Math.max(var5.y() + var5.height(), var6.y() + var6.height());
      return var1 >= var7 && var1 < var9 && var3 >= var8 && var3 < var10;
   }

   private boolean f() {
      return this.E.K() && this.u().W;
   }

   private void l() {
      this.T();
      if (this.E.B(this.p())) {
         this.m.r();
         this.clearFocus();
         this.r.P();
         this.f();
         r4.V("Menu", "Logo clicked; rewinding the entrance to the welcome pose ({})", this.p() ? "animated" : "reduced motion");
      }
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.m.N(var1) || super.charTyped(var1);
   }

   public boolean keyPressed(KeyEvent var1) {
      if (this.m.b(var1)) {
         return true;
      }

      int var2 = r7.g(var1);
      if (this.E.e(var2)) {
         return true;
      }

      if (!this.E.K()) {
         if (var2 == 257 || var2 == 335 || var2 == 32) {
            this.minecraft.setLastInputType(InputType.KEYBOARD_TAB);
            this.E.a(var2);
            if (this.E.q()) {
               this.B.playDownSound(this.minecraft.getSoundManager());
               this.o();
            } else {
               this.E.L();
               this.f();
            }

            return true;
         }

         if (!this.E.q()) {
            this.E.L();
            this.f();
         }

         if (this.E.q()) {
            return true;
         }
      }

      return super.keyPressed(var1);
   }

   public boolean keyReleased(KeyEvent var1) {
      return this.E.C(r7.g(var1)) || super.keyReleased(var1);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.m.l(var1)) {
         return true;
      }

      if (!this.E.K()) {
         if (r7.y(var1) == 0) {
            this.t = (boolean)1;
            if (this.E.q()) {
               this.B.playDownSound(this.minecraft.getSoundManager());
               this.o();
            } else {
               this.E.L();
               this.f();
            }
         }

         return true;
      } else if (r7.y(var1) == 0 && this.r.S(var1.x(), var1.y())) {
         this.l = (boolean)1;
         this.m.r();
         this.clearFocus();
         return true;
      } else if (super.mouseClicked(var1, var2)) {
         return true;
      } else if (r7.y(var1) == 0 && this.f() && this.q(var1.x(), var1.y())) {
         this.t = (boolean)1;
         this.B.playDownSound(this.minecraft.getSoundManager());
         this.l();
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      if (r7.y(var1) == 0 && this.l) {
         this.l = (boolean)0;
         this.r.O();
         return true;
      } else if (this.m.I()) {
         return true;
      } else if (this.t && r7.y(var1) == 0) {
         this.t = (boolean)0;
         return true;
      } else {
         return !this.E.K() || super.mouseReleased(var1);
      }
   }

   public void mouseMoved(double var1, double var3) {
      this.r.L(var1, var3);
      super.mouseMoved(var1, var3);
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      this.r.L(var1.x(), var1.y());
      return this.r.A() || super.mouseDragged(var1, var2, var4);
   }

   public void added() {
      super.added();
      this.E.F(var1 -> r7.r(this.minecraft.getWindow(), var1));
      if (this.E.q()) {
         for (int var4 : new int[]{257, 335, 32}) {
            if (r7.r(this.minecraft.getWindow(), var4)) {
               this.E.a(var4);
            }
         }
      }
   }

   public void removed() {
      this.m.r();
      if (!this.E.q()) {
         this.E.L();
      }

      this.l = (boolean)0;
      this.r.L(!this.E.q());
      super.removed();
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      return this.m.Q(var1, var3, var7) || super.mouseScrolled(var1, var3, var5, var7);
   }

   public boolean isPauseScreen() {
      return false;
   }

   private record x(String text, float x, float width) {
   }
}
