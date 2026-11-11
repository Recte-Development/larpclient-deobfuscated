package com.larpclient.config;

import com.larpclient.util.world.AStarPathfinder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public final class BuildOrderFile {
   static final int mAX_CELLS = 4000000;

   private BuildOrderFile() {
   }

   public static String orderFileName(String var0) {
      return var0 + ".order.txt";
   }

   public static void write(Path var0, String var1, List<int[]> var2) throws IOException {
      StringBuilder var3 = new StringBuilder("# Larp build order for ")
         .append(var1)
         .append(", ")
         .append(var2.size())
         .append(" cells in the order they were placed: x y z in the schematic's own coordinates\n");

      for (int[] var5 : var2) {
         var3.append(var5[0]).append(' ').append(var5[1]).append(' ').append(var5[2]).append('\n');
      }

      Files.createDirectories(var0.toAbsolutePath().getParent());
      Path var7 = var0.resolveSibling(var0.getFileName() + ".tmp");
      Files.writeString(var7, var3, StandardCharsets.UTF_8);

      try {
         Files.move(var7, var0, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
      } catch (AtomicMoveNotSupportedException var6) {
         Files.move(var7, var0, StandardCopyOption.REPLACE_EXISTING);
      }
   }

   public static List<int[]> read(Path var0) throws IOException {
      return !Files.isRegularFile(var0) ? List.of() : parse(Files.readAllLines(var0, StandardCharsets.UTF_8));
   }

   public static List<int[]> parse(List<String> var0) {
      ArrayList var1 = new ArrayList();
      HashSet var2 = new HashSet();

      for (String var4 : var0) {
         String var5 = var4.strip();
         if (!var5.isEmpty() && !var5.startsWith("#")) {
            String[] var6 = var5.toLowerCase(Locale.ROOT).split("\\s+");
            if (var6.length == 3) {
               try {
                  int var7 = Integer.parseInt(var6[0]);
                  int var8 = Integer.parseInt(var6[1]);
                  int var9 = Integer.parseInt(var6[2]);
                  if (var2.add(AStarPathfinder.packPos(var7, var8, var9))) {
                     var1.add(new int[]{var7, var8, var9});
                  }
               } catch (NumberFormatException var10) {
               }

               if (var1.size() >= 4000000) {
                  break;
               }
            }
         }
      }

      return var1;
   }
}
