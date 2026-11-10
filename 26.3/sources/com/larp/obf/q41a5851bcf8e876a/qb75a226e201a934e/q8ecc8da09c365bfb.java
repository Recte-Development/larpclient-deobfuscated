package com.larp.obf.q41a5851bcf8e876a.qb75a226e201a934e;

import net.minecraft.client.gui.screens.dialog.DialogScreen;
import net.minecraft.server.dialog.Dialog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DialogScreen.class)
public interface q8ecc8da09c365bfb {
   @Accessor("dialog")
   Dialog larp$dialog();
}
