package com.larpclient;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

final class fq implements ByteChannel {
   private final HANDLE q;
   private final IntByReference a = new IntByReference();
   private final IntByReference w = new IntByReference();
   private volatile boolean S = (boolean)1;

   fq(String var1) throws IOException {
      this.q = Kernel32.INSTANCE.CreateFile(var1, -1073741824, 0, null, 3, 0, null);
      if (WinBase.INVALID_HANDLE_VALUE.equals(this.q)) {
         throw e("open");
      }

      if (!Kernel32.INSTANCE.SetNamedPipeHandleState(this.q, new IntByReference(1), null, null)) {
         IOException var2 = e("nonblocking mode");
         this.close();
         throw var2;
      }
   }

   @Override
   public synchronized int read(ByteBuffer var1) throws IOException {
      if (!this.S) {
         return -1;
      }

      if (!var1.hasRemaining()) {
         return 0;
      }

      if (!Kernel32.INSTANCE.PeekNamedPipe(this.q, null, 0, null, this.a, null)) {
         throw e("peek");
      }

      int var2 = Math.min(var1.remaining(), this.a.getValue());
      if (var2 == 0) {
         return 0;
      }

      byte[] var3 = new byte[var2];
      if (!Kernel32.INSTANCE.ReadFile(this.q, var3, var2, this.w, null)) {
         throw e("read");
      }

      var1.put(var3, 0, this.w.getValue());
      return this.w.getValue();
   }

   @Override
   public synchronized int write(ByteBuffer var1) throws IOException {
      if (!this.S) {
         throw new IOException("Discord pipe closed");
      }

      byte[] var2 = new byte[var1.remaining()];
      var1.duplicate().get(var2);
      if (!Kernel32.INSTANCE.WriteFile(this.q, var2, var2.length, this.w, null)) {
         throw e("write");
      }

      var1.position(var1.position() + this.w.getValue());
      return this.w.getValue();
   }

   @Override
   public boolean isOpen() {
      return this.S;
   }

   @Override
   public synchronized void close() throws IOException {
      if (this.S) {
         this.S = (boolean)0;
         if (!Kernel32.INSTANCE.CloseHandle(this.q)) {
            throw e("close");
         }
      }
   }

   private static IOException e(String var0) {
      return new IOException("Discord pipe " + var0 + " failed (Win32 " + Kernel32.INSTANCE.GetLastError() + ")");
   }
}
