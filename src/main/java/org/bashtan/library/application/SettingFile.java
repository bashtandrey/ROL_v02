package org.bashtan.library.application;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SettingFile {
    private final File fileHibernateProperties;
    private final File fileSecretKey;
    private String SECRET_KEY;

    public SettingFile(File fileSecretKey, File fileHibernateProperties) {
        this.fileHibernateProperties = fileHibernateProperties;
        this.fileSecretKey = fileSecretKey;
        if(RunLibrary.flagFileSecretKey)
            readFileSecretKey();
    }

    public boolean createFileSecretKey(String string) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(fileSecretKey)) {
            fileOutputStream.write(string.getBytes());
        } catch (IOException exception) {
            return false;
        }
        return true;
    }

    public void readFileSecretKey() {
             try (FileInputStream fileInputStream = new FileInputStream(fileSecretKey)) {
                 SECRET_KEY = new String(fileInputStream.readAllBytes(), StandardCharsets.UTF_8);
             } catch (IOException exception) {
             }
    }
    public boolean creatFileHibernateProperties(Object object) {
        try {
            byte[] encryptedObject = encryptObject(object); // Шифрование объекта

            try (FileOutputStream fileOutputStream = new FileOutputStream(fileHibernateProperties)) {
                fileOutputStream.write(encryptedObject); // Запись зашифрованных данных в файл
            } catch (IOException e) {
                return false;            }
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException |
                 BadPaddingException | IOException e) {
            return false;
        }
        return true;
    }

    public Object readFileHibernateProperties() {
        try {
            byte[] encryptedData = readFromFile(fileHibernateProperties); // Чтение зашифрованных данных из файла
            return decryptObject(encryptedData); // Дешифровка данных и восстановление объекта
        } catch (IOException | ClassNotFoundException | NoSuchPaddingException | NoSuchAlgorithmException |
                 InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            return null;
        }
    }
    private byte[] readFromFile(File file) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(file);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }
    private byte[] encryptObject(Object object) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        Cipher cipher = Cipher.getInstance("AES"); // Выбор алгоритма шифрования (AES)

        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES"); // Создание ключа для шифрования
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec); // Инициализация шифрования

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
            objectOutputStream.writeObject(object); // Сериализация объекта
            byte[] objectBytes = byteArrayOutputStream.toByteArray(); // Преобразование объекта в массив байтов

            return cipher.doFinal(objectBytes); // Шифрование сериализованных данных объекта
        }
    }
    private Object decryptObject ( byte[] encryptedData) throws
            NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IOException, ClassNotFoundException, IllegalBlockSizeException, BadPaddingException
    {
        Cipher cipher = Cipher.getInstance("AES"); // Выбор алгоритма шифрования (AES)

        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "AES"); // Создание ключа для расшифровки
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec); // Инициализация расшифровки

        byte[] decryptedObjectBytes = cipher.doFinal(encryptedData); // Расшифровка данных

        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decryptedObjectBytes);
             ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            return objectInputStream.readObject();  // Десериализация объекта
        }
    }
}