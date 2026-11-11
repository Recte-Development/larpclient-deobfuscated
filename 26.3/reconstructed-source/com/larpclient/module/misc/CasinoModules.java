package com.larpclient.module.misc;

import com.larpclient.config.VisualWinnerConfig;
import com.larpclient.gui.screen.ArrowGameLayout;
import com.larpclient.module.exploit.BlackjackRigger;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.module.exploit.DispenserMinigameSpoofer;
import com.larpclient.module.exploit.HigherLowerSwapper;
import com.larpclient.module.exploit.HotbarForceQueue;
import com.larpclient.module.exploit.HotbarSwapQueue2;
import com.larpclient.module.exploit.OddEvenCardForce;
import com.larpclient.module.player.FishingRodSwapper;
import com.larpclient.module.world.WorldSelectionPicker;
import com.larpclient.util.data.BlackjackTracker;
import com.larpclient.util.inventory.DispenserRemap;
import com.larpclient.util.inventory.DispenserSpoofer;
import com.larpclient.util.misc.BaccaratRig;
import com.larpclient.util.misc.FiftyFiftyDispenser;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.DispenserRegistry;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;

public final class CasinoModules {
   private static final String[] handSides = new String[]{"Player", "Hand 2", "Hand 3", "Dealer"};

   private CasinoModules() {
   }

   private static String capitalize(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1).toLowerCase(Locale.ROOT);
   }

   private static int handIndex(String var0) {
      return switch (var0) {
         case "Hand 2" -> 1;
         case "Hand 3" -> 2;
         case "Dealer" -> -1;
         default -> 0;
      };
   }

   private static String handName(int var0) {
      return var0 < 0 ? "Dealer" : (var0 == 1 ? "Hand 2" : (var0 == 2 ? "Hand 3" : "Player"));
   }

   private static void addHandRows(List<String[]> var0, int var1) {
      BlackjackAssistant var2 = BlackjackAssistant.getInstance();

      for (int var3 = 0; var3 < var1; var3++) {
         var0.add(new String[]{var1 == 1 ? "Player" : "Hand " + (var3 + 1), var2.handText(var3)});
      }

      var0.add(new String[]{"Dealer", var2.handText(-1)});
      int var4 = CardForce.getInstance().countTemplates();
      var0.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
   }

   public static final class ArmorGameModule extends CasinoModules.DispenserWinnerModule {
      public ArmorGameModule() {
         super(
            "Armor Game",
            "Shows the chosen armor piece as the winner",
            CasinoRigManager.Game.ARMOR_GAME,
            DispenserMinigameSpoofer.aRMOR_GAME,
            4,
            DispenserRegistry.GameType.ARMOR_GAME
         );
      }
   }

   public static final class ArrowGameModule extends CasinoGameModule {
      private final ModeSetting targetSetting = this.addSetting(new ModeSetting("Target", "Whether the shot is shown as a win or a loss", "Win", "Win", "Lose"));

      @Override
      public String setupSummary() {
         return super.setupSummary() + "\nX map: " + (ArrowGame.getInstance().hasXMap() ? "selected" : "none");
      }

      public ArrowGameModule() {
         super("Arrow Game", "Shows the arrow dispenser and item frames for a win or a loss, visually only", CasinoRigManager.Game.ARROW_GAME);
         this.addBind("Side Bind", "Switch between win and lose", this::toggleTarget);
         this.addBind("Note Block Bind", "Look at a note block and press to bind it as the fake press trigger", this::bindLookedNoteBlock);
         this.addBind("X Map Bind", "Hold the filled map used as the X mark and press to store it", () -> {
            ArrowGame.getInstance().setXMapFromHand();
            addSetupAction("Arrow Game: X map stored");
         });
         this.addBind("Clear X Map Bind", "Forget the stored X map", () -> {
            ArrowGame.getInstance().getTargetLabel();
            addSetupAction("Arrow Game: X map cleared");
         });
         this.addTableBind("Dispenser Bind", DispenserRegistry.GameType.ARROW_GAME);
         this.addTableSelector("Select table", DispenserRegistry.GameType.ARROW_GAME);
         this.addRemoveBind();
         this.addLogBind();
         this.addStopBind();
         this.stopAll(
            "Select X map",
            "filled map used as the X mark",
            var0 -> var0.is(Items.FILLED_MAP) && var0.get(DataComponents.MAP_ID) != null,
            "Pick a filled map",
            var0 -> {
               int var1 = ((MapId)var0.get(DataComponents.MAP_ID)).id();
               ArrowGameLayout.getInstance().setSlot(var1);
               return "Arrow Game X map: map #" + var1;
            },
            () -> ArrowGame.getInstance().hasXMap() ? "" : null
         );
         this.getSetupActions(
            "Select note block",
            () -> WorldSelectionPicker.selectBlock(
               "Right-click the note block that fires the shot (Esc cancels)",
               var0 -> var0.getBlock() instanceof NoteBlock,
               "That is not a note block",
               var0 -> {
                  ArrowGame.getInstance().bindNoteBlock(Minecraft.getInstance(), var0);
                  return "Arrow Game note block selected";
               }
            ),
            () -> {
               int var0 = ArrowGameLayout.getInstance().noteblockCount();
               return var0 == 0 ? "Select note block" : (var0 == 1 ? "Note block selected" : var0 + " note blocks selected");
            }
         );
         this.getSetupActions("Clear X map", () -> {
            ArrowGame.getInstance().getTargetLabel();
            addSetupAction("Arrow Game: X map cleared");
         }, () -> ArrowGame.getInstance().hasXMap() ? "Clear X map" : "No X map to clear");
         this.addCommonActions();
         this.targetSetting.onChange(var1 -> this.applySettings());
      }

      private void bindLookedNoteBlock() {
         Minecraft var1 = Minecraft.getInstance();
         if (var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var2.getType() == Type.BLOCK
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof NoteBlock) {
            ArrowGame.getInstance().bindNoteBlock(var1, var2.getBlockPos());
            addSetupAction("Arrow Game: note block bound");
         } else {
            addSetupAction("Look at a note block first");
         }
      }

      private void toggleTarget() {
         this.targetSetting.setValue(this.targetSetting.isMode("Win") ? "Lose" : "Win");
         addSetupAction("Arrow Game: " + this.targetSetting.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            ArrowGame.getInstance().setTarget(this.targetSetting.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         int var2 = ArrowGameLayout.getInstance().noteblockCount();
         var1.add(new String[]{"Note block", var2 == 0 ? "Not selected" : (var2 == 1 ? "Selected" : var2 + " selected")});
         var1.add(new String[]{"X map", ArrowGame.getInstance().hasXMap() ? "Selected" : "Not selected"});
      }
   }

   public static final class BaccaratModule extends CasinoGameModule {
      private final ModeSetting targetSetting = this.addSetting(
         new ModeSetting(
            "Target", "Which hand the automatic plan makes win. Off leaves the draws alone unless you queue a preset", "Player", "Player", "Banker", "Off"
         ) {
            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Off") ? "Off (presets only)" : var1;
            }
         }
      );
      private final TextSetting[] presetSettings = new TextSetting[5];
      private final KeybindSetting[] presetBinds = new KeybindSetting[5];

      public BaccaratModule() {
         super("Baccarat", "Counts the hands and queues card values for the next draws", CasinoRigManager.Game.BACCARAT);
         this.addBind("Side Bind", "Switch between player and banker", this::toggleSide);
         this.addTableBind("Player Dispenser Bind", DispenserRegistry.GameType.BACCARAT_PLAYER);
         this.addTableSelector("Select player table", DispenserRegistry.GameType.BACCARAT_PLAYER);
         this.addTableBind("Banker Dispenser Bind", DispenserRegistry.GameType.BACCARAT_BANKER);
         this.addTableSelector("Select banker table", DispenserRegistry.GameType.BACCARAT_BANKER);
         this.addRemoveBind();

         for (int var1 = 0; var1 < 5; var1++) {
            int var2 = var1;
            this.presetSettings[var1] = this.addSetting(
               new TextSetting("Preset " + (var1 + 1), "Up to three card values 1 to 9 separated by spaces, queued by the preset bind", "")
            );
            this.presetSettings[var1].onChange(var1x -> this.applySettings());
            this.presetBinds[var1] = this.addBind("Preset " + (var1 + 1) + " Bind", "Queue the values in Preset " + (var1 + 1), () -> this.queuePreset(var2));
         }

         this.addLogBind();
         this.addStopBind();
         this.addCommonActions();
         this.targetSetting.onChange(var1x -> this.applySettings());
      }

      private static int[] parsePreset(String var0) {
         int[] var1 = new int[3];
         int var2 = 0;
         if (var0 != null) {
            for (String var6 : var0.trim().split("[\\s,]+")) {
               if (var2 == var1.length) {
                  break;
               }

               if (var6.matches("[1-9]")) {
                  var1[var2++] = Integer.parseInt(var6);
               }
            }
         }

         return Arrays.copyOf(var1, var2);
      }

      private void queuePreset(int var1) {
         int[] var2 = parsePreset(this.presetSettings[var1].getValue());
         if (var2.length == 0) {
            addSetupAction("Baccarat preset " + (var1 + 1) + " is empty");
         } else if (HotbarForceQueue.getInstance().isSwapBusy()) {
            addSetupAction("Baccarat preset " + (var1 + 1) + " not queued: a card is being swapped, press it again after this draw");
         } else {
            if (this.getRig().releaseGame(var1, var2)) {
               addSetupAction("Baccarat preset " + (var1 + 1) + " queued for the next draws: " + formatValues(var2));
            } else {
               addSetupAction("Baccarat preset " + (var1 + 1) + " not queued: open a Baccarat table once so the cards are learned");
            }
         }
      }

      private static String formatValues(int[] var0) {
         StringBuilder var1 = new StringBuilder();

         for (int var5 : var0) {
            var1.append(var1.isEmpty() ? "" : " ").append(var5);
         }

         return var1.toString();
      }

      private static boolean isValidPreset(String var0) {
         if (var0 != null && !var0.isBlank()) {
            String[] var1 = var0.trim().split("[\\s,]+");
            if (var1.length > 3) {
               return false;
            }

            for (String var5 : var1) {
               if (!var5.matches("[1-9]")) {
                  return false;
               }
            }

            return true;
         } else {
            return true;
         }
      }

      @Override
      public String setupSummary() {
         StringBuilder var1 = new StringBuilder(super.setupSummary());

         for (int var2 = 0; var2 < 5; var2++) {
            String var3 = this.presetSettings[var2].getValue();
            if (var3 != null && !var3.isBlank()) {
               int[] var4 = parsePreset(var3);
               var1.append("\nPreset ")
                  .append(var2 + 1)
                  .append(": ")
                  .append(var4.length == 0 ? "no valid values" : formatValues(var4))
                  .append(isValidPreset(var3) ? "" : " (only values 1 to 9, at most three, are used)");
            }
         }

         return var1.toString();
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         BaccaratRig var2 = BaccaratRig.getInstance();
         int var3 = var2.dealtCount() > 0 ? 1 : 0;
         var1.add(new String[]{"Player", var3 != 0 ? Integer.toString(var2.playerTotal()) : "-"});
         var1.add(new String[]{"Banker", var3 != 0 ? Integer.toString(var2.bankerTotal()) : "-"});
         String var4 = var2.presetDisplay();
         var1.add(var4.isEmpty() ? new String[]{"Next card", HotbarForceQueue.getInstance().pendingLabel()} : new String[]{"Preset queue", var4});
         int var5 = HotbarForceQueue.getInstance().countTemplates();
         var1.add(new String[]{"Cards learned", var5 == 0 ? "None, open a table once" : Integer.toString(var5)});
      }

      private void toggleSide() {
         this.targetSetting.setValue(this.targetSetting.isMode("Player") ? "Banker" : "Player");
         addSetupAction("Baccarat: " + this.targetSetting.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            BaccaratRig.getInstance().setTarget(this.targetSetting.getValue().toLowerCase(Locale.ROOT), HotbarForceQueue.getInstance());
            int[] var1 = new int[15];
            Arrays.fill(var1, -1);

            for (int var2 = 0; var2 < 5; var2++) {
               int[] var3 = parsePreset(this.presetSettings[var2].getValue());
               System.arraycopy(var3, 0, var1, var2 * 3, var3.length);
            }

            this.getRig().setSlotMapping(var1);
         }
      }
   }

   public static final class BlackjackForceOnlyModule extends CasinoGameModule {
      private final NumberSetting handCountSetting = this.addSetting(new NumberSetting("Hands", "Number of player hands at the table", 1.0, 1.0, 3.0, 1.0));
      private final ModeSetting drawForSetting = this.addSetting(
         new ModeSetting(
            "Draw For",
            "Auto prepares the player's safe draws at 12 to 16, then the dealer's losing draw once the player stands at 17. Player or Dealer keeps one side",
            "Auto",
            "Auto",
            "Player",
            "Dealer"
         )
      );

      public BlackjackForceOnlyModule() {
         super("Blackjack Force Only", "Prepares the dispenser for the draw you want; the server decides what drops", CasinoRigManager.Game.BLACKJACK_RIG);
         this.addBindOption("Side Bind", "Cycle Auto, Player and Dealer", this::cycleDrawFor, true);
         this.addBind("Import Hotbar Bind", "Read the card map templates from your hotbar", this::importHotbar);
         this.addTableBind("Dispenser Bind", DispenserRegistry.GameType.BLACKJACK_FORCE_ONLY);
         this.addTableSelector("Select table", DispenserRegistry.GameType.BLACKJACK_FORCE_ONLY);
         this.addRemoveBind();
         this.addLogBind();
         this.addStopBind();
         this.getSetupActions(
            "Import hotbar cards", this::importHotbar, () -> BlackjackRigger.getInstance().hasMapMapping() ? "Hotbar cards imported" : "Import hotbar cards"
         );
         this.addCommonActions();
         this.drawForSetting.onChange(var1 -> this.applySettings());
         this.handCountSetting.onChange(var1 -> this.applySettings());
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         CasinoModules.addHandRows(var1, (int)Math.round(this.handCountSetting.getValue()));
         var1.add(new String[]{"Card values", BlackjackRigger.getInstance().hasMapMapping() ? "From hotbar" : "From table slots"});
      }

      private void importHotbar() {
         String var1 = BlackjackRigger.getInstance().importHotbar(Minecraft.getInstance());
         addSetupAction(var1 == null ? "Blackjack Force Only: hotbar card maps imported" : "Hotbar import failed: " + var1);
         if (var1 == null) {
            this.getRig().saveConfig();
         }
      }

      private void cycleDrawFor() {
         this.drawForSetting.cycleNext();
         addSetupAction("Blackjack Force Only draws for: " + this.drawForSetting.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            BlackjackAssistant var1 = BlackjackAssistant.getInstance();
            var1.setHandCount((int)Math.round(this.handCountSetting.getValue()));
            var1.updateSide(this.drawForSetting.getValue());
         }
      }

      @Override
      public void onDisable() {
         super.onDisable();
         BlackjackAssistant.getInstance().updateSide("auto");
      }

      @Override
      protected boolean screenAllowsBinds(Minecraft var1) {
         return BlackjackRigger.getInstance().isDispenserScreenOpen(var1);
      }
   }

   public static final class BlackjackModule extends CasinoGameModule {
      private final NumberSetting handCountSetting = this.addSetting(new NumberSetting("Hands", "Number of player hands at the table", 1.0, 1.0, 3.0, 1.0));
      private final ModeSetting sideSetting = this.addSetting(
         new ModeSetting(
            "Side",
            "Who takes the next cards. Player hands are counted; dealer draws are prepared to beat the best hand. Starts each round on the first hand",
            "Player",
            CasinoModules.handSides
         ) {
            @Override
            public List<String> getVisibleModes() {
               int var1 = BlackjackModule.this.handCount();
               return Arrays.stream(CasinoModules.handSides).filter(var1x -> var1x.equals("Dealer") || CasinoModules.handIndex(var1x) < var1).toList();
            }

            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Player") && BlackjackModule.this.handCount() > 1 ? "Hand 1" : var1;
            }
         }
      );

      public BlackjackModule() {
         super("Blackjack", "Counts every hand and prepares the dealer's draws to beat the best player hand", CasinoRigManager.Game.BLACKJACK_ASSIST);
         this.addBind("Side Bind", "Move to the next hand, then the dealer", this::cycleSide);
         this.addTableBind("Dispenser Bind", DispenserRegistry.GameType.BLACKJACK);
         this.addTableSelector("Select table", DispenserRegistry.GameType.BLACKJACK);
         this.addRemoveBind();
         this.addLogBind();
         this.addStopBind();
         this.addCommonActions();
         this.sideSetting.onChange(var1 -> this.applySettings());
         this.handCountSetting.onChange(var1 -> {
            if (CasinoModules.handIndex(this.sideSetting.getValue()) >= this.handCount()) {
               this.sideSetting.setValue("Player");
            }

            this.applySettings();
         });
      }

      private int handCount() {
         return (int)Math.round(this.handCountSetting.getValue());
      }

      private void cycleSide() {
         this.sideSetting.cycleNext();
         addSetupAction("Blackjack: " + this.sideSetting.getModeLabel(this.sideSetting.getValue()) + " draws next");
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            BlackjackAssistant var1 = BlackjackAssistant.getInstance();
            var1.setHandCount(this.handCount());
            var1.armRecommendation(CasinoModules.handIndex(this.sideSetting.getValue()));
         }
      }

      @Override
      protected void afterTick(Minecraft var1) {
         String var2 = CasinoModules.handName(BlackjackAssistant.getInstance().allowsCard());
         if (!this.sideSetting.isMode(var2) && this.sideSetting.acceptsValue(var2)) {
            this.sideSetting.setValue(var2);
         }
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         CasinoModules.addHandRows(var1, this.handCount());
         var1.add(new String[]{"Dealer plan", BlackjackAssistant.getInstance().pickDealerCard()});
      }
   }

   abstract static class DispenserWinnerModule extends CasinoGameModule {
      private final DispenserMinigameSpoofer spoofer;
      private final int winnerCount;
      private final ModeSetting winnerSetting;

      DispenserWinnerModule(String var1, String var2, CasinoRigManager.Game var3, DispenserMinigameSpoofer var4, int var5, DispenserRegistry.GameType var6) {
         super(var1, var2, var3);
         this.spoofer = var4;
         this.winnerCount = var5;
         String[] var7 = new String[var5];

         for (int var8 = 0; var8 < var5; var8++) {
            var7[var8] = Integer.toString(var8 + 1);
         }

         this.winnerSetting = this.addSetting(new ModeSetting("Winner", "Which piece is shown as the winner", "1", var7) {
            @Override
            public String getModeLabel(String var1) {
               return DispenserWinnerModule.this.winnerLabel(Integer.parseInt(var1));
            }
         });
         this.addBind("Side Bind", "Advance to the next winner", this::cycleWinner);
         this.addTableBind("Dispenser Bind", var6);
         this.addTableSelector("Select table", var6);
         this.addRemoveBind();
         this.addLogBind();
         this.addStopBind();
         this.addCommonActions();
         this.winnerSetting.onChange(var1x -> this.applySettings());
      }

      private String winnerLabel(int var1) {
         String var2 = this.spoofer == DispenserMinigameSpoofer.hORSE_RACING
            ? (Minecraft.getInstance() != null && Minecraft.getInstance().level != null ? this.spoofer.getSlotLabel(var1) : "")
            : this.spoofer.setEnabled(var1);
         return var2 != null && !var2.isBlank() ? var2 : (this.spoofer == DispenserMinigameSpoofer.hORSE_RACING ? "Horse " + var1 : Integer.toString(var1));
      }

      private void cycleWinner() {
         this.winnerSetting.setValue(Integer.toString(Integer.parseInt(this.winnerSetting.getValue()) % this.winnerCount + 1));
         addSetupAction(this.getName() + " winner: " + this.winnerSetting.getModeLabel(this.winnerSetting.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            this.spoofer.setSelectedIndex(Integer.parseInt(this.winnerSetting.getValue()));
            this.spoofer.setEnabled(true);
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Table", this.spoofer.getStatus()});
      }
   }

   public static final class EvenOrOddModule extends CasinoGameModule {
      private final ModeSetting paritySetting = this.addSetting(new ModeSetting("Parity", "Which parity is shown as the result", "Odd", "Odd", "Even"));

      public EvenOrOddModule() {
         super("Even or Odd", "Swaps the dealt map card for one of the chosen parity", CasinoRigManager.Game.ODD_EVEN);
         this.addBind("Side Bind", "Switch between odd and even", this::toggleParity);
         this.addTableBind("Dispenser Bind", DispenserRegistry.GameType.ODD_EVEN);
         this.addTableSelector("Select table", DispenserRegistry.GameType.ODD_EVEN);
         this.addRemoveBind();
         this.addLogBind();
         this.addStopBind();
         this.addCommonActions();
         this.paritySetting.onChange(var1 -> this.applySettings());
      }

      private void toggleParity() {
         this.paritySetting.setValue(this.paritySetting.isMode("Odd") ? "Even" : "Odd");
         addSetupAction("Even or Odd: " + this.paritySetting.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            OddEvenCardForce var1 = OddEvenCardForce.getInstance();
            var1.setParity(this.paritySetting.isMode("Even") ? 1 : 0);
            var1.clearQueue();
            this.getRig().saveConfig();
         }
      }

      @Override
      protected String targetName() {
         return "Result";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         OddEvenCardForce var2 = OddEvenCardForce.getInstance();
         var1.add(new String[]{"Next card", var2.pendingLabel()});
         int var3 = var2.countTemplates();
         var1.add(new String[]{"Cards learned", var3 == 0 ? "None, open the table once" : Integer.toString(var3)});
      }
   }

   public static final class FiftyFiftyModule extends CasinoGameModule {
      private final ModeSetting winnerSetting = this.addSetting(new ModeSetting("Winner", "Which of the two items is shown as the winner", "1", "1", "2") {
         @Override
         public String getModeLabel(String var1) {
            return "1".equals(var1) ? "First item" : "Second item";
         }
      });

      @Override
      public String getDisplayName() {
         return "50/50";
      }

      @Override
      public List<String> getSearchAliases() {
         return List.of("50/50", "fifty fifty");
      }

      public FiftyFiftyModule() {
         super("50-50", "Shows one of the two 50/50 items as the winner and keeps the real pull intact", CasinoRigManager.Game.FIFTY_FIFTY);
         this.addBindOption("Side Bind", "Switch the winning item", this::toggleWinner, true);
         this.addTableBind("Dispenser Bind", DispenserRegistry.GameType.FIFTY_FIFTY);
         this.addTableSelector("Select table", DispenserRegistry.GameType.FIFTY_FIFTY);
         this.addRemoveBind();
         this.addLogBind();
         this.addStopBind();
         this.addCommonActions();
         this.winnerSetting.onChange(var1 -> this.applySettings());
      }

      private void toggleWinner() {
         this.winnerSetting.setValue(this.winnerSetting.isMode("1") ? "2" : "1");
         addSetupAction("50/50 winner: " + this.winnerSetting.getModeLabel(this.winnerSetting.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            FiftyFiftyDispenser.getInstance().setChosenIndex(Integer.parseInt(this.winnerSetting.getValue()));
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Table screen", FiftyFiftyDispenser.getInstance().isMenuSynced() ? "Open" : "Closed"});
      }

      @Override
      protected boolean screenAllowsBinds(Minecraft var1) {
         return FiftyFiftyDispenser.getInstance().isMenuSynced();
      }
   }

   public static final class FishingGameModule extends CasinoGameModule {
      @Override
      public String setupSummary() {
         int var1 = FishingRodSwapper.getInstance().findRodSlot();
         return var1 < 0 ? "No lure 3 rod in your inventory" : "Rig rod in inventory slot " + (var1 + 1);
      }

      public FishingGameModule() {
         super("Fishing Game", "Silently casts a lure 3 rod from your inventory when you use any rod", CasinoRigManager.Game.NONE);
         this.addBind("Hide Rod Bind", "Hide or show the rig rod in your inventory display", () -> {
            FishingRodSwapper.getInstance().toggleSpoof();
            addSetupAction("Fishing Game: rod " + (FishingRodSwapper.getInstance().spoofEnabled ? "hidden" : "visible"));
         });
         this.getSetupActions("Hide or show rig rod", () -> {
            FishingRodSwapper.getInstance().toggleSpoof();
            addSetupAction("Fishing Game: rod " + (FishingRodSwapper.getInstance().spoofEnabled ? "hidden" : "visible"));
         }, () -> FishingRodSwapper.getInstance().spoofEnabled ? "Rig rod hidden: press to show" : "Hide rig rod");
         this.addSetupAction("Open game log", CasinoLogScreen::open);
         this.addLogBind();
         this.addStopBind();
      }

      @Override
      public void onEnable() {
         super.onEnable();
         FishingRodSwapper.getInstance().setAutoSwap(true);
      }

      @Override
      protected void releaseExtra() {
         FishingRodSwapper.getInstance().setAutoSwap(false);
      }

      @Override
      protected void applySettings() {
      }

      @Override
      public String getStatus() {
         return FishingRodSwapper.getInstance().autoSwapEnabled ? "Rig on" : "Rig paused";
      }

      @Override
      public String setupHint() {
         return "Keep a lure 3 rod anywhere in your inventory, then cast with any rod.";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         FishingRodSwapper var2 = FishingRodSwapper.getInstance();
         int var3 = var2.findRodSlot();
         var1.add(new String[]{"Rig", var2.autoSwapEnabled ? "On" : "Paused"});
         var1.add(new String[]{"Lure 3 rod", var3 < 0 ? "Not found" : "Slot " + (var3 + 1)});
         var1.add(new String[]{"Rod display", var2.spoofEnabled ? "Hidden" : "Visible"});
      }
   }

   public static final class FortyFiveModule extends CasinoGameModule {
      private final ModeSetting winnerSetting = this.addSetting(
         new ModeSetting("Winner", "Which item is shown as the winning one", "First", "First", "Second", "Middle")
      );
      private final TextSetting slotTargetsSetting = this.addSetting(
         new TextSetting("Slot Targets", "Nine digits, one per dispenser slot from top left: 1 first item, 2 second item, 3 middle item", "112132122")
            .setTitle("112132122")
      );

      @Override
      public String setupSummary() {
         DispenserSpoofer var1 = DispenserSpoofer.getInstance();
         String var2 = this.slotTargetsSetting.getValue() == null ? "" : this.slotTargetsSetting.getValue().trim();
         String var3;
         if (isValidLayout(var2)) {
            var3 = "Slot targets: " + var2.substring(0, 3) + " / " + var2.substring(3, 6) + " / " + var2.substring(6);
         } else {
            int[] var4 = var1.encodeLayout();
            StringBuilder var5 = new StringBuilder();

            for (int var6 = 0; var6 < 9; var6++) {
               var5.append(var6 > 0 && var6 % 3 == 0 ? " / " : "").append(var4[var6]);
            }

            var3 = "Slot Targets needs exactly 9 digits, each 1, 2 or 3. Still using " + var5;
         }

         return super.setupSummary()
            + "\nFirst: "
            + addTableBind(var1.firstItem)
            + ", second: "
            + addTableBind(var1.secondItem)
            + ", middle: "
            + addTableBind(var1.middleItem)
            + "\n"
            + var3;
      }

      @Override
      public String getDisplayName() {
         return "45/45/10";
      }

      @Override
      public List<String> getSearchAliases() {
         return List.of("45/45/10", "forty five");
      }

      private static boolean isValidLayout(String var0) {
         return var0.matches("[123]{9}");
      }

      public FortyFiveModule() {
         super("45-45-10", "Shows a chosen item of the 45/45/10 table as the winner, visually only", CasinoRigManager.Game.SPLIT_45_45_10);
         this.addBind("Side Bind", "Cycle first, second and middle winner", this::cycleWinner);
         this.addBind("From Hand Bind", "Store the item in your hand as the item of the current Winner", () -> this.setWinnerItemFromHand());
         this.addTableBind("Dispenser Bind", DispenserRegistry.GameType.FORTY_FIVE_TEN);
         this.addTableSelector("Select table", DispenserRegistry.GameType.FORTY_FIVE_TEN);
         this.addRemoveBind();
         this.addLogBind();
         this.addStopBind();
         this.stopAll("Select first item", "first (45%) item", var0 -> true, "Pick an item", var1 -> {
            DispenserSpoofer.getInstance().firstItem = var1.getItem();
            this.getRig().saveConfig();
            return "45/45/10 first item: " + var1.getHoverName().getString();
         }, () -> addTableBind(DispenserSpoofer.getInstance().firstItem));
         this.stopAll("Select second item", "second (45%) item", var0 -> true, "Pick an item", var1 -> {
            DispenserSpoofer.getInstance().secondItem = var1.getItem();
            this.getRig().saveConfig();
            return "45/45/10 second item: " + var1.getHoverName().getString();
         }, () -> addTableBind(DispenserSpoofer.getInstance().secondItem));
         this.stopAll("Select middle item", "middle (10%) item", var0 -> true, "Pick an item", var1 -> {
            DispenserSpoofer.getInstance().middleItem = var1.getItem();
            this.getRig().saveConfig();
            return "45/45/10 middle item: " + var1.getHoverName().getString();
         }, () -> addTableBind(DispenserSpoofer.getInstance().middleItem));
         this.addCommonActions();
         this.winnerSetting.onChange(var1 -> this.applySettings());
         this.slotTargetsSetting.onChange(var1 -> this.applySettings());
      }

      private void setWinnerItemFromHand() {
         CasinoRigManager.FromHandResult var1 = this.getRig().setItemFromHand(CasinoRigManager.Game.SPLIT_45_45_10);
         addSetupAction(var1.message);
      }

      private void cycleWinner() {
         this.winnerSetting.setValue(this.winnerSetting.isMode("First") ? "Second" : (this.winnerSetting.isMode("Second") ? "Middle" : "First"));
         addSetupAction("45/45/10 winner: " + this.winnerSetting.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            DispenserSpoofer var1 = DispenserSpoofer.getInstance();

            var1.selectedSlotType = switch ((String)this.winnerSetting.getValue()) {
               case "Second" -> DispenserSpoofer.SlotType.SECOND;
               case "Middle" -> DispenserSpoofer.SlotType.MIDDLE;
               default -> DispenserSpoofer.SlotType.FIRST;
            };
            String var5 = this.slotTargetsSetting.getValue() == null ? "" : this.slotTargetsSetting.getValue().trim();
            if (isValidLayout(var5)) {
               int[] var6 = new int[9];

               for (int var4 = 0; var4 < 9; var4++) {
                  var6[var4] = var5.charAt(var4) - '0';
               }

               var1.loadLayout(var6);
            }

            this.getRig().saveConfig();
         }
      }

      @Override
      protected String targetName() {
         return "Winner";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         DispenserSpoofer var2 = DispenserSpoofer.getInstance();

         Item var3 = switch (var2.selectedSlotType) {
            case FIRST -> var2.firstItem;
            case SECOND -> var2.secondItem;
            case MIDDLE -> var2.middleItem;
         };
         var1.add(new String[]{"Winner item", addTableBind(var3)});
         if (!isValidLayout(this.slotTargetsSetting.getValue() == null ? "" : this.slotTargetsSetting.getValue().trim())) {
            var1.add(new String[]{"Slot Targets", "Invalid, see settings"});
         }
      }
   }

   public static final class HigherOrLowerModule extends CasinoGameModule {
      private final ModeSetting targetSetting = this.addSetting(
         new ModeSetting("Target", "Whether the next card is made to read higher or lower", "Higher", "Higher", "Lower")
      );

      public HigherOrLowerModule() {
         super("Higher or Lower", "Prepares the next card to read higher or lower than the reference", CasinoRigManager.Game.HIGHER_LOWER);
         this.addBind("Side Bind", "Switch between higher and lower", this::toggleTarget);
         this.addTableBind("Dispenser Bind", DispenserRegistry.GameType.HIGHER_LOWER);
         this.addTableSelector("Select table", DispenserRegistry.GameType.HIGHER_LOWER);
         this.addRemoveBind();
         this.addLogBind();
         this.addStopBind();
         this.addCommonActions();
         this.targetSetting.onChange(var1 -> this.applySettings());
      }

      private void toggleTarget() {
         this.targetSetting.setValue(this.targetSetting.isMode("Higher") ? "Lower" : "Higher");
         addSetupAction("Higher or Lower: " + this.targetSetting.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            HigherLowerSwapper.getInstance().setMode(this.targetSetting.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         HigherLowerSwapper var2 = HigherLowerSwapper.getInstance();
         int var3 = var2.getMapId();
         var1.add(new String[]{"Shown card", var3 < 1 ? "-" : Integer.toString(var3)});
         var1.add(new String[]{"Next card", var2.pendingLabel()});
         int var4 = var2.countTemplates();
         var1.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
      }
   }

   public static final class HorseRacingModule extends CasinoModules.DispenserWinnerModule {
      public HorseRacingModule() {
         super(
            "Horse Racing",
            "Shows the chosen horse armor as the winner",
            CasinoRigManager.Game.HORSE_RACING,
            DispenserMinigameSpoofer.hORSE_RACING,
            3,
            DispenserRegistry.GameType.HORSE_RACING
         );
      }
   }

   public static final class MexicanJackModule extends CasinoGameModule {
      private final ModeSetting targetSetting = this.addSetting(
         new ModeSetting(
            "Target",
            "Who takes the next cards after the first two. Player draws are counted; dealer draws are prepared to reach at least the player's total without going over 9",
            "Player",
            "Player",
            "Dealer"
         ) {
            @Override
            public String getModeLabel(String var1) {
               return var1.equals("Player") ? "Player (count)" : "Dealer (prepared)";
            }
         }
      );

      public MexicanJackModule() {
         super("Mexican Jack", "Counts both hands and prepares the dealer's draws", CasinoRigManager.Game.BLACKJACK_DRAWS);
         this.targetSetting.setDisplayName("Drawing");
         this.addBind("Side Bind", "Switch between player and dealer drawing", this::toggleTarget);
         this.addTableBind("Dispenser Bind", DispenserRegistry.GameType.MEXICAN_JACK);
         this.addTableSelector("Select table", DispenserRegistry.GameType.MEXICAN_JACK);
         this.addRemoveBind();
         this.addLogBind();
         this.addStopBind();
         this.addCommonActions();
         this.targetSetting.onChange(var1 -> this.applySettings());
      }

      private void toggleTarget() {
         this.targetSetting.setValue(this.targetSetting.isMode("Player") ? "Dealer" : "Player");
         addSetupAction("Mexican Jack: " + this.targetSetting.getModeLabel(this.targetSetting.getValue()));
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            BlackjackTracker.getInstance().setTurn(this.targetSetting.getValue().toLowerCase(Locale.ROOT), HotbarSwapQueue2.getInstance());
         }
      }

      @Override
      protected String targetName() {
         return "Drawing";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         BlackjackTracker var2 = BlackjackTracker.getInstance();
         var1.add(new String[]{"Player", var2.playerHandText()});
         var1.add(new String[]{"Dealer", var2.dealerHandText()});
         int var3 = var2.dealerHandText();
         var1.add(new String[]{"Next card", var3 < 1 ? "none" : Integer.toString(var3)});
         int var4 = HotbarSwapQueue2.getInstance().countTemplates();
         var1.add(new String[]{"Cards learned", var4 == 0 ? "None, open the table once" : Integer.toString(var4)});
      }
   }

   public static final class PaperModule extends CasinoGameModule {
      private final ModeSetting targetSetting = this.addSetting(
         new ModeSetting("Target", "Which table's drawn paper is shown as the higher one", "Host", "Host", "Viewer")
      );
      private final BooleanSetting universalItemsSetting = this.addSetting(
         new BooleanSetting("Universal Items", "Learn one item set and use it for both tables", true)
      );
      private final TextSetting hostSuffixSetting = this.addSetting(new TextSetting("Host Suffix", "Name suffix that marks host table items", "Host"));
      private final TextSetting viewerSuffixSetting = this.addSetting(new TextSetting("Viewer Suffix", "Name suffix that marks viewer table items", "Viewer"));

      public PaperModule() {
         super("Paper", "Shows the chosen table's paper draw as the higher one, visually only", CasinoRigManager.Game.VISUAL_WINNER);
         this.addBind("Side Bind", "Switch the target between host and viewer", this::toggleTarget);
         this.addTableBind("Host Dispenser Bind", DispenserRegistry.GameType.PAPER_HOST);
         this.addTableSelector("Select host table", DispenserRegistry.GameType.PAPER_HOST);
         this.addTableBind("Viewer Dispenser Bind", DispenserRegistry.GameType.PAPER_VIEWER);
         this.addTableSelector("Select viewer table", DispenserRegistry.GameType.PAPER_VIEWER);
         this.addRemoveBind();
         this.addLogBind();
         this.addStopBind();
         this.addCommonActions();
         this.targetSetting.onChange(var1 -> this.applySettings());
         this.universalItemsSetting.onChange(var1 -> {
            this.applySettings();
            if (this.isEnabled()) {
               LarpLog.info("Rig", "Paper: Universal Items {}", var1 ? "on" : "off");
            }
         });
         this.hostSuffixSetting.onChange(var1 -> this.applySettings());
         this.viewerSuffixSetting.onChange(var1 -> this.applySettings());
      }

      private void toggleTarget() {
         this.targetSetting.setValue(this.targetSetting.isMode("Host") ? "Viewer" : "Host");
         addSetupAction("Paper: " + this.targetSetting.getValue());
      }

      @Override
      protected void applySettings() {
         if (this.isEnabled()) {
            VisualWinnerConfig var1 = VisualWinnerConfig.getInstance();
            if (var1.setLinked(this.universalItemsSetting.getValue())) {
               this.getRig().saveConfig();
            }

            var1.setHostName(this.hostSuffixSetting.getValue());
            var1.setTargetByName(this.viewerSuffixSetting.getValue());
            var1.setTargetByName(this.targetSetting.getValue().toLowerCase(Locale.ROOT));
         }
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         var1.add(new String[]{"Items", this.universalItemsSetting.getValue() ? "Same for both tables" : "Per table"});
      }
   }

   public static final class RussianRouletteModule extends CasinoGameModule {
      @Override
      public String setupSummary() {
         DispenserRemap var1 = DispenserRemap.getInstance();
         return super.setupSummary()
            + "\nNormal: "
            + addTableBind(var1.paperItem)
            + ", trigger: "
            + addTableBind(var1.tntItem)
            + (var1.refill ? " (armed)" : "");
      }

      private static void toggleArmed() {
         DispenserRemap var0 = DispenserRemap.getInstance();
         if (var0.refill) {
            var0.refill = (boolean)0;
         } else {
            var0.arm();
         }

         addSetupAction("Russian Roulette: " + (var0.refill ? "armed, the next pull shows the trigger item" : "disarmed"));
      }

      @Override
      protected String targetName() {
         return "Next pull";
      }

      @Override
      protected void addHudRows(List<String[]> var1) {
         DispenserRemap var2 = DispenserRemap.getInstance();
         var1.add(new String[]{"Normal item", addTableBind(var2.paperItem)});
         var1.add(new String[]{"Trigger item", addTableBind(var2.tntItem)});
      }

      public RussianRouletteModule() {
         super("Russian Roulette", "Swaps the normal item for the trigger item when armed, visually only", CasinoRigManager.Game.RUSSIAN_ROULETTE);
         this.addBind("Arm Bind", "Arm or disarm the trigger item", CasinoModules.RussianRouletteModule::toggleArmed);
         this.addBind(
            "From Hand Bind",
            "Store your hand item as the trigger item when armed, else the normal item",
            () -> addSetupAction(this.getRig().setItemFromHand(CasinoRigManager.Game.RUSSIAN_ROULETTE).message)
         );
         this.addTableBind("Dispenser Bind", DispenserRegistry.GameType.RUSSIAN_ROULETTE);
         this.addTableSelector("Select table", DispenserRegistry.GameType.RUSSIAN_ROULETTE);
         this.addRemoveBind();
         this.addLogBind();
         this.addStopBind();
         this.stopAll("Select normal item", "normal item", var0 -> true, "Pick an item", var1 -> {
            DispenserRemap.getInstance().paperItem = var1.getItem();
            this.getRig().saveConfig();
            return "Russian Roulette normal item: " + var1.getHoverName().getString();
         }, () -> addTableBind(DispenserRemap.getInstance().paperItem));
         this.stopAll("Select trigger item", "trigger item", var0 -> true, "Pick an item", var1 -> {
            DispenserRemap.getInstance().tntItem = var1.getItem();
            this.getRig().saveConfig();
            return "Russian Roulette trigger item: " + var1.getHoverName().getString();
         }, () -> addTableBind(DispenserRemap.getInstance().tntItem));
         this.getSetupActions("Arm or disarm", () -> {
            if (this.isEnabled()) {
               toggleArmed();
            } else {
               addSetupAction("Enable Russian Roulette first");
            }
         }, () -> DispenserRemap.getInstance().refill ? "Armed: press to disarm" : "Arm trigger");
         this.addCommonActions();
      }

      @Override
      protected void applySettings() {
      }
   }
}
