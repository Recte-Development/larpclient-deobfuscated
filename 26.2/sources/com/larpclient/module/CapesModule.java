package com.larpclient.module;

import com.larpclient.bd;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.ClientAsset.Texture;
import net.minecraft.world.entity.player.PlayerSkin;

public final class CapesModule extends Module {
   private static CapesModule m;
   private final l p = this.addSetting(new l("Cape", "Official cape to wear. Only your client shows it", "MineCon 2011", com.larpclient.pi.U()) {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public String getModeLabel(String var1) {
         return com.larpclient.pi.s(var1);
      }

      @Override
      public int drawModeIcon(GuiGraphicsExtractor var1, String var2, int var3, int var4) {
         com.larpclient.pi.f var5 = com.larpclient.pi.i(var2);
         if (var5 == null) {
            return 0;
         }

         Texture var6 = bd.W(var5);
         if (var6 != null) {
            var1.blit(RenderPipelines.GUI_TEXTURED, var6.texturePath(), var3 + 4, var4, 1.0F, 1.0F, 10, 16, 10, 16, 64, 32);
         } else {
            var1.fill(var3 + 4, var4, var3 + 14, var4 + 16, 822083583);
         }

         return 18;
      }
   });
   private volatile Texture V;
   private long W;
   private long S;
   private volatile String q = "Choose a cape";

   public CapesModule() {
      super("Capes", "Wear any official cape, like a MineCon cape. Only you see it", Category.VISUALS, -1);
      m = this;
      this.p.o(var1 -> {
         if (this.isRuntimeActive()) {
            this.b();
         }
      });
   }

   public static CapesModule K() {
      return m;
   }

   @Override
   public String getStatus() {
      return this.q;
   }

   @Override
   public void onEnable() {
      this.b();
   }

   @Override
   public void onTick() {
      long var7;
      int var10000 = (var7 = this.S - 0L) == 0L ? 0 : (var7 < 0L ? -1 : 1);
      boolean var2 = false;
      int var1 = var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         long var8;
         var10000 = (var8 = System.nanoTime() - this.S) == 0L ? 0 : (var8 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         if (Integer.signum(var3) >>> 31 == 0) {
            this.b();
         }
      }
   }

   @Override
   public void onDisable() {
      this.W++;
      this.V = null;
      this.S = 0L;
      this.q = "Original cape restored";
      com.larpclient.h.A("Capes", "Disabled; original cape restored");
   }

   private void b() {
      this.S = 0L;
      long var1 = ++this.W;
      com.larpclient.pi.f var3 = com.larpclient.pi.i(this.p.getValue());
      if (var3 == null) {
         this.V = null;
         this.q = "Choose a cape";
      } else {
         CompletableFuture var4 = bd.t(var3);
         if (var4.isDone() && !var4.isCompletedExceptionally()) {
            this.V = (Texture)var4.join();
            this.q = var3.label();
         } else {
            this.q = "Loading " + var3.label() + "...";
            var4.whenCompleteAsync((var4x, var5) -> {
               if (var1 == this.W && this.isRuntimeActive()) {
                  if (var5 != null) {
                     this.q = "Could not load " + var3.label() + ". Retrying in 30 seconds";
                     this.S = System.nanoTime() + 30000000000L;
                  } else {
                     this.V = var4x;
                     this.q = var3.label();
                     com.larpclient.h.A("Capes", "Wearing {}", var3.label());
                  }
               }
            }, getMc());
         }
      }
   }

   public PlayerSkin b(PlayerSkin var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.isOperational()) {
            com.larpclient.pi.f var2 = com.larpclient.pi.i(this.p.getValue());
            Texture var3 = var2 == null ? null : bd.W(var2);
            return var3 == null ? var1 : new PlayerSkin(var1.body(), var3, null, var1.model(), var1.secure());
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public PlayerSkin L(UUID var1, PlayerSkin var2) {
      if (this.isEnabled()) {
         Texture var3 = this.V;
         return var3 != null && var2 != null && this.isRuntimeActive() && mc.player != null && var1.equals(mc.player.getUUID())
            ? new PlayerSkin(var2.body(), var3, null, var2.model(), var2.secure())
            : var2;
      } else {
         return var2;
      }
   }
}
