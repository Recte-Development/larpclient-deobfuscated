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

public final class de extends Screen implements d7 {
   private static final ah.z Q = new ah.z();
   private static final o5.b l = new o5.b();
   private static final String t = "Larp Client";
   private static final ic.b c = ic.b.D;
   private static final float o = 0.45F;
   private static final float Y = 1.1F;
   private final ah v = Q.Q();
   private final List<vr> y = new ArrayList<>();
   private final List<de.o> R = new ArrayList<>();
   private qa D;
   private vr i;
   private vr C;
   private vr H;
   private vr u;
   private vr X;
   private UpdateLayout.Notice V;
   private boolean T;
   private boolean G;
   private boolean q;
   private ah.c L;
   private final bd d = new bd();
   private final dl m = new dl(l.g());
   private final pq B = new pq(this);

   public de() {
      super(Component.literal("Larp Client"));
   }

   public static boolean B() {
      LarpClient var0 = LarpClient.N();
      return var0 == null || var0.h() == null || var0.h().k().M;
   }

   protected void init() {
      this.D = qa.calculate(this.width, this.height, (int)Math.ceil(ic.Z("Larp Client", c) * 1.375F));
      this.D();
      this.y.clear();
      if (!this.G().I) {
         this.v.R();
      }

      this.i = (vr)this.addRenderableWidget(
         new vr(Component.translatable("menu.singleplayer"), vr.h.Y, () -> this.x(new SelectWorldScreen(this)), this.D.navigation(0))
      );
      this.addRenderableWidget(new vr(Component.translatable("menu.multiplayer"), vr.h.Y, () -> this.x(new JoinMultiplayerScreen(this)), this.D.navigation(1)));
      this.addRenderableWidget(new vr(Component.translatable("menu.online"), vr.h.Y, () -> this.x(new RealmsMainScreen(this)), this.D.navigation(2)));
      this.addRenderableWidget(
         new vr(Component.literal("Minecraft Settings"), vr.h.M, () -> this.x(new OptionsScreen(this, this.minecraft.options)), this.D.navigation(3))
      );
      this.addRenderableWidget(new vr(Component.literal("Client"), vr.h.M, () -> this.x(fj.I(this)), this.D.navigation(4)));
      this.addRenderableWidget(new vr(Component.translatable("menu.quit"), vr.h.h, this.minecraft::stop, this.D.navigation(5)));
      o5.t var1 = dl.I(this.D);
      this.H = (vr)this.addRenderableWidget(
         new vr(
            Component.literal("Switch Microsoft account: " + this.minecraft.getUser().getName()),
            vr.h.Q,
            this.B::t,
            var1.visible() ? var1.name() : this.D.account()
         )
      );
      this.B.Z(new _c.f(this.H.getX(), this.H.getY(), this.H.getWidth(), this.H.getHeight()));
      this.H.A = var1x -> {
         String var2 = ic.P(this.minecraft.getUser().getName(), this.H.getWidth() - 32, ic.b.I);
         int var3x = ic.z(var2, ic.b.I);
         int var4x = this.H.getX() + (this.H.getWidth() - var3x - 14) / 2;
         ic.Y(var1x, var2, var4x, this.H.getY() + 4, vv.O(-1513240, this.H.T()), ic.b.I);
         wz.D(var1x, wz.M(this.B.M() ? "chevron-up" : "chevron-down"), var4x + var3x + 5, this.H.getY() + 4, 9, vv.O(-5592406, this.H.T()));
         double var5 = this.H.M();
         vr.L(
            var1x,
            this.H.getX() + this.H.getWidth() / 2.0F,
            this.H.getY() + 17,
            (float)(20.0 + (var3x + 14 - 20) * var5),
            vv.O(vv.Q(-11447983, -4342339, var5), this.H.T())
         );
      };

      for (GuiEventListener var3 : this.children()) {
         if (var3 instanceof vr var4 && var4 != this.H) {
            this.y.add(var4);
         }
      }

      this.C = (vr)this.addRenderableWidget(new vr(Component.literal("Press to enter"), vr.h.Y, this::Z, ah.W(this.width, this.height)));
      this.V = UpdateLayout.notice(this.width, this.height);
      this.u = (vr)this.addRenderableWidget(new vr(Component.literal("View update"), vr.h.M, () -> {
         UpdateRuntime.later();
         this.x(f8.i(this, "Updates"));
      }, this.V.open()));
      this.X = (vr)this.addRenderableWidget(new vr(Component.literal("Later"), vr.h.h, UpdateRuntime::later, this.V.later()));
      this.K();
      _q.c("Menu", "Text navigation initialized at {}x{}; bordered labels, Realms under Multiplayer, direct Minecraft options", this.width, this.height);
   }

   protected void setInitialFocus() {
      if (!this.v.F()) {
         if (this.v.y()) {
            this.setInitialFocus(this.C);
         } else {
            this.clearFocus();
         }
      } else {
         if (this.minecraft.getLastInputType().isKeyboard()) {
            this.setInitialFocus(this.i);
         } else {
            this.clearFocus();
         }
      }
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.X();
      ah.u var5 = this.v.v(this.D, this.u());
      if (this.G().t && this.u()) {
         this.D(var1);
      }

      var1.nextStratum();
      ah.m var6 = this.v.G(this.D, this.u());
      if (var6.alpha() > 0.0) {
         this.C(var1, var6);
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
               var1.blit(RenderPipelines.GUI_TEXTURED, h.b(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -16777216);
            } finally {
               var1.pose().popMatrix();
            }
         }

         var1.blit(RenderPipelines.GUI_TEXTURED, h.b(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -855310);
      } finally {
         var1.pose().popMatrix();
      }

      ah.j var20 = this.v.t(this.u());
      if (!this.v.F() && var20.alpha() > 0.0) {
         this.a(
            var1,
            this.width / 2.0F,
            (float)(this.height * 0.48 + Math.clamp(this.height * 0.16, 30.0, 86.0) / 2.0 + 14.0),
            Math.min(1.65F, this.D.brandScale() + 0.15F),
            var20.alpha()
         );
      }

      this.m.D(var1, this.D, this.v, this.d, var2, var3, this.u(), this.G().I);
      this.g();
      var1.nextStratum();
      this.V(var1);
      if (!this.B.M() && this.Z() && this.l(var2, var3)) {
         var1.requestCursor(CursorTypes.POINTING_HAND);
      }

      int var21 = this.B.M() || this.m.M() || !this.v.F() && !this.v.y() ? 0 : 1;
      super.extractRenderState(var1, var21 != 0 ? var2 : -1, var21 != 0 ? var3 : -1, var4);
      this.m.I(var1);
      if (this.m.M()) {
         var1.requestCursor(CursorTypes.RESIZE_ALL);
      } else if (!this.B.M() && this.m.O(var2, var3)) {
         var1.requestCursor(CursorTypes.POINTING_HAND);
      }

      this.B.c(var1, var2, var3);
   }

   private void V(GuiGraphicsExtractor var1) {
      if (this.u != null && this.u.visible) {
         UpdateRuntime.noticeShown();
         if (!this.V.compact()) {
            _c.f var2 = this.V.surface();
            w1.a(var1, var2.x(), var2.y(), var2.width(), var2.height(), 5.0F, -451208421);
            ic.Y(var1, ic.W(UpdateRuntime.noticeTitle(), var2.width() - 16), var2.x() + 8, var2.y() + 9, -2171170, ic.b.R);
            String var3 = UpdateRuntime.noticeDetail();
            ic.H(var1, ic.W(var3, var2.width() - 16), var2.x() + 8, var2.y() + 25, -6513508);
         }
      }
   }

   private void a(GuiGraphicsExtractor var1, float var2, float var3, float var4, double var5) {
      float var7 = var4 * 1.375F;
      var1.pose().pushMatrix();

      try {
         var1.pose().translate(var2 - ic.Z("Larp Client", c) * var7 / 2.0F, var3 + 4.5F * var4 - 4.6F * var7);
         var1.pose().scale(var7, var7);
         ic.A(var1, "Larp Client", 0, 0, vv.O(-1644826, var5), vv.O(-16777216, var5), 0.45F, c);
      } finally {
         var1.pose().popMatrix();
      }
   }

   private void C(GuiGraphicsExtractor var1, ah.m var2) {
      _c.f var3 = this.D.title();
      float var4 = this.D.brandScale() * 1.375F;
      float var5 = (float)(var3.y() + var3.height() / 2.0 - 4.6F * var4 + var2.lift());
      if (!(var2.clip() <= var2.x()) && !this.R.isEmpty()) {
         for (de.o var7 : this.R) {
            double var8 = var2.x() + var7.x() * var4;
            double var10 = Math.max(1.0F, var7.width() * var4);
            double var12 = Math.clamp((var8 + var10 - var2.clip()) / var10, 0.0, 1.0);
            double var14 = var2.alpha() * var12 * var12 * (3.0 - 2.0 * var12);
            if (!(var14 < 0.016)) {
               h(var1, var7.text(), (float)var8, var5, var4, var14);
            }
         }
      } else {
         h(var1, "Larp Client", (float)var2.x(), var5, var4, var2.alpha());
      }
   }

   private static void h(GuiGraphicsExtractor var0, String var1, float var2, float var3, float var4, double var5) {
      var0.pose().pushMatrix();

      try {
         var0.pose().translate(var2, var3);
         var0.pose().scale(var4, var4);
         ic.A(var0, var1, 0, 0, vv.O(-1644826, var5), vv.O(-16777216, var5), 0.45F, c);
      } finally {
         var0.pose().popMatrix();
      }
   }

   private void D() {
      this.R.clear();
      float var1 = 0.0F;
      int var2 = 0;

      while (var2 < "Larp Client".length()) {
         int var3 = "Larp Client".codePointAt(var2);
         String var4 = "Larp Client".substring(var2, var2 + Character.charCount(var3));
         var2 += var4.length();
         float var5 = ic.Z(var4, c);
         this.R.add(new de.o(var4, var1, var5));
         var1 += var5;
      }
   }

   private void D(GuiGraphicsExtractor var1) {
      for (int var2 = 0; var2 < 12; var2++) {
         ah.r var3 = this.v.m(var2, this.D);
         if (!(var3.alpha() <= 0.0)) {
            var1.pose().pushMatrix();

            try {
               var1.pose().translate((float)var3.x(), (float)var3.y());
               var1.pose().rotate((float)var3.rotation());
               float var4 = (float)(var3.size() / 64.0);
               var1.pose().scale(var4, var4);
               var1.blit(RenderPipelines.GUI_TEXTURED, h.b(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, vv.N(-1513240, var3.alpha()));
            } finally {
               var1.pose().popMatrix();
            }
         }
      }
   }

   private i3 G() {
      return LarpClient.N().h().k();
   }

   private void x(Screen var1) {
      if (var1 instanceof f8 var2) {
         var2.x();
      } else if (var1 instanceof sd var3) {
         var3.larpOpenMenuPopup();
      } else {
         _q.R("Menu", "Popup hook unavailable for {}; opening destination directly", var1.getClass().getSimpleName());
      }

      _q.c("Menu", "Opening {} with the shared {} ms popup", var1.getClass().getSimpleName(), 240);
      this.minecraft.gui.setScreen(var1);
   }

   private boolean u() {
      return this.G().w && this.G().Jw != ng.c.T && (Double)this.minecraft.options.panoramaSpeed().get() > 0.0;
   }

   public ah X() {
      this.v.E(var1x -> j2.U(this.minecraft.getWindow(), var1x));
      long var1 = System.nanoTime();
      int var3 = this.minecraft.gui.overlay() == null && this.minecraft.isWindowActive() ? 1 : 0;
      this.v.D(var1, (boolean)var3, this.u());
      this.K();
      if (!this.q && this.minecraft.gui.overlay() == null) {
         this.q = (boolean)1;
         _q.z("Menu", "Launch presentation: {}; Enter, Space or click to enter; another press skips motion", this.v.c());
      }

      return this.v;
   }

   private void K() {
      if (this.C != null) {
         this.C.visible = (boolean)(!this.v.F() && this.v.l(this.u()).alpha() > 0.0 ? 1 : 0);
         this.C.active = this.v.y();
         this.C.E(this.v.l(this.u()));

         for (int var1 = 0; var1 < this.y.size(); var1++) {
            vr var2 = this.y.get(var1);
            ah.j var3 = this.v.E(var1);
            var2.E(var3);
            var2.visible = (boolean)(var3.alpha() > 0.0 ? 1 : 0);
            var2.active = this.v.F();
         }

         this.g();
         if (this.u != null) {
            int var4 = this.v.F() && UpdateRuntime.notice() ? 1 : 0;
            this.u.visible = this.u.active = (boolean)var4;
            this.X.visible = this.X.active = (boolean)(var4 != 0 && !this.V.compact() ? 1 : 0);
            this.u.setMessage(Component.literal(this.V.compact() ? UpdateRuntime.noticeTitle() : UpdateRuntime.noticeAction()));
         }

         if (this.L != this.v.c()) {
            ah.c var5 = this.L;
            this.L = this.v.c();
            this.setInitialFocus();
            if (this.v.F()) {
               _q.z("Menu", "Launch reveal settled; native navigation active");
            } else if (this.v.y() && var5 == ah.c.K) {
               _q.z("Menu", "Entrance replay reached the welcome pose; waiting for entry");
            }
         }
      }
   }

   private void g() {
      if (this.H != null) {
         int var1 = !this.v.F() || o5.n(this.D).visible() && this.m.x() != 1.0 ? 0 : 1;
         this.H.visible = this.H.active = (boolean)var1;
         this.H.E(ah.j.REST);
         this.H.setMessage(Component.literal("Switch Microsoft account: " + this.minecraft.getUser().getName()));
      }
   }

   private void Z() {
      this.X();
      if (this.v.i(this.u())) {
         this.clearFocus();
         this.K();
         _q.z("Menu", "Entry accepted; logo anticipation and side arc into the lockup, then the wordmark slides out from behind it while the menu staggers in");
      }
   }

   private boolean l(double var1, double var3) {
      _c.f var5 = this.D.logo();
      _c.f var6 = this.D.title();
      int var7 = Math.min(var5.x(), var6.x());
      int var8 = Math.min(var5.y(), var6.y());
      int var9 = Math.max(var5.x() + var5.width(), var6.x() + var6.width());
      int var10 = Math.max(var5.y() + var5.height(), var6.y() + var6.height());
      return var1 >= var7 && var1 < var9 && var3 >= var8 && var3 < var10;
   }

   private boolean Z() {
      return this.v.F() && this.G().I;
   }

   private void m() {
      this.X();
      if (this.v.e(this.u())) {
         this.B.U();
         this.clearFocus();
         this.m.Q();
         this.K();
         _q.z("Menu", "Logo clicked; rewinding the entrance to the welcome pose ({})", this.u() ? "animated" : "reduced motion");
      }
   }

   public boolean charTyped(CharacterEvent var1) {
      return this.B.J(var1) || super.charTyped(var1);
   }

   public boolean keyPressed(KeyEvent var1) {
      if (this.B.d(var1)) {
         return true;
      }

      int var2 = j2.P(var1);
      if (this.v.y(var2)) {
         return true;
      }

      if (!this.v.F()) {
         if (var2 == 257 || var2 == 335 || var2 == 32) {
            this.minecraft.setLastInputType(InputType.KEYBOARD_TAB);
            this.v.l(var2);
            if (this.v.y()) {
               this.C.playDownSound(this.minecraft.getSoundManager());
               this.Z();
            } else {
               this.v.N();
               this.K();
            }

            return true;
         }

         if (!this.v.y()) {
            this.v.N();
            this.K();
         }

         if (this.v.y()) {
            return true;
         }
      }

      return super.keyPressed(var1);
   }

   public boolean keyReleased(KeyEvent var1) {
      return this.v.u(j2.P(var1)) || super.keyReleased(var1);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      if (this.B.e(var1)) {
         return true;
      }

      if (!this.v.F()) {
         if (j2.L(var1) == 0) {
            this.T = (boolean)1;
            if (this.v.y()) {
               this.C.playDownSound(this.minecraft.getSoundManager());
               this.Z();
            } else {
               this.v.N();
               this.K();
            }
         }

         return true;
      } else if (j2.L(var1) == 0 && this.m.I(var1.x(), var1.y())) {
         this.G = (boolean)1;
         this.B.U();
         this.clearFocus();
         return true;
      } else if (super.mouseClicked(var1, var2)) {
         return true;
      } else if (j2.L(var1) == 0 && this.Z() && this.l(var1.x(), var1.y())) {
         this.T = (boolean)1;
         this.C.playDownSound(this.minecraft.getSoundManager());
         this.m();
         return true;
      } else {
         return false;
      }
   }

   public boolean mouseReleased(MouseButtonEvent var1) {
      if (j2.L(var1) == 0 && this.G) {
         this.G = (boolean)0;
         this.m.A();
         return true;
      } else if (this.B.M()) {
         return true;
      } else if (this.T && j2.L(var1) == 0) {
         this.T = (boolean)0;
         return true;
      } else {
         return !this.v.F() || super.mouseReleased(var1);
      }
   }

   public void mouseMoved(double var1, double var3) {
      this.m.N(var1, var3);
      super.mouseMoved(var1, var3);
   }

   public boolean mouseDragged(MouseButtonEvent var1, double var2, double var4) {
      this.m.N(var1.x(), var1.y());
      return this.m.M() || super.mouseDragged(var1, var2, var4);
   }

   public void added() {
      super.added();
      this.v.E(var1 -> j2.U(this.minecraft.getWindow(), var1));
      if (this.v.y()) {
         for (int var4 : new int[]{257, 335, 32}) {
            if (j2.U(this.minecraft.getWindow(), var4)) {
               this.v.l(var4);
            }
         }
      }
   }

   public void removed() {
      this.B.U();
      if (!this.v.y()) {
         this.v.N();
      }

      this.G = (boolean)0;
      this.m.A(!this.v.y());
      super.removed();
   }

   public boolean shouldCloseOnEsc() {
      return false;
   }

   public boolean mouseScrolled(double var1, double var3, double var5, double var7) {
      return this.B.x(var1, var3, var7) || super.mouseScrolled(var1, var3, var5, var7);
   }

   public boolean isPauseScreen() {
      return false;
   }

   private record o(String text, float x, float width) {
   }
}
