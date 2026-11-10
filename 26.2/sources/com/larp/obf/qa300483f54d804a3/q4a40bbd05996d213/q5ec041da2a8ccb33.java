package com.larp.obf.qa300483f54d804a3.q4a40bbd05996d213;

import net.minecraft.client.gui.screens.dialog.DialogScreen;
import net.minecraft.server.dialog.Dialog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DialogScreen.class)
public interface q5ec041da2a8ccb33 {
   @Accessor("dialog")
   Dialog larp$dialog();
}
