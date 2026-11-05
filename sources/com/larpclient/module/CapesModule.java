package com.larpclient.module;

import com.larpclient.op;
import com.larpclient.r4;
import com.larpclient.xm;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.ClientAsset.Texture;
import net.minecraft.world.entity.player.PlayerSkin;

public final class CapesModule extends Module {
   private static CapesModule Y;
   private final lp V = this.addSetting(new lp("Cape", "Official cape to wear. Only your client shows it", "MineCon 2011", xm.h()) {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public String getModeLabel(String var1) {
         return xm.g(var1);
      }

      @Override
      public int drawModeIcon(GuiGraphicsExtractor var1, String var2, int var3, int var4) {
         xm.p var5 = xm.m(var2);
         if (var5 == null) {
            return 0;
         }

         Texture var6 = op.w(var5);
         if (var6 != null) {
            var1.blit(RenderPipelines.GUI_TEXTURED, var6.texturePath(), var3 + 4, var4, 1.0F, 1.0F, 10, 16, 10, 16, 64, 32);
         } else {
            var1.fill(var3 + 4, var4, var3 + 14, var4 + 16, 822083583);
         }

         return 18;
      }
   });
   private volatile Texture b;
   private long N;
   private long o;
   private volatile String v = "Choose a cape";

   public CapesModule() {
      super("Capes", "Wear any official cape, like a MineCon cape. Only you see it", Category.VISUALS, -1);
      Y = this;
      this.V.W(var1 -> {
         if (this.isRuntimeActive()) {
            this.n();
         }
      });
   }

   public static CapesModule p() {
      return Y;
   }

   @Override
   public String getStatus() {
      return this.v;
   }

   @Override
   public void onEnable() {
      this.n();
   }

   @Override
   public void onTick() {
      long var7;
      int var10000 = (var7 = this.o - 0L) == 0L ? 0 : (var7 < 0L ? -1 : 1);
      boolean var2 = false;
      int var1 = var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         long var8;
         var10000 = (var8 = System.nanoTime() - this.o) == 0L ? 0 : (var8 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         if (Integer.signum(var3) >>> 31 == 0) {
            this.n();
         }
      }
   }

   @Override
   public void onDisable() {
      this.N++;
      this.b = null;
      this.o = 0L;
      this.v = "Original cape restored";
      r4.V("Capes", "Disabled; original cape restored");
   }

   private void n() {
      this.o = 0L;
      long var1 = ++this.N;
      xm.p var3 = xm.m(this.V.getValue());
      if (var3 == null) {
         this.b = null;
         this.v = "Choose a cape";
      } else {
         CompletableFuture var4 = op.y(var3);
         if (var4.isDone() && !var4.isCompletedExceptionally()) {
            this.b = (Texture)var4.join();
            this.v = var3.label();
         } else {
            this.v = "Loading " + var3.label() + "...";
            var4.whenCompleteAsync((var4x, var5) -> {
               if (var1 == this.N && this.isRuntimeActive()) {
                  if (var5 != null) {
                     this.v = "Could not load " + var3.label() + ". Retrying in 30 seconds";
                     this.o = System.nanoTime() + 30000000000L;
                  } else {
                     this.b = var4x;
                     this.v = var3.label();
                     r4.V("Capes", "Wearing {}", var3.label());
                  }
               }
            }, getMc());
         }
      }
   }

   public PlayerSkin v(PlayerSkin var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.isOperational()) {
            xm.p var2 = xm.m(this.V.getValue());
            Texture var3 = var2 == null ? null : op.w(var2);
            return var3 == null ? var1 : new PlayerSkin(var1.body(), var3, null, var1.model(), var1.secure());
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public PlayerSkin b(UUID var1, PlayerSkin var2) {
      if (this.isEnabled()) {
         Texture var3 = this.b;
         return var3 != null && var2 != null && this.isRuntimeActive() && mc.player != null && var1.equals(mc.player.getUUID())
            ? new PlayerSkin(var2.body(), var3, null, var2.model(), var2.secure())
            : var2;
      } else {
         return var2;
      }
   }
}
