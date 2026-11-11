package com.larpclient.util.render;

import com.larpclient.render.entity.FlameRingRenderer;
import com.larpclient.render.model.RingModel;
import com.larpclient.util.misc.PreviewSink;
import java.util.ArrayList;
import java.util.List;
import org.joml.Matrix4f;

public final class HeadCoinLayout {
   public static final double dAMPING = 0.995;
   public static final float hALO_COIN_SCALE = 0.118F;
   public static final float cOIN_SCALE = 0.19666666F;
   private static final int[] cOIN_ORDER = new int[]{0, 1, 3, 2};
   private static final float oRBIT_RADIUS = 0.32F;
   private static final List<HeadCoinLayout.CoinSlot> cROWN_SLOTS = buildSlots();

   private static List<HeadCoinLayout.CoinSlot> buildSlots() {
      ArrayList var0 = new ArrayList();

      for (int var1 = 0; var1 < cOIN_ORDER.length; var1++) {
         float var2 = (float)Math.toRadians(90 * var1);
         var0.add(new HeadCoinLayout.CoinSlot(cOIN_ORDER[var1], 0.32F * (float)Math.sin(var2), 0.0F, -0.32F * (float)Math.cos(var2), var2));
      }

      return List.copyOf(var0);
   }

   public static List<HeadCoinLayout.CoinSlot> slotsFor(HeadCoinLayout.Accessory var0) {
      return var0 == HeadCoinLayout.Accessory.CROWN ? cROWN_SLOTS : List.of();
   }

   public static Matrix4f buildSlots(Matrix4f var0, HeadCoinLayout.CoinSlot var1, double var2, Matrix4f var4) {
      float var5 = (float)var2;
      return var4.set(var0)
         .translate(var1.offsetX() * var5, -0.5F - var1.rise() * var5, var1.offsetZ() * var5)
         .rotateY((float) Math.PI - var1.angle())
         .rotateZ((float) Math.PI)
         .scale(0.19666666F * var5);
   }

   private HeadCoinLayout() {
   }

   public static double riseFor(HeadCoinLayout.Accessory var0, double var1) {
      return (var0 == HeadCoinLayout.Accessory.CROWN ? RingModel.getModel().rise() : FlameRingRenderer.tOP_EXTENT) * var1;
   }

   public static void render(PreviewSink var0, Matrix4f var1, HeadCoinLayout.Accessory var2, double var3, double var5, boolean var7, double var8, double var10) {
      if (var2 == HeadCoinLayout.Accessory.CROWN) {
         RingModel.render(var0, var1, var3, var5, var7, var8);
      } else {
         FlameRingRenderer.renderFlameRing(var0, var1, var3, var5, var10);
      }
   }

   public enum Accessory {
      CROWN("Crown"),
      HALO("Halo");

      public final String LABEL;

      Accessory(String var3) {
         this.LABEL = var3;
      }
   }

   public record CoinSlot(int coin, float x, float rise, float z, float angle) {
      private final float offsetX;
      private final float offsetZ;

      public CoinSlot(int coin, float x, float rise, float z, float angle) {
         this.coin = coin;
         this.offsetX = x;
         this.rise = rise;
         this.offsetZ = z;
         this.angle = angle;
      }
   }
}
