# Aplikacja do sprawdzania pogody

## Uruchomienie
### Wymagania do uruchomienia
* Node.JS >= 14.0
* Java 17
* Git
* Git Bash lub inne podobne narzędzie (Windows)

### Linux i MacOS
Uruchomić Terminal, a następnie wykonać instrukcje poniżej
### Windows
Uruchomić Git Bash, a następnie wykonać instrukcje poniżej
### Instrukcje do uruchomienia za pomocą skryptu
Sklonować repozytorium
```
$ git clone 'nazwa repozytorium.git'
```
Wejść do katalogu
```
$ cd kp-wt-1640-pogodynki/
```
Uruchomić skrypt start.sh
```
$ ./start.sh
```
Jeżeli nie są nadane odpowiednie uprawnienia należy wcześniej wykonać
```
$ chmod 744 start.sh
```
A następnie dopiero
```
$ ./start.sh
```
### Manualne uruchomienie programu
#### Linux i MacOS
Pobrać pliki z repozytorium, rozpakować i wejść do katalogu
```
$ cd kp-wt-1640-pogodynki/
```
Uruchomić serwer z okna terminalu
```
$ ./gradlew bootRun
```
Przejście do katalogu z Frontendem
```
$ cd front/weather-app/
```
Uruchomienie frontendu
```
$ npm start
```
Uruchomić przeglądarkę na adresie:
```
http://localhost:3000
```
#### Windows
Pobrać pliki z repozytorium, rozpakować i wejść do katalogu
```
> cd kp-wt-1640-pogodynki\
```
Uruchomić serwer z okna terminalu
```
> ./gradlew.bat bootRun
```
Przejście do katalogu z Frontendem
```
> cd front\weather-app\
```
Uruchomienie frontendu
```
> npm start
```
Uruchomić przeglądarkę na adresie:
```
http://localhost:3000
```
## Frontend
Do interfejsu graficznego oraz
### Komponenty
#### Form
Kod JSX zwracany przez komponent
```
<form className="latlong-form">
    <h1>Weather Form</h1>
    <label htmlFor="lattitude">Lattitude</label>
    <input onChange={lattitudeValidation} name="lattitude" type="text"></input>
    {showErrorLattitude && <p className="form-error">* Lattitude must be between -90 and 90</p>}
    <label htmlFor="longitude">Longitude</label>
    <input onChange={longitudeValidation} name="longitude" type="text"></input>
    {showErrorLongitude && <p className="form-error">* Longitude must be between -180 and 180</p>}
    <button onClick={formWeatherRequest}>Get Weather ⛅</button>
    {showSubmitError && <p className="form-error">* Invalid data in the form</p>}
</form>
```
Komponent posiada dwa inputy, które przy wpisywaniu wartości walidują dane przy pomocy funkcji *longitudeValidation* oraz *lattitudeValidation*. Funkcje te działają analogicznie dla obu inputów
```
const lattitudeValidation = (event) => {
    let val = event.target.value;
    setLattitude(val);
    console.log(val);
    if (/^\[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$/.test(val)) {
        setShowErrorLattitude(true);
        return;
    }
    let floatVal = parseFloat(val); 
    if (floatVal < 90 && floatVal > -90) {
        setShowErrorLattitude(false);
    } else {
        setShowErrorLattitude(true);
    }
}
```
Komponent posiada też guzik, który przy kliknięciu wykonuje funkcję *formWeatherRequest*
```
const formWeatherRequest = async (event) => {
    event.preventDefault();
    if (showErrorLattitude || showErrorLongitude || !longitude || !lattitude) {
        clearTimeout(timeoutId);
        setShowSubmitError(true);
        setTimeoutId(setTimeout(() => {setShowSubmitError(false)}, 3000))
    } else {
        try {
            await WeatherService.getWeatherByCoordinates(lattitude, longitude);
            navigate("/weather");
        } catch (error) {
            console.error(error.message);
        }
    }
}
```
Funkcja ta sprawdza, czy wartości podane przez użytkownika są poprawne, a następnie wykonuje funkcję serwisu *getWeatherByCoordinates* i przekierowuje do komponentu *WeatherOverview* zajmującego się wyświetlaniem danych.
#### WeatherView
```
export default function WeatherView() {
    
    const [weatherInfo, setWeatherInfo] = useState(null);

    useEffect(() => {
    const fetchData = async () => {
    try {
        const currentWeather = await WeatherService.getCurrentWeather();
        setWeatherInfo(currentWeather)
      } catch (error) {
        console.error(error.message);
      }
    };
    fetchData();
        }, [])

    if (!weatherInfo) {
        return null
    }
    
    return (
        <div className="weather-container">
            <WeatherOverview {...weatherInfo}/>
        </div>
    )
}
```
Komponent pobiera dane z serwisu i przekazuje je do komponentu *WeatherOverview*
#### WeatherOverview
```
export default function WeatherOverview(props) {
    return (
    <div className="weather-overview-container">
        <h2 className="weather-city-name">{props.location}</h2>
        <img className="weather-img-present" src={require(`../images/${props.img_path}`)} alt={props.condition}/>
        <h3 className="weather-temperature">{props.temp_c}&#176;C</h3>
        <h4 className="weather-condition">{props.condition}</h4>
    </div>
    )
}
```
Komponent przyjmuje od *WeatherView* poprzez *props* dane na temat pogody i lokalizacji pobrane wcześniej z serwisu. Komponent zajmuje się wyświetlaniem tych danych
### Serwisy
#### WeatherService
```
import axios from 'axios';

const enpointURL = 'http://127.0.0.1:8080';

const WeatherService = {
  getWeatherByCoordinates: async (lattitude, longitude) => {
    try {
      const response = await axios.post(`${enpointURL}/weather`, {
          lat: lattitude,
          lng: longitude,
        }
      );
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch weather data by coordinates');
    }
  },

  getCurrentWeather: async () => {
    try {
      const response = await axios.get(`${enpointURL}/weather/current`);
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch current weather data');
    }
  },
};

export default WeatherService;
```
Serwis służący do komunikacji z backendem. funkcja *getWeatherByCoordinates* wysyła koordynaty pobrane od użytkownika, korzystając z endpointa */weather*, natomiast funkcja getCurrentWeather pobiera informacje korzystając z endpointa */weather/current* i je zwraca
## Backend
### Kontrolery
### WeatherController 
```
@RestController
@CrossOrigin()
@RequestMapping("/weather")
public class WeatherController {
    private final WeatherService service;

    @Autowired
    public WeatherController(WeatherService service) {
        this.service = service;
    }
    ...
}
```
Klasa kontroler, zawierająca endpointy do komunikacji z frontendem
#### findWeather
```
@PostMapping("")
public ResponseEntity<WeatherResponse> findWeather(@RequestBody WeatherRequestDto weatherRequestDto) {
    WeatherRequest weatherRequest = new WeatherRequest(weatherRequestDto.lat(), weatherRequestDto.lng());
    Optional<WeatherResponse> weatherResponse;
    try {
        weatherResponse = service.findWeather(weatherRequest);
    } catch (IOException ignored) {
        return new ResponseEntity<>(BAD_GATEWAY);
    }

    return weatherResponse
            .map(wr -> new ResponseEntity<>(wr, OK))
            .orElse(new ResponseEntity<>(NOT_FOUND));
}
```
Endpoint udostępnoiny na porcie localhost:8080/weather, metoda post. Przyjmuje obiekt klasy WeatherRequestDto,
mapuje go do obiektu klasu WeatherRequest i przekazuje do funkcji findWeather z serwisu WeatherService.
Zawraca ReponseEntity z WeatherResponse i statusem HTTP.
#### getLastWeatherResponse
```
@GetMapping("/current")
public ResponseEntity<WeatherResponse> getLastWeatherResponse() {
    if (!ResponseHolder.isLastResponse()) {
        return new ResponseEntity<>(NOT_FOUND);
    }
    return new ResponseEntity<>(ResponseHolder.getLastResponse(), OK);
}
```
Drugi endpoint służy do pobrania ostatniego zapytania, tak aby po odświeżeniu przeglądarki nasze dane nie znikły oraz nie było potrzebne wykonanie ponownego takiego samego zapytania
### Modele
#### WeatherRequestDto
```
public record WeatherRequestDto(double lat, double lng) {
}
```
Klasa zawierająca dwie liczby zmiennoprzecinkowe, będące koordynatami interesującej nas lokalizacji. Jest to data transfer
object, co oznacza, że w razie gdy api z front endu się zmieni, nie będziemy musieli ruszać logiki
w naszych serwisach, wystarczy przemapować tą klasę odpowiednio na WeatherRequest. Jest to tylko
klasa przejściowa.

#### WeatherRequest
```
public record WeatherRequest(double lat, double lng) {
}
```
Klasa zawierająca dwie liczby zmiennoprzecinkowe, będące koordynatami interesującej nas lokalizacji. Operujemy na niej
w procesie zdobywania informacji o pogodzie

#### WeatherMultiRequest
```
public record WeatherMultiRequest(
        List<WeatherRequest> weatherRequests
) {
}
```
Klasa zawierająca listę obiektów klasy WeatherRequest wykorzystywana przy zapytaniach dotyczących wielu lokalizacji.

#### WeatherResponse
```
public record WeatherResponse(
        String location,
        double temp_c,
        String img_path,
        String condition,
        double precip_mm
) {
}
```
Klasa zawierająca dane dotyczące lokalizacji oraz pogody. Służy do zwrócenia uzyskanych z API informacji.

#### WeatherForecastResponse
```
public record WeatherForecastResponse(
        String location,
        List<WeatherPerHour> todayWeather,
        List<WeatherPerHour> tomorrowWeather
) {
}
```
Klasa zawierająca lokalizację, pogodę na obecny dzień i pogodę na jutro.

#### WeatherResponseConverted
```
public record WeatherResponseConverted(
        List<String> locations,
        TemperatureLevel temperatureLevel,
        boolean isWindy,
        List<Precipitation> precipitation,
        double minTemp,
        double sensedTemp,
        double maxPrecip
) {
}
```
Klasa zawierająca dane pogodowe wykorzystywane przez frontend.

#### Precipitation
```
public enum Precipitation {
    SNOW,
    RAIN,
    NONE;

    public static List<Precipitation> prepareListOfPrecipitations(boolean willItRain, boolean willItSnow) {
        List<Precipitation> precipitations = new ArrayList<>();
        if (willItRain) {
            precipitations.add(RAIN);
        }
        if (willItSnow) {
            precipitations.add(SNOW);
        }
        if (precipitations.isEmpty()) {
            precipitations.add(NONE);
        }
        return precipitations;
    }
}
```
Klasa zawierająca wartości opisujące warunki dotyczące opadów atmosferycznych oraz metodę przygotowującą listę opadów.

#### TemperatureLevel
```
public enum TemperatureLevel {
    FREEZING,
    COLD,
    WARM,
    HOT;

    public static TemperatureLevel determineTemperatureLevel(double sensedTemp) {
        if (sensedTemp < -5) {
            return FREEZING;
        }
        if (sensedTemp < 5) {
            return COLD;
        }
        if (sensedTemp < 20) {
            return WARM;
        }
        return HOT;
    }
}
```
Klasa zawierająca wartości opisujące warunki dotyczące poziomu temperatury oraz metodę determinującą poziom dla warunków pogodowych.

#### WeatherPerHour
```
public record WeatherPerHour(
        String time,
        double temp_c,
        double precip_mm,
        double wind_kph,
        int will_it_rain,
        int will_it_snow
) {
}
```
Klasa zawierająca dane dotyczące warunków pogodowych występujących w danej godzinie.

### Serwisy
#### WeatherService
```
@Service
public class WeatherService {

    private static final String URL_CURRENT = "http://api.weatherapi.com/v1/current.json";
    private static final String URL_FORECAST = "http://api.weatherapi.com/v1/forecast.json";
    private static final String API_KEY = "53416f14f51041f593a122744232711";
    private static final String LOCATION = "location";
    private static final String CURRENT = "current";
    private static final String CONDITION = "condition";
    private final OkHttpClient client;
    private final Gson gson;

    @Autowired
    public WeatherService(OkHttpClient client, Gson gson) {
        this.client = client;
        this.gson = gson;
    }

    public Optional<WeatherResponse> findWeather(WeatherRequest weatherRequest) throws IOException {
        String params = weatherRequest.lat() + "," + weatherRequest.lng();
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(URL_CURRENT)).newBuilder()
                .addQueryParameter("key", API_KEY)
                .addQueryParameter("q", params);
        Request request = new Request.Builder()
                .url(builder.build())
                .build();
        try (Response response = client.newCall(request).execute()) {
            if (response.code() == 200) {
                JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                String location = json.getAsJsonObject(LOCATION).get("name").getAsString();
                double temp_c = Double.parseDouble(json.getAsJsonObject(CURRENT).get("temp_c").getAsString());
                String[] img_path = json.getAsJsonObject(CURRENT).getAsJsonObject(CONDITION).get("icon").getAsString().split("/");
                String condition = json.getAsJsonObject(CURRENT).getAsJsonObject(CONDITION).get("text").getAsString();
                double precip_mm = Double.parseDouble(json.getAsJsonObject(CURRENT).get("precip_mm").getAsString());
                WeatherResponse lastResponse = new WeatherResponse(location, temp_c, img_path[img_path.length - 2]
                        + "/" + img_path[img_path.length - 1], condition, precip_mm);
                ResponseHolder.updateLastResponse(lastResponse);
                return Optional.of(lastResponse);
            }
        } catch (IOException e) {
            throw new IOException();
        } catch (NullPointerException e) {
            return Optional.empty();
        }
        return Optional.empty();
    }

    public Optional<WeatherResponseConverted> findWeatherForecast(List<WeatherRequest> weatherRequests) throws IOException {
        List<WeatherForecastResponse> responses = new ArrayList<>();
        for (WeatherRequest weatherRequest : weatherRequests) {
            String params = weatherRequest.lat() + "," + weatherRequest.lng();
            HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(URL_FORECAST)).newBuilder()
                    .addQueryParameter("key", API_KEY)
                    .addQueryParameter("q", params)
                    .addQueryParameter("days", "2");
            Request request = new Request.Builder()
                    .url(builder.build())
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (response.code() == 200) {
                    JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                    String location = json.getAsJsonObject(LOCATION).get("name").getAsString();

                    JsonArray weathers = json.getAsJsonObject("forecast").getAsJsonArray("forecastday");

                    JsonArray todayWeatherJson = weathers.get(0).getAsJsonObject().getAsJsonArray("hour");
                    List<WeatherPerHour> todayWeather = List.of(gson.fromJson(todayWeatherJson, WeatherPerHour[].class));

                    JsonArray tomorrowWeatherJson = weathers.get(1).getAsJsonObject().getAsJsonArray("hour");
                    List<WeatherPerHour> tomorrowWeather = List.of(gson.fromJson(tomorrowWeatherJson, WeatherPerHour[].class));

                    responses.add(new WeatherForecastResponse(location, todayWeather, tomorrowWeather));
                }
            } catch (IOException e) {
                throw new IOException();
            } catch (NullPointerException ignored) {
                return Optional.empty();
            }
        }
        if (responses.isEmpty()) {
            return Optional.empty();
        }
        try {
            return Optional.of(WeatherResponseConverter.convertWeatherResponse(responses));
        } catch (MissingDataException e) {
            return Optional.empty();
        }
    }
}
```
Klasa serwis, jej zadaniem jest połączenie się z API, wysłanie zapytania oraz przetworzenie informacji zwrotnej.

### Konfiguratory
#### WeatherConfigurator
```
@Configuration
public class WeatherConfigurator {

    @Bean
    OkHttpClient client() {
        return new OkHttpClient();
    }
}
```
Klasa odpowiedzialna za skonstruowanie obiektu OkHttpClient, wstrzykiwanego do serwisu.

### Konwertery
#### WeatherResponseConverter
```
public class WeatherResponseConverter {
    private static final double MIN_WINDY_VALUE = 5;

    public static WeatherResponseConverted convertWeatherResponse(List<WeatherForecastResponse> weatherForecastResponses)
            throws MissingDataException {
        List<WeatherPerHour> weatherPerHours = prepareDataToAnalysis(weatherForecastResponses);
        List<String> locations = getLocations(weatherForecastResponses);

        double minTemp = findMinTemp(weatherPerHours);
        double maxPrecip = findMaxPrecip(weatherPerHours);
        double maxWind = findMaxWind(weatherPerHours);
        boolean willItRain = checkWillItRain(weatherPerHours);
        boolean willItSnow = checkWillItSnow(weatherPerHours);
        double sensedTemp = calculateSensedTemp(minTemp, maxWind);

        TemperatureLevel temperatureLevel = determineTemperatureLevel(sensedTemp);
        boolean isWindy = determineIsWindy(maxWind);
        List<Precipitation> precipitations = prepareListOfPrecipitations(willItRain, willItSnow);

        return new WeatherResponseConverted(locations, temperatureLevel, isWindy, precipitations, minTemp, sensedTemp, maxPrecip);
    }

    private static List<String> getLocations(List<WeatherForecastResponse> weatherForecastResponses) {
        return weatherForecastResponses.stream()
                .map(WeatherForecastResponse::location)
                .toList();
    }

    private static List<WeatherPerHour> prepareDataToAnalysis(List<WeatherForecastResponse> weatherForecastResponses) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime latestTimeForToday = LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(),
                currentTime.getDayOfMonth(), 6, 0);
        if (currentTime.isAfter(latestTimeForToday)) {
            return weatherForecastResponses.stream()
                    .map(WeatherForecastResponse::tomorrowWeather)
                    .flatMap(Collection::stream)
                    .toList();
        }
        return weatherForecastResponses.stream()
                .map(WeatherForecastResponse::todayWeather)
                .flatMap(Collection::stream)
                .toList();
    }

    private static boolean determineIsWindy(double maxWind) {
        return maxWind > MIN_WINDY_VALUE;
    }
}
```
Klasa odpowiedzialna za przetworzenie otrzymanych z serwisu danych do formy wyświetlanej na frontendzie.

#### WeatherCalculator
```
class WeatherCalculator {
    private static final String TEMP_EXCEPTION_MESSAGE = "Data about temperature not found";
    private static final String PRECIP_EXCEPTION_MESSAGE = "Data about precipitation not found";
    private static final String WIND_EXCEPTION_MESSAGE = "Data about wind not found";
    private static final int FLAG_TRUE = 1;

    static double findMinTemp(List<WeatherPerHour> weatherPerHours) throws MissingDataException {
        OptionalDouble minTemp = weatherPerHours.stream()
                .mapToDouble(WeatherPerHour::temp_c)
                .min();
        if (minTemp.isEmpty()) {
            throw new MissingDataException(TEMP_EXCEPTION_MESSAGE);
        }
        return minTemp.getAsDouble();
    }

    static double findMaxPrecip(List<WeatherPerHour> weatherPerHours) throws MissingDataException {
        OptionalDouble maxPrecip = weatherPerHours.stream()
                .mapToDouble(WeatherPerHour::precip_mm)
                .max();
        if (maxPrecip.isEmpty()) {
            throw new MissingDataException(PRECIP_EXCEPTION_MESSAGE);
        }
        return maxPrecip.getAsDouble();
    }

    static double findMaxWind(List<WeatherPerHour> weatherPerHours) throws MissingDataException {
        OptionalDouble maxWind = weatherPerHours.stream()
                .mapToDouble(WeatherPerHour::wind_kph)
                .max();
        if (maxWind.isEmpty()) {
            throw new MissingDataException(WIND_EXCEPTION_MESSAGE);
        }
        return maxWind.getAsDouble();
    }

    static boolean checkWillItRain(List<WeatherPerHour> weatherPerHours) {
        Optional<Integer> willItRain = weatherPerHours.stream()
                .map(WeatherPerHour::will_it_rain)
                .filter(rain -> rain == FLAG_TRUE)
                .findAny();
        return willItRain.isPresent();
    }

    static boolean checkWillItSnow(List<WeatherPerHour> weatherPerHours) {
        Optional<Integer> willItSnow = weatherPerHours.stream()
                .map(WeatherPerHour::will_it_snow)
                .filter(snow -> snow == FLAG_TRUE)
                .findAny();
        return willItSnow.isPresent();
    }

    static double calculateSensedTemp(double temp, double wind) {
        return 33 + (0.478 + 0.237 * sqrt(wind) - 0.0124 * wind) * (temp - 33);
    }
}
```
Klasa wykonująca operacje wykorzystywane w klasie WeatherResponseConverter.

### Klasy pomocnicze
#### ResponseHolder
```
public class ResponseHolder {
    private static WeatherResponse lastResponse = null;

    public static void updateLastResponse(WeatherResponse weatherResponse) {
        lastResponse = weatherResponse;
    }

    public static WeatherResponse getLastResponse() {
        return lastResponse;
    }

    public static boolean isLastResponse() {
        return lastResponse != null;
    }
}
```
Klasa trzymająca stan ostatniego WeatherResponse.

