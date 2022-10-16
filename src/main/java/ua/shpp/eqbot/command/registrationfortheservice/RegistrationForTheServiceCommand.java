package ua.shpp.eqbot.command.registrationfortheservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import ua.shpp.eqbot.cache.RegistrationForTheServiceCache;
import ua.shpp.eqbot.command.ICommand;
import ua.shpp.eqbot.dto.RegistrationForTheServiceDto;
import ua.shpp.eqbot.dto.UserDto;
import ua.shpp.eqbot.internationalization.BundleLanguage;
import ua.shpp.eqbot.mapper.RegistrationForTheServiceMapper;
import ua.shpp.eqbot.model.RegistrationForTheServiceEntity;
import ua.shpp.eqbot.model.ServiceEntity;
import ua.shpp.eqbot.model.UserEntity;
import ua.shpp.eqbot.repository.RegistrationForTheServiceRepository;
import ua.shpp.eqbot.repository.ServiceRepository;
import ua.shpp.eqbot.service.SendBotMessageService;
import ua.shpp.eqbot.service.UserService;
import ua.shpp.eqbot.stage.PositionMenu;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Optional;

@Component
public class RegistrationForTheServiceCommand implements ICommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistrationForTheServiceCommand.class);
    private static int numberOfDaysInSearchOfService;
    private final SendBotMessageService sendBotMessageService;
    private final RegistrationForTheServiceRepository registrationForTheServiceRepository;
    private final ServiceRepository serviceRepository;
    private final BundleLanguage bundleLanguage;
    private final UserService userService;
    private final int quantityPerRow;

    public RegistrationForTheServiceCommand(SendBotMessageService sendBotMessageService,
                                            RegistrationForTheServiceRepository registrationForTheServiceRepository,
                                            ServiceRepository serviceRepository, BundleLanguage bundleLanguage,
                                            UserService userService) {
        this.sendBotMessageService = sendBotMessageService;
        this.registrationForTheServiceRepository = registrationForTheServiceRepository;
        this.serviceRepository = serviceRepository;
        this.bundleLanguage = bundleLanguage;
        this.userService = userService;
        quantityPerRow = 4;
    }

    public static void setNumberOfDaysInSearchOfService(int numberOfDaysInSearchOfService) {
        RegistrationForTheServiceCommand.numberOfDaysInSearchOfService = numberOfDaysInSearchOfService;
    }

    @Override
    public boolean execute(Update update) {
        boolean isRegistered = false;
        Long userId = update.getCallbackQuery().getFrom().getId();
        UserDto userDto = userService.getDto(userId);
        UserEntity userEntity = userService.getEntity(userId);
        Long serviceId;
        Optional<ServiceEntity> optional;
        List<RegistrationForTheServiceEntity> listServices;
        RegistrationForTheServiceDto registrationDto;
        LocalDateTime date;
        switch (userDto.getPositionMenu()) {
            case SEARCH_BY_NAME:
                LOGGER.info("search for free days to sign up for the service");
                serviceId = Long.valueOf(update.getCallbackQuery().getData());
                optional = serviceRepository.findById(serviceId);
                if (optional.isPresent()) {
                    ServiceEntity serviceEntity = optional.get();
                    registrationDto = new RegistrationForTheServiceDto();
                    registrationDto.setServiceEntity(serviceEntity);
                    registrationDto.setUserEntity(userEntity);
                    RegistrationForTheServiceCache.add(registrationDto, userId);
                    //listServices = registrationForTheServiceRepository.findAllServicesById(serviceId);
                    date = LocalDateTime.parse(LocalDateTime.now().toLocalDate().toString() + "T00:00:00.0000");
                    listServices = registrationForTheServiceRepository
                            .findAllServicesByDateAndServiceId(date, date.plusDays(numberOfDaysInSearchOfService + 1),
                                    registrationDto.getServiceEntity().getId());
                    List<String> freeDays = findData(listServices, serviceEntity);
                    if (!freeDays.isEmpty()) {
                        new CreatingButtonField(sendBotMessageService, quantityPerRow,
                                freeDays, bundleLanguage.getValue(userId, "choosing_the_date"), userId);
                        userDto.setPositionMenu(PositionMenu.REGISTRATION_FOR_THE_SERVICES_DATE);
                    }
                }
                break;
            case REGISTRATION_FOR_THE_SERVICES_DATE:
                LOGGER.info("search for free times to sign up for the service");
                registrationDto = RegistrationForTheServiceCache.findByUserTelegramId(userId);
                date = LocalDateTime.parse(LocalDateTime.now().toLocalDate().toString() + "T00:00:00.0000");
                date = date.plusDays(Long.parseLong(update.getCallbackQuery().getData()) - date.getDayOfMonth());
                registrationDto.setServiceRegistrationDateTime(date);
                LocalDateTime dateNextDay = date.plusDays(1);
                listServices = registrationForTheServiceRepository
                        .findAllServicesByDateAndServiceId(date, dateNextDay, registrationDto.getServiceEntity().getId());
                optional = serviceRepository.findById(registrationDto.getServiceEntity().getId());
                if (optional.isPresent()) {
                    ServiceEntity serviceEntity = optional.get();
                    EnumMap<DayOfWeek, String> schedule = getSchedule(serviceEntity);
                    List<String> freeTimes = findFreeTime(listServices, schedule.get(date.getDayOfWeek()),
                            serviceEntity.getTimeBetweenClients());
                    new CreatingButtonField(sendBotMessageService, quantityPerRow,
                            freeTimes, bundleLanguage.getValue(userId, "choosing_the_time"), userId);
                    userDto.setPositionMenu(PositionMenu.REGISTRATION_FOR_THE_SERVICES_TIME);
                }
                break;
            case REGISTRATION_FOR_THE_SERVICES_TIME:
                registrationDto = RegistrationForTheServiceCache.findByUserTelegramId(userId);
                date = registrationDto.getServiceRegistrationDateTime();
                date = LocalDateTime.parse(date.toLocalDate().toString() + "T" + update.getCallbackQuery().getData());
                registrationDto.setServiceRegistrationDateTime(date);
                userDto.setPositionMenu(PositionMenu.MENU_START);
                isRegistered = true;
                registrationForTheServiceRepository.save(RegistrationForTheServiceMapper.INSTANCE
                        .registrationForTheServiceDtoToEntity(registrationDto));
                sendBotMessageService.sendMessage(SendMessage.builder()
                        .text(bundleLanguage.getValue(userId, "registration_for_the_service_is_over")
                                + " " + date.toLocalDate() + " " + date.toLocalTime())
                        .chatId(userId)
                        .build());
                RegistrationForTheServiceCache.remove(userId);
                break;
            default:
                break;
        }
        return isRegistered;
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
        LocalDateTime nowDay = LocalDateTime.now();
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
                                      String schedule, String timeBetweenClients) {
        List<String> workTime = workTime(timeBetweenClients, schedule);
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
        String[] str = schedule.split("-");
        String[] start = str[0].split(":");
        String[] end = str[1].split(":");
        int hour = Integer.parseInt(start[0]);
        int min = Integer.parseInt(start[1]);
        int endHour = Integer.parseInt(end[0]);
        int endMin = Integer.parseInt(end[1]);
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
