(ns re-work.subs
 (:require
   [reagent.ratom  :refer [make-reaction]]
   [re-work.db    :refer [app-db]]
   [re-work.utils :refer [first-in-vector warn error]]))


;; maps from handler-id to handler-fn
(def ^:private key->fn (atom {}))


(defn clear-handlers!
  "Unregisters all subscription handlers"
  []
  (reset! key->fn {}))


(defn register
  "Registers a handler function for an id"
  [key-v handler-fn]
  (if (contains? @key->fn key-v)
    (warn "re-frame: overwriting subscription-handler for: " key-v))   ;; allow it, but warn.
  (swap! key->fn assoc key-v handler-fn))


(defn subscribe
  "Returns a reagent/reaction which observes a part of app-db"
  [v]
  (let [key-v       (first-in-vector v)
        handler-fn  (get @key->fn key-v)]
    (if (nil? handler-fn)
      (error "re-frame: no subscription handler registered for: \"" key-v "\".  Returning a nil subscription.")
      (handler-fn app-db v))))

