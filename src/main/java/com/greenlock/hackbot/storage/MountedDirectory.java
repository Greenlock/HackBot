package com.greenlock.hackbot.storage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LukeSmalley on 12/23/2016.
 */
public class MountedDirectory implements FileContainer {

    private FileContainer base;
    private Map<String, FileContainer> mountedChildren = new HashMap<>();

    public MountedDirectory(FileContainer base) {
        this.base = base;
    }

    public void mount(String name, FileContainer child) {
        mountedChildren.put(name, child);
    }

    @Override
    public void delete() throws IOException {
        for (String key : mountedChildren.keySet()) {
            mountedChildren.get(key).delete();
        }
        base.delete();
    }

    @Override
    public boolean hasChild(String name) {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            return mountedChildren.containsKey(child) ? mountedChildren.get(child).hasChild(remainingPath) : base.hasChild(name);
        } else {
            return mountedChildren.containsKey(name) || base.hasChild(name);
        }
    }

    @Override
    public FileContainer getChild(String name) {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            return mountedChildren.containsKey(child) ? mountedChildren.get(child).getChild(remainingPath) : base.getChild(name);
        } else {
            return mountedChildren.containsKey(name) ? mountedChildren.get(name) : base.getChild(name);
        }
    }

    @Override
    public FileContainer createChild(String name) throws IOException {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            return mountedChildren.containsKey(child) ? mountedChildren.get(child).createChild(remainingPath) : base.createChild(name);
        } else {
            return base.createChild(name);
        }
    }

    @Override
    public boolean hasFile(String name) {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            return mountedChildren.containsKey(child) ? mountedChildren.get(child).hasFile(remainingPath) : base.hasFile(name);
        } else {
            return base.hasFile(name);
        }
    }

    @Override
    public String getFile(String name) throws IOException {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            return mountedChildren.containsKey(child) ? mountedChildren.get(child).getFile(remainingPath) : base.getFile(name);
        } else {
            return base.getFile(name);
        }
    }

    @Override
    public String createFile(String name, String defaultValue) throws IOException {
        return hasFile(name) ? getFile(name) : base.createFile(name, defaultValue); //TODO: WRONG
    }

    @Override
    public void deleteFile(String name) throws IOException {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            if (mountedChildren.containsKey(child)) {
                mountedChildren.get(child).deleteFile(remainingPath);
            } else {
                base.deleteFile(name);
            }
        } else {
            base.deleteFile(name);
        }
    }

    @Override
    public void writeFile(String name, String value) throws IOException {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            if (mountedChildren.containsKey(child)) {
                mountedChildren.get(child).writeFile(remainingPath, value);
            } else {
                base.writeFile(name, value);
            }
        } else {
            base.writeFile(name, value);
        }
    }

    @Override
    public void appendFile(String name, String value) throws IOException {
        if (name.contains("/")) {
            String child = name.split("/")[0];
            String remainingPath = name.substring(child.length() + 1);
            if (mountedChildren.containsKey(child)) {
                mountedChildren.get(child).appendFile(remainingPath, value);
            } else {
                base.appendFile(name, value);
            }
        } else {
            base.appendFile(name, value);
        }
    }
}
