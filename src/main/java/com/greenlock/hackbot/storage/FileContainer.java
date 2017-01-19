package com.greenlock.hackbot.storage;

import java.io.IOException;

/**
 * Created by LukeSmalley on 12/23/2016.
 */
public interface FileContainer {

    void delete() throws IOException;
    boolean hasChild(String name);
    FileContainer getChild(String name);
    FileContainer createChild(String name) throws IOException;
    boolean hasFile(String name);
    String getFile(String name) throws IOException;
    String createFile(String name, String defaultValue) throws IOException;
    void deleteFile(String name) throws IOException;
    void writeFile(String name, String value) throws IOException;
    void appendFile(String name, String value) throws IOException;
}
