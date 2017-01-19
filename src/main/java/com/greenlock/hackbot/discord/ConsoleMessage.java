package com.greenlock.hackbot.discord;

import com.greenlock.hackbot.QuickLog;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.MissingPermissionsException;
import sx.blah.discord.util.RateLimitException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by LukeSmalley on 12/27/2016.
 */
public class ConsoleMessage implements IMessage {

    private String message;
    private LocalDateTime timestamp;
    private IDiscordClient client;

    public ConsoleMessage(String message, IDiscordClient client) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.client = client;
    }

    @Override
    public String getContent() {
        return message;
    }

    @Override
    public IChannel getChannel() {
        return ConsoleChannel.get(client);
    }

    @Override
    public IUser getAuthor() {
        return ConsoleUser.get(client);
    }

    @Override
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public List<IUser> getMentions() {
        return Collections.emptyList();
    }

    @Override
    public List<IRole> getRoleMentions() {
        return Collections.emptyList();
    }

    @Override
    public List<IChannel> getChannelMentions() {
        return Collections.emptyList();
    }

    @Override
    public List<Attachment> getAttachments() {
        return Collections.emptyList();
    }

    @Override
    public List<IEmbedded> getEmbedded() {
        return Collections.emptyList();
    }

    @Override
    public void reply(String s) throws MissingPermissionsException, RateLimitException, DiscordException {
        QuickLog.info("@Console " + s);
    }

    @Override
    public IMessage edit(String s) throws MissingPermissionsException, RateLimitException, DiscordException {
        return new ConsoleMessage(s, client);
    }

    @Override
    public boolean mentionsEveryone() {
        return false;
    }

    @Override
    public void delete() throws MissingPermissionsException, RateLimitException, DiscordException {
        //Do Nothing.
    }

    @Override
    public Optional<LocalDateTime> getEditedTimestamp() {
        return Optional.empty();
    }

    @Override
    public boolean isPinned() {
        return false;
    }

    @Override
    public IGuild getGuild() {
        return null;
    }

    @Override
    public String getID() {
        return Long.toString(System.currentTimeMillis());
    }

    @Override
    public IDiscordClient getClient() {
        return client;
    }

    @Override
    public IMessage copy() {
        return new ConsoleMessage(message, client);
    }
}
