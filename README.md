# Aplikacja do sprawdzania pogody

## Frontend

## Backend
### Kontrolery
### WeatherController 
![img_1.png](img_1.png)
Klasa kontroler, zawierająca endpointy do komunikacji z frontendem
#### findWeather
![img.png](img.png)
Endpoint udostępnoiny na porcie localhost:8080/weather, metoda get. Przyjmuje obiekt klasy WeatherRequestDto,
mapuje go do obiektu klasu WeatherRequest i przekazuje do funkcji findWeather z serwisu WeatherService.
Zawraca ReponseEntity z WeatherResponse i statusem HTTP.

### Modele
#### WeatherRequestDto
![img_2.png](img_2.png)
Klasa zawierająca dwie liczby zmiennoprzecinkowe, będące koordynatami interesującej nas lokalizacji. Jest to data transfer
object, co oznacza, że w razie gdy api z front endu się zmieni, nie będziemy musieli ruszać logiki
w naszych serwisach, wystarczy przemapować tą klasę odpowiednio na WeatherRequest. Jest to tylko
klasa przejściowa.

#### WeatherRequest
![img_3.png](img_3.png)
Klasa zawierająca dwie liczby zmiennoprzecinkowe, będące koordynatami interesującej nas lokalizacji. Operujemy na niej
w procesie zdobywania informacji o pogodzie

#### WeatherResponse
![img_4.png](img_4.png)
Klasa zawierająca dane dotyczące lokalizacji oraz pogody. Służy do zwrócenia uzyskanych z API informacji.

### Serwisy
### WeatherService
![img_5.png](img_5.png)
Klasa serwis, jej zadaniem jest połączenie się z API, wysłanie zapytania oraz przetworzenie informacji zwrotnej.
