package com.larpclient.update;

import java.io.IOException;
import java.util.Set;

public record Identity(String product, String edition, String minecraft, String loader, String channel, String flavor, String version, String buildId) {
   public Identity(String product, String edition, String minecraft, String loader, String channel, String flavor, String version, String buildId) {
      for (String var12 : new String[]{product, edition, minecraft, loader, channel, flavor, buildId}) {
         if (var12 == null || !StrictJson.IDENTIFIER.matcher(var12).matches()) {
            throw new IllegalArgumentException("Invalid build identity");
         }
      }

      if (!product.equals("larpclient")
         || !Set.of("full", "legit").contains(edition)
         || !loader.equals("fabric")
         || !Set.of("stable", "beta", "experimental").contains(channel)
         || !Set.of("plain", "protected").contains(flavor)) {
         throw new IllegalArgumentException("Unsupported build identity");
      }

      if (version != null && !version.isBlank() && version.length() <= 128 && !version.chars().anyMatch(var0 -> var0 < 33 || var0 > 126)) {
         this.product = product;
         this.edition = edition;
         this.minecraft = minecraft;
         this.loader = loader;
         this.channel = channel;
         this.flavor = flavor;
         this.version = version;
         this.buildId = buildId;
      } else {
         throw new IllegalArgumentException("Invalid build version");
      }
   }

   public boolean sameStream(Identity var1) {
      return var1 != null
         && this.product.equals(var1.product)
         && this.edition.equals(var1.edition)
         && this.minecraft.equals(var1.minecraft)
         && this.loader.equals(var1.loader)
         && this.channel.equals(var1.channel)
         && this.flavor.equals(var1.flavor);
   }

   static Identity checked(String var0, String var1, String var2, String var3, String var4, String var5, String var6, String var7) throws IOException {
      try {
         return new Identity(var0, var1, var2, var3, var4, var5, var6, var7);
      } catch (IllegalArgumentException var9) {
         throw new IOException("Invalid artifact identity", var9);
      }
   }
}
