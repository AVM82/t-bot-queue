package ua.shpp.eqbot.service;


import com.amazonaws.auth.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;


import java.io.*;
import java.net.*;
import java.util.Comparator;
import java.util.List;

@Component
public class ImageService {

    @Autowired
    EqTelegramBot bot;

    Logger log = LoggerFactory.getLogger(ImageService.class);
    public final int IMAGE_MAX_WIDTH = 100;
    public static final int TIMEOUT = 5000;

    public PhotoSize getBiggestImageSmallerThan (List<PhotoSize> photos, int sizeLimit){
        return photos.stream().filter(x -> x.getWidth()<sizeLimit).filter(x->x.getHeight()<sizeLimit)
                .max(Comparator.comparing(PhotoSize::getFileSize)).orElse(null);
    }

    public PhotoSize getBiggestImage(List<PhotoSize> photos,){
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

    private AmazonS3 setupAWS(){
        AWSCredentials credentials = new BasicAWSCredentials(awsId, awsAccessKey);
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(awsRegion)
                .build();
        if(!s3client.doesBucketExistV2(s3BucketName)){
            log.warn("Bucket with such name is not exists");
            return null;
        }
        return s3client;
    }

    public boolean sendImageToAWS(InputStream inputStream, String path){
        AmazonS3 s3Client = setupAWS();
        if(s3Client==null){
            return false;
        }
        PutObjectResult putObjectResult = s3Client.putObject(s3BucketName, "images/"+path, inputStream, new ObjectMetadata());
        return true;
    }

    public InputStream getImageFromAWS(String fileName){
        AmazonS3 s3Client = setupAWS();
        if(s3Client==null){
            return null;
        }
        S3Object s3Object = s3Client.getObject(new GetObjectRequest(s3BucketName, "images/1"));
        return s3Object.getObjectContent();
    }

    public byte[] getArrayOfLogo(List<PhotoSize> photos){
        try(InputStream is = photoToStream(getBiggestImageSmallerThan(photos, IMAGE_MAX_WIDTH))) {
            return is.readAllBytes();
        } catch (IOException e) {
            return null;
        }
    }

    public boolean sendBigImageToAWS(List<PhotoSize> photos, String path){

    }

    public InputStream photoToStream(PhotoSize photo){
        GetFile getFile = new GetFile();
        getFile.setFileId(photo.getFileId());
        org.telegram.telegrambots.meta.api.objects.File file = null;
        try {
            file = bot.execute(getFile);
        } catch (TelegramApiException e) {
            log.warn("Can`t get file from bot", e);
            return null;
        }
        URL url = null;
        try {
            url = new URL(file.getFileUrl(bot.getBotToken()));
        } catch (MalformedURLException e) {
            log.warn("Can't create URL", e);
            return null;
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            log.warn("Can`t open connection", e);
            return null;
        }
        try {
            conn.setRequestMethod("GET");
        } catch (ProtocolException e) {
            log.warn("Can`t make request", e);
        }
        conn.setConnectTimeout(TIMEOUT);
        InputStream inStream = null;
        try {
            return  conn.getInputStream();
        } catch (IOException e) {
            log.warn("Can`t get input stream", e);
            return null;
        }

    }

    public Message sendImage(byte[] image, String chatId, String imageName){
        SendPhoto img = new SendPhoto();
        img.setChatId(chatId);
        img.setPhoto(new InputFile(new ByteArrayInputStream(image), imageName));
        try {
            return bot.execute(img);
        } catch (TelegramApiException e) {
            log.warn("Can`t send image", e);
            return null;

        }
    }

    public Message sendImage(InputStream inputStream, String chatId, String imageName){
        SendPhoto img = new SendPhoto();
        img.setChatId(chatId);
        img.setPhoto(new InputFile(inputStream, imageName));
        try {
            return bot.execute(img);
        } catch (TelegramApiException e) {
            log.warn("Can't send image", e);
            return null;
        }
    }
}

