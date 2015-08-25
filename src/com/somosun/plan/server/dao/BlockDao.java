package com.somosun.plan.server.dao;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.somosun.plan.shared.control.Block;
import com.somosun.plan.shared.control.SubjectValue;

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
		blockToReturn = getBlock(block.getClassRoom(), block.getDay(),block.getEndHour(), block.getStartHour());
		if(blockToReturn == null){
			blockToReturn = block;
			if(blockToReturn.getId()==null){
				saveBlock(blockToReturn);
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
	private Block getBlock(String classRoom, int day, int endHour, int startHour) {
		return ofy().load().type(Block.class).filter("classRoom", classRoom).filter("day", day).filter("endHour", endHour).filter("startHour", startHour).first().now();
	}

	/**
	 * This method should be used only to create a block, not to update a block, this method will delete any id in order to create and not to save
	 * @param b
	 */
	public void saveBlock(Block b) {
		if(b.getId() != null) b.setId(generateId());
		ofy().save().entity(b).now();
	}

	private Long generateId() {
		ObjectifyFactory f = new ObjectifyFactory();
		Key<Block> key = f.allocateId(Block.class);
		return key.getId();
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
