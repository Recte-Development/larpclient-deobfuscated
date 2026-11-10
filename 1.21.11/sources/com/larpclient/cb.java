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

public final class cb {
   public static final int y = 0;
   public static final int H = 1;
   public static final int h = 2;
   public static final int T = 3;
   public static final int J = 4;
   public static final int l = 5;
   public static final int W = 6;
   public static final int L = 7;
   public static final int P = 8;
   public static final int p = 9;
   public static final int q = 10;
   public static final int w = cb.x.values().length * 10;
   private static final String[] m = new String[]{"x", "y", "z", "pitch", "yaw", "roll", "bend", "scale", "spread", "hold"};
   private static final float[][] G = new float[][]{
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
   public final String F;
   public final String e;
   public final int E;
   public final int K;
   public final int n;
   public final int Y;
   public final boolean M;
   private final boolean u;
   private final cb.i[] t = new cb.i[w];
   private final Set<String> s = new TreeSet<>();

   private cb(String var1, String var2, int var3, int var4, int var5, boolean var6, int var7, boolean var8) {
      this.F = var1;
      this.e = var2;
      this.E = var3;
      this.K = var4;
      this.n = var5;
      this.M = var6;
      this.Y = var7;
      this.u = var8;
   }

   public static cb A(Reader var0, cb.p var1) {
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
         cb var12 = new cb(g(var2, "name"), g(var2, "author"), var5, var6, var9, (boolean)var7, var8, (boolean)var11);

         for (JsonElement var14 : var4.getAsJsonArray("moves")) {
            JsonObject var15 = var14.getAsJsonObject();
            int var16 = var15.get("tick").getAsInt();
            cb.a var17 = cb.a.z(var15.has("easing") ? var15.get("easing").getAsString() : "linear");
            if (var17 == null) {
               var12.s.add("easing " + var15.get("easing").getAsString());
               var17 = cb.a.k;
            }

            int var18 = var15.has("turn") ? var15.get("turn").getAsInt() : 0;

            for (Entry var20 : var15.entrySet()) {
               if (((JsonElement)var20.getValue()).isJsonObject()) {
                  cb.x var21 = i((String)var20.getKey(), var3);
                  if (var21 == null) {
                     var12.s.add((String)var20.getKey());
                  } else {
                     for (Entry var23 : ((JsonElement)var20.getValue()).getAsJsonObject().entrySet()) {
                        int var24 = Arrays.asList(m).indexOf(var23.getKey());
                        if (var24 >= 0 && (var24 != 6 || w(var21)) && (var24 < 7 || var21 == cb.x.B || var24 == 7 && var21 == cb.x.z)) {
                           float var25 = ((JsonElement)var23.getValue()).getAsFloat();
                           if (Float.isNaN(var25)) {
                              throw new IllegalArgumentException("Keyframe value is NaN");
                           }

                           int var26 = (var24 < 3 || var24 > 6) && var24 != 8 ? 0 : 1;
                           if (var26 != 0 && var10 != 0) {
                              var25 *= (float) (Math.PI / 180.0);
                           }

                           float var27 = var26 != 0 && var18 != 0 ? var25 + (float)((Math.PI * 2) * var18) : var25;
                           var12.P(var21, var24).I(var16, var25, var27, var17);
                        } else {
                           var12.s.add((String)var20.getKey() + "." + (String)var23.getKey());
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

   private static String g(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonPrimitive() ? var0.get(var1).getAsString() : "";
   }

   private static cb.x i(String var0, int var1) {
      return switch (var0) {
         case "head" -> cb.x.y;
         case "torso" -> var1 < 3 ? cb.x.D : cb.x.g;
         case "body" -> cb.x.D;
         case "rightArm" -> cb.x.j;
         case "leftArm" -> cb.x.m;
         case "rightLeg" -> cb.x.k;
         case "leftLeg" -> cb.x.M;
         case "prop" -> cb.x.B;
         case "tool" -> cb.x.z;
         default -> null;
      };
   }

   public static boolean w(cb.x var0) {
      return var0 == cb.x.j || var0 == cb.x.m || var0 == cb.x.k || var0 == cb.x.M;
   }

   private cb.i P(cb.x var1, int var2) {
      int var3 = var1.ordinal() * 10 + var2;
      if (this.t[var3] == null) {
         this.t[var3] = new cb.i(var2 < 3 ? G[var1.ordinal()][var2] : (var2 == 7 ? 1.0F : 0.0F));
      }

      return this.t[var3];
   }

   public Set<String> Q() {
      return Set.copyOf(this.s);
   }

   public boolean t(cb.x var1, int var2) {
      return this.t[var1.ordinal() * 10 + var2] != null;
   }

   public int v(int var1) {
      return this.M && var1 > this.K ? this.Y + (var1 - this.K - 1) % (this.K - this.Y + 1) : var1;
   }

   public boolean k(int var1) {
      return this.M && var1 > this.K;
   }

   public float K(int var1, float var2) {
      return !this.M ? 0.0F : (this.v(var1) + var2 - this.Y) / (this.K - this.Y + 1);
   }

   public boolean d(int var1) {
      return !this.M && var1 >= this.n;
   }

   public void z(int var1, float var2, float[] var3) {
      int var4 = this.v(var1);
      boolean var5 = this.k(var1);

      for (int var6 = 0; var6 < w; var6++) {
         var3[var6] = this.t[var6] == null ? Float.NaN : this.v(this.t[var6], var4, var2, var5);
      }
   }

   private float v(cb.i var1, int var2, float var3, boolean var4) {
      List var5 = var1.r;
      int var6 = this.K - this.Y + 1;
      cb.q var7 = null;
      cb.q var8 = null;
      int var9 = 0;
      int var10 = 0;

      for (int var11 = var5.size() - 1; var11 >= 0; var11--) {
         cb.q var12 = (cb.q)var5.get(var11);
         if (var12.tick <= var2 && (!this.M || var12.tick <= this.K) && (!var4 || var12.tick >= this.Y)) {
            var7 = var12;
            var9 = var12.tick;
            break;
         }
      }

      for (cb.q var15 : var5) {
         if (var15.tick > var2 && (!this.M || var15.tick <= this.K)) {
            var8 = var15;
            var10 = var15.tick;
            break;
         }
      }

      if (this.M && var7 == null && var4) {
         var7 = this.O(var5);
         if (var7 != null) {
            var9 = var7.tick - var6;
         }
      }

      if (this.M && var8 == null) {
         var8 = this.i(var5);
         if (var8 != null) {
            var10 = var8.tick + var6;
         }
      }

      if (var7 == null) {
         var7 = new cb.q(this.E, var1.m, var1.m, cb.a.k);
         var9 = this.E;
      }

      if (var8 == null) {
         return var7.depart;
      }

      if (var10 <= var9) {
         return var7.depart;
      }

      float var14 = Math.clamp((var2 + var3 - var9) / (var10 - var9), 0.0F, 1.0F);
      float var16 = (this.u ? var8.ease : var7.ease).E(var14);
      return var7.depart + (var8.arrive - var7.depart) * var16;
   }

   private cb.q i(List<cb.q> var1) {
      for (cb.q var3 : var1) {
         if (var3.tick >= this.Y && var3.tick <= this.K) {
            return var3;
         }
      }

      return null;
   }

   private cb.q O(List<cb.q> var1) {
      for (int var2 = var1.size() - 1; var2 >= 0; var2--) {
         cb.q var3 = (cb.q)var1.get(var2);
         if (var3.tick >= this.Y && var3.tick <= this.K) {
            return var3;
         }
      }

      return null;
   }

   public enum a {
      k,
      Y,
      Z,
      I,
      v,
      T,
      h,
      U,
      K,
      w,
      V,
      b,
      F,
      P,
      r,
      p,
      l,
      u,
      i,
      A,
      C,
      t,
      e,
      D,
      d,
      S,
      Q,
      M,
      s,
      y,
      G,
      X;

      static cb.a z(String var0) {
         String var1 = var0.toUpperCase(Locale.ROOT).replace("_", "");
         if (var1.equals("STEP")) {
            return Y;
         }

         for (cb.a var5 : values()) {
            if (var5.name().equals(var1)) {
               return var5;
            }
         }

         return null;
      }

      public float E(float var1) {
         double var2 = var1;
         double var4 = 1.70158;
         double var6 = var4 * 1.525;
         double var8 = var4 + 1.0;
         double var10 = Math.PI * 2.0 / 3.0;
         double var12 = Math.PI * 4.0 / 9.0;

         return switch (this) {
            case k -> (float)var2;
            case Y -> 0.0F;
            case Z -> (float)(1.0 - Math.cos(var2 * Math.PI / 2.0));
            case I -> (float)Math.sin(var2 * Math.PI / 2.0);
            case v -> (float)(-(Math.cos(Math.PI * var2) - 1.0) / 2.0);
            case T -> (float)(var2 * var2);
            case h -> (float)(1.0 - (1.0 - var2) * (1.0 - var2));
            case U -> var2 < 0.5 ? (float)(2.0 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0) / 2.0);
            case K -> (float)(var2 * var2 * var2);
            case w -> (float)(1.0 - Math.pow(1.0 - var2, 3.0));
            case V -> var2 < 0.5 ? (float)(4.0 * var2 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 3.0) / 2.0);
            case b -> (float)Math.pow(var2, 4.0);
            case F -> (float)(1.0 - Math.pow(1.0 - var2, 4.0));
            case P -> var2 < 0.5 ? (float)(8.0 * Math.pow(var2, 4.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 4.0) / 2.0);
            case r -> (float)Math.pow(var2, 5.0);
            case p -> (float)(1.0 - Math.pow(1.0 - var2, 5.0));
            case l -> var2 < 0.5 ? (float)(16.0 * Math.pow(var2, 5.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 5.0) / 2.0);
            case u -> var2 == 0.0 ? 0.0F : (float)Math.pow(2.0, 10.0 * var2 - 10.0);
            case i -> var2 == 1.0 ? 1.0F : (float)(1.0 - Math.pow(2.0, -10.0 * var2));
            case A -> var2 == 0.0 || var2 == 1.0
               ? (float)var2
               : (var2 < 0.5 ? (float)(Math.pow(2.0, 20.0 * var2 - 10.0) / 2.0) : (float)((2.0 - Math.pow(2.0, -20.0 * var2 + 10.0)) / 2.0));
            case C -> (float)(1.0 - Math.sqrt(1.0 - var2 * var2));
            case t -> (float)Math.sqrt(1.0 - Math.pow(var2 - 1.0, 2.0));
            case e -> var2 < 0.5
               ? (float)((1.0 - Math.sqrt(1.0 - Math.pow(2.0 * var2, 2.0))) / 2.0)
               : (float)((Math.sqrt(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0)) + 1.0) / 2.0);
            case D -> (float)(var8 * var2 * var2 * var2 - var4 * var2 * var2);
            case d -> (float)(1.0 + var8 * Math.pow(var2 - 1.0, 3.0) + var4 * Math.pow(var2 - 1.0, 2.0));
            case S -> var2 < 0.5
               ? (float)(Math.pow(2.0 * var2, 2.0) * ((var6 + 1.0) * 2.0 * var2 - var6) / 2.0)
               : (float)((Math.pow(2.0 * var2 - 2.0, 2.0) * ((var6 + 1.0) * (var2 * 2.0 - 2.0) + var6) + 2.0) / 2.0);
            case Q -> var2 != 0.0 && var2 != 1.0 ? (float)(-Math.pow(2.0, 10.0 * var2 - 10.0) * Math.sin((var2 * 10.0 - 10.75) * var10)) : (float)var2;
            case M -> var2 != 0.0 && var2 != 1.0 ? (float)(Math.pow(2.0, -10.0 * var2) * Math.sin((var2 * 10.0 - 0.75) * var10) + 1.0) : (float)var2;
            case s -> var2 == 0.0 || var2 == 1.0
               ? (float)var2
               : (
                  var2 < 0.5
                     ? (float)(-(Math.pow(2.0, 20.0 * var2 - 10.0) * Math.sin((20.0 * var2 - 11.125) * var12)) / 2.0)
                     : (float)(Math.pow(2.0, -20.0 * var2 + 10.0) * Math.sin((20.0 * var2 - 11.125) * var12) / 2.0 + 1.0)
               );
            case y -> (float)(1.0 - e(1.0 - var2));
            case G -> (float)e(var2);
            case X -> var2 < 0.5 ? (float)((1.0 - e(1.0 - 2.0 * var2)) / 2.0) : (float)((1.0 + e(2.0 * var2 - 1.0)) / 2.0);
         };
      }

      private static double e(double var0) {
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

   private static final class i {
      final float m;
      final List<cb.q> r = new ArrayList<>();

      i(float var1) {
         this.m = var1;
      }

      void I(int var1, float var2, float var3, cb.a var4) {
         int var5 = 0;

         while (var5 < this.r.size() && this.r.get(var5).tick < var1) {
            var5++;
         }

         if (var5 < this.r.size() && this.r.get(var5).tick == var1) {
            this.r.set(var5, new cb.q(var1, this.r.get(var5).arrive, var3, var4));
         } else {
            this.r.add(var5, new cb.q(var1, var2, var3, var4));
         }
      }
   }

   public record p(int returnTick, int endTick) {
   }

   private record q(int tick, float arrive, float depart, cb.a ease) {
   }

   public enum x {
      y,
      g,
      j,
      m,
      k,
      M,
      D,
      B,
      z;
   }
}
