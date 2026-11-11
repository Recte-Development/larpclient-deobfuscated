package com.larpclient.util.data;

import com.larpclient.util.world.BuildScanResult;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.ClickEvent.CopyToClipboard;
import net.minecraft.network.chat.HoverEvent.ShowText;
import net.minecraft.world.phys.Vec3;

public record FoundLocation(String title, String detail, int x, int y, int z) {
   private final int blockX;
   private final int blockY;
   private final int blockZ;
   public static final int NO_Y = Integer.MIN_VALUE;
   private static final int DIM = 5592405;
   private static final int BRAND = 16733695;
   private static final int COORDINATES = 16777215;
   private static final int MUTED = 11184810;
   private static final String SEPARATOR = " · ";

   public FoundLocation(String title, String detail, int x, int y, int z) {
      this.title = title;
      this.detail = detail;
      this.blockX = x;
      this.blockY = y;
      this.blockZ = z;
   }

   public static FoundLocation chunk(String var0, String var1, int var2, int var3) {
      return new FoundLocation(var0, var1, var2 + 8, Integer.MIN_VALUE, var3 + 8);
   }

   public static FoundLocation spawners(int var0, int var1, SpawnerScanCache.ScanResult var2) {
      List var3 = var2.hits();
      if (var3.isEmpty()) {
         return new FoundLocation(spawnerTitle("Spawner", var2), "", var0 + 8, var2.sampleY(), var1 + 8);
      }

      SpawnerScanCache.SpawnerHit var4 = (SpawnerScanCache.SpawnerHit)var3.getFirst();
      return new FoundLocation(spawnerTitle(var4.name(), var2), "", var0 + var4.x2(), var4.y2(), var1 + var4.z2());
   }

   private static String spawnerTitle(String var0, SpawnerScanCache.ScanResult var1) {
      int var2 = var1.spawners();
      if (var2 <= 1) {
         return var0;
      }

      int var3 = !var1.hits().isEmpty() && var1.hits().size() != var2 ? 0 : 1;

      for (SpawnerScanCache.SpawnerHit var5 : var1.hits()) {
         var3 &= var5.name().equals(var0);
      }

      return var3 != 0 ? var0 + " x" + var2 : var0 + " +" + (var2 - 1) + " more";
   }

   public static FoundLocation base(int var0, int var1, BuildScanResult var2) {
      String var3 = var2.dedicated() > 0 ? var2.clue() : "Possible build";
      if (var2.bounds() == null) {
         return new FoundLocation(var3, var2.counts(), var0 + 8, var2.sampleY(), var1 + 8);
      }

      Vec3 var4 = var2.bounds().getCenter();
      return new FoundLocation(var3, var2.counts(), var0 + (int)Math.floor(var4.x), (int)Math.floor(var4.y), var1 + (int)Math.floor(var4.z));
   }

   public FoundLocation zone(int var1, int var2, int var3) {
      return new FoundLocation(this.title, var3 + " chunks" + (this.detail.isEmpty() ? "" : " · " + this.detail), var1, Integer.MIN_VALUE, var2);
   }

   public String coordinates() {
      return this.blockY == Integer.MIN_VALUE ? this.blockX + ", " + this.blockZ : this.blockX + ", " + this.blockY + ", " + this.blockZ;
   }

   public String clipboard() {
      return this.coordinates().replace(",", "");
   }

   public String summary(double var1) {
      return this.title + " · " + this.coordinates() + " · " + Math.round(var1) + "m";
   }

   public MutableComponent chatLine(String var1, int var2, int var3, int var4, double var5) {
      MutableComponent var7 = Component.literal(var1 + "\nChunk " + var2 + ", " + var3 + "\nClick to copy coordinates");
      MutableComponent var8 = Component.empty()
         .withStyle(var2x -> var2x.withClickEvent(new CopyToClipboard(this.clipboard())).withHoverEvent(new ShowText(var7)));
      var8.append(text("[", 5592405)).append(Component.literal("Larp").withStyle(var0 -> var0.withColor(16733695).withBold(true))).append(text("] ", 5592405));
      var8.append(text(this.title, var4 & 16777215)).append(text(" · ", 5592405)).append(text(this.coordinates(), 16777215));
      var8.append(text(" · ", 5592405)).append(text(Math.round(var5) + "m", 11184810));
      if (!this.detail.isEmpty()) {
         var8.append(text(" · ", 5592405)).append(text(this.detail, 11184810));
      }

      return var8;
   }

   private static MutableComponent text(String var0, int var1) {
      return Component.literal(var0).withStyle(var1x -> var1x.withColor(var1));
   }
}
