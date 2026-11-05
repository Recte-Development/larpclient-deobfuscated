package com.larpclient;

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

public final class jc {
   public static final int b = 0;
   public static final int M = 1;
   public static final int F = 2;
   public static final int v = 3;
   public static final int I = 4;
   public static final int A = 5;
   public static final int w = 6;
   public static final int r = 7;
   public static final int O = 8;
   public static final int p = 9;
   public static final int S = 10;
   public static final int t = jc.z.values().length * 10;
   private static final String[] D = new String[]{"x", "y", "z", "pitch", "yaw", "roll", "bend", "scale", "spread", "hold"};
   private static final float[][] l = new float[][]{
      {0.0F, 0.0F, 0.0F},
      {0.0F, 0.0F, 0.0F},
      {-5.0F, 2.0F, 0.0F},
      {5.0F, 2.0F, 0.0F},
      {-1.9F, 12.0F, 0.1F},
      {1.9F, 12.0F, 0.1F},
      {0.0F, 0.0F, 0.0F},
      {0.0F, 0.0F, 0.0F}
   };
   public final String V;
   public final String Y;
   public final int c;
   public final int q;
   public final int W;
   public final int X;
   public final boolean P;
   private final boolean G;
   private final jc.u[] Q = new jc.u[t];
   private final Set<String> j = new TreeSet<>();

   private jc(String var1, String var2, int var3, int var4, int var5, boolean var6, int var7, boolean var8) {
      this.V = var1;
      this.Y = var2;
      this.c = var3;
      this.q = var4;
      this.W = var5;
      this.P = var6;
      this.X = var7;
      this.G = var8;
   }

   public static jc K(Reader var0, jc.f var1) {
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
         jc var12 = new jc(y(var2, "name"), y(var2, "author"), var5, var6, var9, (boolean)var7, var8, (boolean)var11);

         for (JsonElement var14 : var4.getAsJsonArray("moves")) {
            JsonObject var15 = var14.getAsJsonObject();
            int var16 = var15.get("tick").getAsInt();
            jc.t var17 = jc.t.R(var15.has("easing") ? var15.get("easing").getAsString() : "linear");
            if (var17 == null) {
               var12.j.add("easing " + var15.get("easing").getAsString());
               var17 = jc.t.R;
            }

            int var18 = var15.has("turn") ? var15.get("turn").getAsInt() : 0;

            for (Entry var20 : var15.entrySet()) {
               if (((JsonElement)var20.getValue()).isJsonObject()) {
                  jc.z var21 = A((String)var20.getKey(), var3);
                  if (var21 == null) {
                     var12.j.add((String)var20.getKey());
                  } else {
                     for (Entry var23 : ((JsonElement)var20.getValue()).getAsJsonObject().entrySet()) {
                        int var24 = Arrays.asList(D).indexOf(var23.getKey());
                        if (var24 >= 0 && (var24 != 6 || G(var21)) && (var24 < 7 || var21 == jc.z.Y)) {
                           float var25 = ((JsonElement)var23.getValue()).getAsFloat();
                           if (Float.isNaN(var25)) {
                              throw new IllegalArgumentException("Keyframe value is NaN");
                           }

                           int var26 = (var24 < 3 || var24 > 6) && var24 != 8 ? 0 : 1;
                           if (var26 != 0 && var10 != 0) {
                              var25 *= (float) (Math.PI / 180.0);
                           }

                           float var27 = var26 != 0 && var18 != 0 ? var25 + (float)((Math.PI * 2) * var18) : var25;
                           var12.M(var21, var24).E(var16, var25, var27, var17);
                        } else {
                           var12.j.add((String)var20.getKey() + "." + (String)var23.getKey());
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

   private static String y(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonPrimitive() ? var0.get(var1).getAsString() : "";
   }

   private static jc.z A(String var0, int var1) {
      return switch (var0) {
         case "head" -> jc.z.J;
         case "torso" -> var1 < 3 ? jc.z.v : jc.z.L;
         case "body" -> jc.z.v;
         case "rightArm" -> jc.z.K;
         case "leftArm" -> jc.z.W;
         case "rightLeg" -> jc.z.I;
         case "leftLeg" -> jc.z.h;
         case "prop" -> jc.z.Y;
         default -> null;
      };
   }

   public static boolean G(jc.z var0) {
      return var0 == jc.z.K || var0 == jc.z.W || var0 == jc.z.I || var0 == jc.z.h;
   }

   private jc.u M(jc.z var1, int var2) {
      int var3 = var1.ordinal() * 10 + var2;
      if (this.Q[var3] == null) {
         this.Q[var3] = new jc.u(var2 < 3 ? l[var1.ordinal()][var2] : (var2 == 7 ? 1.0F : 0.0F));
      }

      return this.Q[var3];
   }

   public Set<String> s() {
      return Set.copyOf(this.j);
   }

   public boolean V(jc.z var1, int var2) {
      return this.Q[var1.ordinal() * 10 + var2] != null;
   }

   public int q(int var1) {
      return this.P && var1 > this.q ? this.X + (var1 - this.q - 1) % (this.q - this.X + 1) : var1;
   }

   public boolean Y(int var1) {
      return this.P && var1 > this.q;
   }

   public float I(int var1, float var2) {
      return !this.P ? 0.0F : (this.q(var1) + var2 - this.X) / (this.q - this.X + 1);
   }

   public boolean v(int var1) {
      return !this.P && var1 >= this.W;
   }

   public void Y(int var1, float var2, float[] var3) {
      int var4 = this.q(var1);
      boolean var5 = this.Y(var1);

      for (int var6 = 0; var6 < t; var6++) {
         var3[var6] = this.Q[var6] == null ? Float.NaN : this.l(this.Q[var6], var4, var2, var5);
      }
   }

   private float l(jc.u var1, int var2, float var3, boolean var4) {
      List var5 = var1.T;
      int var6 = this.q - this.X + 1;
      jc.a var7 = null;
      jc.a var8 = null;
      int var9 = 0;
      int var10 = 0;

      for (int var11 = var5.size() - 1; var11 >= 0; var11--) {
         jc.a var12 = (jc.a)var5.get(var11);
         if (var12.tick <= var2 && (!this.P || var12.tick <= this.q) && (!var4 || var12.tick >= this.X)) {
            var7 = var12;
            var9 = var12.tick;
            break;
         }
      }

      for (jc.a var15 : var5) {
         if (var15.tick > var2 && (!this.P || var15.tick <= this.q)) {
            var8 = var15;
            var10 = var15.tick;
            break;
         }
      }

      if (this.P && var7 == null && var4) {
         var7 = this.n(var5);
         if (var7 != null) {
            var9 = var7.tick - var6;
         }
      }

      if (this.P && var8 == null) {
         var8 = this.v(var5);
         if (var8 != null) {
            var10 = var8.tick + var6;
         }
      }

      if (var7 == null) {
         var7 = new jc.a(this.c, var1.Q, var1.Q, jc.t.R);
         var9 = this.c;
      }

      if (var8 == null) {
         return var7.depart;
      }

      if (var10 <= var9) {
         return var7.depart;
      }

      float var14 = Math.clamp((var2 + var3 - var9) / (var10 - var9), 0.0F, 1.0F);
      float var16 = (this.G ? var8.ease : var7.ease).U(var14);
      return var7.depart + (var8.arrive - var7.depart) * var16;
   }

   private jc.a v(List<jc.a> var1) {
      for (jc.a var3 : var1) {
         if (var3.tick >= this.X && var3.tick <= this.q) {
            return var3;
         }
      }

      return null;
   }

   private jc.a n(List<jc.a> var1) {
      for (int var2 = var1.size() - 1; var2 >= 0; var2--) {
         jc.a var3 = (jc.a)var1.get(var2);
         if (var3.tick >= this.X && var3.tick <= this.q) {
            return var3;
         }
      }

      return null;
   }

   private record a(int tick, float arrive, float depart, jc.t ease) {
   }

   public record f(int returnTick, int endTick) {
   }

   public enum t {
      R,
      I,
      i,
      E,
      x,
      n,
      f,
      c,
      K,
      k,
      P,
      q,
      z,
      G,
      D,
      Z,
      o,
      d,
      M,
      r,
      A,
      h,
      w,
      m,
      H,
      B,
      Y,
      O,
      g,
      t,
      S,
      U;

      static jc.t R(String var0) {
         String var1 = var0.toUpperCase(Locale.ROOT).replace("_", "");
         if (var1.equals("STEP")) {
            return I;
         }

         for (jc.t var5 : values()) {
            if (var5.name().equals(var1)) {
               return var5;
            }
         }

         return null;
      }

      public float U(float var1) {
         double var2 = var1;
         double var4 = 1.70158;
         double var6 = var4 * 1.525;
         double var8 = var4 + 1.0;
         double var10 = Math.PI * 2.0 / 3.0;
         double var12 = Math.PI * 4.0 / 9.0;

         return switch (this) {
            case R -> (float)var2;
            case I -> 0.0F;
            case i -> (float)(1.0 - Math.cos(var2 * Math.PI / 2.0));
            case E -> (float)Math.sin(var2 * Math.PI / 2.0);
            case x -> (float)(-(Math.cos(Math.PI * var2) - 1.0) / 2.0);
            case n -> (float)(var2 * var2);
            case f -> (float)(1.0 - (1.0 - var2) * (1.0 - var2));
            case c -> var2 < 0.5 ? (float)(2.0 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0) / 2.0);
            case K -> (float)(var2 * var2 * var2);
            case k -> (float)(1.0 - Math.pow(1.0 - var2, 3.0));
            case P -> var2 < 0.5 ? (float)(4.0 * var2 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 3.0) / 2.0);
            case q -> (float)Math.pow(var2, 4.0);
            case z -> (float)(1.0 - Math.pow(1.0 - var2, 4.0));
            case G -> var2 < 0.5 ? (float)(8.0 * Math.pow(var2, 4.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 4.0) / 2.0);
            case D -> (float)Math.pow(var2, 5.0);
            case Z -> (float)(1.0 - Math.pow(1.0 - var2, 5.0));
            case o -> var2 < 0.5 ? (float)(16.0 * Math.pow(var2, 5.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 5.0) / 2.0);
            case d -> var2 == 0.0 ? 0.0F : (float)Math.pow(2.0, 10.0 * var2 - 10.0);
            case M -> var2 == 1.0 ? 1.0F : (float)(1.0 - Math.pow(2.0, -10.0 * var2));
            case r -> var2 == 0.0 || var2 == 1.0
               ? (float)var2
               : (var2 < 0.5 ? (float)(Math.pow(2.0, 20.0 * var2 - 10.0) / 2.0) : (float)((2.0 - Math.pow(2.0, -20.0 * var2 + 10.0)) / 2.0));
            case A -> (float)(1.0 - Math.sqrt(1.0 - var2 * var2));
            case h -> (float)Math.sqrt(1.0 - Math.pow(var2 - 1.0, 2.0));
            case w -> var2 < 0.5
               ? (float)((1.0 - Math.sqrt(1.0 - Math.pow(2.0 * var2, 2.0))) / 2.0)
               : (float)((Math.sqrt(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0)) + 1.0) / 2.0);
            case m -> (float)(var8 * var2 * var2 * var2 - var4 * var2 * var2);
            case H -> (float)(1.0 + var8 * Math.pow(var2 - 1.0, 3.0) + var4 * Math.pow(var2 - 1.0, 2.0));
            case B -> var2 < 0.5
               ? (float)(Math.pow(2.0 * var2, 2.0) * ((var6 + 1.0) * 2.0 * var2 - var6) / 2.0)
               : (float)((Math.pow(2.0 * var2 - 2.0, 2.0) * ((var6 + 1.0) * (var2 * 2.0 - 2.0) + var6) + 2.0) / 2.0);
            case Y -> var2 != 0.0 && var2 != 1.0 ? (float)(-Math.pow(2.0, 10.0 * var2 - 10.0) * Math.sin((var2 * 10.0 - 10.75) * var10)) : (float)var2;
            case O -> var2 != 0.0 && var2 != 1.0 ? (float)(Math.pow(2.0, -10.0 * var2) * Math.sin((var2 * 10.0 - 0.75) * var10) + 1.0) : (float)var2;
            case g -> var2 == 0.0 || var2 == 1.0
               ? (float)var2
               : (
                  var2 < 0.5
                     ? (float)(-(Math.pow(2.0, 20.0 * var2 - 10.0) * Math.sin((20.0 * var2 - 11.125) * var12)) / 2.0)
                     : (float)(Math.pow(2.0, -20.0 * var2 + 10.0) * Math.sin((20.0 * var2 - 11.125) * var12) / 2.0 + 1.0)
               );
            case t -> (float)(1.0 - j(1.0 - var2));
            case S -> (float)j(var2);
            case U -> var2 < 0.5 ? (float)((1.0 - j(1.0 - 2.0 * var2)) / 2.0) : (float)((1.0 + j(2.0 * var2 - 1.0)) / 2.0);
         };
      }

      private static double j(double var0) {
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

   private static final class u {
      final float Q;
      final List<jc.a> T = new ArrayList<>();

      u(float var1) {
         this.Q = var1;
      }

      void E(int var1, float var2, float var3, jc.t var4) {
         int var5 = 0;

         while (var5 < this.T.size() && this.T.get(var5).tick < var1) {
            var5++;
         }

         if (var5 < this.T.size() && this.T.get(var5).tick == var1) {
            this.T.set(var5, new jc.a(var1, this.T.get(var5).arrive, var3, var4));
         } else {
            this.T.add(var5, new jc.a(var1, var2, var3, var4));
         }
      }
   }

   public enum z {
      J,
      L,
      K,
      W,
      I,
      h,
      v,
      Y;
   }
}
