package com.uibinder.server.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.uibinder.shared.control.Block;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class BlockDao {

	static{
		ObjectifyService.register(Block.class);
	}
	
	/*public void updateBlock(Block blockToReturn) {
		deleteBlock(blockToReturn.getId());
		saveBlock(blockToReturn);
	}*/
	
	public Block getBlockByBlock(Block block){
		Block blockToReturn = null;
		//blockToReturn = getBlock(block.getClassRoom(), block.getDay(),block.getEndHour(), block.getStartHour());
		//if(blockToReturn == null){
			blockToReturn = block;
			if(blockToReturn.getId()==null){
				saveBlock(blockToReturn);
			}
		//}
		return blockToReturn;
	}

	/**
	 * Block does not allow search other than by ID
	 * 
	 * @param classRoom
	 * @param day
	 * @param endHour
	 * @param startHour
	 * @return
	 */
	private Block getBlock(String classRoom, int day, int endHour, int startHour) {
		return null;// ofy().load().type(Block.class).filter("classRoom", classRoom).filter("day", day).filter("endHour", endHour).filter("startHour", startHour).first().now();
	}
	
	/*Block does not accept deletion it could create a problem
	/*private void deleteBlock(Long id) {
		Key<Block> key = Key.create(Block.class, id);
		ofy().delete().key(key).now();
	}*/

	public void saveBlock(Block b) {
		ofy().save().entity(b).now();
	}

	public Block getBlockById(Long id) {
		if(id!=null){
		Key<Block> key = Key.create(Block.class, id);
		return ofy().load().key(key).now();
		} else {
			return null;
		}
	}
	
}
