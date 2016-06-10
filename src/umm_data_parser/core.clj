(ns umm-data-parser.core
  (:gen-class)
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.pprint :as p]))

(defn read-file []
  (with-open [in-file (io/reader "resources/umm.csv")]
    (doall
      (csv/read-csv in-file))))

(defn csv-map [[head & lines]]
  (map #(zipmap (map keyword head) %1) lines))

(defn create-groups [data]
  (group-by #(select-keys % [:company :series]) data))

(defn compact [[group & data]]
  (let [joined (map #(select-keys % [:decided :event_duration]) (first data))]
    [group joined]))

(defn extract-data [groups]
    (map compact groups))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file (read-file)
        mapped (csv-map file)]))
