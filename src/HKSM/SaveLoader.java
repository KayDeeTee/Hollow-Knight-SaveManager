package HKSM;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import com.google.gson.*;

import gnu.crypto.cipher.IBlockCipher;
import gnu.crypto.mode.IMode;
import gnu.crypto.mode.ModeFactory;
import gnu.crypto.pad.IPad;
import gnu.crypto.pad.PadFactory;

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
	
	public enum MapZone
	{
		// Token: 0x04002E53 RID: 11859
		NONE,
		// Token: 0x04002E54 RID: 11860
		TEST_AREA,
		// Token: 0x04002E55 RID: 11861
		KINGS_PASS,
		// Token: 0x04002E56 RID: 11862
		CLIFFS,
		// Token: 0x04002E57 RID: 11863
		TOWN,
		// Token: 0x04002E58 RID: 11864
		CROSSROADS,
		// Token: 0x04002E59 RID: 11865
		GREEN_PATH,
		// Token: 0x04002E5A RID: 11866
		ROYAL_GARDENS,
		// Token: 0x04002E5B RID: 11867
		FOG_CANYON,
		// Token: 0x04002E5C RID: 11868
		WASTES,
		// Token: 0x04002E5D RID: 11869
		DEEPNEST,
		// Token: 0x04002E5E RID: 11870
		HIVE,
		// Token: 0x04002E5F RID: 11871
		BONE_FOREST,
		// Token: 0x04002E60 RID: 11872
		PALACE_GROUNDS,
		// Token: 0x04002E61 RID: 11873
		MINES,
		// Token: 0x04002E62 RID: 11874
		RESTING_GROUNDS,
		// Token: 0x04002E63 RID: 11875
		CITY,
		// Token: 0x04002E64 RID: 11876
		DREAM_WORLD,
		// Token: 0x04002E65 RID: 11877
		COLOSSEUM,
		// Token: 0x04002E66 RID: 11878
		ABYSS,
		// Token: 0x04002E67 RID: 11879
		ROYAL_QUARTER,
		// Token: 0x04002E68 RID: 11880
		WHITE_PALACE,
		// Token: 0x04002E69 RID: 11881
		SHAMAN_TEMPLE,
		// Token: 0x04002E6A RID: 11882
		WATERWAYS,
		// Token: 0x04002E6B RID: 11883
		QUEENS_STATION,
		// Token: 0x04002E6C RID: 11884
		OUTSKIRTS,
		// Token: 0x04002E6D RID: 11885
		KINGS_STATION,
		// Token: 0x04002E6E RID: 11886
		MAGE_TOWER,
		// Token: 0x04002E6F RID: 11887
		TRAM_UPPER,
		// Token: 0x04002E70 RID: 11888
		TRAM_LOWER,
		// Token: 0x04002E71 RID: 11889
		FINAL_BOSS,
		// Token: 0x04002E72 RID: 11890
		SOUL_SOCIETY,
		// Token: 0x04002E73 RID: 11891
		ACID_LAKE,
		// Token: 0x04002E74 RID: 11892
		NOEYES_TEMPLE,
		// Token: 0x04002E75 RID: 11893
		MONOMON_ARCHIVE,
		// Token: 0x04002E76 RID: 11894
		MANTIS_VILLAGE,
		// Token: 0x04002E77 RID: 11895
		RUINED_TRAMWAY,
		// Token: 0x04002E78 RID: 11896
		DISTANT_VILLAGE,
		// Token: 0x04002E79 RID: 11897
		ABYSS_DEEP,
		// Token: 0x04002E7A RID: 11898
		ISMAS_GROVE,
		// Token: 0x04002E7B RID: 11899
		WYRMSKIN,
		// Token: 0x04002E7C RID: 11900
		LURIENS_TOWER,
		// Token: 0x04002E7D RID: 11901
		LOVE_TOWER,
		// Token: 0x04002E7E RID: 11902
		GLADE,
		// Token: 0x04002E7F RID: 11903
		BLUE_LAKE,
		// Token: 0x04002E80 RID: 11904
		PEAK,
		// Token: 0x04002E81 RID: 11905
		JONI_GRAVE,
		// Token: 0x04002E82 RID: 11906
		OVERGROWN_MOUND,
		// Token: 0x04002E83 RID: 11907
		CRYSTAL_MOUND,
		// Token: 0x04002E84 RID: 11908
		BEASTS_DEN
	}
	
	public static byte[] encGNU(String input) throws Exception {
		byte[] message = input.getBytes("UTF-8");
		
		IMode mode = ModeFactory.getInstance("ECB", "Rijndael", 16);
		
		Map attributes = new HashMap();		
	    attributes.put(IMode.KEY_MATERIAL, "UKu52ePUBwetZ9wNX88o54dnfKRu0T1l".getBytes("UTF-8"));
	    attributes.put(IMode.CIPHER_BLOCK_SIZE, new Integer(16));
	    attributes.put(IMode.STATE, new Integer(IMode.ENCRYPTION));
	    
	    mode.init(attributes);
	    
	    int bs = mode.currentBlockSize();
	    
	    IPad padding = PadFactory.getInstance("PKCS7");
	    padding.init(bs);
	    byte[] pad = padding.pad(message, 0, message.length);
	    byte[] pt = new byte[message.length + pad.length];
	    byte[] ct = new byte[pt.length];

	    System.arraycopy(message, 0, pt, 0, message.length);
	    System.arraycopy(pad, 0, pt, message.length, pad.length);
	     
	     for (int i = 0; i + bs <= pt.length; i += bs)
	        {
	           mode.update(pt, i, ct, i);
	        }
	     
		return Base64.getMimeEncoder().encode(ct);
	}
	
	public static byte[] decGNU(String input) throws Exception {
		byte[] message = input.getBytes();
	    byte[] tmp = Arrays.copyOfRange(message, 0, message.length-1);
	    String str = new String(tmp);
	    message = Base64.getMimeDecoder().decode(str);
		
		IMode mode = ModeFactory.getInstance("ECB", "Rijndael", 16);
		
		Map attributes = new HashMap();		
	    attributes.put(IMode.KEY_MATERIAL, "UKu52ePUBwetZ9wNX88o54dnfKRu0T1l".getBytes("UTF-8"));
	    attributes.put(IMode.CIPHER_BLOCK_SIZE, new Integer(16));
	    attributes.put(IMode.STATE, new Integer(IMode.DECRYPTION));
	    
	    mode.init(attributes);
	    
	    int bs = mode.currentBlockSize();
	    
	    IPad padding = PadFactory.getInstance("PKCS7");
	    padding.init(bs);
	    
	    byte[] pad = padding.pad(message, 0, message.length);
	    byte[] ct = new byte[message.length+pad.length];
	    byte[] cpt = new byte[message.length];
	    
	    System.arraycopy(message, 0, ct, 0, message.length);
	    System.arraycopy(pad, 0, ct, message.length, pad.length);
	     
	     for (int i = 0; i + bs < ct.length; i += bs)
	        {
	           mode.update(ct, i, cpt, i);
	        }
	     
	     int unpad = padding.unpad(cpt, 0, cpt.length);
	     byte[] output = new byte[cpt.length - unpad];
	     System.arraycopy(cpt, 0, output, 0, output.length);
	    
		return output;
	}
	
	
	public static byte[] encrypt(String input) throws Exception {
		
	    byte[] message = input.getBytes("UTF-8");
	    
	    SecretKeySpec sKey = new SecretKeySpec("UKu52ePUBwetZ9wNX88o54dnfKRu0T1l".getBytes("UTF-8"), "Rijndael");
	    final Cipher decipher = Cipher.getInstance("Rijndael/ECB/PKCS5Padding");
	    decipher.init(Cipher.ENCRYPT_MODE, sKey);
	    final byte[] enc = decipher.doFinal(message);
	    
	    
	    message = Base64.getMimeEncoder().encode(enc);
	
	    return message;
	}
	
	public static String decrypt(String input) throws Exception {
	
	    byte[] message = input.getBytes();
	    
	    byte[] tmp = Arrays.copyOfRange(message, 0, message.length-1);
	    
	    String str = new String(tmp);
	    
	    message = Base64.getMimeDecoder().decode(str);
	    
	    SecretKeySpec sKey = new SecretKeySpec("UKu52ePUBwetZ9wNX88o54dnfKRu0T1l".getBytes("UTF-8"), "Rijndael");
	    final Cipher decipher = Cipher.getInstance("Rijndael/ECB/PKCS5Padding");
	    decipher.init(Cipher.DECRYPT_MODE, sKey);
	    final byte[] plainText = decipher.doFinal(message);
	    
	    return new String(plainText, "UTF-8");
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
	
	public static byte[] getLength(byte[] input){
		String rawLengthBinary =  Integer.toBinaryString(input.length);
		String reverse = new StringBuffer(rawLengthBinary).reverse().toString();
		//System.out.println(reverse.length());
		String[] data = new String[(int) Math.ceil( ((double)reverse.length()) / 7.0f)];
		//System.out.println(data.length);
		for( int i = 0; i < data.length-1; i++){
			data[i] = reverse.subSequence(i*7, ((i+1)*7)).toString()+"1";
		}
		data[data.length-1] = reverse.subSequence((data.length-1)*7, reverse.length()).toString();
		byte[] output = new byte[data.length];
		for( int i = 0; i < data.length; i++){
			//System.out.println(data[i]);
			output[i] = (byte) Long.parseLong( new StringBuffer(data[i]).reverse().toString(), 2);
		}
		return output;
	}
	
	public static void saveSave(File dir, JsonObject json){
		SaveField.validateSaveData(json);
		SaveEditor.validateCharms(json.getAsJsonObject("playerData"));
		Gson gson = new GsonBuilder().create();
		String jsonString = gson.toJson(json);//.replaceAll("\\s","");
		jsonString = jsonString.substring(0, jsonString.length()-2)+"}}";
		//System.out.println(jsonString);
		try {
			//byte[] toSave = encrypt(jsonString);
			byte[] toSave = encGNU(jsonString);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static JsonObject loadSave(File dir) throws Exception {
		String str = new String(Files.readAllBytes(dir.toPath()),"UTF-8");
		//String json = new String(decrypt(str));
		String json = new String(decGNU(str), "UTF-8");
		//System.out.println(json);
		JsonObject jElement = new Gson().fromJson(json, JsonObject.class);
		return jElement;
	}
}
