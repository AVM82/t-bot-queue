//package ua.shpp.eqbot.command;
//
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import ua.shpp.eqbot.model.UserEntity;
//import ua.shpp.eqbot.repository.ServiceRepository;
//
//@ExtendWith(MockitoExtension.class)
//class AddServiceTest {
//    private static final Logger LOGGER = LoggerFactory.getLogger(AddService.class);
//    @Mock
//    private AddService addService;
//
//    @InjectMocks
//    private ServiceRepository serviceRepository;
//
//    UserEntity user = new UserEntity();
//
//
//    @BeforeEach
//    void setUp() {
//
//        user.setIdTelegram(1L).setName("Anton").setCity("Kiev").setLanguage("uk")
//        serviceRepository.save(
//
//        );
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void addService() {
//    }
//
//    @Test
//    void execute() {
//    }
//}