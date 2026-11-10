package com.larpclient.module;

import com.larpclient.c5;
import com.larpclient.ez;
import com.larpclient.f1;
import com.larpclient.ip;
import com.larpclient.q8;
import com.larpclient.qo;
import com.larpclient.qw;
import com.larpclient.t_;
import com.larpclient.wc;
import com.larpclient.wr;
import java.util.List;
import java.util.Locale;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldExtractionContext;
import net.minecraft.class_11658;
import net.minecraft.class_12074;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_310;
import net.minecraft.class_239.class_240;

public final class BlockOverlayModule extends Module {
   private static BlockOverlayModule D;
   private final lo w = this.addSetting(new lo("Transition", "Time to glide to another block, in milliseconds; 0 is instant", 240.0, 0.0, 300.0, 10.0));
   private final li b = this.addSetting(new li("Color Mode", "Use your interface accent or a custom color", "Theme", "Theme", "Custom"));
   private final l0 s = this.addSetting(new l0("Color", "Custom highlight color", -1381654));
   private final lo x = this.addSetting(b("Outline Opacity", "Outline visibility", 0.85, 0.1, 1.0, 0.05));
   private final lo n = this.addSetting(new lo("Outline Thickness", "Stroke width in blocks", 0.015, 0.005, 0.05, 0.005) {
      @Override
      public String getValueAsString() {
         return String.format(Locale.ROOT, "%.3f", this.getValue());
      }
   });
   private final l3 X = this.addSetting(new l3("Fill", "Subtle tint over the selected shape", true));
   private final lo V = this.addSetting(b("Fill Opacity", "Keep the block texture visible", 0.15, 0.01, 0.3, 0.01));
   private final lo q = this.addSetting(b("Glow", "Soft edge halo and shared quality-controlled bloom", 1.5, 0.0, 1.5, 0.05));
   private final qw L = new qw();
   private Object l;
   private Object G;
   private class_243 F;
   private class_265 i;
   private ip Q;
   private class_2338 e;
   private int u = -1;
   private boolean O;
   private boolean j;
   private static final RenderStateDataKey<BlockOverlayModule.x> E = RenderStateDataKey.create(() -> "Larp block overlay");

   public BlockOverlayModule() {
      super("Block Overlay", "Highlight the block you aim at", Category.VISUALS, -1);
      D = this;
      this.s.O(() -> this.b.G("Custom"));
      this.V.O(this.X::getValue);
   }

   private static lo b(String var0, String var1, double var2, double var4, double var6, double var8) {
      return new lo(var0, var1, var2, var4, var6, var8) {
         @Override
         public String getValueAsString() {
            return this.getEditValueAsString() + "%";
         }

         @Override
         public String getEditValueAsString() {
            return Long.toString(Math.round(this.getValue() * 100.0));
         }

         @Override
         public void setFromEditString(String var1) {
            this.q(Double.parseDouble(var1) / 100.0);
         }
      };
   }

   public static BlockOverlayModule Q() {
      return D;
   }

   public List<lb<?>> l() {
      return List.of(this.b, this.s, this.x, this.n, this.X, this.V, this.q);
   }

   public List<lb<?>> o() {
      return List.of(this.w);
   }

   @Override
   public void onEnable() {
      this.Z();
      q8.G("BlockOverlay", "Selection overlay enabled; cached native shapes, interruptible {} ms transition, shared glow", this.w.getValue());
   }

   @Override
   public void onDisable() {
      this.Z();
   }

   public void Z() {
      this.L.B();
      this.Q = null;
      this.i = null;
      this.e = null;
      this.l = this.G = null;
      this.F = null;
      this.O = this.j = (boolean)0;
   }

   public void b(WorldExtractionContext var1, class_239 var2) {
      if (this.isEnabled()) {
         class_11658 var3 = var1.worldState();
         var3.setData(E, null);
         if (!this.isRuntimeActive()) {
            this.Z();
         } else {
            class_310 var4 = getMc();
            if (var4.field_1755 == null && !var4.field_1690.field_1842 && var4.method_1569() && var4.field_1724 != null && var4.field_1724.method_5805()) {
               class_243 var5 = var1.camera().method_71156();
               if (this.l != var1.world() || this.G != var4.field_1724 || this.u != t_.g || this.F != null && this.F.method_1022(var5) > 8.0) {
                  this.Z();
                  this.l = var1.world();
                  this.G = var4.field_1724;
                  this.u = t_.g;
               }

               this.F = var5;
               if (!this.O) {
                  try {
                     class_12074 var6 = var1.worldState().field_63083;
                     class_238 var7 = null;
                     this.e = null;
                     if (var6 != null && var2 != null && var2.method_17783() == class_240.field_1332) {
                        class_265 var8 = var6.comp_4935();
                        this.Q = this.A(var8);
                        if (this.Q != null) {
                           this.e = var6.comp_4932();
                           var7 = this.Q.bounds().method_996(this.e);
                        } else if (!this.j) {
                           this.j = (boolean)1;
                           q8.G("BlockOverlay", "Empty or complex selection shape; keeping native outline (limits: {} edges, {} boxes)", 128, 32);
                        }
                     }

                     qw.c var11 = this.L.U(var7, qo.x(), this.w.getValue() / 1000.0);
                     BlockOverlayModule.x var9 = this.Q != null && var11.bounds() != null
                        ? new BlockOverlayModule.x(
                           this.e,
                           this.Q,
                           var11.bounds().method_1014(0.002),
                           var11.alpha(),
                           this.b.G("Theme") ? ez.a : this.s.getValue(),
                           this.x.getValue(),
                           this.n.getValue(),
                           this.X.getValue() ? this.V.getValue() : 0.0,
                           this.q.getValue()
                        )
                        : null;
                     var3.setData(E, var9);
                  } catch (RuntimeException var10) {
                     this.O = (boolean)1;
                     this.e = null;
                     q8.u("BlockOverlay", "Selection extraction failed; native outline retained, retry on resource reload", var10);
                  }
               }
            } else {
               this.Z();
            }
         }
      }
   }

   private ip A(class_265 var1) {
      if (var1 != this.i) {
         this.Q = ip.build(var1);
         this.i = var1;
      }

      return this.Q;
   }

   private static BlockOverlayModule.x p(class_11658 var0) {
      return (BlockOverlayModule.x)var0.getData(E);
   }

   public boolean n(class_2338 var1, class_11658 var2) {
      if (!this.isEnabled()) {
         return false;
      }

      BlockOverlayModule.x var3 = p(var2);
      return this.isRuntimeActive() && !this.O && wc.l() && var3 != null && var1.equals(var3.selected);
   }

   public boolean i(class_11658 var1) {
      if (!this.isEnabled()) {
         return false;
      }

      BlockOverlayModule.x var2 = p(var1);
      return this.isRuntimeActive() && var2 != null && var2.alpha > 0.001 && var2.glow > 0.0;
   }

   public void E(f1 var1, boolean var2, class_11658 var3) {
      if (this.isEnabled()) {
         BlockOverlayModule.x var4 = p(var3);
         if (this.isRuntimeActive() && var4 != null && !(var4.alpha < 0.001) && (!var2 || !(var4.glow <= 0.0))) {
            long var5 = c5.X();
            wr.d(var1, var2, var4.geometry, var4.bounds, var4.alpha, var4.color, var4.opacity, var4.width, var4.fillOpacity, var4.glow);
            c5.w(var2 ? "BlockOverlay glow" : "BlockOverlay base", var5);
         }
      }
   }

   private record x(class_2338 selected, ip geometry, class_238 bounds, double alpha, int color, double opacity, double width, double fillOpacity, double glow) {
   }
}
