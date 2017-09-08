package HKSM.data;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;

import gnu.crypto.mode.IMode;
import gnu.crypto.mode.ModeFactory;
import gnu.crypto.pad.IPad;
import gnu.crypto.pad.PadFactory;

/**
 * 
 * 
 * @author Kristian Thorpe <sfgmugen@gmail.com>
 *
 */
public class SaveLoader {
	
	public static byte[] cSharpHeader = {
			00,
			01,
			00,
			00,
			00,
			(byte) 255,
			(byte) 255,
			(byte) 255,
			(byte) 255,
			01,
			00,
			00,
			00,
			00,
			00,
			00,
			00,
			06,
			01,
			00,
			00,
			00
	};
	
	/** */
	public enum MapZone
	{
		NONE,
		TEST_AREA,
		KINGS_PASS,
		CLIFFS,
		TOWN,
		CROSSROADS,
		GREEN_PATH,
		ROYAL_GARDENS,
		FOG_CANYON,
		WASTES,
		DEEPNEST,
		HIVE,
		BONE_FOREST,
		PALACE_GROUNDS,
		MINES,
		RESTING_GROUNDS,
		CITY,
		DREAM_WORLD,
		COLOSSEUM,
		ABYSS,
		ROYAL_QUARTER,
		WHITE_PALACE,
		SHAMAN_TEMPLE,
		WATERWAYS,
		QUEENS_STATION,
		OUTSKIRTS,
		KINGS_STATION,
		MAGE_TOWER,
		TRAM_UPPER,
		TRAM_LOWER,
		FINAL_BOSS,
		SOUL_SOCIETY,
		ACID_LAKE,
		NOEYES_TEMPLE,
		MONOMON_ARCHIVE,
		MANTIS_VILLAGE,
		RUINED_TRAMWAY,
		DISTANT_VILLAGE,
		ABYSS_DEEP,
		ISMAS_GROVE,
		WYRMSKIN,
		LURIENS_TOWER,
		LOVE_TOWER,
		GLADE,
		BLUE_LAKE,
		PEAK,
		JONI_GRAVE,
		OVERGROWN_MOUND,
		CRYSTAL_MOUND,
		BEASTS_DEN
	}
	
	private static final String CIPHER_MODE = "ECB";
	private static final String CIPHER_ALGO = "Rijndael";
	private static final String CIPHER_PAD  = "PKCS7";
	private static final byte[] RIJNDAEL_KEY = "UKu52ePUBwetZ9wNX88o54dnfKRu0T1l".getBytes();
	private static final int BLOCK_SIZE = 16;
	
	
	private static byte[] crypt(byte[] input, int encMode) throws Exception{
		IMode mode = ModeFactory.getInstance(CIPHER_MODE, CIPHER_ALGO, BLOCK_SIZE);
		
		Map<String, Object> attributes = new HashMap<String, Object>();		
	    attributes.put(IMode.KEY_MATERIAL, RIJNDAEL_KEY);
	    attributes.put(IMode.CIPHER_BLOCK_SIZE, BLOCK_SIZE);
	    attributes.put(IMode.STATE, encMode);
	    
	    mode.init(attributes);
	    
	    int bs = mode.currentBlockSize();
	    
	    IPad padding = PadFactory.getInstance(CIPHER_PAD);
	    padding.init(bs);
	    byte[] pad = padding.pad(input, 0, input.length);
	    byte[] pt = new byte[input.length + pad.length];
	    byte[] ct = new byte[pt.length];

	    System.arraycopy(input, 0, pt, 0, input.length);
	    System.arraycopy(pad, 0, pt, input.length, pad.length);
	    
	    for (int i = 0; ((i + bs < pt.length)&&encMode==IMode.DECRYPTION)||((i + bs <= pt.length)&&encMode==IMode.ENCRYPTION); i += bs)	    	 
	    	mode.update(pt, i, ct, i);
	     
	     if( encMode == IMode.DECRYPTION){
		     int unpad = padding.unpad(ct, 0, ct.length);
		     byte[] output = new byte[ct.length - unpad];
		     System.arraycopy(ct, 0, output, 0, ct.length);
		     return output;
	     }
	     
		return ct;
	}
	
	
	/**
	 * Encrypts a string via Rijndael/ECB/RKCS7 with the key UKu52ePUBwetZ9wNX88o54dnfKRu0T1l then encodes in base 64
	 * This uses GNU libraries to bypass the JCE requirement
	 * 
	 * @param input the string representing the json, to be encrypted
	 */
	public static byte[] encrypt(String input) throws Exception {
		byte[] message = input.getBytes();
		byte[] ct = crypt(message, IMode.ENCRYPTION);
		return Base64.getMimeEncoder().encode(ct);
	}
	/**
	 * Base 64 Decodes a string then decrypts it via Rijndael/ECB/RKCS7 with the key UKu52ePUBwetZ9wNX88o54dnfKRu0T1l 
	 * This uses GNU libraries to bypass the JCE requirement
	 * 
	 * @param input the encrypted string representing the json, to be decrypted
	 */
	public static byte[] decrypt(String input) throws Exception {
		byte[] message = input.getBytes();
	    byte[] tmp = Arrays.copyOfRange(message, 0, message.length-1);
	    String str = new String(tmp);
	    message = Base64.getMimeDecoder().decode(str);
	    byte[] output = crypt(message, IMode.DECRYPTION);
	    return output;
	}
	
	public static String getHealthAndSoul(JsonObject je){
		int health = 5 + je.getAsJsonObject("playerData").get("heartPieces").getAsInt();
		int soul = je.getAsJsonObject("playerData").get("MPReserveMax").getAsInt() / 33;
		String ret = health + " | " + soul;
		return ret;
	}
	
	public static String getGeo(JsonObject je){
		int geo = je.getAsJsonObject("playerData").get("geo").getAsInt();
		String ret = "$" + geo;
		return ret;
	}
	
	public static String getPerma(JsonObject je){
		boolean perma = je.getAsJsonObject("playerData").get("permadeathMode").getAsBoolean();
		String ret = perma ? "SteelSoul ON" : "SteelSoul OFF";
		return ret;
	}
	
	
	public static String getLocation(JsonObject je){
		int mapZone = je.getAsJsonObject("playerData").get("mapZone").getAsInt();
		if( mapZone == MapZone.CITY.ordinal() )
			return "CITY OF TEARS";
		String ret = "unknown region";
		return ret;
	}
	
	public static String getCompletion(JsonObject je){
		float cp = je.getAsJsonObject("playerData").get("completionPercentage").getAsFloat();
		String ret = cp + "%";
		return ret;
	}
	
	/**
	 * 
	 * @param input The length of the encrypted + encoded json string in bytes
	 * @return output c# variable length encoded int
	 */
	private static byte[] getLength(byte[] input){
		String rawLengthBinary =  Integer.toBinaryString(input.length);
		String reverse = new StringBuffer(rawLengthBinary).reverse().toString();
		System.out.println(reverse.length());
		String[] data = new String[(int) Math.ceil( ((double)reverse.length()) / 7.0f)];
		System.out.println(data.length);
		for( int i = 0; i < data.length-1; i++){
			data[i] = reverse.subSequence(i*7, ((i+1)*7)).toString()+"1";
		}
		data[data.length-1] = reverse.subSequence((data.length-1)*7, reverse.length()).toString();
		byte[] output = new byte[data.length];
		for( int i = 0; i < data.length; i++){
			System.out.println(data[i]);
			output[i] = (byte) Long.parseLong( new StringBuffer(data[i]).reverse().toString(), 2);
		}
		return output;
	}
	
	/**
	 * Encrypts the given json at the requested file
	 * 
	 * @param dir the location to encrypt and save the save (lol)
	 * @param json the save data to be encrypted and written
	 */
	public static void saveSave(File dir, JsonObject json){
		validateSaveData(json);
		validateCharms(json.getAsJsonObject("playerData"));
		Gson gson = new GsonBuilder().create();
		String jsonString = gson.toJson(json);//.replaceAll("\\s","");
		jsonString = jsonString.substring(0, jsonString.length()-2)+"}}";
		System.out.println(jsonString);
		try {
			byte[] toSave = encrypt(jsonString);
			int count = 0;
			for( int i = 0; i< toSave.length; i++){
				if(!(toSave[i] == 0x0D || toSave[i] == 0x0A))
					count++;
			}
			byte[] fix = new byte[count];
			count = 0;
			for( int i = 0; i< toSave.length; i++){
				if(!(toSave[i] == 0x0D || toSave[i] == 0x0A))
					fix[count++] = toSave[i];
			}
			
			toSave = fix;
			
			FileOutputStream out = new FileOutputStream(dir);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
			outputStream.write(cSharpHeader);
			outputStream.write(getLength(toSave));
			outputStream.write(toSave);
			outputStream.write(new byte[]{11});
			
			byte[] outArray = outputStream.toByteArray();
			
			out.write(outArray);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * @param dir the file to be decrypted into JSON format
	 * @return the decrypted save data in JSON format
	 * @throws Exception to capture generic errors
	 */
	public static JsonObject loadSave(File dir) throws Exception {
		String str = new String(Files.readAllBytes(dir.toPath()),"UTF-8");
		String json = new String(decrypt(str)).trim();
		JsonObject jElement = new Gson().fromJson(json, JsonObject.class);
		return jElement;
	}


	/**
	 * Adds accurate nail art and dash flags to the save's playerData
	 * 
	 * @param json the save file to be modified
	 */
	private static void validateSaveData(JsonObject json){
		JsonObject playerData = json.getAsJsonObject("playerData");
		
		//NAIL ARTS VERIFY
		boolean dashSlash = playerData.get("hasDashSlash").getAsBoolean();
		boolean cyclone = playerData.get("hasCyclone").getAsBoolean();
		boolean upward = playerData.get("hasUpwardSlash").getAsBoolean();
		boolean hasNailArt = dashSlash | cyclone | upward;
		boolean hasAllNailArt = dashSlash & cyclone & upward;
		
		playerData.addProperty("hasNailArt", hasNailArt);
		playerData.addProperty("hasAllNailArts", hasAllNailArt);
		
		//SKILL VERIFY
		playerData.addProperty("canDash", playerData.get("hasDash").getAsBoolean());
	}


	public static void validateCharms(JsonObject playerData){
		JsonArray charms = new JsonArray();
		for( int i = 1; i <= 36; i++){
			if( playerData.get("equippedCharm_"+Integer.toString(i)).getAsBoolean() )
				charms.add(i);
		}
		playerData.add("equppedCharms", charms);
	}
}
