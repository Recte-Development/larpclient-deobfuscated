package com.larp.updater;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

public final class UpdateManifest {
   public static final int MAX_DOCUMENT_BYTES = 131072;
   public static final long MAX_ARTIFACT_BYTES = 134217728L;
   private static final long MAX_INTEGER = 9007199254740991L;
   private static final String AUTHORITY_DOMAIN = "Larp update keys v1\n";
   private static final String RELEASE_DOMAIN = "Larp release v1\n";

   private UpdateManifest() {
   }

   public static UpdateManifest.Verified verify(byte[] var0, byte[] var1, Identity var2, Host var3, Instant var4, long var5, long var7) throws IOException {
      Objects.requireNonNull(var2);
      Objects.requireNonNull(var3);
      Objects.requireNonNull(var4);
      if (var5 >= 0L && var7 >= 0L) {
         JsonObject var9 = StrictJson.object(var1);
         StrictJson.fields(var9, "schema", "keys");
         schema(var9);
         Map var10 = keys(StrictJson.child(var9, "keys"));
         JsonObject var11 = StrictJson.object(var0);
         StrictJson.fields(var11, "authority", "release");
         JsonObject var12 = signed(StrictJson.child(var11, "authority"), var10, "Larp update keys v1\n");
         StrictJson.fields(var12, "schema", "type", "version", "issuedAt", "expiresAt", "keys");
         schema(var12);
         if (!StrictJson.string(var12, "type", 32).equals("release-keys")) {
            throw StrictJson.fail("Wrong authority type");
         } else {
            long var13 = StrictJson.number(var12, "version", 1L, 9007199254740991L);
            if (var13 < var5) {
               throw StrictJson.fail("Release authority rollback");
            } else {
               long var15 = StrictJson.number(var12, "issuedAt", 1L, 9007199254740991L);
               long var17 = StrictJson.number(var12, "expiresAt", 1L, 9007199254740991L);
               time(var15, var17, var4.getEpochSecond(), 31622400L);
               Map var19 = keys(StrictJson.child(var12, "keys"));
               JsonObject var20 = signed(StrictJson.child(var11, "release"), var19, "Larp release v1\n");
               UpdateManifest.Release var21 = release(var20);
               time(var21.issuedAt(), var21.expiresAt(), var4.getEpochSecond(), 2678400L);
               if (var21.issuedAt() < var15 || var21.expiresAt() > var17) {
                  throw StrictJson.fail("Release outside authority validity");
               } else if (!var2.sameStream(var21.identity())) {
                  throw StrictJson.fail("Update stream mismatch");
               } else if (!var21.supports(var3)) {
                  throw StrictJson.fail("Unsupported update host");
               } else if (var21.sequence() < var7) {
                  throw StrictJson.fail("Release sequence rollback");
               } else {
                  return new UpdateManifest.Verified(var21, var13);
               }
            }
         }
      } else {
         throw StrictJson.fail("Invalid rollback floor");
      }
   }

   private static UpdateManifest.Release release(JsonObject var0) throws IOException {
      StrictJson.fields(
         var0,
         "schema",
         "releaseId",
         "buildId",
         "sequence",
         "product",
         "edition",
         "minecraft",
         "loader",
         "channel",
         "flavor",
         "version",
         "minimumLoader",
         "minimumJava",
         "platforms",
         "artifactId",
         "sha256",
         "size",
         "issuedAt",
         "expiresAt",
         "sourceRevision",
         "title",
         "summary",
         "notes"
      );
      schema(var0);
      Identity var1 = Identity.checked(
         StrictJson.identifier(var0, "product"),
         StrictJson.identifier(var0, "edition"),
         StrictJson.identifier(var0, "minecraft"),
         StrictJson.identifier(var0, "loader"),
         StrictJson.identifier(var0, "channel"),
         StrictJson.identifier(var0, "flavor"),
         StrictJson.string(var0, "version", 128),
         StrictJson.identifier(var0, "buildId")
      );
      String var2 = StrictJson.string(var0, "minimumLoader", 128);

      try {
         Version.parse(var2);
      } catch (VersionParsingException var5) {
         throw StrictJson.fail("Invalid minimum loader");
      }

      if (!var2.matches("[0-9]+(?:\\.[0-9]+){1,3}(?:[-+][A-Za-z0-9.-]+)?")) {
         throw StrictJson.fail("Invalid minimum loader");
      }

      List var3 = strings(var0, "platforms", 2, 32, true);
      if (!var3.isEmpty()
         && new HashSet(var3).size() == var3.size()
         && Set.of("any", "linux-x64", "windows-x64").containsAll(var3)
         && (!var3.contains("any") || var3.size() == 1)
         && (!var1.flavor().equals("protected") || !var3.contains("any"))) {
         String var4 = StrictJson.string(var0, "sha256", 64);
         if (!var4.matches("[0-9a-f]{64}")) {
            throw StrictJson.fail("Invalid artifact digest");
         } else {
            return new UpdateManifest.Release(
               StrictJson.identifier(var0, "releaseId"),
               var1.buildId(),
               StrictJson.number(var0, "sequence", 1L, 9007199254740991L),
               var1.product(),
               var1.edition(),
               var1.minecraft(),
               var1.loader(),
               var1.channel(),
               var1.flavor(),
               var1.version(),
               var2,
               (int)StrictJson.number(var0, "minimumJava", 21L, 99L),
               var3,
               StrictJson.identifier(var0, "artifactId"),
               var4,
               StrictJson.number(var0, "size", 1L, 134217728L),
               StrictJson.number(var0, "issuedAt", 1L, 9007199254740991L),
               StrictJson.number(var0, "expiresAt", 1L, 9007199254740991L),
               StrictJson.identifier(var0, "sourceRevision"),
               text(var0, "title", 120, true),
               text(var0, "summary", 1000, false),
               strings(var0, "notes", 32, 2000, false)
            );
         }
      } else {
         throw StrictJson.fail("Invalid release platforms");
      }
   }

   private static List<String> strings(JsonObject var0, String var1, int var2, int var3, boolean var4) throws IOException {
      JsonElement var5 = var0.get(var1);
      if (var5 != null && var5.isJsonArray() && var5.getAsJsonArray().size() <= var2) {
         ArrayList var6 = new ArrayList();

         for (JsonElement var8 : var5.getAsJsonArray()) {
            JsonObject var9 = new JsonObject();
            var9.add(var1, var8);
            var6.add(text(var9, var1, var3, var4));
         }

         return List.copyOf(var6);
      } else {
         throw StrictJson.fail("Invalid list: " + var1);
      }
   }

   private static String text(JsonObject var0, String var1, int var2, boolean var3) throws IOException {
      String var4 = StrictJson.string(var0, var1, var2);

      for (int var5 = 0; var5 < var4.length(); var5++) {
         char var6 = var4.charAt(var5);
         if (var6 < ' ' && (var3 || var6 != '\n' && var6 != '\t') || var6 == 127 || var6 >= 8234 && var6 <= 8238 || var6 >= 8294 && var6 <= 8297) {
            throw StrictJson.fail("Unsafe display text");
         }
      }

      if (var3 && var4.isBlank()) {
         throw StrictJson.fail("Empty display text");
      } else {
         return var4;
      }
   }

   private static void schema(JsonObject var0) throws IOException {
      StrictJson.number(var0, "schema", 1L, 1L);
   }

   private static void time(long var0, long var2, long var4, long var6) throws IOException {
      if (var0 > var4 + 300L || var2 <= var4 || var2 <= var0 || var2 - var0 > var6) {
         throw StrictJson.fail("Invalid metadata validity period");
      }
   }

   private static JsonObject signed(JsonObject var0, Map<String, PublicKey> var1, String var2) throws IOException {
      StrictJson.fields(var0, "payload", "keyId", "signature");
      String var3 = StrictJson.identifier(var0, "keyId");
      PublicKey var4 = (PublicKey)var1.get(var3);
      if (var4 == null) {
         throw StrictJson.fail("Untrusted signing key");
      }

      byte[] var5 = decode(StrictJson.string(var0, "payload", 131072), 131072);
      byte[] var6 = decode(StrictJson.string(var0, "signature", 86), 64);
      if (var6.length != 64) {
         throw StrictJson.fail("Invalid signature length");
      }

      try {
         Signature var7 = Signature.getInstance("Ed25519");
         var7.initVerify(var4);
         var7.update(var2.getBytes(StandardCharsets.UTF_8));
         var7.update(var5);
         if (!var7.verify(var6)) {
            throw StrictJson.fail("Invalid update signature");
         }
      } catch (GeneralSecurityException var8) {
         throw new IOException("Unable to verify update signature", var8);
      }

      return StrictJson.object(var5);
   }

   private static Map<String, PublicKey> keys(JsonObject var0) throws IOException {
      if (var0.size() >= 1 && var0.size() <= 8) {
         LinkedHashMap var1 = new LinkedHashMap();
         HashSet var2 = new HashSet();

         for (String var4 : var0.keySet()) {
            if (!StrictJson.IDENTIFIER.matcher(var4).matches()) {
               throw StrictJson.fail("Invalid signing key identifier");
            }

            String var5 = StrictJson.string(var0, var4, 128);
            if (!var2.add(var5)) {
               throw StrictJson.fail("Repeated signing key");
            }

            byte[] var6 = decode(var5, 64);
            if (var6.length != 44 || !HexFormat.of().formatHex(Arrays.copyOf(var6, 12)).equals("302a300506032b6570032100")) {
               throw StrictJson.fail("Invalid Ed25519 key encoding");
            }

            try {
               PublicKey var7 = KeyFactory.getInstance("Ed25519").generatePublic(new X509EncodedKeySpec(var6));
               if (!Arrays.equals(var7.getEncoded(), var6)) {
                  throw StrictJson.fail("Noncanonical public key");
               }

               var1.put(var4, var7);
            } catch (GeneralSecurityException var8) {
               throw new IOException("Invalid signing key", var8);
            }
         }

         return Map.copyOf(var1);
      } else {
         throw StrictJson.fail("Invalid key count");
      }
   }

   private static byte[] decode(String var0, int var1) throws IOException {
      if (!var0.isEmpty() && var0.matches("[A-Za-z0-9_-]+")) {
         try {
            byte[] var2 = Base64.getUrlDecoder().decode(var0);
            if (var2.length <= var1 && Base64.getUrlEncoder().withoutPadding().encodeToString(var2).equals(var0)) {
               return var2;
            } else {
               throw StrictJson.fail("Noncanonical base64url encoding");
            }
         } catch (IllegalArgumentException var3) {
            throw StrictJson.fail("Invalid base64url encoding");
         }
      } else {
         throw StrictJson.fail("Invalid base64url encoding");
      }
   }

   public record Release(
      String releaseId,
      String buildId,
      long sequence,
      String product,
      String edition,
      String minecraft,
      String loader,
      String channel,
      String flavor,
      String version,
      String minimumLoader,
      int minimumJava,
      List<String> platforms,
      String artifactId,
      String sha256,
      long size,
      long issuedAt,
      long expiresAt,
      String sourceRevision,
      String title,
      String summary,
      List<String> notes
   ) {
      public Release(
         String releaseId,
         String buildId,
         long sequence,
         String product,
         String edition,
         String minecraft,
         String loader,
         String channel,
         String flavor,
         String version,
         String minimumLoader,
         int minimumJava,
         List<String> platforms,
         String artifactId,
         String sha256,
         long size,
         long issuedAt,
         long expiresAt,
         String sourceRevision,
         String title,
         String summary,
         List<String> notes
      ) {
         platforms = List.copyOf(platforms);
         notes = List.copyOf(notes);
         this.releaseId = releaseId;
         this.buildId = buildId;
         this.sequence = sequence;
         this.product = product;
         this.edition = edition;
         this.minecraft = minecraft;
         this.loader = loader;
         this.channel = channel;
         this.flavor = flavor;
         this.version = version;
         this.minimumLoader = minimumLoader;
         this.minimumJava = minimumJava;
         this.platforms = platforms;
         this.artifactId = artifactId;
         this.sha256 = sha256;
         this.size = size;
         this.issuedAt = issuedAt;
         this.expiresAt = expiresAt;
         this.sourceRevision = sourceRevision;
         this.title = title;
         this.summary = summary;
         this.notes = notes;
      }

      public Identity identity() {
         return new Identity(this.product, this.edition, this.minecraft, this.loader, this.channel, this.flavor, this.version, this.buildId);
      }

      public boolean supports(Host var1) {
         if (var1.javaVersion() >= this.minimumJava && (this.platforms.contains("any") || this.platforms.contains(var1.platform()))) {
            try {
               return Version.parse(var1.loaderVersion()).compareTo(Version.parse(this.minimumLoader)) >= 0;
            } catch (VersionParsingException var3) {
               return false;
            }
         } else {
            return false;
         }
      }
   }

   public record Verified(UpdateManifest.Release release, long authorityVersion) {
   }
}
