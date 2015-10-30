# re-work: re-frame on workers

>  Rouse him, and learn the principle of his activity or inactivity. Force him to reveal himself, so as to find out his vulnerable spots. 

> -- Sun Tzu, The Art of War

## Why Should You Care About re-work?

Either:

1.  You want to develop an [SPA] in [re-frame], and you are doing heavy-weight computations, or
2.  Well, that's it, really.


## re-work

re-frame is a pattern for writing [SPAs] in ClojureScript, using [re-frame] and [component]s.

This repo contains both a **description of this pattern** and a **reference implementation**.

To quote McCoy: "It's re-frame, Jim, but not as we know it".

To build a re-work app, you:
 - do all the things you'd do for a re-frame app (data, query, view, control layer)
 - decide which of the query, view, control layer can be grouped thematically or computationally
 - module them up by providing components for those groups
 - If you've been pure, you won't worry to know that each of these modules will run on their own worker in a separate js context.
 - distribute your components over workers, wire them up, and hope you don't face the CPU hog problem this way.

Features:

1. See [re-frame]
2. Each of your components can be run in a separate worker (```SharedWorker```) context
3. Your main event loop runs in its own separate worker
4. Your [re-frame] view decides the maximum frequency at which it wants to be presented snapshots of your app's state
5. Upon state updates, at most at the specified frequency, a re-frame event is dispatched in the main js context so that the state can be (potentially, that's up to react) rendered.


## Using re-work

It's not released yet. You cannot use it yet.

## Tutorial / Table of Contents

 - [What Problem Does It Solve?](#what-problem-does-it-solve)
 - [Guiding Philosophy](#guiding-philosophy)
 - [Functionality of Your Application](#functionality-of-your-application)
 - [When Events Are All You Care About](#when-events-are-all-you-care-about)
 - [Event Flow](#event-flow)
 - [Dispatching Events](#dispatching-events)
 - [Event Handlers](#event-handlers)
 - [Routing](#routing)
 - [Logging and Debugging](#logging-and-debugging)
 - [Talking To A Server](#talking-to-a-server)
 - [The CPU Hog Problem Revisited](#the-cpu-hog-problem-revisited)
 - [In Summary](#in-summary)
 - [Where Do I Go Next](#where-do-i-go-next)
 - [Licence](#licence)

## What Problem Does It Solve?

When [SPA]s become richer and fatter and more featureful, it is not uncommon that some of their computations begin taking longer and longer. Particularly you quickly become in danger of overstepping the time allocated to you (implicitly) by the [re-frame] core router loop. This is known in re-frame as the "CPU hog" problem. Of course it's nothing new. Usually what we do nowadays if we have to service such a tight loop, we go into parallel universes and compute there.

It also happens that on the runtime environment that [re-frame] targets, we have means available to us to leave the main javascript context with its event handlers, and enter into our own context. This is offered by different types of [WebWorker]s. With [WebWorker]s, we can launch multiple javascript contexts, and essentially communicate via message passing between these share-nothing(-by-default-but-allow-transfer-of-data) environments.

This presents us with technical challenges surrounding the [WebWorker]s themselves (the ones not detailed here are detailed under [Practical Considerations](#practical-considerations)). First and foremost, there is a problem with portability. Yes, only more modern browsers implement [WebWorker]s, but that's not the real problem. The real problem is that not all browser implementations agree on the extent of API available to running workers, in particular, launching further workers (they should support it, but, e.g., chrome doesn't). This documents the necessity to create the workers in the main context, and establish potential data flow dependencies manually. Furthermore, parallel access to our [SPA] now means we are re-launching these parallel workers. This may or may not be what we want to do. Plain [WebWorker]s only allow us the possibility of spawning them again and again. Luckily there are [SharedWorker]s (and [ServiceWorker]s as well, but those are even less implemented than SharedWorkers). These allow getting access to an established javascript context if the identifier of the worker matches up with an existing one.

With the tools picked, we "only" need to establish utility code that launches workers for us in the main javascript context, wires them together as necessary, dispatches to these workers and finally handle replies from the workers as well. This is where re-work comes in. re-work is about providing the utility/glue code that brings these workers together as well as documenting how to approach the problem with components and the presented code.

## Guiding Philosophy

### Functionality of Your Application

### When Events Are All You Care About

## Event Flow

### Dispatching Events

### Event Handlers

### Routing

## Logging And Debugging

## Talking To A Server

## The CPU Hog Problem Revisited

## Practical Considerations

## Licence

Copyright © 2015 Michael Thompson (re-frame)
Copyright © 2015 Martin S. Weber (re-work)

Distributed under The MIT License (MIT) - See LICENSE.txt

[WebWorker]:https://developer.mozilla.org/en-US/docs/Web/API/Worker
[SharedWorker]:https://developer.mozilla.org/en/docs/Web/API/SharedWorker
[component]:https://github.com/stuartsierra/component
[re-frame]:https://github.com/Day8/re-frame
[SPAs]:http://en.wikipedia.org/wiki/Single-page_application
[SPA]:http://en.wikipedia.org/wiki/Single-page_application
[Reagent]:http://reagent-project.github.io/
[Dan Holmsand]:https://twitter.com/holmsand
[Flux]:http://facebook.github.io/flux/docs/overview.html#content
[Hiccup]:https://github.com/weavejester/hiccup
[FRP]:https://gist.github.com/staltz/868e7e9bc2a7b8c1f754
[Elm]:http://elm-lang.org/
[OM]:https://github.com/swannodette/om
[Prismatic Schema]:https://github.com/Prismatic/schema
[datascript]:https://github.com/tonsky/datascript
[Hoplon]:http://hoplon.io/
[Pedestal App]:https://github.com/pedestal/pedestal-app
