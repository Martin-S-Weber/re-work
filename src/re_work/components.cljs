(ns re-work.components
  (:require 
    [com.stuartsierra.component :as component]
    [re-work.workers :as workers]))


;;; public API
;; facade for accessing & managing the worker in the main js context
(defrecord WorkerFacade [create-worker worker-opts]
  component/Lifecycle
  (start [component]
    (let [w (create-worker (:worker-opts component)
          ;; send worker init signal
          ;;
          ;; wait for init reply from worker
          ;;
          ;; assoc communication machinery into component
          component' (assoc component ::comm-fu {}) ]
      component'))
  (stop [component]
    (let [wm (::comm-fu component)
          ;; send worker stop signal
          ;;
          ;; wait for stop reply from worker
          ;;
          ;; dissoc state
          component' (dissoc component ::comm-fu)]
      component')))

;; component representing the other side of the main js context worker facade,
;; i.e., this component has to run in the worker for re-work to work.
(defrecord Worker []
  component/Lifecycle
  (start [component]
    component)
  (stop [component]
    component))


;; private worker pool
(defn web-worker [worker-opts]
  (->WorkerFacade workers/launch-web-worker worker-opts))

;; shared worker pooltop level component
;; warning:
;; - shared worker portability / availability
(defn shared-worker [worker-opts]
  (->WorkerFacade workers/launch-shared-worker worker-opts))

;; service worker pool
;; warning:
;; - service worker portability / availability
;; - lifecycle handling: http://www.w3.org/TR/2015/WD-service-workers-20150205/#service-worker-lifetime
(defn service-worker [worker-opts]
  (->WorkerFacade workers/launch-service-worker worker-opts))
