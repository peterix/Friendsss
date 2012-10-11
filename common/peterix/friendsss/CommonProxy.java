package peterix.friendsss;

import java.lang.reflect.Field;

import net.minecraft.src.Block;

public class CommonProxy {
	public void nerfEndermen () {
		// get enderman class
	    Class endermanClass = net.minecraft.src.EntityEnderman.class;
	    // get the carriableBlocks field
	    try {
			// steal only flowers. flowers are pretty :D 
			boolean[] carriableBlocks = new boolean[4096];
	        carriableBlocks[Block.plantYellow.blockID] = true;
	        carriableBlocks[Block.plantRed.blockID] = true;
	        
	        Field carriableBlocksField = endermanClass.getField("carriableBlocks");
	        carriableBlocksField.set(null, carriableBlocks);
			
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}