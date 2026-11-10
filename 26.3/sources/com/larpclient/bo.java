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

public final class bo {
   public static final int m = 0;
   public static final int L = 1;
   public static final int t = 2;
   public static final int d = 3;
   public static final int c = 4;
   public static final int q = 5;
   public static final int Z = 6;
   public static final int K = 7;
   public static final int T = 8;
   public static final int i = 9;
   public static final int k = 10;
   public static final int s = bo.i.values().length * 10;
   private static final String[] G = new String[]{"x", "y", "z", "pitch", "yaw", "roll", "bend", "scale", "spread", "hold"};
   private static final float[][] j = new float[][]{
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
   public final String O;
   public final String l;
   public final int o;
   public final int g;
   public final int A;
   public final int B;
   public final boolean V;
   private final boolean X;
   private final bo.a[] h = new bo.a[s];
   private final Set<String> J = new TreeSet<>();

   private bo(String var1, String var2, int var3, int var4, int var5, boolean var6, int var7, boolean var8) {
      this.O = var1;
      this.l = var2;
      this.o = var3;
      this.g = var4;
      this.A = var5;
      this.V = var6;
      this.B = var7;
      this.X = var8;
   }

   public static bo k(Reader var0, bo.d var1) {
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
         bo var12 = new bo(s(var2, "name"), s(var2, "author"), var5, var6, var9, (boolean)var7, var8, (boolean)var11);

         for (JsonElement var14 : var4.getAsJsonArray("moves")) {
            JsonObject var15 = var14.getAsJsonObject();
            int var16 = var15.get("tick").getAsInt();
            bo.r var17 = bo.r.m(var15.has("easing") ? var15.get("easing").getAsString() : "linear");
            if (var17 == null) {
               var12.J.add("easing " + var15.get("easing").getAsString());
               var17 = bo.r.M;
            }

            int var18 = var15.has("turn") ? var15.get("turn").getAsInt() : 0;

            for (Entry var20 : var15.entrySet()) {
               if (((JsonElement)var20.getValue()).isJsonObject()) {
                  bo.i var21 = p((String)var20.getKey(), var3);
                  if (var21 == null) {
                     var12.J.add((String)var20.getKey());
                  } else {
                     for (Entry var23 : ((JsonElement)var20.getValue()).getAsJsonObject().entrySet()) {
                        int var24 = Arrays.asList(G).indexOf(var23.getKey());
                        if (var24 >= 0 && (var24 != 6 || q(var21)) && (var24 < 7 || var21 == bo.i.p || var24 == 7 && var21 == bo.i.t)) {
                           float var25 = ((JsonElement)var23.getValue()).getAsFloat();
                           if (Float.isNaN(var25)) {
                              throw new IllegalArgumentException("Keyframe value is NaN");
                           }

                           int var26 = (var24 < 3 || var24 > 6) && var24 != 8 ? 0 : 1;
                           if (var26 != 0 && var10 != 0) {
                              var25 *= (float) (Math.PI / 180.0);
                           }

                           float var27 = var26 != 0 && var18 != 0 ? var25 + (float)((Math.PI * 2) * var18) : var25;
                           var12.j(var21, var24).v(var16, var25, var27, var17);
                        } else {
                           var12.J.add((String)var20.getKey() + "." + (String)var23.getKey());
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

   private static String s(JsonObject var0, String var1) {
      return var0.has(var1) && var0.get(var1).isJsonPrimitive() ? var0.get(var1).getAsString() : "";
   }

   private static bo.i p(String var0, int var1) {
      return switch (var0) {
         case "head" -> bo.i.b;
         case "torso" -> var1 < 3 ? bo.i.W : bo.i.L;
         case "body" -> bo.i.W;
         case "rightArm" -> bo.i.y;
         case "leftArm" -> bo.i.u;
         case "rightLeg" -> bo.i.E;
         case "leftLeg" -> bo.i.Z;
         case "prop" -> bo.i.p;
         case "tool" -> bo.i.t;
         default -> null;
      };
   }

   public static boolean q(bo.i var0) {
      return var0 == bo.i.y || var0 == bo.i.u || var0 == bo.i.E || var0 == bo.i.Z;
   }

   private bo.a j(bo.i var1, int var2) {
      int var3 = var1.ordinal() * 10 + var2;
      if (this.h[var3] == null) {
         this.h[var3] = new bo.a(var2 < 3 ? j[var1.ordinal()][var2] : (var2 == 7 ? 1.0F : 0.0F));
      }

      return this.h[var3];
   }

   public Set<String> U() {
      return Set.copyOf(this.J);
   }

   public boolean s(bo.i var1, int var2) {
      return this.h[var1.ordinal() * 10 + var2] != null;
   }

   public int u(int var1) {
      return this.V && var1 > this.g ? this.B + (var1 - this.g - 1) % (this.g - this.B + 1) : var1;
   }

   public boolean p(int var1) {
      return this.V && var1 > this.g;
   }

   public float z(int var1, float var2) {
      return !this.V ? 0.0F : (this.u(var1) + var2 - this.B) / (this.g - this.B + 1);
   }

   public boolean X(int var1) {
      return !this.V && var1 >= this.A;
   }

   public void X(int var1, float var2, float[] var3) {
      int var4 = this.u(var1);
      boolean var5 = this.p(var1);

      for (int var6 = 0; var6 < s; var6++) {
         var3[var6] = this.h[var6] == null ? Float.NaN : this.w(this.h[var6], var4, var2, var5);
      }
   }

   private float w(bo.a var1, int var2, float var3, boolean var4) {
      List var5 = var1.w;
      int var6 = this.g - this.B + 1;
      bo.b var7 = null;
      bo.b var8 = null;
      int var9 = 0;
      int var10 = 0;

      for (int var11 = var5.size() - 1; var11 >= 0; var11--) {
         bo.b var12 = (bo.b)var5.get(var11);
         if (var12.tick <= var2 && (!this.V || var12.tick <= this.g) && (!var4 || var12.tick >= this.B)) {
            var7 = var12;
            var9 = var12.tick;
            break;
         }
      }

      for (bo.b var15 : var5) {
         if (var15.tick > var2 && (!this.V || var15.tick <= this.g)) {
            var8 = var15;
            var10 = var15.tick;
            break;
         }
      }

      if (this.V && var7 == null && var4) {
         var7 = this.B(var5);
         if (var7 != null) {
            var9 = var7.tick - var6;
         }
      }

      if (this.V && var8 == null) {
         var8 = this.Q(var5);
         if (var8 != null) {
            var10 = var8.tick + var6;
         }
      }

      if (var7 == null) {
         var7 = new bo.b(this.o, var1.X, var1.X, bo.r.M);
         var9 = this.o;
      }

      if (var8 == null) {
         return var7.depart;
      }

      if (var10 <= var9) {
         return var7.depart;
      }

      float var14 = Math.clamp((var2 + var3 - var9) / (var10 - var9), 0.0F, 1.0F);
      float var16 = (this.X ? var8.ease : var7.ease).X(var14);
      return var7.depart + (var8.arrive - var7.depart) * var16;
   }

   private bo.b Q(List<bo.b> var1) {
      for (bo.b var3 : var1) {
         if (var3.tick >= this.B && var3.tick <= this.g) {
            return var3;
         }
      }

      return null;
   }

   private bo.b B(List<bo.b> var1) {
      for (int var2 = var1.size() - 1; var2 >= 0; var2--) {
         bo.b var3 = (bo.b)var1.get(var2);
         if (var3.tick >= this.B && var3.tick <= this.g) {
            return var3;
         }
      }

      return null;
   }

   private static final class a {
      final float X;
      final List<bo.b> w = new ArrayList<>();

      a(float var1) {
         this.X = var1;
      }

      void v(int var1, float var2, float var3, bo.r var4) {
         int var5 = 0;

         while (var5 < this.w.size() && this.w.get(var5).tick < var1) {
            var5++;
         }

         if (var5 < this.w.size() && this.w.get(var5).tick == var1) {
            this.w.set(var5, new bo.b(var1, this.w.get(var5).arrive, var3, var4));
         } else {
            this.w.add(var5, new bo.b(var1, var2, var3, var4));
         }
      }
   }

   private record b(int tick, float arrive, float depart, bo.r ease) {
   }

   public record d(int returnTick, int endTick) {
   }

   public enum i {
      b,
      L,
      y,
      u,
      E,
      Z,
      W,
      p,
      t;
   }

   public enum r {
      M,
      k,
      a,
      S,
      Q,
      E,
      q,
      A,
      j,
      P,
      C,
      r,
      W,
      y,
      i,
      X,
      h,
      B,
      u,
      d,
      F,
      b,
      g,
      v,
      t,
      H,
      I,
      O,
      e,
      s,
      N,
      V;

      static bo.r m(String var0) {
         String var1 = var0.toUpperCase(Locale.ROOT).replace("_", "");
         if (var1.equals("STEP")) {
            return k;
         }

         for (bo.r var5 : values()) {
            if (var5.name().equals(var1)) {
               return var5;
            }
         }

         return null;
      }

      public float X(float var1) {
         double var2 = var1;
         double var4 = 1.70158;
         double var6 = var4 * 1.525;
         double var8 = var4 + 1.0;
         double var10 = Math.PI * 2.0 / 3.0;
         double var12 = Math.PI * 4.0 / 9.0;

         return switch (this) {
            case M -> (float)var2;
            case k -> 0.0F;
            case a -> (float)(1.0 - Math.cos(var2 * Math.PI / 2.0));
            case S -> (float)Math.sin(var2 * Math.PI / 2.0);
            case Q -> (float)(-(Math.cos(Math.PI * var2) - 1.0) / 2.0);
            case E -> (float)(var2 * var2);
            case q -> (float)(1.0 - (1.0 - var2) * (1.0 - var2));
            case A -> var2 < 0.5 ? (float)(2.0 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0) / 2.0);
            case j -> (float)(var2 * var2 * var2);
            case P -> (float)(1.0 - Math.pow(1.0 - var2, 3.0));
            case C -> var2 < 0.5 ? (float)(4.0 * var2 * var2 * var2) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 3.0) / 2.0);
            case r -> (float)Math.pow(var2, 4.0);
            case W -> (float)(1.0 - Math.pow(1.0 - var2, 4.0));
            case y -> var2 < 0.5 ? (float)(8.0 * Math.pow(var2, 4.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 4.0) / 2.0);
            case i -> (float)Math.pow(var2, 5.0);
            case X -> (float)(1.0 - Math.pow(1.0 - var2, 5.0));
            case h -> var2 < 0.5 ? (float)(16.0 * Math.pow(var2, 5.0)) : (float)(1.0 - Math.pow(-2.0 * var2 + 2.0, 5.0) / 2.0);
            case B -> var2 == 0.0 ? 0.0F : (float)Math.pow(2.0, 10.0 * var2 - 10.0);
            case u -> var2 == 1.0 ? 1.0F : (float)(1.0 - Math.pow(2.0, -10.0 * var2));
            case d -> var2 == 0.0 || var2 == 1.0
               ? (float)var2
               : (var2 < 0.5 ? (float)(Math.pow(2.0, 20.0 * var2 - 10.0) / 2.0) : (float)((2.0 - Math.pow(2.0, -20.0 * var2 + 10.0)) / 2.0));
            case F -> (float)(1.0 - Math.sqrt(1.0 - var2 * var2));
            case b -> (float)Math.sqrt(1.0 - Math.pow(var2 - 1.0, 2.0));
            case g -> var2 < 0.5
               ? (float)((1.0 - Math.sqrt(1.0 - Math.pow(2.0 * var2, 2.0))) / 2.0)
               : (float)((Math.sqrt(1.0 - Math.pow(-2.0 * var2 + 2.0, 2.0)) + 1.0) / 2.0);
            case v -> (float)(var8 * var2 * var2 * var2 - var4 * var2 * var2);
            case t -> (float)(1.0 + var8 * Math.pow(var2 - 1.0, 3.0) + var4 * Math.pow(var2 - 1.0, 2.0));
            case H -> var2 < 0.5
               ? (float)(Math.pow(2.0 * var2, 2.0) * ((var6 + 1.0) * 2.0 * var2 - var6) / 2.0)
               : (float)((Math.pow(2.0 * var2 - 2.0, 2.0) * ((var6 + 1.0) * (var2 * 2.0 - 2.0) + var6) + 2.0) / 2.0);
            case I -> var2 != 0.0 && var2 != 1.0 ? (float)(-Math.pow(2.0, 10.0 * var2 - 10.0) * Math.sin((var2 * 10.0 - 10.75) * var10)) : (float)var2;
            case O -> var2 != 0.0 && var2 != 1.0 ? (float)(Math.pow(2.0, -10.0 * var2) * Math.sin((var2 * 10.0 - 0.75) * var10) + 1.0) : (float)var2;
            case e -> var2 == 0.0 || var2 == 1.0
               ? (float)var2
               : (
                  var2 < 0.5
                     ? (float)(-(Math.pow(2.0, 20.0 * var2 - 10.0) * Math.sin((20.0 * var2 - 11.125) * var12)) / 2.0)
                     : (float)(Math.pow(2.0, -20.0 * var2 + 10.0) * Math.sin((20.0 * var2 - 11.125) * var12) / 2.0 + 1.0)
               );
            case s -> (float)(1.0 - i(1.0 - var2));
            case N -> (float)i(var2);
            case V -> var2 < 0.5 ? (float)((1.0 - i(1.0 - 2.0 * var2)) / 2.0) : (float)((1.0 + i(2.0 * var2 - 1.0)) / 2.0);
         };
      }

      private static double i(double var0) {
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
}
