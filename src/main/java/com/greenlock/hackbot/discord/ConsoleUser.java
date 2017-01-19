package com.greenlock.hackbot.discord;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by LukeSmalley on 12/27/2016.
 */
public class ConsoleUser implements IUser {

    private static ConsoleUser instance = null;

    public static ConsoleUser get(IDiscordClient client) {
        if (instance == null) {
            instance = new ConsoleUser(client);
        }
        return instance;
    }

    private IDiscordClient client;

    private ConsoleUser(IDiscordClient client) {
        this.client = client;
    }

    @Override
    public String getName() {
        return "Console";
    }

    @Override
    public Status getStatus() {
        return Status.empty();
    }

    @Override
    public String getAvatar() {
        return null;
    }

    @Override
    public String getAvatarURL() {
        return null;
    }

    @Override
    public Presences getPresence() {
        return Presences.ONLINE;
    }

    @Override
    public String getDisplayName(IGuild iGuild) {
        return "Console";
    }

    @Override
    public String mention() {
        return "";
    }

    @Override
    public String mention(boolean b) {
        return "";
    }

    @Override
    public String getDiscriminator() {
        return "0000";
    }

    @Override
    public List<IRole> getRolesForGuild(IGuild iGuild) {
        return Collections.emptyList();
    }

    @Override
    public Optional<String> getNicknameForGuild(IGuild iGuild) {
        return Optional.empty();
    }

    @Override
    public boolean isBot() {
        return false;
    }

    @Override
    public void moveToVoiceChannel(IVoiceChannel iVoiceChannel) throws DiscordException, RateLimitException, MissingPermissionsException {
        //Do Nothing.
    }

    @Override
    public List<IVoiceChannel> getConnectedVoiceChannels() {
        return Collections.emptyList();
    }

    @Override
    public IPrivateChannel getOrCreatePMChannel() throws RateLimitException, DiscordException {
        return ConsoleChannel.get(client);
    }

    @Override
    public boolean isDeaf(IGuild iGuild) {
        return false;
    }

    @Override
    public boolean isMuted(IGuild iGuild) {
        return false;
    }

    @Override
    public boolean isDeafLocally() {
        return false;
    }

    @Override
    public boolean isMutedLocally() {
        return false;
    }

    @Override
    public void addRole(IRole iRole) throws MissingPermissionsException, RateLimitException, DiscordException {
        //Do Nothing.
    }

    @Override
    public void removeRole(IRole iRole) throws MissingPermissionsException, RateLimitException, DiscordException {
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
    public IUser copy() {
        return ConsoleUser.get(client);
    }
}
