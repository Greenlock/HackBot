package com.greenlock.hackbot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

/**
 * Created by LukeSmalley on 10/15/2016.
 */
public class QuickGson {

    public static String serializeToJson(Object o) {
        return new GsonBuilder().serializeNulls().disableHtmlEscaping().create().toJson(o);
    }

    public static String serializeToPrettyJson(Object o) {
        return new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create().toJson(o);
    }

    public static <T> T deserializeFromJson(String j, Class<T> c) {
        return new Gson().fromJson(j, c);
    }

    public static <T> T deserializeFromJson(String j, Type t) {
        return new Gson().fromJson(j, t);
    }

    public static void serializeToJsonFile(File f, Object o) throws IOException {
        FileUtils.writeStringToFile(f, new GsonBuilder().disableHtmlEscaping().serializeNulls().create().toJson(o), Charset.forName("UTF-8"));
    }

    public static void serializeToPrettyJsonFile(File f, Object o) throws IOException {
        FileUtils.writeStringToFile(f, new GsonBuilder().disableHtmlEscaping().serializeNulls().setPrettyPrinting().create().toJson(o), Charset.forName("UTF-8"));
    }

    public static <T> T deserializeFromJsonFile(File f, Class<T> c) throws IOException {
        return new Gson().fromJson(FileUtils.readFileToString(f, Charset.forName("UTF-8")), c);
    }

    public static <T> T deserializeFromJsonFile(File f, Type t) throws IOException {
        return new Gson().fromJson(FileUtils.readFileToString(f, Charset.forName("UTF-8")), t);
    }
}
