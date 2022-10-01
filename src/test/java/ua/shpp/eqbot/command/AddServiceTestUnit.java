//package ua.shpp.eqbot.command;
//
//import net.bytebuddy.implementation.bind.annotation.IgnoreForBinding;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import ua.shpp.eqbot.internationalization.BundleLanguage;
//import ua.shpp.eqbot.model.ServiceDTO;
//import ua.shpp.eqbot.model.ServiceEntity;
//import ua.shpp.eqbot.repository.ProvideRepository;
//import ua.shpp.eqbot.repository.ServiceRepository;
//import ua.shpp.eqbot.service.ImageService;
//import ua.shpp.eqbot.service.SendBotMessageService;
//import ua.shpp.eqbot.service.UserService;
//
//import java.util.List;
//
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.is;
//@ExtendWith(MockitoExtension.class)
//public class AddServiceTestUnit {
//    private static final Logger LOGGER = LoggerFactory.getLogger(AddService.class);
//    @Mock
//    private SendBotMessageService sendBotMessageService;
//    public static final String ADD_SERVICE_MESSAGE = "input_name_service";
//    @Mock
//    private ServiceRepository serviceRepository;
//    @Mock
//    private UserService userService;
//    @Mock
//    private ImageService imageService;
//    @Mock
//    private ProvideRepository provideRepository;
//    @Mock
//    private BundleLanguage bundleLanguage;
//    @InjectMocks
//    AddService addService;
//
//
//    ServiceDTO service = new ServiceDTO();
//    ServiceEntity serviceEntity;
//
//    @BeforeEach
//    void setUp() {
//        service.setName("lol");
//        serviceEntity = new ServiceEntity();
//        serviceEntity.setIdTelegram(service.getIdTelegram())
//                .setName(service.getName())
//                .setDescription(service.getDescription())
//                .setAvatar(service.getAvatar());
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    void addService() {
//        addService.addService(service);
//        List<ServiceEntity> allById = serviceRepository.findAllById(1L);
//        ServiceEntity serviceEntity1 = allById.get(0);
//        assertThat(serviceEntity1.getName(), is("first name"));
//    }
//
//    @Test
//    void execute() {
//    }
//}