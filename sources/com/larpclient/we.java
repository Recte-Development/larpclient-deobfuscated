package com.larpclient;

import com.larpclient.module.HUDModule;
import com.larpclient.module.Module;
import com.larpclient.module.dz;
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

public final class we {
   public static final int t = 32;
   public static final int v = 12;
   public static final int G = 13;
   public static final int V = -267645934;
   private static final Map<Module, Float> s = new IdentityHashMap<>();
   private static final Map<Identifier, Float> q = new HashMap<>();
   private static Object B;
   private static Object O;
   private static long H;
   private static long R;
   private static final Map<Identifier, we.n> b = new HashMap<>();

   private we() {
   }

   public static void E() {
      s.clear();
      q.clear();
      b.clear();
      O = null;
      B = null;
      R = 0L;
      H = 0L;
   }

   private static void u() {
      Minecraft var0 = Minecraft.getInstance();
      if (B != var0.level || O != var0.player) {
         E();
         B = var0.level;
         O = var0.player;
      }
   }

   public static int L(String var0) {
      return V(var0, 13.0);
   }

   public static int V(String var0, double var1) {
      return (int)Math.ceil(ew.N(var0, var1));
   }

   public static String t(String var0, double var1, double var3) {
      return ew.S(var0, var1, var3);
   }

   public static int r(int var0, int var1) {
      return var0 + var1 + (int)Math.ceil(54.0);
   }

   public static int P(int var0, int var1, int var2) {
      return 26 + (int)Math.ceil(16.799999999999997) + var2 + var0 + 48 + var1;
   }

   public static float b(float var0, boolean var1) {
      return P(var0, var1, 0.016666666666666666);
   }

   public static float P(float var0, boolean var1, double var2) {
      double var4 = 1.0 - Math.pow(0.85, Math.clamp(var2, 0.0, 0.25) * 60.0);
      return (float)(var0 + ((var1 ? 1 : 0) - var0) * var4);
   }

   private static double F(long var0, long var2) {
      return var0 == 0L ? 0.016666666666666666 : Math.max(0L, var2 - var0) / 1.0E9;
   }

   public static String C(int var0, boolean var1) {
      int var2 = Math.max(0, var0 / 20);
      return var1 ? "∞" : String.format(Locale.ROOT, "%d:%02d", var2 / 60, var2 % 60);
   }

   private static we.t A(String var0, List<we.n> var1, boolean var2) {
      int var3 = Math.max(168, L(var0) + 28 + 24);
      double var4 = 48.0;

      for (we.n var7 : var1) {
         var3 = Math.max(var3, var7.width());
         var4 += (var2 ? 22 : 23) * var7.alpha();
      }

      return new we.t(var0, List.copyOf(var1), var3, (int)Math.ceil(var4), var2);
   }

   public static we.t r(boolean var0) {
      u();
      long var1 = System.nanoTime();
      double var3 = F(H, var1);
      if (!var0) {
         H = var1;
      }

      ArrayList var5 = new ArrayList();

      for (Module var7 : LarpClient.F().x().C()) {
         HUDModule var8 = HUDModule.U();
         int var9 = var7.getKeyBind() < 0 || !var7.isEnabled() && (var8 == null || !var8.g.S("Always")) ? 0 : 1;
         float var10 = var0 ? (var9 != 0 ? 1 : 0) : P(s.getOrDefault(var7, 0.0F), (boolean)var9, var3);
         if (var10 < 0.05F && var9 == 0) {
            if (!var0) {
               s.remove(var7);
            }
         } else {
            if (!var0) {
               s.put(var7, var10);
            }

            if (!(var10 <= 0.05F)) {
               String var11 = var7.getDisplayName();
               String var12 = var7.getKeyBind() < 0 ? "None" : cc.u(var7.getKeyBind()).toUpperCase(Locale.ROOT);
               var5.add(new we.n(var11, var12, null, r(L(var11), L(var12)), var10));
            }
         }
      }

      if (var0 && var5.isEmpty()) {
         var5.add(new we.n("Backtrack", "R", null, r(L("Backtrack"), L("R")), 1.0F));
         var5.add(new we.n("Speed", "V", null, r(L("Speed"), L("V")), 1.0F));
      }

      return A("KeyBinds", var5, true);
   }

   public static we.t z(boolean var0) {
      com.larpclient.module.v var1 = com.larpclient.module.v.v();
      String var2 = var1 == null ? "Blackjack" : var1.getDisplayName();
      List var3 = var1 == null
         ? List.of(new String[]{"Drawing", "Dealer"}, new String[]{"Player", "18"}, new String[]{"Dealer", "6"}, new String[]{"Dealer plan", "A"})
         : var1.a();
      ArrayList var4 = new ArrayList();

      for (String[] var6 : var3) {
         var4.add(new we.n(var6[0], var6[1], null, r(L(var6[0]), L(var6[1])), 1.0F));
      }

      we.t var7 = A(var2, var4, true);
      return new we.t(var7.title(), var7.rows(), var7.width(), var7.height(), true, "store");
   }

   public static we.t E(boolean var0) {
      u();
      long var1 = System.nanoTime();
      double var3 = F(R, var1);
      if (!var0) {
         R = var1;
      }

      Minecraft var5 = Minecraft.getInstance();
      ArrayList var6 = new ArrayList();
      HashSet var7 = new HashSet();
      if (var5.player != null) {
         for (MobEffectInstance var9 : var5.player.getActiveEffects()) {
            Identifier var10 = BuiltInRegistries.MOB_EFFECT.getKey((MobEffect)var9.getEffect().value());
            var7.add(var10);
            float var11 = var0 ? 1.0F : P(q.getOrDefault(var10, 0.0F), true, var3);
            if (!var0) {
               q.put(var10, var11);
            }

            String var12 = ((MobEffect)var9.getEffect().value()).getDisplayName().getString()
               + (var9.getAmplifier() > 0 ? " " + (var9.getAmplifier() + 1) : "");
            String var13 = C(var9.getDuration(), var9.isInfiniteDuration());
            Identifier var14 = Identifier.fromNamespaceAndPath(var10.getNamespace(), "mob_effect/" + var10.getPath());
            we.n var15 = new we.n(var12, var13, var14, P(L(var12), L(var13), L(">")), var11);
            var6.add(var15);
            if (!var0) {
               b.put(var10, var15);
            }
         }
      }

      if (!var0) {
         Iterator var16 = q.entrySet().iterator();

         while (var16.hasNext()) {
            Entry var18 = (Entry)var16.next();
            if (!var7.contains(var18.getKey())) {
               float var20 = P((Float)var18.getValue(), false, var3);
               if (var20 <= 0.05F) {
                  var16.remove();
                  b.remove(var18.getKey());
               } else {
                  var18.setValue(var20);
                  we.n var21 = b.get(var18.getKey());
                  if (var21 != null) {
                     var6.add(new we.n(var21.label(), var21.value(), var21.icon(), var21.width(), var20));
                  }
               }
            }
         }
      }

      if (var0 && var6.isEmpty()) {
         for (String[] var19 : List.of(new String[]{"Speed 2", "0:45", "speed"}, new String[]{"Strength", "0:30", "strength"})) {
            var6.add(new we.n(var19[0], var19[1], Identifier.withDefaultNamespace("mob_effect/" + var19[2]), P(L(var19[0]), L(var19[1]), L(">")), 1.0F));
         }
      }

      var6.sort(Comparator.comparingInt(we.n::width).reversed());
      return A("Potions", var6, false);
   }

   public static void W(GuiGraphicsExtractor var0, String var1, double var2, double var4, int var6) {
      F(var0, var1, var2, var4, 13.0, var6);
   }

   public static void F(GuiGraphicsExtractor var0, String var1, double var2, double var4, double var6, int var8) {
      ew.m(var0, var1, var2, var4, var6, var8);
   }

   public static void G(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5) {
      Identifier var6 = Identifier.fromNamespaceAndPath("larp-client", "textures/gui/larp/" + var1 + ".png");
      var0.blit(RenderPipelines.GUI_TEXTURED, var6, var2, var3, 0.0F, 0.0F, var4, var4, 16, 16, 16, 16, de.h(var5));
   }

   public static void C(GuiGraphicsExtractor var0, String var1, int var2, int var3, int var4, int var5) {
      if (!ew.r(var0, var1, var2, var3, var4, var5)) {
         tp.N(var0, tp.j(var1), var2, var3, var4, var5);
      }
   }

   private static int R(we.t var0) {
      return var0.keys() ? 22 : 23;
   }

   public static List<ez.h> e(we.t var0, int var1, int var2, boolean var3) {
      ArrayList var4 = new ArrayList();
      var4.add(new ez.h(var1, var2, var0.width(), var0.height()));
      double var5 = var2 + 39;

      for (we.n var8 : var0.rows()) {
         int var9 = var0.keys() ? 22 : 23;
         var4.add(new ez.h(var1, (int)Math.floor(var5), var0.width(), (int)Math.ceil(var9 * var8.alpha())));
         var5 += var9 * var8.alpha();
      }

      return var4;
   }

   public static void V(GuiGraphicsExtractor var0, we.t var1, int var2, int var3, boolean var4) {
      int var5 = var1.icon() != null ? 1 : 0;
      e0.l(var0, var2, var3, var1.width(), var1.height(), var1.title(), var5 != 0 ? var1.icon() : (var1.keys() ? "keyboard" : "flask-conical"), true);
      List var6 = e(var1, var2, var3, var4);

      for (int var7 = 0; var7 < var1.rows().size(); var7++) {
         we.n var8 = var1.rows().get(var7);
         ez.h var9 = (ez.h)var6.get(var7 + 1);
         int var10 = var9.y();
         int var11 = var9.height();
         double var12 = var8.alpha();
         var0.enableScissor(var2, var10, var2 + var1.width(), var10 + var11);

         try {
            double var14 = ew.N(var10, R(var1), 13.0) - (R(var1) - var11) / 2.0;
            int var16 = var2 + 12;
            if (!var1.keys() && var5 == 0) {
               var0.blitSprite(RenderPipelines.GUI_TEXTURED, var8.icon(), var16, var10 + (var11 - 15) / 2, 15, 15, _3.U(-1, var12));
               var16 += 22;
            }

            int var17 = L(var8.value());
            int var18 = var17 + 12;
            int var19 = var2 + var1.width() - 12 - var17;
            int var20 = -1250068;
            if (var1.keys() && var5 == 0) {
               dz var21 = LarpClient.F().x();
               boolean var22 = var21.C().stream().anyMatch(var1x -> var1x.isEnabled() && var1x.getDisplayName().equals(var8.label()));
               var20 = var22 ? f.C : -1494422292;
               c7.n(var0, var19 - 6, var10 + (var11 - 17) / 2, var18, 17.0F, 4.0F, _3.U(-1275068416, var12));
            }

            String var26 = t(var8.label(), var19 - var16 - 14, 13.0);
            W(var0, var26, var16, var14, _3.U(var20, var12));
            W(var0, var8.value(), var19, var14, _3.U(-4671294, var12));
         } finally {
            var0.disableScissor();
         }
      }
   }

   public record n(String label, String value, Identifier icon, int width, float alpha) {
   }

   public record t(String title, List<we.n> rows, int width, int height, boolean keys, String icon) {
      public t(String var1, List<we.n> var2, int var3, int var4, boolean var5) {
         this(var1, var2, var3, var4, var5, null);
      }
   }
}
