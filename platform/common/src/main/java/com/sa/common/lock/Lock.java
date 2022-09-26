package com.sa.common.lock;

import java.io.Closeable;
import java.io.IOException;

public interface Lock extends Closeable {

    boolean tryLock();

    void unlock();
    String getkey();
    @Override
    void close() throws IOException;
}
