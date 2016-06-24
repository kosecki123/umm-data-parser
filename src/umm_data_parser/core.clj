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
  (pmap #(zipmap (map keyword head) %1) lines))

(defn event-duration-to-hours [{start :event_start end :event_stop}]
  (when-not (or (blank? start) (blank? end))
    (let [start (tf/parse start)
          end (tf/parse end)]
      (when (tc/before? start end)
        (tc/in-hours (tc/interval start end))))))

(defn create-groups [data]
  (group-by #(select-keys % [:company :series]) data))

(defn assoc-durations [first-message last-message reestimation-nr]
  (let [
        start-duration (event-duration-to-hours first-message)
        end-duration (event-duration-to-hours last-message)]
      (assoc first-message :event_duration_num start-duration :event_duration_num_last end-duration :reestimation_nr reestimation-nr)))

(defn compact [[group & data]]
  (let [ data (reverse (first data))
         first-message (first data)]
    (if (> (count data) 1)
        (mapv assoc-durations data (rest data) (iterate inc 1))
        (assoc-durations first-message first-message 0))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (time
    (let [raw (read-file)
          mapped (csv-to-map raw)
          grouped (create-groups mapped)
          compacted (flatten (pmap compact grouped))
          head (conj (first raw) "event_duration_num" "event_duration_num_last" "reestimation_nr")]
        (println "Processed " (count compacted) " rows")
        (write-file head compacted)
        (shutdown-agents))))
