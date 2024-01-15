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
<div className="main-container">
    <form className="latlong-form">
    <h1>Weather Form</h1>
    {inputComponents}
    <div className="latlong-form-buttons">
        <button type="submit" onClick={handleAddClick} className={`latlong-form-${inputCount === 5 ? 'disabled' : 'add'} material-symbols-outlined`}>add_circle</button>
        <button type="submit" onClick={handleRemoveClick} className={`latlong-form-${inputCount === 1 ? 'disabled' : 'remove'} material-symbols-outlined`}>cancel</button>
    </div>
    <div className="latlong-save-trip-container">
        <label htmlFor="save-checkbox">
        Save trip
        <input type="checkbox" id="save-checkbox" onChange={handleCheckboxClick} className="latlong-save-checkbox" />
        </label>
        <input disabled={!saveTrip} onChange={handleNameInputChange} className="latlong-save-input" placeholder="trip name..." />
    </div>
    {saveTrip && tripName === '' && <p className="form-error">* Invalid trip name</p>}
    <button type="submit" onClick={formWeatherRequest} className="latlong-form-submit">Get Weather ⛅</button>
    {showSubmitError && <p className="form-error">* Invalid data in the form</p>}
    </form>
</div>
```
Komponent 2 przyciski do dodawania i usuwania pól formularza, zawiera również input i checkbox do opcjonalnego zapisywania wycieczki do ulubionych wycieczek. Po kliknięciu przycisku przekierowuje nas do widoku pogody w danym miejscu (do ścieżki "/trips/:id" lub "/weather" w zależności od tego czy chcieliśmy zapisać wycieczkę).
```
const handleAddClick = (event) => {
    event.preventDefault();
    if (inputCount < 5) {
      setInputComponents([...inputComponents,
        <FormInput key={inputCount} id={inputCount} handleChange={handleInputChange} />]);
      setData((arr) => [...arr, { coordinates: { lat: '', lng: '' }, validData: false }]);
      setInputCount(inputCount + 1);
    }
  };

  const handleRemoveClick = (event) => {
    event.preventDefault();
    if (inputCount > 1) {
      setInputComponents((arr) => arr.slice(0, -1));
      setData((arr) => arr.slice(0, -1));
      setInputCount(inputCount - 1);
    }
  };

  const handleCheckboxClick = () => {
    setSaveTrip(!saveTrip);
  };

  const handleNameInputChange = (event) => {
    setTripName(event.target.value);
  };

  const checkData = () => data.find((element) => element.validData === false) || (saveTrip && tripName === '');

  const formWeatherRequest = (event) => {
    event.preventDefault();
    if (checkData()) {
      clearTimeout(timeoutId);
      setShowSubmitError(true);
      setTimeoutId(setTimeout(() => { setShowSubmitError(false); }, 3000));
    } else if (saveTrip) {
      TripService.addNewTrip(
        { name: tripName, locations: data.map((element) => element.coordinates) },
      )
        .then((responseData) => { navigate(`/trips/${responseData.id}`); })
        .catch((error) => console.log(error));
    } else {
      sessionStorage.setItem('lastTrip', JSON.stringify(data.map((element) => element.coordinates)));
      WeatherService.getWeatherByCoordinates(data.map((element) => element.coordinates))
        .then(() => {
          navigate('/weather');
        })
        .catch((error) => console.log(error));
    }
  };
```
Funkcje do obsługiwania zmiany inputów oraz obsługi przycisków. *formWeatherRequest* służy do wysyłania do backendu naszego zapytania. Sprawdza też ona czy końcowe dane są poprawne. Funkcje *handleAddClick* oraz *handleRemoveClick* dodają/usuwają kolejne pola formularza.
#### FormInput
Kod JSX zwracany przez komponent
```
<>
    <h3>
    Location #
    {id + 1}
    </h3>
    <label htmlFor="lattitude">
    Lattitude
    <input onChange={lattitudeValidation} name="lattitude" id="lattitude" type="text" className="latlong-form-input" />
    </label>
    {showErrorLattitude && lattitude !== '' && <p className="form-error">* Lattitude must be between -90 and 90</p>}
    <label htmlFor="longitude">
    Longitude
    <input onChange={longitudeValidation} name="longitude" id="longitude" type="text" className="latlong-form-input" />
    </label>
    {showErrorLongitude && longitude !== '' && <p className="form-error">* Longitude must be between -180 and 180</p>}
</>
```
Komponent zawiera dwa pola input, które są walidowane przez odpowiednie funkcje, które pokazują, jeżeli wpiszemy błędne dane do formularza
```
useEffect(() => {
handleChange(
    {
    coordinates: {
        lat: lattitude,
        lng: longitude,
    },
    validData: !(showErrorLattitude || showErrorLongitude),
    },
    id,
    );
}, [lattitude, longitude, showErrorLattitude, showErrorLongitude]);

const lattitudeValidation = (event) => {
    const val = event.target.value;
    setLattitude(val);
    if (/^\[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$/.test(val)) {
        setShowErrorLattitude(true);
    }
    const floatVal = parseFloat(val);
    if (floatVal < 90 && floatVal > -90) {
        setShowErrorLattitude(false);
    } else {
        setShowErrorLattitude(true);
    }
};

const longitudeValidation = (event) => {
    const val = event.target.value;
    setLongitude(val);
    if (/^\[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)$/.test(val)) {
        setShowErrorLongitude(true);
    }
    const floatVal = parseFloat(val);
    if (floatVal < 180 && floatVal > -180) {
        setShowErrorLongitude(false);
    } else {
        setShowErrorLongitude(true);
    }
};
```
Funkcje *longitudeValidation* oraz *lattitudeValidation* służą do walidacji danych, a w Hooku *useEffect* jest obsługa aktualizowania danych w parent component jakim jest *Form*
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
  const [weatherTripName, setWeatherTripName] = useState('');
  const [savedTrip, setSavedTrip] = useState(false);

  const [tripName, setTripName] = useState('');
  const [tripNameError, setTripNameError] = useState(false);

  const location = useLocation();
  const { id } = useParams();

  const navigate = useNavigate();

  useEffect(() => {
    const fetchData = async () => {
      try {
        if (location.pathname.includes('trips')) {
          TripService.getTripById(id)
            .then((data) => {
              setWeatherTripName(data.name);
              setSavedTrip(true);
              WeatherService.getWeatherByCoordinates(data.locations)
                .then((weatherData) => {
                  setWeatherInfo(DataConvertService.getWeatherInfo(weatherData));
                }).catch((error) => console.error(error.message));
            }).catch((error) => console.error(error.message));
        } else {
          WeatherService.getCurrentWeather()
            .then((data) => {
              setWeatherInfo(DataConvertService.getWeatherInfo(data));
            }).catch((error) => console.error(error.message));
        }
      } catch (error) {
        console.error(error.message);
      }
    };
    fetchData();
  }, [location, id]);

  const handleNameChange = (event) => {
    setTripName(event.target.value);
    setTripNameError(false);
  };

  const handleSaveButton = () => {
    if (tripName === '') {
      setTripNameError(true);
    } else {
      TripService.addNewTrip({ locations: JSON.parse(sessionStorage.lastTrip), name: tripName })
        .then((data) => { navigate(`/trips/${data.id}`); })
        .catch((error) => console.error(error.message));
    }
  };

  const handleDeleteButton = () => {
    TripService.deleteTrip(id)
      .then(() => navigate('/trips'))
      .catch((error) => console.error(error));
  };

  if (!weatherInfo) {
    return null;
  }

  return (
    <div className="main-container">
      <div className="weather-container">
        {weatherInfo && (
        <WeatherOverview
          classNames={weatherInfo.classNames}
          img={weatherInfo.img}
          tempC={weatherInfo.tempC}
          condition={weatherInfo.condition}
          locations={weatherInfo.locations}
          tripName={weatherTripName}
        />
        )}
        {!savedTrip
                && (
                <div className="save-trip-input">
                  <label htmlFor="trip-name">
                    Trip name:
                    <input id="trip-name" name="trip-name" onChange={handleNameChange} />
                  </label>
                  <button type="submit" onClick={handleSaveButton}>Save</button>
                </div>
                )}
        {savedTrip
                && <button type="submit" className="delete-button" onClick={handleDeleteButton}>Delete</button>}
        {tripNameError && <p className="form-error">* Invalid trip name</p>}
      </div>
    </div>
  );
}

```
Komponent pobiera dane z serwisu i przekazuje je do komponentu *WeatherOverview*. W zależności od tego czy jest to zapisana wycieczka czy nie to odnosi się do innego endpointu na backendzie.
#### WeatherOverview
```
export default function WeatherOverview({
  tripName, classNames, img, tempC, condition, locations,
}) {
  return (
    <>
      {tripName !== '' && (
        <div className="trip-name">
          Trip Name:
          {' '}
          <h2>{tripName}</h2>
        </div>
      )}
      <div className="weather-overview-container">

        <div className="thermometer-container">
          <div className="logo">
            <div className={`bar ${classNames}-bar`} />
            <div className={`circle ${classNames}-circle`} />
          </div>
        </div>
        <div className="information-container">
          <h2 className="weather-city-name">{locations}</h2>
          <span className="weather-img-container">
            {img.map((weatherImg) => <img src={weatherImg} alt="conditions" className="weather-img-present" />)}
          </span>
          <h3 className="weather-temperature">
            {tempC}
            &#176;C
          </h3>
          <h4 className="weather-condition">{condition}</h4>
        </div>
      </div>
    </>
  );
}
```
Komponent przyjmuje od *WeatherView* poprzez *props* dane na temat pogody i lokalizacji pobrane wcześniej z serwisu. Komponent zajmuje się wyświetlaniem tych danych
#### Navbar
```
export default function Navbar() {
  const navigate = useNavigate();

  return (
    <nav className="nav-container">
      <div className="left-nav" role="button" onClick={() => navigate('/')} onKeyDown={() => navigate('/')} tabIndex={0}>
        <img src={logo} alt="Sun behind the clouds in a circle" className="nav-icon" />
        <h2 className="nav-title">WeatherApp</h2>
      </div>
      <div role="button" className="right-nav" onClick={() => navigate('/trips')} onKeyDown={() => navigate('/trips')} tabIndex={0}>
        <h3>My Trips</h3>
      </div>
    </nav>
  );
}
```
Komponent pozwala na przekierowanie do *My Trips* (do widoku zapisanych wycieczek) lub do głównego formularza (klikając na logo aplikacji)
#### Trips
```
export default function Trips() {
  const [tripsList, setTripsList] = useState([]);
  const [tripsFetched, setTripsFetched] = useState(false);

  const [refreshComponent, setRefreshComponent] = useState(false);

  useEffect(
    () => {
      TripService.getTrips()
        .then((data) => {
          setTripsList(data);
          setTripsFetched(true);
        }).catch(() => {
          setTripsFetched(true);
        });
    },
    [refreshComponent],
  );

  const refreshParent = () => {
    setRefreshComponent(!refreshComponent);
  };

  if (!tripsFetched) {
    return null;
  }
  if (tripsFetched && !tripsList.length) {
    return (
      <div className="main-container">
        <div className="trips-container">
          No saved trips has been found.
        </div>
      </div>
    );
  }

  return (
    <div className="main-container">
      <div className="trips-container">
        {tripsList.map((trip) => (
          <TripElement
            id={trip.id}
            key={trip.id}
            name={trip.name}
            refreshParent={refreshParent}
          />
        ))}
      </div>
    </div>
  );
}
```
Komponent pobiera za pomocą *TripService* zapisane wycieczki i wyświetla je w postaci komponentów *TripElement*
#### TripElement
```
export default function TripElement({ id, name, refreshParent }) {
  const navigate = useNavigate();

  const handleShowButtonClick = () => {
    navigate(`/trips/${id}`);
  };

  const handleDeleteButtonClick = () => {
    TripService.deleteTrip(id)
      .then(() => refreshParent())
      .catch((error) => console.error(error));
  };

  return (
    <div className="trip-container-element">
      <h3>{name}</h3>
      <div className="trip-container-buttons">
        <button type="submit" className="trip-container-element-delete" onClick={handleDeleteButtonClick}>Delete</button>
        <button type="submit" className="trip-container-element-show" onClick={handleShowButtonClick}>Show</button>
      </div>
    </div>
  );
}
```
Komponent pozwalający na przejście do wycieczki o podanej nazwie lub jej usunięcie z zapisanych wycieczek.
### Serwisy
#### WeatherService
```
const WeatherService = {
  getWeatherByCoordinates: async (coordinates) => {
    try {
      const response = await axios.post(`${enpointURL}/weather`, coordinates);
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
```
Serwis służący do komunikacji z backendem. funkcja *getWeatherByCoordinates* wysyła koordynaty pobrane od użytkownika, korzystając z endpointa */weather*, natomiast funkcja getCurrentWeather pobiera informacje korzystając z endpointa */weather/current* i je zwraca
#### TripService
```
const TripService = {
  getTrips: async () => {
    try {
      const response = await axios.get(`${enpointURL}/trips`);
      return response.data;
    } catch (error) {
      throw new Error('Failed to fetch trip of id:');
    }
  },

  getTripById: async (id) => {
    try {
      const response = await axios.get(`${enpointURL}/trips/${id}`);
      return response.data;
    } catch (error) {
      throw new Error(`Failed to fetch trip of id: ${id}`);
    }
  },

  addNewTrip: async (trip) => {
    try {
      const response = await axios.post(`${enpointURL}/trips`, trip);
      return response.data;
    } catch (error) {
      throw new Error('Failed to post new Trip');
    }
  },

  deleteTrip: async (id) => {
    try {
      const response = await axios.delete(`${enpointURL}/trips/${id}`);
      return response.data;
    } catch (error) {
      throw new Error('Failed to delete Trip');
    }
  },
};
```
Serwis do obsługiwania operacji CRUD (bez Update) na zapisanych wycieczkach.
#### DataConvertService
```
const DataConvertService = {
  getTemperature: (sensedTemp) => sensedTemp.toLowerCase(),
  getPrecipitation: (precipitation) => {
    const imgArray = [];
    precipitation.forEach((element) => {
      if (element === 'RAIN') {
        imgArray.push(rainLogo);
      } else if (element === 'SNOW') {
        imgArray.push(snowLogo);
      }
    });
    if (imgArray.length === 0) {
      imgArray.push(sunLogo);
    }
    return imgArray;
  },
  getWind: (wind, imgArray) => {
    if (wind) {
      imgArray.push(windLogo);
    }
  },
  getMud: (mud, imgArray) => {
    if (mud) {
      imgArray.push(mudLogo);
    }
  },
  getLocations: (locations) => locations.join(', '),
  getWeatherInfo: (data) => {
    const imgArray = DataConvertService.getPrecipitation(data.precipitation);
    DataConvertService.getWind(data.isWindy, imgArray);
    DataConvertService.getMud(data.isMuddy, imgArray);
    const classNames = DataConvertService.getTemperature(data.temperatureLevel);
    const weatherData = {
      img: imgArray,
      classNames,
      locations: DataConvertService.getLocations(data.locations),
      tempC: Math.round(data.sensedTemp),
      condition: data.temperatureLevel,
    };
    return weatherData;
  },
};
```
Serwis do przekształcenia danych pobranych z backendu, tak aby były gotowe do wyświetlenia (np. przypisanie odpowiednich zdjęć i zmiana tekstu na odpowiednią wielkość)
## Backend
### Kontrolery
#### WeatherController 
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

    @PostMapping("")
    public ResponseEntity<WeatherResponseConverted> findWeather(@RequestBody List<WeatherRequestDto> weatherRequestsDto) {
        List<WeatherRequest> weatherRequests = weatherRequestsDto.stream()
                .map(weatherRequestDto -> new WeatherRequest(weatherRequestDto.lat(), weatherRequestDto.lng()))
                .toList();
        Optional<WeatherResponseConverted> weatherResponse;
        try {
            weatherResponse = service.findWeather(weatherRequests);
        } catch (CallToApiWentWrongException ignored) {
            return new ResponseEntity<>(BAD_GATEWAY);
        }

        return weatherResponse
                .map(wr -> new ResponseEntity<>(wr, OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @GetMapping("/current")
    public ResponseEntity<WeatherResponseConverted> getLastWeatherResponse() {
        if (!ResponseHolder.isLastResponse()) {
            return new ResponseEntity<>(NOT_FOUND);
        }
        return new ResponseEntity<>(ResponseHolder.getLastResponse(), OK);
    }
}
```
Klasa kontroler, zawierająca endpointy do komunikacji z frontendem
##### findWeather
```
@PostMapping("")
public ResponseEntity<WeatherResponseConverted> findWeather(@RequestBody List<WeatherRequestDto> weatherRequestsDto) {
    List<WeatherRequest> weatherRequests = weatherRequestsDto.stream()
            .map(weatherRequestDto -> new WeatherRequest(weatherRequestDto.lat(), weatherRequestDto.lng()))
            .toList();
    Optional<WeatherResponseConverted> weatherResponse;
    try {
        weatherResponse = service.findWeather(weatherRequests);
    } catch (CallToApiWentWrongException ignored) {
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
##### getLastWeatherResponse
```
@GetMapping("/current")
public ResponseEntity<WeatherResponseConverted> getLastWeatherResponse() {
    if (!ResponseHolder.isLastResponse()) {
        return new ResponseEntity<>(NOT_FOUND);
    }
    return new ResponseEntity<>(ResponseHolder.getLastResponse(), OK);
}
```
Drugi endpoint służy do pobrania ostatniego zapytania, tak aby po odświeżeniu przeglądarki nasze dane nie znikły oraz nie było potrzebne wykonanie ponownego takiego samego zapytania
#### TripController 
```
@RestController
@CrossOrigin()
@RequestMapping("")
public class TripController {
    private final TripService tripService;

    @Autowired
    public TripController(TripService tripService) {
        this.tripService = tripService;
    }

    @PostMapping("/trips")
    public ResponseEntity<Trip> saveNewTrip(@RequestBody Trip trip) {
        Trip savedTrip;
        try {
            savedTrip = tripService.saveTrip(trip);
        } catch (ArgumentToUseInDbIsNullException ignored) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return new ResponseEntity<>(savedTrip, OK);
    }

    @GetMapping("/trips/{id}")
    public ResponseEntity<Trip> getTripById(@PathVariable int id) {
        Optional<Trip> trip;
        try {
            trip = tripService.getTrip(id);
        } catch (ArgumentToUseInDbIsNullException ignored) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return trip
                .map(foundTrip -> new ResponseEntity<>(foundTrip, OK))
                .orElse(new ResponseEntity<>(NOT_FOUND));
    }

    @GetMapping("/trips")
    public ResponseEntity<List<Trip>> getAllTrips() {
        return new ResponseEntity<>(tripService.getTrips(), OK);
    }

    @DeleteMapping("/trips/{id}")
    public ResponseEntity<HttpStatus> deleteTripById(@PathVariable int id) {
        try {
            tripService.deleteTrip(id);
        } catch (ArgumentToUseInDbIsNullException ignored) {
            return new ResponseEntity<>(BAD_REQUEST);
        }
        return new ResponseEntity<>(OK);
    }
}
```
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

#### WeatherHistoryResponse
```
public record WeatherHistoryResponse(
        List<Integer> wasRainyFirstDay,
        List<Integer> wasRainySecondDay
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
@Builder
public record WeatherResponseConverted(
        List<String> locations,
        TemperatureLevel temperatureLevel,
        boolean isWindy,
        List<Precipitation> precipitation,
        double minTemp,
        double sensedTemp,
        double maxPrecip,
        boolean isMuddy
) {
}
```
Klasa zawierająca dane pogodowe wykorzystywane przez frontend.

#### Precipitation
```
public enum Precipitation {
    SNOW,
    RAIN,
    CLEAR
}
```
Klasa zawierająca wartości opisujące warunki dotyczące opadów atmosferycznych.

#### TemperatureLevel
```
public enum TemperatureLevel {
    FREEZING,
    COLD,
    CHILLY,
    WARM,
    HOT;

    public static TemperatureLevel determineTemperatureLevel(double sensedTemp) {
        if (sensedTemp < -3) {
            return FREEZING;
        }
        if (sensedTemp < 3) {
            return COLD;
        }
        if (sensedTemp < 10) {
            return CHILLY;
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
        @JsonProperty("temp_c")
        double tempC,
        @JsonProperty("precip_mm")
        double precipMm,
        @JsonProperty("wind_kph")
        double windKph,
        @JsonProperty("will_it_rain")
        int willItRain,
        @JsonProperty("will_it_snow")
        int willItSnow
) {
}
```
Klasa zawierająca dane dotyczące warunków pogodowych występujących w danej godzinie.

#### Trip
```
@Entity
public class Trip {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Location> locations;

    public Trip(String name) {
        this.name = name;
        locations = new ArrayList<>();
    }

    public Trip() {
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void addLocation(Location location) {
        locations.add(location);
    }
}
```
Klasa zawierająca dane dotyczące zapisanych wycieczek. Obiekty tej klasy są zapisywane w bazie danych. Każda wycieczka posiada swoje id, nazwę oraz listę lokalizacji odwiedzancyh podczas wycieczki.

#### Location
```
@Entity
public class Location {
    @Id
    @GeneratedValue
    private int id;
    private double lat;
    private double lng;

    public Location(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public Location() {
    }
}
```
Klasa zawierająca wpółrzędne danej lokalizacji. Obiekty tej klasy są zapisywane w bazie danych. Jest ona w relacji many to one z klasą Trip.

### Serwisy
#### WeatherService
```
@Service
public class WeatherService {
    private final ForecastService forecastService;
    private final HistoryService historyService;

    @Autowired
    public WeatherService(ForecastService forecastService, HistoryService historyService) {
        this.forecastService = forecastService;
        this.historyService = historyService;
    }

    public Optional<WeatherResponseConverted> findWeather(List<WeatherRequest> weatherRequests) throws IOException {
        List<WeatherForecastResponse> weatherForecastResponses = forecastService.findWeatherForecast(weatherRequests);
        WeatherHistoryResponse weatherHistoryResponse = historyService.findWeatherHistory(weatherRequests);
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime latestTimeForToday = LocalDateTime.of(currentTime.getYear(), currentTime.getMonth(),
                currentTime.getDayOfMonth(), 6, 0);
        try {
            ResponseHolder.updateLastResponse(WeatherResponseConverter.convertWeatherResponse(weatherForecastResponses, weatherHistoryResponse, currentTime, latestTimeForToday));
            return Optional.of(WeatherResponseConverter.convertWeatherResponse(weatherForecastResponses, weatherHistoryResponse, currentTime, latestTimeForToday));
        } catch (MissingDataException e) {
            return Optional.empty();
        }
    }
}
```
Klasa serwis, funkcjonuje jako pośrednik pomiędzy kontrolerem a serwisami podrzędnymi. Przekazuje zapytanie z kontrolera do serwisów i wysyła przetworzoną
informację zwrotną z powrotem do kontrolera.

#### ForecastService
```
@Service
public class ForecastService {
    private static final String URL_FORECAST = "http://api.weatherapi.com/v1/forecast.json";
    private static final String API_KEY = "53416f14f51041f593a122744232711";
    private static final String LOCATION = "location";
    private static final String FORECAST = "forecast";
    private final OkHttpClient client;
    private final Gson gson;

    @Autowired
    public ForecastService(OkHttpClient client, Gson gson) {
        this.client = client;
        this.gson = gson;
    }

    public List<WeatherForecastResponse> findWeatherForecast(List<WeatherRequest> weatherRequests) throws IOException {
        List<WeatherForecastResponse> responses = new ArrayList<>();
        for (WeatherRequest weatherRequest : weatherRequests) {
            Request request = createHttpForecastRequest(weatherRequest);

            try (Response response = client.newCall(request).execute()) {
                if (response.code() == 200) {
                    WeatherForecastResponse valuesFromJson = getValuesFromJson(response);
                    responses.add(valuesFromJson);
                }
            } catch (IOException e) {
                throw new IOException();
            }
        }
        return responses;
    }

    private Request createHttpForecastRequest(WeatherRequest weatherRequest) {
        String params = weatherRequest.lat() + "," + weatherRequest.lng();
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(URL_FORECAST)).newBuilder()
                .addQueryParameter("key", API_KEY)
                .addQueryParameter("q", params)
                .addQueryParameter("days", "2");
        return new Request.Builder()
                .url(builder.build())
                .build();
    }

    private WeatherForecastResponse getValuesFromJson(Response response) throws IOException {
        JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
        String location = json.getAsJsonObject(LOCATION).get("name").getAsString();

        JsonArray weathers = json.getAsJsonObject(FORECAST).getAsJsonArray("forecastday");

        JsonArray todayWeatherJson = weathers.get(0).getAsJsonObject().getAsJsonArray("hour");
        WeatherPerHour[] weatherPerHours = gson.fromJson(todayWeatherJson, WeatherPerHour[].class);
        List<WeatherPerHour> todayWeather = List.of(weatherPerHours);

        JsonArray tomorrowWeatherJson = weathers.get(1).getAsJsonObject().getAsJsonArray("hour");
        List<WeatherPerHour> tomorrowWeather = List.of(gson.fromJson(tomorrowWeatherJson, WeatherPerHour[].class));

        return new WeatherForecastResponse(location, todayWeather, tomorrowWeather);
    }
}
```
Klasa serwis, służy do uzyskania z API informacji dotyczących prognozowanych warunków pogodowych.

#### HistoryService
```
@Service
public class HistoryService {
    private static final String URL_HISTORY = "http://api.weatherapi.com/v1/history.json";
    private static final String API_KEY = "53416f14f51041f593a122744232711";
    private static final String FORECAST = "forecast";
    private final OkHttpClient client;

    @Autowired
    public HistoryService(OkHttpClient client, Gson gson) {
        this.client = client;
    }

    public WeatherHistoryResponse findWeatherHistory(List<WeatherRequest> weatherRequests) throws IOException {
        List<Integer> wasRainyFirstDay = new ArrayList<>();
        List<Integer> wasRainySecondDay = new ArrayList<>();
        LocalDate date = LocalDate.now();

        for (WeatherRequest weatherRequest : weatherRequests) {
            Request request = createHttpHistoryRequest(weatherRequest, date.minusDays(2));
            try (Response response = client.newCall(request).execute()) {
                if (response.code() == 200) {
                    int wasRainy = getRainInformation(response);
                    wasRainyFirstDay.add(wasRainy);
                }
            }
            request = createHttpHistoryRequest(weatherRequest, date.minusDays(1));
            try (Response response = client.newCall(request).execute()) {
                if (response.code() == 200) {
                    int wasRainy = getRainInformation(response);
                    wasRainySecondDay.add(wasRainy);
                }
            }
        }
        return new WeatherHistoryResponse(wasRainyFirstDay, wasRainySecondDay);
    }

    private Request createHttpHistoryRequest(WeatherRequest weatherRequest, LocalDate date) {
        String params = weatherRequest.lat() + "," + weatherRequest.lng();
        String dt = date.toString();
        HttpUrl.Builder builder = Objects.requireNonNull(HttpUrl.parse(URL_HISTORY)).newBuilder()
                .addQueryParameter("key", API_KEY)
                .addQueryParameter("q", params)
                .addQueryParameter("dt", dt);
        return new Request.Builder()
                .url(builder.build())
                .build();
    }

    private int getRainInformation(Response response) throws IOException {
        JsonObject json = JsonParser.parseString(response.body().string()).getAsJsonObject();
        return json.getAsJsonObject(FORECAST)
                .getAsJsonArray("forecastday")
                .get(0).getAsJsonObject().
                getAsJsonObject("day").get("daily_will_it_rain").getAsInt();
    }
}
```
Klasa serwis, służy do uzyskania z API informacji dotyczących warunków pogodowych zarejestrowanych w minionych dniach.

#### TripService
```
@Service
public class TripService {
    private final TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    public List<Trip> getTrips() {
        return tripRepository.findAll();
    }

    public Optional<Trip> getTrip(int id) throws ArgumentToUseInDbIsNullException {
        try {
            return tripRepository.findById(id);
        } catch (IllegalArgumentException ignored) {
            throw new ArgumentToUseInDbIsNullException();
        }
    }

    public Trip saveTrip(Trip trip) throws ArgumentToUseInDbIsNullException {
        try {
            return tripRepository.save(trip);
        } catch (IllegalArgumentException ignored) {
            throw new ArgumentToUseInDbIsNullException();
        }
    }

    public void deleteTrip(int id) throws ArgumentToUseInDbIsNullException {
        Optional<Trip> trip = getTrip(id);
        try {
            trip.ifPresent(tripRepository::delete);
        } catch (IllegalArgumentException ignored) {
            throw new ArgumentToUseInDbIsNullException();
        }
    }
}
```
Klasa funkcjonuje jako pośrednik między klasami TripController i TripRepository. Zawiera metody takie jak: zwrócenie wszystkich wycieczek z bazy, zwrócenie wycieczki o danym id, zapisanie danej wycieczki w bazie i usunięcie danej wycieczki z bazy.

### Repozytoria
#### TripRepository
```
@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {
    Optional<Trip> findByName(String name);
}
```
Interface typu JpaRepository zawiera podstawowe metody do tabeli Trip w bazie danych.
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
@NoArgsConstructor(access = PRIVATE)
public class WeatherResponseConverter {
    private static final double MIN_WINDY_VALUE = 5;

    public static WeatherResponseConverted convertWeatherResponse(
            List<WeatherForecastResponse> weatherForecastResponses,
            WeatherHistoryResponse weatherHistoryResponse,
            LocalDateTime currentTime,
            LocalDateTime latestTimeForToday) throws MissingDataException {

        List<WeatherPerHour> weatherPerHours = prepareDataToAnalysis(
                weatherForecastResponses,
                currentTime,
                latestTimeForToday
        );
        WeatherResponseConvertedBuilder responseBuilder = WeatherResponseConverted.builder();
        responseBuilder.locations(getLocations(weatherForecastResponses));
        responseBuilder.maxPrecip(findMaxPrecip(weatherPerHours));

        boolean willItRain = checkWillItRain(weatherPerHours);
        boolean willItSnow = checkWillItSnow(weatherPerHours);
        responseBuilder.precipitation(prepareListOfPrecipitations(willItRain, willItSnow));

        double maxWind = findMaxWind(weatherPerHours);
        responseBuilder.isWindy(determineIsWindy(maxWind));
        double minTemp = findMinTemp(weatherPerHours);
        responseBuilder.minTemp(findMinTemp(weatherPerHours));
        double sensedTemp = calculateSensedTemp(minTemp, maxWind);
        responseBuilder.sensedTemp(sensedTemp);
        responseBuilder.temperatureLevel(determineTemperatureLevel(sensedTemp));
        boolean isMuddy = checkIsMuddy(
                weatherHistoryResponse,
                weatherForecastResponses,
                currentTime,
                latestTimeForToday
        );
        responseBuilder.isMuddy(isMuddy);
        return responseBuilder.build();
    }

    private static List<String> getLocations(List<WeatherForecastResponse> weatherForecastResponses) {
        return weatherForecastResponses.stream()
                .map(WeatherForecastResponse::location)
                .toList();
    }

    private static List<WeatherPerHour> prepareDataToAnalysis(
            List<WeatherForecastResponse> weatherForecastResponses,
            LocalDateTime currentTime,
            LocalDateTime latestTimeForToday
    ) {
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

    private static List<Precipitation> prepareListOfPrecipitations(boolean willItRain, boolean willItSnow) {
        List<Precipitation> precipitations = new ArrayList<>();
        if (willItRain) {
            precipitations.add(RAIN);
        }
        if (willItSnow) {
            precipitations.add(SNOW);
        }
        if (precipitations.isEmpty()) {
            precipitations.add(CLEAR);
        }
        return precipitations;
    }
}
```
Klasa odpowiedzialna za przetworzenie otrzymanych z serwisów danych do formy wyświetlanej na frontendzie.

#### WeatherCalculator
```
@NoArgsConstructor(access = PRIVATE)
class WeatherCalculator {
    private static final String TEMP_EXCEPTION_MESSAGE = "Data about temperature not found";
    private static final String PRECIP_EXCEPTION_MESSAGE = "Data about precipitation not found";
    private static final String WIND_EXCEPTION_MESSAGE = "Data about wind not found";
    private static final int FLAG_TRUE = 1;

    static double findMinTemp(List<WeatherPerHour> weatherPerHours) throws MissingDataException {
        OptionalDouble minTemp = weatherPerHours.stream()
                .mapToDouble(WeatherPerHour::tempC)
                .min();
        if (minTemp.isEmpty()) {
            throw new MissingDataException(TEMP_EXCEPTION_MESSAGE);
        }
        return minTemp.getAsDouble();
    }

    static double findMaxPrecip(List<WeatherPerHour> weatherPerHours) throws MissingDataException {
        OptionalDouble maxPrecip = weatherPerHours.stream()
                .mapToDouble(WeatherPerHour::precipMm)
                .max();
        if (maxPrecip.isEmpty()) {
            throw new MissingDataException(PRECIP_EXCEPTION_MESSAGE);
        }
        return maxPrecip.getAsDouble();
    }

    static double findMaxWind(List<WeatherPerHour> weatherPerHours) throws MissingDataException {
        OptionalDouble maxWind = weatherPerHours.stream()
                .mapToDouble(WeatherPerHour::windKph)
                .max();
        if (maxWind.isEmpty()) {
            throw new MissingDataException(WIND_EXCEPTION_MESSAGE);
        }
        return maxWind.getAsDouble();
    }

    static boolean checkWillItRain(List<WeatherPerHour> weatherPerHours) {
        Optional<Integer> willItRain = weatherPerHours.stream()
                .map(WeatherPerHour::willItRain)
                .filter(rain -> rain == FLAG_TRUE)
                .findAny();
        return willItRain.isPresent();
    }

    static boolean checkWillItSnow(List<WeatherPerHour> weatherPerHours) {
        Optional<Integer> willItSnow = weatherPerHours.stream()
                .map(WeatherPerHour::willItSnow)
                .filter(snow -> snow == FLAG_TRUE)
                .findAny();
        return willItSnow.isPresent();
    }

    static double calculateSensedTemp(double temp, double wind) {
        return 33 + (0.478 + 0.237 * sqrt(wind) - 0.0124 * wind) * (temp - 33);
    }

    static boolean checkIsMuddy(
            WeatherHistoryResponse weatherHistoryResponse,
            List<WeatherForecastResponse> weatherForecastResponses,
            LocalDateTime currentTime,
            LocalDateTime latestTimeForToday
    ) {
        Optional<Integer> firstDay;
        Optional<Integer> secondDay;

        if (currentTime.isAfter(latestTimeForToday)) {
            firstDay = weatherHistoryResponse.wasRainySecondDay().stream()
                    .filter(e -> e == FLAG_TRUE)
                    .findAny();
            secondDay = weatherForecastResponses.stream()
                    .map(WeatherForecastResponse::todayWeather)
                    .flatMap(Collection::stream)
                    .toList()
                    .stream()
                    .map(WeatherPerHour::willItRain)
                    .filter(rain -> rain == FLAG_TRUE)
                    .findAny();
        } else {
            firstDay = weatherHistoryResponse.wasRainyFirstDay().stream()
                    .filter(e -> e == FLAG_TRUE)
                    .findAny();
            secondDay = weatherHistoryResponse.wasRainySecondDay().stream()
                    .filter(e -> e == FLAG_TRUE)
                    .findAny();
        }
        return (firstDay.isPresent() || secondDay.isPresent());
    }
}
```
Klasa wykonująca operacje wykorzystywane w klasie WeatherResponseConverter.

### Klasy pomocnicze
#### ResponseHolder
```
public class ResponseHolder {
    private static WeatherResponseConverted lastResponse = null;

    private ResponseHolder() {
    }

    public static void updateLastResponse(WeatherResponseConverted weatherResponse) {
        lastResponse = weatherResponse;
    }

    public static WeatherResponseConverted getLastResponse() {
        return lastResponse;
    }

    public static boolean isLastResponse() {
        return lastResponse != null;
    }
}
```
Klasa trzymająca stan ostatniego WeatherResponse.

### Wyjątki
#### ArgumentToUseInDbIsNullException
```
public class ArgumentToUseInDbIsNullException extends Exception {
    private static final String MESSAGE = "Argument used during db operation was null!";

    public ArgumentToUseInDbIsNullException() {
        super(MESSAGE);
    }
}
```

#### CallToApiWentWrongException
```
public class CallToApiWentWrongException extends Exception {
    private static final String MESSAGE = "Executing call to api went wrong";

    public CallToApiWentWrongException() {
        super(MESSAGE);
    }
}
```
#### MissingDataException
```
public class MissingDataException extends Exception {
    public MissingDataException(String message) {
        super(message);
    }
}
```
