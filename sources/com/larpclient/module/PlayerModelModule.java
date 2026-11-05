package com.larpclient.module;

import com.larpclient._i;
import com.larpclient.jk;
import com.larpclient.r4;
import com.larpclient.rh;
import com.larpclient.rr;
import com.larpclient.y2;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class PlayerModelModule extends Module {
   private static PlayerModelModule g;
   private final lc k = this.addSetting(new lc("Scale", "Visual character size", 1.0, 0.5, 2.0, 0.05));
   private final ln Q = this.addSetting(new ln("Show Timing", "Show CPU model submission timing", false));
   private Object j;
   private Object n;
   private long E;
   private long H;
   private long l;
   private boolean u;
   private LivingEntityRenderState L;
   private int o = -1;
   private final Matrix4f W = new Matrix4f();
   private float Y;
   private boolean z;
   private com.larpclient.lv D;

   public PlayerModelModule() {
      super("Player Model", "Replace your rendered player with Tung Tung Tung Sahur", Category.VISUALS, -1);
      g = this;
   }

   public static PlayerModelModule r() {
      return g;
   }

   public List<l6<?>> S() {
      return List.of(this.k);
   }

   public List<l6<?>> N() {
      return List.of(this.Q);
   }

   public boolean n() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && this.o != jk.v && _i.V();
   }

   public boolean v(EntityRenderState var1) {
      return !this.isEnabled() ? false : this.n() && this.L == var1;
   }

   public void n(LivingEntityRenderState var1) {
      if (this.isEnabled()) {
         this.L = var1;
      }
   }

   @Override
   public void onEnable() {
      this.b();

      try {
         this.r();
      } catch (Exception var2) {
         r4.r("PlayerModel", "Mesh stage failed; retaining native body", var2);
         this.setEnabled(false);
      }
   }

   private void r() {
      _i.h();
      if (this.D == null || this.D.w() != _i.P()) {
         this.D = new com.larpclient.lv(_i.P());
      }

      r4.g("PlayerModel", "Sahur ready: walk rig, {} triangles, scale={}", this.D.y().triangles(), this.k.getValue());
   }

   @Override
   public void onDisable() {
      this.b();
   }

   public void b() {
      this.u = (boolean)0;
      this.L = null;
      this.j = this.n = null;
      this.l = this.H = 0L;
   }

   public void x() {
      this.b();
      this.o = jk.v;
   }

   public void g(float var1, Vec3 var2) {
      if (this.isEnabled()) {
         this.u = (boolean)0;
         if (this.isRuntimeActive() && this.isInGame() && this.o != jk.v) {
            if (!_i.V() || this.D == null || this.D.w() != _i.P()) {
               this.r();
            }

            if (this.n() && this.isInGame()) {
               LocalPlayer var3 = mc.player;
               if (this.j != var3 || this.n != mc.level) {
                  LivingEntityRenderState var4 = this.L;
                  this.b();
                  this.L = var4;
                  this.j = var3;
                  this.n = mc.level;
               }

               if (!mc.options.getCameraType().isFirstPerson() || rr.o()) {
                  if (this.L != null) {
                     this.Y = this.k.getValue().floatValue();
                     this.z = var3.isCrouching();
                     this.D.Q(this.L.walkAnimationPos, this.L.walkAnimationSpeed);
                     rh.o(
                        this.W,
                        _i.P(),
                        (float)(Mth.lerp(var1, var3.xOld, var3.getX()) - var2.x),
                        (float)(Mth.lerp(var1, var3.yOld, var3.getY()) - var2.y),
                        (float)(Mth.lerp(var1, var3.zOld, var3.getZ()) - var2.z),
                        (float)Math.toRadians(Feature3DUIModule.t(var3, Mth.rotLerp(var1, var3.yBodyRotO, var3.yBodyRot), var1)),
                        this.Y,
                        this.z
                     );
                     this.u = (boolean)1;
                  }
               }
            }
         }
      }
   }

   public void L(PoseStack var1, y2 var2) {
      if (this.isEnabled()) {
         if (this.u && this.n() && this.j == mc.player && this.n == mc.level) {
            long var3 = System.nanoTime();

            try {
               _i.s(var1, var2, this.W, this.D.j(), this.D.y().colors());
            } catch (RuntimeException var6) {
               r4.r("PlayerModel", "Submission failed; restoring native body", var6);
               this.setEnabled(false);
            }

            if (this.Q.getValue()) {
               this.H = this.H + (System.nanoTime() - var3);
               this.l++;
               if (System.nanoTime() - this.E > 1000000000L) {
                  mc.player
                     .sendOverlayMessage(
                        Component.literal(
                           String.format(Locale.ROOT, "%s: %.3f ms CPU/frame", "Tung Tung Tung Sahur", this.H / 1000000.0 / Math.max(1L, this.l))
                        )
                     );
                  this.E = System.nanoTime();
                  this.H = this.l = 0L;
               }
            }
         }
      }
   }

   public boolean B(EntityRenderState var1, HumanoidArm var2, PoseStack var3) {
      if (this.isEnabled()) {
         if (this.v(var1) && this.u) {
            int var4 = var2 == HumanoidArm.RIGHT ? 1 : 0;
            rh.q(var3, _i.P(), (boolean)var4, this.D.L((boolean)var4, rh.Z((boolean)var4)), this.Y, this.z, this.L.scale);
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }
}
