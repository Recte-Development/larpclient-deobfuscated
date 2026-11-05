package com.larpclient.module;

import com.larpclient.ei;
import com.larpclient.ke;
import com.larpclient.mt;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.ClientAsset.Texture;
import net.minecraft.world.entity.player.PlayerSkin;

public final class CapesModule extends Module {
   private static CapesModule P;
   private final d6 L = this.addSetting(new d6("Cape", "Official cape to wear. Only your client shows it", "MineCon 2011", ei.Y()) {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public String getModeLabel(String var1) {
         return ei.B(var1);
      }

      @Override
      public int drawModeIcon(GuiGraphicsExtractor var1, String var2, int var3, int var4) {
         ei.b var5 = ei.e(var2);
         if (var5 == null) {
            return 0;
         }

         Texture var6 = ke.I(var5);
         if (var6 != null) {
            var1.blit(RenderPipelines.GUI_TEXTURED, var6.texturePath(), var3 + 4, var4, 1.0F, 1.0F, 10, 16, 10, 16, 64, 32);
         } else {
            var1.fill(var3 + 4, var4, var3 + 14, var4 + 16, 822083583);
         }

         return 18;
      }
   });
   private volatile Texture x;
   private long V;
   private long c;
   private volatile String Y = "Choose a cape";

   public CapesModule() {
      super("Capes", "Wear any official cape, like a MineCon cape. Only you see it", Category.VISUALS, -1);
      P = this;
      this.L.h(var1 -> {
         if (this.isRuntimeActive()) {
            this.H();
         }
      });
   }

   public static CapesModule u() {
      return P;
   }

   @Override
   public String getStatus() {
      return this.Y;
   }

   @Override
   public void onEnable() {
      this.H();
   }

   @Override
   public void onTick() {
      long var7;
      int var10000 = (var7 = this.c - 0L) == 0L ? 0 : (var7 < 0L ? -1 : 1);
      boolean var2 = false;
      int var1 = var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         long var8;
         var10000 = (var8 = System.nanoTime() - this.c) == 0L ? 0 : (var8 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         if (Integer.signum(var3) >>> 31 == 0) {
            this.H();
         }
      }
   }

   @Override
   public void onDisable() {
      this.V++;
      this.x = null;
      this.c = 0L;
      this.Y = "Original cape restored";
      mt.K("Capes", "Disabled; original cape restored");
   }

   private void H() {
      this.c = 0L;
      long var1 = ++this.V;
      ei.b var3 = ei.e(this.L.P());
      if (var3 == null) {
         this.x = null;
         this.Y = "Choose a cape";
      } else {
         CompletableFuture var4 = ke.l(var3);
         if (var4.isDone() && !var4.isCompletedExceptionally()) {
            this.x = (Texture)var4.join();
            this.Y = var3.label();
         } else {
            this.Y = "Loading " + var3.label() + "...";
            var4.whenCompleteAsync((var4x, var5) -> {
               if (var1 == this.V && this.isRuntimeActive()) {
                  if (var5 != null) {
                     this.Y = "Could not load " + var3.label() + ". Retrying in 30 seconds";
                     this.c = System.nanoTime() + 30000000000L;
                  } else {
                     this.x = var4x;
                     this.Y = var3.label();
                     mt.K("Capes", "Wearing {}", var3.label());
                  }
               }
            }, getMc());
         }
      }
   }

   public PlayerSkin T(PlayerSkin var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.isOperational()) {
            ei.b var2 = ei.e(this.L.P());
            Texture var3 = var2 == null ? null : ke.I(var2);
            return var3 == null ? var1 : new PlayerSkin(var1.body(), var3, null, var1.model(), var1.secure());
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public PlayerSkin d(UUID var1, PlayerSkin var2) {
      if (this.isEnabled()) {
         Texture var3 = this.x;
         return var3 != null && var2 != null && this.isRuntimeActive() && mc.player != null && var1.equals(mc.player.getUUID())
            ? new PlayerSkin(var2.body(), var3, null, var2.model(), var2.secure())
            : var2;
      } else {
         return var2;
      }
   }
}
