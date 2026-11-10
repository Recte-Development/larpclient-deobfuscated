package com.larpclient;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

final class jh implements ByteChannel {
   private final HANDLE e;
   private final IntByReference S = new IntByReference();
   private final IntByReference q = new IntByReference();
   private volatile boolean u = (boolean)1;

   jh(String var1) throws IOException {
      this.e = Kernel32.INSTANCE.CreateFile(var1, -1073741824, 0, null, 3, 0, null);
      if (WinBase.INVALID_HANDLE_VALUE.equals(this.e)) {
         throw W("open");
      }

      if (!Kernel32.INSTANCE.SetNamedPipeHandleState(this.e, new IntByReference(1), null, null)) {
         IOException var2 = W("nonblocking mode");
         this.close();
         throw var2;
      }
   }

   @Override
   public synchronized int read(ByteBuffer var1) throws IOException {
      if (!this.u) {
         return -1;
      }

      if (!var1.hasRemaining()) {
         return 0;
      }

      if (!Kernel32.INSTANCE.PeekNamedPipe(this.e, null, 0, null, this.S, null)) {
         throw W("peek");
      }

      int var2 = Math.min(var1.remaining(), this.S.getValue());
      if (var2 == 0) {
         return 0;
      }

      byte[] var3 = new byte[var2];
      if (!Kernel32.INSTANCE.ReadFile(this.e, var3, var2, this.q, null)) {
         throw W("read");
      }

      var1.put(var3, 0, this.q.getValue());
      return this.q.getValue();
   }

   @Override
   public synchronized int write(ByteBuffer var1) throws IOException {
      if (!this.u) {
         throw new IOException("Discord pipe closed");
      }

      byte[] var2 = new byte[var1.remaining()];
      var1.duplicate().get(var2);
      if (!Kernel32.INSTANCE.WriteFile(this.e, var2, var2.length, this.q, null)) {
         throw W("write");
      }

      var1.position(var1.position() + this.q.getValue());
      return this.q.getValue();
   }

   @Override
   public boolean isOpen() {
      return this.u;
   }

   @Override
   public synchronized void close() throws IOException {
      if (this.u) {
         this.u = (boolean)0;
         if (!Kernel32.INSTANCE.CloseHandle(this.e)) {
            throw W("close");
         }
      }
   }

   private static IOException W(String var0) {
      return new IOException("Discord pipe " + var0 + " failed (Win32 " + Kernel32.INSTANCE.GetLastError() + ")");
   }
}
