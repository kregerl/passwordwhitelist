package com.loucaskreger.passwordwhitelist.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import com.loucaskreger.passwordwhitelist.PasswordWhitelist;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraftforge.fml.loading.FMLPaths;

public class PasswordManager {

	private static final Path path = FMLPaths.GAMEDIR.get().resolve("password.nbt");
	private static final String ALGORITHM = "PBKDF2WithHmacSHA1";
	private static final String HASH_KEY = "hash";
	private static final int SALT_SIZE = 16;
	private static final int HASH_SIZE = 128;
	private static final int ITERATIONS = 65536;

	public static PasswordManager INSTANCE;

	private File file;

	public static void init() {
		INSTANCE = new PasswordManager();
	}

	public PasswordManager() {
		this.file = path.toFile();
		try {
			this.file.createNewFile();
		} catch (IOException e) {
			PasswordWhitelist.LOGGER.error(e);
		}
	}

	public String createHash(String password) {
		SecureRandom rand = new SecureRandom();
		byte[] salt = new byte[SALT_SIZE];
		rand.nextBytes(salt);
		return this.toHex(salt) + ":" + this.toHex(this.genHash(password.toCharArray(), salt));
	}

	private byte[] genHash(char[] password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_SIZE);
		SecretKeyFactory factory;
		try {
			factory = SecretKeyFactory.getInstance(ALGORITHM);
			return factory.generateSecret(spec).getEncoded();
		} catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
			PasswordWhitelist.LOGGER.error(e);
		}
		return new byte[0];
	}

	private String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0)
			return String.format("%0" + paddingLength + "d", 0) + hex;
		else
			return hex;
	}

	private byte[] fromHex(String hex) {
		byte[] binary = new byte[hex.length() / 2];
		for (int i = 0; i < binary.length; i++) {
			binary[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
		}
		return binary;
	}

	public void writeHash(String password) {
		String hash = this.createHash(password);
		CompoundNBT nbt = new CompoundNBT();
		nbt.putString(HASH_KEY, hash);
		FileOutputStream fileoutputstream;
		try {
			fileoutputstream = new FileOutputStream(this.file);
			CompressedStreamTools.writeCompressed(nbt, fileoutputstream);
			fileoutputstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String readHash() {
		if (this.file.length() != 0) {
			try {
				FileInputStream fs = new FileInputStream(this.file);
				CompoundNBT nbt = CompressedStreamTools.readCompressed(fs);
				fs.close();
				return nbt.getString(HASH_KEY);
			} catch (IOException e) {
				PasswordWhitelist.LOGGER.error(e);
			}
		}
		return "";
	}

	private boolean areHashesEqual(byte[] hash, byte[] passHash) {
		int diff = hash.length ^ passHash.length;
		for (int i = 0; i < hash.length && i < passHash.length; i++) {
			diff |= hash[i] ^ passHash[i];
		}
		return diff == 0;
	}

	public boolean matches(String password) {
		String[] params = this.readHash().split(":");
		byte[] salt = this.fromHex(params[0]);
		byte[] hash = this.fromHex(params[1]);

		byte[] passHash = genHash(password.toCharArray(), salt);

		return this.areHashesEqual(hash, passHash);
	}

}
