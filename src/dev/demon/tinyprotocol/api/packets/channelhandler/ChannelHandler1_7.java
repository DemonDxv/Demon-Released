/*
 * Created by Justin Heflin on 4/19/18 8:21 PM
 * Copyright (c) 2018.
 *
 * Can be redistributed non commercially as long as credit is given to original copyright owner.
 *
 * last modified: 4/19/18 7:22 PM
 */
package dev.demon.tinyprotocol.api.packets.channelhandler;

import dev.demon.Demon;
import dev.demon.tinyprotocol.reflection.FieldAccessor;
import dev.demon.tinyprotocol.reflection.Reflection;
import dev.demon.utils.blockbox.ReflectionUtil;
import dev.demon.tinyprotocol.api.ProtocolVersion;
import dev.demon.tinyprotocol.api.packets.reflections.Reflections;
import net.minecraft.util.com.mojang.authlib.GameProfile;
import net.minecraft.util.io.netty.channel.Channel;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.WeakHashMap;

public class ChannelHandler1_7 extends ChannelHandlerAbstract {
    static final FieldAccessor<GameProfile> getGameProfile = Reflection.getField(PACKET_LOGIN_IN_START, GameProfile.class, 0);
    static final FieldAccessor<Integer> protocolId = Reflection.getField(PACKET_SET_PROTOCOL, int.class, 0);
    static final FieldAccessor<Enum> protocolType = Reflection.getField(PACKET_SET_PROTOCOL, Enum.class, 0);
    private Map<String, Channel> channelLookup = new WeakHashMap<>();
    private Map<Channel, Integer> protocolLookup = new WeakHashMap<>();

    @Override public void addChannel(Player player) {
        net.minecraft.util.io.netty.channel.Channel channel = getChannel(player);
        this.addChannelHandlerExecutor.execute(() -> {
            if (channel != null && channel.pipeline().get(this.playerKey) == null) {
                channel.pipeline().addBefore(this.handlerKey, this.playerKey, new ChannelHandler(player, this));
            }
        });
    }

    @Override public void removeChannel(Player player) {
        net.minecraft.util.io.netty.channel.Channel channel = getChannel(player);
        this.removeChannelHandlerExecutor.execute(() -> {
            if (channel != null && channel.pipeline().get(this.playerKey) != null) {
                channel.pipeline().remove(this.playerKey);
            }
        });
    }

    private net.minecraft.util.io.netty.channel.Channel getChannel(Player player) {
        return (net.minecraft.util.io.netty.channel.Channel) Reflections.getNMSClass("NetworkManager").getFirstFieldByType(net.minecraft.util.io.netty.channel.Channel.class).get(networkManagerField.get(playerConnectionField.get(ReflectionUtil.getEntityPlayer(player))));
    }

    public ProtocolVersion getProtocolVersion(Player player) {
        Channel channel = channelLookup.get(player.getName());

        // Lookup channel again
        if (channel == null) {

            channelLookup.put(player.getName(), getChannel(player));
        }

        return ProtocolVersion.getVersion(protocolLookup.getOrDefault(channel, -1));
    }

    private class ChannelHandler extends net.minecraft.util.io.netty.channel.ChannelDuplexHandler {
        private final Player player;
        private final ChannelHandlerAbstract channelHandlerAbstract;

        ChannelHandler(Player player, ChannelHandlerAbstract channelHandlerAbstract) {
            this.player = player;
            this.channelHandlerAbstract = channelHandlerAbstract;
        }

        @Override public void write(net.minecraft.util.io.netty.channel.ChannelHandlerContext ctx, Object msg, net.minecraft.util.io.netty.channel.ChannelPromise promise) throws Exception {
            Object packet = Demon.getInstance().getTinyProtocolHandler().onPacketOutAsync(player, msg);
            if (packet != null) {
                super.write(ctx, packet, promise);
            }
        }

        @Override public void channelRead(net.minecraft.util.io.netty.channel.ChannelHandlerContext ctx, Object msg) throws Exception {
            final Channel channel = ctx.channel();
            if (PACKET_LOGIN_IN_START.isInstance(msg)) {
                GameProfile profile = getGameProfile.get(msg);
                channelLookup.put(profile.getName(), channel);
            } else if (PACKET_SET_PROTOCOL.isInstance(msg)) {
                String protocol = protocolType.get(msg).name();
                if (protocol.equalsIgnoreCase("LOGIN")) {
                    protocolLookup.put(channel, protocolId.get(msg));
                }
            }
            Object packet = Demon.getInstance().getTinyProtocolHandler().onPacketInAsync(player, msg);
            if (packet != null) {
                super.channelRead(ctx, packet);
            }
        }
    }

    public void sendPacket(Player player, Object packet) {
        getChannel(player).pipeline().writeAndFlush(packet);
    }
}
