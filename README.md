# Movie suggestions Android app
The application shows flow of films based on an rating from user. It was founded to try out new approaches and libraries.

## Screenshots
<img src="/images/main.png" width="250"/>
<img src="/images/drawer.png" width="250"/>
<img src="/images/bookmarks.png" width="250"/>

## Libraries
Android app which is using 
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* [ButterKnife](http://jakewharton.github.io/butterknife/)
* [RetroFit](http://square.github.io/retrofit/)
* [Dagger](http://square.github.io/dagger/)
* [Gradle Retrolambda Plugin](https://github.com/evant/gradle-retrolambda)

## Used API services
* Movie suggestions - [tastekid](https://www.tastekid.com/read/api)
* Movie informations - [OMDb](http://www.omdbapi.com/)

## How use
To compile project you need to use your TasteKid API key:

1. Obtain an API key from [TasteKid.com](https://www.tastekid.com/read/api)
2. Insert your API key as string resource with name _taste_kid_api_key_ (e.g. to strings.xml)

