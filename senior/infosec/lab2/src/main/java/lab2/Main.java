package lab2;

import org.apache.commons.cli.*;
import org.apache.commons.codec.*;
import org.apache.commons.codec.binary.Hex;

import java.io.IOException;
import java.nio.file.StandardOpenOption;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args){
        Options options = new Options();

        Option input = new Option("s", "source", true, "input text file path");
        input.setRequired(true);
        options.addOption(input);

        Option encrypted = new Option("e", "encrypted", true, "file path to encrypted text");
        encrypted.setRequired(true);
        options.addOption(encrypted);

        Option decrypted = new Option("d", "decrypted", true, "file path to decrypted text");
        decrypted.setRequired(true);
        options.addOption(decrypted);

        Option blockLength = new Option("bl", "blocklength", true, "size of block, Bite");
        blockLength.setRequired(true);
        options.addOption(blockLength);

        Option keyLength = new Option("kl", "keylength", true, "size of key, Bite");
        keyLength.setRequired(true);
        options.addOption(keyLength);

        Option password = new Option("p", "password", true, "used in key generator");
        password.setRequired(true);
        options.addOption(password);

        Option ofb = new Option("ofb", "output-feedback", false, "sets encryption mode to ofb");
        ofb.setRequired(false);
        options.addOption(ofb);


        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            String inputFile = cmd.getOptionValue("source");
            String encryptedFile = cmd.getOptionValue("encrypted");
            String decryptedFile = cmd.getOptionValue("decrypted");
            String pass = cmd.getOptionValue("password");

            int blocksize = Integer.parseInt(cmd.getOptionValue("blocklength"));
            int keysize = Integer.parseInt(cmd.getOptionValue("keylength"));

            if((blocksize != 16 && blocksize != 24 && blocksize != 32) ||
                    keysize != 16 && keysize != 24 && keysize != 32)
            {
                System.out.println("Invalid length of key or block");
                System.exit(1);
            }

//            Files.write(Paths.get("text.txt"), Util.getRandomString(500000).getBytes("ascii"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

//            шифруем
            Rijndael rijndael = new Rijndael(blocksize, keysize, pass, cmd.hasOption("output-feedback"));

            String contents = new String(Files.readAllBytes(Paths.get(inputFile)));

            long time = System.currentTimeMillis();
            String encryptedText = rijndael.encrypt(contents);
            System.out.println("Encryption time:"+ (System.currentTimeMillis() - time));

            Files.write(Paths.get(encryptedFile), encryptedText.getBytes("ascii"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            //дешифруем
            time = System.currentTimeMillis();
            String decryptedText = rijndael.decrypt(encryptedText);
            System.out.println("Decryption time:"+ (System.currentTimeMillis() - time));
            Files.write(Paths.get(decryptedFile), decryptedText.getBytes("utf-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

        } catch (ParseException | IOException e) {
            System.out.println(e.getMessage());

            System.exit(1);
        }

    }
}

