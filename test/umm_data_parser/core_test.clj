(ns umm-data-parser.core-test
  (:require [clojure.test :refer :all]
            [umm-data-parser.core :refer :all]
            [clojure.pprint :as pp]))

(deftest a-test
  (testing "FIXME, I fail."
    (let [csv (read-file)
          parsed (csv-map csv)
          grouped (create-groups parsed)
          p (extract-data grouped)
          fi (filter #(> (count (second %)) 3) p)
          f (take 5 fi)]
      (pp/pprint f)
      (is (= 1 1)))))
