package com.netflared;

import com.mojang.blaze3d.platform.InputConstants;
import com.netflared.config.NetflaredConfig;
import com.netflared.gui.NetflaredSettingsScreen;
import com.netflared.tunnel.TunnelManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.client.event.ClientTickEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Mod(NetflaredMod.MOD_ID)
public class NetflaredMod {
    public static final String MOD_ID="netflared";
    public static final Logger LOGGER=LoggerFactory.getLogger(MOD_ID);
    private static NetflaredMod INSTANCE;
    private static NetflaredConfig config;
    private static TunnelManager tunnelManager;
    private static KeyMapping openTunnelKey;
    public static NetflaredMod getInstance(){return INSTANCE;}
    public static NetflaredConfig getConfig(){return config;}
    public static TunnelManager getTunnelManager(){return tunnelManager;}

    public static final class Translations {
        private static final String LANG_BRANCH="https://raw.githubusercontent.com/matejpcs/netflared/lang/src/main/resources/assets/netflared/lang/";
        private static final Map<String,String> values=new HashMap<>();
        private static volatile String loadedLocale;
        private static volatile String requestedLocale;
        private static final String[][] defaults={
            {"netflared.settings.title","Netflared Settings"},{"netflared.settings.name","Name"},{"netflared.settings.domain","Tunnel Domain"},{"netflared.settings.port","Local Port"},{"netflared.settings.add","+ Add Server"},{"netflared.settings.save","Save"},{"netflared.settings.saved","Settings saved"},{"netflared.settings.save_failed","Could not save settings"},{"netflared.settings.cancel","Cancel"},{"netflared.settings.back","Back"},
            {"netflared.status.title","Netflared — %s"},{"netflared.status.working","Connecting..."},{"netflared.status.downloading","Downloading cloudflared..."},{"netflared.status.connecting","Establishing tunnel..."},{"netflared.status.ready","Tunnel ready"},{"netflared.status.connected","Connection established"},{"netflared.status.exited","cloudflared stopped unexpectedly"},{"netflared.status.failed","Connection failed"},{"netflared.status.error_detail","Details: %s"},{"netflared.status.local","Local endpoint: %s"},{"netflared.status.domain","Cloudflare host: %s"},{"netflared.status.join","Join"},{"netflared.status.back","Back"},{"netflared.status.cancel","Cancel"},{"netflared.status.idle","Idle"},{"netflared.status.connect","Connect"},{"netflared.status.disconnect","Disconnect"},
            {"netflared.debug.button","Debug"},{"netflared.debug.title","Netflared Debug Tools"},{"netflared.debug.subtitle","Internal UI testing — no tunnel is started"},{"netflared.debug.working","Test Connecting"},{"netflared.debug.success","Test Connected"},{"netflared.debug.error","Test Error"},{"netflared.debug.settings","Test Settings"},{"netflared.debug.working_message","Waiting for tunnel..."},{"netflared.debug.success_message","Debug state: tunnel ready"},{"netflared.debug.error_message","Debug state: simulated connection failure"}
        };
        static{for(String[] e:defaults)values.put(e[0],e[1]);}
        public static synchronized void refresh(){
            String locale=currentLocale(); if(locale.equals(requestedLocale))return; requestedLocale=locale; final String selected=locale;
            Thread t=new Thread(()->{try{
                Path dir=FMLPaths.CONFIGDIR.get().resolve(MOD_ID).resolve("lang"); Files.createDirectories(dir);
                Path sf=dir.resolve(selected+".json"), ef=dir.resolve("en_us.json");
                loadFile(ef); if(!selected.equals("en_us"))loadFile(sf);
                downloadIfChanged(sf,selected); if(!selected.equals("en_us"))downloadIfChanged(ef,"en_us");
                loadFile(ef); if(!selected.equals("en_us"))loadFile(sf); loadedLocale=selected;
                LOGGER.info("[Netflared] Loaded dynamic language {}",selected);
            }catch(Exception e){LOGGER.warn("[Netflared] Dynamic language sync failed",e);}}, "netflared-language-sync");
            t.setDaemon(true); t.start();
        }
        public static void refreshIfChanged(){String l=currentLocale();if(!l.equals(loadedLocale)&&!l.equals(requestedLocale))refresh();}
        private static String currentLocale(){try{return Minecraft.getInstance().getLanguageManager().getSelected();}catch(Throwable ignored){return "en_us";}}
        public static String text(String key,Object...args){String v=values.getOrDefault(key,key);try{return args.length>0?String.format(v,args):v;}catch(Exception e){return v;}}
        private static void downloadIfChanged(Path target,String locale)throws Exception{
            HttpURLConnection c=(HttpURLConnection)URI.create(LANG_BRANCH+locale+".json").toURL().openConnection(); c.setConnectTimeout(5000);c.setReadTimeout(10000);c.setRequestProperty("User-Agent","Netflared/"+MOD_ID);c.setRequestProperty("Accept","application/json");
            int st=c.getResponseCode();if(st<200||st>=300){c.disconnect();return;}byte[] d=c.getInputStream().readAllBytes();c.disconnect();if(d.length==0)return;
            Path tmp=target.resolveSibling(target.getFileName()+".part");Files.write(tmp,d);try{Files.move(tmp,target,java.nio.file.StandardCopyOption.REPLACE_EXISTING,java.nio.file.StandardCopyOption.ATOMIC_MOVE);}catch(java.nio.file.AtomicMoveNotSupportedException e){Files.move(tmp,target,java.nio.file.StandardCopyOption.REPLACE_EXISTING);}
        }
        private static synchronized void loadFile(Path f){if(!Files.isRegularFile(f))return;try{JsonObject o=JsonParser.parseString(Files.readString(f,StandardCharsets.UTF_8)).getAsJsonObject();for(Map.Entry<String,JsonElement> e:o.entrySet())if(e.getValue().isJsonPrimitive())values.put(e.getKey(),e.getValue().getAsString());}catch(Exception e){LOGGER.warn("[Netflared] Invalid language file {}",f,e);}}
    }
    public static void refreshTranslations(){Translations.refreshIfChanged();}
    public static net.minecraft.network.chat.Component tr(String key,Object...args){return net.minecraft.network.chat.Component.literal(Translations.text(key,args));}

    public NetflaredMod(FMLJavaModLoadingContext context){
        IEventBus modBus=context.getModEventBus();
        INSTANCE=this;Path dir=FMLPaths.CONFIGDIR.get().resolve(MOD_ID);config=NetflaredConfig.load(dir);tunnelManager=new TunnelManager(dir);tunnelManager.killOrphanedTunnels();Translations.refresh();
        Runtime.getRuntime().addShutdownHook(new Thread(()->{try{if(tunnelManager!=null)tunnelManager.forceStopAll();}catch(Throwable ignored){}}, "netflared-shutdown"));
        modBus.addListener(NetflaredMod::registerKeyMappings);MinecraftForge.EVENT_BUS.register(NetflaredMod.class);
    }
    private static void registerKeyMappings(RegisterKeyMappingsEvent e){openTunnelKey=new KeyMapping("key.netflared.open_tunnel_ui",InputConstants.Type.KEYSYM,GLFW.GLFW_KEY_F9,"key.categories.netflared");e.register(openTunnelKey);}
    @SubscribeEvent public static void onClientTick(ClientTickEvent.Post e){Minecraft c=Minecraft.getInstance();Translations.refreshIfChanged();if(openTunnelKey==null)return;while(openTunnelKey.consumeClick()){Screen s=c.screen;if(s instanceof TitleScreen||s instanceof JoinMultiplayerScreen)c.setScreen(new NetflaredSettingsScreen(s));}}
    @SubscribeEvent public static void onScreenInit(ScreenEvent.Init.Post e){if(!(e.getScreen() instanceof JoinMultiplayerScreen s))return;e.addListener(net.minecraft.client.gui.components.Button.builder(tr("netflared.button.label"),b->Minecraft.getInstance().setScreen(new NetflaredSettingsScreen(s))).bounds(5,5,80,20).build());}
}