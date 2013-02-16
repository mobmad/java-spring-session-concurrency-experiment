package no.mobmad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("session")
public class CarService
{
    @Autowired
    CarRepository carRepository;

    public List<Car> getCars(String mode, int responseDelay, Logger logger)
    {
        if (mode == null)
        {
            mode = "nosync";
        }

        List<Car> cars;

        if ("autowired".equals(mode))
        {
            cars = carRepository.getCarsSynchronizedOnAutowiredValue(logger, responseDelay);
        } else if ("instance".equals(mode))
        {
            cars = carRepository.getCarsSynchronizedOnInstanceLock(logger, responseDelay);
        } else if ("sessionmutex".equals(mode))
        {
            cars = carRepository.getCarsSynchronizedOnSessionMutex(logger, responseDelay);
        } else
        {
            cars = carRepository.getCarsUnsynchronized(logger, responseDelay);
        }
        return cars;
    }
}
