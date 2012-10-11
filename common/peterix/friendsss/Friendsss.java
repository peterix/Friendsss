package peterix.friendsss;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid="Friendsss", name="Friendsss", version="0.0.1")
@NetworkMod(clientSideRequired=false, serverSideRequired=true)
public class Friendsss {

    // The instance of your mod that Forge uses.
	@Instance("Friendsss")
	public static Friendsss instance;
	
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="peterix.friendsss.client.ClientProxy", serverSide="peterix.friendsss.CommonProxy")
	public static CommonProxy proxy;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
	}
	
	@Init
	public void load(FMLInitializationEvent event) {
	}
	
	@PostInit
	public void postInit(FMLPostInitializationEvent event) {
		proxy.nerfEndermen();
	}
}