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

public final class cx {
   public static final int j = 0;
   public static final int m = 1;
   public static final int s = 2;
   public static final int G = 3;
   public static final int c = 4;
   public static final int X = 5;
   public static final int f = 6;
   public static final int v = 7;
   public static final int q = 8;
   public static final int r = 9;
   public static final int T = 10;
   public static final int B = cx.q.values().length * 10;
   private static final String[] p = new String[]{"x", "y", "z", "pitch", "yaw", "roll", "bend", "scale", "spread", "hold"};
   private static final float[][] o = new float[][]{
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
   public final String K;
   public final String d;
   public final int V;
   public final int E;
   public final int n;
   public final int Z;
   public final boolean i;
   private final boolean P;
   private final cx.g[] g = new cx.g[B];
   private final Set<String> L = new TreeSet<>();

   private cx(String var1, String var2, int var3, int var4, int var5, boolean var6, int var7, boolean var8) {
      this.K = var1;
      this.d = var2;
      this.V = var3;
      this.E = var4;
      this.n = var5;
      this.i = var6;
      this.Z = var7;
      this.P = var8;
   }

   public static cx c(Reader var0, cx.w var1) {
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
         cx var12 = new cx(U(var2, "name"), U(var2, "author"), var5, var6, var9, (boolean)var7, var8, (boolean)var11);

         for (JsonElement var14 : var4.getAsJsonArray("moves")) {
            JsonObject var15 = var14.getAsJsonObject();
            int var16 = var15.get("tick").getAsInt();
            cx.a var17 = cx.a.q(var15.has("easing") ? var15.get("easing").getAsString() : "linear");
            if (var17 == null) {
               var12.L.add("easing " + var15.get("easing").getAsString());
               var17 = cx.a.Z;
            }

            int var18 = var15.has("turn") ? var15.get("turn").getAsInt() : 0;

            for (Entry var20 : var15.entrySet()) {
               if (((JsonElement)var20.getValue()).isJsonObject()) {
                  cx.q var21 = U((String)var20.getKey(), var3);
                  if (var21 == null) {
                     var12.L.add((String)var20.getKey());
                  } else {
                     for (Entry var23 : ((JsonElement)var20.getValue()).getAsJsonObject().entrySet()) {
                        int var24 = Arrays.asList(p).indexOf(var23.getKey());
                        if (var24 >= 0 && (var24 != 6 || S(var21)) && (var24 < 7 || var21 == cx.q.M || var24 == 7 && var21 == cx.q.V)) {
                           float var25 = ((JsonElement)var23.getValue()).getAsFloat();
                           if (Float.isNaN(var25)) {
                              throw new IllegalArgumentException("Keyframe value is NaN");
                           }

                           int var26 = (var24 < 3 || var24 > 6) && var24 != 8 ? 0 : 1;
                           if (var26 != 0 && var10 != 0) {
                              var25 *= (float) (Math.PI / 180.0);
                           }

                           float var27 = var26 != 0 && var18 != 0 ? var25 + (float)((Math.PI * 2) * var18) : var25;
                           var12.J(var21, var24).e(var16, var25, var27, var17);
                        } else {
                           var12.L.add((String)var20.getKey() + "." + (String)var23.getKey());
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

   private static String U(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonPrimitive() ? var0.get(var1).getAsString() : "";
   }

   private static cx.q U(String var0, int var1) {
      return switch (var0) {
         case "head" -> cx.q.P;
         case "torso" -> var1 < 3 ? cx.q.m : cx.q.E;
         case "body" -> cx.q.m;
         case "rightArm" -> cx.q.Q;
         case "leftArm" -> cx.q.q;
         case "rightLeg" -> cx.q.l;
         case "leftLeg" -> cx.q.A;
         case "prop" -> cx.q.M;
         case "tool" -> cx.q.V;
         default -> null;
      };
   }

   public static boolean S(cx.q var0) {
      return var0 == cx.q.Q || var0 == cx.q.q || var0 == cx.q.l || var0 == cx.q.A;
   }

   private cx.g J(cx.q var1, int var2) {
      int var3 = var1.ordinal() * 10 + var2;
      if (this.g[var3] == null) {
         this.g[var3] = new cx.g(var2 < 3 ? o[var1.ordinal()][var2] : (var2 == 7 ? 1.0F : 0.0F));
      }

      return this.g[var3];
   }

   public Set<String> g() {
      return Set.copyOf(this.L);
   }

   public boolean J(cx.q var1, int var2) {
      return this.g[var1.ordinal() * 10 + var2] != null;
   }

   public int x(int var1) {
      return this.i && var1 > this.E ? this.Z + (var1 - this.E - 1) % (this.E - this.Z + 1) : var1;
   }

   public boolean U(int var1) {
      return this.i && var1 > this.E;
   }

   public float L(int var1, float var2) {
      return !this.i ? 0.0F : (this.x(var1) + var2 - this.Z) / (this.E - this.Z + 1);
   }

   public boolean b(int var1) {
      return !this.i && var1 >= this.n;
   }

   public void A(int var1, float var2, float[] var3) {
      int var4 = this.x(var1);
      boolean var5 = this.U(var1);

      for (int var6 = 0; var6 < B; var6++) {
         var3[var6] = this.g[var6] == null ? Float.NaN : this.c(this.g[var6], var4, var2, var5);
      }
   }

   private float c(cx.g var1, int var2, float var3, boolean var4) {
      List var5 = var1.m;
      int var6 = this.E - this.Z + 1;
      cx.b var7 = null;
      cx.b var8 = null;
      int var9 = 0;
      int var10 = 0;

      for (int var11 = var5.size() - 1; var11 >= 0; var11--) {
         cx.b var12 = (cx.b)var5.get(var11);
         if (var12.tick <= var2 && (!this.i || var12.tick <= this.E) && (!var4 || var12.tick >= this.Z)) {
            var7 = var12;
            var9 = var12.tick;
            break;
         }
      }

      for (cx.b var15 : var5) {
         if (var15.tick > var2 && (!this.i || var15.tick <= this.E)) {
            var8 = var15;
            var10 = var15.tick;
            break;
         }
      }

      if (this.i && var7 == null && var4) {
         var7 = this.n(var5);
         if (var7 != null) {
            var9 = var7.tick - var6;
         }
      }

      if (this.i && var8 == null) {
         var8 = this.R(var5);
         if (var8 != null) {
            var10 = var8.tick + var6;
         }
      }

      if (var7 == null) {
         var7 = new cx.b(this.V, var1.x, var1.x, cx.a.Z);
         var9 = this.V;
      }

      if (var8 == null) {
         return var7.depart;
      }

      if (var10 <= var9) {
         return var7.depart;
      }

      float var14 = Math.clamp((var2 + var3 - var9) / (var10 - var9), 0.0F, 1.0F);
      float var16 = (this.P ? var8.ease : var7.ease).g(var14);
      return var7.depart + (var8.arrive - var7.depart) * var16;
   }

   private cx.b R(List<cx.b> var1) {
      for (cx.b var3 : var1) {
         if (var3.tick >= this.Z && var3.tick <= this.E) {
            return var3;
         }
      }

      return null;
   }

   private cx.b n(List<cx.b> var1) {
      for (int var2 = var1.size() - 1; var2 >= 0; var2--) {
         cx.b var3 = (cx.b)var1.get(var2);
         if (var3.tick >= this.Z && var3.tick <= this.E) {
            return var3;
         }
      }

      return null;
   }

   public enum a {
      Z,
      q,
      f,
      B,
      n,
      U,
      V,
      S,
      w,
      A,
      g,
      k,
      W,
      M,
      d,
      O,
      r,
      v,
      y,
      m,
      L,
      Y,
      c,
      b,
      N,
      e,
      K,
      E,
      H,
      R,
      u,
      s;

      static cx.a q(String var0) {
         String var1 = var0.toUpperCase(Locale.ROOT).replace("_", "");
         if (var1.equals("STEP")) {
            return q;
         }

         for (cx.a var5 : values()) {
            if (var5.name().equals(var1)) {
               return var5;
            }
         }

         return null;
      }

      public float g(float var1) {
         double var2 = var1;
         double var4 = 1.70158;
         double var6 = var4 * 1.525;
         double var8 = var4 + 1.0;
         double var10 = Math.PI * 2.0 / 3.0;
         double var12 = Math.PI * 4.0 / 9.0;

         return switch (this) {
            case Z -> (float)var2;
            case q -> 0.0F;
            case f -> (float)(1.0 - Math.cos(var2 * Math.PI / 2.0));
            case B -> (float)Math.sin(var2 * Math.PI / 2.0);
            case n -> (float)(-(Math.cos(Math.PI * var2) - 1.0) / 2.0);
            case U -> (float)(var2 * var2);
            case V -> (float)(1.0 - (1.0 - var2) * (1.0 - var2));
            case S -> var2 < 0.5 ? (float)(2.0 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0) / 2.0);
            case w -> (float)(var2 * var2 * var2);
            case A -> (float)(1.0 - Math.pow(1.0 - var2, 3.0));
            case g -> var2 < 0.5 ? (float)(4.0 * var2 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 3.0) / 2.0);
            case k -> (float)Math.pow(var2, 4.0);
            case W -> (float)(1.0 - Math.pow(1.0 - var2, 4.0));
            case M -> var2 < 0.5 ? (float)(8.0 * Math.pow(var2, 4.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 4.0) / 2.0);
            case d -> (float)Math.pow(var2, 5.0);
            case O -> (float)(1.0 - Math.pow(1.0 - var2, 5.0));
            case r -> var2 < 0.5 ? (float)(16.0 * Math.pow(var2, 5.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 5.0) / 2.0);
            case v -> var2 == 0.0 ? 0.0F : (float)Math.pow(2.0, 10.0 * var2 - 10.0);
            case y -> var2 == 1.0 ? 1.0F : (float)(1.0 - Math.pow(2.0, -10.0 * var2));
            case m -> var2 == 0.0 || var2 == 1.0
               ? (float)var2
               : (var2 < 0.5 ? (float)(Math.pow(2.0, 20.0 * var2 - 10.0) / 2.0) : (float)((2.0 - Math.pow(2.0, -20.0 * var2 + 10.0)) / 2.0));
            case L -> (float)(1.0 - Math.sqrt(1.0 - var2 * var2));
            case Y -> (float)Math.sqrt(1.0 - Math.pow(var2 - 1.0, 2.0));
            case c -> var2 < 0.5
               ? (float)((1.0 - Math.sqrt(1.0 - Math.pow(2.0 * var2, 2.0))) / 2.0)
               : (float)((Math.sqrt(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0)) + 1.0) / 2.0);
            case b -> (float)(var8 * var2 * var2 * var2 - var4 * var2 * var2);
            case N -> (float)(1.0 + var8 * Math.pow(var2 - 1.0, 3.0) + var4 * Math.pow(var2 - 1.0, 2.0));
            case e -> var2 < 0.5
               ? (float)(Math.pow(2.0 * var2, 2.0) * ((var6 + 1.0) * 2.0 * var2 - var6) / 2.0)
               : (float)((Math.pow(2.0 * var2 - 2.0, 2.0) * ((var6 + 1.0) * (var2 * 2.0 - 2.0) + var6) + 2.0) / 2.0);
            case K -> var2 != 0.0 && var2 != 1.0 ? (float)(-Math.pow(2.0, 10.0 * var2 - 10.0) * Math.sin((var2 * 10.0 - 10.75) * var10)) : (float)var2;
            case E -> var2 != 0.0 && var2 != 1.0 ? (float)(Math.pow(2.0, -10.0 * var2) * Math.sin((var2 * 10.0 - 0.75) * var10) + 1.0) : (float)var2;
            case H -> var2 == 0.0 || var2 == 1.0
               ? (float)var2
               : (
                  var2 < 0.5
                     ? (float)(-(Math.pow(2.0, 20.0 * var2 - 10.0) * Math.sin((20.0 * var2 - 11.125) * var12)) / 2.0)
                     : (float)(Math.pow(2.0, -20.0 * var2 + 10.0) * Math.sin((20.0 * var2 - 11.125) * var12) / 2.0 + 1.0)
               );
            case R -> (float)(1.0 - B(1.0 - var2));
            case u -> (float)B(var2);
            case s -> var2 < 0.5 ? (float)((1.0 - B(1.0 - 2.0 * var2)) / 2.0) : (float)((1.0 + B(2.0 * var2 - 1.0)) / 2.0);
         };
      }

      private static double B(double var0) {
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

   private record b(int tick, float arrive, float depart, cx.a ease) {
   }

   private static final class g {
      final float x;
      final List<cx.b> m = new ArrayList<>();

      g(float var1) {
         this.x = var1;
      }

      void e(int var1, float var2, float var3, cx.a var4) {
         int var5 = 0;

         while (var5 < this.m.size() && this.m.get(var5).tick < var1) {
            var5++;
         }

         if (var5 < this.m.size() && this.m.get(var5).tick == var1) {
            this.m.set(var5, new cx.b(var1, this.m.get(var5).arrive, var3, var4));
         } else {
            this.m.add(var5, new cx.b(var1, var2, var3, var4));
         }
      }
   }

   public enum q {
      P,
      E,
      Q,
      q,
      l,
      A,
      m,
      M,
      V;
   }

   public record w(int returnTick, int endTick) {
   }
}
