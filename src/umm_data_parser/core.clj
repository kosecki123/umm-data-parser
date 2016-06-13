(ns umm-data-parser.core
  (:gen-class)
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.pprint :as p]
            [clj-time.core :as tc]
            [clj-time.format :as tf]
            [clojure.string :refer [blank?]]))

(defn read-file []
  (with-open [in-file (io/reader "resources/umm.csv")]
    (doall
      (csv/read-csv in-file))))

(defn map-to-csv [header rows]
  (let [columns (map keyword header)
        rows (mapv #(mapv % columns) rows)]
    (cons header rows)))

(defn write-file [header rows]
  (with-open [file (io/writer "resources/umm-processed.csv")]
    (csv/write-csv file (map-to-csv header rows))))

(defn csv-to-map [[head & lines]]
  (map #(zipmap (map keyword head) %1) lines))

(defn event-duration-to-hours [{start :event_start end :event_stop}]
  (when-not (and (blank? start) (blank? end))
    (let [start (tf/parse start)
          end (tf/parse end)]
      (if (tc/before? start end)
        (tc/in-hours (tc/interval start end))
        (* -1 (tc/in-hours (tc/interval end start)))))))

(defn create-groups [data]
  (group-by #(select-keys % [:company :series]) data))

(defn assoc-durations [first-message last-message]
  (let [
        start-duration (event-duration-to-hours first-message)
        end-duration (event-duration-to-hours last-message)]
      (assoc first-message :event_duration_num start-duration :event_duration_num_last end-duration)))

(defn compact [[group & data]]
  (let [ [last-message & rest] (first data)]
    (if-not (empty? rest)
            (assoc-durations (last rest) last-message)
            (assoc-durations last-message last-message))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [raw (read-file)
        mapped (csv-to-map raw)
        grouped (create-groups mapped)
        compacted (map compact grouped)
        head (conj (first raw) "event_duration_num" "event_duration_num_last")]
      (write-file head compacted)))
