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

public final class uy {
   public static final int J = 0;
   public static final int q = 1;
   public static final int p = 2;
   public static final int N = 3;
   public static final int b = 4;
   public static final int F = 5;
   public static final int U = 6;
   public static final int i = 7;
   public static final int l = 8;
   public static final int u = 9;
   public static final int z = 10;
   public static final int Y = uy.q.values().length * 10;
   private static final String[] T = new String[]{"x", "y", "z", "pitch", "yaw", "roll", "bend", "scale", "spread", "hold"};
   private static final float[][] n = new float[][]{
      {0.0F, 0.0F, 0.0F},
      {0.0F, 0.0F, 0.0F},
      {-5.0F, 2.0F, 0.0F},
      {5.0F, 2.0F, 0.0F},
      {-1.9F, 12.0F, 0.1F},
      {1.9F, 12.0F, 0.1F},
      {0.0F, 0.0F, 0.0F},
      {0.0F, 0.0F, 0.0F}
   };
   public final String j;
   public final String v;
   public final int d;
   public final int k;
   public final int h;
   public final int P;
   public final boolean K;
   private final boolean o;
   private final uy.f[] X = new uy.f[Y];
   private final Set<String> g = new TreeSet<>();

   private uy(String var1, String var2, int var3, int var4, int var5, boolean var6, int var7, boolean var8) {
      this.j = var1;
      this.v = var2;
      this.d = var3;
      this.k = var4;
      this.h = var5;
      this.K = var6;
      this.P = var7;
      this.o = var8;
   }

   public static uy B(Reader var0, uy.h var1) {
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
         uy var12 = new uy(b(var2, "name"), b(var2, "author"), var5, var6, var9, (boolean)var7, var8, (boolean)var11);

         for (JsonElement var14 : var4.getAsJsonArray("moves")) {
            JsonObject var15 = var14.getAsJsonObject();
            int var16 = var15.get("tick").getAsInt();
            uy.d var17 = uy.d.O(var15.has("easing") ? var15.get("easing").getAsString() : "linear");
            if (var17 == null) {
               var12.g.add("easing " + var15.get("easing").getAsString());
               var17 = uy.d.B;
            }

            int var18 = var15.has("turn") ? var15.get("turn").getAsInt() : 0;

            for (Entry var20 : var15.entrySet()) {
               if (((JsonElement)var20.getValue()).isJsonObject()) {
                  uy.q var21 = k((String)var20.getKey(), var3);
                  if (var21 == null) {
                     var12.g.add((String)var20.getKey());
                  } else {
                     for (Entry var23 : ((JsonElement)var20.getValue()).getAsJsonObject().entrySet()) {
                        int var24 = Arrays.asList(T).indexOf(var23.getKey());
                        if (var24 >= 0 && (var24 != 6 || B(var21)) && (var24 < 7 || var21 == uy.q.u)) {
                           float var25 = ((JsonElement)var23.getValue()).getAsFloat();
                           if (Float.isNaN(var25)) {
                              throw new IllegalArgumentException("Keyframe value is NaN");
                           }

                           int var26 = (var24 < 3 || var24 > 6) && var24 != 8 ? 0 : 1;
                           if (var26 != 0 && var10 != 0) {
                              var25 *= (float) (Math.PI / 180.0);
                           }

                           float var27 = var26 != 0 && var18 != 0 ? var25 + (float)((Math.PI * 2) * var18) : var25;
                           var12.D(var21, var24).o(var16, var25, var27, var17);
                        } else {
                           var12.g.add((String)var20.getKey() + "." + (String)var23.getKey());
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

   private static String b(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonPrimitive() ? var0.get(var1).getAsString() : "";
   }

   private static uy.q k(String var0, int var1) {
      return switch (var0) {
         case "head" -> uy.q.S;
         case "torso" -> var1 < 3 ? uy.q.f : uy.q.W;
         case "body" -> uy.q.f;
         case "rightArm" -> uy.q.x;
         case "leftArm" -> uy.q.J;
         case "rightLeg" -> uy.q.G;
         case "leftLeg" -> uy.q.C;
         case "prop" -> uy.q.u;
         default -> null;
      };
   }

   public static boolean B(uy.q var0) {
      return var0 == uy.q.x || var0 == uy.q.J || var0 == uy.q.G || var0 == uy.q.C;
   }

   private uy.f D(uy.q var1, int var2) {
      int var3 = var1.ordinal() * 10 + var2;
      if (this.X[var3] == null) {
         this.X[var3] = new uy.f(var2 < 3 ? n[var1.ordinal()][var2] : (var2 == 7 ? 1.0F : 0.0F));
      }

      return this.X[var3];
   }

   public Set<String> E() {
      return Set.copyOf(this.g);
   }

   public boolean s(uy.q var1, int var2) {
      return this.X[var1.ordinal() * 10 + var2] != null;
   }

   public int Y(int var1) {
      return this.K && var1 > this.k ? this.P + (var1 - this.k - 1) % (this.k - this.P + 1) : var1;
   }

   public boolean A(int var1) {
      return this.K && var1 > this.k;
   }

   public float W(int var1, float var2) {
      return !this.K ? 0.0F : (this.Y(var1) + var2 - this.P) / (this.k - this.P + 1);
   }

   public boolean p(int var1) {
      return !this.K && var1 >= this.h;
   }

   public void p(int var1, float var2, float[] var3) {
      int var4 = this.Y(var1);
      boolean var5 = this.A(var1);

      for (int var6 = 0; var6 < Y; var6++) {
         var3[var6] = this.X[var6] == null ? Float.NaN : this.U(this.X[var6], var4, var2, var5);
      }
   }

   private float U(uy.f var1, int var2, float var3, boolean var4) {
      List var5 = var1.f;
      int var6 = this.k - this.P + 1;
      uy.y var7 = null;
      uy.y var8 = null;
      int var9 = 0;
      int var10 = 0;

      for (int var11 = var5.size() - 1; var11 >= 0; var11--) {
         uy.y var12 = (uy.y)var5.get(var11);
         if (var12.tick <= var2 && (!this.K || var12.tick <= this.k) && (!var4 || var12.tick >= this.P)) {
            var7 = var12;
            var9 = var12.tick;
            break;
         }
      }

      for (uy.y var15 : var5) {
         if (var15.tick > var2 && (!this.K || var15.tick <= this.k)) {
            var8 = var15;
            var10 = var15.tick;
            break;
         }
      }

      if (this.K && var7 == null && var4) {
         var7 = this.E(var5);
         if (var7 != null) {
            var9 = var7.tick - var6;
         }
      }

      if (this.K && var8 == null) {
         var8 = this.C(var5);
         if (var8 != null) {
            var10 = var8.tick + var6;
         }
      }

      if (var7 == null) {
         var7 = new uy.y(this.d, var1.c, var1.c, uy.d.B);
         var9 = this.d;
      }

      if (var8 == null) {
         return var7.depart;
      }

      if (var10 <= var9) {
         return var7.depart;
      }

      float var14 = Math.clamp((var2 + var3 - var9) / (var10 - var9), 0.0F, 1.0F);
      float var16 = (this.o ? var8.ease : var7.ease).d(var14);
      return var7.depart + (var8.arrive - var7.depart) * var16;
   }

   private uy.y C(List<uy.y> var1) {
      for (uy.y var3 : var1) {
         if (var3.tick >= this.P && var3.tick <= this.k) {
            return var3;
         }
      }

      return null;
   }

   private uy.y E(List<uy.y> var1) {
      for (int var2 = var1.size() - 1; var2 >= 0; var2--) {
         uy.y var3 = (uy.y)var1.get(var2);
         if (var3.tick >= this.P && var3.tick <= this.k) {
            return var3;
         }
      }

      return null;
   }

   public enum d {
      B,
      b,
      y,
      z,
      R,
      E,
      Y,
      M,
      h,
      o,
      s,
      u,
      C,
      N,
      f,
      x,
      m,
      e,
      T,
      X,
      U,
      v,
      P,
      J,
      I,
      r,
      l,
      K,
      c,
      q,
      L,
      F;

      static uy.d O(String var0) {
         String var1 = var0.toUpperCase(Locale.ROOT).replace("_", "");
         if (var1.equals("STEP")) {
            return b;
         }

         for (uy.d var5 : values()) {
            if (var5.name().equals(var1)) {
               return var5;
            }
         }

         return null;
      }

      public float d(float var1) {
         double var2 = var1;
         double var4 = 1.70158;
         double var6 = var4 * 1.525;
         double var8 = var4 + 1.0;
         double var10 = Math.PI * 2.0 / 3.0;
         double var12 = Math.PI * 4.0 / 9.0;

         return switch (this) {
            case B -> (float)var2;
            case b -> 0.0F;
            case y -> (float)(1.0 - Math.cos(var2 * Math.PI / 2.0));
            case z -> (float)Math.sin(var2 * Math.PI / 2.0);
            case R -> (float)(-(Math.cos(Math.PI * var2) - 1.0) / 2.0);
            case E -> (float)(var2 * var2);
            case Y -> (float)(1.0 - (1.0 - var2) * (1.0 - var2));
            case M -> var2 < 0.5 ? (float)(2.0 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0) / 2.0);
            case h -> (float)(var2 * var2 * var2);
            case o -> (float)(1.0 - Math.pow(1.0 - var2, 3.0));
            case s -> var2 < 0.5 ? (float)(4.0 * var2 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 3.0) / 2.0);
            case u -> (float)Math.pow(var2, 4.0);
            case C -> (float)(1.0 - Math.pow(1.0 - var2, 4.0));
            case N -> var2 < 0.5 ? (float)(8.0 * Math.pow(var2, 4.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 4.0) / 2.0);
            case f -> (float)Math.pow(var2, 5.0);
            case x -> (float)(1.0 - Math.pow(1.0 - var2, 5.0));
            case m -> var2 < 0.5 ? (float)(16.0 * Math.pow(var2, 5.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 5.0) / 2.0);
            case e -> var2 == 0.0 ? 0.0F : (float)Math.pow(2.0, 10.0 * var2 - 10.0);
            case T -> var2 == 1.0 ? 1.0F : (float)(1.0 - Math.pow(2.0, -10.0 * var2));
            case X -> var2 == 0.0 || var2 == 1.0
               ? (float)var2
               : (var2 < 0.5 ? (float)(Math.pow(2.0, 20.0 * var2 - 10.0) / 2.0) : (float)((2.0 - Math.pow(2.0, -20.0 * var2 + 10.0)) / 2.0));
            case U -> (float)(1.0 - Math.sqrt(1.0 - var2 * var2));
            case v -> (float)Math.sqrt(1.0 - Math.pow(var2 - 1.0, 2.0));
            case P -> var2 < 0.5
               ? (float)((1.0 - Math.sqrt(1.0 - Math.pow(2.0 * var2, 2.0))) / 2.0)
               : (float)((Math.sqrt(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0)) + 1.0) / 2.0);
            case J -> (float)(var8 * var2 * var2 * var2 - var4 * var2 * var2);
            case I -> (float)(1.0 + var8 * Math.pow(var2 - 1.0, 3.0) + var4 * Math.pow(var2 - 1.0, 2.0));
            case r -> var2 < 0.5
               ? (float)(Math.pow(2.0 * var2, 2.0) * ((var6 + 1.0) * 2.0 * var2 - var6) / 2.0)
               : (float)((Math.pow(2.0 * var2 - 2.0, 2.0) * ((var6 + 1.0) * (var2 * 2.0 - 2.0) + var6) + 2.0) / 2.0);
            case l -> var2 != 0.0 && var2 != 1.0 ? (float)(-Math.pow(2.0, 10.0 * var2 - 10.0) * Math.sin((var2 * 10.0 - 10.75) * var10)) : (float)var2;
            case K -> var2 != 0.0 && var2 != 1.0 ? (float)(Math.pow(2.0, -10.0 * var2) * Math.sin((var2 * 10.0 - 0.75) * var10) + 1.0) : (float)var2;
            case c -> var2 == 0.0 || var2 == 1.0
               ? (float)var2
               : (
                  var2 < 0.5
                     ? (float)(-(Math.pow(2.0, 20.0 * var2 - 10.0) * Math.sin((20.0 * var2 - 11.125) * var12)) / 2.0)
                     : (float)(Math.pow(2.0, -20.0 * var2 + 10.0) * Math.sin((20.0 * var2 - 11.125) * var12) / 2.0 + 1.0)
               );
            case q -> (float)(1.0 - W(1.0 - var2));
            case L -> (float)W(var2);
            case F -> var2 < 0.5 ? (float)((1.0 - W(1.0 - 2.0 * var2)) / 2.0) : (float)((1.0 + W(2.0 * var2 - 1.0)) / 2.0);
         };
      }

      private static double W(double var0) {
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

   private static final class f {
      final float c;
      final List<uy.y> f = new ArrayList<>();

      f(float var1) {
         this.c = var1;
      }

      void o(int var1, float var2, float var3, uy.d var4) {
         int var5 = 0;

         while (var5 < this.f.size() && this.f.get(var5).tick < var1) {
            var5++;
         }

         if (var5 < this.f.size() && this.f.get(var5).tick == var1) {
            this.f.set(var5, new uy.y(var1, this.f.get(var5).arrive, var3, var4));
         } else {
            this.f.add(var5, new uy.y(var1, var2, var3, var4));
         }
      }
   }

   public record h(int returnTick, int endTick) {
   }

   public enum q {
      S,
      W,
      x,
      J,
      G,
      C,
      f,
      u;
   }

   private record y(int tick, float arrive, float depart, uy.d ease) {
   }
}
