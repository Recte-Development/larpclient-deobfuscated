package com.larpclient.social.emote;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

public final class EmoteAnimation {
   public static final int channelX = 0;
   public static final int channelY = 1;
   public static final int channelZ = 2;
   public static final int channelPitch = 3;
   public static final int channelYaw = 4;
   public static final int channelRoll = 5;
   public static final int channelBend = 6;
   public static final int channelScale = 7;
   public static final int channelSpread = 8;
   public static final int channelHold = 9;
   public static final int channelsPerPart = 10;
   public static final int totalChannels = EmoteAnimation.Part.values().length * 10;
   private static final String[] channelNames = new String[]{"x", "y", "z", "pitch", "yaw", "roll", "bend", "scale", "spread", "hold"};
   private static final float[][] defaultOffsets = new float[][]{
      {0.0F, 0.0F, 0.0F},
      {0.0F, 0.0F, 0.0F},
      {-5.0F, 2.0F, 0.0F},
      {5.0F, 2.0F, 0.0F},
      {-1.9F, 12.0F, 0.1F},
      {1.9F, 12.0F, 0.1F},
      {0.0F, 0.0F, 0.0F},
      {0.0F, 0.0F, 0.0F},
      {0.0F, 0.0F, 0.0F}
   };
   public final String name;
   public final String author;
   public final int beginTick;
   public final int endTick;
   public final int stopTick;
   public final int returnTick;
   public final boolean looping;
   private final boolean easeBeforeKeyframe;
   private final EmoteAnimation.Track[] tracks = new EmoteAnimation.Track[totalChannels];
   private final Set<String> unsupportedFeatures = new TreeSet<>();

   private EmoteAnimation(String var1, String var2, int var3, int var4, int var5, boolean var6, int var7, boolean var8) {
      this.name = var1;
      this.author = var2;
      this.beginTick = var3;
      this.endTick = var4;
      this.stopTick = var5;
      this.looping = var6;
      this.returnTick = var7;
      this.easeBeforeKeyframe = var8;
   }

   public static EmoteAnimation parse(Reader var0, EmoteAnimation.LoopRange var1) {
      JsonObject var2 = JsonParser.parseReader(var0).getAsJsonObject();
      if (!var2.has("emote")) {
         throw new IllegalArgumentException("Only Emotecraft JSON emotes are supported");
      }

      int var3 = var2.has("version") ? var2.get("version").getAsInt() : 1;
      if (var3 > 3) {
         throw new IllegalArgumentException("Unsupported emote version " + var3);
      }

      JsonObject var4 = var2.getAsJsonObject("emote");
      int var5 = var4.has("beginTick") ? var4.get("beginTick").getAsInt() : 0;
      int var6 = var4.get("endTick").getAsInt();
      if (var6 <= 0) {
         throw new IllegalArgumentException("endTick must be positive");
      }

      int var7 = var4.has("isLoop") && var4.has("returnTick") && var4.get("isLoop").getAsBoolean() ? 1 : 0;
      int var8 = var7 != 0 ? var4.get("returnTick").getAsInt() : 0;
      int var9 = var4.has("stopTick") ? var4.get("stopTick").getAsInt() : var6;
      if (var1 != null) {
         var7 = 1;
         var8 = var1.returnTick();
         var6 = var1.endTick();
         var9 = var6 + 1;
      }

      if (var7 == 0 || var8 >= 0 && var8 <= var6) {
         int var10 = var4.has("degrees") && !var4.get("degrees").getAsBoolean() ? 0 : 1;
         int var11 = var4.has("easeBeforeKeyframe") && var4.get("easeBeforeKeyframe").getAsBoolean() ? 1 : 0;
         EmoteAnimation var12 = new EmoteAnimation(readString(var2, "name"), readString(var2, "author"), var5, var6, var9, (boolean)var7, var8, (boolean)var11);

         for (JsonElement var14 : var4.getAsJsonArray("moves")) {
            JsonObject var15 = var14.getAsJsonObject();
            int var16 = var15.get("tick").getAsInt();
            EmoteAnimation.Easing var17 = EmoteAnimation.Easing.fromName(var15.has("easing") ? var15.get("easing").getAsString() : "linear");
            if (var17 == null) {
               var12.unsupportedFeatures.add("easing " + var15.get("easing").getAsString());
               var17 = EmoteAnimation.Easing.LINEAR;
            }

            int var18 = var15.has("turn") ? var15.get("turn").getAsInt() : 0;

            for (Entry var20 : var15.entrySet()) {
               if (((JsonElement)var20.getValue()).isJsonObject()) {
                  EmoteAnimation.Part var21 = resolvePart((String)var20.getKey(), var3);
                  if (var21 == null) {
                     var12.unsupportedFeatures.add((String)var20.getKey());
                  } else {
                     for (Entry var23 : ((JsonElement)var20.getValue()).getAsJsonObject().entrySet()) {
                        int var24 = Arrays.asList(channelNames).indexOf(var23.getKey());
                        if (var24 >= 0
                           && (var24 != 6 || isLimb(var21))
                           && (var24 < 7 || var21 == EmoteAnimation.Part.PROP || var24 == 7 && var21 == EmoteAnimation.Part.TOOL)) {
                           float var25 = ((JsonElement)var23.getValue()).getAsFloat();
                           if (Float.isNaN(var25)) {
                              throw new IllegalArgumentException("Keyframe value is NaN");
                           }

                           int var26 = (var24 < 3 || var24 > 6) && var24 != 8 ? 0 : 1;
                           if (var26 != 0 && var10 != 0) {
                              var25 *= (float) (Math.PI / 180.0);
                           }

                           float var27 = var26 != 0 && var18 != 0 ? var25 + (float)((Math.PI * 2) * var18) : var25;
                           var12.getOrCreateTrack(var21, var24).addKeyframe(var16, var25, var27, var17);
                        } else {
                           var12.unsupportedFeatures.add((String)var20.getKey() + "." + (String)var23.getKey());
                        }
                     }
                  }
               }
            }
         }

         return var12;
      } else {
         throw new IllegalArgumentException("returnTick must be within 0..endTick");
      }
   }

   private static String readString(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonPrimitive() ? var0.get(var1).getAsString() : "";
   }

   private static EmoteAnimation.Part resolvePart(String var0, int var1) {
      return switch (var0) {
         case "head" -> EmoteAnimation.Part.HEAD;
         case "torso" -> var1 < 3 ? EmoteAnimation.Part.BODY : EmoteAnimation.Part.TORSO;
         case "body" -> EmoteAnimation.Part.BODY;
         case "rightArm" -> EmoteAnimation.Part.RIGHT_ARM;
         case "leftArm" -> EmoteAnimation.Part.LEFT_ARM;
         case "rightLeg" -> EmoteAnimation.Part.RIGHT_LEG;
         case "leftLeg" -> EmoteAnimation.Part.LEFT_LEG;
         case "prop" -> EmoteAnimation.Part.PROP;
         case "tool" -> EmoteAnimation.Part.TOOL;
         default -> null;
      };
   }

   public static boolean isLimb(EmoteAnimation.Part var0) {
      return var0 == EmoteAnimation.Part.RIGHT_ARM
         || var0 == EmoteAnimation.Part.LEFT_ARM
         || var0 == EmoteAnimation.Part.RIGHT_LEG
         || var0 == EmoteAnimation.Part.LEFT_LEG;
   }

   private EmoteAnimation.Track getOrCreateTrack(EmoteAnimation.Part var1, int var2) {
      int var3 = var1.ordinal() * 10 + var2;
      if (this.tracks[var3] == null) {
         this.tracks[var3] = new EmoteAnimation.Track(var2 < 3 ? defaultOffsets[var1.ordinal()][var2] : (var2 == 7 ? 1.0F : 0.0F));
      }

      return this.tracks[var3];
   }

   public Set<String> getUnsupportedFeatures() {
      return Set.copyOf(this.unsupportedFeatures);
   }

   public boolean readString(EmoteAnimation.Part var1, int var2) {
      return this.tracks[var1.ordinal() * 10 + var2] != null;
   }

   public int wrapTick(int var1) {
      return this.looping && var1 > this.endTick ? this.returnTick + (var1 - this.endTick - 1) % (this.endTick - this.returnTick + 1) : var1;
   }

   public boolean resolvePart(int var1) {
      return this.looping && var1 > this.endTick;
   }

   public float loopProgress(int var1, float var2) {
      return !this.looping ? 0.0F : (this.wrapTick(var1) + var2 - this.returnTick) / (this.endTick - this.returnTick + 1);
   }

   public boolean sample(int var1) {
      return !this.looping && var1 >= this.stopTick;
   }

   public void sample(int var1, float var2, float[] var3) {
      int var4 = this.wrapTick(var1);
      boolean var5 = this.resolvePart(var1);

      for (int var6 = 0; var6 < totalChannels; var6++) {
         var3[var6] = this.tracks[var6] == null ? Float.NaN : this.interpolate(this.tracks[var6], var4, var2, var5);
      }
   }

   private float interpolate(EmoteAnimation.Track var1, int var2, float var3, boolean var4) {
      List var5 = var1.keyframes;
      int var6 = this.endTick - this.returnTick + 1;
      EmoteAnimation.Keyframe var7 = null;
      EmoteAnimation.Keyframe var8 = null;
      int var9 = 0;
      int var10 = 0;

      for (int var11 = var5.size() - 1; var11 >= 0; var11--) {
         EmoteAnimation.Keyframe var12 = (EmoteAnimation.Keyframe)var5.get(var11);
         if (var12.tick <= var2 && (!this.looping || var12.tick <= this.endTick) && (!var4 || var12.tick >= this.returnTick)) {
            var7 = var12;
            var9 = var12.tick;
            break;
         }
      }

      for (EmoteAnimation.Keyframe var15 : var5) {
         if (var15.tick > var2 && (!this.looping || var15.tick <= this.endTick)) {
            var8 = var15;
            var10 = var15.tick;
            break;
         }
      }

      if (this.looping && var7 == null && var4) {
         var7 = this.lastLoopKeyframe(var5);
         if (var7 != null) {
            var9 = var7.tick - var6;
         }
      }

      if (this.looping && var8 == null) {
         var8 = this.firstLoopKeyframe(var5);
         if (var8 != null) {
            var10 = var8.tick + var6;
         }
      }

      if (var7 == null) {
         var7 = new EmoteAnimation.Keyframe(this.beginTick, var1.defaultValue, var1.defaultValue, EmoteAnimation.Easing.LINEAR);
         var9 = this.beginTick;
      }

      if (var8 == null) {
         return var7.depart;
      }

      if (var10 <= var9) {
         return var7.depart;
      }

      float var14 = Math.clamp((var2 + var3 - var9) / (var10 - var9), 0.0F, 1.0F);
      float var16 = (this.easeBeforeKeyframe ? var8.ease : var7.ease).apply(var14);
      return var7.depart + (var8.arrive - var7.depart) * var16;
   }

   private EmoteAnimation.Keyframe firstLoopKeyframe(List<EmoteAnimation.Keyframe> var1) {
      for (EmoteAnimation.Keyframe var3 : var1) {
         if (var3.tick >= this.returnTick && var3.tick <= this.endTick) {
            return var3;
         }
      }

      return null;
   }

   private EmoteAnimation.Keyframe lastLoopKeyframe(List<EmoteAnimation.Keyframe> var1) {
      for (int var2 = var1.size() - 1; var2 >= 0; var2--) {
         EmoteAnimation.Keyframe var3 = (EmoteAnimation.Keyframe)var1.get(var2);
         if (var3.tick >= this.returnTick && var3.tick <= this.endTick) {
            return var3;
         }
      }

      return null;
   }

   public enum Easing {
      LINEAR,
      STEP,
      SINE_IN,
      SINE_OUT,
      SINE_IN_OUT,
      QUAD_IN,
      QUAD_OUT,
      QUAD_IN_OUT,
      CUBIC_IN,
      CUBIC_OUT,
      CUBIC_IN_OUT,
      QUART_IN,
      QUART_OUT,
      QUART_IN_OUT,
      QUINT_IN,
      QUINT_OUT,
      QUINT_IN_OUT,
      EXPO_IN,
      EXPO_OUT,
      EXPO_IN_OUT,
      CIRC_IN,
      CIRC_OUT,
      CIRC_IN_OUT,
      BACK_IN,
      BACK_OUT,
      BACK_IN_OUT,
      ELASTIC_IN,
      ELASTIC_OUT,
      ELASTIC_IN_OUT,
      BOUNCE_IN,
      BOUNCE_OUT,
      BOUNCE_IN_OUT;

      static EmoteAnimation.Easing fromName(String var0) {
         String var1 = var0.toUpperCase(Locale.ROOT).replace("_", "");
         if (var1.equals("STEP")) {
            return STEP;
         }

         for (EmoteAnimation.Easing var5 : values()) {
            if (var5.name().equals(var1)) {
               return var5;
            }
         }

         return null;
      }

      public float apply(float var1) {
         double var2 = var1;
         double var4 = 1.70158;
         double var6 = var4 * 1.525;
         double var8 = var4 + 1.0;
         double var10 = Math.PI * 2.0 / 3.0;
         double var12 = Math.PI * 4.0 / 9.0;

         return switch (this) {
            case LINEAR -> (float)var2;
            case STEP -> 0.0F;
            case SINE_IN -> (float)(1.0 - Math.cos(var2 * Math.PI / 2.0));
            case SINE_OUT -> (float)Math.sin(var2 * Math.PI / 2.0);
            case SINE_IN_OUT -> (float)(-(Math.cos(Math.PI * var2) - 1.0) / 2.0);
            case QUAD_IN -> (float)(var2 * var2);
            case QUAD_OUT -> (float)(1.0 - (1.0 - var2) * (1.0 - var2));
            case QUAD_IN_OUT -> var2 < 0.5 ? (float)(2.0 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0) / 2.0);
            case CUBIC_IN -> (float)(var2 * var2 * var2);
            case CUBIC_OUT -> (float)(1.0 - Math.pow(1.0 - var2, 3.0));
            case CUBIC_IN_OUT -> var2 < 0.5 ? (float)(4.0 * var2 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 3.0) / 2.0);
            case QUART_IN -> (float)Math.pow(var2, 4.0);
            case QUART_OUT -> (float)(1.0 - Math.pow(1.0 - var2, 4.0));
            case QUART_IN_OUT -> var2 < 0.5 ? (float)(8.0 * Math.pow(var2, 4.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 4.0) / 2.0);
            case QUINT_IN -> (float)Math.pow(var2, 5.0);
            case QUINT_OUT -> (float)(1.0 - Math.pow(1.0 - var2, 5.0));
            case QUINT_IN_OUT -> var2 < 0.5 ? (float)(16.0 * Math.pow(var2, 5.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 5.0) / 2.0);
            case EXPO_IN -> var2 == 0.0 ? 0.0F : (float)Math.pow(2.0, 10.0 * var2 - 10.0);
            case EXPO_OUT -> var2 == 1.0 ? 1.0F : (float)(1.0 - Math.pow(2.0, -10.0 * var2));
            case EXPO_IN_OUT -> var2 == 0.0 || var2 == 1.0
               ? (float)var2
               : (var2 < 0.5 ? (float)(Math.pow(2.0, 20.0 * var2 - 10.0) / 2.0) : (float)((2.0 - Math.pow(2.0, -20.0 * var2 + 10.0)) / 2.0));
            case CIRC_IN -> (float)(1.0 - Math.sqrt(1.0 - var2 * var2));
            case CIRC_OUT -> (float)Math.sqrt(1.0 - Math.pow(var2 - 1.0, 2.0));
            case CIRC_IN_OUT -> var2 < 0.5
               ? (float)((1.0 - Math.sqrt(1.0 - Math.pow(2.0 * var2, 2.0))) / 2.0)
               : (float)((Math.sqrt(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0)) + 1.0) / 2.0);
            case BACK_IN -> (float)(var8 * var2 * var2 * var2 - var4 * var2 * var2);
            case BACK_OUT -> (float)(1.0 + var8 * Math.pow(var2 - 1.0, 3.0) + var4 * Math.pow(var2 - 1.0, 2.0));
            case BACK_IN_OUT -> var2 < 0.5
               ? (float)(Math.pow(2.0 * var2, 2.0) * ((var6 + 1.0) * 2.0 * var2 - var6) / 2.0)
               : (float)((Math.pow(2.0 * var2 - 2.0, 2.0) * ((var6 + 1.0) * (var2 * 2.0 - 2.0) + var6) + 2.0) / 2.0);
            case ELASTIC_IN -> var2 != 0.0 && var2 != 1.0 ? (float)(-Math.pow(2.0, 10.0 * var2 - 10.0) * Math.sin((var2 * 10.0 - 10.75) * var10)) : (float)var2;
            case ELASTIC_OUT -> var2 != 0.0 && var2 != 1.0 ? (float)(Math.pow(2.0, -10.0 * var2) * Math.sin((var2 * 10.0 - 0.75) * var10) + 1.0) : (float)var2;
            case ELASTIC_IN_OUT -> var2 == 0.0 || var2 == 1.0
               ? (float)var2
               : (
                  var2 < 0.5
                     ? (float)(-(Math.pow(2.0, 20.0 * var2 - 10.0) * Math.sin((20.0 * var2 - 11.125) * var12)) / 2.0)
                     : (float)(Math.pow(2.0, -20.0 * var2 + 10.0) * Math.sin((20.0 * var2 - 11.125) * var12) / 2.0 + 1.0)
               );
            case BOUNCE_IN -> (float)(1.0 - bounceOut(1.0 - var2));
            case BOUNCE_OUT -> (float)bounceOut(var2);
            case BOUNCE_IN_OUT -> var2 < 0.5 ? (float)((1.0 - bounceOut(1.0 - 2.0 * var2)) / 2.0) : (float)((1.0 + bounceOut(2.0 * var2 - 1.0)) / 2.0);
         };
      }

      private static double bounceOut(double var0) {
         double var2 = 7.5625;
         double var4 = 2.75;
         if (var0 < 1.0 / var4) {
            return var2 * var0 * var0;
         } else if (var0 < 2.0 / var4) {
            double var8;
            return var2 * (var8 = var0 - 1.5 / var4) * var8 + 0.75;
         } else {
            double var6;
            double var7;
            return var0 < 2.5 / var4 ? var2 * (var6 = var0 - 2.25 / var4) * var6 + 0.9375 : var2 * (var7 = var0 - 2.625 / var4) * var7 + 0.984375;
         }
      }
   }

   private record Keyframe(int tick, float arrive, float depart, EmoteAnimation.Easing ease) {
   }

   public record LoopRange(int returnTick, int endTick) {
   }

   public enum Part {
      HEAD,
      TORSO,
      RIGHT_ARM,
      LEFT_ARM,
      RIGHT_LEG,
      LEFT_LEG,
      BODY,
      PROP,
      TOOL;
   }

   private static final class Track {
      final float defaultValue;
      final List<EmoteAnimation.Keyframe> keyframes = new ArrayList<>();

      Track(float var1) {
         this.defaultValue = var1;
      }

      void addKeyframe(int var1, float var2, float var3, EmoteAnimation.Easing var4) {
         int var5 = 0;

         while (var5 < this.keyframes.size() && this.keyframes.get(var5).tick < var1) {
            var5++;
         }

         if (var5 < this.keyframes.size() && this.keyframes.get(var5).tick == var1) {
            this.keyframes.set(var5, new EmoteAnimation.Keyframe(var1, this.keyframes.get(var5).arrive, var3, var4));
         } else {
            this.keyframes.add(var5, new EmoteAnimation.Keyframe(var1, var2, var3, var4));
         }
      }
   }
}
