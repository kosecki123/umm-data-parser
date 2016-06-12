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

(deftest event-duration-parsing-test
  (testing "FIXME, I fail."
    (let [ event-duration "1 day, 7:00:00"
           event-duration-2 "2 days, 12:00:00"
           numeric (event-duration-to-hours event-duration)
           numeric2 (event-duration-to-hours event-duration-2)]
      (is (= numeric 31))
      (is (= numeric2 60)))))
