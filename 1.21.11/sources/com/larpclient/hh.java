package com.larpclient;

import com.larpclient.update.UpdateLayout;
import com.larpclient.update.UpdateRuntime;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_10799;
import net.minecraft.class_11876;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_429;
import net.minecraft.class_4325;
import net.minecraft.class_437;
import net.minecraft.class_500;
import net.minecraft.class_526;
import net.minecraft.class_8015;

public final class hh extends class_437 implements c2 {
   private static final x.i e = new x.i();
   private static final yx.f n = new yx.f();
   private static final String u = "Larp Client";
   private static final uw.r Z = uw.r.m;
   private static final float z = 0.45F;
   private static final float T = 1.1F;
   private final x m = e.J();
   private final List<wn> q = new ArrayList<>();
   private final List<hh.d> C = new ArrayList<>();
   private hc f;
   private wn j;
   private wn k;
   private wn r;
   private wn H;
   private wn Y;
   private UpdateLayout.Notice d;
   private boolean i;
   private boolean O;
   private boolean D;
   private x.a x;
   private final ht v = new ht();
   private final u_ p = new u_(n.F());
   private final jc A = new jc(this);

   public hh() {
      super(class_2561.method_43470("Larp Client"));
   }

   public static boolean D() {
      LarpClient var0 = LarpClient.b();
      return var0 == null || var0.e() == null || var0.e().F().A;
   }

   protected void method_25426() {
      this.f = hc.calculate(this.field_22789, this.field_22790, (int)Math.ceil(uw.t("Larp Client", Z) * 1.375F));
      this.V();
      this.q.clear();
      if (!this.O().z) {
         this.m.a();
      }

      this.j = (wn)this.method_37063(new wn(class_2561.method_43471("menu.singleplayer"), wn.c.H, () -> this.G(new class_526(this)), this.f.navigation(0)));
      this.method_37063(new wn(class_2561.method_43471("menu.multiplayer"), wn.c.H, () -> this.G(new class_500(this)), this.f.navigation(1)));
      this.method_37063(new wn(class_2561.method_43471("menu.online"), wn.c.H, () -> this.G(new class_4325(this)), this.f.navigation(2)));
      this.method_37063(
         new wn(class_2561.method_43470("Minecraft Settings"), wn.c.i, () -> this.G(new class_429(this, this.field_22787.field_1690)), this.f.navigation(3))
      );
      this.method_37063(new wn(class_2561.method_43470("Client"), wn.c.i, () -> this.G(tm.y(this)), this.f.navigation(4)));
      this.method_37063(new wn(class_2561.method_43471("menu.quit"), wn.c.j, this.field_22787::method_1592, this.f.navigation(5)));
      yx.x var1 = u_.d(this.f);
      this.r = (wn)this.method_37063(
         new wn(
            class_2561.method_43470("Switch Microsoft account: " + this.field_22787.method_1548().method_1676()),
            wn.c.J,
            this.A::a,
            var1.visible() ? var1.name() : this.f.account()
         )
      );
      this.A.W(new ue.c(this.r.method_46426(), this.r.method_46427(), this.r.method_25368(), this.r.method_25364()));
      this.r.G = var1x -> {
         String var2 = uw.n(this.field_22787.method_1548().method_1676(), this.r.method_25368() - 32, uw.r.M);
         int var3x = uw.z(var2, uw.r.M);
         int var4x = this.r.method_46426() + (this.r.method_25368() - var3x - 14) / 2;
         uw.D(var1x, var2, var4x, this.r.method_46427() + 4, ei.R(-1513240, this.r.G()), uw.r.M);
         lx.N(var1x, lx.n(this.A.M() ? "chevron-up" : "chevron-down"), var4x + var3x + 5, this.r.method_46427() + 4, 9, ei.R(-5592406, this.r.G()));
         double var5 = this.r.s();
         wn.I(
            var1x,
            this.r.method_46426() + this.r.method_25368() / 2.0F,
            this.r.method_46427() + 17,
            (float)(20.0 + (var3x + 14 - 20) * var5),
            ei.R(ei.W(-11447983, -4342339, var5), this.r.G())
         );
      };

      for (class_364 var3 : this.method_25396()) {
         if (var3 instanceof wn var4 && var4 != this.r) {
            this.q.add(var4);
         }
      }

      this.k = (wn)this.method_37063(new wn(class_2561.method_43470("Press to enter"), wn.c.H, this::k, com.larpclient.x.g(this.field_22789, this.field_22790)));
      this.d = UpdateLayout.notice(this.field_22789, this.field_22790);
      this.H = (wn)this.method_37063(new wn(class_2561.method_43470("View update"), wn.c.i, () -> {
         UpdateRuntime.later();
         this.G(tk.Q(this, "Updates"));
      }, this.d.open()));
      this.Y = (wn)this.method_37063(new wn(class_2561.method_43470("Later"), wn.c.j, UpdateRuntime::later, this.d.later()));
      this.D();
      q8.v(
         "Menu",
         "Text navigation initialized at {}x{}; bordered labels, Realms under Multiplayer, direct Minecraft options",
         this.field_22789,
         this.field_22790
      );
   }

   protected void method_56131() {
      if (!this.m.W()) {
         if (this.m.F()) {
            this.method_48265(this.k);
         } else {
            this.method_48267();
         }
      } else {
         if (this.field_22787.method_48186().method_48183()) {
            this.method_48265(this.j);
         } else {
            this.method_48267();
         }
      }
   }

   public void method_25394(class_332 var1, int var2, int var3, float var4) {
      this.b();
      x.v var5 = this.m.p(this.f, this.e());
      if (this.O().l && this.e()) {
         this.o(var1);
      }

      var1.method_71048();
      x.q var6 = this.m.I(this.f, this.e());
      if (var6.alpha() > 0.0) {
         this.a(var1, var6);
         var1.method_71048();
      }

      var1.method_51448().pushMatrix();

      try {
         var1.method_51448().translate((float)var5.x(), (float)var5.y());
         var1.method_51448().rotate((float)var5.rotation());
         float var7 = (float)(var5.size() / 64.0);
         float var8 = (float)(var5.size() / 64.0 * var5.stretch());
         var1.method_51448().scale(var7, var8);

         for (int var9 = 0; var9 < 16; var9++) {
            double var10 = var9 * Math.PI / 8.0;
            var1.method_51448().pushMatrix();

            try {
               var1.method_51448().translate((float)(Math.cos(var10) * 1.1F / var7), (float)(Math.sin(var10) * 1.1F / var8));
               var1.method_25293(class_10799.field_56883, gc.z(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -16777216);
            } finally {
               var1.method_51448().popMatrix();
            }
         }

         var1.method_25293(class_10799.field_56883, gc.z(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, -855310);
      } finally {
         var1.method_51448().popMatrix();
      }

      x.h var20 = this.m.U(this.e());
      if (!this.m.W() && var20.alpha() > 0.0) {
         this.Z(
            var1,
            this.field_22789 / 2.0F,
            (float)(this.field_22790 * 0.48 + Math.clamp(this.field_22790 * 0.16, 30.0, 86.0) / 2.0 + 14.0),
            Math.min(1.65F, this.f.brandScale() + 0.15F),
            var20.alpha()
         );
      }

      this.p.W(var1, this.f, this.m, this.v, var2, var3, this.e(), this.O().z);
      this.a();
      var1.method_71048();
      this.q(var1);
      if (!this.A.M() && this.c() && this.A(var2, var3)) {
         var1.method_74037(class_11876.field_62455);
      }

      int var21 = this.A.M() || this.p.y() || !this.m.W() && !this.m.F() ? 0 : 1;
      super.method_25394(var1, var21 != 0 ? var2 : -1, var21 != 0 ? var3 : -1, var4);
      this.p.S(var1);
      if (this.p.y()) {
         var1.method_74037(class_11876.field_62458);
      } else if (!this.A.M() && this.p.h(var2, var3)) {
         var1.method_74037(class_11876.field_62455);
      }

      this.A.x(var1, var2, var3);
   }

   private void q(class_332 var1) {
      if (this.H != null && this.H.field_22764) {
         UpdateRuntime.noticeShown();
         if (!this.d.compact()) {
            ue.c var2 = this.d.surface();
            ws.r(var1, var2.x(), var2.y(), var2.width(), var2.height(), 5.0F, -451208421);
            uw.D(var1, uw.R(UpdateRuntime.noticeTitle(), var2.width() - 16), var2.x() + 8, var2.y() + 9, -2171170, uw.r.V);
            String var3 = UpdateRuntime.noticeDetail();
            uw.z(var1, uw.R(var3, var2.width() - 16), var2.x() + 8, var2.y() + 25, -6513508);
         }
      }
   }

   private void Z(class_332 var1, float var2, float var3, float var4, double var5) {
      float var7 = var4 * 1.375F;
      var1.method_51448().pushMatrix();

      try {
         var1.method_51448().translate(var2 - uw.t("Larp Client", Z) * var7 / 2.0F, var3 + 4.5F * var4 - 4.6F * var7);
         var1.method_51448().scale(var7, var7);
         uw.S(var1, "Larp Client", 0, 0, ei.R(-1644826, var5), ei.R(-16777216, var5), 0.45F, Z);
      } finally {
         var1.method_51448().popMatrix();
      }
   }

   private void a(class_332 var1, x.q var2) {
      ue.c var3 = this.f.title();
      float var4 = this.f.brandScale() * 1.375F;
      float var5 = (float)(var3.y() + var3.height() / 2.0 - 4.6F * var4 + var2.lift());
      if (!(var2.clip() <= var2.x()) && !this.C.isEmpty()) {
         for (hh.d var7 : this.C) {
            double var8 = var2.x() + var7.x() * var4;
            double var10 = Math.max(1.0F, var7.width() * var4);
            double var12 = Math.clamp((var8 + var10 - var2.clip()) / var10, 0.0, 1.0);
            double var14 = var2.alpha() * var12 * var12 * (3.0 - 2.0 * var12);
            if (!(var14 < 0.016)) {
               C(var1, var7.text(), (float)var8, var5, var4, var14);
            }
         }
      } else {
         C(var1, "Larp Client", (float)var2.x(), var5, var4, var2.alpha());
      }
   }

   private static void C(class_332 var0, String var1, float var2, float var3, float var4, double var5) {
      var0.method_51448().pushMatrix();

      try {
         var0.method_51448().translate(var2, var3);
         var0.method_51448().scale(var4, var4);
         uw.S(var0, var1, 0, 0, ei.R(-1644826, var5), ei.R(-16777216, var5), 0.45F, Z);
      } finally {
         var0.method_51448().popMatrix();
      }
   }

   private void V() {
      this.C.clear();
      float var1 = 0.0F;
      int var2 = 0;

      while (var2 < "Larp Client".length()) {
         int var3 = "Larp Client".codePointAt(var2);
         String var4 = "Larp Client".substring(var2, var2 + Character.charCount(var3));
         var2 += var4.length();
         float var5 = uw.t(var4, Z);
         this.C.add(new hh.d(var4, var1, var5));
         var1 += var5;
      }
   }

   private void o(class_332 var1) {
      for (int var2 = 0; var2 < 12; var2++) {
         x.d var3 = this.m.l(var2, this.f);
         if (!(var3.alpha() <= 0.0)) {
            var1.method_51448().pushMatrix();

            try {
               var1.method_51448().translate((float)var3.x(), (float)var3.y());
               var1.method_51448().rotate((float)var3.rotation());
               float var4 = (float)(var3.size() / 64.0);
               var1.method_51448().scale(var4, var4);
               var1.method_25293(class_10799.field_56883, gc.z(), -32, -32, 0.0F, 0.0F, 64, 64, 64, 64, 64, 64, ei.D(-1513240, var3.alpha()));
            } finally {
               var1.method_51448().popMatrix();
            }
         }
      }
   }

   private y0 O() {
      return LarpClient.b().e().F();
   }

   private void G(class_437 var1) {
      if (var1 instanceof tk var2) {
         var2.V();
      } else if (var1 instanceof qx var3) {
         var3.larpOpenMenuPopup();
      } else {
         q8.L("Menu", "Popup hook unavailable for {}; opening destination directly", var1.getClass().getSimpleName());
      }

      q8.v("Menu", "Opening {} with the shared {} ms popup", var1.getClass().getSimpleName(), 240);
      this.field_22787.method_1507(var1);
   }

   private boolean e() {
      return this.O().k && this.O().U != td.r.y && (Double)this.field_22787.field_1690.method_45581().method_41753() > 0.0;
   }

   public x b() {
      this.m.K(var1x -> s2.G(this.field_22787.method_22683(), var1x));
      long var1 = System.nanoTime();
      int var3 = this.field_22787.method_18506() == null && this.field_22787.method_1569() ? 1 : 0;
      this.m.h(var1, (boolean)var3, this.e());
      this.D();
      if (!this.D && this.field_22787.method_18506() == null) {
         this.D = (boolean)1;
         q8.G("Menu", "Launch presentation: {}; Enter, Space or click to enter; another press skips motion", this.m.l());
      }

      return this.m;
   }

   private void D() {
      if (this.k != null) {
         this.k.field_22764 = (boolean)(!this.m.W() && this.m.A(this.e()).alpha() > 0.0 ? 1 : 0);
         this.k.field_22763 = this.m.F();
         this.k.d(this.m.A(this.e()));

         for (int var1 = 0; var1 < this.q.size(); var1++) {
            wn var2 = this.q.get(var1);
            x.h var3 = this.m.N(var1);
            var2.d(var3);
            var2.field_22764 = (boolean)(var3.alpha() > 0.0 ? 1 : 0);
            var2.field_22763 = this.m.W();
         }

         this.a();
         if (this.H != null) {
            int var4 = this.m.W() && UpdateRuntime.notice() ? 1 : 0;
            this.H.field_22764 = this.H.field_22763 = (boolean)var4;
            this.Y.field_22764 = this.Y.field_22763 = (boolean)(var4 != 0 && !this.d.compact() ? 1 : 0);
            this.H.method_25355(class_2561.method_43470(this.d.compact() ? UpdateRuntime.noticeTitle() : UpdateRuntime.noticeAction()));
         }

         if (this.x != this.m.l()) {
            x.a var5 = this.x;
            this.x = this.m.l();
            this.method_56131();
            if (this.m.W()) {
               q8.G("Menu", "Launch reveal settled; native navigation active");
            } else if (this.m.F() && var5 == x.a.C) {
               q8.G("Menu", "Entrance replay reached the welcome pose; waiting for entry");
            }
         }
      }
   }

   private void a() {
      if (this.r != null) {
         int var1 = !this.m.W() || yx.O(this.f).visible() && this.p.l() != 1.0 ? 0 : 1;
         this.r.field_22764 = this.r.field_22763 = (boolean)var1;
         this.r.d(x.h.REST);
         this.r.method_25355(class_2561.method_43470("Switch Microsoft account: " + this.field_22787.method_1548().method_1676()));
      }
   }

   private void k() {
      this.b();
      if (this.m.z(this.e())) {
         this.method_48267();
         this.D();
         q8.G("Menu", "Entry accepted; logo anticipation and side arc into the lockup, then the wordmark slides out from behind it while the menu staggers in");
      }
   }

   private boolean A(double var1, double var3) {
      ue.c var5 = this.f.logo();
      ue.c var6 = this.f.title();
      int var7 = Math.min(var5.x(), var6.x());
      int var8 = Math.min(var5.y(), var6.y());
      int var9 = Math.max(var5.x() + var5.width(), var6.x() + var6.width());
      int var10 = Math.max(var5.y() + var5.height(), var6.y() + var6.height());
      return var1 >= var7 && var1 < var9 && var3 >= var8 && var3 < var10;
   }

   private boolean c() {
      return this.m.W() && this.O().z;
   }

   private void f() {
      this.b();
      if (this.m.e(this.e())) {
         this.A.Q();
         this.method_48267();
         this.p.k();
         this.D();
         q8.G("Menu", "Logo clicked; rewinding the entrance to the welcome pose ({})", this.e() ? "animated" : "reduced motion");
      }
   }

   public boolean method_25400(class_11905 var1) {
      return this.A.L(var1) || super.method_25400(var1);
   }

   public boolean method_25404(class_11908 var1) {
      if (this.A.L(var1)) {
         return true;
      }

      int var2 = s2.i(var1);
      if (this.m.E(var2)) {
         return true;
      }

      if (!this.m.W()) {
         if (var2 == 257 || var2 == 335 || var2 == 32) {
            this.field_22787.method_48185(class_8015.field_41780);
            this.m.c(var2);
            if (this.m.F()) {
               this.k.method_25354(this.field_22787.method_1483());
               this.k();
            } else {
               this.m.j();
               this.D();
            }

            return true;
         }

         if (!this.m.F()) {
            this.m.j();
            this.D();
         }

         if (this.m.F()) {
            return true;
         }
      }

      return super.method_25404(var1);
   }

   public boolean method_16803(class_11908 var1) {
      return this.m.p(s2.i(var1)) || super.method_16803(var1);
   }

   public boolean method_25402(class_11909 var1, boolean var2) {
      if (this.A.C(var1)) {
         return true;
      }

      if (!this.m.W()) {
         if (s2.m(var1) == 0) {
            this.i = (boolean)1;
            if (this.m.F()) {
               this.k.method_25354(this.field_22787.method_1483());
               this.k();
            } else {
               this.m.j();
               this.D();
            }
         }

         return true;
      } else if (s2.m(var1) == 0 && this.p.P(var1.comp_4798(), var1.comp_4799())) {
         this.O = (boolean)1;
         this.A.Q();
         this.method_48267();
         return true;
      } else if (super.method_25402(var1, var2)) {
         return true;
      } else if (s2.m(var1) == 0 && this.c() && this.A(var1.comp_4798(), var1.comp_4799())) {
         this.i = (boolean)1;
         this.k.method_25354(this.field_22787.method_1483());
         this.f();
         return true;
      } else {
         return false;
      }
   }

   public boolean method_25406(class_11909 var1) {
      if (s2.m(var1) == 0 && this.O) {
         this.O = (boolean)0;
         this.p.w();
         return true;
      } else if (this.A.M()) {
         return true;
      } else if (this.i && s2.m(var1) == 0) {
         this.i = (boolean)0;
         return true;
      } else {
         return !this.m.W() || super.method_25406(var1);
      }
   }

   public void method_16014(double var1, double var3) {
      this.p.P(var1, var3);
      super.method_16014(var1, var3);
   }

   public boolean method_25403(class_11909 var1, double var2, double var4) {
      this.p.P(var1.comp_4798(), var1.comp_4799());
      return this.p.y() || super.method_25403(var1, var2, var4);
   }

   public void method_49589() {
      super.method_49589();
      this.m.K(var1 -> s2.G(this.field_22787.method_22683(), var1));
      if (this.m.F()) {
         for (int var4 : new int[]{257, 335, 32}) {
            if (s2.G(this.field_22787.method_22683(), var4)) {
               this.m.c(var4);
            }
         }
      }
   }

   public void method_25432() {
      this.A.Q();
      if (!this.m.F()) {
         this.m.j();
      }

      this.O = (boolean)0;
      this.p.k(!this.m.F());
      super.method_25432();
   }

   public boolean method_25422() {
      return false;
   }

   public boolean method_25401(double var1, double var3, double var5, double var7) {
      return this.A.l(var1, var3, var7) || super.method_25401(var1, var3, var5, var7);
   }

   public boolean method_25421() {
      return false;
   }

   private record d(String text, float x, float width) {
   }
}
