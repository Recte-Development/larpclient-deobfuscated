package com.larpclient.util.data;

public final class SequenceMarker {
   private long epoch;
   private long primaryCount;
   private long secondaryCount;
   private long primaryMarkEpoch = -1L;
   private long secondaryMarkEpoch = -1L;
   private long primaryMarkCount = -1L;
   private long secondaryMarkCount = -1L;

   public void startEpoch() {
      this.epoch++;
      this.primaryCount = this.secondaryCount = 0L;
   }

   public void incrementPrimary() {
      this.primaryCount++;
   }

   public void incrementSecondary() {
      this.secondaryCount++;
   }

   public boolean isPrimaryMarked() {
      return this.primaryMarkEpoch == this.epoch && this.primaryMarkCount == this.primaryCount;
   }

   public boolean isSecondaryMarked() {
      return this.secondaryMarkEpoch == this.epoch && this.secondaryMarkCount == this.secondaryCount;
   }

   public void markPrimary() {
      this.primaryMarkEpoch = this.epoch;
      this.primaryMarkCount = this.primaryCount;
   }

   public void markSecondary() {
      this.secondaryMarkEpoch = this.epoch;
      this.secondaryMarkCount = this.secondaryCount;
   }

   public void clearMarks() {
      this.primaryMarkEpoch = this.secondaryMarkEpoch = -1L;
   }
}
