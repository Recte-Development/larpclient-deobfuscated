package com.larpclient.module.world;

import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.RigLog;
import java.util.function.Function;
import java.util.function.Predicate;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents.BeforeGizmos;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public final class WorldSelectionPicker {
   private static final long tIMEOUT_NANOS = 30000000000L;
   private static final long hIGHLIGHT_NANOS = 1200000000L;
   private static final long pROMPT_INTERVAL_NANOS = 1500000000L;
   private static final int vALID_COLOR = -13180806;
   private static final int iNVALID_COLOR = -2074034;
   private static WorldSelectionPicker.Selection activeSelection;
   private static long deadlineNanos;
   private static long lastPromptNanos;
   private static BlockPos highlightedPos;
   private static long highlightEndNanos;
   private static AbstractContainerMenu highlightedMenu;
   private static int highlightedSlot = -1;
   private static long slotHighlightEndNanos;
   private static boolean hooksRegistered;

   private WorldSelectionPicker() {
   }

   public static void selectBlock(String var0, Predicate<BlockState> var1, String var2, Function<BlockPos, String> var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var4.level != null) {
         registerHooks();
         activeSelection = new WorldSelectionPicker.BlockSelection(var0, var1, var2, var3);
         sendOverlayMessage(var4);
         var4.gui.setScreen(null);
      } else {
         LarpLog.info("Rig", "Selection needs a world: {}", var0);
      }
   }

   public static void tick(String var0, Predicate<ItemStack> var1, String var2, Function<ItemStack, String> var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4.player != null && var4.level != null) {
         registerHooks();
         activeSelection = new WorldSelectionPicker.ItemSelection(var0, var1, var2, var3);
         sendOverlayMessage(var4);
         var4.gui.setScreen(new InventoryScreen(var4.player));
      } else {
         LarpLog.info("Rig", "Selection needs a world: {}", var0);
      }
   }

   public static boolean isSelecting() {
      return activeSelection != null;
   }

   private static void sendOverlayMessage(Minecraft var0) {
      long var1 = System.nanoTime();
      deadlineNanos = var1 + 30000000000L;
      lastPromptNanos = var1;
      sendOverlayMessage(var0, activeSelection.prompt());
      LarpLog.info("Rig", "Selection started: {}", activeSelection.prompt());
   }

   public static void endSelection(String var0) {
      if (activeSelection != null) {
         activeSelection = null;
         sendOverlayMessage(Minecraft.getInstance(), "Selection " + var0);
         LarpLog.info("Rig", "Selection {}", var0);
      }
   }

   private static void sendOverlayMessage(Minecraft var0, String var1) {
      if (var0.player != null) {
         var0.player.sendOverlayMessage(Component.literal(var1));
      }
   }

   private static void registerHooks() {
      if (!hooksRegistered) {
         hooksRegistered = (boolean)1;
         ClientTickEvents.END_CLIENT_TICK.register(WorldSelectionPicker::tick);
         LevelRenderEvents.BEFORE_GIZMOS.register((BeforeGizmos)var0 -> renderHighlight());
      }
   }

   private static void tick(Minecraft var0) {
      if (activeSelection != null) {
         if (var0.player != null && var0.level != null) {
            long var1 = System.nanoTime();
            if (var1 > deadlineNanos) {
               endSelection("timed out");
            } else if (activeSelection instanceof WorldSelectionPicker.BlockSelection && var0.gui.screen() != null) {
               endSelection("cancelled");
            } else if (activeSelection instanceof WorldSelectionPicker.ItemSelection && !(var0.gui.screen() instanceof AbstractContainerScreen)) {
               endSelection("cancelled");
            } else {
               if (activeSelection instanceof WorldSelectionPicker.BlockSelection && var1 - lastPromptNanos > 1500000000L) {
                  lastPromptNanos = var1;
                  sendOverlayMessage(var0, activeSelection.prompt());
               }
            }
         } else {
            activeSelection = null;
         }
      }
   }

   private static AABB blockBox(BlockPos var0) {
      return new AABB(var0.getX() - 0.002, var0.getY() - 0.002, var0.getZ() - 0.002, var0.getX() + 1.002, var0.getY() + 1.002, var0.getZ() + 1.002);
   }

   private static void renderHighlight() {
      Minecraft var0 = Minecraft.getInstance();
      if (var0.level != null) {
         if (activeSelection instanceof WorldSelectionPicker.BlockSelection var1
            && var0.hitResult instanceof BlockHitResult var2
            && var2.getType() == Type.BLOCK) {
            boolean var6 = var1.valid().test(var0.level.getBlockState(var2.getBlockPos()));
            Gizmos.cuboid(blockBox(var2.getBlockPos()), GizmoStyle.stroke(var6 ? -13180806 : -2074034, 2.5F)).setAlwaysOnTop();
         }

         long var4 = System.nanoTime();
         if (highlightedPos != null && var4 < highlightEndNanos) {
            int var7 = (int)(110L * (highlightEndNanos - var4) / 1.2E9);
            Gizmos.cuboid(blockBox(highlightedPos), GizmoStyle.strokeAndFill(-13180806, 2.5F, var7 << 24 | 3596410)).setAlwaysOnTop();
         } else {
            highlightedPos = null;
         }
      }
   }

   public static boolean handleBlockClick(Minecraft var0) {
      if (!(activeSelection instanceof WorldSelectionPicker.BlockSelection var1 && var0.level != null)) {
         return false;
      } else if (var0.hitResult instanceof BlockHitResult var5 && var5.getType() == Type.BLOCK) {
         BlockPos var6 = var5.getBlockPos().immutable();
         if (!var1.valid().test(var0.level.getBlockState(var6))) {
            sendOverlayMessage(var0, var1.invalid());
            return true;
         } else {
            activeSelection = null;
            String var4 = var1.apply().apply(var6);
            highlightedPos = var6;
            highlightEndNanos = System.nanoTime() + 1200000000L;
            sendOverlayMessage(var0, var4);
            RigLog.logMessage("Casino", var4);
            return true;
         }
      } else {
         sendOverlayMessage(var0, var1.prompt());
         return true;
      }
   }

   public static boolean handleSlotClick(Slot var0, AbstractContainerMenu var1) {
      if (activeSelection instanceof WorldSelectionPicker.ItemSelection var2 && var0 != null) {
         Minecraft var6 = Minecraft.getInstance();
         ItemStack var4 = var0.getItem();
         if (!var4.isEmpty() && var2.valid().test(var4)) {
            activeSelection = null;
            String var5 = var2.apply().apply(var4.copy());
            highlightedMenu = var1;
            highlightedSlot = var0.index;
            slotHighlightEndNanos = System.nanoTime() + 1200000000L;
            sendOverlayMessage(var6, var5);
            RigLog.logMessage("Casino", var5);
            return true;
         } else {
            sendOverlayMessage(var6, var2.invalid());
            return true;
         }
      } else {
         return false;
      }
   }

   public static int getSlotHighlightColor(Slot var0, boolean var1, AbstractContainerMenu var2) {
      long var3 = System.nanoTime();
      if (highlightedSlot >= 0 && var3 >= slotHighlightEndNanos) {
         highlightedSlot = -1;
         highlightedMenu = null;
      }

      if (highlightedSlot >= 0 && var2 == highlightedMenu && var0.index == highlightedSlot) {
         int var7 = (int)(150L * (slotHighlightEndNanos - var3) / 1.2E9);
         return var7 << 24 | 3596410;
      }

      if (activeSelection instanceof WorldSelectionPicker.ItemSelection var5 && !var0.getItem().isEmpty()) {
         boolean var8 = var5.valid().test(var0.getItem());
         if (var1) {
            return var8 ? -2143887238 : -2132780466;
         } else {
            return var8 ? 808902778 : 0;
         }
      } else {
         return 0;
      }
   }

   private record BlockSelection(String prompt, Predicate<BlockState> valid, String invalid, Function<BlockPos, String> apply)
      implements WorldSelectionPicker.Selection {
   }

   private record ItemSelection(String prompt, Predicate<ItemStack> valid, String invalid, Function<ItemStack, String> apply)
      implements WorldSelectionPicker.Selection {
   }

   private sealed interface Selection permits WorldSelectionPicker.BlockSelection, WorldSelectionPicker.ItemSelection {
      String prompt();
   }
}
