package lib;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;

public class CharHelper {
    public static CharBuffer decode(ByteBuffer byteBuffer){
        CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder();
        try {
            return decoder.decode(byteBuffer);
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ByteBuffer encode(CharBuffer charBuffer){
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        try {
            return encoder.encode(charBuffer);
        } catch (CharacterCodingException e) {
            e.printStackTrace();
        }
        return null;
    };
}
