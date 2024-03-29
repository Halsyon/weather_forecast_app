package weather.forecast.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import weather.forecast.model.Weather;
import weather.forecast.service.WeatherService;

import java.time.Duration;

@RestController
public class WeatherController {

    @Autowired
    private final WeatherService weathers;

    public WeatherController(WeatherService weathers) {
        this.weathers = weathers;
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> all() {
        Flux<Weather> data = weathers.all();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping(value = "/get/{id}")
    public Mono<Weather> get(@PathVariable Integer id) {
        return weathers.findById(id);
    }

    /**
     * cityGreatThen int max temperature
     * @param id
     * @return
     */
    @GetMapping(value = "/cityGreatThen/{id}")
    public Flux<String> cityGreatThen(@PathVariable Integer id) {
        Flux<String> data = weathers.cityGreatThen(id);
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    /**
     * method that will return the city with the maximum temperature.
     * @return
     */
    @GetMapping(value = "/hottest")
    public Mono<Weather> hottest() {
        return weathers.findByHottest();
    }
}