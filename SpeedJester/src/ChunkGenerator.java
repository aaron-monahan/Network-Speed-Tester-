
public class ChunkGenerator {
	
	private final int KB_TO_BYTES = 1;
	private int sizeKB;
	
	//size in KB
	public ChunkGenerator(int size){
		this.sizeKB = size;
	}
	
	
	public String generate()
	{
		StringBuffer chunkBuffer = new StringBuffer();
		
		int size = sizeKB * KB_TO_BYTES;
		
    	while( size > 0)
    	{
    		size--;
    		chunkBuffer.append('t');
    	}
    	return chunkBuffer.toString();
	}

}
