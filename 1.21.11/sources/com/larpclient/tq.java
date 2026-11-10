package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.GpuDevice;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.RenderSystem.class_5590;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.class_5595;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.function.Supplier;
import net.minecraft.class_287;
import net.minecraft.class_4588;
import net.minecraft.class_8251;
import net.minecraft.class_9799;
import net.minecraft.class_9801;

public final class tq implements AutoCloseable {
   private final Supplier<String> I;
   private final int A;
   private final ArrayList<tq.t> l = new ArrayList<>();
   private int q;

   public tq(Supplier<String> var1, int var2) {
      this.I = var1;
      this.A = var2;
   }

   public tq.t F(VertexFormat var1, class_5596 var2, class_8251 var3) {
      if (this.q == this.l.size()) {
         this.l.add(new tq.t(Math.min(this.A, 65536)));
      }

      tq.t var4 = this.l.get(this.q++);
      var4.u = new class_287(var4.J, var2, var1);
      var4.F = var3;
      var4.O = null;
      return var4;
   }

   public class_4588 l(tq.t var1) {
      return var1.u;
   }

   public void a() {
      GpuDevice var1 = RenderSystem.getDevice();
      CommandEncoder var2 = var1.createCommandEncoder();

      for (int var3 = 0; var3 < this.q; var3++) {
         tq.t var4 = this.l.get(var3);
         class_9801 var5 = var4.u.method_60794();
         var4.u = null;
         if (var5 != null) {
            class_9801 var6 = var5;

            try {
               if (var4.F != null && var5.method_60822().comp_752() == class_5596.field_27382) {
                  var5.method_60819(var4.J, var4.F);
               }

               ByteBuffer var7 = var5.method_60818();
               if (var4.Q == null || var4.Q.size() < var7.remaining()) {
                  if (var4.Q != null) {
                     var4.Q.close();
                  }

                  var4.Q = var1.createBuffer(this.I, 40, var7.remaining());
               }

               var2.writeToBuffer(var4.Q.slice(0L, var7.remaining()), var7);
               ByteBuffer var10 = var5.method_60821();
               GpuBuffer var8;
               class_5595 var9;
               if (var10 == null) {
                  class_5590 var11 = RenderSystem.getSequentialBuffer(var5.method_60822().comp_752());
                  var8 = var11.method_68274(var5.method_60822().comp_751());
                  var9 = var11.method_31924();
               } else {
                  if (var4.a == null || var4.a.size() < var10.remaining()) {
                     if (var4.a != null) {
                        var4.a.close();
                     }

                     var4.a = var1.createBuffer(this.I, 72, var10.remaining());
                  }

                  var2.writeToBuffer(var4.a.slice(0L, var10.remaining()), var10);
                  var8 = var4.a;
                  var9 = var5.method_60822().comp_753();
               }

               var4.O = new tq.l(var4.Q, var8, var9, var5.method_60822().comp_751(), 0, 0);
            } catch (Throwable var13) {
               if (var6 != null) {
                  try {
                     var6.close();
                  } catch (Throwable var12) {
                     var13.addSuppressed(var12);
                  }
               }

               throw var13;
            }

            if (var6 != null) {
               var6.close();
            }
         }
      }
   }

   public tq.l a(tq.t var1) {
      return var1.O;
   }

   public void S() {
      for (int var1 = 0; var1 < this.q; var1++) {
         tq.t var2 = this.l.get(var1);
         var2.J.method_60809();
         var2.u = null;
         var2.O = null;
      }

      this.q = 0;
   }

   public void X() {
      this.S();
   }

   @Override
   public void close() {
      for (tq.t var2 : this.l) {
         var2.J.close();
         if (var2.Q != null) {
            var2.Q.close();
         }

         if (var2.a != null) {
            var2.a.close();
         }
      }

      this.l.clear();
      this.q = 0;
   }

   public record l(GpuBuffer vertexBuffer, GpuBuffer indexBuffer, class_5595 indexType, int indexCount, int firstIndex, int baseVertex) {
   }

   public static final class t {
      final class_9799 J;
      class_287 u;
      class_8251 F;
      GpuBuffer Q;
      GpuBuffer a;
      tq.l O;

      t(int var1) {
         this.J = new class_9799(var1);
      }
   }
}
