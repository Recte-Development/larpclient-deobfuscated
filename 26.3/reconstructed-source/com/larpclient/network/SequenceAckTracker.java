package com.larpclient.network;

final class SequenceAckTracker {
   private int sequence = -1;
   private int acknowledged = -1;
   private int minNextSequence = 1;
   private boolean cancelled;

   boolean canIssue(int var1) {
      return !this.cancelled && var1 >= this.minNextSequence && (this.sequence < 0 || this.isAcknowledged());
   }

   void begin(int var1, int var2) {
      this.sequence = var1;
      this.minNextSequence = var2 + 1;
   }

   void acknowledge(int var1) {
      this.acknowledged = Math.max(this.acknowledged, var1);
   }

   void cancel() {
      this.cancelled = (boolean)1;
   }

   boolean hasSequence() {
      return this.sequence >= 0;
   }

   boolean isAcknowledged() {
      return this.hasSequence() && (this.cancelled || this.acknowledged >= this.sequence);
   }

   String describe() {
      return "sequence=" + this.sequence + " acknowledged=" + this.acknowledged;
   }
}
