package com.larpclient.module.render;

import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.social.cosmetic.CapeCatalog;
import com.larpclient.social.cosmetic.CapeTextureCache;
import com.larpclient.util.misc.LarpLog;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.ClientAsset.Texture;
import net.minecraft.world.entity.player.PlayerSkin;

public final class CapesModule extends Module {
   private static CapesModule instance;
   private final ModeSetting cape = this.addSetting(
      new ModeSetting("Cape", "Official cape to wear. Only your client shows it", "MineCon 2011", CapeCatalog.getCapeNames()) {
         @Override
         public boolean isSearchable() {
            return true;
         }

         @Override
         public String getModeLabel(String var1) {
            return CapeCatalog.getLabel(var1);
         }

         @Override
         public int drawModeIcon(GuiGraphicsExtractor var1, String var2, int var3, int var4) {
            CapeCatalog.Cape var5 = CapeCatalog.findByName(var2);
            if (var5 == null) {
               return 0;
            }

            Texture var6 = CapeTextureCache.getTexture(var5);
            if (var6 != null) {
               var1.blit(RenderPipelines.GUI_TEXTURED, var6.texturePath(), var3 + 4, var4, 1.0F, 1.0F, 10, 16, 10, 16, 64, 32);
            } else {
               var1.fill(var3 + 4, var4, var3 + 14, var4 + 16, 822083583);
            }

            return 18;
         }
      }
   );
   private volatile Texture texture;
   private long loadGeneration;
   private long retryAtNanos;
   private volatile String status = "Choose a cape";

   public CapesModule() {
      super("Capes", "Wear any official cape, like a MineCon cape. Only you see it", Category.VISUALS, -1);
      instance = this;
      this.cape.onChange(var1 -> {
         if (this.isRuntimeActive()) {
            this.loadCape();
         }
      });
   }

   public static CapesModule getInstance() {
      return instance;
   }

   @Override
   public String getStatus() {
      return this.status;
   }

   @Override
   public void onEnable() {
      this.loadCape();
   }

   @Override
   public void onTick() {
      long var7;
      int var10000 = (var7 = this.retryAtNanos - 0L) == 0L ? 0 : (var7 < 0L ? -1 : 1);
      boolean var2 = false;
      int var1 = var10000;
      var10000 = Integer.signum(var1);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         long var8;
         var10000 = (var8 = System.nanoTime() - this.retryAtNanos) == 0L ? 0 : (var8 < 0L ? -1 : 1);
         boolean var4 = false;
         int var3 = var10000;
         if (Integer.signum(var3) >>> 31 == 0) {
            this.loadCape();
         }
      }
   }

   @Override
   public void onDisable() {
      this.loadGeneration++;
      this.texture = null;
      this.retryAtNanos = 0L;
      this.status = "Original cape restored";
      LarpLog.info("Capes", "Disabled; original cape restored");
   }

   private void loadCape() {
      this.retryAtNanos = 0L;
      long var1 = ++this.loadGeneration;
      CapeCatalog.Cape var3 = CapeCatalog.findByName(this.cape.getValue());
      if (var3 == null) {
         this.texture = null;
         this.status = "Choose a cape";
      } else {
         CompletableFuture var4 = CapeTextureCache.loadCape(var3);
         if (var4.isDone() && !var4.isCompletedExceptionally()) {
            this.texture = (Texture)var4.join();
            this.status = var3.label();
         } else {
            this.status = "Loading " + var3.label() + "...";
            var4.whenCompleteAsync((var4x, var5) -> {
               if (var1 == this.loadGeneration && this.isRuntimeActive()) {
                  if (var5 != null) {
                     this.status = "Could not load " + var3.label() + ". Retrying in 30 seconds";
                     this.retryAtNanos = System.nanoTime() + 30000000000L;
                  } else {
                     this.texture = var4x;
                     this.status = var3.label();
                     LarpLog.info("Capes", "Wearing {}", var3.label());
                  }
               }
            }, getMc());
         }
      }
   }

   public PlayerSkin applyCape(PlayerSkin var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.isOperational()) {
            CapeCatalog.Cape var2 = CapeCatalog.findByName(this.cape.getValue());
            Texture var3 = var2 == null ? null : CapeTextureCache.getTexture(var2);
            return var3 == null ? var1 : new PlayerSkin(var1.body(), var3, null, var1.model(), var1.secure());
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   public PlayerSkin applyToOwnSkin(UUID var1, PlayerSkin var2) {
      if (this.isEnabled()) {
         Texture var3 = this.texture;
         return var3 != null && var2 != null && this.isRuntimeActive() && minecraft.player != null && var1.equals(minecraft.player.getUUID())
            ? new PlayerSkin(var2.body(), var3, null, var2.model(), var2.secure())
            : var2;
      } else {
         return var2;
      }
   }
}
