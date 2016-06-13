(ns umm-data-parser.core-test
  (:require [clojure.test :refer :all]
            [umm-data-parser.core :refer :all]
            [clojure.pprint :as pp]))

(deftest a-test
  (testing "FIXME, I fail."
    (let [csv (read-file)
          parsed (csv-to-map csv)
          grouped (create-groups parsed)
          mapped (map compact grouped)
          f (take 5 mapped)]
      (pp/pprint f)
      (is (= 1 1)))))

(deftest event-duration-parsing-test
  (testing "FIXME, I fail."
    (let [ event-duration "1 day, 7:00:00"
           event-duration-2 "2 days, 12:00:00"
           event-duration-3 "broken"
           numeric (event-duration-to-hours event-duration)
           numeric2 (event-duration-to-hours event-duration-2)
           numeric3 (event-duration-to-hours event-duration-3)]
      (is (= numeric 31))
      (is (= numeric2 60))
      (is (= numeric3 nil)))))
