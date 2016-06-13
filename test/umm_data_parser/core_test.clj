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
    (let [ start "2015-07-16 08:00:00"
           end "2015-07-17 16:00:00"
           result (event-duration-to-hours {:event_start start :event_stop end})]
      (is (= result 32)))))

(deftest event-duration-parsing-test
  (testing "FIXME, I fail."
    (let [ start "2015-07-16 08:00:00"
           end ""
           result (event-duration-to-hours {:event_start start :event_stop end})]
      (is (= result 32)))))
