(ns aoc.2024.day07-test
  (:require [clojure.test :refer :all]
            [aoc.2024.day01 :as camel-cards :refer :all]))

(deftest parse-input-with-joker-test
  (testing "Parse input with Joker"
    (is (= [{:hand "32T3K" :bid 765 :type :one-pair}
            {:hand "T55J5" :bid 684 :type :four-of-a-kind}
            {:hand "KK677" :bid 28 :type :two-pair}
            {:hand "KTJJT" :bid 220 :type :four-of-a-kind}
            {:hand "QQQJA" :bid 483 :type :four-of-a-kind}] (camel-cards/calibration-value )))))
