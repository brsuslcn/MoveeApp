# MoveeApp

MoveeApp is a specialized platform tailored for movie and TV series aficionados. Leveraging The Movie Database (TMDb) API, the application provides users with a comprehensive catalog of films and series. The app allows users to curate personalized lists of movies and series, offering in-depth insights into specific titles, actors, and genres. This feature-rich environment empowers users to make well-informed decisions about the content they wish to watch, while also enabling them to compile their own lists of favorites.

The application offers full support for the Romanian, Turkish, and English languages. Upon launch, it will automatically detect and open in the language set on your phone. If your phone's language is set to Turkish, the application will open in Turkish. If it's set to Romanian, the application will open in Romanian. For all other languages, the default language of the application will be English.

## Features

- **Favorites List**: Users can add movies and series when they like movie or series to favorites list.
- **Detailed Information**: Access detailed information about each movie or series, including cast and genres.
- **Responsive Design**: The application features a responsive design to adapt to different screen sizes and devices.
- **Data Security**: Data is encrypted using security crypto and stored securely.

## Installation
Open the local.properties file and add your API key as follows:
API_KEY="your_api_key_here"

## Dependencies
The application uses the following dependencies:
- **Navigation Compose**: Used for navigation within the app.
- **Dagger Hilt**: Used for dependency injection.
- **ViewModel LiveData**: Used for data storage and management.
- **Retrofit**: Used for making API calls and processing responses.
- **Security Crypto**: Used for data encryption.
- **Lottie**: Used for animations.
- **Coil**: Used for loading and displaying images.
- **Paging**: Used for paginating and displaying data.
- **Accompanist System UI Controller**: Used for controlling the Android system UI.
- **Compose Material**: Provides Material Design components for Jetpack Compose.
- **Logging Interceptor**: Used for logging API calls.
- **Core Splash Screen**: Used for managing splash screens.
- **Mockk**: Used for creating mock objects for testing.
- **Kotlin Coroutines Test**: Used for testing Kotlin Coroutines.
- **Paging3 Test**: Used for testing paging functionality.
- **Material3**: Material3 components are used in the application; however, they will be completely restructured in future updates.

## Testing
Some ViewModel tests have been written within the application. However, incomplete tests will be completed in future updates.
