import java.io.*;
import java.net.Socket;

public class ServerConnectionHandler implements Runnable {

	private Socket socket;

	public ServerConnectionHandler(Socket socket) {
		this.socket = socket;
		
	}

	@Override
	public void run() {
		BufferedReader reader = null;
		PrintWriter writer = null;
		boolean done = false;

		long startTestTime = 0;
		long currentTime = 0;
		int interactions = 0;

		try {
			System.out.println("size buffer: " + socket.getReceiveBufferSize());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(socket.getOutputStream(), true);
			String line;

			while (!done && ((line = reader.readLine()) != null)) {
				if (line.compareTo("startdownload") == 0) {
					String downloadChunk = (new ChunkGenerator(SpeedJesterMain.BUFFER_SIZE)).generate();
					while (!done && ((line = reader.readLine()) != null)) {
						if (line.compareTo("downloaddone") == 0) {
							done = true;
							System.out.println("Download Test concluded!");
						}else{
							writer.println(downloadChunk);
						}
					}

				}else if(line.compareTo("startupload") == 0)
				{
					
					Double uploadSpeed = new Double(0);
					startTestTime = System.currentTimeMillis();
					while ((startTestTime + SpeedJesterMain.TEST_DURATION) > currentTime) {
						interactions++;
						currentTime = System.currentTimeMillis();
						writer.println(getUploadSpeed((double) (currentTime - startTestTime), interactions));
						reader.readLine();
//						System.out.println("time:" + (double) (currentTime - startTestTime) + "interactions : " + interactions);
					}
					done = true;
					//System.out.println("Upload Test concluded!");
					writer.println("uploaddone");
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (reader != null)
					reader.close();
				if (writer != null)
					writer.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}

	}

	private String getUploadSpeed(double time, int interactions) {
		// buffer 16384 = 16KB
		Double uploadSpeed = ((interactions * SpeedJesterMain.BUFFER_SIZE) / (time / 1000)) * 8;
		StringBuffer s = new StringBuffer();

		s.append("Interaction;" + interactions);
		s.append(";Time;" + time);
		s.append(";Upload;" + uploadSpeed);

		return s.toString();
	}
}
