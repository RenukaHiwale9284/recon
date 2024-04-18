package com.anemoi.security;

public class DecryptAlgo extends BaseSecurityConfiguration {

	private byte[][] K;
	private byte[][] K1;
	private byte[][] K2;
		
	public byte[] TripleDES_Decrypt(byte[] data, byte[][] keys) {
		int i;
		byte[] tmp = new byte[data.length];
		byte[] bloc = new byte[8];

		K = generateSubKeys(keys[0]);
		K1 = generateSubKeys(keys[1]);
		K2 = generateSubKeys(keys[2]);

		for (i = 0; i < data.length; i++) {
			if (i > 0 && i % 8 == 0) {
				bloc = encrypt64Bloc(bloc, K2, true);
				bloc = encrypt64Bloc(bloc, K1, false);
				bloc = encrypt64Bloc(bloc, K, true);
				System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
			}
			if (i < data.length)
				bloc[i % 8] = data[i];
		}
		bloc = encrypt64Bloc(bloc, K2, true);
		bloc = encrypt64Bloc(bloc, K1, false);
		bloc = encrypt64Bloc(bloc, K, true);
		System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);

		tmp = deletePadding(tmp);

		return tmp;
	}

	public byte[] decrypt(byte[] data, byte[] key) {
		int i;
		byte[] tmp = new byte[data.length];
		byte[] bloc = new byte[8];

		K = generateSubKeys(key);

		for (i = 0; i < data.length; i++) {
			if (i > 0 && i % 8 == 0) {
				bloc = encrypt64Bloc(bloc, K, true);
				System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);
			}
			if (i < data.length)
				bloc[i % 8] = data[i];
		}
		bloc = encrypt64Bloc(bloc, K, true);
		System.arraycopy(bloc, 0, tmp, i - 8, bloc.length);

		tmp = deletePadding(tmp);

		return tmp;
	}

	
}
