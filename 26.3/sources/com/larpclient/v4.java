package com.larpclient;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;

final class v4 implements ByteChannel {
   private final HANDLE R;
   private final IntByReference e = new IntByReference();
   private final IntByReference r = new IntByReference();
   private volatile boolean V = (boolean)1;

   v4(String var1) throws IOException {
      this.R = Kernel32.INSTANCE.CreateFile(var1, -1073741824, 0, null, 3, 0, null);
      if (WinBase.INVALID_HANDLE_VALUE.equals(this.R)) {
         throw r("open");
      }

      if (!Kernel32.INSTANCE.SetNamedPipeHandleState(this.R, new IntByReference(1), null, null)) {
         IOException var2 = r("nonblocking mode");
         this.close();
         throw var2;
      }
   }

   @Override
   public synchronized int read(ByteBuffer var1) throws IOException {
      if (!this.V) {
         return -1;
      }

      if (!var1.hasRemaining()) {
         return 0;
      }

      if (!Kernel32.INSTANCE.PeekNamedPipe(this.R, null, 0, null, this.e, null)) {
         throw r("peek");
      }

      int var2 = Math.min(var1.remaining(), this.e.getValue());
      if (var2 == 0) {
         return 0;
      }

      byte[] var3 = new byte[var2];
      if (!Kernel32.INSTANCE.ReadFile(this.R, var3, var2, this.r, null)) {
         throw r("read");
      }

      var1.put(var3, 0, this.r.getValue());
      return this.r.getValue();
   }

   @Override
   public synchronized int write(ByteBuffer var1) throws IOException {
      if (!this.V) {
         throw new IOException("Discord pipe closed");
      }

      byte[] var2 = new byte[var1.remaining()];
      var1.duplicate().get(var2);
      if (!Kernel32.INSTANCE.WriteFile(this.R, var2, var2.length, this.r, null)) {
         throw r("write");
      }

      var1.position(var1.position() + this.r.getValue());
      return this.r.getValue();
   }

   @Override
   public boolean isOpen() {
      return this.V;
   }

   @Override
   public synchronized void close() throws IOException {
      if (this.V) {
         this.V = (boolean)0;
         if (!Kernel32.INSTANCE.CloseHandle(this.R)) {
            throw r("close");
         }
      }
   }

   private static IOException r(String var0) {
      return new IOException("Discord pipe " + var0 + " failed (Win32 " + Kernel32.INSTANCE.GetLastError() + ")");
   }
}
