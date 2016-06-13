(ns umm-data-parser.data-vis
  (:gen-class)
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.pprint :as p]
            [incanter.core :as i]
            [incanter.stats :as is]
            [incanter.charts :as ic]
            [incanter.io :as iio]))

(def data
  (let [ data (iio/read-dataset "resources/umm-processed.csv" :header true)
         ints (filter integer? (i/$ "event_duration_num" data))]
      (filter #(> 1000 %) ints)))

(is/mean data)
;(i/view data)
(i/view (ic/histogram data :nbins 1000))
