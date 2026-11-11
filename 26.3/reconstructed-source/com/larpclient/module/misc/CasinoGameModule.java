package com.larpclient.module.misc;

import com.larpclient.module.world.WorldSelectionPicker;
import com.larpclient.util.data.BindCodes;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.RigLog;
import com.larpclient.util.world.DispenserRegistry;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;

public abstract class CasinoGameModule extends Module {
   private static final List<CasinoGameModule> instances = new ArrayList<>();
   private static boolean switching;
   private final CasinoRigManager.Game game;
   private final List<CasinoGameModule.BindAction> binds = new ArrayList<>();
   private final Map<KeybindSetting, Boolean> bindStates = new IdentityHashMap<>();
   private final List<CasinoGameModule.SetupAction> setupActions = new ArrayList<>();
   private final List<Entry<String, DispenserRegistry.GameType>> tableSelections = new ArrayList<>();

   protected CasinoGameModule(String var1, String var2, CasinoRigManager.Game var3) {
      super(var1, var2, Category.PLAYER, -1);
      this.game = var3;
      synchronized (instances) {
         instances.add(this);
      }
   }

   public final CasinoRigManager.Game getGame() {
      return this.game;
   }

   protected final CasinoRigManager getRig() {
      return CasinoRigManager.getInstance();
   }

   protected final KeybindSetting addBind(String var1, String var2, Runnable var3) {
      return this.addBindOption(var1, var2, var3, false);
   }

   protected final KeybindSetting addBindOption(String var1, String var2, Runnable var3, boolean var4) {
      KeybindSetting var5 = this.addSetting(new KeybindSetting(var1, var2, -1));
      this.binds.add(new CasinoGameModule.BindAction(var5, var3, var4));
      return var5;
   }

   protected final KeybindSetting addTableBind(String var1, DispenserRegistry.GameType var2) {
      return this.addBind(
         var1,
         "Look at a dispenser and press to assign it as the " + var2.DISPLAYNAME + " table",
         () -> showResult(DispenserRegistry.getInstance().assignLookedAt(getMc(), var2))
      );
   }

   public final List<CasinoGameModule.SetupAction> getSetupActions() {
      return List.copyOf(this.setupActions);
   }

   public final List<Setting<?>> getBindSettings() {
      ArrayList var1 = new ArrayList();

      for (CasinoGameModule.BindAction var3 : this.binds) {
         var1.add(var3.key());
      }

      return var1;
   }

   protected final void addSetupAction(String var1, Runnable var2) {
      this.setupActions.add(new CasinoGameModule.SetupAction(var1, var2));
   }

   protected final void getSetupActions(String var1, Runnable var2, Supplier<String> var3) {
      this.setupActions.add(new CasinoGameModule.SetupAction(var1, var2, var3));
   }

   protected static String formatSelected(String var0, String var1) {
      String var2 = var0.startsWith("Select ") ? var0.substring(7) : var0;
      String var3 = Character.toUpperCase(var2.charAt(0)) + var2.substring(1) + " selected";
      return var1 != null && !var1.isBlank() ? var3 + ": " + var1 : var3;
   }

   protected final void addTableSelector(String var1, DispenserRegistry.GameType var2) {
      String var3 = var1.startsWith("Select ") ? var1.substring(7) : var1;
      this.tableSelections.add(Map.entry(Character.toUpperCase(var3.charAt(0)) + var3.substring(1), var2));
      this.getSetupActions(
         var1,
         () -> WorldSelectionPicker.selectBlock(
            "Right-click the " + var2.DISPLAYNAME + " dispenser (Esc cancels)",
            var0x -> var0x.getBlock() instanceof DispenserBlock,
            "That is not a dispenser",
            var1x -> {
               DispenserRegistry var2x = DispenserRegistry.getInstance();
               DispenserRegistry.AssignResult var3x = var2x.assign(var2x.getActiveKey(getMc(), var1x), var2);
               return var3x.message();
            }
         ),
         () -> {
            int var2x = DispenserRegistry.getInstance().setLookedAtGameType(var2).size();
            if (var2x == 0) {
               return var1;
            }

            String var3x = formatSelected(var1, "");
            return var2x == 1 ? var3x : var2x + " " + var3x.replace("table selected", "tables selected");
         }
      );
   }

   protected final void addSetupAction() {
      this.addSetupAction(
         "Remove a table",
         () -> WorldSelectionPicker.selectBlock(
            "Right-click the dispenser to remove (Esc cancels)", var0 -> var0.getBlock() instanceof DispenserBlock, "That is not a dispenser", var0 -> {
               DispenserRegistry var1 = DispenserRegistry.getInstance();
               return var1.unassign(var1.getActiveKey(getMc(), var0)).message();
            }
         )
      );
   }

   protected final void getSetupActions(String var1, String var2, Predicate<ItemStack> var3, String var4, Function<ItemStack, String> var5) {
      this.addSetupAction(var1, () -> WorldSelectionPicker.tick("Click the " + var2 + " in your inventory (Esc cancels)", var3, var4, var5));
   }

   protected final void stopAll(String var1, String var2, Predicate<ItemStack> var3, String var4, Function<ItemStack, String> var5, Supplier<String> var6) {
      this.getSetupActions(var1, () -> WorldSelectionPicker.tick("Click the " + var2 + " in your inventory (Esc cancels)", var3, var4, var5), () -> {
         String var2x = (String)var6.get();
         return var2x == null ? var1 : formatSelected(var1, var2x);
      });
   }

   protected final void addCommonActions() {
      this.addSetupAction();
      this.addSetupAction("Open game log", CasinoLogScreen::open);
      this.addSetupAction("Stop all games", CasinoGameModule::stopAll);
   }

   public String setupHint() {
      return "Press a Select button, then right-click the block or click the item. It flashes green when chosen. Press a selected button again to change it.";
   }

   public static void stopAll() {
      CasinoRigManager.getInstance().stopAll();
      List var0;
      synchronized (instances) {
         var0 = List.copyOf(instances);
      }

      for (CasinoGameModule var2 : var0) {
         if (var2.isEnabled()) {
            var2.disable();
         }
      }

      addSetupAction("All casino games stopped");
   }

   public static CasinoGameModule getActiveGame() {
      List var0;
      synchronized (instances) {
         var0 = List.copyOf(instances);
      }

      CasinoGameModule var5 = null;

      for (CasinoGameModule var3 : var0) {
         if (var3.isEnabled()) {
            if (var3.game != CasinoRigManager.Game.NONE) {
               return var3;
            }

            var5 = var3;
         }
      }

      return var5;
   }

   public final List<String[]> getHudRows() {
      ArrayList var1 = new ArrayList();
      if (this.game != CasinoRigManager.Game.NONE) {
         CasinoRigManager var2 = this.getRig();
         String var3 = this.getStatus();
         var1.add(new String[]{this.targetName(), var3.isEmpty() ? "-" : var3});
         if (var2.isPaused()) {
            var1.add(new String[]{"State", "Paused: unassigned dispenser"});
         } else if (CasinoRigManager.isBusy()) {
            var1.add(new String[]{"State", "Swapping cards"});
         }

         for (Entry var5 : this.tableSelections) {
            int var6 = DispenserRegistry.getInstance().setLookedAtGameType((DispenserRegistry.GameType)var5.getValue()).size();
            var1.add(new String[]{(String)var5.getKey(), var6 == 0 ? "Not selected" : (var6 == 1 ? "Selected" : var6 + " selected")});
         }
      }

      this.addHudRows(var1);
      return var1;
   }

   protected String targetName() {
      return "Target";
   }

   protected void addHudRows(List<String[]> var1) {
   }

   public String setupSummary() {
      if (getMc() != null && getMc().level != null) {
         StringBuilder var1 = new StringBuilder();

         for (Entry var3 : this.tableSelections) {
            if (!var1.isEmpty()) {
               var1.append('\n');
            }

            var1.append(describeTables((String)var3.getKey(), (DispenserRegistry.GameType)var3.getValue()));
         }

         return var1.toString();
      } else {
         return "Join a world to select tables";
      }
   }

   protected static String describeTables(String var0, DispenserRegistry.GameType var1) {
      List var2 = DispenserRegistry.getInstance().setLookedAtGameType(var1);
      if (var2.isEmpty()) {
         return var0 + ": none selected";
      }

      StringBuilder var3 = new StringBuilder(var0).append(": ");

      for (int var4 = 0; var4 < var2.size(); var4++) {
         BlockPos var5 = DispenserRegistry.getInstance().getPosition((String)var2.get(var4));
         if (var4 > 0) {
            var3.append(", ");
         }

         if (var4 == 3) {
            var3.append("+").append(var2.size() - 3).append(" more");
            break;
         }

         var3.append(var5.getX()).append(" ").append(var5.getY()).append(" ").append(var5.getZ());
      }

      return var3.toString();
   }

   protected static String addTableBind(Item var0) {
      return !var0.builtInRegistryHolder().areComponentsBound()
         ? Component.translatable(var0.getDescriptionId()).getString()
         : new ItemStack(var0).getHoverName().getString();
   }

   protected final KeybindSetting addRemoveBind() {
      return this.addBind(
         "Remove Bind",
         "Look at an assigned dispenser and press to remove its game assignment",
         () -> showResult(DispenserRegistry.getInstance().unassignLookedAt(getMc()))
      );
   }

   protected final KeybindSetting addLogBind() {
      return this.addBind("Log Bind", "Open the recent game log", CasinoLogScreen::openIfNoScreen);
   }

   protected final KeybindSetting addStopBind() {
      return this.addBindOption("Stop Bind", "Stop every casino game and drop pending actions", CasinoGameModule::stopAll, true);
   }

   private static void showResult(DispenserRegistry.AssignResult var0) {
      addSetupAction(var0.message());
   }

   protected static void addSetupAction(String var0) {
      if (var0 != null && !var0.isBlank()) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.player != null) {
            var1.player.sendOverlayMessage(Component.literal(var0));
         }

         RigLog.logMessage("Casino", var0);
      }
   }

   private static void getActiveGame(CasinoRigManager.Game var0) {
      if (!switching) {
         switching = (boolean)1;

         try {
            List var1;
            synchronized (instances) {
               var1 = List.copyOf(instances);
            }

            for (CasinoGameModule var3 : var1) {
               if (var3.game != CasinoRigManager.Game.NONE) {
                  int var4 = var3.game == var0 ? 1 : 0;
                  if (var4 != 0 && !var3.isEnabled()) {
                     var3.enable();
                  } else if (var4 == 0 && var3.isEnabled()) {
                     var3.disable();
                  }
               }
            }
         } finally {
            switching = (boolean)0;
         }
      }
   }

   @Override
   public void onEnable() {
      CasinoRigManager var1 = this.getRig();
      var1.getInstance();
      this.bindStates.clear();
      this.applySettings();
      if (this.game != CasinoRigManager.Game.NONE) {
         var1.activateGame(this.game);
         LarpLog.info("Rig", "{} enabled", this.getName());
      }
   }

   @Override
   public void onDisable() {
      this.bindStates.clear();
      CasinoRigManager var1 = this.getRig();
      if (this.game != CasinoRigManager.Game.NONE && var1.getActiveGame() == this.game) {
         var1.releaseGame();
         DispenserRegistry.getInstance().saveAssignments();
         LarpLog.info("Rig", "{} disabled; game state released", this.getName());
      }

      this.releaseExtra();
   }

   protected void releaseExtra() {
   }

   protected abstract void applySettings();

   @Override
   public void onTick() {
      Minecraft var1 = getMc();
      this.getRig().tick(var1);
      this.pollBinds(var1);
      this.afterTick(var1);
   }

   protected void afterTick(Minecraft var1) {
   }

   private void pollBinds(Minecraft var1) {
      if (var1.getWindow() != null) {
         int var2 = var1.gui.screen() != null ? 1 : 0;
         long var3 = var1.getWindow().handle();

         for (CasinoGameModule.BindAction var6 : this.binds) {
            int var7 = var6.key().getValue();
            int var8 = var7 > 0 && BindCodes.isPressed(var3, var7) ? 1 : 0;
            boolean var9 = this.bindStates.getOrDefault(var6.key(), false);
            this.bindStates.put(var6.key(), Boolean.valueOf((boolean)var8));
            if (var8 != 0 && !var9 && (var2 == 0 || var6.inScreen() || this.screenAllowsBinds(var1))) {
               try {
                  var6.action().run();
               } catch (RuntimeException var11) {
                  LarpLog.error("Rig", this.getName() + " bind failed", var11);
               }
            }
         }
      }
   }

   protected boolean screenAllowsBinds(Minecraft var1) {
      return false;
   }

   @Override
   public String getStatus() {
      CasinoRigManager var1 = this.getRig();
      return var1.getActiveGame() == this.game ? var1.updateGameFlags(this.game) : "";
   }

   static {
      CasinoRigManager.getInstance().addListener(CasinoGameModule::getActiveGame);
   }

   private record BindAction(KeybindSetting key, Runnable action, boolean inScreen) {
   }

   public record SetupAction(String label, Runnable run, Supplier<String> current) {
      public SetupAction(String var1, Runnable var2) {
         this(var1, var2, () -> var1);
      }

      public String currentLabel() {
         try {
            String var1 = this.current.get();
            return var1 != null && !var1.isBlank() ? var1 : this.label;
         } catch (RuntimeException var2) {
            return this.label;
         }
      }
   }
}
