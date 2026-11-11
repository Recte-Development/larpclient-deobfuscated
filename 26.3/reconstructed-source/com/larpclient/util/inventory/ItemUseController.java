package com.larpclient.util.inventory;

public final class ItemUseController {
   private ItemUseController.Owner owner;
   private ItemUseController.UseContext context;
   private int previousSlot;
   private int useSlot;
   private int startTick;
   private boolean usingSeen;
   private boolean startedUnderScreen;

   public boolean isOwner(ItemUseController.Owner var1) {
      return this.owner == var1;
   }

   public void request(ItemUseController.Owner var1, ItemUseController.UseContext var2, int var3) {
      if (this.owner != null && (!this.context.current() || this.context.player() != var2.player())) {
         this.reset();
      }

      if (var2.allowed() && var3 >= 0) {
         if (this.owner != null && this.owner != var1) {
            if (var1 != ItemUseController.Owner.OVERRIDE) {
               return;
            }

            this.reset();
         }

         if (this.owner != var1) {
            if (!var2.usingItem() && !var2.manualUse() && var2.usable(var3)) {
               this.owner = var1;
               this.context = var2;
               this.previousSlot = var2.selected();
               this.useSlot = var3;
               this.startTick = var2.tick();
               this.usingSeen = this.startedUnderScreen = (boolean)0;
               var2.select(this.useSlot);
               var2.holdUse(true);
            }
         } else if (!this.context.manualUse() && this.context.selected() == this.useSlot && this.context.usable(this.useSlot)) {
            if (this.context.usingItem()) {
               this.usingSeen = (boolean)1;
            } else {
               if (this.usingSeen || this.context.tick() - this.startTick >= 8) {
                  this.reset();
                  return;
               }

               if (!this.startedUnderScreen) {
                  this.startedUnderScreen = this.context.startUnderScreen();
               }
            }

            this.context.holdUse(true);
         } else {
            this.reset();
         }
      } else {
         this.release(var1);
      }
   }

   public void release(ItemUseController.Owner var1) {
      if (this.owner == var1) {
         this.reset();
      }
   }

   public void reset() {
      ItemUseController.UseContext var1 = this.context;
      int var2 = this.previousSlot;
      int var3 = this.useSlot;
      this.owner = null;
      this.context = null;
      this.usingSeen = (boolean)0;
      if (var1 != null) {
         try {
            var1.releaseUse();
         } finally {
            if (var1.current() && !var1.manualUse() && var1.selected() == var3) {
               var1.select(var2);
            }
         }
      }
   }

   public enum Owner {
      NORMAL,
      OVERRIDE;
   }

   public interface UseContext {
      Object player();

      int tick();

      boolean current();

      boolean allowed();

      int selected();

      void select(int var1);

      boolean usable(int var1);

      boolean usingItem();

      boolean manualUse();

      void holdUse(boolean var1);

      void releaseUse();

      boolean startUnderScreen();
   }
}
