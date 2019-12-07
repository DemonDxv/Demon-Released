package dev.demon;

import dev.demon.base.CheckManager;
import dev.demon.base.processors.OptifineProcessor;
import dev.demon.base.user.User;
import dev.demon.base.user.UserManager;
import dev.demon.commands.CommandManager;
import dev.demon.events.BitDefenderListener;
import dev.demon.events.EventManager;
import dev.demon.listeners.GeneralListeners;
import dev.demon.listeners.PacketListener;
import dev.demon.tinyprotocol.api.TinyProtocolHandler;
import dev.demon.tinyprotocol.api.packets.reflections.Reflections;
import dev.demon.tinyprotocol.api.packets.reflections.types.WrappedField;
import dev.demon.tinyprotocol.packet.out.WrappedOutTransaction;
import dev.demon.utils.BlockUtil;
import dev.demon.utils.TimeUtils;
import dev.demon.utils.blockbox.BlockBoxManager;
import dev.demon.utils.blockbox.impl.BoundingBoxes;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;

/**
 * Created on 30/10/2019 Package cc.flycode.bitdefender
 */

public class Demon extends JavaPlugin {
    @Getter
    private static Demon instance;

    @Getter
    private UserManager userManager;

    @Getter
    private BlockBoxManager blockBoxManager;

    @Getter
    private BoundingBoxes boxes;

    @Getter
    private String bukkitVerbose, pluginVersion;

    @Getter
    private TinyProtocolHandler tinyProtocolHandler;

    @Getter
    private ScheduledExecutorService executorService, processorService;

    @Getter
    private EventManager eventManager;

    @Getter
    private Map<UUID, List<Entity>> entities = new ConcurrentHashMap<>();

    @Getter
    private CheckManager checkManager;

    @Getter
    private List<BitDefenderListener> demonListeners = Collections.synchronizedList(new ArrayList<>());

    @Getter
    private List<UUID> banQueue = new CopyOnWriteArrayList<>();

    @Getter
    private Version version;

    @Getter
    private CommandManager commandManager;

    FileConfiguration config;
    File cfile;

    public static String banMessage, banCommand, alertsMessage, alertsDev;
    public static Boolean banEnabled, alertsEnabled;

    @Override
    public void onEnable() {
        instance = this;
        bukkitVerbose = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        pluginVersion = getDescription().getVersion();
        if (bukkitVerbose.contains("1_8")) version = Version.V1_8;
        if (bukkitVerbose.contains("1_7")) version = Version.V1_7;
        userManager = new UserManager();
        new BlockUtil();
        this.blockBoxManager = new BlockBoxManager();
        this.boxes = new BoundingBoxes();
        tinyProtocolHandler = new TinyProtocolHandler();
        executorService = Executors.newSingleThreadScheduledExecutor();
        processorService = Executors.newSingleThreadScheduledExecutor();
        eventManager = new EventManager();
        Bukkit.getOnlinePlayers().forEach(player -> TinyProtocolHandler.getInstance().addChannel(player));
        loadListeners();
        config = getConfig();
        cfile = new File(getDataFolder(), "config.yml");
        saveDefaultConfig();
        loadConfig();
      /*  RunUtils.taskTimer(() -> {
            for (World world : Bukkit.getWorlds()) {
                Object vWorld = CraftReflection.getVanillaWorld(world);

                List<Object> vEntities = Collections.synchronizedList(entityList.get(vWorld));

                List<Entity> bukkitEntities = vEntities
                        .parallelStream()
                        .map(CraftReflection::getBukkitEntity)
                        .collect(Collectors.toList());

                entities.put(world.getUID(), bukkitEntities);
            }
        }, 40L, 2L);*/
        checkManager = new CheckManager();
        new OptifineProcessor();
        commandManager = new CommandManager();
        executorService.scheduleAtFixedRate(() -> userManager.getUsers().stream().filter(user -> TimeUtils.secondsFromLong(user.lastJoin) > 3L).forEach(user -> {
            WrappedOutTransaction transaction = new WrappedOutTransaction(0, (short) 421, false);
            TinyProtocolHandler.sendPacket(user.getPlayer(), transaction.getObject());
        }), 500L, 500L, TimeUnit.MILLISECONDS);
    }

    @Override
    public void onDisable() {
        checkManager.unRegisterAll();
        Bukkit.getOnlinePlayers().forEach(player -> {
            userManager.removeUser(new User(player));
            TinyProtocolHandler.getInstance().removeChannel(player);
        });
        processorService.shutdownNow();
        executorService.shutdownNow();
        demonListeners.parallelStream().forEach(demonListeners -> eventManager.unregisterListener(demonListeners));
    }

    private void loadConfig() {
        //Bans
        banMessage = Demon.instance.getConfig().getString("Bans.message");
        banCommand = Demon.instance.getConfig().getString("Bans.command");
        banEnabled = Demon.instance.getConfig().getBoolean("Bans.enabled");

        //Alerts
        alertsEnabled = Demon.instance.getConfig().getBoolean("Alerts.enabled");
        alertsMessage= Demon.instance.getConfig().getString("Alerts.message");
        alertsDev = Demon.instance.getConfig().getString("Alerts.dev-message");

    }

    private void loadListeners() {
        getServer().getPluginManager().registerEvents(new GeneralListeners(), this);
        demonListeners.add(new PacketListener());

        demonListeners.parallelStream().forEach(demonListeners -> eventManager.registerListeners(demonListeners, this));
    }

    private static WrappedField entityList = Reflections.getNMSClass("World").getFieldByName("entityList");

    public enum Version {
        V1_8,
        V1_7
    }
}
