package com.anemoi.security;

public class CryptAlgo extends BaseSecurityConfiguration {

	private byte[][] K;
	private byte[][] K1;
	private byte[][] K2;
	
	public byte[] encrypt(byte[] data, byte[] key) {
		int lenght = 0;
		byte[] padding = new byte[1];
		int i;
		lenght = 8 - data.length % 8;
		padding = new byte[lenght];
		padding[0] = (byte) 0x80;

		for (i = 1; i < lenght; i++)
			padding[i] = 0;

		byte[] tmp = new byte[data.length + lenght];
		byte[] bloc = new byte[8];

		K = generateSubKeys(key);

		int count = 0;

		for (i = 0; i < data.length + lenght; i++) {
			if (i > 0 && i % 8 == 0) {
				bloc = encrypt64Bloc(bloc, K, false);
				System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
			}
			if (i < data.length)
				bloc[i % 8] = data[i];
			else {
				bloc[i % 8] = padding[count % 8];
				count++;
			}
		}
		if (bloc.length == 8) {
			bloc = encrypt64Bloc(bloc, K, false);
			System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
		}
		return tmp;
	}

	public byte[] TripleDES_Encrypt(byte[] data, byte[][] keys) {
		int lenght = 0;
		byte[] padding = new byte[1];
		int i;

		lenght = 8 - data.length % 8;
		padding = new byte[lenght];
		padding[0] = (byte) 0x80;

		for (i = 1; i < lenght; i++)
			padding[i] = 0;

		byte[] tmp = new byte[data.length + lenght];
		byte[] bloc = new byte[8];

		K = generateSubKeys(keys[0]);
		K1 = generateSubKeys(keys[1]);
		K2 = generateSubKeys(keys[2]);

		int count = 0;

		for (i = 0; i < data.length + lenght; i++) {
			if (i > 0 && i % 8 == 0) {
				bloc = encrypt64Bloc(bloc, K, false);
				bloc = encrypt64Bloc(bloc, K1, true);
				bloc = encrypt64Bloc(bloc, K2, false);
				System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
			}
			if (i < data.length)
				bloc[i % 8] = data[i];
			else {
				bloc[i % 8] = padding[count % 8];
				count++;
			}
		}
		if (bloc.length == 8) {
			bloc = encrypt64Bloc(bloc, K, false);
			bloc = encrypt64Bloc(bloc, K1, true);
			bloc = encrypt64Bloc(bloc, K2, false);
			System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
		}
		return tmp;
	}


}
