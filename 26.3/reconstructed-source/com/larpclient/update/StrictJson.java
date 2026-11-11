package com.larpclient.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.regex.Pattern;

public final class StrictJson {
   public static final int MAX_DOCUMENT = 131072;
   static final Pattern IDENTIFIER = Pattern.compile("[A-Za-z0-9][A-Za-z0-9._-]{0,127}");
   private static final Pattern INTEGER = Pattern.compile("0|[1-9][0-9]*");

   private StrictJson() {
   }

   public static JsonObject object(byte[] var0) throws IOException {
      return object(var0, true);
   }

   static JsonObject metadataObject(byte[] var0) throws IOException {
      return object(var0, false);
   }

   private static JsonObject object(byte[] var0, boolean var1) throws IOException {
      if (var0.length != 0 && var0.length <= 131072) {
         String var2;
         try {
            var2 = StandardCharsets.UTF_8
               .newDecoder()
               .onMalformedInput(CodingErrorAction.REPORT)
               .onUnmappableCharacter(CodingErrorAction.REPORT)
               .decode(ByteBuffer.wrap(var0))
               .toString();
         } catch (CharacterCodingException var8) {
            throw fail("Invalid UTF-8");
         }

         lexicalStrings(var2);

         try {
            JsonReader var3 = new JsonReader(new StringReader(var2));

            JsonObject var6;
            try {
               var3.setLenient(false);
               int[] var4 = new int[]{0};
               JsonElement var5 = read(var3, 0, var4, var1);
               if (var3.peek() != JsonToken.END_DOCUMENT || !var5.isJsonObject()) {
                  throw fail("Expected one JSON object");
               }

               var6 = var5.getAsJsonObject();
            } catch (Throwable var9) {
               try {
                  var3.close();
               } catch (Throwable var7) {
                  var9.addSuppressed(var7);
               }

               throw var9;
            }

            var3.close();
            return var6;
         } catch (IllegalStateException | NumberFormatException var10) {
            throw fail("Malformed JSON");
         }
      } else {
         throw fail("JSON document size");
      }
   }

   private static void lexicalStrings(String var0) throws IOException {
      boolean var1 = false;
      boolean var2 = false;

      for (int var3 = 0; var3 < var0.length(); var3++) {
         char var4 = var0.charAt(var3);
         if (var1) {
            if (var4 < ' ') {
               throw fail("Unescaped JSON control character");
            }

            if (var2) {
               var2 = false;
            } else if (var4 == '\\') {
               var2 = true;
            } else if (var4 == '"') {
               var1 = false;
            }
         } else if (var4 == '"') {
            var1 = true;
         } else if (var4 == '\ufeff') {
            throw fail("Unexpected JSON byte order mark");
         }
      }
   }

   private static JsonElement read(JsonReader var0, int var1, int[] var2, boolean var3) throws IOException {
      if (var1 <= 24 && ++var2[0] <= 20000) {
         return (JsonElement)(switch (var0.peek()) {
            case BEGIN_OBJECT -> {
               var0.beginObject();
               JsonObject var19 = new JsonObject();

               while (var0.hasNext()) {
                  String var20 = var0.nextName();
                  validateString(var20, 256);
                  if (var19.has(var20)) {
                     throw fail("Duplicate JSON key");
                  }

                  var19.add(var20, read(var0, var1 + 1, var2, var3));
               }

               var0.endObject();
               JsonObject var16 = var19;
               yield var16;
            }
            case BEGIN_ARRAY -> {
               var0.beginArray();
               JsonArray var18 = new JsonArray();

               while (var0.hasNext()) {
                  var18.add(read(var0, var1 + 1, var2, var3));
               }

               var0.endArray();
               JsonArray var15 = var18;
               yield var15;
            }
            case STRING -> {
               String var17 = var0.nextString();
               validateString(var17, 131072);
               JsonPrimitive var14 = new JsonPrimitive(var17);
               yield var14;
            }
            case NUMBER -> {
               String var5 = var0.nextString();
               if (!var3) {
                  if (var5.length() > 128 || !var5.matches("-?(?:0|[1-9][0-9]*)(?:\\.[0-9]+)?(?:[eE][+-]?[0-9]+)?")) {
                     throw fail("Invalid metadata JSON number");
                  }

                  JsonPrimitive var12;
                  try {
                     var12 = new JsonPrimitive(new BigDecimal(var5));
                  } catch (NumberFormatException var10) {
                     throw fail("Invalid metadata JSON number");
                  }

                  yield var12;
               } else {
                  if (!INTEGER.matcher(var5).matches() || var5.length() > 16) {
                     throw fail("Expected unsigned integer");
                  }

                  long var6;
                  try {
                     var6 = Long.parseLong(var5);
                  } catch (NumberFormatException var9) {
                     throw fail("Integer overflow");
                  }

                  if (var6 > 9007199254740991L) {
                     throw fail("Integer outside interoperable range");
                  }

                  JsonPrimitive var13 = new JsonPrimitive(var6);
                  yield var13;
               }
            }
            case BOOLEAN -> {
               JsonPrimitive var11 = new JsonPrimitive(var0.nextBoolean());
               yield var11;
            }
            case NULL -> {
               var0.nextNull();
               JsonNull var4 = JsonNull.INSTANCE;
               yield var4;
            }
            default -> throw fail("Unexpected JSON token");
         });
      } else {
         throw fail("JSON structure limit");
      }
   }

   public static void fields(JsonObject var0, String... var1) throws IOException {
      if (!var0.keySet().equals(Set.of(var1))) {
         throw fail("Unexpected or missing JSON fields");
      }
   }

   public static JsonObject child(JsonObject var0, String var1) throws IOException {
      JsonElement var2 = var0.get(var1);
      if (var2 != null && var2.isJsonObject()) {
         return var2.getAsJsonObject();
      } else {
         throw fail("Expected object: " + var1);
      }
   }

   public static String string(JsonObject var0, String var1, int var2) throws IOException {
      JsonElement var3 = var0.get(var1);
      if (var3 != null && var3.isJsonPrimitive() && var3.getAsJsonPrimitive().isString()) {
         String var4 = var3.getAsString();
         validateString(var4, var2);
         return var4;
      } else {
         throw fail("Expected string: " + var1);
      }
   }

   public static String identifier(JsonObject var0, String var1) throws IOException {
      String var2 = string(var0, var1, 128);
      if (!IDENTIFIER.matcher(var2).matches()) {
         throw fail("Invalid identifier: " + var1);
      } else {
         return var2;
      }
   }

   public static long number(JsonObject var0, String var1, long var2, long var4) throws IOException {
      JsonElement var6 = var0.get(var1);
      if (var6 != null && var6.isJsonPrimitive() && var6.getAsJsonPrimitive().isNumber()) {
         long var7;
         try {
            var7 = var6.getAsBigDecimal().longValueExact();
         } catch (ArithmeticException | NumberFormatException var10) {
            throw fail("Expected exact integer: " + var1);
         }

         if (var7 >= var2 && var7 <= var4) {
            return var7;
         } else {
            throw fail("Integer out of range: " + var1);
         }
      } else {
         throw fail("Expected integer: " + var1);
      }
   }

   static void validateString(String var0, int var1) throws IOException {
      if (var0.length() > var1) {
         throw fail("String limit");
      }

      for (int var2 = 0; var2 < var0.length(); var2++) {
         char var3 = var0.charAt(var2);
         if (Character.isHighSurrogate(var3)) {
            var2++;
            if (var2 >= var0.length() || !Character.isLowSurrogate(var0.charAt(var2))) {
               throw fail("Unpaired surrogate");
            }
         } else if (Character.isLowSurrogate(var3) || var3 == 0) {
            throw fail("Invalid string character");
         }
      }
   }

   static IOException fail(String var0) {
      return new IOException(var0);
   }
}
