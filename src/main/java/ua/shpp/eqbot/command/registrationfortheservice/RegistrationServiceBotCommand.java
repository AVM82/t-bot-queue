package ua.shpp.eqbot.command.registrationfortheservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ua.shpp.eqbot.cache.RegistrationForTheServiceCache;
import ua.shpp.eqbot.command.BotCommand;
import ua.shpp.eqbot.command.SendNotificationToProviderCommand;
import ua.shpp.eqbot.dto.RegistrationForTheServiceDto;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.mapper.RegistrationForTheServiceMapper;
import ua.shpp.eqbot.model.RegistrationForTheServiceEntity;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.RegistrationForTheServiceRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.ProviderService;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;
import ua.shpp.eqbot.stage.icon.Icon;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDate;
import java.util.*;

@Component("registrationfortheservicecommandBotCommand")
public class RegistrationServiceBotCommand implements BotCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationServiceBotCommand.class);
    private static int numberOfDaysInSearchOfService;
    private final SendBotMessageService sendBotMessageService;
    private final RegistrationForTheServiceRepository registrationForTheServiceRepository;
    private final ServiceRepository serviceRepository;
    private final BundleLanguage bundleLanguage;
    private final UserService userService;
    private final ProviderService providerService;
    private final int quantityPerRow;
    private Long userTelegramId;
    private UserEntity userEntity;
    private UserDto userDto;
    private RegistrationForTheServiceDto registrationDto;
    private ServiceEntity serviceEntity;

    @Autowired
    public RegistrationServiceBotCommand(SendBotMessageService sendBotMessageService,
                                         RegistrationForTheServiceRepository registrationForTheServiceRepository,
                                         ServiceRepository serviceRepository, BundleLanguage bundleLanguage,
                                         UserService userService, ProviderService providerService) {
        this.sendBotMessageService = sendBotMessageService;
        this.registrationForTheServiceRepository = registrationForTheServiceRepository;
        this.serviceRepository = serviceRepository;
        this.bundleLanguage = bundleLanguage;
        this.userService = userService;
        this.providerService = providerService;
        quantityPerRow = 4;
    }

    public static void setNumberOfDaysInSearchOfService(int numberOfDaysInSearchOfService) {
        RegistrationServiceBotCommand.numberOfDaysInSearchOfService = numberOfDaysInSearchOfService;
    }

    private void init(Update update) {
        if (update.hasCallbackQuery()) {
            userTelegramId = update.getCallbackQuery().getFrom().getId();
        } else if (update.hasMessage()) {
            userTelegramId = update.getMessage().getChatId();
        }
        userDto = userService.getDto(userTelegramId);

        //якщо є у юзера в dto його кліент, то вважаємо що треба зареєструвати його кліента,
        //і до бази данних додамо не данного юзера а його кліента
        if (userDto.getCustomerDto() != null) {
            userEntity = userService.getEntity(userDto.getCustomerDto().getTelegramId());
        } else {
            userEntity = userService.getEntity(userTelegramId);
        }
        registrationDto = RegistrationForTheServiceCache.findByUserTelegramId(userTelegramId);
        if (registrationDto == null) {
            if (userDto.getCustomerDto() != null) {
                //костиль на випадок якщо у провайдера тільки 1 сервіс
                serviceEntity = serviceRepository.findByTelegramId(userDto.getTelegramId());

            } else {
                Optional<ServiceEntity> optional;
                String callbackData = update.getCallbackQuery().getData();
                if (callbackData.startsWith("appoint/")) {
                    optional = serviceRepository.findById(Long.parseLong(callbackData.split("/")[1]));
                } else {
                    optional = serviceRepository.findById(Long.parseLong(callbackData));
                }
                optional.ifPresent(entity -> serviceEntity = entity);
            }
            registrationDto = createDto();
            RegistrationForTheServiceCache.add(registrationDto, userTelegramId);
        } else {
            serviceEntity = registrationDto.getServiceEntity();
        }
    }

    @Override
    public boolean execute(Update update) {
        boolean isRegistered = false;
        init(update);
        List<RegistrationForTheServiceEntity> listServices;
        LocalDateTime date;
        try {
            //перевіряемо чи не нажимали кнопку при реестрації "змінити дату"
            if (update.hasCallbackQuery()
                    && update.getCallbackQuery()
                    .getData().equals(bundleLanguage.getValue(userTelegramId, "change_the_date"))) {
                userDto.setPositionMenu(PositionMenu.REGISTRATION_FOR_THE_SERVICES_START);
            }
            switch (userDto.getPositionMenu()) {
                case REGISTRATION_FOR_THE_SERVICES_START:
                    LOGGER.info("search for free days to sign up for the service");
                    Set<Long> blacklistForService = providerService.getByTelegramIdEntity(
                            serviceRepository.findFirstById(registrationDto.getServiceEntity().getId()).getTelegramId()).getBlacklist();
                    if (blacklistForService.contains(userTelegramId)) {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setText(bundleLanguage.getValue(userTelegramId, "blacklist_you_are_in_blacklist"));
                        sendMessage.setReplyMarkup(InlineKeyboardMarkup.builder().
                                keyboard(List.of(List.of(bundleLanguage.createButton(userTelegramId, "exit", "exit")))).build());
                        sendMessage.setChatId(userTelegramId);
                        sendBotMessageService.sendMessage(sendMessage);
                        userDto.setPositionMenu(PositionMenu.MENU_START);
                        return false;
                    }
                    LOGGER.info("menu position REGISTRATION_FOR_THE_SERVICES_START, registrationDto = {}", registrationDto);
                    date = LocalDateTime.parse(LocalDateTime.now().toLocalDate().toString() + "T00:00:00.0000");
                    listServices = registrationForTheServiceRepository
                            .findAllServicesByDateAndServiceId(date,
                                    date.plusDays(numberOfDaysInSearchOfService + 1L),
                                    registrationDto.getServiceEntity().getId());
                    List<String> freeDays = findData(listServices, registrationDto.getServiceEntity());
                    if (!freeDays.isEmpty()) {
                        new CreatingButtonField(sendBotMessageService, quantityPerRow,
                                freeDays, bundleLanguage.getValue(userTelegramId, "choosing_the_date")
                                + " '" + registrationDto.getServiceEntity().getName() + "'" + Icon.CALENDAR.get(), userTelegramId, "");
                        userDto.setPositionMenu(PositionMenu.REGISTRATION_FOR_THE_SERVICES_DATE);
                    }
                    break;
                case REGISTRATION_FOR_THE_SERVICES_DATE:
                    LOGGER.info("search for free times to sign up for the service");

                    LOGGER.info("menu position REGISTRATION_FOR_THE_SERVICES_DATE, registrationDto = {}", registrationDto);
                    date = LocalDateTime.parse(LocalDateTime.now().toLocalDate().toString() + "T00:00:00.0000");
                    date = date.plusDays(Long.parseLong(update.getCallbackQuery().getData()) - date.getDayOfMonth());
                    registrationDto.setServiceRegistrationDateTime(date);
                    LocalDateTime dateNextDay = date.plusDays(1);
                    listServices = registrationForTheServiceRepository
                            .findAllServicesByDateAndServiceId(date, dateNextDay, registrationDto.getServiceEntity().getId());
                    EnumMap<DayOfWeek, String> schedule = getSchedule(serviceEntity);
                    List<String> freeTimes = findFreeTime(listServices, schedule.get(date.getDayOfWeek()),
                            serviceEntity.getTimeBetweenClients(),
                            LocalDate.from(date).isEqual(ChronoLocalDate.from(LocalDateTime.now()))); //is it present day
                    new CreatingButtonField(sendBotMessageService, quantityPerRow,
                            freeTimes, bundleLanguage
                            .getValue(userTelegramId, "choosing_the_time")
                            + " '" + registrationDto.getServiceEntity().getName() + "'"
                            + " " + registrationDto.getServiceRegistrationDateTime().toLocalDate(), userTelegramId,
                            bundleLanguage.getValue(userTelegramId, "change_the_date"));
                    userDto.setPositionMenu(PositionMenu.REGISTRATION_FOR_THE_SERVICES_TIME);
                    break;
                case REGISTRATION_FOR_THE_SERVICES_TIME:
                    LOGGER.info("menu position REGISTRATION_FOR_THE_SERVICES_TIME, registrationDto = {}", registrationDto);
                    date = registrationDto.getServiceRegistrationDateTime();
                    date = LocalDateTime.parse(date.toLocalDate().toString() + "T" + update.getCallbackQuery().getData());
                    registrationDto.setServiceRegistrationDateTime(date);
                    userDto.setPositionMenu(PositionMenu.MENU_START).setCustomerDto(null);
                    isRegistered = true;
                    RegistrationForTheServiceEntity entity = RegistrationForTheServiceMapper.INSTANCE
                            .registrationForTheServiceDtoToEntity(registrationDto);
                    LOGGER.info("menu position REGISTRATION_FOR_THE_SERVICES_TIME, registrationEntity = {}", entity); //for debug
                    registrationForTheServiceRepository.save(entity);
                    sendBotMessageService.sendMessage(SendMessage.builder()
                            .text(bundleLanguage.getValue(userTelegramId, "registration_for_the_service_is_over")
                                    + " '" + registrationDto.getServiceEntity().getName() + "'"
                                    + " " + date.toLocalDate() + " " + date.toLocalTime())
                            .chatId(userTelegramId)
                            .build());
                    SendNotificationToProviderCommand sendNotificationToProviderCommand =
                            new SendNotificationToProviderCommand(serviceRepository, sendBotMessageService, bundleLanguage);
                    sendNotificationToProviderCommand.sendNotification(registrationDto.getServiceEntity().getId(),
                            date.toLocalDate() + " " + date.toLocalTime(),
                            registrationDto.getUserEntity().getName(),
                            update.getCallbackQuery().getFrom().getId().toString());
                    RegistrationForTheServiceCache.remove(userTelegramId);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("Registration has error. Message = {}", e.getMessage());
            userDto.setPositionMenu(PositionMenu.MENU_START).setCustomerDto(null);
            sendBotMessageService.sendMessage(SendMessage.builder()
                    .text(bundleLanguage.getValue(userTelegramId, "error_registration"))
                    .chatId(userTelegramId)
                    .build());
            isRegistered = true;
            RegistrationForTheServiceCache.remove(userTelegramId);
        }
        return isRegistered;
    }

    private RegistrationForTheServiceDto createDto() {
        RegistrationForTheServiceDto dto = new RegistrationForTheServiceDto();
        dto.setServiceEntity(serviceEntity)
                .setUserEntity(userEntity);
        return dto;
    }

    /**
     * search for a free date of registration for the service in the provider's
     * schedule, the number of days for the search is set by a static variable
     *
     * @param listServices  - selection of all records for this service from the registration for the user table
     * @param serviceEntity - data about the service required by the user
     * @return - a list of days on which there is free time for registration
     */
    private List<String> findData(List<RegistrationForTheServiceEntity> listServices, ServiceEntity serviceEntity) {
        List<String> freeDays = new ArrayList<>();
        EnumMap<DayOfWeek, String> schedule = getSchedule(serviceEntity);
        List<String> workTime = workTime(serviceEntity.getTimeBetweenClients(),
                schedule.get(LocalDateTime.now().toLocalDate().getDayOfWeek()));
        LocalDateTime nowDay = LocalDateTime.now();
        if (workTime.isEmpty() || (!workTime.isEmpty()
                && LocalTime.parse(workTime.get(workTime.size() - 1)).isBefore(nowDay.toLocalTime()))) {
            nowDay = nowDay.plusDays(1);
        }
        LocalDateTime date;
        int numberOfEntriesPerDay; //записів на день
        for (int count = 0; count <= numberOfDaysInSearchOfService; count++) {
            numberOfEntriesPerDay = 0;
            date = nowDay.plusDays(count); //date new day
            for (RegistrationForTheServiceEntity item : listServices) {
                if (item.getServiceRegistrationDateTime().getDayOfMonth() == date.getDayOfMonth()) {
                    numberOfEntriesPerDay++;
                }
            }
            if (numberOfEntriesPerDay < workTime(serviceEntity.getTimeBetweenClients(),
                    schedule.get(date.getDayOfWeek())).size()) {
                freeDays.add(String.valueOf(date.getDayOfMonth()));
            }
        }
        return freeDays;
    }

    /**
     * search for free hours at the provider for registration
     *
     * @param listServices       - list of times on the selected day when the provider is busy
     * @param schedule           - work schedule for the day
     * @param timeBetweenClients - the time it takes to receive one client
     * @return - list of free time at the provider
     */
    private List<String> findFreeTime(List<RegistrationForTheServiceEntity> listServices,
                                      String schedule, String timeBetweenClients, boolean isNow) {
        List<String> workTime = workTime(timeBetweenClients, schedule);
        if (isNow) {
            LocalTime timeNow = LocalTime.now();
            Iterator<String> iterator = workTime.iterator();
            while (iterator.hasNext()) {
                String e = iterator.next();
                if (timeNow.isAfter(LocalTime.parse(e))) {
                    iterator.remove();
                }
            }
        }
        if (listServices.isEmpty()) {
            return workTime;
        }
        List<String> listFreeTime = new ArrayList<>();
        boolean coincidence;
        for (String workingTime : workTime) {
            String[] temp = workingTime.split(":");
            int workingHour = Integer.parseInt(temp[0]);
            int workingMinute = Integer.parseInt(temp[1]);
            coincidence = false;
            for (RegistrationForTheServiceEntity item : listServices) {
                int hour = item.getServiceRegistrationDateTime().getHour();
                int minute = item.getServiceRegistrationDateTime().getMinute();
                if (hour == workingHour && minute == workingMinute) {
                    coincidence = true;
                    break;
                }
            }
            if (!coincidence) {
                listFreeTime.add(getTime(workingHour, workingMinute));
            }
        }
        return listFreeTime;
    }


    /**
     * converts numerical values of hours and minutes into time
     *
     * @param hour   - hours in numbers
     * @param minute -minute in numbers
     * @return - time string
     */
    private String getTime(int hour, int minute) {
        String hh;
        String mm;
        if (hour < 10) {
            hh = "0" + hour;
        } else {
            hh = String.valueOf(hour);
        }
        if (minute < 10) {
            mm = "0" + minute;
        } else {
            mm = String.valueOf(minute);
        }
        return hh + ":" + mm;
    }

    /**
     * calculates the recording time for registration
     *
     * @param timeBetweenClients - the time it takes to receive one client
     * @param schedule           -work schedule for the day
     * @return - a list of times for which the client can register
     */
    private List<String> workTime(String timeBetweenClients, String schedule) {
        List<String> workTime = new ArrayList<>();
        if (schedule.equals("-")) {
            return workTime;
        }
        String[] str = schedule.split("-");
        String[] start = str[0].split(":");
        String[] end = str[1].split(":");
        int hour = Integer.parseInt(start[0]);
        int min = Integer.parseInt(start[1]);
        int endHour = Integer.parseInt(end[0]);
        String[] clientTime = timeBetweenClients.split(":");
        int clientTimeHour = Integer.parseInt(clientTime[0]);
        int clientTimeMin = Integer.parseInt(clientTime[1]);
        while (endHour > hour) {
            workTime.add(getTime(hour, min));
            hour += clientTimeHour;
            min += clientTimeMin;
            if (min > 59) {
                min %= 60;
                hour++;
            }
        }
        return workTime;
    }

    /**
     * fills the schedule for the whole week
     *
     * @param serviceEntity - data about the service required by the user
     * @return - work schedule map for the week
     */
    private EnumMap<DayOfWeek, String> getSchedule(ServiceEntity serviceEntity) {
        EnumMap<DayOfWeek, String> schedule = new EnumMap<>(DayOfWeek.class);
        schedule.put(DayOfWeek.SUNDAY, serviceEntity.getSundayWorkingHours());
        schedule.put(DayOfWeek.MONDAY, serviceEntity.getMondayWorkingHours());
        schedule.put(DayOfWeek.TUESDAY, serviceEntity.getTuesdayWorkingHours());
        schedule.put(DayOfWeek.WEDNESDAY, serviceEntity.getWednesdayWorkingHours());
        schedule.put(DayOfWeek.THURSDAY, serviceEntity.getThursdayWorkingHours());
        schedule.put(DayOfWeek.FRIDAY, serviceEntity.getFridayWorkingHours());
        schedule.put(DayOfWeek.SATURDAY, serviceEntity.getSaturdayWorkingHours());
        return schedule;
    }
}
