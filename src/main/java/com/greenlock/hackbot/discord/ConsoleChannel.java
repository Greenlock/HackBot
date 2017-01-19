package com.greenlock.hackbot.discord;

import com.greenlock.hackbot.QuickLog;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MessageList;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

/**
 * Created by LukeSmalley on 12/24/2016.
 */
public class ConsoleChannel implements IChannel, IPrivateChannel {

    private static ConsoleChannel instance = null;

    public static ConsoleChannel get(IDiscordClient client) {
        if (instance == null) {
            instance = new ConsoleChannel(client);
        }
        return instance;
    }

    private IDiscordClient client;

    private ConsoleChannel(IDiscordClient client) {
        this.client = client;
    }

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public MessageList getMessages() {
        return null;
    }

    @Override
    public IMessage getMessageByID(String s) {
        return null;
    }

    @Override
    public IGuild getGuild() {
        return null;
    }

    @Override
    public boolean isPrivate() {
        return true;
    }

    @Override
    public String getTopic() {
        return "The most important things";
    }

    @Override
    public String mention() {
        return "";
    }

    @Override
    public IMessage sendMessage(String s) throws MissingPermissionsException, RateLimitException, DiscordException {
        QuickLog.info(s);
        return new ConsoleMessage(s, client);
    }

    @Override
    public IMessage sendMessage(String s, boolean b) throws MissingPermissionsException, RateLimitException, DiscordException {
        QuickLog.info(s);
        return new ConsoleMessage(s, client);
    }

    @Override
    public IMessage sendFile(File file, String s) throws IOException, MissingPermissionsException, RateLimitException, DiscordException {
        throw new DiscordException("Files cannot be sent to the console.");
    }

    @Override
    public IMessage sendFile(File file) throws IOException, MissingPermissionsException, RateLimitException, DiscordException {
        throw new DiscordException("Files cannot be sent to the console.");
    }

    @Override
    public IMessage sendFile(InputStream inputStream, String s, String s1) throws IOException, MissingPermissionsException, RateLimitException, DiscordException {
        throw new DiscordException("Files cannot be sent to the console.");
    }

    @Override
    public IMessage sendFile(InputStream inputStream, String s) throws IOException, MissingPermissionsException, RateLimitException, DiscordException {
        throw new DiscordException("Files cannot be sent to the console.");
    }

    @Override
    public IInvite createInvite(int i, int i1, boolean b) throws MissingPermissionsException, RateLimitException, DiscordException {
        throw new DiscordException("Users cannot be invited to the console.");
    }

    @Override
    public void toggleTypingStatus() {
        //Do Nothing.
    }

    @Override
    public void setTypingStatus(boolean b) {
        //Do Nothing.
    }

    @Override
    public boolean getTypingStatus() {
        return false;
    }

    @Override
    public void changeName(String s) throws RateLimitException, DiscordException, MissingPermissionsException {
        throw new DiscordException("The console cannot be renamed.");
    }

    @Override
    public void changePosition(int i) throws RateLimitException, DiscordException, MissingPermissionsException {
        //Do Nothing.
    }

    @Override
    public void changeTopic(String s) throws RateLimitException, DiscordException, MissingPermissionsException {
        throw new DiscordException("The console topic cannot be changed.");
    }

    @Override
    public int getPosition() {
        return 0;
    }

    @Override
    public void delete() throws MissingPermissionsException, RateLimitException, DiscordException {
        throw new DiscordException("The console cannot be deleted.");
    }

    @Override
    public Map<String, PermissionOverride> getUserOverrides() {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, PermissionOverride> getRoleOverrides() {
        return Collections.emptyMap();
    }

    @Override
    public EnumSet<Permissions> getModifiedPermissions(IUser iUser) {
        return EnumSet.noneOf(Permissions.class);
    }

    @Override
    public EnumSet<Permissions> getModifiedPermissions(IRole iRole) {
        return EnumSet.noneOf(Permissions.class);
    }

    @Override
    public void removePermissionsOverride(IUser iUser) throws MissingPermissionsException, RateLimitException, DiscordException {
        //Do Nothing.
    }

    @Override
    public void removePermissionsOverride(IRole iRole) throws MissingPermissionsException, RateLimitException, DiscordException {
        //Do Nothing.
    }

    @Override
    public void overrideRolePermissions(IRole iRole, EnumSet<Permissions> enumSet, EnumSet<Permissions> enumSet1) throws MissingPermissionsException, RateLimitException, DiscordException {
        //Do Nothing.
    }

    @Override
    public void overrideUserPermissions(IUser iUser, EnumSet<Permissions> enumSet, EnumSet<Permissions> enumSet1) throws MissingPermissionsException, RateLimitException, DiscordException {
        //Do Nothing.
    }

    @Override
    public List<IInvite> getInvites() throws DiscordException, RateLimitException, MissingPermissionsException {
        return Collections.emptyList();
    }

    @Override
    public List<IUser> getUsersHere() {
        return Collections.singletonList(ConsoleUser.get(client));
    }

    @Override
    public List<IMessage> getPinnedMessages() throws RateLimitException, DiscordException {
        return Collections.emptyList();
    }

    @Override
    public void pin(IMessage iMessage) throws RateLimitException, DiscordException, MissingPermissionsException {
        //Do Nothing.
    }

    @Override
    public void unpin(IMessage iMessage) throws RateLimitException, DiscordException, MissingPermissionsException {
        //Do Nothing.
    }

    @Override
    public String getID() {
        return "Console";
    }

    @Override
    public IDiscordClient getClient() {
        return client;
    }

    @Override
    public IUser getRecipient() {
        return ConsoleUser.get(client);
    }

    @Override
    public IPrivateChannel copy() {
        return ConsoleChannel.get(client);
    }
}
