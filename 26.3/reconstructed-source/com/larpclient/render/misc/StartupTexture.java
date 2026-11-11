package com.larpclient.render.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.client.renderer.texture.MipmapStrategy;
import net.minecraft.client.renderer.texture.TextureContents;
import net.minecraft.client.resources.metadata.texture.TextureMetadataSection;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.LoggerFactory;

public final class StartupTexture extends MipmappedTexture {
   public static final Identifier bRAND_TEXTURE = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/startup-brand.png");
   public static final Identifier tYPOGRAPHY_TEXTURE = Identifier.fromNamespaceAndPath("larp-client", "startup/typography");
   private final String resourcePath;

   public static StartupTexture.StartupPreferences loadPreferences(Path var0) {
      byte var1 = 1;
      if (!Files.exists(var0)) {
         return new StartupTexture.StartupPreferences((boolean)var1);
      }

      try {
         if (Files.isSymbolicLink(var0) || Files.size(var0) > 131072L) {
            throw new IOException("Unsafe startup appearance preferences");
         }

         JsonObject var2 = JsonParser.parseString(Files.readString(var0)).getAsJsonObject();
         JsonElement var3 = var2.get("motion");
         if (var3 != null && var3.isJsonPrimitive() && var3.getAsJsonPrimitive().isBoolean()) {
            var1 = var3.getAsBoolean();
         }
      } catch (IOException | RuntimeException var4) {
         LoggerFactory.getLogger("Larp/Startup").warn("Startup motion preferences unavailable; using default motion", var4);
      }

      return new StartupTexture.StartupPreferences((boolean)var1);
   }

   public StartupTexture() {
      this(bRAND_TEXTURE, "textures/gui/logo-hd.png", true);
   }

   public StartupTexture(Identifier var1, String var2) {
      this(var1, var2, false);
   }

   private StartupTexture(Identifier var1, String var2, boolean var3) {
      super(var1, var3);
      this.resourcePath = "/assets/larp-client/" + var2;
   }

   @Override
   public TextureContents loadContents(ResourceManager var1) throws IOException {
      try (InputStream var2 = StartupTexture.class.getResourceAsStream(this.resourcePath)) {
         if (var2 == null) {
            throw new IOException("Missing startup asset: " + this.resourcePath);
         } else {
            return new TextureContents(NativeImage.read(var2), new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         }
      }
   }

   public record StartupPreferences(boolean motion) {
   }
}
