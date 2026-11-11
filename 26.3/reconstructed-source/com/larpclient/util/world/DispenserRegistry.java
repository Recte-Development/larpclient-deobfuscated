package com.larpclient.util.world;

import com.larpclient.config.VisualWinnerConfig;
import com.larpclient.module.exploit.BlackjackRigger;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.module.exploit.DispenserMinigameSpoofer;
import com.larpclient.module.exploit.HigherLowerSwapper;
import com.larpclient.module.exploit.HotbarForceQueue;
import com.larpclient.module.exploit.HotbarSwapQueue2;
import com.larpclient.module.exploit.OddEvenCardForce;
import com.larpclient.module.misc.ArrowGame;
import com.larpclient.module.misc.BlackjackAssistant;
import com.larpclient.module.misc.CasinoRigManager;
import com.larpclient.util.data.BlackjackTracker;
import com.larpclient.util.data.HigherLowerMapTracker;
import com.larpclient.util.inventory.DispenserRemap;
import com.larpclient.util.inventory.DispenserSpoofer;
import com.larpclient.util.misc.BaccaratRig;
import com.larpclient.util.misc.FiftyFiftyDispenser;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.RigLog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DispenserMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class DispenserRegistry {
   private static final DispenserRegistry instance = new DispenserRegistry();
   private final Map<String, DispenserRegistry.GameType> assignments = new LinkedHashMap<>();
   private final Map<String, Boolean> triggeredStates = new HashMap<>();
   private String activeKey;
   private int activeContainerId = -1;
   private int refreshedContainerId = -1;
   private int importRetries;
   private int lastContainerId = -1;
   private String lastLookedKey;
   private boolean lookingAtNoteBlock;
   private boolean lookingAtUnassigned;
   private String lastDimension;

   private static Path getStorePath() {
      return CasinoPaths.resolve("dispensers.properties");
   }

   private DispenserRegistry() {
      this.loadAssignments();
   }

   public static DispenserRegistry getInstance() {
      return instance;
   }

   public DispenserRegistry.GameType loadAssignments() {
      return this.activeKey == null ? null : this.assignments.get(this.activeKey);
   }

   public DispenserRegistry.GameType getEffectiveGameType() {
      return this.lookingAtNoteBlock ? DispenserRegistry.GameType.ARROW_GAME : this.loadAssignments();
   }

   public boolean isLookingAtUnassigned() {
      return this.lookingAtUnassigned;
   }

   public String getActiveKey() {
      return this.activeKey;
   }

   public int getActiveKey() {
      return this.activeContainerId;
   }

   public boolean setLookedAtGameType(Minecraft var1, DispenserRegistry.GameType var2) {
      if (var1 != null && var1.player != null && var1.level != null && var1.hitResult instanceof BlockHitResult var3) {
         BlockPos var7 = var3.getBlockPos().immutable();
         BlockState var5 = var1.level.getBlockState(var7);
         if (!(var5.getBlock() instanceof DispenserBlock)) {
            return false;
         }

         String var6 = this.getActiveKey(var1, var7);
         this.lastLookedKey = var6;
         return this.assign(var6, var2).success();
      } else {
         return false;
      }
   }

   public DispenserRegistry.AssignResult unassignLookedAt(Minecraft var1) {
      return var1 != null
            && var1.player != null
            && var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock
         ? this.unassign(this.getActiveKey(var1, var2.getBlockPos()))
         : new DispenserRegistry.AssignResult(false, "Look at a dispenser first.");
   }

   public DispenserRegistry.GameType onDispenserUpdate(Minecraft var1) {
      return var1 != null
            && var1.level != null
            && var1.hitResult instanceof BlockHitResult var2
            && var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock
         ? this.assignments.get(this.getActiveKey(var1, var2.getBlockPos()))
         : null;
   }

   public DispenserRegistry.AssignResult assignLookedAt(Minecraft var1, DispenserRegistry.GameType var2) {
      if (var1 != null
         && var1.player != null
         && var1.level != null
         && var1.hitResult instanceof BlockHitResult var3
         && var1.level.getBlockState(var3.getBlockPos()).getBlock() instanceof DispenserBlock) {
         String var5 = this.getActiveKey(var1, var3.getBlockPos());
         this.lastLookedKey = var5;
         this.lookingAtUnassigned = (boolean)0;
         return this.assign(var5, var2);
      } else {
         return new DispenserRegistry.AssignResult(false, "Look at a dispenser first.");
      }
   }

   public void saveAssignments() {
      this.triggeredStates.clear();
      this.activeKey = null;
      this.activeContainerId = -1;
      this.lastContainerId = -1;
      this.importRetries = 0;
      this.lookingAtUnassigned = (boolean)0;
      this.lookingAtNoteBlock = (boolean)0;
      this.lastDimension = null;
   }

   public Map<String, DispenserRegistry.GameType> getInstance() {
      return Collections.unmodifiableMap(this.assignments);
   }

   public List<String> setLookedAtGameType(DispenserRegistry.GameType var1) {
      ArrayList var2 = new ArrayList();
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null && var3.level != null && var1 != null) {
         String var4 = var3.level.dimension().identifier() + ";";

         for (Entry var6 : this.assignments.entrySet()) {
            if (var6.getValue() == var1 && ((String)var6.getKey()).startsWith(var4)) {
               var2.add((String)var6.getKey());
            }
         }

         return var2;
      } else {
         return var2;
      }
   }

   public DispenserRegistry.AssignResult assign(String var1, DispenserRegistry.GameType var2) {
      if (var1 != null && var2 != null) {
         DispenserRegistry.GameType var3 = this.assignments.get(var1);
         if (var3 == var2) {
            boolean var5 = this.saveAssignments();
            return new DispenserRegistry.AssignResult(
               var5, var5 ? "This dispenser is set and saved as " + var2.DISPLAYNAME + "." : "This dispenser is set, but saving failed."
            );
         }

         this.assignments.put(var1, var2);
         this.lastLookedKey = var1;
         this.triggeredStates.remove(var1);
         if (!this.saveAssignments()) {
            return new DispenserRegistry.AssignResult(false, "Could not save dispenser assignment. Check the Larp log.");
         }

         String var4 = var3 == null
            ? "Dispenser set to " + var2.DISPLAYNAME + "."
            : "Dispenser moved from " + var3.DISPLAYNAME + " to " + var2.DISPLAYNAME + ".";
         if (getActiveKey(var2)) {
            var4 = var4 + " Open it manually to refresh all item templates.";
         }

         return new DispenserRegistry.AssignResult(true, var4);
      } else {
         return new DispenserRegistry.AssignResult(false, "Invalid dispenser assignment.");
      }
   }

   public DispenserRegistry.AssignResult unassign(String var1) {
      DispenserRegistry.GameType var2 = this.assignments.remove(var1);
      this.triggeredStates.remove(var1);
      boolean var3 = this.saveAssignments();
      return new DispenserRegistry.AssignResult(
         var3,
         !var3 ? "Could not save dispenser assignment." : (var2 == null ? "This dispenser was not assigned." : "Removed " + var2.DISPLAYNAME + " dispenser.")
      );
   }

   public void onContainerOpened(Minecraft var1, DispenserMenu var2) {
      if (var1 != null && var1.player != null && var1.level != null && var2 != null) {
         if (!this.lookingAtUnassigned && !CardForce.getActive().isSwapActive()) {
            String var3 = this.findActiveKey(var1);
            DispenserRegistry.GameType var4 = var3 == null ? null : this.assignments.get(var3);
            if (var4 != null) {
               if (var2.containerId != this.lastContainerId || !var3.equals(this.activeKey)) {
                  this.lookingAtNoteBlock = (boolean)0;
                  this.activeKey = var3;
                  this.activeContainerId = var2.containerId;
                  this.refreshedContainerId = -1;
                  this.lastContainerId = var2.containerId;
                  this.importRetries = !getActiveKey(var4)
                        && var4 != DispenserRegistry.GameType.FORTY_FIVE_TEN
                        && var4 != DispenserRegistry.GameType.RUSSIAN_ROULETTE
                        && DispenserMinigameSpoofer.forMode(var4) == null
                     ? 20
                     : 0;
                  switch (var4) {
                     case BLACKJACK:
                     case BLACKJACK_FORCE_ONLY:
                        CardForce.getInstance().setContainerContext(var3);
                        break;
                     case ODD_EVEN:
                        OddEvenCardForce.getInstance().setContainerContext(var3);
                        break;
                     case HIGHER_LOWER:
                        HigherLowerSwapper.getInstance().setContainerContext(var3);
                        break;
                     case BACCARAT_PLAYER:
                     case BACCARAT_BANKER:
                        HotbarForceQueue.getInstance().setContainerContext(var3);
                        break;
                     case MEXICAN_JACK:
                        HotbarSwapQueue2.getInstance().setContainerContext(var3);
                     case RUSSIAN_ROULETTE:
                     case HORSE_RACING:
                     case ARMOR_GAME:
                     default:
                        break;
                     case ARROW_GAME:
                        ArrowGame.getInstance().setTarget(var1, var2);
                  }

                  this.syncGameMode(var1, var4);
                  if (!getActiveKey(var4)
                     && var4 != DispenserRegistry.GameType.RUSSIAN_ROULETTE
                     && DispenserMinigameSpoofer.forMode(var4) == null
                     && this.importTemplates(var1, var2, var4)) {
                     this.importRetries = 0;
                  }
               }
            }
         }
      }
   }

   public void tick(Minecraft var1) {
      if (var1 != null && var1.player != null && var1.level != null) {
         this.getStorePath(var1);
         this.updateLookTarget(var1);
         if (!this.lookingAtUnassigned && !this.assignments.isEmpty()) {
            if (var1.player.containerMenu instanceof DispenserMenu var2) {
               if (var2.containerId != this.lastContainerId) {
                  this.onContainerOpened(var1, var2);
               }
            } else {
               this.lastContainerId = -1;
            }

            if (this.importRetries > 0 && var1.player.containerMenu instanceof DispenserMenu var7 && var7.containerId == this.activeContainerId) {
               DispenserRegistry.GameType var10 = this.assignments.get(this.activeKey);
               if (var10 != null && !getActiveKey(var10) && this.importTemplates(var1, var7, var10)) {
                  this.importRetries = 0;
                  if (var10 == DispenserRegistry.GameType.BLACKJACK) {
                     RigLog.logMessage(
                        "Blackjack",
                        "Templates imported manually from " + this.activeKey + " - registry now holds " + CardForce.getInstance().countTemplates() + " cards"
                     );
                  }
               } else {
                  this.importRetries--;
               }
            }

            String var8 = var1.level.dimension().identifier() + ";";

            for (Entry var4 : new LinkedHashMap<>(this.assignments).entrySet()) {
               String var5 = (String)var4.getKey();
               if (var5.startsWith(var8)) {
                  BlockPos var6 = this.parsePosition(var5);
                  if (var6 != null
                     && (
                        var4.getValue() == DispenserRegistry.GameType.HIGHER_LOWER
                           || var4.getValue() == DispenserRegistry.GameType.BLACKJACK
                           || var4.getValue() == DispenserRegistry.GameType.BLACKJACK_FORCE_ONLY
                           || var4.getValue() == DispenserRegistry.GameType.BACCARAT_PLAYER
                           || var4.getValue() == DispenserRegistry.GameType.BACCARAT_BANKER
                           || var4.getValue() == DispenserRegistry.GameType.MEXICAN_JACK
                           || var4.getValue() == DispenserRegistry.GameType.ODD_EVEN
                           || getActiveKey((DispenserRegistry.GameType)var4.getValue())
                     )) {
                     this.onDispenserUpdate(var1, var6, var1.level.getBlockState(var6));
                  }
               }
            }
         }
      } else {
         this.lookingAtUnassigned = (boolean)0;
         this.lookingAtNoteBlock = (boolean)0;
      }
   }

   private void getStorePath(Minecraft var1) {
      String var2 = var1.level.dimension().identifier().toString();
      if (!var2.equals(this.lastDimension)) {
         this.triggeredStates.clear();
         this.loadAssignments();
         this.lastDimension = var2;
      }
   }

   public void onDispenserUpdate(Minecraft var1, BlockPos var2, BlockState var3) {
      if (var1 != null && var1.isSameThread() && var1.player != null && var1.level != null) {
         this.getStorePath(var1);
         String var4 = this.getActiveKey(var1, var2);
         DispenserRegistry.GameType var5 = this.assignments.get(var4);
         if (var5 == DispenserRegistry.GameType.BLACKJACK
            || var5 == DispenserRegistry.GameType.BLACKJACK_FORCE_ONLY
            || var5 == DispenserRegistry.GameType.HIGHER_LOWER
            || var5 == DispenserRegistry.GameType.BACCARAT_PLAYER
            || var5 == DispenserRegistry.GameType.BACCARAT_BANKER
            || var5 == DispenserRegistry.GameType.MEXICAN_JACK
            || var5 == DispenserRegistry.GameType.ODD_EVEN
            || getActiveKey(var5)) {
            int var6 = var3.getBlock() instanceof DispenserBlock && this.isTriggered(var3) ? 1 : 0;
            if (this.markTriggered(var4, (boolean)var6)) {
               CasinoRigManager var7 = CasinoRigManager.getInstance();
               if (!this.lookingAtUnassigned && var7 != null) {
                  if (getActiveKey(var5)) {
                     if (var7.isRigActive() && VisualWinnerConfig.getInstance().enabled) {
                        int var12 = var5 == DispenserRegistry.GameType.PAPER_HOST ? 1 : 0;
                        RigLog.logMessage("Paper", (var12 != 0 ? "Host" : "Viewer") + " dispenser trigger detected at " + var4);
                        VisualWinnerConfig.getInstance().onContainerOpen(var1, (boolean)var12, var2);
                     }
                  } else if (var5 == DispenserRegistry.GameType.BLACKJACK) {
                     CardForce var11 = CardForce.getInstance();
                     if (!var11.isSwapActive()) {
                        var11.setContainerContext(var4);
                     }

                     BlackjackAssistant.getInstance().beginRound(var1, false);
                  } else if (var5 == DispenserRegistry.GameType.BLACKJACK_FORCE_ONLY) {
                     BlackjackRigger.getInstance().isDispenserScreenOpen(var2, var3);
                     BlackjackAssistant.getInstance().beginRound(var1, true);
                  } else if (var5 == DispenserRegistry.GameType.BACCARAT_PLAYER || var5 == DispenserRegistry.GameType.BACCARAT_BANKER) {
                     HotbarForceQueue var10 = HotbarForceQueue.getInstance();
                     if (!var10.isSwapActive()) {
                        var10.setContainerContext(var4);
                     }

                     BaccaratRig.getInstance()
                        .onDispense(var1, var10, var5 == DispenserRegistry.GameType.BACCARAT_PLAYER ? BaccaratRig.Side.PLAYER : BaccaratRig.Side.BANKER);
                  } else if (var5 == DispenserRegistry.GameType.MEXICAN_JACK) {
                     HotbarSwapQueue2 var9 = HotbarSwapQueue2.getInstance();
                     if (!var9.isSwapActive()) {
                        var9.setContainerContext(var4);
                     }

                     BlackjackTracker.getInstance().registerDraw(var1, var9);
                  } else if (var5 == DispenserRegistry.GameType.ODD_EVEN) {
                     OddEvenCardForce.getInstance().startBusyCooldown();
                  } else if (var7.isRigActive() && HigherLowerMapTracker.getInstance().paused) {
                     HigherLowerSwapper var8 = HigherLowerSwapper.getInstance();
                     if (!var8.isSwapBusy()) {
                        var8.setContainerContext(var4);
                        var8.activate(var1);
                     }
                  }
               }
            }
         }
      }
   }

   public boolean saveAssignments(DispenserRegistry.GameType var1) {
      return !this.lookingAtUnassigned && !this.setLookedAtGameType(var1).isEmpty();
   }

   private boolean markTriggered(String var1, boolean var2) {
      Boolean var3 = this.triggeredStates.put(var1, var2);
      return var2 && !Boolean.TRUE.equals(var3);
   }

   public String findNearestKey(DispenserRegistry.GameType var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null && var2.level != null && var1 != null) {
         String var3 = var2.level.dimension().identifier() + ";";
         String var4 = null;
         if (var2.player != null
            && var2.player.containerMenu instanceof DispenserMenu var5
            && var5.containerId == this.activeContainerId
            && this.assignments.get(this.activeKey) == var1) {
            var4 = this.activeKey;
         } else if (var2.hitResult instanceof BlockHitResult var6 && var6.getType() == Type.BLOCK) {
            var4 = this.getActiveKey(var2, var6.getBlockPos());
         }

         return this.selectNearestKey(var1, var3, var2.player == null ? null : new Vec3(var2.player.getX(), var2.player.getY(), var2.player.getZ()), var4);
      } else {
         return null;
      }
   }

   String selectNearestKey(DispenserRegistry.GameType var1, String var2, Vec3 var3, String var4) {
      String var5 = null;
      double var6 = Double.MAX_VALUE;

      for (Entry var9 : this.assignments.entrySet()) {
         if (var9.getValue() == var1 && ((String)var9.getKey()).startsWith(var2)) {
            BlockPos var10 = this.parsePosition((String)var9.getKey());
            if (var10 != null) {
               double var11 = var3 == null ? 0.0 : var3.distanceToSqr(Vec3.atCenterOf(var10));
               if (((String)var9.getKey()).equals(var4) && var11 <= 64.0) {
                  return var4;
               }

               if (var5 == null || var11 < var6) {
                  var6 = var11;
                  var5 = (String)var9.getKey();
               }
            }
         }
      }

      return var5;
   }

   public BlockPos getPosition(String var1) {
      return var1 == null ? null : this.parsePosition(var1);
   }

   public boolean isWithinRange(DispenserRegistry.GameType var1, double var2) {
      Minecraft var4 = Minecraft.getInstance();
      if (var4 != null && var4.player != null && var4.level != null && var1 != null) {
         BlockPos var5 = this.onDispenserUpdate(var1);
         if (var5 == null) {
            return false;
         }

         double var6 = var4.player.distanceToSqr(var5.getX() + 0.5, var5.getY() + 0.5, var5.getZ() + 0.5);
         return var6 <= var2 * var2;
      } else {
         return false;
      }
   }

   public void selectNearestKey(int var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2 != null
         && var2.player != null
         && var1 == this.activeContainerId
         && var2.player.containerMenu instanceof DispenserMenu var3
         && var3.containerId == var1) {
         DispenserRegistry.GameType var6 = this.assignments.get(this.activeKey);
         if (var6 == DispenserRegistry.GameType.FIFTY_FIFTY) {
            FiftyFiftyDispenser.getInstance().isActive(var2, var3, this.getPosition(this.activeKey));
         }

         DispenserMinigameSpoofer var5 = DispenserMinigameSpoofer.forMode(var6);
         if (var5 != null) {
            var5.onDispenserOpened(var2, var3, this.getPosition(this.activeKey));
         } else if (var6 == DispenserRegistry.GameType.FORTY_FIVE_TEN || var6 == DispenserRegistry.GameType.RUSSIAN_ROULETTE) {
            this.importTemplates(var2, var3, var6);
         } else if (var6 == DispenserRegistry.GameType.ARROW_GAME) {
            ArrowGame.getInstance().setTarget(var2, var3);
         } else if (getActiveKey(var6) && this.refreshedContainerId != var1) {
            this.refreshedContainerId = var1;
            VisualWinnerConfig.getInstance().setTarget(var2, var3);
            if (this.importTemplates(var2, var3, var6)) {
               RigLog.logMessage(
                  "Paper",
                  (var6 == DispenserRegistry.GameType.PAPER_HOST ? "Host" : "Viewer") + " templates refreshed from manually opened dispenser " + this.activeKey
               );
            }
         }
      }
   }

   public void onTemplateSlotClick(int var1, int var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (var3 != null
         && var3.player != null
         && var1 == this.activeContainerId
         && var2 >= 0
         && var2 < 9
         && var3.player.containerMenu instanceof DispenserMenu var4
         && var4.containerId == var1
         && getActiveKey(this.loadAssignments())) {
         ItemStack var7 = var4.getSlot(var2).getItem();
         if (!var7.isEmpty() && VisualWinnerConfig.getInstance().setStackFromName(this.loadAssignments() == DispenserRegistry.GameType.PAPER_HOST, var2, var7)) {
            CasinoRigManager var6 = CasinoRigManager.getInstance();
            if (var6 != null) {
               var6.saveConfig();
            }
         }
      }
   }

   private static boolean getActiveKey(DispenserRegistry.GameType var0) {
      return var0 == DispenserRegistry.GameType.PAPER_HOST || var0 == DispenserRegistry.GameType.PAPER_VIEWER;
   }

   private void syncGameMode(Minecraft var1, DispenserRegistry.GameType var2) {
      CasinoRigManager var3 = CasinoRigManager.getInstance();
      if (var3 != null && var2 != null) {
         if (var3.isRigActive()) {
            switch (var2) {
               case PAPER_HOST:
               case PAPER_VIEWER:
                  var3.activateGame(CasinoRigManager.Game.VISUAL_WINNER);
                  break;
               case FORTY_FIVE_TEN:
                  var3.activateGame(CasinoRigManager.Game.SPLIT_45_45_10);
                  break;
               case FIFTY_FIFTY:
                  var3.activateGame(CasinoRigManager.Game.FIFTY_FIFTY);
                  break;
               case BLACKJACK:
                  var3.activateGame(CasinoRigManager.Game.BLACKJACK_ASSIST);
                  break;
               case BLACKJACK_FORCE_ONLY:
                  var3.activateGame(CasinoRigManager.Game.BLACKJACK_RIG);
                  break;
               case ODD_EVEN:
                  var3.activateGame(CasinoRigManager.Game.ODD_EVEN);
                  break;
               case HIGHER_LOWER:
                  var3.activateGame(CasinoRigManager.Game.HIGHER_LOWER);
                  break;
               case BACCARAT_PLAYER:
               case BACCARAT_BANKER:
                  var3.activateGame(CasinoRigManager.Game.BACCARAT);
                  break;
               case MEXICAN_JACK:
                  var3.activateGame(CasinoRigManager.Game.BLACKJACK_DRAWS);
                  break;
               case RUSSIAN_ROULETTE:
                  var3.activateGame(CasinoRigManager.Game.RUSSIAN_ROULETTE);
                  break;
               case HORSE_RACING:
                  var3.activateGame(CasinoRigManager.Game.HORSE_RACING);
                  break;
               case ARMOR_GAME:
                  var3.activateGame(CasinoRigManager.Game.ARMOR_GAME);
                  break;
               case ARROW_GAME:
                  var3.activateGame(CasinoRigManager.Game.ARROW_GAME);
            }
         }
      }
   }

   private String findActiveKey(Minecraft var1) {
      if (var1.hitResult instanceof BlockHitResult var2) {
         String var13 = this.getActiveKey(var1, var2.getBlockPos());
         if (var1.level.getBlockState(var2.getBlockPos()).getBlock() instanceof DispenserBlock) {
            return this.assignments.containsKey(var13) ? var13 : null;
         }
      }

      if (this.lastLookedKey != null && this.assignments.containsKey(this.lastLookedKey)) {
         BlockPos var11 = this.parsePosition(this.lastLookedKey);
         String var14 = var1.level.dimension().identifier() + ";";
         if (var11 != null
            && this.lastLookedKey.startsWith(var14)
            && var1.player.distanceToSqr(var11.getX() + 0.5, var11.getY() + 0.5, var11.getZ() + 0.5) <= 64.0) {
            return this.lastLookedKey;
         }
      }

      String var12 = var1.level.dimension().identifier() + ";";
      String var15 = null;
      double var4 = 64.0;

      for (String var7 : this.assignments.keySet()) {
         if (var7.startsWith(var12)) {
            BlockPos var8 = this.parsePosition(var7);
            if (var8 != null) {
               double var9 = var1.player.distanceToSqr(var8.getX() + 0.5, var8.getY() + 0.5, var8.getZ() + 0.5);
               if (var9 < var4) {
                  var4 = var9;
                  var15 = var7;
               }
            }
         }
      }

      return var15;
   }

   private void updateLookTarget(Minecraft var1) {
      if (var1.gui.screen() == null) {
         if (var1.hitResult instanceof BlockHitResult var2 && var2.getType() == Type.BLOCK) {
            Block var7 = var1.level.getBlockState(var2.getBlockPos()).getBlock();
            if (var7 instanceof NoteBlock) {
               this.lookingAtNoteBlock = (boolean)1;
               this.lookingAtUnassigned = (boolean)0;
            } else if (!(var7 instanceof DispenserBlock)) {
               this.lookingAtUnassigned = (boolean)0;
            } else {
               this.lookingAtNoteBlock = (boolean)0;
               String var4 = this.getActiveKey(var1, var2.getBlockPos());
               BlackjackRigger var5 = BlackjackRigger.getInstance();
               int var6 = var5.isEnabled() && var5.isTransferActive() && this.loadAssignments() == DispenserRegistry.GameType.BLACKJACK_FORCE_ONLY ? 1 : 0;
               this.lookingAtUnassigned = (boolean)(!this.assignments.containsKey(var4) && var6 == 0 ? 1 : 0);
               if (this.assignments.containsKey(var4)) {
                  this.lastLookedKey = var4;
                  if (!var4.equals(this.activeKey)) {
                     this.activeKey = var4;
                     this.activeContainerId = -1;
                     this.importRetries = 0;
                  }
               }
            }
         } else {
            this.lookingAtUnassigned = (boolean)0;
         }
      }
   }

   private boolean importTemplates(Minecraft var1, DispenserMenu var2, DispenserRegistry.GameType var3) {
      int var4 = switch (var3) {
         case PAPER_HOST -> this.getPosition(var2, true);
         case PAPER_VIEWER -> this.getPosition(var2, false);
         case FORTY_FIVE_TEN -> this.importFortyFiveTen(var2);
         case FIFTY_FIFTY, HORSE_RACING, ARMOR_GAME, ARROW_GAME -> 0;
         case BLACKJACK -> this.importMapTemplates(var2, CardForce.getInstance(), new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10});
         case BLACKJACK_FORCE_ONLY -> BlackjackRigger.getInstance().hasMapMapping()
            ? 0
            : this.importMapTemplates(var2, CardForce.getInstance(), new int[]{1, 3, 4, 5, 6, 7, 8, 9, 10});
         case ODD_EVEN -> this.importOddEvenTemplates(var2);
         case HIGHER_LOWER -> this.importHigherLowerTemplates(var2, HigherLowerSwapper.getInstance(), 9);
         case BACCARAT_PLAYER, BACCARAT_BANKER -> this.importHigherLowerTemplates(var2, HotbarForceQueue.getInstance(), 9);
         case MEXICAN_JACK -> this.importMapTemplates(var2, HotbarSwapQueue2.getInstance(), new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9});
         case RUSSIAN_ROULETTE -> this.onContainerOpened(var2);
      };
      if (var4 <= 0) {
         return false;
      }

      CasinoRigManager var5 = CasinoRigManager.getInstance();
      if (var5 != null) {
         var5.saveConfig();
      }

      if (var3 == DispenserRegistry.GameType.RUSSIAN_ROULETTE) {
         DispenserRemap var6 = DispenserRemap.getInstance();
         RigLog.logMessage(
            "Russian Roulette",
            "Loaded dispenser: normal="
               + new ItemStack(var6.paperItem).getHoverName().getString()
               + ", trigger="
               + new ItemStack(var6.tntItem).getHoverName().getString()
         );
      }

      return true;
   }

   private int getPosition(DispenserMenu var1, boolean var2) {
      ItemStack[] var3 = new ItemStack[9];
      Arrays.fill(var3, ItemStack.EMPTY);
      int var4 = 0;
      int var5 = 0;

      for (Slot var7 : var1.slots) {
         if (getActiveKey(var7) && var7.getContainerSlot() >= 0 && var7.getContainerSlot() <= 8) {
            ItemStack var8 = var7.hasItem() ? var7.getItem().copyWithCount(1) : ItemStack.EMPTY;
            var3[var7.getContainerSlot()] = var8;
            var4++;
            if (!var8.isEmpty()) {
               var5++;
            }
         }
      }

      if (var4 >= 9 && var5 > 0) {
         VisualWinnerConfig var9 = VisualWinnerConfig.getInstance();
         byte var10 = 0;

         for (int var11 = 0; var11 < var3.length; var11++) {
            if (!var3[var11].isEmpty() && var9.setStackFromName(var2, var11, var3[var11])) {
               var10 = 1;
            }
         }

         if (var10 != 0) {
            LarpLog.info(
               "Rig",
               "Paper: learned {} items from the {} table ({})",
               var5,
               var2 ? "host" : "viewer",
               var9.isLinked() ? "shared by both tables" : "this table only"
            );
         }

         return var10 != 0 ? var5 : 0;
      } else {
         return 0;
      }
   }

   private int importFortyFiveTen(DispenserMenu var1) {
      return DispenserSpoofer.getInstance().captureLayout(Minecraft.getInstance(), this.getPosition(this.activeKey), var1) ? 9 : 0;
   }

   private int importHigherLowerTemplates(DispenserMenu var1, CardForce var2, int var3) {
      int var4 = 0;

      for (Slot var6 : var1.slots) {
         if (getActiveKey(var6) && var6.hasItem() && var6.getItem().is(Items.FILLED_MAP)) {
            ItemStack var7 = var6.getItem();
            MapId var8 = (MapId)var7.getComponents().get(DataComponents.MAP_ID);
            int var9 = assignLookedAt(var7.getHoverName().getString(), var3);
            if (var9 < 1 && var6.getContainerSlot() >= 0 && var6.getContainerSlot() < var3) {
               var9 = var6.getContainerSlot() + 1;
            }

            if (var8 != null && var9 >= 1 && var9 <= var3) {
               var2.setTemplateForValue(var9, var8);
               if (var2 instanceof HigherLowerSwapper) {
                  String var10 = var7.getHoverName().getString().replaceAll("[\\p{Cntrl}]", " ");
                  RigLog.logDebug(
                     "HigherLower Template import slot="
                        + var6.getContainerSlot()
                        + " mapId="
                        + var8.id()
                        + " displayedName="
                        + var10.substring(0, Math.min(var10.length(), 120))
                        + " chosenValue="
                        + var9
                  );
               }

               var4++;
            }
         }
      }

      var2.scanCurrentContainerTemplateSlots(Minecraft.getInstance());
      return var4;
   }

   private int importOddEvenTemplates(DispenserMenu var1) {
      OddEvenCardForce var2 = OddEvenCardForce.getInstance();
      int var3 = 0;

      for (int var4 = 0; var4 < 9; var4++) {
         if (var4 != 4) {
            Slot var5 = null;

            for (Slot var7 : var1.slots) {
               if (getActiveKey(var7) && var7.getContainerSlot() == var4) {
                  var5 = var7;
                  break;
               }
            }

            if (var5 != null && var5.hasItem() && var5.getItem().is(Items.FILLED_MAP)) {
               MapId var8 = (MapId)var5.getItem().getComponents().get(DataComponents.MAP_ID);
               int var9 = var4 + 1;
               if (var8 != null) {
                  var2.setTemplateForValue(var9, var8);
                  var3++;
               }
            }
         }
      }

      var2.scanCurrentContainerTemplateSlots(Minecraft.getInstance());
      var2.clearQueue();
      var2.forceSelectedParity();
      return var3;
   }

   private int importMapTemplates(DispenserMenu var1, CardForce var2, int[] var3) {
      int var4 = 0;

      for (Slot var6 : var1.slots) {
         if (getActiveKey(var6) && var6.hasItem() && var6.getItem().is(Items.FILLED_MAP)) {
            int var7 = var6.getContainerSlot();
            if (var7 >= 0 && var7 < var3.length) {
               MapId var8 = (MapId)var6.getItem().getComponents().get(DataComponents.MAP_ID);
               if (var8 != null) {
                  var2.setTemplateForValue(var3[var7], var8);
                  var4++;
               }
            }
         }
      }

      var2.scanCurrentContainerTemplateSlots(Minecraft.getInstance());
      return var4;
   }

   private int onContainerOpened(DispenserMenu var1) {
      return DispenserRemap.getInstance().configure(Minecraft.getInstance(), this.getPosition(this.activeKey), var1) ? 9 : 0;
   }

   private static boolean getActiveKey(Slot var0) {
      return var0 != null && !(var0.container instanceof Inventory);
   }

   private static int assignLookedAt(String var0, int var1) {
      if (var0 == null) {
         return -1;
      }

      String var2 = var0.toLowerCase(Locale.ROOT);
      if (var2.matches(".*\\b(ace|a)\\b.*")) {
         return 1;
      }

      if (var2.matches(".*\\b(jack|queen|king|j|q|k)\\b.*")) {
         return Math.min(10, var1);
      }

      Matcher var3 = Pattern.compile("(?<!\\d)(10|[1-9])(?!\\d)").matcher(var2);
      if (!var3.find()) {
         return -1;
      }

      try {
         int var4 = Integer.parseInt(var3.group(1));
         return var4 <= var1 ? var4 : -1;
      } catch (NumberFormatException var5) {
         return -1;
      }
   }

   private boolean isTriggered(BlockState var1) {
      return var1 != null && var1.hasProperty(BlockStateProperties.TRIGGERED) && (Boolean)var1.getValue(BlockStateProperties.TRIGGERED);
   }

   public String getActiveKey(Minecraft var1, BlockPos var2) {
      return var1.level.dimension().identifier() + ";" + var2.getX() + ";" + var2.getY() + ";" + var2.getZ();
   }

   private BlockPos parsePosition(String var1) {
      try {
         String[] var2 = var1.split(";");
         return var2.length == 4 ? new BlockPos(Integer.parseInt(var2[1]), Integer.parseInt(var2[2]), Integer.parseInt(var2[3])) : null;
      } catch (Exception var3) {
         return null;
      }
   }

   public BlockPos onDispenserUpdate(DispenserRegistry.GameType var1) {
      return this.getPosition(this.findNearestKey(var1));
   }

   private void loadAssignments() {
      if (getStorePath() != null && Files.exists(getStorePath())) {
         Properties var1 = new Properties();

         try (BufferedReader var2 = Files.newBufferedReader(getStorePath())) {
            var1.load(var2);
            LinkedHashMap var3 = new LinkedHashMap();

            for (String var5 : var1.stringPropertyNames()) {
               DispenserRegistry.GameType var6 = DispenserRegistry.GameType.fromKey(var1.getProperty(var5));
               if (var6 != null) {
                  var3.put(var5, var6);
               }
            }

            this.assignments.clear();
            this.assignments.putAll(var3);
         } catch (Exception var9) {
            LarpLog.warn("Rig", "Could not load dispenser assignments from {}: {}", getStorePath(), var9.getMessage());
         }
      }
   }

   private boolean saveAssignments() {
      if (getStorePath() == null) {
         return false;
      }

      Properties var1 = new Properties();

      for (Entry var3 : this.assignments.entrySet()) {
         var1.setProperty((String)var3.getKey(), ((DispenserRegistry.GameType)var3.getValue()).KEY);
      }

      try {
         Files.createDirectories(getStorePath().getParent());
         Path var18 = getStorePath().resolveSibling(getStorePath().getFileName() + ".tmp");

         try (
            FileChannel var19 = FileChannel.open(var18, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
            OutputStream var4 = Channels.newOutputStream(var19);
            OutputStreamWriter var5 = new OutputStreamWriter(var4, StandardCharsets.ISO_8859_1);
         ) {
            var1.store(var5, "Assigned casino game dispensers");
            var5.flush();
            var19.force(true);
         }

         try {
            Files.move(var18, getStorePath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
         } catch (Exception var12) {
            Files.move(var18, getStorePath(), StandardCopyOption.REPLACE_EXISTING);
         }

         Properties var20 = new Properties();

         try (BufferedReader var21 = Files.newBufferedReader(getStorePath())) {
            var20.load(var21);
         }

         for (Entry var23 : this.assignments.entrySet()) {
            if (!((DispenserRegistry.GameType)var23.getValue()).KEY.equalsIgnoreCase(var20.getProperty((String)var23.getKey(), ""))) {
               throw new IOException("verification failed for " + (String)var23.getKey());
            }
         }

         return true;
      } catch (Exception var17) {
         LarpLog.warn("Rig", "Could not save dispenser assignments to {}: {}", getStorePath(), var17.getMessage());
         return false;
      }
   }

   public record AssignResult(boolean success, String message) {
   }

   public enum GameType {
      PAPER_HOST("paper_host", "Paper host"),
      PAPER_VIEWER("paper_viewer", "Paper viewer"),
      FORTY_FIVE_TEN("forty_five_ten", "45/45/10"),
      FIFTY_FIFTY("fifty_fifty", "50/50"),
      BLACKJACK("blackjack", "Blackjack"),
      BLACKJACK_FORCE_ONLY("blackjack_fo", "Blackjack Force Only"),
      ODD_EVEN("odd_even", "Even or Odd"),
      HIGHER_LOWER("higher_lower", "Higher or Lower"),
      BACCARAT_PLAYER("baccarat_player", "Baccarat Player"),
      BACCARAT_BANKER("baccarat_banker", "Baccarat Banker"),
      MEXICAN_JACK("mexican_jack", "Mexican Jack"),
      RUSSIAN_ROULETTE("russian", "Russian Roulette"),
      HORSE_RACING("horse_racing", "Horse Racing"),
      ARMOR_GAME("armor_game", "Armor Game"),
      ARROW_GAME("arrow", "Arrow Game");

      public final String KEY;
      public final String DISPLAYNAME;

      GameType(String var3, String var4) {
         this.KEY = var3;
         this.DISPLAYNAME = var4;
      }

      private static DispenserRegistry.GameType fromKey(String var0) {
         if (var0 == null) {
            return null;
         }

         String var1 = var0.trim();
         if ("paper".equalsIgnoreCase(var1)) {
            return PAPER_HOST;
         }

         if ("baccarat".equalsIgnoreCase(var1)) {
            return BACCARAT_PLAYER;
         }

         if (!"blackjack_force".equalsIgnoreCase(var1) && !"blackjack_normal".equalsIgnoreCase(var1)) {
            for (DispenserRegistry.GameType var5 : values()) {
               if (var5.KEY.equalsIgnoreCase(var1)) {
                  return var5;
               }
            }

            return null;
         } else {
            return BLACKJACK;
         }
      }
   }
}
