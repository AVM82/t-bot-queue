//package ua.shpp.eqbot;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.telegram.telegrambots.meta.api.objects.Chat;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import ua.shpp.eqbot.model.UserDto;
//import ua.shpp.eqbot.model.UserEntity;
//import ua.shpp.eqbot.service.UserService;
//
//import java.time.LocalDateTime;
//
//@WebMvcTest(MockitoExtension.class)
//class RegistrationUserTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private Update update;
//
//    private UserDto dto;
//
//    private UserEntity entity;
//
//    @BeforeEach
//    void setDtoAndEntity() {
//        dto = new UserDto();
//       /* dto.setTask("task");
//        dto.setPlace("place");
//        dto.setStatus(JobStatus.PLANNED);
//        entity.setId(id);
//        entity.setTask("task");
//        entity.setPlaceOfExecution("place");
//        entity.setExecutionStatus(JobStatus.PLANNED);
//        entity.setTimeOfCreation(LocalDateTime.parse("2022-05-08T12:35:29"));
//        returnEntity.setId(id);
//        returnEntity.setTask("task");
//        returnEntity.setPlaceOfExecution("place2");
//        returnEntity.setExecutionStatus(JobStatus.PLANNED);
//        returnEntity.setTimeOfCreation(LocalDateTime.parse("2022-08-08T12:35:29"));*/
//    }
//
//    @Test
//    void RegistrationNameTest() {
//        update = new Update();
//        Message message = new Message();
//        message.setText("hello");
//        message.setChat(new Chat());
//        update.setMessage(message);
//        Mockito.when(update.getMessage().getChatId()).thenReturn(111L);
//
//
//        Mockito.when(update.getMessage().getChatId()).thenReturn(111L);
//    }
//}
