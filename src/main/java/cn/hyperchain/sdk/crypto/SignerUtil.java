package cn.hyperchain.sdk.crypto;

import cn.hyperchain.sdk.common.utils.ByteUtil;
import cn.hyperchain.sdk.common.utils.Utils;
import cn.hyperchain.sdk.crypto.ecdsa.ECUtil;
import cn.hyperchain.sdk.crypto.sm.sm2.SM2Util;

public class SignerUtil {

    /**
     * verify signature by sdk.
     * @param sourceData source data
     * @param signature signature
     * @param publicKey public key
     * @return is legal
     */
    public static boolean verifySign(String sourceData, String signature, String publicKey) {
        return verifySign(sourceData.getBytes(Utils.DEFAULT_CHARSET), ByteUtil.fromHex(signature), ByteUtil.fromHex(publicKey));
    }

    /**
     * verify signature by sdk.
     * @param sourceData source data bytes
     * @param signature signature bytes
     * @param publicKey public key bytes
     * @return is legal
     */
    public static boolean verifySign(byte[] sourceData, byte[] signature, byte[] publicKey) {
        boolean flag = signature[0] == 0;
        int lenSig = signature.length;
        if (flag) {
            byte[] realSig = new byte[lenSig - 1];
            System.arraycopy(signature, 1, realSig, 0, lenSig - 1);
            return ECUtil.verify(sourceData, realSig, publicKey);
        } else {
            byte[] realSig = new byte[lenSig - 66];
            System.arraycopy(signature, 66, realSig, 0, lenSig - 66);
            return SM2Util.verify(sourceData, realSig, publicKey);
        }
    }
}
