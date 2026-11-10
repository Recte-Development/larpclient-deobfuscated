package com.larpclient.module;

import com.larpclient.cb;
import com.larpclient.cw;
import com.larpclient.ef;
import com.larpclient.g6;
import com.larpclient.hr;
import com.larpclient.j_;
import com.larpclient.q1;
import com.larpclient.q8;
import com.larpclient.qv;
import com.larpclient.t_;
import com.larpclient.wp;
import com.larpclient.yr;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_10017;
import net.minecraft.class_10042;
import net.minecraft.class_1306;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_746;
import org.joml.Matrix4f;

public final class PlayerModelModule extends Module {
   private static PlayerModelModule c;
   private final li n = this.addSetting(
      O()
         ? new li("Character", "Which character replaces your body", "Tung Tung Tung Sahur", "Tung Tung Tung Sahur", "Banana")
         : new li("Character", "Which character replaces your body", "Tung Tung Tung Sahur", "Tung Tung Tung Sahur")
   );
   private final lo J = this.addSetting(new lo("Scale", "Visual character size", 1.0, 0.5, 2.0, 0.05));
   private final l3 s = this.addSetting(new l3("Show Timing", "Show CPU model submission timing", false));
   private Object M;
   private Object k;
   private long S;
   private long i;
   private long z;
   private boolean v;
   private class_10042 Y;
   private int t = -1;
   private final Matrix4f H = new Matrix4f();
   private float x;
   private boolean G;
   private g6 g;
   private ef u;
   private final Matrix4f A = new Matrix4f();
   private final float[] E = new float[cb.w];
   private boolean B;

   public static boolean O() {
      return PlayerModelModule.class.getResource("/assets/larp-client/models/player/banana/mesh.bin") != null;
   }

   public PlayerModelModule() {
      super("Player Model", "Replace your rendered player with a character", Category.VISUALS, -1);
      c = this;
      this.n.a(var1 -> {
         this.B();
         q8.v("PlayerModel", "Character changed to {}", var1);
      });
   }

   public static PlayerModelModule h() {
      return c;
   }

   public List<lb<?>> f() {
      return List.of(this.n, this.J);
   }

   public List<lb<?>> g() {
      return List.of(this.s);
   }

   private boolean q() {
      return "Banana".equals(this.n.getValue());
   }

   public boolean X() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.t != t_.g && (this.q() ? yr.m() && this.u != null : j_.x());
   }

   public boolean g(class_10017 var1) {
      return !this.isEnabled() ? false : this.X() && this.Y == var1;
   }

   public void S(class_10042 var1) {
      if (this.isEnabled()) {
         this.Y = var1;
      }
   }

   @Override
   public void onEnable() {
      this.B();

      try {
         this.h();
      } catch (Exception var2) {
         q8.u("PlayerModel", "Mesh stage failed; retaining native body", var2);
         this.setEnabled(false);
      }
   }

   private void h() {
      if (this.q()) {
         yr.g();
         if (this.u == null || this.u.R() != yr.Y()) {
            this.u = new ef(yr.Y());
         }

         q8.v("PlayerModel", "Banana ready: {} triangles, {} vertices, scale={}", this.u.R().triangles(), this.u.R().vertices(), this.J.getValue());
      } else {
         j_.H();
         if (this.g == null || this.g.a() != j_.S()) {
            this.g = new g6(j_.S());
         }

         q8.v("PlayerModel", "Sahur ready: walk rig, {} triangles, scale={}", this.g.Y().triangles(), this.J.getValue());
      }
   }

   @Override
   public void onDisable() {
      this.B();
   }

   public void B() {
      this.v = (boolean)0;
      this.Y = null;
      this.M = this.k = null;
      this.z = this.i = 0L;
   }

   public void X() {
      this.B();
      this.t = t_.g;
   }

   public void w(float var1, class_243 var2) {
      if (this.isEnabled()) {
         this.v = (boolean)0;
         if (this.isRuntimeActive() && this.isInGame() && this.t != t_.g) {
            if (this.q() ? !yr.m() || this.u == null || this.u.R() != yr.Y() : !j_.x() || this.g == null || this.g.a() != j_.S()) {
               this.h();
            }

            if (this.X() && this.isInGame()) {
               class_746 var3 = mc.field_1724;
               if (this.M != var3 || this.k != mc.field_1687) {
                  class_10042 var4 = this.Y;
                  this.B();
                  this.Y = var4;
                  this.M = var3;
                  this.k = mc.field_1687;
               }

               if (!mc.field_1690.method_31044().method_31034() || wp.d()) {
                  if (this.Y != null) {
                     this.x = this.J.getValue().floatValue();
                     this.G = var3.method_18276();
                     if (this.q()) {
                        this.u(var3, var1, var2);
                        this.v = (boolean)1;
                     } else {
                        this.g.f(this.Y.field_53450, this.Y.field_53451);
                        hr.Y(
                           this.H,
                           j_.S(),
                           (float)(class_3532.method_16436(var1, var3.field_6038, var3.method_23317()) - var2.field_1352),
                           (float)(class_3532.method_16436(var1, var3.field_5971, var3.method_23318()) - var2.field_1351),
                           (float)(class_3532.method_16436(var1, var3.field_5989, var3.method_23321()) - var2.field_1350),
                           (float)Math.toRadians(Feature3DUIModule.n(var3, class_3532.method_17821(var1, var3.field_6220, var3.field_6283), var1)),
                           this.x,
                           this.G
                        );
                        this.v = (boolean)1;
                     }
                  }
               }
            }
         }
      }
   }

   private void u(class_746 var1, float var2, class_243 var3) {
      long var4 = System.nanoTime();
      float[] var6 = null;
      float var7 = 0.0F;
      if (this.Y instanceof q1 var8 && var8.larp$emoteWeight() > 0.0F) {
         var6 = var8.larp$emoteSample();
         var7 = var8.larp$emoteWeight();
      }

      ef.U(this.E, this.Y.field_53450, this.Y.field_53451, this.Y.field_53328, this.Y.field_53447, this.Y.field_53448, this.G);
      if (var6 != null) {
         ef.K(this.E, var6, var7);
      }

      this.u.N(this.E, 1.0F - var7);
      if (var6 != null && !this.B) {
         q8.v("PlayerModel", "Banana emote started");
      }

      this.B = (boolean)(var6 != null ? 1 : 0);
      float var10 = var6 != null ? this.Y.field_53446 : Feature3DUIModule.n(var1, class_3532.method_17821(var2, var1.field_6220, var1.field_6283), var2);
      qv.m(
         this.A,
         this.u.R(),
         (float)(class_3532.method_16436(var2, var1.field_6038, var1.method_23317()) - var3.field_1352),
         (float)(class_3532.method_16436(var2, var1.field_5971, var1.method_23318()) - var3.field_1351),
         (float)(class_3532.method_16436(var2, var1.field_5989, var1.method_23321()) - var3.field_1350),
         (float)Math.toRadians(var10),
         this.x,
         var6,
         var7
      );
      if (this.s.getValue()) {
         this.i = this.i + (System.nanoTime() - var4);
      }
   }

   public void J(class_4587 var1, cw var2) {
      if (this.isEnabled()) {
         if (this.v && this.X() && this.M == mc.field_1724 && this.k == mc.field_1687) {
            long var3 = System.nanoTime();

            try {
               if (this.q()) {
                  yr.X(var1, var2, this.A, this.u);
               } else {
                  j_.a(var1, var2, this.H, this.g.X(), this.g.Y().colors());
               }
            } catch (RuntimeException var6) {
               q8.u("PlayerModel", "Submission failed; restoring native body", var6);
               this.setEnabled(false);
            }

            if (this.s.getValue()) {
               this.i = this.i + (System.nanoTime() - var3);
               this.z++;
               if (System.nanoTime() - this.S > 1000000000L) {
                  mc.field_1724
                     .method_7353(
                        class_2561.method_43470(
                           String.format(Locale.ROOT, "%s: %.3f ms CPU/frame", this.n.getValue(), this.i / 1000000.0 / Math.max(1L, this.z))
                        ),
                        true
                     );
                  this.S = System.nanoTime();
                  this.i = this.z = 0L;
               }
            }
         }
      }
   }

   public boolean o(class_10017 var1, class_1306 var2, class_4587 var3) {
      if (this.isEnabled()) {
         if (this.g(var1) && this.v) {
            int var4 = var2 == class_1306.field_6183 ? 1 : 0;
            if (this.q()) {
               float var5 = this.Y instanceof q1 var6 ? var6.larp$emoteWeight() : 0.0F;
               qv.e(
                  var3,
                  this.u.R(),
                  this.u.W((boolean)var4),
                  (boolean)var4,
                  this.x,
                  this.G,
                  this.Y.field_53453,
                  var5 > 0.0F ? ((q1)this.Y).larp$emoteSample() : null,
                  var5
               );
               return true;
            } else {
               hr.D(var3, j_.S(), (boolean)var4, this.g.x((boolean)var4, hr.m((boolean)var4)), this.x, this.G, this.Y.field_53453);
               return true;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
