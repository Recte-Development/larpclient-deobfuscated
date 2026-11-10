package com.larpclient.module;

import com.larpclient.fq;
import com.larpclient.q8;
import com.larpclient.wt;
import java.util.List;
import java.util.Set;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientChunkEvents.Unload;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1923;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_4184;
import net.minecraft.class_638;
import net.minecraft.class_6606;
import org.joml.Vector3f;

public final class MotionScannerModule extends Module {
   private final lo k = this.addSetting(new lo("Opacity", "Opacity of detected chunk volumes", 140.0, 10.0, 255.0, 1.0));
   private final lo a = this.addSetting(new lo("Sensitivity", "Minimum nearby anomalous chunks required for a discovery", 1.0, 1.0, 5.0, 1.0));
   private final l3 m = this.addSetting(new l3("HUD Notification", "Show a local discovery notification", true));
   private final l3 J = this.addSetting(new l3("Sound Notification", "Play the source note-block chime on a discovery", true));
   private final l3 F = this.addSetting(new l3("Chat Notifications", "Write discoveries to local chat", true));
   private final l3 T = this.addSetting(new l3("Show Coords", "Include representative chunk-center coordinates in chat", true));
   private final l3 L = this.addSetting(new l3("Show Tracers", "Draw camera-to-discovery lines", false));
   private final lo D = this.addSetting(new lo("Tracers Alpha", "Opacity of camera-to-discovery lines", 70.0, 0.0, 255.0, 1.0));
   private final l0 y = this.addSetting(new l0("Highlight Color", "Detected chunk and tracer color; source blue by default", -14122241));
   private final fq p = new fq();
   private Set<Long> C = Set.of();
   private class_638 t;
   private boolean S;
   private static MotionScannerModule x;

   public MotionScannerModule() {
      super("Motion Scanner", "Detect unusual received skylight patterns below sea level", Category.RENDER, -1);
      x = this;
      this.D.O(this.L::getValue);
      this.T.O(this.F::getValue);
      this.a.a(var1 -> this.S = (boolean)1);
      ClientChunkEvents.CHUNK_UNLOAD.register((Unload)(var1, var2) -> {
         if (var1 == this.t) {
            this.p.a(var2.method_12004().method_8324());
            this.S = (boolean)1;
            this.C = this.p.M();
         }
      });
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.E());
   }

   public List<lb<?>> K() {
      return List.of(this.a);
   }

   public List<lb<?>> w() {
      return List.of(this.m, this.J, this.F, this.T);
   }

   public List<lb<?>> u() {
      return List.of(this.k, this.y, this.L, this.D);
   }

   private void G(class_638 var1) {
      this.p.T();
      this.C = Set.of();
      this.S = (boolean)0;
      this.t = var1;
      wt.T().w(this.getName());
   }

   @Override
   public void onEnable() {
      this.G(getMc().field_1687);
      q8.G("BaseFinding", "Motion Scanner active: explicit zeroed sky-light sections below Y 62, cluster threshold {}", this.a.getValue());
   }

   @Override
   public void onDisable() {
      q8.v("BaseFinding", "Motion Scanner cleanup: {} tracked chunks", this.p.R());
      this.G(null);
   }

   public static void H(class_638 var0, int var1, int var2, class_6606 var3) {
      if (x != null && x.isEnabled()) {
         MotionScannerModule var4 = x;
         if (var4 != null && var4.isRuntimeActive() && var0 != null && var3 != null) {
            class_310 var5 = getMc();
            if (var5.method_18854() && var5.field_1687 == var0 && var5.field_1724 != null && !(var5.field_1724.method_23318() < 0.0)) {
               if (var4.t != var0) {
                  var4.G(var0);
               }

               if (var0.method_2935().method_21730(var1, var2) != null) {
                  try {
                     List var6 = var3.method_38606().stream().map(fq::k).toList();
                     boolean var7 = var4.p.P(var1, var2, var0.method_32890(), var0.method_32891(), var3.method_38601(), var3.method_38604(), var6);
                     var4.S |= var7;
                  } catch (RuntimeException var8) {
                     q8.O("BaseFinding", "Motion Scanner packet classification failed; later light updates can recover", var8);
                  }
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         if (this.t != mc.field_1687) {
            this.G(mc.field_1687);
         }

         int var10000 = this.p.j(var1x -> {
            class_1923 var3x = new class_1923(var1x);
            return this.t.method_2935().method_21730(var3x.field_9181, var3x.field_9180) != null;
         });
         boolean var6 = false;
         byte var5 = (byte)var10000;
         var10000 = Integer.signum(var5);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.C = this.p.M();
         }

         double var14;
         var10000 = (var14 = mc.field_1724.method_23318() - 0.0) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
         boolean var8 = false;
         int var7 = var10000;
         if (Integer.signum(var7) >>> 31 == 0) {
            boolean var10 = false;
            byte var9 = this.S;
            var10000 = Integer.signum(var9);
            if ((var10000 | -var10000) >>> 31 != 0) {
               class_1923 var1 = mc.field_1724.method_31476();

               for (long var3 : this.p.s(this.a.getValue().intValue(), var1.field_9181, var1.field_9180)) {
                  this.A(var3);
               }

               this.C = this.p.M();
               this.S = (boolean)0;
               return;
            }
         }
      }
   }

   private void A(long var1) {
      class_1923 var3 = new class_1923(var1);
      int var4 = var3.method_8326() + 8;
      int var5 = var3.method_8328() + 8;
      q8.v("BaseFinding", "Motion Scanner discovery: chunk [{}, {}], center {}, {}", var3.field_9181, var3.field_9180, var4, var5);
      if (this.J.getValue()) {
         mc.field_1724.method_5783((class_3414)class_3417.field_14725.comp_349(), 1.2F, 1.2F);
      }

      if (this.F.getValue()) {
         mc.method_44714()
            .method_44736(class_2561.method_43470("(Motion Scanner) Base detected" + (this.T.getValue() ? ": " + var4 + ", " + var5 : "")), false);
      }

      if (this.m.getValue()) {
         wt.T().U(this.getName(), "Base detected", wt.s.R);
      }
   }

   private void E() {
      if (this.isRuntimeActive() && this.isInGame() && this.t == mc.field_1687) {
         class_1923 var1 = mc.field_1724.method_31476();
         int var2 = this.y.getValue() & 16777215 | this.k.getValue().intValue() << 24;
         class_4184 var3 = mc.field_1773.method_19418();
         Vector3f var4 = new Vector3f(0.0F, 0.0F, -1.0F).rotate(var3.method_23767());
         class_243 var5 = var3.method_71156().method_1031(var4.x * 0.1, var4.y * 0.1, var4.z * 0.1);

         try {
            for (long var7 : this.C) {
               class_1923 var9 = new class_1923(var7);
               if (Math.abs((long)var9.field_9181 - var1.field_9181) <= 17L
                  && Math.abs((long)var9.field_9180 - var1.field_9180) <= 17L
                  && this.t.method_2935().method_21730(var9.field_9181, var9.field_9180) != null) {
                  class_238 var10 = new class_238(var9.method_8326(), -64.0, var9.method_8328(), var9.method_8326() + 16, 320.0, var9.method_8328() + 16);
                  class_12180.method_75541(var10, class_12179.method_75539(var2)).method_75533();
                  if (this.L.getValue() && this.D.getValue() > 0.0) {
                     class_12180.method_75546(var5, var10.method_1005(), var2 & 16777215 | this.D.getValue().intValue() << 24, 1.0F).method_75533();
                  }
               }
            }
         } catch (RuntimeException var11) {
            q8.O("BaseFinding", "Motion Scanner render failed; evidence retained", var11);
         }
      }
   }

   @Override
   public String getStatus() {
      return this.C.size() + " discoveries";
   }
}
