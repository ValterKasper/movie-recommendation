# Movie suggestions Android app
Android app which is using 
* [RxAndroid](https://github.com/ReactiveX/RxAndroid)
* ~~[EventBus](https://github.com/greenrobot/EventBus)~~
* [Otto](http://square.github.io/otto/)
* ~~[Robolectric](https://github.com/robolectric/robolectric)~~ not now
* [ButterKnife](http://jakewharton.github.io/butterknife/)
* [RetroFit](http://square.github.io/retrofit/)
* [Gradle Retrolambda Plugin](https://github.com/evant/gradle-retrolambda)
* ~~MVP architecture (more [here](http://antonioleiva.com/mvp-android/) or in this [podcast]~~ (http://fragmentedpodcast.com/episodes/11/)).

# Used API services
* Movie suggestions - [tastekid](https://www.tastekid.com/read/api)
* Movie informations - [OMDb](http://www.omdbapi.com/)

# How use
To compile project you need to use your TasteKid API key:
1. Obtain an API key from [TasteKid.com](https://www.tastekid.com/read/api)
2. Insert your API key as string resource with name _taste_kid_api_key_ (e.g. to strings.xml)
