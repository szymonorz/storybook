# Storybook Frontend

Frontend dla serwisu Storybook.

## Obecna funkcjonalność

- Rejestracja i logowanie użytkownika
- Na stronie głównej są wyświetlane ostatnio zaktualizowane/utworzone książki
- Możliwość tworzenia własnych książek i ich rozdziałów
- Możliwość wyświetlania książek i rozdziałów innych użytkowników
- Możliwość wyszukiwania innych użytkowników i ich twórczości w serwisie

## Wykorzystane technologie

- React 18.3.1
- react-router 7.1.5
- react-hook-form 7.54.2
- react-i18next 15.4.0
- Vite 6.0.11

### react-router

Routing w serwisie został obsłużony za pomocą paczki `react-router`. 

### react-hook-form

W celu swobodnego zarządzania stanem formularzy oraz ich walidacji został wykorzystany pakiet `react-hook-form`.

### i18n

Internalizacja wspiera obecnie tylko dwa języki, polski oraz angielski. 

Pliki `pl.json` oraz `en.json` znajdują się w katalogu `src/locales`.

# Uruchomienie

Frontend wymaga działającego backendu oraz bazy danych. Najlepiej uruchomić wszystko razem poprzez dostarczony
skrypt `docker-compose.yaml`.

W katalogu głównym repozytorium należy uruchomić

```console
git clone https://github.con/szymonorz/storybook
cd storybook
docker compose up --build --no-deps --force-recreate
```