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
