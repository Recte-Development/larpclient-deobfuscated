package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.i9;
import com.larpclient.n6;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.ClientAsset.Texture;
import net.minecraft.world.entity.player.PlayerSkin;

public final class CapesModule extends Module {
   private static CapesModule M;
   private final o f = this.addSetting(new o("Cape", "Official cape to wear. Only your client shows it", "MineCon 2011", i9.X()) {
      @Override
      public boolean isSearchable() {
         return true;
      }

      @Override
      public String getModeLabel(String var1) {
         return i9.a(var1);
      }

      @Override
      public int drawModeIcon(GuiGraphicsExtractor var1, String var2, int var3, int var4) {
         i9.w var5 = i9.m(var2);
         if (var5 == null) {
            return 0;
         }

         Texture var6 = n6.P(var5);
         if (var6 != null) {
            var1.blit(RenderPipelines.GUI_TEXTURED, var6.texturePath(), var3 + 4, var4, 1.0F, 1.0F, 10, 16, 10, 16, 64, 32);
         } else {
            var1.fill(var3 + 4, var4, var3 + 14, var4 + 16, 822083583);
         }

         return 18;
      }
   });
   private volatile Texture k;
   private long P;
   private long x;
   private volatile String a = "Choose a cape";

   public CapesModule() {
      super("Capes", "Wear any official cape, like a MineCon cape. Only you see it", Category.VISUALS, -1);
      M = this;
      this.f.w(var1 -> {
         if (this.isRuntimeActive()) {
            this.B();
         }
      });
   }

   public static CapesModule E() {
      return M;
   }

   @Override
   public String getStatus() {
      return this.a;
   }

   @Override
   public void onEnable() {
      this.B();
   }

   @Override
   public void onTick() {
      long var7;
      int var10000 = (var7 = this.x - 0L) == 0L ? 0 : (var7 < 0L ? -1 : 1);
      boolean var2 = false;
      int var1 = var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         long var8;
         var10000 = (var8 = System.nanoTime() - this.x) == 0L ? 0 : (var8 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         if (Integer.signum(var3) >>> 31 == 0) {
            this.B();
         }
      }
   }

   @Override
   public void onDisable() {
      this.P++;
      this.k = null;
      this.x = 0L;
      this.a = "Original cape restored";
      _q.z("Capes", "Disabled; original cape restored");
   }

   private void B() {
      this.x = 0L;
      long var1 = ++this.P;
      i9.w var3 = i9.m(this.f.getValue());
      if (var3 == null) {
         this.k = null;
         this.a = "Choose a cape";
      } else {
         CompletableFuture var4 = n6.l(var3);
         if (var4.isDone() && !var4.isCompletedExceptionally()) {
            this.k = (Texture)var4.join();
            this.a = var3.label();
         } else {
            this.a = "Loading " + var3.label() + "...";
            var4.whenCompleteAsync((var4x, var5) -> {
               if (var1 == this.P && this.isRuntimeActive()) {
                  if (var5 != null) {
                     this.a = "Could not load " + var3.label() + ". Retrying in 30 seconds";
                     this.x = System.nanoTime() + 30000000000L;
                  } else {
                     this.k = var4x;
                     this.a = var3.label();
                     _q.z("Capes", "Wearing {}", var3.label());
                  }
               }
            }, getMc());
         }
      }
   }

   public PlayerSkin l(PlayerSkin var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.isOperational()) {
            i9.w var2 = i9.m(this.f.getValue());
            Texture var3 = var2 == null ? null : n6.P(var2);
            return var3 == null ? var1 : new PlayerSkin(var1.body(), var3, null, var1.model(), var1.secure());
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public PlayerSkin N(UUID var1, PlayerSkin var2) {
      if (this.isEnabled()) {
         Texture var3 = this.k;
         return var3 != null && var2 != null && this.isRuntimeActive() && mc.player != null && var1.equals(mc.player.getUUID())
            ? new PlayerSkin(var2.body(), var3, null, var2.model(), var2.secure())
            : var2;
      } else {
         return var2;
      }
   }
}
