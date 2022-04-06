import java.io.*;
import java.util.*;
import java.math.*;
import java.nio.ByteBuffer;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
public class Main {

	public static HashMap<Short,Short> checkpointMapping;
	public static HashMap<Integer,String> charSpawnerIDMapping;
	
	public static void main(String[] args) throws IOException {
		//getCharacterSpawnerData("C:\\Learning\\eclipse-workspace\\DK64Textures\\src\\117C77A_ZLib.bin","C:\\Users\\Jacob\\Desktop\\output.txt");
		//getFloorData("C:\\Learning\\eclipse-workspace\\DK64FileParsers\\src\\test-files\\63CE96_ZLibtmp.bin.bin","C:\\Users\\Jacob\\Desktop\\output.txt");
		floorIsWater("C:\\Learning\\eclipse-workspace\\DK64FileParsers\\src\\test-files\\63CDE4_ZLibtmp.bin","C:\\Users\\Jacob\\Desktop\\output.bin");
		//getCharacterSpawnerData("C:\\Learning\\eclipse-workspace\\DK64Textures\\src\\117CA8C_ZLib.bin","C:\\Users\\Jacob\\Desktop\\output.txt");
		//printPointerTableOffsets("C:\\Users\\Jacob\\Desktop\\racedata.bin","C:\\Users\\Jacob\\Desktop\\output.txt");
		//getCheckpointData("C:\\Learning\\crankys-lab\\dk64-tag-anywhere\\"+
		//					"maps\\14 - Angry_Aztec__Beetle_Race\\race_checkpoints.bin",
		//					"C:\\Users\\Jacob\\Desktop\\output.txt");
		//getCheckpointData("C:\\Learning\\crankys-lab\\dk64-tag-anywhere\\"+
		//					"maps\\185 - Creepy_Castle__Car_Race\\race_checkpoints.bin",
		//					"C:\\Users\\Jacob\\Desktop\\output.txt");
		//getGeometryData("C:\\Users\\Jacob\\Desktop\\17DA9E_ZLib.bin","C:\\Users\\Jacob\\Desktop\\output.txt");
	}
	
	public static void floorIsWater(String inPath, String outPath) throws IOException {
		System.out.println("Begin.");
		Path p = FileSystems.getDefault().getPath("", inPath);
		byte[] bytes = Files.readAllBytes(p);
		
		FileOutputStream fos = new FileOutputStream(outPath);
		
		ArrayList<Integer> waterPropertyIndices = new ArrayList<Integer>();
		
		int index=4;
		int meshIndex=1;
		while(index < bytes.length) {
			byte[] nextAddressBytes = {bytes[index],bytes[index+1],bytes[index+2],bytes[index+3]};
			int nextAddress = ByteBuffer.wrap(nextAddressBytes).getInt();
			index+=4;
			int triNum=0;
			while(index < nextAddress) {
				waterPropertyIndices.add(index+19);
				index+=24;
			}
		}
		
		for(Integer i: waterPropertyIndices) bytes[i] = 1;
		fos.write(bytes);
		fos.close();
		
		System.out.println("End.");
		
	}
	
	public static void getFloorData(String inPath, String outPath) throws IOException {
		getCollisionData('f',inPath,outPath);
	}
	
	public static void getWallData(String inPath, String outPath) throws IOException {
		getCollisionData('w',inPath,outPath);
	}
	
	public static void getCollisionData(char floor_or_wall, String inPath, String outPath) throws IOException {
		File output = new File(outPath);
		PrintWriter fileout = new PrintWriter(output);
		System.out.println("Begin.");
		Path p = FileSystems.getDefault().getPath("", inPath);
		byte[] bytes = Files.readAllBytes(p);
		
		String sOut = "Unk bytes (0x4):"+String.format("%02x%02x%02x%02x\n", bytes[0],bytes[1],bytes[2],bytes[3]);
		int index=4;
		int meshIndex=1;
		while(index < bytes.length) {
			sOut+="\nNext Mesh Address: "+String.format("%02x%02x%02x%02x\n", bytes[index],bytes[index+1],bytes[index+2],bytes[index+3]);
			byte[] nextAddressBytes = {bytes[index],bytes[index+1],bytes[index+2],bytes[index+3]};
			int nextAddress = ByteBuffer.wrap(nextAddressBytes).getInt();
			index+=4;
			sOut+="***********Mesh "+(meshIndex++)+"***********\n";
			int triNum=0;
			while(index < nextAddress) {
				sOut+="****Tri "+(triNum++)+"****\n";
				byte[] 	x1 = {bytes[index++],bytes[index++]},
						y1 = {bytes[index++],bytes[index++]},
						z1 = {bytes[index++],bytes[index++]},
						x2 = {bytes[index++],bytes[index++]},
						y2 = {bytes[index++],bytes[index++]},
						z2 = {bytes[index++],bytes[index++]},
						x3 = {bytes[index++],bytes[index++]},
						y3 = {bytes[index++],bytes[index++]},
						z3 = {bytes[index++],bytes[index++]},
						props_1 = {bytes[index++]},
						props_2 = {bytes[index++]},
						props_3 = {bytes[index++]},
						props_4 = {bytes[index++]},
						props_5 = {bytes[index++]},
						props_6 = {bytes[index++]};
				if(floor_or_wall == 'f') {
					sOut+=	"x1: "+ByteBuffer.wrap(x1).getShort()/6+" "+String.format("(%02x%02x)\n", x1[0],x1[1])
						    +"y1: "+ByteBuffer.wrap(y1).getShort()/6+" "+String.format("(%02x%02x)\n", y1[0],y1[1])
						    +"z1: "+ByteBuffer.wrap(z1).getShort()/6+" "+String.format("(%02x%02x)\n", z1[0],z1[1])
						    +"x2: "+ByteBuffer.wrap(x2).getShort()/6+" "+String.format("(%02x%02x)\n", x2[0],x2[1])
						    +"y2: "+ByteBuffer.wrap(y2).getShort()/6+" "+String.format("(%02x%02x)\n", y2[0],y2[1])
						    +"z2: "+ByteBuffer.wrap(z2).getShort()/6+" "+String.format("(%02x%02x)\n", z2[0],z2[1])
							+"x3: "+ByteBuffer.wrap(x3).getShort()/6+" "+String.format("(%02x%02x)\n", x3[0],x3[1])
						    +"y3: "+ByteBuffer.wrap(y3).getShort()/6+" "+String.format("(%02x%02x)\n", y3[0],y3[1])
						    +"z3: "+ByteBuffer.wrap(z3).getShort()/6+" "+String.format("(%02x%02x)\n", z3[0],z3[1]);
					
					sOut+="floor property bitfield 1 (void, non-solid): "+Integer.toBinaryString((int)props_1[0])+"\n";
					sOut+="floor property bitfield 2 (damage, insta-death, water): "+Integer.toBinaryString((int)props_2[0])+"\n";
					sOut+="floor property bitfield 3: "+Integer.toBinaryString((int)props_3[0])+"\n";
					sOut+="floor property bitfield 4 (sfx): "+Integer.toBinaryString((int)props_4[0])+"\n";
					sOut+="floor property bitfield 5 (brightness): "+Integer.toBinaryString((int)props_5[0])+"\n";
					sOut+="floor property bitfield 6: "+Integer.toBinaryString((int)props_6[0])+"\n";
				} else {
					sOut+=	"x1: "+ByteBuffer.wrap(x1).getShort()+" "+String.format("(%02x%02x)\n", x1[0],x1[1])
						    +"y1: "+ByteBuffer.wrap(y1).getShort()+" "+String.format("(%02x%02x)\n", y1[0],y1[1])
						    +"z1: "+ByteBuffer.wrap(z1).getShort()+" "+String.format("(%02x%02x)\n", z1[0],z1[1])
						    +"x2: "+ByteBuffer.wrap(x2).getShort()+" "+String.format("(%02x%02x)\n", x2[0],x2[1])
						    +"y2: "+ByteBuffer.wrap(y2).getShort()+" "+String.format("(%02x%02x)\n", y2[0],y2[1])
						    +"z2: "+ByteBuffer.wrap(z2).getShort()+" "+String.format("(%02x%02x)\n", z2[0],z2[1])
							+"x3: "+ByteBuffer.wrap(x3).getShort()+" "+String.format("(%02x%02x)\n", x3[0],x3[1])
						    +"y3: "+ByteBuffer.wrap(y3).getShort()+" "+String.format("(%02x%02x)\n", y3[0],y3[1])
						    +"z3: "+ByteBuffer.wrap(z3).getShort()+" "+String.format("(%02x%02x)\n", z3[0],z3[1]);
					sOut+="wall property bitfield 1: "+Integer.toBinaryString((int)props_1[0])+"\n";
					sOut+="wall property bitfield 2: "+Integer.toBinaryString((int)props_2[0])+"\n";
					sOut+="wall property bitfield 3: "+Integer.toBinaryString((int)props_3[0])+"\n";
					sOut+="wall property bitfield 4: "+Integer.toBinaryString((int)props_4[0])+"\n";
					sOut+="wall property bitfield 5 (damage): "+Integer.toBinaryString((int)props_5[0])+"\n";
					sOut+="wall property bitfield 6: "+Integer.toBinaryString((int)props_6[0])+"\n";
				}
			}
		}
		
		System.out.println(sOut);
		fileout.println(sOut);
		fileout.close();
		System.out.println("End.");
	}
	
	public static void getGeometryData(String inPath, String outPath) throws IOException {
		File output = new File(outPath);
		PrintWriter fileout = new PrintWriter(output);
		System.out.println("Begin.");
		Path p = FileSystems.getDefault().getPath("", inPath);
		byte[] bytes = Files.readAllBytes(p);
		
		byte[] dlStart = {bytes[52],bytes[53],bytes[54],bytes[55]};
		byte[] vertStart = {bytes[56],bytes[57],bytes[58],bytes[59]};
		byte[] vertEnd = {bytes[64],bytes[65],bytes[66],bytes[67]};
		byte[] sectionStart = {bytes[88],bytes[89],bytes[90],bytes[91]};
		byte[] sectionEnd = {bytes[92],bytes[93],bytes[94],bytes[95]};
		byte[] chunkCountOffset = {bytes[100],bytes[101],bytes[102],bytes[103]};
		byte[] chunkStart = {bytes[104],bytes[105],bytes[106],bytes[107]};
		String sOut = "**** Header Data ****\n";
		sOut += "DL Start: "+String.format("%02x%02x%02x%02x", dlStart[0],dlStart[1],dlStart[2],dlStart[3]) + "\n" +
				"Vert Start: "+String.format("%02x%02x%02x%02x", vertStart[0],vertStart[1],vertStart[2],vertStart[3]) + "\n" +
				"Vert End: "+String.format("%02x%02x%02x%02x", vertEnd[0],vertEnd[1],vertEnd[2],vertEnd[3]) + "\n" +
				"Section Start: "+String.format("%02x%02x%02x%02x", sectionStart[0],sectionStart[1],sectionStart[2],sectionStart[3]) + "\n" +
				"Section End: "+String.format("%02x%02x%02x%02x", sectionEnd[0],sectionEnd[1],sectionEnd[2],sectionEnd[3]) + "\n" +
				"Chunk Count Offset: "+String.format("%02x%02x%02x%02x", chunkCountOffset[0],chunkCountOffset[1],chunkCountOffset[2],chunkCountOffset[3]) + "\n" + 
				"Chunk Start: "+String.format("%02x%02x%02x%02x", chunkStart[0],chunkStart[1],chunkStart[2],chunkStart[3]) + "\n" +
				"*********************\n";
		
		sOut += "**** Chunk Data ****\n";
		int chunkOffset = ByteBuffer.wrap(chunkCountOffset).getInt();
		byte[] numCh = {bytes[chunkOffset],bytes[chunkOffset+1],bytes[chunkOffset+2],bytes[chunkOffset+3]};
		int numChunks = ByteBuffer.wrap(numCh).getInt();
		sOut += "Number of Chunks: "+numChunks + "\n";
		
		ArrayList<byte[]> chunkData = new ArrayList<byte[]>();
		int chunkSize = 52; //chunk size = 0x34 = 52 bytes
		int chunkStartOffset = ByteBuffer.wrap(chunkStart).getInt();
		for(int i=0; i<numChunks; ++i) {
			byte[] chunkBytes = new byte[chunkSize];
			for(int j=0; j<52; ++j) {
				chunkBytes[j] = bytes[chunkStartOffset + i*chunkSize + j];
			}
			chunkData.add(chunkBytes);
			byte[] x = {chunkBytes[0],chunkBytes[1],chunkBytes[2],chunkBytes[3]};
			byte[] y = {chunkBytes[4],chunkBytes[5],chunkBytes[6],chunkBytes[7]};
			sOut += "** Chunk "+(i+1)+": **\n" + 
					"x: "+ByteBuffer.wrap(x).getInt() + "\n" +
					"y: "+ByteBuffer.wrap(y).getInt() + "\n";
			sOut += "** DL table: \n";
			for(int j=0; j<4; ++j) {
				byte[] dlOffsets = {chunkBytes[12 + j*8 + 0],chunkBytes[12 + j*8 + 1],chunkBytes[12 + j*8 + 2],chunkBytes[12 + j*8 + 3]};
				byte[] dlSizes = {chunkBytes[12 + j*8 + 4],chunkBytes[12 + j*8 + 5],chunkBytes[12 + j*8 + 6],chunkBytes[12 + j*8 + 7]};
				sOut += "**** DL " + (j+1) + " Offset: " + String.format("%02x%02x%02x%02x",dlOffsets[0],dlOffsets[1],dlOffsets[2],dlOffsets[3]) + "\n" +
						"**** DL " + (j+1) + " Sizes: " + String.format("%02x%02x%02x%02x",dlSizes[0],dlSizes[1],dlSizes[2],dlSizes[3]) + "\n" + 
						"****\n";
			}
			byte[] vertOffset = {chunkBytes[44],chunkBytes[45],chunkBytes[46],chunkBytes[47]};
			byte[] vertSize = {chunkBytes[48],chunkBytes[49],chunkBytes[50],chunkBytes[51]};
			sOut += "Vert Offset: " + String.format("%02x%02x%02x%02x",vertOffset[0],vertOffset[1],vertOffset[2],vertOffset[3]) + "\n" +
					"Vert Size: " + String.format("%02x%02x%02x%02x",vertSize[0],vertSize[1],vertSize[2],vertSize[3]) + "\n" +
					"****\n";
		}
		
		sOut += "*********************\n";
		
		System.out.println(sOut);
		fileout.println(sOut);
		fileout.close();
		System.out.println("End.");
	}
	
	public static void getCheckpointData(String inPath, String outPath) throws IOException {
		checkpointMapping = new HashMap<Short,Short>();
		
		File output = new File(outPath);
		PrintWriter fileout = new PrintWriter(output);
		System.out.println("Begin.");
		Path p = FileSystems.getDefault().getPath("", inPath);
		byte[] bytes = Files.readAllBytes(p);
		
		byte[] unkData0 = {bytes[0]};
		byte[] unkData1 = {bytes[1], bytes[2]};
		fileout.println("Unknown data, 3 bytes");
		System.out.println("Unknown data, 3 bytes");
		
		byte[] numCP = {bytes[3], bytes[4]};
		short numCheckpoints = ByteBuffer.wrap(numCP).getShort();
		fileout.println("Number of checkpoints: "+numCheckpoints);
		System.out.println("Number of checkpoints: "+numCheckpoints);
		int index = 5;
		for(short i=0; i<numCheckpoints; ++i) {
			byte[] cpN = {bytes[index++],bytes[index++]};
			short checkpointN = ByteBuffer.wrap(cpN).getShort();
			fileout.println("Checkpoint "+i+": Entry #"+checkpointN);
			System.out.println("Checkpoint "+i+": Entry #"+checkpointN);
			
			checkpointMapping.put(i,checkpointN);
		}
		
		fileout.println("*********************\nCheckpoint Data\n*********************");
		System.out.println("*********************\nCheckpoint Data\n*********************");
		for(short i=0; i<numCheckpoints; ++i) {
			fileout.println("****************\nCheckpoint "+i+" (Entry #"+checkpointMapping.get(i)+")\n****************");
			System.out.println("****************\nCheckpoint "+i+" (Entry #"+checkpointMapping.get(i)+")\n****************");
			byte xPos[] = {bytes[index++],bytes[index++]};
			byte yPos[] = {bytes[index++],bytes[index++]};
			byte zPos[] = {bytes[index++],bytes[index++]};
			byte ang[] = {bytes[index++],bytes[index++]};
			
			short x,y,z,angle;
			x = ByteBuffer.wrap(xPos).getShort();
			y = ByteBuffer.wrap(yPos).getShort();
			z = ByteBuffer.wrap(zPos).getShort();
			angle = ByteBuffer.wrap(ang).getShort();
			
			byte unkFloat0[] = {bytes[index++], bytes[index++], bytes[index++], bytes[index++]};
			byte unkFloat1[] = {bytes[index++], bytes[index++], bytes[index++], bytes[index++]};
			byte unkShort0[] = {bytes[index++], bytes[index++]};
			byte unkShort1[] = {bytes[index++], bytes[index++]};
			byte unkFloat2[] = {bytes[index++], bytes[index++], bytes[index++], bytes[index++]};
			byte unkShort2[] = {bytes[index++], bytes[index++]};
			byte unkShort3[] = {bytes[index++], bytes[index++]};
			
			fileout.println("X position: "+x);
			fileout.println("Y position: "+y);
			fileout.println("Z position: "+z);
			fileout.println("Angle: "+angle);
			System.out.println("X position: "+x);
			System.out.println("Y position: "+y);
			System.out.println("Z position: "+z);
			System.out.println("Angle: "+angle+"\n");
			
			fileout.println("Unknown float 0: "+ByteBuffer.wrap(unkFloat0).getFloat());
			fileout.println("Unknown float 1: "+ByteBuffer.wrap(unkFloat1).getFloat());
			fileout.println("Unknown short 0: "+ByteBuffer.wrap(unkShort0).getShort());
			fileout.println("Unknown short 1: "+ByteBuffer.wrap(unkShort1).getShort());
			fileout.println("Unknown float 2: "+ByteBuffer.wrap(unkFloat2).getFloat());
			fileout.println("Unknown short 2: "+ByteBuffer.wrap(unkShort2).getShort());
			fileout.println("Unknown short 3: "+ByteBuffer.wrap(unkShort3).getShort());
			
			System.out.println("Unknown float 0: "+ByteBuffer.wrap(unkFloat0).getFloat());
			System.out.println("Unknown float 1: "+ByteBuffer.wrap(unkFloat1).getFloat());
			System.out.println("Unknown short 0: "+ByteBuffer.wrap(unkShort0).getShort());
			System.out.println("Unknown short 1: "+ByteBuffer.wrap(unkShort1).getShort());
			System.out.println("Unknown float 2: "+ByteBuffer.wrap(unkFloat2).getFloat());
			System.out.println("Unknown short 2: "+ByteBuffer.wrap(unkShort2).getShort());
			System.out.println("Unknown short 3: "+ByteBuffer.wrap(unkShort3).getShort());
			
			fileout.println("*********************");
			System.out.println("*********************");
		}
		fileout.println("End.");
		fileout.close();
		System.out.println("End.");
		
	}
	
	public static void getCharacterSpawnerData(String inPath, String outPath) throws IOException{
		setupNameMappings();
		
		File output = new File(outPath);
		PrintWriter fileout = new PrintWriter(output);
		System.out.println("Begin.");
		Path p = FileSystems.getDefault().getPath("", inPath);
		byte[] bytes = Files.readAllBytes(p);
		byte[] paths = {bytes[0],bytes[1]};
		//
		int numPaths = ByteBuffer.wrap(paths).getShort();
		//int numPoints = ByteBuffer.wrap(points).getShort();
		fileout.println("Pens: "+numPaths);
		System.out.println("Pens: "+numPaths);
		int index = 2;
		for(int i=0; i<numPaths; ++i) {
			fileout.println("********\nPen "+(i+1)+"\n********");
			System.out.println("********\nPen "+(i+1)+"\n********");
			byte[] points = {bytes[index++],bytes[index++]};
			int numPoints = ByteBuffer.wrap(points).getShort();
			fileout.println(numPoints+" points.");
			System.out.println(numPoints+" points.");
			for(int j=0; j<numPoints; ++j) {
				fileout.println("Point "+(j+1)+":");
				System.out.println("Point "+(j+1)+":");
				byte[] xCoord = {bytes[index++],bytes[index++]};
				byte[] yCoord = {bytes[index++],bytes[index++]};
				byte[] zCoord = {bytes[index++],bytes[index++]};
				fileout.println("x: "+ByteBuffer.wrap(xCoord).getShort()+" "+
								   "y: "+ByteBuffer.wrap(yCoord).getShort()+" "+
								   "z: "+ByteBuffer.wrap(zCoord).getShort());
				System.out.println("x: "+ByteBuffer.wrap(xCoord).getShort()+" "+
						   "y: "+ByteBuffer.wrap(yCoord).getShort()+" "+
						   "z: "+ByteBuffer.wrap(zCoord).getShort());
			}
			byte[] miscData = {bytes[index++],bytes[index++]};
			int miscDataCount = ByteBuffer.wrap(miscData).getShort();
			for(int j=0; j<miscDataCount; ++j) {
				fileout.println("-------Misc Data "+(j+1)+"--------");
				System.out.println("-------Misc Data "+(j+1)+"--------");
				byte[] xCoord = {bytes[index++],bytes[index++]};
				byte[] yCoord = {bytes[index++],bytes[index++]};
				byte[] zCoord = {bytes[index++],bytes[index++]};
				fileout.println("x: "+ByteBuffer.wrap(xCoord).getShort()+" "+
								   "y: "+ByteBuffer.wrap(yCoord).getShort()+" "+
								   "z: "+ByteBuffer.wrap(zCoord).getShort());
				System.out.println("x: "+ByteBuffer.wrap(xCoord).getShort()+" "+
						   			"y: "+ByteBuffer.wrap(yCoord).getShort()+" "+
						   			"z: "+ByteBuffer.wrap(zCoord).getShort());
				String miscStr = String.format("%02x%02x%02x%02x", 
												bytes[index++],bytes[index++],bytes[index++],bytes[index++]);
				fileout.println(miscStr);
				System.out.println(miscStr);
			}
			if(miscDataCount > 0 ) {
				fileout.println("---------------");
				System.out.println("---------------");
			}
			byte[] unk1 = {bytes[index++],bytes[index++]};
			byte[] unk2 = {bytes[index++],bytes[index++]};
			int unknown1 = ByteBuffer.wrap(unk1).getShort();
			int unknown2 = ByteBuffer.wrap(unk2).getShort();
			fileout.println("Unique Pen ID: "+unknown1);
			System.out.println("Unique Pen ID: "+unknown1);
			fileout.println("Unknown 2-byte binary value: "+unknown2);
			System.out.println("Unknown 2-byte binary value: "+unknown2);
		}
		fileout.println("********");
		System.out.println("********");
		
		byte[] spawners = {bytes[index++],bytes[index++]};
		int numSpawners = ByteBuffer.wrap(spawners).getShort();
		fileout.println("Number of spawners: "+numSpawners+"\n");
		System.out.println("Number of spawners: "+numSpawners+"\n");
		
		fileout.println("Spawners: ");
		System.out.println("Spawners: ");
		for(int i=0; i<numSpawners; ++i)
		{
			fileout.println("********\nSpawner "+(i+1)+"\n********");
			System.out.println("********\nSpawner "+(i+1)+"\n********");
			byte[] enemyvalue = {bytes[index++]}; 
			byte[] unk1 = {bytes[index++]};
			byte[] yrot = {bytes[index++],bytes[index++]};
			byte[] xpos = {bytes[index++],bytes[index++]};
			byte[] ypos = {bytes[index++],bytes[index++]};
			byte[] zpos = {bytes[index++],bytes[index++]};
			byte[] csmodel = {bytes[index++]};
			byte[] unk2 = {bytes[index++]};
			byte[] maxidlespeed = {bytes[index++]};
			byte[] maxaggrospeed = {bytes[index++]};
			byte[] pen_id = {bytes[index++]};
			byte[] scale = {bytes[index++]};
			byte[] aggro = {bytes[index++]};
			byte[] unk4 = {bytes[index++]};
			byte[] unk5 = {bytes[index++]};
			byte[] spawntrigger = {bytes[index++]};
			byte[] respawntimer = {bytes[index++]};
			byte[] unk6 = {bytes[index++]};
			String chSpawnOut = "Enemy value: ("+enemyvalue[0]+") "+charSpawnerIDMapping.get(Integer.parseInt(String.format("%02x",enemyvalue[0]),16))+"\n"+
								"Unknown byte: "+String.format("%02x",unk1[0])+"\n"+
								"Y rotation: "+ByteBuffer.wrap(yrot).getShort()+"\n"+
								"X position: "+ByteBuffer.wrap(xpos).getShort()+"\n"+
								"Y position: "+ByteBuffer.wrap(ypos).getShort()+"\n"+
								"Z position: "+ByteBuffer.wrap(zpos).getShort()+"\n"+
								"CS Model: "+csmodel[0]+"\n"+
								"Unknown byte: "+String.format("%02x",unk2[0])+"\n"+
								"MaxIdleSpeed: "+maxidlespeed[0]+"\n"+
								"MaxAggroSpeed: "+maxaggrospeed[0]+"\n"+
								"Pen ID: "+String.format("%02x",pen_id[0])+"\n"+
								"Scale: "+scale[0]+"\n"+
								"Aggro: "+aggro[0]+"\n"+
								"Unknown bytes: "+String.format("%02x %02x",unk4[0],unk5[0])+"\n"+
								"SpawnTrigger: "+spawntrigger[0]+"\n"+
								"RespawnTimer: "+respawntimer[0]+"\n"+
								"Unknown byte: "+String.format("%02x",unk6[0])+"\n";
			fileout.println(chSpawnOut);
			System.out.println(chSpawnOut);
		}
		fileout.println("********");
		System.out.println("********");
		fileout.close();
		System.out.println("End.");
	}
	
	public static void setupNameMappings() throws FileNotFoundException {
		if(charSpawnerIDMapping == null) charSpawnerIDMapping = new HashMap<Integer,String>();
		Scanner sc = new Scanner(new File("C:\\Learning\\eclipse-workspace\\DK64Textures\\src\\CharSpawnerIDMapping.txt"));
		int i=0;
		while(sc.hasNextLine()) {
			charSpawnerIDMapping.put(i++,sc.nextLine().trim());
		}
	}

	public static void printPointerTableOffsets(String inPath, String outPath) throws IOException{
		File output = new File(outPath);
		PrintWriter fileout = new PrintWriter(output);
		System.out.println("Begin.");
		Path p = FileSystems.getDefault().getPath("", inPath);
		byte[] bytes = Files.readAllBytes(p);
		for(int i=0; i<bytes.length; i+=4) {
			byte[] arr = {bytes[i],bytes[i+1],bytes[i+2],bytes[i+3]};
			ByteBuffer wrap = ByteBuffer.wrap(arr);
			int result = wrap.getInt();
			int offset = Integer.parseInt("101C50",16);
			System.out.println(Integer.toString(result+offset,16));
			fileout.println("0x"+Integer.toString(result+offset,16).toUpperCase());
		}
		fileout.close();
		System.out.println("End.");
	}
}
