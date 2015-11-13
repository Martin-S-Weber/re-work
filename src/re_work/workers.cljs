(ns re-work.workers
  )

;; note:
;; dedicated workers work as MessagePorts themselves
;; shared ( & service) workers HAVE MessagePorts
;;
(defn post! [worker-port message & transfers]
  )

;;; worker protocols
;; re-frame (event) protocol
;; consists of event receiver for:
;; :event event
;; XXX: state? (I don't think so, but we'll see; state sync should be an :event anyways)
(defn- handle-event [{:keys [worker port] :as opts} event]
  (let [data (.-data event)]
    (when (= ":stop" (aget data 0))
      )))

(defn event-protocol! [{:keys [worker] :as opts}]
  (.removeEventListener worker "message" #(handle-event opts %) false)
  (.addEventListener worker "message" #(handle-event opts %) false))

;; lifecycle protocol
;; consists of event receiver for:
;; component/Lifecycle induced events:
;; :start event; emits :started
;; :stop event; emits :stopped
;;
;; lower-level than that:
;; :terminate; emits :do-it (finalizer signal)
;; 
;; when the handlers are installed, signal availability of the lifecycle protocol handler in the worker
;; by emitting a :started signal
;; 
;; the life cycle protocol should probably live in re-work.components, eh?
;; after all it should side-effect the running system.
;; XXX Should the Lifecycle handling be its own (trivial) component?
(defn- handle-start [{:keys [worker port] :as opts} event]
  (let [data (.-data event)]
    (when (= ":start" (aget data 0))
      ;; XXX side-effect of actually starting the system
      (post! port [":started"])
      )))

(defn- handle-stop [{:keys [worker port] :as opts} event]
  (let [data (.-data event)]
    (when (= ":stop" (aget data 0))
      ;; XXX side-effect of actually stopping the system
      (post! port [":started"])
      )))

;; XXX post :started
(defn lifecycle-protocol! [{:keys [worker] :as opts}]
  (.removeEventListener worker "message" handle-start false)
  (.removeEventListener worker "message" handle-stop false)
  (.addEventListener worker "message" handle-start false)
  (.addEventListener worker "message" handle-stop false))


(defn launch-web-worker [& {:keys [src] :as opts} ]
  (let [worker nil
        ]
    (assoc opts :worker worker :port worker)))

;; shared worker pooltop level component
;; warning:
;; - shared worker portability / availability
(defn launch-shared-worker [& {:keys [src name] :as opts}]
  (let [worker nil
        ]
    (assoc opts :worker worker)))

;; service worker pool
;; warning:
;; - service worker portability / availability
;; - lifecycle handling: http://www.w3.org/TR/2015/WD-service-workers-20150205/#service-worker-lifetime
(defn launch-service-worker [& {:keys [src name] :as opts}]
  (let [worker nil
        ]
    (assoc opts :worker worker)))
