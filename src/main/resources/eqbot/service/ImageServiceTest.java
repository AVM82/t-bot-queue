package ua.shpp.eqbot.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import ua.shpp.eqbot.telegrambot.EqTelegramBot;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    EqTelegramBot bot;

    @InjectMocks
    ImageService imageService = new ImageService(bot);

    @Test
    void getBiggestImageSmallerThan() {
        List<PhotoSize> photos = new ArrayList<>();
        PhotoSize photo1= new PhotoSize("1", "1", 5000, 5000, 5000, "path1");
        PhotoSize photo2= new PhotoSize("1", "1", 115, 115, 115, "path1");
        PhotoSize photo3= new PhotoSize("1", "1", 195, 195, 195, "path1");
        photos.add(photo1);
        photos.add(photo2);
        photos.add(photo3);
        assertThat(imageService.getBiggestImageSmallerThan(photos, 200), is(photo3));
        PhotoSize photo4= new PhotoSize("1", "1", 200, 200, 200, "path1");
        photos.add(photo4);
        assertThat(imageService.getBiggestImageSmallerThan(photos, 200), is(photo3));
        PhotoSize photo5= new PhotoSize("1", "1", 199, 199, 199, "path1");
        photos.add(photo5);
        assertThat(imageService.getBiggestImageSmallerThan(photos, 200), is(photo5));
    }

    @Test
    void getBiggestImage() {
        List<PhotoSize> photos = new ArrayList<>();
        PhotoSize photo1= new PhotoSize("1", "1", 5000, 5000, 5000, "path1");
        PhotoSize photo2= new PhotoSize("1", "1", 115, 115, 115, "path1");
        PhotoSize photo3= new PhotoSize("1", "1", 195, 195, 195, "path1");
        photos.add(photo1);
        photos.add(photo2);
        photos.add(photo3);
        assertThat(imageService.getBiggestImage(photos), is(photo1));
        PhotoSize photo4= new PhotoSize("1", "1", 5000, 5000, 5000, "path1");
        photos.add(photo4);
        assertThat(imageService.getBiggestImage(photos), is(photo1));
        PhotoSize photo5= new PhotoSize("1", "1", 5001, 5001, 5001, "path1");
        photos.add(photo5);
        assertThat(imageService.getBiggestImage(photos), is(photo5));

    }


}