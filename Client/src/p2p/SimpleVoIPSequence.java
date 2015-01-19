package p2p;

import java.util.ArrayList;

public class SimpleVoIPSequence {

	private ArrayList<BlockSeqPair> blocks;
	private int maxSize = 4;
	
	public SimpleVoIPSequence(int maxSize) {
		blocks = new ArrayList<BlockSeqPair>();
		this.maxSize = maxSize;
	}
	
	//packets added contain the relative difference from the previously recieved packet, so that they can be placed in the sequencer.
	//note: if a negative sequence number is recieved, DO NOT use that as the base to calculate the sequence difference to the
	//next packet
	public synchronized void add(byte[] data, byte seqDiff) {
		if (seqDiff == 0) return; //duplicate
		else if (seqDiff > 0) {
			//after last sequence element, add block at end
			blocks.add(new BlockSeqPair(data, seqDiff));
		} else {
			//before last sequence element, traverse backwards to find our place
			int position = blocks.size();
			while (seqDiff < 0) {
				seqDiff += (blocks.get(--position).seqDiff);
				if (seqDiff == 0) return; //duplicate
			}
			blocks.get(position).seqDiff -= seqDiff; //alter seqDiff of block after new one
			//to correct the difference to the last block (which is now the new block!)
			blocks.add(position, new BlockSeqPair(data, seqDiff));
		}
		
		//afterwards, check if our buffer is too large. If it is, skip the earlier packets.
		while (blocks.size() > maxSize) {
			blocks.remove(0);
			System.out.println("buffer exceeded, trimming queued sequence");
		}
	}
	
	public synchronized byte[] get() {
		if (blocks.size() == 0) return null;
		BlockSeqPair first = blocks.get(0);
		if (--first.seqDiff <= 0) {
			//move forward in the sequence. return our block if it is ready to be queued, otherwise return null.
			//(sequencer will deal with repeating segments or playing nothing)
			byte[] dat = first.block;
			blocks.remove(0);
			return dat;
		}
		return null;
	}
	
	private class BlockSeqPair {
		public byte[] block;
		public byte seqDiff;
		public BlockSeqPair(byte[] block, byte seqDiff) {
			this.block = block;
			this.seqDiff = seqDiff;
		}
	}
}
