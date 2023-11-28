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
@RequestMapping("weather")
public class WeatherController {
    WeatherService service;

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
public ResponseEntity<WeatherResponse> findWeather(@RequestBody WeatherRequestDto weatherRequestDto) throws IOException {
    WeatherRequest weatherRequest = new WeatherRequest(weatherRequestDto.lat(), weatherRequestDto.lng());
    WeatherResponse weatherResponse = service.findWeather(weatherRequest);
    if (weatherResponse == null) {
        return new ResponseEntity<>(null, NOT_FOUND);
    }
    return new ResponseEntity<>(weatherResponse, OK);
}
```
Endpoint udostępnoiny na porcie localhost:8080/weather, metoda post. Przyjmuje obiekt klasy WeatherRequestDto,
mapuje go do obiektu klasu WeatherRequest i przekazuje do funkcji findWeather z serwisu WeatherService.
Zawraca ReponseEntity z WeatherResponse i statusem HTTP.
#### getLastWeatherResponse
```
@GetMapping("/current")
public ResponseEntity<WeatherResponse> getLastWeatherResponse() {
    if (!service.isLastResponse()) {
        return new ResponseEntity<>(null, NOT_FOUND);
    }
    return new ResponseEntity<>(service.getLastResponse(), OK);
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

### Serwisy
#### WeatherService
```
@Service
public class WeatherService {

    private WeatherResponse lastResponse;

    private final OkHttpClient client = new OkHttpClient();
    public WeatherResponse findWeather(WeatherRequest weatherRequest) throws IOException {
        String url = "http://api.weatherapi.com/v1/current.json";
        String params = weatherRequest.lat() + "," + weatherRequest.lng();
        String apiKey = "53416f14f51041f593a122744232711";
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(url)).newBuilder()
                .addQueryParameter("key", apiKey)
                .addQueryParameter("q", params);
        Request request = new Request.Builder()
                .url(builder.build())
                .build();
        try(Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
                String location = json.getAsJsonObject("location").get("name").getAsString();
                double temp_c = Double.parseDouble(json.getAsJsonObject("current").get("temp_c").getAsString());
                String[] img_path = json.getAsJsonObject("current").getAsJsonObject("condition").get("icon").getAsString().split("/");
                String condition = json.getAsJsonObject("current").getAsJsonObject("condition").get("text").getAsString();
                double precip_mm = Double.parseDouble(json.getAsJsonObject("current").get("precip_mm").getAsString());
                lastResponse = new WeatherResponse(location, temp_c, img_path[img_path.length - 2] + "/" + img_path[img_path.length - 1], condition, precip_mm);
                return lastResponse;
            }
        }
        catch (IOException e) {
            throw new IOException();
        }
        return null;
    }

    public WeatherResponse getLastResponse() {
        return lastResponse;
    }

    public boolean isLastResponse() {
        if (lastResponse == null) {
            return false;
        }
        return true;
    }
}
```
Klasa serwis, jej zadaniem jest połączenie się z API, wysłanie zapytania oraz przetworzenie informacji zwrotnej.
