package me.mcofficer.minecraftrpc;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

import javax.annotation.Nullable;

public class RPCClient {

    public static final String CLIENT_ID = "384232743162216448";

    private static Thread callbackRunner;

    public synchronized void init()
    {
        DiscordEventHandlers handlers = new DiscordEventHandlers();

        DiscordRPC.INSTANCE.Discord_Initialize(CLIENT_ID, handlers, true, null);

        if (callbackRunner == null)
        {
            callbackRunner = new Thread(() -> {
                while (!Thread.currentThread().isInterrupted()) {
                    DiscordRPC.INSTANCE.Discord_RunCallbacks();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ignored) {}
                }
            }, "RPC-Callback-Handler");

            callbackRunner.start();
        }

        System.out.println("[Minecraft-RPC:] RPCClient has been started.");

    }

    public void updatePresence(@Nullable String details)
    {
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.largeImageKey = "minecraft";
        presence.largeImageText = "Minecraft";
        presence.smallImageKey = "minecraft";
        presence.smallImageText = "Minecraft";
        if (details != null){
            presence.details = details;
            presence.startTimestamp = System.currentTimeMillis() / 1000;
        }

        DiscordRPC.INSTANCE.Discord_UpdatePresence(presence);
    }

}