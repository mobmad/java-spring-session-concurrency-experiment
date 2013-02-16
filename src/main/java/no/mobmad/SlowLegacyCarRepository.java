package no.mobmad;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("request")
public class SlowLegacyCarRepository
{

    public List<Car> getCars(int responseDelay)
    {
        try
        {
            Thread.sleep(responseDelay);
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        ArrayList<Car> cars = new ArrayList<Car>();
        cars.add(new Car("Volkswagen"));
        cars.add(new Car("Toyota"));
        cars.add(new Car("Audi"));
        return cars;
    }
}
