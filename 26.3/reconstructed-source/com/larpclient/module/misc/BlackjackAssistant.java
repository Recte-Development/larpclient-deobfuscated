package com.larpclient.module.misc;

import com.larpclient.module.exploit.BlackjackRigger;
import com.larpclient.module.exploit.CardForce;
import com.larpclient.util.misc.RigLog;
import com.larpclient.util.misc.RigLogger;
import com.larpclient.util.world.DispenserRegistry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.saveddata.maps.MapId;

public final class BlackjackAssistant {
   private static final BlackjackAssistant iNSTANCE = new BlackjackAssistant();
   private static final RigLogger lOGGER = RigLogger.forModule("BJAssistant");
   private final List<List<Integer>> hands = new ArrayList<>();
   private final List<Integer> dealerCards = new ArrayList<>();
   private int handCount = 1;
   private int currentHand;
   private BlackjackAssistant.Side forcedSide;
   private int recommendedSecond = -1;
   private Map<Integer, Integer> baselineMaps;
   private Map<Integer, Integer> countedMaps = new HashMap<>();
   private Map<Integer, Integer> lastSeenMaps = new HashMap<>();
   private int stableTicks;
   private BlackjackAssistant.Side side = BlackjackAssistant.Side.PLAYER;
   private final long createdAt = System.currentTimeMillis();
   private int extraCards;
   private int emptyHandTicks;
   private int pendingActivations;
   private boolean roundActive;
   private boolean useServerValues;
   private boolean enabled = (boolean)1;
   private boolean autoArm;
   private long lastActionTime;
   private int dealtCount;
   private int recommendedCard = -1;

   private BlackjackAssistant() {
   }

   public static BlackjackAssistant getInstance() {
      return iNSTANCE;
   }

   public void setAutoArm(boolean var1) {
      this.autoArm = var1;
   }

   public void startRound(Minecraft var1) {
      this.beginRound(var1, false);
   }

   public void beginRound(Minecraft var1, boolean var2) {
      if (!this.roundActive) {
         CardForce.getInstance().onMapCountRise();
         this.hands.clear();
         this.dealerCards.clear();
         this.dealtCount = 0;
         this.recommendedCard = -1;
         this.recommendedSecond = -1;
         this.extraCards = 0;
         this.emptyHandTicks = 0;
         this.roundActive = (boolean)1;
         this.useServerValues = var2;
         if (this.baselineMaps == null && var1 != null && var1.player != null) {
            this.baselineMaps = this.pickHighestCard(var1);
            this.countedMaps = new HashMap<>(this.baselineMaps);
            this.lastSeenMaps = new HashMap<>(this.baselineMaps);
            this.stableTicks = 0;
         }
      }

      this.pendingActivations++;
   }

   public void tick(Minecraft var1, CardForce var2, boolean var3) {
      if (!var3 || var1 == null || var1.player == null) {
         this.reset();
      } else if (var2 == null || !var2.isSwapBusy()) {
         Map var4 = this.pickHighestCard(var1);
         if (!var4.equals(this.lastSeenMaps)) {
            this.lastSeenMaps = new HashMap<>(var4);
            this.stableTicks = 0;
         } else {
            this.stableTicks++;
         }

         if (this.baselineMaps == null) {
            this.baselineMaps = new HashMap<>(var4);
            this.countedMaps = new HashMap<>(var4);
            this.lastSeenMaps = new HashMap<>(var4);
         } else if (!this.roundActive) {
            this.baselineMaps = new HashMap<>(var4);
            this.countedMaps = new HashMap<>(var4);
            this.lastSeenMaps = new HashMap<>(var4);
            this.extraCards = 0;
            this.emptyHandTicks = 0;
         } else {
            int var5 = this.countNewMaps(var4);
            if (this.stableTicks >= 2 && var5 > this.extraCards) {
               int var6 = var5 - this.extraCards;
               int var7 = Math.min(var6, this.pendingActivations);
               if (var6 > var7) {
                  RigLog.logDebug("BJ hand ignored duplicate map-count rise=" + var6 + " activations=" + this.pendingActivations);
               }

               if (var7 != 0) {
                  int var8 = 0;

                  for (Entry var10 : var4.entrySet()) {
                     int var11 = Math.max(0, (Integer)var10.getValue() - this.countedMaps.getOrDefault(var10.getKey(), 0));

                     while (var11-- > 0 && var8 < var7) {
                        int var12 = this.findCardValue(var1, var2, (Integer)var10.getKey());
                        if (var12 >= 1 && var12 <= 10) {
                           var2.continueFromPacket(var12, (Integer)var10.getKey());
                           this.recordCard(var12);
                           this.pendingActivations--;
                           var8++;
                           this.countedMaps.merge((Integer)var10.getKey(), 1, Integer::sum);
                        }
                     }

                     if (var8 >= var7) {
                        break;
                     }
                  }

                  if (var8 > 0) {
                     this.extraCards += var8;
                  }
               }
            }

            this.emptyHandTicks = var5 <= 0 && !BlackjackRigger.getInstance().isTransferActive() ? this.emptyHandTicks + 1 : 0;
            if (this.dealtCount > 0 && this.emptyHandTicks >= 1 && this.emptyHandTicks % 5 == 1) {
               lOGGER.info(
                  "BJ {} reset-watch dealtCards={} extras={} emptyHandTicks={} busy={}",
                  this.sideId(),
                  this.dealtCount,
                  var5,
                  this.emptyHandTicks,
                  var2 != null && var2.isSwapBusy()
               );
            }

            if (this.dealtCount > 0 && this.emptyHandTicks >= 6) {
               lOGGER.info("BJ {} round reset (cards returned)", this.sideId());
               this.hands.clear();
               this.dealerCards.clear();
               this.dealtCount = 0;
               this.recommendedCard = -1;
               this.recommendedSecond = -1;
               this.baselineMaps = new HashMap<>(var4);
               this.countedMaps = new HashMap<>(var4);
               this.lastSeenMaps = new HashMap<>(var4);
               this.extraCards = 0;
               this.emptyHandTicks = 0;
               this.pendingActivations = 0;
               this.roundActive = (boolean)0;
               if (this.side != BlackjackAssistant.Side.PLAYER || this.currentHand != 0) {
                  this.side = BlackjackAssistant.Side.PLAYER;
                  this.currentHand = 0;
                  RigLog.logMessage("Blackjack", "Round over; side reset to hand 1");
               }
            }

            if (BlackjackRigger.getInstance().isEnabled()) {
               this.updateSide();
            }

            int var13 = this.handCount * 2 + 1;
            if (this.dealtCount >= var13 && this.side == BlackjackAssistant.Side.DEALER) {
               int[] var14 = this.recommend(var2);
               if (var14 == null) {
                  this.recommendedCard = -1;
                  this.recommendedSecond = -1;
               } else {
                  this.recommendedCard = var14[0];
                  this.recommendedSecond = var14[1];
               }
            } else {
               this.recommendedCard = -1;
               this.recommendedSecond = -1;
            }

            this.armRecommendation(var2);
         }
      }
   }

   public boolean allowsCard(int var1, int var2) {
      if (this.side != BlackjackAssistant.Side.DEALER) {
         return true;
      } else if (this.hands.isEmpty()) {
         return true;
      } else {
         int var3 = this.bestPlayerTotal();
         if (var3 < 0) {
            return false;
         } else if (var3 >= 2 && var1 >= 1 && var2 >= 1) {
            int var4 = totalWith(this.dealerCards, var1).total;
            return var4 < 17 || var4 > 21 || var4 <= var3;
         } else {
            return true;
         }
      }
   }

   private void armRecommendation(CardForce var1) {
      if (var1 != null && !var1.isSwapBusy() && this.autoArm) {
         if (this.recommendedCard >= 1) {
            int var2 = var1.getPendingValue();
            if (var2 != this.recommendedCard || var1.getQueuedValueAt(1) != this.recommendedSecond) {
               var1.queueForceSequence(this.recommendedCard, this.recommendedSecond);
               RigLog.logDebug(
                  "SRIG "
                     + this.sideId()
                     + " armed plan rec="
                     + (this.recommendedSecond >= 1 ? this.recommendedCard + "->" + this.recommendedSecond : this.recommendedCard)
               );
            }
         } else if (var1.hasQueuedForce()) {
            var1.clearQueue();
            RigLog.logDebug("SRIG " + this.sideId() + " cleared plan (no recommendation)");
         }
      }
   }

   private void recordCard(int var1) {
      int var2 = this.handCount;
      if (this.dealtCount < var2) {
         this.addToHand(this.dealtCount, var1);
      } else if (this.dealtCount == var2) {
         this.dealerCards.add(var1);
      } else if (this.dealtCount <= var2 * 2) {
         this.addToHand(this.dealtCount - var2 - 1, var1);
      } else if (this.side == BlackjackAssistant.Side.PLAYER) {
         this.addToHand(this.currentHand, var1);
      } else {
         this.dealerCards.add(var1);
      }

      this.dealtCount++;
   }

   private void addToHand(int var1, int var2) {
      while (this.hands.size() <= var1) {
         this.hands.add(new ArrayList<>());
      }

      this.hands.get(var1).add(var2);
   }

   private int[] recommend(CardForce var1) {
      int var2 = this.side == BlackjackAssistant.Side.DEALER ? 1 : 0;
      List var3 = var2 != 0 ? this.dealerCards : this.currentHandCards();
      if (var3.isEmpty()) {
         return null;
      }

      int var4 = var2 != 0 ? this.bestPlayerTotal() : cardLabel(this.dealerCards).total;
      if (var2 != 0 && var4 < 0) {
         return null;
      }

      if (var2 != 0 && cardLabel(var3).total >= 17) {
         return null;
      }

      int var5 = -1;
      int var6 = -1;

      for (int var7 = 1; var7 <= 10; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = totalWith(var3, var7).total;
            if (var8 <= 21 && var8 >= 17) {
               if (var8 > var4 && var5 < 0) {
                  var5 = var7;
               } else if (var8 == var4 && var6 < 0) {
                  var6 = var7;
               }
            }
         }
      }

      if (var5 >= 1) {
         return new int[]{var5, -1};
      }

      for (int var12 = 1; var12 <= 10; var12++) {
         if (var1.getTemplateForValue(var12) != null) {
            int var15 = totalWith(var3, var12).total;
            if (var15 <= 21 && (var2 == 0 || var15 < 17)) {
               ArrayList var9 = new ArrayList(var3);
               var9.add(var12);

               for (int var10 = 1; var10 <= 10; var10++) {
                  if (var1.getTemplateForValue(var10) != null) {
                     int var11 = totalWith(var9, var10).total;
                     if (var11 <= 21 && var11 >= 17 && var11 > var4) {
                        return new int[]{var12, var10};
                     }
                  }
               }
            }
         }
      }

      if (var6 >= 1) {
         return new int[]{var6, -1};
      }

      for (int var13 = 1; var13 <= 10; var13++) {
         if (var1.getTemplateForValue(var13) != null) {
            int var16 = totalWith(var3, var13).total;
            if (var16 <= 21 && (var2 == 0 || var16 < 17)) {
               ArrayList var17 = new ArrayList(var3);
               var17.add(var13);

               for (int var18 = 1; var18 <= 10; var18++) {
                  if (var1.getTemplateForValue(var18) != null) {
                     int var19 = totalWith(var17, var18).total;
                     if (var19 <= 21 && var19 >= 17 && var19 == var4) {
                        return new int[]{var13, var18};
                     }
                  }
               }
            }
         }
      }

      int var14 = var2 != 0 ? this.pickDealerCard(var1, var3, var4) : this.recommend(var1, var3, var4);
      return var14 >= 1 ? new int[]{var14, -1} : null;
   }

   private List<Integer> currentHandCards() {
      return this.currentHand >= 0 && this.currentHand < this.hands.size() ? this.hands.get(this.currentHand) : new ArrayList<>();
   }

   private int recommend(CardForce var1, List<Integer> var2, int var3) {
      ArrayList var4 = new ArrayList();
      ArrayList var5 = new ArrayList();

      for (int var6 = 1; var6 <= 10; var6++) {
         if (var1.getTemplateForValue(var6) != null) {
            int var7 = totalWith(var2, var6).total;
            if (var7 <= 21) {
               if (var7 > var3) {
                  var4.add(var6);
               } else if (var7 == var3) {
                  var5.add(var6);
               }
            }
         }
      }

      if (!var4.isEmpty()) {
         return (Integer)var4.get(ThreadLocalRandom.current().nextInt(var4.size()));
      } else {
         return !var5.isEmpty() ? (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size())) : this.pickHighestCard(var1, var2);
      }
   }

   private int maxHandTotal() {
      int var1 = 0;

      for (List var3 : this.hands) {
         var1 = Math.max(var1, cardLabel(var3).total);
      }

      return var1;
   }

   private void updateSide() {
      int var1 = -1;

      for (int var2 = 0; var2 < this.handCount && var1 < 0; var2++) {
         if (var2 >= this.hands.size() || cardLabel(this.hands.get(var2)).total < 17) {
            var1 = var2;
         }
      }

      if (var1 >= 0) {
         this.currentHand = var1;
      }

      this.side = this.forcedSide != null ? this.forcedSide : (var1 < 0 ? BlackjackAssistant.Side.DEALER : BlackjackAssistant.Side.PLAYER);
   }

   private int bestPlayerTotal() {
      int var1 = -1;

      for (List var3 : this.hands) {
         int var4 = cardLabel(var3).total;
         if (var4 <= 21) {
            var1 = Math.max(var1, var4);
         }
      }

      return var1;
   }

   private int pickDealerCard(CardForce var1, List<Integer> var2, int var3) {
      int var4 = cardLabel(var2).total;
      if (var4 >= 17) {
         return -1;
      }

      ArrayList var5 = new ArrayList();
      ArrayList var6 = new ArrayList();

      for (int var7 = 1; var7 <= 10; var7++) {
         if (var1.getTemplateForValue(var7) != null) {
            int var8 = totalWith(var2, var7).total;
            if (var8 <= 21 && var8 >= 17) {
               if (var8 > var3) {
                  for (int var9 = 0; var9 < 6; var9++) {
                     var5.add(var7);
                  }
               } else if (var8 == var3) {
                  var6.add(var7);
               }
            }
         }
      }

      if (!var5.isEmpty()) {
         for (int var15 : var6) {
            var5.add(var15);
         }

         return (Integer)var5.get(ThreadLocalRandom.current().nextInt(var5.size()));
      } else {
         if (var4 < 17) {
            ArrayList var12 = new ArrayList();

            for (int var14 = 1; var14 <= 10; var14++) {
               if (var1.getTemplateForValue(var14) != null) {
                  int var16 = totalWith(var2, var14).total;
                  int var10 = var16 == 16 ? 6 : (var16 == 15 ? 4 : (var16 == 14 ? 2 : 0));

                  for (int var11 = 0; var11 < var10; var11++) {
                     var12.add(var14);
                  }
               }
            }

            if (!var12.isEmpty()) {
               return (Integer)var12.get(ThreadLocalRandom.current().nextInt(var12.size()));
            }
         }

         return !var6.isEmpty() ? (Integer)var6.get(ThreadLocalRandom.current().nextInt(var6.size())) : this.pickHighestCard(var1, var2);
      }
   }

   private int pickHighestCard(CardForce var1, List<Integer> var2) {
      int var3 = -1;
      int var4 = -1;

      for (int var5 = 1; var5 <= 10; var5++) {
         if (var1.getTemplateForValue(var5) != null) {
            int var6 = totalWith(var2, var5).total;
            if (var6 <= 21 && var6 > var4) {
               var4 = var6;
               var3 = var5;
            }
         }
      }

      return var3;
   }

   private Map<Integer, Integer> pickHighestCard(Minecraft var1) {
      HashMap var2 = new HashMap();
      BlackjackRigger var3 = BlackjackRigger.getInstance();

      for (int var4 = 0; var4 < var1.player.getInventory().getContainerSize(); var4++) {
         ItemStack var5 = var1.player.getInventory().getItem(var4);
         int var6 = var3.importHotbar(var4, var5);
         if (var6 < var5.getCount()) {
            this.countMapItem(var2, var6 == 0 ? var5 : var5.copyWithCount(var5.getCount() - var6));
         }
      }

      if (var1.level != null) {
         for (Entity var9 : var1.level.entitiesForRendering()) {
            if (var9 instanceof ItemFrame var10) {
               this.countMapItem(var2, var10.getItem());
            } else if (var9 instanceof ItemEntity var7) {
               this.countMapItem(var2, var7.getItem());
            }
         }
      }

      return var2;
   }

   private void countMapItem(Map<Integer, Integer> var1, ItemStack var2) {
      if (var2 != null && !var2.isEmpty() && var2.is(Items.FILLED_MAP)) {
         MapId var3 = (MapId)var2.getComponents().get(DataComponents.MAP_ID);
         if (var3 != null && this.mapCardValue(var3) >= 1) {
            var1.merge(var3.id(), var2.getCount(), Integer::sum);
         }
      }
   }

   private int mapCardValue(MapId var1) {
      BlackjackRigger var2 = BlackjackRigger.getInstance();
      return this.useServerValues && var2.hasMapMapping() ? var2.rankForMapId(var1.id()) : CardForce.getInstance().getMapId(var1);
   }

   private int findCardValue(Minecraft var1, CardForce var2, int var3) {
      for (int var4 = 0; var4 < var1.player.getInventory().getContainerSize(); var4++) {
         ItemStack var5 = var1.player.getInventory().getItem(var4);
         if (var5 != null && !var5.isEmpty() && var5.is(Items.FILLED_MAP)) {
            MapId var6 = (MapId)var5.getComponents().get(DataComponents.MAP_ID);
            if (var6 != null && var6.id() == var3) {
               if (DispenserRegistry.getInstance().saveAssignments(DispenserRegistry.GameType.BLACKJACK_FORCE_ONLY)) {
                  return this.mapCardValue(var6);
               }

               if (BlackjackRigger.getInstance().isEnabled()) {
                  return var2.getMapId(var6);
               }

               return var2.setPhase("inventory[" + var4 + "]", var5);
            }
         }
      }

      if (var1.level != null) {
         for (Entity var10 : var1.level.entitiesForRendering()) {
            if (var10 instanceof ItemFrame var11) {
               ItemStack var7 = var11.getItem();
               if (var7 != null && !var7.isEmpty() && var7.is(Items.FILLED_MAP)) {
                  MapId var8 = (MapId)var7.getComponents().get(DataComponents.MAP_ID);
                  if (var8 != null && var8.id() == var3) {
                     if (DispenserRegistry.getInstance().saveAssignments(DispenserRegistry.GameType.BLACKJACK_FORCE_ONLY)) {
                        return this.mapCardValue(var8);
                     }

                     if (BlackjackRigger.getInstance().isEnabled()) {
                        return var2.getMapId(var8);
                     }

                     return var2.setPhase("item_frame[" + var11.getId() + "]", var7);
                  }
               }
            }
         }
      }

      return -1;
   }

   private int countNewMaps(Map<Integer, Integer> var1) {
      int var2 = 0;

      for (Entry var4 : var1.entrySet()) {
         var2 += Math.max(0, (Integer)var4.getValue() - this.baselineMaps.getOrDefault(var4.getKey(), 0));
      }

      return var2;
   }

   private void reset() {
      CardForce.getInstance().onMapCountRise();
      this.hands.clear();
      this.dealerCards.clear();
      this.baselineMaps = null;
      this.countedMaps.clear();
      this.lastSeenMaps.clear();
      this.stableTicks = 0;
      this.extraCards = 0;
      this.emptyHandTicks = 0;
      this.pendingActivations = 0;
      this.roundActive = (boolean)0;
      this.useServerValues = (boolean)0;
      this.dealtCount = 0;
      this.recommendedCard = -1;
      this.recommendedSecond = -1;
   }

   public int countMapItem() {
      return this.maxHandTotal();
   }

   public int startRound() {
      return cardLabel(this.dealerCards).total;
   }

   public boolean isDealerTurn() {
      return this.side == BlackjackAssistant.Side.DEALER;
   }

   public int pickHitCard(boolean[] var1) {
      if (!this.isDealerTurn() && this.dealtCount >= this.handCount * 2 + 1 && this.pendingActivations <= 0) {
         List var2 = this.currentHandCards();
         int var3 = cardLabel(var2).total;
         return var3 >= 12 && var3 < 17 ? pickSafeCard(var2, var1) : -1;
      } else {
         return -1;
      }
   }

   public boolean addToHand(int var1, int var2) {
      return isValidPick(this.currentHandCards(), var1, var2);
   }

   public static boolean isValidPick(List<Integer> var0, int var1, int var2) {
      if (!var0.isEmpty() && var1 >= 1 && var1 <= 10 && var2 >= 1 && var2 <= 10) {
         int var3 = totalWith(var0, var1).total;
         int var4 = totalWith(var0, var2).total;
         return var4 <= 21 && (var3 < 17 || var4 >= 17);
      } else {
         return false;
      }
   }

   public static int pickSafeCard(List<Integer> var0, boolean[] var1) {
      if (!var0.isEmpty() && var1.length >= 11) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();

         for (int var4 = 1; var4 <= 10; var4++) {
            if (var1[var4]) {
               int var5 = totalWith(var0, var4).total;
               if (var5 >= 17 && var5 <= 21) {
                  var2.add(var4);
               } else if (var5 < 17) {
                  var3.add(var4);
               }
            }
         }

         ArrayList var6 = var2.isEmpty() ? var3 : var2;
         return var6.isEmpty() ? -1 : (Integer)var6.get(ThreadLocalRandom.current().nextInt(var6.size()));
      } else {
         return -1;
      }
   }

   public int pickHighestCard(boolean[] var1) {
      if (this.isDealerTurn() && this.dealtCount >= this.handCount * 2 + 1 && this.pendingActivations <= 0) {
         int var2 = 0;

         for (List var4 : this.hands) {
            int var5 = cardLabel(var4).total;
            if (var5 <= 21) {
               var2 = Math.max(var2, var5);
            }
         }

         return pickRiggedCard(var2, this.dealerCards, var1);
      } else {
         return -1;
      }
   }

   public static int pickRiggedCard(int var0, List<Integer> var1, boolean[] var2) {
      int var3 = cardLabel(var1).total;
      if (var0 >= 2 && var0 <= 21 && !var1.isEmpty() && var2.length >= 11 && var3 < 17) {
         if (var0 >= 18) {
            for (int var4 = 1; var4 <= 10; var4++) {
               if (var2[var4] && totalWith(var1, var4).total == 17) {
                  return var4;
               }
            }
         }

         ArrayList var16 = new ArrayList();
         int var5 = -1;
         int var6 = 21;

         for (int var7 = 1; var7 <= 10; var7++) {
            if (var2[var7]) {
               int var8 = totalWith(var1, var7).total;
               if (var8 > var6) {
                  var5 = var7;
                  var6 = var8;
               }

               if (var8 >= 24) {
                  var16.add(var7);
               }
            }
         }

         int var17 = var16.isEmpty() ? var5 : (Integer)var16.get(ThreadLocalRandom.current().nextInt(var16.size()));
         if (var17 <= 0 || var3 >= 12 && var3 <= 15) {
            int var18 = -1;
            int var9 = -1;
            ArrayList var10 = new ArrayList();
            ArrayList var11 = new ArrayList();
            boolean[] var12 = (boolean[])var2.clone();

            for (int var13 = 1; var13 <= 10; var13++) {
               if (var12[var13]) {
                  int var14 = totalWith(var1, var13).total;
                  if (var14 < 17) {
                     var12[var13] = (boolean)0;
                     ArrayList var15 = new ArrayList(var1);
                     var15.add(var13);
                     if (pickRiggedCard(var15, var12)) {
                        if (var14 >= 12) {
                           var10.add(var13);
                        }

                        if (var14 > var3) {
                           var11.add(var13);
                        }

                        if (var14 > var9) {
                           var18 = var13;
                           var9 = var14;
                        }
                     }

                     var12[var13] = (boolean)1;
                  }
               }
            }

            if (var17 <= 0) {
               return var10.isEmpty() ? var18 : (Integer)var10.get(ThreadLocalRandom.current().nextInt(var10.size()));
            } else {
               return !var11.isEmpty() && ThreadLocalRandom.current().nextBoolean()
                  ? (Integer)var11.get(ThreadLocalRandom.current().nextInt(var11.size()))
                  : var17;
            }
         } else {
            return var17;
         }
      } else {
         return -1;
      }
   }

   private static boolean pickRiggedCard(List<Integer> var0, boolean[] var1) {
      for (int var2 = 1; var2 <= 10; var2++) {
         if (var1[var2]) {
            int var3 = totalWith(var0, var2).total;
            if (var3 > 21) {
               return true;
            }

            if (var3 < 17) {
               var1[var2] = (boolean)0;
               ArrayList var4 = new ArrayList(var0);
               var4.add(var2);
               boolean var5 = pickRiggedCard(var4, var1);
               var1[var2] = (boolean)1;
               if (var5) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean hasSoftHand() {
      for (List var2 : this.hands) {
         if (cardLabel(var2).soft) {
            return true;
         }
      }

      return false;
   }

   public boolean isDealerSoft() {
      return cardLabel(this.dealerCards).soft;
   }

   public int getHandCount() {
      return this.handCount;
   }

   public int isValidPick(int var1) {
      return var1 >= 0 && var1 < this.hands.size() ? cardLabel(this.hands.get(var1)).total : 0;
   }

   public int recommend(int var1) {
      if (var1 >= 0 && var1 < this.hands.size()) {
         return cardLabel(this.hands.get(var1)).soft ? 1 : 0;
      } else {
         return 0;
      }
   }

   public void setHandCount(int var1) {
      int var2 = Math.max(1, Math.min(3, var1));
      if (this.handCount != var2) {
         this.handCount = var2;
         this.reset();
         this.currentHand = 0;
      }
   }

   public void isDealerTurn() {
      this.recommendedCard = -1;
      this.recommendedSecond = -1;
      if (this.side == BlackjackAssistant.Side.DEALER) {
         this.side = BlackjackAssistant.Side.PLAYER;
         this.currentHand = 0;
      } else {
         int var1 = this.currentHand + 1;
         if (var1 >= this.handCount) {
            this.side = BlackjackAssistant.Side.DEALER;
         } else {
            this.currentHand = var1;
         }
      }
   }

   public void setSideByName(String var1) {
      if ("dealer".equalsIgnoreCase(var1)) {
         this.side = BlackjackAssistant.Side.DEALER;
      } else if ("player".equalsIgnoreCase(var1)) {
         this.side = BlackjackAssistant.Side.PLAYER;
         this.currentHand = 0;
      }
   }

   public void armRecommendation(int var1) {
      BlackjackAssistant.Side var2 = var1 < 0 ? BlackjackAssistant.Side.DEALER : BlackjackAssistant.Side.PLAYER;
      int var3 = var1 < 0 ? this.currentHand : Math.min(var1, this.handCount - 1);
      if (var2 != this.side || var3 != this.currentHand) {
         this.side = var2;
         this.currentHand = var3;
         this.recommendedCard = -1;
         this.recommendedSecond = -1;
      }
   }

   public int allowsCard() {
      return this.side == BlackjackAssistant.Side.DEALER ? -1 : this.currentHand;
   }

   public void updateSide(String var1) {
      this.forcedSide = "player".equalsIgnoreCase(var1)
         ? BlackjackAssistant.Side.PLAYER
         : ("dealer".equalsIgnoreCase(var1) ? BlackjackAssistant.Side.DEALER : null);
   }

   public boolean isAutoSide() {
      return this.forcedSide == null;
   }

   public String pickHitCard() {
      if (this.side == BlackjackAssistant.Side.DEALER) {
         return "Dealer";
      } else {
         return this.handCount == 1 ? "Player" : "Hand " + (this.currentHand + 1);
      }
   }

   public String handText(int var1) {
      if (var1 < 0) {
         return this.dealerCards.isEmpty() ? "-" : formatHand(this.dealerCards);
      } else {
         return var1 < this.hands.size() && !this.hands.get(var1).isEmpty() ? formatHand(this.hands.get(var1)) : "-";
      }
   }

   private static String formatHand(List<Integer> var0) {
      BlackjackAssistant.Anonymous var1 = cardLabel(var0);
      return var1.total + (var1.total > 21 ? " bust" : (var1.soft ? " soft" : ""));
   }

   public String mapCardValue() {
      return switch (this.side) {
         case PLAYER -> "HAND " + (this.currentHand + 1);
         case DEALER -> "DEALER";
      };
   }

   public String sideId() {
      return switch (this.side) {
         case PLAYER -> "hand-" + this.currentHand;
         case DEALER -> "dealer";
      };
   }

   public int recommend() {
      return this.recommendedCard;
   }

   public int getRecommendedSecond() {
      return this.recommendedSecond;
   }

   public String addToHand() {
      return this.recommendedCard < 1 ? "-1" : this.recordCard();
   }

   public String pickDealerCard() {
      return this.recommendedCard < 1 ? "none" : this.recordCard();
   }

   private String recordCard() {
      StringBuilder var1 = new StringBuilder(cardLabel(this.recommendedCard));
      if (this.recommendedSecond >= 1) {
         var1.append(" -> ").append(cardLabel(this.recommendedSecond));
      }

      return var1.toString();
   }

   private static String cardLabel(int var0) {
      return switch (var0) {
         case 1 -> "A";
         case 10 -> "10";
         default -> String.valueOf(var0);
      };
   }

   public int addToHand() {
      return this.dealtCount;
   }

   public int pickDealerCard() {
      return this.pendingActivations;
   }

   public boolean countMapItem() {
      return this.enabled;
   }

   private static BlackjackAssistant.Anonymous totalWith(List<Integer> var0, int var1) {
      ArrayList var2 = new ArrayList(var0);
      var2.add(var1);
      return cardLabel(var2);
   }

   private static BlackjackAssistant.Anonymous cardLabel(List<Integer> var0) {
      int var1 = 0;
      int var2 = 0;

      for (int var4 : var0) {
         if (var4 == 1) {
            var2++;
            var1 += 11;
         } else {
            var1 += Math.min(10, Math.max(2, var4));
         }
      }

      while (var1 > 21 && var2 > 0) {
         var1 -= 10;
         var2--;
      }

      return new BlackjackAssistant.Anonymous(var1, var2 > 0);
   }

   private record Anonymous(int total, boolean soft) {
   }

   private enum Side {
      PLAYER,
      DEALER;
   }
}
