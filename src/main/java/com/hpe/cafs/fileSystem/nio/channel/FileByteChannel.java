/*
 * ******************************************************************************
 *  * © Copyright ${year}, Hewlett-Packard Development Company, L.P.
 *  *****************************************************************************
 */

package com.hpe.cafs.fileSystem.nio.channel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.util.ArrayList;
import java.util.List;

import com.hpe.caf.storage.sdk.StorageClient;
import com.hpe.cafs.fileSystem.nio.CafsFile;
import com.hpe.cafs.fileSystem.nio.CafsProvider;

/**
 * Created by dev on 3/22/16.
 */
public class FileByteChannel implements SeekableByteChannel{

    private List<Byte> byteArray;
    private long position;
    private boolean isOpen;
    private boolean isWrite = false;

    private String parentId;
    private String fileName;



    public FileByteChannel(String parentId, String fileName) {
        byteArray = new ArrayList<Byte>(1024);
        position = 0;
        isOpen = true;

        this.parentId = parentId;
        this.fileName = fileName;
    }

    public FileByteChannel(String parentId, String fileName, List<Byte> bytes) {
        this(parentId, fileName);
        byteArray = bytes;
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        if (dst == null) {
            throw new IllegalArgumentException("Source buffer is null");
        }
        if (size() == position) {
            return -1;
        }

        int totalBytes = dst.remaining();
        totalBytes = (int)Math.min(totalBytes, size() - position);
        for (int i = 0; i < totalBytes; i++) {
            dst.put(byteArray.get((int) position++));
        }

        return totalBytes;
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        if (src == null) {
            throw new IllegalArgumentException("Source buffer is null");
        }

        final int totalBytes = src.remaining();
        if (totalBytes > 0) {
            isWrite = true;
        }
        for (int i = 0; i < totalBytes; i++) {
            byteArray.add((int)position++, src.get());
        }
        return totalBytes;
    }

    @Override
    public long position() throws IOException {
        return position;
    }

    @Override
    public SeekableByteChannel position(long newPosition) throws IOException {
        if (newPosition < 0 || newPosition >= byteArray.size() ) {
            throw new IOException("Position out of bound!");
        }
        position = newPosition;
        return this;
    }

    @Override
    public long size() throws IOException {
        return byteArray.size();
    }

    @Override
    public SeekableByteChannel truncate(long size) throws IOException {
        throw new IOException("not support this operation");
        // return null;
    }

    @Override
    public boolean isOpen() {
        return isOpen;
    }

    @Override
    public void close() throws IOException {
        if (!isOpen) return;
        isOpen = false;
        if (isWrite) {
            byte[] input = new byte[(int)size()];
            for (int i = 0; i < size(); i++) {
                input[i] = byteArray.get(i);
            }
            try {
                CafsProvider.createFile(fileName, new ByteArrayInputStream(input), size(), parentId, CafsFile.TYPE_FILE);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
