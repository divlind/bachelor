package lab1;

import org.apache.commons.cli.*;
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

        Option toLeft = new Option("l", "left", false, "direction of rotation");
        toLeft.setRequired(false);
        options.addOption(toLeft);

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);

            String inputFile = cmd.getOptionValue("source");
            String encryptedFile = cmd.getOptionValue("encrypted");
            String decryptedFile = cmd.getOptionValue("decrypted");


            //шифруем
            CardanGrille encryptor = new CardanGrille(cmd.hasOption("left"));
            String contents = new String(Files.readAllBytes(Paths.get(inputFile)));
            String encryptedText = encryptor.encrypt(contents);

            Files.write(Paths.get(encryptedFile), encryptedText.getBytes("utf-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

            //дешифруем
            String decryptedText = encryptor.decrypt(encryptedText);
            Files.write(Paths.get(decryptedFile), decryptedText.getBytes("utf-8"), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (ParseException | IOException e) {
            System.out.println(e.getMessage());

            System.exit(1);
        }
    }


}
