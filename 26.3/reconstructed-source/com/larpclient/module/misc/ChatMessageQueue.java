package com.larpclient.module.misc;

import java.util.function.IntUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

final class ChatMessageQueue {
   private static final Pattern playerPlaceholderPattern = Pattern.compile("\\{player}", 2);
   private static final Pattern payCommandPattern = Pattern.compile("/\\s*(?:[\\w.-]+:)?e?pay(?![\\w])", 2);
   private ChatMessageQueue.OutgoingMessage pending;
   private double sendTime;
   private double nextAllowedTime = Double.NEGATIVE_INFINITY;

   static boolean isPayCommand(String var0) {
      return payCommandPattern.matcher(var0).find();
   }

   static ChatMessageQueue.OutgoingMessage prepareMessage(String var0, String var1) {
      boolean var2 = var0.strip().startsWith("/");
      String var3 = playerPlaceholderPattern.matcher(var0).replaceAll(Matcher.quoteReplacement(var1));
      String var4 = StringUtil.trimChatMessage(StringUtils.normalizeSpace(StringUtil.filterText(var3).strip()));
      if (!isPayCommand(var0) && !isPayCommand(var4)) {
         if (var2) {
            var4 = var4.substring(1).strip();
         }

         return var4.isEmpty() ? null : new ChatMessageQueue.OutgoingMessage(var4, var2);
      } else {
         return null;
      }
   }

   static int nextIndex(int var0, int var1, boolean var2, IntUnaryOperator var3) {
      if (var0 <= 1) {
         return 0;
      } else if (!var2) {
         return (var1 + 1) % var0;
      } else if (var1 >= 0 && var1 < var0) {
         int var4 = var3.applyAsInt(var0 - 1);
         return var4 >= var1 ? var4 + 1 : var4;
      } else {
         return var3.applyAsInt(var0);
      }
   }

   boolean canQueue(double var1) {
      return this.pending == null && var1 >= this.nextAllowedTime;
   }

   void queue(ChatMessageQueue.OutgoingMessage var1, double var2) {
      this.pending = var1;
      this.sendTime = var2;
   }

   ChatMessageQueue.OutgoingMessage pollDue(double var1, double var3) {
      if (this.pending != null && !(var1 < this.sendTime)) {
         ChatMessageQueue.OutgoingMessage var5 = this.pending;
         this.pending = null;
         this.nextAllowedTime = var1 + var3;
         return var5;
      } else {
         return null;
      }
   }

   void clearPending() {
      this.pending = null;
   }

   record OutgoingMessage(String text, boolean command) {
   }
}
