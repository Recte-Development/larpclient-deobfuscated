package com.larpclient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.vertex.PoseStack;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.IntBinaryOperator;
import org.joml.Quaternionf;

public record c1(List<c1.n> parts, float scale, c1.l root, c1.l arm, c1.l armFit) {
   private static final int MAGIC = 1263422769;
   private static final int MAX_METADATA = 1048576;

   public static int vertexIndex(int var0, int var1) {
      return var0 < 0 && var1 != 0 ? 4 - var1 : var1;
   }

   private static void apply(PoseStack var0, float[][] var1, float[] var2, int var3, float var4, int var5) {
      float var6 = Math.clamp(var4, 0.0F, 1.0F) * (var1.length - 1);
      int var7 = Math.min((int)var6, Math.max(0, var1.length - 2));
      float var8 = var6 - var7;
      float[] var9 = var1[var7];
      float[] var10 = var1[Math.min(var7 + 1, var1.length - 1)];
      Quaternionf var11 = new Quaternionf(var9[3], var9[4], var9[5], var9[6]).slerp(new Quaternionf(var10[3], var10[4], var10[5], var10[6]), var8);
      if (var3 == 2) {
         float var18 = Math.min(var6, var1.length - 1 - var6);
         if (var18 < 1.0F) {
            float var19 = Math.clamp(var18, 0.0F, 1.0F);
            float[] var14 = var6 < 1.0F ? var1[0] : var1[var1.length - 1];
            float var15 = 1.0F - var19 * var19 * (3.0F - 2.0F * var19);
            Quaternionf var16 = new Quaternionf(var14[3], var14[4], var14[5], var14[6]).invert().mul(new Quaternionf(var2[3], var2[4], var2[5], var2[6]));
            var11.mul(new Quaternionf().slerp(var16, var15));
            var0.translate(
               var5 * (var9[0] + (var10[0] - var9[0]) * var8 + (var2[0] - var14[0]) * var15),
               var9[1] + (var10[1] - var9[1]) * var8 + (var2[1] - var14[1]) * var15,
               var9[2] + (var10[2] - var9[2]) * var8 + (var2[2] - var14[2]) * var15
            );
         } else {
            var0.translate(var5 * (var9[0] + (var10[0] - var9[0]) * var8), var9[1] + (var10[1] - var9[1]) * var8, var9[2] + (var10[2] - var9[2]) * var8);
         }

         u.v(var0, new Quaternionf(var11.x, var5 * var11.y, var5 * var11.z, var11.w));
      } else {
         float var12 = var3 == 0 ? 1.0F : Math.clamp((1.0F - var4) / 0.08F, 0.0F, 1.0F);
         if (var3 == 2 || var3 == 3) {
            var12 *= Math.clamp(var4 / 0.06F, 0.0F, 1.0F);
         }

         var12 = var12 * var12 * (3.0F - 2.0F * var12);
         Quaternionf var13 = new Quaternionf(var2[3], var2[4], var2[5], var2[6]).slerp(var11, var12);
         var0.translate(
            var5 * (var2[0] + (var9[0] + (var10[0] - var9[0]) * var8 - var2[0]) * var12),
            var2[1] + (var9[1] + (var10[1] - var9[1]) * var8 - var2[1]) * var12,
            var2[2] + (var9[2] + (var10[2] - var9[2]) * var8 - var2[2]) * var12
         );
         u.v(var0, new Quaternionf(var13.x, var5 * var13.y, var5 * var13.z, var13.w));
      }
   }

   private static c1.l motion(JsonObject var0) throws IOException {
      return new c1.l(track(var0, "idle"), track(var0, "equip"), track(var0, "inspect"), track(var0, "attack"));
   }

   public c1 shaded(int var1, int var2, IntBinaryOperator var3) {
      ArrayList var4 = new ArrayList(this.parts.size());

      for (c1.n var6 : this.parts) {
         if (!var6.tint()) {
            var4.add(var6);
         } else {
            float[] var7 = var6.vertices();
            float[] var8 = new float[var7.length / 8];

            for (int var9 = 0; var9 < var8.length; var9++) {
               int var10 = var9 * 8;
               int var11 = var3.applyAsInt(Math.clamp((int)(var7[var10 + 3] * var1), 0, var1 - 1), Math.clamp((int)(var7[var10 + 4] * var2), 0, var2 - 1));
               var8[var9] = 0.2126F * (var11 >>> 16 & 0xFF) + 0.7152F * (var11 >>> 8 & 0xFF) + 0.0722F * (var11 & 0xFF);
            }

            float[] var16 = (float[])var8.clone();
            Arrays.sort(var16);
            float var17 = Math.max(1.0F, var16[(int)((var16.length - 1) * 0.95)]);
            int[] var18 = new int[var8.length];

            for (int var12 = 0; var12 < var18.length; var12++) {
               float var13 = Math.abs(var7[var12 * 8 + 7]);
               float var14 = (0.58F + 0.42F * var13 * var13 * var13) * (0.84F + 0.16F * Math.clamp(var8[var12] / var17, 0.0F, 1.0F));
               int var15 = Math.clamp(Math.round(255.0F * var14), 0, 255);
               var18[var12] = 0xFF000000 | var15 << 16 | var15 << 8 | var15;
            }

            var4.add(new c1.n(var6.name(), var6.x(), var6.y(), var6.z(), var7, var6.finishUv(), var18, true, var6.equip(), var6.inspect(), var6.attack()));
         }
      }

      return new c1(List.copyOf(var4), this.scale, this.root, this.arm, this.armFit);
   }

   private static float[] finishUv(float[] var0) {
      float var1 = Float.POSITIVE_INFINITY;
      float var2 = Float.NEGATIVE_INFINITY;
      float var3 = Float.POSITIVE_INFINITY;
      float var4 = Float.NEGATIVE_INFINITY;

      for (byte var5 = 0; var5 < var0.length; var5 += 8) {
         var1 = Math.min(var1, var0[var5]);
         var2 = Math.max(var2, var0[var5]);
         var3 = Math.min(var3, var0[var5 + 1]);
         var4 = Math.max(var4, var0[var5 + 1]);
      }

      float var8 = Math.max(1.0E-4F, Math.max(var2 - var1, var4 - var3));
      float[] var6 = new float[var0.length / 4];

      for (byte var7 = 0; var7 < var0.length; var7 += 8) {
         var6[var7 / 4] = (var0[var7] - var1) / var8;
         var6[var7 / 4 + 1] = (var0[var7 + 1] - var3) / var8;
      }

      return var6;
   }

   private static float[][] track(JsonObject var0, String var1) throws IOException {
      JsonArray var2 = var0.getAsJsonArray(var1);
      if (var2.size() >= (var1.equals("idle") ? 1 : 2) && var2.size() <= 600) {
         float[][] var3 = new float[var2.size()][7];

         for (int var4 = 0; var4 < var3.length; var4++) {
            JsonArray var5 = var2.get(var4).getAsJsonArray();
            if (var5.size() != 7) {
               throw new IOException("Invalid transform");
            }

            for (int var6 = 0; var6 < 7; var6++) {
               var3[var4][var6] = var5.get(var6).getAsFloat();
               if (!Float.isFinite(var3[var4][var6]) || Math.abs(var3[var4][var6]) > 4.0F) {
                  throw new IOException("Invalid transform value");
               }
            }

            float[] var7 = var3[var4];
            if (Math.abs(var7[3] * var7[3] + var7[4] * var7[4] + var7[5] * var7[5] + var7[6] * var7[6] - 1.0F) > 0.001) {
               throw new IOException("Invalid rotation");
            }
         }

         return var3;
      } else {
         throw new IOException("Invalid track length");
      }
   }

   public static c1 read(InputStream var0) throws IOException {
      if (var0 == null) {
         throw new IOException("Missing knife mesh");
      }

      try (DataInputStream var1 = new DataInputStream(new BufferedInputStream(var0))) {
         if (var1.readInt() != 1263422769) {
            throw new IOException("Invalid knife mesh header");
         }

         int var2 = var1.readInt();
         if (var2 < 2 || var2 > 1048576) {
            throw new IOException("Invalid knife metadata length");
         }

         byte[] var3 = var1.readNBytes(var2);
         if (var3.length != var2) {
            throw new EOFException("Truncated knife metadata");
         }

         JsonObject var4 = JsonParser.parseString(new String(var3, StandardCharsets.UTF_8)).getAsJsonObject();
         int var5 = var4.get("version").getAsInt();
         if (var5 != 3 && var5 != 4) {
            throw new IOException("Unsupported mesh version");
         }

         ArrayList var6 = new ArrayList();
         HashSet var7 = new HashSet();
         Iterator var8 = var4.getAsJsonArray("parts").iterator();

         while (true) {
            if (var8.hasNext()) {
               JsonElement var22 = (JsonElement)var8.next();
               JsonObject var10 = var22.getAsJsonObject();
               String var11 = var10.get("name").getAsString();
               if ((var5 == 4 ? Set.of("body", "case") : Set.of("body", "safe", "blade", "bite", "latch")).contains(var11) && var7.add(var11)) {
                  JsonArray var12 = var10.getAsJsonArray("pivot");
                  if (var12.size() != 3) {
                     throw new IOException("Invalid pivot");
                  }

                  int var13 = var10.get("vertices").getAsInt();
                  if (var13 > 0 && var13 % 32 == 0 && var13 <= 2000000) {
                     float[] var14 = new float[var13];

                     for (int var15 = 0; var15 < var14.length; var15++) {
                        var14[var15] = var1.readFloat();
                        if (!Float.isFinite(var14[var15]) || Math.abs(var14[var15]) > 4.0F) {
                           throw new IOException("Invalid vertex");
                        }
                     }

                     float var23 = var12.get(0).getAsFloat();
                     float var16 = var12.get(1).getAsFloat();
                     float var17 = var12.get(2).getAsFloat();
                     if (Float.isFinite(var23 + var16 + var17) && !(Math.max(Math.abs(var23), Math.max(Math.abs(var16), Math.abs(var17))) > 2.0F)) {
                        var6.add(
                           new c1.n(
                              var11,
                              var23,
                              var16,
                              var17,
                              var14,
                              finishUv(var14),
                              white(var14.length / 8),
                              var10.get("tint").getAsBoolean(),
                              track(var10, "equip"),
                              track(var10, "inspect"),
                              track(var10, "attack")
                           )
                        );
                        continue;
                     }

                     throw new IOException("Invalid pivot coordinate");
                  }

                  throw new IOException("Invalid quad count");
               }

               throw new IOException("Invalid or duplicate knife part: " + var11);
            }

            if (var1.read() != -1) {
               throw new IOException("Unexpected knife mesh trailer");
            }

            if (var5 == 4
               ? var7.equals(Set.of("body", "case")) || var7.equals(Set.of("body"))
               : var7.equals(Set.of("body", "blade")) || var7.equals(Set.of("safe", "blade", "bite", "latch"))) {
               float var21 = var4.get("scale").getAsFloat();
               if (Float.isFinite(var21) && !(var21 < 0.1F) && !(var21 > 2.0F)) {
                  return new c1(
                     List.copyOf(var6),
                     var21,
                     motion(var4.getAsJsonObject("root")),
                     motion(var4.getAsJsonObject("arm")),
                     var4.has("arm_fit") ? fit(var4.getAsJsonObject("arm_fit")) : null
                  );
               } else {
                  throw new IOException("Invalid source scale");
               }
            } else {
               throw new IOException("Incomplete knife assembly");
            }
         }
      } catch (JsonParseException | IllegalArgumentException | IllegalStateException | UnsupportedOperationException | NullPointerException var20) {
         throw new IOException("Malformed knife mesh", var20);
      }
   }

   public c1 sharing(c1 var1, String var2) throws IOException {
      if (var1 == null) {
         throw new IOException("Missing owner of shared knife part " + var2);
      }

      c1.n var3 = var1.parts()
         .stream()
         .filter(var1x -> var1x.name().equals(var2))
         .findFirst()
         .orElseThrow(() -> new IOException("Missing shared knife part " + var2));
      if (this.parts.stream().anyMatch(var1x -> var1x.name().equals(var2))) {
         throw new IOException("Duplicate shared knife part " + var2);
      }

      ArrayList var4 = new ArrayList<>(this.parts);
      var4.add(var3);
      return new c1(List.copyOf(var4), this.scale, this.root, this.arm, this.armFit);
   }

   private static int[] white(int var0) {
      int[] var1 = new int[var0];
      Arrays.fill(var1, -1);
      return var1;
   }

   private static c1.l fit(JsonObject var0) throws IOException {
      c1.l var1 = motion(var0);

      for (float[][] var5 : new float[][][]{var1.idle(), var1.equip(), var1.inspect(), var1.attack()}) {
         for (float[] var9 : var5) {
            if (var9[0] * var9[0] + var9[1] * var9[1] + var9[2] * var9[2] > 0.0484F || var9[3] != 0.0F || var9[4] != 0.0F || var9[5] != 0.0F || var9[6] != 1.0F
               )
             {
               throw new IOException("Invalid native arm clearance offset");
            }
         }
      }

      return var1;
   }

   public record l(float[][] idle, float[][] equip, float[][] inspect, float[][] attack) {
      public void transform(PoseStack var1, int var2, float var3, int var4) {
         float[][] var5 = switch (var2) {
            case 1 -> this.equip;
            case 2 -> this.inspect;
            case 3 -> this.attack;
            default -> this.idle;
         };
         c1.apply(var1, var5, this.idle[0], var2, var3, var4);
      }

      public void translate(PoseStack var1, int var2, float var3, int var4) {
         float[][] var5 = switch (var2) {
            case 1 -> this.equip;
            case 2 -> this.inspect;
            case 3 -> this.attack;
            default -> this.idle;
         };
         float var6 = Math.clamp(var3, 0.0F, 1.0F) * (var5.length - 1);
         int var7 = (int)var6;
         float var8 = var6 - var7;
         float[] var9 = var5[var7];
         float[] var10 = var5[Math.min(var7 + 1, var5.length - 1)];
         var1.translate(var4 * (var9[0] + (var10[0] - var9[0]) * var8), var9[1] + (var10[1] - var9[1]) * var8, var9[2] + (var10[2] - var9[2]) * var8);
      }
   }

   public record n(
      String name,
      float x,
      float y,
      float z,
      float[] vertices,
      float[] finishUv,
      int[] finishColor,
      boolean tint,
      float[][] equip,
      float[][] inspect,
      float[][] attack
   ) {
      public void transform(PoseStack var1, int var2, float var3, int var4) {
         if (var2 != 0) {
            float[][] var5 = switch (var2) {
               case 1 -> this.equip;
               case 2 -> this.inspect;
               default -> this.attack;
            };
            c1.apply(var1, var5, new float[]{0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.0F}, var2, var3, var4);
         }
      }
   }
}
