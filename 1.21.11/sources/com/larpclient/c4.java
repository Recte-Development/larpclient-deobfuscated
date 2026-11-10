package com.larpclient;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import net.minecraft.class_1011;
import net.minecraft.class_10539;
import net.minecraft.class_1084;
import net.minecraft.class_12253;
import net.minecraft.class_2960;
import net.minecraft.class_3300;
import org.slf4j.LoggerFactory;

public final class c4 extends ca {
   public static final class_2960 W = class_2960.method_60655("larp-client", "textures/gui/startup-brand.png");
   public static final class_2960 C = class_2960.method_60655("larp-client", "startup/typography");
   private final String E;

   public static c4.i p(Path var0) {
      byte var1 = 1;
      if (!Files.exists(var0)) {
         return new c4.i((boolean)var1);
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

      return new c4.i((boolean)var1);
   }

   public c4() {
      this(W, "textures/gui/logo-hd.png", true);
   }

   public c4(class_2960 var1, String var2) {
      this(var1, var2, false);
   }

   private c4(class_2960 var1, String var2, boolean var3) {
      super(var1, var3);
      this.E = "/assets/larp-client/" + var2;
   }

   @Override
   public class_10539 method_65809(class_3300 var1) throws IOException {
      try (InputStream var2 = c4.class.getResourceAsStream(this.E)) {
         if (var2 == null) {
            throw new IOException("Missing startup asset: " + this.E);
         } else {
            return new class_10539(class_1011.method_4309(var2), new class_1084(true, true, class_12253.field_64077, 0.0F));
         }
      }
   }

   public record i(boolean motion) {
   }
}
