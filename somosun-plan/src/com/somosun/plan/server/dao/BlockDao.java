package com.somosun.plan.server.dao;

import java.util.List;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.somosun.plan.shared.control.Block;

import static com.googlecode.objectify.ObjectifyService.ofy;

public class BlockDao implements Dao<Block>{
	
	final static Logger log = Logger.getLogger("BlockDao");

	static{
		ObjectifyService.register(Block.class);
	}
	
	/*public void updateBlock(Block blockToReturn) {
		deleteBlock(blockToReturn.getId());
		saveBlock(blockToReturn);
	}*/
	
	public Block getBlockByBlock(Block block){
		Block blockToReturn = null;
		blockToReturn = get(block.getClassRoom(), block.getDay(),block.getEndHour(), block.getStartHour());
		if(blockToReturn == null){
			blockToReturn = block;
			if(blockToReturn.getId()==null){
				save(blockToReturn);
			}
		}
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
	private Block get(String classRoom, int day, int endHour, int startHour) {
		return ofy().load().type(Block.class).filter("classRoom", classRoom).filter("day", day).filter("endHour", endHour).filter("startHour", startHour).first().now();
	}

	/**
	 * This method should be used only to create a block, not to update a block, this method will delete any id in order to create and not to save
	 * @param b
	 */
	public Long save(Block b) {
		if(b.getId() != null) b.setId(generateId());
		ofy().save().entity(b).now();
		return b.getId();
	}

	public Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<Block> key = f.allocateId(Block.class);
		return key.getId();
	}

	public Block getById(Long id) {
		Block toReturn = null;
		if(id!=null){
			Key<Block> key = Key.create(Block.class, id);
			toReturn = ofy().load().key(key).now();
		}
		return toReturn;
	}

	public void deleteAll() {
		log.warning("Starting to delete all blocks");
		List<Block> list = ofy().load().type(Block.class).list();
		for(Block b : list){
			delete(b.getId());
		}
		log.warning("All blocks deleted");
	}

	public boolean delete(Long id) {
		boolean toReturn = false;
		if(id != null){			
			Key<Block> key = Key.create(Block.class, id);
			ofy().delete().key(key).now();
			toReturn = true;
		}
		return toReturn;
	}
	
}
