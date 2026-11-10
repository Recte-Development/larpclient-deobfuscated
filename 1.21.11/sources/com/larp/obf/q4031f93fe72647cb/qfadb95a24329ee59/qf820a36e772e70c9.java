package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import net.minecraft.class_636;
import net.minecraft.class_638;
import net.minecraft.class_7204;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(class_636.class)
public interface qf820a36e772e70c9 {
   @Invoker("method_2911")
   void larpRig$syncSelectedSlot();

   @Invoker("method_41931")
   void larpRig$sendSequencedPacket(class_638 var1, class_7204 var2);
}
