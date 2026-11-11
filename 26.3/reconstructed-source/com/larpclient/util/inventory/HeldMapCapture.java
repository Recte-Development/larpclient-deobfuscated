package com.larpclient.util.inventory;

import com.larpclient.module.exploit.CardForce;
import net.minecraft.world.item.ItemStack;

public interface HeldMapCapture {
   void larpRig$captureHeldMap(CardForce var1, ItemStack var2, int var3);

   void larpRig$releaseHeldMap(CardForce var1, ItemStack var2);
}
