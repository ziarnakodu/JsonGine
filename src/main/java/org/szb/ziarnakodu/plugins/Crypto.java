/*
 * Copyright (c) 2022-present, by Szymon Blachuta and Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package org.szb.ziarnakodu.plugins;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {
	protected final static String AES_KEY = "aLe0=juwzLtDD327"; //16 bytes
	
    private static Cipher mkAes(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        final byte[] aesKey = AES_KEY.getBytes(StandardCharsets.UTF_8);
        cipher.init(mode, new SecretKeySpec(aesKey, "AES"));
        return cipher;
    }
    
    public static String decryptAES(final String id) {
        String envId = System.getenv(id);
        if (envId==null) {
            envId = System.getProperty(id);
        }
		Cipher cipher=null;
		try {
			cipher = mkAes(Cipher.DECRYPT_MODE);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
			e.printStackTrace();
		}
        byte[] encryptedMsg=null;
		try {
			encryptedMsg = cipher.doFinal(Base64.getDecoder().decode(envId));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}
		return new String(encryptedMsg);
    }
    protected static String encrypt(final String txt) throws Exception {
        final Cipher cipher = mkAes(Cipher.ENCRYPT_MODE);
        final byte[] encryptedMsg = cipher.doFinal(txt.getBytes());
        return new String(Base64.getEncoder().encode(encryptedMsg));
    }
}
