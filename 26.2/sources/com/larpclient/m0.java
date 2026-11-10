package com.larpclient;

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

public final class m0 extends mc {
   public static final Identifier r = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/startup-brand.png");
   public static final Identifier q = Identifier.fromNamespaceAndPath("larp-client", "startup/typography");
   private final String l;

   public static m0.r S(Path var0) {
      byte var1 = 1;
      if (!Files.exists(var0)) {
         return new m0.r((boolean)var1);
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

      return new m0.r((boolean)var1);
   }

   public m0() {
      this(r, "textures/gui/logo-hd.png", true);
   }

   public m0(Identifier var1, String var2) {
      this(var1, var2, false);
   }

   private m0(Identifier var1, String var2, boolean var3) {
      super(var1, var3);
      this.l = "/assets/larp-client/" + var2;
   }

   @Override
   public TextureContents loadContents(ResourceManager var1) throws IOException {
      try (InputStream var2 = m0.class.getResourceAsStream(this.l)) {
         if (var2 == null) {
            throw new IOException("Missing startup asset: " + this.l);
         } else {
            return new TextureContents(NativeImage.read(var2), new TextureMetadataSection(true, true, MipmapStrategy.MEAN, 0.0F));
         }
      }
   }

   public record r(boolean motion) {
   }
}
