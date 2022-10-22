package ua.shpp.eqbot.service;


import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class ImageService {

    final EqTelegramBot bot;
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);
    public static final int IMAGE_MAX_WIDTH = 100;
    public static final int TIMEOUT = 5000;

    public ImageService(EqTelegramBot bot) {
        this.bot = bot;
    }

    public PhotoSize getBiggestImageSmallerThan(List<PhotoSize> photos, int sizeLimit) {
        return photos.stream()
                .filter(x -> x.getWidth() < sizeLimit)
                .filter(x -> x.getHeight() < sizeLimit)
                .max(Comparator.comparing(PhotoSize::getFileSize))
                .orElse(null);
    }

    public PhotoSize getBiggestImage(List<PhotoSize> photos) {
        return photos.stream().max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
    }

    @Value("${aws.id}")
    private String awsId;

    @Value("${aws.access.key}")
    private String awsAccessKey;

    @Value("${aws.region}")
    private String awsRegion;

    @Value("${aws.s3.bucket_name}")
    private String s3BucketName;

    private AmazonS3 setupAWS() {
        AWSCredentials credentials = new BasicAWSCredentials(awsId, awsAccessKey);
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsRegion).build();
        if (!s3client.doesBucketExistV2(s3BucketName)) {
            LOGGER.warn("Bucket with such name is not exists");
            return null;
        }
        return s3client;
    }

    public boolean sendImageToAWS(InputStream inputStream, String path) {
        AmazonS3 s3Client = setupAWS();
        if (s3Client == null) {
            return false;
        }
        PutObjectResult putObjectResult = s3Client.putObject(s3BucketName, "images/" + path, inputStream, new ObjectMetadata());
        return putObjectResult.isRequesterCharged();
    }

    public SendPhoto sendImageFromAWS(String telegramId, String serviceName) {
        AmazonS3 s3Client = setupAWS();
        if (s3Client == null) {
            return null;
        }
        String filePath = "images/" + telegramId + "/" + serviceName;
        boolean isExists = s3Client.doesObjectExist(s3BucketName, filePath);
        SendPhoto sendPhoto = null;
        if (isExists) {
            S3Object s3Object = s3Client.getObject(new GetObjectRequest(s3BucketName, filePath));
            sendPhoto = sendImage(s3Object.getObjectContent(), serviceName);
            LOGGER.info("Getting image from AWS");
        }
        return sendPhoto;
    }

    public List<String> listObjectsInS3Folder(String folderPath) {
        List<String> list = new ArrayList<>();
        AmazonS3 s3Client = setupAWS();
        if (s3Client == null) {
            return list;
        }
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(s3BucketName)
                .withPrefix(folderPath + "/");
        ObjectListing objects = s3Client.listObjects(listObjectsRequest);
        for (;;) {
            List<S3ObjectSummary> summaries = objects.getObjectSummaries();
            if (summaries.isEmpty()) {
                break;
            }

            summaries.forEach(s -> list.add(s.getKey()));
            objects = s3Client.listNextBatchOfObjects(objects);
        }
        return list;
    }

    public byte[] getArrayOfLogo(List<PhotoSize> photos) {
        try (InputStream is = photoToStream(getBiggestImageSmallerThan(photos, IMAGE_MAX_WIDTH))) {
            return is.readAllBytes();
        } catch (IOException e) {
            LOGGER.warn("Can`t convert logo to byte array", e);
            return new byte[0];
        }
    }

    public boolean sendBigImageToAWS(List<PhotoSize> photos, String path) {
        InputStream is = photoToStream(getBiggestImage(photos));
        if (is == null) {
            LOGGER.warn("Can`t send image to AWS S3 because input stream is null");
            return false;
        }
        LOGGER.info("Sending image to AWS");
        return sendImageToAWS(is, path);
    }

    public InputStream photoToStream(PhotoSize photo) {
        if (photo == null) {
            LOGGER.warn("Photo must not be null");
            return null;
        }
        GetFile getFile = new GetFile();
        getFile.setFileId(photo.getFileId());
        org.telegram.telegrambots.meta.api.objects.File file;
        try {
            file = bot.execute(getFile);
        } catch (TelegramApiException e) {
            LOGGER.warn("Can`t get file from bot", e);
            return null;
        }
        URL url;
        try {
            url = new URL(file.getFileUrl(bot.getBotToken()));
        } catch (MalformedURLException e) {
            LOGGER.warn("Can't create URL", e);
            return null;
        }
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            LOGGER.warn("Can`t open connection", e);
            return null;
        }
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            LOGGER.warn("Can`t make request", e);
        }
        conn.setConnectTimeout(TIMEOUT);

        try {
            return conn.getInputStream();
        } catch (IOException e) {
            LOGGER.warn("Can`t get input stream", e);
            return null;
        }

    }

    public Message sendImageFromDB(byte[] image, String chatId, String imageName) {
        SendPhoto img = new SendPhoto();
        img.setChatId(chatId);
        img.setPhoto(new InputFile(new ByteArrayInputStream(image), imageName));
        try {
            Message message = bot.execute(img);
            LOGGER.info("Sending image from byte array");
            return message;
        } catch (TelegramApiException e) {
            LOGGER.warn("Can`t send image", e);
            return null;
        }
    }

    public SendPhoto sendImage(InputStream inputStream, String imageName) {
        SendPhoto img = new SendPhoto();
        img.setPhoto(new InputFile(inputStream, imageName));
        return img;
    }

    public List<Message> sendMediaGroup(String folderPath) {
        SendMediaGroup message = new SendMediaGroup();
        List<InputMedia> photos = new ArrayList<>();
        List<String> photosPath = listObjectsInS3Folder(folderPath);
        for (String path : photosPath) {
            photos.add(new InputMediaPhoto(path));
        }
        message.setMedias(photos);
        try {
            return bot.execute(message);
        } catch (TelegramApiException e) {
            LOGGER.warn("Can`t send media group", e);
            return new ArrayList<>();
        }
    }
}
