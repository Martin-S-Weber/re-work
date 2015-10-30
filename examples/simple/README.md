# Simple Example App for re-work

TODO: need to show a re-work simple app. Maybe the example app?

Run "`lein do clean, figwheel`" in a terminal to compile the app, and then open `http://localhost:3449/example.html`.

Any changes to ClojureScript source files (in `src`) will be reflected in the running page immediately (while "`lein figwheel`" is running).

Run "`lein do clean, with-profile prod compile`" to compile an optimized version, and then open `resources/public/example.html`.

Original reagent example code found at https://github.com/reagent-project/reagent
