package no.mobmad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static no.mobmad.LogStatus.*;

@Component
@Scope("session")
public class CarRepository
{
    @Autowired
    HttpServletRequest request;

    @Autowired
    private SlowLegacyCarRepository slowLegacyCarRepository;

    private List<Car> cars;
    private final Object LOCK = new Object();

    public CarRepository()
    {
        cars = new ArrayList<Car>();
    }

    public List<Car> getCarsUnsynchronized(Logger logger, int responseDelay)
    {
        logger.setContext("nosync").log(QUEUED);

        logger.log(PROCESSING);
        if (cars.isEmpty())
        {
            cars = slowLegacyCarRepository.getCars(responseDelay);
        }

        logger.log(DONE);
        return cars;
    }

    public List<Car> getCarsSynchronizedOnInstanceLock(Logger logger, int responseDelay)
    {
        logger.setContext("instance").log(QUEUED);

        synchronized (LOCK)
        {
            logger.log(PROCESSING);
            if (cars.isEmpty())
            {
                cars = slowLegacyCarRepository.getCars(responseDelay);
            }
        }

        logger.log(DONE);
        return cars;
    }

    public List<Car> getCarsSynchronizedOnAutowiredValue(Logger logger, int responseDelay)
    {
        logger.setContext("autowired").log(QUEUED);

        synchronized (slowLegacyCarRepository)
        {
            logger.log(PROCESSING);
            if (cars.isEmpty())
            {
                cars = slowLegacyCarRepository.getCars(responseDelay);
            }
        }

        logger.log(DONE);
        return cars;
    }

    public List<Car> getCarsSynchronizedOnSessionMutex(Logger logger, int responseDelay)
    {
        logger.setContext("sessionmutex").log(QUEUED);
        HttpSession session = request.getSession(false);
        if (session != null)
        {
            Object sessionMutex = WebUtils.getSessionMutex(session);

            synchronized (sessionMutex)
            {
                logger.log(PROCESSING);
                if (cars.isEmpty())
                {
                    cars = slowLegacyCarRepository.getCars(responseDelay);
                }
            }
        }

        logger.log(DONE);
        return cars;
    }
}
