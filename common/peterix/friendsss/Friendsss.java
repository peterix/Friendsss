package peterix.friendsss;

import java.lang.reflect.Field;
import java.util.logging.Level;

import net.minecraft.src.Block;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLLog;
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

	boolean NERF_CREEPERS;
	boolean NERF_ENDERMEN;
	boolean NERF_GHASTS;
	
    // The instance of your mod that Forge uses.
	@Instance("Friendsss")
	public static Friendsss instance;
	
	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="peterix.friendsss.client.ClientProxy", serverSide="peterix.friendsss.CommonProxy")
	public static CommonProxy proxy;
	
	@PreInit
	public void preInit(FMLPreInitializationEvent event) {
		
		Configuration cfg = new Configuration(event.getSuggestedConfigurationFile());
		try {
			cfg.load();
			NERF_CREEPERS = cfg.get(Configuration.CATEGORY_GENERAL, "nerfCreepers", true).getBoolean(true);
			NERF_ENDERMEN = cfg.get(Configuration.CATEGORY_GENERAL, "nerfEndermen", true).getBoolean(true);
			NERF_GHASTS = cfg.get(Configuration.CATEGORY_GENERAL, "nerfGhasts", true).getBoolean(true);
		} catch (Exception e) {
			FMLLog.log(Level.SEVERE, e, "Friendsss failed to load config file.");
		} finally {
			cfg.save();
		}
		
		if(NERF_ENDERMEN)
		{
			// get enderman class
		    Class endermanClass = net.minecraft.src.EntityEnderman.class;
		    // get the carriableBlocks field
		    try {
				// steal only flowers. flowers are pretty :D 
				boolean[] carriableBlocks = new boolean[4096];
		        carriableBlocks[Block.plantYellow.blockID] = true;
		        carriableBlocks[Block.plantRed.blockID] = true;
		        
		        //TODO: This needs to be fixed when MC obfuscation changes 
		        Field carriableBlocksField = endermanClass.getField("d");
		        carriableBlocksField.set(null, carriableBlocks);
		        FMLLog.log(Level.INFO, "Sucessfully nerfed endermen.");
				
			} catch (Exception e) {
				FMLLog.log(Level.WARNING, "Endermen nerf failed:");
				e.printStackTrace();
			}
		}
		if(NERF_CREEPERS)
		{
			// nerf them green guys
		    try {
		    	// unprotect the variable
		        MinecraftForge.EVENT_BUS.register(new CreeperUpdateEvent());
		        FMLLog.log(Level.INFO, "Sucessfully nerfed creepers.");
				
			} catch (Exception e) {
				FMLLog.log(Level.WARNING, "Creeper nerf failed:");
				e.printStackTrace();
			}
		}
		if(NERF_GHASTS)
		{
			// nerf ghasts
		    try {
		    	// unprotect the variable
		        MinecraftForge.EVENT_BUS.register(new GhastUpdateEvent());
		        FMLLog.log(Level.INFO, "Sucessfully nerfed ghasts.");
				
			} catch (Exception e) {
				FMLLog.log(Level.WARNING, "Ghast nerf failed:");
				e.printStackTrace();
			}
		}
	}
}