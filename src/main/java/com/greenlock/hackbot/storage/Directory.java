package com.greenlock.hackbot.storage;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by LukeSmalley on 12/23/2016.
 */
public class Directory implements FileContainer {

    private File directory;
    private FileContainer parent = null;

    public Directory(File directory) {
        this.directory = directory;
    }

    public Directory(File directory, FileContainer parent) {
        this.directory = directory;
        this.parent = parent;
    }

    @Override
    public void delete() throws IOException {
        FileUtils.deleteDirectory(directory);
    }

    @Override
    public boolean hasChild(String name) {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            if (child.equals("..")) {
                return parent != null && parent.hasChild(remainingPath);
            } else {
                return new Directory(new File(directory, child)).hasChild(remainingPath);
            }
        } else {
            return name.equals("..") ? parent != null : new File(directory, name).isDirectory();
        }
    }

    @Override
    public FileContainer getChild(String name) {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            if (child.equals("..")) {
                return parent == null ? null : parent.getChild(remainingPath);
            } else {
                return new Directory(new File(directory, child), this).getChild(remainingPath);
            }
        } else {
            if (name.equals("..")) {
                return parent == null ? null : parent;
            } else {
                return new Directory(new File(directory, name), this);
            }
        }
    }

    @Override
    public FileContainer createChild(String name) throws IOException {
        if (hasChild(name)) {
            return getChild(name);
        }

        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            if (child.equals("..")) {
                return parent == null ? null : parent.createChild(remainingPath);
            } else {
                return new Directory(new File(directory, child), this).createChild(remainingPath);
            }
        } else {
            File newChild = new File(directory, name);
            if (!newChild.isDirectory() && !newChild.mkdirs() && !newChild.isDirectory()) {
                throw new IOException("Failed to create path towards '" + newChild.getName() + "'.");
            }
            return new Directory(newChild, this);
        }
    }

    @Override
    public boolean hasFile(String name) {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            if (child.equals("..")) {
                return parent != null && parent.hasChild(remainingPath);
            } else {
                return new Directory(new File(directory, child)).hasChild(remainingPath);
            }
        } else {
            return new File(directory, name).isFile();
        }
    }

    @Override
    public String getFile(String name) throws IOException {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            if (child.equals("..")) {
                return parent == null ? null : parent.getFile(remainingPath);
            } else {
                return new Directory(new File(directory, child), this).getFile(remainingPath);
            }
        } else {
            return name.equals("..") ? null : FileUtils.readFileToString(new File(directory, name), Charset.forName("UTF-8"));
        }
    }

    @Override
    public String createFile(String name, String defaultValue) throws IOException {
        if (hasFile(name)) {
            return getFile(name);
        }

        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            if (child.equals("..")) {
                return parent == null ? null : parent.createFile(remainingPath, defaultValue);
            } else {
                return new Directory(new File(directory, child), this).createFile(remainingPath, defaultValue);
            }
        } else {
            File member = new File(directory, name);
            if (member.isFile()) {
                return FileUtils.readFileToString(member, Charset.forName("UTF-8"));
            } else {
                FileUtils.writeStringToFile(member, defaultValue, Charset.forName("UTF-8"));
                return defaultValue;
            }
        }
    }

    @Override
    public void deleteFile(String name) throws IOException {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            if (child.equals("..")) {
                parent.deleteFile(remainingPath);
            } else {
                new Directory(new File(directory, child), this).deleteFile(remainingPath);
            }
        } else {
            if (!new File(directory, name).delete()) {
                throw new IOException("Failed to delete file '" + name + "'.");
            }
        }
    }

    @Override
    public void writeFile(String name, String value) throws IOException {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            if (child.equals("..")) {
                parent.writeFile(remainingPath, value);
            } else {
                new Directory(new File(directory, child), this).writeFile(remainingPath, value);
            }
        } else {
            FileUtils.writeStringToFile(new File(directory, name), value, Charset.forName("UTF-8"));
        }
    }

    @Override
    public void appendFile(String name, String value) throws IOException {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            if (child.equals("..")) {
                parent.appendFile(remainingPath, value);
            } else {
                new Directory(new File(directory, child), this).appendFile(remainingPath, value);
            }
        } else {
            FileUtils.writeStringToFile(new File(directory, name), value, Charset.forName("UTF-8"), true);
        }
    }
}
