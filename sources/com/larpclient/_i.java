package com.larpclient;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

final class _i implements ByteChannel {
   private final HANDLE B;
   private final IntByReference P = new IntByReference();
   private final IntByReference p = new IntByReference();
   private volatile boolean n = (boolean)1;

   _i(String var1) throws IOException {
      this.B = Kernel32.INSTANCE.CreateFile(var1, -1073741824, 0, null, 3, 0, null);
      if (WinBase.INVALID_HANDLE_VALUE.equals(this.B)) {
         throw R("open");
      }

      if (!Kernel32.INSTANCE.SetNamedPipeHandleState(this.B, new IntByReference(1), null, null)) {
         IOException var2 = R("nonblocking mode");
         this.close();
         throw var2;
      }
   }

   @Override
   public synchronized int read(ByteBuffer var1) throws IOException {
      if (!this.n) {
         return -1;
      }

      if (!var1.hasRemaining()) {
         return 0;
      }

      if (!Kernel32.INSTANCE.PeekNamedPipe(this.B, null, 0, null, this.P, null)) {
         throw R("peek");
      }

      int var2 = Math.min(var1.remaining(), this.P.getValue());
      if (var2 == 0) {
         return 0;
      }

      byte[] var3 = new byte[var2];
      if (!Kernel32.INSTANCE.ReadFile(this.B, var3, var2, this.p, null)) {
         throw R("read");
      }

      var1.put(var3, 0, this.p.getValue());
      return this.p.getValue();
   }

   @Override
   public synchronized int write(ByteBuffer var1) throws IOException {
      if (!this.n) {
         throw new IOException("Discord pipe closed");
      }

      byte[] var2 = new byte[var1.remaining()];
      var1.duplicate().get(var2);
      if (!Kernel32.INSTANCE.WriteFile(this.B, var2, var2.length, this.p, null)) {
         throw R("write");
      }

      var1.position(var1.position() + this.p.getValue());
      return this.p.getValue();
   }

   @Override
   public boolean isOpen() {
      return this.n;
   }

   @Override
   public synchronized void close() throws IOException {
      if (this.n) {
         this.n = (boolean)0;
         if (!Kernel32.INSTANCE.CloseHandle(this.B)) {
            throw R("close");
         }
      }
   }

   private static IOException R(String var0) {
      return new IOException("Discord pipe " + var0 + " failed (Win32 " + Kernel32.INSTANCE.GetLastError() + ")");
   }
}
