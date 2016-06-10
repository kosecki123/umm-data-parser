(ns umm-data-parser.core-test
  (:require [clojure.test :refer :all]
            [umm-data-parser.core :refer :all]
            [clojure.pprint :as pp]))

(deftest a-test
  (testing "FIXME, I fail."
    (let [csv (read-file)
          parsed (csv-map csv)
          grouped (create-groups parsed)
          f (take 5 grouped)
          p (extract-data f)]
      (pp/pprint p)
      (is (= p 1)))))
