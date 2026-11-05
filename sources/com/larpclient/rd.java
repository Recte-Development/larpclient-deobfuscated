package com.larpclient;

import com.larpclient.module.HUDModule;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;

public final class rd {
   public static final int W = 32;
   public static final int v = 12;
   public static final int P = 13;
   public static final int H = -267645934;
   private static final Map<Module, Float> f = new IdentityHashMap<>();
   private static final Map<Identifier, Float> Z = new HashMap<>();
   private static Object b;
   private static Object C;
   private static long E;
   private static long M;
   private static final Map<Identifier, rd.n> d = new HashMap<>();

   private rd() {
   }

   public static void g() {
      f.clear();
      Z.clear();
      d.clear();
      C = null;
      b = null;
      M = 0L;
      E = 0L;
   }

   private static void s() {
      Minecraft var0 = Minecraft.getInstance();
      if (b != var0.level || C != var0.player) {
         g();
         b = var0.level;
         C = var0.player;
      }
   }

   public static int D(String var0) {
      return H(var0, 13.0);
   }

   public static int H(String var0, double var1) {
      return (int)Math.ceil(rz.V(var0, var1));
   }

   public static String R(String var0, double var1, double var3) {
      return rz.H(var0, var1, var3);
   }

   public static int h(int var0, int var1) {
      return var0 + var1 + (int)Math.ceil(54.0);
   }

   public static int Y(int var0, int var1, int var2) {
      return 26 + (int)Math.ceil(16.799999999999997) + var2 + var0 + 48 + var1;
   }

   public static float K(float var0, boolean var1) {
      return z(var0, var1, 0.016666666666666666);
   }

   public static float z(float var0, boolean var1, double var2) {
      double var4 = 1.0 - Math.pow(0.85, Math.clamp(var2, 0.0, 0.25) * 60.0);
      return (float)(var0 + ((var1 ? 1 : 0) - var0) * var4);
   }

   private static double E(long var0, long var2) {
      return var0 == 0L ? 0.016666666666666666 : Math.max(0L, var2 - var0) / 1.0E9;
   }

   public static String A(int var0, boolean var1) {
      int var2 = Math.max(0, var0 / 20);
      return var1 ? "∞" : String.format(Locale.ROOT, "%d:%02d", var2 / 60, var2 % 60);
   }

   private static rd.w g(String var0, List<rd.n> var1, boolean var2) {
      int var3 = Math.max(168, D(var0) + 28 + 24);
      double var4 = 48.0;

      for (rd.n var7 : var1) {
         var3 = Math.max(var3, var7.width());
         var4 += (var2 ? 22 : 23) * var7.alpha();
      }

      return new rd.w(var0, List.copyOf(var1), var3, (int)Math.ceil(var4), var2);
   }

   public static rd.w w(boolean var0) {
      s();
      long var1 = System.nanoTime();
      double var3 = E(E, var1);
      if (!var0) {
         E = var1;
      }

      ArrayList var5 = new ArrayList();

      for (Module var7 : LarpClient.y().e().X()) {
         HUDModule var8 = HUDModule.f();
         int var9 = var7.getKeyBind() < 0 || !var7.isEnabled() && (var8 == null || !var8.a.i("Always")) ? 0 : 1;
         float var10 = var0 ? (var9 != 0 ? 1 : 0) : z(f.getOrDefault(var7, 0.0F), (boolean)var9, var3);
         if (var10 < 0.05F && var9 == 0) {
            if (!var0) {
               f.remove(var7);
            }
         } else {
            if (!var0) {
               f.put(var7, var10);
            }

            if (!(var10 <= 0.05F)) {
               String var11 = var7.getDisplayName();
               String var12 = var7.getKeyBind() < 0 ? "None" : pe.A(var7.getKeyBind()).toUpperCase(Locale.ROOT);
               var5.add(new rd.n(var11, var12, null, h(D(var11), D(var12)), var10));
            }
         }
      }

      if (var0 && var5.isEmpty()) {
         var5.add(new rd.n("Backtrack", "R", null, h(D("Backtrack"), D("R")), 1.0F));
         var5.add(new rd.n("Speed", "V", null, h(D("Speed"), D("V")), 1.0F));
      }

      return g("KeyBinds", var5, true);
   }

   public static rd.w K(boolean var0) {
      com.larpclient.module.a var1 = com.larpclient.module.a.x();
      String var2 = var1 == null ? "Blackjack" : var1.getDisplayName();
      List var3 = var1 == null
         ? List.of(new String[]{"Drawing", "Dealer"}, new String[]{"Player", "18"}, new String[]{"Dealer", "6"}, new String[]{"Dealer plan", "A"})
         : var1.k();
      ArrayList var4 = new ArrayList();

      for (String[] var6 : var3) {
         var4.add(new rd.n(var6[0], var6[1], null, h(D(var6[0]), D(var6[1])), 1.0F));
      }

      rd.w var7 = g(var2, var4, true);
      return new rd.w(var7.title(), var7.rows(), var7.width(), var7.height(), true, "store");
   }

   public static rd.w h(boolean var0) {
      s();
      long var1 = System.nanoTime();
      double var3 = E(M, var1);
      if (!var0) {
         M = var1;
      }

      Minecraft var5 = Minecraft.getInstance();
      ArrayList var6 = new ArrayList();
      HashSet var7 = new HashSet();
      if (var5.player != null) {
         for (MobEffectInstance var9 : var5.player.getActiveEffects()) {
            Identifier var10 = BuiltInRegistries.MOB_EFFECT.getKey((MobEffect)var9.getEffect().value());
            var7.add(var10);
            float var11 = var0 ? 1.0F : z(Z.getOrDefault(var10, 0.0F), true, var3);
            if (!var0) {
               Z.put(var10, var11);
            }

            String var12 = ((MobEffect)var9.getEffect().value()).getDisplayName().getString()
               + (var9.getAmplifier() > 0 ? " " + (var9.getAmplifier() + 1) : "");
            String var13 = A(var9.getDuration(), var9.isInfiniteDuration());
            Identifier var14 = Identifier.fromNamespaceAndPath(var10.getNamespace(), "mob_effect/" + var10.getPath());
            rd.n var15 = new rd.n(var12, var13, var14, Y(D(var12), D(var13), D(">")), var11);
            var6.add(var15);
            if (!var0) {
               d.put(var10, var15);
            }
         }
      }

      if (!var0) {
         Iterator var16 = Z.entrySet().iterator();

         while (var16.hasNext()) {
            Entry var18 = (Entry)var16.next();
            if (!var7.contains(var18.getKey())) {
               float var20 = z((Float)var18.getValue(), false, var3);
               if (var20 <= 0.05F) {
                  var16.remove();
                  d.remove(var18.getKey());
               } else {
                  var18.setValue(var20);
                  rd.n var21 = d.get(var18.getKey());
                  if (var21 != null) {
                     var6.add(new rd.n(var21.label(), var21.value(), var21.icon(), var21.width(), var20));
                  }
               }
            }
         }
      }

      if (var0 && var6.isEmpty()) {
         for (String[] var19 : List.of(new String[]{"Speed 2", "0:45", "speed"}, new String[]{"Strength", "0:30", "strength"})) {
            var6.add(new rd.n(var19[0], var19[1], Identifier.withDefaultNamespace("mob_effect/" + var19[2]), Y(D(var19[0]), D(var19[1]), D(">")), 1.0F));
         }
      }

      var6.sort(Comparator.comparingInt(rd.n::width).reversed());
      return g("Potions", var6, false);
   }

   public static void o(GuiGraphicsExtractor var0, String var1, double var2, double var4, int var6) {
      v(var0, var1, var2, var4, 13.0, var6);
   }

   public static void v(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      rz.j(var0, var1, var2, var4, var6, var8);
   }

   public static void L(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5) {
      Identifier var6 = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/larp/" + var1 + ".png");
      var0.blit(RenderPipelines.GUI_TEXTURED, var6, var2, var3, 0.0F, 0.0F, var4, var4, 16, 16, 16, 16, r.D(var5));
   }

   public static void Z(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5) {
      if (!rz.n(var0, var1, var2, var3, var4, var5)) {
         ou.u(var0, ou.e(var1), var2, var3, var4, var5);
      }
   }

   private static int T(rd.w var0) {
      return var0.keys() ? 22 : 23;
   }

   public static List<_h.p> R(rd.w var0, int var1, int var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      var4.add(new _h.p(var1, var2, var0.width(), var0.height()));
      double var5 = var2 + 39;

      for (rd.n var8 : var0.rows()) {
         int var9 = var0.keys() ? 22 : 23;
         var4.add(new _h.p(var1, (int)Math.floor(var5), var0.width(), (int)Math.ceil(var9 * var8.alpha())));
         var5 += var9 * var8.alpha();
      }

      return var4;
   }

   public static void N(GuiGraphicsExtractor var0, rd.w var1, int var2, int var3, boolean var4) {
      int var5 = var1.icon() != null ? 1 : 0;
      p3.f(var0, var2, var3, var1.width(), var1.height(), var1.title(), var5 != 0 ? var1.icon() : (var1.keys() ? "keyboard" : "flask-conical"), true);
      List var6 = R(var1, var2, var3, var4);

      for (int var7 = 0; var7 < var1.rows().size(); var7++) {
         rd.n var8 = var1.rows().get(var7);
         _h.p var9 = (_h.p)var6.get(var7 + 1);
         int var10 = var9.y();
         int var11 = var9.height();
         double var12 = var8.alpha();
         var0.enableScissor(var2, var10, var2 + var1.width(), var10 + var11);

         try {
            double var14 = rz.W(var10, T(var1), 13.0) - (T(var1) - var11) / 2.0;
            int var16 = var2 + 12;
            if (!var1.keys() && var5 == 0) {
               var0.blitSprite(RenderPipelines.GUI_TEXTURED, var8.icon(), var16, var10 + (var11 - 15) / 2, 15, 15, oh.S(-1, var12));
               var16 += 22;
            }

            int var17 = D(var8.value());
            int var18 = var17 + 12;
            int var19 = var2 + var1.width() - 12 - var17;
            int var20 = -1250068;
            if (var1.keys() && var5 == 0) {
               com.larpclient.module.ly var21 = LarpClient.y().e();
               boolean var22 = var21.X().stream().anyMatch(var1x -> var1x.isEnabled() && var1x.getDisplayName().equals(var8.label()));
               var20 = var22 ? rv.e : -1494422292;
               fr.S(var0, var19 - 6, var10 + (var11 - 17) / 2, var18, 17.0F, 4.0F, oh.S(-1275068416, var12));
            }

            String var26 = R(var8.label(), var19 - var16 - 14, 13.0);
            o(var0, var26, var16, var14, oh.S(var20, var12));
            o(var0, var8.value(), var19, var14, oh.S(-4671294, var12));
         } finally {
            var0.disableScissor();
         }
      }
   }

   public record n(String label, String value, Identifier icon, int width, float alpha) {
   }

   public record w(String title, List<rd.n> rows, int width, int height, boolean keys, String icon) {
      public w(String var1, List<rd.n> var2, int var3, int var4, boolean var5) {
         this(var1, var2, var3, var4, var5, null);
      }
   }
}
