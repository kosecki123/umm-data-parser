(ns umm-data-parser.core
  (:gen-class)
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.pprint :as p]
            [clojure.string :refer [blank?]]))

(defn read-file []
  (with-open [in-file (io/reader "resources/umm.csv")]
    (doall
      (csv/read-csv in-file))))

(defn csv-map [[head & lines]]
  (map #(zipmap (map keyword head) %1) lines))

(defn event-duration-to-hours [event_duration]
  (when-not (blank? event_duration)
    (if-let [ [_ days hours] (re-find (re-matcher #"(\d{1,}) .*, (\d{1,}).*" event_duration))]
      (let [days (Integer. days)
            hours (Integer. hours)]
          (+ (* 24 days) hours))
      nil)))

(defn create-groups [data]
  (group-by #(select-keys % [:company :series]) data))

(defn compact [[group & data]]
  (let [[current-message & rest] (first data)
        get-duration-in-hours #(event-duration-to-hours (:event_duration %))
        first-message (last rest)
        first-message (assoc first-message :event_duration_num (get-duration-in-hours first-message))
        first-message (assoc first-message :event_duration_num_last (get-duration-in-hours current-message))]
        ;item (assoc first :last_event_duration (:event_duration current))]
    [group first-message]))

(defn extract-data [groups]
    (map compact groups))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file (read-file)
        mapped (csv-map file)]))
