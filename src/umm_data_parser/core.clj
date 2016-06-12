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
    (let [ groups (re-find (re-matcher #"(\d{1,}) .*, (\d{1,}).*" event_duration))
           days (Integer. (second groups))
           hours (Integer. (nth groups 2))]
        (+ (* 24 days) hours))))

(defn create-groups [data]
  (group-by #(select-keys % [:company :series]) data))

(defn compact [[group & data]]
  (let [joined (map #(select-keys % [:status :decided :event_start :event_stop :event_duration :unit_names]) (first data))
        with-parsed-duration (map #(update-in % [:event_duration] event-duration-to-hours) joined)]
    [group with-parsed-duration]))

(defn extract-data [groups]
    (map compact groups))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file (read-file)
        mapped (csv-map file)]))

(re-find (re-matcher #"(\d{1,}) .*, (\d{1,}).*" "1 day, 17:00:00"))
