package org.tonyhsu17.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;



public class FileUtils {
    /**
     * Calculates the CRC32 value of a file.
     *
     * @param file File to compute the CRC value
     * @return CRC value formatted in 8 length hexadecimal in lowercase
     */
    public static String getCRC32(File file) throws IOException
    {
        return getCRC32(file, 8);
    }

    /**
     * Calculates the CRC32 value of a file.
     *
     * @param file File to compute the CRC value
     * @param formatLength CRC value formatted to length
     * @return CRC value formatted in n length, hexadecimal in lowercase
     */
    public static String getCRC32(File file, int formatLength) throws IOException
    {
        if(file == null)
        {
            return null;
        }
        String hex = "";
        try
        {
            CheckedInputStream cis = new CheckedInputStream(new FileInputStream(file), new CRC32());
            byte[] buf = new byte[10240]; // 10mb

            while(cis.read(buf) >= 0);

            hex = Long.toHexString(cis.getChecksum().getValue());
            cis.close();
        }
        catch (IOException | NullPointerException e)
        {
            throw new IOException("Unable to determine CRC32 value for file: " + file.getName());
        }
        for(int i = hex.length(); i < formatLength; i++)
        {
            hex = "0" + hex;
        }
        return hex;
    }
}
