
# MyMovieApp

MyMovieapp is an application to display various films from MovieDB Api such as films based on genre, popular films, now playing films, top rated films, and upcoming films. This application has an application detail feature which contains movie summaries, audience reviews, and trailers from YouTube. In this application there is a login feature that functions to access the film details feature and reminder notifications for watching movies

## Screenshots

![App Screenshot](https://docs.google.com/uc?id=1AxCjvNGRtY8Px4Jj-pB62-PBC3HDIDeL)
![App Screenshot](https://docs.google.com/uc?id=1JBC6X4a8JR-jzd42AZ1tcoq8EnjcWfCz)
![App Screenshot](https://docs.google.com/uc?id=1SjCIUVnz4WmJ0n7M2F6JFlWpwwXfelg7)

## Tech Stack & Open Source Libraries

**Single Activity Pattern:** The Single Activity Pattern is an architectural approach used in Android app development where an application contains only one main activity responsible for managing multiple fragments or screens. 

**MVVM:** MVVM is a design pattern separates an application into three components: the Model (data and business logic), the View (user interface elements), and the ViewModel (mediator between Model and View, handling data presentation and user interactions).

**Lifecyle:** Lifecycle in Android refers to the series of states and events that an app component, such as an Activity or Fragment, goes through during its existence. This includes creation, starting, stopping, and destruction.

**Firebase Auth:** Firebase Authentication is a service provided by Google's Firebase platform that simplifies user authentication and identity management in mobile and web applications. It offers various authentication methods such as email/password, social media logins, and multi-factor authentication to enhance the security of user accounts.

**Firebase Analytics:** Firebase Analytics is part of the Firebase suite and is a mobile and web app analytics solution. It provides developers and marketers with valuable insights into user behavior, app usage, and engagement. 

**ViewModel:** In Android architecture components, a ViewModel is a class designed to store and manage UI-related data in a lifecycle-aware manner.

**Paging:** Paging is a library in Android that helps with efficiently loading and displaying large sets of data from a data source, such as a database or network.

**Navigation:** Navigation in the context of Android app development refers to the process of moving between different screens or destinations within an app.

**Retrofit2:** Retrofit2 is a popular networking library for Android that simplifies the process of making network requests and handling API responses.

**Koin:** Koin is a lightweight dependency injection framework for Kotlin-based Android applications.

**Glide:** Glide is an image loading and caching library for Android that helps with efficiently loading and displaying images from various sources.

**YoutubeMediaPlayer:** YoutubeMediaPlayer is a library for show video from youtube id.

**Lottie:** Lottie is a library that allows you to render animations and vector graphics in an Android app.

**SweetToast:** SweetToast refers to a custom user interface component or library, often used in Android app development, that provides visually appealing and user-friendly toast messages.

**SweetDialog:** SweetDialog is a term used to describe a custom dialog box or popup library often utilized in mobile app development. These dialogs are designed to have a visually pleasing and user-friendly appearance, often with animations and customizable features, to enhance the presentation of important messages or user interactions.

**ImageSlider:** An ImageSlider is a user interface element or component commonly used in mobile app development to display a slideshow of images or content. It allows users to swipe or navigate through a series of images.

## Features

- Login User
- Register User
- Notification For Watching Movies
- Popular Movies
- Now Playing Movies
- Top Rated Movies
- Upcoming Movies
- Genre Movies
- Movies by Genre
- Detail Movies
- Reviews User About Movies
- Trailer Movies from Youtube
  
## How To Run App

Add your [TMDB](https://www.themoviedb.org/) API key in the `local.properties` file:

```bash
TOKEN_KEY={YOUR_API_KEY}
```
    
