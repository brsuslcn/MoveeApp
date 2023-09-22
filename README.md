# MoveeApp

MoveeApp is a specialized platform tailored for movie and TV series aficionados. Leveraging The Movie Database (TMDb) API, the application provides users with a comprehensive catalog of films and series. The app allows users to curate personalized lists of movies and series, offering in-depth insights into specific titles, actors, and genres. This feature-rich environment empowers users to make well-informed decisions about the content they wish to watch, while also enabling them to compile their own lists of favorites.

The application fully supports Romanian, Turkish, and English languages. It will open in the language set on the phone. If the phone language is set to Turkish, it will open in Turkish; if it's set to Romanian, it will open in Romanian; for all other languages, it will open in English.

## Features

- **Personalized Lists**: Users can add movies and series they want to watch to personalized lists.
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
