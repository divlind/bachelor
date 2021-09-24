package lab2;

import java.security.MessageDigest;

public class Rijndael {
    private int blockLength = 0;
    private int keyLength = 0;
    private int Nb = 0, Nk = 0, Nr = 0;
    private String password;
    private boolean OFB = false;

    //look-up таблица для операции subBytes
    private int[] Sbox = {
                0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76,
                0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0,
                0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15,
                0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75,
                0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84,
                0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf,
                0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8,
                0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2,
                0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73,
                0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb,
                0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79,
                0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08,
                0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a,
                0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e,
                0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf,
                0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16
    };

    //look-up таблица для операции invSubBytes
    private int[] InvSbox = {
        0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb,
                0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb,
                0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e,
                0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25,
                0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92,
                0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84,
                0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06,
                0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b,
                0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73,
                0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e,
                0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b,
                0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4,
                0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f,
                0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef,
                0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61,
                0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d
    };

    //применяется при генерации раундовых ключей
    private int [][] Rcon = {
        {0x00, 0x00, 0x00, 0x00},
        {0x01, 0x00, 0x00, 0x00},
        {0x02, 0x00, 0x00, 0x00},
        {0x04, 0x00, 0x00, 0x00},
        {0x08, 0x00, 0x00, 0x00},
        {0x10, 0x00, 0x00, 0x00},
        {0x20, 0x00, 0x00, 0x00},
        {0x40, 0x00, 0x00, 0x00},
        {0x80, 0x00, 0x00, 0x00},
        {0x1b, 0x00, 0x00, 0x00},
        {0x36, 0x00, 0x00, 0x00}
    };



    Rijndael(int blockLength,int keyLength, String password, boolean ofb){
        this.blockLength = blockLength;
        this.keyLength = keyLength;
        this.password = password;
        this.OFB = ofb;

        this.Nb = blockLength/4;
        this.Nk = keyLength/4;

        //выбираем количество раундов
        switch (Nb)
        {
            case 4:
                Nr = Nb + Nk +2;
                break;
            case 6:
                Nr = Nk == 8? 14 : 12;
                break;
            case 8:
                Nr = 14;
                break;
        }
    }

    //Метод перестановки байт состояния по look-up таблице
    private int[][] subBytes(int[][] state){
        int[][] res = new int[4][Nb];
        for (int i =0; i<4; i++)
            for (int j = 0; j<Nb; j++)
            res[i][j] = Sbox[state[i][j]];
        return res;
    }

    // обратная операция subBytes
    private int[][] invSubBytes(int[][] state){
        int[][] res = new int[4][Nb];
        for (int i =0; i<4; i++)
            for (int j = 0; j<Nb; j++)
                res[i][j] = InvSbox[state[i][j]];
        return res;
    }

    // сдвиг строк состояния по следующему правилу:
//        {1,2,3,4},        {1,2,3,4},
//        {1,2,3,4},  -->   {2,3,4,1},
//        {1,2,3,4},        {3,4,1,2},
//        {1,2,3,4},        {4,1,3,2},

    private int[][] shiftRows(int[][] state){
        int[][] res = new int[4][Nb];
        for(int i =0; i<Nb; i++) { //столбцы
            res[0][i] = state[0][i];
            res[1][i] = state[1][(i + 1) % Nb];
            res[2][i] = state[2][(i + 2) % Nb];
            res[3][i] = state[3][(i + 3) % Nb];
        }
        return res;
    }

    // сдвиг строк состояния по следующему правилу:
//     {1,2,3,4},      {1,2,3,4},
//     {2,3,4,1},  --> {1,2,3,4},
//     {3,4,1,2},      {1,2,3,4},
//     {4,1,3,2},      {1,2,3,4},
    private int[][] invShiftRows(int[][]state){
        int[][] res = new int[4][Nb];
        for(int i = Nb-1; i > -1; i--) { //столбцы
            res[0][i] = state[0][i];
            res[1][i] = state[1][(i+(Nb-1))%Nb];//(1+(Nb - i)) % Nb];
            res[2][i] = state[2][(i+(Nb-2))%Nb];
            res[3][i] = state[3][(i+(Nb-3))%Nb];
        }
        return res;
    }

    // Каждая колонка состояния трактуется как полином третьей степени.Над этими полиномами производится умножение в поле Галоиса
    // по модулю x^{4}+1 на фиксированный многочлен c(x)=3x^{3}+x^{2}+x+2
    public int[][] mixColumns(int[][] state){
        int[][] res = new int[4][Nb];

        for(int i =0; i<Nb; i++) { //столбцы
            res[0][i] = (Galois.multiply((byte)2, (byte)state[0][i]) ^ Galois.multiply((byte)3,(byte)state[1][i]) ^ (byte)state[2][i] ^ (byte)state[3][i]) & 0xFF;
            res[1][i] = ((byte)state[0][i] ^ Galois.multiply((byte)2, (byte)state[1][i]) ^ Galois.multiply((byte)3, (byte)state[2][i]) ^ (byte)state[3][i]) & 0xFF;
            res[2][i] = ((byte)state[0][i] ^ (byte)state[1][i] ^ Galois.multiply((byte)2,(byte)state[2][i]) ^ Galois.multiply((byte)3,(byte)state[3][i])) & 0xFF;
            res[3][i] = (Galois.multiply((byte)3,(byte)state[0][i]) ^ (byte)state[1][i] ^ (byte)state[2][i] ^ Galois.multiply((byte)2,(byte)state[3][i])) & 0xFF;
        }
        return res;
    }

    // обратная операция mixColumns
    public int[][] invMixColumns(int[][] state){
        int[][] res = new int[4][Nb];

        for(int i =0; i<Nb; i++) { //столбцы
            res[0][i] = (Galois.multiply((byte)0x0e, (byte)state[0][i]) ^ Galois.multiply((byte)0x0b,(byte)state[1][i]) ^
                            Galois.multiply((byte)0x0d, (byte)state[2][i]) ^ Galois.multiply((byte)0x09,(byte)state[3][i])) & 0xFF;
            res[1][i] = (Galois.multiply((byte)0x09, (byte)state[0][i]) ^ Galois.multiply((byte)0x0e,(byte)state[1][i]) ^
                            Galois.multiply((byte)0x0b, (byte)state[2][i]) ^ Galois.multiply((byte)0x0d,(byte)state[3][i])) & 0xFF;
            res[2][i] = (Galois.multiply((byte)0x0d, (byte)state[0][i]) ^ Galois.multiply((byte)0x09,(byte)state[1][i]) ^
                            Galois.multiply((byte)0x0e, (byte)state[2][i]) ^ Galois.multiply((byte)0x0b,(byte)state[3][i])) & 0xFF;
            res[3][i] = (Galois.multiply((byte)0x0b, (byte)state[0][i]) ^ Galois.multiply((byte)0x0d,(byte)state[1][i]) ^
                            Galois.multiply((byte)0x09, (byte)state[2][i]) ^ Galois.multiply((byte)0x0e,(byte)state[3][i])) & 0xFF;
        }
        return res;
    }

    //скалдываем каждый элемент состояния с соответствующим ему элементом раундового ключа
    private int[][] addRoundKey(int[][] state, int[][] roundKey, int round){
        int[][] res = new int[4][Nb];
        for(int i =0; i<Nb; i++) //столбцы
            for(int j=0; j<4;j++)
                res[j][i] = state[j][i] ^ roundKey[j][round*4+i];
        return res;
    }

    //генерируем cipher key используя MD5hash
    private int[][] generateKey(String word){
        byte[] passwordBytes = word.getBytes();
        int[][] key = new int[4][Nk];

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        }catch (Exception e){}

        byte[] mdPass = md.digest(passwordBytes);

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < Nk; j++)
                key[i][j] = mdPass[(i+4*j)%16] & 0xFF;

        return key;
    }

    //Метод генерации раундовых ключей
    private int[][] keyExpansion(int[][] key){
        //содержи в себе ключ шифрования[0] и Nr раунд ключей[1..Nr]
        int[][] schedule = new int[4][(Nr+1)*Nk];

        //копируем ключ шифрования
        for(int i = 0; i<4; i++)
            for(int j = 0; j<Nk; j++)
                schedule[i][j]  = key[i][j];

        int curRound = 1; //[1, Nr]
        while (true) {
            if(curRound > Nr)
                break;

            // i1 = rotate(i Col)
            // i2 = subWord(i1)
            // newColumn  = (i - 4) ^ i2 ^ rcon(i)
            for (int i = 0; i < 4; i++) {

                int[] rotatedWord = rotWord(new int[]{schedule[0][curRound*4 + i - 1], schedule[1][curRound*4 + i - 1],
                                                    schedule[2][curRound*4 + i - 1], schedule[3][curRound*4 + i - 1]});
                int[] subbed = subWord(rotatedWord);

                int[] jMinusFour = new int[]{schedule[0][curRound*4 + i - 4], schedule[1][curRound*4 + i - 4],
                                                    schedule[2][curRound*4 + i - 4], schedule[3][curRound*4 + i - 4]};

                //bitwise xor and write to schedule
                int[] res = new int[4];
                for (int j = 0; j < 4; j++)
                    schedule[j][curRound*4+i] = jMinusFour[j] ^ subbed[j] ^ Rcon[curRound][j];
            }
            curRound++;
        }
        return schedule;
    }

    //циклический сдвиг колонок, применяется только в расписаниии
    private int[] rotWord(int[] word){
        int[] res = new int[word.length];
        for (int i = 0; i <3; i++){
            res[i] = word[i+1];
        }
        res[word.length-1] = word[0];
        return res;
    }
    private int[] subWord(int[] word){
        int[] res = new int[4];
        for (int i =0; i<4; i++)
            res[i] = Sbox[word[i]];
        return res;
    }

    private int[][] xorArrays(int[][]a,int[][]b)
    {
        int[][] res = new int[4][Nb];
        for(int i = 0; i<4; i++)
            for (int j = 0;j<4;j++)
                res[i][j] = a[i][j] ^ b[i][j];
        return res;
    }

    //метод основной части шифрования Aes
    private int[][] encryptBlock(int[][] stata, int[][]schedule) {
        int[][] state = new int[4][Nb];
        // начальный раунд
        state = addRoundKey(stata, schedule, 0);

        // основные раунды
        for(int i = 1; i <= Nr - 1 ; i++){
            state = subBytes(state);
            state = shiftRows(state);
            state = mixColumns(state);
            state = addRoundKey(state, schedule, i);
        }

        //финальный раунд
        state = subBytes(state);
        state = shiftRows(state);
        state = addRoundKey(state, schedule, Nr);

        return state;
    }

    public String encrypt(String text){
        int[][] state = new int[4][Nb];
        int[][] ofb_state = new int[4][Nb];
        int[][] text_block = new int[4][Nb];
        String paddedText = "";
        String result = " ";

        if(this.OFB)
            ofb_state = generateKey("OFB");

        paddedText = makePadding(text);
        result = String.format("%"+paddedText.length()+"s",result);
        StringBuilder stringBuilder = new StringBuilder(result);

        int[][] key = generateKey(this.password);
        int[][] schedule = keyExpansion(key);

        int k = 0;
        while(true){ // блоки
            if(k == paddedText.length() / blockLength)
                break;

            //заполняем text
            for(int i = 0; i < 4; i++)
                for (int j = 0; j < Nb; j++)
                    text_block[i][j] = paddedText.charAt(k*blockLength+Nb*i+j);

            if(this.OFB)
                state = ofb_state;
            else
                state = text_block;

            state = encryptBlock(state, schedule);
            ofb_state = state;

            int[][] tmp_state = state;
            if(this.OFB) {
                tmp_state = xorArrays(state, text_block);
            }

            for(int i = 0; i < 4; i++)
                for (int j = 0; j < Nb; j++)
                    stringBuilder.setCharAt(k*blockLength+Nb*i+j,(char)tmp_state[i][j]);

            k++;
        }
        return stringBuilder.toString();
    }

    public String decrypt(String text){
        int[][] state = new int[4][Nb];

        int[][] ofb_state = new int[4][Nb];
        int[][] text_block = new int[4][Nb];

        if(this.OFB)
            ofb_state = generateKey("OFB");

        //генерим основной и раундовые ключи
        int[][] key = generateKey(this.password);
        int[][] schedule = keyExpansion(key);

        String result = " ";
        result = String.format("%"+text.length()+"s",result);
        StringBuilder stringBuilder = new StringBuilder(result);

        int k = 0;
        while(true){ // блоки
            if(k == text.length() / blockLength)
                break;

            //заполняем text
            for(int i = 0; i < 4; i++)
                for (int j = 0; j < Nb; j++)
                    text_block[i][j] = text.charAt(k*blockLength+Nb *i+j);

            if(this.OFB) {
                //в случае ОФБ нам необходимо заново зашифровать IV и xor его с криптограммой
                state = ofb_state;
                state = encryptBlock(state,schedule);
            }
            else {
                state = text_block;

                // начальный раунд
                state = addRoundKey(state, schedule, Nr);


                // основные раунды
                for (int i = Nr - 1; i >= 1; i--) {
                    state = invShiftRows(state);
                    state = invSubBytes(state);
                    state = addRoundKey(state, schedule, i);
                    state = invMixColumns(state);
                }

                //финальный раунд
                state = invShiftRows(state);
                state = invSubBytes(state);
                state = addRoundKey(state, schedule, 0);
            }

            int[][] tmp_state = state;
            if(this.OFB) {
                ofb_state = state;
                tmp_state = xorArrays(state, text_block);
            }

            for(int i = 0; i < 4; i++)
                for (int j = 0; j < Nb; j++)
                    stringBuilder.setCharAt(k*blockLength+Nb*i+j,(char)tmp_state[i][j]);
            k++;
        }

        return stringBuilder.toString();
    }

    // добавляем padding
    public String makePadding(String text){
        String result = text;
        int charsToAdd = 0;
        int amountOfBlocks = (int)result.length() / blockLength + ( result.length()%blockLength == 0 ? 0:1);
        System.out.println(amountOfBlocks);

        charsToAdd = blockLength*amountOfBlocks - result.length();


        result += Util.getRandomString(charsToAdd);
        return result;
    }

}
