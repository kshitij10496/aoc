(ns aoc.2015.day01-test
  (:require [clojure.test :refer :all]
            [aoc.2015.day01 :as santa :refer :all]))

(deftest count-floor-test
  (testing "example input"
    (is (= 0 (santa/count-floor "")))
    (is (= 0 (santa/count-floor "(())")))
    (is (= 0 (santa/count-floor "()()")))
    (is (= 3 (santa/count-floor "(((")))
    (is (= 3 (santa/count-floor "(()(()(")))
    (is (= 3 (santa/count-floor "))(((((")))
    (is (= -1 (santa/count-floor "())")))
    (is (= -1 (santa/count-floor "))(")))
    (is (= -3 (santa/count-floor ")))")))
    (is (= -3 (santa/count-floor ")())())")))))