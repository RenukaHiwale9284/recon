package com.anemoi.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.UUID;

public class FileCrypto 
	{
	
	public static void main(String[] argv) throws Exception
		{
		FileCrypto crypto = new FileCrypto();
		
		String key = "averageforfortax" ;
		File srcFile = new File( "/home/devzone/MyCenter/release/SET1/ITR6_structure_tags2.xlsx" ) ;
		File finalFile = new File( "/home/devzone/MyCenter/release/SET1/decrtest.xlsx" ) ;
		
		File encrFile = crypto.encr( srcFile, key ) ;
		System.out.print("\n File crypt ofile :" + encrFile.getAbsolutePath());
		
		File ofile = crypto.decr(encrFile, key, finalFile) ;
		System.out.print("\n File decrypt ofile :" + ofile.getAbsolutePath());

		}
	

	public File encr(File srcFile, String key) throws Exception
		{
		File ofile = new File(new UUID(System.currentTimeMillis(), 323032042L).toString() + ".bin") ;
		
		FileOutputStream ostream = new FileOutputStream(ofile);	

		CryptAlgo crypto = new CryptAlgo() ; 

		byte[] data = readContentIntoByteArray(srcFile);

		byte[] encrtext = crypto.encrypt(data, key.getBytes());
				
		ostream.write(encrtext);
		ostream.close();
		return ofile ;		
		}
	
	public File decr(File srcFile,  String key, File ofile) throws Exception {
			
		FileOutputStream ostream = new FileOutputStream(ofile);	

		DecryptAlgo decrypto = new DecryptAlgo() ; 

		byte[] data = readContentIntoByteArray(srcFile);
		byte[] text = decrypto.decrypt(data, key.getBytes());
				
		ostream.write(text);
		ostream.close();
		return ofile ;		
		}

	
	public void testCrypto()
		{
		String data = "This is the best life " ;
		String key = "chimbotingomingovenugopal" ;
		
		CryptAlgo crypto = new CryptAlgo() ; 
		DecryptAlgo decrypto = new DecryptAlgo() ; 
		
		byte[] encrtext = crypto.encrypt(data.getBytes(), key.getBytes());
		
		byte[] decrtext = decrypto.decrypt(encrtext, key.getBytes());
		
		System.out.print( new String(decrtext) );		
		}


	
	public byte[] reduceByteArraySize(byte[] data, int len)
		{	
		if(data.length != len)
			{
			byte[] reducedData = Arrays.copyOf(data, len) ;
			System.out.append("\nArray len change :: " + data.length + " -->" + reducedData.length);
			return reducedData;
			}
		
		return data ;
		}
	
	   private static byte[] readContentIntoByteArray(File file)
	   {
	      FileInputStream fileInputStream = null;
	      byte[] bFile = new byte[(int) file.length()];
	      try
	      {
	         //convert file into array of bytes
	         fileInputStream = new FileInputStream(file);
	         fileInputStream.read(bFile);
	         fileInputStream.close();
	         for (int i = 0; i < bFile.length; i++)
	         {
	            System.out.print((char) bFile[i]);
	         }
	      }
	      catch (Exception e)
	      {
	         e.printStackTrace();
	      }
	      return bFile;
	   }

	
	}
